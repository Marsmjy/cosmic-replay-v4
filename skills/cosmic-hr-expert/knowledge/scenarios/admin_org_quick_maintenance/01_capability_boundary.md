# 能力边界 · 行政组织快速维护

> **状态**: 🟢 基于 `platform/openapi_capability_map.md` + `domain/org/real_world_scenarios.md` 整合
> **confidence**: verified

---

## 一、10 条平台级硬规则（必记）

这些规则**跨场景适用**，是定制开发的硬边界：

| # | 规则 | 影响 |
|---|---|---|
| 1 | `number` 字段创建后**不可修改** | 下游所有引用都绑定 number |
| 2 | 字段 key ≤ **24** 字符 | 数据库列名上限 |
| 3 | 实体 key ≤ **36** 字符 | 数据库表名上限 |
| 4 | 表名 ≤ **25** 字符 | 数据库硬上限 |
| 5 | 系统计算字段禁止手改 | `level` / `longnumber` / `structlongnumber` / `belongcompany` |
| 6 | `errorCode="0"` ≠ 成功 | 必须二次验证（`getFormSchema`/`list_rules`） |
| 7 | `op` 枚举**只 4 值** | `add` / `modify` / `remove` / `move` |
| 8 | `actionType` / `fieldKey` 必须 **PascalCase** | 小写静默忽略 |
| 9 | `updateOperation.plugins` 是**全量替换** | 必须先 get 再 append |
| 10 | `registerPlugin.targetType` 必须**大写枚举** | 5 值: `BILL_FORM`/`LIST_FORM`/`MOBILE_BILL_FORM`/`OPERATION`/`EVENT` |

---

## 二、✅ 标品原生支持

### 列表展示
- ✅ 树形结构展示（层级可折叠，最大 10 级）
- ✅ 分页查询（默认 20 条/页，可配至 50）
- ✅ 按组织编码、名称、状态筛选
- ✅ 列自定义（列表配置界面）
- ✅ 导出 Excel
- ✅ 权限过滤（按用户管辖组织）
- ✅ 4 时态视图（当前/过去/未来/初始）

### 新增行政组织
- ✅ 单个新增，向导式填写
- ✅ 必填字段：编码/名称/上级/类型/生效日期
- ✅ 编码自动生成选项（可通过插件定制规则）
- ✅ 保存即校验（beforeSave 链）
- ✅ 同步生成 `haos_adminorgdetail` / `haos_adminorghis` 记录

### 信息变更
- ✅ 修改名称、描述、负责人等非关键字段
- ✅ 自动记录变更历史（`haos_adminorghis`）
- ✅ 有效期管理（`bsed` / `expiry_date`）
- ✅ 支持"修订"（纠错）vs "调整"（产生新版本）

### 调整上级
- ✅ 单个组织调整上级
- ✅ 自动重算 `level` / `longnumber` / `belongcompany`
- ✅ 自动刷新下属岗位的归属
- ✅ 前端选择树自动排除后代节点（防循环引用）

---

## 三、❌ 标品不支持（需定制）

- **批量新增**: 不支持一次性创建多个组织（需定制批量插件）
- **批量调整上级**: 逐个调整，200+ 组织时性能差（建议走 `homs_orgbatchc`）
- **组织合并**: 合并两个组织为一个，需大量数据迁移
- **组织拆分**: 一个组织拆成多个，历史数据归属复杂
- **跨租户移动**: 标品仅支持同租户内调整
- **回溯生效**: 无法让变更在过去某个日期生效（bsed 只能当前或未来）
- **自定义审批流**: 标品审批流是预设的，高度定制需要 bos 工作流模块

---

## 四、🔧 可通过配置实现（无需代码）

### 列表配置
- 默认显示列、列宽、排序: **管理 → 列表配置**
- 默认筛选条件: **同上**
- 最大分页: 配置项 `list.default.page.size`

### 表单配置
- 新增表单的字段可见性、只读性: **表单设计器**
- 字段默认值: **设计器属性面板**
- 字段分组布局: **容器配置**

### 业务配置
- 组织类型字典 (`haos_adminorgtype`): **基础数据维护**
- 组织层次字典 (`haos_adminorglayer`): **同上**
- 组织职能字典 (`haos_adminorgfunction`): **同上**
- 变动场景/原因: **变动单配置**
- 编码规则（前缀、步长等）: **基础服务云 → 公共设置**

### 审计配置
- 变更日志字段范围: **审计配置**
- 操作日志开关: **系统设置**

---

## 五、💻 必须通过插件扩展

