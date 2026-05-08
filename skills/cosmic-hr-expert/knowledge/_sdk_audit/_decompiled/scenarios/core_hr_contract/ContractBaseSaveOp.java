/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService
 *  kd.hr.hlcm.business.domian.repository.CommonRepository
 *  kd.hr.hlcm.business.domian.repository.ContractSignApplyRepository
 *  kd.hr.hlcm.business.domian.utils.ContractSignUtils
 *  kd.hr.hlcm.business.domian.utils.EntityMetadataUtils
 *  kd.hr.hlcm.business.domian.utils.HLCMImportUtils
 *  kd.hr.hlcm.business.domian.utils.HRSignImportCommonUtils
 *  kd.hr.hlcm.common.entity.ErManQueryParam
 *  kd.hr.hlcm.common.entity.ErManQueryResult
 *  kd.hr.hlcm.common.entity.PersonInfo
 *  kd.hr.hlcm.opplugin.validator.BatchSignValidator
 *  kd.hr.hlcm.opplugin.validator.CancelProtocolMustInputValidator
 *  kd.hr.hlcm.opplugin.validator.ContractApplyEntryMustInputValidator
 *  kd.hr.hlcm.opplugin.validator.ContractBaseSaveValidator
 *  kd.hr.hlcm.opplugin.validator.ContractTemplatePropValidator
 *  kd.hr.hlcm.opplugin.validator.IDCardNumberValidator
 *  kd.hr.hlcm.opplugin.validator.IsInitOrPaperDataValidator
 *  kd.hr.hlcm.opplugin.validator.IsMaintainElectricSealValidator
 *  kd.hr.hlcm.opplugin.validator.MatchSignWayValidator
 *  kd.hr.hlcm.opplugin.validator.MuchValueValidator
 *  kd.hr.hlcm.opplugin.validator.MustMainContractValidator
 *  kd.hr.hlcm.opplugin.validator.NewSignRangesOverlapValidator
 *  kd.hr.hlcm.opplugin.validator.SignDateCannotBeforeStartDateValidator
 *  kd.hr.hlcm.opplugin.validator.common.cancel.EmpNumberSameConAndYGCancelValidator
 *  kd.sdk.hr.hlcm.common.enums.BusinessTypeEnum
 *  kd.sdk.hr.hlcm.common.enums.HandleStatusEnum
 */
package kd.hr.hlcm.opplugin.contract;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService;
import kd.hr.hlcm.business.domian.repository.CommonRepository;
import kd.hr.hlcm.business.domian.repository.ContractSignApplyRepository;
import kd.hr.hlcm.business.domian.utils.ContractSignUtils;
import kd.hr.hlcm.business.domian.utils.EntityMetadataUtils;
import kd.hr.hlcm.business.domian.utils.HLCMImportUtils;
import kd.hr.hlcm.business.domian.utils.HRSignImportCommonUtils;
import kd.hr.hlcm.common.entity.ErManQueryParam;
import kd.hr.hlcm.common.entity.ErManQueryResult;
import kd.hr.hlcm.common.entity.PersonInfo;
import kd.hr.hlcm.opplugin.validator.BatchSignValidator;
import kd.hr.hlcm.opplugin.validator.CancelProtocolMustInputValidator;
import kd.hr.hlcm.opplugin.validator.ContractApplyEntryMustInputValidator;
import kd.hr.hlcm.opplugin.validator.ContractBaseSaveValidator;
import kd.hr.hlcm.opplugin.validator.ContractTemplatePropValidator;
import kd.hr.hlcm.opplugin.validator.IDCardNumberValidator;
import kd.hr.hlcm.opplugin.validator.IsInitOrPaperDataValidator;
import kd.hr.hlcm.opplugin.validator.IsMaintainElectricSealValidator;
import kd.hr.hlcm.opplugin.validator.MatchSignWayValidator;
import kd.hr.hlcm.opplugin.validator.MuchValueValidator;
import kd.hr.hlcm.opplugin.validator.MustMainContractValidator;
import kd.hr.hlcm.opplugin.validator.NewSignRangesOverlapValidator;
import kd.hr.hlcm.opplugin.validator.SignDateCannotBeforeStartDateValidator;
import kd.hr.hlcm.opplugin.validator.common.cancel.EmpNumberSameConAndYGCancelValidator;
import kd.sdk.hr.hlcm.common.enums.BusinessTypeEnum;
import kd.sdk.hr.hlcm.common.enums.HandleStatusEnum;

