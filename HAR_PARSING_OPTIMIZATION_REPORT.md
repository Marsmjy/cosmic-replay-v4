# HAR 文件解析流程优化分析报告与实施方案

## 1. 目标与范围

本次优化覆盖 `cosmic-replay` 从 HAR 文件导入到用例执行的完整链路：

`HAR 上传/导入 -> HAR 解析 -> 智能变量识别 -> 环境字段识别 -> YAML 用例生成 -> YAML 解析 -> 用例执行 -> 保存/提交 -> 数据入库`

目标是让系统能稳定识别智能用例变量和环境相关字段变量，生成可执行的 YAML，并保证已有成功样例不被破坏。

## 2. 项目理解

本项目核心链路如下：

- `lib/har_extractor.py`：负责 HAR 解析、步骤规整、变量识别、环境字段提取、YAML 生成。
- `lib/runner.py`：负责 YAML 执行、变量解析、`pick_fields` 覆盖、断言和执行结果汇总。
- `lib/replay.py`：负责登录、pageId 生命周期、`open_form/loadData/invoke` 等苍穹协议回放。
- `lib/metadata_resolver.py`：负责在线读取实体字段元数据，用于增强字段标签、类型、必填性判断。
- `lib/field_resolver.py`：负责基础资料跨环境解析。
- `skills/cosmic-replay-overview`：提供系统架构、配置、用例格式和链路说明。
- `skills/cosmic-replay-troubleshooter`：提供 pageId、`target_forms`、保存失败、变量漏识别等排故规则。

从文档和代码综合看，系统的关键风险集中在三类：

- HAR 中的浏览器上下文不等于 API 回放上下文。
- HAR 里并非所有字段都会显式出现，部分字段由客户端或服务端隐式带出。
- YAML 的类型推断会影响 Java 服务端反射调用，尤其是多语言文本里的数字字符串。

## 3. 8 种 HAR 导入类型分析

当前系统样例以 `har_uploads/` 中 8 个 HAR 为准，并与 `cases/` 中同名 YAML 一一对应。

| 序号 | HAR 类型 | 主场景 | 关键链路 | 优化后结果 |
|---|---|---|---|---|
| 1 | HR基础服务云新增一条用工关系基础资料 | 门户进入基础资料列表后新增 | `menuItemClick -> basedatalist -> card -> save` | 通过 |
| 2 | 业务模型添加一个基础资料附表 | 业务模型树菜单进入，多弹窗/子表配置 | `appItemClick -> treeMenuClick -> logicentity -> saveandeffect` | 通过 |
| 3 | 新增一条行政组织 | 行政组织新增 | `menuItemClick -> addnew -> pick -> save` | 通过 |
| 4 | 新增入职0512测试 | 入职信息新增并流转 | `menuItemClick -> onboard info -> startupflow -> confirm` | 通过 |
| 5 | 入职申请到确认入职 | 入职申请到确认入职完整流程 | `appItemClick -> onboard -> workflow -> confirm` | 通过 |
| 6 | 基础资料-用人单位 | 基础资料列表进入用人单位后新增 | `basedatalist -> enterprise -> new -> save` | 通过 |
| 7 | 基础资料-新增国籍 | 国籍基础资料新增 | `basedatalist -> nationality -> new -> save` | 通过 |
| 8 | 岗位信息维护-新增一个岗位 | 岗位维护树上下文新增 | `positionhr -> new -> pick/update -> save` | 通过 |

优化前明确失败的两个样例：

- `岗位信息维护-新增一个岗位`
- `基础资料-用人单位`

优化过程中还发现 `新增入职0512测试` 由于邮箱固定值导致重复数据错误，已一并纳入变量化处理。

## 4. 失败根因

### 4.1 岗位信息维护-新增一个岗位

原始问题：

- 执行中出现 `java.lang.Integer cannot be cast to java.lang.String`。
- 修复类型问题后又出现 `请填写“行政组织”`。

根因：

