/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.haos.business.domain.otherstruct.helper.StructClassHelper
 *  kd.hr.haos.business.domain.otherstruct.helper.StructTypeIETempHelper
 *  kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService
 *  kd.hr.haos.common.constants.otherstruct.BizRuleLibraryEnum
 *  kd.hr.haos.common.constants.otherstruct.StructTypeConstant
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.otherstruct.structtype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.haos.business.domain.otherstruct.helper.StructClassHelper;
import kd.hr.haos.business.domain.otherstruct.helper.StructTypeIETempHelper;
import kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService;
import kd.hr.haos.common.constants.otherstruct.BizRuleLibraryEnum;
import kd.hr.haos.common.constants.otherstruct.StructTypeConstant;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructTypeEnableOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(StructTypeEnableOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("metanumsuffix");
        fieldKeys.add("name");
        fieldKeys.add("effdt");
        fieldKeys.add("number");
        fieldKeys.add("org");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs event) {
        DynamicObject[] dataEntities = event.getDataEntities();
        OtherStructTypeService otherStructTypeService = new OtherStructTypeService();
        List structTypeIds = Arrays.stream(dataEntities).map(data -> data.getLong("id")).collect(Collectors.toList());
        DynamicObject[] structTypeDyo = otherStructTypeService.getStructTypeDataById(structTypeIds, "id,enable");
        Map<Long, String> dataForEnable = Arrays.stream(structTypeDyo).collect(Collectors.toMap(data -> data.getLong("id"), data -> data.getString("enable")));
        ArrayList<DynamicObject> enabling = new ArrayList<DynamicObject>(dataEntities.length);
        ArrayList<DynamicObject> disable = new ArrayList<DynamicObject>(dataEntities.length);
        for (DynamicObject dataEntity : dataEntities) {
            String enable = dataForEnable.get(dataEntity.getLong("id"));
            if ("10".equals(enable)) {
                enabling.add(dataEntity);
                continue;
            }
            if (!"0".equals(enable)) continue;
            disable.add(dataEntity);
        }
        otherStructTypeService.processEnableStatus(enabling, disable);
        StructClassHelper.saveEnable((DynamicObject[])enabling.toArray(new DynamicObject[0]));
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        ArrayList<String> allowCreateMeta = new ArrayList<String>(10);
        try (TXHandle required = TX.requiresNew();){
            try {
                for (DynamicObject dataEntity2 : e.getDataEntities()) {
                    String metaNumSufFix = dataEntity2.getString("metanumsuffix");
                    StructClassHelper.creatMetaDataAndMenu((String)metaNumSufFix, (DynamicObject)dataEntity2);
                    for (String meta : StructTypeConstant.BASE_META_PAGE) {
                        allowCreateMeta.add(meta + "_" + metaNumSufFix);
                    }
                }
            }
            catch (Throwable ex) {
                required.markRollback();
                LOG.error("creatMetaDataAndMenu error", ex);
                throw new KDBizException(ex.getMessage());
            }
        }
        OtherStructTypeService otherStructMetadataService = new OtherStructTypeService();
        Map structTypeIdForMetaMap = otherStructMetadataService.getStructTypeIdMap(e.getDataEntities());
        DynamicObject[] bosObject = otherStructMetadataService.queryAllMetaData(allowCreateMeta);
        Map<String, DynamicObject> bosObjectForMap = Arrays.stream(bosObject).collect(Collectors.toMap(data -> data.getString("id"), data -> data));
        ArrayList bizRuleDyos = new ArrayList(BizRuleLibraryEnum.values().length);
        ArrayList bizRuleLibraryDyos = new ArrayList(BizRuleLibraryEnum.values().length);
        ArrayList opBizRuleSetDyns = new ArrayList(BizRuleLibraryEnum.values().length);
        OtherStructTypeService service = new OtherStructTypeService();
        DynamicObject defaultStructType = service.queryStructTypeDefaultData("id");
        for (DynamicObject dataEntity3 : e.getDataEntities()) {
            String metaNumSufFix = dataEntity3.getString("metanumsuffix");
            StructClassHelper.saveStructConfig((DynamicObject)dataEntity3, (String)metaNumSufFix, (Map)structTypeIdForMetaMap, (DynamicObject)defaultStructType, bosObjectForMap);
            StructTypeIETempHelper.addTemplate((DynamicObject)dataEntity3);
            StructClassHelper.creatBizRule((DynamicObject)dataEntity3, bizRuleDyos, bizRuleLibraryDyos, opBizRuleSetDyns);
        }
        if (!bizRuleLibraryDyos.isEmpty()) {
            StructClassHelper.saveRizRuleLibrary(bizRuleLibraryDyos);
        }
        if (!bizRuleDyos.isEmpty()) {
            StructClassHelper.saveRizRule(bizRuleDyos);
        }
        if (!opBizRuleSetDyns.isEmpty()) {
            StructClassHelper.saveOpBizRuleSet(opBizRuleSetDyns);
        }
        String typeName = Arrays.stream(e.getDataEntities()).map(dataEntity -> dataEntity.getString("name")).collect(Collectors.joining(","));
        this.operationResult.setSuccess(true);
        this.operationResult.setMessage(ResManager.loadKDString((String)"\u7cfb\u7edf\u5df2\u5728\u201c\u7ec4\u7ec7\u7ba1\u7406>\u5176\u4ed6\u5f62\u6001\u7ec4\u7ec7\u201d\u4e0b\u65b0\u589e\u4e86\u4e8c\u7ea7\u83dc\u5355:\u201c%s\u201d, \u8bf7\u524d\u5f80\u201cHR\u57fa\u7840\u670d\u52a1\u4e91\u3009HR\u901a\u7528\u670d\u52a1-\u6743\u9650\u201d\u5efa\u7acb\u8be5\u7c7b\u578b\u5bf9\u5e94\u7684\u7ef4\u5ea6\u548c\u6743\u9650\u7ef4\u5ea6\u6620\u5c04\uff0c\u5e76\u7ed9\u7528\u6237\u6388\u6743\uff0c\u6388\u6743\u540e\u624d\u53ef\u5728\u8be5\u83dc\u5355\u4e0b\u7ef4\u62a4\u7ec4\u7ec7\u67b6\u6784\u4fe1\u606f\u3002", (String)"StructTypeEnableOP_1", (String)"hrmp-haos-opplugin", (Object[])new Object[]{typeName}));
        this.operationResult.setShowMessage(true);
    }
}

