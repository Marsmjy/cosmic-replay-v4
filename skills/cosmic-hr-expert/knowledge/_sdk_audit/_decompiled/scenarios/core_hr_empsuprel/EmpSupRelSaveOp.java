/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRDynamicObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.domain.assigment.service.IEmpSupRelDomainService
 *  kd.hrmp.hrpi.common.enums.BusinessStatusEnum
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelEmpposorelDateValidator
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelEmpposorelValidator
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelReportTypeValidator
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelSuperiorValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRDynamicObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.domain.assigment.service.IEmpSupRelDomainService;
import kd.hrmp.hrpi.common.enums.BusinessStatusEnum;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelEmpposorelDateValidator;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelEmpposorelValidator;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelReportTypeValidator;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelSuperiorValidator;

public final class EmpSupRelSaveOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(EmpSupRelSaveOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("startdate");
        fieldKeys.add("enddate");
        fieldKeys.add("reporttype");
        fieldKeys.add("superiorempposorgrel");
        fieldKeys.add("empposorgrel");
        fieldKeys.add("assignment");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new EmpSupRelSuperiorValidator());
        args.addValidator((AbstractValidator)new EmpSupRelEmpposorelValidator());
        args.addValidator((AbstractValidator)new EmpSupRelEmpposorelDateValidator());
        args.addValidator((AbstractValidator)new EmpSupRelReportTypeValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] dys = args.getDataEntities();
        ArrayList<DynamicObject> delList = new ArrayList<DynamicObject>(16);
        ArrayList<DynamicObject> modifyList = new ArrayList<DynamicObject>(16);
        for (DynamicObject dy : dys) {
            DynamicObject[] existDys;
            Date startDate = dy.getDate("startdate");
            long startTime = startDate.getTime();
            Date endDate = dy.getDate("enddate");
            endDate = endDate == null ? TimeLineServiceUtil.getMaxEffEndDate() : endDate;
            long endTime = endDate.getTime();
            for (DynamicObject existDy : existDys = IEmpSupRelDomainService.getInstance().getOverlapEmpSupRel(dy)) {
                long existStartTime = existDy.getDate("startdate").getTime();
                long existEndTime = existDy.getDate("enddate").getTime();
                if (startTime <= existStartTime && endTime >= existEndTime) {
                    delList.add(existDy);
                    continue;
                }
                if (startTime > existStartTime && endTime < existEndTime) {
                    DynamicObject newHisDy = IEmpSupRelDomainService.getInstance().generateEmptyDynamicObject();
                    HRDynamicObjectUtils.copy((DynamicObject)existDy, (DynamicObject)newHisDy);
                    existDy.set("enddate", (Object)HRDateTimeUtils.addDay((Date)startDate, (long)-1L));
                    modifyList.add(existDy);
                    newHisDy.set("startdate", (Object)HRDateTimeUtils.addDay((Date)endDate, (long)1L));
                    modifyList.add(newHisDy);
                    continue;
                }
                if (startTime <= existStartTime) {
                    existDy.set("startdate", (Object)HRDateTimeUtils.addDay((Date)endDate, (long)1L));
                    modifyList.add(existDy);
                    continue;
                }
                if (endTime < existEndTime) continue;
                existDy.set("enddate", (Object)HRDateTimeUtils.addDay((Date)startDate, (long)-1L));
                modifyList.add(existDy);
            }
            String businessStatus = BusinessStatusEnum.getBusinessStatusByDate((Date)startDate, (Date)endDate);
            if (!HRStringUtils.isNotEmpty((String)businessStatus)) continue;
            dy.set("businessstatus", (Object)businessStatus);
        }
        if (!delList.isEmpty()) {
            IEmpSupRelDomainService.getInstance().delete((Object[])delList.toArray(new DynamicObject[0]));
        }
        if (!modifyList.isEmpty()) {
            IEmpSupRelDomainService.getInstance().save(modifyList.toArray(new DynamicObject[0]));
        }
    }
}
