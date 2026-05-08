# 数据流转 · 行政组织快速维护

> **状态**: 🟢 基于 `adminorg_extension_pattern.md` 级联实证
> **关键发现**: 1 字段扩展 → 8+ 业务包同步扩展（级联面极大）

---

## 一、级联全景：1 字段扩展 = 8 包同步

**⚠️ 组织域最重要的认知**：给 `haos_adminorg` 加 1 个字段，**平均触发 8 个下游业务包同步扩展**。

```
haos_adminorg 扩展 (主表 1 字段)
    │
    ├─► hrmp-kdtest_haos_ext         (主扩展包, DYM)
    ├─► odc-kdtest_homs_ext          (调整单扩展, DYM + 4前缀)
    ├─► hr-hom-dm                    (信息组配置, SQL)
    ├─► hrmp-hrcs-dm                 (权限配置, SQL)
    ├─► hrpi 扩展                    (员工档案联动)
    ├─► hbpm 扩展                    (岗位管理)
    ├─► pay 扩展                     (薪酬档案)
    └─► att 扩展                     (考勤范围)
```

---

## 二、调整上级触发的跨模块反写

### 同步反写（事务内，失败回滚）

#### RW-01 · 反写 `hrpi_employee.org_path` 🟢

- **触发**: `onParentChange@haos_adminorg` → `afterSave` 事务内
- **目标**: 员工档案的组织路径字段
- **反写内容**: 重算组织路径（如 "总部/研发中心/HR 组"）
- **事务**: **同步，同一事务**
- **异常**: 失败则整个调整上级回滚
- **性能**: 下属 1000+ 岗位时可能耗时 5-10 秒
- **实现**: `OrgPathRecalcPlugin`（标品）

#### RW-02 · 反写 `hbpm_position.adminorg` 🟢

- **触发**: 同上
- **目标**: 岗位归属组织（如果 parentorg 链变了）
- **事务**: 同步

### 异步反写（独立事务 + 重试）

#### RW-03 · 反写 `pay_salary_archive.cost_center` 🟡

- **触发**: 事件订阅 `org_change_event`
- **目标**: 薪酬档案的成本中心字段
- **事务**: **独立异步**
- **失败**: 重试 3 次，最终失败记日志
- **触发条件**: 仅在成本中心字段随组织变化时触发
- **实现**: `PayCostCenterSyncSubscriber`（标品）

#### RW-04 · 反写 `att_schedule.org_scope` 🟡

- **触发**: 事件订阅
- **目标**: 考勤排班的范围
- **事务**: 独立异步

#### RW-05 · 反写 `perf_template.adminorg` 🟡

- **触发**: 事件订阅
- **目标**: 绩效模板的适用组织
- **事务**: 独立异步

---

## 三、数据流图

```
调整 haos_adminorg.parentorg
    │
    ├────── 同步反写（事务内）─────────┐
    │  🟢 hrpi_employee.org_path       │
    │  🟢 hbpm_position.adminorg        │ 失败则
    │                                   │ 整体回滚
    │                                   │
    ├────── 异步反写（独立事务）────────┤
    │  🟡 pay_salary_archive.cost_center│
    │  🟡 att_schedule.org_scope        │ 失败独立
    │  🟡 perf_template.adminorg        │ 重试 3 次
    │                                   │
    └────── 事件广播 ─────────────────┤
       📡 org_change_event              │ 供外部订阅
          (可供外部 BI/数仓订阅)         │ (消息队列)
```

---

## 四、事件体设计

### `org_change_event` 结构

```json
{
  "eventType": "PARENT_CHANGED",
  "orgId": 123456,
  "orgNumber": "ORG001",
  "oldParent": {
    "id": 1001,
    "number": "PARENT_OLD",
    "name": "旧上级"
  },
  "newParent": {
    "id": 1002,
    "number": "PARENT_NEW",
    "name": "新上级"
  },
  "bsed": "2026-04-18",
  "operatorId": 999,
  "timestamp": "2026-04-18T10:30:00Z"
}
```

### 其他事件类型

| 事件 | 触发 |
|---|---|
| `CREATED` | 新增组织 |
| `INFO_MODIFIED` | 信息变更（非 parent） |
| `PARENT_CHANGED` | 调整上级 |
| `STATUS_CHANGED` | 状态变化（启用/停用） |
| `DELETED` | 删除 |

---

## 五、事务边界速查表

| 反写目标 | 事务类型 | 失败影响 | 重试 | 典型耗时 |
|---|---|---|---|---|
| `hrpi_employee.org_path` | 同步 | **整体回滚** | - | 1-5s |
| `hbpm_position.adminorg` | 同步 | 整体回滚 | - | 100-500ms |
| `pay_salary_archive.cost_center` | 异步 | 主操作不受影响 | 3 次 | 5-10s |
| `att_schedule.org_scope` | 异步 | 主操作不受影响 | 3 次 | 1-5s |
| `perf_template.adminorg` | 异步 | 主操作不受影响 | 3 次 | 1-5s |
| `org_change_event` | 消息队列 | 主操作不受影响 | 队列重投 | 即发即忘 |

---

## 六、系统计算字段的反写链

**非常重要**：调整 parentorg 时，**系统自动计算**以下字段：

```
parentorg 变更
    ↓
OrgPathRecalcPlugin (标品, 同步)
    ↓
自动重算以下字段 (事务内):
    ├── level          = parentorg.level + 1
    ├── longnumber     = parentorg.longnumber + "/" + number
    ├── structlongnumber = 类似, 带类型前缀
    └── belongcompany  = 沿 parentorg 找"公司"或"集团"
    ↓
同时重算所有后代组织的上述 4 字段
    ↓
触发后代组织的 afterSave (级联事件)
```

**⚠️ 性能警告**: 深度大或下属多的情况，路径重算可能触发**上千条 SQL update**。

---

## 七、数据一致性保障机制

### 机制 1：前置校验阻止脏数据

`beforeSave` 链：编码唯一 + 层级深度 + 循环引用 检测

### 机制 2：系统计算字段防篡改

`level` / `longnumber` / `belongcompany` 由平台插件计算，自定义插件**不应**直接赋值。

### 机制 3：时间轴版本控制

每次变更产生新 `bsed` 版本，老版本进 `haos_adminorghis`，**永不丢失历史**。

### 机制 4：变动单审计

通过 `homs_orgchgbill` + `orgchgeffectlog` 追溯所有变动。

### 机制 5：跨模块反写失败的兜底

异步反写失败 → 记入失败队列 → 人工可补发 → 不阻塞主操作。

---

## 八、性能陷阱

### 陷阱 1：大组织调整上级

- **场景**: 组织 A 下属 2000+ 岗位，调整 A 的上级
- **问题**: 同步反写 `hrpi_employee.org_path` 可能卡 30 秒
- **建议**: 改走"调整申请"路径（异步批处理），不走快速维护

### 陷阱 2：深层组织树路径重算

- **场景**: 11+ 层深度的组织树
- **问题**: 一次 parentorg 变更触发上万条 SQL
- **缓解**: 限制层级 ≤ 10

### 陷阱 3：级联事件风暴

- **场景**: 批量调整 100+ 组织
- **问题**: 每个触发 5 个异步事件 = 500 次消息
- **建议**: 用 `homs_orgbatchc` 批量单（走标品批处理）

---

**📌 来源追溯**：
- 级联 8 包: `adminorg_extension_pattern.md` §1 扩展包结构
- 同步/异步反写: `anchors.md` + 实测
- 计算字段算法: `ontology.md` belongcompany 推算
- 4 前缀 OID: `adminorg_extension_pattern.md` §3
