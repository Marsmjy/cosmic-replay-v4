/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleDynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FieldTip
 *  kd.bos.form.FieldTip$FieldTipsLevel
 *  kd.bos.form.FieldTip$FieldTipsTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IPageCache
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.Label
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.mvc.bill.BillView
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.service.perm.ChoiceFieldPageCustomQueryService
 *  kd.hr.hrcs.bussiness.service.perm.init.PermInitFinishValidateService
 *  kd.hr.hrcs.bussiness.service.perm.init.PermRoleInitFinishValidateService
 *  kd.hr.hrcs.bussiness.service.perm.init.UserPermInitConvertService
 *  kd.hr.hrcs.bussiness.service.perm.init.roleinit.PermRoleInitService
 *  org.apache.commons.collections.MapUtils
 */
package kd.hr.hrcs.formplugin.web.perm.init;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleDynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FieldTip;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IPageCache;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.control.Label;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.mvc.bill.BillView;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.service.perm.ChoiceFieldPageCustomQueryService;
import kd.hr.hrcs.bussiness.service.perm.init.PermInitFinishValidateService;
import kd.hr.hrcs.bussiness.service.perm.init.PermRoleInitFinishValidateService;
import kd.hr.hrcs.bussiness.service.perm.init.UserPermInitConvertService;
import kd.hr.hrcs.bussiness.service.perm.init.roleinit.PermRoleInitService;
import org.apache.commons.collections.MapUtils;

