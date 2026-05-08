/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.SqlParameter
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ArrayUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.DB
 *  kd.bos.db.DBRoute
 *  kd.bos.entity.BasedataEntityType
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.datamodel.ITreeModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.property.GroupProp
 *  kd.bos.entity.property.ParentBasedataProp
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.filter.CommonFilterColumn
 *  kd.bos.filter.FilterColumn
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.TreeNodeClickListener
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.form.control.events.TreeNodeQueryListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.AbstractTreeListView
 *  kd.bos.list.BillList
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.list.events.ChatEvent
 *  kd.bos.list.events.CreateTreeListViewEvent
 *  kd.bos.log.api.OpLogAppInfo
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.form.mcontrol.SearchAp
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.cache.util.PermCommonUtil
 *  kd.bos.schedule.api.JobInfo
 *  kd.bos.schedule.api.JobType
 *  kd.bos.schedule.api.TaskInfo
 *  kd.bos.schedule.form.JobFormInfo
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.hbp.business.dao.factory.HRBaseDaoFactory
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.cache.HRAppCache
 *  kd.hr.hbp.common.cache.IHRAppCache
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRStandardTreeList
 *  kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr
 *  kd.hr.hrcs.bussiness.service.perm.RoleService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.HRPermServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.RoleMemberAssignServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.RoleServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.role.HRRolePermHelper
 *  kd.hr.hrcs.formplugin.web.perm.init.excel.PermHelper
 *  kd.hr.hrcs.formplugin.web.perm.role.CommonTreeListView
 */
package kd.hr.hrcs.formplugin.web.perm.permfile;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.SqlParameter;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ArrayUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.DB;
import kd.bos.db.DBRoute;
import kd.bos.entity.BasedataEntityType;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.datamodel.ITreeModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.property.GroupProp;
import kd.bos.entity.property.ParentBasedataProp;
import kd.bos.entity.tree.TreeNode;
import kd.bos.filter.CommonFilterColumn;
import kd.bos.filter.FilterColumn;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.control.Control;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.TreeNodeClickListener;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.control.events.TreeNodeQueryListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.AbstractTreeListView;
import kd.bos.list.BillList;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.list.events.ChatEvent;
import kd.bos.list.events.CreateTreeListViewEvent;
import kd.bos.log.api.OpLogAppInfo;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.form.mcontrol.SearchAp;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.cache.util.PermCommonUtil;
import kd.bos.schedule.api.JobInfo;
import kd.bos.schedule.api.JobType;
import kd.bos.schedule.api.TaskInfo;
import kd.bos.schedule.form.JobFormInfo;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.business.dao.factory.HRBaseDaoFactory;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.cache.HRAppCache;
import kd.hr.hbp.common.cache.IHRAppCache;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRStandardTreeList;
import kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr;
import kd.hr.hrcs.bussiness.service.perm.RoleService;
import kd.hr.hrcs.bussiness.servicehelper.perm.HRPermServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.RoleMemberAssignServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.RoleServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.role.HRRolePermHelper;
import kd.hr.hrcs.formplugin.web.perm.init.excel.PermHelper;
import kd.hr.hrcs.formplugin.web.perm.role.CommonTreeListView;

