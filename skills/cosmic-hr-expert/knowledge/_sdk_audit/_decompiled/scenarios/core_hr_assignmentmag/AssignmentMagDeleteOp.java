/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
 *  kd.bos.orm.query.QFilter
 *  kd.bos.service.operation.OperationServiceImpl
 *  kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult
 *  kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult$DataChangeInfo
 *  kd.hr.hbp.common.api.HrApiResponse
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRDynamicObjectUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.AssignmentMagDeleteKeepOneValidator
 *  kd.sdk.hr.hbp.business.helper.timeline.TimelineServiceHelper
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import kd.bos.orm.query.QFilter;
import kd.bos.service.operation.OperationServiceImpl;
import kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult;
import kd.hr.hbp.common.api.HrApiResponse;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRDynamicObjectUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.AssignmentMagDeleteKeepOneValidator;
import kd.sdk.hr.hbp.business.helper.timeline.TimelineServiceHelper;

public final class AssignmentMagDeleteOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(AssignmentMagDeleteOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("startdate");
        fieldKeys.add("enddate");
        fieldKeys.add("iscurrentdata");
        fieldKeys.add("assignmentstatus");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new AssignmentMagDeleteKeepOneValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        DynamicObject[] dataEntities = e.getDataEntities();
        List ids = Arrays.stream(dataEntities).map(dy -> dy.getLong("id")).collect(Collectors.toList());
        HrApiResponse timelineHandlerResult = TimelineServiceHelper.deleteTimespansGetChangeInfo((String)this.billEntityType.getName(), ids, (boolean)true);
        TimelineHandlerResult data = (TimelineHandlerResult)timelineHandlerResult.getData();
        List newOrModifyDataList = data.getNewOrModifyDataList();
        Set endDateChangeIds = data.getEndDateChanges().stream().filter(r -> HRDateTimeUtils.isOverSysMaxDateThreshold((Date)r.getAfterEndDate())).map(TimelineHandlerResult.DataChangeInfo::getId).collect(Collectors.toSet());
        List modifyEndDateDataList = newOrModifyDataList.stream().filter(dy -> endDateChangeIds.contains(dy.getLong("id"))).collect(Collectors.toList());
        Set assignmentIds = modifyEndDateDataList.stream().map(dy -> dy.getLong("assignment.id")).collect(Collectors.toSet());
        DynamicObject[] existAssignmentDys = IAssignmentDomainService.getInstance().loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", assignmentIds)});
        Map<Long, DynamicObject> existAssignmentDyMap = Arrays.stream(existAssignmentDys).collect(Collectors.toMap(dy -> dy.getLong("id"), dy -> dy, (v1, v2) -> v1));
        ArrayList assignmentDys = Lists.newArrayListWithCapacity((int)dataEntities.length);
        for (DynamicObject assignmentMagDy : modifyEndDateDataList) {
            long assignmentId = assignmentMagDy.getLong("assignment.id");
            DynamicObject assignmentDy = existAssignmentDyMap.get(assignmentId);
            if (assignmentDy == null) continue;
            HRDynamicObjectUtils.copy((DynamicObject)assignmentMagDy, (DynamicObject)assignmentDy);
            assignmentDy.set("id", (Object)assignmentId);
            assignmentDy.set("enable", (Object)"1");
            assignmentDys.add(assignmentDy);
        }
        if (!assignmentDys.isEmpty()) {
            OperateOption option = OperateOption.create();
            option.mergeValue(this.getOption());
            option.setVariableValue("ishasright", Boolean.TRUE.toString());
            option.setVariableValue("skipCheckDataPermission", Boolean.TRUE.toString());
            option.setVariableValue("ignorewarn", Boolean.TRUE.toString());
            option.setVariableValue("fromAssignmentmag", Boolean.TRUE.toString());
            OperationServiceImpl operationService = new OperationServiceImpl();
            OperationResult operationResult = operationService.localInvokeOperation("save", assignmentDys.toArray(new DynamicObject[0]), option);
            if (!operationResult.isSuccess()) {
                LOG.error("\u7ec4\u7ec7\u5206\u914d\u4fdd\u5b58\u5931\u8d25\uff1a{}", (Object)operationResult.getMessage());
                String errorMsg = operationResult.getAllErrorOrValidateInfo().stream().map(IOperateInfo::getMessage).collect(Collectors.joining(","));
                throw new KDBizException(errorMsg);
            }
        }
    }
}
