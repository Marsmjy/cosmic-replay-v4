---
name: cosmic-db-query
description: >
  苍穹平台 PostgreSQL 数据库直查工具。当用户要求查询数据库数据、查看某个实体的数据表记录、
  排查数据问题、分析数据流转时触发。支持 SELECT/COUNT/UPDATE/DELETE/INSERT/DESC 操作。
user-invocable: true
---

# 苍穹数据库直查工具

> **用途**：基于元数据定位物理表，直接查询/修改 PostgreSQL 数据库数据。用于业务逻辑反推、数据问题排查、数据验证。

## 目录结构

```
skills/cosmic-db-query/
├── SKILL.md                  ← 本文件：技能入口
└── scripts/
    ├── db_query.py           ← 核心脚本：连接PG、执行SQL、格式化输出
    └── db_config.json        ← 数据库连接配置
```

## 外部依赖

| 依赖 | 说明 |
|------|------|
| `psycopg2-binary` | PostgreSQL Python 驱动（需离线安装） |

## 数据库架构

苍穹平台按模块拆分数据库，库名格式：`{db_prefix}_{module}`

| 模块 | 库名示例 | 包含的表 |
|------|---------|---------|
| `hr` | `hrse_sit_dev_pg1_hr` | `t_hrpi_*`（员工）、`t_haos_*`（组织）、`t_hbjm_*`（职位）、`t_hbss_*`（基础资料）、`t_hbpm_*`（岗位）等 |
| `hpdi` | `hrse_sit_dev_pg1_hpdi` | 人事档案相关 |
| `sys` | `hrse_sit_dev_pg1_sys` | 系统配置表 |
| `meta` | `hrse_sit_dev_pg1_meta` | 元数据设计表 |

默认连接 `hr` 模块，可通过 `--db` 参数切换。

## AI 执行流程

收到用户查询数据库的请求后，按以下步骤操作：

### Step 1: 识别目标实体

从用户描述中提取元数据名称。例如：
- "查一下员工表" → `hrpi_employee`
- "看看组织信息" → `haos_adminorg`
- "查岗位数据" → `hbpm_positionhr`

### Step 2: 读取元数据文件

读取 `references/hcm/hrmp-entity-metadata/metadata/{entity}.md`，从中提取：

1. **物理表名**：从"物理表"部分获取（如 `t_hrpi_employee`、`t_hrpi_employee_a`）
2. **字段映射**：从"字段列表"的"数据库字段名"列提取 `表名.列名` 格式
3. **中文名映射**：`字段Key` → `中文名` → `物理列名`

### Step 3: 翻译用户条件为 SQL

| 用户说 | 翻译为 |
|--------|--------|
| "工号 qjytest02240002" | `--where "fempnumber='qjytest02240002'"` |
| "姓名包含张" | `--where "fname LIKE '%张%'"` |
| "2024年后生效的" | `--where "fbsed >= '2024-01-01'"` |
| "当前生效数据" | `--where "fiscurrentversion='1'"` |
| "已删除的" | `--where "fisdeleted='1'"` |

### Step 4: 调用 db_query.py

```bash
python skills/cosmic-db-query/scripts/db_query.py <command> [args]
```

### Step 5: 解读并展示结果

将返回的 JSON 中的物理列名映射回中文名展示给用户。

## 可用命令

### select — 查询数据

```bash
python skills/cosmic-db-query/scripts/db_query.py select <table> \
  [--columns col1,col2,...] [--where "condition"] [--limit N] [--offset N]
```

- 默认 LIMIT 50
- 不指定 `--columns` 则查全部列
- 输出 JSON 格式

**示例**：
```bash
# 按工号查员工
python skills/cosmic-db-query/scripts/db_query.py select t_hrpi_employee \
  --columns fid,fempnumber,fname,fbsed,fiscurrentversion \
  --where "fempnumber='qjytest02240002'"

# 查全量当前生效员工数
python skills/cosmic-db-query/scripts/db_query.py count t_hrpi_employee \
  --where "fiscurrentversion='1'"
```

### count — 统计行数

```bash
python skills/cosmic-db-query/scripts/db_query.py count <table> [--where "condition"]
```

### desc — 查看表结构

```bash
python skills/cosmic-db-query/scripts/db_query.py desc <table>
```

