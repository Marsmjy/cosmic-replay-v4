# 业务流转 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于 6 反编译类 + plugin_registry + HisModel 框架分析
> **confidence**: verified
> **审计时间**: 2026-04-27

---

## 1. 核心操作链路（时序资料特有）

本场景是 HisModel 时序资料，其业务流转与普通基础资料有本质区别：
- **新建**：创建第一个版本，boid = id，iscurrentversion = true
- **修改（modify）**：直接编辑当前版本（小幅改动，不产生新版本）
- **变更（change/confirmchange）**：产生新历史版本，旧版本 iscurrentversion 变 false
- **禁用/启用**：整个 boid 链级别的状态变更（影响所有版本）

---

## 2. 新建标准岗位流程

```
用户点"新增"
    │
    ├── StandardPositionEdit.preOpenForm
    │     └── 设置标题"标准岗位-新增"
    │         若父表单是 hbpm_stposition，则标题设为"标准岗位-变更"
    │
    ├── StandardPositionEdit.afterBindData
    │     ├── buttonVisibleAfterBindData（按钮可见性）
    │     ├── 清空 org 字段（新增时防带入历史值）
    │     └── 若编码规则存在：自动生成 number（PositionCodeRuleHelper.readNumber）
    │
    ├── 用户填写基本信息
    │     ├── name（必填）
    │     ├── number（可自填，或编码规则自动生成）
    │     ├── org（必填 · 触发 propertyChanged → 重新生成编码）
    │     ├── adminorg（必填 · 触发 propertyChanged → org 自动联动）
    │     └── job / positiontype / 职等范围等业务字段
    │
    ├── 用户点"保存"
    │     │
    │     ├── StandardPositionEdit.beforeDoOperation
    │     │     └── 无信息变更时提示"无信息变更，请确认"并阻断
    │     │
    │     ├── HisModelOPCommonPlugin.onAddValidators（时序校验）
    │     ├── HisUniqueValidateOp.onAddValidators（boid 唯一性）
    │     ├── HRBaseDataStatusOp.onAddValidators（状态校验）
    │     ├── StandardPositionSaveOp.onAddValidators（空方法）
    │     │
    │     ├── HisModelOPCommonPlugin.beforeExecuteOperationTransaction
    │     │     └── 分配 boid / 设置版本信息 / iscurrentversion=true
    │     ├── StandardPositionSaveOp.beforeExecuteOperationTransaction
    │     │     ├── super.beforeExecuteOperationTransaction（调 MsgHandleOp）
    │     │     └── entity.set("isstandardpos", "1")（强制标记为标准岗位）
    │     │
    │     └── 保存成功 → StandardPositionEdit.afterDoOperation
    │           └── 隐藏 insertdatabtn / hisversionbtn / bsed 控件
    │
    └── StandardPositionEdit.pageRelease（用户关闭但未保存）
          └── 自动回收编码（recycleNumber）
```

---

## 3. 变更标准岗位流程（产生新历史版本）

```
用户在详情页点"变更"操作
    │
    ├── StandardPositionEdit.beforeDoOperation（operateKey="change"）
    │     ├── adminorg 字段设为可编辑
    │     └── 显示 bsed（生效日期）控件
    │
    ├── 用户设置新生效日期（bsed）并修改需要变更的字段
    │
    ├── 用户点"确认变更"（confirmchange 或 confirmchangenoaudit）
    │     │
    │     ├── StandardPositionEdit.beforeDoOperation
    │     │     └── 若无信息变更：提示并阻断（带缓存控制防二次触发）
    │     │
    │     ├── StandardPositionChangeOp.onAddValidators（空方法）
    │     ├── HisModelOPCommonPlugin（时序版本管理）
    │     │     └── 验证新旧版本时间区间不重叠
    │     │
    │     ├── HisModelOPCommonPlugin.beforeExecuteOperationTransaction
    │     │     ├── 新记录：boid = 旧记录 boid · id = 新 id
    │     │     ├── 新记录：sourcevid = 旧记录 id · hisversion++
    │     │     ├── 新记录：iscurrentversion = true
    │     │     └── 旧记录：iscurrentversion = false
    │     │
    │     └── StandardPositionMsgHandleOp（父类）afterExecuteOperationTransaction
    │           └── 可能发布 BEC 消息通知下游（类名推断 · 未实证）
    │
    └── StandardPositionEdit.afterDoOperation
          └── 操作成功后隐藏变更相关按钮
```

