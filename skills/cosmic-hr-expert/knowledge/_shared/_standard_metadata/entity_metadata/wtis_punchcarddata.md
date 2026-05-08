---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2MKKI+46MWNZ
app_number: wtis
app_name: 工时假勤集成服务
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtis_punchcarddata — 打卡数据

**表单编码**: `wtis_punchcarddata`  
**表单ID**: `2NCSH0V4IRCJ`  
**归属**: 工时假勤云 / 工时假勤集成服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtis_punchcarddata（打卡数据） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtis_punchcarddata` | 主表 · 23 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtis_punchcarddata.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtis_punchcarddata.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtis_punchcarddata.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtis_punchcarddata.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| number | 工号 | TextField | t_wtis_punchcarddata.fnumber |  |  |
| card | 考勤卡号 | TextField | t_wtis_punchcarddata.fcard |  |  |
| punchcardtime | 打卡时间 | TextField | t_wtis_punchcarddata.fpunchcardtime |  |  |
| accesstag | 进出卡 | ComboField | t_wtis_punchcarddata.faccesstag |  |  |
| signsourcename | 打卡来源 | TextField | t_wtis_punchcarddata.fsignsourcename |  |  |
| place | 设备位置 | TextField | t_wtis_punchcarddata.fplace |  |  |
| equipment | 打卡设备 | TextField | t_wtis_punchcarddata.fequipment |  |  |
| equipnumber | 识别码 | TextField | t_wtis_punchcarddata.fequipnumber |  |  |
| reason | 异常原因 | TextField | t_wtis_punchcarddata.freason |  |  |
| batchnumber | 批次号 | TextField | t_wtis_punchcarddata.fbatchnumber |  |  |
| status | 同步状态 | ComboField | t_wtis_punchcarddata.fstatus |  |  |
| times | 同步次数 | IntegerField | t_wtis_punchcarddata.ftimes |  |  |
| dataid | 数据来源方ID | TextField | t_wtis_punchcarddata.fdataid |  |  |
| timezone | 时区 | BasedataField | t_wtis_punchcarddata.ftimezoneid |  | inte_timezone |
| punchcarddate | 打卡日期 | DateField | t_wtis_punchcarddata.fpunchcarddate |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtis_punchcarddata.fmasterid |  |  |
| presetbiz1 | 预留业务字段1 | TextField | t_wtis_punchcarddata.fpresetbiz1 |  |  |
| presetbiz2 | 预留业务字段2 | TextField | t_wtis_punchcarddata.fpresetbiz2 |  |  |
| signsource | 数据来源方 | ComboField | t_wtis_punchcarddata.fsignsource |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtis_punchcarddata（主表） | 23 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
