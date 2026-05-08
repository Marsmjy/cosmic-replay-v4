/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication
 *  kd.hrmp.hbpm.business.application.service.IPositionRelationServiceApplication
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionChangeEventQueryRepository
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository
 *  kd.hrmp.hbpm.opplugin.web.position.validate.PositionImptRuleNumberValidator
 */
package kd.hrmp.hbpm.opplugin.web.position;

import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.util.CollectionUtils;
import kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication;
import kd.hrmp.hbpm.business.application.service.IPositionRelationServiceApplication;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionChangeEventQueryRepository;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository;
import kd.hrmp.hbpm.opplugin.web.position.PositionHrCommonOp;
import kd.hrmp.hbpm.opplugin.web.position.validate.PositionImptRuleNumberValidator;

public class PositionHrSaveOp
extends PositionHrCommonOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        if (this.isImport()) {
            args.addValidator((AbstractValidator)new PositionImptRuleNumberValidator());
        }
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        String unitPositionMapStr = this.getOption().containsVariable("unitPositionMap") ? this.getOption().getVariableValue("unitPositionMap") : "";
        HashMap unitPositionMap = (HashMap)JSON.parseObject((String)unitPositionMapStr, Map.class);
        if (CollectionUtils.isEmpty((Map)unitPositionMap)) {
            unitPositionMap = new HashMap(0);
        }
        HashMap<String, DynamicObject> newPositionDyMap = new HashMap<String, DynamicObject>(unitPositionMap.size());
        for (DynamicObject pos : e.getDataEntities()) {
            String positionNumber = pos.getString("number");
            pos.set("org", pos.get("adminorg.org"));
            if (!unitPositionMap.containsKey(positionNumber)) continue;
            pos.set("id", unitPositionMap.get(positionNumber));
            newPositionDyMap.put(positionNumber, pos);
        }
        if (CollectionUtils.isEmpty(newPositionDyMap)) {
            return;
        }
        for (DynamicObject pos : e.getDataEntities()) {
            String parentNumber = pos.getString("parent.number");
            if (!StringUtils.isNotEmpty((CharSequence)parentNumber) || !newPositionDyMap.containsKey(parentNumber)) continue;
            pos.set("parent", newPositionDyMap.get(parentNumber));
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] positions = args.getDataEntities();
        IPositionRelationServiceApplication.getInstance().saveSysRelation(Arrays.stream(positions).collect(Collectors.toList()));
        IPositionRelationServiceApplication.getInstance().saveCooperationRelation(Arrays.asList(positions));
        HashMap<Long, DynamicObject> boToDynMap = new HashMap<Long, DynamicObject>(positions.length);
        ArrayList<Long> afterVersionids = new ArrayList<Long>(positions.length);
        for (DynamicObject position : positions) {
            boToDynMap.put(position.getLong("boid"), position);
            afterVersionids.add(position.getLong("sourcevid"));
        }
        DynamicObject[] afterVersions = PositionQueryRepository.getInstance().queryPositionsById(afterVersionids);
        PositionChangeEventQueryRepository instance = PositionChangeEventQueryRepository.getInstance();
        DynamicObject changeType = instance.queryChangeType(Long.valueOf(1010L));
        DynamicObject changeOperationDy = instance.queryChangeOperation(Long.valueOf(1010L));
        DynamicObject changeSceneDy = instance.queryChangeScene(Long.valueOf(1010L));
        for (DynamicObject afterVersion : afterVersions) {
            long boid = afterVersion.getLong("boid");
            DynamicObject curData = (DynamicObject)boToDynMap.get(boid);
            afterVersion.set("changedesc", curData.get("changedesc"));
            afterVersion.set("changedescription", curData.get("changedescription"));
            afterVersion.set("changetype", (Object)changeType);
            afterVersion.set("changeoperate", (Object)changeOperationDy);
            afterVersion.set("changescene", (Object)changeSceneDy);
        }
        IPositionHrServiceApplication.getInstance().afterSavePosition(afterVersions);
    }
}
