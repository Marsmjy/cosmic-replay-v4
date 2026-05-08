/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.RefObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.support.util.ReflectionUtils
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.operate.listop.ReturnData
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.BillList
 *  kd.bos.list.ControlContext
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.SessionManager
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.StringUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper
 *  kd.hr.hrcs.formplugin.web.label.util.LabelDialogShowUtil
 */
package kd.hr.hrcs.formplugin.web.label;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.RefObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.support.util.ReflectionUtils;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.operate.listop.ReturnData;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.ControlContext;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.SessionManager;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.StringUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper;
import kd.hr.hrcs.formplugin.web.label.util.LabelDialogShowUtil;

public final class LabelObjectListPlugin
extends HRDataBaseList {
    private static final Log LOGGER = LogFactory.getLog(LabelObjectListPlugin.class);
    private static final String OP_SHOW_FACTOR = "showfactor";
    private static final String OP_SHOW_CONFIG = "showconfig";
    private static final String OP_SECONDARY_DISABLE = "secondaryDisable";

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        ListSelectedRowCollection coll;
        List ids;
        boolean isPolicyRef;
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        String operateKey = formOperate.getOperateKey();
        if ("disablestatus".equals(operateKey) && !formOperate.getOption().tryGetVariableValue(OP_SECONDARY_DISABLE, new RefObject()) && (isPolicyRef = LabelObjectServiceHelper.isEnableLblStrategy4LblObj(ids = (coll = ((ListView)this.getView()).getSelectedRows()).stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList())))) {
            LabelDialogShowUtil.openLabelDialog((AbstractFormPlugin)this, ids, (String)"labelobj");
            args.setCancel(true);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String disableRef;
        String actionId = closedCallBackEvent.getActionId();
        if ("disableRef".equals(actionId) && Boolean.parseBoolean(disableRef = (String)closedCallBackEvent.getReturnData())) {
            OperateOption disableOption = OperateOption.create();
            disableOption.setVariableValue(OP_SECONDARY_DISABLE, "true");
            this.getView().invokeOperation("disablestatus", disableOption);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        String operateKey = formOperate.getOperateKey();
        if (OP_SHOW_CONFIG.equals(operateKey) || OP_SHOW_FACTOR.equals(operateKey)) {
            Long id = (Long)formOperate.getListFocusRow().getPrimaryKeyValue();
            this.showForm(operateKey, id);
        } else if ("disablestatus".equals(operateKey)) {
            OperationResult result = args.getOperationResult();
            if (result.isSuccess()) {
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u7981\u7528\u6210\u529f\u3002", (String)"LabelObjectListPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            }
            this.getView().invokeOperation("refresh");
        } else if ("enablestatus".equals(operateKey)) {
            OperationResult result = args.getOperationResult();
            if (result.isSuccess()) {
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u542f\u7528\u6210\u529f\u3002", (String)"LabelObjectListPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            }
            this.getView().invokeOperation("refresh");
        }
    }

    private void showForm(String operateKey, Long lblObjId) {
        String pageId = SessionManager.getCurrent().get(this.getView().getPageId() + "showForm" + lblObjId + operateKey);
        IFormView view = SessionManager.getCurrent().getView(pageId);
        if (!HRStringUtils.isEmpty((String)pageId) && view != null) {
            IClientViewProxy service = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
            service.addAction("activate", (Object)pageId);
        } else {
            FormShowParameter showParameter = new FormShowParameter();
            if (OP_SHOW_CONFIG.equals(operateKey)) {
                showParameter = new BaseShowParameter();
                DynamicObject lblObjConfig = LabelObjectServiceHelper.getLblObjConfig((Long)lblObjId);
                if (lblObjConfig != null) {
                    ((BaseShowParameter)showParameter).setPkId((Object)lblObjConfig.getLong("id"));
                }
                showParameter.setFormId("hrcs_lblobjconfig");
            } else if (OP_SHOW_FACTOR.equals(operateKey)) {
                showParameter = new ListShowParameter();
                ((ListShowParameter)showParameter).setBillFormId("hrcs_lblobjectfield");
            }
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            showParameter.setCustomParam("labelObjectId", (Object)lblObjId);
            SessionManager.getCurrent().put(this.getView().getPageId() + "showForm" + lblObjId + operateKey, showParameter.getPageId());
            this.getView().showForm(showParameter);
        }
    }

    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);
        BillList billList = (BillList)e.getSource();
        ControlContext context = billList.getContext();
        if (context.isLookup()) {
            e.getQFilters().add(new QFilter("publishstatus", "=", (Object)"1"));
        }
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        try {
            Class<?> clazz = Class.forName("kd.bos.mvc.form.FormView");
            Field field = clazz.getDeclaredField("formOperate");
            ReflectionUtils.makeAccessible((Field)field);
            Object formOperate = field.get(e.getSource());
            if (formOperate instanceof ReturnData) {
                ListShowParameter parameter = (ListShowParameter)this.getView().getFormShowParameter();
                String modifyTip = (String)parameter.getCustomParam("modifytip");
                String modifyId = (String)parameter.getCustomParam("modifyid");
                ListSelectedRow row = ((ListView)this.getView()).getCurrentSelectedRowInfo();
                if (!StringUtils.isEmpty((Object)modifyTip) && null != row && !modifyId.equals(String.valueOf(row.getPrimaryKeyValue())) && null == parameter.getCustomParam("closeCurPage")) {
                    this.getView().showConfirm(modifyTip, MessageBoxOptions.OKCancel, new ConfirmCallBackListener("closeEvent", (IFormPlugin)this));
                    e.setCancel(true);
                }
            }
        }
        catch (Exception ex) {
            LOGGER.error("invoke error!", (Throwable)ex);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        String callBackId = evt.getCallBackId();
        MessageBoxResult result = evt.getResult();
        if (result == MessageBoxResult.Yes && "closeEvent".equals(callBackId)) {
            this.getView().returnDataToParent((Object)((ListView)this.getView()).getSelectedRows());
            this.getView().getFormShowParameter().setCustomParam("closeCurPage", (Object)"true");
            this.getView().close();
        }
    }
}
