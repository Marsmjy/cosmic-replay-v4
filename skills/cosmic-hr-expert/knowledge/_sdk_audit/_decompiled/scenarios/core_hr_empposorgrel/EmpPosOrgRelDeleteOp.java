/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.service.operation.validate.BaseDataDeleteValidator
 *  kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo
 *  kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult
 *  kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IEmpPosOrgRelApplicationService
 *  kd.hrmp.hrpi.business.infrastructure.timeline.EmpPosOrgRelNoIntervalNoOverlapHandler
 *  kd.hrmp.hrpi.business.infrastructure.timeline.dao.TimelineEntityConf
 *  kd.hrmp.hrpi.business.infrastructure.timeline.util.EmpPosOrgRelTimeLineServiceUtil
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelDeleteKeepOneValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.service.operation.validate.BaseDataDeleteValidator;
import kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo;
import kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult;
import kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IEmpPosOrgRelApplicationService;
import kd.hrmp.hrpi.business.infrastructure.timeline.EmpPosOrgRelNoIntervalNoOverlapHandler;
import kd.hrmp.hrpi.business.infrastructure.timeline.dao.TimelineEntityConf;
import kd.hrmp.hrpi.business.infrastructure.timeline.util.EmpPosOrgRelTimeLineServiceUtil;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelDeleteKeepOneValidator;

public final class EmpPosOrgRelDeleteOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(EmpPosOrgRelDeleteOp.class);

    public void onAddValidators(AddValidatorsEventArgs e) {
        e.getValidators().removeIf(abstractValidator -> abstractValidator instanceof BaseDataDeleteValidator);
        e.getValidators().add(new EmpPosOrgRelDeleteKeepOneValidator());
    }

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("startdate");
        e.getFieldKeys().add("enddate");
        e.getFieldKeys().add("isdeleted");
        e.getFieldKeys().add("islatestrecord");
        e.getFieldKeys().add("iscurrentdata");
        e.getFieldKeys().add("isprimary");
        e.getFieldKeys().add("orgrelseq");
        e.getFieldKeys().add("timeseq");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        String isChg = (String)this.getOption().getVariables().get("hr_hpfstag_of_datasource");
        if (HRStringUtils.equals((String)Boolean.TRUE.toString(), (String)isChg)) {
            LOG.info("invoker is affairsChange");
        } else {
            List removeIds = Stream.of(e.getDataEntities()).map(dy -> dy.getLong("id")).collect(Collectors.toList());
            IEmpPosOrgRelApplicationService.getInstance().discardAdminOrgLeader(removeIds);
        }
        String entityNumber = this.billEntityType.getName();
        TimelineHandlerResult result = this.deleteData(entityNumber, Arrays.stream(e.getDataEntities()).collect(Collectors.toList()));
        this.setChangeInfoToOpParam(result);
    }

    private TimelineHandlerResult deleteData(String entityNumber, List<DynamicObject> dataList) {
        TimelineEntityConf entityConfig = new TimelineEntityConf();
        HashSet logicSet = new HashSet(3);
        Collections.addAll(logicSet, "assignment", "isprimary", "orgrelseq");
        entityConfig.setLogicKey(logicSet);
        entityConfig.setModelTypeEnum(TimelineModelTypeEnum.NOINTERRUPTION_NOOVERLAP);
        entityConfig.setCover(Boolean.TRUE);
        entityConfig.setStartDateLimit(Boolean.TRUE);
        String allowChangeInSameDay = this.getOption().getVariables().getOrDefault("allowChangeInSameDay", Boolean.TRUE.toString());
        EmpPosOrgRelNoIntervalNoOverlapHandler timelineHandler = new EmpPosOrgRelNoIntervalNoOverlapHandler(entityNumber, entityConfig, Boolean.parseBoolean(allowChangeInSameDay), true);
        List timelineDataGroups = EmpPosOrgRelTimeLineServiceUtil.buildTimeLineDataGroups((String)entityNumber, (Set)entityConfig.getLogicKey(), dataList);
        EmpPosOrgRelTimeLineServiceUtil.addDeletedDynamicObjectToTimeLineDataGroups((List)timelineDataGroups);
        return timelineHandler.deleteBatch(timelineDataGroups);
    }

    private void setChangeInfoToOpParam(TimelineHandlerResult result) {
        TimelineChangeIdInfo changeInfo = new TimelineChangeIdInfo();
        changeInfo.setSaveDataIds(result.getNewOrModifyDataList().stream().map(dy -> dy.getLong("id")).collect(Collectors.toList()));
        changeInfo.setRemoveDataIds(result.getRemoveIds());
        this.getOption().setVariableValue("timeLineOpChangeInfo", SerializationUtils.toJsonString((Object)changeInfo));
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        IEmpPosOrgRelApplicationService.getInstance().setLatestRecordValue(dataEntities);
    }
}
