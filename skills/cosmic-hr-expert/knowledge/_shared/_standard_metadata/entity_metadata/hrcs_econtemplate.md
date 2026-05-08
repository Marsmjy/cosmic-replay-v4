# hrcs_econtemplate — 电子签署配置

**表单编码**: `hrcs_econtemplate`  
**表单ID**: `2A9EWZYC3YRF`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_econtemplate（电子签署配置） [BaseEntity]

- **数据库表**: `t_hrcs_econtemplate`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| signtype | 签署类型 | RadioGroupField | fsigntype | ✓ |  |
| radiofield | 单签（企业签） | RadioField | — |  |  |
| radiofield1 | 单签（个人签） | RadioField | — |  |  |
| radiofield2 | 双签（企业签+个人签） | RadioField | — |  |  |
| offsetx | 甲方签署坐标偏移量-X轴 | IntegerField | foffsetx | ✓ |  |
| offsety | 甲方签署坐标偏移量-Y轴 | IntegerField | foffsety | ✓ |  |
| offsetxdouble | 乙方签署坐标偏移量-X轴 | IntegerField | foffsetxdouble | ✓ |  |
| offsetydouble | 乙方签署坐标偏移量-Y轴 | IntegerField | foffsetydouble | ✓ |  |
| sealtypeid | 企业印章类型 | BasedataField | fsealtypeid | ✓ | hrcs_esignsealtype |
| keyword | 企业签署位置 | TextField | fkeyword | ✓ |  |
| corporateseal | 加盖法人章 | CheckBoxField | fcorporateseal |  |  |
| legalsealkey | 法人章签章位置 | TextField | flegalsealkey |  |  |
| sealway | 企业印章加盖方式 | ComboField | fsealway | ✓ |  |
| acrosspagesign | 骑缝章 | CheckBoxField | facrosspagesign |  |  |
| keyworddouble | 个人签署位置 | TextField | fkeyworddouble | ✓ |  |
| onlyfirstauth | 个人签署前进行认证 | CheckBoxField | fonlyfirstauth |  |  |
| issigntragectory | 显示签名轨迹 | CheckBoxField | fissigntragectory |  |  |
| isshowdate | 显示签署时间 | CheckBoxField | fisshowdate |  |  |
| cloudtemp | 云平台合同模板 | TextField | fcloudtemp |  |  |
| personsignway | 个人签名方式 | ComboField | fpersonsignway | ✓ |  |
| lastpageflag | 所有文档阅读至末页才可提交签署 | CheckBoxField | fcheckboxfield |  |  |
| readtimeflag | 提交签署前要求最短阅读时长(秒)： | CheckBoxField | freadtimeflag |  |  |
| readingtime | 阅读时间 | StepperField | freadingtime |  |  |
| signidea | 签署意愿方式 | MulComboField | fsignidea | ✓ |  |

