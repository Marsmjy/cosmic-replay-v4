/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplTypeRepository
 */
package kd.hrmp.hbpm.opplugin.web.position;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplTypeRepository;
import kd.hrmp.hbpm.opplugin.web.basedata.CtrlStrategyValidator;
import kd.hrmp.hbpm.opplugin.web.position.validate.PositionTplCommonValidator;
import kd.hrmp.hbpm.opplugin.web.position.validate.PositionTplTypeIndexUniqueValidator;

public final class PositionTplTypeSaveOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs evt) {
        super.onAddValidators(evt);
        evt.addValidator((AbstractValidator)new PositionTplCommonValidator());
        evt.addValidator((AbstractValidator)new PositionTplTypeIndexUniqueValidator());
        evt.addValidator((AbstractValidator)new CtrlStrategyValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities;
        int largestIndex = this.getLargestIndex();
        for (DynamicObject dataEntity : dataEntities = e.getDataEntities()) {
            DynamicProperty indexProp = dataEntity.getDynamicObjectType().getProperty("index");
            Object indexPropValue = dataEntity.getDataStorage().getLocalValue((IDataEntityProperty)indexProp);
            if (!HRObjectUtils.isEmpty((Object)indexPropValue)) continue;
            dataEntity.set("index", (Object)(largestIndex += 10));
        }
    }

    private Integer getLargestIndex() {
        DynamicObject[] dynArr = PositionTplTypeRepository.getInstance().getAllDyn("index");
        int largestIndex = 0;
        for (DynamicObject dyn : dynArr) {
            int index = dyn.getInt("index");
            largestIndex = Math.max(largestIndex, index);
        }
        return largestIndex;
    }
}

