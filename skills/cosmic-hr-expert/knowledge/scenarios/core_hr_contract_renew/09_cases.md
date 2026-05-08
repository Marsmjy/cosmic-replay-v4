# core_hr_contract_renew · 实施案例

## case · contract_renew_batch（W1 标杆精修案例）

### 资产来源

- **DCS 项目**：`D:\myproject\dcs_clean\contract_renew_batch`
- **原开发商标识**：`tdkw`（dcs_clean 阶段·后期会改造者全局替换）
- **入库轨迹**：dcs_regression/passed/case_002_contract_renew/
- **场景知识**：本场景 core_hr_contract_renew (11md + 35 件套)

### 客户业务背景（典型场景）

某大型集团 HR 接入苍穹核心人力·业务方提：
- 季度末批量给 N 个事业部的合同到期员工统一办理续签
- 每事业部 100-500 员工·按所属组织拉名单
- 一次提交 = 一批次·走 HR → 部门长 → CFO 三级审批
- 配合现有法大大签署服务

**痛点**：
- 标品 hlcm_contractapplyrenew 是单员工流程·一对一办·HR 一上午只能办 30 个
- 缺批次维度的"主表 → N 员工"关联·无法批量审批
- 缺与法大大签署状态的拦截规则

### 实施过程

走 K2 dcs-case-run v2.1 + K3 dcs-asset-replicate v1.3 全 8 步：

| Step | 工具 | 产物 | 耗时 |
|---|---|---|---|
| 1 baseline | `_baseline_diff.py snapshot` | 锚定 v3 当前态 | 1 min |
| 2 coverage | `_kb_coverage_scan.py` | coverage_report.md | 5 min |
| 3 dcs_compare | `_dcs_compare.py` | dcs_implementation.json + verdict.md 草稿 | 10 min |
| 4 fill | 人工 | requirement.md / verdict 审 / feedback_actions.yaml | 15 min |
| **5 ⛔ user review** | 用户 | verdict 拍板 = **B**（轻度知识缺口·走 6 阶段反哺） | - |
| 6 反哺 | `_kb_inject.py` + 人工 | core_hr_contract_renew 场景 + 4 索引 | 30 min |
| 7 验证 | `_dependency_tracker.py` + `_baseline_diff.py compare` | blast_radius=0 / 0 退化 | 5 min |
| 8 归档 | `mv pending → passed` + changelog | passed/case_002_contract_renew/ | 2 min |

### Phase D 真扫产物（W1 标杆质量）

| 文件 | 大小 | 内容 |
|---|---|---|
| deep_scan_inventory.md | 3868b | 7 dym + 5 java + 15 SQL + 2 i18n + 6 工程文件清单 |
| deep_scan_class_tree.md | 5920b | 4 类 × 18 方法树·每个方法 file:line 锚点 |
| deep_scan_audit.md | 10396b | 12 段（A-L）60+ 业务断言全 file:line 锚 |
| deep_scan_diff.md | 6486b | 4 大对照（new/cancel closedCallBack / before/after OP / ListPlugin 3 路径 / Utils ISV vs 平台）|
| deep_scan_forbidden_check.md | 621b | 0 SDK 白名单违反·全合规 |

### 关键反哺产出

#### 6 阶段反哺·B 类 case 反哺成果

| 反哺类型 | 产物 | 锚点 |
|---|---|---|
| L1 索引 | `_intent_routing.json` 加意图 `合同批量续签` | knowledge/_intent_routing.json |
| L1 索引 | `_scene_relations.json` 加 `core_hr_contract_renew` 关联 | knowledge/_scene_relations.json |
| L1 索引 | `_antipatterns.json` 加 4 反指引（直接 modifyMeta 标品 / 跨表 SaveServiceHelper / 重发 BEC / 改 hlcm 状态字段）| knowledge/_antipatterns.json |
| L2 场景 | 完整 35 件套 + 11md | knowledge/scenarios/core_hr_contract_renew/ |
| L3 资产 | 模板化 37 文件 + assemble_asset.py | _assets/contract_renew_batch/ |
| 历史轨迹 | _kb_changelog.md v1.x.y 入账 | knowledge/_kb_changelog.md |

### 关键学习点

#### 1. ${ISV_FLAG} 占位符体系（架构胜出）

**问题**：tdkw 是 dcs_clean 阶段标识·真发给客户要替换为 bjss / cds0 等。

**解法**：assemble_asset.py 在产出阶段做全局替换：
```bash
python scripts/assemble_asset.py --asset contract_renew_batch \
    --isv-flag bjss --biz-app bjss_hlcm_ext \
    --output D:/myproject/contract_renew_batch-bjss/
```

→ 一份资产模板·N 客户 N 标识各自部署。

#### 2. 反向引用模式（ISV 经典模式）

**模式**：ISV 主表 → 标品 (1:N) 的反向引用：
- ISV 主表存 `${ISV_FLAG}_itemids_tag = "id1,id2,id3"`（字符串列表）
- 标品记录加 `${ISV_FLAG}_renewbatch BasedataField`（反向单选）
- OP 同事务双向写入·强一致

**避免的反模式**：
- ❌ 不在 ISV 主表加 entry 子表（会冗余·解绑复杂）
- ❌ 不直接改 hlcm_contractapplyrenew 标品状态字段（PR-001）

#### 3. 7 op 分组语义

beforeExecute 守门 + afterExecute 双语义：
- "建关联" 4 op（save/submit/audit/wfauditing）
- "解关联" 3 op（wfunaudit/invalid/delete）
- delete 走专用 dealUnbindBatch 保幂等

#### 4. 标品兼容护城河

本资产**不继承标品任何插件**·全部走"并列挂"模式·哪怕 hlcm 后续标品演进·本资产可隔离影响。

### 部署到客户现场的真实步骤

详见 [`_assets/contract_renew_batch/deploy_sop.md`](../../_assets/contract_renew_batch/deploy_sop.md)·6 步法：

1. assemble_asset.py 替换 ISV_FLAG
2. 客户环境 dym diff（确认 ext2 不冲突）
3. 跑 15 SQL preinsdata（已有则跳过）
4. 部署 5 java 类·重启服务
5. 烟测：建一个测试批次·选 10 员工·走完 save → audit → wfunaudit
6. 性能压测：选 1000+ 员工·确认 itemClick 弹窗 < 5s

### 验收标准

- ✅ baseline_diff = 0 退化
- ✅ assemble 重复 N 客户·md5 校验产物 N 致
- ✅ quality_gate 22+/22+ 全过
- ✅ _audit_depth 评级 A
- ✅ 6 阶段反哺无遗漏·changelog 入账

## 案例集合

未来同类案例可参考此 case：
- 合同变更批量场景（hlcm_contractapplychange + 类似批次主表）
- 协议批量解除（hlcm_empprotocolrelieve）
- 合同到期批量提醒（hlcm_contractapplyend）

→ 都可走"ISV 主表 → 标品反向引用"模式·复用本资产架构。

## 关联文档

- [v3 ZERO_OMISSION 案例总览](../../../v3_rewrite_plan/CASE_REGISTRY.md)（如已建）
- [W1 deep_scan 全套](../../../dcs_regression/passed/case_002_contract_renew/)
- [资产模板](../../_assets/contract_renew_batch/)
