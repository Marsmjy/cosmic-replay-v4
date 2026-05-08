/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.base.Splitter
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.Row
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.EntityType
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.SaveAndNew
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ClickListener
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
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hies.common.HiesCommonRes
 *  kd.hr.hies.common.constant.TemplateConfConst
 *  kd.hr.hies.common.enu.OprCategory
 *  kd.hr.hies.common.util.MethodUtil
 *  kd.hrmp.hies.multientry.common.EntryConstant
 *  kd.hrmp.hies.multientry.common.HiesEntryRes
 *  kd.hrmp.hies.multientry.common.util.TemplateFormCommonUtil
 *  org.apache.commons.lang3.ObjectUtils
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hrmp.hies.multientry.formplugin;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.EntityType;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.SaveAndNew;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ClickListener;
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
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hies.common.HiesCommonRes;
import kd.hr.hies.common.constant.TemplateConfConst;
import kd.hr.hies.common.enu.OprCategory;
import kd.hr.hies.common.util.MethodUtil;
import kd.hrmp.hies.multientry.common.EntryConstant;
import kd.hrmp.hies.multientry.common.HiesEntryRes;
import kd.hrmp.hies.multientry.common.util.TemplateFormCommonUtil;
import org.apache.commons.lang3.ObjectUtils;

