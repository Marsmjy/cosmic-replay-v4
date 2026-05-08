/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.servicehelper.MetadataServiceHelper
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
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.haos.business.domain.otherstruct.helper.StructClassHelper;
import kd.hr.haos.business.domain.otherstruct.helper.StructTypeIETempHelper;
import kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService;
import kd.hr.haos.common.constants.otherstruct.BizRuleLibraryEnum;
import kd.hr.haos.common.constants.otherstruct.StructTypeConstant;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructTypeSaveOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(StructTypeSaveOp.class);

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        long[] pkIds = ORM.create().genLongIds((IDataEntityType)MetadataServiceHelper.getDataEntityType((String)"haos_structtype"), e.getDataEntities().length);
        for (int i = 0; i < e.getDataEntities().length; ++i) {
            DynamicObject dyn = e.getDataEntities()[i];
            boolean fromDatabase = dyn.getDataEntityState().getFromDatabase();
            String enable = dyn.getString("enable");
            if (!"1".equals(enable) || fromDatabase) continue;
            StructClassHelper.saveNew((DynamicObject)dyn, (long)pkIds[i]);
            this.getOption().setVariableValue(dyn.getString("id"), "0");
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        ArrayList<String> allowCreateMeta = new ArrayList<String>(10);
        try (TXHandle required = TX.requiresNew();){
            try {
                for (DynamicObject dataEntity : e.getDataEntities()) {
                    Map variables = this.getOption().getVariables();
                    if (!variables.containsKey(dataEntity.getString("id"))) continue;
                    String isNewData = this.getOption().getVariableValue(dataEntity.getString("id"));
                    LOG.info("isNewData:" + isNewData);
                    DynamicObject[] enable = dataEntity.getString("enable");
                    if (!"1".equals(enable) || !"0".equals(isNewData)) continue;
                    String metaNumSufFix = dataEntity.getString("metanumsuffix");
                    StructClassHelper.creatMetaDataAndMenu((String)metaNumSufFix, (DynamicObject)dataEntity);
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
        ArrayList<String> dataName = new ArrayList<String>(0);
        ArrayList bizRuleDyos = new ArrayList(BizRuleLibraryEnum.values().length);
        ArrayList bizRuleLibraryDyos = new ArrayList(BizRuleLibraryEnum.values().length);
        ArrayList opBizRuleSetDyns = new ArrayList(BizRuleLibraryEnum.values().length);
        OtherStructTypeService service = new OtherStructTypeService();
        DynamicObject[] bosObject = service.queryAllMetaData(allowCreateMeta);
        Map<String, DynamicObject> bosObjectForMap = Arrays.stream(bosObject).collect(Collectors.toMap(data -> data.getString("id"), data -> data));
        DynamicObject defaultStructType = service.queryStructTypeDefaultData("id");
        for (DynamicObject dataEntity : e.getDataEntities()) {
            Map variables = this.getOption().getVariables();
            if (!variables.containsKey(dataEntity.getString("id"))) continue;
            String isNewData = this.getOption().getVariableValue(dataEntity.getString("id"));
            String enable = dataEntity.getString("enable");
            if (!"1".equals(enable) || !"0".equals(isNewData)) continue;
            String metaNumSufFix = dataEntity.getString("metanumsuffix");
            StructClassHelper.saveStructConfig((DynamicObject)dataEntity, (String)metaNumSufFix, Collections.emptyMap(), (DynamicObject)defaultStructType, bosObjectForMap);
            StructTypeIETempHelper.addTemplate((DynamicObject)dataEntity);
            StructClassHelper.creatBizRule((DynamicObject)dataEntity, bizRuleDyos, bizRuleLibraryDyos, opBizRuleSetDyns);
            dataName.add(dataEntity.getString("name"));
        }
        if (!bizRuleDyos.isEmpty()) {
            StructClassHelper.saveRizRule(bizRuleDyos);
        }
        if (!bizRuleLibraryDyos.isEmpty()) {
            StructClassHelper.saveRizRuleLibrary(bizRuleLibraryDyos);
        }
        if (!opBizRuleSetDyns.isEmpty()) {
            StructClassHelper.saveOpBizRuleSet(opBizRuleSetDyns);
        }
        if (!dataName.isEmpty()) {
            String typeName = String.join((CharSequence)",", dataName);
            this.operationResult.setSuccess(true);
            this.operationResult.setMessage(ResManager.loadKDString((String)"\u7cfb\u7edf\u5df2\u5728\u201c\u7ec4\u7ec7\u7ba1\u7406>\u5176\u4ed6\u5f62\u6001\u7ec4\u7ec7\u201d\u4e0b\u65b0\u589e\u4e86\u4e8c\u7ea7\u83dc\u5355:\u201c%s\u201d, \u8bf7\u524d\u5f80\u201cHR\u57fa\u7840\u670d\u52a1\u4e91\u3009HR\u901a\u7528\u670d\u52a1-\u6743\u9650\u201d\u5efa\u7acb\u8be5\u7c7b\u578b\u5bf9\u5e94\u7684\u7ef4\u5ea6\u548c\u6743\u9650\u7ef4\u5ea6\u6620\u5c04\uff0c\u5e76\u7ed9\u7528\u6237\u6388\u6743\uff0c\u6388\u6743\u540e\u624d\u53ef\u5728\u8be5\u83dc\u5355\u4e0b\u7ef4\u62a4\u7ec4\u7ec7\u67b6\u6784\u4fe1\u606f\u3002", (String)"StructTypeSaveOp_1", (String)"hrmp-haos-opplugin", (Object[])new Object[]{typeName}));
            this.operationResult.setShowMessage(true);
        }
    }
}

