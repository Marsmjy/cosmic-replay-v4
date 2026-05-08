/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin
 */
package kd.hr.haos.formplugin.web.database;

import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin;

public final class ChangeReasonListPlugin
extends ListOrderCommonPlugin {
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        setFilterEvent.getQFilters().add(new QFilter("id", "!=", (Object)1010L));
    }

    public void beforeShowBill(BeforeShowBillFormEvent e) {
        e.getParameter().setCaption(this.getView().getFormShowParameter().getCaption());
    }
}
