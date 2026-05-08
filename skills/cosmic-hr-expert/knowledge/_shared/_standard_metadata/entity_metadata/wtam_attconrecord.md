---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15=TGRTUNG1B
app_number: wtam
app_name: 日常考勤
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtam_attconrecord — 考勤确认记录

**表单编码**: `wtam_attconrecord`  
**表单ID**: `4/5FRW5PWYN+`  
**归属**: 工时假勤云 / 日常考勤  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtam_attconrecord（考勤确认记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtam_attconrecord` | 主表 · 24 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtam_attconrecord.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtam_attconrecord.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtam_attconrecord.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtam_attconrecord.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| attfile | 考勤档案 | BasedataField | t_wtam_attconrecord.fattfileid | ✓ | wtp_attfilebase |
| attfileversion | 考勤档案版本 | BasedataField | — | ✓ | wtp_attfilebase |
| perattperiod | 人员考勤期间 | BasedataField | t_wtam_attconrecord.fperattperiodid | ✓ | wtp_perattperiod |
| status | 确认状态 | ComboField | t_wtam_attconrecord.fstatus | ✓ |  |
| enddate | 结束日期 | DateField | t_wtam_attconrecord.fenddate | ✓ |  |
| startdate | 开始日期 | DateField | t_wtam_attconrecord.fstartdate | ✓ |  |
| revoker | 撤销人 | UserField | t_wtam_attconrecord.frevokerid | ✓ | bos_user |
| receiver | 接收人 | UserField | t_wtam_attconrecord.freceiverid | ✓ | bos_user |
| confirmer | 最终确认人 | UserField | t_wtam_attconrecord.fconfirmerid | ✓ | bos_user |
| confirmmode | 确认方式 | ComboField | t_wtam_attconrecord.fconfirmmode | ✓ |  |
| confirmendtime | 考勤确认截止日期 | DateField | t_wtam_attconrecord.fconfirmendtime | ✓ |  |
| overhandletype | 逾期处理方式 | ComboField | t_wtam_attconrecord.foverhandletype | ✓ |  |
| confirmlock | 确认后自动锁定考勤记录 | CheckBoxField | t_wtam_attconrecord.fconfirmlock |  |  |
| type | 确认维度 | ComboField | t_wtam_attconrecord.ftype | ✓ |  |
| showlast | 允许查看上次确认的内容 | CheckBoxField | t_wtam_attconrecord.fshowlast |  |  |
| revoketime | 撤销时间 | DateTimeField | t_wtam_attconrecord.frevoketime | ✓ |  |
| confirmtime | 确认时间 | DateTimeField | t_wtam_attconrecord.fconfirmtime | ✓ |  |
| source | 数据来源 | ComboField | t_wtam_attconrecord.fsource | ✓ |  |
| genrule | 考勤确认生成规则 | BasedataField | t_wtam_attconrecord.fgenrule |  | wtp_attconfirmgenrule |
| datastatus | 数据状态 | ComboField | t_wtam_attconrecord.fdatastatus | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtam_attconrecord（主表） | 23 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
