/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.service.labelandreport.DataPreviewService
 *  kd.hr.hbp.common.model.complexobj.HRComplexObjContext
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.common.util.ReportTimeZoneUtil
 *  kd.hr.hrptmc.business.anobj.AnObjDataStoreTaskServiceHelper
 *  kd.hr.hrptmc.business.repcalculate.algox.helper.AlgoxJobKeyHelper
 *  kd.hr.hrptmc.common.model.anobj.EntityRelationBo
 *  kd.hr.hrptmc.common.model.anobj.JoinEntityBo
 *  kd.hr.hrptmc.common.model.anobj.QueryFieldBo
 *  kd.hr.hrptmc.common.model.calfield.CalculateFieldBo
 *  kd.hr.hrptmc.common.util.ReportCommonUtils
 *  kd.hr.hrptmc.common.util.ReportComplexObjTransferUtil
 *  kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCommonEdit
 */
package kd.hr.hrptmc.formplugin.web.anobj;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.container.Tab;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.service.labelandreport.DataPreviewService;
import kd.hr.hbp.common.model.complexobj.HRComplexObjContext;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.common.util.ReportTimeZoneUtil;
import kd.hr.hrptmc.business.anobj.AnObjDataStoreTaskServiceHelper;
import kd.hr.hrptmc.business.repcalculate.algox.helper.AlgoxJobKeyHelper;
import kd.hr.hrptmc.common.model.anobj.EntityRelationBo;
import kd.hr.hrptmc.common.model.anobj.JoinEntityBo;
import kd.hr.hrptmc.common.model.anobj.QueryFieldBo;
import kd.hr.hrptmc.common.model.calfield.CalculateFieldBo;
import kd.hr.hrptmc.common.util.ReportCommonUtils;
import kd.hr.hrptmc.common.util.ReportComplexObjTransferUtil;
import kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCommonEdit;

