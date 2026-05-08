/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.servicehelper.basedata.BaseDataServiceHelper
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hrmp.lcs.formplugin.web.basedata;

import java.util.Collection;
import java.util.List;
import kd.bos.context.RequestContext;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.servicehelper.basedata.BaseDataServiceHelper;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public class CostAdaptionList
extends HRDataBaseList {
    public void filterContainerBeforeF7Select(BeforeFilterF7SelectEvent args) {
        QFilter baseDataFilter;
        List hasPermOrgs;
        long userId;
        HasPermOrgResult permOrgs;
        super.filterContainerBeforeF7Select(args);
        String fieldName = args.getFieldName();
        List filters = args.getQfilters();
        if (("coststru.id".equals(fieldName) || "coststru.name".equals(fieldName)) && (permOrgs = PermissionServiceHelper.getAllPermOrgs((long)(userId = RequestContext.get().getCurrUserId()), (String)"11", (String)"198IF7HLNV46", (String)"lcs_costadaption", (String)"47150e89000000ac")) != null && !permOrgs.hasAllOrgPerm() && CollectionUtils.isNotEmpty((Collection)(hasPermOrgs = permOrgs.getHasPermOrgs())) && (baseDataFilter = BaseDataServiceHelper.getBaseDataFilter((String)"lcs_coststru", (List)hasPermOrgs, (boolean)true)) != null) {
            filters.add(baseDataFilter);
        }
    }
}
