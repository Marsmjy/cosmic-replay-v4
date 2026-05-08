/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.opplugin.web.HRCoreBaseBillOp
 *  kd.hr.homs.business.domain.batchbill.service.OrgBatchBreakupService
 *  kd.hr.homs.opplugin.web.orgbatch.validator.AdminOrgBatchBillBreakupStatusValidator
 */
package kd.hr.homs.opplugin.web.orgbatch;

import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.opplugin.web.HRCoreBaseBillOp;
import kd.hr.homs.business.domain.batchbill.service.OrgBatchBreakupService;
import kd.hr.homs.opplugin.web.orgbatch.validator.AdminOrgBatchBillBreakupStatusValidator;

public final class AdminOrgBatchBreakupOp
extends HRCoreBaseBillOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        e.getValidators().add(new AdminOrgBatchBillBreakupStatusValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] bills = args.getDataEntities();
        OrgBatchBreakupService.doBreakUpBill((DynamicObject)bills[0], (OperateOption)this.getOption());
    }
}
