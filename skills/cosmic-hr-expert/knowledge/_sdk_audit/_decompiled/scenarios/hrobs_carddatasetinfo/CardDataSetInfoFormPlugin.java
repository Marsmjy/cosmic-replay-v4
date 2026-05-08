/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.AbstractBasePlugIn
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DataEntityState
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.TextProp
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FieldTip
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.control.Button
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.events.BeforeClickEvent
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.TextEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.QueryServiceHelper
 *  kd.hrmp.hrobs.business.workbench.engine.repository.WbCardRepository
 */
package kd.hrmp.hrobs.formplugin.workbench.datasetconfig;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import kd.bos.base.AbstractBasePlugIn;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DataEntityState;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.TextProp;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FieldTip;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.control.Button;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.BeforeClickEvent;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.TextEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.hrmp.hrobs.business.workbench.engine.repository.WbCardRepository;

public final class CardDataSetInfoFormPlugin
extends AbstractBasePlugIn
implements BeforeF7SelectListener {
    private static final Log LOG = LogFactory.getLog(CardDataSetInfoFormPlugin.class);

    public void registerListener(EventObject e) {
        super.registerListener(e);
        TextEdit basedatainfoControl = (TextEdit)this.getView().getControl("basedatainfotext");
        basedatainfoControl.addButtonClickListener((ClickListener)this);
        BasedataEdit datasettypeControl = (BasedataEdit)this.getView().getControl("dstype");
        datasettypeControl.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        this.addClickListeners(new String[]{"btnsave"});
    }

    public void afterCopyData(EventObject e) {
        super.afterCopyData(e);
        BillShowParameter billShowParameter = (BillShowParameter)this.getView().getFormShowParameter();
        billShowParameter.setCustomParam("fromCopy", (Object)true);
        this.setBasedatainfotest();
    }

    private void setBasedatainfotest() {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        String binddatainfo = dataEntity.getString("binddatainfo");
        Map<String, String> formIdMap = this.getFormIdMap();
        String binddataFormId = formIdMap.get("binddataFormId");
        DynamicObject dynamicObject = QueryServiceHelper.queryOne((String)binddataFormId, (String)"id,name", (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)Long.valueOf(binddatainfo))});
        if (dynamicObject != null) {
            this.getModel().setValue("basedatainfotext", (Object)dynamicObject.getString("name"));
            dataEntity.getDataEntityState().setBizChanged(dataEntity.getDynamicObjectType().getProperty("basedatainfotext").getOrdinal(), false);
        }
    }

    public void beforeBindData(EventObject e) {
        BillShowParameter billShowParameter = (BillShowParameter)this.getView().getFormShowParameter();
        this.getPageCache().put("pageInitStatus", billShowParameter.getStatus().toString());
        if (OperationStatus.ADDNEW.equals((Object)billShowParameter.getStatus())) {
            boolean fromCopy;
            boolean bl = fromCopy = billShowParameter.getCustomParam("fromCopy") != null;
            if (!fromCopy) {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"basedatainfotext"});
            }
        } else if (OperationStatus.EDIT.equals((Object)billShowParameter.getStatus())) {
            this.getView().setVisible(Boolean.valueOf(true), new String[]{"basedatainfotext"});
            this.getView().setEnable(Boolean.valueOf(false), new String[]{"dstype", "basedatainfotext"});
        }
        DynamicObject dstypeDy = this.getModel().getDataEntity().getDynamicObject("dstype");
        if (dstypeDy != null) {
            TextEdit basedatainfoControl = (TextEdit)this.getView().getControl("basedatainfotext");
            basedatainfoControl.setCaption(new LocaleString(dstypeDy.getString("commonbinddataname")));
            basedatainfoControl.setDisplayName(new LocaleString(dstypeDy.getString("commonbinddataname")));
            basedatainfoControl.setMustInput(true);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        DynamicObject wbGroupDy;
        String name = beforeF7SelectEvent.getProperty().getName();
        if ("dstype".equals(name) && (wbGroupDy = this.getModel().getDataEntity().getDynamicObject("wbgroup")) != null) {
            QFilter wbGroupFilter = new QFilter("commontype", "=", (Object)"1");
            beforeF7SelectEvent.addCustomQFilter(wbGroupFilter);
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        String propertyName = args.getProperty().getName();
        if ("dstype".equals(propertyName)) {
            DynamicObject dstypeDy = this.getModel().getDataEntity().getDynamicObject("dstype");
            if (dstypeDy != null) {
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"basedatainfotext"});
                TextEdit basedatainfoControl = (TextEdit)this.getView().getControl("basedatainfotext");
                basedatainfoControl.setCaption(LocaleString.fromMap((Map)dstypeDy.getLocaleString("commonbinddataname")));
                basedatainfoControl.setDisplayName(LocaleString.fromMap((Map)dstypeDy.getLocaleString("commonbinddataname")));
                basedatainfoControl.setMustInput(true);
                this.getModel().setValue("basedatainfotext", null);
                this.getModel().setValue("binddatainfo", null);
            } else {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"basedatainfotext"});
            }
        }
    }

    public void afterLoadData(EventObject e) {
        super.afterLoadData(e);
        this.setBasedatainfotest();
    }

    public void beforeClick(BeforeClickEvent evt) {
        super.beforeClick(evt);
        String key = ((Control)evt.getSource()).getKey().toLowerCase();
        if ("basedatainfotext".equals(key)) {
            DynamicObject dstypeDy = this.getModel().getDataEntity().getDynamicObject("dstype");
            this.checkDstypeIsEmpty(evt, dstypeDy);
        }
    }

    private void checkDstypeIsEmpty(BeforeClickEvent evt, DynamicObject dstypeDy) {
        if (dstypeDy == null) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u6570\u636e\u96c6\u7c7b\u578b\u3002", (String)"CardDataSetInfoFormPlugin_2", (String)"hrmp-hrobs-formplugin", (Object[])new Object[0]));
            evt.setCancel(true);
        }
    }

    public void click(EventObject evt) {
        DynamicObject dstypeDy;
        String basedatainfotext;
        String name;
        super.click(evt);
        Object source = evt.getSource();
        if (source instanceof TextEdit) {
            Map<String, String> formIdMap;
            String binddataFormId;
            TextEdit textEdit = (TextEdit)source;
            String name2 = textEdit.getKey();
            if ("basedatainfotext".equals(name2) && StringUtils.isNotEmpty((CharSequence)(binddataFormId = (formIdMap = this.getFormIdMap()).get("binddataFormId")))) {
                ListShowParameter showParameter = new ListShowParameter();
                showParameter.setLookUp(true);
                if ("hrptmc_reportmanage".equals(binddataFormId)) {
                    showParameter.setFormId("bos_templatetreelistf7");
                    QFilter publishworkstatusFilter = new QFilter("publishworkstatus", "=", (Object)"A");
                    showParameter.getListFilterParameter().getQFilters().add(publishworkstatusFilter);
                } else {
                    showParameter.setFormId("bos_listf7");
                }
                showParameter.setBillFormId(binddataFormId);
                showParameter.setMultiSelect(false);
                showParameter.setShowTitle(false);
                showParameter.setHasRight(true);
                StyleCss css = new StyleCss();
                css.setWidth("960px");
                css.setHeight("580px");
                showParameter.getOpenStyle().setInlineStyleCss(css);
                showParameter.getOpenStyle().setShowType(ShowType.Modal);
                CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, name2);
                showParameter.setCloseCallBack(closeCallBack);
                this.getView().showForm((FormShowParameter)showParameter);
            }
        } else if (source instanceof Button && "btnsave".equals(name = ((Button)source).getKey()) && StringUtils.isEmpty((CharSequence)(basedatainfotext = this.getView().getModel().getDataEntity(true).getString("basedatainfotext"))) && (dstypeDy = this.getModel().getDataEntity().getDynamicObject("dstype")) != null) {
            FieldTip fieldTip = new FieldTip();
            fieldTip.setSuccess(false);
            fieldTip.setFieldKey("basedatainfotext");
            fieldTip.setTip(ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)"CardDataSetInfoFormPlugin_5", (String)"hrmp-hrobs-formplugin", (Object[])new Object[0]));
            this.getView().showFieldTip(fieldTip);
        }
    }

    private Map<String, String> getFormIdMap() {
        DynamicObject dsTypeDy = this.getModel().getDataEntity().getDynamicObject("dstype");
        HashMap<String, String> formIdMap = new HashMap<String, String>();
        formIdMap.put("binddataFormId", dsTypeDy.getString("commonbinddata.id"));
        formIdMap.put("binddetailFormId", dsTypeDy.getString("commonbinddetailpage.id"));
        return formIdMap;
    }

    public void closedCallBack(ClosedCallBackEvent e) {
        ListSelectedRowCollection rows;
        super.closedCallBack(e);
        String actionId = e.getActionId();
        Object returnData = e.getReturnData();
        if (returnData instanceof ListSelectedRowCollection && "basedatainfotext".equals(actionId) && (rows = (ListSelectedRowCollection)e.getReturnData()) != null && (!rows.isEmpty() || rows.isClearFlag())) {
            ListSelectedRow row = rows.get(0);
            Object baseId = row.getPrimaryKeyValue();
            String name = row.getName();
            this.getModel().setValue("basedatainfotext", (Object)name);
            this.getModel().setValue("binddatainfo", baseId);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        Long pkId;
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)"save", (CharSequence)formOperate.getOperateKey()) && this.checkDSBeUsed(pkId = (Long)this.getView().getModel().getDataEntity(true).getPkValue())) {
            args.setCancel(true);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        if ("save".equals(args.getOperateKey()) && args.getOperationResult().isSuccess()) {
            if (OperationStatus.ADDNEW.toString().equals(this.getPageCache().get("pageInitStatus"))) {
                this.openDataSetConfigPage(args.getOperationResult().getSuccessPkIds().get(0));
            }
            this.getView().close();
        }
    }

    private void openDataSetConfigPage(Object pkId) {
        Map<String, String> formIdMap = this.getFormIdMap();
        String binddetailFormId = formIdMap.get("binddetailFormId");
        if (StringUtils.isNotEmpty((CharSequence)binddetailFormId)) {
            BillShowParameter showParameter = new BillShowParameter();
            showParameter.setFormId(binddetailFormId);
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            showParameter.setStatus(OperationStatus.ADDNEW);
            showParameter.setPkId(pkId);
            showParameter.setCaption(ResManager.loadKDString((String)"\u65b0\u589e\u6570\u636e\u96c6", (String)"CardDataSetInfoFormPlugin_3", (String)"hrmp-hrobs-formplugin", (Object[])new Object[0]));
            this.getView().getParentView().showForm((FormShowParameter)showParameter);
            this.getView().sendFormAction(this.getView().getParentView());
        }
    }

    private boolean checkDSBeUsed(Long pkId) {
        QFilter datasetIdFilter = new QFilter("wbdscardinfo.dsconfig", "=", (Object)pkId);
        DynamicObject[] wbCardDyns = new WbCardRepository().query("id,name,number", new QFilter[]{datasetIdFilter});
        if (wbCardDyns != null && wbCardDyns.length > 0) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8be5\u6570\u636e\u96c6\u5df2\u88ab\u9875\u9762\u5f15\u7528\uff0c\u4e0d\u80fd\u4fee\u6539\u3002", (String)"CardDataSetInfoFormPlugin_4", (String)"hrmp-hrobs-formplugin", (Object[])new Object[0]));
            return true;
        }
        return false;
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        DataEntityState dataEntityState = this.getModel().getDataEntity().getDataEntityState();
        Iterable bizChangedProperties = dataEntityState.getBizChangedProperties();
        for (IDataEntityProperty iDataEntityProperty : bizChangedProperties) {
            DynamicObject dstypeDy;
            String name = iDataEntityProperty.getName();
            if ("binddatainfo".contains(name)) {
                dataEntityState.setBizChanged(iDataEntityProperty.getOrdinal(), false);
            }
            if (!"basedatainfotext".contains(name) || (dstypeDy = this.getModel().getDataEntity().getDynamicObject("dstype")) == null) continue;
            ((TextProp)iDataEntityProperty).setDisplayName(LocaleString.fromMap((Map)dstypeDy.getLocaleString("commonbinddataname")));
        }
    }
}
