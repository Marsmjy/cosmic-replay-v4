/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.utils.OrmUtils
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.metadata.AbstractElement
 *  kd.bos.metadata.dao.MetadataReader
 *  kd.bos.metadata.domainmodel.define.DomainModelTypeFactory
 *  kd.bos.metadata.form.ControlAp
 *  kd.bos.metadata.form.DesignFormMeta
 *  kd.bos.metadata.form.FormMetadata
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.bos.servicehelper.devportal.BizAppServiceHelp
 *  kd.bos.servicehelper.devportal.BizCloudServiceHelp
 *  kd.bos.servicehelper.user.UserServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.servicehelper.prompt.PromptServiceHelper
 *  kd.hr.hrcs.opplugin.validator.PromptRuleImportValidator
 */
package kd.hr.hrcs.opplugin.web;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.OrmUtils;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.metadata.AbstractElement;
import kd.bos.metadata.dao.MetadataReader;
import kd.bos.metadata.domainmodel.define.DomainModelTypeFactory;
import kd.bos.metadata.form.ControlAp;
import kd.bos.metadata.form.DesignFormMeta;
import kd.bos.metadata.form.FormMetadata;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.bos.servicehelper.devportal.BizAppServiceHelp;
import kd.bos.servicehelper.devportal.BizCloudServiceHelp;
import kd.bos.servicehelper.user.UserServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.servicehelper.prompt.PromptServiceHelper;
import kd.hr.hrcs.opplugin.validator.PromptRuleImportValidator;

public final class PromptRuleBatchImportSaveOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new PromptRuleImportValidator());
        Arrays.stream(args.getDataEntities()).forEach(dy -> dy.set("controledit", dy.get("controlnumber")));
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        MetadataReader reader = new MetadataReader(false);
        this.buildPromptRuleDy(reader, args.getDataEntities());
    }

    private void buildPromptRuleDy(MetadataReader reader, DynamicObject[] dys) {
        HashSet entityIdSet = Sets.newHashSetWithExpectedSize((int)dys.length);
        for (DynamicObject dy : dys) {
            entityIdSet.add(dy.getDynamicObject("businessobject").getString("dentityid"));
        }
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("bos_entitymeta");
        DynamicObject[] results = serviceHelper.query(new QFilter[]{new QFilter("id", "in", (Object)entityIdSet)});
        Map<String, DynamicObject> entityMap = Arrays.stream(results).collect(Collectors.toMap(result -> result.getString("id"), result -> result));
        for (DynamicObject dy : dys) {
            HRBaseServiceHelper ruleHelper;
            DynamicObject formDy = dy.getDynamicObject("businessobject");
            if (Objects.isNull(dy.get("entity"))) {
                MainEntityType entityType = MetadataServiceHelper.getDataEntityType((String)formDy.getString("number"));
                dy.set("entity", HRStringUtils.isEmpty((String)entityType.getAlias()) ? null : entityMap.get(formDy.getString("dentityid")));
            }
            String appId = BizAppServiceHelp.getAppIdByFormNum((String)formDy.getString("number"));
            String cloudId = BizCloudServiceHelp.getBizCloudByFormID((String)formDy.getString("dentityid")).getString("id");
            dy.set("bonumber", (Object)(cloudId + "#" + appId + "#" + "dentityid"));
            String controlNumber = dy.getString("controlnumber");
            if (HRStringUtils.isNotEmpty((String)controlNumber)) {
                String extEntityId = PromptServiceHelper.getExtEntityId((String)formDy.getString("dentityid"));
                FormMetadata formMeta = (FormMetadata)reader.readMeta(extEntityId, OrmUtils.getDataEntityType(DesignFormMeta.class));
                Map<String, ControlAp> controlNumMap = formMeta.getItems().stream().collect(Collectors.toMap(AbstractElement::getKey, controlAp -> controlAp));
                ControlAp controlAp2 = controlNumMap.get(controlNumber);
                dy.set("controlname", (Object)controlAp2.getName());
                dy.set("controltype", (Object)DomainModelTypeFactory.getDomainModelType((String)formMeta.getModelType(), (boolean)true).getElementType(controlAp2.getClass().getSimpleName()).getName());
            }
            if ((ruleHelper = new HRBaseServiceHelper("hrcs_promptrule")).isExists(new QFilter[]{new QFilter("number", "=", (Object)dy.getString("number"))})) continue;
            dy.set("enable", (Object)"1");
            dy.set("status", (Object)"C");
            dy.set("creator", (Object)UserServiceHelper.getCurrentUserId());
            dy.set("createtime", (Object)new Date());
            dy.set("modifytime", (Object)new Date());
        }
    }
}
