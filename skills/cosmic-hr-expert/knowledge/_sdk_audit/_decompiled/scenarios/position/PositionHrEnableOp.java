/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication
 *  kd.hrmp.hbpm.business.domain.position.service.IBosPositionService
 *  kd.hrmp.hbpm.business.domain.position.service.impl.ChangeMsgServiceImpl
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionChangeEventQueryRepository
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository
 */
package kd.hrmp.hbpm.opplugin.web.position;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication;
import kd.hrmp.hbpm.business.domain.position.service.IBosPositionService;
import kd.hrmp.hbpm.business.domain.position.service.impl.ChangeMsgServiceImpl;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionChangeEventQueryRepository;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository;

public class PositionHrEnableOp
extends AbstractOperationServicePlugIn {
    private Map<Long, Long> boToBeforeIdMap = new HashMap<Long, Long>();

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("parent");
        fieldKeys.add("adminorg");
        args.getFieldKeys().clear();
        ArrayList fieldsNameList = Lists.newArrayListWithExpectedSize((int)this.billEntityType.getFields().size());
        DataEntityPropertyCollection collection = this.billEntityType.getProperties();
        collection.forEach(entityProperty -> {
            String propertyName = entityProperty.getName();
            if (!HRStringUtils.equals((String)propertyName, (String)"multilanguagetext") && !propertyName.endsWith("_id")) {
                fieldsNameList.add(propertyName);
            }
        });
        args.getFieldKeys().addAll(fieldsNameList);
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        List boIdList = Arrays.stream(args.getDataEntities()).map(dyn -> dyn.getLong("boid")).collect(Collectors.toList());
        this.boToBeforeIdMap = PositionQueryRepository.getInstance().getBoToLatestHisIdMap(boIdList);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] positions = args.getDataEntities();
        List afterVersionids = Arrays.stream(positions).map(dyn -> dyn.getLong("sourcevid")).collect(Collectors.toList());
        DynamicObject[] afterVersions = PositionQueryRepository.getInstance().queryPositionsById(afterVersionids);
        PositionChangeEventQueryRepository instance = PositionChangeEventQueryRepository.getInstance();
        DynamicObject changeType = instance.queryChangeType(Long.valueOf(1070L));
        DynamicObject changeOperationDy = instance.queryChangeOperation(Long.valueOf(1070L));
        DynamicObject changeSceneDy = instance.queryChangeScene(Long.valueOf(1070L));
        for (DynamicObject afterVersion : afterVersions) {
            afterVersion.set("sourcevid", (Object)this.boToBeforeIdMap.getOrDefault(afterVersion.getLong("boid"), 0L));
            afterVersion.set("changetype", (Object)changeType);
            afterVersion.set("changeoperate", (Object)changeOperationDy);
            afterVersion.set("changescene", (Object)changeSceneDy);
        }
        IPositionHrServiceApplication.getInstance().afterSavePosition(afterVersions);
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        ChangeMsgServiceImpl changeMsgServiceImpl = new ChangeMsgServiceImpl();
        changeMsgServiceImpl.sendMsg();
        IBosPositionService.getInstance().commonSyncPositions();
    }
}
