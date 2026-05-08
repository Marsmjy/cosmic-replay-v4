/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.Save
 *  kd.bos.form.FormConfig
 *  kd.bos.form.FormMetadataCache
 *  kd.bos.form.IFormView
 *  kd.bos.form.control.Control
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.ListShowParameter
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hies.common.HiesCommonRes
 *  kd.hr.hies.common.util.MetaMenuUtil
 */
package kd.hr.hies.formplugin;

import java.util.Collection;
import java.util.EventObject;
import java.util.Locale;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.Save;
import kd.bos.form.FormConfig;
import kd.bos.form.FormMetadataCache;
import kd.bos.form.IFormView;
import kd.bos.form.control.Control;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.ListShowParameter;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hies.common.HiesCommonRes;
import kd.hr.hies.common.util.MetaMenuUtil;

@ExcludeFromJacocoGeneratedReport
public final class SwitchRegistryEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final String CLOSECALLBACK_SAVE = "closeCallBack_save";
    public static final String OPKEY_SAVE = "save";
    public static final String FIELD_OLDOP = "oldop";
    public static final String FIELD_NEWOP = "newop";

    public void initialize() {
        super.initialize();
    }

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        BasedataEdit mainEntityBaseData = (BasedataEdit)this.getView().getControl("entity");
        mainEntityBaseData.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        this.addClickListeners(new String[]{"btnsave", "btncancel", "btnclose"});
    }

    public void click(EventObject evt) {
        super.click(evt);
        Control ctlSource = (Control)evt.getSource();
        switch (ctlSource.getKey()) {
            case "btnsave": {
                break;
            }
            case "btncancel": {
                this.getView().close();
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        IFormView view;
        IDataModel model;
        DynamicObject entity;
        super.propertyChanged(args);
        String fieldKey = args.getProperty().getName();
        if (HRStringUtils.equals((String)"entity", (String)fieldKey) && !ObjectUtils.isEmpty((Object)(entity = (DynamicObject)(model = (view = this.getView()).getModel()).getValue("entity")))) {
            FormConfig formConfig = FormMetadataCache.getFormConfig((String)entity.getString("number"));
            model.setValue("bizapp", (Object)MetaMenuUtil.getAppId((String)formConfig.getBizAppNumber()));
        }
    }

    public void beforeBindData(EventObject args) {
        super.beforeBindData(args);
        IFormView view = this.getView();
        view.setEnable(Boolean.FALSE, new String[]{"bizapp"});
        String isView = (String)view.getFormShowParameter().getCustomParam("isView");
        if (StringUtils.isNotBlank((CharSequence)isView)) {
            view.setEnable(Boolean.FALSE, new String[]{"entity"});
            view.setVisible(Boolean.FALSE, new String[]{"btnsave", "btncancel"});
            view.setVisible(Boolean.TRUE, new String[]{"btnclose"});
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (!(formOperate instanceof Save) || !StringUtils.equals((CharSequence)OPKEY_SAVE, (CharSequence)formOperate.getOperateKey()) || args.getOperationResult() == null || args.getOperationResult().isSuccess()) {
            // empty if block
        }
    }

    private void refreshList(AfterDoOperationEventArgs args) {
        FormOperate formOperate = (FormOperate)args.getSource();
        switch (formOperate.getOperateKey()) {
            case "btnsave": 
            case "btncancel": {
                if (!args.getOperationResult().isSuccess()) break;
                ListView listView = (ListView)this.getView();
                listView.refresh();
                listView.clearSelection();
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String fieldKey = beforeF7SelectEvent.getProperty().getName();
        if (StringUtils.equals((CharSequence)fieldKey, (CharSequence)"entity")) {
            ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            showParameter.setFormId("bos_devp_formtreelistf7");
            showParameter.setCustomParam("onlyvisible", (Object)Boolean.FALSE);
            showParameter.setCustomParam("nodeid", (Object)"root");
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        if (CLOSECALLBACK_SAVE.equals(actionId)) {
            this.getView().invokeOperation("refresh");
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "save": {
                if (!this.checkOpExist(args)) break;
                args.setCancel(true);
                return;
            }
        }
    }

    private boolean checkOpExist(BeforeDoOperationEventArgs args) {
        DynamicObject dataEntity = this.getView().getModel().getDataEntity();
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hies_switchregistry");
        DynamicObject entity = dataEntity.getDynamicObject("entity");
        String oldOp = dataEntity.getString(FIELD_OLDOP);
        String newOp = dataEntity.getString(FIELD_NEWOP);
        QFilter[] filters = new QFilter[]{new QFilter("id", "!=", (Object)dataEntity.getLong("id")), new QFilter("entity", "=", (Object)entity.getString("id")), new QFilter(FIELD_OLDOP, "=", (Object)oldOp).or(FIELD_NEWOP, "=", (Object)newOp)};
        DynamicObjectCollection dynamicObjects = helper.queryOriginalCollection("id,name,oldop,newop", filters);
        if (CollectionUtils.isNotEmpty((Collection)dynamicObjects)) {
            DynamicObject dynamicObject = (DynamicObject)dynamicObjects.get(0);
            String dbOldOp = dynamicObject.getString(FIELD_OLDOP);
            String dbNewOp = dynamicObject.getString(FIELD_NEWOP);
            if (StringUtils.equals((CharSequence)dbOldOp, (CharSequence)oldOp)) {
                this.getView().showTipNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u201c%1$s\u201d\u7684\u65e7\u7248\u6309\u94ae\u201c%2$s\u201d\u5b58\u5728\u91cd\u590d\u8bb0\u5f55\uff0c\u8bf7\u4fee\u6539\u3002", (String)HiesCommonRes.SwitchRegistryEdit_0.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]), entity.getString("name"), oldOp));
                return true;
            }
            if (StringUtils.equals((CharSequence)dbNewOp, (CharSequence)newOp)) {
                this.getView().showTipNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u201c%1$s\u201d\u7684\u65b0\u7248\u6309\u94ae\u201c%2$s\u201d\u5b58\u5728\u91cd\u590d\u8bb0\u5f55\uff0c\u8bf7\u4fee\u6539\u3002", (String)HiesCommonRes.SwitchRegistryEdit_1.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]), entity.getString("name"), newOp));
                return true;
            }
        }
        return false;
    }
}
