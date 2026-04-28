# HAR → YAML 用例抽取 SOP

> 2026-04-23 以 `D:/aiworkspace/行政组织快速维护操作.har` → `cases/admin_org_new.yaml` 为实证样本，
> 总结出这份操作规程。后续任何 HAR 生成用例都按这个流程，**以 HAR 实锤为准，不脑补**（参考 feedback_har_is_ground_truth）。

---

## 1. 读 HAR 之前先看结构类型

苍穹菜单背后的页面**不是都长一样**，抽取策略要先识别页面结构：

| 结构 | 识别特征 | 对应 HAR 特征 | 代表例子 |
|------|----------|----------------|----------|
| **A. 左树右表** | 左侧 treeview 树 + 右侧列表 + 详情 tab | `addnew` postData 里有 `{treeview: {focus: {...}}}`；save 动作会 `bar_change / new_save` 多态分叉 | 行政组织快速维护 |
| **B. 纯列表** | 无树，全屏列表 + 详情弹窗/slide | `addnew` postData = `[{},[]]`；列表 loadData 无树过滤字段 | 员工档案搜索列表 |
| **C. 向导 / 分步表单** | 步骤条、下一步 / 上一步按钮 | 见 `stepNext / stepPrev` 类动作，多个 L3 pageId 依次下发 | 入职办理 |
| **D. 详情直达** | 点菜单直接进详情（极少，多见于个人中心） | menuItemClick 响应里只下发一个 pageId，type=kdform 且非 list | "我的薪资" |

**识别方法**：看 menuItemClick 响应里 addVirtualTab 下发的 form 类型（是 list 还是 bill form）+ 有没有 tree 相关的 loadData。

写 YAML 前**必须**在 HAR 里确认属于哪种结构，不同结构后续步骤差异在第 5 步展开。

---

## 2. HAR → YAML 的 7 阶段流水线

以 admin_org（左树右表）为例，每一步说清"从 HAR 里读什么 / 写进 YAML 里什么"：

### 阶段 1：解析 HAR，提取所有 batchInvokeAction 请求

`har_extractor.py` 当前实现（extract_steps）。

**从 HAR 读**：
- `request.url` 匹配 `/form/batchInvokeAction.do` 或 `/form/getConfig.do`
- query string 里 `appId / f / ac`
- request.postData.text URL-decode 后取 `pageId / params`
- `params` 是一个 JSON 数组，每项是一个 action（`{key, methodName, args, postData}`）

**产物**：扁平的 `steps` 列表，每项带上 `_har_index / _har_page_id / _tier`（noise/ui_reaction/core 分级）。

### 阶段 2：定位业务入口——裁掉前置装饰

`har_extractor.py` 当前实现（build_yaml_case 里的 "trim_strategy"）。

**规则**：从第一个 `ac=menuItemClick` 开始保留，前面的全丢。
- HAR 前 200+ 个请求都是登录、首页、门户、消息、我的应用加载这些装饰
- 真正业务从"点菜单"开始

**特例**：如果 HAR 里没有 menuItemClick（比如用户从浏览器收藏夹直接打开了业务页面），回退到"主表单首次出现"为起点。

### 阶段 3：识别四层 pageId 架构，生成 click_menu step

**⚠ 这是 har_extractor.py 目前缺失的关键步骤**，本轮是手动补的。

从 HAR menuItemClick 请求里提取：
- `params[0].args[0]`：`{menuId, appId, cloudId}` 三要素
- 把这三个值写进 YAML 的 `vars`，再生成一个 `click_menu` step：

```yaml
vars:
  menu_id: "<从 HAR 提取>"
  menu_cloud_id: "<从 HAR 提取>"
  menu_app_id: "<从 HAR 提取>"

steps:
  - id: click_menu
    type: click_menu
    menu_id: ${vars.menu_id}
    cloud_id: ${vars.menu_cloud_id}
    menu_app_id: ${vars.menu_app_id}
    target_form: <主表单 formId>   # 见下
```

**target_form 怎么定**：看 HAR 后续 batchInvokeAction 请求里 URL 的 `f=?` 参数。
这个 `f` 一定和 menuItemClick 响应里 addVirtualTab 下发的 formId 是两回事（见 cosmic_replay_pageid_architecture）。
`target_form` 要写 URL `f` 里用的那个。

### 阶段 4：裁掉 menuItemClick 和 addnew 之间的装饰请求

`har_extractor.py` 当前的 `ui_reaction / noise` 分级过滤完成这步，但规则需要更严：

