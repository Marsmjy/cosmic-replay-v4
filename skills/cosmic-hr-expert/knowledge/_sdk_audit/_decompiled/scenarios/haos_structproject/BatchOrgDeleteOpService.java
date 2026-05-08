/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository
 *  kd.hr.haos.business.domain.adminorg.util.AdminOrgHisDynKey
 *  kd.hr.haos.business.domain.org.abs.AbsBatchOrgOriBaseOpService
 *  kd.hr.haos.business.domain.org.helper.HisModelHelper
 *  kd.hr.haos.business.domain.org.helper.OrgStructHelper
 *  kd.hr.haos.business.domain.org.helper.TimeLineHelper
 *  kd.hr.haos.business.domain.org.setter.OrgSetter
 *  kd.hr.hbp.business.application.impl.history.HisModelAPIService
 *  kd.hr.hbp.common.model.history.param.HisDeleteParam
 */
package kd.hr.haos.business.domain.org.service;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository;
import kd.hr.haos.business.domain.adminorg.util.AdminOrgHisDynKey;
import kd.hr.haos.business.domain.org.abs.AbsBatchOrgOriBaseOpService;
import kd.hr.haos.business.domain.org.helper.HisModelHelper;
import kd.hr.haos.business.domain.org.helper.OrgStructHelper;
import kd.hr.haos.business.domain.org.helper.TimeLineHelper;
import kd.hr.haos.business.domain.org.setter.OrgSetter;
import kd.hr.hbp.business.application.impl.history.HisModelAPIService;
import kd.hr.hbp.common.model.history.param.HisDeleteParam;

public class BatchOrgDeleteOpService
extends AbsBatchOrgOriBaseOpService {
    protected final List<Long> orgBoIds;
    protected final boolean deleteChildren;

    public BatchOrgDeleteOpService(DynamicObject[] data, boolean isHandleInputData, OperateOption operateOption, Long structProjectId, boolean deleteChildren) {
        super(data, isHandleInputData, operateOption, structProjectId);
        this.orgBoIds = Lists.newArrayListWithExpectedSize((int)data.length);
        this.deleteChildren = deleteChildren;
    }

    protected void execute() {
        DynamicObject[] opData;
        for (DynamicObject opDy : opData = this.getOpData()) {
            Long deleteStructProjectId = this.structProjectId;
            if (deleteStructProjectId != null && 0L == deleteStructProjectId) {
                deleteStructProjectId = null;
            }
            Map structGroupByMap = OrgStructHelper.getRootAllChildStructGroupByMap((DynamicObject)opDy, (Long)deleteStructProjectId);
            long boId = opDy.getLong("boid");
            ArrayList oldStructIds = Lists.newArrayListWithExpectedSize((int)structGroupByMap.size());
            for (Map.Entry structEntry : structGroupByMap.entrySet()) {
                List structs = (List)structEntry.getValue();
                for (DynamicObject struct : structs) {
                    oldStructIds.add(struct.getLong("id"));
                }
                if (!this.deleteChildren) continue;
                this.orgBoIds.add((Long)structEntry.getKey());
            }
            if (!this.deleteChildren) {
                this.orgBoIds.add(boId);
            }
            TimeLineHelper.deleteTimelineDys((String)AdminOrgHisDynKey.ADMIN_STRUCT_KEY.getDynKey(), (List)oldStructIds);
        }
    }

    protected OrgSetter getOrgSetter(DynamicObject[] data, OperateOption operateOption) {
        return new OrgSetter();
    }

    protected void handleInputData() {
        DynamicObject[] deleteOrgDys;
        List deleteOrgData;
        if (this.isHandleInputData && !this.orgBoIds.isEmpty() && !(deleteOrgData = Arrays.stream(deleteOrgDys = OrgRepository.getInstance().loadOrgDys(this.orgBoIds, new QFilter("iscurrentversion", "=", (Object)"1"))).filter(org -> org != null && (!this.isAddTempTag || org.getBoolean("isvirtualorg"))).collect(Collectors.toList())).isEmpty()) {
            HisModelAPIService hisModelAPIService = new HisModelAPIService();
            HisDeleteParam hisDeleteParam = HisModelHelper.buildOneDeleteParam((String)AdminOrgHisDynKey.ADMIN_ORG_KEY.getDynKey(), deleteOrgData);
            hisModelAPIService.deleteBo(hisDeleteParam);
        }
    }
}
