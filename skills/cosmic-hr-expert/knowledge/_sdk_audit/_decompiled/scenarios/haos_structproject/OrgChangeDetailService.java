/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.property.MulBasedataProp
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.hr.haos.business.domain.org.service.OrgChangeDetailService$AdminBeforeAndAfterId
 *  kd.hr.haos.business.domain.org.service.OrgChangeDetailService$SubEntryEntityBuilder
 *  kd.hr.haos.business.util.IdCreator
 *  kd.hr.haos.business.util.PatternUtil
 *  kd.hr.haos.business.util.PropertyGetUtils
 *  kd.hr.haos.common.constants.batchchg.OrgBatchChgBillConstants
 *  kd.hr.haos.common.constants.masterdata.AdminOrgConstants
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.haos.business.domain.org.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.property.MulBasedataProp;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.haos.business.domain.org.service.OrgChangeDetailService;
import kd.hr.haos.business.util.IdCreator;
import kd.hr.haos.business.util.PatternUtil;
import kd.hr.haos.business.util.PropertyGetUtils;
import kd.hr.haos.common.constants.batchchg.OrgBatchChgBillConstants;
import kd.hr.haos.common.constants.masterdata.AdminOrgConstants;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.util.HRStringUtils;

public class OrgChangeDetailService {
    public List<String> baseInfo = new ArrayList<String>(Arrays.asList("number", "name", "simplename", "adminorgtype", "parentorg", "establishmentdate", "org", "belongcompany", "corporateorg", "adminorglayer", "adminorgfunction", "tobedisableflag", "tobedisabledate", "enable", "companyarea", "city", "workplace", "detailaddress", "description", "index", "positioning", "mainduty", "industrytype"));
    private static final Long[] NEED_COOP_REL_CHANGE_TYPES = new Long[]{OrgBatchChgBillConstants.CHANGE_TYPE_PARENT, OrgBatchChgBillConstants.CHANGE_TYPE_INFO, OrgBatchChgBillConstants.CHANGE_TYPE_MERGE, OrgBatchChgBillConstants.CHANGE_TYPE_SPLIT, OrgBatchChgBillConstants.CHANGE_TYPE_COOP_CHANGE};
    public Map<String, Integer> baseInfoIndexMap = new ConcurrentHashMap<String, Integer>(16);
    public int corStartIndex = 10000;
    public int spStartIndex = 20000;
    public int startIndex = 0;
    public int moreIndex = 10000;
    public List<String> statusInfo = new ArrayList<String>(Arrays.asList("tobedisableflag", "tobedisabledate", "enable"));
    private Map<Long, List<DynamicObject>> oldOrgId2CoopDynMap;
    private Map<Long, List<DynamicObject>> oldOrgId2structDynMap;
    private Map<Long, List<DynamicObject>> newOrgId2structDynMap;
    private final HRBaseServiceHelper ORGBATCHCHGBILL_HELPER;
    private final HRBaseServiceHelper BATCHORGENTITY_HELPER;
    private final HRBaseServiceHelper orgChgEntryHelper;
    private final HRBaseServiceHelper orgChgRecordHelper;
    private final HRBaseServiceHelper subEntryEntity;
    private final HRBaseServiceHelper adminOrgDetail;
    private final List<DynamicObject> orgChgRecordResult;
    private final List<DynamicObject> orgChgEntryResult;
    private final List<DynamicObject> orgChgSubEntryEntity;
    private static final String ORG_CHG_RECORD = "homs_orgchgrecord";
    private static final String ORG_CHG_ENTRY = "homs_orgchgentry";
    private static final String SUB_ENTRY_ENTITY = "homs_subentryentity";
    private static final String ADMIN_ORG_DETAIL = "haos_adminorgdetail";
    private final Map<Long, DynamicObject> adminBoIdToRecord;
    private final List<Long> mergeBoIds;
    private final List<Long> splitBoIds;
    private final Map<Long, DynamicObject> mergeBoIdTODynamicObject;
    private final Map<Long, DynamicObject> splitBoIdTODynamicObject;
    private final Map<Long, DynamicObject> boIdToOrgChgEntry;
    private final IdCreator idCreator;

