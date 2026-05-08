# core_hr_org_unit_transfer · 实施案例

## case_001 · 标准资产复刻（dcs_clean v1.5）

### 客户背景
- 改造者占位符：`tdkw`（dcs_clean 通用占位）
- 资产来源：金蝶 dcs_clean 标准化资产·路径 `hr-hdm-orgtransfer @ feature-standard cf299a0`
- 资产文档：`成建制划转复用说明书.docx` v1.0 (2025-08-04 梅小雪/宁杰)

### 实施过程

走 dcs-case-run K2 v2.1 全 8 步：
1. baseline snapshot
2. coverage scan（v3 知识库覆盖度 100%·1 ISV 自建场景 `tdkw_orguntitransfer`）
3. dcs_compare 评分 37/40（标品复用 + ISV 隔离 双 10/10）
4. 填 verdict / requirement / feedback_actions
5. **用户拍板 verdict = A**（无需大改·标准化漂亮）
6. Phase D 真扫（5 文件·deep_scan_inventory + class_tree + audit + diff + forbidden_check）
7. 反哺到 v3：建本场景（11md + 35件套）+ 建 [`_assets/org_unit_transfer/`](../../_assets/org_unit_transfer/) 资产模板
8. 归档 → `dcs_regression/passed/case_001_org_unit_transfer/`

### 关键产出

- **资产模板**：21 文件·一键 assemble
- **业务知识场景**：本场景 (core_hr_org_unit_transfer) 11md + 35件套
- **意图路由**：`_intent_routing.json` 加"成建制划转 / 批量调动 / 组织上级调整"等 4 意图
- **跨场景关系**：`_scene_relations.json` 加 11 个 form 反向引用
- **修了 2 个 dcs_clean bug**：
  - `schedule_*.schdata:231` `<TaskClassName>dgdl.hr.hdm.orgunittransfer.OrgRenameTask</TaskClassName>` → 修为 `tdkw.hr.hdm.orgtransfer.business.OrgRenameChgTask`
  - `tdkw_orguntitransfer.dym:184` `<Key>dgdl_listcolumnap</Key>` → 修为 `<Key>tdkw_listcolumnap</Key>`

### 经验固化

- **铁律**：`tdkw_*` 是改造者占位·真发给客户必须全局替换 → memory `feedback_isv_developer_flag_per_project.md`
- **K2 v2.1 升级**：加 Step 0 `detect_isv_flag` + GATE-013 + CR-013·防回归
- **数据驱动·代码不变**：v3 spec §3.1 原则·`assemble_asset.py` 不知道任何"客户"·只知道占位符替换

## case_002 · 合同续签（参考·不同业务但同套路）

详见 `dcs_regression/passed/case_002_contract_renew/`·走类似流程·passed 状态。

## 期望未来案例

| 场景 | 客户特征 | 预期产出 |
|---|---|---|
| 加新 changeType=D 部门撤销 | 客户业务侧定义部门撤销动作 | 走 CS-04 / EP-04 |
| 改数据源用客户 ESB 推送 | 客户已有自己的组织变更通知系统 | 走 CS-05 / EP-06 |
| 加 ISV 审批意见字段 | 客户调动单要求审批留痕 | 走 CS-03 / EP-03 |
