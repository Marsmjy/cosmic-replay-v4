/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgStructRepository
 *  kd.hr.hbp.common.model.AuthorizedOrgResultWithSub
 *  kd.hr.hbp.common.model.OrgSubInfo
 */
package kd.hr.haos.business.util;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgStructRepository;
import kd.hr.hbp.common.model.AuthorizedOrgResultWithSub;
import kd.hr.hbp.common.model.OrgSubInfo;

public class OrgPermHelper {
    private static final String HR_ORG_VIEW_TYPE = "21";
    private static final String HOMS_APP = "homs";

    public static HasPermOrgResult getHRPermOrg() {
        return OrgPermHelper.getHRPermOrg(RequestContext.get().getCurrUserId(), HOMS_APP, "haos_adminorgdetail", "47150e89000000ac");
    }

    public static HasPermOrgResult getHRPermOrg(String entityName) {
        return OrgPermHelper.getHRPermOrg(RequestContext.get().getCurrUserId(), HOMS_APP, entityName, "47150e89000000ac");
    }

    public static HasPermOrgResult getHRPermOrg(boolean mustQuery) {
        return PermissionServiceHelper.getAllPermOrgs((long)RequestContext.get().getCurrUserId(), (String)HR_ORG_VIEW_TYPE, (String)HOMS_APP, (String)"haos_adminorgdetail", (String)"47150e89000000ac", (boolean)mustQuery);
    }

    public static HasPermOrgResult getHRPermOrg(long userId, String appId, String entityNumber, String permId) {
        return PermissionServiceHelper.getAllPermOrgs((long)userId, (String)HR_ORG_VIEW_TYPE, (String)appId, (String)entityNumber, (String)permId, (boolean)false);
    }

    public static QFilter getHrPermFilter(String entityNumber, String property) {
        HasPermOrgResult permOrg = OrgPermHelper.getHRPermOrg(RequestContext.get().getCurrUserId(), HOMS_APP, entityNumber, "47150e89000000ac");
        QFilter filter = !permOrg.hasAllOrgPerm() ? new QFilter(property, "in", (Object)permOrg.getHasPermOrgs()) : new QFilter("1", "=", (Object)1);
        return filter;
    }

    public static void resetPermOrgResultWithSubWithDate(AuthorizedOrgResultWithSub permOrgResultWithSub, Date date, long structProjectId) {
        Map<Long, OrgSubInfo> adBoVsSubInfo = permOrgResultWithSub.getHasPermOrgsWithSub().stream().collect(Collectors.toMap(OrgSubInfo::getOrgId, subInfo -> subInfo, (value1, value2) -> value1));
        DynamicObject[] structDys = AdminOrgStructRepository.getInstance().queryHisByOrgId("adminorg.id, structlongnumber", adBoVsSubInfo.keySet(), Long.valueOf(structProjectId), date);
        Arrays.stream(structDys).forEach(structDy -> {
            long adminOrgBo = structDy.getLong("adminorg.id");
            String SLN = structDy.getString("structlongnumber");
            OrgSubInfo orgSubInfo = (OrgSubInfo)adBoVsSubInfo.get(adminOrgBo);
            Optional.ofNullable(orgSubInfo).ifPresent(subInfo -> subInfo.setLongStructNumber(SLN));
        });
    }
}
