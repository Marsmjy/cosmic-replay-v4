/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IAssignmentApplicationService
 */
package kd.hrmp.hrpi.opplugin.web.template;

import java.util.List;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IAssignmentApplicationService;

public final class AssignmentTplCommonSaveOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List propertySet = e.getFieldKeys();
        propertySet.add("employee");
        propertySet.add("assignment");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        IAssignmentApplicationService.getInstance().setDefaultAssignment(e.getDataEntities());
    }
}
