# hrptmc_reflineconf — 图表参考线配置

**表单编码**: `hrptmc_reflineconf`  
**表单ID**: `4NX3XD0LV2=W`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_reflineconf（图表参考线配置） [BaseEntity]

- **数据库表**: `t_hrptmc_reflineconf`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| name | 显示名称 | MuliLangTextField | fname |  |  |
| valmethod | 取值方式 | ComboField | fvalmethod |  |  |
| reffield | 参考字段 | TextField | freffield |  |  |
| calrule | 计算规则 | ComboField | fcalrule |  |  |
| linetype | 线条类型 | ComboField | flinetype |  |  |
| linecolor | 线条颜色 | TextField | flinecolor |  |  |
| fixvalue | 固定值 | DecimalField | ffixvalue |  |  |
| displaycontent | 显示内容 | MulComboField | fdisplaycontent |  |  |
| report | 报表 | BasedataField | freportid |  | hrptmc_reportmanage |
| workrpt | 工作表 | BasedataField | fworkrptid |  | hrptmc_workreport |
| index | 排序号 | IntegerField | findex |  |  |

