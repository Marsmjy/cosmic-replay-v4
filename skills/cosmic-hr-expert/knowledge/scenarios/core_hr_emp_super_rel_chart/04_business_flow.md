# core_hr_emp_super_rel_chart · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### EmpSuprelChartPlugin — EmpSuprelChartPlugin.java

  - L52: public void registerListener(EventObject e) {
  - L59: public void click(EventObject evt) {
  - L70: public void afterCreateNewData(EventObject e) {
  - L95: private EmpSuprelChartTreeNode getEmpSuprelChartTreeNode(Long personId, JSONObject paramsJsonObj, EmpSuprelChartTreeNode
  - L107: public void customEvent(CustomEventArgs args) {
  - L126: public void propertyChanged(PropertyChangedArgs e) {
  - L188: private Long getChargePersonByOrgId(long orgBoId) {
  - L206: public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
  - L217: private void queryEmpSuprelChartTreeNode(JSONObject paramsJsonObj, String eventName, String status) {
  - L234: public JSONObject getPersonInfo(String personId) {
  - L253: public JSONObject getReportReltypeInfo(String number) {
  - L274: private void afterReportReltypeF7Select(ClosedCallBackEvent closedCallBackEvent) {
  - L290: private void afterHrPersonF7Select(ClosedCallBackEvent closedCallBackEvent) {
  - L306: private void jumpToErmanFileInfo(JSONObject paramsJsonObj) {
  - L333: private void setDataToCustomControl(String eventName, Map<String, Object> data, String code, String status) {

### EmpSuprelChartTreeNode — EmpSuprelChartTreeNode.java

  - L29: public String getShowDirection() {
  - L33: public void setShowDirection(String showDirection) {
  - L39: public EmpSuprelChartTreeNode() {
  - L43: public EmpSuprelChartTreeNode(String parentId, String id, String text, boolean isParent) {
  - L49: public Integer getChildrenCount() {
  - L53: public void setChildrenCount(Integer childrenCount) {
  - L57: public int getCurrentLevel() {
  - L61: public void setCurrentLevel(int currentLevel) {
  - L65: public Map<String, Object> getShowFields() {
  - L69: public void setShowFields(Map<String, Object> showFields) {
  - L73: public void setChildrenCount(int childrenCount) {
  - L77: public String getPersonId() {
  - L81: public void setPersonId(String personId) {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
