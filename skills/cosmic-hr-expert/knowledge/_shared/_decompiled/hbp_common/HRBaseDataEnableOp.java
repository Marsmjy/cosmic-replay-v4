/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hbp.opplugin.web.config;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import org.apache.commons.lang3.StringUtils;

public class HRBaseDataEnableOp
extends AbstractOperationServicePlugIn {
    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        DynamicObject[] dys = args.getDataEntities();
        String formId = this.billEntityType.getName();
        String ignoreResetEnable = this.getOption().getVariableValue("ignoreResetEnable", "false");
        if (HRStringUtils.equals((String)ignoreResetEnable, (String)"true")) {
            return;
        }
        String enableStatus = HRBaseDataConfigUtil.getEnableStatus((String)formId);
        for (DynamicObject dy : dys) {
            if (dy.getDataEntityState().getFromDatabase()) continue;
            if (StringUtils.equals((CharSequence)enableStatus, (CharSequence)"10")) {
                dy.set("enable", (Object)"10");
                continue;
            }
            if (StringUtils.equals((CharSequence)enableStatus, (CharSequence)"0")) {
                dy.set("enable", (Object)"10");
                continue;
            }
            dy.set("enable", (Object)"1");
        }
    }
}
