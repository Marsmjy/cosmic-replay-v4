# hbpm_positionhr · 定制方案（ISV 扩展真实模板）

> **form**：`hbpm_positionhr`
> **生成时间**：2026-04-29
> **方法**：基于反编译实物的标品类，给出 ISV 真扩展模板（不是空壳骨架）

## CS-01 · 加 ISV 自定义字段

在开发平台给本 form（`hbpm_positionhr`）加 ISV 字段：

```yaml
步骤:
  1. 设计器中打开本 form 元数据
  2. 添加 ISV 字段（建议加前缀如 _ext / _isv 标识私域）
  3. 字段类型避开 Required=true（防止数据迁入失败）
  4. 元数据扩展归属为 ISV · 不破坏标品 form （PR-001）
```

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 模板基于：admin_org_quick_maintenance（金标准）+ 反编译实证
- 跨云穿透：见 `11_upstream_downstream_logic.md` 自动注入段

---

## 被引用·成建制划转（业务流型聚合）

> ⚡ 本场景作为下游被引用方·在 `core_hr_org_unit_transfer` 业务流中扮演角色。

### 引用方
- **业务流场景**：[`core_hr_org_unit_transfer`](../core_hr_org_unit_transfer/) (HR 核心人力 / hdm 调配管理)
- **完整可复刻资产**：[`_assets/org_unit_transfer/`](../../_assets/org_unit_transfer/)

### 引用关系
成建制划转任务 C 通过 `OrgTransferHelper.queryPosHisDataByIdAndEffectDate` 查询本场景的历史岗位版本（`iscurrentversion!=1`）

### 部署后的影响
当客户部署成建制划转资产 (`_assets/org_unit_transfer/`)·调度任务每日跑会**消费本场景的标品数据** (查询岗位历史版本·刷新任职经历的 `positionvid` 字段)·然后批量装配标品调动单 `hdm_transferbatch` + `SUBMITEFFECT` 触发跨云协同。

### 跟本场景 ISV 扩展的关系
- ✅ 客户在本场景做 ISV 扩展（如 CS-01 加字段 / CS-02 反向引用查询）·**不影响**成建制划转资产消费
- ⚠️ 客户在本场景**改字段命名规范**（如改某字段 key）·**必须**同步检查 `core_hr_org_unit_transfer` 是否引用·避免破坏跨场景集成
