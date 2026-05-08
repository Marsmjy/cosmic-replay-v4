/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin
 */
package kd.hrmp.hbpm.formplugin.web.position;

import java.util.EventObject;
import java.util.HashMap;
import kd.bos.form.FormShowParameter;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin;

public class PositionPageRightDynamicPlugin
extends HRDynamicFormBasePlugin {
    private static final String RELATE_ENTITY_TYPE_ID = "1021";
    private static Log logger = LogFactory.getLog(PositionPageRightDynamicPlugin.class);

    public void preOpenForm(PreOpenFormEventArgs args) {
        super.preOpenForm(args);
        FormShowParameter showParameter = (FormShowParameter)args.getSource();
        showParameter.setCustomParam("hbss_entitytype_id", (Object)RELATE_ENTITY_TYPE_ID);
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        HashMap<String, String> customParam = (HashMap<String, String>)formShowParameter.getCustomParam("customPKFilter");
        if (customParam == null) {
            customParam = new HashMap<String, String>();
            formShowParameter.setCustomParam("customPKFilter", customParam);
        }
        customParam.put("boid", this.getModel().getDataEntity().getString("boid"));
        this.getView().cacheFormShowParameter();
    }
}
