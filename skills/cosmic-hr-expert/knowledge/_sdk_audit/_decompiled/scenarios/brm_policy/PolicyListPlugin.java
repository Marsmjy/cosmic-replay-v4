/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Maps
 *  kd.bos.attachment.DisposableUrlParam
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ITreeModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.filter.ControlFilter
 *  kd.bos.entity.filter.ControlFilters
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.filter.FilterColumn
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.Search
 *  kd.bos.form.control.TreeView$TreeState
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.control.events.SearchEnterListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.New
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.lang.Lang
 *  kd.bos.list.BillList
 *  kd.bos.list.IListView
 *  kd.bos.list.ITreeListView
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.list.plugin.AbstractTreeListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.service.PermissionServiceImpl
 *  kd.bos.schedule.api.JobInfo
 *  kd.bos.schedule.api.JobType
 *  kd.bos.schedule.api.TaskInfo
 *  kd.bos.schedule.form.JobForm
 *  kd.bos.schedule.form.JobFormInfo
 *  kd.bos.servicehelper.AttachmentServiceHelper
 *  kd.hr.brm.business.web.DecisionTableHelper
 *  kd.hr.brm.business.web.PolicyServiceHelper
 *  kd.hr.brm.business.web.RuleWebHelper
 *  kd.hr.brm.common.tools.HRTreeSearchTool
 *  kd.hr.brm.formplugin.web.provider.PolicyListDataProvider
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.brm.formplugin.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import kd.bos.attachment.DisposableUrlParam;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ITreeModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.filter.ControlFilter;
import kd.bos.entity.filter.ControlFilters;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.tree.TreeNode;
import kd.bos.filter.FilterColumn;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.ShowType;
import kd.bos.form.control.Search;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.control.events.SearchEnterListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.New;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.lang.Lang;
import kd.bos.list.BillList;
import kd.bos.list.IListView;
import kd.bos.list.ITreeListView;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.list.plugin.AbstractTreeListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.service.PermissionServiceImpl;
import kd.bos.schedule.api.JobInfo;
import kd.bos.schedule.api.JobType;
import kd.bos.schedule.api.TaskInfo;
import kd.bos.schedule.form.JobForm;
import kd.bos.schedule.form.JobFormInfo;
import kd.bos.servicehelper.AttachmentServiceHelper;
import kd.hr.brm.business.web.DecisionTableHelper;
import kd.hr.brm.business.web.PolicyServiceHelper;
import kd.hr.brm.business.web.RuleWebHelper;
import kd.hr.brm.common.tools.HRTreeSearchTool;
import kd.hr.brm.formplugin.web.provider.PolicyListDataProvider;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.util.HRStringUtils;

