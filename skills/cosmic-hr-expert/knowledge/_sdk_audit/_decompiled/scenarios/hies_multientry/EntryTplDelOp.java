/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRQFilterHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hrmp.hies.multientry.opplugin;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRQFilterHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

@ExcludeFromJacocoGeneratedReport
public final class EntryTplDelOp
extends HRDataBaseOp {
    public void endOperationTransaction(EndOperationTransactionArgs args) {
        String operationKey = args.getOperationKey();
        if (HRStringUtils.equals((String)"delete", (String)operationKey)) {
            Set ids = Arrays.stream(args.getDataEntities()).map(dataEntity -> dataEntity.get("id")).collect(Collectors.toSet());
            HRBaseServiceHelper fieldShowNameDBService = new HRBaseServiceHelper("hies_mentry_fieldshowname");
            fieldShowNameDBService.deleteByFilter(new QFilter[]{HRQFilterHelper.buildIn((String)"reltplId", ids)});
        }
    }
}
