/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.haos.business.application.service.init.IOrgHisInitValidService
 *  kd.hr.haos.common.constants.init.AdminOrgInitTipsEnum
 *  kd.hr.haos.common.constants.masterdata.AdminOrgConstants
 *  kd.hr.haos.common.util.init.AdminOrgInitCommonUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.haos.business.application.service.init.impl;

import com.google.common.collect.Sets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.haos.business.application.service.init.IOrgHisInitValidService;
import kd.hr.haos.common.constants.init.AdminOrgInitTipsEnum;
import kd.hr.haos.common.constants.masterdata.AdminOrgConstants;
import kd.hr.haos.common.util.init.AdminOrgInitCommonUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;

public class OrgHisInitValidServiceImpl
implements IOrgHisInitValidService {
    private static final long DAY_MILLISECONDS = 86400000L;
    private static final String DATE_CONNECTION_KEY = "~";
    private static final String LEFT_SQUARE_BRACKETS = "[";
    private static final String RIGHT_SQUARE_BRACKETS = "]";
    public static final Log LOG = LogFactory.getLog(OrgHisInitValidServiceImpl.class);
    private HRBaseServiceHelper orgService = new HRBaseServiceHelper("haos_adminorg");

    public void validEffDateContinuity(List<DynamicObject> hisOrgDynList, LinkedHashMap<Long, StringBuilder> validErrMap) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        LinkedHashMap<Long, List<DynamicObject>> boDynListMap = this.getOrgBoHisVerDynListMap(hisOrgDynList);
        boDynListMap.forEach((boId, boDynList) -> {
            if (boDynList.size() < 2) {
                return;
            }
            DynamicObject preNisOrgDyn = (DynamicObject)boDynList.get(0);
            for (int i = 1; i < boDynList.size(); ++i) {
                DynamicObject currentDyn = (DynamicObject)boDynList.get(i);
                Date nextStartDate = currentDyn.getDate("bsed");
                Date prevEndDate = preNisOrgDyn.getDate("bsled");
                if (HRObjectUtils.isEmpty((Object)nextStartDate) || HRObjectUtils.isEmpty((Object)prevEndDate)) {
                    LOG.error("startDate or prevEndDate is null.orgNumber={}", (Object)preNisOrgDyn.getString("number"));
                    continue;
                }
                long diff = nextStartDate.getTime() - prevEndDate.getTime();
                if (diff > 86400000L) {
                    String startDateStr = HRDateTimeUtils.format((Date)preNisOrgDyn.getDate("bsed"), (String)"yyyy-MM-dd");
                    String startEndStr = HRDateTimeUtils.format((Date)preNisOrgDyn.getDate("bsled"), (String)"yyyy-MM-dd");
                    String curEndDateStr = HRDateTimeUtils.format((Date)currentDyn.getDate("bsed"), (String)"yyyy-MM-dd");
                    String earliestErrMsg = MessageFormat.format(AdminOrgInitTipsEnum.ADMIN_ORG_DISCONTINUOUS_ERR.getInfo(), preNisOrgDyn.getString("number"), preNisOrgDyn.getString("id"), LEFT_SQUARE_BRACKETS + startDateStr + DATE_CONNECTION_KEY + startEndStr + RIGHT_SQUARE_BRACKETS, currentDyn.getString("id"), LEFT_SQUARE_BRACKETS + nextStartDate + DATE_CONNECTION_KEY + curEndDateStr + RIGHT_SQUARE_BRACKETS);
                    AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)preNisOrgDyn, (LinkedHashMap)validErrMap, (String)earliestErrMsg);
                }
                preNisOrgDyn = currentDyn;
            }
        });
    }

    public void validEndDate(List<DynamicObject> hisOrgDynList, LinkedHashMap<Long, StringBuilder> validErrMap) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        LinkedHashMap<Long, List<DynamicObject>> boDynListMap = this.getOrgBoHisVerDynListMap(hisOrgDynList);
        boDynListMap.forEach((boId, boDynList) -> {
            if (CollectionUtils.isEmpty((Collection)boDynList)) {
                return;
            }
            DynamicObject lastHisVerDyn = (DynamicObject)boDynList.get(boDynList.size() - 1);
            if (lastHisVerDyn.getDate("bsled") == null) {
                AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)lastHisVerDyn, (LinkedHashMap)validErrMap, (String)MessageFormat.format(AdminOrgInitTipsEnum.ADMIN_ORG_END_DATE_ERR.getInfo(), lastHisVerDyn.getString("number"), HRDateTimeUtils.format((Date)lastHisVerDyn.getDate("bsed"), (String)"yyyy-MM-dd")));
            }
        });
    }

    public void validEndDateRange(List<DynamicObject> hisOrgDynList, Map<Long, DynamicObject> dbEffBizBoDynMap, Map<Long, List<DynamicObject>> dbEffOrgVerDynListMap, LinkedHashMap<Long, StringBuilder> validErrMap) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        LinkedHashMap<Long, List<DynamicObject>> boDynListMap = this.getOrgBoHisVerDynListMap(hisOrgDynList);
        boDynListMap.forEach((boId, boDynList) -> {
            List dbEffOrgHisDynList = (List)dbEffOrgVerDynListMap.get(boId);
            if (CollectionUtils.isEmpty((Collection)dbEffOrgHisDynList)) {
                return;
            }
            this.compareHisEndVsManualStartDate(validErrMap, (List<DynamicObject>)boDynList, dbEffOrgHisDynList);
        });
    }

    private void compareHisEndVsManualStartDate(LinkedHashMap<Long, StringBuilder> validErrMap, List<DynamicObject> boDynList, List<DynamicObject> dbEffOrgHisDynList) {
        List manualDbEffOrgDynList = dbEffOrgHisDynList.stream().filter(dyn -> HRStringUtils.equals((String)dyn.getString("initdatasource"), (String)"0")).collect(Collectors.toList());
        for (DynamicObject hisDyn : boDynList) {
            Date hisEndDate = hisDyn.getDate("bsled");
            if (hisEndDate == null || CollectionUtils.isEmpty(manualDbEffOrgDynList)) continue;
            DynamicObject manualDbEffOrgFirstDyn = (DynamicObject)manualDbEffOrgDynList.get(0);
            Date dbManualStartDate = manualDbEffOrgFirstDyn.getDate("bsed");
            if (hisEndDate.getTime() - dbManualStartDate.getTime() < 0L) continue;
            String hisEndDateStr = HRDateTimeUtils.format((Date)hisEndDate, (String)"yyyy-MM-dd");
            String dbManualStartDateStr = HRDateTimeUtils.format((Date)dbManualStartDate, (String)"yyyy-MM-dd");
            String earliestErrMsg = String.format(Locale.ROOT, AdminOrgInitTipsEnum.ADMIN_ORG_START_DATE_ERR.getInfo(), hisEndDateStr, dbManualStartDateStr);
            AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)hisDyn, validErrMap, (String)earliestErrMsg);
        }
    }

    public void validEffDateLegitimacy(List<DynamicObject> hisOrgDynList, Map<Long, DynamicObject> dbEffBizBoDynMap, LinkedHashMap<Long, StringBuilder> validErrMap) {
        LinkedHashMap<Long, List<DynamicObject>> boDynListMap = this.getOrgBoHisVerDynListMap(hisOrgDynList);
        boDynListMap.forEach((boId, boDynList) -> {
            DynamicObject boDyn = (DynamicObject)dbEffBizBoDynMap.get(boId);
            Date boOrgFirstStartDate = boDyn.getDate("firstbsed");
            for (DynamicObject hisDyn : boDynList) {
                Date curOrgStartDate = hisDyn.getDate("bsed");
                long diff = boOrgFirstStartDate.getTime() - curOrgStartDate.getTime();
                if (diff <= 86400000L) continue;
                String earliestErrMsg = MessageFormat.format(AdminOrgInitTipsEnum.ADMIN_ORG_EARLIEST_ERR.getInfo(), boDyn.getString("number"), hisDyn.getString("id"), HRDateTimeUtils.format((Date)curOrgStartDate, (String)"yyyy-MM-dd"), HRDateTimeUtils.format((Date)boOrgFirstStartDate, (String)"yyyy-MM-dd"));
                AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)hisDyn, (LinkedHashMap)validErrMap, (String)earliestErrMsg);
            }
        });
    }

    public void validFirstEffDateConsistency(List<DynamicObject> hisOrgDynList, Map<Long, DynamicObject> dbEffBizBoDynMap, LinkedHashMap<Long, StringBuilder> validErrMap) {
        LinkedHashMap<Long, List<DynamicObject>> boDynListMap = this.getOrgBoHisVerDynListMap(hisOrgDynList);
        boDynListMap.forEach((boId, boDynList) -> {
            Date dbEffManualFirstEffDate;
            DynamicObject boDyn = (DynamicObject)dbEffBizBoDynMap.get(boId);
            DynamicObject firstHisVerDyn = (DynamicObject)boDynList.get(0);
            Date hisDynEffDate = firstHisVerDyn.getDate("bsed");
            if (!HRDateTimeUtils.dayEquals((Date)hisDynEffDate, (Date)(dbEffManualFirstEffDate = boDyn.getDate("firstbsed")))) {
                String errMsg = MessageFormat.format(AdminOrgInitTipsEnum.ADMIN_ORG_FIRST_START_DATE_ERR.getInfo(), boDyn.getString("number"), firstHisVerDyn.getString("id"), HRDateTimeUtils.format((Date)hisDynEffDate, (String)"yyyy-MM-dd"), HRDateTimeUtils.format((Date)dbEffManualFirstEffDate, (String)"yyyy-MM-dd"));
                AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)firstHisVerDyn, (LinkedHashMap)validErrMap, (String)errMsg);
            }
        });
    }

    private LinkedHashMap<Long, List<DynamicObject>> getOrgBoHisVerDynListMap(List<DynamicObject> hisOrgDynList) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return new LinkedHashMap<Long, List<DynamicObject>>();
        }
        return hisOrgDynList.stream().collect(Collectors.groupingBy(dyn -> dyn.getLong("boid"), LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(dyn -> dyn.getDate("bsed"))).collect(Collectors.toList()))));
    }

    public void validOrgParent(List<DynamicObject> hisOrgDynList, Map<Long, DynamicObject> parentBoMap, LinkedHashMap<Long, StringBuilder> validErrMap) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        LinkedHashMap boDynListMap = hisOrgDynList.stream().collect(Collectors.groupingBy(dyn -> dyn.getLong("boid"), LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(dyn -> dyn.getDate("bsed"))).collect(Collectors.toList()))));
        boDynListMap.forEach((boId, boDynList) -> {
            if (CollectionUtils.isEmpty((Collection)boDynList)) {
                return;
            }
            for (int index = 1; index < boDynList.size(); ++index) {
                DynamicObject hisDyn = (DynamicObject)boDynList.get(index);
                Date bsed = hisDyn.getDate("bsed");
                DynamicObject parentBo = (DynamicObject)parentBoMap.get(hisDyn.getLong("parentorg.id"));
                Date parentFirstBsed = parentBo.getDate("firstbsed");
                if (bsed == null || parentFirstBsed == null) {
                    LOG.warn("bsed is null or adminOrg bsed is null.bsed:{}, parentFirstBsed:{}", (Object)bsed, (Object)parentFirstBsed);
                    continue;
                }
                if (!HRDateTimeUtils.truncateDate((Date)bsed).before(HRDateTimeUtils.truncateDate((Date)parentFirstBsed))) continue;
                String errorMsg = String.format(Locale.ROOT, AdminOrgInitTipsEnum.ADMIN_ORG_PARENT_ERR.getInfo(), HRDateTimeUtils.format((Date)bsed, (String)"yyyy-MM-dd"), parentBo.getString("number"), HRDateTimeUtils.format((Date)parentFirstBsed, (String)"yyyy-MM-dd"));
                AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)hisDyn, (LinkedHashMap)validErrMap, (String)errorMsg);
            }
        });
    }

    public void validFirstVersionParent(List<DynamicObject> hisOrgDynList, Map<Long, DynamicObject> parentBoMap, LinkedHashMap<Long, StringBuilder> validErrMap) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        LinkedHashMap boDynListMap = hisOrgDynList.stream().collect(Collectors.groupingBy(dyn -> dyn.getLong("boid"), LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(dyn -> dyn.getDate("bsed"))).collect(Collectors.toList()))));
        boDynListMap.forEach((boId, boDynList) -> {
            if (CollectionUtils.isEmpty((Collection)boDynList)) {
                return;
            }
            DynamicObject hisDyn = (DynamicObject)boDynList.get(0);
            DynamicObject parentBo = (DynamicObject)parentBoMap.get(hisDyn.getLong("parentorg.id"));
            Date parentFirstBsed = parentBo.getDate("firstbsed");
            Date blsed = hisDyn.getDate("bsled");
            if (blsed != null && HRDateTimeUtils.truncateDate((Date)blsed).before(HRDateTimeUtils.truncateDate((Date)parentFirstBsed))) {
                String errorMsg = String.format(Locale.ROOT, AdminOrgInitTipsEnum.ADMIN_ORG_FIRST_HIS_PARENT_ERR.getInfo(), HRDateTimeUtils.format((Date)blsed, (String)"yyyy-MM-dd"), parentBo.getString("number"), HRDateTimeUtils.format((Date)parentFirstBsed, (String)"yyyy-MM-dd"));
                AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)hisDyn, (LinkedHashMap)validErrMap, (String)errorMsg);
            }
        });
    }

    public void validCurrVersonParent(List<DynamicObject> hisOrgDynList, Map<Long, DynamicObject> dbEffBizBoDynMap, LinkedHashMap<Long, StringBuilder> validErrMap) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        Set currParentBoIdSet = dbEffBizBoDynMap.values().stream().map(boDyn -> boDyn.getLong("parentorg.id")).collect(Collectors.toSet());
        DynamicObject[] parentBoDynArr = this.orgService.queryOriginalArray("id,number,boid,firstbsed", new QFilter[]{new QFilter("boid", "in", currParentBoIdSet), new QFilter("iscurrentversion", "=", (Object)"1")});
        Map<Long, DynamicObject> currBoParentBoMap = Arrays.stream(parentBoDynArr).collect(Collectors.toMap(dyn -> dyn.getLong("boid"), dyn -> dyn, (d1, d2) -> d1));
        LinkedHashMap boHisDynListMap = hisOrgDynList.stream().collect(Collectors.groupingBy(dyn -> dyn.getLong("boid"), LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(dyn -> dyn.getDate("bsed"))).collect(Collectors.toList()))));
        for (List boDynList : boHisDynListMap.values()) {
            DynamicObject lastHisDyn = (DynamicObject)boDynList.get(boDynList.size() - 1);
            DynamicObject currentBo = dbEffBizBoDynMap.get(lastHisDyn.getLong("boid"));
            if (!"1".equals(currentBo.getString("initdatasource"))) continue;
            DynamicObject parentBo = currBoParentBoMap.get(currentBo.getLong("parentorg.id"));
            if (parentBo == null) {
                if (!LOG.isDebugEnabled()) continue;
                LOG.debug("currVersion of parentBo is null,boid:{},parentOrgId:{}", (Object)lastHisDyn.getLong("boid"), (Object)currentBo.getLong("parentorg.id"));
                continue;
            }
            Date bsled = lastHisDyn.getDate("bsled");
            Date parentFirstBsed = parentBo.getDate("firstbsed");
            if (bsled == null || parentFirstBsed == null) {
                LOG.warn("bsed is null or currentParent firstbsed is null");
                continue;
            }
            if (!HRDateTimeUtils.truncateDate((Date)bsled).before(HRDateTimeUtils.truncateDate((Date)parentFirstBsed))) continue;
            String errorMsg = String.format(Locale.ROOT, AdminOrgInitTipsEnum.ADMIN_ORG_INIT_CURR_PARENT_ERR.getInfo(), HRDateTimeUtils.format((Date)bsled, (String)"yyyy-MM-dd"), parentBo.getString("number"), HRDateTimeUtils.format((Date)parentFirstBsed, (String)"yyyy-MM-dd"));
            AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)lastHisDyn, validErrMap, (String)errorMsg);
        }
    }

    public void validBelongCompany(List<DynamicObject> hisOrgDynList, LinkedHashMap<Long, StringBuilder> validErrMap) {
        Set belongCompanyIdSet = hisOrgDynList.stream().map(dyn -> dyn.getLong("belongcompany.id")).collect(Collectors.toSet());
        Object[] companyDynArr = this.orgService.queryOriginalArray("id,number,boid,adminorgtype.id,belongcompany.id", new QFilter[]{new QFilter("id", "in", belongCompanyIdSet), new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE)});
        if (ObjectUtils.isEmpty((Object[])companyDynArr)) {
            throw new KDBizException(AdminOrgInitTipsEnum.ADMIN_COMPANY_ERR.getInfo());
        }
        Set adminOrgTypeIdSet = Arrays.stream(companyDynArr).map(dyn -> dyn.getLong("adminorgtype.id")).collect(Collectors.toSet());
        HRBaseServiceHelper orgTypeHelper = new HRBaseServiceHelper("haos_adminorgtype");
        DynamicObject[] orgTypeDynArr = orgTypeHelper.queryOriginalArray("id,adminorgtypestd.id", new QFilter[]{new QFilter("id", "in", adminOrgTypeIdSet)});
        Map<Long, Long> orgTypeVsStdMap = Arrays.stream(orgTypeDynArr).collect(Collectors.toMap(dyn -> dyn.getLong("id"), dyn -> dyn.getLong("adminorgtypestd.id")));
        Map<Long, DynamicObject> companyDynMap = Arrays.stream(companyDynArr).collect(Collectors.toMap(dyn -> dyn.getLong("id"), dyn -> dyn));
        for (DynamicObject hisDyn : hisOrgDynList) {
            long orgTypeId = hisDyn.getLong("adminorgtype.id");
            long companyId = hisDyn.getLong("belongcompany.id");
            DynamicObject companyDyn = companyDynMap.get(companyId);
            if (orgTypeId != 0L && companyDyn != null && AdminOrgConstants.ADMIN_ORG_TYPE_COMPANY_AND_GROUP.contains(orgTypeVsStdMap.get(companyDyn.getLong("adminorgtype.id")))) continue;
            String earliestErrMsg = MessageFormat.format(AdminOrgInitTipsEnum.ADMIN_ORG_COMPANY_ERR.getInfo(), hisDyn.getString("number"), hisDyn.getString("id"), hisDyn.getString("number"));
            AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)hisDyn, validErrMap, (String)earliestErrMsg);
        }
    }

    public void validStartDateLessEndDate(List<DynamicObject> hisOrgDynList, LinkedHashMap<Long, StringBuilder> validErrMap) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        for (DynamicObject hisDyn : hisOrgDynList) {
            Date startDate = hisDyn.getDate("bsed");
            Date endDate = hisDyn.getDate("bsled");
            if (HRObjectUtils.isEmpty((Object)startDate)) {
                LOG.error("startDate is null.");
                continue;
            }
            if (HRObjectUtils.isEmpty((Object)endDate) || !HRDateTimeUtils.truncateDate((Date)startDate).after(HRDateTimeUtils.truncateDate((Date)endDate))) continue;
            AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)hisDyn, validErrMap, (String)AdminOrgInitTipsEnum.ADMIN_ORG_START_LESS_END_DATE_ERR.getInfo());
        }
    }

    public void verifyHistoryMigrated(List<DynamicObject> hisOrgDynList, Map<Long, List<DynamicObject>> dbEffAndDiscardOrgVerMap, LinkedHashMap<Long, StringBuilder> validErrMap) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        HashSet notHisMigratedOrgBoIdSet = Sets.newHashSetWithExpectedSize((int)16);
        for (Map.Entry<Long, List<DynamicObject>> entry : dbEffAndDiscardOrgVerMap.entrySet()) {
            List<DynamicObject> hisVerList = entry.getValue();
            long initVersionCount = hisVerList.stream().filter(dyn -> HRStringUtils.equals((String)dyn.getString("initdatasource"), (String)"1")).count();
            long hasParentVersionCount = hisVerList.stream().filter(dyn -> HRStringUtils.equals((String)dyn.getString("initdatasource"), (String)"1")).filter(dyn -> dyn.getLong("parentorg.id") != 0L).count();
            if (initVersionCount > 2L || hasParentVersionCount > 1L) continue;
            notHisMigratedOrgBoIdSet.add(entry.getKey());
        }
        Set<Long> dbAllBoIdSet = dbEffAndDiscardOrgVerMap.keySet();
        for (DynamicObject hisDyn : hisOrgDynList) {
            Long boID = hisDyn.getLong("boid");
            if (!dbAllBoIdSet.contains(boID)) {
                LOG.info("ADMIN_ORG_VERIFY_HIS_BO_EXISTED,hisDynNumber:{},boID:{}", (Object)hisDyn.getString("number"), (Object)boID);
                AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)hisDyn, validErrMap, (String)AdminOrgInitTipsEnum.ADMIN_ORG_VERIFY_HIS_BO_EXISTED.getInfo());
                continue;
            }
            if (notHisMigratedOrgBoIdSet.contains(boID)) continue;
            LOG.info("ADMIN_ORG_VERIFY_HIS_MIGRATED,hisDynNumber:{},boID:{}", (Object)hisDyn.getString("number"), (Object)boID);
            AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)hisDyn, validErrMap, (String)String.format(Locale.ROOT, AdminOrgInitTipsEnum.ADMIN_ORG_VERIFY_HIS_MIGRATED.getInfo(), hisDyn.getString("number")));
        }
    }
}
