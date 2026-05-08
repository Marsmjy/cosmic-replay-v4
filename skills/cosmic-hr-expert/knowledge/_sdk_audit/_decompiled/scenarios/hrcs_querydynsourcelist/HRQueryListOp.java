/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.metadata.dao.MetadataDao
 *  kd.bos.service.operation.validate.BaseDataDeleteValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.multientity.EntityReleaseInfoService
 */
package kd.hr.hrcs.opplugin.web.multientity;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.metadata.dao.MetadataDao;
import kd.bos.service.operation.validate.BaseDataDeleteValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.multientity.EntityReleaseInfoService;

public final class HRQueryListOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        this.getOption().setVariableValue("ignorerefentityids", "bos_printtemplate,bos_devp_formmeta,bos_formmeta");
        BaseDataDeleteValidator baseDataDeleteValidator = new BaseDataDeleteValidator();
        baseDataDeleteValidator.setOption(this.getOption());
        e.addValidator((AbstractValidator)baseDataDeleteValidator);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        for (DynamicObject obj : e.getDataEntities()) {
            MetadataDao.delFormMetadata((String)obj.getString("id"));
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        for (DynamicObject obj : e.getDataEntities()) {
            EntityReleaseInfoService.deleteByEntityNumber((String)obj.getString("number"));
        }
    }
}
