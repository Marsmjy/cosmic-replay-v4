/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.IMetadata
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.EntryType
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.RowDataEntity
 *  kd.bos.entity.datamodel.events.AfterAddRowEventArgs
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.EntryProp
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.OpenStyle
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.container.Container
 *  kd.bos.form.control.Button
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.CellClickEvent
 *  kd.bos.form.control.events.CellClickListener
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.RowClickEventListener
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.OnGetControlArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.field.MulComboEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.cache.HRPageCache
 *  kd.hr.hbp.common.util.HRDynamicObjectUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hpfs.business.application.service.fileMap.FileMapManagerService
 *  kd.hr.hpfs.business.application.service.fileMap.IFileMapManagerService
 *  kd.hr.hpfs.common.utils.MetaUtils
 *  kd.sdk.hr.hpfs.business.helper.HPFSMetaDataServiceHelper
 *  kd.sdk.hr.hpfs.common.constants.FileMapManagerConstants
 *  kd.sdk.hr.hpfs.utils.RepositoryUtils
 */
package kd.hr.hpfs.formplugin.fieldmap;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IMetadata;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.EntryType;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.RowDataEntity;
import kd.bos.entity.datamodel.events.AfterAddRowEventArgs;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.EntryProp;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.OpenStyle;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.container.Container;
import kd.bos.form.control.Button;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.CellClickEvent;
import kd.bos.form.control.events.CellClickListener;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.OnGetControlArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.MulComboEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.cache.HRPageCache;
import kd.hr.hbp.common.util.HRDynamicObjectUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hpfs.business.application.service.fileMap.FileMapManagerService;
import kd.hr.hpfs.business.application.service.fileMap.IFileMapManagerService;
import kd.hr.hpfs.common.utils.MetaUtils;
import kd.sdk.hr.hpfs.business.helper.HPFSMetaDataServiceHelper;
import kd.sdk.hr.hpfs.common.constants.FileMapManagerConstants;
import kd.sdk.hr.hpfs.utils.RepositoryUtils;

