/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 */
package kd.hr.haos.common.constants.init;

import java.util.function.Supplier;
import kd.bos.dataentity.resource.ResManager;

public enum AdminOrgInitTipsEnum {
    ADMIN_ORG_BO_NOT_EXIST_ERR(() -> ResManager.loadKDString((String)"\u7ec4\u7ec7boid\u4e0d\u5b58\u5728\uff0c\u8bf7\u68c0\u67e5\uff01", (String)"AdminOrgInitTipsEnum_0", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_FIRST_START_DATE_ERR(() -> ResManager.loadKDString((String)"\u7ec4\u7ec7\uff1a{0}\u7684\u7b2c\u4e00\u4e2a\u6570\u636e\u7248\u672c\uff1a{1}\u751f\u6548\u65e5\u671f\uff1a{2}\u4e0e\u6700\u65e9\u751f\u6548\u65e5\u671f\uff1a{3}\u4e0d\u4e00\u81f4\uff0c\u8bf7\u68c0\u67e5\uff01", (String)"AdminOrgInitTipsEnum_1", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_END_DATE_ERR(() -> ResManager.loadKDString((String)"\u7ec4\u7ec7\uff1a{0}\u7684\u6700\u540e\u4e00\u4e2a\u6570\u636e\u7248\u672c\uff1a{1}\u7ed3\u675f\u65e5\u671f\u4e0d\u80fd\u4e3a\u7a7a\uff0c\u8bf7\u68c0\u67e5\uff01", (String)"AdminOrgInitTipsEnum_2", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_START_DATE_ERR(() -> ResManager.loadKDString((String)"%1$s\u540e\u884c\u653f\u7ec4\u7ec7\u5df2\u7ecf\u5728\u9875\u9762\u53d1\u751f\u4e86\u8c03\u6574\uff0c\u5386\u53f2\u7248\u672c\u7684\u5931\u6548\u65e5\u9700\u8981\u65e9\u4e8e%2$s\u3002", (String)"AdminOrgInitTipsEnum_3", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_EARLIEST_ERR(() -> ResManager.loadKDString((String)"\u7ec4\u7ec7\uff1a{0}\u7684\u6570\u636e\u7248\u672c\uff1a{1}\u7684\u751f\u6548\u65e5\u671f\uff1a{2}\u65e9\u4e8ebo\u7ec4\u7ec7\u6700\u65e9\u751f\u6548\u65e5\u671f\uff1a{3}\uff0c\u8bf7\u68c0\u67e5\uff01", (String)"AdminOrgInitTipsEnum_4", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_DISCONTINUOUS_ERR(() -> ResManager.loadKDString((String)"\u7ec4\u7ec7\uff1a{0}\u7684\u6570\u636e\u7248\u672c\uff1a{1}\u7684\u751f\u6548/\u5931\u6548\u65e5\u671f\uff1a{2}\u4e0e\u7248\u672c\uff1a{3}\u7684\u751f\u6548/\u5931\u6548\u65e5\u671f\uff1a{4}\u4e0d\u8fde\u7eed\uff0c\u8bf7\u68c0\u67e5\uff01", (String)"AdminOrgInitTipsEnum_5", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_BO_BIZ_NOT_EXIST_ERR(() -> ResManager.loadKDString((String)"\u7ec4\u7ec7\uff1a{0}\u7684\u4e1a\u52a1\u7248\u672c\u4e0d\u5b58\u5728\uff0c\u8bf7\u68c0\u67e5\uff01", (String)"AdminOrgInitTipsEnum_6", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_COMPANY_ERR(() -> ResManager.loadKDString((String)"\u7ec4\u7ec7\uff1a{0}\u7684\u6570\u636e\u7248\u672c\uff1a{1}\u7684\u884c\u653f\u7ec4\u7ec7\u6240\u5c5e\u516c\u53f8\uff1a{2}\u7684\u7c7b\u578b\u5f52\u5c5e\u4e0d\u4e3a\u96c6\u56e2\u6216\u516c\u53f8\uff0c\u8bf7\u68c0\u67e5\uff01", (String)"AdminOrgInitTipsEnum_7", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_PARENT_ERR(() -> ResManager.loadKDString((String)"\u5386\u53f2\u7248\u672c\u7684\u751f\u6548\u65e5\u671f%1$s\u8981\u665a\u4e8e\u6216\u7b49\u4e8e\u4e0a\u7ea7\u884c\u653f\u7ec4\u7ec7%2$s\u7684\u6700\u65e9\u751f\u6548\u65e5\u671f%3$s\u3002", (String)"AdminOrgInitTipsEnum_13", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_START_LESS_END_DATE_ERR(() -> ResManager.loadKDString((String)"\u751f\u6548\u65e5\u671f\u4e0d\u80fd\u665a\u4e8e\u5931\u6548\u65e5\u671f\u3002", (String)"AdminOrgInitTipsEnum_9", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_BO_ERR(() -> ResManager.loadKDString((String)"\u7ec4\u7ec7:{0}\u7684\u5176\u4ed6\u7248\u672c\u5b58\u5728\u9519\u8bef,\u8bf7\u68c0\u67e5!", (String)"AdminOrgInitTipsEnum_10", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_COMPANY_ERR(() -> ResManager.loadKDString((String)"\u6240\u5c5e\u516c\u53f8\u4e0d\u5b58\u5728,\u8bf7\u68c0\u67e5!", (String)"AdminOrgInitTipsEnum_11", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_FIRST_HIS_PARENT_ERR(() -> ResManager.loadKDString((String)"\u7b2c\u4e00\u4e2a\u5386\u53f2\u7248\u672c\u7ed3\u675f\u65e5\u671f%1$s\u8981\u665a\u4e8e\u6216\u7b49\u4e8e\u4e0a\u7ea7\u884c\u653f\u7ec4\u7ec7%2$s\u7684\u6700\u65e9\u751f\u6548\u65e5\u671f%3$s\u3002", (String)"AdminOrgInitTipsEnum_12", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_INIT_CURR_PARENT_ERR(() -> ResManager.loadKDString((String)"\u6700\u540e\u4e00\u4e2a\u5386\u53f2\u7248\u672c\u7684\u5931\u6548\u65e5%1$s\u671f\u9700\u8981\u665a\u4e8e\u6216\u7b49\u4e8e\u5f53\u524d\u7248\u672c\u7684\u4e0a\u7ea7\u884c\u653f\u7ec4\u7ec7%2$s\u6700\u65e9\u751f\u6548\u65e5\u671f%3$s\u3002", (String)"AdminOrgInitTipsEnum_14", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_VERIFY_HIS_MIGRATED(() -> ResManager.loadKDString((String)"\u884c\u653f\u7ec4\u7ec7%s\u5df2\u7ecf\u8fc1\u79fb\u8fc7\u5386\u53f2\u7248\u672c\uff0c\u4e0d\u652f\u6301\u518d\u6b21\u8fc1\u79fb\u6216\u8865\u5145\u8fc1\u79fb\u3002", (String)"AdminOrgInitTipsEnum_15", (String)"hrmp-haos-common", (Object[])new Object[0])),
    ADMIN_ORG_VERIFY_HIS_BO_EXISTED(() -> ResManager.loadKDString((String)"\u884c\u653f\u7ec4\u7ec7%s\u7684\u5f53\u524d\u7248\u672c\u8fd8\u6ca1\u6709\u8fc1\u79fb\u3002", (String)"AdminOrgInitTipsEnum_16", (String)"hrmp-haos-common", (Object[])new Object[0]));

    private Supplier<String> info;

    private AdminOrgInitTipsEnum(Supplier<String> info) {
        this.info = info;
    }

    public String getInfo() {
        return this.info.get();
    }
}
