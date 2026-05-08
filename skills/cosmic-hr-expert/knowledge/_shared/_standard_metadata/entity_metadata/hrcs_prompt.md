# hrcs_prompt — 提示语配置

**表单编码**: `hrcs_prompt`  
**表单ID**: `1GZPOTSAHIEK`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_prompt（提示语配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_prompt` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_prompt（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_prompt.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_prompt.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_prompt.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_prompt.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_prompt.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_prompt.fenable |  |  |
| createtime | 创建日期 | CreateDateField | t_hrcs_prompt.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_prompt.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_prompt.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_prompt.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_prompt.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_prompt.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_prompt.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_prompt.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_prompt.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_prompt.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_prompt.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_prompt.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_prompt.foriname |  |  |
| categorize | 分类 | TextField | t_hrcs_prompt.fcategorize |  |  |
| categorizeid | 分类ID | TextField | t_hrcs_prompt.fcategorizeid |  |  |
| pageid | 页面id | TextField | — |  |  |
| cloud | 云 | BasedataField | t_hrcs_prompt.fcloud |  | bos_devportal_bizcloud |
| app | 应用 | BasedataField | t_hrcs_prompt.fapp |  | bos_devportal_bizapp |
| businessobject | 业务对象 | BasedataField | t_hrcs_prompt.fbusinessobject |  | hbp_entityobject |
| syscontent | 系统文案 | RadioField | — |  |  |
| selectcontent | 启用文案 | RadioGroupField | t_hrcs_prompt.fselectcontent |  |  |
| diycontent | 自定义文案 | RadioField | — |  |  |
| syslangcontent | 多语言文本 | MuliLangTextField | t_hrcs_prompt.fsyslangcontent |  |  |
| promptlangcontent | 多语言文本 | MuliLangTextField | t_hrcs_prompt.fpromptlangcontent |  |  |
| langtype | 文本类型 | ComboField | t_hrcs_prompt.flangtype | ✓ |  |
| entryentity | 单据体 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — （单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_prompt（主表） | 31 |
| （单据体） | 7 |
| 无数据库列 | 3 |

