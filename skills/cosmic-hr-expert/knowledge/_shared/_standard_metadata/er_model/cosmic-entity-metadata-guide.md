# 金蝶苍穹平台实体元数据设计文档

> 本文档系统性描述金蝶云苍穹（Kingdee Cloud Cosmos / KDDM）平台的实体元数据体系，
> 包含实体类型、字段类型、命名规范、物理存储映射、运行时对象模型、API 接口，
> 并通过多个典型场景（单据、基础资料、HCM 入职单、采购订单）深入解释。
>
> 日期：2026-03-25

---

## 目录

1. [核心概念与架构总览](#一核心概念与架构总览)
2. [实体类型体系（EntityType）](#二实体类型体系entitytype)
3. [字段类型体系（FieldDataType）](#三字段类型体系fielddatatype)
4. [命名规范](#四命名规范)
5. [实体元数据的物理存储](#五实体元数据的物理存储)
6. [运行时对象模型（DynamicObject / DynamicObjectType）](#六运行时对象模型dynamicobject--dynamicobjecttype)
7. [元数据 API 接口](#七元数据-api-接口)
8. [典型场景详解](#八典型场景详解)
9. [附录：字段类型与数据库列类型映射表](#九附录字段类型与数据库列类型映射表)

---

## 一、核心概念与架构总览

### 1.1 什么是实体元数据

苍穹平台的"实体元数据"（Entity Metadata）是描述**业务数据模型**的核心结构。一个实体对应一张业务单据或基础资料的数据定义，包含：

- **实体定义**（Entity）：描述数据结构的"骨架"——有哪些表、表之间的关系
- **字段定义**（Field/Property）：描述每个表中的"列"——字段名称、数据类型、是否必填、引用关系
- **操作定义**（Operation）：描述可对数据执行的操作——保存、提交、审核、删除
- **表单定义**（Form）：描述 UI 层的控件布局——控件树、插件绑定

它们之间的关系：

```
┌─────────────────────────── 设计期（IDE / 元数据建模）─────────────────────────┐
│                                                                              │
│  EntityMetadata（实体元数据）              FormMetadata（表单元数据）            │
│  ┌──────────────────────┐                ┌──────────────────────┐            │
│  │ BillEntity（主实体）   │◄───绑定────────│ Form（表单）           │            │
│  │  ├─ Field × N        │                │  ├─ FieldAp × N      │            │
│  │  └─ EntryEntity × M  │                │  ├─ EntryAp × M      │            │
│  │      └─ Field × K    │                │  └─ Plugin × P       │            │
│  └──────────────────────┘                └──────────────────────┘            │
│         ↓ 持久化                                  ↓ 持久化                   │
│  T_META_ENTITYDESIGN                     T_META_FORMDESIGN                   │
│                                          T_META_FORMDESIGN_L                 │
└──────────────────────────────────────────────────────────────────────────────┘
                          ↓ rebuildRuntimeMeta
┌─────────────────────────── 运行期（Java 运行时）─────────────────────────────┐
│                                                                              │
│  DynamicObjectType（动态类型） ────→ DynamicObject（动态对象/数据容器）        │
│  ├─ getName()           → "hom_onbrdinfo"                                   │
│  ├─ getProperties()     → [billno, billstatus, org, ...]                    │
│  ├─ getProperty("org")  → OrgField 属性定义                                 │
│  └─ getPrimaryKey()     → "id"                                              │
│                                                                              │
│  DynamicObject 存储实际数据值：                                               │
│  ├─ obj.get("billno")     → "ONB20260325001"                                │
│  ├─ obj.get("billstatus") → "A"                                             │
│  └─ obj.get("org")        → DynamicObject（关联的组织对象）                   │
└──────────────────────────────────────────────────────────────────────────────┘
```

### 1.2 一个实体元数据的完整生命周期

```
Step 1: 需求建模
  用户需求 → AI/开发人员 → RequirementArtifact JSON

Step 2: 元数据创建
  RequirementArtifact → buildMeta API → EntityMetadata + FormMetadata
  → 持久化到 T_META_ENTITYDESIGN / T_META_FORMDESIGN

Step 3: 物理建模
  entityMeta API → 提取表结构 → PDM 数据模型 → DDL → 数据库建表
  → CREATE TABLE t_xxx (FID BIGINT, FBILLNO VARCHAR(200), ...)

Step 4: 运行时加载
  MetadataDao.readEntityMeta("hom_onbrdinfo")
  → DynamicObjectType → 供 FormPlugin / OpPlugin / ServiceHelper 使用

Step 5: 数据操作
  DynamicObject obj = BusinessDataServiceHelper.loadSingle(id, "hom_onbrdinfo");
  obj.set("billstatus", "C");
  SaveServiceHelper.save(new DynamicObject[]{obj});
```

### 1.3 核心术语速查

| 术语 | 英文 | 说明 |
|------|------|------|
| 实体编码 | entityNumber / entityKey | 实体的唯一标识符，如 `hom_onbrdinfo`、`bd_supplier` |
| 表单编码 | formNumber / formKey | 通常与 entityKey 相同 |
| 字段编码 | fieldKey / Name | 字段的逻辑标识，如 `billno`、`org`、`cew4_price` |
| 列名 | columnName / Alias | 数据库物理列名，如 `fbillno`、`forgid`、`fk_cew4_price` |
| 表名 | tableName / Alias | 数据库物理表名，如 `t_hom_onbrdbill` |
| 基础资料编码 | basedataNumber | BasedataField 引用的目标实体编码，如 `bd_supplier` |
| ISV | isv | 独立软件供应商编码，决定命名前缀，如 `cew4`、`kingdee` |
| 应用编码 | appNumber / BizAppNumber | 实体所属的业务应用编码 |

---

## 二、实体类型体系（EntityType）

### 2.1 全部实体类型

苍穹平台定义了 **7 种**实体类型，覆盖所有业务数据建模场景：

| 实体类型 | 类名 | 适用场景 | 是否有状态机 | 是否有审批流 |
|---------|------|---------|-----------|-----------|
| **BillEntity** | BillEntityType | 业务单据（单据头） | 是（A→B→C） | 是 |
| **BaseEntity** | BaseEntityType | 基础资料/主数据 | 仅启用/禁用 | 否 |
| **EntryEntity** | EntryEntityType | 分录/明细行 | 否 | 否 |
| **SubEntryEntity** | SubEntryEntityType | 子分录（嵌套分录） | 否 | 否 |
| **TreeEntryEntity** | TreeEntryEntityType | 树形分录 | 否 | 否 |
| **QueryEntity** | QueryEntityType | 查询视图 | 否 | 否 |
| **SingleRowEntity** | SingleRowEntityType | 单行参数/配置 | 否 | 否 |

> **关键约束**：代码中必须使用精确的类名（如 `BillEntity`），不能用 `MainEntity`、`main`、`Bill`、`Entry` 等简写，否则后端反序列化报错。

### 2.2 BillEntity — 单据实体

**适用场景**：所有有生命周期状态流转、可能有审批流的业务单据。

**特征**：
- 自动注入 `billno`（单据编号）、`billstatus`（单据状态）、`creator`、`createtime` 等系统字段
- 标准状态机：A=暂存 → B=已提交 → C=已审核 → D=驳回重新提交
- 支持操作：保存(save)、提交(submit)、审核(audit)、反审核(unaudit)、删除(delete)

**实体结构示例**：

```
cew4_purchaseorder (BillEntity, 单据头)
├── billno (单据编号, BillNoField, 系统自动注入)
├── billstatus (单据状态, BillStatusField, 系统自动注入)
├── creator (创建人, CreaterField, 系统自动注入)
├── createtime (创建时间, CreateDateField, 系统自动注入)
├── modifier (修改人, ModifierField, 系统自动注入)
├── modifytime (修改时间, ModifyDateField, 系统自动注入)
├── cew4_signing_date (订单签订日期, DateField, 业务自定义)
├── cew4_procurementmgr (采购负责人, UserField, 业务自定义)
└── entryentity (EntryEntity, 单据体/分录)
    ├── seq (分录行号, IntegerField, 系统自动注入)
    ├── cew4_material (物料, BasedataField → cew4_mymaterial)
    ├── cew4_qty (订货数量, IntegerField)
    ├── cew4_price (采购单价, DecimalField)
    └── cew4_deliverydate (交货日期, DateField)
```

### 2.3 BaseEntity — 基础资料实体

**适用场景**：字典数据、主数据、档案类——无审批流，仅有启用/禁用状态。

**特征**：
- 自动注入 `number`（编码）、`name`（名称）、`enable`（使用状态）
- 通常被 BasedataField 引用（如"供应商"、"员工组"等）
- 支持主数据分发（master/slave）
- 支持历史版本（bsed/bsled 生效失效日期）

**实体结构示例**：

```
hbss_employeegroup (BaseEntity, 员工组)
├── number (编码, TextField, ✓必填)
├── name (名称, MuliLangTextField, ✓必填)
├── status (数据状态, BillStatusField)
├── enable (使用状态, BillStatusField)
├── creator (创建人, CreaterField)
├── createtime (创建时间, CreateDateField)
├── simplename (简称, MuliLangTextField)
├── description (描述, MuliLangTextField)
├── index (排序号, IntegerField)
├── issyspreset (系统预置, CheckBoxField)
└── disabler (禁用人, UserField → bos_user)
```

### 2.4 EntryEntity — 分录实体

**适用场景**：单据体、明细行——属于某个主实体的一对多子表。

**特征**：
- 必须设置 `parentEntityId` 指向所属主实体
- 自动注入 `seq`（行号/序号）
- 有独立的物理表，通过 FK（外键）关联主表
- 主键字段通常为 `FENTRYID`，外键为 `FID`

**EntryEntity 与 BillEntity 的主子关系**：

```
BillEntity（主表）──1:N──→ EntryEntity（分录表）
  FID (PK)                   FENTRYID (PK)
  FBILLNO                    FID (FK → 主表)
  FBILLSTATUS                FSEQ (行号)
  ...                        ...业务字段...
```

### 2.5 SubEntryEntity — 子分录实体

**适用场景**：嵌套在 EntryEntity 下的二级明细——如采购订单分录下的"收货计划"。

```
purchaseorder (BillEntity)
└── entryentity (EntryEntity, 采购明细)
    ├── material (物料)
    ├── qty (数量)
    └── delivery_plan (SubEntryEntity, 收货计划)
        ├── plan_date (计划日期)
        └── plan_qty (计划数量)
```

### 2.6 其他实体类型

| 类型 | 说明 | 示例 |
|------|------|------|
| **TreeEntryEntity** | 树形分录，有 parent 关系形成层级 | BOM 工序清单、项目分解结构(WBS) |
| **QueryEntity** | 只读查询视图，不对应物理表 | 员工花名册查询视图 |
| **SingleRowEntity** | 单行配置实体，整个表只有一行数据 | 系统参数配置、模块设置 |

---

## 三、字段类型体系（FieldDataType）

### 3.1 类型体系总览

苍穹平台共提供 **40+** 种字段类型，分为以下六大类：

```
FieldDataType（字段类型体系）
├── 一、文本类（commonfield）
│   ├── TextField              — 单行文本（名称/编码/标题, maxLength=255）
│   ├── TextAreaField          — 多行文本（备注/说明, maxLength=500）
│   ├── LargeTextField         — 大文本（正文/详情, maxLength=2000）
│   ├── RichTextField          — 富文本（HTML内容）
│   ├── MuliLangTextField      — 多语言文本（国际化名称）
│   ├── EmailField             — 邮箱
│   ├── TelephoneField         — 手机号/电话
│   └── SignatureField         — 手写签名
│
├── 二、数值类（commonfield）
│   ├── IntegerField           — 整数（件数/次数/序号）
│   ├── BigIntField            — 长整数（ID类）
│   ├── DecimalField           — 小数（高精度, scale=2）
│   ├── AmountField            — 金额（总价/费用, scale=2）
│   ├── PriceField             — 单价（含税价/不含税价, scale=2）
│   ├── QtyField               — 数量（库存/物料, scale=6）
│   └── ExchangeRateField      — 汇率
│
├── 三、日期时间类（commonfield）
│   ├── DateField              — 日期（yyyy-MM-dd）
│   ├── TimeField              — 时间（HH:mm:ss）
│   ├── DateTimeField          — 日期时间（yyyy-MM-dd HH:mm:ss）
│   ├── DateRangeField         — 日期范围
│   └── TimeRangeField         — 时间范围
│
├── 四、选择类（commonfield）
│   ├── ComboField             — 下拉单选（枚举/状态/类型, 需comboOptions）
│   ├── MulComboField          — 下拉多选（需comboOptions）
│   ├── CheckBoxField          — 复选框（是/否布尔值）
│   ├── CheckBoxGroupField     — 多选框组（需comboOptions）
│   ├── RadioGroupField        — 单选按钮组
│   └── RadioField             — 单选框
│
├── 五、基础资料及关联类（businessfield）
│   ├── BasedataField          — 关联基础资料（需basedataNumber）
│   ├── MulBasedataField       — 多选基础资料（需basedataNumber）
│   ├── OrgField               — 组织/部门（内置→bos_org, 禁填basedataNumber）
│   ├── UserField              — 用户/员工（内置→bos_user, 禁填basedataNumber）
│   ├── GroupField             — 基础资料分组（→bos_group, 不是部门字段!）
│   ├── MaterielField          — 物料（内置→bd_material）
│   ├── CurrencyField          — 币别（内置→bd_currency）
│   ├── RefBillField           — 引用单据（需refBillNumber）
│   └── AssistantField         — 辅助资料
│
└── 六、系统自动注入字段（禁止手动创建）
    ├── BillNoField            — 单据编号
    ├── BillStatusField        — 单据状态
    ├── CreaterField           — 创建人
    ├── CreateDateField        — 创建时间
    ├── ModifierField          — 修改人
    ├── ModifyDateField        — 修改时间
    └── MasterIdField          — 主数据ID
```

> **核心规则**：所有字段类型名**必须以 `Field` 结尾**（如 `TextField`、`DateField`），简写如 `Text`、`Date`、`Decimal` 不合法。

### 3.2 基础资料关联类字段的特殊约束

基础资料关联是苍穹元数据中最复杂的部分，规则如下：

| 字段类型 | basedataNumber | 说明 |
|----------|---------------|------|
| `BasedataField` | **必须填写** | 如 `bd_supplier`、`hbss_employeegroup` |
| `MulBasedataField` | **必须填写** | 同上 |
| `OrgField` | **禁止填写** | 内置关联 `bos_org`（含组织和部门） |
| `UserField` | **禁止填写** | 内置关联 `bos_user` |
| `MaterielField` | **不需要** | 内置关联 `bd_material` |
| `CurrencyField` | **不需要** | 内置关联 `bd_currency` |
| `GroupField` | **禁止填写** | 关联 `bos_group`，且**不是部门字段** |

> **高频错误**：
> - 苍穹中"部门"和"组织"均使用 `OrgField`，**不能**用 `BasedataField + bd_department`
> - `GroupField` 是基础资料分组（树形分类），不是部门字段

#### 3.2.1 HR 业务高频脑补陷阱（实证 → 别脑补）

LLM 在生成 HR 方案时容易把这些字段的类型写错，**真账以 `_metadata_rules_form.json` 为准**：

| 字段含义 | ❌ 容易脑补成 | ✅ 标品真存法 | 实证 |
|---|---|---|---|
| 性别 | "下拉（男/女）"·`ComboField` | `BasedataField` 引用 `hbss_sex` | `scenarios/core_hr_blacklist/_metadata_rules_form.json` 的 `gender` 字段 |
| 民族 | `ComboField` | `BasedataField` 引用 `hbss_nationality` | hbss 基础资料 |
| 国籍 | `ComboField` | `BasedataField` 引用 `hbss_country` | hbss 基础资料 |
| 政治面貌 | `ComboField` | `BasedataField` 引用 `hbss_politicalstatus` | hbss 基础资料 |
| 部门 / 组织 | `BasedataField + bd_department` | `OrgField`（不填 basedataNumber） | 见上"高频错误" |
| 员工 | `BasedataField + bd_user` | `BasedataField` 引用 `hrpi_employee` | core_hr 标品 |
| 岗位 | 自定义 ComboField | `BasedataField` 引用 `hrpi_position` | core_hr 标品 |

> **铁律**：HR 业务字段定义前·先 grep `knowledge/scenarios/<相关场景>/_metadata_rules_form.json` 看标品真存法·**不要凭印象选 ComboField**。
> "下拉" 是中文俗称·苍穹元数据真名是 `ComboField` / `MulComboField`·不要把"下拉"作为字段类型字面值写到方案里。
> 详见反面案例 `skill_incidents/2026-05-07_retirement_bill_brainmaking/audit_report.md`。

#### 3.2.2 ⭐ EmployeeField 控件（HR 业务专用·新模型推荐方案）

> **铁律**：HR 单据涉及"工号/员工/任职/组织分配"字段·**优先用 `EmployeeField` 控件**·不要再用 `BasedataField + hspm_ermanfile` 老模式（hspm_ermanfile 已废）。
>
> 来源：2026-05-08 case_005_batchorgadjust 真发发现·配套 [memory/cosmic_employeefield_authoritative.md](file:///C:/Users/kingdee/.claude/projects/d--aiworkspace-cludecodeworkspace/memory/cosmic_employeefield_authoritative.md)

##### 6 个查询实体选项（苍穹开发助手 IDE 字段属性下拉）

| 选项 | 业务实体 | F7 查询视图 | 适用场景 |
|---|---|---|---|
| **员工** | `hrpi_employee` | `hrpi_employeenewf7query` | 1:1 锚点·按员工挂数据 |
| **组织分配** | `hrpi_assignment` | `hrpi_assignmentf7query` | 需 persongroup / 行政组织信息 |
| **任职经历** | `hrpi_empposorgrel` | `hrpi_empposf7query` | 一员工多任职·调动业务必选 |
| 员工（薪酬镜像） | `hrpi_employee_lk` | 薪酬云镜像 | swc 云内引用员工 |
| 组织分配（薪酬镜像）| `hrpi_assignment_lk` | 薪酬云镜像 | swc 云内引用组织分配 |
| 任职经历（薪酬镜像）| `hrpi_empposorgrel_lk` | 薪酬云镜像 | swc 云内引用任职 |

##### dym XML 形态

```xml
<EmployeeField>           <!-- 字段类型 EmployeeField·非 BasedataField -->
    <Key>${ISV_FLAG}_employeefield</Key>
    <FieldName>fk_${ISV_FLAG}_employeefield</FieldName>
    <Name>工号</Name>
    <DisplayProp>employee.number</DisplayProp>      <!-- F7 选中显示 -->
    <BaseEntityId>hrpi_employee</BaseEntityId>      <!-- 6 选 1 -->
    <ParentId>分录ID</ParentId>
</EmployeeField>
```

##### 标品实证

参考标品用法：
- `haos_staffcase` 的 `empposorgrel` 字段（EmployeeField·hrpi_empposf7query）
- `haos_staffcase` 的 `employee` 字段（EmployeeField·hrpi_employeenewf7query）
- `haos_othemproleorgrel` 的 `employee` 字段（同上）

##### ⚠ OpenAPI 限制

- v3 真发的 `buildMeta` / `modifyMeta` **不支持**创建 `EmployeeField`
- **必须用苍穹开发助手 IDE 手工建**·或先 IDE 建再导出 dym
- 详 `aihr/cosmic/field_types_authoritative.py:92` 注释

##### 决策树（场景 → 选哪个）

```
单据按"员工"挂数据·一对一        → 选"员工" → hrpi_employee
单据按"任职"挂数据·一员工可多任职 → 选"任职经历" → hrpi_empposorgrel
单据需 persongroup/管理范围信息   → 选"组织分配" → hrpi_assignment
跨云 swc 内引用                  → 选对应的薪酬镜像（_lk）
```

### 3.3 下拉字段的 comboOptions 格式

凡 `ComboField`、`MulComboField`、`CheckBoxGroupField`、`RadioGroupField` 类型，**必须**提供选项：

```json
{
  "fieldKey": "enrollstatus",
  "displayName": "入职状态",
  "dataType": "ComboField",
  "comboOptions": [
    { "label": "待启动", "value": "wait_start" },
    { "label": "待入职", "value": "wait_onbrd" },
    { "label": "入职中", "value": "onbrd_ing" },
    { "label": "已入职", "value": "onbrded" },
    { "label": "已终止", "value": "break_up" }
  ]
}
```

规则：
- `label`：中文显示名
- `value`：英文存储值，全小写 + 下划线
- 选项数量：2~20个

---

## 四、命名规范

### 4.1 命名体系总览

苍穹平台的命名体系有两套规则，取决于 ISV（独立软件供应商）身份：

| 命名对象 | ISV 非 kingdee 格式 | ISV = kingdee 格式 |
|---------|--------------------|--------------------|
| **entityKey** | `{app_number}_{业务名}` | `{业务名}` |
| **fieldKey** | `{app_number}_{业务词}` | `{业务词}` |
| **tableName** | `tk_{entityKey}` | `t_{entityKey}` |
| **columnName** | `fk_{isv}_{业务词}` | `f{业务词}` |

### 4.2 entityKey（实体编码）

```
格式：{app_number}_{业务名}
长度：≤ 36 字符（建议 ≤ 30，为分录 _entry 后缀留空间）
字符：全小写字母、数字、下划线；不能以数字开头
禁止：不能以 _id / _mob / _u / _pr 结尾
唯一性：整个系统内不重复

示例（app_number = cew4）：
  采购订单 → cew4_purchaseorder
  采购订单分录 → cew4_purchaseorder_entry（加 _entry 后缀，注意总长不超过 36）

示例（isv = kingdee，HCM标准产品）：
  入职信息 → hom_onbrdinfo（无额外ISV前缀，hom 是HCM入职模块标识）
  员工 → hrpi_employee
  员工组 → hbss_employeegroup
```

### 4.3 fieldKey（字段编码）

```
格式：{app_number}_{业务词}
长度：≤ 24 字符
唯一性：同一实体内不得重复
禁止：SQL保留字（order, group, key, value, type）

示例（app_number = cew4）：
  采购单价 → cew4_price
  订货数量 → cew4_qty
  交货日期 → cew4_deliverydate
  供应商 → cew4_supplier

示例（isv = kingdee）：
  入职类型 → onbrdtype（直接用业务词，无前缀）
  证件号码 → certificatenumber
  入职日期 → b_effectivedate
```

### 4.4 tableName（物理表名）

```
格式：
  非 kingdee → tk_{entityKey}
  kingdee    → t_{entityKey}
长度：≤ 25 字符（苍穹平台数据库硬限制）
分录：必须使用 _e 简写后缀，禁止 _entry（会超长）

示例：
  主实体 cew4_purchaseorder     → tk_cew4_purchaseorder（不超25字符）
  分录   cew4_purchaseorder_entry → tk_cew4_purchaseorder_e（用 _e 简写）
  
  主实体 hom_onbrdinfo          → t_hom_onbrdbill（kingdee产品，t_ 前缀）
  基础资料 hbss_employeegroup   → t_hbss_employeegroup
```

### 4.5 columnName（数据库列名）

```
格式：
  非 kingdee → fk_{isv}_{业务词}
  kingdee    → f{业务词}（f开头，无下划线）
长度：≤ 24 字符

示例（isv = cew4）：
  fieldKey cew4_price       → columnName fk_cew4_price
  fieldKey cew4_qty         → columnName fk_cew4_qty
  fieldKey cew4_supplier    → columnName fk_cew4_supplier

示例（isv = kingdee）：
  fieldKey billno           → columnName fbillno
  fieldKey billstatus       → columnName fbillstatus
  fieldKey onbrdtype        → columnName fonbrdtype
```

### 4.6 命名完整对照示例

以采购订单为例（isv = cew4, app_number = cew4）：

| 业务语义 | entityKey | tableName | fieldKey | columnName |
|---------|-----------|-----------|----------|------------|
| 采购订单（主实体） | cew4_purchaseorder | tk_cew4_purchaseorder | — | — |
| 采购订单（分录） | entryentity | t_scm_purchase11entry | — | — |
| 采购单价 | — | — | cew4_price | fk_cew4_price |
| 订货数量 | — | — | cew4_qty | fk_cew4_qty |
| 物料 | — | — | cew4_material | fk_cew4_material |
| 供应商 | — | — | cew4_supplier | fk_cew4_supplier |

以 HCM 入职信息为例（isv = kingdee）：

| 业务语义 | entityKey | tableName | fieldKey | columnName |
|---------|-----------|-----------|----------|------------|
| 入职申请（主实体） | hom_onbrdinfo | t_hom_onbrdbill | — | — |
| 入职类型 | — | — | onbrdtype | fonbrdtype |
| 证件号码 | — | — | certificatenumber | fcertificatenumber |
| 入职日期 | — | — | b_effectivedate | fb_effectivedate |
| 入职地点 | — | — | onbrdtcity | fonbrdtcity |

---

## 五、实体元数据的物理存储

### 5.1 设计期存储

实体元数据在设计期存储在苍穹平台的元数据管理表中：

| 存储表 | 内容 | 说明 |
|--------|------|------|
| **T_META_ENTITYDESIGN** | 实体元数据的序列化 BLOB | 包含全部实体、字段、关系定义 |
| **T_META_FORMDESIGN** | 表单元数据的序列化 BLOB | 包含控件树、布局、插件绑定 |
| **T_META_FORMDESIGN_L** | 表单多语言名称 | 支持多语言显示名 |

### 5.2 运行期物理表结构

每个实体对应一张数据库物理表（DDL 由 PDM 生成）：

**主表示例** — `t_hom_onbrdbill`（入职申请单）：

```sql
CREATE TABLE t_hom_onbrdbill (
  FID              BIGINT       NOT NULL,   -- 主键
  FBILLNO          VARCHAR(200),            -- 单据编号
  FBILLSTATUS      VARCHAR(10),             -- 单据状态（A/B/C/D）
  FCREATORID       BIGINT,                  -- 创建人ID（FK → bos_user）
  FCREATETIME      DATETIME,                -- 创建时间
  FMODIFIERID      BIGINT,                  -- 修改人ID
  FMODIFYTIME      DATETIME,                -- 修改时间
  FORGID           BIGINT,                  -- 人事管理组织ID（FK → bos_org）
  FONBRDTYPE       BIGINT,                  -- 入职类型（FK → hbss_onboardtype）
  FCERTIFICATENUMBER VARCHAR(50),           -- 证件号码
  FB_EFFECTIVEDATE DATE,                    -- 入职日期
  FENROLLSTATUS    VARCHAR(50),             -- 入职状态（ComboField）
  FPROCESSSTATUS   VARCHAR(50),             -- 流程状态
  ...
  PRIMARY KEY (FID)
);
```

**分录表示例** — `t_hom_onbrdbill_e`（入职申请单分录）：

```sql
CREATE TABLE t_hom_onbrdbill_e (
  FENTRYID         BIGINT       NOT NULL,   -- 分录主键
  FID              BIGINT       NOT NULL,   -- 外键 → 主表 FID
  FSEQ             INT,                     -- 行号/序号
  ...业务字段...
  PRIMARY KEY (FENTRYID),
  FOREIGN KEY (FID) REFERENCES t_hom_onbrdbill(FID)
);
```

### 5.3 字段类型到数据库列类型的映射

| 字段类型 | DB 列类型 | 默认长度 | 默认精度 |
|---------|-----------|---------|---------|
| TextField | VARCHAR | 50 | — |
| LargeTextField | NVARCHAR | 2000 | — |
| MuliLangTextField | NVARCHAR | 255 | — |
| IntegerField | INT | — | — |
| BigIntField | BIGINT | — | — |
| DecimalField | DECIMAL | 23 | 10 |
| AmountField | DECIMAL | 23 | 10 |
| PriceField | DECIMAL | 23 | 10 |
| DateField | DATETIME | — | — |
| DateTimeField | DATETIME | — | — |
| CheckBoxField | CHAR(1) | 1 | — |
| ComboField | VARCHAR | 50 | — |
| BasedataField | BIGINT | — | — |
| OrgField | BIGINT | — | — |
| UserField | BIGINT | — | — |
| BillNoField | VARCHAR | 30 | — |
| BillStatusField | VARCHAR | 10 | — |

> **关键规律**：所有引用类（BasedataField/OrgField/UserField/CreaterField/ModifierField）在数据库中存储的都是 **BIGINT 类型的 ID 值**，而不是名称。

### 5.4 引用字段的双列存储模式

每个引用类字段在数据库中会生成**两个列**：

| 字段 | 逻辑列（对象引用） | 物理ID列 |
|------|-----------------|---------|
| `creator` | → DynamicObject（完整的创建人对象） | `creator_id` → BIGINT |
| `cew4_material` | → DynamicObject（物料对象） | `cew4_material_id` → BIGINT |
| `cew4_supplier` | → DynamicObject（供应商对象） | `cew4_supplier_id` → BIGINT |

在实体元数据的字段列表中可以看到这种模式：

```
| Type           | Name                  | Alias             | DisplayName |
|----------------|-----------------------|-------------------|-------------|
| BasedataProp   | cew4_material         | fk_cew4_material  | 物料        |  ← 逻辑字段
| LongProp       | cew4_material_id      | fk_cew4_material  | （无）      |  ← ID字段
```

Java 代码中使用方式：

```java
// 获取引用对象
DynamicObject material = row.getDynamicObject("cew4_material");
String materialName = material.getString("name");

// 获取引用ID
long materialId = row.getLong("cew4_material_id");
// 或
long materialId = ((DynamicObject) row.get("cew4_material")).getLong("id");
```

---

## 六、运行时对象模型（DynamicObject / DynamicObjectType）

### 6.1 核心对象关系

```
DynamicObjectType（元数据类型）
  ↓ 描述
DynamicObject（数据容器）
  ↓ 集合
DynamicObjectCollection（分录集合）
```

| 对象 | 职责 | 类比 |
|------|------|------|
| DynamicObjectType | 元数据描述（字段结构定义） | Java 中的 Class 对象 |
| DynamicObject | 数据容器（存储字段值） | Java 中的 Object 实例 |
| DynamicObjectCollection | 分录行集合 | Java 中的 List |
| IDataEntityProperty | 单个字段属性定义 | Java 中的 Field 对象 |

### 6.2 DynamicObjectType 常用方法

| 方法 | 返回值 | 用途 | 使用频率 |
|------|-------|------|---------|
| `getName()` | String | 获取实体编码（如 "hom_onbrdinfo"） | 极高(58次) |
| `getProperties()` | DataEntityPropertyCollection | 获取全部字段定义 | 极高(81次) |
| `getProperty(key)` | IDataEntityProperty | 获取指定字段定义（可能为null） | 高(40次) |
| `getPrimaryKey()` | ISimpleProperty | 获取主键属性 | 低 |
| `addProperty(prop)` | void | 动态添加属性（仅打印/报表场景） | 中(16次) |
| `createInstance()` | DynamicObject | 创建空实例 | 低 |

### 6.3 典型代码模式

**模式1：获取实体编码进行数据查询**

```java
DynamicObject org = (DynamicObject) getModel().getValue("org");
// 重新加载获取完整字段
org = BusinessDataServiceHelper.loadSingle(
    org.getPkValue(),
    org.getDynamicObjectType().getName(),  // → "bos_org"
    "id,name,parent"
);
```

**模式2：遍历所有字段批量操作**

```java
DynamicObjectCollection entries = getModel().getEntryEntity("entryentity");
DataEntityPropertyCollection props = entries.getDynamicObjectType().getProperties();
for (IDataEntityProperty prop : props) {
    getView().setVisible(false, prop.getName());  // 批量隐藏
}
```

**模式3：字段存在性检查（兼容不同版本）**

```java
DynamicObject bill = getModel().getDataEntity();
if (bill.getDynamicObjectType().getProperty("newField") != null) {
    // 字段存在，安全访问
    Object value = bill.get("newField");
}
```

**模式4：动态构建数据结构（打印场景）**

```java
DynamicObjectType customType = new DynamicObjectType("reportData");
customType.addProperty(new DynamicSimpleProperty("column1", String.class, null));
customType.addProperty(new DynamicSimpleProperty("amount", BigDecimal.class, null));
DynamicObject row = new DynamicObject(customType);
row.set("column1", "测试");
row.set("amount", new BigDecimal("100.50"));
```

### 6.4 铁律

```
① getName() 返回实体编码（如 "bd_supplier"），不是显示名称（不是"供应商"）
② getProperty(key) 可能返回 null，必须先判空
③ getProperties() 返回字段定义集合，不是字段值
④ 动态添加属性仅适用于临时数据结构（打印/报表），不能修改表单元数据
⑤ 遍历 properties 时使用 IDataEntityProperty 接口
```

---

## 七、元数据 API 接口

### 7.1 buildMeta — 创建实体元数据

| 项目 | 内容 |
|------|------|
| 方法 | `POST` |
| 路径 | `/ai-meta/buildMeta` |
| 作用 | 根据 RequirementArtifact JSON 自动创建实体+表单元数据 |

**调用链**：

```
HTTP POST /ai-meta/buildMeta
  └─ MetaNLApiService.createEntityMetadata()
       ├─ 1. validateCreateEntityRequest() — 参数校验
       ├─ 2. MetadataDao.getIdByNumber() — 编码唯一性检查
       ├─ 3. DBServiceHelper.genStringId() — 生成 formId/entityId
       ├─ 4. 构建 EntityMetadata
       │      ├─ buildRootEntity() — 创建根实体（BillEntity/BaseEntity）
       │      ├─ buildFieldFromDefinition() × N — 创建字段
       │      └─ EntryEntity × M — 创建分录实体
       ├─ 5. 构建 FormMetadata
       │      ├─ FieldAp — 表头控件
       │      ├─ EntryAp — 分录控件
       │      └─ EntryFieldAp — 分录字段控件
       ├─ 6. 绑定实体-表单关联
       ├─ 7. TX.required { MetadataWriter.save() } — 事务持久化
       ├─ 8. rebuildRuntimeMetaById() — 重建运行时元数据
       └─ 9. registerFormToApp() — 注册到应用
```

### 7.2 entityMeta — 查询实体元数据

| 项目 | 内容 |
|------|------|
| 方法 | `GET` |
| 路径 | `/kapi/v2/devportal/pdm/entityMeta` |
| 参数 | `entityNumber`（实体编码，如 `hom_onbrdinfo`） |
| 作用 | 获取指定实体的设计期元数据（含所有表、字段、主键、外键信息） |

**响应格式**：

```json
{
  "success": true,
  "data": {
    "entityNumber": "testbill1",
    "appId": "e2024072409210798",
    "appNumber": "bastax",
    "tables": [
      {
        "tableName": "t_testauditlogbill",
        "entityType": "main",
        "pkFieldName": "FID",
        "pkDbType": -5,
        "columns": [
          {
            "fieldName": "FID",
            "dbType": -5,
            "fieldLen": 0,
            "precision": 18,
            "scale": 0
          },
          {
            "fieldName": "FBILLNO",
            "dbType": 12,
            "fieldLen": 200,
            "precision": 0,
            "scale": 0
          }
        ]
      },
      {
        "tableName": "t_testauditlogbill_E",
        "entityType": "entry",
        "pkFieldName": "FENTRYID",
        "fkFieldName": "FID",
        "seqFieldName": "FSEQ",
        "columns": [...]
      }
    ]
  }
}
```

**响应字段说明**：

| 字段 | 说明 |
|------|------|
| `entityNumber` | 实体编码 |
| `appId` | 所属应用 ID |
| `tables[].tableName` | 物理表名 |
| `tables[].entityType` | 实体类型（`main` / `entry`） |
| `tables[].pkFieldName` | 主键字段名（主表通常为 `FID`，分录为 `FENTRYID`） |
| `tables[].fkFieldName` | 外键字段名（仅分录表有，指向主表的 `FID`） |
| `tables[].columns[].fieldName` | 字段列名 |
| `tables[].columns[].dbType` | 数据库类型（java.sql.Types 常量值） |
| `tables[].columns[].fieldLen` | 字段长度 |
| `tables[].columns[].precision` | 精度 |
| `tables[].columns[].scale` | 小数位数 |

**dbType 常量映射**：

| dbType | java.sql.Types | 数据库类型 |
|--------|---------------|-----------|
| 1 | CHAR | CHAR |
| 12 | VARCHAR | VARCHAR |
| -9 | NVARCHAR | NVARCHAR |
| 4 | INTEGER | INT |
| -5 | BIGINT | BIGINT |
| 3 | DECIMAL | DECIMAL |
| 93 | TIMESTAMP | TIMESTAMP |
| 91 | DATE | DATE |

### 7.3 RequirementArtifact JSON 格式

这是 AI 建模输出的标准产物格式，后端通过 buildMeta 接口消费：

```json
{
  "requirementId": "REQ-20260325-001",
  "sourceDoc": "采购订单需求.md",
  "parsedAt": "2026-03-25T02:00:00Z",
  "mode": "STANDARD",
  "version": 1,
  "status": "REVIEW_PENDING",
  "entities": [
    {
      "id": "e1",
      "entityKey": "cew4_purchaseorder",
      "displayName": "采购订单",
      "tableName": "tk_cew4_purchaseorder",
      "type": "BillEntity",
      "description": "采购订单主实体，记录采购业务单据",
      "status": "CONFIRMED",
      "confidence": "HIGH",
      "fields": [
        {
          "id": "f1",
          "fieldKey": "cew4_signing_date",
          "displayName": "订单签订日期",
          "columnName": "fk_cew4_signing_date",
          "description": "采购订单的签订日期",
          "dataType": "DateField",
          "mustInput": false,
          "status": "CONFIRMED",
          "confidence": "HIGH"
        },
        {
          "id": "f2",
          "fieldKey": "cew4_procurementmgr",
          "displayName": "采购负责人",
          "columnName": "fk_cew4_procurementmgr",
          "description": "负责本次采购的人员",
          "dataType": "UserField",
          "mustInput": false,
          "status": "CONFIRMED",
          "confidence": "HIGH"
        }
      ]
    },
    {
      "id": "e2",
      "entityKey": "cew4_purchaseorder_entry",
      "displayName": "采购订单明细",
      "tableName": "tk_cew4_purchaseorder_e",
      "type": "EntryEntity",
      "parentEntityId": "e1",
      "description": "采购订单的物料明细行",
      "status": "CONFIRMED",
      "confidence": "HIGH",
      "fields": [
        {
          "id": "f3",
          "fieldKey": "cew4_material",
          "displayName": "物料",
          "columnName": "fk_cew4_material",
          "description": "采购的物料",
          "dataType": "BasedataField",
          "basedataNumber": "cew4_mymaterial",
          "mustInput": true,
          "status": "CONFIRMED",
          "confidence": "HIGH"
        },
        {
          "id": "f4",
          "fieldKey": "cew4_qty",
          "displayName": "订货数量",
          "columnName": "fk_cew4_qty",
          "description": "订购的物料数量",
          "dataType": "IntegerField",
          "mustInput": true,
          "status": "CONFIRMED",
          "confidence": "HIGH"
        },
        {
          "id": "f5",
          "fieldKey": "cew4_price",
          "displayName": "采购单价(含税)",
          "columnName": "fk_cew4_price",
          "description": "含税的采购单价",
          "dataType": "DecimalField",
          "mustInput": false,
          "status": "CONFIRMED",
          "confidence": "HIGH"
        }
      ]
    }
  ],
  "plugins": []
}
```

---

## 八、典型场景详解

### 场景 1：简单基础资料 — 员工组（hbss_employeegroup）

这是最简单的实体——一个 BaseEntity，无分录，无审批流。

**业务需求**：管理员工分组（如"正式员工"、"实习生"、"外包人员"），支持启用/禁用。

**实体元数据**：

```
hbss_employeegroup (BaseEntity)
├── number (编码, TextField, 必填)
├── name (名称, MuliLangTextField, 必填)
├── status (数据状态, BillStatusField, 系统自动)
├── enable (使用状态, BillStatusField, 系统自动)
├── simplename (简称, MuliLangTextField)
├── description (描述, MuliLangTextField)
├── index (排序号, IntegerField)
└── issyspreset (系统预置, CheckBoxField)
```

**物理表** `t_hbss_employeegroup`：

| 列名 | 类型 | 说明 |
|------|------|------|
| FID | BIGINT | 主键 |
| FNUMBER | VARCHAR(50) | 编码 |
| FNAME | NVARCHAR(255) | 名称（多语言） |
| FSTATUS | VARCHAR(10) | 数据状态 |
| FENABLE | VARCHAR(10) | 使用状态 |
| FSIMPLENAME | NVARCHAR(255) | 简称 |
| FDESCRIPTION | NVARCHAR(255) | 描述 |
| FINDEX | INT | 排序号 |
| FISSYSPRESET | CHAR(1) | 系统预置标识 |

**Java 使用**：

```java
// 查询所有启用的员工组
DynamicObject[] groups = BusinessDataServiceHelper.load(
    "hbss_employeegroup",
    "id,number,name",
    new QFilter("enable", "=", "C")  // C = 已启用
);

// 在入职单的字段中引用（BasedataField → hbss_employeegroup）
DynamicObject empGroup = onbrdBill.getDynamicObject("ba_a_empgroup");
String groupName = empGroup.getString("name");
```

**关键点**：
- 基础资料实体的标准状态值：A=暂存、C=已启用、D=已禁用
- 被其他实体通过 `BasedataField` + `basedataNumber: "hbss_employeegroup"` 引用

---

### 场景 2：复杂单据 — 采购订单（cew4_purchaseorder）

一个典型的主子结构单据——BillEntity + EntryEntity，有审批流。

**业务需求**：记录采购订单，包含单头（订单日期、负责人）和分录（物料、数量、单价、供应商）。

**完整实体元数据**：

```
cew4_purchaseorder (BillEntity, 主表: t_scm_purchase11)
│
│ ── 系统字段（模板自动注入，禁止手动创建）──
├── billno (单据编号, TextProp)
├── billstatus (单据状态, BillStatusProp, A=暂存/B=已提交/C=已审核)
├── creator (创建人, CreaterProp → bos_user)
├── modifier (修改人, ModifierProp → bos_user)
├── auditor (审核人, UserProp → bos_user)
├── auditdate (审核日期, DateTimeProp)
├── createtime (创建时间, CreateDateProp)
├── modifytime (修改时间, ModifyDateProp)
│
│ ── 业务字段（自定义）──
├── cew4_procurementmgr (采购负责人, UserProp → bos_user)
├── cew4_signing_date (订单签订日期, DateProp)
│
│ ── 引用ID字段（系统自动生成）──
├── creator_id (BIGINT)
├── modifier_id (BIGINT)
├── auditor_id (BIGINT)
├── org_id (BIGINT)
├── cew4_procurementmgr_id (BIGINT)
│
└── entryentity (EntryEntity, 分录表: t_scm_purchase11entry)
    │
    │ ── 系统字段 ──
    ├── seq (分录行号, IntegerProp)
    ├── modifierfield (修改人, ModifierProp → bos_user)
    ├── modifydatefield (修改时间, ModifyDateProp)
    │
    │ ── 业务字段 ──
    ├── cew4_material (物料, BasedataProp → cew4_mymaterial)
    ├── cew4_qty (订货数量, IntegerProp)
    ├── cew4_price (采购单价含税, DecimalProp)
    ├── cew4_price_total (产品采购价格, DecimalProp)
    ├── cew4_unit (计量单位, BasedataProp → bd_measureunits)
    ├── cew4_receivedqty (交货数量, IntegerProp)
    ├── cew4_deliverydate (交货日期, DateProp)
    ├── cew4_supplier (供应商, SupplierProp → bd_supplier)
    │
    │ ── 引用ID字段 ──
    ├── modifierfield_id (BIGINT)
    ├── cew4_material_id (BIGINT)
    ├── cew4_unit_id (BIGINT)
    └── cew4_supplier_id (BIGINT)
```

**操作集合**（单据生命周期操作）：

| 操作Key | 操作名称 | 校验规则 | 操作插件 |
|---------|---------|---------|---------|
| save | 保存 | MustInput | CodeRuleOp（编号规则） |
| submit | 提交 | FormValidate + GroupFieldUnique + MustInput | CodeRuleOp |
| audit | 审核 | FormValidate + InProcess | — |
| unaudit | 反审核 | FormValidate + InProcess | — |
| unsubmit | 撤销 | FormValidate | — |
| delete | 删除 | FormValidate | CodeRuleDeleteOp |

**表单控件树**：

```
titlepanelflex (Flex面板)
├── titlepanel (标题区)
│   └── tbmain (工具栏)
│       ├── bar_new (新增)
│       ├── bar_save (保存)
│       ├── bar_submit (提交)
│       ├── bar_audit (审核)
│       └── bar_print (打印)
└── contentpanelflex (内容区)
    └── contentpanel
        ├── fs_baseinfo (基本信息面板)
        │   ├── billno (单据编号)
        │   ├── billstatus (单据状态)
        │   ├── cew4_procurementmgr (采购负责人)
        │   ├── org (采购组织)
        │   └── cew4_signing_date (订单签订日期)
        ├── advconap (分录面板)
        │   └── entryentity (单据体表格)
        │       ├── cew4_material (物料)
        │       ├── cew4_supplier (供应商)
        │       ├── cew4_price (采购单价)
        │       ├── cew4_qty (订货数量)
        │       └── ...
        └── attachmentpanel (合同附件)
```

**关键点**：
- 单据类型有完整的状态机 + 操作集合 + 校验规则 + 操作插件
- 分录通过外键关联主表
- 引用类字段自动生成 `_id` 后缀的 BIGINT 列
- 表单控件树定义了 UI 布局

---

### 场景 3：超大型 HCM 单据 — 入职信息（hom_onbrdinfo）

这是一个**极度复杂**的真实企业级单据——260+ 字段、跨 7 个云模块引用、前/后员工双份字段。

**业务需求**：管理员工入职全生命周期，包含候选人信息、入职条件、组织分配、任职经历、审批状态等。

**实体规模**：

| 维度 | 数据 |
|------|------|
| 主实体字段数 | ~260+ |
| 分录实体 | 1 个（entryentity_all，包含所有附属信息） |
| 引用的基础资料 | 40+ 种（跨 haos/hbjm/hbpm/hbss/hcf/hrpi/hpfs 七大云） |
| ComboField | 15+ 个（入职状态/流程状态/同步结果等） |
| CheckBoxField | 15+ 个（是否高端/外籍/特殊人才等） |
| 时间字段 | 20+ 个（入职日期/转正日期/报到日期等） |

**字段命名特征（kingdee ISV 模式）**：

```
HCM 系统使用 ba_ / bb_ 前缀区分入职前后的双份数据：

ba_ = 入职后（After）的数据快照
bb_ = 前员工（Before）的数据快照

示例：
  ba_po_adminorg   = 入职后_任职经历_行政组织（历史快照）
  ba_po_adminorgbo = 入职后_任职经历_行政组织（最新值）
  bb_po_adminorg   = 前员工_任职经历_行政组织（历史快照）
  bb_po_adminorgbo = 前员工_任职经历_行政组织（最新值）

每组有 4 个字段：ba历史 + ba最新 + bb历史 + bb最新
→ 这导致了 260+ 字段的"膨胀"
```

**跨云引用关系图**：

```
hom_onbrdinfo（入职单）
├─→ haos_adminorghrf7（行政组织云）: acompany, ba_po_adminorg, bb_po_adminorg, ...
├─→ hbpm_positionhrf7（岗位管理云）: ba_po_position, bb_po_position, ...
├─→ hbjm_jobhr（职位管理云）: ba_po_job, bb_po_job, ajoblevel, ...
├─→ hbss_*（基础服务云）: onbrdtype, certificatetype, laborreltype, ...
├─→ hcf_candidate（拟入职人员）: candidate
├─→ hrpi_employee*（员工信息）: ba_em_tid, bb_em_tid
└─→ hpfs_chgaction（人事变动）: affaction, chgreason
```

**HCM 特有字段类型**（苍穹平台标准类型之外的扩展）：

| 字段类型 | 说明 | 对应基础资料 |
|---------|------|-----------|
| HRAdminOrgField | HR行政组织字段（扩展的BasedataField） | haos_adminorghrf7 |
| HRPositionField | HR岗位字段 | hbpm_positionhrf7 |
| EmployeeField | 员工引用字段 | hrpi_employeenewf7query |
| MulEmployeeField | 多选员工字段 | — |
| HRMulAdminOrgField | 多选行政组织字段 | — |
| HisModelBasedataField | 历史模型基础资料字段 | — |
| QueryField | 查询视图字段 | — |

**关键点**：
- HCM 系统在标准苍穹字段类型之上扩展了领域特有字段类型
- ba/bb 双份字段模式是 HCM 处理"再入职"场景的设计方案
- 单个实体可引用 40+ 种基础资料，形成复杂的跨云依赖网

---

### 场景 4：员工主数据 — 员工信息（hrpi_employee）

一个带**时间版本**的 BaseEntity——员工信息有历史版本管理。

**业务需求**：存储员工基本信息，支持历史版本追溯（bsed/bsled 生效/失效日期模式）。

**版本控制字段**：

```
hrpi_employee (BaseEntity, 带时间版本)
├── boid (业务ID, BigIntField)           — 同一员工的所有版本共享此ID
├── iscurrentversion (当前生效, CheckBoxField) — 标识当前生效的版本
├── datastatus (数据版本状态, ComboField)
├── sourcevid (关联历史版本, BigIntField)
├── firstbsed (最早生效日期, DateField)
├── bsed (生效日期, DateField)           — Begin of Service Effective Date
├── bsled (失效日期, DateField)          — Begin of Service Last Effective Date
├── hisversion (版本号, TextField)
└── changedescription (变更说明, TextField)
```

**版本模式工作原理**：

```
员工"张三"入职时创建 v1：
  boid=1001, bsed=2026-04-01, bsled=9999-12-31, iscurrentversion=true

2026-07月调动部门，创建 v2：
  boid=1001, bsed=2026-07-01, bsled=9999-12-31, iscurrentversion=true
  v1 更新为：bsled=2026-06-30, iscurrentversion=false

查询时：
  当前员工 → WHERE iscurrentversion = true
  某日在职 → WHERE bsed <= '2026-05-01' AND bsled >= '2026-05-01'
```

**丰富的基础资料引用**：

```
hrpi_employee 引用的基础资料：
├── globalperson → hrpi_globalperson（全球员工）
├── primaryemployee → hrpi_employee（主员工，自引用）
├── oldemployee → hrpi_employee（前员工，自引用）
├── assignment → hrpi_assignment（组织分配）
├── gender → hbss_sex（性别）
├── nationality → hbss_nationality（国籍）
├── folk → hbss_flok（民族）
├── marriagestatus → hbss_marriagestatus（婚姻状况）
├── healthstatus → hbss_healthstatus（健康状况）
├── procreatstatus → hbss_procreatstatus（生育状况）
├── nbloodtype → hbss_bloodtype（血型）
├── symbolicanimals → hbss_zodiac（生肖）
└── constellation → hbss_constellation（星座）
```

**关键点**：
- BaseEntity 也可以很复杂（70+ 字段）
- 时间版本模式（bsed/bsled）是 HCM 系统的标准历史追溯方案
- 自引用关系（员工→前员工、员工→主员工）

---

## 九、附录：字段类型与数据库列类型映射表

### 完整映射表

| 字段类型 | ColumnType 编码 | DB 类型 | 默认长度 | 默认精度 | 默认值 |
|---------|----------------|---------|---------|---------|--------|
| TextField | 1 (VARCHAR) | VARCHAR | 50 | — | — |
| LargeTextField | 3 (NVARCHAR) | NVARCHAR | 2000 | — | — |
| MuliLangTextField | 3 (NVARCHAR) | NVARCHAR | 255 | — | — |
| RichTextField | 3 (NVARCHAR) | NVARCHAR | 2000 | — | — |
| BillNoField | 1 (VARCHAR) | VARCHAR | 30 | — | — |
| BillStatusField | 1 (VARCHAR) | VARCHAR | 10 | — | — |
| IntegerField | 9 (INT) | INT | — | — | — |
| BigIntField | 11 (BIGINT) | BIGINT | — | — | — |
| DecimalField | 14 (DECIMAL) | DECIMAL | 23 | 10 | — |
| AmountField | 14 (DECIMAL) | DECIMAL | 23 | 10 | — |
| PriceField | 14 (DECIMAL) | DECIMAL | 23 | 10 | — |
| DateField | 12 (DATETIME) | DATETIME | — | — | — |
| DateTimeField | 12 (DATETIME) | DATETIME | — | — | — |
| TimeField | 1 (VARCHAR) | VARCHAR | 8 | — | — |
| CreateDateField | 12 (DATETIME) | DATETIME | — | — | — |
| ModifyDateField | 12 (DATETIME) | DATETIME | — | — | — |
| CheckBoxField | 0 (CHAR) | CHAR | 1 | — | '0' |
| ComboField | 1 (VARCHAR) | VARCHAR | 50 | — | — |
| RadioField | 0 (CHAR) | CHAR | 1 | — | '0' |
| BasedataField | 11 (BIGINT) | BIGINT | — | — | 0 |
| MulBasedataField | 11 (BIGINT) | BIGINT | — | — | 0 |
| OrgField | 11 (BIGINT) | BIGINT | — | — | 0 |
| UserField | 11 (BIGINT) | BIGINT | — | — | 0 |
| CreaterField | 11 (BIGINT) | BIGINT | — | — | 0 |
| ModifierField | 11 (BIGINT) | BIGINT | — | — | 0 |
| FlexField | 1 (VARCHAR) | VARCHAR | 255 | — | — |
| AdminDivisionField | 11 (BIGINT) | BIGINT | — | — | 0 |

### java.sql.Types 对照表

| java.sql.Types 常量 | 值 | 对应 DB 类型 |
|--------------------|------|-------------|
| CHAR | 1 | CHAR |
| VARCHAR | 12 | VARCHAR |
| NVARCHAR | -9 | NVARCHAR |
| INTEGER | 4 | INT |
| BIGINT | -5 | BIGINT |
| DECIMAL | 3 | DECIMAL |
| TIMESTAMP | 93 | TIMESTAMP |
| DATE | 91 | DATE |
| TIME | 92 | TIME |

---

## 十、总结

### 苍穹实体元数据的核心设计哲学

1. **万物皆 DynamicObject**：苍穹通过统一的动态对象模型，实现了"一套机制服务所有业务实体"，无论是简单的基础资料还是 260+ 字段的入职单，底层都是 DynamicObjectType + DynamicObject。

2. **设计期-运行期分离**：元数据在设计期以结构化 JSON 定义并持久化到 T_META_* 表，运行期动态加载为 DynamicObjectType，支持热更新和在线修改。

3. **主子表范式**：所有复杂单据都遵循 BillEntity（主表）+ EntryEntity（分录）的层次结构，通过 FID/FENTRYID 主外键关联。

4. **引用即 BIGINT**：所有基础资料引用（BasedataField/OrgField/UserField）在物理层存储的都是 BIGINT 类型的 ID，运行时按需加载完整对象。

5. **命名即规范**：ISV/app_number 前缀机制确保多租户环境下的命名隔离，kingdee 内部产品使用更简洁的命名。

6. **模板注入系统字段**：单据编号、状态、创建人、修改人等由模板自动注入，开发者只需关注业务字段。
