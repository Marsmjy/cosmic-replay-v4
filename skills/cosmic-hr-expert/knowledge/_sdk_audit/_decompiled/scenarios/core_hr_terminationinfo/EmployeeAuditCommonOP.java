/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.service.operation.validate.BillExistsValidator
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.history.EntityInheritTypeEnum
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.sdk.annotation.SdkPublic
 *  kd.sdk.hr.hspm.business.internal.approval.ApprovalBillSupport
 *  kd.sdk.hr.hspm.business.internal.approval.EmployeeAuditOperationContext
 *  kd.sdk.hr.hspm.business.internal.approval.PictureAuditSupport
 *  kd.sdk.hr.hspm.business.internal.approval.strategy.AuditStrategyFactory
 *  kd.sdk.hr.hspm.business.internal.approval.strategy.IAuditStrategy
 *  kd.sdk.hr.hspm.common.constants.DynConfigConstants
 *  kd.sdk.hr.hspm.common.entity.approval.CompareChangeEntity
 *  kd.sdk.hr.hspm.opplugin.reform.validate.EmployeeAuditRejectDataValidator
 *  kd.sdk.hr.hspm.opplugin.reform.validate.EmployeeAuditValidator
 */
package kd.sdk.hr.hspm.opplugin.reform;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.service.operation.validate.BillExistsValidator;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.history.EntityInheritTypeEnum;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.sdk.annotation.SdkPublic;
import kd.sdk.hr.hspm.business.internal.approval.ApprovalBillSupport;
import kd.sdk.hr.hspm.business.internal.approval.EmployeeAuditOperationContext;
import kd.sdk.hr.hspm.business.internal.approval.PictureAuditSupport;
import kd.sdk.hr.hspm.business.internal.approval.strategy.AuditStrategyFactory;
import kd.sdk.hr.hspm.business.internal.approval.strategy.IAuditStrategy;
import kd.sdk.hr.hspm.common.constants.DynConfigConstants;
import kd.sdk.hr.hspm.common.entity.approval.CompareChangeEntity;
import kd.sdk.hr.hspm.opplugin.reform.validate.EmployeeAuditRejectDataValidator;
import kd.sdk.hr.hspm.opplugin.reform.validate.EmployeeAuditValidator;

@SdkPublic
public final class EmployeeAuditCommonOP
extends HRDataBaseOp
implements DynConfigConstants {
    private static final Log LOG = LogFactory.getLog(EmployeeAuditCommonOP.class);
    private EmployeeAuditOperationContext auditContext;
    private IAuditStrategy auditStrategy;

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().addAll(this.billEntityType.getAllFields().keySet());
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.getValidators().removeIf(BillExistsValidator.class::isInstance);
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new EmployeeAuditValidator());
        args.addValidator((AbstractValidator)new EmployeeAuditRejectDataValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs beforeOperationArgs) {
        DynamicObject[] dataEntities = beforeOperationArgs.getDataEntities();
        EntityInheritTypeEnum entityInheritTypeEnum = ApprovalBillSupport.getEntityInheritTypeEnum((String)dataEntities[0].getDataEntityType().getName());
        String opKey = beforeOperationArgs.getOperationKey();
        this.getOption().setVariableValue("logBizCustomVal", "employee");
        String commonTpl = entityInheritTypeEnum == null ? null : entityInheritTypeEnum.getType();
        this.auditContext = new EmployeeAuditOperationContext(commonTpl, dataEntities, opKey, this.getOption());
        if (!EntityInheritTypeEnum.COMMON_TPL.getType().equals(commonTpl) && "saveatt".equals(opKey)) {
            this.hisModelRemoveAttOpt(dataEntities[0]);
        }
        new PictureAuditSupport().preventRemovePic(dataEntities[0], this.getOption(), new HashSet(this.auditContext.getConfigFieldList()));
        this.preventRemoveAttachmentField(beforeOperationArgs);
        this.auditContext.setAttachmentPanelInfo(this.getOption().getVariableValue("attachmentpanel", ""));
        this.auditStrategy = AuditStrategyFactory.getAuditStrategy((EmployeeAuditOperationContext)this.auditContext);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs beginOperationTransactionArgs) {
        beginOperationTransactionArgs.setCancelOperation(true);
        OperationResult operationResult = this.auditStrategy.doAudit();
        String error = ApprovalBillSupport.handleResultError((OperationResult)operationResult);
        if (HRStringUtils.isNotEmpty((String)error)) {
            throw new KDBizException(error);
        }
    }

    private void preventRemoveAttachmentField(BeforeOperationArgs beforeOperationArgs) {
        String opKey = beforeOperationArgs.getOperationKey();
        if (!"deleteatt".equals(opKey)) {
            return;
        }
        String attFieldMap = this.getOption().getVariableValue("deleteAttFieldIdsMap", null);
        if (HRStringUtils.isEmpty((String)attFieldMap)) {
            return;
        }
        List auditFieldList = this.auditContext.getAuditFieldList();
        if (!CollectionUtils.isEmpty((Collection)auditFieldList)) {
            this.getOption().removeVariable("deleteAttFieldIdsMap");
        }
    }

    private void hisModelRemoveAttOpt(DynamicObject dataEntity) {
        long id = dataEntity.getLong("id");
        if (id != 0L) {
            HRBaseServiceHelper entityServiceHelper = this.auditContext.getEntityServiceHelper();
            DynamicObject dbDy = entityServiceHelper.loadDynamicObject(new QFilter[]{new QFilter("id", "=", (Object)id)});
            if (dbDy != null) {
                CompareChangeEntity compareChangeEntity = new CompareChangeEntity(dataEntity, dbDy, this.auditContext.getAuditFieldList(), this.auditContext.getConfigFieldList(), false);
                compareChangeEntity.setAuditFields(new HashSet(this.auditContext.getAuditFieldList()));
                ApprovalBillSupport.checkNotAuditFieldChanged((CompareChangeEntity)compareChangeEntity);
                if (!CollectionUtils.isEmpty((Collection)compareChangeEntity.getChangeFields())) {
                    LOG.info("auditBill#remove_attachmentpanel_data-->{}", (Object)id);
                    this.getOption().removeVariable("attachmentpanel");
                }
            }
        }
    }
}
