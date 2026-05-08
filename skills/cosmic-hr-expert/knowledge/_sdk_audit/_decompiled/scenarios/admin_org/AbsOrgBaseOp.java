/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.hr.haos.business.domain.org.abs.IBatchOrgOpService
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.orgfast;

import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.hr.haos.business.domain.org.abs.IBatchOrgOpService;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public abstract class AbsOrgBaseOp
extends HRDataBaseOp {
    protected IBatchOrgOpService opService;

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
        this.opService = this.initBatchOrgOpService(e.getDataEntities(), this.operateOption);
        this.opService.beforeTransDoOp();
    }

    protected abstract IBatchOrgOpService initBatchOrgOpService(DynamicObject[] var1, OperateOption var2);

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        if (this.opService != null && !this.opService.isAsync()) {
            this.opService.doChangeOp();
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        super.endOperationTransaction(e);
        if (this.opService != null && !this.opService.isAsync()) {
            this.opService.endDoChangeOp();
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        if (this.opService != null) {
            if (!this.opService.isAsync()) {
                this.opService.afterTransDoOp();
            } else {
                this.opService.runAsync();
            }
        }
    }
}
