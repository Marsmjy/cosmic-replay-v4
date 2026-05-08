"""资产复刻装配器 (Asset Replicator) v1.0

把 knowledge/_assets/<asset_id>/ 下的资产模板·按客户参数·装配为完整可部署工程。

用法：
  python scripts/assemble_asset.py \
      --asset org_unit_transfer \
      --isv-flag bjss \
      --biz-app bjss_hdm_ext \
      --output D:/myproject/org_unit_transfer-bjss/

  # 干跑·只看产出文件清单·不真写
  python scripts/assemble_asset.py --asset org_unit_transfer --isv-flag bjss --biz-app bjss_hdm_ext --dry-run

协议：feedback_isv_developer_flag_per_project.md（占位符部署级变量）
       金蝶 dcs 复用 SOP（docx §4.1.2）
"""
from __future__ import annotations

import argparse
import re
import sys
import shutil
from pathlib import Path
from string import Template

if sys.platform == "win32":
    try:
        sys.stdout.reconfigure(encoding="utf-8")
    except Exception:
        pass

ROOT = Path(__file__).resolve().parent.parent
ASSETS_ROOT = ROOT / "knowledge" / "_assets"

# ────────────────────────────────────────────────────
# 资产元数据加载（轻量·只解析需要的部分·不引 PyYAML）
# ────────────────────────────────────────────────────

def load_asset_meta(asset_id: str) -> dict:
    meta_file = ASSETS_ROOT / asset_id / "_asset_meta.yaml"
    if not meta_file.exists():
        raise FileNotFoundError(f"Asset meta not found: {meta_file}")
    text = meta_file.read_text(encoding="utf-8")
    return {"raw": text, "path": meta_file}


def get_default_module_for_asset(asset_id: str) -> str:
    """从 asset_id 推断默认模块名 (org_unit_transfer → hr-hdm)·未来可扩展从 yaml 读"""
    return {
        "org_unit_transfer": "hr-hdm",
        # 后续 dcs_clean 14 项目添加各自映射
    }.get(asset_id, "biz")


def get_asset_metadata(asset_id: str) -> dict:
    """返回元数据 dict（asset_name_cn / business_brief 等·assemble README 用）"""
    return {
        "org_unit_transfer": {
            "asset_name_cn": "成建制划转",
            "asset_name_en": "Org Unit Transfer",
            "business_brief": "调度任务批量处理\"组织/岗位变更\"·写入标品调动单 hdm_transferbatch·完整代码 5 java 1354 LOC + 7 datamodel + 部署 SOP。",
            "asset_lib": "hdm-orgtransfer",
        },
    }.get(asset_id, {})


# ────────────────────────────────────────────────────
# 模板装配
# ────────────────────────────────────────────────────

def render_template(text: str, vars_: dict) -> str:
    """${VAR} 占位符替换 · 用 safe_substitute·允许 ${VAR} 不在 vars_ 时保留原文
    （兼容 gradle 等工具自身用的 $var 语法）
    """
    return Template(text).safe_substitute(vars_)


