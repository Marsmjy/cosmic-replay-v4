/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hrmp.lcs.business.basedata.CostBasaDataHelper
 */
package kd.hrmp.lcs.formplugin.web.basedata;

import java.util.List;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hrmp.lcs.business.basedata.CostBasaDataHelper;

public class CostBizObjBillList
extends HRDataBaseList {
    public void afterDoOperation(AfterDoOperationEventArgs args) {
        String key;
        super.afterDoOperation(args);
        switch (key = args.getOperateKey()) {
            case "donothing_allow": {
                this.unSubmitAllotBill(args, Boolean.TRUE);
                break;
            }
            case "donothing_ban": {
                this.unSubmitAllotBill(args, Boolean.FALSE);
                break;
            }
        }
    }

    private void unSubmitAllotBill(AfterDoOperationEventArgs args, Boolean aFalse) {
        OperationResult unSubmitOperateResult = args.getOperationResult();
        List unSubmitSuccessPkIds = unSubmitOperateResult.getSuccessPkIds();
        CostBasaDataHelper.updateIsallowedsplit((List)unSubmitSuccessPkIds, (Boolean)aFalse);
        this.getView().invokeOperation("refresh");
    }
}
