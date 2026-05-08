/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  kd.bos.form.events.SetFilterEvent
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hr.haos.formplugin.web.database;

import kd.bos.form.events.SetFilterEvent;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public final class AdminorgtypeListPlugin
extends HRDataBaseList {
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        setFilterEvent.setOrderBy("enable desc,adminorgtypestd.number asc,number asc");
    }
}
