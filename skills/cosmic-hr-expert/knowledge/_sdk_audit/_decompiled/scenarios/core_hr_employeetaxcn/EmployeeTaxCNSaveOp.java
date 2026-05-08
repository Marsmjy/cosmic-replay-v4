/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.opplugin.web.employee.validator.EmployeeTaxCNSaveValidator
 */
package kd.hrmp.hrpi.opplugin.web.employee;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.util.StringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.opplugin.web.employee.validator.EmployeeTaxCNSaveValidator;

public final class EmployeeTaxCNSaveOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("assignment.id");
        e.getFieldKeys().add("taxunit.id");
        e.getFieldKeys().add("startdate");
        e.getFieldKeys().add("enddate");
        e.getFieldKeys().add("taxunit.name");
        e.getFieldKeys().add("credentialstypename");
        e.getFieldKeys().add("percrenumber");
        e.getFieldKeys().add("employee.nationality.id");
        e.getFieldKeys().add("employee.birthday.id");
        e.getFieldKeys().add("employee.gender.id");
        e.getFieldKeys().add("taxstatus");
        e.getFieldKeys().add("empdate");
        e.getFieldKeys().add("quitdate");
        e.getFieldKeys().add("firstentrydate");
        e.getFieldKeys().add("departdate");
        e.getFieldKeys().add("regpermres.id");
        e.getFieldKeys().add("regpermres.level");
        e.getFieldKeys().add("regpermres.country.id");
        e.getFieldKeys().add("regpermrescity.id");
        e.getFieldKeys().add("regpermrescounty.id");
        e.getFieldKeys().add("regpermrescity.parent.id");
        e.getFieldKeys().add("regpermrescounty.parent.id");
        e.getFieldKeys().add("habitres.id");
        e.getFieldKeys().add("habitres.level");
        e.getFieldKeys().add("habitres.country.id");
        e.getFieldKeys().add("habitrescity.id");
        e.getFieldKeys().add("habitrescounty.id");
        e.getFieldKeys().add("habitrescity.parent.id");
        e.getFieldKeys().add("habitrescounty.parent.id");
        e.getFieldKeys().add("birthplace");
        e.getFieldKeys().add("address.id");
        e.getFieldKeys().add("address.level");
        e.getFieldKeys().add("address.country.id");
        e.getFieldKeys().add("addresscity.id");
        e.getFieldKeys().add("addresscounty.id");
        e.getFieldKeys().add("addresscity.parent.id");
        e.getFieldKeys().add("addresscounty.parent.id");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new EmployeeTaxCNSaveValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
        for (DynamicObject data : e.getDataEntities()) {
            boolean isResidentIDCard;
            Object credentialType;
            String empType = data.getString("emptype");
            if ("4".equals(empType) || "3".equals(empType)) {
                data.set("empsituation", null);
            }
            if ((credentialType = data.get("credentialstype")) != null) {
                long credentialTypeId = 0L;
                if (credentialType instanceof DynamicObject) {
                    credentialTypeId = ((DynamicObject)credentialType).getLong("id");
                } else if (credentialType instanceof Long) {
                    credentialTypeId = (Long)credentialType;
                }
                isResidentIDCard = 1010L == credentialTypeId;
            } else {
                isResidentIDCard = false;
            }
            if (isResidentIDCard) {
                data.set("taxpayernum", (Object)data.getString("percrenumber"));
                data.set("chinesename", null);
                data.set("taxreason", null);
                data.set("otheridtype", null);
                data.set("birthplace", null);
                data.set("firstentrydate", null);
                data.set("departdate", null);
            }
            if (StringUtils.isEmpty((String)data.getString("otheridtype"))) {
                data.set("otheridnumber", null);
            }
            if (!"1".equals(data.getString("disability"))) {
                data.set("disabilitynum", null);
            }
            if (!"1".equals(data.getString("martyrsfamily"))) {
                data.set("martyrsfamilynum", null);
            }
            if (data.getDynamicObject("regpermres") == null) {
                data.set("regpermrescity", null);
            }
            if (data.getDynamicObject("habitres") == null) {
                data.set("habitrescity", null);
            }
            if (data.getDynamicObject("address") == null) {
                data.set("addresscity", null);
            }
            if (data.getDynamicObject("regpermrescity") == null) {
                data.set("regpermrescounty", null);
            }
            if (data.getDynamicObject("habitrescity") == null) {
                data.set("habitrescounty", null);
            }
            if (data.getDynamicObject("addresscity") != null) continue;
            data.set("addresscounty", null);
        }
    }
}
