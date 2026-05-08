# hbss_supplier · 业务规则

> **聚合场景**：供应商/银行/工作地点字典（12 个子实体）
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

### `hbss_supplier` · 供应商

**字段总数**：38

**必填字段（7）**：
- `supplierlevel` (供应商等级) · `BasedataField` → ref hbss_supplierlevel
- `sereffectdate` (服务生效日期) · `DateField`
- `servicestatus` (服务状态) · `ComboField`
- `servicetype` (服务类型) · `MulBasedataField`
- `contactname` (姓名) · `MuliLangTextField`
- `issyscontact` (默认联系人) · `CheckBoxField`
- `mobile` (手机号码) · `TelephoneField`

**引用基础资料（5）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `syssupplier` (对应系统供应商) → `bd_bizpartner` · `BasedataField`
- `supplierlevel` (供应商等级) → `hbss_supplierlevel` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_supplierlevel` · 供应商等级

**字段总数**：19

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_bankdeposit` · 开户行

**字段总数**：19

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_workplace` · 工作地

**字段总数**：24

**必填字段（3）**：
- `country` (国家/地区) · `BasedataField` → ref bd_country
- `city` (城市) · `BasedataField` → ref bd_admindivision
- `createorg` (创建组织) · `OrgField` → ref bos_org

**引用基础资料（6）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `country` (国家/地区) → `bd_country` · `BasedataField`
- `city` (城市) → `bd_admindivision` · `BasedataField`
- `createorg` (创建组织) → `bos_org` · `OrgField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_encouragewords` · 激励文案

**字段总数**：7

**必填字段（1）**：
- `number` (编码) · `TextField`

**引用基础资料（2）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `number`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_addressdetail` · 详细地址

**字段总数**：23

**必填字段（4）**：
- `country` (国家/地区) · `BasedataField` → ref bd_country
- `city` (城市) · `BasedataField` → ref bd_admindivision
- `detailaddr` (详细地址) · `AddressField` → ref cts_address
- `addresstype` (地址类型) · `MulComboField`

**引用基础资料（6）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `country` (国家/地区) → `bd_country` · `BasedataField`
- `city` (城市) → `bd_admindivision` · `BasedataField`
- `detailaddr` (详细地址) → `cts_address` · `AddressField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_cycletype` · 周期类型

**字段总数**：19

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_entitytype` · 实体类型

**字段总数**：20

**必填字段（1）**：
- `relatepageinfo` (页签页面) · `TextField`

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_procreatstatus` · 生育状况

**字段总数**：19

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_procreatmode` · ⚠️ 标品 entity_metadata md 不存在

### `hbss_certmember` · 许可分组员工

**字段总数**：3

**标品公共字段**：`name`, `number`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_category` · ⚠️ 标品 entity_metadata md 不存在

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