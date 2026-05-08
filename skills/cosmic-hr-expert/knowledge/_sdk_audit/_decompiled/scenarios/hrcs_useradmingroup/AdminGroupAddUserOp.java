/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.FormConfigFactory
 *  kd.bos.permission.cache.helper.ConstantsHelper
 *  kd.bos.permission.cache.util.PermCommonUtil
 *  kd.bos.permission.log.enums.EnumPermBusiType
 *  kd.bos.permission.log.helper.ConstantsHelper
 *  kd.bos.permission.log.helper.PermAdminLogHelper
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.operation.SaveServiceHelper
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.perm.hradmin.HRAdminGroupService
 *  kd.hr.hrcs.common.constants.perm.HRAdminConstant
 */
package kd.hr.hrcs.opplugin.web.perm;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.FormConfigFactory;
import kd.bos.permission.cache.helper.ConstantsHelper;
import kd.bos.permission.cache.util.PermCommonUtil;
import kd.bos.permission.log.enums.EnumPermBusiType;
import kd.bos.permission.log.helper.PermAdminLogHelper;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.perm.hradmin.HRAdminGroupService;
import kd.hr.hrcs.common.constants.perm.HRAdminConstant;

public class AdminGroupAddUserOp
extends HRDataBaseOp
implements HRAdminConstant {
    private static final Log LOGGER = LogFactory.getLog(AdminGroupAddUserOp.class);

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        String adminGroupId = null;
        ArrayList userIdList = Lists.newArrayListWithExpectedSize((int)e.getDataEntities().length);
        for (DynamicObject dataEntity : e.getDataEntities()) {
            adminGroupId = dataEntity.getString("usergroup.id");
            userIdList.add(dataEntity.getLong("user.id"));
        }
        if (adminGroupId == null) {
            LOGGER.info("ADMIN_GROUP_ID_IS_EMPTY");
            return;
        }
        List<Long> saveUserIds = this.saveUserAdminGroup(adminGroupId, userIdList);
        if (PermCommonUtil.isEnableAuthorityChangeNotice()) {
            try {
                FormConfigFactory.cancelShowFormRights(saveUserIds);
            }
            catch (Exception ex) {
                LOGGER.error("[clearDynamicCache]\u6e05\u9664\u9886\u57df\u7f13\u5b58\u5f02\u5e38", (Throwable)ex);
            }
        }
        String appId = this.getOption().getVariableValue("appId", "");
        if (PermCommonUtil.isEnablePermLog()) {
            String focusAdgNumber = this.getOption().getVariableValue("focusAdgNumber", "");
            String focusAdgName = this.getOption().getVariableValue("focusAdgName", "");
            String afterData = PermAdminLogHelper.adminEventImage((String)adminGroupId, (String)RequestContext.get().getLang().name(), (Set)Sets.newHashSet((Iterable)userIdList));
            String opbtn = ConstantsHelper.getAdd();
            HRAdminGroupService.adminEvent2PermLog((String)appId, (String)"bar_add", (String)opbtn, (String)adminGroupId, (String)focusAdgNumber, (String)focusAdgName, (String)"", (String)afterData, (String)kd.bos.permission.log.helper.ConstantsHelper.getAdminAddBusifrom(), (EnumPermBusiType)EnumPermBusiType.ADMIN_ADD);
        }
        HRAdminGroupService.writeOpLog((boolean)true, (String)appId);
    }

    private List<Long> saveUserAdminGroup(String adminGroupId, List<Long> userIdSet) {
        ArrayList saveList = new ArrayList(10);
        ArrayList<Long> saveUserIds = new ArrayList<Long>(10);
        userIdSet.forEach(primaryKeyValue -> {
            DynamicObject userAdminGroup = BusinessDataServiceHelper.newDynamicObject((String)"hrcs_useradmingroup");
            userAdminGroup.set("user", primaryKeyValue);
            userAdminGroup.set("usergroup", (Object)Long.valueOf(adminGroupId));
            saveList.add(userAdminGroup);
            saveUserIds.add((Long)primaryKeyValue);
        });
        SaveServiceHelper.save((DynamicObject[])saveList.toArray(new DynamicObject[0]));
        return saveUserIds;
    }
}
