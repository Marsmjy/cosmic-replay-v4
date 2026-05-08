/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  kd.bos.basedataref.BaseDataCheckRefrence
 *  kd.bos.basedataref.BaseDataCheckRefrenceResult
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.BasedataEntityType
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.hr.haos.common.constants.AdminOrgTypeStdEnum
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.haos.formplugin.web.database;

import java.util.EventObject;
import kd.bos.basedataref.BaseDataCheckRefrence;
import kd.bos.basedataref.BaseDataCheckRefrenceResult;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.BasedataEntityType;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.haos.common.constants.AdminOrgTypeStdEnum;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class AdminorgtypeEditPlugin
extends HRDataBaseEdit {
    private static final String ADMIN_ORG_TYPE_STD = "adminorgtypestd";

    public void beforeBindData(EventObject e) {
        HRBaseServiceHelper serviceHelper;
        DynamicObject dynamicObject;
        BasedataEntityType basedataEntityType;
        BaseDataCheckRefrenceResult result;
        super.beforeBindData(e);
        DynamicObject dataEntity = this.getModel().getDataEntity();
        Long id = dataEntity.getLong("id");
        if (id != 0L && (result = AdminorgtypeEditPlugin.baseDataCheckReference(basedataEntityType = (BasedataEntityType)(dynamicObject = (serviceHelper = new HRBaseServiceHelper("haos_adminorgtype")).loadSingle((Object)id)).getDataEntityType(), id)).isRefence()) {
            this.getView().setEnable(Boolean.valueOf(false), new String[]{ADMIN_ORG_TYPE_STD});
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        String name = e.getProperty().getName();
        if (ADMIN_ORG_TYPE_STD.equals(name)) {
            DynamicObject dynamicObject = (DynamicObject)this.getModel().getValue(ADMIN_ORG_TYPE_STD);
            if (dynamicObject == null) {
                return;
            }
            this.getModel().setValue("orgpattern", (Object)AdminOrgTypeStdEnum.getOrgPatternIdById((long)dynamicObject.getLong("id")));
        }
    }

    private static BaseDataCheckRefrenceResult baseDataCheckReference(BasedataEntityType baseEntityType, Long id) {
        BaseDataCheckRefrence checker = new BaseDataCheckRefrence();
        checker.setDraftValidReference(false);
        return checker.checkRef(baseEntityType, (Object)id);
    }
}
