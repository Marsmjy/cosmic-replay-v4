/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListColumnsArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.IListView
 *  kd.bos.list.ListOperationColumn
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrptmc.business.anobj.AnalyseObjectService
 *  kd.hr.hrptmc.common.constant.anobj.AnalyseObjectConstants
 *  kd.hr.hrptmc.common.util.HRReportParamUtils
 */
package kd.hr.hrptmc.formplugin.web.anobj;

import java.util.EventObject;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.IListView;
import kd.bos.list.ListOperationColumn;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrptmc.business.anobj.AnalyseObjectService;
import kd.hr.hrptmc.common.constant.anobj.AnalyseObjectConstants;
import kd.hr.hrptmc.common.util.HRReportParamUtils;

public final class AnalyseObjectListPlugin
extends HRDataBaseList
implements AnalyseObjectConstants {
    private static final Log LOGGER = LogFactory.getLog(AnalyseObjectListPlugin.class);
    private static final String OP_DATA_EXTRACT = "dataextract";
    private static final String OP_CONFIG_DATA_PERM = "configdataperm";
    private static final String OP_IMPORT_BY_TEMP_LIB = "importbytemplatelib";
    private static final String OP_COPY_AN_OBJ = "copyanobj";
    private static final String CALL_BACK_KEY_CHOOSE_TEMPLATE = "closeCallBack_ChooseTemplate";
    private static final String CALL_BACK_KEY_CHOOSE_EXTRACT = "closeCallBack_ExtractConfig";

    public void setFilter(SetFilterEvent evt) {
        super.setFilter(evt);
        evt.getQFilters().add(new QFilter("objecttype", "=", (Object)"multientity"));
    }

    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        super.beforeCreateListColumns(args);
        try {
            boolean openExtract = HRReportParamUtils.isOpenExtract();
            if (!openExtract) {
                args.getListColumns().stream().filter(col -> HRStringUtils.equals((String)col.getListFieldKey(), (String)"listoperationcolumnap")).findFirst().flatMap(col -> ((ListOperationColumn)col).getOperationColItems().stream().filter(item -> HRStringUtils.equals((String)item.getOperationKey(), (String)OP_DATA_EXTRACT)).findFirst()).ifPresent(item -> item.setVisible(false));
            }
        }
        catch (Exception exception) {
            LOGGER.error("AnalyseObjectListPlugin beforeCreateListColumns: ", (Throwable)exception);
        }
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        if (!HRReportParamUtils.showExportSql()) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"exportconfigsql"});
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        try {
            this.doOperation(args);
        }
        catch (Exception exception) {
            LOGGER.error("AnalyseObjectListPlugin beforeDoOperation: ", (Throwable)exception);
        }
    }

    private void doOperation(BeforeDoOperationEventArgs args) {
        ListSelectedRowCollection selectedRows;
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if (HRStringUtils.equals((String)OP_CONFIG_DATA_PERM, (String)operateKey)) {
            FormShowParameter showParameter = new FormShowParameter();
            showParameter.setFormId("hrptmc_datapermrule");
            String name = ((ListView)this.getView()).getCurrentSelectedRowInfo().getName();
            showParameter.setCaption(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6570\u636e\u63a7\u6743\u89c4\u5219\u914d\u7f6e%s", (String)"AnalyseObjectListPlugin_2", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), "-" + name));
            showParameter.getOpenStyle().setShowType(ShowType.Modal);
            Long currentAnObjId = (Long)((ListView)this.getView()).getCurrentSelectedRowInfo().getPrimaryKeyValue();
            showParameter.setCustomParam("anObjId", (Object)currentAnObjId);
            this.getView().showForm(showParameter);
        } else if (HRStringUtils.equals((String)operateKey, (String)OP_COPY_AN_OBJ) && (selectedRows = ((IListView)this.getView()).getSelectedRows()).size() > 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u8981\u6267\u884c\u7684\u6570\u636e\u3002", (String)"AnalyseObjectListPlugin_6", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        try {
            this.doAfterOperation(args);
        }
        catch (Exception exception) {
            LOGGER.error("AnalyseObjectListPlugin afterDoOperation: ", (Throwable)exception);
        }
    }

    private void doAfterOperation(AfterDoOperationEventArgs args) {
        if (args.getSource() instanceof AbstractOperate) {
            AbstractOperate operate = (AbstractOperate)args.getSource();
            String operateKey = operate.getOperateKey();
            if ("exportconfig".equals(operateKey)) {
                ListView listView = (ListView)this.getView();
                List<Object> idList = listView.getSelectedRows().stream().map(ListSelectedRow::getPrimaryKeyValue).collect(Collectors.toList());
                this.exportConfig(idList);
            } else if ("importconfig".equals(operateKey)) {
                this.importConfigFile();
            } else if (HRStringUtils.equals((String)operateKey, (String)OP_COPY_AN_OBJ)) {
                BaseShowParameter showParameter = new BaseShowParameter();
                showParameter.setFormId("hrptmc_analyseobject");
                showParameter.setCaption(ResManager.loadKDString((String)"\u590d\u5236\u5206\u6790\u5bf9\u8c61", (String)"AnalyseObjectListPlugin_7", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                showParameter.setStatus(OperationStatus.ADDNEW);
                showParameter.setBillStatus(BillOperationStatus.ADDNEW);
                showParameter.setCustomParam("copyId", ((IListView)this.getView()).getSelectedRows().get(0).getPrimaryKeyValue());
                showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, OP_COPY_AN_OBJ));
                this.getView().showForm((FormShowParameter)showParameter);
            } else if (OP_IMPORT_BY_TEMP_LIB.equals(operateKey)) {
                FormShowParameter parameter = new FormShowParameter();
                parameter.setFormId("hrptmc_anobjtemplatef7");
                parameter.getOpenStyle().setShowType(ShowType.Modal);
                parameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CALL_BACK_KEY_CHOOSE_TEMPLATE));
                this.getView().showForm(parameter);
            } else if (HRStringUtils.equals((String)operateKey, (String)OP_DATA_EXTRACT)) {
                FormShowParameter parameter = new FormShowParameter();
                parameter.setFormId("hrptmc_dataextractconfig");
                parameter.getOpenStyle().setShowType(ShowType.Modal);
                parameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CALL_BACK_KEY_CHOOSE_EXTRACT));
                Object anObjId = ((IListView)this.getView()).getFocusRowPkId();
                parameter.setCustomParam("anObjId", anObjId);
                DynamicObject anObjDy = AnalyseObjectService.getInstance().getAnObjDy((Long)anObjId);
                parameter.setCustomParam("anObjName", (Object)anObjDy.getString("name"));
                parameter.setCustomParam("anObjNum", (Object)anObjDy.getString("number"));
                this.getView().showForm(parameter);
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        try {
            this.doCloseCallBack(event);
        }
        catch (Exception exception) {
            LOGGER.error("AnalyseObjectListPlugin closedCallBack: ", (Throwable)exception);
        }
    }

    private void doCloseCallBack(ClosedCallBackEvent event) {
        if (HRStringUtils.equals((String)event.getActionId(), (String)"report_conf_exp_closecallback")) {
            Map ret = (Map)event.getReturnData();
            if (ret != null) {
                String expStatus = (String)ret.get("exp_status");
                if ("exp_sucess".equals(expStatus)) {
                    this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5bfc\u51fa\u6210\u529f", (String)"AnalyseObjectListPlugin_4", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                } else if ("exp_fail".equals(expStatus)) {
                    String traceid = (String)ret.get("traceid");
                    this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5bfc\u51fa\u5931\u8d25\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u5206\u6790\u65e5\u5fd7\uff0ctraceid:%s\u3002", (String)"AnalyseObjectListPlugin_5", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), traceid));
                }
            }
        } else if (HRStringUtils.equals((String)event.getActionId(), (String)CALL_BACK_KEY_CHOOSE_TEMPLATE)) {
            String anObjIdStr = (String)event.getReturnData();
            if (HRStringUtils.isEmpty((String)anObjIdStr)) {
                return;
            }
            BaseShowParameter showParameter = new BaseShowParameter();
            showParameter.setFormId("hrptmc_analyseobject");
            showParameter.setCaption(ResManager.loadKDString((String)"\u5bfc\u5165\u5206\u6790\u5bf9\u8c61", (String)"AnalyseObjectListPlugin_8", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            showParameter.setStatus(OperationStatus.ADDNEW);
            showParameter.setBillStatus(BillOperationStatus.ADDNEW);
            showParameter.setCustomParam("copyId", (Object)Long.parseLong(anObjIdStr));
            showParameter.setCustomParam("isTemplateImport", (Object)"true");
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "newByTemplate"));
            this.getView().showForm((FormShowParameter)showParameter);
        } else if (HRStringUtils.equals((String)event.getActionId(), (String)OP_COPY_AN_OBJ) || HRStringUtils.equals((String)event.getActionId(), (String)"newByTemplate")) {
            ((IListView)this.getView()).refresh();
        }
    }

    private void importConfigFile() {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrptmc_configimportstart");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        Map customParams = showParameter.getCustomParams();
        customParams.put("formId", "hrptmc_analyseobject");
        String billFormId = ((ListView)this.getView()).getBillFormId();
        customParams.put("anObjIsTemplate", HRStringUtils.equals((String)billFormId, (String)"hrptmc_anobjtemplib"));
        customParams.put("entityName", StringUtils.isBlank((CharSequence)this.getView().getFormShowParameter().getCaption()) ? this.getView().getFormShowParameter().getFormConfig().getCaption().getLocaleValue() : this.getView().getFormShowParameter().getCaption());
        this.getView().showForm(showParameter);
    }

    private void exportConfig(List<Object> idList) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrptmc_configexportstart");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        Map customParams = showParameter.getCustomParams();
        customParams.put("formId", "hrptmc_analyseobject");
        customParams.put("exportAnObjPks", idList);
        customParams.put("entitynumber", "hrptmc_analyseobject");
        customParams.put("exp_excel_name", ResManager.loadKDString((String)"\u5206\u6790\u5bf9\u8c61\u5bfc\u51fa", (String)"AnalyseObjectListPlugin_3", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
        customParams.put("entityName", StringUtils.isBlank((CharSequence)this.getView().getFormShowParameter().getCaption()) ? this.getView().getFormShowParameter().getFormConfig().getCaption().getLocaleValue() : this.getView().getFormShowParameter().getCaption());
        customParams.put("taskClassName", "kd.hr.hrptmc.formplugin.web.exp.HReportConfExportTask");
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "report_conf_exp_closecallback"));
        this.getView().showForm(showParameter);
    }
}
