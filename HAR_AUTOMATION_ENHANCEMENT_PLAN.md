# 金蝶云 HAR 全流程自动化增强方案

## 目标

把 cosmic-replay 从“HAR 录制回放工具”升级为“金蝶云协议级自动化平台”：

1. 新 HAR 导入前能自动判断质量和风险。
2. YAML 生成能稳定处理变量、环境字段、上下文、PageId。
3. 执行失败后能自动归因并给出修复方向。
4. 新业务类型通过插件/规则扩展，不破坏已成功用例。

## 已完成：第一阶段

### 1. 导入质量评分

入口：`/api/har/preview` 返回 `preview.quality`。

评分维度：

| 维度 | 权重 | 检查内容 |
|---|---:|---|
| 主链路 | 35 | 主表单、核心步骤、保存/提交/流程确认动作 |
| 变量 | 25 | 编号、名称、手机号、证件号、邮箱等唯一字段是否变量化 |
| 环境字段 | 25 | 组织、法人、职位、国家、工作地、树焦点等是否进入配置 |
| 兼容性 | 15 | 未知 ac、步骤过长、新组件风险 |

输出字段：

```yaml
quality:
  score: 100
  grade: A
  blocking: false
  summary: A 级 / 100 分：结构完整，适合直接生成并执行。
  dimensions: [...]
  issues: [...]
  checks: {...}
```

### 2. 执行失败自动归因

入口：runner 失败时推送 `failure_analysis` SSE 事件，并写入 run_history。

当前分类：

| category | 含义 | 典型错误 |
|---|---|---|
| `transient_protocol` | 协议/网关瞬态 | HTTP 502/503/504、超时 |
| `navigation_service_unavailable` | 非主导航表单不可达 | homs_apphome 服务 1002 |
| `environment_service_unavailable` | 主业务服务不可达 | 主表单 AppIdName 不存在 |
| `pageid_context` | PageId/上下文失效 | 页面未初始化或过期 |
| `business_missing_required` | 必填缺失 | 请填写/请选择/不能为空 |
| `business_duplicate` | 唯一字段重复 | 已存在/重复 |
| `business_invalid_value` | 字段值不合法 | 格式错误/不允许/超出 |
| `assertion_anchor_missing` | 断言挂靠步骤未执行到 | 找不到步骤 |

### 3. 前端可视化

已在导入向导增加“导入质量评估”卡片：

1. 展示总分、等级、是否建议确认后生成。
2. 展示主链路、变量、环境字段、兼容性四维分。
3. 展示前 5 个风险项和修复建议。
4. 预览步骤列表展示 `optional` 标识。

已在用例运行结果页增加“自动归因”卡片：

1. 展示失败分类、置信度、根因。
2. 展示证据错误文本。
3. 展示推荐处理动作。

## 已完成：第二阶段

### 1. 环境字段自动解析中心

目标：减少手工配置 `pick_fields.value_id`。

已实现：

1. `FieldResolver.resolve_basedata_result()` 返回结构化结果：`resolved / ambiguous / not_found / error / skipped`。
2. 支持金蝶两类候选数据形态：`rows + dataindex`、`setLookUpListValue(data + columns)`。
3. `pick_fields` 统一输出 `form_id/app_id/auto_resolve/resolve_status`，导入和执行共用同一套字段元信息。
4. 新增 `/api/env-fields/resolve`，Web UI 可在导入期“一键解析当前环境”。
5. 导入期解析会使用 HAR 中同表单首个 `loadData` 预热 pageId 上下文，避免“页面未初始化或者已经过期”。
6. 运行期解析只在 `resolved` 时覆盖 `value_id`；歧义、未找到、异常均保留原值并暴露诊断信息。
7. 成功解析结果写入本地环境缓存 `data/env_field_cache.json`，该文件不进入版本管理。

验证：

1. 岗位 HAR 的 `adminorg/changedesc/positiontype` 可在线精确解析。
2. 8 个基准 HAR 离线 YAML 生成均通过。
3. 19 条单元回归通过。

## 已完成：第三阶段

### 1. 组件插件化

目标：遇到新 HAR 类型时，不继续堆 if/else。

已实现第一步：组件处理器注册表和组件雷达。

抽象：

```text
ComponentHandler
  classify(step)
  handler_id
  component
  category
  support_level: supported / partial / unsupported
  risk
  suggestion
```

入口：

1. `lib/component_registry.py`：组件处理器注册表。
2. `preview.components`：组件覆盖率报告。
3. `preview.steps[*].component`：每个 step 的组件标签。
4. `quality.checks.component_*`：质量评分中的组件覆盖指标。
5. Web UI “组件雷达”：展示覆盖率、主要组件和未覆盖步骤。

当前已登记组件：

1. 基础资料选择器。
2. 字段更新。
3. 表单打开/加载。
4. 树控件。
5. 列表桥接导航。
6. 门户/应用导航。
7. 弹窗确认。
8. 单据体/分录行。
9. 保存/提交/审核。
10. 新增/修改态。
11. 国家、省市、电话区号级联。
12. 后台任务/侧边栏。
13. 用户偏好/首页设置。
14. 业务模型结构操作。
15. 通用低风险动作。

验证：

1. 8 个基准 HAR 的组件雷达均为 `unsupported=0`。
2. 组件注册表、preview、质量评分回归共 23 条测试通过。
3. 该阶段只做诊断和标记，不改变 YAML 生成行为，兼容历史成功用例。

## 第四阶段建议

### 1. 自动修复闭环

目标：失败后不只是提示，还能生成补丁。

流程：

```text
run_history -> failure_analysis -> advisor -> patch proposal -> user confirm -> update YAML -> rerun
```

建议先支持三类高频补丁：

1. 缺必填字段：插入 `update_fields` 或 `pick_basedata`。
2. 唯一字段重复：更新 `vars` 模板。
3. 导航服务不可达：将非主导航步骤标记 optional。

### 2. 回归样本库

目标：保证新规则不影响历史成功 YAML。

建议：

1. 每个新 HAR 类型保留原始 HAR、生成 YAML、成功 run_id、失败样例。
2. 单测覆盖解析结果，集成测试按需执行 API 闭环。
3. 建立“规则变更影响面”报告：哪些 HAR 生成 YAML 发生变化、变化是否合理。

## 后续方向

1. 批量任务报告增加失败分类聚合图。
2. 质量评分低于阈值时阻止直接生成，要求用户确认风险项。
3. 结合在线元数据自动识别字段类型和必填项。
4. 建立金蝶云场景知识库：表单、字段、按钮、流程动作、典型上下文。
5. 支持“新 HAR 导入后自动试跑沙箱”，自动生成可执行性报告。

## 工程原则

1. 主业务步骤不随便 optional，避免假通过。
2. 导航/装饰步骤可以降级，避免环境服务差异阻断主流程。
3. 环境字段统一进入 `pick_fields`，不散落在步骤里。
4. 解析规则必须有回归测试。
5. 自动修复先给建议，再让用户确认，不直接静默改业务语义。
