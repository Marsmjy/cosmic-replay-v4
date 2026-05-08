/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DataEntityState
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.ISimpleProperty
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.BasedataEntityType
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.LongProp
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.ext.hr.ruleengine.controls.RuleCondition
 *  kd.bos.ext.hr.ruleengine.utils.RuleUtil
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil
 *  kd.hr.hbp.business.service.labelandreport.AnobjWarnFilterUtil
 *  kd.hr.hbp.business.service.labelandreport.FieldDefineService
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.model.complexobj.labelandreport.FieldTreeNode
 *  kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.common.constants.earlywarn.WarnCalFieldConstants
 *  kd.hr.hrcs.common.constants.earlywarn.WarnSchemeBaseConditionConstants
 *  kd.hr.hrcs.common.constants.earlywarn.WarnSchemeFieldConstants
 *  kd.hr.hrcs.common.model.earlywarn.WarnBaseConditionBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnCalFieldBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnCommonConditionBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnFilterLogicExprBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo
 *  kd.hr.hrcs.common.model.earlywarn.vo.WarnBaseConditionReturnVo
 *  kd.hr.hrcs.common.util.earlywarn.WarnEntityEnumComboUtils
 *  kd.hr.hrcs.formplugin.web.earlywarn.scene.process.WarnComConditionProcessor
 *  kd.hr.hrcs.formplugin.web.earlywarn.utils.FilterOperatorUtils
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scheme;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DataEntityState;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.ISimpleProperty;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.BasedataEntityType;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.LongProp;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.ext.hr.ruleengine.controls.RuleCondition;
import kd.bos.ext.hr.ruleengine.utils.RuleUtil;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil;
import kd.hr.hbp.business.service.labelandreport.AnobjWarnFilterUtil;
import kd.hr.hbp.business.service.labelandreport.FieldDefineService;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.model.complexobj.labelandreport.FieldTreeNode;
import kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.common.constants.earlywarn.WarnCalFieldConstants;
import kd.hr.hrcs.common.constants.earlywarn.WarnSchemeBaseConditionConstants;
import kd.hr.hrcs.common.constants.earlywarn.WarnSchemeFieldConstants;
import kd.hr.hrcs.common.model.earlywarn.WarnBaseConditionBo;
import kd.hr.hrcs.common.model.earlywarn.WarnCalFieldBo;
import kd.hr.hrcs.common.model.earlywarn.WarnCommonConditionBo;
import kd.hr.hrcs.common.model.earlywarn.WarnFilterLogicExprBo;
import kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo;
import kd.hr.hrcs.common.model.earlywarn.vo.WarnBaseConditionReturnVo;
import kd.hr.hrcs.common.util.earlywarn.WarnEntityEnumComboUtils;
import kd.hr.hrcs.formplugin.web.earlywarn.scene.process.WarnComConditionProcessor;
import kd.hr.hrcs.formplugin.web.earlywarn.utils.FilterOperatorUtils;

