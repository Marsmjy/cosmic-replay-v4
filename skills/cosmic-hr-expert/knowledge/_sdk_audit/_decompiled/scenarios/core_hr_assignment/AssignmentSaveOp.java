/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.operate.result.IOperateInfo
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.plugin.args.ReturnOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IAssignmentApplicationService
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.AssignmentMagSaveValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.operate.result.IOperateInfo;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.plugin.args.ReturnOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IAssignmentApplicationService;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.AssignmentMagSaveValidator;

public final class AssignmentSaveOp
extends HRDataBaseOp {
    private static final String SUCCESS = "success";

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("employee");
        fieldKeys.add("startdate");
        fieldKeys.add("isprimary");
        fieldKeys.add("primaryassignment");
        fieldKeys.add("assignmentstatus");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        e.addValidator((AbstractValidator)new AssignmentMagSaveValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        Map variables = this.getOption().getVariables();
        String numbers = (String)variables.get("number");
        if (numbers != null) {
            JSONObject idNumberObj = JSON.parseObject((String)numbers);
            for (DynamicObject dataEntity : e.getDataEntities()) {
                long id = dataEntity.getLong("id");
                String number = idNumberObj.getString(String.valueOf(id));
                if (dataEntity.getDataEntityState().getFromDatabase() || !HRStringUtils.isNotEmpty((String)number)) continue;
                dataEntity.set("number", (Object)number);
            }
        }
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
        Map variables = this.getOption().getVariables();
        if (!Boolean.TRUE.toString().equals(variables.get("fromAssignmentmag"))) {
            e.setCancel(true);
            Map retMap = IAssignmentApplicationService.getInstance().localInvokeOperationSave(e.getDataEntities(), this.getOption());
            OperationResult operationResult = (OperationResult)retMap.get("result");
            if (!operationResult.isSuccess()) {
                String errorMsg = operationResult.getAllErrorOrValidateInfo().stream().map(IOperateInfo::getMessage).collect(Collectors.joining(","));
                throw new KDBizException(errorMsg);
            }
            Map customData = this.operationResult.getCustomData();
            customData.put(SUCCESS, Boolean.TRUE.toString());
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        Set modifyIds = IAssignmentApplicationService.getInstance().savePrimaryAssignment(dataEntities);
        this.getOption().setVariableValue("genericEntityModifyExtIds", SerializationUtils.toJsonString((Object)modifyIds));
    }

    public void onReturnOperation(ReturnOperationArgs e) {
        super.onReturnOperation(e);
        OperationResult result = (OperationResult)e.getOperationResult();
        Map customData = result.getCustomData();
        if (HRStringUtils.equals((String)((String)customData.get(SUCCESS)), (String)Boolean.TRUE.toString())) {
            result.setSuccess(true);
            result.setMessage(null);
        }
    }
}
