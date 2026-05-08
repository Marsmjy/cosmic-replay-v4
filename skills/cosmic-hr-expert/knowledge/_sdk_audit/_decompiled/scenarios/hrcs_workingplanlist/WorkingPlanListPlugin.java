/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.Row
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge
 *  kd.bos.dataentity.utils.ArrayUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.DBRoute
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.filter.CommonFilterColumn
 *  kd.bos.filter.FilterColumn
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.BillList
 *  kd.bos.list.IListView
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.plugin.AbstractListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.hr.hbp.business.log.ModifyDirtyManager
 *  kd.hr.hbp.business.service.operatelog.OperateLogService
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.model.FormModel
 *  kd.hr.hbp.common.util.HRDBUtil
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRShowPageUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.calendar.WorkingPlanServiceHelper
 */
package kd.hr.hrcs.formplugin.web.calendar;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge;
import kd.bos.dataentity.utils.ArrayUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.DBRoute;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.filter.CommonFilterColumn;
import kd.bos.filter.FilterColumn;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.IListView;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.hbp.business.log.ModifyDirtyManager;
import kd.hr.hbp.business.service.operatelog.OperateLogService;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.model.FormModel;
import kd.hr.hbp.common.util.HRDBUtil;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRShowPageUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.calendar.WorkingPlanServiceHelper;

