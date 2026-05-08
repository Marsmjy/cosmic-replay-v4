# hrcs_odc_field_perm — 组织基础资料控权

**表单编码**: `hrcs_odc_field_perm`  
**表单ID**: `4=MZDBQYFQ2+`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_odc_field_perm（组织基础资料控权） [BaseEntity]

- **数据库表**: `t_hrcs_odc_field_perm`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| entitynum | 实体对象 | TextField | fentitynum | ✓ |  |
| adminorgfieldname | 部门 | TextField | fadminorgfieldname |  |  |
| positionfieldname | 岗位 | TextField | fpositionfieldname |  |  |
| jobfieldname | 职位 | TextField | fjobfieldname |  |  |
| jobgradescmfieldname | 职等方案 | TextField | fjobgradescmfieldname |  |  |
| joblevelscmfieldname | 职级方案 | TextField | fjoblevelscmfieldname |  |  |
| jobgradefieldname | 职等 | TextField | fjobgradefieldname |  |  |
| joblevelfieldname | 职级 | TextField | fjoblevelfieldname |  |  |
| jobscmorgfieldname | 职位体系管理组织 | TextField | fjobscmorgfieldname |  |  |
| followorgdesign | 权限范围是否根据职位管理体系组织控制 | CheckBoxField | ffolloworgdesign |  |  |
| isdopropertychange | 是否处理值改变事件 | CheckBoxField | fisdopropertychange |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| jobscmfieldname | 职位体系方案 | TextField | fjobscmfieldname |  |  |

