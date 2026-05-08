/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.servicehelper.operation.OperationServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.model.complexobj.labelandreport.EntityRelationCommonBo
 *  kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper
 *  kd.hr.hrcs.bussiness.service.label.LabelService
 *  kd.hr.hrcs.common.model.label.LabelJoinEntityCommonBo
 *  kd.hr.hrcs.opplugin.validator.label.LabelObjectValidator
 */
package kd.hr.hrcs.opplugin.web.label;

import com.alibaba.fastjson.JSONArray;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.model.complexobj.labelandreport.EntityRelationCommonBo;
import kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper;
import kd.hr.hrcs.bussiness.service.label.LabelService;
import kd.hr.hrcs.common.model.label.LabelJoinEntityCommonBo;
import kd.hr.hrcs.opplugin.validator.label.LabelObjectValidator;

public final class LabelObjectOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("publishstatus");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new LabelObjectValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        if ("save".equals(e.getOperationKey())) {
            for (DynamicObject dataEntity : dataEntities) {
                Long pkValue = (Long)dataEntity.getPkValue();
                if (pkValue != null && 0L != pkValue) continue;
                dataEntity.set("publishstatus", (Object)"10");
            }
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        if ("enablestatus".equals(e.getOperationKey())) {
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_labelobject");
            Date date = new Date();
            for (DynamicObject dataEntity : dataEntities) {
                dataEntity.set("publishstatus", (Object)"1");
                dataEntity.set("modifytime", (Object)date);
            }
            serviceHelper.save(dataEntities);
        } else if ("disablestatus".equals(e.getOperationKey())) {
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_labelobject");
            Date date = new Date();
            for (DynamicObject dataEntity : dataEntities) {
                dataEntity.set("publishstatus", (Object)"0");
                dataEntity.set("modifytime", (Object)date);
            }
            serviceHelper.save(dataEntities);
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        if ("save".equals(e.getOperationKey())) {
            this.saveOthers(e.getDataEntities());
        } else if ("delete".equals(e.getOperationKey())) {
            List idList = Arrays.stream(e.getDataEntities()).map(dyn -> dyn.getLong("id")).collect(Collectors.toList());
            DynamicObject[] fields = LabelObjectServiceHelper.getFields(idList);
            OperationServiceHelper.executeOperate((String)"delete", (String)"hrcs_lblobjectfield", (DynamicObject[])fields, (OperateOption)OperateOption.create());
            DynamicObject[] fieldTypes = LabelObjectServiceHelper.getFieldTypes(idList);
            OperationServiceHelper.executeOperate((String)"delete", (String)"hrcs_lblfieldtype", (DynamicObject[])fieldTypes, (OperateOption)OperateOption.create());
            DynamicObject[] entityRelations = LabelObjectServiceHelper.getEntityRelations(idList);
            OperationServiceHelper.executeOperate((String)"delete", (String)"hrcs_lblentityrelation", (DynamicObject[])entityRelations, (OperateOption)OperateOption.create());
            DynamicObject[] joinEntities = LabelObjectServiceHelper.getJoinEntities(idList);
            OperationServiceHelper.executeOperate((String)"delete", (String)"hrcs_lbljoinentity", (DynamicObject[])joinEntities, (OperateOption)OperateOption.create());
            DynamicObject[] lblObjConfigs = LabelObjectServiceHelper.getLblObjConfigs(idList);
            OperationServiceHelper.executeOperate((String)"delete", (String)"hrcs_lblobjconfig", (DynamicObject[])lblObjConfigs, (OperateOption)OperateOption.create());
        }
    }

    private void saveOthers(DynamicObject[] dataEntities) {
        for (DynamicObject dataEntity : dataEntities) {
            Long pkValue = (Long)dataEntity.getPkValue();
            String entityStr = (String)this.getOption().getVariables().get("entityNodes");
            String fieldStr = (String)this.getOption().getVariables().get("queryFields");
            List joinEntityCommonBoList = JSONArray.parseArray((String)entityStr, LabelJoinEntityCommonBo.class);
            List queryFieldCommonBoList = JSONArray.parseArray((String)fieldStr, QueryFieldCommonBo.class);
            List fieldAliases = queryFieldCommonBoList.stream().map(QueryFieldCommonBo::getFieldAlias).collect(Collectors.toList());
            String unSlcFieldStr = (String)this.getOption().getVariables().get("unSlcFields");
            if (HRStringUtils.isNotEmpty((String)unSlcFieldStr)) {
                List unSlcQueryFieldCommonBoList = JSONArray.parseArray((String)unSlcFieldStr, QueryFieldCommonBo.class);
                unSlcQueryFieldCommonBoList.removeIf(bo -> fieldAliases.contains(bo.getFieldAlias()));
                queryFieldCommonBoList.addAll(unSlcQueryFieldCommonBoList);
            }
            LabelService labelService = new LabelService();
            labelService.saveEntityAndField(pkValue, joinEntityCommonBoList, queryFieldCommonBoList);
            String mainObjBoStr = (String)this.getOption().getVariables().get("mainLabelObjBo");
            String mainObjRelBoStr = (String)this.getOption().getVariables().get("mainLabelObjRel");
            if (HRStringUtils.isEmpty((String)mainObjBoStr) || HRStringUtils.isEmpty((String)mainObjRelBoStr)) continue;
            LabelJoinEntityCommonBo entityCommonBo = (LabelJoinEntityCommonBo)SerializationUtils.fromJsonString((String)mainObjBoStr, LabelJoinEntityCommonBo.class);
            EntityRelationCommonBo relationCommonBo = (EntityRelationCommonBo)SerializationUtils.fromJsonString((String)mainObjRelBoStr, EntityRelationCommonBo.class);
            labelService.saveMainLblObjRel(pkValue, entityCommonBo, relationCommonBo);
        }
    }
}
