/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin
 */
package kd.hr.homs.formplugin.web.changedetail;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Set;
import kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin;

public final class AdminOrgChgRecordBUListPlugin
extends AbstractBUListPlugin {
    private final Set<String> fieldSet = Sets.newConcurrentHashSet(Arrays.asList("adminorg", "adminorg.org", "searchchangescene", "adminorg.adminorgtype", "adminorg.parentorg", "adminorg.corporateorg", "adminorg.belongcompany", "adminorg.adminorglayer", "adminorg.adminorgfunction", "changescene", "changereason", "chgbill.disorg.fbasedataid"));

    public Set<String> getCtrlBUFieldSet() {
        return this.fieldSet;
    }
}
