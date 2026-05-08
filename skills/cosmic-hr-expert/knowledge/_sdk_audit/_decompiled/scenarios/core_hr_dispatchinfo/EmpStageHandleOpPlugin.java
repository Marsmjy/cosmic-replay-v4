/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IEmpStageApplicationService
 */
package kd.hrmp.hrpi.opplugin.web.assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IEmpStageApplicationService;

public final class EmpStageHandleOpPlugin
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(EmpStageHandleOpPlugin.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("employee");
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        Object[] dataEntities = e.getDataEntities();
        if (ObjectUtils.isEmpty((Object[])dataEntities)) {
            return;
        }
        String entityName = dataEntities[0].getDynamicObjectType().getName();
        if (!IEmpStageApplicationService.ENTITY_SET.contains(entityName)) {
            return;
        }
        if (HRStringUtils.equals((String)Boolean.TRUE.toString(), (String)((String)this.getOption().getVariables().get("hr_revise_entrydate_departdate")))) {
            return;
        }
        LOGGER.info("EmpStageHandleOpPlugin.endOperationTransaction,\u5f00\u59cb\u7ef4\u62a4\u5b9e\u4f53{}\u7684\u96c7\u4f63\u9636\u6bb5", (Object)entityName);
        String operationKey = e.getOperationKey();
        if ("save".equals(operationKey) || "delete".equals(operationKey)) {
            this.checkAndResetEmpStage((DynamicObject[])dataEntities);
        }
    }

    private void checkAndResetEmpStage(DynamicObject[] dataEntities) {
        ArrayList empIdList = new ArrayList(10);
        Arrays.stream(dataEntities).forEach(obj -> empIdList.add(obj.getLong("employee.id")));
        String entityName = dataEntities[0].getDataEntityType().getName();
        HRBaseServiceHelper helper = new HRBaseServiceHelper(entityName);
        DynamicObject[] objects = helper.loadDynamicObjectArray(new QFilter[]{new QFilter("employee.id", "in", empIdList)});
        IEmpStageApplicationService.getInstance().saveEmpStage(objects);
    }
}
