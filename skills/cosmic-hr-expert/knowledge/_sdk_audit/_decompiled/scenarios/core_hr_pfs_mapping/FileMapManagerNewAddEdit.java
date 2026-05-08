/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.EntryType
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.EntryProp
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hpfs.business.application.service.fileMap.FileMapManagerService
 *  kd.sdk.hr.hpfs.common.constants.FileMapManagerConstants
 */
package kd.hr.hpfs.formplugin.fieldmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.EntryType;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.EntryProp;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hpfs.business.application.service.fileMap.FileMapManagerService;
import kd.sdk.hr.hpfs.common.constants.FileMapManagerConstants;

public final class FileMapManagerNewAddEdit
extends HRDataBaseEdit
implements FileMapManagerConstants {
    public void afterBindData(EventObject e) {
        Optional<DynamicObject> exist;
        DynamicObjectCollection mappedFieldEntry;
        DynamicObject businessBillDy = this.getModel().getDataEntity().getDynamicObject("personnelbusinessbill");
        if (businessBillDy == null) {
            return;
        }
        this.setBillInfoCombo();
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity");
        if (CollectionUtils.isEmpty((Collection)entryEntity)) {
            return;
        }
        for (int index = 0; index < entryEntity.size(); ++index) {
            DynamicObject entryDy = (DynamicObject)entryEntity.get(index);
            entryDy.set("billinfocombo", (Object)entryDy.getString("sourceentry"));
            if ("1".equals(entryDy.getString("srcentryissyspreset"))) {
                entryDy.set("billinfocombo", (Object)entryDy.getString("billinfogroup"));
            }
            if (CollectionUtils.isEmpty((Collection)(mappedFieldEntry = entryDy.getDynamicObjectCollection("subentryentity"))) || !(exist = mappedFieldEntry.stream().filter(mappedFieldDy -> mappedFieldDy.getDynamicObject("targetfieldnew") != null).findAny()).isPresent()) continue;
            this.getView().setEnable(Boolean.valueOf(false), index, new String[]{"billinfocombo"});
        }
        this.getModel().setDataChanged(false);
        this.getView().updateView("entryentity");
        DynamicObjectCollection candidateEntryEntity = this.getModel().getEntryEntity("entryentity_c");
        if (CollectionUtils.isEmpty((Collection)candidateEntryEntity)) {
            return;
        }
        for (int indexc = 0; indexc < candidateEntryEntity.size(); ++indexc) {
            mappedFieldEntry = ((DynamicObject)candidateEntryEntity.get(indexc)).getDynamicObjectCollection("subentryentity_c");
            if (CollectionUtils.isEmpty((Collection)mappedFieldEntry) || !(exist = mappedFieldEntry.stream().filter(mappedFieldDy -> mappedFieldDy.getDynamicObject("targetfieldnew_c") != null).findAny()).isPresent()) continue;
            this.getView().setEnable(Boolean.valueOf(false), indexc, new String[]{"sourceentity_c"});
        }
        this.getView().updateView("entryentity_c");
    }

    public void propertyChanged(PropertyChangedArgs evt) {
        String propertyName = evt.getProperty().getName();
        ChangeData[] changeSet = evt.getChangeSet();
        switch (propertyName) {
            case "personnelbusinessbill": {
                if (changeSet == null || changeSet.length == 0) {
                    return;
                }
                if (Objects.nonNull(changeSet[0].getNewValue()) && changeSet[0].getNewValue().equals(changeSet[0].getOldValue())) {
                    return;
                }
                if (!Objects.isNull(changeSet[0].getOldValue())) break;
                this.setBillInfoCombo();
                break;
            }
            case "billinfocombo": {
                ChangeData changeData = changeSet[0];
                int rowIndex = changeData.getRowIndex();
                this.getModel().setValue("sourceentry", changeData.getNewValue(), rowIndex);
                String comboMapStr = this.getPageCache().get("billinfocombo");
                Map comboMap = (Map)SerializationUtils.fromJsonString((String)comboMapStr, Map.class);
                this.getModel().setValue("billinfogroup", comboMap.get(changeData.getNewValue()), rowIndex);
                this.getModel().setValue("sourceentity", this.getModel().getValue("personnelbusinessbill"), rowIndex);
                break;
            }
        }
    }

    private void setBillInfoCombo() {
        HashMap<String, LocaleString> comboMap = new HashMap<String, LocaleString>(8);
        ArrayList<ComboItem> comboItems = new ArrayList<ComboItem>();
        String entityNumber = this.getModel().getDataEntity().getDynamicObject("personnelbusinessbill").getString("number");
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        Set blackListFieldSet = new FileMapManagerService().getAllBlackListFields(dataEntityType, this.getPageCache());
        for (IDataEntityProperty property : dataEntityType.getProperties()) {
            Map fields;
            if (!(property instanceof EntryProp) || blackListFieldSet.containsAll((fields = ((EntryType)((EntryProp)property).getItemType()).getFields()).keySet())) continue;
            ComboItem item = new ComboItem(property.getDisplayName(), property.getName());
            comboItems.add(item);
            comboMap.put(property.getName(), property.getDisplayName());
        }
        ComboItem otherItem = new ComboItem(ResManager.getLocaleString((String)"\u5176\u4ed6\u5b57\u6bb5\u5206\u7ec4", (String)"FileMapManagerEdit_7", (String)"hr-hpfs-formplugin"), "n_extinfo_fields");
        comboItems.add(otherItem);
        comboMap.put("n_extinfo_fields", ResManager.getLocaleString((String)"\u5176\u4ed6\u5b57\u6bb5\u5206\u7ec4", (String)"FileMapManagerEdit_7", (String)"hr-hpfs-formplugin"));
        ComboEdit billInfoCombo = (ComboEdit)this.getControl("billinfocombo");
        billInfoCombo.setComboItems(comboItems);
        this.getPageCache().put("billinfocombo", SerializationUtils.toJsonString(comboMap));
    }
}
