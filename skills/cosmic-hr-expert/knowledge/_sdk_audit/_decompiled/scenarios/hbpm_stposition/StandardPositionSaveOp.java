/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 */
package kd.hrmp.hbpm.opplugin.web.position;

import java.util.Arrays;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hrmp.hbpm.opplugin.web.position.StandardPositionMsgHandleOp;

public final class StandardPositionSaveOp
extends StandardPositionMsgHandleOp {
    private static final Log logger = LogFactory.getLog(StandardPositionSaveOp.class);

    public void onAddValidators(AddValidatorsEventArgs args) {
    }

    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);
        DynamicObject[] positions = args.getDataEntities();
        if (positions.length == 0) {
            return;
        }
        Arrays.stream(positions).forEach(position -> position.set("isstandardpos", (Object)"1"));
    }
}

