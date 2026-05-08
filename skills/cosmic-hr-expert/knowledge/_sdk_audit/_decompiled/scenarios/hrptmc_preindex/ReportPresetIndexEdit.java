/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrptmc.common.constant.preindex.PresetIndexConstants
 *  kd.hr.hrptmc.common.util.ReportCommonUtils
 */
package kd.hr.hrptmc.formplugin.web.preindex;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrptmc.common.constant.preindex.PresetIndexConstants;
import kd.hr.hrptmc.common.util.ReportCommonUtils;

public final class ReportPresetIndexEdit
extends HRDataBaseEdit
implements PresetIndexConstants,
BeforeF7SelectListener {
    private static final Pattern NUMBER_START_PATTERN = Pattern.compile("^[0-9]");
    private static final Log LOGGER = LogFactory.getLog(ReportPresetIndexEdit.class);

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        this.addItemClickListeners(new String[]{"paramtbmain"});
        this.addItemClickListeners(new String[]{"coltbmain"});
        BasedataEdit anObjEdit = (BasedataEdit)this.getControl("anobj");
        if (anObjEdit != null) {
            anObjEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        ListShowParameter formShowParameter = (ListShowParameter)evt.getFormShowParameter();
        QFilter qFilter = new QFilter("objecttype", "=", (Object)"multientity");
        formShowParameter.getListFilterParameter().setFilter(qFilter);
    }

    public void beforeBindData(EventObject event) {
        super.beforeBindData(event);
        this.showBusInfoByGetWay(false);
        HashMap map = new HashMap(1);
        HashMap<String, String> item = new HashMap<String, String>(1);
        map.put("item", item);
        if (ReportCommonUtils.validateNumberByISVIsKD()) {
            item.put("emptytip", String.format(ResManager.loadKDString((String)"%s\u5f00\u5934\uff0c\u652f\u6301\u82f1\u6587\u3001\u6570\u5b57\u548c\u4e0b\u5212\u7ebf\u3002", (String)"ReportPresetIndexEdit_11", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), "kdhr_"));
        } else {
            item.put("emptytip", ResManager.loadKDString((String)"\u4ec5\u652f\u6301\u82f1\u6587\u5c0f\u5199\u3001\u6570\u5b57\u548c\u4e0b\u5212\u7ebf\u3002", (String)"ReportPresetIndexEdit_12", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
        }
        this.getView().updateControlMetadata("number", map);
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        if (this.getView().getFormShowParameter().getStatus() == OperationStatus.EDIT && ((Boolean)this.getModel().getValue("issyspreset")).booleanValue()) {
            ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
        }
    }

    public void afterLoadData(EventObject e) {
        super.afterLoadData(e);
        HRBaseServiceHelper dimMapHelper = new HRBaseServiceHelper("hrptmc_dimmap");
        Long id = (Long)this.getModel().getValue("id");
        if (dimMapHelper.isExists(new QFilter("preindex", "=", (Object)id))) {
            this.getView().setEnable(Boolean.FALSE, new String[]{"number"});
            this.getView().setEnable(Boolean.FALSE, new String[]{"getway"});
            this.getView().setEnable(Boolean.FALSE, new String[]{"service"});
            this.getView().setEnable(Boolean.FALSE, new String[]{"anobj"});
            this.getView().setEnable(Boolean.FALSE, new String[]{"target"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"addparam"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"deleteparam"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"addcol"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"deletecol"});
            this.getView().setEnable(Boolean.FALSE, new String[]{"entryentity_col"});
        }
    }

    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        if ("addparam".equals(evt.getItemKey())) {
            this.openSelParamF7();
        } else if ("deleteparam".equals(evt.getItemKey())) {
            this.deleteSelParam();
        } else if ("addcol".equals(evt.getItemKey())) {
            this.openSelColF7();
        } else if ("deletecol".equals(evt.getItemKey())) {
            this.deleteSelCol();
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        IDataEntityProperty property = args.getProperty();
        String propName = property.getName();
        if ("getway".equals(propName)) {
            this.showBusInfoByGetWay(true);
        } else if ("service".equals(propName)) {
            this.loadRequiredParam();
        } else if ("anobj".equals(propName)) {
            this.deleteColInfos();
            this.chgTargetByAnObj(true);
        } else if ("target".equals(propName)) {
            this.chgTargetSourceByTarget();
        }
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        List selCols;
        super.closedCallBack(event);
        if ("selParamF7Close".equals(event.getActionId())) {
            ListSelectedRowCollection f7SelParams = (ListSelectedRowCollection)event.getReturnData();
            if (null != f7SelParams) {
                this.chgParamInfo(f7SelParams);
            }
        } else if ("selColF7Close".equals(event.getActionId()) && !CollectionUtils.isEmpty((Collection)(selCols = (List)event.getReturnData()))) {
            this.deleteCol(selCols);
            this.addCol(selCols);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String number = (String)this.getModel().getValue("number");
        if (ReportCommonUtils.validateNumberByISVIsKD()) {
            if (!number.startsWith("kdhr_") || !ReportCommonUtils.validateNumberNoUpper((String)number)) {
                this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u7f16\u7801\u4ee5%s\u5f00\u5934\uff0c\u4ec5\u652f\u6301\u82f1\u6587\u5927\u5c0f\u5199\u3001\u6570\u5b57\u4e0e\u4e0b\u5212\u7ebf\uff0c\u4e0d\u533a\u5206\u5927\u5c0f\u5199\u3002", (String)"ReportPresetIndexEdit_5", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), "kdhr_"));
                args.setCancel(true);
                return;
            }
        } else if (number.startsWith("kdhr_") || !ReportCommonUtils.validateNumberNoUpper((String)number)) {
            this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u7f16\u7801\u7981\u6b62\u4ee5%s\u5f00\u5934\uff0c\u4ec5\u652f\u6301\u82f1\u6587\u5c0f\u5199\u3001\u6570\u5b57\u4e0e\u4e0b\u5212\u7ebf\u3002", (String)"ReportPresetIndexEdit_6", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), "kdhr_"));
            args.setCancel(true);
            return;
        }
        if (NUMBER_START_PATTERN.matcher(number).find()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u4fee\u6539\u7f16\u7801\uff0c\u4e0d\u80fd\u4ee5\u6570\u5b57\u5f00\u5934\u3002", (String)"ReportPresetIndexEdit_13", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
        boolean paramsFill = true;
        String getWay = (String)this.getModel().getValue("getway");
        if (HRStringUtils.equals((String)getWay, (String)"10")) {
            DynamicObjectCollection paramEntry = this.getModel().getEntryEntity("entryentity_param");
            if (paramEntry.isEmpty()) {
                paramsFill = false;
            }
        } else {
            DynamicObjectCollection paramEntry = this.getModel().getEntryEntity("entryentity_col");
            if (paramEntry.isEmpty()) {
                paramsFill = false;
            }
        }
        if (!paramsFill) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u6dfb\u52a0\u53c2\u6570\u4fe1\u606f\u3002", (String)"ReportPresetIndexEdit_14", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
    }

    private void showBusInfoByGetWay(boolean deleteEntryFlag) {
        IFormView view = this.getView();
        IDataModel model = this.getModel();
        String getWay = (String)this.getModel().getValue("getway");
        BasedataEdit serviceEdit = (BasedataEdit)view.getControl("service");
        BasedataEdit anObjEdit = (BasedataEdit)view.getControl("anobj");
        ComboEdit targetEdit = (ComboEdit)view.getControl("target");
        ComboEdit comboEdit = (ComboEdit)view.getControl("calmethod");
        EntryGrid paramGrid = (EntryGrid)view.getControl("entryentity_param");
        EntryGrid colGrid = (EntryGrid)view.getControl("entryentity_col");
        if ("10".equals(getWay)) {
            view.setVisible(Boolean.TRUE, new String[]{"paraminfo", "service"});
            view.setVisible(Boolean.FALSE, new String[]{"selparam", "anobj", "target", "targetfrom", "calmethod", "colinfo"});
            serviceEdit.setMustInput(Boolean.TRUE.booleanValue());
            anObjEdit.setMustInput(Boolean.FALSE.booleanValue());
            targetEdit.setMustInput(Boolean.FALSE.booleanValue());
            comboEdit.setMustInput(Boolean.FALSE.booleanValue());
            paramGrid.setMustInput("paramname", Boolean.TRUE.booleanValue());
            colGrid.setMustInput("colname", Boolean.FALSE.booleanValue());
            model.setValue("anobj", null);
            model.setValue("target", null);
            model.setValue("targetfrom", null);
            model.setValue("calmethod", null);
            ComboEdit targetCombo = (ComboEdit)this.getView().getControl("target");
            targetCombo.setComboItems(null);
            this.deleteColInfos();
            if (deleteEntryFlag) {
                this.deleteParamInfos();
            }
            DynamicObjectCollection dyS = this.getModel().getEntryEntity("entryentity_param");
            for (int i = 0; i < dyS.size(); ++i) {
                Boolean isRequired = ((DynamicObject)dyS.get(i)).getBoolean("selparam.isrequired");
                if (!isRequired.booleanValue()) continue;
                this.getView().setEnable(Boolean.valueOf(false), i, new String[]{"isrequired"});
            }
            this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
        } else if ("20".equals(getWay)) {
            view.setVisible(Boolean.FALSE, new String[]{"service", "paraminfo"});
            view.setVisible(Boolean.TRUE, new String[]{"anobj", "target", "targetfrom", "calmethod", "colinfo"});
            serviceEdit.setMustInput(Boolean.FALSE.booleanValue());
            anObjEdit.setMustInput(Boolean.TRUE.booleanValue());
            targetEdit.setMustInput(Boolean.TRUE.booleanValue());
            comboEdit.setMustInput(Boolean.TRUE.booleanValue());
            paramGrid.setMustInput("paramname", Boolean.FALSE.booleanValue());
            colGrid.setMustInput("colname", Boolean.TRUE.booleanValue());
            this.chgTargetByAnObj(false);
            this.getModel().setValue("service", null);
            this.deleteParamInfos();
            if (deleteEntryFlag) {
                this.deleteColInfos();
            }
        }
    }

    private void openSelColF7() {
        DynamicObject anObj = this.getModel().getDataEntity().getDynamicObject("anobj");
        if (null == anObj) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u8981\u5206\u6790\u7684\u5bf9\u8c61\u3002", (String)"ReportPresetIndexEdit_0", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            return;
        }
        DynamicObjectCollection cols = this.getModel().getEntryEntity("entryentity_col");
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrptmc_selcol");
        showParameter.setCaption(ResManager.loadKDString((String)"\u5b57\u6bb5\u9009\u62e9", (String)"ReportPresetIndexEdit_10", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
        showParameter.setCustomParam("anObjId", anObj.getPkValue());
        ArrayList selCols = Lists.newArrayListWithExpectedSize((int)cols.size());
        for (int i = 0; i < cols.size(); ++i) {
            HashMap<String, String> selCol = new HashMap<String, String>(2);
            DynamicObject col = (DynamicObject)cols.get(i);
            selCol.put("colfrom", col.getString("colfrom"));
            selCol.put("colnumber", col.getString("colnumber"));
            selCols.add(selCol);
        }
        showParameter.setCustomParam("anObjId", anObj.getPkValue());
        showParameter.setCustomParam("selCols", (Object)selCols);
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "selColF7Close"));
        this.getView().showForm(showParameter);
    }

    private void openSelParamF7() {
        DynamicObject service = this.getModel().getDataEntity().getDynamicObject("service");
        if (null == service) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u670d\u52a1\u3002", (String)"ReportPresetIndexEdit_1", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            return;
        }
        List<Long> requiredParams = this.getRequiredParamIds();
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.setFormId("bos_listf7");
        listShowParameter.setBillFormId("hrptmc_selparam");
        listShowParameter.setShowTitle(Boolean.FALSE.booleanValue());
        listShowParameter.setCaption(ResManager.loadKDString((String)"\u53c2\u6570\u9009\u62e9", (String)"ReportPresetIndexEdit_8", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
        listShowParameter.getOpenStyle().setShowType(ShowType.Modal);
        StyleCss inlineStyleCss = new StyleCss();
        inlineStyleCss.setHeight("580");
        inlineStyleCss.setWidth("960");
        listShowParameter.getOpenStyle().setInlineStyleCss(inlineStyleCss);
        listShowParameter.setMultiSelect(true);
        listShowParameter.setLookUp(true);
        listShowParameter.setShowApproved(true);
        listShowParameter.setShowUsed(true);
        DynamicObjectCollection params = this.getModel().getEntryEntity("entryentity_param");
        int selParamCount = params.size() - requiredParams.size();
        if (!CollectionUtils.isEmpty((Collection)params) && selParamCount > 0) {
            ArrayList lists = Lists.newArrayListWithExpectedSize((int)selParamCount);
            for (int i = 0; i < params.size(); ++i) {
                Long selParamId = ((DynamicObject)params.get(i)).getLong("selparam.id");
                if (requiredParams.contains(selParamId)) continue;
                lists.add(selParamId);
            }
            listShowParameter.setSelectedRows(lists.toArray(new Object[0]));
        }
        ListFilterParameter listFilterParameter = listShowParameter.getListFilterParameter();
        Long currServiceId = this.getModel().getDataEntity().getLong("service.id");
        listFilterParameter.setFilter(new QFilter("serviceid", "=", (Object)currServiceId));
        listFilterParameter.setFilter(new QFilter("isrequired", "!=", (Object)Boolean.TRUE));
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "selParamF7Close");
        listShowParameter.setCloseCallBack(closeCallBack);
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    private void deleteSelParam() {
        EntryGrid entry = (EntryGrid)this.getControl("entryentity_param");
        int[] selectRowIndexS = entry.getSelectRows();
        if (0 == selectRowIndexS.length) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u6ca1\u6709\u9009\u4e2d\u884c\u3002", (String)"ReportPresetIndexEdit_2", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            return;
        }
        List<Long> requiredParamIds = this.getRequiredParamIds();
        Boolean allowDelete = true;
        DynamicObjectCollection srvParams = this.getModel().getEntryEntity("entryentity_param");
        for (int i = 0; i < selectRowIndexS.length; ++i) {
            DynamicObject srvParam = (DynamicObject)srvParams.get(selectRowIndexS[i]);
            if (!requiredParamIds.contains(srvParam.getLong("selparam.id"))) continue;
            allowDelete = false;
            break;
        }
        if (allowDelete.booleanValue()) {
            AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
            model.deleteEntryRows("entryentity_param", selectRowIndexS);
            this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f\u3002", (String)"ReportPresetIndexEdit_3", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
        } else {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u670d\u52a1\u5fc5\u4f20\u53c2\u6570\u4e0d\u5141\u8bb8\u5220\u9664\u3002", (String)"ReportPresetIndexEdit_4", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
        }
    }

    private void deleteSelCol() {
        EntryGrid entry = (EntryGrid)this.getControl("entryentity_col");
        int[] selectRowIndexS = entry.getSelectRows();
        if (0 == selectRowIndexS.length) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u6ca1\u6709\u9009\u4e2d\u884c\u3002", (String)"ReportPresetIndexEdit_2", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            return;
        }
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.deleteEntryRows("entryentity_col", selectRowIndexS);
        this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f\u3002", (String)"ReportPresetIndexEdit_3", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
    }

    private void loadRequiredParam() {
        DynamicObject service = (DynamicObject)this.getModel().getValue("service");
        this.deleteParamInfos();
        if (null != service) {
            IFormView view = this.getView();
            IDataModel model = this.getModel();
            DynamicObjectCollection srvParams = service.getDynamicObjectCollection("entryentity");
            ArrayList requiredParams = Lists.newArrayListWithExpectedSize((int)16);
            for (int i = 0; i < srvParams.size(); ++i) {
                DynamicObject srvParam = (DynamicObject)srvParams.get(i);
                boolean isRequired = srvParam.getBoolean("isrequired");
                if (!isRequired) continue;
                requiredParams.add(srvParam);
            }
            if (CollectionUtils.isEmpty((Collection)requiredParams)) {
                return;
            }
            model.batchCreateNewEntryRow("entryentity_param", requiredParams.size());
            int idx = 0;
            for (int i = 0; i < requiredParams.size(); ++i) {
                DynamicObject requiredParam = (DynamicObject)requiredParams.get(i);
                model.setValue("selparam", requiredParam.getPkValue(), idx);
                model.setValue("paramname", requiredParam.get("paramname"), idx);
                model.setValue("isrequired", requiredParam.get("isrequired"), idx);
                ++idx;
            }
            int[] disableIndexS = this.buildIdxArray(model.getEntryRowCount("entryentity_param"));
            for (int i = 0; i < disableIndexS.length; ++i) {
                view.setEnable(Boolean.valueOf(false), disableIndexS[i], new String[]{"isrequired"});
            }
        }
    }

    private void chgParamInfo(ListSelectedRowCollection f7SelParams) {
        List<Long> requiredParamIds = this.getRequiredParamIds();
        this.deleteParam(requiredParamIds, f7SelParams);
        this.addParam(requiredParamIds, f7SelParams);
        DynamicObjectCollection params = this.getModel().getEntryEntity("entryentity_param");
        for (int i = 0; i < params.size(); ++i) {
            if (!requiredParamIds.contains(((DynamicObject)params.get(i)).get("selparam.id"))) continue;
            this.getView().setEnable(Boolean.valueOf(false), i, new String[]{"isrequired"});
        }
    }

    private void addParam(List<Long> requiredParamIds, ListSelectedRowCollection f7SelParams) {
        DynamicObjectCollection params = this.getModel().getEntryEntity("entryentity_param");
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        block0: for (int j = 0; j < f7SelParams.size(); ++j) {
            DynamicObject param;
            ListSelectedRow f7SelParam = f7SelParams.get(j);
            int idx = params.size();
            if (requiredParamIds.size() == params.size()) {
                model.createNewEntryRow("entryentity_param");
                model.setValue("selparam", f7SelParam.getPrimaryKeyValue(), idx);
                model.setValue("paramname", (Object)f7SelParam.getName(), idx);
                model.setValue("isrequired", (Object)Boolean.FALSE, idx);
                view.updateView("entryentity_param");
                params = model.getEntryEntity("entryentity_param");
                continue;
            }
            for (int i = requiredParamIds.size(); i < params.size() && !ObjectUtils.nullSafeEquals((Object)(param = (DynamicObject)params.get(i)).get("selparam.id"), (Object)f7SelParam.getPrimaryKeyValue()); ++i) {
                if (i != params.size() - 1) continue;
                model.createNewEntryRow("entryentity_param");
                model.setValue("selparam", f7SelParam.getPrimaryKeyValue(), idx);
                model.setValue("paramname", (Object)f7SelParam.getName(), idx);
                model.setValue("isrequired", (Object)Boolean.FALSE, idx);
                view.updateView("entryentity_param");
                params = model.getEntryEntity("entryentity_param");
                continue block0;
            }
        }
    }

    private void deleteParam(List<Long> requiredParamIds, ListSelectedRowCollection f7SelParams) {
        DynamicObjectCollection params = this.getModel().getEntryEntity("entryentity_param");
        ArrayList deleteIdxS = Lists.newArrayListWithExpectedSize((int)16);
        block0: for (int i = requiredParamIds.size(); i < params.size(); ++i) {
            Object f7SelParamId;
            Long paramId = ((DynamicObject)params.get(i)).getLong("selparam.id");
            for (int j = 0; j < f7SelParams.size() && !ObjectUtils.nullSafeEquals((Object)paramId, (Object)(f7SelParamId = f7SelParams.get(j).getPrimaryKeyValue())); ++j) {
                if (j != f7SelParams.size() - 1) continue;
                deleteIdxS.add(i);
                continue block0;
            }
        }
        int[] deleteIdxArr = new int[deleteIdxS.size()];
        for (int i = 0; i < deleteIdxS.size(); ++i) {
            deleteIdxArr[i] = (Integer)deleteIdxS.get(i);
        }
        this.getModel().deleteEntryRows("entryentity_param", deleteIdxArr);
        this.getView().updateView("entryentity_param");
    }

    private void addCol(List<Map<String, Object>> selCols) {
        DynamicObjectCollection cols = this.getModel().getEntryEntity("entryentity_col");
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        if (cols.size() == 0) {
            int idx = 0;
            for (int i = 0; i < selCols.size(); ++i) {
                Map<String, Object> selCol = selCols.get(i);
                model.createNewEntryRow("entryentity_col");
                model.setValue("colfrom", selCol.get("colfrom"), idx);
                model.setValue("colnumber", selCol.get("colnumber"), idx);
                model.setValue("colname", selCol.get("colname"), idx);
                model.setValue("coltype", selCol.get("coltype"), idx);
                view.updateView("entryentity_col");
                ++idx;
            }
            return;
        }
        block1: for (int i = 0; i < selCols.size(); ++i) {
            Map<String, Object> selCol = selCols.get(i);
            String selColFrom = (String)selCol.get("colfrom");
            String selColNumber = (String)selCol.get("colnumber");
            int idx = cols.size();
            for (int j = 0; j < cols.size(); ++j) {
                DynamicObject col = (DynamicObject)cols.get(j);
                String colFrom = col.getString("colfrom");
                String colNumber = col.getString("colnumber");
                if (StringUtils.equals((CharSequence)selColNumber, (CharSequence)colNumber) && StringUtils.equals((CharSequence)selColFrom, (CharSequence)colFrom)) continue block1;
                if (j != cols.size() - 1) continue;
                model.createNewEntryRow("entryentity_col");
                model.setValue("colfrom", selCol.get("colfrom"), idx);
                model.setValue("colnumber", selCol.get("colnumber"), idx);
                model.setValue("colname", selCol.get("colname"), idx);
                model.setValue("coltype", selCol.get("coltype"), idx);
                view.updateView("entryentity_col");
                cols = model.getEntryEntity("entryentity_col");
                continue block1;
            }
        }
    }

    private void deleteCol(List<Map<String, Object>> selCols) {
        DynamicObjectCollection cols = this.getModel().getEntryEntity("entryentity_col");
        ArrayList deleteIdxS = Lists.newArrayListWithExpectedSize((int)16);
        block0: for (int i = 0; i < cols.size(); ++i) {
            DynamicObject col = (DynamicObject)cols.get(i);
            String colFrom = col.getString("colfrom");
            String colNumber = col.getString("colnumber");
            for (int j = 0; j < selCols.size(); ++j) {
                Map<String, Object> selCol = selCols.get(j);
                String selColfrom = (String)selCol.get("colfrom");
                String selColNumber = (String)selCol.get("colnumber");
                if (StringUtils.equals((CharSequence)colNumber, (CharSequence)selColNumber) && StringUtils.equals((CharSequence)colFrom, (CharSequence)selColfrom)) continue block0;
                if (j != selCols.size() - 1) continue;
                deleteIdxS.add(i);
            }
        }
        int[] deleteIdxArr = new int[deleteIdxS.size()];
        for (int i = 0; i < deleteIdxS.size(); ++i) {
            deleteIdxArr[i] = (Integer)deleteIdxS.get(i);
        }
        this.getModel().deleteEntryRows("entryentity_col", deleteIdxArr);
        this.getView().updateView("entryentity_col");
    }

    private void deleteParamInfos() {
        DynamicObjectCollection params = this.getModel().getEntryEntity("entryentity_param");
        if (!CollectionUtils.isEmpty((Collection)params)) {
            IDataModel model = this.getModel();
            int rowCount = model.getEntryRowCount("entryentity_param");
            model.deleteEntryRows("entryentity_param", this.buildIdxArray(rowCount));
            model.setDataChanged(false);
            this.getView().updateView("entryentity_param");
        }
    }

    private void deleteColInfos() {
        DynamicObjectCollection params = this.getModel().getEntryEntity("entryentity_col");
        if (!CollectionUtils.isEmpty((Collection)params)) {
            IDataModel model = this.getModel();
            int rowCount = model.getEntryRowCount("entryentity_col");
            model.deleteEntryRows("entryentity_col", this.buildIdxArray(rowCount));
            model.setDataChanged(false);
            this.getView().updateView("entryentity_col");
        }
    }

    private void chgTargetByAnObj(boolean resetTarget) {
        DynamicObject anObj = this.getModel().getDataEntity().getDynamicObject("anobj");
        ComboEdit targetCombo = (ComboEdit)this.getView().getControl("target");
        targetCombo.setComboItems(null);
        if (resetTarget) {
            this.getModel().setValue("target", null);
            this.getModel().setValue("targetfrom", null);
        }
        if (null == anObj) {
            return;
        }
        QFilter anObjIdFilter = new QFilter("anobj", "=", anObj.getPkValue());
        QFilter valTypeFilter = new QFilter("valuetype", "in", (Object)TARGET_VALUE_TYPE);
        QFilter[] qFilters = new QFilter[]{anObjIdFilter, valTypeFilter};
        HRBaseServiceHelper calHelper = new HRBaseServiceHelper("hrptmc_calculatefield");
        DynamicObject[] calCols = calHelper.query("number,name", qFilters);
        HRBaseServiceHelper qryHelper = new HRBaseServiceHelper("hrptmc_anobjqueryfield");
        DynamicObject[] qryCols = qryHelper.query("fieldpath,fieldname,fieldalias", qFilters);
        ArrayList targetComboItems = new ArrayList(calCols.length + qryCols.length);
        Arrays.stream(calCols).forEach(calCol -> {
            if (!calCol.getString("number").contains(".id")) {
                ComboItem item = new ComboItem();
                item.setValue(calCol.getString("number"));
                item.setCaption(new LocaleString(calCol.getLocaleString("name").getLocaleValue()));
                targetComboItems.add(item);
            }
        });
        Arrays.stream(qryCols).forEach(qryCol -> {
            if (!qryCol.getString("fieldpath").contains(".id")) {
                ComboItem item = new ComboItem();
                item.setValue(qryCol.getString("fieldpath"));
                item.setCaption(new LocaleString(qryCol.getLocaleString("fieldname").getLocaleValue()));
                targetComboItems.add(item);
            }
        });
        targetCombo.setComboItems(targetComboItems);
    }

    private void chgTargetSourceByTarget() {
        String target = this.getModel().getDataEntity().getString("target");
        if (StringUtils.isEmpty((CharSequence)target)) {
            this.getModel().setValue("targetfrom", null);
            return;
        }
        DynamicObject anObj = this.getModel().getDataEntity().getDynamicObject("anobj");
        if (null == anObj) {
            return;
        }
        if (null == anObj) {
            return;
        }
        HRBaseServiceHelper qryHelper = new HRBaseServiceHelper("hrptmc_anobjqueryfield");
        QFilter colNumberFilter = new QFilter("fieldpath", "=", (Object)target);
        QFilter anObjIdFilter = new QFilter("anobj", "=", anObj.getPkValue());
        QFilter[] qFilters = new QFilter[]{anObjIdFilter, colNumberFilter};
        if (qryHelper.isExists(qFilters)) {
            this.getModel().setValue("targetfrom", (Object)"20");
        } else {
            this.getModel().setValue("targetfrom", (Object)"10");
        }
    }

    private int[] buildIdxArray(int rowCount) {
        int[] idxArray = new int[rowCount];
        for (int i = 0; i < idxArray.length; ++i) {
            idxArray[i] = i;
        }
        return idxArray;
    }

    private List<Long> getRequiredParamIds() {
        ArrayList requiredParams = Lists.newArrayListWithExpectedSize((int)16);
        DynamicObject service = this.getModel().getDataEntity().getDynamicObject("service");
        DynamicObjectCollection srvParams = service.getDynamicObjectCollection("entryentity");
        for (int i = 0; i < srvParams.size(); ++i) {
            DynamicObject srvParam = (DynamicObject)srvParams.get(i);
            boolean isRequired = srvParam.getBoolean("isrequired");
            if (!isRequired) continue;
            requiredParams.add(srvParam.getLong("id"));
        }
        return requiredParams;
    }
}
