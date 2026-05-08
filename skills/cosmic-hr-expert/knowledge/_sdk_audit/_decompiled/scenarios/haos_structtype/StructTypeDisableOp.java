/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.haos.business.domain.otherstruct.service.OtherAdminOrgService
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.otherstruct.structtype;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.haos.business.domain.otherstruct.service.OtherAdminOrgService;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructTypeDisableOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        evt.getFieldKeys().add("enable");
        evt.getFieldKeys().add("metanumsuffix");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] structureData;
        DynamicObject[] adminOrgData;
        super.beginOperationTransaction(e);
        DynamicObject[] dataEntities = e.getDataEntities();
        OtherAdminOrgService otherAdminOrgService = new OtherAdminOrgService();
        Set otClassifyId = Arrays.stream(dataEntities).map(dynamicObject -> dynamicObject.getLong("id")).collect(Collectors.toSet());
        for (DynamicObject adminOrgDatum : adminOrgData = otherAdminOrgService.getOtherAdminOrgData(otClassifyId, "enable")) {
            adminOrgDatum.set("enable", (Object)"0");
        }
        for (DynamicObject structureDatum : structureData = otherAdminOrgService.getStructureDataById(otClassifyId, "enable")) {
            structureDatum.set("enable", (Object)"0");
        }
        otherAdminOrgService.saveOtherAdminOrgData(adminOrgData);
        otherAdminOrgService.saveStructureData(structureData);
    }
}

