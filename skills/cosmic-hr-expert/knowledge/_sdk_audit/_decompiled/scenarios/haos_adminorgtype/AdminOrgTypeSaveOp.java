/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.hr.haos.common.constants.AdminOrgTypeStdEnum
 *  kd.hr.hbp.common.cache.HRAppCache
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.hr.haos.common.constants.AdminOrgTypeStdEnum;
import kd.hr.hbp.common.cache.HRAppCache;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class AdminOrgTypeSaveOp
extends HRDataBaseOp {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        String appCacheKey = "IMPORT_ADMIN_TYPE" + RequestContext.get().getTraceId();
        try {
            if (HRStringUtils.isEmpty((String)((String)this.getOption().getVariables().get("importtype"))) ) {
                return;
            }
            Map appCache = (Map)HRAppCache.get((String)"homs").get(appCacheKey, Map.class);
            List orgPatternIds = Arrays.stream(AdminOrgTypeStdEnum.values()).map(std -> std.getOrgPatternId()).collect(Collectors.toList());
            DynamicObject[] orgPatternDynamicObjectArray = BusinessDataServiceHelper.load((String)"bos_org_pattern", (String)"id", (QFilter[])new QFilter[]{new QFilter("id", "in", orgPatternIds)});
            Map<Long, DynamicObject> orgPatternMap = Arrays.stream(orgPatternDynamicObjectArray).collect(Collectors.toMap(pattern -> pattern.getLong("id"), pattern -> pattern));
            DynamicObject[] dataEntities = e.getDataEntities();
            List adminOrgTypesNumbers = Arrays.stream(dataEntities).map(dyn -> dyn.getString("number")).collect(Collectors.toList());
            DynamicObject[] adminTypeDynamicFromDb = BusinessDataServiceHelper.load((String)"haos_adminorgtype", (String)"id,number,adminorgtypestd", (QFilter[])new QFilter[]{new QFilter("number", "in", adminOrgTypesNumbers)});
            Map<String, DynamicObject> number2Dyn = Arrays.stream(adminTypeDynamicFromDb).collect(Collectors.toMap(dyn -> dyn.getString("number"), dyn -> dyn));
            for (DynamicObject dynamicObject : dataEntities) {
                DynamicObject adminorgtypestd = dynamicObject.getDynamicObject("adminorgtypestd");
                String number = dynamicObject.getString("number");
                DynamicObject originalDynamicObject = number2Dyn.get(number);
                long fromExcel = adminorgtypestd.getLong("id");
                if ((originalDynamicObject == null || originalDynamicObject.getLong("adminorgtypestd.id") == fromExcel || !(appCache.get(number) instanceof Map) || ((Map)appCache.get(number)).get("orgpattern") != null) && dynamicObject.getDynamicObject("orgpattern") != null) continue;
                dynamicObject.set("orgpattern", (Object)orgPatternMap.get(AdminOrgTypeStdEnum.getOrgPatternIdById((long)adminorgtypestd.getLong("id"))));
            }
        }
        finally {
            HRAppCache.get((String)"homs").remove(appCacheKey);
        }
    }
}
