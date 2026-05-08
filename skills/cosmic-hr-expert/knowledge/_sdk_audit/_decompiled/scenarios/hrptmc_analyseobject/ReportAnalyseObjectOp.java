/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DataEntityBase
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.ISVInfo
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.id.ID
 *  kd.bos.lang.Lang
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.schedule.api.JobInfo
 *  kd.bos.schedule.api.JobType
 *  kd.bos.schedule.executor.JobClient
 *  kd.bos.servicehelper.ISVServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.model.complexobj.labelandreport.JoinConditionBo
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrptmc.business.anobj.AnObjDataStoreTaskServiceHelper
 *  kd.hr.hrptmc.business.anobj.AnObjGroupFieldService
 *  kd.hr.hrptmc.business.anobj.AnObjHisVersionService
 *  kd.hr.hrptmc.business.anobj.AnObjPermRuleService
 *  kd.hr.hrptmc.business.anobj.AnObjSideBarService
 *  kd.hr.hrptmc.business.anobj.AnalyseObjectService
 *  kd.hr.hrptmc.business.calfield.CalculateFieldService
 *  kd.hr.hrptmc.common.constant.anobj.AnalyseObjectConstants
 *  kd.hr.hrptmc.common.model.anobj.AnObjGroupField
 *  kd.hr.hrptmc.common.model.anobj.AnObjSideBar
 *  kd.hr.hrptmc.common.model.anobj.EntityRelationBo
 *  kd.hr.hrptmc.common.model.anobj.JoinEntityBo
 *  kd.hr.hrptmc.common.model.anobj.QueryFieldBo
 *  kd.hr.hrptmc.common.model.calfield.CalculateFieldBo
 *  kd.hr.hrptmc.opplugin.validator.anobj.ReportAnalyseObjectValidator
 */
package kd.hr.hrptmc.opplugin.web.anobj;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DataEntityBase;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.ISVInfo;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.id.ID;
import kd.bos.lang.Lang;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.schedule.api.JobInfo;
import kd.bos.schedule.api.JobType;
import kd.bos.schedule.executor.JobClient;
import kd.bos.servicehelper.ISVServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.model.complexobj.labelandreport.JoinConditionBo;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrptmc.business.anobj.AnObjDataStoreTaskServiceHelper;
import kd.hr.hrptmc.business.anobj.AnObjGroupFieldService;
import kd.hr.hrptmc.business.anobj.AnObjHisVersionService;
import kd.hr.hrptmc.business.anobj.AnObjPermRuleService;
import kd.hr.hrptmc.business.anobj.AnObjSideBarService;
import kd.hr.hrptmc.business.anobj.AnalyseObjectService;
import kd.hr.hrptmc.business.calfield.CalculateFieldService;
import kd.hr.hrptmc.common.constant.anobj.AnalyseObjectConstants;
import kd.hr.hrptmc.common.model.anobj.AnObjGroupField;
import kd.hr.hrptmc.common.model.anobj.AnObjSideBar;
import kd.hr.hrptmc.common.model.anobj.EntityRelationBo;
import kd.hr.hrptmc.common.model.anobj.JoinEntityBo;
import kd.hr.hrptmc.common.model.anobj.QueryFieldBo;
import kd.hr.hrptmc.common.model.calfield.CalculateFieldBo;
import kd.hr.hrptmc.opplugin.validator.anobj.ReportAnalyseObjectValidator;

