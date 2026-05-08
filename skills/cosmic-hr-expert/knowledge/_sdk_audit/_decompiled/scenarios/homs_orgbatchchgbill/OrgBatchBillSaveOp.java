/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.opplugin.web.HRCoreBaseBillOp
 *  kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBillSaveHelper
 *  kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveAndSubmitValidator
 *  kd.hr.homs.opplugin.web.orgbatch.validator.AdminOrgBillSaveValidator
 */
package kd.hr.homs.opplugin.web.orgbatch;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRCoreBaseBillOp;
import kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBillSaveHelper;
import kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveAndSubmitValidator;
import kd.hr.homs.opplugin.web.orgbatch.validator.AdminOrgBillSaveValidator;

public final class OrgBatchBillSaveOp
extends HRCoreBaseBillOp {
    private static final Log logger = LogFactory.getLog(OrgBatchBillSaveOp.class);

    public void onAddValidators(AddValidatorsEventArgs e) {
        logger.info("OrgBatchBillSaveOp.onAddValidators start.");
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new OrgBatchBillSaveAndSubmitValidator());
        e.addValidator((AbstractValidator)new AdminOrgBillSaveValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        logger.info("OrgBatchBillSaveOp.beginOperationTransaction start.");
        if (e.getDataEntities().length == 0) {
            return;
        }
        DynamicObject dataEntity = e.getDataEntities()[0];
        OrgBatchBillSaveHelper.getInstance().orgBatchBillSave(dataEntity);
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        Long billId = e.getDataEntities()[0].getLong("id");
        HRBaseServiceHelper batchOrgEntityHelper = new HRBaseServiceHelper("homs_batchorgentity");
        QFilter adminOrgIdFilter = new QFilter("creator", "=", (Object)0);
        QFilter billIdFilter = new QFilter("billid", "=", (Object)billId);
        adminOrgIdFilter.and(billIdFilter);
        batchOrgEntityHelper.deleteByFilter(adminOrgIdFilter.toArray());
    }
}
