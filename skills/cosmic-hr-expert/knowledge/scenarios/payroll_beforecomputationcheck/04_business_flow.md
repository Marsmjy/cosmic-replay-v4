# payroll_beforecomputationcheck · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### CalPayPushPlugin — CalPayPushPlugin.java

  - L39: public void registerListener(EventObject e) {
  - L52: public void afterCreateNewData(EventObject e) {
  - L67: public void afterDoOperation(AfterDoOperationEventArgs args) {
  - L145: private static MessageInfo getMessageInfo(long dataId, String billNo, String billName, long creatorId) {
  - L170: private static String getCacheKey(long creatorId, String billNo) {

### HsasCalpayRolltListPlugin — HsasCalpayRolltListPlugin.java

  - L33: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L47: public void afterDoOperation(AfterDoOperationEventArgs args) {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
