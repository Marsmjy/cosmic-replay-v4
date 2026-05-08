/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hrmp.lcs.formplugin.web.basedata;

import kd.bos.dataentity.resource.ResManager;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public class CostBizTypeList
extends HRDataBaseList {
    public void initialize() {
        super.initialize();
        this.getView().getFormShowParameter().setCaption(ResManager.loadKDString((String)"\u4eba\u529b\u6210\u672c\u4e1a\u52a1\u7c7b\u578b", (String)"CostBizTypeList_1", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]));
    }
}
