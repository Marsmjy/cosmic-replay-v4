/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.context.OperationContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.operate.AbstractOperationResult
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.operatecol.OperationColItem
 *  kd.bos.list.column.ListOperationColumnDesc
 *  kd.bos.list.plugin.AbstractListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.DispatchServiceHelper
 *  kd.bos.servicehelper.workflow.WorkflowServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.servicehelper.activity.ActivityInsServiceHelper
 */
package kd.hr.hrcs.formplugin.web.activity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;
import kd.bos.context.OperationContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.operate.AbstractOperationResult;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.operatecol.OperationColItem;
import kd.bos.list.column.ListOperationColumnDesc;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.bos.servicehelper.workflow.WorkflowServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.servicehelper.activity.ActivityInsServiceHelper;

public final class ActivityExceptionLogPlugin
extends AbstractListPlugin {
    private static final Log LOGGER = LogFactory.getLog(ActivityExceptionLogPlugin.class);

    public void registerListener(EventObject e) {
        super.registerListener(e);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        ListSelectedRowCollection listSelectedData = args.getListSelectedData();
        if (CollectionUtils.isEmpty((Collection)listSelectedData)) {
            return;
        }
        ListSelectedRow listSelectedRow = listSelectedData.get(0);
        long primaryKeyValue = (Long)listSelectedRow.getPrimaryKeyValue();
        if ("redo".equals(operateKey)) {
            this.redoTask(primaryKeyValue);
        } else if ("batchredooperat".equals(operateKey)) {
            if (CollectionUtils.isEmpty((Collection)listSelectedData)) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u6761\u6570\u636e\u3002", (String)"HRQueryTreeListPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            listSelectedData.forEach(data -> this.redoTask((Long)data.getPrimaryKeyValue()));
        } else if ("viewtaskflowchart".equals(operateKey)) {
            Long id = (Long)this.getFocusRowPkId();
            HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_activityexception");
            DynamicObject dynamicObject = helper.queryOriginalOne("activityins", new QFilter[]{new QFilter("id", "=", (Object)id)});
            if (dynamicObject == null) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u672a\u627e\u5230\u8be5\u4efb\u52a1", (String)"ActivityExceptionLogPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            DynamicObject actInsInfo = this.getActInsInfo(dynamicObject.getLong("activityins"));
            if (actInsInfo == null) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u672a\u627e\u5230\u8be5\u4efb\u52a1", (String)"ActivityExceptionLogPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            String bizBillId = actInsInfo.getString("bizbillid");
            this.viewTaskFlowChartPage(bizBillId);
        }
    }

    public void packageData(PackageDataEvent e) {
        super.packageData(e);
        if (e.getSource() instanceof ListOperationColumnDesc) {
            DynamicObject rowData = e.getRowData();
            String exceptiontype = rowData.getString("exceptiontype");
            String result = rowData.getString("result");
            List operationColItems = (List)e.getFormatValue();
            for (OperationColItem item : operationColItems) {
                if (!"redo".equals(item.getOperationKey())) continue;
                if ("2".equals(exceptiontype) || "1".equals(exceptiontype)) {
                    item.setOperationName(new LocaleString(ResManager.loadKDString((String)"\u5de5\u4f5c\u6d41\u5904\u7406", (String)"ActivityExceptionLogPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
                    item.setForeColor("#333333");
                    item.setLocked(true);
                    continue;
                }
                if (!HRStringUtils.equals((String)"1", (String)result)) continue;
                item.setForeColor("#333333");
                item.setLocked(true);
            }
        }
    }

    private DynamicObject getActInsInfo(Long actInsId) {
        return ActivityInsServiceHelper.getActivityInsById((String)"", (Long)actInsId);
    }

    private void viewTaskFlowChartPage(String bizBillId) {
        WorkflowServiceHelper.viewFlowchart((String)this.getView().getPageId(), (Object)bizBillId);
    }

    private void redoTask(long primaryKeyValue) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_activityerrparam");
        DynamicObjectCollection dynamicObjects = helper.queryOriginalCollection("param", new QFilter[]{new QFilter("exceptionid", "in", (Object)Lists.newArrayList((Object[])new Long[]{primaryKeyValue}))});
        dynamicObjects.forEach(paramsDy -> {
            List param = (List)SerializationUtils.fromJsonString((String)paramsDy.getString("param"), List.class);
            String methodName = param.get(0).toString();
            param.remove(0);
            Object[] finParamArr = param.toArray();
            OperationContext operationContext = OperationContext.get();
            if (operationContext == null) {
                operationContext = new OperationContext();
                operationContext.setAppId("hrcs_activity");
            }
            String appId = operationContext.getAppId();
            operationContext.setAppId("hrcs_activity");
            try {
                OperationResult operationResult = (OperationResult)DispatchServiceHelper.invokeBizService((String)"hrmp", (String)"hrcs", (String)"IHRCSActivityService", (String)methodName, (Object[])finParamArr);
                List allErrorInfo = Optional.ofNullable(operationResult).map(AbstractOperationResult::getAllErrorInfo).orElseGet(ArrayList::new);
                if (!CollectionUtils.isEmpty((Collection)allErrorInfo)) {
                    ActivityInsServiceHelper.updateExceptionStatus((String)"2", (Long)primaryKeyValue);
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u91cd\u65b0\u6267\u884c\u5931\u8d25", (String)"ActivityExceptionLogPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                } else {
                    ActivityInsServiceHelper.updateExceptionStatus((String)"1", (Long)primaryKeyValue);
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u5904\u7406\u6210\u529f", (String)"ActivityExceptionLogPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                }
            }
            catch (Exception exception) {
                ActivityInsServiceHelper.updateExceptionStatus((String)"2", (Long)primaryKeyValue);
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u91cd\u65b0\u6267\u884c\u5931\u8d25", (String)"ActivityExceptionLogPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            }
            operationContext.setAppId(appId);
        });
    }
}
