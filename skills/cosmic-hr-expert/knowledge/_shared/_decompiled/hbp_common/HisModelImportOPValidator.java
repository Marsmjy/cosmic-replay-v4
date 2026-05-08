/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.basedata.BaseDataServiceHelper
 *  kd.hr.hbp.business.service.history.HisModelCommonService
 *  kd.hr.hbp.business.service.history.core.HisModelGeneralService
 *  kd.hr.hbp.business.service.history.dao.HisModelCommonDao
 *  kd.hr.hbp.business.service.history.util.HisModelImportUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.cache.HRAppCache
 *  kd.hr.hbp.common.cache.IHRAppCache
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.constants.history.HisModelImportConstants
 *  kd.hr.hbp.common.constants.history.HisModelTypeEnum
 *  kd.hr.hbp.common.model.history.HisModelEntityConfig
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRDynamicObjectUtils
 *  kd.hr.hbp.common.util.HRMapUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.sdk.annotation.SdkInternal
 *  org.apache.commons.collections4.MapUtils
 *  org.apache.commons.lang3.tuple.Pair
 */
package kd.hr.hbp.opplugin.web.history.validator;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.basedata.BaseDataServiceHelper;
import kd.hr.hbp.business.service.history.HisModelCommonService;
import kd.hr.hbp.business.service.history.core.HisModelGeneralService;
import kd.hr.hbp.business.service.history.dao.HisModelCommonDao;
import kd.hr.hbp.business.service.history.util.HisModelImportUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.cache.HRAppCache;
import kd.hr.hbp.common.cache.IHRAppCache;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.constants.history.HisModelImportConstants;
import kd.hr.hbp.common.constants.history.HisModelTypeEnum;
import kd.hr.hbp.common.model.history.HisModelEntityConfig;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRDynamicObjectUtils;
import kd.hr.hbp.common.util.HRMapUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.sdk.annotation.SdkInternal;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;

