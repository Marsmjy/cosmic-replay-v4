# hrpi_perrprecord — 奖惩记录

**表单编码**: `hrpi_perrprecord`  
**表单ID**: `15BJTFGI=2CS`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_perrprecord（奖惩记录） [BaseEntity]

- **数据库表**: `t_hrpi_perrprecord`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| issingle | 单行显示 | CheckBoxField | fissingle |  |  |
| type | 奖惩类别 | BasedataField | ftypeid |  | hbss_rewpnmtype |
| level | 奖惩级别 | BasedataField | flevelid |  | hbss_rewpnmlevel |
| rewarddate | 奖惩日期 | DateField | frewarddate |  |  |
| revocationdate | 撤销日期 | DateField | frevocationdate |  |  |
| cutpayment | 薪资发放扣款 | CheckBoxField | fcutpayment |  |  |
| relcredit | 诚信相关 | CheckBoxField | frelcredit |  |  |
| currency | 币别 | CurrencyField | fcurrencyid |  | bd_currency |
| content | 奖惩内容 | MuliLangTextField | fcontent |  |  |
| unit | 奖惩单位 | MuliLangTextField | funit |  |  |
| witness | 证明人 | MuliLangTextField | fwitness |  |  |
| reasonrevocate | 撤销原因 | MuliLangTextField | freasonrevocate |  |  |
| documentnumber | 奖惩文号 | MuliLangTextField | fdocumentnumber |  |  |
| revocationnumber | 撤销文号 | MuliLangTextField | frevocationnumber |  |  |
| matchstage | 对应期次 | MuliLangTextField | fmatchstage |  |  |
| money | 金额 | DecimalField | fmoney |  |  |
| flag | 奖惩标识 | ComboField | fflag |  |  |
| revocation | 撤销奖励（惩罚） | CheckBoxField | frevocation |  |  |

