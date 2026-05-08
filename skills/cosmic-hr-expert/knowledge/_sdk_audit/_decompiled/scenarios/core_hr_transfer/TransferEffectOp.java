/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer
 *  kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hdm.business.domain.utils.FieldUtil
 *  kd.hr.hdm.business.infrastructure.client.hpfs.DevParamConfigExternalService
 *  kd.hr.hdm.common.transfer.enums.TransferInStatusEnum
 *  kd.hr.hdm.common.transfer.enums.TransferOutStatusEnum
 *  kd.hr.hdm.common.transfer.enums.TransferTypeEnum
 *  kd.hr.hdm.common.transfer.util.TransferPageHelperUtil
 *  kd.hr.hdm.common.util.HdmAppConfigUtil
 *  kd.hr.hpfs.business.infrastructure.client.hrpi.EmpJobRelRepository
 *  kd.sdk.hr.hdm.common.enums.transfer.TransferOriginatorEnum
 *  kd.sdk.hr.hpfs.business.perchg.executor.enums.ChgModeEnum
 *  kd.sdk.hr.hpfs.utils.ChgRecordOpHelper
 *  kd.sdk.hr.hpfs.utils.ChgUtils
 *  kd.sdk.hr.hpfs.utils.PerChgNewBillUtils
 *  kd.sdk.hr.hrpi.common.enums.EmployeeModelConfigTypeEnum
 */
package kd.hr.hdm.opplugin.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer;
import kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hdm.business.domain.utils.FieldUtil;
import kd.hr.hdm.business.infrastructure.client.hpfs.DevParamConfigExternalService;
import kd.hr.hdm.common.transfer.enums.TransferInStatusEnum;
import kd.hr.hdm.common.transfer.enums.TransferOutStatusEnum;
import kd.hr.hdm.common.transfer.enums.TransferTypeEnum;
import kd.hr.hdm.common.transfer.util.TransferPageHelperUtil;
import kd.hr.hdm.common.util.HdmAppConfigUtil;
import kd.hr.hpfs.business.infrastructure.client.hrpi.EmpJobRelRepository;
import kd.sdk.hr.hdm.common.enums.transfer.TransferOriginatorEnum;
import kd.sdk.hr.hpfs.business.perchg.executor.enums.ChgModeEnum;
import kd.sdk.hr.hpfs.utils.ChgRecordOpHelper;
import kd.sdk.hr.hpfs.utils.ChgUtils;
import kd.sdk.hr.hpfs.utils.PerChgNewBillUtils;
import kd.sdk.hr.hrpi.common.enums.EmployeeModelConfigTypeEnum;

