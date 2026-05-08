/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.OrmUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.OpenStyle
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.TextEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.metadata.dao.MetadataReader
 *  kd.bos.metadata.domainmodel.define.DomainModelTypeFactory
 *  kd.bos.metadata.form.ControlAp
 *  kd.bos.metadata.form.DesignFormMeta
 *  kd.bos.metadata.form.FormMetadata
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.bos.servicehelper.devportal.BizAppServiceHelp
 *  kd.bos.servicehelper.devportal.BizCloudServiceHelp
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRCloudServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.servicehelper.prompt.PromptServiceHelper
 *  org.apache.commons.collections.CollectionUtils
 */
package kd.hr.hrcs.formplugin.web.prompt;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.OrmUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.OpenStyle;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.TextEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.metadata.dao.MetadataReader;
import kd.bos.metadata.domainmodel.define.DomainModelTypeFactory;
import kd.bos.metadata.form.ControlAp;
import kd.bos.metadata.form.DesignFormMeta;
import kd.bos.metadata.form.FormMetadata;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.bos.servicehelper.devportal.BizAppServiceHelp;
import kd.bos.servicehelper.devportal.BizCloudServiceHelp;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRCloudServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.servicehelper.prompt.PromptServiceHelper;
import org.apache.commons.collections.CollectionUtils;

