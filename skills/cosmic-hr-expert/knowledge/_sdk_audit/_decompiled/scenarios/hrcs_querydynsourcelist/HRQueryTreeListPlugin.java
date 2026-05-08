/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.SqlParameter
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.db.DB
 *  kd.bos.db.DBRoute
 *  kd.bos.db.ResultSetHandler
 *  kd.bos.designer.query.QueryTreeListPlugin
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.list.column.AbstractColumnDesc
 *  kd.bos.entity.operate.Donothing
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.exception.BosErrorCode
 *  kd.bos.exception.KDException
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.IListView
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.dao.MetadataDao
 *  kd.bos.metadata.devportal.AppMetadata
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.api.AdminAppResult
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.QueryServiceHelper
 *  kd.bos.servicehelper.devportal.AppMetaServiceHelper
 *  kd.bos.servicehelper.devportal.BizAppServiceHelp
 *  kd.bos.servicehelper.devportal.BizCloudServiceHelp
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.bos.servicehelper.smc.ManageServiceHelper
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.service.multientity.EntityReleaseInfoService
 *  kd.hr.hrcs.formplugin.web.query.SortTreeNode
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hrcs.formplugin.web.query;

import com.google.common.collect.Maps;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.SqlParameter;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.db.DB;
import kd.bos.db.DBRoute;
import kd.bos.db.ResultSetHandler;
import kd.bos.designer.query.QueryTreeListPlugin;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.list.column.AbstractColumnDesc;
import kd.bos.entity.operate.Donothing;
import kd.bos.entity.tree.TreeNode;
import kd.bos.exception.BosErrorCode;
import kd.bos.exception.KDException;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.IListView;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.dao.MetadataDao;
import kd.bos.metadata.devportal.AppMetadata;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.api.AdminAppResult;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.devportal.AppMetaServiceHelper;
import kd.bos.servicehelper.devportal.BizAppServiceHelp;
import kd.bos.servicehelper.devportal.BizCloudServiceHelp;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.bos.servicehelper.smc.ManageServiceHelper;
import kd.bos.util.StringUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.service.multientity.EntityReleaseInfoService;
import kd.hr.hrcs.formplugin.web.query.SortTreeNode;