    public OrgChangeDetailService(Collection<Long> allOrgBoIds) {
        this.baseInfo.forEach(info -> this.baseInfoIndexMap.put((String)info, ++this.startIndex));
        this.oldOrgId2CoopDynMap = new ConcurrentHashMap<Long, List<DynamicObject>>(0);
        this.ORGBATCHCHGBILL_HELPER = new HRBaseServiceHelper("homs_orgbatchchgbill");
        this.BATCHORGENTITY_HELPER = new HRBaseServiceHelper("homs_batchorgentity");
        this.mergeBoIds = new ArrayList<Long>(16);
        this.splitBoIds = new ArrayList<Long>(16);
        this.mergeBoIdTODynamicObject = new ConcurrentHashMap<Long, DynamicObject>(16);
        this.splitBoIdTODynamicObject = new ConcurrentHashMap<Long, DynamicObject>(16);
        this.boIdToOrgChgEntry = new ConcurrentHashMap<Long, DynamicObject>(16);
        this.idCreator = new IdCreator();
        this.orgChgRecordResult = Lists.newArrayListWithExpectedSize((int)16);
        this.orgChgEntryResult = Lists.newArrayListWithExpectedSize((int)16);
        this.orgChgSubEntryEntity = Lists.newArrayListWithExpectedSize((int)16);
        this.orgChgRecordHelper = new HRBaseServiceHelper(ORG_CHG_RECORD);
        this.orgChgEntryHelper = new HRBaseServiceHelper(ORG_CHG_ENTRY);
        this.subEntryEntity = new HRBaseServiceHelper(SUB_ENTRY_ENTITY);
        this.adminOrgDetail = new HRBaseServiceHelper(ADMIN_ORG_DETAIL);
        DynamicObject[] records = this.orgChgRecordHelper.queryOriginalArray("id,adminorg", new QFilter[]{new QFilter("adminorg", "in", allOrgBoIds)});
        this.adminBoIdToRecord = Arrays.stream(records).collect(Collectors.toMap(dyn -> dyn.getLong("adminorg"), dyn -> dyn, (k1, k2) -> k1));
    }

