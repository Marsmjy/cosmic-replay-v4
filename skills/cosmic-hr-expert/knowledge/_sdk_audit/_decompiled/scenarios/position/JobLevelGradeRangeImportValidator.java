/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.opplugin.validator.HRDataBaseValidator
 *  kd.hrmp.hbpm.business.infrastructure.client.job.HbjmMserviceHelper
 *  kd.hrmp.hbpm.business.utils.SystemParamHelper
 *  kd.hrmp.hbpm.common.constants.JobLevelGradeTextEnum
 */
package kd.hrmp.hbpm.opplugin.web.position.validate;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.opplugin.validator.HRDataBaseValidator;
import kd.hrmp.hbpm.business.infrastructure.client.job.HbjmMserviceHelper;
import kd.hrmp.hbpm.business.utils.SystemParamHelper;
import kd.hrmp.hbpm.common.constants.JobLevelGradeTextEnum;

public class JobLevelGradeRangeImportValidator
extends HRDataBaseValidator {
    private static final Log LOGGER = LogFactory.getLog(JobLevelGradeRangeImportValidator.class);
    private boolean isExistMustRelateJobParam;

    public JobLevelGradeRangeImportValidator() {
    }

    public JobLevelGradeRangeImportValidator(boolean isExistMustRelateJobParam) {
        this.isExistMustRelateJobParam = isExistMustRelateJobParam;
    }

    public void validate() {
        ExtendedDataEntity[] data = this.getDataEntities();
        long startTime = System.currentTimeMillis();
        this.checkJobRelatedInformation(data);
        LOGGER.info(String.format("JobLevelGradeRangeImportValidator validate all time : %s", System.currentTimeMillis() - startTime));
    }

    private void checkJobRelatedInformation(ExtendedDataEntity[] data) {
        Map buToIsMustRelateJobMap = new HashMap();
        if (this.isExistMustRelateJobParam) {
            List buIdList = Arrays.stream(data).map(d -> d.getDataEntity().getLong("adminorg.org.id")).collect(Collectors.toList());
            buToIsMustRelateJobMap = SystemParamHelper.getBatchPosMustRelateJobParameter(buIdList);
        }
        List gradeScmIdList = Arrays.stream(data).map(d -> d.getDataEntity().getLong("jobgradescm.id")).collect(Collectors.toList());
        List levelScmIdList = Arrays.stream(data).map(d -> d.getDataEntity().getLong("joblevelscm.id")).collect(Collectors.toList());
        Map gradeScmIdToGradeMap = HbjmMserviceHelper.getGradeScmIdToGradeMap(gradeScmIdList);
        Map levelScmIdToLevelMap = HbjmMserviceHelper.getLevelScmIdToLevelMap(levelScmIdList);
        for (ExtendedDataEntity dataEntity : data) {
            DynamicObject dy = dataEntity.getDataEntity();
            boolean isMustRelateJob = false;
            if (this.isExistMustRelateJobParam) {
                long buId = dy.getLong("adminorg.org.id");
                isMustRelateJob = buToIsMustRelateJobMap.getOrDefault(buId, false);
            }
            this.showErrorMessage(dataEntity, this.checkJobSystemRelatedInfo(dy, isMustRelateJob, gradeScmIdToGradeMap, levelScmIdToLevelMap));
        }
    }

    private List<String> checkJobSystemRelatedInfo(DynamicObject dy, boolean isMustRelateJob, Map<Long, Map<Long, DynamicObject>> gradeScmIdToGradeMap, Map<Long, Map<Long, DynamicObject>> levelScmIdToLevelMap) {
        DynamicObjectCollection jobScmMul;
        LinkedList<String> results = new LinkedList<String>();
        DynamicObject job = dy.getDynamicObject("job");
        if (isMustRelateJob && job == null) {
            results.add(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u804c\u4f4d\uff0c\u5982\u679c\u65e0\u9700\u586b\u5199\u804c\u4f4d\uff0c\u9700\u8981\u4fee\u6539\u53c2\u6570\u201c\u5c97\u4f4d\u662f\u5426\u5fc5\u987b\u5173\u8054\u804c\u4f4d\u4fe1\u606f\u201d\uff0c\u8def\u5f84\u4e3a\uff1a\u7ec4\u7ec7\u53d1\u5c55\u4e91>\u7ec4\u7ec7\u7ba1\u7406>\u53c2\u6570\u914d\u7f6e\u3002", (String)"JobLevelGradeRangeCheck_0", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
            return results;
        }
        long jobscmId = dy.getLong("jobscm.id");
        if (jobscmId == 0L && job != null && ((jobScmMul = job.getDynamicObjectCollection("jobscm")).size() > 1 || this.isFromPage().booleanValue() && jobScmMul.size() == 1)) {
            results.add(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u804c\u4f4d\u4f53\u7cfb\u65b9\u6848\u3002", (String)"JobLevelGradeRangeCheck_3", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
        }
        if (this.isFromPage().booleanValue()) {
            return results;
        }
        if (jobscmId != 0L) {
            if (job == null) {
                results.add(ResManager.loadKDString((String)"\u8bf7\u5148\u586b\u5199\u804c\u4f4d\u3002", (String)"JobLevelGradeRangeCheck_1", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
            } else {
                jobScmMul = job.getDynamicObjectCollection("jobscm");
                List jobscmIdList = jobScmMul.stream().map(dyn -> dyn.getLong("fbasedataid_id")).collect(Collectors.toList());
                if (!jobscmIdList.contains(jobscmId)) {
                    results.add(ResManager.loadKDString((String)"\u804c\u4f4d\u4f53\u7cfb\u65b9\u6848\u4e0d\u5728\u804c\u4f4d\u5173\u8054\u8303\u56f4\u5185\u3002", (String)"JobLevelGradeRangeCheck_2", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
                }
            }
        } else if (job != null && (jobScmMul = job.getDynamicObjectCollection("jobscm")).size() == 1) {
            dy.set("jobscm", (Object)((DynamicObject)jobScmMul.get(0)).getDynamicObject("fbasedataid"));
        }
        long jobgradescmId = dy.getLong("jobgradescm.id");
        if (job != null) {
            long jobgradescmIdFromJob = job.getLong("jobgradescm.id");
            if (jobgradescmIdFromJob != 0L && jobgradescmId != 0L && jobgradescmId != jobgradescmIdFromJob) {
                results.add(ResManager.loadKDString((String)"\u804c\u7b49\u65b9\u6848\u9700\u4e3a\u804c\u4f4d\u5173\u8054\u7684\u804c\u7b49\u65b9\u6848\u3002", (String)"JobLevelGradeRangeCheck_4", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
            } else if (isMustRelateJob && jobgradescmIdFromJob == 0L && jobgradescmId != 0L) {
                results.add(ResManager.loadKDString((String)"\u804c\u7b49\u65b9\u6848\u9700\u4e3a\u804c\u4f4d\u5173\u8054\u7684\u804c\u7b49\u65b9\u6848\u3002\u5982\u679c\u9700\u8981\u9009\u62e9\u5176\u4ed6\u804c\u7b49\u65b9\u6848\uff0c\u9700\u8981\u4fee\u6539\u53c2\u6570\u201c\u5c97\u4f4d\u662f\u5426\u5fc5\u987b\u5173\u8054\u804c\u4f4d\u4fe1\u606f\u201d\uff0c\u8def\u5f84\u4e3a\uff1a\u7ec4\u7ec7\u53d1\u5c55\u4e91>\u7ec4\u7ec7\u7ba1\u7406>\u53c2\u6570\u914d\u7f6e\u3002", (String)"JobLevelGradeRangeCheck_5", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
            }
        }
        long joblevelscmId = dy.getLong("joblevelscm.id");
        if (job != null) {
            long joblevelscmIdFromJob = job.getLong("joblevelscm.id");
            if (joblevelscmIdFromJob != 0L && joblevelscmId != 0L && joblevelscmId != joblevelscmIdFromJob) {
                results.add(ResManager.loadKDString((String)"\u804c\u7ea7\u65b9\u6848\u9700\u4e3a\u804c\u4f4d\u5173\u8054\u7684\u804c\u7ea7\u65b9\u6848\u3002", (String)"JobLevelGradeRangeCheck_6", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
            } else if (isMustRelateJob && joblevelscmIdFromJob == 0L && joblevelscmId != 0L) {
                results.add(ResManager.loadKDString((String)"\u804c\u7ea7\u65b9\u6848\u9700\u4e3a\u804c\u4f4d\u5173\u8054\u7684\u804c\u7ea7\u65b9\u6848\u3002\u5982\u679c\u9700\u8981\u9009\u62e9\u5176\u4ed6\u804c\u7ea7\u65b9\u6848\uff0c\u9700\u8981\u4fee\u6539\u53c2\u6570\u201c\u5c97\u4f4d\u662f\u5426\u5fc5\u987b\u5173\u8054\u804c\u4f4d\u4fe1\u606f\u201d\uff0c\u8def\u5f84\u4e3a\uff1a\u7ec4\u7ec7\u53d1\u5c55\u4e91>\u7ec4\u7ec7\u7ba1\u7406>\u53c2\u6570\u914d\u7f6e\u3002", (String)"JobLevelGradeRangeCheck_7", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
            }
        }
        this.setLowHighValueSameWhenAnyOneEmpty(dy, JobLevelGradeTextEnum.LEVEL);
        this.setLowHighValueSameWhenAnyOneEmpty(dy, JobLevelGradeTextEnum.GRADE);
        long lowJobGradeId = dy.getLong("lowjobgrade.id");
        long hignJobGradeId = dy.getLong("highjobgrade.id");
        if (lowJobGradeId != 0L || hignJobGradeId != 0L) {
            if (jobgradescmId == 0L) {
                results.add(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u201c\u804c\u7b49\u65b9\u6848\u201d\u3002", (String)"JobLevelGradeRangeCheck_8", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
            } else {
                int highSeq;
                int lowSeq;
                Map<Long, DynamicObject> gradeLevelsMap = gradeScmIdToGradeMap.get(jobgradescmId);
                Map range = HbjmMserviceHelper.getRange((DynamicObject)job, gradeLevelsMap, (JobLevelGradeTextEnum)JobLevelGradeTextEnum.GRADE);
                if (!range.containsKey(lowJobGradeId)) {
                    results.add(ResManager.loadKDString((String)"\u201c\u6700\u4f4e\u804c\u7b49\u201d\u4e0d\u5728\u53ef\u586b\u8303\u56f4\u5185\u3002", (String)"JobLevelGradeRangeCheck_9", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
                }
                if (!range.containsKey(hignJobGradeId)) {
                    results.add(ResManager.loadKDString((String)"\u201c\u6700\u9ad8\u804c\u7b49\u201d\u4e0d\u5728\u53ef\u586b\u8303\u56f4\u5185\u3002", (String)"JobLevelGradeRangeCheck_10", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
                }
                DynamicObject lowJobGrade = gradeLevelsMap.get(lowJobGradeId);
                DynamicObject highJobGrade = gradeLevelsMap.get(hignJobGradeId);
                if (lowJobGrade != null && highJobGrade != null && (lowSeq = lowJobGrade.getInt(JobLevelGradeTextEnum.GRADE.getSeqSign())) > (highSeq = highJobGrade.getInt(JobLevelGradeTextEnum.GRADE.getSeqSign()))) {
                    results.add(ResManager.loadKDString((String)"\u201c\u6700\u4f4e\u804c\u7b49\u987a\u5e8f\u7801\u201d\u5e94\u5c0f\u4e8e\u201c\u6700\u9ad8\u804c\u7b49\u987a\u5e8f\u7801\u201d\u3002", (String)"JobLevelGradeRangeCheck_11", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
                }
            }
        }
        long lowJobLevelId = dy.getLong("lowjoblevel.id");
        long hignJobLevelId = dy.getLong("highjoblevel.id");
        if (lowJobLevelId != 0L || hignJobLevelId != 0L) {
            if (joblevelscmId == 0L) {
                results.add(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u201c\u804c\u7ea7\u65b9\u6848\u201d\u3002", (String)"JobLevelGradeRangeCheck_12", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
            } else {
                int highSeq;
                int lowSeq;
                Map<Long, DynamicObject> gradeLevelsMap = levelScmIdToLevelMap.get(joblevelscmId);
                Map range = HbjmMserviceHelper.getRange((DynamicObject)job, gradeLevelsMap, (JobLevelGradeTextEnum)JobLevelGradeTextEnum.LEVEL);
                if (!range.containsKey(lowJobLevelId)) {
                    results.add(ResManager.loadKDString((String)"\u201c\u6700\u4f4e\u804c\u7ea7\u201d\u4e0d\u5728\u53ef\u586b\u8303\u56f4\u5185\u3002", (String)"JobLevelGradeRangeCheck_13", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
                }
                if (!range.containsKey(hignJobLevelId)) {
                    results.add(ResManager.loadKDString((String)"\u201c\u6700\u9ad8\u804c\u7ea7\u201d\u4e0d\u5728\u53ef\u586b\u8303\u56f4\u5185\u3002", (String)"JobLevelGradeRangeCheck_14", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
                }
                DynamicObject lowJobLevel = gradeLevelsMap.get(lowJobLevelId);
                DynamicObject highJobLevel = gradeLevelsMap.get(hignJobLevelId);
                if (lowJobLevel != null && highJobLevel != null && (lowSeq = lowJobLevel.getInt(JobLevelGradeTextEnum.LEVEL.getSeqSign())) > (highSeq = highJobLevel.getInt(JobLevelGradeTextEnum.LEVEL.getSeqSign()))) {
                    results.add(ResManager.loadKDString((String)"\u201c\u6700\u4f4e\u804c\u7ea7\u987a\u5e8f\u7801\u201d\u5e94\u5c0f\u4e8e\u201c\u6700\u9ad8\u804c\u7ea7\u987a\u5e8f\u7801\u201d\u3002", (String)"JobLevelGradeRangeCheck_15", (String)"hrmp-hbpm-business", (Object[])new Object[0]));
                }
            }
        }
        return results;
    }

    private void setLowHighValueSameWhenAnyOneEmpty(DynamicObject dy, JobLevelGradeTextEnum enu) {
        if (dy.getDynamicObject(enu.getLowSign()) == null && dy.getDynamicObject(enu.getHighSign()) != null) {
            dy.set(enu.getLowSign(), (Object)dy.getDynamicObject(enu.getHighSign()));
        } else if (dy.getDynamicObject(enu.getHighSign()) == null && dy.getDynamicObject(enu.getLowSign()) != null) {
            dy.set(enu.getHighSign(), (Object)dy.getDynamicObject(enu.getLowSign()));
        }
    }

    private void showErrorMessage(ExtendedDataEntity dataEntity, Object content) {
        if (Objects.isNull(content)) {
            return;
        }
        if (content instanceof String) {
            this.addFatalErrorMessage(dataEntity, (String)content);
        } else if (content instanceof List) {
            List result = (List)content;
            if (CollectionUtils.isEmpty((Collection)result)) {
                return;
            }
            result.forEach(value -> this.addFatalErrorMessage(dataEntity, (String)value));
        }
    }

    private Boolean isFromPage() {
        if (this.getOption().containsVariable("isFromPage")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
