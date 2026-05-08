/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgTeamCoopRelRepository
 *  kd.hr.haos.business.domain.org.helper.CoopRelHelper
 *  kd.hr.haos.business.domain.org.helper.OrgFullNameHelper
 *  kd.hr.haos.business.domain.org.helper.OrgUnitHelper
 *  kd.hr.haos.business.domain.org.helper.TimeLineHelper
 *  kd.hr.haos.business.domain.org.service.AdminChangeMsgService
 *  kd.hr.haos.business.domain.org.service.AdminOrgChangeLogService
 *  kd.hr.haos.business.domain.org.service.BatchProjectTeamNewOpService
 *  kd.hr.haos.business.domain.org.service.OrgChangeDetailService
 *  kd.hr.haos.business.domain.org.setter.AdminOrgAddSetter
 *  kd.hr.haos.business.domain.org.setter.OrgSetter
 *  kd.hr.haos.business.util.PropertyGetUtils
 *  kd.hr.haos.common.constants.masterdata.AdminOrgConstants
 *  kd.hr.haos.common.model.org.OrgTreeNode
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.haos.business.domain.org.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgTeamCoopRelRepository;
import kd.hr.haos.business.domain.org.helper.CoopRelHelper;
import kd.hr.haos.business.domain.org.helper.OrgFullNameHelper;
import kd.hr.haos.business.domain.org.helper.OrgUnitHelper;
import kd.hr.haos.business.domain.org.helper.TimeLineHelper;
import kd.hr.haos.business.domain.org.service.AdminChangeMsgService;
import kd.hr.haos.business.domain.org.service.AdminOrgChangeLogService;
import kd.hr.haos.business.domain.org.service.BatchProjectTeamNewOpService;
import kd.hr.haos.business.domain.org.service.OrgChangeDetailService;
import kd.hr.haos.business.domain.org.setter.AdminOrgAddSetter;
import kd.hr.haos.business.domain.org.setter.OrgSetter;
import kd.hr.haos.business.util.PropertyGetUtils;
import kd.hr.haos.common.constants.masterdata.AdminOrgConstants;
import kd.hr.haos.common.model.org.OrgTreeNode;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.util.HRStringUtils;

