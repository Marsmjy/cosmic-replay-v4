/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.expt.common.plugin.AfterQueryDataEventArgs
 *  kd.hr.expt.common.plugin.BeforeWriteDataEventArgs
 *  kd.hr.expt.common.plugin.HRExportPlugin
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.haos.formplugin.web.export;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.expt.common.plugin.AfterQueryDataEventArgs;
import kd.hr.expt.common.plugin.BeforeWriteDataEventArgs;
import kd.hr.expt.common.plugin.HRExportPlugin;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper;
import kd.hr.hbp.common.util.HRStringUtils;

public class OrgBatchExportTplPlugin
implements HRExportPlugin {
    public void afterQueryData(AfterQueryDataEventArgs args) {
        super.afterQueryData(args);
        Set orgIdSet = args.getDataList().stream().map(dyn -> dyn.getLong("id")).collect(Collectors.toSet());
        DynamicObject[] coopeRels = AdminOrgDetailHelper.queryOrgTeamCoopeRel(orgIdSet);
        Map<Long, List<DynamicObject>> orgIdCoopRelMap = Arrays.stream(coopeRels).collect(Collectors.groupingBy(dyn -> dyn.getLong("org.id")));
        args.getDataList().forEach(dyn -> {
            long orgId = dyn.getLong("id");
            List coopRelList = (List)orgIdCoopRelMap.get(orgId);
            if (CollectionUtils.isEmpty((Collection)coopRelList)) {
                return;
            }
            DynamicObjectCollection coopRelCollection = dyn.getDynamicObjectCollection("cooprelentryentity");
            coopRelList.forEach(coopRel -> {
                DynamicObject coopRelEntry = coopRelCollection.addNew();
                coopRelEntry.set("coopreltype", coopRel.get("coopreltype"));
                coopRelEntry.set("cooporgteam", coopRel.get("cooporgteam"));
                coopRelEntry.set("id", null);
            });
        });
    }

    public void beforeWriteData(BeforeWriteDataEventArgs args) {
        super.beforeWriteData(args);
        args.getTplDataRows().forEach(row -> row.forEach((callIndex, callMap) -> {
            if (HRStringUtils.equals((String)"cooprelentryentity_id", (String)((String)callMap.get("key")))) {
                callMap.put("val", "");
            }
        }));
    }
}
