# haos_structproconfig — 架构方案配置

**表单编码**: `haos_structproconfig`  
**表单ID**: `3BWQ64IF0255`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_structproconfig（架构方案配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_haos_structproconfig` | BaseEntity | 主表 |
| `t_haos_strprofieldentry` | EntryEntity | 单据体 |
| `t_haos_strprojectentry` | SubEntryEntity | 子单据体 |
| `t_haos_strconotclassify` | MulEmployeeField子表 | 组织分类 |

### 字段列表 — t_haos_structproconfig（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_haos_structproconfig.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_haos_structproconfig.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_haos_structproconfig.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_haos_structproconfig.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_haos_structproconfig.finitdatasource |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_haos_structproconfig.fissyspreset |  |  |
| entitytype | 业务对象 | BasedataField | t_haos_structproconfig.fentitytypeid | ✓ | bos_entityobject |
| bizapp | 应用 | BasedataField | t_haos_structproconfig.fappid |  | hbp_devportal_bizapp |
| entryentity | 单据体 | EntryEntity | → t_haos_strprofieldentry |  |  |

### 字段列表 — t_haos_strprofieldentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| columnname | 字段名称 | TextField | t_haos_strprofieldentry.fcolumnname |  |  |
| description | 描述 | MuliLangTextField | t_haos_strprofieldentry.fdescription |  |  |
| isallowmodify | 是否允许修改 | ComboField | t_haos_strprofieldentry.fisallowmodify |  |  |
| entryissyspreset | 系统预置 | CheckBoxField | t_haos_strprofieldentry.fissyspreset |  |  |
| otclassify | 组织分类 | MulBasedataField | t_haos_strconotclassify（子表） |  |  |
| propkey | 字段 | TextField | t_haos_strprofieldentry.fcolumn |  |  |
| subentryentity | 子单据体 | SubEntryEntity | → t_haos_strprojectentry |  |  |

### 字段列表 — t_haos_strprojectentry（子单据体·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| structproject | 架构方案 | BasedataField | t_haos_strprojectentry.fstructprojectid | ✓ | haos_structproject |
| subentryissyspreset | 系统预置 | CheckBoxField | t_haos_strprojectentry.fissyspreset |  |  |
| enable | 状态 | ComboField | t_haos_strprojectentry.fenable |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_structproconfig（主表） | 8 |
| t_haos_strprofieldentry（单据体） | 6 |
| t_haos_strprojectentry（子单据体） | 3 |
| t_haos_strconotclassify（MulEmployeeField子表） | 1 |

