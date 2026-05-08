/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.haos.business.application.service.IStructProjectApplication
 *  kd.hr.haos.business.application.service.impl.StructProjectApplicationImpl
 *  kd.hr.haos.opplugin.web.otherframework.validator.StructProjectDeleteValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.otherframework;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.haos.business.application.service.IStructProjectApplication;
import kd.hr.haos.business.application.service.impl.StructProjectApplicationImpl;
import kd.hr.haos.opplugin.web.otherframework.validator.StructProjectDeleteValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructProjectDeleteOP
extends HRDataBaseOp {
    private IStructProjectApplication structProjectApplication = new StructProjectApplicationImpl();
    private static final String HRMP_HAOS_OPPLUGIN = "hrmp-haos-opplugin";

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new StructProjectDeleteValidator());
    }

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("enable");
        e.getFieldKeys().add("rootorg");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs event) {
        DynamicObject[] dataEntities = event.getDataEntities();
        ArrayList needToDeleteData = Lists.newArrayListWithExpectedSize((int)16);
        for (DynamicObject dataEntity : dataEntities) {
            if (!"10".equals(dataEntity.getString("enable"))) continue;
            needToDeleteData.add(dataEntity);
        }
        DynamicObject[] dyToDel = needToDeleteData.toArray(new DynamicObject[0]);
        this.structProjectApplication.doWithDelete(dyToDel);
    }
}
