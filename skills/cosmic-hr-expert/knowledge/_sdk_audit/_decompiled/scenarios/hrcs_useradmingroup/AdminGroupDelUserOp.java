/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.FormConfigFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.cache.helper.ConstantsHelper
 *  kd.bos.permission.cache.util.PermCommonUtil
 *  kd.bos.permission.log.enums.EnumPermBusiType
 *  kd.bos.permission.log.helper.ConstantsHelper
 *  kd.bos.permission.log.helper.PermAdminLogHelper
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.operation.DeleteServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.perm.hradmin.HRAdminGroupService
 *  kd.hr.hrcs.common.constants.perm.HRAdminConstant
 */
package kd.hr.hrcs.opplugin.web.perm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.FormConfigFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.cache.helper.ConstantsHelper;
import kd.bos.permission.cache.util.PermCommonUtil;
import kd.bos.permission.log.enums.EnumPermBusiType;
import kd.bos.permission.log.helper.PermAdminLogHelper;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.DeleteServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.perm.hradmin.HRAdminGroupService;
import kd.hr.hrcs.common.constants.perm.HRAdminConstant;

public class AdminGroupDelUserOp
extends HRDataBaseOp
implements HRAdminConstant {
    private static final Log LOGGER = LogFactory.getLog(AdminGroupDelUserOp.class);

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        String focusNodeId = this.getOption().getVariableValue("focusNodeId", "");
        if (HRStringUtils.isEmpty((String)focusNodeId)) {
            LOGGER.info("FOCUS_NODE_ID_IS_EMPTY");
            return;
        }
        ArrayList userAdminGroupIdList = Lists.newArrayListWithExpectedSize((int)e.getDataEntities().length);
        for (DynamicObject dataEntity : e.getDataEntities()) {
            userAdminGroupIdList.add(dataEntity.getLong("id"));
        }
        ArrayList<Long> userIds = new ArrayList<Long>(10);
        if (!"8609760E-EF83-4775-A9FF-CCDEC7C0B689".equalsIgnoreCase(focusNodeId)) {
            DynamicObject[] userAdminGroups;
            for (DynamicObject userAdminGroup : userAdminGroups = BusinessDataServiceHelper.load((String)"hrcs_useradmingroup", (String)"id, user.id", (QFilter[])new QFilter[]{new QFilter("id", "in", (Object)userAdminGroupIdList)})) {
                userIds.add(userAdminGroup.getLong("user.id"));
            }
            DeleteServiceHelper.delete((String)"hrcs_useradmingroup", (QFilter[])new QFilter[]{new QFilter("id", "in", (Object)userAdminGroupIdList)});
        } else {
            DynamicObject[] userAdminGroups;
            ArrayList<Long> deleteList = new ArrayList<Long>(userAdminGroupIdList.size());
            boolean delAll = true;
            StringBuilder notDelData = new StringBuilder();
            String superiorGroupIds = this.getOption().getVariableValue("superiorGroupIds", "");
            List superiorGroupId = (List)SerializationUtils.fromJsonString((String)superiorGroupIds, List.class);
            for (DynamicObject userAdminGroup : userAdminGroups = BusinessDataServiceHelper.load((String)"hrcs_useradmingroup", (String)"id, user.id, user.name, usergroup.id, usergroup.name", (QFilter[])new QFilter[]{new QFilter("id", "in", (Object)userAdminGroupIdList)})) {
                Long id = userAdminGroup.getLong("id");
                Long adminGroupId = userAdminGroup.getLong("usergroup.id");
                String adminGroupName = userAdminGroup.getString("usergroup.name");
                Long userId = userAdminGroup.getLong("user.id");
                String name = userAdminGroup.getString("user.name");
                if (!superiorGroupId.contains(adminGroupId)) {
                    deleteList.add(id);
                    userIds.add(userId);
                    continue;
                }
                delAll = false;
                notDelData.append('[').append(adminGroupName).append(" - ").append(name).append("]\r\n");
            }
            if (!deleteList.isEmpty()) {
                DeleteServiceHelper.delete((String)"hrcs_useradmingroup", (QFilter[])new QFilter[]{new QFilter("id", "in", deleteList)});
            }
            HashMap returnMap = Maps.newHashMapWithExpectedSize((int)2);
            returnMap.put("delAll", Boolean.toString(delAll));
            returnMap.put("count", deleteList.size() + "");
            returnMap.put("notDelData", notDelData.toString());
            this.getOperationResult().setCustomData((Map)returnMap);
        }
        String adminGroupId = focusNodeId.split("_")[0];
        if (PermCommonUtil.isEnablePermLog()) {
            String focusAdgNumber = this.getOption().getVariableValue("focusAdgNumber", "");
            String focusAdgName = this.getOption().getVariableValue("focusAdgName", "");
            String appId = this.getOption().getVariableValue("appId", "");
            String afterData = PermAdminLogHelper.adminEventImage((String)adminGroupId, (String)RequestContext.get().getLang().name(), new HashSet(userIds));
            String opbtn = ConstantsHelper.getDel();
            HRAdminGroupService.adminEvent2PermLog((String)appId, (String)"donothing_remove_user", (String)opbtn, (String)adminGroupId, (String)focusAdgNumber, (String)focusAdgName, (String)"", (String)afterData, (String)kd.bos.permission.log.helper.ConstantsHelper.getAdminDelBusifrom(), (EnumPermBusiType)EnumPermBusiType.ADMIN_DEL);
        }
        if (PermCommonUtil.isEnableAuthorityChangeNotice()) {
            try {
                FormConfigFactory.cancelShowFormRights(userIds);
            }
            catch (Exception ex) {
                LOGGER.error("[clearDynamicCache]\u6e05\u9664\u9886\u57df\u7f13\u5b58\u5f02\u5e38", (Throwable)ex);
            }
        }
    }
}
