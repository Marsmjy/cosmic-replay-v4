---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2WQD=V5DU=7Q
app_number: hies
app_name: HR导入导出管理
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hies_taskinfo — 导入导出记录

**表单编码**: `hies_taskinfo`  
**表单ID**: `2XBQMQE=8S16`  
**归属**: HR基础服务云 / HR导入导出管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hies_taskinfo（导入导出记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hies_taskinfo` | 主表 · 43 列 |
| `t_hies_taskinfo_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hies_taskinfo.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hies_taskinfo.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hies_taskinfo.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hies_taskinfo.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| taskid | 平台生成的导入导出ID | TextField | t_hies_taskinfo.ftaskid |  |  |
| serialno | 任务流水号 | TextField | t_hies_taskinfo.fserialno |  |  |
| oprpage | 操作页面参数 | TextField | t_hies_taskinfo.foprpage | ✓ |  |
| oprtype | 操作类型 | ComboField | t_hies_taskinfo.foprtype | ✓ |  |
| oprcategory | 操作分类 | ComboField | t_hies_taskinfo.foprcategory | ✓ |  |
| entitytype | 实体类型 | ComboField | t_hies_taskinfo.fentitytype | ✓ |  |
| filesize | 文件大小 | BigIntField | t_hies_taskinfo.ffilesize |  |  |
| progress | 处理进度 | IntegerField | t_hies_taskinfo.fprogress |  |  |
| manualintflag | 是否手动中断 | ComboField | t_hies_taskinfo.fmanualintflag |  |  |
| inttor | 中断人 | CreaterField | t_hies_taskinfo.finttor |  | bos_user |
| sucamount | 成功数量 | IntegerField | t_hies_taskinfo.fsucamount |  |  |
| failamount | 失败数量 | IntegerField | t_hies_taskinfo.ffailamount |  |  |
| status | 任务状态 | ComboField | t_hies_taskinfo.fstatus |  |  |
| totalamount | 数据总量 | IntegerField | t_hies_taskinfo.ftotalamount |  |  |
| totalcost | 耗时 | IntegerField | t_hies_taskinfo.ftotalcost |  |  |
| handlemachine | 处理机器信息 | TextField | t_hies_taskinfo.fhandlemachine |  |  |
| uploadfileurl | 上传文件路径 | TextField | t_hies_taskinfo.fuploadfileurl |  |  |
| alldatafileurl | 全量报告数据文件路径 | TextField | t_hies_taskinfo.falldatafileurl |  |  |
| errdatafileurl | 错误报告数据文件路径 | TextField | t_hies_taskinfo.ferrdatafileurl |  |  |
| downloadfileurl | 下载文件路径 | TextField | t_hies_taskinfo.fdownloadfileurl |  |  |
| result | 结果 | ComboField | t_hies_taskinfo.fresult |  |  |
| stime | 开始时间 | DateTimeField | t_hies_taskinfo.fstime |  |  |
| etime | 结束时间 | DateTimeField | t_hies_taskinfo.fetime |  |  |
| inttime | 中断时间 | DateTimeField | t_hies_taskinfo.finttime |  |  |
| performancelog | 性能日志 | TextField | t_hies_taskinfo.fperformancelog |  |  |
| systemerrlog | 系统错误日志 | TextField | t_hies_taskinfo.fsystemerrlog |  |  |
| datavalidlog | 数据校验日志 | TextField | t_hies_taskinfo.fdatavalidlog |  |  |
| pluginrunlog | 插件运行日志 | TextField | t_hies_taskinfo.fpluginrunlog |  |  |
| intreason | 中断原因 | MuliLangTextField | t_hies_taskinfo_l.fintreason |  |  |
| businessfield | 业务领域 | BasedataField | t_hies_taskinfo.fbusinessfield |  | hbss_cloud |
| extparam | 扩展请求参数 | TextField | t_hies_taskinfo.fextparam |  |  |
| reqparam | 请求参数 | TextField | t_hies_taskinfo.freqparam |  |  |
| oprtypename | 操作名称 | TextField | — |  |  |
| oprcategoryname | 操作分类名称 | TextField | — |  |  |
| progressdesc | 处理进度描述 | TextField | — |  |  |
| schedulestatus | 调度状态 | ComboField | t_hies_taskinfo.fschedulestatus |  |  |
| retrynum | 重试次数 | IntegerField | t_hies_taskinfo.fretrynum |  |  |
| lastretrytime | 最新重试时间 | DateTimeField | t_hies_taskinfo.flastretrytime |  |  |
| app | 所属应用 | BasedataField | t_hies_taskinfo.fappid |  | hbp_devportal_bizapp |
| usetpl | 使用模板 | BasedataField | t_hies_taskinfo.fusetpl |  | hies_diaetplconf |
| issysexpt | 系统异常 | CheckBoxField | t_hies_taskinfo.fissysexpt |  |  |
| entitynum | 操作页面标识 | TextField | t_hies_taskinfo.fentitynum | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hies_taskinfo（主表） | 42 |
| t_hies_taskinfo_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
