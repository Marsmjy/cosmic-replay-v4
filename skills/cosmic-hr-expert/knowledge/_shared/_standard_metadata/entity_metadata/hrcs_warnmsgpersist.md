# hrcs_warnmsgpersist — 消息详情

**表单编码**: `hrcs_warnmsgpersist`  
**表单ID**: `476625PZOZ7B`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnmsgpersist（消息详情） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_warnmsgpersist` | BaseEntity | 主表 |
| `t_hrcs_warnmsgpsreciver` | EntryEntity | 接收人分录 |
| `（虚拟分录）` | EntryEntity | 行数据展示动态分录 |
| `（虚拟分录）` | EntryEntity | 行数据展示动态分录 |

### 字段列表 — t_hrcs_warnmsgpersist（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_warnmsgpersist.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_warnmsgpersist.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_warnmsgpersist.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_warnmsgpersist.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_warnmsgpersist.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_warnmsgpersist.finitdatasource |  |  |
| warnscheme | 预警方案 | BasedataField | t_hrcs_warnmsgpersist.fwarnschemeid |  | hrcs_warnscheme |
| datahead | 表头数据 | TextField | t_hrcs_warnmsgpersist.fdatahead |  |  |
| logid | 日志id | TextField | t_hrcs_warnmsgpersist.flogid |  |  |
| msgtitle | 消息标题 | TextField | t_hrcs_warnmsgpersist.fmsgtitle |  |  |
| msgmain | 消息正文 | TextField | t_hrcs_warnmsgpersist.fmsgmain |  |  |
| msgend | 消息结束语 | TextField | t_hrcs_warnmsgpersist.fmsgend |  |  |
| channel | 渠道 | TextField | t_hrcs_warnmsgpersist.fchannel |  |  |
| bilingual | 双语 | CheckBoxField | t_hrcs_warnmsgpersist.fbilingual |  |  |
| msgmainen | 消息正文英文 | TextField | t_hrcs_warnmsgpersist.fmsgmainen |  |  |
| msgenden | 消息结束语英文 | TextField | t_hrcs_warnmsgpersist.fmsgenden |  |  |
| receiverentry | 接收人分录 | EntryEntity | → t_hrcs_warnmsgpsreciver |  |  |
| showentity | 行数据展示动态分录 | EntryEntity | → （虚拟分录） |  |  |
| showenentity | 行数据展示动态分录 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — t_hrcs_warnmsgpsreciver（接收人分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| receiverid | 接收人id | TextField | t_hrcs_warnmsgpsreciver.freceiverid |  |  |
| receivername | 接收人名称 | TextField | t_hrcs_warnmsgpsreciver.freceivername |  |  |

### 字段列表 — （行数据展示动态分录·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （行数据展示动态分录·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_warnmsgpersist（主表） | 16 |
| t_hrcs_warnmsgpsreciver（接收人分录） | 2 |

