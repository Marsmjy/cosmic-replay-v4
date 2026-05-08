# ISV 实战 case · wtp_coordstrategytab

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（从 deep_resolve 推典型 case）
> **数据源**: `_deep_resolve_index.json` · `rules_chain_all.json::opKeys`
> **生成**: polish_form_scene_v2.py（v5.1 render_09）

## 典型 ISV 定制 case（按可继承类聚合）

### Case 1：扩展 `PermissionTabPlugin` (opKey=`save`)

**业务背景**：标品 `PermissionTabPlugin` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.formplugin.web.coordination` 包
2. 写 ISV 子类继承 `PermissionTabPlugin`：
```java
public class IsvCustomPermissionTabPlugin extends PermissionTabPlugin {
    @Override
    public void beforeBindData(...) {
        super.beforeBindData(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=BILL_FORM
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

## 通用 ISV 套路提醒

- **必调 super**：所有 lifecycle 方法 override 必须先调 super · 保留标品行为
- **不动标品 form**：要加字段 → 建 ISV 扩展元数据 + `_inherits` 引用主表
- **注册插件 targetType**：BILL_FORM / LIST_FORM / OPERATION 之一（大写枚举·区分大小写）
- **测试**：跑标品测试用例 + ISV 自有用例·验证 super 行为完好
