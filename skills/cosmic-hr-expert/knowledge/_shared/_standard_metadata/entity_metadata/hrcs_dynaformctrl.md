# hrcs_dynaformctrl — 虚字段数据控权配置

**表单编码**: `hrcs_dynaformctrl`  
**表单ID**: `2XASLI2R8GSQ`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynaformctrl（虚字段数据控权配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_dynaformctrl` | BaseEntity | 主表 |
| `t_hrcs_dynaformfield` | EntryEntity | 动态表单属性 |

### 字段列表 — t_hrcs_dynaformctrl（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_dynaformctrl.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_dynaformctrl.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_dynaformctrl.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_dynaformctrl.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_dynaformctrl.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_dynaformctrl.finitdatasource |  |  |
| entitytype | 业务对象 | BasedataField | t_hrcs_dynaformctrl.fentitytypeid | ✓ | bos_entityobject |
| app | 应用 | BasedataField | t_hrcs_dynaformctrl.fappid |  | hbp_devportal_bizapp |
| entryentity | 动态表单属性 | EntryEntity | → t_hrcs_dynaformfield |  |  |

### 字段列表 — t_hrcs_dynaformfield（动态表单属性·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| propkey | 属性 | TextField | t_hrcs_dynaformfield.fpropkey | ✓ |  |
| propname | 名称 | MuliLangTextField | t_hrcs_dynaformfield.fpropname | ✓ |  |
| bdtype | 基础资料 | BasedataField | t_hrcs_dynaformfield.fbdtype |  | bos_entityobject |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_dynaformfield.fissyspreset |  |  |
| bucafunc | 职能 | BasedataField | t_hrcs_dynaformfield.fbucafuncid |  | hbss_hrbucafunc |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_dynaformctrl（主表） | 8 |
| t_hrcs_dynaformfield（动态表单属性） | 5 |

