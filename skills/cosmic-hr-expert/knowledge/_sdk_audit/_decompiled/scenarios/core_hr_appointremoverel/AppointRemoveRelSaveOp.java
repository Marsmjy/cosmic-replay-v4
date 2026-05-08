/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.servicehelper.org.OrgUnitServiceHelper
 *  kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.AppointRemoveRelsaveOpValidater
 *  kd.sdk.hr.hpfs.business.utils.AppParamUtil
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.servicehelper.org.OrgUnitServiceHelper;
import kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.AppointRemoveRelsaveOpValidater;
import kd.sdk.hr.hpfs.business.utils.AppParamUtil;

public final class AppointRemoveRelSaveOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        Map variablesMap = this.getOption().getVariables();
        String hrdmInitSaveVar = (String)variablesMap.get("hr_hrdmsynctag_of_datasource");
        String hrdmInitValidateVar = (String)variablesMap.get("hr_hrdmvalidatetag_of_datasource");
        if ((HRStringUtils.isNotEmpty((String)hrdmInitSaveVar) || HRStringUtils.isNotEmpty((String)hrdmInitValidateVar)) && this.getopenSihccadm().booleanValue()) {
            e.addValidator((AbstractValidator)new AppointRemoveRelsaveOpValidater());
        }
    }

    public Boolean getopenSihccadm() {
        Long orgId = OrgUnitServiceHelper.getRootOrgId();
        Object isOpenSihcCadm = AppParamUtil.getAppParam((String)"XYRL3+A8Z+Z", (Long)orgId, (String)"isopencadm");
        return Objects.nonNull(isOpenSihcCadm) && isOpenSihcCadm.equals("2");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        Map variablesMap = this.getOption().getVariables();
        String hrdmInitSaveVar = (String)variablesMap.get("hr_hrdmsynctag_of_datasource");
        String hrdmInitValidateVar = (String)variablesMap.get("hr_hrdmvalidatetag_of_datasource");
        if ((HRStringUtils.isNotEmpty((String)hrdmInitSaveVar) || HRStringUtils.isNotEmpty((String)hrdmInitValidateVar)) && this.getopenSihccadm().booleanValue()) {
            this.initDismissDate(e.getDataEntities());
        }
        this.initializeMaxEffEndDate(e.getDataEntities());
        this.setRelationValue(e.getDataEntities());
    }

    private void initDismissDate(DynamicObject[] dataEntities) {
        if (dataEntities != null) {
            for (int i = 0; i < dataEntities.length; ++i) {
                LocalDate localSysMaxDate;
                DynamicObject dynamicObject = dataEntities[i];
                Date dismissDate = dynamicObject.getDate("dismissdate");
                LocalDate localDismissDate = dismissDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (localDismissDate.equals(localSysMaxDate = HRDateTimeUtils.getSysMaxDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                    dynamicObject.set("appointtypestatus", (Object)"1");
                    continue;
                }
                dynamicObject.set("appointtypestatus", (Object)"2");
            }
        }
    }

    private void setRelationValue(DynamicObject[] dataEntities) {
        for (DynamicObject dataEntity : dataEntities) {
            if (dataEntity.getDynamicObject("adminorg.belongcompany") != null) {
                dataEntity.set("company", (Object)dataEntity.getDynamicObject("adminorg.belongcompany"));
            }
            if (dataEntity.getDynamicObject("empposrel") != null) {
                dataEntity.set("isprimappoint", (Object)dataEntity.getBoolean("empposrel.isprimary"));
            }
            if (dataEntity.getDynamicObject("empposrel.postype") == null) continue;
            dataEntity.set("postype", (Object)dataEntity.getDynamicObject("empposrel.postype"));
        }
    }

    private void initializeMaxEffEndDate(DynamicObject[] dataEntities) {
        if (dataEntities != null) {
            for (int i = 0; i < dataEntities.length; ++i) {
                DynamicObject dynamicObject = dataEntities[i];
                if (dynamicObject.getDate("enddate") != null) continue;
                dynamicObject.set("enddate", (Object)TimeLineServiceUtil.getMaxEffEndDate());
            }
        }
    }
}
