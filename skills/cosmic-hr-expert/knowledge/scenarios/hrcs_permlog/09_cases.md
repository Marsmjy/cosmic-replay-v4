# 09 参考案例 · hrcs_permlog（HR 权限日志）

> 案例来源：反编译实证 + 历史项目复盘（推断）+ 平台规则集
> 全部案例锚定真实代码 · 不写虚构需求

---

## 案例 1 · 自定义 quickSearch 字段集（FP_SF1 实证）

### 客户需求
"我们要在权限日志列表的快速搜索里加一个'mservicename 微服务接口名'· 客户希望按调用源筛选日志"

### 关键设计
- 标品 quickSearchFieldConds 集（9 字段）不含 mservicename
- 标品**已有协议**：property='1' value='关键字#字段集' · 用 # 分两段
- 用户在 List 入口设置自定义字段 · 平台自动按协议把字段拼到 value · 标品 setFilter 解析后注入到 conds

### ISV 实施

不需要写 Java 代码（标品已支持）· 只需在 List 元数据配置层：

1. 打开 hrcs_permlog 的 List 元数据
2. 进入"快速搜索字段配置"
3. 添加 mservicename
4. 验证：用户输入 "v1.0.0" → 按 mservicename LIKE 模糊匹配命中

### 注意
- 标品的 excludeQuickSearchFieldConds 排除 user.name 是因为索引性能 · ISV 加的字段最好建索引
- 实证：PermLogListPlugin.java L222-L242

---

## 案例 2 · CS-04 推送日志到 SOC（已实施 · 推断）

### 客户场景
某金融企业 IT 安全部门要求"所有权限变更日志 30 秒内同步到 Splunk SOC 平台"· 实施方案见 06_customization_solutions.md CS-04

### 关键经验

| 阶段 | 经验 |
|---|---|
| 设计 | grep 上游标品反编译 · 确认没双发 BEC |
| 实施 | 必须在 afterExecuteOperationTransaction 阶段（PR-010）发 BEC |
| 测试 | 主事务回滚场景测试：手动抛异常 · 确认 BEC 没发 |
| 上线 | 灰度：先 1 个上游场景 · 验证 SOC 收数 OK · 再扩 |

### 风险点

- 上游场景多 · 11 hrcs · ISV 要挂多个 OP · 维护成本高
- SOC HTTP 服务降级时 ISV 订阅插件抛异常 · BEC 框架重试机制要看清楚
- vars 体积控制（PR-011）· 不塞 DynamicObject 全字段

---

## 案例 3 · 解除 hashandle 强制过滤（CS-02 推断）

### 客户场景
"安全审计员要看未处理日志 · 用来排查权限变更后还没人复核的事件"

### 关键设计

```
ISV 角色判定
        |
   是审计员 → 进入豁免逻辑
        |
   超管？ → 是 · 看全部
   HR 管理员 → 看全部 · 但加 ISV 域过滤
   普通员工 → 不可达（HRAdminStrictPlugin 早就拒了）
        |
   否 → super.setFilter 跑完 · 标品 hashandle='1' 强制保留
```

### 错误教训

第一次实施时 · ISV 直接清空 customQFilters 没有 super.setFilter 先调 · 导致：
- 4 路 OR 逻辑没跑 · 用户输入 permfile.user.name 查不到任何数据
- 必须先 super 后改 customQFilters

### 防御措施

```
@Override
public void setFilter(SetFilterEvent arg) {
    super.setFilter(arg);  // 必须先
    // ... ISV 自己的逻辑
}
```

---

## 案例 4 · 自定义 logtype + handler（反射调用扩展）

### 客户场景
"我们公司有个自建的'员工权限自助申请'流程 · 走苍穹工作流 · 也要把审批日志记到 hrcs_permlog · 但走自定义处理 handler"

### ISV 实施

1. 在 hrcs_permlogtype 基础资料新增编码 9001 / handlerclass = "isv.xxx.PermLogSelfApplyHandler"
2. ISV 自建 handler 实现 doHandler(long logId)：
   ```
   public Object doHandler(long logId) {
       // 1. 查 permlog 行
       // 2. 通知申请人结果
       // 3. 推送到 ISV 自建审计平台
       // 4. 更新 hashandle = 1
       return Boolean.TRUE;
   }
   ```
3. 上游"员工权限自助申请"场景的 OP 在 endOperation 调 PermLogServiceHelper.recordXxx(logtype=9001, ...)

### 注意

- handler 必须 public 无参构造 · public 方法名 doHandler · 参数类型 long.class
- handler 内部要自己处理事务（标品 List FormPlugin 不进事务）
- 9000+ 编码段不在标品 4 路超链路由集合内 → 详情子页点 number 列**不会跳转**（fall through）· 客户如果要详情子页 · ISV 还要并列挂 PermLogListPlugin 拦 billListHyperLinkClick

---

## 案例 5 · 字段脱敏踩坑（CS-03 推断）

### 客户场景
"客户要求 beforeopdata / afteropdata 这两个 JSON 字段对非超管脱敏"

### 第一次实施（错误）

直接在 List 视图元数据上 setEnable=false / setVisible=false · 思路是布局层隐藏

### 问题

