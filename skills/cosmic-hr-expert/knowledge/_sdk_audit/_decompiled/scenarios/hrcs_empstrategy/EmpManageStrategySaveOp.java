/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hrcs.bussiness.service.StrategyServiceHelper
 *  kd.hr.hrcs.opplugin.web.ManageStrategySaveOp
 */
package kd.hr.hrcs.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hrcs.bussiness.service.StrategyServiceHelper;
import kd.hr.hrcs.opplugin.web.ManageStrategySaveOp;

@ExcludeFromJacocoGeneratedReport
public final class EmpManageStrategySaveOp
extends ManageStrategySaveOp {
    protected void doSaveStrategyHis(DynamicObject[] newEmpStrategyDys, String changeType) {
        if (newEmpStrategyDys == null) {
            return;
        }
        HRBaseServiceHelper hrBaseServiceHelper = new HRBaseServiceHelper("hrcs_empstrategy");
        for (DynamicObject newEmpStrategyDy : newEmpStrategyDys) {
            DynamicObject oldEmpStrategyDyc = hrBaseServiceHelper.queryOne("id, orgteam, inheritedorg, hrbu, sourceorg, strategytype, status, enable,effdt, effectivedate,changetype, modifytime, modifier, entryentity.id,entryentity.entryhrbu,entryentity.entryinheritedorg, entryentity.entryenable,entryentity.entryorgteam,entryentity.bussinessfield, entryentity.strategyentrytype,entryentity.entrysourceorg, entryeffdt, entryentity.entrydefstrategy, defstrategytype,entryentity.entrychangetype", (Object)newEmpStrategyDy.getLong("id"));
            StrategyServiceHelper.sysnSubOrgStrategy((DynamicObject)oldEmpStrategyDyc, (DynamicObject)newEmpStrategyDy, (String)"emp");
        }
    }
}
