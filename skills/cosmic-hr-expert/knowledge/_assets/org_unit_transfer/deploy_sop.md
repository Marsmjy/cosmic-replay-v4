# 成建制划转 · 部署 SOP

> 协议：金蝶官方"成建制划转复用说明书.docx"v1.0 (2025-08-04 梅小雪/宁杰) §4.1.2
> 本 SOP = docx §4.1.2 原文 + dcs-case-run K2 v2.1 case_001 实战经验固化

## 前置准备

| 项 | 说明 |
|---|---|
| 苍穹环境 | HR 核心人力 + hdm 调配管理 应用已部署 |
| 客户开发商标识 | `${ISV_FLAG}`·客户提供（如 `bjss` / `cds0` / 等） |
| ISV 应用包 | `${BIZ_APP}`·命名建议 `${ISV_FLAG}_hdm_ext` |
| 苍穹环境 BizappId | 部署时获取真值（跑 `cosmic-env list_apps`）·**不能脑补** |
| MC 服务地址 | 部署侧准备好（gradle.properties 里 MCServerURL） |
| mservice 部署模式 | 6.0 还是 7.0 domain 模式（影响下面步骤 5）|

## 部署 6 步法（按 docx §4.1.2 + 实战）

### Step 1 · assemble 资产

```bash
python scripts/assemble_asset.py \
  --asset org_unit_transfer \
  --isv-flag ${ISV_FLAG} \
  --biz-app ${BIZ_APP} \
  --output D:/myproject/${ASSET_ID}-${ISV_FLAG}/
```

产物：完整工程 5 java + 7 datamodel + 6 工程文件 + README + 本文档。

### Step 2 · 检查目标 BizappId 是否被现场扩展过

> docx §4.1.2 引用：
> "检查目标应用 xx 以及元数据 xxxx 是否被扩展·任职经历基础页面 hrpi_empposorgrel·若已扩展应用或者元数据·内容不多可考虑备份删除·然后进行到 MC 补丁升级。若不方便删除·则可考虑导出当前环境已扩展的 xx 应用（包含元数据）·查看 `<BizappId>` 标签的值·例如：1WXBXYCLS05D"

实操：
```bash
# 1. 查 ${BIZ_APP} 是否已存在
cosmic-env query_app --number ${BIZ_APP}

# 2. 查 hrpi_empposorgrel 是否被扩展
cosmic-env query_form_extensions --form hrpi_empposorgrel

# 3. 若已扩展·导出现场环境的 BizappId 真值
# 真值会在第 3 步用到
```

### Step 3 · 替换 BizappId 真值

> docx §4.1.2 引用：
> "在 dm 包元数据开发商标识都替换的基础上·将所有二开 dym 文件中 `<BizappId>46XU=YL75E1+</BizappId>` 应用 id 更改为现场环境已扩展 xx 应用 `<BizappId>` 的值 1WXBXYCLS05D"

实操：
```bash
# 把所有 dym 文件里 <BizappId>${BIZ_APP_ID_PLACEHOLDER}</BizappId> 替换为真值
find datamodel -name '*.dym' -exec sed -i 's|<BizappId>${BIZ_APP_ID_PLACEHOLDER}</BizappId>|<BizappId>${BIZ_APP_ID_REAL}</BizappId>|g' {} \;
```

### Step 4 · MC 升级补丁导入 dm 元数据

> docx §4.1.2 引用：
> "将更新开发商标识的补丁包通过 MC 升级或者手动按应用导入到开发环境中·完成元数据的升级"

```bash
# 6.0 环境
1) 打 dm 补丁包（用 idea cosmic 插件 buildDm 或 gradle）
2) 登录 MC 控制台·上传 hr-${BIZ_APP}-<时间戳>.zip
3) 不重启·进 step 5

# 7.0 domain 环境
1) 把 ${ASSET_LIB}.lib 手动放到 mservice 物理机的 /var/appstatic/appstore/cosmic 目录
2) 通过 MC 补丁升级 hr-${BIZ_APP}-<时间戳>.zip
```

### Step 5 · 配置 mservice 环境变量 + 重启

| 环境 | 操作 |
|---|---|
| 6.0 | 登录 gpass·修改 mservice 环境变量·添加 `CUSLIBS=${ASSET_LIB}` |
| 7.0 domain | 登录 gpass·修改 mservice 环境变量·添加 `libs=${ASSET_LIB}.lib` |

注：`${ASSET_LIB}` 默认 `hdm-orgtransfer`·跟模板生成的 buildJar 输出一致。

```bash
# 重启 mservice
gpass restart mservice
```

### Step 6 · 配置调度计划 + 启用

```bash
# 进入苍穹 → 系统服务云 → 调度服务 → 调度计划
# 1. 找 hdm_orgtransfer_plan_SKDP_S（已随 dm 包导入）
# 2. 启用三子任务：
#    - 成建制划转（组织上级调整）作业 → ${ISV_FLAG}.hr.hdm.orgtransfer.business.OrgParentChgTask
#    - 成建制划转（组织更名）作业    → ${ISV_FLAG}.hr.hdm.orgtransfer.business.OrgRenameChgTask
#    - 成建制划转（岗位更名）作业    → ${ISV_FLAG}.hr.hdm.orgtransfer.business.PostRenameChgTask
# 3. 启用调度计划（每日 cron）
```

## 验收清单

部署完成后必须打钩：

- [ ] 看 mservice 启动日志·有 `${ISV_FLAG}.hr.hdm.orgtransfer.business.AbstractCommonTask` 类被加载
- [ ] 跑一次手动调度任务·log 出 "成建制划转(组织上级调整)作业·开始执行..."
- [ ] 跑前先在 `${ISV_FLAG}_orguntitransfer` 表手工塞一条 `${ISV_FLAG}_executestatus="01"` 的测试数据
- [ ] 调度结束后该数据 `${ISV_FLAG}_executestatus` 应改为 `"03"`
- [ ] 标品 `hdm_transferbatch` 调动单表里应有对应单据·状态为已生效
- [ ] 标品 `hrpi_empposorgrel` 任职经历表对应人员应被刷新（标品自己处理·ISV 不私改）

## 常见问题

| 问题 | 原因 | 解决 |
|---|---|---|
| 调度任务报"找不到 TaskClassName" | schdata 里 `${ISV_FLAG}` 没替换或拼错 | 检查 schedule.schdata 里 `<TaskClassName>` |
| 调动单生成失败 | `BizappId` 没替换为现场真值 | 走 step 3 重做 |
| `${ISV_FLAG}_orguntitransfer` 表查不到 | dm 包没导入或 mservice 没重启 | 走 step 4-5 重做 |
| OperationServiceHelper.executeOperate 失败 | 标品 `hdm_transferbatch` 操作权限 | 检查 mservice 用户角色 |

## 升级路径

后续金蝶官方 dcs_clean 资产升级时：
1. 拿新版资产模板替换 `_assets/org_unit_transfer/code_templates/`
2. 跑一遍 assemble_asset.py 出新工程
3. 跑 diff vs 旧工程·人审差异
4. 走 step 4-5 升级 dm 补丁

## 协议引用

- docx §1.3 业务方案描述
- docx §4.1.2 部署说明（本 SOP 的本源）
- docx §4.1.2 第 1-4 步：开发商标识替换流程
- 实测 case_001 dcs-case-run @ 2026-05-06
