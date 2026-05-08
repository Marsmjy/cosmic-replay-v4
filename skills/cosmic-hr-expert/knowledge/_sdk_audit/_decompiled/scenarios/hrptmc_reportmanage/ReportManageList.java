/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.AppMenuInfo
 *  kd.bos.entity.AppMetadataCache
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.PermissionControlType
 *  kd.bos.entity.datamodel.ITreeModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.filter.FilterKeyValue
 *  kd.bos.entity.filter.FilterKeyValueCollection
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.list.column.ComboColumnDesc
 *  kd.bos.entity.list.column.DynamicTextColumnDesc
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.ext.hr.ruleengine.utils.IDStringUtils
 *  kd.bos.filter.FilterContainerFilterValues
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.BillListHyperLinkClickEvent
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.BillList
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.list.plugin.StandardTreeListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.dao.MetaCategory
 *  kd.bos.metadata.dao.MetadataDao
 *  kd.bos.metadata.entity.EntityMetadata
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrptmc.business.preindex.PresetIndexServiceHelper
 *  kd.hr.hrptmc.business.repdesign.ReportManageService
 *  kd.hr.hrptmc.business.repdesign.info.ReportBillHeadInfo
 *  kd.hr.hrptmc.common.constant.publish.HRPublishConstants
 *  kd.hr.hrptmc.common.constant.repdesign.ReportManageConstants
 *  kd.hr.hrptmc.common.util.HRReportParamUtils
 *  kd.hr.hrptmc.formplugin.web.repdesign.ReportManageList$PublishPath
 *  org.apache.commons.collections.CollectionUtils
 *  org.apache.commons.collections.MapUtils
 *  org.apache.commons.lang.ArrayUtils
 */
package kd.hr.hrptmc.formplugin.web.repdesign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.AppMenuInfo;
import kd.bos.entity.AppMetadataCache;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.PermissionControlType;
import kd.bos.entity.datamodel.ITreeModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.filter.FilterKeyValue;
import kd.bos.entity.filter.FilterKeyValueCollection;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.list.column.ComboColumnDesc;
import kd.bos.entity.list.column.DynamicTextColumnDesc;
import kd.bos.entity.tree.TreeNode;
import kd.bos.ext.hr.ruleengine.utils.IDStringUtils;
import kd.bos.filter.FilterContainerFilterValues;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.BillListHyperLinkClickEvent;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.list.plugin.StandardTreeListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.dao.MetaCategory;
import kd.bos.metadata.dao.MetadataDao;
import kd.bos.metadata.entity.EntityMetadata;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrptmc.business.preindex.PresetIndexServiceHelper;
import kd.hr.hrptmc.business.repdesign.ReportManageService;
import kd.hr.hrptmc.business.repdesign.info.ReportBillHeadInfo;
import kd.hr.hrptmc.common.constant.publish.HRPublishConstants;
import kd.hr.hrptmc.common.constant.repdesign.ReportManageConstants;
import kd.hr.hrptmc.common.util.HRReportParamUtils;
import kd.hr.hrptmc.formplugin.web.repdesign.ReportManageList;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;

