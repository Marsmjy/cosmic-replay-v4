# haos_orgfullnameconfig — 行政组织全称规则

**表单编码**: `haos_orgfullnameconfig`  
**表单ID**: `5AJ=1=P7I59K`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_orgfullnameconfig（行政组织全称规则） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_haos_orgfullnameconfig` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity | 行政组织类型映射分录 |
| `（虚拟分录）` | EntryEntity | 管理层级映射分录 |
| `t_haos_ignoreorgtype` | MulEmployeeField子表 | 忽略组织类型 |

### 字段列表 — t_haos_orgfullnameconfig（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| configtype | 配置规则 | RadioOptGroupField | t_haos_orgfullnameconfig.fconfigtype |  |  |
| joinsymbol | 设置拼接符 | ComboField | t_haos_orgfullnameconfig.fjoinsymbol |  |  |
| joinfield | 设置拼接规则 | MulComboField | — |  |  |
| ig_adminorgtype | 忽略组织类型 | MulBasedataField | t_haos_ignoreorgtype（子表） |  |  |
| ig_adminorglayer | 忽略组织层级 | MulBasedataField | — |  |  |
| startlevel | 开始层级 | IntegerField | t_haos_orgfullnameconfig.fstartlevel | ✓ |  |
| orgtype | 行政组织类型映射分录 | EntryEntity | → （虚拟分录） |  |  |
| orglayer | 管理层级映射分录 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — （行政组织类型映射分录·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （管理层级映射分录·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_orgfullnameconfig（主表） | 6 |
| （行政组织类型映射分录） | 4 |
| （管理层级映射分录） | 4 |
| t_haos_ignoreorgtype（MulEmployeeField子表） | 1 |
| 无数据库列 | 2 |

