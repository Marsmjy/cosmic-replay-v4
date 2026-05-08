# 03 模型设计 · hrcs_permlog（HR 权限日志）

> 数据源：probe_out + scene_doc.json + 反编译 PermLogListPlugin.java + plugins.json
> 物理表：13 张（t_hrcs_permlog 主 + 12 张子表/分录/语言表）
> ModelType：BillFormModel · **日志只读视图** · 不可编辑 · 跨表 JOIN 强
> 父模板（InheritPath L0）：`hbp_bd_originaltpl`（HR 原生基础资料模板）

---

## 1. 表层视图

### 1.1 主表与物理表全集

| # | 物理表 | 角色 | 字段数概况 |
|---|---|---|---|
| 1 | `t_hrcs_permlog` | **主单据头** · 14 业务字段 + 4 系统字段 | 主键 fid · operator/logtype/operationtime/hashandle 等 |
| 2 | `t_hrcs_permlogbaseinfo` | 基础信息分录 · changefield/before/after/description | 通用变更前后值 |
| 3 | `t_hrcs_permlogrolefunc` | 角色功能权限分录 · bizapp/entitytype/permitem/description | 角色挂功能权限项变更 |
| 4 | `t_hrcs_permlogroledim` | 角色维度分录 · bizapp/entitytype/dimension/bucafunc/propkey/before/after | 角色挂维度变更（dimension+bucafunc） |
| 5 | `t_hrcs_permlograngefield` | 角色字段范围分录 · bizapp/entitytype/propkey/propname/before/after | 字段级权限范围 |
| 6 | `t_hrcs_permlogroleopen` | 角色公开管理员分录 · admingroup + influadmin（MulBasedataField）| 角色对管理员组开放 |
| 7 | `t_hrcs_permloginfluuser` | 影响用户分录 · 17 字段（permfile/role/permitem/entitytype/bizapp/dataproperty/intersection 等）| 受影响用户清单 |
| 8 | `t_hrcs_permlograngedr` | 数据规则范围分录 · bizapp/entitytype/permitem/before/afterdr | 数据规则变更 |
| 9 | `t_hrcs_permlograngebddr` | 基础资料数据规则分录 · 类似 rangedr | 基础资料维度数据规则 |
| 10 | `t_hrcs_permloginflurole` | 影响角色分录 · bizapp/entitytype/permitem/role/influtype/dimension | 受影响角色清单 |
| 11 | `t_hrcs_permlograngeorg` | 组织范围分录 · bucafunc/beforeorgs/afterorgs | 组织维度范围变更 |
| 12 | `t_hrcs_permlograngebiz` | 业务范围分录 · bucafunc/before/after | 业务维度范围变更 |
| 13 | `t_hrcs_permlog_l` | **多语言表**（_l 结尾）| 存 description 多语言 |

> 任务说"4 张表"是简化口径 · 实际 scene_doc.json 物理表清单显示 **13 张**（含语言表）。核心高频用的 4 张：`t_hrcs_permlog` + `t_hrcs_permlogbaseinfo` + `t_hrcs_permlogrolefunc` + `t_hrcs_permloginfluuser`。

### 1.2 表关系图

```
                       t_hrcs_permlog (主)
                              | 1
                              |
        +--------+--------+---+---+--------+--------+--------+
        |        |        |   |   |        |        |        |
        N        N        N   N   N        N        N        N
   permlogbaseinfo  permlogrolefunc  permlogroledim  permlograngefield
   permlogroleopen  permloginfluuser  permlograngedr  permlograngebddr
   permloginflurole  permlograngeorg  permlograngebiz
                              |
                              | 1:1
                              v
                       t_hrcs_permlog_l (_l 多语言表)
```

**核心 JOIN 实证**（`PermLogListPlugin.setFilter` L243-L269）：
- `permfile.user.name` 触发 4 路 OR：自身 + influuserentry.influuser_permfile
- `permfile.id` 触发 3 路 OR
- `rolename` 触发 3 路 OR：rolenumber + influroleentry × 2

