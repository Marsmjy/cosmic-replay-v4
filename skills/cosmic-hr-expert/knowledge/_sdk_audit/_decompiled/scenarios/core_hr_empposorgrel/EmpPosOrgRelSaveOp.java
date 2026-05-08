/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.operate.result.OperateErrorInfo
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.entity.validate.ErrorLevel
 *  kd.bos.entity.validate.ValidateResultCollection
 *  kd.bos.entity.validate.ValidationErrorInfo
 *  kd.bos.exception.KDBizException
 *  kd.bos.id.ID
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.service.operation.OperationServiceImpl
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo
 *  kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult
 *  kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum
 *  kd.hr.hbp.common.util.DatePattern
 *  kd.hr.hbp.common.util.DateUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IEmpPosOrgRelApplicationService
 *  kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService
 *  kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentMagDomainService
 *  kd.hrmp.hrpi.business.domain.assigment.service.IEmpPosOrgRelDomainService
 *  kd.hrmp.hrpi.business.domain.init.HRBuHelper
 *  kd.hrmp.hrpi.business.infrastructure.client.adminorg.IAdminOrgService
 *  kd.hrmp.hrpi.business.infrastructure.timeline.EmpPosOrgRelNoIntervalNoOverlapHandler
 *  kd.hrmp.hrpi.business.infrastructure.timeline.dao.TimelineEntityConf
 *  kd.hrmp.hrpi.business.infrastructure.timeline.util.EmpPosOrgRelTimeLineServiceUtil
 *  kd.hrmp.hrpi.common.HRPIValueConstants
 *  kd.hrmp.hrpi.common.entity.assignment.attach.EmpPosOrgRelInitSaveEntity
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelIsPrimaryValidator
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelPositionValidator
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelPostTypeCategoryValidator
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelSeqValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.operate.result.OperateErrorInfo;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.entity.validate.ErrorLevel;
import kd.bos.entity.validate.ValidateResultCollection;
import kd.bos.entity.validate.ValidationErrorInfo;
import kd.bos.exception.KDBizException;
import kd.bos.id.ID;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.service.operation.OperationServiceImpl;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo;
import kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult;
import kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum;
import kd.hr.hbp.common.util.DatePattern;
import kd.hr.hbp.common.util.DateUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IEmpPosOrgRelApplicationService;
import kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService;
import kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentMagDomainService;
import kd.hrmp.hrpi.business.domain.assigment.service.IEmpPosOrgRelDomainService;
import kd.hrmp.hrpi.business.domain.init.HRBuHelper;
import kd.hrmp.hrpi.business.infrastructure.client.adminorg.IAdminOrgService;
import kd.hrmp.hrpi.business.infrastructure.timeline.EmpPosOrgRelNoIntervalNoOverlapHandler;
import kd.hrmp.hrpi.business.infrastructure.timeline.dao.TimelineEntityConf;
import kd.hrmp.hrpi.business.infrastructure.timeline.util.EmpPosOrgRelTimeLineServiceUtil;
import kd.hrmp.hrpi.common.HRPIValueConstants;
import kd.hrmp.hrpi.common.entity.assignment.attach.EmpPosOrgRelInitSaveEntity;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelIsPrimaryValidator;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelPositionValidator;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelPostTypeCategoryValidator;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.EmpPosOrgRelSeqValidator;

public final class EmpPosOrgRelSaveOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(EmpPosOrgRelSaveOp.class);
    private List<Long> removeIds;
    private final IAssignmentDomainService assignmentDomainService = IAssignmentDomainService.getInstance();
    private final IEmpPosOrgRelDomainService empPosOrgRelDomainService = IEmpPosOrgRelDomainService.getInstance();

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("employee.id");
        fieldKeys.add("assignment");
        fieldKeys.add("startdate");
        fieldKeys.add("enddate");
        fieldKeys.add("iscurrentdata");
        fieldKeys.add("islatestrecord");
        fieldKeys.add("adminorg");
        fieldKeys.add("postype");
        fieldKeys.add("posstatus");
        fieldKeys.add("company");
        fieldKeys.add("isprimary");
        fieldKeys.add("adminorgvid");
        fieldKeys.add("position");
        fieldKeys.add("positionvid");
        fieldKeys.add("job");
        fieldKeys.add("jobvid");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        DynamicObject[] dataEntities = args.getDataEntities();
        this.addLog(dataEntities);
        DynamicObject[] empPosOrgRelDynamicObjects = this.getEmpPosOrgRelData(dataEntities);
        args.addValidator((AbstractValidator)new EmpPosOrgRelPositionValidator());
        args.addValidator((AbstractValidator)new EmpPosOrgRelIsPrimaryValidator(empPosOrgRelDynamicObjects));
        args.addValidator((AbstractValidator)new EmpPosOrgRelSeqValidator(empPosOrgRelDynamicObjects));
        args.addValidator((AbstractValidator)new EmpPosOrgRelPostTypeCategoryValidator(empPosOrgRelDynamicObjects));
    }

    private static Map<String, Set<String>> saveManagementOpErrorConvert(List<OperationResult> operationResults, Map<Long, DynamicObject> assignmentMap) {
        HashMap errorMsgMap = Maps.newHashMapWithExpectedSize((int)16);
        operationResults.forEach(opResult -> {
            ValidateResultCollection validateResult = opResult.getValidateResult();
            List validateErrors = validateResult.getValidateErrors();
            validateErrors.forEach(validateError -> {
                List allErrorInfo = validateError.getAllErrorInfo();
                allErrorInfo.forEach(errorInfo -> EmpPosOrgRelSaveOp.handleErrorMsg(errorMsgMap, errorInfo, assignmentMap));
            });
        });
        return errorMsgMap;
    }

    private TimelineHandlerResult saveData(String entityNumber, List<DynamicObject> dataList) {
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
        return timelineHandler.saveBatch(timelineDataGroups);
    }

    private void addErrorInfo(TimelineHandlerResult result, DynamicObject[] dataEntities, BeginOperationTransactionArgs args) {
        if (result.hasError()) {
            ArrayList<Long> errorDyIds = new ArrayList<Long>(result.getFailDataList().size());
            for (int i = 0; i < result.getFailDataList().size(); ++i) {
                DynamicObject errorDy = (DynamicObject)result.getFailDataList().get(i);
                ValidationErrorInfo operateErrorInfo = new ValidationErrorInfo();
                operateErrorInfo.setPkValue((Object)errorDy.getLong("id"));
                operateErrorInfo.setErrorLevel(ErrorLevel.Error.toString());
                operateErrorInfo.setMessage((String)result.getErrorMsgList().get(i));
                this.getOperationResult().addErrorInfo((OperateErrorInfo)operateErrorInfo);
                errorDyIds.add(errorDy.getLong("id"));
            }
            List<DynamicObject> dataEntityList = Arrays.stream(dataEntities).collect(Collectors.toList());
            if (dataEntityList.removeIf(data -> errorDyIds.contains(data.getLong("id")))) {
                args.setDataEntities(dataEntityList.toArray(new DynamicObject[0]));
            }
        }
    }

    private static void handleErrorMsg(Map<String, Set<String>> errorMsgMap, OperateErrorInfo errorInfo, Map<Long, DynamicObject> assignmentMap) {
        if (errorInfo.getLevel() != ErrorLevel.Error && errorInfo.getLevel() != ErrorLevel.FatalError) {
            return;
        }
        Object pkValue = errorInfo.getPkValue();
        Long pkId = pkValue instanceof Integer ? Long.valueOf(((Integer)pkValue).longValue()) : (Long)pkValue;
        String errorMsg = errorInfo.getMessage();
        DynamicObject assignment = assignmentMap.get(pkId);
        String empNumber = assignment.getDynamicObject("employee").getString("empnumber");
        errorMsgMap.computeIfAbsent(empNumber, k -> Sets.newHashSet()).add(errorMsg);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        IEmpPosOrgRelApplicationService empPosOrgRelApplicationService = IEmpPosOrgRelApplicationService.getInstance();
        if (dataEntities != null && dataEntities.length > 0) {
            TimelineHandlerResult saveResult;
            String entityNumber = this.billEntityType.getName();
            empPosOrgRelApplicationService.assignSeq(dataEntities);
            empPosOrgRelApplicationService.setDefaultValue(dataEntities);
            try {
                saveResult = this.saveData(entityNumber, Arrays.stream(dataEntities).collect(Collectors.toList()));
                this.setChangeInfoToOpParam(saveResult);
            }
            catch (Exception exception) {
                LOGGER.error((Throwable)exception);
                throw new KDBizException(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\u3002", (String)"EmpPosOrgRelError_1", (String)"hrmp-hrpi-opplugin", (Object[])new Object[0]));
            }
            this.addErrorInfo(saveResult, dataEntities, e);
            List<DynamicObject> dataEntityList = Arrays.stream(dataEntities).collect(Collectors.toList());
            List removeIds = saveResult.getRemoveIds();
            if (dataEntityList.removeIf(data -> removeIds.contains(data.getLong("id")))) {
                e.setDataEntities(dataEntityList.toArray(new DynamicObject[0]));
                this.removeIds = removeIds;
            }
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        super.endOperationTransaction(e);
        IEmpPosOrgRelApplicationService.getInstance().setLatestRecordValue(e.getDataEntities());
        String isChg = (String)this.getOption().getVariables().get("hr_hpfstag_of_datasource");
        if (HRStringUtils.equals((String)Boolean.TRUE.toString(), (String)isChg)) {
            LOGGER.info("invoker is affairsChange");
        } else {
            IEmpPosOrgRelApplicationService.getInstance().discardAdminOrgLeader(this.removeIds);
            IEmpPosOrgRelApplicationService.getInstance().syncAdminOrgLeader(e.getDataEntities());
        }
        Map variables = this.getOption().getVariables();
        String hrdmInitSaveVar = (String)variables.get("hr_hrdmsynctag_of_datasource");
        if (HRStringUtils.isNotEmpty((String)hrdmInitSaveVar)) {
            LOGGER.info("invoker is hrdmInitSave");
            this.dealWithHrdmInitSave(e.getDataEntities());
        }
    }

    private void dealWithHrdmInitSave(DynamicObject[] dataEntities) {
        Set employeeIdSet = Arrays.stream(dataEntities).map(data -> data.getLong("employee.id")).collect(Collectors.toSet());
        DynamicObject[] empPosOrgArr = this.empPosOrgRelDomainService.loadDynamicObjectArray(new QFilter("employee.id", "in", employeeIdSet).toArray());
        Map empPosOrgRelMap = Arrays.stream(empPosOrgArr).collect(Collectors.groupingBy(dy -> dy.getLong("employee.id"), HashMap::new, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().filter(dyn -> dyn.getBoolean("isprimary") && dyn.getLong("postype.id") == 1010L).sorted(Comparator.comparing(o -> o.getDate("startdate")).thenComparingInt(o -> o.getInt("timeseq"))).collect(Collectors.toList()))));
        EmpPosOrgRelInitSaveEntity context = this.queryContextData(empPosOrgArr);
        LinkedHashMap forwardAssignmentMap = Maps.newLinkedHashMap();
        ArrayList addAssignMagDynList = Lists.newArrayListWithExpectedSize((int)dataEntities.length);
        empPosOrgRelMap.forEach((empId, posOrgRelList) -> {
            for (int i = posOrgRelList.size() - 2; i >= 0; --i) {
                DynamicObject posOrgRelDyBefore = (DynamicObject)posOrgRelList.get(i);
                DynamicObject dynamicObjectAfter = (DynamicObject)posOrgRelList.get(i + 1);
                boolean isCurrentSame = posOrgRelDyBefore.getDate("startdate").equals(posOrgRelDyBefore.getDate("enddate"));
                boolean isSameWithNext = dynamicObjectAfter.getDate("startdate").equals(posOrgRelDyBefore.getDate("startdate"));
                if (!isCurrentSame || !isSameWithNext) continue;
                posOrgRelList.remove(i);
            }
            for (int index = 0; index < posOrgRelList.size(); ++index) {
                DynamicObject posOrgRelDy = (DynamicObject)posOrgRelList.get(index);
                DynamicObject assignment = posOrgRelDy.getDynamicObject("assignment");
                Date startDate = posOrgRelDy.getDate("startdate");
                if (startDate.before(assignment.getDate("startdate"))) {
                    DynamicObject assignmentMngDy = this.createAssignmentMagMagDy(posOrgRelDy, assignment, context);
                    addAssignMagDynList.add(assignmentMngDy);
                    continue;
                }
                Map forwardMap = forwardAssignmentMap.computeIfAbsent(index, HashMap::new);
                forwardMap.put(posOrgRelDy.getLong("assignment.id"), posOrgRelDy);
                forwardAssignmentMap.put(index, forwardMap);
            }
        });
        OperateOption option = this.operateOption.copy();
        option.setVariableValue("ishasright", Boolean.TRUE.toString());
        option.setVariableValue("skipCheckDataPermission", Boolean.TRUE.toString());
        OperationServiceImpl operationService = new OperationServiceImpl();
        if (CollectionUtils.isNotEmpty((Collection)addAssignMagDynList)) {
            OperationResult result = operationService.localInvokeOperation("save", addAssignMagDynList.toArray(new DynamicObject[0]), option);
            Map<Long, DynamicObject> assignmentMagMap = addAssignMagDynList.stream().collect(Collectors.toMap(assignmentMag -> assignmentMag.getLong("id"), assignmentMag -> assignmentMag));
            this.handleErrorDate(Lists.newArrayList((Object[])new OperationResult[]{result}), assignmentMagMap);
        }
        ArrayList operationResults = Lists.newArrayListWithExpectedSize((int)forwardAssignmentMap.keySet().size());
        HashMap assignmentMap = Maps.newHashMapWithExpectedSize((int)dataEntities.length);
        forwardAssignmentMap.forEach((index, forwardMap) -> {
            DynamicObject[] assignmentDys;
            for (DynamicObject assignmentDy : assignmentDys = this.assignmentDomainService.loadDynamicObjectArray(new QFilter("id", "in", forwardMap.keySet()).toArray())) {
                DynamicObject empPosOrgRelDy = (DynamicObject)forwardMap.get(assignmentDy.getLong("id"));
                this.buildAssignment(assignmentDy, empPosOrgRelDy, context);
                assignmentMap.put(assignmentDy.getLong("id"), assignmentDy);
            }
            OperationResult result = operationService.localInvokeOperation("save", assignmentDys, option);
            if (!result.isSuccess()) {
                operationResults.add(result);
            }
        });
        this.handleErrorDate(operationResults, assignmentMap);
    }

    private void handleErrorDate(List<OperationResult> operationResults, Map<Long, DynamicObject> entityMap) {
        Map<String, Set<String>> validateErrorMap = EmpPosOrgRelSaveOp.saveManagementOpErrorConvert(operationResults, entityMap);
        StringBuilder sb = new StringBuilder();
        if (validateErrorMap != null && !validateErrorMap.isEmpty()) {
            String entityName = entityMap.values().stream().findFirst().get().getDataEntityType().getDisplayName().getLocaleValue();
            validateErrorMap.entrySet().stream().forEach(entry -> sb.append((String)entry.getKey()).append("\uff1a").append(String.join((CharSequence)",", (Iterable)entry.getValue())).append(";").append("\n"));
            throw new KDBizException(String.format(ResManager.loadKDString((String)"\u4fdd\u5b58%1$s\u6570\u636e\u5931\u8d25\uff1a%2$s", (String)"EmpPosOrgRelSaveOp_1", (String)"hrmp-hrpi-business", (Object[])new Object[]{entityName, sb}), new Object[0]));
        }
    }

    private EmpPosOrgRelInitSaveEntity queryContextData(DynamicObject[] dataEntities) {
        HashMap adminOrgMap = Maps.newHashMapWithExpectedSize((int)dataEntities.length);
        HashMap adminOrgBuMap = Maps.newHashMapWithExpectedSize((int)dataEntities.length);
        HashMap assignmentMagMap = Maps.newHashMapWithExpectedSize((int)dataEntities.length);
        HashMap businessFieldMap = Maps.newHashMapWithExpectedSize((int)dataEntities.length);
        HashSet adminorgIds = Sets.newHashSetWithExpectedSize((int)dataEntities.length);
        HashSet assignmentIds = Sets.newHashSetWithExpectedSize((int)dataEntities.length);
        for (DynamicObject dataEntity : dataEntities) {
            adminorgIds.add(dataEntity.getLong("adminorg.id"));
            assignmentIds.add(dataEntity.getLong("assignment.id"));
        }
        DynamicObject[] adminOrgs = IAdminOrgService.getInstance().queryOrgDetailDys("id,org,otclassify,firstbsed", new QFilter("id", "in", (Object)adminorgIds), null);
        adminOrgMap.putAll(Arrays.stream(adminOrgs).collect(Collectors.toMap(dy -> dy.getLong("id"), Function.identity(), (l, r) -> r)));
        DynamicObject[] assignmentMags = IAssignmentMagDomainService.getInstance().loadDynamicObjectArray(new QFilter("assignment.id", "in", (Object)assignmentIds).and("iscurrentdata", "=", (Object)Boolean.TRUE).toArray());
        assignmentMagMap.putAll(Arrays.stream(assignmentMags).collect(Collectors.toMap(dy -> dy.getLong("assignment.id"), Function.identity(), (l, r) -> r)));
        adminOrgBuMap.putAll(new HRBuHelper().queryBatchBu(adminOrgMap.keySet()));
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hbss_bussinessfield");
        businessFieldMap.put(HRPIValueConstants.BUSINESS_HR, helper.loadOne(new QFilter("id", "=", (Object)HRPIValueConstants.BUSINESS_HR).toArray()));
        EmpPosOrgRelInitSaveEntity context = new EmpPosOrgRelInitSaveEntity((Map)adminOrgMap, (Map)assignmentMagMap, (Map)adminOrgBuMap, (Map)businessFieldMap);
        return context;
    }

    private void buildAssignment(DynamicObject assignment, DynamicObject empentrelExperience, EmpPosOrgRelInitSaveEntity context) {
        DynamicObject businessFieldDy = (DynamicObject)context.getBusinessFieldDyMap().get(HRPIValueConstants.BUSINESS_HR);
        Map adminOrgBuMap = context.getAdminOrgBuMap();
        assignment.set("startdate", (Object)empentrelExperience.getDate("startdate"));
        DynamicObject dy = empentrelExperience.getDynamicObject("adminorg");
        if (dy != null) {
            DynamicObject adminOrgDy = (DynamicObject)context.getAdminOrgMap().get(dy.getLong("id"));
            assignment.set("org", adminOrgBuMap.get(empentrelExperience.getLong("adminorg.id")));
            if (adminOrgDy != null) {
                assignment.set("orgtype", adminOrgDy.get("otclassify"));
            }
        }
        assignment.set("businesstype", (Object)businessFieldDy);
        assignment.set("adminorg", empentrelExperience.get("adminorg"));
        assignment.set("enable", (Object)"1");
        assignment.set("initdatasource", (Object)"1");
    }

    private DynamicObject createAssignmentMagMagDy(DynamicObject posOrgRelDy, DynamicObject assignment, EmpPosOrgRelInitSaveEntity context) {
        Map adminOrgMap = context.getAdminOrgMap();
        Map assignmentMagMap = context.getAssignmentMagMap();
        Map adminOrgBuMap = context.getAdminOrgBuMap();
        DynamicObject adminOrg = (DynamicObject)adminOrgMap.get(posOrgRelDy.getLong("adminorg.id"));
        DynamicObject adminOrgBu = (DynamicObject)adminOrgBuMap.get(posOrgRelDy.getLong("adminorg.id"));
        DynamicObject currAssignmentMag = (DynamicObject)assignmentMagMap.get(assignment.getLong("id"));
        DynamicObject assignmentMagDy = new DynamicObject(currAssignmentMag.getDynamicObjectType());
        assignmentMagDy.set("id", (Object)ID.genLongId());
        assignmentMagDy.set("startdate", (Object)posOrgRelDy.getDate("startdate"));
        assignmentMagDy.set("enddate", (Object)posOrgRelDy.getDate("enddate"));
        assignmentMagDy.set("assignment", (Object)assignment);
        assignmentMagDy.set("adminorg", (Object)adminOrg);
        assignmentMagDy.set("org", (Object)adminOrgBu);
        assignmentMagDy.set("country", null);
        assignmentMagDy.set("orgtype", adminOrg.get("otclassify"));
        assignmentMagDy.set("primaryassignment", currAssignmentMag.get("primaryassignment"));
        assignmentMagDy.set("isprimary", currAssignmentMag.get("isprimary"));
        assignmentMagDy.set("employee", currAssignmentMag.get("employee"));
        assignmentMagDy.set("initdatasource", (Object)"1");
        return assignmentMagDy;
    }

    private void setChangeInfoToOpParam(TimelineHandlerResult result) {
        TimelineChangeIdInfo changeInfo = new TimelineChangeIdInfo();
        changeInfo.setSaveDataIds(result.getNewOrModifyDataList().stream().map(dy -> dy.getLong("id")).collect(Collectors.toList()));
        changeInfo.setRemoveDataIds(result.getRemoveIds());
        this.getOption().setVariableValue("timeLineOpChangeInfo", SerializationUtils.toJsonString((Object)changeInfo));
    }

    private void addLog(DynamicObject[] dataEntities) {
        if (dataEntities != null) {
            Date maxEffEndDate = TimeLineServiceUtil.getMaxEffEndDate();
            ArrayList params = Lists.newArrayListWithCapacity((int)dataEntities.length);
            for (DynamicObject dataEntity : dataEntities) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("fromDatabase", dataEntity.getDataEntityState().getFromDatabase());
                long id = dataEntity.getLong("id");
                map.put("id", id);
                long employeeId = dataEntity.getLong("employee_id");
                map.put("employeeId", employeeId);
                long assignmentId = dataEntity.getLong("assignment_id");
                map.put("assignmentId", assignmentId);
                int orgRelNo = dataEntity.getInt("orgrelseq");
                map.put("orgRelNo", orgRelNo);
                int timeSeq = dataEntity.getInt("timeseq");
                map.put("timeSeq", timeSeq);
                boolean isPrimary = dataEntity.getBoolean("isprimary");
                map.put("isPrimary", isPrimary);
                long adminOrgId = dataEntity.getLong("adminorg.id");
                map.put("adminOrgId", adminOrgId);
                long jobId = dataEntity.getLong("job_id");
                map.put("jobId", jobId);
                long positionId = dataEntity.getLong("position_id");
                map.put("positionId", positionId);
                Date startDate = dataEntity.getDate("startdate");
                String startDateStr = startDate == null ? "" : DateUtils.dateToString((Date)startDate, (DatePattern)DatePattern.YYYY_MM_DD);
                map.put("startDate", startDateStr);
                Date endDate = dataEntity.getDate("enddate");
                if (null == endDate) {
                    dataEntity.set("enddate", (Object)maxEffEndDate);
                }
                String endDateStr = endDate == null ? "" : DateUtils.dateToString((Date)endDate, (DatePattern)DatePattern.YYYY_MM_DD);
                map.put("endDate", endDateStr);
                params.add(map);
            }
            LOGGER.info("EmpPosOrgRelSaveOp save onlyValidate|{} params|{}", (Object)this.getOption().getVariables().getOrDefault("onlyvalidate", Boolean.FALSE.toString()), (Object)params);
        }
    }

    private DynamicObject[] getEmpPosOrgRelData(DynamicObject[] dataEntities) {
        Set assignmentIds = Arrays.stream(dataEntities).map(dy -> dy.getLong("assignment_id")).collect(Collectors.toSet());
        String selectFields = "id,employee.id,assignment.id,assignment.orgtype.id,postype.id,postype.postcategory.id,isprimary,orgrelseq,timeseq,enddate";
        return IEmpPosOrgRelApplicationService.getInstance().queryOriginalArray(selectFields, new QFilter[]{new QFilter("assignment", "in", assignmentIds)});
    }
}
