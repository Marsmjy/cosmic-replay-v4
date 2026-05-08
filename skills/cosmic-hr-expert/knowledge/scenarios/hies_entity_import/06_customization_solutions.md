# hies_entity_import · 定制方案（HR 实体导入·HIES-Pro）

> **聚合场景**：HR 实体导入（HIES-Pro）· 苍穹标品业务级导入工具
> **核心价值**：标品已包揽 Excel 解析 / 字段映射 / 校验规则 / 错误回滚 / 大批量分批 / 进度监控等基础设施·ISV 仅做"配置 + 注册"
> **触发关键词**：组织批量导入 / HR 实体导入 / HIES / HIES-Pro / 导入导出模板 / 数据导入
> **更新时间**：2026-05-07（v1.0.3 加 CS-00 完整 SOP）

---

## CS-00 · HIES-Pro 配置化定制（⭐ 最高优先 · 0 行 Java · 4 步 SOP）

> 当用户选了"HR 导入导出（HIES-Pro）"路径后·**必须给完整 4 步配置 SOP**·不允许只回"用 HIES-Pro"就完事。
> 触发反模式 **AP-024**："选了 HIES 后没给具体配置 SOP" 直接拒绝默认。

### 业务场景

- 客户要"组织批量导入变更" / "员工档案批量导入" / "薪资档案批量导入"
- 任何"我要批量导入 X 数据·走 Excel"类需求

### HIES-Pro 标品包揽（ISV 不重写）

| 能力 | 实现方 |
|---|---|
| Excel 文件上传 | 标品 |
| 字段映射 | 标品（hies_diaetplconf 配置）|
| 校验规则（必填/格式/字典/外键）| 标品 |
| 数据清洗 / 去重 | 标品 |
| 大批量分批 | 标品（默认 5000 行/批·可配）|
| 错误行回滚 + 错误清单导出 | 标品 |
| 任务进度监控 | 标品（hies_taskinfo）|
| 异步任务 + 邮件通知 | 标品 |

### ISV 4 步配置 SOP

#### Step 1 · 扩展点配置（hies_diaesysparam）

注册自己的业务对象到 HIES-Pro：

```
入口：苍穹后台 → 系统服务云 → HR 业务模型工具 → HIES 扩展点配置
菜单：hies_diaesysparam

操作：
1. 新增扩展点
   - 业务对象 = 你的 form（如 ${ISV_FLAG}_my_entity）
   - 业务模块 = 你的 ISV 应用编码
   - 启用状态 = 启用
2. 配置导入入口（可选 · 决定从哪几个菜单能触发导入）
3. 配置导出权限（按角色/部门隔离）
4. 保存
```

#### Step 2 · 模板配置（hies_diaetplconf）

定义 Excel 字段 ↔ form 字段映射：

```
入口：苍穹后台 → 系统服务云 → HR 业务模型工具 → HIES 模板配置
菜单：hies_diaetplconf

操作：
1. 新增模板
   - 模板名称 = "${ISV_FLAG} 业务对象批量导入模板"
   - 关联业务对象 = Step 1 注册的 form
2. 配置字段映射（每行 1 个字段）
   - Excel 列名（如 "员工编号"）→ form 字段 key（如 number）
   - 数据类型（文本/数值/日期/字典/外键）
   - 是否必填
   - 字典/外键值（如有）→ 配查找配置
3. 配置校验规则（可选）
   - 唯一性（如员工编号不重）
   - 格式（如手机号正则）
   - 联动（如部门必须属于公司）
4. 保存 + 启用模板
5. 下载 Excel 模板（标品自动生成空模板·客户填数据再上传）
```

#### Step 3 · HR 业务模型工具挂导入按钮

```
入口：苍穹后台 → 系统服务云 → HR 业务模型工具 → 通用配置 → 列表配置
       → 选你的业务对象 form → 点"导入导出"按钮配置

操作：
1. 启用"导入"按钮（标品自动连接 HIES-Pro）
2. 启用"导出"按钮（标品自动）
3. 关联 Step 2 创建的模板
4. 保存 + 发布
```

#### Step 4 · 运行时监控（hies_taskinfo）

```
入口：苍穹后台 → 系统服务云 → HR 业务模型工具 → HIES 任务列表
菜单：hies_taskinfo

操作：
- 实时看每个导入任务的进度（处理中/成功/失败/部分失败）
- 失败任务可重跑·或下载错误清单 Excel 给业务人员修
- 导入历史归档（标品自动·可保留 N 天）
```

### 4 步完成后的验收

- [ ] 列表页有"导入"按钮·点能弹出 Excel 上传对话框
- [ ] 点"下载模板" 能拿到含字段表头的空 Excel
- [ ] 上传含错误数据的 Excel·能回滚 + 给错误清单
- [ ] hies_taskinfo 里能看到任务记录
- [ ] 导出按钮也能正常工作（导出选中行 / 全部 / 按筛选条件）

### 反指引

- ❌ **AP-009 自写 ExcelImportPlugin 解析 xlsx**：标品已全包揽·不要重写
- ❌ **AP-005 继承 HRBaseDataImportEditPlugin / HRBaseDataTplEdit**：HIES 模板类禁继承·只配置不写代码
- ❌ **AP-024 选了 HIES 后没给配置 SOP**：必须出 Step 1-4 完整指引

---

## CS-01 · 极少数高级场景才用：注册自定义 SPI 处理器（⚠️ 仅在 4 步配置不够时）

如果客户需求确实超出 hies_diaetplconf 的字段映射能力（如需要复杂的"多表关联导入"）·**才考虑**继承 HIES 提供的 SPI：

```java
// 仅当 Step 1-4 完成后·业务规则配置仍解决不了"复杂业务流处理"时使用
@SdkPlugin
public class ${ISV_FLAG}MyEntityImportSpi implements IHIESCustomImportProcessor {
    @Override
    public void beforeImport(ImportContext context) {
        // 自定义预处理（如调外部接口校验）
    }

    @Override
    public ImportResult afterRowImport(DynamicObject row, int rowIndex) {
        // 自定义行级处理（如批量补全关联数据）
    }
}
```

⚠️ 大多数项目用不到这一步·先确认 Step 1-4 + 业务规则配置真不够·才下沉。

---

## 关联场景

- `hies_diaesysparam` · 扩展点配置（Step 1 入口）
- `hies_diaetplconf` · 模板配置（Step 2 入口）
- `hies_taskinfo` · 任务监控（Step 4 入口）
- `hies_multientry_tpl` · 多分录模板·复杂导入用
- `hies_mgrmonitor` · 管理员监控视图

## 关联反模式

- AP-005 · 继承 HRBaseDataImportEditPlugin（HIES 模板类禁继承）
- AP-009 · 自写 ExcelImportPlugin 解析 xlsx（标品已包揽）
- AP-024 · 选了 HIES 后没给具体配置 SOP（v1.0.3 加）

## 跨云影响

- HIES-Pro 是平台级工具·覆盖 5 大云所有"基础资料 / 单据"导入需求
- 配置好后·整个 HR 域共享同一套 import 基础设施
- 标品后续升级 HIES（如新增校验类型）→ ISV 0 改动跟随
