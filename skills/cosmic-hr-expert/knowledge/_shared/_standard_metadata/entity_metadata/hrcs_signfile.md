# hrcs_signfile — 电子签署文件

**表单编码**: `hrcs_signfile`  
**表单ID**: `3E34T3Q71VFI`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_signfile（电子签署文件） [BillEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_signfile` | BillEntity | 主表 |
| `t_hrcs_signfileext` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_signfile（主表·BillEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_hrcs_signfile.fbillno |  |  |
| billstatus | 单据状态 | BillStatusField | t_hrcs_signfile.fbillstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_signfile.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_signfile.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hrcs_signfile.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_hrcs_signfile.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_signfile.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_signfile.fcreatetime |  |  |
| barcode | 条形码 | TextField | — |  |  |
| eventeffectdate | 事务生效时间(废弃) | DateTimeField | — |  |  |
| isexistsworkflow | 是否有工作流 | CheckBoxField | — |  |  |
| inputdevicetype | 输入设备 | TextField | — |  |  |
| auditstatus | 审批状态 | BillStatusField | — |  |  |
| cloud | 业务云编码 | TextField | t_hrcs_signfile.fcloud |  |  |
| businesstype | 业务类型 | TextField | t_hrcs_signfile.fbusinesstype |  |  |
| businessid | 业务对象ID | TextField | t_hrcs_signfile.fbusinessid |  |  |
| signconfig | 签署配置信息 | BasedataField | t_hrcs_signfile.fsignconfigid |  | hrcs_esignappcfg |
| appnum | 应用编码 | TextField | t_hrcs_signfile.fappnum |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_signfileext |  |  |

### 字段列表 — t_hrcs_signfileext（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| key | 属性名称 | TextField | t_hrcs_signfileext.fkey |  |  |
| value | 属性值 | LargeTextField | t_hrcs_signfileext.fvalue |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_signfile（主表） | 18 |
| t_hrcs_signfileext（单据体） | 2 |
| 无数据库列 | 5 |

