/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.algo.DataSet
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.query.QFilter$QFilterNest
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.coderule.CodeRuleServiceHelper
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgCoopRelRepository
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository
 *  kd.hr.haos.business.domain.adminorg.service.impl.UnitAdminOrgRepository
 *  kd.hr.haos.business.domain.org.service.AdminChangeMsgService
 *  kd.hr.haos.business.util.BaseDataUtils
 *  kd.hr.haos.business.util.PropertyGetUtils
 *  kd.hr.haos.common.constants.AdminOrgDetailConstants
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.impt.common.dto.ImportBillData
 *  kd.hr.impt.common.dto.ImportLog
 *  kd.hr.impt.common.dto.ImptPluginContext
 *  kd.hr.impt.common.enu.ValidatorEnum
 *  kd.hr.impt.common.enu.ValidatorOrderEnum
 *  kd.hr.impt.common.plugin.AfterImportCompleteArgs
 *  kd.hr.impt.common.plugin.AfterInitContextArgs
 *  kd.hr.impt.common.plugin.AfterLoadStartPageEventArgs
 *  kd.hr.impt.common.plugin.BeforeCallOperationEventArgs
 *  kd.hr.impt.common.plugin.BeforeInitValidatorEventArgs
 *  kd.hr.impt.common.plugin.BeforeQueryRefBdEventArgs
 *  kd.hr.impt.common.plugin.BeforeTempStoreInstoreArgs
 *  kd.hr.impt.common.plugin.CustomInstoreParam
 *  kd.hr.impt.common.plugin.HRImportPlugin
 *  kd.hr.impt.core.validate.AbstractValidateHandler
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.haos.formplugin.web.impo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.property.BasedataProp;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgCoopRelRepository;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository;
import kd.hr.haos.business.domain.adminorg.service.impl.UnitAdminOrgRepository;
import kd.hr.haos.business.domain.org.service.AdminChangeMsgService;
import kd.hr.haos.business.util.BaseDataUtils;
import kd.hr.haos.business.util.PropertyGetUtils;
import kd.hr.haos.common.constants.AdminOrgDetailConstants;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.impt.common.dto.ImportBillData;
import kd.hr.impt.common.dto.ImportLog;
import kd.hr.impt.common.dto.ImptPluginContext;
import kd.hr.impt.common.enu.ValidatorEnum;
import kd.hr.impt.common.enu.ValidatorOrderEnum;
import kd.hr.impt.common.plugin.AfterImportCompleteArgs;
import kd.hr.impt.common.plugin.AfterInitContextArgs;
import kd.hr.impt.common.plugin.AfterLoadStartPageEventArgs;
import kd.hr.impt.common.plugin.BeforeCallOperationEventArgs;
import kd.hr.impt.common.plugin.BeforeInitValidatorEventArgs;
import kd.hr.impt.common.plugin.BeforeQueryRefBdEventArgs;
import kd.hr.impt.common.plugin.BeforeTempStoreInstoreArgs;
import kd.hr.impt.common.plugin.CustomInstoreParam;
import kd.hr.impt.common.plugin.HRImportPlugin;
import kd.hr.impt.core.validate.AbstractValidateHandler;
import org.apache.commons.lang3.StringUtils;

