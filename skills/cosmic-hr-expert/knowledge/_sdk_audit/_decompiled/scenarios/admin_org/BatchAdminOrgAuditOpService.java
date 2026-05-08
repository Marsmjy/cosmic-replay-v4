/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.hr.haos.business.domain.org.setter.OrgSetter
 */
package kd.hr.haos.business.domain.org.service;

import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.haos.business.domain.org.service.BatchAdminOrgNewOpService;
import kd.hr.haos.business.domain.org.setter.OrgSetter;

public class BatchAdminOrgAuditOpService
extends BatchAdminOrgNewOpService {
    public BatchAdminOrgAuditOpService(DynamicObject[] data, OperateOption operateOption) {
        super(data, false, operateOption);
    }

    @Override
    protected void execute() {
        super.execute();
    }

    @Override
    protected OrgSetter getOrgSetter(DynamicObject[] data, OperateOption operateOption) {
        return new OrgSetter();
    }
}
