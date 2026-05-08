/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.haos.business.domain.common.service.impl.HAOSBaseRepository
 *  kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository$StructProjectInstance
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.common.model.AuthorizedStructResult
 */
package kd.hr.haos.business.domain.structproject.service.impl;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.haos.business.domain.common.service.impl.HAOSBaseRepository;
import kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.common.model.AuthorizedStructResult;

/*
 * Exception performing whole class analysis ignored.
 */
public class StructProjectRepository
extends HAOSBaseRepository {
    public StructProjectRepository() {
        super("haos_structproject");
    }

    public static StructProjectRepository getInstance() {
        return StructProjectInstance.access$000();
    }

    public DynamicObject[] queryAllStructProject(String selectField) {
        QFilter filter = this.enabledFilter().or(this.disabledFilter());
        return this.serviceHelper.query(selectField, new QFilter[]{filter});
    }

    public List<Long> queryEnablingByIds(Set<Long> ids) {
        QFilter filter = new QFilter("id", "in", ids).and(new QFilter("enable", "=", (Object)"10"));
        return this.serviceHelper.queryTranPropList("id", new QFilter[]{filter}, "id", Long.class);
    }

    public DynamicObject[] queryStructProjectByOtClassify(String selectField, Collection<Long> otClassify) {
        QFilter filter = this.enabledFilter().or(this.disabledFilter());
        if (!CollectionUtils.isEmpty(otClassify)) {
            filter.and(new QFilter("otclassify", "in", otClassify));
        }
        return this.serviceHelper.query(selectField, new QFilter[]{filter});
    }

    public DynamicObject[] getUserStructProject(boolean showAllArea) {
        List<QFilter> userStructProjectFilter = this.createUserStructProjectFilter(showAllArea);
        String selectProperties = String.join((CharSequence)",", "name", "number");
        DynamicObject[] result = this.serviceHelper.query(selectProperties, userStructProjectFilter.toArray(new QFilter[0]), "issyspreset desc, number asc");
        LinkedList structProjects = Lists.newLinkedList();
        for (DynamicObject dy : result) {
            if (Objects.equals(dy.getPkValue(), 1010L)) {
                structProjects.addFirst(dy);
                continue;
            }
            structProjects.addLast(dy);
        }
        return structProjects.toArray(new DynamicObject[0]);
    }

    public List<QFilter> createUserStructProjectFilter(boolean showAllArea) {
        AuthorizedStructResult permResult;
        ArrayList filters = Lists.newArrayListWithExpectedSize((int)3);
        long currUserId = RequestContext.get().getCurrUserId();
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        if (showAllArea) {
            map.put("needToAllAreasStructProject", true);
        }
        if (!(permResult = (AuthorizedStructResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getUserStructProjectsF7", (Object[])new Object[]{currUserId, "217WYC/L9U7E", "haos_adminorgdetail", "47150e89000000ac", "boid", map})).isHasAllStruct()) {
            QFilter structProjectFilter = new QFilter("id", "in", (Object)permResult.getAuthorizedStructs());
            filters.add(structProjectFilter);
        }
        QFilter otClassifyFilter = new QFilter("otclassify", "=", (Object)1010L);
        QFilter enableFilter = this.enabledFilter().or(this.disabledFilter());
        filters.add(otClassifyFilter);
        filters.add(enableFilter);
        if (!showAllArea) {
            filters.add(new QFilter("istoallareas", "=", (Object)Boolean.TRUE));
        }
        return filters;
    }

    public List<QFilter> createUserStructProjectFilterNewChart(boolean showAllArea) {
        ArrayList filters = Lists.newArrayListWithExpectedSize((int)3);
        long currUserId = RequestContext.get().getCurrUserId();
        AuthorizedStructResult permResult = (AuthorizedStructResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getUserStructProjectsF7", (Object[])new Object[]{currUserId, "217WYC/L9U7E", "homs_orgchart_new", "47150e89000000ac", "boid", null});
        if (!permResult.isHasAllStruct()) {
            QFilter structProjectFilter = new QFilter("id", "in", (Object)permResult.getAuthorizedStructs());
            filters.add(structProjectFilter);
        }
        QFilter otClassifyFilter = new QFilter("otclassify", "=", (Object)1010L);
        QFilter enableFilter = this.enabledFilter().or(this.disabledFilter());
        filters.add(otClassifyFilter);
        filters.add(enableFilter);
        if (!showAllArea) {
            filters.add(new QFilter("istoallareas", "=", (Object)Boolean.TRUE));
        }
        return filters;
    }

    public DynamicObject[] getUserStructProjectNewChart(boolean showAllArea) {
        List<QFilter> userStructProjectFilter = this.createUserStructProjectFilterNewChart(showAllArea);
        String selectProperties = String.join((CharSequence)",", "name", "number");
        DynamicObject[] result = this.serviceHelper.query(selectProperties, userStructProjectFilter.toArray(new QFilter[0]), "issyspreset desc, number asc");
        LinkedList structProjects = Lists.newLinkedList();
        for (DynamicObject dy : result) {
            if (Objects.equals(dy.getPkValue(), 1010L)) {
                structProjects.addFirst(dy);
                continue;
            }
            structProjects.addLast(dy);
        }
        return structProjects.toArray(new DynamicObject[0]);
    }

    public DynamicObject queryOneByStructProjectId(String selectProperties, long structProjectId) {
        QFilter filter = new QFilter("id", "=", (Object)structProjectId);
        return this.serviceHelper.queryOne(selectProperties, filter.toArray());
    }

    public DynamicObject[] queryByStructProjectIds(String selectProperties, Set<Long> structProjectIds) {
        QFilter filter = new QFilter("id", "in", structProjectIds);
        return this.serviceHelper.query(selectProperties, filter.toArray());
    }

    public DynamicObject[] queryOriginalByNameAndOrgId(String selectField, Collection<String> names, Collection<Long> orgIds) {
        QFilter filter = new QFilter("name", "in", names);
        return this.serviceHelper.queryOriginalArray(selectField, filter.toArray());
    }

    public DynamicObject[] queryStructNullEffdt() {
        QFilter filter = new QFilter("effdt", "is null", null);
        return this.serviceHelper.loadDynamicObjectArray(filter.toArray());
    }

    public DynamicObject[] queryAllStructArrBySyncorg() {
        QFilter currentQf = new QFilter("enable", "=", (Object)"1");
        QFilter syncorgQf = new QFilter("issyncorg", "=", (Object)"1");
        return this.serviceHelper.query("id,rootorg.id,rootorg.structnumber,rootorg.sourcevid", new QFilter[]{syncorgQf, currentQf}, "number asc");
    }

    public DynamicObject[] queryAllStructArrBySyncorg(List<Long> structIds, String selectFields) {
        QFilter currentQf = new QFilter("enable", "=", (Object)"1");
        QFilter syncorgQf = new QFilter("issyncorg", "=", (Object)"1");
        QFilter idQf = new QFilter("id", "in", structIds);
        return this.serviceHelper.query(selectFields, new QFilter[]{syncorgQf, currentQf, idQf}, "number asc");
    }
}
