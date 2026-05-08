/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.events.SetFilterEvent
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hrmp.hbjm.formplugin.web.common;

import kd.bos.form.events.SetFilterEvent;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public final class JobBaseDataListPlugin
extends HRDataBaseList {
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        String commonOrderBy = "createorg asc,enable desc,number asc";
        setFilterEvent.setOrderBy(commonOrderBy);
    }
}
