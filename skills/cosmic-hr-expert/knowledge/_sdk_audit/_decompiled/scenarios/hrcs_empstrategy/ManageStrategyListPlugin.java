/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hrcs.formplugin.web.managestrategy.log;

import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import org.apache.commons.lang3.StringUtils;

public final class ManageStrategyListPlugin
extends HRDataBaseList {
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        String operKey = formOperate.getOperateKey();
        ListShowParameter listShowParameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (StringUtils.equals((CharSequence)"logview", (CharSequence)operKey)) {
            this.viewLog(listShowParameter.getBillFormId(), 0L);
        } else if (StringUtils.equals((CharSequence)"viewonelog", (CharSequence)operKey)) {
            ListSelectedRowCollection listSelectedData = args.getListSelectedData();
            ListSelectedRow listSelectedRow = listSelectedData.get(0);
            long primaryKeyValue = (Long)listSelectedRow.getPrimaryKeyValue();
            this.viewLog(listShowParameter.getBillFormId(), primaryKeyValue);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void viewLog(String billFormId, long primaryKeyValue) {
        ListShowParameter lsp = new ListShowParameter();
        lsp.setFormId("bos_list");
        lsp.setBillFormId("hbss_history_logview");
        ListFilterParameter listFilterParameter = new ListFilterParameter();
        QFilter bizobj = new QFilter("bizobj", "=", (Object)billFormId);
        if (primaryKeyValue != 0L) {
            QFilter qFilter = new QFilter("modifybillid", "in", (Object)new String[]{String.valueOf(primaryKeyValue), primaryKeyValue + "+", primaryKeyValue + "-"});
            bizobj.and(qFilter);
        }
        listFilterParameter.setFilter(bizobj);
        lsp.setListFilterParameter(listFilterParameter);
        lsp.setHasRight(true);
        String pageId = this.getView().getPageId() + "_" + primaryKeyValue;
        lsp.setPageId(pageId);
        lsp.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        lsp.setCustomParam("customLogDetailFormId", (Object)"hbss_strategy_logdetail");
        lsp.setCustomParam("appId", (Object)this.getView().getFormShowParameter().getAppId());
        lsp.setCustomParam("formId", (Object)((ListShowParameter)this.getView().getFormShowParameter()).getBillFormId());
        this.getView().showForm((FormShowParameter)lsp);
    }
}