public final class TransferEffectOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(TransferEffectOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        evt.getFieldKeys().addAll(TransferPageHelperUtil.getInstance().getSingleAllFieldsList());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        long transferType;
        super.beforeExecuteOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities.length == 0) {
            return;
        }
        Map variables = this.getOption().getVariables();
        if (!variables.containsKey("autoEffect") && !HRStringUtils.equals((String)args.getOperationKey(), (String)"donothing_retry") && (transferType = dataEntities[0].getLong("transfertype.id")) != TransferTypeEnum.BATCH.getType()) {
            DevParamConfigExternalService devParamConfigExternalService = DevParamConfigExternalService.getInstance();
            String autoEffect = devParamConfigExternalService.getValueByKey("hdm_transfer_hpfs_autoeffect", "true");
            if (HRStringUtils.equals((String)autoEffect, (String)"false")) {
                LOG.warn("auto effect config switched off, transfer effective terminate");
            }
            this.getOption().setVariableValue("autoEffect", autoEffect);
        }
        if (this.isNotAutoEffect()) {
            LOG.warn("not auto effect");
            return;
        }
        Map futureEffectConfigMap = PerChgNewBillUtils.getFutureEffectConfigMap((DynamicObject[])args.getDataEntities());
        Arrays.stream(dataEntities).filter(entity -> this.isEffectTransfer((DynamicObject)entity, futureEffectConfigMap)).forEach(dataEntity -> {
            FieldUtil.coverFieldValue((DynamicObject)dataEntity, (String)"ba_po_adminorg", (Object)dataEntity.getDynamicObject("aorg"));
            FieldUtil.coverFieldValue((DynamicObject)dataEntity, (String)"ba_po_position", (Object)dataEntity.getDynamicObject("aposition"));
            FieldUtil.coverFieldValue((DynamicObject)dataEntity, (String)"ba_po_job", (Object)dataEntity.getDynamicObject("ajob"));
            Date effectiveDate = dataEntity.getDate("b_effectivedate");
            FieldUtil.coverFieldValue((DynamicObject)dataEntity, (String)"ba_po_startdate", (Object)effectiveDate);
            FieldUtil.setDefaultValue((DynamicObject)dataEntity, (String)"ba_po_isprimary", (Object)dataEntity.getBoolean("bb_po_isprimary"));
            FieldUtil.setDefaultValue((DynamicObject)dataEntity, (String)"ba_po_ctrworkplace", (Object)dataEntity.getDynamicObject("bb_po_ctrworkplace"));
            if (!HRStringUtils.equals((String)dataEntity.getString("ba_a_chgtype"), (String)"2")) {
                FieldUtil.setDefaultValue((DynamicObject)dataEntity, (String)"bb_a_startdate", (Object)effectiveDate);
                FieldUtil.coverFieldValue((DynamicObject)dataEntity, (String)"bb_a_org", (Object)dataEntity.get("ba_a_org"));
                FieldUtil.coverFieldValue((DynamicObject)dataEntity, (String)"bb_a_empgroup", (Object)dataEntity.get("ba_a_empgroup"));
                FieldUtil.coverFieldValue((DynamicObject)dataEntity, (String)"bb_a_manageadminorg", (Object)dataEntity.get("ba_a_manageadminorg"));
                boolean isPrimary = dataEntity.getBoolean("ba_po_isprimary");
                DynamicObject aCountry = dataEntity.getDynamicObject("ba_a_country");
                if (!HRObjectUtils.isEmpty((Object)aCountry) && isPrimary) {
                    FieldUtil.coverFieldValue((DynamicObject)dataEntity, (String)"bb_a_country", (Object)aCountry);
                }
            } else {
                FieldUtil.setDefaultValue((DynamicObject)dataEntity, (String)"ba_a_startdate", (Object)effectiveDate);
            }
        });
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        int length = dataEntities.length;
        if (length == 0) {
            return;
        }
        boolean notAutoEffect = this.isNotAutoEffect();
        ArrayList effectTransferBills = new ArrayList(length);
        Map futureEffectConfigMap = PerChgNewBillUtils.getFutureEffectConfigMap((DynamicObject[])args.getDataEntities());
        Arrays.stream(dataEntities).forEach(dataEntity -> {
            if (this.isEffectTransfer((DynamicObject)dataEntity, futureEffectConfigMap)) {
                effectTransferBills.add(dataEntity);
                if (notAutoEffect) {
                    dataEntity.set("transfereffectstatus", (Object)"0");
                } else {
                    dataEntity.set("transfereffectstatus", (Object)"1");
                }
            } else {
                dataEntity.set("transfereffectstatus", (Object)"0");
            }
            dataEntity.set("transferstatus", (Object)"4");
            dataEntity.set("billstatus", (Object)"C");
            String originator = dataEntity.getString("originator");
            if (HRStringUtils.equals((String)originator, (String)TransferOriginatorEnum.OUT.getOriginator()) || HRStringUtils.equals((String)originator, (String)TransferOriginatorEnum.IN.getOriginator())) {
                dataEntity.set("transferoutstatus", (Object)TransferOutStatusEnum.transferOutApproved.getApproveStatus());
                dataEntity.set("transferinstatus", (Object)TransferInStatusEnum.transferInApproved.getApproveStatus());
            }
        });
        if (notAutoEffect || effectTransferBills.isEmpty()) {
            LOG.warn("isNotAutoEffect or effectTransferBills is empty");
            ChgRecordOpHelper.clearEffectChgRecordIds();
            return;
        }
        HashSet affRecordIds = new HashSet(length);
        ArrayList employeeIds = new ArrayList(length);
        effectTransferBills.forEach(dataEntity -> {
            affRecordIds.add(dataEntity.getLong("affrecord"));
            employeeIds.add(dataEntity.getLong("bb_em_tid.id"));
        });
        LOG.info("affRecordIds:{}", affRecordIds);
        Map affRecordMap = ChgRecordOpHelper.getChgRecords();
        if (affRecordMap == null || affRecordMap.isEmpty()) {
            LOG.warn("affRecordMap is empty");
            return;
        }
        DynamicObject[] empJobRelDys = EmpJobRelRepository.getInstance().getEmpJobRelByEmpIds(employeeIds, null, null);
        Map empJobRelMap = empJobRelDys.length > 0 ? Arrays.stream(empJobRelDys).collect(Collectors.toMap(dy -> dy.getLong("employee.id"), dy -> dy, (v1, v2) -> v1)) : Collections.emptyMap();
        String privacyPropJson = ChgUtils.getPrivacyPropJson();
        boolean evaluationJobIsCtr = HdmAppConfigUtil.getEvaluationJobIsCtr();
        Map billFileMapManagerMap = ChgUtils.batchGetBillFileMapManager(effectTransferBills);
        effectTransferBills.forEach(dataEntity -> {
            long employeeId = dataEntity.getLong("bb_em_tid.id");
            long affRecordId = dataEntity.getLong("affrecord");
            DynamicObject affRecord = (DynamicObject)affRecordMap.get(affRecordId);
            DynamicObject empJobRel = (DynamicObject)empJobRelMap.get(employeeId);
            this.handleEmpJobRel((DynamicObject)dataEntity, affRecord, empJobRel, privacyPropJson, evaluationJobIsCtr, (DynamicObject)billFileMapManagerMap.get(dataEntity.getLong("affaction.id")));
            this.handleChargePerson((DynamicObject)dataEntity, affRecord, privacyPropJson);
        });
    }

    private boolean isHandleEmpJobRel(DynamicObject transferBill) {
        long bEvaluationJobId = transferBill.getLong("bevaluationjob.id");
        long bJobGradeId = transferBill.getLong("bjobgrade.id");
        long bJobLevelId = transferBill.getLong("bjoblevel.id");
        long aEvaluationJobId = transferBill.getLong("aevaluationjob.id");
        long aJobGradeId = transferBill.getLong("ajobgrade.id");
        long aJobLevelId = transferBill.getLong("ajoblevel.id");
        if (aJobGradeId == 0L && aJobLevelId == 0L) {
            return false;
        }
        return bEvaluationJobId != aEvaluationJobId || bJobGradeId != aJobGradeId || bJobLevelId != aJobLevelId;
    }

    private void handleEmpJobRel(DynamicObject transferBill, DynamicObject affRecord, DynamicObject oldEmpJobRel, String privacyPropJson, boolean evaluationJobIsCtr, DynamicObject fileMapping) {
        if (!this.isHandleEmpJobRel(transferBill)) {
            LOG.info("not handle emp job rel");
            return;
        }
        DynamicObjectCollection entry = affRecord.getDynamicObjectCollection("paramentry");
        DynamicObjectCollection attParamEntry = affRecord.getDynamicObjectCollection("attparamentry");
        List noEmpJobRelEntry = entry.stream().filter(dy -> HRStringUtils.equals((String)dy.getString("paramchgentity.number"), (String)"hrpi_empjobrel")).collect(Collectors.toList());
        entry.removeAll(noEmpJobRelEntry);
        if (!HRObjectUtils.isEmpty((Object)oldEmpJobRel)) {
            DynamicObject newEntryItem = entry.addNew();
            String dataBefore = ChgUtils.setDataBefore((DynamicObject)newEntryItem, (DynamicObject)oldEmpJobRel, (String)privacyPropJson);
            Date effectDate = transferBill.getDate("b_effectivedate");
            oldEmpJobRel.set("enddate", (Object)HRDateTimeUtils.addDay((Date)effectDate, (long)-1L));
            if (!evaluationJobIsCtr) {
                oldEmpJobRel.set("orgrelseq", null);
                oldEmpJobRel.set("position", null);
            }
            ChgUtils.processTargetEntityMapping((DynamicObject)transferBill, (DynamicObject)oldEmpJobRel, (DynamicObject)fileMapping, (DynamicObjectCollection)attParamEntry, (boolean)Boolean.TRUE);
            String employeeModel = EmployeeModelConfigTypeEnum.ASSIGNMENT_ATTACH_MODEL.getCode();
            String timelineModelType = TimelineModelTypeEnum.INTERRUPTION_OVERLAP.getType();
            ChgUtils.buildChgRecordEntry((DynamicObject)newEntryItem, (String)"1", (String)"hrpi_empjobrel", (DynamicObject)oldEmpJobRel, (DynamicObject)oldEmpJobRel, (String)dataBefore, (String)employeeModel, (String)timelineModelType, (String)privacyPropJson);
        }
        DynamicObject newEmpJobRel = this.newAfterEmpJobRel(transferBill, evaluationJobIsCtr);
        ChgUtils.processTargetEntityMapping((DynamicObject)transferBill, (DynamicObject)newEmpJobRel, (DynamicObject)fileMapping, (DynamicObjectCollection)attParamEntry, (boolean)Boolean.FALSE);
        DynamicObject newEntryItem = entry.addNew();
        String employeeModel = EmployeeModelConfigTypeEnum.ASSIGNMENT_ATTACH_MODEL.getCode();
        String timelineModelType = TimelineModelTypeEnum.INTERRUPTION_OVERLAP.getType();
        ChgUtils.buildChgRecordEntry((DynamicObject)newEntryItem, (String)"0", (String)"hrpi_empjobrel", (DynamicObject)oldEmpJobRel, (DynamicObject)newEmpJobRel, null, (String)employeeModel, (String)timelineModelType, (String)privacyPropJson);
    }

    private void handleChargePerson(DynamicObject transferBill, DynamicObject affRecord, String privacyPropJson) {
        DynamicObject empPosOrgRel = transferBill.getDynamicObject("ba_po_tid");
        if (HRObjectUtils.isEmpty((Object)empPosOrgRel)) {
            LOG.warn("empPosOrgRel is empty");
            return;
        }
        long bEmpPosOrgRelId = transferBill.getLong("bb_po_tid.id");
        long adminOrgId = transferBill.getLong("ba_po_adminorg.boid");
        long bAdminOrgId = transferBill.getLong("bb_po_adminorg.boid");
        boolean positionIsLeader = transferBill.getBoolean("ba_po_position.isleader");
        DynamicObjectCollection chgRecordEntryCol = affRecord.getDynamicObjectCollection("paramentry");
        DynamicObject transferOutChargePersonEntry = null;
        DynamicObject transferOutChargePerson = null;
        DynamicObject transferInChargePersonEntry = null;
        for (DynamicObject chgRecordEntry : chgRecordEntryCol) {
            DynamicObject chargePerson;
            if (!HRStringUtils.equals((String)chgRecordEntry.getString("paramchgentity.number"), (String)"haos_chargeperson") || HRObjectUtils.isEmpty((Object)(chargePerson = CustomDynamicObjectJsonSerializer.parseDynamicObjectJson((DynamicObject)affRecord, (String)chgRecordEntry.getString("paramdataafter_tag"), (String)"haos_chargeperson")))) continue;
            long chargePersonEmpPosOrgRelId = chargePerson.getLong("empposorgrel.id");
            String chgMode = chgRecordEntry.getString("paramchgmode");
            if (HRStringUtils.equals((String)chgMode, (String)ChgModeEnum.ADD_NEW.getChgMode())) {
                transferInChargePersonEntry = chgRecordEntry;
                continue;
            }
            if (chargePersonEmpPosOrgRelId != bEmpPosOrgRelId) continue;
            long chargePersonAdminOrgId = chargePerson.getLong("adminorg.id");
            long chargeSourceId = chargePerson.getLong("changesource.id");
            if (chargePersonAdminOrgId == bAdminOrgId) {
                transferOutChargePersonEntry = chgRecordEntry;
                transferOutChargePerson = chargePerson;
                if (chargeSourceId != 1040L || adminOrgId != bAdminOrgId) continue;
                this.updateChargePersonEmpPosOrgRel(chgRecordEntry, chargePerson, empPosOrgRel, privacyPropJson);
                continue;
            }
            this.updateChargePersonEmpPosOrgRel(chgRecordEntry, chargePerson, empPosOrgRel, privacyPropJson);
        }
        if (adminOrgId == bAdminOrgId && positionIsLeader && transferOutChargePersonEntry != null && transferInChargePersonEntry != null) {
            long chargeSourceId = transferOutChargePerson.getLong("changesource.id");
            if (chargeSourceId != 1040L) {
                this.updateChargePersonEmpPosOrgRel(transferOutChargePersonEntry, transferOutChargePerson, empPosOrgRel, privacyPropJson);
            }
            chgRecordEntryCol.remove(transferInChargePersonEntry);
        }
    }

    private void updateChargePersonEmpPosOrgRel(DynamicObject chgRecordEntry, DynamicObject chargePerson, DynamicObject empPosOrgRel, String privacyPropJson) {
        chargePerson.set("empposorgrel", (Object)empPosOrgRel);
        chargePerson.set("datastatus", (Object)"1");
        chargePerson.set("leffdt", (Object)HRDateTimeUtils.getSysMaxDate());
        String dataAfter = ChgUtils.setDataAfter((DynamicObject)chgRecordEntry, (DynamicObject)chargePerson, (String)privacyPropJson);
        ChgUtils.setDataCompare((DynamicObject)chgRecordEntry, (String)chgRecordEntry.getString("paramdatabefore_tag"), (String)dataAfter);
    }

    private DynamicObject newAfterEmpJobRel(DynamicObject transferBill, boolean evaluationJobIsCtr) {
        DynamicObject newEmpJobRel = EmpJobRelRepository.getInstance().generateEmptyDynamicObject();
        long newEmpJobRelId = ORM.create().genLongId("hrpi_empjobrel");
        newEmpJobRel.set("id", (Object)newEmpJobRelId);
        newEmpJobRel.set("iscurrentdata", (Object)"1");
        DynamicObject beforeEmployee = transferBill.getDynamicObject("bb_em_tid");
        DynamicObject afterEmployee = transferBill.getDynamicObject("bb_em_tid");
        newEmpJobRel.set("employee", (Object)(afterEmployee == null ? beforeEmployee : afterEmployee));
        DynamicObject beforeAssignment = transferBill.getDynamicObject("bb_a_tid");
        long afterAssignmentId = transferBill.getLong("ba_a_tid");
        if (afterAssignmentId == 0L) {
            newEmpJobRel.set("assignment", (Object)beforeAssignment);
        } else {
            DynamicObject afterAssignment = new HRBaseServiceHelper("hrpi_assignment").generateEmptyDynamicObject();
            afterAssignment.set("id", (Object)afterAssignmentId);
            newEmpJobRel.set("assignment", (Object)afterAssignment);
        }
        if (evaluationJobIsCtr) {
            newEmpJobRel.set("orgrelseq", transferBill.get("ba_po_orgrelseq"));
            newEmpJobRel.set("position", transferBill.get("ba_po_positionbo"));
        }
        newEmpJobRel.set("startdate", (Object)transferBill.getDate("b_effectivedate"));
        newEmpJobRel.set("enddate", (Object)HRDateTimeUtils.getSysMaxDate());
        newEmpJobRel.set("adminorg", transferBill.get("ba_po_adminorgbo"));
        newEmpJobRel.set("jobclass", transferBill.get("ajobclass"));
        newEmpJobRel.set("joblevel", transferBill.get("ajoblevel"));
        newEmpJobRel.set("jobgrade", transferBill.get("ajobgrade"));
        newEmpJobRel.set("jobscm", transferBill.get("ajobscm"));
        newEmpJobRel.set("hrbu", transferBill.get("ajoborg"));
        newEmpJobRel.set("jobfamily", transferBill.get("ajobfamily"));
        newEmpJobRel.set("jobseq", transferBill.get("ajobseq"));
        newEmpJobRel.set("joblevelscm", transferBill.get("ajoblevelscm"));
        newEmpJobRel.set("jobgradescm", transferBill.get("ajobgradescm"));
        newEmpJobRel.set("chgaction", transferBill.get("affaction"));
        newEmpJobRel.set("company", transferBill.get("arealitycompany"));
        newEmpJobRel.set("job", transferBill.get("aevaluationjob"));
        return newEmpJobRel;
    }

    private boolean isEffectTransfer(DynamicObject transferBill, Map<Long, Boolean> futureEffectConfigMap) {
        return null != futureEffectConfigMap.get(transferBill.getLong("org.id")) && futureEffectConfigMap.get(transferBill.getLong("org.id")) != false || HRDateTimeUtils.isBeforeOrEqualNow((Date)transferBill.getDate("b_effectivedate"));
    }

    private boolean isNotAutoEffect() {
        Map variables = this.getOption().getVariables();
        return HRStringUtils.equals((String)((String)variables.get("autoEffect")), (String)"false");
    }
}
