# 模型设计 · 定额考勤项目

> **状态**: 🟢 已实证（v5+ 13 step + polish 后 · 2026-05-02 v5.1 升级）
> **初始化**: 2026-05-03
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## ✅ verified · 继承链（getFormMetadata.InheritPath 实抓）

详见 [_auto_inherit_chain.md](_auto_inherit_chain.md)

**摘要**:

- `wtp_quotasource`: （见 sidecar 表格）

## ✅ verified · 字段清单（getFormSchema.schemaText 实抓）

详见 `probe_out/<fnum>/main_form_schema.txt`（文本格式完整清单）

## 🟡 likely · 字段业务语义

<TODO> 对每个关键字段，补充业务语义：

| 字段 key | 业务含义 | 是否常改 | 影响下游 |
|---|---|---|---|
| `<key>` | `<TODO>` | | |

## ⚠️ unverified · ISV 扩展点位

<TODO>
- [ ] 哪些字段客户常要求加？
- [ ] 扩展字段应该建在哪个 InheritPath 层？
