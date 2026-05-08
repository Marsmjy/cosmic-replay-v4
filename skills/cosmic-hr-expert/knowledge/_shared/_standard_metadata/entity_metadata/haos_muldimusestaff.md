# haos_muldimusestaff — 多维占编明细

**表单编码**: `haos_muldimusestaff`  
**表单ID**: `2OPU5VJ4KF42`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_muldimusestaff（多维占编明细） [BaseEntity]

- **数据库表**: `t_haos_muldimusestaff`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| personstaffinfo | 占编员工信息 | BasedataField | fid |  | haos_personstaffinfo |
| orgusestaffdetail | 组织占编信息 | BasedataField | fuid |  | haos_orgusestaffdetail |
| muldimendetail | 多维控编明细 | BasedataField | fentryid |  | haos_muldimendetail |
| stafftype | 占编类型 | ComboField | ftype |  |  |
| status | 状态 | ComboField | fstatus |  |  |
| effdt | 生效日期 | DateField | feffdt |  |  |
| leffdt | 失效日期 | DateField | fleffdt |  |  |
| bo | 业务id | BigIntField | fboid |  |  |

