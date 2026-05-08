/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.control.AttachmentPanel
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.hr.hbp.common.cache.HRPageCache
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hrmp.hbjm.formplugin.web.basedata;

import java.util.EventObject;
import java.util.List;
import java.util.Map;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.control.AttachmentPanel;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.hr.hbp.common.cache.HRPageCache;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class JobHisBasedataFiledChangeEdit
extends HRDataBaseEdit {
    public void afterBindData(EventObject e) {
        String pageStatus = this.getPageCache().get("pageStatus");
        if ("edit".equals(pageStatus)) {
            this.cacheAttachmentData();
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String opKey;
        super.beforeDoOperation(args);
        AbstractOperate operate = (AbstractOperate)args.getSource();
        switch (opKey = operate.getOperateKey()) {
            case "save": 
            case "bar_confirmchange": 
            case "confirmchangenoaudit": {
                String confirmSaveShow = this.getPageCache().get("confirmShow");
                String deleteEntryFlag = this.getView().getPageCache().get("deleteentry");
                if (HRStringUtils.equals((String)confirmSaveShow, (String)"1") || this.getModel().getDataChanged() || HRStringUtils.equals((String)deleteEntryFlag, (String)"true") || this.isAttachmentChange()) break;
                this.getPageCache().put("confirmShow", "1");
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u65e0\u4fe1\u606f\u53d8\u66f4\uff0c\u8bf7\u786e\u8ba4\u3002", (String)"JobHisBasedataEdit_0", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        String operateKey = afterDoOperationEventArgs.getOperateKey();
        if (afterDoOperationEventArgs.getOperationResult() != null && afterDoOperationEventArgs.getOperationResult().isSuccess() && HRStringUtils.equals((String)operateKey, (String)"deleteentry")) {
            this.getView().getPageCache().put("deleteentry", "true");
        }
    }

    private void cacheAttachmentData() {
        AttachmentPanel attachmentpanel = (AttachmentPanel)this.getView().getControl("attachmentpanelap");
        if (attachmentpanel != null) {
            List attachmentData = attachmentpanel.getAttachmentData();
            HRPageCache pageCache = new HRPageCache(this.getPageCache());
            pageCache.put("attachmentpanelap", (Object)attachmentData);
        }
    }

    private boolean isAttachmentChange() {
        AttachmentPanel attachmentpanel = (AttachmentPanel)this.getView().getControl("attachmentpanelap");
        if (attachmentpanel != null) {
            List attachmentData = attachmentpanel.getAttachmentData();
            HRPageCache pageCache = new HRPageCache(this.getPageCache());
            List oldAttachmentData = (List)pageCache.get("attachmentpanelap", List.class);
            return JobHisBasedataFiledChangeEdit.isAttachmentChange(attachmentData, oldAttachmentData);
        }
        return false;
    }

    public static boolean isAttachmentChange(List<Map<String, Object>> attachmentData, List<Map<String, Object>> oldAttachmentData) {
        if (attachmentData.size() != oldAttachmentData.size()) {
            return true;
        }
        int size = attachmentData.size();
        for (int i = 0; i < size; ++i) {
            Map<String, Object> oldAttachmentDataMap;
            Map<String, Object> newAttachmentDataMap = attachmentData.get(i);
            if (JobHisBasedataFiledChangeEdit.attachmentDataMapQuals(newAttachmentDataMap, oldAttachmentDataMap = oldAttachmentData.get(i))) continue;
            return true;
        }
        return false;
    }

    private static boolean attachmentDataMapQuals(Map<String, Object> newAttachmentDataMap, Map<String, Object> oldAttachmentDataMap) {
        if (newAttachmentDataMap.size() != oldAttachmentDataMap.size()) {
            return false;
        }
        return newAttachmentDataMap.get("uid").equals(oldAttachmentDataMap.get("uid")) && newAttachmentDataMap.get("name").equals(oldAttachmentDataMap.get("name")) && newAttachmentDataMap.get("description").equals(oldAttachmentDataMap.get("description"));
    }
}
