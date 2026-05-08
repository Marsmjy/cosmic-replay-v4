# payroll_sit_socialsecurity · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### SinsurfileHelper — SinsurfileHelper.java

  - L49: private static HashMap<String, BigDecimal> getsocialBaseValueMap() {
  - L62: private static HashMap<String, HashMap<String, Object[]>> getInsuranceConfigs() {
  - L70: private static HashMap<String, Object[]> getFundConfigs() {
  - L78: private static HashMap<String, Object[]> getSocialConfigs() {
  - L92: public Long doSinsurAgnetPay(DynamicObject bill, HashMap<String, Long> insuranceProp) {
  - L166: public void stopSocialFileByValue(long sinsurfileid, Date effectiveDate, String insuranceType, DynamicObject bill, HashM
  - L233: public void stopSocialFile(long sinsurfileid, Date effectiveDate, String insuranceType, HashMap<String, Long> insuranceP
  - L291: public void alterSocialFileBase(DynamicObject bill, HashMap<String, Long> insuranceProp) {
  - L354: private DynamicObject[] addNewFile(DynamicObject bill) {
  - L400: protected void addNewFileBase(DynamicObject bill, DynamicObject newFile, String uniqueCode, HashMap<String, Long> insura
  - L461: private Long getSinSurstdandarId(DynamicObject bill) {
  - L528: protected HashMap<String, Long> getWelfaretypeMap() {
  - L544: public static HashMap<String, Long> getInsuranceProp() {
  - L558: protected void checkServiceResult(Map<String, Object> serviceResult) {
  - L570: public void addSocialFileBase(Long sinsurfileid, DynamicObject bill, HashMap<String, Long> insuranceProp) {

### SocialSecurityFundCommon — SocialSecurityFundCommon.java

  - L42: public static Set<Long> getBUInsuredUnitIds(String createOrgNumber) {

### SocialSecurityFundFormPlugin — SocialSecurityFundFormPlugin.java

  - L34: public void registerListener(EventObject e) {
  - L46: public void afterBindData(EventObject e) {
  - L54: public void propertyChanged(PropertyChangedArgs e) {
  - L70: public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
  - L109: private QFilter getUniqueVerificationOfInsuredUnit() {
  - L117: public void beforeItemClick(BeforeItemClickEvent evt) {
  - L129: public void beforeDoOperation(BeforeDoOperationEventArgs args) {
  - L136: private void showChangeReasonByChangeBtn() {
  - L153: private void hideAndMustInputFieldByCreatOrg() {
  - L189: private void setMustInput(String[] fields){
  - L206: private void cancelMustInput(String[] fields){
  - L223: private String getCreateOrgNumber() {

### SocialSecurityFundListPlugin — SocialSecurityFundListPlugin.java

  - L13: public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {

### SocialSecurityPaybillListPlugin — SocialSecurityPaybillListPlugin.java

  - L34: public void beforeItemClick(BeforeItemClickEvent evt) {

### SocialSecurityPayClickNumberPlugin — SocialSecurityPayClickNumberPlugin.java

  - L30: public void registerListener(EventObject e) {
  - L36: public void click(EventObject evt) {
  - L81: public static Map<String, String> queryAllFileRelatedPkId(DynamicObject fileDy) {
  - L96: public static void addHisCurrFilter(QFilter filter) {
  - L105: public void beforeDoOperation(BeforeDoOperationEventArgs args) {

### SocialSecurityPayFormPlugin — SocialSecurityPayFormPlugin.java

  - L51: public void afterBindData(EventObject e) {
  - L112: public static HashMap<String, HashMap<String, String[]>> getInsuranceConfigs(HashMap<String, String[]> socialConfigs, Ha
  - L119: public static HashMap<String, String[]> getFundConfigs() {
  - L125: public static HashMap<String, String[]> getSocialConfigs() {
  - L137: public void propertyChanged(PropertyChangedArgs e) {
  - L208: private void pagePaymentDetails(String name, PropertyChangedArgs e) {
  - L269: private void baseAndSetEntry(DynamicObject dataEntity, String newValue, String insurTypeList) {
  - L317: private void stopAndSetEntry(DynamicObject dataEntity, String newValue) {
  - L364: private void deleteAndAddEntryentity(String paymentInsuranceArg, String insurTypeList) {
  - L386: private void personChange(String name) {
  - L434: private void getSocialSecurityBase(DynamicObject kdcdPerson) {
  - L471: private void setSuspensionPaymentInsurance(DynamicObject personDynmi) {
  - L523: private void setPaymentInsurance(DynamicObject personDynmi) {
  - L605: private void setDropDownBox(ArrayList<String> paymentInsuranceList, boolean flag) {
  - L637: private void setPersonInfo(DynamicObject personDynmic) {

### SocialSecurityPayOperationPlugin — SocialSecurityPayOperationPlugin.java

  - L25: public void   onPreparePropertys(PreparePropertysEventArgs e) {
  - L78: public void onAddValidators(AddValidatorsEventArgs e) {
  - L84: public void beginOperationTransaction(BeginOperationTransactionArgs e) {

### SocialSecurityPayValidator — SocialSecurityPayValidator.java

  - L40: public void initializeConfiguration() {
  - L47: public void validate() {
  - L178: private static void checkEntryFiled(ArrayList<String> insurTypeList, StringBuffer errmessage, DynamicObject dynamicObjec
  - L204: private static void checkEntryFiledYX(ArrayList<String> insurTypeList, StringBuffer errmessage, DynamicObject dynamicObj

### SocialSecurityPersonFilterFormPlugin — SocialSecurityPersonFilterFormPlugin.java

  - L38: public void registerListener(EventObject e) {
  - L52: public void beforeF7Select(BeforeF7SelectEvent f7) {
  - L140: public void afterF7Select (AfterF7SelectEvent afterF7SelectEvent){
  - L279: private void setEntryFiledEnable (DynamicObject paymentServiceType){
  - L313: private String getOrgNumber() {

### OperateResultCheck — OperateResultCheck.java

  - L14: public static void check(OperationResult operationResult, String bizName)

### PersonOrgUtil — PersonOrgUtil.java

  - L17: public static DynamicObject getBUByCurrentOrgId(long orgId) {

### StringHelper — StringHelper.java

  - L16: public static String toSql(DynamicObjectCollection collection)
  - L35: public static String longIdToSql(DynamicObjectCollection collection)
  - L40: public static String longIdToSql(DynamicObjectCollection collection, String propertyName)
  - L59: public static String toSql(List list)
  - L82: public static String longIdToSql(Collection list)
  - L88: public static String longIdToSql(List list)
  - L111: public static ArrayList<String> getList(String... elements)
  - L121: public static ArrayList<Long> getList(long... elements)
  - L131: public static ArrayList<Object> getObjList(Object... elements)
  - L143: public static String join(Object[] srcValues)
  - L148: public static String join(Object[] srcValues, String splitor, boolean isgoreEmpty)

### TypeConvertUtil — TypeConvertUtil.java

  - L17: public static String objToString(Object obj)
  - L26: public static BigDecimal objToBigDecimal(Object obj)
  - L49: public static boolean objToBoolean(Object obj)
  - L68: public static byte objToByte(Object obj)
  - L76: public static short objToShort(Object obj)
  - L84: public static int objToInt(Object obj)
  - L111: public static long objToLong(Object obj)
  - L136: public static float objToFloat(Object obj)
  - L161: public static double objToDouble(Object obj)
  - L186: public static Time objToTime(Object obj)
  - L241: public static Date parseDate(String s)
  - L282: public static Timestamp objToTimestamp(Object obj)
  - L307: public static byte[] objToBytes(Object obj)

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
