/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.context.RequestContext
 *  kd.bos.form.IFormView
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.IListView
 *  kd.bos.list.plugin.AbstractListPlugin
 *  kd.bos.orm.query.QFilter
 */
package kd.hros.hrom.formplugin.route.apply;

import com.google.common.collect.Lists;
import java.util.List;
import kd.bos.context.RequestContext;
import kd.bos.form.IFormView;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.IListView;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QFilter;

public final class ApplyBaseListPlugin
extends AbstractListPlugin {
    public void setFilter(SetFilterEvent event) {
        IListView listView;
        String billFormId;
        super.setFilter(event);
        IFormView formView = this.getView();
        if (formView instanceof IListView && "hrom_applybill_emp".equals(billFormId = (listView = (IListView)formView).getBillFormId())) {
            QFilter bizDataFilter = new QFilter("applytype", "=", (Object)"1");
            bizDataFilter.and(new QFilter("creator", "=", (Object)RequestContext.get().getCurrUserId()));
            event.setCustomQFilters((List)Lists.newArrayList((Object[])new QFilter[]{bizDataFilter}));
        }
    }
}
