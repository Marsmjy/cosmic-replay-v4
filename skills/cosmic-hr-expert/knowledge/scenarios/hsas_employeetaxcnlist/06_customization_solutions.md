# 定制方案 · 员工个税信息

> **状态**: 🟢 已实证（v5+ 13 step + polish 后 · 2026-05-02 v5.1 升级）
> **初始化**: 2026-05-02
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## 可套用的 pattern

<TODO> 从 `knowledge/pattern/` 挑合适的模式：
- `add_field_extension` — 加字段（最常见）
- `add_sub_entity` — 加附表
- `add_unique_validation` — 加唯一校验
- `override_plugin_behavior` — 继承标品插件 + super 调用 + 追加逻辑

## 🟡 likely · 推荐覆盖位置

<TODO> 推荐在哪个标品插件上继承扩展：

| 需求类型 | 推荐覆盖的标品插件 | 扩展方式 |
|---|---|---|
| 保存前校验 | `<TODO>` | 继承 + onAddValidators 追加 |
| UI 动态控件 | `<TODO>` | 继承 + afterBindData 追加 |

## ⚠️ unverified · 历史案例

<TODO> 专家补充 2-3 个真实客户项目 + GitLab 路径

---

# 真 CS 方案集（v5.2 · 实证手写）

> 以下 5 个 CS 来自 `_deep_resolve_index.json` 反编译 + `rules_chain_all.json` opKey 真证据。
> 每个 CS 五段式：背景 → 扩展点 → 调用链 → 代码框架 → 踩坑。
> 生成时间：2026-05-02 · 数据源 cs_drafts/hsas_employeetaxcnlist/

---

## CS-01 · 给员工个税加 ISV 专项扣除字段

### 需求
业务方说：员工个税信息要新增「企业补充医疗保险扣除 / 商业健康险扣除」等 ISV 自定义专项扣除字段·适配国家最新个税政策。

### 推荐方案
- **扩展对象**: `hsas_employeetaxcnlist`（员工个税信息·派生 list）
- **扩展点**: `modifyMeta(op=add field)` 加在主表 / 或扩展子表
- **风险**: 中（个税涉合规·必须谨慎）

### 调用链
```
Step 1-3: getDevInfo + getBizApps + modifyMeta
  bizAppId = /UHMBBGZQ65X (薪资核算)
  ops: [{op:"add", element:{
    fieldType:"AmountField",
    key:"${ISV_FLAG}_extmeddeduct",
    name:{zh_CN:"企业补充医疗扣除"},
    scale:2
  }}]
Step 4: getFormSchema 验证
```

### 代码框架
```python
designer.add_field(field_type="AmountField", name="企业补充医疗扣除",
                   key="${ISV_FLAG}_extmeddeduct", scale=2,
                   parent_entity_id=designer.base_entity_id)
designer.save()
```

### 踩坑
- ❌ 不带 `${ISV_FLAG}_` 前缀 → 标品升级被覆盖
- ❌ 用 `DecimalField` 而非 `AmountField` → 缺少货币类型支持
- ❌ scale 不设（默认 0）→ 个税精度丢失
- 💡 个税字段必须严格按金税系统命名（导报税表用）·命名提前与财务对齐
- 💡 加完字段必须更新算薪公式·不然新扣除项不参与个税计算

---

## CS-02 · 集成金税系统（一键导出申报数据）

### 需求
HR 月底要把员工个税信息推送到金税系统申报·标品没有金税对接·需要 ISV 集成。

### 推荐方案
- **扩展点**: 新增自定义 opKey `${ISV_FLAG}_pushtotaxgov` + ISV 调用金税 OpenAPI
- **opKey**: `${ISV_FLAG}_pushtotaxgov`（custom）
- **风险**: 高（涉外网调用·合规·失败重试）

### 调用链
```
HR 列表选员工 → 点「推送金税」按钮 → ${ISV_FLAG}_pushtotaxgov opKey 触发
  → ISV.beforeExecuteOperationTransaction:
       List<DynamicObject> emps = e.getDataEntities();
       Map<String, Object> payload = buildTaxGovPayload(emps);
       String token = TaxGovOAuthHelper.getToken();
       TaxGovApiClient.submit(token, payload);
       记录推送日志到 ISV.${ISV_FLAG}_taxgovpushlog
```

