# haos_staff — 编制信息维护

**表单编码**: `haos_staff`  
**表单ID**: `2IV213+ATZBR`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_staff（编制信息维护） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_haos_staff` | BaseEntity | 主表 |
| `t_haos_useorgdetail` | EntryEntity | 单据体 |
| `（虚拟分录）` | EntryEntity | 责任组织编制 |
| `（虚拟分录）` | EntryEntity | 使用组织编制 |
| `（虚拟分录）` | EntryEntity | 岗位编制 |
| `（虚拟分录）` | EntryEntity | 职位编制 |
| `（虚拟分录）` | EntryEntity | 用工关系类型编制 |
| `（虚拟分录）` | EntryEntity | 扩展维度1 |
| `（虚拟分录）` | EntryEntity | 扩展维度2 |
| `（虚拟分录）` | EntryEntity | 扩展维度3 |
| `（虚拟分录）` | EntryEntity | 扩展维度4 |
| `（虚拟分录）` | EntryEntity | 扩展维度5 |
| `（虚拟分录）` | EntryEntity | 扩展维度6 |
| `（虚拟分录）` | EntryEntity | 扩展维度7 |
| `（虚拟分录）` | EntryEntity | 扩展维度8 |
| `（虚拟分录）` | EntryEntity | 扩展维度9 |
| `（虚拟分录）` | EntryEntity | 扩展维度10 |
| `（虚拟分录）` | EntryEntity | 扩展维度11 |
| `（虚拟分录）` | EntryEntity | 扩展维度12 |
| `（虚拟分录）` | EntryEntity | 扩展维度13 |
| `（虚拟分录）` | EntryEntity | 扩展维度14 |
| `（虚拟分录）` | EntryEntity | 扩展维度15 |
| `（虚拟分录）` | EntryEntity | 扩展维度16 |
| `（虚拟分录）` | EntryEntity | 扩展维度17 |
| `（虚拟分录）` | EntryEntity | 扩展维度18 |
| `（虚拟分录）` | EntryEntity | 扩展维度19 |
| `（虚拟分录）` | EntryEntity | 扩展维度20 |
| `（虚拟分录）` | SubEntryEntity | 岗位编制 |
| `（虚拟分录）` | SubEntryEntity | 职位编制 |
| `（虚拟分录）` | SubEntryEntity | 用工关系类型编制 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度1单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度2单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度3单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度4单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度5单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度6单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度7单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度8单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度9单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度10单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度11单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度12单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度13单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度14单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度15单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度16单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度17单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度18单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度19单据体 |
| `（虚拟分录）` | SubEntryEntity | 扩展维度20单据体 |
| `t_haos_staffmuldimension` | MulEmployeeField子表 | 编制维度 |

### 字段列表 — t_haos_staff（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_haos_staff.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_haos_staff.fname |  |  |
| status | 数据状态 | BillStatusField | t_haos_staff.fstatus |  |  |
| creator | 创建人 | CreaterField | t_haos_staff.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_haos_staff.fmodifierid |  | bos_user |
| enable | 业务状态 | BillStatusField | t_haos_staff.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_haos_staff.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_haos_staff.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_haos_staff.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_haos_staff.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_haos_staff.fdescription |  |  |
| index | 排序号 | IntegerField | t_haos_staff.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_haos_staff.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_haos_staff.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_haos_staff.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_haos_staff.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_haos_staff.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_haos_staff.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_haos_staff.foriname |  |  |
| orgteam | 使用组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| buseorg | 使用组织 | HRAdminOrgField | t_haos_staff.fuseorgid | ✓ | haos_adminorghrf7 |
| bcontrolstrategy | 控编方式 | ComboField | t_haos_staff.fcontrolstrategy | ✓ |  |
| belasticcontrol | 弹性方式 | ComboField | t_haos_staff.felasticcontrol |  |  |
| belasticcount | 弹性额度 | IntegerField | t_haos_staff.felasticcount |  |  |
| byearstaff | 直属 | IntegerField | t_haos_staff.fyearstaff |  |  |
| bhalfyearstaff1 | 上半年 | IntegerField | t_haos_staff.fhalfyearstaff1 |  |  |
| bhalfyearstaff2 | 下半年 | IntegerField | t_haos_staff.fhalfyearstaff2 |  |  |
| bquarterstaff1 | 第一季度 | IntegerField | t_haos_staff.fquarterstaff1 |  |  |
| bquarterstaff2 | 第二季度 | IntegerField | t_haos_staff.fquarterstaff2 |  |  |
| bquarterstaff3 | 第三季度 | IntegerField | t_haos_staff.fquarterstaff3 |  |  |
| bquarterstaff4 | 第四季度 | IntegerField | t_haos_staff.fquarterstaff4 |  |  |
| bmonthstaff1 | 1月 | IntegerField | — |  |  |
| bmonthstaff2 | 2月 | IntegerField | — |  |  |
| bmonthstaff3 | 3月 | IntegerField | — |  |  |
| bmonthstaff4 | 4月 | IntegerField | — |  |  |
| bmonthstaff5 | 5月 | IntegerField | — |  |  |
| bmonthstaff6 | 6月 | IntegerField | — |  |  |
| bmonthstaff7 | 7月 | IntegerField | — |  |  |
| bmonthstaff8 | 8月 | IntegerField | — |  |  |
| bmonthstaff9 | 9月 | IntegerField | — |  |  |
| bmonthstaff10 | 10月 | IntegerField | — |  |  |
| bmonthstaff11 | 11月 | IntegerField | — |  |  |
| bmonthstaff12 | 12月 | IntegerField | — |  |  |
| bstaffdimension | 编制维度 | MulBasedataField | — |  |  |
| bparentlongname | 上级组织长名称 | MuliLangTextField | — |  |  |
| brealnumwithsub | 含下级 | IntegerField | — |  |  |
| blevel | 物理层级 | IntegerField | — |  |  |
| bdirectnum | 直属 | IntegerField | — |  |  |
| bstaffnumwithsub | 已分配额度 | IntegerField | — |  |  |
| byearstaffnum | 直属 | IntegerField | — |  |  |
| bhalfyearstaff1num | 上半年 | IntegerField | — |  |  |
| bhalfyearstaff2num | 下半年 | IntegerField | — |  |  |
| bquarterstaff1num | 第一季度 | IntegerField | — |  |  |
| bquarterstaff2num | 第二季度 | IntegerField | — |  |  |
| bquarterstaff3num | 第三季度 | IntegerField | — |  |  |
| bquarterstaff4num | 第四季度 | IntegerField | — |  |  |
| bmonthstaff1num | 1月 | IntegerField | — |  |  |
| bmonthstaff2num | 2月 | IntegerField | — |  |  |
| bmonthstaff3num | 3月 | IntegerField | — |  |  |
| bmonthstaff4num | 4月 | IntegerField | — |  |  |
| bmonthstaff5num | 5月 | IntegerField | — |  |  |
| bmonthstaff6num | 6月 | IntegerField | — |  |  |
| bmonthstaff7num | 7月 | IntegerField | — |  |  |
| bmonthstaff8num | 8月 | IntegerField | — |  |  |
| bmonthstaff9num | 9月 | IntegerField | — |  |  |
| bmonthstaff10num | 10月 | IntegerField | — |  |  |
| bmonthstaff11num | 11月 | IntegerField | — |  |  |
| bmonthstaff12num | 12月 | IntegerField | — |  |  |
| bstaffnumwithsubnum | 已分配额度 | IntegerField | — |  |  |
| byearstaffnumwithsub | 含下级 | IntegerField | — |  |  |
| byearstaffnumwithsubnum | 含下级 | IntegerField | — |  |  |
| bstructlongnumber | 组织长编码 | TextField | — |  |  |
| buseorgboid | 使用组织当前版本 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| bdutyorg | 责任组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| bhavesubentry | 是否存在下级 | CheckBoxField | — |  |  |
| showdisable | 显示停用组织 | CheckBoxField | — |  |  |
| year | 编制年份 | DateTimeField | t_haos_staff.fyear | ✓ |  |
| staffcycle | 填报期间 | BasedataField | t_haos_staff.fstaffcycleid | ✓ | haos_staffcycle |
| staffproject | 控编规则 | BasedataField | t_haos_staff.fstaffprojectid | ✓ | haos_staffproject |
| staffdimension | 编制维度 | MulBasedataField | t_haos_staffmuldimension（子表） |  |  |
| staffcontrolstrategy | 控编方式 | ComboField | t_haos_staff.fcontrolstrategy | ✓ |  |
| staffelasticcontrol | 弹性方式 | ComboField | t_haos_staff.felasticcontrol |  |  |
| staffelasticcount | 弹性额度 | IntegerField | t_haos_staff.felasticcount |  |  |
| org | 组织体系管理组织 | OrgField | t_haos_staff.forgid | ✓ | bos_org |
| entryentity | 单据体 | EntryEntity | → t_haos_useorgdetail |  |  |
| aentryentity | 责任组织编制 | EntryEntity | → （虚拟分录） |  |  |
| bentryentity_import | 使用组织编制 | EntryEntity | → （虚拟分录） |  |  |
| centryentity_import | 岗位编制 | EntryEntity | → （虚拟分录） |  |  |
| dentryentity_import | 职位编制 | EntryEntity | → （虚拟分录） |  |  |
| eentryentity_import | 用工关系类型编制 | EntryEntity | → （虚拟分录） |  |  |
| z1entryentity_import | 扩展维度1 | EntryEntity | → （虚拟分录） |  |  |
| z2entryentity_import | 扩展维度2 | EntryEntity | → （虚拟分录） |  |  |
| z3entryentity_import | 扩展维度3 | EntryEntity | → （虚拟分录） |  |  |
| z4entryentity_import | 扩展维度4 | EntryEntity | → （虚拟分录） |  |  |
| z5entryentity_import | 扩展维度5 | EntryEntity | → （虚拟分录） |  |  |
| z6entryentity_import | 扩展维度6 | EntryEntity | → （虚拟分录） |  |  |
| z7entryentity_import | 扩展维度7 | EntryEntity | → （虚拟分录） |  |  |
| z8entryentity_import | 扩展维度8 | EntryEntity | → （虚拟分录） |  |  |
| z9entryentity_import | 扩展维度9 | EntryEntity | → （虚拟分录） |  |  |
| z10entryentity_import | 扩展维度10 | EntryEntity | → （虚拟分录） |  |  |
| z11entryentity_import | 扩展维度11 | EntryEntity | → （虚拟分录） |  |  |
| z12entryentity_import | 扩展维度12 | EntryEntity | → （虚拟分录） |  |  |
| z13entryentity_import | 扩展维度13 | EntryEntity | → （虚拟分录） |  |  |
| z14entryentity_import | 扩展维度14 | EntryEntity | → （虚拟分录） |  |  |
| z15entryentity_import | 扩展维度15 | EntryEntity | → （虚拟分录） |  |  |
| z16entryentity_import | 扩展维度16 | EntryEntity | → （虚拟分录） |  |  |
| z17entryentity_import | 扩展维度17 | EntryEntity | → （虚拟分录） |  |  |
| z18entryentity_import | 扩展维度18 | EntryEntity | → （虚拟分录） |  |  |
| z19entryentity_import | 扩展维度19 | EntryEntity | → （虚拟分录） |  |  |
| z20entryentity_import | 扩展维度20 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — t_haos_useorgdetail（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| useorg | 使用组织 | HRAdminOrgField | t_haos_useorgdetail.fuseorgid |  | haos_adminorghrf7 |

### 字段列表 — （责任组织编制·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （使用组织编制·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （岗位编制·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （职位编制·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （用工关系类型编制·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度1·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度2·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度3·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度4·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度5·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度6·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度7·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度8·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度9·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度10·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度11·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度12·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度13·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度14·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度15·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度16·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度17·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度18·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度19·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度20·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （岗位编制·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （职位编制·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （用工关系类型编制·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度1单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度2单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度3单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度4单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度5单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度6单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度7单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度8单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度9单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度10单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度11单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度12单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度13单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度14单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度15单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度16单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度17单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度18单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度19单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （扩展维度20单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_staff（主表） | 84 |
| t_haos_useorgdetail（单据体） | 1 |
| （责任组织编制） | 1470 |
| （使用组织编制） | 1470 |
| （岗位编制） | 1470 |
| （职位编制） | 1470 |
| （用工关系类型编制） | 1470 |
| （扩展维度1） | 1470 |
| （扩展维度2） | 1470 |
| （扩展维度3） | 1470 |
| （扩展维度4） | 1470 |
| （扩展维度5） | 1470 |
| （扩展维度6） | 1470 |
| （扩展维度7） | 1470 |
| （扩展维度8） | 1470 |
| （扩展维度9） | 1470 |
| （扩展维度10） | 1470 |
| （扩展维度11） | 1470 |
| （扩展维度12） | 1470 |
| （扩展维度13） | 1470 |
| （扩展维度14） | 1470 |
| （扩展维度15） | 1470 |
| （扩展维度16） | 1470 |
| （扩展维度17） | 1470 |
| （扩展维度18） | 1470 |
| （扩展维度19） | 1470 |
| （扩展维度20） | 1470 |
| （岗位编制） | 1470 |
| （职位编制） | 1470 |
| （用工关系类型编制） | 1470 |
| （扩展维度1单据体） | 1470 |
| （扩展维度2单据体） | 1470 |
| （扩展维度3单据体） | 1470 |
| （扩展维度4单据体） | 1470 |
| （扩展维度5单据体） | 1470 |
| （扩展维度6单据体） | 1470 |
| （扩展维度7单据体） | 1470 |
| （扩展维度8单据体） | 1470 |
| （扩展维度9单据体） | 1470 |
| （扩展维度10单据体） | 1470 |
| （扩展维度11单据体） | 1470 |
| （扩展维度12单据体） | 1470 |
| （扩展维度13单据体） | 1470 |
| （扩展维度14单据体） | 1470 |
| （扩展维度15单据体） | 1470 |
| （扩展维度16单据体） | 1470 |
| （扩展维度17单据体） | 1470 |
| （扩展维度18单据体） | 1470 |
| （扩展维度19单据体） | 1470 |
| （扩展维度20单据体） | 1470 |
| t_haos_staffmuldimension（MulEmployeeField子表） | 1 |
| 无数据库列 | 222 |

