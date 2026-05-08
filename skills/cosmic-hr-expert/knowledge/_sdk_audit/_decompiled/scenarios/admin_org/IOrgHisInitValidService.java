/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.orm.util.CollectionUtils
 */
package kd.hr.haos.business.application.service.init;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.orm.util.CollectionUtils;

public interface IOrgHisInitValidService {
    public void validEffDateContinuity(List<DynamicObject> var1, LinkedHashMap<Long, StringBuilder> var2);

    public void validEndDate(List<DynamicObject> var1, LinkedHashMap<Long, StringBuilder> var2);

    public void validEndDateRange(List<DynamicObject> var1, Map<Long, DynamicObject> var2, Map<Long, List<DynamicObject>> var3, LinkedHashMap<Long, StringBuilder> var4);

    public void validEffDateLegitimacy(List<DynamicObject> var1, Map<Long, DynamicObject> var2, LinkedHashMap<Long, StringBuilder> var3);

    public void validFirstEffDateConsistency(List<DynamicObject> var1, Map<Long, DynamicObject> var2, LinkedHashMap<Long, StringBuilder> var3);

    public void validOrgParent(List<DynamicObject> var1, Map<Long, DynamicObject> var2, LinkedHashMap<Long, StringBuilder> var3);

    public void validFirstVersionParent(List<DynamicObject> var1, Map<Long, DynamicObject> var2, LinkedHashMap<Long, StringBuilder> var3);

    public void validCurrVersonParent(List<DynamicObject> var1, Map<Long, DynamicObject> var2, LinkedHashMap<Long, StringBuilder> var3);

    public void validBelongCompany(List<DynamicObject> var1, LinkedHashMap<Long, StringBuilder> var2);

    public void validStartDateLessEndDate(List<DynamicObject> var1, LinkedHashMap<Long, StringBuilder> var2);

    public void verifyHistoryMigrated(List<DynamicObject> var1, Map<Long, List<DynamicObject>> var2, LinkedHashMap<Long, StringBuilder> var3);

    default public void initHisOrgValidErrMap(List<DynamicObject> hisOrgDynList, LinkedHashMap<Long, StringBuilder> validErrMap) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        if (!ObjectUtils.isEmpty(validErrMap)) {
            return;
        }
        for (DynamicObject dyn : hisOrgDynList) {
            validErrMap.putIfAbsent(dyn.getLong("id"), new StringBuilder());
        }
    }
}
