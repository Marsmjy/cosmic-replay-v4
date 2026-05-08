# 批量劳动合同续签 (contract_renew_batch) · 部署 SOP

> 协议：金蝶官方资产复用文档 §4.1.2 + dcs-case-run K2 v2.1 实战经验固化
> 适用版本：苍穹 v1.5+

## 准入清单（部署前确认）

| 项 | 要求 | 检查方式 |
|---|---|---|
| 苍穹版本 | v1.5+ | 后台 → 系统信息 |
| 法大大集成 | 已实施 | 客户已能看到 hlcm_contractapplyrenew 列表的 fdd 状态字段 |
| ISV 开发商标识 | 客户已选定（如 bjss） | 与客户确认 |
| ISV 应用编码 | 客户已分配（如 bjss_hlcm_ext） | 苍穹 → 应用管理 |
| ext2 元数据冲突 | 现场无 hlcm_contractapplyrenew_ext / _ext2 同名 | dym 包 grep |
| 预置数据冲突 | hlcm 系列预置数据未提前导入 | SQL 查 t_hr_hlcm_*  count |

## Step 1 · assemble 出客户专属代码

```bash
cd <skills-dir>/cosmic-hr-expert

python scripts/assemble_asset.py \
    --asset contract_renew_batch \
    --isv-flag bjss \
    --biz-app bjss_hlcm_ext \
    --output D:/myproject/contract_renew_batch-bjss/
```

**产物 37 文件**（详见 [_asset_meta.yaml artifacts](_asset_meta.yaml)）：
- 5 java（AdminOrgHrUtils + 3 plugin + 1 op）
- 7 dym + 4 配套元数据（renewbatch + ext2 + biz_app + cld）
- 15 SQL preinsdata
- 6 build 工程文件
- 2 i18n key

**校验**：grep `[占位符]_` / `[占位符]\.` 全部应该 0 命中（占位符指改造者标识·dcs_clean 阶段）。

## Step 2 · 客户环境元数据 diff（防冲突）

```bash
# 检查 hlcm_contractapplyrenew 是否已有冲突 ext2
cd /客户/datamodel/...
ls hlcm_contractapplyrenew_ext*.dym hlcm_contractap_ext*.dym

# 如已有同名 → 必须合并·不能覆盖
diff <现场 ext>.dym  D:/myproject/contract_renew_batch-bjss/datamodel/.../bjss_hlcm_contractap_ext2.dym
```

**冲突解决方案**：
1. 现场无 ext → 直接放进
2. 现场有 ext → 把本资产 4 字段合并到现场 ext·重命名我们的 ext2 文件（避免编译时双扩展冲突）
3. 现场已有同名字段（如 fddsignstatuss） → 用现场字段·删本资产 ext2 同名字段定义

## Step 3 · 跑 15 SQL 预置数据（按需）

```bash
cd D:/myproject/contract_renew_batch-bjss/datamodel/.../preinsdata/

# 部署前先确认客户环境是否已存在
sqlcmd -Q "SELECT COUNT(*) FROM t_hr_hlcm_contract"
# 如已有数据 → 跳过本步骤·避免重复 insert

# 客户为新环境 → 按 SQL 文件名顺序执行
for sql in 0_hlcm_contract.sql 1_hlcm_contractapplynew.sql ...; do
    sqlcmd -i $sql
done
```

⚠ **风险**：15 SQL 包含 hlcm 系列 form 的初始数据·客户已有则**必须跳过**·重复 insert 会主键冲突。

## Step 4 · 部署 5 java 类 + 重启服务

```bash
# 编译 + 打 jar
cd D:/myproject/contract_renew_batch-bjss
gradle build

# 上传 jar 到客户苍穹环境
cp build/libs/bjss_hlcm_ext-1.0.jar /客户/cosmic/lib/

# 重启苍穹后台 + 前端
service cosmic restart
```

**验证 jar 加载成功**：
1. 进 `bjss_hlcm_renewbatch` 主表新增页 → 应能正常打开
2. 进 hlcm_contractapplyrenew 列表 → 工具栏 3 按钮（提交/提交生效/ISV 自建）应能拦截

## Step 5 · 烟测（必跑）

### 烟测 1 · 标准 save 流程

1. 进 `bjss_hlcm_renewbatch` 新增页
2. 选所属组织（如某事业部）
3. 点"选员工" → 弹窗显示该组织 + 子组织员工 → 多选 10 个
4. 保存
5. **验证**：
   - personsize = 10
   - itemids_tag = "id1,id2,...,id10"（10 个逗号分隔）
   - 反查 hlcm_contractapplyrenew → 这 10 条的 `bjss_renewbatch` 字段已写入主表 ID

### 烟测 2 · 校验拦截

