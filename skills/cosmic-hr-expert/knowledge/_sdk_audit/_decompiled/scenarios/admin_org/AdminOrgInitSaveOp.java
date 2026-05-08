/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.haos.business.application.service.ServiceFactory
 *  kd.hr.haos.business.application.service.init.IOrgHisVerInitService
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisBelongCompanyValidator
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisEffDateContinuityValidator
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisEffDateLegitimacyValidator
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisEndDateRangeValidator
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisEndDateValidator
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisFirstEffDateConsistencyValidator
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisMigratedValidator
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisOrgCurrVerParentValidator
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisOrgErrValidator
 *  kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisOrgParentValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.adminorg.init;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.haos.business.application.service.ServiceFactory;
import kd.hr.haos.business.application.service.init.IOrgHisVerInitService;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisBelongCompanyValidator;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisEffDateContinuityValidator;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisEffDateLegitimacyValidator;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisEndDateRangeValidator;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisEndDateValidator;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisFirstEffDateConsistencyValidator;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisMigratedValidator;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisOrgCurrVerParentValidator;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisOrgErrValidator;
import kd.hr.haos.opplugin.web.adminorg.init.validate.OrgHisOrgParentValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class AdminOrgInitSaveOp
extends HRDataBaseOp {
    private IOrgHisVerInitService validService = (IOrgHisVerInitService)ServiceFactory.getService(IOrgHisVerInitService.class);

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new OrgHisEndDateValidator());
        args.addValidator((AbstractValidator)new OrgHisEndDateRangeValidator());
        args.addValidator((AbstractValidator)new OrgHisEffDateContinuityValidator());
        args.addValidator((AbstractValidator)new OrgHisMigratedValidator());
        args.addValidator((AbstractValidator)new OrgHisEffDateLegitimacyValidator());
        args.addValidator((AbstractValidator)new OrgHisFirstEffDateConsistencyValidator());
        args.addValidator((AbstractValidator)new OrgHisBelongCompanyValidator());
        args.addValidator((AbstractValidator)new OrgHisOrgParentValidator());
        args.addValidator((AbstractValidator)new OrgHisOrgCurrVerParentValidator());
        args.addValidator((AbstractValidator)new OrgHisOrgErrValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
    }

    public void endOperationTransaction(EndOperationTransactionArgs args) {
        DynamicObject[] dys = args.getDataEntities();
        List validDataList = Arrays.stream(dys).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(validDataList)) {
            return;
        }
        validDataList.sort(Comparator.comparing(dyn -> dyn.getDate("bsed")));
        this.validService.saveOrgHisVersion(validDataList);
    }
}
