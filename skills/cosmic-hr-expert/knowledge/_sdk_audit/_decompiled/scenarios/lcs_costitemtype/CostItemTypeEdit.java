/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hrmp.lcs.formplugin.web.basedata.CostAbstractPlugIn
 */
package kd.hrmp.lcs.formplugin.web.basedata;

import java.util.EventObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.field.BasedataEdit;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hrmp.lcs.formplugin.web.basedata.CostAbstractPlugIn;

public class CostItemTypeEdit
extends CostAbstractPlugIn {
    public void propertyChanged(PropertyChangedArgs e) {
        String name = e.getProperty().getName();
        if ("areatype".equals(name)) {
            this.countryTypeChanged(e.getChangeSet()[0].getNewValue());
        }
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        String countryType = this.getModel().getDataEntity().getString("areatype");
        this.updateCountryView(countryType);
        this.getView().setEnable(Boolean.TRUE, new String[]{"number", "name"});
        this.getView().setVisible(Boolean.TRUE, new String[]{"btnsave"});
    }

    private void countryTypeChanged(Object areatypeType) {
        if (null == areatypeType) {
            return;
        }
        String value = String.valueOf(areatypeType);
        this.updateCountryView(value);
    }

    private void updateCountryView(String areatypeTypeValue) {
        BasedataEdit control = (BasedataEdit)this.getView().getControl("country");
        if (HRStringUtils.isEmpty((String)areatypeTypeValue) || "1".equals(areatypeTypeValue)) {
            control.setMustInput(false);
            this.getView().getModel().setValue("country", null);
        }
        if ("2".equals(areatypeTypeValue)) {
            control.setMustInput(true);
        }
    }
}
