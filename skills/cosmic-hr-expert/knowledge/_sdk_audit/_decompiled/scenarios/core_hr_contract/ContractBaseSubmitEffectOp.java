/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DataEntityBase
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hlcm.business.domian.repository.CommonRepository
 *  kd.hr.hlcm.business.domian.repository.ContractSignApplyRepository
 *  kd.hr.hlcm.business.domian.service.signmgt.ISignManageService
 *  kd.hr.hlcm.business.domian.utils.ContractSignUtils
 *  kd.hr.hlcm.business.domian.utils.EntityMetadataUtils
 *  kd.hr.hlcm.opplugin.validator.CancelBillCannotSubmitEffectValidator
 *  kd.hr.hlcm.opplugin.validator.CancelProtocolMustInputValidator
 *  kd.hr.hlcm.opplugin.validator.ContractApplyEntryMustInputValidator
 *  kd.hr.hlcm.opplugin.validator.ContractBaseSaveValidator
 *  kd.hr.hlcm.opplugin.validator.ContractTemplatePropValidator
 *  kd.hr.hlcm.opplugin.validator.MatchSignWayValidator
 *  kd.hr.hlcm.opplugin.validator.MuchValueValidator
 *  kd.hr.hlcm.opplugin.validator.NewSignRangesOverlapValidator
 *  kd.sdk.hr.hlcm.common.enums.BillStatusEnum
 *  kd.sdk.hr.hlcm.common.enums.BusinessStatusEnum
 *  kd.sdk.hr.hlcm.common.enums.HandleStatusEnum
 *  kd.sdk.hr.hlcm.common.enums.SignStatusEnum
 */
package kd.hr.hlcm.opplugin.contract;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DataEntityBase;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hlcm.business.domian.repository.CommonRepository;
import kd.hr.hlcm.business.domian.repository.ContractSignApplyRepository;
import kd.hr.hlcm.business.domian.service.signmgt.ISignManageService;
import kd.hr.hlcm.business.domian.utils.ContractSignUtils;
import kd.hr.hlcm.business.domian.utils.EntityMetadataUtils;
import kd.hr.hlcm.opplugin.validator.CancelBillCannotSubmitEffectValidator;
import kd.hr.hlcm.opplugin.validator.CancelProtocolMustInputValidator;
import kd.hr.hlcm.opplugin.validator.ContractApplyEntryMustInputValidator;
import kd.hr.hlcm.opplugin.validator.ContractBaseSaveValidator;
import kd.hr.hlcm.opplugin.validator.ContractTemplatePropValidator;
import kd.hr.hlcm.opplugin.validator.MatchSignWayValidator;
import kd.hr.hlcm.opplugin.validator.MuchValueValidator;
import kd.hr.hlcm.opplugin.validator.NewSignRangesOverlapValidator;
import kd.sdk.hr.hlcm.common.enums.BillStatusEnum;
import kd.sdk.hr.hlcm.common.enums.BusinessStatusEnum;
import kd.sdk.hr.hlcm.common.enums.HandleStatusEnum;
import kd.sdk.hr.hlcm.common.enums.SignStatusEnum;

public final class ContractBaseSubmitEffectOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        List fieldKeys = evt.getFieldKeys();
        fieldKeys.addAll(EntityMetadataUtils.getEntityFields((String)"hlcm_contractapplybase"));
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new ContractApplyEntryMustInputValidator());
        args.addValidator((AbstractValidator)new CancelBillCannotSubmitEffectValidator());
        args.addValidator((AbstractValidator)new MatchSignWayValidator());
        args.addValidator((AbstractValidator)new CancelProtocolMustInputValidator());
        args.addValidator((AbstractValidator)new ContractBaseSaveValidator());
        args.addValidator((AbstractValidator)new NewSignRangesOverlapValidator());
        args.addValidator((AbstractValidator)new MuchValueValidator());
        args.addValidator((AbstractValidator)new ContractTemplatePropValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        Arrays.stream(dataEntities).forEach(dynamicObject -> {
            dynamicObject.set("handlestatus", (Object)HandleStatusEnum.ARCHIVE.getCombKey());
            dynamicObject.set("businessstatus", (Object)BusinessStatusEnum.ARCHIVE.getCombKey());
            dynamicObject.set("billstatus", (Object)BillStatusEnum.STATUS_PASS.getCode());
            dynamicObject.set("signstatus", (Object)SignStatusEnum.COMPLETE_SIGN.getCombKey());
            ContractSignApplyRepository.getInstance().setContractCode(dynamicObject);
            ContractSignUtils.HandleEntryInfo((DynamicObject)dynamicObject);
            ContractSignUtils.setSignTemplateHis((DynamicObject)dynamicObject);
            ContractSignUtils.setPeriodUnit((DynamicObject)dynamicObject);
        });
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        DynamicObject[] dynamicObjects = CommonRepository.queryByIds((String)"hlcm_contractapplybase", null, Arrays.stream(dataEntities).map(DataEntityBase::getPkValue).map(Object::toString).map(Long::valueOf).collect(Collectors.toList()));
        DynamicObject[] filterDys = Arrays.stream(dynamicObjects).filter(dataEntity -> HRObjectUtils.isEmpty((Object)dataEntity.get("actualsigndate"))).collect(Collectors.toList()).toArray(new DynamicObject[0]);
        ISignManageService.getInstance().batchReplaceKeywordGenContract(dynamicObjects, false);
        if (filterDys.length > 0) {
            Arrays.stream(filterDys).forEach(dataEntity -> dataEntity.set("actualsigndate", dataEntity.get("signeddate")));
        }
        CommonRepository.updateDynamicObject((String)"hlcm_contractapplybase", (DynamicObject[])dynamicObjects);
        ISignManageService.getInstance().archiveAfterTransaction(e.getDataEntities());
    }
}