def collect_artifacts(asset_dir: Path, vars_: dict) -> list[tuple[Path, Path]]:
    """v2.0 通用版·从 _asset_meta.yaml 的 layout 字段读路径布局·跨资产类型通用

    layout 字段（_asset_meta.yaml 内）：
      java_root_subpath:    e.g. "hr/hlcmext/plugin"  (源相对路径·不含 ${ISV_FLAG} 前缀)
      datamodel_app_dir:    e.g. "hr/1.5.0/main"      (源 dym 所在目录·不含 ${BIZ_APP})
      datamodel_dyms:       [{src_name, target_form_name}] e.g.
        - src_name: hlcm_renewbatch.dym.tmpl
          target_form_name: hlcm_renewbatch  (会拼成 ${ISV_FLAG}_<target_form_name>)
      datamodel_other:      [{src_name, target_subpath}] (schdata / sql 等其他文件)
    """
    artifacts = []

    # 读 _asset_meta.yaml 的 layout 字段（轻量解析·不引 PyYAML）
    meta_text = (asset_dir / "_asset_meta.yaml").read_text(encoding="utf-8") if (asset_dir / "_asset_meta.yaml").exists() else ""
    layout = parse_layout(meta_text)

    isv_flag = vars_["ISV_FLAG"]
    biz_app = vars_["BIZ_APP"]

    # 1. java 模板·按 tmpl 内 package 声明还原子包结构（v1.0.4 修·原 layout.java_root_subpath 拍扁子包导致 ClassNotFound）
    import re as _re
    code_template_dir = asset_dir / "code_templates"
    if code_template_dir.exists():
        for tmpl in code_template_dir.rglob("*.java.tmpl"):
            target_name = tmpl.name.replace(".tmpl", "")
            # 读 tmpl 的 package 声明·替换 ${ISV_FLAG} 占位符·得到真实 package
            try:
                tmpl_text = tmpl.read_text(encoding="utf-8")
                m = _re.search(r"^package\s+([\w\.\$\{\}]+);", tmpl_text, _re.MULTILINE)
            except Exception:
                m = None
            if m:
                pkg = m.group(1)
                # 替换 ${ISV_FLAG} -> isv_flag
                pkg_real = pkg.replace("${ISV_FLAG}", isv_flag)
                # package "khr.swc.hsbm.biz.foo" -> 物理路径 "khr/swc/hsbm/biz/foo"
                pkg_path = pkg_real.replace(".", "/")
                target = Path(f"code/{biz_app}/src/main/java/{pkg_path}/{target_name}")
            else:
                # fallback·没 package 声明（如 package-info / Enum 漏写）·按 layout 老路径
                java_subpath = layout.get("java_root_subpath", "")
                if java_subpath:
                    target = Path(f"code/{biz_app}/src/main/java/{isv_flag}/{java_subpath}/{target_name}")
                else:
                    rel = tmpl.relative_to(code_template_dir)
                    rel_dir = rel.parent
                    if str(rel_dir) in ("", ".", "business"):
                        target = Path(f"code/{biz_app}/src/main/java/{isv_flag}/{target_name}")
                    else:
                        target = Path(f"code/{biz_app}/src/main/java/{isv_flag}/{rel_dir}/{target_name}")
            artifacts.append((tmpl, target))

    # 2. datamodel · v3.0 支持多模块（biz_app_modules）
    dm_template_dir = asset_dir / "datamodel_templates"
    is_multi_module = layout.get("multi_module", False)
    biz_app_modules = layout.get("biz_app_modules", [])
    primary_module_id = layout.get("primary_module") or (
        next((m["module_id"] for m in biz_app_modules if m.get("is_primary")), None)
    )

    if is_multi_module and biz_app_modules:
        # === 多模块装配 ===
        for module in biz_app_modules:
            module_id = module["module_id"]
            module_app_dir = module.get("datamodel_app_dir", f"{module_id}/1.5.0/main")
            is_primary = module.get("is_primary", False)
            # 客户的 biz_app：主模块用 --biz-app 入参；副模块按 pattern 派生
            if is_primary:
                module_biz_app = biz_app
            else:
                pattern = module.get("target_biz_app_pattern", f"${{ISV_FLAG}}_{module_id}")
                module_biz_app = pattern.replace("${ISV_FLAG}", isv_flag)

            # 主模块的 .tmpl 在 datamodel_templates/ 根；副模块在 datamodel_templates/<module_id>/
            module_tmpl_dir = dm_template_dir if is_primary else (dm_template_dir / module_id)
            if not module_tmpl_dir.exists():
                continue
            _add_datamodel_artifacts(artifacts, module_tmpl_dir, module_app_dir, module_biz_app, isv_flag)

        # 顶层 datamodel.xml（在 dm_template_dir 根·不属于任何模块）
        top_xml = dm_template_dir / "datamodel.xml.tmpl"
        if top_xml.exists():
            artifacts.append((top_xml, Path("datamodel/datamodel.xml")))
        return artifacts

    # === 单模块（兼容 v2.0）===
    dm_app_dir = layout.get("datamodel_app_dir", "hr/1.5.0/main")
    if dm_template_dir.exists():
        # 顶层 datamodel.xml
        if (dm_template_dir / "datamodel.xml.tmpl").exists():
            artifacts.append((dm_template_dir / "datamodel.xml.tmpl", Path("datamodel/datamodel.xml")))

        # 通用 biz_app.* 系列（app/appx/xml）
        if (dm_template_dir / "biz_app.xml.tmpl").exists():
            artifacts.append((dm_template_dir / "biz_app.xml.tmpl",
                              Path(f"datamodel/{dm_app_dir}/{biz_app}/{biz_app}.xml")))
        if (dm_template_dir / "biz_app.app.tmpl").exists():
            artifacts.append((dm_template_dir / "biz_app.app.tmpl",
                              Path(f"datamodel/{dm_app_dir}/{biz_app}/metadata/{biz_app}.app")))
        if (dm_template_dir / "biz_app.zh_CN.appx.tmpl").exists():
            artifacts.append((dm_template_dir / "biz_app.zh_CN.appx.tmpl",
                              Path(f"datamodel/{dm_app_dir}/{biz_app}/metadata/{biz_app}.zh_CN.appx")))

        # 所有 dym + dymx 模板·按 layout.datamodel_dyms 映射；如未配置·按"去掉前缀"自动推
        configured_dyms = {d["src_name"]: d for d in layout.get("datamodel_dyms", [])}
        for tmpl in dm_template_dir.glob("*.dym.tmpl"):
            cfg = configured_dyms.get(tmpl.name)
            if cfg:
                target_form = cfg["target_form_name"]
            else:
                # auto: hlcm_renewbatch.dym.tmpl → 目标 form = ${ISV_FLAG}_hlcm_renewbatch
                target_form = tmpl.name.replace(".dym.tmpl", "")
            target = Path(f"datamodel/{dm_app_dir}/{biz_app}/metadata/{isv_flag}_{target_form}.dym")
            artifacts.append((tmpl, target))
        for tmpl in dm_template_dir.glob("*.zh_CN.dymx.tmpl"):
            cfg = configured_dyms.get(tmpl.name.replace(".zh_CN.dymx.tmpl", ".dym.tmpl"))
            if cfg:
                target_form = cfg["target_form_name"]
            else:
                target_form = tmpl.name.replace(".zh_CN.dymx.tmpl", "")
            target = Path(f"datamodel/{dm_app_dir}/{biz_app}/metadata/{isv_flag}_{target_form}.zh_CN.dymx")
            artifacts.append((tmpl, target))

        # schdata 模板（任意名）
        for tmpl in dm_template_dir.glob("*.schdata.tmpl"):
            target_name = tmpl.name.replace(".tmpl", "")
            # 默认 schedule.schdata 名·有 layout 配置就用配置的
            schdata_target = layout.get("schdata_filename", target_name)
            target = Path(f"datamodel/{dm_app_dir}/{biz_app}/metadata/{schdata_target}")
            artifacts.append((tmpl, target))

        # preinsdata SQL 模板（FormPlugin 类资产 case_002 必有）
        preins_dir = dm_template_dir / "preinsdata"
        if preins_dir.exists():
            for tmpl in preins_dir.glob("*.sql.tmpl"):
                target_name = tmpl.name.replace(".tmpl", "")
                target = Path(f"datamodel/{dm_app_dir}/{biz_app}/preinsdata/{target_name}")
                artifacts.append((tmpl, target))

        # cld + cldx 元数据（hlcm-renew 有这种文件·历史前后端配置）
        for tmpl in dm_template_dir.glob("*.cld.tmpl"):
            target_name = tmpl.name.replace(".tmpl", "")
            target = Path(f"datamodel/{dm_app_dir}/{biz_app}/metadata/{target_name}")
            artifacts.append((tmpl, target))
        for tmpl in dm_template_dir.glob("*.zh_CN.cldx.tmpl"):
            target_name = tmpl.name.replace(".tmpl", "")
            target = Path(f"datamodel/{dm_app_dir}/{biz_app}/metadata/{target_name}")
            artifacts.append((tmpl, target))

    # 3. build files
    build_template_dir = asset_dir / "build_templates"
    if build_template_dir.exists():
        for tmpl in build_template_dir.glob("*.tmpl"):
            target_name = tmpl.name.replace(".tmpl", "")
            artifacts.append((tmpl, Path(target_name)))

    # 4. deploy_sop.md / customization_points.md
    for md in ["deploy_sop.md", "customization_points.md"]:
        src = asset_dir / md
        if src.exists():
            artifacts.append((src, Path(md)))

    return artifacts


