# brm_rulelist — 规则列表

**表单编码**: `brm_rulelist`  
**表单ID**: `1SIH10AN8KEY`  
**归属**: HR基础服务云 / 业务规则管理  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: brm_rulelist（规则列表） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_brm_policy` | BaseEntity | 主表 |
| `t_brm_drlfilter` | EntryEntity | 规则信息分录 |
| `t_brm_ruleadminorg` | MulEmployeeField子表 | 行政组织 |

### 字段列表 — t_brm_policy（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| policy | 策略 | BasedataField | — |  | brm_policy_edit |
| entryrulelist | 规则信息分录 | EntryEntity | → t_brm_drlfilter |  |  |

### 字段列表 — t_brm_drlfilter（规则信息分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rulenumber | 规则编码 | TextField | t_brm_drlfilter.fnumber |  |  |
| ruleenable | 使用状态 | CheckBoxField | t_brm_drlfilter.fenable |  |  |
| templatenumber | 模板编码 | TextField | t_brm_drlfilter.ftemplatenumber |  |  |
| rulebizapp | 所属应用 | BasedataField | t_brm_drlfilter.fbizappid |  | bos_devportal_bizapp |
| rulescene | 所属场景 | BasedataField | t_brm_drlfilter.fsceneid |  | brm_scene |
| ruleorder | 优先级 | IntegerField | t_brm_drlfilter.fruleorder |  |  |
| filtercondition | 条件 | TextField | t_brm_drlfilter.fconditions |  |  |
| filterresult | 结果 | TextField | t_brm_drlfilter.fresults |  |  |
| rulename | 规则名称 | MuliLangTextField | t_brm_drlfilter.fname |  |  |
| ruledescription | 规则描述 | MuliLangTextField | t_brm_drlfilter.fdescription |  |  |
| conditionpreview | 条件 | TextField | — |  |  |
| resultpreview | 结果 | TextField | — |  |  |
| adminorg | 行政组织 | HRMulAdminOrgField | t_brm_ruleadminorg（子表） |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_brm_policy（主表） | 1 |
| t_brm_drlfilter（规则信息分录） | 13 |
| t_brm_ruleadminorg（MulEmployeeField子表） | 1 |
| 无数据库列 | 3 |

