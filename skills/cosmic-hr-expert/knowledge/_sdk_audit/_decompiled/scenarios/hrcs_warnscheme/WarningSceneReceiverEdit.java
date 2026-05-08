/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.Row
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.AppInfo
 *  kd.bos.entity.AppMetadataCache
 *  kd.bos.entity.ISVInfo
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.exception.KDException
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.id.ID
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.ISVServiceHelper
 *  kd.hr.hbp.business.service.complexobj.util.MainEntityTypeUtil
 *  kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.WarnObjTplService
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.receiver.WarnCalcReceiverService
 *  kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo
 *  kd.hr.hrcs.formplugin.web.earlywarn.scene.process.WarnMsgProcessor
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scene;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.AppInfo;
import kd.bos.entity.AppMetadataCache;
import kd.bos.entity.ISVInfo;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.exception.KDException;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.id.ID;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.ISVServiceHelper;
import kd.hr.hbp.business.service.complexobj.util.MainEntityTypeUtil;
import kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.WarnObjTplService;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.receiver.WarnCalcReceiverService;
import kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo;
import kd.hr.hrcs.formplugin.web.earlywarn.scene.process.WarnMsgProcessor;

public final class WarningSceneReceiverEdit
extends HRDataBaseEdit {
    private static final Log LOGGER = LogFactory.getLog(WarningSceneReceiverEdit.class);

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        try {
            this.initRCRelation();
            this.setRCRelationDisplay();
            this.getModel().setDataChanged(false);
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String propName = args.getProperty().getName();
        if (HRStringUtils.equals((String)propName, (String)"warnscene")) {
            DynamicObject warnSceneDy = (DynamicObject)args.getChangeSet()[0].getNewValue();
            if (warnSceneDy != null) {
                this.setRCRelationDisplay();
            } else {
                this.clearRCEntry();
            }
        } else if (HRStringUtils.equals((String)propName, (String)"warnbizobj")) {
            this.clearRCEntry();
        }
    }

    private void setRCRelationDisplay() {
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        DynamicObjectCollection rcRelationEntry = model.getEntryEntity("rcrelationentryentity");
        if (!CollectionUtils.isEmpty((Collection)rcRelationEntry) && !rcRelationEntry.isEmpty()) {
            WarnMsgProcessor processor = new WarnMsgProcessor(this.getView());
            Map idNameMap = processor.getFieldIdNameMapFromCache();
            rcRelationEntry.forEach(entry -> entry.set("rcuserdisplay", idNameMap.get(entry.getString("rcuser"))));
        }
        model.endInit();
        this.getView().updateView("rcrelationentryentity");
    }

    private void initRCRelation() {
        ArrayList comboEditList = Lists.newArrayListWithExpectedSize((int)16);
        QFilter enableFilter = new QFilter("enable", "=", (Object)"1");
        DataSet dataSet = new HRBaseServiceHelper("hrcs_warnmsgrc").queryDataSet("WarningSceneReceiverEdit_query_comboEditList", "number,name", new QFilter[]{enableFilter});
        while (dataSet.hasNext()) {
            Row row = dataSet.next();
            ComboItem comboItem = new ComboItem();
            comboItem.setId(row.getString(0));
            comboItem.setValue(row.getString(0));
            comboItem.setCaption(new LocaleString(row.getString(1)));
            comboEditList.add(comboItem);
        }
        ComboEdit comboEdit = (ComboEdit)this.getView().getControl("rcrelationship");
        comboEdit.setComboItems((List)comboEditList);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        try {
            AbstractOperate op = (AbstractOperate)args.getSource();
            String operateKey = op.getOperateKey();
            if ("rcrelationadd".equals(operateKey)) {
                this.openRCRelationF7();
            } else if ("adminorgadd".equals(operateKey)) {
                args.setCancel(true);
                this.openRCAdminOrgF7();
            } else if ("positionadd".equals(operateKey)) {
                args.setCancel(true);
                this.openRCPositionF7();
            } else if (HRStringUtils.equals((String)"save", (String)operateKey)) {
                this.validatePluginEntry(args);
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        try {
            Object returnData = closedCallBackEvent.getReturnData();
            if ("rcrelationadd".equals(closedCallBackEvent.getActionId())) {
                this.setRcRelationEntryData((ListSelectedRowCollection)returnData);
            } else if ("adminorgadd".equals(closedCallBackEvent.getActionId())) {
                this.setRcAdminOrgEntryData((ListSelectedRowCollection)returnData);
            } else if ("positionadd".equals(closedCallBackEvent.getActionId())) {
                this.setRcPositionEntryData((ListSelectedRowCollection)returnData);
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        try {
            AbstractOperate op = (AbstractOperate)afterDoOperationEventArgs.getSource();
            String operateKey = op.getOperateKey();
            if ("rcfixadd".equals(operateKey) || "rcrelationadd".equals(operateKey) || "rcroleadd".equals(operateKey)) {
                this.getModel().setValue("status", (Object)"A");
            }
        }
        catch (Exception exp) {
            LOGGER.error("error:", (Throwable)exp);
        }
    }

    private void openRCRelationF7() {
        Map<String, WarnQueryFieldBo> map;
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"hrcs_rcrelation", (boolean)true, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "rcrelationadd"));
        List queryFields = JSON.parseArray((String)this.getPageCache().get("queryFields"), WarnQueryFieldBo.class);
        WarnMsgProcessor processor = new WarnMsgProcessor(this.getView());
        Map idNameMap = processor.getFieldIdNameMapFromCache();
        HashSet queryFieldsTemp = Sets.newHashSetWithExpectedSize((int)queryFields.size());
        Map<String, WarnQueryFieldBo> finalMap = map = this.getAllRCField();
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("rcrelationentryentity");
        entryEntity.forEach(entry -> {
            WarnQueryFieldBo cfr_ignored_0 = (WarnQueryFieldBo)finalMap.remove(entry.getString("rcuser"));
        });
        queryFields.stream().filter(bo -> WarnCalcReceiverService.getInstance().isReceiverField(bo.getBaseDataNum())).forEach(bo2 -> {
            WarnQueryFieldBo warnQueryFieldBo = bo2.getFieldAlias().endsWith(".id") ? (WarnQueryFieldBo)finalMap.get(bo2.getFieldAlias()) : (WarnQueryFieldBo)finalMap.get(bo2.getFieldAlias().substring(0, bo2.getFieldAlias().lastIndexOf(".")).concat(".id"));
            if (warnQueryFieldBo != null) {
                queryFieldsTemp.add(warnQueryFieldBo);
            }
        });
        if (queryFieldsTemp.size() > 0) {
            long[] ids = ID.genLongIds((int)queryFieldsTemp.size());
            int index = 0;
            for (WarnQueryFieldBo bo3 : queryFieldsTemp) {
                String displayName = (String)idNameMap.get(bo3.getFieldAlias());
                if (HRStringUtils.isNotEmpty((String)displayName)) {
                    bo3.setFieldName(new LocaleString(displayName));
                }
                bo3.setId(String.valueOf(ids[index]));
                ++index;
            }
        }
        fsp.setCustomParam("queryFields", (Object)SerializationUtils.toJsonString((Object)queryFieldsTemp));
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void openRCAdminOrgF7() {
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"haos_adminorghrf7", (boolean)true, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "adminorgadd"));
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("adminorgentry");
        List ids = entryEntity.stream().map(dy -> dy.getDynamicObject("adminorgfield").getLong("id")).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            QFilter qFilter = new QFilter("id", "not in", ids);
            fsp.getListFilterParameter().getQFilters().add(qFilter);
        }
        fsp.setCustomParam("customHREntityNumber", (Object)this.getModel().getDataEntity().getDataEntityType().getName());
        fsp.setCustomParam("custom_parent_f7_prop", (Object)"adminorgfield");
        this.getView().cacheFormShowParameter();
        fsp.setCustomParam("struct_project_ids", (Object)SerializationUtils.toJsonString(Collections.singletonList(1010)));
        fsp.setCustomParam("struct_project_is_to_all_areas", (Object)"true");
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void openRCPositionF7() {
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"hbpm_positionhrf7", (boolean)true, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "positionadd"));
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("positionentry");
        List ids = entryEntity.stream().map(dy -> dy.getDynamicObject("position").getLong("id")).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            QFilter qFilter = new QFilter("id", "not in", ids);
            fsp.getListFilterParameter().getQFilters().add(qFilter);
        }
        this.getView().showForm((FormShowParameter)fsp);
    }

    private Map<String, WarnQueryFieldBo> getAllRCField() {
        DynamicObject sceneDy;
        Map<Object, Object> map = Maps.newHashMapWithExpectedSize((int)16);
        String allQueryFieldStr = this.getView().getPageCache().get("queryFields");
        if (HRStringUtils.isEmpty((String)allQueryFieldStr) && (sceneDy = (DynamicObject)this.getModel().getValue("warnscene")) != null) {
            List queryFields = JSON.parseArray((String)this.getPageCache().get("queryFields"), WarnQueryFieldBo.class);
            allQueryFieldStr = SerializationUtils.toJsonString((Object)queryFields);
            this.getPageCache().put("queryFields", allQueryFieldStr);
        }
        if (HRStringUtils.isNotEmpty((String)allQueryFieldStr)) {
            List WarnQueryFieldBos = JSON.parseArray((String)allQueryFieldStr, WarnQueryFieldBo.class);
            WarnQueryFieldBos = WarnQueryFieldBos.stream().map(field -> {
                if (AnalyseObjectUtil.isBaseDataType((String)field.getComplexType())) {
                    Map baseDataNumAndPkType = AnalyseObjectUtil.parseFieldAliasGetBaseDataNumAndPkType((String)field.getEntityNumber(), (String)field.getFieldAlias(), (MainEntityTypeUtil)new MainEntityTypeUtil());
                    field.setBaseDataNum((String)baseDataNumAndPkType.get("baseDataNum"));
                    field.setBaseDataIdType((String)baseDataNumAndPkType.get("pkType"));
                }
                return field;
            }).collect(Collectors.toList());
            map = WarnQueryFieldBos.stream().filter(bo -> WarnCalcReceiverService.getInstance().isReceiverField(bo.getBaseDataNum())).collect(Collectors.toMap(QueryFieldCommonBo::getFieldAlias, Function.identity(), (x1, x2) -> x2));
        }
        return map;
    }

    private void setRcRelationEntryData(ListSelectedRowCollection selectedRows) {
        if (null == selectedRows) {
            return;
        }
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        int startIdx = this.getModel().getEntryEntity("rcrelationentryentity").size();
        if (!CollectionUtils.isEmpty((Collection)selectedRows)) {
            model.batchCreateNewEntryRow("rcrelationentryentity", selectedRows.size());
            for (int i = 0; i < selectedRows.size(); ++i) {
                int idx = startIdx + i;
                ListSelectedRow row = selectedRows.get(i);
                model.setValue("rcuser", (Object)row.getNumber(), idx);
                model.setValue("rcuserdisplay", (Object)row.getName(), idx);
            }
        }
        this.getView().updateView("rcrelationentryentity");
    }

    private void setRcAdminOrgEntryData(ListSelectedRowCollection selectedRows) {
        if (null == selectedRows) {
            return;
        }
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        int startIdx = this.getModel().getEntryEntity("adminorgentry").size();
        if (!CollectionUtils.isEmpty((Collection)selectedRows)) {
            model.batchCreateNewEntryRow("adminorgentry", selectedRows.size());
            model.beginInit();
            for (int i = 0; i < selectedRows.size(); ++i) {
                int idx = startIdx + i;
                ListSelectedRow row = selectedRows.get(i);
                model.setValue("adminorgfield", row.getPrimaryKeyValue(), idx);
            }
            model.endInit();
        }
        this.getView().updateView("adminorgentry");
    }

    private void setRcPositionEntryData(ListSelectedRowCollection selectedRows) {
        if (null == selectedRows) {
            return;
        }
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        int startIdx = this.getModel().getEntryEntity("positionentry").size();
        if (!CollectionUtils.isEmpty((Collection)selectedRows)) {
            model.batchCreateNewEntryRow("positionentry", selectedRows.size());
            model.beginInit();
            for (int i = 0; i < selectedRows.size(); ++i) {
                int idx = startIdx + i;
                ListSelectedRow row = selectedRows.get(i);
                model.setValue("position", row.getPrimaryKeyValue(), idx);
            }
            model.endInit();
        }
        this.getView().updateView("positionentry");
    }

    private void validatePluginEntry(BeforeDoOperationEventArgs args) {
        if (args.isCancel()) {
            return;
        }
        DynamicObjectCollection pluginEntry = this.getModel().getEntryEntity("pluginentry");
        StringBuilder errorMsg = new StringBuilder();
        for (int i = 0; i < pluginEntry.size(); ++i) {
            String serviceFactoryClassName;
            String[] split;
            boolean formatError;
            DynamicObject row = (DynamicObject)pluginEntry.get(i);
            String serviceClass = row.getString("serviceclass");
            if (HRStringUtils.isEmpty((String)serviceClass)) continue;
            boolean bl = formatError = !serviceClass.contains(".");
            if (!(formatError || (split = serviceClass.split("\\.")).length == 2 && split[0].length() != 0 && split[1].length() != 0)) {
                formatError = true;
            }
            if (formatError) {
                errorMsg.append(String.format(ResManager.loadKDString((String)"\u6d88\u606f\u63a5\u6536\u4eba\u8bbe\u7f6e\u63d2\u4ef6\u7b2c%1$s\u884c:\u670d\u52a1\u7c7b\u683c\u5f0f\u4e0d\u6b63\u786e\uff0c\u8bf7\u6309\u201c\u5e94\u7528\u6807\u8bc6.\u5fae\u670d\u52a1\u540d\u79f0\u201d\u586b\u5199\u3002\r\n", (String)"WarningSceneReceiverEdit_1", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), i + 1));
                continue;
            }
            String[] splitServiceClass = serviceClass.split("\\.");
            String appId = splitServiceClass[0];
            String serviceClassName = splitServiceClass[1];
            String cloudNum = "";
            try {
                AppInfo appInfoByNumber = AppMetadataCache.getAppInfoByNumber((String)appId);
                cloudNum = appInfoByNumber.getCloudNum();
                cloudNum = cloudNum.toLowerCase(Locale.ROOT);
            }
            catch (KDException exception) {
                errorMsg.append(String.format(ResManager.loadKDString((String)"\u6d88\u606f\u63a5\u6536\u4eba\u8bbe\u7f6e\u63d2\u4ef6\u7b2c%1$s\u884c:\u5e94\u7528\u6807\u8bc6\u4e0d\u5b58\u5728\u3002\r\n", (String)"WarningSceneReceiverEdit_2", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), i + 1));
                continue;
            }
            WarnObjTplService warnObjTplService = WarnObjTplService.getInstance();
            if (!warnObjTplService.isKingdeeIsv(appId)) {
                ISVInfo isvInfo = ISVServiceHelper.getISVInfo();
                serviceFactoryClassName = String.join((CharSequence)".", isvInfo.getId(), cloudNum, appId, "ServiceFactory");
            } else {
                serviceFactoryClassName = String.format("kd.%s.%s.servicehelper.ServiceFactory", cloudNum, appId);
            }
            try {
                Class<?> aClass = Class.forName(serviceFactoryClassName);
                Object instance = aClass.newInstance();
                try {
                    aClass.getMethod("getService", String.class).invoke(instance, serviceClassName);
                }
                catch (InvocationTargetException exception) {
                    errorMsg.append(String.format(ResManager.loadKDString((String)"\u6d88\u606f\u63a5\u6536\u4eba\u8bbe\u7f6e\u63d2\u4ef6\u7b2c%1$s\u884c:\u670d\u52a1\u4e0d\u5b58\u5728\u3002\r\n", (String)"WarningSceneReceiverEdit_3", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), i + 1));
                }
                continue;
            }
            catch (ClassNotFoundException exception) {
                LOGGER.error("ServiceFactoryClassName: {} is not exist.", (Object)serviceFactoryClassName);
                continue;
            }
            catch (IllegalAccessException | NoSuchMethodException e) {
                LOGGER.error("ServiceFactoryClassName: {} invoke method error.", (Object)serviceFactoryClassName);
                continue;
            }
            catch (InstantiationException e) {
                LOGGER.error("ServiceFactoryClassName: {} new instance error.", (Object)serviceFactoryClassName);
            }
        }
        if (errorMsg.length() > 0) {
            args.setCancel(true);
            this.getView().showTipNotification(errorMsg.toString());
        }
    }

    private void clearRCEntry() {
        this.getModel().deleteEntryData("rcrelationentryentity");
        this.getView().updateView("rcrelationentryentity");
        this.getModel().deleteEntryData("rcroleentryentity");
        this.getView().updateView("rcroleentryentity");
        this.getModel().deleteEntryData("adminorgentry");
        this.getView().updateView("adminorgentry");
        this.getModel().deleteEntryData("positionentry");
        this.getView().updateView("positionentry");
        this.getModel().deleteEntryData("pluginentry");
        this.getView().updateView("pluginentry");
        this.getModel().deleteEntryData("rcfixentryentity");
        this.getView().updateView("rcfixentryentity");
    }
}
