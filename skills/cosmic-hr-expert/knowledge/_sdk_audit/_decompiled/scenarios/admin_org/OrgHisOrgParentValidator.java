/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.application.service.ServiceFactory
 *  kd.hr.haos.business.application.service.init.IOrgHisInitValidService
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisAbstractValidator
 */
package kd.hr.haos.opplugin.web.adminorg.init.validate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.application.service.ServiceFactory;
import kd.hr.haos.business.application.service.init.IOrgHisInitValidService;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisAbstractValidator;

public class OrgHisOrgParentValidator
extends OrgHisAbstractValidator {
    private IOrgHisInitValidService validService = (IOrgHisInitValidService)ServiceFactory.getService(IOrgHisInitValidService.class);

    protected void bizValidate(List<DynamicObject> hisOrgDynList) {
        Set parentBoIdSet = hisOrgDynList.stream().map(dyn -> dyn.getLong("parentorg.id")).collect(Collectors.toSet());
        DynamicObject[] parentBoDynArr = this.adminOrgServiceHelper.queryOriginalArray("id,number,boid,firstbsed", new QFilter[]{new QFilter("boid", "in", parentBoIdSet), new QFilter("iscurrentversion", "=", (Object)"1")});
        Map<Long, DynamicObject> parentBoMap = Arrays.stream(parentBoDynArr).collect(Collectors.toMap(dyn -> dyn.getLong("boid"), dyn -> dyn, (d1, d2) -> d1));
        this.validService.validOrgParent(hisOrgDynList, parentBoMap, this.validErrMap);
        this.validService.validFirstVersionParent(hisOrgDynList, parentBoMap, this.validErrMap);
    }
}
