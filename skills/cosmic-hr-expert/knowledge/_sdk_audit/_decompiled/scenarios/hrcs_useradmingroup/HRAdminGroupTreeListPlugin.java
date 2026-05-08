/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.db.DB
 *  kd.bos.db.DBRoute
 *  kd.bos.db.ResultSetHandler
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.exception.KDBizException
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IPageCache
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.Search
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.SearchEnterListener
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.ChatEvent
 *  kd.bos.list.plugin.AbstractTreeListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.cache.util.PermCommonUtil
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.operation.OperationServiceHelper
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.business.service.diff.HRPluginProxy
 *  kd.hr.hbp.business.service.perm.HRAdminService
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr
 *  kd.hr.hrcs.bussiness.service.perm.hradmin.HRAdminGroupService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.PermDBServiceHelper
 *  kd.hr.hrcs.common.constants.perm.HRAdminConstant
 *  kd.sdk.hr.hbp.business.extpoint.permission.hradmi.AddCustomUserEventArgs
 *  kd.sdk.hr.hbp.business.extpoint.permission.hradmi.IAdminGroupListSubExtPlugin
 */
package kd.hr.hrcs.formplugin.web.perm.hradmin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.db.DB;
import kd.bos.db.DBRoute;
import kd.bos.db.ResultSetHandler;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.tree.TreeNode;
import kd.bos.exception.KDBizException;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IPageCache;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.control.Search;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.SearchEnterListener;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.ChatEvent;
import kd.bos.list.plugin.AbstractTreeListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.cache.util.PermCommonUtil;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.bos.util.StringUtils;
import kd.hr.hbp.business.service.diff.HRPluginProxy;
import kd.hr.hbp.business.service.perm.HRAdminService;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr;
import kd.hr.hrcs.bussiness.service.perm.hradmin.HRAdminGroupService;
import kd.hr.hrcs.bussiness.servicehelper.perm.PermDBServiceHelper;
import kd.hr.hrcs.common.constants.perm.HRAdminConstant;
import kd.sdk.hr.hbp.business.extpoint.permission.hradmi.AddCustomUserEventArgs;
import kd.sdk.hr.hbp.business.extpoint.permission.hradmi.IAdminGroupListSubExtPlugin;

