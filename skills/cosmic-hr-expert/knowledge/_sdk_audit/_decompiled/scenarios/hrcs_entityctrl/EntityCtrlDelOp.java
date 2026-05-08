/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.TypeReference
 *  com.alibaba.fastjson.parser.Feature
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.service.perm.log.EntityCtrlLogService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper
 */
package kd.hr.hrcs.opplugin.web.perm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.service.perm.log.EntityCtrlLogService;
import kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper;

public class EntityCtrlDelOp
extends AbstractOperationServicePlugIn {
    private static final Log LOGGER = LogFactory.getLog(EntityCtrlDelOp.class);

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        String toDelDimRoleRangesStr = this.getOption().getVariableValue("toDelDimRoleRanges", "");
        if (HRStringUtils.isEmpty((String)toDelDimRoleRangesStr)) {
            LOGGER.info("Del_IS_EMPTY");
            return;
        }
        Map toDelDimRoleRangeMap = (Map)SerializationUtils.fromJsonString((String)toDelDimRoleRangesStr, Map.class);
        ArrayList idList = Lists.newArrayListWithExpectedSize((int)e.getDataEntities().length);
        ArrayList toDelDimRoleRanges = Lists.newArrayListWithExpectedSize((int)e.getDataEntities().length);
        for (DynamicObject dataEntity : e.getDataEntities()) {
            idList.add(dataEntity.getLong("id"));
            List toDelDimRoleRangeList = (List)toDelDimRoleRangeMap.get(dataEntity.getLong("id"));
            if (CollectionUtils.isEmpty((Collection)toDelDimRoleRangeList)) continue;
            toDelDimRoleRanges.addAll(toDelDimRoleRangeList);
        }
        HRBaseServiceHelper roleDimHelper = new HRBaseServiceHelper("hrcs_roledimension");
        for (String toDelDimRoleRange : toDelDimRoleRanges) {
            String[] split = toDelDimRoleRange.split("\\|");
            EntityCtrlServiceHelper.deleteRoleRange((HRBaseServiceHelper)roleDimHelper, (long)Long.parseLong(split[0]), (String)split[1], (String)split[2], (Long)Long.valueOf(split[3]), (List)Lists.newArrayListWithExpectedSize((int)0));
        }
        String logInfosStr = this.getOption().getVariableValue("logInfos", "");
        this.resolvePermLog(idList, logInfosStr);
    }

    private void resolvePermLog(List<Long> idList, String logInfoStr) {
        if (HRStringUtils.isNotEmpty((String)logInfoStr)) {
            Map entityCtrlLogInfoMap = (Map)JSON.parseObject((String)logInfoStr, (TypeReference)new /* Unavailable Anonymous Inner Class!! */, (Feature[])new Feature[0]);
            ArrayList entityCtrlLogInfos = Lists.newArrayListWithExpectedSize((int)idList.size());
            for (Long id : idList) {
                List entityCtrlModels = (List)entityCtrlLogInfoMap.get(id);
                if (CollectionUtils.isEmpty((Collection)entityCtrlModels)) continue;
                entityCtrlLogInfos.addAll(entityCtrlModels);
            }
            EntityCtrlLogService.resolveLog((List)entityCtrlLogInfos);
        }
    }
}
