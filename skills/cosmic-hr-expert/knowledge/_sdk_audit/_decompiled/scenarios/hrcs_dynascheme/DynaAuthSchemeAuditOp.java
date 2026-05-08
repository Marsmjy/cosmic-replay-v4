/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.perm.dyna.DynaRoleDetailServiceHelper
 *  kd.hr.hrcs.bussiness.service.perm.dyna.DynaSchemeRoleAssignServiceHelper
 */
package kd.hr.hrcs.opplugin.web.perm.dyna;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.perm.dyna.DynaRoleDetailServiceHelper;
import kd.hr.hrcs.bussiness.service.perm.dyna.DynaSchemeRoleAssignServiceHelper;

public final class DynaAuthSchemeAuditOp
extends HRDataBaseOp {
    public void endOperationTransaction(EndOperationTransactionArgs args) {
        super.endOperationTransaction(args);
        DynamicObject[] dyoArr = args.getDataEntities();
        String isListOp = this.getOption().getVariableValue("list_op", "");
        if ("1".equals(isListOp)) {
            for (DynamicObject dyo : dyoArr) {
                long boId = dyo.getLong("id");
                DynamicObjectCollection roleEntryColl = DynaRoleDetailServiceHelper.loadRoleCustomInfo((long)boId);
                long sourceVid = dyo.getLong("sourcevid");
                DynamicObjectCollection versionRoleEntryColl = DynaRoleDetailServiceHelper.genVersionRoleEntryColl((long)sourceVid, (DynamicObjectCollection)roleEntryColl);
                DynaSchemeRoleAssignServiceHelper.saveRoleEntry((long)sourceVid, (DynamicObjectCollection)versionRoleEntryColl);
            }
        } else {
            DynamicObject dyo = dyoArr[0];
            long sourceVid = dyo.getLong("sourcevid");
            DynamicObjectCollection roleEntryColl = dyo.getDynamicObjectCollection("roleentry");
            DynamicObjectCollection versionRoleEntryColl = DynaRoleDetailServiceHelper.genVersionRoleEntryColl((long)sourceVid, (DynamicObjectCollection)roleEntryColl);
            DynaSchemeRoleAssignServiceHelper.saveRoleEntry((long)sourceVid, (DynamicObjectCollection)versionRoleEntryColl);
        }
    }
}
