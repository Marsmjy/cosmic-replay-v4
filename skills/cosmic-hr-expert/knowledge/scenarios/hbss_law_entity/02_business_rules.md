# hbss_law_entity · 业务规则

> **聚合场景**：法律实体与聘用单位（8 个子实体）
> **生成时间**：2026-04-29
> **方法**：从 `_shared/_standard_metadata/entity_metadata/<entity>.md` 提取每子实体真字段元数据 → 标品 HRBaseDataTplEdit 共性规则 + 子实体特有约束

## 一、共性规则（HRBaseDataTplEdit 标品模板 · 12 实体共用）

本聚合场景所有子实体均继承 `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` 标品模板，自带规则：

| 规则 ID | 触发点 | 行为 | ISV 是否可改 |
|---|---|---|:---:|
| BR_TPL_1 | 表单加载 (afterBindData) | 自动加载基础资料元数据 + 渲染字段 | ❌ 标品 |
| BR_TPL_2 | save 操作 | 触发 CodeRuleOp 自动生成 number 字段（按编码规则） | ❌ 标品 · 规则在元数据里配置 |
| BR_TPL_3 | save 操作 | HRBaseDataStatusOp · 设置 status 字段（A/B/C 状态机） | ❌ 标品 |
| BR_TPL_4 | save 操作 | HRBaseOriginalOp · 维护 orinumber/oriname/oristatus 出厂数据 | ❌ 标品 |
| BR_TPL_5 | enable / disable | HRBaseDataEnableOp · 维护 enable 字段 + disabledate/disabler | ❌ 标品 |
| BR_TPL_6 | save 操作 | HRBaseDataLogOp · 写变更日志（按 HRBaseDataConfigUtil 配置启用） | ❌ 标品 |

> ⚠️ ISV 不应继承 HRBaseDataTplEdit · 应**并列挂** `HRDataBaseEdit` 实现自定义逻辑（PR-001）

## 二、子实体特有规则（按字段提取）

### `hbss_lawentity` · 法律实体

**字段总数**：86

**必填字段（1）**：
- `entitytype` (实体类型) · `ComboField`

**引用基础资料（14）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `createorg` (创建组织) → `bos_org` · `OrgField`
- `org` (管理组织) → `bos_org` · `OrgField`
- `useorg` (使用组织) → `bos_org` · `OrgField`
- `srccreateorg` (原创建组织) → `bos_org` · `OrgField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `adminorg` (关联法人) → `bos_org` · `OrgField`
- `chgcreator1` (变更创建人) → `bos_user` · `CreaterField`
- `chgcreator` (创建人) → `bos_user` · `CreaterField`
- … 共 14 个引用

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_lawentityuse` · 法律实体使用情况

**字段总数**：21

**必填字段（1）**：
- `opitemid` (操作记录ID) · `BigIntField`

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_lawentityvrinf` · 法律实体版本详情

**字段总数**：39

**必填字段（3）**：
- `entitytype` (实体类型) · `ComboField`
- `createorg` (创建组织) · `OrgField` → ref bos_org
- `ctrlstrategy` (控制策略) · `ComboField`

**引用基础资料（5）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `adminorg` (关联法人) → `bos_org` · `OrgField`
- `createorg` (创建组织) → `bos_org` · `OrgField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_signcompany` · 聘用单位

**字段总数**：44

**必填字段（1）**：
- `representative` (法定代表人/主要负责人) · `TextField`

**引用基础资料（10）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (变更人) → `bos_user` · `ModifierField`
- `createorg` (创建组织) → `bos_org` · `OrgField`
- `org` (管理组织) → `bos_org` · `OrgField`
- `useorg` (使用组织) → `bos_org` · `OrgField`
- `srccreateorg` (原创建组织) → `bos_org` · `OrgField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `lawentity` (法律实体) → `hbss_lawentity` · `BasedataField`
- `reorg` (关联法人) → `hbss_lawentity` · `BasedataField`
- `changeperson` (变更人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_signcompanyhis` · 聘用单位历史

**字段总数**：23

**必填字段（5）**：
- `createorg` (创建组织) · `OrgField` → ref bos_org
- `number` (编码) · `TextField`
- `name` (名称) · `MuliLangTextField`
- `ctrlstrategy` (控制策略) · `ComboField`
- `representative` (法定代表人/主要负责人) · `TextField`

**引用基础资料（5）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (变更人) → `bos_user` · `ModifierField`
- `createorg` (创建组织) → `bos_org` · `OrgField`
- `reorg` (关联法人) → `hbss_lawentity` · `BasedataField`
- `lawentity` (法律实体) → `hbss_lawentity` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `number`, `name`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_taxunit` · 纳税单位

**字段总数**：22

**必填字段（1）**：
- `admindivision` (纳税地) · `BasedataField` → ref bd_admindivision

**引用基础资料（5）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `lawentity` (法律实体) → `hbss_lawentity` · `BasedataField`
- `admindivision` (纳税地) → `bd_admindivision` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_enterprise` · 用人单位

**字段总数**：28

**引用基础资料（8）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `createorg` (创建组织) → `bos_org` · `OrgField`
- `org` (管理组织) → `bos_org` · `OrgField`
- `useorg` (使用组织) → `bos_org` · `OrgField`
- `srccreateorg` (原创建组织) → `bos_org` · `OrgField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `lawentity` (关联法律实体) → `hbss_lawentity` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_payrollacrelation` · ⚠️ 标品 entity_metadata md 不存在

## 三、关键约束（共 12 实体）

| 约束 | 适用实体 | 来源 |
|---|---|---|
| `number` 唯一性 | 全部 12 实体 | 标品 CodeRuleOp + 元数据 UniqueValidation |
| `enable` 默认值 = '1'（启用） | 全部 12 实体 | HRBaseDataEnableOp |
| `status` 状态机 (A 暂存 → B 待审核 → C 已审核) | 全部 12 实体 | HRBaseDataStatusOp |
| `disabler` / `disabledate` 自动维护 | 全部 12 实体 | HRBaseDataEnableOp |

---

**精修元数据**：
- 生成器：`scripts/polish_aggregate_scene.py`
- 数据源：12 子实体的 `_shared/_standard_metadata/entity_metadata/<entity>.md`
- 标品共性来自 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` / `HRBaseDataEnableOp` / `HRBaseOriginalOp` / `HRBaseDataLogOp` 反编译实证