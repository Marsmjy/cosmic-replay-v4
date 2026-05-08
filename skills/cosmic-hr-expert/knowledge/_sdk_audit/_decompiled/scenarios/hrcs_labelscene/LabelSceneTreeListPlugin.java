/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ITreeModel
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin
 *  kd.hr.hrcs.formplugin.web.filter.util.HRTreeSearchUtil
 *  kd.hr.hrcs.formplugin.web.label.LabelSceneTreeListPlugin$LabelSceneListDataProvider
 */
package kd.hr.hrcs.formplugin.web.label;

import java.util.EventObject;
import kd.bos.base.BaseShowParameter;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ITreeModel;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin;
import kd.hr.hrcs.formplugin.web.filter.util.HRTreeSearchUtil;
import kd.hr.hrcs.formplugin.web.label.LabelSceneTreeListPlugin;

@ExcludeFromJacocoGeneratedReport
public final class LabelSceneTreeListPlugin
extends HRF7TreeListPlugin {
    private static final String BOS_DEVPORTAL_BIZAPP = "bos_devportal_bizapp";
    private static final HRBaseServiceHelper bosAppServiceHelper = new HRBaseServiceHelper("bos_devportal_bizapp");

    public void refreshNode(RefreshNodeEvent e) {
        ITreeModel tv1 = this.getTreeListView().getTreeModel();
        e.setChildNodes(HRTreeSearchUtil.getChildrenNodes((ITreeModel)tv1));
        HRTreeSearchUtil.resetCurrentNode();
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addItemClickListeners(new String[]{"tbmain"});
    }

    public void search(SearchEnterEvent evt) {
        super.search(evt);
        String searchText = evt.getText();
        TreeNode rootNode = this.getTreeModel().getRoot();
        IPageCache pageCache = this.getPageCache();
        HRTreeSearchUtil.doSearchNode((IFormView)this.getView(), (TreeNode)rootNode, (IPageCache)pageCache, (String)searchText);
        String matchNodesCacheKey = this.getView().getPageId() + "_matchNodes";
        String matchedKeys = pageCache.get(matchNodesCacheKey);
        if (matchedKeys != null && matchedKeys.equals("[]")) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u641c\u7d22\u5df2\u5b8c\u6210\uff0c\u672a\u627e\u5230\u5339\u914d\u9879\u3002", (String)"LabelSceneTreeListPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        }
    }

    public void setFilter(SetFilterEvent e) {
        e.setOrderBy("createtime desc");
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        String btnKey = evt.getItemKey();
        if ("tblnew".equals(btnKey)) {
            BaseShowParameter fsp = new BaseShowParameter();
            fsp.setFormId("hrcs_labelscene");
            fsp.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "NEW_CALLBACK");
            fsp.setCloseCallBack(closeCallBack);
            Object focusedIdObject = this.getTreeModel().getCurrentNodeId();
            if (null != focusedIdObject) {
                String focusedId = (String)focusedIdObject;
                QFilter[] filters = new QFilter[]{new QFilter("id", "=", (Object)focusedId)};
                if (bosAppServiceHelper.isExists(filters)) {
                    fsp.setCustomParam("biz_app_id", (Object)focusedId);
                }
            }
            this.getView().showForm((FormShowParameter)fsp);
            evt.setCancel(true);
        }
    }

    public void closedCallBack(ClosedCallBackEvent e) {
        String callbackKey = e.getActionId();
        if ("NEW_CALLBACK".equals(callbackKey)) {
            this.getView().updateView();
        }
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new LabelSceneListDataProvider());
    }
}
