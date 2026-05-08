# hrcs_permapplybill — 权限申请单

**表单编码**: `hrcs_permapplybill`  
**表单ID**: `3TP3H2ITC7SJ`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_permapplybill（权限申请单） [BillEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_permapplybill` | BillEntity | 主表 |
| `t_hrcs_permapplyassign` | EntryEntity | 分配记录分录 |
| `t_hrcs_permapplycancel` | EntryEntity | 取消记录分录 |

### 字段列表 — t_hrcs_permapplybill（主表·BillEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 申请单号 | BillNoField | t_hrcs_permapplybill.fbillno |  |  |
| billstatus | 单据状态 | BillStatusField | t_hrcs_permapplybill.fbillstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_permapplybill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_permapplybill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hrcs_permapplybill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_hrcs_permapplybill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_permapplybill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_permapplybill.fcreatetime |  |  |
| barcode | 条形码 | TextField | — |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| isexistsworkflow | 是否有工作流 | CheckBoxField | — |  |  |
| inputdevicetype | 输入设备 | TextField | — |  |  |
| auditstatus | 审批状态 | BillStatusField | — |  |  |
| user | 申请人 | UserField | t_hrcs_permapplybill.fuserid |  | bos_user |
| subscriber | 订阅事件 | BasedataField | t_hrcs_permapplybill.fsubscriberid |  | evt_subscription |
| authaction | 授权动作 | ComboField | t_hrcs_permapplybill.fauthaction |  |  |
| message | 业务事件消息 | BasedataField | t_hrcs_permapplybill.fmessageid |  | evt_event |
| chgaction | 变动操作 | BasedataField | t_hrcs_permapplybill.fchgactionid |  | hpfs_chgaction |
| chgevent | 变动大类 | BasedataField | t_hrcs_permapplybill.fchgeventid |  | hpfs_chgevent |
| chgrecord | 变动记录 | BasedataField | t_hrcs_permapplybill.fchgrecordid |  | hpfs_chgrecord |
| chgcategory | 变动类型 | BasedataField | t_hrcs_permapplybill.fchgcategoryid |  | hpfs_chgcategory |
| assignentry | 分配记录分录 | EntryEntity | → t_hrcs_permapplyassign |  |  |
| cancelentry | 取消记录分录 | EntryEntity | → t_hrcs_permapplycancel |  |  |

### 字段列表 — t_hrcs_permapplyassign（分配记录分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| assign_role | 角色 | BasedataField | t_hrcs_permapplyassign.froleid |  | perm_role |
| assign_scheme | 方案 | BasedataField | t_hrcs_permapplyassign.fschemeid |  | hrcs_dynascheme |
| assign_validstart | 有效期开始 | DateTimeField | t_hrcs_permapplyassign.fvalidstart |  |  |
| assign_validend | 有效期结束 | DateTimeField | t_hrcs_permapplyassign.fvalidend |  |  |
| assign_dealway | 处理方式 | ComboField | t_hrcs_permapplyassign.fdealway |  |  |
| assign_filechangetype | 权限档案变动 | ComboField | t_hrcs_permapplyassign.ffilechangetype |  |  |
| assign_permfile | 权限档案 | BasedataField | t_hrcs_permapplyassign.fpermfileid |  | hrcs_userpermfile |
| assign_desc | 分配说明 | MuliLangTextField | t_hrcs_permapplyassign.fassigndesc |  |  |
| roleentryid | 方案角色分录ID | BigIntField | t_hrcs_permapplyassign.froleentryid |  |  |

### 字段列表 — t_hrcs_permapplycancel（取消记录分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| cancel_permfile | 权限档案 | BasedataField | t_hrcs_permapplycancel.fpermfileid |  | hrcs_userpermfile |
| cancel_role | 角色 | BasedataField | t_hrcs_permapplycancel.froleid |  | perm_role |
| cancel_scheme | 方案 | BasedataField | t_hrcs_permapplycancel.fschemeid |  | hrcs_dynascheme |
| cancel_validstart | 有效期开始 | DateTimeField | t_hrcs_permapplycancel.fvalidstart |  |  |
| cancel_validend | 有效期结束 | DateTimeField | t_hrcs_permapplycancel.fvalidend |  |  |
| cancel_desc | 取消说明 | MuliLangTextField | t_hrcs_permapplycancel.fcanceldesc |  |  |
| cancel_reason | 取消原因 | MuliLangTextField | t_hrcs_permapplycancel.fcancelreason |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_permapplybill（主表） | 21 |
| t_hrcs_permapplyassign（分配记录分录） | 9 |
| t_hrcs_permapplycancel（取消记录分录） | 7 |
| 无数据库列 | 5 |

