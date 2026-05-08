# core_hr_org_unit_transfer · 推荐定制方案

> **状态**: 🟢 基于 dcs_clean v1.5 真代码 + docx §3.2 + case_001 反哺
> **资产复刻**：[`_assets/org_unit_transfer/`](../../_assets/org_unit_transfer/)·一键 assemble
> **代码示例**：本节代码使用 `${ISV_FLAG}` 占位符代表客户开发商标识·部署到客户时全局替换为客户实际值（如 bjss / cds0 等）·详见 `feedback_isv_developer_flag_per_project.md`

---

## CS-01 · 完整资产复刻（最高频路径 ⭐）

### 业务背景

ISV 实施工程师拿到客户需求"我要做成建制划转"·客户提供：
- 开发商标识（如 `bjss`）
- ISV 应用编码（如 `bjss_hdm_ext`）

**预期产出**：完整工程包 (5 java + 7 datamodel + 6 工程文件 + 部署 SOP) 直接落地客户环境。

### 推荐方案

```bash
# 1 行命令出工程
python scripts/assemble_asset.py \
    --asset org_unit_transfer \
    --isv-flag bjss \
    --biz-app bjss_hdm_ext \
    --output D:/myproject/org_unit_transfer-bjss/
```

产物 21 文件：
- `code/bjss_hdm_ext/src/main/java/bjss/hr/hdm/orgtransfer/business/{AbstractCommonTask, OrgParentChgTask, OrgRenameChgTask, PostRenameChgTask, OrgTransferHelper}.java`
- `datamodel/hdm_ext/1.5.0/main/bjss_hdm_ext/{*.app, *.appx, *.xml, metadata/{bjss_orguntitransfer.dym, .dymx, schedule_*.schdata}}`
- `build.gradle / settings.gradle / config.gradle / cosmic.json / cosmic.properties / gradle.properties`
- `README.md / deploy_sop.md / customization_points.md`

### 部署 SOP（详见 [`_assets/org_unit_transfer/deploy_sop.md`](../../_assets/org_unit_transfer/deploy_sop.md)）

6 步法（按 docx §4.1.2）：
1. assemble 资产
2. 检查目标 BizappId 是否被现场扩展
3. 替换 BizappId 真值（dym 内 `<BizappId>` 标签）
4. MC 升级补丁导入 dm 元数据
5. mservice 配 CUSLIBS / libs 环境变量 + 重启
6. 配调度计划 + 启用 3 子任务

---

## CS-02 · 改变动场景编码集

**业务背景**：客户业务侧定义新的变动场景（如 "ZZCJ06 行政区划撤销"）·想让成建制划转任务也消费此场景。

### 扩展点

3 个调度任务子类的常量定义（详见 [`_assets/org_unit_transfer/customization_points.md`](../../_assets/org_unit_transfer/customization_points.md) EP-01）：

```java
// ${ISV_FLAG}.hr.hdm.orgtransfer.business.OrgParentChgTask:28
private static final Set<String> CHANGE_SCENE_ORG_PARENT_NUMBER =
    Sets.newHashSet("1020_S", "ZZCJ06");  // ⚡ 加客户自定义场景

// 同理 OrgRenameChgTask:33 / PostRenameChgTask:28
```

### 调用链

修改后·下次调度自动消费新场景·无需重启。但**新编码必须在 `haos_changescene` 表里真实存在**·否则查不到数据。

### 踩坑

- ❌ 编码集**只能加·不要删**标品默认值（1020_S/1030_S/1080_S）·删了会漏现有变更
- ❌ 自定义编码必须用 ISV 前缀（如 `${ISV_FLAG}_CUSTOM_01`）·防跟标品命名空间冲突

### 关联 PR

- PR-008 / PR-009 时序资料规范（changescene 是 HisModel 时序）

---

## CS-03 · 加 ISV 中间表自定义字段

**业务背景**：客户业务方要求在 `${ISV_FLAG}_orguntitransfer` 加自定义字段（如"审批意见 `${ISV_FLAG}_approval_note`"）。

### 扩展点

3 处联动（详见 [`_assets/org_unit_transfer/customization_points.md`](../../_assets/org_unit_transfer/customization_points.md) EP-03）：

#### a) datamodel/.../${ISV_FLAG}_orguntitransfer.dym

加字段定义：
```xml
<TextField id="${ISV_FLAG}_approval_note" length="500" required="false">
    <Name lcid="zh_CN" value="审批意见"/>
</TextField>
```

#### b) `OrgTransferHelper.java:357` BusinessDataServiceHelper.load 字段列表

```java
return BusinessDataServiceHelper.load("${ISV_FLAG}_orguntitransfer",
    "${ISV_FLAG}_takeeffectdate,${ISV_FLAG}_orgid,${ISV_FLAG}_orgnumber,${ISV_FLAG}_orgname," +
    "${ISV_FLAG}_executestatus,${ISV_FLAG}_resultmsg,${ISV_FLAG}_resultmsg_tag," +
    "${ISV_FLAG}_approval_note",  // ⚡ 加字段
    new QFilter[]{qFilter}, "${ISV_FLAG}_takeeffectdate asc,${ISV_FLAG}_modifydate asc");
```