public class BatchAdminOrgNewOpService
extends BatchProjectTeamNewOpService {
    protected OrgChangeDetailService changeDetailService;
    protected List<Long> adminOrgAddOrgBoIds;

    public BatchAdminOrgNewOpService(DynamicObject[] data, boolean isSaveInputData, OperateOption operateOption) {
        this(data, isSaveInputData, operateOption, AdminOrgConstants.ADMINORG_STRUCT);
    }

    public BatchAdminOrgNewOpService(DynamicObject[] data, boolean isSaveInputData, OperateOption operateOption, Long structProjectId) {
        super(data, isSaveInputData, operateOption, structProjectId);
        for (DynamicObject orgDy : data) {
            DynamicObjectCollection structProjectEntity = orgDy.getDynamicObjectCollection("struct_project_entry");
            if (structProjectEntity.isEmpty()) continue;
            for (DynamicObject nowStructProjectEntry : structProjectEntity) {
                long entryStructProjectId = PropertyGetUtils.getDyBdPropId((DynamicObject)nowStructProjectEntry, (String)"struct_project");
                this.structProjectIds.add(entryStructProjectId);
            }
        }
        this.adminOrgAddOrgBoIds = Lists.newArrayListWithExpectedSize((int)(data.length * 2));
    }

    protected void execute() {
        super.execute();
        this.changeDetailService = new OrgChangeDetailService((Collection)this.orgBoIds);
        if (!this.isAddTempTag) {
            DynamicObject[] opData = this.getOpData();
            DynamicObject[] oldTeamCoopRelDys = OrgTeamCoopRelRepository.getInstance().queryNoTimeTeamCoopRel(new QFilter("org", "in", (Object)this.orgBoIds));
            if (oldTeamCoopRelDys != null && oldTeamCoopRelDys.length > 0) {
                ArrayList oldCoopRelIds = Lists.newArrayListWithExpectedSize((int)oldTeamCoopRelDys.length);
                for (DynamicObject sourceDy : oldTeamCoopRelDys) {
                    oldCoopRelIds.add(sourceDy.getLong("id"));
                }
                TimeLineHelper.deleteTimelineDys((String)"haos_orgteamcooprel", (List)oldCoopRelIds);
            }
            DynamicObject[] orgCoopRelDys = CoopRelHelper.buildOrgAddCoopRelDys((DynamicObject[])opData);
            TimeLineHelper.saveTimelineDys((String)"haos_orgteamcooprel", (DynamicObject[])orgCoopRelDys);
        }
    }

    public void endDoChangeOp() {
        super.endDoChangeOp();
        if (!this.isAddTempTag) {
            this.saveAdminOrgMsgDetail(this.getOpData());
            this.saveAdminorgChangeLog(this.getOpData());
        }
    }

    protected void saveOrgChangeDetail() {
        super.saveOrgChangeDetail();
        if (this.changeDetailService != null && !this.isAddTempTag) {
            this.changeDetailService.saveOrgChangeDetail(null, this.getOpData(), null);
        }
    }

    private void saveAdminOrgMsgDetail(DynamicObject[] opData) {
        ArrayList<DynamicObject> msgDetailList = new ArrayList<DynamicObject>(opData.length);
        AdminChangeMsgService adminChangeMsgService = new AdminChangeMsgService();
        for (DynamicObject dataEntity : opData) {
            if (HRStringUtils.equals((String)dataEntity.getString("datastatus"), (String)HisModelDataStatusEnum.TO_BE_EFFECT.getStatus())) continue;
            DynamicObject adminChangeMsgDy = adminChangeMsgService.assembleMsgDy(null, dataEntity, null);
            msgDetailList.add(adminChangeMsgDy);
        }
        adminChangeMsgService.saveAdminChangeMsg(msgDetailList);
    }

    private void saveAdminorgChangeLog(DynamicObject[] opData) {
        AdminOrgChangeLogService adminOrgChangeLogService = new AdminOrgChangeLogService();
        if (this.isFromFutureModifyOp || this.isFromFutureEffectOp) {
            adminOrgChangeLogService.updateAdminOrgChangeLog(opData, "", new HashMap());
        } else {
            adminOrgChangeLogService.addAdminOrgChangeLog(opData, new HashMap(), true);
        }
    }

    public void afterTransDoOp() {
        super.afterTransDoOp();
        if (!this.isAddTempTag) {
            this.dbQueryRecorder.mark("saveFullNameData");
            this.saveFullNameData();
            this.dbQueryRecorder.diff("saveFullNameData");
            ArrayList addOrg = Lists.newArrayListWithExpectedSize((int)this.getOpData().length);
            ArrayList updateOrg = Lists.newArrayListWithExpectedSize((int)this.getOpData().length);
            Set unitOrgExistIds = Sets.newHashSetWithExpectedSize((int)0);
            if (this.orgSetter instanceof AdminOrgAddSetter) {
                unitOrgExistIds = ((AdminOrgAddSetter)this.orgSetter).getUnitOrgExistIds();
            }
            for (DynamicObject dataEntity : this.getOpData()) {
                long orgId = dataEntity.getLong("id");
                if (unitOrgExistIds.contains(orgId)) {
                    updateOrg.add(dataEntity);
                    continue;
                }
                addOrg.add(dataEntity);
            }
            String realOpKey = this.operateOption.getVariableValue("adminorg_operation", "");
            if (!"rootreset".equals(realOpKey)) {
                if (!addOrg.isEmpty()) {
                    this.dbQueryRecorder.mark("syncOrgUnit");
                    OrgUnitHelper.syncOrgUnit((DynamicObject[])addOrg.toArray(new DynamicObject[0]));
                    this.dbQueryRecorder.diff("syncOrgUnit");
                }
                if (!updateOrg.isEmpty()) {
                    this.dbQueryRecorder.mark("syncOrgChgUnit");
                    OrgUnitHelper.syncOrgChgUnit((DynamicObject[])updateOrg.toArray(new DynamicObject[0]));
                    this.dbQueryRecorder.diff("syncOrgChgUnit");
                }
            }
        }
        if (this.operateOption.containsVariable("batchApply") && "batchApply".equals(this.operateOption.getVariableValue("batchApply")) || this.operateOption.containsVariable("importtype")) {
            return;
        }
        if (!this.isAddTempTag) {
            this.dbQueryRecorder.mark("handleChangeMsg");
            new AdminChangeMsgService().handleChangeMsg();
            this.dbQueryRecorder.diff("handleChangeMsg");
        }
    }

    protected OrgSetter getOrgSetter(DynamicObject[] data, OperateOption operateOption) {
        return new AdminOrgAddSetter(data, operateOption);
    }

    protected void beforeBuildStructDy(List<OrgTreeNode> orgNodes, Long boId, DynamicObject orgDy) {
        super.beforeBuildStructDy(orgNodes, boId, orgDy);
        DynamicObjectCollection structProjectEntity = orgDy.getDynamicObjectCollection("struct_project_entry");
        if (!structProjectEntity.isEmpty()) {
            for (DynamicObject nowStructProjectEntry : structProjectEntity) {
                long entryStructProjectId = PropertyGetUtils.getDyBdPropId((DynamicObject)nowStructProjectEntry, (String)"struct_project");
                long newStructParentId = PropertyGetUtils.getDyBdPropId((DynamicObject)nowStructProjectEntry, (String)"struct_parent_org");
                if (newStructParentId == 0L) continue;
                orgNodes.add(new OrgTreeNode(boId, Long.valueOf(newStructParentId), Long.valueOf(entryStructProjectId)));
            }
        }
    }

    protected void afterAddChangeOrgStructDy(DynamicObject structDy) {
        super.afterAddChangeOrgStructDy(structDy);
        long structProjectId = PropertyGetUtils.getDyBdPropId((DynamicObject)structDy, (String)"structproject");
        if (AdminOrgConstants.ADMINORG_STRUCT.equals(structProjectId)) {
            this.adminOrgAddOrgBoIds.add(PropertyGetUtils.getDyBdPropId((DynamicObject)structDy, (String)"adminorg"));
        }
    }

    protected void saveFullNameData() {
        if (!this.adminOrgAddOrgBoIds.isEmpty()) {
            OrgFullNameHelper.dispatchUpdateOrgFullNameJob(this.adminOrgAddOrgBoIds);
        }
    }
}
