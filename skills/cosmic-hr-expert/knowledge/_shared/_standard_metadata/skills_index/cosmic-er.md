---
name: cosmic-er
description: >
  HRMP 云 ER 图生成。当用户要求生成、更新、修复 ER 图，
  或需要查看实体引用关系、域划分、权限专题图时触发。
user-invocable: true
---

# HRMP 云 ER 图生成

> **WORKSPACE 推算**：本 SKILL.md 所在目录的上两级即为 cosmic-dev 工作区根目录。
> 脚本内部已通过 `os.path.dirname(__file__)` 自动推算，**无需手动配置路径**。
>
> **Python 命令**：Windows 使用 `python`，Linux/macOS 使用 `python3`。

## 目录结构

```
skills/cosmic-er/
├── SKILL.md                       ← 本文件：技能入口
└── scripts/
    ├── scan_references.py         ← Step 1：扫描元数据 FK 引用
    └── gen_er_diagrams.py         ← Step 2：生成 ER 模型 + Mermaid 图
```

## 数据依赖

| 输入文件 | 来源 | 说明 |
|----------|------|------|
| `references/hrmp_entity_list.md` | `cosmic-meta-api` 技能的 `gen_entity_list.py` 生成 | 实体分类清单 |
| `references/hcm/hrmp-entity-metadata/metadata/*.md` | `cosmic-meta-api` 技能的 `gen_entity_doc.py` 生成 | 每个实体的字段详情 |

| 输出文件 | 说明 |
|----------|------|
| `references/hcm/hrmp-entity-metadata/hrmp_reference_objects.md` | FK 引用分析报告 |
| `references/hcm/hrmp-entity-metadata/er_model.json` | 结构化 ER 模型 |
| `references/hcm/hrmp-entity-metadata/hrmp_er_diagrams.md` | Mermaid ER 图（最终产出） |

---

## 执行流程

### Step 1：扫描 FK 引用

```bash
python skills/cosmic-er/scripts/scan_references.py
```

**功能**：
- 遍历 `metadata/*.md` 中每个实体的字段表，提取"引用"列的 FK 关系
- 结合 `hrmp_entity_list.md` 对引用对象进行分类（平台对象 / HR代表元数据 / 共享元数据 / 非主数据类）
- 输出 `hrmp_reference_objects.md`，包含去重后的 FK 关系清单和分类统计

**关键配置**（脚本内）：
- `PLATFORM_PREFIXES`：平台对象前缀（`bos_`, `bd_`），排除出 ER
- `_is_er_excluded_nonmaster()`：非主数据类分类函数，仅排除枚举/字典表和 UI 模板

### Step 2：生成 ER 图

```bash
python skills/cosmic-er/scripts/gen_er_diagrams.py
```

**功能**：
- 读取 `hrmp_reference_objects.md` 中的 FK 关系 + `metadata/*.md` 中的中文名和物理表
- 构建 ER 模型（实体、关系、域划分），保存为 `er_model.json`
- 生成 4 层 Mermaid 图，保存为 `hrmp_er_diagrams.md`

**输出结构**：

| Section | 内容 |
|---------|------|
| 1. 领域依赖鸟瞰图 | flowchart，节点 = 子域，箭头 = 跨域 FK 数 |
| 2. 核心实体图（TOP-20） | erDiagram，连接度最高的 20 个实体 |
| 3. 子域详细 ER 图 | 每个物理域一张 erDiagram + 实体清单 |
| 4. 专题 ER 图 | 按业务主题跨域抽取（如权限基础服务） |

---

## 配置项（gen_er_diagrams.py）

### ENTITY_ALIASES — 实体别名归一化

将查询模型 / F7 实体映射到规范实体，避免 ER 图中出现重复节点：

```python
ENTITY_ALIASES = {
    'hrpi_employeenewf7query': 'hrpi_employee',
    'hrpi_assignmentf7query': 'hrpi_assignment',
    'hrpi_empposf7query': 'hrpi_empposorgrel',
    'haos_othorgstruct': 'haos_adminorgstruct',
}
```

**维护规则**：当发现 ER 图中有实体与另一实体"在关系上表达的是同一个事情"时，将非规范名映射到规范名。

### DOMAIN_MAP — 域划分

按实体名前缀自动归域，长前缀优先匹配：

```python
DOMAIN_MAP = [
    ('hrptmc_', 'rpt',  '报表分析域'),
    ('hrpi_',   'pi',   '人员信息域'),
    ('hrcs_',   'cs',   '公共服务域'),
    ...
]
```

### TOPIC_DOMAINS — 专题域

从物理域中抽取实体组成跨域专题视图。专题实体会从原物理域移除，在鸟瞰图中独立展示：

```python
TOPIC_DOMAINS = {
    'perm': {
        'name_cn': '权限基础服务',
        'entities': ['hrcs_role', 'hrcs_dimension', ...],  # 33 个实体
    },
}
```

**维护规则**：
- 实体用**中英文关键词联合匹配**筛选（如英文 `perm/role/auth` + 中文 `权限/授权/角色`）
- 已排除：`hrcs_esigncoauth`（电子签授权）、`hrcs_esignsealauth`（印章授权）、`hrcs_labeldimension`（打标维度）、`hrcs_lbldimension`（废弃）

---

## 典型使用场景

### 场景 1：完整重新生成

```bash
# Step 1
python skills/cosmic-er/scripts/scan_references.py
# Step 2
python skills/cosmic-er/scripts/gen_er_diagrams.py
```

### 场景 2：新增实体别名

在 `gen_er_diagrams.py` 的 `ENTITY_ALIASES` 中添加映射，然后只需重跑 Step 2。

### 场景 3：新增专题域

在 `gen_er_diagrams.py` 的 `TOPIC_DOMAINS` 中添加配置，然后只需重跑 Step 2。

### 场景 4：新增元数据后刷新

用 `cosmic-meta-api` 的 `gen_entity_doc.py` 生成新实体的 `.md` 文件后，重跑 Step 1 + Step 2。

---

## Mermaid erDiagram 格式规范

- **关系基数**：所有 FK 引用均为 `}o--||`（N:1 多对一）
- **实体属性**：每个实体最多一行属性，格式为 `主表 t_xxx "中文名"`
- **外部标记**：跨域引用的外部实体注释中标注 `[外部]`
- **禁止使用**：`string` 等类型关键词、样式定义（`classDef`、`fill`）
