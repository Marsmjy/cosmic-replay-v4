# hbss_hr_config · 模型设计

> **聚合场景**：HR 系统配置与安全（18 个子实体）
> **生成时间**：2026-04-29

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hbss_safeuri` | 链接明细信息 | 25 | 0 | 4 | — |
| `hbss_safeuriconfig` | 链接有效期配置 | 21 | 2 | 3 | — |
| `hbss_loginconfig` | 登录页配置 | 47 | 3 | 8 | — |
| `hbss_loginscene` | 登录场景 | 19 | 0 | 3 | — |
| `hbss_appentryconfig` | 应用入口配置 | 12 | 0 | 2 | — |
| `hbss_privacyusertype` | 外部用户类型 | 19 | 0 | 3 | — |
| `hbss_privacysigning` | 隐私声明签署记录 | 11 | 0 | 6 | — |
| `hbss_basedatalist` | HR基础资料 | 20 | 1 | 3 | — |
| `hbss_rolesourcetype` | — | ? | ? | ? | 1 |
| `hbss_timestamp` | 期间标识 | 20 | 1 | 4 | — |
| `hbss_relatepanelset` | 侧边栏配置 | 26 | 4 | 6 | — |
| `hbss_cloud` | HR领域云 | 8 | 1 | 3 | — |
| `hbss_cloud_app` | HR云与应用 | 9 | 1 | 3 | — |
| `hbss_actiontype` | 业务操作类型 | 20 | 1 | 4 | — |
| `hbss_action` | 业务操作 | 20 | 1 | 4 | — |
| `hbss_hrbuviewquery` | HR业务管理视图 | 0 | 0 | 0 | — |
| `hbss_hrbuquery` | HR管理组织 | 0 | 0 | 0 | — |
| `hbss_hrbu_hitfrequency` | HR管理组织命中次数 | 9 | 0 | 2 | — |

## 二、继承层次

```
bos_basetpl                      ← 苍穹基础模板
    └── hbp_bd_tpl_all           ← HR 基础资料模板（4 层）
            └── <子实体>          ← 12 个 hbss 字典实体
```

继承深度：4 层（hbss 字典场景标准深度）

## 三、跨云引用拓扑

本场景的子实体被以下云引用（详见 `11_upstream_downstream_logic.md` 自动注入的下游段）：

| 消费云 | 引用次数 |
|---|:---:|
| org_dev | 1 |