1. 选员工时·混入 1 个 billstatus != "C" 的员工
2. 保存 → **应弹窗**：`员工 张三 合同未审核·不能加入续签批次`
3. 主表 + hlcm 都不应有数据写入（事务回滚）

### 烟测 3 · 解绑流程

1. 走完 save → audit 后·点反审批 (wfunaudit)
2. **验证**：所有 10 条 hlcm 的 `bjss_renewbatch` 字段被清 null
3. 再点 invalid（失效）→ 同样清 null（幂等）
4. 最后 delete 主表 → dealUnbindBatch 强解绑·全清

### 烟测 4 · 列表按钮拦截

1. 在 hlcm_contractapplyrenew 列表选 1 行 fillinstatus="1" 的
2. 点"提交生效" → **应弹窗**：`合同填写未完成·不能提交生效`
3. 在列表选 1 行 fdd="A"·signway="B" 的
4. 点"提交" → **应弹窗**：`合同签署状态 待签署 不允许提交`

## Step 6 · 性能压测（推荐·大客户必跑）

| 测试项 | 标准 | 阈值 |
|---|---|---|
| 选员工弹窗加载 | 500 子组织 × 1000 员工 | < 5s |
| 保存批次（100 员工）| 含 beforeExecute 校验 + 写关联 | < 3s |
| 大批次 audit（500 员工）| 完整 op 链 | < 10s |
| dealUnbindBatch（500 已关联）| delete 主表 | < 5s |

性能不达标 → 加索引（详见 [08_impact_analysis.md 性能优化建议](../../scenarios/core_hr_contract_renew/08_impact_analysis.md)）。

## 部署后变更清单（交客户运维）

| 项 | 内容 |
|---|---|
| 新增数据表 | t_hr_bjss_hlcm_renewbatch（主表）+ t_hr_bjss_hlcm_renewbatch_l（语言表）|
| 修改标品表 | hlcm_contractapplyrenew 加 4 字段（走 ext2·**不直接 alter table**）|
| 新增 jar | bjss_hlcm_ext-1.0.jar |
| 新增菜单 | "批量续签" 菜单（客户在 bjss_hlcm_ext 应用下手动挂）|
| 新增按钮 | hlcm_contractapplyrenew 列表加 `bjss_baritemapex1` ISV 自建按钮 |
| 加 i18n 资源 | 2 个 zh_CN + en_US |

## 回滚 SOP

如部署后发现严重问题需要回滚（24 小时内）：

```bash
# 1. 业务先停 → 通知 HR 暂停使用批量续签
# 2. 反向解绑·清所有 hlcm 上的 bjss_renewbatch 字段
sqlcmd -Q "UPDATE t_hr_hlcm_contractapplyrenew_ext2 SET renewbatch = NULL"

# 3. 删主表数据
sqlcmd -Q "TRUNCATE TABLE t_hr_bjss_hlcm_renewbatch"
sqlcmd -Q "TRUNCATE TABLE t_hr_bjss_hlcm_renewbatch_l"

# 4. 卸载 jar
rm /客户/cosmic/lib/bjss_hlcm_ext-1.0.jar

# 5. 删 ext2 字段定义（注意先确认客户没用过这些字段）
# 苍穹后台 → 元数据 → 删 bjss_hlcm_contractap_ext2

# 6. 重启
service cosmic restart
```

## 故障排查（常见 5 类）

| 现象 | 可能原因 | 排查 |
|---|---|---|
| 选员工弹窗为空 | 1. 所属组织未填 / 2. 子组织无员工 / 3. hlcm_contractapplyrenew 全部已关联 | 反查 affiliationord + haos_adminorg 数据 |
| 保存报"员工 X 合同未审核" | 该员工 billstatus != "C" | 让其先走 hlcm 审核 |
| 提交按钮置灰 | fdd != F 且 signway != A | 让相对方签 / 改纸质 |
| 删除主表后 hlcm 关联未清 | dealUnbindBatch 失败（罕见）| SQL 手动 UPDATE renewbatch=NULL |
| getLowerOrgIds 慢（> 5s）| structlongnumber LIKE 全表扫 | 加索引 |

详见 [10_exceptions.md 业务侧排错指南](../../scenarios/core_hr_contract_renew/10_exceptions.md)。

## 关联文档

- 业务规则：[02_business_rules.md](../../scenarios/core_hr_contract_renew/02_business_rules.md)
- 模型设计：[03_model_design.md](../../scenarios/core_hr_contract_renew/03_model_design.md)
- 业务流程：[04_business_flow.md](../../scenarios/core_hr_contract_renew/04_business_flow.md)
- 扩展点：[customization_points.md](customization_points.md)
- W1 真扫：[`dcs_regression/passed/case_002_contract_renew/`](../../../dcs_regression/passed/case_002_contract_renew/)
