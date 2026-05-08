# hbss_headcount_management · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### DateTimeCommon — DateTimeCommon.java

  - L21: public static String getDateMonthStr(Date startMonth)  {
  - L31: public static String getDateStr(Date startMonth)  {
  - L36: public static Date ConvertToDateTime(String stime) {
  - L49: public static Date ConvertToDate(String stime) {
  - L62: public static String covertToyyyyMMddHHmmss(Date date) {
  - L71: public static String covertToyyyyMMdd(Date date) {
  - L80: public static String GetWeek(String pTime) throws Throwable {
  - L99: public static String ConvertToTime(String sCtime) {
  - L108: public static String ConvertToTime(Integer iCtime) {
  - L126: public static Date GetDateFromTimeStamp(Long timestamp) {
  - L131: public static String FormatDate(Date date, String sFormat) {
  - L137: public static String genCurrTimestamp() {
  - L141: public static Date getNextDay(Date date) {
  - L148: public static Date getWeekStartDate() {
  - L155: public static Date getMonthStartDate() {

### HisAttachmentTool — HisAttachmentTool.java

  - L23: public static void putAttachmentsIntoCustomParam(IFormView view, FormShowParameter formShowParameter) {
  - L47: private static void findAttachments(Control control, Set<String> attachmentKeys) {
  - L58: private static void removeRepeatAttachmentsForList(List<Map<String, Object>> attachmentData) {

### PlanCollectFromPlugin — PlanCollectFromPlugin.java

  - L33: public void afterBindData(EventObject e) {
  - L108: private void setLaborreltypeDate(Long detailId) {
  - L168: private void setPropertyDate(Long detailId) {
  - L209: private void setJobTagDate(Long detailId) {
  - L289: private void setJobLevelDate(Long detailId) {
  - L331: private void setCollectDate(List<DynamicObject> collect, String collectName, int index) {
  - L403: private void setSumEntry(int index) {

### PlanmonthBillBzFormPlugin — PlanmonthBillBzFormPlugin.java

  - L39: public void registerListener(EventObject e) {
  - L46: public void afterBindData(EventObject e) {
  - L204: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L215: public void propertyChanged(PropertyChangedArgs e) {
  - L233: public void beforeImportData(BeforeImportDataEventArgs e) {
  - L287: private void checkSave() {
  - L330: private void checkImport(Map<String, Object> sourceData, String importtype) {
  - L467: private int getLabeldimension() {
  - L486: private void setBzColor() {
  - L516: private void setKbNumber() {

### PlanmonthBillFormPlugin — PlanmonthBillFormPlugin.java

  - L67: public void registerListener(EventObject e) {
  - L156: public void propertyChanged(PropertyChangedArgs e) {
  - L297: public void afterDoOperation(AfterDoOperationEventArgs eventArgs) {
  - L321: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L345: public void afterBindData(EventObject e) {
  - L463: private void setStyle(int startStr, int endStr) {
  - L503: private void checkSave() {
  - L537: public static void sendMessage(String title, String content, List<Long> userList, String url) {
  - L548: private String getUrl(Long successId, String frombill) {
  - L560: private Set<Long> getAdminByOrg(String orgNumber, String hierarchy) {

### PlanMonthFormPlugin — PlanMonthFormPlugin.java

  - L35: public void propertyChanged(PropertyChangedArgs e) {
  - L52: public void afterBindData(EventObject e) {
  - L74: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L85: public static Date getMaxMonthDate(Date date) {
  - L94: public static Date getDataFormat(Date date, boolean isStart) {

### PlanPersonFormPlugin — PlanPersonFormPlugin.java

  - L29: public void propertyChanged(PropertyChangedArgs e) {

### PlanPersonsFromPlugin — PlanPersonsFromPlugin.java

  - L34: public void registerListener(EventObject e) {
  - L65: public void afterBindData(EventObject e) {

### PlanyearBillFormPlugin — PlanyearBillFormPlugin.java

  - L51: public void registerListener(EventObject e) {
  - L57: public void afterBindData(EventObject e) {
  - L130: public void itemClick(ItemClickEvent evt) {
  - L180: public void closedCallBack(ClosedCallBackEvent e) {
  - L201: private Set<Long> getAdminByOrg(String orgNumber, String hierarchy) {

### PlanyearDetailFormPlugin — PlanyearDetailFormPlugin.java

  - L51: public void registerListener(EventObject e) {
  - L132: public void afterBindData(EventObject e) {
  - L276: public void propertyChanged(PropertyChangedArgs e) {
  - L440: private void showHzPortForm(boolean b, String type, String table) {
  - L468: public void itemClick(ItemClickEvent evt) {
  - L508: public void closedCallBack(ClosedCallBackEvent e) {
  - L517: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L735: public void afterDoOperation(AfterDoOperationEventArgs args) {
  - L768: public void confirmCallBack(MessageBoxClosedEvent event) {
  - L779: public void beforeImportEntry(BeforeImportEntryEventArgs args) {
  - L1000: private String isHas(String key) {
  - L1004: private Map<String, Integer> joinStr(DynamicObjectCollection collection) {
  - L1076: public void hyperLinkClick(HyperLinkClickEvent evt) {
  - L1098: private   List<String>  setFieldList(DynamicObject dgdlPlanObj) {
  - L1152: private void setEnable(IFormView view, int i, boolean item) {

### PlanyearDetailPCFormPlugin — PlanyearDetailPCFormPlugin.java

  - L18: public void afterBindData(EventObject e) {

### PlanYearFormPlugin — PlanYearFormPlugin.java

  - L37: public void registerListener(EventObject e) {
  - L45: public void propertyChanged(PropertyChangedArgs e) {
  - L69: public void afterBindData(EventObject e) {

### PlanyearSynPersonFormPlugin — PlanyearSynPersonFormPlugin.java

  - L50: public void afterBindData(EventObject e) {
  - L59: public void registerListener(EventObject e) {
  - L65: public void click(EventObject evt) {
  - L230: public void closedCallBack(ClosedCallBackEvent e) {
  - L244: private Map<String, DynamicObject> getAdminByOrg(String orgNumber, String hierarchy) {
  - L276: private List<Long> getAllOrg(Long orgId) {
  - L289: private boolean isLastOrg(DynamicObject dataEntity) {

### CompilationPlanListPlugin — CompilationPlanListPlugin.java

  - L59: public void afterDoOperation(AfterDoOperationEventArgs args) {
  - L165: private void showModel(String formId) {
  - L200: private BillShowParameter newBillShowParameter(String entityId) {
  - L205: private void setPersonalCustomParams(Map<String, Object> customParams) {
  - L223: public void registerListener(EventObject e) {
  - L229: public void beforeItemClick(BeforeItemClickEvent evt) {
  - L275: private void dispatch(Map<String, Object> paramMap, String item) {
  - L304: public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
  - L331: private Map<String, DynamicObject> getAdminByOrg(String orgNumber, String hierarchy) {

### CompilationPlanMonthListPlugin — CompilationPlanMonthListPlugin.java

  - L30: public void beforeDoOperation(BeforeDoOperationEventArgs args) {

### PlanMonthAdminOrgTreeList — PlanMonthAdminOrgTreeList.java

  - L26: public PlanMonthAdminOrgTreeList() {
  - L31: public void initializeTree(EventObject e) {
  - L46: public void treeNodeClick(TreeNodeEvent e) {
  - L54: public void propertyChanged(PropertyChangedArgs changedArgs) {
  - L64: protected QFilter buildNodeClickFilter(BuildTreeListFilterEvent buildTreeListFilterEvent) {
  - L91: public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {

### PlanMonthListPlugin — PlanMonthListPlugin.java

  - L44: public PlanMonthListPlugin() {
  - L49: public void registerListener(EventObject e) {
  - L55: public void setFilter(SetFilterEvent e) {
  - L65: public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
  - L90: public void beforeItemClick(BeforeItemClickEvent evt) {
  - L192: public void afterDoOperation(AfterDoOperationEventArgs args) {
  - L241: private List<String> getHidecolumnList(int index, List<String> columnList) {
  - L254: private void setListUnitStyle(Long id, DynamicObjectCollection data) {
  - L259: private void doCellStyle(DynamicObject dataEntity, DynamicObjectCollection data) {
  - L355: private static void serCell(int row, List<CellStyle> cellStyles, String tdkw_bzStr) {

### PlanYearAdminOrgTreeList — PlanYearAdminOrgTreeList.java

  - L29: public PlanYearAdminOrgTreeList() {
  - L34: public void initializeTree(EventObject e) {
  - L48: public void treeNodeClick(TreeNodeEvent e) {
  - L71: protected QFilter buildNodeClickFilter(BuildTreeListFilterEvent buildTreeListFilterEvent) {

### PlanyearDetailListPlugin — PlanyearDetailListPlugin.java

  - L24: public void afterBindData(EventObject e) {
  - L31: public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
  - L50: private List<String> getHidecolumnList(int index){

### AffirmPersonOp — AffirmPersonOp.java

  - L103: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L141: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L301: private void args(PreparePropertysEventArgs e) {
  - L339: private void setAvg(DynamicObject addNew, DynamicObject thisObj) {

### CompilationPlanMonthSaveOp — CompilationPlanMonthSaveOp.java

  - L34: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L43: public void onAddValidators(AddValidatorsEventArgs e) {
  - L55: public void validate() {

### CompilationPlanReviseOp — CompilationPlanReviseOp.java

  - L21: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L27: public void onAddValidators(AddValidatorsEventArgs e) {
  - L38: public void validate() {

### CompilationPlanSaveAndEnableOp — CompilationPlanSaveAndEnableOp.java

  - L34: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L43: public void onAddValidators(AddValidatorsEventArgs e) {
  - L55: public void validate() {

### CompilationPlanSaveOp — CompilationPlanSaveOp.java

  - L19: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L25: public void beginOperationTransaction(BeginOperationTransactionArgs e) {

### CompilationPlanUrgeOp — CompilationPlanUrgeOp.java

  - L89: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L106: public void endOperationTransaction(EndOperationTransactionArgs e) {
  - L727: public void endOperationTransaction(EndOperationTransactionArgs e) {
  - L743: private PreparedData prepareData(EndOperationTransactionArgs e) {
  - L759: private ProcessResult processDataEntities(EndOperationTransactionArgs e, PreparedData preparedData) {
  - L822: private DynamicObject createPlanBill(String billKey, DynamicObject dataEntity) {
  - L993: private Map<String, Set<Long>> calculateEmployeeCount(DynamicObject dataEntity, List<Long> orgList, Set<String> orgStruc
  - L1422: private void saveAndSendMessage(EndOperationTransactionArgs e, ProcessResult processResult) {
  - L1456: public PreparedData(Map<Object, DynamicObject> planYearMap, Map<Object, DynamicObject> planMonthMap) {
  - L1480: private void sendMessage(EndOperationTransactionArgs e, DynamicObject[] successEntrys) {
  - L1564: private Map<String, DynamicObject> getAdminByOrg(String orgNumber, String hierarchy) {
  - L1589: private Map<String, DynamicObject> getlastOrg(String orgNumber, String hierarchy) {
  - L1609: private List<Long> getOrgList(String orgNumber) {

### DeletePlanDetailOp — DeletePlanDetailOp.java

  - L17: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L30: public void onAddValidators(AddValidatorsEventArgs e) {

### DeletePlanOp — DeletePlanOp.java

  - L17: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L23: public void onAddValidators(AddValidatorsEventArgs e) {

### LockPersonOp — LockPersonOp.java

  - L38: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L46: public void beginOperationTransaction(BeginOperationTransactionArgs e) {

### MonthBillCheckOp — MonthBillCheckOp.java

  - L18: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L29: public void onAddValidators(AddValidatorsEventArgs e) {

### MonthBillSaveOp — MonthBillSaveOp.java

  - L16: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L24: public void onAddValidators(AddValidatorsEventArgs e) {

### PlanMonthBzSaveOp — PlanMonthBzSaveOp.java

  - L33: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L55: public void endOperationTransaction(EndOperationTransactionArgs e) {

### PlanYearOp — PlanYearOp.java

  - L18: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L24: public void onAddValidators(AddValidatorsEventArgs e) {

### PlanYearStartOp — PlanYearStartOp.java

  - L18: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L25: public void onAddValidators(AddValidatorsEventArgs e) {

### PositionHrChangeOpPlugin — PositionHrChangeOpPlugin.java

  - L75: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L98: public void endOperationTransaction(EndOperationTransactionArgs e) {
  - L179: private void setPositionName(DynamicObject obj) {

### PositionValidator — PositionValidator.java

  - L13: public void validate() {

### SavePlanOp — SavePlanOp.java

  - L31: public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
  - L71: private String getadminStr(DynamicObject dataEntry, DynamicObject lastOrg, int intHierarchy) {

### SubmitEffectOp — SubmitEffectOp.java

  - L102: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L142: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L275: private void args(PreparePropertysEventArgs e) {
  - L313: private void setAvg(DynamicObject addNew, DynamicObject thisObj) {

### SycnOnPersonOp — SycnOnPersonOp.java

  - L153: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L191: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L547: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L560: private PreparedData prepareData(BeginOperationTransactionArgs e) {
  - L598: private void processAndSaveData(BeginOperationTransactionArgs e, PreparedData preparedData) {
  - L618: private DynamicObject processSingleDataEntity(DynamicObject dataEntity, PreparedData preparedData) {
  - L661: private EmployeeData prepareEmployeeData(DynamicObject planYear, List<Long> adminList, PreparedData preparedData) {
  - L962: public EmployeeData(DataSet empData, List<String> groupList, List<String> fieldList) {
  - L972: private   List<String>  setJUMPFieldList(DynamicObject dgdlPlanObj) {
  - L1034: private List<Long> getAllOrg(Long orgId) {
  - L1049: private boolean isLastOrg(DynamicObject dataEntity, Map<Object, List<DynamicObject>> planYearMap) {
  - L1091: private void setCount(DynamicObject addNew, DynamicObject thisObj, List<String> jumpFieldList) {
  - L1143: private void setAvg(DynamicObject addNew, int count,List<String> jumpFieldList) {

### MonthReportExportTask — MonthReportExportTask.java

  - L30: public void execute(RequestContext requestContext, Map<String, Object> map) throws KDException {
  - L40: private void dataEncapsulation(Map<String, Object> map) {

### PlanMonthBillCreateTask — PlanMonthBillCreateTask.java

  - L67: public void execute(RequestContext requestContext, Map<String, Object> requestMap) throws KDException {
  - L97: private DynamicObject[] getPlanMonths(Map<String, Object> requestMap) {
  - L109: private BatchData prepareBatchData(DynamicObject[] bzBillObjs) {
  - L140: private ArrayList<DynamicObject> processBillObjects(DynamicObject[] bzBillObjs, BatchData batchData) {
  - L186: private Map<String, Integer> calculateEmployeeCount(DynamicObject planMonth, List<Long> orgList, Set<String> orgStructNu
  - L431: private String getClosestOrgStructNumber(String structNumber, String structlongnumber, Set<String> orgStructNumbers) {
  - L448: private void aggregateToParentOrgs(List<DynamicObject> bzObjs, Map<String, Integer> countMap, String hierarchy) {
  - L497: private void updateBillActualCount(List<DynamicObject> bzObjs, Map<String, Integer> countMap) {
  - L541: private Map<String, List<Long>>  getOrgList(Set<String> orgNumberList) {
  - L557: private Map<String, List<DynamicObject>> getAdminByOrg(Set<String> orgNumberList) {

### YearReportExportClickTask — YearReportExportClickTask.java

  - L21: public void click(ClickEventArgs e) {

### YearReportExportTask — YearReportExportTask.java

  - L152: public void execute(RequestContext requestContext, Map<String, Object> map) throws KDException {
  - L370: private void dataEncapsulation(Map<String, Object> map) {
  - L421: private DataSet getEmpDataSet(Long orgId, Date syndate) {
  - L433: private DataSet getPersonRankDataSet(DataSet empDataSet) {
  - L445: private DataSet getJobLabelDataSet() {
  - L453: private DynamicObject[] getDetailObjects(List<Long> pkValueList) {
  - L461: private void processDetailData(DynamicObject detailData, DataSet empDataSet, DynamicObject planYear, Date syndate, DataS
  - L588: private List<Long> getAllOrg(Long orgId) {
  - L609: private DataSet getDataSet(DataSet empData, DynamicObject planYear, Date syndate, DynamicObject lastOrg, Map<Long, Strin
  - L704: private void setAvg(DynamicObject addNew, int count) {

### DeletePlanDetailValidator — DeletePlanDetailValidator.java

  - L31: public void validate() {

### DeletePlanValidator — DeletePlanValidator.java

  - L36: public void validate() {

### MonthBillCheckValidator — MonthBillCheckValidator.java

  - L25: public void validate() {
  - L199: private DynamicObjectCollection getDynamicObjects(QFilter planfilter, List parentorglist, String parentorgkey) {
  - L203: private DynamicObjectCollection getObjects(QFilter planfilter, List parentorglist, String parentorgkey) {

### MonthBillSaveValidator — MonthBillSaveValidator.java

  - L28: public void validate() {
  - L84: private void groupAdmin(Map<String, List<DynamicObject>> classifyData, DynamicObject planMonth) {

### PlanYearSaveValidator — PlanYearSaveValidator.java

  - L35: public void validate() {

### PlanYearStartValidator — PlanYearStartValidator.java

  - L36: public void validate() {

### PlanPersonImportOp — PlanPersonImportOp.java

  - L30: public void beginOperationTransaction(BeginOperationTransactionArgs e) {

### BatchYearImportEdit — BatchYearImportEdit.java

  - L33: public void afterBindData(EventObject e) {
  - L47: public void queryImportBasedata(QueryImportBasedataEventArgs e) {

### PlanImportFormPlugin — PlanImportFormPlugin.java

  - L37: public void afterBindData(EventObject e) {
  - L49: public void queryImportBasedata(QueryImportBasedataEventArgs e) {

### PlanMonthBillBzFormPlugin — PlanMonthBillBzFormPlugin.java

  - L42: public void queryImportBasedata(QueryImportBasedataEventArgs e) {

### PlanMonthBillBzImportPlugin — PlanMonthBillBzImportPlugin.java

  - L22: public List<ComboItem> getOverrideFieldsConfig() {
  - L32: public String getDefaultKeyFields() {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