def _add_datamodel_artifacts(artifacts: list, tmpl_dir, app_dir: str, biz_app: str, isv_flag: str):
    """v3.0 装配单模块 datamodel·从 tmpl_dir 落到 datamodel/<app_dir>/<biz_app>/..."""
    from pathlib import Path
    if not tmpl_dir.exists():
        return

    # biz_app.xml / app / appx
    for src_name, sub in [
        ("biz_app.xml.tmpl",        f"{biz_app}.xml"),
        ("biz_app.app.tmpl",        f"metadata/{biz_app}.app"),
        ("biz_app.zh_CN.appx.tmpl", f"metadata/{biz_app}.zh_CN.appx"),
    ]:
        src = tmpl_dir / src_name
        if src.exists():
            artifacts.append((src, Path(f"datamodel/{app_dir}/{biz_app}/{sub}")))

    # 模块自身 xml（如 hbjm_ext.xml / homs_ext.xml·命名 ${module_id}.xml）
    for src in tmpl_dir.glob("*.xml.tmpl"):
        if src.name in {"biz_app.xml.tmpl", "datamodel.xml.tmpl"}:
            continue
        target_name = src.name.replace(".tmpl", "")
        artifacts.append((src, Path(f"datamodel/{app_dir}/{biz_app}/{target_name}")))

    # dym + dymx
    for src in tmpl_dir.glob("*.dym.tmpl"):
        target_form = src.name.replace(".dym.tmpl", "")
        artifacts.append((src, Path(f"datamodel/{app_dir}/{biz_app}/metadata/{isv_flag}_{target_form}.dym")))
    for src in tmpl_dir.glob("*.dymx.tmpl"):
        # 处理 zh_CN.dymx / en_US.dymx 等多语言变体
        base = src.name.replace(".tmpl", "")  # e.g. hbss_joblabel.zh_CN.dymx
        # 去掉 i18n 后缀拿 form 名：hbss_joblabel.zh_CN → hbss_joblabel
        m = re.match(r"^(.+?)\.(zh_CN|en_US|ko_KR|ja_JP|ru_RU)(\.dymx)$", base)
        if m:
            target_form = m.group(1)
            i18n = m.group(2)
            ext = m.group(3)
            target_name = f"{isv_flag}_{target_form}.{i18n}{ext}"
        else:
            # 没 i18n·直接 form 名
            target_form = base.replace(".dymx", "")
            target_name = f"{isv_flag}_{target_form}.dymx"
        artifacts.append((src, Path(f"datamodel/{app_dir}/{biz_app}/metadata/{target_name}")))

    # schdata
    for src in tmpl_dir.glob("*.schdata.tmpl"):
        target_name = src.name.replace(".tmpl", "")
        artifacts.append((src, Path(f"datamodel/{app_dir}/{biz_app}/metadata/{target_name}")))

    # cld + cldx
    for src in tmpl_dir.glob("*.cld.tmpl"):
        target_name = src.name.replace(".tmpl", "")
        artifacts.append((src, Path(f"datamodel/{app_dir}/{biz_app}/metadata/{target_name}")))
    for src in tmpl_dir.glob("*.cldx.tmpl"):
        target_name = src.name.replace(".tmpl", "")
        artifacts.append((src, Path(f"datamodel/{app_dir}/{biz_app}/metadata/{target_name}")))

    # preinsdata SQL
    preins_dir = tmpl_dir / "preinsdata"
    if preins_dir.exists():
        for src in preins_dir.glob("*.sql.tmpl"):
            target_name = src.name.replace(".tmpl", "")
            artifacts.append((src, Path(f"datamodel/{app_dir}/{biz_app}/preinsdata/{target_name}")))


