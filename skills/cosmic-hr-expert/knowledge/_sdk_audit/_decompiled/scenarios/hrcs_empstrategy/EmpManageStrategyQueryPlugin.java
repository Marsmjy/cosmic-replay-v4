/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.FormOperate
 *  kd.hr.hrcs.bussiness.service.StrategyServiceHelper
 *  kd.hr.hrcs.bussiness.strategy.InitStrategyService
 *  kd.hr.hrcs.bussiness.strategy.impl.InitEmpStrategyServiceImpl
 *  kd.hr.hrcs.formplugin.web.managestrategy.ManageStrategyQueryPlugin
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hrcs.formplugin.web.managestrategy;

import java.util.EventObject;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.hr.hrcs.bussiness.service.StrategyServiceHelper;
import kd.hr.hrcs.bussiness.strategy.InitStrategyService;
import kd.hr.hrcs.bussiness.strategy.impl.InitEmpStrategyServiceImpl;
import kd.hr.hrcs.formplugin.web.managestrategy.ManageStrategyQueryPlugin;
import org.apache.commons.lang3.StringUtils;

public final class EmpManageStrategyQueryPlugin
extends ManageStrategyQueryPlugin {
    public void registerListener(EventObject e) {
        super.registerListener(e);
        super.addItemClickListeners(new String[]{"initempstrategy"});
    }

    public String getEntityName() {
        return "hrcs_empstrategy";
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)"initempstrategy", (CharSequence)formOperate.getOperateKey())) {
            StrategyServiceHelper.itemClickInitStrategy((IFormView)this.getView(), (IPageCache)this.getPageCache(), (InitStrategyService)new InitEmpStrategyServiceImpl());
        }
    }
}
