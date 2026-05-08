# core_hr_contract_renew · 能力边界

> **场景类型**：业务表单+OP 型聚合场景（4 java / 1036 LOC + 2 ISV 主表 + 标品 hlcm_contractapplyrenew 反向扩展）
> **核心动作**：批量挑选合同续签申请单 → 写"批量续签记账单"主表 → 标品 op 联动双向同步
> **数据源**：[`_assets/contract_renew_batch/`](../../_assets/contract_renew_batch/)
> **业务文档**：金蝶官方"批量劳动合同续签资产复用说明书"v1.0
> **代码实证**：dcs_clean/hlcm-renew/ @ feature-standard-new c8864f4
> **建设来源**：dcs-asset-replicate W1 精修 / 2026-05-06

## 业务定义

批量劳动合同续签：苍穹 HR 核心人力 hlcm 合同管理提供的"按所属组织批量挑选员工 → 一次性发起合同续签申请"能力。

| 字段 | 业务含义 | 实证 |
|---|---|---|
| `${ISV_FLAG}_hlcm_renewbatch` | ISV 主表·"批量续签记账单"·一条记录管 N 个员工的续签 | tdkw_hlcm_renewbatch.dym |
| `${ISV_FLAG}_hlcm_contractap_ext2` | ISV 扩展元数据 (PR-001)·给标品 `hlcm_contractapplyrenew` 加 `${ISV_FLAG}_renewbatch` 反向引用字段 | tdkw_hlcm_contractap_ext2.dym |
| `hlcm_contractapplyrenew` | **标品**·合同续签申请单·**核心被消费 + 被扩展对象** | RenewBatchBillPlugin.java:56,98 等 7 处 |

**业务流**：HR 在 `${ISV_FLAG}_hlcm_renewbatch` 单据界面 → 选所属组织 → 点"选员工"按钮打开 `hlcm_contractapplyrenew` 列表 → 多选员工 → 写入 `${ISV_FLAG}_itemids_tag`（逗号分隔 ID） → 提交主表 → OpPlugin 反向标记 `hlcm_contractapplyrenew.${ISV_FLAG}_renewbatch=主表 ID` → 审批通过后法大大签署集成 → 完成续签。

## 能做什么 ✅

- ✅ 按所属组织 + 子组织（递归）批量挑选员工的合同续签申请
- ✅ 一条 `${ISV_FLAG}_hlcm_renewbatch` 记账单管 N 个员工·`${ISV_FLAG}_personsize` 字段记总数
- ✅ 主表 ↔ 标品 `hlcm_contractapplyrenew` 双向关联（通过 `${ISV_FLAG}_renewbatch` 字段）
- ✅ 7 op 全覆盖：save / submit / audit / wfauditing / wfunaudit / invalid / delete
- ✅ 集成法大大电子签署：拦截 `tblsubmiteffect/tblsubmit` 按钮·只在 `${ISV_FLAG}_fddsignstatuss="F/已完成"` 或 `${ISV_FLAG}_signwaynew="A/纸质签署"` 时放行
- ✅ ISV 自定义按钮 `${ISV_FLAG}_baritemapex1`·允许在"待相对方签署 (E)"+"已完成 (F)" 两态点击

## 不能做什么 ❌

- ❌ **不做单人续签**·标品 `hlcm_contractapplyrenew` 自带·不需要本资产
- ❌ **不私改 hlcm_contractapplyrenew 的 billstatus/auditstatus 等核心标品字段**·只写 ISV 加的 `${ISV_FLAG}_renewbatch` 反向引用
- ❌ **不发跨云事件 (BEC)**·纯本应用 op 联动·不主动通知薪酬/考勤云
- ❌ **不批量改组织主数据**·只读 `haos_adminorg`·不动它

## 与相邻场景边界

| 场景 | 关系 |
|---|---|
| `core_hr_contract` (合同管理) | **同应用·父场景**·本资产是 contract 系列下的"批量续签"专用工具 |
| `hlcm_contractapplyrenew` (合同续签申请单) | **核心被扩展**·本资产加 `${ISV_FLAG}_renewbatch` 字段·扩展工具栏拦截器 |
| `hlcm_contract`·`hlcm_contractapplynew/end/change/cancel` | **同应用兄弟 form**·本资产 preinsdata 预置数据涉及它们但不直接扩展 |
| `haos_adminorg` (行政组织) | **被查询**·只读用·查 structlongnumber 子组织 |

## 适用 Tier 评级 (v3 spec §3.5)

- **Tier 1**：✅ scene_doc_lite + entity_metadata + 元数据规则
- **Tier 2**：✅ 11md 全 + 资产模板 (35 .tmpl) + 36/40 评分 + Phase D 5 文件
- **Tier 3**：⚡ 准备升（待加 rules_chain_all.json + 反编译 anchors）

## 关联资产

- **完整资产复刻**：[`_assets/contract_renew_batch/`](../../_assets/contract_renew_batch/) · `assemble_asset.py` 一键 37 文件
- **真扫报告**：[deep_scan_inventory.md](../../../dcs_regression/passed/case_002_contract_renew/deep_scan_inventory.md) / [deep_scan_audit.md](../../../dcs_regression/passed/case_002_contract_renew/deep_scan_audit.md) / [deep_scan_diff.md](../../../dcs_regression/passed/case_002_contract_renew/deep_scan_diff.md)
- **业务文档**：金蝶官方"批量劳动合同续签资产复用说明书 v1.0"