public final class FileMapManagerEdit
extends HRDataBaseEdit
implements CellClickListener,
BeforeF7SelectListener,
RowClickEventListener,
FileMapManagerConstants {
    private static final String CONTROL_KEY_BTN_MAP = "btn_map";
    private static final String CONTROL_KEY_BTN_UNMAP = "btn_unmap";
    private static final String CONTROL_KEY_ENEITYCONF = "entityconf";
    private static final String CHILDVIEWID = "childviewid";
    private static final String CHANGE_PERSONNEL_BUSINESS_BILL = "changePersonnelBusinessBill";
    private static final String ADV_SHEETMATCH_C = "adv_sheetmatch_c";
    private static final String BIZAPPID_ID = "bizappid.id";
    private HRPageCache hrPageCache;
    private static final String CACHE_KEY_SELECTED_MAPPED_FIELDS = "selected_mapped_fields";
    private static final String CACHE_KEY_LAST_INITED_COMBO = "last_inited_combo";
    private static final String UNMAP = "unmap";
    private static final Map<String, List<String>> PERSON3KEY;
    private static final Log LOG;

    public void beforeBindData(EventObject evt) {
        super.beforeBindData(evt);
        this.initHRPageCache();
        this.setHomVisible();
    }

    private void initHRPageCache() {
        HRPageCache pageCache = this.getHRPageCache();
        LinkedList selectedFields = new LinkedList();
        pageCache.put(CACHE_KEY_SELECTED_MAPPED_FIELDS, selectedFields);
    }

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        Button btnMap = (Button)this.getView().getControl(CONTROL_KEY_BTN_MAP);
        btnMap.addClickListener((ClickListener)this);
        Button btnUnmap = (Button)this.getView().getControl(CONTROL_KEY_BTN_UNMAP);
        btnUnmap.addClickListener((ClickListener)this);
        EntryGrid entryGrid = (EntryGrid)this.getView().getControl("entryentity");
        entryGrid.addCellClickListener((CellClickListener)this);
        entryGrid.addRowClickListener((RowClickEventListener)this);
        EntryGrid entryCGrid = (EntryGrid)this.getView().getControl("entryentity_c");
        entryCGrid.addCellClickListener((CellClickListener)this);
        entryCGrid.addRowClickListener((RowClickEventListener)this);
        BasedataEdit sourceEntityF7 = (BasedataEdit)this.getView().getControl("sourceentity");
        sourceEntityF7.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit targetEntityF7 = (BasedataEdit)this.getView().getControl("targetentity");
        targetEntityF7.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit sourceEntityC = (BasedataEdit)this.getView().getControl("sourceentity_c");
        sourceEntityC.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit targetEntityC = (BasedataEdit)this.getView().getControl("targetentity_c");
        targetEntityC.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void cellClick(CellClickEvent evt) {
        String fieldKey;
        switch (fieldKey = evt.getFieldKey()) {
            case "sourceprikey": {
                this.setMapKeyComboProp("sourceentity", "sourceprikey", evt.getRow());
                break;
            }
            case "targetprikey": {
                this.setMapKeyComboProp("targetentity", "targetprikey", evt.getRow());
                break;
            }
            case "selectsrcentry": {
                this.showSelectEntryF7(evt);
                break;
            }
            case "srcentryfieldrelation": {
                this.showSrcEntryFieldRelationF7(evt);
                break;
            }
            case "srcentryfieldrelation_c": {
                this.showSrcEntryFieldRelationF7C(evt);
                break;
            }
        }
    }

    private void showSrcEntryFieldRelationF7C(CellClickEvent evt) {
        if (this.getChildView(false)) {
            return;
        }
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hpfs_fieldrelationc");
        showParameter.getOpenStyle().setShowType(ShowType.PageDrawer);
        showParameter.getOpenStyle().setPlaceholder(false);
        showParameter.setStatus(OperationStatus.EDIT);
        String entryKey = ((EntryGrid)evt.getSource()).getEntryKey();
        DynamicObject selectRowEntityDy = this.getModel().getEntryRowEntity(entryKey, evt.getRow());
        if (Objects.isNull(selectRowEntityDy)) {
            return;
        }
        DynamicObject sourceEntityDy = selectRowEntityDy.getDynamicObject("sourceentity_c");
        if (Objects.isNull(sourceEntityDy)) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u62df\u5165\u804c\u4eba\u5458\u4fe1\u606f\u7ec4\u3002", (String)"FileMapManagerEdit_13", (String)"hr-hpfs-formplugin", (Object[])new Object[0]));
            return;
        }
        DynamicObject targetEntityDy = selectRowEntityDy.getDynamicObject("targetentity_c");
        if (Objects.isNull(targetEntityDy)) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u5173\u8054\u7684\u4eba\u5458\u4fe1\u606f\u7ec4\u3002", (String)"FileMapManagerEdit_8", (String)"hr-hpfs-formplugin", (Object[])new Object[0]));
            return;
        }
        long id = selectRowEntityDy.getLong("id");
        if (id == 0L) {
            ORM.create().genLongId(selectRowEntityDy.getDataEntityType());
            selectRowEntityDy.set("id", (Object)id);
        }
        String caption = String.format(Locale.ROOT, ResManager.loadKDString((String)"%1$s\u4e0e%2$s\u5b57\u6bb5\u5173\u7cfb", (String)"FileMapManagerEdit_12", (String)"hr-hpfs-formplugin", (Object[])new Object[0]), selectRowEntityDy.getString("sourceentity_c.name"), targetEntityDy.getString("name"));
        showParameter.setCaption(caption);
        showParameter.setCustomParam("selectrowentitydy", (Object)SerializationUtils.serializeToBase64((Object)selectRowEntityDy));
        DynamicObject dataEntity = this.getModel().getDataEntity();
        DynamicObjectCollection entryEntityDys = this.getModel().getEntryEntity("entryentity_c");
        DynamicObjectCollection otherEntryEntityDys = this.getModel().getEntryEntity("entryentity");
        showParameter.setCustomParam("entryentitydata", (Object)SerializationUtils.serializeToBase64((Object)entryEntityDys));
        showParameter.setCustomParam("otherentryentitydata", (Object)SerializationUtils.serializeToBase64((Object)otherEntryEntityDys));
        DynamicObject personnelBusinessBill = dataEntity.getDynamicObject("personnelbusinessbill");
        if (Objects.isNull(personnelBusinessBill)) {
            return;
        }
        showParameter.setCustomParam("personnelbusinessbillnumber", (Object)personnelBusinessBill.getString("number"));
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "srcentryfieldrelation_c"));
        String pageId = showParameter.getPageId();
        this.getPageCache().put(CHILDVIEWID, pageId);
        this.getView().showForm(showParameter);
    }

    private boolean getChildView(boolean isClose) {
        try {
            String childWiewId = this.getPageCache().get(CHILDVIEWID);
            if (childWiewId != null) {
                IFormView view = this.getView().getView(childWiewId);
                if (isClose) {
                    view.getPageCache().put("isdirectClose", "1");
                    view.close();
                    this.getView().sendFormAction(view);
                    return false;
                }
                if (view.getModel().getDataChanged() && StringUtils.isNotBlank((CharSequence)view.getModel().getChangeDesc())) {
                    view.close();
                    this.getView().sendFormAction(view);
                    return true;
                }
            }
        }
        catch (Exception exception) {
            LOG.error("childview is null", (Throwable)exception);
        }
        return false;
    }

    private void showSrcEntryFieldRelationF7(CellClickEvent evt) {
        if (this.getChildView(false)) {
            return;
        }
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hpfs_fieldrelation");
        showParameter.getOpenStyle().setShowType(ShowType.PageDrawer);
        showParameter.getOpenStyle().setPlaceholder(false);
        showParameter.setStatus(OperationStatus.EDIT);
        String entryKey = ((EntryGrid)evt.getSource()).getEntryKey();
        DynamicObject selectRowEntityDy = this.getModel().getEntryRowEntity(entryKey, evt.getRow());
        if (Objects.isNull(selectRowEntityDy)) {
            return;
        }
        DynamicObject sourceEntityDy = selectRowEntityDy.getDynamicObject("sourceentity");
        if (Objects.isNull(sourceEntityDy)) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u5355\u636e\u4fe1\u606f\u5206\u7ec4\u3002", (String)"FileMapManagerEdit_14", (String)"hr-hpfs-formplugin", (Object[])new Object[0]));
            return;
        }
        DynamicObject targetEntityDy = selectRowEntityDy.getDynamicObject("targetentity");
        if (Objects.isNull(targetEntityDy)) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u5173\u8054\u7684\u4eba\u5458\u4fe1\u606f\u7ec4\u3002", (String)"FileMapManagerEdit_8", (String)"hr-hpfs-formplugin", (Object[])new Object[0]));
            return;
        }
        long id = selectRowEntityDy.getLong("id");
        if (id == 0L) {
            ORM.create().genLongId(selectRowEntityDy.getDataEntityType());
            selectRowEntityDy.set("id", (Object)id);
        }
        String caption = String.format(Locale.ROOT, ResManager.loadKDString((String)"%1$s\u4e0e%2$s\u5b57\u6bb5\u5173\u7cfb", (String)"FileMapManagerEdit_12", (String)"hr-hpfs-formplugin", (Object[])new Object[0]), selectRowEntityDy.getString("billinfogroup"), targetEntityDy.getString("name"));
        showParameter.setCaption(caption);
        showParameter.setCustomParam("selectrowentitydy", (Object)SerializationUtils.serializeToBase64((Object)selectRowEntityDy));
        DynamicObject dataEntity = this.getModel().getDataEntity();
        DynamicObjectCollection entryEntityDys = this.getModel().getEntryEntity("entryentity");
        DynamicObjectCollection otherEntryEntityDys = this.getModel().getEntryEntity("entryentity_c");
        showParameter.setCustomParam("entryentitydata", (Object)SerializationUtils.serializeToBase64((Object)entryEntityDys));
        showParameter.setCustomParam("otherentryentitydata", (Object)SerializationUtils.serializeToBase64((Object)otherEntryEntityDys));
        DynamicObject personnelBusinessBill = dataEntity.getDynamicObject("personnelbusinessbill");
        if (Objects.isNull(personnelBusinessBill)) {
            return;
        }
        showParameter.setCustomParam("personnelbusinessbillnumber", (Object)personnelBusinessBill.getString("number"));
        showParameter.setCustomParam("blackListField", (Object)this.getPageCache().get("blackListField"));
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "srcentryfieldrelation"));
        String pageId = showParameter.getPageId();
        this.getPageCache().put(CHILDVIEWID, pageId);
        this.getView().showForm(showParameter);
    }

    public void afterLoadData(EventObject evt) {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        this.setGroupInfo(dataEntity);
        this.getModel().setDataChanged(false);
    }

    private Map<String, String> getEntityKeyAndNameMap(DynamicObject dataEntity, List<String> extEntryList) {
        String number = dataEntity.getString("personnelbusinessbill.number");
        DataEntityPropertyCollection properties = IFileMapManagerService.getInstance().getEntityNameByExtEntityName(number).getProperties();
        List collect = properties.stream().filter(data -> extEntryList.contains(data.getName())).collect(Collectors.toList());
        return collect.stream().collect(Collectors.toMap(IMetadata::getName, data -> StringUtils.isNotBlank((CharSequence)data.getDisplayName().getLocaleValue()) ? data.getDisplayName().getLocaleValue() : data.getName()));
    }

    public void cellDoubleClick(CellClickEvent cellClickEvent) {
    }

    public void click(EventObject evt) {
        String key;
        super.click(evt);
        Control control = (Control)evt.getSource();
        switch (key = control.getKey()) {
            case "btn_unmap": {
                this.showUnmapConfirm();
                break;
            }
        }
    }

    private void showUnmapConfirm() {
        HRPageCache pageCache = this.getHRPageCache();
        List<String> selectField = this.getSelectedMappedFields(pageCache);
        if (CollectionUtils.isEmpty(selectField)) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u9700\u8981\u89e3\u9664\u5173\u8054\u7684\u5b57\u6bb5\u3002", (String)"FileMapManagerEdit_0", (String)"hr-hpfs-formplugin", (Object[])new Object[0]));
            return;
        }
        this.getView().showConfirm(ResManager.loadKDString((String)"\u786e\u8ba4\u89e3\u9664\u5173\u8054\uff1f", (String)"FileMapManagerEdit_1", (String)"hr-hpfs-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, new ConfirmCallBackListener(UNMAP, (IFormPlugin)this));
    }

    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        int result;
        super.confirmCallBack(messageBoxClosedEvent);
        String callBackId = messageBoxClosedEvent.getCallBackId();
        if (HRStringUtils.equals((String)callBackId, (String)CHANGE_PERSONNEL_BUSINESS_BILL) && (result = messageBoxClosedEvent.getResult().getValue()) == MessageBoxResult.Yes.getValue()) {
            this.changePersonnelBusinessBill();
        }
    }

    private void changePersonnelBusinessBill() {
        this.setHomVisible();
        this.getModel().deleteEntryData("entryentity");
        this.getModel().deleteEntryData("entryentity_c");
        DynamicObject dataEntity = this.getModel().getDataEntity();
        this.setGroupInfo(dataEntity);
        this.getChildView(true);
    }

    private List<String> getSelectedMappedFields(HRPageCache pageCache) {
        ArrayList selectedFields = (ArrayList)pageCache.get(CACHE_KEY_SELECTED_MAPPED_FIELDS, List.class);
        if (null == selectedFields) {
            selectedFields = new ArrayList();
        }
        return selectedFields;
    }

    public void onGetControl(OnGetControlArgs evt) {
        super.onGetControl(evt);
        String key = evt.getKey();
        if (this.isClickableFieldFlex(key)) {
            this.bindFieldFlexClick(key, evt);
        }
    }

    public boolean isClickableFieldFlex(String key) {
        return key.contains("flex_fieldum") || key.contains("flex_fieldmm");
    }

    private void bindFieldFlexClick(String key, OnGetControlArgs evt) {
        Container container = new Container();
        container.setKey(key);
        container.setView(this.getView());
        container.addClickListener((ClickListener)this);
        evt.setControl((Control)container);
    }

    private boolean isFieldShouldShow(IDataEntityProperty property) {
        LocaleString chineseName = property.getDisplayName();
        String name = property.getName();
        return null != chineseName || "id".equals(name);
    }

    public void propertyChanged(PropertyChangedArgs evt) {
        String propertyName;
        block5 : switch (propertyName = evt.getProperty().getName()) {
            case "targetentity": {
                ChangeData[] changeSet = evt.getChangeSet();
                if (changeSet == null || changeSet.length == 0) {
                    return;
                }
                if (Objects.nonNull(changeSet[0].getOldValue()) && !changeSet[0].getOldValue().equals(changeSet[0].getNewValue())) {
                    this.getModel().deleteEntryData("subentryentity");
                    this.getView().updateView("subentryentity");
                }
                if (!Objects.nonNull(changeSet[0].getNewValue())) break;
                DynamicObjectCollection entryEntityDys = this.getModel().getEntryEntity("entryentity");
                Map<String, List<DynamicObject>> targetEntityAndDysMap = entryEntityDys.stream().filter(data -> "n_extinfo_fields".equals(data.getString("sourceentry"))).filter(data -> Objects.nonNull(data.getDynamicObject("targetentity"))).collect(Collectors.groupingBy(data -> data.getString("targetentity.id")));
                for (Map.Entry<String, List<DynamicObject>> entry : targetEntityAndDysMap.entrySet()) {
                    if (entry.getValue().size() <= 1) continue;
                    String billInfoGroup = entry.getValue().get(0).getString("billinfogroup");
                    String targetEntityName = entry.getValue().get(0).getString("targetentity.name");
                    String msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5355\u636e\u4fe1\u606f\u5206\u7ec4\u201c%1$s\u201d\uff0c\u4eba\u5458\u4fe1\u606f\u7ec4\u201c%2$s\u201d\u4e0d\u80fd\u76f8\u540c\uff0c\u8bf7\u8c03\u6574\u540e\u518d\u8bd5\u3002", (String)"FileMapManagerEdit_9", (String)"hr-hpfs-formplugin", (Object[])new Object[0]), billInfoGroup, targetEntityName);
                    this.getView().showErrorNotification(msg);
                    break block5;
                }
                break;
            }
            case "targetentity_c": {
                this.changeTargetEntityC(evt);
                break;
            }
            case "personnelbusinessbill": {
                this.changePersonnelBusinessBill(evt);
                break;
            }
        }
    }

    private void setHomVisible() {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        DynamicObject personnelbusinessbill = dataEntity.getDynamicObject("personnelbusinessbill");
        if (Objects.nonNull(personnelbusinessbill)) {
            this.getView().setVisible(Boolean.valueOf("1WXB5G9/BL46".equals(personnelbusinessbill.getString(BIZAPPID_ID))), new String[]{ADV_SHEETMATCH_C});
        } else {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{ADV_SHEETMATCH_C});
        }
    }

    private void changeTargetEntityC(PropertyChangedArgs evt) {
        ChangeData[] changeSet = evt.getChangeSet();
        if (changeSet == null || changeSet.length == 0) {
            return;
        }
        if (Objects.nonNull(changeSet[0].getOldValue()) && !changeSet[0].getOldValue().equals(changeSet[0].getNewValue())) {
            this.getModel().deleteEntryData("subentryentity_c");
            this.getView().updateView("subentryentity_c");
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if ("save".equals(operateKey) && this.getChildView(false)) {
            args.setCancel(true);
        }
    }

    private void changePersonnelBusinessBill(PropertyChangedArgs args) {
        ChangeData[] changeSet = args.getChangeSet();
        if (changeSet == null || changeSet.length == 0) {
            return;
        }
        if (Objects.nonNull(changeSet[0].getNewValue()) && changeSet[0].getNewValue().equals(changeSet[0].getOldValue())) {
            return;
        }
        if (Objects.isNull(changeSet[0].getOldValue())) {
            this.changePersonnelBusinessBill();
        } else {
            this.getView().showConfirm(ResManager.loadKDString((String)"\u66f4\u6362\u4eba\u4e8b\u4e1a\u52a1\u5355\u636e\u540e\u5c06\u6e05\u7a7a\u5355\u636e\u4fe1\u606f\uff0c\u8bf7\u786e\u8ba4\uff1f", (String)"FileMapManagerEdit_11", (String)"hr-hpfs-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, new ConfirmCallBackListener(CHANGE_PERSONNEL_BUSINESS_BILL, (IFormPlugin)this));
        }
    }

    private void setGroupInfo(DynamicObject dataEntity) {
        DynamicObject personnelBusinessBill = dataEntity.getDynamicObject("personnelbusinessbill");
        if (Objects.isNull(personnelBusinessBill)) {
            return;
        }
        String number = personnelBusinessBill.getString("number");
        MainEntityType dataEntityType = IFileMapManagerService.getInstance().getEntityNameByExtEntityName(number);
        DataEntityPropertyCollection properties = dataEntityType.getProperties();
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity");
        DynamicObjectCollection entryEntityC = this.getModel().getEntryEntity("entryentity_c");
        this.dealSysPresetEntryInfo(number, entryEntity);
        this.dealExtEntryInfo(dataEntityType, dataEntity, properties, entryEntity);
        this.sortEntryEntity(entryEntity, "srcentryissyspreset", "billinfogroup", "targetentity.id");
        this.getModel().updateEntryCache(entryEntity);
        this.getView().updateView("entryentity");
        this.dealHcfInfo(dataEntity, entryEntityC);
        this.sortEntryEntity(entryEntityC, "srcentryissyspreset_c", "sourceentity_c.name", "targetentity_c.id");
        this.getModel().updateEntryCache(entryEntityC);
        this.getView().updateView("entryentity_c");
    }

    private void dealSysPresetEntryInfo(String number, DynamicObjectCollection entryEntity) {
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (!OperationStatus.ADDNEW.equals((Object)status)) {
            return;
        }
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hpfs_filemapmanager");
        QFilter qFilter = new QFilter("personnelbusinessbill", "=", (Object)number);
        DynamicObject dynamicObject = helper.loadDynamicObject(new QFilter[]{qFilter, RepositoryUtils.isSysPreset()});
        if (dynamicObject == null) {
            LOG.warn("preset data is empty,number :{}", (Object)number);
            return;
        }
        DynamicObjectCollection collection = dynamicObject.getDynamicObjectCollection("entryentity");
        for (DynamicObject entry : collection) {
            if (!"1".equals(entry.getString("srcentryissyspreset"))) continue;
            DynamicObject addNew = entryEntity.addNew();
            HashSet<String> hashSet = new HashSet<String>(16);
            hashSet.add("id");
            HRDynamicObjectUtils.copy((DynamicObject)entry, (DynamicObject)addNew, hashSet);
            addNew.set("billinfocombo", (Object)entry.getString("billinfogroup"));
        }
    }

    private void sortEntryEntity(DynamicObjectCollection entryEntityC, String srcEntryIsSysPreset, String sourceEntityCName, String targetEntityCId) {
        Collator instance = Collator.getInstance(RequestContext.get().getLang().getLocale());
        entryEntityC.sort((o1, o2) -> {
            String index2;
            String srcEntryIsSysPresetIndex1 = o1.getString(srcEntryIsSysPreset);
            String srcEntryIsSysPresetIndex2 = o2.getString(srcEntryIsSysPreset);
            int srcEntryIsSysPresetIndex = srcEntryIsSysPresetIndex2.compareTo(srcEntryIsSysPresetIndex1);
            if (srcEntryIsSysPresetIndex != 0) {
                return srcEntryIsSysPresetIndex;
            }
            String index1 = o1.getString(sourceEntityCName) == null ? "" : o1.getString(sourceEntityCName);
            int index = instance.compare(index1, index2 = o2.getString(sourceEntityCName) == null ? "" : o2.getString(sourceEntityCName));
            if (index != 0) {
                return index;
            }
            String index3 = o1.getString(targetEntityCId) == null ? "" : o1.getString(targetEntityCId);
            String index4 = o2.getString(targetEntityCId) == null ? "" : o2.getString(targetEntityCId);
            return instance.compare(index3, index4);
        });
    }

    private void dealHcfInfo(DynamicObject dataEntity, DynamicObjectCollection entryEntity) {
        String appId = dataEntity.getDynamicObject("personnelbusinessbill").getString(BIZAPPID_ID);
        if (!"1WXB5G9/BL46".equals(appId) || !OperationStatus.ADDNEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
            return;
        }
        DynamicObject[] childDys = HPFSMetaDataServiceHelper.getCandidateObjecttype();
        List sourceEntityCs = entryEntity.stream().map(data -> data.getString("sourceentity_c.number")).collect(Collectors.toList());
        entryEntity.stream().map(data -> data.getString("sourceentity_c.number")).collect(Collectors.toList());
        for (DynamicObject childDy : childDys) {
            if (sourceEntityCs.contains(childDy.getString("number"))) continue;
            DynamicObject dynamicObject = entryEntity.addNew();
            dynamicObject.set("sourceentity_c", (Object)childDy);
            dynamicObject.set("srcentryissyspreset_c", (Object)"0");
        }
    }

    private void dealExtEntryInfo(MainEntityType dataEntityType, DynamicObject dataEntity, DataEntityPropertyCollection properties, DynamicObjectCollection entryEntity) {
        Set blackListFieldSet = new FileMapManagerService().getAllBlackListFields(dataEntityType, this.getPageCache());
        ArrayList<String> extEntryList = new ArrayList<String>(8);
        properties.forEach(dataEntityProperty -> {
            if (dataEntityProperty instanceof EntryProp) {
                Map fields = ((EntryType)((EntryProp)dataEntityProperty).getItemType()).getFields();
                if (blackListFieldSet.containsAll(fields.keySet())) {
                    return;
                }
                extEntryList.add(dataEntityProperty.getName());
            }
        });
        if (CollectionUtils.isEmpty(extEntryList)) {
            return;
        }
        Map<String, String> entityKeyAndNameMap = this.getEntityKeyAndNameMap(dataEntity, extEntryList);
        Map<String, List<DynamicObject>> pageGroupMaps = entryEntity.stream().collect(Collectors.groupingBy(data -> data.getString("sourceentry")));
        for (String extEntry : extEntryList) {
            List<DynamicObject> dynamicObjects = pageGroupMaps.get(extEntry);
            if (CollectionUtils.isEmpty(dynamicObjects)) continue;
            for (DynamicObject dynamicObject : dynamicObjects) {
                dynamicObject.set("sourceentity", (Object)dataEntity.getDynamicObject("personnelbusinessbill"));
                dynamicObject.set("billinfogroup", (Object)entityKeyAndNameMap.getOrDefault(extEntry, extEntry));
                dynamicObject.set("sourceentry", (Object)extEntry);
                dynamicObject.set("srcentryissyspreset", (Object)"0");
            }
        }
    }

    private void setMapKeyComboProp(String entityKey, String comboKey, int rowIndex) {
        HRPageCache cache;
        MulComboEdit fieldEditor = (MulComboEdit)this.getView().getControl(comboKey);
        DynamicObject entity = (DynamicObject)this.getModel().getValue(entityKey, rowIndex);
        if (null == entity) {
            return;
        }
        String entityNumber = entity.getString("number");
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        String initedComboName = entityNumber + comboKey;
        if (HRStringUtils.equals((String)initedComboName, (String)((String)(cache = this.getHRPageCache()).get(CACHE_KEY_LAST_INITED_COMBO, String.class)))) {
            return;
        }
        DataEntityPropertyCollection properties = dataEntityType.getProperties();
        ArrayList<ComboItem> fieldCombos = new ArrayList<ComboItem>();
        for (IDataEntityProperty property : properties) {
            if (!this.isFieldShouldShow(property)) continue;
            ComboItem item = new ComboItem();
            String name = property.getName();
            LocaleString chineseNameLS = property.getDisplayName();
            String chineseName = null == chineseNameLS ? name : chineseNameLS.getLocaleValue();
            item.setValue(name);
            item.setCaption(new LocaleString(name + "(" + chineseName + ")"));
            fieldCombos.add(item);
        }
        fieldEditor.setComboItems(fieldCombos);
        EntryGrid tableEntry = (EntryGrid)this.getControl("entryentity");
        cache.put(CACHE_KEY_LAST_INITED_COMBO, (Object)initedComboName);
        tableEntry.focusCell(rowIndex, comboKey);
    }

    public HRPageCache getHRPageCache() {
        if (null == this.hrPageCache) {
            this.hrPageCache = new HRPageCache(this.getPageCache());
        }
        return this.hrPageCache;
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String fieldKey = evt.getProperty().getName();
        ListShowParameter showParameter = (ListShowParameter)evt.getFormShowParameter();
        DynamicObject entityConf = (DynamicObject)this.getModel().getValue(CONTROL_KEY_ENEITYCONF);
        switch (fieldKey) {
            case "sourceentity": {
                showParameter.getListFilterParameter().getQFilters().add(this.getSourceEntityFilter(entityConf));
                break;
            }
            case "targetentity": 
            case "targetentity_c": {
                showParameter.getListFilterParameter().getQFilters().add(this.getTargetEntityFilter(entityConf));
                List entitys = HPFSMetaDataServiceHelper.getEmployeeObjecttype();
                QFilter filter1 = new QFilter("number", "in", (Object)entitys);
                evt.addCustomQFilter(filter1);
                showParameter.getListFilterParameter().getQFilters().add(filter1);
                break;
            }
            case "sourceentity_c": {
                QFilter filter = new QFilter(BIZAPPID_ID, "=", (Object)"15W6K=0MEPN8");
                filter.or(new QFilter("bizappid.masterid", "=", (Object)"15W6K=0MEPN8"));
                evt.addCustomQFilter(filter);
                showParameter.getListFilterParameter().getQFilters().add(filter);
                break;
            }
        }
    }

    private QFilter getSourceEntityFilter(DynamicObject entityConf) {
        if (null == entityConf) {
            return this.getDefaultEntityFilter();
        }
        DynamicObjectCollection sourceEntities = entityConf.getDynamicObjectCollection("sourceentryentity");
        ArrayList sourceEntityIds = new ArrayList(sourceEntities.size());
        sourceEntities.forEach(sourceEntity -> sourceEntityIds.add(sourceEntity.getString("sourceentity.id")));
        QFilter entityFilter = new QFilter("id", "in", sourceEntityIds);
        return entityFilter;
    }

    public QFilter getDefaultEntityFilter() {
        return new QFilter("modeltype", "=", (Object)"BaseFormModel").or(new QFilter("modeltype", "=", (Object)"BillFormModel"));
    }

    private QFilter getTargetEntityFilter(DynamicObject entityConf) {
        if (null == entityConf) {
            return this.getDefaultEntityFilter();
        }
        DynamicObjectCollection sourceEntities = entityConf.getDynamicObjectCollection("targetentryentity");
        ArrayList sourceEntityIds = new ArrayList(sourceEntities.size());
        sourceEntities.forEach(sourceEntity -> sourceEntityIds.add(sourceEntity.getString("targetentity.id")));
        QFilter entityFilter = new QFilter("id", "in", sourceEntityIds);
        return entityFilter;
    }

    private void showSelectEntryF7(CellClickEvent evt) {
        DynamicObject mainEntity = (DynamicObject)this.getModel().getValue("sourceentity", evt.getRow());
        if (null == mainEntity) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u6e90\u5b9e\u4f53", (String)"FileMapManagerEdit_10", (String)"hr-hpfs-formplugin", (Object[])new Object[0]));
            return;
        }
        String entityId = mainEntity.getString("id");
        entityId = MetaUtils.getSourceEntityNum((String)entityId);
        QFilter entityFilter = new QFilter("fid", "=", (Object)entityId).and("isdeleted", "=", (Object)"0");
        this.showEntryF7(evt, entityFilter);
    }

    public void showEntryF7(CellClickEvent evt, QFilter entityFilter) {
        String cellKey = evt.getFieldKey();
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.setBillFormId("hpfs_entityinfo");
        listShowParameter.setFormId("bos_listf7");
        listShowParameter.setMultiSelect(false);
        listShowParameter.setShowApproved(false);
        listShowParameter.setLookUp(true);
        listShowParameter.setShowTitle(false);
        listShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, cellKey));
        listShowParameter.setHasRight(true);
        listShowParameter.setCustomParam("rowIndex", (Object)String.valueOf(evt.getRow()));
        List filters = listShowParameter.getListFilterParameter().getQFilters();
        filters.add(entityFilter);
        OpenStyle style = this.getOpenStyle();
        listShowParameter.setOpenStyle(style);
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    private OpenStyle getOpenStyle() {
        OpenStyle style = new OpenStyle();
        StyleCss cssValue = new StyleCss();
        cssValue.setWidth("880px");
        cssValue.setHeight("600px");
        style.setInlineStyleCss(cssValue);
        style.setShowType(ShowType.Modal);
        return style;
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        DynamicObjectCollection subEntryEntityDys;
        Object returnData;
        super.closedCallBack(closedCallBackEvent);
        String actionId = closedCallBackEvent.getActionId();
        if ("selectsrcentry".equals(actionId)) {
            Map pkMap;
            Map entityNameMap;
            HRPageCache pageCache = new HRPageCache(this.getView());
            List pageList = (List)pageCache.get("entityList", List.class);
            ListSelectedRowCollection selectedRows = (ListSelectedRowCollection)closedCallBackEvent.getReturnData();
            if (HRObjectUtils.isEmpty((Object)selectedRows)) {
                return;
            }
            ListSelectedRow row = selectedRows.get(0);
            Optional<Map> any = pageList.stream().filter(entry -> entry.containsKey(row.getPrimaryKeyValue())).findAny();
            if (any.isPresent() && !CollectionUtils.isEmpty((Map)(entityNameMap = (Map)(pkMap = any.get()).get(row.getPrimaryKeyValue())))) {
                String entitykey = (String)entityNameMap.get("entitykey");
                String name = (String)entityNameMap.get("name");
                int rowIndex = Integer.parseInt((String)entityNameMap.get("rowIndex"));
                DynamicObjectCollection entity = this.getModel().getEntryEntity("entryentity");
                this.getModel().setValue("sourceentry", (Object)entitykey, rowIndex);
                this.getModel().setValue("sourceentryname", (Object)name, rowIndex);
            }
        }
        if (actionId.equals("srcentryfieldrelation") && Objects.nonNull(returnData = closedCallBackEvent.getReturnData())) {
            subEntryEntityDys = (DynamicObjectCollection)SerializationUtils.deSerializeFromBase64((String)String.valueOf(returnData));
            IFileMapManagerService.getInstance().setSubEntryEntity(subEntryEntityDys, this.getView());
            this.setBillInfoOrCandidateEnable("subentryentity", "targetfieldnew", "entryentity", "billinfocombo");
            this.getView().updateView("subentryentity");
        }
        if (actionId.equals("srcentryfieldrelation_c") && Objects.nonNull(returnData = closedCallBackEvent.getReturnData())) {
            subEntryEntityDys = (DynamicObjectCollection)SerializationUtils.deSerializeFromBase64((String)String.valueOf(returnData));
            IFileMapManagerService.getInstance().setSubEntryEntityC(subEntryEntityDys, this.getView());
            this.setBillInfoOrCandidateEnable("subentryentity_c", "targetfieldnew_c", "entryentity_c", "sourceentity_c");
            this.getView().updateView("subentryentity_c");
        }
    }

    private void setBillInfoOrCandidateEnable(String childEntryentity, String targetfieldkey, String parentEntryentity, String billInfo) {
        DynamicObjectCollection subEntryentities = this.getModel().getEntryEntity(childEntryentity);
        Optional<DynamicObject> exist = subEntryentities.stream().filter(mappedFieldDy -> mappedFieldDy.getDynamicObject(targetfieldkey) != null).findAny();
        int entryCurrentRowIndex = this.getModel().getEntryCurrentRowIndex(parentEntryentity);
        if (exist.isPresent()) {
            this.getView().setEnable(Boolean.valueOf(false), entryCurrentRowIndex, new String[]{billInfo});
        } else {
            this.getView().setEnable(Boolean.valueOf(true), entryCurrentRowIndex, new String[]{billInfo});
        }
    }

    public void afterAddRow(AfterAddRowEventArgs rowEventArgs) {
        super.afterAddRow(rowEventArgs);
        String entryName = rowEventArgs.getEntryProp().getName();
        RowDataEntity[] rowDataEntities = rowEventArgs.getRowDataEntities();
        if ("entryentity".equals(entryName)) {
            this.setNewRowInfoGroup(rowEventArgs, rowDataEntities, entryName, "billinfogroup");
        } else if ("entryentity_c".equals(entryName)) {
            this.setNewRowInfoGroup(rowEventArgs, rowDataEntities, entryName, "sourceentity_c");
        }
    }

    private void setNewRowInfoGroup(AfterAddRowEventArgs rowEventArgs, RowDataEntity[] rowDataEntities, String entryName, String field) {
        if (Objects.nonNull(rowDataEntities) && rowDataEntities.length > 0) {
            int insertRow = rowEventArgs.getInsertRow();
            if (insertRow == -1) {
                return;
            }
            DynamicObjectCollection entryDys = this.getModel().getEntryEntity(entryName);
            DynamicObject dynamicObject = (DynamicObject)entryDys.get(insertRow - 1);
            for (RowDataEntity rowData : rowDataEntities) {
                int rowIndex = rowData.getRowIndex();
                if (dynamicObject == null) continue;
                this.getModel().setValue(field, dynamicObject.get(field), rowIndex);
                if ("entryentity".equals(entryName)) {
                    this.getModel().setValue("sourceentity", dynamicObject.get("sourceentity"), rowIndex);
                    this.getModel().setValue("sourceprikey", dynamicObject.get("sourceprikey"), rowIndex);
                    this.getModel().setValue("sourceentry", dynamicObject.get("sourceentry"), rowIndex);
                    this.getModel().setValue("targetprikey", dynamicObject.get("targetprikey"), rowIndex);
                    this.getModel().setValue("srcentryissyspreset", dynamicObject.get("srcentryissyspreset"), rowIndex);
                    continue;
                }
                if (!"entryentity_c".equals(entryName)) continue;
                this.getModel().setValue("sourceprikey_c", dynamicObject.get("sourceprikey_c"), rowIndex);
                this.getModel().setValue("sourceentry_c", dynamicObject.get("sourceentry_c"), rowIndex);
                this.getModel().setValue("billinfogroup_c", dynamicObject.get("billinfogroup_c"), rowIndex);
                this.getModel().setValue("targetprikey_c", dynamicObject.get("targetprikey_c"), rowIndex);
                this.getModel().setValue("srcentryissyspreset_c", dynamicObject.get("srcentryissyspreset_c"), rowIndex);
            }
        }
    }

    static {
        LOG = LogFactory.getLog(FileMapManagerEdit.class);
        List<String> empValList = Arrays.asList("enterprise", "laborreltype", "laborrelstatus", "empnumber", "startdate", "enddate", "bsed", "bsled", "sysenddate");
        List<String> depValList = Arrays.asList("adminorg", "position", "job", "stdposition", "postype", "posstatus", "startdate", "enddate", "bsed", "bsled", "sysenddate", "businessstatus", "org", "affiliateadminorg", "empgroup", "positionvid", "jobvid", "stdpositionvid");
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("hrpi_employee", empValList);
        map.put("hrpi_empentrel", empValList);
        map.put("hrpi_depemp", depValList);
        map.put("hrpi_empposorgrel", depValList);
        map.put("hrpi_emporgrelall", depValList);
        map.put("hspm_ermanfile", depValList);
        PERSON3KEY = Collections.unmodifiableMap(map);
    }
}