public final class HRQueryTreeListPlugin
extends QueryTreeListPlugin {
    private static final Log logger = LogFactory.getLog(HRQueryTreeListPlugin.class);
    private static final String HRCS_FORMPLUGIN = "hrmp-hrcs-formplugin";
    private static final String BTN_NEW = "btnnew";
    private static final String BTN_EDIT = "btnedit";
    private static final String BTN_DEL = "btndel";
    private Map<String, String> ksqlquerytypeMap;
    private Map<String, String> queryExtSourceMap;
    private Map<String, DynamicObject> metaMap;

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void packageData(PackageDataEvent e) {
        super.packageData(e);
        AbstractColumnDesc columnDesc = (AbstractColumnDesc)e.getSource();
        String fieldKey = columnDesc.getFieldKey();
        if (HRStringUtils.equals((String)fieldKey, (String)"queryscheme")) {
            String queryEntityNumber = e.getRowData().getString("number");
            String queryType = this.ksqlquerytypeMap.get(queryEntityNumber);
            if (org.apache.commons.lang3.StringUtils.isEmpty((CharSequence)queryType)) {
                String sourceQueryEntityNumber = this.queryExtSourceMap.get(queryEntityNumber);
                queryType = this.ksqlquerytypeMap.get(sourceQueryEntityNumber);
            }
            e.setFormatValue((Object)(org.apache.commons.lang3.StringUtils.isEmpty((CharSequence)queryType) ? "KSQL" : queryType));
        } else if (HRStringUtils.equals((String)fieldKey, (String)"isvinfo")) {
            DynamicObject object = this.metaMap.get((String)e.getRowData().getPkValue());
            if (object == null) {
                return;
            }
            e.setFormatValue((Object)object.getString("isv"));
        } else if (HRStringUtils.equals((String)fieldKey, (String)"isextended")) {
            DynamicObject object = this.metaMap.get((String)e.getRowData().getPkValue());
            if (object == null) {
                return;
            }
            String inheritpath = object.getString("inheritpath");
            e.setFormatValue((Object)(!org.apache.commons.lang3.StringUtils.isEmpty((CharSequence)inheritpath) ? 1 : 0));
        } else if (HRStringUtils.equals((String)fieldKey, (String)"extentsource")) {
            DynamicObject object = this.metaMap.get((String)e.getRowData().getPkValue());
            if (object == null) {
                return;
            }
            String inheritpath = object.getString("inheritpath");
            if (org.apache.commons.lang3.StringUtils.isNotBlank((CharSequence)inheritpath)) {
                try {
                    MainEntityType entityType = EntityMetadataCache.getDataEntityTypeById((String)inheritpath);
                    e.setFormatValue((Object)entityType.getName());
                }
                catch (Exception ex) {
                    logger.info("dirty data!", (Object)ex.getMessage());
                }
            }
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        if (evt.getOperationKey().equals("delquery")) {
            evt.setCancel(true);
            ConfirmCallBackListener confirmCallBacks = new ConfirmCallBackListener("deleteQueryConfig", (IFormPlugin)this);
            this.getView().showConfirm(ResManager.loadKDString((String)"\u786e\u5b9a\u5220\u9664\u5f53\u524d\u9009\u62e9\u7684\u67e5\u8be2\u914d\u7f6e\u4fe1\u606f\u5417\uff1f", (String)"HRQueryTreeListPlugin_3", (String)HRCS_FORMPLUGIN, (Object[])new Object[0]), MessageBoxOptions.OKCancel, confirmCallBacks);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        if (args.getSource() instanceof Donothing) {
            Donothing op = (Donothing)args.getSource();
            String key = op.getOperateKey();
            this.executeOp(key, args);
        }
    }

    private void executeOp(String key, BeforeDoOperationEventArgs args) {
        if ("addquery".equals(key)) {
            args.setCancel(true);
            Object currentNodeObj = this.getTreeModel().getCurrentNodeId();
            if (currentNodeObj != null) {
                String nodeId = currentNodeObj.toString();
                DynamicObjectCollection dynColl = this.getBizCloudDynamicObjectCollectionByAPPId(nodeId);
                if (!dynColl.isEmpty()) {
                    FormShowParameter showParameter = new FormShowParameter();
                    showParameter.setFormId("hrcs_querydynsource");
                    showParameter.getCustomParams().put("currentBizAppId", nodeId);
                    showParameter.setCustomParam("sources", this.getView().getFormShowParameter().getCustomParam("sources"));
                    showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "tblnew"));
                    showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                    this.getView().showForm(showParameter);
                } else {
                    this.getView().showMessage(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u5de6\u4fa7\u6811\u5f62\u83dc\u5355\u4e2d\u7684\u4e1a\u52a1\u4e91\u5e94\u7528\u8282\u70b9\u3002", (String)"HRQueryTreeListPlugin_1", (String)HRCS_FORMPLUGIN, (Object[])new Object[0]));
                }
            } else {
                this.getView().showMessage(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e1a\u52a1\u4e91\u4e0b\u7684\u5e94\u7528\u8282\u70b9\u3002", (String)"HRQueryTreeListPlugin_2", (String)HRCS_FORMPLUGIN, (Object[])new Object[0]));
            }
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent arg0) {
        if (arg0.getCallBackId().equals("deleteQueryConfig")) {
            if (MessageBoxResult.Yes.equals((Object)arg0.getResult())) {
                this.getView().invokeOperation("delquery");
                IListView iListView = (IListView)this.getView();
                iListView.refresh();
            }
        } else {
            super.confirmCallBack(arg0);
        }
    }

    private DynamicObjectCollection getBizCloudDynamicObjectCollectionByAPPId(String id) {
        QFilter qFilter = new QFilter("id", "=", (Object)id);
        QFilter[] filters = new QFilter[]{qFilter};
        String selectFields = "id";
        DynamicObjectCollection dynObjColl = QueryServiceHelper.query((String)"bos_devportal_bizapp", (String)selectFields, (QFilter[])filters);
        return dynObjColl;
    }

    public void billListHyperLinkClick(HyperLinkClickArgs evt) {
        IListView listView = (IListView)this.getView();
        if (!listView.getRootControl().getKey().endsWith("f7")) {
            evt.setCancel(true);
            String queryEntityId = (String)listView.getFocusRowPkId();
            this.openPage(queryEntityId);
        }
    }

    private void openPage(String queryEntityId) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_querydynsource");
        showParameter.setStatus(OperationStatus.EDIT);
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        String queryEntityNumber = MetadataDao.getNumberById((String)queryEntityId);
        showParameter.setCustomParam("queryEntityNumber", (Object)queryEntityNumber);
        showParameter.setCustomParam("queryEntityId", (Object)queryEntityId);
        Map<String, String> appMap = this.getBizAppMap(queryEntityId);
        showParameter.setCustomParam("currentBizAppId", (Object)appMap.get("appId"));
        showParameter.setCustomParam("currentUnitId", (Object)appMap.get("unitId"));
        this.getView().showForm(showParameter);
    }

    private Map<String, String> getBizAppMap(String queryEntityId) {
        String sql = "select a.FBIZAPPID,b.FBIZUNITID from t_meta_formdesign a left join t_meta_bizunitrelform b on a.FBIZAPPID = b.FBIZAPPID and a.fid = b.fformid where a.FID = ?";
        Object[] params = new SqlParameter[]{new SqlParameter(":FID", 12, (Object)queryEntityId)};
        ResultSetHandler callBackHanlder = rs -> {
            HashMap<String, String> bizUnitMap = new HashMap<String, String>();
            try {
                if (rs.next()) {
                    bizUnitMap.put("appId", rs.getString(1));
                    bizUnitMap.put("unitId", rs.getString(2));
                }
            }
            catch (SQLException e) {
                throw new KDException((Throwable)e, BosErrorCode.sQL, new Object[]{String.format(Locale.ROOT, "Error:%s", e.getMessage())});
            }
            return bizUnitMap;
        };
        return (Map)DB.query((DBRoute)DBRoute.meta, (String)sql, (Object[])params, (ResultSetHandler)callBackHanlder);
    }

    public void refreshNode(RefreshNodeEvent evt) {
        List<TreeNode> childNodes = this.getChildNodes(evt.getNodeId());
        evt.setChildNodes(childNodes);
    }

    public void initializeTree(EventObject evt) {
        this.intiTree();
    }

    private List<TreeNode> getChildNodes(Object parentId) {
        List<TreeNode> cloudNodes = null;
        if (this.getTreeModel().getRoot().getId().equals(parentId)) {
            cloudNodes = this.getCloudNodes();
            Map<String, ILocaleString> appNameMap = HRQueryTreeListPlugin.getAppNameMap();
            this.getChildNodes(cloudNodes, appNameMap);
        } else {
            TreeNode node = this.getTreeModel().getRoot().getTreeNode((String)parentId, 20);
            if (node != null) {
                return node.getChildren();
            }
        }
        return cloudNodes;
    }

    private void getChildNodes(List<TreeNode> cloudNodes, Map<String, ILocaleString> appNameMap) {
        Iterator<TreeNode> cloudIterator = cloudNodes.iterator();
        Set<String> disabledAppIds = this.getDisabledAppIds();
        AdminAppResult adminAppResult = PermissionServiceHelper.getAdminApps((Long)RequestContext.get().getCurrUserId(), (boolean)true);
        logger.debug("Appid is diabled : {}, needAdminAppRange : {}, rangeApp size : {}", new Object[]{disabledAppIds, adminAppResult.needAdminAppRange(), adminAppResult.getAppIds() == null ? "null" : Integer.valueOf(adminAppResult.getAppIds().size())});
        while (cloudIterator.hasNext()) {
            TreeNode cloudNode = cloudIterator.next();
            List<TreeNode> groupNodes = this.getAppNodes(cloudNode.getId(), disabledAppIds, adminAppResult, appNameMap);
            if (groupNodes.isEmpty()) {
                cloudIterator.remove();
                continue;
            }
            this.addChildNode(cloudNode, groupNodes);
        }
    }

    private TreeNode addChildNode(TreeNode pnode, List<TreeNode> childNodes) {
        for (int i = 0; i < childNodes.size(); ++i) {
            TreeNode cnode = childNodes.get(i);
            String parentId = cnode.getParentid();
            if (!pnode.getId().equals(parentId)) continue;
            pnode.addChild(this.addChildNode(cnode, childNodes));
        }
        return pnode;
    }

    private void intiTree() {
        TreeNode root = new TreeNode();
        root.setText(ResManager.loadKDString((String)"\u5168\u90e8", (String)"HRQueryTreeListPlugin_0", (String)HRCS_FORMPLUGIN, (Object[])new Object[0]));
        root.setParentid("");
        root.setId("-1");
        root.setIsOpened(true);
        List<TreeNode> cloudNodes = this.getCloudNodes();
        Map<String, ILocaleString> appNameMap = HRQueryTreeListPlugin.getAppNameMap();
        this.getPageCache().put("appNameMap", SerializationUtils.toJsonString(appNameMap));
        this.getChildNodes(cloudNodes, appNameMap);
        root.addChildren(cloudNodes);
        this.getPageCache().put("treenodecache", SerializationUtils.toJsonString((Object)root));
        this.getTreeModel().setRoot(root);
        this.getTreeModel().setCurrentNodeId((Object)root.getId());
        this.setBarItemEnable(false, true);
    }

    private void setBarItemEnable(boolean enable, boolean isSetAll) {
        if (isSetAll) {
            this.getView().setEnable(Boolean.valueOf(enable), new String[]{BTN_NEW, BTN_EDIT, BTN_DEL});
        } else {
            this.getView().setEnable(Boolean.valueOf(enable), new String[]{BTN_NEW});
            this.getView().setEnable(Boolean.valueOf(!enable), new String[]{BTN_EDIT, BTN_DEL});
        }
    }

    private List<TreeNode> getCloudNodes() {
        ArrayList<SortTreeNode> sortTreeNodeList = new ArrayList<SortTreeNode>();
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        String pageType = (String)formShowParameter.getCustomParam("pagetype");
        if ("extend".equals(pageType)) {
            String refAppId = (String)formShowParameter.getCustomParam("refappid");
            DynamicObject cloudObject = BizCloudServiceHelp.getBizCloudByAppID((String)refAppId);
            SortTreeNode node = new SortTreeNode("-1", cloudObject.getString("id"), cloudObject.getLocaleString("name").getLocaleValue(), 0);
            sortTreeNodeList.add(node);
        } else {
            DynamicObjectCollection dynamicObjects = BizCloudServiceHelp.getAllBizClouds();
            Set disabledCloudIds = ManageServiceHelper.getDisabledCloudIds();
            logger.debug("Cloud is diabled : {}", (Object)disabledCloudIds);
            HRBaseServiceHelper cloudHelper = new HRBaseServiceHelper("hbss_cloud");
            DynamicObject[] clouds = cloudHelper.queryOriginalArray("cloud,index", new QFilter[0]);
            Set cloudIdSet = Arrays.stream(clouds).map(el -> el.getString("cloud")).collect(Collectors.toSet());
            Map<String, Integer> cloudIndex = Arrays.stream(clouds).collect(Collectors.toMap(el -> el.getString("cloud"), dyo -> dyo.getInt("index"), (k1, k2) -> k1));
            for (DynamicObject dynamicObject : dynamicObjects) {
                String cloudId = dynamicObject.getString("id");
                if (disabledCloudIds.contains(cloudId) || !cloudIdSet.contains(cloudId)) continue;
                int index = cloudIndex.get(cloudId);
                SortTreeNode node = new SortTreeNode(index);
                node.setId(cloudId);
                node.setText(dynamicObject.getString("name"));
                node.setParentid("-1");
                sortTreeNodeList.add(node);
            }
        }
        sortTreeNodeList.sort(Comparator.comparingInt(SortTreeNode::getIndex));
        return new ArrayList<TreeNode>(sortTreeNodeList);
    }

    private Map<String, String> getQueryExtSourceMap(DynamicObjectCollection rows) {
        Set numbers = rows.stream().map(row -> row.getString("number")).filter(HRStringUtils::isNotEmpty).collect(Collectors.toSet());
        return EntityReleaseInfoService.queryQueryExtSourceMap(numbers);
    }

    private Map<String, String> getKsqlquerytype(DynamicObjectCollection rows) {
        ArrayList<String> numbers = new ArrayList<String>(rows.size());
        for (DynamicObject row : rows) {
            numbers.add(row.getString("number"));
        }
        DynamicObject[] entityReleaseInfoListByName = EntityReleaseInfoService.getEntityReleaseInfoListByName(numbers);
        HashMap<String, String> map = new HashMap<String, String>(Maps.newHashMapWithExpectedSize((int)rows.size()));
        for (DynamicObject row : entityReleaseInfoListByName) {
            map.put(row.getString("queryentityname"), row.getString("datasourcetype"));
        }
        return map;
    }

    private Map<String, DynamicObject> getMetaInfo(DynamicObjectCollection rows) {
        DynamicObject[] objs = BusinessDataServiceHelper.load((String)"bos_formmeta", (String)"fid,isv,inheritpath", (QFilter[])new QFilter[]{new QFilter("modeltype", "=", (Object)"QueryListModel")});
        HashMap<String, DynamicObject> map = new HashMap<String, DynamicObject>(Maps.newHashMapWithExpectedSize((int)rows.size()));
        for (DynamicObject row : objs) {
            map.put(row.getString("id"), row);
        }
        return map;
    }

    private List<TreeNode> getAppNodes(String cloudId, Set<String> disabledAppIds, AdminAppResult adminAppResult, Map<String, ILocaleString> appNameMap) {
        HRBaseServiceHelper cloudAppHelper = new HRBaseServiceHelper("hbss_cloud_app");
        DynamicObject[] apps = cloudAppHelper.queryOriginalArray("app,index", new QFilter[0]);
        Set appIdSet = Arrays.stream(apps).map(el -> el.getString("app")).collect(Collectors.toSet());
        Map<String, Integer> appIndexMap = Arrays.stream(apps).collect(Collectors.toMap(el -> el.getString("app"), dyo -> dyo.getInt("index"), (k1, k2) -> k1));
        ArrayList<SortTreeNode> sortTreeNodeList = new ArrayList<SortTreeNode>();
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        String pageType = (String)formShowParameter.getCustomParam("pagetype");
        if ("extend".equals(pageType)) {
            String refAppId = (String)formShowParameter.getCustomParam("refappid");
            AppMetadata refAppMeta = AppMetaServiceHelper.loadAppMetadataFromCacheById((String)refAppId, (boolean)false);
            String refAppName = refAppMeta.getName().getLocaleValue();
            SortTreeNode node = new SortTreeNode(cloudId, refAppId, refAppName, 999);
            sortTreeNodeList.add(node);
        } else {
            DynamicObjectCollection dynamicObjects = BizAppServiceHelp.getAllBizAppsByCloudID((String)cloudId);
            dynamicObjects = this.getMergeAppCollection(dynamicObjects, appNameMap);
            for (DynamicObject dynamicObject : dynamicObjects) {
                boolean adminNotCharge;
                String appId = dynamicObject.getString("id");
                String masterId = dynamicObject.getString("masterid");
                int index = 0;
                if (appIdSet.contains(appId)) {
                    index = appIndexMap.get(appId);
                } else {
                    if (!appIdSet.contains(masterId)) continue;
                    index = appIndexMap.get(masterId);
                }
                boolean bl = adminNotCharge = adminAppResult.needAdminAppRange() && (adminAppResult.getAppIds() == null || !adminAppResult.getAppIds().contains(appId));
                if (disabledAppIds.contains(appId) || adminNotCharge) continue;
                SortTreeNode node = new SortTreeNode(index);
                node.setId(appId);
                node.setText(dynamicObject.getString("name"));
                node.setParentid(cloudId);
                node.setData((Object)masterId);
                sortTreeNodeList.add(node);
            }
        }
        sortTreeNodeList.sort(Comparator.comparingInt(SortTreeNode::getIndex));
        return new ArrayList<TreeNode>(sortTreeNodeList);
    }

    private DynamicObjectCollection getMergeAppCollection(DynamicObjectCollection dynamicObjects, Map<String, ILocaleString> appNameMap) {
        String masterId;
        DynamicObjectCollection appCollection = new DynamicObjectCollection();
        HashSet<String> appExtends = new HashSet<String>();
        for (DynamicObject dynObj : dynamicObjects) {
            masterId = dynObj.getString("masterid");
            if (StringUtils.isEmpty((String)masterId)) continue;
            appExtends.add(masterId);
        }
        for (DynamicObject dynObj : dynamicObjects) {
            ILocaleString name;
            masterId = dynObj.getString("masterid");
            if (!StringUtils.isEmpty((String)masterId)) continue;
            String appId = dynObj.getString("id");
            String number = dynObj.getString("number");
            if (appExtends.contains(appId) && appNameMap != null && !appNameMap.isEmpty() && (name = appNameMap.get(number)) != null) {
                dynObj.set("name", (Object)name);
            }
            appCollection.add((Object)dynObj);
        }
        return appCollection;
    }

    private Set<String> getDisabledAppIds() {
        Set disabledAppIds = ManageServiceHelper.getDisabledAppIds();
        Set<String> extendAppIds = this.getExtendAppIds(disabledAppIds);
        disabledAppIds.addAll(extendAppIds);
        return disabledAppIds;
    }

    private Set<String> getExtendAppIds(Set<String> bizAppId) {
        String selectFields = "id";
        QFilter[] filters = new QFilter[]{new QFilter("masterid", "in", bizAppId)};
        DynamicObjectCollection dynamicObjects = QueryServiceHelper.query((String)"bos_devportal_bizapp", (String)selectFields, (QFilter[])filters);
        HashSet<String> extendAppIds = new HashSet<String>();
        if (dynamicObjects != null && !dynamicObjects.isEmpty()) {
            for (DynamicObject dynamicObject : dynamicObjects) {
                String id = dynamicObject.getString("id");
                if (!org.apache.commons.lang3.StringUtils.isNotBlank((CharSequence)id)) continue;
                extendAppIds.add(id);
            }
        }
        return extendAppIds;
    }

    private static Map<String, ILocaleString> getAppNameMap() {
        String sql = "select a.fappid ,b.fname, b.flocaleid from t_meta_appruntime a join t_meta_appruntime_l b on a.fappid = b.fappid ";
        return (Map)DB.query((DBRoute)DBRoute.meta, (String)sql, rs -> {
            HashMap<String, ILocaleString> map = new HashMap<String, ILocaleString>(16);
            while (rs.next()) {
                String appId = rs.getString("fappid");
                String name = rs.getString("fname");
                if (!org.apache.commons.lang3.StringUtils.isNotBlank((CharSequence)name)) continue;
                String localeId = rs.getString("flocaleid");
                ILocaleString localeString = map.computeIfAbsent(appId, k -> new LocaleString());
                localeString.put((Object)localeId, (Object)name);
            }
            return map;
        });
    }

    static /* synthetic */ Map access$002(HRQueryTreeListPlugin x0, Map x1) {
        x0.ksqlquerytypeMap = x1;
        return x0.ksqlquerytypeMap;
    }

    static /* synthetic */ Map access$100(HRQueryTreeListPlugin x0, DynamicObjectCollection x1) {
        return x0.getKsqlquerytype(x1);
    }

    static /* synthetic */ Map access$202(HRQueryTreeListPlugin x0, Map x1) {
        x0.queryExtSourceMap = x1;
        return x0.queryExtSourceMap;
    }

    static /* synthetic */ Map access$300(HRQueryTreeListPlugin x0, DynamicObjectCollection x1) {
        return x0.getQueryExtSourceMap(x1);
    }

    static /* synthetic */ Map access$402(HRQueryTreeListPlugin x0, Map x1) {
        x0.metaMap = x1;
        return x0.metaMap;
    }

    static /* synthetic */ Map access$500(HRQueryTreeListPlugin x0, DynamicObjectCollection x1) {
        return x0.getMetaInfo(x1);
    }
}
