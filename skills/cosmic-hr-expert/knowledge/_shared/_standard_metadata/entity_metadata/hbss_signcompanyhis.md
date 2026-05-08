# hbss_signcompanyhis — 聘用单位历史

**表单编码**: `hbss_signcompanyhis`  
**表单ID**: `26XRJ39UX6KT`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_signcompanyhis（聘用单位历史） [BaseEntity]

- **数据库表**: `t_hbss_signcompanyhis`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建日期 | CreateDateField | fcreatetime |  |  |
| modifier | 变更人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 变更日期 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| createorg | 创建组织 | OrgField | fcreateorgid | ✓ | bos_org |
| number | 编码 | TextField | fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | fname | ✓ |  |
| ctrlstrategy | 控制策略 | ComboField | fctrlstrategy | ✓ |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| changetype | 变更类型 | ComboField | fchangetype |  |  |
| changereason | 变更原因 | MuliLangTextField | fchangereason |  |  |
| ischangecon | 是否需要改签合同 | CheckBoxField | fischangecon |  |  |
| vid | 关联聘用单位ID | BigIntField | fid |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| representative | 法定代表人/主要负责人 | TextField | flegalrepresent | ✓ |  |
| unifiedcode | 统一社会信用代码 | TextField | funifiedcode |  |  |
| address | 地址 | MuliLangTextField | faddress |  |  |
| contactnumber | 联系电话 | TextField | fcontactnumber |  |  |
| postalcode | 邮编 | TextField | fpostalcode |  |  |
| reorg | 关联法人 | BasedataField | freorgid |  | hbss_lawentity |
| lawentity | 法律实体 | BasedataField | fcorporateorgid |  | hbss_lawentity |
| busperiod | 营业期限 | DateRangeField | fbusperiod |  |  |

