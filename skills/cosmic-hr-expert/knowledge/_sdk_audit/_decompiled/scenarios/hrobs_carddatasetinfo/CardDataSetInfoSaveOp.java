/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.validator.CardDataSetInfoSaveValidator
 */
package kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo;

import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.validator.CardDataSetInfoSaveValidator;

public final class CardDataSetInfoSaveOp
extends AbstractOperationServicePlugIn {
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.getValidators().add(new CardDataSetInfoSaveValidator());
    }
}
