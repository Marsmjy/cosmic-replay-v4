/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hom.business.domain.service.handler.HomHPFSTemplatePropertyHandler
 *  kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp
 */
package kd.hr.hom.opplugin.onbrd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hom.business.domain.service.handler.HomHPFSTemplatePropertyHandler;
import kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp;

public final class OnbrdEffectOp
extends AbstractOperationServicePlugIn {
    private static final Log LOG = LogFactory.getLog(OnbrdConfirmOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        List fieldKeys = evt.getFieldKeys();
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)"hom_onbrdbilltpl");
        Map allFields = dataEntityType.getAllFields();
        fieldKeys.addAll(new ArrayList(allFields.keySet()));
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities.length == 0) {
            return;
        }
        for (DynamicObject dataEntity : dataEntities) {
            DynamicObject baACountryDy;
            DynamicObject baAMangeAdminOrgDy;
            DynamicObject baAOrgDy;
            String baAChgType = dataEntity.getString("ba_a_chgtype");
            if (!HRStringUtils.isNotEmpty((String)baAChgType) || "2".equals(baAChgType)) continue;
            DynamicObject baAEmpGroupDy = dataEntity.getDynamicObject("ba_a_empgroup");
            if (baAEmpGroupDy != null) {
                dataEntity.set("bb_a_empgroup", (Object)baAEmpGroupDy);
            }
            if ((baAOrgDy = dataEntity.getDynamicObject("ba_a_org")) != null) {
                dataEntity.set("bb_a_org", (Object)baAOrgDy);
            }
            if ((baAMangeAdminOrgDy = dataEntity.getDynamicObject("ba_a_manageadminorg")) != null) {
                dataEntity.set("bb_a_manageadminorg", (Object)baAMangeAdminOrgDy);
                dataEntity.set("bb_a_manageadminorgbo", (Object)dataEntity.getDynamicObject("ba_a_manageadminorgbo"));
            }
            if ((baACountryDy = dataEntity.getDynamicObject("ba_a_country")) == null) continue;
            dataEntity.set("bb_a_country", (Object)baACountryDy);
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        long startTime = System.currentTimeMillis();
        LOG.info("###OnbrdSubmitEffectOp#endOperationTransaction-start:{}", (Object)RequestContext.getOrCreate().getTraceId());
        DynamicObject[] dataEntities = args.getDataEntities();
        HomHPFSTemplatePropertyHandler.getInstance().buildOpCustomPersonParams(dataEntities);
        LOG.info("###OnbrdSubmitEffectOp#endOperationTransaction-end:{}", (Object)RequestContext.getOrCreate().getTraceId());
        LOG.info("###OnbrdSubmitEffectOp#endOperationTransaction-cost:{} ms", (Object)(System.currentTimeMillis() - startTime));
    }
}
