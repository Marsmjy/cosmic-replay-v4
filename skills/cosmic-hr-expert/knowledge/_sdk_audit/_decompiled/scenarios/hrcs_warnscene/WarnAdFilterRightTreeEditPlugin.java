/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.form.control.Html
 *  kd.bos.form.control.Search
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.control.events.SearchEnterListener
 *  kd.bos.form.control.events.TreeNodeClickListener
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.function.helper.HRFunctionHelper
 *  kd.hr.hbp.business.service.formula.entity.item.FunctionItem
 *  kd.hr.hbp.business.service.formula.entity.item.TreeNodeItem
 *  kd.hr.hbp.business.service.formula.utils.FormulaTreeNodeUtils
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.cache.HRPageCache
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.common.constants.earlywarn.WarnAdConditionConstants
 */
package kd.hr.hrcs.formplugin.web.earlywarn.ad;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.form.control.Html;
import kd.bos.form.control.Search;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.control.events.SearchEnterListener;
import kd.bos.form.control.events.TreeNodeClickListener;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.function.helper.HRFunctionHelper;
import kd.hr.hbp.business.service.formula.entity.item.FunctionItem;
import kd.hr.hbp.business.service.formula.entity.item.TreeNodeItem;
import kd.hr.hbp.business.service.formula.utils.FormulaTreeNodeUtils;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.cache.HRPageCache;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.common.constants.earlywarn.WarnAdConditionConstants;

