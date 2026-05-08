/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 */
package kd.hr.hrcs.formplugin.web.prompt;

import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;

public final class PromptRuleInitPlugin
extends HRBaseDataCommonEdit {
    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        Optional.ofNullable(this.getModel().getDataEntity().getPkValue()).ifPresent(primaryId -> {
            OrmLocaleValue controlName = (OrmLocaleValue)this.getModel().getValue("controlname");
            if (Objects.nonNull(controlName)) {
                this.getModel().setValue("controledit", (Object)controlName.getLocaleValue());
            }
        });
    }

    protected List<String> getUnCheckField() {
        List unCheckField = super.getUnCheckField();
        unCheckField.add("bonumber");
        unCheckField.add("controlname");
        unCheckField.add("controledit");
        return unCheckField;
    }
}
