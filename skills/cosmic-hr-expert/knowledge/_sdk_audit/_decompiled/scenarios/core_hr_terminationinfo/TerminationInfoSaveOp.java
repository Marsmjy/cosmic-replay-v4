/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.List;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class TerminationInfoSaveOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("startdate");
        fieldKeys.add("enddate");
        fieldKeys.add("departdate");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        Object[] dataEntities = e.getDataEntities();
        if (ObjectUtils.isEmpty((Object[])dataEntities)) {
            return;
        }
        for (Object dataEntity : dataEntities) {
            if (dataEntity.getDate("startdate") == null) {
                dataEntity.set("startdate", (Object)dataEntity.getDate("departdate"));
            }
            if (dataEntity.getDate("enddate") != null) continue;
            dataEntity.set("enddate", (Object)TimeLineServiceUtil.getMaxEffEndDate());
        }
    }
}