public final class PermfilesListPlugin
extends HRStandardTreeList
implements TreeNodeQueryListener,
TreeNodeClickListener {
    private static final String ORG_NAME = "org.name";
    private static final String PERMFILEENABLE = "permfileenable";
    private static final String TREEVIEW = "treeview";
    private static final String NEW_PERMFILE = "new_permfile";
    private static final String HBSS_USERPERMFILE = "hrcs_userpermfile";
    private static final String HBSS_PERMFILEGRP = "hrcs_permfilegrp";
    private static final String HBSS_APP_ID = "XYRL3+A8Z+Z";
    private static final String PAGE_HBSS_HRBUQUERY = "hbss_hrbuquery";
    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final String CONFIRMCALLBACK_SAVE = "confirmcallback_save";
    private static final Log LOGGER = LogFactory.getLog(PermfilesListPlugin.class);
    private static final String ID_ROOTNODE = "8609760E-EF83-4775-A9FF-CCDEC7C0B689";
    private static final String BAR_REFRESH = "refresh";
    private static final String BAR_CLEARCACHE = "btn_clearcache";
    private static final String BAR_COPYPERM = "btn_copyperm";
    private static final String BAR_ASSIGNPERM = "baritem_assignperm";
    private static final String BAR_ASSIGNROLE = "btn_allocprem";
    private static final String BAR_USERPERMINIT = "inituserperm";
    private static final String BAR_EXPORT_USERPERMINIT = "exportuserperm";
    private static final String BAR_NEW = "bar_new";
    private static final String BAR_BATCHGROUP = "btn_batchgroup";
    private static final String USER_LIST = "billlistap";
    private static final String BAR_ENABLE = "enable";
    private static final String BAR_DISABLE = "disable";
    private static final String BAR_IMPORTDATA = "bar_import";
    private static final String BTN_INITDATA = "btn_initdata";
    private static final String PERFILE_STATUS_0 = "0";
    private static final String PERFILE_STATUS_1 = "1";
    private static final String BUSINESS_STATUS_1 = "1";
    private static final String ACTION_ID_EXPORT = "exportUrl";
    private static final String HRCS_APPID = PermCommonUtil.getAppIdFromSuspectedAppNum((String)"hrcs");
    private static final String ENTITYTYPE_PERMFILEGRP = "hrcs_permfilegrp";
    private CommonTreeListView treeListView = new CommonTreeListView("hrcs_permfilegrp");

    public void chat(ChatEvent event) {
        event.setCancel(true);
    }

    public PermfilesListPlugin() {
        super("hrcs_permfilegrp", ID_ROOTNODE, false);
    }

    public void createTreeListView(CreateTreeListViewEvent event) {
        super.createTreeListView(event);
        event.setView((AbstractTreeListView)this.treeListView);
    }

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        this.updateSearch();
    }

    private void updateSearch() {
        SearchAp search = new SearchAp();
        search.setKey("searchap");
        search.setSearchEmptyText(new LocaleString(ResManager.loadKDString((String)"\u8bf7\u8f93\u5165\u6743\u9650\u6863\u6848\u7ec4\u540d\u79f0\u3002", (String)"PermfilesListPlugin_13", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        this.getView().updateControlMetadata("searchap", search.createControl());
    }

    public void initTreeToolbar(EventObject eventObject) {
        super.initTreeToolbar(eventObject);
        this.getView().setVisible(Boolean.TRUE, new String[]{"btnnew", "btnedit", "btndel"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"iscontainnow", "iscontainlower"});
    }

    protected DynamicObject getRootDynamicObject() {
        DynamicObject rootDy = BusinessDataServiceHelper.newDynamicObject((String)"hrcs_permfilegrp");
        rootDy.set("id", (Object)ID_ROOTNODE);
        rootDy.set("name", (Object)ResManager.loadKDString((String)"\u5168\u90e8", (String)"PermfilesListPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        rootDy.set("parent", (Object)"");
        rootDy.set("longnumber", (Object)"");
        return rootDy;
    }

    protected DynamicObjectCollection getTreeViewCollection(String entityName, String parentId) {
        QFilter filter = null;
        String selectFields = "id, name, parent, longnumber, isleaf";
        filter = HRStringUtils.equals((String)parentId, (String)ID_ROOTNODE) || HRStringUtils.equals((String)parentId, (String)PERFILE_STATUS_0) ? new QFilter(BAR_ENABLE, "=", (Object)"1") : new QFilter("parent", "=", (Object)Long.valueOf(parentId));
        QFilter[] filters = new QFilter[]{filter};
        DynamicObjectCollection dyColl = this.getPermfileGrp(entityName, selectFields, filters);
        if (Objects.isNull(dyColl)) {
            dyColl = new DynamicObjectCollection();
        }
        return dyColl;
    }

    private DynamicObjectCollection getPermfileGrp(String entityName, String selectFields, QFilter[] filters) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper(entityName);
        DynamicObject[] objects = helper.query(selectFields, filters, "longnumber");
        DynamicObjectCollection dyColl = null;
        if (objects.length > 0) {
            DynamicObject obj = objects[0];
            dyColl = new DynamicObjectCollection(obj.getDynamicObjectType(), (Object)obj);
            for (DynamicObject dynamicObject : objects) {
                if (Objects.isNull(dynamicObject.get("parent"))) {
                    dynamicObject.set("parent", (Object)0);
                } else {
                    dynamicObject.set("parent", dynamicObject.get("parent.id"));
                }
                dyColl.add((Object)dynamicObject);
            }
        }
        return dyColl;
    }

    protected QFilter buildNodeClickFilter(BuildTreeListFilterEvent event) {
        String focusNodeId = event.getNodeId().toString();
        QFilter nodeClickFilter = null;
        if (!this.getTreeModel().getRoot().getId().equals(focusNodeId)) {
            QFilter focusNodeFilter = new QFilter("id", "=", (Object)Long.valueOf(focusNodeId));
            DynamicObject selectNodeDy = HRBaseDaoFactory.getInstance((String)this.getEntityName()).queryOne("longnumber", focusNodeFilter);
            if (ObjectUtils.isEmpty((Object)selectNodeDy)) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(focusNodeId);
                this.getTreeModel().deleteNode(treeNode, true);
                this.getTreeListView().focusRootNode();
                this.getTreeListView().refreshTreeView();
                this.getTreeListView().refresh();
            } else {
                List<Long> list = this.getSubGrp(selectNodeDy);
                nodeClickFilter = new QFilter("hrcs_permfilegrpmember.permfilegrp.id", "in", list);
            }
        }
        return nodeClickFilter;
    }

    private List<Long> getSubGrp(DynamicObject focusNodeDy) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_permfilegrp");
        QFilter[] filters = new QFilter[]{new QFilter("longnumber", "like", (Object)(focusNodeDy.getString("longnumber") + "." + "%"))};
        DynamicObjectCollection coll = helper.queryOriginalCollection("id", filters);
        ArrayList list = Lists.newArrayListWithExpectedSize((int)coll.size());
        for (DynamicObject obj : coll) {
            list.add(obj.getLong("id"));
        }
        list.add(focusNodeDy.getLong("id"));
        return list;
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeItemClick(BeforeItemClickEvent evt) {
        BillList billList = (BillList)this.getView().getControl(USER_LIST);
        ListSelectedRowCollection listSelectRows = billList.getSelectedRows();
        String itemKey = evt.getItemKey();
        boolean ifNeedCancel = false;
        this.getPageCache().put("itemKey", itemKey);
        switch (itemKey) {
            case "btn_batchgroup": {
                ifNeedCancel = this.ifNoOneSelected(listSelectRows);
                break;
            }
            case "btn_allocprem": {
                ifNeedCancel = this.checkSelected(listSelectRows);
                break;
            }
            case "enable": {
                ifNeedCancel = this.ifNoOneSelected(listSelectRows);
                break;
            }
            case "disable": {
                ifNeedCancel = this.ifNoOneSelected(listSelectRows);
                break;
            }
            case "baritem_assignperm": {
                ifNeedCancel = this.checkSelected(listSelectRows);
                break;
            }
            case "btn_copyperm": {
                if (listSelectRows.size() == 0) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u4e2a\u7528\u6237\u3002", (String)"PermfilesListPlugin_40", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    ifNeedCancel = true;
                    break;
                }
                if (listSelectRows.size() <= 1) break;
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u4e2a\u7528\u6237\u3002", (String)"PermfilesListPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                ifNeedCancel = true;
                break;
            }
        }
        if (ifNeedCancel) {
            evt.setCancel(true);
        }
    }

    private boolean checkSelected(ListSelectedRowCollection listSelectRows) {
        boolean result = false;
        result = this.ifNoOneSelected(listSelectRows);
        if (!result) {
            result = this.ifMoreThanOneSelected(listSelectRows);
        }
        return result;
    }

    private boolean ifMoreThanOneSelected(ListSelectedRowCollection listSelectRows) {
        boolean result = false;
        if (listSelectRows.size() > 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u4e2a\u7528\u6237\u3002", (String)"PermfilesListPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            result = true;
        }
        return result;
    }

    private boolean ifNoOneSelected(ListSelectedRowCollection listSelectRows) {
        boolean result = false;
        if (listSelectRows.size() == 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u8981\u6267\u884c\u7684\u6570\u636e\u3002", (String)"PermfilesListPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            result = true;
        }
        return result;
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String itemKey = this.getPageCache().get("itemKey");
        if (HRStringUtils.isNotEmpty((String)itemKey)) {
            BillList billList = (BillList)this.getView().getControl(USER_LIST);
            ListSelectedRowCollection rows = billList.getSelectedRows();
            switch (itemKey) {
                case "bar_new": {
                    this.showNewForm(args);
                    break;
                }
                case "enable": 
                case "disable": {
                    OperateOption operateOption = ((FormOperate)args.getSource()).getOption();
                    operateOption.setVariableValue("tag_of_view", "true");
                    break;
                }
                case "bar_import": {
                    MultiLangEnumBridge langEnumBridge = new MultiLangEnumBridge("\u5bfc\u5165", "PermfilesListPlugin_25", "hrmp-hrcs-formplugin");
                    RoleService.commonWriteLogNoOpKey((String)"importdata", (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_permfilelist", (String)HRCS_APPID);
                    break;
                }
                case "btn_allocprem": {
                    this.userAssignRole(rows);
                    break;
                }
                case "inituserperm": {
                    this.userPermInit();
                    break;
                }
                case "exportuserperm": {
                    this.exportUserPerm(rows);
                    break;
                }
                case "btn_initdata": {
                    this.genPermFiles();
                    MultiLangEnumBridge langEnumBridge2 = new MultiLangEnumBridge("\u6309\u7ec4\u7ec7\u5206\u914d\u751f\u6210", "PermfilesListPlugin_37", "hrmp-hrcs-formplugin");
                    RoleService.commonWriteLogNoOpKey((String)"syncperm", (MultiLangEnumBridge)langEnumBridge2, (boolean)true, (String)"hrcs_permfilelist", (String)HRCS_APPID);
                    break;
                }
                case "btn_copyperm": {
                    this.copyPerm(rows);
                    break;
                }
                case "btn_batchgroup": {
                    this.handleBatchGroup(rows);
                    break;
                }
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        String itemKey = this.getPageCache().get("itemKey");
        if (HRStringUtils.isNotEmpty((String)itemKey)) {
            BillList billList = (BillList)this.getView().getControl(USER_LIST);
            ListSelectedRowCollection selectedRows = billList.getSelectedRows();
            switch (itemKey) {
                case "enable": 
                case "disable": {
                    Map customDataMap = args.getOperationResult().getCustomData();
                    String notNeedUpdateTag = (String)customDataMap.get("dealResult_notNeedUpdateTag");
                    String notNeedUpdateMsg = (String)customDataMap.get("dealResult_notNeedUpdateMsg");
                    if (selectedRows.size() == 1 && HRStringUtils.equals((String)notNeedUpdateTag, (String)"true")) {
                        this.getView().showTipNotification(notNeedUpdateMsg);
                        return;
                    }
                    String successNumberStr = (String)customDataMap.get("dealResult_successNumber");
                    String detailMsg = (String)customDataMap.get("dealResult_detailMsg");
                    int successNumber = Integer.parseInt(successNumberStr);
                    String targetStatus = itemKey.equals(BAR_ENABLE) ? "1" : PERFILE_STATUS_0;
                    this.showChangeStatusMessage(selectedRows.size(), successNumber, targetStatus, detailMsg);
                    billList.refresh();
                    billList.clearSelection();
                    break;
                }
            }
            this.getPageCache().remove("itemKey");
        }
    }

    public void itemClick(ItemClickEvent evt) {
        String itemkey = evt.getItemKey().toLowerCase(Locale.ROOT);
        BillList billList = (BillList)this.getView().getControl(USER_LIST);
        ListSelectedRowCollection rows = billList.getSelectedRows();
        HashMap params = Maps.newHashMapWithExpectedSize((int)16);
        this.setDefault(rows, params);
        switch (itemkey) {
            case "btn_clearcache": {
                HRPermCacheMgr.clearAllCache();
                HRPermCacheMgr.clearAllManageCache();
                MultiLangEnumBridge langEnumBridge = new MultiLangEnumBridge("\u6e05\u7a7a\u6743\u9650\u7f13\u5b58", "PermfilesListPlugin_26", "hrmp-hrcs-formplugin");
                RoleService.commonWriteLogNoOpKey((String)"clear permission cache", (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_permfilelist", (String)HRCS_APPID);
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u6743\u9650\u7f13\u5b58\u5df2\u6e05\u7406\u3002", (String)"PermfilesListPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), Integer.valueOf(2500));
                break;
            }
            case "refresh": {
                this.refresh(billList);
                break;
            }
        }
    }

    private void setDefault(ListSelectedRowCollection rows, Map<String, Object> params) {
        Map customParamsFromFsp;
        if (rows != null && rows.size() > 0) {
            String userId = String.valueOf(rows.get(0).getPrimaryKeyValue());
            params.put("paramUserId", userId);
        }
        if ((customParamsFromFsp = this.getView().getFormShowParameter().getCustomParams()).get("FormShowParam_dimension") == null) {
            params.put("FormShowParam_dimension", "DIM_ORG");
        }
    }

    private void handleBatchGroup(ListSelectedRowCollection rows) {
        HashMap params = Maps.newHashMapWithExpectedSize((int)16);
        StringBuilder permfileIds = new StringBuilder();
        for (ListSelectedRow row : rows) {
            String permfileId = String.valueOf(row.getPrimaryKeyValue());
            permfileIds.append(permfileId).append(',');
        }
        MultiLangEnumBridge langEnumBridge = new MultiLangEnumBridge("\u6279\u91cf\u5206\u7ec4", "PermfilesListPlugin_27", "hrmp-hrcs-formplugin");
        RoleService.commonWriteLogNoOpKey((String)"batch grouping", (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_permfilelist", (String)HRCS_APPID);
        params.put("permfiles", permfileIds.substring(0, permfileIds.length() - 1));
        FormShowParameter fsp = new FormShowParameter();
        RoleServiceHelper.showForm((String)"hrcs_permfilegrptree", (Map)params, null, (ShowType)ShowType.Modal, (FormShowParameter)fsp, (IFormView)this.getView());
    }

    private void refresh(BillList billList) {
        billList.clearSelection();
        billList.refresh();
        TreeView tv = (TreeView)this.getControl(TREEVIEW);
        if (tv != null && tv.getTreeState().getFocusNode() != null) {
            Map focusNode = tv.getTreeState().getFocusNode();
            tv.treeNodeClick(focusNode.get("parentid").toString(), focusNode.get("id").toString());
        }
    }

    private void showNewForm(BeforeDoOperationEventArgs args) {
        BillShowParameter bsp = new BillShowParameter();
        bsp.setCustomParam("groupId", this.getTreeModel().getCurrentNodeId());
        bsp.setFormId(HBSS_USERPERMFILE);
        bsp.getOpenStyle().setShowType(ShowType.Modal);
        CloseCallBack callBack = new CloseCallBack((IFormPlugin)this, NEW_PERMFILE);
        bsp.setCloseCallBack(callBack);
        this.getView().showForm((FormShowParameter)bsp);
        RoleService.commonWriteLogBeforeDoOp((BeforeDoOperationEventArgs)args);
        args.setCancel(true);
    }

    private void userAssignRole(ListSelectedRowCollection rows) {
        Object[] ids = rows.getPrimaryKeyValues();
        Long permfileId = (Long)ids[0];
        if (!this.checkDisable(permfileId)) {
            return;
        }
        DynamicObject permFile = RoleMemberAssignServiceHelper.getPermFileById((Long)permfileId);
        Long userId = permFile.getLong("user.id");
        if (Objects.isNull(userId) || Objects.equals(userId, 0L)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u7528\u6237\u6570\u636e\u65e0\u6548", (String)"PermfilesListPlugin_51", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        HashMap<String, Long> params = new HashMap<String, Long>(16);
        params.put("permfileId", permfileId);
        MultiLangEnumBridge langEnumBridge = new MultiLangEnumBridge("\u7528\u6237\u5206\u914d\u89d2\u8272", "PermfilesListPlugin_28", "hrmp-hrcs-formplugin");
        RoleService.commonWriteLogNoOpKey((String)"user assignment role", (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_permfilelist", (String)HRCS_APPID);
        FormShowParameter fsp = new FormShowParameter();
        fsp.setPageId("hrcs_userassignrole_page_" + this.getView().getPageId() + "_" + permfileId);
        RoleServiceHelper.showForm((String)"hrcs_userassignrole", params, null, (ShowType)ShowType.MainNewTabPage, (FormShowParameter)fsp, (IFormView)this.getView());
    }

    @ExcludeFromJacocoGeneratedReport
    private void userPermInit() {
        if (!RoleService.isAdmin()) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u60a8\u65e0\u6cd5\u8bbf\u95ee\u8be5\u529f\u80fd\uff0c\u56e0\u4e3a\u60a8\u4e0d\u662fHR\u9886\u57df\u7ba1\u7406\u5458\u3002", (String)"PermfilesListPlugin_39", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        listShowParameter.setFormId("bos_list");
        listShowParameter.setBillFormId("hrcs_perminitrecord");
        listShowParameter.setPageId(this.getView().getPageId() + RequestContext.get().getCurrUserId());
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    @ExcludeFromJacocoGeneratedReport
    private void exportUserPerm(ListSelectedRowCollection rows) {
        if (!RoleService.isAdmin()) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u60a8\u65e0\u6cd5\u8bbf\u95ee\u8be5\u529f\u80fd\uff0c\u56e0\u4e3a\u60a8\u4e0d\u662fHR\u9886\u57df\u7ba1\u7406\u5458\u3002", (String)"PermfilesListPlugin_39", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        Object[] permFileIds = rows.getPrimaryKeyValues();
        if (permFileIds.length <= 0) {
            return;
        }
        MultiLangEnumBridge langEnumBridge = new MultiLangEnumBridge("\u5bfc\u51fa\u7528\u6237\u6743\u9650", "PermfilesListPlugin_38", "hrmp-hrcs-formplugin");
        RoleService.commonWriteLogNoOpKey((String)BAR_EXPORT_USERPERMINIT, (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_permfilelist", (String)HRCS_APPID);
        if (PermHelper.getUserRelatesByPermFields((Object[])permFileIds).length <= 0) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5f53\u524d\u9009\u4e2d\u7684\u6570\u636e\uff0c\u65e0\u7528\u6237\u6743\u9650\u8bb0\u5f55\u3002", (String)"PermSheetHelper_64", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        LOGGER.info("Got selected perm field ids: {}.", permFileIds);
        this.showProgressForm(permFileIds);
    }

    @ExcludeFromJacocoGeneratedReport
    private void showProgressForm(Object[] permFileIds) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_exportperm");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.getCustomParams().put("entityname", ResManager.loadKDString((String)"\u7528\u6237\u6743\u9650", (String)"PermSheetHelper_85", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        showParameter.getCustomParams().put("taskClassName", "kd.hr.hrcs.formplugin.web.perm.init.task.PermFilesExportTask");
        showParameter.getCustomParams().put("permFileIds", permFileIds);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, ACTION_ID_EXPORT));
        this.getView().showForm(showParameter);
    }

    @ExcludeFromJacocoGeneratedReport
    private void handleTaskInfo(TaskInfo taskInfo) {
        if (taskInfo.isTaskEnd()) {
            Map taskInfoMap = (Map)JSONObject.parseObject((String)taskInfo.getData(), Map.class);
            if (Objects.isNull(taskInfoMap)) {
                return;
            }
            String exportUrl = (String)taskInfoMap.get(ACTION_ID_EXPORT);
            if (HRStringUtils.isNotEmpty((String)exportUrl)) {
                IClientViewProxy clientViewProxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
                clientViewProxy.addAction("download", (Object)exportUrl);
            }
        } else {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u6784\u5efa\u5bfc\u51faURL\u5931\u8d25\uff01", (String)"HRMultiEntityExportPlugin_1", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]));
        }
    }

    private boolean checkDisable(Long permfileId) {
        String userId;
        String currentUserId;
        boolean flag = false;
        HRBaseServiceHelper helper = new HRBaseServiceHelper(HBSS_USERPERMFILE);
        DynamicObject permfile = helper.queryOriginalOne("permfileenable,user", (Object)permfileId);
        if (HRStringUtils.equals((String)permfile.getString(PERMFILEENABLE), (String)"1")) {
            flag = true;
        }
        if (!HRStringUtils.equals((String)(currentUserId = RequestContext.get().getUserId()), (String)(userId = permfile.getString("user")))) {
            flag = true;
        } else {
            flag = false;
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u6388\u6743\u7ed9\u81ea\u5df1\u3002", (String)"PermfilesListPlugin_24", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        }
        return flag;
    }

    private String getOperationName(String enable) {
        return HRStringUtils.equals((String)"1", (String)enable) ? ResManager.loadKDString((String)"\u542f\u7528", (String)"PermfilesListPlugin_30", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u7981\u7528", (String)"PermfilesListPlugin_31", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
    }

    @ExcludeFromJacocoGeneratedReport
    private void doBuildErrorMsg(DynamicObjectCollection permfiles, String errorReasonPre, String enable, StringBuilder sb, List<OpLogAppInfo> appLogInfoList) {
        String opKey = HRStringUtils.equals((String)"1", (String)enable) ? BAR_ENABLE : BAR_DISABLE;
        String opName = this.getOperationName(enable);
        for (DynamicObject permfile : permfiles) {
            String tempEnable = permfile.getString(PERMFILEENABLE);
            if (!HRStringUtils.equals((String)tempEnable, (String)enable)) continue;
            RoleService.addLogWithOpKey((String)opKey, (String)opName, (boolean)false, (String)"hrcs_permfilelist", (String)HRCS_APPID, appLogInfoList, (String)this.buildErrorMsg(errorReasonPre, sb, permfile));
        }
    }

    private void showChangeStatusMessage(int selectSize, int success, String enable, String detailMsg) {
        int fail = selectSize - success;
        if (fail == 0) {
            String msg = HRStringUtils.equals((String)enable, (String)"1") ? ResManager.loadKDString((String)"\u751f\u6548\u6210\u529f", (String)"PermfilesListPlugin_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u5931\u6548\u6210\u529f", (String)"PermfilesListPlugin_10", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            this.getView().showSuccessNotification(msg);
        } else if (fail == 1 && selectSize == 1) {
            this.getView().showTipNotification(detailMsg);
        } else {
            String msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5171%1$s\u6761\u5355\u636e\uff0c\u6210\u529f%2$s\u6761\uff0c\u5931\u8d25%3$s\u6761\u3002", (String)"PermfilesListPlugin_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), selectSize, success, fail);
            FormShowParameter parameter = new FormShowParameter();
            parameter.getOpenStyle().setShowType(ShowType.Modal);
            parameter.setShowTitle(false);
            parameter.setFormId("bos_operationresult");
            parameter.setCustomParam("title", (Object)msg);
            parameter.setCustomParam("errorMsg", (Object)detailMsg);
            this.getView().showForm(parameter);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void handleBeforeRefresh(int fail, int success, String enable, StringBuilder sb, Object[] ids) {
        if (fail == 0) {
            String msg = HRStringUtils.equals((String)enable, (String)"1") ? ResManager.loadKDString((String)"\u751f\u6548\u6210\u529f", (String)"PermfilesListPlugin_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u5931\u6548\u6210\u529f", (String)"PermfilesListPlugin_10", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            this.getView().showSuccessNotification(msg);
        } else if (fail == 1 && ids.length == 1) {
            this.getView().showTipNotification(sb.toString());
        } else {
            String msg = HRStringUtils.equals((String)enable, (String)"1") ? String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5171%1$s\u6761\u5355\u636e\uff0c\u751f\u6548\u6210\u529f%2$s\u6761\uff0c\u5931\u8d25%3$s\u6761\u3002", (String)"PermfilesListPlugin_46", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), ids.length, success, fail) : String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5171%1$s\u6761\u5355\u636e\uff0c\u5931\u6548\u6210\u529f%2$s\u6761\uff0c\u5931\u8d25%3$s\u6761\u3002", (String)"PermfilesListPlugin_47", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), ids.length, success, fail);
            this.showResultForm(msg, sb);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private String buildEnableMsg(String enable, DynamicObject permfile) {
        String msg = HRStringUtils.equals((String)enable, (String)"1") ? String.format(Locale.ROOT, ResManager.loadKDString((String)"%1$s\uff08%2$s\uff09\u6863\u6848\u72b6\u6001\u5df2\u751f\u6548\u3002", (String)"PermfilesListPlugin_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), permfile.getString("user.name"), permfile.getString(ORG_NAME)) : String.format(Locale.ROOT, ResManager.loadKDString((String)"%1$s\uff08%2$s\uff09\u6863\u6848\u72b6\u6001\u5df2\u5931\u6548\u3002", (String)"PermfilesListPlugin_7", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), permfile.getString("user.name"), permfile.getString(ORG_NAME));
        return msg;
    }

    @ExcludeFromJacocoGeneratedReport
    private String buildErrorMsg(String errorReasonPre, StringBuilder sb, DynamicObject permfile) {
        ArrayList errorMsgs = Lists.newArrayListWithExpectedSize((int)16);
        String errorMsg = String.format(ResManager.loadKDString((String)"%1$s\uff08%2$s\uff09", (String)"PermfilesListPlugin_45", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), permfile.getString("user.name"), permfile.getString(ORG_NAME));
        errorMsgs.add(errorMsg);
        errorMsgs.add(errorReasonPre);
        errorMsgs.add(System.lineSeparator());
        String errorMsgFullStr = String.join((CharSequence)"", errorMsgs);
        sb.append(errorMsgFullStr);
        return errorMsgFullStr;
    }

    @ExcludeFromJacocoGeneratedReport
    private void showResultForm(String msg, StringBuilder sb) {
        FormShowParameter parameter = new FormShowParameter();
        parameter.getOpenStyle().setShowType(ShowType.Modal);
        parameter.setShowTitle(false);
        parameter.setFormId("bos_operationresult");
        parameter.setCustomParam("title", (Object)msg);
        parameter.setCustomParam("errorMsg", (Object)sb.toString());
        this.getView().showForm(parameter);
    }

    private DynamicObjectCollection getPermfiles(String enable, Object[] ids) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper(HBSS_USERPERMFILE);
        QFilter[] filters = new QFilter[]{new QFilter("id", "in", (Object)ids), new QFilter(PERMFILEENABLE, "=", (Object)enable)};
        return helper.queryOriginalCollection("id,user.name,org.name,permfileenable", filters);
    }

    private List<Object> getUpdateIds(Object[] ids, DynamicObjectCollection permfiles) {
        ArrayList list = Lists.newArrayList();
        for (Object idInList : ids) {
            boolean isFound = false;
            String id = idInList.toString();
            for (DynamicObject permfile : permfiles) {
                if (!HRStringUtils.equals((String)id, (String)permfile.getString("id"))) continue;
                isFound = true;
                break;
            }
            if (isFound) continue;
            list.add(idInList);
        }
        return list;
    }

    private int setEnable(String enable, List<Object> idList) {
        int success = 0;
        if (Objects.nonNull(idList) && idList.size() > 0) {
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(HBSS_USERPERMFILE);
            success = serviceHelper.update((DynamicObject[])Arrays.stream(serviceHelper.query("id,permfileenable", new QFilter[]{new QFilter("id", "in", idList)})).map(item -> {
                item.set(PERMFILEENABLE, (Object)enable);
                return item;
            }).toArray(DynamicObject[]::new)).length;
            if (HRStringUtils.equals((String)PERFILE_STATUS_0, (String)enable)) {
                RoleServiceHelper.disablePermfile(idList);
            }
        }
        HRPermCacheMgr.clearAllCache();
        return success;
    }

    @ExcludeFromJacocoGeneratedReport
    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        block15: {
            Object returnData;
            super.closedCallBack(closedCallBackEvent);
            if (NEW_PERMFILE.equals(closedCallBackEvent.getActionId())) {
                Map returnData2 = (Map)closedCallBackEvent.getReturnData();
                if (Objects.isNull(returnData2)) {
                    return;
                }
                String isNeedRefresh = (String)returnData2.get("isNeedRefresh");
                if (HRStringUtils.equals((String)isNeedRefresh, (String)"true")) {
                    BillList billList = (BillList)this.getView().getControl(USER_LIST);
                    billList.refresh();
                }
            } else if ("syncPermFilesTask".equals(closedCallBackEvent.getActionId())) {
                try {
                    IHRAppCache appCache = HRAppCache.get((String)"hrcs");
                    String taskId = (String)appCache.get("syncPermFilesTaskId", String.class);
                    TaskInfo taskInfo = null;
                    if (StringUtils.isNotEmpty((CharSequence)taskId)) {
                        taskInfo = this.getTaskInfoByTaskId(taskId);
                    }
                    if (null != taskInfo && !taskInfo.isTaskEnd()) {
                        return;
                    }
                    Map rspMap = (Map)appCache.get("syncPermFilesTask" + RequestContext.get().getCurrUserId(), HashMap.class);
                    if (CollectionUtils.isEmpty((Map)rspMap)) break block15;
                    appCache.put("syncPermFilesTaskId", null);
                    Object newCount = rspMap.get("newCount");
                    Object updCount = rspMap.get("updCount");
                    if (((Boolean)rspMap.get("success")).booleanValue()) {
                        String successMsg = String.format(ResManager.loadKDString((String)"\u6309\u7ec4\u7ec7\u5206\u914d\u751f\u6210\u5168\u90e8\u6210\u529f\uff0c\u672c\u6b21\u5171\u521b\u5efa %1$s \u6761\uff0c\u751f\u6548 %2$s \u6761\u3002", (String)"PermfilesListPlugin_33", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), newCount, updCount);
                        this.getView().showConfirm(successMsg, MessageBoxOptions.OK);
                        break block15;
                    }
                    String msg = String.format(ResManager.loadKDString((String)"\u6309\u7ec4\u7ec7\u5206\u914d\u751f\u6210\u90e8\u5206\u5931\u8d25\uff0c\u672c\u6b21\u5df2\u521b\u5efa %1$s \u6761\uff0c\u751f\u6548 %2$s \u6761\uff0c\u8bf7\u8054\u7cfb\u6280\u672f\u4eba\u5458\u8fdb\u884c\u6392\u67e5\u5177\u4f53\u539f\u56e0\uff0c\u5931\u8d25\u5173\u952e\u4fe1\u606f\u5982\u4e0b\uff1a%3$s\u3002", (String)"PermfilesListPlugin_34", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), newCount, updCount, rspMap.get("errorInfo"));
                    this.getView().showMessage(msg);
                }
                catch (Exception exc) {
                    String msg = String.format(ResManager.loadKDString((String)"\u6309\u7ec4\u7ec7\u5206\u914d\u751f\u6210\uff0c\u4efb\u52a1\u56de\u8c03\u5931\u8d25\uff0c\u8bf7\u8054\u7cfb\u6280\u672f\u4eba\u5458\u8fdb\u884c\u6392\u67e5\u5177\u4f53\u539f\u56e0\uff0c\u5931\u8d25\u5173\u952e\u4fe1\u606f\u5982\u4e0b\uff1a%1$s\u3002", (String)"PermfilesListPlugin_36", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), exc.getMessage());
                    this.getView().showMessage(msg);
                    LOGGER.error("PermfilesListPlugin-closedCallBack-syncPermFilesTask-error\uff1a", (Throwable)exc);
                }
            } else if (HRStringUtils.equals((String)ACTION_ID_EXPORT, (String)closedCallBackEvent.getActionId())) {
                Map result;
                Object returnData3 = closedCallBackEvent.getReturnData();
                if (returnData3 instanceof Map && (result = (Map)returnData3).containsKey("taskinfo")) {
                    String taskInfoStr = (String)result.get("taskinfo");
                    TaskInfo taskInfo = (TaskInfo)SerializationUtils.fromJsonString((String)taskInfoStr, TaskInfo.class);
                    this.handleTaskInfo(taskInfo);
                }
            } else if (HRStringUtils.equals((String)CONFIRMCALLBACK_SAVE, (String)closedCallBackEvent.getActionId()) && (returnData = closedCallBackEvent.getReturnData()) instanceof MulBasedataDynamicObjectCollection) {
                List<Long> laborrelTypeIds = ((MulBasedataDynamicObjectCollection)returnData).stream().map(dy -> dy.getLong("fbasedataid_id")).collect(Collectors.toList());
                LOGGER.info("generate permfile by ermanfile,laborrelTypeIds:{}", laborrelTypeIds);
                this.launchSyncPermfileJob(laborrelTypeIds);
            }
        }
    }

    public void treeToolbarClick(EventObject eventObject) {
        ITreeModel treeModel = this.getTreeModel();
        Control ctl = (Control)eventObject.getSource();
        GroupProp prop = this.getTreeModel().getGroupProp();
        String entityId = prop.getBaseEntityId();
        String currentNodeId = (String)treeModel.getCurrentNodeId();
        if (ctl.getKey().equals("btnnew")) {
            this.addGroupNode(entityId, currentNodeId);
        } else if (ctl.getKey().equals("btnedit")) {
            if (this.buildBtnEditErrorMsg(prop, currentNodeId)) {
                return;
            }
            this.editGroupNode(entityId, currentNodeId);
        } else if (ctl.getKey().equals("btndel")) {
            if (this.buildBtnDelErrorMsg(prop, currentNodeId)) {
                return;
            }
            TreeNode currentNode = this.getTreeModel().getRoot().getTreeNode(currentNodeId, 10);
            String text = currentNode.getText();
            ConfirmCallBackListener confirmCallBacks = new ConfirmCallBackListener("group_bar_del", (IFormPlugin)this);
            String strs = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u60a8\u786e\u8ba4\u5220\u9664\u5206\u7ec4%s\uff1f", (String)"PermfilesListPlugin_20", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), text);
            this.getView().showConfirm(strs, MessageBoxOptions.YesNo, confirmCallBacks);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean buildBtnEditErrorMsg(GroupProp prop, String currentNodeId) {
        if (this.getTreeModel().getRoot().getId().equals(currentNodeId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u7f16\u8f91\u6839\u8282\u70b9\u3002", (String)"PermfilesListPlugin_15", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return true;
        }
        if (StringUtils.isBlank((CharSequence)currentNodeId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u6709\u6548\u7684\u5206\u7ec4\u8282\u70b9\u3002", (String)"PermfilesListPlugin_16", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return true;
        }
        if (!prop.isNeedRefreshTree()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u7981\u7528\u5237\u65b0\u60c5\u51b5\u4e0d\u5141\u8bb8\u4fee\u6539\u8282\u70b9", (String)"PermfilesListPlugin_17", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return true;
        }
        return false;
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean buildBtnDelErrorMsg(GroupProp prop, String currentNodeId) {
        if (this.getTreeModel().getRoot().getId().equals(currentNodeId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u5220\u9664\u6839\u8282\u70b9\u3002", (String)"PermfilesListPlugin_18", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return true;
        }
        if (StringUtils.isBlank((CharSequence)currentNodeId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u6709\u6548\u7684\u5206\u7ec4\u8282\u70b9\u3002", (String)"PermfilesListPlugin_16", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return true;
        }
        if (!prop.isNeedRefreshTree()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u7981\u7528\u5237\u65b0\u60c5\u51b5\u4e0d\u5141\u8bb8\u5220\u9664\u8282\u70b9", (String)"PermfilesListPlugin_19", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return true;
        }
        return !this.canDelGroup(currentNodeId);
    }

    private boolean canDelGroup(String currentNodeId) {
        HRBaseServiceHelper permfileGrpHelper = new HRBaseServiceHelper("hrcs_permfilegrp");
        QFilter[] permfileGrpFilters = new QFilter[]{new QFilter("parent", "=", (Object)Long.valueOf(currentNodeId))};
        DynamicObjectCollection permfileGrpColl = permfileGrpHelper.queryOriginalCollection("id", permfileGrpFilters);
        if (permfileGrpColl.size() > 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u5220\u9664\u8be5\u5206\u7ec4\uff0c\u56e0\u4e3a\u5b83\u5305\u542b\u4e0b\u7ea7\u5206\u7ec4\u3002", (String)"PermfilesListPlugin_21", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_permfilegrpmember");
        QFilter[] filters = new QFilter[]{new QFilter("permfilegrp", "=", (Object)Long.valueOf(currentNodeId))};
        DynamicObjectCollection coll = helper.queryOriginalCollection("id", filters);
        if (coll.size() > 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u5206\u7ec4\u5b58\u5728\u6743\u9650\u6863\u6848\uff0c\u4e0d\u5141\u8bb8\u5220\u9664\u3002", (String)"PermfilesListPlugin_22", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        return true;
    }

    private void editGroupNode(String entityId, String currentNodeId) {
        BaseShowParameter formShowParameter = this.createFormShowParameter(entityId, currentNodeId, "group_bar_edit");
        formShowParameter.setPkId(this.getTreeModel().getCurrentNodeId());
        formShowParameter.setStatus(OperationStatus.EDIT);
        formShowParameter.setCustomParam("operate", (Object)"edit");
        HashMap<String, String> parentInfo = new HashMap<String, String>();
        parentInfo.put("value", currentNodeId);
        formShowParameter.setCustomParam("tree_curr_id", parentInfo);
        this.getView().showForm((FormShowParameter)formShowParameter);
    }

    private void addGroupNode(String entityId, String currentNodeId) {
        BasedataEntityType basedataEntityType;
        BaseShowParameter formShowParameter = this.createFormShowParameter(entityId, currentNodeId, "group_bar_add");
        formShowParameter.setCustomParam("operate", (Object)"addnew");
        if (!currentNodeId.equals(this.getTreeModel().getRoot().getId()) && (basedataEntityType = (BasedataEntityType)EntityMetadataCache.getDataEntityType((String)entityId)) != null) {
            this.doSetCustomParam(basedataEntityType, currentNodeId, (BillShowParameter)formShowParameter);
        }
        this.getView().showForm((FormShowParameter)formShowParameter);
    }

    protected BaseShowParameter createFormShowParameter(String entityId, String currentNodeId, String actionId) {
        BaseShowParameter formShowParameter = new BaseShowParameter();
        formShowParameter.setFormId(entityId);
        formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
        formShowParameter.setCustomParam("table", (Object)this.getTreeModel().getGroupProp().getGroupTableName());
        CloseCallBack callBack = new CloseCallBack((IFormPlugin)this, actionId);
        formShowParameter.setCloseCallBack(callBack);
        formShowParameter.setCustomParam("id", (Object)currentNodeId);
        return formShowParameter;
    }

    private void doSetCustomParam(BasedataEntityType basedataEntityType, String currentNodeId, BillShowParameter formShowParameter) {
        HashMap<String, String> parentInfo = new HashMap<String, String>();
        for (IDataEntityProperty pro : basedataEntityType.getProperties()) {
            if (!(pro instanceof ParentBasedataProp)) continue;
            parentInfo.put("key", pro.getName());
            parentInfo.put("value", currentNodeId);
            formShowParameter.setCustomParam("tree_parent_id", parentInfo);
            break;
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        args.setCancel(true);
        BillList source = (BillList)args.getHyperLinkClickEvent().getSource();
        ListSelectedRow currentSelectedRowInfo = source.getCurrentSelectedRowInfo();
        BillShowParameter showParameter = new BillShowParameter();
        CloseCallBack callBack = new CloseCallBack((IFormPlugin)this, NEW_PERMFILE);
        showParameter.setCloseCallBack(callBack);
        showParameter.setFormId(HBSS_USERPERMFILE);
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        if (this.validateModifyPermission()) {
            showParameter.setStatus(OperationStatus.EDIT);
        } else {
            showParameter.setStatus(OperationStatus.VIEW);
        }
        showParameter.setPkId(currentSelectedRowInfo.getPrimaryKeyValue());
        this.getView().showForm((FormShowParameter)showParameter);
    }

    private boolean validateModifyPermission() {
        return this.checkPermission("4715a0df000000ac", ResManager.loadKDString((String)"\u4fee\u6539", (String)"PermfilesListPlugin_12", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
    }

    private boolean checkPermission(String permItemId, String opName) {
        long userId = RequestContext.get().getCurrUserId();
        boolean hasPerm = PermissionServiceHelper.checkPermission((Long)userId, (String)HBSS_APP_ID, (String)HBSS_USERPERMFILE, (String)permItemId);
        return hasPerm;
    }

    @ExcludeFromJacocoGeneratedReport
    public QFilter nodeClickFilter() {
        QFilter resultFilter = super.nodeClickFilter();
        TreeView userGroupTree = (TreeView)this.getControl(TREEVIEW);
        String focusNodeId = userGroupTree.getTreeState().getFocusNodeId();
        if (HRStringUtils.isNotEmpty((String)focusNodeId) && !HRStringUtils.equals((String)ID_ROOTNODE, (String)focusNodeId)) {
            QFilter groupFilter = new QFilter("hrcs_permfilegrpmember.permfilegrp.id", "=", (Object)focusNodeId);
            resultFilter = resultFilter == null ? groupFilter : resultFilter.and(groupFilter);
        }
        return resultFilter;
    }

    public void filterContainerInit(FilterContainerInitArgs args) {
        List listFilterColumns = args.getFilterContainerInitEvent().getCommonFilterColumns();
        for (FilterColumn listFilter : listFilterColumns) {
            CommonFilterColumn commFilter = (CommonFilterColumn)listFilter;
            String fieldName = commFilter.getFieldName();
            if (!HRStringUtils.equals((String)PERMFILEENABLE, (String)fieldName)) continue;
            commFilter.setDefaultValue("1");
            break;
        }
    }

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        TreeView treeView = (TreeView)this.getView().getControl(TREEVIEW);
        treeView.addTreeNodeQueryListener((TreeNodeQueryListener)this);
    }

    @ExcludeFromJacocoGeneratedReport
    public void queryTreeNodeChildren(TreeNodeEvent evt) {
        this.treeNodeClick(evt);
    }

    @ExcludeFromJacocoGeneratedReport
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        long level = HRPermServiceHelper.getUserGroupMinLevel();
        if (-1L == level || level > 2L) {
            List userPermFileIds = HRPermServiceHelper.getUserPermFile();
            setFilterEvent.getQFilters().add(new QFilter("org.id", "in", (Object)userPermFileIds));
        }
    }

    @ExcludeFromJacocoGeneratedReport
    protected void genPermFiles() {
        if (!RoleService.isAdmin()) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u60a8\u65e0\u6cd5\u8bbf\u95ee\u8be5\u529f\u80fd\uff0c\u56e0\u4e3a\u60a8\u4e0d\u662fHR\u9886\u57df\u7ba1\u7406\u5458\u3002", (String)"PermfilesListPlugin_39", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        IHRAppCache appCache = HRAppCache.get((String)"hrcs");
        String taskId = (String)appCache.get("syncPermFilesTaskId", String.class);
        TaskInfo taskInfo = null;
        if (StringUtils.isNotEmpty((CharSequence)taskId)) {
            taskInfo = this.getTaskInfoByTaskId(taskId);
        }
        if (null != taskInfo && !taskInfo.isTaskEnd()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u540e\u53f0\u4efb\u52a1\u5df2\u6267\u884c\uff0c\u70b9\u51fb\u201c\u67e5\u770b\u8fdb\u5ea6\u201d\u3002", (String)"PermfilesListPlugin_44", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        this.showSyncPermForm();
    }

    private void showSyncPermForm() {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_syncpermfile");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        StyleCss styleCss = new StyleCss();
        styleCss.setWidth("440");
        styleCss.setHeight("330");
        showParameter.getOpenStyle().setInlineStyleCss(styleCss);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CONFIRMCALLBACK_SAVE));
        this.getView().showForm(showParameter);
    }

    @ExcludeFromJacocoGeneratedReport
    private void launchSyncPermfileJob(List<Long> laborrelTypeIds) {
        IHRAppCache appCache = HRAppCache.get((String)"hrcs");
        String taskId = (String)appCache.get("syncPermFilesTaskId", String.class);
        TaskInfo taskInfo = null;
        if (StringUtils.isNotEmpty((CharSequence)taskId)) {
            taskInfo = this.getTaskInfoByTaskId(taskId);
        }
        if (null != taskInfo && !taskInfo.isTaskEnd()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5df2\u6709\u540e\u53f0\u6267\u884c\u4efb\u52a1\uff0c\u65e0\u9700\u518d\u6267\u884c\u3002", (String)"PermfilesListPlugin_35", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        JobInfo jobInfo = new JobInfo();
        jobInfo.setAppId("hrcs");
        jobInfo.setJobType(JobType.REALTIME);
        jobInfo.setRunByUserId(RequestContext.get().getCurrUserId());
        jobInfo.setName(String.format(ResManager.loadKDString((String)"\u540c\u6b65\u6743\u9650\u6863\u6848", (String)"PermfilesListPlugin_32", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), new Object[0]));
        jobInfo.setId(UUID.randomUUID().toString());
        jobInfo.setRunByLang(RequestContext.get().getLang());
        jobInfo.setTaskClassname("kd.hr.hrcs.bussiness.task.SyncPermFilesTask");
        HashMap<String, List<Long>> params = new HashMap<String, List<Long>>(16);
        params.put("laborrelTypeIds", laborrelTypeIds);
        jobInfo.setParams(params);
        this.getView().getFormShowParameter().getCustomParams().putAll(params);
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "genPermFiles");
        JobFormInfo jobFormInfo = new JobFormInfo(jobInfo);
        jobFormInfo.setCaption(String.format(ResManager.loadKDString((String)"\u540c\u6b65\u6743\u9650\u6863\u6848", (String)"PermfilesListPlugin_32", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), new Object[0]));
        jobFormInfo.setCanBackground(true);
        jobFormInfo.setCanStop(true);
        jobFormInfo.setCloseCallBack(closeCallBack);
        jobFormInfo.setClickClassName("kd.hr.hrcs.bussiness.task.SyncPermFilesTaskClick");
        this.dispatch(jobFormInfo, this.getView());
    }

    @ExcludeFromJacocoGeneratedReport
    private void dispatch(JobFormInfo jobFormInfo, IFormView view) {
        jobFormInfo.setRootPageId(view.getFormShowParameter().getRootPageId());
        jobFormInfo.setParentPageId(view.getPageId());
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_syncprocess");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        if (StringUtils.isNotEmpty((CharSequence)jobFormInfo.getCaption())) {
            showParameter.setCaption(jobFormInfo.getCaption());
        } else if (StringUtils.isBlank((CharSequence)jobFormInfo.getJobInfo().getName())) {
            showParameter.setCaption(jobFormInfo.getJobInfo().getName());
        }
        showParameter.getCustomParams().put("ServiceAppId", view.getFormShowParameter().getServiceAppId());
        String jobInfoStr = SerializationUtils.toJsonString((Object)jobFormInfo);
        showParameter.getCustomParams().put("sch_clientjobinfo", jobInfoStr);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "syncPermFilesTask"));
        showParameter.setShowClose(false);
        view.showForm(showParameter);
    }

    @ExcludeFromJacocoGeneratedReport
    private TaskInfo getTaskInfoByTaskId(String taskId) {
        String strSQL2 = "SELECT FID,FSTATUS FROM T_SCH_TASK WHERE FID= ? ";
        Object[] params = new SqlParameter[]{new SqlParameter(":FID", 12, (Object)taskId)};
        DBRoute Sch_Route = DBRoute.basedata;
        return (TaskInfo)DB.query((DBRoute)Sch_Route, (String)strSQL2, (Object[])params, rs -> {
            TaskInfo taskInfo = new TaskInfo();
            if (rs.next()) {
                taskInfo.setId(rs.getString("FID"));
                taskInfo.setStatus(rs.getString("FSTATUS"));
            }
            return taskInfo;
        });
    }

    @ExcludeFromJacocoGeneratedReport
    private void copyPerm(ListSelectedRowCollection rows) {
        Object[] ids = rows.getPrimaryKeyValues();
        Long permfileId = (Long)ids[0];
        if (!this.checkCanCopy(permfileId)) {
            return;
        }
        this.showCopyPermForm(permfileId);
    }

    @ExcludeFromJacocoGeneratedReport
    private void showCopyPermForm(Long permfileId) {
        FormShowParameter showParameter = new FormShowParameter();
        String pageId = this.getView().getPageId() + "_hrcs_copyperm_" + permfileId;
        showParameter.setPageId(pageId);
        showParameter.setFormId("hrcs_copyperm");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        StyleCss styleCss = new StyleCss();
        styleCss.setWidth("1000");
        styleCss.setHeight("600");
        showParameter.getOpenStyle().setInlineStyleCss(styleCss);
        showParameter.setCaption(ResManager.loadKDString((String)"\u590d\u5236\u6743\u9650", (String)"PermfilesListPlugin_43", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        showParameter.setCustomParam("permfileId", (Object)permfileId);
        this.getView().showForm(showParameter);
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean checkCanCopy(Long permFileId) {
        if (this.checkRoleForBidden(permFileId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u8bb0\u5f55\u5df2\u5931\u6548\u6216\u7528\u6237\u7981\u7528\uff0c\u4e0d\u5141\u8bb8\u590d\u5236\u6743\u9650\u3002", (String)"PermfilesListPlugin_41", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        Object[] relations = this.getUserRelatesByPermFields(new Object[]{permFileId});
        if (ArrayUtils.isEmpty((Object[])relations)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u7528\u6237\u6ca1\u6709\u53ef\u590d\u5236\u7684\u89d2\u8272\u3002", (String)"PermfilesListPlugin_42", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        Set roleIds = HRRolePermHelper.queryViewableRoles((long)RequestContext.get().getCurrUserId());
        ArrayList viewRelations = Lists.newArrayListWithCapacity((int)relations.length);
        for (Object relation : relations) {
            String roleId = relation.getString("role.id");
            if (!roleIds.contains(roleId)) continue;
            viewRelations.add(relation);
        }
        if (CollectionUtils.isEmpty((Collection)viewRelations)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u7528\u6237\u6ca1\u6709\u53ef\u590d\u5236\u7684\u89d2\u8272\u3002", (String)"PermfilesListPlugin_42", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        return true;
    }

    @ExcludeFromJacocoGeneratedReport
    private DynamicObject[] getUserRelatesByPermFields(Object[] permFileIds) {
        HRBaseServiceHelper permFileHelper = new HRBaseServiceHelper("hrcs_userrolerelat");
        return permFileHelper.query("id,user.id,user.name,user.number,permfile.org.id,permfile.org.name,permfile.org.number,role.id,role.number,role.name,role.enable,customenable,validstart,validend,creator", new QFilter[]{new QFilter("permfile.id", "in", (Object)permFileIds), new QFilter("role.enable", "=", (Object)"1")}, "role.number,user.name");
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean checkRoleForBidden(Long permfileId) {
        DynamicObject permFile = RoleMemberAssignServiceHelper.getPermFileById((Long)permfileId);
        return Objects.isNull(permFile) || permFile.getBoolean("user.isforbidden") || HRStringUtils.equals((String)permFile.getString(PERMFILEENABLE), (String)PERFILE_STATUS_0);
    }
}
