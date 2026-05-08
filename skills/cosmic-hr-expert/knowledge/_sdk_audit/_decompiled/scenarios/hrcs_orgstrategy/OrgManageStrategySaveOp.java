/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hrcs.bussiness.service.StrategyServiceHelper
 *  kd.hr.hrcs.opplugin.web.ManageStrategySaveOp
 */
package kd.hr.hrcs.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hrcs.bussiness.service.StrategyServiceHelper;
import kd.hr.hrcs.opplugin.web.ManageStrategySaveOp;

public final class OrgManageStrategySaveOp
extends ManageStrategySaveOp {
    protected void doSaveStrategyHis(DynamicObject[] newOrgStrategyDys, String changeType) {
        if (newOrgStrategyDys == null) {
            return;
        }
        HRBaseServiceHelper hrBaseServiceHelper = new HRBaseServiceHelper("hrcs_orgstrategy");
        for (DynamicObject newOrgStrategyDy : newOrgStrategyDys) {
            DynamicObject oldOrgStrategyDy = hrBaseServiceHelper.queryOne("id, orgteam, inheritedorg, hrbu, sourceorg, strategytype, status, enable,effdt, effectivedate,changetype, modifytime, modifier, entryentity.id,entryentity.entryhrbu,entryentity.entryinheritedorg, entryentity.entryenable,entryentity.entryorgteam,entryentity.bussinessfield, entryentity.strategyentrytype,entryentity.entrysourceorg, entryeffdt, entryentity.entrydefstrategy, defstrategytype,entryentity.entrychangetype", (Object)newOrgStrategyDy.getLong("id"));
            StrategyServiceHelper.sysnSubOrgStrategy((DynamicObject)oldOrgStrategyDy, (DynamicObject)newOrgStrategyDy, (String)"org");
        }
    }
}