public final class WorkingPlanListPlugin
extends AbstractListPlugin {
    private static final Log LOGGER = LogFactory.getLog(WorkingPlanListPlugin.class);
    private static final String CACHE_KEY = "WorkingPlanListPlugin";
    private static final Map<Object, ModifyDirtyManager> modifyDirtyManagerMap = Maps.newHashMapWithExpectedSize((int)16);

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        if (!(args.getSource() instanceof FormOperate)) {
            return;
        }
        FormOperate formOp = (FormOperate)args.getSource();
        String operateKey = formOp.getOperateKey();
        String enable = null;
        switch (operateKey) {
            case "new": {
                this.openNewWorkingPlanPage(args);
                break;
            }
            case "delete": {
                this.validateDeleteWorkingPlan(args);
                break;
            }
            case "copy": {
                this.viewLog(args);
                break;
            }
            case "preview": {
                this.openPreviewForm(args);
                break;
            }
            case "enable": {
                enable = "1";
                break;
            }
            case "disable": {
                enable = "0";
                break;
            }
        }
        if (enable != null) {
            Map workingPlanMap = BusinessDataServiceHelper.loadFromCache((Object[])args.getListSelectedData().getPrimaryKeyValues(), (String)"working_plan");
            for (DynamicObject dyn : workingPlanMap.values()) {
                dyn.set("enable", (Object)enable);
                ModifyDirtyManager modifyDirtyManager = new ModifyDirtyManager();
                modifyDirtyManager.init(dyn.getDynamicObjectType(), new DynamicObject[]{dyn}, operateKey);
                modifyDirtyManagerMap.put(CACHE_KEY + dyn.getLong("id"), modifyDirtyManager);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        String operateKey = afterDoOperationEventArgs.getOperateKey();
        if ("delete".equals(operateKey)) {
            List successPkIds = afterDoOperationEventArgs.getOperationResult().getSuccessPkIds();
            long[] ids = successPkIds.stream().mapToLong(idStr -> (Long)idStr).toArray();
            WorkingPlanServiceHelper.deleteScopeObj((long[])ids);
            MultiLangEnumBridge deleteName = new MultiLangEnumBridge("\u5220\u9664", "HRRoleListPlugin_10", "hrmp-hrcs-formplugin");
            MultiLangEnumBridge operateDesc = new MultiLangEnumBridge("\u5220\u9664\u6210\u529f\u3002", "HRRoleListPlugin_11", "hrmp-hrcs-formplugin");
            OperateLogService.commonWriteLog((String)"15NPDX/GJFOO", (String)"hrcs_workingplanlist", (String)operateKey, (MultiLangEnumBridge)deleteName, (MultiLangEnumBridge)operateDesc, (String[])new String[0]);
        } else if ("enable".equals(operateKey) || "disable".equals(operateKey)) {
            List successPkIds = afterDoOperationEventArgs.getOperationResult().getSuccessPkIds();
            Map workingPlanMap = BusinessDataServiceHelper.loadFromCache((Object[])successPkIds.toArray(), (String)"working_plan");
            workingPlanMap.forEach((key, value) -> {
                ModifyDirtyManager modifyDirtyManager = modifyDirtyManagerMap.get(CACHE_KEY + key);
                modifyDirtyManager.batchInsertLog(new DynamicObject[]{value});
                modifyDirtyManagerMap.remove(CACHE_KEY + key);
            });
        }
    }

    public void setFilter(SetFilterEvent event) {
        super.setFilter(event);
        List dataPermQFilters = event.getDataPermQFilters();
        ArrayList newDataPermQFilters = Lists.newArrayList((Iterable)dataPermQFilters);
        newDataPermQFilters.removeIf(next -> {
            if (next != null) {
                return "countryid".equals(next.getProperty());
            }
            return false;
        });
        event.setDataPermQFilters((List)newDataPermQFilters);
        event.setOrderBy("modifytime desc");
        List qFilters = event.getQFilters();
        qFilters.add(new QFilter("countryid", "!=", (Object)0L));
    }

    private void openNewWorkingPlanPage(BeforeDoOperationEventArgs args) {
        FormModel formModel = new FormModel("hrcs_workingplandy", null, "1");
        formModel.setCallBack(Boolean.TRUE);
        HRShowPageUtils.showPage((FormModel)formModel, (AbstractFormPlugin)this);
        this.getView().getPageCache().put("closeCallBackForm", formModel.getFormId());
        args.setCancel(true);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        String openFormId = this.getView().getPageCache().get("closeCallBackForm");
        if (HRStringUtils.isNotEmpty((String)openFormId)) {
            this.getView().getPageCache().remove("closeCallBackForm");
        }
        if (closedCallBackEvent.getActionId().equals(openFormId)) {
            this.getView().invokeOperation("refresh");
        }
    }

    private void validateDeleteWorkingPlan(BeforeDoOperationEventArgs args) {
        IListView iListView = (IListView)this.getView();
        ListSelectedRowCollection roleList = iListView.getSelectedRows();
        MultiLangEnumBridge deleteName = new MultiLangEnumBridge("\u5220\u9664", "HRRoleListPlugin_10", "hrmp-hrcs-formplugin");
        HashSet wpIdSet = new HashSet(16);
        roleList.forEach(row -> {
            Long roleId = (Long)row.getPrimaryKeyValue();
            wpIdSet.add(roleId);
        });
        Long[] ids = wpIdSet.toArray(new Long[0]);
        Object[] wps = WorkingPlanServiceHelper.loadWorkingPlanObj((Long[])ids);
        if (ArrayUtils.isEmpty((Object[])wps)) {
            MultiLangEnumBridge errMsg = new MultiLangEnumBridge("\u6570\u636e\u4e0d\u5b58\u5728\u3002", "HRWorkingPlanListPlugin_0", "hrmp-hrcs-formplugin");
            this.getView().showErrorNotification(errMsg.loadKDString());
            args.setCancel(true);
            OperateLogService.commonWriteLog((String)"15NPDX/GJFOO", (String)"hrcs_workingplanlist", (String)"delete", (MultiLangEnumBridge)deleteName, (MultiLangEnumBridge)errMsg, (String[])new String[0]);
            return;
        }
        for (Object wp : wps) {
            Optional<ListSelectedRow> first = args.getListSelectedData().stream().filter(arg_0 -> WorkingPlanListPlugin.lambda$validateDeleteWorkingPlan$4((DynamicObject)wp, arg_0)).findFirst();
            if (first.isPresent()) {
                ListSelectedRow listSelectedRow = first.get();
                listSelectedRow.setNumber(wp.getString("number"));
            }
            if (wp.getBoolean("enable")) continue;
            MultiLangEnumBridge description = new MultiLangEnumBridge("\u6570\u636e\u201c%s\u201d\u4e3a\u7981\u7528\u6570\u636e\uff0c\u65e0\u6cd5\u5220\u9664\u3002", "HRWorkingPlanListPlugin_1", "hrmp-hrcs-formplugin");
            String errMsg = String.format(Locale.ROOT, description.loadKDString(), wp.getString("name"));
            this.getView().showErrorNotification(errMsg);
            args.setCancel(true);
            OperateLogService.commonWriteLog((String)"15NPDX/GJFOO", (String)"hrcs_workingplanlist", (String)"delete", (MultiLangEnumBridge)deleteName, (MultiLangEnumBridge)description, (String[])new String[]{wp.getString("name")});
            return;
        }
        this.validate(args);
        iListView.refresh();
    }

    private void validate(BeforeDoOperationEventArgs args) {
        ListSelectedRowCollection listSelectedData = args.getListSelectedData();
        int total = listSelectedData.size();
        HashSet wpIdSet = Sets.newHashSetWithExpectedSize((int)total);
        StringBuilder idCount = new StringBuilder();
        for (ListSelectedRow row : listSelectedData) {
            wpIdSet.add((Long)row.getPrimaryKeyValue());
            idCount.append("?,");
        }
        if (wpIdSet.size() > 0) {
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("bos_objecttyperef");
            DynamicObject[] results = serviceHelper.query("id,objecttypeid,frefobjecttypeid,tablename,fieldname", new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)"hrcs_workingplanquery")});
            HashMap refMap = Maps.newHashMapWithExpectedSize((int)total);
            for (DynamicObject result : results) {
                DataSet dataSet;
                String tableName = result.getString("tablename");
                String fieldName = result.getString("fieldname");
                if (StringUtils.isEmpty((CharSequence)tableName) || StringUtils.isEmpty((CharSequence)fieldName)) continue;
                String sql = "SELECT " + fieldName + " FROM " + tableName + " WHERE " + fieldName + " IN (" + idCount.substring(0, idCount.length() - 1) + ')';
                MainEntityType mainEntityType = MetadataServiceHelper.getDataEntityType((String)result.getString("objecttypeid"));
                try {
                    dataSet = HRDBUtil.queryDataSet((String)("WorkingPlanDeleteOp." + tableName), (DBRoute)new DBRoute(mainEntityType.getDBRouteKey()), (String)sql, (Object[])wpIdSet.toArray());
                }
                catch (Exception ex) {
                    LOGGER.error("WorkingPlanListPlugin_delete_fail:" + ex.getMessage());
                    continue;
                }
                while (dataSet.hasNext()) {
                    Row next = dataSet.next();
                    Long wpId = next.getLong(fieldName);
                    if (refMap.get(wpId) != null) continue;
                    DataEntityPropertyCollection properties = mainEntityType.getProperties();
                    String field = "";
                    for (IDataEntityProperty property : properties) {
                        if (!Objects.equals(property.getAlias(), fieldName)) continue;
                        field = property.getDisplayName().getLocaleValue();
                        break;
                    }
                    HashMap entityMap = Maps.newHashMapWithExpectedSize((int)16);
                    entityMap.put("entity", mainEntityType.getDisplayName().getLocaleValue());
                    entityMap.put("field", field);
                    refMap.put(wpId, entityMap);
                }
            }
            if (refMap.size() > 0) {
                if (total == 1) {
                    Map entityMap = (Map)refMap.get(listSelectedData.get(0).getPrimaryKeyValue());
                    MultiLangEnumBridge description = new MultiLangEnumBridge("\u5220\u9664\u5931\u8d25\uff0c\u201c%2$s\u201d\u7684\u201c%3$s\u201d\u5b57\u6bb5\u5f15\u7528\u4e86\u6b64\u6570\u636e\u3002", "WorkingPlanListPlugin_0", "hrmp-hrcs-opplugin");
                    String errMsg = String.format(description.loadKDString(), listSelectedData.get(0).getNumber(), entityMap.get("entity"), entityMap.get("field"));
                    this.getView().showErrorNotification(errMsg);
                    args.setCancel(true);
                    MultiLangEnumBridge deleteName = new MultiLangEnumBridge("\u5220\u9664", "HRRoleListPlugin_10", "hrmp-hrcs-formplugin");
                    OperateLogService.commonWriteLog((String)"15NPDX/GJFOO", (String)"hrcs_workingplanlist", (String)"delete", (MultiLangEnumBridge)deleteName, (MultiLangEnumBridge)description, (String[])new String[]{listSelectedData.get(0).getNumber(), (String)entityMap.get("entity"), (String)entityMap.get("field")});
                } else {
                    Iterator iterator = listSelectedData.iterator();
                    StringBuilder errMsgBuilder = new StringBuilder();
                    while (iterator.hasNext()) {
                        ListSelectedRow row = (ListSelectedRow)iterator.next();
                        Long id = (Long)row.getPrimaryKeyValue();
                        Map entityMap = (Map)refMap.get(id);
                        if (entityMap == null) continue;
                        String multiErrorMsg = ResManager.loadKDString((String)"\u5220\u9664\u5931\u8d25\uff0c\u201c%2$s\u201d\u7684\u201c%3$s\u201d\u5b57\u6bb5\u5f15\u7528\u4e86\u6b64\u6570\u636e\u3002", (String)"WorkingPlanListPlugin_0", (String)"hrmp-hrcs-opplugin", (Object[])new Object[]{row.getNumber(), entityMap.get("entity"), entityMap.get("field")});
                        errMsgBuilder.append(multiErrorMsg).append("\n");
                        iterator.remove();
                    }
                    if (errMsgBuilder.length() > 0) {
                        String msg = ResManager.loadKDString((String)"\u5171%1$s\u6761\u5355\u636e\uff0c\u5220\u9664\u6210\u529f%2$s\u6761\uff0c\u5931\u8d25%3$s\u6761\u3002", (String)"WorkingPlanListPlugin_1", (String)"hrmp-hrcs-opplugin", (Object[])new Object[]{total, total - refMap.size(), refMap.size()});
                        this.getView().showConfirm(msg, errMsgBuilder.toString(), MessageBoxOptions.OK, ConfirmTypes.Fail, null);
                        if (listSelectedData.size() == 0) {
                            args.setCancel(true);
                        }
                    }
                }
            }
        }
    }

    public void filterContainerInit(FilterContainerInitArgs args) {
        List listFilterColumns = args.getFilterContainerInitEvent().getCommonFilterColumns();
        for (FilterColumn listFilter : listFilterColumns) {
            CommonFilterColumn commFilter = (CommonFilterColumn)listFilter;
            String fieldName = commFilter.getFieldName();
            if (!HRStringUtils.equals((String)"enable", (String)fieldName)) continue;
            commFilter.setDefaultValue("1");
        }
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        ListShowParameter listShowParameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (!listShowParameter.isLookUp()) {
            args.setCancel(true);
        }
        BillList source = (BillList)args.getHyperLinkClickEvent().getSource();
        ListSelectedRow currentSelectedRowInfo = source.getCurrentSelectedRowInfo();
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_workingplandy");
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setCustomParam("id", currentSelectedRowInfo.getPrimaryKeyValue());
        showParameter.setStatus(OperationStatus.EDIT);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, showParameter.getFormId()));
        this.getView().showForm(showParameter);
        this.getView().getPageCache().put("closeCallBackForm", showParameter.getFormId());
    }

    public void itemClick(ItemClickEvent evt) {
        ListShowParameter parameter = new ListShowParameter();
        switch (evt.getItemKey()) {
            case "openformworkingtime": {
                parameter.setBillFormId("working_time");
                parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                this.getView().showForm((FormShowParameter)parameter);
                break;
            }
            case "openformworkinghours": {
                parameter.setBillFormId("working_hours");
                parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                this.getView().showForm((FormShowParameter)parameter);
                break;
            }
        }
    }

    private void viewLog(BeforeDoOperationEventArgs args) {
        ListShowParameter lsp = new ListShowParameter();
        lsp.setFormId("bos_list");
        lsp.setBillFormId("hbss_history_logview");
        ListFilterParameter listFilterParameter = new ListFilterParameter();
        QFilter bizObj = new QFilter("bizobj", "=", (Object)"working_plan");
        ListSelectedRowCollection listSelectedData = args.getListSelectedData();
        ListSelectedRow listSelectedRow = listSelectedData.get(0);
        long primaryKeyValue = (Long)listSelectedRow.getPrimaryKeyValue();
        if (primaryKeyValue != 0L) {
            QFilter qFilter = new QFilter("modifybillid", "in", (Object)new String[]{String.valueOf(primaryKeyValue), primaryKeyValue + "+", primaryKeyValue + "-"});
            bizObj.and(qFilter);
        }
        listFilterParameter.setFilter(bizObj);
        lsp.setListFilterParameter(listFilterParameter);
        lsp.setHasRight(true);
        String pageId = this.getView().getPageId() + "_" + primaryKeyValue;
        lsp.setPageId(pageId);
        lsp.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        ListShowParameter listShowParameter = (ListShowParameter)this.getView().getFormShowParameter();
        lsp.setCustomParam("appId", (Object)listShowParameter.getAppId());
        lsp.setCustomParam("formId", (Object)listShowParameter.getBillFormId());
        lsp.setCustomParam("tab", (Object)"1");
        lsp.setCustomParam("pkid", (Object)primaryKeyValue);
        lsp.setCustomParam("caption", (Object)listShowParameter.getCaption());
        this.getView().showForm((FormShowParameter)lsp);
        args.setCancel(true);
    }

    private void openPreviewForm(BeforeDoOperationEventArgs args) {
        ListSelectedRowCollection listSelectedData = args.getListSelectedData();
        ListSelectedRow listSelectedRow = listSelectedData.get(0);
        long primaryKeyValue = (Long)listSelectedRow.getPrimaryKeyValue();
        if (primaryKeyValue != 0L) {
            DynamicObject wpObj = WorkingPlanServiceHelper.getWorkingPlanFromPlatform((long)primaryKeyValue);
            Date startDate = wpObj.getDate("startdate");
            Date endDate = wpObj.getDate("enddate");
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_workingplandy");
            DynamicObject dynamicObject = serviceHelper.generateEmptyDynamicObject();
            WorkingPlanServiceHelper.copyEntryForWorkingPlan((DynamicObject)dynamicObject, (DynamicObject)wpObj, (boolean)false, (IFormView)this.getView());
            long workingTimeId = 0L;
            if (wpObj.getDataEntityType().getProperties().containsKey((Object)"cardentryentity")) {
                DynamicObjectCollection workingTimeColl = wpObj.getDynamicObjectCollection("cardentryentity");
                if (!workingTimeColl.isEmpty()) {
                    DynamicObject workingTime = ((DynamicObject)workingTimeColl.get(0)).getDynamicObject("workingtime");
                    workingTimeId = workingTime.getLong("id");
                }
            } else {
                workingTimeId = wpObj.getLong("workingtime.id");
            }
            DynamicObject workingTime = BusinessDataServiceHelper.loadSingle((Object)workingTimeId, (String)"working_time");
            String startDateStr = HRDateTimeUtils.format((Date)startDate, (String)"yyyy-MM-dd HH:mm:ss");
            Map workTypeMap = WorkingPlanServiceHelper.processWorkType((DynamicObject)workingTime, null, (String)startDateStr);
            List holidayList = WorkingPlanServiceHelper.processHoliday((Date)startDate, (Date)endDate, (DynamicObjectCollection)dynamicObject.getDynamicObjectCollection("entryentity2"));
            List otherDayList = WorkingPlanServiceHelper.processOtherDays((DynamicObjectCollection)dynamicObject.getDynamicObjectCollection("entryentity1"), null);
            FormShowParameter showForm = WorkingPlanServiceHelper.getShowForm((Date)startDate, (Date)endDate, (Map)workTypeMap, (List)holidayList, (List)otherDayList);
            this.getView().showForm(showForm);
        }
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        modifyDirtyManagerMap.clear();
    }

    private static /* synthetic */ boolean lambda$validateDeleteWorkingPlan$4(DynamicObject wp, ListSelectedRow sd) {
        return Objects.equals(sd.getPrimaryKeyValue(), wp.getLong("id"));
    }
}
