/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.Row
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ArrayUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.BizDataEventArgs
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.Save
 *  kd.bos.form.FormConfig
 *  kd.bos.form.FormMetadataCache
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.OpBizRuleSetServiceHelper
 *  kd.hr.expt.common.enu.HiesExportRes
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hies.common.util.MetaMenuUtil
 *  kd.hr.hies.common.util.MethodUtil
 *  kd.hr.hies.common.util.TemplateUtil
 */
package kd.hr.expt.formplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ArrayUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.BizDataEventArgs;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.Save;
import kd.bos.form.FormConfig;
import kd.bos.form.FormMetadataCache;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.OpBizRuleSetServiceHelper;
import kd.hr.expt.common.enu.HiesExportRes;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hies.common.util.MetaMenuUtil;
import kd.hr.hies.common.util.MethodUtil;
import kd.hr.hies.common.util.TemplateUtil;

@ExcludeFromJacocoGeneratedReport
public final class EntityEncryptEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final String CLOSECALLBACK_SAVE = "closeCallBack_save";
    public static final String OPKEY_SAVE = "save";

    public void initialize() {
        super.initialize();
    }

    public void createNewData(BizDataEventArgs e) {
        super.createNewData(e);
        IFormView view = this.getView();
        String formTitle = ResManager.loadKDString((String)"\u5bfc\u51fa\u5b9e\u4f53\u52a0\u5bc6\u8bbe\u7f6e", (String)HiesExportRes.EntityEncryptEdit_2.resId(), (String)"hrmp-hies-export", (Object[])new Object[0]);
        view.setFormTitle(new LocaleString(formTitle));
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

    public void itemClick(ItemClickEvent evt) {
        switch (evt.getItemKey()) {
            default: 
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        IFormView view;
        IDataModel model;
        DynamicObject entity;
        super.propertyChanged(args);
        String fieldKey = args.getProperty().getName();
        if (HRStringUtils.equals((String)"entity", (String)fieldKey) && !ObjectUtils.isEmpty((Object)(entity = (DynamicObject)(model = (view = this.getView()).getModel()).getValue("entity")))) {
            FormConfig formConfig = FormMetadataCache.getFormConfig((String)entity.getString("id"));
            model.setValue("app", (Object)MetaMenuUtil.getAppId((String)formConfig.getBizAppNumber()));
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        switch (evt.getItemKey()) {
            default: 
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
    }

    public void beforeBindData(EventObject args) {
        super.beforeBindData(args);
        IFormView view = this.getView();
        view.setEnable(Boolean.FALSE, new String[]{"app"});
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
        if (formOperate instanceof Save && StringUtils.equals((CharSequence)OPKEY_SAVE, (CharSequence)formOperate.getOperateKey()) && args.getOperationResult() != null && args.getOperationResult().isSuccess()) {
            OpBizRuleSetServiceHelper.clearCache();
            this.getView().getParentView().showSuccessNotification(ResManager.loadKDString((String)"\u4fdd\u5b58\u6210\u529f\u3002", (String)HiesExportRes.EntityEncryptEdit_1.resId(), (String)"hrmp-hies-export", (Object[])new Object[0]));
            this.getView().close();
        }
    }

    public void preOpenForm(PreOpenFormEventArgs e) {
        FormShowParameter formShowParameter = e.getFormShowParameter();
        String isView = (String)formShowParameter.getCustomParam("isView");
        if (StringUtils.isNotBlank((CharSequence)isView)) {
            formShowParameter.setCaption(ResManager.loadKDString((String)"\u5bfc\u51fa\u5b9e\u4f53\u52a0\u5bc6", (String)HiesExportRes.EntityEncryptEdit_4.resId(), (String)"hrmp-hies-export", (Object[])new Object[0]));
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String fieldKey = beforeF7SelectEvent.getProperty().getName();
        if (StringUtils.equals((CharSequence)fieldKey, (CharSequence)"entity")) {
            ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            showParameter.getListFilterParameter().setFilter(new QFilter("number", "in", this.getBillNumbers()));
            showParameter.setCaption(ResManager.loadKDString((String)"\u5bfc\u51fa\u52a0\u5bc6\u5b9e\u4f53\u9009\u62e9", (String)HiesExportRes.EntityEncryptEdit_3.resId(), (String)"hrmp-hies-export", (Object[])new Object[0]));
        }
    }

    private List<String> getBillNumbers() {
        String fields = "number";
        String orderBy = "bizappid asc";
        Object[] validEntityNumberFilter = TemplateUtil.getValidEntityNumberFilter((boolean)true);
        List filters = MethodUtil.arrayToList((Object[])validEntityNumberFilter);
        Object[] entityEncryptObjects = BusinessDataServiceHelper.load((String)"hies_entityencryptconf", (String)"entity", (QFilter[])new QFilter[0]);
        if (ArrayUtils.isNotEmpty((Object[])entityEncryptObjects)) {
            Set entityIdSet = Arrays.stream(entityEncryptObjects).map(o -> o.getDynamicObject("entity").getPkValue()).collect(Collectors.toSet());
            QFilter entityIdFilter = new QFilter("id", "not in", entityIdSet);
            filters.add(entityIdFilter);
        }
        ArrayList<String> billNumbers = new ArrayList<String>();
        try (DataSet ds = ORM.create().queryDataSet("bos_entityobject", "bos_entityobject", fields, filters.toArray(new QFilter[0]), orderBy);){
            for (Row row : ds) {
                billNumbers.add(row.getString("number"));
            }
        }
        return billNumbers;
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        if (CLOSECALLBACK_SAVE.equals(actionId)) {
            this.getView().invokeOperation("refresh");
        }
    }
}
