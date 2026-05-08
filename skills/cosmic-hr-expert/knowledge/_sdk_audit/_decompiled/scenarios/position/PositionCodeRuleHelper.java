/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.servicehelper.coderule.CodeRuleServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionBillQueryRepository
 */
package kd.hrmp.hbpm.business.domain.position.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionBillQueryRepository;

public class PositionCodeRuleHelper {
    public static String getCode(String entityId, DynamicObject dataInfo, String orgID) {
        return CodeRuleServiceHelper.getNumber((String)entityId, (DynamicObject)dataInfo, (String)orgID);
    }

    public static void recycleNumber(DynamicObject[] positionBills) {
        if (positionBills == null || positionBills.length == 0) {
            return;
        }
        Map<String, DynamicObject> numberToBill = Arrays.stream(positionBills).filter(bill -> HRStringUtils.isNotEmpty((String)bill.getString("number"))).collect(Collectors.toMap(bill -> bill.getString("number"), bill -> bill));
        if (numberToBill.size() == 0) {
            return;
        }
        DynamicObject[] existPos = PositionBillQueryRepository.getInstance().queryPositionDataByNumbers(new ArrayList<String>(numberToBill.keySet()));
        Set existNumberSet = Arrays.stream(existPos).map(bill -> bill.getString("number")).collect(Collectors.toSet());
        for (String existNumber : existNumberSet) {
            numberToBill.remove(existNumber);
        }
        Map orgToPosMap = new ArrayList<DynamicObject>(numberToBill.values()).stream().collect(Collectors.groupingBy(bill -> bill.getLong("adminorg.org.id"), Collectors.toList()));
        for (Map.Entry next : orgToPosMap.entrySet()) {
            Long orgId = next.getKey();
            String orgIdStr = null;
            if (orgId != 0L) {
                orgIdStr = String.valueOf(orgId);
            }
            List<DynamicObject> bills = next.getValue();
            CodeRuleServiceHelper.recycleBatchNumber((String)"hbpm_positionhr", (DynamicObject[])bills.toArray(new DynamicObject[0]), (String)orgIdStr, (String[])((String[])bills.stream().map(bill -> bill.getString("number")).toArray(String[]::new)));
        }
    }
}
