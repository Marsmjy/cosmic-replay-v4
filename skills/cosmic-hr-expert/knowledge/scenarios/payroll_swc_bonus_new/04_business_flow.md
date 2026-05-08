# payroll_swc_bonus_new · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### PersonOrgUtil — PersonOrgUtil.java

  - L14: public static long getUserPersonId(long userId) {
  - L31: public static DynamicObject getBUByCurrentOrgId(long orgId) {

### SWCI18NParam — SWCI18NParam.java

  - L17: public SWCI18NParam() {
  - L20: public SWCI18NParam(String description, String resourceID, String systemType) {
  - L26: public String loadKDString() {
  - L30: public LocaleString getLocaleString() {
  - L34: public String loadKDString(Object... params) {
  - L38: public String getDescription() {
  - L42: public void setDescription(String description) {
  - L46: public String getResourceID() {
  - L50: public void setResourceID(String resourceID) {
  - L54: public String getSystemType() {
  - L58: public void setSystemType(String systemType) {

### BonusUtil — BonusUtil.java

  - L59: public static boolean batchSendMsg(Map<Long, String> groupSendMsgMap, Date currentDate, String linkOpenEntityId, String
  - L126: public static void openForm(IFormView view,String entityName,Map<String, Object> customMap) {
  - L134: public static void openBill(IFormView view, Long pkId, String formId, Map<String,Object> customData) {
  - L152: public static void openInfoBill(IFormView view, Long pkId, String formId, Map<String,Object> customData) {
  - L176: public static String afterExpectQueryAllField(String entityName, String... fields) {
  - L183: public static void updateBill(DynamicObject[] bills){
  - L193: public static Object[] saveBill(DynamicObject[] bills) {
  - L204: public static DynamicObject getCurrentUserErmanfile() {
  - L220: public static DynamicObject getCurrentErmanfile(String personNumber) {

### PushSalaryUtil — PushSalaryUtil.java

  - L24: public static HashMap<Long, ArrayList<String>> dealResult(Map<String, Object> result) {

### TdkwSendMessageUtil — TdkwSendMessageUtil.java

  - L25: public static boolean sendMsg(Map<String, Object> messageParams,List<Map<String,Object>> messageData) {

### BonusDisSumAllPlugin — BonusDisSumAllPlugin.java

  - L49: public void entryRowClick(RowClickEvent evt) {
  - L73: public void registerListener(EventObject e) {
  - L86: public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
  - L115: private void filterOrgTreeByF7Limit(ListShowParameter showParameter,QFilter orgFilter){
  - L130: public void afterBindData(EventObject e) {
  - L140: public void afterLoadData(EventObject e) {
  - L150: public void afterCreateNewData(EventObject e) {
  - L159: public void propertyChanged(PropertyChangedArgs e) {
  - L175: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L182: public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
  - L194: private void valueChangeCreateStaffSelect(){
  - L220: private void setCreatorDefault(){

### BonusDistributeSumOperatePlugin — BonusDistributeSumOperatePlugin.java

  - L64: public void onAddValidators(AddValidatorsEventArgs e) {
  - L69: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L83: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L106: private void revokeOperate(DynamicObject[] entities) {
  - L115: private static void createApprovalBills(DynamicObject[] entities) {
  - L175: private static void cleanDistributeCache(List<String> distributeSumIdList) {
  - L192: private static void compositeOperate(DynamicObject[] entities) {
  - L200: private static void setHiddenInfo(Date currentDate, long currUserId, DynamicObject approvalSumBill, List<String> distrib
  - L212: private static void setDistributeBaseInfo(DynamicObject[] entities, DynamicObject approvalSumBill, String billNo) {
  - L249: private static void setCreatorDefault(DynamicObject approvalSumBill){
  - L265: private static void setAwardEntry(DynamicObject[] entities, DynamicObject approvalSumBill) {
  - L300: private static DynamicObject[] listDisSumIdsByNos(Set<String> sumNoSet){
  - L316: private static DynamicObject[] listDisSumByNos(Set<Long> sumNoSet){
  - L327: private static void setOverviewInfo(DynamicObject[] entities, DynamicObject approvalSumBill) {

### BonusSumApprovalOperatePlugin — BonusSumApprovalOperatePlugin.java

  - L54: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L68: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L101: public void endOperationTransaction(EndOperationTransactionArgs e) {
  - L117: private static void syncAuditStatus(DynamicObject[] dataEntities) {
  - L139: private void discard(DynamicObject[] dataEntities) {
  - L160: public List<DynamicObject> creatBounsPayments(DynamicObject[] dataEntities,String isChecked) {
  - L306: public List<DynamicObject> creatDepartmentFunds(DynamicObject[] dataEntities,String isChecked) {
  - L403: private static void checkOperationResult(OperationResult auditResult,String message) {

### OperateDistributeCacheUtil — OperateDistributeCacheUtil.java

  - L14: public static <T> void operateCacheTryLock(InvokeFunction<T,Boolean> function, String key, long waitTime){

### BonusDistributeSumValidatePlugin — BonusDistributeSumValidatePlugin.java

  - L25: public Set<String> preparePropertys() {
  - L35: public void validate() {
  - L58: private void checkBillStatus() {
  - L63: private void checkDistributeRatio() {
  - L74: private void checkSameApprovalBillNo() {
  - L82: private void checkRepeatCreate() {
  - L87: private void checkStatus() {

### BonusDistributeBillPlugin — BonusDistributeBillPlugin.java

  - L51: public void registerListener(EventObject e) {
  - L59: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L79: public void afterLoadData(EventObject e) {
  - L84: public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
  - L105: public void afterBindData(EventObject e) {
  - L131: private void checkBillStatus(){
  - L139: private void rejectOperatePerson() {
  - L167: public void openForm(IFormView view, String entityName, Map<String, Object> customMap) {
  - L178: public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
  - L187: private void rejectOperateDept() {
  - L216: public static Map<String, Object> createCustomMap(Set<DynamicObject> sourceOfHDSet,String keyName) {
  - L231: public void hyperLinkClick(HyperLinkClickEvent hyperLinkClickEvent) {
  - L248: private void hyperLinkOpenBill(HyperLinkClickEvent hyperLinkClickEvent,String linkId,String openBillName,String entryNam

### BonusDistributeSumListPlugin — BonusDistributeSumListPlugin.java

  - L63: public void setFilter(SetFilterEvent e) {
  - L110: public void billListHyperLinkClick(HyperLinkClickArgs args) {
  - L120: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L145: public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
  - L159: public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
  - L184: public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
  - L221: public void beforePackageData(BeforePackageDataEvent e) {
  - L248: private void urgingOperate(Date currentDate, List<DynamicObject> urgeList) {
  - L277: private void checkEnableUrge(DynamicObject[] sumDistributeArrays) {
  - L287: private void hyperLinkOpenBill(HyperLinkClickArgs args,String selectField,String linkId,String openBillName) {

### BonusPaymentBillPlugin — BonusPaymentBillPlugin.java

  - L29: public void registerListener(EventObject e) {
  - L35: public void cellClick(CellClickEvent cellClickEvent) {
  - L63: public void cellDoubleClick(CellClickEvent cellClickEvent) {

### BonusPaymentImportPlugin — BonusPaymentImportPlugin.java

  - L33: public List<String> getDefaultLockUIs() {
  - L41: public String getDefaultImportType() {
  - L46: protected ApiResult save(List<ImportBillData> rowdatas, ImportLogger logger) {

### BounsPaymentListPlugin — BounsPaymentListPlugin.java

  - L42: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L119: public void afterDoOperation(AfterDoOperationEventArgs args) {
  - L172: private void showBonusPlanApproval(String formId, Object pkId) {
  - L185: private void showBonusRecords(String formId, List<Long> pkId) {
  - L199: public void createAgentApprove(DynamicObject[] entities) {
  - L228: public void createEndApprove(DynamicObject[] entities) {
  - L256: public void createBoundsRecord(DynamicObject[] entities) {

### BounsPaymentUtils — BounsPaymentUtils.java

  - L43: public static DynamicObject[] getBounsAgentApprove(List<Long> ids){
  - L58: public static DynamicObject[] getBounsEndApprove(List<Long> ids){
  - L73: public static DynamicObject[] getBounsRecord(List<Long> ids){
  - L88: public static DynamicObject[] getBounsRecordAll(List<Long> ids){
  - L106: public static DynamicObject[] getBounsAgentRecordAll(List<Long> ids){
  - L124: public static DynamicObject[] getBounsEndRecordAll(List<Long> ids){
  - L143: public static List<Long> getBounsAgentApproveSourceIds(List<Long> ids){
  - L159: public static List<Long> getBounsEndApproveSourceIds(List<Long> ids){
  - L175: public static List<Long> getBounsRecordSourceIds(List<Long> ids){
  - L191: public static DynamicObject[] getErmanfile(Long userId){
  - L207: public static List<String> bounsPaymentvalidtor(List<Long> ids,String operateKey){
  - L318: public static List<String> bounsPaymentvalidtorExtA(List<Long> ids,String operateKey){
  - L363: private static DynamicObject[] getBonusPaymentObjecys(List<Long> ids) {
  - L370: public static HashMap<Long, List<String>> bounsPaymentvalidtorExtB(List<Long> ids){
  - L419: public static Map<String, String> pushItem(DynamicObjectCollection entryBills,Long currencyId,String batchcode,Date effD

### BonusPlanImportPlugin — BonusPlanImportPlugin.java

  - L31: public List<String> getDefaultLockUIs() {
  - L40: public String getDefaultImportType() {
  - L45: protected void beforeSave(List<ImportBillData> billdatas, ImportLogger logger) {

### CreateApproval — CreateApproval.java

  - L48: public void registerListener(EventObject e) {
  - L53: public void setFilter(SetFilterEvent e) {
  - L110: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L154: public void afterDoOperation(AfterDoOperationEventArgs args) {
  - L175: private void openBonusApprovedFrom(DynamicObject bonusPlan) {
  - L191: public void billListHyperLinkClick(HyperLinkClickArgs args) {
  - L205: private void showBonusPlanApproval(DynamicObject bill) {
  - L218: public void beforePackageData(BeforePackageDataEvent e) {

### FilterItemByType — FilterItemByType.java

  - L51: public void registerListener(EventObject e) {
  - L62: public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
  - L100: public void afterCreateNewData(EventObject e) {
  - L108: private void setPersonalRewards() {
  - L140: public void afterBindData(EventObject e) {
  - L186: public void propertyChanged(PropertyChangedArgs e) {
  - L282: public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
  - L306: private void showBonusPlanApproval(DynamicObject bill) {
  - L318: private void isMustInput(String filedKey,Boolean isMustInput) {
  - L323: private void setVisible(String filedKey,Boolean isVisible){

### BonusPlanApprovalBillPlugin — BonusPlanApprovalBillPlugin.java

  - L45: public void registerListener(EventObject e) {
  - L59: public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
  - L84: private void filterOrgTreeByF7Limit(ListShowParameter showParameter,QFilter orgFilter){
  - L99: public void afterBindData(EventObject e) {
  - L115: public void propertyChanged(PropertyChangedArgs e) {
  - L142: public void cellClick(CellClickEvent cellClickEvent) {
  - L168: public void cellDoubleClick(CellClickEvent cellClickEvent) {

### BonusPlanApprovalOperationPlugin — BonusPlanApprovalOperationPlugin.java

  - L28: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L39: public void beginOperationTransaction(BeginOperationTransactionArgs e) {

### BounsAgentApproveBillPlugin — BounsAgentApproveBillPlugin.java

  - L21: public void registerListener(EventObject e) {
  - L28: public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {

### BounsImportBillPlugin — BounsImportBillPlugin.java

  - L38: public void queryImportBasedata(QueryImportBasedataEventArgs e) {
  - L55: public void beforeImportEntry(BeforeImportEntryEventArgs e) {
  - L78: private void validateEntry(List<ImportEntryData> entryList, ImportLogger importLogger) {
  - L283: public static String getFieldStringValue(JSONObject data, String mainDataKey, String fieldName) {
  - L300: public static void setFieldValue(JSONObject data,String mainDataKey, String fieldName,Object fieldValue){

### BounsRecodBillPlugin — BounsRecodBillPlugin.java

  - L57: private static Set<String> getEntryFileds() {
  - L68: public void registerListener(EventObject e) {
  - L75: public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
  - L99: public void afterBindData(EventObject e) {
  - L110: public void propertyChanged(PropertyChangedArgs e) {
  - L195: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L273: public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
  - L286: public void repush(DynamicObjectCollection entryentities){
  - L349: public void distributeBack(DynamicObjectCollection entryentities){
  - L401: public void discard(DynamicObjectCollection entryentities){
  - L423: public void validator(DynamicObjectCollection entryentities,List<String> statusList,String errorMsg){
  - L438: private DynamicObject[] updateBounsPayment(DynamicObjectCollection entryentities, List<Long> sourceids) {
  - L491: public void setRecordInfo(DynamicObject bill){
  - L510: public void beforeImportEntry(BeforeImportEntryEventArgs e) {
  - L536: private void validateEntry(List<ImportEntryData> entryList, ImportLogger importLogger) {

### PayrollPushUtil — PayrollPushUtil.java

  - L25: private static ArrayList<String> getSalaryCalcStyle() {
  - L30: private static ArrayList<String> getSalaryFileDataStatusList() {
  - L42: public static Map<String, Map<String, Object>> getEffectiveSalaryFile(Set<String> personNumberList, Set<String> accountO

### BounsRecordOperation — BounsRecordOperation.java

  - L38: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L45: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L123: private static void setSalaryFile(DynamicObject[] entities) {
  - L151: private static DynamicObject[] getPayPersons(DynamicObject[] entities,List<Long> sourceId) {
  - L207: private static void validator(DynamicObject[] entities,List<Long> sourceId) {

### BounsImportOperation — BounsImportOperation.java

  - L38: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L45: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L172: private OperationResult getOperationResult(List<DynamicObject> saveDataList) {
  - L179: private OperationResult getOperationResult(List<DynamicObject> saveDataList, OperateOption operateOption) {
  - L186: private static void validator(DynamicObject entity ,List<Long> sourceId, Map<Long, DynamicObject> map) {

### BounsEndApproveOperation — BounsEndApproveOperation.java

  - L27: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L36: public void beginOperationTransaction(BeginOperationTransactionArgs e) {

### BounsAgentApproveOperation — BounsAgentApproveOperation.java

  - L24: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L31: public void beginOperationTransaction(BeginOperationTransactionArgs e) {

### BonusPlanConvertPlugin — BonusPlanConvertPlugin.java

  - L33: public void afterGetSourceData(AfterGetSourceDataEventArgs e) {
  - L67: public void afterConvert(AfterConvertEventArgs e) {

### BonusPlanOperation — BonusPlanOperation.java

  - L30: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L44: public void onAddValidators(AddValidatorsEventArgs e) {
  - L48: public void afterExecuteOperationTransaction(AfterOperationArgs e) {

### BonusPlanOtherOperation — BonusPlanOtherOperation.java

  - L31: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L45: public void onAddValidators(AddValidatorsEventArgs e) {
  - L50: public void beginOperationTransaction(BeginOperationTransactionArgs e) {

### BonusPlanValidator — BonusPlanValidator.java

  - L29: public void validate() {

### BounsAgentConvertPlugin — BounsAgentConvertPlugin.java

  - L29: public void afterGetSourceData(AfterGetSourceDataEventArgs e) {
  - L40: public void afterConvert(AfterConvertEventArgs e) {

### BounsEndConvertPlugin — BounsEndConvertPlugin.java

  - L29: public void afterGetSourceData(AfterGetSourceDataEventArgs e) {
  - L40: public void afterConvert(AfterConvertEventArgs e) {

### BounsRecordConvertPlugin — BounsRecordConvertPlugin.java

  - L23: public void afterGetSourceData(AfterGetSourceDataEventArgs e) {
  - L34: public void afterConvert(AfterConvertEventArgs e) {

### BounsPaymentAgentOperation — BounsPaymentAgentOperation.java

  - L26: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L38: public void onAddValidators(AddValidatorsEventArgs e) {
  - L44: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L75: public void createAgentApprove(DynamicObject[] entities) {
  - L104: public void createEndApprove(DynamicObject[] entities) {
  - L132: public void createBoundsRecord(DynamicObject[] entities) {

### BounsPaymentValidator — BounsPaymentValidator.java

  - L22: public void validate() {

### BonusDisListPlugin — BonusDisListPlugin.java

  - L45: public void registerListener(EventObject e) {
  - L50: public void setFilter(SetFilterEvent e) {
  - L56: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L121: public void openForm(IFormView view, String entityName, Map<String, Object> customMap) {
  - L131: public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
  - L142: public void afterDoOperation(AfterDoOperationEventArgs args) {
  - L148: public void billListHyperLinkClick(HyperLinkClickArgs args) {
  - L153: public void beforePackageData(BeforePackageDataEvent e) {

### BonusDisRejectPlugin — BonusDisRejectPlugin.java

  - L49: public void beforeBindData(EventObject e) {
  - L118: private DisNode(String billNo, String billNoPath, int pathLevel){
  - L124: public String getBillNo() {
  - L128: public void setBillNo(String billNo) {
  - L132: public String getBillNoPath() {
  - L136: public void setBillNoPath(String billNoPath) {
  - L140: public int getPathLevel() {
  - L144: public void setPathLevel(int pathLevel) {
  - L154: private List<String> listTreeMinNode(List<String> yesBillNoList){
  - L195: private DynamicObject[] listAllNext(List<String> yesBillNoList){
  - L214: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L220: public void afterDoOperation(AfterDoOperationEventArgs args) {
  - L247: private void handleReject(DynamicObject dataEntity){
  - L318: private void handleSumThenRefresh(Set<String> disBillSumNos,Set<String> fistBillNoSet,Set<String> secondBillNoSet){
  - L362: private DynamicObject[] listByBillNos(Set<String> yesBillNoSet){

### BonusDisSecondPlugin — BonusDisSecondPlugin.java

  - L53: public void registerListener(EventObject e) {
  - L78: public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
  - L131: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L175: public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
  - L216: public void afterLoadData(EventObject e) {
  - L223: public void afterBindData(EventObject e) {
  - L246: public void propertyChanged(PropertyChangedArgs e) {
  - L294: private void orgKeepChange(ChangeData changeData) {
  - L314: private void adminOrgChange(ChangeData changeData) {
  - L350: private void filterOrgTreeByF7Limit(ListShowParameter showParameter,QFilter orgFilter){
  - L368: private void employeeChange(ChangeData changeData) {
  - L406: private void updatePersonUnderTakeOrg(int rowIndex){
  - L431: private void updateOrgUnderTakeOrg(int rowIndex){
  - L460: private DynamicObject getUndertakeOrg(DynamicObject dataEntity,DynamicObject rowObject,String entryOrgKey,String entryJy
  - L495: private void updateKeepUnderTakeOrg(int rowIndex){

### TransferToHandleDisTask — TransferToHandleDisTask.java

  - L22: public void registerListener(EventObject e) {
  - L27: public void click(EventObject evt) {

### BonusDisSecondPushPlugin — BonusDisSecondPushPlugin.java

  - L57: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L70: public void onAddValidators(AddValidatorsEventArgs e) {
  - L76: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L164: private Object[] pushToDisSum(DynamicObject bill, Map<String, DynamicObject> disSumMap){
  - L251: private DynamicObjectCollection listManualHandles(DynamicObject bill,String entryKey,String lockKey){
  - L264: private List<DynamicObject> genDistributeByDeptDetailNext(Date currentTime, DynamicObject bill, Map<String, DynamicObjec
  - L401: private void setCreatorDefaultNext(DynamicObject distributeDynamicObject,DynamicObject entry,Map<String, DynamicObject>
  - L419: private void genDistributeEntryNext(DynamicObject distributeDynamicObject, List<DynamicObject> personDetailCollection, L

### BonusDisSecValidator — BonusDisSecValidator.java

  - L23: public void validate() {
  - L52: private void checkEntryRepeat(DynamicObject distributeDynamicObject){
  - L82: private void checkMoneyConsumeEnd(DynamicObject dataEntity){
  - L96: private void checkPsRelation(ExtendedDataEntity bill){

### BonusApprovalSheetBillListPlugin — BonusApprovalSheetBillListPlugin.java

  - L48: public void registerListener(EventObject e) {
  - L53: public void setFilter(SetFilterEvent e) {
  - L96: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L101: public void afterDoOperation(AfterDoOperationEventArgs args) {
  - L129: public void billListHyperLinkClick(HyperLinkClickArgs args) {
  - L172: public void beforePackageData(BeforePackageDataEvent e) {

### BonusApprovalSheetBillSecondFlowPlugin — BonusApprovalSheetBillSecondFlowPlugin.java

  - L47: public void registerListener(EventObject e) {
  - L60: public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
  - L90: private void filterOrgTreeByF7Limit(ListShowParameter showParameter,QFilter orgFilter){
  - L109: public void propertyChanged(PropertyChangedArgs e) {
  - L122: public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
  - L136: private void valueChangeCreateStaffSelect(){
  - L156: public void hyperLinkClick(HyperLinkClickEvent hyperLinkClickEvent) {

### BonusApprovalSheetBillSecondPlugin — BonusApprovalSheetBillSecondPlugin.java

  - L62: private static List<String> getqueryPlanFieldKeyList() {
  - L100: private EntriesAmountSumRelation(String entriesKey, String entriesItemKey, String sumTargetKey,String underTakeOrgKey){
  - L107: public String getEntriesKey() {
  - L111: public void setEntriesKey(String entriesKey) {
  - L115: public String getEntriesItemKey() {
  - L119: public void setEntriesItemKey(String entriesItemKey) {
  - L123: public String getSumTargetKey() {
  - L127: public void setSumTargetKey(String sumTargetKey) {
  - L131: public String getUnderTakeOrgKey() {
  - L135: public void setUnderTakeOrgKey(String underTakeOrgKey) {
  - L141: public static List<EntriesAmountSumRelation> getEntriesAmountSumRelations() {
  - L155: public void registerListener(EventObject e) {
  - L181: public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
  - L243: public void afterBindData(EventObject e) {
  - L265: public void afterLoadData(EventObject e) {

### BonusWriteBackUtil — BonusWriteBackUtil.java

  - L29: public static void updateBonusPlanSure(List<Long> checkIdList) {
  - L73: public static DynamicObject getBosUser(DynamicObject subordinate){
  - L89: public static List<String> checkPsRelationCommon(DynamicObject dataEntity,String entryKey,String propertyName,String num
  - L126: public static List<String> checkPsRelationCommonExt(DynamicObject dataEntity,String entryKey,String propertyName,String
  - L148: public static void setSumBillName(DynamicObject distributeSumDynamicObject){
  - L164: public static void setDistributeBillName(DynamicObject distributeSumDynamicObject){
  - L181: public static void setParentFromHd(DynamicObject distributeSumDynamicObject,DynamicObject bill){
  - L204: public static List<Long> listOrgDutyPersonIds(long adminOrgId){
  - L215: private static DynamicObject[] getChargepeople(long adminOrgId) {
  - L225: private static DynamicObject[] getChargepeople(QFilter filter) {
  - L236: public static DynamicObjectCollection listOrg(Set<String> orgNumberSet){
  - L250: public static DynamicObjectCollection adminKeeplistOrg(Set<String> orgNumberSet){
  - L265: public static DynamicObjectCollection listStaff(Set<String> staffNumberSet){
  - L277: public static DynamicObjectCollection queryErmanFile(Set<Long> hrpiEmpposorgrels){

### BonusApprovalConvertPlugin — BonusApprovalConvertPlugin.java

  - L34: public void afterGetSourceData(AfterGetSourceDataEventArgs e) {
  - L69: public void afterConvert(AfterConvertEventArgs e) {

### BonusApprovalAuditPlugin — BonusApprovalAuditPlugin.java

  - L39: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L48: public void onAddValidators(AddValidatorsEventArgs e) {
  - L53: public void beginOperationTransaction(BeginOperationTransactionArgs e) {

### BonusApprovalSheetBillSecondPushPlugin — BonusApprovalSheetBillSecondPushPlugin.java

  - L72: public void onPreparePropertys(PreparePropertysEventArgs e) {
  - L85: public void onAddValidators(AddValidatorsEventArgs e) {
  - L91: public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  - L220: public void afterExecuteOperationTransaction(AfterOperationArgs e) {
  - L268: private DynamicObject[] createDistributeBill(DynamicObject[] entities, Date currentTime, DynamicObject bosUser, Map<Stri
  - L280: private List<DynamicObject> genDistributeByDeptDetail(Date currentTime, DynamicObject bill, DynamicObject bosUser, Map<S
  - L408: private static void setCreatorDefault(DynamicObject distributeDynamicObject,DynamicObject entry) {
  - L426: private static void setCreatorDefaultSum(DynamicObject distributeDynamicObject) {
  - L445: private void genDistributeEntry(DynamicObject distributeDynamicObject, List<DynamicObject> personDetailCollection, List<
  - L493: private DynamicObject[] createDistributeSumBill(DynamicObject[] entities, Date currentTime, DynamicObject bosUser, Map<S
  - L506: private static List<DynamicObject> genDistributeSumByPersonDetail(Date currentTime, DynamicObject bill, DynamicObject bo
  - L585: private static List<DynamicObject> genDistributeSumByDeptDetail(Date currentTime, DynamicObject bill, DynamicObject bosU
  - L622: private static void genDistributeSumEntry(DynamicObject bill, DynamicObject distributeSumDynamicObject, List<DynamicObje
  - L672: private static void genDistributeSumEntryByPersonEntry(DynamicObject bill, DynamicObject distributeSumDynamicObject, Set
  - L721: private static void setBaseInfo(DynamicObject bill, DynamicObject distributeSumDynamicObject) {

### BonusApprovalSecValidator — BonusApprovalSecValidator.java

  - L30: public Set<String> preparePropertys() {
  - L39: public void validate() {
  - L161: public void submitCheck(ExtendedDataEntity bill){
  - L191: private void checkOrgOrStaffRepeat(DynamicObject dataEntity){
  - L222: private void checkApprovalValid(DynamicObject dataEntity){
  - L238: private void checkApprovalStaffValid(DynamicObject dataEntity){
  - L295: private void checkPsRelation(ExtendedDataEntity bill){

### CalendarUtil — CalendarUtil.java

  - L21: public static Date queryMonthFirstDay(Date date){
  - L42: public static Date queryMonthLastDay(Date date){

### ObjectUtils — ObjectUtils.java

  - L15: public static boolean isNotEmpty(Object object) {
  - L18: public static boolean isApacheEmpty(Object object) {

### PersonOrgUtil — PersonOrgUtil.java

  - L17: public static DynamicObject getBUByCurrentOrgId(long orgId) {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
