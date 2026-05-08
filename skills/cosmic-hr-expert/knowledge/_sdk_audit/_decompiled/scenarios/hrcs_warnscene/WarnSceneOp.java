/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DataEntityBase
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.model.complexobj.labelandreport.JoinConditionBo
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.WarningSceneService
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.WarnCalcFieldService
 *  kd.hr.hrcs.common.constants.earlywarn.WarningSceneConstants
 *  kd.hr.hrcs.common.model.earlywarn.WarnCalFieldBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnEntityRelationBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnJoinEntityBo
 *  kd.hr.hrcs.opplugin.validator.earlywarn.scene.WarnSceneValidator
 */
package kd.hr.hrcs.opplugin.web.earlywarn.scene;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DataEntityBase;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.model.complexobj.labelandreport.JoinConditionBo;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.WarningSceneService;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.WarnCalcFieldService;
import kd.hr.hrcs.common.constants.earlywarn.WarningSceneConstants;
import kd.hr.hrcs.common.model.earlywarn.WarnCalFieldBo;
import kd.hr.hrcs.common.model.earlywarn.WarnEntityRelationBo;
import kd.hr.hrcs.common.model.earlywarn.WarnJoinEntityBo;
import kd.hr.hrcs.opplugin.validator.earlywarn.scene.WarnSceneValidator;

