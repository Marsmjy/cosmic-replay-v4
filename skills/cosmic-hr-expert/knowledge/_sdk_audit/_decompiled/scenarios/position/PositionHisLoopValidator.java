/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.validate.ValidateResultCollection
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.validator.HRDataBaseValidator
 *  kd.hrmp.hbpm.business.domain.position.models.ReportHisLoopCheckEntity
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository
 *  kd.hrmp.hbpm.business.domain.position.service.repository.ReportingrelationQueryRepository
 *  kd.hrmp.hbpm.common.util.AdaptHisUtil
 *  org.apache.commons.collections.CollectionUtils
 */
package kd.hrmp.hbpm.opplugin.web.position.validate;

import com.google.common.collect.Lists;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.ValidateResultCollection;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.validator.HRDataBaseValidator;
import kd.hrmp.hbpm.business.domain.position.models.ReportHisLoopCheckEntity;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository;
import kd.hrmp.hbpm.business.domain.position.service.repository.ReportingrelationQueryRepository;
import kd.hrmp.hbpm.common.util.AdaptHisUtil;
import org.apache.commons.collections.CollectionUtils;

public class PositionHisLoopValidator
extends HRDataBaseValidator {
    private boolean checkSysRel;
    private boolean checkTeamUpRel;
    private boolean checkCurrent;
    private boolean onlyCheckMemory;
    private String keyName = "id";
    private Date date2999 = AdaptHisUtil.getDate2999(null);
    private Map<Long, List<ReportHisLoopCheckEntity>> extParentMap;
    private boolean isEnableOperation;

    public PositionHisLoopValidator checkHis() {
        this.checkSysRel = true;
        return this;
    }

    public void validate() {
        this.init();
        ExtendedDataEntity[] dataArr = this.getDataEntities();
        ArrayList<ExtendedDataEntity> dataList = new ArrayList<ExtendedDataEntity>(Arrays.asList(dataArr));
        List<ExtendedDataEntity> falseDataentity = this.getFalseExtendedDataEntity();
        if (CollectionUtils.isNotEmpty(falseDataentity)) {
            dataList.removeAll(falseDataentity);
        }
        if (this.checkSysRel) {
            this.checkSysRel(dataList);
        }
        if (this.checkTeamUpRel) {
            this.checkTeamUpRel(dataList);
        }
    }

    private void init() {
        this.extParentMap = new HashMap<Long, List<ReportHisLoopCheckEntity>>();
        if (this.isEnableOperation) {
            List list = Arrays.stream(this.getDataEntities()).map(ExtendedDataEntity::getDataEntity).collect(Collectors.toList());
        }
    }

    private void checkSysRel(List<ExtendedDataEntity> dataList) {
        long parent;
        DynamicObject pos;
        List<ExtendedDataEntity> checkSysRelDataList = this.removeParentNeedentCheck(dataList);
        if (checkSysRelDataList.size() == 0) {
            return;
        }
        HashMap<Long, List<ReportHisLoopCheckEntity>> roleToReportListMap = new HashMap<Long, List<ReportHisLoopCheckEntity>>();
        if (!this.onlyCheckMemory) {
            roleToReportListMap = this.getPosHisEnableAdminRelByTimeRange(checkSysRelDataList, Collections.singletonList("1010"));
        }
        if (this.isEnableOperation) {
            roleToReportListMap.putAll(this.extParentMap);
        }
        for (ExtendedDataEntity entity : dataList) {
            pos = entity.getDataEntity();
            long role = pos.getLong(this.keyName);
            parent = pos.getLong("parent.id");
            ReportHisLoopCheckEntity newCheckEntity = this.createNewCheckEntity(role, parent, "1010", pos);
            this.replaceReport(newCheckEntity, roleToReportListMap);
        }
        for (ExtendedDataEntity entity : checkSysRelDataList) {
            pos = entity.getDataEntity();
            long boid = pos.getLong("boid");
            if (!this.chekParentLoop(boid, parent = pos.getLong("parent.id"), "1010", pos, roleToReportListMap)) continue;
            String message = String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u884c\u653f\u6c47\u62a5\u5173\u7cfb\u6210\u73af\uff0c\u8bf7\u4fee\u6539\u4e0a\u7ea7\u5c97\u4f4d\u4fe1\u606f\u3002", (String)"PositionHisLoopValidator_1", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), entity.getDataEntity().getString("number"));
            this.addFatalErrorMessage(entity, message);
        }
    }

    private void checkTeamUpRel(List<ExtendedDataEntity> dataList) {
        long role;
        DynamicObjectCollection entries;
        DynamicObject pos;
        ArrayList<String> reportTypeIds = new ArrayList<String>();
        for (ExtendedDataEntity entity : dataList) {
            DynamicObject pos2 = entity.getDataEntity();
            DynamicObjectCollection entries2 = pos2.getDynamicObjectCollection("entryentity");
            if (Objects.isNull(entries2) || entries2.isEmpty()) continue;
            entries2.forEach(dy -> {
                String reportingType = dy.getString("reporttype.id");
                reportTypeIds.add(reportingType);
            });
        }
        if (CollectionUtils.isEmpty(reportTypeIds)) {
            return;
        }
        Map<Long, List<ReportHisLoopCheckEntity>> roleToReportListMap = this.getPosHisEnableAdminRelByTimeRange(dataList, reportTypeIds);
        for (ExtendedDataEntity entity : dataList) {
            pos = entity.getDataEntity();
            entries = pos.getDynamicObjectCollection("entryentity");
            if (Objects.isNull(entries) || entries.isEmpty()) continue;
            role = pos.getLong("boid");
            entries.forEach(dy -> {
                long parent = dy.getLong("targetpos.id");
                String reportingType = dy.getString("reporttype.id");
                ReportHisLoopCheckEntity newCheckEntity = this.createNewCheckEntity(role, parent, reportingType, pos);
                this.replaceReport(newCheckEntity, roleToReportListMap);
            });
        }
        for (ExtendedDataEntity entity : dataList) {
            pos = entity.getDataEntity();
            entries = pos.getDynamicObjectCollection("entryentity");
            if (Objects.isNull(entries) || entries.isEmpty()) continue;
            role = pos.getLong("boid");
            entries.forEach(dy -> {
                long parent = dy.getLong("targetpos.id");
                String reportingType = dy.getString("reporttype.id");
                if (role == parent || this.chekParentLoop(role, parent, reportingType, pos, roleToReportListMap)) {
                    String message = MessageFormat.format(ResManager.loadKDString((String)"\u201c{0}\u201d\u534f\u4f5c\u5173\u7cfb\u6210\u73af\uff0c\u8bf7\u4fee\u6539\u201c{0}\u201d\u7684\u201c\u534f\u4f5c\u5c97\u4f4d\u201d\u3002", (String)"PositionHisLoopValidator_2", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), dy.getString("reporttype.name"));
                    this.addFatalErrorMessage(entity, message);
                }
            });
        }
    }

    private Map<Long, List<ReportHisLoopCheckEntity>> getPosHisEnableAdminRelByTimeRange(List<ExtendedDataEntity> dataList, List<String> reportTypeIdList) {
        Date maxLeffdt;
        Date minEffdt;
        if (this.checkCurrent) {
            minEffdt = HRDateTimeUtils.truncateDate((Date)new Date());
            maxLeffdt = null;
        } else if (dataList.size() == 1) {
            minEffdt = dataList.get(0).getDataEntity().getDate("bsed");
            maxLeffdt = dataList.get(0).getDataEntity().getDate("bsled");
        } else {
            minEffdt = this.getMinEffdt(dataList);
            maxLeffdt = null;
        }
        return ReportingrelationQueryRepository.getInstance().queryPosHisEnableAdminRelByTimeRange(minEffdt, maxLeffdt, reportTypeIdList);
    }

    private List<ExtendedDataEntity> removeParentNeedentCheck(List<ExtendedDataEntity> dataList) {
        ArrayList resultDataList = Lists.newArrayListWithExpectedSize((int)dataList.size());
        Map<Object, Object> idParentMap = new HashMap();
        Map<Object, Object> idBoIdMap = new HashMap();
        if (!this.onlyCheckMemory) {
            List hisIdList = dataList.stream().map(o -> o.getDataEntity().getLong(this.keyName)).collect(Collectors.toList());
            DynamicObject[] posFromDB = PositionQueryRepository.getInstance().queryPositionParentByIds(hisIdList);
            idParentMap = Arrays.stream(posFromDB).collect(Collectors.toMap(o -> o.getLong("id"), o -> o.getLong("parent")));
            idBoIdMap = Arrays.stream(posFromDB).collect(Collectors.toMap(o -> o.getLong("id"), o -> o.getLong("boid")));
            if (HRStringUtils.equals((String)this.keyName, (String)"boid")) {
                idBoIdMap = dataList.stream().collect(Collectors.toMap(data -> data.getDataEntity().getLong("id"), data -> data.getDataEntity().getLong("boid")));
            }
        }
        for (ExtendedDataEntity data2 : dataList) {
            DynamicObject pos = data2.getDataEntity();
            long id = pos.getLong("id");
            long parent = pos.getLong("parent.id");
            Long boId = (Long)idBoIdMap.get(id);
            Long parentFromDB = (Long)idParentMap.get(id);
            if (boId != null && parent == boId) {
                String message = String.format(ResManager.loadKDString((String)"\u4e0a\u7ea7\u5c97\u4f4d\u4e0d\u80fd\u9009\u5f53\u524d\u5c97\u4f4d\u201c%1$s(%2$s)\u201d\u3002", (String)"PositionHisLoopValidator_0", (String)"hrmp-hbpm-opplugin", (Object[])new Object[0]), data2.getDataEntity().getString("name"), data2.getDataEntity().getString("number"));
                this.addFatalErrorMessage(data2, message);
                continue;
            }
            if (parentFromDB != null && parent == parentFromDB || parent == 0L) continue;
            resultDataList.add(data2);
        }
        return resultDataList;
    }

    private boolean chekParentLoop(long boid, long parent, String reportingtype, DynamicObject pos, Map<Long, List<ReportHisLoopCheckEntity>> roleToReportListMap) {
        if (boid == 0L || parent == 0L) {
            return Boolean.FALSE;
        }
        ReportHisLoopCheckEntity newCheckEntity = this.createNewCheckEntity(boid, parent, reportingtype, pos);
        HashSet<Long> checkedSet = new HashSet<Long>();
        return this.check(newCheckEntity, parent, roleToReportListMap, checkedSet);
    }

    private void replaceReport(ReportHisLoopCheckEntity newCheckEntity, Map<Long, List<ReportHisLoopCheckEntity>> roleToReportListMap) {
        long role = newCheckEntity.getRole();
        List<ReportHisLoopCheckEntity> roleReportList = roleToReportListMap.get(role);
        if (roleReportList == null) {
            roleReportList = new ArrayList<ReportHisLoopCheckEntity>();
        }
        for (ReportHisLoopCheckEntity report : roleReportList) {
            boolean lessThanOriginalBlsed;
            if (!HRStringUtils.equals((String)newCheckEntity.getReportingtype(), (String)report.getReportingtype())) continue;
            boolean largeThanOriginalBsed = !HRDateTimeUtils.dayAfter((Date)HRDateTimeUtils.truncateDate((Date)report.getBsed()), (Date)HRDateTimeUtils.truncateDate((Date)newCheckEntity.getBsed()));
            boolean bl = lessThanOriginalBlsed = !HRDateTimeUtils.dayAfter((Date)HRDateTimeUtils.truncateDate((Date)newCheckEntity.getBsed()), (Date)HRDateTimeUtils.truncateDate((Date)report.getBsled()));
            if (!largeThanOriginalBsed || !lessThanOriginalBlsed) continue;
            report.setBsled(HRDateTimeUtils.addDay((Date)newCheckEntity.getBsed(), (long)-1L));
        }
        roleReportList.add(newCheckEntity);
    }

    private boolean check(ReportHisLoopCheckEntity original, long temp, Map<Long, List<ReportHisLoopCheckEntity>> roleToReportListMap, Set<Long> checkedSet) {
        while (Objects.nonNull(roleToReportListMap.get(temp))) {
            boolean ischanged = false;
            List<ReportHisLoopCheckEntity> reports = roleToReportListMap.get(temp);
            for (ReportHisLoopCheckEntity entity : reports) {
                if (!this.isSameType(original, entity) || !this.isSameTime(original, entity)) continue;
                if (entity.getParent() == original.getRole()) {
                    return Boolean.TRUE;
                }
                if (!checkedSet.add(temp)) continue;
                temp = entity.getParent();
                ischanged = Boolean.TRUE;
                if (!this.check(original, temp, roleToReportListMap, checkedSet)) continue;
                return Boolean.TRUE;
            }
            if (ischanged) continue;
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    private boolean isSameType(ReportHisLoopCheckEntity original, ReportHisLoopCheckEntity entity) {
        return HRStringUtils.equals((String)original.getReportingtype(), (String)entity.getReportingtype());
    }

    private boolean isSameTime(ReportHisLoopCheckEntity original, ReportHisLoopCheckEntity entity) {
        boolean bsledLargeThanOriginalBsed = !HRDateTimeUtils.dayAfter((Date)HRDateTimeUtils.truncateDate((Date)original.getBsed()), (Date)HRDateTimeUtils.truncateDate((Date)entity.getBsled()));
        boolean bsedLessThanOriginalBlsed = !HRDateTimeUtils.dayAfter((Date)HRDateTimeUtils.truncateDate((Date)entity.getBsed()), (Date)HRDateTimeUtils.truncateDate((Date)original.getBsled()));
        return bsledLargeThanOriginalBsed && bsedLessThanOriginalBlsed;
    }

    private ReportHisLoopCheckEntity createNewCheckEntity(long boid, long parent, String reportingtype, DynamicObject pos) {
        ReportHisLoopCheckEntity entity = new ReportHisLoopCheckEntity();
        entity.setRole(boid);
        entity.setParent(parent);
        entity.setBsed(pos.getDate("bsed"));
        Date bsled = pos.getDate("bsled");
        entity.setBsled(bsled == null ? this.date2999 : bsled);
        entity.setReportingtype(reportingtype);
        return entity;
    }

    private Date getMinEffdt(List<ExtendedDataEntity> data) {
        List sortetList = data.stream().sorted(Comparator.comparing(o -> o.getDataEntity().getDate("bsed"))).collect(Collectors.toList());
        return ((ExtendedDataEntity)sortetList.get(0)).getDataEntity().getDate("bsed");
    }

    private List<ExtendedDataEntity> getFalseExtendedDataEntity() {
        ValidateResultCollection resultCollection = this.getValidateContext().getValidateResults();
        if (resultCollection == null) {
            return new ArrayList<ExtendedDataEntity>();
        }
        HashSet falseSet = this.getValidateContext().getValidateResults().getErrorDataIndexs();
        ExtendedDataEntity[] extendedDataEntities = this.getDataEntities();
        if (!kd.bos.orm.util.CollectionUtils.isEmpty((Collection)falseSet)) {
            ArrayList<ExtendedDataEntity> falseList = new ArrayList<ExtendedDataEntity>(falseSet.size());
            falseSet.forEach(id -> falseList.add(extendedDataEntities[id]));
            return falseList;
        }
        return new ArrayList<ExtendedDataEntity>();
    }
}
