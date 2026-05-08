/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.common.collect.Sets$SetView
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.QueryEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.Button
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.field.TextEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.service.perm.dimension.PermRtSyncService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.HRBuCaServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dimension.PermRelateServiceHelper
 *  kd.hr.hrcs.common.model.RelatePermInfo
 */
package kd.hr.hrcs.formplugin.web.perm.dimension;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.QueryEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.control.Button;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.TextEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.service.perm.dimension.PermRtSyncService;
import kd.hr.hrcs.bussiness.servicehelper.perm.HRBuCaServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.dimension.PermRelateServiceHelper;
import kd.hr.hrcs.common.model.RelatePermInfo;

public final class PermRelateEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener,
ClickListener {
    private static final Log LOGGER = LogFactory.getLog(PermRelateEdit.class);
    private static final String FIELD_ENTRY_ENTITY_TYPE = "entitytypeid";
    private static final String FIELD_PERM_ITEM = "mainpermitem";
    private static final String FIELD_ENTRY_PERM_ITEM_ID = "permitemid";
    private static final String FIELD_ENTRY_PERM_ITEM = "permitem";
    private final HRBaseServiceHelper permRelateHelper = new HRBaseServiceHelper("hrcs_permrelat");

    public void registerListener(EventObject e) {
        super.registerListener(e);
        Toolbar control = (Toolbar)this.getView().getControl("toolbarap");
        control.addItemClickListener((ItemClickListener)this);
        BasedataEdit entityEdit = (BasedataEdit)this.getView().getControl("entitytype");
        entityEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit entryEntityEdit = (BasedataEdit)this.getView().getControl(FIELD_ENTRY_ENTITY_TYPE);
        entryEntityEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit appEdit = (BasedataEdit)this.getView().getControl("app");
        appEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        TextEdit textEdit = (TextEdit)this.getView().getControl(FIELD_ENTRY_PERM_ITEM);
        textEdit.addClickListener((ClickListener)this);
        Button syncRole = (Button)this.getControl("btnsave");
        if (syncRole != null) {
            syncRole.addClickListener((ClickListener)this);
        }
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        DynamicObject entity = (DynamicObject)this.getModel().getValue("entitytype");
        EntityCtrlServiceHelper.queryExistedForBidInfo((IFormView)this.getView());
        this.putAllBuInfoToCache();
        if (entity != null) {
            DynamicObject app = this.getModel().getDataEntity().getDynamicObject("bizapp");
            if (null != app) {
                boolean flag = this.setAppComboList(entity);
                if (!flag) {
                    return;
                }
                this.getModel().setValue("appcombo", (Object)app.getString("id"));
                this.setMainPermItems(entity, app, false);
            }
            DynamicObjectCollection dys = this.getModel().getEntryEntity("entryentity");
            for (int idx = 0; idx < dys.size(); ++idx) {
                DynamicObject dy = (DynamicObject)dys.get(idx);
                DynamicObject entryEntity = dy.getDynamicObject(FIELD_ENTRY_ENTITY_TYPE);
                boolean isSys = dy.getBoolean("issyspreset");
                if (entryEntity != null) {
                    Map permItemsMap = EntityCtrlServiceHelper.queryEntityPermItems((DynamicObject)entryEntity);
                    String[] permIds = dy.getString(FIELD_ENTRY_PERM_ITEM_ID).split(",");
                    StringBuilder sb = new StringBuilder();
                    for (String permId : permIds) {
                        sb.append((String)permItemsMap.get(permId));
                        sb.append(',');
                    }
                    this.getModel().setValue(FIELD_ENTRY_PERM_ITEM, (Object)sb.substring(0, sb.toString().lastIndexOf(44)), idx);
                }
                if (!isSys) continue;
                this.getView().setEnable(Boolean.FALSE, idx, new String[]{FIELD_ENTRY_ENTITY_TYPE, "app", FIELD_ENTRY_PERM_ITEM});
            }
            this.getModel().setDataChanged(false);
        }
        DynamicObjectCollection entry = this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity");
        List ids = entry.stream().map(it -> it.getLong("id")).collect(Collectors.toList());
        this.getPageCache().put("ids", SerializationUtils.toJsonString(ids));
        Set<String> originEntryInfo = this.getEntryInfo();
        this.getPageCache().put("originEntryInfo", SerializationUtils.toJsonString(originEntryInfo));
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs evt) {
        block4: {
            int[] selectRows;
            DynamicObjectCollection entry;
            FormOperate source;
            block3: {
                super.beforeDoOperation(evt);
                source = (FormOperate)evt.getSource();
                entry = this.getModel().getEntryEntity("entryentity");
                if (!StringUtils.equals((CharSequence)"save", (CharSequence)source.getOperateKey())) break block3;
                entry.removeIf(it -> it.getDynamicObject(FIELD_ENTRY_ENTITY_TYPE) == null);
                DynamicObject entity = this.getModel().getDataEntity().getDynamicObject("entitytype");
                if (null == entity) {
                    return;
                }
                String mainBuId = this.getMainBu();
                Map allBuInfo = (Map)SerializationUtils.fromJsonString((String)this.getPageCache().get("allBuInfo"), Map.class);
                for (DynamicObject dy : entry) {
                    String buId = this.getBu(dy.getString("entitytypeid.id"), dy.getString("app.id"));
                    if (HRStringUtils.equals((String)mainBuId, (String)buId)) continue;
                    this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u5f53\u524d\u4e3b\u4e1a\u52a1\u5bf9\u8c61\u7684\u804c\u80fd\u7c7b\u578b\u4e3a\u201c%1$s\u201d\uff0c\u4e0d\u5141\u8bb8\u5173\u8054\u804c\u80fd\u7c7b\u578b\u4e3a\u975e\u201c%1$s\u201d\u7684\u4e1a\u52a1\u5bf9\u8c61\u3002", (String)"PermRelateEdit_02", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), allBuInfo.get(mainBuId)));
                    evt.setCancel(true);
                }
                break block4;
            }
            if (!StringUtils.equals((CharSequence)"deleteentry", (CharSequence)source.getOperateKey())) break block4;
            EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity");
            for (int selectRow : selectRows = entryGrid.getSelectRows()) {
                if (!((DynamicObject)entry.get(selectRow)).getBoolean("issyspreset")) continue;
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u9884\u7f6e\u6570\u636e\u65e0\u6cd5\u5220\u9664\u3002", (String)"PermRelateEdit_05", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                evt.setCancel(true);
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String name = evt.getProperty().getName();
        ListShowParameter lsp = (ListShowParameter)evt.getFormShowParameter();
        QFilter f7Filter = EntityCtrlServiceHelper.buildFilterForF7((boolean)false);
        IFormView view = this.getView();
        QFilter noPermEntityFilter = EntityCtrlServiceHelper.filterNoPermEntity((IFormView)view);
        QFilter F7CommonFilter = f7Filter.and(noPermEntityFilter);
        switch (name) {
            case "entitytype": {
                lsp.getListFilterParameter().setFilter(F7CommonFilter);
                break;
            }
            case "entitytypeid": {
                DynamicObjectCollection entry = this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity");
                List ids = entry.stream().filter(dy -> dy.getString("entitytypeid.id") != null).map(dy -> dy.getString("entitytypeid.id")).collect(Collectors.toList());
                if (ids.size() > 0) {
                    F7CommonFilter = F7CommonFilter.and(new QFilter("number", "not in", ids));
                }
                lsp.getListFilterParameter().setFilter(F7CommonFilter);
                break;
            }
            case "app": {
                int row = evt.getRow();
                DynamicObjectCollection entry = this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity");
                DynamicObject entity = ((DynamicObject)entry.get(row)).getDynamicObject(FIELD_ENTRY_ENTITY_TYPE);
                if (null == entity) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e1a\u52a1\u5bf9\u8c61\u3002", (String)"PermRelateEdit_03", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{null}));
                    evt.setCancel(true);
                    return;
                }
                Set entityRelatedApps = EntityCtrlServiceHelper.getEntityRelatedApps((DynamicObject)entity);
                Set<String> forBidApps = this.getForBidApp();
                this.removeForBidApp(entityRelatedApps, forBidApps, entity.getString("number"));
                if (entityRelatedApps.size() == 0) {
                    this.getView().setEnable(Boolean.FALSE, row, new String[]{"app"});
                    this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5f53\u524d\u4e1a\u52a1\u5bf9\u8c61\u7684\u5e94\u7528\u5df2\u8bbe\u7f6e\u4e3a\u4e0d\u5141\u8bb8\u6388\u6743\uff0c\u4e0d\u5141\u8bb8\u8bbe\u7f6e\u5173\u8054\u6743\u9650\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PermRelateEdit_08", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                }
                QFilter filter = new QFilter("id", "in", (Object)entityRelatedApps);
                lsp.getListFilterParameter().setFilter(filter);
                break;
            }
        }
        lsp.setFormId("bos_listf7");
    }

    private void removeForBidApp(Set<String> entityRelatedApps, Set<String> forBidApps, String entity) {
        Map<String, List<String>> forBidAppEntity = this.getForBidAppEntity();
        HashSet<String> toDeleteApps = new HashSet<String>(forBidAppEntity.size());
        for (String entityRelatedApp : entityRelatedApps) {
            if (!forBidApps.contains(entityRelatedApp) && (forBidAppEntity.get(entityRelatedApp) == null || !Sets.newHashSet((Iterable)forBidAppEntity.get(entityRelatedApp)).contains(entity))) continue;
            toDeleteApps.add(entityRelatedApp);
        }
        entityRelatedApps.removeAll(toDeleteApps);
    }

    public void propertyChanged(PropertyChangedArgs evt) {
        String propName = evt.getProperty().getName();
        if (HRStringUtils.equals((String)propName, (String)"entitytype")) {
            DynamicObject oldEntity = (DynamicObject)evt.getChangeSet()[0].getOldValue();
            if (oldEntity != null && (HRStringUtils.isNotEmpty((String)this.getModelValStr(FIELD_PERM_ITEM)) || this.getModel().getEntryRowCount("entryentity") > 0)) {
                this.getPageCache().put("oldEntity", oldEntity.getString("id"));
                this.getView().showConfirm(ResManager.loadKDString((String)"\u786e\u5b9a\u8981\u6e05\u9664\u4e3b\u6743\u9650\u9879\u548c\u5173\u8054\u4fe1\u606f\u5417\uff1f", (String)"PermRelateEdit_09", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, new ConfirmCallBackListener("mainEntityChangeConfirm", (IFormPlugin)this));
                return;
            }
            this.afterEntityChange();
        } else if (HRStringUtils.equals((String)propName, (String)"appcombo")) {
            String appId = (String)evt.getChangeSet()[0].getNewValue();
            this.getModel().setValue("bizapp", (Object)appId);
            DynamicObject dataEntity = this.getModel().getDataEntity();
            DynamicObject entity = dataEntity.getDynamicObject("entitytype");
            DynamicObject app = dataEntity.getDynamicObject("bizapp");
            this.setMainPermItems(entity, app, true);
        } else if (HRStringUtils.equals((String)propName, (String)FIELD_ENTRY_ENTITY_TYPE)) {
            DynamicObject newValue = (DynamicObject)evt.getChangeSet()[0].getNewValue();
            int rowIndex = evt.getChangeSet()[0].getRowIndex();
            this.getModel().setValue("app", null, rowIndex);
            if (null == newValue) {
                this.getModel().setValue(FIELD_ENTRY_ENTITY_TYPE, null, rowIndex);
                return;
            }
            Set entityRelatedApps = EntityCtrlServiceHelper.getEntityRelatedApps((DynamicObject)newValue);
            Set<String> forBidApps = this.getForBidApp();
            this.removeForBidApp(entityRelatedApps, forBidApps, newValue.getString("number"));
            if (entityRelatedApps.size() == 0) {
                this.getView().setEnable(Boolean.FALSE, rowIndex, new String[]{"app"});
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5f53\u524d\u4e1a\u52a1\u5bf9\u8c61\u7684\u5e94\u7528\u5df2\u8bbe\u7f6e\u4e3a\u4e0d\u5141\u8bb8\u6388\u6743\uff0c\u4e0d\u5141\u8bb8\u8bbe\u7f6e\u5173\u8054\u6743\u9650\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PermRelateEdit_08", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return;
            }
            if (entityRelatedApps.size() == 1) {
                this.getModel().setValue("app", entityRelatedApps.iterator().next(), rowIndex);
                this.getView().setEnable(Boolean.FALSE, rowIndex, new String[]{"app"});
            }
        } else if (HRStringUtils.equals((String)propName, (String)FIELD_ENTRY_PERM_ITEM)) {
            String newValue = (String)evt.getChangeSet()[0].getNewValue();
            int rowIndex = evt.getChangeSet()[0].getRowIndex();
            if (HRStringUtils.isEmpty((String)newValue)) {
                this.getModel().setValue(FIELD_ENTRY_PERM_ITEM_ID, (Object)"", rowIndex);
            }
        } else if (HRStringUtils.equals((String)propName, (String)"app")) {
            String buId;
            DynamicObject app = (DynamicObject)evt.getChangeSet()[0].getNewValue();
            int rowIndex = evt.getChangeSet()[0].getRowIndex();
            DynamicObject row = (DynamicObject)this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity").get(rowIndex);
            DynamicObject entity = row.getDynamicObject(FIELD_ENTRY_ENTITY_TYPE);
            if (null == app || null == entity) {
                return;
            }
            String entityId = entity.getString("id");
            String appId = app.getString("id");
            String mainBu = this.getMainBu();
            if (!HRStringUtils.equals((String)mainBu, (String)(buId = this.getBu(entityId, appId)))) {
                Map allBuInfo = (Map)SerializationUtils.fromJsonString((String)this.getPageCache().get("allBuInfo"), Map.class);
                this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u5f53\u524d\u4e3b\u4e1a\u52a1\u5bf9\u8c61\u7684\u804c\u80fd\u7c7b\u578b\u4e3a\u201c%1$s\u201d\uff0c\u4e0d\u5141\u8bb8\u5173\u8054\u804c\u80fd\u7c7b\u578b\u4e3a\u975e\u201c%1$s\u201d\u7684\u4e1a\u52a1\u5bf9\u8c61\u3002", (String)"PermRelateEdit_02", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), allBuInfo.get(mainBu)));
                this.getModel().setValue(FIELD_ENTRY_ENTITY_TYPE, (Object)"", rowIndex);
            }
        }
    }

    private void afterEntityChange() {
        this.getModel().setValue(FIELD_PERM_ITEM, (Object)"");
        this.getModel().deleteEntryData("entryentity");
        DynamicObject entity = this.getModel().getDataEntity().getDynamicObject("entitytype");
        boolean noApp = this.setAppComboList(entity);
        if (!noApp) {
            return;
        }
        DynamicObject app = this.getModel().getDataEntity().getDynamicObject("bizapp");
        this.setMainPermItems(entity, app, true);
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        String callBackId = evt.getCallBackId();
        MessageBoxResult result = evt.getResult();
        if (HRStringUtils.equals((String)callBackId, (String)"mainEntityChangeConfirm")) {
            if (HRStringUtils.equals((String)result.name(), (String)MessageBoxResult.Yes.name())) {
                this.afterEntityChange();
            } else {
                String oldEntity = this.getPageCache().get("oldEntity");
                this.getModel().beginInit();
                this.getModel().setValue("entitytype", (Object)oldEntity);
                this.getModel().endInit();
                this.getView().updateView("entitytype");
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        String actionId = evt.getActionId();
        ListSelectedRowCollection returnData = (ListSelectedRowCollection)evt.getReturnData();
        if (returnData == null) {
            return;
        }
        if (returnData.size() == 0) {
            return;
        }
        if ("hrcs_choose_permitem".equals(actionId)) {
            StringBuilder permItemIds = new StringBuilder();
            StringBuilder permItemNames = new StringBuilder();
            for (ListSelectedRow row : returnData) {
                String pk = row.getPrimaryKeyValue().toString();
                int index = pk.indexOf("||");
                String permId = pk.substring(0, index);
                String permName = pk.substring(index + 2);
                permItemIds.append(permId).append(',');
                permItemNames.append(permName).append(',');
            }
            String ids = permItemIds.substring(0, permItemIds.toString().length() - 1);
            String names = permItemNames.substring(0, permItemNames.toString().length() - 1);
            if (ids.length() > 0 && names.length() > 0) {
                EntryGrid en = (EntryGrid)this.getView().getControl("entryentity");
                int selectRow = en.getSelectRows()[0];
                this.getModel().setValue(FIELD_ENTRY_PERM_ITEM_ID, (Object)ids, selectRow);
                this.getModel().setValue(FIELD_ENTRY_PERM_ITEM, (Object)names, selectRow);
            }
        }
    }

    private String getMainBu() {
        DynamicObject dynamicObject = this.getModel().getDataEntity().getDynamicObject("entitytype");
        DynamicObject app = this.getModel().getDataEntity().getDynamicObject("bizapp");
        String mainEntityId = dynamicObject.getString("id");
        String appId = app.getString("id");
        return this.getBu(mainEntityId, appId);
    }

    private String getBu(String mainEntityId, String appId) {
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)mainEntityId);
        if (dataEntityType instanceof QueryEntityType) {
            QueryEntityType entityType = (QueryEntityType)dataEntityType;
            mainEntityId = entityType.getMainEntityType().getName();
        }
        return HRBuCaServiceHelper.getBuCaFuncFromSpec((String)mainEntityId, (String)appId);
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        DynamicObject entity;
        String itemKey = evt.getItemKey();
        if (HRStringUtils.equals((String)itemKey, (String)"addrows") && null == (entity = this.getModel().getDataEntity().getDynamicObject("entitytype"))) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e3b\u4e1a\u52a1\u5bf9\u8c61\u3002", (String)"PermRelateEdit_04", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            evt.setCancel(true);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        String operateKey = args.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"save") && args.getOperationResult().isSuccess()) {
            String idsStr = this.getPageCache().get("ids");
            List ids = (List)SerializationUtils.fromJsonString((String)idsStr, List.class);
            DynamicObject dataEntity = this.getModel().getDataEntity(true);
            DynamicObjectCollection entry = dataEntity.getDynamicObjectCollection("entryentity");
            long count = entry.stream().filter(it -> !ids.contains(it.getLong("id"))).count();
            if (count > 0L) {
                String entityNum = dataEntity.getDynamicObject("entitytype").getString("id");
                String permNum = dataEntity.getString(FIELD_PERM_ITEM);
                HashMap<String, String> maps = new HashMap<String, String>(2);
                maps.put("entityNum", entityNum);
                maps.put("permNum", permNum);
                this.getView().setReturnData(maps);
                this.getView().returnDataToParent(maps);
            }
            this.afterSaveProcessing();
        }
        if ((HRStringUtils.equals((String)operateKey, (String)"save") || HRStringUtils.equals((String)operateKey, (String)"modify")) && args.getOperationResult().isSuccess()) {
            HashMap<String, String> returnData = (HashMap<String, String>)this.getView().getReturnData();
            if (returnData == null) {
                returnData = new HashMap<String, String>(1);
            }
            returnData.put("changed", "changed");
            this.getView().setReturnData(returnData);
            this.getView().returnDataToParent(returnData);
        }
        if (HRStringUtils.equals((String)operateKey, (String)"save")) {
            String bizApp = (String)this.getView().getModel().getValue("appcombo");
            String mainPerm = (String)this.getView().getModel().getValue(FIELD_PERM_ITEM);
            ArrayList mainPermInfoList = Lists.newArrayListWithExpectedSize((int)1);
            ArrayList relatePermInfoList = Lists.newArrayListWithExpectedSize((int)16);
            DynamicObject entity = this.getModel().getDataEntity().getDynamicObject("entitytype");
            mainPermInfoList.add(new RelatePermInfo(null, null, null, entity.getString("number"), bizApp, mainPerm));
            QFilter qFilter = null;
            DynamicObject dataEntity = this.getModel().getDataEntity(true);
            DynamicObjectCollection entry = dataEntity.getDynamicObjectCollection("entryentity");
            for (DynamicObject itm : entry) {
                String perm = itm.getString(FIELD_ENTRY_PERM_ITEM_ID);
                if (perm.contains(",")) {
                    String[] split;
                    for (String permStr : split = perm.split(",")) {
                        RelatePermInfo relatePermInfo = new RelatePermInfo(itm.getString("entitytypeid.number"), itm.getString("app.id"), permStr.trim(), entity.getString("number"), bizApp, mainPerm);
                        relatePermInfoList.add(relatePermInfo);
                    }
                } else {
                    RelatePermInfo relatePermInfo = new RelatePermInfo(itm.getString("entitytypeid.number"), itm.getString("app.id"), perm.trim(), entity.getString("number"), bizApp, mainPerm);
                    relatePermInfoList.add(relatePermInfo);
                }
                String[] permItemNumerArr = perm.split(",");
                List<String> permItemNumerList = Arrays.asList(permItemNumerArr);
                QFilter singleQFilter = new QFilter("app.id", "=", (Object)itm.getString("app.id")).and(new QFilter("entitytype.number", "=", (Object)itm.getString("entitytypeid.number"))).and(new QFilter("permitem.number", "in", permItemNumerList));
                if (qFilter == null) {
                    qFilter = singleQFilter;
                    continue;
                }
                qFilter.or(singleQFilter);
            }
            List<RelatePermInfo> alonePermsList = this.getRelatePermInfos(qFilter);
            if (alonePermsList.size() > 0) {
                alonePermsList.removeIf(it -> it.getAppId() == null || it.getPermId() == null);
                relatePermInfoList.removeIf(it -> alonePermsList.stream().anyMatch(elm -> elm.getPermId().equals(it.getPermId()) && elm.getAppId().equals(it.getAppId()) && elm.getEntityNum().equals(it.getEntityNum())));
            }
            if (relatePermInfoList.size() == 0) {
                return;
            }
            Map permInfoMap = PermRelateServiceHelper.getPermInfo();
            relatePermInfoList.removeIf(it -> permInfoMap.get(it.getPermId()) == null);
            long start = System.currentTimeMillis();
            LinkedHashMap resultRolePermMap = PermRtSyncService.calcRtPermRole((List)relatePermInfoList, (List)mainPermInfoList);
            LOGGER.info("PermRelateEdit.afterDoOperation calcRtPermRole cost time:{}", (Object)(System.currentTimeMillis() - start));
            if (!CollectionUtils.isEmpty((Map)resultRolePermMap)) {
                HashMap<String, Serializable> maps = new HashMap<String, Serializable>(2);
                maps.put("syncRole", Integer.valueOf(1));
                maps.put("resultRolePermMap", resultRolePermMap);
                this.getView().setReturnData(maps);
                this.getView().returnDataToParent(maps);
            }
        }
    }

    private List<RelatePermInfo> getRelatePermInfos(QFilter qFilter) {
        HRBaseServiceHelper authHelper = new HRBaseServiceHelper("hrcs_permrelatcfg");
        DynamicObject[] rows = authHelper.query("id,app.id,entitytype.number,permitem.number,isassign", new QFilter[]{qFilter});
        return Arrays.stream(rows).filter(it -> HRStringUtils.equals((String)it.getString("isassign"), (String)"1")).filter(it -> HRStringUtils.isNotEmpty((String)it.getString("entitytype.number")) && HRStringUtils.isNotEmpty((String)it.getString("app.id")) && HRStringUtils.isNotEmpty((String)it.getString("permitem.number"))).map(it -> new RelatePermInfo(it.getString("entitytype.number"), it.getString("app.id"), it.getString("permitem.number"), null)).collect(Collectors.toList());
    }

    private void afterSaveProcessing() {
        String originEntryInfoStr = this.getPageCache().get("originEntryInfo");
        Set originEntryInfo = (Set)SerializationUtils.fromJsonString((String)originEntryInfoStr, Set.class);
        Set<String> newEntryInfo = this.getEntryInfo();
        Sets.SetView deleteRows = Sets.difference((Set)originEntryInfo, newEntryInfo);
        Sets.SetView addRows = Sets.difference(newEntryInfo, (Set)originEntryInfo);
        PermRelateServiceHelper.deletePermRelateConfigs((Set)deleteRows);
        PermRelateServiceHelper.addPermRelateConfigs((Set)addRows);
    }

    public void click(EventObject evt) {
        super.click(evt);
        Object source = evt.getSource();
        if (source instanceof TextEdit && ((TextEdit)source).getFieldKey().equals(FIELD_ENTRY_PERM_ITEM)) {
            EntryGrid en = (EntryGrid)this.getView().getControl("entryentity");
            int row = en.getSelectRows()[0];
            DynamicObjectCollection entryEntities = this.getModel().getEntryEntity("entryentity");
            DynamicObject dynamicObject = (DynamicObject)entryEntities.get(row);
            DynamicObject entity = dynamicObject.getDynamicObject(FIELD_ENTRY_ENTITY_TYPE);
            DynamicObject app = dynamicObject.getDynamicObject("app");
            boolean isSys = dynamicObject.getBoolean("issyspreset");
            if (isSys) {
                return;
            }
            if (entity != null && app != null) {
                this.showForm(entity);
            } else {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e1a\u52a1\u5bf9\u8c61\u3002", (String)"PermRelateEdit_03", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{null}));
            }
        }
    }

    private void showForm(DynamicObject entity) {
        Object mainPermItem = this.getModel().getValue(FIELD_PERM_ITEM);
        DynamicObject dataEntity = this.getModel().getDataEntity(true);
        String mainAppId = dataEntity.getDynamicObject("bizapp").getString("id");
        String mainEntityId = dataEntity.getDynamicObject("entitytype").getString("id");
        int entryCurrentRowIndex = this.getModel().getEntryCurrentRowIndex("entryentity");
        DynamicObject row = (DynamicObject)dataEntity.getDynamicObjectCollection("entryentity").get(entryCurrentRowIndex);
        String permItemId = row.getString(FIELD_ENTRY_PERM_ITEM_ID);
        String permItemName = row.getString(FIELD_ENTRY_PERM_ITEM);
        String appId = row.getDynamicObject("app").getString("id");
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"hrcs_choose_permitem", (boolean)true, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "hrcs_choose_permitem"));
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setPageId("hrcs_choosefield@" + this.getView().getPageId());
        Map entityPerm = (Map)SerializationUtils.fromJsonString((String)this.getPageCache().get("entityPerm"), Map.class);
        Map appEntityPerm = (Map)SerializationUtils.fromJsonString((String)this.getPageCache().get("appEntityPerm"), Map.class);
        ArrayList forbidPermIds = Lists.newArrayListWithCapacity((int)10);
        String entityNum = entity.getString("number");
        if (entityPerm.containsKey(entityNum)) {
            forbidPermIds.addAll((Collection)entityPerm.get(entityNum));
        }
        if (appEntityPerm.containsKey(appId) && ((Map)appEntityPerm.get(appId)).containsKey(entityNum)) {
            forbidPermIds.addAll((Collection)((Map)appEntityPerm.get(appId)).get(entityNum));
        }
        DynamicObjectCollection collection = new HRBaseServiceHelper("perm_permitem").queryOriginalCollection("number", new QFilter[]{new QFilter("id", "in", (Object)forbidPermIds)});
        List forbidPermNums = collection.stream().map(it -> it.getString("number")).collect(Collectors.toList());
        fsp.setCustomParam("paramEntityName", (Object)entityNum);
        fsp.setCustomParam("mainAppId", (Object)mainAppId);
        fsp.setCustomParam("appId", (Object)appId);
        fsp.setCustomParam("forbidPermNums", (Object)SerializationUtils.toJsonString(forbidPermNums));
        fsp.setCustomParam(FIELD_PERM_ITEM, mainPermItem);
        fsp.setCustomParam("mainentityid", (Object)mainEntityId);
        if (!permItemId.isEmpty()) {
            String[] permItemIds = permItemId.split(",");
            String[] permItemNames = permItemName.split(",");
            Object[] ids = new String[permItemIds.length];
            for (int index = 0; index < permItemIds.length; ++index) {
                ids[index] = permItemIds[index] + "||" + permItemNames[index];
            }
            fsp.setSelectedRows(ids);
        }
        fsp.setHasRight(true);
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void setMainPermItems(DynamicObject entity, DynamicObject app, boolean isAdd) {
        Map permItemMap = EntityCtrlServiceHelper.queryEntityPermItemIdNum((DynamicObject)entity);
        if (isAdd) {
            this.filterMainPermItem(entity, app, permItemMap);
        }
        HashMap permItemMapCopy = Maps.newHashMapWithExpectedSize((int)permItemMap.size());
        for (Map.Entry entry : permItemMap.entrySet()) {
            permItemMapCopy.put(((String)entry.getKey()).split("\\#")[1], entry.getValue());
        }
        ComboEdit comboEdit = (ComboEdit)this.getControl(FIELD_PERM_ITEM);
        ArrayList<ComboItem> data = new ArrayList<ComboItem>(permItemMap.size());
        for (Map.Entry var : permItemMapCopy.entrySet()) {
            data.add(new ComboItem(new LocaleString((String)var.getValue()), (String)var.getKey()));
        }
        comboEdit.setComboItems(data);
    }

    private void filterMainPermItem(DynamicObject entity, DynamicObject app, Map<String, String> permItemMap) {
        String entityId = entity.getString("id");
        String appId = app.getString("id");
        QFilter filter = new QFilter("entitytype", "=", (Object)entityId).and("bizapp", "=", (Object)appId);
        DynamicObject[] dys = this.permRelateHelper.query(FIELD_PERM_ITEM, new QFilter[]{filter});
        Set var1 = Arrays.stream(dys).map(dy -> dy.getString(FIELD_PERM_ITEM)).collect(Collectors.toSet());
        QFilter filter1 = new QFilter("entryentity.entitytypeid.id", "=", (Object)entityId).and("entryentity.app", "=", (Object)appId);
        DynamicObject[] dys2 = this.permRelateHelper.query("entryentity.permitemid,entryentity.app,entryentity.entitytypeid", new QFilter[]{filter1});
        StringBuilder sb = new StringBuilder();
        for (DynamicObject dy2 : dys2) {
            DynamicObjectCollection dc = dy2.getDynamicObjectCollection("entryentity");
            for (DynamicObject d : dc) {
                String relAppId = d.getString("app.id");
                String relEntityId = d.getString("entitytypeid.id");
                if (!HRStringUtils.equals((String)relAppId, (String)appId) || !HRStringUtils.equals((String)relEntityId, (String)entityId)) continue;
                sb.append(d.getString(FIELD_ENTRY_PERM_ITEM_ID));
                sb.append(',');
            }
        }
        String permItemStr = sb.toString();
        String[] permItemArr = permItemStr.split(",");
        var1.addAll(Arrays.asList(permItemArr));
        Set<String> usedPermNums = permItemMap.keySet().stream().filter(it -> var1.contains(it.split("\\#")[1])).collect(Collectors.toSet());
        usedPermNums.forEach(permItemMap::remove);
        var1.clear();
        Map entityPerm = (Map)SerializationUtils.fromJsonString((String)this.getPageCache().get("entityPerm"), Map.class);
        Map appEntityPerm = (Map)SerializationUtils.fromJsonString((String)this.getPageCache().get("appEntityPerm"), Map.class);
        for (Map.Entry<String, String> entry : permItemMap.entrySet()) {
            String[] permItemInfo = entry.getKey().split("\\#");
            String permId = permItemInfo[0];
            if (entityPerm.containsKey(entityId) && ((List)entityPerm.get(entityId)).contains(permId)) {
                var1.add(entry.getKey());
            }
            if (!appEntityPerm.containsKey(appId) || !((Map)appEntityPerm.get(appId)).containsKey(entityId) || !((List)((Map)appEntityPerm.get(appId)).get(entityId)).contains(permId)) continue;
            var1.add(entry.getKey());
        }
        for (String str : var1) {
            permItemMap.remove(str);
        }
    }

    private boolean setAppComboList(DynamicObject entity) {
        Set<String> forBidApps;
        ComboEdit cloudCombo = (ComboEdit)this.getView().getControl("appcombo");
        if (null == entity) {
            cloudCombo.setComboItems(new ArrayList(16));
            return false;
        }
        Map<String, List<String>> forBidAppEntity = this.getForBidAppEntity();
        List comboItems = EntityCtrlServiceHelper.getAppComboForPerm((DynamicObject)entity, forBidAppEntity, forBidApps = this.getForBidApp());
        if (comboItems.size() == 0) {
            this.getView().setEnable(Boolean.FALSE, new String[]{"appcombo"});
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u4e3b\u4e1a\u52a1\u5bf9\u8c61\u7684\u5e94\u7528\u5df2\u8bbe\u7f6e\u4e3a\u4e0d\u5141\u8bb8\u6388\u6743\uff0c\u4e0d\u5141\u8bb8\u8bbe\u7f6e\u5173\u8054\u6743\u9650\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PermRelateEdit_07", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        cloudCombo.setComboItems(comboItems);
        this.getModel().setValue("appcombo", (Object)((ComboItem)comboItems.get(0)).getValue());
        if (comboItems.size() <= 1) {
            this.getView().setEnable(Boolean.FALSE, new String[]{"appcombo"});
        }
        return true;
    }

    private Map<String, List<String>> getForBidAppEntity() {
        IPageCache pageCache = this.getPageCache();
        String forBidAppEntityStr = pageCache.get("forBidAppEntity");
        HashMap<String, List<String>> forBidAppEntity = new HashMap(16);
        if (HRStringUtils.isNotEmpty((String)forBidAppEntityStr)) {
            forBidAppEntity = (Map)SerializationUtils.fromJsonString((String)forBidAppEntityStr, Map.class);
        } else {
            EntityCtrlServiceHelper.queryEntityForBidInfo(null, null, forBidAppEntity);
            pageCache.put("forBidAppEntity", SerializationUtils.toJsonString(forBidAppEntity));
        }
        return forBidAppEntity;
    }

    private Set<String> getForBidApp() {
        IPageCache pageCache = this.getPageCache();
        String forBidAppStr = pageCache.get("forBidAppStr");
        HashSet<String> forBidAppIds = new HashSet(16);
        if (HRStringUtils.isNotEmpty((String)forBidAppStr)) {
            forBidAppIds = (Set)SerializationUtils.fromJsonString((String)forBidAppStr, Set.class);
        } else {
            EntityCtrlServiceHelper.queryEntityForBidInfo(null, forBidAppIds, null);
            pageCache.put("forBidAppStr", SerializationUtils.toJsonString(forBidAppIds));
        }
        return forBidAppIds;
    }

    private void putAllBuInfoToCache() {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbss_hrbucafunc");
        DynamicObject[] dys = serviceHelper.query("id,name", null);
        HashMap<String, String> map = new HashMap<String, String>(dys.length);
        for (DynamicObject dy : dys) {
            int buId = dy.getInt("id");
            map.put(Integer.toString(buId), dy.getString("name"));
        }
        this.getPageCache().put("allBuInfo", SerializationUtils.toJsonString(map));
    }

    private Set<String> getEntryInfo() {
        Map permIdNumberMap;
        DynamicObjectCollection collection = this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity");
        String permIdNumberMapStr = this.getPageCache().get("permIdNumberMap");
        if (HRStringUtils.isEmpty((String)permIdNumberMapStr)) {
            permIdNumberMap = PermRelateServiceHelper.queryPermItems();
            permIdNumberMapStr = SerializationUtils.toJsonString((Object)permIdNumberMap);
            this.getPageCache().put("permIdNumberMap", permIdNumberMapStr);
        } else {
            permIdNumberMap = (Map)SerializationUtils.fromJsonString((String)permIdNumberMapStr, Map.class);
        }
        return PermRelateServiceHelper.getEntryInfo((DynamicObjectCollection)collection, (Map)permIdNumberMap);
    }
}
