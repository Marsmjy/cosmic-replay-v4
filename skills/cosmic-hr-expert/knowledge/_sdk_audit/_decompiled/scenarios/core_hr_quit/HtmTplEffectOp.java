/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer
 *  kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hpfs.business.application.service.relatiolservice.repository.HelperRepository
 *  kd.sdk.hr.hpfs.utils.ChgRecordOpHelper
 *  kd.sdk.hr.hpfs.utils.ChgUtils
 *  kd.sdk.hr.hrpi.common.enums.EmployeeModelConfigTypeEnum
 */
package kd.hr.hpfs.opplugin.op.htmtpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer;
import kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hpfs.business.application.service.relatiolservice.repository.HelperRepository;
import kd.sdk.hr.hpfs.utils.ChgRecordOpHelper;
import kd.sdk.hr.hpfs.utils.ChgUtils;
import kd.sdk.hr.hrpi.common.enums.EmployeeModelConfigTypeEnum;

public final class HtmTplEffectOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(HtmTplEffectOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        List fieldKeys = evt.getFieldKeys();
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)"hpfs_hrhtmbillorgtpl");
        Map allFields = dataEntityType.getAllFields();
        fieldKeys.addAll(new ArrayList(allFields.keySet()));
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities.length == 0) {
            return;
        }
        Map defaultChgRecordMap = ChgRecordOpHelper.getChgRecords();
        Set chgRecordIds = Arrays.stream(dataEntities).map(it -> it.getLong("affrecord")).collect(Collectors.toSet());
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hpfs_chgrecord");
        LOG.info("HtmTplEffectOp Got chgRecordIds: {} from bills.", chgRecordIds);
        Map<Long, DynamicObject> chgRecordMap = Arrays.stream(serviceHelper.loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", chgRecordIds)})).collect(Collectors.toMap(it -> it.getLong("id"), it -> it, (oldValue, newValue) -> oldValue));
        chgRecordMap.putAll(defaultChgRecordMap);
        List empIdList = Arrays.stream(dataEntities).map(dy -> dy.getLong("bb_em_tid.id")).collect(Collectors.toList());
        Map empEmpSupRelMapByEmp = HelperRepository.getEmpEmpSupRelMapByEmp(empIdList);
        Map empEmpSupRelMapBySupEmp = HelperRepository.getEmpEmpSupRelMapBySupEmp(empIdList);
        String privacyPropJson = ChgUtils.getPrivacyPropJson();
        for (DynamicObject quitApplyDy : dataEntities) {
            Long chgRecordId = quitApplyDy.getLong("affrecord");
            DynamicObject chgRecord = chgRecordMap.get(chgRecordId);
            if (null == chgRecord) {
                LOG.info("ApplyUtils Bill[{}]'s chgRecord:[{}] is empty.", (Object)quitApplyDy.getString("id"), (Object)chgRecordId);
                continue;
            }
            if (HRStringUtils.equals((String)"1", (String)chgRecord.getString("datastatus"))) {
                LOG.info("ApplyUtils Ignore chgRecord[id:{}] for dataStatus.", chgRecord.get("id"));
                continue;
            }
            long bb_em_tid = quitApplyDy.getLong("bb_em_tid.id");
            List empSupRelListByEmp = (List)empEmpSupRelMapByEmp.get(bb_em_tid);
            List empSupRelListBySupEmp = (List)empEmpSupRelMapBySupEmp.get(bb_em_tid);
            this.setEntry(quitApplyDy, chgRecord, empSupRelListByEmp, empSupRelListBySupEmp, privacyPropJson);
            this.modifyEmpCadre(chgRecord);
        }
        ChgRecordOpHelper.setChgRecords(chgRecordMap);
    }

    private void modifyEmpCadre(DynamicObject chgRecord) {
        DynamicObjectCollection entry = chgRecord.getDynamicObjectCollection("paramentry");
        String privacyPropJson = ChgUtils.getPrivacyPropJson();
        for (DynamicObject paramEntry : entry) {
            if (!"hrpi_empcadre".equals(paramEntry.get("paramchgentity_id"))) continue;
            String paramDataAfterTag = paramEntry.getString("paramdataafter_tag");
            DynamicObject empCadreDy = CustomDynamicObjectJsonSerializer.parseDynamicObjectJson((DynamicObject)chgRecord, (String)paramDataAfterTag, (String)"hrpi_empcadre");
            Date startDate = empCadreDy.getDate("startdate");
            Date endDate = empCadreDy.getDate("enddate");
            if (startDate == null || endDate == null || !HRDateTimeUtils.dayBefore((Date)endDate, (Date)startDate)) continue;
            empCadreDy.set("enddate", (Object)startDate);
            String dataAfter = ChgUtils.setDataAfter((DynamicObject)paramEntry, (DynamicObject)empCadreDy, (String)privacyPropJson);
            ChgUtils.setDataCompare((DynamicObject)paramEntry, (String)paramEntry.getString("paramdatabefore_tag"), (String)dataAfter);
        }
    }

    private void setEntry(DynamicObject quitApplyDy, DynamicObject chgRecord, List<DynamicObject> empSupRelListByEmp, List<DynamicObject> empSupRelListBySupEmp, String privacyPropJson) {
        Date preBEffectiveDate = null;
        Date bEffectiveDate = quitApplyDy.getDate("b_effectivedate");
        if (bEffectiveDate != null) {
            preBEffectiveDate = HRDateTimeUtils.addDays((int)-1, (Date)bEffectiveDate);
        }
        DynamicObjectCollection entry = chgRecord.getDynamicObjectCollection("paramentry");
        if (empSupRelListByEmp != null) {
            for (DynamicObject empEmpRel : empSupRelListByEmp) {
                this.buildEntry(entry, empEmpRel, preBEffectiveDate, privacyPropJson);
            }
        }
        if (empSupRelListBySupEmp != null) {
            for (DynamicObject empEmpSupRel : empSupRelListBySupEmp) {
                this.buildEntry(entry, empEmpSupRel, preBEffectiveDate, privacyPropJson);
            }
        }
    }

    private void buildEntry(DynamicObjectCollection entry, DynamicObject empEmpRel, Date preBEffectiveDate, String privacyPropJson) {
        DynamicObject entryItem = entry.addNew();
        String dataBefore = ChgUtils.setDataBefore((DynamicObject)entryItem, (DynamicObject)empEmpRel, (String)privacyPropJson);
        Date startDate = empEmpRel.getDate("startdate");
        if (startDate != null && preBEffectiveDate != null && HRDateTimeUtils.dayBefore((Date)startDate, (Date)preBEffectiveDate)) {
            empEmpRel.set("enddate", (Object)preBEffectiveDate);
        } else {
            empEmpRel.set("enddate", (Object)startDate);
        }
        String employeeModel = EmployeeModelConfigTypeEnum.ASSIGNMENT_ATTACH_MODEL.getCode();
        String timelineModelType = TimelineModelTypeEnum.INTERRUPTION_OVERLAP.getType();
        ChgUtils.buildChgRecordEntry((DynamicObject)entryItem, (String)"1", (String)"hrpi_empsuprel", (DynamicObject)empEmpRel, (DynamicObject)empEmpRel, (String)dataBefore, (String)employeeModel, (String)timelineModelType, (String)privacyPropJson);
    }
}
