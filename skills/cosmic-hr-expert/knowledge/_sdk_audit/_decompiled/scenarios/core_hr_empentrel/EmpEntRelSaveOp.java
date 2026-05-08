/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hrmp.hrpi.opplugin.web.employee.attach;

import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class EmpEntRelSaveOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(EmpEntRelSaveOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("laborrelstatus");
        fieldKeys.add("ishired");
        fieldKeys.add("istrial");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities;
        for (DynamicObject dataEntity : dataEntities = e.getDataEntities()) {
            LOGGER.info("laborrelstatus.id|{},laborrelstatus.ishired|{},laborrelstatus.istrial|{}", new Object[]{dataEntity.getLong("laborrelstatus.id"), dataEntity.getBoolean("laborrelstatus.ishired"), dataEntity.getBoolean("laborrelstatus.istrial")});
            dataEntity.set("ishired", (Object)dataEntity.getBoolean("laborrelstatus.ishired"));
            dataEntity.set("istrial", (Object)dataEntity.getBoolean("laborrelstatus.istrial"));
        }
    }
}
