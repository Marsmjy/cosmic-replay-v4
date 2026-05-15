"""验证脚本：导入 5 种 HAR 并执行验证"""
import json
import time
import sys
import requests
from pathlib import Path

BASE_URL = "http://127.0.0.1:8768"
HAR_DIR = Path(r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads")
ENV_ID = "sit2222"

# 5 种 HAR 类型 → (关键词, 用例名前缀)
HAR_TYPES = [
    ("行政组织", "final_行政组织"),
    ("入职申请到确认入职", "final_入职申请"),
    ("新增入职0512测试", "final_新增入职"),
    ("业务模型", "final_业务模型"),
    ("HR基础服务云", "final_用工关系"),
]

def find_latest_har(keyword):
    """找最新的包含关键词的 HAR 文件"""
    matches = sorted(
        [p for p in HAR_DIR.glob(f"*{keyword}*") if p.suffix == ".har"],
        key=lambda p: p.stat().st_mtime,
        reverse=True,
    )
    return matches[0] if matches else None


def upload_har(har_path):
    """上传 HAR 获取预览"""
    with open(har_path, "rb") as f:
        content = f.read()
    filename = har_path.name
    resp = requests.post(
        f"{BASE_URL}/api/har/preview?filename={filename}",
        data=content,
        headers={"Content-Type": "application/octet-stream"},
        timeout=120,
    )
    return resp.json()


def extract_case(har_file, case_name):
    """从预览结果提取 YAML"""
    resp = requests.post(
        f"{BASE_URL}/api/har/extract",
        json={"har_file": har_file, "case_name": case_name},
        timeout=120,
    )
    return resp.json()


def run_case(case_name, env_id):
    """运行用例并等待完成"""
    # 启动运行
    resp = requests.post(
        f"{BASE_URL}/api/cases/{case_name}/run",
        json={"env_id": env_id},
        timeout=30,
    )
    data = resp.json()
    run_id = data.get("run_id")
    if not run_id:
        return {"passed": False, "error": f"启动失败: {data}"}

    # 通过 SSE 监听事件
    import sseclient  # type: ignore
    try:
        sse_resp = requests.get(
            f"{BASE_URL}/api/runs/{run_id}/events",
            stream=True,
            timeout=300,
        )
        client = sseclient.SSEClient(sse_resp)

        result = {"passed": False, "step_count": 0, "step_ok": 0, "errors": [], "duration_s": 0}

        for event in client.events():
            if event.event == "close":
                break
            if event.event == "case_done":
                payload = json.loads(event.data)
                result["passed"] = payload.get("passed", False)
                result["step_count"] = payload.get("step_count", 0)
                result["step_ok"] = payload.get("step_ok", 0)
                result["duration_s"] = payload.get("duration_s", 0)
                break
            if event.event == "step_fail":
                payload = json.loads(event.data)
                err = payload.get("error") or "; ".join(payload.get("errors", [])[:3])
                result["errors"].append(f"[{payload.get('id', '?')}] {err[:150]}")
            if event.event == "case_error":
                payload = json.loads(event.data)
                result["errors"].append(payload.get("error", "unknown error"))
                break

        return result
    except Exception as e:
        # fallback: 没有 sseclient，用轮询
        return run_case_polling(run_id)


def run_case_polling(run_id):
    """轮询方式获取运行结果"""
    result = {"passed": False, "step_count": 0, "step_ok": 0, "errors": [], "duration_s": 0}
    for _ in range(120):  # 最多等 120 秒
        time.sleep(2)
        try:
            resp = requests.get(f"{BASE_URL}/api/run_history/{run_id}", timeout=10)
            if resp.status_code == 200:
                data = resp.json()
                events = data.get("events", [])
                for evt in events:
                    if evt.get("type") == "case_done":
                        payload = evt.get("data", {})
                        result["passed"] = payload.get("passed", False)
                        result["step_count"] = payload.get("step_count", 0)
                        result["step_ok"] = payload.get("step_ok", 0)
                        result["duration_s"] = payload.get("duration_s", 0)
                        return result
                    if evt.get("type") == "step_fail":
                        payload = evt.get("data", {})
                        err = payload.get("error") or "; ".join(payload.get("errors", [])[:3])
                        if err not in [e for e in result["errors"]]:
                            result["errors"].append(f"[{payload.get('id', '?')}] {err[:150]}")
                    if evt.get("type") == "case_error":
                        payload = evt.get("data", {})
                        result["errors"].append(payload.get("error", "unknown"))
                        return result
        except Exception:
            pass
    result["errors"].append("超时：120秒内未完成")
    return result


def main():
    print("=" * 80)
    print("  cosmic-replay 5 HAR 全面验证")
    print("=" * 80)

    # 检查 sseclient
    try:
        import sseclient  # noqa
        has_sse = True
    except ImportError:
        has_sse = False
        print("  [info] sseclient 未安装，将使用轮询方式监听结果")

    results = []

    for keyword, case_name in HAR_TYPES:
        print(f"\n{'─' * 60}")
        print(f"  [{keyword}] 开始处理...")

        # 1. 找 HAR 文件
        har_path = find_latest_har(keyword)
        if not har_path:
            print(f"  [SKIP] 未找到包含 '{keyword}' 的 HAR 文件")
            results.append({
                "type": keyword,
                "case_name": case_name,
                "step_count": 0,
                "result": "SKIP",
                "detail": "未找到 HAR 文件",
            })
            continue
        print(f"  HAR: {har_path.name}")

        # 2. 上传预览
        print(f"  上传预览中...")
        try:
            preview_resp = upload_har(har_path)
            if not preview_resp.get("ok"):
                err = preview_resp.get("error", "unknown")
                print(f"  [FAIL] 预览失败: {err}")
                results.append({
                    "type": keyword, "case_name": case_name,
                    "step_count": 0, "result": "FAIL",
                    "detail": f"预览失败: {err}",
                })
                continue
            har_file = preview_resp["har_file"]
            step_count = len(preview_resp.get("preview", {}).get("steps", []))
            main_form = preview_resp.get("preview", {}).get("main_form_id", "")
            print(f"  预览成功: {step_count} 步, main_form={main_form}")
        except Exception as e:
            print(f"  [FAIL] 预览异常: {e}")
            results.append({
                "type": keyword, "case_name": case_name,
                "step_count": 0, "result": "FAIL",
                "detail": f"预览异常: {e}",
            })
            continue

        # 3. 提取 YAML
        print(f"  提取 YAML: {case_name}...")
        try:
            extract_resp = extract_case(har_file, case_name)
            if not extract_resp.get("ok"):
                err = extract_resp.get("detail", "unknown")
                print(f"  [FAIL] 提取失败: {err}")
                results.append({
                    "type": keyword, "case_name": case_name,
                    "step_count": step_count, "result": "FAIL",
                    "detail": f"提取失败: {err}",
                })
                continue
            print(f"  提取成功: {extract_resp.get('file', '')}")
        except Exception as e:
            print(f"  [FAIL] 提取异常: {e}")
            results.append({
                "type": keyword, "case_name": case_name,
                "step_count": step_count, "result": "FAIL",
                "detail": f"提取异常: {e}",
            })
            continue

        # 4. 运行用例
        print(f"  运行用例 (env={ENV_ID})...")
        try:
            run_result = run_case(case_name, ENV_ID)
            passed = run_result.get("passed", False)
            status = "PASS" if passed else "FAIL"
            s_count = run_result.get("step_count", 0)
            s_ok = run_result.get("step_ok", 0)
            duration = run_result.get("duration_s", 0)
            errors = run_result.get("errors", [])
            print(f"  [{status}] {s_ok}/{s_count} 步成功, 耗时 {duration:.1f}s")
            if errors:
                for err in errors[:3]:
                    print(f"    错误: {err}")

            results.append({
                "type": keyword, "case_name": case_name,
                "step_count": s_count, "step_ok": s_ok,
                "result": status, "duration_s": duration,
                "detail": "; ".join(errors[:3]) if errors else "",
            })
        except Exception as e:
            print(f"  [FAIL] 运行异常: {e}")
            results.append({
                "type": keyword, "case_name": case_name,
                "step_count": step_count, "result": "FAIL",
                "detail": f"运行异常: {e}",
            })

    # 最终报告
    print(f"\n{'=' * 80}")
    print(f"  最终验证报告")
    print(f"{'=' * 80}")
    print(f"{'HAR类型':<15} {'用例名称':<20} {'步骤':>4} {'结果':>6} {'耗时':>8} {'失败详情'}")
    print(f"{'─' * 15} {'─' * 20} {'─' * 4} {'─' * 6} {'─' * 8} {'─' * 30}")
    for r in results:
        duration_str = f"{r.get('duration_s', 0):.1f}s" if r.get('duration_s') else "-"
        step_str = f"{r.get('step_ok', 0)}/{r.get('step_count', 0)}" if r.get('step_count') else "-"
        detail = r.get("detail", "")[:50]
        print(f"{r['type']:<15} {r['case_name']:<20} {step_str:>4} {r['result']:>6} {duration_str:>8} {detail}")

    passed_count = sum(1 for r in results if r["result"] == "PASS")
    total_count = len(results)
    print(f"\n  总计: {passed_count}/{total_count} 通过")
    print(f"{'=' * 80}")

    # 输出 JSON 格式结果
    json_path = Path(r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\scripts\verify_results.json")
    with open(json_path, "w", encoding="utf-8") as f:
        json.dump(results, f, ensure_ascii=False, indent=2)
    print(f"  结果已保存: {json_path}")

    return 0 if passed_count == total_count else 1


if __name__ == "__main__":
    sys.exit(main())
