/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ArrayUtils
 *  kd.bos.devportal.common.util.AppUtils
 *  kd.bos.entity.datamodel.ITreeModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.operate.Delete
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.filter.FilterColumn
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.form.plugin.TreeListBizAppsPlugin
 *  kd.bos.list.BillList
 *  kd.bos.list.IListView
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.devportal.BizAppServiceHelp
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hies.common.constant.TemplateConfConst
 *  kd.hr.hies.common.util.MethodUtil
 *  kd.hr.hies.common.util.TemplateUtil
 *  kd.hrmp.hies.multientry.business.templateConf.EntryTemplateConfService
 *  kd.hrmp.hies.multientry.common.HiesEntryRes
 *  kd.hrmp.hies.multientry.formplugin.TemplateListDataProvider
 *  org.apache.commons.lang3.tuple.Pair
 */
package kd.hrmp.hies.multientry.formplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ArrayUtils;
import kd.bos.devportal.common.util.AppUtils;
import kd.bos.entity.datamodel.ITreeModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.operate.Delete;
import kd.bos.entity.tree.TreeNode;
import kd.bos.filter.FilterColumn;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.form.plugin.TreeListBizAppsPlugin;
import kd.bos.list.BillList;
import kd.bos.list.IListView;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.devportal.BizAppServiceHelp;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hies.common.constant.TemplateConfConst;
import kd.hr.hies.common.util.MethodUtil;
import kd.hr.hies.common.util.TemplateUtil;
import kd.hrmp.hies.multientry.business.templateConf.EntryTemplateConfService;
import kd.hrmp.hies.multientry.common.HiesEntryRes;
import kd.hrmp.hies.multientry.formplugin.TemplateListDataProvider;
import org.apache.commons.lang3.tuple.Pair;

