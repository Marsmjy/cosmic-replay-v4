/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.OpenStyle
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.Search
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.TabSelectEvent
 *  kd.bos.form.control.events.TabSelectListener
 *  kd.bos.form.control.events.TreeNodeClickListener
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.FieldEdit
 *  kd.bos.form.field.events.AfterF7SelectEvent
 *  kd.bos.form.field.events.AfterF7SelectListener
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.list.BillList
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin
 *  kd.hr.hrcs.bussiness.service.esign.constant.ESignCOSealMgrPluginPage
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignCOAuthServiceHelper
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper
 *  kd.hr.hrcs.formplugin.common.HrcsFormpluginRes
 *  kd.hr.hrcs.formplugin.web.esign.control.ESignSPMgrBDEdit
 *  org.apache.commons.collections4.MapUtils
 *  org.apache.commons.lang3.ObjectUtils
 */
package kd.hr.hrcs.formplugin.web.esign;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.OpenStyle;
import kd.bos.form.ShowType;
import kd.bos.form.container.Tab;
import kd.bos.form.control.Search;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.TabSelectEvent;
import kd.bos.form.control.events.TabSelectListener;
import kd.bos.form.control.events.TreeNodeClickListener;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.FieldEdit;
import kd.bos.form.field.events.AfterF7SelectEvent;
import kd.bos.form.field.events.AfterF7SelectListener;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.list.BillList;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin;
import kd.hr.hrcs.bussiness.service.esign.constant.ESignCOSealMgrPluginPage;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignCOAuthServiceHelper;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper;
import kd.hr.hrcs.formplugin.common.HrcsFormpluginRes;
import kd.hr.hrcs.formplugin.web.esign.control.ESignSPMgrBDEdit;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;

