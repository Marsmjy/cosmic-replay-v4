/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.list.ListShowParameter
 *  kd.bos.permission.cache.util.PermCommonUtil
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.hbp.business.service.perm.HRAdminService
 *  kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin
 */
package kd.hr.hrcs.formplugin.web.perm.hradmin;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.FormShowParameter;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.list.ListShowParameter;
import kd.bos.permission.cache.util.PermCommonUtil;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.business.service.perm.HRAdminService;
import kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin;

public final class HRAdminStrictPlugin
extends HRDynamicFormBasePlugin {
    public void preOpenForm(PreOpenFormEventArgs args) {
        ListShowParameter lsp;
        super.preOpenForm(args);
        FormShowParameter fsp = args.getFormShowParameter();
        if (fsp instanceof ListShowParameter && (lsp = (ListShowParameter)fsp).isLookUp()) {
            return;
        }
        HRAdminStrictPlugin.showMesIfUserIsNotAdmin(args);
    }

    public static void showMesIfUserIsNotAdmin(PreOpenFormEventArgs e) {
        boolean isCosmic;
        long userId = RequestContext.get().getCurrUserId();
        boolean isAdmin = PermissionServiceHelper.isAdminUser((long)userId);
        if (!isAdmin & !(isCosmic = PermCommonUtil.isCosmicUser((Long)userId))) {
            e.setCancel(true);
            e.setCancelMessage(ResManager.loadKDString((String)"\u60a8\u65e0\u6cd5\u8bbf\u95ee\u8be5\u529f\u80fd\uff0c\u56e0\u4e3a\u60a8\u4e0d\u662fHR\u9886\u57df\u7ba1\u7406\u5458\u3002", (String)"HRAdminStrictPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        if (!HRAdminStrictPlugin.isHrAdmin()) {
            e.setCancel(true);
            e.setCancelMessage(ResManager.loadKDString((String)"\u60a8\u65e0\u6cd5\u8bbf\u95ee\u8be5\u529f\u80fd\uff0c\u56e0\u4e3a\u60a8\u4e0d\u662fHR\u9886\u57df\u7ba1\u7406\u5458\u3002", (String)"HRAdminStrictPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
    }

    public static boolean isHrAdmin() {
        return HRAdminService.isHrAdmin();
    }
}
