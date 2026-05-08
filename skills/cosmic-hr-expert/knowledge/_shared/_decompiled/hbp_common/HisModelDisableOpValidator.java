/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.business.service.history.HisModelCommonService
 *  kd.hr.hbp.business.service.history.validate.HisModelLineValidateService
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.common.constants.history.HisModelTypeEnum
 *  kd.hr.hbp.common.model.history.HisModelEntityConfig
 *  kd.hr.hbp.common.model.history.param.HisModelOPParam
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.opplugin.web.history.validator;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.business.service.history.HisModelCommonService;
import kd.hr.hbp.business.service.history.validate.HisModelLineValidateService;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.constants.history.HisModelTypeEnum;
import kd.hr.hbp.common.model.history.HisModelEntityConfig;
import kd.hr.hbp.common.model.history.param.HisModelOPParam;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public class HisModelDisableOpValidator
extends AbstractValidator
implements HisModelConstants {
    public void validate() {
        String operateKey = this.getOperateKey();
        if (!HRStringUtils.equals((String)operateKey, (String)"disable")) {
            return;
        }
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities.length == 0) {
            return;
        }
        String entityNumber = dataEntities[0].getDataEntity().getDataEntityType().getName();
        HisModelEntityConfig hisModelEntityConfig = HisModelCommonService.getInstance().getEntityConfigWithException(entityNumber);
        if (hisModelEntityConfig.getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            this.validateEnableForNonLine();
            return;
        }
        String hisOpParamStr = this.getOption().getVariableValue("hisOpParam", "");
        HisModelOPParam hisModelOPParam = new HisModelOPParam();
        if (HRStringUtils.isNotEmpty((String)hisOpParamStr)) {
            hisModelOPParam = (HisModelOPParam)SerializationUtils.fromJsonString((String)hisOpParamStr, HisModelOPParam.class);
        }
        hisModelOPParam = HisModelOPParam.getHisModelOPParamFromOption((OperateOption)this.getOption(), (HisModelOPParam)hisModelOPParam);
        HisModelLineValidateService validateService = new HisModelLineValidateService();
        List dataList = Arrays.stream(dataEntities).map(ExtendedDataEntity::getDataEntity).collect(Collectors.toList());
        HashMap errorMsgMap = Maps.newHashMapWithExpectedSize((int)16);
        this.validateEnableForLine();
        validateService.validateDisable(hisModelOPParam.getDisableDate(), dataList, (Map)errorMsgMap);
        if (errorMsgMap.isEmpty()) {
            return;
        }
        for (ExtendedDataEntity dataEntity : dataEntities) {
            long id = dataEntity.getDataEntity().getLong("id");
            if (errorMsgMap.get(id) == null) continue;
            this.addErrorMessage(dataEntity, (String)errorMsgMap.get(id));
        }
    }

    private void validateEnableForLine() {
        ExtendedDataEntity[] dataEntities;
        for (ExtendedDataEntity dataEntity : dataEntities = this.getDataEntities()) {
            String status = dataEntity.getDataEntity().getString("status");
            String enable = dataEntity.getDataEntity().getString("enable");
            if (!HRStringUtils.equals((String)status, (String)"B") || !HRStringUtils.equals((String)enable, (String)"0")) continue;
            this.addErrorMessage(dataEntity, ResManager.loadKDString((String)"\u6570\u636e\u5df2\u4e3a\u7981\u7528\u72b6\u6001\u3002", (String)"HisModelDisableOpValidator_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
        }
    }

    private void validateEnableForNonLine() {
        ExtendedDataEntity[] dataEntities;
        for (ExtendedDataEntity dataEntity : dataEntities = this.getDataEntities()) {
            String enable = dataEntity.getDataEntity().getString("enable");
            if (!HRStringUtils.equals((String)enable, (String)"0")) continue;
            this.addErrorMessage(dataEntity, ResManager.loadKDString((String)"\u6570\u636e\u5df2\u4e3a\u7981\u7528\u72b6\u6001\u3002", (String)"HisModelDisableOpValidator_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
        }
    }
}