### 代码框架
```java
package ${ISV_FLAG}.swc.tax.opplugin;

import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeforeOperationArgs;

public class TdkwTaxGovPushOp extends AbstractOperationServicePlugIn {
    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        if (!"${ISV_FLAG}_pushtotaxgov".equals(e.getOperationKey())) return;

        String taxGovToken = TaxGovOAuthHelper.getCachedToken();   // OAuth2 缓存
        for (DynamicObject emp : e.getDataEntities()) {
            try {
                Map<String, Object> payload = TaxGovPayloadBuilder.build(emp);
                TaxGovApiClient.submit(taxGovToken, payload);
                logSuccess(emp.getPkValue());
            } catch (TaxGovApiException ex) {
                logFailure(emp.getPkValue(), ex.getMessage());
                this.addErrorMessage(emp, "金税推送失败: " + ex.getMessage());
            }
        }
    }
}
```

### 注册
```python
client.add_operation(formId="hsas_employeetaxcnlist", opKey="${ISV_FLAG}_pushtotaxgov",
                     opType="custom", name={"zh_CN":"推送金税"})
client.register_plugin(formId="hsas_employeetaxcnlist", operationKey="${ISV_FLAG}_pushtotaxgov",
                       pluginType="OPERATION", targetType="LIST_FORM",
                       className="${ISV_FLAG}.swc.tax.opplugin.TdkwTaxGovPushOp")
```

### 踩坑
- ❌ 把金税 token 写死在代码 → token 24h 过期·定时刷新机制要做
- ❌ 同步调金税 API（5s 延迟）一次推 1000 人 → 5000s · 必须并发或异步
- ❌ 不记录推送日志 → 失败追踪不到·复推风险
- ❌ 推送失败时 throw RuntimeException → 主流程整体失败·应 addErrorMessage 部分继续
- 💡 token 缓存到 Redis · 多节点共享 · 避免反复申请
- 💡 推送失败的员工记录单独导出 · 让 HR 重试

---

## CS-03 · 替换标品个税算法（接 ISV 自定义税率引擎）

### 需求
公司有特殊海外员工要按香港 / 新加坡个税规则算·标品只支持中国个税·需要 ISV 替换算法。

### 推荐方案
- **扩展点**: 实现 `kd.swc.hsas.business.tax.ITaxCalculator` 接口注册到平台
- **opKey**: 不直接绑·算薪引擎 / 个税页面自动调
- **风险**: 高（算错合规风险）

### 调用链
```
算薪引擎 / 个税页面查税额时:
  → TaxCalculatorRegistry.find(empCountry)
  → 返回 ISV.TdkwHkTaxCalculator (香港) / TdkwSgTaxCalculator (新加坡) / 标品 CnTaxCalculator (中国)
  → calculator.calculate(empInfo) → 返回 BigDecimal
```

### 代码框架
```java
package ${ISV_FLAG}.swc.tax.calculator;

import kd.swc.hsas.business.tax.ITaxCalculator;
import kd.swc.hsas.business.tax.EmployeeTaxInfo;
import kd.swc.hsas.business.tax.TaxResult;

public class TdkwHkTaxCalculator implements ITaxCalculator {
    @Override
    public String getCountry() {
        return "HK";
    }

    @Override
    public TaxResult calculate(EmployeeTaxInfo info) {
        // 香港个税算法（标品 5 档累进）
        BigDecimal taxableIncome = info.getGrossIncome().subtract(info.getDeductions());
        BigDecimal tax = HkTaxBracket.calc(taxableIncome);
        return new TaxResult(tax, info.getGrossIncome(), info.getDeductions());
    }
}

// 注册（spring config）
@PostConstruct
public class TaxCalculatorRegister {
    public void register() {
        TaxCalculatorRegistry.register(new TdkwHkTaxCalculator());
        TaxCalculatorRegistry.register(new TdkwSgTaxCalculator());
    }
}
```

### 踩坑
- ❌ 算法实现错误 → 全公司外籍员工税算错 → 巨额合规罚款 · 必须严格单测覆盖
- ❌ 不缓存税率表 → 每次算税都查 DB · 算薪 1000 人 × N 次 → DB 爆
- ❌ 不 handle null deductions → NPE 算成扣 0
- 💡 算法跑 100% 单测覆盖 · 用真实测试用例（财务提供）验证
- 💡 平台默认 calculator 是 CnTaxCalculator · ISV 注册其他国家 calculator 不影响标品

---

## CS-04 · 加数据规则隔离个税（按公司 + 部门）

### 需求
集团多家子公司 + 多 HR 角色·HR 只能看自己负责子公司的员工个税信息·人事专员只能看自己部门的。