public final class ReportAnalyseObjectEdit
extends AnalyseObjectCommonEdit {
    private static final Log LOGGER = LogFactory.getLog(ReportAnalyseObjectEdit.class);

    public void customEvent(CustomEventArgs args) {
        if (HRStringUtils.equals((String)args.getEventName(), (String)"dragEntity")) {
            this.entityProcessor.dragEntity(args);
        } else if (HRStringUtils.equals((String)args.getEventName(), (String)"clickRelevance")) {
            this.entityProcessor.clickRelevance(args);
        } else if (HRStringUtils.equals((String)args.getEventName(), (String)"delEntity")) {
            this.entityProcessor.delEntity(args);
        } else if (HRStringUtils.equals((String)args.getEventName(), (String)"modifyFieldName")) {
            this.fieldProcessor.openModifyFieldPage(args);
        } else if (HRStringUtils.equals((String)args.getEventName(), (String)"changeDataSource")) {
            this.dataProcessor.changeDataSource(args);
        } else if (HRStringUtils.equals((String)args.getEventName(), (String)"getAllData")) {
            this.dataProcessor.getAllData(args);
        }
    }

    public void afterLoadData(EventObject eventObject) {
        super.afterLoadData(eventObject);
        this.initProcessor.initData(false);
    }

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        HashMap map = new HashMap(1);
        HashMap<String, String> item = new HashMap<String, String>(1);
        map.put("item", item);
        if (ReportCommonUtils.validateNumberByISVIsKD()) {
            item.put("emptytip", String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u5f00\u5934\uff0c\u652f\u6301\u82f1\u6587\u3001\u6570\u5b57\u548c\u4e0b\u5212\u7ebf\u3002", (String)"ReportAnalyseObjectEdit_8", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), "kdhr_"));
        } else {
            item.put("emptytip", ResManager.loadKDString((String)"\u4ec5\u652f\u6301\u82f1\u6587\u3001\u6570\u5b57\u548c\u4e0b\u5212\u7ebf\u3002", (String)"ReportAnalyseObjectEdit_9", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
        }
        this.getView().updateControlMetadata("number", map);
        if (this.copyProcessor.isCopyAnObj()) {
            this.copyProcessor.queryDataAndSetValueInModel();
            this.initProcessor.initData(true);
        }
    }

    public void afterBindData(EventObject evt) {
        super.afterBindData(evt);
        this.getView().setVisible(Boolean.FALSE, new String[]{"laststepbtn", "previewbtn", "bar_save"});
        if (this.getView().getFormShowParameter().getStatus() == OperationStatus.EDIT && ((Boolean)this.getModel().getValue("issyspreset")).booleanValue()) {
            ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        boolean needGetAllConfigData;
        Tab tab = (Tab)this.getView().getControl("tabap");
        String nextTab = this.formProcessor.getNextTab(tab);
        if (HRStringUtils.equals((String)evt.getOperationKey(), (String)"nextstep")) {
            if (nextTab.equals("definefield")) {
                boolean numberValid = this.dataProcessor.validateNumber();
                boolean nameValid = this.dataProcessor.validateDuplicateName();
                if (!numberValid || !nameValid) {
                    evt.setCancel(true);
                    return;
                }
            }
            String initCompleted = this.getPageCache().get("initCompleted");
            if (HRStringUtils.equals((String)nextTab, (String)"definefield") && !HRStringUtils.equals((String)initCompleted, (String)"1")) {
                this.initProcessor.initCustomControl();
            } else if (nextTab.equals("definefield")) {
                this.formProcessor.sendFlagForToStep2();
            }
        }
        boolean bl = needGetAllConfigData = HRStringUtils.equals((String)evt.getOperationKey(), (String)"nextstep") && HRStringUtils.equals((String)nextTab, (String)"filterdata") || HRStringUtils.equals((String)evt.getOperationKey(), (String)"previewdata") || HRStringUtils.equals((String)evt.getOperationKey(), (String)"save") && HRStringUtils.equals((String)nextTab, (String)"filterdata");
        if (needGetAllConfigData) {
            this.getView().showLoading(new LocaleString(ResManager.loadKDString((String)"\u6b63\u5728\u52a0\u8f7d...", (String)"ReportAnalyseObjectEdit_23", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0])));
            evt.setCancel(true);
            this.formProcessor.sendFlagForGetAllData(evt.getOperationKey(), nextTab);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        Tab tab = (Tab)this.getView().getControl("tabap");
        switch (operateKey) {
            case "save": {
                this.beforeSave(tab, args, op);
                break;
            }
            case "laststep": {
                this.beforeLastStepOption(tab);
                break;
            }
            case "nextstep": {
                this.beforeNextStepOption(tab, args);
                break;
            }
            case "previewdata": {
                if (this.dataProcessor.validateFieldDefineData("previewdata")) {
                    List allJoinEntity = this.commonProcessor.getAllJoinEntity();
                    boolean virtualEntity = ((JoinEntityBo)allJoinEntity.get(0)).getVirtualEntity();
                    if (!virtualEntity) {
                        this.fieldProcessor.setFieldComplexType();
                        this.fieldProcessor.setFieldControlType();
                    }
                    this.fieldProcessor.setBaseDataIdFields(virtualEntity);
                    if (!virtualEntity) {
                        this.fieldProcessor.setFieldComplexType();
                        this.fieldProcessor.setFieldControlType();
                    }
                    this.fieldProcessor.setZoneInfoIntoQueryField();
                    this.getView().showLoading(new LocaleString(ResManager.loadKDString((String)"\u6b63\u5728\u52a0\u8f7d...", (String)"ReportAnalyseObjectEdit_23", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0])));
                    try {
                        this.openDataPreview();
                        break;
                    }
                    finally {
                        this.getView().hideLoading();
                    }
                }
                this.getView().hideLoading();
                break;
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        if (HRStringUtils.equals((String)args.getOperateKey(), (String)"save")) {
            this.getView().hideLoading();
            this.stopDataExtract();
        } else if (HRStringUtils.equals((String)args.getOperateKey(), (String)"nextstep")) {
            this.afterNextStepOption(args);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        super.confirmCallBack(event);
        int result = event.getResult().getValue();
        if (HRStringUtils.equals((String)event.getCallBackId(), (String)"deleteMainEntity") || HRStringUtils.equals((String)event.getCallBackId(), (String)"deleteEntity")) {
            if (result == MessageBoxResult.Yes.getValue()) {
                this.entityProcessor.deleteEntityCustomEvent();
            }
            this.getPageCache().remove("delRefCalculateFieldNums");
        } else if (HRStringUtils.equals((String)event.getCallBackId(), (String)"changeDataSourceConfirm")) {
            this.dataProcessor.confirmChangeDataSource(result);
        }
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        Object returnData = event.getReturnData();
        if (returnData == null) {
            return;
        }
        if (HRStringUtils.equals((String)"hbp_setentityrelation", (String)event.getActionId())) {
            this.entityProcessor.updateEntityRelation(returnData);
        } else if (HRStringUtils.equals((String)"hbp_modifyfieldname", (String)event.getActionId())) {
            this.fieldProcessor.setFieldNewName(returnData);
        }
    }

    private void beforeSave(Tab tab, BeforeDoOperationEventArgs args, AbstractOperate op) {
        if (HRStringUtils.equals((String)tab.getCurrentTab(), (String)"definefield") && !this.dataProcessor.validateFieldDefineData("save")) {
            this.getView().hideLoading();
            args.setCancel(true);
            return;
        }
        if (HRStringUtils.equals((String)tab.getCurrentTab(), (String)"definefield")) {
            List allJoinEntity = this.commonProcessor.getAllJoinEntity();
            boolean virtualEntity = ((JoinEntityBo)allJoinEntity.get(0)).getVirtualEntity();
            if (!virtualEntity) {
                this.fieldProcessor.setFieldComplexType();
                this.fieldProcessor.setFieldControlType();
            }
            this.fieldProcessor.setBaseDataIdFields(virtualEntity);
            if (!virtualEntity) {
                this.fieldProcessor.setFieldComplexType();
                this.fieldProcessor.setFieldControlType();
            }
        }
        String joinEntities = this.getPageCache().get("joinEntities");
        String queryFields = this.getPageCache().get("queryFields");
        String entityRelations = this.getPageCache().get("entityRelations");
        String calFields = this.getPageCache().get("calculateFields");
        op.getOption().setVariableValue("joinEntities", joinEntities);
        op.getOption().setVariableValue("queryFields", queryFields);
        op.getOption().setVariableValue("entityRelations", entityRelations);
        op.getOption().setVariableValue("calculateFields", calFields);
        Object copyAnObjId = this.getView().getFormShowParameter().getCustomParam("copyId");
        if (copyAnObjId != null) {
            op.getOption().setVariableValue("copyId", String.valueOf(copyAnObjId));
        }
        List joinEntityBos = JSON.parseArray((String)joinEntities, JoinEntityBo.class);
        if ("true".equals(this.getView().getFormShowParameter().getCustomParam("isTemplatePage"))) {
            this.getModel().setValue("objecttype", (Object)"template");
        } else if (joinEntityBos.size() == 1 && ((JoinEntityBo)joinEntityBos.get(0)).getVirtualEntity()) {
            this.getModel().setValue("objecttype", (Object)"virtualentity");
        }
        this.getModel().setValue("datafilter", (Object)this.getHRFilter().getValue());
    }

    private void beforeLastStepOption(Tab tab) {
        String lastTab;
        if ("filterdata".equals(tab.getCurrentTab())) {
            this.getModel().setValue("datafilter", (Object)this.getHRFilter().getValue());
            List dataFilterRefFieldAlias = this.commonProcessor.getDataFilterRefFieldAlias();
            List pivotRefFieldAlias = this.commonProcessor.getPivotRefFieldAlias();
            List groupFieldRefFieldAlias = this.commonProcessor.getGroupFieldRefFieldAlias();
            dataFilterRefFieldAlias.addAll(groupFieldRefFieldAlias);
            dataFilterRefFieldAlias.addAll(pivotRefFieldAlias);
            CustomControl customcontrol = (CustomControl)this.getView().getControl("customcontrolap");
            HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
            dataMap.put("method", "updateFilterFieldAliasList");
            dataMap.put("filterFieldAliasList", dataFilterRefFieldAlias);
            customcontrol.setData((Object)dataMap);
        }
        if ((lastTab = this.formProcessor.getLastTab(tab)) != null) {
            tab.activeTab(lastTab);
        } else {
            lastTab = tab.getCurrentTab();
        }
        this.formProcessor.setVisibleForBtn(lastTab, this.getView());
    }

    private void beforeNextStepOption(Tab tab, BeforeDoOperationEventArgs args) {
        String nextTab = this.formProcessor.getNextTab(tab);
        if (nextTab != null) {
            if (nextTab.equals("filterdata") && !this.dataProcessor.validateFieldDefineData("nextstep")) {
                args.setCancel(true);
                this.getView().hideLoading();
                return;
            }
            if (nextTab.equals("filterdata")) {
                List allJoinEntity = this.commonProcessor.getAllJoinEntity();
                boolean virtualEntity = ((JoinEntityBo)allJoinEntity.get(0)).getVirtualEntity();
                if (!virtualEntity) {
                    this.fieldProcessor.setFieldComplexType();
                    this.fieldProcessor.setFieldControlType();
                }
                this.fieldProcessor.setBaseDataIdFields(virtualEntity);
                if (!virtualEntity) {
                    this.fieldProcessor.setFieldComplexType();
                    this.fieldProcessor.setFieldControlType();
                }
                this.fieldProcessor.setZoneInfoIntoQueryField();
            }
        }
    }

    private void afterNextStepOption(AfterDoOperationEventArgs args) {
        if (args.getOperationResult().isSuccess()) {
            Tab tab = (Tab)this.getView().getControl("tabap");
            String nextTab = this.formProcessor.getNextTab(tab);
            if (HRStringUtils.equals((String)nextTab, (String)"filterdata") && !this.dataProcessor.validateFieldDefineData("nextstep")) {
                this.getView().hideLoading();
                return;
            }
            if (nextTab != null) {
                tab.activeTab(nextTab);
            } else {
                nextTab = tab.getCurrentTab();
            }
            this.formProcessor.setVisibleForBtn(nextTab, this.getView());
            if (nextTab.equals("definefield") || nextTab.equals("filterdata")) {
                this.initProcessor.initDataFilter();
            }
            this.getView().hideLoading();
        } else {
            Tab tab = (Tab)this.getView().getControl("tabap");
            String nextTab = this.formProcessor.getNextTab(tab);
            if (HRStringUtils.equals((String)nextTab, (String)"definefield")) {
                this.getPageCache().remove("initCompleted");
                this.getPageCache().remove("allFieldTreeNodes");
            }
            this.getView().hideLoading();
        }
    }

    private void openDataPreview() {
        HashMap fieldMap;
        String calculateFieldsStr;
        String entityRelationsStr;
        String queryFieldsStr;
        List entityRelations = Collections.emptyList();
        List queryFields = Collections.emptyList();
        List calculateFields = Collections.emptyList();
        List joinEntities = Collections.emptyList();
        String joinEntitiesStr = this.getPageCache().get("joinEntities");
        if (HRStringUtils.isNotEmpty((String)joinEntitiesStr)) {
            joinEntities = JSON.parseArray((String)joinEntitiesStr, JoinEntityBo.class);
        }
        if (HRStringUtils.isNotEmpty((String)(queryFieldsStr = this.getPageCache().get("queryFields")))) {
            queryFields = JSON.parseArray((String)queryFieldsStr, QueryFieldBo.class);
        }
        if (HRStringUtils.isNotEmpty((String)(entityRelationsStr = this.getPageCache().get("entityRelations")))) {
            entityRelations = JSON.parseArray((String)entityRelationsStr, EntityRelationBo.class);
        }
        if (HRStringUtils.isNotEmpty((String)(calculateFieldsStr = this.getPageCache().get("calculateFields")))) {
            calculateFields = JSON.parseArray((String)calculateFieldsStr, CalculateFieldBo.class);
        }
        ArrayList qFilters = Lists.newArrayListWithCapacity((int)10);
        List selectedCalFields = calculateFields.stream().filter(CalculateFieldBo::getSelected).collect(Collectors.toList());
        List sortedCalFields = selectedCalFields.stream().sorted(Comparator.comparingInt(CalculateFieldBo::getOrder)).collect(Collectors.toList());
        HRComplexObjContext context = ReportComplexObjTransferUtil.transferToComplexObjContext((List)(joinEntities = this.entityProcessor.sortEntities(joinEntities)), (List)queryFields, (List)entityRelations, sortedCalFields, (List)qFilters);
        if (context != null) {
            ILocaleString name = (ILocaleString)this.getModel().getValue("name");
            context.setAlgoxJobKey(AlgoxJobKeyHelper.getPreAlgoxJobKeyByAnalyseObject((String)name.getLocaleValue()));
            context.setTransferField(false);
            this.commonProcessor.setVirtualEntityConfigForContext(context, joinEntities, queryFields, (List)qFilters);
            context.setCurrentUserDateFormat(ReportTimeZoneUtil.getUserDateFormat());
            context.setCurrentUserDateTimeFormat(ReportTimeZoneUtil.getUserDateTimeFormat());
        }
        ArrayList fieldMapList = Lists.newArrayListWithExpectedSize((int)queryFields.size());
        for (QueryFieldBo queryFieldBo : queryFields) {
            fieldMap = Maps.newHashMapWithExpectedSize((int)3);
            fieldMap.put("id", queryFieldBo.getFieldAlias());
            fieldMap.put("name", queryFieldBo.getFieldName().getLocaleValue());
            fieldMap.put("type", queryFieldBo.getValueType());
            fieldMapList.add(fieldMap);
        }
        for (CalculateFieldBo selectedCalField : selectedCalFields) {
            fieldMap = Maps.newHashMapWithExpectedSize((int)3);
            fieldMap.put("id", selectedCalField.getFieldNumber());
            fieldMap.put("name", selectedCalField.getFieldName().getLocaleValue());
            fieldMap.put("type", selectedCalField.getValueType());
            fieldMapList.add(fieldMap);
        }
        FormShowParameter dataPreviewShowParameter = DataPreviewService.getDataPreviewShowParameter((HRComplexObjContext)context, (List)fieldMapList);
        this.getView().showForm(dataPreviewShowParameter);
    }

    private void stopDataExtract() {
        long id = (Long)this.getModel().getValue("id");
        AnObjDataStoreTaskServiceHelper.stopSyn((Long)id);
    }
}
