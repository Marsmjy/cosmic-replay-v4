/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.GetEntityTypeEventArgs
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.entity.property.OrgProp
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Container
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.CellClickEvent
 *  kd.bos.form.control.events.CellClickListener
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.TextEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.model.complexobj.FieldComplexType
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scene;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.GetEntityTypeEventArgs;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.BasedataProp;
import kd.bos.entity.property.OrgProp;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.container.Container;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.CellClickEvent;
import kd.bos.form.control.events.CellClickListener;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.TextEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.model.complexobj.FieldComplexType;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper;

@ExcludeFromJacocoGeneratedReport
public final class WarningSceneReceiverPermEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener,
CellClickListener {
    private static final Log LOGGER = LogFactory.getLog(WarningSceneReceiverPermEdit.class);
    private static final String CLOSE_CALL_BACK_FUNCTION_ENTITY = "WARN_FROM_ENTITY_F7";
    private static final String ENTITY_NUMBER_HRCS_DIMENSION = "hrcs_dimension";
    private static final String WARN_RC_PERM_ENTRY_ENTITY_ROW_INDEX = "WARN_RC_PERM_ENTRY_ENTITY_ROW_INDEX";

    public void registerListener(EventObject e) {
        EntryGrid permEntryGrid;
        TextEdit fieldNameEdit;
        super.registerListener(e);
        BasedataEdit fromEntityEdit = (BasedataEdit)this.getView().getControl("warnfromentiy");
        if (fromEntityEdit != null) {
            fromEntityEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        }
        if ((fieldNameEdit = (TextEdit)this.getView().getControl("fieldname")) != null) {
            fieldNameEdit.addButtonClickListener((ClickListener)this);
        }
        if ((permEntryGrid = (EntryGrid)this.getView().getControl("warnrcpermentryentity")) != null) {
            permEntryGrid.addCellClickListener((CellClickListener)this);
        }
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        try {
            this.updatePermEntry();
            this.setPermRCFlexCollapse();
            this.getModel().setDataChanged(false);
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    private void updatePermEntry() {
        Object warnFromEntityObj = this.getModel().getValue("warnfromentiy");
        if (warnFromEntityObj != null) {
            DynamicObject warnFromEntity = (DynamicObject)warnFromEntityObj;
            this.setRcPermHeader(warnFromEntity.getString("name"));
            this.refreshWarnFormEntityEntry(warnFromEntity.getString("id"), null);
        }
    }

    private List<Long> getPermDimensionIdList(List<Map<String, Object>> result) {
        ArrayList<Long> permDimensionIdList = new ArrayList<Long>(10);
        if (result != null && result.size() > 0) {
            result.forEach(item -> permDimensionIdList.add(Long.parseLong(String.valueOf(item.get("id")))));
        }
        return permDimensionIdList;
    }

    private void refreshWarnFormEntityEntry(String entityNumber, String propKey) {
        int idx2;
        LOGGER.info("WarningSceneReceiverPermEdit.getCtrlDimensionByEntity entry,entityNumber:{},propKey:{}", (Object)entityNumber, (Object)propKey);
        List result = EntityCtrlServiceHelper.getCtrlDimensionByEntity((String)entityNumber, (String)propKey);
        List<Long> permDimensionIdList = this.getPermDimensionIdList(result);
        HashMap permDimensionMap = new HashMap(16);
        LOGGER.info("WarningSceneReceiverPermEdit.getCtrlDimensionByEntity result:{} ", (Object)result);
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        ArrayList<Integer> needDeleteRows = new ArrayList<Integer>(10);
        DynamicObjectCollection dynColl = this.getModel().getEntryEntity("warnrcpermentryentity");
        if (dynColl != null && dynColl.size() > 0) {
            for (idx2 = 0; idx2 < dynColl.size(); ++idx2) {
                if (dynColl.get(idx2) == null || ((DynamicObject)dynColl.get(idx2)).getDynamicObject("warnentityperm") == null || !permDimensionIdList.contains(((DynamicObject)dynColl.get(idx2)).getDynamicObject("warnentityperm").getLong("id"))) {
                    needDeleteRows.add(idx2);
                    continue;
                }
                permDimensionIdList.remove(permDimensionIdList.indexOf(((DynamicObject)dynColl.get(idx2)).getDynamicObject("warnentityperm").getLong("id")));
            }
        }
        needDeleteRows.forEach(idx -> this.getModel().deleteEntryRow("warnrcpermentryentity", idx.intValue()));
        dynColl = this.getModel().getEntryEntity("warnrcpermentryentity");
        for (idx2 = 0; idx2 < result.size(); ++idx2) {
            if (permDimensionIdList.size() > 0 && permDimensionIdList.contains(((Map)result.get(idx2)).get("id"))) {
                DynamicObject newRow = dynColl.addNew();
                MainEntityType entityType = EntityMetadataCache.getDataEntityType((String)ENTITY_NUMBER_HRCS_DIMENSION);
                DynamicObject dynamicObject = new DynamicObject((DynamicObjectType)entityType);
                dynamicObject.getDataEntityType().getPrimaryKey().setValueFast((Object)dynamicObject, ((Map)result.get(idx2)).get("id"));
                dynamicObject.set("id", ((Map)result.get(idx2)).get("id"));
                dynamicObject.set("number", ((Map)result.get(idx2)).get("number"));
                dynamicObject.set("name", ((Map)result.get(idx2)).get("name"));
                newRow.set("warnentityperm", (Object)dynamicObject);
            }
            permDimensionMap.put(((Map)result.get(idx2)).get("id"), result.get(idx2));
        }
        model.endInit();
        if (permDimensionMap.size() > 0) {
            this.getPageCache().put("warnentityperm", SerializationUtils.toJsonString(permDimensionMap));
        }
        this.getView().updateView("warnrcpermentryentity");
    }

    public void cellClick(CellClickEvent cellClickEvent) {
        EntryGrid entryGrid;
        super.click((EventObject)cellClickEvent);
        if (cellClickEvent.getSource() instanceof EntryGrid && "warnrcpermentryentity".equals((entryGrid = (EntryGrid)cellClickEvent.getSource()).getEntryKey()) && "fieldname".equals(cellClickEvent.getFieldKey())) {
            this.refreshPermDimensionCache();
            this.getPageCache().put(WARN_RC_PERM_ENTRY_ENTITY_ROW_INDEX, String.valueOf(cellClickEvent.getRow()));
            DynamicObjectCollection permEntryCol = this.getModel().getEntryEntity("warnrcpermentryentity");
            if (permEntryCol != null && permEntryCol.size() > cellClickEvent.getRow()) {
                DynamicObject permDyn = (DynamicObject)permEntryCol.get(cellClickEvent.getRow());
                this.setWarnScenePermField(permDyn);
            }
        }
    }

    public void cellDoubleClick(CellClickEvent cellClickEvent) {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void propertyChanged(PropertyChangedArgs args) {
        try {
            String changeKey = args.getProperty().getName();
            if ("permrc".equals(changeKey)) {
                ConfirmCallBackListener confirmListener = new ConfirmCallBackListener("permrc", (IFormPlugin)this);
                ChangeData changeData = args.getChangeSet()[0];
                if (!((Boolean)changeData.getNewValue()).booleanValue() && this.isInputReceiverPerm()) {
                    this.getView().showConfirm(ResManager.loadKDString((String)"\u5173\u95ed\u540e\uff0c\u4f1a\u6e05\u7a7a\u201c\u6d88\u606f\u63a5\u6536\u4eba\u63a7\u6743\u8bbe\u7f6e\u201d\uff0c\u786e\u5b9a\u5173\u95ed\u5417\uff1f", (String)"WarningSceneReceiverPermEdit_2", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, confirmListener);
                    return;
                } else {
                    this.setPermRCFlexCollapse();
                }
                return;
            }
            if (!"warnfromentiy".equals(changeKey)) return;
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    private boolean isInputReceiverPerm() {
        return this.getModel().getValue("warnfromentiy") != null;
    }

    private void setEntityFiled(Object returnData) {
        String idx = this.getPageCache().get(WARN_RC_PERM_ENTRY_ENTITY_ROW_INDEX);
        if (returnData instanceof Map && HRStringUtils.isNotEmpty((String)idx)) {
            int rowIndex = Integer.parseInt(idx);
            Map fieldInfoMap = (Map)returnData;
            this.getView().getModel().setValue("fieldname", fieldInfoMap.get("originalName"), rowIndex);
            this.getView().getModel().setValue("entitynumber", fieldInfoMap.get("entityNumber"), rowIndex);
            this.getView().getModel().setValue("fieldalias", fieldInfoMap.get("fieldAlias"), rowIndex);
            this.getView().getModel().setValue("fieldpath", fieldInfoMap.get("fieldPath"), rowIndex);
            this.getView().getModel().setValue("valuetype", fieldInfoMap.get("valueType"), rowIndex);
            this.getView().updateView("warnrcpermentryentity");
        }
    }

    private void setPermBizApp(Object returnData) {
        if (returnData instanceof ListSelectedRowCollection) {
            ListSelectedRowCollection selectedRows = (ListSelectedRowCollection)returnData;
            for (ListSelectedRow row : selectedRows) {
                Object keyValue = row.getPrimaryKeyValue();
                if (keyValue != null) {
                    String[] keys = String.valueOf(keyValue).split("__");
                    this.getView().getModel().setValue("warnfromentiy", (Object)keys[0]);
                    if (keys.length == 2) {
                        this.getView().getModel().setValue("warnbizappid", (Object)keys[1]);
                        this.setRcPermHeader(row.getName());
                    }
                    this.changeWarnFormEntityEntry(keys[0], null);
                    continue;
                }
                this.getView().getModel().setValue("warnfromentiy", null);
                this.getView().getModel().setValue("warnbizappid", null);
            }
        }
    }

    private void changeWarnFormEntityEntry(String entityNumber, String propKey) {
        LOGGER.info("WarningSceneReceiverPermEdit.getCtrlDimensionByEntity entry,entityNumber:{},propKey:{}", (Object)entityNumber, (Object)propKey);
        List result = EntityCtrlServiceHelper.getCtrlDimensionByEntity((String)entityNumber, (String)propKey);
        HashMap permDimensionMap = new HashMap(16);
        LOGGER.info("WarningSceneReceiverPermEdit.getCtrlDimensionByEntity result:{} ", (Object)result);
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        this.getModel().deleteEntryData("warnrcpermentryentity");
        if (result != null && result.size() > 0) {
            model.batchCreateNewEntryRow("warnrcpermentryentity", result.size());
            for (int idx = 0; idx < result.size(); ++idx) {
                model.setValue("warnentityperm", ((Map)result.get(idx)).get("id"), idx);
                permDimensionMap.put(((Map)result.get(idx)).get("id"), result.get(idx));
            }
        }
        model.endInit();
        this.getPageCache().put("warnentityperm", SerializationUtils.toJsonString(permDimensionMap));
        this.getView().updateView("warnrcpermentryentity");
    }

    private void refreshPermDimensionCache() {
        String cacheValue = this.getPageCache().get("warnentityperm");
        if (HRStringUtils.isEmpty((String)cacheValue)) {
            this.updatePermEntry();
        }
    }

    private void setRcPermHeader(String permEntityName) {
        LocaleString columnTitle = ((BasedataEdit)this.getControl("warnentityperm")).getDisplayName();
        columnTitle.setLocaleValue(permEntityName + columnTitle.getLocaleValue());
        EntryGrid grid = (EntryGrid)this.getControl("warnrcpermentryentity");
        grid.setColumnProperty("warnentityperm", "header", (Object)columnTitle);
    }

    public void click(EventObject evt) {
        super.click(evt);
        if (evt.getSource() instanceof TextEdit && evt.getSource() != null && "fieldname".equals(((TextEdit)evt.getSource()).getKey())) {
            this.openRcFieldF7();
        }
    }

    private void openRcFieldF7() {
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"hrcs_warnobjectfieldf7", (boolean)false, (int)0, (boolean)true);
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setMultiSelect(false);
        fsp.setShowTitle(false);
        fsp.setHasRight(true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "fieldname"));
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void setWarnScenePermField(DynamicObject permDimensionDyn) {
        Map permDimensionMapList;
        if (permDimensionDyn == null) {
            return;
        }
        String value = this.getView().getPageCache().get("warnentityperm");
        if (HRStringUtils.isNotEmpty((String)value) && (permDimensionMapList = (Map)SerializationUtils.fromJsonString((String)value, Map.class)) != null && permDimensionDyn.getDynamicObject("warnentityperm") != null && permDimensionMapList.containsKey(String.valueOf(permDimensionDyn.getDynamicObject("warnentityperm").get("id")))) {
            Map permDimensionMap = (Map)permDimensionMapList.get(String.valueOf(permDimensionDyn.getDynamicObject("warnentityperm").get("id")));
            value = this.getView().getPageCache().get("allFieldTreeNodes");
            if (HRStringUtils.isNotEmpty((String)value)) {
                List fieldList = (List)SerializationUtils.fromJsonString((String)value, List.class);
                ArrayList<Map> f7FieldList = new ArrayList<Map>(10);
                boolean addFlag = false;
                for (Map fieldMap : fieldList) {
                    addFlag = false;
                    if ("orgteam".equals(permDimensionMap.get("datasource"))) {
                        if (String.valueOf(fieldMap.get("key")).endsWith("boid") || String.valueOf(fieldMap.get("key")).endsWith(".id")) {
                            addFlag = true;
                        } else if (AnalyseObjectUtil.isBaseDataType((String)String.valueOf(fieldMap.get("complexType"))) && ("haos_adminorghrf7".equals(String.valueOf(fieldMap.get("baseDataNum"))) || "haos_projectteamhr".equals(String.valueOf(fieldMap.get("baseDataNum"))) || "haos_adminorgteam".equals(String.valueOf(fieldMap.get("baseDataNum"))))) {
                            addFlag = true;
                        } else if (FieldComplexType.ADMIN_ORG.getValue().equals(String.valueOf(fieldMap.get("complexType"))) && "01".equals(this.getOrgFuncs(String.valueOf(fieldMap.get("entityNumber")), String.valueOf(fieldMap.get("number"))))) {
                            addFlag = true;
                        }
                    } else if ("hrbu".equals(permDimensionMap.get("datasource"))) {
                        if (FieldComplexType.ADMIN_ORG.getValue().equals(String.valueOf(fieldMap.get("complexType")))) {
                            String orgFuncs = this.getOrgFuncs(String.valueOf(fieldMap.get("entityNumber")), String.valueOf(fieldMap.get("number")));
                            if (!"01".equals(orgFuncs) && orgFuncs != null && orgFuncs.equals(String.valueOf(permDimensionMap.get("bucafuncid")))) {
                                addFlag = true;
                            }
                        } else if (String.valueOf(fieldMap.get("key")).endsWith("boid") || String.valueOf(fieldMap.get("key")).endsWith(".id")) {
                            addFlag = true;
                        } else if (!AnalyseObjectUtil.isBaseDataType((String)String.valueOf(fieldMap.get("complexType")))) {
                            addFlag = true;
                        }
                    } else if ("basedata".equals(permDimensionMap.get("datasource"))) {
                        if ((FieldComplexType.BASE_DATA.getValue().equals(String.valueOf(fieldMap.get("complexType"))) || FieldComplexType.HIS_BASE_DATA.getValue().equals(String.valueOf(fieldMap.get("complexType")))) && String.valueOf(fieldMap.get("baseDataNum")).equals(permDimensionMap.get("entitytype"))) {
                            addFlag = true;
                        } else if (String.valueOf(fieldMap.get("key")).endsWith("boid") || String.valueOf(fieldMap.get("key")).endsWith(".id")) {
                            addFlag = true;
                        } else if (!AnalyseObjectUtil.isBaseDataType((String)String.valueOf(fieldMap.get("complexType")))) {
                            addFlag = true;
                        }
                    } else if ("enum".equals(permDimensionMap.get("datasource"))) {
                        if (String.valueOf(fieldMap.get("key")).endsWith("boid") || String.valueOf(fieldMap.get("key")).endsWith(".id")) {
                            addFlag = true;
                        } else if (!AnalyseObjectUtil.isBaseDataType((String)String.valueOf(fieldMap.get("complexType")))) {
                            addFlag = true;
                        }
                    }
                    if (!addFlag) continue;
                    f7FieldList.add(fieldMap);
                }
                this.getView().getPageCache().put("fieldname", SerializationUtils.toJsonString(f7FieldList));
            }
        }
    }

    private String getOrgFuncs(String entityNumber, String key) {
        String[] items = key.split("\\.");
        DynamicProperty dynamicProperty = EntityMetadataCache.getDataEntityType((String)entityNumber).getProperty(items[0]);
        if (dynamicProperty instanceof OrgProp) {
            return ((OrgProp)dynamicProperty).getOrgFunc();
        }
        return null;
    }

    private void setPermRCFlexCollapse() {
        BasedataEdit fromEntityEdit;
        Boolean permRc = (Boolean)this.getModel().getValue("permrc");
        Container permRcFlex = (Container)this.getView().getControl("flexpanelreceiverperm");
        if (permRcFlex != null && permRc != null) {
            permRcFlex.setCollapse(permRc == false);
        }
        if (permRc != null && !permRc.booleanValue()) {
            this.getModel().setValue("warnfromentiy", null);
            this.getModel().setValue("warnbizappid", null);
            this.getModel().deleteEntryData("warnrcpermentryentity");
        }
        if ((fromEntityEdit = (BasedataEdit)this.getView().getControl("warnfromentiy")) != null && permRc != null) {
            fromEntityEdit.setMustInput(permRc.booleanValue());
            BasedataProp fromEntityProperty = (BasedataProp)fromEntityEdit.getProperty();
            if (fromEntityProperty != null) {
                fromEntityProperty.setMustInput(permRc.booleanValue());
            }
        }
    }

    public void getEntityType(GetEntityTypeEventArgs e) {
        super.getEntityType(e);
        MainEntityType originalEntityType = e.getOriginalEntityType();
        try {
            e.setNewEntityType((MainEntityType)originalEntityType.clone());
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        try {
            String fieldKey = beforeF7SelectEvent.getProperty().getName();
            FormShowParameter f7SelectEventFormShowParameter = beforeF7SelectEvent.getFormShowParameter();
            if ("warnfromentiy".equals(fieldKey)) {
                f7SelectEventFormShowParameter.setCustomParam("IS_F7_RETURN_ID_INCLUDE_APP_ID_KEY", (Object)"true");
                f7SelectEventFormShowParameter.setCustomParam("LIST_CUSTOM_TITLE_KEY", (Object)ResManager.loadKDString((String)"\u5904\u7406\u9884\u8b66\u4e1a\u52a1\u529f\u80fd", (String)"WarningSceneReceiverPermEdit_0", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                f7SelectEventFormShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CLOSE_CALL_BACK_FUNCTION_ENTITY));
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarningSceneReceiverPermEdit_beforeF7Select_error_", (Throwable)exception);
        }
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        Object returnData = event.getReturnData();
        if (returnData == null) {
            return;
        }
        if (CLOSE_CALL_BACK_FUNCTION_ENTITY.equals(event.getActionId())) {
            this.setPermBizApp(returnData);
        } else if ("fieldname".equals(event.getActionId())) {
            this.setEntityFiled(returnData);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        super.confirmCallBack(messageBoxClosedEvent);
        try {
            String actionId = messageBoxClosedEvent.getCallBackId();
            if ("permrc".contains(actionId)) {
                if (messageBoxClosedEvent.getResult() == MessageBoxResult.Yes) {
                    this.setPermRCFlexCollapse();
                } else {
                    this.getModel().setValue("permrc", (Object)Boolean.TRUE);
                    this.getView().updateView("permrc");
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }
}
