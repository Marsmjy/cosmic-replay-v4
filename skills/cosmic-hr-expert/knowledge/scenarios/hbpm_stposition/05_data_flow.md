# 数据流转 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于 6 反编译类 + HisModel 框架分析 + 物理表结构
> **confidence**: verified
> **审计时间**: 2026-04-27

---

## 1. 物理表写入路径

```
用户操作 → StandardPositionSaveOp / StandardPositionChangeOp
    │
    ├── HisModelOPCommonPlugin.beforeExecuteOperationTransaction（版本字段注入）
    │     └── boid / iscurrentversion / hisversion / sourcevid / firstbsed 自动写入
    │
    ├── StandardPositionSaveOp.beforeExecuteOperationTransaction
    │     └── entity.set("isstandardpos", "1")（强制标记）
    │
    └── 平台事务提交 → 写 t_hbpm_position + t_hbpm_standposentry + t_hbpm_position_l
```

---

## 2. 新建时序数据流

```
用户新增第一条标准岗位：
    输入: number="SP001", name="研发工程师", org=..., adminorg=..., bsed=2025-01-01

    HisModelOPCommonPlugin 注入时序字段：
        boid = id（新分配的 Long ID，如 12345）
        iscurrentversion = true
        hisversion = "1"
        sourcevid = null（第一版本无前驱）
        firstbsed = 2025-01-01（记录最早生效日期）

    StandardPositionSaveOp 注入：
        isstandardpos = "1"

    最终写入 t_hbpm_position：
        fid=12345, fboid=12345, fiscurrentversion=true,
        fhisversion="1", fsourcevid=null,
        ffirstbsed=2025-01-01, fbsed=2025-01-01, fbsled=9999-12-31,
        fisstandardpos="1", fnumber="SP001", ...

    写入 t_hbpm_position_l：
        fid=12345, flangid=zh_CN, fname="研发工程师", ...

    写入 t_hbpm_standposentry：
        fentryboid=新分配ID, fadminorgid=<adminorg_boid>, ...
```

---

## 3. 变更时序数据流（产生新版本）

```
标准岗位 boid=12345（版本1）变更，新生效日期=2026-01-01：

    HisModelOPCommonPlugin 处理：
        旧版本(id=12345): iscurrentversion=false, bsled=2025-12-31
        新版本(id=99999): boid=12345, iscurrentversion=true,
                          hisversion="2", sourcevid=12345（指向旧版本）
                          bsed=2026-01-01, bsled=9999-12-31

    数据库状态（查询 boid=12345 的所有版本）：
        id=12345: iscurrentversion=false, bsed=2025-01-01, bsled=2025-12-31, hisversion="1"
        id=99999: iscurrentversion=true,  bsed=2026-01-01, bsled=9999-12-31, hisversion="2"

    hbpm_positionhr 不受影响（引用的是 boid=12345，boid 未变）
```

---

## 4. 时序数据读取规范

### 4.1 查当前有效标准岗位（最常用）

```java
// 查所有当前有效的标准岗位（boid 维度）
QFilter[] filters = {new QFilter("iscurrentversion", "=", Boolean.TRUE)};
DynamicObjectCollection result = QueryServiceHelper.query(
    "hbpm_stposition",
    "id,boid,number,name,org,adminorg,job,enable",
    filters
);
```

### 4.2 查某个 boid 的当前版本

```java
// 根据 boid 查当前版本详情
QFilter[] filters = {
    new QFilter("boid", "=", stPositionBoid),
    new QFilter("iscurrentversion", "=", Boolean.TRUE)
};
DynamicObject position = QueryServiceHelper.queryOne(
    "hbpm_stposition",
    "id,boid,number,name,org,adminorg,job,bsed,bsled",
    filters
);
```

### 4.3 查某个 boid 的全部历史版本

