# 参考案例 · 岗位模板类型（hbpm_positiontpltype）

> **状态**: 🟢 典型场景案例（基于 CS 框架构造）
> **confidence**: pattern-based
> **最后更新**: 2026-04-27

---

## 案例一：index 冲突导致保存失败但无字段高亮

**场景**：某客户在 API 批量导入岗位模板类型时，同 org 下多条数据的 index 值相同，保存报错"数据已存在"，但用户界面没有高亮提示哪个字段有问题（因为 API 导入不走 UI 层）。

**原因分析**：
- `PositionTplTypeIndexUniqueValidator` 失败时，将 `indexError` 写入 OperateOption
- `PositionTplTypeEditPlugin.afterDoOperation` 解析 `indexError` 高亮 index 字段
- 但 API 导入不经过 FormPlugin，`afterDoOperation` 无法执行，用户看不到高亮

**解决方案**：
1. API 导入前，先查最大 index 值，步长 10 递增分配（避免冲突）
2. 或在导入前通过 getFormSchema 验证 index 唯一性

---

## 案例二：加"适用模块"扩展字段 + afterBindData 联动

**需求**：给岗位模板类型加 `${ISV_FLAG}_applicable_module`（ComboField，值：招聘/绩效/培训），在模板类型被禁用（enable=0）时，该字段也需只读。

**关键实现**：
1. CS-01：modifyMeta 加字段
2. 建 `TdkwPositionTplTypeEditPlugin extends HRDataBaseEdit` 并列挂，RowKey > #9
3. afterBindData 中先判断 enable 状态（enable="0" 时跳过联动，PositionTplTypeEditPlugin 已切 VIEW）
4. propertyChanged 中监听 ${ISV_FLAG}_applicable_module 变更，更新其他 ISV 字段

**关键陷阱**：
- ISV afterBindData 中不能再次调用 setBillStatus（会覆盖 PositionTplTypeEditPlugin 设置的 VIEW 状态）
- RowKey 必须排在 PositionTplTypeEditPlugin(#9) 之后，否则 enable 检查时机不对

---

## 案例三：防止被岗位模板引用的类型被误删

**需求**：某集团运营团队删除了一个已被 30 个岗位模板引用的"管理类"模板类型，导致下游岗位模板的 type 字段游离。

**实施**：参考 CS-02，建 `TdkwPositionTplTypeRefCheckOp extends HRDataBaseOp`，onAddValidators 注册反向引用校验（查 hbpm_positiontpl 的 type 字段）。

**关键知识点**：
- 父类选 `HRDataBaseOp`（禁继承 PositionTplTypeSaveOp · 场景专属 · PR-001）
- BaseFormModel 查询直接用 id（不加 iscurrentversion）
- RowKey 排在 PositionTplTypeSaveOp(#25) 之后

---

## 共性踩坑总结

| 陷阱 | 错误做法 | 正确做法 |
|---|---|---|
| 继承场景专属 FormPlugin | extends PositionTplTypeEditPlugin | extends HRDataBaseEdit，并列挂 |
| 继承场景专属 OP | extends PositionTplTypeSaveOp | extends HRDataBaseOp，并列挂 |
| 继承 AbsOrgBaseOp | `AbsOrgBaseOp`（禁止）| 不可继承（forbidden）· 用 HRDataBaseOp |
| ISV afterBindData 不判断 enable 状态 | 直接在禁用态写字段 | 先判断 enable="0"，禁用态跳过 |
| API 导入时 index 冲突 | 不处理 index | 导入前预计算 index，确保不重复 |
