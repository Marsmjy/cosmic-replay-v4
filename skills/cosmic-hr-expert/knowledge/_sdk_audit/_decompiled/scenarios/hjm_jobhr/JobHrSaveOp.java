/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.db.DB
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp
 */
package kd.hrmp.hbjm.opplugin.web.job;

import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.db.DB;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp;

public final class JobHrSaveOp
extends JobHrMsgHandleOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List keys = e.getFieldKeys();
        keys.add("jobscm.enable");
        keys.add("jobseq.enable");
        keys.add("jobfamily.enable");
        keys.add("jobclass.enable");
        keys.add("name");
        keys.add("number");
        keys.add("boid");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities != null && dataEntities.length > 0) {
            long[] longIds = DB.genGlobalLongIds((int)args.getDataEntities().length);
            int index = 0;
            for (DynamicObject dy : args.getDataEntities()) {
                long id = dy.getLong("id");
                if (id != 0L) continue;
                dy.set("id", (Object)longIds[index]);
                ++index;
            }
        }
    }
}
