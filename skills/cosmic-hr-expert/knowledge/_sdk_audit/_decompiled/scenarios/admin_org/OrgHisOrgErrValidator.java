/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisAbstractValidator
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.haos.opplugin.web.adminorg.init.validate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisAbstractValidator;
import kd.hr.hbp.common.util.HRStringUtils;

public class OrgHisOrgErrValidator
extends OrgHisAbstractValidator {
    protected void bizValidate(List<DynamicObject> hisOrgDynList) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        Set validErrBoIdSet = hisOrgDynList.stream().filter(dyn -> this.validErrMap.containsKey(dyn.getLong("id")) && HRStringUtils.isNotEmpty((String)((StringBuilder)this.validErrMap.get(dyn.getLong("id"))).toString())).map(dyn -> dyn.getLong("boid")).collect(Collectors.toSet());
        Set allFailIdSet = hisOrgDynList.stream().filter(dyn -> validErrBoIdSet.contains(dyn.getLong("boid"))).map(dyn -> dyn.getLong("id")).collect(Collectors.toSet());
        this.addHisOrgErrMsg(this.validErrMap, allFailIdSet);
        this.resetData();
    }
}
