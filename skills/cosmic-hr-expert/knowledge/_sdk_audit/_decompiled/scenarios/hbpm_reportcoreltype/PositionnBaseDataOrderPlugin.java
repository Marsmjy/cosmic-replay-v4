/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.events.SetFilterEvent
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hrmp.hbpm.formplugin.web.basedata;

import kd.bos.form.events.SetFilterEvent;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public final class PositionnBaseDataOrderPlugin
extends HRDataBaseList {
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        setFilterEvent.setOrderBy("createorg.id asc,index asc");
    }
}

