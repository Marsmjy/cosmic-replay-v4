# hbss_headcount_management · 推荐定制方案

> **资产名**：编制管理
> **数据源**：本场景 11md + 资产 _assets/<asset>/customization_points.md

## CS-01 · 完整资产复刻（最高频路径 ⭐）

### 业务背景

ISV 实施工程师拿到客户需求 → 客户提供：
- 开发商标识（如 `bjss`）
- ISV 应用编码（如 `bjss_<app>_ext`）

**预期产出**：完整工程包（53 java + N datamodel + 工程文件 + 部署 SOP）。

### 推荐方案

```bash
python scripts/assemble_asset.py \
    --asset headcount_management \
    --isv-flag bjss \
    --biz-app bjss_<app>_ext \
    --output D:/myproject/headcount_management-bjss/
```

### 部署 SOP

详见 [`_assets/headcount_management/deploy_sop.md`](../../_assets/headcount_management/deploy_sop.md)

## CS-02 ~ CS-N

⚡ 后续按需补：
- 改某字段过滤规则
- 改某 op 行为
- 加 ISV 自定义字段
- 加审批工作流
- 接其他外部系统
- 详见 [`_assets/headcount_management/customization_points.md`](../../_assets/headcount_management/customization_points.md)

## ❌ 反指引（不要在本场景做）

| 反模式 | 为什么不应该 |
|---|---|
| ❌ 直接 modifyMeta 标品 form 加字段 | PR-001·必须走 ext2 扩展元数据 |
| ❌ 在 BillPlugin 里直接 SaveServiceHelper.save 标品记录 | 应在 OpPlugin before/after·保事务一致 |
| ❌ 在 OpPlugin 里发 BEC 事件 | 标品自带 BEC·ISV 不重发 |
| ❌ 改标品 form 的 billstatus / auditstatus | 标品状态机·ISV 不能动·只能加 `${ISV_FLAG}_*` 自定义字段 |

详见 [`_antipatterns.json`](../../_antipatterns.json)。

## 关联 PR

- **PR-001** ISV 隔离·所有 ISV 加字段必带 `${ISV_FLAG}_` 前缀·走 ext 扩展元数据 ✅
- **PR-008/009** 时序资料·HisModel 查询带 `iscurrentversion=1`
- **PR-010** OP 13 个生命周期方法
- **PR-011** BEC 事件中心·标品自带·ISV 不重发
