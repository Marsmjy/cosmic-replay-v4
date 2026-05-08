# 参考案例 · 协作类型（hbpm_reportcoreltype）

> **状态**: 🟢 典型场景案例（基于 CS 框架构造）
> **confidence**: pattern-based
> **最后更新**: 2026-04-27

---

## 案例一：orgteamtype 多选字段误操作导致子表数据丢失

**场景**：某客户通过 API 保存协作类型时，只传了主表字段，未传 orgteamtype 字段。由于子表是全量替换模式（DELETE+INSERT），平台将子表全量删除后，INSERT 了空数据，导致该协作类型的 orgteamtype 关联全部丢失。

**原因分析**：
- `t_hbpm_orgteamtype` 子表采用全量替换写入模式
- orgteamtype 是 `required=true` 字段，UI 层会拦截，但 API 调用绕过了 UI
- 子表全量替换时，空 orgteamtype 等于删除所有关联

**解决方案**：
1. API 保存时，`orgteamtype` 字段必须与主表数据同时提交
2. 在 OP 层 `onAddValidators` 追加 ISV Validator 检查 orgteamtype 不为空（与标品 `required=true` 兜底双保险）
3. 在 OP 层 `onPreparePropertys` 显式声明 `orgteamtype` 才能在事务内读取子表数据（PR-010）

---

## 案例二：加"适用层级"扩展字段 + afterBindData 联动

**需求**：给协作类型加 `${ISV_FLAG}_applicable_level`（ComboField，值：L1/L2/L3），并在打开编辑页时，若协作类型已被禁用（issyspreset=true），该字段也显示为只读。

**关键实现**：
1. CS-01：modifyMeta 加字段
2. 建 `TdkwReportCoreltypeEditPlugin extends HRDataBaseEdit` 并列挂，RowKey > #9
3. afterBindData 中先判断 issyspreset（issyspreset=true 时整页已是 VIEW，跳过 ISV 联动）
4. propertyChanged 中监听 ${ISV_FLAG}_applicable_level 变更，更新其他 ISV 联动字段

**关键陷阱**：
- RowKey 必须排在 PositionBasedataEdit(#9) 之后，否则 issyspreset 检查时机不对
- ISV afterBindData 中不能再次调用 `setBillStatus(VIEW)`（PositionBasedataEdit 已处理）
- 与 hbpm_positiontpltype 场景不同：本场景没有 enable="0" 的只读检查，只有 issyspreset 检查

---

## 案例三：防止被汇报关系引用的协作类型被误删

**需求**：某集团 HR 管理员删除了一个已被汇报关系配置大量使用的"虚线汇报"协作类型，导致汇报关系配置中该类型字段游离，汇报层级计算失效。

**实施**：参考 CS-02，建 `TdkwReportCoreltypeRefCheckOp extends HRDataBaseOp`，onAddValidators 注册反向引用校验（查汇报关系场景中引用本字典的字段）。

**关键知识点**：
- 父类选 `HRDataBaseOp`（禁继承 BaseDataBuOp(hbpm 域) · 场景专属 OP · PR-001）
- BaseFormModel 查询直接用 id（不加 iscurrentversion · PR-009 不适用本场景）
- RowKey 排在 BaseDataBuOp(hbpm 域)(#26) 之后
- 若引用字段是 MulBasedataField，查询需走子表路径（.fbasedataid），不是直字段查

---

## 共性踩坑总结

| 陷阱 | 错误做法 | 正确做法 |
|---|---|---|
| 继承场景专属 FormPlugin | extends PositionBasedataEdit | extends HRDataBaseEdit，并列挂 |
| 继承场景专属 ListPlugin | extends PositionnBaseDataOrderPlugin | extends ListPlugin 基类，并列挂 |
| 继承场景专属 OP | extends BaseDataBuOp（hbpm 域）| extends HRDataBaseOp，并列挂 |
| 继承 AbsOrgBaseOp | `AbsOrgBaseOp`（禁止）| 不可继承（forbidden）· 用 HRDataBaseOp |
| ISV afterBindData 不判断 issyspreset | 直接在预置数据上 setValue | 先判断 issyspreset=true，跳过联动 |
| API 保存不传 orgteamtype | 只传主表字段 | 主表+子表字段同时提交 |
| 直接查 t_hbpm_orgteamtype 子表 | SELECT * FROM t_hbpm_orgteamtype | 通过 orgteamtype.fbasedataid 路径查 |