#### c) 业务代码读写新字段（按需）

```java
String note = orgTran.getString("${ISV_FLAG}_approval_note");
```

### 踩坑

- ⚠️ 改 dym 后必须**重打 dm 包**·MC 升级·不能只改运行时
- ⚠️ load 字段列表漏改 → 业务代码读到 null · 不报错 · 隐蔽 bug

---

## CS-04 · 添加新 changeType（D / E / ...）

**业务背景**：客户想加一个新的变动类型（如 D = "部门撤销"）·走类似的批量处理。

### 扩展点

5 步走（详见 [`_assets/org_unit_transfer/customization_points.md`](../../_assets/org_unit_transfer/customization_points.md) EP-04）：

1. 新建 java 子类 `${ISV_FLAG}.hr.hdm.orgtransfer.business.DeptRevokeChgTask`·继承 `AbstractCommonTask`
2. 常量定义：
   ```java
   private static final String DEPT_REVOKE_CHG_TYPE = "D";
   private static final Set<String> CHANGE_SCENE_DEPT_REVOKE_NUMBER = Sets.newHashSet("ZZCJ-DEPT-DEL");
   ```
3. 覆盖 `execute()` + `handleOrgTransfer()`·参考 OrgParentChgTask（A）的骨架
4. 加 schdata 调度配置·新加任务节点
5. 加 docx 关键码值表·在 `${ISV_FLAG}_changetype` 字典里加"D=部门撤销"

### 调用链

新 task 走相同 4 步骤模式（见 02_business_rules.md R-03）·只是数据源 + 过滤集换。

### 踩坑

- ⚠️ 类名 PascalCase·**不要带客户前缀**（包前缀已经带了）
- ⚠️ schdata 改完必须重导 dm 包

---

## CS-05 · 改数据源（用客户自建消息表）

**业务背景**：客户有自己的"组织变更通知"实体（如客户 ESB 推送的 `${ISV_FLAG}_my_org_change`）·不想用标品 `homs_orgchgrecord`。

### 扩展点

修改 `OrgTransferHelper.java:32-65` 的 `queryHAOSChangeMsg`（详见 [`_assets/org_unit_transfer/customization_points.md`](../../_assets/org_unit_transfer/customization_points.md) EP-06）：

```java
public static DynamicObject[] queryHAOSChangeMsg(Map<String, Object> taskDays, Set<String> changeSceneSet) {
    HRBaseServiceHelper helper = new HRBaseServiceHelper("${ISV_FLAG}_my_org_change");  // ⚡ 改 form 名
    QFilter changeSceneFilter = new QFilter("orgchgentry.changescene.number", QCP.in, changeSceneSet);
    return helper.query("...", new QFilter[]{...});
}
```

### 踩坑

- ⚠️ 自建消息表必须有跟 `homs_orgchgrecord` 相同的字段结构（`orgchgentry.chgeffecttime` 等）
- ⚠️ 否则 `getOrgTransferDyList` 转换逻辑会失败

---

## CS-06 · 改调度执行天数

**业务背景**：默认 `EXECUTE_DAYS = 1`·客户想跑 7 天补漏单。

### 扩展点

`AbstractCommonTask.java:48`：
```java
private static final int EXECUTE_DAYS = 7;  // 默认 1 改 7
```

### 踩坑

- ⚠️ 改大值会增加查询数据量·建议 ≤ 30
- ⚠️ 跑批历史数据·可能要清掉之前漏处理的数据

---

## ❌ 反指引（不要在本场景做）

| 反模式 | 为什么不应该 |
|---|---|
| ❌ 私改 `hrpi_empposorgrel` 等任职经历底表 | 旧 hbis 客户版做法·已被本资产架构淘汰·改用标品 `submiteffect` |
| ❌ 跨服务自发 MQ 通知下游 | 旧客户版的 `ChgRecordUtil.packageMsg` 反模式·标品 op 已自带协同 |
| ❌ 在子任务里写跨云调用 SendEmailService 发邮件 | 应交给标品 op 处理·或加独立 BEC 订阅 |
| ❌ 类名带具体客户标识（如 `BjssOrgParentChgTask`）| 包前缀已带 `${ISV_FLAG}.`·类名不该再重复 |

详见 [`_antipatterns.json`](../../_antipatterns.json) AP-01x 系列。

---

## 关联 PR 红线

- **PR-001** ISV 并列挂不继承（本资产 5 类全继承 SDK 白名单父类·0 内部 API） ✅
- **PR-005** `kd.bos.id.ID` 生成主键（本资产用 CodeRuleServiceHelper·更高级） ✅
- **PR-008/009** 时序资料 HisModel（haos_adminorgdetail / hbpm_positionhr） ✅
- **PR-010** OP 13 个生命周期方法·调度任务用 `execute()` 入口 ✅

详见 [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json)
