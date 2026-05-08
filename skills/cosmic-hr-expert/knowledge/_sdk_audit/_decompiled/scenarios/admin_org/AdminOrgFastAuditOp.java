/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.hr.haos.business.domain.org.abs.IBatchOrgOpService
 *  kd.hr.haos.business.domain.org.service.BatchAdminOrgAuditOpService
 *  kd.hr.haos.opplugin.web.orgfast.AbsOrgBaseOp
 */
package kd.hr.haos.opplugin.web.orgfast;

import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.haos.business.domain.org.abs.IBatchOrgOpService;
import kd.hr.haos.business.domain.org.service.BatchAdminOrgAuditOpService;
import kd.hr.haos.opplugin.web.orgfast.AbsOrgBaseOp;

public class AdminOrgFastAuditOp
extends AbsOrgBaseOp {
    protected IBatchOrgOpService initBatchOrgOpService(DynamicObject[] data, OperateOption operateOption) {
        return new BatchAdminOrgAuditOpService(data, operateOption);
    }
}
