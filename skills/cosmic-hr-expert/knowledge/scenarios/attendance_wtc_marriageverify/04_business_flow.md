# attendance_wtc_marriageverify · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### MarriageVerifyPlugin — MarriageVerifyPlugin.java

  - L35: public void propertyChanged(PropertyChangedArgs e) {
  - L77: private void setLblText(String key, String text) {
  - L84: private void setMarriageLableInfo(Date marriageRegistDate){
  - L99: public void afterBindData(EventObject e) {
  - L148: public void registerListener(EventObject e) {
  - L156: public void beforeClick(BeforeClickEvent evt) {

### MobilleMarriageVerifyPlugin — MobilleMarriageVerifyPlugin.java

  - L29: public void propertyChanged(PropertyChangedArgs e) {
  - L45: private void setMarriageLableInfo(Date marriageRegistDate){
  - L55: private void setLblText(String key, String text) {
  - L63: public void registerListener(EventObject e) {
  - L74: public void beforeClick(BeforeClickEvent evt) {

### BatchMarriageDateValidator — BatchMarriageDateValidator.java

  - L24: public void validate() {

### MarriageDateValidator — MarriageDateValidator.java

  - L25: public void validate() {

### MarriageDateDealUtil — MarriageDateDealUtil.java

  - L38: public static Date dealMarriageDate(long personId, IFormView view) {
  - L57: public static DynamicObject[] getMarriageInfo(List<Long> employeeIdList) {
  - L71: public static Date getMarriageStartDate(IFormView view) {
  - L95: public static Date getMarriageEndDate(IFormView view) {
  - L111: public static Date getMarriageEndDate(Date marriageStartDate) {
  - L126: public static boolean valideClick(Date startdate, Date enddate, IFormView view) {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
