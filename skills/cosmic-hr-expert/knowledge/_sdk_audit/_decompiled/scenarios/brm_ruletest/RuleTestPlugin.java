/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.BasedataEntityType
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.ValueMapItem
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.entity.property.ComboProp
 *  kd.bos.entity.property.FieldProp
 *  kd.bos.entity.property.MulBasedataProp
 *  kd.bos.exception.KDBizException
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.ext.hr.ruleengine.enums.ParamTypeEnum
 *  kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo
 *  kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil
 *  kd.bos.form.IFormView
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.events.TabSelectEvent
 *  kd.bos.form.control.events.TabSelectListener
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.hr.brm.business.web.RuleServiceHelper
 *  kd.hr.brm.common.constants.RuleConstants
 *  kd.hr.brm.common.constants.RuleControlConstants
 *  kd.hr.brm.common.enums.ReturnResultEnum
 *  kd.hr.brm.common.enums.ReturnTypeEnum
 *  kd.hr.brm.formplugin.util.SceneUtil
 *  kd.hr.brm.formplugin.web.customplugin.IRulePlugin
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.brm.formplugin.web.customplugin;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.BasedataEntityType;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.ValueMapItem;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.BasedataProp;
import kd.bos.entity.property.ComboProp;
import kd.bos.entity.property.FieldProp;
import kd.bos.entity.property.MulBasedataProp;
import kd.bos.exception.KDBizException;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.ext.hr.ruleengine.enums.ParamTypeEnum;
import kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo;
import kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil;
import kd.bos.form.IFormView;
import kd.bos.form.container.Tab;
import kd.bos.form.control.events.TabSelectEvent;
import kd.bos.form.control.events.TabSelectListener;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.brm.business.web.RuleServiceHelper;
import kd.hr.brm.common.constants.RuleConstants;
import kd.hr.brm.common.constants.RuleControlConstants;
import kd.hr.brm.common.enums.ReturnResultEnum;
import kd.hr.brm.common.enums.ReturnTypeEnum;
import kd.hr.brm.formplugin.util.SceneUtil;
import kd.hr.brm.formplugin.web.customplugin.IRulePlugin;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class RuleTestPlugin
extends HRDataBaseEdit
implements IRulePlugin,
TabSelectListener,
BeforeF7SelectListener,
RuleControlConstants,
RuleConstants {
    private static final Log LOGGER = LogFactory.getLog(RuleTestPlugin.class);
    private static final String OPEN_DYNAMIC_OBJECT_F7_EVENT_NAME = "openConditionParamF7Event";
    private static final String OPEN_F7_CALLBACK = "openConditionParamF7CallBack";
    private static final String TEST_RULE_EVENT_NAME = "testRuleEvent";
    private static final String DATE_FORMAT_EVENT_NAME = "setDateFormatEvent";
    private static final String RESULTJSON = "resultJson";
    private static final String RESULT_TAB = "resulttab";
    private static final String RESULT_JSON_PAGE = "resultjsonpageap";
    private static final String RESULT_JSONCONTROL = "resultjsoncontrol";
    private static final String ENTRY_FIELD = "entryentity";

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        Tab resultTab = (Tab)this.getControl(RESULT_TAB);
        resultTab.addTabSelectListener((TabSelectListener)this);
        BasedataEdit sceneEdit = (BasedataEdit)this.getView().getControl("scene");
        sceneEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        SceneUtil.beforeSceneF7Select((IDataModel)this.getModel(), (IFormView)this.getView(), (BeforeF7SelectEvent)evt);
    }

    public void propertyChanged(PropertyChangedArgs args) {
        if ("scene".equals(args.getProperty().getName()) && this.getModel().getValue("scene") != null) {
            Long id = ((DynamicObject)args.getChangeSet()[0].getNewValue()).getLong("id");
            this.updateConditionData("param", this.getInputParamNoContainTarget(id));
        } else if ("ruledate".equals(args.getProperty().getName())) {
            Date newDate = (Date)args.getChangeSet()[0].getNewValue();
            if (null == newDate) {
                return;
            }
            String dateFormat = this.getView().getPageCache().get("ruleDateFormat");
            this.updateConditionData("date", HRDateTimeUtils.format((Date)newDate, (String)dateFormat));
            this.getModel().setValue("ruledate", null);
        } else if ("bizapp".equals(args.getProperty().getName())) {
            this.getModel().setValue("scene", null);
        }
    }

    public void customEvent(CustomEventArgs args) {
        if (OPEN_DYNAMIC_OBJECT_F7_EVENT_NAME.equals(args.getEventName())) {
            this.openParamF7((AbstractFormPlugin)this, (LinkedHashMap)SerializationUtils.fromJsonString((String)args.getEventArgs(), Map.class), OPEN_F7_CALLBACK);
        } else if (TEST_RULE_EVENT_NAME.equals(args.getEventName())) {
            if (this.valid(args)) {
                this.testRuleEvent(args.getEventArgs());
            }
        } else if (DATE_FORMAT_EVENT_NAME.equals(args.getEventName())) {
            this.dateFormat((Map)SerializationUtils.fromJsonString((String)args.getEventArgs(), Map.class));
        }
    }

    private void dateFormat(Map<String, Object> argsJson) {
        String dateFormat = this.getDateFormat((String)argsJson.get("dateFormat"));
        this.getView().getPageCache().put("ruleDateFormat", dateFormat);
        HashMap paramMap = Maps.newHashMapWithExpectedSize((int)4);
        HashMap itemMap = Maps.newHashMapWithExpectedSize((int)4);
        paramMap.put("mask", dateFormat);
        itemMap.put("item", paramMap);
        this.getView().updateControlMetadata("ruledate", (Map)itemMap);
        this.updateConditionData("showDate", Boolean.TRUE);
    }

    private boolean valid(CustomEventArgs args) {
        return null != this.getModel().getValue("bu") && null != this.getModel().getValue("bizapp") && null != this.getModel().getValue("scene");
    }

    protected void testRuleEvent(String conditionJson) {
        RuleValidateInfo ruleValidateInfo = RuleValidateUtil.validRuleTestCondition((String)conditionJson);
        if (!ruleValidateInfo.isSuccess()) {
            this.getView().showErrorNotification(ruleValidateInfo.getMsg());
            return;
        }
        HashMap requestMap = Maps.newHashMapWithExpectedSize((int)16);
        String bizApp = ((DynamicObject)this.getModel().getValue("bizApp")).getString("number");
        String sceneNumber = ((DynamicObject)this.getModel().getValue("scene")).getString("number");
        String buNumber = ((DynamicObject)this.getModel().getValue("bu")).getString("number");
        Map<String, Object> paramsMapFromRequest = this.getParamsMapFromRequest(conditionJson);
        requestMap.put("bizApp", bizApp);
        requestMap.put("sceneNumber", sceneNumber);
        requestMap.put("buNumber", buNumber);
        requestMap.put("inputParams", paramsMapFromRequest);
        LOGGER.info("requestMap:{}", (Object)requestMap);
        Map response = (Map)HRMServiceHelper.invokeHRMPService((String)"bree", (String)"IBREERuleService", (String)"callRuleEngine", (Object[])new Object[]{requestMap});
        LOGGER.info("responseMap:{}", (Object)response);
        Tab tab = (Tab)this.getView().getControl(RESULT_TAB);
        if (HRStringUtils.equals((String)((String)response.get("responseDesc")), (String)"success") && null != response.get("policyResults")) {
            this.setFieldEntry((List)response.get("policyResults"));
        } else {
            this.getModel().deleteEntryData(ENTRY_FIELD);
            this.getView().updateView(ENTRY_FIELD);
            tab.activeTab(RESULT_JSON_PAGE);
        }
        if (RESULT_JSON_PAGE.equals(tab.getCurrentTab())) {
            this.updateResultJson(SerializationUtils.toJsonString((Object)response));
        }
        this.getPageCache().put(RESULTJSON, SerializationUtils.toJsonString((Object)response));
    }

    private Map<String, Object> getParamsMapFromRequest(String conditionJson) {
        List params = SerializationUtils.fromJsonStringToList((String)conditionJson, Map.class);
        HashMap paramsMapFromRequest = Maps.newHashMapWithExpectedSize((int)params.size());
        params.stream().forEach(map -> this.formatRequest(paramsMapFromRequest, (Map)map));
        return paramsMapFromRequest;
    }

    private void formatRequest(Map<String, Object> paramsMapFromRequest, Map map) {
        String type;
        Object value = map.get("value");
        if (value == null || StringUtils.isEmpty((CharSequence)value.toString())) {
            return;
        }
        switch (type = map.get("type").toString()) {
            case "string": {
                value = value.toString();
                break;
            }
            case "number": {
                value = new BigDecimal(value.toString());
                break;
            }
            case "boolean": {
                value = Boolean.parseBoolean(value.toString());
                break;
            }
            case "date": {
                try {
                    value = HRDateTimeUtils.parseDate((String)value.toString(), (String)this.getDateFormat((String)map.get("dateFormat")));
                }
                catch (ParseException e) {
                    LOGGER.error((Throwable)e);
                }
                break;
            }
            case "dynamicObject": {
                value = value.toString();
                break;
            }
        }
        String param = map.get("param").toString();
        if (param.contains(".")) {
            String typeDetail = (String)map.get("typeDetail");
            String[] params = param.split("\\.");
            String key = params[0];
            if (params.length > 4) {
                String[] lvParam = param.split("\\.", 3);
                Object obj = this.getValue(typeDetail, lvParam[2], paramsMapFromRequest, value, key);
                Object obj2 = this.getValue(ParamTypeEnum.DYNAMICOBJECT.getValue(), params[0] + "." + params[1] + "." + params[2], paramsMapFromRequest, obj, "");
                paramsMapFromRequest.put(key, obj2);
            } else {
                Object obj = this.getValue(typeDetail, param, paramsMapFromRequest, value, "");
                paramsMapFromRequest.put(key, obj);
            }
        } else {
            paramsMapFromRequest.put(param, value);
        }
    }

    private Object getParamValue(Map<String, Object> paramsMapFromRequest, String key, String parentField, Object defaultValue) {
        if (StringUtils.isNotEmpty((CharSequence)parentField)) {
            DynamicObject obj = (DynamicObject)paramsMapFromRequest.get(parentField);
            return null == obj || null == obj.get(key) ? defaultValue : obj.get(key);
        }
        return paramsMapFromRequest.getOrDefault(key, defaultValue);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected Object getValue(String typeDetail, String param, Map<String, Object> paramsMapFromRequest, Object value, String parentField) {
        String[] dynamicObjects = param.split("\\.");
        String key = dynamicObjects[0];
        String entityNumber = dynamicObjects[1];
        String field = dynamicObjects[2];
        HRBaseServiceHelper helper = new HRBaseServiceHelper(entityNumber);
        if (ParamTypeEnum.MUL_ENUM.getValue().equals(typeDetail) || !typeDetail.startsWith("mul_")) {
            DynamicObject obj = (DynamicObject)this.getParamValue(paramsMapFromRequest, key, parentField, helper.generateEmptyDynamicObject());
            if (this.isDynamicObject(dynamicObjects)) {
                DynamicProperty prop = (DynamicProperty)obj.getDataEntityType().getProperties().get((Object)field);
                if (!(prop instanceof BasedataProp)) throw new KDBizException("prop type error:" + prop.getClass().getName());
                String subEntityNumber = ((BasedataProp)prop).getBaseEntityId();
                DynamicObject obj1 = new HRBaseServiceHelper(subEntityNumber).generateEmptyDynamicObject();
                obj1.set("id", value);
                obj.set(field, (Object)obj1);
                return obj;
            } else {
                obj.set(field, value);
            }
            return obj;
        }
        if (this.isDynamicObject(dynamicObjects)) {
            DynamicObject obj = (DynamicObject)this.getParamValue(paramsMapFromRequest, key, parentField, helper.generateEmptyDynamicObject());
            DynamicProperty prop = (DynamicProperty)obj.getDataEntityType().getProperties().get((Object)field);
            if (!(prop instanceof MulBasedataProp)) throw new KDBizException("prop type error:" + prop.getClass().getName());
            DynamicObject mulBaseData = obj.getDynamicObjectCollection(prop.getName()).addNew();
            HRBaseServiceHelper helper1 = new HRBaseServiceHelper(((MulBasedataProp)prop).getBaseEntityId());
            MulBasedataDynamicObjectCollection colls = new MulBasedataDynamicObjectCollection();
            for (String id : value.toString().split(",")) {
                DynamicObject obj1 = helper1.generateEmptyDynamicObject();
                obj1.set("id", (Object)id);
                mulBaseData.set("fbasedataid", (Object)obj1);
                colls.add(mulBaseData);
            }
            obj.set(field, (Object)colls);
            return obj;
        }
        if (!"id".equals(field)) return null;
        DynamicObjectCollection colls = (DynamicObjectCollection)this.getParamValue(paramsMapFromRequest, key, parentField, new DynamicObjectCollection());
        for (String id : value.toString().split(",")) {
            DynamicObject obj1 = helper.generateEmptyDynamicObject();
            obj1.set("id", (Object)id);
            colls.add((Object)obj1);
        }
        return colls;
    }

    private boolean isDynamicObject(String[] dynamicObjects) {
        return dynamicObjects.length == 4;
    }

    private void setFieldEntry(List<Map<String, Object>> policys) {
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.deleteEntryData(ENTRY_FIELD);
        model.beginInit();
        TableValueSetter vs = new TableValueSetter(new String[0]);
        vs.addField("policyname", new Object[0]);
        vs.addField("policymode", new Object[0]);
        vs.addField("returnresult", new Object[0]);
        vs.addField("rulenumber", new Object[0]);
        vs.addField("rulename", new Object[0]);
        vs.addField("resulttype", new Object[0]);
        vs.addField("resultvalue", new Object[0]);
        HashMap ruleNameMap = Maps.newHashMapWithExpectedSize((int)16);
        for (Map<String, Object> policy : policys) {
            String policyNumber = (String)policy.get("policyNumber");
            String policyMode = (String)policy.get("policyMode");
            String resultType = "value";
            List rulesResults = (List)policy.get("ruleResults");
            List rosterResults = (List)policy.get("rosterResults");
            List defaultResults = (List)policy.get("defaultResults");
            if (null != rulesResults && !rulesResults.isEmpty()) {
                for (Map rule : rulesResults) {
                    String ruleNumber = (String)rule.get("ruleNumber");
                    Long ruleDesignId = Long.valueOf((String)rule.get("ruleDesignId"));
                    String ruleName = (String)ruleNameMap.get(ruleDesignId);
                    if (ruleName == null) {
                        ruleName = RuleServiceHelper.queryDesignRuleName((Long)ruleDesignId);
                        ruleNameMap.put(ruleDesignId, ruleName);
                    }
                    vs.addRow(new Object[]{policyNumber, policyMode, ReturnResultEnum.ruleResult, ruleNumber, ruleName, resultType, this.formatValue((List)rule.get("matchResults"))});
                }
                continue;
            }
            if (null != defaultResults && !defaultResults.isEmpty()) {
                vs.addRow(new Object[]{policyNumber, policyMode, ReturnResultEnum.defaultResult, "", "", resultType, this.formatValue(defaultResults)});
                continue;
            }
            if (null == rosterResults || rosterResults.isEmpty()) continue;
            vs.addRow(new Object[]{policyNumber, policyMode, ReturnResultEnum.rosterResult, "", "", resultType, this.formatValue(rosterResults)});
        }
        model.batchCreateNewEntryRow(ENTRY_FIELD, vs);
        model.endInit();
        this.getView().updateView(ENTRY_FIELD);
    }

    private String formatValue(List<Map<String, Object>> results) {
        ArrayList sb = Lists.newArrayListWithCapacity((int)16);
        for (Map<String, Object> result : results) {
            String fieldNumber = (String)result.get("field");
            String fieldType = (String)result.get("fieldType");
            String entityType = (String)result.get("entityType");
            String name = (String)result.get("field");
            String value = String.valueOf(result.get("value"));
            if (StringUtils.isNotEmpty((CharSequence)entityType)) {
                String field;
                MainEntityType mainEntityType = MetadataServiceHelper.getDataEntityType((String)entityType);
                String string = name = null == mainEntityType.getProperty(field = fieldNumber.split("\\.")[1]).getDisplayName() ? mainEntityType.getDisplayName().getLocaleValue() : mainEntityType.getDisplayName().getLocaleValue() + "." + mainEntityType.getProperty(field).getDisplayName().getLocaleValue();
                if (fieldType.equals(ReturnTypeEnum.STRING.getValue())) {
                    value = this.getStringValue(mainEntityType, field, value);
                } else if (fieldType.equals(ReturnTypeEnum.BOOLEAN.getValue())) {
                    value = value.equals("true") ? ResManager.loadKDString((String)"\u662f", (String)"RuleTestPlugin_0", (String)"hrmp-brm-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u5426", (String)"RuleTestPlugin_1", (String)"hrmp-brm-formplugin", (Object[])new Object[0]);
                } else if (fieldType.equals(ReturnTypeEnum.DYNAMICOBJECT.getValue())) {
                    value = this.getDynamicObjectValue(mainEntityType, field, value);
                }
            }
            sb.add(name + "=" + value);
        }
        return ((Object)sb).toString();
    }

    private String getStringValue(MainEntityType mainEntityType, String field, String value) {
        String controlType = ((FieldProp)mainEntityType.getProperty(field)).getFilterControlType();
        if ("enum".equals(controlType)) {
            List mapItemList = ((ComboProp)mainEntityType.getProperty(field)).getComboItems();
            for (ValueMapItem item : mapItemList) {
                if (!value.equals(item.getValue())) continue;
                return item.getName().getLocaleValue();
            }
        }
        return value;
    }

    private String getDynamicObjectValue(MainEntityType mainEntityType, String field, String value) {
        String entityNumber;
        if ("null".equals(value)) {
            return null;
        }
        if ("id".equals(field)) {
            entityNumber = mainEntityType.getName();
        } else {
            DynamicProperty prop = mainEntityType.getProperty(field);
            if (prop instanceof BasedataProp) {
                entityNumber = ((BasedataProp)prop).getBaseEntityId();
            } else if (prop instanceof MulBasedataProp) {
                entityNumber = ((MulBasedataProp)prop).getBaseEntityId();
            } else {
                throw new KDBizException("prop type error:" + prop.getClass().getName());
            }
        }
        HRBaseServiceHelper helper = new HRBaseServiceHelper(entityNumber);
        BasedataEntityType basedataEntityType = (BasedataEntityType)MetadataServiceHelper.getDataEntityType((String)entityNumber);
        String nameKey = basedataEntityType.getNameProperty();
        if (StringUtils.isEmpty((CharSequence)nameKey)) {
            return value;
        }
        if (value.contains(",")) {
            QFilter qFilter;
            DynamicObject[] objects;
            String[] ids = value.split(",");
            ArrayList idList = Lists.newArrayListWithExpectedSize((int)ids.length);
            if (Pattern.matches("\\d+", ids[0])) {
                for (String idStr : ids) {
                    idList.add(Long.parseLong(idStr));
                }
            } else {
                for (String idStr : ids) {
                    idList.add(idStr);
                }
            }
            if ((objects = helper.query(nameKey, new QFilter[]{qFilter = new QFilter("id", "in", (Object)idList)})) != null && objects.length > 0) {
                return Joiner.on((String)",").join((Iterable)Arrays.stream(objects).map(object -> {
                    String name = object.get(nameKey).toString();
                    return StringUtils.isEmpty((CharSequence)name) ? object.getPkValue().toString() : name;
                }).collect(Collectors.toList()));
            }
        } else {
            Object id = Pattern.matches("\\d+", value) ? Long.valueOf(Long.parseLong(value)) : value;
            DynamicObject object2 = helper.queryOne(nameKey, id);
            if (object2 != null) {
                String name = object2.get(nameKey).toString();
                return StringUtils.isEmpty((CharSequence)name) ? object2.getPkValue().toString() : name;
            }
        }
        return value;
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String[] params = closedCallBackEvent.getActionId().split("!");
        if (params[1].equals(OPEN_F7_CALLBACK)) {
            Map resultMap = this.getF7CallBack(params[2], closedCallBackEvent);
            this.updateConditionData("value", resultMap);
        }
    }

    public void tabSelected(TabSelectEvent tabSelectEvent) {
        if (RESULT_JSON_PAGE.equals(tabSelectEvent.getTabKey())) {
            this.updateResultJson(this.getPageCache().get(RESULTJSON));
        }
    }

    private void updateResultJson(Object resultJson) {
        CustomControl customcontrol = (CustomControl)this.getView().getControl(RESULT_JSONCONTROL);
        HashMap data = Maps.newHashMapWithExpectedSize((int)16);
        data.put("json", resultJson);
        customcontrol.setData((Object)data);
    }

    private void updateConditionData(String key, Object obj) {
        this.updateControlData(this.getView(), "conditioncontrol", key, obj);
    }

    private String getDateFormat(String dateFormat) {
        return StringUtils.isEmpty((CharSequence)dateFormat) ? "yyyy-MM-dd" : dateFormat;
    }
}
