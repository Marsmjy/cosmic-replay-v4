/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  org.apache.commons.lang.StringUtils
 */
package kd.hrmp.lcs.formplugin.web.basedata;

import java.util.EventObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import org.apache.commons.lang.StringUtils;

public class CostItemEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final String KEY_COSTITEMTYPE = "costitemtype";
    private static final String KEY_COSTBIZTYPE = "costbiztype";

    public void registerListener(EventObject e) {
        super.registerListener(e);
        BasedataEdit costItemTypeEdit = (BasedataEdit)this.getView().getControl(KEY_COSTITEMTYPE);
        costItemTypeEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void propertyChanged(PropertyChangedArgs e) {
        ChangeData[] changeData;
        DynamicObject newValue;
        String fieldKey = e.getProperty().getName();
        if (StringUtils.equals((String)KEY_COSTBIZTYPE, (String)fieldKey) && (newValue = (DynamicObject)(changeData = e.getChangeSet())[0].getNewValue()) != null) {
            this.getView().getModel().setValue(KEY_COSTITEMTYPE, null);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent e) {
        String propertyName = e.getProperty().getName();
        if (HRStringUtils.equals((String)KEY_COSTITEMTYPE, (String)propertyName)) {
            long costBizTypeId = this.getView().getModel().getDataEntity().getLong("costbiztype.id");
            if (costBizTypeId == 0L) {
                String message = ResManager.loadKDString((String)"\u8bf7\u5148\u8bbe\u7f6e\u6210\u672c\u4e1a\u52a1\u7c7b\u578b\u3002", (String)"CostItemEdit_0", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]);
                this.getView().showTipNotification(message);
                e.setCancel(true);
            } else {
                ListShowParameter param = (ListShowParameter)e.getFormShowParameter();
                QFilter idFilter = new QFilter("costbiztype.id", "=", (Object)costBizTypeId);
                param.getListFilterParameter().setFilter(idFilter);
            }
        }
    }
}
