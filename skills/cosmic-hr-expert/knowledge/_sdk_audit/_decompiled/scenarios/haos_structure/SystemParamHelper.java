/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.AppInfo
 *  kd.bos.entity.AppMetadataCache
 *  kd.bos.entity.param.AppParam
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.parameter.SystemParamServiceHelper
 */
package kd.hr.haos.business.domain.common.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kd.bos.entity.AppInfo;
import kd.bos.entity.AppMetadataCache;
import kd.bos.entity.param.AppParam;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.parameter.SystemParamServiceHelper;

public class SystemParamHelper {
    private static final String CREATOR_HAS_PERMISSION = "creatorhaspermission";
    private static final String STAFF_PAST_MONTH_MODIFY = "staffpastmonthmodify";
    private static final String ALLOW_ON_POS_DISABLE = "allowonposdisable";
    private static final String CHOOSE_CHARGE_PERSON = "choosechargeperson";

    public static boolean getCreatorHasPermission(Long buOrgId) {
        AppInfo appInfo = AppMetadataCache.getAppInfo((String)"homs");
        String appId = appInfo != null ? appInfo.getId() : null;
        AppParam appParam = new AppParam(appId, buOrgId);
        Object parameter = SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam, (String)CREATOR_HAS_PERMISSION);
        return parameter == null || (Boolean)parameter != false;
    }

    public static Map<String, Map<String, Object>> getBatchParameter(List<Long> buOrgIdList) {
        AppInfo appInfo = AppMetadataCache.getAppInfo((String)"homs");
        AppParam appParam = new AppParam();
        if (appInfo != null) {
            appParam.setAppId(appInfo.getId());
        }
        return SystemParamServiceHelper.loadBatchAppParameterByOrgFromCache((AppParam)appParam, buOrgIdList);
    }

    public static Map<Long, Boolean> getBatchOrgParameter(Set<Long> buOrgIdSet, String key) {
        Map<String, Map<String, Object>> parameterMap = SystemParamHelper.getBatchParameter(new ArrayList<Long>(buOrgIdSet));
        HashMap<Long, Boolean> resultMap = new HashMap<Long, Boolean>(buOrgIdSet.size());
        for (Long buOrgId : buOrgIdSet) {
            if (CollectionUtils.isEmpty(parameterMap)) {
                return resultMap;
            }
            Map<String, Object> map = parameterMap.get(String.valueOf(buOrgId));
            Boolean flag = CollectionUtils.isEmpty(map) || map.get(key) == null ? Boolean.FALSE : (Boolean)map.get(key);
            resultMap.put(buOrgId, flag);
        }
        return resultMap;
    }

    public static Boolean getStaffPastMonthModifyParameter(Long buOrgId) {
        Map<Long, Boolean> batchOrgParameter = SystemParamHelper.getBatchOrgParameter(Collections.singleton(buOrgId), STAFF_PAST_MONTH_MODIFY);
        return batchOrgParameter.getOrDefault(buOrgId, Boolean.FALSE);
    }

    public static boolean getAllowPersonOnPosDisableParameter(Long buOrgId) {
        AppInfo appInfo = AppMetadataCache.getAppInfo((String)"homs");
        String appId = appInfo != null ? appInfo.getId() : null;
        AppParam appParam = new AppParam(appId, buOrgId);
        Object parameter = SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam, (String)ALLOW_ON_POS_DISABLE);
        return parameter != null && (Boolean)parameter != false;
    }

    public static boolean getChooseChargePersonParameter(Long buOrgId) {
        AppInfo appInfo = AppMetadataCache.getAppInfo((String)"homs");
        String appId = appInfo != null ? appInfo.getId() : null;
        AppParam appParam = new AppParam(appId, buOrgId);
        Object parameter = SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam, (String)CHOOSE_CHARGE_PERSON);
        return parameter != null && (Boolean)parameter != false;
    }

    public static boolean getIgnoreOrgNameUniqueParameter() {
        Object ignoreOrgNameUniqueParameter = SystemParamServiceHelper.getBillParameter((String)"bos_adminorg", (String)"ignoreorgnameunique");
        return ignoreOrgNameUniqueParameter != null && ignoreOrgNameUniqueParameter.equals(true);
    }
}
