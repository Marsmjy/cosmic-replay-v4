# hbss_edu_train · 案例

> **生成时间**：2026-04-29
> **方法**：从 `_cross_cloud_reports/hr_hrmp_consumed_by.json` 提取真实跨云消费场景

## 真实跨云消费案例

### CASE-1: `hbss_diploma` 被消费 (7 处 · 6 跨云)

**org_dev（3 处）：**

- `hbjm_jobhr` → `diplomareq`
- `hbpm_positionhis` → `diplomareq`
- `hbpm_positionhr` → `diplomareq`

**payroll（1 处）：**

- `hcf_caneduexp` → `education`

**core_hr（2 处）：**

- `hrpi_employeetaxcn` → `education`
- `hrpi_pereduexp` → `education`

### CASE-2: `hbss_credentialstype` 被消费 (7 处 · 6 跨云)

**payroll（1 处）：**

- `hcf_cancre` → `credentialstype`

**core_hr（5 处）：**

- `hrpi_blacklist` → `maincardtype`, `cardtypeimport`, `cardtype`
- `hrpi_employeetaxcn` → `credentialstype`
- `hrpi_percre` → `credentialstype`

### CASE-3: `hbss_college` 被消费 (4 处 · 3 跨云)

**other（1 处）：**

- `hbp_unittesttime01` → `college`

**payroll（1 处）：**

- `hcf_caneduexp` → `graduateschool`

**core_hr（1 处）：**

- `hrpi_pereduexp` → `graduateschool`

### CASE-4: `hbss_ocpquallevel` 被消费 (3 处 · 3 跨云)

**payroll（1 处）：**

- `hcf_canocpqual` → `qualevel`

**core_hr（2 处）：**

- `hrpi_perocpqual` → `qualevel`
- `hrpi_perpractqual` → `qualevel`

### CASE-5: `hbss_degree` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_caneduexp` → `degree`

**core_hr（1 处）：**

- `hrpi_pereduexp` → `degree`

### CASE-6: `hbss_diplomatype` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_caneduexp` → `edunature`

**core_hr（1 处）：**

- `hrpi_pereduexp` → `edunature`

### CASE-7: `hbss_educerttype` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_caneduexp` → `certtype`

**core_hr（1 处）：**

- `hrpi_pereduexp` → `certtype`

### CASE-8: `hbss_languagetype` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_canlgability` → `language`

**core_hr（1 处）：**

- `hrpi_perlgability` → `language`

### CASE-9: `hbss_languagecert` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_canlgability` → `languagecert`

**core_hr（1 处）：**

- `hrpi_perlgability` → `languagecert`

### CASE-10: `hbss_ocpqual` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_canocpqual` → `qualification`

**core_hr（1 处）：**

- `hrpi_perocpqual` → `qualification`

## ISV 改本场景实体的影响推演

假如 ISV 试图修改 `number` / `name` / `enable` 等标品公共字段：

| 改动 | 影响下游 | 后果 |
|---|---|---|
| 改 `number` 类型从 TextField 改 LongField | ❌ 全部下游 BasedataField 引用 | 下游 form 加载失败 |
| 删除标品字段 | ❌ 全部下游引用 | 下游 form 元数据加载错误 |
| 加 ISV 字段（前缀 _ext） | ✅ 不影响下游 | 下游不感知 |
| 改 `enable` 默认值 | ⚠️ 部分下游 | 数据导入时可能默认值不一致 |