**丢掉**（noise）：
- `clientCallBack`（纯前端回调，服务端不记录业务状态）
- `queryExceedMaxCount`（列表数量预查询）
- `customEvent`（UI 事件）
- `clientPosInvokeMethod`（客户端定位类）
- `selectTab`（切 tab UI 动作；注意：**addnew 之后** HAR 里常有一个 selectTab，那个也丢）
- `getMenuData / getFrequentData`（菜单数据，click_menu 已经覆盖）
- 对 `homs_apphome / bos_portal_*` 等门户 form 的 loadData / clientCallBack（这些是 app 首页渲染）

**保留**（core）：
- `loadData` on 主表单 L2 pageId（列表初始化，**可能需要**）
- `addnew / modify / copyBill`（进入编辑态，必留）
- `updateValue / setItemByIdFromClient`（填字段，必留）
- `changeYear`（**本轮新发现必留，不能丢**）—— HAR 里每个日期 updateValue 前都有一次 changeYear
- `save / submit / delete`（最终动作，必留）
- `queryTreeNodeChildren`（**左树右表页面必留**，见阶段 5）

**规则表要更新**：`har_extractor.py` 的 `AC_TIER` 里 `changeYear` 当前标成 `noise`，应该升级为 `core`。

### 阶段 5：结构相关步骤（按页面结构分叉）

**A. 左树右表（admin_org）**：
- `click_addnew` 的 `post_data` 第一项必须有 `treeview.focus = {id, parentid, text, isParent}`
- `id` 写进 YAML vars 里（`root_org_id`），text 也写进（`root_org_name`）
- HAR 里是硬编码的树根节点 id，换环境要改

**B. 纯列表**：
- `click_addnew` 的 `post_data` 一般就是 `[{},[]]`
- 没有 treeview 字段

**C. 向导**：
- 会有多个 `stepNext` 动作，每个后面跟一批 `updateValue`
- L3 pageId 可能被多次更新（每步换一个），需要重新 harvest
- 用例要按向导步拆分成若干 `click_toolbar` + `update_fields` 组合

**D. 详情直达**：
- 没有 L2 阶段，menuItemClick 直接下发一个 L3-like pageId
- `target_form` 写 menuItemClick 响应里的那个 formId
- 不需要 `click_addnew` step（因为已经在详情态）

### 阶段 6：合并字段、降级语义、抽 vars

`har_extractor.py` 已做：
- `merge_consecutive_update_values` → 把连续 `updateValue` 合成 `update_fields`
- `lower_set_item_to_pick_basedata` → `setItemByIdFromClient` 转 `pick_basedata`
- `detect_var_placeholders` → 识别 `number / name / code` 等唯一字段抽成 `${vars.test_xxx}`

**本轮新发现要补**：
- `setItemByIdFromClient` 的 `postData[1]` 如果含有附带字段（像 HAR 里 pick_changescene 带的 `kdtest_field009=1`），**不能降级成 pick_basedata**——因为 `pick_basedata` helper 丢掉了 postData。要保留 `type: invoke` 形态。
- 需要在 `lower_set_item_to_pick_basedata` 里加判断：`if len(post_data[1]) > 0: 保持 invoke`。

### 阶段 7：生成断言 + 注释说明

`har_extractor.py` 已做：
- `_build_default_assertions` 生成 `no_save_failure + no_error_actions`
- 顶部注释说明来源 / 裁剪了多少条 / 人工审查建议

**要加**：生成后打一段"调试剧本"注释，内容是"如果 save 报 请填写 XXX，去检查 target_form 是否和 URL 里 f 参数一致 / changeYear 是否保留 / setItemByIdFromClient 的 postData 副作用是否保留"等。

---

## 3. 关键坑位（全部从本轮 HAR 实证来）

### 3.1 menuItemClick 响应里 harvest 的 formId ≠ 业务请求里的 f 参数

实锤：
- menuItemClick 响应 addVirtualTab args：`{formId: haos_adminorgtablist, pageId: {menuId}root<base>}`
- HAR entry 240 业务请求 URL：`?appId=haos&f=haos_adminorgdetail&ac=loadData`，body pageId 却是上面那个 L2 pageId

**意味着**：URL 里的 `f` 参数不是 menuItemClick 响应登记的 formId。YAML 里 `form_id` 字段要写 URL `f` 里那个，同时 `click_menu.target_form` 也写这个，让 runner 把 L2 pageId 挂到它名下。

### 3.2 changeYear 丢不得

HAR 每个日期 `updateValue` 前都有一次 `changeYear`（entry 285 / 287）。跳过它，虽然 updateValue 返回 200，但服务端 Model 里日期字段不落——save 时报"请填写 xxx 日期"。

har_extractor 当前把 changeYear 标成 noise 是错的，要改成 core。

### 3.3 setItemByIdFromClient 的 postData 副作用丢不得

HAR entry 290 选变动场景时 postData 带了 `{"k":"kdtest_field009","v":"1"}`——这是"变动场景改变时联动设置组织类型=新设"的业务规则。

