# hbss_capability · 业务规则

> **聚合场景**：胜任力/能力字典（12 个子实体）
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

### `hbss_capacitygroup` · 能力素质维度

**字段总数**：34

**必填字段（1）**：
- `type` (描述方式) · `ComboField`

**引用基础资料（9）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `parent` (上级维度) → `hbss_capacitygroup` · `ParentBasedataField`
- `createorg` (创建组织) → `bos_org` · `OrgField`
- `org` (管理组织) → `bos_org` · `OrgField`
- `useorg` (使用组织) → `bos_org` · `OrgField`
- `srccreateorg` (原创建组织) → `bos_org` · `OrgField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `parentdim` (上级维度) → `hbss_capacitygroup` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_capacityitem` · 能力素质项

**字段总数**：51

**必填字段（3）**：
- `createorg` (创建组织) · `OrgField` → ref bos_org
- `caprankscheme` (能力等级方案) · `BasedataField` → ref hbss_capacityrankscheme
- `type` (描述方式) · `ComboField`

**引用基础资料（9）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `group` (所属维度) → `hbss_capacitygroup` · `GroupField`
- `org` (管理组织) → `bos_org` · `OrgField`
- `createorg` (创建组织) → `bos_org` · `OrgField`
- `useorg` (使用组织) → `bos_org` · `OrgField`
- `srccreateorg` (原创建组织) → `bos_org` · `OrgField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `caprankscheme` (能力等级方案) → `hbss_capacityrankscheme` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_capacityaction` · 能力行为

**字段总数**：21

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_capacityrankscheme` · 能力等级方案

**字段总数**：32

**必填字段（2）**：
- `entrynumber` (等级编码) · `TextField`
- `entryname` (等级名称) · `MuliLangTextField`

**引用基础资料（7）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `createorg` (创建组织) → `bos_org` · `OrgField`
- `org` (管理组织) → `bos_org` · `OrgField`
- `useorg` (使用组织) → `bos_org` · `OrgField`
- `srccreateorg` (原创建组织) → `bos_org` · `OrgField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_passessnode` · 绩效业务活动

**字段总数**：19

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_rewpnmlevel` · ⚠️ 标品 entity_metadata md 不存在

### `hbss_rewpnmtype` · ⚠️ 标品 entity_metadata md 不存在

### `hbss_scoreinterval` · 评分间隔

**字段总数**：14

**必填字段（5）**：
- `name` (名称) · `MuliLangTextField`
- `gears` (档位数) · `IntegerField`
- `minvalue` (最小值) · `DecimalField`
- `maxvalue` (最大值) · `DecimalField`
- `score` (分值) · `DecimalField`

**引用基础资料（2）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `name`, `number`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_scoresystem` · 评分分制

**字段总数**：38

**必填字段（4）**：
- `maxscore` (基准最高分) · `DecimalField`
- `minscore` (基准最低分) · `DecimalField`
- `tag` (标签) · `MuliLangTextField`
- `score` (评分) · `DecimalField`

**引用基础资料（8）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `createorg` (创建组织) → `bos_org` · `OrgField`
- `org` (管理组织) → `bos_org` · `OrgField`
- `useorg` (使用组织) → `bos_org` · `OrgField`
- `srccreateorg` (原创建组织) → `bos_org` · `OrgField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `scoreinterval` (评分间隔) → `hbss_scoreinterval` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_hrbuca` · HR业务管理视图

**字段总数**：24

**引用基础资料（4）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `parent` (HR业务管理视图) → `hbss_hrbuca` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_hrbucafunc` · ⚠️ 标品 entity_metadata md 不存在

### `hbss_familiarity` · ⚠️ 标品 entity_metadata md 不存在

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