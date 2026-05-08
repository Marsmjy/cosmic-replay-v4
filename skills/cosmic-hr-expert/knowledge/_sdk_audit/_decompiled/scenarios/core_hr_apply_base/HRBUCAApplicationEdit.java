/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.property.OrgProp
 *  kd.bos.form.field.OrgEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.hbp.formplugin.web.hrbu;

import java.util.EventObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.property.OrgProp;
import kd.bos.form.field.OrgEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class HRBUCAApplicationEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        MainEntityType dataEntityType = this.getView().getModel().getDataEntityType();
        DataEntityPropertyCollection properties = dataEntityType.getProperties();
        properties.forEach(entityProperty -> {
            if (!(entityProperty instanceof OrgProp)) {
                return;
            }
            OrgProp orgProp = (OrgProp)entityProperty;
            if (!"11".equals(orgProp.getOrgFunc())) {
                return;
            }
            OrgEdit orgEdit = (OrgEdit)this.getControl(orgProp.getName());
            if (orgEdit != null) {
                HRBUCAApplicationEdit.bindHRBUBeforeF7SelectListener(eventObject, orgEdit, orgProp);
            }
        });
    }

    private static void bindHRBUBeforeF7SelectListener(EventObject eventObject, OrgEdit orgEdit, OrgProp orgProp) {
        orgEdit.addBeforeF7SelectListener((BeforeF7SelectListener)new /* Unavailable Anonymous Inner Class!! */);
    }

    private static DynamicObject getHrBuCaDyByAppNumber(String appNumber) {
        HRBaseServiceHelper hbssAppBusinessType = new HRBaseServiceHelper("hbss_appbusinesstype");
        QFilter appQFliter = new QFilter("app.number", "=", (Object)appNumber);
        QFilter typeEnableQFliter = new QFilter("enable", "=", (Object)"1");
        DynamicObject dynamicObject = hbssAppBusinessType.queryOne("businesstype", new QFilter[]{appQFliter.and(typeEnableQFliter)});
        if (dynamicObject == null) {
            return null;
        }
        long controlFunTypeId = dynamicObject.getLong("businesstype.controlfuntype.id");
        HRBaseServiceHelper hrBuCaHelper = new HRBaseServiceHelper("hbss_hrbuca");
        QFilter hrbuCaFuncQFliter = new QFilter("hrbucafunc", "=", (Object)controlFunTypeId);
        QFilter hrbuTypeEnableQFliter = new QFilter("enable", "=", (Object)"1");
        return hrBuCaHelper.queryOriginalOne("id,number", new QFilter[]{hrbuCaFuncQFliter.and(hrbuTypeEnableQFliter)});
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
    }

    static /* synthetic */ DynamicObject access$000(String x0) {
        return HRBUCAApplicationEdit.getHrBuCaDyByAppNumber(x0);
    }
}
