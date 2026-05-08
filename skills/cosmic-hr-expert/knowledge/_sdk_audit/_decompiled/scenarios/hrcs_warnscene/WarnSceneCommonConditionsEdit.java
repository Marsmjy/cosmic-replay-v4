/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.RefObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.ISimpleProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.LongProp
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.events.TabSelectEvent
 *  kd.bos.form.control.events.TabSelectListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.application.impl.common.HRLongValueParseService
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.utils.WarnCommonConditionUtils
 *  kd.hr.hrcs.common.constants.earlywarn.WarnCalFieldConstants
 *  kd.hr.hrcs.common.constants.earlywarn.WarnSceneComConditionConstants
 *  kd.hr.hrcs.common.constants.earlywarn.WarnSceneFieldConstants
 *  kd.hr.hrcs.common.constants.earlywarn.WarningSceneConstants
 *  kd.hr.hrcs.common.model.earlywarn.WarnCommonConditionBo
 *  kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonEdit
 *  kd.hr.hrcs.formplugin.web.earlywarn.scene.process.WarnComConditionProcessor
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scene;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.RefObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.ISimpleProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.LongProp;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.container.Tab;
import kd.bos.form.control.events.TabSelectEvent;
import kd.bos.form.control.events.TabSelectListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.application.impl.common.HRLongValueParseService;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.utils.WarnCommonConditionUtils;
import kd.hr.hrcs.common.constants.earlywarn.WarnCalFieldConstants;
import kd.hr.hrcs.common.constants.earlywarn.WarnSceneComConditionConstants;
import kd.hr.hrcs.common.constants.earlywarn.WarnSceneFieldConstants;
import kd.hr.hrcs.common.constants.earlywarn.WarningSceneConstants;
import kd.hr.hrcs.common.model.earlywarn.WarnCommonConditionBo;
import kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonEdit;
import kd.hr.hrcs.formplugin.web.earlywarn.scene.process.WarnComConditionProcessor;
import org.apache.commons.lang3.StringUtils;