public final class WarnAdFilterRightTreeEditPlugin
extends HRDataBaseEdit
implements TreeNodeClickListener,
SearchEnterListener,
WarnAdConditionConstants {
    private static final Log LOG = LogFactory.getLog(WarnAdFilterRightTreeEditPlugin.class);
    private static final String FUNCTREE = "functree";
    private static final String SEARCHFUNC = "searchfunc";
    private static final String DESCHTML = "deschtml";
    private static final String TEMPLATE_NAME = "FormulaEditDesc";
    private static final String CACHE_KEY_FUNC_NODE_LIST = "CACHE_KEY_FUNC_NODE_LIST";

    @ExcludeFromJacocoGeneratedReport
    public void registerListener(EventObject e) {
        try {
            super.registerListener(e);
            TreeView treeView = (TreeView)this.getView().getControl(FUNCTREE);
            treeView.addTreeNodeClickListener((TreeNodeClickListener)this);
            Search search = (Search)this.getView().getControl(SEARCHFUNC);
            search.addEnterListener((SearchEnterListener)this);
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeBindData(EventObject e) {
        try {
            super.beforeBindData(e);
            String bizAppNumber = this.getView().getFormShowParameter().getFormConfig().getBizAppNumber();
            HRFunctionHelper.getFuncTreeItemList((String)bizAppNumber).ifPresent(funcList -> {
                if (!CollectionUtils.isEmpty((Collection)funcList)) {
                    this.updateCache((List<TreeNodeItem>)funcList);
                    funcList.add(FormulaTreeNodeUtils.getFunctionRootItem());
                    FormulaTreeNodeUtils.loadTreeNodeItemList((List)funcList).ifPresent(treeNodes -> {
                        TreeView treeView = (TreeView)this.getView().getControl(FUNCTREE);
                        treeView.deleteNode("FC");
                        treeView.addNodes(treeNodes);
                    });
                }
            });
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void treeNodeClick(TreeNodeEvent evt) {
        try {
            String parentNodeId = (String)evt.getParentNodeId();
            if (HRStringUtils.isEmpty((String)parentNodeId)) {
                return;
            }
            Html html = (Html)this.getView().getControl(DESCHTML);
            String desc = "";
            FunctionItem funcData = this.findFuncById(String.valueOf(evt.getNodeId()));
            if (Objects.nonNull(funcData) && !funcData.isHasChild()) {
                desc = funcData.getFuncDescription();
            }
            html.setConent(desc);
            this.getView().setVisible(Boolean.TRUE, new String[]{DESCHTML});
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void treeNodeDoubleClick(TreeNodeEvent evt) {
        try {
            String parentNodeId = (String)evt.getParentNodeId();
            if (HRStringUtils.isEmpty((String)parentNodeId)) {
                return;
            }
            String value = "";
            FunctionItem funcData = this.findFuncById(String.valueOf(evt.getNodeId()));
            if (Objects.nonNull(funcData)) {
                if (funcData.isHasChild()) {
                    return;
                }
                value = funcData.getFuncFullName();
            }
            CustomControl ruleEditor = (CustomControl)this.getView().getControl("conditioneditor");
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("method", "insertValue");
            data.put("value", value);
            data.put("date", String.valueOf(new Date().getTime()));
            ruleEditor.setData(data);
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void search(SearchEnterEvent arg) {
        try {
            String searchText = arg.getText();
            List<FunctionItem> functionNodeItem = this.getFunctionItemCache();
            ArrayList treeNodeItems = Lists.newArrayListWithExpectedSize((int)functionNodeItem.size());
            treeNodeItems.addAll(functionNodeItem);
            boolean isFunctionFind = this.searchByCache(treeNodeItems, searchText, FormulaTreeNodeUtils.getFunctionRootItem());
            if (!isFunctionFind) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u641c\u7d22\u5df2\u5b8c\u6210\uff0c\u672a\u627e\u5230\u5339\u914d\u9879\u3002", (String)"WarnSchemeAdFilterRightTreeEditPlugin_0", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
            }
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean searchByCache(List<TreeNodeItem> treeNodeItems, String searchText, TreeNodeItem rootNode) {
        ArrayList findNodeItems;
        if (HRStringUtils.isNotEmpty((String)searchText)) {
            findNodeItems = Lists.newArrayListWithExpectedSize((int)treeNodeItems.size());
            for (TreeNodeItem treeNodeItem : treeNodeItems) {
                if (!HRStringUtils.isNotEmpty((String)treeNodeItem.getName()) || !treeNodeItem.getName().contains(searchText)) continue;
                findNodeItems.add(treeNodeItem);
            }
            if (CollectionUtils.isEmpty((Collection)findNodeItems)) {
                this.renderTree(rootNode, findNodeItems);
                return false;
            }
            List parentNodeItem = FormulaTreeNodeUtils.getAllParentTreeNodeItem((List)findNodeItems, (List)treeNodeItems);
            List childNodeItem = FormulaTreeNodeUtils.getAllChildTreeNodeItem((List)findNodeItems, (List)treeNodeItems);
            findNodeItems.addAll(parentNodeItem);
            findNodeItems.addAll(childNodeItem);
        } else {
            findNodeItems = treeNodeItems;
        }
        findNodeItems.add(rootNode);
        this.renderTree(rootNode, findNodeItems);
        return true;
    }

    @ExcludeFromJacocoGeneratedReport
    private void renderTree(TreeNodeItem rootNode, List<TreeNodeItem> findNodeItems) {
        TreeView treeView = (TreeView)this.getView().getControl(FUNCTREE);
        treeView.deleteNode(rootNode.getId());
        treeView.expand(rootNode.getId());
        FormulaTreeNodeUtils.loadTreeNodeItemList(findNodeItems).ifPresent(arg_0 -> ((TreeView)treeView).addNodes(arg_0));
    }

    @ExcludeFromJacocoGeneratedReport
    private FunctionItem findFuncById(String id) {
        if (id == null) {
            return null;
        }
        List<FunctionItem> result = this.getFunctionItemCache();
        FunctionItem functionItem = null;
        for (FunctionItem item : result) {
            if (item == null || !id.equals(item.getId())) continue;
            functionItem = item;
            break;
        }
        return functionItem;
    }

    @ExcludeFromJacocoGeneratedReport
    private List<FunctionItem> getFunctionItemCache() {
        HRPageCache pageCache = new HRPageCache(this.getView());
        List list = (List)pageCache.get(CACHE_KEY_FUNC_NODE_LIST, List.class);
        if (list == null) {
            return Lists.newArrayListWithExpectedSize((int)0);
        }
        ArrayList treeNodeItems = Lists.newArrayListWithExpectedSize((int)list.size());
        for (Object obj : list) {
            treeNodeItems.add(JSONObject.parseObject((String)SerializationUtils.toJsonString(obj), FunctionItem.class));
        }
        return treeNodeItems;
    }

    @ExcludeFromJacocoGeneratedReport
    private void updateCache(List<TreeNodeItem> treeNodeItems) {
        HRPageCache pageCache = new HRPageCache(this.getPageCache());
        pageCache.put(CACHE_KEY_FUNC_NODE_LIST, treeNodeItems);
    }
}
