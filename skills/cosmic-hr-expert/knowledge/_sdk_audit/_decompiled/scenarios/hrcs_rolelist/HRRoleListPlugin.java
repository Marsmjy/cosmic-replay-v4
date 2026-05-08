/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.common.collect.Sets$SetView
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.SqlParameter
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ArrayUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.DB
 *  kd.bos.db.DBRoute
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.BasedataEntityType
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.ValueTextItem
 *  kd.bos.entity.datamodel.ITreeModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.operate.IOperationResult
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.property.GroupProp
 *  kd.bos.entity.property.ParentBasedataProp
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.exception.BosErrorCode
 *  kd.bos.exception.KDException
 *  kd.bos.filter.CommonFilterColumn
 *  kd.bos.filter.FilterColumn
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.form.control.events.TreeNodeQueryListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.AbstractTreeListView
 *  kd.bos.list.BillList
 *  kd.bos.list.F7SelectedList
 *  kd.bos.list.IListView
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.list.events.ChatEvent
 *  kd.bos.list.events.CreateTreeListViewEvent
 *  kd.bos.list.events.ListRowClickEvent
 *  kd.bos.list.events.ListRowClickListener
 *  kd.bos.log.api.ILogService
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.form.mcontrol.SearchAp
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.cache.CacheMrg
 *  kd.bos.permission.cache.util.PermCommonUtil
 *  kd.bos.schedule.api.TaskInfo
 *  kd.bos.service.ServiceFactory
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.operation.DeleteServiceHelper
 *  kd.bos.servicehelper.operation.SaveServiceHelper
 *  kd.hr.hbp.business.dao.factory.HRBaseDaoFactory
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.cache.HRPageCache
 *  kd.hr.hbp.common.model.FormModel
 *  kd.hr.hbp.common.util.Conditional
 *  kd.hr.hbp.common.util.HRShowPageUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRStandardTreeList
 *  kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr
 *  kd.hr.hrcs.bussiness.service.perm.RoleService
 *  kd.hr.hrcs.bussiness.service.perm.log.PermLogService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.PermDBServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.RoleMemberAssignServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.RoleServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.log.PermLogRoleStatusServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.log.RolePermLogServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.role.HRRolePermHelper
 *  kd.hr.hrcs.common.constants.perm.log.RoleInfoLogModel
 *  kd.hr.hrcs.formplugin.web.perm.init.excel.PermHelper
 *  kd.hr.hrcs.formplugin.web.perm.role.CommonTreeListView
 *  kd.hr.hrcs.formplugin.web.perm.role.HRRoleListPlugin$HRRoleProvider
 */
package kd.hr.hrcs.formplugin.web.perm.role;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.SqlParameter;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ArrayUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.DB;
import kd.bos.db.DBRoute;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.BasedataEntityType;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.ValueTextItem;
import kd.bos.entity.datamodel.ITreeModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.operate.IOperationResult;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.property.GroupProp;
import kd.bos.entity.property.ParentBasedataProp;
import kd.bos.entity.tree.TreeNode;
import kd.bos.exception.BosErrorCode;
import kd.bos.exception.KDException;
import kd.bos.filter.CommonFilterColumn;
import kd.bos.filter.FilterColumn;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.container.Tab;
import kd.bos.form.control.Control;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.control.events.TreeNodeQueryListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.AbstractTreeListView;
import kd.bos.list.BillList;
import kd.bos.list.F7SelectedList;
import kd.bos.list.IListView;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.list.events.ChatEvent;
import kd.bos.list.events.CreateTreeListViewEvent;
import kd.bos.list.events.ListRowClickEvent;
import kd.bos.list.events.ListRowClickListener;
import kd.bos.log.api.ILogService;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.form.mcontrol.SearchAp;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.cache.CacheMrg;
import kd.bos.permission.cache.util.PermCommonUtil;
import kd.bos.schedule.api.TaskInfo;
import kd.bos.service.ServiceFactory;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.DeleteServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.hr.hbp.business.dao.factory.HRBaseDaoFactory;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.cache.HRPageCache;
import kd.hr.hbp.common.model.FormModel;
import kd.hr.hbp.common.util.Conditional;
import kd.hr.hbp.common.util.HRShowPageUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRStandardTreeList;
import kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr;
import kd.hr.hrcs.bussiness.service.perm.RoleService;
import kd.hr.hrcs.bussiness.service.perm.log.PermLogService;
import kd.hr.hrcs.bussiness.servicehelper.perm.PermDBServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.RoleMemberAssignServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.RoleServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.log.PermLogRoleStatusServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.log.RolePermLogServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.role.HRRolePermHelper;
import kd.hr.hrcs.common.constants.perm.log.RoleInfoLogModel;
import kd.hr.hrcs.formplugin.web.perm.init.excel.PermHelper;
import kd.hr.hrcs.formplugin.web.perm.role.CommonTreeListView;
import kd.hr.hrcs.formplugin.web.perm.role.HRRoleListPlugin;

