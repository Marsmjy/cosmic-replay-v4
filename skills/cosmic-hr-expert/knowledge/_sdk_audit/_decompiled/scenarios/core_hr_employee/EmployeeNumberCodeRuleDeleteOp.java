/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.coderule.CodeRuleDeleteOp
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 */
package kd.hrmp.hrpi.opplugin.web.employee;

import java.util.List;
import kd.bos.coderule.CodeRuleDeleteOp;
import kd.bos.entity.plugin.PreparePropertysEventArgs;

public final class EmployeeNumberCodeRuleDeleteOp
extends CodeRuleDeleteOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List propertySet = e.getFieldKeys();
        propertySet.add("boid");
        propertySet.add("id");
        propertySet.add("globalperson");
        propertySet.add("number");
        propertySet.add("empnumber");
        propertySet.add("isprimary");
        propertySet.add("primaryemployee");
        propertySet.add("bsed");
        propertySet.add("bsled");
        propertySet.add("iscurrentversion");
    }

    protected String getBillNoField() {
        return "number";
    }
}
