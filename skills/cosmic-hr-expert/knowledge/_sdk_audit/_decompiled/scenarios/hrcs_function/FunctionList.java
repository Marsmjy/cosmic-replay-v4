/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.form.events.BeforeCreateListColumnsArgs
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hr.hrcs.formplugin.web.function;

import java.util.EventObject;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public final class FunctionList
extends HRDataBaseList {
    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
    }

    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
    }

    public void packageData(PackageDataEvent e) {
        super.packageData(e);
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        this.getView().setVisible(Boolean.FALSE, new String[]{"iscontainnow", "iscontainlower"});
    }
}
