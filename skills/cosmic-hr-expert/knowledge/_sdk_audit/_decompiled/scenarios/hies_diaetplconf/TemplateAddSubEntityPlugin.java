/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.Row
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.designer.query.QueryEntityTreeBuildParameter
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.QueryEntityType
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.SaveAndNew
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.TextEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.bill.BillModel
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hies.common.HiesCommonRes
 *  kd.hr.hies.common.dto.CustomTabPage
 *  kd.hr.hies.common.dto.EntityFieldContext
 *  kd.hr.hies.common.enu.OprCategory
 *  kd.hr.hies.common.util.MethodUtil
 *  kd.hr.hies.common.util.TemplateEntityFieldUtil
 *  kd.hr.hies.common.util.TemplateFormCommonUtil
 *  kd.hr.hies.common.util.TemplateUtil
 */
package kd.hr.hies.formplugin;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.designer.query.QueryEntityTreeBuildParameter;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.QueryEntityType;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.SaveAndNew;
import kd.bos.entity.property.BasedataProp;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.TextEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.bill.BillModel;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hies.common.HiesCommonRes;
import kd.hr.hies.common.dto.CustomTabPage;
import kd.hr.hies.common.dto.EntityFieldContext;
import kd.hr.hies.common.enu.OprCategory;
import kd.hr.hies.common.util.MethodUtil;
import kd.hr.hies.common.util.TemplateEntityFieldUtil;
import kd.hr.hies.common.util.TemplateFormCommonUtil;
import kd.hr.hies.common.util.TemplateUtil;