public final class PolicyListPlugin
extends AbstractTreeListPlugin {
    private static final String OTHERNODE = "8609760E-EF83-4775-A9FF-CCDEC7C0B689";
    private static final int TREE_DEPTH = 4;
    private static final String BIZ_CLOUD = "bizcloud";
    private static final String BIZ_APP = "bizapp";
    private static final String SCENE = "scene";
    private static final String RULE_BIZ_APP_ID = "ruleBizAppId";
    private static final Log LOGGER = LogFactory.getLog(PolicyListPlugin.class);
    private static final String ROOT_ID = "1010";

    public void initializeTree(EventObject e) {
        super.initializeTree(e);
        ITreeModel tm1 = this.getTreeListView().getTreeModel();
        this.buildTree(tm1);
    }

    public void initTreeToolbar(EventObject e) {
        super.initTreeToolbar(e);
        this.getView().setVisible(Boolean.FALSE, new String[]{"btnnew", "btnedit", "btndel"});
    }

    public void filterContainerInit(FilterContainerInitArgs args) {
        String bizAppId = (String)this.getView().getFormShowParameter().getCustomParam(RULE_BIZ_APP_ID);
        if (StringUtils.isNotEmpty((CharSequence)bizAppId)) {
            List filterColumnList = args.getFilterContainerInitEvent().getCommonFilterColumns();
            for (int i = filterColumnList.size() - 1; i >= 0; --i) {
                if (!"bizappid.name".equals(((FilterColumn)filterColumnList.get(i)).getFieldName())) continue;
                filterColumnList.remove(i);
            }
            List schemeColumnList = args.getFilterContainerInitEvent().getSchemeFilterColumns();
            for (int i = schemeColumnList.size() - 1; i >= 0; --i) {
                if (!"bizappid.name".equals(((FilterColumn)schemeColumnList.get(i)).getFieldName())) continue;
                schemeColumnList.remove(i);
            }
        }
    }

    private void buildTree(ITreeModel tm1) {
        TreeNode rootNode = new TreeNode(null, ROOT_ID, ResManager.loadKDString((String)"\u4e1a\u52a1\u4e91", (String)"PolicyListPlugin_0", (String)"hrmp-brm-formplugin", (Object[])new Object[0]), true);
        rootNode.setIsOpened(true);
        DynamicObjectCollection bizClouds = RuleWebHelper.getBizCloud();
        Map bizAppMap = RuleWebHelper.getBizAppMap((DynamicObjectCollection)bizClouds);
        Map sceneMap = RuleWebHelper.getSceneMap((Map)bizAppMap);
        this.buildTree(rootNode, bizClouds, bizAppMap, sceneMap);
        tm1.setRoot(rootNode);
        this.getPageCache().put("policyTreeRootNode", SerializationUtils.toJsonString((Object)rootNode));
    }

    private void buildTree(TreeNode rootNode, DynamicObjectCollection bizClouds, Map<String, List<DynamicObject>> bizAppMap, Map<String, List<DynamicObject>> sceneMap) {
        for (DynamicObject bizCloud : bizClouds) {
            TreeNode tn1 = new TreeNode(ROOT_ID, bizCloud.getString("id"), bizCloud.getString("name"), true, (Object)BIZ_CLOUD);
            tn1.setIsOpened(false);
            List<DynamicObject> bizApps = bizAppMap.get(bizCloud.getString("id"));
            if (bizApps != null && !bizApps.isEmpty()) {
                bizApps.forEach(bizApp -> {
                    List sceneList = (List)sceneMap.get(bizApp.getString("id"));
                    if (sceneList != null && !sceneList.isEmpty()) {
                        TreeNode tn2 = new TreeNode(bizCloud.getString("id"), bizApp.getString("id"), bizApp.getString("name"), true, (Object)BIZ_APP);
                        sceneList.forEach(scene -> {
                            TreeNode treeNode3 = new TreeNode(bizApp.getString("id"), String.valueOf(scene.getPkValue()), scene.getString("name"), false, (Object)SCENE);
                            tn2.addChild(treeNode3);
                        });
                        tn1.addChild(tn2);
                    } else {
                        TreeNode tn2 = new TreeNode(bizCloud.getString("id"), bizApp.getString("id"), bizApp.getString("name"), false, (Object)BIZ_APP);
                        tn1.addChild(tn2);
                    }
                });
            }
            rootNode.addChild(tn1);
        }
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new PolicyListDataProvider());
    }

    public void afterBindData(EventObject e) {
        TreeView.TreeState treeState;
        Map focusNode;
        super.afterBindData(e);
        ITreeListView treeListView = this.getTreeListView();
        if (treeListView != null && (focusNode = (treeState = treeListView.getTreeView().getTreeState()).getFocusNode()) == null) {
            this.getTreeListView().focusRootNode();
        }
    }

    public void buildTreeListFilter(BuildTreeListFilterEvent e) {
        super.buildTreeListFilter(e);
        String bizAppId = (String)this.getView().getFormShowParameter().getCustomParam(RULE_BIZ_APP_ID);
        if (StringUtils.isNotEmpty((CharSequence)bizAppId)) {
            e.addQFilter(new QFilter("bizappid", "=", (Object)bizAppId));
        }
        if (HRStringUtils.equalsIgnoreCase((String)ROOT_ID, (String)String.valueOf(e.getNodeId())) || HRStringUtils.equals((String)OTHERNODE, (String)String.valueOf(e.getNodeId()))) {
            return;
        }
        TreeNode rootNode = this.getTreeModel().getRoot();
        TreeNode focusNode = rootNode.getTreeNode((String)e.getNodeId(), 4);
        QFilter filter = HRStringUtils.equalsIgnoreCase((String)BIZ_CLOUD, (String)((String)focusNode.getData())) ? new QFilter("bizappid.bizcloud", "=", e.getNodeId()) : (HRStringUtils.equalsIgnoreCase((String)BIZ_APP, (String)((String)focusNode.getData())) ? new QFilter("bizappid", "=", e.getNodeId()) : new QFilter(SCENE, "=", (Object)Long.valueOf(e.getNodeId().toString())));
        e.addQFilter(filter);
        e.setCancel(true);
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        Search search = (Search)this.getView().getControl("searchap");
        if (search != null) {
            search.addEnterListener((SearchEnterListener)this);
        }
    }

    public void search(SearchEnterEvent evt) {
        super.search(evt);
        String searchText = evt.getText();
        TreeNode rootNode = this.getTreeModel().getRoot();
        IPageCache pageCache = this.getPageCache();
        HRTreeSearchTool.doSearchNode((IFormView)this.getView(), (TreeNode)rootNode, (IPageCache)pageCache, (String)searchText);
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        IPageCache pageCache = this.getView().getPageCache();
        Map allCache = pageCache.getAll();
        allCache.forEach((key, value) -> {
            if (key.startsWith("brm_policy_edit") && !key.contains("root")) {
                e.setCancel(true);
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u5173\u95ed\u6240\u6709\u7b56\u7565\u9875\u9762\u3002", (String)"PolicyListPlugin_1", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
            }
        });
    }

    public void refreshNode(RefreshNodeEvent e) {
        ITreeModel tv1 = this.getTreeListView().getTreeModel();
        e.setChildNodes(HRTreeSearchTool.getChildrenNodes((ITreeModel)tv1));
        HRTreeSearchTool.resetCurrentNode();
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        if (args.getSource() instanceof AbstractOperate) {
            AbstractOperate op = (AbstractOperate)args.getSource();
            String operateKey = op.getOperateKey();
            if ("new".equals(operateKey)) {
                this.handleNewPolicy(args);
            } else if ("importdata".equals(operateKey)) {
                this.handleImportPolicy(args);
            } else if ("delete".equals(operateKey)) {
                this.handleDelPolicy(args);
            }
        }
    }

    private void handleDelPolicy(BeforeDoOperationEventArgs args) {
        ListSelectedRowCollection rows = args.getListSelectedData();
        for (ListSelectedRow row : rows) {
            Long policyId = (Long)row.getPrimaryKeyValue();
            String policyPageId = this.getPolicyPageId(policyId);
            if (policyPageId == null) continue;
            String msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u7801\u4e3a %s \u7684\u7b56\u7565\u7f16\u8f91\u9875\u9762\u5df2\u6253\u5f00\uff0c\u8bf7\u5148\u5173\u95ed\u518d\u5220\u9664\u3002", (String)"PolicyListPlugin_7", (String)"hrmp-brm-formplugin", (Object[])new Object[0]), row.getNumber());
            this.getView().showTipNotification(msg);
            args.setCancel(true);
            return;
        }
    }

    private void handleNewPolicy(BeforeDoOperationEventArgs args) {
        String currentNodeId;
        BillShowParameter showParameter = new BillShowParameter();
        TreeNode rootNode = (TreeNode)SerializationUtils.fromJsonString((String)this.getPageCache().get("policyTreeRootNode"), TreeNode.class);
        if (rootNode.getNodeLevel(currentNodeId = (String)this.getTreeModel().getCurrentNodeId(), 0) == 2) {
            showParameter.setCustomParam("defaultAppId", (Object)currentNodeId);
        } else if (rootNode.getNodeLevel(currentNodeId, 0) == 3) {
            TreeNode currentNode = rootNode.getTreeNode(currentNodeId, 3);
            showParameter.setCustomParam("defaultAppId", (Object)currentNode.getParentid());
            showParameter.setCustomParam("defaultSceneId", (Object)currentNodeId);
        }
        ControlFilters filters = ((IListView)this.getView()).getControlFilters();
        ControlFilter filter = (ControlFilter)filters.getFilters().get("createbu.id");
        if (null != filter) {
            List values = filter.getValue();
            showParameter.setCustomParam("createbu", (Object)values.get(0).toString());
        }
        args.setCancel(true);
        CloseCallBack callBack = new CloseCallBack((IFormPlugin)this, "brm_policy_edit");
        showParameter.setCloseCallBack(callBack);
        showParameter.setFormId(((New)args.getSource()).getViewBillFormId());
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setStatus(OperationStatus.ADDNEW);
        showParameter.setCaption(ResManager.loadKDString((String)"\u7b56\u7565\u914d\u7f6e", (String)"PolicyListPlugin_2", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
        this.getView().showForm((FormShowParameter)showParameter);
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        if (args.getSource() instanceof AbstractOperate) {
            String operateKey;
            AbstractOperate op = (AbstractOperate)args.getSource();
            switch (operateKey = op.getOperateKey()) {
                case "delete": 
                case "save": {
                    this.refreshListView();
                    break;
                }
                case "exportpolicy": {
                    ListSelectedRowCollection selectedRows = ((ListView)this.getView()).getSelectedRows();
                    this.downExportExcel(selectedRows);
                    break;
                }
            }
        }
        if (args.getOperationResult() != null) {
            ListView listView = (ListView)this.getView();
            listView.refresh();
        }
    }

    private void downExportExcel(ListSelectedRowCollection rows) {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setAppId("brm");
        jobInfo.setJobType(JobType.REALTIME);
        jobInfo.setRunByUserId(RequestContext.get().getCurrUserId());
        jobInfo.setName("policyExportUrl");
        jobInfo.setId(UUID.randomUUID().toString());
        jobInfo.setTaskClassname("kd.hr.brm.business.service.export.PolicyExportService");
        jobInfo.setRunByLang(Lang.get());
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("policyIds", rows.stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList()));
        params.put("clientFullContextPath", RequestContext.get().getClientFullContextPath());
        jobInfo.setParams(params);
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "policyExportUrl");
        JobFormInfo jobFormInfo = new JobFormInfo(jobInfo);
        jobFormInfo.setCaption(ResManager.loadKDString((String)"\u5bfc\u51fa\u4efb\u52a1", (String)"PolicyListPlugin_3", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
        jobFormInfo.setCloseCallBack(closeCallBack);
        jobFormInfo.setCanBackground(false);
        jobFormInfo.setCanStop(false);
        JobForm.dispatch((JobFormInfo)jobFormInfo, (IFormView)this.getView());
    }

    public void beforeShowBill(BeforeShowBillFormEvent event) {
        super.beforeShowBill(event);
        HashMap customParams = Maps.newHashMapWithExpectedSize((int)16);
        customParams.put("defaultAppId", this.getPageCache().get("defaultAppId"));
        customParams.put("defaultSceneId", this.getPageCache().get("defaultSceneId"));
        event.getParameter().setCustomParams((Map)customParams);
        CloseCallBack callBack = new CloseCallBack((IFormPlugin)this, "brm_policy_edit");
        event.getParameter().setCloseCallBack(callBack);
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        String actionId = event.getActionId();
        if ("brm_policy_edit".equals(actionId) || "brm_rulelist".equals(actionId) || "page_decisionTable".equals(actionId)) {
            this.refreshListView();
        } else if ("policyExportUrl".equals(actionId)) {
            Map returnData = (Map)event.getReturnData();
            if (returnData == null) {
                return;
            }
            String taskInfoStr = (String)returnData.get("taskinfo");
            if (HRStringUtils.isNotEmpty((String)taskInfoStr)) {
                TaskInfo taskInfo = (TaskInfo)SerializationUtils.fromJsonString((String)taskInfoStr, TaskInfo.class);
                this.handleTaskInfo(taskInfo);
            } else {
                this.getView().download((String)returnData.get("exportUrl"));
            }
        } else if ("importFinish".equals(event.getActionId())) {
            this.refreshListView();
        }
    }

    public static String getDownloadUrl(String surl) {
        DisposableUrlParam param = new DisposableUrlParam();
        param.setEntityNum("brm_policy_edit");
        param.setPath(surl);
        param.setPermItem("4730fc9f000004ae");
        param.setPkId((Object)0L);
        return AttachmentServiceHelper.genCustomAttachUrl((DisposableUrlParam)param);
    }

    private void handleTaskInfo(TaskInfo taskInfo) {
        if (taskInfo.isTaskEnd()) {
            Map map = (Map)JSONObject.parseObject((String)taskInfo.getData(), Map.class);
            String exportUrl = (String)map.get("exportUrl");
            if (HRStringUtils.isNotEmpty((String)exportUrl)) {
                this.getView().download(PolicyListPlugin.getDownloadUrl(exportUrl));
            }
        } else {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5bfc\u51fa\u7b56\u7565\u5931\u8d25\u3002", (String)"PolicyListPlugin_4", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
        }
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        String fieldName = args.getHyperLinkClickEvent().getFieldName();
        BillList list = (BillList)args.getHyperLinkClickEvent().getSource();
        Long policyId = (Long)list.getCurrentSelectedRowInfo().getPrimaryKeyValue();
        String status = list.getCurrentSelectedRowInfo().getBillStatus();
        if (HRStringUtils.equals((String)"number", (String)fieldName)) {
            String ruleListPageId = this.getOuterRuleListPageId(policyId);
            if (this.getView().getViewNoPlugin(ruleListPageId) != null) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u5173\u95ed\u5f53\u524d\u7b56\u7565\u7684\u89c4\u5219\u5217\u8868\u9875\u9762\u3002", (String)"PolicyListPlugin_5", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
            }
        } else if (HRStringUtils.equals((String)"simplename", (String)args.getHyperLinkClickEvent().getFieldName())) {
            args.setCancel(true);
            String policyPageId = this.getPolicyPageId(policyId);
            if (policyPageId != null && this.getView().getViewNoPlugin(policyPageId) != null) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u5173\u95ed\u5f53\u524d\u7b56\u7565\u7684\u7f16\u8f91\u9875\u9762\u3002", (String)"PolicyListPlugin_6", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
                return;
            }
            PolicyServiceHelper helper = new PolicyServiceHelper();
            DynamicObject policyDy = helper.queryPolicyType(policyId);
            String pageStatus = this.getPageStatus(status);
            if ("decision_set".equals(policyDy.getString("policytype"))) {
                this.openRuleListPage(policyId, pageStatus);
            } else {
                this.openDecisionTablePage(policyId, policyDy.getLong("scene.id"), pageStatus);
            }
        }
    }

    private String getPageStatus(String status) {
        PermissionServiceImpl service;
        boolean permission;
        String pageStatus = "edit";
        String needAudit = this.getPageCache().get("needAudit");
        if (HRStringUtils.isEmpty((String)needAudit)) {
            boolean audit = HRBaseDataConfigUtil.getAudit((String)"brm_policy_edit");
            needAudit = String.valueOf(audit);
        }
        if (HRStringUtils.equals((String)needAudit, (String)"true") && !HRStringUtils.equals((String)status, (String)"A")) {
            pageStatus = "view";
        }
        if (HRStringUtils.equals((String)pageStatus, (String)"edit") && !(permission = (service = new PermissionServiceImpl()).checkPermission(RequestContext.get().getCurrUserId(), "brm", "brm_policy_edit", "4715a0df000000ac"))) {
            pageStatus = "view";
        }
        return pageStatus;
    }

    private void openRuleListPage(Long policyId, String pageStatus) {
        BillShowParameter showParameter = new BillShowParameter();
        showParameter.setFormId("brm_rulelist");
        showParameter.setPageId(this.getOuterRuleListPageId(policyId));
        showParameter.setPkId((Object)policyId);
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setStatus(OperationStatus.EDIT);
        showParameter.setCustomParam("policyId", (Object)policyId);
        showParameter.setCustomParam("pageState", (Object)pageStatus);
        CloseCallBack callBack = new CloseCallBack((IFormPlugin)this, "brm_rulelist");
        showParameter.setCloseCallBack(callBack);
        this.getView().showForm((FormShowParameter)showParameter);
    }

    private void openDecisionTablePage(Long policyId, Long sceneId, String pageStatus) {
        BillShowParameter showParameter = new BillShowParameter();
        showParameter.setFormId("brm_decision_tables");
        DynamicObject decisionTableDy = DecisionTableHelper.getDecisionTableByPolicy((Object)policyId);
        if (decisionTableDy != null) {
            showParameter.setPkId(decisionTableDy.getPkValue());
            showParameter.setCustomParam("pageState", (Object)"EDIT");
        } else {
            showParameter.setCustomParam("pageState", (Object)"ADDNEW");
            showParameter.setCustomParam("policyid", (Object)policyId);
        }
        showParameter.setPageId(this.getDecisionTablePageId(policyId));
        showParameter.setCustomParam("parentPageStatus", (Object)pageStatus);
        showParameter.setCustomParam("sceneid", (Object)sceneId.toString());
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setStatus(OperationStatus.EDIT);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "page_decisionTable"));
        this.getView().showForm((FormShowParameter)showParameter);
    }

    private String getPolicyPageId(Long policyId) {
        return this.getView().getPageCache().get("policy_page_id__" + RequestContext.get().getCurrUserId() + "_" + policyId);
    }

    private String getOuterRuleListPageId(Long policyId) {
        return this.getView().getPageId() + "_" + "brm_rulelist" + "_" + policyId + "_" + RequestContext.get().getCurrUserId();
    }

    private String getDecisionTablePageId(Long policyId) {
        return this.getView().getPageId() + "_" + "page_decisionTable" + "_" + policyId + "_" + RequestContext.get().getCurrUserId();
    }

    private void refreshListView() {
        ListView listView = (ListView)this.getView();
        listView.clearSelection();
        listView.refresh();
    }

    private void handleImportPolicy(BeforeDoOperationEventArgs args) {
        args.setCancel(true);
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("brm_policy_import");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setStatus(OperationStatus.EDIT);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "importFinish"));
        this.getView().showForm(showParameter);
    }
}
