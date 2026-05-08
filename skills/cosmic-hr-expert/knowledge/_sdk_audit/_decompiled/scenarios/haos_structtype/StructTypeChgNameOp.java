/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.hr.haos.business.domain.otherstruct.helper.StructClassHelper
 *  kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.otherstruct.structtype;

import java.util.HashMap;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.hr.haos.business.domain.otherstruct.helper.StructClassHelper;
import kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructTypeChgNameOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("name");
        e.getFieldKeys().add("metanumsuffix");
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        OtherStructTypeService otherStructTypeService = new OtherStructTypeService();
        DynamicObject[] dataEntities = e.getDataEntities();
        HashMap changNameForMap = new HashMap(3);
        for (DynamicObject dataEntity : dataEntities) {
            ILocaleString localeString = dataEntity.getLocaleString("name");
            Map nameValuesForMap = otherStructTypeService.getChangeNameMap(changNameForMap, dataEntity);
            localeString.forEach((k, v) -> nameValuesForMap.put(k, v));
            String numberPrefix = dataEntity.getString("metanumsuffix");
            StructClassHelper.changeMetaName((String)numberPrefix, (String)localeString.getLocaleValue());
            StructClassHelper.chgMenuName((String)dataEntity.getString("id"), (Map)nameValuesForMap);
        }
    }
}

