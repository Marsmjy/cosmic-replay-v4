# core_hr_downermanfileword · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### CustomFileHeadHelper — CustomFileHeadHelper.java

  - L34: public static Map<String, DynamicObject> queryDataFromDb(List<Map<String, Object>> headFieldList, FormShowParameter form
  - L94: private static void putNumberToEntrel(Map<String, List<Map<String, Object>>> groupFields) {
  - L117: private static void addtionalCondition(QFilter qFilter, String entityName) {
  - L124: private static QFilter getQFilter(String pageNumber, FormShowParameter formShowParameter) {
  - L139: public static String getData(Map<String, DynamicObject> data, Map<String, Object> headField) {

### CustomPersonModelUtil — CustomPersonModelUtil.java

  - L23: public static QFilter getQFilterForHeadArea(String pageNumber, Map<String, Object> values) {
  - L76: private static boolean validateData(Map<String, Object> values, String key) {
  - L80: private static boolean validate(String pageNumber, Map<String, Object> values, String key) {
  - L84: public static CustomPersonModelClassificationEnum getClassification(String pageNumber) {

### CustomValueConvertHelper — CustomValueConvertHelper.java

  - L13: public static String handleMulBaseData(List<?> data) {

### ErmanFileReusePlugin — ErmanFileReusePlugin.java

  - L37: public void afterDoOperation(AfterDoOperationEventArgs e) {
  - L61: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L76: public void closedCallBack(ClosedCallBackEvent close) {
  - L97: private static DynamicObjectCollection queryPerProWord() {
  - L112: private List<Object> getPKIds() {

### PersonFileDownloadFormPlugin — PersonFileDownloadFormPlugin.java

  - L72: public void afterCreateNewData(EventObject e) {
  - L79: public void afterBindData(EventObject e) {
  - L91: private void handleField(Map<String, Object> field) {
  - L98: private Map<String, DynamicObject> queryDataFromDb(List<Map<String, Object>> headFieldList) {
  - L140: private void handleConfigurableField(Map<String, Object> field) {
  - L173: private void setTips(String key, String content) {
  - L185: private boolean isEmployee() {
  - L189: private String getData(Map<String, DynamicObject> data, Map<String, Object> headField) {
  - L205: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L220: public void afterDoOperation(AfterDoOperationEventArgs e) {
  - L246: public void closedCallBack(ClosedCallBackEvent close) {
  - L265: private static DynamicObjectCollection queryPerProWord() {
  - L276: public List<Long> getErmanFileIds() {
  - L295: public void setIsShow() {

### PersonFileDownloadRptPlugin — PersonFileDownloadRptPlugin.java

  - L35: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L55: public void afterDoOperation(AfterDoOperationEventArgs e) {
  - L79: public void closedCallBack(ClosedCallBackEvent close) {
  - L98: private static DynamicObjectCollection queryPerProWord() {
  - L109: public List<Long> getErmanFileIds() {

### WordPrintSelectTemplatePlugin — WordPrintSelectTemplatePlugin.java

  - L52: public void afterCreateNewData(EventObject e) {
  - L71: public void afterDoOperation(AfterDoOperationEventArgs e) {
  - L151: public void beforeDoOperation(BeforeDoOperationEventArgs args) {

### FileConversionImpl — FileConversionImpl.java

  - L61: public Map<String, Object> fileConversion(Long employeeId,Long assignmentId, String personName, String templateNumber) {
  - L67: public Map<String, Object> assembleWordFile(Map<String, Object> allDataMap, String fileName, String templateEncoding, Li
  - L104: private Map<String, Object> buildResultMap(Map<String, Object> allDataMap, String fileName, Map<String, Object> resultMa
  - L127: public Map<String, Object> buildDate(Long employeeId,Long assignmentId, String personName, String templateNumber) {
  - L214: private List<DynamicObject> empOrgRelAllHandle(DynamicObject[] empOrgRelAllColl) {
  - L301: private static void specialEmpPreWork(Map<String, Object> allMap, List<DynamicObject> empPreList, List<Object> perEduLis
  - L392: private static boolean equals(String startDate, String endData) {
  - L401: private static void currencyBuildMap(Long billId, String entityName, String orderByFiled, Map<String, Object> allMap, St
  - L415: private static List<DynamicObject> sortFamilyMemberDy(DynamicObject[] query) {
  - L448: private static List<String> familymembSort() {
  - L491: public static String returnStr(String strPro, String returnStr) {
  - L499: private static void specialEmrgContact(String entityName, Map<String, Object> allMap, DynamicObject[] attendRecordsColl)
  - L511: private static void specialPerEduExp(Long billId, String entityName, Map<String, Object> allMap, StringBuilder filedStr,
  - L575: private static void specialEmpPosOrgRel(String entityName, Map<String, Object> allMap, DynamicObject[] attendRecordsColl
  - L598: private static void empPosOrgHandle(DynamicObject[] attendRecordsColl, Map<String, String> empMap, Map<String, Object> a

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