| 需求类型 | 推荐扩展点 | Pattern |
|---|---|---|
| 编码自动生成规则 | `beforeSave@haos_adminorg` | - |
| 跨字段联动校验 | `beforeSave@haos_adminorg` | add_unique_validation |
| 调整上级时自定义通知 | `onParentChange@haos_adminorg` | - |
| 组织列表按权限过滤 | `onListQuery@haos_adminorgtablist` | - |
| 删除前业务检查 | `beforeDelete@haos_adminorg` | - |
| 反写其他模块 | `afterSave@haos_adminorg` + 事件订阅 | - |
| 扩展字段 | `modifyMeta op=add field` | add_field_extension |
| 字段级联到调整单 | `modifyMeta × 4` | orgbill_4prefix_cascade |

---

## 六、📊 能力矩阵

| 子场景 | 列表展示 | 新增 | 变更 | 调整上级 |
|---|---|---|---|---|
| **标品单个** | ✅ | ✅ | ✅ | ✅ |
| **批量操作** | ⚠️ 仅导出 | ❌ | ❌ | ⚠️ 走 orgbatchc |
| **历史追溯** | ✅ 自带版本 | - | ✅ | ✅ |
| **跨租户** | - | ❌ | ❌ | ❌ |
| **审批流** | - | ⚙️ 可配 | ⚙️ 可配 | ⚙️ 必配（敏感） |
| **扩展字段** | ✅ 显示 | ✅ 录入 | ✅ 编辑 | ⚠️ 需 4 前缀同步 |

---

## 七、🚨 标品限制（重要）

- 组织层级深度**最多 10 级**（系统参数可调整，但 UI 渲染性能会降）
- 单次事务内关联修改**不超过 1000 条下属数据**，否则锁表
- 组织编码一旦创建**不允许修改**（唯一性约束）
- 失效组织不能添加新岗位
- 同一租户只允许一个"集团"级根组织
- 修改 `haos_adminorg` 必须通过 API / designer，**禁止直接 SQL 改表**

---

## 八、扩展对象选择决策树

```
我要改什么？
    │
    ├─ 字段/业务规则  →  扩展 haos_adminorg (主)
    │
    ├─ 列表显示规则   →  扩展 haos_adminorgtablist
    │
    ├─ 调整申请审批  →  扩展 homs_orgchgbill + 4前缀分录
    │
    ├─ 跨模块联动    →  afterSave@haos_adminorg + 事件订阅
    │
    └─ 不知道         →  先看 07_ext_points.md
```

**⚠️ 不要扩展**: `haos_adminorgdetail` / `haos_adminorghis`（这是视图层）

---

## 九、字段类型约束（平台级）

### 推荐使用
- `TextField` / `MuliLangTextField` - 文本
- `DateField` - 日期
- `BasedataField` - 引用（含引用人员: `basedataNumber="hrpi_employeenewf7query"`）
- `ComboField` - 枚举（必须带 `comboOptions`）
- `IntegerField` / `DecimalField` - 数字

### ❌ 禁用
- `EmployeeField` - buildMeta 枚举不支持（用 BasedataField 替代）
- `MultiLangTextField` - 拼写错误（正确是 `MuliLangTextField`）
- `RadioGroup` - 已废弃（用 `RadioOptGroupField`）

详见 `cosmic_realworld_traps/buildmeta_traps.md`

---

## 十、OpenAPI 覆盖度（2026-04-18 实测）

| 操作 | OpenAPI 支持 | 备注 |
|---|---|---|
| buildMeta 建新实体 | ✅ | 74 值 FieldType 枚举 |
| modifyMeta 改元数据 | ✅ | op 4 值 |
| addRule 加规则 | ✅ | 三层规则（formRule/bizRule/Validation） |
| registerPlugin 注册插件 | ✅ | 5 种 targetType |
| updateOperation 改操作 | ✅ | plugins/validations 全量替换 |
| EmbedFormAp 嵌入 | ⚠️ 部分 | 员工档案有特殊限制 |
| PDM 同步 | ⚠️ | 需跨 server/datamodel 两套环境 |
| 权限 SQL 预置 | ❌ | bulkInsert 未开放 |
| DYM 导入 | ❌ | 需走管理界面 |

---

**📌 来源追溯**：
- 10 条硬规则: `cosmic_realworld_traps/` + `platform/openapi_capability_map.md`
- 标品能力矩阵: `knowledge/domain/org/ontology.md`
- OpenAPI 覆盖度: `knowledge/platform/openapi_capability_map.md`
- 实测纠偏: `knowledge/domain/org/real_world_scenarios.md`
