# hrptmc_rptdispscmrow — 报表显示方案配置_行

**表单编码**: `hrptmc_rptdispscmrow`  
**表单ID**: `36K7QKR3+J+=`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_rptdispscmrow（报表显示方案配置_行） [BaseEntity]

- **数据库表**: `t_hrptmc_rptdispscmrow`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| bizindex | 顺序号 | IntegerField | fbizindex |  |  |
| rptdispscm | 报表显示方案 | BasedataField | frptdispscmid |  | hrptmc_rptdisscmety |
| rowfield | 行字段 | BasedataField | frowfieldid |  | hrptmc_rowfield |
| enable | 允许用户编辑 | CheckBoxField | fenable |  |  |
| defaultscheme | 默认方案 | CheckBoxField | fdefaultscheme |  |  |
| hide | 隐藏列 | CheckBoxField | fhide |  |  |
| freeze | 冻结列 | CheckBoxField | ffreeze |  |  |
| tableheadlv2 | 二级表头 | MuliLangTextField | ftableheadlv2 |  |  |
| name | 显示名称 | MuliLangTextField | fdisplayname |  |  |
| treeid | 树形ID | TextField | ftreeid |  |  |
| parenttreeid | 树形父ID | TextField | fparenttreeid |  |  |

