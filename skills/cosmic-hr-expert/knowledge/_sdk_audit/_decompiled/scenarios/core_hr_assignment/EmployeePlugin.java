/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.FormOperate
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hrmp.hrpi.formplugin.web.person;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class EmployeePlugin
extends HRDataBaseEdit {
    public void initialize() {
        super.initialize();
        this.getPageCache().put("skipChangeTips", "true");
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        super.beforeItemClick(evt);
    }

    public void propertyChanged(PropertyChangedArgs propertyChangedArgs) {
        super.propertyChanged(propertyChangedArgs);
        String key = propertyChangedArgs.getProperty().getName();
        if (HRStringUtils.equals((String)key, (String)"birthday")) {
            Date birthday = (Date)this.getModel().getValue("birthday");
            BigDecimal age = birthday == null ? BigDecimal.ZERO : BigDecimal.valueOf(HRDateTimeUtils.dateDiff((String)"yyyy", (Date)birthday, (Date)new Date()));
            this.getModel().setValue("age", (Object)age);
            this.getView().updateView("age");
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        DynamicObject dataEntity = this.getModel().getDataEntity();
        List iDataEntityProperties = dataEntity.getDataEntityState().GetDirtyProperties();
        if (formOperate.getOperateKey().equals("confirmchange") && iDataEntityProperties.size() > 0) {
            this.getModel().setValue("bsed", (Object)new Date());
        }
    }
}
