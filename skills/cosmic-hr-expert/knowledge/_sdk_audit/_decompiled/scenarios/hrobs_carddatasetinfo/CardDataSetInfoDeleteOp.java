/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hrmp.hrobs.business.workbench.engine.carddataset.repository.CardDataSetConfigRepository
 */
package kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hrmp.hrobs.business.workbench.engine.carddataset.repository.CardDataSetConfigRepository;

public final class CardDataSetInfoDeleteOp
extends AbstractOperationServicePlugIn {
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        if (dataEntities.length == 0) {
            return;
        }
        List carddatasetIdList = Arrays.stream(dataEntities).map(dyn -> dyn.getLong("id")).collect(Collectors.toList());
        CardDataSetConfigRepository.getInstance().deleteByIds(carddatasetIdList);
    }
}
