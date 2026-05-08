/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.mvc.list.ListView
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrptmc.business.preindex.PresetIndexServiceHelper
 */
package kd.hr.hrptmc.formplugin.web.preindex;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.mvc.list.ListView;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrptmc.business.preindex.PresetIndexServiceHelper;

public final class ReportPresetIndexListPlugin
extends HRDataBaseList {
    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        if (args.getSource() instanceof AbstractOperate) {
            AbstractOperate operate = (AbstractOperate)args.getSource();
            String operateKey = operate.getOperateKey();
            if ("exportconfig".equals(operateKey)) {
                ListView listView = (ListView)this.getView();
                List<Object> idList = listView.getSelectedRows().stream().map(ListSelectedRow::getPrimaryKeyValue).collect(Collectors.toList());
                this.exportConfigExcel(idList);
            } else if ("importconfig".equals(operateKey)) {
                this.importConfigFile();
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        Map ret;
        super.closedCallBack(event);
        if (HRStringUtils.equals((String)event.getActionId(), (String)"report_conf_exp_closecallback") && (ret = (Map)event.getReturnData()) != null) {
            String expStatus = (String)ret.get("exp_status");
            if ("exp_sucess".equals(expStatus)) {
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5bfc\u51fa\u6210\u529f", (String)"ReportPresetIndexListPlugin_1", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            } else if ("exp_fail".equals(expStatus)) {
                String traceid = (String)ret.get("traceid");
                this.getView().showErrorNotification(String.format(ResManager.loadKDString((String)"\u5bfc\u51fa\u5931\u8d25\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u5206\u6790\u65e5\u5fd7\uff0ctraceid:%s\u3002", (String)"ReportPresetIndexListPlugin_2", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), traceid));
            }
        }
    }

    private void exportConfigExcel(List<Object> idList) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrptmc_configexportstart");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        Map customParams = showParameter.getCustomParams();
        customParams.put("formId", "hrptmc_preindex");
        customParams.put("preIndexPks", idList);
        PresetIndexServiceHelper presetIndexHelper = new PresetIndexServiceHelper();
        List servicePkList = presetIndexHelper.queryServicePkList(idList);
        customParams.put("servicePks", servicePkList);
        List anObjPkList = presetIndexHelper.queryAnObjPkList(idList);
        customParams.put("exportAnObjPks", anObjPkList);
        customParams.put("exp_excel_name", ResManager.loadKDString((String)"\u9884\u7f6e\u6307\u6807\u5bfc\u51fa", (String)"ReportPresetIndexListPlugin_0", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
        customParams.put("exp_all_sheet", "preindex");
        customParams.put("entitynumber", "hrptmc_preindex");
        customParams.put("entityName", StringUtils.isBlank((CharSequence)this.getView().getFormShowParameter().getCaption()) ? this.getView().getFormShowParameter().getFormConfig().getCaption().getLocaleValue() : this.getView().getFormShowParameter().getCaption());
        customParams.put("taskClassName", "kd.hr.hrptmc.formplugin.web.exp.HReportConfExportTask");
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "report_conf_exp_closecallback"));
        this.getView().showForm(showParameter);
    }

    private void importConfigFile() {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrptmc_configimportstart");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        Map customParams = showParameter.getCustomParams();
        customParams.put("formId", "hrptmc_preindex");
        customParams.put("entityName", StringUtils.isBlank((CharSequence)this.getView().getFormShowParameter().getCaption()) ? this.getView().getFormShowParameter().getFormConfig().getCaption().getLocaleValue() : this.getView().getFormShowParameter().getCaption());
        this.getView().showForm(showParameter);
    }

    public void setFilter(SetFilterEvent evt) {
        super.setFilter(evt);
    }
}
