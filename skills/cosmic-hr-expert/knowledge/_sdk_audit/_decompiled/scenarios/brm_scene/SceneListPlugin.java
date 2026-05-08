/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.entity.datamodel.ITreeModel
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.control.Search
 *  kd.bos.form.control.events.RefreshNodeEvent
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.control.events.SearchEnterListener
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.hr.brm.common.tools.HRTreeSearchTool
 *  kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin
 */
package kd.hr.brm.formplugin.web;

import com.google.common.collect.Maps;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import kd.bos.entity.datamodel.ITreeModel;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.control.Search;
import kd.bos.form.control.events.RefreshNodeEvent;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.control.events.SearchEnterListener;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.hr.brm.common.tools.HRTreeSearchTool;
import kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin;

public final class SceneListPlugin
extends HRF7TreeListPlugin {
    public void refreshNode(RefreshNodeEvent e) {
        ITreeModel tv1 = this.getTreeListView().getTreeModel();
        e.setChildNodes(HRTreeSearchTool.getChildrenNodes((ITreeModel)tv1));
        HRTreeSearchTool.resetCurrentNode();
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        Search search = (Search)this.getView().getControl("searchap");
        if (search != null) {
            search.addEnterListener((SearchEnterListener)this);
        }
    }

    public void search(SearchEnterEvent evt) {
        super.search(evt);
        String searchText = evt.getText();
        TreeNode rootNode = this.getTreeModel().getRoot();
        IPageCache pageCache = this.getPageCache();
        HRTreeSearchTool.doSearchNode((IFormView)this.getView(), (TreeNode)rootNode, (IPageCache)pageCache, (String)searchText);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        AbstractOperate op;
        String operateKey;
        super.beforeDoOperation(args);
        if (args.getSource() instanceof AbstractOperate && "new".equals(operateKey = (op = (AbstractOperate)args.getSource()).getOperateKey())) {
            this.getPageCache().remove("sceneDefaultAppId");
            TreeNode rootNode = this.getTreeModel().getRoot();
            String currentNodeId = (String)this.getTreeModel().getCurrentNodeId();
            if (rootNode.getNodeLevel(currentNodeId, 0) == 2) {
                this.getPageCache().put("sceneDefaultAppId", currentNodeId);
            }
        }
    }

    public void beforeShowBill(BeforeShowBillFormEvent e) {
        super.beforeShowBill(e);
        HashMap customParams = Maps.newHashMapWithExpectedSize((int)16);
        customParams.put("sceneDefaultAppId", this.getPageCache().get("sceneDefaultAppId"));
        e.getParameter().setCustomParams((Map)customParams);
    }
}
