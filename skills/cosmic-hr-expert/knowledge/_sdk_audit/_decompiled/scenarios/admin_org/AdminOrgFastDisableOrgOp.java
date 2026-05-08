/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.exception.KDBizException
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgStrategyService
 *  kd.hr.haos.business.domain.org.service.AdminChangeMsgService
 *  kd.hr.haos.business.infrastructure.client.platformorg.AdminOrgUnitSyncService
 *  kd.hr.hbp.business.service.history.util.HisModelCopyUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.orgfast;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.exception.KDBizException;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgStrategyService;
import kd.hr.haos.business.domain.org.service.AdminChangeMsgService;
import kd.hr.haos.business.infrastructure.client.platformorg.AdminOrgUnitSyncService;
import kd.hr.hbp.business.service.history.util.HisModelCopyUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class AdminOrgFastDisableOrgOp
extends HRDataBaseOp {
    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] adminOrgs = this.buildAdminOrg(args);
        OperateOption operateOption = OperateOption.create();
        operateOption.mergeValue(this.getOption());
        operateOption.setVariableValue("OP_SKIP_ADMIN_NAME_SPECIAL_SYMBOL_CHECK", "true");
        OperationResult operationResult = new AdminOrgDetailHelper().executeOperate("confirmchange", adminOrgs, "disableorg", operateOption);
        if (operationResult == null) {
            return;
        }
        if (!operationResult.isSuccess()) {
            StringBuilder msgBuilder = new StringBuilder();
            operationResult.getValidateResult().getValidateErrors().forEach(validateResult -> validateResult.getAllErrorInfo().forEach(operateErrorInfo -> msgBuilder.append(operateErrorInfo.getMessage())));
            throw new KDBizException(msgBuilder.toString());
        }
        new AdminOrgDetailHelper().adminOrgCoopHandle("disableorg", args.getDataEntities());
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        new AdminChangeMsgService().handleChangeMsg();
        Date now = HRDateTimeUtils.getNowDate();
        AdminOrgUnitSyncService.getInstance().disableOrEnableOrgChgUnit((DynamicObject[])Arrays.stream(args.getDataEntities()).filter(dyn -> !HRDateTimeUtils.dayAfter((Date)dyn.getDate("bsed"), (Date)now)).toArray(DynamicObject[]::new), "disable");
        List adminOrgIdList = Arrays.stream(args.getDataEntities()).filter(dyn -> !HRDateTimeUtils.dayAfter((Date)dyn.getDate("bsed"), (Date)now)).map(adminOrg -> adminOrg.getLong("id")).collect(Collectors.toList());
        OrgStrategyService.getInstance().disableOrgStrategy(adminOrgIdList);
    }

    public DynamicObject[] buildAdminOrg(BeginOperationTransactionArgs args) {
        List adminOrgIdList = Arrays.stream(args.getDataEntities()).map(adminOrg -> adminOrg.getLong("id")).collect(Collectors.toList());
        QFilter idFilter = new QFilter("id", "in", adminOrgIdList);
        HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorgdetail");
        DynamicObject[] dbAdminOrgs = helper.loadDynamicObjectArray("haos_adminorgdetail", new QFilter[]{idFilter});
        Map<Long, DynamicObject> dbAdminOrgIdDynMap = Arrays.stream(dbAdminOrgs).collect(Collectors.toMap(adminOrg -> adminOrg.getLong("id"), adminOrg -> adminOrg, (k1, k2) -> k2));
        int index = 0;
        DynamicObject[] adminOrgs = new DynamicObject[args.getDataEntities().length];
        HisModelCopyUtil copyUtil = new HisModelCopyUtil();
        for (DynamicObject dyn : args.getDataEntities()) {
            long adminOrgId = dyn.getLong("id");
            DynamicObject dbAdminOrg = dbAdminOrgIdDynMap.get(adminOrgId);
            DynamicObject newAdminOrg = copyUtil.copyTempVersionData(dbAdminOrg);
            this.copyBizField(newAdminOrg, dyn);
            adminOrgs[index++] = newAdminOrg;
        }
        return adminOrgs;
    }

    private void copyBizField(DynamicObject adminOrg, DynamicObject oldAdminOrg) {
        DynamicObject changeScene = oldAdminOrg.getDynamicObject("changescene");
        adminOrg.set("changescene", (Object)changeScene);
        adminOrg.set("changetype", changeScene.get("orgchangetype"));
        adminOrg.set("changereason", oldAdminOrg.get("changereason"));
        adminOrg.set("changedescription", oldAdminOrg.get("changedescription"));
        adminOrg.set("bsed", oldAdminOrg.get("bsed"));
        adminOrg.set("enable", (Object)"0");
        adminOrg.set("disabledate", (Object)oldAdminOrg.getDate("bsed"));
        adminOrg.set("disabler", (Object)RequestContext.get().getCurrUserId());
        adminOrg.set("billid", oldAdminOrg.get("billid"));
        if (adminOrg.getBoolean("tobedisableflag")) {
            adminOrg.set("tobedisableflag", (Object)false);
            adminOrg.set("tobedisabledate", null);
        }
    }
}
