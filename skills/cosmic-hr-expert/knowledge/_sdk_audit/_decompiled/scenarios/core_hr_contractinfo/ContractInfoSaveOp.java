/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.infrastructure.utils.ContractUtil
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.infrastructure.utils.ContractUtil;

public final class ContractInfoSaveOp
extends HRDataBaseOp {
    private static final long NO_FIXED_PERIOD_TYPE_ID = 1383941813181670400L;

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("assignment");
        fieldKeys.add("startdate");
        fieldKeys.add("enddate");
        fieldKeys.add("planenddate");
        fieldKeys.add("contractstatus");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities;
        for (DynamicObject dataEntity : dataEntities = e.getDataEntities()) {
            long periodTypeId;
            if (HRStringUtils.isEmpty((String)dataEntity.getString("contractstatus"))) {
                String contractStatus = ContractUtil.calcContractStatus((DynamicObject)dataEntity);
                dataEntity.set("contractstatus", (Object)contractStatus);
            }
            if ((periodTypeId = dataEntity.getLong("periodtype.id")) != 1383941813181670400L) continue;
            dataEntity.set("planenddate", null);
            dataEntity.set("periodunit", (Object)"0");
        }
    }
}
