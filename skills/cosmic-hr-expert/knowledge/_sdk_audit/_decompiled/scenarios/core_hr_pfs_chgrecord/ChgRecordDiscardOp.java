/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.sdk.hr.hpfs.utils.ChgUtils
 */
package kd.hr.hpfs.opplugin.op.basedata;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.sdk.hr.hpfs.utils.ChgUtils;

public final class ChgRecordDiscardOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        args.getFieldKeys().add("datastatus");
        args.getFieldKeys().add("chgentity");
        args.getFieldKeys().add("dataid");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        for (DynamicObject item : args.getDataEntities()) {
            item.set("datastatus", (Object)"4");
        }
        ChgUtils.deletePersonFlow((DynamicObject[])args.getDataEntities());
    }
}
