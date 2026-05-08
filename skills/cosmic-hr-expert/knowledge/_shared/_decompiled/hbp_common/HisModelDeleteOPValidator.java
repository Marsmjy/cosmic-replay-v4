/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.business.service.history.validate.HisModelCommonValidateService
 *  kd.hr.hbp.common.constants.history.HisModelConstants
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
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.business.service.history.validate.HisModelCommonValidateService;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public class HisModelDeleteOPValidator
extends AbstractValidator
implements HisModelConstants {
    public void validate() {
        String operateKey = this.getOperateKey();
        if (!HRStringUtils.equals((String)operateKey, (String)"delete")) {
            return;
        }
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities.length == 0) {
            return;
        }
        List boDataList = Arrays.stream(dataEntities).map(ExtendedDataEntity::getDataEntity).filter(data -> data.getBoolean("iscurrentversion")).collect(Collectors.toList());
        if (boDataList.isEmpty()) {
            return;
        }
        HashMap errorMsgMap = Maps.newHashMapWithExpectedSize((int)boDataList.size());
        HisModelCommonValidateService.getInstance().validateDeleteBo(boDataList, (Map)errorMsgMap);
        if (errorMsgMap.isEmpty()) {
            return;
        }
        for (ExtendedDataEntity dataEntity : dataEntities) {
            List errorMsgList = (List)errorMsgMap.get(dataEntity.getDataEntity().getLong("id"));
            for (String errorMsg : errorMsgList) {
                this.addErrorMessage(dataEntity, errorMsg);
            }
        }
    }
}
