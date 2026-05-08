/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.common.api.HrApiResponse
 *  kd.hr.hbp.common.model.history.param.HisCreateVersionParam
 *  kd.hr.hbp.common.model.history.param.HisCreateVersionReturnData
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hbpm.business.application.service.IPositionRelationServiceApplication
 *  kd.hrmp.hbpm.business.domain.position.service.impl.PositionHrDomainService
 *  kd.hrmp.hbpm.opplugin.web.position.validate.JobLevelGradeRangeImportValidator
 *  kd.hrmp.hbpm.opplugin.web.position.validate.PositionHisLoopValidator
 *  kd.hrmp.hbpm.opplugin.web.position.validate.PositionHisValidator
 *  kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper
 */
package kd.hrmp.hbpm.opplugin.web.position;

import com.google.common.collect.Sets;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.common.api.HrApiResponse;
import kd.hr.hbp.common.model.history.param.HisCreateVersionParam;
import kd.hr.hbp.common.model.history.param.HisCreateVersionReturnData;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hbpm.business.application.service.IPositionRelationServiceApplication;
import kd.hrmp.hbpm.business.domain.position.service.impl.PositionHrDomainService;
import kd.hrmp.hbpm.opplugin.web.position.validate.JobLevelGradeRangeImportValidator;
import kd.hrmp.hbpm.opplugin.web.position.validate.PositionHisLoopValidator;
import kd.hrmp.hbpm.opplugin.web.position.validate.PositionHisValidator;
import kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper;

public class PositionHisSaveOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(PositionHisSaveOp.class);

    public void onAddValidators(AddValidatorsEventArgs addValidatorsEventArgs) {
        addValidatorsEventArgs.addValidator((AbstractValidator)new JobLevelGradeRangeImportValidator(true));
        addValidatorsEventArgs.addValidator((AbstractValidator)new PositionHisLoopValidator().checkHis());
        addValidatorsEventArgs.addValidator((AbstractValidator)new PositionHisValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        DynamicObject[] positionHisArray = args.getDataEntities();
        Arrays.sort(positionHisArray, Comparator.comparing(dyn -> dyn.getLong("boid")).thenComparing(dyn -> dyn.getDate("bsed")));
        this.setDefaultValue(positionHisArray);
        this.calcBsledByNextBsed(positionHisArray);
    }

    private void setDefaultValue(DynamicObject[] positionHisArray) {
        Set boidSet = Arrays.stream(positionHisArray).map(dyn -> dyn.getLong("boid")).collect(Collectors.toSet());
        LOGGER.info("PositionHisSaveOp:positionHisArray_size:{}, bo_size:{}", (Object)positionHisArray.length, (Object)boidSet.size());
        PositionHrDomainService iPositionHrDomainService = new PositionHrDomainService();
        DynamicObject[] positionBoArray = iPositionHrDomainService.queryPositionByBOID(boidSet);
        LOGGER.info("PositionHisSaveOp:positionBoArray_length:{}", (Object)positionBoArray.length);
        Map<Long, Date> boAndEsDateMap = Arrays.stream(positionBoArray).filter(dyn -> dyn.getDate("establishmentdate") != null).collect(Collectors.toMap(dyn -> dyn.getLong("boid"), dyn -> dyn.getDate("establishmentdate")));
        for (DynamicObject pos : positionHisArray) {
            pos.set("org", pos.get("adminorg.org"));
            if (!HRObjectUtils.isEmpty((Object)pos.getDate("establishmentdate")) || !boAndEsDateMap.containsKey(pos.get("boid"))) continue;
            pos.set("establishmentdate", (Object)boAndEsDateMap.get(pos.getLong("boid")));
        }
    }

    private void calcBsledByNextBsed(DynamicObject[] positionHisArray) {
        HashSet boidSet = Sets.newHashSetWithExpectedSize((int)16);
        for (int i = 0; i < positionHisArray.length - 1; ++i) {
            DynamicObject current = positionHisArray[i];
            DynamicObject next = positionHisArray[i + 1];
            if (!boidSet.contains(current.getLong("boid"))) {
                boidSet.add(current.getLong("boid"));
            }
            if (!boidSet.contains(next.getLong("boid"))) continue;
            Date bsled = current.getDate("bsled");
            Date nextBsed = next.getDate("bsed");
            if (!HRObjectUtils.isEmpty((Object)bsled)) continue;
            Timestamp nextBsedMinusOneDay = new Timestamp(nextBsed.getTime() - 86400000L);
            current.set("bsled", (Object)nextBsedMinusOneDay);
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] positionHisArray = args.getDataEntities();
        HisCreateVersionParam hisCreateVersionParam = new HisCreateVersionParam();
        hisCreateVersionParam.setEntityNumber("hbpm_positionhr");
        Arrays.sort(positionHisArray, Comparator.comparing(dyn -> dyn.getLong("boid")).thenComparing(dyn -> dyn.getDate("bsed")));
        List positionHisList = Arrays.stream(positionHisArray).collect(Collectors.toList());
        hisCreateVersionParam.setDataList(positionHisList);
        HrApiResponse hrApiResponse = HisModelServiceHelper.createDataVersions((HisCreateVersionParam)hisCreateVersionParam);
        positionHisList = ((HisCreateVersionReturnData)hrApiResponse.getData()).getDataList().stream().filter(dyn -> !dyn.getBoolean("iscurrentversion")).collect(Collectors.toList());
        IPositionRelationServiceApplication.getInstance().saveSysRelation(positionHisList);
    }
}
