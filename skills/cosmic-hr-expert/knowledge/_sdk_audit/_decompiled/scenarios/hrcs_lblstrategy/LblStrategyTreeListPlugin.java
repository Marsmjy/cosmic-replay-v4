/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.plugin.StandardTreeListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.SessionManager
 *  kd.bos.orm.query.QFilter
 *  kd.bos.threads.ThreadPools
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.service.label.LabelTaskService
 *  kd.hr.hrcs.bussiness.service.label.LblStrategyServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.label.LabelPolicyServiceHelper
 *  kd.hr.hrcs.common.constants.label.LblStrategyConstants
 *  kd.hr.hrcs.formplugin.web.label.LblStrategyTreeListPlugin$MyListDataProvider
 */
package kd.hr.hrcs.formplugin.web.label;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.ListShowParameter;
import kd.bos.list.plugin.StandardTreeListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.SessionManager;
import kd.bos.orm.query.QFilter;
import kd.bos.threads.ThreadPools;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.service.label.LabelTaskService;
import kd.hr.hrcs.bussiness.service.label.LblStrategyServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.label.LabelPolicyServiceHelper;
import kd.hr.hrcs.common.constants.label.LblStrategyConstants;
import kd.hr.hrcs.formplugin.web.label.LblStrategyTreeListPlugin;

