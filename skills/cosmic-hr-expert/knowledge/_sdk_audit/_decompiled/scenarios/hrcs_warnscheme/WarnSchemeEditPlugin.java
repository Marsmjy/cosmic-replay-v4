/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Maps
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DataEntityState
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.inte.service.InteServiceImpl
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.model.complexobj.DataTypeEnum
 *  kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo
 *  kd.hr.hbp.common.util.HRBaseUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.CalFieldHelper
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.engine.action.EngineLog
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.engine.serivce.impl.EarlyWarnServiceImpl
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.model.WarnExecuteParamBo
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.queryfield.QueryFieldHelper
 *  kd.hr.hrcs.common.constants.earlywarn.WarnRepeatPeriodEnum
 *  kd.hr.hrcs.common.constants.earlywarn.WarnSchemeFieldConstants
 *  kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo
 *  kd.hr.hrcs.common.model.earlywarn.log.WarnScheduleOperationType
 *  kd.hr.hrcs.common.model.earlywarn.log.WarnScheduleStatus
 *  kd.hr.hrcs.formplugin.web.earlywarn.scheme.processor.WarnSchemeFormProcessor
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scheme;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DataEntityState;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.inte.service.InteServiceImpl;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.util.StringUtils;
import kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.model.complexobj.DataTypeEnum;
import kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo;
import kd.hr.hbp.common.util.HRBaseUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.CalFieldHelper;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.engine.action.EngineLog;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.engine.serivce.impl.EarlyWarnServiceImpl;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.model.WarnExecuteParamBo;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.queryfield.QueryFieldHelper;
import kd.hr.hrcs.common.constants.earlywarn.WarnRepeatPeriodEnum;
import kd.hr.hrcs.common.constants.earlywarn.WarnSchemeFieldConstants;
import kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo;
import kd.hr.hrcs.common.model.earlywarn.log.WarnScheduleOperationType;
import kd.hr.hrcs.common.model.earlywarn.log.WarnScheduleStatus;
import kd.hr.hrcs.formplugin.web.earlywarn.scheme.processor.WarnSchemeFormProcessor;