### 1.3 ModelType 与继承

- **ModelType**：BillFormModel（日志查看视图）
- **InheritPath L0**：`hbp_bd_originaltpl`（HR 原生基础资料模板 · ID 1A3I8S4ADNCO）
- **直接父类**：HRDataBaseList（List 视图）
- **不是 HisModel**：grep `iscurrentversion|HisModel|boid` 0 命中 → 普通日志单据 · 没有 boid/iscurrentversion 字段（PR-008/PR-009 时序约束**不适用**）

---

## 2. 字段层（114 字段 · 主表头 + 12 张子表分录字段）

### 2.1 主表头核心字段（17 项 · t_hrcs_permlog）

| # | key | 显示名 | 类型 | 必填 | 物理列 | 引用 | 备注 |
|---|---|---|---|---|---|---|---|
| 1 | creator | 创建人 | CreaterField | x | fcreatorid | bos_user | 系统维护 |
| 2 | createtime | 创建时间 | CreateDateField | x | fcreatetime | - | 系统维护 |
| 3 | modifier | 修改人 | ModifierField | x | fmodifierid | bos_user | 系统维护 |
| 4 | modifytime | 修改时间 | ModifyDateField | x | fmodifytime | - | 系统维护 |
| 5 | description | 描述 | MuliLangTextField | x | - | - | **多语言**（_l 结尾表存）|
| 6 | initdatasource | 初始化数据源 | ComboField | x | - | - | 系统维护 |
| 7 | number | 编码 | TextField | x | fnumber | - | List 超链字段 → showDetail |
| 8 | logtype | 日志类型 | BasedataField | x | flogtypeid | **hrcs_permlogtype** | 决定 handlerclass / 详情子页 formId |
| 9 | operator | 操作人 | UserField | x | foperatorid | bos_user | quickSearch 字段 |
| 10 | operationtime | 操作时间 | DateTimeField | x | - | - | 时序排序基准 |
| 11 | influusernumber | 影响用户数 | IntegerField | x | - | - | List 超链字段 → showDetail (isInfluusernumber=true) |
| 12 | clienttype | 客户端类型 | ComboField | x | - | - | 客户端 |
| 13 | mservicename | 微服务接口名 | TextField | x | - | - | 调用源 |
| 14 | opentitytype | 实体类型 | BasedataField | **必** | fopentitytypeid | bos_entityobject | 系统维护 |
| 15 | opbtnname | 操作名称 | TextField | x | - | - | 按钮文案 |
| 16 | beforeopdata | 操作前数据 | TextField | x | - | - | JSON 序列化字符串 |
| 17 | afteropdata | 操作后数据 | TextField | x | - | - | JSON 序列化字符串 |
| 18 | hashandle | 是否已处理 | CheckBoxField | x | - | - | **List 强制过滤 hashandle='1'**（FP_SF5）|

### 2.2 主表关联引用字段

| key | 类型 | 物理列 | 引用 | 备注 |
|---|---|---|---|---|
| role | BasedataField | froleid | perm_role | 角色 |
| entitytype | BasedataField | fentitytypeid | bos_entityobject | 实体类型 |
| datarule | BasedataField | fdataruleid | hrcs_datarule | 数据规则 |
| permfile | BasedataField | fpermfileid | hrcs_userpermfile | 用户权限档案 |
| datarulenumber/datarulename/rolenumber/rolename | TextField | - | - | 冗余字段（虚字段 · 写入时同步快照存 · setFilter rolename → rolenumber 三路 OR）|

### 2.3 子分录前缀对照表

