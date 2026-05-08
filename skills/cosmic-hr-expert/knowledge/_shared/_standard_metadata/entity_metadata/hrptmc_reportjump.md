# hrptmc_reportjump — 报表跳转配置

**表单编码**: `hrptmc_reportjump`  
**表单ID**: `3EWQ8BT=+F/U`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_reportjump（报表跳转配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_rptjump` | BaseEntity | 主表 |
| `t_hrptmc_rptjumprule` | EntryEntity | 跳转规则 |
| `t_hrptmc_fieldmap` | EntryEntity | 跨分析对象跳转字段映射 |

### 字段列表 — t_hrptmc_rptjump（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_rptjump.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_rptjump.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_rptjump.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_rptjump.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrptmc_rptjump.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_rptjump.finitdatasource |  |  |
| report | 报表 | BasedataField | t_hrptmc_rptjump.freportid |  | hrptmc_reportmanage |
| rulegroup | 规则组 | TextField | t_hrptmc_rptjump.frulegroup |  |  |
| jumpreport | 跳转报表 | BasedataField | t_hrptmc_rptjump.fjumpreportid |  | hrptmc_reportmanage |
| carryfilter | 携带筛选条件 | CheckBoxField | t_hrptmc_rptjump.fcarryfilter |  |  |
| index | 排序号 | IntegerField | t_hrptmc_rptjump.findex |  |  |
| workrpt | 工作表 | BasedataField | t_hrptmc_rptjump.fworkrptid |  | hrptmc_workreport |
| jumpsameanobj | 当前分析对象内跳转 | CheckBoxField | t_hrptmc_rptjump.fjumpsameanobj |  |  |
| jumpruleentry | 跳转规则 | EntryEntity | → t_hrptmc_rptjumprule |  |  |
| fieldmapentry | 跨分析对象跳转字段映射 | EntryEntity | → t_hrptmc_fieldmap |  |  |

### 字段列表 — t_hrptmc_rptjumprule（跳转规则·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| type | 源字段类别 | ComboField | t_hrptmc_rptjumprule.ftype |  |  |
| sourcefield | 源字段 | TextField | t_hrptmc_rptjumprule.fsourcefield |  |  |
| targetfield | 目标字段 | TextField | t_hrptmc_rptjumprule.ftargetfield |  |  |

### 字段列表 — t_hrptmc_fieldmap（跨分析对象跳转字段映射·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| mapsource | 源字段 | TextField | t_hrptmc_fieldmap.fmapsource |  |  |
| maptarget | 目标字段 | TextField | t_hrptmc_fieldmap.fmaptarget |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_rptjump（主表） | 13 |
| t_hrptmc_rptjumprule（跳转规则） | 3 |
| t_hrptmc_fieldmap（跨分析对象跳转字段映射） | 2 |

