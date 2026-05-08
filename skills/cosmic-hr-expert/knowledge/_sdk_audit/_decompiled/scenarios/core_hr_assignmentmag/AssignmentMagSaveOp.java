/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.operate.result.IOperateInfo
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.service.operation.OperationServiceImpl
 *  kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult
 *  kd.hr.hbp.common.api.HrApiResponse
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRDynamicObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService
 *  kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentMagDomainService
 *  kd.hrmp.hrpi.common.enums.AssignmentStatusEnum
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmployeeDateOverLapValidator
 *  kd.sdk.hr.hbp.business.helper.timeline.TimelineServiceHelper
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.operate.result.IOperateInfo;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.service.operation.OperationServiceImpl;
import kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult;
import kd.hr.hbp.common.api.HrApiResponse;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRDynamicObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService;
import kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentMagDomainService;
import kd.hrmp.hrpi.common.enums.AssignmentStatusEnum;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmployeeDateOverLapValidator;
import kd.sdk.hr.hbp.business.helper.timeline.TimelineServiceHelper;

public final class AssignmentMagSaveOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(AssignmentMagSaveOp.class);
    private List<DynamicObject> afterTimeLineDys;
    private List<DynamicObject> newOrModifyDataList;

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("startdate");
        fieldKeys.add("enddate");
        fieldKeys.add("iscurrentdata");
        fieldKeys.add("assignmentstatus");
        fieldKeys.add("assignment");
    }

    private void init(DynamicObject[] dataEntities) {
        this.genPkIds(dataEntities);
        LOGGER.info("dataEntities|{}", this.covertLog(Lists.newArrayList((Object[])dataEntities)));
        HrApiResponse timelineHandlerResult = TimelineServiceHelper.saveTimespansGetChangeInfo((String)this.billEntityType.getName(), (DynamicObject[])dataEntities, (boolean)true);
        TimelineHandlerResult data = (TimelineHandlerResult)timelineHandlerResult.getData();
        this.newOrModifyDataList = data.getNewOrModifyDataList();
        List removeIds = data.getRemoveIds();
        Set employeeIds = this.newOrModifyDataList.stream().map(dy -> dy.getLong("employee.id")).collect(Collectors.toSet());
        Set batchIds = this.newOrModifyDataList.stream().map(dy -> dy.getLong("id")).collect(Collectors.toSet());
        DynamicObject[] dbDys = IAssignmentMagDomainService.getInstance().loadDynamicObjectArray(new QFilter[]{new QFilter("employee", "in", employeeIds)});
        this.afterTimeLineDys = new ArrayList<DynamicObject>(this.newOrModifyDataList);
        for (DynamicObject dbDy : dbDys) {
            if (batchIds.contains(dbDy.getLong("id"))) continue;
            this.afterTimeLineDys.add(dbDy);
        }
        this.afterTimeLineDys.removeIf(dy -> removeIds.contains(dy.getLong("id")));
        LOGGER.info("afterTimeLineDys|{}", this.covertLog(this.afterTimeLineDys));
    }

    private void setDefaultValue(DynamicObject[] dataEntities) {
        Map<Long, List<DynamicObject>> employeeDysMap = this.afterTimeLineDys.stream().collect(Collectors.groupingBy(dy -> dy.getLong("employee.id")));
        for (DynamicObject dataEntity : dataEntities) {
            DynamicObject assignmentDy = dataEntity.getDynamicObject("assignment");
            if (HRStringUtils.isEmpty((String)dataEntity.getString("number"))) {
                dataEntity.set("number", (Object)assignmentDy.getString("number"));
            }
            if (dataEntity.getDynamicObject("orgtype") == null) {
                dataEntity.set("orgtype", (Object)assignmentDy.getDynamicObject("orgtype"));
            }
            if (dataEntity.getDynamicObject("businesstype") == null) {
                dataEntity.set("businesstype", (Object)assignmentDy.getDynamicObject("businesstype"));
            }
            dataEntity.set("isprimary", (Object)assignmentDy.getBoolean("isprimary"));
            if (dataEntity.getBoolean("isprimary")) {
                dataEntity.set("primaryassignment", (Object)assignmentDy);
                continue;
            }
            Date endDate = dataEntity.getDate("enddate");
            List<DynamicObject> dys = employeeDysMap.get(dataEntity.getLong("employee.id"));
            dys.stream().filter(dy -> dy.getBoolean("isprimary") && HRStringUtils.equals((String)dy.getString("assignmentstatus"), (String)AssignmentStatusEnum.EFFECT.getValue()) && dy.getDate("startdate").compareTo(endDate) <= 0 && dy.getDate("enddate").compareTo(endDate) >= 0).findFirst().ifPresent(dy -> {
                LOGGER.info("no main dataEntity|{},dys|{},dy|{}", new Object[]{this.covertLog(Lists.newArrayList((Object[])new DynamicObject[]{dataEntity})), this.covertLog(dys), this.covertLog(Lists.newArrayList((Object[])new DynamicObject[]{dy}))});
                dataEntity.set("primaryassignment", (Object)dy.getDynamicObject("assignment"));
            });
        }
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        this.init(dataEntities);
        this.setDefaultValue(dataEntities);
        e.addValidator((AbstractValidator)new EmployeeDateOverLapValidator(this.afterTimeLineDys));
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        DynamicObject[] dataEntities = e.getDataEntities();
        Map<Long, List<DynamicObject>> employeeDysMap = this.afterTimeLineDys.stream().collect(Collectors.groupingBy(dy -> dy.getLong("employee.id")));
        Set newOrModifyDataIds = this.newOrModifyDataList.stream().map(dy -> dy.getLong("id")).collect(Collectors.toSet());
        ArrayList<DynamicObject> timeLineDys = new ArrayList<DynamicObject>(this.afterTimeLineDys);
        ArrayList<DynamicObject> newDys = new ArrayList<DynamicObject>();
        for (DynamicObject dataEntity : dataEntities) {
            if (!HRStringUtils.equals((String)dataEntity.getString("assignmentstatus"), (String)AssignmentStatusEnum.EFFECT.getValue())) continue;
            boolean fromDatabase = dataEntity.getDataEntityState().getFromDatabase();
            boolean isPrimary = dataEntity.getBoolean("isprimary");
            long assignmentId = dataEntity.getLong("assignment.id");
            DynamicObject assignmentDy = dataEntity.getDynamicObject("assignment");
            boolean maxDate = HRDateTimeUtils.isOverSysMaxDateThreshold((Date)dataEntity.getDate("enddate"));
            List<DynamicObject> dys = employeeDysMap.get(dataEntity.getLong("employee.id"));
            if (fromDatabase || !isPrimary || !maxDate) continue;
            dataEntity.set("primaryassignment", (Object)assignmentDy);
            for (DynamicObject dy2 : dys) {
                Date endDate;
                DynamicObject newDy;
                boolean overSysMaxDateThreshold = HRDateTimeUtils.isOverSysMaxDateThreshold((Date)dy2.getDate("enddate"));
                if (assignmentId == dy2.getLong("assignment.id") || !HRStringUtils.equals((String)dy2.getString("assignmentstatus"), (String)AssignmentStatusEnum.EFFECT.getValue())) continue;
                if (dy2.getBoolean("isprimary") && overSysMaxDateThreshold && dy2.getDate("startdate").before(dataEntity.getDate("startdate"))) {
                    LOGGER.debug("main mag dy|{},dys|{},dataEntity|{}", new Object[]{this.covertLog(Lists.newArrayList((Object[])new DynamicObject[]{dy2})), this.covertLog(dys), this.covertLog(Lists.newArrayList((Object[])new DynamicObject[]{dataEntity}))});
                    newDy = IAssignmentMagDomainService.getInstance().generateEmptyDynamicObject();
                    HRDynamicObjectUtils.copy((DynamicObject)dy2, (DynamicObject)newDy);
                    newDy.set("startdate", (Object)dataEntity.getDate("startdate"));
                    newDy.set("assignmentstatus", (Object)AssignmentStatusEnum.INVALID.getValue());
                    newDy.set("iscurrentdata", (Object)(dataEntity.getDate("startdate").compareTo(HRDateTimeUtils.getNowDate()) <= 0 ? 1 : 0));
                    newDys.add(newDy);
                    endDate = HRDateTimeUtils.getBeforeDay((Date)dataEntity.getDate("startdate"));
                    dy2.set("enddate", (Object)endDate);
                    dy2.set("iscurrentdata", (Object)(endDate.compareTo(HRDateTimeUtils.getNowDate()) >= 0 ? 1 : 0));
                    newDys.add(dy2);
                }
                if (dy2.getBoolean("isprimary") || !overSysMaxDateThreshold || !dy2.getDate("startdate").before(dataEntity.getDate("startdate"))) continue;
                LOGGER.debug("no main mag dy|{},dys|{},dataEntity|{}", new Object[]{this.covertLog(Lists.newArrayList((Object[])new DynamicObject[]{dy2})), this.covertLog(dys), this.covertLog(Lists.newArrayList((Object[])new DynamicObject[]{dataEntity}))});
                newDy = IAssignmentMagDomainService.getInstance().generateEmptyDynamicObject();
                HRDynamicObjectUtils.copy((DynamicObject)dy2, (DynamicObject)newDy);
                newDy.set("startdate", (Object)dataEntity.getDate("startdate"));
                newDy.set("primaryassignment", (Object)assignmentDy);
                newDy.set("iscurrentdata", (Object)dataEntity.getDate("startdate").before(HRDateTimeUtils.getNowDate()));
                newDys.add(newDy);
                endDate = HRDateTimeUtils.getBeforeDay((Date)dataEntity.getDate("startdate"));
                dy2.set("enddate", (Object)endDate);
                dy2.set("iscurrentdata", (Object)(endDate.compareTo(HRDateTimeUtils.getNowDate()) >= 0 ? 1 : 0));
                newDys.add(dy2);
            }
        }
        newDys.removeIf(dy -> newOrModifyDataIds.contains(dy.getLong("id")));
        if (!newDys.isEmpty()) {
            IAssignmentMagDomainService.getInstance().save(newDys.toArray(new DynamicObject[0]));
            timeLineDys.addAll(newDys);
        }
        List<DynamicObject> maxAssignmentMagDys = timeLineDys.stream().filter(dy -> HRDateTimeUtils.isOverSysMaxDateThreshold((Date)dy.getDate("enddate"))).collect(Collectors.toList());
        LOGGER.info("maxAssignmentMagDys|{}", this.covertLog(maxAssignmentMagDys));
        Set assignmentIds = maxAssignmentMagDys.stream().map(dy -> dy.getLong("assignment_id")).collect(Collectors.toSet());
        DynamicObject[] existAssignmentDys = IAssignmentDomainService.getInstance().loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", assignmentIds)});
        Map<Long, DynamicObject> existAssignmentDyMap = Arrays.stream(existAssignmentDys).collect(Collectors.toMap(dy -> dy.getLong("id"), dy -> dy, (v1, v2) -> v1));
        ArrayList assignmentDys = Lists.newArrayListWithCapacity((int)dataEntities.length);
        for (DynamicObject assignmentMagDy : maxAssignmentMagDys) {
            if (assignmentMagDy.getDataEntityState().getFromDatabase() && !assignmentMagDy.getDataEntityState().isChanged()) continue;
            long assignmentId = assignmentMagDy.getLong("assignment.id");
            DynamicObject assignmentDy = Optional.ofNullable(existAssignmentDyMap.get(assignmentId)).orElseGet(() -> IAssignmentDomainService.getInstance().generateEmptyDynamicObject());
            HRDynamicObjectUtils.copy((DynamicObject)assignmentMagDy, (DynamicObject)assignmentDy);
            assignmentDy.set("id", (Object)assignmentId);
            assignmentDy.set("enable", (Object)"1");
            assignmentDys.add(assignmentDy);
        }
        if (!assignmentDys.isEmpty()) {
            Map<String, String> idNumberMap = assignmentDys.stream().filter(dy -> !dy.getDataEntityState().getFromDatabase()).collect(Collectors.toMap(dy -> dy.getString("id"), dy -> dy.getString("number"), (v1, v2) -> v1));
            OperateOption option = OperateOption.create();
            option.mergeValue(this.getOption());
            option.setVariableValue("ishasright", Boolean.TRUE.toString());
            option.setVariableValue("skipCheckDataPermission", Boolean.TRUE.toString());
            option.setVariableValue("ignorewarn", Boolean.TRUE.toString());
            option.setVariableValue("fromAssignmentmag", Boolean.TRUE.toString());
            option.setVariableValue("number", JSON.toJSONString(idNumberMap));
            OperationServiceImpl operationService = new OperationServiceImpl();
            OperationResult operationResult = operationService.localInvokeOperation("save", assignmentDys.toArray(new DynamicObject[0]), option);
            if (!operationResult.isSuccess()) {
                String errorMsg = operationResult.getAllErrorOrValidateInfo().stream().map(IOperateInfo::getMessage).collect(Collectors.joining(","));
                throw new KDBizException(errorMsg);
            }
        }
    }

    private void genPkIds(DynamicObject[] dataEntities) {
        List nullPrimaryKeyDys = Arrays.stream(dataEntities).filter(dataEntity -> dataEntity.getLong("id") == 0L).collect(Collectors.toList());
        if (nullPrimaryKeyDys.isEmpty()) {
            return;
        }
        long[] pkIds = ORM.create().genLongIds(((DynamicObject)nullPrimaryKeyDys.get(0)).getDataEntityType(), nullPrimaryKeyDys.size());
        for (int i = 0; i < nullPrimaryKeyDys.size(); ++i) {
            ((DynamicObject)nullPrimaryKeyDys.get(i)).set("id", (Object)pkIds[i]);
        }
    }

    private List<Map<String, Object>> covertLog(List<DynamicObject> magDys) {
        ArrayList result = Lists.newArrayListWithCapacity((int)magDys.size());
        for (DynamicObject magDy : magDys) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", magDy.getLong("id"));
            map.put("startdate", HRDateTimeUtils.formatDate((Date)magDy.getDate("startdate")));
            map.put("enddate", HRDateTimeUtils.formatDate((Date)magDy.getDate("enddate")));
            map.put("assignment_id", magDy.getLong("assignment.id"));
            result.add(map);
        }
        return result;
    }
}
