/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.events.BeforeDoCheckDataPermissionArgs
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectListener
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.formplugin.web.HRCoreBaseList
 */
package kd.hr.hbp.formplugin.web;

import kd.bos.form.events.BeforeDoCheckDataPermissionArgs;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectListener;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRCoreBaseList;

public final class HRPermCommonList
extends HRCoreBaseList
implements BeforeFilterF7SelectListener {
    public void beforeCheckDataPermission(BeforeDoCheckDataPermissionArgs args) {
        super.beforeCheckDataPermission(args);
    }

    public void initialize() {
        super.initialize();
    }

    public void beforeF7Select(BeforeFilterF7SelectEvent evt) {
    }

    protected String getAdminOrgFilterField() {
        return null;
    }

    protected boolean isAdminOrgFilterEnable() {
        return true;
    }

    protected String getBUFilterEntityName() {
        return null;
    }

    protected String getBUFilterField() {
        return null;
    }

    protected String getEmpgrpFilterField() {
        return null;
    }

    protected String getBUFilterAppId() {
        return null;
    }

    protected QFilter getCustomFilter() {
        return null;
    }

    protected boolean isCheckView() {
        return true;
    }

    protected boolean isCheckModify() {
        return true;
    }
}
