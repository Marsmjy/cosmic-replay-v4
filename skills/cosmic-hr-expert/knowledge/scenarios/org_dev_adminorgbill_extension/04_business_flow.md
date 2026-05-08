# org_dev_adminorgbill_extension · 业务流程

> **数据源**：deep_scan_class_tree.md + deep_scan_audit.md D 段

## 调用链概览

基于下方各类的方法签名·补完整时序图（HR 操作 → BillPlugin → OpPlugin → 数据落库）

## 各类方法清单（来自 class_tree）

### AdminOrgBatchDetailDealSourceInfoFormPlugin — AdminOrgBatchDetailDealSourceInfoFormPlugin.java

  - L29: public void afterBindData(EventObject e) {
  - L82: private void setValue(String textCoin,Object oriValue,String showValue){
  - L87: private void showOrHideFlex(DynamicObject info, DynamicObject orgInfo) {

### AdminOrgShowMobBillPlugin — AdminOrgShowMobBillPlugin.java

  - L41: public void afterBindData(EventObject e) {
  - L54: private void updateTabPageText() {
  - L82: private void dealChangeTypeData(OrgChgBillMobileBo orgChgBillMobileBo, List<DynamicObject> entryData) {
  - L118: private void showAddEntryData(OrgChgBillMobileBo orgChgBillMobileBo, List<DynamicObject> entryData) {
  - L153: private void showParentEntryData(OrgChgBillMobileBo orgChgBillMobileBo, List<DynamicObject> entryData) {
  - L202: private void showInfoEntryData(OrgChgBillMobileBo orgChgBillMobileBo, List<DynamicObject> entryData) {
  - L245: private void showDisabledEntryData(OrgChgBillMobileBo orgChgBillMobileBo, List<DynamicObject> entryData) {
  - L272: private void addCardEntryData(CardEntry cardEntry, int index, String dataKey, Map<String, Object> dataVal) {
  - L278: private void showInfoChgEntryData(CardEntry cardEntry, int index, DynamicObject dynamicObject, DynamicObject org) {
  - L447: private void showParentChgEntryData(CardEntry cardEntry, int index, DynamicObject dynamicObject, DynamicObject org) {
  - L645: public String getContentFlexCoin() {
  - L649: public void setContentFlexCoin(String contentFlexCoin) {
  - L653: public String getEntryCoin() {
  - L657: public void setEntryCoin(String entryCoin) {

### OrgBatchBillSubmitDealSourceInfoOp — OrgBatchBillSubmitDealSourceInfoOp.java

  - L27: public void endOperationTransaction(EndOperationTransactionArgs e) {

### OrgBatchBillInfoHelper — OrgBatchBillInfoHelper.java

  - L20: public static DynamicObject[] getHisDataById(Set<Long> orgSet) {
  - L27: public static DynamicObject[] getAllDataByIdSet(Set<Long> orgSet) {
  - L34: public static DynamicObject getOrgInfoById(Long id) {

## 平台 op 调用清单

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## 关联

- 真扫源：deep_scan_class_tree.md / deep_scan_audit.md
