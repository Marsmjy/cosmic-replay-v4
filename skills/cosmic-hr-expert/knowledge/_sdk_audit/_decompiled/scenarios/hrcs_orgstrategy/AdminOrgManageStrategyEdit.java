/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.hr.hrcs.common.constants.ManageStrategyConstants
 *  kd.hr.hrcs.formplugin.web.managestrategy.OrgManageStrategyEdit
 *  kd.hr.hrcs.formplugin.web.utils.ManageStrategyServiceHelper
 */
package kd.hr.hrcs.formplugin.web.managestrategy;

import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.hrcs.common.constants.ManageStrategyConstants;
import kd.hr.hrcs.formplugin.web.managestrategy.OrgManageStrategyEdit;
import kd.hr.hrcs.formplugin.web.utils.ManageStrategyServiceHelper;

public final class AdminOrgManageStrategyEdit
extends OrgManageStrategyEdit {
    protected Long getOrgType() {
        return ManageStrategyConstants.LONG_ORGTYPE_ADMINORG;
    }

    protected Long getBusinessObjectId() {
        return ManageStrategyConstants.LONG_BUSSINESSOBJECTID_ID_ORG;
    }

    protected List<String> getStrategyTypes() {
        DynamicObject adminorg = (DynamicObject)this.getModel().getValue("orgteam");
        if (adminorg == null) {
            return null;
        }
        return ManageStrategyServiceHelper.getStrategyTypes((DynamicObject)adminorg, (Long)this.getOrgType(), (Long)ManageStrategyConstants.LONG_BUSSINESSOBJECTID_ID_ORG, (Long)0L);
    }
}
