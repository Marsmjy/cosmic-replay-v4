/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.coderule.api.CodeRuleInfo
 *  kd.bos.coderule.domain.DynamicObjectDTO
 *  kd.bos.coderule.domain.GroupHandlerDataEntity
 *  kd.bos.coderule.service.CodeRuleServiceImp
 *  kd.bos.coderule.util.CodeRuleNumberCheckUtil
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.validate.AbstractValidator
 *  org.apache.commons.collections4.CollectionUtils
 */
package kd.bos.business.plugin;

import java.util.List;
import kd.bos.coderule.api.CodeRuleInfo;
import kd.bos.coderule.domain.DynamicObjectDTO;
import kd.bos.coderule.domain.GroupHandlerDataEntity;
import kd.bos.coderule.service.CodeRuleServiceImp;
import kd.bos.coderule.util.CodeRuleNumberCheckUtil;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import org.apache.commons.collections4.CollectionUtils;

public class CodeRuleNumberValidator
extends AbstractValidator {
    private List<DynamicObjectDTO> verifyData;
    private DynamicProperty billNoProp;

    public void validate() {
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (CollectionUtils.isEmpty(this.verifyData) || dataEntities == null || dataEntities.length == 0) {
            return;
        }
        CodeRuleServiceImp codeRuleServiceImp = new CodeRuleServiceImp();
        for (DynamicObjectDTO dynamicObjectDTO : this.verifyData) {
            ExtendedDataEntity extendedDataEntity;
            boolean checkNumberFormat;
            CodeRuleInfo codeRuleInfo = dynamicObjectDTO.getCodeRuleInfo();
            GroupHandlerDataEntity groupHandlerDataEntity = dynamicObjectDTO.getDataEntity();
            DynamicObject dynamicObject = groupHandlerDataEntity.getDynamicObject();
            if (codeRuleInfo == null || !codeRuleInfo.isCheckNumber().booleanValue() || (checkNumberFormat = CodeRuleNumberCheckUtil.checkNumberFormat((CodeRuleInfo)codeRuleInfo, (DynamicObject)dynamicObject, (String)groupHandlerDataEntity.getBillNo())) || (extendedDataEntity = this.getExtendedDataEntity(dataEntities, dynamicObject)) == null) continue;
            this.addErrorMessage(extendedDataEntity, String.format(ResManager.loadKDString((String)"\u201c%1$s\u201d\u7684\u683c\u5f0f\u4e0e\u7cfb\u7edf\u751f\u6210\u7f16\u7801\u7684\u683c\u5f0f\u4e0d\u4e00\u81f4\uff0c\u8bf7\u53c2\u8003\u7f16\u7801\u683c\u5f0f\u201c%2$s\u201d\u4fee\u6539\u6216\u5173\u95ed\u201c\u6821\u9a8c\u7f16\u7801\u683c\u5f0f\u201d\u53c2\u6570\u3002", (String)"CodeRuleNumberValidator_0", (String)"bos-coderule", (Object[])new Object[0]), this.billNoProp.getDisplayName().getLocaleValue(), codeRuleServiceImp.generateExampleSerialNumber(codeRuleInfo, dynamicObject)));
        }
    }

    private ExtendedDataEntity getExtendedDataEntity(ExtendedDataEntity[] dataEntities, DynamicObject data) {
        for (ExtendedDataEntity dataEntity : dataEntities) {
            if (data != dataEntity.getDataEntity()) continue;
            return dataEntity;
        }
        return null;
    }

    public void setVerifyData(List<DynamicObjectDTO> verifyData) {
        this.verifyData = verifyData;
    }

    public void setBillNoProp(DynamicProperty billNoProp) {
        this.billNoProp = billNoProp;
    }
}
