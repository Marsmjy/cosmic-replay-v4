/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.Tuple
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.operate.OperationException
 *  kd.bos.entity.operate.result.OperateErrorInfo
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.entity.validate.ErrorLevel
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.bos.threads.ThreadPool
 *  kd.bos.threads.ThreadPools
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.common.api.HrApiResponse
 *  kd.hr.hbp.common.model.org.staff.StaffUseParam
 *  kd.hr.hbp.common.util.HRArrayUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hom.business.application.service.hcf.IHomToHcfAppService
 *  kd.hr.hom.business.application.service.staff.IStaffUseService
 *  kd.hr.hom.business.domain.service.common.IBaseDataDomainService
 *  kd.hr.hom.business.domain.service.hcf.IHcfDataDomainService
 *  kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainService
 *  kd.hr.hom.common.constant.BaseDataIdConstants
 *  kd.hr.hom.common.constant.HcfCanCreConstants
 *  kd.hr.hom.common.constant.HcfCandidateConstants
 *  kd.hr.hom.common.entity.IDCardInfo
 *  kd.hr.hom.common.util.HOMObjectUtils
 *  kd.hr.hom.common.util.IDCardUtils
 *  kd.hr.hom.common.util.PinyinUtil
 *  kd.hr.hom.opplugin.validate.personwaitstart.PersonWaitStartValidator
 *  kd.hrmp.hrpi.business.infrastructure.utils.LunarcalendarUtil
 *  kd.sdk.hr.hom.common.enums.OnbrdStatusEnum
 *  kd.sdk.hr.hpfs.business.config.service.IDevParamConfigService
 */
package kd.hr.hom.opplugin.onbrd;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.Tuple;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.operate.OperationException;
import kd.bos.entity.operate.result.OperateErrorInfo;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.entity.validate.ErrorLevel;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.bos.threads.ThreadPool;
import kd.bos.threads.ThreadPools;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.common.api.HrApiResponse;
import kd.hr.hbp.common.model.org.staff.StaffUseParam;
import kd.hr.hbp.common.util.HRArrayUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hom.business.application.service.hcf.IHomToHcfAppService;
import kd.hr.hom.business.application.service.staff.IStaffUseService;
import kd.hr.hom.business.domain.service.common.IBaseDataDomainService;
import kd.hr.hom.business.domain.service.hcf.IHcfDataDomainService;
import kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainService;
import kd.hr.hom.common.constant.BaseDataIdConstants;
import kd.hr.hom.common.constant.HcfCanCreConstants;
import kd.hr.hom.common.constant.HcfCandidateConstants;
import kd.hr.hom.common.entity.IDCardInfo;
import kd.hr.hom.common.util.HOMObjectUtils;
import kd.hr.hom.common.util.IDCardUtils;
import kd.hr.hom.common.util.PinyinUtil;
import kd.hr.hom.opplugin.validate.personwaitstart.PersonWaitStartValidator;
import kd.hrmp.hrpi.business.infrastructure.utils.LunarcalendarUtil;
import kd.sdk.hr.hom.common.enums.OnbrdStatusEnum;
import kd.sdk.hr.hpfs.business.config.service.IDevParamConfigService;