@ExcludeFromJacocoGeneratedReport
public final class TemplateTreeListEdit
extends TreeListBizAppsPlugin
implements TemplateConfConst {
    private static final String BIZOBJECT = "entity";
    private static final String NEW = "tblnew";
    private static final String COPY = "tblcopy";
    private static final String OP_REFRESH = "refresh";
    private static final String BINDENTITYID = "bindEntityId";
    private static final String CLOSECALLBACK_SAVE = "closeCallBack_save";
    private static final String TBLNEW_IMPORT = "tblnew_import";
    private static final String TBLNEW_EXPORT = "tblnew_export";
    private static final String TBL_ENTRY_IMPORT = "tbl_entry_import";
    private static final String TBL_MULTIENTRY_IMPORT = "tbl_multientry_import";
    private static final String CLOSECALLBACK_IMPORT_TYPE = "closeCallBack_import_type";
    private static final String TEMPTYPE_SYS_GEN = "flexpanel_sys";
    private static final String TEMPTYPE_LOC_GEN = "flexpanel_loc";
    private static final String IS_IMPORT = "isImport";

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new TemplateListDataProvider());
    }

    public void initialize() {
        super.initialize();
        this.addClickListeners(new String[]{NEW});
        this.getView().setVisible(Boolean.FALSE, new String[]{"btnnew", "btnedit", "btndel"});
    }

    public void initializeTree(EventObject e) {
        super.initializeTree(e);
        ITreeModel treeModel = this.getTreeModel();
        TreeNode root = treeModel.getRoot();
        root.setIsOpened(true);
        root.setChildren(this.getCloudNodes());
        this.getView().setVisible(Boolean.FALSE, new String[]{"btnnew", "btnedit", "btndel"});
    }

    private void openNewTempTab(String tplGenMode, String isImport) {
        BillShowParameter showParameter = new BillShowParameter();
        showParameter.setFormId(((ListView)this.getView()).getBillFormId());
        showParameter.setStatus(OperationStatus.ADDNEW);
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        String bindEntityId = this.getView().getPageCache().get("_IMPORTTEMPLATETREELIST_BIZOBJID_");
        if (HRStringUtils.isNotBlank((CharSequence)bindEntityId)) {
            showParameter.setCustomParam(BINDENTITYID, (Object)bindEntityId);
        }
        showParameter.setCustomParam("tplgenmode", (Object)tplGenMode);
        showParameter.setCustomParam(IS_IMPORT, (Object)isImport);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CLOSECALLBACK_SAVE));
        this.getView().showForm((FormShowParameter)showParameter);
    }

    private String getCurFormId() {
        return ((ListView)this.getView()).getBillFormId();
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String key = op.getOperateKey();
        if (HRStringUtils.equals((String)key, (String)"assign")) {
            ListSelectedRowCollection selectedRows = this.getSelectedRows();
            ListSelectedRow selectedRow = selectedRows.get(0);
            Object primaryKeyValue = selectedRow.getPrimaryKeyValue();
            BillShowParameter showParameter = new BillShowParameter();
            showParameter.setFormId(this.getCurFormId());
            showParameter.setStatus(OperationStatus.EDIT);
            showParameter.setCaption(ResManager.loadKDString((String)"\u5206\u914d\u4f7f\u7528\u4eba", (String)HiesEntryRes.TemplateTreeListEdit_7.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
            showParameter.setPkId(primaryKeyValue);
            StyleCss styleCss = new StyleCss();
            styleCss.setWidth("1000");
            styleCss.setHeight("600");
            showParameter.getOpenStyle().setInlineStyleCss(styleCss);
            showParameter.getOpenStyle().setShowType(ShowType.Modal);
            showParameter.setCustomParam("btn", (Object)"assign");
            showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CLOSECALLBACK_SAVE));
            this.getView().showForm((FormShowParameter)showParameter);
            args.setCancel(true);
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        ListSelectedRowCollection selectedRows = this.getSelectedRows();
        switch (evt.getItemKey()) {
            case "tblcopy": {
                if (!selectedRows.isEmpty()) break;
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u9700\u8981\u590d\u5236\u7684\u6a21\u677f\u3002", (String)HiesEntryRes.TemplateTreeListEdit_6.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                evt.setCancel(true);
                break;
            }
            case "assign": {
                if (selectedRows.isEmpty()) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u6a21\u677f\u3002", (String)HiesEntryRes.TemplateTreeListEdit_11.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                    evt.setCancel(true);
                    break;
                }
                if (selectedRows.size() > 1) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u652f\u6301\u6279\u91cf\u64cd\u4f5c\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u6570\u636e\u3002", (String)HiesEntryRes.TemplateTreeListEdit_12.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                    evt.setCancel(true);
                    break;
                }
                Object primaryKeyValue = selectedRows.get(0).getPrimaryKeyValue();
                DynamicObject dy = EntryTemplateConfService.load((Object)primaryKeyValue);
                Boolean issyspreset = dy.getBoolean("issyspreset");
                if (!Boolean.TRUE.equals(issyspreset)) break;
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u9884\u7f6e\u6a21\u677f\u4e0d\u652f\u6301\u64cd\u4f5c\u3002", (String)HiesEntryRes.TemplateTreeListEdit_8.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                evt.setCancel(true);
                break;
            }
            case "tblnew_import": {
                this.openTemplateTypeChoose(evt, "1");
                break;
            }
            case "tblnew_export": {
                this.openNewTempTab("sysgen", "0");
                evt.setCancel(true);
                break;
            }
            case "tbl_entry_import": {
                this.openNewTempTab("sysgen", "1");
                break;
            }
            case "tbl_multientry_import": {
                this.openNewTempTab("sysgen", "1");
                break;
            }
        }
    }

    private void openTemplateTypeChoose(BeforeItemClickEvent evt, String isImport) {
        FormShowParameter formShowParameter = new FormShowParameter();
        formShowParameter.setFormId("hies_tempgentype");
        formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
        formShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CLOSECALLBACK_IMPORT_TYPE));
        formShowParameter.setCustomParam(IS_IMPORT, (Object)isImport);
        this.getView().showForm(formShowParameter);
        evt.setCancel(true);
    }

    private List<TreeNode> getCloudNodes() {
        HRBaseServiceHelper cloudHelper = new HRBaseServiceHelper("hbss_cloud");
        DynamicObject[] hrClouds = cloudHelper.queryOriginalArray("cloud", new QFilter[0], "index");
        Pair<Set<Object>, Set<Object>> pair = this.getEntityCloudIdList();
        Set cloudIds = (Set)pair.getLeft();
        Set appIds = (Set)pair.getRight();
        Object[] hrCloudIds = Arrays.stream(hrClouds).filter(el -> {
            String pkValue = el.getString("cloud");
            return cloudIds.contains(pkValue);
        }).map(el -> el.getString("cloud")).toArray();
        TreeNode root = AppUtils.getSubsysTree((String[])cloudIds.toArray(new String[0]), (String[])appIds.toArray(new String[0]), null, (String)"app", (boolean)true, (boolean)false, (boolean)true);
        List clouds = null;
        if (root != null) {
            clouds = root.getChildren();
        }
        if (clouds == null) {
            return new ArrayList<TreeNode>();
        }
        ArrayList<TreeNode> cloudOrderNodes = new ArrayList<TreeNode>();
        for (Object cloudId : hrCloudIds) {
            for (TreeNode cloudNode : clouds) {
                if (!cloudId.equals(cloudNode.getId())) continue;
                cloudNode.setChildren(new ArrayList());
                cloudOrderNodes.add(cloudNode);
            }
        }
        return cloudOrderNodes;
    }

    public Pair<Set<Object>, Set<Object>> getEntityCloudIdList() {
        Set<Object> cloudIdSet = new HashSet(16);
        Set<Object> appIdSet = new HashSet(16);
        Object[] objects = BusinessDataServiceHelper.load((String)this.getCurFormId(), (String)BIZOBJECT, (QFilter[])new QFilter[0]);
        if (!ArrayUtils.isEmpty((Object[])objects)) {
            Set entityIdSet = Arrays.stream(objects).filter(o -> Objects.nonNull(o.getDynamicObject(BIZOBJECT))).map(o -> o.getDynamicObject(BIZOBJECT).getPkValue()).collect(Collectors.toSet());
            DynamicObject[] appObjects = BusinessDataServiceHelper.load((String)"bos_entityobject", (String)"bizappid", (QFilter[])new QFilter[]{new QFilter("id", "in", entityIdSet)});
            appIdSet = Arrays.stream(appObjects).filter(o -> Objects.nonNull(o.getDynamicObject("bizappid"))).map(o -> o.getDynamicObject("bizappid").getPkValue()).collect(Collectors.toSet());
            DynamicObject[] cloudObjects = BusinessDataServiceHelper.load((String)"bos_devportal_bizapp", (String)"bizcloud", (QFilter[])new QFilter[]{new QFilter("id", "in", appIdSet)});
            cloudIdSet = Arrays.stream(cloudObjects).filter(o -> o.getDynamicObject("bizcloud") != null).map(o -> o.getDynamicObject("bizcloud").getPkValue()).collect(Collectors.toSet());
        }
        return Pair.of(cloudIdSet, appIdSet);
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        setFilterEvent.addCustomQFilter(new QFilter(BIZOBJECT, "in", (Object)TemplateUtil.getValidEntityNumbers((boolean)false)));
        setFilterEvent.addCustomQFilter(new QFilter("source", "in", Arrays.asList("normal", "external", "")));
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        String oprFormId = (String)formShowParameter.getCustomParam("oprFormId");
        if (HRStringUtils.isNotBlank((CharSequence)oprFormId)) {
            List qFilters = setFilterEvent.getQFilters();
            String[] split = oprFormId.split("\\.");
            oprFormId = split[0];
            QFilter matchEntityNumberQFilter = new QFilter(BIZOBJECT, "=", (Object)oprFormId);
            if (split.length == 2) {
                qFilters.add(matchEntityNumberQFilter.and(new QFilter("entrytype", "like", (Object)("%" + split[1] + "%"))));
            } else {
                qFilters.add(matchEntityNumberQFilter);
            }
        }
    }

    public void filterContainerInit(FilterContainerInitArgs args) {
        super.filterContainerInit(args);
        IFormView view = this.getView();
        if (this.isLookup((IListView)view)) {
            return;
        }
        List commonFilterColumns = args.getCommonFilterColumns();
        for (FilterColumn column : commonFilterColumns) {
            String fieldName = column.getFieldName();
            if (!"enable".equals(fieldName)) continue;
            column.setDefaultValue("1");
        }
    }

    private boolean isLookup(IListView view) {
        boolean isLookup = false;
        if (view.getFormShowParameter() instanceof ListShowParameter) {
            ListShowParameter listShowParameter = (ListShowParameter)view.getFormShowParameter();
            isLookup = listShowParameter.isLookUp();
        }
        return isLookup;
    }

    public void buildTreeListFilter(BuildTreeListFilterEvent e) {
        String nodeId = e.getNodeId().toString();
        TreeNode root = this.getTreeModel().getRoot();
        TreeNode node = root.getTreeNode(nodeId, 20);
        QFilter filter = this.getFilter(node);
        e.addQFilter(filter);
        e.setCancel(true);
    }

    public void refreshNode(RefreshNodeEvent e) {
        String nodeId = e.getNodeId().toString();
        TreeNode root = this.getTreeModel().getRoot();
        TreeNode parent = root.getTreeNode(nodeId, 20);
        List<TreeNode> childNodes = this.getChildNodes(root, parent);
        e.setChildNodes(childNodes);
        if (parent != null) {
            this.getTreeListView().getTreeView().expand(nodeId);
        }
    }

    private List<TreeNode> getChildNodes(TreeNode root, TreeNode parent) {
        String key;
        if (HRStringUtils.equals((String)parent.getId(), (String)root.getId())) {
            return this.getCloudNodes();
        }
        switch (key = (String)parent.getData()) {
            case "cloud": {
                return this.getAppNodesBy(parent.getId());
            }
            case "app": {
                return this.getBillNodesBy(this.getAppIdsBy(parent.getId()));
            }
        }
        return new ArrayList<TreeNode>();
    }

    protected List<TreeNode> getBillNodesBy(List<String> appIds) {
        String fields = "id, number, name, bizappid";
        Object[] validEntityNumberFilter = TemplateUtil.getValidEntityNumberFilter((boolean)false);
        List filters = MethodUtil.arrayToList((Object[])validEntityNumberFilter);
        QFilter appIdFilter = new QFilter("bizappid", "in", appIds);
        filters.add(appIdFilter);
        QFilter entityIdFilter = null;
        Object[] diaeTplObjects = BusinessDataServiceHelper.load((String)this.getCurFormId(), (String)BIZOBJECT, (QFilter[])new QFilter[0]);
        if (!ArrayUtils.isEmpty((Object[])diaeTplObjects)) {
            Set entityIdSet = Arrays.stream(diaeTplObjects).filter(o -> Objects.nonNull(o.getDynamicObject(BIZOBJECT))).map(o -> o.getDynamicObject(BIZOBJECT).getPkValue()).collect(Collectors.toSet());
            entityIdFilter = new QFilter("id", "in", entityIdSet);
        }
        filters.add(entityIdFilter);
        DynamicObject[] objects = BusinessDataServiceHelper.load((String)"bos_entityobject", (String)fields, (QFilter[])filters.toArray(new QFilter[0]));
        ArrayList<TreeNode> billNodes = new ArrayList<TreeNode>(objects.length);
        for (DynamicObject object : objects) {
            String nodeId = object.getString("id");
            String name = object.getLocaleString("name").toString();
            String parentId = object.getString("bizappid_id");
            String data = object.getString("number");
            TreeNode node = this.createTreeNode(nodeId, name, parentId, data);
            billNodes.add(node);
        }
        return billNodes;
    }

    private TreeNode createTreeNode(String nodeId, String name, String parentId, String data) {
        TreeNode node = new TreeNode();
        node.setText(name);
        node.setParentid(parentId);
        node.setId(nodeId);
        node.setData((Object)data);
        return node;
    }

    public List<TreeNode> getAppNodesBy(String cloudNodeId) {
        Pair<Set<Object>, Set<Object>> entityCloudIdList = this.getEntityCloudIdList();
        Set appIds = (Set)entityCloudIdList.getRight();
        TreeNode root = AppUtils.getSubsysTree((String[])new String[]{cloudNodeId}, (String[])appIds.toArray(new String[0]), null, (String)"app", (boolean)true, (boolean)false, (boolean)true);
        List clouds = null;
        if (root != null) {
            clouds = root.getChildren();
        }
        if (clouds == null || clouds.isEmpty()) {
            return new ArrayList<TreeNode>();
        }
        List currCloud = clouds.stream().filter(cloud -> cloud.getId().equals(cloudNodeId)).collect(Collectors.toList());
        List apps = ((TreeNode)currCloud.get(0)).getChildren();
        if (apps == null || apps.isEmpty()) {
            return new ArrayList<TreeNode>();
        }
        for (TreeNode app : apps) {
            if (app == null) continue;
            app.setChildren(new ArrayList(0));
        }
        return apps;
    }

    private List<String> getAppIdsBy(String appId) {
        DynamicObjectCollection allApps = BizAppServiceHelp.getAllBizApps();
        String masterId = allApps.stream().filter(obj -> HRStringUtils.equals((String)appId, (String)obj.getString("id"))).findFirst().map(app -> {
            String tType = app.getString("type");
            return HRStringUtils.equals((String)"0", (String)tType) ? app.getString("id") : app.getString("masterid");
        }).orElse("");
        return HRStringUtils.isBlank((CharSequence)masterId) ? new ArrayList<String>() : allApps.stream().filter(obj -> {
            String tId = obj.getString("id");
            String tMasterId = obj.getString("masterid");
            return HRStringUtils.equals((String)masterId, (String)tId) || HRStringUtils.equals((String)masterId, (String)tMasterId);
        }).map(obj -> obj.getString("id")).collect(Collectors.toList());
    }

    private QFilter getFilter(TreeNode node) {
        String nodeId = node.getId();
        if (this.isTopNode(nodeId)) {
            return null;
        }
        switch ((String)node.getData()) {
            case "cloud": {
                return new QFilter(BIZOBJECT, "in", (Object)this.getBillIdInCloud(nodeId));
            }
            case "app": {
                return new QFilter(BIZOBJECT, "in", (Object)this.getBillIdInApp(nodeId));
            }
        }
        return new QFilter(BIZOBJECT, "=", (Object)nodeId);
    }

    public void beforeShowBill(BeforeShowBillFormEvent e) {
        super.beforeShowBill(e);
        BillOperationStatus billStatus = e.getParameter().getBillStatus();
        if (!OperationStatus.ADDNEW.name().equals(billStatus.name())) {
            e.getParameter().setStatus(OperationStatus.VIEW);
            e.getParameter().setBillStatus(BillOperationStatus.VIEW);
        }
        BillShowParameter param = e.getParameter();
        String currentNodeId = this.getTreeModel().getCurrentNodeId().toString();
        String rootId = this.getTreeModel().getRoot().getId();
        if (currentNodeId.contains("cloud_") || currentNodeId.contains("app_") || rootId.equals(currentNodeId)) {
            return;
        }
        param.setCustomParam("tree_parent_id", null);
        param.setCustomParam(BIZOBJECT, (Object)currentNodeId);
    }

    public void treeNodeClick(TreeNodeEvent treenodeevent) {
        this.getTreeModel().setNodeClickExpand(false);
        String nodeId = treenodeevent.getNodeId().toString();
        TreeNode node = this.getTreeModel().getRoot().getTreeNode(nodeId, 10);
        this.getPageCache().remove("_IMPORTTEMPLATETREELIST_BIZOBJID_");
        if (node != null && node.getData() != null) {
            String nodeData = (String)node.getData();
            if (node.getChildren() != null || "app".equals(nodeData) || "cloud".equals(nodeData)) {
                nodeData = null;
            }
            this.getPageCache().put("_IMPORTTEMPLATETREELIST_BIZOBJID_", nodeData);
        }
        super.treeNodeClick(treenodeevent);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        if (CLOSECALLBACK_SAVE.equals(actionId)) {
            this.getView().invokeOperation(OP_REFRESH);
        } else if (CLOSECALLBACK_IMPORT_TYPE.equals(actionId)) {
            Map returnData = (Map)closedCallBackEvent.getReturnData();
            if (returnData == null || returnData.isEmpty()) {
                return;
            }
            String tempType = (String)returnData.get("temptype");
            String isImport = (String)returnData.get(IS_IMPORT);
            if (TEMPTYPE_SYS_GEN.equals(tempType)) {
                this.openNewTempTab("sysgen", isImport);
            } else if (TEMPTYPE_LOC_GEN.equals(tempType)) {
                this.openNewTempTab("localupload", isImport);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        String operateKey = formOperate.getOperateKey();
        if (formOperate instanceof Delete && HRStringUtils.equals((String)"delete", (String)operateKey) && args.getOperationResult() != null && !args.getOperationResult().isSuccess()) {
            BillList billList = (BillList)this.getView().getControl("billlistap");
            ListSelectedRowCollection selectedRows = billList.getSelectedRows();
            int selectedSize = selectedRows.size();
            if (selectedSize == 1) {
                return;
            }
            HashSet errorPkIds = args.getOperationResult().getValidateResult().getErrorPkIds();
            int errRowSize = errorPkIds.size();
            if (selectedSize == errRowSize) {
                return;
            }
            List sortSelectedRowKeys = selectedRows.stream().sorted(Comparator.comparing(ListSelectedRow::getRowKey)).collect(Collectors.toList());
            ArrayList<Integer> finalRowKey = new ArrayList<Integer>(8);
            int sucessNum = 0;
            for (ListSelectedRow selectedRow : sortSelectedRowKeys) {
                int rowKey = selectedRow.getRowKey();
                if (errorPkIds.contains(selectedRow.getPrimaryKeyValue())) {
                    finalRowKey.add(rowKey - sucessNum);
                    continue;
                }
                ++sucessNum;
            }
            int[] errRowKeys = finalRowKey.stream().mapToInt(Integer::intValue).toArray();
            billList.selectRows(errRowKeys);
        }
    }
}