public final class ContractBaseSaveOp
extends HRDataBaseOp {
    private static final String FORM_MSERVICE = "formMService";
    private static final String ORG_NUMBER = "orgmodel.number";
    private static final String POSITION_NUMBER = "positionmodel.number";
    private static final String JOB_NUMBER = "job.number";

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        List fieldKeys = evt.getFieldKeys();
        fieldKeys.addAll(EntityMetadataUtils.getEntityFields((String)"hlcm_contractapplybase"));
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        this.setValue(args.getDataEntities());
        args.addValidator((AbstractValidator)new ContractApplyEntryMustInputValidator());
        args.addValidator((AbstractValidator)new IsInitOrPaperDataValidator());
        args.addValidator((AbstractValidator)new ContractBaseSaveValidator());
        args.addValidator((AbstractValidator)new MustMainContractValidator());
        args.addValidator((AbstractValidator)new NewSignRangesOverlapValidator());
        args.addValidator((AbstractValidator)new IDCardNumberValidator());
        args.addValidator((AbstractValidator)new MuchValueValidator());
        args.addValidator((AbstractValidator)new CancelProtocolMustInputValidator());
        args.addValidator((AbstractValidator)new MatchSignWayValidator());
        args.addValidator((AbstractValidator)new SignDateCannotBeforeStartDateValidator());
        args.addValidator((AbstractValidator)new BatchSignValidator());
        args.addValidator((AbstractValidator)new ContractTemplatePropValidator());
        args.addValidator((AbstractValidator)new IsMaintainElectricSealValidator());
        EmpNumberSameConAndYGCancelValidator empNumberSameConAndYGCancelValidator = new EmpNumberSameConAndYGCancelValidator();
        empNumberSameConAndYGCancelValidator.setAddBillNoForContent(false);
        args.addValidator((AbstractValidator)empNumberSameConAndYGCancelValidator);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        Arrays.stream(dataEntities).forEach(dynamicObject -> {
            if (HRObjectUtils.isEmpty((Object)dynamicObject.get("handlestatus"))) {
                dynamicObject.set("handlestatus", (Object)HandleStatusEnum.TOSUBMIT.getCombKey());
            }
            if (HRStringUtils.equals((String)dynamicObject.getString("handlestatus"), (String)HandleStatusEnum.ARCHIVE.getCombKey())) {
                dynamicObject.set("handlestatus", (Object)HandleStatusEnum.TOSUBMIT.getCombKey());
                dynamicObject.set("businessstatus", (Object)" ");
                dynamicObject.set("signstatus", (Object)" ");
                dynamicObject.set("auditor", null);
                dynamicObject.set("auditdate", null);
            }
            if (HRStringUtils.equals((String)"4", (String)dynamicObject.getString("businesstype"))) {
                DynamicObject cancelContract = dynamicObject.getDynamicObject("oldcontract");
                dynamicObject.set("actualsigncompany", (Object)cancelContract.getDynamicObject("actualsigncompany"));
                dynamicObject.set("actualsigncompanyhis", (Object)cancelContract.getDynamicObject("actualsigncompanyhis"));
                dynamicObject.set("periodtype", (Object)cancelContract.getDynamicObject("periodtype"));
                dynamicObject.set("periodunit", (Object)cancelContract.getString("periodunit"));
                dynamicObject.set("period", cancelContract.get("period"));
                dynamicObject.set("enddate", (Object)cancelContract.getDate("enddate"));
                dynamicObject.set("startdate", (Object)cancelContract.getDate("startdate"));
            }
            if (HRObjectUtils.isEmpty((Object)dynamicObject.get("period"))) {
                dynamicObject.set("period", (Object)BigDecimal.ZERO);
            }
            ContractSignApplyRepository.getInstance().setContractCode(dynamicObject);
            ContractSignUtils.setPeriodUnit((DynamicObject)dynamicObject);
            ContractSignUtils.HandleEntryInfo((DynamicObject)dynamicObject);
            ContractSignUtils.setSignTemplateHis((DynamicObject)dynamicObject);
            Map variables = this.getOption().getVariables();
            if (variables.containsKey(FORM_MSERVICE) && "true".equals(variables.get(FORM_MSERVICE))) {
                Map<ErManQueryParam, PersonInfo> personInfoByEmpNumber = this.getPersonInfoByErManFileId((DynamicObject)dynamicObject);
                Map personInfoMap = HRSignImportCommonUtils.getPersonInfoMap(personInfoByEmpNumber);
                long currUserId = RequestContext.get().getCurrUserId();
                DynamicObject opPerson = CommonRepository.queryById((String)"bos_user", (String)"id,name", (Long)currUserId);
                long pid = dynamicObject.getLong("employee.id");
                PersonInfo personInfo = (PersonInfo)personInfoMap.get(pid);
                if (personInfo != null) {
                    HRSignImportCommonUtils.setErmanfileInfo((DynamicObject)dynamicObject, (Map)personInfoMap);
                    HRSignImportCommonUtils.setPersonInfo((DynamicObject)dynamicObject, (PersonInfo)personInfo, (DynamicObject)opPerson);
                }
            }
        });
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        SyncStartStatusService.getInstance().syncSignBillHandleStatusToPreWarn(dataEntities);
        SyncStartStatusService.getInstance().syncHirePersonStartStatus(dataEntities);
    }

    private Map<ErManQueryParam, PersonInfo> getPersonInfoByErManFileId(DynamicObject dynamicObject) {
        String empNumber = dynamicObject.getString("empnumber");
        String orgNumber = dynamicObject.getString(ORG_NUMBER);
        String positionNumber = dynamicObject.getString(POSITION_NUMBER);
        String jobNumber = dynamicObject.getString(JOB_NUMBER);
        ErManQueryParam erManQueryParam = new ErManQueryParam(empNumber, orgNumber, positionNumber, jobNumber, 1);
        HLCMImportUtils.queryInitParam(Collections.singletonList(erManQueryParam));
        HashMap<ErManQueryParam, ErManQueryResult> erManQueryParamErManQueryResultMap = new HashMap<ErManQueryParam, ErManQueryResult>(16);
        erManQueryParamErManQueryResultMap.putAll(HLCMImportUtils.queryInitParam(Collections.singletonList(erManQueryParam)));
        HashMap result = new HashMap();
        erManQueryParamErManQueryResultMap.forEach((param, erManQueryResult) -> result.put(erManQueryResult.getAssignmentId(), param));
        String entityId = dynamicObject.getDataEntityType().getName();
        return HLCMImportUtils.getPersonInfoByAssignmentId((String)entityId, result);
    }

    private void setValue(DynamicObject[] dataEntities) {
        for (DynamicObject dataEntity : dataEntities) {
            if (!HRStringUtils.equals((String)dataEntity.getString("businesstype"), (String)BusinessTypeEnum.CANCEL.getCombKey())) {
                return;
            }
            DynamicObjectCollection entryEntities = dataEntity.getDynamicObjectCollection("entryentity");
            if (CollectionUtils.isEmpty((Collection)entryEntities)) continue;
            entryEntities.forEach(entryEntity -> entryEntity.set("entrycontracttype", dataEntity.get("contracttype")));
        }
    }
}
