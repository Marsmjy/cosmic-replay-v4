# hrptmc_reportconfighis — 报表配置历史版本

**表单编码**: `hrptmc_reportconfighis`  
**表单ID**: `47ICKKT6=KN8`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_reportconfighis（报表配置历史版本） [BaseEntity]

- **数据库表**: `t_hrptmc_reportconfighis`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| rptmanageconfig | 报表管理配置json | TextField | frptmanageconfig |  |  |
| calculatefield | 报表计算字段json | TextField | fcalculatefield |  |  |
| datesplit | 日期拆分字段json | TextField | fdatesplit |  |  |
| presetindex | 预置指标字段json | TextField | fpresetindex |  |  |
| starttime | 开始时间 | DateTimeField | fstarttime |  |  |
| endtime | 结束时间 | DateTimeField | fendtime |  |  |
| reportid | 报表id | BigIntField | freportid |  |  |
| anobjid | 分析对象id | BigIntField | fanobjid |  |  |
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |

