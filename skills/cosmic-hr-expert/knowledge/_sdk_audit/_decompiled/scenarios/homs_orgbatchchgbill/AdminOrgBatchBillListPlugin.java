/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.BeforePackageDataEvent
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.list.column.TextColumnDesc
 *  kd.bos.form.FloatingDirection
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.OpenStyle
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerSearchClickArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.list.BillList
 *  kd.bos.list.IListColumn
 *  kd.bos.list.IListView
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.list.events.ListRowClickEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.haos.business.util.OrgPermHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatchChgHelper
 *  kd.hr.homs.business.domain.batchbill.service.OrgBatchBreakupService
 *  kd.hr.homs.common.constants.batchchg.OrgBatchChgBillConstants
 */
package kd.hr.homs.formplugin.web.orgbatch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.BeforePackageDataEvent;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.list.column.TextColumnDesc;
import kd.bos.form.FloatingDirection;
import kd.bos.form.FormShowParameter;
import kd.bos.form.OpenStyle;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerSearchClickArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.IListColumn;
import kd.bos.list.IListView;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.list.events.ListRowClickEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.haos.business.util.OrgPermHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatchChgHelper;
import kd.hr.homs.business.domain.batchbill.service.OrgBatchBreakupService;
import kd.hr.homs.common.constants.batchchg.OrgBatchChgBillConstants;

public final class AdminOrgBatchBillListPlugin
extends HRDataBaseList {
    private static final Log logger = LogFactory.getLog(AdminOrgBatchBillListPlugin.class);
    private static final Map<String, Long> CHANGE_TYPE_ID_NAME = Collections.unmodifiableMap(new /* Unavailable Anonymous Inner Class!! */);
    private Map<Long, Map<String, Integer>> map;

    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        setFilterEvent.setOrderBy("createtime desc,billno desc");
    }

    public void beforePackageData(BeforePackageDataEvent event) {
        List idList = event.getPageData().stream().map(pageDate -> pageDate.getLong("id")).collect(Collectors.toList());
        logger.info(String.format(Locale.ROOT, "beforePackageData idList : %s", idList));
        HRBaseServiceHelper orgChgBillEntryHelper = new HRBaseServiceHelper("homs_batchorgentity");
        QFilter idFilter = new QFilter("billid", "in", idList);
        DynamicObject[] orgChgBillEntryDyns = orgChgBillEntryHelper.query("id, billid, changetype , aftermergeorgid, beforesplitorgid", new QFilter[]{idFilter});
        Map<Long, List<DynamicObject>> billId2DynListMap = Arrays.stream(orgChgBillEntryDyns).collect(Collectors.groupingBy(dyn -> dyn.getLong("billid")));
        Set<Map.Entry<Long, List<DynamicObject>>> billIdDynListMapEntrySet = billId2DynListMap.entrySet();
        this.map = Maps.newHashMapWithExpectedSize((int)billIdDynListMapEntrySet.size());
        for (Map.Entry<Long, List<DynamicObject>> longListEntry : billIdDynListMapEntrySet) {
            List<DynamicObject> entryDynList = longListEntry.getValue();
            Map<Long, List<DynamicObject>> changeTypeId2EntryDynListMap = entryDynList.stream().collect(Collectors.groupingBy(entryDyn -> entryDyn.getLong("changetype.id")));
            HashMap field2CountMap = Maps.newHashMapWithExpectedSize((int)changeTypeId2EntryDynListMap.size());
            List<DynamicObject> addEntryDynList = changeTypeId2EntryDynListMap.get(OrgBatchChgBillConstants.CHANGE_TYPE_ADD);
            List<DynamicObject> parentEntryDynList = changeTypeId2EntryDynListMap.get(OrgBatchChgBillConstants.CHANGE_TYPE_PARENT);
            List<DynamicObject> infoEntryDynList = changeTypeId2EntryDynListMap.get(OrgBatchChgBillConstants.CHANGE_TYPE_INFO);
            List<DynamicObject> disableEntryDynList = changeTypeId2EntryDynListMap.get(OrgBatchChgBillConstants.CHANGE_TYPE_DISABLE);
            List<DynamicObject> mergeEntryDynList = changeTypeId2EntryDynListMap.get(OrgBatchChgBillConstants.CHANGE_TYPE_MERGE);
            List<DynamicObject> splitEntryDynList = changeTypeId2EntryDynListMap.get(OrgBatchChgBillConstants.CHANGE_TYPE_SPLIT);
            this.putField2CountMap(field2CountMap, addEntryDynList, "addcount");
            this.putField2CountMap(field2CountMap, parentEntryDynList, "parentcount");
            this.putField2CountMap(field2CountMap, infoEntryDynList, "infocount");
            this.putField2CountMap(field2CountMap, disableEntryDynList, "disablecount");
            this.putField2CountMap(field2CountMap, mergeEntryDynList, "mergecount");
            this.putField2CountMap(field2CountMap, splitEntryDynList, "splitcount");
            this.map.put(longListEntry.getKey(), field2CountMap);
        }
    }

    public void packageData(PackageDataEvent event) {
        if (event.getSource() instanceof TextColumnDesc) {
            String key = ((TextColumnDesc)event.getSource()).getKey();
            Long id = event.getRowData().getLong("id");
            if (CHANGE_TYPE_ID_NAME.containsKey(key)) {
                Map<String, Integer> field2CountMap = this.map.get(id);
                if (null == field2CountMap) {
                    event.setFormatValue((Object)0);
                    logger.info(MessageFormat.format("packageData field2CountMap null id:{0}", id));
                } else {
                    Integer count = field2CountMap.get(key);
                    event.setFormatValue((Object)count);
                }
            }
        }
    }

    private void putField2CountMap(Map<String, Integer> field2CountMap, List<DynamicObject> entryDynList, String fieldKey) {
        if (!CollectionUtils.isEmpty(entryDynList)) {
            if ("mergecount".equals(fieldKey)) {
                field2CountMap.put(fieldKey, Integer.valueOf(entryDynList.stream().map(dy -> dy.getLong("aftermergeorgid")).distinct().count() + ""));
            } else if ("splitcount".equals(fieldKey)) {
                field2CountMap.put(fieldKey, Integer.valueOf(entryDynList.stream().map(dy -> dy.getLong("beforesplitorgid")).distinct().count() + ""));
            } else {
                field2CountMap.put(fieldKey, entryDynList.size());
            }
        } else {
            field2CountMap.put(fieldKey, 0);
        }
    }

    public void listRowClick(ListRowClickEvent evt) {
        BillList billList = (BillList)evt.getSource();
        String focusField = billList.getEntryState().getFocusField();
        ListSelectedRow currentListSelectedRow = evt.getCurrentListSelectedRow();
        if (CHANGE_TYPE_ID_NAME.containsKey(focusField) && currentListSelectedRow != null) {
            DynamicObject[] entityInfoArr = this.getBillEntityByBillId((Long)currentListSelectedRow.getPrimaryKeyValue(), CHANGE_TYPE_ID_NAME.get(focusField));
            if (entityInfoArr == null || entityInfoArr.length == 0) {
                return;
            }
            FormShowParameter formShowParameter = this.getFormShowParameter();
            ArrayList result = Lists.newArrayListWithExpectedSize((int)entityInfoArr.length);
            if ("mergecount".equals(focusField) || "splitcount".equals(focusField)) {
                this.setFormShowParameter(billList, entityInfoArr, formShowParameter, focusField, result);
            } else {
                formShowParameter.setCaption(this.getColumnName(billList, entityInfoArr.length));
                for (DynamicObject dynamicObject : entityInfoArr) {
                    HashMap resultMap = Maps.newHashMapWithExpectedSize((int)16);
                    resultMap.put("number", dynamicObject.getString("adminorg.number"));
                    resultMap.put("name", dynamicObject.getString("adminorg.name"));
                    result.add(resultMap);
                }
            }
            formShowParameter.setCustomParam("nameAndNumber", (Object)result);
            this.getView().showForm(formShowParameter);
        }
    }

    private void setFormShowParameter(BillList billList, DynamicObject[] entityInfoArr, FormShowParameter formShowParameter, String countName, List<Map<String, String>> result) {
        Map<String, List<DynamicObject>> mergeCount = Arrays.stream(entityInfoArr).collect(Collectors.groupingBy(dy -> dy.getLong("changetype") + "_" + dy.getLong("mergecount".equals(countName) ? "aftermergeorgid" : "beforesplitorgid")));
        formShowParameter.setFormId("mergecount".equals(countName) ? "homs_orgbatchbillmergetip" : "homs_orgbatchbillsplittip");
        formShowParameter.setCaption(this.getColumnName(billList, mergeCount.size()));
        for (Map.Entry<String, List<DynamicObject>> entry : mergeCount.entrySet()) {
            List<DynamicObject> value = entry.getValue();
            List afterMergeIds = value.stream().filter(dyn -> dyn.getLong("mergecount".equals(countName) ? "beforemergeorgid" : "aftersplitorgid") != 0L).collect(Collectors.toList());
            List beforeMergeIds = value.stream().filter(dyn -> dyn.getLong("mergecount".equals(countName) ? "beforemergeorgid" : "aftersplitorgid") == 0L).collect(Collectors.toList());
            HashMap resultMap = Maps.newHashMapWithExpectedSize((int)16);
            resultMap.put("mergecount".equals(countName) ? "number" : "name", String.join((CharSequence)"; ", afterMergeIds.stream().map(dyn -> dyn.getString("adminorg.name")).collect(Collectors.toList())));
            resultMap.put("mergecount".equals(countName) ? "name" : "number", ((DynamicObject)beforeMergeIds.get(0)).getString("adminorg.name"));
            result.add(resultMap);
        }
    }

    private FormShowParameter getFormShowParameter() {
        FormShowParameter formShowParameter = new FormShowParameter();
        OpenStyle openStyle = formShowParameter.getOpenStyle();
        openStyle.setShowType(ShowType.Modal);
        openStyle.setFloatingDirection(FloatingDirection.RightCenter);
        openStyle.setTargetKey("btntreefilter");
        StyleCss cssValue = new StyleCss();
        cssValue.setWidth("460px");
        cssValue.setHeight("360px");
        openStyle.setInlineStyleCss(cssValue);
        formShowParameter.setFormId("homs_orgbatchbilltips");
        return formShowParameter;
    }

    private String getColumnName(BillList billList, int length) {
        String focusField = billList.getEntryState().getFocusField();
        List showListColumns = billList.getShowListColumns();
        String columnName = "";
        for (IListColumn column : showListColumns) {
            if (!column.getListFieldKey().equals(focusField)) continue;
            columnName = column.getCaption().getLocaleValue();
            break;
        }
        if (!HRStringUtils.isEmpty((String)columnName)) {
            columnName = columnName + "\uff08" + length + "\uff09";
        }
        return columnName;
    }

    private DynamicObject[] getBillEntityByBillId(Long billId, Long changeTypeId) {
        HRBaseServiceHelper billServiceHelper = new HRBaseServiceHelper("homs_batchorgentity");
        QFilter billIdFilter = new QFilter("billid", "=", (Object)billId);
        billIdFilter.and("changetype", "=", (Object)changeTypeId);
        return billServiceHelper.queryOriginalArray("adminorg.name, adminorg.number,beforemergeorgid,aftermergeorgid,beforesplitorgid,aftersplitorgid,changetype", new QFilter[]{billIdFilter});
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        String fieldName = args.getFieldName();
        if ("billno".equals(fieldName)) {
            this.getView().getPageCache().put("isFromBillClick", "true");
        }
        if (CHANGE_TYPE_ID_NAME.containsKey(fieldName)) {
            args.setCancel(true);
        }
    }

    public void beforeShowBill(BeforeShowBillFormEvent e) {
        super.beforeShowBill(e);
        String orgId = this.getView().getPageCache().get("businessUnit");
        if (Objects.isNull(orgId)) {
            if (this.getView().getPageCache().get("isFromBillClick") != null) {
                this.getView().getPageCache().remove("isFromBillClick");
                e.getParameter().setCustomParam("isFromBillClick", (Object)"true");
            } else {
                HasPermOrgResult hrPermOrg = OrgPermHelper.getHRPermOrg((boolean)false);
                List hasPermOrgs = hrPermOrg.getHasPermOrgs();
                if (!Objects.isNull(hasPermOrgs) && hasPermOrgs.size() != 0) {
                    if (hasPermOrgs.contains(RequestContext.get().getOrgId())) {
                        e.getParameter().setCustomParam("businessUnit", (Object)String.valueOf(RequestContext.get().getOrgId()));
                    } else {
                        e.getParameter().setCustomParam("businessUnit", (Object)String.valueOf(hasPermOrgs.get(0)));
                    }
                }
            }
        } else {
            e.getParameter().setCustomParam("businessUnit", (Object)orgId);
        }
        e.getParameter().setCustomParam("fromList", (Object)"true");
    }

    public void filterContainerAfterSearchClick(FilterContainerSearchClickArgs args) {
        Object filterValue = args.getFilterValue("org.id");
        if (!Objects.isNull(filterValue) && filterValue instanceof Long) {
            this.getView().getPageCache().put("businessUnit", filterValue.toString());
        } else {
            this.getView().getPageCache().remove("businessUnit");
        }
        super.filterContainerAfterSearchClick(args);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        String operateKey = formOperate.getOperateKey();
        IListView iListView = (IListView)this.getView();
        ListSelectedRowCollection selectedRows = iListView.getSelectedRows();
        if (selectedRows.size() > 0 && HRStringUtils.equals((String)operateKey, (String)"breakup")) {
            if (formOperate.getOption().containsVariable("afterconfirm")) {
                return;
            }
            if (selectedRows.size() > 1) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e00\u6b21\u53ea\u80fd\u64cd\u4f5c\u4e00\u6761\u6570\u636e\u3002", (String)"AdminorgBatchBillPlugin_67", (String)"odc-homs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
            } else {
                OrgBatchBreakupService.callValidatorBeforeDoOperation((BeforeDoOperationEventArgs)args, (Object)selectedRows.getPrimaryKeyValues()[0], (AbstractFormPlugin)this);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        if (args.getOperationResult() != null && !args.getOperationResult().isSuccess()) {
            return;
        }
        String operateKey = args.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"breakup")) {
            this.getView().invokeOperation("refresh");
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        super.confirmCallBack(event);
        IListView iListView = (IListView)this.getView();
        ListSelectedRowCollection selectedRows = iListView.getSelectedRows();
        if (event.getCallBackId().equals("del_row") && "Yes".equals(event.getResultValue())) {
            if (!this.checkUsePermission("4715e1f1000000ac")) {
                this.getView().showMessage(ResManager.loadKDString((String)"\u60a8\u7684\u201c\u7ec4\u7ec7\u8c03\u6574\u7533\u8bf7\u5217\u8868\u201d\u7684\u5220\u9664\u6743\u9650\u5df2\u53d1\u751f\u53d8\u66f4\uff0c\u65e0\u6cd5\u7ee7\u7eed\u64cd\u4f5c\uff0c\u8bf7\u91cd\u65b0\u6253\u5f00\u9875\u9762\u3002", (String)"AdminorgBatchBillPlugin_35", (String)"odc-homs-formplugin", (Object[])new Object[0]));
                return;
            }
            HRBaseServiceHelper orgChgBillEntryHelper = new HRBaseServiceHelper("homs_batchorgentity");
            QFilter billIdFilter = new QFilter("billid", "in", (Object)selectedRows.getPrimaryKeyValues());
            DynamicObject[] orgChgBillEntryDyns = orgChgBillEntryHelper.query("id, number, changetype,adminorg", new QFilter[]{billIdFilter});
            List addOrgEntryIdList = Arrays.stream(orgChgBillEntryDyns).filter(dyn -> dyn.getLong("changetype.id") == OrgBatchChgBillConstants.CHANGE_TYPE_ADD.longValue()).map(dyn -> dyn.getLong("adminorg.id")).collect(Collectors.toList());
            AdminOrgBatchChgHelper.delAddMaster(addOrgEntryIdList);
            HRBaseServiceHelper billHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
            billHelper.delete(selectedRows.getPrimaryKeyValues());
            this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f", (String)"AdminorgBatchBillPlugin_19", (String)"odc-homs-formplugin", (Object[])new Object[0]));
            this.getView().invokeOperation("refresh");
            this.getView().invokeOperation("delete_nothing");
        } else if (event.getCallBackId().equals("discard_row") && "Yes".equals(event.getResultValue())) {
            DynamicObject[] dynamicObjectArr;
            if (!this.checkUsePermission("0=KX5+R7YTRT")) {
                this.getView().showMessage(ResManager.loadKDString((String)"\u60a8\u7684\u201c\u7ec4\u7ec7\u8c03\u6574\u7533\u8bf7\u5217\u8868\u201d\u7684\u5e9f\u5f03\u6743\u9650\u5df2\u53d1\u751f\u53d8\u66f4\uff0c\u65e0\u6cd5\u7ee7\u7eed\u64cd\u4f5c\uff0c\u8bf7\u91cd\u65b0\u6253\u5f00\u9875\u9762\u3002", (String)"AdminorgBatchBillPlugin_36", (String)"odc-homs-formplugin", (Object[])new Object[0]));
                return;
            }
            HRBaseServiceHelper billHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
            for (DynamicObject billDy : dynamicObjectArr = billHelper.query("billstatus", new QFilter("id", "in", (Object)selectedRows.getPrimaryKeyValues()).toArray())) {
                billDy.set("billstatus", (Object)"F");
            }
            billHelper.updateDatas(dynamicObjectArr);
            this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5e9f\u5f03\u6210\u529f", (String)"AdminorgBatchBillPlugin_27", (String)"odc-homs-formplugin", (Object[])new Object[0]));
            this.getView().invokeOperation("refresh");
            this.getView().invokeOperation("discard_nothing");
        }
    }

    private boolean checkUsePermission(String permItemId) {
        PermissionServiceHelper permissionServiceHelper = new PermissionServiceHelper();
        Long userId = RequestContext.get().getCurrUserId();
        boolean checkPermission = PermissionServiceHelper.checkPermission((Long)userId, (String)this.getView().getFormShowParameter().getAppId(), (String)"homs_orgbatchchgbill", (String)permItemId);
        return checkPermission;
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        String actionId = closedCallBackEvent.getActionId();
        if (HRStringUtils.equals((String)actionId, (String)"breakup")) {
            Object returnData = closedCallBackEvent.getReturnData();
            if (returnData == null) {
                return;
            }
            OperateOption option = OperateOption.create();
            option.setVariableValue("returnData", SerializationUtils.toJsonString((Object)returnData));
            option.setVariableValue("afterconfirm", "true");
            this.getView().invokeOperation(actionId, option);
        }
    }
}