---

## 4. 禁用流程

```
用户在列表或详情选中标准岗位 → 点"禁用"
    │
    ├── HRBaseDataStatusOp.onAddValidators（平台通用禁用校验）
    ├── StandardPositionDisableOp.onAddValidators（空方法）
    │
    ├── HisModelOPCommonPlugin.beforeExecuteOperationTransaction
    │     └── 处理时序版本级禁用
    │
    └── StandardPositionMsgHandleOp.afterExecuteOperationTransaction（推断）
          └── 通知下游岗位状态变化
```

---

## 5. 状态机

```
                     [新增 save]
  -----------------------------------------------------------
  ↓                                                         ↑
初始态(enable=0, status=A)                           [撤销/删除]
  │
  ├─[配置审批流: 提交] → 审批中(status=B) → [审核] → 生效(status=C)
  │
  └─[不配审批流: 直接 save] → 生效(enable=1)
          │
          ├─[change] → 产生新版本(iscurrentversion=true) 旧版 iscurrentversion=false
          │
          └─[disable] → 禁用(enable=10)
                │
                └─[enable] → 重新启用(enable=1)
```

---

## 6. 编码规则生命周期（独特逻辑）

```
用户进入新增页
    │
    ├── afterBindData: 若无 recycleCache 且 codeRuleExist() → setPositionNumber()
    │     └── 调 CodeRuleServiceHelper.readNumber("hbpm_stposition", entity, orgId)
    │         获取编码 → 存入 recycleCache
    │
    ├── 用户改 org / adminorg / job
    │     └── propertyChanged → 若字段在 getNumberRuleAllFieldSet() 中
    │           └── changeNumber() → recycleNumber() + setPositionNumber()（重新生成）
    │
    └── 用户放弃（pageRelease · 新增状态且非变更页）
          └── recycleNumber() → PositionCodeRuleHelper.recycleNumber() 归还编码
```

---

## 7. 版本历史查询流程

```
用户在详情页点"历史版本"按钮（hisversionbtn）
    │
    └── 打开历史版本列表（按 boid 过滤）
          └── HisModelListCommonPlugin 接管 · 展示该 boid 所有历史版本
                └── HisModelFormCommonPlugin 打开某历史版本详情（只读）
```

---

## 8. F7 选标准岗位流程（跨场景）

```
其他场景字段（如 hbpm_positionhr.stposition）F7 选值
    │
    ├── HisBaseDataF7FastFilter 接管
    ├── PositionBaseDataF7FastFilter 接管（岗位专属过滤器）
    └── HisModelF7ListPlugin
          ├── beforeBindData：加 iscurrentversion=true 过滤 + 生效日期范围过滤
          ├── beforePackageData：组装 F7 列表数据
          └── 用户选定 → 返回 boid 给调用方（PR-009）
```

---

## 9. 与 hbpm_positionhr 的对比分析（标准场景分析）

| 维度 | hbpm_stposition（本场景）| hbpm_positionhr（岗位信息）|
|---|---|---|
| 物理表 | t_hbpm_position（共用）| t_hbpm_position（共用）|
| isstandardpos | "1"（StandardPositionSaveOp 强制写）| "0"（普通岗位）|
| 时序模型 | HisModel（boid/iscurrentversion）| HisModel（同模板继承）|
| 与本场景关系 | 被引用方 | 引用方（stposition=本场景 boid）|
| 插件数量 | 37 个（最多）| 类似数量（未统计）|
| 变更操作 | change 产生新版本 | change 产生新版本 |
| BEC | StandardPositionMsgHandleOp 可能发（likely）| 可能发 positionhr 变更事件 |

---

## 10. 关联文档

- `02_business_rules.md` · INV-SP-06 ~ 12 前端联动规则
- `05_data_flow.md` · 时序数据写入流
- `06_customization_solutions.md` · CS-03 字段联动 · CS-05 BEC 扩展
