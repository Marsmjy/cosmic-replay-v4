/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.bdversion.BdVersionUtils
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DataEntityState
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.org.utils.Utils
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.service.KDDateFormatUtils
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.basedata.BaseDataServiceHelper
 */
package kd.bos.base.bdversion;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kd.bos.base.bdversion.BdVersionUtils;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DataEntityState;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.org.utils.Utils;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.service.KDDateFormatUtils;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.basedata.BaseDataServiceHelper;

public class BdVersionSaveServicePlugin
extends AbstractOperationServicePlugIn {
    private static final Log log = LogFactory.getLog(BdVersionSaveServicePlugin.class);
    private String nameFieldId;
    private DateFormat dateFormat;
    private Date today;
    private Map<Object, Map<String, Object>> nameVersionParams;
    private Map<String, String> nameVersionChangedProperties;

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        this.createNameHistory(dataEntities);
    }

    private void createNameHistory(DynamicObject[] dataEntities) {
        DynamicObject firstDataEntity = dataEntities[0];
        DynamicProperty nameVersionEntry = firstDataEntity.getDynamicObjectType().getProperty("name$version");
        if (nameVersionEntry == null) {
            return;
        }
        log.debug("\u3010\u540d\u79f0\u7248\u672c\u5316\u3011\u5f00\u59cb\u5904\u7406\u57fa\u7840\u8d44\u6599\u540d\u79f0\u5386\u53f2\uff1a" + this.billEntityType.getName());
        this.nameFieldId = BdVersionUtils.getNameFieldId((DynamicObject)firstDataEntity);
        if (StringUtils.isBlank((CharSequence)this.nameFieldId)) {
            log.info("\u3010\u540d\u79f0\u7248\u672c\u5316\u3011\u83b7\u53d6\u540d\u79f0\u5c5e\u6027\u540d\u5931\u8d25\u3002");
            return;
        }
        this.initParams(dataEntities);
        HashMap<Object, DynamicObject> newNameHistoryObjMap = new HashMap<Object, DynamicObject>(dataEntities.length);
        Boolean enableNameVersion = BaseDataServiceHelper.isEnableNameVersion((String)this.billEntityType.getName());
        for (DynamicObject dataEntity : dataEntities) {
            if (!this.isNamePropertyChanged(dataEntity)) continue;
            DynamicObjectCollection nameHistoryObjCol = dataEntity.getDynamicObjectCollection("name$version");
            if (CollectionUtils.isEmpty((Collection)nameHistoryObjCol)) {
                if (!enableNameVersion.booleanValue()) continue;
                newNameHistoryObjMap.put(dataEntity.getPkValue(), dataEntity);
                continue;
            }
            DynamicObject latestNameHistoryEntry = this.getLatestNameHistoryEntry(nameHistoryObjCol);
            if (latestNameHistoryEntry == null || !this.isNameValueChanged(dataEntity, latestNameHistoryEntry)) continue;
            Date startDate = this.getStartDate(dataEntity.getPkValue());
            this.modifyNameHistory(dataEntity, nameHistoryObjCol, startDate);
            if (enableNameVersion.booleanValue()) {
                if (startDate.compareTo(latestNameHistoryEntry.getDate("name$version$enddate")) > 0) {
                    latestNameHistoryEntry.set("name$version$enddate", (Object)BdVersionUtils.getEndDate((Date)startDate, (int)-1));
                }
                this.addNewNameHistoryEntry(dataEntity, startDate);
                continue;
            }
            this.modifyLatestNameHistory(dataEntity, latestNameHistoryEntry);
        }
        this.addNewNameHistory(newNameHistoryObjMap);
        this.setNameVersionChangedParam();
        log.debug("\u3010\u540d\u79f0\u7248\u672c\u5316\u3011\u5b8c\u6210\u5904\u7406\u57fa\u7840\u8d44\u6599\u540d\u79f0\u5386\u53f2\u3002\u6539\u540d\u914d\u7f6e\u542f\u7528\u72b6\u6001\u662f\uff1a" + enableNameVersion);
    }

    private boolean isNamePropertyChanged(DynamicObject dataEntity) {
        DataEntityState dataEntityState = dataEntity.getDataEntityState();
        if (!dataEntityState.getFromDatabase()) {
            return false;
        }
        Iterable bizChangedProperties = dataEntityState.getBizChangedProperties();
        if (bizChangedProperties == null) {
            return false;
        }
        Iterator changedProps = bizChangedProperties.iterator();
        boolean isNameChanged = false;
        while (changedProps.hasNext()) {
            if (!this.nameFieldId.equals(((IDataEntityProperty)changedProps.next()).getName())) continue;
            isNameChanged = true;
            break;
        }
        return isNameChanged;
    }

    private boolean isNameValueChanged(DynamicObject dataEntity, DynamicObject latestNameHistoryEntry) {
        return BdVersionUtils.isNameChanged((ILocaleString)latestNameHistoryEntry.getLocaleString("name$version$name"), (ILocaleString)dataEntity.getLocaleString(this.nameFieldId));
    }

    private void modifyNameHistory(DynamicObject dataEntity, DynamicObjectCollection nameHistoryObjCol, Date currentStartDate) {
        boolean isNameVersionChanged = false;
        Iterator iterator = nameHistoryObjCol.iterator();
        while (iterator.hasNext()) {
            int today_HisEndDate;
            DynamicObject nameHistoryObj = (DynamicObject)iterator.next();
            if (!nameHistoryObj.getBoolean("name$version$enable") || (today_HisEndDate = this.today.compareTo(nameHistoryObj.getDate("name$version$enddate"))) > 0) continue;
            Date hisStartDate = nameHistoryObj.getDate("name$version$startdate");
            int today_HisStartDate = this.today.compareTo(hisStartDate);
            if (today_HisStartDate < 0) {
                iterator.remove();
                isNameVersionChanged = true;
                continue;
            }
            if (today_HisStartDate == 0 && currentStartDate.compareTo(hisStartDate) == 0) {
                nameHistoryObj.set("name$version$enable", (Object)"0");
                nameHistoryObj.set("name$version$enddate", (Object)BdVersionUtils.getEndDate((Date)this.today, (int)0));
                isNameVersionChanged = true;
                continue;
            }
            if (today_HisEndDate >= 0) continue;
            nameHistoryObj.set("name$version$enddate", (Object)BdVersionUtils.getEndDate((Date)currentStartDate, (int)-1));
            isNameVersionChanged = true;
        }
        this.nameVersionChanged(dataEntity, isNameVersionChanged);
    }

    private void modifyLatestNameHistory(DynamicObject dataEntity, DynamicObject latestNameHistoryEntry) {
        boolean isNameVersionChanged = false;
        int today_latestStartDate = this.today.compareTo(latestNameHistoryEntry.getDate("name$version$startdate"));
        int today_latestEndDate = this.today.compareTo(latestNameHistoryEntry.getDate("name$version$enddate"));
        if (today_latestEndDate < 0 && today_latestStartDate < 0) {
            latestNameHistoryEntry.set("name$version$enddate", (Object)BdVersionUtils.getEndDate((Date)this.today, (int)0));
            isNameVersionChanged = true;
        }
        if (today_latestStartDate == 0) {
            latestNameHistoryEntry.set("name$version$enable", (Object)"0");
            isNameVersionChanged = true;
        }
        this.nameVersionChanged(dataEntity, isNameVersionChanged);
    }

    private void nameVersionChanged(DynamicObject dataEntity, boolean isNameVersionChanged) {
        if (isNameVersionChanged) {
            this.nameVersionChangedProperties.put(dataEntity.getPkValue().toString(), this.nameFieldId);
        }
    }

    private DynamicObject getLatestNameHistoryEntry(DynamicObjectCollection nameHistoryObjCol) {
        Date latestStartDate = null;
        DynamicObject latestNameHistoryEntry = null;
        for (DynamicObject nameHistoryObj : nameHistoryObjCol) {
            if (!nameHistoryObj.getBoolean("name$version$enable")) continue;
            Date hisStartDate = nameHistoryObj.getDate("name$version$startdate");
            if (latestStartDate != null && latestStartDate.compareTo(hisStartDate) >= 0) continue;
            latestStartDate = hisStartDate;
            latestNameHistoryEntry = nameHistoryObj;
        }
        return latestNameHistoryEntry;
    }

    private void addNewNameHistory(Map<Object, DynamicObject> newNameHistoryObjMap) {
        if (newNameHistoryObjMap.isEmpty()) {
            return;
        }
        QFilter filter = new QFilter(this.billEntityType.getPrimaryKey().getName(), "in", newNameHistoryObjMap.keySet());
        QFilter[] filters = new QFilter[]{filter};
        DynamicObject[] dataEntityArr = BusinessDataServiceHelper.load((String)this.billEntityType.getName(), (String)this.nameFieldId, (QFilter[])filters);
        if (dataEntityArr == null || dataEntityArr.length == 0) {
            return;
        }
        Date firstStartDate = BdVersionUtils.getDefaultStartDate();
        for (DynamicObject oldDataEntity : dataEntityArr) {
            DynamicObject dataEntity;
            ILocaleString newName;
            ILocaleString oldName = oldDataEntity.getLocaleString(this.nameFieldId);
            if (!BdVersionUtils.isNameChanged((ILocaleString)oldName, (ILocaleString)(newName = (dataEntity = newNameHistoryObjMap.get(oldDataEntity.getPkValue())).getLocaleString(this.nameFieldId)))) continue;
            Date startDate = this.getStartDate(dataEntity.getPkValue());
            Date lastDate = Utils.addDay((Date)startDate, (int)-1);
            this.addNewNameHistoryEntry(dataEntity, oldName, firstStartDate, lastDate);
            this.addNewNameHistoryEntry(dataEntity, startDate);
        }
    }

    private void addNewNameHistoryEntry(DynamicObject dataEntity, Date startDate) {
        this.addNewNameHistoryEntry(dataEntity, dataEntity.get(this.nameFieldId), startDate, null);
    }

    private void addNewNameHistoryEntry(DynamicObject dataEntity, Object name, Date startDate, Date endDate) {
        DynamicObjectCollection nameHistoryObjCol = dataEntity.getDynamicObjectCollection("name$version");
        DynamicObject newNameHistoryObj = nameHistoryObjCol.addNew();
        newNameHistoryObj.set("seq", (Object)nameHistoryObjCol.size());
        newNameHistoryObj.set("name$version$name", name);
        newNameHistoryObj.set("name$version$startdate", (Object)startDate);
        endDate = endDate == null ? BdVersionUtils.getDefaultEndDate() : BdVersionUtils.getEndDate((Date)endDate, (int)0);
        newNameHistoryObj.set("name$version$enddate", (Object)endDate);
        newNameHistoryObj.set("name$version$enable", (Object)"1");
        newNameHistoryObj.set("name$version$creator", (Object)RequestContext.get().getCurrUserId());
        this.nameVersionChangedProperties.put(dataEntity.getPkValue().toString(), this.nameFieldId);
    }

    private Date getStartDate(Object pk) {
        Map<String, Object> nameVersionMap = this.nameVersionParams.get(pk.toString());
        try {
            if (nameVersionMap == null) {
                return this.today;
            }
            Object startDateParam = nameVersionMap.get("name$version$startdate");
            if (startDateParam == null) {
                return this.today;
            }
            Date startDate = this.dateFormat.parse(startDateParam.toString());
            if (startDate.compareTo(this.today) < 0) {
                startDate = this.today;
            }
            return startDate;
        }
        catch (Exception e) {
            return this.today;
        }
    }

    private void initParams(DynamicObject[] dataEntities) {
        String nameVersionParameters = this.getOption().getVariableValue("nameVersionParameters", null);
        this.nameVersionParams = nameVersionParameters == null ? new HashMap<Object, Map<String, Object>>(0) : (Map)SerializationUtils.fromJsonString((String)nameVersionParameters, Map.class);
        this.nameVersionChangedProperties = new HashMap<String, String>(dataEntities.length);
        this.dateFormat = KDDateFormatUtils.getDateFormat();
        this.today = BdVersionUtils.getToday();
    }

    private void setNameVersionChangedParam() {
        log.debug("\u3010\u540d\u79f0\u7248\u672c\u5316\u3011\u64cd\u4f5c\u53c2\u6570\u8bb0\u5f55\u7684\u53d8\u66f4\u57fa\u7840\u8d44\u6599\uff1a" + this.nameVersionChangedProperties.keySet());
        if (CollectionUtils.isEmpty(this.nameVersionChangedProperties)) {
            return;
        }
        this.getOption().setVariableValue("nameVersionChangedProperties", SerializationUtils.toJsonString(this.nameVersionChangedProperties));
    }
}