public final class WarnSchemeEditPlugin
extends HRDataBaseEdit
implements WarnSchemeFieldConstants,
BeforeF7SelectListener {
    private static final Log LOGGER = LogFactory.getLog(WarnSchemeEditPlugin.class);
    private static final String KEY_ADMIN_ORG = "adminorg";
    private final WarnSchemeFormProcessor formProcessor = new WarnSchemeFormProcessor((HRDataBaseEdit)this);

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        ((BasedataEdit)this.getControl(KEY_ADMIN_ORG)).addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void afterCreateNewData(EventObject e) {
        super.afterCreateNewData(e);
        Object createOrg = this.getModel().getValue("createorg");
        if (createOrg == null) {
            this.getModel().setValue("createorg", (Object)RequestContext.get().getOrgId());
        }
    }

    public void beforeBindData(EventObject eventObject) {
        try {
            super.beforeBindData(eventObject);
            this.setDefaultValue();
            this.processControlCustomYearDay(false);
            if (this.getView().getFormShowParameter().getCustomParam("warnscene") != null) {
                this.getModel().setValue("warntype", (Object)"scene");
                this.getModel().setValue("warnscene", this.getView().getFormShowParameter().getCustomParam("warnscene"));
            }
            this.setBizApp();
            this.getModel().setDataChanged(false);
            for (DynamicObject dynObject : this.getModel().getEntryEntity("rcfixentryentity")) {
                dynObject.getDataEntityState().setBizChanged(Boolean.FALSE.booleanValue());
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void afterBindData(EventObject eventObject) {
        try {
            super.afterBindData(eventObject);
            this.setSchemeConfVisible();
            this.processControlCustomYearDay(false);
            for (DynamicObject dynObject : this.getModel().getEntryEntity("rcfixentryentity")) {
                dynObject.getDataEntityState().setBizChanged(Boolean.FALSE.booleanValue());
            }
            this.setWarnTypeVisible();
            this.setReceiverFlexByReceiverType();
            this.initDataCache();
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void afterLoadData(EventObject e) {
        super.afterLoadData(e);
        DynamicObject sceneDyo = (DynamicObject)this.getModel().getValue("warnscene");
        long scenePk = sceneDyo.getLong("id");
        this.getPageCache().put("CACHE_KEY_OF_DB_SCENE_ID", String.valueOf(scenePk));
        String baseConditionJson = (String)this.getModel().getValue("basecondition");
        this.getPageCache().put("CACHE_KEY_OF_DB_BASE_CONDITION", baseConditionJson);
    }

    private void initDataCache() {
        DynamicObject sceneDy = (DynamicObject)this.getModel().getValue("warnscene");
        if (sceneDy == null) {
            return;
        }
        List fieldTreeNodeList = CalFieldHelper.getAllEntityFieldsByTree((DynamicObject)sceneDy);
        HashMap idNameMap = new HashMap(16);
        QueryFieldHelper.initTreeNodeKeyNameMap(idNameMap, (List)fieldTreeNodeList, null);
        List queryFields = QueryFieldHelper.getAllWarnQueryFields((List)fieldTreeNodeList);
        List WarnQueryFieldBoList = QueryFieldHelper.setFieldControlType((List)queryFields);
        this.getPageCache().put("queryFields", SerializationUtils.toJsonString((Object)WarnQueryFieldBoList));
        List calculateFieldList = CalFieldHelper.getAllCalculateFields((DynamicObject)sceneDy);
        this.getPageCache().put("calculateFields", SerializationUtils.toJsonString((Object)calculateFieldList));
        this.getPageCache().put("allFieldTreeNodes", SerializationUtils.toJsonString((Object)fieldTreeNodeList));
        this.getPageCache().put("allFieldTreeIdNameMap", SerializationUtils.toJsonString(idNameMap));
        this.setFieldControlType();
    }

    public void setFieldControlType() {
        String queryFieldsStr = this.getView().getPageCache().get("queryFields");
        if (HRStringUtils.isEmpty((String)queryFieldsStr)) {
            return;
        }
        List queryFieldCommonBoList = JSON.parseArray((String)queryFieldsStr, QueryFieldCommonBo.class);
        List textQueryFieldCommonList = queryFieldCommonBoList.stream().filter(queryField -> DataTypeEnum.STRING.getDataTypeKey().equals(queryField.getValueType())).collect(Collectors.toList());
        AnalyseObjectUtil.setFieldControlType(textQueryFieldCommonList);
        List warnQueryFieldList = JSON.parseArray((String)queryFieldsStr, WarnQueryFieldBo.class);
        for (WarnQueryFieldBo warnQueryField : warnQueryFieldList) {
            if (HRStringUtils.isNotEmpty((String)warnQueryField.getControlType())) continue;
            textQueryFieldCommonList.stream().filter(field -> HRStringUtils.equals((String)field.getFieldAlias(), (String)warnQueryField.getFieldAlias())).findAny().ifPresent(field -> warnQueryField.setControlType(field.getControlType()));
        }
        this.getView().getPageCache().put("queryFields", SerializationUtils.toJsonString((Object)warnQueryFieldList));
    }

    private void setWarnTypeVisible() {
        String warnType = (String)this.getModel().getValue("warntype");
        boolean isBizObj = "bizobj".equals(warnType);
        this.getView().setVisible(Boolean.valueOf(isBizObj), new String[]{"warnbizobj"});
        this.getView().setVisible(Boolean.valueOf(!isBizObj), new String[]{"warnscene"});
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        try {
            String itemKey = evt.getItemKey();
            if (HRStringUtils.equals((String)"bar_close", (String)itemKey)) {
                DynamicObject dataEntity = this.getModel().getDataEntity(true);
                DataEntityState dataEntityState = dataEntity.getDataEntityState();
                Iterable bizChangedProperties = dataEntityState.getBizChangedProperties();
                if (this.getModel().getDataChanged()) {
                    for (IDataEntityProperty next : bizChangedProperties) {
                        String name = next.getName();
                        if ("createorg_id".equals(name) || "msgtitlehide".equals(name) || "msgmainhide".equals(name) || "msgconclusionhide".equals(name)) {
                            dataEntityState.setBizChanged(next.getOrdinal(), false);
                            continue;
                        }
                        if (!"pushchannel".equals(name) && !"timezone_id".equals(name) && !"bizapp_id".equals(name) && !"warnscene_id".equals(name) && !"localeid".equals(name) && !"owner_id".equals(name) && !"startdate".equals(name) && !"enddate".equals(name) || (Long)this.getModel().getValue("id") != 0L) continue;
                        dataEntityState.setBizChanged(next.getOrdinal(), false);
                    }
                    DynamicObjectCollection collection = dataEntity.getDynamicObjectCollection("rcrelationentryentity");
                    collection.forEach(dy -> {
                        DataEntityState entityState = dy.getDataEntityState();
                        for (IDataEntityProperty bizChangedProperty : entityState.getBizChangedProperties()) {
                            String name = bizChangedProperty.getName();
                            if (!"rcuserdisplay".equals(name)) continue;
                            dy.getDataEntityState().setBizChanged(bizChangedProperty.getOrdinal(), false);
                        }
                    });
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    private void setReceiverFlexByReceiverType() {
        this.setReceiverFlexByReceiverType("relations", "relationsflex");
        this.setReceiverFlexByReceiverType("designee", "designeeflex");
        this.setReceiverFlexByReceiverType("role", "roleflex");
        this.setReceiverFlexByReceiverType(KEY_ADMIN_ORG, "adminorgflex");
        this.setReceiverFlexByReceiverType("position", "positionflex");
        this.setReceiverFlexByReceiverType("plugin", "pluginflex");
    }

    private void setReceiverFlexByReceiverType(String receiverType, String flexKey) {
        String receiverTypeValue = (String)this.getModel().getValue("msgreceivertype");
        boolean selected = HRStringUtils.isNotEmpty((String)receiverTypeValue) && receiverTypeValue.contains(receiverType);
        this.getView().setVisible(Boolean.valueOf(selected), new String[]{flexKey});
    }

    public void itemClick(ItemClickEvent evt) {
        try {
            super.itemClick(evt);
            this.formProcessor.updateFormStatus();
        }
        catch (Exception exception) {
            LOGGER.error("itemClick_error_", (Throwable)exception);
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        try {
            String changeKey = args.getProperty().getName();
            if ("warnscene".equals(changeKey)) {
                this.reloadDefaultData();
            } else if ("repeatperiod".equals(changeKey)) {
                this.processControlCustomYearDay(true);
            } else if ("monthweek".equals(changeKey)) {
                this.processControlCustomYearDay(false);
            } else if ("msgreceivertype".equals(changeKey)) {
                this.setReceiverFlexByReceiverType();
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    private void reloadDefaultData() {
        this.setSchemeConfVisible();
        this.setBizApp();
        this.clearSchemeConf();
        this.getModel().setValue("repeatperiod", (Object)WarnRepeatPeriodEnum.ByDays.getPeriod());
        this.processControlCustomYearDay(true);
        this.initDataCache();
        this.setUserTimeZone();
    }

    private void clearSchemeConf() {
        this.getModel().beginInit();
        this.getModel().setValue("msgreceivertype", (Object)"");
        this.getModel().deleteEntryData("rcrelationentryentity");
        this.getModel().deleteEntryData("rcfixentryentity");
        this.getModel().deleteEntryData("rcroleentryentity");
        this.getModel().deleteEntryData("adminorgentry");
        this.getModel().deleteEntryData("positionentry");
        this.getModel().deleteEntryData("pluginentry");
        this.getView().updateView();
        this.getModel().endInit();
    }

    private String getActionTag() {
        return ResManager.loadKDString((String)"\u624b\u5de5\u6267\u884c", (String)"WarnSchemeEditPlugin_4", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        block14: {
            try {
                super.beforeDoOperation(args);
                AbstractOperate op = (AbstractOperate)args.getSource();
                String operateKey = op.getOperateKey();
                if ("manualexecute".equals(operateKey)) {
                    long id = this.getModel().getDataEntity().getLong("id");
                    DynamicObject warnScene = this.getModel().getDataEntity().getDynamicObject("warnscene");
                    DynamicObject bizObjDy = this.getModel().getDataEntity().getDynamicObject("warnbizobj");
                    if (id == 0L || warnScene == null && bizObjDy == null) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u4fdd\u5b58\u6570\u636e\u3002", (String)"WarnSchemeEditPlugin_2", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                        return;
                    }
                    Long sceneId = warnScene == null ? null : (Long)warnScene.getPkValue();
                    String bizObjId = bizObjDy == null ? null : String.valueOf(bizObjDy.getPkValue());
                    EngineLog log = EngineLog.getInstance((String)String.valueOf(id), (Long)sceneId, (String)bizObjId, (WarnScheduleOperationType)WarnScheduleOperationType.Manual);
                    log.logRecord(this.getActionTag(), ResManager.loadKDString((String)"\u624b\u52a8\u6267\u884c\u5f00\u59cb", (String)"WarnSchemeEditPlugin_5", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                    WarnScheduleStatus status = WarnScheduleStatus.Success;
                    try {
                        EarlyWarnServiceImpl.getInstance().execute(new WarnExecuteParamBo(sceneId, bizObjId, Long.valueOf(id), WarnScheduleOperationType.Manual.toString(), log.getEarlyWarnLogId(), null));
                        this.getView().showMessage(ResManager.loadKDString((String)"\u624b\u52a8\u6267\u884c\u6210\u529f", (String)"WarnSchemeEditPlugin_9", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                        break block14;
                    }
                    catch (Throwable e) {
                        status = WarnScheduleStatus.Failure;
                        String msg = String.format(ResManager.loadKDString((String)"\u624b\u52a8\u6267\u884c\u5f02\u5e38\uff1a%s", (String)"WarnSchemeEditPlugin_6", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), e.getLocalizedMessage());
                        log.logRecord(this.getActionTag(), msg);
                        this.getView().showErrorNotification(msg);
                        break block14;
                    }
                    finally {
                        log.logRecord(this.getActionTag(), ResManager.loadKDString((String)"\u624b\u52a8\u6267\u884c\u7ed3\u675f", (String)"WarnSchemeEditPlugin_7", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                        log.endRecord(status);
                    }
                }
                if ("walktest".equals(operateKey)) {
                    long id = this.getModel().getDataEntity().getLong("id");
                    DynamicObject warnScene = this.getModel().getDataEntity().getDynamicObject("warnscene");
                    DynamicObject bizObjDy = this.getModel().getDataEntity().getDynamicObject("warnbizobj");
                    if (id == 0L || warnScene == null && bizObjDy == null) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u4fdd\u5b58\u6570\u636e\u3002", (String)"WarnSchemeEditPlugin_2", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                        return;
                    }
                    Long sceneId = warnScene == null ? null : (Long)warnScene.getPkValue();
                    String bizObjId = bizObjDy == null ? null : String.valueOf(bizObjDy.getPkValue());
                    this.showPokeTestForm(id, sceneId, bizObjId);
                } else if ("viewexelog".equals(operateKey)) {
                    Object pkValue = this.getModel().getDataEntity().getPkValue();
                    if (pkValue == null || "0".equals(String.valueOf(pkValue))) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u4fdd\u5b58\u6570\u636e", (String)"WarnSchemeEditPlugin_8", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                        return;
                    }
                    ListShowParameter listShowParameter = ShowFormHelper.createShowListForm((String)"hrcs_warnexeclog", (boolean)false, (int)0, (boolean)true);
                    listShowParameter.getListFilterParameter().setFilter(new QFilter("warnscheme", "=", pkValue));
                    this.getView().showForm((FormShowParameter)listShowParameter);
                }
            }
            catch (Exception exception) {
                LOGGER.error("error:", (Throwable)exception);
            }
        }
    }

    private void showPokeTestForm(Long warnSchemeId, Long sceneId, String bizObjId) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_walktestconfirm");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        Map customParams = showParameter.getCustomParams();
        customParams.put("formId", this.getView().getFormShowParameter().getFormId());
        customParams.put("entitynumber", this.getView().getFormShowParameter().getFormId());
        customParams.put("entityname", StringUtils.isBlank((String)this.getView().getFormShowParameter().getCaption()) ? this.getView().getFormShowParameter().getFormConfig().getCaption().getLocaleValue() : this.getView().getFormShowParameter().getCaption());
        customParams.put("warnSchemeId", warnSchemeId);
        customParams.put("warnSceneId", sceneId);
        customParams.put("warnBizObjId", bizObjId);
        customParams.put("operationType", WarnScheduleOperationType.PokeTest.toString());
        customParams.put("taskClassName", "kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.EarlyWarningTask");
        this.getView().showForm(showParameter);
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        try {
            String propertyName = beforeF7SelectEvent.getProperty().getName();
            if (HRStringUtils.equals((String)KEY_ADMIN_ORG, (String)propertyName)) {
                beforeF7SelectEvent.getFormShowParameter().setCustomParam("struct_project_ids", (Object)SerializationUtils.toJsonString(Collections.singletonList("1010")));
                beforeF7SelectEvent.getFormShowParameter().setCustomParam("struct_project_visible", (Object)"false");
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    public void customEvent(CustomEventArgs args) {
        try {
            if (HRStringUtils.equals((String)args.getEventName(), (String)"selectDay")) {
                this.getModel().setValue("yearday", (Object)args.getEventArgs());
            }
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    private void setDefaultValue() {
        Date startDate;
        BaseShowParameter showParameter = (BaseShowParameter)this.getView().getFormShowParameter();
        Boolean isCopy = (Boolean)showParameter.getCustomParam("iscopy");
        if (isCopy != null && isCopy.booleanValue()) {
            Object id = showParameter.getPkId();
            HRBaseServiceHelper helper = new HRBaseServiceHelper(showParameter.getFormId());
            DynamicObject dbDy = helper.queryOriginalOne("number,name", id);
            this.getModel().setValue("number", (Object)(dbDy.getString("number") + "_copy"));
            ((OrmLocaleValue)this.getModel().getValue("name")).clear();
            this.getModel().setValue("name", (Object)MessageFormat.format(ResManager.loadKDString((String)"{0}_\u590d\u5236", (String)"WarnSchemeEditPlugin_3", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), dbDy.getString("name")));
        } else {
            isCopy = Boolean.FALSE;
        }
        Date endDate = (Date)this.getModel().getValue("enddate");
        if (endDate == null && !isCopy.booleanValue()) {
            this.getModel().setValue("enddate", (Object)HRBaseUtils.getMaxEndDate());
        }
        if ((startDate = (Date)this.getModel().getValue("startdate")) == null && !isCopy.booleanValue()) {
            this.getModel().setValue("startdate", (Object)new Date());
        }
    }

    private void setUserTimeZone() {
        InteServiceImpl inteServiceImpl = new InteServiceImpl();
        long userId = RequestContext.get().getCurrUserId();
        DynamicObject userTimezoneMeta = inteServiceImpl.getUserTimezone(Long.valueOf(userId));
        if (userTimezoneMeta == null) {
            userTimezoneMeta = inteServiceImpl.getSysTimezone();
        }
        this.getModel().setValue("timezone", (Object)userTimezoneMeta);
        this.getModel().setValue("startdate", (Object)new Date());
        this.getModel().setValue("enddate", (Object)HRBaseUtils.getMaxEndDate());
    }

    private void processControlCustomYearDay(boolean clear) {
        String monthWeek;
        String repeatPeriod = (String)this.getModel().getValue("repeatperiod");
        if (StringUtils.isEmpty((String)repeatPeriod)) {
            return;
        }
        if (WarnRepeatPeriodEnum.ByYears.getPeriod().equals(repeatPeriod)) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"customyearday"});
            CustomControl customcontrol = (CustomControl)this.getView().getControl("customyearday");
            this.getView().getControl("repeatperiod");
            String yearDay = (String)this.getModel().getValue("yearday");
            if (StringUtils.isEmpty((String)yearDay) || clear) {
                yearDay = "01-01";
            }
            HashMap data = Maps.newHashMapWithExpectedSize((int)2);
            data.put("enable", true);
            data.put("value", yearDay);
            customcontrol.setData((Object)data);
            this.getView().setVisible(Boolean.FALSE, new String[]{"weekday"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"monthday"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"monthweek"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"monthweekday"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"customcron"});
        } else {
            if (WarnRepeatPeriodEnum.ByWeeks.getPeriod().equals(repeatPeriod)) {
                String weekDay = (String)this.getModel().getValue("weekday");
                if (StringUtils.isEmpty((String)weekDay) || ",,".equals(weekDay) || clear) {
                    this.getModel().setValue("weekday", (Object)",2,");
                }
                this.getView().setVisible(Boolean.TRUE, new String[]{"weekday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthweek"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthweekday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"customcron"});
            } else if (WarnRepeatPeriodEnum.ByMonths.getPeriod().equals(repeatPeriod)) {
                monthWeek = (String)this.getModel().getValue("monthweek");
                if ((monthWeek == null || HRStringUtils.isNotEmpty((String)monthWeek) && ",,".equals(monthWeek)) && clear) {
                    this.getModel().setValue("monthday", (Object)",1,");
                }
                this.getView().setVisible(Boolean.FALSE, new String[]{"weekday"});
                this.getView().setVisible(Boolean.TRUE, new String[]{"monthday"});
                this.getView().setVisible(Boolean.TRUE, new String[]{"monthweek"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthweekday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"customcron"});
            } else if (WarnRepeatPeriodEnum.ByDays.getPeriod().equals(repeatPeriod)) {
                this.getView().setVisible(Boolean.FALSE, new String[]{"weekday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthweek"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthweekday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"customcron"});
            } else if (WarnRepeatPeriodEnum.ByCustomCron.getPeriod().equals(repeatPeriod)) {
                this.getView().setVisible(Boolean.TRUE, new String[]{"customcron"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"weekday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthweek"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monthweekday"});
                this.getView().setVisible(Boolean.FALSE, new String[]{"monitortime"});
            }
            this.getView().setVisible(Boolean.FALSE, new String[]{"customyearday"});
        }
        if (!WarnRepeatPeriodEnum.ByCustomCron.getPeriod().equals(repeatPeriod)) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"monitortime"});
        }
        if (clear) {
            this.getModel().setValue("monitortime", (Object)"0");
            this.getModel().setValue("customcron", null);
            this.getModel().setValue("monthweek", null);
            this.getModel().setValue("monthweekday", null);
        }
        if (HRStringUtils.isNotEmpty((String)(monthWeek = (String)this.getModel().getValue("monthweek"))) && !",,".equals(monthWeek)) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"monthweekday"});
        } else {
            this.getModel().setValue("monthweekday", null);
        }
    }

    private void setBizApp() {
        if (this.getModel().getValue("warnscene") != null) {
            this.getModel().setValue("bizapp", (Object)((DynamicObject)this.getModel().getValue("warnscene")).getString("bizapp.id"));
        }
    }

    private void setSchemeConfVisible() {
        DynamicObject sceneDy = (DynamicObject)this.getModel().getValue("warnscene");
        boolean visible = sceneDy != null && sceneDy.getBoolean("permrc");
        this.setRcFixPerm(visible);
        this.getView().setVisible(Boolean.valueOf(sceneDy != null), new String[]{"schemeconfflex"});
    }

    private void setRcFixPerm(boolean visible) {
        this.getView().setVisible(Boolean.valueOf(visible), new String[]{"rcfixperm"});
        this.getView().setVisible(Boolean.valueOf(visible), new String[]{"roleperm"});
        this.getView().setVisible(Boolean.valueOf(visible), new String[]{"adminorgperm"});
        this.getView().setVisible(Boolean.valueOf(visible), new String[]{"positionperm"});
    }
}