def parse_layout(meta_text: str) -> dict:
    """v3.0 解析 layout·含多模块 biz_app_modules"""
    layout = {}
    if not meta_text:
        return layout
    in_layout = False
    in_dyms = False
    in_modules = False
    cur_dym = None
    cur_module = None
    for line in meta_text.splitlines():
        stripped = line.strip()
        if line.startswith("layout:"):
            in_layout = True
            continue
        if in_layout:
            if line and not line.startswith(" ") and not line.startswith("#"):
                in_layout = False
                continue
            if in_modules:
                if stripped.startswith("- module_id:"):
                    if cur_module is not None:
                        layout.setdefault("biz_app_modules", []).append(cur_module)
                    cur_module = {"module_id": stripped.split(":", 1)[1].strip().strip('"').strip("'")}
                    continue
                elif cur_module is not None and stripped.startswith("datamodel_app_dir:"):
                    cur_module["datamodel_app_dir"] = stripped.split(":", 1)[1].strip().strip('"').strip("'")
                    continue
                elif cur_module is not None and stripped.startswith("is_primary:"):
                    cur_module["is_primary"] = stripped.split(":", 1)[1].strip().lower() == "true"
                    continue
                elif cur_module is not None and stripped.startswith("target_biz_app_pattern:"):
                    cur_module["target_biz_app_pattern"] = stripped.split(":", 1)[1].strip().strip('"').strip("'")
                    continue
                elif stripped and not stripped.startswith("-") and ":" in stripped and not line.startswith("      "):
                    # 退出 modules 段
                    if cur_module is not None:
                        layout.setdefault("biz_app_modules", []).append(cur_module)
                        cur_module = None
                    in_modules = False

            if stripped == "biz_app_modules:":
                in_modules = True
                layout["biz_app_modules"] = []
                if cur_dym is not None:
                    layout.setdefault("datamodel_dyms", []).append(cur_dym)
                    cur_dym = None
                in_dyms = False
                continue
            if stripped.startswith("java_root_subpath:"):
                layout["java_root_subpath"] = stripped.split(":", 1)[1].strip().strip('"').strip("'")
            elif stripped.startswith("multi_module:"):
                layout["multi_module"] = stripped.split(":", 1)[1].strip().lower() == "true"
            elif stripped.startswith("primary_module:"):
                layout["primary_module"] = stripped.split(":", 1)[1].strip().strip('"').strip("'")
            elif stripped.startswith("datamodel_app_dir:"):
                layout["datamodel_app_dir"] = stripped.split(":", 1)[1].strip().strip('"').strip("'")
            elif stripped.startswith("schdata_filename:"):
                layout["schdata_filename"] = stripped.split(":", 1)[1].strip().strip('"').strip("'")
            elif stripped.startswith("schdata_module_id:"):
                layout["schdata_module_id"] = stripped.split(":", 1)[1].strip().strip('"').strip("'")
            elif stripped == "datamodel_dyms:":
                in_dyms = True
                layout["datamodel_dyms"] = []
                if cur_module is not None:
                    layout.setdefault("biz_app_modules", []).append(cur_module)
                    cur_module = None
                in_modules = False
            elif in_dyms and stripped.startswith("- "):
                if cur_dym is not None:
                    layout["datamodel_dyms"].append(cur_dym)
                cur_dym = {}
                rest = stripped[2:]
                if rest.startswith("src_name:"):
                    cur_dym["src_name"] = rest.split(":", 1)[1].strip().strip('"').strip("'")
            elif in_dyms and cur_dym is not None and stripped.startswith("src_name:"):
                cur_dym["src_name"] = stripped.split(":", 1)[1].strip().strip('"').strip("'")
            elif in_dyms and cur_dym is not None and stripped.startswith("target_form_name:"):
                cur_dym["target_form_name"] = stripped.split(":", 1)[1].strip().strip('"').strip("'")
    # 收尾
    if cur_module is not None:
        layout.setdefault("biz_app_modules", []).append(cur_module)
    if cur_dym is not None:
        layout.setdefault("datamodel_dyms", []).append(cur_dym)
    return layout


