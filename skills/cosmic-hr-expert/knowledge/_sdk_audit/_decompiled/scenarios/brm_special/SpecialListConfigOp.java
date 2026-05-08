/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.brm.business.util.SpecialListUtil
 *  kd.hr.brm.opplugin.validator.SpecialListValidator
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.brm.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.brm.business.util.SpecialListUtil;
import kd.hr.brm.opplugin.validator.SpecialListValidator;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class SpecialListConfigOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new SpecialListValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs trans) {
        if ("save".equals(trans.getOperationKey())) {
            String listType = trans.getDataEntities()[0].getString("listtype");
            if (HRStringUtils.equals((String)"string", (String)listType.toString())) {
                trans.getDataEntities()[0].set("listcategory", null);
                trans.getDataEntities()[0].set("brm_list_person", null);
                trans.getDataEntities()[0].set("brm_list_emp", null);
                trans.getDataEntities()[0].set("brm_list_org", null);
            } else if (HRStringUtils.equals((String)"basedata", (String)listType.toString())) {
                trans.getDataEntities()[0].set("list_string_param", null);
                String listCategory = trans.getDataEntities()[0].getString("listcategory");
                if (null == listCategory) {
                    trans.getDataEntities()[0].set("brm_list_org", null);
                    trans.getDataEntities()[0].set("brm_list_person", null);
                    trans.getDataEntities()[0].set("brm_list_emp", null);
                } else if (HRStringUtils.equals((String)"haos_adminorghrf7", (String)listCategory.toString())) {
                    trans.getDataEntities()[0].set("brm_list_person", null);
                    trans.getDataEntities()[0].set("brm_list_emp", null);
                } else if (SpecialListUtil.isEmployee((String)listCategory.toString())) {
                    trans.getDataEntities()[0].set("brm_list_person", null);
                    trans.getDataEntities()[0].set("brm_list_org", null);
                } else if (SpecialListUtil.isUser((String)listCategory.toString())) {
                    trans.getDataEntities()[0].set("brm_list_org", null);
                    trans.getDataEntities()[0].set("brm_list_emp", null);
                }
            }
        }
    }
}