public class OrgBatchHRImportPlugin
implements HRImportPlugin {
    protected static final Log LOGGER = LogFactory.getLog(OrgBatchHRImportPlugin.class);
    protected String parentKey = "parentorg";
    private static final String LOG_KEY = "log";
    private static final String ORG_NUMBER_ID_MAP = "orgNumberIdMap";
    private static final String PARENT_ORG_NUMBER_MAP = "parentOrgNumberMap";
    private static final String UNIT_ORG_ID_MAP = "unitOrgIdMap";

    public void beforeQueryRefBd(BeforeQueryRefBdEventArgs args) {
        if (!args.isImportInvoke()) {
            String fieldId = args.getFieldId();
            if ("parentorg".equals(fieldId)) {
                List list = args.arrayToList((Object[])args.getFilters());
                QFilter filter = new QFilter("1", "<>", (Object)1);
                list.add(filter);
                args.setFilters(list.toArray(new QFilter[0]));
            }
        } else if ("parentorg".equals(args.getFieldId())) {
            List list = args.arrayToList((Object[])args.getFilters());
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                QFilter next = (QFilter)iterator.next();
                if ("enable".equals(next.getProperty())) {
                    iterator.remove();
                    continue;
                }
                List nests = next.getNests(true);
                Iterator nestIterator = nests.iterator();
                while (nestIterator.hasNext()) {
                    QFilter.QFilterNest filterNest = (QFilter.QFilterNest)nestIterator.next();
                    QFilter filterNestFilter = filterNest.getFilter();
                    if (!"enable".equals(filterNestFilter.getProperty())) continue;
                    nestIterator.remove();
                }
            }
            args.setFilters(list.toArray(new QFilter[0]));
        }
    }

    public void afterLoadStartPage(AfterLoadStartPageEventArgs args) {
        args.setSerialModel(true);
    }

    public void afterInitContext(AfterInitContextArgs args) {
        CustomInstoreParam customInstoreParam = new CustomInstoreParam();
        customInstoreParam.setSync(true);
        args.setCustomInstoreParam(customInstoreParam);
    }

    public void beforeInitValidator(BeforeInitValidatorEventArgs args) {
        final ConcurrentHashMap customParams = args.getCustomParams();
        if ("new".equals(args.getContext().getImporttype()) || "updateandnew".equals(args.getContext().getImporttype())) {
            String tplNumber = args.getContext().getTplNumber();
            DynamicObject parentOrgImptType = new HRBaseServiceHelper("hies_diaetplconf").queryOriginalOne("tpltreeentryentity.imptattr", new QFilter[]{new QFilter("number", "=", (Object)tplNumber), new QFilter("tpltreeentryentity.imptattr", "!=", (Object)""), new QFilter("tpltreeentryentity.fieldnumber", "=", (Object)"parentorg")});
            String imptType = "number";
            if (parentOrgImptType != null) {
                imptType = parentOrgImptType.getString(0);
            }
            customParams.put("parentOrgImptType", imptType);
            final String finalImptType = imptType;
            args.setValidator(ValidatorEnum.EXISTS_VALIDATOR, new AbstractValidateHandler(){

                public ValidatorOrderEnum setValidatorRole() {
                    return ValidatorOrderEnum.BEFORE;
                }

                public void validate(List<ImportBillData> billDataList, ImportLog importLog) {
                    LOGGER.info("OrgBatchHRImportPlugin.beforeInitValidator.importBillDataList.length:{}", (Object)billDataList.size());
                    customParams.put(OrgBatchHRImportPlugin.LOG_KEY, importLog);
                    long orgId = RequestContext.get().getOrgId();
                    boolean openNumberRule = CodeRuleServiceHelper.isExist((String)"haos_adminorgdetail", (DynamicObject)OrgRepository.getInstance().genEmptyDy(), (String)String.valueOf(orgId));
                    AbstractCollection batchNumberList = null;
                    if (openNumberRule) {
                        String[] batchNumbers = CodeRuleServiceHelper.getBatchNumber((String)"haos_adminorgdetail", (DynamicObject)OrgRepository.getInstance().genEmptyDy(), (String)String.valueOf(orgId), (int)billDataList.size());
                        batchNumberList = Arrays.stream(batchNumbers).collect(Collectors.toCollection(LinkedList::new));
                    }
                    HashSet orgNumbers = Sets.newHashSetWithExpectedSize((int)billDataList.size());
                    for (ImportBillData importBillData : billDataList) {
                        String mainEntityId = importBillData.getMainEntityId();
                        JSONObject dataJson = importBillData.getData().getJSONObject(mainEntityId);
                        Object parentOrg = dataJson.get((Object)OrgBatchHRImportPlugin.this.parentKey);
                        if (parentOrg == null) continue;
                        String number = dataJson.getString("number");
                        if (openNumberRule && StringUtils.isBlank((CharSequence)number)) {
                            number = (String)((LinkedList)batchNumberList).poll();
                            dataJson.put("number", (Object)number);
                        }
                        HashSet orgSets = customParams.getOrDefault("orgSets", Sets.newHashSet());
                        if (StringUtils.isNotBlank((CharSequence)number)) {
                            String name;
                            if ("number_name".equals(finalImptType)) {
                                name = dataJson.getJSONObject("name").getString(RequestContext.get().getLang().getLangTag());
                                orgSets.add(number.concat("##").concat(name));
                            } else if ("name".equals(finalImptType)) {
                                name = dataJson.getJSONObject("name").getString(RequestContext.get().getLang().getLangTag());
                                orgSets.add(name);
                            } else {
                                orgSets.add(number);
                            }
                            customParams.put("orgSets", orgSets);
                        }
                        String parentOrgNumber = "number_name".equals(finalImptType) ? (String)parentOrg : ((JSONObject)parentOrg).getString(finalImptType);
                        orgNumbers.add(dataJson.getString("number"));
                        if (orgSets.contains(parentOrgNumber)) {
                            Map parentOrgNumberMap = customParams.getOrDefault(OrgBatchHRImportPlugin.PARENT_ORG_NUMBER_MAP, Maps.newHashMap());
                            parentOrgNumberMap.put(number, parentOrgNumber);
                            customParams.put(OrgBatchHRImportPlugin.PARENT_ORG_NUMBER_MAP, parentOrgNumberMap);
                            dataJson.put(OrgBatchHRImportPlugin.this.parentKey, null);
                            continue;
                        }
                        LOGGER.info("beforeInitValidator_contains_org_number_{},parentOrgNumber_{}", (Object)number, (Object)parentOrgNumber);
                    }
                    if (batchNumberList != null && !batchNumberList.isEmpty()) {
                        for (String number : batchNumberList) {
                            CodeRuleServiceHelper.recycleNumber((String)"haos_adminorgdetail", (DynamicObject)OrgRepository.getInstance().genEmptyDy(), (String)String.valueOf(orgId), (String)number);
                        }
                    }
                    Map unitOrgIdMap = OrgBatchHRImportPlugin.getUnitOrgIdMap(orgNumbers);
                    Map customParamUnitOrgIdMap = (Map)customParams.get(OrgBatchHRImportPlugin.UNIT_ORG_ID_MAP);
                    if (customParamUnitOrgIdMap != null) {
                        customParamUnitOrgIdMap.putAll(unitOrgIdMap);
                    } else {
                        customParamUnitOrgIdMap = unitOrgIdMap;
                    }
                    customParams.put(OrgBatchHRImportPlugin.UNIT_ORG_ID_MAP, customParamUnitOrgIdMap);
                }
            });
        }
    }

    public void beforeTempStoreInstore(BeforeTempStoreInstoreArgs args) {
        ArrayList importBillDataList = Lists.newArrayListWithExpectedSize((int)16);
        Map changeSceneMap = BaseDataUtils.getBaseDataMap((String)"haos_changescene");
        ConcurrentHashMap.KeySetView parentNumberSet = ConcurrentHashMap.newKeySet();
        ConcurrentHashMap.KeySetView currentParentNumberSet = ConcurrentHashMap.newKeySet();
        Map parentOrgNumberMap = args.getCustomParams().getOrDefault(PARENT_ORG_NUMBER_MAP, Maps.newHashMap());
        Long lastChangeTypeId = null;
        for (Map.Entry dataSetEntry : args.getDatas().entrySet()) {
            DataSet dataSet = (DataSet)dataSetEntry.getValue();
            while (dataSet.hasNext()) {
                ImportBillData billData = (ImportBillData)JSONObject.parseObject((String)((String)dataSet.next().get("data")), ImportBillData.class);
                Object pkId = billData.getPkId();
                if (pkId instanceof Integer) {
                    billData.setPkId((Object)Long.parseLong(pkId.toString()));
                }
                JSONObject data = billData.getData().getJSONObject(billData.getMainEntityId());
                String number = data.getString("number");
                JSONObject changeScene = data.getJSONObject("changescene");
                Long changeSceneId = changeScene.getLong("id");
                DynamicObject changeSceneDy = (DynamicObject)changeSceneMap.get(changeSceneId);
                Long changeTypeId = changeSceneDy.getLong("orgchangetype.id");
                if (AdminOrgDetailConstants.CHANGE_TYPE_DISABLE.equals(changeTypeId)) {
                    if (!importBillDataList.isEmpty() && !changeTypeId.equals(lastChangeTypeId)) {
                        try {
                            args.getSyncSemaphore().acquire();
                            args.getCustomParams().put("disable", "false");
                            args.pushInstore((List)importBillDataList);
                        }
                        catch (InterruptedException interruptedException) {
                            LOGGER.error((Throwable)interruptedException);
                            throw new KDBizException(new ErrorCode("beforeTempStoreInstore", interruptedException.getMessage()), new Object[0]);
                        }
                        currentParentNumberSet.clear();
                        importBillDataList.clear();
                    }
                    importBillDataList.add(billData);
                    lastChangeTypeId = changeTypeId;
                    continue;
                }
                if (AdminOrgDetailConstants.CHANGE_TYPE_DISABLE.equals(lastChangeTypeId) && !importBillDataList.isEmpty()) {
                    try {
                        args.getSyncSemaphore().acquire();
                        args.getCustomParams().put("disable", "true");
                        args.pushInstore((List)importBillDataList);
                    }
                    catch (InterruptedException interruptedException) {
                        LOGGER.error((Throwable)interruptedException);
                        throw new KDBizException(new ErrorCode("beforeTempStoreInstore", interruptedException.getMessage()), new Object[0]);
                    }
                    currentParentNumberSet.clear();
                    importBillDataList.clear();
                }
                lastChangeTypeId = changeTypeId;
                if (data.get((Object)this.parentKey) != null) {
                    importBillDataList.add(billData);
                    parentNumberSet.add(number);
                    currentParentNumberSet.add(number);
                    continue;
                }
                String parentNumber = String.valueOf(parentOrgNumberMap.get(number));
                if (parentNumberSet.contains(parentNumber) && !importBillDataList.isEmpty() && currentParentNumberSet.contains(parentNumber)) {
                    try {
                        args.getSyncSemaphore().acquire();
                        args.getCustomParams().put("disable", "false");
                        args.pushInstore((List)importBillDataList);
                    }
                    catch (InterruptedException interruptedException) {
                        LOGGER.error((Throwable)interruptedException);
                        throw new KDBizException(new ErrorCode("beforeTempStoreInstore", interruptedException.getMessage()), new Object[0]);
                    }
                    currentParentNumberSet.clear();
                    importBillDataList.clear();
                }
                currentParentNumberSet.add(number);
                parentNumberSet.add(number);
                importBillDataList.add(billData);
            }
        }
        try {
            if (!importBillDataList.isEmpty()) {
                args.getSyncSemaphore().acquire();
                if (AdminOrgDetailConstants.CHANGE_TYPE_DISABLE.equals(lastChangeTypeId)) {
                    args.getCustomParams().put("disable", "true");
                } else {
                    args.getCustomParams().put("disable", "false");
                }
                args.pushInstore((List)importBillDataList);
            }
        }
        catch (InterruptedException interruptedException) {
            LOGGER.error((Throwable)interruptedException);
            throw new KDBizException(new ErrorCode("beforeTempStoreInstore", interruptedException.getMessage()), new Object[0]);
        }
    }

    /*
     * WARNING - void declaration
     */
    public void beforeCallOperation(BeforeCallOperationEventArgs args) {
        LOGGER.info("OrgBatchHRImportPlugin.beforeCallOperation.importBillDataList.length:{}", (Object)args.getDynamicObjects().length);
        Object[] orgDys = (DynamicObject[])args.getDynamicObjects();
        ConcurrentHashMap customParams = args.getCustomParams();
        List importBillData = args.getImportBillDatas();
        ImportLog importLog = (ImportLog)customParams.get(LOG_KEY);
        Map orgNumberIdMap = customParams.getOrDefault(ORG_NUMBER_ID_MAP, Maps.newHashMap());
        Map parentOrgNumberMap = customParams.getOrDefault(PARENT_ORG_NUMBER_MAP, new HashMap());
        Map unitOrgNumberIdMap = customParams.getOrDefault(UNIT_ORG_ID_MAP, new HashMap());
        if ("true".equals(customParams.get("disable"))) {
            ImptPluginContext context = args.getContext();
            Set allEntityIds = context.getAllEntityId();
            args.setGroupName("disableorg");
            for (String entityId : allEntityIds) {
                args.getSubmitOPs().put(entityId, "disableorg");
            }
        }
        HashSet orgIds = Sets.newHashSetWithExpectedSize((int)orgDys.length);
        ArrayList errorIds = Lists.newArrayListWithExpectedSize((int)orgDys.length);
        LOGGER.info("beforeCallOperation_unitOrgNumberIdMap_{}", (Object)unitOrgNumberIdMap);
        for (int index = 0; index < orgDys.length; ++index) {
            DynamicObject orgDy2 = orgDys[index];
            ImportBillData importData = (ImportBillData)importBillData.get(index);
            String number = orgDy2.getString("number");
            orgNumberIdMap.put(number, orgDy2.getLong("id"));
            customParams.put(ORG_NUMBER_ID_MAP, orgNumberIdMap);
            Object parentOrgNumber = parentOrgNumberMap.remove(number);
            if (parentOrgNumber != null) {
                void var16_20;
                Object v = orgNumberIdMap.get(parentOrgNumber);
                if (unitOrgNumberIdMap.get(parentOrgNumber) != null) {
                    Object v2 = unitOrgNumberIdMap.get(parentOrgNumber);
                }
                if (var16_20 != null) {
                    DynamicObject singleFromCache = BusinessDataServiceHelper.loadSingleFromCache((Object)var16_20, (DynamicObjectType)((BasedataProp)orgDy2.getDynamicObjectType().getProperty(this.parentKey)).getDynamicComplexPropertyType());
                    if (singleFromCache == null) {
                        LOGGER.error("beforeCallOperation_parentOrg_is_null_parentId={}", (Object)var16_20);
                        importLog.writeRowLog(importData.getSheetName(), importData.getStartIndex(), importData.getEndIndex(), ResManager.loadKDString((String)"\u4e0a\u7ea7\u7ec4\u7ec7\u83b7\u53d6\u4e3a\u7a7a\uff0c\u8bf7\u68c0\u67e5\u3002", (String)"OrgBatchHRImportPlugin_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                        errorIds.add(orgDy2.getLong("id"));
                        continue;
                    }
                    orgDy2.set(this.parentKey, (Object)singleFromCache);
                } else {
                    LOGGER.error("beforeCallOperation_parentOrgId_is_null_parentNumber={}", (Object)number);
                    importLog.writeRowLog(importData.getSheetName(), importData.getStartIndex(), importData.getEndIndex(), ResManager.loadKDString((String)"\u4e0a\u7ea7\u7ec4\u7ec7\u83b7\u53d6\u4e3a\u7a7a\uff0c\u8bf7\u68c0\u67e5\u3002", (String)"OrgBatchHRImportPlugin_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                    errorIds.add(orgDy2.getLong("id"));
                    continue;
                }
            }
            orgIds.add(orgDy2.getLong("boid"));
        }
        DynamicObject[] orgRelDys = OrgCoopRelRepository.getInstance().queryOriginalCurrentCoopRelDys((Collection)orgIds);
        HashMap dbOrgIdToRelMap = Maps.newHashMapWithExpectedSize((int)orgRelDys.length);
        for (DynamicObject dynamicObject : orgRelDys) {
            long orgId = dynamicObject.getLong("org.id");
            Map coopTypeMap = dbOrgIdToRelMap.getOrDefault(orgId, new HashMap());
            long coopTypeId = PropertyGetUtils.getDyBdPropId((DynamicObject)dynamicObject, (String)"coopreltype.id");
            coopTypeMap.put(coopTypeId, dynamicObject);
            dbOrgIdToRelMap.put(orgId, coopTypeMap);
        }
        if (!errorIds.isEmpty()) {
            orgDys = (DynamicObject[])Arrays.stream(orgDys).filter(orgDy -> !errorIds.contains(orgDy.getLong("id"))).toArray(DynamicObject[]::new);
        }
        for (DynamicObject dynamicObject : orgDys) {
            long orgId = dynamicObject.getLong("boid");
            Map dbCoopTypeMap = (Map)dbOrgIdToRelMap.get(orgId);
            if (CollectionUtils.isEmpty((Map)dbCoopTypeMap)) continue;
            DynamicObjectCollection coopRelEntryEntity = dynamicObject.getDynamicObjectCollection("cooprelentryentity");
            this.fillSubCoopRel(dbCoopTypeMap, coopRelEntryEntity);
            this.fillOtherCoopRelId(dbCoopTypeMap, coopRelEntryEntity);
            dynamicObject.set("cooprelentryentity", (Object)coopRelEntryEntity);
        }
        args.setDynamicObjects(orgDys);
    }

    private void fillOtherCoopRelId(Map<Long, DynamicObject> dbCoopTypeMap, DynamicObjectCollection coopRelEntryEntity) {
        for (DynamicObject coopRelEntry : coopRelEntryEntity) {
            long coopTypeId = PropertyGetUtils.getDyBdPropId((DynamicObject)coopRelEntry, (String)"coopreltype.id");
            if (!dbCoopTypeMap.containsKey(coopTypeId)) continue;
            DynamicObject dbCoopRelDyn = dbCoopTypeMap.get(coopTypeId);
            coopRelEntry.set("cooprelid", dbCoopRelDyn.get("id"));
        }
    }

    private void fillSubCoopRel(Map<Long, DynamicObject> dbCoopTypeMap, DynamicObjectCollection coopRelEntryEntity) {
        DynamicObject subCoopRel = dbCoopTypeMap.get(1010L);
        if (subCoopRel == null) {
            return;
        }
        DynamicObjectType entityDynamicObjectType = coopRelEntryEntity.getDynamicObjectType();
        DynamicObject orgRelEntry = new DynamicObject(entityDynamicObjectType);
        Object coopOrgTeam = subCoopRel.get("cooporgteam");
        orgRelEntry.set("coopreltype", subCoopRel.get("coopreltype"));
        orgRelEntry.set("cooporgteam", coopOrgTeam);
        orgRelEntry.set("cooprelid", subCoopRel.get("id"));
        coopRelEntryEntity.add((Object)orgRelEntry);
    }

    public void afterImportComplete(AfterImportCompleteArgs args) {
        new AdminChangeMsgService().handleChangeMsg();
    }

    private static Map<String, Long> getUnitOrgIdMap(Set<String> orgNumbers) {
        DynamicObject[] unitOrgDys = UnitAdminOrgRepository.getInstance().loadByFilter(new QFilter("number", "in", orgNumbers));
        HashMap unitOrgIdMap = Maps.newHashMapWithExpectedSize((int)unitOrgDys.length);
        for (DynamicObject unitOrg : unitOrgDys) {
            unitOrgIdMap.put(unitOrg.getString("number"), unitOrg.getLong("id"));
        }
        return unitOrgIdMap;
    }
}
