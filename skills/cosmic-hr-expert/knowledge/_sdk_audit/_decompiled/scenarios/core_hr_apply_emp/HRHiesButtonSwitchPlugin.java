/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.list.IListView
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.dao.MetaCategory
 *  kd.bos.metadata.dao.MetadataDao
 *  kd.bos.mvc.bill.BillView
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.cache.util.PermCommonUtil
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRQFilterHelper
 *  kd.hr.hbp.formplugin.web.util.perm.HRPermUtil
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hbp.formplugin.web.template;

import java.util.Collections;
import java.util.EventObject;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.IListView;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.dao.MetaCategory;
import kd.bos.metadata.dao.MetadataDao;
import kd.bos.mvc.bill.BillView;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.cache.util.PermCommonUtil;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRQFilterHelper;
import kd.hr.hbp.formplugin.web.util.perm.HRPermUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class HRHiesButtonSwitchPlugin
extends AbstractFormPlugin {
    private static final Log LOGGER = LogFactory.getLog(HRHiesButtonSwitchPlugin.class);
    private static final String ENABLE_STATUS_OLD = "1";

    public void afterBindData(EventObject arg) {
        Object[] switchRegistryDyns;
        IFormView view = this.getView();
        if (!(view instanceof ListView) && !(view instanceof BillView)) {
            LOGGER.warn("\u4e0d\u652f\u6301\u6b64\u79cd\u7c7b\u578b\u9875\u9762");
            return;
        }
        String billFormId = view instanceof ListView ? ((IListView)view).getBillFormId() : view.getEntityId();
        try {
            String id = MetadataDao.getIdByNumber((String)billFormId, (MetaCategory)MetaCategory.Form);
            HRBaseServiceHelper switchRegistryService = new HRBaseServiceHelper("hies_switchregistry");
            switchRegistryDyns = switchRegistryService.queryOriginalArray("optype,oldop,newop,permitem,enablestatus", new QFilter[]{HRQFilterHelper.buildEql((String)"entity", (Object)id)});
            if (ArrayUtils.isEmpty((Object[])switchRegistryDyns)) {
                return;
            }
        }
        catch (Exception ex) {
            LOGGER.warn("\u67e5\u8be2hr\u5bfc\u5165\u5bfc\u51fa\u5207\u6362\u65b9\u6848\u5931\u8d25 errmsg:{}", (Object)ex.getMessage(), (Object)ex);
            return;
        }
        boolean enableNoPermBtnHide = PermCommonUtil.isEnableNoPermBtnHide();
        LOGGER.info("billFormId:{} enableNoPermBtnHide:{}", (Object)billFormId, (Object)enableNoPermBtnHide);
        for (Object switchRegistryDyn : switchRegistryDyns) {
            String opType = switchRegistryDyn.getString("optype");
            String oldOp = switchRegistryDyn.getString("oldop");
            String newOp = switchRegistryDyn.getString("newop");
            String permItem = switchRegistryDyn.getString("permitem");
            if (StringUtils.isBlank((CharSequence)permItem)) {
                if ("expt".equals(opType)) {
                    permItem = "4730fc9f000004ae";
                } else if ("impt".equals(opType)) {
                    permItem = "4730fc9f000003ae";
                }
            }
            if (enableNoPermBtnHide) {
                String appId = HRPermUtil.getAppIdFromShowParam((FormShowParameter)view.getFormShowParameter());
                long currUserId = RequestContext.get().getCurrUserId();
                boolean isPerm = PermissionServiceHelper.checkPermission((Long)currUserId, (String)appId, (String)billFormId, (String)permItem);
                LOGGER.info("currUserId:{} appId:{} billFormId:{} permItem:{}", new Object[]{currUserId, appId, billFormId, permItem});
                if (isPerm) {
                    this.switchButton(view, billFormId, (DynamicObject)switchRegistryDyn, oldOp, newOp);
                    continue;
                }
                this.hiddenButton(view, newOp);
                this.hiddenButton(view, oldOp);
                continue;
            }
            this.switchButton(view, billFormId, (DynamicObject)switchRegistryDyn, oldOp, newOp);
        }
    }

    private void switchButton(IFormView view, String billFormId, DynamicObject switchRegistryDyn, String oldOp, String newOp) {
        String enableStatus = switchRegistryDyn.getString("enablestatus");
        if (ENABLE_STATUS_OLD.equals(enableStatus)) {
            this.hiddenButton(view, newOp);
            this.showButton(view, oldOp);
        } else {
            this.hiddenButton(view, oldOp);
            this.showButton(view, newOp);
        }
    }

    private void showButton(IFormView view, String buttonKeys) {
        String[] split;
        if (StringUtils.isBlank((CharSequence)buttonKeys)) {
            LOGGER.warn("buttonKeys is null");
            return;
        }
        LOGGER.info("buttonKeys={}", (Object)buttonKeys);
        for (String buttonKey : split = buttonKeys.split(",")) {
            if (StringUtils.isBlank((CharSequence)buttonKey)) continue;
            view.setVisible(Boolean.TRUE, new String[]{buttonKey});
            view.updateControlMetadata(buttonKey, Collections.singletonMap("vi", 63));
        }
    }

    private void hiddenButton(IFormView view, String buttonKeys) {
        String[] split;
        if (StringUtils.isBlank((CharSequence)buttonKeys)) {
            LOGGER.warn("buttonKeys is null");
            return;
        }
        LOGGER.info("buttonKeys={}", (Object)buttonKeys);
        for (String buttonKey : split = buttonKeys.split(",")) {
            if (StringUtils.isBlank((CharSequence)buttonKey)) continue;
            view.setVisible(Boolean.FALSE, new String[]{buttonKey});
            view.updateControlMetadata(buttonKey, Collections.singletonMap("vi", 0));
        }
    }
}
