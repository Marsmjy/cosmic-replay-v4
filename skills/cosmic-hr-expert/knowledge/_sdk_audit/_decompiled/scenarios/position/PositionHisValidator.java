/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ArrayUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.opplugin.validator.HRDataBaseValidator
 *  kd.hrmp.hbpm.business.domain.position.service.impl.PositionHrDomainService
 *  kd.hrmp.hbpm.business.utils.PositionUtils
 */
package kd.hrmp.hbpm.opplugin.web.position.validate;

import com.google.common.collect.Sets;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ArrayUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.opplugin.validator.HRDataBaseValidator;
import kd.hrmp.hbpm.business.domain.position.service.impl.PositionHrDomainService;
import kd.hrmp.hbpm.business.utils.PositionUtils;
import kd.hrmp.hbpm.opplugin.web.position.validate.PositionValidatorServiceHelper;

public class PositionHisValidator
extends HRDataBaseValidator {
    PositionValidatorServiceHelper positionValidatorServiceHelper;
    private static final Log LOGGER = LogFactory.getLog(PositionHisValidator.class);

    public void validate() {
        if (ArrayUtils.isEmpty((Object[])this.dataEntities)) {
            return;
        }
        this.positionValidatorServiceHelper = new PositionValidatorServiceHelper();
        PositionHrDomainService iPositionHrDomainService = new PositionHrDomainService();
        Arrays.sort(this.dataEntities, Comparator.comparing(dataEntity -> dataEntity.getDataEntity().getLong("boid")).thenComparing(dataEntity -> dataEntity.getDataEntity().getDate("bsed")));
        Set boidSet = Arrays.stream(this.dataEntities).map(dataEntity -> dataEntity.getDataEntity().getLong("boid")).collect(Collectors.toSet());
        List<DynamicObject> positionHisList = Arrays.stream(this.dataEntities).map(dataEntity -> dataEntity.getDataEntity()).collect(Collectors.toList());
        DynamicObject[] positionBoArray = iPositionHrDomainService.queryPositionByBOID(boidSet);
        Map<Long, Date> boAndFirstBSEDMap = Arrays.stream(positionBoArray).collect(Collectors.toMap(dyn -> dyn.getLong("boid"), dyn -> dyn.getDate("firstbsed")));
        Map<Long, Long> boAndAdminOrgIdMap = Arrays.stream(positionBoArray).collect(Collectors.toMap(dyn -> dyn.getLong("boid"), dyn -> dyn.getLong("adminorg.id")));
        Set firstVersionIdSet = positionHisList.stream().collect(Collectors.toMap(dyn -> dyn.getLong("boid"), dyn -> dyn.getLong("id"), (first, second) -> first)).values().stream().collect(Collectors.toSet());
        Set lastVersionIdSet = positionHisList.stream().collect(Collectors.toMap(dyn -> dyn.getLong("boid"), dyn -> dyn.getLong("id"), (first, second) -> second)).values().stream().collect(Collectors.toSet());
        for (ExtendedDataEntity dataEntity2 : this.dataEntities) {
            DynamicObject dyn2 = dataEntity2.getDataEntity();
            LOGGER.info("PositionHisValidator:positionhis(number:{},boid:{})", (Object)dyn2.getString("number"), (Object)dyn2.getLong("boid"));
            if (this.validateBoId(dataEntity2, dyn2)) continue;
            if (firstVersionIdSet.contains(dyn2.getLong("id"))) {
                this.validateBSEDLessFirstBSED(dyn2, dataEntity2, boAndFirstBSEDMap.get(dyn2.getLong("boid")));
            }
            if (lastVersionIdSet.contains(dyn2.getLong("id"))) {
                this.validateBSLEDRequired(dyn2, dataEntity2);
            }
            this.validateAdminOrg(dyn2, boAndAdminOrgIdMap.get(dyn2.getLong("boid")), dataEntity2);
            this.validateJobScm(dyn2, dataEntity2);
            this.validateBSEDLESSBSLED(dyn2, dataEntity2);
            this.validateEstablishLessParentFirstBsed(dyn2, dataEntity2);
            this.validateBSedAndAdminOrgFirstBsed(dyn2, dataEntity2);
            this.validateBSedLessNow(dyn2, dataEntity2);
        }
        this.validateBsedAndBsledRelationship(positionHisList);
        this.validateWholeErrorInfo();
    }

    public void validateBsedAndBsledRelationship(List<DynamicObject> positionHisList) {
        HashSet boidSet = Sets.newHashSetWithExpectedSize((int)16);
        for (int i = 0; i < positionHisList.size() - 1; ++i) {
            Timestamp nextBsedMinusOneDay;
            DynamicObject current = positionHisList.get(i);
            DynamicObject next = positionHisList.get(i + 1);
            if (!boidSet.contains(current.getLong("boid"))) {
                boidSet.add(current.getLong("boid"));
            }
            if (!boidSet.contains(next.getLong("boid"))) continue;
            Date bsled = current.getDate("bsled");
            Date nextBsed = next.getDate("bsed");
            if (bsled == null || nextBsed == null || bsled.equals(nextBsedMinusOneDay = new Timestamp(nextBsed.getTime() - 86400000L))) continue;
            this.addFatalErrorMessage(this.dataEntities[i + 1], String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5f53\u524d\u7248\u672c\u7684\u751f\u6548\u65e5\u671f(%1$s)\u4e0e\u4e0a\u4e00\u4e2a\u5386\u53f2\u7248\u672c\u7684\u5931\u6548\u65e5\u671f(%2$s)\u4e0d\u8fde\u7eed\uff0c\u8bf7\u8c03\u6574\u751f\u6548\u65e5\u671f\u3002", (String)"PositionHisValidator_7", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), nextBsed, bsled));
        }
    }

    private void validateBSEDLESSBSLED(DynamicObject dyn, ExtendedDataEntity dataEntity) {
        if (!HRObjectUtils.isEmpty((Object)dyn.getDate("bsled")) && HRDateTimeUtils.dayAfter((Date)dyn.getDate("bsed"), (Date)dyn.getDate("bsled"))) {
            this.addFatalErrorMessage(dataEntity, String.format(Locale.ROOT, ResManager.loadKDString((String)"\u751f\u6548\u65e5\u671f\u4e0d\u80fd\u665a\u4e8e\u5931\u6548\u65e5\u671f\u3002", (String)"PositionHisValidator_6", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), new Object[0]));
        }
    }

    private boolean validateBoId(ExtendedDataEntity dataEntity, DynamicObject dyn) {
        if (HRObjectUtils.isEmpty((Object)dyn.getLong("boid"))) {
            LOGGER.error("position(number:{},boid{}) boid is null", (Object)dyn.getString("number"), (Object)dyn.getLong("boid"));
            throw new KDBizException(String.format(ResManager.loadKDString((String)"\u5c97\u4f4d\u5386\u53f2\u7684\u8fc1\u79fb\u5bf9\u8c61\u5b57\u6bb5\"\u5386\u53f2\u7248\u672c\u6570\u636e\"\u672a\u52fe\u9009\u4e3a\u662f\uff0c\u65e0\u6cd5\u8fc1\u79fb\u5386\u53f2\u3002", (String)"PositionHisValidator_3", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), dataEntity.getDataEntity().getString("number")));
        }
        if (dyn.getLong("boid") == 0L) {
            LOGGER.error("position(number:{},boid{} boid is 0L", (Object)dyn.getString("number"), (Object)dyn.getLong("boid"));
            this.addFatalErrorMessage(dataEntity, String.format(ResManager.loadKDString((String)"\u7f16\u7801\u4e3a%1$s\u7684\u5c97\u4f4d\u5728\u7cfb\u7edf\u4e2d\u4e0d\u5b58\u5728\uff0c\u8bf7\u5148\u5bfc\u5165\u5f53\u524d\u7248\u672c\u5c97\u4f4d\u6570\u636e\u3002", (String)"PositionHisValidator_5", (String)dyn.getString("number"), (Object[])new Object[0]), new Object[0]));
            return true;
        }
        return false;
    }

    private void validateJobScm(DynamicObject dyn, ExtendedDataEntity dataEntity) {
        String message = this.positionValidatorServiceHelper.validateJobScm(dyn);
        if (StringUtils.isNotEmpty((CharSequence)message)) {
            this.addFatalErrorMessage(dataEntity, message);
        }
    }

    private void validateEstablishLessParentFirstBsed(DynamicObject positionBill, ExtendedDataEntity data) {
        String message = this.positionValidatorServiceHelper.validateEstablishLessParentFirstBsed(positionBill.getDate("bsed"), positionBill.getDynamicObject("parent"));
        if (StringUtils.isNotEmpty((CharSequence)message)) {
            this.addFatalErrorMessage(data, message);
        }
    }

    private void validateBSedAndAdminOrgFirstBsed(DynamicObject positionBill, ExtendedDataEntity data) {
        String msg = this.positionValidatorServiceHelper.validateBSedAndAdminOrgFirstBsed(positionBill.getDate("bsed"), positionBill.getDynamicObject("adminorg"));
        if (StringUtils.isNotEmpty((CharSequence)msg)) {
            this.addFatalErrorMessage(data, msg);
        }
    }

    private void validateBSedLessNow(DynamicObject position, ExtendedDataEntity data) {
        Date bsed = position.getDate("bsed");
        if (bsed == null) {
            return;
        }
        String msg = this.positionValidatorServiceHelper.validateBSedLessAssignDate(bsed, PositionUtils.getCurrentDate());
        if (StringUtils.isNotEmpty((CharSequence)msg)) {
            this.addFatalErrorMessage(data, msg);
        }
    }

    private void validateBSLEDRequired(DynamicObject dyn, ExtendedDataEntity dataEntity) {
        if (HRObjectUtils.isEmpty((Object)dyn.getDate("bsled"))) {
            this.addFatalErrorMessage(dataEntity, String.format(ResManager.loadKDString((String)"\u6700\u540e\u4e00\u4e2a\u7248\u672c\u5931\u6548\u65e5\u671f\u5fc5\u586b\u3002", (String)"PositionHisValidator_2", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), dataEntity.getDataEntity().getString("number")));
        }
    }

    private void validateBSEDLessFirstBSED(DynamicObject dyn, ExtendedDataEntity dataEntity, Date firstBSED) {
        String message = this.positionValidatorServiceHelper.validateBSEDLessFirstBSED(dyn, firstBSED);
        if (StringUtils.isNotEmpty((CharSequence)message)) {
            this.addFatalErrorMessage(dataEntity, message);
        }
    }

    private void validateAdminOrg(DynamicObject positionHisDyn, Long currentVersionAdminOrgId, ExtendedDataEntity dataEntity) {
        if (positionHisDyn.getLong("adminorg.id") != currentVersionAdminOrgId.longValue()) {
            this.addFatalErrorMessage(dataEntity, String.format(ResManager.loadKDString((String)"\u5386\u53f2\u7248\u672c\u6570\u636e\u884c\u653f\u7ec4\u7ec7\u9700\u8981\u4e0e\u5f53\u524d\u7248\u672c\u6570\u636e\u4e00\u81f4\u3002", (String)"PositionHisValidator_4", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), dataEntity.getDataEntity().getString("number")));
        }
    }

    private void validateWholeErrorInfo() {
        if (!this.validateResult.isSuccess()) {
            Set errorIndexSet = this.validateResult.getAllErrorInfo().stream().map(errorInfo -> errorInfo.getDataEntityIndex()).collect(Collectors.toSet());
            Map<Long, List<Integer>> boidIndexMap = errorIndexSet.stream().collect(Collectors.groupingBy(errorIndex -> this.dataEntities[errorIndex].getDataEntity().getLong("boid")));
            Map<Long, List<ExtendedDataEntity>> boDataEntityMap = Arrays.stream(this.dataEntities).filter(dataEntity -> boidIndexMap.containsKey(dataEntity.getDataEntity().getLong("boid"))).collect(Collectors.groupingBy(dataEntity -> dataEntity.getDataEntity().getLong("boid")));
            for (Map.Entry<Long, List<Integer>> entry : boidIndexMap.entrySet()) {
                List<Integer> errorIndexList = entry.getValue();
                Long errorBOID = entry.getKey();
                List bsedList = boDataEntityMap.get(errorBOID).stream().filter(dataEntity -> errorIndexList.contains(dataEntity.getDataEntityIndex())).map(dataEntity -> dataEntity.getDataEntity()).map(dyn -> dyn.getDate("bsed")).collect(Collectors.toList());
                boDataEntityMap.get(errorBOID).stream().filter(dataEntity -> !errorIndexList.contains(dataEntity.getDataEntityIndex())).forEach(dataEntity -> this.addFatalErrorMessage((ExtendedDataEntity)dataEntity, String.format(ResManager.loadKDString((String)"\u6709\u5176\u4ed6\u7248\u672c\u6821\u9a8c\u5931\u8d25(\u7f16\u7801\uff1a%1$s,\u65e5\u671f\uff1a%2$s)\u3002", (String)"PositionHisValidator_1", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), dataEntity.getDataEntity().getString("number"), bsedList)));
            }
        }
    }
}