public final class ReportManageList
extends StandardTreeListPlugin
implements ReportManageConstants,
HRPublishConstants {
    private static final Log LOGGER = LogFactory.getLog(ReportManageList.class);
    private static final String OPEN_NEW_REPORT = "openNewReport";
    private static final String OPEN_REPORT_MANAGE = "openReportManage";
    private static final String INIT_PARAM_SET = "initParamSetCompleted";
    private static final String TREE_ROOT = "-1";

    public void initialize() {
        super.initialize();
        String initSetCompleted = this.getPageCache().get(INIT_PARAM_SET);
        if (!HRStringUtils.equals((String)initSetCompleted, (String)"true")) {
            boolean orgPermControl = HRReportParamUtils.isOrgPermControl();
            LOGGER.info("ReportList orgPermControl : {}, currentOrgId: {}", (Object)orgPermControl, (Object)RequestContext.get().getOrgId());
            MainEntityType mainType = EntityMetadataCache.getDataEntityType((String)"hrptmc_reportmanage");
            PermissionControlType permissionControlType = mainType.getPermissionControlType();
            if (!orgPermControl) {
                permissionControlType.setDimension("DIM_NULL");
                permissionControlType.setDataDimension("");
                permissionControlType.setDataDimensionField("");
                mainType.setPermissionControlType(permissionControlType);
            } else if (HRStringUtils.equals((String)permissionControlType.getDimension(), (String)"DIM_NULL")) {
                String metadataId = MetadataDao.getIdByNumber((String)"hrptmc_reportmanage", (MetaCategory)MetaCategory.Entity);
                EntityMetadata entityMetadata = (EntityMetadata)MetadataDao.readRuntimeMeta((String)metadataId, (MetaCategory)MetaCategory.Entity);
                PermissionControlType originControlType = entityMetadata.buildDataEntityType().getPermissionControlType();
                mainType.setPermissionControlType(originControlType);
            }
            this.getPageCache().put(INIT_PARAM_SET, "true");
        }
    }

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        this.addItemClickListeners(new String[]{"tblnew"});
    }

    public void initializeTree(EventObject evt) {
        this.initTree();
    }

    public void initTreeToolbar(EventObject evt) {
        super.initTreeToolbar(evt);
        this.getView().setVisible(Boolean.FALSE, new String[]{"btnnew", "btnedit", "btndel"});
    }

    public void refreshNode(RefreshNodeEvent evt) {
        List<TreeNode> childNodes = this.getChildNodes(evt.getNodeId());
        evt.setChildNodes(childNodes);
    }

    public void buildTreeListFilter(BuildTreeListFilterEvent nodeEvent) {
        super.buildTreeListFilter(nodeEvent);
        String initTreePage = this.getPageCache().get("initTreePage");
        if (StringUtils.isEmpty((CharSequence)initTreePage)) {
            String nodeId = nodeEvent.getNodeId().toString();
            if (!TREE_ROOT.equals(nodeId)) {
                QFilter qFilter = new QFilter("cloudid", "=", (Object)nodeId);
                nodeEvent.addQFilter(qFilter);
            }
            nodeEvent.setCancel(true);
        }
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        if (!HRReportParamUtils.showExportSql()) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"exportconfigsql"});
        }
    }

    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);
        List qFilterList = e.getQFilters();
        for (int i = 0; i < qFilterList.size(); ++i) {
            QFilter qFilter = (QFilter)qFilterList.get(i);
            if (!"newpublishstatus".equals(qFilter.getProperty())) continue;
            if ("IN".equals(qFilter.getCP())) {
                qFilterList.set(i, QFilter.of((String)"1=1", (Object[])new Object[0]));
                continue;
            }
            if (!"=".equals(qFilter.getCP())) continue;
            if ("A".equals(qFilter.getValue())) {
                qFilterList.set(i, new QFilter("publishstatus", "=", (Object)"A").or(new QFilter("publishrptstatus", "=", (Object)"A").or(new QFilter("publishworkstatus", "=", (Object)"A"))));
                continue;
            }
            qFilterList.set(i, new QFilter("publishstatus", "!=", (Object)"A").and(new QFilter("publishrptstatus", "!=", (Object)"A").and(new QFilter("publishworkstatus", "!=", (Object)"A"))));
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if ("new".equals(operateKey)) {
            args.setCancel(true);
        } else if ("rptcopy".equals(operateKey)) {
            BillList billList = (BillList)this.getControl("billlistap");
            ListSelectedRowCollection selectedRows = billList.getSelectedRows();
            if (selectedRows.size() == 0) {
                args.setCancel(true);
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u9700\u8981\u590d\u5236\u7684\u62a5\u8868\u3002", (String)"ReportManageList_10", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            } else if (selectedRows.size() > 1) {
                args.setCancel(true);
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u4ec5\u652f\u6301\u590d\u5236\u4e00\u4e2a\u62a5\u8868\u3002", (String)"ReportManageList_11", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            } else {
                Long rptId = (Long)selectedRows.get(0).getPrimaryKeyValue();
                if (ReportManageService.isReportFileSourceType((long)rptId)) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u6587\u4ef6\u6570\u636e\u6e90\u62a5\u8868\u4e0d\u652f\u6301\u590d\u5236\u3002", (String)"ReportManageList_13", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                    args.setCancel(true);
                    return;
                }
                this.openNewReport(rptId);
            }
        } else if ("config".equals(operateKey)) {
            ListSelectedRowCollection selectedRows = args.getListSelectedData();
            if (selectedRows.size() > 1) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u53ea\u80fd\u9009\u62e9\u4e00\u6761\u6570\u636e\u3002", (String)"ReportManageList_15", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                return;
            }
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrptmc_paramconfig");
            DynamicObject config = serviceHelper.queryOriginalOne("id", new QFilter("report", "=", selectedRows.get(0).getPrimaryKeyValue()));
            BaseShowParameter showParameter = new BaseShowParameter();
            showParameter.setFormId("hrptmc_paramconfig");
            if (config != null) {
                showParameter.setPkId(config.get("id"));
            }
            showParameter.setCustomParam("report", selectedRows.get(0).getPrimaryKeyValue());
            showParameter.getOpenStyle().setShowType(ShowType.Modal);
            this.getView().showForm((FormShowParameter)showParameter);
        }
    }

    private ReportBillHeadInfo getBillHead(Long rptId) {
        ReportBillHeadInfo reportBillHeadInfo = new ReportBillHeadInfo();
        DynamicObject rptDy = ReportManageService.getRptBillHead((Long)rptId);
        reportBillHeadInfo.setSrcRptId(rptId);
        reportBillHeadInfo.setCreateOrgId(Long.valueOf(rptDy.getLong("createorg")));
        String number = rptDy.getString("number") + "_copy";
        if (number.length() > 25) {
            reportBillHeadInfo.setNumber(rptDy.getString("number"));
        } else {
            reportBillHeadInfo.setNumber(number);
        }
        String name = String.format(ResManager.loadKDString((String)"%s_\u590d\u5236", (String)"ReportManageList_12", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), rptDy.getString("name"));
        if (name.length() > 50) {
            reportBillHeadInfo.setName(new LocaleString(rptDy.getString("name")));
        } else {
            reportBillHeadInfo.setName(new LocaleString(name));
        }
        reportBillHeadInfo.setCloudId(rptDy.getString("cloudid"));
        reportBillHeadInfo.setAnObjId(Long.valueOf(rptDy.getLong("anobjid")));
        reportBillHeadInfo.setDescription(new LocaleString(rptDy.getString("description")));
        return reportBillHeadInfo;
    }

    public void afterDoOperation(AfterDoOperationEventArgs eve) {
        String operateKey;
        super.afterDoOperation(eve);
        AbstractOperate op = (AbstractOperate)eve.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "exportconfig": {
                ListView listView = (ListView)this.getView();
                List<Object> idList = listView.getSelectedRows().stream().map(ListSelectedRow::getPrimaryKeyValue).collect(Collectors.toList());
                if (ReportManageService.isReportsFileSourceType(idList)) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u6587\u4ef6\u6570\u636e\u6e90\u7c7b\u578b\u7684\u62a5\u8868\u4e0d\u652f\u6301\u914d\u7f6e\u5bfc\u51fa\u3002", (String)"ReportManageList_14", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                    return;
                }
                this.exportConfigExcel(idList);
                break;
            }
            case "importconfig": {
                this.importConfigFile();
                break;
            }
        }
    }

    public void itemClick(ItemClickEvent evt) {
        long currUserId;
        boolean result;
        super.click((EventObject)evt);
        if (HRStringUtils.equals((String)evt.getItemKey(), (String)"tblnew") && (result = PermissionServiceHelper.hasNewPermission((long)(currUserId = RequestContext.get().getCurrUserId()), (String)this.getView().getFormShowParameter().getCheckRightAppId(), (String)"hrptmc_reportmanage"))) {
            this.openNewReport(null);
        }
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        if (StringUtils.equals((CharSequence)"number", (CharSequence)args.getHyperLinkClickEvent().getFieldName())) {
            Long id = (Long)((BillListHyperLinkClickEvent)args.getHyperLinkClickEvent()).getCurrentRow().getPrimaryKeyValue();
            this.showReportInfos(id);
        }
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        Map ret;
        super.closedCallBack(event);
        if (OPEN_NEW_REPORT.equals(event.getActionId())) {
            ReportBillHeadInfo returnData = (ReportBillHeadInfo)event.getReturnData();
            if (null != returnData) {
                this.openReportMange(returnData);
            }
            this.getView().invokeOperation("refresh");
        } else if (HRStringUtils.equals((String)event.getActionId(), (String)"report_conf_exp_closecallback") && (ret = (Map)event.getReturnData()) != null) {
            String expStatus = (String)ret.get("exp_status");
            if ("exp_sucess".equals(expStatus)) {
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5bfc\u51fa\u6210\u529f", (String)"ReportManageList_7", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            } else if ("exp_fail".equals(expStatus)) {
                String traceid = (String)ret.get("traceid");
                this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5bfc\u51fa\u5931\u8d25\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u5206\u6790\u65e5\u5fd7\uff0ctraceid:%s\u3002", (String)"ReportManageList_8", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), traceid));
            }
        }
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void packageData(PackageDataEvent event) {
        if (event.getSource() instanceof DynamicTextColumnDesc) {
            DynamicTextColumnDesc col = (DynamicTextColumnDesc)event.getSource();
            if (HRStringUtils.equals((String)"publishpath", (String)col.getKey())) {
                this.setPublishPath(event, "appPath");
            } else if (HRStringUtils.equals((String)"publishpathrpt", (String)col.getKey())) {
                this.setPublishPath(event, "rptPath");
            } else if (HRStringUtils.equals((String)"publishpathwork", (String)col.getKey())) {
                this.setPublishPath(event, "workPath");
            }
        } else if (event.getSource() instanceof ComboColumnDesc && HRStringUtils.equals((String)"newpublishstatus", (String)((ComboColumnDesc)event.getSource()).getKey())) {
            DynamicObject dy = event.getRowData();
            if ("A".equals(dy.get("publishstatus")) || "A".equals(dy.get("publishrptstatus")) || "A".equals(dy.get("publishworkstatus"))) {
                event.setFormatValue((Object)"A");
            } else {
                event.setFormatValue((Object)"B");
            }
        }
    }

    private void setPublishPath(PackageDataEvent event, String pathKey) {
        Map publishPathMaps;
        String id = event.getRowData().getPkValue().toString();
        String publishPathMapsPage = this.getPageCache().get("publishPathMaps");
        if (HRStringUtils.isNotEmpty((String)publishPathMapsPage) && MapUtils.isNotEmpty((Map)(publishPathMaps = (Map)SerializationUtils.fromJsonString((String)this.getPageCache().get("publishPathMaps"), Map.class))) && null != publishPathMaps.get(id)) {
            event.setFormatValue(((LinkedHashMap)publishPathMaps.get(id)).get(pathKey));
            return;
        }
        event.setFormatValue((Object)"");
    }

    private void initTree() {
        ITreeModel treeModel = this.getTreeModel();
        if (null == treeModel.getRoot()) {
            TreeNode root = new TreeNode();
            root.setText(ResManager.loadKDString((String)"\u5168\u90e8\u4e1a\u52a1\u4e91", (String)"RepManageList_0", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            root.setParentid("");
            root.setId(TREE_ROOT);
            root.setIsOpened(true);
            List<TreeNode> cloudNodes = this.getCloudNodes();
            root.addChildren(cloudNodes);
            treeModel.setRoot(root);
            treeModel.setCurrentNodeId((Object)root.getId());
        }
    }

    private List<TreeNode> getCloudNodes() {
        ArrayList<TreeNode> cloudNode = new ArrayList<TreeNode>();
        HRBaseServiceHelper cloudHelper = new HRBaseServiceHelper("hbss_cloud");
        DynamicObject[] clouds = cloudHelper.query("cloud,index", new QFilter[0], "index asc");
        List bizCloudIdList = Arrays.stream(clouds).map(el -> el.getString("cloud.id")).collect(Collectors.toList());
        HRBaseServiceHelper bizCloudHelper = new HRBaseServiceHelper("bos_devportal_bizcloud");
        DynamicObject[] hrCloudList = bizCloudHelper.query("id,number,name", new QFilter[]{new QFilter("id", "in", bizCloudIdList)});
        block0: for (DynamicObject cloud : clouds) {
            for (DynamicObject hrCloud : hrCloudList) {
                if (!StringUtils.equals((CharSequence)cloud.getString("cloud.id"), (CharSequence)hrCloud.getString("id"))) continue;
                TreeNode node = new TreeNode();
                node.setId(hrCloud.getString("id"));
                node.setText(hrCloud.getString("name"));
                node.setParentid(TREE_ROOT);
                cloudNode.add(node);
                continue block0;
            }
        }
        return cloudNode;
    }

    private List<TreeNode> getChildNodes(Object parentId) {
        List<TreeNode> cloudNodes = null;
        if (this.getTreeModel().getRoot().getId().equals(parentId)) {
            cloudNodes = this.getCloudNodes();
        } else {
            TreeNode node = this.getTreeModel().getRoot().getTreeNode((String)parentId, 20);
            if (node != null) {
                return node.getChildren();
            }
        }
        return cloudNodes;
    }

    private void openNewReport(Long rptId) {
        FilterKeyValue filterKeyValue;
        List value;
        List filterKeyValues;
        Optional<FilterKeyValue> valueOp;
        FilterContainerFilterValues filterContainerFilterValues;
        List filterValueCollection;
        Optional<FilterKeyValueCollection> first;
        String filterStr = this.getPageCache().get("filtercontainerap_cachedFilterValues");
        String createOrgId = null;
        if (HRStringUtils.isNotEmpty((String)filterStr) && (first = (filterValueCollection = (filterContainerFilterValues = FilterContainerFilterValues.deSerialize((String)filterStr)).getOtherFilterValues().getFilterValueCollection()).stream().filter(filterValue -> filterValue.getFilterKeyValues().stream().anyMatch(filterKey -> HRStringUtils.equals((String)filterKey.getKey(), (String)"FieldName") && !filterKey.getValue().isEmpty() && HRStringUtils.equals((String)((String)filterKey.getValue().get(0)), (String)"createorg.id"))).findFirst()).isPresent() && (valueOp = (filterKeyValues = first.get().getFilterKeyValues()).stream().filter(filterKey -> HRStringUtils.equals((String)filterKey.getKey(), (String)"Value")).findFirst()).isPresent() && (value = (filterKeyValue = valueOp.get()).getValue()) != null && !value.isEmpty()) {
            createOrgId = (String)value.get(0);
        }
        FormShowParameter showParam = new FormShowParameter();
        showParam.setFormId("hrptmc_newreport");
        showParam.getOpenStyle().setShowType(ShowType.Modal);
        showParam.setCloseCallBack(new CloseCallBack((IFormPlugin)this, OPEN_NEW_REPORT));
        if (IDStringUtils.idNotEmpty((Long)rptId)) {
            showParam.setFormId("hrptmc_copyreport");
            showParam.setCustomParam("billHeadInfo", (Object)this.getBillHead(rptId));
        } else {
            String focusCloudId = this.getTreeListView().getTreeView().getTreeState().getFocusNodeId();
            showParam.setCustomParam("focusCloudId", (Object)focusCloudId);
            showParam.setCustomParam("createorg", createOrgId);
        }
        this.getView().showForm(showParam);
    }

    private void openReportMange(ReportBillHeadInfo returnData) {
        BaseShowParameter showParam = new BaseShowParameter();
        showParam.setFormId("hrptmc_reportmanage");
        showParam.setCustomParam("billHeadInfo", (Object)returnData);
        showParam.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParam.setCloseCallBack(new CloseCallBack((IFormPlugin)this, OPEN_REPORT_MANAGE));
        this.getView().showForm((FormShowParameter)showParam);
    }

    private void showReportInfos(Long id) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrptmc_reportmanage");
        DynamicObject data = helper.queryOne("id,number,name,cloudid,anobjid,description", (Object)id);
        if (data == null) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u6570\u636e\u5df2\u4e0d\u5b58\u5728\uff0c\u8bf7\u5237\u65b0\u9875\u9762\u3002", (String)"ReportManageList_4", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            return;
        }
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        formShowParameter.setCustomParam("name", data.get("name"));
        formShowParameter.setCustomParam("number", data.get("number"));
        formShowParameter.setCustomParam("cloudid", data.get("cloudid"));
        formShowParameter.setCustomParam("anobjid", data.get("anobjid"));
        formShowParameter.setCustomParam("description", data.get("description"));
    }

    private void putPublishPath(List<Long> idList) {
        DynamicObjectCollection rptCenterColl;
        HashMap<String, PublishPath> publishPathMaps = new HashMap<String, PublishPath>(idList.size());
        HRBaseServiceHelper hrCloudAppServiceHelper = new HRBaseServiceHelper("bos_devportal_bizapp");
        HRBaseServiceHelper hrBaseServiceHelper = new HRBaseServiceHelper("hrptmc_publishmenu");
        QFilter qFilter = new QFilter("reportmanage.id", "in", idList);
        DynamicObject[] publishMenuDys = hrBaseServiceHelper.query("id, menuapp, menu, appsrc, reportmanage.id", new QFilter[]{qFilter});
        Set menuAppIds = Arrays.stream(publishMenuDys).map(dy -> dy.getString("menuapp")).collect(Collectors.toSet());
        QFilter appFilter = new QFilter("id", "in", menuAppIds);
        Object[] appsDys = hrCloudAppServiceHelper.query("id, name, masterid", new QFilter[]{appFilter});
        if (!ArrayUtils.isEmpty((Object[])appsDys)) {
            Map<String, String> appidToNameMaps = Arrays.stream(appsDys).collect(Collectors.toMap(dy -> dy.getString("id"), dy -> null == dy.getString("name") ? "" : dy.getString("name")));
            Map<String, String> appidToOriAppIdMaps = Arrays.stream(appsDys).collect(Collectors.toMap(dy -> dy.getString("id"), dy -> StringUtils.isNotEmpty((CharSequence)dy.getString("masterid")) ? dy.getString("masterid") : dy.getString("id")));
            Arrays.stream(publishMenuDys).forEach(publishMenuDy -> {
                StringBuilder stringBuilder = new StringBuilder();
                String appId = publishMenuDy.getString("menuapp");
                if (!appidToNameMaps.containsKey(appId)) {
                    return;
                }
                stringBuilder.append(String.format(Locale.ROOT, "%s/", appidToNameMaps.get(appId)));
                List appMenusInfos = AppMetadataCache.getAppMenusInfoByAppId((String)((String)appidToOriAppIdMaps.get(appId)));
                Map<String, AppMenuInfo> menuIdToMenuInfoMaps = appMenusInfos.stream().collect(Collectors.toMap(AppMenuInfo::getId, Function.identity()));
                this.getFinalMenuName(menuIdToMenuInfoMaps, stringBuilder, publishMenuDy.getString("menu"));
                String finalMenuName = stringBuilder.substring(0, stringBuilder.length() - 1);
                PublishPath publishPath = publishPathMaps.getOrDefault(publishMenuDy.getString("reportmanage.id"), new PublishPath());
                if ("0".equals(publishMenuDy.getString("appsrc"))) {
                    publishPath.setAppPath(finalMenuName);
                } else {
                    if (StringUtils.isNotEmpty((CharSequence)publishPath.getWorkPath())) {
                        finalMenuName = publishPath.getWorkPath() + "," + finalMenuName;
                    }
                    publishPath.setWorkPath(finalMenuName);
                }
                publishPathMaps.put(publishMenuDy.getString("reportmanage.id"), publishPath);
            });
        }
        if (null != (rptCenterColl = ReportManageService.queryAllRptCenter()) && !rptCenterColl.isEmpty()) {
            DynamicObjectCollection rptGroupColl = ReportManageService.queryAllRptGroup();
            Map<Long, DynamicObject> groupMap = rptGroupColl.stream().collect(Collectors.toMap(rptGroup -> rptGroup.getLong("id"), x -> x));
            List endGroupList = rptGroupColl.stream().filter(rptGroup -> rptGroup.getBoolean("isendgroup")).collect(Collectors.toList());
            HashMap<Long, String> groupPathMap = new HashMap<Long, String>(endGroupList.size());
            HashMap<Long, String> endGroupPathMap = new HashMap<Long, String>(endGroupList.size());
            for (DynamicObject endGroup : endGroupList) {
                Long id = endGroup.getLong("id");
                String groupPath = this.getGroupPath(groupMap, groupPathMap, endGroup.getLong("id"));
                endGroupPathMap.put(id, groupPath);
            }
            for (DynamicObject rptCenterDy : rptCenterColl) {
                String rptManageId = rptCenterDy.getString("reportmanage");
                Long rptGroupId = rptCenterDy.getLong("reportgroup");
                PublishPath publishPath = publishPathMaps.getOrDefault(rptManageId, new PublishPath());
                publishPath.setRptPath((String)endGroupPathMap.get(rptGroupId));
                publishPathMaps.put(rptManageId, publishPath);
            }
        }
        this.getPageCache().put("publishPathMaps", SerializationUtils.toJsonString(publishPathMaps));
    }

    private String getGroupPath(Map<Long, DynamicObject> groupMap, Map<Long, String> groupPathMap, Long id) {
        String groupPath = groupPathMap.get(id);
        if (StringUtils.isEmpty((CharSequence)groupPath)) {
            DynamicObject groupDy = groupMap.get(id);
            Long parentId = groupDy.getLong("parent");
            if (IDStringUtils.idEmpty((Long)parentId)) {
                groupPathMap.put(groupDy.getLong("id"), groupDy.getString("name"));
                return groupDy.getString("name");
            }
            groupPath = this.getGroupPath(groupMap, groupPathMap, parentId) + "/" + groupDy.getString("name");
            groupPathMap.put(id, groupPath);
        }
        return groupPath;
    }

    private void getFinalMenuName(Map<String, AppMenuInfo> menuIdToMenuInfoMaps, StringBuilder stringBuilder, String menuId) {
        if (HRStringUtils.isEmpty((String)menuId)) {
            return;
        }
        AppMenuInfo appMenuInfo = menuIdToMenuInfoMaps.get(menuId);
        if (appMenuInfo == null) {
            return;
        }
        this.getFinalMenuName(menuIdToMenuInfoMaps, stringBuilder, appMenuInfo.getParentId());
        stringBuilder.append(String.format(Locale.ROOT, "%s/", appMenuInfo.getName()));
    }

    private void exportConfigExcel(List<Object> idList) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrptmc_configexportstart");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        Map customParams = showParameter.getCustomParams();
        customParams.put("formId", "hrptmc_reportmanage");
        customParams.put("exportReportPks", idList);
        List anObjPkList = ReportManageService.queryAnObjPkList(idList);
        customParams.put("exportAnObjPks", anObjPkList);
        List preIndexPkList = ReportManageService.queryPreIndexPkList(idList);
        customParams.put("preIndexPks", preIndexPkList);
        PresetIndexServiceHelper presetIndexHelper = new PresetIndexServiceHelper();
        if (CollectionUtils.isNotEmpty((Collection)preIndexPkList)) {
            List servicePkList = presetIndexHelper.queryServicePkList(preIndexPkList);
            customParams.put("servicePks", servicePkList);
            anObjPkList.addAll(presetIndexHelper.queryAnObjPkList(preIndexPkList));
            customParams.put("exportAnObjPks", anObjPkList);
        }
        customParams.put("exp_excel_name", ResManager.loadKDString((String)"\u62a5\u8868\u914d\u7f6e\u5bfc\u51fa", (String)"ReportManageList_6", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
        customParams.put("exp_all_sheet", "all");
        customParams.put("entitynumber", "hrptmc_reportmanage");
        customParams.put("entityName", StringUtils.isBlank((CharSequence)this.getView().getFormShowParameter().getCaption()) ? this.getView().getFormShowParameter().getFormConfig().getCaption().getLocaleValue() : this.getView().getFormShowParameter().getCaption());
        customParams.put("taskClassName", "kd.hr.hrptmc.formplugin.web.exp.HReportConfExportTask");
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "report_conf_exp_closecallback"));
        this.getView().showForm(showParameter);
    }

    private void importConfigFile() {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrptmc_configimportstart");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        Map customParams = showParameter.getCustomParams();
        customParams.put("formId", "hrptmc_reportmanage");
        customParams.put("entityName", StringUtils.isBlank((CharSequence)this.getView().getFormShowParameter().getCaption()) ? this.getView().getFormShowParameter().getFormConfig().getCaption().getLocaleValue() : this.getView().getFormShowParameter().getCaption());
        this.getView().showForm(showParameter);
    }

    static /* synthetic */ void access$000(ReportManageList x0, List x1) {
        x0.putPublishPath(x1);
    }
}
