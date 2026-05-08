/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.hr.haos.business.application.service.ServiceFactory
 *  kd.hr.haos.business.application.service.init.IOrgHisInitValidService
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisAbstractValidator
 */
package kd.hr.haos.opplugin.web.adminorg.init.validate;

import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.haos.business.application.service.ServiceFactory;
import kd.hr.haos.business.application.service.init.IOrgHisInitValidService;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisAbstractValidator;

public class OrgHisFirstEffDateConsistencyValidator
extends OrgHisAbstractValidator {
    private IOrgHisInitValidService validService = (IOrgHisInitValidService)ServiceFactory.getService(IOrgHisInitValidService.class);

    protected void bizValidate(List<DynamicObject> hisOrgDynList) {
        this.validService.validFirstEffDateConsistency(hisOrgDynList, this.dbEffBizBoDynMap, this.validErrMap);
    }
}
