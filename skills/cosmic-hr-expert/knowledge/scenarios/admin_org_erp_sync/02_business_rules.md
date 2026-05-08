# admin_org_erp_sync · 业务规则

> **聚合场景**：ERP 组织同步（1 个子实体）
> **生成时间**：2026-04-30
> **方法**：从 `_shared/_standard_metadata/entity_metadata/<entity>.md` 提取每子实体真字段元数据 → 标品 HRBaseDataTplEdit 共性规则 + 子实体特有约束

## 一、共性规则（HRBaseDataTplEdit 标品模板 · 12 实体共用）

本聚合场景所有子实体均继承 `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` 标品模板，自带规则：

| 规则 ID | 触发点 | 行为 | ISV 是否可改 |
|---|---|---|:---:|
| BR_TPL_1 | 表单加载 (afterBindData) | 自动加载基础资料元数据 + 渲染字段 | ❌ 标品 |
| BR_TPL_2 | save 操作 | 触发 CodeRuleOp 自动生成 number 字段（按编码规则） | ❌ 标品 · 规则在元数据里配置 |
| BR_TPL_3 | save 操作 | HRBaseDataStatusOp · 设置 status 字段（A/B/C 状态机） | ❌ 标品 |
| BR_TPL_4 | save 操作 | HRBaseOriginalOp · 维护 orinumber/oriname/oristatus 出厂数据 | ❌ 标品 |
| BR_TPL_5 | enable / disable | HRBaseDataEnableOp · 维护 enable 字段 + disabledate/disabler | ❌ 标品 |
| BR_TPL_6 | save 操作 | HRBaseDataLogOp · 写变更日志（按 HRBaseDataConfigUtil 配置启用） | ❌ 标品 |

> ⚠️ ISV 不应继承 HRBaseDataTplEdit · 应**并列挂** `HRDataBaseEdit` 实现自定义逻辑（PR-001）

## 二、子实体特有规则（按字段提取）

### `homs_orgdifftemp` · ERP组织同步工具

**字段总数**：43

**必填字段（7）**：
- `number` (行政组织编码) · `TextField`
- `name` (行政组织名称) · `MuliLangTextField`
- `index` (排序号) · `IntegerField`
- `parentorg` (上级行政组织) · `BasedataField` → ref homs_parentorgdifftemp
- `adminorgtype` (行政组织类型) · `BasedataField` → ref haos_adminorgtype
- `bsed` (生效日期) · `DateField`
- `org` (组织体系管理组织) · `BasedataField` → ref bos_org

**引用基础资料（13）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `disabler` (禁用人) → `bos_user` · `UserField`
- `industrytype` (行业类别) → `hbss_industrytype` · `BasedataField`
- `parentorg` (上级行政组织) → `homs_parentorgdifftemp` · `BasedataField`
- `adminorgtype` (行政组织类型) → `haos_adminorgtype` · `BasedataField`
- `corporateorg` (法律实体) → `hbss_lawentity` · `BasedataField`
- `adminorglayer` (管理层级) → `haos_adminorglayer` · `BasedataField`
- `adminorgfunction` (行政组织职能) → `haos_adminorgfunction` · `BasedataField`
- `companyarea` (国家地区) → `bd_country` · `BasedataField`
- … 共 13 个引用

**标品公共字段**：`number`, `name`, `status`, `creator`, `modifier`, `enable`, `createtime`, `modifytime`, `masterid`, `issyspreset`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

## 三、关键约束（共 12 实体）

| 约束 | 适用实体 | 来源 |
|---|---|---|
| `number` 唯一性 | 全部 12 实体 | 标品 CodeRuleOp + 元数据 UniqueValidation |
| `enable` 默认值 = '1'（启用） | 全部 12 实体 | HRBaseDataEnableOp |
| `status` 状态机 (A 暂存 → B 待审核 → C 已审核) | 全部 12 实体 | HRBaseDataStatusOp |
| `disabler` / `disabledate` 自动维护 | 全部 12 实体 | HRBaseDataEnableOp |

---

**精修元数据**：
- 生成器：`scripts/polish_aggregate_scene.py`
- 数据源：12 子实体的 `_shared/_standard_metadata/entity_metadata/<entity>.md`
- 标品共性来自 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` / `HRBaseDataEnableOp` / `HRBaseOriginalOp` / `HRBaseDataLogOp` 反编译实证