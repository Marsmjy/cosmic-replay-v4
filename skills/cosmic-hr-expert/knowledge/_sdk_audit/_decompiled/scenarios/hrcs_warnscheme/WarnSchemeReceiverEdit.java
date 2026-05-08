/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.FieldEdit
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scheme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.FieldEdit;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class WarnSchemeReceiverEdit
extends HRDataBaseEdit {
    private static final Log LOGGER = LogFactory.getLog(WarnSchemeReceiverEdit.class);

    public void registerListener(EventObject e) {
        super.registerListener(e);
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        try {
            this.setRcFixUserOrg();
            this.setRcFixEntryColumnDisplay();
            this.checkUserHasPerm();
            this.checkRoleHasPerm();
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void beforeClosed(BeforeClosedEvent e) {
        String name;
        DynamicObject reDy;
        int idx;
        super.beforeClosed(e);
        DynamicObjectCollection dynamicObjectUserCollection = this.getModel().getEntryEntity("rcfixentryentity");
        DynamicObjectCollection dynamicObjectRoleCollection = this.getModel().getEntryEntity("rcroleentryentity");
        if (dynamicObjectUserCollection != null) {
            for (idx = 0; idx < dynamicObjectUserCollection.size(); ++idx) {
                reDy = (DynamicObject)dynamicObjectUserCollection.get(idx);
                for (IDataEntityProperty bizChangedProperty : reDy.getDataEntityState().getBizChangedProperties()) {
                    name = bizChangedProperty.getName();
                    if (!"rcfixsourcetype".equals(name)) continue;
                    reDy.getDataEntityState().setBizChanged(bizChangedProperty.getOrdinal(), false);
                }
            }
        }
        if (dynamicObjectRoleCollection != null) {
            for (idx = 0; idx < dynamicObjectRoleCollection.size(); ++idx) {
                reDy = (DynamicObject)dynamicObjectRoleCollection.get(idx);
                for (IDataEntityProperty bizChangedProperty : reDy.getDataEntityState().getBizChangedProperties()) {
                    name = bizChangedProperty.getName();
                    if (!"rcrolesourcetype".equals(name)) continue;
                    reDy.getDataEntityState().setBizChanged(bizChangedProperty.getOrdinal(), false);
                }
            }
        }
    }

    private List<Object> getSelectedUserIds() {
        ArrayList<Object> selectIds = new ArrayList<Object>(10);
        DynamicObjectCollection dynColl = this.getModel().getEntryEntity("rcfixentryentity");
        if (dynColl != null && dynColl.size() > 0) {
            dynColl.forEach(item -> selectIds.add(item.getDynamicObject("rcfixuser").get("id")));
        }
        return selectIds;
    }

    private void setRcFixUserOrg() {
        List<Object> seletedUserIds = this.getSelectedUserIds();
        if (seletedUserIds.size() > 0) {
            Map<Object, String> userOrgMap = this.getUserOrg(seletedUserIds);
            AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
            model.beginInit();
            DynamicObjectCollection rcFixCol = this.getModel().getEntryEntity("rcfixentryentity");
            if (!CollectionUtils.isEmpty((Collection)rcFixCol) && rcFixCol.size() > 0) {
                DynamicObjectCollection entryEntity = model.getEntryEntity("rcfixentryentity");
                for (int idx = 0; idx < rcFixCol.size(); ++idx) {
                    if (((DynamicObject)rcFixCol.get(idx)).getDynamicObject("rcfixuser") == null || !userOrgMap.containsKey(((DynamicObject)rcFixCol.get(idx)).getDynamicObject("rcfixuser").get("id"))) continue;
                    model.setValue("rcfixuserorg", (Object)userOrgMap.get(((DynamicObject)rcFixCol.get(idx)).getDynamicObject("rcfixuser").get("id")), idx);
                    DynamicObject reDy = (DynamicObject)entryEntity.get(idx);
                    for (IDataEntityProperty bizChangedProperty : reDy.getDataEntityState().getBizChangedProperties()) {
                        String name = bizChangedProperty.getName();
                        if (!"rcfixuserorg".equals(name)) continue;
                        reDy.getDataEntityState().setBizChanged(bizChangedProperty.getOrdinal(), false);
                    }
                }
            }
            model.endInit();
            this.getView().updateView("rcfixentryentity");
        }
    }

    private Map<Object, String> getUserOrg(ListSelectedRowCollection selectedRows) {
        HashMap<Object, String> resultMap = new HashMap<Object, String>(16);
        if (null == selectedRows) {
            return resultMap;
        }
        ArrayList<Object> userIds = new ArrayList<Object>(10);
        for (ListSelectedRow selectedRow : selectedRows) {
            userIds.add(selectedRow.getPrimaryKeyValue());
        }
        return this.getUserOrg(userIds);
    }

    private Map<Object, String> getUserOrg(List<Object> userIds) {
        DynamicObject[] userDyns;
        HashMap<Object, String> resultMap = new HashMap<Object, String>(16);
        HRBaseServiceHelper userHelper = new HRBaseServiceHelper("bos_user");
        for (DynamicObject userDyn : userDyns = userHelper.loadDynamicObjectArray(userIds.toArray())) {
            resultMap.put(userDyn.get("id"), this.getUserOrgNames(userDyn));
        }
        return resultMap;
    }

    private String getUserOrgNames(DynamicObject userDyn) {
        StringBuilder stringBuilder = new StringBuilder();
        if (userDyn.getDynamicObjectCollection("entryentity") != null && userDyn.getDynamicObjectCollection("entryentity").size() > 0) {
            for (int idx = 0; idx < userDyn.getDynamicObjectCollection("entryentity").size(); ++idx) {
                if (idx > 0 && !stringBuilder.toString().endsWith(",")) {
                    stringBuilder.append(",");
                }
                if (((DynamicObject)userDyn.getDynamicObjectCollection("entryentity").get(idx)).getDynamicObject("dpt") == null) continue;
                stringBuilder.append(((DynamicObject)userDyn.getDynamicObjectCollection("entryentity").get(idx)).getDynamicObject("dpt").getString("name"));
            }
        }
        return stringBuilder.toString();
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        Object returnData = event.getReturnData();
        if (returnData == null) {
            return;
        }
        if ("rcfixadd".equals(event.getActionId())) {
            this.setRcFixEntryData((ListSelectedRowCollection)returnData);
            this.checkUserHasPerm();
        } else if ("rcroleadd".equals(event.getActionId())) {
            this.setRcRoleEntryData((ListSelectedRowCollection)returnData);
            this.checkRoleHasPerm();
        }
    }

    private void checkRoleHasPerm() {
        DynamicObject sceneDy = (DynamicObject)this.getModel().getValue("warnscene");
        if (sceneDy == null) {
            return;
        }
        DynamicObject bizAppDyn = sceneDy.getDynamicObject("warnbizappid");
        if (bizAppDyn == null) {
            return;
        }
        DynamicObject warnFromEntity = sceneDy.getDynamicObject("warnfromentiy");
        if (warnFromEntity == null) {
            return;
        }
        DynamicObjectCollection dynamicObjectCollection = this.getModel().getEntryEntity("rcroleentryentity");
        if (dynamicObjectCollection == null) {
            return;
        }
        HashMap<String, String> tipMap = new HashMap<String, String>(16);
        String tips = String.format(ResManager.loadKDString((String)"\u6b64\u89d2\u8272\u6ca1\u6709\u201c%1s\u201d\u529f\u80fd\u6743\u9650\uff0c\u65e0\u6cd5\u6309\u7167\u6570\u636e\u6743\u9650\u63a7\u5236\u9884\u8b66\u6570\u636e\u8303\u56f4\uff0c\u5c06\u63a8\u9001\u5168\u91cf\u9884\u8b66\u6570\u636e\u3002", (String)"WarningSceneReceiverPermEdit_4", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), warnFromEntity.getString("name"));
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        DynamicObjectCollection entryEntity = model.getEntryEntity("rcroleentryentity");
        for (int idx = 0; idx < dynamicObjectCollection.size(); ++idx) {
            DynamicObject dynamicObject = (DynamicObject)dynamicObjectCollection.get(idx);
            if (dynamicObject.getDynamicObject("rcroleentry") == null) continue;
            if (this.hasRolePerm(dynamicObject.getDynamicObject("rcroleentry").getString("id"), bizAppDyn.getString("id"), warnFromEntity.getString("number"))) {
                this.getView().setEnable(Boolean.TRUE, idx, new String[]{"roleperm"});
                tipMap.put(String.valueOf(idx), null);
            } else {
                this.getModel().setValue("roleperm", (Object)Boolean.FALSE, idx);
                this.getView().setEnable(Boolean.FALSE, idx, new String[]{"roleperm"});
                tipMap.put(String.valueOf(idx), tips);
            }
            DynamicObject reDy = (DynamicObject)entryEntity.get(idx);
            for (IDataEntityProperty bizChangedProperty : reDy.getDataEntityState().getBizChangedProperties()) {
                String name = bizChangedProperty.getName();
                if (!"roleperm".equals(name)) continue;
                reDy.getDataEntityState().setBizChanged(bizChangedProperty.getOrdinal(), false);
            }
        }
        model.endInit();
        this.showRoleFixTips(tipMap);
        this.getView().updateView("rcroleentryentity");
    }

    private boolean hasUserPerm(Long userId, String appId, String entity) {
        return PermissionServiceHelper.checkPermission((Long)userId, (String)appId, (String)entity, (String)"47150e89000000ac");
    }

    @ExcludeFromJacocoGeneratedReport
    private void showRoleFixTips(Map<String, String> tipMap) {
        if (tipMap.size() > 0) {
            CustomControl customControl = (CustomControl)this.getView().getControl("customcontrolaproletips");
            HashMap<String, Object> cellTips = new HashMap<String, Object>(16);
            cellTips.put("id", "roleperm");
            cellTips.put("ct", tipMap);
            cellTips.put("t", new Date().getTime());
            customControl.setData(cellTips);
            this.getView().updateView("customcontrolaproletips");
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean hasRolePerm(String roleId, String appId, String entity) {
        QFilter roleIdFilter = new QFilter("roleid", "in", (Object)roleId);
        QFilter bizAppFilter = new QFilter("roleperm.bizapp", "=", (Object)appId);
        QFilter entityFilter = new QFilter("roleperm.entity", "=", (Object)entity);
        DynamicObject[] rolePerms = BusinessDataServiceHelper.load((String)"perm_roleperm", (String)"roleperm, roleperm.bizapp, roleperm.entity,roleperm.entityname,roleperm.permitem", (QFilter[])new QFilter[]{roleIdFilter, bizAppFilter, entityFilter});
        return rolePerms != null && rolePerms.length > 0;
    }

    private void checkUserHasPerm() {
        DynamicObject sceneDy = (DynamicObject)this.getModel().getValue("warnscene");
        if (sceneDy == null) {
            return;
        }
        DynamicObject bizAppDyn = sceneDy.getDynamicObject("warnbizappid");
        if (bizAppDyn == null) {
            return;
        }
        DynamicObject warnFromEntity = sceneDy.getDynamicObject("warnfromentiy");
        if (warnFromEntity == null) {
            return;
        }
        DynamicObjectCollection dynamicObjectCollection = this.getModel().getEntryEntity("rcfixentryentity");
        if (dynamicObjectCollection == null) {
            return;
        }
        HashMap<String, String> tipMap = new HashMap<String, String>(16);
        String tips = String.format(ResManager.loadKDString((String)"\u6b64\u4eba\u5458\u6ca1\u6709\u201c%1s\u201d\u529f\u80fd\u6743\u9650\uff0c\u65e0\u6cd5\u6309\u7167\u6570\u636e\u6743\u9650\u63a7\u5236\u9884\u8b66\u6570\u636e\u8303\u56f4\uff0c\u5c06\u63a8\u9001\u5168\u91cf\u9884\u8b66\u6570\u636e\u3002", (String)"WarningSceneReceiverPermEdit_3", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), warnFromEntity.getString("name"));
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        DynamicObjectCollection entryEntity = model.getEntryEntity("rcfixentryentity");
        for (int idx = 0; idx < dynamicObjectCollection.size(); ++idx) {
            DynamicObject dynamicObject = (DynamicObject)dynamicObjectCollection.get(idx);
            if (dynamicObject.getDynamicObject("rcfixuser") == null) continue;
            if (this.hasUserPerm(dynamicObject.getDynamicObject("rcfixuser").getLong("id"), bizAppDyn.getString("id"), warnFromEntity.getString("id"))) {
                this.getView().setEnable(Boolean.TRUE, idx, new String[]{"rcfixperm"});
                tipMap.put(String.valueOf(idx), null);
            } else {
                this.getModel().setValue("rcfixperm", (Object)Boolean.FALSE, idx);
                this.getView().setEnable(Boolean.FALSE, idx, new String[]{"rcfixperm"});
                tipMap.put(String.valueOf(idx), tips);
            }
            DynamicObject reDy = (DynamicObject)entryEntity.get(idx);
            for (IDataEntityProperty bizChangedProperty : reDy.getDataEntityState().getBizChangedProperties()) {
                String name = bizChangedProperty.getName();
                if (!"rcfixperm".equals(name)) continue;
                reDy.getDataEntityState().setBizChanged(bizChangedProperty.getOrdinal(), false);
            }
        }
        model.endInit();
        this.showRcFixTips(tipMap);
        this.getView().updateView("rcfixentryentity");
    }

    private void setRcFixEntryColumnDisplay() {
        DynamicObject sceneDy = (DynamicObject)this.getModel().getValue("warnscene");
        if (sceneDy == null) {
            return;
        }
        Boolean permRc = sceneDy.getBoolean("permrc");
        if (permRc != null) {
            EntryGrid entryGrid = (EntryGrid)this.getView().getControl("rcfixentryentity");
            List fieldEdits = entryGrid.getFieldEdits();
            for (FieldEdit fieldEdit : fieldEdits) {
                if (!fieldEdit.getFieldKey().equals("rcfixperm")) continue;
                fieldEdit.setVisible("rcfixperm", permRc.booleanValue());
            }
            EntryGrid roleEntryGrid = (EntryGrid)this.getView().getControl("rcroleentryentity");
            List roleFieldEdits = roleEntryGrid.getFieldEdits();
            for (FieldEdit fieldEdit : roleFieldEdits) {
                if (!fieldEdit.getFieldKey().equals("roleperm")) continue;
                fieldEdit.setVisible("roleperm", permRc.booleanValue());
            }
            if (!permRc.booleanValue()) {
                String name;
                DynamicObject reDy;
                int idx;
                DynamicObjectCollection dynamicObjectUserCollection = this.getModel().getEntryEntity("rcfixentryentity");
                DynamicObjectCollection dynamicObjectRoleCollection = this.getModel().getEntryEntity("rcroleentryentity");
                AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
                model.beginInit();
                if (dynamicObjectUserCollection != null) {
                    for (idx = 0; idx < dynamicObjectUserCollection.size(); ++idx) {
                        this.getModel().setValue("rcfixperm", (Object)Boolean.FALSE, idx);
                        reDy = (DynamicObject)dynamicObjectUserCollection.get(idx);
                        for (IDataEntityProperty bizChangedProperty : reDy.getDataEntityState().getBizChangedProperties()) {
                            name = bizChangedProperty.getName();
                            if (!"rcfixperm".equals(name)) continue;
                            reDy.getDataEntityState().setBizChanged(bizChangedProperty.getOrdinal(), false);
                        }
                    }
                }
                if (dynamicObjectRoleCollection != null) {
                    for (idx = 0; idx < dynamicObjectRoleCollection.size(); ++idx) {
                        this.getModel().setValue("roleperm", (Object)Boolean.FALSE, idx);
                        reDy = (DynamicObject)dynamicObjectRoleCollection.get(idx);
                        for (IDataEntityProperty bizChangedProperty : reDy.getDataEntityState().getBizChangedProperties()) {
                            name = bizChangedProperty.getName();
                            if (!"roleperm".equals(name)) continue;
                            reDy.getDataEntityState().setBizChanged(bizChangedProperty.getOrdinal(), false);
                        }
                    }
                }
                model.endInit();
                this.getView().updateView("rcfixentryentity");
                this.getView().updateView("rcroleentryentity");
            }
        }
    }

    private void showRcFixTips(Map<String, String> tipMap) {
        if (tipMap.size() > 0) {
            CustomControl customControl = (CustomControl)this.getView().getControl("customcontrolapusertips");
            HashMap<String, Object> cellTips = new HashMap<String, Object>(16);
            cellTips.put("id", "rcfixperm");
            cellTips.put("ct", tipMap);
            cellTips.put("t", new Date().getTime());
            customControl.setData(cellTips);
            this.getView().updateView("customcontrolapusertips");
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void setRcRoleEntryData(ListSelectedRowCollection selectedRows) {
        if (null == selectedRows) {
            return;
        }
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        int startIdx = this.getModel().getEntryEntity("rcroleentryentity").size();
        if (!CollectionUtils.isEmpty((Collection)selectedRows)) {
            model.batchCreateNewEntryRow("rcroleentryentity", selectedRows.size());
            for (int i = 0; i < selectedRows.size(); ++i) {
                int idx = startIdx + i;
                ListSelectedRow row = selectedRows.get(i);
                model.setValue("rcroleentry", row.getPrimaryKeyValue(), idx);
                model.setValue("roleperm", (Object)Boolean.TRUE, idx);
            }
        }
        model.endInit();
        this.getView().updateView("rcroleentryentity");
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        try {
            AbstractOperate op = (AbstractOperate)args.getSource();
            String operateKey = op.getOperateKey();
            if ("rcfixadd".equals(operateKey)) {
                this.openRcFixF7();
            } else if ("rcroleadd".equals(operateKey)) {
                this.openRCRoleF7();
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    private void openRCRoleF7() {
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"hrcs_warnpermrolef7", (boolean)true, (int)0, (boolean)true);
        ListFilterParameter listFilterParameter = fsp.getListFilterParameter();
        List qFilters = listFilterParameter.getQFilters();
        if (this.getSelectedRoleIds().size() > 0) {
            qFilters.add(new QFilter("id", "not in", this.getSelectedRoleIds()));
        }
        fsp.setCloseCallBack(new CloseCallBack());
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setMultiSelect(true);
        fsp.setShowTitle(false);
        fsp.setHasRight(true);
        DynamicObject sceneDy = (DynamicObject)this.getModel().getValue("warnscene");
        if (sceneDy != null) {
            DynamicObject bizAppDyn = sceneDy.getDynamicObject("warnbizappid");
            DynamicObject warnFromEntity = sceneDy.getDynamicObject("warnfromentiy");
            if (bizAppDyn != null && warnFromEntity != null) {
                String onlyPermCheckboxMsg = String.format(ResManager.loadKDString((String)"\u4ec5\u663e\u793a\u5177\u6709\u201c%1s\u201d\u529f\u80fd\u6743\u9650\u7684\u89d2\u8272\u3002", (String)"WarningSceneReceiverPermEdit_5", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), warnFromEntity.getString("name"));
                fsp.setCustomParam("rolePermBizApp", (Object)bizAppDyn.getString("id"));
                fsp.setCustomParam("rolePermEntity", (Object)warnFromEntity.getString("id"));
                fsp.setCustomParam("onlyPermCheckboxMsg", (Object)onlyPermCheckboxMsg);
            }
        }
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "rcroleadd"));
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void openRcFixF7() {
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"bos_user", (boolean)true, (int)0, (boolean)true);
        ListFilterParameter listFilterParameter = fsp.getListFilterParameter();
        List qFilters = listFilterParameter.getQFilters();
        qFilters.add(new QFilter("id", "not in", this.getSelectedUserIds()));
        fsp.setCloseCallBack(new CloseCallBack());
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setMultiSelect(true);
        fsp.setShowTitle(false);
        fsp.setHasRight(true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "rcfixadd"));
        this.getView().showForm((FormShowParameter)fsp);
    }

    private List<Object> getSelectedRoleIds() {
        ArrayList<Object> selectIds = new ArrayList<Object>(10);
        DynamicObjectCollection dynColl = this.getModel().getEntryEntity("rcroleentryentity");
        if (dynColl != null && dynColl.size() > 0) {
            dynColl.forEach(item -> selectIds.add(item.getDynamicObject("rcroleentry").get("id")));
        }
        return selectIds;
    }

    private void setRcFixEntryData(ListSelectedRowCollection selectedRows) {
        if (null == selectedRows) {
            return;
        }
        Map<Object, String> userOrgMap = this.getUserOrg(selectedRows);
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        int startIdx = this.getModel().getEntryEntity("rcfixentryentity").size();
        if (!CollectionUtils.isEmpty((Collection)selectedRows)) {
            model.batchCreateNewEntryRow("rcfixentryentity", selectedRows.size());
            for (int i = 0; i < selectedRows.size(); ++i) {
                int idx = startIdx + i;
                ListSelectedRow row = selectedRows.get(i);
                model.setValue("rcfixuser", row.getPrimaryKeyValue(), idx);
                model.setValue("rcfixperm", (Object)Boolean.TRUE, idx);
                if (!userOrgMap.containsKey(row.getPrimaryKeyValue())) continue;
                model.setValue("rcfixuserorg", (Object)userOrgMap.get(row.getPrimaryKeyValue()), idx);
            }
        }
        model.endInit();
        this.getView().updateView("rcfixentryentity");
    }
}