@ExcludeFromJacocoGeneratedReport
public final class PromptRuleEditPlugin
extends AbstractFormPlugin
implements BeforeF7SelectListener {
    private String[] filterModelType = new String[]{"DynamicFormModel", "BillFormModel", "BaseFormModel", "QueryListModel", "MobileFormModel"};

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        TextEdit control = (TextEdit)this.getView().getControl("controledit");
        control.addClickListener((ClickListener)this);
        Toolbar ruleEntryToolBar = (Toolbar)this.getControl("ruleentrytoolbar");
        ruleEntryToolBar.addItemClickListener((ItemClickListener)this);
        this.addClickListeners(new String[]{"viewpromptcontent"});
        BasedataEdit businessEdit = (BasedataEdit)this.getView().getControl("businessobject");
        businessEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void click(EventObject evt) {
        TextEdit source;
        super.click(evt);
        if (evt.getSource() instanceof TextEdit && HRStringUtils.equals((String)"controledit", (String)(source = (TextEdit)evt.getSource()).getKey())) {
            DynamicObject formDy = (DynamicObject)this.getModel().getValue("businessobject");
            if (Objects.isNull(formDy)) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u57fa\u672c\u4fe1\u606f\u4e2d\u7684\u201c\u9875\u9762\u201d\u3002", (String)"PromptRuleEditPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return;
            }
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity");
            if (CollectionUtils.isNotEmpty((Collection)entryEntity)) {
                this.getView().showConfirm(ResManager.loadKDString((String)"\u66f4\u6539\u63a7\u4ef6\u4fe1\u606f\uff0c\u5c06\u6e05\u7a7a\u5206\u5f55\u4fe1\u606f\uff0c\u786e\u5b9a\u7ee7\u7eed\u5417\uff1f", (String)"PromptRuleEditPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.YesNo, new ConfirmCallBackListener("ClearEntryEntity", (IFormPlugin)this));
                return;
            }
            this.showChoseControl();
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String propertyKey = evt.getProperty().getName();
        if (HRStringUtils.equals((String)"businessobject", (String)propertyKey)) {
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity");
            String controlNumber = (String)this.getModel().getValue("controlnumber");
            if (CollectionUtils.isNotEmpty((Collection)entryEntity) || HRStringUtils.isNotEmpty((String)controlNumber)) {
                this.getView().showConfirm(ResManager.loadKDString((String)"\u66f4\u6539\u9875\u9762\u4fe1\u606f\uff0c\u5c06\u6e05\u7a7a\u63a7\u4ef6\u53ca\u5206\u5f55\u4fe1\u606f\uff0c\u786e\u5b9a\u7ee7\u7eed\u5417\uff1f", (String)"PromptRuleEditPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.YesNo, new ConfirmCallBackListener("ClearControlAndEntryEntity", (IFormPlugin)this));
                evt.setCancel(true);
            }
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        String callBackId;
        super.confirmCallBack(evt);
        switch (callBackId = evt.getCallBackId()) {
            case "ClearEntryEntity": {
                if (evt.getResult().getValue() != MessageBoxResult.Yes.getValue()) break;
                this.getModel().deleteEntryData("entryentity");
                this.getView().updateView();
                this.showChoseControl();
                break;
            }
            case "ClearControlAndEntryEntity": {
                if (evt.getResult().getValue() != MessageBoxResult.Yes.getValue()) break;
                this.clearControlAndShowFormF7();
                break;
            }
        }
    }

    private void clearControlAndShowFormF7() {
        this.getModel().setValue("controlname", null);
        this.getModel().setValue("controlnumber", null);
        this.getModel().setValue("controltype", null);
        this.getModel().deleteEntryData("entryentity");
        this.getView().updateView("entryentity");
        ListShowParameter listSP = new ListShowParameter();
        listSP.setFormId("bos_devp_formtreelistf7");
        listSP.setBillFormId("bos_formmeta");
        listSP.setLookUp(true);
        listSP.setShowTitle(false);
        listSP.setMultiSelect(false);
        OpenStyle style = new OpenStyle();
        StyleCss cssValue = new StyleCss();
        cssValue.setWidth("960px");
        cssValue.setHeight("580px");
        style.setInlineStyleCss(cssValue);
        style.setShowType(ShowType.Modal);
        listSP.setOpenStyle(style);
        listSP.setCloseCallBack(new CloseCallBack());
        listSP.getCloseCallBack().setControlKey("businessobject");
        listSP.getListFilterParameter().setFilter(new QFilter("modeltype", "in", (Object)this.filterModelType));
        List cloudIdSortList = HRCloudServiceHelper.getAllHRCommonCloudIdsSort();
        listSP.setCustomParam("bizcloudids", (Object)Lists.newArrayList((Iterable)cloudIdSortList));
        this.getView().showForm((FormShowParameter)listSP);
    }

    private void showChoseControl() {
        DynamicObject formDy = (DynamicObject)this.getModel().getValue("businessobject");
        ListShowParameter showParameter = ShowFormHelper.createShowListForm((String)"hrcs_controlchoose", (boolean)false, (int)0, (boolean)true);
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setShowTitle(false);
        showParameter.setCustomParam("entityId", (Object)PromptServiceHelper.getExtEntityId((String)formDy.getString("dentityid")));
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "controledit"));
        this.getView().showForm((FormShowParameter)showParameter);
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        Object returnData = event.getReturnData();
        switch (event.getActionId()) {
            case "controledit": {
                Optional.ofNullable(returnData).ifPresent(data -> {
                    ListSelectedRowCollection listRowData = (ListSelectedRowCollection)returnData;
                    DynamicObject entityDy = (DynamicObject)this.getModel().getValue("businessobject");
                    ListSelectedRow row = listRowData.get(0);
                    MetadataReader reader = new MetadataReader(false);
                    FormMetadata formMeta = (FormMetadata)reader.readMeta(PromptServiceHelper.getExtEntityId((String)entityDy.getString("dentityid")), OrmUtils.getDataEntityType(DesignFormMeta.class));
                    ControlAp controlAp = formMeta.getItem((String)row.getPrimaryKeyValue());
                    this.getModel().setValue("controlname", (Object)controlAp.getName());
                    this.getModel().setValue("controlnumber", (Object)controlAp.getKey());
                    this.getModel().setValue("controltype", (Object)DomainModelTypeFactory.getDomainModelType((String)formMeta.getModelType(), (boolean)true).getElementType(controlAp.getClass().getSimpleName()).getName());
                    this.getModel().setValue("controledit", (Object)controlAp.getName().getLocaleValue());
                });
                break;
            }
            case "addruleentry": {
                Optional.ofNullable(returnData).ifPresent(data -> {
                    HashMap resultMap = (HashMap)data;
                    int entryRow = this.getModel().createNewEntryRow("entryentity");
                    String ruleStr = (String)resultMap.get("rule");
                    if (HRStringUtils.isNotEmpty((String)ruleStr)) {
                        this.getModel().setValue("entryrule", (Object)ruleStr, entryRow);
                    }
                    this.getModel().setValue("entryprompt", resultMap.get("prompt"), entryRow);
                    this.getModel().setValue("entrydescription", resultMap.get("description"), entryRow);
                });
                break;
            }
            case "entrysetting": {
                Optional.ofNullable(returnData).ifPresent(data -> {
                    HashMap resultMap = (HashMap)data;
                    int rowIndex = (Integer)resultMap.get("rowIndex");
                    String ruleStr = (String)resultMap.get("rule");
                    if (HRStringUtils.isNotEmpty((String)ruleStr)) {
                        this.getModel().setValue("entryrule", (Object)ruleStr, rowIndex);
                    }
                    this.getModel().setValue("entryprompt", resultMap.get("prompt"), rowIndex);
                    this.getModel().setValue("entrydescription", resultMap.get("description"), rowIndex);
                    this.getView().updateView("entryentity");
                });
                break;
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        if ("businessobject".equals(args.getProperty().getName())) {
            this.getModel().deleteEntryData("entryentity");
            this.getModel().setValue("controlname", null);
            this.getModel().setValue("controlnumber", null);
            this.getModel().setValue("controltype", null);
            ChangeData changeSet = args.getChangeSet()[0];
            DynamicObject newForm = (DynamicObject)changeSet.getNewValue();
            if (newForm != null) {
                MainEntityType entityType = MetadataServiceHelper.getDataEntityType((String)newForm.getString("number"));
                this.getModel().setValue("entity", HRStringUtils.isEmpty((String)entityType.getAlias()) ? null : newForm.getString("dentityid"));
            }
            this.setBoNumber();
            this.getView().updateView();
        }
    }

    private void setBoNumber() {
        DynamicObject bo = (DynamicObject)this.getModel().getValue("businessobject");
        Optional.ofNullable(bo).ifPresent(dy -> {
            String boNumber = BizCloudServiceHelp.getBizCloudByFormID((String)dy.getString("dentityid")).getString("id") + "#" + BizAppServiceHelp.getAppIdByFormNum((String)bo.getString("number")) + "#" + bo.getString("id");
            this.getModel().setValue("bonumber", (Object)boNumber);
        });
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        switch (formOperate.getOperateKey()) {
            case "save": {
                String controlEdit = (String)this.getModel().getValue("controledit");
                if (HRStringUtils.isEmpty((String)controlEdit)) {
                    this.getModel().setValue("controlname", null);
                    this.getModel().setValue("controlnumber", null);
                    this.getModel().setValue("controltype", null);
                }
                this.validateControlDul(args);
                break;
            }
            case "openform": {
                this.clickOpenForm();
                break;
            }
            case "entrysetting": {
                DynamicObject formDy = (DynamicObject)this.getModel().getValue("businessobject");
                if (Objects.isNull(formDy)) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u57fa\u672c\u4fe1\u606f\u4e2d\u7684\u201c\u9875\u9762\u201d\u3002", (String)"PromptRuleEditPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    return;
                }
                FormShowParameter addRuleFSP = this.openAddRuleEntryForm(formDy);
                int rowIndex = this.getModel().getEntryCurrentRowIndex("entryentity");
                DynamicObject entryDy = this.getModel().getEntryRowEntity("entryentity", rowIndex);
                Object promptId = entryDy.getDynamicObject("entryprompt").getPkValue();
                String entryRule = entryDy.getString("entryrule");
                addRuleFSP.setCustomParam("promptId", promptId);
                addRuleFSP.setCustomParam("entryDescription", (Object)SerializationUtils.toJsonString((Object)entryDy.get("entrydescription")));
                addRuleFSP.setCustomParam("entryRule", (Object)entryRule);
                addRuleFSP.setCustomParam("rowIndex", (Object)rowIndex);
                addRuleFSP.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "entrysetting"));
                this.getView().showForm(addRuleFSP);
                break;
            }
        }
    }

    private void validateControlDul(BeforeDoOperationEventArgs args) {
        HRBaseServiceHelper ruleHelper = new HRBaseServiceHelper("hrcs_promptrule");
        DynamicObject formDy = (DynamicObject)this.getModel().getValue("businessobject");
        String controlNum = (String)this.getModel().getValue("controlnumber");
        if (ruleHelper.isExists(new QFilter[]{new QFilter("businessobject", "=", formDy.getPkValue()), new QFilter("controlnumber", "=", (Object)controlNum), new QFilter("id", "!=", this.getModel().getDataEntity().getPkValue())})) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5df2\u5b58\u5728\u76f8\u540c\u7684\u63a7\u4ef6\u6620\u5c04\u8bb0\u5f55\uff0c\u8bf7\u4fee\u6539\u539f\u6709\u8bb0\u5f55\u3002", (String)"PromptRuleEditPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
    }

    private FormShowParameter openAddRuleEntryForm(DynamicObject formDy) {
        DynamicObject entityDy = (DynamicObject)this.getModel().getValue("entity");
        FormShowParameter addRuleFSP = new FormShowParameter();
        addRuleFSP.setCustomParam("formId", formDy.get("dentityid"));
        addRuleFSP.getOpenStyle().setShowType(ShowType.Modal);
        if (Objects.nonNull(entityDy)) {
            addRuleFSP.setCustomParam("entity", (Object)entityDy.getString("number"));
            addRuleFSP.setFormId("hrcs_addrule");
            addRuleFSP.setCaption(ResManager.loadKDString((String)"\u63d0\u793a\u8bed\u663e\u793a\u89c4\u5219", (String)"PromptRuleEditPlugin_5", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        } else {
            addRuleFSP.setFormId("hrcs_addnoentityrule");
            addRuleFSP.setCaption(ResManager.loadKDString((String)"\u63d0\u793a\u8bed", (String)"PromptRuleEditPlugin_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        }
        return addRuleFSP;
    }

    private void clickOpenForm() {
        FormShowParameter showPromptContentFSP = new FormShowParameter();
        showPromptContentFSP.setFormId("hrcs_promptview");
        showPromptContentFSP.getOpenStyle().setShowType(ShowType.Modal);
        int rowIndex = this.getModel().getEntryCurrentRowIndex("entryentity");
        DynamicObject promptDy = (DynamicObject)this.getModel().getValue("entryprompt", rowIndex);
        showPromptContentFSP.setCustomParam("prompt", promptDy.getPkValue());
        showPromptContentFSP.setCustomParam("businessobject", promptDy.get("businessobject.number"));
        this.getView().showForm(showPromptContentFSP);
    }

    public void itemClick(ItemClickEvent evt) {
        String itemKey;
        super.itemClick(evt);
        switch (itemKey = evt.getItemKey()) {
            case "addruleentry": {
                DynamicObject formDy = (DynamicObject)this.getModel().getValue("businessobject");
                if (Objects.isNull(formDy)) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u57fa\u672c\u4fe1\u606f\u4e2d\u7684\u201c\u9875\u9762\u201d\u3002", (String)"PromptRuleEditPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    return;
                }
                if (StringUtils.isEmpty((CharSequence)((CharSequence)this.getModel().getValue("controledit")))) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u63a7\u4ef6", (String)"PromptRuleEditPlugin_7", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    return;
                }
                FormShowParameter addRuleFSP = this.openAddRuleEntryForm(formDy);
                addRuleFSP.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "addruleentry"));
                this.getView().showForm(addRuleFSP);
                break;
            }
            case "deleteruleentry": {
                EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity");
                Optional.ofNullable(entryGrid.getSelectRows()).ifPresent(rowIndex -> this.getModel().deleteEntryRows("entryentity", rowIndex));
                this.getView().sendFormAction(this.getView());
                break;
            }
        }
    }
}