@SdkInternal
public class HisModelImportOPValidator
extends AbstractValidator
implements HisModelConstants,
HisModelImportConstants {
    private static final Log LOGGER = LogFactory.getLog(HisModelImportOPValidator.class);

    public void validate() {
        String importType = this.getOption().getVariableValue("importtype", null);
        if (HRStringUtils.isEmpty((String)importType)) {
            return;
        }
        HisModelEntityConfig hisModelEntityConfig = HisModelCommonService.getInstance().getHisModelEntityConfig(this.getEntityKey());
        HashSet newDataPks = Sets.newHashSetWithExpectedSize((int)16);
        this.handleNewDataForOverrideNewImport(importType, newDataPks);
        this.copyDataForImport(importType);
        this.validateImportPolicy(importType, newDataPks, hisModelEntityConfig);
        this.validateEffStartDate(importType, newDataPks, hisModelEntityConfig);
    }

    private void handleNewDataForOverrideNewImport(String importType, Set<Object> newDataPks) {
        if (!"overridenew".equals(importType)) {
            return;
        }
        for (ExtendedDataEntity dataEntity : this.getDataEntities()) {
            DynamicObject data = dataEntity.getDataEntity();
            if (data.getDataEntityState().getFromDatabase()) continue;
            data.set("iscurrentversion", (Object)Boolean.TRUE);
            newDataPks.add(data.getPkValue());
        }
    }

    private void copyDataForImport(String importType) {
        if (HRStringUtils.equals((String)"new", (String)importType)) {
            return;
        }
        DataEntityPropertyCollection properties = this.getDataEntities()[0].getDataEntity().getDataEntityType().getProperties();
        boolean hasStatus = HisModelGeneralService.getInstance().hasStatus(properties);
        boolean hasEnable = HisModelGeneralService.getInstance().hasEnable(properties);
        this.handleStatusAndEnable(hasStatus, hasEnable);
        boolean isBaseDataCtrl = this.isBdBaseData();
        for (ExtendedDataEntity dataEntity : this.getDataEntities()) {
            String currentDataEnable;
            DynamicObject data = dataEntity.getDataEntity();
            if (!data.getDataEntityState().getFromDatabase()) continue;
            Set<String> ignoreFields = Stream.of("bsled", "hisversion").collect(Collectors.toSet());
            if (hasEnable && HRStringUtils.equals((String)dataEntity.getDataEntity().getString("enable"), (String)"10")) continue;
            Long boId = data.getLong("boid");
            if (hasStatus) {
                if (HRStringUtils.equals((String)dataEntity.getDataEntity().getString("status"), (String)"C")) {
                    data = HisModelImportOPValidator.copyTempVersionDy(this.getEntityKey(), data, ignoreFields);
                    data.set("id", (Object)boId);
                }
            } else {
                data = HisModelImportOPValidator.copyTempVersionDy(this.getEntityKey(), data, ignoreFields);
                data.set("id", (Object)boId);
            }
            boolean hasMasterId = properties.containsKey((Object)"masterid");
            if (isBaseDataCtrl && hasMasterId) {
                data.set("masterid", (Object)boId);
            }
            if (hasEnable && HRStringUtils.isEmpty((String)(currentDataEnable = data.getString("enable")))) {
                data.set("enable", (Object)"1");
            }
            dataEntity.setDataEntity(data);
        }
    }

    private void handleStatusAndEnable(boolean hasStatus, boolean hasEnable) {
        boolean needAudit = this.isNeedAudit(this.getEntityKey(), this.dataEntities[0].getDataEntity().getDataEntityType());
        Map enableMap = new HashMap(0);
        Map statusMap = new HashMap(0);
        if (hasEnable || hasStatus) {
            Set ids = Arrays.stream(this.dataEntities).map(item -> item.getValue("id")).collect(Collectors.toSet());
            Pair dbEnableAndStatus = HisModelCommonDao.getDbEnableAndStatus((boolean)hasStatus, (boolean)hasEnable, (HRBaseServiceHelper)new HRBaseServiceHelper(this.getEntityKey()), ids);
            enableMap = (Map)dbEnableAndStatus.getLeft();
            statusMap = (Map)dbEnableAndStatus.getRight();
        }
        for (ExtendedDataEntity extendedDataEntity : this.dataEntities) {
            boolean curNonAudited;
            boolean nonEnabling;
            DynamicObject curDyn = extendedDataEntity.getDataEntity();
            if (!curDyn.getDataEntityState().getFromDatabase()) continue;
            boolean isAudited = hasStatus && HRStringUtils.equals((String)"C", (String)MapUtils.getString(statusMap, (Object)curDyn.get("id")));
            boolean bl = nonEnabling = hasEnable && !HRStringUtils.equals((String)"10", (String)MapUtils.getString(enableMap, (Object)curDyn.get("id")));
            if (!isAudited || !nonEnabling) continue;
            String curDynStatus = curDyn.getString("status");
            String curDynEnable = curDyn.getString("enable");
            boolean bl2 = curNonAudited = !HRStringUtils.equals((String)curDynStatus, (String)"C");
            if (needAudit && curNonAudited) {
                if (StringUtils.equals((CharSequence)curDynEnable, (CharSequence)"10")) {
                    this.addFatalErrorMessage(extendedDataEntity, ResManager.loadKDString((String)"\u6570\u636e\u5df2\u8bb0\u5f55\u5386\u53f2\uff0c\u4e0d\u5141\u8bb8\u53d8\u66f4\u6570\u636e\u72b6\u6001\uff0c\u4e5f\u4e0d\u5141\u8bb8\u53d8\u66f4\u4f7f\u7528\u72b6\u6001\u4e3a\u201c\u5f85\u542f\u7528\u201d\u3002", (String)"HisModelImportOPValidator_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
                    continue;
                }
                this.addFatalErrorMessage(extendedDataEntity, ResManager.loadKDString((String)"\u6570\u636e\u5df2\u8bb0\u5f55\u5386\u53f2\uff0c\u4e0d\u5141\u8bb8\u53d8\u66f4\u6570\u636e\u72b6\u6001\u3002", (String)"HisModelImportOPValidator_2", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
                continue;
            }
            if (!StringUtils.equals((CharSequence)curDynEnable, (CharSequence)"10")) continue;
            this.addFatalErrorMessage(extendedDataEntity, ResManager.loadKDString((String)"\u6570\u636e\u5df2\u8bb0\u5f55\u5386\u53f2\uff0c\u4e0d\u5141\u8bb8\u53d8\u66f4\u4f7f\u7528\u72b6\u6001\u4e3a\u201c\u5f85\u542f\u7528\u201d\u3002", (String)"HisModelImportOPValidator_3", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
        }
    }

    private void validateImportPolicy(String importType, Set<Object> newDataPks, HisModelEntityConfig hisModelEntityConfig) {
        if (HRStringUtils.equals((String)"new", (String)importType) || hisModelEntityConfig.getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            return;
        }
        String updateType = this.getUpdateType();
        if (HRStringUtils.equals((String)updateType, (String)"1")) {
            return;
        }
        ExtendedDataEntity[] dataCol = this.getDataEntities();
        List<Object> idList = Arrays.stream(dataCol).map(ExtendedDataEntity::getBillPkId).filter(billPkId -> !newDataPks.contains(billPkId)).collect(Collectors.toList());
        if (idList.isEmpty()) {
            return;
        }
        Map<Object, List<DynamicObject>> hisDataMap = this.queryToBeEffectVersions(idList);
        if (hisDataMap.isEmpty()) {
            return;
        }
        for (ExtendedDataEntity dy : dataCol) {
            Date effStartDate = dy.getDataEntity().getDate("bsed");
            List<DynamicObject> hisDataList = hisDataMap.get(dy.getBillPkId());
            if (hisDataList == null || !hisDataList.stream().anyMatch(data -> data.getDate("bsed").getTime() > effStartDate.getTime())) continue;
            this.addErrorMessage(dy, ResManager.loadKDString((String)"\u7cfb\u7edf\u6570\u636e\u5728\u53d8\u66f4\u751f\u6548\u65e5\u671f\u540e\uff0c\u5b58\u5728\u201c\u5f85\u751f\u6548\u201d\u7248\u672c\uff0c\u4e0d\u6267\u884c\u66f4\u65b0\u64cd\u4f5c\u3002", (String)"HisModelImportOPValidator_4", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
        }
    }

    private void validateEffStartDate(String importType, Set<Object> newDataPks, HisModelEntityConfig hisModelEntityConfig) {
        if (HRStringUtils.equals((String)"new", (String)importType) || hisModelEntityConfig.getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            return;
        }
        ExtendedDataEntity[] dataCol = this.getDataEntities();
        Set boIds = Arrays.stream(dataCol).map(data -> data.getValue("boid")).collect(Collectors.toSet());
        HRBaseServiceHelper helper = new HRBaseServiceHelper(this.getEntityKey());
        QFilter qFilter = new QFilter("id", "in", boIds);
        DynamicObjectCollection dys = helper.queryOriginalCollection("id, bsed, iscurrentversion, datastatus", new QFilter[]{qFilter});
        Map<Object, Date> currentMap = dys.stream().collect(Collectors.toMap(dy -> dy.get("id"), dy -> dy.getDate("bsed")));
        for (ExtendedDataEntity dy2 : dataCol) {
            Date effStartDate;
            DynamicObject dataEntity;
            Date currentEffStartDate;
            if (newDataPks.contains(dy2.getBillPkId()) || (currentEffStartDate = currentMap.get((dataEntity = dy2.getDataEntity()).get("boid"))) == null || HisModelDataStatusEnum.TEMP.getStatus().equals(dataEntity.getString("datastatus")) && dataEntity.getBoolean("iscurrentversion") || (effStartDate = (Date)dy2.getValue("bsed")) == null || currentEffStartDate.getTime() <= effStartDate.getTime()) continue;
            this.addErrorMessage(dy2, String.format(ResManager.loadKDString((String)"\u53d8\u66f4\u751f\u6548\u65e5\u671f\u5e94\u665a\u4e8e\u6216\u7b49\u4e8e\u5f53\u524d\u751f\u6548\u65e5\u671f\u201c%s\u201d\u3002", (String)"HisModelImportOPValidator_5", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), HRDateTimeUtils.formatUserSettingFromDate((Date)currentEffStartDate)));
        }
    }

    private Map<Object, List<DynamicObject>> queryToBeEffectVersions(List<Object> idList) {
        QFilter boIdFilter = new QFilter("boid", "in", idList);
        QFilter statusFilter = new QFilter("datastatus", "=", (Object)HisModelDataStatusEnum.TO_BE_EFFECT.getStatus());
        QFilter versionFilter = new QFilter("iscurrentversion", "=", (Object)Boolean.FALSE);
        QFilter[] qFilters = new QFilter[]{boIdFilter, statusFilter, versionFilter};
        HRBaseServiceHelper helper = new HRBaseServiceHelper(this.getEntityKey());
        DynamicObjectCollection hisData = helper.queryOriginalCollection("boid, bsed", qFilters);
        return hisData.stream().collect(Collectors.groupingBy(data -> data.get("boid")));
    }

    private boolean isBdBaseData() {
        String isBdBaseDataKey;
        IHRAppCache cache = HRAppCache.get((String)"hbp");
        String isBdBaseData = (String)cache.get(isBdBaseDataKey = String.format("HisImportIsBdBaseData_%s_%s", this.getEntityKey(), HisModelImportUtil.getImportCacheKeySuffix()), String.class);
        if (HRStringUtils.isEmpty((String)isBdBaseData)) {
            Boolean isBaseDataCtrl = BaseDataServiceHelper.checkBaseDataCtrl((String)this.getEntityKey());
            isBdBaseData = isBaseDataCtrl != false ? "1" : "0";
        }
        return HRStringUtils.equals((String)isBdBaseData, (String)"1");
    }

    private boolean isNeedAudit(String entityNumber, IDataEntityType dataEntityType) {
        IHRAppCache ihrAppCache = HRAppCache.get((String)"hbp");
        String cacheKey = String.format("HisImportNeedAudit_%s_%s", entityNumber, HisModelImportUtil.getImportCacheKeySuffix());
        Boolean needAudit = (Boolean)ihrAppCache.get(cacheKey, Boolean.class);
        if (needAudit == null) {
            needAudit = HisModelGeneralService.getInstance().getNeedAuditForOp(dataEntityType);
        }
        return needAudit;
    }

    private String getUpdateType() {
        Map extParamMap;
        String updateType = null;
        String extParam = this.getOption().getVariableValue("extParam", "");
        if (StringUtils.isNotBlank((CharSequence)extParam) && !HRMapUtils.isEmpty((Map)(extParamMap = (Map)SerializationUtils.fromJsonString((String)extParam, Map.class)))) {
            updateType = (String)extParamMap.get("updatetype");
            LOGGER.info("HisModelImportOPValidator updatetype\uff1a{}", (Object)updateType);
        }
        if (HRStringUtils.isEmpty(updateType)) {
            String entityId = this.getEntityKey();
            IHRAppCache appCache = HRAppCache.get((String)"hbp");
            String cacheKey = String.format("HRHisModelImport_%s_%s_UpdateType", entityId, RequestContext.get().getCurrUserId());
            updateType = (String)appCache.get(cacheKey, String.class);
        }
        return updateType;
    }

    private static DynamicObject copyTempVersionDy(String entityNum, DynamicObject copyData, Set<String> ignoreFields) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper(entityNum);
        DynamicObject dyNewVersion = helper.generateEmptyDynamicObject();
        Set ignoreFieldsAll = Stream.concat(ignoreFields.stream(), Stream.of("id", "masterid", "iscurrentversion", "datastatus", "creator", "creator_id")).collect(Collectors.toSet());
        HRDynamicObjectUtils.copy((DynamicObject)copyData, (DynamicObject)dyNewVersion, ignoreFieldsAll, (boolean)true);
        HisModelImportOPValidator.setSimpleNewInfo(dyNewVersion);
        dyNewVersion.set("iscurrentversion", (Object)Boolean.FALSE);
        dyNewVersion.set("datastatus", (Object)HisModelDataStatusEnum.TEMP.getStatus());
        if (dyNewVersion.getDataEntityType().getProperties().containsKey((Object)"status")) {
            dyNewVersion.set("status", (Object)"A");
        }
        return dyNewVersion;
    }

    private static void setSimpleNewInfo(DynamicObject dynamicObject) {
        if (dynamicObject == null) {
            return;
        }
        Date now = new Date();
        if (dynamicObject.getDynamicObjectType().getProperties().containsKey((Object)"createtime")) {
            dynamicObject.set("createtime", (Object)now);
        }
        if (dynamicObject.getDynamicObjectType().getProperties().containsKey((Object)"creator")) {
            dynamicObject.set("creator", (Object)RequestContext.get().getCurrUserId());
            dynamicObject.set("creator_id", (Object)RequestContext.get().getCurrUserId());
        }
        if (dynamicObject.getDynamicObjectType().getProperties().containsKey((Object)"modifytime")) {
            dynamicObject.set("modifytime", (Object)now);
        }
        if (dynamicObject.getDynamicObjectType().getProperties().containsKey((Object)"modifier")) {
            dynamicObject.set("modifier", (Object)RequestContext.get().getCurrUserId());
            dynamicObject.set("modifier_id", (Object)RequestContext.get().getCurrUserId());
        }
    }
}
