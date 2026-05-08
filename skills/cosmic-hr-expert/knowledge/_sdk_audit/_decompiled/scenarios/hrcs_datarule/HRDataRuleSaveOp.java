/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.Tuple
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr
 *  kd.hr.hrcs.bussiness.service.perm.PermNotifyService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.log.DataRuleLogServiceHelper
 *  kd.hr.hrcs.common.constants.perm.log.DataRuleLogModel
 *  kd.hr.hrcs.opplugin.validator.HRDataRuleSaveValidator
 */
package kd.hr.hrcs.opplugin.web.perm;

import kd.bos.dataentity.Tuple;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr;
import kd.hr.hrcs.bussiness.service.perm.PermNotifyService;
import kd.hr.hrcs.bussiness.servicehelper.perm.log.DataRuleLogServiceHelper;
import kd.hr.hrcs.common.constants.perm.log.DataRuleLogModel;
import kd.hr.hrcs.opplugin.validator.HRDataRuleSaveValidator;

public final class HRDataRuleSaveOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(HRDataRuleSaveOp.class);

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new HRDataRuleSaveValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        for (int index = 0; index < args.getDataEntities().length; ++index) {
            DynamicObject item = args.getDataEntities()[index];
            long dataRuleId = item.getLong("id");
            if (0L == dataRuleId) continue;
            Tuple<Boolean, String> compareResult = this.compareFilterCondition(dataRuleId, item.getString("rule"));
            boolean changeFlag = (Boolean)compareResult.item1;
            String beforeRuleStr = (String)compareResult.item2;
            if (!changeFlag) continue;
            this.getOption().setVariableValue("operate_" + dataRuleId, "1");
            this.getOption().setVariableValue("originalRule_" + dataRuleId, beforeRuleStr);
            DataRuleLogModel dataRuleLogModel = DataRuleLogServiceHelper.getDataRuleLogModel((Object)dataRuleId, (boolean)true);
            this.getOption().setVariableValue("beforeData_" + dataRuleId, SerializationUtils.toJsonString((Object)dataRuleLogModel));
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        for (int index = 0; index < args.getDataEntities().length; ++index) {
            DynamicObject item = args.getDataEntities()[index];
            long dataRuleId = item.getLong("id");
            boolean isChange = HRStringUtils.equals((String)"1", (String)this.getOption().getVariableValue("operate_" + dataRuleId, "0"));
            String originalRule = this.getOption().getVariableValue("originalRule_" + dataRuleId, "");
            LOG.info("Got dataRule: {} with isChange: {}.", (Object)dataRuleId, (Object)isChange);
            if (isChange && !HRStringUtils.equals((String)originalRule, (String)item.getString("rule"))) {
                PermNotifyService.notifyByDataRule((Long)dataRuleId);
                String[] dataRuleCacheKeyArr = new String[]{HRPermCacheMgr.getTypeByPrefix((String)"BS_HR_PERM_DATA_RULE"), HRPermCacheMgr.getTypeByPrefix((String)"BS_HR_PERM_BD_DATA_RULE")};
                HRPermCacheMgr.clearCache((String[])dataRuleCacheKeyArr);
            }
            if (!isChange) continue;
            DataRuleLogModel dataRuleLogModel = DataRuleLogServiceHelper.getDataRuleLogModel((Object)dataRuleId, (boolean)false);
            String beforeDataStr = this.getOption().getVariableValue("beforeData_" + dataRuleId);
            DataRuleLogModel beforeData = (DataRuleLogModel)SerializationUtils.fromJsonString((String)beforeDataStr, DataRuleLogModel.class);
            dataRuleLogModel.setBeforeDataRuleModel(beforeData);
            DataRuleLogServiceHelper.dataRuleLogInit((String)"modify", (DataRuleLogModel)dataRuleLogModel);
        }
    }

    private Tuple<Boolean, String> compareFilterCondition(Object pkValue, String ruleStr) {
        HRBaseServiceHelper dataRuleServiceHelper = new HRBaseServiceHelper("hrcs_datarule");
        if (dataRuleServiceHelper.isExists(pkValue)) {
            DynamicObject dynamicObject = dataRuleServiceHelper.queryOne(pkValue);
            String beforelEntryNumber = dynamicObject.getString("entitynum.number");
            String beforeRule = dynamicObject.getString("rule");
            return Tuple.create((Object)DataRuleLogServiceHelper.compareFilterControls((String)beforelEntryNumber, (String)beforeRule, (String)ruleStr), (Object)beforeRule);
        }
        return Tuple.create((Object)false, (Object)"");
    }
}