| 前缀 | 物理表 | 业务含义 | 字段数 |
|---|---|---|---|
| `baseinfo_` | t_hrcs_permlogbaseinfo | 基础信息变更 | 4 |
| `rolefunc_` | t_hrcs_permlogrolefunc | 角色功能权限 | 5 |
| `roledim_` | t_hrcs_permlogroledim | 角色维度 | 9 |
| `rolefield_` | t_hrcs_permlograngefield | 角色字段 | 7 |
| `roleopen_` | t_hrcs_permlogroleopen | 角色公开 | 7 |
| `influuser_` | t_hrcs_permloginfluuser | 影响用户 | 19 |
| `roledr_` | t_hrcs_permlograngedr | 数据规则 | 8 |
| `rolebddr_` | t_hrcs_permlograngebddr | 基础资料数据规则 | 9 |
| `influrole_` | t_hrcs_permloginflurole | 影响角色 | 11 |
| `rangeorg_` | t_hrcs_permlograngeorg | 组织范围 | 4 |
| `rangebiz_` | t_hrcs_permlograngebiz | 业务范围 | 4 |

### 2.4 字段类型分布统计

| 类型 | 数量 | 备注 |
|---|---|---|
| TextField | 60+ | 大量 before/after/description 文本字段 · 序列化 JSON 存 |
| BasedataField | 30+ | 各分录 bizapp/entitytype/permitem/role 引用 |
| MulBasedataField | 1 | influadmin（多选管理员组）|
| MuliLangTextField | 1 | description（多语言）|
| ComboField | 4+ | initdatasource/clienttype/dataproperty/influtype/intersection |
| CheckBoxField | 2 | hashandle / influuser_isuserforbidden |
| DateTimeField | 1 | operationtime |
| DateField | 2 | influuser_validstart/validend |
| IntegerField | 1 | influusernumber |
| UserField | 1 | operator |
| 系统字段 | 4 | creator/createtime/modifier/modifytime |

---

## 3. 必填 & 唯一约束

### 3.1 必填字段（实证 scene_doc.json `req:1`）

- `opentitytype`（实体类型）· isvCannotModify · BasedataField → bos_entityobject

### 3.2 系统维护字段（lk=1 · isvCannotModify · 不可改写）

`creator / createtime / modifier / modifytime / description / initdatasource / opentitytype` 共 7 个

### 3.3 唯一约束

无业务唯一索引（日志单据可重复 · 不同 logtype 下 number 可重复）。

### 3.4 多语言表（_l 结尾）

- `t_hrcs_permlog_l`：存 `description` 多语言值
- 字段命名规则：业务表名 + `_l` 后缀
- 跟标品所有支持多语言的实体一致

---

## 4. 性能索引建议（ISV 自助加 · 标品未必有）

### 4.1 高频查询场景

1. **List setFilter 强制带 hashandle='1'**（FP_SF5）→ 建议 `(hashandle, operationtime DESC)` 复合索引
2. **按 operator 用户查询**（quickSearchFieldConds 含 operator.username）→ `t_hrcs_permlog(foperatorid)` 已有 BasedataField 隐式索引
3. **按 logtype 分类筛选** → `t_hrcs_permlog(flogtypeid)` 已有
4. **按 operationtime 时间窗** → 强烈建议加索引 · 查最近 7/30 天日志

### 4.2 已知性能坑

- `description` 是 MuliLangTextField · 跨语言查询走 t_hrcs_permlog_l 表 · 大数据量下慢
- `beforeopdata` / `afteropdata` 是 TextField 但实际存 JSON 串 · 不要 LIKE 这两个字段（全表扫）· ISV 想做"查变更前后某字段值" · 改造成结构化字段或建二级索引

---

## 5. 反模式 · 常见错误

### 5.1 当字段权限来源理解错

❌ 把 `t_hrcs_permlog` 当成"实时权限授权表" · 实际它**只是变更日志**（事后审计追溯用）· 实时权限走 `perm_role / perm_useradmingroup / hrcs_userpermfile` 等其他表

### 5.2 ISV 加字段误区

❌ ISV 给主表 `hrcs_permlog` 加业务字段（标品 ISV 边界 · 标品 formId 严禁 modifyMeta add field）· 正确做法：

