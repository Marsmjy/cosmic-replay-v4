/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.filter.FilterContainer
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.AfterSearchClickListener
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.SearchClickEvent
 *  kd.bos.form.control.events.TreeNodeClickListener
 *  kd.bos.form.control.events.TreeNodeEvent
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.BillList
 *  kd.bos.list.events.CreateListDataProviderListener
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.bussiness.cert.HRCertCommonHelper
 *  kd.hr.hbp.bussiness.cert.HRCertConstant
 *  kd.hr.hbp.bussiness.cert.HRCertPromptInfoTypeEnum
 *  kd.hr.hbp.bussiness.cert.model.HRCertPacket
 *  kd.hr.hbp.common.cache.HRPageCache
 *  kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin
 *  kd.hr.hbss.formplugin.web.cert.CertListDataProvider
 */
package kd.hr.hbss.formplugin.web.cert;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.tree.TreeNode;
import kd.bos.filter.FilterContainer;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.control.Control;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.AfterSearchClickListener;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.SearchClickEvent;
import kd.bos.form.control.events.TreeNodeClickListener;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.BillList;
import kd.bos.list.events.CreateListDataProviderListener;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.bussiness.cert.HRCertCommonHelper;
import kd.hr.hbp.bussiness.cert.HRCertConstant;
import kd.hr.hbp.bussiness.cert.HRCertPromptInfoTypeEnum;
import kd.hr.hbp.bussiness.cert.model.HRCertPacket;
import kd.hr.hbp.common.cache.HRPageCache;
import kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin;
import kd.hr.hbss.formplugin.web.cert.CertListDataProvider;