@ExcludeFromJacocoGeneratedReport
public final class TemplateAddSubEntityPlugin
extends HRBaseDataCommonEdit
implements BeforeF7SelectListener,
TemplateConfConst,
EntryConstant {
    private static final Log LOG = LogFactory.getLog(TemplateAddSubEntityPlugin.class);
    private static final int ENTITY_UNIQUE_VAL = 500;
    private static final String KEY_OPKEY = "saveandnew";

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        BasedataEdit mainEntityBaseData = (BasedataEdit)this.getView().getControl("entity");
        mainEntityBaseData.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        TextEdit entityUniqueNameText = (TextEdit)this.getView().getControl("entryuniquename");
        entityUniqueNameText.addClickListener((ClickListener)this);
    }

    public void afterCreateNewData(EventObject evt) {
    }

    public void afterLoadData(EventObject eventObject) {
        this.fillEntryList();
    }

    private void fillEntryList() {
        DynamicObject mainEntityDyn = (DynamicObject)this.getModel().getValue("entity");
        String entityNumber = mainEntityDyn.getString("number");
        MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        Map allEntities = mainEntityType.getAllEntities();
        Set entries = allEntities.entrySet();
        HashMap entryMap = new HashMap(entries.size());
        for (Map.Entry entry : entries) {
            if (entityNumber.equals(entry.getKey())) continue;
            entryMap.put(entry.getKey(), entry.getValue());
        }
        DynamicObjectCollection entryDyns = this.getModel().getEntryEntity("entrylist");
        if (entryDyns.size() > 0) {
            entryDyns.forEach(entryDyn -> {
                String entryNumber = entryDyn.getString("entrynumber");
                entryDyn.set("entryname", (Object)((EntityType)entryMap.get(entryNumber)).getDisplayName());
                StringBuilder entryUniqueNames = new StringBuilder();
                String entryUniqueValues = entryDyn.getString("entryuniqueval");
                if (HRStringUtils.isNotBlank((CharSequence)entryUniqueValues)) {
                    EntityType entityType = (EntityType)allEntities.get(entryNumber);
                    DataEntityPropertyCollection properties = entityType.getProperties();
                    String primaryId = entryNumber.concat(".").concat("id");
                    String[] entryUniqueValueArray = entryUniqueValues.split(",");
                    Arrays.stream(entryUniqueValueArray).filter(field -> HRStringUtils.isNotBlank((CharSequence)field) && (primaryId.equals(field) || properties.containsKey(field))).forEach(field -> {
                        if (primaryId.equals(field)) {
                            entryUniqueNames.append(ResManager.loadKDString((String)"\u5185\u7801", (String)HiesCommonRes.TemplateFieldConfPlugin_2.resId(), (String)"hrmp-hies-common", (Object[])new Object[0])).append("\uff1b");
                            return;
                        }
                        if (HRStringUtils.isNotBlank((Object)((IDataEntityProperty)properties.get(field)).getDisplayName())) {
                            entryUniqueNames.append(((IDataEntityProperty)properties.get(field)).getDisplayName().getLocaleValue()).append("\uff1b");
                        } else {
                            entryUniqueNames.append(((IDataEntityProperty)properties.get(field)).getName()).append("\uff1b");
                        }
                    });
                    entryDyn.set("entryuniquename", (Object)entryUniqueNames.toString());
                }
            });
            this.getModel().setDataChanged(false);
            this.getView().updateView("entrylist");
        }
    }

    public void afterCopyData(EventObject e) {
        this.fillEntryList();
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (formOperate instanceof SaveAndNew && org.apache.commons.lang3.StringUtils.equals((CharSequence)KEY_OPKEY, (CharSequence)((SaveAndNew)formOperate).getOriOperateKey()) && args.getOperationResult() != null && args.getOperationResult().isSuccess()) {
            TemplateFormCommonUtil.removeTabPage((IFormView)this.getView());
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        if (!"save".equals(evt.getOperationKey()) && !KEY_OPKEY.equals(evt.getOperationKey())) {
            return;
        }
        boolean isImport = "IMPT".equals(this.getModel().getValue("tmpltype"));
        Object importType = this.getModel().getValue("importtype");
        if (isImport && Objects.isNull(importType)) {
            String importTypeName = ((ComboEdit)this.getView().getControl("importtype")).getProperty().getDisplayName().getLocaleValue();
            this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u8bf7\u586b\u5199%s\u3002", (String)HiesEntryRes.TemplateAddSubEntityPlugin_9.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), importTypeName));
            evt.setCancel(true);
            return;
        }
        DynamicObjectCollection entryDyns = this.getModel().getEntryEntity("entrylist");
        String importtype = (String)this.getModel().getValue("importtype");
        boolean isNew = OprCategory.isNew((String)importtype);
        StringBuilder sb = new StringBuilder();
        for (DynamicObject dynamicObject : entryDyns) {
            String msg;
            String entryUniqueVal = dynamicObject.getString("entryuniqueval");
            if (!isNew && isImport && HRStringUtils.isBlank((CharSequence)entryUniqueVal)) {
                msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u201c%1$s\u201d\u7684\u9700\u66f4\u65b0\u8bb0\u5f55\u8bc6\u522b\u5b57\u6bb5\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_11.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]), dynamicObject.getString("entryname").concat("(").concat(dynamicObject.getString("entrynumber")).concat(")"));
                sb.append(msg).append("\r\n");
            }
            if (!HRStringUtils.isNotBlank((CharSequence)entryUniqueVal) || entryUniqueVal.length() <= 500) continue;
            msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u201c%1$s\u201d\u7684\u9700\u66f4\u65b0\u8bb0\u5f55\u8bc6\u522b\u5b57\u6bb5\u8d85\u8fc7\u6700\u5927\u957f\u5ea6\u9650\u5236\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_3.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]), dynamicObject.getString("rentity.name").concat("(").concat(dynamicObject.getString("rentity.number")).concat(")"));
            sb.append(msg).append("\r\n");
        }
        if (!ObjectUtils.isEmpty((Object)sb.toString())) {
            this.getView().showTipNotification(sb.toString());
            evt.setCancel(true);
            return;
        }
        HashMap<String, List> importFieldMap = new HashMap<String, List>(16);
        DynamicObjectCollection dynColl = this.getModel().getEntryEntity("tpltreeentryentity");
        for (DynamicObject dyn : dynColl) {
            if (!dyn.getBoolean("isimport")) continue;
            importFieldMap.computeIfAbsent(dyn.getString("entrykey"), k -> new ArrayList()).add(dyn.getString("fieldnumber"));
        }
        LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)this.getView());
        for (Map.Entry entryMap : allEntity.entrySet()) {
            String[] entityAndEntryArr = ((String)entryMap.getKey()).split(":");
            String entryNumber = entityAndEntryArr[1];
            List fieldList = (List)importFieldMap.get(entryNumber);
            if (!HRCollUtil.isEmpty((Collection)fieldList)) continue;
            this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5355\u636e\u4f53[%s]\u81f3\u5c11\u914d\u7f6e\u4e00\u4e2a\u5bfc\u5165\u5b57\u6bb5\u3002", (String)HiesEntryRes.TemplateConfPlugin_92.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), entryMap.getValue()));
            evt.setCancel(true);
            return;
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String propName = args.getProperty().getName();
        ChangeData[] changeSet = args.getChangeSet();
        Object newValue = changeSet[0].getNewValue();
        int row = args.getChangeSet()[0].getRowIndex();
        switch (propName) {
            case "entity": {
                break;
            }
            case "tmpltype": {
                this.initTemplateType();
                break;
            }
            case "importtype": 
            case "entryuniquename": {
                if (!HRStringUtils.isBlank((CharSequence)((String)newValue))) break;
                String oldUniqueVal = (String)this.getModel().getValue("entryuniqueval");
                this.getModel().setValue("entryuniqueval", newValue, row);
                DynamicObject dynObject = (DynamicObject)this.getModel().getEntryEntity("entrylist").get(row);
                String entryNumber = dynObject.getString("entrynumber");
                TemplateFormCommonUtil.refreshTree4UniqueVal((IFormView)this.getView(), (String)entryNumber, (String)"", (String)oldUniqueVal);
                TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)entryNumber);
                TemplateFormCommonUtil.refreshTabPage4UniqueVal((IFormView)this.getView(), (String)entryNumber, (String)"", (String)oldUniqueVal);
                break;
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String fieldKey = beforeF7SelectEvent.getProperty().getName();
        if (org.apache.commons.lang3.StringUtils.equals((CharSequence)fieldKey, (CharSequence)"entity")) {
            ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            showParameter.setCaption(ResManager.loadKDString((String)"\u5b9e\u4f53\u9009\u62e9\u5217\u8868", (String)HiesEntryRes.TemplateAddSubEntityPlugin_10.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
            showParameter.getListFilterParameter().setFilter(new QFilter("number", "in", this.getBillNumbers()));
        }
    }

    private void initTemplateType() {
        boolean isImportTpl = "IMPT".equals(this.getModel().getValue("tmpltype"));
        ((ComboEdit)this.getControl("importtype")).setMustInput(isImportTpl);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<String> getBillNumbers() {
        String fields = "number";
        String orderby = "bizappid asc";
        QFilter modelTypeFilter = new QFilter("modeltype", "in", Arrays.asList("BaseFormModel", "BillFormModel", "QueryListModel"));
        QFilter[] filters = new QFilter[4];
        filters[0] = modelTypeFilter;
        ArrayList<String> billNumbers = new ArrayList<String>();
        try (DataSet ds = ORM.create().queryDataSet("bos_entityobject", "bos_entityobject", fields, filters, orderby);){
            for (Row row : ds) {
                billNumbers.add(row.getString("number"));
            }
        }
        LOG.info("TemplateAddSubEntityPlugin.getBillNumbers billNumbers:{}", billNumbers);
        return billNumbers;
    }

    public void click(EventObject evt) {
        super.click(evt);
        if (evt.getSource() instanceof TextEdit) {
            TextEdit source = (TextEdit)evt.getSource();
            String entryKey = source.getEntryKey();
            if (source.getView().getFormShowParameter().getStatus().name().equalsIgnoreCase("VIEW")) {
                return;
            }
            if ("entrylist".equals(entryKey)) {
                EntryGrid entryGrid = (EntryGrid)this.getView().getControl("entrylist");
                int[] selectRows = entryGrid.getSelectRows();
                if (selectRows == null || selectRows.length == 0) {
                    return;
                }
                int rowNum = selectRows[0];
                String fieldKey = source.getKey();
                if (HRStringUtils.equals((String)"entryuniquename", (String)fieldKey)) {
                    this.openSelectFieldF7(rowNum, source);
                }
            }
        }
    }

    private void openSelectFieldF7(int rowIndex, TextEdit textEditCol) {
        String entryNumber = (String)this.getModel().getValue("entrynumber", rowIndex);
        if (HRStringUtils.isBlank((CharSequence)entryNumber)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u5173\u8054\u5b50\u5b9e\u4f53\u3002", (String)HiesCommonRes.TemplateAddSubEntityPlugin_6.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
            return;
        }
        DynamicObject mainEntity = (DynamicObject)this.getModel().getValue("entity");
        String entityNumber = mainEntity.getString("number");
        MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        EntityType entityType = (EntityType)mainEntityType.getAllEntities().get(entryNumber);
        String entryName = entityType.getName();
        TreeNode fieldTreeNode = new TreeNode("", entryName, entityType.getDisplayName().toString());
        fieldTreeNode.setIsOpened(true);
        for (IDataEntityProperty prop : entityType.getProperties()) {
            if (MethodUtil.ignoreUniqueValField((IDataEntityProperty)prop, (boolean)false)) continue;
            fieldTreeNode.addChild(new TreeNode(entryName, prop.getName(), prop.getDisplayName().getLocaleValue()));
        }
        DynamicObject entryDy = textEditCol.getModel().getEntryRowEntity("entrylist", rowIndex);
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hies_entityobjectfieldf7");
        String nodesJson = SerializationUtils.toJsonString((Object)fieldTreeNode);
        showParameter.getCustomParams().put("treenodes", nodesJson);
        showParameter.getCustomParams().put("rootNodeId", entryName);
        showParameter.getCustomParams().put("selectfieldvalue", entryDy.getString("entryuniqueval"));
        showParameter.getCustomParams().put("selectfieldname", entryDy.getString("entryuniquename"));
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "entryuniquename_" + rowIndex));
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setCaption(ResManager.loadKDString((String)"\u9700\u66f4\u65b0\u8bb0\u5f55\u8bc6\u522b\u5b57\u6bb5", (String)HiesCommonRes.TemplateAddSubEntityPlugin_7.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
        this.getView().showForm(showParameter);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        IDataModel model = this.getModel();
        String actionId = closedCallBackEvent.getActionId();
        if (actionId.contains("entryuniquename")) {
            DynamicObjectCollection entryDyns = this.getModel().getEntryEntity("entrylist");
            int selectRowIndex = Integer.parseInt(actionId.split("_")[1]);
            DynamicObject dynObject = (DynamicObject)entryDyns.get(selectRowIndex);
            HashMap returnData = (HashMap)closedCallBackEvent.getReturnData();
            if (returnData != null) {
                String oldUnique = dynObject.getString("entryuniqueval");
                String uniqueval = (String)returnData.get("currNodeIds");
                dynObject.set("entryuniquename", returnData.get("currNodeNames"));
                dynObject.set("entryuniqueval", (Object)uniqueval);
                String entryNumber = dynObject.getString("entrynumber");
                String newUnique = uniqueval;
                List<Object> cancelUnique = new ArrayList(16);
                List<Object> newAddUnique = new ArrayList(16);
                if (HRStringUtils.isNotBlank((CharSequence)oldUnique)) {
                    List oldUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)oldUnique).collect(Collectors.toList());
                    if (StringUtils.isNotBlank((CharSequence)newUnique)) {
                        List newUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)newUnique).collect(Collectors.toList());
                        oldUniqueList.removeAll(newUniqueList);
                    }
                    cancelUnique = oldUniqueList;
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank((CharSequence)newUnique)) {
                    List newUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)newUnique).collect(Collectors.toList());
                    if (StringUtils.isNotBlank((CharSequence)oldUnique)) {
                        List oldUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)oldUnique).collect(Collectors.toList());
                        newUniqueList.removeAll(oldUniqueList);
                    }
                    newAddUnique = newUniqueList;
                }
                String cancelUniqueVal = Joiner.on((String)",").join(cancelUnique);
                String mustInputField = TemplateFormCommonUtil.getEntityMustInputField((IDataModel)model, (String)entryNumber);
                TemplateFormCommonUtil.refreshTree4UniqueVal((IFormView)this.getView(), (String)entryNumber, (String)mustInputField, (String)cancelUniqueVal);
                String newAddUniqueVal = Joiner.on((String)",").join(newAddUnique);
                TemplateFormCommonUtil.refreshTabPage4UniqueVal((IFormView)this.getView(), (String)entryNumber, (String)newAddUniqueVal, (String)cancelUniqueVal);
                TemplateFormCommonUtil.updateParentEntry4UniqueVal((IFormView)this.getView(), (String)entryNumber, (String)newAddUniqueVal);
                TemplateFormCommonUtil.lockShowField4UniqueVal((IFormView)this.getView(), (String)entryNumber, (String)mustInputField);
                TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)entryNumber);
                this.getModel().updateEntryCache(entryDyns);
                this.getView().updateView("entrylist");
            }
        }
    }
}