@ExcludeFromJacocoGeneratedReport
public final class WarnSceneCommonConditionsEdit
extends WarnSceneCommonEdit
implements WarningSceneConstants,
WarnCalFieldConstants,
WarnSceneFieldConstants,
WarnSceneComConditionConstants,
TabSelectListener {
    private static final Log LOGGER = LogFactory.getLog(WarnSceneCommonConditionsEdit.class);
    private static final String CACHE_KEY_COMMON_CONDITION_INIT = "CACHE_KEY_COMMON_CONDITION_INIT";
    private static final String CACHE_KEY_TWICE_CLOSE = "CACHE_KEY_TWICE_CLOSE";
    private static final String CACHE_KEY_TAB_SELECT = "CACHE_KEY_TAB_SELECT";
    private static final String META_NUMBER_BOS_OPERATION_RESULT = "bos_operationresult";

    public void registerListener(EventObject eventObject) {
        try {
            super.registerListener(eventObject);
            Tab tab = (Tab)this.getView().getControl("maintabap");
            tab.addTabSelectListener((TabSelectListener)this);
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneCommonConditionsEdit_registerListener_error_", (Throwable)exception);
        }
    }

    public void tabSelected(TabSelectEvent tabSelectEvent) {
        try {
            String tabKey = tabSelectEvent.getTabKey();
            if ("commonconditiontab".equals(tabKey)) {
                this.getPageCache().put(CACHE_KEY_TAB_SELECT, "true");
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneCommonConditionsEdit_tabSelected_error_", (Throwable)exception);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        try {
            super.beforeDoOperation(args);
            AbstractOperate op = (AbstractOperate)args.getSource();
            String operateKey = op.getOperateKey();
            String isSelected = this.getPageCache().get(CACHE_KEY_TAB_SELECT);
            if (("save".equals(operateKey) || "laststep".equals(operateKey)) && "true".equals(isSelected) && !op.getOption().tryGetVariableValue("OP_KEY_GET_COMMON_CONDITION_LAST_VALUE", new RefObject())) {
                this.beforeDoOp(operateKey);
                args.setCancel(true);
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void beforeClosed(BeforeClosedEvent closedEvent) {
        try {
            super.beforeClosed(closedEvent);
            String closeTag = this.getPageCache().get(CACHE_KEY_TWICE_CLOSE);
            String isSelected = this.getPageCache().get(CACHE_KEY_TAB_SELECT);
            if (!"true".equals(closeTag) && "true".equals(isSelected)) {
                this.beforeDoOp("close");
                this.getPageCache().remove(CACHE_KEY_TWICE_CLOSE);
                closedEvent.setCancel(true);
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneCommonConditionsEdit_beforeClosed_error_", (Throwable)exception);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        try {
            Tab tab;
            String currentTab;
            super.afterDoOperation(afterDoOperationEventArgs);
            AbstractOperate op = (AbstractOperate)afterDoOperationEventArgs.getSource();
            String operateKey = op.getOperateKey();
            if ("nextstep".equals(operateKey) && afterDoOperationEventArgs.getOperationResult().isSuccess() && "definefield".equals(currentTab = (tab = (Tab)this.getView().getControl("tabap")).getCurrentTab())) {
                String init = this.getPageCache().get(CACHE_KEY_COMMON_CONDITION_INIT);
                if (!"true".equals(init)) {
                    this.initProcessor.initCommonCondition("initControl");
                    this.getPageCache().put(CACHE_KEY_COMMON_CONDITION_INIT, "true");
                } else {
                    this.initProcessor.initCommonCondition("reRenderControl");
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        this.cacheSelConditionField();
    }

    public void customEvent(CustomEventArgs eventArgs) {
        try {
            super.customEvent(eventArgs);
            String key = eventArgs.getKey();
            String args = eventArgs.getEventArgs();
            String eventName = eventArgs.getEventName();
            if ("hrwarnscene".equals(key)) {
                if ("openDefaultValueF7".equals(eventName)) {
                    this.openDefaultValueF7(args);
                } else if ("openOptionalRangeF7".equals(eventName)) {
                    this.openOptionalRangeF7(args);
                } else if ("delCondition".equals(eventName)) {
                    this.deleteCondition(args);
                } else if ("updateConditionAllData".equals(eventName)) {
                    this.updateConditionAllData(args);
                } else if ("doOp".equals(eventName)) {
                    this.doOp(args);
                } else if ("selectQueryFieldBos".equals(eventName)) {
                    WarnComConditionProcessor processor = new WarnComConditionProcessor((HRDataBaseEdit)this);
                    processor.openSelectFieldPage(args);
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        try {
            super.closedCallBack(closedCallBackEvent);
            String actionId = closedCallBackEvent.getActionId();
            if (HRStringUtils.isNotEmpty((String)actionId)) {
                if (actionId.startsWith("openDefaultValueF7") || actionId.startsWith("openOptionalRangeF7")) {
                    String jsMethodKey;
                    String eventKey;
                    if (actionId.startsWith("openDefaultValueF7")) {
                        eventKey = "openDefaultValueF7";
                        jsMethodKey = "setDefaultValueCallBack";
                    } else {
                        eventKey = "openOptionalRangeF7";
                        jsMethodKey = "setOptionalRangeCallBack";
                    }
                    String[] keys = actionId.split("\\$");
                    if (keys.length < 2) {
                        return;
                    }
                    String rowNumStr = keys[0].replaceFirst(eventKey + "_", "");
                    String fieldAlias = keys[1];
                    ListSelectedRowCollection returnRows = (ListSelectedRowCollection)closedCallBackEvent.getReturnData();
                    if (null == returnRows || 0 == returnRows.size()) {
                        return;
                    }
                    ArrayList ids = Lists.newArrayListWithExpectedSize((int)returnRows.size());
                    ArrayList names = Lists.newArrayListWithExpectedSize((int)returnRows.size());
                    for (ListSelectedRow returnRow : returnRows) {
                        ids.add(String.valueOf(returnRow.getPrimaryKeyValue()));
                        if (fieldAlias.endsWith(".number")) {
                            names.add(returnRow.getNumber());
                            continue;
                        }
                        names.add(returnRow.getName());
                    }
                    this.setBaseDataFormCallBack(rowNumStr, StringUtils.join((Iterable)ids, (String)","), StringUtils.join((Iterable)names, (String)"\uff1b"), jsMethodKey);
                } else if ("warn_field_select_callback".equals(actionId)) {
                    Map paramMap = (Map)closedCallBackEvent.getReturnData();
                    WarnComConditionProcessor processor = new WarnComConditionProcessor((HRDataBaseEdit)this);
                    processor.setSelQueryFieldCallBack(paramMap, "hrwarnscene");
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String changeKey = args.getProperty().getName();
        if ("commoncondition".equals(changeKey)) {
            this.cacheSelConditionField();
        }
    }

    private void openDefaultValueF7(String args) {
        JSONObject data = JSONObject.parseObject((String)args);
        String entityNumber = data.getString("entityNumber");
        String rowIndex = data.getString("rowIndex");
        String baseDataIds = data.getString("baseDataIds");
        String multi = data.getString("multi");
        String fieldAlias = data.getString("fieldAlias");
        this.showBaseDataF7(entityNumber, fieldAlias, multi, baseDataIds, "openDefaultValueF7", rowIndex);
    }

    private void openOptionalRangeF7(String args) {
        JSONObject data = JSONObject.parseObject((String)args);
        String entityNumber = data.getString("entityNumber");
        String rowIndex = data.getString("rowIndex");
        String baseDataIds = data.getString("baseDataIds");
        String fieldAlias = data.getString("fieldAlias");
        String multi = "multiple".equals(data.getString("selectMode")) ? "true" : "false";
        this.showBaseDataF7(entityNumber, fieldAlias, multi, baseDataIds, "openOptionalRangeF7", rowIndex);
    }

    private void showBaseDataF7(String entityNumber, String fieldAlias, String multi, String baseDataIds, String actionKey, String rowIndex) {
        ListShowParameter listShowParameter = ShowFormHelper.createShowListForm((String)entityNumber, (boolean)Boolean.parseBoolean(multi), (int)0, (boolean)true);
        ArrayList<Object> ids = new ArrayList<Object>();
        if (HRStringUtils.isNotEmpty((String)baseDataIds)) {
            MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
            ISimpleProperty primaryKey = dataEntityType.getPrimaryKey();
            boolean pkIsLong = primaryKey instanceof LongProp;
            for (String str : baseDataIds.split(",")) {
                if (!HRStringUtils.isNotEmpty((String)str)) continue;
                if (pkIsLong) {
                    ids.add(HRLongValueParseService.getInstance().parseLong((Object)str));
                    continue;
                }
                ids.add(str);
            }
            listShowParameter.setSelectedRows(ids.toArray());
        }
        listShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, actionKey + "_" + rowIndex + "$" + fieldAlias));
        listShowParameter.getOpenStyle().setShowType(ShowType.Modal);
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    private void setBaseDataFormCallBack(String rowIndex, String ids, String names, String methodKey) {
        HashMap value = Maps.newHashMapWithExpectedSize((int)4);
        value.put("rowIndex", rowIndex);
        value.put("ids", ids);
        value.put("names", names);
        this.invokeCustomPlugin(methodKey, value);
    }

    private void beforeDoOp(String opKey) {
        this.invokeCustomPlugin("beforeDoOp", opKey);
    }

    private void doOp(String args) {
        JSONObject data = JSONObject.parseObject((String)args);
        String opKey = data.getString("opKey");
        String newConditionData = data.getString("commonConditionData");
        List newConditions = Collections.emptyList();
        if (HRStringUtils.isNotEmpty((String)newConditionData)) {
            newConditions = JSONArray.parseArray((String)newConditionData, WarnCommonConditionBo.class);
        }
        String conditionData = (String)this.getModel().getValue("commoncondition");
        List oldConditions = Collections.emptyList();
        if (HRStringUtils.isNotEmpty((String)conditionData)) {
            oldConditions = JSONArray.parseArray((String)conditionData, WarnCommonConditionBo.class);
        }
        if (!oldConditions.equals(newConditions)) {
            this.getModel().setValue("commoncondition", (Object)newConditionData);
        }
        if ("close".equals(opKey)) {
            this.getPageCache().put(CACHE_KEY_TWICE_CLOSE, "true");
            this.getView().close();
        } else if (!"refreshcommoncondition".equals(opKey)) {
            OperateOption operateOption = OperateOption.create();
            operateOption.setVariableValue("OP_KEY_GET_COMMON_CONDITION_LAST_VALUE", "true");
            this.getView().invokeOperation(opKey, operateOption);
            this.formProcessor.resetFormStatusAfterDoOp();
        }
    }

    private void deleteCondition(String args) {
        JSONObject data = JSONObject.parseObject((String)args);
        JSONArray ruleValueUniqueArr = data.getJSONArray("ruleValueUniqueArr");
        String newConditionData = data.getString("dataSource");
        List newConditions = Collections.emptyList();
        if (HRStringUtils.isNotEmpty((String)newConditionData)) {
            newConditions = JSONArray.parseArray((String)newConditionData, WarnCommonConditionBo.class);
        }
        if (!ruleValueUniqueArr.isEmpty()) {
            Map referCommonConditionToSchemeName = null;
            if (this.getModel().getDataEntity().getDataEntityState().getFromDatabase()) {
                referCommonConditionToSchemeName = WarnCommonConditionUtils.getReferCommonConditionToSchemeName((Long)this.getModel().getDataEntity().getLong("id"));
            }
            Iterator iterator = ruleValueUniqueArr.iterator();
            LinkedHashMap cannotDeleteDataRuleUniques = Maps.newLinkedHashMapWithExpectedSize((int)ruleValueUniqueArr.size());
            if (referCommonConditionToSchemeName != null) {
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    List schemeNames = (List)referCommonConditionToSchemeName.get(String.valueOf(next));
                    if (schemeNames == null || schemeNames.isEmpty()) continue;
                    cannotDeleteDataRuleUniques.put(String.valueOf(next), schemeNames);
                    iterator.remove();
                }
            }
            Iterator conditionBoIterator = newConditions.iterator();
            boolean change = false;
            while (conditionBoIterator.hasNext()) {
                WarnCommonConditionBo next = (WarnCommonConditionBo)conditionBoIterator.next();
                if (!ruleValueUniqueArr.contains((Object)next.getRuleValueUnique())) continue;
                conditionBoIterator.remove();
                change = true;
            }
            if (change) {
                this.getModel().setValue("commoncondition", (Object)SerializationUtils.toJsonString((Object)newConditions));
            }
            this.invokeCustomPlugin("renderConditions", newConditions);
            this.showDeleteErrorMsg(cannotDeleteDataRuleUniques);
        }
    }

    private void showDeleteErrorMsg(Map<String, List<String>> cannotDeleteDataRuleUniques) {
        if (!cannotDeleteDataRuleUniques.isEmpty()) {
            HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_warnscene");
            DynamicObject dbDy = helper.queryOriginalOne("commoncondition", this.getModel().getDataEntity().getPkValue());
            List dbConditions = JSONArray.parseArray((String)dbDy.getString("commoncondition"), WarnCommonConditionBo.class);
            ArrayList errorMsg = Lists.newArrayListWithExpectedSize((int)cannotDeleteDataRuleUniques.size());
            for (WarnCommonConditionBo dbCondition : dbConditions) {
                String ruleValueUnique = dbCondition.getRuleValueUnique();
                List<String> schemeNames = cannotDeleteDataRuleUniques.get(ruleValueUnique);
                if (schemeNames == null || schemeNames.isEmpty()) continue;
                schemeNames = schemeNames.stream().map(name -> "\u201c" + name + "\u201d").collect(Collectors.toList());
                if (cannotDeleteDataRuleUniques.size() == 1) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)MessageFormat.format("\u5220\u9664\u5931\u8d25\uff0c\u8be5\u6761\u4ef6\u5df2\u88ab\u9884\u8b66\u573a\u666f{0}\u5f15\u7528\u3002", String.join((CharSequence)"\u3001", schemeNames)), (String)"WarnSceneCommonConditionsEdit_0", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                    break;
                }
                errorMsg.add(ResManager.loadKDString((String)MessageFormat.format("{0}\uff1a\u5220\u9664\u5931\u8d25\uff0c\u8be5\u6761\u4ef6\u5df2\u88ab\u9884\u8b66\u573a\u666f{1}\u5f15\u7528\u3002", dbCondition.getName().getLocaleValue(), String.join((CharSequence)"\u3001", schemeNames)), (String)"WarnSceneCommonConditionsEdit_1", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
            }
            if (!errorMsg.isEmpty()) {
                FormShowParameter parameter = new FormShowParameter();
                parameter.getOpenStyle().setShowType(ShowType.Modal);
                parameter.setShowTitle(false);
                parameter.setFormId(META_NUMBER_BOS_OPERATION_RESULT);
                parameter.setCustomParam("title", (Object)ResManager.loadKDString((String)"\u5220\u9664\u5931\u8d25", (String)"WarnSceneCommonConditionsEdit_2", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                parameter.setCustomParam("errorMsg", (Object)errorMsg);
                this.getView().showForm(parameter);
            }
        }
    }

    private void invokeCustomPlugin(String methodKey, Object value) {
        CustomControl commonCondition = (CustomControl)this.getView().getControl("hrwarnscene");
        HashMap data = Maps.newHashMapWithExpectedSize((int)3);
        data.put("method", methodKey);
        data.put("date", String.valueOf(System.currentTimeMillis()));
        data.put("value", value);
        commonCondition.setData((Object)data);
    }

    private void updateConditionAllData(String args) {
        JSONObject data = JSONObject.parseObject((String)args);
        String newConditionData = data.getString("newDataSource");
        if (HRStringUtils.isEmpty((String)newConditionData)) {
            newConditionData = data.getString("dataSource");
        }
        this.getModel().setValue("commoncondition", (Object)newConditionData);
        this.cacheSelConditionField();
    }

    private void cacheSelConditionField() {
        String newConditionData = (String)this.getModel().getValue("commoncondition");
        WarnComConditionProcessor processor = new WarnComConditionProcessor((HRDataBaseEdit)this);
        processor.cacheSelConditionField(newConditionData);
    }
}
