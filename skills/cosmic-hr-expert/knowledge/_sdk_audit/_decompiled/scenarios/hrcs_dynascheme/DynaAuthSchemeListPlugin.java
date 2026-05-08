/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.constants.history.HisPageEnum
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dyna.DynaAuthSchemeServiceHelper
 *  org.apache.commons.lang3.tuple.Pair
 */
package kd.hr.hrcs.formplugin.web.perm.dyna;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.constants.history.HisPageEnum;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.bussiness.servicehelper.perm.dyna.DynaAuthSchemeServiceHelper;
import org.apache.commons.lang3.tuple.Pair;

@ExcludeFromJacocoGeneratedReport
public final class DynaAuthSchemeListPlugin
extends HRDataBaseList {
    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        String hisPage = (String)this.getView().getFormShowParameter().getCustomParam("hisPage");
        if (HisPageEnum.VERSION_LIST_PAGE.getPage().equals(hisPage)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"setadminrange"});
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        String operateKey = formOperate.getOperateKey();
        ListSelectedRowCollection listSelectedData = args.getListSelectedData();
        if (HRStringUtils.equals((String)"setadminrange", (String)operateKey) || HRStringUtils.equals((String)"assignrecord", (String)operateKey)) {
            if (listSelectedData.getPrimaryKeyValues().length > 1) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u6761\u65b9\u6848\u3002", (String)"DynaAuthSchemeListPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
            }
        } else if (HRStringUtils.equals((String)"copy", (String)operateKey)) {
            Object[] primaryKeyValues = listSelectedData.getPrimaryKeyValues();
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_dynascheme");
            DynamicObject scheme = serviceHelper.queryOriginalOne("id,name,sourcevid", primaryKeyValues[0]);
            String schemeName = ResManager.loadKDString((String)"%s-\u590d\u5236", (String)"DynaAuthSchemeListPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{scheme.getString("name")});
            this.getView().getFormShowParameter().getCustomParams().put("schemeName", schemeName);
            this.getView().getFormShowParameter().getCustomParams().put("sourceSchemeId", scheme.getLong("sourcevid") == 0L ? scheme.getLong("id") : scheme.getLong("sourcevid"));
        } else if (HRStringUtils.equals((String)"enable", (String)operateKey) || HRStringUtils.equals((String)"disable", (String)operateKey)) {
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_dynascheme");
            DynamicObject[] auditSchemes = serviceHelper.loadDynamicObjectArray(listSelectedData.getPrimaryKeyValues());
            Map<Long, Pair<Long, List<Long>>> sourceMap = this.getSchemeInfo(auditSchemes);
            this.getPageCache().put("sourceMap", SerializationUtils.toJsonString(sourceMap));
        } else if (HRStringUtils.equals((String)"confirmchange", (String)operateKey) || HRStringUtils.equals((String)"audit", (String)operateKey) || HRStringUtils.equals((String)"submit", (String)operateKey)) {
            formOperate.getOption().setVariableValue("list_op", "1");
        }
    }

    public void beforeShowBill(BeforeShowBillFormEvent evt) {
        Object pkId = evt.getParameter().getPkId();
        if (null == pkId) {
            return;
        }
        long boid = DynaAuthSchemeServiceHelper.querySchemeBoid((Object)pkId);
        Map customParams = this.getView().getFormShowParameter().getCustomParams();
        if (customParams.containsKey("schemeName")) {
            evt.getParameter().setCustomParam("schemeName", customParams.get("schemeName"));
        }
        if (customParams.containsKey("sourceSchemeId")) {
            evt.getParameter().setCustomParam("sourceSchemeId", customParams.get("sourceSchemeId"));
        }
        Set operationalSchemes = DynaAuthSchemeServiceHelper.queryOperationalSchemes();
        Object isCopy = evt.getParameter().getCustomParam("iscopy");
        if (!operationalSchemes.contains(boid) && null == isCopy) {
            evt.getParameter().setStatus(OperationStatus.VIEW);
        }
    }

    public void setFilter(SetFilterEvent evt) {
        QFilter idFilter = new QFilter("boid", "in", (Object)DynaAuthSchemeServiceHelper.queryViewableSchemes());
        evt.getQFilters().add(idFilter);
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        String operateKey = args.getOperateKey();
        OperationResult operationResult = args.getOperationResult();
        ListView listView = (ListView)this.getView();
        ListSelectedRow currentSelectedRowInfo = listView.getCurrentSelectedRowInfo();
        if (HRStringUtils.equals((String)operateKey, (String)"setadminrange") && operationResult.isSuccess()) {
            DynaAuthSchemeServiceHelper.showAdminRangeDetail((Object)currentSelectedRowInfo.getPrimaryKeyValue(), (IFormView)this.getView());
        } else if (HRStringUtils.equals((String)operateKey, (String)"assignrecord") && operationResult.isSuccess()) {
            ListSelectedRowCollection selectedRows = listView.getSelectedRows();
            Object[] primaryKeyValues = selectedRows.getPrimaryKeyValues();
            ListShowParameter listShowParameter = new ListShowParameter();
            listShowParameter.setBillFormId("hrcs_userrolerelat");
            listShowParameter.setFormId("bos_list");
            listShowParameter.setHasRight(true);
            listShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_dynascheme");
            DynamicObject scheme = serviceHelper.queryOriginalOne("number", primaryKeyValues[0]);
            String schemeNumber = scheme.getString("number");
            QFilter filter = new QFilter("sourcetype", "=", (Object)"4");
            listShowParameter.setCustomParam("schemeNumber", (Object)schemeNumber);
            listShowParameter.getListFilterParameter().getQFilters().add(filter);
            listShowParameter.setCaption(ResManager.loadKDString((String)"\u52a8\u6001\u6388\u6743\u65b9\u6848\u5206\u914d\u8bb0\u5f55", (String)"DynaAuthSchemeListPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            this.getView().showForm((FormShowParameter)listShowParameter);
        } else if (HRStringUtils.equals((String)"audithisconfirmchange", (String)operateKey) && operationResult.isSuccess()) {
            List successPkIds = operationResult.getSuccessPkIds();
            if (successPkIds.size() <= 0) {
                return;
            }
            HRBaseServiceHelper schemeHelper = new HRBaseServiceHelper("hrcs_dynascheme");
            DynamicObject scheme = schemeHelper.queryOriginalOne("boid", successPkIds.get(0));
            long boid = scheme.getLong("boid");
            DynaAuthSchemeServiceHelper.showChangeTips((IFormView)this.getView(), (long)boid, (IFormPlugin)this, (String)operateKey);
        } else if (HRStringUtils.equals((String)"delete", (String)operateKey)) {
            List successPkIds = operationResult.getSuccessPkIds();
            if (successPkIds.size() <= 0) {
                return;
            }
            QFilter[] idFilter = new QFilter[]{new QFilter("scheme", "in", (Object)successPkIds)};
            HRBaseServiceHelper rangeHelper = new HRBaseServiceHelper("hrcs_dynaschemerange");
            rangeHelper.deleteByFilter(idFilter);
            HRBaseServiceHelper orgHelper = new HRBaseServiceHelper("hrcs_dynaschorg");
            orgHelper.deleteByFilter(idFilter);
            HRBaseServiceHelper dimGrpHelper = new HRBaseServiceHelper("hrcs_dynaschdimgrp");
            dimGrpHelper.deleteByFilter(idFilter);
            HRBaseServiceHelper dataRuleHelper = new HRBaseServiceHelper("hrcs_dynaschdatarule");
            dataRuleHelper.deleteByFilter(idFilter);
            HRBaseServiceHelper filedHelper = new HRBaseServiceHelper("hrcs_dynaschfield");
            filedHelper.deleteByFilter(idFilter);
        } else if (HRStringUtils.equals((String)"enable", (String)operateKey) || HRStringUtils.equals((String)"disable", (String)operateKey)) {
            List successPkIds = operationResult.getSuccessPkIds();
            if (successPkIds.size() <= 0) {
                return;
            }
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_dynascheme");
            DynamicObject[] auditSchemes = serviceHelper.loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", (Object)successPkIds), new QFilter("status", "=", (Object)"C")});
            if (auditSchemes.length == 0) {
                return;
            }
        }
    }

    private Map<Long, Pair<Long, List<Long>>> getSchemeInfo(DynamicObject[] auditSchemes) {
        List<Long> sourceVids = Arrays.stream(auditSchemes).map(it -> it.getLong("sourcevid")).collect(Collectors.toList());
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_dynascheme");
        DynamicObject[] sourceDys = serviceHelper.loadDynamicObjectArray((Object[])sourceVids.toArray(new Long[0]));
        HashMap entryIdMap = Maps.newHashMapWithExpectedSize((int)sourceDys.length);
        for (DynamicObject sourceDy : sourceDys) {
            long id = sourceDy.getLong("id");
            DynamicObjectCollection roleEntry = sourceDy.getDynamicObjectCollection("roleentry");
            List entryIds = roleEntry.stream().map(it -> it.getLong("id")).collect(Collectors.toList());
            entryIdMap.put(id, entryIds);
        }
        HashMap sourceMap = Maps.newHashMapWithExpectedSize((int)auditSchemes.length);
        for (DynamicObject auditScheme : auditSchemes) {
            long id = auditScheme.getLong("id");
            long sourceVid = auditScheme.getLong("sourcevid");
            sourceMap.put(id, Pair.of((Object)sourceVid, entryIdMap.get(sourceVid)));
        }
        return sourceMap;
    }
}