public final class HRAdminGroupTreeListPlugin
extends AbstractTreeListPlugin
implements IAdminGroupListSubExtPlugin,
HRAdminConstant {
    private static final Log logger = LogFactory.getLog(HRAdminGroupTreeListPlugin.class);
    public static final String BOS_USERTREELISTF7 = "bos_usertreelistf7";
    private static final String TREE_ADMIN_GROUP = "treeview";
    private static final String BTN_NEW = "btnnew";
    private static final String BTN_EDIT = "btnedit";
    private static final String BTN_DEL = "btndel";
    private static final String BTN_SEARCH = "searchap";
    private static final String CONFIRM_CALLBACK_DELETE_ADMIN_GROUP = "confirm_callBack_delete_admin_group";

    public void registerListener(EventObject e) {
        super.registerListener(e);
        Search search = (Search)this.getControl(BTN_SEARCH);
        search.addEnterListener((SearchEnterListener)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void initializeTree(EventObject e) {
        super.initializeTree(e);
        TreeNode root = null;
        try {
            root = HRAdminGroupService.initAdminGroupTree((TreeView)((TreeView)this.getControl(TREE_ADMIN_GROUP)), (IPageCache)this.getView().getPageCache());
        }
        catch (Exception ex) {
            this.getView().showErrorNotification(ex.getMessage());
            logger.error((Throwable)ex);
            return;
        }
        this.getTreeModel().setRoot(root);
        this.getTreeModel().setCurrentNodeId((Object)root.getId());
        this.getView().setVisible(Boolean.FALSE, new String[]{"cardview"});
    }

    public void initTreeToolbar(EventObject e) {
        this.getView().setVisible(Boolean.TRUE, new String[]{BTN_NEW, BTN_EDIT, BTN_DEL});
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs evt) {
        String opKey;
        super.beforeDoOperation(evt);
        if (!(evt.getSource() instanceof FormOperate)) {
            return;
        }
        if (!HRAdminService.isHrAdmin()) {
            throw new KDBizException(ResManager.loadKDString((String)"\u60a8\u65e0\u6cd5\u8bbf\u95ee\u8be5\u529f\u80fd\uff0c\u56e0\u4e3a\u60a8\u4e0d\u662fHR\u9886\u57df\u7ba1\u7406\u5458\u3002", (String)"HRAdminStrictPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        }
        switch (opKey = ((FormOperate)evt.getSource()).getOperateKey()) {
            case "donothing_add_user": {
                this.showUserF7TreeList();
                break;
            }
            case "donothing_remove_user": {
                List superiorGroupId;
                IPageCache pageCache = this.getPageCache();
                TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
                String focusNodeId = tv.getTreeState().getFocusNodeId();
                String adminGroupId = focusNodeId.split("_")[0];
                if ("8609760E-EF83-4775-A9FF-CCDEC7C0B689".equalsIgnoreCase(adminGroupId) || !(superiorGroupId = (List)SerializationUtils.fromJsonString((String)pageCache.get("superiorGroupIds"), List.class)).contains(Long.valueOf(adminGroupId))) break;
                this.getView().showTipNotification(this.getOnlyModifySubGroupText(), Integer.valueOf(2000));
                evt.setCancel(true);
                break;
            }
            case "donothing_add_group": {
                this.adminGroupTreeAddOperation();
                break;
            }
            case "donothing_modify_group": {
                this.adminGroupTreeModifyOperation();
                break;
            }
            case "donothing_remove_group": {
                this.adminGroupTreeRemoveOperation();
                break;
            }
            case "donothing_batch_perm": {
                this.adminGroupTreeBatchPermOperation();
                break;
            }
            case "refresh": {
                this.refresh();
                break;
            }
        }
    }

    private void adminGroupTreeBatchPermOperation() {
        TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
        String focusNodeId = tv.getTreeState().getFocusNodeId();
        String[] arr = focusNodeId.split("_");
        String adminGroupId = arr[0];
        if (!this.verifyBatchAuth(this.getPageCache(), adminGroupId)) {
            return;
        }
        FormShowParameter bsp = new FormShowParameter();
        bsp.setFormId("hrcs_amingroupbatchauth");
        bsp.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        bsp.getCustomParams().put("adminGroupParentId", adminGroupId);
        bsp.getCustomParams().put("level", arr[1]);
        bsp.getCustomParams().put("type", "batchAuth");
        this.getView().showForm(bsp);
    }

    private void adminGroupTreeRemoveOperation() {
        TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
        Map focusNode = tv.getTreeState().getFocusNode();
        String focusNodeId = (String)focusNode.get("id");
        if ("8609760E-EF83-4775-A9FF-CCDEC7C0B689".equals(focusNodeId)) {
            this.getView().showTipNotification(this.getNotRootNodeText());
            return;
        }
        String adminGroupIdStr = ((String)focusNode.get("id")).split("_")[0];
        Long adminGroupId = Long.parseLong(adminGroupIdStr);
        if (!this.verifyAdminGroup(this.getPageCache(), adminGroupIdStr)) {
            return;
        }
        String name = (String)focusNode.get("text");
        DynamicObject subAdminGroup = BusinessDataServiceHelper.loadSingle((String)"perm_admingroup", (String)"id", (QFilter[])new QFilter[]{new QFilter("parent", "=", (Object)adminGroupId)});
        if (subAdminGroup != null) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5220\u9664\u5931\u8d25\uff0c\u201c%s\u201d\u5b58\u5728\u4e0b\u7ea7\u5206\u7ec4\u3002", (String)"AdminGroupTreeListPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{name}));
            return;
        }
        DynamicObject user = BusinessDataServiceHelper.loadSingle((String)"hrcs_useradmingroup", (String)"id", (QFilter[])new QFilter[]{new QFilter("usergroup", "=", (Object)adminGroupId)});
        if (user != null) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u5220\u9664\u7ba1\u7406\u5458\u5206\u7ec4\u201c%s\u201d\uff0c\u8be5\u7ec4\u4e0b\u5b58\u5728\u7528\u6237\u3002", (String)"AdminGroupTreeListPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{name}));
            return;
        }
        if (this.checkHasRoleRef(adminGroupId, name)) {
            return;
        }
        this.getView().showConfirm(ResManager.loadKDString((String)"\u5220\u9664\u7ba1\u7406\u5458\u7ec4\u540e\uff0c\u5c06\u540c\u6b65\u5220\u9664\u7ba1\u7406\u5458\u7ec4\u7684\u6388\u6743\u8303\u56f4\u3002\u786e\u8ba4\u5220\u9664\u5417\uff1f", (String)"HRAdminGroupTreeListPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{name}), MessageBoxOptions.OKCancel, new ConfirmCallBackListener(CONFIRM_CALLBACK_DELETE_ADMIN_GROUP));
    }

    private void adminGroupTreeModifyOperation() {
        FormShowParameter bsp = new FormShowParameter();
        bsp.setFormId("hrcs_admingroupdetail");
        bsp.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        bsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "hrcs_admingroupdetail"));
        TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
        String focusNodeId = tv.getTreeState().getFocusNodeId();
        IPageCache pageCache = this.getPageCache();
        if (StringUtils.isEmpty((String)focusNodeId)) {
            focusNodeId = pageCache.get("focusNodeId");
        }
        String parentNodeId = pageCache.get("focusNodeParentId");
        String[] arr = focusNodeId.split("_");
        String adminGroupId = arr[0];
        if (!this.verifyAdminGroup(pageCache, adminGroupId)) {
            return;
        }
        int level = Integer.parseInt(arr[1]);
        String newPageId = adminGroupId + "|" + this.getView().getPageId();
        bsp.setPageId(newPageId);
        bsp.getCustomParams().put("adminGroupId", adminGroupId);
        String adminGroupParentId = "";
        adminGroupParentId = parentNodeId.contains("_") ? parentNodeId.substring(0, parentNodeId.indexOf(95)) : parentNodeId;
        if (level == 1) {
            adminGroupParentId = "0";
        }
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("perm_admingroup");
        DynamicObject adminGroup = serviceHelper.queryOne("number,name,description", new QFilter("id", "=", (Object)Long.valueOf(adminGroupId)));
        bsp.setCaption(adminGroup.getLocaleString("name").getLocaleValue());
        bsp.getCustomParams().put("adminGroupParentId", adminGroupParentId);
        bsp.getCustomParams().put("level", String.valueOf(level));
        if (!"8609760E-EF83-4775-A9FF-CCDEC7C0B689".equalsIgnoreCase(adminGroupId)) {
            List superiorGroupId = (List)SerializationUtils.fromJsonString((String)pageCache.get("superiorGroupIds"), List.class);
            if (superiorGroupId.contains(Long.valueOf(adminGroupId))) {
                bsp.getCustomParams().put("viewstatus", "1");
                bsp.setStatus(OperationStatus.VIEW);
            } else {
                bsp.getCustomParams().put("viewstatus", "0");
                bsp.setStatus(OperationStatus.EDIT);
            }
        }
        this.getView().showForm(bsp);
    }

    private void adminGroupTreeAddOperation() {
        FormShowParameter bsp = new FormShowParameter();
        bsp.setFormId("hrcs_admingroupdetail");
        bsp.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        bsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "hrcs_admingroupdetail"));
        TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
        String focusNodeId = tv.getTreeState().getFocusNodeId();
        IPageCache pageCache = this.getPageCache();
        if (StringUtils.isEmpty((String)focusNodeId)) {
            focusNodeId = pageCache.get("focusNodeId");
        }
        String parentNodeId = pageCache.get("focusNodeParentId");
        String[] arr = focusNodeId.split("_");
        String adminGroupId = arr[0];
        if (!this.verifyAdminGroup(pageCache, adminGroupId)) {
            return;
        }
        int level = Integer.parseInt(arr[1]);
        if (level >= PermCommonUtil.getAdminLevelLimit()) {
            this.getView().showTipNotification(this.getLevelLimitText(PermCommonUtil.getAdminLevelLimit()));
            return;
        }
        bsp.getCustomParams().put("adminGroupId", "");
        bsp.getCustomParams().put("adminGroupParentId", adminGroupId);
        bsp.getCustomParams().put("level", String.valueOf(level + 1));
        bsp.getCustomParams().put("viewstatus", "0");
        bsp.setStatus(OperationStatus.ADDNEW);
        this.getView().showForm(bsp);
    }

    @ExcludeFromJacocoGeneratedReport
    public void afterDoOperation(AfterDoOperationEventArgs e) {
        String opKey;
        super.afterDoOperation(e);
        if (e.getSource() instanceof FormOperate && "donothing_remove_user".equals(opKey = ((FormOperate)e.getSource()).getOperateKey()) && e.getOperationResult().isSuccess()) {
            List successPkIds = e.getOperationResult().getSuccessPkIds();
            TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
            String focusNodeIdStr = tv.getTreeState().getFocusNodeId();
            long focusNodeId = 0L;
            if (!"8609760E-EF83-4775-A9FF-CCDEC7C0B689".equals(focusNodeIdStr)) {
                String[] arr = focusNodeIdStr.split("_");
                focusNodeId = Long.parseLong(arr[0]);
            }
            OperateOption operateOption = OperateOption.create();
            operateOption.setVariableValue("focusNodeId", focusNodeIdStr);
            operateOption.setVariableValue("superiorGroupIds", this.getPageCache().get("superiorGroupIds"));
            operateOption.setVariableValue("focusAdgNumber", this.getPageCache().get("focusAdgNumber"));
            operateOption.setVariableValue("focusAdgName", this.getPageCache().get("focusAdgName"));
            operateOption.setVariableValue("appId", this.getView().getFormShowParameter().getAppId());
            operateOption.setVariableValue("tag_of_view", Boolean.TRUE.toString());
            DynamicObject[] dynamicObjects = new DynamicObject[successPkIds.size()];
            int i = 0;
            for (Object id : successPkIds) {
                DynamicObject dynamicObject = BusinessDataServiceHelper.newDynamicObject((String)"hrcs_useradmingroup");
                dynamicObject.set("id", id);
                DynamicObject adminGroup = BusinessDataServiceHelper.newDynamicObject((String)"perm_admingroup");
                adminGroup.set("id", (Object)focusNodeId);
                dynamicObject.set("usergroup", (Object)adminGroup);
                dynamicObjects[i] = dynamicObject;
                ++i;
            }
            OperationResult operationResult = OperationServiceHelper.executeOperate((String)"do_remove_user", (String)"hrcs_useradmingroup", (DynamicObject[])dynamicObjects, (OperateOption)operateOption);
            if (operationResult.isSuccess()) {
                Map customData = operationResult.getCustomData();
                if (Boolean.FALSE.toString().equals(customData.get("delAll"))) {
                    String count = (String)customData.get("count");
                    String notDelData = (String)customData.get("notDelData");
                    this.getView().showMessage(String.format(ResManager.loadKDString((String)"\u6210\u529f\u5220\u9664%1$s\u6761\u6570\u636e\uff0c\u4ee5\u4e0b\u5206\u7ec4\u4e0d\u5728\u60a8\u7684\u7ba1\u63a7\u8303\u56f4\u5185\uff0c\u65e0\u6743\u7ef4\u62a4\u5f53\u524d\u5206\u7ec4\u7684\u7ba1\u7406\u5458\uff0c\u8bf7\u9009\u62e9\u5176\u4ed6\u7ba1\u7406\u5458\u7ec4\uff1a\n%2$s", (String)"AdminGroupTreeListPlugin_12", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), Integer.parseInt(count), notDelData));
                } else {
                    this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f\u3002", (String)"AdminGroupTreeListPlugin_6", (String)"bos-permission-formplugin", (Object[])new Object[0]));
                }
                this.clearCache();
                this.refresh();
            } else {
                this.getView().showErrorNotification(operationResult.getMessage());
            }
        }
    }

    private void refresh() {
        try {
            TreeView tv = (TreeView)this.getView().getControl(TREE_ADMIN_GROUP);
            HRAdminGroupService.initAdminGroupTree((TreeView)tv, (IPageCache)this.getPageCache());
        }
        catch (Exception var12) {
            this.getView().showErrorNotification(var12.getMessage());
            logger.error("[AdminGroupEditPlugin]\u91cd\u65b0\u52a0\u8f7d\u7ba1\u7406\u5458\u7ec4\u6811\u5f02\u5e38\uff1a", (Throwable)var12);
            return;
        }
    }

    private void clearCache() {
        HRPermCacheMgr.clearAllCache();
    }

    private void showUserF7TreeList() {
        IPageCache pageCache = this.getPageCache();
        String adminSchemeStr = pageCache.get("adminScheme");
        String appId = this.getView().getFormShowParameter().getAppId();
        if (HRStringUtils.isEmpty((String)adminSchemeStr)) {
            HRAdminGroupService.writeOpLog((boolean)false, (String)appId);
        }
        Long adminScheme = Long.parseLong(adminSchemeStr);
        Long adminType = Long.parseLong(pageCache.get("adminType"));
        TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
        String focusNodeId = tv.getTreeState().getFocusNodeId();
        String adminGroupId = focusNodeId.split("_")[0];
        if (!this.verifyAdmin(pageCache, adminGroupId)) {
            HRAdminGroupService.writeOpLog((boolean)false, (String)appId);
            return;
        }
        String groupFilterSql = "SELECT FUSERID FROM T_PERM_USERADMINGROUP WHERE FADMINGROUPID = " + adminGroupId;
        Set filterUserIds = (Set)DB.query((DBRoute)DBRoute.base, (String)groupFilterSql, (ResultSetHandler)new /* Unavailable Anonymous Inner Class!! */);
        filterUserIds.add(RequestContext.get().getCurrUserId());
        String schemeFilterSql = " SELECT DISTINCT uap.fuserid FROM t_perm_useradmingroup uap  INNER JOIN t_perm_admingroup ug ON uap.fadmingroupid = ug.fid  WHERE ug.fadminscheme = ? AND ug.fadmintype != ? ";
        Set schemeFilterUserIds = (Set)DB.query((DBRoute)DBRoute.base, (String)schemeFilterSql, (Object[])new Object[]{adminScheme, adminType}, (ResultSetHandler)new /* Unavailable Anonymous Inner Class!! */);
        filterUserIds.addAll(schemeFilterUserIds);
        ListShowParameter lsp = ShowFormHelper.createShowListForm((String)"bos_user", (boolean)true);
        ArrayList<QFilter> rangeFilterList = new ArrayList<QFilter>(10);
        if (!CollectionUtils.isEmpty((Collection)filterUserIds)) {
            rangeFilterList.add(new QFilter("id", "not in", (Object)filterUserIds));
        }
        rangeFilterList.add(new QFilter("enable", "=", (Object)"1"));
        QFilter userTypeFilter = new QFilter("usertype", "=", (Object)"1");
        userTypeFilter = userTypeFilter.or(QFilter.like((String)"usertype", (String)"1,%"));
        userTypeFilter = userTypeFilter.or(QFilter.like((String)"usertype", (String)"%,1"));
        userTypeFilter = userTypeFilter.or(QFilter.like((String)"usertype", (String)"%,1,%"));
        rangeFilterList.add(userTypeFilter);
        AddCustomUserEventArgs eventArgs = new AddCustomUserEventArgs();
        eventArgs.setLsp(lsp);
        HRPluginProxy proxy = new HRPluginProxy((Object)this, IAdminGroupListSubExtPlugin.class, "kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin#customUserF7ShowParameter", null);
        proxy.callAfter(p -> {
            p.beforeAddCustomUser(eventArgs);
            return null;
        });
        lsp.getListFilterParameter().getQFilters().addAll(rangeFilterList);
        lsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, BOS_USERTREELISTF7));
        lsp.setCustomParam("isOrgBaseAdmin", (Object)Boolean.TRUE);
        this.getView().showForm((FormShowParameter)lsp);
    }

    public void beforeAddCustomUser(AddCustomUserEventArgs eventArgs) {
    }

    private boolean verifyBatchAuth(IPageCache pageCache, String adminGroupId) {
        if ("8609760E-EF83-4775-A9FF-CCDEC7C0B689".equalsIgnoreCase(adminGroupId)) {
            this.getView().showTipNotification(this.getNotRootNodeText(), Integer.valueOf(2000));
            return false;
        }
        String focusNodeLongNumber = pageCache.get("focusNodeLongNumber");
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("perm_admingroup");
        DynamicObjectCollection collection = serviceHelper.queryOriginalCollection("id", new QFilter[]{new QFilter("longnumber", "like", (Object)("%" + focusNodeLongNumber + "%"))});
        if (collection.size() < 2) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u9009\u62e9\u4e00\u4e2a\u975e\u660e\u7ec6\u7ba1\u7406\u5458\u5206\u7ec4\u3002", (String)"HRAdminGroupTreeListPlugin_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), Integer.valueOf(2000));
            return false;
        }
        Set currentUserInGroup = (Set)SerializationUtils.fromJsonString((String)pageCache.get("currentUserInGroup"), Set.class);
        for (String longNumber : currentUserInGroup) {
            if (!focusNodeLongNumber.startsWith(longNumber)) continue;
            return true;
        }
        this.getView().showTipNotification(this.getOnlyModifySubGroupText(), Integer.valueOf(2000));
        return false;
    }

    private boolean verifyAdminGroup(IPageCache pageCache, String adminGroupId) {
        String focusNodeLongNumber;
        if ("8609760E-EF83-4775-A9FF-CCDEC7C0B689".equalsIgnoreCase(adminGroupId)) {
            this.getView().showTipNotification(this.getNotRootNodeText(), Integer.valueOf(2000));
            return false;
        }
        Set currentUserInGroup = (Set)SerializationUtils.fromJsonString((String)pageCache.get("currentUserInGroup"), Set.class);
        if (currentUserInGroup.contains(focusNodeLongNumber = pageCache.get("focusNodeLongNumber"))) {
            return true;
        }
        List superiorGroupId = (List)SerializationUtils.fromJsonString((String)pageCache.get("superiorGroupIds"), List.class);
        if (superiorGroupId.contains(Long.valueOf(adminGroupId))) {
            this.getView().showTipNotification(this.getOnlyModifySubGroupText(), Integer.valueOf(2000));
            return false;
        }
        return true;
    }

    private boolean verifyAdmin(IPageCache pageCache, String adminGroupId) {
        if ("8609760E-EF83-4775-A9FF-CCDEC7C0B689".equalsIgnoreCase(adminGroupId)) {
            this.getView().showTipNotification(this.getNotRootNodeText(), Integer.valueOf(2000));
            return false;
        }
        List superiorGroupId = (List)SerializationUtils.fromJsonString((String)pageCache.get("superiorGroupIds"), List.class);
        if (superiorGroupId.contains(Long.valueOf(adminGroupId))) {
            this.getView().showTipNotification(this.getOnlyModifySubGroupText(), Integer.valueOf(2000));
            return false;
        }
        return true;
    }

    private boolean checkHasRoleRef(Long adminGroupId, String name) {
        boolean roleExists = PermDBServiceHelper.roleDBService.isExists(new QFilter("createadmingrp", "=", (Object)adminGroupId));
        if (roleExists) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u5220\u9664\u7ba1\u7406\u5458\u5206\u7ec4\u201c%s\u201d\uff0c\u8be5\u7ec4\u5df2\u88ab\u89d2\u8272\u5f15\u7528\u3002", (String)"HRAdminGroupTreeListPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{name}));
            return true;
        }
        HRBaseServiceHelper openScopeHelper = new HRBaseServiceHelper("hrcs_roleopenscope");
        boolean openScopeExistS = openScopeHelper.isExists(new QFilter("admingroup", "=", (Object)adminGroupId));
        if (openScopeExistS) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u5220\u9664\u7ba1\u7406\u5458\u5206\u7ec4\u201c%s\u201d\uff0c\u8be5\u7ec4\u5df2\u88ab\u89d2\u8272\u5f15\u7528\u3002", (String)"HRAdminGroupTreeListPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{name}));
            return true;
        }
        HRBaseServiceHelper assignScopeHelper = new HRBaseServiceHelper("hrcs_roleassignscope");
        boolean assignScopeExistS = assignScopeHelper.isExists(new QFilter("admingroup", "=", (Object)adminGroupId));
        if (assignScopeExistS) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u5220\u9664\u7ba1\u7406\u5458\u5206\u7ec4\u201c%s\u201d\uff0c\u8be5\u7ec4\u5df2\u88ab\u89d2\u8272\u5f15\u7528\u3002", (String)"HRAdminGroupTreeListPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{name}));
            return true;
        }
        return false;
    }

    @ExcludeFromJacocoGeneratedReport
    public void confirmCallBack(MessageBoxClosedEvent event) {
        String callBackId = event.getCallBackId();
        TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
        if (callBackId.equals(CONFIRM_CALLBACK_DELETE_ADMIN_GROUP) && event.getResult() == MessageBoxResult.Yes) {
            String focusNodeId = this.getView().getPageCache().get("focusNodeId");
            if (StringUtils.isEmpty((String)focusNodeId)) {
                focusNodeId = tv.getTreeState().getFocusNodeId();
            }
            OperateOption operateOption = OperateOption.create();
            operateOption.setVariableValue("focusNodeId", focusNodeId);
            operateOption.setVariableValue("tag_of_view", Boolean.TRUE.toString());
            DynamicObject dynamicObject = BusinessDataServiceHelper.newDynamicObject((String)"hrcs_useradmingroup");
            DynamicObject adminGroup = BusinessDataServiceHelper.newDynamicObject((String)"perm_admingroup");
            String[] arr = focusNodeId.split("_");
            adminGroup.set("id", (Object)Long.parseLong(arr[0]));
            dynamicObject.set("usergroup", (Object)adminGroup);
            OperationResult operationResult = OperationServiceHelper.executeOperate((String)"do_remove_group", (String)"hrcs_useradmingroup", (DynamicObject[])new DynamicObject[]{dynamicObject}, (OperateOption)operateOption);
            if (operationResult.isSuccess()) {
                try {
                    HRAdminGroupService.initAdminGroupTree((TreeView)tv, (IPageCache)this.getView().getPageCache());
                }
                catch (Exception e) {
                    this.getView().showErrorNotification(e.getMessage());
                    logger.error("[AdminGroupTreeListPlugin]\u91cd\u65b0\u52a0\u8f7d\u7ba1\u7406\u5458\u7ec4\u6811\u5f02\u5e38\uff1a", (Throwable)e);
                }
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f\u3002", (String)"AdminGroupTreeListPlugin_6", (String)"bos-permission-formplugin", (Object[])new Object[0]));
            } else {
                this.getView().showErrorNotification(operationResult.getMessage());
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent e) {
        super.closedCallBack(e);
        String actionId = e.getActionId();
        ListSelectedRowCollection userIds = (ListSelectedRowCollection)e.getReturnData();
        if (BOS_USERTREELISTF7.equals(actionId) && !CollectionUtils.isEmpty((Collection)userIds)) {
            Set userIdSet = userIds.stream().map(userId -> (Long)userId.getPrimaryKeyValue()).collect(Collectors.toSet());
            String focusNodeId = this.getView().getPageCache().get("focusNodeId");
            String adminGroupId = focusNodeId.split("_")[0];
            OperateOption operateOption = OperateOption.create();
            operateOption.setVariableValue("appId", this.getView().getFormShowParameter().getAppId());
            operateOption.setVariableValue("focusAdgNumber", this.getPageCache().get("focusAdgNumber"));
            operateOption.setVariableValue("focusAdgName", this.getPageCache().get("focusAdgName"));
            operateOption.setVariableValue("tag_of_view", Boolean.TRUE.toString());
            long adminGroupIdL = Long.parseLong(adminGroupId);
            DynamicObject[] dynamicObjects = new DynamicObject[userIdSet.size()];
            int i = 0;
            for (Long userId2 : userIdSet) {
                DynamicObject dynamicObject = BusinessDataServiceHelper.newDynamicObject((String)"hrcs_useradmingroup");
                DynamicObject user = BusinessDataServiceHelper.newDynamicObject((String)"bos_user");
                user.set("id", (Object)userId2);
                dynamicObject.set("user", (Object)user);
                DynamicObject adminGroup = BusinessDataServiceHelper.newDynamicObject((String)"perm_admingroup");
                adminGroup.set("id", (Object)adminGroupIdL);
                dynamicObject.set("usergroup", (Object)adminGroup);
                dynamicObjects[i] = dynamicObject;
                ++i;
            }
            OperationResult operationResult = OperationServiceHelper.executeOperate((String)"do_add_user", (String)"hrcs_useradmingroup", (DynamicObject[])dynamicObjects, (OperateOption)operateOption);
            if (operationResult.isSuccess()) {
                TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
                Map focusNode = tv.getTreeState().getFocusNode();
                tv.treeNodeClick((String)focusNode.get("parentid"), (String)focusNode.get("id"));
                this.clearCache();
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u6dfb\u52a0\u6210\u529f\u3002", (String)"AdminGroupTreeListPlugin_8", (String)"bos-permission-formplugin", (Object[])new Object[0]));
            } else {
                this.getView().showErrorNotification(operationResult.getMessage());
            }
        } else if ("hrcs_admingroupdetail".equals(actionId)) {
            try {
                TreeView userIdSet = (TreeView)this.getView().getControl(TREE_ADMIN_GROUP);
            }
            catch (Exception var12) {
                this.getView().showErrorNotification(var12.getMessage());
                logger.error("[AdminGroupEditPlugin]\u91cd\u65b0\u52a0\u8f7d\u7ba1\u7406\u5458\u7ec4\u6811\u5f02\u5e38\uff1a", (Object)e);
                return;
            }
        }
    }

    public void treeNodeClick(TreeNodeEvent e) {
        super.treeNodeClick(e);
        String nodeId = (String)e.getNodeId();
        String parentNodeId = (String)e.getParentNodeId();
        String longNumber = "";
        String adgNumber = "";
        String adgName = "";
        if ("8609760E-EF83-4775-A9FF-CCDEC7C0B689".equals(nodeId)) {
            adgNumber = "";
            adgName = "";
            longNumber = "";
        } else {
            Long adminGroupId = Long.parseLong(nodeId.split("_")[0]);
            DynamicObject adg = BusinessDataServiceHelper.loadSingleFromCache((Object)adminGroupId, (String)"perm_admingroup", (String)"number, name, longnumber");
            adgNumber = adg.getString("number");
            adgName = adg.getString("name");
            longNumber = adg.getString("longnumber");
        }
        this.getPageCache().put("focusNodeId", nodeId);
        this.getPageCache().put("focusNodeParentId", parentNodeId);
        this.getPageCache().put("focusAdgNumber", adgNumber);
        this.getPageCache().put("focusAdgName", adgName);
        this.getPageCache().put("focusNodeLongNumber", longNumber);
    }

    protected QFilter nodeClickFilter() {
        QFilter filter;
        String focusNodeId = this.getView().getPageCache().get("focusNodeId");
        if (StringUtils.isEmpty((String)focusNodeId)) {
            TreeView tv = (TreeView)this.getControl(TREE_ADMIN_GROUP);
            focusNodeId = tv.getTreeState().getFocusNodeId();
        }
        if (StringUtils.isNotEmpty((String)focusNodeId) && !"8609760E-EF83-4775-A9FF-CCDEC7C0B689".equals(focusNodeId)) {
            String adminGroupId = focusNodeId.split("_")[0];
            filter = new QFilter("usergroup", "=", (Object)Long.parseLong(adminGroupId));
        } else {
            filter = new QFilter("id", "=", (Object)0L);
        }
        return filter;
    }

    public void setFilter(SetFilterEvent e) {
        List adminGroupIdCanSee;
        String adminGroupCanSee = this.getPageCache().get("adminGroupCanSee");
        if (StringUtils.isNotEmpty((String)adminGroupCanSee) && !CollectionUtils.isEmpty((Collection)(adminGroupIdCanSee = (List)SerializationUtils.fromJsonString((String)adminGroupCanSee, List.class)))) {
            QFilter adminGroupCanSeeFilter = new QFilter("usergroup.id", "in", (Object)adminGroupIdCanSee);
            e.getCustomQFilters().add(adminGroupCanSeeFilter);
        }
    }

    private String getNotRootNodeText() {
        return ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u975e\u6839\u8282\u70b9\u3002", (String)"HRAdminGroupTreeListPlugin_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
    }

    private String getOnlyModifySubGroupText() {
        return ResManager.loadKDString((String)"\u5f53\u524d\u5206\u7ec4\u4e0d\u5728\u60a8\u7684\u7ba1\u63a7\u8303\u56f4\u5185\uff0c\u65e0\u6743\u7ef4\u62a4\u5f53\u524d\u5206\u7ec4\u7684\u7ba1\u7406\u5458\uff0c\u8bf7\u9009\u62e9\u5176\u4ed6\u7ba1\u7406\u5458\u7ec4\u3002", (String)"HRAdminGroupTreeListPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
    }

    private String getLevelLimitText(int level) {
        return ResManager.loadKDString((String)"\u4ec5\u5141\u8bb8\u521b\u5efa%s\u7ea7\u7ba1\u7406\u5458\u3002", (String)"HRAdminGroupTreeListPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{level});
    }

    public void chat(ChatEvent e) {
        e.setCancel(true);
    }
}
