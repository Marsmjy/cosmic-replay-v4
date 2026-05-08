/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.EntityType
 *  kd.bos.entity.EntryType
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.ORM
 *  kd.hr.hbp.business.service.history.HisModelCommonService
 *  kd.hr.hbp.business.service.history.util.HisModelCommonUtil
 *  kd.hr.hbp.business.service.history.validate.HisModelCommonValidateService
 *  kd.hr.hbp.business.service.history.validate.HisModelLineValidateService
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.common.constants.history.HisModelTypeEnum
 *  kd.hr.hbp.common.model.history.HisModelEntityConfig
 *  kd.hr.hbp.common.model.history.param.HisModelOPParam
 *  kd.hr.hbp.common.util.HRHisEntryObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.opplugin.web.history.validator;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.EntityType;
import kd.bos.entity.EntryType;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.ORM;
import kd.hr.hbp.business.service.history.HisModelCommonService;
import kd.hr.hbp.business.service.history.util.HisModelCommonUtil;
import kd.hr.hbp.business.service.history.validate.HisModelCommonValidateService;
import kd.hr.hbp.business.service.history.validate.HisModelLineValidateService;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.constants.history.HisModelTypeEnum;
import kd.hr.hbp.common.model.history.HisModelEntityConfig;
import kd.hr.hbp.common.model.history.param.HisModelOPParam;
import kd.hr.hbp.common.util.HRHisEntryObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public class HisModelOPCommonValidator
extends AbstractValidator
implements HisModelConstants {
    private final HisModelLineValidateService lineValidateService = new HisModelLineValidateService();

    public void validate() {
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities.length == 0) {
            return;
        }
        String entityNumber = dataEntities[0].getDataEntity().getDataEntityType().getName();
        HisModelEntityConfig hisModelEntityConfig = HisModelCommonService.getInstance().getEntityConfigWithException(entityNumber);
        String operateKey = this.getOperateKey();
        String hisOpParamStr = this.getOption().getVariableValue("hisOpParam", "");
        HisModelOPParam hisModelOPParam = new HisModelOPParam();
        if (HRStringUtils.isNotEmpty((String)hisOpParamStr)) {
            hisModelOPParam = (HisModelOPParam)SerializationUtils.fromJsonString((String)hisOpParamStr, HisModelOPParam.class);
        }
        hisModelOPParam = HisModelOPParam.getHisModelOPParamFromOption((OperateOption)this.getOption(), (HisModelOPParam)hisModelOPParam);
        this.validateEntryBoID(entityNumber);
        this.validateTempVersionSave(hisModelOPParam, hisModelEntityConfig);
        block18: for (ExtendedDataEntity dataEntity : dataEntities) {
            switch (operateKey) {
                case "save": 
                case "submit": 
                case "audit": {
                    this.validateDate(dataEntity, hisModelEntityConfig);
                    continue block18;
                }
                case "confirmchange": {
                    this.validateConfirmChange(dataEntity);
                    this.validateDate(dataEntity, hisModelEntityConfig);
                    continue block18;
                }
                case "disable": {
                    this.validateDisable(dataEntity, hisModelOPParam, hisModelEntityConfig);
                    continue block18;
                }
                case "enable": {
                    this.validateEnable(dataEntity);
                    continue block18;
                }
                case "delete": {
                    this.validateDelete(dataEntity);
                    continue block18;
                }
                case "unaudit": {
                    this.validateUnAudit(dataEntity);
                    continue block18;
                }
            }
        }
    }

    private void validateEntryBoID(String entityNumber) {
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        List entryTypes = dataEntityType.getAllEntities().values().stream().filter(entity -> entity instanceof EntryType && entity.getClass() == EntryType.class).collect(Collectors.toList());
        for (EntityType entryType : entryTypes) {
            HRHisEntryObjectUtils.validateEntryForProp(new ArrayList(entryType.getFields().values()));
        }
    }

    private void validateTempVersionSave(HisModelOPParam hisModelOPParam, HisModelEntityConfig hisModelEntityConfig) {
        if (!HRStringUtils.equals((String)this.getOperateKey(), (String)"save")) {
            return;
        }
        if (hisModelOPParam.isReviseSave()) {
            return;
        }
        List tempVersions = Arrays.stream(this.getDataEntities()).map(ExtendedDataEntity::getDataEntity).filter(data -> data.getLong("boid") != 0L && !data.getBoolean("iscurrentversion")).collect(Collectors.toList());
        if (tempVersions.isEmpty()) {
            return;
        }
        if (hisModelEntityConfig.getModelType() == HisModelTypeEnum.NO_INTERRUPTION_NO_OVERLAP) {
            List notIdData = Arrays.stream(this.dataEntities).filter(data -> data.getDataEntity().getLong("id") == 0L).collect(Collectors.toList());
            if (!notIdData.isEmpty()) {
                long[] newIds = ORM.create().genLongIds(this.entityKey, notIdData.size());
                for (int i = 0; i < notIdData.size(); ++i) {
                    ExtendedDataEntity extendedDataEntity = (ExtendedDataEntity)notIdData.get(i);
                    extendedDataEntity.getDataEntity().set("id", (Object)newIds[i]);
                }
            }
            HashMap errorMsgMap = Maps.newHashMapWithExpectedSize((int)16);
            this.lineValidateService.validateTempVersionsOverlapForOp(tempVersions, (Map)errorMsgMap);
            if (errorMsgMap.isEmpty()) {
                return;
            }
            for (ExtendedDataEntity dataEntity : this.dataEntities) {
                long id = dataEntity.getDataEntity().getLong("id");
                if (errorMsgMap.get(id) == null) continue;
                this.addErrorMessage(dataEntity, (String)errorMsgMap.get(id));
            }
        }
    }

    private void validateDate(ExtendedDataEntity dataEntity, HisModelEntityConfig hisModelEntityConfig) {
        if (hisModelEntityConfig.getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            return;
        }
        HisModelLineValidateService validateService = new HisModelLineValidateService();
        DynamicObject data = dataEntity.getDataEntity();
        String errorMsg = validateService.validateStartDateNullForOp(data);
        if (HRStringUtils.isNotEmpty((String)errorMsg)) {
            this.addErrorMessage(dataEntity, errorMsg);
        }
        if (data.getDate("bsed") == null) {
            return;
        }
        errorMsg = validateService.validateDateRange(data);
        if (HRStringUtils.isNotEmpty((String)errorMsg)) {
            this.addErrorMessage(dataEntity, errorMsg);
        }
    }

    private void validateConfirmChange(ExtendedDataEntity dataEntity) {
        DynamicObject data = dataEntity.getDataEntity();
        String errorMsg = HisModelCommonValidateService.getInstance().validateConfirmChange(data);
        if (HRStringUtils.isNotEmpty((String)errorMsg)) {
            this.addErrorMessage(dataEntity, errorMsg);
        }
    }

    public void validateEnable(ExtendedDataEntity dataEntity) {
        DynamicObject data = dataEntity.getDataEntity();
        String errorMsg = HisModelCommonValidateService.getInstance().validateEnable(data);
        if (HRStringUtils.isNotEmpty((String)errorMsg)) {
            this.addErrorMessage(dataEntity, errorMsg);
        }
    }

    public void validateDisable(ExtendedDataEntity dataEntity, HisModelOPParam hisModelOPParam, HisModelEntityConfig hisModelEntityConfig) {
        String errorMsg;
        if (hisModelEntityConfig.getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            errorMsg = HisModelCommonValidateService.getInstance().validateDisable(dataEntity.getDataEntity());
        } else {
            HisModelLineValidateService validateService = new HisModelLineValidateService();
            errorMsg = validateService.validateDisableDate(hisModelOPParam.getDisableDate(), dataEntity.getDataEntity());
        }
        if (HRStringUtils.isNotEmpty((String)errorMsg)) {
            this.addErrorMessage(dataEntity, errorMsg);
        }
    }

    private void validateDelete(ExtendedDataEntity dataEntity) {
        DynamicObject data = dataEntity.getDataEntity();
        String errorMsg = HisModelCommonValidateService.getInstance().validateDeleteVersion(data);
        if (HRStringUtils.isNotEmpty((String)errorMsg)) {
            this.addErrorMessage(dataEntity, errorMsg);
        }
    }

    private void validateUnAudit(ExtendedDataEntity dataEntity) {
        DynamicObject data = dataEntity.getDataEntity();
        if (!HRStringUtils.equals((String)HisModelCommonUtil.safeGetEnable((DynamicObject)data), (String)"10") && HRStringUtils.equals((String)HisModelCommonUtil.safeGetStatus((DynamicObject)data), (String)"C")) {
            this.addErrorMessage(dataEntity, ResManager.loadKDString((String)"\u53cd\u5ba1\u6838\u5931\u8d25\uff0c\u6570\u636e\u5df2\u8bb0\u5f55\u5386\u53f2\uff0c\u4e0d\u5141\u8bb8\u53cd\u5ba1\u6838\u3002", (String)"HisModelOPCommonValidator_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
        }
    }
}