def assemble(asset_id: str, vars_: dict, output: Path, dry_run: bool) -> dict:
    asset_dir = ASSETS_ROOT / asset_id
    if not asset_dir.exists():
        raise FileNotFoundError(f"Asset not found: {asset_dir}")

    artifacts = collect_artifacts(asset_dir, vars_)
    print(f"=== Asset Replicator · {asset_id} → {output} ({'DRY-RUN' if dry_run else 'WRITE'}) ===")
    print(f"模板变量：")
    for k, v in sorted(vars_.items()):
        print(f"  ${k} = {v}")
    print(f"\n待装配 {len(artifacts)} 个文件：\n")

    summary = {"written": 0, "skipped": 0, "errors": []}

    for tmpl, target_rel in artifacts:
        try:
            text = tmpl.read_bytes().decode("utf-8")
            rendered = render_template(text, vars_)
            target_abs = output / target_rel
            print(f"  → {target_rel}  ({len(rendered)}b)")
            if not dry_run:
                target_abs.parent.mkdir(parents=True, exist_ok=True)
                target_abs.write_bytes(rendered.encode("utf-8"))
                summary["written"] += 1
        except Exception as e:
            summary["errors"].append(f"{target_rel}: {e}")
            print(f"  ❌ {target_rel}: {e}")

    print(f"\n=== 汇总 ===")
    print(f"  写入: {summary['written']} 文件")
    print(f"  跳过: {summary['skipped']}")
    print(f"  错误: {len(summary['errors'])}")
    if summary["errors"]:
        print("\n错误清单：")
        for e in summary["errors"]:
            print(f"  - {e}")

    return summary


