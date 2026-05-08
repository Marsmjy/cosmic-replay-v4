/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IPageCache
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.form.mcontrol.SearchAp
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRCloudServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin
 *  kd.hr.hrcs.common.constants.earlywarn.WarnSchemeFieldConstants
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scheme;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IPageCache;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.form.mcontrol.SearchAp;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRCloudServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin;
import kd.hr.hrcs.common.constants.earlywarn.WarnSchemeFieldConstants;

public final class WarnSchemeTreeListPlugin
extends HRF7TreeListPlugin
implements WarnSchemeFieldConstants {
    private static final Log LOG = LogFactory.getLog(WarnSchemeTreeListPlugin.class);
    private static final String NODE_TYPE_SCENE = "scene";
    private static final String NODE_TYPE_BIZ_OBJ = "bizObj";
    private String filterKey = "bizapp";
    private static final String DEPLOY_STATUS = "deploystatus";
    private static final String BOS_DEVPORTAL_BIZAPP = "bos_devportal_bizapp";
    private static final String VISIBLE = "visible";

    public void beforeBindData(EventObject e) {
        try {
            super.beforeBindData(e);
            this.updateSearch();
        }
        catch (Exception exception) {
            LOG.error("beforeBindData_error_", (Throwable)exception);
        }
    }

    private void updateSearch() {
        SearchAp searchAp = new SearchAp();
        searchAp.setKey("searchap");
        searchAp.setSearchEmptyText(new LocaleString(ResManager.loadKDString((String)"\u641c\u7d22\u4e91/\u5e94\u7528/\u9884\u8b66\u573a\u666f\u540d\u79f0/\u4e1a\u52a1\u5bf9\u8c61", (String)"WarningSceneTreeListPlugin_3", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0])));
        this.getView().updateControlMetadata("searchap", searchAp.createControl());
    }

    public String getBizAppId() {
        return this.filterKey;
    }

    public void initializeTree(EventObject evt) {
        super.initializeTree(evt);
        try {
            TreeNode root = this.getTreeModel().getRoot();
            this.initChildrenNode(root);
        }
        catch (Throwable exception) {
            LOG.error("error:", exception);
        }
    }

    private void initChildrenNode(TreeNode root) {
        List cloudTreeNodes = root.getChildren();
        LOG.info("initChildrenNode_cloudTreeNode_{}", (Object)cloudTreeNodes);
        List cloudIds = HRCloudServiceHelper.getAllHRCommonCloudIdsSort();
        LOG.info("initChildrenNode_cloudIds_{}", (Object)cloudIds);
        DynamicObjectCollection sceneDys = new HRBaseServiceHelper("hrcs_warnscene").queryOriginalCollection("bizapp,name,number,id", new QFilter[0]);
        ArrayList cloudTreeNodesTemp = Lists.newArrayListWithExpectedSize((int)cloudTreeNodes.size());
        HashMap appNodesMap = Maps.newHashMapWithExpectedSize((int)16);
        HashSet appIds = Sets.newHashSetWithExpectedSize((int)16);
        if (sceneDys != null) {
            for (DynamicObject sceneDy : sceneDys) {
                appIds.add(sceneDy.getString("bizapp"));
            }
        }
        HashMap appIdExtMap = Maps.newHashMapWithExpectedSize((int)16);
        if (!appIds.isEmpty()) {
            DynamicObject[] apps;
            ArrayList qFilters = Lists.newArrayListWithExpectedSize((int)3);
            qFilters.add(new QFilter("id", "in", (Object)appIds).or(new QFilter("masterid", "in", (Object)appIds)));
            qFilters.add(new QFilter(DEPLOY_STATUS, "=", (Object)"2"));
            qFilters.add(new QFilter(VISIBLE, "=", (Object)Boolean.TRUE));
            Iterator appHelper = new HRBaseServiceHelper(BOS_DEVPORTAL_BIZAPP);
            for (DynamicObject app : apps = appHelper.queryOriginalArray("id,masterid", qFilters.toArray(new QFilter[0]))) {
                appIdExtMap.put(app.getString("id"), app.getString("masterid"));
            }
        }
        if (sceneDys != null) {
            LinkedHashSet hasPermSceneIds = Sets.newLinkedHashSetWithExpectedSize((int)sceneDys.size());
            for (DynamicObject sceneDy : sceneDys) {
                String originalAppId = sceneDy.getString("bizapp");
                String sceneName = sceneDy.getString("name");
                String sceneId = sceneDy.getString("id");
                String appId = (String)appIdExtMap.get(originalAppId);
                if (appId != null) {
                    if (HRStringUtils.isEmpty((String)appId)) {
                        appId = originalAppId;
                    }
                    TreeNode treeNode = new TreeNode(appId, sceneId, sceneName, false, (Object)NODE_TYPE_SCENE);
                    List treeNodes = appNodesMap.computeIfAbsent(appId, key -> Lists.newArrayListWithExpectedSize((int)10));
                    hasPermSceneIds.add(sceneDy.getLong("id"));
                    treeNodes.add(treeNode);
                    continue;
                }
                LOG.error("initChildrenNode_scene_get_empty_originalAppId_{},name_{}", (Object)originalAppId, (Object)sceneName);
            }
            LOG.info("initChildrenNode_hasPermSceneIds_{}", (Object)hasPermSceneIds);
            this.getPageCache().put("hasPermSceneIds", SerializationUtils.toJsonString((Object)hasPermSceneIds));
        }
        LOG.info("initChildrenNode_appNodesMap_{}", (Object)appNodesMap);
        for (TreeNode treeNode : cloudTreeNodes) {
            treeNode.setIsOpened(true);
            List<TreeNode> childrenTreeNodes = treeNode.getChildren().stream().filter(node -> appNodesMap.containsKey(node.getId())).collect(Collectors.toList());
            childrenTreeNodes.forEach(node -> {
                node.setIsOpened(true);
                node.setChildren((List)appNodesMap.get(node.getId()));
            });
            treeNode.setChildren(childrenTreeNodes);
            if (childrenTreeNodes.size() <= 0) continue;
            cloudTreeNodesTemp.add(treeNode);
        }
        root.setChildren((List)cloudTreeNodesTemp);
    }

    public void buildTreeListFilter(BuildTreeListFilterEvent evt) {
    }

    /*
     * WARNING - void declaration
     */
    public void setFilter(SetFilterEvent evt) {
        String hasPermSceneIds;
        QFilter qFilter;
        DynamicObject[] cloudNodeList;
        List appNodeList;
        super.setFilter(evt);
        String currentNodeId = (String)this.getTreeModel().getCurrentNodeId();
        TreeNode currentNode = this.getTreeModel().getRoot().getTreeNode(currentNodeId, 5);
        if (!currentNodeId.equals(this.getTreeModel().getRoot().getId()) && (currentNode == null || currentNode.isLeaf() && !new HRBaseServiceHelper("hrcs_warnscene").isExists((Object)currentNodeId) && !new HRBaseServiceHelper("hrcs_warnscheme").isExists(new QFilter("warnbizobj", "=", (Object)currentNodeId)))) {
            this.getTreeListView().focusRootNode();
            this.getTreeListView().refresh();
            return;
        }
        String leafType = (String)currentNode.getData();
        HashSet appList = Sets.newHashSetWithExpectedSize((int)16);
        if ("cloud".equals(leafType)) {
            appNodeList = currentNode.getChildren();
            if (appNodeList != null && !appNodeList.isEmpty()) {
                for (TreeNode treeNode : appNodeList) {
                    if (null == treeNode || !StringUtils.isNotEmpty((CharSequence)treeNode.getId())) continue;
                    appList.add(treeNode.getId());
                }
            }
        } else if ("root".equals(leafType)) {
            appNodeList = Lists.newArrayListWithExpectedSize((int)16);
            cloudNodeList = this.getTreeModel().getRoot().getChildren();
            if (cloudNodeList != null && cloudNodeList.size() > 0) {
                for (TreeNode treeNode : cloudNodeList) {
                    if (null == treeNode || null == treeNode.getChildren()) continue;
                    appNodeList.addAll(treeNode.getChildren());
                }
                for (TreeNode treeNode : appNodeList) {
                    if (null == treeNode || !StringUtils.isNotEmpty((CharSequence)treeNode.getId())) continue;
                    appList.add(treeNode.getId());
                }
            }
        } else if (NODE_TYPE_SCENE.equals(leafType)) {
            qFilter = new QFilter("warnscene", "=", (Object)Long.parseLong(currentNodeId));
            evt.addCustomQFilter(qFilter);
        } else if (NODE_TYPE_BIZ_OBJ.equals(leafType)) {
            qFilter = new QFilter("warnbizobj", "=", (Object)currentNodeId);
            evt.addCustomQFilter(qFilter);
        } else {
            appList.add(currentNodeId);
        }
        if (!appList.isEmpty()) {
            DynamicObject[] objs = BusinessDataServiceHelper.load((String)BOS_DEVPORTAL_BIZAPP, (String)"id", (QFilter[])new QFilter[]{new QFilter("masterid", "in", (Object)appList)});
            if (objs.length > 0) {
                void var9_14;
                cloudNodeList = objs;
                int n = cloudNodeList.length;
                boolean bl = false;
                while (var9_14 < n) {
                    DynamicObject dynamicObject = cloudNodeList[var9_14];
                    appList.add(dynamicObject.getString("id"));
                    ++var9_14;
                }
            }
            QFilter qFilter2 = new QFilter(this.getBizAppId(), "in", (Object)appList);
            evt.addCustomQFilter(qFilter2);
        }
        if (StringUtils.isNotBlank((CharSequence)(hasPermSceneIds = this.getPageCache().get("hasPermSceneIds")))) {
            evt.addCustomQFilter(new QFilter("warnscene", "in", (Object)SerializationUtils.fromJsonStringToList((String)hasPermSceneIds, Long.class)).or(new QFilter("warntype", "=", (Object)"bizobj")));
        }
    }

    public QFilter getHRAppFilter(ListShowParameter listShowParameter) {
        this.filterKey = "bizapp";
        return super.getHRAppFilter(listShowParameter);
    }

    public void search(SearchEnterEvent evt) {
        try {
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
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    private void focusNode(TreeView treeView, TreeNode searchTreeNode) {
        treeView.showNode(searchTreeNode.getId());
        treeView.treeNodeClick(searchTreeNode.getParentid(), searchTreeNode.getId());
        treeView.checkNode(searchTreeNode);
        treeView.focusNode(searchTreeNode);
    }

    public void refreshNode(RefreshNodeEvent refreshNodeEvent) {
        try {
            super.initCloudAppTree();
            this.initChildrenNode(this.getTreeModel().getRoot());
            String nodeId = refreshNodeEvent.getNodeId().toString();
            TreeNode node = this.getTreeModel().getRoot().getTreeNode(nodeId, 15);
            if (node.getChildren() != null && node.getChildren().size() > 0) {
                node.setIsOpened(true);
            }
            this.focusNode(this.getTreeListView().getTreeView(), node);
            refreshNodeEvent.setChildNodes(node.getChildren());
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        block5: {
            try {
                super.beforeDoOperation(args);
                AbstractOperate op = (AbstractOperate)args.getSource();
                String operateKey = op.getOperateKey();
                if ("viewexelog".equals(operateKey)) {
                    ListSelectedRowCollection selectedRows = args.getListSelectedData();
                    ListView view = (ListView)this.getView();
                    int focusRow = view.getFocusRow();
                    for (ListSelectedRow selectedRow : selectedRows) {
                        int rowKey = selectedRow.getRowKey();
                        if (focusRow != rowKey) continue;
                        ListShowParameter listShowParameter = ShowFormHelper.createShowListForm((String)"hrcs_warnexeclog", (boolean)false, (int)0, (boolean)true);
                        listShowParameter.getListFilterParameter().setFilter(new QFilter("warnscheme", "=", selectedRow.getPrimaryKeyValue()));
                        view.showForm((FormShowParameter)listShowParameter);
                        break block5;
                    }
                    break block5;
                }
                if ("refresh".equals(operateKey)) {
                    this.getTreeModel().refreshNode((Object)this.getTreeModel().getRoot().getId());
                }
            }
            catch (Exception exception) {
                LOG.error("error:", (Throwable)exception);
            }
        }
    }
}
