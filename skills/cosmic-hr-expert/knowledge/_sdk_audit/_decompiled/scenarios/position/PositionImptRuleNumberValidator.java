/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.validate.ValidateResultCollection
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.opplugin.validator.HRDataBaseValidator
 *  kd.hrmp.hbpm.business.domain.position.service.impl.PositionCodeRuleHelper
 */
package kd.hrmp.hbpm.opplugin.web.position.validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.ValidateResultCollection;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.opplugin.validator.HRDataBaseValidator;
import kd.hrmp.hbpm.business.domain.position.service.impl.PositionCodeRuleHelper;

public class PositionImptRuleNumberValidator
extends HRDataBaseValidator {
    public void validate() {
        PositionCodeRuleHelper.recycleNumber((DynamicObject[])((DynamicObject[])this.getFalseExtendedDataEntity().stream().map(ExtendedDataEntity::getDataEntity).toArray(DynamicObject[]::new)));
    }

    private List<ExtendedDataEntity> getFalseExtendedDataEntity() {
        ValidateResultCollection resultCollection = this.getValidateContext().getValidateResults();
        if (resultCollection == null) {
            return new ArrayList<ExtendedDataEntity>();
        }
        HashSet falseSet = this.getValidateContext().getValidateResults().getErrorDataIndexs();
        ExtendedDataEntity[] extendedDataEntities = this.getDataEntities();
        if (!CollectionUtils.isEmpty((Collection)falseSet)) {
            ArrayList<ExtendedDataEntity> falseList = new ArrayList<ExtendedDataEntity>(falseSet.size());
            falseSet.forEach(id -> falseList.add(extendedDataEntities[id]));
            return falseList;
        }
        return new ArrayList<ExtendedDataEntity>();
    }
}