public final class ReportAnalyseObjectOp
extends HRDataBaseOp
implements AnalyseObjectConstants {
    private static final Log LOGGER = LogFactory.getLog(ReportAnalyseObjectOp.class);

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new ReportAnalyseObjectValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        if (HRStringUtils.equals((String)args.getOperationKey(), (String)"save")) {
            this.save(args);
        } else if (HRStringUtils.equals((String)args.getOperationKey(), (String)"delete")) {
            this.delete(args);
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        DynamicObject anObjDy;
        if (HRStringUtils.equals((String)args.getOperationKey(), (String)"save") && !"filesource".equals((anObjDy = args.getDataEntities()[0]).getString("objecttype"))) {
            long anObjId = anObjDy.getLong("id");
            JobInfo jobInfo = new JobInfo();
            jobInfo.setRunByLang(Lang.get());
            jobInfo.setAppId("hrptmc");
            jobInfo.setJobType(JobType.REALTIME);
            jobInfo.setName("AnalyseObjectDimCountTaskForOne");
            jobInfo.setId(UUID.randomUUID().toString());
            jobInfo.setTaskClassname("kd.hr.hrptmc.business.anobj.AnalyseObjectDimCountTask");
            HashMap params = Maps.newHashMapWithExpectedSize((int)16);
            params.put("anObjId", String.valueOf(anObjId));
            jobInfo.setParams((Map)params);
            JobClient.dispatch((JobInfo)jobInfo);
        }
    }

    private void save(BeginOperationTransactionArgs args) {
        String joinEntitiesStr = this.getOption().getVariableValue("joinEntities", "");
        String queryFieldsStr = this.getOption().getVariableValue("queryFields", "");
        String entityRelationsStr = this.getOption().getVariableValue("entityRelations", "");
        String calFieldsStr = this.getOption().getVariableValue("calculateFields", "");
        String sideBarStr = this.getOption().getVariableValue("sideBar", "");
        String groupFieldStr = this.getOption().getVariableValue("cache_group_fields", "");
        List joinEntities = Collections.emptyList();
        List queryFields = Collections.emptyList();
        List entityRelations = Collections.emptyList();
        List calculatedFields = Collections.emptyList();
        List sideBars = Collections.emptyList();
        List anObjGroupFields = Collections.emptyList();
        if (HRStringUtils.isNotEmpty((String)joinEntitiesStr)) {
            joinEntities = JSON.parseArray((String)joinEntitiesStr, JoinEntityBo.class);
        }
        if (HRStringUtils.isNotEmpty((String)queryFieldsStr)) {
            queryFields = JSON.parseArray((String)queryFieldsStr, QueryFieldBo.class);
        }
        if (HRStringUtils.isNotEmpty((String)entityRelationsStr)) {
            entityRelations = JSON.parseArray((String)entityRelationsStr, EntityRelationBo.class);
        }
        if (HRStringUtils.isNotEmpty((String)calFieldsStr)) {
            calculatedFields = JSON.parseArray((String)calFieldsStr, CalculateFieldBo.class);
        }
        if (HRStringUtils.isNotEmpty((String)sideBarStr)) {
            sideBars = JSON.parseArray((String)sideBarStr, AnObjSideBar.class);
        }
        if (HRStringUtils.isNotEmpty((String)groupFieldStr)) {
            anObjGroupFields = JSON.parseArray((String)groupFieldStr, AnObjGroupField.class);
        }
        ISVInfo isvInfo = ISVServiceHelper.getISVInfo();
        String isvId = isvInfo.getId();
        DynamicObject anObjDy = args.getDataEntities()[0];
        Map<Object, DynamicObject> joinEntityLoadMap = Collections.emptyMap();
        Map<Object, DynamicObject> queryFieldLoadMap = Collections.emptyMap();
        Map<Object, DynamicObject> entityRelationLoadMap = Collections.emptyMap();
        boolean fromDatabase = anObjDy.getDataEntityState().getFromDatabase();
        if (fromDatabase) {
            AnalyseObjectService service = AnalyseObjectService.getInstance();
            DynamicObject[] joinEntityLoads = service.loadJoinEntities(Long.valueOf(anObjDy.getLong("id")));
            joinEntityLoadMap = Arrays.stream(joinEntityLoads).collect(Collectors.toMap(DataEntityBase::getPkValue, dy -> dy));
            DynamicObject[] queryFieldLoads = service.loadQueryFields(Long.valueOf(anObjDy.getLong("id")));
            queryFieldLoadMap = Arrays.stream(queryFieldLoads).collect(Collectors.toMap(DataEntityBase::getPkValue, dy -> dy));
            DynamicObject[] entityRelationLoads = service.loadEntityRelations(Long.valueOf(anObjDy.getLong("id")));
            entityRelationLoadMap = Arrays.stream(entityRelationLoads).collect(Collectors.toMap(DataEntityBase::getPkValue, dy -> dy));
        } else {
            long anObjId = anObjDy.getLong("id");
            if (anObjId == 0L) {
                anObjId = ORM.create().genLongId("hrptmc_analyseobject");
                anObjDy.set("id", (Object)anObjId);
            }
        }
        DynamicObject[] joinEntityCol = this.assembleAndSaveJoinEntities(anObjDy, joinEntities, joinEntityLoadMap, isvId);
        this.assembleAndSaveQueryFields(anObjDy, queryFields, queryFieldLoadMap, isvId);
        this.assembleAndSaveEntityRelations(anObjDy, entityRelations, entityRelationLoadMap, joinEntityCol, isvId);
        CalculateFieldService.getInstance().saveCalculateFieldsForAnObj(fromDatabase, Long.valueOf(anObjDy.getLong("id")), calculatedFields);
        AnObjGroupFieldService.getInstance().saveGroupFields(Long.valueOf(anObjDy.getLong("id")), anObjGroupFields, queryFields, calculatedFields);
        List pivotConfigDys = AnalyseObjectService.getInstance().saveAnObjPivotConfig(anObjDy);
        AnObjSideBarService.getInstance().saveSideBars(anObjDy.getLong("id"), sideBars, anObjGroupFields);
        String copyAnObjId = this.getOption().getVariableValue("copyId", null);
        if (HRStringUtils.isNotEmpty((String)copyAnObjId)) {
            AnObjPermRuleService.getInstance().copyPermRules(anObjDy.getLong("id"), Long.parseLong(copyAnObjId));
        }
        List analysePivotIndexes = AnalyseObjectService.getInstance().getAnalysePivotIndexes(pivotConfigDys.toArray(new DynamicObject[0]), queryFields, calculatedFields, false);
        AnObjHisVersionService.getInstance().saveAnObjHisVersionConfigBo(anObjDy, anObjDy.getString("objecttype"), joinEntities, entityRelations, queryFields, calculatedFields, analysePivotIndexes, anObjGroupFields, anObjDy.getString("datafilter"));
    }

    private void delete(BeginOperationTransactionArgs args) {
        List anObjIds = Arrays.stream(args.getDataEntities()).map(dy -> dy.get("id")).collect(Collectors.toList());
        AnObjDataStoreTaskServiceHelper.deleteAnoSchedule(anObjIds);
        QFilter anObjQFilter = new QFilter("anobj", "in", anObjIds);
        HRBaseServiceHelper joinEntityHelper = new HRBaseServiceHelper("hrptmc_anobjjoinentity");
        joinEntityHelper.deleteByFilter(new QFilter[]{anObjQFilter});
        HRBaseServiceHelper queryFieldHelper = new HRBaseServiceHelper("hrptmc_anobjqueryfield");
        queryFieldHelper.deleteByFilter(new QFilter[]{anObjQFilter});
        HRBaseServiceHelper entityRelationHelper = new HRBaseServiceHelper("hrptmc_anobjentityrel");
        entityRelationHelper.deleteByFilter(new QFilter[]{anObjQFilter});
        HRBaseServiceHelper calFieldHelper = new HRBaseServiceHelper("hrptmc_calculatefield");
        calFieldHelper.deleteByFilter(new QFilter[]{anObjQFilter});
        AnObjPermRuleService.getInstance().deletePermRuleByAnObjId(anObjIds);
        AnObjGroupFieldService.getInstance().deleteGroupFieldsByAnObjId(anObjIds);
        AnalyseObjectService.getInstance().deleteAnObjPivotConfig(anObjIds);
        AnObjSideBarService.getInstance().deleteSideBars(anObjIds);
        AnObjHisVersionService.getInstance().updateVersionWhenDeleteAnObj(anObjIds);
    }

    private DynamicObject[] assembleAndSaveJoinEntities(DynamicObject anObjDy, List<JoinEntityBo> joinEntities, Map<Object, DynamicObject> joinEntityLoadMap, String isvId) {
        HRBaseServiceHelper joinEntityHelper = new HRBaseServiceHelper("hrptmc_anobjjoinentity");
        DynamicObjectCollection joinEntityCol = new DynamicObjectCollection();
        Set<Long> ids = joinEntities.stream().map(joinEntityBo -> this.getLongId(joinEntityBo.getId())).collect(Collectors.toSet());
        List<Object> deleteIds = this.getDeleteId(ids, joinEntityLoadMap);
        for (JoinEntityBo joinEntity : joinEntities) {
            DynamicObject joinDy = joinEntityLoadMap.get(this.getLongId(joinEntity.getId()));
            if (joinDy == null) {
                joinDy = joinEntityHelper.generateEmptyDynamicObject();
            }
            joinDy.set("anobj", anObjDy.get("id"));
            joinDy.set("index", (Object)joinEntity.getIndex());
            joinDy.set("longnumber", (Object)joinEntity.getLongNumber());
            joinDy.set("entitynumber", (Object)joinEntity.getEntityNumber());
            joinDy.set("entityalias", (Object)joinEntity.getEntityAlias());
            joinDy.set("type", (Object)joinEntity.getType());
            joinDy.set("isv", (Object)isvId);
            joinEntityCol.add((Object)joinDy);
        }
        joinEntityHelper.delete(deleteIds.toArray(new Object[0]));
        return (DynamicObject[])joinEntityHelper.save(joinEntityCol);
    }

    private void assembleAndSaveQueryFields(DynamicObject anObjDy, List<QueryFieldBo> queryFields, Map<Object, DynamicObject> queryFieldLoadMap, String isvId) {
        HRBaseServiceHelper queryFieldHelper = new HRBaseServiceHelper("hrptmc_anobjqueryfield");
        DynamicObjectCollection queryFieldCol = new DynamicObjectCollection();
        Set<Long> ids = queryFields.stream().map(queryFieldBo -> this.getLongId(queryFieldBo.getId())).collect(Collectors.toSet());
        List<Object> deleteIds = this.getDeleteId(ids, queryFieldLoadMap);
        Map<String, DynamicObject> fieldAliasToFieldLoadMap = queryFieldLoadMap.values().stream().collect(Collectors.toMap(dy -> dy.getString("fieldalias"), dy -> dy));
        long newIdCount = queryFields.stream().filter(field -> HRStringUtils.isEmpty((String)field.getId())).count();
        long[] newIds = new long[1];
        int idIndex = 0;
        if (newIdCount > 0L) {
            newIds = ID.genLongIds((int)Integer.parseInt(String.valueOf(newIdCount)));
        }
        for (QueryFieldBo queryField : queryFields) {
            DynamicObject queryFieldDy = queryField.getFieldAlias().endsWith(".id") ? fieldAliasToFieldLoadMap.get(queryField.getFieldAlias()) : queryFieldLoadMap.get(this.getLongId(queryField.getId()));
            if (queryFieldDy == null) {
                queryFieldDy = queryFieldHelper.generateEmptyDynamicObject();
                long newId = newIds[idIndex++];
                queryFieldDy.set("id", (Object)newId);
                queryField.setId(String.valueOf(newId));
            }
            queryFieldDy.set("anobj", anObjDy.get("id"));
            queryFieldDy.set("entitynumber", (Object)queryField.getEntityNumber());
            queryFieldDy.set("fieldname", (Object)queryField.getFieldName());
            queryFieldDy.set("fieldalias", (Object)queryField.getFieldAlias());
            queryFieldDy.set("fieldpath", (Object)queryField.getFieldPath());
            queryFieldDy.set("valuetype", (Object)queryField.getValueType());
            queryFieldDy.set("complextype", (Object)queryField.getComplexType());
            queryFieldDy.set("controltype", (Object)queryField.getControlType());
            queryFieldDy.set("storefield", (Object)queryField.getStoreField());
            queryFieldDy.set("isvirtualfield", (Object)queryField.isVirtualEntityField());
            queryFieldDy.set("isv", (Object)isvId);
            queryFieldCol.add((Object)queryFieldDy);
        }
        queryFieldHelper.delete(deleteIds.toArray(new Object[0]));
        queryFieldHelper.save(queryFieldCol);
    }

    private void assembleAndSaveEntityRelations(DynamicObject anObjDy, List<EntityRelationBo> entityRelations, Map<Object, DynamicObject> entityRelationLoadMap, DynamicObject[] joinEntityCol, String isvId) {
        HRBaseServiceHelper entityRelationHelper = new HRBaseServiceHelper("hrptmc_anobjentityrel");
        DynamicObjectCollection entityRelationCol = new DynamicObjectCollection();
        Set<Long> ids = entityRelations.stream().map(entityRelationBo -> this.getLongId(entityRelationBo.getId())).collect(Collectors.toSet());
        List<Object> deleteIds = this.getDeleteId(ids, entityRelationLoadMap);
        Map<Object, DynamicObject> joinEntityMap = Arrays.stream(joinEntityCol).collect(Collectors.toMap(entity -> entity.get("entityalias"), entity -> entity));
        for (EntityRelationBo entityRelation : entityRelations) {
            DynamicObject entityRelationDy = entityRelationLoadMap.get(this.getLongId(entityRelation.getId()));
            if (entityRelationDy == null) {
                entityRelationDy = entityRelationHelper.generateEmptyDynamicObject();
            }
            entityRelationDy.set("anobj", anObjDy.get("id"));
            DynamicObject entityDy = joinEntityMap.get(entityRelation.getEntityAlias());
            entityRelationDy.set("entityid", entityDy.getPkValue());
            entityRelationDy.set("jointype", (Object)entityRelation.getJoinType());
            DynamicObject joinEntityDy = joinEntityMap.get(entityRelation.getJoinEntityAlias());
            entityRelationDy.set("joinentityid", joinEntityDy.getPkValue());
            entityRelationDy.set("isv", (Object)isvId);
            DynamicObjectCollection joinConditionCol = entityRelationHelper.generateEmptyEntryCollection(entityRelationDy, "joinconditions");
            List joinConditions = entityRelation.getConditions();
            int index = 0;
            for (JoinConditionBo joinCondition : joinConditions) {
                DynamicObject joinConditionDy = entityRelationHelper.generateEmptyEntryDynamicObject("joinconditions");
                joinConditionDy.set("seq", (Object)index++);
                joinConditionDy.set("leftprop", (Object)joinCondition.getLeftProp());
                joinConditionDy.set("comparetype", (Object)joinCondition.getCompareType());
                joinConditionDy.set("rightprop", (Object)joinCondition.getRightProp());
                joinConditionDy.set("rightproptype", (Object)joinCondition.getRightPropType());
                joinConditionDy.set("rightpropval", (Object)joinCondition.getRightPropVal());
                joinConditionDy.set("logictype", (Object)joinCondition.getLogicType());
                joinConditionCol.add((Object)joinConditionDy);
            }
            entityRelationDy.set("joinconditions", (Object)joinConditionCol);
            entityRelationCol.add((Object)entityRelationDy);
        }
        entityRelationHelper.delete(deleteIds.toArray(new Object[0]));
        entityRelationHelper.save(entityRelationCol);
    }

    private List<Object> getDeleteId(Set<Long> ids, Map<Object, DynamicObject> dyMap) {
        ArrayList deleteIds = Lists.newArrayListWithCapacity((int)10);
        for (DynamicObject dy : dyMap.values()) {
            Long id = dy.getLong("id");
            if (ids.contains(id)) continue;
            deleteIds.add(id);
        }
        return deleteIds;
    }

    private Long getLongId(String strId) {
        if (HRStringUtils.isEmpty((String)strId)) {
            return 0L;
        }
        return Long.valueOf(strId);
    }
}
