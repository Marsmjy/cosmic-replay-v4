/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.InfoCollectHiredValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment.infocollect;

import java.util.ArrayList;
import java.util.List;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.InfoCollectHiredValidator;

public final class InfoCollectStartOp
extends HRDataBaseOp {
    private List<Object> successPkIds = new ArrayList<Object>();

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("employee");
        e.getFieldKeys().add("assignmentstatus");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new InfoCollectHiredValidator(this.successPkIds));
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        if (!HRObjectUtils.isEmpty((Object)this.getOperationResult())) {
            this.getOperationResult().setSuccessPkIds(this.successPkIds);
        }
    }
}
