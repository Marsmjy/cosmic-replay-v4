/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hpfs.opplugin.validators.FileMapManagerSaveValidator
 */
package kd.hr.hpfs.opplugin.fieldmap;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hpfs.opplugin.validators.FileMapManagerSaveValidator;

public final class FileMapManagerSaveOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new FileMapManagerSaveValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
    }
}
