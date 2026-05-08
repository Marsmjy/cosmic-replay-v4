/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.FieldEdit
 *  kd.bos.form.field.MulComboEdit
 *  kd.bos.form.field.TextEdit
 *  kd.bos.form.operate.FormOperate
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.common.constants.SignType
 *  kd.hr.hrcs.formplugin.web.econtract.EContractTemplateEditPlugin$1
 */
package kd.hr.hrcs.formplugin.web.econtract;

import java.util.EventObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.FieldEdit;
import kd.bos.form.field.MulComboEdit;
import kd.bos.form.field.TextEdit;
import kd.bos.form.operate.FormOperate;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.common.constants.SignType;
import kd.hr.hrcs.formplugin.web.econtract.EContractTemplateEditPlugin;

public final class EContractTemplateEditPlugin
extends HRDataBaseEdit {
    private static final String SAVE = "save";

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        this.addItemClickListeners(new String[]{"tbmain"});
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String number;
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)SAVE, (CharSequence)formOperate.getOperateKey()) && StringUtils.isNotBlank((CharSequence)(number = (String)this.getModel().getValue("number")))) {
            this.getModel().setValue("number", (Object)number.replaceAll("\\s*", ""));
        }
    }

    public void propertyChanged(PropertyChangedArgs pca) {
        ChangeData[] changeSet;
        Boolean newValue;
        String fieldKey = pca.getProperty().getName();
        if (StringUtils.equals((CharSequence)"signtype", (CharSequence)fieldKey)) {
            this.changePlane();
        } else if (StringUtils.equals((CharSequence)"corporateseal", (CharSequence)fieldKey) && (newValue = (Boolean)(changeSet = pca.getChangeSet())[0].getNewValue()).booleanValue()) {
            FieldEdit fieldEdit = (FieldEdit)this.getControl("legalsealkey");
            fieldEdit.setMustInput(true);
        }
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        this.changePlane();
        FieldEdit fieldEdit = (FieldEdit)this.getControl("onlyfirstauth");
        fieldEdit.setMustInput(true);
        Boolean newValue = (Boolean)this.getModelVal("corporateseal");
        if (newValue.booleanValue()) {
            FieldEdit legalSealKey = (FieldEdit)this.getControl("legalsealkey");
            legalSealKey.setMustInput(true);
        }
    }

    private void changePlane() {
        String signtype = (String)this.getModel().getValue("signtype");
        switch (1.$SwitchMap$kd$hr$hrcs$common$constants$SignType[SignType.getSignTypeByCode((String)signtype).ordinal()]) {
            case 1: {
                this.changeControlNameByPerson();
                break;
            }
            case 2: {
                this.changeControlNameBYEN();
                break;
            }
            case 3: {
                this.changeControlNameBYDouble();
                break;
            }
        }
    }

    private void changeControlNameByPerson() {
        this.getView().setVisible(Boolean.TRUE, new String[]{"persionadv"});
        this.setFieldMustInput(false, true);
        this.getView().setVisible(Boolean.FALSE, new String[]{"companyadv"});
        FieldEdit fieldEdit = (FieldEdit)this.getControl("keyworddouble");
        if (fieldEdit != null) {
            // empty if block
        }
        if ((fieldEdit = (FieldEdit)this.getControl("offsetxdouble")) != null) {
            fieldEdit.setCaption(new LocaleString(ResManager.loadKDString((String)"\u7b7e\u7f72\u5750\u6807\u504f\u79fb\u91cf-X\u8f74", (String)"EContractTemplateEditPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        }
        if ((fieldEdit = (FieldEdit)this.getControl("offsetydouble")) != null) {
            fieldEdit.setCaption(new LocaleString(ResManager.loadKDString((String)"\u7b7e\u7f72\u5750\u6807\u504f\u79fb\u91cf-Y\u8f74", (String)"EContractTemplateEditPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        }
        if (this.getModelVal("sealtypeid") != null) {
            this.setModelNullVal("sealtypeid");
        }
        if (HRStringUtils.isNotEmpty((String)((String)this.getModelVal("keyword")))) {
            this.setModelNullVal("keyword");
        }
        if (HRStringUtils.isNotEmpty((String)((String)this.getModelVal("legalsealkey")))) {
            this.setModelNullVal("legalsealkey");
        }
        this.setModelVal("corporateseal", 0);
    }

    private void changeControlNameBYEN() {
        this.getView().setVisible(Boolean.FALSE, new String[]{"persionadv"});
        this.setFieldMustInput(true, false);
        this.getView().setVisible(Boolean.TRUE, new String[]{"companyadv"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"acrosspagesign"});
        FieldEdit fieldEdit = (FieldEdit)this.getControl("keyword");
        if (fieldEdit != null) {
            // empty if block
        }
        if ((fieldEdit = (FieldEdit)this.getControl("offsetx")) != null) {
            fieldEdit.setCaption(new LocaleString(ResManager.loadKDString((String)"\u7b7e\u7f72\u5750\u6807\u504f\u79fb\u91cf-X\u8f74", (String)"EContractTemplateEditPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        }
        if ((fieldEdit = (FieldEdit)this.getControl("offsety")) != null) {
            fieldEdit.setCaption(new LocaleString(ResManager.loadKDString((String)"\u7b7e\u7f72\u5750\u6807\u504f\u79fb\u91cf-Y\u8f74", (String)"EContractTemplateEditPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        }
        if (HRStringUtils.isNotEmpty((String)((String)this.getModelVal("personsignway")))) {
            this.setModelNullVal("personsignway");
        }
        if (HRStringUtils.isNotEmpty((String)((String)this.getModelVal("keyworddouble")))) {
            this.setModelNullVal("keyworddouble");
        }
        if (HRStringUtils.isNotEmpty((String)((String)this.getModelVal("legalsealkey")))) {
            this.setModelNullVal("legalsealkey");
        }
        this.setModelVal("corporateseal", 0);
    }

    private void changeControlNameBYDouble() {
        this.getView().setVisible(Boolean.TRUE, new String[]{"persionadv"});
        this.getView().setVisible(Boolean.TRUE, new String[]{"companyadv"});
        this.setFieldMustInput(true, true);
        this.getView().setVisible(Boolean.TRUE, new String[]{"acrosspagesign"});
        FieldEdit fieldEdit = (FieldEdit)this.getControl("keyworddouble");
        if (fieldEdit != null) {
            // empty if block
        }
        if ((fieldEdit = (FieldEdit)this.getControl("offsetxdouble")) != null) {
            fieldEdit.setCaption(new LocaleString(ResManager.loadKDString((String)"\u4e59\u65b9\u7b7e\u7f72\u5750\u6807\u504f\u79fb\u91cf-X\u8f74", (String)"EContractTemplateEditPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        }
        if ((fieldEdit = (FieldEdit)this.getControl("offsetydouble")) != null) {
            fieldEdit.setCaption(new LocaleString(ResManager.loadKDString((String)"\u4e59\u65b9\u7b7e\u7f72\u5750\u6807\u504f\u79fb\u91cf-Y\u8f74", (String)"EContractTemplateEditPlugin_5", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        }
        if ((fieldEdit = (FieldEdit)this.getControl("keyword")) != null) {
            // empty if block
        }
        if ((fieldEdit = (FieldEdit)this.getControl("offsetx")) != null) {
            fieldEdit.setCaption(new LocaleString(ResManager.loadKDString((String)"\u7532\u65b9\u7b7e\u7f72\u5750\u6807\u504f\u79fb\u91cf-X\u8f74", (String)"EContractTemplateEditPlugin_7", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        }
        if ((fieldEdit = (FieldEdit)this.getControl("offsety")) != null) {
            fieldEdit.setCaption(new LocaleString(ResManager.loadKDString((String)"\u7532\u65b9\u7b7e\u7f72\u5750\u6807\u504f\u79fb\u91cf-Y\u8f74", (String)"EContractTemplateEditPlugin_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        }
    }

    private void setFieldMustInput(boolean comPanyFlag, boolean personFlag) {
        ComboEdit personSignWay = (ComboEdit)this.getControl("personsignway");
        personSignWay.setMustInput(personFlag);
        TextEdit keywordDouble = (TextEdit)this.getControl("keyworddouble");
        keywordDouble.setMustInput(personFlag);
        MulComboEdit signidea = (MulComboEdit)this.getControl("signidea");
        signidea.setMustInput(personFlag);
        ComboEdit sealway = (ComboEdit)this.getControl("sealway");
        sealway.setMustInput(comPanyFlag);
        BasedataEdit sealtypeid = (BasedataEdit)this.getControl("sealtypeid");
        sealtypeid.setMustInput(comPanyFlag);
        TextEdit keyword = (TextEdit)this.getControl("keyword");
        keyword.setMustInput(comPanyFlag);
    }
}
