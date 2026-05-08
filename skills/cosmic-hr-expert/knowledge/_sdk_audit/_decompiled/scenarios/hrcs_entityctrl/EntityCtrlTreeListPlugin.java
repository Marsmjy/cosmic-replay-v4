/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.IPageCache
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.AfterQueryOfExportEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.plugin.AbstractTreeListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.cache.HRAppCache
 *  kd.hr.hbp.common.cache.IHRAppCache
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.service.perm.ChoiceFieldPageCustomQueryService
 *  kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper
 *  org.apache.commons.collections.CollectionUtils
 *  org.apache.commons.collections.MapUtils
 */
package kd.hr.hrcs.formplugin.web.perm.dimension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.IPageCache;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.AfterQueryOfExportEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.plugin.AbstractTreeListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.cache.HRAppCache;
import kd.hr.hbp.common.cache.IHRAppCache;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.service.perm.ChoiceFieldPageCustomQueryService;
import kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr;
import kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

@ExcludeFromJacocoGeneratedReport
public final class EntityCtrlTreeListPlugin
extends AbstractTreeListPlugin {
    private static final Log LOGGER = LogFactory.getLog(EntityCtrlTreeListPlugin.class);

    public void setFilter(SetFilterEvent evt) {
        super.setFilter(evt);
        evt.getQFilters().add(new QFilter("entitytype.number", "is not null", null));
        Set noExistedEntityNumbers = EntityCtrlServiceHelper.getAllNoExistEntityCtrlNumbers();
        if (CollectionUtils.isNotEmpty((Collection)noExistedEntityNumbers)) {
            LOGGER.warn("hrcs_entityctrl noExistedEntityNumbers:{}", (Object)noExistedEntityNumbers);
            evt.getQFilters().add(new QFilter("entitytype", "not in", (Object)noExistedEntityNumbers));
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        Set entryIds;
        ListSelectedRowCollection listSelectedData;
        Set ids;
        boolean isPass;
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        String operateKey = formOperate.getOperateKey();
        if (operateKey.equals("delete") && !(isPass = EntityCtrlServiceHelper.beforeDelOp(ids = (listSelectedData = args.getListSelectedData()).stream().map(it -> Long.valueOf(String.valueOf(it.getPrimaryKeyValue()))).collect(Collectors.toSet()), entryIds = listSelectedData.stream().map(it -> Long.valueOf(String.valueOf(it.getEntryPrimaryKeyValue()))).collect(Collectors.toSet()), (OperateOption)formOperate.getOption()))) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u9884\u7f6e\u6570\u636e\u65e0\u6cd5\u5220\u9664\u3002", (String)"EntityCtrlTreeListPlugin_01", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        String operateKey = args.getOperateKey();
        OperationResult operationResult = args.getOperationResult();
        if (operateKey.equals("delete") && operationResult.isSuccess()) {
            HRPermCacheMgr.clearAllCache();
        }
    }

    public void afterQueryOfExport(AfterQueryOfExportEvent evt) {
        IPageCache pageCache = this.getPageCache();
        String entityFieldNameMapStr = pageCache.get("entityFieldNameMap");
        HashMap<String, Map<String, String>> entityFieldNameMap = HRStringUtils.isNotEmpty((String)entityFieldNameMapStr) ? (HashMap<String, Map<String, String>>)SerializationUtils.fromJsonString((String)entityFieldNameMapStr, Map.class) : new HashMap<String, Map<String, String>>(16);
        IHRAppCache appCache = HRAppCache.get((String)"hrcs");
        HashMap<String, Set<String>> entityFields = (HashMap<String, Set<String>>)appCache.get("entityFields", Map.class);
        if (MapUtils.isEmpty((Map)entityFields)) {
            entityFields = new HashMap<String, Set<String>>(16);
        }
        DynamicObject[] entityCtrls = evt.getQueryValues();
        ChoiceFieldPageCustomQueryService customQuery = new ChoiceFieldPageCustomQueryService();
        Map customParams = this.getView().getFormShowParameter().getCustomParams();
        for (DynamicObject entityCtrl : entityCtrls) {
            String entity = entityCtrl.getString("entitytype.id");
            Map<String, String> fieldMap = (Map<String, String>)entityFieldNameMap.get(entity);
            if (MapUtils.isEmpty((Map)fieldMap)) {
                MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entity);
                fieldMap = customQuery.parsePropertySub((IDataEntityType)mainEntityType, null, customParams, "1=1", new ArrayList(10)).stream().collect(Collectors.toMap(it -> (String)it.get("field_id"), it -> (String)it.get("field_name"), (newVal, oldVal) -> oldVal));
                entityFieldNameMap.put(entity, fieldMap);
                entityFields.put(entity, fieldMap.keySet());
            }
            DynamicObjectCollection collection = entityCtrl.getDynamicObjectCollection("entryentity");
            Iterator iterator = collection.iterator();
            while (iterator.hasNext()) {
                DynamicObject row;
                String propKey = (row = (DynamicObject)iterator.next()).getString("propkey");
                String propName = fieldMap.get(propKey);
                row.set("propname", (Object)(HRStringUtils.isEmpty((String)propName) ? propKey : propName));
            }
        }
        pageCache.put("entityFieldNameMap", SerializationUtils.toJsonString(entityFieldNameMap));
        appCache.put("entityFields", entityFields);
    }
}
