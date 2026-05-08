/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.opplugin.validator.label.LabelSceneEditValidator
 */
package kd.hr.hrcs.opplugin.web.label;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.opplugin.validator.label.LabelSceneEditValidator;

public final class LabelSceneEditOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new LabelSceneEditValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        DynamicObject[] objs = e.getDataEntities();
        String operateKey = e.getOperationKey();
        if (("save".equals(operateKey) || "submit".equals(operateKey)) && this.getOption().containsVariable("isFromForm")) {
            DynamicObject dy = objs[0];
            DynamicObjectCollection coll = dy.getDynamicObjectCollection("entryentity");
            List labelIds = coll.stream().sorted(Comparator.comparing(entry -> entry.getInt("bizindex"))).map(entry -> entry.getLong("label.id")).collect(Collectors.toList());
            for (DynamicObject labelEntry : coll) {
                labelEntry.set("seq", (Object)labelIds.indexOf(labelEntry.getLong("label.id") + 1L));
            }
        }
    }
}
