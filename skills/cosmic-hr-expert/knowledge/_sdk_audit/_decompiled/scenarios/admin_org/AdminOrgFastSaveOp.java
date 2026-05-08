/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.hr.haos.business.domain.org.abs.IBatchOrgOpService
 *  kd.hr.haos.business.domain.org.service.BatchAdminOrgNewOpService
 *  kd.hr.haos.opplugin.web.orgfast.AbsOrgBaseOp
 */
package kd.hr.haos.opplugin.web.orgfast;

import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.haos.business.domain.org.abs.IBatchOrgOpService;
import kd.hr.haos.business.domain.org.service.BatchAdminOrgNewOpService;
import kd.hr.haos.opplugin.web.orgfast.AbsOrgBaseOp;

public class AdminOrgFastSaveOp
extends AbsOrgBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
    }

    protected IBatchOrgOpService initBatchOrgOpService(DynamicObject[] data, OperateOption operateOption) {
        return new BatchAdminOrgNewOpService(data, false, operateOption);
    }
}
