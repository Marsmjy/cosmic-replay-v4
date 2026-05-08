# hrptmc_anobjconfighis — 分析对象配置历史版本

**表单编码**: `hrptmc_anobjconfighis`  
**表单ID**: `47I=K0MF8RJ=`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_anobjconfighis（分析对象配置历史版本） [BaseEntity]

- **数据库表**: `t_hrptmc_anobjconfighis`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| queryfield | 查询字段json | TextField | fqueryfield |  |  |
| joinentity | 关联实体json | TextField | fjoinentity |  |  |
| entityrelation | 关联关系和条件json | TextField | fentityrelation |  |  |
| calculatefield | 计算字段json | TextField | fcalculatefield |  |  |
| datafilter | 数据过滤json | TextField | fdatafilter |  |  |
| objecttype | 对象类型 | TextField | fobjecttype |  |  |
| pivotindexes | 分析对象行列转置指标json | TextField | fpivotindexes |  |  |
| groupfields | 分组赋值字段json | TextField | fgroupfields |  |  |
| starttime | 开始时间 | DateTimeField | fstarttime |  |  |
| endtime | 结束时间 | DateTimeField | fendtime |  |  |
| anobjid | 分析对象id | BigIntField | fanobjid |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| number | 编码 | TextField | fnumber |  |  |

