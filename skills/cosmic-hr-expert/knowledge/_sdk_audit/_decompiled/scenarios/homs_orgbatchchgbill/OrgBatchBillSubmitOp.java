/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.opplugin.web.HRCoreBaseBillOp
 *  kd.hr.homs.business.domain.batchbill.service.OrgBatchValidateHelper
 *  kd.hr.homs.business.domain.orgfast.service.impl.AdminOrgChgBillSaveService
 */
package kd.hr.homs.opplugin.web.orgbatch;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRCoreBaseBillOp;
import kd.hr.homs.business.domain.batchbill.service.OrgBatchValidateHelper;
import kd.hr.homs.business.domain.orgfast.service.impl.AdminOrgChgBillSaveService;

public final class OrgBatchBillSubmitOp
extends HRCoreBaseBillOp {
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        Long billId = e.getDataEntities()[0].getLong("id");
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
        DynamicObject dynamicObject = serviceHelper.queryOne("billstatus", new QFilter("id", "=", (Object)billId).toArray());
        dynamicObject.set("billstatus", (Object)"B");
        serviceHelper.updateOne(dynamicObject);
        DynamicObject[] datas = OrgBatchValidateHelper.BATCHORGENTITY_HELPER.loadDynamicObjectArray(new QFilter[]{new QFilter("billid", "=", (Object)billId)});
        List orgIdList = Arrays.stream(datas).map(dy -> dy.getLong("adminorg.boid")).collect(Collectors.toList());
        AdminOrgChgBillSaveService billSaveService = AdminOrgChgBillSaveService.getInstance();
        Map oldOrgRelateVersionMap = billSaveService.getOrgRelateVersionInfo(orgIdList);
        Map oldOrgBasicInfo = billSaveService.getOrgBeforeChangeVersionBasicInfo(orgIdList);
        billSaveService.setBeforeAndAfterChgVersionId(datas, null, oldOrgRelateVersionMap, oldOrgBasicInfo);
        OrgBatchValidateHelper.BATCHORGENTITY_HELPER.update(datas);
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        if ("submit".equals(e.getOperationKey())) {
            HRBaseServiceHelper batchOrgEntityHelper = new HRBaseServiceHelper("homs_batchorgentity");
            Date effdt = e.getDataEntities()[0].getDate("effdt");
            Long billId = e.getDataEntities()[0].getLong("id");
            DynamicObject[] billEntries = batchOrgEntityHelper.loadDynamicObjectArray(new QFilter("billid", "=", (Object)billId).toArray());
            Arrays.stream(billEntries).forEach(dy -> dy.set("bsed", (Object)effdt));
        }
    }
}
