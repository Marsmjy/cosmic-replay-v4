/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo
 *  kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IEmpPosOrgRelApplicationService
 *  kd.hrmp.hrpi.business.domain.assigment.service.IEmpJobRelDomainService
 *  kd.hrmp.hrpi.business.infrastructure.utils.DateUtil
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.BeforeAdjEmpJobRelEndDateValidator
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.BeforeAdjEmpJobRelValidator
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpJobRelJobValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo;
import kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IEmpPosOrgRelApplicationService;
import kd.hrmp.hrpi.business.domain.assigment.service.IEmpJobRelDomainService;
import kd.hrmp.hrpi.business.infrastructure.utils.DateUtil;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.BeforeAdjEmpJobRelEndDateValidator;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.BeforeAdjEmpJobRelValidator;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpJobRelJobValidator;

public final class EmpJobRelSaveOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(EmpJobRelSaveOp.class);
    private static final String BASE_DATA_ID = "fbasedataid";
    private Set<Long> chgActionIds;

    public EmpJobRelSaveOp() {
        HRBaseServiceHelper chgActionHelper = new HRBaseServiceHelper("hpfs_chgaction");
        DynamicObject[] chgActionDys = chgActionHelper.queryOriginalArray("id", new QFilter[]{new QFilter("chgcategory", "=", (Object)102030L)});
        this.chgActionIds = Arrays.stream(chgActionDys).map(dy -> dy.getLong("id")).collect(Collectors.toSet());
    }

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("assignment");
        fieldKeys.add("enddate");
        fieldKeys.add("startdate");
        fieldKeys.add("adminorg");
        fieldKeys.add("company");
        fieldKeys.add("orgrelseq");
        fieldKeys.add("job");
        fieldKeys.add("jobclass");
        fieldKeys.add("jobseq");
        fieldKeys.add("jobfamily");
        fieldKeys.add("jobscm");
        fieldKeys.add("chgaction");
        fieldKeys.add("primaryempjobrel");
        fieldKeys.add("beforeadjempjobrel");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        this.addLog(dataEntities);
        args.addValidator((AbstractValidator)new EmpJobRelJobValidator());
        args.addValidator((AbstractValidator)new BeforeAdjEmpJobRelEndDateValidator());
        args.addValidator((AbstractValidator)new BeforeAdjEmpJobRelValidator(this.chgActionIds));
    }

    private void addLog(DynamicObject[] dataEntities) {
        ArrayList params = Lists.newArrayListWithCapacity((int)dataEntities.length);
        Date timelineMaxDate = TimeLineServiceUtil.getMaxEffEndDate();
        for (DynamicObject dataEntity : dataEntities) {
            if (dataEntity.getDate("enddate") == null) {
                dataEntity.set("enddate", (Object)timelineMaxDate);
            }
            LinkedHashMap<String, Comparable<Boolean>> param = new LinkedHashMap<String, Comparable<Boolean>>();
            param.put("fromDatabase", Boolean.valueOf(dataEntity.getDataEntityState().getFromDatabase()));
            param.put("id", Long.valueOf(dataEntity.getLong("id")));
            param.put("beforeadjempjobrel_id", Long.valueOf(dataEntity.getLong("beforeadjempjobrel_id")));
            param.put("primaryempjobrel", Boolean.valueOf(dataEntity.getBoolean("primaryempjobrel")));
            param.put("assignment", Long.valueOf(dataEntity.getLong("assignment.id")));
            param.put("employee", Long.valueOf(dataEntity.getLong("employee.id")));
            param.put("orgrelseq", Integer.valueOf(dataEntity.getInt("orgrelseq")));
            param.put("adminorg", Long.valueOf(dataEntity.getLong("adminorg.id")));
            param.put("job", Long.valueOf(dataEntity.getLong("job.id")));
            param.put("startdate", dataEntity.getDate("startdate"));
            param.put("enddate", dataEntity.getDate("enddate"));
            params.add(param);
        }
        LOGGER.info("EmpJobRelSaveOp save onlyValidate|{} params|{}", (Object)this.getOption().getVariables().getOrDefault("onlyvalidate", Boolean.FALSE.toString()), (Object)params);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        Map<Long, DynamicObject> curDataEntityMap = Arrays.stream(dataEntities).collect(Collectors.toMap(dy -> dy.getLong("id"), dy -> dy, (v1, v2) -> v2));
        HashMap dbModifyEndDateMap = Maps.newHashMapWithExpectedSize((int)dataEntities.length);
        Set assignmentIds = Arrays.stream(dataEntities).map(dy -> dy.getLong("assignment.id")).collect(Collectors.toSet());
        DynamicObject[] primaryOrgRelDys = IEmpPosOrgRelApplicationService.getInstance().queryEmpposorgrel("adminorg,assignment.id", new QFilter[]{new QFilter("assignment", "in", assignmentIds), new QFilter("isprimary", "=", (Object)Boolean.TRUE), new QFilter("isseqlatestrecord", "=", (Object)Boolean.TRUE)});
        Map<Long, DynamicObject> primaryOrgRelAdminOrgMap = Arrays.stream(primaryOrgRelDys).collect(Collectors.toMap(dy -> dy.getLong("assignment.id"), dy -> dy.getDynamicObject("adminorg"), (v1, v2) -> v1));
        for (DynamicObject dataEntity : dataEntities) {
            DynamicObject jobDy;
            DynamicObjectCollection jobGradeScmDys;
            DynamicObject adminOrgDy;
            long beforeAdjEmpJobRelId = dataEntity.getLong("beforeadjempjobrel_id");
            boolean isNotAdjust = dataEntity.getDataEntityState().getFromDatabase() || beforeAdjEmpJobRelId == 0L || !this.chgActionIds.contains(dataEntity.getLong("chgaction.id"));
            int orgRelSeq = dataEntity.getInt("orgrelseq");
            DynamicObject beforeJobRelDy = dataEntity.getDynamicObject("beforeadjempjobrel");
            if (orgRelSeq == 0 && !isNotAdjust && beforeJobRelDy != null) {
                dataEntity.set("orgrelseq", (Object)beforeJobRelDy.getInt("orgrelseq"));
            }
            if ((adminOrgDy = dataEntity.getDynamicObject("adminorg")) == null) {
                if (!isNotAdjust && beforeJobRelDy != null) {
                    dataEntity.set("adminorg", (Object)beforeJobRelDy.getDynamicObject("adminorg"));
                } else {
                    long assignmentId = dataEntity.getLong("assignment.id");
                    dataEntity.set("adminorg", (Object)primaryOrgRelAdminOrgMap.get(assignmentId));
                }
            }
            dataEntity.set("company", (Object)dataEntity.getDynamicObject("adminorg.belongcompany"));
            DynamicObjectCollection jobLevelScmDys = dataEntity.getDynamicObjectCollection("joblevel.joblevelscm");
            if (!CollectionUtils.isEmpty((Collection)jobLevelScmDys) && jobLevelScmDys.size() == 1) {
                dataEntity.set("joblevelscm", (Object)((DynamicObject)jobLevelScmDys.get(0)).getDynamicObject(BASE_DATA_ID));
            }
            if (!CollectionUtils.isEmpty((Collection)(jobGradeScmDys = dataEntity.getDynamicObjectCollection("jobgrade.jobgradescm"))) && jobGradeScmDys.size() == 1) {
                dataEntity.set("jobgradescm", (Object)((DynamicObject)jobGradeScmDys.get(0)).getDynamicObject(BASE_DATA_ID));
            }
            if ((jobDy = dataEntity.getDynamicObject("job")) != null) {
                dataEntity.set("jobclass", (Object)jobDy.getDynamicObject("jobclass"));
                dataEntity.set("jobfamily", (Object)jobDy.getDynamicObject("jobfamily"));
                dataEntity.set("jobseq", (Object)jobDy.getDynamicObject("jobseq"));
                DynamicObjectCollection jobScmDys = jobDy.getDynamicObjectCollection("jobscm");
                if (!CollectionUtils.isEmpty((Collection)jobScmDys) && jobScmDys.size() == 1) {
                    dataEntity.set("jobscm", (Object)((DynamicObject)jobScmDys.get(0)).getDynamicObject(BASE_DATA_ID));
                }
            }
            if (isNotAdjust) continue;
            Date startDate = dataEntity.getDate("startdate");
            DynamicObject beforeAdjEmpJobRelDy = curDataEntityMap.get(beforeAdjEmpJobRelId);
            Date beforeDay = HRDateTimeUtils.getBeforeDay((Date)startDate);
            if (beforeAdjEmpJobRelDy != null) {
                beforeAdjEmpJobRelDy.set("enddate", (Object)beforeDay);
                continue;
            }
            dbModifyEndDateMap.put(beforeAdjEmpJobRelId, beforeDay);
        }
        if (dbModifyEndDateMap.isEmpty()) {
            return;
        }
        DynamicObject[] dbExistDataEntities = IEmpJobRelDomainService.getInstance().query("startdate,enddate,iscurrentdata", new QFilter[]{new QFilter("id", "in", dbModifyEndDateMap.keySet())});
        ArrayList originalDataEntities = Lists.newArrayListWithCapacity((int)dbExistDataEntities.length);
        for (DynamicObject dbExistDataEntity : dbExistDataEntities) {
            long empJobRelId = dbExistDataEntity.getLong("id");
            Date beforeDay = (Date)dbModifyEndDateMap.get(empJobRelId);
            if (beforeDay == null || DateUtil.isSameDay((Object)beforeDay, (Object)dbExistDataEntity.getDate("enddate"))) continue;
            Date startDate = dbExistDataEntity.getDate("startdate");
            dbExistDataEntity.set("enddate", (Object)beforeDay);
            Date today = HRDateTimeUtils.truncateDate((Date)new Date());
            dbExistDataEntity.set("enddate", (Object)beforeDay);
            boolean isCurrentData = startDate.getTime() <= today.getTime() && beforeDay.getTime() >= today.getTime();
            dbExistDataEntity.set("iscurrentdata", (Object)isCurrentData);
            originalDataEntities.add(dbExistDataEntity);
        }
        IEmpJobRelDomainService.getInstance().save(originalDataEntities.toArray(new DynamicObject[0]));
        TimelineChangeIdInfo changeInfo = new TimelineChangeIdInfo();
        changeInfo.setSaveDataIds(originalDataEntities.stream().map(dy -> dy.getLong("id")).collect(Collectors.toList()));
        this.getOption().setVariableValue("timeLineOpChangeInfo", SerializationUtils.toJsonString((Object)changeInfo));
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        super.endOperationTransaction(e);
        DynamicObject[] dataEntities = e.getDataEntities();
        Map<Long, DynamicObject> assignmentPrimaryEmpJobRelMap = Arrays.stream(dataEntities).filter(dy -> dy.getBoolean("primaryempjobrel")).collect(Collectors.toMap(dy -> dy.getLong("assignment.id"), dy -> dy, (v1, v2) -> v2));
        DynamicObject[] dbEmpJobRelDys = IEmpJobRelDomainService.getInstance().query("primaryempjobrel,startdate,enddate,assignment.id", new QFilter[]{new QFilter("assignment", "in", assignmentPrimaryEmpJobRelMap.keySet()), new QFilter("primaryempjobrel", "=", (Object)Boolean.TRUE)});
        ArrayList modifyDbEmpJobRelDys = Lists.newArrayListWithCapacity((int)dbEmpJobRelDys.length);
        for (DynamicObject dbEmpJobRelDy : dbEmpJobRelDys) {
            boolean isCover;
            long assignmentId = dbEmpJobRelDy.getLong("assignment.id");
            DynamicObject newEmpJobRelDy = assignmentPrimaryEmpJobRelMap.get(assignmentId);
            if (newEmpJobRelDy == null || newEmpJobRelDy.getLong("id") == dbEmpJobRelDy.getLong("id")) continue;
            Date newStartDate = newEmpJobRelDy.getDate("startdate");
            Date newEndDate = newEmpJobRelDy.getDate("enddate");
            Date dbStartDate = dbEmpJobRelDy.getDate("startdate");
            Date dbEndDate = dbEmpJobRelDy.getDate("enddate");
            boolean noOverlap = newEndDate.before(dbStartDate) || newStartDate.after(dbEndDate);
            boolean bl = isCover = DateUtil.isSameDay((Object)dbStartDate, (Object)newStartDate) && DateUtil.isSameDay((Object)dbEndDate, (Object)newEndDate);
            if (noOverlap && !isCover) continue;
            dbEmpJobRelDy.set("primaryempjobrel", (Object)Boolean.FALSE);
            modifyDbEmpJobRelDys.add(dbEmpJobRelDy);
        }
        IEmpJobRelDomainService.getInstance().save(modifyDbEmpJobRelDys.toArray(new DynamicObject[0]));
    }
}
