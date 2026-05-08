/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin
 */
package kd.hr.haos.formplugin.web.database;

import kd.bos.form.FormShowParameter;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin;

public final class ChangeSceneListPlugin
extends ListOrderCommonPlugin {
    private static final Long ADMINISTRATIVE = 1010L;

    public void setFilter(SetFilterEvent event) {
        super.setFilter(event);
        FormShowParameter formParameter = this.getView().getFormShowParameter();
        Object otclassifyid = formParameter.getCustomParam("otclassify");
        if (otclassifyid == null) {
            otclassifyid = ADMINISTRATIVE;
        }
        event.getQFilters().add(new QFilter("otclassify.id", "=", otclassifyid));
        event.getQFilters().add(new QFilter("id", "!=", (Object)1070L));
    }

    public void beforeShowBill(BeforeShowBillFormEvent e) {
        e.getParameter().setCaption(this.getView().getFormShowParameter().getCaption());
    }

    public void filterColumnSetFilter(SetFilterEvent event) {
        super.filterColumnSetFilter(event);
        String fieldName = event.getFieldName();
        if ("orgchangetype.name".equals(fieldName)) {
            event.getCustomQFilters().add(new QFilter("orinumber", "!=", (Object)"1100_S"));
        }
    }
}
