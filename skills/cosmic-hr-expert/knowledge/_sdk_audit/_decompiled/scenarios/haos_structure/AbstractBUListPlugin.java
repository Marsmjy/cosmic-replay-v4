/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.filter.FilterColumn
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.permission.api.HasPermOrgResultImpl
 *  kd.hr.haos.business.domain.common.service.impl.BaseDataHelper
 *  kd.hr.haos.business.util.OrgPermHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hr.haos.formplugin.web.adminorg.template;

import java.util.List;
import java.util.Set;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.entity.property.BasedataProp;
import kd.bos.filter.FilterColumn;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.permission.api.HasPermOrgResultImpl;
import kd.hr.haos.business.domain.common.service.impl.BaseDataHelper;
import kd.hr.haos.business.util.OrgPermHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public abstract class AbstractBUListPlugin
extends HRDataBaseList {
    private HasPermOrgResult hasPermOrgResult;
    public static final String ORG_PERM_CACHE_KEY = "org_perm_result";

    public abstract Set<String> getCtrlBUFieldSet();

    public HasPermOrgResult getPermOrgResult() {
        if (this.hasPermOrgResult == null) {
            String orgPermString = this.getPageCache().get(ORG_PERM_CACHE_KEY);
            if (orgPermString == null) {
                this.hasPermOrgResult = OrgPermHelper.getHRPermOrg((String)((ListView)this.getView()).getBillFormId());
                this.getPageCache().put(ORG_PERM_CACHE_KEY, ((HasPermOrgResultImpl)this.hasPermOrgResult).toSerializeStr());
            } else {
                this.hasPermOrgResult = HasPermOrgResultImpl.fromSerializeStr((String)orgPermString);
                if (!this.hasPermOrgResult.hasAllOrgPerm()) {
                    List hasPermOrgs = this.hasPermOrgResult.getHasPermOrgs();
                    for (int i = 0; i < hasPermOrgs.size(); ++i) {
                        hasPermOrgs.set(i, Long.parseLong(hasPermOrgs.get(i) + ""));
                    }
                }
            }
        }
        return this.hasPermOrgResult;
    }

    public void filterColumnSetFilter(SetFilterEvent args) {
        HasPermOrgResult permOrgResult;
        super.filterColumnSetFilter(args);
        String fieldName = args.getFieldName();
        if (this.isAddBUFilter(fieldName) && !(permOrgResult = this.getPermOrgResult()).hasAllOrgPerm()) {
            String fieldNameInMainEntity = fieldName.substring(0, fieldName.indexOf("."));
            DynamicProperty dynamicProperty = ((FilterColumn)args.getSource()).getEntityType().getProperty(fieldNameInMainEntity);
            String entityName = this.getBaseEntityName(fieldName, dynamicProperty);
            if (!HRStringUtils.isEmpty((String)entityName)) {
                QFilter baseDataFilter = BaseDataHelper.getAdminOrgBaseDataFilter((String)entityName, (List)permOrgResult.getHasPermOrgs());
                args.getCustomQFilters().add(baseDataFilter);
            }
        }
    }

    private String getBaseEntityName(String fieldName, DynamicProperty dynamicProperty) {
        String entityName = null;
        if (!(dynamicProperty instanceof BasedataProp)) {
            return entityName;
        }
        BasedataProp basedataProp = (BasedataProp)dynamicProperty;
        String[] array = fieldName.split("\\.");
        if (array.length == 2) {
            entityName = basedataProp.getBaseEntityId();
        } else {
            DynamicProperty subDp = basedataProp.getDynamicComplexPropertyType().getProperty(array[1]);
            if (subDp instanceof BasedataProp) {
                entityName = ((BasedataProp)subDp).getBaseEntityId();
            }
        }
        return entityName;
    }

    public void filterContainerBeforeF7Select(BeforeFilterF7SelectEvent args) {
        HasPermOrgResult permOrgResult;
        super.filterContainerBeforeF7Select(args);
        String fieldName = args.getFieldName();
        if (this.isAddBUFilter(fieldName) && !(permOrgResult = this.getPermOrgResult()).hasAllOrgPerm()) {
            String entityName = args.getRefEntityId();
            QFilter baseDataFilter = BaseDataHelper.getAdminOrgBaseDataFilter((String)entityName, (List)permOrgResult.getHasPermOrgs());
            args.getCustomQFilters().add(baseDataFilter);
        }
    }

    private boolean isAddBUFilter(String fieldName) {
        String bdName = !fieldName.contains(".") ? fieldName : fieldName.substring(0, fieldName.lastIndexOf("."));
        Set<String> ctrlBUFieldSet = this.getCtrlBUFieldSet();
        return !CollectionUtils.isEmpty(ctrlBUFieldSet) && ctrlBUFieldSet.contains(bdName);
    }

    public void filterContainerInit(FilterContainerInitArgs args) {
        List columnList = args.getCommonFilterColumns();
        for (FilterColumn filterColumn : columnList) {
            String fieldName = filterColumn.getFieldName();
            if (!HRStringUtils.equals((String)fieldName, (String)"org.name")) continue;
            filterColumn.setDefaultValue("");
            this.getPageCache().put("first", "false");
            return;
        }
    }
}
