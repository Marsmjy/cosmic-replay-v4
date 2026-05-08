/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.ListShowParameter
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hr.hrcs.formplugin.web.econtract;

import java.util.HashMap;
import java.util.Map;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.ListShowParameter;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public final class TplVariableConfigTest
extends HRDataBaseList {
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if ("tplvariableconfig".equals(operateKey)) {
            ListShowParameter fsp = new ListShowParameter();
            fsp.setBillFormId("hrcs_commonvariable");
            fsp.getOpenStyle().setTargetKey("hrcs");
            fsp.setFormId("bos_list");
            HashMap<String, Object> params = new HashMap<String, Object>(16);
            Map originalCustParams = this.getView().getFormShowParameter().getCustomParams();
            params.putAll(originalCustParams);
            params.put("formnumber", "hlcm_contracttemplate");
            HashMap<String, String> mainEntity = new HashMap<String, String>();
            mainEntity.put("hlcm_contractapplybase", ResManager.loadKDString((String)"\u7b7e\u7f72\u7533\u8bf7\u5355", (String)"TplVariableConfigPlugin_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            params.put("mappingEntity", mainEntity);
            fsp.setCustomParams(params);
            fsp.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            this.getView().showForm((FormShowParameter)fsp);
        }
    }
}
