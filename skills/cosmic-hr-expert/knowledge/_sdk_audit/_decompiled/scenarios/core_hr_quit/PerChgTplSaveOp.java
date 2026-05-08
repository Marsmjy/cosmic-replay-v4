/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hpfs.opplugin.validators.OnbrdRevokedValidator
 *  kd.hr.hpfs.opplugin.validators.PerChgTplCrossValidator
 */
package kd.hr.hpfs.opplugin.op.tpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hpfs.opplugin.validators.OnbrdRevokedValidator;
import kd.hr.hpfs.opplugin.validators.PerChgTplCrossValidator;

public final class PerChgTplSaveOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        List fieldKeys = evt.getFieldKeys();
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)"hpfs_hrnewbillorgtpl");
        Map allFields = dataEntityType.getAllFields();
        fieldKeys.addAll(new ArrayList(allFields.keySet()));
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new OnbrdRevokedValidator());
        args.addValidator((AbstractValidator)new PerChgTplCrossValidator());
    }
}
