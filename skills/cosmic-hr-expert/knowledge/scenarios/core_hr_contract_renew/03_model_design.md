# core_hr_contract_renew · 数据模型

> 涉及 2 个 ISV 表 + 1 个核心标品表（被反向扩展）+ 8 个标品 form（preinsdata 关联）

## 模型分层全景

```
┌─────────────────────────────────────────────────────────────┐
│ 入口层 · ISV 主表                                            │
│   ${ISV_FLAG}_hlcm_renewbatch (批量续签记账单)               │
│     字段：${ISV_FLAG}_itemids_tag / _personsize             │
│           / _affiliationord / _invalid                      │
└──────────────────────┬──────────────────────────────────────┘
                       ↓ 通过 ${ISV_FLAG}_itemids_tag 关联
┌─────────────────────────────────────────────────────────────┐
│ 核心层 · 标品被扩展                                          │
│   hlcm_contractapplyrenew (合同续签申请单)                  │
│     ISV 加字段（走 ext2 扩展）：                             │
│     ${ISV_FLAG}_renewbatch (反向引用·BasedataField)         │
│     ${ISV_FLAG}_fddsignstatuss (法大大签署状态)             │
│     ${ISV_FLAG}_signwaynew (签署方式)                       │
│     ${ISV_FLAG}_fillinstatus (合同填写状态)                 │
└──────────────────────┬──────────────────────────────────────┘
                       ↓ 完成续签
┌─────────────────────────────────────────────────────────────┐
│ 数据层 · 合同主表 + 协议表（标品·preinsdata 关联）           │
│   hlcm_contract / hlcm_contractapplynew/end/change/cancel   │
│   hlcm_contractfileemp / hlcm_contractfileother             │
│   hlcm_empprotocolnew / hlcm_empprotocolrelieve / terminate │
│   hlcm_otheragreements                                      │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ 查询层 · 行政组织                                            │
│   haos_adminorg (行政组织·只读)                              │
│     按 structlongnumber 模糊匹配查子组织                     │
└─────────────────────────────────────────────────────────────┘
```

## 实体清单（按层）

### 1. ISV 自建 (2)

| form | 关键字段 | 用途 |
|---|---|---|
| `${ISV_FLAG}_hlcm_renewbatch` | `${ISV_FLAG}_itemids_tag` (string·逗号分隔 ID) / `_personsize` (int) / `_affiliationord` (BasedataField → bos_org) / `_invalid` (boolean) | 批量续签记账单·主表·一条记录管 N 员工 |
| `${ISV_FLAG}_hlcm_contractap_ext2` | (扩展元数据·见下方 hlcm_contractapplyrenew 字段) | ISV 给标品 hlcm_contractapplyrenew 加字段（PR-001 走 hsv_extension） |

### 2. 标品被扩展 (1·核心)

| form | 标品自有字段 | ISV 加字段 | 用途 |
|---|---|---|---|
| `hlcm_contractapplyrenew` | `id, billstatus, handlestatus, auditstatus, empnumber, affiliationord` | `${ISV_FLAG}_renewbatch / _fddsignstatuss / _signwaynew / _fillinstatus` | 合同续签申请单·标品 |

### 3. 标品·preinsdata 预置关联 (10)

| form | 预置类型 | 用途 |
|---|---|---|
| `hlcm_contract` | 合同主表 | 续签前后跟踪 |
| `hlcm_contractapplynew` | 合同新签申请单 | 同系列·业务规则参考 |
| `hlcm_contractapplyend` | 合同到期申请单 | 续签前置·到期触发 |
| `hlcm_contractapplychange` | 合同变更申请单 | 续签前置·变更场景 |
| `hlcm_contractapplycancel` | 合同取消申请单 | 异常路径 |
| `hlcm_contractfileemp` | 合同员工档案 | 续签后归档 |
| `hlcm_contractfileother` | 合同其他档案 | 同上 |
| `hlcm_empprotocolnew` | 员工协议新签 | 关联协议 |
| `hlcm_empprotocolrelieve` | 员工协议解除 | 异常路径 |
| `hlcm_empprotocolterminate` | 员工协议终止 | 异常路径 |
| `hlcm_otheragreements` | 其他协议 | 同上 |

### 4. 标品·只读引用 (1)

| form | 用途 | 引用方式 |
|---|---|---|
| `haos_adminorg` | 行政组织 | `AdminOrgHrUtils.getLowerOrgIds` 模糊匹配 structlongnumber·返子组织 |

## 字段命名规则（ISV 隔离·PR-001）

| 字段族 | 前缀 | 例子 |
|---|---|---|
| ISV 自建表字段 | `${ISV_FLAG}_` | `${ISV_FLAG}_itemids_tag` / `_personsize` |
| ISV 加在标品上的字段 | `${ISV_FLAG}_` | `${ISV_FLAG}_renewbatch` 加在 hlcm_contractapplyrenew |
| ISV 自建按钮控件 | `${ISV_FLAG}_` | `${ISV_FLAG}_billlistap` / `_baritemapex1` |

⚡ **铁律**：禁止 ISV 直接 modifyMeta 标品 form 加字段·必须走 ext 扩展元数据（如 ext2.dym）。

## 字段值枚举（状态机）

### `${ISV_FLAG}_fddsignstatuss`（法大大签署状态）
- `"A"` 待签署
- `"E"` 待相对方签署
- `"F"` 已完成

实证锚点：ContractOtherExListPlugin.java:151,162,210-211

### `${ISV_FLAG}_signwaynew`（签署方式）
- `"A"` 纸质签署
- `"B"` 电子签署（默认）

实证锚点：ContractOtherExListPlugin.java:151,157

### `${ISV_FLAG}_fillinstatus`（合同填写状态）
- `"1"` 填写中
- `"2"` 已完成

实证锚点：ContractOtherExListPlugin.java:110-111

## 跨云引用（ADR-009 跨云穿透）

| 来源云 | 来源 form | 引用 | 说明 |
|---|---|---|---|
| 核心人力云 (core_hr) | `hlcm_contractapplyrenew` 等 hlcm 系列 | 主消费 | 本场景核心 |
| 组织发展云 (org_dev) | `haos_adminorg` | 只读·查 structlongnumber | 选员工时按组织过滤 |

## ISV 元数据归属（4 铁律）

| 字段 / form | 归属 | 不能怎么动 |
|---|---|---|
| `${ISV_FLAG}_hlcm_renewbatch` 全部字段 | ISV 自建 | ✅ 完全自主·可加可删可改 |
| `hlcm_contractapplyrenew.${ISV_FLAG}_*` 4 个字段 | ISV 扩展 (走 ext2) | ✅ 走扩展·不直接改标品 dym |
| `hlcm_contractapplyrenew` 标品字段 | 标品 | ❌ ISV 不能改 billstatus/auditstatus 等核心字段 |
| `haos_adminorg` | 标品 | ❌ 只读 |

## 模型扩展点

5 个常见扩展点（详见 customization_points.md）：

- **EP-01** 改"选员工"过滤维度（不只 affiliationord·按职等/雇佣类型等）
- **EP-02** 加 ISV 主表自定义字段
- **EP-03** 改"按子组织递归"逻辑（如限定 N 层）
- **EP-04** 接入其他签署系统（替代法大大）
- **EP-05** 改 7 op 行为（如 audit 时不强制校验 billstatus="C"）
- **EP-06** 加批次审批工作流