    public void saveOrgChangeDetail(DynamicObject bill, DynamicObject[] orgOrBillEntryDys, Map<Long, DynamicObject> oldOrgDyMap) {
        if (orgOrBillEntryDys == null || orgOrBillEntryDys.length == 0) {
            return;
        }
        IDataEntityType dataEntityType = orgOrBillEntryDys[0].getDataEntityType();
        IDataEntityProperty billIdProperty = (IDataEntityProperty)dataEntityType.getProperties().get((Object)"billid");
        long billId = 0L;
        if (billIdProperty != null) {
            billId = orgOrBillEntryDys[0].getLong("billid");
        }
        if (bill != null) {
            billId = bill.getLong("id");
        }
        Date billModifyTime = new Date();
        DynamicObject batchBillDy = null;
        if (billId != 0L && (batchBillDy = this.ORGBATCHCHGBILL_HELPER.loadOne("creator,isexistsworkflow,modifytime", new QFilter[]{new QFilter("id", "=", (Object)billId)})) != null) {
            billModifyTime = batchBillDy.getDate("modifytime");
        }
        this.init();
        HashMap orgBoIdToAfterValues = Maps.newHashMapWithExpectedSize((int)orgOrBillEntryDys.length);
        HashMap newOrgId2CoopDysMap = Maps.newHashMapWithExpectedSize((int)orgOrBillEntryDys.length);
        ArrayList allOrgIds = Lists.newArrayListWithExpectedSize((int)(orgOrBillEntryDys.length * 2));
        List noMergeSplitOrg = Arrays.stream(orgOrBillEntryDys).filter(dyn -> dyn.getLong("changetype.id") != 1070L && dyn.getLong("changetype.id") != 1080L).filter(dyn -> !HRStringUtils.equals((String)dyn.getString("datastatus"), (String)HisModelDataStatusEnum.TO_BE_EFFECT.getStatus())).collect(Collectors.toList());
        for (DynamicObject data : noMergeSplitOrg) {
            this.fillSomeAttr(orgBoIdToAfterValues, newOrgId2CoopDysMap, allOrgIds, data, oldOrgDyMap);
        }
        Map<Long, DynamicObject> dynamicObjectMap = Arrays.stream(this.adminOrgDetail.loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", (Object)allOrgIds)})).collect(Collectors.toMap(dy -> dy.getLong("id"), dy -> dy));
        long[] recordIds = ORM.create().genLongIds(ORG_CHG_RECORD, orgBoIdToAfterValues.size());
        long[] entryIds = ORM.create().genLongIds(ORG_CHG_ENTRY, orgBoIdToAfterValues.size());
        int recordIndex = 0;
        int entryIdIndex = 0;
        Map<Object, Object> boIdToModifierIdMap = new HashMap();
        if (batchBillDy != null && batchBillDy.getBoolean("isexistsworkflow")) {
            DynamicObject[] batchOrgEntitys = this.BATCHORGENTITY_HELPER.queryOriginalArray("adminorg.boid,modifier.id", new QFilter[]{new QFilter("billid", "=", (Object)batchBillDy.getLong("id"))});
            boIdToModifierIdMap = Arrays.stream(batchOrgEntitys).collect(Collectors.toMap(dyn -> dyn.getLong("adminorg.boid"), dyn -> dyn.getLong("modifier.id"), (v1, v2) -> v1));
        }
        for (Map.Entry<Long, AdminBeforeAndAfterId> entry : orgBoIdToAfterValues.entrySet()) {
            AdminBeforeAndAfterId value = (AdminBeforeAndAfterId)entry.getValue();
            DynamicObject orgchgrecord = this.adminBoIdToRecord.get(entry.getKey());
            if (orgchgrecord == null) {
                orgchgrecord = this.orgChgRecordHelper.generateEmptyDynamicObject();
                orgchgrecord.set("id", (Object)recordIds[recordIndex++]);
                orgchgrecord.set("adminorg", entry.getKey());
            }
            DynamicObject orgchgentry = this.orgChgEntryHelper.generateEmptyDynamicObject();
            orgchgentry.set("chgbill", (Object)value.getBillId());
            orgchgentry.set("orgentry", (Object)value.getEntryId());
            orgchgentry.set("chgeffecttime", (Object)value.getEffectDate());
            orgchgentry.set("changescene", (Object)value.getChangeScene());
            orgchgentry.set("changetype", (Object)value.getChangeType());
            orgchgentry.set("changedescription", value.getChangeDescription());
            orgchgentry.set("changereason", (Object)value.getChangeReason());
            orgchgentry.set("orgchgrecord", (Object)orgchgrecord.getLong("id"));
            if (batchBillDy != null && batchBillDy.getBoolean("isexistsworkflow")) {
                orgchgentry.set("operator", (Object)boIdToModifierIdMap.getOrDefault(entry.getKey(), value.getModifier()));
            } else if (value.getModifier() == 0L) {
                orgchgentry.set("operator", (Object)RequestContext.get().getCurrUserId());
            } else {
                orgchgentry.set("operator", (Object)RequestContext.get().getCurrUserId());
            }
            orgchgentry.set("afterchgorg", (Object)value.getAfterId());
            orgchgentry.set("operationtime", (Object)billModifyTime);
            long entryId = entryIds[entryIdIndex++];
            orgchgentry.set("id", (Object)entryId);
            this.addOrgChgSubEntryEntity(newOrgId2CoopDysMap, dynamicObjectMap, entry, value, entryId);
            this.orgChgEntryResult.add(orgchgentry);
            this.boIdToOrgChgEntry.put(entry.getKey(), orgchgentry);
            if (this.adminBoIdToRecord.get(entry.getKey()) != null) continue;
            this.adminBoIdToRecord.put(entry.getKey(), orgchgrecord);
            this.orgChgRecordResult.add(orgchgrecord);
        }
        this.saveEntity();
    }

    public void saveOrgChangeDetailForSplitAndMerge(DynamicObject bill, DynamicObject[] billEntries) {
        HRBaseServiceHelper orgBatchBillHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
        DynamicObject dynamicObject = orgBatchBillHelper.queryOne("creator,isexistsworkflow,modifytime", new QFilter[]{new QFilter("id", "=", (Object)bill.getLong("id"))});
        Date billModifyTime = new Date();
        if (dynamicObject != null) {
            billModifyTime = dynamicObject.getDate("modifytime");
        }
        this.init();
        this.initMergeSplit(billEntries);
        this.handleMergeAndSplitEntry(bill, billEntries, dynamicObject, billModifyTime, true);
        this.handleMergeAndSplitEntry(bill, billEntries, dynamicObject, billModifyTime, false);
        this.saveEntity();
    }

    private void handleMergeAndSplitEntry(DynamicObject bill, DynamicObject[] billEntries, DynamicObject dynamicObject, Date billModifyTime, boolean isMerge) {
        List mergeOrg = Arrays.stream(billEntries).filter(dyn -> dyn.getLong("changetype.id") == (long)(isMerge ? 1070 : 1080)).collect(Collectors.toList());
        Map<String, List<DynamicObject>> aftermergeorgid = mergeOrg.stream().collect(Collectors.groupingBy(dy -> dy.getLong("changetype.id") + "_" + dy.getLong(isMerge ? "aftermergeorgid" : "beforesplitorgid")));
        for (Map.Entry<String, List<DynamicObject>> entry : aftermergeorgid.entrySet()) {
            String key = entry.getKey();
            List<DynamicObject> value = entry.getValue();
            Long targetOrgId = Long.valueOf(key.substring(key.indexOf("_") + 1));
            List beforemergeorgid = value.stream().filter(dy -> dy.getLong(isMerge ? "beforemergeorgid" : "aftersplitorgid") != 0L).collect(Collectors.toList());
            if (beforemergeorgid.size() <= 0) continue;
            MulBasedataProp baseDataProp = (MulBasedataProp)MetadataServiceHelper.getDataEntityType((String)ORG_CHG_ENTRY).getProperty("mulbasedatafield");
            List needSaveEntries = value.stream().filter(dyn -> dyn.getLong(isMerge ? "beforemergeorgid" : "beforesplitorgid") != dyn.getLong(isMerge ? "aftermergeorgid" : "aftersplitorgid")).collect(Collectors.toList());
            for (DynamicObject dyn2 : needSaveEntries) {
                DynamicObject orgchgentry;
                DynamicObject orgchgrecord = this.adminBoIdToRecord.get(dyn2.getLong("adminorg.boid"));
                if (orgchgrecord == null) {
                    orgchgrecord = this.orgChgRecordHelper.generateEmptyDynamicObject();
                    orgchgrecord.set("id", (Object)this.idCreator.getId());
                    orgchgrecord.set("adminorg", (Object)dyn2.getLong("adminorg.boid"));
                }
                if ((orgchgentry = this.boIdToOrgChgEntry.get(dyn2.getLong("adminorg.boid"))) == null) {
                    orgchgentry = this.orgChgEntryHelper.generateEmptyDynamicObject();
                    orgchgentry.set("chgbill", (Object)bill.getLong("id"));
                    orgchgentry.set("orgentry", (Object)dyn2.getLong("id"));
                    orgchgentry.set("chgeffecttime", (Object)bill.getDate("effdt"));
                    orgchgentry.set("changescene", (Object)dyn2.getLong("changescene.id"));
                    orgchgentry.set("changetype", (Object)dyn2.getLong("changetype.id"));
                    orgchgentry.set("changedescription", dyn2.get("changedescription"));
                    orgchgentry.set("changereason", (Object)dyn2.getLong("changereason.id"));
                    orgchgentry.set("orgchgrecord", (Object)orgchgrecord.getLong("id"));
                    if (dynamicObject != null && dynamicObject.getBoolean("isexistsworkflow")) {
                        if (dyn2.getDynamicObjectType().getProperties().containsKey((Object)"modifier_id")) {
                            orgchgentry.set("operator", (Object)dyn2.getLong("modifier.id"));
                        } else {
                            orgchgentry.set("operator", (Object)dynamicObject.getLong("creator.id"));
                        }
                    } else {
                        orgchgentry.set("operator", (Object)RequestContext.get().getCurrUserId());
                    }
                    orgchgentry.set("operationtime", (Object)billModifyTime);
                    long entryId = this.idCreator.getId();
                    orgchgentry.set("id", (Object)entryId);
                    DynamicObjectCollection entireProcessCollection = new DynamicObjectCollection();
                    for (DynamicObject cooOrgId : beforemergeorgid) {
                        DynamicObject dy2 = new DynamicObject(baseDataProp.getDynamicCollectionItemPropertyType());
                        dy2.set("fbasedataid", (Object)cooOrgId.getLong("adminorg.id"));
                        entireProcessCollection.add((Object)dy2);
                    }
                    this.setOrgChgEntryProperties(isMerge, targetOrgId, orgchgentry, entireProcessCollection);
                    this.orgChgEntryResult.add(orgchgentry);
                    this.boIdToOrgChgEntry.put(dyn2.getLong("adminorg.boid"), orgchgentry);
                } else {
                    DynamicObjectCollection entireProcessCollection = new DynamicObjectCollection();
                    for (DynamicObject cooOrgId : beforemergeorgid) {
                        DynamicObject dy3 = new DynamicObject(baseDataProp.getDynamicCollectionItemPropertyType());
                        dy3.set("fbasedataid", (Object)cooOrgId.getLong("adminorg.id"));
                        entireProcessCollection.add((Object)dy3);
                    }
                    this.setOrgChgEntryProperties(isMerge, targetOrgId, orgchgentry, entireProcessCollection);
                }
                if (this.adminBoIdToRecord.get(dyn2.getLong("adminorg.boid")) != null) continue;
                this.adminBoIdToRecord.put(dyn2.getLong("adminorg.boid"), orgchgrecord);
                this.orgChgRecordResult.add(orgchgrecord);
            }
        }
    }

    private void setOrgChgEntryProperties(boolean isMerge, Long targetOrgId, DynamicObject orgchgentry, DynamicObjectCollection entireProcessCollection) {
        orgchgentry.set("mergesplitflag", (Object)(isMerge ? "1" : "2"));
        if (isMerge) {
            orgchgentry.set("mergedorg", (Object)targetOrgId);
        } else {
            orgchgentry.set("splitedorg", (Object)targetOrgId);
        }
        orgchgentry.set("mulbasedatafield", (Object)entireProcessCollection);
    }

    private void initMergeSplit(DynamicObject[] billEntries) {
        this.mergeBoIds.addAll(Arrays.stream(billEntries).filter(dyn -> dyn.getLong("changetype.id") == 1070L).map(dyn -> dyn.getLong("adminorg.boid")).collect(Collectors.toList()));
        this.splitBoIds.addAll(Arrays.stream(billEntries).filter(dyn -> dyn.getLong("changetype.id") == 1080L).map(dyn -> dyn.getLong("adminorg.boid")).collect(Collectors.toList()));
        this.mergeBoIdTODynamicObject.putAll(Arrays.stream(billEntries).filter(dyn -> dyn.getLong("changetype.id") == 1070L).collect(Collectors.toMap(dyn -> dyn.getLong("adminorg.boid"), dyn -> dyn, (v1, v2) -> v1)));
        this.splitBoIdTODynamicObject.putAll(Arrays.stream(billEntries).filter(dyn -> dyn.getLong("changetype.id") == 1080L).collect(Collectors.toMap(dyn -> dyn.getLong("adminorg.boid"), dyn -> dyn, (v1, v2) -> v1)));
    }

    private void init() {
        DynamicObject dynamicObject = this.adminOrgDetail.generateEmptyDynamicObject();
        MainEntityType dataEntityType = (MainEntityType)dynamicObject.getDataEntityType();
        Map allFields = dataEntityType.getAllFields();
        for (String property : allFields.keySet()) {
            DynamicProperty typeProperty = dataEntityType.getProperty(property);
            if (!PatternUtil.isExProperty((String)property) || typeProperty == null || HRStringUtils.isEmpty((String)typeProperty.getAlias())) continue;
            this.baseInfo.add(property);
            this.baseInfoIndexMap.put(property, ++this.startIndex);
        }
    }

    private void addOrgChgSubEntryEntity(Map<Long, List<DynamicObject>> newOrgId2CoopDynsMap, Map<Long, DynamicObject> dynamicObjectMap, Map.Entry<Long, AdminBeforeAndAfterId> entry, AdminBeforeAndAfterId value, long entryId) {
        if (Stream.of(NEED_COOP_REL_CHANGE_TYPES).anyMatch(changeType -> changeType.longValue() == value.getChangeType()) && OrgBatchChgBillConstants.CHANGE_SCENE_ENABLE.longValue() != value.getOriginalChangeScene() && OrgBatchChgBillConstants.CHANGE_SCENE_DISABLE.longValue() != value.getOriginalChangeScene()) {
            List<DynamicObject> dynamicObjects = this.oldOrgId2CoopDynMap.get(entry.getKey());
            this.orgChgSubEntryEntity.addAll(this.compare(entryId, ADMIN_ORG_DETAIL, dynamicObjectMap.get(value.getBeforeId()), dynamicObjectMap.get(value.getAfterId()), dynamicObjectMap.get(value.getBeforeId()).getLong("id"), dynamicObjectMap.get(value.getAfterId()).getLong("id"), this.baseInfo));
            this.orgChgSubEntryEntity.addAll(this.compareCol(entryId, dynamicObjects, newOrgId2CoopDynsMap.get(entry.getKey())));
            this.orgChgSubEntryEntity.addAll(this.compareStructProject(entryId, entry));
        }
        if (value.getOriginalChangeType() == OrgBatchChgBillConstants.CHANGE_TYPE_DISABLE.longValue() || value.getOriginalChangeScene() == OrgBatchChgBillConstants.CHANGE_SCENE_ENABLE.longValue()) {
            this.orgChgSubEntryEntity.addAll(this.compare(entryId, ADMIN_ORG_DETAIL, dynamicObjectMap.get(value.getBeforeId()), dynamicObjectMap.get(value.getAfterId()), dynamicObjectMap.get(value.getBeforeId()).getLong("id"), dynamicObjectMap.get(value.getAfterId()).getLong("id"), this.statusInfo));
        }
    }

    private void saveEntity() {
        if (this.orgChgRecordResult.size() != 0) {
            this.orgChgRecordHelper.save(this.orgChgRecordResult.toArray(new DynamicObject[0]));
        }
        if (this.orgChgEntryResult.size() != 0) {
            this.orgChgEntryHelper.save(this.orgChgEntryResult.toArray(new DynamicObject[0]));
        }
        if (this.orgChgSubEntryEntity.size() != 0) {
            this.subEntryEntity.save(this.orgChgSubEntryEntity.toArray(new DynamicObject[0]));
        }
    }

    private void fillSomeAttr(Map<Long, AdminBeforeAndAfterId> map, Map<Long, List<DynamicObject>> newOrgId2CoopDysMap, List<Long> allOrgIds, DynamicObject dy, Map<Long, DynamicObject> oldOrgDyMap) {
        long boId = this.getOrgBoId(dy);
        AdminBeforeAndAfterId orDefault = map.getOrDefault(boId, new AdminBeforeAndAfterId());
        long oldOrgDyVid = 0L;
        DynamicObject oldOrgDy = null;
        if (oldOrgDyMap != null) {
            oldOrgDy = oldOrgDyMap.get(boId);
        }
        if (oldOrgDy != null) {
            oldOrgDyVid = oldOrgDy.getLong("sourcevid");
        }
        long newVid = dy.getLong("id");
        if (dy.getBoolean("iscurrentversion")) {
            newVid = dy.getLong("sourcevid");
        }
        allOrgIds.add(oldOrgDyVid);
        allOrgIds.add(newVid);
        DynamicObject changeSceneDy = dy.getDynamicObject("changescene");
        long changeSceneId = changeSceneDy.getLong("id");
        orDefault.setOriginalChangeScene(changeSceneId);
        if (this.mergeBoIds.contains(boId)) {
            changeSceneId = this.mergeBoIdTODynamicObject.get(boId).getLong("changescene.id");
        }
        if (this.splitBoIds.contains(boId)) {
            changeSceneId = this.splitBoIdTODynamicObject.get(boId).getLong("changescene.id");
        }
        long changeTypeId = changeSceneDy.getLong("orgchangetype.id");
        orDefault.setOriginalChangeType(changeTypeId);
        if (this.mergeBoIds.contains(boId)) {
            changeTypeId = this.mergeBoIdTODynamicObject.get(boId).getLong("changetype.id");
        }
        if (this.splitBoIds.contains(boId)) {
            changeTypeId = this.splitBoIdTODynamicObject.get(boId).getLong("changetype.id");
        }
        long changeReason = dy.getLong("changereason.id");
        if (this.mergeBoIds.contains(boId)) {
            changeReason = this.mergeBoIdTODynamicObject.get(boId).getLong("changereason.id");
        }
        if (this.splitBoIds.contains(boId)) {
            changeReason = this.splitBoIdTODynamicObject.get(boId).getLong("changereason.id");
        }
        Object changeDescription = dy.get("changedescription");
        if (this.mergeBoIds.contains(boId)) {
            changeDescription = this.mergeBoIdTODynamicObject.get(boId).get("changedescription");
        }
        if (this.splitBoIds.contains(boId)) {
            changeDescription = this.splitBoIdTODynamicObject.get(boId).get("changedescription");
        }
        orDefault.setChangeScene(changeSceneId);
        orDefault.setChangeType(changeTypeId);
        orDefault.setBeforeId(oldOrgDyVid);
        orDefault.setChangeDescription(changeDescription);
        orDefault.setAfterId(newVid);
        orDefault.setChangeReason(changeReason);
        orDefault.setEstablishTime(dy.getDate("establishmentdate"));
        orDefault.setEffectDate(this.getEffectDate(dy));
        orDefault.setModifier(PropertyGetUtils.getDyBdPropId((DynamicObject)dy, (String)"modifier"));
        IDataEntityType dataEntityType = dy.getDataEntityType();
        IDataEntityProperty billIdProperty = (IDataEntityProperty)dataEntityType.getProperties().get((Object)"billid");
        if (billIdProperty != null) {
            orDefault.setBillId(dy.getLong("billid"));
        }
        map.put(boId, orDefault);
        newOrgId2CoopDysMap.put(boId, (List<DynamicObject>)dy.getDynamicObjectCollection("cooprelentryentity"));
    }

    private Collection<? extends DynamicObject> compareStructProject(long entryId, Map.Entry<Long, AdminBeforeAndAfterId> orgBoIdVsAdminBeforeAndAfterIdModel) {
        if (CollectionUtils.isEmpty(this.oldOrgId2structDynMap) && CollectionUtils.isEmpty(this.newOrgId2structDynMap)) {
            return Collections.emptyList();
        }
        List<DynamicObject> oldStructDyList = this.oldOrgId2structDynMap.get(orgBoIdVsAdminBeforeAndAfterIdModel.getKey());
        List<DynamicObject> newStructDyList = this.newOrgId2structDynMap.get(orgBoIdVsAdminBeforeAndAfterIdModel.getKey());
        ArrayList collection = Lists.newArrayListWithExpectedSize((int)16);
        if ((oldStructDyList == null || oldStructDyList.isEmpty()) && newStructDyList != null) {
            for (DynamicObject newStructDy : newStructDyList) {
                long newStructParentId = PropertyGetUtils.getDyBdPropId((DynamicObject)newStructDy, (String)"parentorg.id");
                if (newStructParentId == 0L) continue;
                collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(++this.spStartIndex).chgEntityNumber("haos_adminorgstruct").beforeChgEntity(0L).afterChgEntity(newStructParentId).entryId(entryId).structProject(PropertyGetUtils.getDyBdPropId((DynamicObject)newStructDy, (String)"structproject.id")).build());
            }
            return collection;
        }
        if (oldStructDyList != null && newStructDyList != null) {
            Map<Long, DynamicObject> oldStructProjectVsStruct = oldStructDyList.stream().collect(Collectors.toMap(dyn -> PropertyGetUtils.getDyBdPropId((DynamicObject)dyn, (String)"structproject.id"), dyn -> dyn, (x, y) -> y));
            Map<Long, DynamicObject> newStructProjectVsStruct = newStructDyList.stream().collect(Collectors.toMap(dyn -> PropertyGetUtils.getDyBdPropId((DynamicObject)dyn, (String)"structproject.id"), dyn -> dyn, (x, y) -> y));
            for (Map.Entry<Long, DynamicObject> oldStructProjectIdVsStructEntry : oldStructProjectVsStruct.entrySet()) {
                DynamicObject newStruct = newStructProjectVsStruct.get(oldStructProjectIdVsStructEntry.getKey());
                if (newStruct == null) {
                    collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(++this.spStartIndex).chgEntityNumber("haos_adminorgstruct").beforeChgEntity(PropertyGetUtils.getDyBdPropId((DynamicObject)oldStructProjectIdVsStructEntry.getValue(), (String)"parentorg.id")).afterChgEntity(0L).entryId(entryId).structProject(oldStructProjectIdVsStructEntry.getKey().longValue()).build());
                    continue;
                }
                DynamicObject oldStruct = oldStructProjectIdVsStructEntry.getValue();
                long structProjectId = PropertyGetUtils.getDyBdPropId((DynamicObject)newStruct, (String)"structproject");
                if (structProjectId == AdminOrgConstants.ADMINORG_STRUCT || PropertyGetUtils.getDyBdPropId((DynamicObject)oldStruct, (String)"parentorg.id") == PropertyGetUtils.getDyBdPropId((DynamicObject)newStruct, (String)"parentorg.id")) continue;
                collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(++this.spStartIndex).chgEntityNumber("haos_adminorgstruct").beforeChgEntity(PropertyGetUtils.getDyBdPropId((DynamicObject)oldStruct, (String)"parentorg.id")).afterChgEntity(PropertyGetUtils.getDyBdPropId((DynamicObject)newStruct, (String)"parentorg.id")).entryId(entryId).structProject(oldStructProjectIdVsStructEntry.getKey().longValue()).build());
            }
            for (Map.Entry<Long, DynamicObject> newStructProjectIdVsStructEntry : newStructProjectVsStruct.entrySet()) {
                DynamicObject oldStruct = oldStructProjectVsStruct.get(newStructProjectIdVsStructEntry.getKey());
                if (oldStruct != null || PropertyGetUtils.getDyBdPropId((DynamicObject)newStructProjectIdVsStructEntry.getValue(), (String)"parentorg.id") == 0L) continue;
                collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(++this.spStartIndex).chgEntityNumber("haos_adminorgstruct").beforeChgEntity(0L).afterChgEntity(PropertyGetUtils.getDyBdPropId((DynamicObject)newStructProjectIdVsStructEntry.getValue(), (String)"parentorg.id")).entryId(entryId).structProject(newStructProjectIdVsStructEntry.getKey().longValue()).build());
            }
        }
        return collection;
    }

    private List<DynamicObject> compareCol(long entryId, List<DynamicObject> oldRelList, List<DynamicObject> newRelList) {
        ArrayList collection = Lists.newArrayListWithExpectedSize((int)16);
        if (oldRelList == null || oldRelList.size() == 0) {
            for (DynamicObject newRel : newRelList) {
                collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(++this.corStartIndex).chgEntityNumber("haos_orgteamcooprel").beforeChgEntity(0L).afterChgEntity(newRel.getLong("cooporgteam.id")).entryId(entryId).coopRelType(newRel.getLong("coopreltype.id")).build());
            }
            return collection;
        }
        Map<Long, DynamicObject> newRelTypeMap = newRelList.stream().collect(Collectors.toMap(dyn -> dyn.getLong("coopreltype.id"), dyn -> dyn));
        Map<Long, DynamicObject> oldRelTypeMap = oldRelList.stream().collect(Collectors.toMap(dyn -> dyn.getLong("coopreltype.id"), dyn -> dyn));
        for (Map.Entry<Long, DynamicObject> entry : oldRelTypeMap.entrySet()) {
            Long coopRelType = entry.getKey();
            if (1010L == coopRelType) continue;
            DynamicObject newRel = newRelTypeMap.get(coopRelType);
            if (newRel == null) {
                collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(++this.corStartIndex).chgEntityNumber("haos_orgteamcooprel").beforeChgEntity(entry.getValue().getLong("cooporgteam.id")).afterChgEntity(0L).entryId(entryId).coopRelType(coopRelType.longValue()).build());
                continue;
            }
            DynamicObject oldRel = entry.getValue();
            if (oldRel.getLong("cooporgteam.boid") == newRel.getLong("cooporgteam.boid")) continue;
            collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(++this.corStartIndex).chgEntityNumber("haos_orgteamcooprel").beforeChgEntity(oldRel.getLong("cooporgteam.id")).afterChgEntity(PropertyGetUtils.getDyBdPropId((DynamicObject)newRel, (String)"cooporgteam.id")).entryId(entryId).coopRelType(coopRelType.longValue()).build());
        }
        for (Map.Entry<Long, DynamicObject> entry : newRelTypeMap.entrySet()) {
            DynamicObject oldRel = oldRelTypeMap.get(entry.getKey());
            if (oldRel != null) continue;
            collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(++this.corStartIndex).chgEntityNumber("haos_orgteamcooprel").beforeChgEntity(0L).afterChgEntity(entry.getValue().getLong("cooporgteam.id")).entryId(entryId).coopRelType(entry.getKey().longValue()).build());
        }
        return collection;
    }

    private List<DynamicObject> compare(long entryId, String entityNumber, DynamicObject before, DynamicObject after, long beforeId, long afterId, List<String> properties) {
        ArrayList collection = Lists.newArrayListWithExpectedSize((int)16);
        if (before == null && after == null) {
            return collection;
        }
        if (before != null && after == null || before == null && after != null) {
            if (before != null) {
                for (String property : properties) {
                    this.comparePlainObject(entryId, entityNumber, before, beforeId, afterId, collection, property);
                }
            } else {
                for (String property : properties) {
                    this.comparePlainObject(entryId, entityNumber, after, beforeId, afterId, collection, property);
                }
            }
            return collection;
        }
        for (String property : properties) {
            Object be = before.get(property);
            Object af = after.get(property);
            if (be instanceof DynamicObject && af instanceof DynamicObject) {
                long id2;
                long id1 = ((DynamicObject)be).getLong("id");
                if (id1 == (id2 = ((DynamicObject)af).getLong("id"))) continue;
                collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(this.baseInfoIndexMap.getOrDefault(property, --this.moreIndex).intValue()).chgEntityNumber(entityNumber).chgPageElement(property).beforeChgEntity(beforeId).afterChgEntity(afterId).entryId(entryId).build());
                continue;
            }
            if (be instanceof OrmLocaleValue && af instanceof OrmLocaleValue) {
                AtomicBoolean breaked = new AtomicBoolean(false);
                ((OrmLocaleValue)be).forEach((beCode, beNameVal) -> {
                    if (HRStringUtils.equals((String)"GLang", (String)beCode)) {
                        return;
                    }
                    String afterVal = ((OrmLocaleValue)af).get(beCode);
                    if (afterVal == null && !HRStringUtils.isEmpty((String)beNameVal) && !breaked.get()) {
                        collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(this.baseInfoIndexMap.getOrDefault(property, --this.moreIndex).intValue()).chgEntityNumber(entityNumber).chgPageElement(property).beforeChgEntity(beforeId).afterChgEntity(afterId).entryId(entryId).build());
                        breaked.set(true);
                        return;
                    }
                    if (!breaked.get() && !HRStringUtils.equals((String)beNameVal, (String)afterVal)) {
                        collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(this.baseInfoIndexMap.getOrDefault(property, --this.moreIndex).intValue()).chgEntityNumber(entityNumber).chgPageElement(property).beforeChgEntity(beforeId).afterChgEntity(afterId).entryId(entryId).build());
                        breaked.set(true);
                        return;
                    }
                });
                ((OrmLocaleValue)af).forEach((afCode, afNameVal) -> {
                    if (HRStringUtils.equals((String)"GLang", (String)afCode)) {
                        return;
                    }
                    String beVal = ((OrmLocaleValue)be).get(afCode);
                    if (beVal == null && !HRStringUtils.isEmpty((String)afNameVal) && !breaked.get()) {
                        collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(this.baseInfoIndexMap.getOrDefault(property, --this.moreIndex).intValue()).chgEntityNumber(entityNumber).chgPageElement(property).beforeChgEntity(beforeId).afterChgEntity(afterId).entryId(entryId).build());
                        breaked.set(true);
                        return;
                    }
                });
                continue;
            }
            if (be == null && af == null || be != null && af != null && be.equals(af)) continue;
            if (be instanceof MulBasedataDynamicObjectCollection && af instanceof MulBasedataDynamicObjectCollection) {
                Set afSet;
                MulBasedataDynamicObjectCollection beCollection = (MulBasedataDynamicObjectCollection)be;
                MulBasedataDynamicObjectCollection afCollection = (MulBasedataDynamicObjectCollection)af;
                Set beSet = beCollection.stream().map(beC -> beC.getLong("fbasedataid_id")).collect(Collectors.toSet());
                if (beSet.equals(afSet = afCollection.stream().map(afC -> afC.getLong("fbasedataid_id")).collect(Collectors.toSet()))) continue;
                collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(this.baseInfoIndexMap.getOrDefault(property, --this.moreIndex).intValue()).chgEntityNumber(entityNumber).chgPageElement(property).beforeChgEntity(beforeId).afterChgEntity(afterId).entryId(entryId).build());
                continue;
            }
            collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(this.baseInfoIndexMap.getOrDefault(property, --this.moreIndex).intValue()).chgEntityNumber(entityNumber).chgPageElement(property).beforeChgEntity(beforeId).afterChgEntity(afterId).entryId(entryId).build());
        }
        return collection;
    }

    private long getOrgBoId(DynamicObject dy) {
        IDataEntityType dataEntityType = dy.getDataEntityType();
        String entityTypeName = dataEntityType.getName();
        if ("".equals(entityTypeName)) {
            return dy.getLong("adminorg.id");
        }
        return dy.getLong("boid");
    }

    private Date getEffectDate(DynamicObject dy) {
        IDataEntityType dataEntityType = dy.getDataEntityType();
        String entityTypeName = dataEntityType.getName();
        if ("".equals(entityTypeName)) {
            return dy.getDate("effdt");
        }
        return dy.getDate("bsed");
    }

    private void comparePlainObject(long entryId, String entityNumber, DynamicObject before, long beforeId, long afterId, List<DynamicObject> collection, String property) {
        Object be = before.get(property);
        if (be instanceof Long && (Long)be == 0L) {
            return;
        }
        if (be instanceof String && HRStringUtils.isEmpty((String)((String)be))) {
            return;
        }
        if (be == null) {
            return;
        }
        collection.add(new SubEntryEntityBuilder().serviceHelper(this.subEntryEntity).seq(this.baseInfoIndexMap.getOrDefault(property, --this.moreIndex).intValue()).chgEntityNumber(entityNumber).chgPageElement(property).beforeChgEntity(beforeId).afterChgEntity(afterId).entryId(entryId).build());
    }

    public void setOldOrgId2CoopDynMap(Map<Long, List<DynamicObject>> oldOrgId2CoopDynMap) {
        this.oldOrgId2CoopDynMap = oldOrgId2CoopDynMap;
    }

    public void setOldOrgId2structDynMap(Map<Long, List<DynamicObject>> oldOrgId2structDynMap) {
        this.oldOrgId2structDynMap = oldOrgId2structDynMap;
    }

    public void setNewOrgId2structDynMap(Map<Long, List<DynamicObject>> newOrgId2structDynMap) {
        this.newOrgId2structDynMap = newOrgId2structDynMap;
    }

    public void deleteOrgChangeDetail() {
        if (!this.adminBoIdToRecord.isEmpty()) {
            this.orgChgRecordHelper.deleteByFilter(new QFilter[]{new QFilter("adminorg", "in", this.adminBoIdToRecord.keySet())});
        }
    }
}
