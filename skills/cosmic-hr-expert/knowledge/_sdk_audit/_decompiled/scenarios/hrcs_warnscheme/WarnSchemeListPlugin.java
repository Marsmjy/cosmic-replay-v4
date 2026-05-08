/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.control.Control
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListColumnsArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.IListColumn
 *  kd.bos.list.ListGridView
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.list.plugin.AbstractListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.query.QFilter$QFilterNest
 *  kd.bos.schedule.api.JobDispatcher
 *  kd.bos.schedule.api.ScheduleManager
 *  kd.bos.service.ServiceFactory
 *  kd.bos.servicehelper.schedule.ScheduleServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.exportconfig.HRWarnPreSQLHelper
 *  kd.hr.hrcs.common.constants.earlywarn.WarnSchemeFieldConstants
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.control.Control;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.IListColumn;
import kd.bos.list.ListGridView;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.schedule.api.JobDispatcher;
import kd.bos.schedule.api.ScheduleManager;
import kd.bos.service.ServiceFactory;
import kd.bos.servicehelper.schedule.ScheduleServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.exportconfig.HRWarnPreSQLHelper;
import kd.hr.hrcs.common.constants.earlywarn.WarnSchemeFieldConstants;

@ExcludeFromJacocoGeneratedReport
public final class WarnSchemeListPlugin
extends HRDataBaseList
implements WarnSchemeFieldConstants {
    private static final Log LOGGER = LogFactory.getLog(WarnSchemeListPlugin.class);
    private static final String KEY_SELECTED_IDS = "selectedIds";

    public void beforeBindData(EventObject eventObject) {
        try {
            List items;
            super.beforeBindData(eventObject);
            ListGridView gridView = (ListGridView)this.getControl("gridview");
            if (gridView != null && (items = gridView.getItems()) != null) {
                for (Control item : items) {
                    String listFieldKey;
                    if (!(item instanceof IListColumn) || !"belongto".equals(listFieldKey = ((IListColumn)item).getListFieldKey())) continue;
                    ((IListColumn)item).setBlankFieldCanOrderAndFilter(true);
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("beforeBindData_error_", (Throwable)exception);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs eve) {
        try {
            Map focusNode;
            super.beforeDoOperation(eve);
            AbstractOperate op = (AbstractOperate)eve.getSource();
            String operateKey = op.getOperateKey();
            if (HRStringUtils.equals((String)"delete", (String)operateKey)) {
                List<Object> selectedIds = eve.getListSelectedData().stream().map(ListSelectedRow::getPrimaryKeyValue).collect(Collectors.toList());
                if (!selectedIds.isEmpty()) {
                    this.getPageCache().put(KEY_SELECTED_IDS, SerializationUtils.toJsonString(this.getPlanIds(selectedIds)));
                }
            } else if (HRStringUtils.equals((String)"new", (String)operateKey) && (focusNode = ((ListView)this.getView()).getTreeListView().getTreeView().getTreeState().getFocusNode()) != null && focusNode.get("isParent") != null && !((Boolean)focusNode.get("isParent")).booleanValue()) {
                boolean exists;
                String leafType;
                Object id = focusNode.get("id");
                TreeNode treeNode = ((ListView)this.getView()).getTreeListView().getTreeModel().getRoot().getTreeNode(String.valueOf(id), 15);
                if (treeNode != null && "scene".equals(leafType = (String)treeNode.getData()) && !(exists = new HRBaseServiceHelper("hrcs_warnscene").isExists((Object)Long.parseLong(String.valueOf(id))))) {
                    eve.setCancel(true);
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u9009\u62e9\u7684\u9884\u8b66\u573a\u666f\u5df2\u88ab\u5220\u9664\uff0c\u8bf7\u5237\u65b0\u9875\u9762\u540e\uff0c\u91cd\u65b0\u9009\u62e9\u3002", (String)"WarnSchemeListPlugin_3", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void beforeShowBill(BeforeShowBillFormEvent event) {
        try {
            TreeNode treeNode;
            Map focusNode = ((ListView)this.getView()).getTreeListView().getTreeView().getTreeState().getFocusNode();
            if (focusNode != null && (treeNode = ((ListView)this.getView()).getTreeListView().getTreeModel().getRoot().getTreeNode(String.valueOf(focusNode.get("id")), 15)) != null) {
                String leafType = (String)treeNode.getData();
                if ("scene".equals(leafType)) {
                    event.getParameter().setCustomParam("warnscene", focusNode.get("id"));
                } else if ("bizObj".equals(leafType)) {
                    event.getParameter().setCustomParam("warnbizobj", focusNode.get("id"));
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void packageData(PackageDataEvent packageDataEvent) {
        try {
            super.packageData(packageDataEvent);
            String colKey = packageDataEvent.getColKey();
            if ("belongto".equals(colKey)) {
                DynamicObject rowData = packageDataEvent.getRowData();
                String warnType = rowData.getString("warntype");
                if ("bizobj".equals(warnType)) {
                    DynamicObject bizObjDy = rowData.getDynamicObject("warnbizobj");
                    if (bizObjDy != null) {
                        packageDataEvent.setFormatValue((Object)bizObjDy.getString("name"));
                    }
                } else {
                    DynamicObject sceneDy = rowData.getDynamicObject("warnscene");
                    if (sceneDy != null) {
                        packageDataEvent.setFormatValue((Object)sceneDy.getString("name"));
                    }
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("packageData_error_", (Throwable)exception);
        }
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        try {
            super.setFilter(setFilterEvent);
            List qFilters = setFilterEvent.getQFilters();
            if (qFilters != null) {
                for (QFilter sceneQFilter : qFilters) {
                    this.updateBelongToFilterProperty(sceneQFilter);
                    List mainNest = sceneQFilter.getNests(true);
                    for (QFilter.QFilterNest nest : mainNest) {
                        this.updateBelongToFilterProperty(nest.getFilter());
                    }
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("setFilter_error_", (Throwable)exception);
        }
    }

    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        super.beforeCreateListColumns(args);
        List listColumns = args.getListColumns();
        if (listColumns != null) {
            for (IListColumn listColumn : listColumns) {
                String fieldName = listColumn.getFieldName();
                if (!"belongto".equals(fieldName)) continue;
                listColumn.setBlankFieldCanOrderAndFilter(true);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs eve) {
        try {
            List successIds;
            super.afterDoOperation(eve);
            AbstractOperate op = (AbstractOperate)eve.getSource();
            String operateKey = op.getOperateKey();
            if ("exportconfigsql".equals(operateKey)) {
                if (((ListView)this.getView()).getSelectedRows().size() != 1) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u884c\u3002", (String)"WarnSchemeListPlugin_1", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                    return;
                }
                Object currentAnObjId = ((ListView)this.getView()).getCurrentSelectedRowInfo().getPrimaryKeyValue();
                HRWarnPreSQLHelper hrReportPreSQLHelper = new HRWarnPreSQLHelper();
                ArrayList<Long> idList = new ArrayList<Long>(16);
                idList.add((Long)currentAnObjId);
                hrReportPreSQLHelper.generateHRWarnSchemePreSQLFile((AbstractListPlugin)this, idList);
            } else if (HRStringUtils.equals((String)"disable", (String)operateKey)) {
                List successIds2 = eve.getOperationResult().getSuccessPkIds();
                if (!successIds2.isEmpty()) {
                    ScheduleManager scheduleManager = (ScheduleManager)ServiceFactory.getService(ScheduleManager.class);
                    this.getPlanIds(successIds2).values().stream().flatMap(Collection::stream).forEach(arg_0 -> ((ScheduleManager)scheduleManager).disableSchedule(arg_0));
                }
            } else if (HRStringUtils.equals((String)"enable", (String)operateKey)) {
                List successIds3 = eve.getOperationResult().getSuccessPkIds();
                if (!successIds3.isEmpty()) {
                    ScheduleServiceHelper.enableSchedule(this.getPlanIds(successIds3).values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
                }
            } else if (HRStringUtils.equals((String)"delete", (String)operateKey) && !(successIds = eve.getOperationResult().getSuccessPkIds()).isEmpty()) {
                JobDispatcher jobDispatcher = (JobDispatcher)ServiceFactory.getService(JobDispatcher.class);
                String selectedIdsStr = this.getPageCache().get(KEY_SELECTED_IDS);
                if (HRStringUtils.isNotEmpty((String)selectedIdsStr)) {
                    Map selectedIdMap = (Map)SerializationUtils.fromJsonString((String)selectedIdsStr, Map.class);
                    successIds.forEach(warnSchemeId -> {
                        List planIds = (List)selectedIdMap.get(warnSchemeId.toString());
                        if (Objects.nonNull(planIds) && !planIds.isEmpty()) {
                            planIds.forEach(arg_0 -> ((JobDispatcher)jobDispatcher).deletePlan(arg_0));
                        }
                    });
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    private Map<String, List<String>> getPlanIds(List<Object> ids) {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_warnscheme");
        return Arrays.stream(serviceHelper.queryOriginalArray("id,planid", new QFilter[]{new QFilter("id", "in", ids)})).collect(Collectors.toMap(it -> it.getString("id"), it -> Arrays.asList(it.getString("planid").split(",")), (oldValue, newValue) -> oldValue));
    }

    private void updateBelongToFilterProperty(QFilter qFilter) {
        String property = qFilter.getProperty();
        if ("belongto".equals(property)) {
            String cp = qFilter.getCP();
            QFilter bizObjQFilter = qFilter.__copy(true);
            qFilter.__setProperty("warnscene.name");
            List nests = qFilter.getNests(true);
            for (QFilter.QFilterNest nest : nests) {
                QFilter nestFilter = nest.getFilter();
                if (!"belongto".equals(nestFilter.getProperty())) continue;
                nestFilter.__setProperty("warnscene.name");
            }
            bizObjQFilter.__setProperty("warnbizobj.name");
            List copyNests = bizObjQFilter.getNests(true);
            for (QFilter.QFilterNest copyNest : copyNests) {
                QFilter nestFilter = copyNest.getFilter();
                if (!"belongto".equals(nestFilter.getProperty())) continue;
                nestFilter.__setProperty("warnbizobj.name");
            }
            switch (cp) {
                case "!=": 
                case "<>": 
                case "not in": 
                case "not like": {
                    qFilter.and(bizObjQFilter);
                    break;
                }
                case "is null": 
                case "is not null": {
                    qFilter.and(new QFilter("warntype", "=", (Object)"scene")).or(bizObjQFilter.and(new QFilter("warntype", "=", (Object)"bizobj")));
                    break;
                }
                default: {
                    qFilter.or(bizObjQFilter);
                }
            }
        }
    }
}
