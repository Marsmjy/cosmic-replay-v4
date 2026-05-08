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
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IEmpStageApplicationService
 *  kd.hrmp.hrpi.business.domain.employee.service.IEmpStageDomainService
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IEmpStageApplicationService;
import kd.hrmp.hrpi.business.domain.employee.service.IEmpStageDomainService;

public final class TerminationInfoEmpStageOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(TerminationInfoEmpStageOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("departdate");
        fieldKeys.add("employee");
        fieldKeys.add("empstage");
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        Object[] dataEntities = e.getDataEntities();
        if (ObjectUtils.isEmpty((Object[])dataEntities)) {
            return;
        }
        String operationKey = e.getOperationKey();
        ArrayList list = new ArrayList(10);
        if ("save".equals(operationKey)) {
            ArrayList<DynamicObject> stageList = new ArrayList<DynamicObject>(10);
            for (Object terminalInfo : dataEntities) {
                DynamicObject employee = terminalInfo.getDynamicObject("employee");
                Date departDate = terminalInfo.getDate("departdate");
                DynamicObject empStageByDate = IEmpStageApplicationService.getInstance().getStandardEmpStageByDate(departDate, Long.valueOf(employee.getLong("id")));
                if (empStageByDate == null || empStageByDate.getDate("departdate") != null && empStageByDate.getDate("departdate").compareTo(departDate) == 0) continue;
                if (terminalInfo.getDate("departdate").compareTo(empStageByDate.getDate("entrydate")) == 0) {
                    if (empStageByDate.getDate("enddate").compareTo(terminalInfo.getDate("departdate")) == 0) {
                        empStageByDate.set("departdate", (Object)terminalInfo.getDate("departdate"));
                    } else {
                        DynamicObject empStage = IEmpStageDomainService.getInstance().getLatestEmpStageByDate(employee.getLong("id"), empStageByDate.getDate("entrydate"));
                        if (empStage != null) {
                            empStageByDate = empStage;
                        }
                        empStageByDate.set("departdate", (Object)terminalInfo.getDate("departdate"));
                    }
                } else {
                    empStageByDate.set("departdate", (Object)terminalInfo.getDate("departdate"));
                }
                stageList.add(empStageByDate);
            }
            if (!ObjectUtils.isEmpty(stageList)) {
                IEmpStageDomainService.getInstance().save(stageList.toArray(new DynamicObject[0]));
            }
        } else if ("delete".equals(operationKey)) {
            Arrays.stream(dataEntities).forEach(obj -> {
                if (obj.getDynamicObject("empstage") == null) {
                    return;
                }
                DynamicObject empStageObj = IEmpStageDomainService.getInstance().load((Object)obj.getLong("empstage.id"));
                if (empStageObj == null) {
                    return;
                }
                empStageObj.set("departdate", null);
                list.add(empStageObj);
            });
            if (ObjectUtils.isEmpty(list)) {
                return;
            }
            IEmpStageDomainService.getInstance().save(list.toArray(new DynamicObject[0]));
        }
    }
}
