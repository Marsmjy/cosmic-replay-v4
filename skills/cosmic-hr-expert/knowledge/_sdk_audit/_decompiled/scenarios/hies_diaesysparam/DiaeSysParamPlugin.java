/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.events.BizDataEventArgs
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.Control
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hies.business.FieldStyleServiceHelper
 *  kd.hr.hies.common.HiesCommonRes
 */
package kd.hr.hies.formplugin;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.events.BizDataEventArgs;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.Control;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hies.business.FieldStyleServiceHelper;
import kd.hr.hies.common.HiesCommonRes;

@ExcludeFromJacocoGeneratedReport
public final class DiaeSysParamPlugin
extends HRDataBaseEdit {
    public static final String KEY_BILL = "hies_diaesysparam";
    String TXT_VALUE = "value";
    public static final String ACTION_SELECT_SERVICE_PLUGINS = "closeCallBack_selectServicePlugins";
    public static final String FIELD_PLUGIN = "plugin";

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        this.addClickListeners(new String[]{FIELD_PLUGIN});
    }

    public void createNewData(BizDataEventArgs e) {
        super.createNewData(e);
        DynamicObject dynamicObject = BusinessDataServiceHelper.loadSingle((String)KEY_BILL, (QFilter[])new QFilter[0]);
        if (Objects.nonNull(dynamicObject)) {
            e.setDataEntity((Object)dynamicObject);
        }
    }

    public void afterCreateNewData(EventObject e) {
        if (HRStringUtils.isBlank((CharSequence)this.getModel().getValue("globalfieldstyle").toString())) {
            JSONObject stringFieldStyleMap = new JSONObject();
            JSONObject fieldStyleMap = FieldStyleServiceHelper.getDefaultStyle();
            stringFieldStyleMap.put("excel", (Object)fieldStyleMap);
            JSONObject valueMap = new JSONObject();
            valueMap.put("globalfieldstyle", (Object)stringFieldStyleMap);
            this.getModel().setValue("globalfieldstyle", (Object)valueMap.toJSONString());
        }
        this.showGlobalStyle();
    }

    public void click(EventObject evt) {
        switch (((Control)evt.getSource()).getKey()) {
            case "plugin": {
                this.showPlugin();
                break;
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        Map value;
        Object returnData = evt.getReturnData();
        if (returnData == null) {
            return;
        }
        String actionId = evt.getActionId();
        if (ACTION_SELECT_SERVICE_PLUGINS.equalsIgnoreCase(actionId) && (value = (Map)returnData).get(this.TXT_VALUE) != null && value.get(this.TXT_VALUE) instanceof List) {
            List plugins = (List)value.get(this.TXT_VALUE);
            String pluginString = "";
            if (!plugins.isEmpty()) {
                pluginString = SerializationUtils.toJsonString((Object)plugins);
            }
            this.getModel().setValue(FIELD_PLUGIN, (Object)pluginString);
        }
    }

    private void showGlobalStyle() {
        HashMap param = Maps.newHashMapWithExpectedSize((int)1);
        param.put("formId", "hies_tplfieldstyle");
        param.put("entityNumber", "globalfieldstyle");
        param.put("entityName", "globalfieldstyle");
        HashMap fieldsMap = new HashMap();
        HashMap<String, Object> fieldsDetailMap = new HashMap<String, Object>();
        fieldsDetailMap.put("fieldname", ResManager.loadKDString((String)"\u8868\u5934", (String)HiesCommonRes.DiaeSysParamPlugin_10.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
        fieldsDetailMap.put("fieldcontent", ResManager.loadKDString((String)"\u8868\u6837\u5185\u5bb9", (String)HiesCommonRes.DiaeSysParamPlugin_11.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
        fieldsDetailMap.put("pid", 1);
        fieldsDetailMap.put("seq", 1);
        fieldsMap.put("excel", fieldsDetailMap);
        param.put("fields", fieldsMap);
        param.put("tmpltype", "IMPT");
        param.put("fieldstyle", this.getModel().getValue("globalfieldstyle"));
        param.put("isSysParamsPage", true);
        FormShowParameter showParameter = FormShowParameter.createFormShowParameter((Map)param);
        showParameter.getOpenStyle().setShowType(ShowType.InContainer);
        showParameter.getOpenStyle().setTargetKey("fieldstyleflex");
        this.getView().showForm(showParameter);
    }

    private void showPlugin() {
        Object value = this.getModel().getValue(FIELD_PLUGIN);
        String formId = this.getView().getFormShowParameter().getFormId();
        List<?> plugins = this.getPluginsArray(value);
        FormShowParameter formShowParameter = this.buildShowParams(formId, plugins, new CloseCallBack((IFormPlugin)this, ACTION_SELECT_SERVICE_PLUGINS));
        this.getView().showForm(formShowParameter);
    }

    private List<?> getPluginsArray(Object val) {
        List plugins = new ArrayList();
        if (val != null && HRStringUtils.isNotEmpty((String)val.toString())) {
            plugins = SerializationUtils.fromJsonStringToList((String)val.toString(), Map.class);
        }
        return plugins;
    }

    private FormShowParameter buildShowParams(String formId, List<?> plugins, CloseCallBack callback) {
        FormShowParameter showParam = new FormShowParameter();
        showParam.setFormId("hies_plugins");
        showParam.getOpenStyle().setShowType(ShowType.Modal);
        showParam.setCustomParam("value", plugins);
        showParam.setCustomParam("formId", (Object)formId);
        showParam.setCloseCallBack(callback);
        return showParam;
    }

    public void preOpenForm(PreOpenFormEventArgs e) {
        FormShowParameter formShowParameter = e.getFormShowParameter();
        formShowParameter.setCaption(ResManager.loadKDString((String)"\u53c2\u6570\u914d\u7f6e", (String)HiesCommonRes.DiaeSysParamPlugin_0.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
    }
}
