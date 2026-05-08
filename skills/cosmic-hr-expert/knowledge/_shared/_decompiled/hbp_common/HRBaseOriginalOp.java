/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DataEntityState
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper
 */
package kd.hr.hbp.opplugin.web.config;

import java.util.List;
import kd.bos.dataentity.entity.DataEntityState;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper;

public class HRBaseOriginalOp
extends AbstractOperationServicePlugIn {
    private static final String ORI_STATUS = "oristatus";
    private static final String ORI_NUMBER = "orinumber";
    private static final String ORI_NAME = "oriname";

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add(ORI_STATUS);
        fieldKeys.add("issyspreset");
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        DynamicObject[] dys = args.getDataEntities();
        String formId = this.billEntityType.getName();
        if (this.getHisModelCondition(formId)) {
            return;
        }
        if (dys.length > 0) {
            DataEntityPropertyCollection toPros = dys[0].getDataEntityType().getProperties();
            for (IDataEntityProperty toPro : toPros) {
                if (!HRStringUtils.equals((String)toPro.getName(), (String)ORI_STATUS) && !HRStringUtils.equals((String)toPro.getName(), (String)ORI_NUMBER) && !HRStringUtils.equals((String)toPro.getName(), (String)ORI_NAME) || !HRStringUtils.isEmpty((String)toPro.getAlias())) continue;
                return;
            }
            for (DynamicObject dy : dys) {
                DataEntityState dataEntityState = dy.getDataEntityState();
                boolean dataEntityDirty = dataEntityState.getDataEntityDirty();
                boolean isSysPreset = dy.getBoolean("issyspreset");
                if (!dataEntityDirty || !isSysPreset) continue;
                dy.set(ORI_STATUS, (Object)"1");
            }
        }
    }

    private boolean getHisModelCondition(String billFormId) {
        return HisModelServiceHelper.isInheritHisModelTemplate((String)billFormId);
    }
}