- `posorientation/posduty/posstandard` 是多语言文本字段，录制值为 `11111` 这类纯数字字符串。旧 YAML 输出未强制加引号，反序列化后变成整数，传给 Java 文本字段时类型不匹配。
- HAR 中没有显式 `adminorg` 赋值动作，真实组织上下文藏在 `click_tblnew.post_data.treeview.focus.id` 中。旧解析器没有把它转成可执行字段步骤。

修复：

- 多语言语言 key（`zh_CN/zh_TW/en_US/GLang`）下的纯数字值强制按字符串输出。
- 从 `new/addnew` 的 `treeview.focus` 自动提取组织上下文，生成 `pick_adminorg_ctx`。
- 同时将 `treeview.focus` 纳入 `pick_fields`，用于换环境覆盖。

### 4.2 基础资料-用人单位

原始问题：

- 保存时报 `请按要求填写“创建组织”`。

根因：

- HAR 浏览器录制时 `load_enterprise_2` 会自动带出 `createorg`。
- API 回放时同一步响应为 `createorg=null`，并出现 `showFieldTips: 请先录入创建组织`。
- `createorg` 的元数据类型是 `MainOrgProp`，不能用普通 `pick_basedata` 修复。

修复：

- 保留 `basedatalist refresh -> entryRowClick -> hyperLinkClick -> enterprise loadData` 这类列表到卡片的上下文步骤。
- 遇到 `MainOrgProp/createorg` 时，从 `commonSearch` 的 `useorg.id` 或树上下文提取组织 id。
- 自动插入 `fill_createorg_ctx`，以 `update_fields(createorg=<context id>)` 补偿浏览器自动带出字段。
- 将 `createorg/useorg` 纳入 `pick_fields`，用于环境切换。

### 4.3 新增入职0512测试

原始问题：

- 执行时出现邮箱重复。

根因：

- `peremail` 未被识别为唯一字段，仍使用录制时固定邮箱。

修复：

- 将 `email/peremail/workemail/personalemail` 纳入智能变量识别。
- 自动生成 `${vars.test_email}`，保留原域名并随机化本地部分。

## 5. 已实施的统一解决方案

### 5.1 HAR 上下文保留

新增“列表/树到卡片”的上下文桥接识别：

- `refresh`
- `entryRowClick`
- `hyperLinkClick`
- `loadData`
- `selectTab`
- `postExpandNodes`
- `commonSearch`

当 HAR 没有 portal/menu 入口时，不再直接裁剪到主表单，而是保留主表单前的上下文桥接步骤。桥接步骤会从 `optional` 提升为必执行，避免后续默认组织、树焦点、卡片上下文丢失。

### 5.2 隐式上下文字段补偿

新增统一补偿函数：

- `_extract_treeview_focus()`
- `_extract_common_search_defaults()`
- `_infer_context_field_modes()`
- `_inject_context_field_steps()`

补偿规则：

- `treeview.focus` 隐式组织字段 -> 生成 `pick_basedata`，例如 `pick_adminorg_ctx`。
- `MainOrgProp/createorg` -> 生成 `update_fields`，例如 `fill_createorg_ctx`。
- 如果字段已在 HAR 中显式设置，则不重复注入。

### 5.3 环境字段统一提取

扩展 `pick_fields` 提取范围：

- 显式 `pick_basedata` 字段。
- 日期类字段。
- `treeview.focus` 新增上下文组织。
- `update_fields` 中的 `createorg/useorg/adminorg/org/dept/position` 等环境相关字段。

执行期 `pick_fields` 覆盖也同步增强：

- 原本只覆盖 `pick_basedata`。
- 现在也能覆盖上下文补偿生成的 `update_fields`。

### 5.4 YAML 类型安全

修复 YAML 标量输出：

- 多语言 key 下的纯数字字符串必须加引号。
- 长数字 id 字符串继续加引号，避免 YAML 自动转整数。
- 日期字符串继续加引号或替换为 `${today}`。

这避免了 Java 服务端反射时的 `Integer -> String`、日期对象类型不匹配等问题。

### 5.5 唯一变量识别增强

扩展智能变量类别：

