/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IPerSerLenApplicationService
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.PerSerLenValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IPerSerLenApplicationService;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.PerSerLenValidator;

public final class PerSerLenSaveOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(PerSerLenSaveOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("employee");
        fieldKeys.add("assignment");
        fieldKeys.add("iscurrentdata");
        fieldKeys.add("startdate");
        fieldKeys.add("enddate");
        fieldKeys.add("joincomdate");
        fieldKeys.add("adjustcomtime");
        fieldKeys.add("comserviceadjyear");
        fieldKeys.add("iscontinueserviceyear");
        fieldKeys.add("joinworktime");
        fieldKeys.add("adjustworkage");
        fieldKeys.add("adjustworktime");
        fieldKeys.add("comsercount");
        fieldKeys.add("comserviceyear");
        fieldKeys.add("socialworkage");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new PerSerLenValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] perSerLenDys = args.getDataEntities();
        IPerSerLenApplicationService perSerLenApplicationService = IPerSerLenApplicationService.getInstance();
        for (DynamicObject dy : perSerLenDys) {
            ArrayList<DynamicObject> calSerLenDyList = new ArrayList<DynamicObject>(16);
            calSerLenDyList.add(dy);
            BigDecimal comSerAge = perSerLenApplicationService.calComSerAgeByEndDate(calSerLenDyList, new Date(), null);
            dy.set("comsercount", (Object)(comSerAge == null ? BigDecimal.ZERO : comSerAge));
            BigDecimal companyWorkAge = perSerLenApplicationService.calCompanyWorkAgeByEndDate(calSerLenDyList, new Date(), null);
            dy.set("comserviceyear", (Object)(companyWorkAge == null ? BigDecimal.ZERO : companyWorkAge));
            BigDecimal socialWorkAge = perSerLenApplicationService.calSocialWorkAgeByEndDate(calSerLenDyList, new Date(), null);
            dy.set("socialworkage", (Object)(socialWorkAge == null ? BigDecimal.ZERO : socialWorkAge));
            BigDecimal workYear = perSerLenApplicationService.calWorkYearAgeByEndDate(calSerLenDyList, new Date(), null);
            dy.set("workyear", (Object)(workYear == null ? BigDecimal.ZERO : workYear));
        }
    }
}
