/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONArray
 *  com.google.common.base.Joiner
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.ext.hr.filter.control.HRFilter
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.MessageTypes
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil
 *  kd.hr.hbp.business.service.labelandreport.AnobjFilterUtil
 *  kd.hr.hbp.business.service.labelandreport.DataPreviewService
 *  kd.hr.hbp.business.service.labelandreport.FieldDefineService
 *  kd.hr.hbp.business.service.labelandreport.HRFilterUtil
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.model.complexobj.HRComplexObjContext
 *  kd.hr.hbp.common.model.complexobj.labelandreport.EntityRelationCommonBo
 *  kd.hr.hbp.common.model.complexobj.labelandreport.FieldTreeNode
 *  kd.hr.hbp.common.model.complexobj.labelandreport.JoinEntityCommonBo
 *  kd.hr.hbp.common.model.complexobj.labelandreport.ModifyFieldNameBo
 *  kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hrcs.bussiness.service.label.ConvertFieldService
 *  kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper
 *  kd.hr.hrcs.bussiness.service.label.LabelService
 *  kd.hr.hrcs.bussiness.service.label.LblStrategyServiceHelper
 *  kd.hr.hrcs.formplugin.web.label.util.LabelObjectTipUtil
 */
package kd.hr.hrcs.formplugin.web.label;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.ext.hr.filter.control.HRFilter;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.MessageTypes;
import kd.bos.form.ShowType;
import kd.bos.form.container.Tab;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.util.StringUtils;
import kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil;
import kd.hr.hbp.business.service.labelandreport.AnobjFilterUtil;
import kd.hr.hbp.business.service.labelandreport.DataPreviewService;
import kd.hr.hbp.business.service.labelandreport.FieldDefineService;
import kd.hr.hbp.business.service.labelandreport.HRFilterUtil;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.model.complexobj.HRComplexObjContext;
import kd.hr.hbp.common.model.complexobj.labelandreport.EntityRelationCommonBo;
import kd.hr.hbp.common.model.complexobj.labelandreport.FieldTreeNode;
import kd.hr.hbp.common.model.complexobj.labelandreport.JoinEntityCommonBo;
import kd.hr.hbp.common.model.complexobj.labelandreport.ModifyFieldNameBo;
import kd.hr.hbp.common.model.complexobj.labelandreport.QueryFieldCommonBo;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hrcs.bussiness.service.label.ConvertFieldService;
import kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper;
import kd.hr.hrcs.bussiness.service.label.LabelService;
import kd.hr.hrcs.bussiness.service.label.LblStrategyServiceHelper;
import kd.hr.hrcs.formplugin.web.label.util.LabelObjectTipUtil;