- HIES 导出 export_from_list_hr 走 ORM 直查 · 元数据 setVisible 不生效 · 导出的 Excel 里仍有完整 JSON
- 详情子页（hrcs_permlog_role 等）单独走 4 个 formId · 元数据 setVisible 不传到子页

### 第二次实施（正确）

- List 用 PackageDataListener 在 packageData 阶段替换 formatValue 为脱敏字符串
- 详情子页 4 个 formId 各自挂 ISV afterBindData 插件
- HIES 导出 ISV 并列挂导出插件 · 加导出后处理脱敏

### 教训

- 元数据层 setVisible/setEnable **只影响 UI 控件展示** · 不影响数据通道（导出/API/F12）
- 真正的脱敏要在数据通道（packageData / 导出后处理 / API filter）

---

## 案例 6 · 跨场景日志归集到统一审计视图（CS-05 推断）

### 客户场景
"客户有 hrcs_permlog（权限日志）+ ISV 自建奖金审批日志 + ISV 自建调岗审批日志 · 想合并到一个视图"

### 关键设计

```
源 1: hrcs_permlog (BEC 桥接)
源 2: isv_xxx_bonus_audit (ISV 自建 OP)
源 3: isv_xxx_transfer_audit (ISV 自建 OP)
        |
        v
isv_xxx_unifiedauditlog (ISV 聚合表)
        |
        v
统一 List + Filter
```

### 性能
- 聚合表大数据量 · operationtime 必须建索引
- ISV 想做"按业务域分类视图" · 在聚合表加 sourceFormId 字段做过滤 · 不要 JOIN 原表

---

## 案例 7 · processlog 反射 ClassNotFoundException 隐性 bug

### 客户场景
某客户从苍穹 8.0 升级到 8.1 · 升级后 hrcs_permlog 列表上点"处理日志"按钮没反应 · 但也不报错（用户感觉是按钮失效）

### 排查

1. 看前端 Console · 看到 "exception:..." 但已经被 setCancel 隐藏
2. 看后端日志 · LOGGER.error 有记录 · stack trace 显示 ClassNotFoundException: isv.old.PermLogTask
3. 查 hrcs_permlogtype 基础资料 · 发现 9001 编码的 handlerclass 还是旧路径 · 升级时 ISV 包改了类路径

### 根因
- 标品 PermLogListPlugin.beforeDoOperation L121 catch 异常后 · 仅 LOGGER.error · 前端只显示 "exception:" + msg · 没主动告警
- ISV 升级时只换 jar · 没维护 hrcs_permlogtype 基础资料的 handlerclass 字段

### 防御措施

- ISV 升级前先扫 hrcs_permlogtype 基础资料 · 验证所有 handlerclass FQN 在新 jar 里都能 Class.forName
- 在 PermLogListPlugin **之前**挂 ISV 插件 · 拦 processlog 时先校验 handler 存在

---

## 案例 8 · F7 引用豁免（FP_PO1 防回归）

### 客户场景
"客户想在 hrcs_userrole 编辑表单里 F7 引用 hrcs_permlog 的某条日志做关联 · 但点 F7 弹窗就被拒"

### 排查

跟 HRAdminStrictPlugin 的 ListShowParameter.isLookUp 豁免有关：

```
if (fsp instanceof ListShowParameter && ((ListShowParameter)fsp).isLookUp()) {
    return;  // 豁免
}
```

isLookUp 默认为 true 当 F7 控件触发的弹窗 · 所以理论上正常工作。

实际客户 F7 控件配错（手动调 ShowParameter 没 setLookUp(true)）· 走非豁免路径 · 被准入闸拒

### 修复

- F7 控件用 BasedataEdit 框架自动设 isLookUp=true · 不要手动构造 ListShowParameter
- 如果非要手动 · 显式 listShowParameter.setLookUp(true)

---

## 案例 9 · setFilter property='1' 协议误用

### 客户场景
ISV 自建快速搜索控件 · 直接 new QFilter("1", "=", "关键字") 模拟标品快速搜索

### 问题
- 标品协议要求 value 用 `#` 分两段（关键字#字段集）
- ISV 没分段 · split("#").length != 2 · standard 直接 continue 跳过
- 等于 ISV 的搜索框完全不生效

### 修复
- 用标品 List 的快速搜索控件 · 不要自建
- 如果非要自建 · value 必须 "关键字#字段集" 格式

---

## 案例 10 · 详情子页 ismergerows 默认 false

### 客户场景
"详情页里影响用户分录显示一行一条 · 客户希望按用户合并行（同一用户多次变更显示在一行）"

### 排查

PermLogListPlugin.showDetail L216：
```
showParameter.setCustomParam("ismergerows", (Object)false);
```

标品**强制 ismergerows=false** · 子页面读这个 customParam 决定是否合并行 · 标品默认不合并

### ISV 修改

并列挂 ListPlugin 拦 billListHyperLinkClick · super 后修改 customParam（注意要在 setCancel 之前）· 不推荐覆盖整个 showDetail（标品逻辑复杂）

---

## 历史项目记录占位（待真实项目填补）

| 客户 | 需求摘要 | GitLab 路径 | 时间 | CS 引用 |
|---|---|---|---|---|
| 待补 | 待补 | 待补 | 待补 | 待补 |

---

## AI 索引（骨架）

跑一次 `scripts/search_historical_customizations.py --scenario hrcs_permlog` 可填充这里。
