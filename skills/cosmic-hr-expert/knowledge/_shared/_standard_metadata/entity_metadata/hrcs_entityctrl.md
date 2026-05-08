# hrcs_entityctrl — 业务对象维度映射

**表单编码**: `hrcs_entityctrl`  
**表单ID**: `1ZKKMM842J/D`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_entityctrl（业务对象维度映射） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_entityctrl` | BaseEntity | 主表 |
| `t_hrcs_entitydimentry` | EntryEntity | 业务对象维度映射分录 |

### 字段列表 — t_hrcs_entityctrl（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_entityctrl.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_entityctrl.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_entityctrl.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_entityctrl.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_entityctrl.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_entityctrl.finitdatasource |  |  |
| entitytype | 业务对象 | BasedataField | t_hrcs_entityctrl.fentitytypeid | ✓ | bos_entityobject |
| bizapp | 应用 | BasedataField | t_hrcs_entityctrl.fappid |  | hbp_devportal_bizapp |
| entryentity | 业务对象维度映射分录 | EntryEntity | → t_hrcs_entitydimentry |  |  |

### 字段列表 — t_hrcs_entitydimentry（业务对象维度映射分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| propkey | 字段 | TextField | t_hrcs_entitydimentry.fpropkey | ✓ |  |
| propname | 字段名 | TextField | — |  |  |
| dimension | 维度 | BasedataField | t_hrcs_entitydimentry.fdimensionid | ✓ | hrcs_dimension |
| ismust | 必选 | CheckBoxField | t_hrcs_entitydimentry.fismust |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_entitydimentry.fissyspreset |  |  |
| desc | 描述 | MuliLangTextField | t_hrcs_entitydimentry.fdescription |  |  |
| authrange | 控权范围 | ComboField | t_hrcs_entitydimentry.fauthrange | ✓ |  |
| needhisver | 是否需要历史版本 | CheckBoxField | t_hrcs_entitydimentry.fneedhisver |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_entityctrl（主表） | 8 |
| t_hrcs_entitydimentry（业务对象维度映射分录） | 8 |
| 无数据库列 | 1 |

