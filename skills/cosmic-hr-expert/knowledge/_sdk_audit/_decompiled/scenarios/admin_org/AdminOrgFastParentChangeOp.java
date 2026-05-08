/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.hr.haos.business.domain.org.abs.IBatchOrgOpService
 *  kd.hr.haos.business.domain.org.async.AsyncEffectingMultiCaster
 *  kd.hr.haos.business.domain.org.service.BatchAdminOrgChangeParentOpService
 *  kd.hr.haos.opplugin.web.orgfast.AbsOrgBaseOp
 */
package kd.hr.haos.opplugin.web.orgfast;

import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.haos.business.domain.org.abs.IBatchOrgOpService;
import kd.hr.haos.business.domain.org.async.AsyncEffectingMultiCaster;
import kd.hr.haos.business.domain.org.service.BatchAdminOrgChangeParentOpService;
import kd.hr.haos.opplugin.web.orgfast.AbsOrgBaseOp;

public class AdminOrgFastParentChangeOp
extends AbsOrgBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
    }

    protected IBatchOrgOpService initBatchOrgOpService(DynamicObject[] data, OperateOption operateOption) {
        if (this.operateOption.containsVariable("async_save") && "true".equals(this.operateOption.getVariableValue("async_save"))) {
            return new BatchAdminOrgChangeParentOpService(data, new AsyncEffectingMultiCaster(data), false, operateOption);
        }
        return new BatchAdminOrgChangeParentOpService(data, false, operateOption);
    }
}
