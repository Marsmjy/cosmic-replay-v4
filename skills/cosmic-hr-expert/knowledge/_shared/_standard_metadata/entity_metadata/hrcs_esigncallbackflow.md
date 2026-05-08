# hrcs_esigncallbackflow — 电子签回调流水信息

**表单编码**: `hrcs_esigncallbackflow`  
**表单ID**: `3HNA5+R1Z/O6`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_esigncallbackflow（电子签回调流水信息） [BillEntity]

- **数据库表**: `t_hrcs_esigncallbackflow`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | fbillno |  |  |
| billstatus | 单据状态 | BillStatusField | fbillstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| barcode | 条形码 | TextField | — |  |  |
| eventeffectdate | 事务生效时间(废弃) | DateTimeField | — |  |  |
| isexistsworkflow | 是否有工作流 | CheckBoxField | — |  |  |
| inputdevicetype | 输入设备 | TextField | — |  |  |
| auditstatus | 审批状态 | BillStatusField | — |  |  |
| callbackkey | 回调Key | TextField | fcallbackkey |  |  |
| callbackmsg | 回调信息 | TextField | fcallbackmsg |  |  |
| callbackstatus | 回调状态 | ComboField | fcallbackstatus |  |  |
| callbackresult | 回调结果 | ComboField | fcallbackresult |  |  |
| failmsg | 回调失败原因 | TextField | ffailmsg |  |  |
| reldataid | 关联数据ID | BigIntField | freldataid |  |  |
| callbackkeydetail | 回调Key明细信息 | TextField | fcallbackkeydetail |  |  |