public final class HRRoleListPlugin
extends HRStandardTreeList
implements TreeNodeQueryListener {
    private static final String LONGNUMBER = "longnumber";
    private static final String ERROR_MSG = "errorMsg";
    private static final String PARENT = "parent";
    private static final Log LOGGER = LogFactory.getLog(HRRoleListPlugin.class);
    private static final String ID_ROOTNODE_PRESET = "8609760E-EF83-4775-A9FF-CCDEC7C0B689";
    private static final String ID_ROOTNODE = "0";
    private static final String BARITEM_REFRESH = "refresh";
    private static final String BARITEM_ASSIGNROLE = "btn_allocprem";
    private static final String BARITEM_BATCHGROUP = "btn_batchgroup";
    private static final String BARITEM_NEW = "new";
    private static final String BARITEM_DELETE = "delete";
    private static final String BARITEM_DISABLE = "bar_disable";
    private static final String BARITEM_ENABLE = "bar_enable";
    private static final String BILLLISTAP = "billlistap";
    private static final String BARITEM_ASSIGNMEMBER = "bar_assignmember";
    private static final String BARITEM_EXPORT_ROLE_PERM = "exportroleperm";
    private static final String BARITEM_INIT_ROLE_PERM = "inituserperm";
    private static final String BARITEM_ALERTUSERPERM = "baritem_alteruserperm";
    private static final String BARITEM_SHARE = "";
    private static final String BARITEM_EXPORT_ROLE = "bar_exportrole";
    private static final String BARITEM_INIT_ROLE = "bar_initrole";
    private static final String BARITEM_COPY_ROLE = "bar_copy";
    private static final String ENTITYTYPE_BOSROLEGRP = "perm_rolegroup";
    private static final String ENTITYTYPE_HRROLEGRP = "hrcs_rolegrp";
    private static final String CALLBACKID_DEL = "callBackId_del";
    private static final String HRCS_APPID = PermCommonUtil.getAppIdFromSuspectedAppNum((String)"hrcs");
    private CommonTreeListView treeListView = new CommonTreeListView("perm_rolegroup");
    private static final Map<String, String> IMPORT_TEMP_TYPE = ImmutableMap.of((Object)"importRoleFuns", (Object)"importRoleFun", (Object)"importUserDataRule", (Object)"importUserDr", (Object)"importUserFieldPerm", (Object)"importUserFp", (Object)"importUserOrg", (Object)"importUserOrg");
    private static final String ACTION_ID_EXPORT = "exportUrl";
    private static final String TASK_EXPORT_ROLE_USER = "kd.hr.hrcs.formplugin.web.perm.init.task.RoleUserExportTask";
    private static final String TASK_EXPORT_ROLE = "kd.hr.hrcs.formplugin.web.perm.init.task.RoleExportTask";

    public HRRoleListPlugin() {
        super(ENTITYTYPE_BOSROLEGRP, ID_ROOTNODE_PRESET, false);
    }

    @ExcludeFromJacocoGeneratedReport
    public void chat(ChatEvent chatEvent) {
        chatEvent.setCancel(true);
    }

    public void createTreeListView(CreateTreeListViewEvent event) {
        super.createTreeListView(event);
        event.setView((AbstractTreeListView)this.treeListView);
    }

    public void initTreeToolbar(EventObject event) {
        super.initTreeToolbar(event);
        this.getView().setVisible(Boolean.TRUE, new String[]{"btnnew", "btnedit", "btndel"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"iscontainnow", "iscontainlower"});
    }

    protected DynamicObject getRootDynamicObject() {
        DynamicObject rootDy = BusinessDataServiceHelper.newDynamicObject((String)ENTITYTYPE_BOSROLEGRP);
        rootDy.set("id", (Object)ID_ROOTNODE);
        rootDy.set("name", (Object)ResManager.loadKDString((String)"\u5168\u90e8", (String)"HRRoleListPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        rootDy.set(PARENT, (Object)BARITEM_SHARE);
        rootDy.set(LONGNUMBER, (Object)BARITEM_SHARE);
        return rootDy;
    }

    protected DynamicObjectCollection getTreeViewCollection(String entityName, String parentId) {
        DynamicObjectCollection dyColl;
        DynamicObjectCollection hrRoleColl = HRBaseDaoFactory.getInstance((String)ENTITYTYPE_HRROLEGRP).queryColl("id", null, null);
        ArrayList idList = new ArrayList(16);
        hrRoleColl.forEach(row -> idList.add(row.getString("id")));
        String selectFields = "id, name, parent.id, longnumber, isleaf";
        QFilter qfilter = new QFilter("id", "in", idList);
        if (!HRStringUtils.equals((String)ID_ROOTNODE, (String)parentId) && !HRStringUtils.equals((String)parentId, (String)ID_ROOTNODE_PRESET)) {
            qfilter.and(PARENT, "in", (Object)parentId);
        }
        if (Objects.isNull(dyColl = this.getRoleGrp(selectFields, qfilter))) {
            dyColl = new DynamicObjectCollection();
        }
        return dyColl;
    }

    private DynamicObjectCollection getRoleGrp(String selectFields, QFilter qfilter) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper(ENTITYTYPE_BOSROLEGRP);
        DynamicObject[] objects = helper.query(selectFields, new QFilter[]{qfilter}, LONGNUMBER);
        DynamicObjectCollection dyColl = null;
        if (objects.length > 0) {
            DynamicObject obj = objects[0];
            dyColl = new DynamicObjectCollection(obj.getDynamicObjectType(), (Object)obj);
            for (DynamicObject dynamicObject : objects) {
                dynamicObject.set(PARENT, Objects.isNull(dynamicObject.get(PARENT)) ? Integer.valueOf(0) : dynamicObject.get("parent.id"));
                dyColl.add((Object)dynamicObject);
            }
        }
        return dyColl;
    }

    public void beforeBindData(EventObject event) {
        super.beforeBindData(event);
        this.updateSearch();
    }

    private void updateSearch() {
        SearchAp search = new SearchAp();
        search.setKey("searchap");
        search.setSearchEmptyText(new LocaleString(ResManager.loadKDString((String)"\u8bf7\u8f93\u5165\u89d2\u8272\u7ec4\u540d\u79f0", (String)"HRRoleListPlugin_25", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        this.getView().updateControlMetadata("searchap", search.createControl());
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        long currUserId = RequestContext.get().getCurrUserId();
        QFilter filter = new QFilter("id", "in", (Object)HRRolePermHelper.queryViewableRoles((long)currUserId));
        setFilterEvent.getQFilters().add(filter);
    }

    protected QFilter buildNodeClickFilter(BuildTreeListFilterEvent event) {
        String focusNodeId = event.getNodeId().toString();
        QFilter nodeClicFilter = null;
        if (!this.getTreeModel().getRoot().getId().equals(focusNodeId)) {
            QFilter focusNodeFilter = new QFilter("id", "=", (Object)focusNodeId);
            DynamicObject focusNodeDy = HRBaseDaoFactory.getInstance((String)this.getEntityName()).queryOne(LONGNUMBER, focusNodeFilter);
            if (ObjectUtils.isEmpty((Object)focusNodeDy)) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(focusNodeId);
                this.getTreeModel().deleteNode(treeNode, true);
                this.getTreeListView().focusRootNode();
                this.getTreeListView().refreshTreeView();
                this.getTreeListView().refresh();
            } else {
                List<String> list = this.getSubGrp(focusNodeDy);
                nodeClicFilter = new QFilter("perm_role.group.id", "in", list);
            }
        }
        return nodeClicFilter;
    }

    private List<String> getSubGrp(DynamicObject focusNodeDy) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper(ENTITYTYPE_BOSROLEGRP);
        QFilter[] filters = new QFilter[]{new QFilter(LONGNUMBER, "like", (Object)(focusNodeDy.getString(LONGNUMBER) + "." + "%"))};
        DynamicObjectCollection coll = helper.queryOriginalCollection("id", filters);
        ArrayList list = Lists.newArrayListWithExpectedSize((int)coll.size());
        for (DynamicObject obj : coll) {
            list.add(obj.getString("id"));
        }
        list.add(focusNodeDy.getString("id"));
        return list;
    }

    public void filterContainerInit(FilterContainerInitArgs args) {
        List listFilterColumns = args.getFilterContainerInitEvent().getCommonFilterColumns();
        for (FilterColumn listFilter : listFilterColumns) {
            CommonFilterColumn commFilter = (CommonFilterColumn)listFilter;
            String fieldName = commFilter.getFieldName();
            if (!HRStringUtils.equals((String)"perm_role.enable", (String)fieldName)) continue;
            commFilter.setDefaultValue("1");
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void filterContainerBeforeF7Select(BeforeFilterF7SelectEvent args) {
        super.filterContainerBeforeF7Select(args);
        String fieldName = args.getFieldName();
        if (HRStringUtils.equals((String)"perm_role.group.name", (String)fieldName)) {
            HRBaseServiceHelper roleGrpHelper = new HRBaseServiceHelper(ENTITYTYPE_HRROLEGRP);
            QFilter[] filters = new QFilter[]{QFilter.isNotNull((String)"id")};
            DynamicObjectCollection roleGrpColl = roleGrpHelper.queryOriginalCollection("id", filters);
            ArrayList idList = Lists.newArrayListWithExpectedSize((int)roleGrpColl.size());
            for (DynamicObject roleGrp : roleGrpColl) {
                idList.add(roleGrp.getString("id"));
            }
            QFilter filter = new QFilter("id", "in", (Object)idList);
            args.addCustomQFilter(filter);
        }
    }

    public void treeToolbarClick(EventObject event) {
        ITreeModel treeModel = this.getTreeModel();
        Control ctl = (Control)event.getSource();
        GroupProp prop = this.getTreeModel().getGroupProp();
        String currentNodeId = (String)treeModel.getCurrentNodeId();
        switch (ctl.getKey()) {
            case "btnnew": {
                this.addGroupNode(ENTITYTYPE_HRROLEGRP, currentNodeId);
                break;
            }
            case "btnedit": {
                this.editAction(prop, currentNodeId);
                break;
            }
            case "btndel": {
                this.delAction(prop, currentNodeId);
                break;
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void delAction(GroupProp prop, String currentNodeId) {
        if (this.getTreeModel().getRoot().getId().equals(currentNodeId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u5220\u9664\u6839\u8282\u70b9\u3002", (String)"HRRoleListPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        if (StringUtils.isBlank((CharSequence)currentNodeId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u6709\u6548\u7684\u5206\u7ec4\u8282\u70b9\u3002", (String)"HRRoleListPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        if (!prop.isNeedRefreshTree()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u7981\u7528\u5237\u65b0\u60c5\u51b5\u4e0d\u5141\u8bb8\u5220\u9664\u8282\u70b9", (String)"HRRoleListPlugin_5", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        if (!this.canDelGroup(currentNodeId)) {
            return;
        }
        TreeNode currentNode = this.getTreeModel().getRoot().getTreeNode(currentNodeId, 10);
        String text = currentNode.getText();
        ConfirmCallBackListener confirmCallBacks = new ConfirmCallBackListener("group_bar_del", (IFormPlugin)this);
        String strs = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u60a8\u786e\u8ba4\u5220\u9664\u5206\u7ec4%s\uff1f", (String)"HRRoleListPlugin_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), text);
        this.getView().showConfirm(strs, MessageBoxOptions.YesNo, confirmCallBacks);
    }

    private void editAction(GroupProp prop, String currentNodeId) {
        if (this.getTreeModel().getRoot().getId().equals(currentNodeId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u7f16\u8f91\u6839\u8282\u70b9\u3002", (String)"HRRoleListPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        if (StringUtils.isBlank((CharSequence)currentNodeId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u6709\u6548\u7684\u5206\u7ec4\u8282\u70b9\u3002", (String)"HRRoleListPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        if (!prop.isNeedRefreshTree()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u7981\u7528\u5237\u65b0\u60c5\u51b5\u4e0d\u5141\u8bb8\u4fee\u6539\u8282\u70b9", (String)"HRRoleListPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        this.editGroupNode(ENTITYTYPE_HRROLEGRP, currentNodeId);
    }

    public void queryTreeNodeChildren(TreeNodeEvent event) {
        this.treeNodeClick(event);
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        if (event.getResult() == MessageBoxResult.Yes && "group_bar_del".equals(event.getCallBackId())) {
            this.doDeleteGroup();
        }
    }

    private void doDeleteGroup() {
        String nodeId = (String)this.getTreeModel().getCurrentNodeId();
        GroupProp props = this.getTreeListView().getTreeModel().getGroupProp();
        ArrayList list = new ArrayList(16);
        Optional.ofNullable(props).ifPresent(prop -> {
            BasedataEntityType entityType = prop.getEntityType();
            DynamicObjectCollection collection = this.getChildrenDynamicObject(new Object[]{nodeId}, entityType);
            if (!CollectionUtils.isEmpty((Collection)collection)) {
                collection.forEach(dy -> list.add(dy.getPkValue()));
            }
        });
        if (list.size() > 1) {
            return;
        }
        IOperationResult result = this.getTreeModel().deleteGroup(new Object[]{nodeId});
        if (result.isSuccess()) {
            HRBaseServiceHelper helper = new HRBaseServiceHelper(ENTITYTYPE_HRROLEGRP);
            helper.delete(list.toArray());
            this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f", (String)"HRRoleListPlugin_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        } else {
            this.getView().showOperationResult((OperationResult)result, ResManager.loadKDString((String)"\u5220\u9664", (String)"HRRoleListPlugin_10", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        }
        if (!result.getSuccessPkIds().isEmpty()) {
            TreeNode node = this.getTreeModel().getRoot().getTreeNode(nodeId, 10);
            this.getTreeModel().deleteNode(node, false);
            TreeNode parentNode = this.getTreeModel().getRoot().getTreeNode(node.getParentid(), 10);
            Optional.ofNullable(parentNode).ifPresent(treeNode -> {
                this.treeListView.getTreeModel().refreshNode((Object)treeNode.getId());
                this.treeListView.refreshTreeNode(treeNode.getId());
                this.treeListView.getTreeView().treeNodeClick(treeNode.getParentid(), treeNode.getId());
            });
        }
    }

    private boolean canDelGroup(String nodeId) {
        HRBaseServiceHelper roleGrpHelper = new HRBaseServiceHelper(ENTITYTYPE_BOSROLEGRP);
        QFilter[] roleGrpFilters = new QFilter[]{new QFilter(PARENT, "=", (Object)nodeId)};
        DynamicObjectCollection roleGrpColl = roleGrpHelper.queryOriginalCollection("id", roleGrpFilters);
        if (roleGrpColl.size() > 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u5220\u9664\u8be5\u5206\u7ec4\uff0c\u56e0\u4e3a\u5b83\u5305\u542b\u4e0b\u7ea7\u5206\u7ec4\u3002", (String)"HRRoleListPlugin_27", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        HRBaseServiceHelper helper = new HRBaseServiceHelper("perm_role");
        QFilter[] roleFilters = new QFilter[]{new QFilter("group", "=", (Object)nodeId)};
        DynamicObjectCollection roleColl = helper.queryOriginalCollection("id", roleFilters);
        if (roleColl.size() > 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u5206\u7ec4\u5b58\u5728\u89d2\u8272\uff0c\u4e0d\u5141\u8bb8\u5220\u9664\u3002", (String)"HRRoleListPlugin_28", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        return true;
    }

    @ExcludeFromJacocoGeneratedReport
    private void confirmDeleteRoleInfo() {
        IListView iListView = (IListView)this.getView();
        ListSelectedRowCollection roleList = iListView.getSelectedRows();
        int total = roleList.size();
        HRPageCache cache = new HRPageCache(this.getView());
        List roleIdList = (List)cache.get("canDelRoleList", List.class);
        int errorCount = (Integer)cache.get("errorCount", Integer.class);
        String errorMsg = (String)cache.get(ERROR_MSG, String.class);
        if (!CollectionUtils.isEmpty((Collection)roleIdList)) {
            try {
                for (String roleId : roleIdList) {
                    RoleInfoLogModel beforeRoleInfoLogModel = new RoleInfoLogModel();
                    RolePermLogServiceHelper.setPermLogDataByRoleId((RoleInfoLogModel)beforeRoleInfoLogModel, null, (String)roleId);
                    RoleInfoLogModel afterRoleInfoLogModel = new RoleInfoLogModel();
                    PermLogService.initPermLog((String)"del", (RoleInfoLogModel)beforeRoleInfoLogModel, (RoleInfoLogModel)afterRoleInfoLogModel, (String)roleId);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (this.deleteBosRoleById(roleIdList)) {
                this.deleteHrRoleById(roleIdList);
            }
        }
        if (errorCount == 0) {
            this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f\u3002", (String)"HRRoleListPlugin_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        } else if (errorCount == 1 && (Objects.isNull(roleIdList) || roleIdList.size() == 0)) {
            this.getView().showSuccessNotification(errorMsg.substring(0, errorMsg.length() - 4));
        } else {
            String msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5171%1$s\u6761\u5355\u636e\uff0c\u5220\u9664\u6210\u529f%2$s\u6761\uff0c\u5931\u8d25%3$s\u6761\u3002", (String)"HRRoleListPlugin_12", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), total, total - errorCount, errorCount);
            FormShowParameter parameter = new FormShowParameter();
            parameter.getOpenStyle().setShowType(ShowType.Modal);
            parameter.setShowTitle(false);
            parameter.setFormId("bos_operationresult");
            parameter.setCustomParam("title", (Object)msg);
            parameter.setCustomParam(ERROR_MSG, (Object)errorMsg);
            this.getView().showForm(parameter);
        }
        BillList billList = (BillList)this.getView().getControl(BILLLISTAP);
        billList.clearSelection();
        this.getView().updateView(BILLLISTAP);
    }

    @ExcludeFromJacocoGeneratedReport
    private void deleteHrRoleById(List<String> roleIds) {
        QFilter filter = new QFilter("role", "in", roleIds);
        QFilter[] filters = new QFilter[]{filter};
        try (TXHandle txHandle = TX.required();){
            try {
                this.doDeleteHrRole(roleIds, filters);
            }
            catch (Exception ex) {
                txHandle.markRollback();
                throw new KDException(BosErrorCode.render, new Object[]{ResManager.loadKDString((String)"\u89d2\u8272\u6743\u9650\u5220\u9664\u5f02\u5e38\u3002%s", (String)"HRRoleListPlugin_13", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{ex.getMessage()})});
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void doDeleteHrRole(List<String> roleIds, QFilter[] filters) {
        HRBaseServiceHelper userRoleRelatHelper = new HRBaseServiceHelper("hrcs_userrolerelat");
        DynamicObject[] userRoleRelats = userRoleRelatHelper.query(new QFilter[]{new QFilter("role", "in", roleIds)});
        Set<Long> userRoleRelatIds = Arrays.stream(userRoleRelats).map(userRoleRelat -> userRoleRelat.getLong("id")).collect(Collectors.toSet());
        DeleteServiceHelper.delete((String)"hrcs_userrole", (QFilter[])new QFilter[]{new QFilter("userrolerealt", "in", userRoleRelatIds)});
        this.delUserDataRule(userRoleRelatIds);
        DeleteServiceHelper.delete((String)"hrcs_userrolerelat", (QFilter[])new QFilter[]{new QFilter("id", "in", userRoleRelatIds)});
        DeleteServiceHelper.delete((String)"hrcs_rolebu", (QFilter[])filters);
        this.delRoleRule(roleIds);
        HRBaseServiceHelper roleFieldPermHelper = new HRBaseServiceHelper("hrcs_rolefield");
        DynamicObjectCollection roleFieldPermColl = roleFieldPermHelper.queryOriginalCollection("id", new QFilter[]{new QFilter("role", "in", roleIds)});
        if (!CollectionUtils.isEmpty((Collection)roleFieldPermColl)) {
            List roleFieldPermIds = roleFieldPermColl.stream().map(roleFieldPerm -> roleFieldPerm.getLong("id")).collect(Collectors.toList());
            DeleteServiceHelper.delete((String)"hrcs_rolefield", (QFilter[])new QFilter[]{new QFilter("id", "in", roleFieldPermIds)});
        }
        DeleteServiceHelper.delete((String)"hrcs_role", (QFilter[])new QFilter[]{new QFilter("id", "in", roleIds)});
        DeleteServiceHelper.delete((String)"hrcs_roledimension", (QFilter[])new QFilter[]{new QFilter("role", "in", roleIds)});
        PermDBServiceHelper.roleDimGrpDBService.deleteByFilter(new QFilter[]{new QFilter("role", "in", roleIds)});
        DeleteServiceHelper.delete((String)"hrcs_roleopenscope", (QFilter[])new QFilter[]{new QFilter("roleid", "in", roleIds)});
        DeleteServiceHelper.delete((String)"hrcs_roleassignscope", (QFilter[])new QFilter[]{new QFilter("roleid", "in", roleIds)});
    }

    @ExcludeFromJacocoGeneratedReport
    private void delRoleRule(List<String> roleIds) {
        HRBaseServiceHelper roleDataRuleHelper = new HRBaseServiceHelper("hrcs_roledatarule");
        DynamicObjectCollection roleDataRuleColl = roleDataRuleHelper.queryOriginalCollection("id", new QFilter[]{new QFilter("role", "in", roleIds)});
        if (!CollectionUtils.isEmpty((Collection)roleDataRuleColl)) {
            List roleDataRuleIds = roleDataRuleColl.stream().map(roleDataRule -> roleDataRule.getLong("id")).collect(Collectors.toList());
            DeleteServiceHelper.delete((String)"hrcs_rolebdruleentry", (QFilter[])new QFilter[]{new QFilter("roledatarule", "in", roleDataRuleIds)});
            DeleteServiceHelper.delete((String)"hrcs_roledataruleentry", (QFilter[])new QFilter[]{new QFilter("roledatarule", "in", roleDataRuleIds)});
            DeleteServiceHelper.delete((String)"hrcs_roledatarule", (QFilter[])new QFilter[]{new QFilter("id", "in", roleDataRuleIds)});
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void delUserDataRule(Set<Long> userRoleRelatIds) {
        HRBaseServiceHelper userDataRuleHelper = new HRBaseServiceHelper("hrcs_userdatarule");
        DynamicObject[] userDataRules = userDataRuleHelper.query(new QFilter[]{new QFilter("userrolerelate", "in", userRoleRelatIds)});
        Set userDataRuleIds = Arrays.stream(userDataRules).map(userDataRule -> userDataRule.getLong("id")).collect(Collectors.toSet());
        DeleteServiceHelper.delete((String)"hrcs_userbdruleentry", (QFilter[])new QFilter[]{new QFilter("userdatarule", "in", userDataRuleIds)});
        DeleteServiceHelper.delete((String)"hrcs_userdataruleentry", (QFilter[])new QFilter[]{new QFilter("userdatarule", "in", userDataRuleIds)});
        DeleteServiceHelper.delete((String)"hrcs_userdatarule", (QFilter[])new QFilter[]{new QFilter("id", "in", userDataRuleIds)});
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean deleteBosRoleById(List<String> roleIds) {
        ArrayList appLogInfoList = Lists.newArrayList();
        HRBaseServiceHelper helper = new HRBaseServiceHelper("perm_role");
        DynamicObject[] roles = helper.query("number", new QFilter[]{new QFilter("id", "in", roleIds)});
        boolean flag = false;
        try (DynamicObject[] txHandle = TX.required();){
            try {
                this.doDeleteBosRole(roleIds);
                flag = true;
            }
            catch (Exception e) {
                txHandle.markRollback();
                boolean bl = false;
                if (txHandle != null) {
                    if (var7_7 != null) {
                        try {
                            txHandle.close();
                        }
                        catch (Throwable throwable) {
                            var7_7.addSuppressed(throwable);
                        }
                    } else {
                        txHandle.close();
                    }
                }
                return bl;
            }
        }
        if (flag) {
            for (DynamicObject role : roles) {
                String logDesc = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u53f7%s\uff0c\u5220\u9664\u6210\u529f\u3002", (String)"HRRoleListPlugin_38", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), role.getString("number"));
                MultiLangEnumBridge langEnumBridge = new MultiLangEnumBridge("\u5220\u9664", "HRRoleListPlugin_10", "hrmp-hrcs-formplugin");
                RoleService.addLogNoOpKey((String)BARITEM_DELETE, (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_permfilelist", (String)HRCS_APPID, (List)appLogInfoList, (String)logDesc);
            }
            ILogService service = (ILogService)ServiceFactory.getService(ILogService.class);
            service.addBatchLog((List)Lists.newArrayList((Iterable)appLogInfoList));
        }
        return true;
    }

    @ExcludeFromJacocoGeneratedReport
    private void doDeleteBosRole(List<String> roleIds) {
        DeleteServiceHelper.delete((String)"perm_roleperm", (QFilter[])new QFilter[]{new QFilter("roleid", "in", roleIds)});
        DynamicObject[] arrRoleFieldPerm = BusinessDataServiceHelper.load((String)"perm_rolefieldperm", (String)"fieldperm.id", (QFilter[])new QFilter[]{new QFilter("role", "in", roleIds)});
        ArrayList forDelFieldPermId = new ArrayList(16);
        Optional.ofNullable(arrRoleFieldPerm).ifPresent(fieldPerm -> {
            for (DynamicObject dObj : fieldPerm) {
                forDelFieldPermId.add(dObj.getString("fieldperm.id"));
            }
        });
        if (!CollectionUtils.isEmpty(forDelFieldPermId)) {
            DeleteServiceHelper.delete((String)"perm_fieldperm", (QFilter[])new QFilter[]{new QFilter("id", "in", forDelFieldPermId)});
        }
        DeleteServiceHelper.delete((String)"perm_rolefieldperm", (QFilter[])new QFilter[]{new QFilter("role", "in", roleIds)});
        DynamicObject[] arrRoleDataPerm = BusinessDataServiceHelper.load((String)"perm_roledataperm", (String)"datapermid.id", (QFilter[])new QFilter[]{new QFilter("role", "in", roleIds)});
        ArrayList forDelDataPermId = new ArrayList(16);
        Optional.ofNullable(arrRoleDataPerm).ifPresent(dataPerm -> {
            for (DynamicObject dObj : dataPerm) {
                forDelDataPermId.add(dObj.getString("datapermid.id"));
            }
        });
        if (!CollectionUtils.isEmpty(forDelDataPermId)) {
            DeleteServiceHelper.delete((String)"perm_dataperm", (QFilter[])new QFilter[]{new QFilter("id", "in", forDelDataPermId)});
        }
        DeleteServiceHelper.delete((String)"perm_roledataperm", (QFilter[])new QFilter[]{new QFilter("role", "in", roleIds)});
        DeleteServiceHelper.delete((String)"perm_userrole", (QFilter[])new QFilter[]{new QFilter("role", "in", roleIds)});
        DeleteServiceHelper.delete((String)"perm_role", (QFilter[])new QFilter[]{new QFilter("id", "in", roleIds)});
        for (String roleId : roleIds) {
            this.removeRolePermCache(roleId);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void removeRolePermCache(String roleId) {
        String type = CacheMrg.getType4RolePerm();
        String keyPrefix = roleId + "_";
        CacheMrg.clearCacheWithPrefix((String)type, (String)keyPrefix);
        CacheMrg.clearCache((String)type, (String)roleId);
    }

    private DynamicObjectCollection getChildrenDynamicObject(Object[] ids, BasedataEntityType entityType) {
        ORM orm = ORM.create();
        String number = entityType.getNumberProperty();
        if (Objects.isNull(entityType.getProperty(LONGNUMBER))) {
            return null;
        }
        String entityName = entityType.getName();
        String selectFields = "id,longnumber,parent.longnumber," + number + ",enable";
        String orderBy = "longnumber desc";
        QFilter[] filters = new QFilter[]{new QFilter("id", "in", (Object)ids)};
        DynamicObjectCollection collection = orm.query(entityName, selectFields, filters, orderBy);
        ArrayList<String> list = new ArrayList<String>(16);
        for (DynamicObject dynamicObject : collection) {
            if (dynamicObject.getString(LONGNUMBER) == null || BARITEM_SHARE.equals(dynamicObject.getString(LONGNUMBER).trim())) continue;
            list.add(dynamicObject.getString(LONGNUMBER));
        }
        selectFields = "id,longnumber,parent.longnumber," + number + ",enable";
        QFilter baseQfilter = null;
        ParentBasedataProp parentProp = null;
        DataEntityPropertyCollection props = entityType.getProperties();
        for (IDataEntityProperty prop : props) {
            if (!(prop.getParent() instanceof MainEntityType) || !(prop instanceof ParentBasedataProp)) continue;
            parentProp = (ParentBasedataProp)prop;
            break;
        }
        String longNumberDLM = parentProp == null ? "." : parentProp.getLongNumberDLM();
        baseQfilter = this.buildFilter(ids, list, baseQfilter, longNumberDLM);
        return Objects.isNull(baseQfilter) ? collection : orm.query(entityName, selectFields, new QFilter[]{baseQfilter}, orderBy);
    }

    private QFilter buildFilter(Object[] ids, List<String> list, QFilter baseQfilter, String longNumberDLM) {
        for (Object id : ids) {
            baseQfilter = Objects.isNull(baseQfilter) ? new QFilter("id", "=", id) : baseQfilter.or(new QFilter("id", "=", id));
        }
        for (String longNumber : list) {
            baseQfilter = Objects.isNull(baseQfilter) ? new QFilter(LONGNUMBER, "like", (Object)(longNumber + longNumberDLM + "%")) : baseQfilter.or(new QFilter(LONGNUMBER, "like", (Object)(longNumber + longNumberDLM + "%")));
        }
        return baseQfilter;
    }

    private void addGroupNode(String entityId, String currentNodeId) {
        BaseShowParameter formShowParameter = this.createFormShowParameter(entityId, BARITEM_SHARE, "group_bar_add");
        formShowParameter.setCustomParam("operate", (Object)"addnew");
        if (!currentNodeId.equals(this.getTreeModel().getRoot().getId())) {
            this.getParentNode(entityId, currentNodeId, (BillShowParameter)formShowParameter);
        }
        this.getView().showForm((FormShowParameter)formShowParameter);
    }

    @ExcludeFromJacocoGeneratedReport
    private void getParentNode(String entityId, String currentNodeId, BillShowParameter formShowParameter) {
        BasedataEntityType basedataEntityType = (BasedataEntityType)EntityMetadataCache.getDataEntityType((String)entityId);
        if (basedataEntityType != null) {
            HashMap<String, String> parentInfo = new HashMap<String, String>(16);
            for (IDataEntityProperty pro : basedataEntityType.getProperties()) {
                if (!(pro instanceof ParentBasedataProp)) continue;
                parentInfo.put("key", pro.getName());
                parentInfo.put("value", currentNodeId);
                formShowParameter.setCustomParam("tree_parent_id", parentInfo);
                break;
            }
        }
    }

    private void editGroupNode(String entityId, String currentNodeId) {
        BaseShowParameter formShowParameter = this.createFormShowParameter(entityId, currentNodeId, "group_bar_edit");
        formShowParameter.setPkId(this.getTreeModel().getCurrentNodeId());
        formShowParameter.setStatus(OperationStatus.EDIT);
        formShowParameter.setCustomParam("operate", (Object)"edit");
        HashMap<String, String> parentInfo = new HashMap<String, String>(16);
        parentInfo.put("value", currentNodeId);
        formShowParameter.setCustomParam("tree_curr_id", parentInfo);
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

    @ExcludeFromJacocoGeneratedReport
    public void beforeItemClick(BeforeItemClickEvent evt) {
        BillList billList = (BillList)this.getView().getControl(BILLLISTAP);
        ListSelectedRowCollection listSelectRows = billList.getSelectedRows();
        String itemKey = evt.getItemKey();
        this.getPageCache().put("itemKey", itemKey);
        boolean ifNeedCancel = false;
        switch (itemKey) {
            case "btn_batchgroup": {
                ifNeedCancel = this.isNoOneSelected(listSelectRows, ifNeedCancel);
                break;
            }
            case "btn_allocprem": {
                break;
            }
            case "bar_assignmember": {
                ifNeedCancel = this.validateAssignMember(listSelectRows);
                break;
            }
            case "exportroleperm": {
                ifNeedCancel = this.isNoOneSelected(listSelectRows, ifNeedCancel);
                break;
            }
            case "baritem_alteruserperm": {
                ifNeedCancel = this.isOnlyOneSelected(listSelectRows, ifNeedCancel);
                break;
            }
            case "": {
                ifNeedCancel = this.isNoOneSelected(listSelectRows, ifNeedCancel);
                break;
            }
        }
        this.handlePage(ifNeedCancel, evt, itemKey, listSelectRows);
    }

    private void handlePage(boolean ifNeedCancel, BeforeItemClickEvent evt, String itemKey, ListSelectedRowCollection listSelectRows) {
        String roleId;
        if (ifNeedCancel) {
            evt.setCancel(true);
        }
        if (!(HRStringUtils.equals((String)BARITEM_REFRESH, (String)itemKey) || CollectionUtils.isEmpty((Collection)listSelectRows) || listSelectRows.size() != 1 || this.checkRoleInfoExist(roleId = String.valueOf(listSelectRows.get(0).getPrimaryKeyValue())))) {
            evt.setCancel(true);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void userPermInit() {
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        listShowParameter.setFormId("bos_list");
        listShowParameter.setBillFormId("hrcs_perminitrecord");
        listShowParameter.setPageId(this.getView().getPageId() + RequestContext.get().getCurrUserId());
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    private void rolePermInit() {
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        listShowParameter.setFormId("bos_list");
        listShowParameter.setBillFormId("hrcs_perminitrecord");
        listShowParameter.setPageId(this.getView().getPageId() + RequestContext.get().getCurrUserId());
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean checkRoleInfoExist(String roleId) {
        try {
            DynamicObject hrRoleObj = PermDBServiceHelper.roleDBService.queryOne((Object)String.valueOf(roleId));
            if (Objects.isNull(hrRoleObj)) {
                ListView list = (ListView)this.getView();
                list.clearSelection();
                list.refresh();
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u89d2\u8272\u6570\u636e\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u5237\u65b0\u9875\u9762\u540e\u518d\u64cd\u4f5c\u3002", (String)"HRRoleListPlugin_26", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return false;
            }
        }
        catch (Exception ex) {
            LOGGER.error("query HRRoleListPlugin.billListHyperLinkClick exception:", (Throwable)ex);
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u89d2\u8272\u6570\u636e\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u5237\u65b0\u9875\u9762\u540e\u518d\u64cd\u4f5c\u3002", (String)"HRRoleListPlugin_26", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            ListView list = (ListView)this.getView();
            list.clearSelection();
            list.refresh();
            return false;
        }
        return true;
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean isOnlyOneSelected(ListSelectedRowCollection listSelectRows, boolean ifNeedCancel) {
        if (ifNeedCancel = this.isNoOneSelected(listSelectRows, ifNeedCancel)) {
            return true;
        }
        if (listSelectRows.size() > 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u4e2a\u6570\u636e\u3002", (String)"HRRoleListPlugin_16", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            ifNeedCancel = true;
        }
        return ifNeedCancel;
    }

    @ExcludeFromJacocoGeneratedReport
    private <T, R> boolean getPredicate(T param, R msg, Predicate<T> pred, Consumer<R> consumer) {
        if (pred.test(param)) {
            consumer.accept(msg);
        }
        return pred.test(param);
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean validateAssignMember(ListSelectedRowCollection listSelectRows) {
        return Conditional.getNew(() -> this.getPredicate(listSelectRows, ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u8981\u6267\u884c\u7684\u6570\u636e\u3002", (String)"HRRoleListPlugin_15", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), CollectionUtils::isEmpty, msg -> this.getView().showTipNotification(msg))).or(() -> this.getPredicate(listSelectRows, ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u4e2a\u6570\u636e\u3002", (String)"HRRoleListPlugin_16", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), param -> param.size() > 1, msg -> this.getView().showTipNotification(msg))).or(() -> this.getPredicate(listSelectRows, ResManager.loadKDString((String)"\u8bf7\u5148\u542f\u7528\u89d2\u8272", (String)"HRRoleListPlugin_35", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), this::checkRoleEnable, msg -> this.getView().showTipNotification(msg))).getValue();
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean checkRoleEnable(ListSelectedRowCollection listSelectRows) {
        HRBaseServiceHelper bosPermRoleHelper = new HRBaseServiceHelper("perm_role");
        ListSelectedRow row = listSelectRows.get(0);
        String roleId = String.valueOf(row.getPrimaryKeyValue());
        Object[] roleDy = bosPermRoleHelper.query("enable", new QFilter[]{new QFilter("id", "=", (Object)roleId)});
        return ArrayUtils.isNotEmpty((Object[])roleDy) && !HRStringUtils.equals((String)"1", (String)roleDy[0].getString("enable"));
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean isNoOneSelected(ListSelectedRowCollection listSelectRows, boolean ifNeedCancel) {
        if (listSelectRows.size() == 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u8981\u6267\u884c\u7684\u6570\u636e\u3002", (String)"HRRoleListPlugin_15", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            ifNeedCancel = true;
        }
        return ifNeedCancel;
    }

    @ExcludeFromJacocoGeneratedReport
    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        BillList source;
        ListSelectedRow currentSelectedRowInfo;
        ListShowParameter listShowParameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (!listShowParameter.isLookUp()) {
            args.setCancel(true);
        }
        if (!this.checkRoleInfoExist(String.valueOf((currentSelectedRowInfo = (source = (BillList)args.getHyperLinkClickEvent().getSource()).getCurrentSelectedRowInfo()).getPrimaryKeyValue()))) {
            return;
        }
        FormShowParameter showParameter = new FormShowParameter();
        HRBaseServiceHelper roleHelper = new HRBaseServiceHelper("perm_role");
        String roleId = (String)currentSelectedRowInfo.getPrimaryKeyValue();
        DynamicObject roleInfo = roleHelper.queryOne((Object)roleId);
        if (HRStringUtils.equals((String)roleInfo.getString("enable"), (String)ID_ROOTNODE)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u542f\u7528\u89d2\u8272", (String)"RoleFuntionAssignEdit_17", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        String pageId = this.getView().getPageId() + "_" + "hrcs_modifyrole" + "_" + roleId;
        showParameter.setPageId(pageId);
        showParameter.setFormId("hrcs_modifyrole");
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        HRBaseServiceHelper helper = new HRBaseServiceHelper("perm_role");
        DynamicObject role = helper.queryOne("name", (Object)roleId);
        showParameter.setCaption(role.getLocaleString("name").getLocaleValue());
        showParameter.setCustomParam("roleId", (Object)roleId);
        this.getView().showForm(showParameter);
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String itemKey = this.getPageCache().get("itemKey");
        if (HRStringUtils.isNotEmpty((String)itemKey)) {
            BillList billList = (BillList)this.getView().getControl(BILLLISTAP);
            ListSelectedRowCollection rows = billList.getSelectedRows();
            this.handleBeforeOperatetionEvent(itemKey, args, rows);
        } else if (HRStringUtils.isNotEmpty((String)BARITEM_INIT_ROLE_PERM)) {
            ListShowParameter listShowParameter = new ListShowParameter();
            listShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            listShowParameter.setFormId("bos_list");
            listShowParameter.setBillFormId("hrcs_perminitrecord");
            this.getView().showForm((FormShowParameter)listShowParameter);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        this.getPageCache().remove("itemKey");
        String operateKey = afterDoOperationEventArgs.getOperateKey();
    }

    @ExcludeFromJacocoGeneratedReport
    private void handleBeforeOperatetionEvent(String itemKey, BeforeDoOperationEventArgs args, ListSelectedRowCollection rows) {
        long currUserId = RequestContext.get().getCurrUserId();
        Map adminGroupInfos = HRRolePermHelper.queryUserAdminGroupInfos((long)currUserId);
        Set<Long> groupIds = adminGroupInfos.keySet();
        HashSet longNumbers = Sets.newHashSet(adminGroupInfos.values());
        BillList billList = (BillList)this.getView().getControl(BILLLISTAP);
        switch (itemKey) {
            case "bar_enable": {
                this.enableRoleInfo(groupIds, longNumbers);
                args.setCancel(true);
                billList.clearSelection();
                HRPermCacheMgr.clearAllCacheAsync();
                break;
            }
            case "bar_disable": {
                this.disableRoleInfo(groupIds, longNumbers);
                args.setCancel(true);
                billList.clearSelection();
                break;
            }
            case "bar_assignmember": {
                HashMap<String, Object> params = new HashMap<String, Object>(16);
                this.beforeAssignRole(params, rows);
                args.setCancel(true);
                break;
            }
            case "exportroleperm": {
                this.exportRolePerm(rows);
                args.setCancel(true);
                break;
            }
            case "inituserperm": {
                this.userPermInit();
                args.setCancel(true);
                break;
            }
            case "bar_exportrole": {
                this.exportRole(rows);
                args.setCancel(true);
                break;
            }
            case "bar_initrole": {
                this.rolePermInit();
                args.setCancel(true);
                break;
            }
            case "new": {
                this.openNewRolePage(args);
                break;
            }
            case "bar_copy": {
                this.copyRole(rows);
                args.setCancel(true);
                break;
            }
            case "delete": {
                this.deleteRoleInfo(rows, args, groupIds, longNumbers);
                break;
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean canOperateRole(Set<Long> groupIds, Set<String> longNumbers, Object roleId) {
        boolean noCreateAdminGroup;
        DynamicObject createAdminGroup = PermDBServiceHelper.roleDBService.queryOne("createadmingrp", roleId).getDynamicObject("createadmingrp");
        boolean bl = noCreateAdminGroup = null == createAdminGroup;
        if (noCreateAdminGroup) {
            return true;
        }
        String createAdminGroupLongNumber = createAdminGroup.getString(LONGNUMBER);
        long createAdminGroupId = noCreateAdminGroup ? 0L : createAdminGroup.getLong("id");
        boolean masterGroup = groupIds.contains(createAdminGroupId);
        if (masterGroup) {
            return true;
        }
        boolean topAdminGroupModify = HRRolePermHelper.isTopAdminGroupModify((String)createAdminGroupLongNumber, longNumbers);
        if (topAdminGroupModify) {
            return true;
        }
        HRBaseServiceHelper roleAssignHelper = new HRBaseServiceHelper("hrcs_roleassignscope");
        DynamicObject[] roleAssigns = roleAssignHelper.query("admingroup", new QFilter[]{new QFilter("roleid", "=", roleId).and("ismodifiable", "=", (Object)"1")});
        Set assignGroupIds = Arrays.stream(roleAssigns).map(it -> it.getLong("admingroup.id")).collect(Collectors.toSet());
        if (groupIds.size() == 0 || assignGroupIds.size() == 0) {
            return false;
        }
        return assignGroupIds.retainAll(groupIds);
    }

    public void itemClick(ItemClickEvent evt) {
        String operationKey;
        String itemkey = evt.getItemKey();
        switch (operationKey = itemkey.toLowerCase(Locale.ROOT)) {
            case "refresh": {
                ListView list = (ListView)this.getView();
                list.clearSelection();
                list.refresh();
                break;
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        if (HRStringUtils.equals((String)closedCallBackEvent.getActionId(), (String)ACTION_ID_EXPORT)) {
            this.getReturnExportUrl(closedCallBackEvent);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void getReturnExportUrl(ClosedCallBackEvent closedCallBackEvent) {
        Object returnData = closedCallBackEvent.getReturnData();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("export URL is :" + returnData + (Object)((Object)this));
        }
        Optional.ofNullable(returnData).ifPresent(data -> {
            Map result;
            if (returnData instanceof Map && (result = (Map)returnData).containsKey("taskinfo")) {
                String taskInfoStr = (String)result.get("taskinfo");
                TaskInfo taskInfo = (TaskInfo)SerializationUtils.fromJsonString((String)taskInfoStr, TaskInfo.class);
                this.handleTaskInfo(taskInfo);
            }
        });
    }

    @ExcludeFromJacocoGeneratedReport
    private void handleTaskInfo(TaskInfo taskInfo) {
        if (taskInfo.isTaskEnd()) {
            Map map = (Map)JSONObject.parseObject((String)taskInfo.getData(), Map.class);
            String exportUrl = (String)map.get(ACTION_ID_EXPORT);
            if (HRStringUtils.isNotEmpty((String)exportUrl)) {
                IClientViewProxy clientViewProxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
                clientViewProxy.addAction("download", (Object)exportUrl);
            }
        } else {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u6784\u5efa\u5bfc\u51faURL\u5931\u8d25\uff01", (String)"HRRoleListPlugin_36", (String)"hrmp-hbss-formplugin", (Object[])new Object[0]));
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void beforeAssignRole(Map<String, Object> params, ListSelectedRowCollection rows) {
        if (rows.size() > 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u6761\u6570\u636e\u8fdb\u884c\u64cd\u4f5c\u3002", (String)"HRRoleListPlugin_17", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        } else if (rows.size() == 0) {
            this.getView().showTipNotification(this.getNoSelectMsg());
        } else {
            MultiLangEnumBridge langEnumBridge = new MultiLangEnumBridge("\u5206\u914d\u89d2\u8272\u6210\u5458", "HRRoleListPlugin_39", "hrmp-hrcs-formplugin");
            RoleService.commonWriteLogNoOpKey((String)"assign role members", (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_rolelist", (String)HRCS_APPID);
            String roleId = (String)rows.getPrimaryKeyValues()[0];
            DynamicObject roleName = RoleMemberAssignServiceHelper.getRoleName((String)roleId);
            params.put("useroperation", "assignmember");
            params.put("roleId", roleId);
            params.put("roleName", roleName.getString("name"));
            this.showDynamicForm("hrcs_modifyrole", params, null, ShowType.MainNewTabPage);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void openNewRolePage(BeforeDoOperationEventArgs args) {
        FormModel formModel = new FormModel("hrcs_newrole", null, "1");
        HashMap params = Maps.newHashMapWithExpectedSize((int)1);
        Object currentNodeId = this.getTreeModel().getCurrentNodeId();
        params.put("groupId", currentNodeId);
        formModel.setCustomParam((Map)params);
        HRShowPageUtils.showPage((FormModel)formModel, (AbstractFormPlugin)this);
        RoleService.commonWriteLogBeforeDoOp((BeforeDoOperationEventArgs)args);
        args.setCancel(true);
    }

    @ExcludeFromJacocoGeneratedReport
    private void copyRole(ListSelectedRowCollection rows) {
        Object[] roleIds = rows.getPrimaryKeyValues();
        if (roleIds.length == 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u89d2\u8272\u3002", (String)"HRRoleListPlugin_56", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        if (roleIds.length > 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u4e2a\u89d2\u8272\u3002", (String)"HRRoleListPlugin_64", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        String roleId = (String)roleIds[0];
        if (!this.checkCanCopy(roleId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u60a8\u65e0\u6743\u590d\u5236\u5f53\u524d\u89d2\u8272\uff0c\u8bf7\u8054\u7cfb\u89d2\u8272\u6240\u5c5e\u7ba1\u7406\u5458\u5206\u7ec4\u7684\u7528\u6237\uff0c\u8bbe\u7f6e\u89d2\u8272\u7684\u516c\u5f00\u8303\u56f4\u3002", (String)"HRRoleListPlugin_57", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        HRBaseServiceHelper helper = new HRBaseServiceHelper("perm_role");
        DynamicObject item = helper.queryOne("enable", new QFilter[]{new QFilter("id", "=", (Object)roleId)});
        if (null != item && !item.getBoolean("enable")) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u7981\u7528\u72b6\u6001\u4e0d\u5141\u8bb8\u590d\u5236\u3002", (String)"HRRoleListPlugin_59", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        this.showNewRole(roleId);
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean checkCanCopy(String roleId) {
        long currUserId = RequestContext.get().getCurrUserId();
        Map adminGroupInfos = HRRolePermHelper.queryUserAdminGroupInfos((long)currUserId);
        Set<Long> groupIds = adminGroupInfos.keySet();
        HashSet longNumbers = Sets.newHashSet(adminGroupInfos.values());
        DynamicObject role = PermDBServiceHelper.roleDBService.queryOne("createadmingrp.longnumber", new QFilter[]{new QFilter("id", "=", (Object)roleId)});
        String longNumber = role.getString("createadmingrp.longnumber");
        boolean topAdminGroupModify = HRRolePermHelper.isTopAdminGroupModify((String)longNumber, (Set)longNumbers);
        DynamicObject createAdminGroup = role.getDynamicObject("createadmingrp");
        boolean noCreateAdminGroup = null == createAdminGroup;
        long createAdminGroupId = noCreateAdminGroup ? 0L : createAdminGroup.getLong("id");
        boolean masterGroup = groupIds.contains(createAdminGroupId);
        return noCreateAdminGroup || masterGroup || topAdminGroupModify || this.checkHasModifyPerm(roleId, groupIds, role);
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean checkHasModifyPerm(String roleId, Set<Long> groupIds, DynamicObject dataEntity) {
        DynamicObject createAdminGrp = dataEntity.getDynamicObject("createadmingrp");
        if (null == createAdminGrp) {
            return true;
        }
        HRBaseServiceHelper openServiceHelper = new HRBaseServiceHelper("hrcs_roleopenscope");
        boolean openExists = openServiceHelper.isExists(new QFilter[]{new QFilter("roleid.id", "=", (Object)roleId).and("admingroup", "in", groupIds)});
        if (openExists) {
            return true;
        }
        boolean privateExists = PermDBServiceHelper.roleDBService.isExists(new QFilter[]{new QFilter("id", "=", (Object)roleId).and("usescope", "=", (Object)ID_ROOTNODE).and("createadmingrp", "in", groupIds)});
        if (privateExists) {
            return true;
        }
        HRBaseServiceHelper assignHelper = new HRBaseServiceHelper("hrcs_roleassignscope");
        boolean assignExists = assignHelper.isExists(new QFilter[]{new QFilter("roleid.id", "=", (Object)roleId).and("admingroup", "in", groupIds).and("ismodifiable", "=", (Object)"1")});
        return assignExists;
    }

    @ExcludeFromJacocoGeneratedReport
    private void showNewRole(String roleId) {
        SecureRandom random = new SecureRandom();
        int randomInt = random.nextInt(100000);
        FormShowParameter showParameter = new FormShowParameter();
        String pageId = this.getView().getPageId() + randomInt + "_" + "hrcs_modifyrole" + "_" + roleId;
        showParameter.setPageId(pageId);
        showParameter.setFormId("hrcs_modifyrole");
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setCaption(ResManager.loadKDString((String)"\u590d\u5236\u89d2\u8272", (String)"HRRoleListPlugin_58", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        showParameter.setCustomParam("roleId", (Object)roleId);
        showParameter.setCustomParam("copy", (Object)"1");
        this.getView().showForm(showParameter);
    }

    @ExcludeFromJacocoGeneratedReport
    private void enableRoleInfo(Set<Long> groupIds, Set<String> longNumbers) {
        IListView iListView = (IListView)this.getView();
        ListSelectedRowCollection roleList = iListView.getSelectedRows();
        if (roleList.size() == 0) {
            this.getView().showTipNotification(this.getNoSelectMsg());
            return;
        }
        this.enableRole(groupIds, longNumbers, roleList);
    }

    @ExcludeFromJacocoGeneratedReport
    private void enableRole(Set<Long> groupIds, Set<String> longNumbers, ListSelectedRowCollection roleList) {
        MultiLangEnumBridge langEnumBridge;
        String opDesc;
        HashSet roleIds = new HashSet(16);
        StringBuilder builder = new StringBuilder();
        roleList.forEach(row -> {
            String roleId = (String)row.getPrimaryKeyValue();
            roleIds.add(roleId);
            builder.append(" '").append(roleId).append("',");
        });
        builder.delete(builder.length() - 1, builder.length());
        QFilter[] filters = new QFilter[]{new QFilter("id", "in", roleIds)};
        Object[] arrDObj = BusinessDataServiceHelper.load((String)"perm_role", (String)"enable", (QFilter[])filters);
        if (ArrayUtils.isEmpty((Object[])arrDObj)) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u6570\u636e\u4e0d\u5b58\u5728\u3002", (String)"HRRoleListPlugin_30", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        int fail = 0;
        StringBuilder sb = new StringBuilder();
        ArrayList appLogInfoList = Lists.newArrayList();
        HashSet<String> changeRoleIds = new HashSet<String>(16);
        for (Object role : arrDObj) {
            if (!this.canOperateRole(groupIds, longNumbers, role.get("id"))) {
                ++fail;
                opDesc = ResManager.loadKDString((String)"\u7f16\u53f7%s\uff1a\u5f53\u524d\u7ba1\u7406\u5458\u4e0d\u5141\u8bb8\u4fee\u6539\u8be5\u901a\u7528\u89d2\u8272\u3002", (String)"HRRoleListPlugin_62", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{role.getString("number")});
                langEnumBridge = new MultiLangEnumBridge("\u542f\u7528", "HRRoleListPlugin_51", "hrmp-hrcs-formplugin");
                RoleService.addLogNoOpKey((String)"enable", (MultiLangEnumBridge)langEnumBridge, (boolean)false, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc);
                String cannotOperatePreRole = ResManager.loadKDString((String)"%s\uff1a\u5f53\u524d\u7ba1\u7406\u5458\u4e0d\u5141\u8bb8\u4fee\u6539\u8be5\u901a\u7528\u89d2\u8272\u3002", (String)"HRRoleListPlugin_60", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{role.getString("number")});
                sb.append(cannotOperatePreRole).append(System.lineSeparator());
                continue;
            }
            if (role.getBoolean("enable")) {
                ++fail;
                opDesc = ResManager.loadKDString((String)"\u7f16\u53f7%s\uff1a\u6570\u636e\u5df2\u542f\u7528\u3002", (String)"HRRoleListPlugin_63", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{role.getString("number")});
                langEnumBridge = new MultiLangEnumBridge("\u542f\u7528", "HRRoleListPlugin_51", "hrmp-hrcs-formplugin");
                RoleService.addLogNoOpKey((String)"enable", (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc);
                String reasonPreRole = ResManager.loadKDString((String)"%s\uff1a\u6570\u636e\u5df2\u542f\u7528\u3002", (String)"HRRoleListPlugin_61", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{role.getString("number")});
                sb.append(reasonPreRole).append(System.lineSeparator());
                continue;
            }
            role.set("enable", (Object)"1");
            changeRoleIds.add(role.getString("id"));
        }
        this.doDisableRole((DynamicObject[])arrDObj, builder, sb, fail, true);
        for (Object role : arrDObj) {
            opDesc = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u53f7%1$s\uff0c%2$s", (String)"HRRoleListPlugin_41", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), role.getString("number"), ResManager.loadKDString((String)"\u542f\u7528\u64cd\u4f5c\u6267\u884c\u6210\u529f\u3002", (String)"HRRoleListPlugin_55", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            langEnumBridge = new MultiLangEnumBridge("\u542f\u7528", "HRRoleListPlugin_51", "hrmp-hrcs-formplugin");
            RoleService.addLogNoOpKey((String)"enable", (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc);
        }
        PermLogRoleStatusServiceHelper.addPermLog(changeRoleIds, (long)PermLogRoleStatusServiceHelper.LOG_TYPE_ROLE_ENABLE);
        ILogService service = (ILogService)ServiceFactory.getService(ILogService.class);
        service.addBatchLog((List)Lists.newArrayList((Iterable)appLogInfoList));
        ListView listView = (ListView)this.getView();
        listView.refresh();
    }

    @ExcludeFromJacocoGeneratedReport
    private void disableRoleInfo(Set<Long> groupIds, Set<String> longNumbers) {
        IListView iListView = (IListView)this.getView();
        ListSelectedRowCollection roleList = iListView.getSelectedRows();
        if (roleList.size() == 0) {
            this.getView().showTipNotification(this.getNoSelectMsg());
            return;
        }
        this.disableRole(groupIds, longNumbers, roleList);
    }

    @ExcludeFromJacocoGeneratedReport
    private void disableRole(Set<Long> groupIds, Set<String> longNumbers, ListSelectedRowCollection roleList) {
        MultiLangEnumBridge langEnumBridge;
        String opDesc;
        HashSet roleIds = new HashSet(16);
        StringBuilder builder = new StringBuilder();
        roleList.forEach(row -> {
            String roleId = (String)row.getPrimaryKeyValue();
            roleIds.add(roleId);
            builder.append(" '").append(roleId).append("',");
        });
        builder.delete(builder.length() - 1, builder.length());
        QFilter[] filters = new QFilter[]{new QFilter("id", "in", roleIds)};
        Object[] arrDObj = BusinessDataServiceHelper.load((String)"perm_role", (String)"enable", (QFilter[])filters);
        if (ArrayUtils.isEmpty((Object[])arrDObj)) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u6570\u636e\u4e0d\u5b58\u5728\u3002", (String)"HRRoleListPlugin_30", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        HashSet<String> changeRoleIds = new HashSet<String>(16);
        int fail = 0;
        StringBuilder sb = new StringBuilder();
        ArrayList appLogInfoList = Lists.newArrayList();
        for (Object role : arrDObj) {
            if (!this.canOperateRole(groupIds, longNumbers, role.get("id"))) {
                ++fail;
                opDesc = ResManager.loadKDString((String)"\u7f16\u53f7%s\uff1a\u5f53\u524d\u7ba1\u7406\u5458\u4e0d\u5141\u8bb8\u4fee\u6539\u8be5\u901a\u7528\u89d2\u8272\u3002", (String)"HRRoleListPlugin_62", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{role.getString("number")});
                langEnumBridge = new MultiLangEnumBridge("\u7981\u7528", "HRRoleListPlugin_40", "hrmp-hrcs-formplugin");
                RoleService.addLogNoOpKey((String)"disable", (MultiLangEnumBridge)langEnumBridge, (boolean)false, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc);
                String cannotOperatePreRole = ResManager.loadKDString((String)"%s\uff1a\u5f53\u524d\u7ba1\u7406\u5458\u4e0d\u5141\u8bb8\u4fee\u6539\u8be5\u901a\u7528\u89d2\u8272\u3002", (String)"HRRoleListPlugin_60", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{role.getString("number")});
                sb.append(cannotOperatePreRole).append(System.lineSeparator());
                continue;
            }
            if (!role.getBoolean("enable")) {
                ++fail;
                opDesc = ResManager.loadKDString((String)"\u7f16\u53f7%s\uff1a\u6570\u636e\u5df2\u7981\u7528\u3002", (String)"HRRoleListPlugin_68", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{role.getString("number")});
                langEnumBridge = new MultiLangEnumBridge("\u7981\u7528", "HRRoleListPlugin_40", "hrmp-hrcs-formplugin");
                RoleService.addLogNoOpKey((String)"disable", (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc);
                String reasonPreRole = ResManager.loadKDString((String)"%s\uff1a\u6570\u636e\u5df2\u7981\u7528\u3002", (String)"HRRoleListPlugin_67", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{role.getString("number")});
                sb.append(reasonPreRole).append(System.lineSeparator());
                continue;
            }
            role.set("enable", (Object)ID_ROOTNODE);
            changeRoleIds.add(role.getString("id"));
        }
        this.doDisableRole((DynamicObject[])arrDObj, builder, sb, fail, false);
        for (Object role : arrDObj) {
            opDesc = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u53f7%1$s\uff0c%2$s", (String)"HRRoleListPlugin_41", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), role.getString("number"), ResManager.loadKDString((String)"\u7981\u7528\u64cd\u4f5c\u6267\u884c\u6210\u529f", (String)"HRRoleListPlugin_42", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            langEnumBridge = new MultiLangEnumBridge("\u7981\u7528", "HRRoleListPlugin_40", "hrmp-hrcs-formplugin");
            RoleService.addLogNoOpKey((String)"disable", (MultiLangEnumBridge)langEnumBridge, (boolean)true, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc);
        }
        PermLogRoleStatusServiceHelper.addPermLog(changeRoleIds, (long)PermLogRoleStatusServiceHelper.LOG_TYPE_ROLE_DISABLE);
        ILogService service = (ILogService)ServiceFactory.getService(ILogService.class);
        service.addBatchLog((List)Lists.newArrayList((Iterable)appLogInfoList));
        ListView listView = (ListView)this.getView();
        listView.refresh();
    }

    @ExcludeFromJacocoGeneratedReport
    private void doDisableRole(DynamicObject[] arrDObj, StringBuilder builder, StringBuilder sb, int fail, boolean isEnable) {
        block18: {
            int success = arrDObj.length - fail;
            try (TXHandle txHandle = TX.required();){
                try {
                    SaveServiceHelper.save((IDataEntityType)arrDObj[0].getDataEntityType(), (Object[])arrDObj);
                    String sql = "Update T_Perm_BizRoleComRole set FEnable = '" + (isEnable ? 1 : 0) + "' where FRoleID in (" + builder + ")";
                    Object[] updateParam = new SqlParameter[]{};
                    DB.update((DBRoute)DBRoute.basedata, (String)sql, (Object[])updateParam);
                    if (fail == 0) {
                        if (isEnable) {
                            this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u542f\u7528\u6210\u529f", (String)"HRRoleListPlugin_52", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), Integer.valueOf(2000));
                        } else {
                            Set roleIds = Arrays.stream(builder.toString().replaceAll(" ", BARITEM_SHARE).replaceAll("'", BARITEM_SHARE).split(",")).collect(Collectors.toSet());
                            HRPermCacheMgr.clearAllCacheAndNotifyAsync(roleIds);
                            this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u7981\u7528\u6210\u529f", (String)"HRRoleListPlugin_20", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), Integer.valueOf(2000));
                        }
                        break block18;
                    }
                    if (fail == 1 && arrDObj.length == 1) {
                        this.getView().showTipNotification(sb.toString());
                        break block18;
                    }
                    String msg = isEnable ? String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5171%1$s\u6761\u5355\u636e\uff0c\u542f\u7528\u6210\u529f%2$s\u6761\uff0c\u5931\u8d25%3$s\u6761\u3002", (String)"HRRoleListPlugin_69", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), arrDObj.length, success, fail) : String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5171%1$s\u6761\u5355\u636e\uff0c\u7981\u7528\u6210\u529f%2$s\u6761\uff0c\u5931\u8d25%3$s\u6761\u3002", (String)"HRRoleListPlugin_70", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), arrDObj.length, success, fail);
                    FormShowParameter parameter = new FormShowParameter();
                    parameter.getOpenStyle().setShowType(ShowType.Modal);
                    parameter.setShowTitle(false);
                    parameter.setFormId("bos_operationresult");
                    parameter.setCustomParam("title", (Object)msg);
                    parameter.setCustomParam(ERROR_MSG, (Object)sb.toString());
                    this.getView().showForm(parameter);
                }
                catch (Exception e) {
                    txHandle.markRollback();
                }
            }
        }
        HRPermCacheMgr.clearAllCacheAsync();
    }

    @ExcludeFromJacocoGeneratedReport
    private void deleteRoleInfo(ListSelectedRowCollection rows, BeforeDoOperationEventArgs args, Set<Long> groupIds, Set<String> longNumbers) {
        if (rows.size() == 0) {
            this.getView().showTipNotification(this.getNoSelectMsg());
            return;
        }
        HashSet hrRoleSet = Sets.newHashSetWithExpectedSize((int)rows.size());
        for (ListSelectedRow row : rows) {
            hrRoleSet.add(row.getQueryEntityPrimayKeyValue().toString());
        }
        ArrayList roleIdList = Lists.newArrayListWithExpectedSize((int)rows.size());
        Map<String, Map<String, Object>> roleMap = this.getExistRole(rows, roleIdList);
        ArrayList tempList = Lists.newArrayListWithExpectedSize((int)rows.size());
        Set<String> bosRoleSet = roleMap.keySet();
        Sets.SetView diff = Sets.difference((Set)hrRoleSet, bosRoleSet);
        this.extractId(tempList, (Set<String>)diff);
        if (tempList.size() > 0) {
            this.deleteHrRoleById(tempList);
            LOGGER.info("dirty data is removed!");
            BillList billList = (BillList)this.getView().getControl(BILLLISTAP);
            billList.clearSelection();
            billList.refresh();
        }
        if (roleMap.size() == 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f\u3002", (String)"HRRoleListPlugin_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        boolean canDel = this.checkCanDel(roleMap, groupIds, longNumbers);
        if (!canDel) {
            if (HRStringUtils.equals((String)((String)new HRPageCache(this.getView()).get("cannotOperate", String.class)), (String)"1")) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u7ba1\u7406\u5458\u4e0d\u5141\u8bb8\u5220\u9664\u8be5\u901a\u7528\u89d2\u8272\u3002", (String)"HRRoleListPlugin_54", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            } else {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u5220\u9664\u9884\u7f6e\u89d2\u8272\u3001\u542f\u7528\u72b6\u6001\u89d2\u8272\u3001\u88ab\u52a8\u6001\u6388\u6743\u65b9\u6848\u5f15\u7528\u6216\u8005\u6210\u5458\u4e0d\u4e3a\u7a7a\u7684\u89d2\u8272\u3002", (String)"HRRoleListPlugin_22", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            }
            args.setCancel(true);
            return;
        }
        this.confirmDeleteRoleInfo();
        args.setCancel(true);
    }

    @ExcludeFromJacocoGeneratedReport
    private void extractId(List<String> tempList, Set<String> diff) {
        tempList.addAll(diff);
    }

    private Map<String, Map<String, Object>> getExistRole(ListSelectedRowCollection rows, List<String> roleIdList) {
        for (ListSelectedRow listSelectedRow : rows) {
            roleIdList.add((String)listSelectedRow.getQueryEntityPrimayKeyValue());
        }
        return RoleServiceHelper.getRoleMembers(roleIdList);
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean checkCanDel(Map<String, Map<String, Object>> roleMap, Set<Long> groupIds, Set<String> longNumbers) {
        ArrayList roleIdList = Lists.newArrayListWithExpectedSize((int)roleMap.size());
        ArrayList errorMsgs = Lists.newArrayListWithExpectedSize((int)16);
        errorMsgs.add(ResManager.loadKDString((String)"<\u9519\u8bef\u4fe1\u606f\uff1a>", (String)"HRRoleListPlugin_32", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        int errorCount = 0;
        ArrayList appLogInfoList = Lists.newArrayList();
        Set<String> refrencedRoles = this.getRefrencedRoles(roleMap.keySet());
        HRPageCache cache = new HRPageCache(this.getView());
        for (Map.Entry<String, Map<String, Object>> role : roleMap.entrySet()) {
            MultiLangEnumBridge langEnumBridge;
            String opDesc;
            String number;
            Map<String, Object> tempMap = role.getValue();
            String enable = tempMap.get("enable").toString();
            Boolean isSysPreSet = (Boolean)tempMap.get("issyspreset");
            int memberCount = (Integer)tempMap.get("memberCount");
            String roleId = role.getKey();
            if (refrencedRoles.contains(roleId)) {
                number = tempMap.get("number").toString();
                errorMsgs.add(ResManager.loadKDString((String)"%s\uff1a\u5220\u9664\u5931\u8d25\uff0c\u56e0\u4e3a\u201c\u52a8\u6001\u6388\u6743\u65b9\u6848\u201d\u7684\u201c\u89d2\u8272\u201d\u5b57\u6bb5\u5f15\u7528\u4e86\u6b64\u6570\u636e\u3002", (String)"HRRoleListPlugin_65", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{number}));
                opDesc = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5220\u9664\u5931\u8d25\uff0c\u7f16\u53f7%s\u7684\u6570\u636e\u88ab\u201c\u52a8\u6001\u6388\u6743\u65b9\u6848\u201d\u7684\u201c\u89d2\u8272\u201d\u5b57\u6bb5\u5f15\u7528\u3002", (String)"HRRoleListPlugin_66", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), number);
                langEnumBridge = new MultiLangEnumBridge("\u5220\u9664", "HRRoleListPlugin_10", "hrmp-hrcs-formplugin");
                RoleService.addLogNoOpKey((String)BARITEM_DELETE, (MultiLangEnumBridge)langEnumBridge, (boolean)false, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc);
            } else if (null != isSysPreSet && isSysPreSet.booleanValue()) {
                number = tempMap.get("number").toString();
                errorMsgs.add(ResManager.loadKDString((String)"%s\uff1a\u9884\u7f6e\u89d2\u8272\u4e0d\u5141\u8bb8\u5220\u9664\u3002", (String)"HRRoleListPlugin_46", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{number}));
                opDesc = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u53f7%s\uff0c\u9884\u7f6e\u89d2\u8272\u4e0d\u5141\u8bb8\u5220\u9664\u3002", (String)"HRRoleListPlugin_47", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), number);
                langEnumBridge = new MultiLangEnumBridge("\u5220\u9664", "HRRoleListPlugin_10", "hrmp-hrcs-formplugin");
                RoleService.addLogNoOpKey((String)BARITEM_DELETE, (MultiLangEnumBridge)langEnumBridge, (boolean)false, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc);
            } else {
                number = tempMap.get("number").toString();
                if (!this.canOperateRole(groupIds, longNumbers, roleId)) {
                    String cannotOperatePre = ResManager.loadKDString((String)"%s\uff1a\u5f53\u524d\u7ba1\u7406\u5458\u4e0d\u5141\u8bb8\u4fee\u6539\u8be5\u901a\u7528\u89d2\u8272\u3002", (String)"HRRoleListPlugin_53", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{number});
                    errorMsgs.add(cannotOperatePre);
                    String opDesc2 = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u53f7%s\uff0c\u5f53\u524d\u7ba1\u7406\u5458\u4e0d\u5141\u8bb8\u4fee\u6539\u8be5\u901a\u7528\u89d2\u8272\u3002", (String)"HRRoleListPlugin_49", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), number);
                    MultiLangEnumBridge langEnumBridge2 = new MultiLangEnumBridge("\u5220\u9664", "HRRoleListPlugin_10", "hrmp-hrcs-formplugin");
                    RoleService.addLogNoOpKey((String)BARITEM_DELETE, (MultiLangEnumBridge)langEnumBridge2, (boolean)false, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc2);
                    cache.put("cannotOperate", (Object)"1");
                    ++errorCount;
                    continue;
                }
                if (HRStringUtils.equals((String)enable, (String)ID_ROOTNODE) && memberCount == 0) {
                    roleIdList.add(roleId);
                    continue;
                }
                errorMsgs.add(ResManager.loadKDString((String)"%s\uff1a\u89d2\u8272\u672a\u7981\u7528\uff0c\u4e14\u89d2\u8272\u6210\u5458\u4e0d\u4e3a\u7a7a\u3002", (String)"HRRoleListPlugin_33", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{number}));
                opDesc = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u53f7%s\u7684\u89d2\u8272\u672a\u88ab\u7981\u7528\uff0c\u4e14\u89d2\u8272\u6210\u5458\u4e0d\u4e3a\u7a7a\u3002", (String)"HRRoleListPlugin_43", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), number);
                langEnumBridge = new MultiLangEnumBridge("\u5220\u9664", "HRRoleListPlugin_10", "hrmp-hrcs-formplugin");
                RoleService.addLogNoOpKey((String)BARITEM_DELETE, (MultiLangEnumBridge)langEnumBridge, (boolean)false, (String)"hrcs_rolelist", (String)HRCS_APPID, (List)appLogInfoList, (String)opDesc);
            }
            ++errorCount;
        }
        if (!CollectionUtils.isEmpty((Collection)appLogInfoList)) {
            ILogService service = (ILogService)ServiceFactory.getService(ILogService.class);
            service.addBatchLog((List)Lists.newArrayList((Iterable)appLogInfoList));
        }
        errorMsgs.add(ResManager.loadKDString((String)"\u7ed3\u675f<br/>", (String)"HRRoleListPlugin_34", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        if (errorCount == roleMap.size()) {
            return false;
        }
        cache.put("canDelRoleList", (Object)roleIdList);
        cache.put("errorCount", (Object)errorCount);
        cache.put(ERROR_MSG, (Object)new StringBuilder(String.join((CharSequence)BARITEM_SHARE, errorMsgs)));
        return true;
    }

    private Set<String> getRefrencedRoles(Set<String> roleIds) {
        DynamicObject[] schemeArr;
        HashSet refrencedRoles = Sets.newHashSetWithExpectedSize((int)16);
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_dynascheme");
        for (DynamicObject schemeDyna : schemeArr = helper.query("roleentry.role", new QFilter[]{new QFilter("roleentry.role.id", "in", roleIds)})) {
            DynamicObjectCollection entryColl = schemeDyna.getDynamicObjectCollection("roleentry");
            for (DynamicObject entryDyno : entryColl) {
                String roleId = entryDyno.getString("role.id");
                if (!roleIds.contains(roleId)) continue;
                refrencedRoles.add(roleId);
            }
        }
        return refrencedRoles;
    }

    @ExcludeFromJacocoGeneratedReport
    private void showDynamicForm(String formId, Map<String, Object> params, CloseCallBack closeCallBack, ShowType showType) {
        FormShowParameter fsp = new FormShowParameter();
        if (BARITEM_ASSIGNMEMBER.equals(formId)) {
            String pageId = this.getView().getPageId();
            String userId = (String)params.get("paramUserId");
            String newPageId = pageId + "|" + formId + "|" + userId;
            fsp.setPageId(newPageId);
            IFormView iformView = this.getView().getView(newPageId);
            if (iformView != null) {
                this.handleTab(newPageId);
                return;
            }
        }
        RoleServiceHelper.showForm((String)formId, params, (CloseCallBack)closeCallBack, (ShowType)showType, (FormShowParameter)fsp, (IFormView)this.getView());
    }

    @ExcludeFromJacocoGeneratedReport
    private void handleTab(String newPageId) {
        Tab tab = (Tab)this.getView().getParentView().getControl("_submaintab_");
        if (tab != null) {
            tab.activeTab(newPageId);
            this.getView().sendFormAction(this.getView().getParentView());
        } else {
            LOGGER.info("parentView's tab control is null");
            tab = (Tab)this.getView().getParentView().getParentView().getControl("_submaintab_");
            Optional.ofNullable(tab).ifPresent(tabLabel -> {
                tabLabel.activeTab(newPageId);
                this.getView().sendFormAction(this.getView().getParentView().getParentView());
            });
        }
    }

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        TreeView treeView = (TreeView)this.getView().getControl("treeview");
        treeView.addTreeNodeQueryListener((TreeNodeQueryListener)this);
        BillList f7List = (BillList)this.getView().getControl(BILLLISTAP);
        f7List.addListRowClickListener((ListRowClickListener)this);
    }

    @ExcludeFromJacocoGeneratedReport
    public void refreshNode(RefreshNodeEvent evt) {
        List<TreeNode> childNodes;
        String nodeId = evt.getNodeId().toString();
        TreeNode node = this.getTreeModel().getRoot().getTreeNode(nodeId, 15);
        if (this.getTreeModel().getRoot().getId().equals(nodeId)) {
            childNodes = this.getChildNodes(this.getEntityName(), this.getRootId());
        } else {
            childNodes = this.getChildNodes(this.getEntityName(), nodeId);
            if (childNodes.size() > 0) {
                node.setIsOpened(true);
            }
        }
        evt.setChildNodes(childNodes);
    }

    private List<Map<String, String>> getTreeViewByParent(String entityName, String parentId) {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        DynamicObjectCollection dyColl = this.getTreeViewCollection(entityName, parentId);
        if (CollectionUtils.isEmpty((Collection)dyColl)) {
            return list;
        }
        for (DynamicObject dy : dyColl) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", dy.getString("id"));
            map.put("name", dy.getString("name"));
            map.put(PARENT, dy.getString(PARENT));
            map.put("isleaf", dy.getBoolean("isleaf") ? "1" : ID_ROOTNODE);
            list.add(map);
        }
        return list;
    }

    @ExcludeFromJacocoGeneratedReport
    private List<TreeNode> getChildNodes(String entityName, String parentId) {
        List<Map<String, String>> treeView = this.getTreeViewByParent(entityName, parentId);
        List nodes = treeView.stream().map(this::genTreeNode).collect(Collectors.toList());
        ArrayList<TreeNode> list = new ArrayList<TreeNode>();
        Map<String, TreeNode> dctItems = nodes.stream().collect(Collectors.toMap(TreeNode::getId, node -> node, (oldValue, newValue) -> newValue));
        for (TreeNode node2 : nodes) {
            TreeNode parentNode = dctItems.get(node2.getParentid());
            if (Objects.nonNull(parentNode)) {
                if (Objects.isNull(parentNode.getChildren())) {
                    parentNode.addChildren(new ArrayList());
                }
                parentNode.getChildren().add(node2);
                continue;
            }
            list.add(node2);
        }
        return list;
    }

    private TreeNode genTreeNode(Map<String, String> map) {
        TreeNode node = new TreeNode();
        String parentId = map.get(PARENT);
        node.setParentid(parentId);
        String nodeId = map.get("id");
        if (HRStringUtils.isEmpty((String)nodeId)) {
            return node;
        }
        node.setId(nodeId);
        String nodeName = map.get("name");
        if (HRStringUtils.isEmpty((String)nodeName)) {
            return node;
        }
        node.setText(nodeName);
        String longNumber = map.get(LONGNUMBER);
        node.setData((Object)longNumber);
        String isLeaf = map.get("isleaf");
        if (ID_ROOTNODE.equals(isLeaf)) {
            ArrayList list = new ArrayList();
            node.setChildren(list);
        }
        return node;
    }

    @ExcludeFromJacocoGeneratedReport
    public void listRowClick(ListRowClickEvent evt) {
        ListShowParameter view = (ListShowParameter)this.getView().getFormShowParameter();
        if (!view.isLookUp()) {
            return;
        }
        F7SelectedList f7SelectedList = (F7SelectedList)this.getView().getControl("selectedlistap");
        if (f7SelectedList == null) {
            return;
        }
        BillList billlist = (BillList)evt.getSource();
        if (billlist == null) {
            return;
        }
        ListSelectedRowCollection listRow = billlist.getSelectedRows();
        int size = listRow.size();
        ArrayList<String> pks = new ArrayList<String>(size);
        for (ListSelectedRow row : listRow) {
            pks.add(row.getPrimaryKeyValue().toString());
        }
        List<ValueTextItem> valueTextItems = this.getRoleInfo(pks);
        f7SelectedList.addItems(valueTextItems);
    }

    private List<ValueTextItem> getRoleInfo(List<String> pks) {
        DynamicObject[] roles;
        ArrayList valueTextItems = Lists.newArrayListWithCapacity((int)pks.size());
        HRBaseServiceHelper helper = new HRBaseServiceHelper("perm_role");
        QFilter[] filters = new QFilter[]{new QFilter("id", "in", pks)};
        for (DynamicObject dynamicObject : roles = helper.query("id,name", filters)) {
            ValueTextItem item = new ValueTextItem(dynamicObject.getString("id"), dynamicObject.getString("name"));
            valueTextItems.add(item);
        }
        return valueTextItems;
    }

    @ExcludeFromJacocoGeneratedReport
    private String getNoSelectMsg() {
        return ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u8981\u6267\u884c\u7684\u6570\u636e\u3002", (String)"HRRoleListPlugin_15", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
    }

    @ExcludeFromJacocoGeneratedReport
    private void exportRolePerm(ListSelectedRowCollection rows) {
        Object[] roleIds = rows.getPrimaryKeyValues();
        if (roleIds.length == 0) {
            return;
        }
        if (PermHelper.getUserRelatesByRoleIds((Object[])roleIds).length == 0) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5f53\u524d\u9009\u4e2d\u7684\u6570\u636e\uff0c\u65e0\u7528\u6237\u6743\u9650\u8bb0\u5f55\u3002", (String)"PermSheetHelper_64", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        LOGGER.info("Got selected role ids: {}.", roleIds);
        this.showProgressForm(roleIds, ResManager.loadKDString((String)"\u7528\u6237\u6743\u9650", (String)"PermSheetHelper_85", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), TASK_EXPORT_ROLE_USER);
    }

    @ExcludeFromJacocoGeneratedReport
    private void exportRole(ListSelectedRowCollection rows) {
        Object[] roleIds = rows.getPrimaryKeyValues();
        LOGGER.info("Got selected role ids: {}.", roleIds);
        this.showProgressForm(roleIds, ResManager.loadKDString((String)"\u89d2\u8272\u6743\u9650", (String)"PermSheetHelper_96", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), TASK_EXPORT_ROLE);
    }

    @ExcludeFromJacocoGeneratedReport
    private void showProgressForm(Object[] roleIds, String entityName, String taskClassName) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_exportperm");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.getCustomParams().put("entityname", entityName);
        showParameter.getCustomParams().put("taskClassName", taskClassName);
        showParameter.getCustomParams().put("roleIds", roleIds);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, ACTION_ID_EXPORT));
        this.getView().showForm(showParameter);
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new HRRoleProvider());
    }
}
