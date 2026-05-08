/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.entity.datamodel.events.AfterMoveEntryEventArgs
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.RowClickEventListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.basedata.BaseDataServiceHelper
 *  kd.hr.hbp.common.cache.HRAppCache
 *  kd.hr.hbp.common.cache.IHRAppCache
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hrmp.lcs.business.basedata.CostBasaDataHelper
 *  kd.hrmp.lcs.business.cost.service.CostStruService
 */
package kd.hrmp.lcs.formplugin.web.basedata;

import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.entity.datamodel.events.AfterMoveEntryEventArgs;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.container.Tab;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.basedata.BaseDataServiceHelper;
import kd.hr.hbp.common.cache.HRAppCache;
import kd.hr.hbp.common.cache.IHRAppCache;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hrmp.lcs.business.basedata.CostBasaDataHelper;
import kd.hrmp.lcs.business.cost.service.CostStruService;

public class CostStruEdit
extends HRDataBaseEdit
implements RowClickEventListener,
BeforeF7SelectListener {
    private static final String KEY_DIMENSIONID = "dimensionid";
    private static final String KEY_DELETEENTRY = "deleteentry";
    private static final int DIMENSION_MAX = 30;
    private static final String KEY_STORAGESET = "storageset";
    private static final String KEY_COSTDIMENSION = "costdimension";
    private static final String DONOTHING_NEXTPAGE = "donothing_nextpage";
    private static final String DONOTHING_LASTPAGE = "donothing_lastpage";
    private static final String CURPAGE = "curpage";
    private static final Map<Integer, String> PAGE_MAP = new HashMap<Integer, String>(3);

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        this.showFlexByStatus();
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        EntryGrid dimensionEntry = (EntryGrid)this.getControl("dimensionentry");
        dimensionEntry.addRowClickListener((RowClickEventListener)this);
        BasedataEdit storageSetEdit = (BasedataEdit)this.getView().getControl(KEY_STORAGESET);
        storageSetEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        this.addClickListeners(new String[]{"adaptationruleent"});
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        switch (operateKey = operate.getOperateKey()) {
            case "save": {
                this.validateAndPackageData(args);
                this.setFlexVisibleWithSave();
                break;
            }
            case "submit": {
                this.validateAndPackageData(args);
                break;
            }
            case "deleteentry": {
                String currentSelectedDimensionId = this.getCurrentSelectedDimensionId();
                operate.getOption().setVariableValue(KEY_DIMENSIONID, currentSelectedDimensionId);
                break;
            }
            case "donothing_nextpage": {
                this.toNextPage(args);
                break;
            }
            case "donothing_lastpage": {
                this.toLastPage();
                break;
            }
        }
    }

    public void afterMoveEntryUp(AfterMoveEntryEventArgs e) {
        if (HRStringUtils.equals((String)"dimensionentry", (String)e.getEntryProp().getName())) {
            this.updateStorageset();
        }
    }

    public void afterMoveEntryDown(AfterMoveEntryEventArgs e) {
        if (HRStringUtils.equals((String)"dimensionentry", (String)e.getEntryProp().getName())) {
            this.updateStorageset();
        }
    }

    private void updateStorageset() {
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("dimensionentry");
        if (CollectionUtils.isEmpty((Collection)entryEntity)) {
            return;
        }
        DynamicObject[] segmentArr = CostBasaDataHelper.querySegement();
        for (int i = 0; i < entryEntity.size(); ++i) {
            DynamicObject entryObj = (DynamicObject)entryEntity.get(i);
            entryObj.set(KEY_STORAGESET, (Object)segmentArr[i]);
        }
        int entryIndex = this.getModel().getEntryCurrentRowIndex("dimensionentry");
        this.getModel().setValue(KEY_STORAGESET, (Object)segmentArr[entryIndex], entryIndex);
        this.getView().updateView("dimensionentry", entryIndex);
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        String operateKey;
        super.afterDoOperation(args);
        switch (operateKey = args.getOperateKey()) {
            case "donothing_newentry": {
                this.openCostDimensionListF7();
                break;
            }
            case "deleteentry": {
                this.delCacheDate(args);
                this.updateStorageset();
                this.loadCostStruSecondObjFlex();
                break;
            }
            case "moveentryup": 
            case "moveentrydown": {
                this.loadCostStruSecondObjFlex();
                break;
            }
            case "audit": 
            case "submit": 
            case "unaudit": 
            case "unsubmit": {
                this.isNeedRefreshCostBizObjView(args);
                break;
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent e) {
        String propertyName = e.getProperty().getName();
        if (HRStringUtils.equals((String)KEY_STORAGESET, (String)propertyName)) {
            List<Long> segmentIdList = this.getSelectedSegmentIds();
            ListShowParameter param = (ListShowParameter)e.getFormShowParameter();
            QFilter idFilter = new QFilter("id", "not in", segmentIdList);
            param.getListFilterParameter().setFilter(idFilter);
            param.getListFilterParameter().setOrderBy("index");
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        String actionId;
        super.closedCallBack(evt);
        switch (actionId = evt.getActionId()) {
            case "costdimension": {
                this.addCostDimension(evt);
                this.loadCostStruSecondObjFlex();
                break;
            }
        }
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        String status = (String)this.getModel().getValue("status");
        if ("B".equals(status) || "C".equals(status)) {
            this.getModel().setDataChanged(false);
        }
        IHRAppCache appCache = HRAppCache.get((String)this.getView().getPageId());
        appCache.remove("dimensionCache");
    }

    private void showFlexByStatus() {
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (OperationStatus.ADDNEW.equals((Object)status)) {
            this.initFirstPageVisible();
        } else {
            this.setFlexVisible();
            this.loadCostStruSecondObjFlex();
            this.initCostStruInfo();
        }
    }

    private void setFlexVisibleWithSave() {
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (OperationStatus.ADDNEW.equals((Object)status)) {
            this.setTabPage(0);
            IHRAppCache appCache = HRAppCache.get((String)this.getView().getPageId());
            String costBizPageId = (String)appCache.get("costbizpageid", String.class);
            if (costBizPageId == null) {
                return;
            }
            IFormView childView = this.getView().getView(costBizPageId);
            if (childView == null) {
                return;
            }
            childView.invokeOperation("donothing_refresh");
            this.getView().sendFormAction(childView);
            this.getView().setVisible(Boolean.TRUE, new String[]{"tbmain", "advcontoolbarap"});
            this.getView().setEnable(Boolean.TRUE, new String[]{"fs_baseinfo", "dimensionsetting"});
        }
    }

    private void setFlexVisible() {
        Long costStruId = (Long)this.getModel().getDataEntity().getPkValue();
        String status = this.getView().getModel().getDataEntity().getString("status");
        if (CostBasaDataHelper.isExistCostCfg((Long)costStruId) || CostBasaDataHelper.isExistCostSetUp((Long)costStruId)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"delecostdimension", "upcostdimension", "downcostdimension"});
            this.getView().setVisible(Boolean.TRUE, new String[]{"addcostdimension"});
        } else if ("C".equals(status) || "B".equals(status)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"advcontoolbarap"});
            this.getView().setEnable(Boolean.FALSE, new String[]{"dimensionsetting"});
        } else {
            this.getView().setVisible(Boolean.TRUE, new String[]{"advcontoolbarap"});
            this.getView().setEnable(Boolean.TRUE, new String[]{"dimensionsetting"});
        }
        this.getView().setVisible(Boolean.FALSE, new String[]{"wizardap", "tbmainop1"});
    }

    private void initCostStruInfo() {
        JSONObject dimensionRefBizObjCacheJSONObject = this.getCacheData();
        CostStruService costStruService = new CostStruService();
        costStruService.initDBDataToCache(dimensionRefBizObjCacheJSONObject, this.getView());
    }

    private JSONObject getCacheData() {
        IHRAppCache appCache = HRAppCache.get((String)this.getView().getPageId());
        JSONObject dimensionRefBizObjCacheJSONObject = (JSONObject)appCache.get("dimensionCache", JSONObject.class);
        if (dimensionRefBizObjCacheJSONObject == null) {
            dimensionRefBizObjCacheJSONObject = new JSONObject();
        }
        return dimensionRefBizObjCacheJSONObject;
    }

    private void validateAndPackageData(BeforeDoOperationEventArgs args) {
        Map dimensionObjMap = CostBasaDataHelper.getDimensionObjMap();
        DynamicObjectCollection dimensionEntry = this.getView().getModel().getEntryEntity("dimensionentry");
        for (int i = 0; i < dimensionEntry.size(); ++i) {
            long dimensionId = ((DynamicObject)dimensionEntry.get(i)).getLong("costdimension.id");
            DynamicObject dynamicObject = (DynamicObject)dimensionObjMap.get(dimensionId);
            if (dynamicObject != null) continue;
            args.setCancel(true);
            String errorMsg = MessageFormat.format(ResManager.loadKDString((String)"\u7b2c%s\u884c\uff0c\u4eba\u529b\u6210\u672c\u7ef4\u5ea6\u5df2\u5220\u9664\u3002", (String)"CostStruEdit_3", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]), i + 1);
            this.getView().showErrorNotification(errorMsg);
            return;
        }
        CostStruService costStruService = new CostStruService();
        boolean validateResult = costStruService.validateDimension(this.getView());
        if (!validateResult) {
            args.setCancel(true);
            return;
        }
        boolean validateCostbiz = costStruService.validateCostbiz(this.getView());
        if (!validateCostbiz) {
            args.setCancel(true);
            return;
        }
        costStruService.packageEntryData(this.getView());
        this.setAllDimensionName(dimensionEntry);
    }

    private void setAllDimensionName(DynamicObjectCollection dimensionEntry) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimensionEntry.size(); ++i) {
            String displayName = ((DynamicObject)dimensionEntry.get(i)).getString("dimensiondisplayname");
            sb.append(displayName).append('.');
        }
        if (sb.length() > 0) {
            this.getModel().setValue("alldimensionname", (Object)sb.substring(0, sb.length() - 1));
        }
    }

    private void toNextPage(BeforeDoOperationEventArgs args) {
        DynamicObjectCollection dimensionEntry = this.getModel().getEntryEntity("dimensionentry");
        if (dimensionEntry.isEmpty()) {
            args.setCancel(true);
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5728\u201c\u6210\u672c\u5bf9\u8c61\u201d\u4e0a\u7ef4\u62a4\u542f\u7528\u7684\u7ef4\u5ea6\u3002", (String)"CostStruEdit_2", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]));
            return;
        }
        int curPage = this.getModel().getDataEntity().getInt(CURPAGE);
        if (curPage == 2) {
            CostStruService costStruService = new CostStruService();
            boolean validateDimension = costStruService.validateDimension(this.getView());
            if (!validateDimension) {
                args.setCancel(true);
                return;
            }
            boolean validateCostbiz = costStruService.validateCostbiz(this.getView());
            if (!validateCostbiz) {
                args.setCancel(true);
                return;
            }
        }
        this.toNextPage();
    }

    private void toNextPage() {
        int curPage = this.getModel().getDataEntity().getInt(CURPAGE);
        this.toPage(curPage + 1);
    }

    private void toLastPage() {
        int curPage = this.getModel().getDataEntity().getInt(CURPAGE);
        this.toPage(curPage - 1);
    }

    private void toPage(int page) {
        int minPage = 1;
        int maxPage = PAGE_MAP.size();
        if (page <= maxPage && page >= minPage) {
            this.setTabAndFormShow(page);
        }
    }

    private void setTabAndFormShow(int toPage) {
        switch (toPage) {
            case 1: {
                this.initFirstPageVisible();
                break;
            }
            case 2: {
                this.initSecondPageVisible();
                break;
            }
            case 3: {
                this.initThirdPageVisible();
                break;
            }
        }
        this.setTabPage(toPage);
        this.loadCostStruSecondObjFlex();
    }

    private void loadCostStruSecondObjFlex() {
        FormShowParameter formShowParameter = new FormShowParameter();
        formShowParameter.getOpenStyle().setShowType(ShowType.InContainer);
        formShowParameter.getOpenStyle().setTargetKey("costbizobjflexap");
        formShowParameter.setFormId("lcs_coststrusecobjpage");
        this.getView().showForm(formShowParameter);
    }

    private void setTabPage(int toPage) {
        String tabKey = PAGE_MAP.get(toPage);
        Tab tab = (Tab)this.getControl("tabap");
        tab.activeTab(tabKey);
        this.getModel().setValue(CURPAGE, (Object)new BigDecimal(String.valueOf(toPage)));
    }

    private void initFirstPageVisible() {
        this.getView().setVisible(Boolean.FALSE, new String[]{"tbmain", "lastpage", "btn_save", "btn_submit", "advcostbizobj", "flexpanelap1"});
        this.getView().setVisible(Boolean.TRUE, new String[]{"dimensionsetting", "fs_baseinfo"});
        this.getView().setEnable(Boolean.TRUE, new String[]{"fs_baseinfo", "dimensionsetting"});
    }

    private void initSecondPageVisible() {
        this.getView().setVisible(Boolean.FALSE, new String[]{"tbmain", "btn_save", "btn_submit", "fs_baseinfo", "dimensionsetting"});
        this.getView().setVisible(Boolean.TRUE, new String[]{"advcostbizobj", "lastpage", "nextpage", "flexpanelap1", "advcontoolbarap"});
    }

    private void initThirdPageVisible() {
        this.getView().setVisible(Boolean.FALSE, new String[]{"tbmain", "nextpage", "flexpanelap1", "advcontoolbarap"});
        this.getView().setVisible(Boolean.TRUE, new String[]{"dimensionsetting", "lastpage", "btn_save", "btn_submit", "advcostbizobj", "fs_baseinfo"});
        this.getView().setEnable(Boolean.FALSE, new String[]{"fs_baseinfo", "dimensionsetting"});
    }

    private void isNeedRefreshCostBizObjView(AfterDoOperationEventArgs args) {
        boolean isSuccess = args.getOperationResult().isSuccess();
        if (isSuccess) {
            this.setFlexVisible();
            IHRAppCache appCache = HRAppCache.get((String)this.getView().getPageId());
            String costBizPageId = (String)appCache.get("costbizpageid", String.class);
            if (costBizPageId == null) {
                return;
            }
            IFormView childView = this.getView().getView(costBizPageId);
            if (childView == null) {
                return;
            }
            childView.invokeOperation("donothing_refresh");
            this.getView().sendFormAction(childView);
        }
    }

    private void delCacheDate(AfterDoOperationEventArgs args) {
        FormOperate operate = (FormOperate)args.getSource();
        String dimensionId = operate.getOption().getVariableValue(KEY_DIMENSIONID);
        JSONObject dimensionRefBizObjCacheJSONObject = this.getCacheData();
        if (HRStringUtils.isEmpty((String)dimensionId)) {
            return;
        }
        dimensionRefBizObjCacheJSONObject.remove((Object)dimensionId);
        IHRAppCache appCache = HRAppCache.get((String)this.getView().getPageId());
        appCache.put("dimensionCache", (Object)dimensionRefBizObjCacheJSONObject);
    }

    private List<Long> getSelectedDimensionIds() {
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("dimensionentry");
        ArrayList<Long> dimensionIdList = new ArrayList<Long>(10);
        entryEntity.forEach(dimensionObj -> dimensionIdList.add(dimensionObj.getLong("costdimension.id")));
        return dimensionIdList;
    }

    private List<Long> getSelectedSegmentIds() {
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("dimensionentry");
        ArrayList<Long> segmentIdList = new ArrayList<Long>(10);
        entryEntity.forEach(dimensionObj -> segmentIdList.add(dimensionObj.getLong("storageset.id")));
        return segmentIdList;
    }

    private void openCostDimensionListF7() {
        ListShowParameter showParam = ShowFormHelper.createShowListForm((String)"lcs_costdimension", (boolean)true);
        List<Long> selectedDimensionIds = this.getSelectedDimensionIds();
        QFilter qFilter = new QFilter("id", "not in", selectedDimensionIds);
        qFilter.and("enable", "=", (Object)"1");
        qFilter.and("status", "=", (Object)"C");
        long createOrgId = this.getView().getModel().getDataEntity().getLong("createorg.id");
        QFilter baseDataFilter = CostStruEdit.getBaseDataFilter("lcs_costdimension", createOrgId);
        if (baseDataFilter != null) {
            qFilter.and(baseDataFilter);
        }
        showParam.getListFilterParameter().setFilter(qFilter);
        showParam.setMultiSelect(true);
        showParam.setCloseCallBack(new CloseCallBack((IFormPlugin)this, KEY_COSTDIMENSION));
        this.getView().showForm((FormShowParameter)showParam);
    }

    public static QFilter getBaseDataFilter(String entityID, Long orgID) {
        return BaseDataServiceHelper.getBaseDataFilter((String)entityID, (Long)orgID);
    }

    private void addCostDimension(ClosedCallBackEvent closedCallBackEvent) {
        ListSelectedRowCollection selectedCollection = (ListSelectedRowCollection)closedCallBackEvent.getReturnData();
        if (selectedCollection != null && !selectedCollection.isEmpty()) {
            int dimensionSize = this.getEntrySizeByName("dimensionentry");
            int totalSize = dimensionSize + selectedCollection.size();
            if (totalSize > 30) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u7ef4\u5ea6\u4e0a\u9650\u4e0d\u5f97\u8d85\u8fc730\u4e2a\u3002", (String)"CostStruEdit_1", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]));
                return;
            }
            Object[] primaryKeyValues = selectedCollection.getPrimaryKeyValues();
            DynamicObject[] dimensionObjArr = CostBasaDataHelper.getDimensionObjArrByPkIdList((Object[])primaryKeyValues);
            this.setDimensionEntryValue(dimensionObjArr);
        }
    }

    private void setDimensionEntryValue(DynamicObject[] dimensionObjArr) {
        TableValueSetter setter = this.addTableValueField();
        List<Long> segmentIdList = this.getSelectedSegmentIds();
        DynamicObject[] segmentArr = CostBasaDataHelper.querySegementArr(segmentIdList);
        int index = 0;
        ArrayList<String> dimensionIds = new ArrayList<String>(10);
        for (DynamicObject dimensionObj : dimensionObjArr) {
            setter.addRow(new Object[]{dimensionObj.getString("id"), dimensionObj.getString("name"), segmentArr[index++].getLong("id")});
            dimensionIds.add(dimensionObj.getString("id"));
        }
        this.initNewEntryRow(setter);
        CostStruService costStruService = new CostStruService();
        JSONObject dimensionRefBizObjCacheJSONObject = this.getCacheData();
        costStruService.initNewDataToCache(dimensionRefBizObjCacheJSONObject, this.getView(), dimensionIds);
    }

    private void initNewEntryRow(TableValueSetter setter) {
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        model.batchCreateNewEntryRow("dimensionentry", setter);
        model.endInit();
        this.getView().updateView("dimensionentry");
    }

    private TableValueSetter addTableValueField() {
        TableValueSetter setter = new TableValueSetter(new String[0]);
        setter.addField(KEY_COSTDIMENSION, new Object[0]);
        setter.addField("dimensiondisplayname", new Object[0]);
        setter.addField(KEY_STORAGESET, new Object[0]);
        return setter;
    }

    private String getCurrentSelectedDimensionId() {
        int[] entryGridSelectRows = this.getSelectedEntryIndexArrByEntryName("dimensionentry");
        DynamicObjectCollection dimension = this.getModel().getEntryEntity("dimensionentry");
        if (entryGridSelectRows.length == 0) {
            return "";
        }
        return ((DynamicObject)dimension.get(entryGridSelectRows[0])).getString("costdimension.id");
    }

    private int[] getSelectedEntryIndexArrByEntryName(String entryName) {
        EntryGrid entryGrid = (EntryGrid)this.getView().getControl(entryName);
        return entryGrid.getSelectRows();
    }

    private int getEntrySizeByName(String entryName) {
        DynamicObjectCollection dimensionEntry = this.getModel().getEntryEntity(entryName);
        return dimensionEntry == null ? 0 : dimensionEntry.size();
    }

    static {
        PAGE_MAP.put(1, "firstpage");
        PAGE_MAP.put(2, "secondpage");
        PAGE_MAP.put(3, "thirdpage");
    }
}
