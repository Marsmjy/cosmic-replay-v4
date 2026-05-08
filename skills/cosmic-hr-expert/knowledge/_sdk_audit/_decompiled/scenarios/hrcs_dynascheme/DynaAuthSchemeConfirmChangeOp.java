/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.exception.KDBizException
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.perm.dyna.DynaRoleDetailServiceHelper
 *  kd.hr.hrcs.bussiness.service.perm.dyna.DynaSchemeRoleAssignServiceHelper
 */
package kd.hr.hrcs.opplugin.web.perm.dyna;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.exception.KDBizException;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.perm.dyna.DynaRoleDetailServiceHelper;
import kd.hr.hrcs.bussiness.service.perm.dyna.DynaSchemeRoleAssignServiceHelper;

public final class DynaAuthSchemeConfirmChangeOp
extends HRDataBaseOp {
    public void endOperationTransaction(EndOperationTransactionArgs args) {
        super.endOperationTransaction(args);
        DynamicObject[] dyoArr = args.getDataEntities();
        String isListConfirmChange = this.getOption().getVariableValue("list_op", "");
        if (isListConfirmChange.equals("1")) {
            throw new KDBizException("not support");
        }
        DynamicObject dyo = dyoArr[0];
        long boId = dyo.getLong("boid");
        long bgVid = dyo.getLong("id");
        DynamicObjectCollection versionRuleColl = dyo.getDynamicObjectCollection("roleentry");
        DynaSchemeRoleAssignServiceHelper.saveRoleEntry((long)bgVid, (DynamicObjectCollection)versionRuleColl);
        DynamicObjectCollection boRoleEntryColl = DynaRoleDetailServiceHelper.genVersionRoleEntryColl((long)boId, (DynamicObjectCollection)versionRuleColl);
        DynaSchemeRoleAssignServiceHelper.saveRoleEntry((long)boId, (DynamicObjectCollection)boRoleEntryColl);
    }
}
