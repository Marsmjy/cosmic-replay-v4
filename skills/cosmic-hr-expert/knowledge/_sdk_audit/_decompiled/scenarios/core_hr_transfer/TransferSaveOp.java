/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hdm.business.domain.transfer.service.ITransferBillService
 *  kd.hr.hdm.business.domain.transfer.service.ITransferOpenApiSaveParamsService
 *  kd.hr.hdm.business.domain.utils.OperationServiceUtil
 *  kd.hr.hdm.common.transfer.util.TransferPageHelperUtil
 *  kd.hr.hdm.opplugin.transfer.validator.TransferOpenApiValidator
 */
package kd.hr.hdm.opplugin.transfer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hdm.business.domain.transfer.service.ITransferBillService;
import kd.hr.hdm.business.domain.transfer.service.ITransferOpenApiSaveParamsService;
import kd.hr.hdm.business.domain.utils.OperationServiceUtil;
import kd.hr.hdm.common.transfer.util.TransferPageHelperUtil;
import kd.hr.hdm.opplugin.transfer.validator.TransferOpenApiValidator;

public final class TransferSaveOp
extends HRDataBaseOp {
    private static final String CLIENT_API = "api";
    private static final String OPTION_NEED_SUBMIT_EFFECT = "needSubmitEffect";
    private static final String OPTION_ENABLE_OPENAPI_PARAMS = "enableOpenApiParams";
    private static final String OPERATION_SUBMIT_EFFECT = "submiteffect";
    private static final String PARAM_DATA_ENTITIES = "dataEntities";

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.addAll(TransferPageHelperUtil.getInstance().getSingleAllFieldsList());
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        if (this.isFromApi() || this.isEnableOpenApiParamsFromOption()) {
            DynamicObject[] dataEntities = args.getDataEntities();
            HashMap<String, DynamicObject[]> params = new HashMap<String, DynamicObject[]>();
            params.put(PARAM_DATA_ENTITIES, dataEntities);
            List processResults = ITransferOpenApiSaveParamsService.getInstance().setTransferOpenApiSaveParams(params);
            args.addValidator((AbstractValidator)new TransferOpenApiValidator(processResults));
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] dataEntities;
        for (DynamicObject dataEntity : dataEntities = args.getDataEntities()) {
            ITransferBillService.getInstance().setBMainOrg(dataEntity);
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        this.handleApiSubmitEffect(e);
    }

    private void handleApiSubmitEffect(EndOperationTransactionArgs e) {
        if (!this.isFromApi()) {
            return;
        }
        if (!this.isNeedSubmitEffect()) {
            return;
        }
        DynamicObject[] dataEntities = e.getDataEntities();
        Long[] ids = this.extractIds(dataEntities);
        OperationServiceUtil.localInvokeOperation((Long[])ids, (String)"hdm_transferapply", (String)OPERATION_SUBMIT_EFFECT, (boolean)false);
    }

    private boolean isFromApi() {
        return CLIENT_API.equals(RequestContext.get().getClient());
    }

    private boolean isEnableOpenApiParamsFromOption() {
        String enableOpenApiParams = this.getOption() != null && this.getOption().containsVariable(OPTION_ENABLE_OPENAPI_PARAMS) ? this.getOption().getVariableValue(OPTION_ENABLE_OPENAPI_PARAMS) : "";
        return HRStringUtils.equals((String)enableOpenApiParams, (String)"true");
    }

    private boolean isNeedSubmitEffect() {
        String needSubmitEffect = this.getOption() != null && this.getOption().containsVariable(OPTION_NEED_SUBMIT_EFFECT) ? this.getOption().getVariableValue(OPTION_NEED_SUBMIT_EFFECT) : "";
        return HRStringUtils.equals((String)needSubmitEffect, (String)"true");
    }

    private Long[] extractIds(DynamicObject[] dataEntities) {
        return (Long[])Arrays.stream(dataEntities).map(entity -> (Long)entity.get("id")).toArray(Long[]::new);
    }
}
