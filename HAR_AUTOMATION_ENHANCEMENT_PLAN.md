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

## 已完成：第四阶段

### 1. 自动修复闭环

目标：失败后不只是提示，还能生成补丁。

已实现第一版：

```text
run_history -> failure_analysis -> advisor -> repair_plan -> user confirm -> update YAML -> rerun
```

支持三类高频补丁：

1. 缺必填字段：插入 `update_fields` 或 `pick_basedata`。
2. 唯一字段重复：更新 `vars` 模板并追加 `${rand:6}`。
3. 导航服务不可达：将非主导航步骤标记 optional。

安全策略：

1. 不静默修改 YAML，必须用户点击“应用”。
2. 只有 `safe_to_apply=true` 的补丁能一键应用。
3. 基础资料缺失但没有明确 `value_id` 时，只提示不应用。
4. 应用前写 `.yaml.bak` 本地备份。
5. 主业务表单步骤不会被自动 optional。

入口：

1. `lib/repair_planner.py`：结构化修复计划。
2. runner `fixes_ready.repair_plan`：执行失败后的实时修复计划。
3. `/api/cases/{name}/repairs/plan`：按当前 YAML 和失败信息生成计划。
4. `/api/cases/{name}/repairs/apply`：应用用户确认的单条修复。
5. Web UI “自动修复计划”卡片：展示并应用安全补丁。

验证：

1. 本地单元测试 `tests/unit tests/test_core.py` 共 183 条通过。
2. 修复计划 API 烟测通过：可生成 `repair_plan` 并应用安全补丁。
3. 语法检查通过。

## 已完成：第五阶段前置增强

### 1. 元数据驱动变量识别

目标：让新 HAR 导入时先用金蝶实时元数据和 HR 知识库补齐字段语义，再做变量抽取。

已实现：

1. `detect_var_placeholders()` 接收 `meta_resolver`，导入预览和 YAML 生成都能使用 `/metadata/getEntityType.do?entityId=` 的实时字段类型。
2. `description/remark/memo/note/comment/changedesc` 等文本字段进入智能变量识别。
3. 保存、点击、分录新增携带的 `post_data` 脏字段统一走变量检测，不再只处理编号/名称等唯一字段。
4. `kb_loader` 读取 `cosmic-hr-expert/_shared/_standard_metadata/entity_metadata/*.md`，覆盖没有独立 scenario 的 HR 标准实体。
5. `hbss_enterprise.description` 可解析为标签 `描述`、类型 `MuliLangTextField`，生成 YAML 中输出 `test_description` 和 `vars_labels.test_description=描述`。
6. `cosmic-replay-overview` 与 `cosmic-replay-troubleshooter` 已补充变量遗漏排查链路。

验证：

1. 目标单测覆盖保存按钮 `description` 变量抽取。
2. 企业 HAR 生成 YAML 时 `click_9.post_data.description.zh_CN` 变为 `${vars.test_description}`。
3. 共享实体元数据可解析 `hbss_enterprise`。

## 已完成：第六阶段

### 1. 回归样本库

目标：保证新规则不影响历史成功 YAML。

已实现：

1. `tests/fixtures/har_regression/manifest.json` 固化当前 8 类 HAR 样本。
2. `tests/fixtures/har_regression/baselines/*.json` 保存无敏感值结构基线。
3. `lib/har_regression.py` 统一生成 snapshot、比较 snapshot、输出影响等级。
4. `scripts/har_regression_report.py compare --fail-on-diff` 可在每次解析规则变更后快速判断影响面。
5. 新增单测覆盖值脱敏、差异分级和 8 类基线一致性。

影响等级：

1. `none`：无变化，可继续。
2. `info`：来源或低风险信息变化。
3. `review`：变量、环境字段、断言等变化，需要人工确认。
4. `breaking`：主表单、步骤结构、未知组件或阻塞质量项变化，建议先修复或真实环境试跑。

使用：

```bash
./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff
./venv/bin/python scripts/har_regression_report.py snapshot --update-baseline
```

注意：

1. HAR 原文仍不提交仓库。
2. 只有确认解析规则变化符合预期时，才更新 baseline。
3. 高风险变更后仍建议执行真实环境写库验证。

## 已完成：第七阶段

### 1. 用户侧产品化收口

目标：保留“直接导入生成 YAML”的直观体验，只在需要时暴露诊断和修复。

已实现：

1. HAR 预览页新增“导入验收结论”，默认回答：
   - 是否可直接生成 YAML
   - 是否有环境字段需确认
   - 是否存在未知组件
   - 下一步是生成、解析字段还是查看高级诊断
2. 质量评分、组件雷达、主表单/step 统计和原始步骤列表默认折叠为“高级诊断”。
3. 预览页支持直接编辑用例名并生成 YAML，减少一步确认。
4. 变量配置与环境字段配置保留在主路径，因为这是用户真正需要确认的内容。
5. 执行结果页新增成功证据：
   - 执行通过
   - 保存/断言链路已跑通
   - run_id 可追溯
6. 执行失败页新增下一步建议：
   - 失败位置
   - 自动归因根因
   - “应用安全修复并重跑”
   - “查看需确认的修复项”
7. 自动修复计划每条补丁支持“应用并重跑”。

验证：

1. 企业 HAR 导入前端烟测通过。
2. 模拟执行失败前端烟测通过。
3. 页面初始化无相关 Alpine console error。

## 第八阶段建议

### 1. 新 HAR 成功沉淀

目标：用户执行成功后，系统用低打扰方式沉淀经验。

建议：

1. 执行通过后提供“加入本地回归样本”入口。
2. 自动记录成功环境、run_id、主表单、变量和环境字段摘要。
3. 后续导入相似 HAR 时，优先提示“与已成功样本相似，可直接生成执行”。

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
