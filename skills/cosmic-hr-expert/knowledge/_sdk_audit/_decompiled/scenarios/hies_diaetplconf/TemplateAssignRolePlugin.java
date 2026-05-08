/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.hies.formplugin;

import java.util.HashMap;
import java.util.Map;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

@ExcludeFromJacocoGeneratedReport
public final class TemplateAssignRolePlugin
extends HRDataBaseEdit {
    public static final String ACTION_ROLEOBJ_HELP = "closeCallBack_roleObj_Help";

    public void initialize() {
        super.initialize();
        this.addListener();
    }

    private void addListener() {
        this.addItemClickListeners(new String[]{"toolbar_role"});
        Toolbar roleToolBar = (Toolbar)this.getControl("toolbar_role");
        roleToolBar.addItemClickListener((ItemClickListener)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void closedCallBack(ClosedCallBackEvent e) {
        super.closedCallBack(e);
        String actionId = e.getActionId();
        if (ACTION_ROLEOBJ_HELP.equals(actionId)) {
            Object returnData = e.getReturnData();
            if (!(returnData instanceof ListSelectedRowCollection)) {
                return;
            }
            HashMap<Object, String> roleObjInfos = new HashMap<Object, String>(16);
            ListSelectedRowCollection lsrc = (ListSelectedRowCollection)returnData;
            for (ListSelectedRow listSelectedRow : lsrc) {
                Object pkValue = listSelectedRow.getPrimaryKeyValue();
                String dimObjName = listSelectedRow.getName();
                roleObjInfos.put(pkValue, dimObjName);
            }
            this.fillRoleObjList(roleObjInfos);
        }
    }

    private void fillRoleObjList(Map<Object, String> dimObjInfos) {
        if (CollectionUtils.isEmpty(dimObjInfos)) {
            return;
        }
        this.getModel().beginInit();
        TableValueSetter vs = new TableValueSetter(new String[0]);
        vs.addField("role", new Object[0]);
        for (Map.Entry<Object, String> entry : dimObjInfos.entrySet()) {
            vs.addRow(new Object[]{entry.getKey()});
        }
        ((AbstractFormDataModel)this.getModel()).batchCreateNewEntryRow("rolelist", vs);
        this.getModel().endInit();
        this.getView().updateView("rolelist");
    }

    static /* synthetic */ IDataModel access$000(TemplateAssignRolePlugin x0) {
        return x0.getModel();
    }
}