# ────────────────────────────────────────────────────
# 校验（GATE-01 ~ 04）
# ────────────────────────────────────────────────────

def validate_output(output: Path, vars_: dict) -> tuple[bool, list[str]]:
    """跑 4 项 GATE 验收"""
    errors = []
    isv = vars_["ISV_FLAG"]

    # GATE-01: 所有产物 grep tdkw_ = 0 / grep tdkw\. = 0
    leak_count = 0
    for f in output.rglob("*"):
        if not f.is_file():
            continue
        if f.suffix.lower() in {".jar", ".class", ".bin"}:
            continue
        try:
            text = f.read_text(encoding="utf-8", errors="replace")
            tdkw_leaks = re.findall(r'(?<![a-zA-Z0-9_])tdkw[._]', text)
            # 但若 ISV_FLAG 真的就是 tdkw·则跳过此检查
            if isv != "tdkw" and tdkw_leaks:
                leak_count += len(tdkw_leaks)
                errors.append(f"GATE-01 fail: {f.relative_to(output)} has {len(tdkw_leaks)} tdkw leaks")
        except Exception:
            pass

    # GATE-02: java 包路径与 schdata TaskClassName 一致（仅 schedule_task 类资产·非任务型跳过）
    schdata_files = list(output.rglob("*.schdata"))
    if schdata_files:
        for schdata in schdata_files:
            text = schdata.read_text(encoding="utf-8", errors="replace")
            # 检查 TaskClassName 标签内是否含 isv_flag
            if "<TaskClassName>" in text and f"{isv}." not in text:
                errors.append(f"GATE-02 fail: {schdata.relative_to(output)} TaskClassName 缺 {isv}. 包前缀")
    # 非任务型资产无 schdata·跳过 GATE-02

    # GATE-03: BizappId 占位提示
    for dym in output.rglob("*.dym"):
        text = dym.read_text(encoding="utf-8", errors="replace")
        if "${BIZ_APP_ID_PLACEHOLDER}" in text or "<BizappId></BizappId>" in text:
            print(f"  ⚠ GATE-03 提醒: {dym.relative_to(output)} <BizappId> 待手工填环境真值")
            break

    # GATE-04: deploy_sop.md 引用 docx
    sop = output / "deploy_sop.md"
    if sop.exists():
        text = sop.read_text(encoding="utf-8")
        if "§4.1.2" not in text:
            errors.append(f"GATE-04 fail: deploy_sop.md 未引用 docx §4.1.2")

    return len(errors) == 0, errors


