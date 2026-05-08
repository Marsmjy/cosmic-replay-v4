/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.google.common.collect.Lists
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.QueryEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.property.OrgProp
 *  kd.bos.exception.KDException
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.service.perm.ChoiceFieldPageCustomQueryService
 *  kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr
 *  kd.hr.hrcs.bussiness.service.perm.RoleManageService
 *  kd.hr.hrcs.bussiness.service.perm.dimension.EntityOrgFieldBuQueryService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper
 *  kd.hr.hrcs.common.constants.perm.log.EntityCtrlEntryRowModel
 *  org.apache.commons.collections.CollectionUtils
 */
package kd.hr.hrcs.formplugin.web.perm.dimension;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.QueryEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.property.OrgProp;
import kd.bos.exception.KDException;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.util.StringUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.service.perm.ChoiceFieldPageCustomQueryService;
import kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr;
import kd.hr.hrcs.bussiness.service.perm.RoleManageService;
import kd.hr.hrcs.bussiness.service.perm.dimension.EntityOrgFieldBuQueryService;
import kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper;
import kd.hr.hrcs.common.constants.perm.log.EntityCtrlEntryRowModel;
import org.apache.commons.collections.CollectionUtils;

public final class EntityCtrlEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final Log logger = LogFactory.getLog(EntityCtrlEdit.class);
    private static final String BAR_ADD_ROWS = "addrows";

    public void beforeBindData(EventObject evt) {
        super.beforeBindData(evt);
        DynamicObject entityType = (DynamicObject)this.getModel().getValue("entitytype");
        if (entityType == null) {
            return;
        }
        boolean dynOrVir = EntityCtrlServiceHelper.isDynOrVir((DynamicObject)entityType);
        if (!dynOrVir) {
            this.putMainOrgFieldProp(entityType);
        } else {
            this.putDynaFormCtrlInfo(entityType);
        }
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity");
        if (entryEntity.isEmpty()) {
            return;
        }
        Map entityFieldMap = EntityCtrlServiceHelper.getEntityFieldMap((DynamicObject)entityType);
        List noDBProps = (List)SerializationUtils.fromJsonString((String)this.getPageCache().get("noDBProps"), List.class);
        for (int index = 0; index < entryEntity.size(); ++index) {
            DynamicObject dy = (DynamicObject)entryEntity.get(index);
            String propKey = dy.getString("propkey");
            if (noDBProps.contains(propKey) || this.isQueryEntityProp(propKey)) {
                this.getView().setEnable(Boolean.FALSE, index, new String[]{"authrange"});
            }
            dy.set("propname", entityFieldMap.get(propKey));
            if (dy.getLong("id") == 0L) continue;
            this.getView().setEnable(Boolean.FALSE, index, new String[]{"propname", "dimension", "issyspreset"});
        }
        this.getModel().setDataChanged(false);
    }

    public void afterBindData(EventObject evt) {
        super.afterBindData(evt);
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity");
        List mustSelectedDim = entryEntity.stream().filter(it -> it.getBoolean("ismust")).map(it -> it.getLong("id")).collect(Collectors.toList());
        String mustSelectedDimStr = SerializationUtils.toJsonString(mustSelectedDim);
        this.getPageCache().put("changedMustDim", mustSelectedDimStr);
        this.getPageCache().put("existMustDim", mustSelectedDimStr);
        Map<String, Long> originPropDimInfo = this.getEntryInfo(entryEntity);
        this.getPageCache().put("originPropDimInfo", SerializationUtils.toJsonString(originPropDimInfo));
        List<EntityCtrlEntryRowModel> entryDetail = this.getEntryDetail(entryEntity);
        this.getPageCache().put("beforeOpData", JSONArray.toJSONString(entryDetail));
    }

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        Toolbar control = (Toolbar)this.getView().getControl("toolbarap");
        control.addItemClickListener((ItemClickListener)this);
        BasedataEdit fieldEdit = (BasedataEdit)this.getView().getControl("entitytype");
        fieldEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit dimension = (BasedataEdit)this.getView().getControl("dimension");
        dimension.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String name = evt.getProperty().getName();
        ListShowParameter lsp = (ListShowParameter)evt.getFormShowParameter();
        if (HRStringUtils.equals((String)"entitytype", (String)name)) {
            QFilter filter = this.bulidEntityFilters();
            lsp.getListFilterParameter().setFilter(filter);
            lsp.setFormId("bos_listf7");
        } else if (HRStringUtils.equals((String)"dimension", (String)name)) {
            DynamicObject entityType = (DynamicObject)this.getModel().getValue("entitytype");
            int rowIndex = this.getModel().getEntryCurrentRowIndex("entryentity");
            String propKey = this.getModel().getValue("propkey", rowIndex).toString();
            Map bdPropInfos = (Map)SerializationUtils.fromJsonString((String)this.getPageCache().get("bdPropInfos"), Map.class);
            String entityNum = entityType.getString("id");
            String orgInfoStr = this.getView().getPageCache().get("orgInfos");
            Map orgInfos = HRStringUtils.isNotEmpty((String)orgInfoStr) ? (Map)SerializationUtils.fromJsonString((String)orgInfoStr, Map.class) : null;
            QFilter otBdFilter = new QFilter("datasource", "=", (Object)"basedata").and("entitytype", "in", Arrays.asList("haos_adminorghrf7", "haos_adminorghrf7", "haos_projectteamhr", "haos_adminorgdetail"));
            QFilter filter = null;
            if (bdPropInfos.containsKey(propKey)) {
                if (null != orgInfos && orgInfos.containsKey(propKey)) {
                    Integer buId = (Integer)orgInfos.get(propKey);
                    filter = new QFilter("datasource", "=", (Object)"hrbu");
                    if (buId != null && buId != Integer.MAX_VALUE && buId != 0) {
                        if (buId == 1) {
                            Long[] orgClassifys = new Long[]{1010L, 1020L};
                            filter = new QFilter("datasource", "=", (Object)"orgteam").and("org_classify.fbasedataid", "in", (Object)orgClassifys);
                        } else {
                            filter.and("hrbu", "=", (Object)buId);
                        }
                    }
                } else {
                    List<Long> otclassifyIds = this.queryEntityPropOtclassifyIds(entityNum, propKey);
                    if (CollectionUtils.isNotEmpty(otclassifyIds)) {
                        filter = new QFilter("org_classify.fbasedataid", "in", otclassifyIds);
                        if (otclassifyIds.contains(1010L) || otclassifyIds.contains(1020L)) {
                            filter.or(otBdFilter);
                        }
                    } else {
                        filter = new QFilter("entitytype", "=", bdPropInfos.get(propKey));
                    }
                }
            } else if (!(HRStringUtils.equals((String)propKey, (String)"ID") || HRStringUtils.equals((String)propKey, (String)"boid") || propKey.endsWith(".id"))) {
                filter = new QFilter("datasource", "!=", (Object)"orgteam");
            }
            logger.info("EntityCtrl dimensionFilter = {}", (Object)(filter == null ? "{}" : filter.toString()));
            lsp.getListFilterParameter().setFilter(filter);
            lsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "dimensionCallBack"));
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private List<Long> queryEntityPropOtclassifyIds(String entity, String propKey) {
        List structProjects = (List)HRMServiceHelper.invokeHRMPService((String)"haos", (String)"IHAOSStructProjectService", (String)"queryStructProConfig", (Object[])new Object[]{entity, propKey, null});
        ArrayList<Long> otclassifyIds = new ArrayList<Long>(10);
        if (CollectionUtils.isEmpty((Collection)structProjects)) {
            return otclassifyIds;
        }
        Map structProjectMap = (Map)structProjects.get(0);
        if (null != structProjectMap.get("otclassify")) {
            otclassifyIds.addAll((Set)structProjectMap.get("otclassify"));
        } else {
            List structProjectMapList = (List)structProjectMap.get("structproject");
            structProjectMapList.stream().forEach(it -> otclassifyIds.add((Long)it.get("otclassify")));
        }
        return otclassifyIds;
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        String operateKey = formOperate.getOperateKey();
        DynamicObjectCollection collection = this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity");
        if (operateKey.equals("save")) {
            if (collection.isEmpty()) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u7ef4\u5ea6\u6620\u5c04\u6570\u636e\u3002", (String)"EntityCtrlEdit_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            Map<String, Long> propDimInfo = this.getEntryInfo(collection);
            formOperate.getOption().setVariableValue("propDimInfo", SerializationUtils.toJsonString(propDimInfo));
            formOperate.getOption().setVariableValue("originPropDimInfo", this.getPageCache().get("originPropDimInfo"));
            formOperate.getOption().setVariableValue("tag_of_view", Boolean.TRUE.toString());
        } else if (operateKey.equals("deleteentry")) {
            OperationStatus status;
            EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity");
            int[] selectRows = entryGrid.getSelectRows();
            if (collection.size() > 0 && selectRows.length > 0) {
                for (int selectRow : selectRows) {
                    boolean isSysPreSet = ((DynamicObject)collection.get(selectRow)).getBoolean("issyspreset");
                    if (!isSysPreSet) continue;
                    this.getView().showErrorNotification(ResManager.loadKDString((String)"\u9884\u7f6e\u6570\u636e\u65e0\u6cd5\u5220\u9664\u3002", (String)"EntityCtrlEdit_5", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    args.setCancel(true);
                    return;
                }
            }
            if (OperationStatus.EDIT.equals((Object)(status = this.getView().getFormShowParameter().getStatus()))) {
                this.getView().showConfirm(ResManager.loadKDString((String)"\u5220\u9664\u540e\uff0c\u5173\u8054\u89d2\u8272\u4e0b\u7684\u4e1a\u52a1\u5bf9\u8c61\u5c06\u4e0d\u518d\u53c2\u4e0e\u63a7\u6743\uff0c\u5220\u9664\u7ef4\u5ea6\u63a7\u6743\u5c06\u5f71\u54cd\u89d2\u8272\u6240\u6709\u6210\u5458\uff0c\u786e\u8ba4\u5220\u9664\u5417\uff1f", (String)"EntityCtrlEdit_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.YesNo, new ConfirmCallBackListener("delete_confirm"));
                args.setCancel(true);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        String operateKey = args.getOperateKey();
        OperationResult operationResult = args.getOperationResult();
        if (operateKey.equals("save") && operationResult.isSuccess()) {
            HRPermCacheMgr.clearAllCache();
        }
    }

    private List<EntityCtrlEntryRowModel> getEntryDetail(DynamicObjectCollection collection) {
        ArrayList entryDetails = Lists.newArrayListWithExpectedSize((int)collection.size());
        for (DynamicObject row : collection) {
            EntityCtrlEntryRowModel entityCtrlEntryRowModel = new EntityCtrlEntryRowModel(row.getString("propkey"), row.getString("dimension.name"), row.getString("authrange"), row.getBoolean("ismust"));
            entryDetails.add(entityCtrlEntryRowModel);
        }
        return entryDetails;
    }

    private Map<String, Long> getEntryInfo(DynamicObjectCollection collection) {
        HashMap<String, Long> propDimInfo = new HashMap<String, Long>(collection.size());
        collection.forEach(it -> propDimInfo.put(it.getString("propkey"), it.getLong("dimension.id")));
        return propDimInfo;
    }

    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        String itemKey = evt.getItemKey();
        if (itemKey.equals(BAR_ADD_ROWS)) {
            DynamicObject entityType = (DynamicObject)this.getModel().getValue("entitytype");
            if (entityType == null) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e1a\u52a1\u5bf9\u8c61\u3002", (String)"EntityCtrlEdit_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            } else {
                String number = entityType.getString("number");
                HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("bos_objecttype");
                QFilter filter = new QFilter("number", "=", (Object)number);
                boolean exists = serviceHelper.isExists(new QFilter[]{filter});
                if (exists) {
                    this.showFieldForm(entityType);
                } else {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u5b9e\u4f53\u6ca1\u6709\u5c5e\u6027\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u3002", (String)"EntityCtrlEdit_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                }
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs evt) {
        String name = evt.getProperty().getName();
        DynamicObject dy = evt.getChangeSet()[0].getDataEntity();
        if (HRStringUtils.equals((String)"entitytype", (String)name)) {
            this.getModel().deleteEntryData("entryentity");
            DynamicObject entityType = dy.getDynamicObject("entitytype");
            if (null == entityType) {
                this.getModel().setValue("bizapp", (Object)"");
                return;
            }
            this.bindAppCloud(entityType);
            boolean dynOrVir = EntityCtrlServiceHelper.isDynOrVir((DynamicObject)entityType);
            if (!dynOrVir) {
                this.putMainOrgFieldProp(entityType);
            } else {
                this.putDynaFormCtrlInfo(entityType);
            }
        } else if (HRStringUtils.equals((String)"ismust", (String)name)) {
            List changedMustDim = (List)SerializationUtils.fromJsonString((String)this.getPageCache().get("changedMustDim"), List.class);
            Boolean newValue = (Boolean)evt.getChangeSet()[0].getNewValue();
            if (dy.getLong("id") != 0L) {
                if (newValue.booleanValue()) {
                    changedMustDim.add(dy.getLong("id"));
                } else {
                    changedMustDim.remove(dy.getLong("id"));
                }
                this.getPageCache().put("changedMustDim", SerializationUtils.toJsonString((Object)changedMustDim));
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void putDynaFormCtrlInfo(DynamicObject entityType) {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_dynaformctrl");
        DynamicObject entity = serviceHelper.queryOne("entryentity.propkey,entryentity.bdtype,entryentity.bucafunc", new QFilter[]{new QFilter("entitytype", "=", (Object)entityType.getString("number"))});
        if (null == entity) {
            return;
        }
        DynamicObjectCollection collection = entity.getDynamicObjectCollection("entryentity");
        HashMap<String, String> bdPropInfos = new HashMap<String, String>(16);
        HashMap<String, Integer> orgInfos = new HashMap<String, Integer>(16);
        for (DynamicObject row : collection) {
            String bdType = row.getString("bdtype.number");
            String propKey = row.getString("propkey");
            if (!HRStringUtils.isNotEmpty((String)bdType)) continue;
            bdPropInfos.put(propKey, bdType);
            if (!HRStringUtils.equals((String)"bos_org", (String)bdType)) continue;
            orgInfos.put(propKey, row.getInt("bucafunc.id"));
        }
        this.getView().getPageCache().put("bdPropInfos", SerializationUtils.toJsonString(bdPropInfos));
        this.getPageCache().put("orgInfos", SerializationUtils.toJsonString(orgInfos));
        this.getView().getPageCache().put("noDBProps", SerializationUtils.toJsonString((Object)Lists.newArrayListWithExpectedSize((int)0)));
    }

    private void putMainOrgFieldProp(DynamicObject entityType) {
        Map orgInfos = new EntityOrgFieldBuQueryService().getPropBuMap(entityType.getString("id"));
        MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entityType.getString("id"));
        Map allFields = mainEntityType.getAllFields();
        if (mainEntityType instanceof QueryEntityType) {
            QueryEntityType mainEntity = (QueryEntityType)mainEntityType;
            List list = mainEntity.getAllJoinEntityType();
            for (MainEntityType type : list) {
                String joinEntityName = type.getName();
                Map tempAllFields = type.getAllFields();
                tempAllFields.forEach((key, value) -> allFields.put(joinEntityName + "." + key, value));
            }
        } else {
            this.getPageCache().put("mainorgfield", mainEntityType.getMainOrg());
        }
        for (Map.Entry entry : allFields.entrySet()) {
            String orgFunc;
            if (!(entry.getValue() instanceof OrgProp) || (orgFunc = ((OrgProp)entry.getValue()).getOrgFunc()) == null) continue;
            orgInfos.put(entry.getKey(), Integer.valueOf(orgFunc));
        }
        this.getView().getPageCache().put("orgInfos", SerializationUtils.toJsonString((Object)orgInfos));
        HashMap<String, String> queryCondition = new HashMap<String, String>(2);
        queryCondition.put("param_ifShowBaseDataProp", "true");
        queryCondition.put("param_ifShowAllBaseDataProp", "true");
        queryCondition.put("param_ifShowSonEntityProp", Boolean.TRUE.toString());
        ChoiceFieldPageCustomQueryService choiceFieldPageCustomQueryService = new ChoiceFieldPageCustomQueryService();
        ArrayList noDBProps = new ArrayList(10);
        List maps = choiceFieldPageCustomQueryService.parsePropertySub((IDataEntityType)mainEntityType, null, queryCondition, "1=1", noDBProps);
        HashMap<String, String> bdPropInfos = new HashMap<String, String>(maps.size());
        for (Map map : maps) {
            String fieldEntityNum = (String)map.get("field_entityNum");
            String fieldId = (String)map.get("field_id");
            if (!HRStringUtils.isNotEmpty((String)fieldEntityNum) || !HRStringUtils.isNotEmpty((String)fieldId)) continue;
            bdPropInfos.put(fieldId, fieldEntityNum);
        }
        this.getView().getPageCache().put("bdPropInfos", SerializationUtils.toJsonString(bdPropInfos));
        this.getView().getPageCache().put("noDBProps", SerializationUtils.toJsonString(noDBProps));
    }

    private void showFieldForm(DynamicObject entityType) {
        HRBaseServiceHelper serviceHelper;
        boolean exists;
        boolean dynOrVir = EntityCtrlServiceHelper.isDynOrVir((DynamicObject)entityType);
        if (dynOrVir && !(exists = (serviceHelper = new HRBaseServiceHelper("hrcs_dynaformctrl")).isExists(new QFilter("entitytype", "=", (Object)entityType.getString("id"))))) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5728\u201c\u865a\u5b57\u6bb5\u6570\u636e\u63a7\u6743\u914d\u7f6e\u201d\u4e2d\uff0c\u6dfb\u52a0\u201c%s\u201d\u7684\u76f8\u5173\u5c5e\u6027\u4fe1\u606f\u3002", (String)"EntityCtrlEdit_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{entityType.getString("name")}));
            return;
        }
        HashSet<String> idSet = new HashSet<String>();
        int listRowCount = this.getModel().getEntryRowCount("entryentity");
        for (int index = 0; index < listRowCount; ++index) {
            String id = (String)this.getModel().getValue("propkey", index);
            String name = (String)this.getModel().getValue("propname", index);
            ListSelectedRow listSelectedRow = new ListSelectedRow((Object)(id + "||" + name), Boolean.FALSE);
            listSelectedRow.setName(name);
            listSelectedRow.setNumber(id);
            if (!StringUtils.isNotEmpty((String)id)) continue;
            idSet.add(id);
        }
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"hrcs_choosefield_page", (boolean)false, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "perm_choosefieldpage"));
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setCustomParam("paramEntityName", (Object)entityType.getString("number"));
        fsp.setCustomParam("isDynamicFormType", (Object)dynOrVir);
        fsp.setCustomParam("paramAllReadySelectedPropertyNameSet", idSet);
        fsp.setCustomParam("param_ifShowSonEntityProp", (Object)Boolean.TRUE.toString());
        String mainOrgField = this.getPageCache().get("mainorgfield");
        if (mainOrgField != null) {
            fsp.setCustomParam("mainorgfield", (Object)mainOrgField);
        }
        fsp.setHasRight(true);
        this.getView().showForm((FormShowParameter)fsp);
    }

    @ExcludeFromJacocoGeneratedReport
    public void closedCallBack(ClosedCallBackEvent evt) {
        String actionId = evt.getActionId();
        Object returnData = evt.getReturnData();
        if (returnData == null) {
            return;
        }
        if ("perm_choosefieldpage".equals(actionId)) {
            ListSelectedRowCollection res = (ListSelectedRowCollection)returnData;
            if (res.size() <= 0) {
                return;
            }
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity");
            Set keys = entryEntity.stream().map(entry -> entry.getString("propkey")).collect(Collectors.toSet());
            Iterator iterator = res.iterator();
            int idx = this.getModel().getEntryRowCount("entryentity");
            while (iterator.hasNext()) {
                List noDBProps;
                ListSelectedRow next = (ListSelectedRow)iterator.next();
                String id2Name = next.getPrimaryKeyValue().toString();
                String[] infos = id2Name.split("\\|\\|");
                String fieldNum = infos[0];
                if (!keys.contains(fieldNum)) {
                    this.getModel().createNewEntryRow("entryentity");
                    this.getModel().setValue("propkey", (Object)infos[0], idx);
                    this.getModel().setValue("propname", (Object)infos[1], idx);
                }
                if (!(noDBProps = (List)SerializationUtils.fromJsonString((String)this.getPageCache().get("noDBProps"), List.class)).contains(infos[0]) && !this.isQueryEntityProp(fieldNum)) continue;
                this.getModel().setValue("authrange", (Object)"2", idx);
                this.getView().setEnable(Boolean.FALSE, idx, new String[]{"authrange"});
            }
        } else if ("dimensionCallBack".equals(actionId)) {
            int rowIndex = this.getModel().getEntryCurrentRowIndex("entryentity");
            ListSelectedRowCollection res = (ListSelectedRowCollection)returnData;
            if (res.size() <= 0) {
                return;
            }
            Long dimensionId = (Long)res.get(0).getPrimaryKeyValue();
            this.getModel().setValue("dimension", (Object)dimensionId, rowIndex);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        super.confirmCallBack(messageBoxClosedEvent);
        MessageBoxResult result = messageBoxClosedEvent.getResult();
        if (messageBoxClosedEvent.getCallBackId().equals("delete_confirm") && result.equals((Object)MessageBoxResult.Yes)) {
            EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity");
            int[] selectRows = entryGrid.getSelectRows();
            this.getModel().deleteEntryRows("entryentity", selectRows);
        }
    }

    private QFilter bulidEntityFilters() {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_entityctrl");
        DynamicObject[] entityCtrls = serviceHelper.query("entitytype", null);
        List notInNumbers = Arrays.stream(entityCtrls).filter(dy -> dy.get("entitytype.bizappid") != null).map(entity -> entity.getString("entitytype.number")).collect(Collectors.toList());
        notInNumbers.add("bos_org");
        notInNumbers.add("hbss_hrbu");
        HashSet forBidEntity = new HashSet(16);
        EntityCtrlServiceHelper.queryEntityForBidInfo(forBidEntity, null, null);
        notInNumbers.addAll(forBidEntity);
        Set noCtrlPermEntitys = RoleManageService.getNoCtrlPermEntitysFromCache();
        notInNumbers.addAll(noCtrlPermEntitys);
        Set validApps = EntityCtrlServiceHelper.getHRApps((boolean)false);
        Set allHrHasPermItemEntitys = EntityCtrlServiceHelper.getAllHrHasPermItemEntity((IFormView)this.getView(), (Set)validApps);
        allHrHasPermItemEntitys.removeAll(notInNumbers);
        QFilter inEnityFilter = new QFilter("id", "in", (Object)allHrHasPermItemEntitys);
        QFilter modelTypeFilter = new QFilter("modeltype", "in", Arrays.asList("BaseFormModel", "BillFormModel", "QueryListModel", "DynamicFormModel", "ReportFormModel"));
        QFilter noTemplateFilter = new QFilter("istemplate", "=", (Object)"0");
        return modelTypeFilter.and(noTemplateFilter).and(inEnityFilter);
    }

    private void bindAppCloud(DynamicObject entityType) {
        DynamicObject app = entityType.getDynamicObject("bizappid");
        this.getModel().setValue("bizapp", (Object)app.getString("id"));
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean isQueryEntityProp(String fieldNum) {
        boolean isQueryEntityProp = false;
        String[] split = fieldNum.split("\\.");
        if (split.length > 2) {
            try {
                EntityMetadataCache.getDataEntityType((String)split[1]);
                isQueryEntityProp = true;
            }
            catch (KDException kDException) {
                // empty catch block
            }
        }
        return isQueryEntityProp;
    }
}
