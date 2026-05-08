# org_dev_home_check_rule · 推荐定制方案

> **状态**: 🟢 基于 case 真代码 + 6 EP 扩展点
> **资产复刻**：[`_assets/home_check_rule/`](../../_assets/home_check_rule/)·一键 assemble
> **代码示例**：本节代码使用 `${ISV_FLAG}` 占位符代表客户开发商标识·部署到客户时全局替换为客户实际值

---

## CS-01 · 完整资产复刻（最高频路径 ⭐）

### 业务背景

ISV 实施工程师拿到客户需求"我要做信息采集校验配置工具"·客户提供：
- 开发商标识（如 `bjss`）
- ISV 应用编码（如 `bjss_<asset_app>`）

**预期产出**：完整工程包·客户立即可部署。

### 推荐方案

```bash
python scripts/assemble_asset.py     --asset home_check_rule     --isv-flag bjss     --biz-app bjss_xxx     --output D:/myproject/home_check_rule-bjss/
```

### 部署 SOP

详见 [`_assets/home_check_rule/deploy_sop.md`](../../_assets/home_check_rule/deploy_sop.md)

---

## CS-02 ~ CS-06 · 扩展点

详见 [`_assets/home_check_rule/customization_points.md`](../../_assets/home_check_rule/customization_points.md) EP-01 ~ EP-06。

---

## ❌ 反指引（不要在本场景做）

<待手工精修>
