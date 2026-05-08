# attendance_wtc_roster · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### DateScope — DateScope.java

  - L8: public DateScope(Date startDate, Date endDate) {
  - L13: public Date getStartDate() {
  - L17: public String getStartDateString() {
  - L21: public void setStartDate(Date startDate) {
  - L25: public Date getEndDate() {
  - L29: public String getEndDateString() {
  - L33: public void setEndDate(Date endDate) {

### CommonPropertiesQueryUtil — CommonPropertiesQueryUtil.java

  - L27: public static Map<String, String> queryByNumber(String number) {
  - L32: public static Map<String, Map<String, String>> queryByNumbers(List<String> numbers) {
  - L41: public static Map<String, List<String>> queryByNumberJoinKey(String number) {
  - L46: public static Map<String, Map<String, List<String>>> queryByNumbersJoinKey(List<String> numbers) {
  - L59: public static DynamicObjectCollection queryProperties(List<String> numbers) {
  - L71: public static Boolean isSunCompany(String orgNumber) {
  - L84: public static Boolean isSunOrYxCompany(String orgNumber) {
  - L98: public static Boolean isYxCompany(String orgNumber) {
  - L111: public static Boolean isZjCompany(String orgNumber) {
  - L127: public static Boolean isXNYCompany(String orgNumber) {

### BatchBusiTripBillSubmitPlugin — BatchBusiTripBillSubmitPlugin.java

  - L25: public void validate() {

### BusiTripBillSubmitPlugin — BusiTripBillSubmitPlugin.java

  - L23: public void validate() {
  - L50: private static String getFieldName(String name) {

### ArchivalInformationServiceImpl — ArchivalInformationServiceImpl.java

  - L15: public DynamicObject getArchivalInformation(Long archivalId) {
  - L21: public DynamicObject[] getArchivalInformations(List<Object> attfileIds) {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
