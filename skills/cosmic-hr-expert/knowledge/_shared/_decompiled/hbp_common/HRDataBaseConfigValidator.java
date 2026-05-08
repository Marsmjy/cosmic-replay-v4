/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.constants.history.EntityInheritTypeEnum
 *  kd.hr.hbp.common.util.HRQFilterHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.validator.HRDataBaseValidator
 *  kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper
 *  org.apache.commons.lang3.ObjectUtils
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hbp.opplugin.validator;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.constants.history.EntityInheritTypeEnum;
import kd.hr.hbp.common.util.HRQFilterHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.validator.HRDataBaseValidator;
import kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class HRDataBaseConfigValidator
extends HRDataBaseValidator {
    public void validate() {
        ExtendedDataEntity[] datas = this.getDataEntities();
        String operationKey = this.getOperateKey();
        if ("delete".equals(operationKey) || "save".equals(operationKey)) {
            String formId = datas[0].getDataEntity().getDynamicObjectType().getName();
            EntityInheritTypeEnum entityInheritType = HisModelServiceHelper.queryEntityModelType((String)formId);
            boolean hasHis = entityInheritType == EntityInheritTypeEnum.NO_INTERRUPTION_NO_OVERLAP || entityInheritType == EntityInheritTypeEnum.ONLY_ONE_EFFECT_VERSION;
            boolean audit = HRBaseDataConfigUtil.getAudit((String)formId);
            for (ExtendedDataEntity extendedDataEntity : datas) {
                String initHisModelSave;
                DynamicObject dy = extendedDataEntity.getDataEntity();
                if (!hasHis && StringUtils.equals((CharSequence)dy.getString("status"), (CharSequence)"A") || !audit || HRStringUtils.equals((String)(initHisModelSave = this.getOption().getVariableValue("forceNoAudit", "false")), (String)"true")) continue;
                this.addMessage(extendedDataEntity, operationKey, hasHis);
            }
        }
    }

    public void addMessage(ExtendedDataEntity extendedDataEntity, String operationKey, boolean hasHis) {
        boolean nonTempSave;
        DynamicObject curDyn = extendedDataEntity.getDataEntity();
        boolean bl = nonTempSave = !StringUtils.equals((CharSequence)curDyn.getString("status"), (CharSequence)"A");
        if (nonTempSave && "delete".equals(operationKey)) {
            this.addFatalErrorMessage(extendedDataEntity, ResManager.loadKDString((String)"\u53ea\u80fd\u5220\u9664\u6682\u5b58\u7684\u6570\u636e\u3002", (String)"HRDataBaseConfigValidator_0", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
            return;
        }
        boolean isUpd = curDyn.getDataEntityState().getFromDatabase();
        if (hasHis) {
            String curDynStatus = curDyn.getString("status");
            String curDynEnable = curDyn.getString("enable");
            if (isUpd) {
                QFilter isCurrentQFilter;
                QFilter idQFilter;
                String entityId = extendedDataEntity.getDataEntity().getDataEntityType().getName();
                HRBaseServiceHelper helper = new HRBaseServiceHelper(entityId);
                DynamicObject dbDyn = helper.queryOne("id, enable, status", new QFilter[]{idQFilter = HRQFilterHelper.buildEql((String)"id", (Object)curDyn.getPkValue()), isCurrentQFilter = HRQFilterHelper.buildEql((String)"iscurrentversion", (Object)Boolean.TRUE)});
                if (ObjectUtils.isEmpty((Object)dbDyn)) {
                    boolean nonEnabling;
                    boolean isAudited = StringUtils.equals((CharSequence)curDynStatus, (CharSequence)"C");
                    boolean bl2 = nonEnabling = !StringUtils.equals((CharSequence)curDynEnable, (CharSequence)"10");
                    if (isAudited && nonEnabling) {
                        return;
                    }
                } else {
                    boolean nonEnabling;
                    String dbDynStatus = dbDyn.getString("status");
                    String dbDynEnable = dbDyn.getString("enable");
                    boolean isAudited = StringUtils.equals((CharSequence)dbDynStatus, (CharSequence)"C");
                    boolean bl3 = nonEnabling = !StringUtils.equals((CharSequence)dbDynEnable, (CharSequence)"10");
                    if (isAudited) {
                        if (nonEnabling) {
                            return;
                        }
                        nonTempSave = !StringUtils.equals((CharSequence)dbDynStatus, (CharSequence)"A");
                    }
                }
            } else {
                boolean nonEnabling;
                boolean isAudited = StringUtils.equals((CharSequence)curDynStatus, (CharSequence)"C");
                boolean bl4 = nonEnabling = !StringUtils.equals((CharSequence)curDynEnable, (CharSequence)"10");
                if (isAudited && nonEnabling) {
                    return;
                }
            }
        }
        if (isUpd && nonTempSave) {
            this.addFatalErrorMessage(extendedDataEntity, ResManager.loadKDString((String)"\u53ea\u80fd\u4fee\u6539\u6682\u5b58\u7684\u6570\u636e\u3002", (String)"HRDataBaseConfigValidator_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
        }
    }
}
