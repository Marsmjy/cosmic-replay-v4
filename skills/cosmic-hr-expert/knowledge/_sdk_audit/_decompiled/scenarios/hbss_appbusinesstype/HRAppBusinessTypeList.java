/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.list.plugin.StandardTreeListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.devportal.AppMetadata
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.devportal.AppMetaServiceHelper
 *  kd.bos.servicehelper.devportal.BizAppServiceHelp
 *  kd.bos.servicehelper.devportal.BizCloudServiceHelp
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 */
package kd.hr.hbss.formplugin.web.hrbu;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.list.plugin.StandardTreeListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.devportal.AppMetadata;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.devportal.AppMetaServiceHelper;
import kd.bos.servicehelper.devportal.BizAppServiceHelp;
import kd.bos.servicehelper.devportal.BizCloudServiceHelp;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;

public final class HRAppBusinessTypeList
extends StandardTreeListPlugin {
    private static final Log LOGGER = LogFactory.getLog(HRAppBusinessTypeList.class);
    private static final String BTN_NEW = "btnnew";
    private static final String BTN_EDIT = "btnedit";
    private static final String BTN_DEL = "btndel";
    private static final String TREE_ROOT = "-1";

    @ExcludeFromJacocoGeneratedReport
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate source = (FormOperate)args.getSource();
        String operateKey = source.getOperateKey();
        if ("donothing".equals(operateKey)) {
            ListSelectedRowCollection listSelectedData = ((FormOperate)args.getSource()).getListSelectedData();
            if (listSelectedData.size() > 1) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u4e0d\u652f\u6301\u6279\u91cf\u590d\u5236\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u6570\u636e\u3002", (String)"HRAppBusinessTypeList_0", (String)"hrmp-hbss-formplugin", (Object[])new Object[0]));
                return;
            }
            BaseShowParameter formShowParameter = new BaseShowParameter();
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
            formShowParameter.setStatus(OperationStatus.ADDNEW);
            formShowParameter.setFormId("hbss_appbusinesstype");
            formShowParameter.setCustomParam("primaryKeyValue", listSelectedData.get(0).getPrimaryKeyValue());
            this.getView().showForm((FormShowParameter)formShowParameter);
        }
    }

    public void initialize() {
        super.initialize();
        this.getView().setVisible(Boolean.FALSE, new String[]{BTN_NEW, BTN_EDIT, BTN_DEL, "iscontainlower"});
    }

    public void initializeTree(EventObject evt) {
        this.intiTree();
    }

    public void refreshNode(RefreshNodeEvent evt) {
        List<TreeNode> childNodes = this.getChildNodes(evt.getNodeId());
        evt.setChildNodes(childNodes);
        this.getView().setVisible(Boolean.FALSE, new String[]{BTN_NEW, BTN_EDIT, BTN_DEL, "iscontainlower"});
    }

    public void buildTreeListFilter(BuildTreeListFilterEvent nodeEvent) {
        super.buildTreeListFilter(nodeEvent);
        String nodeId = nodeEvent.getNodeId().toString();
        if (!TREE_ROOT.equals(nodeId)) {
            HashSet nodeIds = Sets.newHashSetWithExpectedSize((int)16);
            nodeIds.add(nodeId);
            List<TreeNode> appNodes = this.getAppNodes(nodeId);
            appNodes.forEach(node -> nodeIds.add(node.getId()));
            QFilter qFilter = new QFilter("app", "in", (Object)nodeIds);
            nodeEvent.addQFilter(qFilter);
        }
        nodeEvent.setCancel(true);
    }

    private void intiTree() {
        TreeNode root = new TreeNode();
        root.setText(ResManager.loadKDString((String)"\u5168\u90e8\u4e1a\u52a1\u4e91", (String)"HRAppBusinessTypeList_1", (String)"hrmp-hbss-formplugin", (Object[])new Object[0]));
        root.setParentid("");
        root.setId(TREE_ROOT);
        root.setIsOpened(true);
        List<TreeNode> cloudNodes = this.getCloudNodes();
        root.addChildren(cloudNodes);
        this.getPageCache().put("treenodecache", SerializationUtils.toJsonString((Object)root));
        this.getTreeModel().setRoot(root);
        this.getTreeModel().setCurrentNodeId((Object)root.getId());
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

    private List<TreeNode> getAppNodes(String cloudId) {
        ArrayList<TreeNode> appNodes = new ArrayList<TreeNode>();
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        String pageType = (String)formShowParameter.getCustomParam("pagetype");
        if (pageType != null && "extend".equals(pageType)) {
            String refAppId = (String)formShowParameter.getCustomParam("refappid");
            AppMetadata refAppMeta = AppMetaServiceHelper.loadAppMetadataFromCacheById((String)refAppId, (boolean)false);
            String refAppName = refAppMeta.getName().getLocaleValue();
            TreeNode node = new TreeNode(cloudId, refAppId, refAppName);
            appNodes.add(node);
        } else {
            DynamicObjectCollection dynamicObjects = BizAppServiceHelp.getAllBizAppsByCloudID((String)cloudId);
            for (DynamicObject dynamicObject : dynamicObjects) {
                TreeNode node = new TreeNode();
                String nodeId = dynamicObject.getString("id");
                String masterId = dynamicObject.getString("masterid");
                node.setId(nodeId);
                node.setText(dynamicObject.getString("name"));
                node.setParentid(cloudId);
                node.setData((Object)masterId);
                appNodes.add(node);
            }
        }
        return appNodes;
    }

    private List<TreeNode> getCloudNodes() {
        ArrayList<TreeNode> cloudNode = new ArrayList<TreeNode>();
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        String pageType = (String)formShowParameter.getCustomParam("pagetype");
        if ("extend".equals(pageType)) {
            String refAppId = (String)formShowParameter.getCustomParam("refappid");
            try {
                DynamicObject cloudObject = BizCloudServiceHelp.getBizCloudByAppID((String)refAppId);
                TreeNode node = new TreeNode(TREE_ROOT, cloudObject.getString("id"), cloudObject.getLocaleString("name").getLocaleValue());
                cloudNode.add(node);
            }
            catch (Exception e) {
                LOGGER.error("appId is wrong , please check appId", (Throwable)e);
            }
        } else {
            HRBaseServiceHelper cloudHelper = new HRBaseServiceHelper("hbss_cloud");
            DynamicObject[] clouds = cloudHelper.query("cloud,index", new QFilter[0], "index asc");
            List cloudList = Arrays.stream(clouds).map(el -> el.getDynamicObject("cloud")).collect(Collectors.toList());
            for (DynamicObject dy : cloudList) {
                TreeNode node = new TreeNode();
                if (Objects.isNull(dy) || Objects.isNull(dy.getString("id")) || Objects.isNull(dy.getString("name"))) continue;
                node.setId(dy.getString("id"));
                node.setText(dy.getString("name"));
                node.setParentid(TREE_ROOT);
                cloudNode.add(node);
            }
        }
        return cloudNode;
    }
}
