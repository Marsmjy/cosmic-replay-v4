/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hrptc.business.servicehelper.ReportCenterServiceHelper
 *  kd.hr.hrptc.formplugin.permission.RptGroupTreeList
 *  kd.hr.hrptmc.business.repdesign.RptCenterPublishService
 */
package kd.hr.hrptc.formplugin.permission;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hrptc.business.servicehelper.ReportCenterServiceHelper;
import kd.hr.hrptc.formplugin.permission.RptGroupTreeList;
import kd.hr.hrptmc.business.repdesign.RptCenterPublishService;

public final class RptAlocPermList
extends RptGroupTreeList {
    private static final Log LOGGER = LogFactory.getLog(RptAlocPermList.class);
    private Map<String, String> publishPathMaps;

    public void buildTreeListFilter(BuildTreeListFilterEvent evt) {
        try {
            String currentNodeId = (String)evt.getNodeId();
            if (!String.valueOf(1000L).equals(currentNodeId)) {
                TreeNode treeNode = this.getTreeModel().getRoot().getTreeNode(currentNodeId);
                List childNodeIds = this.getAllTreeNodeChildNodeIds(treeNode);
                childNodeIds.add(currentNodeId);
                List rptGroupIds = childNodeIds.stream().map(Long::parseLong).collect(Collectors.toList());
                List rptManageIds = ReportCenterServiceHelper.getRptIdsByGroupIds(rptGroupIds);
                if (null == rptManageIds) {
                    evt.addQFilter(QFilter.of((String)"1!=1", (Object[])new Object[0]));
                } else {
                    evt.addQFilter(new QFilter("rptmanage", "in", (Object)rptManageIds));
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("buildTreeListFilter_error_", (Throwable)exception);
        }
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        this.initPublishPath();
        args.setListDataProvider((IListDataProvider)new /* Unavailable Anonymous Inner Class!! */);
    }

    private void initPublishPath() {
        if (null == this.publishPathMaps) {
            this.publishPathMaps = RptCenterPublishService.getPublishPathMaps();
        }
    }

    static /* synthetic */ Map access$000(RptAlocPermList x0) {
        return x0.publishPathMaps;
    }
}
