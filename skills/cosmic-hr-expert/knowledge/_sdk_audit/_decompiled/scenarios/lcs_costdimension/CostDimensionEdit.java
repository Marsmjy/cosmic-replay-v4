/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hrmp.lcs.common.constants.CostConstants
 */
package kd.hrmp.lcs.formplugin.web.basedata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hrmp.lcs.common.constants.CostConstants;

public class CostDimensionEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final String KEY_FILTERCONDITION = "filtercondition";
    private static final String KEY_FILTERDESC = "filterdesc";
    private static final String KEY_VALUESOURCE = "valuesource";
    private static final String KEY_ASSISTANT = "assistant";
    private static final String KEY_DISPLAYPROPERTY = "displayproperty";
    private static final String KEY_VALUETYPE = "valuetype";

    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addClickListeners(new String[]{KEY_FILTERCONDITION});
        BasedataEdit assistantEdit = (BasedataEdit)this.getControl(KEY_ASSISTANT);
        assistantEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit valueSourceEdit = (BasedataEdit)this.getControl(KEY_VALUESOURCE);
        valueSourceEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeBindData(EventObject e) {
        super.afterBindData(e);
        BasedataEdit basedataEdit = (BasedataEdit)this.getView().getControl(KEY_VALUESOURCE);
        basedataEdit.setMustInput(true);
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        String filterDesc = (String)this.getModel().getValue(KEY_FILTERCONDITION);
        this.getModel().setValue(KEY_FILTERCONDITION, (Object)filterDesc);
        this.getModel().setDataChanged(false);
    }

    public void propertyChanged(PropertyChangedArgs e) {
        String propName;
        super.propertyChanged(e);
        switch (propName = e.getProperty().getName()) {
            case "valuesource": {
                this.getModel().setValue(KEY_FILTERDESC, null);
                this.getModel().setValue(KEY_FILTERCONDITION, null);
                break;
            }
            case "valuetype": {
                String valueType = this.getModel().getDataEntity().getString(propName);
                if ("1".equals(valueType)) {
                    BasedataEdit basedataEdit = (BasedataEdit)this.getView().getControl(KEY_VALUESOURCE);
                    this.getModel().setValue(KEY_ASSISTANT, null);
                    basedataEdit.setMustInput(true);
                } else if ("2".equals(valueType)) {
                    BasedataEdit basedataEdit = (BasedataEdit)this.getView().getControl(KEY_ASSISTANT);
                    this.getModel().setValue(KEY_VALUESOURCE, null);
                    basedataEdit.setMustInput(true);
                }
                this.getModel().setValue(KEY_FILTERCONDITION, null);
                break;
            }
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        switch (operateKey = formOperate.getOperateKey()) {
            case "save": {
                String valueType = this.getModel().getDataEntity().getString(KEY_VALUETYPE);
                if (!"1".equals(valueType)) break;
                DynamicObject baseObjectType = this.getModel().getDataEntity().getDynamicObject(KEY_VALUESOURCE);
                if (baseObjectType == null) {
                    this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u201c\u503c\u6765\u6e90\u201d\u3002", (String)"CostDimensionEdit_0", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]));
                    args.setCancel(true);
                    return;
                }
                MainEntityType etType = EntityMetadataCache.getDataEntityType((String)baseObjectType.getString("number"));
                DataEntityPropertyCollection baseDataProprerties = etType.getProperties();
                ArrayList baseDataPropList = new ArrayList(baseDataProprerties.size());
                baseDataProprerties.stream().forEach(prop -> baseDataPropList.add(prop.getName()));
                String baseDataName = baseObjectType.getString("name");
                String displayProperty = this.getModel().getDataEntity().getString(KEY_DISPLAYPROPERTY);
                if (HRStringUtils.isEmpty((String)displayProperty)) {
                    if (baseDataPropList.contains("name")) break;
                    args.setCancel(true);
                    this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u57fa\u7840\u8d44\u6599\u201c%s\u201d\u65e0\u201c\u540d\u79f0\u201d\u5b57\u6bb5\u3002", (String)"CostDimensionEdit_4", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]), baseDataName));
                    return;
                }
                Map disPlayPropMap = CostConstants.getDisPlayPropMap();
                String displayProp = (String)disPlayPropMap.get(displayProperty);
                String[] displayArr = displayProp.split(",");
                int displayArrSize = displayArr.length;
                ArrayList<String> selectedPropList = new ArrayList<String>(displayArrSize);
                for (int i = 0; i < displayArrSize; ++i) {
                    selectedPropList.add(displayArr[i]);
                }
                if (!selectedPropList.contains("name")) {
                    selectedPropList.add("name");
                }
                selectedPropList.removeAll(baseDataPropList);
                if (selectedPropList.isEmpty()) break;
                args.setCancel(true);
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u57fa\u7840\u8d44\u6599\u201c{0}\u201d\u65e0{1}\u5b57\u6bb5\u3002", (String)"CostDimensionEdit_1", (String)"hrmp-lcs-formplugin", (Object[])new Object[]{baseDataName, this.convertStr(((Object)selectedPropList).toString())}));
                break;
            }
        }
    }

    private String convertStr(String tips) {
        tips = tips.replaceAll("number", ResManager.loadKDString((String)"\u7f16\u7801", (String)"CostDimensionEdit_2", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]));
        tips = tips.replaceAll("name", ResManager.loadKDString((String)"\u540d\u79f0", (String)"CostDimensionEdit_3", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]));
        return tips;
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String opKey;
        switch (opKey = beforeF7SelectEvent.getProperty().getName()) {
            case "assistant": {
                List customQFilters = beforeF7SelectEvent.getCustomQFilters();
                customQFilters.add(new QFilter("fbizcloudid", "in", Arrays.asList("0PEIU203SX4Y", "83bfebc800000bac")));
                break;
            }
            case "valuesource": {
                ListShowParameter para = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
                this.setShowParameterInfo(para);
                this.setHrBaseDataListFilter(para);
                break;
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        if ("lcs_costdimension".equals(evt.getActionId())) {
            ListSelectedRowCollection selectedRows = (ListSelectedRowCollection)evt.getReturnData();
            if (selectedRows == null || selectedRows.isEmpty()) {
                return;
            }
            ListSelectedRow listSelectedRow = selectedRows.get(0);
            Object primaryKeyValue = listSelectedRow.getPrimaryKeyValue();
            this.getView().getModel().setValue(KEY_VALUESOURCE, primaryKeyValue);
        }
    }

    private void setShowParameterInfo(ListShowParameter para) {
        para.setLookUp(true);
        para.setBillFormId("bos_entityobject");
        para.getOpenStyle().setShowType(ShowType.Modal);
        StyleCss css = new StyleCss();
        css.setWidth("960px");
        css.setHeight("580px");
        para.getOpenStyle().setInlineStyleCss(css);
        para.setFormId("bos_listf7");
        para.setF7Style(1);
        para.setMultiSelect(false);
        para.setShowTitle(false);
        para.setHasRight(true);
        para.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "lcs_costdimension"));
    }

    private void setHrBaseDataListFilter(ListShowParameter para) {
        List<String> hrCloudIdList = Arrays.asList("0PEIU203SX4Y", "0MUWQ6HSY5JA", "11FWSEDXBS99", "/U+QDTL900//", "1VUV4LLX7ZXE", "24BBHV84L8E9", "17+ZA9TOOB66", "13MN3ZU1+G54", "15DWQTD1X7EK", "0QLCSO6KKZC9");
        QFilter bizCloudQFilter = new QFilter("bizappid.bizcloud.id", "in", hrCloudIdList);
        bizCloudQFilter.and(new QFilter("modeltype", "=", (Object)"BaseFormModel"));
        bizCloudQFilter.and(new QFilter("number", "!=", (Object)"hbss_costcenter"));
        ListFilterParameter listFilterParameter = new ListFilterParameter();
        listFilterParameter.setFilter(bizCloudQFilter);
        para.setListFilterParameter(listFilterParameter);
    }
}
