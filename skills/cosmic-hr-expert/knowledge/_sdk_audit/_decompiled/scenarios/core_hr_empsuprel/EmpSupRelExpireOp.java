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
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.domain.assigment.service.IEmpSupRelDomainService
 *  kd.hrmp.hrpi.common.enums.BusinessStatusEnum
 *  kd.hrmp.hrpi.common.util.DateUtil
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelExpireValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.ArrayList;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.domain.assigment.service.IEmpSupRelDomainService;
import kd.hrmp.hrpi.common.enums.BusinessStatusEnum;
import kd.hrmp.hrpi.common.util.DateUtil;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpSupRelExpireValidator;

public final class EmpSupRelExpireOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(EmpSupRelExpireOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("enddate");
        fieldKeys.add("businessstatus");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new EmpSupRelExpireValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] dys = args.getDataEntities();
        ArrayList<DynamicObject> modifyList = new ArrayList<DynamicObject>(16);
        for (DynamicObject dy : dys) {
            if (!BusinessStatusEnum.BUSINESS_EFF.getCode().equals(dy.getString("businessstatus"))) continue;
            dy.set("businessstatus", (Object)BusinessStatusEnum.BUSINESS_EXPIRED.getCode());
            dy.set("enddate", (Object)DateUtil.getCurrentDate());
            modifyList.add(dy);
        }
        if (!modifyList.isEmpty()) {
            IEmpSupRelDomainService.getInstance().save(modifyList.toArray(new DynamicObject[0]));
        }
    }
}
