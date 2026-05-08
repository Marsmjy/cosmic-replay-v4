# 数据流转 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟢 基于 `_auto_operations.md` 执行链 + `rules_chain_all.json` writeFields / readFields + `scene_doc.json` 物理表结构整合
> **关键发现**: 本场景是**时序基础资料 + 多语言子表 + 多选子表**的三表联写，不像行政组织有 4 前缀分录

---

## 一、数据落地 3 张物理表

```
hbjm_jobhr 表单保存
    ↓
分发到 3 张物理表：
    ├── t_hbjm_job          主物理表（非多语言、非多选字段）
    ├── t_hbjm_job_i        多语言子表（12 个 MuliLangTextField）
    └── t_hbjm_jobscmmul    多选子表（jobscm 字段）
```

**数据源**：`scene_doc.json` L26-L27 `physicalTable: "t_hbjm_job + t_hbjm_job_i（2 张物理表）"` + L805 `jobscm.physicalColumn: "t_hbjm_jobscmmul（子表）"`

### 字段 → 物理表映射

| 物理表 | 字段类型 | 字段数 | 典型字段 |
|---|---|---|---|
| `t_hbjm_job` | 标量 / 外键 / 日期 / 枚举 | 约 52 | `fnumber` / `fjobseqid` / `fstatus` / `fbsed` / `fboid` |
| `t_hbjm_job_i` | 多语言 | 12 | `fname` / `fsimplename` / `fdescription` / `foriname` / `fjobduty` 等 |
| `t_hbjm_jobscmmul` | 多选基础资料 | 隐式 | MulBasedataField 自动生成的关联表 |

---

## 二、save 操作的数据写入链

### 写入顺序（按 `_auto_operations.md` save 链 L99-L113）

```
save 触发
    ↓
[Phase 1: 编码准备]
    (1) CodeRuleOp - 如未填 number，按编码规则自动生成
    ↓
[Phase 2: 基础资料写入]
    (2) BaseDataSavePlugin - 写 t_hbjm_job 主表
    (3) BdVersionSaveServicePlugin - 版本管理（name 历史）
    ↓
[Phase 3: HR 状态维护]
    (4) HRBaseDataStatusOp - 维护 fstatus（A/B/C）
    (5) HRBaseDataLogOp - 操作日志（默认 disabled）
    (6) HRBaseDataEnableOp - 维护 fenable
    (7) HRBaseOriginalOp - 记录 forinumber/foriname/foristatus 出厂值（首次）
    ↓
[Phase 4: 时序维护]
    (8) HisUniqueValidateOp - 校验同 boid 同时段唯一
    (9) HisModelOPCommonPlugin - 写 fboid / fiscurrentversion / fdatastatus / fhisversion / ffirstbsed
    ↓
[Phase 5: 职位业务层]
    (10) JobHrSaveOp - 重写 onPreparePropertys / onAddValidators
         写 id 字段（rules_chain_all.json L394-L396）
```

### 关键写入字段（基于 rules_chain_all.json writeFields）

| opKey | writeFields | 来源 |
|---|---|---|
| `save` | `["id"]` | `rules_chain_all.json` L406 |
| `enable` | `[]` (JobHrEnableOp 只读 boid/enable 做校验) | `rules_chain_all.json` L744 |
| `audit` | `[]` | `rules_chain_all.json` L527 |
| `disable` | `[]` | `rules_chain_all.json` L629 |
| `confirmchange` | 同 save（复用 JobHrSaveOp） | - |

⚠️ **注意**：writeFields 在 rules_chain_all.json 里是从反编译的**方法级抽取**，不代表所有标品写入；标品插件（如 `BdVersionSaveServicePlugin` / `HisModelOPCommonPlugin`）会隐式写入更多字段（`boid` / `iscurrentversion` / `hisversion` 等），这部分需要进入反编译源码才能精确统计。

---

## 三、版本变更的数据流

### JobHrMsgHandleOp 处理数据流（`_auto_plugin_semantics.md` L13-L26）

```
变更事务触发（如 confirmchange）
    ↓
[beforeExecuteOperationTransaction]
    读: boid + iscurrentversion + sourcevid
    ↓
[endOperationTransaction]
    （事务提交前）
    ↓
[afterExecuteOperationTransaction]
    写: sourcevid
    ↓
完成：新版本的 sourcevid 指向旧版本 id
```

**业务意图**：在时序版本链上，让新版本记录知道它的上一版是谁（通过 `sourcevid` 字段），形成单向版本链。

---

## 四、事务边界

### 同步（事务内，失败则整体回滚）

| 写入目标 | 事务 | 失败 |
|---|---|---|
| `t_hbjm_job` | 主事务 | 回滚 |
| `t_hbjm_job_i` | 主事务 | 回滚 |
| `t_hbjm_jobscmmul` | 主事务 | 回滚 |
| 操作日志（标品 `HRBaseDataLogOp`） | 主事务 | 回滚 |
| 编码规则序号占用 | 主事务 | 回滚（`CodeRuleOp` 处理） |

### 异步（默认本场景无标品异步反写）

- **本场景没有跨模块同步反写插件**（区别于行政组织的 hrpi_employee / pay_salary_archive 联动）
- 如需加"职位变更反写岗位"等联动，需要自定义 `afterExecuteOperationTransaction` + 独立事务

---

## 五、数据读取链

### 列表读取

```
JobBaseDataListPlugin.setFilter
    ↓
追加 filter（默认只显示 enable=1 使用中）
    ↓
查询 t_hbjm_job (+ _i join)
    ↓
JobBaseBuListPlugin.filterContainerBeforeF7Select
    getPermOrgResult(String entityName) → 拿到管辖组织集
    ↓
追加 QFilter.in("createorg", 管辖 org ID 集)
    ↓
返回列表数据
```

