/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ArrayUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.Donothing
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IPageCache
 *  kd.bos.form.OpenStyle
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.BillList
 *  kd.bos.list.ListShowParameter
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.operation.OperationServiceHelper
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin
 *  kd.hr.hies.common.HiesCommonRes
 */
package kd.hr.hies.formplugin;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ArrayUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.Donothing;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IPageCache;
import kd.bos.form.OpenStyle;
import kd.bos.form.ShowType;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.ListShowParameter;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin;
import kd.hr.hies.common.HiesCommonRes;

@ExcludeFromJacocoGeneratedReport
public final class SwitchRegistryTreeListPlugin
extends HRF7TreeListPlugin {
    private static final String BTN_NEW = "tblnew";
    private static final String BTN_ENABLEOLD = "enableold";
    private static final String BTN_ENABLENEW = "enablenew";
    private static final String CLOSECALLBACK_SAVE = "closeCallBack_save";

    public String getBizAppId() {
        return "bizapp";
    }

    public void initializeTree(EventObject evt) {
        ListShowParameter listShowParameter = (ListShowParameter)this.getView().getFormShowParameter();
        listShowParameter.setCustomParam("showInvisible", (Object)Boolean.TRUE);
        super.initCloudAppTree();
    }

    public void initialize() {
        super.initialize();
        this.addClickListeners(new String[]{BTN_NEW});
        long userId = RequestContext.get().getCurrUserId();
        String appId = "hies";
        String entityName = "hies_switchregistry";
        boolean hasNewPermission = PermissionServiceHelper.checkPermission((Long)userId, (String)appId, (String)entityName, (String)"47156aff000000ac");
        boolean hasDelPermission = PermissionServiceHelper.checkPermission((Long)userId, (String)appId, (String)entityName, (String)"4715e1f1000000ac");
        this.getView().setVisible(Boolean.valueOf(hasNewPermission), new String[]{BTN_NEW});
        this.getView().setVisible(Boolean.valueOf(hasDelPermission), new String[]{"tbldel"});
    }

    public void search(SearchEnterEvent evt) {
        List treeNodes;
        String searchText = evt.getText();
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
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5df2\u5b8c\u6210\u641c\u7d22\uff0c\u6ca1\u6709\u627e\u5230\u641c\u7d22\u9879\u3002", (String)HiesCommonRes.SwitchRegistryTreeListPlugin_0.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
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

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        if (CLOSECALLBACK_SAVE.equals(actionId)) {
            this.getView().invokeOperation("refresh");
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        BillList billList = (BillList)this.getView().getControl("billlistap");
        ListSelectedRowCollection listSelectRows = billList.getSelectedRows();
        HRBaseServiceHelper baseServiceHelper = new HRBaseServiceHelper("hies_switchregistry");
        Object[] ids = listSelectRows.getPrimaryKeyValues();
        Object source = args.getSource();
        if (source instanceof Donothing) {
            String opKey;
            Donothing operation = (Donothing)source;
            switch (opKey = operation.getOperateKey()) {
                case "enablenew": {
                    if (ArrayUtils.isEmpty((Object[])ids)) {
                        return;
                    }
                    DynamicObject[] dyns = baseServiceHelper.query("id,name,enablestatus,status,enable,issyspreset,oristatus", new QFilter[]{new QFilter("id", "in", (Object)ids)});
                    ArrayList<DynamicObject> updDyns = new ArrayList<DynamicObject>();
                    ArrayList msgs = Lists.newArrayListWithCapacity((int)16);
                    for (DynamicObject dynamicObject : dyns) {
                        String enablestatus = dynamicObject.getString("enablestatus");
                        if ("0".equals(enablestatus)) {
                            msgs.add(dynamicObject.getString("name"));
                            continue;
                        }
                        dynamicObject.set("enablestatus", (Object)"0");
                        updDyns.add(dynamicObject);
                    }
                    if (!CollectionUtils.isEmpty(updDyns)) {
                        OperateOption operateOption = OperateOption.create();
                        operateOption.setVariableValue("ishasright", "true");
                        operateOption.setVariableValue("skipCheckDataPermission", "true");
                        OperationServiceHelper.executeOperate((String)"save", (String)"hies_switchregistry", (DynamicObject[])updDyns.toArray(new DynamicObject[0]), (OperateOption)operateOption);
                    }
                    ListView listView = (ListView)this.getView();
                    listView.refresh();
                    if (!CollectionUtils.isEmpty((Collection)msgs)) {
                        return;
                    }
                    this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u542f\u7528\u65b0\u7248\u6210\u529f\u3002", (String)HiesCommonRes.SwitchRegistryTreeListPlugin_3.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
                    args.setCancel(true);
                    break;
                }
                case "enableold": {
                    if (ArrayUtils.isEmpty((Object[])ids)) {
                        return;
                    }
                    DynamicObject[] query2 = baseServiceHelper.query("id,name,enablestatus,status,enable,issyspreset,oristatus", new QFilter[]{new QFilter("id", "in", (Object)ids)});
                    ArrayList<DynamicObject> updDyns2 = new ArrayList<DynamicObject>();
                    ArrayList msgs2 = Lists.newArrayListWithCapacity((int)16);
                    for (DynamicObject dynamicObject : query2) {
                        String enablestatus = dynamicObject.getString("enablestatus");
                        if ("1".equals(enablestatus)) {
                            msgs2.add(dynamicObject.getString("name"));
                            continue;
                        }
                        dynamicObject.set("enablestatus", (Object)"1");
                        updDyns2.add(dynamicObject);
                    }
                    if (!CollectionUtils.isEmpty(updDyns2)) {
                        OperateOption operateOption = OperateOption.create();
                        operateOption.setVariableValue("ishasright", "true");
                        operateOption.setVariableValue("skipCheckDataPermission", "true");
                        OperationServiceHelper.executeOperate((String)"save", (String)"hies_switchregistry", (DynamicObject[])updDyns2.toArray(new DynamicObject[0]), (OperateOption)operateOption);
                    }
                    ListView listView2 = (ListView)this.getView();
                    listView2.refresh();
                    if (!CollectionUtils.isEmpty((Collection)msgs2)) {
                        return;
                    }
                    this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u542f\u7528\u65e7\u7248\u6210\u529f\u3002", (String)HiesCommonRes.SwitchRegistryTreeListPlugin_4.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
                    args.setCancel(true);
                    break;
                }
            }
        }
    }

    public void itemClick(ItemClickEvent evt) {
        switch (evt.getItemKey()) {
            case "tblnew": {
                BillShowParameter parameter = new BillShowParameter();
                parameter.setFormId("hies_switchregistry");
                OpenStyle openStyle = parameter.getOpenStyle();
                openStyle.setShowType(ShowType.Modal);
                parameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CLOSECALLBACK_SAVE));
                this.getView().showForm((FormShowParameter)parameter);
                break;
            }
        }
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        if ("number".equals(args.getFieldName())) {
            args.setCancel(true);
            BillShowParameter parameter = new BillShowParameter();
            parameter.setFormId("hies_switchregistry");
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
}