1. 创建 ISV 扩展元数据（继承 hrcs_permlog）
2. 加字段到 ISV 子表
3. 同时改 List 列表元数据展示新字段

### 5.3 修改 hashandle 默认值

❌ 想让列表默认显示未处理日志 → 直接 modifyMeta 改 hashandle 默认值或必填？**没用** · setFilter 强制走 `customQFilters.add(hashandle='1')` · 永远过滤已处理。正确：并列挂 ListPlugin · 覆盖 setFilter · 改写 setCustomQFilters

---

## 6. 平台命名规则速查

| 元素 | 规则 | hrcs_permlog 实例 |
|---|---|---|
| 表单 formNumber | `<云>_<域>_<业务>` | `hrcs_permlog`（hr基础服务 + 权限 + log）|
| 物理表 | `t_<formNumber>` | `t_hrcs_permlog` |
| 多语言表 | `t_<formNumber>_l`（**_l 结尾**）| `t_hrcs_permlog_l` |
| 子分录表 | `t_<formNumber><分录名>` | `t_hrcs_permlogbaseinfo` |
| 字段 key | 小驼峰或下划线 | `operationtime` / `roleopen_admingroupname` |
| 字段物理列 | `f<key小写>` 或 `f<key>id`（基础资料）| `foperatorid` / `flogtypeid` |
| 引用基础资料 BasedataField | `bos_*` 平台 / `hr*_*` HR 域 / `hrcs_*` 本域 | `hrcs_permlogtype` 自定义 / `bos_user` 平台 |

### 6.1 反模式（铁律）

- ❌ **继承场景专属类**：PermLogListPlugin 是 final 标品类 · ISV **不可继承** · 用并列挂插件
- ❌ 继承 HBP 通用模板（HRBaseDataImportEdit/HRCertCheckEdit/HRBaseUeEdit/HRHiesButtonSwitchPlugin/ForbidUrlOpenPlugin）虽可 · 但实际业务跟它们无关 · 别绕弯
- ❌ 继承 @SdkInternal 类（HisModelOPCommonPlugin/HisUniqueValidateOp 等）· 平台内部类 · 升级即坏

### 6.2 列表三层模型

- **L0 BillFormModel**：单据视图（permlog 是它）
- **L1 BillList 控件**：列表渲染层（PermLogListPlugin.billListHyperLinkClick 监听这层）
- **L2 IListView/ListView**：视图接口/实现（getSelectedRows / getCurrentSelectedRowInfo）
- **L3 数据层**：DataEntity 通过 IDataModel 跟 List 解耦 · permlog 不直接读

### 6.3 日志场景特性（permlog 跟其他 hrcs 编辑场景的差异）

| 维度 | hrcs_permlog（日志）| hrcs_dynascheme（编辑）|
|---|---|---|
| 是否可编辑 | **✗ 只读** | ✓ |
| 是否走 save/audit/delete | ✗ | ✓ 标品有 OP 链 |
| 是否 HisModel | ✗ | ✗ |
| 跨表 JOIN | **强**（13 张表）| 中（5-6 张）|
| ISV 扩展点 | setFilter / 字段隐藏 / 跨场景日志归集 | save/audit Validator + propertyChanged 联动 |
| BEC 角色 | **0**（不发不订）| 0（标品不发 · ISV 自建）|

---

## 7. 参考资料

- `scene_doc.json` · 114 字段全量
- `scene_doc_lite.json` · 字段清单 lite
- `_auto_field_props.md` · 字段属性
- `_auto_inherit_chain.md` · 继承链 L0=hbp_bd_originaltpl
- `probe_out/hrcs_permlog/main_form_schema.txt` · schema 文本
- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java` · 主列表插件
- `form_lifecycle_rules.json` · 14 条 FP 规则
- `_shared/platform_rules.json` · PR-001/007/010 ISV 边界