public class OnbrdBillSaveOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(OnbrdBillSaveOp.class);
    private static final ThreadPool threadPool = ThreadPools.newCachedThreadPool((String)"OnbrdBillSaveOp", (int)5, (int)1000);

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        List fieldKeys = evt.getFieldKeys();
        fieldKeys.add("ba_em_name");
        fieldKeys.add("ba_em_empnumber");
        fieldKeys.add("billno");
        fieldKeys.add("candidate");
        fieldKeys.add("ba_po_adminorg");
        fieldKeys.add("ba_po_position");
        fieldKeys.add("ba_po_job");
        fieldKeys.add("ajoblevel");
        fieldKeys.add("ba_e_laborreltype");
        fieldKeys.add("viewtype");
        fieldKeys.add("b_effectivedate");
        fieldKeys.add("enrollstatus");
        fieldKeys.add("bb_em_tid");
        fieldKeys.add("automaticinfo");
        String staffAdminOrg = IDevParamConfigService.getInstance().queryBusinessValueByBusinessKey("staff_adminorg");
        if (!HRStringUtils.isBlank((CharSequence)staffAdminOrg)) {
            fieldKeys.add(staffAdminOrg);
        }
        IStaffUseService staffUseService = IStaffUseService.getInstance();
        Set mapFieldSet = staffUseService.getMapFieldSet();
        fieldKeys.addAll(mapFieldSet);
    }

    public void onAddValidators(AddValidatorsEventArgs addValidatorsEventArgs) {
        addValidatorsEventArgs.addValidator((AbstractValidator)new PersonWaitStartValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        DynamicObject[] dataEntities = args.getDataEntities();
        this.setValueByCertNumber(dataEntities);
    }

    private void setValueByCertNumber(DynamicObject[] dataEntities) {
        DynamicObject nationalityChina = IBaseDataDomainService.getInstance().getBaseDataInfoById("hbss_nationality", BaseDataIdConstants.HBSS_NATIONALITY_1010, "number");
        DynamicObject sexMan = IBaseDataDomainService.getInstance().getBaseDataInfoById("hbss_sex", BaseDataIdConstants.HBSS_SEX_1010, "number");
        DynamicObject sexWoman = IBaseDataDomainService.getInstance().getBaseDataInfoById("hbss_sex", BaseDataIdConstants.HBSS_SEX_1020, "number");
        for (DynamicObject onbrdBill : dataEntities) {
            DynamicObject certificateType = onbrdBill.getDynamicObject("certificatetype");
            IDCardInfo idCardInfo = null;
            if (!HRObjectUtils.isEmpty((Object)certificateType)) {
                long cerTypeNum = certificateType.getLong("id");
                if (HcfCanCreConstants.CREDENTIALSTYPE_ID_IDCARD == cerTypeNum) {
                    idCardInfo = IDCardUtils.parse((String)onbrdBill.getString("certificatenumber"));
                }
            }
            if (idCardInfo == null) continue;
            HOMObjectUtils.setFieldValueIfEmpty((DynamicObject)onbrdBill, (String)"nationality", (Object)nationalityChina);
            HOMObjectUtils.setFieldValueIfEmpty((DynamicObject)onbrdBill, (String)"gender", (Object)(idCardInfo.getGender() ? sexMan : sexWoman));
            HOMObjectUtils.setFieldValueIfEmpty((DynamicObject)onbrdBill, (String)"birthday", (Object)idCardInfo.getBirthDay());
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        Object[] dataEntities = args.getDataEntities();
        if (args.isCancelOperation()) {
            LOG.info("args.isCancelOperation()");
            return;
        }
        if (HRArrayUtils.isEmpty((Object[])dataEntities)) {
            LOG.info("That is not Bill need to save!");
            return;
        }
        for (Object dataEntity : dataEntities) {
            DynamicObject candidate;
            Object probationTime = dataEntity.get("probationtime");
            if (probationTime == null) {
                dataEntity.set("probationtime", (Object)0);
            }
            if (!HRObjectUtils.isEmpty((Object)(candidate = dataEntity.getDynamicObject("candidate")))) continue;
            DynamicObject candidateNew = new DynamicObject((DynamicObjectType)MetadataServiceHelper.getDataEntityType((String)"hcf_candidate"));
            candidateNew.set("name", (Object)dataEntity.getString("ba_em_name"));
            DynamicObject candidateFromDB = new HcfDataDomainService().saveCandidate(candidateNew);
            if (HRObjectUtils.isEmpty((Object)candidateFromDB)) {
                String msg = ResManager.loadKDString((String)"\u4fdd\u5b58\u62df\u5165\u804c\u4eba\u5458\u5931\u8d25\u3002", (String)"OnbrdBillSaveOp_1", (String)"hr-hom-opplugin", (Object[])new Object[0]);
                LOG.info("OnbrdBillSaveOp save candidate error traceid:{}", (Object)RequestContext.getOrCreate().getTraceId());
                throw new KDBizException(msg);
            }
            dataEntity.set("candidate", (Object)candidateFromDB);
        }
        this.synCanCertificate((DynamicObject[])dataEntities);
        this.saveHcfBaseInfo((DynamicObject[])dataEntities);
        this.saveHcfContact((DynamicObject[])dataEntities);
        this.saveCanAddressInfo((DynamicObject[])dataEntities);
        List sourceDatas = Stream.of(dataEntities).collect(Collectors.toList());
        List onbrdBillParts = new ArrayList(16);
        if (sourceDatas.size() > 1000) {
            onbrdBillParts = Lists.partition(sourceDatas, (int)1000);
        } else {
            onbrdBillParts.add(sourceDatas);
        }
        HashSet failIds = new HashSet();
        for (List onbrdBillInfos : onbrdBillParts) {
            Tuple<Set<Long>, String> staffUseResult = this.staffUse(args, onbrdBillInfos);
            failIds.addAll((Collection)staffUseResult.item1);
            String errorTip = (String)staffUseResult.item2;
            if (CollectionUtils.isEmpty(failIds)) continue;
            this.throwExceptionForTcc(args, errorTip);
            break;
        }
    }

    private void synCanCertificate(DynamicObject[] dataEntities) {
        Set candidateIds = Arrays.asList(dataEntities).stream().map(dy -> dy.getLong("candidate.id")).collect(Collectors.toSet());
        String properties = Lists.newArrayList((Object[])new String[]{"id", "candidate", "credentialstype", "number", "ismajor", "percardname", "gender", "birthday"}).stream().collect(Collectors.joining(","));
        Map hcfCancreMap = new HcfDataDomainService().querySingleRowEntity(candidateIds, "hcf_cancre", properties);
        DynamicObjectCollection collection = new DynamicObjectCollection();
        Arrays.stream(dataEntities).forEach(onbrdBill -> {
            Long candidateId = onbrdBill.getLong("candidate.id");
            DynamicObject certificateType = onbrdBill.getDynamicObject("certificatetype");
            if (HRObjectUtils.isEmpty((Object)certificateType)) {
                LOG.info("certificateType is empty candidateId:{}", (Object)candidateId);
                return;
            }
            DynamicObject certificateDy = HRObjectUtils.isEmpty(hcfCancreMap.get(candidateId)) ? new DynamicObject((DynamicObjectType)MetadataServiceHelper.getDataEntityType((String)"hcf_cancre")) : (DynamicObject)hcfCancreMap.get(candidateId);
            String certificateNumber = onbrdBill.getString("certificatenumber");
            if (HcfCanCreConstants.CREDENTIALSTYPE_ID_IDCARD.longValue() == certificateType.getLong("id")) {
                Date birthDay = IDCardUtils.getBirthDay((String)certificateNumber);
                certificateDy.set("birthday", (Object)birthDay);
            }
            certificateDy.set("id", (Object)ORM.create().genLongId("hcf_cancre"));
            certificateDy.set("candidate", (Object)candidateId);
            certificateDy.set("credentialstype", (Object)certificateType);
            certificateDy.set("number", (Object)certificateNumber);
            certificateDy.set("ismajor", (Object)Boolean.TRUE);
            certificateDy.set("percardname", onbrdBill.get("ba_em_name"));
            certificateDy.set("gender", onbrdBill.get("gender"));
            collection.add((Object)certificateDy);
        });
        HashMap map = Maps.newHashMapWithExpectedSize((int)2);
        map.put("hcf_cancre", collection);
        ArrayList<HashMap> list = new ArrayList<HashMap>(1);
        list.add(map);
        Map result = IHomToHcfAppService.getInstance().saveOrUpdateCandidates(list);
        boolean isSuccess = Boolean.parseBoolean(result.get("success").toString());
        if (!isSuccess) {
            LOG.error("Fail to synCanCertificate:{} ", result.get("message"));
        }
    }

    private void saveHcfBaseInfo(DynamicObject[] dataEntities) {
        Set candidateIds = Arrays.asList(dataEntities).stream().map(dy -> dy.getLong("candidate.id")).collect(Collectors.toSet());
        String properties = Lists.newArrayList((Object[])new String[]{"name", "gender", "birthday", "lunarcalendarbirthday", "nationality", "nameen", "marriagestatus", "enname", "folk", "joinworktime"}).stream().collect(Collectors.joining(","));
        Map hcfCandidateMap = new HcfDataDomainService().queryCandidateId(properties, new QFilter[]{new QFilter("id", "in", candidateIds)});
        DynamicObjectCollection canBaseInfoCollection = new DynamicObjectCollection();
        Arrays.stream(dataEntities).forEach(onbrdBill -> {
            Long cerType;
            DynamicObject certificateType;
            Long candidateId = onbrdBill.getLong("candidate.id");
            DynamicObject canBaseInfo = (DynamicObject)hcfCandidateMap.get(candidateId);
            if (HRObjectUtils.isEmpty((Object)canBaseInfo)) {
                canBaseInfo = new DynamicObject((DynamicObjectType)MetadataServiceHelper.getDataEntityType((String)"hcf_candidate"));
            }
            canBaseInfo.set("gender", onbrdBill.get("gender"));
            canBaseInfo.set("name", onbrdBill.get("ba_em_name"));
            String pinyin = "";
            try {
                String name = onbrdBill.getString("ba_em_name");
                pinyin = PinyinUtil.converterToSpell((String)name);
                LOG.info("name:{} pinyin:{}", (Object)name, (Object)pinyin);
            }
            catch (Exception exp) {
                LOG.error((Throwable)exp);
            }
            if (HRStringUtils.isEmpty((String)canBaseInfo.getString("nameen"))) {
                canBaseInfo.set("nameen", (Object)pinyin);
            }
            if (onbrdBill.containsProperty("birthday") && HRObjectUtils.isEmpty((Object)canBaseInfo.get("birthday"))) {
                canBaseInfo.set("birthday", onbrdBill.get("birthday"));
            }
            if (!HRObjectUtils.isEmpty((Object)(certificateType = onbrdBill.getDynamicObject("certificatetype"))) && HRObjectUtils.isEmpty((Object)canBaseInfo.get("birthday")) && HcfCanCreConstants.CREDENTIALSTYPE_ID_IDCARD.equals(cerType = Long.valueOf(certificateType.getLong("id")))) {
                String certNum = onbrdBill.getString("certificatenumber");
                Date birthDay = IDCardUtils.getBirthDay((String)certNum);
                canBaseInfo.set("birthday", (Object)birthDay);
                canBaseInfo.set("lunarcalendarbirthday", (Object)LunarcalendarUtil.getLunarcalendarBirthday((Date)birthDay));
            }
            if (onbrdBill.containsProperty("nationality") && HRObjectUtils.isEmpty((Object)canBaseInfo.get("nationality"))) {
                canBaseInfo.set("nationality", (Object)onbrdBill.getLong("nationality.id"));
            }
            if (onbrdBill.containsProperty("engname") && HRObjectUtils.isEmpty((Object)canBaseInfo.get("enname"))) {
                canBaseInfo.set("enname", onbrdBill.get("engname"));
            }
            if (onbrdBill.containsProperty("marriagestatus") && HRObjectUtils.isEmpty((Object)canBaseInfo.get("marriagestatus"))) {
                canBaseInfo.set("marriagestatus", onbrdBill.get("marriagestatus"));
            }
            if (onbrdBill.containsProperty("folk") && HRObjectUtils.isEmpty((Object)canBaseInfo.get("folk"))) {
                canBaseInfo.set("folk", onbrdBill.get("folk"));
            }
            if (onbrdBill.containsProperty("beginservicedate") && HRObjectUtils.isEmpty((Object)canBaseInfo.get("joinworktime"))) {
                canBaseInfo.set("joinworktime", onbrdBill.get("beginservicedate"));
            }
            canBaseInfoCollection.add((Object)canBaseInfo);
        });
        HashMap<String, DynamicObjectCollection> baseMap = new HashMap<String, DynamicObjectCollection>();
        baseMap.put("hcf_candidate", canBaseInfoCollection);
        ArrayList<HashMap<String, DynamicObjectCollection>> candidates = new ArrayList<HashMap<String, DynamicObjectCollection>>();
        candidates.add(baseMap);
        IHomToHcfAppService.getInstance().saveOrUpdateCandidates(candidates);
    }

    private void saveHcfContact(DynamicObject[] dataEntities) {
        Set candidateIds = Arrays.asList(dataEntities).stream().map(dy -> dy.getLong("candidate.id")).collect(Collectors.toSet());
        String properties = Lists.newArrayList((Object[])new String[]{"id", "candidate", "phone", "peremail"}).stream().collect(Collectors.joining(","));
        Map hcfCancontactinfoMap = new HcfDataDomainService().querySingleRowEntity(candidateIds, "hcf_cancontactinfo", properties);
        DynamicObjectCollection hcfCancontactinfoCollection = new DynamicObjectCollection();
        Arrays.stream(dataEntities).forEach(onbrdBill -> {
            Long candidateId = onbrdBill.getLong("candidate.id");
            DynamicObject hcfCancontactinfoObj = HRObjectUtils.isEmpty(hcfCancontactinfoMap.get(candidateId)) ? new DynamicObject((DynamicObjectType)MetadataServiceHelper.getDataEntityType((String)"hcf_cancontactinfo")) : (DynamicObject)hcfCancontactinfoMap.get(candidateId);
            hcfCancontactinfoObj.set("candidate", onbrdBill.get("candidate"));
            if (HRObjectUtils.isEmpty((Object)hcfCancontactinfoObj.get("phone"))) {
                hcfCancontactinfoObj.set("phone", onbrdBill.get("phone"));
            }
            if (HRObjectUtils.isEmpty((Object)hcfCancontactinfoObj.get("peremail"))) {
                hcfCancontactinfoObj.set("peremail", onbrdBill.get("peremail"));
            }
            hcfCancontactinfoCollection.add((Object)hcfCancontactinfoObj);
        });
        HashMap<String, DynamicObjectCollection> map = new HashMap<String, DynamicObjectCollection>();
        map.put("hcf_cancontactinfo", hcfCancontactinfoCollection);
        ArrayList<HashMap<String, DynamicObjectCollection>> candidates = new ArrayList<HashMap<String, DynamicObjectCollection>>();
        candidates.add(map);
        IHomToHcfAppService.getInstance().saveOrUpdateCandidates(candidates);
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        DynamicObject[] dataEntities = e.getDataEntities();
        threadPool.execute(() -> {
            for (DynamicObject dynamicObject : dataEntities) {
                try {
                    IHomToHcfAppService.getInstance().syncPerson2CandidateInfo(dynamicObject);
                }
                catch (Exception exception) {
                    LOG.error("syncPerson2CandidateInfo###exception", (Throwable)exception);
                }
            }
        }, RequestContext.get());
    }

    private Tuple<Set<Long>, String> staffUse(BeginOperationTransactionArgs args, List<DynamicObject> onbrdBillInfos) {
        StaffUseParam staffUseParam;
        if (HRStringUtils.equals((String)args.getOperationKey(), (String)"startupsave")) {
            staffUseParam = IStaffUseService.getInstance().getStaffUseParamsForTCC(onbrdBillInfos.toArray(new DynamicObject[0]), "NEW");
        } else {
            Object[] onbrdBills = (DynamicObject[])onbrdBillInfos.stream().filter(onbrdBill -> HRStringUtils.equals((String)OnbrdStatusEnum.WAIT_ONBRD.getValue(), (String)onbrdBill.getString("enrollstatus"))).toArray(DynamicObject[]::new);
            if (HRArrayUtils.isEmpty((Object[])onbrdBills)) {
                return Tuple.create(new HashSet(), (Object)"");
            }
            staffUseParam = IStaffUseService.getInstance().getStaffUseParamsForTCC((DynamicObject[])onbrdBills, "UPDATE");
        }
        if (staffUseParam == null || CollectionUtils.isEmpty((Collection)staffUseParam.getStaffUseInParamList())) {
            LOG.info("{} staffUseParam.getStaffUseInParamList() isEmpty", (Object)args.getOperationKey());
            return Tuple.create(new HashSet(), (Object)"");
        }
        try {
            LOG.info("IHAOSStaffUseService start invoke staffPreOccupy param :{}", (Object)staffUseParam.toString());
            HrApiResponse response = (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"haos", (String)"IHAOSStaffUseService", (String)"staffPreOccupy", (Object[])new Object[]{staffUseParam});
            if (response == null) {
                throw new KDBizException(ResManager.loadKDString((String)"\u5360\u7f16\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7f16\u5236\u7ba1\u7406\u5458\u3002", (String)"OnbrdBillSaveOp_0", (String)"hr-hom-opplugin", (Object[])new Object[0]));
            }
            if (!response.isSuccess()) {
                String errorMessage = response.getErrorMessage();
                if (!HRStringUtils.isEmpty((String)errorMessage)) {
                    throw new KDBizException(errorMessage);
                }
                throw new KDBizException(ResManager.loadKDString((String)"\u5360\u7f16\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7f16\u5236\u7ba1\u7406\u5458\u3002", (String)"OnbrdBillSaveOp_0", (String)"hr-hom-opplugin", (Object[])new Object[0]));
            }
            LOG.info("staffUseParam.getStaffUseInParamList().size():{}", (Object)staffUseParam.getStaffUseInParamList().size());
            LOG.info("staffUseParam:{}", (Object)staffUseParam);
        }
        catch (Exception exception) {
            LOG.error((Throwable)exception);
        }
        return Tuple.create(new HashSet(), (Object)"");
    }

    private void throwExceptionForTcc(BeginOperationTransactionArgs args, String errorTip) {
        DynamicObject[] dataEntities = args.getDataEntities();
        List failIds = Stream.of(dataEntities).map(onbrdBill -> onbrdBill.getLong("id")).collect(Collectors.toList());
        String operationKey = args.getOperationKey();
        List operateErrorInfos = failIds.stream().map(failId -> this.buildOperateErrorInfo((Long)failId, operationKey, errorTip)).collect(Collectors.toList());
        this.getOperationResult().getSuccessPkIds().remove(failIds);
        this.getOperationResult().getAllErrorInfo().addAll(operateErrorInfos);
        args.setDataEntities(new DynamicObject[0]);
        args.setCancelOperation(true);
        this.getOperationResult().setSuccess(false);
        this.getOperationResult().setMessage(errorTip);
        OperationException operationException = new OperationException(((Long)failIds.get(0)).toString(), "", "");
        operationException.setTitle(operationKey);
        operationException.setMessage(errorTip);
        throw operationException;
    }

    private OperateErrorInfo buildOperateErrorInfo(Long onbrdBillid, String operationKey, String errorTip) {
        OperateErrorInfo errorInfo = new OperateErrorInfo("hom_IHAOSStaffUseService_tcc", ErrorLevel.FatalError, (Object)onbrdBillid);
        errorInfo.setTitle(operationKey);
        errorInfo.setMessage(errorTip);
        return errorInfo;
    }

    private void saveCanAddressInfo(DynamicObject[] dataEntities) {
        Set candidateIds = Arrays.asList(dataEntities).stream().map(dy -> dy.getLong("candidate.id")).collect(Collectors.toSet());
        Map multiRowEntityMap = Maps.newHashMapWithExpectedSize((int)candidateIds.size());
        HcfDataDomainService hcfDataDomainService = new HcfDataDomainService();
        StringBuilder properties = new StringBuilder("id").append(",").append("candidate").append(",").append("addresstype").append(",").append("countrycode").append(",").append("addressinfo");
        multiRowEntityMap = hcfDataDomainService.queryMultiRowEntity(candidateIds, "hcf_canaddress", properties.toString());
        Map<Long, DynamicObject> addressTypeMap = this.getAddressTypeMap();
        ArrayList<DynamicObject> addressSaveList = new ArrayList<DynamicObject>(3);
        for (DynamicObject onbrdBill : dataEntities) {
            DynamicObject regresidenceAddressDy;
            DynamicObject liveAddressDy;
            DynamicObject commuAddressDy;
            Long candidateId = onbrdBill.getLong("candidate.id");
            DynamicObjectCollection multiRowEntity = (DynamicObjectCollection)multiRowEntityMap.get(candidateId);
            Map<Object, Object> addressMap = Maps.newHashMapWithExpectedSize((int)3);
            if (!CollectionUtils.isEmpty((Collection)multiRowEntity)) {
                addressMap = multiRowEntity.stream().collect(Collectors.toMap(rowEntity -> rowEntity.getLong("addresstype.id"), rowEntity -> rowEntity, (oldValue, newValue) -> newValue));
            }
            if (HRObjectUtils.isEmpty((Object)(commuAddressDy = (DynamicObject)addressMap.get(HcfCandidateConstants.ADDRESSTYPE_CN_COMMUID)))) {
                commuAddressDy = new DynamicObject((DynamicObjectType)MetadataServiceHelper.getDataEntityType((String)"hcf_canaddress"));
                commuAddressDy.set("candidate", (Object)candidateId);
                commuAddressDy.set("addresstype", (Object)addressTypeMap.get(HcfCandidateConstants.ADDRESSTYPE_CN_COMMUID));
            }
            if (HRObjectUtils.isEmpty((Object)(liveAddressDy = (DynamicObject)addressMap.get(HcfCandidateConstants.ADDRESSTYPE_CN_PERSONID)))) {
                liveAddressDy = new DynamicObject((DynamicObjectType)MetadataServiceHelper.getDataEntityType((String)"hcf_canaddress"));
                liveAddressDy.set("candidate", (Object)candidateId);
                liveAddressDy.set("addresstype", (Object)addressTypeMap.get(HcfCandidateConstants.ADDRESSTYPE_CN_PERSONID));
            }
            if (HRObjectUtils.isEmpty((Object)(regresidenceAddressDy = (DynamicObject)addressMap.get(HcfCandidateConstants.ADDRESSTYPE_CN_HUKOUID)))) {
                regresidenceAddressDy = new DynamicObject((DynamicObjectType)MetadataServiceHelper.getDataEntityType((String)"hcf_canaddress"));
                regresidenceAddressDy.set("candidate", (Object)candidateId);
                regresidenceAddressDy.set("addresstype", (Object)addressTypeMap.get(HcfCandidateConstants.ADDRESSTYPE_CN_HUKOUID));
            }
            if (onbrdBill.get("commucountry") != null || HRStringUtils.isNotEmpty((String)onbrdBill.getString("commuaddress"))) {
                commuAddressDy.set("countrycode", onbrdBill.get("commucountry"));
                commuAddressDy.set("addressinfo", onbrdBill.get("commuaddress"));
                addressSaveList.add(commuAddressDy);
            }
            if (onbrdBill.get("livecountry") != null || HRStringUtils.isNotEmpty((String)onbrdBill.getString("liveaddressdetail"))) {
                liveAddressDy.set("countrycode", onbrdBill.get("livecountry"));
                liveAddressDy.set("addressinfo", onbrdBill.get("liveaddressdetail"));
                addressSaveList.add(liveAddressDy);
            }
            if (onbrdBill.get("regresidencecountry") == null && !HRStringUtils.isNotEmpty((String)onbrdBill.getString("regresidenceaddressdetail"))) continue;
            regresidenceAddressDy.set("countrycode", onbrdBill.get("regresidencecountry"));
            regresidenceAddressDy.set("addressinfo", onbrdBill.get("regresidenceaddressdetail"));
            addressSaveList.add(regresidenceAddressDy);
        }
        hcfDataDomainService.saveHcfAttachedData("hcf_canaddress", addressSaveList.toArray(new DynamicObject[addressSaveList.size()]));
    }

    private Map<Long, DynamicObject> getAddressTypeMap() {
        HashMap<Long, DynamicObject> addressTypeMap = new HashMap<Long, DynamicObject>();
        addressTypeMap.put(HcfCandidateConstants.ADDRESSTYPE_CN_COMMUID, IHcfDataDomainService.getInstance().getAddressTypeById(HcfCandidateConstants.ADDRESSTYPE_CN_COMMUID));
        addressTypeMap.put(HcfCandidateConstants.ADDRESSTYPE_CN_PERSONID, IHcfDataDomainService.getInstance().getAddressTypeById(HcfCandidateConstants.ADDRESSTYPE_CN_PERSONID));
        addressTypeMap.put(HcfCandidateConstants.ADDRESSTYPE_CN_HUKOUID, IHcfDataDomainService.getInstance().getAddressTypeById(HcfCandidateConstants.ADDRESSTYPE_CN_HUKOUID));
        return addressTypeMap;
    }
}
