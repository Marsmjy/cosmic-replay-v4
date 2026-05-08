/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityType
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.IListView
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.operation.OperationServiceHelper
 *  kd.hr.hbp.business.domain.model.timeline.TimelineDataGroup
 *  kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult
 *  kd.hr.hbp.business.service.timeline.ITimelineHandler
 *  kd.hr.hbp.business.service.timeline.TimelineHandlerFactory
 *  kd.hr.hbp.business.service.timeline.dao.TimelineEntityConf
 *  kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.timeline.TimelineConstants
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hbp.formplugin.web.timeline.util.TimeLinePluginUtil
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.formplugin.web.timeline;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityType;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.IListView;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.hr.hbp.business.domain.model.timeline.TimelineDataGroup;
import kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult;
import kd.hr.hbp.business.service.timeline.ITimelineHandler;
import kd.hr.hbp.business.service.timeline.TimelineHandlerFactory;
import kd.hr.hbp.business.service.timeline.dao.TimelineEntityConf;
import kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.timeline.TimelineConstants;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hbp.formplugin.web.timeline.util.TimeLinePluginUtil;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public final class TimelineTplListPlugin
extends HRDataBaseList
implements TimelineConstants {
    private static final String COPY_CLOSE_KEY = "copyCloseKey";
    private static final String CONFIRM_CALL_BACK_DELETE = "confirmCallBackDelete";

    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);
        String entityNumber = ((ListView)this.getView()).getEntityTypeId();
        TimelineEntityConf entityConfig = TimeLinePluginUtil.getEntityConfig((String)entityNumber);
        if (entityConfig.getLogicDelete().booleanValue()) {
            e.getQFilters().add(new QFilter("isdeleted", "=", (Object)Boolean.FALSE));
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        String enableOpConfirm = this.getView().getPageCache().get("enableOpConfirm");
        if (!HRStringUtils.equals((String)enableOpConfirm, (String)"false") && HRStringUtils.equals((String)operateKey, (String)"delete")) {
            this.showConfirmForOp(args);
        }
        this.getView().getPageCache().remove("enableOpConfirm");
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        String entityNumber = ((ListView)this.getView()).getEntityTypeId();
        if (HRStringUtils.equals((String)operateKey, (String)"copy")) {
            BaseShowParameter showParameter = new BaseShowParameter();
            showParameter.setFormId(entityNumber);
            showParameter.setStatus(OperationStatus.ADDNEW);
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            showParameter.setHasRight(true);
            showParameter.setCustomParam("iscopy", (Object)Boolean.TRUE);
            showParameter.setCustomParam("copyDataId", (Object)String.valueOf(this.getFocusRowPkId()));
            showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, COPY_CLOSE_KEY));
            this.getView().showForm((FormShowParameter)showParameter);
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        if (COPY_CLOSE_KEY.equals(evt.getActionId())) {
            this.getView().invokeOperation("refresh");
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        if (!MessageBoxResult.Yes.equals((Object)evt.getResult())) {
            return;
        }
        if (HRStringUtils.equals((String)evt.getCallBackId(), (String)CONFIRM_CALL_BACK_DELETE)) {
            this.getView().getPageCache().put("enableOpConfirm", "false");
            String entityNumber = ((ListView)this.getView()).getEntityTypeId();
            String customValue = evt.getCustomVaule();
            if (HRStringUtils.isEmpty((String)customValue)) {
                return;
            }
            List ids = SerializationUtils.fromJsonStringToList((String)customValue, Long.class);
            HRBaseServiceHelper helper = new HRBaseServiceHelper(entityNumber);
            DynamicObject[] dataList = helper.loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", (Object)ids)});
            OperationResult operationResult = OperationServiceHelper.executeOperate((String)"delete", (String)entityNumber, (DynamicObject[])dataList, (OperateOption)OperateOption.create());
            if (operationResult.isSuccess()) {
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f\u3002", (String)"TimelineTplListPlugin_2", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]));
            } else {
                this.getView().showOperationResult(operationResult);
            }
            this.getView().invokeOperation("refresh");
        }
    }

    private void showConfirmForOp(BeforeDoOperationEventArgs args) {
        List<TimelineDataGroup> timelineDataGroups;
        String entityNumber = ((ListView)this.getView()).getEntityTypeId();
        TimelineEntityConf entityConfig = TimeLinePluginUtil.getEntityConfig((String)entityNumber);
        if (TimeLinePluginUtil.hasNoEntityConfig((TimelineEntityConf)entityConfig, (BeforeDoOperationEventArgs)args, (IFormView)this.getView())) {
            return;
        }
        ITimelineHandler timeLineHandler = TimelineHandlerFactory.getTimelineHandler((String)entityNumber, (TimelineEntityConf)entityConfig);
        TimelineHandlerResult result = timeLineHandler.deleteBatch(timelineDataGroups = this.getTimeLineDataGroups(entityNumber, entityConfig, args.getListSelectedData()), true);
        if (result == null || !result.hasDataChanged()) {
            return;
        }
        String useCustomOpConfirm = this.getView().getPageCache().get("useCustomOpConfirm");
        if (HRStringUtils.equals((String)useCustomOpConfirm, (String)"true")) {
            AbstractOperate op = (AbstractOperate)args.getSource();
            op.getParameter().put("timeLineOpResult", result);
        } else {
            List ids = args.getListSelectedData().stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList());
            this.getView().showConfirm(this.getConfirmTips(), "", MessageBoxOptions.OKCancel, ConfirmTypes.Default, new ConfirmCallBackListener(CONFIRM_CALL_BACK_DELETE, (IFormPlugin)this), null, SerializationUtils.toJsonString(ids));
        }
        args.setCancel(true);
    }

    private String getConfirmTips() {
        EntityType dataEntityType = ((IListView)this.getView()).getListModel().getDataEntityType();
        Map dateDisplayNameMap = TimeLineServiceUtil.getStartDateAndEndDateDisplayName((IDataEntityType)dataEntityType);
        String startDateName = (String)dateDisplayNameMap.get("startdate");
        String endDateName = (String)dateDisplayNameMap.get("enddate");
        return String.format(ResManager.loadKDString((String)"\u786e\u5b9a\u662f\u5426\u5220\u9664\u6570\u636e\uff1f\u5220\u9664\u540e\uff0c\u524d\u4e00\u6bb5\u6570\u636e\u7684%1$s\u5c06\u6839\u636e\u540e\u4e00\u6bb5\u6570\u636e\u7684%2$s\u81ea\u52a8\u8c03\u6574\u3002", (String)"TimelineTplListPlugin_1", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]), endDateName, startDateName);
    }

    private List<TimelineDataGroup> getTimeLineDataGroups(String entityNumber, TimelineEntityConf entityConfig, ListSelectedRowCollection selectedRows) {
        List ids = selectedRows.stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList());
        HRBaseServiceHelper helper = new HRBaseServiceHelper(entityNumber);
        DynamicObject[] dataList = helper.loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", ids)});
        return TimeLineServiceUtil.buildTimeLineDataGroups((String)entityNumber, (TimelineEntityConf)entityConfig, Arrays.asList(dataList));
    }
}
