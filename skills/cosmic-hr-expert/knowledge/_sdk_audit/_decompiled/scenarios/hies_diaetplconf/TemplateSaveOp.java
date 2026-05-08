/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.id.ID
 *  kd.bos.orm.query.QFilter
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRQFilterHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hies.common.util.HIESUtil
 *  kd.hr.hies.opplugin.validate.TemplateSaveValidator
 */
package kd.hr.hies.opplugin.web;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.id.ID;
import kd.bos.orm.query.QFilter;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRQFilterHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hies.common.util.HIESUtil;
import kd.hr.hies.opplugin.validate.TemplateSaveValidator;

@ExcludeFromJacocoGeneratedReport
public final class TemplateSaveOp
extends HRDataBaseOp {
    private static final String FIELD_ENTITYFID = "childentity";
    private HRBaseServiceHelper fieldShowNameDBService = new HRBaseServiceHelper("hies_fieldshowname");
    private Map<Object, String> entitydescriptionTemp = new HashMap<Object, String>(16);

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new TemplateSaveValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        DynamicObject[] dys;
        for (DynamicObject dataDyn : dys = args.getDataEntities()) {
            if (dataDyn.getLong("id") == 0L) {
                dataDyn.set("id", (Object)ID.genLongId());
            }
            DynamicObjectCollection fieldCollection = dataDyn.getDynamicObjectCollection("tpltreeentryentity");
            LinkedList<DynamicObject> dynsSave = new LinkedList<DynamicObject>();
            for (DynamicObject dyn : fieldCollection) {
                String fieldNumber = dyn.getString("fieldnumber");
                String entityNumber = dyn.getString(FIELD_ENTITYFID);
                String fieldAllNumber = entityNumber + "." + fieldNumber;
                String entitydescription = HIESUtil.trimTplFieldEntitydescription((String)dyn.getString("entitydescription"));
                this.addFieldShowName(dataDyn.getPkValue(), fieldAllNumber, entitydescription, dynsSave);
            }
            QFilter[] filters = new QFilter[]{HRQFilterHelper.buildEql((String)"reltplId", (Object)dataDyn.getPkValue())};
            this.fieldShowNameDBService.deleteByFilter(filters);
            this.fieldShowNameDBService.save(dynsSave.toArray(new DynamicObject[0]));
            fieldCollection.forEach(field -> {
                String localeString = field.getString("entitydescription");
                if (HRStringUtils.isNotEmpty((String)localeString)) {
                    this.entitydescriptionTemp.put(field.getPkValue(), localeString);
                }
                field.set("entitydescription", null);
            });
            if (dataDyn.getDataEntityState().getFromDatabase()) continue;
            if ("external".equalsIgnoreCase(dataDyn.getString("source"))) {
                dataDyn.set("enable", (Object)"1");
                continue;
            }
            String tplGenMode = dataDyn.getString("tplgenmode");
            if ("localupload".equals(tplGenMode)) {
                DynamicObjectCollection dataImportCfgEntrys = dataDyn.getDynamicObjectCollection("entryentity");
                if (CollectionUtils.isEmpty((Collection)dataImportCfgEntrys)) {
                    dataDyn.set("enable", (Object)"10");
                    continue;
                }
                dataDyn.set("enable", (Object)"1");
                continue;
            }
            dataDyn.set("enable", (Object)"1");
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        DynamicObject[] dys;
        super.afterExecuteOperationTransaction(e);
        for (DynamicObject dataDyn : dys = e.getDataEntities()) {
            DynamicObjectCollection fieldCollection = dataDyn.getDynamicObjectCollection("tpltreeentryentity");
            fieldCollection.forEach(field -> {
                String val = this.entitydescriptionTemp.get(field.getPkValue());
                if (val != null) {
                    field.set("entitydescription", (Object)new LocaleString(val));
                }
            });
        }
    }

    void addFieldShowName(Object tplId, String relfieldnumber, String entitydescription, List<DynamicObject> dynsSave) {
        Map map = Maps.newHashMapWithExpectedSize((int)3);
        if (StringUtils.isNotEmpty((CharSequence)entitydescription)) {
            map = (Map)SerializationUtils.fromJsonString((String)entitydescription, Map.class);
        }
        Object finalTplId = tplId;
        map.entrySet().stream().forEach(item -> {
            DynamicObject dynObj = this.fieldShowNameDBService.generateEmptyDynamicObject();
            Map entry = (Map)SerializationUtils.fromJsonString((String)((String)item.getValue()), Map.class);
            dynObj.set("reltplId", finalTplId);
            dynObj.set("relfieldnumber", (Object)relfieldnumber);
            dynObj.set("number", item.getKey());
            if (entry.containsKey("displayname")) {
                LocaleString lang = (LocaleString)SerializationUtils.fromJsonString((String)((String)entry.get("displayname")), LocaleString.class);
                dynObj.set("name", (Object)lang);
            }
            if (entry.containsKey("fieldimportdesc")) {
                LocaleString fieldimportdesclang = (LocaleString)SerializationUtils.fromJsonString((String)((String)entry.get("fieldimportdesc")), LocaleString.class);
                dynObj.set("fieldimportdesc", (Object)fieldimportdesclang);
            }
            dynObj.set("isshow", entry.get("isshow"));
            dynsSave.add(dynObj);
        });
    }
}