public final class ESignCOSealMgrPlugin
extends HRDynamicFormBasePlugin
implements ESignCOSealMgrPluginPage,
AfterF7SelectListener,
BeforeF7SelectListener,
TabSelectListener,
TreeNodeClickListener {
    private static final Log LOGGER = LogFactory.getLog(ESignCOSealMgrPlugin.class);

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        this.createDynamicBaseDataEdit().ifPresent(item -> {
            ArrayList<ESignSPMgrBDEdit> items = new ArrayList<ESignSPMgrBDEdit>(1);
            items.add((ESignSPMgrBDEdit)item);
            this.getView().createControlIndex(items);
        });
        TreeView treeView = (TreeView)this.getControl("treeviewap");
        treeView.addTreeNodeClickListener((TreeNodeClickListener)this);
        Tab tab = (Tab)this.getView().getControl("tabap");
        tab.addTabSelectListener((TabSelectListener)this);
        Search search = (Search)this.getControl("searchap");
        search.addEnterListener(see -> {
            String searchText = see.getText();
            List treeRoots = SerializationUtils.fromJsonStringToList((String)this.getPageCache().get("treeRoots"), TreeNode.class);
            for (TreeNode treeNode : treeRoots) {
                List treeNodeListByText = treeNode.getTreeNodeListByText(new LinkedList(), searchText, node -> {}, 16);
                if (!CollectionUtils.isNotEmpty((Collection)treeNodeListByText)) continue;
                TreeNode showTreeNode = (TreeNode)treeNodeListByText.get(0);
                treeView.showNode(showTreeNode.getParentid());
                treeView.focusNode(showTreeNode);
                String currentTab = tab.getCurrentTab();
                this.showTabForm(currentTab);
                return;
            }
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u672a\u627e\u5230\u76f8\u5173\u67e5\u8be2\u7ed3\u679c\u3002", (String)HrcsFormpluginRes.ESignCOSealMgrPlugin_1.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        });
    }

    public void propertyChanged(PropertyChangedArgs evt) {
        super.propertyChanged(evt);
        String name = evt.getProperty().getName();
        if ("esignsp".equals(name)) {
            ChangeData[] dataArray = evt.getChangeSet();
            if (dataArray == null || dataArray.length < 1) {
                return;
            }
            ChangeData data = dataArray[0];
            DynamicObject newValue = (DynamicObject)data.getNewValue();
            if (ObjectUtils.isEmpty((Object)newValue)) {
                return;
            }
        }
    }

    public void beforeBindData(EventObject evt) {
        super.beforeBindData(evt);
        DynamicObject curESignSPDy = (DynamicObject)this.getModel().getValue("esignsp");
        if (curESignSPDy == null) {
            this.getView().showMessage(ResManager.loadKDString((String)"\u5f53\u524d\u7535\u5b50\u7b7e\u4f01\u4e1a\u5370\u7ae0\u7ba1\u7406\u8fd8\u672a\u7ef4\u62a4\u7535\u5b50\u7b7e\u670d\u52a1\u5546\u4fe1\u606f\uff0c\u8bf7\u5148\u5230\u83dc\u5355\u201cHR\u901a\u7528\u670d\u52a1>HR\u7535\u5b50\u7b7e\u7ba1\u7406\u5e73\u53f0>\u7535\u5b50\u7b7e\u670d\u52a1\u5546\u201d\u5b8c\u6210\u7ef4\u62a4\u7535\u5b50\u7b7e\u670d\u52a1\u5546\u914d\u7f6e\u4fe1\u606f\u518d\u8fdb\u884c\u4f01\u4e1a\u5370\u7ae0\u76f8\u5173\u64cd\u4f5c\u3002", (String)HrcsFormpluginRes.ESignCOSealMgrPlugin_2.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        this.initTree();
        this.showTabForm("tabcoseal");
    }

    public void treeNodeClick(TreeNodeEvent e) {
        String parentNodeId = (String)e.getParentNodeId();
        String nodeId = (String)e.getNodeId();
        Tab tab = (Tab)this.getControl("tabap");
        String currentTab = tab.getCurrentTab();
        String corporateNumber = StringUtils.isNotBlank((CharSequence)parentNodeId) ? nodeId : "";
        this.showTabForm(currentTab);
    }

    private void initTree() {
        TreeView treeView = (TreeView)this.getControl("treeviewap");
        treeView.deleteAllNodes();
        Map<String, TreeNode> treeRoot = this.createNode();
        ArrayList<TreeNode> treeRoots = new ArrayList<TreeNode>(treeRoot.values());
        if (CollectionUtils.isEmpty(treeRoots)) {
            this.getPageCache().remove("treeRoots");
        } else {
            this.getPageCache().put("treeRoots", SerializationUtils.toJsonString(treeRoots));
            Iterator iterator = treeRoots.iterator();
            while (iterator.hasNext()) {
                TreeNode value;
                TreeNode rootNode = value = (TreeNode)iterator.next();
                rootNode.setIsOpened(true);
                this.rootNodeFilter(rootNode);
                treeView.updateNode(rootNode);
            }
            treeView.focusNode((TreeNode)treeRoots.get(0));
        }
    }

    private void rootNodeFilter(TreeNode rootNode) {
        List children = rootNode.getChildren();
        if (CollectionUtils.isEmpty((Collection)children)) {
            return;
        }
        for (TreeNode treeNode : children) {
        }
    }

    public Map<String, TreeNode> createNode() {
        LinkedHashMap<String, TreeNode> appMap = new LinkedHashMap<String, TreeNode>(8);
        TreeView treeView = (TreeView)this.getControl("treeviewap");
        DynamicObject curESignSPDy = (DynamicObject)this.getModel().getValue("esignsp");
        if (curESignSPDy == null) {
            return appMap;
        }
        long spId = curESignSPDy.getLong("id");
        DynamicObjectCollection entryAppCfgDys = HRCSESignSPMgrServiceHelper.getESignSPEnableAppInfos((Object)spId);
        if (entryAppCfgDys != null && entryAppCfgDys.size() > 0) {
            for (DynamicObject entryAppCfgDy : entryAppCfgDys) {
                DynamicObject bdESignAppCfgDyn = entryAppCfgDy.getDynamicObject("bdesignappcfg");
                String appCfgNumber = bdESignAppCfgDyn.getString("number");
                TreeNode rootNode = (TreeNode)appMap.get(appCfgNumber);
                if (rootNode == null) {
                    rootNode = new TreeNode("", "root_" + appCfgNumber, bdESignAppCfgDyn.getString("name"), true);
                    treeView.addNode(rootNode);
                    appMap.put(appCfgNumber, rootNode);
                }
                LOGGER.info("\u6839\u8282\u70b9\uff1a{}", (Object)SerializationUtils.toJsonString((Object)rootNode));
                DynamicObject corporateDyn = bdESignAppCfgDyn.getDynamicObject("corporate");
                TreeNode childNode = new TreeNode(appCfgNumber, corporateDyn.getString("number"), corporateDyn.getString("name"), false);
                rootNode.addChild(childNode);
                DynamicObjectCollection eSignCOAuthDyns = HRCSESignCOAuthServiceHelper.getCOAuthDyns((Long)spId, (String)appCfgNumber);
                if (ObjectUtils.isNotEmpty((Object)eSignCOAuthDyns)) {
                    for (DynamicObject eSignCOAuthDyn : eSignCOAuthDyns) {
                        String lawentityNumber = eSignCOAuthDyn.getString("lawentity.number");
                        String lawentityName = eSignCOAuthDyn.getString("lawentity.name");
                        if (StringUtils.isNotBlank((CharSequence)lawentityNumber) && StringUtils.isNotBlank((CharSequence)lawentityName)) {
                            TreeNode authChildNode = new TreeNode(appCfgNumber, lawentityNumber, lawentityName, false);
                            rootNode.addChild(authChildNode);
                            continue;
                        }
                        LOGGER.info("\u5f02\u5e38\u6388\u6743\u4f01\u4e1a\u6570\u636elawentityNumber\uff1a{} lawentityName\uff1a{}", (Object)lawentityNumber, (Object)lawentityName);
                    }
                }
                LOGGER.info("\u6240\u6709\u8282\u70b9\uff1a{}", (Object)SerializationUtils.toJsonString((Object)rootNode));
            }
        }
        return appMap;
    }

    public void tabSelected(TabSelectEvent tabSelectEvent) {
        String tabKey = tabSelectEvent.getTabKey();
        if ("tabcoseal".equals(tabKey)) {
            this.showTabForm("tabcoseal");
        } else if ("tabauthedseal".equals(tabKey)) {
            this.showTabForm("tabauthedseal");
        }
    }

    private void showTabForm(String controlKey) {
        String spNumber = this.getSpNumber();
        String corporateNumber = "";
        TreeView treeView = (TreeView)this.getControl("treeviewap");
        Map focusNode = treeView.getTreeState().getFocusNode();
        String parentId = MapUtils.getString((Map)focusNode, (Object)"parentid");
        if (StringUtils.isBlank((CharSequence)parentId)) {
            String appNumber = MapUtils.getString((Map)focusNode, (Object)"id");
            this.getView().getModel().setValue("curappnumber", (Object)appNumber);
            this.getView().getModel().setValue("curcorpnumber", (Object)"");
        } else {
            corporateNumber = MapUtils.getString((Map)focusNode, (Object)"id");
            String appNumber = parentId;
            this.getView().getModel().setValue("curappnumber", (Object)appNumber);
            this.getView().getModel().setValue("curcorpnumber", (Object)corporateNumber);
        }
        IPageCache pageCache = this.getView().getPageCache();
        String childPageId = pageCache.get(controlKey + "childpageid");
        if (StringUtils.isNotBlank((CharSequence)childPageId)) {
            this.refreshChildPage(childPageId);
            return;
        }
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.setBillFormId((String)tabPageMapping.get(controlKey));
        listShowParameter.setFormId("bos_list");
        listShowParameter.setLookUp(false);
        OpenStyle openStyle = listShowParameter.getOpenStyle();
        openStyle.setShowType(ShowType.InContainer);
        openStyle.setTargetKey(controlKey);
        listShowParameter.setCustomParam("noSelect", (Object)true);
        this.getView().showForm((FormShowParameter)listShowParameter);
        this.getPageCache().put(controlKey + "childpageid", listShowParameter.getPageId());
    }

    private void refreshChildPage(String childPageId) {
        IFormView childView = this.getView().getView(childPageId);
        Map customParams = childView.getFormShowParameter().getCustomParams();
        customParams.put("noSelect", true);
        BillList billlist = (BillList)childView.getControl("billlistap");
        billlist.refresh();
        this.getView().sendFormAction(childView);
    }

    private String getSpNumber() {
        DynamicObject eSignSPDyn = (DynamicObject)this.getModel().getValue("esignsp");
        return HRObjectUtils.isEmpty((Object)eSignSPDyn) ? "" : eSignSPDyn.getString("number");
    }

    public void afterCreateNewData(EventObject e) {
        DynamicObject eSignSPDyn = HRCSESignSPMgrServiceHelper.getOnlineESignSP();
        if (eSignSPDyn != null) {
            String suffix = ResManager.loadKDString((String)"%s(\u542f\u7528\u4e2d)", (String)HrcsFormpluginRes.ESignCOSealMgrPlugin_3.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            eSignSPDyn.set("name", (Object)String.format(suffix, eSignSPDyn.getString("name")));
            this.getModel().setValue("esignsp", (Object)eSignSPDyn);
        }
    }

    public void afterF7Select(AfterF7SelectEvent evt) {
        FieldEdit fieldEdit = (FieldEdit)evt.getSource();
        if (HRStringUtils.equals((String)"esignsp", (String)fieldEdit.getFieldKey())) {
            Tab tab = (Tab)this.getControl("tabap");
            String currentTab = tab.getCurrentTab();
            this.initTree();
            this.showTabForm(currentTab);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
    }

    private Optional<ESignSPMgrBDEdit> createDynamicBaseDataEdit() {
        BasedataEdit eSignSPMgrBDControl;
        if (ObjectUtils.isNotEmpty((Object)this.getView()) && ObjectUtils.isNotEmpty((Object)(eSignSPMgrBDControl = (BasedataEdit)this.getView().getControl("esignsp"))) && !(eSignSPMgrBDControl instanceof ESignSPMgrBDEdit)) {
            ESignSPMgrBDEdit eSignSPMgrBDEdit = new ESignSPMgrBDEdit();
            eSignSPMgrBDEdit.setView(eSignSPMgrBDControl.getView());
            eSignSPMgrBDEdit.setModel(eSignSPMgrBDControl.getModel());
            eSignSPMgrBDEdit.setDisplayProp(eSignSPMgrBDControl.getDisplayProp());
            eSignSPMgrBDEdit.setEditSearchProp(eSignSPMgrBDControl.getEditSearchProp());
            eSignSPMgrBDEdit.setLayoutId(eSignSPMgrBDControl.getLayoutId());
            eSignSPMgrBDEdit.setKey(eSignSPMgrBDControl.getKey());
            eSignSPMgrBDEdit.setLock(eSignSPMgrBDControl.getLock());
            eSignSPMgrBDEdit.addAfterF7SelectListener((AfterF7SelectListener)this);
            eSignSPMgrBDEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
            return Optional.of(eSignSPMgrBDEdit);
        }
        return Optional.empty();
    }
}
