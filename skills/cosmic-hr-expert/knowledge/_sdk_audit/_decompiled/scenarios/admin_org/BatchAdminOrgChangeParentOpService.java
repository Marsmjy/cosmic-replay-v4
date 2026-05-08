/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgStructRepository
 *  kd.hr.haos.business.domain.adminorg.util.AdminOrgHisDynKey
 *  kd.hr.haos.business.domain.org.async.AsyncEffectingContext
 *  kd.hr.haos.business.domain.org.async.AsyncEffectingMultiCaster
 *  kd.hr.haos.business.domain.org.async.event.AsyncEffectingEvent$AsyncEffectingSaveAdminEvent
 *  kd.hr.haos.business.domain.org.helper.OrgStructHelper
 *  kd.hr.haos.business.domain.org.helper.TimeLineHelper
 *  kd.hr.haos.business.domain.org.service.AdminChangeMsgService
 *  kd.hr.haos.business.domain.org.service.AdminOrgChangeLogService
 *  kd.hr.haos.business.domain.org.service.BatchAdminOrgUpdateOpService
 *  kd.hr.haos.business.util.PropertyGetUtils
 *  kd.hr.haos.common.constants.masterdata.AdminOrgConstants
 *  kd.hr.haos.common.model.org.OrgTreeNode
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.haos.business.domain.org.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgStructRepository;
import kd.hr.haos.business.domain.adminorg.util.AdminOrgHisDynKey;
import kd.hr.haos.business.domain.org.async.AsyncEffectingContext;
import kd.hr.haos.business.domain.org.async.AsyncEffectingMultiCaster;
import kd.hr.haos.business.domain.org.async.event.AsyncEffectingEvent;
import kd.hr.haos.business.domain.org.helper.OrgStructHelper;
import kd.hr.haos.business.domain.org.helper.TimeLineHelper;
import kd.hr.haos.business.domain.org.service.AdminChangeMsgService;
import kd.hr.haos.business.domain.org.service.AdminOrgChangeLogService;
import kd.hr.haos.business.domain.org.service.BatchAdminOrgUpdateOpService;
import kd.hr.haos.business.util.PropertyGetUtils;
import kd.hr.haos.common.constants.masterdata.AdminOrgConstants;
import kd.hr.haos.common.model.org.OrgTreeNode;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;