@ExcludeFromJacocoGeneratedReport
public final class WarnSchemeBCFilterEditPlugin
extends HRDataBaseEdit
implements WarnSchemeFieldConstants,
WarnCalFieldConstants,
WarnSchemeBaseConditionConstants {
    private static final Log LOG = LogFactory.getLog(WarnSchemeBCFilterEditPlugin.class);
    private static final String RULE_DATE_KEY = "ruledate";
    private static final String CACHE_KEY_TWICE_CLOSE = "CACHE_KEY_TWICE_CLOSE";

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        BaseShowParameter showParameter = (BaseShowParameter)this.getView().getFormShowParameter();
        Boolean isCopy = (Boolean)showParameter.getCustomParam("iscopy");
        boolean isFirstEnter = HRStringUtils.isEmpty((String)this.getPageCache().get("CACHE_KEY_OF_DB_SCENE_ID"));
        if (Boolean.TRUE.equals(isCopy) && isFirstEnter) {
            DynamicObject warnSceneDy = (DynamicObject)this.getModel().getValue("warnscene");
            if (warnSceneDy != null) {
                String scenePkStr = String.valueOf(warnSceneDy.getLong("id"));
                this.getPageCache().put("CACHE_KEY_OF_DB_SCENE_ID", scenePkStr);
            }
            String baseConditionJson = (String)this.getModel().getValue("basecondition");
            this.getPageCache().put("CACHE_KEY_OF_DB_BASE_CONDITION", baseConditionJson);
        }
        this.renderConditionControl(true);
        DataEntityState dataEntityState = this.getModel().getDataEntity().getDataEntityState();
        dataEntityState.setBizChanged(this.getModel().getDataEntityType().getProperty("basecondition").getOrdinal(), false);
        RuleCondition ruleCondition = this.getRuleCondition();
        ruleCondition.setContainTarget(Boolean.valueOf(false));
        if ("scene".equals(this.getModel().getValue("warntype"))) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"datafilterflex"});
        } else if ("bizobj".equals(this.getModel().getValue("warntype"))) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"datafilterflex"});
        }
        DynamicObject bziObj = (DynamicObject)this.getModel().getValue("warnbizobj");
        if ("bizobj".equals(this.getModel().getValue("warntype")) && bziObj != null) {
            String entityNum = bziObj.getString("number");
            this.initFilterData(entityNum, true);
        }
        this.setWarnTypeVisible();
        this.cacheSelConditionField();
    }

    private void setWarnTypeVisible() {
        String warnType = (String)this.getModel().getValue("warntype");
        boolean isBizObj = "bizobj".equals(warnType);
        this.getView().setVisible(Boolean.valueOf(isBizObj), new String[]{"datafilterflex"});
        this.getView().setVisible(Boolean.valueOf(!isBizObj), new String[]{"sceneconditionflex"});
    }

    public void propertyChanged(PropertyChangedArgs changedArgs) {
        try {
            super.propertyChanged(changedArgs);
            IDataEntityProperty property = changedArgs.getProperty();
            if ("warnscene".equals(property.getName())) {
                this.renderConditionControl(false);
            } else if ("warntype".equals(property.getName())) {
                if ("scene".equals(changedArgs.getChangeSet()[0].getNewValue())) {
                    this.getView().setVisible(Boolean.FALSE, new String[]{"datafilterflex"});
                } else if ("bizobj".equals(changedArgs.getChangeSet()[0].getNewValue())) {
                    this.getView().setVisible(Boolean.TRUE, new String[]{"datafilterflex"});
                }
                this.setWarnTypeVisible();
            } else if ("warnbizobj".equals(property.getName())) {
                DynamicObject bziObj = (DynamicObject)changedArgs.getChangeSet()[0].getNewValue();
                if ("bizobj".equals(this.getModel().getValue("warntype")) && bziObj != null) {
                    String entityNum = bziObj.getString("number");
                    this.initFilterData(entityNum, false);
                }
            } else if (HRStringUtils.equals((String)RULE_DATE_KEY, (String)property.getName())) {
                Date newData = (Date)changedArgs.getChangeSet()[0].getNewValue();
                if (null == newData) {
                    return;
                }
                String dateFormat = this.getView().getPageCache().get("ruleDateFormat");
                String data = HRDateTimeUtils.format((Date)newData, (String)dateFormat);
                this.getRuleCondition().setDate(data);
                this.getModel().setValue(RULE_DATE_KEY, null);
            } else if (HRStringUtils.equals((String)"basecondition", (String)property.getName())) {
                this.cacheSelConditionField();
            }
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    private RuleCondition getRuleCondition() {
        return (RuleCondition)this.getControl("hrfilterap");
    }

    public void customEvent(CustomEventArgs eventArgs) {
        try {
            super.customEvent(eventArgs);
            String key = eventArgs.getKey();
            String args = eventArgs.getEventArgs();
            String eventName = eventArgs.getEventName();
            if ("baseconditionap".equals(key)) {
                if ("openSelectValueF7".equals(eventName)) {
                    this.openSelectValueF7(args);
                } else if ("updateRuleCondiion".equals(eventName)) {
                    this.updateRuleCondition(args);
                } else if ("selectQueryFieldBos".equals(eventName)) {
                    WarnComConditionProcessor processor = new WarnComConditionProcessor((HRDataBaseEdit)this);
                    processor.openSelectFieldPage(args);
                } else if ("delCondition".equals(eventName)) {
                    this.deleteCondition(args);
                }
            }
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    private void deleteCondition(String args) {
        JSONObject data = JSONObject.parseObject((String)args);
        JSONArray ruleValueUniqueArr = data.getJSONArray("ruleValueUniqueArr");
        String ruleCondition = data.getString("ruleCondition");
        List baseConditionBos = null;
        if (HRStringUtils.isNotEmpty((String)ruleCondition)) {
            baseConditionBos = JSONArray.parseArray((String)ruleCondition, WarnBaseConditionBo.class);
        }
        if (ruleValueUniqueArr.isEmpty() || baseConditionBos == null) {
            return;
        }
        List removeUniKeyList = ruleValueUniqueArr.stream().map(String::valueOf).collect(Collectors.toList());
        LinkedHashMap cannotDeleteRuleName = Maps.newLinkedHashMapWithExpectedSize((int)removeUniKeyList.size());
        for (WarnBaseConditionBo baseConditionBo : baseConditionBos) {
            boolean cannotDel;
            if (!baseConditionBo.isLock() || !(cannotDel = removeUniKeyList.contains(baseConditionBo.getRuleValueUnique()))) continue;
            cannotDeleteRuleName.put(baseConditionBo.getRuleValueUnique(), baseConditionBo.getName().getLocaleValue());
            removeUniKeyList.remove(baseConditionBo.getRuleValueUnique());
        }
        Iterator baseConditionIterator = baseConditionBos.iterator();
        boolean change = false;
        while (baseConditionIterator.hasNext()) {
            WarnBaseConditionBo next = (WarnBaseConditionBo)baseConditionIterator.next();
            if (!removeUniKeyList.contains(next.getRuleValueUnique())) continue;
            baseConditionIterator.remove();
            change = true;
        }
        if (change) {
            String conditionData = (String)this.getModel().getValue("basecondition");
            WarnBaseConditionReturnVo warnBaseConditionReturnVo = new WarnBaseConditionReturnVo();
            if (HRStringUtils.isNotEmpty((String)conditionData)) {
                warnBaseConditionReturnVo = (WarnBaseConditionReturnVo)JSONObject.parseObject((String)conditionData, WarnBaseConditionReturnVo.class);
            }
            warnBaseConditionReturnVo.setBaseConditionBos(baseConditionBos);
            this.getModel().setValue("basecondition", (Object)SerializationUtils.toJsonString((Object)warnBaseConditionReturnVo));
        }
        CustomControl conditionControl = (CustomControl)this.getView().getControl("baseconditionap");
        HashMap rstDataMap = Maps.newHashMapWithExpectedSize((int)3);
        rstDataMap.put("method", "delCondition");
        rstDataMap.put("ruleCondition", baseConditionBos);
        rstDataMap.put("time", System.currentTimeMillis());
        conditionControl.setData((Object)rstDataMap);
        this.showDeleteErrorMsg(cannotDeleteRuleName);
    }

    private void showDeleteErrorMsg(Map<String, String> cannotDeleteRuleName) {
        if (!cannotDeleteRuleName.isEmpty()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)String.format(Locale.ROOT, "\u5220\u9664\u5931\u8d25\uff0c\u4e0d\u80fd\u522a\u9664\u573a\u666f\u4e2d\u8bbe\u7f6e\u7684\u6761\u4ef6\uff08%s\uff09\u3002", String.join((CharSequence)"\u3001", cannotDeleteRuleName.values())), (String)"WarnSchemeBCFilterEditPlugin_1", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        try {
            super.beforeDoOperation(args);
            AbstractOperate op = (AbstractOperate)args.getSource();
            String operateKey = op.getOperateKey();
            if (HRStringUtils.equals((String)operateKey, (String)"save")) {
                if ("bizobj".equals(this.getModel().getValue("warntype"))) {
                    RuleCondition conditionFilter = this.getRuleCondition();
                    this.getModel().setValue("datafilter", (Object)conditionFilter.getValue());
                } else {
                    this.getModel().setValue("datafilter", null);
                }
            }
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        try {
            super.closedCallBack(closedCallBackEvent);
            String actionId = closedCallBackEvent.getActionId();
            if (HRStringUtils.isNotEmpty((String)actionId)) {
                if (actionId.startsWith("openSelectValueF7")) {
                    String[] keys = actionId.split("\\$");
                    if (keys.length < 2) {
                        return;
                    }
                    String rowNumStr = keys[0].replaceFirst("openSelectValueF7_", "");
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
                    this.setBaseDataFormCallBack(rowNumStr, org.apache.commons.lang3.StringUtils.join((Iterable)ids, (String)","), org.apache.commons.lang3.StringUtils.join((Iterable)names, (String)"\uff1b"), "setSelectValueCallBack");
                } else if ("warn_field_select_callback".equals(actionId)) {
                    Map paramMap = (Map)closedCallBackEvent.getReturnData();
                    WarnComConditionProcessor processor = new WarnComConditionProcessor((HRDataBaseEdit)this);
                    processor.setSelQueryFieldCallBack(paramMap, "baseconditionap");
                }
            }
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    void renderConditionControl(boolean isInit) {
        DynamicObject warnSceneDy = (DynamicObject)this.getModel().getValue("warnscene");
        try {
            WarnBaseConditionReturnVo baseConditionReturnVo = new WarnBaseConditionReturnVo();
            if (warnSceneDy != null) {
                String dbScenePk;
                String scenePkStr = String.valueOf(warnSceneDy.getLong("id"));
                boolean isOldScene = HRStringUtils.equals((String)scenePkStr, (String)(dbScenePk = this.getPageCache().get("CACHE_KEY_OF_DB_SCENE_ID")));
                String commonConditionJson = isOldScene ? "" : warnSceneDy.getString("commoncondition");
                if (HRStringUtils.isNotEmpty((String)commonConditionJson)) {
                    List commonConditionBos = JSONArray.parseArray((String)commonConditionJson, WarnCommonConditionBo.class);
                    baseConditionReturnVo.setCommonConditionBos(commonConditionBos);
                }
                String baseConditionJson = "";
                if (isOldScene) {
                    baseConditionJson = this.getPageCache().get("CACHE_KEY_OF_DB_BASE_CONDITION");
                } else if (isInit) {
                    baseConditionJson = (String)this.getModel().getValue("basecondition");
                }
                if (HRStringUtils.isNotEmpty((String)baseConditionJson)) {
                    List commonConditionBos = baseConditionReturnVo.getCommonConditionBos();
                    baseConditionReturnVo = (WarnBaseConditionReturnVo)SerializationUtils.fromJsonString((String)baseConditionJson, WarnBaseConditionReturnVo.class);
                    baseConditionReturnVo.setCommonConditionBos(commonConditionBos);
                }
                baseConditionReturnVo.setMethod("initControl");
                this.setNewBaseConditionBos(baseConditionReturnVo);
                List queryFieldBos = SerializationUtils.fromJsonStringToList((String)this.getPageCache().get("queryFields"), WarnQueryFieldBo.class);
                WarnEntityEnumComboUtils.setQueryFieldEnumComboItems((List)queryFieldBos);
                baseConditionReturnVo.setQueryFieldBos(queryFieldBos);
                String calFieldStr = this.getPageCache().get("calculateFields");
                List calculateFieldList = HRStringUtils.isEmpty((String)calFieldStr) ? Collections.emptyList() : SerializationUtils.fromJsonStringToList((String)calFieldStr, WarnCalFieldBo.class);
                WarnEntityEnumComboUtils.setWarnCalFieldBooleanItems((List)calculateFieldList);
                baseConditionReturnVo.setCalFieldBos(calculateFieldList);
            } else {
                baseConditionReturnVo.setMethod("reRenderControl");
            }
            this.setSelectValueName(baseConditionReturnVo);
            baseConditionReturnVo.setOperatorMap(FilterOperatorUtils.getAllOperatorMap());
            OperationStatus status = this.getView().getFormShowParameter().getStatus();
            baseConditionReturnVo.setStatus(status.name());
            CustomControl conditionControl = (CustomControl)this.getView().getControl("baseconditionap");
            conditionControl.setData((Object)baseConditionReturnVo);
            this.updateRuleConditionModel(baseConditionReturnVo);
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    private void setNewBaseConditionBos(WarnBaseConditionReturnVo baseConditionReturnVo) {
        List commonConditionBos = baseConditionReturnVo.getCommonConditionBos();
        List originalBCBos = baseConditionReturnVo.getBaseConditionBos();
        Map baseConditionBoMap = originalBCBos.stream().collect(Collectors.toMap(WarnBaseConditionBo::getRuleValueUnique, Function.identity(), (oldValue, newValue) -> oldValue));
        Map scenConditionBoMap = commonConditionBos.stream().collect(Collectors.toMap(WarnCommonConditionBo::getRuleValueUnique, Function.identity(), (oldValue, newValue) -> oldValue));
        List<String> allRuleValueUnique = this.getAllRuleValueUnique(commonConditionBos, originalBCBos);
        ArrayList baseConditionBos = Lists.newArrayListWithExpectedSize((int)commonConditionBos.size());
        for (int index = 0; index < allRuleValueUnique.size(); ++index) {
            String ruleValueUnique = allRuleValueUnique.get(index);
            WarnCommonConditionBo commonConditionBo = (WarnCommonConditionBo)scenConditionBoMap.get(ruleValueUnique);
            if (commonConditionBo != null) {
                WarnBaseConditionBo baseConditionBo = new WarnBaseConditionBo();
                WarnBaseConditionBo originalBCBo = (WarnBaseConditionBo)baseConditionBoMap.get(ruleValueUnique);
                if (originalBCBo != null) {
                    baseConditionBo.setSelectValue(originalBCBo.getSelectValue());
                    baseConditionBo.setSelectValueName(originalBCBo.getSelectValueName());
                    baseConditionBo.setOperator(originalBCBo.getOperator());
                } else {
                    baseConditionBo.setSelectValue(commonConditionBo.getDefaultValue());
                    baseConditionBo.setSelectValueName(commonConditionBo.getDefaultValueName());
                    baseConditionBo.setOperator(commonConditionBo.getOperator());
                }
                baseConditionBo.setName(commonConditionBo.getName());
                baseConditionBo.setDescription(commonConditionBo.getDescription());
                baseConditionBo.setOptionalRange(commonConditionBo.getOptionalRange());
                baseConditionBo.setUnit(commonConditionBo.getUnit());
                baseConditionBo.setMustInput(commonConditionBo.isMustInput());
                baseConditionBo.setSource(commonConditionBo.getSource());
                baseConditionBo.setRuleValue(commonConditionBo.getRuleValue());
                baseConditionBo.setRuleValueUnique(commonConditionBo.getRuleValueUnique());
                baseConditionBo.setDataType(commonConditionBo.getDataType());
                baseConditionBo.setValueType(commonConditionBo.getValueType());
                baseConditionBo.setComplexType(commonConditionBo.getComplexType());
                baseConditionBo.setControlType(commonConditionBo.getControlType());
                baseConditionBo.setBaseDataNumber(commonConditionBo.getBaseDataNumber());
                if (HRStringUtils.isEmpty((String)baseConditionBo.getBaseDataNumber())) {
                    baseConditionBo.setBaseDataNumber(commonConditionBo.getBaseDataNum());
                }
                baseConditionBo.setConditionSource("commonCondition");
                baseConditionBo.setSelectList(commonConditionBo.getSelectList());
                baseConditionBo.setLock(false);
                baseConditionBos.add(baseConditionBo);
                continue;
            }
            WarnBaseConditionBo originalBCBo = (WarnBaseConditionBo)baseConditionBoMap.get(ruleValueUnique);
            originalBCBo.setLock(false);
            baseConditionBos.add(originalBCBo);
        }
        baseConditionReturnVo.setBaseConditionBos((List)baseConditionBos);
        this.resetBaseConditionLogicExpress(baseConditionReturnVo);
        baseConditionReturnVo.setMethod("reRenderControl");
    }

    private void resetBaseConditionLogicExpress(WarnBaseConditionReturnVo baseConditionReturnVo) {
        List baseConditionBos = baseConditionReturnVo.getBaseConditionBos();
        ArrayList logicList = Lists.newArrayListWithExpectedSize((int)baseConditionBos.size());
        StringBuilder logicExpr = new StringBuilder();
        for (int index = 0; index < baseConditionBos.size(); ++index) {
            String conditionAlias = index < 9 ? "T0" + (index + 1) : "T" + (index + 1);
            WarnFilterLogicExprBo logic = new WarnFilterLogicExprBo();
            logic.setName(conditionAlias);
            logicExpr.append(conditionAlias);
            if (index < baseConditionBos.size() - 1) {
                logicExpr.append(' ');
                logicExpr.append("AND");
                logicExpr.append(' ');
                logic.setLogical("AND");
            }
            logicList.add(logic);
        }
        baseConditionReturnVo.setLogicType("0");
        baseConditionReturnVo.setLogicExpressStr(logicExpr.toString());
        baseConditionReturnVo.setLogicExpressList((List)logicList);
    }

    private List<String> getAllRuleValueUnique(List<WarnCommonConditionBo> commonConditionBos, List<WarnBaseConditionBo> originalBCBos) {
        ArrayList<String> allRuleValueUnique = new ArrayList<String>(commonConditionBos.size());
        for (WarnCommonConditionBo commonConditionBo : commonConditionBos) {
            allRuleValueUnique.add(commonConditionBo.getRuleValueUnique());
        }
        for (WarnBaseConditionBo originalBCBo : originalBCBos) {
            String ruleValueUnique = originalBCBo.getRuleValueUnique();
            if (allRuleValueUnique.contains(ruleValueUnique)) continue;
            allRuleValueUnique.add(ruleValueUnique);
        }
        return allRuleValueUnique;
    }

    private void setSelectValueName(WarnBaseConditionReturnVo baseConditionReturnVo) {
        List baseConditionBos = baseConditionReturnVo.getBaseConditionBos();
        for (WarnBaseConditionBo baseConditionBo : baseConditionBos) {
            if (!HRStringUtils.isNotEmpty((String)baseConditionBo.getBaseDataNumber())) continue;
            String selectValue = baseConditionBo.getSelectValue();
            HRBaseServiceHelper helper = new HRBaseServiceHelper(baseConditionBo.getBaseDataNumber());
            MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)baseConditionBo.getBaseDataNumber());
            ISimpleProperty primaryKey = dataEntityType.getPrimaryKey();
            if (!HRStringUtils.isNotEmpty((String)selectValue)) continue;
            Object[] pkIds = primaryKey instanceof LongProp ? Arrays.stream(selectValue.split(",")).map(Long::parseLong).toArray() : selectValue.split(",");
            if (!(dataEntityType instanceof BasedataEntityType)) continue;
            String nameProperty = ((BasedataEntityType)dataEntityType).getNameProperty();
            if (HRStringUtils.isEmpty((String)nameProperty)) {
                DynamicProperty property = dataEntityType.getProperty("name");
                nameProperty = property != null ? "name" : null;
            }
            if (nameProperty == null) continue;
            String queryProperty = nameProperty;
            DynamicObject[] dbDys = helper.queryOriginalArray(nameProperty, new QFilter[]{new QFilter(primaryKey.getName(), "in", (Object)pkIds)});
            List selectValueNames = Arrays.stream(dbDys).map(dy -> dy.getString(queryProperty)).collect(Collectors.toList());
            if (selectValueNames.isEmpty()) continue;
            baseConditionBo.setSelectValueName(org.apache.commons.lang3.StringUtils.join(selectValueNames, (String)"\uff1b"));
        }
    }

    private void openSelectValueF7(String args) {
        JSONObject data = JSONObject.parseObject((String)args);
        String entityNumber = data.getString("entityNumber");
        String rowIndex = data.getString("rowIndex");
        String baseDataIds = data.getString("baseDataIds");
        String multi = data.getString("multi");
        String optionalRange = data.getString("optionalRange");
        String fieldAlias = data.getString("fieldAlias");
        this.showBaseDataF7(entityNumber, fieldAlias, multi, baseDataIds, "openSelectValueF7", rowIndex, optionalRange);
    }

    private void showBaseDataF7(String entityNumber, String fieldAlias, String multi, String baseDataIds, String actionKey, String rowIndex, String optionalRange) {
        ListShowParameter listShowParameter = ShowFormHelper.createShowListForm((String)entityNumber, (boolean)Boolean.parseBoolean(multi), (int)0, (boolean)true);
        if (HRStringUtils.isNotEmpty((String)baseDataIds)) {
            String[] selectBaseDataIds = baseDataIds.split(",");
            ArrayList<Object> selectIds = new ArrayList<Object>();
            for (String str : selectBaseDataIds) {
                if (!HRStringUtils.isNotEmpty((String)str)) continue;
                boolean isLong = false;
                try {
                    Long.parseLong(str);
                    isLong = true;
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (isLong) {
                    selectIds.add(Long.parseLong(str));
                    continue;
                }
                selectIds.add(str);
            }
            listShowParameter.setSelectedRows(selectIds.toArray());
        }
        if (HRStringUtils.isNotEmpty((String)optionalRange)) {
            MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
            String[] optionalRangeIdArray = optionalRange.split(",");
            ArrayList<Object> optionalRangeIds = new ArrayList<Object>();
            for (String str : optionalRangeIdArray) {
                if (!HRStringUtils.isNotEmpty((String)str)) continue;
                boolean isLong = false;
                try {
                    Long.parseLong(str);
                    isLong = true;
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (isLong) {
                    optionalRangeIds.add(Long.parseLong(str));
                    continue;
                }
                optionalRangeIds.add(str);
            }
            listShowParameter.getListFilterParameter().setFilter(new QFilter(dataEntityType.getPrimaryKey().getName(), "in", optionalRangeIds));
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
        CustomControl commonCondition = (CustomControl)this.getView().getControl("baseconditionap");
        HashMap data = Maps.newHashMapWithExpectedSize((int)3);
        data.put("method", methodKey);
        data.put("value", value);
        data.put("date", String.valueOf(System.currentTimeMillis()));
        commonCondition.setData((Object)data);
    }

    private void updateRuleCondition(String args) {
        String dbScenePk;
        DynamicObject sceneDyo;
        String scenePkStr;
        JSONObject data = JSONObject.parseObject((String)args);
        String newConditionData = data.getString("baseConditionData");
        WarnBaseConditionReturnVo newCondition = new WarnBaseConditionReturnVo();
        if (HRStringUtils.isNotEmpty((String)newConditionData)) {
            newCondition = (WarnBaseConditionReturnVo)JSONObject.parseObject((String)newConditionData, WarnBaseConditionReturnVo.class);
            this.resetBaseConditionLogicExpress(newCondition);
            for (WarnBaseConditionBo baseConditionBo : newCondition.getBaseConditionBos()) {
                if (!HRStringUtils.isEmpty((String)baseConditionBo.getBaseDataNumber())) continue;
                baseConditionBo.setBaseDataNumber(baseConditionBo.getBaseDataNum());
            }
        }
        String conditionData = (String)this.getModel().getValue("basecondition");
        WarnBaseConditionReturnVo oldCondition = new WarnBaseConditionReturnVo();
        if (HRStringUtils.isNotEmpty((String)conditionData)) {
            oldCondition = (WarnBaseConditionReturnVo)JSONObject.parseObject((String)conditionData, WarnBaseConditionReturnVo.class);
        }
        if (!oldCondition.equals((Object)newCondition)) {
            this.getModel().setValue("basecondition", (Object)SerializationUtils.toJsonString((Object)newCondition));
        }
        if (HRStringUtils.equals((String)(scenePkStr = String.valueOf((sceneDyo = (DynamicObject)this.getModel().getValue("warnscene")).getLong("id"))), (String)(dbScenePk = this.getPageCache().get("CACHE_KEY_OF_DB_SCENE_ID")))) {
            String baseConditionJson = (String)this.getModel().getValue("basecondition");
            this.getPageCache().put("CACHE_KEY_OF_DB_BASE_CONDITION", baseConditionJson);
        }
    }

    private void updateRuleConditionModel(WarnBaseConditionReturnVo warnBaseConditionReturnVo) {
        WarnBaseConditionReturnVo modelVo = new WarnBaseConditionReturnVo();
        modelVo.setBaseConditionBos(warnBaseConditionReturnVo.getBaseConditionBos());
        modelVo.setLogicType(warnBaseConditionReturnVo.getLogicType());
        modelVo.setLogicExpressStr(warnBaseConditionReturnVo.getLogicExpressStr());
        modelVo.setLogicExpressList(warnBaseConditionReturnVo.getLogicExpressList());
        this.getModel().setValue("basecondition", (Object)JSONObject.toJSONString((Object)modelVo));
    }

    private void initFilterData(String entityNum, boolean isInit) {
        ArrayList warnQueryFieldBos = Lists.newArrayListWithExpectedSize((int)16);
        FieldDefineService fieldDefineService = new FieldDefineService();
        FieldTreeNode entityNode = fieldDefineService.getEntityFiledBo(entityNum, entityNum, true, (List)Lists.newArrayListWithCapacity((int)10));
        ArrayList fieldTreeNodes = Lists.newArrayListWithCapacity((int)10);
        entityNode.getChildren().forEach(field -> this.addFieldTreeNode((FieldTreeNode)field, fieldTreeNodes));
        for (FieldTreeNode node : fieldTreeNodes) {
            QueryFieldCommonBo queryFieldCommonBo = new QueryFieldCommonBo();
            queryFieldCommonBo.setEntityName(node.getEntityName());
            queryFieldCommonBo.setEntityNumber(node.getEntityNumber());
            queryFieldCommonBo.setFieldName(node.getName());
            queryFieldCommonBo.setFieldAlias(node.getFieldAlias());
            queryFieldCommonBo.setFieldPath(node.getFieldPath());
            queryFieldCommonBo.setValueType(node.getValueType());
            queryFieldCommonBo.setComplexType(node.getComplexType());
            queryFieldCommonBo.setControlType(node.getControlType());
            queryFieldCommonBo.setVirtualEntityField(false);
            queryFieldCommonBo.setBaseDataNum(node.getBaseDataNum());
            warnQueryFieldBos.add(queryFieldCommonBo);
        }
        AnalyseObjectUtil.setFieldControlType((List)warnQueryFieldBos);
        List inputMap = AnobjWarnFilterUtil.getParamList((List)warnQueryFieldBos);
        if (inputMap.isEmpty()) {
            return;
        }
        Map valueParamMap = AnobjWarnFilterUtil.getValueParamList((List)warnQueryFieldBos);
        String value = (String)this.getModel().getValue("datafilter");
        RuleCondition ruleCondition = this.getRuleCondition();
        HashMap data = Maps.newHashMapWithExpectedSize((int)4);
        data.put("param", inputMap);
        data.put("valueParam", valueParamMap);
        if (StringUtils.isNotEmpty((CharSequence)value) && isInit) {
            value = RuleUtil.getNewestConditionValue((String)value);
            data.put("value", value);
            ruleCondition.setValue(value);
            data.put("onlyUpdateWeb", "true");
        }
        if (this.getView().getFormShowParameter().getStatus() == OperationStatus.EDIT && ((Boolean)this.getModel().getValue("issyspreset")).booleanValue()) {
            data.put("pageState", OperationStatus.VIEW.toString());
        } else {
            data.put("pageState", this.getView().getFormShowParameter().getStatus().toString());
        }
        ruleCondition.setData((Map)data);
    }

    private void addFieldTreeNode(FieldTreeNode node, List<FieldTreeNode> allFieldTreeNodes) {
        if (node.getField().booleanValue()) {
            allFieldTreeNodes.add(node);
        }
        if (node.getChildren() != null) {
            for (FieldTreeNode child : node.getChildren()) {
                this.addFieldTreeNode(child, allFieldTreeNodes);
            }
        }
    }

    private void cacheSelConditionField() {
        String newConditionData = (String)this.getModel().getValue("basecondition");
        WarnBaseConditionReturnVo baseConditionReturnVo = (WarnBaseConditionReturnVo)SerializationUtils.fromJsonString((String)newConditionData, WarnBaseConditionReturnVo.class);
        HashSet<String> selectField = new HashSet<String>(16);
        List baseConditionList = baseConditionReturnVo.getBaseConditionBos();
        if (baseConditionList != null) {
            for (WarnBaseConditionBo baseCondition : baseConditionList) {
                String ruleValue = baseCondition.getRuleValue();
                if (!HRStringUtils.isNotEmpty((String)ruleValue)) continue;
                selectField.add(ruleValue);
            }
        }
        this.getPageCache().put("CACHE_KEY_SEL_COMMON_FIELD", SerializationUtils.toJsonString(selectField));
    }
}
