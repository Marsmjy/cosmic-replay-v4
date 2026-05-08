# hbss_position_dict · 案例

> **生成时间**：2026-04-29
> **方法**：从 `_cross_cloud_reports/hr_hrmp_consumed_by.json` 提取真实跨云消费场景

## 真实跨云消费案例

### CASE-1: `hbss_industrytype` 被消费 (12 处 · 12 跨云)

**org_dev（8 处）：**

- `haos_adminorg` → `industrytype`
- `haos_adminorgcompany` → `industrytype`
- `haos_adminorgdetail` → `industrytype`
- `haos_adminorghis` → `industrytype`
- `homs_orgbatchchgbill` → `add_industrytype`, `parent_industrytype`, `info_industrytype`
- `homs_orgdifftemp` → `industrytype`

**payroll（2 处）：**

- `hcf_canprework` → `trade`
- `hcf_canprojectexp` → `industry`

**core_hr（2 处）：**

- `hrpi_empproexp` → `industry`
- `hrpi_preworkexp` → `trade`

### CASE-2: `hbss_hrbusinessfield` 被消费 (5 处 · 2 跨云)

**other（1 处）：**

- `hbp_datagrade_unittest` → `businessfield`

**payroll（1 处）：**

- `hrss_searchobject` → `cloud`

### CASE-3: `hbss_bussinessfield` 被消费 (7 处 · 2 跨云)

**core_hr（2 处）：**

- `hrpi_assignment` → `businesstype`
- `hrpi_assignmentmag` → `businesstype`

### CASE-4: `hbss_companyscale` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_canprework` → `companyscale`

**core_hr（1 处）：**

- `hrpi_preworkexp` → `companyscale`

### CASE-5: `hbss_projecttype` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_canprojectexp` → `projecttype`

**core_hr（1 处）：**

- `hrpi_empproexp` → `projecttype`

### CASE-6: `hbss_cadrecategory` 被消费 (2 处 · 2 跨云)

**core_hr（2 处）：**

- `hrpi_appointremoverel` → `cadrecat`
- `hrpi_empcadre` → `cadretype`

### CASE-7: `hbss_postype` 被消费 (2 处 · 2 跨云)

**core_hr（2 处）：**

- `hrpi_appointremoverel` → `postype`
- `hrpi_empposorgrel` → `postype`

### CASE-8: `hbss_poststate` 被消费 (2 处 · 2 跨云)

**core_hr（2 处）：**

- `hrpi_appointremoverel` → `posstatus`
- `hrpi_empposorgrel` → `posstatus`

### CASE-9: `hbss_depcytype` 被消费 (1 处 · 1 跨云)

**org_dev（1 处）：**

- `hbjm_jobhr` → `depcytype`

### CASE-10: `hbss_disptype` 被消费 (1 处 · 1 跨云)

**core_hr（1 处）：**

- `hrpi_dispatchinfo` → `disptype`

## ISV 改本场景实体的影响推演

假如 ISV 试图修改 `number` / `name` / `enable` 等标品公共字段：

| 改动 | 影响下游 | 后果 |
|---|---|---|
| 改 `number` 类型从 TextField 改 LongField | ❌ 全部下游 BasedataField 引用 | 下游 form 加载失败 |
| 删除标品字段 | ❌ 全部下游引用 | 下游 form 元数据加载错误 |
| 加 ISV 字段（前缀 _ext） | ✅ 不影响下游 | 下游不感知 |
| 改 `enable` 默认值 | ⚠️ 部分下游 | 数据导入时可能默认值不一致 |
