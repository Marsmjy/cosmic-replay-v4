/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin
 */
package kd.hr.haos.formplugin.web.structures;

import java.util.HashSet;
import java.util.Set;
import kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin;

public final class StructProjectBUListPlugin
extends AbstractBUListPlugin {
    public Set<String> getCtrlBUFieldSet() {
        HashSet<String> ctrlBUFieldSet = new HashSet<String>();
        ctrlBUFieldSet.add("org");
        return ctrlBUFieldSet;
    }
}
