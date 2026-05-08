/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DataEntityBase
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.utils.ArrayUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.events.BizDataEventArgs
 *  kd.bos.entity.operate.Save
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.filter.FilterContainer
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IPageCache
 *  kd.bos.form.OpenStyle
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerSearchClickArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.OpBizRuleSetServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hies.business.HRBizAppHelper
 *  kd.hr.hies.common.util.TemplateUtil
 *  kd.hr.hies.formplugin.HRTreeListBizAppsPlugin
 *  org.apache.commons.collections4.CollectionUtils
 */
package kd.hr.expt.formplugin;

import java.util.ArrayList;
import java.util.Arrays;
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
import kd.bos.dataentity.entity.DataEntityBase;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.ArrayUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.events.BizDataEventArgs;
import kd.bos.entity.operate.Save;
import kd.bos.entity.tree.TreeNode;
import kd.bos.filter.FilterContainer;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IPageCache;
import kd.bos.form.OpenStyle;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerSearchClickArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.OpBizRuleSetServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hies.business.HRBizAppHelper;
import kd.hr.hies.common.util.TemplateUtil;
import kd.hr.hies.formplugin.HRTreeListBizAppsPlugin;
import org.apache.commons.collections4.CollectionUtils;

public final class EntityEncryptTreeListEdit
extends HRTreeListBizAppsPlugin
implements BeforeFilterF7SelectListener {
    private static final Log logger = LogFactory.getLog(EntityEncryptTreeListEdit.class);
    private static final String NEW = "tblnew";
    private static final String DEL = "tbldel";
    private static final String BIZOBJECT = "entity";
    private static final String CLOSECALLBACK_SAVE = "closeCallBack_save";
    private static final HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbss_cloud");

    protected QFilter getEntityFilter() {
        return EntityEncryptTreeListEdit.getEntityEncryptIdFilter();
    }

    public static QFilter getEntityEncryptIdFilter() {
        Object[] entityEncryptObjects = BusinessDataServiceHelper.load((String)"hies_entityencryptconf", (String)BIZOBJECT, (QFilter[])new QFilter[0]);
        if (ArrayUtils.isNotEmpty((Object[])entityEncryptObjects)) {
            Set entityIdSet = Arrays.stream(entityEncryptObjects).map(o -> o.getDynamicObject(BIZOBJECT)).filter(Objects::nonNull).map(DataEntityBase::getPkValue).collect(Collectors.toSet());
            return new QFilter("id", "in", entityIdSet);
        }
        return null;
    }

    public void initTreeToolbar(EventObject e) {
        super.initTreeToolbar(e);
        this.getView().setVisible(Boolean.FALSE, new String[]{"btnnew", "btnedit", "btndel"});
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
    }

    public void initialize() {
        super.initialize();
    }

    public void createNewData(BizDataEventArgs e) {
        super.createNewData(e);
        this.addClickListeners(new String[]{"btnnew"});
    }

    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
    }

    public void initializeTree(EventObject e) {
        super.initializeTree(e);
    }

    public void click(EventObject evt) {
        super.click(evt);
    }

    public void itemClick(ItemClickEvent evt) {
        switch (evt.getItemKey()) {
            case "tblnew": {
                BillShowParameter parameter = new BillShowParameter();
                parameter.setFormId("hies_entityencryptconf");
                OpenStyle openStyle = parameter.getOpenStyle();
                openStyle.setShowType(ShowType.Modal);
                parameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CLOSECALLBACK_SAVE));
                this.getView().showForm((FormShowParameter)parameter);
            }
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        super.beforeItemClick(evt);
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
    }

    public void buildTreeListFilter(BuildTreeListFilterEvent e) {
        String nodeId = e.getNodeId().toString();
        TreeNode root = this.getTreeModel().getRoot();
        TreeNode node = root.getTreeNode(nodeId, 2);
        if (!Objects.isNull(node)) {
            e.addQFilter(this.getFilter(node));
        }
        e.setCancel(true);
    }

    public void refreshNode(RefreshNodeEvent event) {
        String nodeId = event.getNodeId().toString();
        TreeNode root = this.getTreeModel().getRoot();
        TreeNode parent = root.getTreeNode(nodeId, 2);
        List childNodes = this.getChildNodes(root, parent);
        event.setChildNodes(childNodes);
        if (parent == null) {
            this.getTreeListView().getTreeView().expand(nodeId);
        }
    }

    protected List<TreeNode> getBillNodesBy(List<String> appIds) {
        String fields = "id, number, name, bizappid";
        QFilter appIdFilter = new QFilter("bizappid", "in", appIds);
        QFilter[] validEntityNumberFilter = TemplateUtil.getValidEntityNumberFilter((boolean)true);
        QFilter entityIdFilter = null;
        Object[] entityEncryptObjects = BusinessDataServiceHelper.load((String)"hies_entityencryptconf", (String)BIZOBJECT, (QFilter[])new QFilter[0]);
        if (ArrayUtils.isNotEmpty((Object[])entityEncryptObjects)) {
            Set entityIdSet = Arrays.stream(entityEncryptObjects).map(o -> o.getDynamicObject(BIZOBJECT).getPkValue()).collect(Collectors.toSet());
            entityIdFilter = new QFilter("id", "in", entityIdSet);
        }
        List<QFilter> filters = Arrays.stream(validEntityNumberFilter).collect(Collectors.toList());
        filters.add(appIdFilter);
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

    public List<TreeNode> getAppNodesBy(String cloudNodeId, boolean isShowEntity) {
        List cloudNodes = this.getCloudNodes();
        List currCloud = cloudNodes.stream().filter(cloud -> cloud.getId().equals(cloudNodeId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(currCloud)) {
            return new ArrayList<TreeNode>(0);
        }
        List apps = ((TreeNode)currCloud.get(0)).getChildren();
        if (apps == null || apps.isEmpty()) {
            return new ArrayList<TreeNode>(0);
        }
        return apps;
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
        return null;
    }

    public void beforeShowBill(BeforeShowBillFormEvent e) {
        super.beforeShowBill(e);
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
        super.treeNodeClick(treenodeevent);
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        if ("entity_number".equals(args.getFieldName())) {
            args.setCancel(true);
            BillShowParameter parameter = new BillShowParameter();
            parameter.setFormId("hies_entityencryptconf");
            parameter.setStatus(OperationStatus.VIEW);
            parameter.setBillStatus(BillOperationStatus.VIEW);
            parameter.setPkId(this.getFocusRowPkId());
            OpenStyle openStyle = parameter.getOpenStyle();
            openStyle.setShowType(ShowType.Modal);
            parameter.setCustomParam("isView", (Object)Boolean.TRUE.toString());
            parameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CLOSECALLBACK_SAVE));
            this.getView().showForm((FormShowParameter)parameter);
        }
    }

    public void search(SearchEnterEvent evt) {
        String searchText = evt.getText();
        TreeNode root = this.getTreeModel().getRoot();
        IPageCache pageCache = (IPageCache)this.getView().getService(IPageCache.class);
        String pageId = this.getView().getPageId();
        String matchNodesCacheKey = pageId + "_matchNodes";
        String oldSearchTextCacheKey = pageId + "_oldSearchText";
        String oldSearchText = pageCache.get(oldSearchTextCacheKey);
        String matchNodesCache = pageCache.get(matchNodesCacheKey);
        if (oldSearchText != null && !oldSearchText.equals(searchText) || StringUtils.isBlank((CharSequence)matchNodesCache)) {
            pageCache.put(pageId + "_SHOWLEVEL", "1");
            Map apps = this.searchApp(searchText);
            Set clouds = HRBizAppHelper.searchCloud((TreeNode)root, (String)searchText);
            clouds.addAll(apps.values());
        }
        super.search(evt);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        if (CLOSECALLBACK_SAVE.equals(actionId)) {
            this.getView().invokeOperation("refresh");
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        String operateKey;
        super.afterDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "delete": {
                this.getView().invokeOperation("refresh");
                break;
            }
        }
        FormOperate formOperate = (FormOperate)args.getSource();
        if (formOperate instanceof Save && StringUtils.equals((CharSequence)"OPKEY_SAVE", (CharSequence)formOperate.getOperateKey()) && args.getOperationResult() != null && args.getOperationResult().isSuccess()) {
            OpBizRuleSetServiceHelper.clearCache();
            this.getView().invokeOperation("refresh");
        }
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
    }

    public void filterContainerSearchClick(FilterContainerSearchClickArgs args) {
        super.filterContainerSearchClick(args);
    }

    public void beforeF7Select(BeforeFilterF7SelectEvent evt) {
        Set<String> hrCloud;
        if ("app.bizcloud.name".equals(evt.getFieldName()) && CollectionUtils.isNotEmpty(hrCloud = this.getHrCloud())) {
            evt.getQfilters().add(new QFilter("id", "in", hrCloud));
        }
    }

    private Set<String> getHrCloud() {
        Object[] dyns = serviceHelper.query("id,cloud,cloud.id,cloud.name,index", new QFilter[0]);
        if (ArrayUtils.isNotEmpty((Object[])dyns)) {
            return Arrays.stream(dyns).map(item -> item.getString("cloud.id")).collect(Collectors.toSet());
        }
        return new HashSet<String>(0);
    }

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        FilterContainer filterContainer = (FilterContainer)this.getView().getControl("filtercontainerap");
        filterContainer.addBeforeF7SelectListener((BeforeFilterF7SelectListener)this);
    }
}
