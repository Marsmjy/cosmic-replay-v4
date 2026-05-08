# payroll_bankcardchange · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### ChangeBankCardBillPlugin — ChangeBankCardBillPlugin.java

  - L26: public void beforeBindData(EventObject e) {
  - L71: private static void setBankInfo(DynamicObject billEntry, DynamicObject beforebankcard) {
  - L82: private static void setBankInfoDefaultValue(DynamicObject billEntry) {
  - L110: public void beforeDoOperation(BeforeDoOperationEventArgs args) {

### ChangeBankCardOperationPlugin — ChangeBankCardOperationPlugin.java

  - L29: public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
  - L60: private void addNewBankCard(DynamicObject dynamicObject) {
  - L121: private static void modifBank(DynamicObject dynamicObject) {

### AcctModifyBankByPayDetailOperatePlugin — AcctModifyBankByPayDetailOperatePlugin.java

  - L27: public void afterExecuteOperationTransaction(AfterOperationArgs e) {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
