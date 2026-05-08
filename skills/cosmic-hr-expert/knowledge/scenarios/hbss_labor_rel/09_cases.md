# hbss_labor_rel · 案例

> **生成时间**：2026-04-29
> **方法**：从 `_cross_cloud_reports/hr_hrmp_consumed_by.json` 提取真实跨云消费场景

## 真实跨云消费案例

### CASE-1: `hbss_laborreltype` 被消费 (6 处 · 6 跨云)

**org_dev（4 处）：**

- `haos_dimstaffreport` → `laborreltype`
- `haos_muldimendetail` → `laborreltype`
- `haos_muldimendetailhis` → `laborreltype`
- `haos_orgpersonstaffinfo` → `laborreltype`

**core_hr（2 处）：**

- `hrpi_blacklist` → `emptype`
- `hrpi_empentrel` → `laborreltype`

### CASE-2: `hbss_empnature` 被消费 (4 处 · 4 跨云)

**payroll（2 处）：**

- `hcf_canprework` → `businesstypeid`
- `hcf_canprojectexp` → `companynature`

**core_hr（2 处）：**

- `hrpi_empproexp` → `companynature`
- `hrpi_preworkexp` → `businesstypeid`

### CASE-3: `hbss_appointtype` 被消费 (2 处 · 2 跨云)

**core_hr（2 处）：**

- `hrpi_appointremoverel` → `appointtype`, `dismisstype`

### CASE-4: `hbss_apptreasongroup` 被消费 (2 处 · 2 跨云)

**core_hr（2 处）：**

- `hrpi_appointremoverel` → `apptreasonggroup`, `dismissreason`

### CASE-5: `hbss_employeegroup` 被消费 (2 处 · 2 跨云)

**core_hr（2 处）：**

- `hrpi_assignment` → `persongroup`
- `hrpi_assignmentmag` → `persongroup`

### CASE-6: `hbss_contracttypes` 被消费 (1 处 · 1 跨云)

**core_hr（1 处）：**

- `hrpi_contractinfo` → `contracttype`

### CASE-7: `hbss_timelimittype` 被消费 (1 处 · 1 跨云)

**core_hr（1 处）：**

- `hrpi_contractinfo` → `periodtype`

### CASE-8: `hbss_laborrelstatus` 被消费 (1 处 · 1 跨云)

**core_hr（1 处）：**

- `hrpi_empentrel` → `laborrelstatus`

### CASE-9: `hbss_onboardsource` 被消费 (1 处 · 1 跨云)

**core_hr（1 处）：**

- `hrpi_empentrel` → `onboardsource`

## ISV 改本场景实体的影响推演

假如 ISV 试图修改 `number` / `name` / `enable` 等标品公共字段：

| 改动 | 影响下游 | 后果 |
|---|---|---|
| 改 `number` 类型从 TextField 改 LongField | ❌ 全部下游 BasedataField 引用 | 下游 form 加载失败 |
| 删除标品字段 | ❌ 全部下游引用 | 下游 form 元数据加载错误 |
| 加 ISV 字段（前缀 _ext） | ✅ 不影响下游 | 下游不感知 |
| 改 `enable` 默认值 | ⚠️ 部分下游 | 数据导入时可能默认值不一致 |
