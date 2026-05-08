/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.coderule.api.CodeRuleInfo
 *  kd.bos.coderule.opplugin.util.DynamicObjUtil
 *  kd.bos.coderule.service.cache.CodeRuleCache
 *  kd.bos.coderule.util.CodeRuleSystemParam
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.BillEntityType
 *  kd.bos.entity.operate.IOperationResult
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.ReturnOperationArgs
 *  kd.bos.servicehelper.coderule.CodeRuleServiceHelper
 *  org.apache.commons.collections4.CollectionUtils
 */
package kd.bos.coderule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kd.bos.coderule.api.CodeRuleInfo;
import kd.bos.coderule.opplugin.util.DynamicObjUtil;
import kd.bos.coderule.service.cache.CodeRuleCache;
import kd.bos.coderule.util.CodeRuleSystemParam;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.BillEntityType;
import kd.bos.entity.operate.IOperationResult;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.ReturnOperationArgs;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import org.apache.commons.collections4.CollectionUtils;

public class CodeRuleDeleteOp
extends AbstractOperationServicePlugIn {
    private DynamicObject[] allData = null;
    private boolean isMoveRecycleNumber = true;

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        String entityTypeName = this.billEntityType.getName();
        List codeRuleList = CodeRuleCache.getAllCodeRuleByEntity((String)entityTypeName);
        if (CollectionUtils.isNotEmpty((Collection)codeRuleList)) {
            for (CodeRuleInfo codeRuleInfo : codeRuleList) {
                DynamicObjUtil.injectFieldFromConditionEntry((List)e.getFieldKeys(), (List)codeRuleInfo.getConditionEntry());
                DynamicObjUtil.injectFieldFromCodeRuleEntry((List)e.getFieldKeys(), (List)codeRuleInfo.getRuleEntry());
                DynamicObjUtil.injectFieldFromFilterCondition((List)e.getFieldKeys(), (String)codeRuleInfo.getEnableCondition());
            }
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        this.isMoveRecycleNumber = this.getMoveRecycleNumberParam();
        if (this.isMoveRecycleNumber) {
            this.allData = e.getDataEntities();
            return;
        }
        this.recycleNumber(e.getDataEntities());
    }

    public void onReturnOperation(ReturnOperationArgs e) {
        if (!this.isMoveRecycleNumber) {
            return;
        }
        IOperationResult result = e.getOperationResult();
        if (result == null || this.allData == null) {
            return;
        }
        List successPkIds = result.getSuccessPkIds();
        ArrayList<DynamicObject> successData = new ArrayList<DynamicObject>();
        for (DynamicObject data : this.allData) {
            if (!successPkIds.contains(data.getPkValue())) continue;
            successData.add(data);
        }
        this.recycleNumber(successData.toArray(new DynamicObject[0]));
    }

    private boolean getMoveRecycleNumberParam() {
        return CodeRuleSystemParam.getCustomParameter((String)"CODERULE_DELETE_OP_MOVE_RECYCLE", (boolean)true);
    }

    protected void recycleNumber(DynamicObject[] objs) {
        String billNoField;
        if (objs != null && objs.length != 0 && this.billEntityType instanceof BillEntityType && !StringUtils.isBlank((CharSequence)(billNoField = this.getBillNoField()))) {
            ArrayList<String> billNos = new ArrayList<String>();
            ArrayList<DynamicObject> objList = new ArrayList<DynamicObject>();
            for (int i = 0; i < objs.length; ++i) {
                String billNo = objs[i].getString(billNoField);
                if (!StringUtils.isNotBlank((CharSequence)billNo)) continue;
                billNos.add(billNo);
                objList.add(objs[i]);
            }
            if (!billNos.isEmpty()) {
                CodeRuleServiceHelper.recycleBatchNumber((String)this.billEntityType.getName(), (DynamicObject[])objList.toArray(new DynamicObject[objList.size()]), (String)null, (String[])billNos.toArray(new String[billNos.size()]));
            }
        }
    }

    protected String getBillNoField() {
        return ((BillEntityType)this.billEntityType).getBillNo();
    }
}
