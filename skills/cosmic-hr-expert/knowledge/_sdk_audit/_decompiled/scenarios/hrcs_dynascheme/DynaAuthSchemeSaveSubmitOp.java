/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.perm.dyna.DynaSchemeRoleAssignServiceHelper
 */
package kd.hr.hrcs.opplugin.web.perm.dyna;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.perm.dyna.DynaSchemeRoleAssignServiceHelper;

public final class DynaAuthSchemeSaveSubmitOp
extends HRDataBaseOp {
    public void endOperationTransaction(EndOperationTransactionArgs args) {
        super.endOperationTransaction(args);
        String isListOp = this.getOption().getVariableValue("list_op", "");
        if ("1".equals(isListOp)) {
            return;
        }
        DynamicObject[] dyoArr = args.getDataEntities();
        DynamicObject dyo = dyoArr[0];
        long schemeId = dyo.getLong("id");
        DynamicObjectCollection roleEntryView = dyo.getDynamicObjectCollection("roleentry");
        DynaSchemeRoleAssignServiceHelper.saveRoleEntry((long)schemeId, (DynamicObjectCollection)roleEntryView);
    }
}
