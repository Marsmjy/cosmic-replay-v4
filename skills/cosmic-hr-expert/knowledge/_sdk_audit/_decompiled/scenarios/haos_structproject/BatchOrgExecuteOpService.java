/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.hr.haos.business.domain.org.abs.IBatchOrgOpService
 */
package kd.hr.haos.business.domain.org.service;

import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.hr.haos.business.domain.org.abs.IBatchOrgOpService;

public class BatchOrgExecuteOpService {
    private final IBatchOrgOpService batchOrgOpService;

    public BatchOrgExecuteOpService(IBatchOrgOpService batchOrgOpService) {
        this.batchOrgOpService = batchOrgOpService;
    }

    public void execute() {
        this.batchOrgOpService.doBeforeTransDoOp();
        try (TXHandle required = TX.required();){
            boolean success = false;
            try {
                this.batchOrgOpService.doChangeOp();
                this.batchOrgOpService.doEndDoChangeOp();
                success = true;
            }
            catch (Throwable throwable) {
                required.markRollback();
                throw throwable;
            }
            finally {
                if (success) {
                    this.batchOrgOpService.doAfterTransDoOp();
                }
            }
        }
    }
}
