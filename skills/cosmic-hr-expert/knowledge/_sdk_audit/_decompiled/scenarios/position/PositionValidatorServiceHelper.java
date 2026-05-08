/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.login.utils.DateUtils
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.hr.hbp.common.model.AuthorizedOrgResult
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hrmp.hbpm.opplugin.web.position.validate;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.login.utils.DateUtils;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.api.HasPermOrgResult;
import kd.hr.hbp.common.model.AuthorizedOrgResult;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;

public class PositionValidatorServiceHelper {
    public static final Log LOG = LogFactory.getLog(PositionValidatorServiceHelper.class);

    public String validateParentPositionEnable(DynamicObject parentPosition, String name) {
        String msg = null;
        if (HRObjectUtils.isEmpty((Object)parentPosition)) {
            return null;
        }
        if (!"1".equals(parentPosition.getString("enable"))) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\uff1a\u4e0a\u7ea7\u5c97\u4f4d\u5df2\u7981\u7528\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_1", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), name);
        }
        return msg;
    }

    public String validateFirstBsed(Date bsed, Date firstBsed) {
        String msg = null;
        if (bsed.before(firstBsed)) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u751f\u6548\u65e5\u671f\u5e94\u665a\u4e8e\u6216\u7b49\u4e8e\u5c97\u4f4d\u6700\u65e9\u751f\u6548\u65e5\u671f\uff0c\u6700\u65e9\u751f\u6548\u65e5\u671f\u201c%s\u201d\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_28", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), DateUtils.formatDate((Date)firstBsed, (Object[])new Object[]{"yyyy-MM-dd"}));
        }
        return msg;
    }

    public String validateBSedAndAdminOrgFirstBsed(Date bsed, DynamicObject adminOrg) {
        String msg = null;
        if (ObjectUtils.isEmpty((Object)adminOrg)) {
            return null;
        }
        Date adminOrgDate = adminOrg.getDate("firstbsed");
        if (bsed == null || adminOrgDate == null) {
            LOG.error("bsed is null or adminOrg bsed is null");
            return null;
        }
        if (HRDateTimeUtils.truncateDate((Date)bsed).before(HRDateTimeUtils.truncateDate((Date)adminOrgDate))) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u751f\u6548\u65e5\u671f\u5e94\u665a\u4e8e\u6216\u7b49\u4e8e\u6240\u5728\u7ec4\u7ec7\u7684\u6700\u65e9\u751f\u6548\u65e5\u671f\u201c%s\u201d\u3002", (String)"PositionValidatorServiceHelper_9", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), HRDateTimeUtils.format((Date)adminOrgDate, (String)"yyyy-MM-dd"));
        }
        return msg;
    }

    public String validateOrgNotNull(DynamicObject orgDy) {
        String msg = null;
        if (HRObjectUtils.isEmpty((Object)orgDy)) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u201c\u7ec4\u7ec7\u4f53\u7cfb\u7ba1\u7406\u7ec4\u7ec7\u201d\u4e0d\u80fd\u4e3a\u7a7a\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_19", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]);
        }
        return msg;
    }

    public String validateOrgHasPerm(DynamicObject orgDy, HasPermOrgResult posHasPermOrg) {
        String msg = null;
        if (posHasPermOrg.hasAllOrgPerm()) {
            return null;
        }
        if (HRObjectUtils.isEmpty((Object)orgDy)) {
            return null;
        }
        List hasPermOrgs = posHasPermOrg.getHasPermOrgs();
        if (CollectionUtils.isEmpty((Collection)hasPermOrgs) || !hasPermOrgs.contains(orgDy.getLong("id"))) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u4e3b\u7ba1\u8d23\u4efb\u5355\u4f4d\u4e0d\u5728\u6743\u9650\u8303\u56f4\u5185\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_20", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]);
        }
        return msg;
    }

    public String validateBSedLessAssignDate(Date bSed, Date assignDate) {
        String msg = null;
        if (HRDateTimeUtils.truncateDate((Date)bSed).after(HRDateTimeUtils.truncateDate((Date)assignDate))) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u751f\u6548\u65e5\u671f\u5e94\u65e9\u4e8e\u6216\u7b49\u4e8e\u5f53\u524d\u65e5\u671f\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_8", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]);
        }
        return msg;
    }

    public String validateBSed(Date bSed) {
        String msg = null;
        if (bSed == null) {
            msg = ResManager.loadKDString((String)"\u751f\u6548\u65e5\u671f\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"PositionValidatorServiceHelper_37", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]);
        }
        return msg;
    }

    public String validateMustForReportType(DynamicObject reportType) {
        String msg = null;
        if (ObjectUtils.isEmpty((Object)reportType)) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u8bf7\u6309\u8981\u6c42\u586b\u5199\u201c\u534f\u4f5c\u7c7b\u578b\u201d\u3002", (String)"PositionValidatorServiceHelper_3", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]);
        }
        return msg;
    }

    public String validateMustForTargetPos(DynamicObject targetPos) {
        String msg = null;
        if (ObjectUtils.isEmpty((Object)targetPos)) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u8bf7\u6309\u8981\u6c42\u586b\u5199\u201c\u534f\u4f5c\u5c97\u4f4d\u201d\u3002", (String)"PositionValidatorServiceHelper_4", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]);
        }
        return msg;
    }

    public String validateReportTypeUnique(List<Long> reportTypeIds, DynamicObject reportType) {
        String msg = null;
        if (HRObjectUtils.isEmpty((Object)reportType)) {
            return null;
        }
        if (reportTypeIds.contains(reportType.getLong("id"))) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u534f\u4f5c\u7c7b\u578b\u201c%s\u201d\u6709\u591a\u6761\u4e00\u6837\u7684\u6570\u636e\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_18", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), reportType.getString("name"));
        }
        reportTypeIds.add(reportType.getLong("id"));
        return msg;
    }

    public String validateCountryAndCity(DynamicObject country, DynamicObject city) {
        String msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u56fd\u5bb6/\u5730\u533a\u4e0e\u6240\u5728\u57ce\u5e02\u4e0d\u5339\u914d\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_11", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]);
        if (HRObjectUtils.isEmpty((Object)country) && HRObjectUtils.isEmpty((Object)city)) {
            return null;
        }
        if (HRObjectUtils.isEmpty((Object)country) && !HRObjectUtils.isEmpty((Object)city)) {
            return msg;
        }
        if (!HRObjectUtils.isEmpty((Object)country) && !HRObjectUtils.isEmpty((Object)city) && country.getLong("id") != city.getLong("country.id")) {
            return msg;
        }
        return null;
    }

    public String validateEstablishLessParentFirstBsed(Date bsed, DynamicObject parent) {
        String msg = null;
        if (HRObjectUtils.isEmpty((Object)parent)) {
            return null;
        }
        Date date = parent.getDate("firstbsed");
        if (bsed == null || date == null) {
            return null;
        }
        if (bsed.before(HRDateTimeUtils.truncateDate((Date)date))) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u751f\u6548\u65e5\u671f\u5e94\u665a\u4e8e\u6216\u7b49\u4e8e\u4e0a\u7ea7\u5c97\u4f4d\u7684\u6700\u65e9\u751f\u6548\u65e5\u671f\u201c%s\u201d\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_12", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), HRDateTimeUtils.format((Date)date, (String)"yyyy-MM-dd"));
        }
        return msg;
    }

    public String validateAdminOrgIsChange(DynamicObject position, Map<String, DynamicObject> numberAndAdminOrgMap) {
        String msg = null;
        DynamicObject dynamicObject = numberAndAdminOrgMap.get(position.getString("number"));
        if (HRObjectUtils.isEmpty((Object)dynamicObject)) {
            return null;
        }
        if (position.getLong("adminorg.id") != dynamicObject.getLong("adminorg.id")) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5c97\u4f4d\u4e0d\u80fd\u53d8\u66f4\u201c\u884c\u653f\u7ec4\u7ec7\u201d\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_13", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]);
        }
        return msg;
    }

    public String validateAdminOrgHasPerm(DynamicObject adminOrg, AuthorizedOrgResult hasPermAdminOrg) {
        List perms;
        String msg = null;
        if (HRObjectUtils.isEmpty((Object)adminOrg)) {
            return null;
        }
        if (Objects.nonNull(hasPermAdminOrg) && !hasPermAdminOrg.isHasAllOrgPerm() && (CollectionUtils.isEmpty((Collection)(perms = hasPermAdminOrg.getHasPermOrgs())) || !perms.contains(adminOrg.getLong("id")))) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6ca1\u6709\u884c\u653f\u7ec4\u7ec7\u6743\u9650\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_15", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]);
        }
        return msg;
    }

    public String validateAdminOrgEnable(DynamicObject adminOrg) {
        String msg = null;
        if (HRObjectUtils.isEmpty((Object)adminOrg)) {
            return null;
        }
        if ("0".equals(adminOrg.getString("enable"))) {
            msg = ResManager.loadKDString((String)"\u884c\u653f\u7ec4\u7ec7\u5df2\u7981\u7528\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_22", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]);
        }
        return msg;
    }

    public String validateBsledChanged(Date bsled1, Date bsled2) {
        String msg = null;
        if (!HRDateTimeUtils.dayEquals((Date)HRDateTimeUtils.truncateDate((Date)bsled1), (Date)HRDateTimeUtils.truncateDate((Date)bsled2))) {
            msg = ResManager.loadKDString((String)"\u6570\u636e\u53d1\u751f\u53d8\u5316\uff0c\u8bf7\u5237\u65b0\u91cd\u8bd5\u3002", (String)"PositionValidatorServiceHelper_27", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]);
        }
        return msg;
    }

    public String validateDataEnable(DynamicObject dyn) {
        String msg = null;
        if (!HRStringUtils.equals((String)dyn.getString("enable"), (String)"1")) {
            msg = ResManager.loadKDString((String)"\u53ea\u80fd\u4fee\u8ba2\u5df2\u542f\u7528\u7684\u6570\u636e\u3002", (String)"PositionValidatorServiceHelper_25", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]);
        }
        return msg;
    }

    public String validateFirstEffdtLessOldDate(Date newFirstEffdt, Date oldFirstEffdt) {
        String msg = null;
        if (HRDateTimeUtils.truncateDate((Date)newFirstEffdt).after(HRDateTimeUtils.truncateDate((Date)oldFirstEffdt))) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6700\u65e9\u751f\u6548\u65e5\u671f\u5e94\u65e9\u4e8e\u6216\u7b49\u4e8e\u5f53\u524d\u5c97\u4f4d\u6700\u65e9\u751f\u6548\u65e5\u671f\u201c%s\u201d\u3002", (String)"PositionValidatorServiceHelper_23", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), HRDateTimeUtils.format((Date)oldFirstEffdt, (String)"yyyy-MM-dd"));
        }
        return msg;
    }

    public String validateAdminorgNotNull(DynamicObject adminorg) {
        String msg = null;
        if (HRObjectUtils.isEmpty((Object)adminorg)) {
            msg = ResManager.loadKDString((String)"\u8bf7\u6309\u8981\u6c42\u586b\u5199\u201c\u884c\u653f\u7ec4\u7ec7\u201d\u3002", (String)"PositionValidatorServiceHelper_29", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]);
        }
        return msg;
    }

    public String validateAdminorgIsvirtual(DynamicObject adminorg) {
        String msg = null;
        if ("true".equals(adminorg.getString("isvirtualorg"))) {
            msg = ResManager.loadKDString((String)"\u884c\u653f\u7ec4\u7ec7\u4e3a\u865a\u62df\u7ec4\u7ec7\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PositionValidatorServiceHelper_31", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]);
        }
        return msg;
    }

    public String validateIsCurrentversion(DynamicObject dyn) {
        String msg = null;
        Date bsed = dyn.getDate("bsed");
        Date bsled = dyn.getDate("bsled");
        Date now = HRDateTimeUtils.getNowDate();
        if (!HRDateTimeUtils.dayAfter((Date)bsed, (Date)now) && !HRDateTimeUtils.dayBefore((Date)bsled, (Date)now)) {
            msg = ResManager.loadKDString((String)"\u4e0d\u80fd\u4fee\u8ba2\u5f53\u524d\u751f\u6548\u4e2d\u7248\u672c\u6570\u636e\uff0c\u8bf7\u901a\u8fc7\u66f4\u65b0\u5bfc\u5165\u5904\u7406\u3002", (String)"PositionValidatorServiceHelper_30", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]);
        }
        return msg;
    }

    public String validateHasFutureVersion(String positionName, Date bsed) {
        return ResManager.loadKDString((String)"\u201c%1$s\u201d\u6709\u672a\u6765%2$s\u7684\u5f85\u751f\u6548\u6570\u636e\uff0c\u9700\u8981\u5c06\u672a\u6765\u751f\u6548\u7684\u6570\u636e\u64a4\u9500\u540e\u624d\u53ef\u53d8\u66f4\u3002", (String)"PositionValidatorServiceHelper_36", (String)"hrmp-hbpm-opplugin", (Object[])new Object[]{positionName, HRDateTimeUtils.format((Date)bsed, (String)"yyyy-MM-dd")});
    }

    public String validateBsedLessEffectingVersionBsed(Date bsed, Date effectingVersionBsed) {
        String msg = null;
        if (HRDateTimeUtils.truncateDate((Date)bsed).before(HRDateTimeUtils.truncateDate((Date)effectingVersionBsed))) {
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u751f\u6548\u65e5\u671f\u5e94\u665a\u4e8e\u6216\u7b49\u4e8e\u751f\u6548\u4e2d\u7248\u672c\u7684\u751f\u6548\u65e5\u671f\u201c%s\u201d\u3002", (String)"PositionValidatorServiceHelper_35", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), HRDateTimeUtils.format((Date)effectingVersionBsed, (String)"yyyy-MM-dd"));
        }
        return msg;
    }

    public String validateDataStatusFuture(DynamicObject dyn) {
        String msg = null;
        if (!HRStringUtils.equals((String)dyn.getString("datastatus"), (String)"0")) {
            msg = ResManager.loadKDString((String)"\u53ea\u80fd\u4fee\u6539\u5f85\u751f\u6548\u72b6\u6001\u6570\u636e\u3002", (String)"PositionValidatorServiceHelper_34", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]);
        }
        return msg;
    }

    public String validateAdminorgOtclassify(DynamicObject adminorg) {
        if (adminorg.getLong("otclassify.id") != 1010L) {
            return ResManager.loadKDString((String)"\u884c\u653f\u7ec4\u7ec7\u4e0d\u5b58\u5728\u3002", (String)"PositionValidatorServiceHelper_38", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]);
        }
        return null;
    }

    public String validateBsedLessTargetPosFirstBsed(Date bsed, DynamicObject position) {
        Date firstBsed = position.getDate("firstbsed");
        if (HRDateTimeUtils.dayBefore((Date)bsed, (Date)firstBsed)) {
            return String.format(Locale.ROOT, ResManager.loadKDString((String)"\u751f\u6548\u65e5\u671f\u5e94\u665a\u4e8e\u6216\u7b49\u4e8e\u534f\u4f5c\u5c97\u4f4d\u201c%1$s\u201d\u7684\u6700\u65e9\u751f\u6548\u65e5\u671f%2$s\u3002", (String)"PositionValidatorServiceHelper_39", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), position.getString("name"), HRDateTimeUtils.format((Date)firstBsed, (String)"yyyy-MM-dd"));
        }
        return null;
    }

    public String validateJobScm(DynamicObject position) {
        if (!HRObjectUtils.isEmpty((Object)position.getDynamicObject("jobscm")) && HRObjectUtils.isEmpty((Object)position.getDynamicObject("job"))) {
            return String.format(Locale.ROOT, ResManager.loadKDString((String)"\u804c\u4f4d\u4f53\u7cfb\u65b9\u6848\u4e0d\u4e3a\u7a7a\u65f6\uff0c\u804c\u4f4d\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"PositionValidatorServiceHelper_40", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]);
        }
        return null;
    }

    public String validateBSEDLessFirstBSED(DynamicObject dyn, Date firstBSED) {
        if (!HRDateTimeUtils.dayEquals((Date)dyn.getDate("bsed"), (Date)firstBSED)) {
            return String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7b2c\u4e00\u4e2a\u7248\u672c\u7684\u751f\u6548\u65e5\u671f(%1$s)\u9700\u8981\u4e0e\u5c97\u4f4d\u6700\u65e9\u751f\u6548\u65e5\u671f(%2$s)\u4e00\u81f4\u3002", (String)"PositionValidatorServiceHelper_41", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), dyn.getDate("bsed"), firstBSED);
        }
        return null;
    }
}
