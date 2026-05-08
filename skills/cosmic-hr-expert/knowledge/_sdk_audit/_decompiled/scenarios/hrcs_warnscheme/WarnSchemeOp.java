/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.ORM
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.WarnJobAndPlanService
 *  kd.hr.hrcs.opplugin.validator.earlywarn.WarnSchemeValidator
 */
package kd.hr.hrcs.opplugin.web.earlywarn;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.ORM;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.WarnJobAndPlanService;
import kd.hr.hrcs.opplugin.validator.earlywarn.WarnSchemeValidator;

@ExcludeFromJacocoGeneratedReport
public final class WarnSchemeOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new WarnSchemeValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] dynamicObjects = args.getDataEntities();
        if (args.getOperationKey().equals("save")) {
            this.createJobAndPlan(dynamicObjects);
        }
    }

    private void createJobAndPlan(DynamicObject[] dynamicObjects) {
        if (dynamicObjects == null || dynamicObjects.length != 1) {
            return;
        }
        DynamicObject dynamicObject = dynamicObjects[0];
        String entityNumber = dynamicObject.getDynamicObjectType().getName();
        Long schemeId = dynamicObject.getLong("id");
        if (schemeId == null || schemeId == 0L) {
            schemeId = ORM.create().genLongId(entityNumber);
            dynamicObject.set("id", (Object)schemeId);
        }
        WarnJobAndPlanService.getInstance().createJobAndPlan(dynamicObject);
    }
}