@ExcludeFromJacocoGeneratedReport
public final class TemplateAddSubEntityPlugin
extends HRBaseDataCommonEdit
implements BeforeF7SelectListener {
    private static final Log LOG = LogFactory.getLog(TemplateAddSubEntityPlugin.class);
    private static final int ENTITY_UNIQUE_VAL = 500;
    public static final String FIELD_BILLHEAD = "billhead";
    public static final String ACTION_ENTITYOBJ_HELP = "closeCallBack_entityObj_Help";
    private static final String UNIQUE_SELECT_FORM = "hies_entityobjectfieldf7";
    private static final String FIELD_SELECT_FORM = "hies_subentityfieldf7";
    private static final String FIELD_RELATION_LEFT_PROP = "relationleftprop";
    private static final String FIELD_RELATION_LEFT_PROP_NAME = "relationleftpropname";
    private static final String FIELD_RELATION_RIGHT_PROP = "relationrightprop";
    private static final String FIELD_RELATION_RIGHT_PROP_NAME = "relationrightpropname";
    private static final String ENTITY_UNIQUE_VALUE = "entityuniqueval";
    private static final String ENTITY_UNIQUE_NAME = "entityuniquename";
    private static final String RELATION_CONDITION = "relationcondition";
    private static final String STATUS_VIEW = "VIEW";
    private static final String CUSTOMPARAM_TREENODES = "treenodes";
    private static final String CUSTOMPARAM_ROOTNODEID = "rootNodeId";
    private static final String CUSTOMPARAM_SELECTFIELDVALUE = "selectfieldvalue";
    private static final String CUSTOMPARAM_SELECTFIELDNAME = "selectfieldname";
    private static final String FIELD_SEQ = "seq";
    static final String KEY_OPKEY = "saveandnew";
    static final String KEY_OPKEY_SAVE = "save";

    public void afterLoadData(EventObject eventObject) {
        this.resetMainEntityUniqueVal();
    }

    public void afterCopyData(EventObject e) {
        this.resetMainEntityUniqueVal();
    }

    public void initialize() {
        super.initialize();
        this.addListener();
    }

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        BasedataEdit mainEntityBaseData = (BasedataEdit)this.getView().getControl("entity");
        mainEntityBaseData.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit mainQueryEntityBaseData = (BasedataEdit)this.getView().getControl("queryentity");
        mainQueryEntityBaseData.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit entryBaseData = (BasedataEdit)this.getView().getControl("rentity");
        entryBaseData.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        TextEdit entityUniqueNameText = (TextEdit)this.getView().getControl(ENTITY_UNIQUE_NAME);
        entityUniqueNameText.addClickListener((ClickListener)this);
        TextEdit relationLeftPropText = (TextEdit)this.getView().getControl(FIELD_RELATION_LEFT_PROP_NAME);
        relationLeftPropText.addClickListener((ClickListener)this);
        TextEdit relationRightPropText = (TextEdit)this.getView().getControl(FIELD_RELATION_RIGHT_PROP_NAME);
        relationRightPropText.addClickListener((ClickListener)this);
    }

    private void addListener() {
        this.addItemClickListeners(new String[]{"toolbar_subentity"});
        Toolbar roleToolBar = (Toolbar)this.getControl("toolbar_subentity");
        roleToolBar.addItemClickListener((ItemClickListener)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
    }

    public void afterBindData(EventObject e) {
        boolean isExport = "EXPT".equals(this.getModel().getValue("tmpltype"));
        if (isExport) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{ENTITY_UNIQUE_NAME});
        }
        this.getView().setVisible(Boolean.valueOf(false), new String[]{ENTITY_UNIQUE_VALUE});
        this.getView().setVisible(Boolean.valueOf(false), new String[]{FIELD_RELATION_LEFT_PROP});
        this.getView().setVisible(Boolean.valueOf(false), new String[]{FIELD_RELATION_RIGHT_PROP});
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entityrelation");
        if (entryEntity.size() > 0) {
            entryEntity.forEach(entryDyn -> {
                String rightProp;
                entryDyn.set(RELATION_CONDITION, (Object)"=");
                DynamicObject entryBiz = entryDyn.getDynamicObject("rentity");
                if (entryBiz == null) {
                    return;
                }
                StringBuilder uniqueFieldName = new StringBuilder();
                String uniqueFiledValue = entryDyn.getString(ENTITY_UNIQUE_VALUE);
                String entityNumber = entryBiz.getString("number");
                MainEntityType entityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
                DataEntityPropertyCollection properties = entityType.getProperties();
                Arrays.stream(uniqueFiledValue.split(",")).filter(filter -> HRStringUtils.isNotBlank((CharSequence)filter) && properties.containsKey(filter)).forEach(field -> uniqueFieldName.append(((IDataEntityProperty)properties.get(field)).getDisplayName() == null ? ((IDataEntityProperty)properties.get(field)).getName() : ((IDataEntityProperty)properties.get(field)).getDisplayName().getLocaleValue()).append("\uff1b"));
                entryDyn.set(ENTITY_UNIQUE_NAME, (Object)uniqueFieldName.toString());
                String leftProp = entryDyn.getString(FIELD_RELATION_LEFT_PROP);
                if (HRStringUtils.isNotBlank((CharSequence)leftProp)) {
                    entryDyn.set(FIELD_RELATION_LEFT_PROP_NAME, (Object)this.getDisplayName(leftProp));
                }
                if (HRStringUtils.isNotBlank((CharSequence)(rightProp = entryDyn.getString(FIELD_RELATION_RIGHT_PROP)))) {
                    entryDyn.set(FIELD_RELATION_RIGHT_PROP_NAME, (Object)this.getDisplayName(rightProp));
                }
            });
            this.getModel().setDataChanged(false);
        }
        this.initTemplateType();
        this.getView().updateView("entityrelation");
    }

    private String getDisplayName(String rightProp) {
        String displayName;
        String mainEntityNumber = "";
        String propName = "";
        String baseData = "";
        String subData = "";
        String[] split = rightProp.split("\\.");
        if (split.length == 1) {
            mainEntityNumber = rightProp;
            return "";
        }
        if (split.length == 2) {
            mainEntityNumber = split[0];
            propName = split[1];
        } else if (split.length == 3) {
            mainEntityNumber = split[0];
            baseData = split[1];
            propName = split[2];
        } else if (split.length == 4) {
            mainEntityNumber = split[0];
            baseData = split[1];
            subData = split[2];
            propName = split[3];
        }
        if (FIELD_BILLHEAD.equals(propName)) {
            return "";
        }
        MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)mainEntityNumber);
        DataEntityPropertyCollection mainEntityProperties = mainEntityType.getProperties();
        if (HRStringUtils.isNotBlank((CharSequence)subData)) {
            DataEntityPropertyCollection baseProperties = ((BasedataProp)mainEntityProperties.get((Object)baseData)).getComplexType().getProperties();
            BasedataProp subEntityProperty = (BasedataProp)baseProperties.get((Object)subData);
            String name = subEntityProperty.getBaseEntityId();
            DataEntityPropertyCollection properties = subEntityProperty.getComplexType().getProperties();
            propName = ((IDataEntityProperty)properties.get((Object)propName)).getDisplayName() == null ? ((IDataEntityProperty)properties.get((Object)propName)).getName() : ((IDataEntityProperty)properties.get((Object)propName)).getDisplayName().getLocaleValue();
            baseData = ((IDataEntityProperty)mainEntityProperties.get((Object)baseData)).getDisplayName() == null ? ((IDataEntityProperty)mainEntityProperties.get((Object)baseData)).getName() : ((IDataEntityProperty)mainEntityProperties.get((Object)baseData)).getDisplayName().getLocaleValue();
            subData = ((IDataEntityProperty)baseProperties.get((Object)subData)).getDisplayName() == null ? ((IDataEntityProperty)baseProperties.get((Object)subData)).getName() : ((IDataEntityProperty)baseProperties.get((Object)subData)).getDisplayName().getLocaleValue();
            displayName = baseData + "." + subData + "." + propName;
        } else if (HRStringUtils.isNotBlank((CharSequence)baseData)) {
            DataEntityPropertyCollection baseProperties = ((BasedataProp)mainEntityProperties.get((Object)baseData)).getComplexType().getProperties();
            propName = ((IDataEntityProperty)baseProperties.get((Object)propName)).getDisplayName() == null ? ((IDataEntityProperty)baseProperties.get((Object)propName)).getName() : ((IDataEntityProperty)baseProperties.get((Object)propName)).getDisplayName().getLocaleValue();
            baseData = ((IDataEntityProperty)mainEntityProperties.get((Object)baseData)).getDisplayName() == null ? ((IDataEntityProperty)mainEntityProperties.get((Object)baseData)).getName() : ((IDataEntityProperty)mainEntityProperties.get((Object)baseData)).getDisplayName().getLocaleValue();
            displayName = baseData + "." + propName;
        } else {
            displayName = propName = ((IDataEntityProperty)mainEntityProperties.get((Object)propName)).getDisplayName() == null ? ((IDataEntityProperty)mainEntityProperties.get((Object)propName)).getName() : ((IDataEntityProperty)mainEntityProperties.get((Object)propName)).getDisplayName().getLocaleValue();
        }
        return displayName;
    }

    private void resetMainEntityUniqueVal() {
        ComboEdit combo = (ComboEdit)this.getControl("mainentityuniqueval");
        Object mainEntityTmplObj = this.getModel().getValue("entity");
        TemplateFormCommonUtil.resetMainEntityUniqueVal((ComboEdit)combo, (Object)mainEntityTmplObj);
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        MainEntityType type;
        String itemKey;
        switch (itemKey = evt.getItemKey()) {
            case "subentity_del": {
                EntryGrid entryGrid;
                int[] selectRows;
                DynamicObjectCollection subEntityCollection;
                int allDataSize;
                DynamicObject mainDyObj = (DynamicObject)this.getModel().getValue("queryentity");
                if (Objects.isNull(mainDyObj)) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u5b9e\u4f53", (String)"TemplateConfPlugin_5", (String)"hrmp-hies-import", (Object[])new Object[0]));
                    evt.setCancel(true);
                    return;
                }
                String mainEntityNumber = mainDyObj.getString("number");
                MainEntityType mainType = EntityMetadataCache.getDataEntityType((String)mainEntityNumber);
                if (!(mainType instanceof QueryEntityType) || (allDataSize = (subEntityCollection = this.getModel().getEntryEntity("entityrelation")).size()) != (selectRows = (entryGrid = (EntryGrid)this.getView().getControl("entityrelation")).getSelectRows()).length) break;
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u591a\u5b9e\u4f53\u6a21\u677f\u9700\u81f3\u5c11\u5173\u8054\u4e00\u4e2a\u5b50\u5b9e\u4f53", (String)"TemplateConfPlugin_7", (String)"hrmp-hies-import", (Object[])new Object[0]));
                evt.setCancel(true);
                return;
            }
        }
        if (!KEY_OPKEY_SAVE.equals(evt.getOperationKey()) && !KEY_OPKEY.equals(evt.getOperationKey())) {
            return;
        }
        boolean isImport = "IMPT".equals(this.getModel().getValue("tmpltype"));
        DynamicObject bizobject = (DynamicObject)this.getModel().getValue("queryentity");
        String mainEntityNumber = "";
        boolean isQueryEntity = false;
        if (Objects.nonNull(bizobject) && (type = EntityMetadataCache.getDataEntityType((String)(mainEntityNumber = Objects.nonNull(bizobject) ? bizobject.getString("number") : ""))) instanceof QueryEntityType) {
            isQueryEntity = true;
        }
        Object importType = this.getModel().getValue("importtype");
        String importTypeName = ((ComboEdit)this.getView().getControl("importtype")).getProperty().getDisplayName().getLocaleValue();
        if (Objects.isNull(importType) && isImport) {
            this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u8bf7\u586b\u5199%s\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_9.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]), importTypeName));
            evt.setCancel(true);
            return;
        }
        Object entityUnique = this.getModel().getValue("mainentityuniqueval");
        boolean canEdit = TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)this.getModel());
        if (Objects.nonNull(entityUnique) && entityUnique.toString().length() > 500) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u201c\u9700\u66f4\u65b0\u8bb0\u5f55\u8bc6\u522b\u5b57\u6bb5\u201d\u8d85\u8fc7\u6700\u5927\u957f\u5ea6\u9650\u5236\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_1.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
            evt.setCancel(true);
            return;
        }
        if (Objects.isNull(entityUnique) && isImport && canEdit) {
            Object value = this.getModel().getValue("mainentityuniqueval");
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u201c\u9700\u66f4\u65b0\u8bb0\u5f55\u8bc6\u522b\u5b57\u6bb5\u201d\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_8.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
            evt.setCancel(true);
            return;
        }
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entityrelation");
        String importtype = (String)this.getModel().getValue("importtype");
        boolean isNew = OprCategory.isNew((String)importtype);
        StringBuilder sb = new StringBuilder();
        for (DynamicObject dynamicObject : entryEntity) {
            String msg;
            String entityUniqueVal = dynamicObject.getString(ENTITY_UNIQUE_VALUE);
            if (!isNew && isImport && HRStringUtils.isBlank((CharSequence)entityUniqueVal)) {
                msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u201c%1$s\u201d\u7684\u9700\u66f4\u65b0\u8bb0\u5f55\u8bc6\u522b\u5b57\u6bb5\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_11.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]), dynamicObject.getString("rentity.name").concat("(").concat(dynamicObject.getString("rentity.number")).concat(")"));
                sb.append(msg).append("\r\n");
            }
            if (HRStringUtils.isNotBlank((CharSequence)entityUniqueVal) && entityUniqueVal.length() > 500) {
                msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u201c%1$s\u201d\u7684\u9700\u66f4\u65b0\u8bb0\u5f55\u8bc6\u522b\u5b57\u6bb5\u8d85\u8fc7\u6700\u5927\u957f\u5ea6\u9650\u5236\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_3.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]), dynamicObject.getString("rentity.name").concat("(").concat(dynamicObject.getString("rentity.number")).concat(")"));
                sb.append(msg).append("\r\n");
            }
            if (TemplateAddSubEntityPlugin.isMustInput(isQueryEntity, isImport, dynamicObject)) {
                msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u8bf7\u8bbe\u7f6e\u5b50\u5b9e\u4f53\u5217\u8868\u7b2c%s\u884c\u4e0e\u4e3b\u5b9e\u4f53\u5173\u8054\u6761\u4ef6\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_2.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]), dynamicObject.getString("rentity.name").concat("(").concat(dynamicObject.getString("rentity.number")).concat(")"));
                sb.append(msg).append("\r\n");
            }
            if (ObjectUtils.isEmpty((Object)dynamicObject.getString(FIELD_RELATION_LEFT_PROP_NAME))) {
                dynamicObject.set(FIELD_RELATION_LEFT_PROP, (Object)"");
            }
            if (!ObjectUtils.isEmpty((Object)dynamicObject.getString(FIELD_RELATION_RIGHT_PROP_NAME))) continue;
            dynamicObject.set(FIELD_RELATION_RIGHT_PROP, (Object)"");
        }
        if (!ObjectUtils.isEmpty((Object)sb.toString())) {
            this.getView().showTipNotification(sb.toString());
            evt.setCancel(true);
        }
    }

    public void itemClick(ItemClickEvent evt) {
        String itemKey;
        switch (itemKey = evt.getItemKey()) {
            case "subentity_del": {
                TemplateFormCommonUtil.displayEnablePersonIdImport((IFormView)this.getView());
                break;
            }
        }
    }

    private static boolean isMustInput(boolean isQueryEntity, boolean isImport, DynamicObject dynamicObject) {
        return !isImport && !isQueryEntity && (HRStringUtils.isBlank((CharSequence)dynamicObject.getString(FIELD_RELATION_LEFT_PROP)) || HRStringUtils.isBlank((CharSequence)dynamicObject.getString(FIELD_RELATION_RIGHT_PROP)));
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (formOperate instanceof SaveAndNew && HRStringUtils.equals((String)KEY_OPKEY, (String)((SaveAndNew)formOperate).getOriOperateKey()) && args.getOperationResult() != null && args.getOperationResult().isSuccess()) {
            TemplateFormCommonUtil.removeTabPage((IFormView)this.getView());
        }
    }

    private void initTemplateType() {
        boolean isImportTpl = "IMPT".equals(this.getModel().getValue("tmpltype"));
        ((TextEdit)this.getControl(FIELD_RELATION_LEFT_PROP)).setMustInput(!isImportTpl);
        ((TextEdit)this.getControl(FIELD_RELATION_RIGHT_PROP)).setMustInput(!isImportTpl);
        ((ComboEdit)this.getControl("importtype")).setMustInput(isImportTpl);
        this.initUnique();
    }

    private void initUnique() {
        boolean isImportTpl = "IMPT".equals(this.getModel().getValue("tmpltype"));
        String importType = (String)this.getModel().getValue("importtype");
        boolean isNew = OprCategory.isNew((String)importType);
        boolean isUniqueMust = !isNew && isImportTpl;
        ((ComboEdit)this.getControl("mainentityuniqueval")).setMustInput(isUniqueMust);
        ((TextEdit)this.getControl(ENTITY_UNIQUE_NAME)).setMustInput(isUniqueMust);
    }

    public void click(EventObject evt) {
        super.click(evt);
        if (evt.getSource() instanceof TextEdit) {
            TextEdit source = (TextEdit)evt.getSource();
            String entryKey = source.getEntryKey();
            if (source.getView().getFormShowParameter().getStatus().name().equalsIgnoreCase(STATUS_VIEW)) {
                return;
            }
            if ("entityrelation".equals(entryKey)) {
                EntryGrid entryGrid = (EntryGrid)this.getView().getControl("entityrelation");
                int[] selectRows = entryGrid.getSelectRows();
                if (selectRows == null || selectRows.length == 0) {
                    return;
                }
                int rowNum = selectRows[0];
                String fieldKey = source.getKey();
                if (HRStringUtils.equals((String)ENTITY_UNIQUE_NAME, (String)fieldKey)) {
                    this.openSelectFieldF7(rowNum, source);
                } else if (HRStringUtils.equals((String)FIELD_RELATION_LEFT_PROP_NAME, (String)fieldKey)) {
                    DynamicObject dataEntity = source.getModel().getEntryRowEntity("entityrelation", rowNum);
                    if (Objects.isNull(dataEntity.getDynamicObject("rentity"))) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u5b50\u5b9e\u4f53\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_0.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
                    } else {
                        DynamicObject dynamicObject = (DynamicObject)dataEntity.get("rentity");
                        String number = dynamicObject.getString("number");
                        String name = dynamicObject.getString("name");
                        DynamicObject entryDy = this.getModel().getEntryRowEntity("entityrelation", rowNum);
                        String caption = ResManager.loadKDString((String)"\u672c\u5b9e\u4f53\u5b57\u6bb5\u9009\u62e9", (String)HiesCommonRes.TemplateAddSubEntityPlugin_13.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]);
                        this.showFieldForm(this.buildTreeNodesByEntityType(number, ""), entryDy, number, name, "relationleftprop_" + rowNum, caption);
                    }
                } else if (HRStringUtils.equals((String)FIELD_RELATION_RIGHT_PROP_NAME, (String)fieldKey)) {
                    DynamicObject dynamicObject = (DynamicObject)this.getModel().getValue("entity");
                    if (Objects.isNull(this.getModel().getValue("entity"))) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u5b9e\u4f53\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_4.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
                        return;
                    }
                    String number = dynamicObject.getString("number");
                    String name = dynamicObject.getString("name");
                    DynamicObject entryDy = this.getModel().getEntryRowEntity("entityrelation", rowNum);
                    String caption = ResManager.loadKDString((String)"\u4e3b\u5b9e\u4f53\u5b57\u6bb5\u9009\u62e9", (String)HiesCommonRes.TemplateAddSubEntityPlugin_12.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]);
                    this.showFieldForm(this.buildTreeNodesByEntityType(number, ""), entryDy, number, "", "relationrightprop_" + rowNum, caption);
                }
            }
        }
    }

    private void openSelectFieldF7(int rowIndex, TextEdit textEditCol) {
        DynamicObject entity = (DynamicObject)this.getModel().getValue("rentity", rowIndex);
        if (null == entity) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u5173\u8054\u5b50\u5b9e\u4f53\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_6.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
            return;
        }
        String entityNumber = entity.getString("number");
        MainEntityType entityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        TreeNode fieldTreeNode = new TreeNode("", entityType.getName(), entityType.getDisplayName().toString());
        fieldTreeNode.setIsOpened(true);
        fieldTreeNode.addChild(new TreeNode(entityType.getName(), "id", ResManager.loadKDString((String)"\u5185\u7801", (String)HiesCommonRes.TemplateFieldConfPlugin_2.resId(), (String)"hrmp-hies-common", (Object[])new Object[0])));
        for (IDataEntityProperty prop : entityType.getProperties()) {
            if (MethodUtil.ignoreUniqueValField((IDataEntityProperty)prop, (boolean)true)) continue;
            fieldTreeNode.addChild(new TreeNode(entityType.getName(), prop.getName(), prop.getDisplayName().getLocaleValue()));
        }
        DynamicObject entryDy = textEditCol.getModel().getEntryRowEntity("entityrelation", rowIndex);
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId(UNIQUE_SELECT_FORM);
        String nodesJson = SerializationUtils.toJsonString((Object)fieldTreeNode);
        showParameter.getCustomParams().put(CUSTOMPARAM_TREENODES, nodesJson);
        showParameter.getCustomParams().put(CUSTOMPARAM_ROOTNODEID, entityType.getName());
        showParameter.getCustomParams().put(CUSTOMPARAM_SELECTFIELDVALUE, entryDy.getString(ENTITY_UNIQUE_VALUE));
        showParameter.getCustomParams().put(CUSTOMPARAM_SELECTFIELDNAME, entryDy.getString(ENTITY_UNIQUE_NAME));
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "entityuniquename_" + rowIndex));
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setCaption(ResManager.loadKDString((String)"\u9700\u66f4\u65b0\u8bb0\u5f55\u8bc6\u522b\u5b57\u6bb5", (String)HiesCommonRes.TemplateAddSubEntityPlugin_7.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
        this.getView().showForm(showParameter);
    }

    private void showFieldForm(TreeNode entityFieldTreeNode, DynamicObject entryDy, String entityNumber, String entityAlias, String actionId, String caption) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId(FIELD_SELECT_FORM);
        String nodesJson = SerializationUtils.toJsonString((Object)entityFieldTreeNode);
        showParameter.getCustomParams().put(CUSTOMPARAM_TREENODES, nodesJson);
        showParameter.getCustomParams().put("entityalias", entityAlias);
        showParameter.getCustomParams().put("entitynumber", entityNumber);
        showParameter.getCustomParams().put("rowindex", 0);
        showParameter.getCustomParams().put(CUSTOMPARAM_SELECTFIELDVALUE, entryDy.getString(ENTITY_UNIQUE_VALUE));
        showParameter.getCustomParams().put(CUSTOMPARAM_SELECTFIELDNAME, entryDy.getString(ENTITY_UNIQUE_NAME));
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, actionId));
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setCaption(caption);
        this.getView().showForm(showParameter);
    }

    private TreeNode buildTreeNodesByEntityType(String entityNumber, String parentId) {
        boolean isImport = "IMPT".equals(this.getModel().getValue("tmpltype"));
        MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        QueryEntityTreeBuildParameter parameter = new QueryEntityTreeBuildParameter(mainEntityType);
        parameter.setIncludePKField(false);
        TreeNode childNode = TemplateEntityFieldUtil.buildBillTreeNodes((IFormView)this.getView(), (QueryEntityTreeBuildParameter)parameter, (boolean)false, (String)"", (boolean)isImport, null);
        childNode.setParentid(parentId);
        return childNode;
    }

    public void afterCreateNewData(EventObject evt) {
        this.resetMainEntityUniqueVal();
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String fieldKey = beforeF7SelectEvent.getProperty().getName();
        if (HRStringUtils.equals((String)fieldKey, (String)"entity") || HRStringUtils.equals((String)fieldKey, (String)"queryentity")) {
            ArrayList idList = Lists.newArrayListWithExpectedSize((int)16);
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entityrelation");
            ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            showParameter.setCaption(ResManager.loadKDString((String)"\u5b9e\u4f53\u9009\u62e9\u5217\u8868", (String)HiesCommonRes.TemplateAddSubEntityPlugin_10.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
            showParameter.getListFilterParameter().setFilter(new QFilter("number", "in", (Object)TemplateUtil.getValidEntityNumbers((boolean)true)));
            if (entryEntity.size() > 0) {
                entryEntity.stream().filter(dynamicObject -> Objects.nonNull(dynamicObject.getDynamicObject("rentity"))).forEach(object -> idList.add(object.getDynamicObject("rentity").getPkValue()));
                showParameter.getListFilterParameter().setFilter(new QFilter("id", "not in", (Object)idList));
            }
        } else if (HRStringUtils.equals((String)fieldKey, (String)"rentity")) {
            EntryGrid entryGrid;
            int[] selectRows;
            DynamicObject mainEntity = (DynamicObject)this.getModel().getValue("entity");
            HashSet businessObjIds = Sets.newHashSetWithExpectedSize((int)16);
            if (Objects.nonNull(mainEntity)) {
                businessObjIds.add(mainEntity.get("id"));
            }
            DynamicObjectCollection relTmpEntities = this.getModel().getEntryEntity("entityrelation");
            for (DynamicObject relTmp : relTmpEntities) {
                DynamicObject relTmpObj = relTmp.getDynamicObject("rentity");
                if (!Objects.nonNull(relTmpObj)) continue;
                businessObjIds.add(relTmpObj.get("id"));
            }
            ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            showParameter.getListFilterParameter().setFilter(new QFilter("number", "in", this.getBillNumbersExQueryList()));
            if (!businessObjIds.isEmpty()) {
                showParameter.getListFilterParameter().setFilter(new QFilter("id", "not in", (Object)businessObjIds));
            }
            if ((selectRows = (entryGrid = (EntryGrid)this.getView().getControl("entityrelation")).getSelectRows()) == null || selectRows.length == 0) {
                return;
            }
            int rowNum = selectRows[0];
            showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "rentity_" + rowNum));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<String> getBillNumbersExQueryList() {
        String fields = "number";
        String orderby = "bizappid asc";
        Object[] validEntityNumberFilter = TemplateUtil.getValidEntityNumberFilter((boolean)false);
        List filters = MethodUtil.arrayToList((Object[])validEntityNumberFilter);
        ArrayList<String> billNumbers = new ArrayList<String>();
        try (DataSet ds = ORM.create().queryDataSet("bos_entityobject", "bos_entityobject", fields, filters.toArray(new QFilter[0]), orderby);){
            for (Row row : ds) {
                billNumbers.add(row.getString("number"));
            }
        }
        LOG.info("TemplateAddSubEntityPlugin.getBillNumbers billNumbers:{}", billNumbers);
        return billNumbers;
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String propName = args.getProperty().getName();
        ChangeData[] changeSet = args.getChangeSet();
        Object newValue = changeSet[0].getNewValue();
        int row = args.getChangeSet()[0].getRowIndex();
        IDataModel model = this.getModel();
        String value = (String)model.getValue("tmpltype");
        switch (propName) {
            case "entity": {
                this.resetMainEntityUniqueVal();
                DynamicObject oldMainValue = (DynamicObject)changeSet[0].getOldValue();
                if (!Objects.nonNull(oldMainValue)) break;
                this.clearRightFieldValue(oldMainValue.getString("number"));
                break;
            }
            case "tmpltype": {
                this.initTemplateType();
                break;
            }
            case "importtype": {
                this.initUnique();
                break;
            }
            case "rentity": {
                int rowIndex = changeSet[0].getRowIndex();
                this.getModel().setValue(ENTITY_UNIQUE_VALUE, (Object)"", rowIndex);
                this.getModel().setValue(ENTITY_UNIQUE_NAME, (Object)"", rowIndex);
                this.getModel().setValue(FIELD_RELATION_LEFT_PROP, (Object)"", rowIndex);
                this.getModel().setValue(FIELD_RELATION_LEFT_PROP_NAME, (Object)"", rowIndex);
                DynamicObject oldRelValue = (DynamicObject)changeSet[0].getOldValue();
                if (!Objects.nonNull(oldRelValue)) break;
                this.clearRightFieldValue(oldRelValue.getString("id"));
                break;
            }
            case "entityuniquename": {
                if (!HRStringUtils.isBlank((CharSequence)((String)newValue))) break;
                String oldUniqueVal = (String)this.getModel().getValue(ENTITY_UNIQUE_VALUE);
                this.getModel().setValue(ENTITY_UNIQUE_VALUE, newValue, row);
                DynamicObject dynObject = (DynamicObject)this.getModel().getEntryEntity("entityrelation").get(row);
                DynamicObject dynamicObject = dynObject.getDynamicObject("rentity");
                String entityNumber = dynamicObject.getString("id");
                TemplateFormCommonUtil.refreshTree4UniqueVal((IFormView)this.getView(), (String)entityNumber, (String)"", (String)oldUniqueVal);
                TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)entityNumber);
                TemplateFormCommonUtil.refreshTabPage4UniqueVal((IFormView)this.getView(), (String)entityNumber, (String)"", (String)oldUniqueVal);
                break;
            }
            case "relationleftpropname": {
                if (!HRStringUtils.isBlank((CharSequence)((String)newValue))) break;
                String oldUniqueVal = (String)this.getModel().getValue(FIELD_RELATION_LEFT_PROP);
                if (HRStringUtils.isNotBlank((CharSequence)oldUniqueVal)) {
                    String[] split = oldUniqueVal.split("\\.");
                    if (split.length == 3) {
                        oldUniqueVal = split[1];
                    } else if (split.length == 2) {
                        oldUniqueVal = split[split.length - 1];
                    }
                }
                this.getModel().setValue(FIELD_RELATION_LEFT_PROP, newValue, row);
                DynamicObject dynObject = (DynamicObject)this.getModel().getEntryEntity("entityrelation").get(row);
                DynamicObject dynamicObject = dynObject.getDynamicObject("rentity");
                String entityNumber = dynamicObject.getString("id");
                TemplateFormCommonUtil.refreshTabPage((IFormView)this.getView(), (String)entityNumber, (String)"", (String)oldUniqueVal, (Boolean)Boolean.FALSE);
                TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)entityNumber);
                TemplateFormCommonUtil.refreshTree((IFormView)this.getView(), (String)entityNumber, (String)"", (String)oldUniqueVal);
                break;
            }
            case "relationrightpropname": {
                if (!HRStringUtils.isBlank((CharSequence)((String)newValue))) break;
                String oldUniqueVal = (String)this.getModel().getValue(FIELD_RELATION_RIGHT_PROP);
                if (HRStringUtils.isNotBlank((CharSequence)oldUniqueVal)) {
                    String[] split = oldUniqueVal.split("\\.");
                    if (split.length == 3) {
                        oldUniqueVal = split[1];
                    } else if (split.length == 2) {
                        oldUniqueVal = split[split.length - 1];
                    }
                }
                this.getModel().setValue(FIELD_RELATION_RIGHT_PROP, newValue, row);
                DynamicObject mainObj = (DynamicObject)this.getModel().getValue("entity");
                String entityNumber = mainObj.getString("number");
                TemplateFormCommonUtil.refreshTabPage((IFormView)this.getView(), (String)entityNumber, (String)"", (String)oldUniqueVal, (Boolean)Boolean.FALSE);
                TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)entityNumber);
                TemplateFormCommonUtil.refreshTree((IFormView)this.getView(), (String)entityNumber, (String)"", (String)oldUniqueVal);
                break;
            }
            case "rentityalias": {
                DynamicObject mainObj = (DynamicObject)model.getValue("entity");
                String entityNumber = mainObj.getString("number");
                MainEntityType mainType = EntityMetadataCache.getDataEntityType((String)entityNumber);
                if (!TemplateFormCommonUtil.checkQueryEntity((IFormView)this.getView(), (MainEntityType)mainType)) {
                    this.getModel().setValue("queryentity", (Object)"");
                    return;
                }
                TemplateFormCommonUtil.removeAllTabpage((IFormView)this.getView(), (Map)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()));
                TemplateEntityFieldUtil.buildAndRestoreFieldTree((IFormView)this.getView(), (IDataModel)model, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()), (OperationStatus)OperationStatus.EDIT);
                DynamicObject curDynObj = (DynamicObject)this.getModel().getValue("rentity");
                TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)curDynObj.getString("id"));
                break;
            }
        }
    }

    private void clearRightFieldValue(String oldObjNumber) {
        this.getModel().getEntryEntity("entityrelation").stream().filter(row -> HRStringUtils.isNotEmpty((String)row.getString(FIELD_RELATION_RIGHT_PROP)) && row.getString(FIELD_RELATION_RIGHT_PROP).contains(oldObjNumber)).forEach(row -> {
            row.set(FIELD_RELATION_RIGHT_PROP, (Object)"");
            row.set(FIELD_RELATION_RIGHT_PROP_NAME, (Object)"");
        });
        this.getView().updateView("entityrelation");
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        IDataModel model = this.getModel();
        String actionId = closedCallBackEvent.getActionId();
        DynamicObject mainDyObj = (DynamicObject)this.getModel().getValue("entity");
        String mainEntityNumber = "";
        if (Objects.nonNull(mainDyObj)) {
            mainEntityNumber = mainDyObj.getString("number");
        }
        if (ACTION_ENTITYOBJ_HELP.equals(actionId)) {
            Object returnData = closedCallBackEvent.getReturnData();
            if (!(returnData instanceof ListSelectedRowCollection)) {
                return;
            }
            HashMap<String, String> subEntityInfos = new HashMap<String, String>(16);
            ListSelectedRowCollection lsrc = (ListSelectedRowCollection)returnData;
            for (ListSelectedRow listSelectedRow : lsrc) {
                Object pkValue = listSelectedRow.getPrimaryKeyValue();
                String number = listSelectedRow.getNumber();
                String dimObjName = listSelectedRow.getName();
                subEntityInfos.put(pkValue + ":" + number, dimObjName);
            }
            this.fillSubEntityList(subEntityInfos);
            TemplateFormCommonUtil.displayEnablePersonIdImport((IFormView)this.getView());
        } else if (actionId.contains(FIELD_RELATION_LEFT_PROP)) {
            String[] keys = actionId.split("_");
            DynamicObjectCollection dynColl = this.getModel().getEntryEntity("entityrelation");
            int selectRowIndex = Integer.parseInt(keys[1]);
            DynamicObject dynObject = (DynamicObject)dynColl.get(selectRowIndex);
            HashMap returnData = (HashMap)closedCallBackEvent.getReturnData();
            if (!ObjectUtils.isEmpty((Object)returnData)) {
                String[] split;
                DynamicObject dynamicObject = dynObject.getDynamicObject("rentity");
                String entityNumber = dynamicObject.getString("id");
                String oldRelationLeftProp = dynObject.getString(FIELD_RELATION_LEFT_PROP);
                String newRelationLeftProp = (String)returnData.get("currNodeId");
                dynObject.set(FIELD_RELATION_LEFT_PROP_NAME, (Object)this.getDisplayName((String)returnData.get("currNodeId")));
                dynObject.set(FIELD_RELATION_LEFT_PROP, returnData.get("currNodeId"));
                if (HRStringUtils.isNotBlank((CharSequence)oldRelationLeftProp)) {
                    split = oldRelationLeftProp.split("\\.");
                    if (split.length == 3) {
                        oldRelationLeftProp = split[1];
                    } else if (split.length == 2) {
                        oldRelationLeftProp = split[split.length - 1];
                    }
                }
                if (HRStringUtils.isNotBlank((CharSequence)newRelationLeftProp)) {
                    split = newRelationLeftProp.split("\\.");
                    if (split.length == 3) {
                        newRelationLeftProp = split[1];
                    } else if (split.length == 2) {
                        newRelationLeftProp = split[split.length - 1];
                    }
                }
                List<Object> cancelRelationLeftProp = new ArrayList(16);
                if (HRStringUtils.isNotBlank((CharSequence)oldRelationLeftProp) && HRStringUtils.isNotBlank((CharSequence)newRelationLeftProp)) {
                    List oldLeftPropList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)oldRelationLeftProp).collect(Collectors.toList());
                    List newLeftPropList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)newRelationLeftProp).collect(Collectors.toList());
                    oldLeftPropList.removeAll(newLeftPropList);
                    cancelRelationLeftProp = oldLeftPropList;
                }
                String cancelUniqueVal = Joiner.on((String)",").join(cancelRelationLeftProp);
                String mustInputField = TemplateFormCommonUtil.getEntityMustInputField((IDataModel)model, (String)entityNumber);
                TemplateFormCommonUtil.refreshTabPage((IFormView)this.getView(), (String)entityNumber, (String)mustInputField, (String)cancelUniqueVal, (Boolean)Boolean.FALSE);
                TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)entityNumber);
                TemplateFormCommonUtil.refreshTree((IFormView)this.getView(), (String)entityNumber, (String)mustInputField, (String)cancelUniqueVal);
            }
            this.getModel().updateEntryCache(dynColl);
            this.getView().updateView("entityrelation");
        } else if (actionId.contains(FIELD_RELATION_RIGHT_PROP)) {
            String[] keys = actionId.split("_");
            DynamicObjectCollection dynColl = this.getModel().getEntryEntity("entityrelation");
            int selectRowIndex = Integer.parseInt(keys[1]);
            DynamicObject dynObject = (DynamicObject)dynColl.get(selectRowIndex);
            HashMap returnData = (HashMap)closedCallBackEvent.getReturnData();
            if (!ObjectUtils.isEmpty((Object)returnData)) {
                String[] split;
                String oldRelationLeftProp = dynObject.getString(FIELD_RELATION_RIGHT_PROP);
                String newRelationLeftProp = (String)returnData.get("currNodeId");
                dynObject.set(FIELD_RELATION_RIGHT_PROP_NAME, (Object)this.getDisplayName((String)returnData.get("currNodeId")));
                dynObject.set(FIELD_RELATION_RIGHT_PROP, returnData.get("currNodeId"));
                if (HRStringUtils.isNotBlank((CharSequence)oldRelationLeftProp)) {
                    split = oldRelationLeftProp.split("\\.");
                    if (split.length == 3) {
                        oldRelationLeftProp = split[1];
                    } else if (split.length == 2) {
                        oldRelationLeftProp = split[split.length - 1];
                    }
                }
                if (HRStringUtils.isNotBlank((CharSequence)newRelationLeftProp)) {
                    split = newRelationLeftProp.split("\\.");
                    if (split.length == 3) {
                        newRelationLeftProp = split[1];
                    } else if (split.length == 2) {
                        newRelationLeftProp = split[split.length - 1];
                    }
                }
                List<Object> cancelRelationLeftProp = new ArrayList(16);
                if (HRStringUtils.isNotBlank((CharSequence)oldRelationLeftProp) && HRStringUtils.isNotBlank((CharSequence)newRelationLeftProp)) {
                    List oldLeftPropList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)oldRelationLeftProp).collect(Collectors.toList());
                    List newLeftPropList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)newRelationLeftProp).collect(Collectors.toList());
                    oldLeftPropList.removeAll(newLeftPropList);
                    cancelRelationLeftProp = oldLeftPropList;
                }
                String cancelUniqueVal = Joiner.on((String)",").join(cancelRelationLeftProp);
                String mustInputField = TemplateFormCommonUtil.getEntityMustInputField((IDataModel)model, (String)mainEntityNumber);
                TemplateFormCommonUtil.refreshTabPage((IFormView)this.getView(), (String)mainEntityNumber, (String)mustInputField, (String)cancelUniqueVal, (Boolean)Boolean.FALSE);
                TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)mainEntityNumber);
                TemplateFormCommonUtil.refreshTree((IFormView)this.getView(), (String)mainEntityNumber, (String)mustInputField, (String)cancelUniqueVal);
            }
            this.getModel().updateEntryCache(dynColl);
            this.getView().updateView("entityrelation");
        } else if (actionId.contains(ENTITY_UNIQUE_NAME)) {
            DynamicObjectCollection dynColl = this.getModel().getEntryEntity("entityrelation");
            int selectRowIndex = Integer.parseInt(actionId.split("_")[1]);
            DynamicObject dynObject = (DynamicObject)dynColl.get(selectRowIndex);
            HashMap returnData = (HashMap)closedCallBackEvent.getReturnData();
            if (returnData != null) {
                String oldUnique = dynObject.getString(ENTITY_UNIQUE_VALUE);
                String uniqueval = (String)returnData.get("currNodeIds");
                dynObject.set(ENTITY_UNIQUE_NAME, returnData.get("currNodeNames"));
                dynObject.set(ENTITY_UNIQUE_VALUE, (Object)uniqueval);
                DynamicObject dynamicObject = dynObject.getDynamicObject("rentity");
                if (Objects.nonNull(dynamicObject)) {
                    String entityNumber = dynamicObject.getString("id");
                    String newUnique = uniqueval;
                    List<Object> cancelUnique = new ArrayList(16);
                    if (HRStringUtils.isNotBlank((CharSequence)oldUnique) && HRStringUtils.isNotBlank((CharSequence)newUnique)) {
                        List oldUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)oldUnique).collect(Collectors.toList());
                        List newUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)newUnique).collect(Collectors.toList());
                        oldUniqueList.removeAll(newUniqueList);
                        cancelUnique = oldUniqueList;
                    }
                    String cancelUniqueVal = Joiner.on((String)",").join(cancelUnique);
                    String mustInputField = TemplateFormCommonUtil.getEntityMustInputField((IDataModel)model, (String)entityNumber);
                    TemplateFormCommonUtil.refreshTabPage4UniqueVal((IFormView)this.getView(), (String)entityNumber, (String)mustInputField, (String)cancelUniqueVal);
                    TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)entityNumber);
                    TemplateFormCommonUtil.refreshTree4UniqueVal((IFormView)this.getView(), (String)entityNumber, (String)mustInputField, (String)cancelUniqueVal);
                }
                this.getModel().updateEntryCache(dynColl);
                this.getView().updateView("entityrelation");
            }
        }
    }

    private void fillSubEntityList(Map<String, String> subEntityInfos) {
        int[] rows;
        if (CollectionUtils.isEmpty(subEntityInfos)) {
            return;
        }
        String value = (String)this.getModel().getValue("tmpltype");
        boolean isImport = "IMPT".equalsIgnoreCase(value);
        String downloadCond = (String)this.getModel().getValue("enabledowncond");
        boolean isDownloadCond = "1".equals(downloadCond);
        boolean isOpen = TemplateEntityFieldUtil.mustInputIsOpen((IDataModel)this.getModel());
        boolean canEdit = TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)this.getModel());
        boolean baseCanEdit = TemplateEntityFieldUtil.baseCanEdit((IDataModel)this.getModel());
        String uniqueval = (String)this.getModel().getValue("mainentityuniqueval");
        DynamicObject queryBizobject = (DynamicObject)this.getModel().getValue("queryentity");
        String queryEntityNumber = queryBizobject.getString("number");
        this.getModel().beginInit();
        TableValueSetter vs = new TableValueSetter(new String[0]);
        vs.addField("rentity", new Object[0]);
        for (Map.Entry<String, String> entry : subEntityInfos.entrySet()) {
            String[] split = entry.getKey().split(":");
            String entityId = split[0];
            String entityNumber = split[1];
            String entityName = entry.getValue();
            TemplateFormCommonUtil.addTabPage((IFormView)this.getView(), (CustomTabPage)new CustomTabPage(entityId, entityName), (String)entityNumber, (String)queryEntityNumber, (boolean)isImport, (boolean)isDownloadCond, (Boolean)isOpen, (Boolean)Boolean.TRUE, (Boolean)canEdit, (Boolean)baseCanEdit, (String)uniqueval, (OperationStatus)OperationStatus.EDIT);
            EntityFieldContext entityFieldContext = new EntityFieldContext(this.getView(), entityNumber, "tpltreeentryentity", isImport, isOpen, canEdit, baseCanEdit, uniqueval);
            TemplateEntityFieldUtil.newInstance().addTreeEntryRow(entityFieldContext);
            vs.addRow(new Object[]{entityId});
        }
        for (int row : rows = ((AbstractFormDataModel)this.getModel()).batchCreateNewEntryRow("entityrelation", vs)) {
            DynamicObject entryEntity = ((BillModel)this.getModel()).getEntryEntity("entityrelation", row);
            entryEntity.getDataEntityState().setBizChanged(true);
            entryEntity.getDataEntityState().setBizChangeFlags(new long[]{1L});
        }
        this.getModel().endInit();
        this.getView().updateView("entityrelation");
    }

    static /* synthetic */ IDataModel access$000(TemplateAddSubEntityPlugin x0) {
        return x0.getModel();
    }

    static /* synthetic */ IDataModel access$100(TemplateAddSubEntityPlugin x0) {
        return x0.getModel();
    }

    static /* synthetic */ List access$200(TemplateAddSubEntityPlugin x0) {
        return x0.getBillNumbersExQueryList();
    }
}