public class BatchAdminOrgChangeParentOpService
extends BatchAdminOrgUpdateOpService {
    private static final Log LOG = LogFactory.getLog(BatchAdminOrgChangeParentOpService.class);
    protected final Map<Long, List<DynamicObject>> oldOrgId2structDynMap;
    protected final Map<Long, Map<Long, Long>> oldOrgId2structId2ParentIdMap;
    protected final Map<Long, DynamicObject> opDataMap;
    protected final List<OrgTreeNode> changeOrgStructList;
    protected final List<OrgTreeNode> addOrgStructList;
    protected final List<OrgTreeNode> deleteOrgStructList;
    private final List<DynamicObject> adminOrgStructDys = Lists.newArrayListWithExpectedSize((int)10);
    private final List<DynamicObject> orgStructDys = Lists.newArrayListWithExpectedSize((int)10);

    public BatchAdminOrgChangeParentOpService(DynamicObject[] data, OperateOption operateOption) {
        this(data, true, operateOption);
    }

    public BatchAdminOrgChangeParentOpService(DynamicObject[] data, boolean isSaveInputData, OperateOption operateOption) {
        this(data, null, isSaveInputData, operateOption);
    }

    public BatchAdminOrgChangeParentOpService(DynamicObject[] data, boolean isSaveInputData, OperateOption operateOption, Long structProjectId) {
        this(data, null, isSaveInputData, operateOption, structProjectId);
    }

    public BatchAdminOrgChangeParentOpService(DynamicObject[] data, AsyncEffectingMultiCaster asyncCaster, boolean isSaveInputData, OperateOption operateOption) {
        this(data, asyncCaster, isSaveInputData, operateOption, AdminOrgConstants.ADMINORG_STRUCT);
    }

    public BatchAdminOrgChangeParentOpService(DynamicObject[] data, AsyncEffectingMultiCaster asyncCaster, boolean isSaveInputData, OperateOption operateOption, Long structProjectId) {
        super(data, asyncCaster, isSaveInputData, operateOption, structProjectId);
        this.oldOrgId2structDynMap = Maps.newHashMapWithExpectedSize((int)data.length);
        this.opDataMap = Maps.newHashMapWithExpectedSize((int)data.length);
        this.changeOrgStructList = Lists.newArrayListWithExpectedSize((int)data.length);
        this.addOrgStructList = Lists.newArrayListWithExpectedSize((int)data.length);
        this.deleteOrgStructList = Lists.newArrayListWithExpectedSize((int)data.length);
        this.oldOrgId2structId2ParentIdMap = Maps.newHashMapWithExpectedSize((int)data.length);
    }

    public void beforeTransDoOp() {
        super.beforeTransDoOp();
        DynamicObject[] opData = this.getOpData();
        HashSet structIds = Sets.newHashSetWithExpectedSize((int)opData.length);
        for (DynamicObject orgDy : opData) {
            DynamicObjectCollection structEntry = orgDy.getDynamicObjectCollection("struct_project_entry");
            for (DynamicObject nowStructProjectEntry : structEntry) {
                long entryStructProjectId = PropertyGetUtils.getDyBdPropId((DynamicObject)nowStructProjectEntry, (String)"struct_project");
                structIds.add(entryStructProjectId);
            }
        }
        if (this.structProjectId != null) {
            structIds.add(this.structProjectId);
        }
        LOG.info("BatchAdminOrgChangeParentOpService_structProjectId_{}", (Object)this.structProjectId);
        LOG.info("BatchAdminOrgChangeParentOpService_queryNoTimeStructDys_structIds_{}", (Object)structIds);
        LOG.info("BatchAdminOrgChangeParentOpService_queryNoTimeStructDys_orgBoIds_{}", this.oldOrgMap.keySet());
        DynamicObject[] allStructDys = OrgStructRepository.getInstance().queryNoTimeStructDys((Collection)structIds, new QFilter("adminorg", "in", this.oldOrgMap.keySet()));
        LOG.info("BatchAdminOrgChangeParentOpService_queryNoTimeStructDys_allStructDys_size_{}", (Object)allStructDys.length);
        Date now = HRDateTimeUtils.getNowDate();
        for (DynamicObject structDy : allStructDys) {
            long orgId = PropertyGetUtils.getDyBdPropId((DynamicObject)structDy, (String)"adminorg.id");
            DynamicObject oldAdminorg = (DynamicObject)this.oldOrgMap.get(orgId);
            Date compareDate = now;
            if (oldAdminorg != null) {
                compareDate = HRDateTimeUtils.truncateDate((Date)oldAdminorg.getDate("bsled"));
            }
            Date structStartDate = structDy.getDate("startdate");
            Date structEndDate = structDy.getDate("enddate");
            if (HRDateTimeUtils.dayAfter((Date)structStartDate, (Date)compareDate) || HRDateTimeUtils.dayBefore((Date)structEndDate, (Date)compareDate)) continue;
            ArrayList structList = this.oldOrgId2structDynMap.get(orgId);
            if (structList == null) {
                structList = Lists.newArrayListWithExpectedSize((int)opData.length);
            }
            structList.add((DynamicObject)structDy);
            this.oldOrgId2structDynMap.put(orgId, structList);
            Map<Long, Long> structId2ParentIdMap = this.oldOrgId2structId2ParentIdMap.get(orgId);
            if (structId2ParentIdMap == null) {
                structId2ParentIdMap = new HashMap<Long, Long>();
            }
            structId2ParentIdMap.put(PropertyGetUtils.getDyBdPropId((DynamicObject)structDy, (String)"structproject.id"), PropertyGetUtils.getDyBdPropId((DynamicObject)structDy, (String)"parentorg.id"));
            this.oldOrgId2structId2ParentIdMap.put(orgId, structId2ParentIdMap);
        }
        this.initListData();
    }

    protected void execute() {
        this.asyncLog(AsyncEffectingEvent.AsyncEffectingSaveAdminEvent.class, AsyncEffectingContext::resetToStartSecondStep);
        this.handleAddStruct();
        this.handleChangeStruct();
        this.handleDeleteStruct();
        if (!this.orgStructDys.isEmpty()) {
            HashMap newOrgId2structDynMap = Maps.newHashMapWithExpectedSize((int)this.orgStructDys.size());
            for (DynamicObject structDy : this.orgStructDys) {
                long orgId = PropertyGetUtils.getDyBdPropId((DynamicObject)structDy, (String)"adminorg");
                List structList = (List)newOrgId2structDynMap.get(orgId);
                if (structList == null) {
                    structList = Lists.newArrayListWithExpectedSize((int)this.orgStructDys.size());
                }
                structList.add(structDy);
                newOrgId2structDynMap.put(orgId, structList);
            }
            if (this.changeDetailService != null) {
                this.changeDetailService.setOldOrgId2structDynMap(this.oldOrgId2structDynMap);
                this.changeDetailService.setNewOrgId2structDynMap((Map)newOrgId2structDynMap);
            }
        }
        this.asyncLog(AsyncEffectingEvent.AsyncEffectingSaveAdminEvent.class, AsyncEffectingContext::resetToEndSecondStep);
        super.execute();
    }

    public void endDoChangeOp() {
        super.endDoChangeOp();
        this.resetStructOrgFields(this.adminOrgStructDys);
        if (!this.isAddTempTag) {
            new AdminChangeMsgService().saveChgAdminOrgMsgDetail(this.getOpData(), this.oldOrgMap);
            this.saveAdminorgChangeLog(this.getOpData());
        }
    }

    private void saveAdminorgChangeLog(DynamicObject[] opData) {
        AdminOrgChangeLogService adminOrgChangeLogService = new AdminOrgChangeLogService();
        if (this.isFromFutureModifyOp || this.isFromFutureEffectOp) {
            adminOrgChangeLogService.updateAdminOrgChangeLog(opData, "", new HashMap());
        } else {
            adminOrgChangeLogService.addAdminOrgChangeLog(opData, this.oldOrgMap, false);
        }
    }

    public void afterTransDoOp() {
        super.afterTransDoOp();
        if (!this.isAddTempTag) {
            if (this.operateOption.containsVariable("batchApply") && "batchApply".equals(this.operateOption.getVariableValue("batchApply")) || this.operateOption.containsVariable("importtype")) {
                return;
            }
            if (this.operateOption.containsVariable("adminorg_operation") && ("disableorg".equals(this.operateOption.getVariableValue("adminorg_operation")) || "enableorg".equals(this.operateOption.getVariableValue("adminorg_operation")))) {
                return;
            }
            new AdminChangeMsgService().handleChangeMsg();
        }
    }

    protected void initListData() {
        DynamicObject[] opData;
        for (DynamicObject orgDy : opData = this.getOpData()) {
            long orgId = orgDy.getLong("boid");
            this.opDataMap.put(orgId, orgDy);
            DynamicObject dbOrgDy = (DynamicObject)this.oldOrgMap.get(orgId);
            if (dbOrgDy == null) continue;
            LOG.info("BatchAdminOrgChangeParentOpService_initListData_orgId_{}", (Object)orgId);
            long newParentOrgId = PropertyGetUtils.getDyBdPropId((DynamicObject)orgDy, (String)"parentorg");
            long oldParentOrgId = PropertyGetUtils.getDyBdPropId((DynamicObject)dbOrgDy, (String)"parentorg");
            List<DynamicObject> oldStructDys = this.oldOrgId2structDynMap.get(orgId);
            if (newParentOrgId != oldParentOrgId) {
                this.changeOrgStructList.add(new OrgTreeNode(Long.valueOf(orgId), Long.valueOf(newParentOrgId), this.structProjectId));
            } else if (oldStructDys != null) {
                LOG.info("BatchAdminOrgChangeParentOpService_oldStructDys_size_{}", (Object)oldStructDys.size());
                Iterator<DynamicObject> iterator = oldStructDys.iterator();
                while (iterator.hasNext()) {
                    DynamicObject structDy = iterator.next();
                    long dbStructProjectId = PropertyGetUtils.getDyBdPropId((DynamicObject)structDy, (String)"structproject.id");
                    if (dbStructProjectId != this.structProjectId) continue;
                    iterator.remove();
                }
            }
            DynamicObjectCollection structProjectEntity = orgDy.getDynamicObjectCollection("struct_project_entry");
            LOG.info("BatchAdminOrgChangeParentOpService_structProjectEntity_size_{}", (Object)structProjectEntity.size());
            if (structProjectEntity.isEmpty() || oldStructDys == null) continue;
            LOG.info("BatchAdminOrgChangeParentOpService_after_oldStructDys_size_{}", (Object)oldStructDys.size());
            for (int index = 0; index < structProjectEntity.size(); ++index) {
                DynamicObject nowStructProjectEntry = (DynamicObject)structProjectEntity.get(index);
                long entryStructProjectId = PropertyGetUtils.getDyBdPropId((DynamicObject)nowStructProjectEntry, (String)"struct_project");
                long newStructParentId = PropertyGetUtils.getDyBdPropId((DynamicObject)nowStructProjectEntry, (String)"struct_parent_org");
                LOG.info("BatchAdminOrgChangeParentOpService_newStructParentId_{}_index_{}", (Object)newStructParentId, (Object)index);
                boolean find = false;
                for (DynamicObject oldStructDy : oldStructDys) {
                    long dbStructProjectId = PropertyGetUtils.getDyBdPropId((DynamicObject)oldStructDy, (String)"structproject.id");
                    if (entryStructProjectId != dbStructProjectId || entryStructProjectId == AdminOrgConstants.ADMINORG_STRUCT) continue;
                    find = true;
                    long oldStructParentId = PropertyGetUtils.getDyBdPropId((DynamicObject)oldStructDy, (String)"parentorg.id");
                    if (newStructParentId == oldStructParentId) continue;
                    LOG.info("BatchAdminOrgChangeParentOpService_oldStructParentId_{}_index_{}", (Object)oldStructParentId, (Object)index);
                    if (newStructParentId == 0L) {
                        this.deleteOrgStructList.add(new OrgTreeNode(Long.valueOf(orgId), Long.valueOf(newStructParentId), Long.valueOf(entryStructProjectId)));
                        continue;
                    }
                    this.changeOrgStructList.add(new OrgTreeNode(Long.valueOf(orgId), Long.valueOf(newStructParentId), Long.valueOf(entryStructProjectId)));
                }
                if (find || newStructParentId == 0L || entryStructProjectId == AdminOrgConstants.ADMINORG_STRUCT) continue;
                this.addOrgStructList.add(new OrgTreeNode(Long.valueOf(orgId), Long.valueOf(newStructParentId), Long.valueOf(entryStructProjectId)));
            }
        }
    }

    private void handleAddStruct() {
        if (!this.addOrgStructList.isEmpty()) {
            Map<Long, List<OrgTreeNode>> structProjectOrgMap = this.addOrgStructList.stream().collect(Collectors.groupingBy(OrgTreeNode::getStructProjectId));
            Date now = HRDateTimeUtils.getNowDate();
            for (Map.Entry<Long, List<OrgTreeNode>> entry : structProjectOrgMap.entrySet()) {
                Long structProjectId = entry.getKey();
                DynamicObject[] addOrgStructDys = OrgStructHelper.buildAddOrgStructDys(entry.getValue(), this.opDataMap, (Long)structProjectId);
                if (addOrgStructDys.length <= 0) continue;
                for (DynamicObject sturctDy : addOrgStructDys) {
                    if (HRDateTimeUtils.dayAfter((Date)sturctDy.getDate("startdate"), (Date)now)) continue;
                    this.orgStructDys.add(sturctDy);
                    if (!AdminOrgConstants.ADMINORG_STRUCT.equals(entry.getKey())) continue;
                    this.adminOrgStructDys.add(sturctDy);
                }
                TimeLineHelper.saveTimelineDys((String)AdminOrgHisDynKey.ADMIN_STRUCT_KEY.getDynKey(), (DynamicObject[])addOrgStructDys);
            }
        }
    }

    private void handleChangeStruct() {
        if (!this.changeOrgStructList.isEmpty()) {
            for (OrgTreeNode node : this.changeOrgStructList) {
                Long orgId = node.getId();
                DynamicObject orgDy = this.opDataMap.get(orgId);
                Map orgTreeNodeMap = OrgStructHelper.buildStructNodeMapByRootId((Long)node.getParentId(), (DynamicObject)orgDy, (Long)node.getStructProjectId());
                this.buildAndSaveStructDys(node.getParentId(), orgDy, orgTreeNodeMap, node);
                if (this.isAddTempTag || !AdminOrgConstants.ADMINORG_STRUCT.equals(node.getStructProjectId()) || HRStringUtils.equals((String)orgDy.getString("datastatus"), (String)HisModelDataStatusEnum.TO_BE_EFFECT.getStatus())) continue;
                this.changeChildBelongCompany(orgDy, orgTreeNodeMap.keySet());
            }
        }
    }

    private void handleDeleteStruct() {
        if (!this.deleteOrgStructList.isEmpty()) {
            for (OrgTreeNode node : this.deleteOrgStructList) {
                Long orgId = node.getId();
                DynamicObject orgDy = this.opDataMap.get(orgId);
                Map orgTreeNodeMap = OrgStructHelper.buildStructNodeMapByRootId((Long)0L, (DynamicObject)orgDy, (Long)node.getStructProjectId());
                this.buildAndSaveStructDys(node.getParentId(), orgDy, orgTreeNodeMap, node);
            }
        }
    }

    private void buildAndSaveStructDys(long structParentId, DynamicObject adminorgDyn, Map<Long, OrgTreeNode> orgTreeNodeMap, OrgTreeNode node) {
        LOG.info("BatchAdminOrgChangeParentOpService.handleDeleteStruct:{}", (Object)node.getId());
        List structDys = OrgStructHelper.buildStructDysByStructNode((long)structParentId, (DynamicObject)adminorgDyn, orgTreeNodeMap, (boolean)true);
        if (!structDys.isEmpty()) {
            Date now = HRDateTimeUtils.getNowDate();
            for (DynamicObject sturctDy : structDys) {
                if (HRDateTimeUtils.dayAfter((Date)sturctDy.getDate("startdate"), (Date)now)) continue;
                this.orgStructDys.add(sturctDy);
                if (!AdminOrgConstants.ADMINORG_STRUCT.equals(node.getStructProjectId())) continue;
                this.adminOrgStructDys.add(sturctDy);
            }
            TimeLineHelper.saveTimelineDys((String)AdminOrgHisDynKey.ADMIN_STRUCT_KEY.getDynKey(), (DynamicObject[])structDys.toArray(new DynamicObject[0]));
        }
    }
}
