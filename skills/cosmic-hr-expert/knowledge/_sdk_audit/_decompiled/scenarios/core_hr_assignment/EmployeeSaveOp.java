/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.property.IntegerProp
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.service.operation.OperationServiceImpl
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.service.history.util.HisModelCopyUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.api.HrApiResponse
 *  kd.hr.hbp.common.model.history.param.HisCreateVersionParam
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.infrastructure.utils.EmployeeBatchOperationContextUtil
 *  kd.hrmp.hrpi.business.infrastructure.utils.PersonUserUtils
 *  kd.hrmp.hrpi.business.infrastructure.utils.QFilterUtil
 *  kd.hrmp.hrpi.common.enums.BosGenderEnum
 *  kd.hrmp.hrpi.common.util.HRPIDynamicObjectUtil
 *  kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper
 */
package kd.hrmp.hrpi.opplugin.web.employee;

import com.alibaba.fastjson.JSON;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.property.IntegerProp;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.service.operation.OperationServiceImpl;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.business.service.history.util.HisModelCopyUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.api.HrApiResponse;
import kd.hr.hbp.common.model.history.param.HisCreateVersionParam;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.infrastructure.utils.EmployeeBatchOperationContextUtil;
import kd.hrmp.hrpi.business.infrastructure.utils.PersonUserUtils;
import kd.hrmp.hrpi.business.infrastructure.utils.QFilterUtil;
import kd.hrmp.hrpi.common.enums.BosGenderEnum;
import kd.hrmp.hrpi.common.util.HRPIDynamicObjectUtil;
import kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper;

