/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.db.DB
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.servicehelper.DBServiceHelper
 *  kd.hr.hbp.common.util.HRDynamicObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.servicehelper.activity.ActivitySchemeServiceHelper
 *  kd.hr.hrcs.opplugin.validator.activity.ActivitySchemeSaveValidator
 */
package kd.hr.hrcs.opplugin.web.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.db.DB;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.servicehelper.DBServiceHelper;
import kd.hr.hbp.common.util.HRDynamicObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.servicehelper.activity.ActivitySchemeServiceHelper;
import kd.hr.hrcs.opplugin.validator.activity.ActivitySchemeSaveValidator;

public final class ActivitySchemeSaveOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        e.getValidators().clear();
        e.addValidator((AbstractValidator)new ActivitySchemeSaveValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        Map variables = this.getOption().getVariables();
        if (null != variables && HRStringUtils.equals((String)"true", (String)((String)variables.get("batchimport")))) {
            return;
        }
        ArrayList<Long> preIds = new ArrayList<Long>(args.getDataEntities().length);
        DynamicObject[] items = new DynamicObject[args.getDataEntities().length];
        long[] ids = DB.genGlobalLongIds((int)args.getDataEntities().length);
        Date now = new Date();
        for (int i = 0; i < args.getDataEntities().length; ++i) {
            preIds.add((Long)args.getDataEntities()[i].getPkValue());
            DynamicObject item = ActivitySchemeServiceHelper.generateEmptyActivityScheme();
            HRDynamicObjectUtils.copy((DynamicObject)args.getDataEntities()[i], (DynamicObject)item);
            item.set("createtime", (Object)now);
            item.set("modifytime", (Object)now);
            long[] longIds = DBServiceHelper.genLongIds((String)"hrcs_activityscheme", (int)1);
            args.getDataEntities()[i].set("id", (Object)longIds[0]);
            item.set("id", (Object)longIds[0]);
            item.set("masterid", (Object)longIds[0]);
            if (0L == item.getLong("sequence")) {
                item.set("sequence", (Object)ids[i]);
            }
            items[i] = item;
        }
        args.setDataEntities(items);
        this.getOption().setVariableValue("preIds", SerializationUtils.toJsonString(preIds));
    }

    public void endOperationTransaction(EndOperationTransactionArgs args) {
        List preIds = SerializationUtils.fromJsonStringToList((String)this.getOption().getVariableValue("preIds", "[]"), Long.class);
        if (null != preIds && !preIds.isEmpty()) {
            ActivitySchemeServiceHelper.disableLatestById((List)preIds);
        }
    }
}