`pick_basedata` helper 把这个 postData 丢了，直接降级会破坏业务规则。判断：postData[1] 非空时保留 `type: invoke`。

### 3.4 L1 portal form 要用 rootPageId

打开门户类 form（bos_portal_*）的 getConfig 用 `rootPageId` 参数，不是 `parentPageId`。当前 `replay.open_portal` 已实现。har_extractor 不用管这个（click_menu 封装掉了），但生成代码里如果看到对 portal form 做 open_form，要改成 open_portal。

### 3.5 HAR 里硬编码的环境相关值要抽 vars

- `menu_id / menu_cloud_id / menu_app_id`（菜单坐标）
- 树节点 id（treeview.focus.id）
- 基础资料 id（adminorgtype=1020 / changescene=2315256944698337280 等）
- 这些换环境会失效，必须进 vars。har_extractor 当前抽了 test_number / name，**没抽 menu 三件套和基础资料 id**——要补。

---

## 4. 生成后必做的人工验证 checklist

抽取完 YAML 不能直接跑就算完，按这个 checklist 自检：

- [ ] `click_menu.target_form` 和后续所有 invoke 的 `form_id` 一致（且 = HAR URL 里的 `f`）
- [ ] 日期类字段前有 `changeYear` step
- [ ] `setItemByIdFromClient` 如果 HAR 里 postData 有副作用字段，没被降级成 `pick_basedata`
- [ ] `updateValue` 里的 `number / name` 等唯一字段已抽进 vars 加随机
- [ ] 基础资料 id（字符串数字）没有硬编码在步骤里，都在 vars 里
- [ ] 左树右表结构：`click_addnew.post_data[0].treeview.focus` 有值
- [ ] 最后一条 save 的 `toolbar_key / item_id / click_id` 和 HAR 一致（`tbmain / new_save / save`，注意不是 `toolbarap`）

如果一个都没中 → 提高警惕，从头审一遍 HAR。

---

## 5. 当前 har_extractor.py 的 TODO（不立刻改）

优先级从高到低：

1. **P0 生成 click_menu step**：检测到 `ac=menuItemClick` 的步骤，转成 `type: click_menu`，把 `menu_id/cloud_id/menu_app_id` 抽进 vars，加 `target_form` 字段（值 = 后续 invoke 里出现最多的 `f`）
2. **P0 AC_TIER 调整**：把 `changeYear` 从 `noise` 升级为 `core`
3. **P0 setItemByIdFromClient 降级判断**：postData[1] 非空时保留 invoke 形态
4. **P1 抽 menu 三件套进 vars**：menu_id / cloud_id / menu_app_id
5. **P1 抽基础资料 id 进 vars**：setItemByIdFromClient 的 args[0][0] 是字符串数字（>12 位）时抽 vars
6. **P1 识别左树右表结构**：click_addnew postData 有 treeview 时，把 focus.id/text 抽到 vars
7. **P2 生成调试剧本注释**：把常见报错和自查路径写到 YAML 尾部注释
8. **P2 向导 / 详情直达结构支持**：目前 extractor 假设左树右表或纯列表，其它结构要单独测

---

## 6. 调试工具

保留以下 probe 脚本，生成新 YAML 遇到问题时用得上：
- `lib/probe_final.py` - 最简四层链路验证
- `lib/probe_har_replay.py` - HAR 字节级回放 baseline（当用例跑不通、怀疑自己写错了时，先跑这个看 HAR 原汁原味能不能通）

如果 HAR 原汁原味跑不通 → 是登录 / 环境 / session 问题；
如果 HAR 原汁原味能通、我们 YAML 不通 → 是 extractor 抽取或 runner 转换的问题，拿 probe_har_replay 的 params 和 runner 的 params 做 diff。

---

## 7. 一条全新 HAR 上手流程（推荐操作步骤）

1. 浏览器录 HAR：F12 → Network → Preserve log → 操作完整流程 → 导出 .har
2. 把 HAR 丢给 claude（skill: cosmic-replay），描述场景类型（左树右表 / 纯列表 / 向导）
3. 先用 `probe_har_replay.py` 字节级回放 HAR，验证登录和环境 OK（替换菜单相关索引即可）
4. 跑 `har_extractor.py extract <har> -o case.yaml` 产出起步稿
5. 按本 SOP 第 4 节 checklist 逐项审 YAML，尤其 `target_form / changeYear / setItem 副作用`
6. `python -m lib.runner run case.yaml` 跑一次
7. 失败时：runner 的 advisor 会打印建议；如果建议无效，拿 probe_har_replay 做 param diff
8. 通过后：用例 git 管理，跨环境跑 `COSMIC_BASE_URL=... runner run case.yaml`