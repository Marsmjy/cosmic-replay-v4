---
name: cosmic-entity-list
description: >
  云元数据分类清单生成。当用户要求生成、刷新某个云（HRMP/HR/ODC 等）的实体清单，
  或需要查看元数据分类统计、共享物理表关系时触发。
user-invocable: true
---

# 云元数据分类清单生成

> **WORKSPACE 推算**：本 SKILL.md 所在目录的上两级即为 cosmic-dev 工作区根目录。
> 脚本内部已通过 `os.path.dirname(__file__)` 自动推算，**无需手动配置路径**。
>
> **API 依赖**：本技能的脚本调用 `cosmic-meta-api` 技能中的 `cosmic_api.py` 进行鉴权和 API 查询。
> 使用前需确保 `cosmic-meta-api` 的 `config.json` 已正确配置（鉴权信息、服务地址）。
>
> **Python 命令**：Windows 使用 `python`，Linux/macOS 使用 `python3`。

## 目录结构

```
skills/cosmic-entity-list/
├── SKILL.md                       ← 本文件：技能入口
└── scripts/
    └── gen_entity_list.py         ← 核心脚本：查询 + 过滤 + 分类 → Markdown 清单
```

## 外部依赖

| 依赖 | 位置 | 说明 |
|------|------|------|
| `cosmic_api.py` | `skills/cosmic-meta-api/scripts/` | API 鉴权 + 接口调用（`load_config`、`do_auth`、`do_query_forms_by_app`） |
| `config.json` | `skills/cosmic-meta-api/scripts/` | 鉴权凭据、服务地址等环境配置 |
| `src/**/*.dym` | 工作区根目录下 | 本地 .dym 源文件（白名单过滤 + 物理表映射） |

## 输出

默认输出到 `{workspace}/references/hcmnew/{cloud}_entity_list.md`，格式：

- 文件头：总数统计 + 去重折叠说明
- **概览**：各分类行数统计表
- **共享物理表汇总**：列出所有被多个元数据共享的物理表及成员列表
- **各分类明细**：

  | 分类 | 对应 modelType |
  |------|---------------|
  | 基础资料 | `BaseFormModel` |
  | 基础资料-非主数据类 | `BaseFormModel`（符合非主数据规则） |
  | 单据 | `BillFormModel`、`MobileBillFormModel`、`LogBillFormModel` |
  | 动态表单 | `DynamicFormModel`、`MobileFormModel` |
  | 布局 | `WidgetFormModel` |
  | 查询模型 | `QueryListModel`、`ReportQueryListModel` |
  | 报表 | `ReportFormModel` |
  | 移动端 | 移动端相关 |
  | 其他 | 其余类型 |

每行格式：
```
序号 | 代表元数据 | formId | 物理表 | 共享元数据 | 表单名称 | 表单类型 | 所属应用
```

---

## 使用方式

```bash
# 生成单个云的元数据清单
python skills/cosmic-entity-list/scripts/gen_entity_list.py hrmp

# 同时生成多个云
python skills/cosmic-entity-list/scripts/gen_entity_list.py hrmp hr odc

# 生成所有 HCM 云
python skills/cosmic-entity-list/scripts/gen_entity_list.py --all

# 指定输出目录
python skills/cosmic-entity-list/scripts/gen_entity_list.py hrmp --output-dir D:\cosmic-dev\references\hcmnew

# 不用 dym 文件白名单过滤（改用 app 前缀过滤，速度更快但精度较低）
python skills/cosmic-entity-list/scripts/gen_entity_list.py hrmp --no-dym-filter

# 包含测试 app 表单（仅在 --no-dym-filter 时生效）
python skills/cosmic-entity-list/scripts/gen_entity_list.py hrmp --no-dym-filter --include-test
```

## 支持的云编码

| 云编码（参数） | 云 Number | 说明 |
|--------------|-----------|------|
| `hrmp` | HRMP | HR 主平台（人员、组织、考勤等） |
| `hr` | HR | HR 核心云 |
| `hdtc` | HDTC | HR 数据中心 |
| `hros` | HROS | HR OS |
| `odc` | ODC | 组织发展云 |
| `opmc` | OPMC | 绩效管理云 |
| `sihc` | SIHC | 社保云 |
| `sit` | SIT | 社会保险云 |
| `ssc` | SSC | 共享服务云 |
| `swc` | SWC | 薪酬云 |
| `tdc` | TDC | 人才发展云 |
| `tsc` | TSC | 培训服务云 |
| `wtc` | WTC | 考勤云 |

> 完整云列表见 `gen_entity_list.py` 中的 `CLOUD_MAP`。

---

## 工作原理

1. **API 查询**：调用 `queryFormsByApp` 按云拉取所有表单（突破 200 上限，按 app 分批 + keyword 前缀分批）
2. **dym 白名单过滤**：扫描 `src/{cloud_key}/**/*.dym`，只保留本地有 dym 源文件的元数据（过滤测试/废弃表单）
3. **物理表映射**：解析每个 dym 中的 `<TableName>`；无 TableName 时沿 `<ParentId>` 继承链递归向上查找祖先物理表
4. **去重折叠**：同一物理表的多个元数据取 formNumber 最短者为「代表元数据」，其余成员折叠到「共享元数据」列，不单独出行
5. **非主数据分类**：对 `BaseFormModel` 按 8 条规则判定是否属于非主数据类（模板→临时表→日志→分录→规则引擎→辅助→配置→码表）
6. **生成 Markdown**：按分类排序输出

---

## 典型使用场景

### 场景 1：初次建立某云的实体清单

```bash
python skills/cosmic-entity-list/scripts/gen_entity_list.py hrmp
```

### 场景 2：多云批量生成

```bash
python skills/cosmic-entity-list/scripts/gen_entity_list.py --all
```

### 场景 3：为 ER 图提供输入数据

生成的 `hrmp_entity_list.md` 是 `cosmic-er` 技能 `scan_references.py` 的输入依赖。
更新实体清单后，需重跑 ER 图生成流程。

---

## 下游消费者

| 消费方 | 使用的输出文件 | 用途 |
|--------|--------------|------|
| `cosmic-er` 技能 | `hrmp_entity_list.md` | 实体分类 + 共享表关系，用于 FK 引用分类和 ER 建模 |
| `cosmic-meta-api` 技能 | `*_entity_list.md` | 元数据文档生成时参考实体清单 |
