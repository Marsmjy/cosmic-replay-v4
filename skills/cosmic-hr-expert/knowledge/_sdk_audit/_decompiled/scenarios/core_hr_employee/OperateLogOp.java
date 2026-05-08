/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.id.ID
 *  kd.hr.hbp.business.application.impl.common.HrEntityCommonService
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hrmp.hrpi.business.domain.log.service.OperateLogHandler
 */
package kd.hrmp.hrpi.opplugin.web.log;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.id.ID;
import kd.hr.hbp.business.application.impl.common.HrEntityCommonService;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hrmp.hrpi.business.domain.log.service.OperateLogHandler;

public final class OperateLogOp
extends AbstractOperationServicePlugIn {
    private OperateLogHandler operateLogHandler = new OperateLogHandler();

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List propertySet = e.getFieldKeys();
        propertySet.add("employee");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        boolean isEmployeePage;
        super.beginOperationTransaction(args);
        DynamicObject[] dys = args.getDataEntities();
        if (dys == null || dys.length == 0) {
            return;
        }
        OperateOption option = this.getOption();
        option.setVariableValue("eventId", String.valueOf(ID.genLongId()));
        String entityNumber = dys[0].getDynamicObjectType().getName();
        List allParentAndSelfEntityNames = HrEntityCommonService.getInstance().getParentEntity(entityNumber);
        boolean bl = isEmployeePage = "hrpi_employee".equals(entityNumber) || allParentAndSelfEntityNames.contains("hrpi_employee");
        if (isEmployeePage) {
            option.setVariableValue("logBizCustomVal", "boid");
        } else {
            option.setVariableValue("logBizCustomVal", "employee");
        }
        String auditField = option.getVariableValue("audit_field", "");
        Map modifyInfoMap = this.operateLogHandler.buildModifyContent(args.getOperationKey().toLowerCase(Locale.ROOT), dys, auditField);
        this.operateLogHandler.setModifyInfoMap(modifyInfoMap);
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        DynamicObject[] dys = args.getDataEntities();
        if (dys == null || dys.length == 0) {
            return;
        }
        List successPkIds = this.getOperationResult().getSuccessPkIds();
        if (successPkIds.size() > 0) {
            List successDys = Arrays.stream(dys).filter(dy -> successPkIds.contains(dy.getPkValue())).collect(Collectors.toList());
            Map variables = this.getOption().getVariables();
            String eventId = (String)variables.get("eventId");
            String logBizCustomVal = (String)variables.get("logBizCustomVal");
            if (HRStringUtils.isBlank((CharSequence)eventId)) {
                this.operateLogHandler.batchInsertLog(successDys, logBizCustomVal);
            } else {
                this.operateLogHandler.batchInsertLog(successDys, Long.valueOf(eventId), logBizCustomVal, this.operateLogHandler.getModifyInfoMap());
            }
        }
    }
}
