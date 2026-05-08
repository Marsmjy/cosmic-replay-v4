/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.IPageCache
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scene;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.IPageCache;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin;

@ExcludeFromJacocoGeneratedReport
public final class WarningSceneTreeListPlugin
extends HRF7TreeListPlugin {
    private static final String BTN_NEW = "btn_new";
    private static final String BTN_COPY = "btn_copy";

    public String getBizAppId() {
        return "bizapp";
    }

    public void search(SearchEnterEvent evt) {
        String searchText = evt.getText();
        if (!StringUtils.isBlank((CharSequence)searchText)) {
            List treeNodes;
            TreeNode rootNode = this.getTreeModel().getRoot();
            IPageCache pageCache = (IPageCache)this.getView().getService(IPageCache.class);
            String searchNodesCacheKey = this.getView().getPageId() + "_searchNodes";
            String matchNodesCacheKey = this.getView().getPageId() + "_matchNodes";
            String oldSearchTextCacheKey = this.getView().getPageId() + "_oldSearchText";
            String searchIndexCacheKey = this.getView().getPageId() + "_searchIndex";
            String oldSearchText = pageCache.get(oldSearchTextCacheKey);
            pageCache.put(oldSearchTextCacheKey, searchText);
            String matchNodesCache = pageCache.get(matchNodesCacheKey);
            TreeView treeView = this.getTreeListView().getTreeView();
            if ((oldSearchText == null || oldSearchText.equals(searchText)) && !StringUtils.isBlank((CharSequence)matchNodesCache)) {
                String searchNodesCache = pageCache.get(searchNodesCacheKey);
                treeNodes = StringUtils.isBlank((CharSequence)searchNodesCache) ? SerializationUtils.fromJsonStringToList((String)matchNodesCache, TreeNode.class) : SerializationUtils.fromJsonStringToList((String)searchNodesCache, TreeNode.class);
            } else {
                treeNodes = rootNode.getTreeNodeListByText(new LinkedList(), searchText, 10);
                pageCache.put(matchNodesCacheKey, SerializationUtils.toJsonString((Object)treeNodes));
                pageCache.put(searchNodesCacheKey, SerializationUtils.toJsonString((Object)treeNodes));
                pageCache.put(searchIndexCacheKey, String.valueOf(0));
            }
            if (treeNodes.isEmpty()) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u641c\u7d22\u5df2\u5b8c\u6210\uff0c\u672a\u627e\u5230\u5339\u914d\u9879\u3002", (String)"WarningSceneTreeListPlugin_0", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
            } else {
                if (StringUtils.isNotEmpty((CharSequence)pageCache.get(searchIndexCacheKey))) {
                    int indexCache = Integer.parseInt(pageCache.get(searchIndexCacheKey));
                    if (indexCache == treeNodes.size()) {
                        indexCache = 0;
                    }
                    this.focusNode(treeView, (TreeNode)treeNodes.get(indexCache));
                    pageCache.put(searchIndexCacheKey, String.valueOf(++indexCache));
                } else {
                    this.focusNode(treeView, (TreeNode)treeNodes.get(0));
                    pageCache.put(searchIndexCacheKey, String.valueOf(0));
                }
                pageCache.put(searchNodesCacheKey, SerializationUtils.toJsonString((Object)treeNodes));
            }
        }
    }

    private void focusNode(TreeView treeView, TreeNode searchTreeNode) {
        treeView.showNode(searchTreeNode.getId());
        treeView.treeNodeClick(searchTreeNode.getParentid(), searchTreeNode.getId());
        treeView.checkNode(searchTreeNode);
        treeView.focusNode(searchTreeNode);
    }

    public void refreshNode(RefreshNodeEvent refreshNodeEvent) {
        String nodeId = refreshNodeEvent.getNodeId().toString();
        TreeNode node = this.getTreeModel().getRoot().getTreeNode(nodeId, 15);
        if (node.getChildren() != null && node.getChildren().size() > 0) {
            node.setIsOpened(true);
        }
        this.focusNode(this.getTreeListView().getTreeView(), node);
        refreshNodeEvent.setChildNodes(node.getChildren());
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        AbstractOperate op;
        String operateKey;
        super.beforeDoOperation(args);
        if (args.getSource() instanceof AbstractOperate && "new".equals(operateKey = (op = (AbstractOperate)args.getSource()).getOperateKey())) {
            this.getPageCache().remove("defaultappid");
            TreeNode rootNode = this.getTreeModel().getRoot();
            String currentNodeId = (String)this.getTreeModel().getCurrentNodeId();
            if (rootNode.getNodeLevel(currentNodeId, 0) == 2) {
                this.getPageCache().put("defaultappid", currentNodeId);
            }
        }
    }

    public void beforeShowBill(BeforeShowBillFormEvent beforeShowBillFormEvent) {
        super.beforeShowBill(beforeShowBillFormEvent);
        HashMap customParams = Maps.newHashMapWithExpectedSize((int)16);
        customParams.put("defaultappid", this.getPageCache().get("defaultappid"));
        beforeShowBillFormEvent.getParameter().getCustomParams().putAll(customParams);
    }
}
