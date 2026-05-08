/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.Date;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class TrialPeriodSaveOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("probation");
        fieldKeys.add("preenddate");
        fieldKeys.add("trialstartdate");
        fieldKeys.add("probationunit");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        this.setPreenddate(e.getDataEntities());
    }

    private void setPreenddate(DynamicObject[] dataEntities) {
        boolean isRevise = HRStringUtils.equals((String)Boolean.TRUE.toString(), (String)((String)this.getOption().getVariables().get("hr_revise_entrydate_departdate")));
        for (DynamicObject dataEntity : dataEntities) {
            if (!HRObjectUtils.isEmpty((Object)dataEntity.get("preenddate")) && !isRevise || HRObjectUtils.isEmpty((Object)dataEntity.get("trialstartdate")) || HRObjectUtils.isEmpty((Object)dataEntity.get("probation")) || HRObjectUtils.isEmpty((Object)dataEntity.get("probationunit"))) continue;
            Date preenddate = new Date();
            Integer probation = dataEntity.getInt("probation");
            Object probationUnit = dataEntity.get("probationunit");
            Date trialStartDate = dataEntity.getDate("trialstartdate");
            preenddate = "1".equals(probationUnit) ? HRDateTimeUtils.addMonth((Date)trialStartDate, (int)probation) : ("2".equals(probationUnit) ? HRDateTimeUtils.addDay((Date)trialStartDate, (long)((long)probation.intValue() * 7L)) : HRDateTimeUtils.addDay((Date)trialStartDate, (long)probation.intValue()));
            dataEntity.set("preenddate", (Object)preenddate);
        }
    }
}
