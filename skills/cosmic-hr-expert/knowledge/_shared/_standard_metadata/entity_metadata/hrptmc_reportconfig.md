# hrptmc_reportconfig — 报表配置

**表单编码**: `hrptmc_reportconfig`  
**表单ID**: `2VY3N=YDWUTC`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_reportconfig（报表配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_reportconfig` | BaseEntity | 主表 |
| `t_hrptmc_rptlaststyle` | EntryEntity | 最近使用样式 |
| `t_hrptmc_rowalgorithm` | EntryEntity | 小计行汇总 |
| `t_hrptmc_subtotrfieldref` | MulEmployeeField子表 | 小计字段 |

### 字段列表 — t_hrptmc_reportconfig（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_reportconfig.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_reportconfig.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_reportconfig.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_reportconfig.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_reportconfig.finitdatasource |  |  |
| total | 总计 | CheckBoxField | t_hrptmc_reportconfig.ftotal |  |  |
| page | 分页 | CheckBoxField | t_hrptmc_reportconfig.fpage |  |  |
| showseq | 显示序号 | CheckBoxField | t_hrptmc_reportconfig.fshowseq |  |  |
| mergecell | 合并单元格 | CheckBoxField | t_hrptmc_reportconfig.fmergecell |  |  |
| headermerge | 表头归类 | TextField | t_hrptmc_reportconfig.fheadermerge |  |  |
| type | 报表类型 | ComboField | t_hrptmc_reportconfig.ftype |  |  |
| rptmanage | 报表管理 | BasedataField | t_hrptmc_reportconfig.frptmanageid |  | hrptmc_reportmanage |
| subtotalfield | 小计字段 | MulBasedataField | t_hrptmc_subtotrfieldref（子表） |  |  |
| subtotal | 开启小计 | CheckBoxField | t_hrptmc_reportconfig.fsubtotal |  |  |
| rowcoltransposition | 行列转置 | TextField | t_hrptmc_reportconfig.frowcoltransposition |  |  |
| freeze | 冻结列 | IntegerField | t_hrptmc_reportconfig.ffree |  |  |
| headstyle | 表头样式 | TextField | t_hrptmc_reportconfig.fheadstyle |  |  |
| subtotalname | 小计名 | MuliLangTextField | t_hrptmc_reportconfig.fsubtotalname |  |  |
| totalname | 总计名 | MuliLangTextField | t_hrptmc_reportconfig.ftotalname |  |  |
| drillingdrl | 下钻目录（废弃） | TextField | t_hrptmc_reportconfig.fdrillingdrl |  |  |
| rowadvancesort | 行高级排序 | TextField | t_hrptmc_reportconfig.frowadvancesort |  |  |
| coladvancesort | 列高级排序 | TextField | t_hrptmc_reportconfig.fcoladvancesort |  |  |
| workrpt | 工作表 | BasedataField | t_hrptmc_reportconfig.fworkrptid |  | hrptmc_workreport |
| showdatalabel | 显示数据标签 | CheckBoxField | t_hrptmc_reportconfig.fshowdatalabel |  |  |
| categoryname | 类别轴名称 | MuliLangTextField | t_hrptmc_reportconfig.fcategoryname |  |  |
| categoryunit | 类别轴单位 | MuliLangTextField | t_hrptmc_reportconfig.fcategoryunit |  |  |
| valuename | 值轴名称 | MuliLangTextField | t_hrptmc_reportconfig.fvaluename |  |  |
| valueunit | 值轴单位 | MuliLangTextField | t_hrptmc_reportconfig.fvalueunit |  |  |
| legend | 图例 | TextField | t_hrptmc_reportconfig.flegend |  |  |
| tableconfig | 列宽及行高 | TextField | t_hrptmc_reportconfig.ftableconfig |  |  |
| sectors | 合并后扇形数量 | IntegerField | t_hrptmc_reportconfig.fsectors |  |  |
| othername | 区块名称 | MuliLangTextField | t_hrptmc_reportconfig.fothername |  |  |
| annularchart | 环形图 | CheckBoxField | t_hrptmc_reportconfig.fannularchart |  |  |
| shownote | 显示备注 | CheckBoxField | t_hrptmc_reportconfig.fshownote |  |  |
| chartname | 图表名称 | MuliLangTextField | t_hrptmc_reportconfig.fchartname |  |  |
| note | 备注 | MuliLangTextField | t_hrptmc_reportconfig.fnote |  |  |
| tagoverlap | 标签重叠 | CheckBoxField | t_hrptmc_reportconfig.ftagoverlap |  |  |
| entryentity | 最近使用样式 | EntryEntity | → t_hrptmc_rptlaststyle |  |  |
| entryentity1 | 小计行汇总 | EntryEntity | → t_hrptmc_rowalgorithm |  |  |

### 字段列表 — t_hrptmc_rptlaststyle（最近使用样式·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| styletype | 样式分类 | ComboField | t_hrptmc_rptlaststyle.ftype |  |  |
| laststyle | 最近使用样式 | TextField | t_hrptmc_rptlaststyle.flaststyle |  |  |
| time | 使用时间 | DateField | t_hrptmc_rptlaststyle.ftime |  |  |

### 字段列表 — t_hrptmc_rowalgorithm（小计行汇总·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| numberalias | 行字段别名 | TextField | t_hrptmc_rowalgorithm.fnumberalias |  |  |
| algorithm | 汇总方式 | ComboField | t_hrptmc_rowalgorithm.falgorithm |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_reportconfig（主表） | 37 |
| t_hrptmc_rptlaststyle（最近使用样式） | 3 |
| t_hrptmc_rowalgorithm（小计行汇总） | 2 |
| t_hrptmc_subtotrfieldref（MulEmployeeField子表） | 1 |