public final class CertGroupMemberPlugin
extends HRDynamicFormBasePlugin
implements TreeNodeClickListener,
CreateListDataProviderListener,
AfterSearchClickListener {
    private static final String KEY_CACHE_FILTER_SIZE = "FILTER_SIZE";
    private static final String KEY_CACHE_FILTER_PREFIX = "CERT_FILTER_";
    private static final String KEY_CACHE_GROUP_ID = "groupId";
    private static final String KEY_FIELD_BILLLIST = "billlistap";
    private static final String KEY_FIELD_FILTER = "filtercontainerap";

    public void registerListener(EventObject e) {
        super.registerListener(e);
        TreeView treeView = (TreeView)this.getView().getControl("treeview");
        treeView.addTreeNodeClickListener((TreeNodeClickListener)this);
        BillList billList = (BillList)this.getControl(KEY_FIELD_BILLLIST);
        billList.addCreateListDataProviderListener((CreateListDataProviderListener)this);
        this.setEntityId();
        FilterContainer filterContainer = (FilterContainer)this.getControl(KEY_FIELD_FILTER);
        filterContainer.addAfterSearchClickListener((AfterSearchClickListener)this);
        filterContainer.refresh();
    }

    public void click(SearchClickEvent searchClickEvent) {
        List filterList = searchClickEvent.getFastQFilters();
        this.saveQfilterList2Cache(filterList);
        BillList billList = (BillList)this.getControl(KEY_FIELD_BILLLIST);
        billList.getEntryState().setCurrentPageIndex(Integer.valueOf(1));
        this.getView().updateView(KEY_FIELD_BILLLIST);
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        super.beforeItemClick(evt);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)"reset", (CharSequence)formOperate.getOperateKey())) {
            this.saveQfilterList2Cache(null);
            this.initTreeView();
        } else if (StringUtils.equals((CharSequence)"donothing", (CharSequence)formOperate.getOperateKey())) {
            String groupId = this.getPageCache().get(KEY_CACHE_GROUP_ID);
            if (StringUtils.isNotEmpty((CharSequence)groupId) && !"1010".equals(groupId)) {
                Map map = HRCertCommonHelper.verifyCertCount((String)groupId);
                HRCertCommonHelper.clearCache();
                if (null == map || HRCertPromptInfoTypeEnum.FORBIDDEN.getName().equals(map.get("infoType"))) {
                    this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5f53\u524d%s\u8bb8\u53ef\u5360\u7528\u6570\u91cf\u5df2\u8d85\u51fa\u6700\u5927\u8d2d\u4e70\u6570\u91cf\u4e0a\u9650\uff0c\u65e0\u6cd5\u66f4\u65b0\u8bb8\u53ef\u660e\u7ec6\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u8865\u5145\u8bb8\u53ef\u8d2d\u4e70\u6570\u91cf\u3002", (String)"CertGroupMemberPlugin_1", (String)"hrmp-hbss-formplugin", (Object[])new Object[]{HRCertCommonHelper.getGroupName((String)groupId)}));
                } else {
                    Map resultMap = HRCertCommonHelper.updateBizAssignData((String)groupId);
                    if (resultMap == null) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u529f\u80fd\u5f00\u53d1\u4e2d\uff0c\u656c\u8bf7\u671f\u5f85...", (String)"CertGroupMemberPlugin_2", (String)"hrmp-hbss-formplugin", (Object[])new Object[0]));
                    } else if (resultMap.containsKey("exeFlag") && ((Boolean)resultMap.get("exeFlag")).booleanValue()) {
                        this.getView().showConfirm(ResManager.loadKDString((String)"\u66f4\u65b0\u8bb8\u53ef\u660e\u7ec6", (String)"CertGroupMemberPlugin_5", (String)"hrmp-hbss-formplugin", (Object[])new Object[0]), ResManager.loadKDString((String)"\u6b63\u5728\u66f4\u65b0\u8bb8\u53ef\u660e\u7ec6\uff0c\u9884\u8ba1\u9700\u89815\u5206\u949f\uff0c\u8bf75\u5206\u949f\u540e\u518d\u64cd\u4f5c\u4e1a\u52a1\u3002", (String)"CertGroupMemberPlugin_3", (String)"hrmp-hbss-formplugin", (Object[])new Object[0]), MessageBoxOptions.OK, ConfirmTypes.Default, null);
                    } else {
                        this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5f53\u524d%s\u8bb8\u53ef\u5360\u7528\u6570\u91cf\u5df2\u8d85\u51fa\u6700\u5927\u8d2d\u4e70\u6570\u91cf\u4e0a\u9650\uff0c\u65e0\u6cd5\u66f4\u65b0\u8bb8\u53ef\u660e\u7ec6\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u8865\u5145\u8bb8\u53ef\u8d2d\u4e70\u6570\u91cf\u3002", (String)"CertGroupMemberPlugin_1", (String)"hrmp-hbss-formplugin", (Object[])new Object[]{HRCertCommonHelper.getGroupName((String)groupId)}));
                    }
                }
            } else {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u9700\u8981\u66f4\u65b0\u7684\u8bb8\u53ef\u5206\u7ec4\u3002", (String)"CertGroupMemberPlugin_4", (String)"hrmp-hbss-formplugin", (Object[])new Object[0]));
            }
        }
    }

    public void createListDataProvider(BeforeCreateListDataProviderArgs beforeCreateListDataProviderArgs) {
        String groupId = this.getPageCache().get(KEY_CACHE_GROUP_ID);
        List<QFilter> filterList = this.getQfilterListFromCache();
        beforeCreateListDataProviderArgs.setListDataProvider((IListDataProvider)new CertListDataProvider(groupId, filterList));
    }

    public void treeNodeClick(TreeNodeEvent evt) {
        String nodeId;
        super.treeNodeClick(evt);
        Control source = (Control)evt.getSource();
        if ("treeview".equals(source.getKey()) && HRCertConstant.GROUP_ID_SET.contains(nodeId = (String)evt.getNodeId())) {
            this.showFieldList(nodeId);
        }
    }

    public void afterCreateNewData(EventObject e) {
        super.afterCreateNewData(e);
        this.setEntityId();
        this.initTreeView();
        this.initListView();
    }

    private void initListView() {
        BillList billList = (BillList)this.getControl(KEY_FIELD_BILLLIST);
        String groupId = this.getPageCache().get(KEY_CACHE_GROUP_ID);
        List<QFilter> filterList = this.getQfilterListFromCache();
        billList.getListModel().setProvider((IListDataProvider)new CertListDataProvider(groupId, filterList));
    }

    private void setEntityId() {
        FilterContainer filterContainer = (FilterContainer)this.getControl(KEY_FIELD_FILTER);
        filterContainer.setBillFormId("hbss_certmember");
        filterContainer.setEntityId("hbss_certmember");
    }

    private void showFieldList(String nodeId) {
        this.getPageCache().put(KEY_CACHE_GROUP_ID, nodeId);
        if (StringUtils.isNotEmpty((CharSequence)nodeId) && !"1010".equals(nodeId)) {
            this.refreshFilterAndPageIndex();
            this.getView().updateView(KEY_FIELD_BILLLIST);
        }
    }

    private void refreshFilterAndPageIndex() {
        BillList billList = (BillList)this.getControl(KEY_FIELD_BILLLIST);
        billList.getEntryState().setCurrentPageIndex(Integer.valueOf(1));
        HRPageCache pageCache = new HRPageCache(this.getView());
        pageCache.put(KEY_CACHE_FILTER_SIZE, (Object)0);
        FilterContainer filterContainer = (FilterContainer)this.getControl(KEY_FIELD_FILTER);
        filterContainer.clearFast();
        this.getView().updateView(KEY_FIELD_FILTER);
    }

    private void initTreeView() {
        TreeView pairTree = (TreeView)this.getView().getControl("treeview");
        pairTree.deleteAllNodes();
        TreeNode rootNode = new TreeNode("", "0", ResManager.loadKDString((String)"\u5957\u4ef6\u6a21\u5f0f", (String)"CertGroupMemberPlugin_6", (String)"hrmp-hbss-formplugin", (Object[])new Object[0]));
        pairTree.addNode(rootNode);
        pairTree.expand(rootNode.getId());
        TreeNode[] firstNode = new TreeNode[]{null};
        List packetList = HRCertCommonHelper.getHRCertPacketList();
        for (HRCertPacket packet : packetList) {
            TreeNode packetNode = new TreeNode("", packet.getGroupId(), packet.getGroupName());
            packet.getHasGroups().forEach(group -> {
                TreeNode groupNode = new TreeNode("", group.getGroupId(), group.getDisplayGroupName());
                packetNode.addChild(groupNode);
                if (firstNode[0] == null) {
                    firstNode[0] = groupNode;
                }
            });
            rootNode.addChild(packetNode);
            pairTree.expand(packetNode.getId());
        }
        if (firstNode[0] != null) {
            pairTree.focusNode(firstNode[0]);
            this.getPageCache().put(KEY_CACHE_GROUP_ID, firstNode[0].getId());
        }
    }

    private void saveQfilterList2Cache(List<QFilter> filterList) {
        HRPageCache pageCache = new HRPageCache(this.getView());
        if (filterList != null && filterList.size() > 0) {
            pageCache.put(KEY_CACHE_FILTER_SIZE, (Object)filterList.size());
            for (int idx = 0; idx < filterList.size(); ++idx) {
                pageCache.put(KEY_CACHE_FILTER_PREFIX + idx, (Object)filterList.get(idx).toSerializedString());
            }
        } else {
            pageCache.put(KEY_CACHE_FILTER_SIZE, (Object)0);
        }
    }

    private List<QFilter> getQfilterListFromCache() {
        HRPageCache pageCache = new HRPageCache(this.getView());
        Integer filterSize = (Integer)pageCache.get(KEY_CACHE_FILTER_SIZE, Integer.class);
        if (filterSize != null && filterSize > 0) {
            ArrayList<QFilter> resultList = new ArrayList<QFilter>(10);
            for (int idx = 0; idx < filterSize; ++idx) {
                resultList.add(QFilter.fromSerializedString((String)((String)pageCache.get(KEY_CACHE_FILTER_PREFIX + idx, String.class))));
            }
            return resultList;
        }
        return new ArrayList<QFilter>(10);
    }
}
