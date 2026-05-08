/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.BasedataEntityType
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRDynamicObjectUtils
 *  kd.hr.hbp.opplugin.web.HRCoreBaseBillOp
 *  kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatchChgHelper
 *  kd.hr.homs.business.domain.batchbill.service.OrgBatchValidateHelper
 *  kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBillSaveHelper
 *  kd.hr.homs.business.domain.orgfast.service.impl.AdminOrgChgBillSaveService
 *  kd.hr.homs.common.constants.batchchg.OrgBatchChgBillConstants
 */
package kd.hr.homs.opplugin.web.orgbatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.BasedataEntityType;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRDynamicObjectUtils;
import kd.hr.hbp.opplugin.web.HRCoreBaseBillOp;
import kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatchChgHelper;
import kd.hr.homs.business.domain.batchbill.service.OrgBatchValidateHelper;
import kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBillSaveHelper;
import kd.hr.homs.business.domain.orgfast.service.impl.AdminOrgChgBillSaveService;
import kd.hr.homs.common.constants.batchchg.OrgBatchChgBillConstants;

public final class OrgBatchBillSubmitAndEffectiveOp
extends HRCoreBaseBillOp {
    private static final Log logger = LogFactory.getLog(OrgBatchBillSubmitAndEffectiveOp.class);

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject dataEntity = e.getDataEntities()[0];
        this.setAddOrgNumber(dataEntity);
        Long billId = dataEntity.getLong("id");
        DynamicObject[] datas = OrgBatchValidateHelper.BATCHORGENTITY_HELPER.loadDynamicObjectArray(new QFilter[]{new QFilter("billid", "=", (Object)billId)});
        List orgIdList = Arrays.stream(datas).map(dy -> dy.getLong("adminorg.boid")).collect(Collectors.toList());
        AdminOrgChgBillSaveService billSaveService = AdminOrgChgBillSaveService.getInstance();
        Map oldOrgRelateVersionMap = billSaveService.getOrgRelateVersionInfo(orgIdList);
        Map oldOrgBasicInfo = billSaveService.getOrgBeforeChangeVersionBasicInfo(orgIdList);
        billSaveService.setBeforeAndAfterChgVersionId(datas, null, oldOrgRelateVersionMap, oldOrgBasicInfo);
        OrgBatchValidateHelper.BATCHORGENTITY_HELPER.update(datas);
    }

    private void setAddOrgNumber(DynamicObject dataEntity) {
        DynamicObject[] datas = OrgBatchValidateHelper.BATCHORGENTITY_HELPER.loadDynamicObjectArray(new QFilter[]{new QFilter("billid", "=", dataEntity.get("id"))});
        List addDys = Arrays.stream(datas).filter(dynamicObject -> dynamicObject.getLong("changetype.id") == OrgBatchChgBillConstants.CHANGE_TYPE_ADD.longValue() && StringUtils.isEmpty((CharSequence)dynamicObject.getString("number"))).collect(Collectors.toList());
        if (addDys.size() == 0) {
            return;
        }
        Map idVsBelongCompany = AdminOrgBatchChgHelper.findBelongCompanyMap((DynamicObject[])datas);
        ArrayList<DynamicObject> adminorgHrDys = new ArrayList<DynamicObject>(addDys.size());
        for (DynamicObject addDy2 : addDys) {
            DynamicObject adminorgHrDy = new DynamicObject((DynamicObjectType)MetadataServiceHelper.getDataEntityType((String)"haos_adminorgdetail"));
            HRDynamicObjectUtils.copy((DynamicObject)addDy2, (DynamicObject)adminorgHrDy);
            this.setBelongCompany(addDy2, idVsBelongCompany);
            adminorgHrDy.set("parent", addDy2.get("parentorg"));
            adminorgHrDy.set("orgtype", addDy2.get("adminorgtype"));
            adminorgHrDy.set("company", addDy2.get("belongcompany"));
            adminorgHrDy.set("id", addDy2.get("id"));
            IDataEntityType dataEntityType = adminorgHrDy.getDataEntityType();
            ((BasedataEntityType)dataEntityType).setMainOrg("org");
            boolean codeRuleExistFlag = AdminOrgBatchChgHelper.codeRuleExistFlag((String)"haos_adminorgdetail", (long)adminorgHrDy.getLong("org.id"), (DynamicObject)adminorgHrDy);
            if (!codeRuleExistFlag) continue;
            adminorgHrDys.add(adminorgHrDy);
        }
        if (!CollectionUtils.isEmpty(adminorgHrDys)) {
            DynamicObject[] entryDyArr;
            ArrayList numberList = new ArrayList(addDys.size());
            OrgBatchBillSaveHelper.setOrgNumber(addDys, adminorgHrDys, numberList);
            Map<Long, String> map = addDys.stream().collect(Collectors.toMap(dynamicObject -> dynamicObject.getLong("id"), dynamicObject -> dynamicObject.getString("number")));
            ArrayList addEntryIds = new ArrayList();
            addDys.stream().forEach(addDy -> addEntryIds.add(addDy.getLong("id")));
            HRBaseServiceHelper addEntryHelper = new HRBaseServiceHelper("homs_batchorgentity");
            for (DynamicObject dynamicObject2 : entryDyArr = addEntryHelper.query("number", new QFilter("id", "in", addEntryIds).toArray())) {
                dynamicObject2.set("number", (Object)map.get(dynamicObject2.getLong("id")));
            }
            addEntryHelper.update(entryDyArr);
        }
    }

    private void setBelongCompany(DynamicObject addDy, Map<Long, DynamicObject> idVsBelongCompany) {
        addDy.set("belongcompany", (Object)idVsBelongCompany.get(addDy.getLong("adminorg.id")));
    }
}