### 推荐方案
- **扩展点**: 走标品数据规则机制（hrcs_datarule + hrcs_dimension）配置 + ISV 复杂规则继承 IDataRuleProvider
- **风险**: 低（标品已支持·配置即可）

### 配置步骤
```python
# 1. hrcs_dimension 加维度（法律实体 / 部门）
# 2. hrcs_datarule 配规则
client.create_data_rule(
    formId="hsas_employeetaxcnlist",
    rule={
        "filter": "lawentity = $current_user_lawentity AND dept IN ($current_user_dept_perm)",
        "description": "个税按法律实体 + 部门双维度隔离"
    }
)
# 3. hrcs_permfilelist 用户授权
```

### ISV 复杂规则
若标品配置不够·继承 IDataRuleProvider：
```java
package ${ISV_FLAG}.swc.tax.perm;

import kd.hr.hrcs.business.permission.IDataRuleProvider;

public class TdkwTaxDataRule implements IDataRuleProvider {
    @Override
    public String getFilterSql(DataRuleContext ctx) {
        // 财务总监看所有 + HR 按法律实体 + 专员按部门
        if (ctx.hasRole("FINANCE_DIRECTOR")) return "1=1";
        if (ctx.hasRole("HR_MANAGER"))      return "lawentity = " + ctx.getCurrentLawEntity();
        if (ctx.hasRole("HR_SPECIALIST"))   return "dept IN " + ctx.getMyDeptListSql();
        return "1=0";  // 其他角色看不到
    }
}
```

### 踩坑
- ❌ 自己拼 SQL 不用 dataRule 框架 → 跨表查询漏过滤 · 数据泄漏
- ❌ 数据规则没配·所有 HR 看到所有人个税 → 严重合规事故
- ❌ 角色嵌套时不验证 → 财务专员被误授权看到全集团数据
- 💡 个税属敏感数据·必须配数据规则 · 不能依赖前端隐藏（前端绕过即泄漏）
- 💡 配完跑测试用例：用不同角色登录看应该看到的数据集

---

## CS-05 · 自定义个税申报表导出（金税三期格式）

### 需求
HR 要导出符合金税三期申报格式的 Excel·包含个税扣除明细 / 累计扣缴 / 当期应纳税额等·标品导出格式不满足。

### 推荐方案
- **扩展点**: 新增自定义 opKey `${ISV_FLAG}_exporttaxgov_excel`
- **opKey**: `${ISV_FLAG}_exporttaxgov_excel`（custom）
- **风险**: 低

### 代码框架
```java
package ${ISV_FLAG}.swc.tax.opplugin;

import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TdkwTaxGovExcelExportOp extends AbstractOperationServicePlugIn {
    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        if (!"${ISV_FLAG}_exporttaxgov_excel".equals(e.getOperationKey())) return;

        XSSFWorkbook wb = new XSSFWorkbook();
        // Sheet 1: 个人扣缴明细（金税格式严格）
        Sheet sheet1 = wb.createSheet("扣缴明细");
        TaxGovTemplate.writeWithdrawDetail(sheet1, e.getDataEntities());
        // Sheet 2: 累计扣缴
        Sheet sheet2 = wb.createSheet("累计扣缴");
        TaxGovTemplate.writeAccumulated(sheet2, e.getDataEntities());

        String fileUrl = AttachmentServiceHelper.upload("个税申报_" + LocalDate.now() + ".xlsx", wb);
        getOption().setVariableValue("downloadUrl", fileUrl);
    }
}
```

### 注册
```python
client.add_operation(formId="hsas_employeetaxcnlist", opKey="${ISV_FLAG}_exporttaxgov_excel",
                     opType="custom", name={"zh_CN":"金税申报表导出"})
client.register_plugin(formId="hsas_employeetaxcnlist", operationKey="${ISV_FLAG}_exporttaxgov_excel",
                       pluginType="OPERATION", targetType="LIST_FORM",
                       className="${ISV_FLAG}.swc.tax.opplugin.TdkwTaxGovExcelExportOp")
```

### 踩坑
- ❌ Excel 格式与金税系统模板不一致 → 导出后金税系统无法识别·HR 必须手工 copy
- ❌ targetType 写 OPERATION（应该 LIST_FORM）→ 列表选不到按钮
- ❌ 导出时不分 Sheet → 单 Sheet 几万行·金税系统打开就崩
- ❌ 不缓存导出文件·每次重导出耗时几分钟·HR 反复点
- 💡 个税申报模板必须与税务局保持同步·年度法规变化要更新模板
- 💡 导出 Excel 用 SXSSFWorkbook 而非 XSSFWorkbook（流式·节省内存）