```java
// 查历史版本（用于历史查询页）
QFilter[] filters = {new QFilter("boid", "=", stPositionBoid)};
// 按时间倒序排列
DynamicObjectCollection history = QueryServiceHelper.query(
    "hbpm_stposition",
    "id,boid,hisversion,bsed,bsled,iscurrentversion,changedescription",
    filters
);
```

### 4.4 从 hbpm_positionhr 查关联标准岗位

```java
// hbpm_positionhr 的 stposition 字段是 boid（PR-009）
// 先查 positionhr 拿 stposition boid
DynamicObject posHr = QueryServiceHelper.queryOne(
    "hbpm_positionhr",
    "id,stposition",
    new QFilter[]{new QFilter("id", "=", posHrId)}
);
Long stPosBoid = posHr.getLong("stposition");  // 这是 boid

// 再查标准岗位当前版本
QFilter[] filters = {
    new QFilter("boid", "=", stPosBoid),
    new QFilter("iscurrentversion", "=", Boolean.TRUE)
};
DynamicObject stPos = QueryServiceHelper.queryOne("hbpm_stposition", "id,boid,number,name", filters);
```

---

## 5. isstandardpos 区分键数据流

```
t_hbpm_position 物理表数据：
    id=1001, fisstandardpos="1"  ← hbpm_stposition 的数据（标准岗位）
    id=2001, fisstandardpos="0"  ← hbpm_positionhr 的数据（实际岗位）
    id=2002, fisstandardpos="0"

查询时 hbpm_stposition 视图：
    WHERE fisstandardpos = '1'

查询时 hbpm_positionhr 视图：
    WHERE fisstandardpos = '0'（或无此字段过滤，依赖元数据视图定义）
```

---

## 6. F7 数据流（跨场景选标准岗位）

```
调用方字段（如 hbpm_positionhr.stposition）触发 F7
    │
    ├── HisBaseDataF7FastFilter.setFilter（按关键字快速过滤）
    ├── PositionBaseDataF7FastFilter.setFilter（岗位专属：加 isstandardpos=1 过滤）
    ├── HisModelF7ListPlugin.beforeBindData（加 iscurrentversion=true 过滤 + 生效日期）
    │
    └── 用户选中 → F7 返回值 = iscurrentversion=true 版本的 boid
          └── 调用方字段存储该 boid（PR-009 · 不存 id）
```

---

## 7. 编码回收数据流（StandardPositionEdit）

```
用户新增时：
    setPositionNumber()
        └── CodeRuleServiceHelper.readNumber("hbpm_stposition", entity, orgId)
            └── 读取编码 + 暂时"占用"该编码号段
            └── recycleMap 缓存：{number: "SP001", org: xxx, adminorg: yyy, job: zzz}

用户保存时（success）：
    编码正式落库 · recycleMap 不再需要

用户放弃（pageRelease / isFromAdd=true / 非 CHANGE_PAGE）：
    recycleNumber()
        └── PositionCodeRuleHelper.recycleNumber(entities)
            └── 归还 SP001 编码回编码池
```

---

## 8. 数据一致性关键认知

| 场景 | 风险 | 正确处理 |
|---|---|---|
| 变更后 hbpm_positionhr 引用 | hbpm_positionhr 存的是 boid · 变更后 boid 不变 | 无需级联更新（PR-009 设计意图）|
| ISV 直接写 boid | 破坏时序一致性 | 永远不要 entity.set("boid", ...) |
| ISV 直接写 iscurrentversion | 造成多条 iscurrentversion=true | 永远不要 entity.set("iscurrentversion", ...) |
| 并发变更 | 同 boid 两个变更操作并发提交 | HisUniqueValidateOp 保证时序区间不重叠 |

---

## 9. 关联文档

- `03_model_design.md` · 物理表字段详细说明
- `04_business_flow.md` · 操作触发流程
- `11_upstream_downstream_logic.md` · 下游 hbpm_positionhr 引用关系
