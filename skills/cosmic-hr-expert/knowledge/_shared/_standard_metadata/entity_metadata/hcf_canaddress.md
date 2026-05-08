# hcf_canaddress — 拟入职人员地址

**表单编码**: `hcf_canaddress`  
**表单ID**: `15YWLDUCFNH=`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_canaddress（拟入职人员地址） [BaseEntity]

- **数据库表**: `t_hcf_canaddress`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| candidate | 拟入职人员 | BasedataField | fcandidateid |  | hcf_candidate |
| countrycode | 国家/地区 | BasedataField | fcountrycodeid |  | bd_country |
| addresstype | 地址类型 | BasedataField | faddresstypeid |  | hbss_addresstype |
| addressinfo | 详细地址信息 | MuliLangTextField | faddressinfo |  |  |
| postalcode | 邮编 | TextField | fpostalcode |  |  |

