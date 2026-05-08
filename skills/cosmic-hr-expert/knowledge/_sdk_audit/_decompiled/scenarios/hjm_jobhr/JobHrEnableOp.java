/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp
 *  kd.hrmp.hbjm.opplugin.web.validator.JobEnableValidator
 */
package kd.hrmp.hbjm.opplugin.web.job;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp;
import kd.hrmp.hbjm.opplugin.web.validator.JobEnableValidator;

public final class JobHrEnableOp
extends JobHrMsgHandleOp {
    Map<Long, String> jobBoIdVsEnableMap = new ConcurrentHashMap<Long, String>(16);

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        Map fields = EntityMetadataCache.getDataEntityType((String)"hbjm_jobhr").getFields();
        List fieldKeys = evt.getFieldKeys();
        fieldKeys.addAll(fields.keySet());
    }

    public void onAddValidators(AddValidatorsEventArgs evt) {
        evt.addValidator((AbstractValidator)new JobEnableValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
        Arrays.stream(e.getDataEntities()).forEach(dataEntity -> this.jobBoIdVsEnableMap.put(dataEntity.getLong("boid"), dataEntity.getString("enable")));
        this.getOption().setVariableValue("HisModelBusinessClassName", ((Object)((Object)this)).getClass().getName());
        this.getOption().setVariableValue("HisModelBusinessMethodName", "dealDynsInfo");
    }
}
