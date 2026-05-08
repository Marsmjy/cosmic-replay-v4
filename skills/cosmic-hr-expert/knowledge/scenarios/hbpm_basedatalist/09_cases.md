# 参考案例 · 岗位基础资料（hbpm_basedatalist）

> **状态**: 🟢 典型场景案例（基于 CS 框架构造）
> **confidence**: pattern-based
> **最后更新**: 2026-04-27

---

## 案例一：加"岗位序列战略分类"扩展字段

**客户背景**：某集团型企业需要对岗位序列进行战略分层（核心/支撑/专家），用于与薪酬规则联动，同时在 HR 驾驶舱报表中过滤。

**实施路径**：
1. 走 CS-01（modifyMeta add field），加 `${ISV_FLAG}_strategycategory`（ComboField）
2. 在对应岗位序列子 form 的编辑视图布局中，添加新字段显示
3. 在 hbjm_jobhr 的报表查询条件中，通过关联查询使用该字段

**关键决策**：
- 选择 ComboField（不选 BasedataField）：字典值稳定，无需单独建字典表，ComboField 枚举值直接维护更方便
- 字段加在 hbpm_basedatalist 主实体（不加在 hbjm_jobhr）：字典数据在字典表维护，下游岗位只做引用

**结果**：加字段 5 分钟完成，无需发布代码包，标品升级后字段保留。

---

## 案例二：禁止删除被岗位引用的序列基础资料

**客户背景**：某运营商 HR 团队误删了"技术序列"基础资料，导致 300+ 岗位的序列字段显示为空，薪酬计算报错，紧急回滚花了 2 小时。

**实施路径**：
1. 走 CS-02，建 `TdkwPositionBasedataRefCheckOp` 并列挂到 `hbpm_basedatalist` 的 delete/disable 操作
2. `TdkwPositionBasedataRefValidator.validate()` 中查 hbjm_jobhr 反向引用（直接用 id 查，BaseFormModel 不加 iscurrentversion）
3. 命中引用 → addErrorMessage 提示"该序列已被 N 个岗位引用，不可删除，建议改用禁用"

**关键知识点**：
- BaseFormModel 的反向查询直接用 id，不需要 iscurrentversion（与 haos 域 HisModel 场景的区别）
- OP 插件父类选 `HRDataBaseOp`（禁继承 AbsOrgBaseOp · forbidden）

**结果**：防止了 99% 的误操作，ISV 代码 80 行，无标品侵入。

---

## 案例三：列表页追加过滤条件

**客户背景**：某企业有多个事业部共用一套 HR 系统，不同事业部只能看到自己创建的岗位基础资料分组。

**实施路径**：
1. 建 `TdkwPositionBasedataListPlugin extends HRDataBaseList`，并列挂到 hbpm_basedatalist 列表
2. 重写 `setFilter` 方法，追加 `createorg` 过滤（本场景无 createorg · 需要在子 form 层实现）
3. 注意：hbpm_basedatalist 主列表是分组入口，实际过滤应在子列表（pagekey 指向的 form）上实现

**关键陷阱**：
- hbpm_basedatalist 主列表展示的是"基础资料分类"，不是具体数据行
- 按组织过滤要在各子列表（如岗位序列列表）上实现，不在 hbpm_basedatalist 主列表
- 禁继承 `HRBDGroupList`（PR-001）

---

## 共性踩坑总结

| 陷阱 | 错误做法 | 正确做法 |
|---|---|---|
| 误以为 hbpm_basedatalist 存放具体岗位数据 | 在主列表加岗位数量字段 | 主列表是分类入口，具体数据在子 form |
| 忘记 issyspreset 保护 | 直接在 afterBindData 解锁所有字段 | 先判断 issyspreset，预置数据保持 VIEW |
| 继承场景专属类 | extends PositionBasedataEdit | extends HRDataBaseEdit，并列挂 |
| BaseFormModel 加 iscurrentversion 过滤 | QFilter("id","=",id).and("iscurrentversion","=",true) | 直接 QFilter("id","=",id) |
