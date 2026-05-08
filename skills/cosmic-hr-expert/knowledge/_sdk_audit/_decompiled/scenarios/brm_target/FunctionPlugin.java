/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.IPageCache
 *  kd.bos.form.control.Html
 *  kd.bos.form.control.Search
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.control.events.SearchEnterListener
 *  kd.bos.form.control.events.TreeNodeClickListener
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.util.CollectionUtils
 *  kd.hr.brm.common.constants.FunctionConstants
 *  kd.hr.hbp.business.function.helper.HRFunctionHelper
 *  kd.hr.hbp.business.service.formula.entity.item.FunctionItem
 *  kd.hr.hbp.common.cache.HRPageCache
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hbp.formplugin.web.util.TreeViewSearchTool
 */
package kd.hr.brm.formplugin.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.IPageCache;
import kd.bos.form.control.Html;
import kd.bos.form.control.Search;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.control.events.SearchEnterListener;
import kd.bos.form.control.events.TreeNodeClickListener;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.util.CollectionUtils;
import kd.hr.brm.common.constants.FunctionConstants;
import kd.hr.hbp.business.function.helper.HRFunctionHelper;
import kd.hr.hbp.business.service.formula.entity.item.FunctionItem;
import kd.hr.hbp.common.cache.HRPageCache;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hbp.formplugin.web.util.TreeViewSearchTool;

public final class FunctionPlugin
extends HRBaseDataCommonEdit
implements FunctionConstants,
SearchEnterListener {
    private static final Log logger = LogFactory.getLog(FunctionPlugin.class);
    private boolean changeFlag;

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        List<FunctionItem> funItemList = this.getFunctionList(false);
        this.constructFunctionTreeView(funItemList);
        this.getPageCache().put("changeFlag", "true");
    }

    private List<FunctionItem> getFunctionList(boolean forceReload) {
        List functionItems;
        DynamicObject dyObj = this.getView().getModel().getDataEntity().getDynamicObject("bizapp");
        if (dyObj == null) {
            return new ArrayList<FunctionItem>();
        }
        HRPageCache pageCache = new HRPageCache(this.getView().getPageCache());
        List list = functionItems = forceReload ? null : (List)pageCache.get("hbp_function_item_cache_key", List.class);
        if (functionItems == null || functionItems.isEmpty()) {
            functionItems = (List)HRFunctionHelper.getFuncItemList((String)dyObj.getString("number")).get();
            pageCache.put("hbp_function_item_cache_key", (Object)functionItems);
        } else {
            ArrayList functionItemList = Lists.newArrayListWithCapacity((int)functionItems.size());
            for (Object obj : functionItems) {
                if (!(obj instanceof Map)) continue;
                functionItemList.add(JSONObject.parseObject((String)SerializationUtils.toJsonString(obj), FunctionItem.class));
            }
            functionItems = functionItemList;
        }
        return functionItems;
    }

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        TreeView tv1 = (TreeView)this.getView().getControl("functiontreeview");
        tv1.addTreeNodeClickListener(this.getTreeNodeClickListener());
        Search search = (Search)this.getControl("searchfunction");
        search.addEnterListener((SearchEnterListener)this);
    }

    public void search(SearchEnterEvent evt) {
        String searchText = evt.getText();
        TreeView tv = (TreeView)this.getView().getControl("functiontreeview");
        TreeViewSearchTool.search((String)searchText, (TreeView)tv, (IPageCache)this.getPageCache(), (String)"functiontreeview");
    }

    public void propertyChanged(PropertyChangedArgs changedArgs) {
        String key = changedArgs.getProperty().getName();
        Object newValue = changedArgs.getChangeSet()[0].getNewValue();
        if (null == newValue) {
            return;
        }
        if ("bizapp".equals(key)) {
            List<FunctionItem> curFunItemList = this.getFunctionList(true);
            this.constructFunctionTreeView(curFunItemList);
            this.getView().updateView("functiontreeview");
        }
    }

    private void constructFunctionTreeView(List<FunctionItem> funItemList) {
        if (CollectionUtils.isEmpty(funItemList)) {
            return;
        }
        TreeView tv = (TreeView)this.getView().getControl("functiontreeview");
        tv.deleteAllNodes();
        String rootId = "0";
        TreeNode rootNode = new TreeNode(null, "0", ResManager.loadKDString((String)"\u5168\u90e8", (String)"FunctionPlugin_0", (String)"hrmp-brm-formplugin", (Object[])new Object[0]), true);
        rootNode.setIsOpened(true);
        for (FunctionItem functionItemGroup : funItemList) {
            TreeNode tn1 = new TreeNode("0", functionItemGroup.getId(), functionItemGroup.getName(), true);
            tn1.setIsOpened(true);
            if (CollectionUtils.isEmpty((Collection)functionItemGroup.getChildren())) continue;
            for (FunctionItem funItemLeaf : functionItemGroup.getChildren()) {
                TreeNode tn2 = new TreeNode(functionItemGroup.getId(), funItemLeaf.getId(), funItemLeaf.getName(), false);
                tn2.setLeaf(true);
                tn1.addChild(tn2);
            }
            rootNode.addChild(tn1);
        }
        tv.addNode(rootNode);
        this.getPageCache().put(tv.getKey(), SerializationUtils.toJsonString((Object)rootNode));
    }

    private TreeNodeClickListener getTreeNodeClickListener() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    private void showFunctionMsg(FunctionItem function) {
        Html html = (Html)this.getView().getControl("htmlap");
        html.setConent(function.getFuncDescription());
    }

    private Optional<FunctionItem> findFunctionByNodeId(String nodeId) {
        List<FunctionItem> functionItemList = this.getFunctionList(false);
        if (CollectionUtils.isEmpty(functionItemList)) {
            return Optional.empty();
        }
        for (FunctionItem functionItemGroup : functionItemList) {
            for (FunctionItem funItemLeaf : functionItemGroup.getChildren()) {
                if (!nodeId.equals(String.valueOf(funItemLeaf.getId()))) continue;
                return Optional.of(funItemLeaf);
            }
        }
        return Optional.empty();
    }

    protected List<String> getUnCheckField() {
        List uncheckFieldList = super.getUnCheckField();
        uncheckFieldList.add("funcdescription");
        uncheckFieldList.add("format");
        uncheckFieldList.add("param");
        uncheckFieldList.add("example");
        uncheckFieldList.add("targettypegroup");
        return uncheckFieldList;
    }

    static /* synthetic */ Optional access$000(FunctionPlugin x0, String x1) {
        return x0.findFunctionByNodeId(x1);
    }

    static /* synthetic */ void access$100(FunctionPlugin x0, FunctionItem x1) {
        x0.showFunctionMsg(x1);
    }

    static /* synthetic */ boolean access$202(FunctionPlugin x0, boolean x1) {
        x0.changeFlag = x1;
        return x0.changeFlag;
    }

    static /* synthetic */ boolean access$200(FunctionPlugin x0) {
        return x0.changeFlag;
    }
}
