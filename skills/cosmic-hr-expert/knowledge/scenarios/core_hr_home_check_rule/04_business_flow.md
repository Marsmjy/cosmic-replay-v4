# core_hr_home_check_rule · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### CollectInfoRuleMobFormPlugin — CollectInfoRuleMobFormPlugin.java

  - L182: public void afterBindData(EventObject e) {
  - L290: private void updateShowView(String symbol){
  - L301: public void propertyChanged(PropertyChangedArgs e) {
  - L388: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L456: public void busRuleCalculate(String fieldKey, DynamicObject ruleDynamicObject) {
  - L529: public void busRuleFieldEffectSetVal(String fieldKey, DynamicObject ruleDynamicObject) {
  - L593: public void busRuleCopyToTargetField(String fieldKey, DynamicObject ruleDynamicObject) {
  - L610: public void viewRuleCheckRuleDecimal(String fieldKey, DynamicObject ruleDynamicObject) {
  - L638: public void viewRuleCheckRuleText(PropertyChangedArgs e, DynamicObject ruleDynamicObject) {
  - L703: public void viewRuleCheckRuleCheckbox(String fieldKey, DynamicObject ruleDynamicObject) {
  - L728: public void viewRuleCheckRuleDateCompare(String fieldKey, DynamicObject ruleDynamicObject) {
  - L771: public void viewRuleCheckRuleMatchValEffectField(String fieldKey, DynamicObject ruleDynamicObject, String cacheUniqueIdS
  - L1006: public void addMustInputPageCache(String fieldStr) {
  - L1040: public void removeMustInputPageCache(String fieldStr) {

### CollectInfoRuleUtil — CollectInfoRuleUtil.java

  - L83: public static DynamicObject[] getViewCheckedData(String entityName, String fieldKey) {
  - L107: public static DynamicObject[] getBusinessCheckedData(String entityName, String fieldKey) {
  - L124: public static HashMap<String, Object> getDecimalRuleItemMap(Integer decimalPrecision, Integer decimalScale, Integer minV
  - L159: public static HashMap<String, Object> getTextRuleItemMap(Integer textMinLength, Integer textMaxLength) {
  - L186: public static HashMap<String, Object> getFireUpdEvtMap() {
  - L209: public static double calculateDateByYear(Date startDate, Date endDate) {
  - L242: public static int calculateAge(Date birthDate, Date currentDate) {
  - L256: public static LocalDate convertToLocalDate(Date date) {
  - L269: public static boolean containsChinese(String str) {
  - L281: public static boolean containsDigit(String str) {
  - L293: public static boolean containsEnglish(String str) {
  - L305: public static boolean containsSymbol(String str) {
  - L314: public static boolean isNotEmptyNumeric(String str) {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
