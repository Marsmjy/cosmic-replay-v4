/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.BillEntityType
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.ORM
 *  kd.bos.servicehelper.basedata.BaseDataServiceHelper
 *  kd.bos.servicehelper.user.UserServiceHelper
 *  kd.hr.hbp.business.service.history.HisModelCommonService
 *  kd.hr.hbp.business.service.history.core.HisModelAttachmentService
 *  kd.hr.hbp.business.service.history.core.HisModelGeneralService
 *  kd.hr.hbp.business.service.history.util.HisModelCommonUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.constants.history.HisModelTypeEnum
 *  kd.hr.hbp.common.model.history.HisModelEntityConfig
 *  kd.hr.hbp.common.model.history.param.HisModelOPParam
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.opplugin.web.history.validator;

import com.google.common.collect.Maps;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.BillEntityType;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.ORM;
import kd.bos.servicehelper.basedata.BaseDataServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;
import kd.hr.hbp.business.service.history.HisModelCommonService;
import kd.hr.hbp.business.service.history.core.HisModelAttachmentService;
import kd.hr.hbp.business.service.history.core.HisModelGeneralService;
import kd.hr.hbp.business.service.history.util.HisModelCommonUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.constants.history.HisModelTypeEnum;
import kd.hr.hbp.common.model.history.HisModelEntityConfig;
import kd.hr.hbp.common.model.history.param.HisModelOPParam;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public class HisModelChangeOPValidator
extends AbstractValidator
implements HisModelConstants {
    public void validate() {
        this.handleCodeRule();
        this.handleBuDisable();
        HisModelOPParam hisModelOPParam = new HisModelOPParam();
        String hisOpParamStr = this.getOption().getVariableValue("hisOpParam", "");
        if (HRStringUtils.isNotEmpty((String)hisOpParamStr)) {
            hisModelOPParam = (HisModelOPParam)SerializationUtils.fromJsonString((String)hisOpParamStr, HisModelOPParam.class);
        }
        if (!(hisModelOPParam = HisModelOPParam.getHisModelOPParamFromOption((OperateOption)this.getOption(), (HisModelOPParam)hisModelOPParam)).isFromChangePageOp() && !hisModelOPParam.isBoChange()) {
            return;
        }
        HisModelEntityConfig hisModelEntityConfig = HisModelCommonService.getInstance().getEntityConfigWithException(this.entityKey);
        this.copyVersionForChangeOption(hisModelEntityConfig);
        String attachmentJson = this.getOption().getVariableValue("attachmentpanel", "");
        if (HRStringUtils.isNotEmpty((String)attachmentJson)) {
            Map attachmentMap = HisModelAttachmentService.getInstance().handleChangeOpAttachments(attachmentJson, (Long)this.getDataEntities()[0].getBillPkId());
            this.getOption().setVariableValue("attachmentpanel", SerializationUtils.toJsonString((Object)attachmentMap));
        }
    }

    private void handleCodeRule() {
        String hisCurrentNumberCode = this.getOption().getVariableValue("hisCurrentNumberCode", null);
        if (HRStringUtils.isNotEmpty((String)hisCurrentNumberCode)) {
            IDataEntityType dataEntityType = this.getDataEntities()[0].getDataEntity().getDataEntityType();
            String numberField = ((BillEntityType)dataEntityType).getBillNo();
            int numberIndex = ((IDataEntityProperty)dataEntityType.getProperties().get((Object)numberField)).getOrdinal();
            String numberFieldChanged = this.getOption().getVariableValue("hisCurrentNumberCodeIsChanged");
            this.getDataEntities()[0].getDataEntity().set(numberField, (Object)hisCurrentNumberCode);
            if (HRStringUtils.equals((String)numberFieldChanged, (String)"false")) {
                this.getDataEntities()[0].getDataEntity().getDataEntityState().setBizChanged(numberIndex, false);
            }
        }
    }

    private void handleBuDisable() {
        String operateKey = this.getOperateKey();
        if (!HRStringUtils.equals((String)operateKey, (String)"disable")) {
            return;
        }
        Boolean isBaseDataCtrl = BaseDataServiceHelper.checkBaseDataCtrl((String)this.getEntityKey());
        if (!isBaseDataCtrl.booleanValue()) {
            return;
        }
        HashMap dataEnableMap = Maps.newHashMapWithExpectedSize((int)16);
        for (ExtendedDataEntity dataEntity : this.getDataEntities()) {
            long id = dataEntity.getDataEntity().getLong("id");
            String enable = dataEntity.getDataEntity().getString("enable");
            dataEnableMap.put(id, enable);
        }
        this.getOption().setVariableValue("dataEnableMap", SerializationUtils.toJsonString((Object)dataEnableMap));
    }

    private void copyVersionForChangeOption(HisModelEntityConfig hisModelEntityConfig) {
        block6: {
            DynamicObject currentUser;
            int index;
            long[] ids;
            HRBaseServiceHelper helper;
            String operationKey;
            ExtendedDataEntity[] dataEntities;
            block5: {
                dataEntities = this.getDataEntities();
                operationKey = this.getOperateKey();
                helper = new HRBaseServiceHelper(this.entityKey);
                ids = ORM.create().genLongIds(this.entityKey, dataEntities.length);
                index = 0;
                currentUser = UserServiceHelper.getCurrentUser((String)"number");
                if (!HRStringUtils.equals((String)operationKey, (String)"save")) break block5;
                for (ExtendedDataEntity dataEntity : dataEntities) {
                    if (!dataEntity.getDataEntity().getBoolean("iscurrentversion")) continue;
                    DynamicObject copyVersion = HisModelGeneralService.getInstance().hisCopy(dataEntity.getDataEntity(), helper);
                    copyVersion.set("iscurrentversion", (Object)Boolean.FALSE);
                    copyVersion.set("datastatus", (Object)HisModelDataStatusEnum.TEMP.getStatus());
                    copyVersion.set("sourcevid", null);
                    copyVersion.set("hisversion", null);
                    copyVersion.set("firstbsed", null);
                    if (HisModelCommonUtil.hasStatusField((DynamicObject)copyVersion)) {
                        copyVersion.set("status", (Object)"A");
                    }
                    copyVersion.set("id", (Object)ids[index++]);
                    copyVersion.set("creator", (Object)currentUser);
                    copyVersion.set("createtime", null);
                    dataEntity.setDataEntity(copyVersion);
                }
                break block6;
            }
            if (!HRStringUtils.equals((String)operationKey, (String)"confirmchange")) break block6;
            Date today = new Date();
            for (ExtendedDataEntity dataEntity : dataEntities) {
                if (!dataEntity.getDataEntity().getBoolean("iscurrentversion")) continue;
                DynamicObject copyVersion = HisModelGeneralService.getInstance().hisCopy(dataEntity.getDataEntity(), helper);
                if (hisModelEntityConfig.getModelType() == HisModelTypeEnum.NO_INTERRUPTION_NO_OVERLAP) {
                    HisModelGeneralService.getInstance().initNewEffectVersion(copyVersion, dataEntity.getDataEntity().getLong("boid"), false, today, null);
                } else {
                    HisModelGeneralService.getInstance().initNewNonLineVersion(copyVersion, dataEntity.getDataEntity().getLong("boid"));
                }
                copyVersion.set("id", (Object)ids[index++]);
                copyVersion.set("creator", (Object)currentUser);
                copyVersion.set("createtime", null);
                dataEntity.setDataEntity(copyVersion);
            }
        }
    }
}