- 编码：`number/code/billno/orderno`
- 名称：`name/simplename/fullname`
- HR 唯一字段：`empnumber/certificatenumber/phone`
- 邮箱：`email/peremail/workemail/personalemail`

生成策略：

- 编码保留前缀并随机化。
- 姓名与编号关联时复用编号变量。
- 手机号保留区号/前三位并随机化。
- 邮箱保留域名并随机化本地部分。

## 6. 验证结果

本地单元回归：

```bash
pytest -q tests/unit/test_har_extractor_regressions.py
```

结果：

- 6 条回归测试全部通过。

覆盖内容：

- 多语言数字字符串不被 YAML 转整数。
- 用人单位 HAR 保留列表上下文。
- 用人单位 HAR 自动注入 `fill_createorg_ctx`。
- 岗位 HAR 自动注入 `pick_adminorg_ctx`。
- 岗位 HAR 多语言文本字段类型正确。
- 入职 HAR 自动抽取邮箱变量。

端到端验证：

- 使用当前 `build_yaml_case()` 重新生成 `har_uploads/` 下 8 个 HAR。
- 逐个执行生成后的用例。
- 结果：8/8 成功执行。

说明：

- 验证以保存/提交成功、断言通过、后续查询链路无错误为准。
- 未直接做数据库 SQL 查询；当前系统的“数据正常入库”以苍穹保存/提交流程成功和无保存失败响应为判断依据。

## 7. 新 HAR 标准诊断流程

遇到新 HAR 时，建议按以下步骤定位：

1. 先看预览结果。
检查 `main_form_id`、`steps`、`detected_vars`、`pick_fields` 是否完整。

2. 检查变量缺失。
固定编码、名称、手机号、证件号、邮箱、日期如果没有变量化，优先扩展 `detect_var_placeholders()` 的分类规则。

3. 检查环境字段缺失。
如果保存时报“请填写某字段”，先看该字段是否存在于 `pick_fields`。若不存在，判断它是显式基础资料字段、树上下文字段，还是 `MainOrgProp`。

4. 检查上下文桥接。
列表进入卡片、树节点进入详情、新增前的 `entryRowClick/hyperLinkClick/treeview.focus/commonSearch` 不能被裁掉。

5. 检查 pageId。
如果报页面超时、未初始化、缓存连接失败，按 `cosmic-replay-troubleshooter` 的 L2/L3 pageId 和 `target_forms` 规则排查。

6. 做最小回归。
每修复一种模式，添加一个针对 HAR 的单元回归，再跑已有成功样例，确认兼容。

## 8. 预防措施

- 每新增一个失败 HAR，都先归类为：变量缺失、环境字段缺失、上下文缺失、pageId 错误、数据重复、服务端业务校验。
- 对“浏览器自动带出但 HAR 无显式 set”的字段，不直接硬编码业务值，优先从 `treeview.focus`、`commonSearch`、元数据类型中推断。
- 对 `MainOrgProp` 单独处理，不套用普通 `pick_basedata`。
- 对 YAML 输出保持类型保守：文本值宁可显式字符串化，也不要让 YAML 猜类型。
- 所有修复必须配套回归测试，尤其是曾经成功的 8 个样例不能回退。

## 9. 修改文件清单

- `lib/har_extractor.py`
  - HAR 上下文保留。
  - 隐式上下文字段补偿。
  - 环境字段提取增强。
  - 邮箱变量识别。
  - YAML 字符串类型修复。

- `lib/runner.py`
  - `pick_fields` 覆盖增强，支持覆盖 `update_fields` 中的上下文补偿字段。

- `tests/unit/test_har_extractor_regressions.py`
  - 新增 6 条回归测试。

- `task_plan.md`
  - 任务计划与错误处理记录。

- `findings.md`
  - 根因、实现、验证摘要。

- `progress.md`
  - 工作进度记录。

## 10. 当前结论

本轮优化已经完成。当前项目已经具备一套可复用的 HAR 解析标准流程，能覆盖已有 8 类 HAR 导入类型，并能较快定位和修复新 HAR 中的变量识别缺失、环境字段缺失和执行期报错问题。

