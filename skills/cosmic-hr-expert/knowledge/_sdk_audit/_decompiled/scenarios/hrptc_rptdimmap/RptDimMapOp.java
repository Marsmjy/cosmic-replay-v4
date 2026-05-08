/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrptc.common.constant.permission.EntityConstants
 *  kd.hr.hrptc.opplugin.web.perm.ReportUserPermOp
 *  kd.hr.hrptmc.business.repdesign.ReportPermissionService
 */
package kd.hr.hrptc.opplugin.web.perm;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrptc.common.constant.permission.EntityConstants;
import kd.hr.hrptc.opplugin.web.perm.ReportUserPermOp;
import kd.hr.hrptmc.business.repdesign.ReportPermissionService;

public final class RptDimMapOp
extends HRDataBaseOp
implements EntityConstants {
    private static final Log LOGGER = LogFactory.getLog(ReportUserPermOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("id");
        fieldKeys.add("rptmanage");
        fieldKeys.add("aoqfield");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        try {
            DynamicObject[] dataEntities = args.getDataEntities();
            switch (args.getOperationKey()) {
                case "save": {
                    this.updatePermData(dataEntities);
                    break;
                }
                case "delete": {
                    this.delete(dataEntities);
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("RptDimMapOp_error:", (Throwable)exception);
        }
    }

    private void updatePermData(DynamicObject[] dataEntities) {
        for (DynamicObject dy : dataEntities) {
            long rptManageId = dy.getDynamicObject("rptmanage").getLong("id");
            if (!ReportPermissionService.isExistsRptAlocByRptId((long)rptManageId)) {
                ReportPermissionService.saveRptAloc((long)rptManageId);
            }
            DynamicObjectCollection dimEntry = dy.getDynamicObjectCollection("entryentity");
            List queryFieldIds = dimEntry.stream().map(dy1 -> dy1.getDynamicObject("aoqfield").getLong("id")).collect(Collectors.toList());
            ReportPermissionService.updateRptPerm((long)rptManageId, queryFieldIds);
        }
    }

    private void delete(DynamicObject[] dataEntities) {
        List rptManageIds = Arrays.stream(dataEntities).map(dy -> dy.getDynamicObject("rptmanage").getLong("id")).collect(Collectors.toList());
        Iterator iterator = rptManageIds.iterator();
        while (iterator.hasNext()) {
            long rptManageId = (Long)iterator.next();
            ReportPermissionService.delRptPerm((long)rptManageId);
        }
    }
}
