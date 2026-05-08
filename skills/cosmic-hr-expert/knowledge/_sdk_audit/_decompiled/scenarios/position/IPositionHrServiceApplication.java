/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.form.IFormView
 *  kd.hr.hbp.common.util.InstanceGenUtil
 *  kd.hrmp.hbpm.business.application.service.impl.PositionHrServiceApplication
 */
package kd.hrmp.hbpm.business.application.service;

import java.util.Date;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.IFormView;
import kd.hr.hbp.common.util.InstanceGenUtil;
import kd.hrmp.hbpm.business.application.service.impl.PositionHrServiceApplication;

public interface IPositionHrServiceApplication {
    public static IPositionHrServiceApplication getInstance() {
        return (IPositionHrServiceApplication)InstanceGenUtil.getSingletonInstance(PositionHrServiceApplication.class);
    }

    public void afterSavePosition(DynamicObject[] var1);

    public DynamicObject[] changePosition(DynamicObject[] var1);

    public void openPositionViewPage(IFormView var1, long var2, Date var4, Map<String, Object> var5);

    public void syncPlatFormPosition(DynamicObject[] var1, boolean var2);

    public void syncUpdateChargePerson(DynamicObject[] var1, DynamicObject[] var2);
}
