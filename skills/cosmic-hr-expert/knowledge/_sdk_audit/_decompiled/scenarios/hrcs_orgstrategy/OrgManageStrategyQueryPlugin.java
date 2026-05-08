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
 *  kd.hr.hrcs.bussiness.strategy.impl.InitOrgStrategyServiceImpl
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
import kd.hr.hrcs.bussiness.strategy.impl.InitOrgStrategyServiceImpl;
import kd.hr.hrcs.formplugin.web.managestrategy.ManageStrategyQueryPlugin;
import org.apache.commons.lang3.StringUtils;

public final class OrgManageStrategyQueryPlugin
extends ManageStrategyQueryPlugin {
    public void registerListener(EventObject event) {
        super.registerListener(event);
        super.addItemClickListeners(new String[]{"initorgstrategy"});
    }

    public String getEntityName() {
        return "hrcs_orgstrategy";
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)"initorgstrategy", (CharSequence)formOperate.getOperateKey())) {
            StrategyServiceHelper.itemClickInitStrategy((IFormView)this.getView(), (IPageCache)this.getPageCache(), (InitStrategyService)new InitOrgStrategyServiceImpl());
        }
    }
}
