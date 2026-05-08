/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.hr.haos.business.domain.adminorg.service.impl.ChangeSceneServiceHelper
 *  kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit
 */
package kd.hr.haos.formplugin.web.database;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.haos.business.domain.adminorg.service.impl.ChangeSceneServiceHelper;
import kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit;

public final class ChangeSceneEditPlugin
extends HRCoreBaseBillEdit {
    private static final String ORGCHANGETYPE = "orgchangetype";

    public void propertyChanged(PropertyChangedArgs e) {
        String name = e.getProperty().getName();
        if (ORGCHANGETYPE.equals(name)) {
            DynamicObject dynamicObject = (DynamicObject)this.getModel().getValue(ORGCHANGETYPE);
            if (dynamicObject == null) {
                return;
            }
            Long id = dynamicObject.getLong("id");
            Long changeOperateId = ChangeSceneServiceHelper.getChangeOperate((Long)id);
            if (changeOperateId != null) {
                this.getModel().setValue("changeoperat", (Object)new Object[]{changeOperateId});
            } else {
                this.getModel().setValue("changeoperat", null);
            }
        }
    }
}
