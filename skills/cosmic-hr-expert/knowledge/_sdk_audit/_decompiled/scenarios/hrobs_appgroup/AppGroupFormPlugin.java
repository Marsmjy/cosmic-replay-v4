/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.OperationStatus
 *  kd.bos.entity.property.IntegerProp
 *  kd.bos.form.IFormView
 *  kd.bos.form.field.DecimalEdit
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.hrmp.hrobs.business.domain.repository.AppGroupRepository
 */
package kd.hrmp.hrobs.formplugin.appgroup;

import java.math.BigDecimal;
import java.util.EventObject;
import kd.bos.bill.OperationStatus;
import kd.bos.entity.property.IntegerProp;
import kd.bos.form.IFormView;
import kd.bos.form.field.DecimalEdit;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.hrmp.hrobs.business.domain.repository.AppGroupRepository;

public final class AppGroupFormPlugin
extends AbstractFormPlugin {
    public static final String INDEX = "index";

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        IFormView view = this.getView();
        if (view.getFormShowParameter().getStatus().getValue() == OperationStatus.ADDNEW.getValue()) {
            DecimalEdit index = (DecimalEdit)view.getControl(INDEX);
            IntegerProp property = (IntegerProp)index.getProperty();
            BigDecimal max = property.getMax();
            this.initIndex(max);
        }
    }

    private void initIndex(BigDecimal max) {
        Integer maxIndex = AppGroupRepository.getRepository().queryMaxIndex();
        int index = maxIndex + 1;
        if (max.compareTo(BigDecimal.valueOf(index)) <= 0) {
            index = max.intValue();
        }
        this.getModel().setValue(INDEX, (Object)index);
    }
}
