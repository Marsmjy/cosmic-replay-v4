# 扩展新场景实操指南

本文告诉你如何添加新的回放测试用例（从零到入库）。以"员工入职"为例。

## 5 步工作流

### Step 1：录 HAR（5 分钟）

1. 打开浏览器 → F12 → Network tab
2. **勾选** `Preserve log`（页面跳转也保留）
3. 登录苍穹（或刷新已登录页面）
4. 从主页走一遍完整业务流：点菜单 → 打开功能 → 填表 → 保存
5. 在 Network 面板右键 → `Save all as HAR with content`
6. 存到工作目录，比如 `har/employee_new.har`

**录 HAR 的原则**：
- 尽量纯净：只做想测的那一个场景，别穿插切换 tab
- 最短路径：能一次保存成功最好，失败重试过的 HAR 会有干扰动作
- 必填都填：后续 replay 的成功率取决于这次录制填得全不全

### Step 2：自动抽取 YAML（秒级）

```bash
cd .claude/skills/cosmic-replay
python -m lib.har_extractor extract ../../../har/employee_new.har \
    -o cases/employee_new.yaml
```

产出是**起步稿**，需要人工审查。

### Step 3：审查与清理 YAML（10-30 分钟）

起步稿通常有几十步，打开看：

**该删的 step**（`optional: true` 标注的大多可删）：
- `queryExceedMaxCount` - 列表行数查询，纯 UI
- `clientCallBack` - 客户端组件就绪回调
- `getCityInfo / getTelViaList / getCountrys` - 纯下拉联动
- `changeYear` - 年选择器 UI 事件
- `customEvent / selectTab` - 非业务事件

**该留的 step**：
- `loadData` - 初始加载
- `addnew / modify / delete` - 按钮点击
- `updateValue` - 字段填值
- `setItemByIdFromClient` - 下拉/基础资料选值
- `queryTreeNodeChildren` - 组织树选择（如果保存后校验依赖）
- `save / submit` - 真正的提交动作

**动态值改占位符**：
- 时间戳 → `${timestamp}`
- 单据编号之类 → `${vars.xxx}` 在 vars 里定义
- 日期 → `${today}`

**基础资料 id 跨账号问题**：
- 硬编码 id（如 `adminorgtype=1020`）在别的账号可能失效
- 可以用 `pick_basedata` step 类型，但如果要求跨账号可移植，改用 `${resolve:...}`（后续版本支持）

### Step 4：跑用例（秒级）

```bash
# 注入凭证（推荐用环境变量，不要把密码 commit 到 git）
export COSMIC_USERNAME=你的账号
export COSMIC_PASSWORD=你的密码
export COSMIC_DATACENTER_ID=数据中心id

python -m lib.runner run cases/employee_new.yaml
```

**成功** → 看到 `✓ PASS`，所有 step 都是 `✓`，入库。

**失败** → 看错误报告：
```
✗ 步骤 save 失败。
  [1] 请填写"入职日期"
  [2] 请选择"所属部门"
```

### Step 5：修用例再跑（循环）

根据失败信息补 step/字段，再跑。循环到通过。

通过后：
```bash
git add cases/employee_new.yaml
git commit -m "add replay case: employee_new"
```

## YAML 用例 schema 速查

```yaml
name: <case-id>             # 必填
description: <一句话说明>

env:                        # 必填
  base_url: <url>
  username: <user>
  password: <pwd>
  datacenter_id: <dc>

vars:                       # 可选，内部变量
  my_var: hello
  # 支持动态：${timestamp} ${today} ${rand:5} ${uuid}

main_form_id: <form_id>     # 可选，主表单（runner 会先 open）

steps:
  - id: <step-id>
    type: <类型>             # open_form / invoke / update_fields / pick_basedata / click_toolbar / sleep
    form_id: <form>
    app_id: <app>
    # ... 每种 type 的字段见下
    optional: true          # 可选，失败不终止
    capture: <var-name>     # 可选，响应存 vars

assertions:
  - type: <类型>             # no_error_actions / no_save_failure / response_contains
    ...
```

### Step 类型

**open_form** - 打开表单（拿 pageId）
```yaml
- id: open_main
  type: open_form
  form_id: haos_adminorgdetail
  app_id: haos
```

**invoke** - 通用协议调用
```yaml
- id: click_save
  type: invoke
  form_id: haos_adminorgdetail
  app_id: haos
  ac: save
  key: tbmain
  method: itemClick
  args: [new_save, save]
  post_data: [{}, []]
```

