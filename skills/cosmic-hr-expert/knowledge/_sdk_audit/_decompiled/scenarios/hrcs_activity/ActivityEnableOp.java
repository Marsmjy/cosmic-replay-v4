/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.TimeServiceHelper
 *  kd.bos.servicehelper.operation.OperationServiceHelper
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.hrcs.opplugin.web.activity;

import java.util.ArrayList;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.TimeServiceHelper;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class ActivityEnableOp
extends HRDataBaseOp {
    private static final String ENTITY_ENABLEREC = "hrcs_activityenablerec";

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        DynamicObject[] items = e.getDataEntities();
        ArrayList<DynamicObject> actTransRecSet = new ArrayList<DynamicObject>(items.length);
        for (DynamicObject dynObj : items) {
            DynamicObject enableRec = BusinessDataServiceHelper.newDynamicObject((String)ENTITY_ENABLEREC);
            enableRec.set("creator", (Object)RequestContext.get().getCurrUserId());
            enableRec.set("createtime", (Object)TimeServiceHelper.now());
            enableRec.set("activity", (Object)dynObj);
            actTransRecSet.add(enableRec);
        }
        OperationServiceHelper.executeOperate((String)"submit", (String)ENTITY_ENABLEREC, (DynamicObject[])actTransRecSet.toArray(new DynamicObject[0]), (OperateOption)OperateOption.create());
    }
}