public final class EmployeeSaveOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(EmployeeSaveOp.class);
    private Map<Long, DynamicObject> employeeNewGlobalPersonDys = new ConcurrentHashMap<Long, DynamicObject>();

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List propertySet = e.getFieldKeys();
        propertySet.add("boid");
        propertySet.add("id");
        propertySet.add("globalperson");
        propertySet.add("oldempnumber");
        propertySet.add("number");
        propertySet.add("empnumber");
        propertySet.add("isprimary");
        propertySet.add("primaryemployee");
        propertySet.add("bsed");
        propertySet.add("bsled");
        propertySet.add("iscurrentversion");
        propertySet.add("index");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        this.setDefaultValues(dataEntities);
    }

    private void setDefaultValues(DynamicObject[] dataEntities) {
        if (dataEntities.length == 0) {
            return;
        }
        IntegerProp indexProperty = (IntegerProp)dataEntities[0].getDataEntityType().getProperties().get((Object)"index");
        Object defValue = indexProperty.getDefValue();
        for (DynamicObject dataEntity : dataEntities) {
            if (dataEntity.getInt("index") != 0) continue;
            dataEntity.set("index", defValue);
        }
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities == null || dataEntities.length < 1) {
            return;
        }
        if ("save".equals(args.getOperationKey()) || "confirmchange".equals(args.getOperationKey())) {
            for (DynamicObject dynamicObject : dataEntities) {
                this.setDefaultHeadSculpture(dynamicObject);
                this.calculateAge(dynamicObject);
                this.setOldGlobalEmployee(dynamicObject);
            }
            this.initGlobalEmployee(dataEntities);
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        if ("save".equals(args.getOperationKey())) {
            this.saveNewGlobalEmployee();
            this.updatePrimaryEmployee(dataEntities);
        }
        if ("confirmchange".equals(args.getOperationKey())) {
            this.updatePrimaryEmployee(dataEntities);
        }
    }

    private void saveNewGlobalEmployee() {
        if (this.employeeNewGlobalPersonDys.isEmpty()) {
            LOGGER.info("not need create new globalpersondys");
            return;
        }
        String chgRecordIdParam = (String)this.operateOption.getVariables().get("chgRecordId");
        if (HRStringUtils.isNotEmpty((String)chgRecordIdParam)) {
            Map map = (Map)JSON.parseObject((String)chgRecordIdParam, Map.class);
            this.employeeNewGlobalPersonDys.forEach((employeeId, globalPersonDy) -> {
                Long chgRecordId = (Long)map.get(employeeId);
                map.put(globalPersonDy.getLong("id"), chgRecordId);
            });
            this.operateOption.setVariableValue("chgRecordId", JSON.toJSONString((Object)map));
        }
        OperationServiceImpl operationService = new OperationServiceImpl();
        this.operateOption.setVariableValue("ishasright", Boolean.TRUE.toString());
        this.operateOption.setVariableValue("skipCheckDataPermission", Boolean.TRUE.toString());
        OperationResult operationResult = operationService.localInvokeOperation("save", this.employeeNewGlobalPersonDys.values().toArray(new DynamicObject[0]), this.operateOption);
        LOGGER.info("EmployeeSaveOp createDefaultGlobalEmployee ->{}", (Object)operationResult);
    }

    private void updatePrimaryEmployee(DynamicObject[] dataEntities) {
        Set globalPersonIdSet = Arrays.stream(dataEntities).filter(dynamicObject -> dynamicObject.getBoolean("isprimary") && dynamicObject.getLong("globalperson.id") != 0L).map(dynamicObject -> dynamicObject.getLong("globalperson.id")).collect(Collectors.toSet());
        Set oldGlobalPersonIdSet = Arrays.stream(dataEntities).filter(dynamicObject -> dynamicObject.getBoolean("isprimary") && dynamicObject.get("oldemployee") != null && dynamicObject.getLong("oldemployee.globalperson.id") != 0L).map(dynamicObject -> dynamicObject.getLong("oldemployee.globalperson.id")).collect(Collectors.toSet());
        Map<Long, Long> oldEmployeeNewEmployeeIdMap = Arrays.stream(dataEntities).filter(dynamicObject -> dynamicObject.getBoolean("isprimary") && dynamicObject.get("oldemployee") != null).collect(Collectors.toMap(dy -> dy.getLong("oldemployee.id"), dy -> dy.getLong("id"), (v1, v2) -> v1));
        Map<Long, Date> newEmployeeEntryDate = this.getNewEmployeeEntryDate();
        LOGGER.info("newEmployeeEntryDate|{},oldEmployeeNewEmployeeIdMap|{}", newEmployeeEntryDate, oldEmployeeNewEmployeeIdMap);
        for (DynamicObject dataEntity : dataEntities) {
            if (dataEntity.getDate("bsed") != null) continue;
            long employeeId = dataEntity.getLong("id");
            DynamicObject[] newEntryDate = Optional.ofNullable(newEmployeeEntryDate.get(employeeId)).orElseGet(Date::new);
            dataEntity.set("bsed", (Object)newEntryDate);
        }
        globalPersonIdSet.addAll(oldGlobalPersonIdSet);
        Set boIdSet = Arrays.stream(dataEntities).map(dynamicObject -> dynamicObject.getLong("boid")).collect(Collectors.toSet());
        QFilter qFilter = new QFilter("globalperson.id", "in", globalPersonIdSet);
        qFilter.and(new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE));
        qFilter.and(new QFilter("datastatus", "=", (Object)"1"));
        qFilter.and(new QFilter("isprimary", "=", (Object)"1"));
        qFilter.and(new QFilter("boid", "not in", boIdSet));
        qFilter.and(QFilterUtil.buildNotDeletedQFilter());
        HRBaseServiceHelper employeeHelper = new HRBaseServiceHelper("hrpi_employee");
        DynamicObject[] oldEmployeeDys = employeeHelper.loadDynamicObjectArray(new QFilter[]{qFilter});
        if (oldEmployeeDys.length <= 0) {
            return;
        }
        HisModelCopyUtil hisModelCopyUtil = new HisModelCopyUtil();
        ArrayList<DynamicObject> updateDynamicObjectList = new ArrayList<DynamicObject>(oldEmployeeDys.length);
        for (DynamicObject oldEmployeeDy : oldEmployeeDys) {
            long employeeId = oldEmployeeDy.getLong("id");
            Long newEmployeeId = oldEmployeeNewEmployeeIdMap.get(employeeId);
            Date newEntryDate = Optional.ofNullable(newEmployeeEntryDate.get(newEmployeeId)).orElseGet(Date::new);
            DynamicObject updateEmployeeDy = hisModelCopyUtil.copyTempVersionData(oldEmployeeDy, employeeHelper);
            updateEmployeeDy.set("bsed", (Object)newEntryDate);
            updateEmployeeDy.set("isprimary", (Object)Boolean.FALSE);
            updateDynamicObjectList.add(updateEmployeeDy);
        }
        HisCreateVersionParam hisCreateVersionParam = new HisCreateVersionParam();
        hisCreateVersionParam.setEntityNumber("hrpi_employee");
        hisCreateVersionParam.setDataList(updateDynamicObjectList);
        HrApiResponse hrApiResponse = HisModelServiceHelper.createDataVersions((HisCreateVersionParam)hisCreateVersionParam);
        LOGGER.info("PersonGenericServiceImpl saveOperate hrApiResponse error ->{}", (Object)hrApiResponse);
        if (!hrApiResponse.isSuccess()) {
            LOGGER.info("PersonGenericServiceImpl saveOperate hrApiResponse markRollback");
            TXHandle.get().markRollback();
        }
    }

    private void initGlobalEmployee(DynamicObject[] dataEntities) {
        HRBaseServiceHelper hrBaseServiceHelper = new HRBaseServiceHelper("hrpi_globalperson");
        for (DynamicObject dynamicObject : dataEntities) {
            DynamicObject globalEmployee = dynamicObject.getDynamicObject("globalperson");
            if (globalEmployee != null) continue;
            globalEmployee = hrBaseServiceHelper.generateEmptyDynamicObject();
            if (StringUtils.isEmpty((CharSequence)globalEmployee.getString("number"))) {
                globalEmployee.set("id", (Object)ORM.create().genLongId("hrpi_globalperson"));
                globalEmployee.set("number", (Object)dynamicObject.getString("empnumber"));
            }
            dynamicObject.set("globalperson", (Object)globalEmployee);
            this.employeeNewGlobalPersonDys.put(dynamicObject.getLong("id"), globalEmployee);
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
    }

    private void calculateAge(DynamicObject dynamicObject) {
        if (null != dynamicObject.get("birthday")) {
            Date birthday = dynamicObject.getDate("birthday");
            BigDecimal age = BigDecimal.valueOf(HRDateTimeUtils.dateDiff((String)"yyyy", (Date)birthday, (Date)new Date()));
            dynamicObject.set("age", (Object)age);
        }
    }

    private void setOldGlobalEmployee(DynamicObject dataEntity) {
        if (null != dataEntity.get("oldemployee") && dataEntity.getDynamicObject("oldemployee").getLong("id") != 0L) {
            DynamicObject globalPerson = dataEntity.getDynamicObject("oldemployee.globalperson");
            dataEntity.set("globalperson", (Object)globalPerson);
            dataEntity.set("oldempnumber", (Object)dataEntity.getString("oldemployee.empnumber"));
        }
    }

    private void setDefaultHeadSculpture(DynamicObject dynamicObject) {
        String headsculpture = dynamicObject.getString("headsculpture");
        if (HRStringUtils.isEmpty((String)headsculpture)) {
            String name = dynamicObject.getString("name");
            headsculpture = PersonUserUtils.createAvatar((String)name, (int)this.convertGender(dynamicObject));
            dynamicObject.set("headsculpture", (Object)headsculpture);
        }
    }

    private int convertGender(DynamicObject dynamicObject) {
        long genderId = 0L;
        IDataEntityType dataEntityType = dynamicObject.getDataEntityType();
        if (Objects.nonNull(dataEntityType) && Objects.nonNull(dataEntityType.getProperties()) && dataEntityType.getProperties().containsKey((Object)"gender")) {
            genderId = HRPIDynamicObjectUtil.getBasicDataValue((DynamicObject)dynamicObject, (String)"gender");
        }
        return BosGenderEnum.hRGenderConvert2BosGender((long)genderId);
    }

    private Map<Long, Date> getNewEmployeeEntryDate() {
        DynamicObjectCollection empEntRelDys = EmployeeBatchOperationContextUtil.getRelatedDataEntities((String)"hrpi_empentrel");
        if (CollectionUtils.isEmpty((Collection)empEntRelDys)) {
            return new HashMap<Long, Date>();
        }
        return empEntRelDys.stream().collect(Collectors.toMap(empEntRel -> empEntRel.getLong("employee.id"), empEntRel -> empEntRel.getDate("entrydate"), (d1, d2) -> d2));
    }
}
