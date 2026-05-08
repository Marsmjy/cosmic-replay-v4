/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.hbss.opplugin.web;

import com.google.common.collect.Lists;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

@ExcludeFromJacocoGeneratedReport
public final class HrParamsConfigOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        args.getFieldKeys().add("basedatafield");
        args.getFieldKeys().add("auditcheck");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new /* Unavailable Anonymous Inner Class!! */);
    }

    private boolean existAudiDatatByNumber(String number) {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(number);
        try {
            MainEntityType dataEntityType = MetadataServiceHelper.getDataEntityType((String)number);
            IDataEntityProperty property = dataEntityType.findProperty("status");
            if (property == null) {
                return false;
            }
            QFilter idFilter = new QFilter("status", "in", (Object)Lists.newArrayList((Object[])new String[]{"A", "B"}));
            DynamicObject[] dynamicObjects = serviceHelper.queryOriginalArray("id", new QFilter[]{idFilter});
            return dynamicObjects.length != 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    static /* synthetic */ boolean access$000(HrParamsConfigOp x0, String x1) {
        return x0.existAudiDatatByNumber(x1);
    }
}
