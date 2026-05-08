/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.Row
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.db.DB
 *  kd.bos.db.DBRoute
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr
 *  kd.hr.hrcs.bussiness.service.perm.log.EntityCtrlLogService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.HRBuCaServiceHelper
 *  kd.hr.hrcs.common.constants.perm.log.DimRoleInfoModel
 *  kd.hr.hrcs.common.constants.perm.log.EntityCtrlModel
 *  kd.hr.hrcs.opplugin.validator.perm.EntityControlSaveValidator
 *  org.apache.commons.collections.CollectionUtils
 *  org.apache.commons.collections.MapUtils
 */
package kd.hr.hrcs.opplugin.web.perm;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.db.DB;
import kd.bos.db.DBRoute;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr;
import kd.hr.hrcs.bussiness.service.perm.log.EntityCtrlLogService;
import kd.hr.hrcs.bussiness.servicehelper.perm.HRBuCaServiceHelper;
import kd.hr.hrcs.common.constants.perm.log.DimRoleInfoModel;
import kd.hr.hrcs.common.constants.perm.log.EntityCtrlModel;
import kd.hr.hrcs.opplugin.validator.perm.EntityControlSaveValidator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

@ExcludeFromJacocoGeneratedReport
public final class EntityControlSaveOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new EntityControlSaveValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        String operationKey = args.getOperationKey();
        String importType = this.getOption().getVariableValue("importtype", null);
        if (HRStringUtils.equals((String)"save", (String)operationKey) && HRStringUtils.isNotEmpty((String)importType) && !HRStringUtils.equals((String)"override", (String)importType)) {
            DynamicObject[] dataEntities;
            for (DynamicObject dataEntity : dataEntities = args.getDataEntities()) {
                DynamicObject biaAppDB = dataEntity.getDynamicObject("bizapp");
                DynamicObject bizApp = dataEntity.getDynamicObject("entitytype").getDynamicObject("bizappid");
                if (null != biaAppDB || null == bizApp) continue;
                dataEntity.set("bizapp", (Object)bizApp);
            }
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs args) {
        String operationKey = args.getOperationKey();
        String importType = this.getOption().getVariableValue("importtype", null);
        if (HRStringUtils.equals((String)"save", (String)operationKey) && !HRStringUtils.equals((String)"override", (String)importType)) {
            ArrayList<String> entityIds = new ArrayList<String>(args.getDataEntities().length);
            HashMap<Long, Map<String, List<String>>> syncMustDims = new HashMap<Long, Map<String, List<String>>>(16);
            for (DynamicObject dataEntity : args.getDataEntities()) {
                DynamicObject entityType = dataEntity.getDynamicObject("entitytype");
                String entity = entityType.getString("number");
                String appId = entityType.getString("bizappid.id");
                DynamicObjectCollection entryEntity = dataEntity.getDynamicObjectCollection("entryentity");
                entityIds.add(entity);
                for (DynamicObject entryRow : entryEntity) {
                    int isMust = entryRow.getBoolean("ismust");
                    long dimId = entryRow.getLong("dimension.id");
                    String propKey = entryRow.getString("propkey");
                    if (isMust == 0) continue;
                    this.addSyncInfo(syncMustDims, dimId, entity, appId, propKey);
                }
            }
            Map variables = this.getOption().getVariables();
            List entityCtrlModels = JSONArray.parseArray((String)((String)variables.get("entityCtrlModels")), EntityCtrlModel.class);
            Map<String, EntityCtrlModel> entityCtrlModelMap = entityCtrlModels.stream().collect(Collectors.toMap(EntityCtrlModel::getEntityType, it -> it));
            HashMap<String, Map<String, Set<Long>>> entityRolesFuncMap = new HashMap<String, Map<String, Set<Long>>>(16);
            Map<String, Set<DynamicObject>> entityRoleDims = this.assembleEntityRoleDim(entityIds, entityRolesFuncMap);
            if (MapUtils.isEmpty(entityRolesFuncMap) || MapUtils.isEmpty(entityRoleDims)) {
                return;
            }
            String propDimInfoStr = (String)variables.get("propDimInfo");
            if (!HRStringUtils.isEmpty((String)propDimInfoStr)) {
                Map propDimInfo = (Map)SerializationUtils.fromJsonString((String)propDimInfoStr, Map.class);
                Map originPropDimInfo = (Map)SerializationUtils.fromJsonString((String)((String)variables.get("originPropDimInfo")), Map.class);
                for (DynamicObject dataEntity : args.getDataEntities()) {
                    this.deleteRowsPostProcessing(dataEntity, propDimInfo, originPropDimInfo, entityRoleDims, entityCtrlModelMap);
                }
            }
            this.syncMustDimToRoleDim(syncMustDims, entityRoleDims, entityRolesFuncMap, entityCtrlModelMap);
            ArrayList entityCtrlLogInfos = Lists.newArrayListWithExpectedSize((int)entityCtrlModelMap.size());
            for (Map.Entry<String, EntityCtrlModel> entry : entityCtrlModelMap.entrySet()) {
                List effectDimRoleList = entry.getValue().getEffectDimRoleList();
                HashSet roleIdSet = Sets.newHashSetWithExpectedSize((int)entityCtrlLogInfos.size());
                effectDimRoleList.removeIf(dimRoleInfoModel -> !roleIdSet.add(dimRoleInfoModel.getRoleId()));
                entityCtrlLogInfos.add(entry.getValue());
            }
            EntityCtrlLogService.resolveLog((List)entityCtrlLogInfos);
            HRPermCacheMgr.clearAllCache();
        }
    }

    private void deleteRowsPostProcessing(DynamicObject dataEntity, Map<String, Long> propDimInfo, Map<String, Long> originPropDimInfo, Map<String, Set<DynamicObject>> entityRoleDims, Map<String, EntityCtrlModel> entityCtrlModelMap) {
        String entityNum = dataEntity.getDynamicObject("entitytype").getString("number");
        String appId = dataEntity.getString("bizapp.id");
        if (HRStringUtils.isEmpty((String)entityNum) || HRStringUtils.isEmpty((String)appId)) {
            return;
        }
        HRBaseServiceHelper roleDimHelper = new HRBaseServiceHelper("hrcs_roledimension");
        boolean isDelete = false;
        Set<DynamicObject> roleDimSet = entityRoleDims.get(entityNum);
        for (Map.Entry<String, Long> entry : originPropDimInfo.entrySet()) {
            String propKey = entry.getKey();
            if (HRStringUtils.equals((String)String.valueOf(entry.getValue()), (String)"0")) continue;
            Long dimensionId = entry.getValue();
            if (propDimInfo.get(propKey) != null && dimensionId.equals(propDimInfo.get(propKey))) continue;
            EntityControlSaveOp.deleteRoleRange(roleDimSet, entityNum, propKey);
            isDelete = true;
        }
        if (isDelete) {
            ArrayList effectDimRoleInfoModelList = Lists.newArrayListWithExpectedSize((int)10);
            for (DynamicObject roleDim : roleDimSet) {
                String roleId = roleDim.getString("role.id");
                long dimensionId = roleDim.getLong("dimension.id");
                long buId = roleDim.getLong("bucafunc.id");
                DimRoleInfoModel dimRoleInfoModel = new DimRoleInfoModel(roleId, buId, dimensionId, "delete");
                effectDimRoleInfoModelList.add(dimRoleInfoModel);
            }
            roleDimHelper.update(roleDimSet.toArray(new DynamicObject[0]));
            EntityCtrlModel entityCtrlModel = entityCtrlModelMap.get(entityNum);
            entityCtrlModel.getEffectDimRoleList().addAll(effectDimRoleInfoModelList);
        }
    }

    private static void deleteRoleRange(Set<DynamicObject> roleDimSet, String entityNum, String propKey) {
        for (DynamicObject roleDim : roleDimSet) {
            DynamicObjectCollection entry = roleDim.getDynamicObjectCollection("entry");
            entry.removeIf(it -> null == it.getDynamicObject("entitytype") || it.getString("entitytype.number").equals(entityNum) && it.get("propkey").equals(propKey));
        }
    }

    private void syncMustDimToRoleDim(Map<Long, Map<String, List<String>>> syncMustDims, Map<String, Set<DynamicObject>> entityRoleDims, Map<String, Map<String, Set<Long>>> entityRolesFuncMap, Map<String, EntityCtrlModel> entityCtrlModelMap) {
        if (MapUtils.isEmpty(syncMustDims) || MapUtils.isEmpty(entityRoleDims)) {
            return;
        }
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_roledimension");
        ArrayList<DynamicObject> toUpdateRoleDimList = new ArrayList<DynamicObject>(10);
        for (Map.Entry<Long, Map<String, List<String>>> toSyncMustDimEntry : syncMustDims.entrySet()) {
            long dimId = toSyncMustDimEntry.getKey();
            Map<String, List<String>> entityPropMap = toSyncMustDimEntry.getValue();
            for (Map.Entry<String, List<String>> entityPropEntry : entityPropMap.entrySet()) {
                String[] entityInfo = entityPropEntry.getKey().split("#");
                String entityId = entityInfo[0];
                List<String> toSyncProps = entityPropEntry.getValue();
                Set<DynamicObject> roleDims = entityRoleDims.get(entityId);
                if (CollectionUtils.isEmpty(roleDims) || CollectionUtils.isEmpty(toSyncProps)) continue;
                HashSet toUpdateRoleDims = Sets.newHashSetWithExpectedSize((int)roleDims.size());
                HashMap<String, Set> hasSyncRoles = new HashMap<String, Set>(10);
                ArrayList effectDimRoleInfoModelList = Lists.newArrayListWithExpectedSize((int)10);
                for (DynamicObject dynamicObject : roleDims) {
                    long dimensionId = dynamicObject.getLong("dimension.id");
                    if (dimensionId != dimId) continue;
                    boolean changed = false;
                    String roleId = dynamicObject.getString("role.id");
                    long buId = dynamicObject.getLong("bucafunc.id");
                    Set buIdSet = hasSyncRoles.computeIfAbsent(roleId, key -> Sets.newHashSetWithExpectedSize((int)16));
                    buIdSet.add(buId);
                    DynamicObjectCollection collection = dynamicObject.getDynamicObjectCollection("entry");
                    String opType = "";
                    Iterator iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        DynamicObject row = (DynamicObject)iterator.next();
                        String prop = row.getString("propkey");
                        if (!HRStringUtils.equals((String)entityId, (String)row.getString("entitytype.id")) || !toSyncProps.contains(prop) || row.getBoolean("enable")) continue;
                        iterator.remove();
                        changed = true;
                        opType = "modify";
                    }
                    if (!changed) continue;
                    toUpdateRoleDims.add(dynamicObject);
                    DimRoleInfoModel dimRoleInfoModel = new DimRoleInfoModel(roleId, buId, dimId, opType);
                    effectDimRoleInfoModelList.add(dimRoleInfoModel);
                }
                toUpdateRoleDimList.addAll(toUpdateRoleDims);
                Map<String, Set<Long>> rolesFuncMap = entityRolesFuncMap.get(entityId);
                for (Map.Entry<String, Set<Long>> entry : rolesFuncMap.entrySet()) {
                    String roleId = entry.getKey();
                    if (HRStringUtils.isEmpty((String)roleId)) continue;
                    Set buIdSet = (Set)hasSyncRoles.get(roleId);
                    for (Long buCaFuncId : entry.getValue()) {
                        if (buIdSet != null && buIdSet.contains(buCaFuncId)) continue;
                        DynamicObject roleDimTemp = serviceHelper.generateEmptyDynamicObject();
                        roleDimTemp.set("role", (Object)roleId);
                        roleDimTemp.set("dimension", (Object)dimId);
                        roleDimTemp.set("bucafunc", (Object)buCaFuncId);
                        toUpdateRoleDimList.add(roleDimTemp);
                        DimRoleInfoModel dimRoleInfoModel = new DimRoleInfoModel(roleId, buCaFuncId.longValue(), dimId, "add");
                        effectDimRoleInfoModelList.add(dimRoleInfoModel);
                    }
                }
                if (!entityCtrlModelMap.containsKey(entityId)) continue;
                EntityCtrlModel entityCtrlModel = entityCtrlModelMap.get(entityId);
                entityCtrlModel.getEffectDimRoleList().addAll(effectDimRoleInfoModelList);
            }
        }
        try (TXHandle tx = TX.requiresNew();){
            try {
                serviceHelper.save(toUpdateRoleDimList.toArray(new DynamicObject[0]));
            }
            catch (Exception ex) {
                tx.markRollback();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Map<String, Set<DynamicObject>> assembleEntityRoleDim(List<String> entityIds, Map<String, Map<String, Set<Long>>> entityRolesFuncMap) {
        if (CollectionUtils.isEmpty(entityIds)) {
            return new HashMap<String, Set<DynamicObject>>(16);
        }
        DataSet ds = null;
        try {
            DynamicObject[] roleDimArr;
            String appId;
            String algoKey = "EntityControlSaveValidator.assembleRoleRefEntity";
            String entityIdStr = entityIds.stream().map(it -> "'" + it + "'").collect(Collectors.joining(","));
            ds = DB.queryDataSet((String)algoKey, (DBRoute)DBRoute.of((String)"sys"), (String)("select fentitytypeid,froleid,fbizappid from t_perm_rolepermdetial where fentitytypeid in ( " + entityIdStr + ")"));
            ArrayList<String> roleIds = new ArrayList<String>(10);
            HashMap appEntityMap = Maps.newHashMapWithExpectedSize((int)16);
            HashMap appEntityRoleMap = Maps.newHashMapWithExpectedSize((int)16);
            while (ds.hasNext()) {
                Row next = ds.next();
                String entityId = next.getString("fentitytypeid");
                String string = next.getString("froleid");
                appId = next.getString("fbizappid");
                List entityList = appEntityMap.computeIfAbsent(appId, key -> Lists.newArrayListWithCapacity((int)10));
                entityList.add(entityId);
                Map entityRoleMap = appEntityRoleMap.computeIfAbsent(appId, key -> Maps.newHashMapWithExpectedSize((int)16));
                Set roleSet = entityRoleMap.computeIfAbsent(entityId, key -> Sets.newHashSetWithExpectedSize((int)16));
                roleSet.add(string);
                roleIds.add(string);
            }
            Map buCaFuncFromSpec = HRBuCaServiceHelper.getBuCaFuncFromSpec((Map)appEntityMap);
            for (Map.Entry entry : buCaFuncFromSpec.entrySet()) {
                appId = (String)entry.getKey();
                DynamicObject[] entityRoleMap = (DynamicObject[])appEntityRoleMap.get(appId);
                for (Map.Entry entityFuncEntry : ((Map)entry.getValue()).entrySet()) {
                    String entityNum = (String)entityFuncEntry.getKey();
                    Set roleSet = (Set)entityRoleMap.get(entityNum);
                    long funcId = ((DynamicObject)entityFuncEntry.getValue()).getLong("id");
                    Map roleFuncMap = entityRolesFuncMap.computeIfAbsent(entityNum, key -> Maps.newHashMapWithExpectedSize((int)16));
                    for (String roleId : roleSet) {
                        Set funcIdSet = roleFuncMap.computeIfAbsent(roleId, key -> Sets.newHashSetWithExpectedSize((int)16));
                        funcIdSet.add(funcId);
                    }
                }
            }
            HashMap<String, List> roleDynaMap = new HashMap<String, List>(16);
            HRBaseServiceHelper hRBaseServiceHelper = new HRBaseServiceHelper("hrcs_roledimension");
            for (DynamicObject roleDim : roleDimArr = hRBaseServiceHelper.loadDynamicObjectArray(new QFilter[]{new QFilter("role", "in", roleIds)})) {
                String roleId = roleDim.getString("role.id");
                if (HRStringUtils.isEmpty((String)roleId)) continue;
                List roleDimList = roleDynaMap.computeIfAbsent(roleId, key -> new ArrayList(10));
                roleDimList.add(roleDim);
            }
            HashMap entityRoleDimMap = new HashMap(16);
            for (Map.Entry<String, Map<String, Set<Long>>> entry : entityRolesFuncMap.entrySet()) {
                String entity = entry.getKey();
                Map<String, Set<Long>> rolesAppMap = entry.getValue();
                HashSet roleDims = new HashSet(16);
                for (Map.Entry<String, Set<Long>> rolesAppEntry : rolesAppMap.entrySet()) {
                    if (!roleDynaMap.containsKey(rolesAppEntry.getKey())) continue;
                    roleDims.addAll((Collection)roleDynaMap.get(rolesAppEntry.getKey()));
                }
                entityRoleDimMap.put(entity, roleDims);
            }
            HashMap hashMap = entityRoleDimMap;
            return hashMap;
        }
        finally {
            if (null != ds) {
                ds.close();
            }
        }
    }

    private void addSyncInfo(Map<Long, Map<String, List<String>>> toSyncMap, long dimId, String entity, String appId, String propKey) {
        Map entityProps = toSyncMap.computeIfAbsent(dimId, key -> new HashMap(16));
        List properties = entityProps.computeIfAbsent(entity + "#" + appId, key -> new ArrayList(10));
        properties.add(propKey);
    }
}
