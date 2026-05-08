# 扩展点档案索引

> **视角**: 以"扩展点"为中心的独立档案，与"场景视角"互补。
> **命名**: `<时机>_<宿主实体>.md`
> **引用关系**: 每个扩展点档案被 1-多个场景引用（`referenced_by_scenarios`）

---

## 按宿主实体分组

### haos_adminorg (行政组织主实体)

| 扩展点 | 时机 | 风险 |
|---|---|---|
| [beforeSave@haos_adminorg](./before_save_haos_adminorg.md) | 保存前 | ⚠️ 高 |
| [afterSave@haos_adminorg](./after_save_haos_adminorg.md) | 保存后 | 中 |
| [**onParentChange@haos_adminorg**](./on_parent_change_haos_adminorg.md) ⭐ | 上级变更时 | 🔴 极高 |
| [beforeDelete@haos_adminorg](./before_delete_haos_adminorg.md) | 删除前 | 中 |

### haos_adminorgtablist (行政组织列表)

| 扩展点 | 时机 | 风险 |
|---|---|---|
| [onListQuery@haos_adminorgtablist](./on_list_query_haos_adminorgtablist.md) | 列表查询前 | 中 |

---

## 按时机分组

### before_save (保存前)

- [beforeSave@haos_adminorg](./before_save_haos_adminorg.md)

### after_save (保存后)

- [afterSave@haos_adminorg](./after_save_haos_adminorg.md)

### 特殊时机

- [**onParentChange@haos_adminorg**](./on_parent_change_haos_adminorg.md) ⭐ 组织独有

---

## 待补充扩展点（其他实体）

- beforeSave@hr_person
- onStatusChange@haos_adminorg
- onFieldChange@haos_adminorg
- beforeSave@hbpm_position
- ...

---

**📌 说明**:
- 每个扩展点档案是独立数据源
- 场景视图的扩展点卡片是"摘要 + 场景专属注解"
- 点击"完整档案"跳转到这里