public final class LabelObjectPlugin
extends HRBaseDataCommonEdit {
    private static final Log LOGGER = LogFactory.getLog(LabelObjectPlugin.class);
    private static final String NEXT_ONE = "nextone";
    private static final String LAST_ONE = "lastone";
    private static final String TAB_AP = "tabap";
    private static final String PREVIEW = "preview";
    private static final String BAR_SAVE = "bar_save";
    private static final String FILTER = "hrfilterap";
    private static final String KEY_CUSTOM = "customcontrolap";
    private final LabelService labelService = new LabelService();

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        Toolbar toolbar = (Toolbar)this.getControl("tbmain");
        toolbar.addItemClickListener((ItemClickListener)this);
    }

    protected List<String> getUnCheckField() {
        ArrayList uncheckFieldList = Lists.newArrayListWithExpectedSize((int)16);
        uncheckFieldList.add("mainbo");
        uncheckFieldList.add("ruledate");
        return uncheckFieldList;
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (OperationStatus.VIEW.equals((Object)status)) {
            this.getPageCache().put("pageStatus", "view");
        }
        this.loadCustomControlData();
        HRFilter hrFilter = (HRFilter)this.getControl(FILTER);
        hrFilter.setValue((String)this.getModel().getValue("condition"));
    }

    public void customEvent(CustomEventArgs e) {
        if ("getAllData".equals(e.getEventName())) {
            this.getAllData(e);
        } else if ("dragEntity".equals(e.getEventName())) {
            this.dragEntity(e);
        } else if ("clickRelevance".equals(e.getEventName())) {
            this.clickRelevance(e);
        } else if ("delEntity".equals(e.getEventName())) {
            this.delEntity(e);
        } else if ("modifyFieldName".equals(e.getEventName())) {
            this.modifyFieldName(e);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        if ("relation".equals(closedCallBackEvent.getActionId())) {
            String returnData = (String)closedCallBackEvent.getReturnData();
            HashMap map = Maps.newHashMapWithExpectedSize((int)16);
            EntityRelationCommonBo entityRelationCommonBo = (EntityRelationCommonBo)JSON.parseObject((String)returnData, EntityRelationCommonBo.class);
            map.put("entityRelation", entityRelationCommonBo);
            String click = this.getPageCache().get("click");
            if (HRStringUtils.equals((String)"label", (String)click)) {
                map.put("method", "setEntityRelationByLabel");
                if (!HRStringUtils.isEmpty((String)returnData)) {
                    this.getPageCache().put("mainLabelObjRel", returnData);
                }
            } else {
                map.put("method", "setEntityRelation");
            }
            this.setCustomControlData(map);
        } else if ("modifyFieldName".equals(closedCallBackEvent.getActionId())) {
            ModifyFieldNameBo modifyFieldNameBo = (ModifyFieldNameBo)closedCallBackEvent.getReturnData();
            if (null == modifyFieldNameBo) {
                return;
            }
            HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
            dataMap.put("method", "modifyFieldName");
            dataMap.put("data", modifyFieldNameBo);
            this.setCustomControlData(dataMap);
            this.setFieldNameMap(modifyFieldNameBo);
        }
    }

    private void setFieldNameMap(ModifyFieldNameBo modifyFieldNameBo) {
        Map<String, LocaleString> fieldNameMap = this.getFieldNameMap();
        List selectedFieldAlias = modifyFieldNameBo.getFieldNodes().stream().map(FieldTreeNode::getFieldAlias).collect(Collectors.toList());
        if (!selectedFieldAlias.contains(modifyFieldNameBo.getNumber())) {
            fieldNameMap.put(modifyFieldNameBo.getNumber(), modifyFieldNameBo.getName());
        }
        this.getPageCache().put("fieldNameMap", SerializationUtils.toJsonString(fieldNameMap));
    }

    private void setFieldNameMap(List<QueryFieldCommonBo> queryFieldCommonBoList) {
        Map<String, LocaleString> fieldNameMap = this.getFieldNameMap();
        fieldNameMap.putAll(queryFieldCommonBoList.stream().filter(bo -> null != bo.getSelectedField() && bo.getSelectedField() == false).collect(Collectors.toMap(QueryFieldCommonBo::getFieldAlias, QueryFieldCommonBo::getFieldName, (oldValue, newValue) -> newValue)));
        this.getPageCache().put("fieldNameMap", SerializationUtils.toJsonString(fieldNameMap));
    }

    private Map<String, LocaleString> getFieldNameMap() {
        String fieldNameMapStr = this.getPageCache().get("fieldNameMap");
        Map<String, Object> fieldNameMap = new HashMap<String, LocaleString>(16);
        if (StringUtils.isNotEmpty((String)fieldNameMapStr)) {
            fieldNameMap = (Map)SerializationUtils.fromJsonString((String)fieldNameMapStr, Map.class);
        }
        return fieldNameMap;
    }

    private Map<String, LocaleString> getSelectedFieldNameMap() {
        String fieldStr = this.getPageCache().get("queryFields");
        List selectedQueryFieldList = JSONArray.parseArray((String)fieldStr, QueryFieldCommonBo.class);
        return selectedQueryFieldList.stream().collect(Collectors.toMap(QueryFieldCommonBo::getFieldAlias, QueryFieldCommonBo::getFieldName, (oldValue, newValue) -> newValue));
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        if ("confirmDelete".equals(evt.getCallBackId()) && evt.getResult() == MessageBoxResult.Yes) {
            String delEntity = this.getPageCache().get("delEntity");
            Map map = (Map)SerializationUtils.fromJsonString((String)delEntity, Map.class);
            HashMap resultMap = Maps.newHashMapWithExpectedSize((int)16);
            if ("main".equals(((Map)map.get("entityInfo")).get("type"))) {
                this.getPageCache().put("mainBo", null);
            }
            resultMap.put("method", "confirmDelete");
            resultMap.put("entityInfo", map.get("entityInfo"));
            resultMap.put("calFields", Collections.emptyList());
            this.setCustomControlData(resultMap);
        }
    }

    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        String itemKey = evt.getItemKey();
        if (PREVIEW.equals(itemKey)) {
            this.sendGetAllDataRequest(false);
            try {
                Thread.sleep(500L);
            }
            catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        String name = e.getProperty().getName();
        ChangeData changeSet = e.getChangeSet()[0];
        if (HRStringUtils.equals((String)"ruledate", (String)name)) {
            Date newData = (Date)changeSet.getNewValue();
            if (null == newData) {
                return;
            }
            String dateFormat = this.getView().getPageCache().get("ruleDateFormat");
            String date = HRDateTimeUtils.format((Date)newData, (String)dateFormat);
            HRFilter hrFilter = (HRFilter)this.getView().getControl(FILTER);
            hrFilter.setDate(date);
            this.getModel().setValue("ruledate", null);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if ("save".equals(formOperate.getOperateKey())) {
            if (!this.checkRef()) {
                args.setCancel(true);
                return;
            }
            String fieldStr = this.getPageCache().get("queryFields");
            HRFilter hrFilter = (HRFilter)this.getControl(FILTER);
            this.getModel().setValue("condition", (Object)hrFilter.getValue());
            String joinEntityStr = this.getPageCache().get("entityNodes");
            String unSlcFieldStr = this.getPageCache().get("unSlcFields");
            String mainLblObjStr = this.getPageCache().get("mainLabelObjBo");
            String relStr = this.getPageCache().get("mainLabelObjRel");
            formOperate.getOption().setVariableValue("entityNodes", joinEntityStr);
            formOperate.getOption().setVariableValue("queryFields", fieldStr);
            formOperate.getOption().setVariableValue("unSlcFields", unSlcFieldStr);
            formOperate.getOption().setVariableValue("mainLabelObjBo", mainLblObjStr);
            formOperate.getOption().setVariableValue("mainLabelObjRel", relStr);
        }
    }

    private boolean checkRef() {
        return this.checkRef(null);
    }

    private boolean checkRef(Map entityInfo) {
        long lblObjId = this.getModel().getDataEntity().getLong("id");
        if (lblObjId != 0L) {
            List selectedFieldAlias;
            DynamicObject[] dys;
            boolean lblObjCfgRef = false;
            if (null != entityInfo) {
                String entityAlias = (String)entityInfo.get("entityAlias");
                String entityNumber = (String)entityInfo.get("entityNumber");
                if ("main".equals(entityInfo.get("type"))) {
                    QFilter[] qFilters = new QFilter[]{new QFilter("labelobject.id", "=", (Object)lblObjId), new QFilter("param.entitynumber", "=", (Object)entityNumber), new QFilter("param.fieldpath", "not like", (Object)(entityNumber + ".%"))};
                    dys = LblStrategyServiceHelper.getLabelRef((QFilter[])qFilters);
                } else {
                    QFilter[] qFilters = new QFilter[]{new QFilter("labelobject.id", "=", (Object)lblObjId), new QFilter("param.entitynumber", "=", (Object)entityNumber), new QFilter("param.fieldalias", "like", (Object)(entityAlias + ".%"))};
                    dys = LblStrategyServiceHelper.getLabelRef((QFilter[])qFilters);
                }
                selectedFieldAlias = Collections.emptyList();
                lblObjCfgRef = LabelObjectServiceHelper.isLblObjConfigRef((Long)lblObjId, (String)entityAlias);
            } else {
                dys = LblStrategyServiceHelper.getLabelRef((Long)lblObjId);
                List selectedField = SerializationUtils.fromJsonStringToList((String)this.getPageCache().get("queryFields"), QueryFieldCommonBo.class);
                selectedFieldAlias = selectedField.stream().map(QueryFieldCommonBo::getFieldAlias).collect(Collectors.toList());
            }
            List tips = LabelObjectTipUtil.getTips((DynamicObject[])dys, selectedFieldAlias, (long)lblObjId, (boolean)lblObjCfgRef, (Map)entityInfo);
            if (tips.size() == 1) {
                this.getView().showErrorNotification((String)tips.get(0));
                return false;
            }
            if (tips.size() > 1) {
                this.getView().showMessage(ResManager.loadKDString((String)"\u64cd\u4f5c\u5931\u8d25", (String)"LabelObjectPlugin_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), Joiner.on((String)"\r\n").join((Iterable)tips), MessageTypes.Default);
                return false;
            }
        }
        return true;
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        if (afterDoOperationEventArgs.getOperationResult() != null && afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            if (NEXT_ONE.equals(afterDoOperationEventArgs.getOperateKey())) {
                this.next();
                if (!HRBaseDataConfigUtil.getAudit((String)"hrcs_labelobject")) {
                    this.getModel().setValue("status", (Object)"A");
                }
            } else if (LAST_ONE.equals(afterDoOperationEventArgs.getOperateKey())) {
                this.previous();
            }
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        Tab tab;
        String currentTab;
        if (NEXT_ONE.equals(evt.getItemKey()) && "tabpageap1".equals(currentTab = (tab = (Tab)this.getView().getControl(TAB_AP)).getCurrentTab())) {
            this.sendGetAllDataRequest(true);
            evt.setCancel(true);
            try {
                Thread.sleep(500L);
            }
            catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
    }

    private void getAllData(CustomEventArgs e) {
        String mainObjRelStr;
        String queryFieldListStr;
        Map map = (Map)SerializationUtils.fromJsonString((String)e.getEventArgs(), Map.class);
        List entityNodes = (List)map.get("entityNodes");
        if (CollectionUtils.isEmpty((Collection)entityNodes)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e3b\u5b9e\u4f53\uff0c\u7136\u540e\u518d\u9009\u62e9\u5b50\u5b9e\u4f53\u3002", (String)"LabelObjectPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        String isNext = (String)map.get("operate");
        String entityNodesStr = SerializationUtils.toJsonString((Object)entityNodes);
        if (!this.validateInput(entityNodesStr, queryFieldListStr = this.removeTitle(map.get("queryFields")), mainObjRelStr = this.getPageCache().get("mainLabelObjRel"))) {
            return;
        }
        if ("nextStep".equals(isNext)) {
            String queryFieldBoStr = this.getQueryFieldBoStr(queryFieldListStr);
            this.getPageCache().put("entityNodes", entityNodesStr);
            this.getPageCache().put("queryFields", queryFieldBoStr);
            this.setFilterParam(entityNodesStr);
            this.getView().invokeOperation(NEXT_ONE);
        } else {
            HRFilter hrFilter;
            List errorInfoList;
            this.getView().hideLoading();
            Tab tab = (Tab)this.getView().getControl(TAB_AP);
            String currentTab = tab.getCurrentTab();
            String condition = null;
            if ("tabpageap2".equals(currentTab) && !CollectionUtils.isEmpty((Collection)(errorInfoList = this.labelService.validateCondition(condition = (hrFilter = (HRFilter)this.getControl(FILTER)).getValue(true))))) {
                String errorInfo = String.join((CharSequence)";", errorInfoList);
                this.getView().showTipNotification(errorInfo);
                return;
            }
            ArrayList qFilterList = Lists.newArrayList();
            if (!HRStringUtils.isEmpty(condition)) {
                List joinEntityCommonBoList = JSON.parseArray((String)entityNodesStr, JoinEntityCommonBo.class);
                FieldDefineService fieldDefineService = new FieldDefineService();
                List fieldTreeNodeList = fieldDefineService.getEntityAllFields(joinEntityCommonBoList, null);
                ArrayList queryFieldCommonBoList = Lists.newArrayListWithExpectedSize((int)64);
                ConvertFieldService.convertToBO((List)queryFieldCommonBoList, (List)fieldTreeNodeList);
                QFilter qFilter = HRFilterUtil.condition2QFilter4HRReport((String)condition, (String)SerializationUtils.toJsonString((Object)queryFieldCommonBoList));
                if (qFilter != null) {
                    qFilterList.add(qFilter);
                }
            }
            List dataPreviewField = DataPreviewService.getDataPreviewField((String)queryFieldListStr);
            HRComplexObjContext context = DataPreviewService.getContext((String)entityNodesStr, (String)queryFieldListStr, (List)qFilterList);
            FormShowParameter showParameter = DataPreviewService.getDataPreviewShowParameter((HRComplexObjContext)context, (List)dataPreviewField);
            this.getView().showForm(showParameter);
        }
    }

    private void dragEntity(CustomEventArgs e) {
        JoinEntityCommonBo joinEntityCommonBo = (JoinEntityCommonBo)JSON.parseObject((String)e.getEventArgs(), JoinEntityCommonBo.class);
        String mainBoStr = this.getPageCache().get("mainBo");
        JoinEntityCommonBo mainBo = (JoinEntityCommonBo)JSON.parseObject((String)mainBoStr, JoinEntityCommonBo.class);
        if (mainBo == null) {
            this.getPageCache().put("mainBo", e.getEventArgs());
        }
        Map dragEntityResult = this.labelService.getDragEntityResult(joinEntityCommonBo, mainBo);
        this.setCustomControlData(dragEntityResult);
        String entityName = this.getPageCache().get("entityName");
        String oriEntityName = this.getPageCache().get("oriEntityName");
        Map entityNameMap = HRStringUtils.isEmpty((String)entityName) ? Maps.newHashMapWithExpectedSize((int)8) : (Map)SerializationUtils.fromJsonString((String)entityName, Map.class);
        Map oriEntityNameMap = HRStringUtils.isEmpty((String)oriEntityName) ? Maps.newHashMapWithExpectedSize((int)8) : (Map)SerializationUtils.fromJsonString((String)oriEntityName, Map.class);
        entityNameMap.put(joinEntityCommonBo.getEntityAlias(), joinEntityCommonBo.getDisplayName());
        oriEntityNameMap.put(joinEntityCommonBo.getEntityAlias(), joinEntityCommonBo.getDisplayName());
        this.getPageCache().put("entityName", SerializationUtils.toJsonString(entityNameMap));
        this.getPageCache().put("oriEntityName", SerializationUtils.toJsonString(oriEntityNameMap));
    }

    private void clickRelevance(CustomEventArgs e) {
        String relation;
        Map requestMap = (Map)SerializationUtils.fromJsonString((String)e.getEventArgs(), Map.class);
        String click = (String)requestMap.get("click");
        if (HRStringUtils.equals((String)"label", (String)click)) {
            relation = SerializationUtils.toJsonString(requestMap.get("relation"));
            this.getPageCache().put("click", "label");
        } else {
            relation = e.getEventArgs();
            this.getPageCache().put("click", null);
        }
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_setentityrelation");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setCustomParam("relation", (Object)relation);
        showParameter.setCustomParam("entityName", (Object)this.getPageCache().get("entityName"));
        showParameter.setCustomParam("oriEntityName", (Object)this.getPageCache().get("oriEntityName"));
        showParameter.setCustomParam("hideJoinType", (Object)"inner,right");
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "relation"));
        this.getView().showForm(showParameter);
    }

    private void delEntity(CustomEventArgs e) {
        Map map;
        Map entityInfo;
        Long id = (Long)this.getModel().getValue("id");
        if (id != null && !HRStringUtils.isEmpty((String)((String)(entityInfo = (Map)(map = (Map)SerializationUtils.fromJsonString((String)e.getEventArgs(), Map.class)).get("entityInfo")).get("id"))) && !this.checkRef(entityInfo)) {
            return;
        }
        String tipMsg = ResManager.loadKDString((String)"\u5220\u9664\u8be5\u4e1a\u52a1\u5bf9\u8c61\uff0c\u76f8\u5173\u7684\u8ba1\u7b97\u5b57\u6bb5\u4e5f\u5c06\u88ab\u4e00\u5e76\u5220\u9664\uff0c\u786e\u5b9a\u8981\u5220\u9664\u5417\uff1f", (String)"LabelObjectPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        this.getView().showConfirm(tipMsg, MessageBoxOptions.YesNo, new ConfirmCallBackListener("confirmDelete", (IFormPlugin)this));
        this.getPageCache().put("delEntity", e.getEventArgs());
    }

    private void modifyFieldName(CustomEventArgs e) {
        FormShowParameter parameter = new FormShowParameter();
        parameter.setFormId("hbp_modifyfieldname");
        parameter.getOpenStyle().setShowType(ShowType.Modal);
        parameter.setCustomParam("modifyFieldNameParams", (Object)e.getEventArgs());
        parameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "modifyFieldName"));
        this.getView().showForm(parameter);
    }

    private void next() {
        Tab tab = (Tab)this.getView().getControl(TAB_AP);
        String currentTab = tab.getCurrentTab();
        if ("tabpageap".equals(currentTab)) {
            this.setCustomControlData();
        }
        String nextTab = LabelService.doStepAction((Tab)tab, (boolean)true);
        this.setVisible(nextTab);
    }

    private void setVisible(String nextTab) {
        if ("tabpageap2".equals(nextTab)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{NEXT_ONE});
            this.getView().setVisible(Boolean.valueOf(true), new String[]{BAR_SAVE, LAST_ONE, PREVIEW});
        } else if ("tabpageap1".equals(nextTab)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{BAR_SAVE});
            this.getView().setVisible(Boolean.valueOf(true), new String[]{NEXT_ONE, LAST_ONE, PREVIEW});
        } else {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{LAST_ONE, PREVIEW, BAR_SAVE});
            this.getView().setVisible(Boolean.valueOf(true), new String[]{NEXT_ONE});
        }
    }

    private void sendGetAllDataRequest(boolean isNext) {
        HashMap data = Maps.newHashMapWithExpectedSize((int)16);
        data.put("method", "getAllData");
        data.put("random", System.currentTimeMillis());
        data.put("operate", isNext ? "nextStep" : PREVIEW);
        this.setCustomControlData(data);
    }

    private void previous() {
        Tab tab = (Tab)this.getView().getControl(TAB_AP);
        String currentTab = tab.getCurrentTab();
        if ("tabpageap2".equals(currentTab)) {
            HRFilter hrFilter = (HRFilter)this.getControl(FILTER);
            this.getModel().setValue("condition", (Object)hrFilter.getValue());
        }
        String nextTab = LabelService.doStepAction((Tab)tab, (boolean)false);
        this.setVisible(nextTab);
    }

    private void fillFilterParam(List<Map<String, String>> paramList) {
        HRFilter hrFilter = (HRFilter)this.getControl(FILTER);
        this.labelService.fillFilterParam(paramList, hrFilter, hrFilter.getValue(), null);
    }

    private void loadCustomControlData() {
        Long id = this.getModel().getDataEntity().getLong("id");
        Map controlDataMap = this.labelService.getControlData(id);
        List joinEntityCommonBoList = (List)controlDataMap.get("entityNodes");
        HashMap entityNameMap = Maps.newHashMapWithExpectedSize((int)joinEntityCommonBoList.size());
        HashMap oriEntityNameMap = Maps.newHashMapWithExpectedSize((int)joinEntityCommonBoList.size());
        for (JoinEntityCommonBo joinEntityCommonBo : joinEntityCommonBoList) {
            LocaleString displayName;
            EntityRelationCommonBo entityRelation = joinEntityCommonBo.getEntityRelation();
            String entityName = joinEntityCommonBo.getDisplayName();
            if (entityRelation != null && (displayName = entityRelation.getDisplayName()) != null && !displayName.isEmpty()) {
                entityName = displayName.getLocaleValue();
            }
            entityNameMap.put(joinEntityCommonBo.getEntityAlias(), entityName);
            oriEntityNameMap.put(joinEntityCommonBo.getEntityAlias(), joinEntityCommonBo.getDisplayName());
        }
        this.getPageCache().put("entityName", SerializationUtils.toJsonString((Object)entityNameMap));
        this.getPageCache().put("oriEntityName", SerializationUtils.toJsonString((Object)oriEntityNameMap));
        List queryFieldCommonBoList = (List)controlDataMap.get("queryFieldCommonBoList");
        this.setFieldNameMap(queryFieldCommonBoList);
        controlDataMap.put("status", this.getView().getFormShowParameter().getStatus().toString());
        this.getPageCache().put("controlData", SerializationUtils.toJsonString((Object)controlDataMap));
        this.getPageCache().put("mainBo", SerializationUtils.toJsonString(controlDataMap.get("mainBo")));
    }

    private void setCustomControlData() {
        String isSet = this.getPageCache().get("isSet");
        if (!Boolean.TRUE.toString().equals(isSet)) {
            String dataStr = this.getPageCache().get("controlData");
            Map data = (Map)SerializationUtils.fromJsonString((String)dataStr, Map.class);
            this.setType(data);
            this.setCustomControlData(data);
            this.getPageCache().put("isSet", Boolean.TRUE.toString());
        }
    }

    private void setFilterParam(String entityNodesStr) {
        List<QueryFieldCommonBo> queryFieldCommonBoList = this.getQueryFieldCommonBoList(entityNodesStr);
        this.labelService.setBaseDataNum(queryFieldCommonBoList);
        List paramList = AnobjFilterUtil.getParamList(queryFieldCommonBoList);
        this.fillFilterParam(paramList);
    }

    public List<QueryFieldCommonBo> getQueryFieldCommonBoList(String entityNodesStr) {
        List joinEntityCommonBoList = JSON.parseArray((String)entityNodesStr, JoinEntityCommonBo.class);
        FieldDefineService service = new FieldDefineService();
        List treeNodeList = service.getEntityAllFields(joinEntityCommonBoList, null);
        ArrayList queryFieldCommonBoList = Lists.newArrayListWithCapacity((int)10);
        ConvertFieldService.convertToBO((List)queryFieldCommonBoList, (List)treeNodeList);
        AnalyseObjectUtil.setFieldControlType((List)queryFieldCommonBoList);
        Map<String, LocaleString> selectedFieldNameMap = this.getSelectedFieldNameMap();
        Map<String, LocaleString> fieldNameMap = this.getFieldNameMap();
        ArrayList<QueryFieldCommonBo> unSlcField = new ArrayList<QueryFieldCommonBo>(fieldNameMap.size());
        for (QueryFieldCommonBo queryFieldCommonBo : queryFieldCommonBoList) {
            if (fieldNameMap.containsKey(queryFieldCommonBo.getFieldAlias())) {
                queryFieldCommonBo.setFieldName(LocaleString.fromMap((Map)((Map)fieldNameMap.get(queryFieldCommonBo.getFieldAlias()))));
                queryFieldCommonBo.setSelectedField(Boolean.valueOf(false));
                unSlcField.add(queryFieldCommonBo);
                continue;
            }
            if (!selectedFieldNameMap.containsKey(queryFieldCommonBo.getFieldAlias())) continue;
            queryFieldCommonBo.setFieldName(selectedFieldNameMap.get(queryFieldCommonBo.getFieldAlias()));
        }
        this.getPageCache().put("unSlcFields", SerializationUtils.toJsonString(unSlcField));
        return queryFieldCommonBoList;
    }

    private void setCustomControlData(Object data) {
        CustomControl customControl = (CustomControl)this.getControl(KEY_CUSTOM);
        customControl.setData(data);
    }

    private boolean validateInput(String entityNodesStr, String queryFieldListStr, String mainObjRelStr) {
        List joinEntityCommonBoList = JSON.parseArray((String)entityNodesStr, JoinEntityCommonBo.class);
        List treeNodeList = JSON.parseArray((String)queryFieldListStr, FieldTreeNode.class);
        if (CollectionUtils.isEmpty((Collection)treeNodeList)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u52fe\u9009\u5b57\u6bb5\u540e\u91cd\u8bd5\u3002", (String)"LabelObjectPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        int emptyCount = 0;
        for (JoinEntityCommonBo joinEntityCommonBo : joinEntityCommonBoList) {
            EntityRelationCommonBo entityRelation = joinEntityCommonBo.getEntityRelation();
            if (entityRelation != null && !CollectionUtils.isEmpty((Collection)entityRelation.getConditions())) continue;
            ++emptyCount;
        }
        if (emptyCount > 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u914d\u7f6e\u5173\u8054\u5173\u7cfb\u3002", (String)"LabelObjectPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        return true;
    }

    private void setType(Map<String, Object> data) {
        data.put("type", "0");
        data.put("method", "updateType");
    }

    private String removeTitle(Object field) {
        String fieldStr = SerializationUtils.toJsonString((Object)field);
        List fieldTreeNodeList = JSONArray.parseArray((String)fieldStr, FieldTreeNode.class);
        fieldTreeNodeList.removeIf(next -> HRStringUtils.isEmpty((String)next.getFieldAlias()));
        return JSONArray.toJSONString((Object)fieldTreeNodeList);
    }

    private String getQueryFieldBoStr(String fieldTreeNodeStr) {
        List queryFieldCommonBoList = this.labelService.convertToBo(fieldTreeNodeStr);
        this.labelService.setBaseDataNum(queryFieldCommonBoList);
        return SerializationUtils.toJsonString((Object)queryFieldCommonBoList);
    }
}
