# hrcs_signflow — 电子签署流程

**表单编码**: `hrcs_signflow`  
**表单ID**: `3E3C1BR7NM38`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_signflow（电子签署流程） [BillEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_signflow` | BillEntity | 主表 |
| `t_hrcs_signflowext` | EntryEntity | 扩展信息单据体 |
| `t_hrcs_signfloworg` | EntryEntity | 合同签署主体单据体 |
| `t_hrcs_signflowpsn` | EntryEntity | 签署人单据体 |

### 字段列表 — t_hrcs_signflow（主表·BillEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_hrcs_signflow.fbillno |  |  |
| billstatus | 单据状态 | BillStatusField | t_hrcs_signflow.fbillstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_signflow.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_signflow.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hrcs_signflow.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_hrcs_signflow.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_signflow.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_signflow.fcreatetime |  |  |
| barcode | 条形码 | TextField | — |  |  |
| eventeffectdate | 事务生效时间(废弃) | DateTimeField | — |  |  |
| isexistsworkflow | 是否有工作流 | CheckBoxField | — |  |  |
| inputdevicetype | 输入设备 | TextField | — |  |  |
| auditstatus | 审批状态 | BillStatusField | — |  |  |
| signconfig | 签署配置信息 | BasedataField | t_hrcs_signflow.fsignconfigid |  | hrcs_esignappcfg |
| templatetypeid | 电子签署类型 | BasedataField | t_hrcs_signflow.ftemplatetypeid |  | hrcs_econtemplatetype |
| signstatus | 签署状态 | ComboField | t_hrcs_signflow.fsignstatus |  |  |
| signfile | 电子签署文件 | BigIntField | t_hrcs_signflow.fsignfileid |  |  |
| econttmp | 电子合同模板 | BasedataField | t_hrcs_signflow.feconttmpid |  | hrcs_econtemplate |
| entryentity | 扩展信息单据体 | EntryEntity | → t_hrcs_signflowext |  |  |
| entryentityorg | 合同签署主体单据体 | EntryEntity | → t_hrcs_signfloworg |  |  |
| entryentityperson | 签署人单据体 | EntryEntity | → t_hrcs_signflowpsn |  |  |

### 字段列表 — t_hrcs_signflowext（扩展信息单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| key | 属性名称 | TextField | t_hrcs_signflowext.fkey |  |  |
| value | 属性值 | LargeTextField | t_hrcs_signflowext.fvalue |  |  |

### 字段列表 — t_hrcs_signfloworg（合同签署主体单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| signorg | 合同签署主体 | BasedataField | t_hrcs_signfloworg.forgid |  | hbss_lawentity |
| orgsignstatus | 签署状态 | ComboField | t_hrcs_signfloworg.forgsignstatus |  |  |
| orgmsg | 组织签署信息 | TextField | t_hrcs_signfloworg.forgmsg |  |  |

### 字段列表 — t_hrcs_signflowpsn（签署人单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| candidateid | 候选人 | BasedataField | t_hrcs_signflowpsn.fcandidateid |  | hcf_candidate |
| personsignstatus | 签署状态 | ComboField | t_hrcs_signflowpsn.fpersonsignstatus |  |  |
| personmsg | 人员签署信息 | TextField | t_hrcs_signflowpsn.fpersonmsg |  |  |
| naturalid | 自然人 | EmployeeField | t_hrcs_signflowpsn.fnaturalid |  | hrpi_employeenewf7query |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_signflow（主表） | 18 |
| t_hrcs_signflowext（扩展信息单据体） | 2 |
| t_hrcs_signfloworg（合同签署主体单据体） | 3 |
| t_hrcs_signflowpsn（签署人单据体） | 4 |
| 无数据库列 | 5 |

