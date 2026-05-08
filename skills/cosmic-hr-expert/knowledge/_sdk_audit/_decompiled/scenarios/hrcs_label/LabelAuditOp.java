/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper
 *  kd.hr.hrcs.opplugin.validator.label.LabelSaveValidator
 */
package kd.hr.hrcs.opplugin.web.label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper;
import kd.hr.hrcs.opplugin.validator.label.LabelSaveValidator;

public final class LabelAuditOp
extends HRDataBaseOp {
    private static final LabelServiceHelper labelServiceHelper = new LabelServiceHelper();
    private static final Log LOGGER = LogFactory.getLog(LabelAuditOp.class);

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new LabelSaveValidator());
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) {
            return;
        }
        for (DynamicObject dynamicObject : dataEntities) {
            long labelId = dynamicObject.getLong("id");
            DynamicObject label = labelServiceHelper.getLabel(labelId);
            if (!"1".equals(label.getString("configtype"))) continue;
            this.updateSceneAudit(label);
            this.updateTargetAudit(label);
        }
    }

    private void updateSceneAudit(DynamicObject label) {
        DynamicObject[] labelObjectRelCollection = labelServiceHelper.getLabelObjectRelCollection(label.getLong("id"));
        if (labelObjectRelCollection == null || labelObjectRelCollection.length == 0) {
            return;
        }
        for (DynamicObject dy : labelObjectRelCollection) {
            ArrayList inputMaps = new ArrayList();
            ArrayList outputMaps = new ArrayList();
            Map map = labelServiceHelper.updateBrmScene(Long.valueOf(dy.getLong("brmscene.id")), dy.getString("labelobject.name"), label, inputMaps, outputMaps);
            if (200 == Integer.parseInt(map.get("resultCode").toString())) continue;
            throw new KDBizException(map.get("errorMsg").toString());
        }
    }

    private void updateTargetAudit(DynamicObject label) {
        long labelId = label.getLong("id");
        DynamicObject[] labelValueCollection = labelServiceHelper.getLabelValueCollection(labelId);
        if (labelValueCollection == null || labelValueCollection.length == 0) {
            return;
        }
        String expr = (String)this.getOption().getVariables().get("expression");
        if (HRStringUtils.isEmpty((String)expr)) {
            return;
        }
        DynamicObjectCollection entry = label.getDynamicObjectCollection("entryentityrange");
        Map<Long, Long> sceneMap = entry.stream().collect(Collectors.toMap(row -> row.getLong("labelobject.id"), row -> row.getLong("brmscene.id")));
        int index = 0;
        Map groupExprMap = (Map)SerializationUtils.fromJsonString((String)expr, Map.class);
        for (DynamicObject labelValue : labelValueCollection) {
            String labelValueStr = labelValue.getString("value");
            List labelRuleList = labelServiceHelper.listLabelValueRuleByLabelValueId(Long.valueOf(labelValue.getLong("id")));
            if (labelRuleList == null || labelRuleList.isEmpty()) {
                return;
            }
            Map exprMap = (Map)groupExprMap.get(index + "");
            labelRuleList.forEach(labelValueRule -> {
                long objectId = labelValueRule.getLong("labelobject.id");
                DynamicObject objectDy = labelServiceHelper.loadObject(objectId);
                if (objectDy == null) {
                    throw new KDBizException(ResManager.loadKDString((String)"\u6253\u6807\u5bf9\u8c61\u4e0d\u5b58\u5728\u3002", (String)"LabelServiceHelper_0", (String)"hrmp-hrcs-business", (Object[])new Object[0]));
                }
                String objectName = objectDy.getString("name");
                long brmSceneId = (Long)sceneMap.get(objectId);
                if (brmSceneId == 0L) {
                    return;
                }
                long targetId = labelValueRule.getLong("brmtarget.id");
                long labelValueId = labelValue.getLong("id");
                Map resultMap = (Map)exprMap.get(objectId + "");
                Map brmTargetResult = labelServiceHelper.updateBrmTarget(targetId, labelServiceHelper.geneTargetName(label.getString("name"), objectName, labelValueStr), "Label_" + brmSceneId + "_" + labelValueId, (String)resultMap.get("displayfunctiontext"), (String)resultMap.get("expr"), Long.valueOf(brmSceneId), label.getString("status"));
                if (!"200".equals(brmTargetResult.get("resultCode").toString())) {
                    throw new KDBizException(brmTargetResult.get("errorMsg").toString());
                }
            });
            ++index;
        }
    }
}