**update_fields** - 批量填字段
```yaml
- id: fill
  type: update_fields
  form_id: haos_adminorgdetail
  app_id: haos
  fields:
    number: TEST001
    name: {"zh_CN": "测试组织"}
    establishmentdate: 2026-04-22
```

**pick_basedata** - 选基础资料
```yaml
- id: pick_type
  type: pick_basedata
  form_id: haos_adminorgdetail
  app_id: haos
  field_key: adminorgtype
  value_id: "1020"
```

**click_toolbar** - 点工具栏按钮
```yaml
- id: addnew
  type: click_toolbar
  form_id: haos_adminorgdetail
  app_id: haos
  ac: addnew
  item_id: addnew
  toolbar_key: toolbarap
```

**sleep** - 等待（调试用）
```yaml
- id: wait
  type: sleep
  seconds: 2
```

### 断言类型

**no_error_actions** - 最后一步响应里没 `showErrMsg`
```yaml
- type: no_error_actions
  last_step: true
```

**no_save_failure** - 指定步骤的 save 响应没 bos_operationresult 错误
```yaml
- type: no_save_failure
  step: save
```

**response_contains** - 响应里包含特定字符串
```yaml
- type: response_contains
  step: save
  needle: ${vars.test_number}
```

## 常见陷阱

### 陷阱 1：`No such accessible method: updateValue() on object: kd.bos.form.field.TextEdit`

`updateValue` 的 key 是空串，字段名在 `postData`：
```yaml
# ❌ 错
- type: invoke
  ac: updateValue
  key: number           # key 是空串才对
  method: updateValue
  args: []

# ✅ 对（用 update_fields 封装好了）
- type: update_fields
  fields:
    number: TEST001
```

### 陷阱 2：基础资料 id 在别的账号不存在

HAR 里 `adminorgtype=1020` 是录制账号的"公司"id，换账号可能失效。**短期**：换账号手动改；**长期**：等 `${resolve:basedata:...}` 支持。

### 陷阱 3：Save 返回看起来成功，其实被拦截

苍穹"保存失败"不会给 HTTP 错误，而是弹 `bos_operationresult` 子表单。runner 的 `no_save_failure` 断言会自动拉这个子表单的内容查错误原文。**一定要加这条断言**。

### 陷阱 4：`请填写"xxx"` 但 HAR 里看起来没填

addnew 响应里服务端会回显一批**预填默认值**（比如组织体系、变动场景），但这些值只是"客户端显示"，不代表服务端 Model 已经写入。如果默认值是空（基础数据没配）或者录制账号和测试账号的默认值不同，就会报缺失。

**解决**：显式加 `pick_basedata` 或 `update_fields` step，把需要的字段主动写进 Model。

### 陷阱 5：环境网关偶发 502

SIT 环境不稳，登录时 `getPublicKey.do` 偶尔 502。replay.py 里 `login()` 默认重试 3 次 + 等 3 秒。如果持续 502，等一会再试。

## 高级：Python 脚本直接调用

如果 YAML 表达不了复杂逻辑（动态分支、循环造数），直接写 Python：

```python
from lib import login, CosmicFormReplay, FieldResolver, extract_save_errors

sess = login("https://...", "user", "pwd", "dc_id")
replay = CosmicFormReplay(sess)
replay.init_root()

for i in range(100):
    replay.open_form("haos_adminorgdetail", "haos")
    replay.click_toolbar("haos_adminorgdetail", "haos", "addnew",
                        "addnew", "addnew", toolbar_key="toolbarap")
    replay.update_fields("haos_adminorgdetail", "haos", {
        "number": f"BATCH{i:04d}",
        "name": {"zh_CN": f"批量测试_{i}"},
    })
    resp = replay.click_toolbar("haos_adminorgdetail", "haos", "save",
                                "new_save", "save", toolbar_key="tbmain")
    errs = extract_save_errors(resp, replay)
    if errs:
        print(f"#{i} 失败: {errs[0]}")
        break
    print(f"#{i} 成功")
```

## 把 skill 移植到其他项目

整个 `cosmic-replay/` 目录可独立复制：
1. 复制 `cosmic-replay/` 到目标项目的 `.claude/skills/`
2. 确保 `cosmic-login` skill 也在 `.claude/skills/` 下
3. `pip install requests urllib3`（必需），`pip install pyyaml`（推荐）
4. 按需改 `cases/` 里的 `env:` 部分指向新环境

零项目依赖——本 skill 没有 import 任何项目内部模块。