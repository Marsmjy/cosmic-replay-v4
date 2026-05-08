---
name: cosmic-entity-doc
description: >
  实体元数据字段详情文档生成。当用户要求输出某个实体的元数据详情、字段列表、
  物理表结构时触发。
user-invocable: true
---

# 实体元数据字段详情生成

> **WORKSPACE 推算**：本 SKILL.md 所在目录的上两级即为 cosmic-dev 工作区根目录。
> 脚本内部已通过 `os.path.dirname(__file__)` 自动推算，**无需手动配置路径**。
>
> **API 依赖**：本技能的脚本调用 `cosmic-meta-api` 技能中的 `cosmic_api.py` 进行鉴权和 API 查询。
> 使用前需确保 `cosmic-meta-api` 的 `config.json` 已正确配置。
>
> **Python 命令**：Windows 使用 `python`，Linux/macOS 使用 `python3`。

## 目录结构

```
skills/cosmic-entity-doc/
├── SKILL.md                       ← 本文件：技能入口
└── scripts/
    ├── gen_entity_doc.py          ← v1：.dym 解析 + API 合并模式（推荐）
    └── gen_entity_doc_v2.py       ← v2：纯 API 模式（entityMeta + getFormSchema）
```

## 外部依赖

| 依赖 | 位置 | 说明 |
|------|------|------|
| `cosmic_api.py` | `skills/cosmic-meta-api/scripts/` | API 鉴权 + 接口调用 |
| `config.json` | `skills/cosmic-meta-api/scripts/` | 鉴权凭据、服务地址 |
| `skill_env.json` | `skills/cosmic-meta-api/` | workspace 路径（可选，有 fallback） |
| `src/**/*.dym` | 工作区根目录下 | 本地 .dym 元数据源文件 |

---

## v1 脚本（gen_entity_doc.py）— 推荐

> 从 `.dym` 文件提取数据库字段名 / 垂直拆分表信息，并调用 `getFormSchema` API 获取字段类型和引用关系，合并生成 Markdown 文档。

### 使用方式

```bash
# 基本用法
python skills/cosmic-entity-doc/scripts/gen_entity_doc.py <entity_name>

# 指定输出目录
python skills/cosmic-entity-doc/scripts/gen_entity_doc.py <entity_name> --output <dir>

# 指定工作区目录
python skills/cosmic-entity-doc/scripts/gen_entity_doc.py <entity_name> --workspace <dir>

# 仅解析 .dym 文件（跳过 API 调用，字段类型和引用列为空）
python skills/cosmic-entity-doc/scripts/gen_entity_doc.py <entity_name> --no-api

# 指定 formId（优先于 API 查询和 .dym 中的值）
python skills/cosmic-entity-doc/scripts/gen_entity_doc.py <entity_name> --form-id <formId>
```

### 示例

```bash
# 生成入职单据实体（含垂直拆分表 A/B/C）
python skills/cosmic-entity-doc/scripts/gen_entity_doc.py hom_onbrdbillbase

# 生成员工实体
python skills/cosmic-entity-doc/scripts/gen_entity_doc.py hrpi_employee

# 输出到指定目录
python skills/cosmic-entity-doc/scripts/gen_entity_doc.py haos_adminorg --output D:\cosmic-dev\references\hcm\hrmp-entity-metadata\metadata
```

### 输出

默认输出到 `{workspace}/references/hcmnew/{entity_name}.md`，格式：

- 表单编码 / 表单ID / 归属信息
- 物理表列表（主表 + 垂直拆分表 / 分录子表）
- 字段列表（6列）：字段Key、中文名、类型、数据库字段名、必填、引用
- 字段按物理表分布统计（仅拆分表/多实体）

### 工作原理

1. **搜索 .dym 文件**：在 `src/**/datamodel/**/*.dym` 中按文件名或 `<Number>` 标签匹配
2. **解析 .dym**：提取 `<TableName>`、`<SplitTables>`、每个字段的 `<FieldName>`/`<Suffix>`
3. **继承链解析**：沿 `<ParentId>` 递归向上，合并祖先实体的字段和物理表
4. **调用 API**：`getFormSchema` 获取字段中文名、类型和引用关系
5. **合并数据**：以 API 字段顺序为准，补充 .dym 中的数据库字段名和拆分表归属
6. **生成 Markdown**：输出标准格式文档

### 数据库字段名规则

| 场景 | 格式 | 示例 |
|------|------|------|
| 无拆分表 | `{field_name}` | `fnumber` |
| 有拆分表 - 主表字段 | `{table}.{field_name}` | `t_hom_onbrdbill.fbillno` |
| 有拆分表 - 拆分表字段 | `{table}_{suffix}.{field_name}` | `t_hom_onbrdbill_A.fonbrdreason` |
| 子表字段 | `{child_table}（子表）` | `t_hom_onbrdreltr（子表）` |
| 无数据库列 | `—` | RadioField 等虚拟字段 |

---

## v2 脚本（gen_entity_doc_v2.py）— 纯 API 模式

> 完全基于两个 API 获取数据，不依赖本地 `.dym` 文件：
> - `entityMeta` API：获取所有物理表和 DB 列名
> - `getFormSchema` API：获取字段 Key/中文名/类型/引用关系

### 使用方式

```bash
python skills/cosmic-entity-doc/scripts/gen_entity_doc_v2.py <entity_name>
python skills/cosmic-entity-doc/scripts/gen_entity_doc_v2.py <entity_name> --output <dir>
```

### 适用场景

- 本地无 `.dym` 源文件时（如非 HCM 云实体）
- 需要快速生成而不依赖本地代码仓库

---

## 下游消费者

| 消费方 | 使用的输出文件 | 用途 |
|--------|--------------|------|
| `cosmic-er` 技能 | `metadata/*.md` | ER 图实体中文名、物理表、FK 引用提取 |
| `cosmic-gen` 技能 | `*.md` 元数据详情 | 代码生成时的字段映射参考 |
