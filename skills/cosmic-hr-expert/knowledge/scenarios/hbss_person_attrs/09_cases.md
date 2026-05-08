# hbss_person_attrs · 案例

> **生成时间**：2026-04-29
> **方法**：从 `_cross_cloud_reports/hr_hrmp_consumed_by.json` 提取真实跨云消费场景

## 真实跨云消费案例

### CASE-1: `hbss_nationality` 被消费 (6 处 · 6 跨云)

**payroll（2 处）：**

- `hcf_cancre` → `nationality`
- `hcf_candidate` → `nationality`

**core_hr（4 处）：**

- `hrpi_blacklist` → `nation`
- `hrpi_employee` → `nationality`
- `hrpi_employeetaxcn` → `birthplace`
- `hrpi_percre` → `nationality`

### CASE-2: `hbss_sex` 被消费 (6 处 · 6 跨云)

**payroll（2 处）：**

- `hcf_cancre` → `gender`
- `hcf_candidate` → `gender`

**core_hr（4 处）：**

- `hrpi_blacklist` → `gender`
- `hrpi_debardinfo` → `sex`
- `hrpi_employee` → `gender`
- `hrpi_percre` → `gender`

### CASE-3: `hbss_flok` 被消费 (4 处 · 4 跨云)

**payroll（2 处）：**

- `hcf_cancre` → `folk`
- `hcf_candidate` → `folk`

**core_hr（2 处）：**

- `hrpi_employee` → `folk`
- `hrpi_percre` → `folk`

### CASE-4: `hbss_politicalstatus` 被消费 (4 处 · 4 跨云)

**payroll（2 处）：**

- `hcf_canfamily` → `politicalstatus`
- `hcf_personalarea` → `politicalstatus`

**core_hr（2 处）：**

- `hrpi_familymemb` → `politicalstatus`
- `hrpi_perregion` → `politicalstatus`

### CASE-5: `hbss_addresstype` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_canaddress` → `addresstype`

**core_hr（1 处）：**

- `hrpi_peraddress` → `addresstype`

### CASE-6: `hbss_emergcontactype` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_cancontact` → `emergcontactype`

**core_hr（1 处）：**

- `hrpi_emrgcontact` → `emergcontactype`

### CASE-7: `hbss_healthstatus` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_candidate` → `healthstatus`

**core_hr（1 处）：**

- `hrpi_employee` → `healthstatus`

### CASE-8: `hbss_constellation` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_candidate` → `constellation`

**core_hr（1 处）：**

- `hrpi_employee` → `constellation`

### CASE-9: `hbss_zodiac` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_candidate` → `symbolicanimals`

**core_hr（1 处）：**

- `hrpi_employee` → `symbolicanimals`

### CASE-10: `hbss_marriagestatus` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_candidate` → `marriagestatus`

**core_hr（1 处）：**

- `hrpi_employee` → `marriagestatus`

## ISV 改本场景实体的影响推演

假如 ISV 试图修改 `number` / `name` / `enable` 等标品公共字段：

| 改动 | 影响下游 | 后果 |
|---|---|---|
| 改 `number` 类型从 TextField 改 LongField | ❌ 全部下游 BasedataField 引用 | 下游 form 加载失败 |
| 删除标品字段 | ❌ 全部下游引用 | 下游 form 元数据加载错误 |
| 加 ISV 字段（前缀 _ext） | ✅ 不影响下游 | 下游不感知 |
| 改 `enable` 默认值 | ⚠️ 部分下游 | 数据导入时可能默认值不一致 |
