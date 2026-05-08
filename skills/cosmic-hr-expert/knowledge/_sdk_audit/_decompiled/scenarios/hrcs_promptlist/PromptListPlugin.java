/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.ValueTextItem
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.BillList
 *  kd.bos.list.F7SelectedList
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.BuildTreeListFilterEvent
 *  kd.bos.list.events.ListRowClickEvent
 *  kd.bos.orm.query.QFilter
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.servicehelper.prompt.PromptServiceHelper
 *  kd.hr.hrcs.formplugin.web.prompt.utils.PromptTreeList
 */
package kd.hr.hrcs.formplugin.web.prompt;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ValueTextItem;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.BillList;
import kd.bos.list.F7SelectedList;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.BuildTreeListFilterEvent;
import kd.bos.list.events.ListRowClickEvent;
import kd.bos.orm.query.QFilter;
import kd.bos.util.StringUtils;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.servicehelper.prompt.PromptServiceHelper;
import kd.hr.hrcs.formplugin.web.prompt.utils.PromptTreeList;

@ExcludeFromJacocoGeneratedReport
public final class PromptListPlugin
extends PromptTreeList
implements ClickListener {
    public PromptListPlugin() {
        super("");
    }

    protected QFilter buildNodeClickFilter(BuildTreeListFilterEvent evt) {
        String nodeId = String.valueOf(evt.getNodeId());
        return !HRStringUtils.equals((String)"1010", (String)nodeId) ? super.getNodeFilter(nodeId) : null;
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        QFilter languageFilter = new QFilter("hrcs_promptcontent.locale.number", "=", (Object)RequestContext.get().getLang().name());
        languageFilter.or(new QFilter("hrcs_promptcontent.prompt", "is null", null));
        setFilterEvent.getQFilters().add(languageFilter);
        super.setFilter(setFilterEvent);
    }

    public void listRowClick(ListRowClickEvent evt) {
        ListShowParameter listShowParameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (!listShowParameter.isLookUp()) {
            return;
        }
        F7SelectedList selectedList = (F7SelectedList)this.getView().getControl("selectedlistap");
        if (selectedList == null) {
            return;
        }
        BillList billlist = (BillList)evt.getSource();
        ListSelectedRowCollection listRow = billlist.getSelectedRows();
        this.addItems(selectedList, listRow);
    }

    private void addItems(F7SelectedList selectedList, ListSelectedRowCollection listRow) {
        int size = listRow.size();
        ArrayList<Long> pks = new ArrayList<Long>(size);
        for (ListSelectedRow row : listRow) {
            pks.add(Long.valueOf(row.getPrimaryKeyValue().toString()));
        }
        List<ValueTextItem> valueTextItems = this.getValueTextItems(pks);
        selectedList.addItems(valueTextItems);
    }

    private List<ValueTextItem> getValueTextItems(List<Long> pks) {
        ArrayList valueTextItems = Lists.newArrayListWithCapacity((int)pks.size());
        DynamicObject[] prompts = PromptServiceHelper.getPrompts(pks);
        Map<Long, String> promptMap = Arrays.stream(prompts).collect(Collectors.toMap(prompt -> prompt.getLong("id"), prompt -> prompt.getString("name"), (oldValue, newValue) -> newValue));
        pks.forEach(pk -> {
            String name = (String)promptMap.get(pk);
            if (!StringUtils.isEmpty((String)name)) {
                ValueTextItem item = new ValueTextItem(pk.toString(), name);
                valueTextItems.add(item);
            }
        });
        return valueTextItems;
    }
}
