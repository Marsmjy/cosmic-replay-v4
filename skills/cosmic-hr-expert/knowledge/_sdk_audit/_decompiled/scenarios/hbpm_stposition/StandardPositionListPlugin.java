/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.events.EnableCustomSumEvent
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hrmp.hbpm.formplugin.web.standardposition;

import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.events.EnableCustomSumEvent;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public final class StandardPositionListPlugin
extends HRDataBaseList {
    public void setEnableCustomSum(EnableCustomSumEvent args) {
        args.setEnableCustomSum(false);
        super.setEnableCustomSum(args);
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        setFilterEvent.setOrderBy("index asc,number asc");
    }
}

