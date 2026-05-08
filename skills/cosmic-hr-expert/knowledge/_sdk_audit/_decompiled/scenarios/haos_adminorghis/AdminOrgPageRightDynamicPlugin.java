/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin
 */
package kd.hr.haos.formplugin.web.adminorg;

import java.text.ParseException;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.form.FormShowParameter;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin;

public final class AdminOrgPageRightDynamicPlugin
extends HRDynamicFormBasePlugin {
    private static final String RELATE_ENTITY_TYPE_ID = "2030";
    private static Log logger = LogFactory.getLog(AdminOrgPageRightDynamicPlugin.class);

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
        this.setQueryTime();
        customParam.put("boid", this.getModel().getDataEntity().getString("boid"));
        String nameLocal = this.getModel().getDataEntity().getString("name");
        String caption = ResManager.loadKDString((String)"\u5173\u8054\u4fe1\u606f", (String)"AdminOrgPageRightDynamicPlugin_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
        caption = caption + "-" + nameLocal;
        formShowParameter.setCustomParam("caption", (Object)caption);
        this.getView().cacheFormShowParameter();
    }

    private void setQueryTime() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Object param = this.getView().getFormShowParameter().getCustomParam("searchdate");
        Object structProject = this.getView().getFormShowParameter().getCustomParam("structproject");
        if (structProject != null) {
            map.put("structproject", structProject);
        }
        if (param != null) {
            try {
                Date searchDate = HRDateTimeUtils.parseDate((String)param.toString());
                map.put("searchdate", searchDate);
            }
            catch (ParseException e) {
                logger.error("AdminOrgPageRightDynamicPlugin parse date error!");
                throw new KDBizException(new ErrorCode("AdminOrgPageRightDynamicPlugin", e.getMessage()), new Object[0]);
            }
        }
        this.getView().getFormShowParameter().setCustomParam("customvariables", map);
    }
}
