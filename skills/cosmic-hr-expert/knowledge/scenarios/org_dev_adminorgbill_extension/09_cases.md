# org_dev_adminorgbill_extension · 实施案例

## case · 组织调整单扩展

### 资产来源

- **DCS 项目**：dcs_clean / 来源
- **原开发商标识**：`tdkw`（dcs_clean 阶段）
- **入库轨迹**：dcs_regression/passed/case_*/

### 客户业务背景

补典型客户业务背景 / 痛点 / 期望效果

### 实施过程

走 K2 dcs-case-run + K3 dcs-asset-replicate 全 8 步·详见 case_001 / case_002 标杆案例。

### 关键学习点

补 ISV 模式提取 / 反指引 / 标品兼容护城河

### 部署到客户现场

详见 [`_assets/<ASSET_ID>/deploy_sop.md`](../../_assets/<ASSET_ID>/deploy_sop.md)

### 验收标准

- ✅ baseline_diff = 0 退化
- ✅ assemble 重复 N 客户·md5 一致
- ✅ quality_gate 全过

## 关联文档

- W1 标杆 contract_renew_batch：[../core_hr_contract_renew/](../core_hr_contract_renew/)
