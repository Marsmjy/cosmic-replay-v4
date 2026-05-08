# hbss_supplier · 案例

> **生成时间**：2026-04-29
> **方法**：从 `_cross_cloud_reports/hr_hrmp_consumed_by.json` 提取真实跨云消费场景

## 真实跨云消费案例

### CASE-1: `hbss_workplace` 被消费 (13 处 · 13 跨云)

**org_dev（10 处）：**

- `haos_adminorg` → `workplace`
- `haos_adminorgdetail` → `workplace`
- `haos_adminorghis` → `workplace`
- `hbpm_positionhis` → `workplace`
- `hbpm_positionhr` → `workplace`
- `homs_orgbatchchgbill` → `add_workplace`, `parent_workplace`, `info_workplace`, `workplace`
- `homs_orgdifftemp` → `workplace`

**core_hr（3 处）：**

- `hrpi_dispatchinfo` → `dispworkplace`
- `hrpi_empposorgrel` → `workplace`, `contractworkplace`

### CASE-2: `hbss_procreatstatus` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_candidate` → `procreatstatus`

**core_hr（1 处）：**

- `hrpi_employee` → `procreatstatus`

### CASE-3: `hbss_category` 被消费 (2 处 · 2 跨云)

**payroll（1 处）：**

- `hcf_personalarea` → `regresidencenature`

**core_hr（1 处）：**

- `hrpi_perregion` → `regresidencenature`

### CASE-4: `hbss_procreatmode` 被消费 (1 处 · 1 跨云)

**core_hr（1 处）：**

- `hrpi_fertilityinfo` → `procreatmode`

## ISV 改本场景实体的影响推演

假如 ISV 试图修改 `number` / `name` / `enable` 等标品公共字段：

| 改动 | 影响下游 | 后果 |
|---|---|---|
| 改 `number` 类型从 TextField 改 LongField | ❌ 全部下游 BasedataField 引用 | 下游 form 加载失败 |
| 删除标品字段 | ❌ 全部下游引用 | 下游 form 元数据加载错误 |
| 加 ISV 字段（前缀 _ext） | ✅ 不影响下游 | 下游不感知 |
| 改 `enable` 默认值 | ⚠️ 部分下游 | 数据导入时可能默认值不一致 |
