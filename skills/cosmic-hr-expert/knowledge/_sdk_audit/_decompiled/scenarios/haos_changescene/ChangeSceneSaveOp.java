/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.haos.business.domain.adminorg.service.impl.ChangeSceneServiceHelper
 *  kd.hr.haos.opplugin.web.validate.ChangeSceneImportValidator
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.haos.business.domain.adminorg.service.impl.ChangeSceneServiceHelper;
import kd.hr.haos.opplugin.web.validate.ChangeSceneImportValidator;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class ChangeSceneSaveOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new ChangeSceneImportValidator());
    }

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("orgchangetype");
        e.getFieldKeys().add("changeoperat");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities;
        if (HRStringUtils.isEmpty((String)((String)this.getOption().getVariables().get("importtype")))) {
            return;
        }
        for (DynamicObject dataEntity : dataEntities = e.getDataEntities()) {
            long changeTypeId = dataEntity.getLong("orgchangetype.id");
            Long changeOperateId = ChangeSceneServiceHelper.getChangeOperate((Long)changeTypeId);
            DynamicObjectCollection collection = dataEntity.getDynamicObjectCollection("changeoperat");
            collection.clear();
            if (changeOperateId == null) continue;
            DynamicObject changeOperateObj = new DynamicObject(collection.getDynamicObjectType());
            changeOperateObj.set("fbasedataid", (Object)changeOperateId);
            collection.add((Object)changeOperateObj);
        }
    }
}
