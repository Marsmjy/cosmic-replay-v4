# 人员履历Word打印 (downermanfileword) · 部署 SOP

> 协议：金蝶官方资产复用文档 §4.1.2 + dcs-case-run K2 v2.1 实战经验固化
> 适用版本：苍穹 v1.5+

## 准入清单（部署前确认）

| 项 | 要求 | 检查方式 |
|---|---|---|
| 苍穹版本 | v1.5+ | 后台 → 系统信息 |
| 所属云 / 应用 | core_hr / hspm 已部署 | 应用管理 → 检查 |
| ISV 开发商标识 | 客户已选定（如 bjss） | 与客户确认 |
| ISV 应用编码 | 客户已分配（如 bjss_hspm_ext） | 苍穹 → 应用管理 |
| ext2 元数据冲突 | 现场无同名 ext 字段冲突 | dym 包 grep |
| 预置数据冲突 | 预置数据未提前导入 | SQL 查 count |

## Step 1 · assemble 出客户专属代码

```bash
cd <skills-dir>/cosmic-hr-expert

python scripts/assemble_asset.py \
    --asset downermanfileword \
    --isv-flag bjss \
    --biz-app bjss_hspm_ext \
    --output D:/myproject/downermanfileword-bjss/
```

**产物**（详见 [_asset_meta.yaml artifacts](_asset_meta.yaml)）：
- 8 java 类
- 15 datamodel 文件
- 工程文件 + i18n + 部署 SOP

**校验**：grep `[占位符]_` / `[占位符]\.` 全部应该 0 命中（占位符指改造者标识）。

## Step 2 · 客户环境元数据 diff（防冲突）

```bash
# 检查现场是否已有同名 ext 元数据
cd /客户/datamodel/...

# 如已有同名 → 必须合并·不能覆盖
diff <现场 ext>.dym D:/myproject/downermanfileword-bjss/datamodel/.../*.dym
```

**冲突解决方案**：
1. 现场无 ext → 直接放进
2. 现场有 ext → 把本资产新增字段合并到现场 ext·重命名我们的 ext 文件
3. 现场已有同名字段 → 用现场字段·删本资产 ext 同名字段定义

## Step 3 · 跑预置数据 SQL（按需）

⚠ **风险**：客户已有预置数据时**必须跳过**·重复 insert 会主键冲突。

```bash
# 部署前先确认客户环境是否已存在
sqlcmd -Q "SELECT COUNT(*) FROM <主表>"
# 如已有数据 → 跳过本步骤
```

## Step 4 · 部署 java 类 + 重启服务

```bash
cd D:/myproject/downermanfileword-bjss
gradle build

# 上传 jar 到客户苍穹环境
cp build/libs/bjss_hspm_ext-1.0.jar /客户/cosmic/lib/

# 重启苍穹后台 + 前端
service cosmic restart
```

## Step 5 · 烟测（必跑）

### 烟测 1 · 标准业务流程

⚡ 按业务真实场景设计·参考 W1 标杆 [contract_renew_batch deploy_sop.md](../contract_renew_batch/deploy_sop.md)

### 烟测 2 · 校验拦截

⚡ 按业务异常路径设计·参考 [10_exceptions.md](../../scenarios/<scene>/10_exceptions.md)

### 烟测 3 · 反向流程（如有）

⚡ 按 wfunaudit / invalid / delete 等反向 op 设计

## Step 6 · 性能压测（推荐·大客户必跑）

| 测试项 | 标准 | 阈值 |
|---|---|---|
| 主流程响应时间 | 单次 op | < 3s |
| 大批量场景 | 100+ 记录 | < 10s |
| 跨表查询 | 复合 QFilter | < 5s |

性能不达标 → 加索引（详见 [08_impact_analysis.md 性能优化建议](../../scenarios/<scene>/08_impact_analysis.md)）。

## 部署后变更清单（交客户运维）

| 项 | 内容 |
|---|---|
| 新增数据表 | bjss_downermanfileword 主表 + 语言表 |
| 修改标品表 | 走 ext 扩展元数据·**不直接 alter table** |
| 新增 jar | bjss_hspm_ext-1.0.jar |
| 新增菜单 | (按需手动挂) |

## 回滚 SOP

如部署后发现严重问题需要回滚（24 小时内）：

```bash
# 1. 业务先停 → 通知 HR 暂停使用本功能
# 2. 反向解绑·清所有 hlcm 上的字段
# 3. 删主表数据
# 4. 卸载 jar
# 5. 删 ext 字段定义
# 6. 重启
service cosmic restart
```

## 故障排查

详见 [10_exceptions.md 业务侧排错指南](../../scenarios/<scene>/10_exceptions.md)。

## 关联文档

- 业务规则：[02_business_rules.md](../../scenarios/<scene>/02_business_rules.md)
- 模型设计：[03_model_design.md](../../scenarios/<scene>/03_model_design.md)
- 业务流程：[04_business_flow.md](../../scenarios/<scene>/04_business_flow.md)
- 扩展点：[customization_points.md](customization_points.md)
- W1 标杆：[contract_renew_batch deploy_sop.md](../contract_renew_batch/deploy_sop.md)
