/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper
 */
package kd.hr.hrcs.opplugin.web.label;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper;

public final class LabelDeleteOp
extends HRDataBaseOp {
    private static final LabelServiceHelper labelServiceHelper = new LabelServiceHelper();

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) {
            return;
        }
        List labelIdList = Arrays.stream(dataEntities).map(dynamicObject -> dynamicObject.getLong("id")).collect(Collectors.toList());
        List brmSceneList = labelServiceHelper.listBrmSceneByLabelIdList(labelIdList);
        labelServiceHelper.deleteLabelValueAndRule(labelIdList);
        labelServiceHelper.deleteLabelParam(labelIdList);
        if (brmSceneList != null && !brmSceneList.isEmpty()) {
            labelServiceHelper.deleteBrmScene(brmSceneList);
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
    }
}
