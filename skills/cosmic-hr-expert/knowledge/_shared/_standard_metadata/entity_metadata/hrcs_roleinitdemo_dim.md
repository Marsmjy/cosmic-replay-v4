# hrcs_roleinitdemo_dim — 角色初始化角色维度样例

**表单编码**: `hrcs_roleinitdemo_dim`  
**表单ID**: `3FM72P/64ZP6`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_roleinitdemo_dim（角色初始化角色维度样例） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_roleinitdemo_dim` | BaseEntity | 主表 |
| `t_hrcs_roleinitdemo_dime` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_roleinitdemo_dim（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_roleinitdemo_dim.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_roleinitdemo_dim.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_roleinitdemo_dim.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_roleinitdemo_dim.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_roleinitdemo_dim.finitdatasource |  |  |
| sceneid | 场景ID | IntegerField | t_hrcs_roleinitdemo_dim.fsceneid |  |  |
| scenename | 场景名 | MuliLangTextField | t_hrcs_roleinitdemo_dim.fscenename |  |  |
| scenedesc | 场景描述 | MuliLangTextField | t_hrcs_roleinitdemo_dim.fscenedesc |  |  |
| index | 排序号 | IntegerField | t_hrcs_roleinitdemo_dim.findex |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_roleinitdemo_dime |  |  |

### 字段列表 — t_hrcs_roleinitdemo_dime（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rolenumber | 角色编码 | TextField | t_hrcs_roleinitdemo_dime.frolenumber |  |  |
| bucafuncname | 职能类型 | TextField | t_hrcs_roleinitdemo_dime.fbucafuncname |  |  |
| adminorg | 行政类组织团队 | TextField | t_hrcs_roleinitdemo_dime.fadminorg |  |  |
| salarygroup | 薪资核算组 | TextField | t_hrcs_roleinitdemo_dime.fsalarygroup |  |  |
| servicetpl | 业务数据模板 | TextField | t_hrcs_roleinitdemo_dime.fservicetpl |  |  |
| countryarea | 国家地区 | TextField | t_hrcs_roleinitdemo_dime.fcountryarea |  |  |
| rolename | 角色名字 | MuliLangTextField | t_hrcs_roleinitdemo_dime.frolename |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_roleinitdemo_dim（主表） | 9 |
| t_hrcs_roleinitdemo_dime（单据体） | 7 |

