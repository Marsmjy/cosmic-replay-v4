/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.permission.api.HasPermOrgResultImpl
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hrmp.hbjm.business.utils.PermHelper
 */
package kd.hrmp.hbjm.formplugin.web.basedata;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.permission.api.HasPermOrgResultImpl;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hrmp.hbjm.business.utils.PermHelper;

public final class JobBaseBuListPlugin
extends HRDataBaseList {
    private final Set<String> controlSet = Stream.of("hbjm_jobscmhr", "hbjm_jobfamilyhr", "hbjm_jobclasshr", "hbjm_jobseqhr", "bos_org", "hbjm_jobtype").collect(Collectors.collectingAndThen(Collectors.toCollection(ConcurrentHashMap::newKeySet), Collections::unmodifiableSet));
    private HasPermOrgResult hasPermOrgResult;
    public static final String ORG_PERM_CACHE_KEY_PRE = "org_perm_result";

    public Set<String> getFilterSchemaFieldSet() {
        return this.controlSet;
    }

    public String getAppId() {
        return this.getView().getFormShowParameter().getAppId();
    }

    public String getEntityName() {
        return ((ListView)this.getView()).getBillFormId();
    }

    public void filterContainerBeforeF7Select(BeforeFilterF7SelectEvent args) {
        HasPermOrgResult permOrgResult;
        super.filterContainerBeforeF7Select(args);
        Set<String> fieldSet = this.getFilterSchemaFieldSet();
        String entityName = args.getRefEntityId();
        if (fieldSet.contains(entityName) && !(permOrgResult = this.getPermOrgResult(entityName)).hasAllOrgPerm()) {
            QFilter baseDataFilter = PermHelper.getBaseDataFilter((String)entityName, (List)permOrgResult.getHasPermOrgs());
            args.getCustomQFilters().add(baseDataFilter);
        }
    }

    public HasPermOrgResult getPermOrgResult(String entityName) {
        if (HRStringUtils.equals((String)entityName, (String)"bos_org")) {
            entityName = this.getEntityName();
        }
        String fullCacheKey = entityName + ORG_PERM_CACHE_KEY_PRE;
        String orgPermString = this.getPageCache().get(fullCacheKey);
        if (orgPermString == null) {
            this.hasPermOrgResult = PermHelper.getHRPermOrg((String)this.getAppId(), (String)entityName);
            this.getPageCache().put(fullCacheKey, ((HasPermOrgResultImpl)this.hasPermOrgResult).toSerializeStr());
        } else {
            this.hasPermOrgResult = HasPermOrgResultImpl.fromSerializeStr((String)orgPermString);
            if (!this.hasPermOrgResult.hasAllOrgPerm()) {
                List hasPermOrgs = this.hasPermOrgResult.getHasPermOrgs();
                for (int i = 0; i < hasPermOrgs.size(); ++i) {
                    hasPermOrgs.set(i, Long.parseLong(hasPermOrgs.get(i) + ""));
                }
            }
        }
        return this.hasPermOrgResult;
    }
}