自动探测表所在 schema，输出列名、数据类型、是否可空、默认值。

### update — 更新数据（安全模式）

```bash
# 默认 dry-run：显示受影响行数 + 预览
python skills/cosmic-db-query/scripts/db_query.py update <table> \
  --set "col='val'" --where "condition"

# 确认执行
python skills/cosmic-db-query/scripts/db_query.py update <table> \
  --set "col='val'" --where "condition" --confirm
```

**必须有 `--where`**，禁止全表更新。默认 dry-run，需 `--confirm` 才真正执行。

### delete — 删除数据（安全模式）

```bash
# 默认 dry-run
python skills/cosmic-db-query/scripts/db_query.py delete <table> \
  --where "condition"

# 确认执行
python skills/cosmic-db-query/scripts/db_query.py delete <table> \
  --where "condition" --confirm
```

**必须有 `--where`**，禁止全表删除。

### insert — 插入数据（安全模式）

```bash
# 默认 dry-run：仅预览将要执行的 SQL
python skills/cosmic-db-query/scripts/db_query.py insert <table> \
  --columns "col1,col2,..." --values "val1,'val2',..."

# 确认执行
python skills/cosmic-db-query/scripts/db_query.py insert <table> \
  --columns "col1,col2,..." --values "val1,'val2',..." --confirm
```

- `--columns` 可选，不指定则按表列顺序插入
- `--values` 必填，字符串值需用单引号包裹
- 默认 dry-run，需 `--confirm` 才真正执行

**示例**：
```bash
# 预览插入 SQL
python skills/cosmic-db-query/scripts/db_query.py insert t_hrpi_employee \
  --columns "fid,fempnumber,fname" --values "123,'EMP001','张三'"

# 确认执行
python skills/cosmic-db-query/scripts/db_query.py insert t_hrpi_employee \
  --columns "fid,fempnumber,fname" --values "123,'EMP001','张三'" --confirm
```

### raw — 原始 SQL 查询

```bash
python skills/cosmic-db-query/scripts/db_query.py raw "SELECT ..."
```

仅允许 SELECT 语句，拒绝 DROP/TRUNCATE 等危险操作。适用于 JOIN 跨表查询。

### --db 参数（切换数据库模块）

所有命令都支持 `--db` 全局参数：

```bash
# 查询 sys 模块的表
python skills/cosmic-db-query/scripts/db_query.py desc t_xxx --db sys

# 查询 meta 模块的元数据设计表
python skills/cosmic-db-query/scripts/db_query.py select t_meta_formdesign --db meta --limit 5
```

## 跨表查询（垂直拆分表）

部分实体有垂直拆分表（如 `t_haos_adminorg` + `t_haos_adminorg_a`），使用 `raw` 命令 JOIN：

```bash
python skills/cosmic-db-query/scripts/db_query.py raw \
  "SELECT a.fnumber, a.fname, b.fdisbanddate FROM t_haos_adminorg a JOIN t_haos_adminorg_a b ON a.fid = b.fid WHERE a.fnumber = 'xxx'"
```

## 苍穹数据模型常用约定

| 列名 | 含义 | 典型值 |
|------|------|--------|
| `fid` | 主键 | bigint |
| `fboid` | 业务对象ID（同一业务对象不同版本共享） | bigint |
| `fiscurrentversion` | 是否当前生效版本 | '1'=是, '0'=否 |
| `fbsed` | 生效日期 | timestamp |
| `fbsled` | 失效日期 | timestamp, '2999-12-31'=永久 |
| `fdatastatus` | 数据版本状态 | '1'=已生效 |
| `fisdeleted` | 是否已删除 | '0'=未删除, '1'=已删除 |
| `fhisversion` | 版本号 | 'V0001', 'V0002'... |
| `fcreatorid` / `fmodifierid` | 创建/修改人ID | bigint, 关联 bos_user |

## 安全规则

1. **UPDATE/DELETE 必须有 WHERE**，禁止全表操作
2. **UPDATE/DELETE/INSERT 默认 dry-run**，必须显式 `--confirm` 才执行
3. **RAW 仅允许 SELECT**，拒绝 DDL/DML
4. **SELECT 默认 LIMIT 50**，防止全表扫描
5. **连接超时 10 秒，查询超时 30 秒**
