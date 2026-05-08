# payroll_swc_salaryapproval · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### NumItemsDTO — NumItemsDTO.java

  - L14: public NumItemsDTO() {
  - L17: public NumItemsDTO(String selectField, String valueType) {
  - L22: public String getSelectField() {
  - L26: public void setSelectField(String selectField) {
  - L30: public String getValueType() {
  - L34: public void setValueType(String valueType) {
  - L38: public boolean equals(Object o) {
  - L49: public int hashCode() {

### SetFilterUtil — SetFilterUtil.java

  - L18: public static void setFilterBySalaryMap(SetFilterEvent e, IFormView view) {

### SWCPageCache — SWCPageCache.java

  - L20: public SWCPageCache(String pageId) {
  - L24: public SWCPageCache(IPageCache cache) {
  - L28: public SWCPageCache(IFormView view) {
  - L32: public void put(String keyName, Object value) {
  - L36: public void put(String keyName, Object value, boolean inclusionNON_NULL) {
  - L40: public <T> T get(String keyName, Class<T> clazz) {
  - L45: public void remove(String keyName) {
  - L49: public Map<String, String> getAll() {

### PayrollApprovalMobileEditPlugin — PayrollApprovalMobileEditPlugin.java

  - L32: public void afterLoadData(EventObject e) {
  - L36: protected boolean loadOverView() {
  - L97: public void beforeBindData(EventObject e) {
  - L119: private List<Long> getCalTaskId() {

### PayrollApprovalEditPlugin — PayrollApprovalEditPlugin.java

  - L23: public void afterBindData(EventObject e) {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