@ExcludeFromJacocoGeneratedReport
public final class WarnSceneOp
extends HRDataBaseOp
implements WarningSceneConstants {
    private static final Log LOGGER = LogFactory.getLog(WarnSceneOp.class);

    public void onAddValidators(AddValidatorsEventArgs args) {
        try {
            super.onAddValidators(args);
            args.addValidator((AbstractValidator)new WarnSceneValidator());
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneOp_onAddValidators_error:", (Throwable)exception);
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        try {
            super.beginOperationTransaction(args);
            if (HRStringUtils.equals((String)args.getOperationKey(), (String)"save")) {
                this.save(args);
            } else if (HRStringUtils.equals((String)args.getOperationKey(), (String)"delete")) {
                this.delete(args);
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneOp_beginOperationTransaction_error:", (Throwable)exception);
        }
    }

    private void save(BeginOperationTransactionArgs args) {
        DynamicObject anObjDy = args.getDataEntities()[0];
        boolean fromDatabase = anObjDy.getDataEntityState().getFromDatabase();
        String joinEntitiesStr = this.getOption().getVariableValue("joinEntities", "");
        String entityRelationsStr = this.getOption().getVariableValue("entityRelations", "");
        String calFieldsStr = this.getOption().getVariableValue("calculateFields", "");
        List<WarnJoinEntityBo> joinEntities = Collections.emptyList();
        List<WarnJoinEntityBo> objTplJoinEntities = Collections.emptyList();
        List<WarnEntityRelationBo> entityRelations = Collections.emptyList();
        List calculatedFields = Collections.emptyList();
        if (HRStringUtils.isNotEmpty((String)joinEntitiesStr)) {
            joinEntities = JSON.parseArray((String)joinEntitiesStr, WarnJoinEntityBo.class);
            objTplJoinEntities = joinEntities.stream().filter(je -> je.isCore()).collect(Collectors.toList());
            joinEntities = joinEntities.stream().filter(je -> !je.isCore()).collect(Collectors.toList());
        }
        if (HRStringUtils.isNotEmpty((String)entityRelationsStr)) {
            entityRelations = JSON.parseArray((String)entityRelationsStr, WarnEntityRelationBo.class);
            entityRelations = entityRelations.stream().filter(je -> !"warnobjtpl".equals(je.getSource())).collect(Collectors.toList());
        }
        if (HRStringUtils.isNotEmpty((String)calFieldsStr)) {
            calculatedFields = JSON.parseArray((String)calFieldsStr, WarnCalFieldBo.class);
            calculatedFields = calculatedFields.stream().filter(je -> {
                if (HRStringUtils.isEmpty((String)je.getSourceId())) {
                    return true;
                }
                if (!je.isEdited() && je.getSelected().booleanValue() && !String.valueOf(anObjDy.getPkValue()).equals(je.getSourceId())) {
                    je.setSourceId(null);
                    je.setId(null);
                    return true;
                }
                return String.valueOf(anObjDy.getPkValue()).equals(je.getSourceId());
            }).collect(Collectors.toList());
        }
        Map<Object, DynamicObject> joinEntityLoadMap = Collections.emptyMap();
        Map<Object, DynamicObject> entityRelationLoadMap = Collections.emptyMap();
        if (fromDatabase) {
            WarningSceneService service = WarningSceneService.getInstance();
            DynamicObject[] joinEntityLoads = service.loadSceneJoinEntities((Object)anObjDy.getLong("id"));
            joinEntityLoadMap = Arrays.stream(joinEntityLoads).collect(Collectors.toMap(DataEntityBase::getPkValue, dy -> dy, (oldValue, newValue) -> newValue));
            DynamicObject[] entityRelationLoads = service.loadSceneEntityRelations((Object)anObjDy.getLong("id"));
            entityRelationLoadMap = Arrays.stream(entityRelationLoads).collect(Collectors.toMap(DataEntityBase::getPkValue, dy -> dy, (oldValue, newValue) -> newValue));
        } else {
            long warnSceneId = anObjDy.getLong("id");
            if (warnSceneId == 0L) {
                warnSceneId = ORM.create().genLongId("hrcs_warnscene");
                anObjDy.set("id", (Object)warnSceneId);
            }
        }
        DynamicObject[] joinEntityCol = this.assembleAndSaveJoinEntities(anObjDy, joinEntities, joinEntityLoadMap, objTplJoinEntities);
        this.assembleAndSaveEntityRelations(anObjDy, entityRelations, entityRelationLoadMap, joinEntityCol);
        WarnCalcFieldService.getInstance().saveCalFieldsForScene(fromDatabase, (Object)anObjDy.getLong("id"), calculatedFields);
    }

    private void delete(BeginOperationTransactionArgs args) {
        List warnSceneIds = Arrays.stream(args.getDataEntities()).map(dy -> dy.get("id")).collect(Collectors.toList());
        HRBaseServiceHelper joinEntityHelper = new HRBaseServiceHelper("hrcs_warnscenejoinentity");
        joinEntityHelper.deleteByFilter(new QFilter[]{new QFilter("sourceid", "in", warnSceneIds)});
        HRBaseServiceHelper queryFieldHelper = new HRBaseServiceHelper("hrcs_warnscenequeryfield");
        queryFieldHelper.deleteByFilter(new QFilter[]{new QFilter("sourceid", "in", warnSceneIds)});
        HRBaseServiceHelper entityRelationHelper = new HRBaseServiceHelper("hrcs_warnsceneentityrel");
        entityRelationHelper.deleteByFilter(new QFilter[]{new QFilter("sourceid", "in", warnSceneIds)});
        HRBaseServiceHelper calFieldHelper = new HRBaseServiceHelper("hrcs_warncalfield");
        calFieldHelper.deleteByFilter(new QFilter[]{new QFilter("sourceid", "in", warnSceneIds)});
    }

    private DynamicObject[] assembleAndSaveJoinEntities(DynamicObject anObjDy, List<WarnJoinEntityBo> joinEntities, Map<Object, DynamicObject> joinEntityLoadMap, List<WarnJoinEntityBo> objTplJoinEntities) {
        DynamicObject joinDy;
        HRBaseServiceHelper joinEntityHelper = new HRBaseServiceHelper("hrcs_warnscenejoinentity");
        DynamicObjectCollection joinEntityCol = new DynamicObjectCollection();
        DynamicObjectCollection allJoinEntityCol = new DynamicObjectCollection();
        Set<Long> ids = joinEntities.stream().map(joinEntityBo -> this.getLongId(joinEntityBo.getId())).collect(Collectors.toSet());
        List<Object> deleteIds = this.getDeleteId(ids, joinEntityLoadMap);
        for (WarnJoinEntityBo joinEntity : joinEntities) {
            joinDy = joinEntityLoadMap.get(this.getLongId(joinEntity.getId()));
            if (joinDy == null) {
                joinDy = joinEntityHelper.generateEmptyDynamicObject();
            }
            joinDy.set("source", (Object)"warnscene");
            joinDy.set("sourceid", anObjDy.getPkValue());
            joinDy.set("index", (Object)joinEntity.getIndex());
            joinDy.set("longnumber", (Object)joinEntity.getLongNumber());
            joinDy.set("entitynumber", (Object)joinEntity.getEntityNumber());
            joinDy.set("entityalias", (Object)joinEntity.getEntityAlias());
            joinDy.set("type", (Object)joinEntity.getType());
            joinEntityCol.add((Object)joinDy);
            allJoinEntityCol.add((Object)joinDy);
        }
        joinEntityHelper.delete(deleteIds.toArray(new Object[0]));
        joinEntityHelper.save(joinEntityCol);
        for (WarnJoinEntityBo joinEntity : objTplJoinEntities) {
            joinDy = joinEntityHelper.generateEmptyDynamicObject();
            joinDy.set("id", (Object)joinEntity.getId());
            joinDy.set("iscore", (Object)joinEntity.isCore());
            joinDy.set("source", (Object)joinEntity.getSource());
            joinDy.set("sourceid", (Object)joinEntity.getSourceId());
            joinDy.set("index", (Object)joinEntity.getIndex());
            joinDy.set("longnumber", (Object)joinEntity.getLongNumber());
            joinDy.set("entitynumber", (Object)joinEntity.getEntityNumber());
            joinDy.set("entityalias", (Object)joinEntity.getEntityAlias());
            joinDy.set("type", (Object)joinEntity.getType());
            allJoinEntityCol.add((Object)joinDy);
        }
        return (DynamicObject[])allJoinEntityCol.toArray((Object[])new DynamicObject[allJoinEntityCol.size()]);
    }

    private void assembleAndSaveEntityRelations(DynamicObject anObjDy, List<WarnEntityRelationBo> entityRelations, Map<Object, DynamicObject> entityRelationLoadMap, DynamicObject[] joinEntityCol) {
        if (joinEntityCol == null || joinEntityCol.length == 0) {
            return;
        }
        HRBaseServiceHelper entityRelationHelper = new HRBaseServiceHelper("hrcs_warnsceneentityrel");
        DynamicObjectCollection entityRelationCol = new DynamicObjectCollection();
        Set<Long> ids = entityRelations.stream().map(entityRelationBo -> this.getLongId(entityRelationBo.getId())).collect(Collectors.toSet());
        List<Object> deleteIds = this.getDeleteId(ids, entityRelationLoadMap);
        Map<Object, DynamicObject> joinEntityMap = Arrays.stream(joinEntityCol).collect(Collectors.toMap(entity -> entity.get("entityalias"), entity -> entity, (oldValue, newValue) -> newValue));
        for (WarnEntityRelationBo entityRelation : entityRelations) {
            DynamicObject entityRelationDy = entityRelationLoadMap.get(this.getLongId(entityRelation.getId()));
            if (entityRelationDy == null) {
                entityRelationDy = entityRelationHelper.generateEmptyDynamicObject();
            }
            entityRelationDy.set("sourceid", anObjDy.getPkValue());
            entityRelationDy.set("source", (Object)"warnscene");
            DynamicObject entityDy = joinEntityMap.get(entityRelation.getEntityAlias());
            entityRelationDy.set("entityid", entityDy.getPkValue());
            entityRelationDy.set("jointype", (Object)entityRelation.getJoinType());
            DynamicObject joinEntityDy = joinEntityMap.get(entityRelation.getJoinEntityAlias());
            entityRelationDy.set("joinentityid", joinEntityDy.getPkValue());
            DynamicObjectCollection joinConditionCol = entityRelationHelper.generateEmptyEntryCollection(entityRelationDy, "joinconditions");
            List joinConditions = entityRelation.getConditions();
            for (JoinConditionBo joinCondition : joinConditions) {
                DynamicObject joinConditionDy = entityRelationHelper.generateEmptyEntryDynamicObject("joinconditions");
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
