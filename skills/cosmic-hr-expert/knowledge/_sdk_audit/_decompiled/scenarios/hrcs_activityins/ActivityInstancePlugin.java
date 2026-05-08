/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.HyperLinkClickEvent
 *  kd.bos.form.events.HyperLinkClickListener
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.plugin.AbstractListPlugin
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.workflow.WorkflowServiceHelper
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.servicehelper.activity.ActivityInsServiceHelper
 *  kd.hr.hrcs.formplugin.web.utils.ActivityBillFormUtil
 */
package kd.hr.hrcs.formplugin.web.activity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.HyperLinkClickEvent;
import kd.bos.form.events.HyperLinkClickListener;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.workflow.WorkflowServiceHelper;
import kd.bos.util.StringUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.servicehelper.activity.ActivityInsServiceHelper;
import kd.hr.hrcs.formplugin.web.utils.ActivityBillFormUtil;

public class ActivityInstancePlugin
extends AbstractListPlugin
implements HyperLinkClickListener {
    private static final String HRCS_ASSIGNREC_PAGE_KEY = "hrcs_actassignrec";
    private static final String FORMID = "hrcs_actassign";
    private static final String META_NUMBER_HRCS_ACTIVITYNODELOG = "hrcs_activitynodelog";
    private static final String OP_SHOW_LOG = "showlog";

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        Long actInsId = (Long)this.getFocusRowPkId();
        DynamicObject actInsInfo = this.getActInsInfo(actInsId);
        String bizKey = actInsInfo.getString("bizkey");
        String bizBillId = actInsInfo.getString("bizbillid");
        String bindBizKey = actInsInfo.getString("bindbizkey");
        String bindBizBillId = actInsInfo.getString("bindbizbillid");
        if ("biznum".equals(args.getHyperLinkClickEvent().getFieldName())) {
            args.setCancel(true);
            this.getView().showForm((FormShowParameter)ActivityBillFormUtil.assembleShowBillFormParam((String)bizKey, (ShowType)ShowType.Modal, (OperationStatus)OperationStatus.VIEW, (Object)bizBillId, null, null));
        } else if ("bindbiznum".equals(args.getHyperLinkClickEvent().getFieldName())) {
            args.setCancel(true);
            String layoutId = actInsInfo.getString("bindinglayoutid");
            String formId = StringUtils.isEmpty((String)layoutId) ? bindBizKey : layoutId;
            this.getView().showForm((FormShowParameter)ActivityBillFormUtil.assembleShowBillFormParam((String)formId, (ShowType)ShowType.Modal, (OperationStatus)OperationStatus.VIEW, (Object)bindBizBillId, null, null));
        }
    }

    private void openAssignToPage(String taskSwitch) {
        if (HRStringUtils.equals((String)"3", (String)taskSwitch)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u6d3b\u52a8\u4efb\u52a1\u5df2\u8fdb\u5165\u5171\u4eab\uff0c\u65e0\u6cd5\u5206\u914d\u5904\u7406\u4eba\u3002", (String)"ActivityInstancePlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        this.getPageCache().put("actInsToAssign", String.valueOf(this.getFocusRowPkId()));
        this.getView().showForm(ActivityBillFormUtil.assembleShowDynamicFormParam((String)FORMID, null, (CloseCallBack)new CloseCallBack((IFormPlugin)this, FORMID), (ShowType)ShowType.Modal));
    }

    private void viewTaskFlowChartPage(String bizBillId) {
        WorkflowServiceHelper.viewFlowchart((String)this.getView().getPageId(), (Object)bizBillId);
    }

    private void openViewRecPage(Long actInsId) {
        if (this.getSelectedRows().size() > 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u4e2d\u4e00\u6761\u4efb\u52a1\u8fdb\u884c\u67e5\u770b\u5206\u914d\u5386\u53f2\u3002", (String)"ActivityInstancePlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        QFilter qfilter = new QFilter("activityins.id", "=", (Object)actInsId);
        this.getView().showForm((FormShowParameter)ActivityBillFormUtil.assembleShowListFormParam((String)HRCS_ASSIGNREC_PAGE_KEY, null, (CloseCallBack)new CloseCallBack((IFormPlugin)this, HRCS_ASSIGNREC_PAGE_KEY), (ShowType)ShowType.Modal, (QFilter)qfilter));
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        String actionId = closedCallBackEvent.getActionId();
        if (actionId.equals(FORMID)) {
            this.getView().invokeOperation("refresh");
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        ListSelectedRowCollection listSelectedData = args.getListSelectedData();
        FormOperate source = (FormOperate)args.getSource();
        if (OP_SHOW_LOG.equals(source.getOperateKey()) && !listSelectedData.isEmpty()) {
            DynamicObject activityIns = ActivityInsServiceHelper.getActivityInsById((String)"wfnode,wfprocessinsid", (Long)((Long)listSelectedData.get(0).getPrimaryKeyValue()));
            if (activityIns != null) {
                ListShowParameter listShowParameter = new ListShowParameter();
                listShowParameter.setBillFormId(META_NUMBER_HRCS_ACTIVITYNODELOG);
                listShowParameter.setFormId("bos_listf7");
                ArrayList filterList = Lists.newArrayListWithExpectedSize((int)2);
                filterList.add(new QFilter("wfprocessinsid", "=", activityIns.get("wfprocessinsid")));
                filterList.add(new QFilter("wfnodeid", "=", activityIns.get("wfnode")));
                listShowParameter.setListFilterParameter(new ListFilterParameter((List)filterList, "createtime desc"));
                listShowParameter.getOpenStyle().setShowType(ShowType.Modal);
                StyleCss css = new StyleCss();
                css.setWidth("960px");
                css.setHeight("580px");
                listShowParameter.getOpenStyle().setInlineStyleCss(css);
                this.getView().showForm((FormShowParameter)listShowParameter);
            } else {
                this.getView().showErrorNotification("instance log is empty");
            }
        } else if ("assignto".equals(source.getOperateKey()) && this.getSelectedRows().size() > 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u4ec5\u5141\u8bb8\u540c\u65f6\u9009\u4e2d\u4e00\u6761\u4efb\u52a1\u8fdb\u884c\u5206\u914d\u3002", (String)"ActivityInstancePlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        OperationResult result = args.getOperationResult();
        if (null == result || !result.isSuccess()) {
            return;
        }
        String key = ((AbstractOperate)args.getSource()).getOperateKey();
        Long actInsId = (Long)this.getFocusRowPkId();
        if (null != actInsId) {
            DynamicObject actInsInfo = this.getActInsInfo(actInsId);
            String bizBillId = actInsInfo.getString("bizbillid");
            if ("assignto".equals(key)) {
                this.openAssignToPage(actInsInfo.getString("taskswitch"));
            } else if ("viewrec".equals(key)) {
                this.openViewRecPage(actInsId);
            } else if ("viewtaskflowchart".equals(key)) {
                this.viewTaskFlowChartPage(bizBillId);
            }
        }
    }

    private DynamicObject getActInsInfo(Long actInsId) {
        return ActivityInsServiceHelper.getActivityInsById((String)"", (Long)actInsId);
    }

    public void hyperLinkClick(HyperLinkClickEvent event) {
        this.getFocusRowPkId();
    }

    public void setFilter(SetFilterEvent event) {
        event.getQFilters().add(new QFilter("isabandon", "=", (Object)"0"));
    }
}
