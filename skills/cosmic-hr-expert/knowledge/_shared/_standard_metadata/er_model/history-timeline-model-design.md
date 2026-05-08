# HCM 历史模型与时间轴模型设计文档

> **基于源码分析**：`biz-bos-ext/bos-ext-hr`、`hrmp/hbppro`、`hrmp/hrpipro`  
> **版本**：苍穹  8.0.4+  
> **生成时间**：2026-03-25  
> **最后修订**：2026-03-27

---

## 目录

1. [概述与核心差异](#1-概述与核心差异)
2. [通用基础设施](#2-通用基础设施)
3. [历史模型（HisModel）](#3-历史模型hismodel)
   - 3.1 设计哲学
   - 3.2 两种模式
   - 3.3 核心数据结构
   - 3.4 模板继承体系
   - 3.5 实体配置（HisModelEntityConfig）
   - 3.6 版本生命周期
   - 3.7 服务架构
   - 3.8 通用服务（HisModelGeneralService）
   - 3.9 版本回写服务（HisVersionBackWriteService）
   - 3.10 SDK API
4. [时间轴模型（Timeline）](#4-时间轴模型timeline)
   - 4.1 设计哲学
   - 4.2 三种模式
   - 4.3 核心数据结构
   - 4.4 模板继承体系
   - 4.5 实体配置（TimelineEntityConf）
   - 4.6 数据生命周期
   - 4.7 服务架构
   - 4.8 校验器（TimelineCommonValidator）
   - 4.9 SDK API
5. [两种模型的对照分析](#5-两种模型的对照分析)
6. [典型场景](#6-典型场景)
7. [工程代码位置索引](#7-工程代码位置索引)
8. [最佳实践与注意事项](#8-最佳实践与注意事项)

---

## 1. 概述与核心差异

金蝶苍穹 HCM 系统中存在两套独立的"带时间维度的数据版本管理"框架：

| 维度        | 历史模型（HisModel）                                                         | 时间轴模型（Timeline）                                |
| --------- | ---------------------------------------------------------------------- | ---------------------------------------------- |
| **定位**    | **主实体的版本管理**——对整个 BaseEntity 进行版本控制                                    | **分录/关联实体的时间段管理**——对从属数据按时间段切分                 |
| **适用粒度**  | 基础资料主表（如员工、组织、岗位）                                                      | 从属关系表 / 子实体（如任职经历、岗位兼任、方案明细）                   |
| **核心概念**  | BO（业务对象）+ Version（版本）                                                  | 时间段（Timespan）+ 时间线（Timeline）                   |
| **关键字段**  | `boid` / `iscurrentversion` / `bsed` / `bsled` / `datastatus`          | `startdate` / `enddate` / `iscurrentdata`      |
| **时间规则**  | 不间断不重叠 / 仅一个有效版本                                                       | 不间断不重叠 / 可间断不重叠 / 可间断可重叠                       |
| **源码包**   | `kd.hr.hbp.business.service.history`                                   | `kd.hr.hbp.business.service.timeline`          |
| **SDK入口** | `HisModelServiceHelper`                                                | `TimelineServiceHelper`                        |
| **平台扩展层** | `biz-bos-ext/bos-ext-hr` 中的 `HisModelBasedataField`、`HisModelConfigAp` | `biz-bos-ext/bos-ext-hr` 中的 `TimeLineConfigAp` |

**一句话总结**：

- **历史模型** = "同一个人/组织在不同时期的快照"
- **时间轴模型** = "一个人在不同时间段上的多条关联记录"

```
┌──────────────────────────────────────────────────────────────────┐
│                     HCM 时间数据管理体系                          │
│                                                                  │
│  ┌─────────────────────────┐  ┌─────────────────────────────┐   │
│  │    历史模型 HisModel     │  │    时间轴模型 Timeline       │   │
│  │                         │  │                             │   │
│  │  BO(主)  ───── 版本1    │  │  逻辑主键A ─── 时间段1      │   │
│  │          ├── 版本2      │  │              ├── 时间段2     │   │
│  │          └── 版本3      │  │              └── 时间段3     │   │
│  │                         │  │                             │   │
│  │  例：员工/组织/岗位      │  │  例：任职经历/岗位兼任      │   │
│  └─────────────────────────┘  └─────────────────────────────┘   │
└──────────────────────────────────────────────────────────────────┘
```

---

## 2. 通用基础设施

### 2.1 SDK 通用返回结构（HrApiResponse）

历史模型和时间轴模型的 SDK 方法统一使用 `HrApiResponse<T>` 作为返回结构：

```java
@SdkPublic
public class HrApiResponse<T> implements Serializable {
    private boolean success = true;       // 是否成功，默认 true
    private String code = "200";          // 返回编码，成功="200"，失败="500"
    private String errorMessage;          // 错误信息（失败时非空）
    private T data;                       // 业务数据

    // 静态工厂方法
    public static <T> HrApiResponse<T> success(T data);   // 构造成功响应
    public static <T> HrApiResponse<T> fail(String msg);   // 构造失败响应
}
```

**调用方判断逻辑**：

```java
HrApiResponse<HisCreateVersionReturnData> resp = HisModelServiceHelper.createDataVersions(param);
if (resp.isSuccess()) {
    HisCreateVersionReturnData data = resp.getData();
    // 处理成功数据
} else {
    String error = resp.getErrorMessage();
    // 处理错误信息
}
```

---

## 3. 历史模型（HisModel）

### 3.1 设计哲学

历史模型解决的核心问题是：**HR 基础资料需要追溯历史变更**。

在 HR 系统中，一个"员工"、"行政组织"或"岗位"的属性会随时间变化（如员工改名、组织更名、岗位职级调整）。历史模型的设计目标是：

1. **保留完整变更轨迹**：每次变更产生一个新版本，旧版本不删除
2. **按时间点查询任意版本**：给定任意日期，能查到当时生效的那个版本
3. **支持"未来变更"预设**：可以提前录入未来某日生效的变更，到期自动切换
4. **BO 数据始终代表最新生效**：BO（iscurrentversion=true）始终反映当前最新数据

### 3.2 两种模式

历史模型支持两种工作模式，由 `HisModelTypeEnum` 枚举定义：

```java
public enum HisModelTypeEnum {
    NO_INTERRUPTION_NO_OVERLAP("1"),  // 不间断不重叠（时序历史）
    ONLY_ONE_EFFECT_VERSION("2");     // 仅一个有效版本（非时序历史）
}
```

#### 3.2.1 模式一：不间断不重叠（时序历史）

**适用场景**：员工、行政组织、岗位等需要完整时间线追溯的基础资料。

**核心规则**：

- 版本的 `[bsed, bsled]` 时间区间**首尾相连、无间隙、不重叠**
- 新版本插入时，自动调整前后版本的生失效日期，保证时间线连续
- 支持 **变更（Change）**：在时间线中间或末尾插入新版本
- 支持 **修订（Revise）**：对已有版本的数据做原地更新，不改变时间区间

```
时间线示意（不间断不重叠）：

版本1: |========|                    bsed=2020-01-01, bsled=2022-06-30
版本2:          |========|           bsed=2022-07-01, bsled=2024-12-31
版本3:                    |========> bsed=2025-01-01, bsled=2999-12-31
       ────────────────────────────────────────────────────> 时间轴
```

**版本变更计算逻辑**（`HisModelVersionChangeCalService`）：

当插入新版本 `[startDate, endDate]` 时：

1. 查询 BO 下所有有效版本（`datastatus IN ('1','0')`），按生效日期升序排序
2. 找到 `front`：满足 `versionStartDate <= startDate && versionEndDate >= startDate` 的版本
3. 找到 `behind`：满足 `versionStartDate <= endDate && versionEndDate >= endDate` 的版本
4. **边界情况**：如果 `front` 和 `behind` 是同一条记录（新版本完全在某个已有版本内部），则需要将该版本从中间分割为两段
5. 被完全覆盖的中间版本（`versionStartDate >= startDate && versionEndDate <= endDate`）→ 标记为 **已废弃（DISCARDED）**
6. `front` 的失效日期调整为 `startDate - 1天`
7. `behind` 的生效日期调整为 `endDate + 1天`
8. 如果 `front` 被从中间分割，则复制一份到 `endDate + 1天` 之后
9. 如果新版本是时间线最后一条数据，其失效日期自动设为 `2999-12-31`

**变更确认提示**（`getChangeTips` 方法）：

根据版本变化情况生成不同的提示语：

- 覆盖全部版本 → "将新增1个'XX'状态的版本，其日期范围与历史所有版本完全重叠，重叠版本将被废弃"
- 切割出新版本 → "将新增2个'XX'的版本，其日期范围与现有版本存在时间交叉"
- 改变现有版本日期 → "将新增1个'XX'状态的版本，现有版本的生/失效日期将自动调整"
- 有覆盖 → "重叠部分将被废弃，未重叠部分的生/失效日期将自动调整"

#### 3.2.2 模式二：仅一个有效版本（非时序历史）

**适用场景**：薪酬方案等只需保留"最新版本"+"历史记录"但不需要时间区间管理的数据。

**核心规则**：

- 任意时刻只有 **一个** `datastatus=已生效` 的版本
- 新版本生效时，旧的已生效版本自动变为 **已废弃**（由 `HisOnlyOneEffectVersionHandler` 处理）
- 不维护 `bsed/bsled` 的时间连续性

```
版本管理示意（仅一个有效版本）：

版本1: [已废弃]  ← 曾经生效，被版本2替代
版本2: [已废弃]  ← 曾经生效，被版本3替代
版本3: [已生效]  ← 当前唯一生效版本
版本4: [暂存]    ← 未来某版本，尚未生效
```

### 3.3 核心数据结构

历史模型通过**模板继承**为实体注入一组固定字段，定义在 `HisModelConstants` 中：

| 字段Key               | 常量名                   | 类型            | 说明                                               |
| ------------------- | --------------------- | ------------- | ------------------------------------------------ |
| `boid`              | `BOID`                | BigIntField   | **逻辑主键**。BO 记录的 `boid = id`；版本记录的 `boid = BO的id` |
| `iscurrentversion`  | `IS_CURRENT_VERSION`  | CheckBoxField | `true`=BO记录（业务对象），`false`=版本记录                   |
| `bsed`              | `EFF_START_DATE`      | DateField     | 版本开始生效的日期                                        |
| `bsled`             | `EFF_END_DATE`        | DateField     | 版本停止生效的日期，最大值通常为 `2999-12-31`                    |
| `datastatus`        | `DATA_VERSION_STATUS` | ComboField    | 数据版本状态，见下方枚举                                     |
| `firstbsed`         | `EFF_FIRST_BSED`      | DateField     | BO 级别，记录该 BO 的第一个版本的生效日期                         |
| `sourcevid`         | `SOURCE_ID`           | BigIntField   | 与BO数据关联的版本ID                                     |
| `hisversion`        | `HISVERSION`          | TextField     | 自动递增：V0001, V0002, ...                           |
| `changedescription` | `CHANGEDESCRIPTION`   | TextField     | 用户输入的变更备注                                        |
| `entryboid`         | `ENTRY_BOID`          | BigIntField   | 分录 boid，用于跨版本追踪同一条分录记录                           |

#### 数据版本状态（HisModelDataStatusEnum）

```java
public enum HisModelDataStatusEnum {
    TEMP("-3"),        // 暂存版本 —— 尚未提交，编辑中
    DISCARDED("-2"),   // 已废弃版本 —— 被新版本覆盖或手工废弃
    EFFECTING("1"),    // 已生效版本 —— 当前生效中
    TO_BE_EFFECT("0"); // 待生效版本 —— 未来日期生效，等待定时任务切换

    // 有效数据状态集合（已生效 + 待生效）
    public static Set<String> availableStatus() {
        return Stream.of("1", "0").collect(Collectors.toSet());
    }
}
```

#### BO 与 Version 的数据模型

```
同一张物理表中同时存储 BO 记录和 Version 记录：

假设当天日期为 2025-04-10：

┌────────────────────────────────────────────────────────────────────────────┐
│ t_hrpi_employee  (员工表)                                                   │
├──────┬───────┬────────────────┬────────────┬────────────┬──────────┬──────┤
│  id  │ boid  │iscurrentversion│   bsed     │   bsled    │datastatus│ name │
├──────┼───────┼────────────────┼────────────┼────────────┼──────────┼──────┤
│ 1001 │ 1001  │    true        │ 2025-01-01 │ 2999-12-31 │    1     │ 张三 │  ← BO（由最新生效版本V3回写）
│ 2001 │ 1001  │    false       │ 2020-01-01 │ 2022-06-30 │    1     │ 张三 │  ← Version V1（已生效）
│ 2002 │ 1001  │    false       │ 2022-07-01 │ 2024-12-31 │    1     │ 张三丰│  ← Version V2（已生效，改名）
│ 2003 │ 1001  │    false       │ 2025-01-01 │ 2999-12-31 │    1     │ 张三 │  ← Version V3（已生效，改回）
└──────┴───────┴────────────────┴────────────┴────────────┴──────────┴──────┘

说明：
- BO 的 bsed/bsled 和业务字段（name）由最新生效版本（V3）通过 HisVersionBackWriteService 回写
- V3 的 bsed=2025-01-01 ≤ 当天(2025-04-10)，所以 datastatus=1（已生效）
- 如果 V3 的 bsed 是未来日期（如 2025-06-01），则 datastatus=0（待生效），
  此时 BO 仍由 V2 回写，BO 的 bsed/bsled/name 与 V2 一致

查询规则：
- 查 BO 数据：WHERE iscurrentversion = true
- 查某日版本：WHERE boid = ? AND iscurrentversion = false 
              AND datastatus IN ('1','0') AND bsed <= ? AND bsled >= ?
```

### 3.4 模板继承体系

历史模型通过 5 种元数据模板实现字段注入，实体通过继承模板获得历史模型能力：

| 模板编码                     | 模板名称         | 模板ID           | 说明           |
| ------------------------ | ------------ | -------------- | ------------ |
| `hbp_orimintimeseqtpl`   | HR原生最小历史模板   | `2/TXG194/JP1` | 仅包含核心字段      |
| `hbp_histimeseqtpl`      | HR全页面历史模板    | `2/TYOWOSM0TE` | 包含列表+表单+变更按钮 |
| `hbp_hisbutimeseqtpl`    | HR带BU历史模板    | `2/TYVXZRP5/F` | 带业务单元选择      |
| `hbp_hisgrptimeseqtpl`   | HR带分组历史模板    | `2/TYD9B+8LRE` | 带分组选择        |
| `hbp_hisbugrptimeseqtpl` | HR带BU带分组历史模板 | `2/TYGWXZFXTV` | BU + 分组      |

在 `biz-bos-ext` 中，通过 XML 配置注册了 `HisModelConfigAp` 控件，用于在表单设计器中配置历史模型参数：

```xml
<!-- biz-bos-ext/bos-ext-hr/.../HR_ExtendModelTemplate.xml -->
<Style Id="HisModelConfigAp" style="Control">
    <Id>HisModelConfigAp</Id>
    <Name>历史模型配置</Name>
    <PackageName>kd.bos.ext.hr.metadata.form.control</PackageName>
    <ExtendClassName>kd.bos.ext.hr.metadata.form.control.HisModelConfigAp</ExtendClassName>
</Style>
```

判断实体是否继承历史模型模板（`HisModelCommonService.isInheritHisModelTemplate`）：

```java
public boolean isInheritHisModelTemplate(String entityNumber) {
    MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType(entityNumber);
    return dataEntityType.isInheritFrom(HIS_ORI_MIN_TPL_ID) 
        || dataEntityType.isInheritFrom(HIS_TPL_ID)
        || dataEntityType.isInheritFrom(HIS_BU_TPL_ID) 
        || dataEntityType.isInheritFrom(HIS_GRP_TPL_ID)
        || dataEntityType.isInheritFrom(HIS_BU_GRP_TPL_ID);
}
```

### 3.5 实体配置（HisModelEntityConfig）

每个历史模型实体需要通过 `HisModelConfigAp` 控件进行配置，运行时解析为 `HisModelEntityConfig`：

```java
@SdkPublic
public class HisModelEntityConfig implements Serializable {
    String entityNumber;              // 实体编码
    HisModelTypeEnum modelType;       // 模式：不间断不重叠 / 仅一个有效版本
    boolean reviseCover = false;      // 修订时是否可以覆盖版本
    boolean allowSetDisableDate = false; // 是否允许用户设置禁用日期
    boolean enableChangeConfirm = true;  // 变更是否提示二次确认
    List<String> noUpdateFields;      // 版本生效时不更新的字段（回写BO时跳过）
    List<String> compareFields;       // 版本对比时参与对比的字段
    List<String> customPlugins;       // 版本生效定时任务的自定义业务插件类名
    Boolean needSynBo;                // 是否需要定时任务同步BO数据
}
```

**配置获取方式**：

```java
// 方式一：通过 SDK（对外）
HisModelEntityConfig config = HisModelServiceHelper.queryHisModelEntityConfig("hrpi_employee");

// 方式二：通过内部服务（对内）
HisModelEntityConfig config = HisModelCommonService.getInstance().getHisModelEntityConfig("hrpi_employee");
// 不存在则抛异常的版本：
HisModelEntityConfig config = HisModelCommonService.getInstance().getEntityConfigWithException("hrpi_employee");
```

### 3.6 版本生命周期

```
┌──────────┐      createTempVersions()     ┌──────────┐
│  不存在   │ ─────────────────────────────→ │   暂存    │
└──────────┘                                │ TEMP(-3) │
                                            └────┬─────┘
                                                 │ createDataVersions()
                                                 ▼
                                ┌───────────────────────────────────────┐
                                │     版本生效日期 vs 当前日期            │
                                └────────┬──────────────┬───────────────┘
                              生效日期≤今天            生效日期>今天
                                   │                        │
                                   ▼                        ▼
                            ┌──────────┐            ┌──────────────┐
                            │  已生效   │            │   待生效      │
                            │EFFECTING │            │TO_BE_EFFECT  │
                            │   (1)    │            │    (0)       │
                            └────┬─────┘            └──────┬───────┘
                                 │                         │ 定时任务到期生效
                                 │                         │ (HisModelVersionEffectTask)
                                 │                         ▼
                                 │                  ┌──────────┐
                                 │                  │  已生效   │
                                 │                  │EFFECTING │
                                 │                  └──────────┘
                                 │
                被新版本覆盖 │ reviseVersions() / disableBo()
                                 ▼
                          ┌──────────────┐
                          │   已废弃      │
                          │ DISCARDED(-2)│
                          └──────────────┘
```

**状态判断工具方法**（`HisModelGeneralService`）：

```java
// 根据当前日期自动更新数据版本状态
HisModelGeneralService.getInstance().updateDataStatus(data, today);
// 内部逻辑：bsed <= today → EFFECTING，bsed > today → TO_BE_EFFECT
```

**定时任务**：`HisModelVersionEffectTask` 会扫描所有 `datastatus = '0'`（待生效）且 `bsed ≤ 当天` 的版本，将其状态改为已生效，同时通过 `HisVersionBackWriteService` 回写 BO 数据。

### 3.7 服务架构

```
┌───────────────────────────────────────────────────────────────┐
│                    SDK 层（对外暴露）                           │
│  HisModelServiceHelper         HisModelInitServiceHelper      │
│  @SdkService                   @SdkService                    │
└───────────────┬───────────────────────────┬───────────────────┘
                │                           │
                ▼                           ▼
┌───────────────────────────┐   ┌───────────────────────────┐
│    HisModelAPIService     │   │  HisModelInitServiceFactory│
│    (API层/单例)            │   │  (初始化服务工厂)           │
└───────────────┬───────────┘   └───────────────────────────┘
                │
                ▼
┌─────────────────────────────────────────┐
│         HisModelServiceFactory          │
│   根据 HisModelTypeEnum 分派:           │
│   ├── NO_INTERRUPTION_NO_OVERLAP("1")   │
│   │   → HisModelLineService（时序服务）   │
│   └── ONLY_ONE_EFFECT_VERSION("2")      │
│       → HisModelNonLineService（非时序）  │
└─────────────┬──────────────┬────────────┘
              │              │
              ▼              ▼
┌──────────────────┐ ┌──────────────────┐
│HisModelLineService│ │HisModelNonLine.. │
│  implements       │ │  implements      │
│  IHisModelService │ │  IHisModelService│
└────────┬─────────┘ └────────┬─────────┘
         │                    │
   ┌─────┴─────┐        ┌────┴────────────────┐
   │ 版本变更   │        │ HisOnlyOneEffect    │
   │ 计算服务   │        │ VersionHandler      │
   │ CalService │        │ (唯一版本处理器)     │
   └───────────┘        └─────────────────────┘
```

**工厂分派逻辑**（`HisModelServiceFactory`）：

```java
// 来自OP操作
IHisModelService service = HisModelServiceFactory.getServiceForOP(entityNumber);
// 来自API调用
IHisModelService service = HisModelServiceFactory.getServiceForAPI(entityNumber);
// 内部根据 modelType 分派：
//   NO_INTERRUPTION_NO_OVERLAP → new HisModelLineService(...)
//   ONLY_ONE_EFFECT_VERSION    → new HisModelNonLineService(...)
```

**核心接口 IHisModelService**：

```java
public interface IHisModelService {
    // 创建暂存版本
    HisCreateVersionReturnData createTempVersions(HisCreateVersionParam param);
    // 版本生效
    HisCreateVersionReturnData createDataVersions(HisCreateVersionParam param);
    // 启用 BO
    HisEnableDisableReturnData enableBo(HisEnableDisableParam param);
    // 禁用 BO
    HisEnableDisableReturnData disableBo(HisEnableDisableParam param);
    // 删除 BO（级联删除所有版本）
    HisDeleteReturnData deleteBo(HisDeleteParam param);
    // 修订已有版本
    HisReviseReturnData reviseVersions(HisReviseParam hisReviseParam);
}
```

**时序服务额外方法**（`HisModelLineService`）：

```java
// 手动生效待生效版本（仅时序型支持）
public HisEffectVersionReturnData effectToBeEffectVersions(HisEffectVersionParam param);
```

### 3.8 通用服务（HisModelGeneralService）

`HisModelGeneralService` 是历史模型的核心通用服务（单例），提供版本管理中的公共操作：

| 方法                                                                      | 说明                                                 |
| ----------------------------------------------------------------------- | -------------------------------------------------- |
| `setNewDataIds(dataList)`                                               | 为新数据设置 ID 和 BOID（新数据的 boid = id）                   |
| `initEntryBoId(dataList)`                                               | 初始化分录的 entryboid，用于跨版本追踪同一条分录                      |
| `buildHisBoIdDataGroup(entityNumber, dataList)`                         | 按 BOID 分组构建 `HisBoIdDataGroup`，并查询每组的时间线数据         |
| `queryTimeLineDataAndSetHisBoIdDataGroup(entityNumber, groups)`         | 查询 BO 的所有有效版本并设置到分组中                               |
| `updateDataStatus(data, today)`                                         | 根据当前日期自动更新数据版本状态（bsed ≤ today → 已生效，> today → 待生效） |
| `hisCopy(fromDy, helper)`                                               | 复制历史数据（生成新 ID，保留业务字段）                              |
| `initNewEffectVersion(version, boId, firstEffect, today, maxEndDate)`   | 初始化新的生效版本数据                                        |
| `initNewNonLineVersion(version, boId)`                                  | 初始化非时序模式的新版本数据                                     |
| `getEnableOrDisableData(boData, boEnableMap, now, currentUser, helper)` | 获取启用/禁用操作的目标数据（判断是否需要复制新版本）                        |
| `getEffectQFilter()`                                                    | 获取已生效版本的 QFilter                                   |
| `buildErrorMsgForSDK(returnData)`                                       | 构建 SDK 返回的错误信息                                     |

### 3.9 版本回写服务（HisVersionBackWriteService）

版本回写是历史模型的核心机制——当版本生效时，需要将版本数据同步到 BO 记录上。

```java
@SdkInternal
public class HisVersionBackWriteService implements HisModelConstants {
    // 版本数据 → 回写 BO
    public void backWrite(DynamicObject boData, DynamicObject backWriteVersion, 
                          HisModelAttachRelationInfo attachRelationInfo,
                          HisModelEntityConfig entityConfig);

    // 批量回写
    public void backWriteBatch(List<DynamicObject> boDataList, List<DynamicObject> backWriteVersionList,
                               HisModelAttachRelationInfo attachRelationInfo, 
                               HisModelEntityConfig entityConfig);

    // BO 数据 → 回写版本（反向回写，用于修订场景）
    public void backWriteFromBo(DynamicObject versionData, DynamicObject boData,
                                HisModelAttachRelationInfo attachRelationInfo,
                                HisModelEntityConfig entityConfig);
}
```

**回写时固定排除的字段**：

```java
Set<String> ignoreKeys = Stream.of(
    "masterid",          // 主数据ID
    "sourcevid",         // 来源版本ID
    "firstbsed",         // 最早生效日期
    "hisversion",        // 版本号
    "iscurrentversion",  // 是否当前版本
    "creator",           // 创建人
    "creator_id",        // 创建人ID
    "createtime"         // 创建时间
).collect(Collectors.toSet());
```

**额外排除的字段**：`HisModelEntityConfig.noUpdateFields` 中配置的字段也会被排除。

**回写后的处理**：

- BO 的 `iscurrentversion` 设为 `true`
- BO 的 `id` 和 `boid` 保持不变
- BO 的 `sourcevid` 设为回写版本的 ID
- 记录附件复制关系（`HisModelAttachRelationInfo`），由调用方后续处理附件保存

### 3.10 SDK API

#### 3.10.1 版本管理（HisModelServiceHelper）

```java
// 1. 创建暂存版本（编辑中，未生效）
HisCreateVersionParam param = new HisCreateVersionParam();
param.setEntityNumber("hrpi_employee");
param.setDataList(Arrays.asList(dynamicObject));
HrApiResponse<HisCreateVersionReturnData> resp = HisModelServiceHelper.createTempVersions(param);

// 2. 版本生效
HrApiResponse<HisCreateVersionReturnData> resp = HisModelServiceHelper.createDataVersions(param);

// 3. 修订版本（对已生效版本做数据修正，不改时间区间）
HisReviseParam reviseParam = new HisReviseParam();
reviseParam.setEntityNumber("hrpi_employee");
reviseParam.setDataList(Arrays.asList(revisedObj));
HrApiResponse<HisReviseReturnData> resp = HisModelServiceHelper.reviseVersions(reviseParam);

// 4. 启用/禁用 BO
HisEnableDisableParam edParam = new HisEnableDisableParam();
edParam.setEntityNumber("hrpi_employee");
edParam.setDataList(Arrays.asList(boObj));
HrApiResponse<HisEnableDisableReturnData> resp = HisModelServiceHelper.enableBo(edParam);
HrApiResponse<HisEnableDisableReturnData> resp = HisModelServiceHelper.disableBo(edParam);

// 5. 删除 BO（级联删除所有版本）
HisDeleteParam delParam = new HisDeleteParam();
delParam.setEntityNumber("hrpi_employee");
delParam.setDataList(Arrays.asList(boObj));
HrApiResponse<HisDeleteReturnData> resp = HisModelServiceHelper.deleteBo(delParam);

// 6. 查询实体是否为历史模型实体
boolean isHis = HisModelServiceHelper.isInheritHisModelTemplate("hrpi_employee");

// 7. 查询实体的模式类型
EntityInheritTypeEnum type = HisModelServiceHelper.queryEntityModelType("hrpi_employee");

// 8. 获取实体配置
HisModelEntityConfig config = HisModelServiceHelper.queryHisModelEntityConfig("hrpi_employee");

// 9. 获取系统最大失效日期（通常为 2999-12-31）
Date maxEndDate = HisModelServiceHelper.getSysMaxEffEndDate();

// 10. 查询修订日志
DynamicObjectCollection logs = HisModelServiceHelper.queryReviseLogs("hrpi_employee", Arrays.asList(1001L));

// 11. 手动触发待生效版本生效（仅时序型支持）
HisEffectVersionParam effectParam = new HisEffectVersionParam();
effectParam.setEntityNumber("hrpi_employee");
effectParam.setToBeEffectVersions(toBeEffectVersions);
HrApiResponse<HisEffectVersionReturnData> resp = HisModelServiceHelper.effectToBeEffectVersions(effectParam);
```

#### 3.10.2 附件服务（HisModelAttachServiceHelper）

```java
// 查询附件信息
HisAttachParam attachParam = new HisAttachParam();
attachParam.setEntityNumber("hrpi_employee");
attachParam.setIds(Arrays.asList(1001L));
List<HisModelAttachInfo> infos = HisModelAttachServiceHelper.queryAttachmentInfos(attachParam);

// 复制附件到新数据
HisModelAttachServiceHelper.copyAttachmentInfos(entityNumber, copyIdMap, attachInfos);

// 上传并保存附件
HisModelAttachServiceHelper.uploadAttachments(entityNumber, attachInfos);
```

#### 3.10.3 查询过滤器（常用 QFilter 模式）

```java
// 查询某个 BO 当前生效的版本
QFilter effectingFilter = new QFilter("iscurrentversion", QCP.equals, false)
    .and(new QFilter("datastatus", QCP.equals, "1"));

// 查询某日期点的有效版本
QFilter pointFilter = new QFilter("bsed", QCP.less_equals, date)
    .and(new QFilter("bsled", QCP.large_equals, date))
    .and(new QFilter("datastatus", QCP.in, new String[]{"1", "0"}));

// 查询包含未来版本的有效数据
QFilter includeFuture = new QFilter("datastatus", QCP.in, new String[]{"1", "0"})
    .and(new QFilter("bsed", QCP.less_equals, date))
    .and(new QFilter("bsled", QCP.large_equals, date));
```

---

## 4. 时间轴模型（Timeline）

### 4.1 设计哲学

时间轴模型解决的核心问题是：**从属数据需要按时间段管理**。

与历史模型管理"主实体的整体快照"不同，时间轴模型管理的是"一组按时间段划分的关联记录"。典型场景如：

- 员工的**任职经历**：同一员工在不同时间段对应不同的组织+岗位
- 员工的**薪酬标准**：同一员工在不同时间段对应不同的薪资方案
- 组织的**负责人任期**：同一组织在不同时间段有不同的负责人

时间轴模型的关键特征：

1. 以 **逻辑主键（LogicKey）** 定义一条时间线，如 `{employee_id}` 或 `{employee_id, position_id}`
2. 同一逻辑主键下的多条记录按 `[startdate, enddate]` 排列
3. 时间段之间的约束关系由 **模型类型** 决定

### 4.2 三种模式

时间轴模型支持三种模式，由 `TimelineModelTypeEnum` 枚举定义：

```java
public enum TimelineModelTypeEnum {
    NOINTERRUPTION_NOOVERLAP("0"),   // 不间断、不重叠
    INTERRUPTION_NOOVERLAP("1"),     // 可间断、不重叠
    INTERRUPTION_OVERLAP("2");       // 可间断、可重叠
}
```

#### 4.2.1 模式一：不间断不重叠（模式 0）

**适用场景**：员工主任职经历——员工在任何时间点上只能有一个主任职岗位，且时间线必须连续。

```
时间线示意（不间断不重叠）：

记录1: |==========|                     startdate=2020-01-01, enddate=2022-06-30
记录2:            |==========|          startdate=2022-07-01, enddate=2024-12-31
记录3:                        |=======> startdate=2025-01-01, enddate=2999-12-31
       ───────────────────────────────────────────────────────> 时间轴
       无间断、无重叠
```

**约束规则**：

- 最后一条数据的失效日期强制为最大失效日期（2999-12-31），不可手动修改
- 新增时如果 `startDateLimit=true`，不可早于已有最早生效日期
- 新增/修改时如果 `cover=false`，不允许完全覆盖已有段（被引用的数据会校验引用关系）
- 保存时自动调整前后记录的日期保持连续（front/behind 算法，与历史模型版本变更计算逻辑类似）
- 修改时如果缩短了生效日期或失效日期，会自动调整相邻段的日期保持连续
- 删除中间段时，前一段的 enddate 调整为被删除段的 enddate（而非后一段 startdate - 1天）
- 删除后如果时间线最后一条数据的 enddate 不是 2999-12-31，会自动修正

**处理器**：`NoIntervalNoOverlapHandler`（最复杂的模式）

#### 4.2.2 模式二：可间断不重叠（模式 1）

**适用场景**：岗位兼任记录——同一员工在某个兼任岗位上可能有间断，但同一时间点上不能重复。

```
时间线示意（可间断不重叠）：

记录1: |==========|                     startdate=2020-01-01, enddate=2022-06-30
                        （间断）
记录2:                  |==========|    startdate=2023-03-01, enddate=2024-12-31
                                   （间断）
记录3:                                |===> startdate=2025-06-01, enddate=2999-12-31
       ───────────────────────────────────────────────────────> 时间轴
       允许间断、不允许重叠
```

**约束规则**：

- 允许时间段之间有间隔
- 不允许重叠——新增/修改时检查是否与已有段重叠，重叠则拒绝或调整
- 不强制最后一段 enddate 为 2999-12-31
- 也支持 front/behind 查找和日期调整逻辑（与模式 0 类似），但不做连续性校验
- 删除时直接删除，不需要调整相邻段日期

**处理器**：`IntervalAndNoOverlapHandler`

#### 4.2.3 模式三：可间断可重叠（模式 2）

**适用场景**：培训班期记录——同一员工可以同时参加多个培训班，时间段可以重叠。

```
时间线示意（可间断可重叠）：

记录1: |==========|
记录2:      |==========|      （与记录1部分重叠）
记录3:              |==========|
       ───────────────────────────────────────────────────────> 时间轴
       允许间断、允许重叠
```

**约束规则**：

- 仅做基本的日期合法性校验（startdate <= enddate）
- create/update/delete 都是直接操作，无需校验时间冲突，无需调整相邻段日期
- 最简单的模式

**处理器**：`IntervalAndOverlapHandler`

**三种处理器的构造参数差异**：

| 处理器                           | 构造参数                                                        | 说明                     |
| ----------------------------- | ----------------------------------------------------------- | ---------------------- |
| `NoIntervalNoOverlapHandler`  | `(entityNumber, entityConfig, fromOp, fromSDK, logEventId)` | 区分 OP/SDK 来源，支持日志事务 ID |
| `IntervalAndNoOverlapHandler` | `(entityNumber, entityConfig, fromOp, fromSDK, logEventId)` | 同上                     |
| `IntervalAndOverlapHandler`   | `(entityNumber, entityConfig, fromOp)`                      | 不区分 SDK 来源，不支持日志事务 ID  |

### 4.3 核心数据结构

时间轴模型通过继承 `hbp_bd_timelinemintpl` 模板为实体注入字段，定义在 `TimelineConstants` 中：

| 字段Key           | 常量名               | 数据库列         | 类型            | 说明                               |
| --------------- | ----------------- | ------------ | ------------- | -------------------------------- |
| `startdate`     | `START_DATE`      | `fstartdate` | DateField     | 时间段开始日期                          |
| `enddate`       | `END_DATE`        | `fenddate`   | DateField     | 时间段结束日期                          |
| `iscurrentdata` | `IS_CURRENT_DATA` | -            | CheckBoxField | 是否为当前生效的数据（由保存逻辑和定时任务维护）         |
| `isdeleted`     | `IS_DELETED`      | -            | CheckBoxField | 是否已逻辑删除（可选，由 `logicDelete` 配置控制） |

**与历史模型的关键区别**：

- 没有 `boid`（不区分 BO/Version）
- 没有 `datastatus`（没有暂存/待生效/已废弃状态流转）
- 使用 **逻辑主键（LogicKey）** 代替 `boid` 来定义一条时间线
- `iscurrentdata` 仅标识"当前日期落在哪条记录的时间段内"，与版本管理无关
- 记录**直接增删改**，没有暂存→生效的两阶段流程

### 4.4 模板继承体系

时间轴模型只有一个最小模板：

| 模板编码                    | 模板ID           | 说明        |
| ----------------------- | -------------- | --------- |
| `hbp_bd_timelinemintpl` | `4P3X7D6IF4T1` | HR时间轴最小模板 |

在 `biz-bos-ext` 中注册了 `TimeLineConfigAp` 控件：

```xml
<Style Id="TimeLineConfigAp" style="Control">
    <Id>TimeLineConfigAp</Id>
    <Name>时间轴配置1</Name>
    <PackageName>kd.bos.ext.hr.timeline.control</PackageName>
    <ExtendClassName>kd.bos.ext.hr.timeline.control.TimeLineConfigAp</ExtendClassName>
</Style>
```

判断实体是否继承时间轴模板：

```java
// SDK 方法
Boolean isTimeline = TimelineServiceHelper.isInheritTpl("hrpi_empposorgrel");
// 内部实现：检查实体元数据是否继承自 TIMELINE_TPL_ID("4P3X7D6IF4T1")
```

### 4.5 实体配置（TimelineEntityConf）

```java
@SdkPublic
public class TimelineEntityConf implements Serializable {
    Set<String> logicKey;                     // 逻辑主键字段集合
    TimelineModelTypeEnum modelTypeEnum;      // 模型类型（0/1/2）
    Boolean startDateLimit = false;           // 新增时生效日期是否可早于第一段
    Boolean cover = false;                    // 是否可以覆盖其他时间段
    Boolean enableSaveConfirm = true;         // 保存时是否提示二次确认
    Boolean needTimerDealCurrent = true;      // 是否需要定时任务处理 iscurrentdata
    Boolean logicDelete = false;              // 是否开启逻辑删除
}
```

> 注意：与 `HisModelEntityConfig` 不同，`TimelineEntityConf` 没有 `entityNumber` 字段。

**逻辑主键（LogicKey）** 是时间轴模型的核心概念：

- 逻辑主键定义了"哪些字段的组合构成一条时间线"
- 例如任职经历的逻辑主键为 `{employee}`，表示同一个员工的所有任职记录构成一条时间线
- 例如员工-岗位关系的逻辑主键为 `{employee, position}`，表示同一个员工在同一个岗位下的记录构成一条时间线
- 逻辑主键值通过遍历 logicKey 字段集合，拼接各字段值生成（多字段用 `_` 拼接）

**配置获取方式**：

```java
TimelineEntityConf conf = TimelineServiceHelper.queryTimelineEntityConf("hrpi_empposorgrel");
```

### 4.6 数据生命周期

```
┌──────────┐    createTimespan()     ┌──────────────┐
│  不存在   │ ──────────────────────→ │ 新增记录      │
└──────────┘                         │ startdate    │
                                     │ enddate      │
                                     │ iscurrentdata│
                                     └──────┬───────┘
                                            │
                     ┌──────────────────────┼──────────────────────┐
                     │                      │                      │
              updateTimespan()      deleteTimespan()        定时任务
                     │                      │             更新 iscurrentdata
                     ▼                      ▼                      │
              ┌──────────────┐      ┌──────────────┐               │
              │ 修改时间段    │      │ 删除记录      │               │
              │ 自动调整前后  │      │ 自动调整前后  │               ▼
              │ 记录的日期    │      │ 记录的日期    │      ┌──────────────┐
              │（模式0/1）    │      │（仅模式0）    │      │ iscurrentdata│
              └──────────────┘      └──────────────┘      │ = true/false │
                                                          └──────────────┘
```

与历史模型不同，时间轴模型的记录**直接增删改**，没有暂存→生效的两阶段流程。

**iscurrentdata 的维护机制**：

- 保存时由 `TimeLineServiceUtil.updateIsCurrentDataVal()` 方法自动维护
- 定时任务 `TimelineUpdateIsCurrentDataTask` 定期刷新（仅对 `needTimerDealCurrent=true` 的实体生效）
- 逻辑：当前日期在 `[startdate, enddate]` 范围内 → `true`，否则 → `false`

### 4.7 服务架构

```
┌───────────────────────────────────────────────────────────────┐
│                    SDK 层（对外暴露）                           │
│  TimelineServiceHelper                                        │
│  @SdkService("HR时间轴模型服务")                                │
└───────────────┬───────────────────────────────────────────────┘
                │
                ▼
┌───────────────────────────┐
│      TimelineService      │
│      (ITimelineService)   │
│      查询/配置/日志        │
└───────────────┬───────────┘
                │
                ▼
┌─────────────────────────────────────────┐
│        TimelineHandlerFactory           │
│   根据 TimelineModelTypeEnum 分派:       │
│   ├── NOINTERRUPTION_NOOVERLAP("0")     │
│   │   → NoIntervalNoOverlapHandler      │
│   ├── INTERRUPTION_NOOVERLAP("1")       │
│   │   → IntervalAndNoOverlapHandler     │
│   └── INTERRUPTION_OVERLAP("2")         │
│       → IntervalAndOverlapHandler       │
└─────────────┬──────────┬──────────┬─────┘
              │          │          │
              ▼          ▼          ▼
       ┌──────────┐ ┌──────────┐ ┌──────────┐
       │不间断不重叠│ │可间断不重叠│ │可间断可重叠│
       │(最复杂)   │ │(中等)     │ │(最简单)  │
       └──────────┘ └──────────┘ └──────────┘
```

**工厂分派逻辑**（`TimelineHandlerFactory`）：

```java
// 普通调用
ITimelineHandler handler = TimelineHandlerFactory.getTimelineHandler(entityNumber, entityConfig);
// 来自SDK调用
ITimelineHandler handler = TimelineHandlerFactory.getTimelineHandlerForSDK(entityNumber, entityConfig);
// 来自OP操作
ITimelineHandler handler = TimelineHandlerFactory.getTimelineHandlerForOp(entityNumber, entityConfig, logEventId);
```

**核心接口 ITimelineHandler**（提供 28 个方法）：

接口按操作类型分为 add、modify、save、delete 四组，每组提供多种变体：

| 操作     | 单条  | 单条(noDB) | 批量(跨时间线)    | 批量(noDB) | 同时间线批量                    | 同时间线(noDB) |
| ------ | --- | -------- | ----------- | -------- | ------------------------- | ---------- |
| add    | ✓   | ✓        | addBatch    | ✓        | addBatchInSameTimeline    | ✓          |
| modify | ✓   | ✓        | modifyBatch | ✓        | modifyBatchInSameTimeline | ✓          |
| save   | ✓   | ✓        | saveBatch   | ✓        | -                         | -          |
| delete | ✓   | ✓        | deleteBatch | ✓        | deleteBatchInSameTimeline | ✓          |

**noDB 模式**：当 `noDB=true` 时，仅模拟执行并返回数据变化情况，数据不会入库。适用于保存前的预检和二次确认提示。

**关键工具类 TimeLineServiceUtil**：

| 方法                                                          | 说明                                        |
| ----------------------------------------------------------- | ----------------------------------------- |
| `buildTimeLineDataGroups(entityNumber, entityConfig, objs)` | SDK 层构建时间线数据分组的入口（按 logicKey 分组 + 查询已有数据） |
| `setNewDataIds(timelineDataGroups)`                         | 为新增数据分配 ID                                |
| `removeDateHMS(dataList)`                                   | 去除日期的时分秒（精度为"天"级别）                        |
| `updateIsCurrentDataVal(timelineDys, removeList, saveList)` | 维护 iscurrentdata 字段值                      |
| `entityMetaBeReferenced(entityNumber)`                      | 判断实体是否被其他基础资料引用（影响删除校验性能）                 |
| `getMaxEffEndDate()`                                        | 获取系统最大失效日期（2999-12-31）                    |
| `addListNoRepeat(data, list)`                               | 去重添加到列表                                   |
| `writeLog(saveList, removeList, logEventId)`                | 写入时间轴操作日志                                 |
| `getStartDateAndEndDateDisplayName(entityType)`             | 获取生失效日期字段的显示名称（用于校验提示）                    |

### 4.8 校验器（TimelineCommonValidator）

`TimelineCommonValidator` 是时间轴模型中所有校验逻辑的核心类，被三种处理器共同使用：

#### SDK 层校验（在 TimelineServiceHelper 中调用）

| 方法                                                                  | 说明                                               |
| ------------------------------------------------------------------- | ------------------------------------------------ |
| `validateConfig(entityConfig)`                                      | 校验实体配置有效性（logicKey 不能为空、modelType 不能为空等）         |
| `validateData(entityConfig, objs, successObjs, failObjs, errorMsg)` | SDK 层数据校验（日期必填、日期范围、logicKey 值不为空等），将数据分为成功和失败两组 |

#### 处理器层校验（在 Handler 内部调用）

| 方法                                                                      | 说明                                 |
| ----------------------------------------------------------------------- | ---------------------------------- |
| `validateDataForAdd(data, dataList, timelineDys, entityConfig, ...)`    | 新增数据校验：日期范围、startDateLimit、cover 等 |
| `validateDataForModify(data, timelineDys, entityConfig, ...)`           | 修改数据校验：日期范围、logicKey 不可修改、时间冲突等    |
| `validateAddDataContinues(timelineDys, entityConfig, saveList, result)` | 不间断模式下新增数据的连续性校验                   |
| `validateCoverDataRef(data, coverDys, result)`                          | 覆盖时校验被覆盖数据是否被其他基础资料引用              |
| `validateDeleteDataRef(data, deleteDataList, result)`                   | 删除时校验数据是否被其他基础资料引用                 |

#### 基础校验方法

| 方法                                               | 说明                     |
| ------------------------------------------------ | ---------------------- |
| `validateStartDateMustInput(data, entityConfig)` | 生效日期必填校验               |
| `validateEndDateMustInput(data, entityConfig)`   | 失效日期必填校验               |
| `validateDateRange(data)`                        | startdate 不能大于 enddate |
| `validateEndDate(data)`                          | 失效日期合法性校验              |
| `validateLogicKeyValIsNull(data, entityConfig)`  | logicKey 字段值不能为空       |
| `validateLogicKeyModify(data, entityConfig)`     | logicKey 字段不可修改（修改时校验） |

### 4.9 SDK API（TimelineServiceHelper）

#### 4.9.1 查询类

```java
// 1. 判断实体是否继承时间轴模板
Boolean isTimeline = TimelineServiceHelper.isInheritTpl("hrpi_empposorgrel");

// 2. 批量判断多个实体是否继承时间轴模板
Map<String, Boolean> result = TimelineServiceHelper.isInheritTpls(entityNumberSet);

// 3. 获取实体配置
TimelineEntityConf conf = TimelineServiceHelper.queryTimelineEntityConf("hrpi_empposorgrel");

// 4. 获取系统最大失效日期
Date maxEndDate = TimelineServiceHelper.getSysMaxEffEndDate();

// 5. 按逻辑主键查询时间线数据
List<Map<String, Object>> keys = new ArrayList<>();
Map<String, Object> key = new HashMap<>();
key.put("employee_id", 10001L);
keys.add(key);
List<DynamicObject> timespans = TimelineServiceHelper.queryTimespanByLogicKey("hrpi_empposorgrel", keys);

// 6. 按QFilter查询
QFilter filter = new QFilter("startdate", QCP.less_equals, new Date())
    .and(new QFilter("enddate", QCP.large_equals, new Date()));
List<DynamicObject> timespans = TimelineServiceHelper.queryTimespanByQFilter("hrpi_empposorgrel", filter);
```

#### 4.9.2 新增

```java
// 单条新增
HrApiResponse<TimelineResultInfo> resp = TimelineServiceHelper.createTimespan("hrpi_empposorgrel", obj);

// 批量新增（简化返回）
HrApiResponse<TimelineResultInfo> resp = TimelineServiceHelper.createTimespans("hrpi_empposorgrel", objs);

// 批量新增（详细变化信息）
HrApiResponse<TimelineHandlerResult> resp = TimelineServiceHelper.createTimespansGetChangeInfo("hrpi_empposorgrel", objs);
```

#### 4.9.3 修改

```java
// 单条修改
HrApiResponse<TimelineResultInfo> resp = TimelineServiceHelper.updateTimespan("hrpi_empposorgrel", obj);

// 批量修改（简化返回）
HrApiResponse<TimelineResultInfo> resp = TimelineServiceHelper.updateTimespans("hrpi_empposorgrel", objs);

// 批量修改（详细变化信息）
HrApiResponse<TimelineHandlerResult> resp = TimelineServiceHelper.updateTimespansGetChangeInfo("hrpi_empposorgrel", objs);
```

#### 4.9.4 保存（新增或修改，根据是否有主键自动判断）

```java
// 批量保存（实际入库）
HrApiResponse<TimelineResultInfo> resp = TimelineServiceHelper.saveTimespans("hrpi_empposorgrel", objs);

// 批量保存（模拟执行，不入库）
HrApiResponse<TimelineResultInfo> resp = TimelineServiceHelper.saveTimespans("hrpi_empposorgrel", objs, true);

// 批量保存（详细变化信息，支持 noDB）
HrApiResponse<TimelineHandlerResult> resp = TimelineServiceHelper.saveTimespansGetChangeInfo("hrpi_empposorgrel", objs, false);
```

#### 4.9.5 删除

```java
// 单条删除
HrApiResponse<TimelineResultInfo> resp = TimelineServiceHelper.deleteTimespan("hrpi_empposorgrel", id);

// 批量删除（简化返回）
HrApiResponse<TimelineResultInfo> resp = TimelineServiceHelper.deleteTimespans("hrpi_empposorgrel", ids);

// 批量删除（详细变化信息）
HrApiResponse<TimelineHandlerResult> resp = TimelineServiceHelper.deleteTimespansGetChangeInfo("hrpi_empposorgrel", ids);

// 批量删除（详细变化信息 + noDB 模式）
HrApiResponse<TimelineHandlerResult> resp = TimelineServiceHelper.deleteTimespansGetChangeInfo("hrpi_empposorgrel", ids, true);
```

#### 4.9.6 SDK 返回结构说明

**简化返回 TimelineResultInfo**：

```java
public class TimelineResultInfo {
    boolean success;                          // 操作是否成功
    String message;                           // 错误/提示信息
    List<Long> successIds;                    // 成功的数据ID列表
    List<Long> failIds;                       // 失败的数据ID列表
    List<Long> idsOfAdd;                      // 新增的数据ID列表
    List<Long> idsOfModify;                   // 修改的数据ID列表
    List<Long> idsOfDel;                      // 删除的数据ID列表
    List<TimelineChangeIdInfo> changeIdInfoList; // 变更ID信息列表
    List<HisModelAttachInfo> attachmentBos;   // 附件信息
}
```

**详细返回 TimelineHandlerResult**：

```java
public class TimelineHandlerResult {
    List<DynamicObject> newOrModifyDataList;  // 新增或修改的数据集
    List<Long> removeIds;                     // 删除的数据ID集
    List<DynamicObject> failDataList;         // 校验失败的数据集
    List<String> errorMsgList;                // 错误信息列表
    List<DataChangeInfo> startDateChanges;    // 生效日期变化记录
    List<DataChangeInfo> endDateChanges;      // 失效日期变化记录
    List<DataChangeInfo> partitionChanges;    // 分割变化记录
    List<DataChangeInfo> covers;              // 被覆盖的记录
    List<HisModelAttachInfo> attachmentBos;   // 附件信息
}
```

---

## 5. 两种模型的对照分析

### 5.1 详细对比矩阵

| 维度         | 历史模型（HisModel）                                                   | 时间轴模型（Timeline）                                     |
| ---------- | ---------------------------------------------------------------- | --------------------------------------------------- |
| **管理对象**   | 主实体（BaseEntity）的不同时期版本                                           | 从属记录的不同时间段                                          |
| **时间字段**   | `bsed` / `bsled`                                                 | `startdate` / `enddate`                             |
| **逻辑主键**   | `boid`（业务对象ID，自动设置）                                              | `logicKey`（可配置的字段组合）                                |
| **当前标识**   | `iscurrentversion`（区分BO/版本）                                      | `iscurrentdata`（标识当前时间段）                            |
| **状态管理**   | 4状态流转（暂存→待生效→已生效→已废弃）                                            | 无状态流转（直接CRUD）                                       |
| **两阶段提交**  | 支持（暂存→生效）                                                        | 不支持（直接生效）                                           |
| **版本号**    | 自动分配（V0001, V0002...）                                            | 无版本号概念                                              |
| **BO数据回写** | 有（最新生效版本通过 HisVersionBackWriteService 回写BO记录）                    | 无（没有BO概念）                                           |
| **修订能力**   | 支持（reviseVersions, 不改时间区间）                                       | 通过modify实现                                          |
| **附件管理**   | 内置附件复制机制（HisModelAttachmentService + HisModelAttachRelationInfo） | 内置附件复制机制（TimelineAttachmentUtil + copyDataIdMap）    |
| **修订日志**   | 内置（hbp_hismodellog实体）                                            | 内置（TimelineLogHandler，hbp_timelinelog实体）            |
| **定时任务**   | 待生效→已生效切换 + BO数据同步（HisModelVersionEffectTask）                    | iscurrentdata 标识更新（TimelineUpdateIsCurrentDataTask） |
| **删除策略**   | 删除BO级联删除所有版本                                                     | 直接删除记录 + 调整前后日期（模式0）/ 直接删除（模式1/2）                   |
| **逻辑删除**   | 不支持                                                              | 可配置（logicDelete=true 时设 isdeleted=true 而非物理删除）      |
| **模式数量**   | 2种                                                               | 3种                                                  |
| **noDB模拟** | 不支持                                                              | 支持（所有操作都有 noDB 变体）                                  |
| **F7选择**   | 支持带版本的F7选择（业务F7/版本F7）                                            | 标准F7                                                |
| **引用关系校验** | 删除BO时校验                                                          | 删除/覆盖时校验（通过 entityMetaBeReferenced 优化性能）            |
| **工程位置**   | hbppro + bos-ext-hr                                              | hbppro + bos-ext-hr + hrpipro                       |

### 5.2 何时使用哪种模型？

| 场景特征         | 推荐模型      | 理由           |
| ------------ | --------- | ------------ |
| 基础资料需要追溯历史   | **历史模型**  | 需要BO+版本的双层结构 |
| 需要暂存→审核→生效流程 | **历史模型**  | 内置两阶段提交      |
| 需要F7选择时区分版本  | **历史模型**  | 内置历史F7支持     |
| 一对多的时间段记录    | **时间轴模型** | 天然的时间段CRUD   |
| 允许时间段间断或重叠   | **时间轴模型** | 支持3种灵活模式     |
| 需要模拟执行（noDB） | **时间轴模型** | 内置noDB模式     |
| 关联记录按时间线展示   | **时间轴模型** | 逻辑主键+时间排序    |

---

## 6. 典型场景

### 6.1 场景一：行政组织变更（历史模型·时序）

**业务背景**：行政组织 `haos_adminorghr` 在2025年7月进行组织架构调整，部门更名为"数字化研发中心"。

**数据变化**：

```
假设操作日期为 2025-04-10。

操作前——haos_adminorghr 表：
┌──────┬───────┬────────────────┬────────────┬────────────┬──────────┬───────────────┐
│  id  │ boid  │iscurrentversion│   bsed     │   bsled    │datastatus│    name       │
├──────┼───────┼────────────────┼────────────┼────────────┼──────────┼───────────────┤
│ 5001 │ 5001  │    true        │ 2020-01-01 │ 2999-12-31 │    1     │ 技术研发部     │  ← BO（由V1回写）
│ 6001 │ 5001  │    false       │ 2020-01-01 │ 2999-12-31 │    1     │ 技术研发部     │  ← V1（已生效）
└──────┴───────┴────────────────┴────────────┴────────────┴──────────┴───────────────┘

执行变更操作：
HisCreateVersionParam param = new HisCreateVersionParam();
param.setEntityNumber("haos_adminorghr");
DynamicObject newVersion = ...;  // 设置 name="数字化研发中心", bsed=2025-07-01
param.setDataList(Arrays.asList(newVersion));
HisModelServiceHelper.createDataVersions(param);

操作后——haos_adminorghr 表（当天=2025-04-10，V2的bsed=2025-07-01 > 当天，故为待生效）：
┌──────┬───────┬────────────────┬────────────┬────────────┬──────────┬───────────────────┐
│  id  │ boid  │iscurrentversion│   bsed     │   bsled    │datastatus│      name         │
├──────┼───────┼────────────────┼────────────┼────────────┼──────────┼───────────────────┤
│ 5001 │ 5001  │    true        │ 2020-01-01 │ 2025-06-30 │    1     │ 技术研发部         │  ← BO（V2未生效，BO仍由V1回写，bsed/bsled与V1一致）
│ 6001 │ 5001  │    false       │ 2020-01-01 │ 2025-06-30 │    1     │ 技术研发部         │  ← V1（已生效，bsled被调整）
│ 6002 │ 5001  │    false       │ 2025-07-01 │ 2999-12-31 │    0     │ 数字化研发中心      │  ← V2（待生效，bsed > 当天）
└──────┴───────┴────────────────┴────────────┴────────────┴──────────┴───────────────────┘

2025-07-01 定时任务执行后：
- V2 的 datastatus 从 "0"(待生效) 变为 "1"(已生效)
- BO 的 bsed/bsled/name 被 HisVersionBackWriteService 回写为 V2 的值：
  bsed=2025-07-01, bsled=2999-12-31, name="数字化研发中心"
- BO 的 sourcevid 被设为 V2 的 id
```

**过程中的关键调用链**：

1. `HisModelServiceHelper.createDataVersions()` → `HisModelAPIService.createDataVersions()`
2. → `HisModelServiceFactory.getServiceForAPI()` → 返回 `HisModelLineService`（时序模式）
3. → `HisModelLineService.createDataVersions()` 内部：
   - `HisModelGeneralService.setNewDataIds()` 设置新数据的 ID 和 BOID
   - `HisModelGeneralService.initEntryBoId()` 初始化分录 BOID
   - `HisModelGeneralService.buildHisBoIdDataGroup()` 按 BOID 分组
   - `HisCreateDataVersionHandler.handle()` 处理版本变更
   - `HisModelVersionChangeCalService.calVersionChange()` 计算 front/behind，调整 V1 的 bsled
   - `HisModelVersionNumberService` 分配版本号
   - `HisModelCommonUtil.writeReviseLog()` 写修订日志
   - 保存变更数据

### 6.2 场景二：员工主数据变更（历史模型·时序）

**业务背景**：员工 `hrpi_employee` 张三在2025年3月改名为"张三丰"。

```
假设操作日期为 2025-04-10（V2的bsed=2025-03-01 ≤ 当天，立即生效）。

操作前：
┌──────┬───────┬────────────────┬────────────┬────────────┬──────────┬──────┬───────────┐
│  id  │ boid  │iscurrentversion│   bsed     │   bsled    │datastatus│ name │ hisversion│
├──────┼───────┼────────────────┼────────────┼────────────┼──────────┼──────┼───────────┤
│ 1001 │ 1001  │    true        │ 2020-07-01 │ 2999-12-31 │    1     │ 张三 │           │  ← BO（由V1回写）
│ 2001 │ 1001  │    false       │ 2020-07-01 │ 2999-12-31 │    1     │ 张三 │ V0001     │  ← V1（已生效）
└──────┴───────┴────────────────┴────────────┴────────────┴──────────┴──────┴───────────┘

变更操作（name改为"张三丰"，生效日期=2025-03-01）后：
┌──────┬───────┬────────────────┬────────────┬────────────┬──────────┬──────┬───────────┐
│  id  │ boid  │iscurrentversion│   bsed     │   bsled    │datastatus│ name │ hisversion│
├──────┼───────┼────────────────┼────────────┼────────────┼──────────┼──────┼───────────┤
│ 1001 │ 1001  │    true        │ 2025-03-01 │ 2999-12-31 │    1     │ 张三丰│           │  ← BO（由V2回写，bsed/bsled/name与V2一致）
│ 2001 │ 1001  │    false       │ 2020-07-01 │ 2025-02-28 │    1     │ 张三 │ V0001     │  ← V1（已生效，bsled调整）
│ 2002 │ 1001  │    false       │ 2025-03-01 │ 2999-12-31 │    1     │ 张三丰│ V0002     │  ← V2（已生效，bsed ≤ 当天）
└──────┴───────┴────────────────┴────────────┴────────────┴──────────┴──────┴───────────┘

查询2024年12月的员工信息：
QFilter filter = new QFilter("boid", QCP.equals, 1001L)
    .and(new QFilter("iscurrentversion", QCP.equals, false))
    .and(new QFilter("bsed", QCP.less_equals, Date("2024-12-01")))
    .and(new QFilter("bsled", QCP.large_equals, Date("2024-12-01")))
    .and(new QFilter("datastatus", QCP.equals, "1"));
// 结果：返回 V1，name="张三"
```

### 6.3 场景三：任职经历管理（时间轴模型·不间断不重叠）

**业务背景**：员工张三的主任职经历变更。

```
逻辑主键：{employee}
模型类型：不间断不重叠（模式 0）

操作前——hrpi_empposorgrel 表：
┌──────┬──────────┬───────────────────┬────────────┬────────────┬──────────────┬──────────┐
│  id  │ employee │   adminorg        │ startdate  │  enddate   │iscurrentdata │ position │
├──────┼──────────┼───────────────────┼────────────┼────────────┼──────────────┼──────────┤
│ 3001 │ 张三(1001)│ 技术研发部(5001)   │ 2020-07-01 │ 2999-12-31 │    true      │ 开发工程师│
└──────┴──────────┴───────────────────┴────────────┴────────────┴──────────────┴──────────┘

调岗操作（转到产品部-产品经理，生效日期=2025-04-01）：
TimelineServiceHelper.saveTimespans("hrpi_empposorgrel", new DynamicObject[]{newRecord});

操作后：
┌──────┬──────────┬───────────────────┬────────────┬────────────┬──────────────┬──────────┐
│  id  │ employee │   adminorg        │ startdate  │  enddate   │iscurrentdata │ position │
├──────┼──────────┼───────────────────┼────────────┼────────────┼──────────────┼──────────┤
│ 3001 │ 张三(1001)│ 技术研发部(5001)   │ 2020-07-01 │ 2025-03-31 │    false     │ 开发工程师│
│ 3002 │ 张三(1001)│ 产品部(5002)      │ 2025-04-01 │ 2999-12-31 │    true      │ 产品经理 │
└──────┴──────────┴───────────────────┴────────────┴────────────┴──────────────┴──────────┘

关键行为：
- 旧记录(3001)的 enddate 自动调整为 2025-03-31（新记录startdate - 1天）
- 新记录(3002)的 enddate 设为 2999-12-31（最后一段必须到最大日期）
- iscurrentdata 自动更新（由 TimeLineServiceUtil.updateIsCurrentDataVal 处理）
```

**内部调用链**：

1. `TimelineServiceHelper.saveTimespans()` → `saveTimespansGetChangeInfo()`
2. → `TimelineCommonValidator.validateConfig()` 校验配置
3. → `TimelineCommonValidator.validateData()` 校验数据
4. → `TimeLineServiceUtil.buildTimeLineDataGroups()` 按 logicKey 分组 + 查询已有数据
5. → `TimelineHandlerFactory.getTimelineHandlerForSDK()` → 返回 `NoIntervalNoOverlapHandler`
6. → `handler.saveBatch()` → `newOrModifyData()` 内部：
   - `TimeLineServiceUtil.removeDateHMS()` 去除时分秒
   - `TimelineCommonValidator.validateDataForAdd()` 新增校验
   - 查找 front/behind，调整旧记录 enddate
   - `TimeLineServiceUtil.updateIsCurrentDataVal()` 维护 iscurrentdata
   - `TimelineAttachmentUtil.copyAttachments()` 处理附件
   - `TimeLineServiceUtil.writeLog()` 写日志
   - `helper.save()` 保存数据

### 6.4 场景四：岗位兼职记录（时间轴模型·可间断不重叠）

**业务背景**：员工张三在2020年兼任"培训导师"岗位，2022年中止，2024年又重新兼任。

```
逻辑主键：{employee, position}
模型类型：可间断不重叠（模式 1）

hrpi_empposorgrel（兼任类型）表数据：
┌──────┬──────────┬──────────┬────────────┬────────────┬──────────────┐
│  id  │ employee │ position │ startdate  │  enddate   │iscurrentdata │
├──────┼──────────┼──────────┼────────────┼────────────┼──────────────┤
│ 4001 │ 张三(1001)│培训导师   │ 2020-01-01 │ 2022-06-30 │    false     │
│ 4002 │ 张三(1001)│培训导师   │ 2024-01-01 │ 2999-12-31 │    true      │
└──────┴──────────┴──────────┴────────────┴────────────┴──────────────┘

说明：
- 2022-07-01 到 2023-12-31 之间存在间断（允许间断）
- 两条记录的时间段不重叠（不允许重叠）
- 新增一条与现有记录重叠的记录会被拒绝（或触发 front/behind 日期调整）
- 删除时直接删除，不需要调整相邻段日期（与模式0不同）
```

### 6.5 场景五：培训班期管理（时间轴模型·可间断可重叠）

**业务背景**：员工张三同时参加两个培训班，时间段有重叠。

```
逻辑主键：{employee}
模型类型：可间断可重叠（模式 2）

${ISV_FLAG}_training_record 表数据：
┌──────┬──────────┬───────────────┬────────────┬────────────┬──────────────┐
│  id  │ employee │ training_name │ startdate  │  enddate   │iscurrentdata │
├──────┼──────────┼───────────────┼────────────┼────────────┼──────────────┤
│ 7001 │ 张三(1001)│ Python进阶    │ 2025-03-01 │ 2025-04-30 │    true      │
│ 7002 │ 张三(1001)│ AI基础        │ 2025-03-15 │ 2025-05-15 │    true      │
└──────┴──────────┴───────────────┴────────────┴────────────┴──────────────┘

说明：
- 两条记录时间段重叠（2025-03-15 ~ 2025-04-30），模式允许
- IntervalAndOverlapHandler 处理时最简单——不做日期调整，直接保存
- 仅做基本的日期合法性校验（startdate <= enddate）
```

### 6.6 场景六：薪酬方案版本管理（历史模型·非时序）

**业务背景**：薪酬等级方案每年修订一次，只需保留"最新生效版本"+"历史版本"，不需要时间区间连续。

```
模式：仅一个有效版本（非时序历史）

hsas_salarygrade 表数据：
┌──────┬───────┬────────────────┬──────────┬────────────┬────────────────┐
│  id  │ boid  │iscurrentversion│datastatus│ hisversion │  方案名称       │
├──────┼───────┼────────────────┼──────────┼────────────┼────────────────┤
│ 8001 │ 8001  │    true        │    1     │            │ 2025年薪酬方案  │  ← BO（由V0003回写，方案名称与V0003一致）
│ 9001 │ 8001  │    false       │   -2     │ V0001      │ 2023年薪酬方案  │  ← 已废弃（被V0002替代）
│ 9002 │ 8001  │    false       │   -2     │ V0002      │ 2024年薪酬方案  │  ← 已废弃（被V0003替代）
│ 9003 │ 8001  │    false       │    1     │ V0003      │ 2025年薪酬方案  │  ← 唯一已生效版本
└──────┴───────┴────────────────┴──────────┴────────────┴────────────────┘

关键行为：
- 只有 V0003 是已生效状态，V0001/V0002 自动变为已废弃
- HisModelNonLineService 处理：
  - HisOnlyOneEffectVersionHandler 将旧的已生效版本标记为 DISCARDED
  - 新版本设为 EFFECTING
  - HisVersionBackWriteService 将 BO 数据回写为最新版本内容
```

---

## 7. 工程代码位置索引

### 7.1 biz-bos-ext 工程

| 文件                                          | 说明                                                                 |
| ------------------------------------------- | ------------------------------------------------------------------ |
| `bos-ext-hr/.../HR_ExtendModelTemplate.xml` | HR控件扩展注册（HisModelConfigAp、TimeLineConfigAp、HisModelBasedataField等） |
| `bos-ext-hr/.../HisModelBasedataField.java` | 带时序历史的基础资料字段控件                                                     |
| `bos-ext-hr/.../HisModelBasedataProp.java`  | 历史基础资料属性（selData、bsedField、effDateFieldType等）                      |
| `bos-ext-hr/.../HisModelConfigAp.java`      | 历史模型配置控件（表单设计器中配置模式选择等参数）                                          |
| `bos-ext-hr/.../TimeLineConfigAp.java`      | 时间轴配置控件                                                            |

### 7.2 hbppro 工程（HR基础平台）

#### 通用基础

| 目录/文件                                           | 说明                               |
| ----------------------------------------------- | -------------------------------- |
| `hrmp-hbp-common/.../api/HrApiResponse.java`    | SDK 通用返回结构                       |
| `hrmp-hbp-common/.../api/EnumResponseCode.java` | 返回码枚举（SUCCESS="200", FAIL="500"） |

#### 历史模型

| 目录/文件                                                                            | 说明                                                           |
| -------------------------------------------------------------------------------- | ------------------------------------------------------------ |
| `hrmp-hbp-common/.../history/HisModelConstants.java`                             | 历史模型全部常量定义                                                   |
| `hrmp-hbp-common/.../history/HisModelTypeEnum.java`                              | 2种模式枚举                                                       |
| `hrmp-hbp-common/.../history/HisModelDataStatusEnum.java`                        | 4种版本状态枚举                                                     |
| `hrmp-hbp-common/.../history/EntityInheritTypeEnum.java`                         | 实体继承类型枚举                                                     |
| `hrmp-hbp-common/.../history/HisModelEntityConfig.java`                          | 实体配置模型                                                       |
| `hrmp-hbp-common/.../history/HisModelVersionChangeBo.java`                       | 版本变更计算结果                                                     |
| `hrmp-hbp-common/.../history/param/*.java`                                       | SDK参数/返回值DTO                                                 |
| `hrmp-hbp-business/.../history/HisModelCommonService.java`                       | 公共服务（模板继承判断、配置读取，单例）                                         |
| `hrmp-hbp-business/.../history/core/IHisModelService.java`                       | 核心服务接口                                                       |
| `hrmp-hbp-business/.../history/core/HisModelServiceFactory.java`                 | 工厂类（按 modelType 分派）                                          |
| `hrmp-hbp-business/.../history/core/HisModelLineService.java`                    | 时序服务实现                                                       |
| `hrmp-hbp-business/.../history/core/HisModelNonLineService.java`                 | 非时序服务实现                                                      |
| `hrmp-hbp-business/.../history/core/HisModelGeneralService.java`                 | 通用服务（单例，ID设置、分组、状态更新、数据复制等）                                  |
| `hrmp-hbp-business/.../history/core/HisVersionBackWriteService.java`             | 版本回写BO服务（单例）                                                 |
| `hrmp-hbp-business/.../history/core/HisModelVersionChangeCalService.java`        | 版本变更计算（单例，front/behind算法）                                    |
| `hrmp-hbp-business/.../history/core/HisModelVersionNumberService.java`           | 版本号分配服务                                                      |
| `hrmp-hbp-business/.../history/core/HisModelReviseService.java`                  | 修订服务（单例）                                                     |
| `hrmp-hbp-business/.../history/core/HisModelAttachmentService.java`              | 附件服务（单例）                                                     |
| `hrmp-hbp-business/.../history/core/handler/HisCreateDataVersionHandler.java`    | 版本生效处理器                                                      |
| `hrmp-hbp-business/.../history/core/handler/HisOnlyOneEffectVersionHandler.java` | 非时序唯一版本处理器                                                   |
| `hrmp-hbp-business/.../history/core/handler/HisDeleteVersionHandler.java`        | 版本删除处理器                                                      |
| `hrmp-hbp-business/.../history/task/HisModelVersionEffectTask.java`              | 版本生效定时任务                                                     |
| `hrmp-hbp-business/.../history/validate/*.java`                                  | 校验服务（Api/Common/Line/NonLine/Revise/Init）                    |
| `hrmp-hbp-business/.../history/dao/HisModelCommonDao.java`                       | 数据访问层                                                        |
| `hrmp-hbp-business/.../history/util/*.java`                                      | 工具类（CommonUtil/CopyUtil/DataLogUtil/ImportUtil/ValidateUtil） |
| `hrmp-hbp-business/.../helper/history/HisModelServiceHelper.java`                | SDK入口                                                        |
| `hrmp-hbp-business/.../helper/history/HisModelAttachServiceHelper.java`          | 附件SDK                                                        |
| `hrmp-hbp-business/.../helper/history/HisModelInitServiceHelper.java`            | 初始化SDK                                                       |

#### 时间轴模型

| 目录/文件                                                                      | 说明                          |
| -------------------------------------------------------------------------- | --------------------------- |
| `hrmp-hbp-common/.../timeline/TimelineConstants.java`                      | 时间轴常量定义                     |
| `hrmp-hbp-common/.../timeline/TimelineModelTypeEnum.java`                  | 3种模式枚举                      |
| `hrmp-hbp-common/.../timeline/TimelineLogConstants.java`                   | 日志常量                        |
| `hrmp-hbp-common/.../timeline/api/TimelineResultInfo.java`                 | SDK简化返回结构                   |
| `hrmp-hbp-business/.../timeline/ITimelineService.java`                     | 查询服务接口                      |
| `hrmp-hbp-business/.../timeline/TimelineService.java`                      | 查询服务实现（单例）                  |
| `hrmp-hbp-business/.../timeline/ITimelineHandler.java`                     | 处理器接口（28个方法）                |
| `hrmp-hbp-business/.../timeline/TimelineHandlerFactory.java`               | 工厂类（按 modelType 分派）         |
| `hrmp-hbp-business/.../timeline/NoIntervalNoOverlapHandler.java`           | 不间断不重叠处理器（最复杂）              |
| `hrmp-hbp-business/.../timeline/IntervalAndNoOverlapHandler.java`          | 可间断不重叠处理器                   |
| `hrmp-hbp-business/.../timeline/IntervalAndOverlapHandler.java`            | 可间断可重叠处理器（最简单）              |
| `hrmp-hbp-business/.../timeline/dao/TimelineEntityConf.java`               | 实体配置模型                      |
| `hrmp-hbp-business/.../timeline/validator/TimelineCommonValidator.java`    | 通用校验器（配置校验、数据校验、引用关系校验）     |
| `hrmp-hbp-business/.../timeline/util/TimeLineServiceUtil.java`             | 核心工具类（分组、日期处理、ID分配、日志、引用检查） |
| `hrmp-hbp-business/.../timeline/util/TimelineAttachmentUtil.java`          | 附件处理工具                      |
| `hrmp-hbp-business/.../timeline/util/TimelineDataLogUtil.java`             | 数据日志工具                      |
| `hrmp-hbp-business/.../timeline/TimelineLogHandler.java`                   | 修订日志处理                      |
| `hrmp-hbp-business/.../timeline/task/TimelineUpdateIsCurrentDataTask.java` | iscurrentdata定时任务           |
| `hrmp-hbp-business/.../helper/timeline/TimelineServiceHelper.java`         | SDK入口                       |
| `hrmp-hbp-business/.../domain/model/timeline/TimelineDataGroup.java`       | 时间线数据分组模型                   |
| `hrmp-hbp-business/.../domain/model/timeline/TimelineHandlerResult.java`   | 处理器详细返回结果                   |

### 7.3 hrpipro 工程（人员信息）

| 文件                                                          | 说明                     |
| ----------------------------------------------------------- | ---------------------- |
| `.../timeline/IEmpPosOrgRelTimelineHandler.java`            | 任职经历时间轴处理器接口           |
| `.../timeline/dao/TimelineEntityConf.java`                  | hrpi自有的时间轴实体配置（与hbp独立） |
| `.../timeline/model/EmpPosOrgRelTimelineDataGroup.java`     | 任职经历时间线分组              |
| `.../timeline/validator/EmpPosOrgRelTimelineValidator.java` | 任职经历校验器                |

---

## 8. 最佳实践与注意事项

### 8.1 历史模型最佳实践

1. **查询BO数据用 iscurrentversion=true**：BO记录始终反映最新生效数据，适用于绝大多数业务查询场景。

2. **查询历史版本时排除暂存和废弃**：
   
   ```java
   QFilter filter = new QFilter("iscurrentversion", QCP.equals, false)
       .and(new QFilter("datastatus", QCP.in, HisModelDataStatusEnum.availableStatus()));
   // availableStatus() 返回 {"1", "0"}
   ```

3. **日期参数必须去除时分秒**：`bsed` 和 `bsled` 的比较精度为"天"级别，带时分秒的日期可能导致查不到数据。源码中通过 `HisModelCommonUtil.removeDateHMS()` 处理。

4. **使用系统最大失效日期常量**：不要硬编码 `2999-12-31`，应调用 `HisModelServiceHelper.getSysMaxEffEndDate()`。

5. **版本生效时不更新的字段**：配置 `noUpdateFields` 可以避免某些字段（如工号）在版本切换时被 `HisVersionBackWriteService` 覆盖。

6. **分录数据的 entryboid**：如果实体有分录，`HisModelGeneralService.initEntryBoId()` 会自动初始化分录的 `entryboid`，用于跨版本追踪同一条分录记录。

7. **回写排除字段**：版本回写 BO 时，固定排除 `masterid, sourcevid, firstbsed, hisversion, iscurrentversion, creator, creator_id, createtime`，加上 `noUpdateFields` 配置的字段。

8. **判断调用结果**：SDK 方法返回 `HrApiResponse<T>`，通过 `resp.isSuccess()` 判断成功，`resp.getErrorMessage()` 获取错误信息，`resp.getData()` 获取业务数据。

### 8.2 时间轴模型最佳实践

1. **正确定义逻辑主键**：逻辑主键决定了哪些记录属于同一条时间线。定义过宽会导致不同业务场景的数据混在一条时间线上。

2. **选择合适的模式**：
   
   - 主任职必须连续 → 不间断不重叠（模式 0）
   - 兼任可以间断 → 可间断不重叠（模式 1）
   - 培训/项目可以重叠 → 可间断可重叠（模式 2）

3. **利用 noDB 模式做预检**：在实际保存前调用 `saveTimespans(entity, objs, true)` 可以获取数据变化预览，用于二次确认提示。

4. **注意 iscurrentdata 的更新时机**：`iscurrentdata` 由保存逻辑（`TimeLineServiceUtil.updateIsCurrentDataVal`）和定时任务（`TimelineUpdateIsCurrentDataTask`）共同维护。如果配置了 `needTimerDealCurrent=false`，需要业务代码自行维护。

5. **删除前校验引用关系**：删除时间段数据时，处理器会自动通过 `TimelineCommonValidator.validateDeleteDataRef()` 校验引用关系。开启逻辑删除（`logicDelete=true`）时跳过引用校验。

6. **引用关系校验的性能优化**：处理器内部会先调用 `TimeLineServiceUtil.entityMetaBeReferenced()` 判断实体是否被其他基础资料引用，如果没有则跳过后续的逐条引用校验，提升批量操作性能。

7. **日期精度**：所有处理器在操作前都会调用 `TimeLineServiceUtil.removeDateHMS()` 去除时分秒，确保日期比较精度为"天"级别。

8. **模式 0 删除的日期调整逻辑**：删除中间段时，前一段的 enddate 调整为被删除段的 enddate（不是后一段 startdate - 1天）。删除后如果最后一段的 enddate 不是 2999-12-31，会自动修正。

9. **模式 1 也有 front/behind 逻辑**：可间断不重叠模式并非简单的"重叠检查+直接保存"，它也有 front/behind 查找和日期调整逻辑（与模式 0 类似），只是不强制最后一段 enddate 为 2999-12-31，也不做连续性校验。

10. **GetChangeInfo 方法**：如果需要获取操作后的详细数据变化（哪些记录被新增/修改/删除/覆盖），使用 `xxxGetChangeInfo` 系列方法，返回 `TimelineHandlerResult`。

### 8.3 两种模型的协同使用

在实际 HCM 系统中，历史模型和时间轴模型经常**协同使用**：

```
                     ┌──────────────────────────────────────┐
                     │         员工（hrpi_employee）          │
                     │         历史模型·时序                  │
                     │         boid + bsed/bsled             │
                     └───────────┬──────────────────────────┘
                                 │ 1:N
                     ┌───────────▼──────────────────────────┐
                     │    任职经历（hrpi_empposorgrel）       │
                     │    时间轴模型·不间断不重叠              │
                     │    logicKey={employee}                │
                     │    startdate + enddate                │
                     └──────────────────────────────────────┘
```

- **员工主数据**用历史模型管理（改名、改证件等变更产生新版本）
- **任职经历**用时间轴模型管理（调岗、兼任等产生新的时间段记录）
- 两者通过 `employee` 字段关联

在查询"某个时间点上，某员工在哪个组织的什么岗位上，姓名是什么"时，需要：

1. 用历史模型查员工在该时间点的版本 → 获取姓名等属性
2. 用时间轴模型查该时间点的任职经历 → 获取组织、岗位信息

```java
// 查询2024年6月1日的员工信息 + 任职经历
Date queryDate = DateUtils.parseDate("2024-06-01");

// 1. 查历史版本
QFilter hisFilter = new QFilter("boid", QCP.equals, employeeBoId)
    .and(new QFilter("iscurrentversion", QCP.equals, false))
    .and(new QFilter("bsed", QCP.less_equals, queryDate))
    .and(new QFilter("bsled", QCP.large_equals, queryDate))
    .and(new QFilter("datastatus", QCP.equals, "1"));
DynamicObject empVersion = helper.loadSingle("hrpi_employee", hisFilter);

// 2. 查时间轴
QFilter timelineFilter = new QFilter("employee", QCP.equals, employeeBoId)
    .and(new QFilter("startdate", QCP.less_equals, queryDate))
    .and(new QFilter("enddate", QCP.large_equals, queryDate));
List<DynamicObject> posOrgRels = TimelineServiceHelper.queryTimespanByQFilter("hrpi_empposorgrel", timelineFilter);
```

---

> **文档完成**。本文档基于 `biz-bos-ext`、`hbppro`、`hrpipro` 三个工程的源码分析，完整阐述了历史模型和时间轴模型的设计原理、数据结构、服务架构、SDK API 以及典型业务场景。相比初版，本次修订补充了通用基础设施（HrApiResponse）、通用服务（HisModelGeneralService）、版本回写服务（HisVersionBackWriteService）、校验器（TimelineCommonValidator）、工具类（TimeLineServiceUtil）等关键内容，修正了接口方法数量、处理器构造参数差异、删除逻辑等描述。
