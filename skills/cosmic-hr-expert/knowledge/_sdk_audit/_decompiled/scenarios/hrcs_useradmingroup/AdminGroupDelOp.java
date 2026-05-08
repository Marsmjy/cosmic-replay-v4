/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.operation.DeleteServiceHelper
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.common.constants.perm.HRAdminConstant
 */
package kd.hr.hrcs.opplugin.web.perm;

import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.operation.DeleteServiceHelper;
import kd.bos.util.StringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.common.constants.perm.HRAdminConstant;

public class AdminGroupDelOp
extends HRDataBaseOp
implements HRAdminConstant {
    private static final Log LOGGER = LogFactory.getLog(AdminGroupDelOp.class);

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        String focusNodeId = this.getOption().getVariableValue("focusNodeId", "");
        if (StringUtils.isEmpty((String)focusNodeId)) {
            LOGGER.info("FOCUS_NODE_ID_IS_EMPTY");
            return;
        }
        String[] arr = focusNodeId.split("_");
        Long adminGroupId = Long.parseLong(arr[0]);
        try {
            DeleteServiceHelper.delete((String)"perm_admingroupfunperm", (QFilter[])new QFilter[]{new QFilter("usergroup", "=", (Object)adminGroupId)});
            DeleteServiceHelper.delete((String)"perm_admingroupbizunit", (QFilter[])new QFilter[]{new QFilter("usergroup", "=", (Object)adminGroupId)});
            DeleteServiceHelper.delete((String)"perm_admingrouporg", (QFilter[])new QFilter[]{new QFilter("usergroup", "=", (Object)adminGroupId)});
            DeleteServiceHelper.delete((String)"perm_admingroupapp", (QFilter[])new QFilter[]{new QFilter("usergroup", "=", (Object)adminGroupId)});
            DeleteServiceHelper.delete((String)"perm_admingroupadduser", (QFilter[])new QFilter[]{new QFilter("usergroup", "=", (Object)adminGroupId)});
            DeleteServiceHelper.delete((String)"perm_admingroup", (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)adminGroupId)});
            DeleteServiceHelper.delete((String)"hrcs_admingrouporg", (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)adminGroupId)});
            DeleteServiceHelper.delete((String)"hrcs_admingroupfunc", (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)adminGroupId)});
            DeleteServiceHelper.delete((String)"hrcs_admingroupfile", (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)adminGroupId)});
        }
        catch (Exception ex) {
            LOGGER.error("\u5220\u9664\u7ba1\u7406\u5458\u7ec4\u5931\u8d25", (Throwable)ex);
            throw new KDBizException(ResManager.loadKDString((String)"\u5220\u9664\u5931\u8d25\u3002", (String)"AdminGroupTreeListPlugin_7", (String)"bos-permission-formplugin", (Object[])new Object[0]));
        }
    }
}
