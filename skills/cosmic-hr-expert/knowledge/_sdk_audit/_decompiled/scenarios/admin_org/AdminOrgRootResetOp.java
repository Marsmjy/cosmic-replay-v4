/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper
 *  kd.hr.haos.common.constants.AdminOrgDetailConstants
 *  kd.hr.hbp.business.service.history.util.HisModelCopyUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.orgfast;

import java.util.HashMap;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.property.BasedataProp;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper;
import kd.hr.haos.common.constants.AdminOrgDetailConstants;
import kd.hr.hbp.business.service.history.util.HisModelCopyUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class AdminOrgRootResetOp
extends HRDataBaseOp {
    private final String reset_one_msg = ResManager.loadKDString((String)"\u53ea\u80fd\u91cd\u7f6e\u4e00\u4e2a\u884c\u653f\u7ec4\u7ec7\u3002", (String)"AdminOrgRootResetOp_0", (String)"hrmp-haos-opplugin", (Object[])new Object[0]);
    private static Log LOGGER = LogFactory.getLog(AdminOrgRootResetOp.class);

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        LOGGER.error("AdminOrgRootResetOp beginOperationTransaction");
        DynamicObject[] adminOrgs = args.getDataEntities();
        if (adminOrgs.length > 1) {
            LOGGER.error("AdminOrgRootResetOp error, data length:{}", (Object)adminOrgs.length);
            throw new KDBizException(this.reset_one_msg);
        }
        DynamicObject rootOrg = adminOrgs[0];
        ((BasedataProp)rootOrg.getDataEntityType().getProperties().get((Object)"parentorg")).setMustInput(false);
        LOGGER.error("AdminOrgRootResetOp start saveRootOrg");
        OperationResult operationResult = this.saveRootOrg(rootOrg);
        LOGGER.error("AdminOrgRootResetOp end saveRootOrg");
        if (operationResult != null && !operationResult.isSuccess()) {
            Set resultSet = AdminOrgDetailHelper.parseOpResult((OperationResult)operationResult);
            LOGGER.error("AdminOrgRootResetOp saveRootOrg error, msg:{}", (Object)resultSet);
            String msg = String.join((CharSequence)",", resultSet);
            throw new KDBizException(msg);
        }
        LOGGER.error("AdminOrgRootResetOp start changeRootParent");
        operationResult = this.changeRootParent(rootOrg);
        LOGGER.error("AdminOrgRootResetOp end changeRootParent");
        if (operationResult != null && !operationResult.isSuccess()) {
            Set resultSet = AdminOrgDetailHelper.parseOpResult((OperationResult)operationResult);
            LOGGER.error("AdminOrgRootResetOp changeRootParent error, msg:{}", (Object)resultSet);
            String msg = String.join((CharSequence)",", resultSet);
            throw new KDBizException(msg);
        }
    }

    private OperationResult saveRootOrg(DynamicObject rootOrg) {
        long pkId = rootOrg.getLong("id");
        DynamicObject dbRootDyn = AdminOrgDetailHelper.getOrgDyById((Long)pkId);
        if (dbRootDyn != null) {
            return null;
        }
        HashMap<String, String> opParamMap = new HashMap<String, String>(16);
        opParamMap.put("adminorg_operation", "rootreset");
        opParamMap.put("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
        return new AdminOrgDetailHelper().executeOperate("save", new DynamicObject[]{rootOrg}, opParamMap);
    }

    private OperationResult changeRootParent(DynamicObject rootOrg) {
        Long currentRootOrgId = this.getCurrentRootOrgId();
        DynamicObject currentRootOrgDy = this.buildAdminOrg(currentRootOrgId, rootOrg);
        HashMap<String, String> opParamMap = new HashMap<String, String>(16);
        opParamMap.put("adminorg_operation", "rootreset");
        opParamMap.put("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
        return new AdminOrgDetailHelper().executeOperate("confirmchange", new DynamicObject[]{currentRootOrgDy}, opParamMap);
    }

    private Long getCurrentRootOrgId() {
        String oldRootIdStr = this.getOption().getVariableValue("oldrootid", null);
        if (HRStringUtils.isNotEmpty((String)oldRootIdStr)) {
            return Long.parseLong(oldRootIdStr);
        }
        return AdminOrgDetailHelper.getAdminOrgRootIdDyn();
    }

    public DynamicObject buildAdminOrg(Long adminOrgId, DynamicObject rootOrg) {
        QFilter idFilter = new QFilter("id", "=", (Object)adminOrgId);
        HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorgdetail");
        DynamicObject dbAdminOrg = helper.loadDynamicObject(new QFilter[]{idFilter});
        HisModelCopyUtil copyUtil = new HisModelCopyUtil();
        DynamicObject newAdminOrg = copyUtil.copyTempVersionData(dbAdminOrg);
        newAdminOrg.set("parentorg", (Object)rootOrg);
        newAdminOrg.set("changescene", (Object)AdminOrgDetailHelper.getChangeSceneById((Long)AdminOrgDetailConstants.ID_CHANGESCENE_PARENT));
        newAdminOrg.set("bsed", rootOrg.get("bsed"));
        return newAdminOrg;
    }
}