### 详情读取

```
JobInitFilterCommonPlugin.billListHyperLinkClick
    ↓
跳转详情表单 JobBasedataEdit / JobHisBasedataEdit
    ↓
按 id 或 boid 查 t_hbjm_job（默认查 iscurrentversion=true 当前版本）
    ↓
关联 t_hbjm_job_i（多语言按用户语言筛选）
    ↓
关联 t_hbjm_jobscmmul（拿多个 jobscm）
    ↓
onLoadComplete → 渲染
```

---

## 六、时序模型的数据版本链

```
时间轴：
┌──────────────────────────────────────────────────────────┐
│  v1 (初始)            v2 (当前)             v3 (未来)     │
│  bsed=2020-01-01     bsed=2024-06-01      bsed=2026-10-01│
│  bsled=2024-05-31    bsled=2026-09-30     bsled=9999-12-31│
│  datastatus=初始     datastatus=当前       datastatus=未来│
│  iscurrentversion=F  iscurrentversion=T    iscurrentversion=F│
│  sourcevid=NULL      sourcevid=v1.id       sourcevid=v2.id │
│                                                            │
│  ^ 同一 boid 共享此业务链                                    │
└──────────────────────────────────────────────────────────┘
```

### 新增数据版本时的数据流

```
newhisversion
    ↓
复制当前版本所有字段 → 新记录
    ↓
新记录.boid = 原记录.boid（共享业务标识）
新记录.sourcevid = 原记录.id
新记录.iscurrentversion = false（新版本未生效）
新记录.bsed = 用户填写的新生效日期
新记录.datastatus = 新版本初始态
    ↓
原记录.bsled = 新记录.bsed - 1 day（自动收紧）
    ↓
到期后定时任务将 iscurrentversion 从旧记录切换到新记录
```

---

## 七、性能陷阱

### 陷阱 1：多语言字段列表查询慢

- **场景**：列表显示 12 个多语言字段时，需要 join `t_hbjm_job_i`
- **问题**：大数据量（>10000 职位）列表查询可能 > 2s
- **缓解**：列表配置减少显示列 / 索引 `t_hbjm_job_i(fpkid, flocaleid)`

### 陷阱 2：jobscm 多选查询

- **场景**：按职位体系方案筛选职位
- **问题**：`t_hbjm_jobscmmul` 子表 join 可能产生笛卡尔积
- **缓解**：用 `QFilter.exists("jobscm.id", ...)` 而非 inner join

### 陷阱 3：时序版本全量查询

- **场景**：查某职位的所有历史版本
- **问题**：同一 boid 可能有 N 版本，`showallversion` 会全部拉出
- **缓解**：分页 / 时间范围限制

---

## 八、数据一致性保障

### 机制 1：保存链 10 个插件的顺序约束

`CodeRuleOp` 必须在第 1 位（否则编码未生成就保存失败）；`JobHrSaveOp` 在第 10 位（最后业务补完）。`updateOperation.plugins` 是全量替换语义，**覆盖前必须先 get 再 append**，否则会丢标品 9 个插件。

### 机制 2：HisUniqueValidateOp 时序唯一性

同一 `boid` 在同一时段只能有一条有效数据（`iscurrentversion=true`）。save 和 confirmchange 的执行链都包含此插件（`_auto_operations.md` L110 / L595）。

### 机制 3：HRBaseOriginalOp 出厂值锁

标品 `HRBaseOriginalOp` 负责记录 `orinumber` / `oriname` / `oristatus`（出厂字段）初始值；后续任何修改不能反写这 3 字段。

### 机制 4：GroupFieldUnique 多重唯一

3 条唯一性校验（`_auto_operations.md` L120-L122）：
- 全局编码唯一（enabled）
- 创建组织 + 编码唯一（disabled，可按需切换）
- 创建组织 + 名称唯一（disabled，可按需切换）

### 机制 5：CodeRuleDeleteOp 编码回收

delete 时 `CodeRuleDeleteOp` 把占用的编码序号释放回编码规则（`_auto_operations.md` L137），保证删除后编码可复用。

---

## 九、本场景与行政组织的数据流差异

| 维度 | 行政组织 haos_adminorg | 职位 hbjm_jobhr |
|---|---|---|
| **下游反写** | 5+（hrpi_employee / hbpm_position / pay_salary_archive / att_schedule / perf_template） | 0 标品反写（默认） |
| **4 前缀分录** | 有（VQ597FqFoc / 7auphYEIJr / 8bosVcKAfQ / wHBtyCCUik） | 无 |
| **事件订阅** | `org_change_event` | 无（但 `JobHrMsgHandleOp` 可作变更广播源） |
| **系统计算字段** | level / longnumber / structlongnumber / belongcompany | 仅 `hisversion` / `iscurrentversion`（时序派生） |
| **物理表** | 1 张（3 视图共用） | 3 张（主 + 多语言 + 多选） |
| **级联影响** | 1 字段 → 8 包 | 1 字段 → 1-2 包（主表 + 多语言子表自动） |

---

**📌 来源追溯**：
- 3 张物理表：`scene_doc.json` L26-L27 / L805
- save 10 插件：`_auto_operations.md` L99-L113
- JobHrMsgHandleOp 读写字段：`_auto_plugin_semantics.md` L18-L20
- writeFields 数据：`rules_chain_all.json` L406（save）/ L744（enable）
- GroupFieldUnique 规则：`_auto_operations.md` L120-L122
- 时序机制：`_auto_operations.md` L110（HisUniqueValidateOp）+ L111（HisModelOPCommonPlugin）
