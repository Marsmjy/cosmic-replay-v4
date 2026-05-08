/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.datamodel.events.BeforePackageDataEvent
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.TreeNodeClickListener
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.form.control.events.TreeNodeQueryListener
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.plugin.StandardTreeListPlugin
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbss.bussiness.config.HRBdWhiteListServiceHelper
 *  kd.hr.hbss.bussiness.config.HRConfigServiceHelper
 *  kd.hr.hbss.bussiness.config.HRParamConfigTreeServiceHelper
 */
package kd.hr.hbss.formplugin.web.config;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.BeforePackageDataEvent;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.TreeNodeClickListener;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.control.events.TreeNodeQueryListener;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.ListShowParameter;
import kd.bos.list.plugin.StandardTreeListPlugin;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbss.bussiness.config.HRBdWhiteListServiceHelper;
import kd.hr.hbss.bussiness.config.HRConfigServiceHelper;
import kd.hr.hbss.bussiness.config.HRParamConfigTreeServiceHelper;

public final class HRConfigTreeListPlugin
extends StandardTreeListPlugin
implements TreeNodeQueryListener,
TreeNodeClickListener {
    private HRParamConfigTreeServiceHelper treeHelper;

    public void beforePackageData(BeforePackageDataEvent event) {
        super.beforePackageData(event);
        Map<String, Map<String, Object>> paramConfMap = event.getPageData().stream().filter(key -> HRStringUtils.isNotEmpty((String)key.getString("basedatafield.number"))).collect(Collectors.toMap(key -> key.getString("basedatafield.number"), val -> {
            HashMap<String, Object> map = new HashMap<String, Object>(16);
            map.put("enablestatus", val.get("enablestatus"));
            map.put("auditcheck", val.get("auditcheck"));
            map.put("changecheck", val.get("changecheck"));
            return map;
        }, (key1, key2) -> key1));
        DynamicObject[] whiteLists = HRBdWhiteListServiceHelper.queryAllWhiteList();
        Map<String, DynamicObject> whiteListMap = Arrays.stream(whiteLists).filter(dyo -> HRStringUtils.isNotEmpty((String)dyo.getString("basedata.number"))).collect(Collectors.toMap(dy -> dy.getString("basedata.number"), Function.identity(), (x, y) -> y));
        for (DynamicObject pageDatum : event.getPageData()) {
            Map<String, Object> baseDataConf;
            String entityNumber = pageDatum.getString("basedatafield.number");
            if (HRStringUtils.isEmpty((String)entityNumber) || (baseDataConf = this.getBaseDataConf(entityNumber, paramConfMap, whiteListMap)) == null) continue;
            baseDataConf.forEach((arg_0, arg_1) -> ((DynamicObject)pageDatum).set(arg_0, arg_1));
        }
    }

    private Map<String, Object> getBaseDataConf(String entityNumber, Map<String, Map<String, Object>> paramConfMap, Map<String, DynamicObject> whiteListMap) {
        DynamicObject whiteListDyo = whiteListMap.get(entityNumber);
        Map paramConfDetail = paramConfMap.get(entityNumber);
        if (paramConfDetail == null) {
            paramConfDetail = HRConfigServiceHelper.getParamByEntity((String)entityNumber);
        }
        if (whiteListDyo == null) {
            return paramConfDetail;
        }
        String refBaseDataNum = whiteListDyo.getString("refbasedata.number");
        if (HRStringUtils.isNotEmpty((String)refBaseDataNum)) {
            return this.getBaseDataConf(refBaseDataNum, paramConfMap, whiteListMap);
        }
        Map whiteListDefValMap = HRBdWhiteListServiceHelper.getDefVal((DynamicObject)whiteListDyo);
        if (paramConfDetail != null) {
            paramConfDetail.forEach(whiteListDefValMap::putIfAbsent);
        }
        return whiteListDefValMap;
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addItemClickListeners(new String[]{"tbsyn"});
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"lookwhitelist")) {
            this.openWhiteListPage();
            args.setCancel(true);
        }
    }

    private void openWhiteListPage() {
        ListShowParameter showParameter = new ListShowParameter();
        showParameter.setFormId("bos_treelist");
        showParameter.setBillFormId("hbss_paramwhitelist");
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setCustomParam("view_whitelist", (Object)"1");
        this.getView().showForm((FormShowParameter)showParameter);
    }

    public void initialize() {
        super.initialize();
        this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanel_treebtn"});
        TreeView treeView = this.getTreeView();
        this.treeHelper = new HRParamConfigTreeServiceHelper(treeView);
        treeView.addTreeNodeQueryListener((TreeNodeQueryListener)this);
    }

    public void afterCreateNewData(EventObject e) {
        this.treeHelper.reBuildWholeTree();
    }

    public void setFilter(SetFilterEvent evt) {
        super.setFilter(evt);
        String leftTreeQFilterJson = this.getPageCache().get("lefttree_qfilter_json");
        if (HRStringUtils.isNotEmpty((String)leftTreeQFilterJson)) {
            evt.getQFilters().add(QFilter.fromSerializedString((String)leftTreeQFilterJson));
        }
    }

    public void treeNodeClick(TreeNodeEvent treeNodeEvent) {
        String nodeId = (String)treeNodeEvent.getNodeId();
        String parentNodeId = (String)treeNodeEvent.getParentNodeId();
        TreeView leftTree = (TreeView)treeNodeEvent.getSource();
        String id = String.valueOf(nodeId);
        if (HRStringUtils.equals((String)"1010", (String)id)) {
            this.getPageCache().remove("lefttree_qfilter_json");
            return;
        }
        boolean isSearchMode = HRStringUtils.equals((String)this.getPageCache().get("lefttree_search_mode"), (String)"true");
        String[] idArr = id.split("_split_");
        if (idArr.length < 2) {
            return;
        }
        String nodeIdPrefix = idArr[0];
        TreeNode rootNode = new TreeNode();
        rootNode.setId("1010");
        String trueId = idArr[1];
        List formIdList = Lists.newArrayListWithCapacity((int)16);
        if (HRStringUtils.equals((String)"1", (String)nodeIdPrefix)) {
            formIdList = HRConfigServiceHelper.getEntityNumByCloudId((String)trueId);
            if (!isSearchMode) {
                leftTree.queryTreeNodeChildren(parentNodeId, nodeId);
            }
        } else {
            formIdList = HRConfigServiceHelper.getEntityNumByAppId((String)trueId);
            if (!isSearchMode) {
                leftTree.queryTreeNodeChildren(parentNodeId, nodeId);
            }
        }
        QFilter qFilter = new QFilter("basedatafield", "in", (Object)formIdList);
        this.getPageCache().put("lefttree_qfilter_json", qFilter.toSerializedString());
    }

    public void queryTreeNodeChildren(TreeNodeEvent event) {
        if (event.getNodeId() != null) {
            String nodeId = event.getNodeId().toString();
            this.treeHelper.handleExpandNodeClick(nodeId);
        }
    }

    private TreeView getTreeView() {
        return (TreeView)this.getView().getControl("treeview");
    }
}
