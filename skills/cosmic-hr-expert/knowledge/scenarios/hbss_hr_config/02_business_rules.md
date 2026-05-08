# hbss_hr_config · 业务规则

> **聚合场景**：HR 系统配置与安全（18 个子实体）
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

### `hbss_safeuri` · 链接明细信息

**字段总数**：25

**引用基础资料（4）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `safeuriconfig` (短链配置) → `hbss_safeuriconfig` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_safeuriconfig` · 链接有效期配置

**字段总数**：21

**必填字段（2）**：
- `expiryduration` (有效期) · `IntegerField`
- `expiryunit` () · `ComboField`

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_loginconfig` · 登录页配置

**字段总数**：47

**必填字段（3）**：
- `loginscene` (登录场景) · `BasedataField` → ref hbss_loginscene
- `usertype` (用户类型) · `BasedataField` → ref hrcs_privacyusertype
- `logintype` (登录方式) · `MulComboField`

**引用基础资料（8）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `loginscene` (登录场景) → `hbss_loginscene` · `BasedataField`
- `privacystmt` (隐私声明) → `privacystatement` · `BasedataField`
- `redirectform` (登录后跳转表单_移动端) → `bos_objecttype` · `BasedataField`
- `usertype` (用户类型) → `hrcs_privacyusertype` · `BasedataField`
- `redirectappformid` (登录后跳转表单_PC端) → `bos_objecttype` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_loginscene` · 登录场景

**字段总数**：19

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_appentryconfig` · 应用入口配置

**字段总数**：12

**引用基础资料（2）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `number`, `name`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_privacyusertype` · 外部用户类型

**字段总数**：19

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_privacysigning` · 隐私声明签署记录

**字段总数**：11

**引用基础资料（6）**：
- `usertype` (用户类型) → `hbss_privacyusertype` · `BasedataField`
- `privacystmt` (隐私声明) → `privacystatement` · `BasedataField`
- `form` (业务对象) → `bos_objecttype` · `BasedataField`
- `locale` (语种) → `inte_language` · `BasedataField`
- `country` (国家) → `bd_country` · `BasedataField`
- `org` (行政组织) → `bos_org` · `OrgField`

**标品公共字段**：`modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_basedatalist` · HR基础资料

**字段总数**：20

**必填字段（1）**：
- `pagekey` (页面标识) · `TextField`

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_rolesourcetype` · ⚠️ 标品 entity_metadata md 不存在

### `hbss_timestamp` · 期间标识

**字段总数**：20

**必填字段（1）**：
- `cycletype` (周期类型) · `BasedataField` → ref hbss_cycletype

**引用基础资料（4）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `cycletype` (周期类型) → `hbss_cycletype` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_relatepanelset` · 侧边栏配置

**字段总数**：26

**必填字段（4）**：
- `entitytype` (实体类型) · `BasedataField` → ref hbss_entitytype
- `pageinfo` (页面信息) · `BasedataField` → ref bos_entityobject
- `pagetype` (页面类型) · `ComboField`
- `mainpropname` (主实体属性) · `TextField`

**引用基础资料（6）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `entitytype` (实体类型) → `hbss_entitytype` · `BasedataField`
- `pageinfo` (页面信息) → `bos_entityobject` · `BasedataField`
- `appid` (应用名称) → `bos_devportal_bizapp` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_cloud` · HR领域云

**字段总数**：8

**必填字段（1）**：
- `cloud` (云) · `BasedataField` → ref bos_devportal_bizcloud

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `cloud` (云) → `bos_devportal_bizcloud` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_cloud_app` · HR云与应用

**字段总数**：9

**必填字段（1）**：
- `app` (应用) · `BasedataField` → ref hbp_devportal_bizapp

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `app` (应用) → `hbp_devportal_bizapp` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_actiontype` · 业务操作类型

**字段总数**：20

**必填字段（1）**：
- `businessdomain` (业务类型) · `BasedataField` → ref hbss_bussinessfield

**引用基础资料（4）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `businessdomain` (业务类型) → `hbss_bussinessfield` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_action` · 业务操作

**字段总数**：20

**必填字段（1）**：
- `actiontype` (业务操作类型) · `BasedataField` → ref hbss_actiontype

**引用基础资料（4）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `actiontype` (业务操作类型) → `hbss_actiontype` · `BasedataField`

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hbss_hrbuviewquery` · HR业务管理视图

**字段总数**：0

### `hbss_hrbuquery` · HR管理组织

**字段总数**：0

### `hbss_hrbu_hitfrequency` · HR管理组织命中次数

**字段总数**：9

**引用基础资料（2）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

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