# ────────────────────────────────────────────────────
# CLI
# ────────────────────────────────────────────────────

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--asset", required=True, help="资产 ID·如 org_unit_transfer")
    ap.add_argument("--isv-flag", required=True, help="客户开发商标识·如 bjss")
    ap.add_argument("--biz-app", required=True, help="ISV 应用包·如 bjss_hdm_ext")
    ap.add_argument("--output", help="输出目录·默认 D:/myproject/<asset>-<isv-flag>/")
    ap.add_argument("--dry-run", action="store_true", help="干跑·只看产物清单")
    ap.add_argument("--no-validate", action="store_true", help="跳过 GATE 验证")
    args = ap.parse_args()

    # 校验输入
    if not re.match(r'^[a-z][a-z0-9]{1,15}$', args.isv_flag):
        print(f"❌ ISV flag 格式不对（要 ^[a-z][a-z0-9]{{1,15}}$）: {args.isv_flag}")
        return 1
    if not re.match(r'^[a-z][a-z0-9_]{3,30}$', args.biz_app):
        print(f"❌ BIZ_APP 格式不对（要 ^[a-z][a-z0-9_]{{3,30}}$）: {args.biz_app}")
        return 1

    output = Path(args.output) if args.output else Path(f"D:/myproject/{args.asset}-{args.isv_flag}")

    asset_meta = get_asset_metadata(args.asset)
    vars_ = {
        "ISV_FLAG": args.isv_flag,
        "BIZ_APP": args.biz_app,
        "ASSET_ID": args.asset,
        "ASSET_MODULE": get_default_module_for_asset(args.asset),
        "ASSET_NAME_CN": asset_meta.get("asset_name_cn", args.asset),
        "ASSET_NAME_EN": asset_meta.get("asset_name_en", args.asset),
        "ASSET_LIB": asset_meta.get("asset_lib", args.asset),
        "BUSINESS_BRIEF": asset_meta.get("business_brief", ""),
        "CLIENT_NAME": args.isv_flag.upper(),  # 默认占位·客户可后改
        "BIZ_APP_ID_PLACEHOLDER": "${BIZ_APP_ID_TO_FILL}",  # 占位标记·部署时替换
        "BIZ_APP_ID_REAL": "<待部署时获取真值>",
    }

    summary = assemble(args.asset, vars_, output, args.dry_run)

    if not args.dry_run and not args.no_validate:
        print(f"\n=== 跑 GATE 验收 ===")
        ok, errors = validate_output(output, vars_)
        if ok:
            print("  ✅ 全部 GATE 通过")
        else:
            print(f"  ❌ {len(errors)} 项 GATE 失败:")
            for e in errors:
                print(f"     - {e}")
            return 2

    return 0 if not summary["errors"] else 1


if __name__ == "__main__":
    sys.exit(main())
