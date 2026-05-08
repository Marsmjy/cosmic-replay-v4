/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.hr.hbp.common.util.InstanceGenUtil
 *  kd.hrmp.hbpm.business.application.service.impl.PositionRelationServiceApplicationImpl
 */
package kd.hrmp.hbpm.business.application.service;

import java.util.List;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.hbp.common.util.InstanceGenUtil;
import kd.hrmp.hbpm.business.application.service.impl.PositionRelationServiceApplicationImpl;

public interface IPositionRelationServiceApplication {
    public static IPositionRelationServiceApplication getInstance() {
        return (IPositionRelationServiceApplication)InstanceGenUtil.getSingletonInstance(PositionRelationServiceApplicationImpl.class);
    }

    public void saveSysRelation(List<DynamicObject> var1);

    public void saveCooperationRelation(List<DynamicObject> var1);

    public void changeCooperationRelation(List<DynamicObject> var1);

    public void deleteRelationByPosition(Set<Long> var1);

    public void deleteRelation(DynamicObject[] var1);
}
