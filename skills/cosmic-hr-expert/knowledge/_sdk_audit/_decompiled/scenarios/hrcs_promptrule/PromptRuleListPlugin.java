/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.plugin.AbstractListPlugin
 */
package kd.hr.hrcs.formplugin.web.prompt;

import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.plugin.AbstractListPlugin;

public final class PromptRuleListPlugin
extends AbstractListPlugin {
    public void setFilter(SetFilterEvent e) {
        e.setOrderBy("modifytime desc");
    }
}
