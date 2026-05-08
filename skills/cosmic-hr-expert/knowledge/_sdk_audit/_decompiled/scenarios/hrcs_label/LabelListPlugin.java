/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.RefObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.BeforeClickEvent
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.list.plugin.AbstractTreeListPlugin
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.label.LblHelperConstants
 *  kd.hr.hrcs.formplugin.web.label.LabelListPlugin$LabelListDataProvider
 *  kd.hr.hrcs.formplugin.web.label.util.LabelDialogShowUtil
 */
package kd.hr.hrcs.formplugin.web.label;

import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.RefObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.form.control.Control;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.BeforeClickEvent;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.plugin.AbstractTreeListPlugin;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.label.LblHelperConstants;
import kd.hr.hrcs.formplugin.web.label.LabelListPlugin;
import kd.hr.hrcs.formplugin.web.label.util.LabelDialogShowUtil;

public final class LabelListPlugin
extends AbstractTreeListPlugin {
    private static final LabelServiceHelper labelServiceHelper = new LabelServiceHelper();
    private static final String CURRENT_MODULE_NAME = "hrmp-hrcs-formplugin";
    private static final String PUBLIC_LABEL_ID_STR = "1000000000000000000";
    private static final Pattern numberPattern = Pattern.compile("^[\\d]*$");
    private static final String OP_SECONDARY_DISABLE = "secondaryDisable";

    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addItemClickListeners(new String[]{"tbmain"});
        this.addClickListeners(new String[]{"btndel", "btnedit", "btnnew"});
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        ListSelectedRowCollection coll;
        List ids;
        boolean isPolicyRef;
        super.beforeDoOperation(args);
        AbstractOperate operate = (AbstractOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        if ("disable".equals(operateKey) && !operate.getOption().tryGetVariableValue(OP_SECONDARY_DISABLE, new RefObject()) && (isPolicyRef = LabelObjectServiceHelper.isEnableLblStrategy4Lbl(ids = (coll = ((ListView)this.getView()).getSelectedRows()).stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList())))) {
            LabelDialogShowUtil.openLabelDialog((AbstractFormPlugin)this, ids, (String)"label");
            args.setCancel(true);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String disableRef;
        String actionId = closedCallBackEvent.getActionId();
        if ("disableRef".equals(actionId) && Boolean.parseBoolean(disableRef = (String)closedCallBackEvent.getReturnData())) {
            OperateOption disableOption = OperateOption.create();
            disableOption.setVariableValue(OP_SECONDARY_DISABLE, "true");
            this.getView().invokeOperation("disable", disableOption);
        }
    }

    public void beforeClick(BeforeClickEvent evt) {
        String operateKey;
        switch (operateKey = ((Control)evt.getSource()).getKey()) {
            case "btnedit": 
            case "btndel": {
                if (!this.isPublicLabel()) break;
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u4fee\u6539\u201c\u516c\u5171\u6807\u7b7e\u201d\u8282\u70b9\u3002", (String)"LabelListPlugin_1", (String)CURRENT_MODULE_NAME, (Object[])new Object[0]));
                evt.setCancel(true);
                break;
            }
            case "btnnew": {
                if (!this.curNodeIsLeaf()) break;
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u5206\u7ec4\u5df2\u662f\u672b\u7ea7\u5206\u7ec4\uff0c\u4e0d\u5141\u8bb8\u5efa\u4e0b\u7ea7\u5206\u7ec4", (String)"LabelListPlugin_2", (String)CURRENT_MODULE_NAME, (Object[])new Object[0]));
                evt.setCancel(true);
            }
        }
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        TreeView treeView = (TreeView)this.getControl("treeview");
        treeView.expand(PUBLIC_LABEL_ID_STR);
    }

    private boolean curNodeIsLeaf() {
        String idStr;
        TreeView tv = (TreeView)this.getControl("treeview");
        Map focusNodeMap = tv.getTreeState().getFocusNode();
        if (!CollectionUtils.isEmpty((Map)focusNodeMap) && this.isNumber(idStr = (String)focusNodeMap.get("id"))) {
            Long id = Long.parseLong(idStr);
            DynamicObject nodeDy = LblHelperConstants.labelGroupServiceHelper.queryOne("id,isleaf", new QFilter("id", "=", (Object)id));
            return nodeDy.getBoolean("isleaf") && !PUBLIC_LABEL_ID_STR.equals(idStr);
        }
        return false;
    }

    private boolean isNumber(String numStr) {
        return numberPattern.matcher(numStr).matches();
    }

    private boolean isPublicLabel() {
        String idStr;
        TreeView tv = (TreeView)this.getControl("treeview");
        Map focusNodeMap = tv.getTreeState().getFocusNode();
        return !CollectionUtils.isEmpty((Map)focusNodeMap) && PUBLIC_LABEL_ID_STR.equals(idStr = (String)focusNodeMap.get("id"));
    }

    public void initializeTree(EventObject e) {
        this.getTreeModel().setRootVisable(false);
        this.getView().setVisible(Boolean.FALSE, new String[]{"cardview"});
    }

    public void setFilter(SetFilterEvent e) {
        e.setOrderBy("createtime desc");
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        args.getHyperLinkClickEvent().getFieldName();
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        super.beforeCreateListDataProvider(args);
        args.setListDataProvider((IListDataProvider)new LabelListDataProvider());
    }

    static /* synthetic */ LabelServiceHelper access$000() {
        return labelServiceHelper;
    }
}