public final class LblStrategyTreeListPlugin
extends StandardTreeListPlugin
implements LblStrategyConstants {
    private static final Log LOGGER = LogFactory.getLog(LblStrategyTreeListPlugin.class);

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new MyListDataProvider(this.getView()));
    }

    public void initializeTree(EventObject e) {
        super.initializeTree(e);
        String rootName = ResManager.loadKDString((String)"\u5168\u90e8", (String)"LblStrategyTreeListPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        TreeNode rootNode = new TreeNode("", "1010", rootName, true);
        rootNode.setIsOpened(true);
        this.getTreeModel().setRoot(rootNode);
        this.getTreeModel().setCurrentNodeId((Object)rootNode.getId());
    }

    public void initTreeToolbar(EventObject e) {
        super.initTreeToolbar(e);
        this.getView().setVisible(Boolean.valueOf(false), new String[]{"btnnew", "btnedit", "btndel"});
    }

    public void afterCreateNewData(EventObject e) {
        TreeView treeView = this.getTreeView();
        if (null != treeView) {
            this.rebuildTree(treeView);
        }
    }

    public void search(SearchEnterEvent evt) {
        String text = evt.getText();
        TreeView treeView = this.getTreeView();
        if (HRStringUtils.isEmpty((String)text)) {
            this.rebuildTree(treeView);
        } else if (!this.buildSearchTree(treeView, text)) {
            return;
        }
        this.getView().updateView("treeview");
    }

    public void setFilter(SetFilterEvent evt) {
        String labelIds;
        String labelObjIds;
        super.setFilter(evt);
        String leftTreeQFilterJson = this.getPageCache().get("filter");
        if (HRStringUtils.isNotEmpty((String)leftTreeQFilterJson)) {
            evt.getQFilters().add(QFilter.fromSerializedString((String)leftTreeQFilterJson));
        }
        if (StringUtils.isNotEmpty((CharSequence)(labelObjIds = (String)this.getView().getFormShowParameter().getCustomParam("labelObjectIds")))) {
            List idList = SerializationUtils.fromJsonStringToList((String)labelObjIds, Long.class);
            evt.getQFilters().add(new QFilter("labelobject", "in", (Object)idList));
            evt.getQFilters().add(new QFilter("enable", "=", (Object)"1"));
        }
        if (StringUtils.isNotEmpty((CharSequence)(labelIds = (String)this.getView().getFormShowParameter().getCustomParam("labelIds")))) {
            List idList = SerializationUtils.fromJsonStringToList((String)labelIds, Long.class);
            evt.getQFilters().add(new QFilter("label", "in", (Object)idList));
            evt.getQFilters().add(new QFilter("enable", "=", (Object)"1"));
        }
    }

    public void treeNodeClick(TreeNodeEvent treeNodeEvent) {
        String nodeId = (String)treeNodeEvent.getNodeId();
        String id = String.valueOf(nodeId);
        if (HRStringUtils.equals((String)"1010", (String)id)) {
            this.getPageCache().remove("filter");
            return;
        }
        Long lblObjId = Long.parseLong(id);
        QFilter qFilter = new QFilter("labelobject", "=", (Object)lblObjId);
        this.getPageCache().put("filter", qFilter.toSerializedString());
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        if ("viewresults".equals(afterDoOperationEventArgs.getOperateKey())) {
            List successPkIds = afterDoOperationEventArgs.getOperationResult().getSuccessPkIds();
            Object lblStrategyId = successPkIds.get(0);
            String pageId = SessionManager.getCurrent().get(this.getView().getPageId() + "showForm" + lblStrategyId);
            IFormView resultView = this.getView().getView(pageId);
            if (!HRStringUtils.isEmpty((String)pageId) && resultView != null) {
                IClientViewProxy service = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
                service.addAction("activate", (Object)pageId);
            } else {
                ListShowParameter showParameter = new ListShowParameter();
                showParameter.setFormId("bos_list");
                showParameter.setBillFormId("hrcs_labelresultshow");
                showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                showParameter.setCustomParam("label", successPkIds.get(0));
                SessionManager.getCurrent().put(this.getView().getPageId() + "showForm" + lblStrategyId, showParameter.getPageId());
                this.getView().showForm((FormShowParameter)showParameter);
            }
        } else if ("execute".equals(afterDoOperationEventArgs.getOperateKey())) {
            List successPkIds = afterDoOperationEventArgs.getOperationResult().getSuccessPkIds();
            long id = (Long)successPkIds.get(0);
            boolean isPass = this.validatePre(id);
            if (isPass) {
                LabelPolicyServiceHelper labelPolicyServiceHelper = new LabelPolicyServiceHelper();
                String taskNum = labelPolicyServiceHelper.savePolicyTask(id, 99);
                ThreadPools.executeOnce((String)"kd.hr.hrcs.opplugin.web.label.LabelStrategyOp.afterExecuteOperationTransaction", () -> {
                    LabelTaskService labelTask = new LabelTaskService(Long.valueOf(id), taskNum);
                    labelTask.execute();
                    LOGGER.info("labelStrategy-executeTask,id:{},taskNum:{}", (Object)id, (Object)taskNum);
                });
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u7b56\u7565\u5f00\u59cb\u6267\u884c\uff0c\u53ef\u524d\u5f80\u201c\u67e5\u770b\u6253\u6807\u7ed3\u679c\u201d\u9875\u9762\u67e5\u770b\u6253\u6807\u7ed3\u679c\u3002", (String)"LblStrategyTreeListPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            }
        } else if ("refresh".equals(afterDoOperationEventArgs.getOperateKey())) {
            TreeView treeView = this.getTreeView();
            this.rebuildTree(treeView);
        } else if ("deletestrategy".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            this.getView().invokeOperation("refresh");
        }
    }

    private TreeView getTreeView() {
        return (TreeView)this.getView().getControl("treeview");
    }

    private void addLblObjNode(TreeNode rootNode) {
        String parentId = rootNode.getId();
        List<TreeNode> lblObjNodeList = this.getLblObjNodeList(parentId, null);
        lblObjNodeList.forEach(arg_0 -> ((TreeNode)rootNode).addChild(arg_0));
    }

    private List<TreeNode> getLblObjNodeList(String parentId, String text) {
        DynamicObject[] labelObjects = HRStringUtils.isEmpty((String)text) ? LblStrategyServiceHelper.getLabelObjects() : LblStrategyServiceHelper.getLabelObjects((String)text);
        ArrayList treeNodeList = Lists.newArrayListWithExpectedSize((int)labelObjects.length);
        for (DynamicObject labelObject : labelObjects) {
            long id = labelObject.getLong("id");
            String name = labelObject.getString("name");
            TreeNode node = new TreeNode(parentId, Long.toString(id), name, false);
            treeNodeList.add(node);
        }
        return treeNodeList;
    }

    private void rebuildTree(TreeView treeView) {
        treeView.deleteAllNodes();
        TreeNode rootNode = this.getRootNode();
        rootNode.setIsOpened(true);
        this.addLblObjNode(rootNode);
        treeView.addNode(rootNode);
        treeView.focusNode(rootNode);
        treeView.treeNodeClick("", rootNode.getId());
        this.getPageCache().put("search", null);
    }

    private TreeNode getRootNode() {
        String rootName = ResManager.loadKDString((String)"\u5168\u90e8", (String)"LblStrategyTreeListPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        return new TreeNode("", "1010", rootName, true);
    }

    private boolean buildSearchTree(TreeView treeView, String text) {
        TreeNode rootNode = this.getRootNode();
        rootNode.setIsOpened(true);
        List<TreeNode> lblObjNodeList = this.getLblObjNodeList(rootNode.getId(), text);
        if (lblObjNodeList.isEmpty()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u641c\u7d22\u5df2\u5b8c\u6210\uff0c\u672a\u627e\u5230\u5339\u914d\u9879\u3002", (String)"LblStrategyTreeListPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        this.handleTreeNode(treeView, rootNode, lblObjNodeList.get(0), lblObjNodeList);
        this.getPageCache().put("search", "1");
        return true;
    }

    private void handleTreeNode(TreeView treeView, TreeNode rootNode, TreeNode focusNode, List<TreeNode> lblObjNodeList) {
        treeView.deleteAllNodes();
        treeView.addNode(rootNode);
        treeView.focusNode(focusNode);
        treeView.treeNodeClick(focusNode.getParentid(), focusNode.getId());
        treeView.addNodes(lblObjNodeList);
    }

    private boolean validatePre(Long id) {
        boolean isPass = true;
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_lblstrategy");
        DynamicObject dynamicObject = serviceHelper.queryOne("id,enable,status,enddate", new QFilter("id", "=", (Object)id));
        if ("0".equals(dynamicObject.getString("enable"))) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u6253\u6807\u7b56\u7565\u5df2\u88ab\u7981\u7528\uff0c\u65e0\u6cd5\u6267\u884c\u6570\u636e\u6253\u6807\u3002", (String)"LblStrategyTreeListPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            isPass = false;
        } else if ("10".equals(dynamicObject.getString("enable"))) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u6253\u6807\u7b56\u7565\u672a\u542f\u7528\uff0c\u65e0\u6cd5\u6267\u884c\u6570\u636e\u6253\u6807\u3002", (String)"LblStrategyTreeListPlugin_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            isPass = false;
        } else if (!"C".equals(dynamicObject.getString("status"))) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4ec5\u652f\u6301\u4e3a\u201c\u5df2\u5ba1\u6838\u201d\u72b6\u6001\u7684\u7b56\u7565\u7acb\u5373\u6253\u6807\u3002", (String)"LblStrategyTreeListPlugin_5", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            isPass = false;
        } else if (new Date().compareTo(dynamicObject.getDate("enddate")) > 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u6253\u6807\u7b56\u7565\u5df2\u8fc7\u671f\uff0c\u65e0\u6cd5\u6267\u884c\u6570\u636e\u6253\u6807\u3002", (String)"LblStrategyTreeListPlugin_7", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            isPass = false;
        } else {
            HRBaseServiceHelper policyTaskServiceHelper = new HRBaseServiceHelper("hrcs_labelpolicytask");
            boolean exists = policyTaskServiceHelper.isExists(new QFilter[]{new QFilter("taskstatus", "=", (Object)"1"), new QFilter("labelpolicy", "=", (Object)id)});
            if (exists) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u5df2\u6709\u6267\u884c\u4e2d\u7684\u4efb\u52a1\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\u3002", (String)"LblStrategyTreeListPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                isPass = false;
            }
        }
        return isPass;
    }
}
