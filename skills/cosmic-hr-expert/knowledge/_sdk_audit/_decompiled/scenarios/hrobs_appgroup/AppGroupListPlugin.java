/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.plugin.AbstractListPlugin
 */
package kd.hrmp.hrobs.formplugin.appgroup;

import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.plugin.AbstractListPlugin;

public final class AppGroupListPlugin
extends AbstractListPlugin {
    public void setFilter(SetFilterEvent event) {
        event.setOrderBy("index asc,modifytime desc");
    }
}
