/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.plugin.AbstractListPlugin
 *  kd.bos.mvc.list.ListView
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrptmc.business.exportconfig.HRReportPreSQLHelper
 */
package kd.hr.hrptmc.formplugin.web.export.sql;

import java.util.ArrayList;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.mvc.list.ListView;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrptmc.business.exportconfig.HRReportPreSQLHelper;

@ExcludeFromJacocoGeneratedReport
public final class AnalyseObjectPreSqlListPlugin
extends HRDataBaseList {
    public void afterDoOperation(AfterDoOperationEventArgs args) {
        AbstractOperate operate;
        String operateKey;
        super.afterDoOperation(args);
        if (args.getSource() instanceof AbstractOperate && "exportconfigsql".equals(operateKey = (operate = (AbstractOperate)args.getSource()).getOperateKey())) {
            if (((ListView)this.getView()).getSelectedRows().size() != 1) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u884c\u3002", (String)"AnalyseObjectListPlugin_1", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                return;
            }
            Object currentAnObjId = ((ListView)this.getView()).getCurrentSelectedRowInfo().getPrimaryKeyValue();
            HRReportPreSQLHelper hrReportPreSQLHelper = new HRReportPreSQLHelper();
            ArrayList<Long> idList = new ArrayList<Long>(16);
            idList.add((Long)currentAnObjId);
            hrReportPreSQLHelper.generateHRAnalysisObjPreSQLFile((AbstractListPlugin)this, idList);
        }
    }
}