public final class PermInitRecordEdit
extends HRDataBaseEdit {
    private static final String FIELD_INITTYPE = "inittype";
    private static final String USERROLE = "userrole";
    private static final String ROLE = "role";
    private static final String FIELD_DEALSTATUS = "dealstatus";
    private static final String BTN_USERIMPORT = "userimport";
    private static final String BTN_ROLEIMPORT = "roleimport";

    public void beforeBindData(EventObject evt) {
        super.beforeBindData(evt);
        String recordId = (String)this.getView().getFormShowParameter().getCustomParam("recordId");
        String initType = (String)this.getView().getFormShowParameter().getCustomParam(FIELD_INITTYPE);
        this.initViewVisible(initType);
        if (HRStringUtils.isNotEmpty((String)recordId)) {
            if (ROLE.equals(initType)) {
                this.showRoleFuncForm(recordId);
                this.showRoleDimForm(recordId);
                this.showRoleDrForm(recordId);
                this.initRoleFdEntry(recordId);
                this.setFlexVisible(initType);
            } else {
                this.showDynaDimForm(recordId);
                this.paintErrMarkCol();
                this.setFlexVisible(initType);
            }
        } else if (ROLE.equals(initType)) {
            this.getModel().setValue(FIELD_INITTYPE, (Object)ROLE);
        } else {
            this.getModel().setValue(FIELD_INITTYPE, (Object)USERROLE);
        }
        if (HRStringUtils.equals((String)((String)this.getModel().getValue(FIELD_DEALSTATUS)), (String)"1")) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"finishiinit", "flexpanelap4"});
        }
        this.getModel().setDataChanged(false);
    }

    private void initRoleFdEntry(String recordId) {
        HashMap typeMap = Maps.newHashMapWithExpectedSize((int)16);
        DynamicObjectCollection fieldEntry = this.getModel().getEntryEntity("rolefieldentry");
        Map<String, String> roleNameMap = this.getRoleNameMap();
        for (DynamicObject entry : fieldEntry) {
            String roleNumber = entry.getString("rfield_rolenumber");
            entry.set("rfield_rolename", (Object)roleNameMap.get(roleNumber));
            String entityNumber = entry.getString("rfield_entitytype.number");
            String propKey = entry.getString("rfield_propkey");
            MainEntityType entityType = typeMap.getOrDefault(entityNumber, EntityMetadataCache.getDataEntityType((String)entityNumber));
            DataEntityPropertyCollection properties = entityType.getProperties();
            if (!Objects.nonNull(properties.get((Object)propKey))) continue;
            String propName = ((IDataEntityProperty)properties.get((Object)propKey)).getDisplayName() == null ? ((IDataEntityProperty)properties.get((Object)propKey)).getName() : ((IDataEntityProperty)properties.get((Object)propKey)).getDisplayName().getLocaleValue();
            entry.set("rfield_propname", (Object)propName);
        }
        this.getModel().setDataChanged(false);
    }

    private Map<String, String> getRoleNameMap() {
        DynamicObjectCollection roleBaseCol = this.getModel().getEntryEntity("rolebaseentry");
        LinkedHashMap roleNameMap = Maps.newLinkedHashMapWithExpectedSize((int)16);
        for (DynamicObject dynamicObject : roleBaseCol) {
            String rbaseName = dynamicObject.getString("rbase_name");
            String rbaseNumber = dynamicObject.getString("rbase_number");
            roleNameMap.put(rbaseNumber, rbaseName);
        }
        return roleNameMap;
    }

    private void initViewVisible(String initType) {
        Label label1 = (Label)this.getView().getControl("labelap4");
        Label label2 = (Label)this.getView().getControl("labelap");
        if (ROLE.equals(initType)) {
            label1.setText(ResManager.loadKDString((String)"1\u3001\u521d\u59cb\u5316\u89d2\u8272\u4e4b\u524d\uff0c\u8bf7\u786e\u8ba4\u6743\u9650\u57fa\u7840\u914d\u7f6e\u662f\u5426\u5df2\u5b8c\u6210\u7ef4\u62a4\uff0c\u4ee5\u514d\u5f15\u5165\u540e\u6570\u636e\u6709\u8bef\uff1b2\u3001\u53ea\u652f\u6301\u65b0\u589e\u89d2\u8272\uff0c\u4e0d\u652f\u6301\u589e\u91cf\u573a\u666f\uff0c\u8bf7\u786e\u8ba4\u6570\u636e\u5b8c\u6574\u6027\u540e\u518d\u7ed3\u675f\u521d\u59cb\u5316\uff1b", (String)"PermInitRecordEdit_13", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            label2.setText(ResManager.loadKDString((String)"\u6682\u65e0\u6570\u636e\uff0c\u8bf7\u5148\u5bfc\u5165\u89d2\u8272\u6743\u9650\u3002", (String)"PermInitRecordEdit_15", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        } else {
            label1.setText(ResManager.loadKDString((String)"1\u3001\u521d\u59cb\u5316\u7528\u6237\u6743\u9650\u4e4b\u524d\uff0c\u8bf7\u5148\u914d\u7f6e\u6743\u9650\u57fa\u7840\u914d\u7f6e\u3001\u7ef4\u62a4\u89d2\u8272\u6570\u636e\uff1b2\u3001\u521d\u59cb\u5316\u53ea\u652f\u6301\u65b0\u589e\u7528\u6237\u89d2\u8272\u6570\u636e\uff0c\u4e0d\u66f4\u65b0\u5df2\u6709\u7684\u6743\u9650\u8bb0\u5f55\uff1b", (String)"PermInitRecordEdit_12", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            label2.setText(ResManager.loadKDString((String)"\u6682\u65e0\u6570\u636e\uff0c\u8bf7\u5148\u5bfc\u5165\u7528\u6237\u6743\u9650\u3002", (String)"PermInitRecordEdit_14", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        }
        this.getView().setEnable(Boolean.FALSE, new String[]{FIELD_INITTYPE});
        this.getView().setVisible(Boolean.valueOf(USERROLE.equals(initType)), new String[]{BTN_USERIMPORT});
        this.getView().setVisible(Boolean.valueOf(ROLE.equals(initType)), new String[]{BTN_ROLEIMPORT});
        this.getView().setVisible(Boolean.FALSE, new String[]{"tabap1"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"tabap"});
    }

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        this.addClickListeners(new String[]{BTN_USERIMPORT});
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        String operateKey = args.getOperateKey();
        if (HRStringUtils.equals((String)"save", (String)operateKey) && args.getOperationResult().isSuccess() && HRStringUtils.isNotEmpty((String)this.getPageCache().get("importInvokeSave"))) {
            this.getPageCache().remove("importInvokeSave");
            args.getOperationResult().setShowMessage(false);
        }
        String initType = (String)this.getModel().getValue(FIELD_INITTYPE);
        this.setFlexVisible(initType);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        String operateKey = formOperate.getOperateKey();
        String initType = (String)this.getModel().getValue(FIELD_INITTYPE);
        DynamicObject dataEntity = this.getView().getModel().getDataEntity();
        ORM orm = ORM.create();
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_perminitrecord");
        if (BTN_USERIMPORT.equals(operateKey)) {
            if (this.checkTaskName(args, dataEntity, orm, helper)) {
                args.setCancel(true);
                return;
            }
            if (this.getModel().getEntryRowCount("userdimentry") > 0) {
                this.getView().showConfirm(ResManager.loadKDString((String)"\u5bfc\u5165\u5c06\u6e05\u7a7a\u5df2\u6709\u7684\u6570\u636e\uff0c\u662f\u5426\u5bfc\u5165?", (String)"PermInitRecordEdit_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, new ConfirmCallBackListener("reimport"));
            } else {
                this.showPermImportStart();
            }
            this.setFlexVisible(initType);
            args.setCancel(true);
        } else if (BTN_ROLEIMPORT.equals(operateKey)) {
            if (this.checkTaskName(args, dataEntity, orm, helper)) {
                args.setCancel(true);
                return;
            }
            if (this.getModel().getEntryRowCount("rolebaseentry") > 0) {
                this.getView().showConfirm(ResManager.loadKDString((String)"\u5bfc\u5165\u5c06\u6e05\u7a7a\u5df2\u6709\u7684\u6570\u636e\uff0c\u662f\u5426\u5bfc\u5165?", (String)"PermInitRecordEdit_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, new ConfirmCallBackListener("reimport"));
            } else {
                this.showRoleImportStart();
            }
            this.setFlexVisible(initType);
            args.setCancel(true);
        } else if ("finishinit".equals(operateKey)) {
            if (ROLE.equals(initType)) {
                if (dataEntity.getLong("id") != 0L) {
                    Long recordId;
                    DynamicObject dyn;
                    boolean dataChanged = this.getModel().getDataChanged();
                    if (dataChanged) {
                        if (HRStringUtils.isEmpty((String)dataEntity.getString("name"))) {
                            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u6309\u8981\u6c42\u586b\u5199\u201c\u4efb\u52a1\u540d\u79f0\u201d\u3002", (String)"PermInitRecordEdit_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                            FieldTip fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "name", ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)"RoleInitRecordEdit_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                            this.getView().showFieldTip(fieldTip);
                            args.setCancel(true);
                            return;
                        }
                        this.getPageCache().put("importInvokeSave", "1");
                        this.getView().invokeOperation("save");
                    }
                    if (!this.hasRoleEntryData(dyn = helper.queryOne((Object)(recordId = Long.valueOf(dataEntity.getLong("id")))))) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u6682\u65e0\u6570\u636e\uff0c\u8bf7\u5148\u5bfc\u5165\u89d2\u8272\u6743\u9650\u3002", (String)"PermInitRecordEdit_15", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                        return;
                    }
                    String recordNumber = dataEntity.getString("number");
                    PermRoleInitFinishValidateService finishValidateService = new PermRoleInitFinishValidateService();
                    int[] errorNumArr = finishValidateService.clickFinishValidate(recordId);
                    if (errorNumArr[0] + errorNumArr[1] + errorNumArr[2] + errorNumArr[3] + errorNumArr[4] > 0) {
                        FormShowParameter showParameter = new FormShowParameter();
                        showParameter.setFormId("hrcs_roleinitcheckresult");
                        showParameter.getOpenStyle().setShowType(ShowType.Modal);
                        showParameter.setCustomParam("recordId", (Object)recordId.toString());
                        showParameter.setCustomParam("recordNumber", (Object)recordNumber);
                        showParameter.setCustomParam("roleErrorNum", (Object)errorNumArr[0]);
                        showParameter.setCustomParam("funcItemErrorNum", (Object)errorNumArr[1]);
                        showParameter.setCustomParam("dimErrorNum", (Object)errorNumArr[2]);
                        showParameter.setCustomParam("dataRangeErrorNum", (Object)errorNumArr[3]);
                        showParameter.setCustomParam("fieldErrorNum", (Object)errorNumArr[4]);
                        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "permimportstart"));
                        this.getView().showForm(showParameter);
                    } else {
                        boolean result = PermRoleInitService.getInstance().initRole(recordId);
                        if (result) {
                            this.getModel().setValue(FIELD_DEALSTATUS, (Object)"1");
                            this.getPageCache().put("importInvokeSave", "1");
                            this.getView().invokeOperation("save");
                            this.getView().setVisible(Boolean.valueOf(false), new String[]{"finishiinit"});
                        }
                    }
                } else {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u6682\u65e0\u6570\u636e\uff0c\u8bf7\u5148\u5bfc\u5165\u89d2\u8272\u6743\u9650\u3002", (String)"PermInitRecordEdit_15", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                }
            } else if (dataEntity.getLong("id") != 0L) {
                Long recordId;
                DynamicObject dyn;
                boolean dataChanged = this.getModel().getDataChanged();
                if (dataChanged) {
                    if (HRStringUtils.isEmpty((String)dataEntity.getString("name"))) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u6309\u8981\u6c42\u586b\u5199\u201c\u4efb\u52a1\u540d\u79f0\u201d\u3002", (String)"PermInitRecordEdit_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                        FieldTip fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "name", ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)"RoleInitRecordEdit_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                        this.getView().showFieldTip(fieldTip);
                        args.setCancel(true);
                        return;
                    }
                    this.getPageCache().put("importInvokeSave", "1");
                    this.getView().invokeOperation("save");
                }
                if (!this.hasUserRoleEntryData(dyn = helper.queryOne((Object)(recordId = Long.valueOf(dataEntity.getLong("id")))))) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u6682\u65e0\u6570\u636e\uff0c\u8bf7\u5148\u5bfc\u5165\u7528\u6237\u6743\u9650\u3002", (String)"PermInitRecordEdit_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    return;
                }
                String recordNumber = dataEntity.getString("number");
                PermInitFinishValidateService finishValidateService = new PermInitFinishValidateService();
                int[] errorNumArr = finishValidateService.clickFinishValidate(recordId);
                if (errorNumArr[0] + errorNumArr[1] + errorNumArr[2] + errorNumArr[3] > 0) {
                    FormShowParameter showParameter = new FormShowParameter();
                    showParameter.setFormId("hrcs_perminitcheckresult");
                    showParameter.getOpenStyle().setShowType(ShowType.Modal);
                    showParameter.setCustomParam("recordId", (Object)recordId.toString());
                    showParameter.setCustomParam("recordNumber", (Object)recordNumber);
                    showParameter.setCustomParam("userRoleErrorNum", (Object)errorNumArr[0]);
                    showParameter.setCustomParam("dateRuleErrorNum", (Object)errorNumArr[1]);
                    showParameter.setCustomParam("bdDataRuleErrorNum", (Object)errorNumArr[2]);
                    showParameter.setCustomParam("fieldErrorNum", (Object)errorNumArr[3]);
                    CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "importFinish");
                    showParameter.setCloseCallBack(closeCallBack);
                    this.getView().showForm(showParameter);
                    return;
                }
                boolean result = UserPermInitConvertService.convertRecord((Long)recordId);
                if (result) {
                    this.getModel().setValue(FIELD_DEALSTATUS, (Object)"1");
                    this.getPageCache().put("importInvokeSave", "1");
                    this.getView().invokeOperation("save");
                    this.getView().setVisible(Boolean.valueOf(false), new String[]{"finishiinit"});
                } else {
                    this.getView().showErrorNotification("FAIL!");
                }
            } else {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u6682\u65e0\u6570\u636e\uff0c\u8bf7\u5148\u5bfc\u5165\u7528\u6237\u6743\u9650\u3002", (String)"PermInitRecordEdit_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            }
        } else if ("save".equals(operateKey) && dataEntity.getLong("id") == 0L) {
            long id = orm.genLongId("hrcs_perminitrecord");
            dataEntity.set("id", (Object)id);
        }
    }

    private boolean checkTaskName(BeforeDoOperationEventArgs args, DynamicObject dataEntity, ORM orm, HRBaseServiceHelper helper) {
        if (HRStringUtils.isEmpty((String)dataEntity.getString("name"))) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u6309\u8981\u6c42\u586b\u5199\u201c\u4efb\u52a1\u540d\u79f0\u201d\u3002", (String)"PermInitRecordEdit_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            FieldTip fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "name", ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)"RoleInitRecordEdit_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            this.getView().showFieldTip(fieldTip);
            args.setCancel(true);
            return true;
        }
        QFilter[] filters = new QFilter[]{new QFilter("name", "=", (Object)dataEntity.getString("name")).and("id", "!=", (Object)dataEntity.getLong("id"))};
        DynamicObjectCollection dynamicObjects = helper.queryOriginalCollection("id,dealstatus", filters);
        boolean isExists = false;
        if (CollectionUtils.isNotEmpty((Collection)dynamicObjects)) {
            for (DynamicObject dynamicObject : dynamicObjects) {
                String dealStatus = dynamicObject.getString(FIELD_DEALSTATUS);
                long id = dynamicObject.getLong("id");
                if ("1".equals(dealStatus)) {
                    isExists = true;
                    continue;
                }
                helper.deleteOne((Object)id);
            }
        }
        if (isExists) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u201c\u4efb\u52a1\u540d\u79f0\u201d\u91cd\u590d\u3002", (String)"RoleInitRecordEdit_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return true;
        }
        if (dataEntity.getLong("id") == 0L) {
            Long id = orm.genLongId("hrcs_perminitrecord");
            dataEntity.set("id", (Object)id);
            this.getView().getFormShowParameter().setCustomParam("recordId", (Object)String.valueOf(id));
            this.getPageCache().put("importInvokeSave", "1");
            this.getView().invokeOperation("save");
        }
        return false;
    }

    public void propertyChanged(PropertyChangedArgs args) {
        String propName = args.getProperty().getName();
        if (HRStringUtils.equals((String)propName, (String)"name")) {
            LocaleDynamicObjectCollection newValue = (LocaleDynamicObjectCollection)args.getChangeSet()[0].getNewValue();
            LocaleDynamicObjectCollection oldValue = (LocaleDynamicObjectCollection)args.getChangeSet()[0].getOldValue();
            if (oldValue.size() <= 0 && newValue.size() > 0) {
                FieldTip fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Info, FieldTip.FieldTipsTypes.notNull, "name", "");
                fieldTip.setSuccess(true);
                this.getView().showFieldTip(fieldTip);
            }
        }
    }

    private boolean hasUserRoleEntryData(DynamicObject dyn) {
        if (dyn != null) {
            DynamicObjectCollection userdimentry = dyn.getDynamicObjectCollection("userdimentry");
            return userdimentry != null && userdimentry.size() > 0;
        }
        return false;
    }

    private boolean hasRoleEntryData(DynamicObject dyn) {
        if (dyn != null) {
            DynamicObjectCollection userdimentry = dyn.getDynamicObjectCollection("rolebaseentry");
            return userdimentry != null && userdimentry.size() > 0;
        }
        return false;
    }

    public void beforeClosed(BeforeClosedEvent evt) {
        super.beforeClosed(evt);
        BillView billView = (BillView)evt.getSource();
        DynamicObject dataEntity = billView.getModel().getDataEntity();
        String dealStatus = dataEntity.getString(FIELD_DEALSTATUS);
        long id = dataEntity.getLong("id");
        if (HRStringUtils.equals((String)dealStatus, (String)"0") && id != 0L) {
            new HRBaseServiceHelper("hrcs_perminitrecord").deleteOne((Object)id);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        String callBackId = evt.getCallBackId();
        String initType = (String)this.getModel().getValue(FIELD_INITTYPE);
        if (HRStringUtils.equals((String)callBackId, (String)"reimport") && evt.getResult().equals((Object)MessageBoxResult.Yes)) {
            if (ROLE.equals(initType)) {
                DynamicObject dataEntity = this.getView().getModel().getDataEntity();
                String id = dataEntity.getString("id");
                HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_perminitrecord");
                DynamicObject object = helper.queryOne((Object)Long.parseLong(id));
                object.set("rolebaseentry", null);
                object.set("rolefuncentry", null);
                object.set("roledimentry", null);
                object.set("roledataentry", null);
                object.set("rolefieldentry", null);
                object.set("name", dataEntity.get("name"));
                helper.saveOne(object);
                this.getPageCache().put("deltempData", "success");
                this.getView().invokeOperation("refresh");
                this.showRoleImportStart();
            } else {
                DynamicObject dataEntity = this.getView().getModel().getDataEntity();
                String id = dataEntity.getString("id");
                HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_perminitrecord");
                DynamicObject object = helper.queryOne((Object)Long.parseLong(id));
                object.set("userdimentry", null);
                object.set("userdataruleentry", null);
                object.set("userbdentry", null);
                object.set("userfieldentry", null);
                object.set("name", dataEntity.get("name"));
                helper.saveOne(object);
                this.getPageCache().put("deltempData", "success");
                this.getView().invokeOperation("refresh");
                this.showPermImportStart();
            }
        }
    }

    private void showPermImportStart() {
        DynamicObject dataEntity = this.getView().getModel().getDataEntity();
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_permimportstart");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setCustomParam("recordId", (Object)dataEntity.getString("id"));
        showParameter.setCustomParam("importFlag", (Object)"userRole");
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "importFinish");
        showParameter.setCloseCallBack(closeCallBack);
        this.getView().showForm(showParameter);
    }

    private void showRoleImportStart() {
        DynamicObject dataEntity = this.getView().getModel().getDataEntity();
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_rolepermimportstart");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setCustomParam("recordId", (Object)dataEntity.getString("id"));
        showParameter.setCustomParam("importFlag", (Object)ROLE);
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "importFinish");
        showParameter.setCloseCallBack(closeCallBack);
        this.getView().showForm(showParameter);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        Object returnData;
        super.closedCallBack(closedCallBackEvent);
        String success = this.getView().getPageCache().get("success");
        String delTempData = this.getView().getPageCache().get("deltempData");
        String initType = (String)this.getModel().getValue(FIELD_INITTYPE);
        if (HRStringUtils.equals((String)closedCallBackEvent.getActionId(), (String)"importFinish") && HRStringUtils.equals((String)"success", (String)success)) {
            if (ROLE.equals(initType)) {
                this.getView().invokeOperation("refresh");
            } else {
                this.getView().updateView("userdimentry");
                this.getView().updateView("userdataruleentry");
                this.getView().invokeOperation("refresh");
                this.setFlexVisible(initType);
                this.showDynaDimForm(this.getModel().getDataEntity().getString("id"));
            }
        } else if (HRStringUtils.equals((String)"success", (String)delTempData) && HRStringUtils.isNotEmpty((String)success) && HRStringUtils.equals((String)"success", (String)success)) {
            this.getView().invokeOperation("refresh");
            this.setFlexVisible(initType);
            if (ROLE.equals(initType)) {
                String recordId = this.getModel().getDataEntity().getString("id");
                this.showRoleFuncForm(recordId);
                this.showRoleDimForm(recordId);
                this.showRoleDrForm(recordId);
                this.initRoleFdEntry(recordId);
            } else {
                this.showDynaDimForm(this.getModel().getDataEntity().getString("id"));
            }
        } else if (HRStringUtils.equals((String)"permimportstart", (String)closedCallBackEvent.getActionId()) && !ObjectUtils.isEmpty((Object)(returnData = closedCallBackEvent.getReturnData())) && HRStringUtils.equals((String)"showPermImportStart", (String)((String)returnData))) {
            this.showRoleImportStart();
        }
    }

    private void showDynaDimForm(String recordId) {
        FormShowParameter fsp = new FormShowParameter();
        fsp.setFormId("hrcs_dynadim");
        fsp.setCustomParam("recordId", (Object)recordId);
        fsp.getOpenStyle().setShowType(ShowType.InContainer);
        fsp.getOpenStyle().setTargetKey("tabpageap");
        this.getView().showForm(fsp);
    }

    private void showRoleFuncForm(String recordId) {
        FormShowParameter fsp = new FormShowParameter();
        fsp.setFormId("hrcs_permroleinitfunc");
        fsp.setCustomParam("recordId", (Object)recordId);
        fsp.getOpenStyle().setShowType(ShowType.InContainer);
        fsp.getOpenStyle().setTargetKey("tabpageap5");
        this.getView().showForm(fsp);
    }

    private void showRoleDimForm(String recordId) {
        FormShowParameter fsp = new FormShowParameter();
        fsp.setFormId("hrcs_permroleinitdim");
        fsp.setCustomParam("recordId", (Object)recordId);
        fsp.getOpenStyle().setShowType(ShowType.InContainer);
        fsp.getOpenStyle().setTargetKey("tabpageap6");
        this.getView().showForm(fsp);
    }

    private void showRoleDrForm(String recordId) {
        FormShowParameter fsp = new FormShowParameter();
        fsp.setFormId("hrcs_permroleinitdr");
        fsp.setCustomParam("recordId", (Object)recordId);
        fsp.getOpenStyle().setShowType(ShowType.InContainer);
        fsp.getOpenStyle().setTargetKey("tabpageap7");
        this.getView().showForm(fsp);
    }

    private void paintErrMarkCol() {
        IPageCache pageCache = this.getPageCache();
        String entityFieldsStr = pageCache.get("entityFields");
        HashMap<String, Map<String, String>> entityFields = HRStringUtils.isNotEmpty((String)entityFieldsStr) ? (HashMap<String, Map<String, String>>)SerializationUtils.fromJsonString((String)entityFieldsStr, Map.class) : new HashMap<String, Map<String, String>>(16);
        this.getModel().beginInit();
        DynamicObjectCollection bdCol = this.getModel().getEntryEntity("userbdentry");
        ChoiceFieldPageCustomQueryService customQuery = new ChoiceFieldPageCustomQueryService();
        for (int index = 0; index < bdCol.size(); ++index) {
            DynamicObject dbRow = (DynamicObject)bdCol.get(index);
            String entity = dbRow.getString("bd_entitytype.id");
            String propKey = dbRow.getString("bd_propkey");
            if (HRStringUtils.isEmpty((String)entity)) continue;
            Map<String, String> fieldMap = (Map<String, String>)entityFields.get(entity);
            if (MapUtils.isEmpty((Map)fieldMap)) {
                MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entity);
                fieldMap = customQuery.parseProperty((IDataEntityType)mainEntityType).stream().collect(Collectors.toMap(it -> (String)it.get("field_id"), it -> (String)it.get("field_name"), (newVal, oldVal) -> oldVal));
                entityFields.put(entity, fieldMap);
            }
            String propName = (String)fieldMap.get(propKey);
            this.getModel().setValue("bd_propname", (Object)(HRStringUtils.isEmpty((String)propName) ? propKey : propName), index);
        }
        DynamicObjectCollection fieldCol = this.getModel().getEntryEntity("userfieldentry");
        for (int index = 0; index < fieldCol.size(); ++index) {
            DynamicObject fieldRow = (DynamicObject)fieldCol.get(index);
            String entity = fieldRow.getString("field_entitytype.id");
            String propKey = fieldRow.getString("field_propkey");
            if (HRStringUtils.isEmpty((String)entity)) continue;
            Map<String, String> fieldMap = (Map<String, String>)entityFields.get(entity);
            if (MapUtils.isEmpty((Map)fieldMap)) {
                MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entity);
                fieldMap = customQuery.parseProperty((IDataEntityType)mainEntityType).stream().collect(Collectors.toMap(it -> (String)it.get("field_id"), it -> (String)it.get("field_name"), (newVal, oldVal) -> oldVal));
                entityFields.put(entity, fieldMap);
            }
            String propName = (String)fieldMap.get(propKey);
            this.getModel().setValue("field_propname", (Object)(HRStringUtils.isEmpty((String)propName) ? propKey : propName), index);
        }
        this.getModel().endInit();
        this.getModel().setDataChanged(false);
        this.getView().updateView("userbdentry");
        this.getView().updateView("userfieldentry");
        pageCache.put("entityFields", SerializationUtils.toJsonString(entityFields));
    }

    private void setFlexVisible(String initType) {
        if (ROLE.equals(initType)) {
            int entryRowCount = this.getModel().getEntryRowCount("rolebaseentry");
            if (entryRowCount <= 0) {
                this.getView().setVisible(Boolean.TRUE, new String[]{"nodatatipflex"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"tabap1"});
            } else {
                this.getView().setVisible(Boolean.TRUE, new String[]{"tabap1"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"nodatatipflex"});
            }
        } else {
            int entryRowCount = this.getModel().getEntryRowCount("userdimentry");
            if (entryRowCount <= 0) {
                this.getView().setVisible(Boolean.TRUE, new String[]{"nodatatipflex"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"tabap"});
            } else {
                this.getView().setVisible(Boolean.TRUE, new String[]{"tabap"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"nodatatipflex"});
            }
        }
    }
}
