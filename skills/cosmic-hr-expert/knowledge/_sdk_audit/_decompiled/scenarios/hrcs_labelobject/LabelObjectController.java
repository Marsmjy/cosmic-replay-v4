/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.AbstractBasedataController
 *  kd.bos.form.field.events.BaseDataCustomControllerEvent
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 */
package kd.hr.hrcs.formplugin.web.label.controller;

import java.util.List;
import kd.bos.base.AbstractBasedataController;
import kd.bos.form.field.events.BaseDataCustomControllerEvent;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;

public final class LabelObjectController
extends AbstractBasedataController {
    private static final long serialVersionUID = 5327682443161237607L;

    public void buildBaseDataCoreFilter(BaseDataCustomControllerEvent event) {
        super.buildBaseDataCoreFilter(event);
        ListShowParameter listShowParameter = event.getListShowParameter();
        List qFilters = listShowParameter.getListFilterParameter().getQFilters();
        qFilters.add(new QFilter("publishstatus", "=", (Object)"1"));
    }
}
