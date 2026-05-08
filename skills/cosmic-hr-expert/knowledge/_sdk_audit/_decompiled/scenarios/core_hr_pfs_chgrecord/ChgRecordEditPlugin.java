/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.orm.util.StringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.hpfs.formplugin.chgrecord;

import java.util.EventObject;
import java.util.Locale;
import java.util.Objects;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.orm.util.StringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class ChgRecordEditPlugin
extends HRDataBaseEdit {
    public void afterBindData(EventObject e) {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        if (Objects.nonNull(dataEntity) && !StringUtils.isEmpty((Object)dataEntity.getString("billno"))) {
            String billTitle = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55-%s", (String)"ChgRecordEditPlugin_0", (String)"hr-hpfs-formplugin", (Object[])new Object[0]), dataEntity.getString("billno"));
            this.getView().setFormTitle(new LocaleString(billTitle));
        }
    }
}
