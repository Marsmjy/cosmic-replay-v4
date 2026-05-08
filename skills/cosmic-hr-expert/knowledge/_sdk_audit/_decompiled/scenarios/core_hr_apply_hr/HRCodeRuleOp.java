/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.coderule.api.CodeRuleInfo
 *  kd.bos.coderule.api.ICodeRuleService
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.BillEntityType
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.ReturnOperationArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.service.ServiceFactory
 *  kd.bos.servicehelper.coderule.CodeRuleServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.hbp.opplugin.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import kd.bos.coderule.api.CodeRuleInfo;
import kd.bos.coderule.api.ICodeRuleService;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.BillEntityType;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.ReturnOperationArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.service.ServiceFactory;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;

public final class HRCodeRuleOp
extends AbstractOperationServicePlugIn {
    private static final Log log = LogFactory.getLog(HRCodeRuleOp.class);
    private DynamicObject[] allData = null;
    private String billNoFieldKey = "billno";

    public void onAddValidators(AddValidatorsEventArgs e) {
        this.allData = e.getDataEntities();
        DynamicObject[] objs = e.getDataEntities();
        if (objs != null && objs.length > 0) {
            boolean isNew = !objs[0].getDataEntityState().getFromDatabase();
            this.getOption().setVariableValue("isNew", String.valueOf(isNew));
            if (isNew) {
                this.manageBillNo(objs);
            }
        }
    }

    public void onReturnOperation(ReturnOperationArgs e) {
        super.onReturnOperation(e);
        if (this.allData != null && this.allData.length != 0) {
            ArrayList<DynamicObject> list = new ArrayList<DynamicObject>(Arrays.asList(this.allData));
            for (int i = this.allData.length - 1; i >= 0; --i) {
                if (!e.getOperationResult().getSuccessPkIds().contains(this.allData[i].getPkValue())) continue;
                list.remove(i);
            }
            String isNew = this.getOption().getVariableValue("isNew", null);
            if (!list.isEmpty() && HRStringUtils.equals((String)isNew, (String)"true")) {
                log.info("[CodeRuleOp]\u672c\u6b21\u64cd\u4f5c\u6ca1\u6709\u6210\u529f\u51c6\u5907\u5c1d\u8bd5\u56de\u6536\u5904\u7406");
                this.recycleNumber(list.toArray(new DynamicObject[list.size()]));
            }
        }
    }

    private void manageBillNo(DynamicObject[] objs) {
        for (DynamicObject obj : objs) {
            this.manageBillNo(obj);
        }
    }

    private void manageBillNo(DynamicObject obj) {
        String orgId;
        String entityId = obj.getDataEntityType().getName();
        CodeRuleInfo codeRuleInfo = CodeRuleServiceHelper.getCodeRule((String)entityId, (DynamicObject)obj, (String)(orgId = this.getMainOrgId(obj)));
        if (codeRuleInfo == null) {
            return;
        }
        String number = obj.getString(this.billNoFieldKey);
        boolean fitCodeRule = true;
        if (StringUtils.isNotBlank((CharSequence)number)) {
            fitCodeRule = this.getCodeRuleService().checkNumber(entityId, obj, orgId == null ? null : Long.valueOf(Long.parseLong(orgId)), number);
        }
        if (!fitCodeRule) {
            log.error(String.format(Locale.ROOT, "The encoding rules are not met, and the encoding is not regenerated(entityId=%s,number=%s)", entityId, number));
            return;
        }
        if (codeRuleInfo.getIsModifiable().booleanValue()) {
            number = CodeRuleServiceHelper.getNumber((CodeRuleInfo)codeRuleInfo, (DynamicObject)obj);
            obj.set(this.billNoFieldKey, (Object)number);
        } else {
            String billNo = CodeRuleServiceHelper.readNumber((CodeRuleInfo)codeRuleInfo, (DynamicObject)obj);
            if (StringUtils.isBlank((CharSequence)number) || number.equals(billNo) || StringUtils.isNotBlank((CharSequence)number) && !number.equals(billNo)) {
                String codeNum = CodeRuleServiceHelper.getNumber((CodeRuleInfo)codeRuleInfo, (DynamicObject)obj);
                obj.set(this.billNoFieldKey, (Object)codeNum);
            }
        }
    }

    private String getMainOrgId(DynamicObject obj) {
        String mainOrg = this.billEntityType.getMainOrg();
        String orgId = null;
        if (mainOrg == null) {
            return orgId;
        }
        try {
            Object orgObj = obj.get(mainOrg);
            if (orgObj == null) {
                return orgId;
            }
            if (orgObj instanceof DynamicObject) {
                orgId = String.valueOf(((DynamicObject)orgObj).getPkValue());
            } else if (orgObj instanceof Long) {
                orgId = String.valueOf(orgObj);
            }
        }
        catch (Exception e) {
            log.error("coderuleop.e = " + e);
        }
        return orgId;
    }

    protected void recycleNumber(DynamicObject[] objs) {
        if (objs == null || objs.length == 0) {
            return;
        }
        if (!(this.billEntityType instanceof BillEntityType)) {
            return;
        }
        String billNoField = ((BillEntityType)this.billEntityType).getBillNo();
        if (StringUtils.isBlank((CharSequence)billNoField)) {
            return;
        }
        ArrayList<String> billNos = new ArrayList<String>();
        ArrayList<DynamicObject> objList = new ArrayList<DynamicObject>();
        for (int i = 0; i < objs.length; ++i) {
            String billNo = objs[i].getString(billNoField);
            if (!StringUtils.isNotBlank((CharSequence)billNo)) continue;
            billNos.add(billNo);
            objList.add(objs[i]);
        }
        if (!billNos.isEmpty()) {
            ICodeRuleService codeRuleService = this.getCodeRuleService();
            codeRuleService.recycleBatchNumber(this.billEntityType.getName(), objList.toArray(new DynamicObject[objList.size()]), null, billNos.toArray(new String[billNos.size()]));
        }
    }

    private ICodeRuleService getCodeRuleService() {
        ICodeRuleService codeRuleService = (ICodeRuleService)ServiceFactory.getService(ICodeRuleService.class);
        return codeRuleService;
    }
}
