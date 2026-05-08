/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.BeforeDeleteRowEventArgs
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.entity.property.EntryProp
 *  kd.bos.entity.property.MulBasedataProp
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrptc.business.servicehelper.RptDimMapServiceHelper
 *  kd.hr.hrptc.common.constant.permission.EntityConstants
 *  kd.hr.hrptmc.business.repdesign.ReportManageService
 *  kd.hr.hrptmc.business.repdesign.ReportPermissionService
 *  org.apache.commons.collections.CollectionUtils
 */
package kd.hr.hrptc.formplugin.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.BeforeDeleteRowEventArgs;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.BasedataProp;
import kd.bos.entity.property.EntryProp;
import kd.bos.entity.property.MulBasedataProp;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrptc.business.servicehelper.RptDimMapServiceHelper;
import kd.hr.hrptc.common.constant.permission.EntityConstants;
import kd.hr.hrptmc.business.repdesign.ReportManageService;
import kd.hr.hrptmc.business.repdesign.ReportPermissionService;

public final class RptDimMapEdit
extends HRDataBaseEdit
implements EntityConstants,
BeforeF7SelectListener {
    private static final Log LOGGER = LogFactory.getLog(RptDimMapEdit.class);
    private static final String OP_ADD_DIMENSION = "adddimension";
    private static final String OP_DELETE_ENTRY = "deleteentry";
    private static final String CALLBACK_DIMENSION_F7 = "dimensionF7callback";
    private static final String CALLBACK_DEL_DIMENSION = "deldimensioncallback";
    private static final String DELETE_ENTER = "delEnter";

    public void registerListener(EventObject e) {
        super.registerListener(e);
        BasedataEdit basedataEdit = (BasedataEdit)this.getControl("rptmanage");
        basedataEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
        e.getFormShowParameter().getOpenStyle().setShowType(ShowType.Modal);
    }

    public void afterLoadData(EventObject e) {
        super.afterLoadData(e);
        this.setNameAndNumber();
    }

    private long getRptManageId() {
        return this.getModel().getDataEntity().getDynamicObject("rptmanage").getLong("id");
    }

    private Map<Long, Map<String, String>> getPermissionFieldMap() {
        long rptManageId = this.getRptManageId();
        LOGGER.info("rptManageId:{}", (Object)rptManageId);
        Map permissionFieldMap = ReportManageService.getPermFieldIdMap((long)rptManageId);
        for (Map fieldMap : permissionFieldMap.values()) {
            HashMap<String, String> paramMap = new HashMap<String, String>();
            String number = (String)fieldMap.get("number");
            String entityNumber = (String)fieldMap.get("entityNumber");
            if (number.contains(".")) {
                String[] fields = number.split("\\.");
                this.handlerParamMap(paramMap, fields, entityNumber);
            } else {
                paramMap.put("parententity", entityNumber);
                paramMap.put("propkey", number);
            }
            fieldMap.putAll(paramMap);
        }
        LOGGER.info("permissionFieldMap:{}", (Object)permissionFieldMap);
        return permissionFieldMap;
    }

    private void handlerParamMap(Map<String, String> paramMap, String[] fields, String entityNumber) {
        if (fields[0].equals(entityNumber)) {
            if (fields.length == 2) {
                paramMap.put("parententity", entityNumber);
                paramMap.put("propkey", fields[1]);
            } else {
                this.handlerParamMap(paramMap, entityNumber, fields, 1);
            }
        } else {
            this.handlerParamMap(paramMap, entityNumber, fields, 0);
        }
    }

    private void handlerParamMap(Map<String, String> paramMap, String entityNumber, String[] fields, int i) {
        MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        IDataEntityProperty property = mainEntityType.findProperty(fields[i]);
        while (property instanceof EntryProp) {
            property = mainEntityType.findProperty(fields[++i]);
        }
        if (i < fields.length - 1) {
            if (property instanceof BasedataProp) {
                ((BasedataProp)property).getBaseEntityId();
                this.handlerParamMap(paramMap, ((BasedataProp)property).getBaseEntityId(), fields, i + 1);
            } else if (property instanceof MulBasedataProp) {
                this.handlerParamMap(paramMap, ((MulBasedataProp)property).getBaseEntityId(), fields, i + 1);
            }
        } else {
            paramMap.put("parententity", entityNumber);
            paramMap.put("propkey", fields[i]);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        if (OP_ADD_DIMENSION.equals(operateKey)) {
            if (null == this.getModel().getValue("rptmanage")) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u9700\u8981\u63a7\u6743\u7684\u62a5\u8868\u3002", (String)"RptDimMapEdit_1", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            Map<Long, Map<String, String>> permissionFieldMap = this.getPermissionFieldMap();
            if (CollectionUtils.isEmpty(permissionFieldMap)) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u62a5\u8868\u6ca1\u6709\u53ef\u63a7\u6743\u7684\u7ef4\u5ea6\u3002", (String)"RptDimMapEdit_4", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            this.openDimensionF7(permissionFieldMap);
        } else if ("save".equals(operateKey)) {
            if (null == this.getModel().getValue("rptmanage")) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u9700\u8981\u63a7\u6743\u7684\u62a5\u8868\u3002", (String)"RptDimMapEdit_1", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            if (0 == this.getModel().getEntryEntity("entryentity").size()) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u6dfb\u52a0\u63a7\u6743\u7ef4\u5ea6\u3002", (String)"RptDimMapEdit_5", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            Long id = (Long)this.getModel().getValue("id");
            if (RptDimMapServiceHelper.isRptExists((Long)id, (Long)this.getRptManageId())) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u62a5\u8868\u5df2\u914d\u7f6e\u7ef4\u5ea6\u6620\u5c04\uff0c\u4e0d\u80fd\u91cd\u590d\u914d\u7f6e\u3002", (String)"RptDimMapEdit_2", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
        }
    }

    public void beforeDeleteRow(BeforeDeleteRowEventArgs rowEventArgs) {
        super.beforeDeleteRow(rowEventArgs);
        EntryProp entryProp = rowEventArgs.getEntryProp();
        if ("entryentity".equals(entryProp.getName())) {
            String delEnter = this.getPageCache().get(DELETE_ENTER);
            if (StringUtils.isEmpty((CharSequence)delEnter) || !Boolean.parseBoolean(delEnter)) {
                int[] rowIndices = rowEventArgs.getRowIndexs();
                ArrayList<Long> queryFieldIds = new ArrayList<Long>(rowIndices.length);
                for (int i : rowIndices) {
                    DynamicObject disDy = this.getModel().getEntryRowEntity("entryentity", i);
                    queryFieldIds.add(disDy.getDynamicObject("aoqfield").getLong("id"));
                }
                if (ReportPermissionService.checkPermission((long)this.getRptManageId(), queryFieldIds)) {
                    String tips = ResManager.loadKDString((String)"\u5f53\u524d\u9009\u62e9\u7684\u7ef4\u5ea6\u6620\u5c04\u5df2\u88ab\u5f15\u7528\uff0c\u5220\u9664\u540e\u62a5\u8868\u5c06\u4e0d\u518d\u8fdb\u884c\u6570\u636e\u6743\u9650\u63a7\u6743\uff0c\u786e\u5b9a\u5220\u9664\uff1f", (String)"RptDimMapEdit_3", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]);
                    this.getView().showConfirm(tips, MessageBoxOptions.OKCancel, new ConfirmCallBackListener(CALLBACK_DEL_DIMENSION, (IFormPlugin)this));
                    rowEventArgs.setCancel(true);
                }
            } else {
                this.getPageCache().put(DELETE_ENTER, "false");
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        String name = e.getProperty().getName();
        if ("rptmanage".equals(name)) {
            this.getModel().deleteEntryData("entryentity");
        }
    }

    private void openDimensionF7(Map<Long, Map<String, String>> permissionFieldMap) {
        ListShowParameter showParameter = ShowFormHelper.createShowListForm((String)"hrptc_selectdimfield", (boolean)true, (int)0, (boolean)true);
        showParameter.setCustomParam("permissionFieldMap", (Object)SerializationUtils.toJsonString(permissionFieldMap));
        showParameter.setCustomParam("rptManageId", (Object)this.getRptManageId());
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CALLBACK_DIMENSION_F7));
        List<Long> selectedIds = this.getSelectedDimIds();
        if (!CollectionUtils.isEmpty(selectedIds)) {
            showParameter.setSelectedRows(selectedIds.toArray());
        }
        this.getView().showForm((FormShowParameter)showParameter);
    }

    private List<Long> getSelectedDimIds() {
        DynamicObjectCollection displayRows = this.getModel().getEntryEntity("entryentity");
        ArrayList<Long> selectIds = new ArrayList<Long>(displayRows.size());
        for (DynamicObject row : displayRows) {
            if (row == null || null == row.getDynamicObject("aoqfield")) continue;
            selectIds.add(row.getDynamicObject("aoqfield").getLong("id"));
        }
        return selectIds;
    }

    public void closedCallBack(ClosedCallBackEvent callBackEvent) {
        super.closedCallBack(callBackEvent);
        String actionId = callBackEvent.getActionId();
        if (actionId.equals(CALLBACK_DIMENSION_F7)) {
            ListSelectedRowCollection selectedRows = (ListSelectedRowCollection)callBackEvent.getReturnData();
            this.setFieldEntry(selectedRows);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        String callBackId = evt.getCallBackId();
        MessageBoxResult result = evt.getResult();
        if (result == MessageBoxResult.Yes && CALLBACK_DEL_DIMENSION.equals(callBackId)) {
            this.getPageCache().put(DELETE_ENTER, "true");
            this.getView().invokeOperation(OP_DELETE_ENTRY);
        }
    }

    private void setFieldEntry(ListSelectedRowCollection selectedRows) {
        if (null == selectedRows) {
            return;
        }
        List<Long> oldSelectIds = this.getSelectedDimIds();
        List newSelectIds = selectedRows.stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList());
        List deleteList = (List)org.apache.commons.collections.CollectionUtils.subtract(oldSelectIds, newSelectIds);
        List addList = (List)org.apache.commons.collections.CollectionUtils.subtract(newSelectIds, oldSelectIds);
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        if (!CollectionUtils.isEmpty((Collection)selectedRows) && !CollectionUtils.isEmpty((Collection)addList)) {
            int idx = this.getModel().getEntryEntity("entryentity").size();
            model.batchCreateNewEntryRow("entryentity", addList.size());
            Map<Long, Map<String, String>> permissionFieldMap = this.getPermissionFieldMap();
            for (ListSelectedRow row2 : selectedRows) {
                Long id = (Long)row2.getPrimaryKeyValue();
                if (!addList.contains(id)) continue;
                model.setValue("aoqfield", (Object)id, idx);
                model.setValue("propkey", (Object)row2.getNumber(), idx);
                model.setValue("propname", (Object)row2.getName(), idx);
                Map<String, String> map = permissionFieldMap.get(id);
                if (null != map) {
                    model.setValue("parententity", (Object)map.get("parententity"), idx);
                    model.setValue("entity", (Object)map.get("baseDataNum"), idx);
                    model.setValue("number", (Object)map.get("propkey"), idx);
                }
                ++idx;
            }
        }
        if (!CollectionUtils.isEmpty((Collection)deleteList)) {
            DynamicObjectCollection displayRows = this.getModel().getEntryEntity("entryentity");
            for (int i = 0; i < displayRows.size(); ++i) {
                Long id = ((DynamicObject)displayRows.get(i)).getDynamicObject("aoqfield").getLong("id");
                if (!deleteList.contains(id)) continue;
                model.deleteEntryRow("entryentity", i);
            }
        }
        model.endInit();
        this.getView().updateView("entryentity");
    }

    private void setNameAndNumber() {
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        DynamicObjectCollection displayRows = this.getModel().getEntryEntity("entryentity");
        if (null != displayRows && displayRows.size() > 0) {
            model.beginInit();
            Map<Long, Map<String, String>> permissionFieldMap = this.getPermissionFieldMap();
            for (int i = 0; i < displayRows.size(); ++i) {
                Map<String, String> map;
                DynamicObject dy = ((DynamicObject)displayRows.get(i)).getDynamicObject("aoqfield");
                if (null == dy || null == (map = permissionFieldMap.get(dy.getLong("id")))) continue;
                model.setValue("propname", (Object)map.get("name"), i);
            }
            model.endInit();
            this.getView().updateView("entryentity");
            this.getModel().setDataChanged(false);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        beforeF7SelectEvent.getFormShowParameter().setFormId("hrptc_rptgrouptreelist");
    }
}
