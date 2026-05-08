/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.FieldEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.servicehelper.HRBizCloudServiceHelper
 */
package kd.hr.hrcs.formplugin.web.activity;

import java.util.EventObject;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.FieldEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.servicehelper.HRBizCloudServiceHelper;

public final class ActivityEditPlugin
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final String[] TYPES = new String[]{"BaseFormModel", "BillFormModel", "DynamicFormModel", "MobileFormModel"};

    public void registerListener(EventObject e) {
        super.registerListener(e);
        BasedataEdit mainentity = (BasedataEdit)this.getControl("bizobj");
        mainentity.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        if (this.hasBeenEnabled()) {
            this.getView().setEnable(Boolean.FALSE, new String[]{"app"});
            this.getView().setEnable(Boolean.FALSE, new String[]{"bizobj"});
        }
        this.getModel().setDataChanged(false);
        DynamicObject activitytype = (DynamicObject)this.getModelVal("activitytype");
        if ("1010_S".equals(activitytype.getString("number"))) {
            FieldEdit fieldAp = (FieldEdit)this.getControl("bizobj");
            fieldAp.setMustInput(true);
            FieldEdit appAp = (FieldEdit)this.getControl("app");
            appAp.setMustInput(true);
        }
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        ((BasedataEdit)this.getControl("app")).setQFilter(new QFilter("bizcloud.number", "in", (Object)HRBizCloudServiceHelper.getBizCloudNumber()));
    }

    public void propertyChanged(PropertyChangedArgs e) {
        if (OperationStatus.ADDNEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
            this.setFiledAppByBizObj(e);
        }
        String fieldName = e.getProperty().getName();
        ChangeData[] changeSet = e.getChangeSet();
        if ("activitytype".equals(fieldName)) {
            ChangeData inDisplayValueChange = changeSet[0];
            DynamicObject newValue = (DynamicObject)inDisplayValueChange.getNewValue();
            if ("1010_S".equals(newValue.getString("number"))) {
                FieldEdit fieldAp = (FieldEdit)this.getControl("bizobj");
                fieldAp.setMustInput(true);
                FieldEdit appAp = (FieldEdit)this.getControl("app");
                appAp.setMustInput(true);
            } else {
                FieldEdit fieldAp = (FieldEdit)this.getControl("bizobj");
                fieldAp.setMustInput(false);
                FieldEdit appAp = (FieldEdit)this.getControl("app");
                appAp.setMustInput(false);
            }
            if (OperationStatus.EDIT.equals((Object)this.getView().getFormShowParameter().getStatus())) {
                if ("1010_S".equals(newValue.getString("number"))) {
                    this.getView().setEnable(Boolean.TRUE, new String[]{"bizobj"});
                } else {
                    this.getView().setEnable(Boolean.FALSE, new String[]{"bizobj"});
                }
            }
        }
    }

    private boolean hasBeenEnabled() {
        HRBaseServiceHelper recHelper = new HRBaseServiceHelper("hrcs_activityenablerec");
        QFilter filter = new QFilter("activity", "=", this.getModel().getValue("id"));
        return recHelper.isExists(filter);
    }

    private void setFiledAppByBizObj(PropertyChangedArgs e) {
        String fieldName = e.getProperty().getName();
        if ("bizobj".equals(fieldName)) {
            DynamicObject bizDyn = (DynamicObject)this.getModel().getValue("bizobj");
            if (bizDyn != null) {
                this.getModel().setValue("app", bizDyn.get("bizappid"));
            } else {
                this.getModel().setValue("app", null);
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String property = evt.getProperty().getName();
        if ("bizobj".equals(property)) {
            ListShowParameter showParameter = (ListShowParameter)evt.getFormShowParameter();
            showParameter.getListFilterParameter().setFilter(new QFilter("modeltype", "in", (Object)TYPES));
        }
    }
}
