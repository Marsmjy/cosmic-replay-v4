/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.RefObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.EntityType
 *  kd.bos.entity.EntryType
 *  kd.bos.entity.MainEntityType
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hpfs.business.application.service.fileMap.FileMapManagerService
 *  kd.hr.hpfs.business.application.service.fileMap.IFileMapManagerService
 *  kd.sdk.hr.hpfs.business.utils.AttachmentUtils
 *  kd.sdk.hr.hpfs.common.constants.FileMapManagerConstants
 *  kd.sdk.hr.hspm.business.internal.file.InfoGroupSupport
 */
package kd.hr.hpfs.formplugin.fieldmap;

import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.RefObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.EntityType;
import kd.bos.entity.EntryType;
import kd.bos.entity.MainEntityType;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hpfs.business.application.service.fileMap.FileMapManagerService;
import kd.hr.hpfs.business.application.service.fileMap.IFileMapManagerService;
import kd.sdk.hr.hpfs.business.utils.AttachmentUtils;
import kd.sdk.hr.hpfs.common.constants.FileMapManagerConstants;
import kd.sdk.hr.hspm.business.internal.file.InfoGroupSupport;

public final class FieldRelationEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener,
FileMapManagerConstants {
    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        BasedataEdit targetField = (BasedataEdit)this.getView().getControl("targetfieldnew");
        targetField.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        boolean notPass;
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        OperateOption option = operate.getOption();
        RefObject confirmCallBack = new RefObject();
        boolean exist = option.tryGetVariableValue("confirmCallBack", confirmCallBack);
        if (exist && "1".equals(confirmCallBack.getValue())) {
            return;
        }
        String entryData = (String)this.getView().getFormShowParameter().getCustomParam("selectrowentitydy");
        String entryEntityData = (String)this.getView().getFormShowParameter().getCustomParam("entryentitydata");
        if (StringUtils.isBlank((CharSequence)entryEntityData)) {
            return;
        }
        if (StringUtils.isBlank((CharSequence)entryData)) {
            return;
        }
        DynamicObjectCollection parentEntryEntityDys = (DynamicObjectCollection)SerializationUtils.deSerializeFromBase64((String)entryEntityData);
        DynamicObject selectRowEntityDy = (DynamicObject)SerializationUtils.deSerializeFromBase64((String)entryData);
        if (Objects.isNull(parentEntryEntityDys)) {
            return;
        }
        if (Objects.isNull(selectRowEntityDy)) {
            return;
        }
        if (selectRowEntityDy.getBoolean("srcentryissyspreset")) {
            return;
        }
        String sourceEntryTag = selectRowEntityDy.getString("sourceentry");
        if (StringUtils.isBlank((CharSequence)sourceEntryTag)) {
            return;
        }
        DynamicObject targetEntityDy = selectRowEntityDy.getDynamicObject("targetentity");
        if (Objects.isNull(targetEntityDy)) {
            return;
        }
        DynamicObjectCollection subEntryEntityDys = this.getModel().getEntryEntity("subentryentity");
        Optional<String> loadPersonInfoValid = this.loadPersonInfoValid(parentEntryEntityDys, sourceEntryTag, targetEntityDy, subEntryEntityDys);
        if (loadPersonInfoValid.isPresent()) {
            this.getView().showErrorNotification(loadPersonInfoValid.get());
            args.setCancel(true);
            return;
        }
        String billInfoGroup = selectRowEntityDy.getString("billinfogroup");
        Optional<String> writePersonInfoValid = this.writePersonInfoValid(subEntryEntityDys, billInfoGroup, targetEntityDy);
        if (writePersonInfoValid.isPresent()) {
            this.getView().showErrorNotification(writePersonInfoValid.get());
            args.setCancel(true);
            return;
        }
        Optional<String> otherWritePersonInfoValid = this.otherWritePersonInfoValid(targetEntityDy, subEntryEntityDys);
        if (otherWritePersonInfoValid.isPresent()) {
            this.getView().showErrorNotification(otherWritePersonInfoValid.get());
            args.setCancel(true);
            return;
        }
        Optional<String> wrapWritePersonInfoValid = this.wrapWritePersonInfoValid(targetEntityDy, subEntryEntityDys);
        if (wrapWritePersonInfoValid.isPresent()) {
            this.getView().showErrorNotification(wrapWritePersonInfoValid.get());
            args.setCancel(true);
        }
        if (notPass = this.writePersonInfoMultiValid(parentEntryEntityDys, selectRowEntityDy, subEntryEntityDys, billInfoGroup)) {
            args.setCancel(true);
        }
    }

    private boolean writePersonInfoMultiValid(DynamicObjectCollection parentEntryEntityDys, DynamicObject selectRowEntityDy, DynamicObjectCollection subEntryEntityDys, String billInfoGroup) {
        DynamicObject targetEntityDy = selectRowEntityDy.getDynamicObject("targetentity");
        Map<Long, String> selfTargetFieldNewAndSourceFieldTagMap = subEntryEntityDys.stream().filter(data -> Objects.nonNull(data.getDynamicObject("targetfieldnew")) && data.getBoolean("writepersoninfo")).collect(Collectors.toMap(data -> data.getLong("targetfieldnew.id"), data -> data.getString("sourcefield"), (o1, o2) -> o1));
        if (CollectionUtils.isEmpty(selfTargetFieldNewAndSourceFieldTagMap)) {
            return false;
        }
        for (DynamicObject entryEntityDy : parentEntryEntityDys) {
            if (entryEntityDy.getPkValue().equals(selectRowEntityDy.getPkValue()) || !targetEntityDy.getString("id").equals(entryEntityDy.getString("targetentity.id"))) continue;
            DynamicObjectCollection parentSubEntryEntityDys = entryEntityDy.getDynamicObjectCollection("subentryentity");
            Map<Long, List<DynamicObject>> targetFieldNewAndSourceFieldMap = parentSubEntryEntityDys.stream().filter(data -> Objects.nonNull(data.getDynamicObject("targetfieldnew")) && data.getBoolean("writepersoninfo")).collect(Collectors.groupingBy(data -> data.getLong("targetfieldnew.id")));
            for (Map.Entry<Long, String> selfTargetFieldNewAndSourceFieldTag : selfTargetFieldNewAndSourceFieldTagMap.entrySet()) {
                List<DynamicObject> dynamicObjects;
                Long selfTargetFieldNew = selfTargetFieldNewAndSourceFieldTag.getKey();
                if (CollectionUtils.isEmpty((Collection)targetFieldNewAndSourceFieldMap.get(selfTargetFieldNew)) || CollectionUtils.isEmpty(dynamicObjects = targetFieldNewAndSourceFieldMap.get(selfTargetFieldNew))) continue;
                for (DynamicObject dynamicObject : dynamicObjects) {
                    String existField = dynamicObject.getString("sourcefieldtag");
                    if (!StringUtils.isNotEmpty((CharSequence)existField)) continue;
                    this.getView().showConfirm(ResManager.loadKDString((String)"\u4fe1\u606f\u7ec4\u201c%1$s\u201d\u7684\u5b57\u6bb5\u201c%2$s\u201d\u3001\u4fe1\u606f\u7ec4\u201c%3$s\u201d\u7684\u5b57\u6bb5\u201c%4$s\u201d\u5747\u6620\u5c04\u5230\u4fe1\u606f\u7ec4\u201c%5$s\u201d\u7684\u5b57\u6bb5\u201c%6$s\u201d\uff0c\u7cfb\u7edf\u5c06\u6839\u636e\u6570\u636e\u7ec4\u88c5\u987a\u5e8f\u8fdb\u884c\u8986\u76d6\u5199\u5165\u3002", (String)"FieldRelationEdit_1", (String)"hr-hpfs-formplugin", (Object[])new Object[]{entryEntityDy.getString("billinfogroup"), dynamicObject.getString("sourcefield"), billInfoGroup, selfTargetFieldNewAndSourceFieldTag.getValue(), entryEntityDy.getString("targetentity.name"), dynamicObject.getString("targetfieldnew.name")}), MessageBoxOptions.OKCancel, ConfirmTypes.Default, new ConfirmCallBackListener("donothing_save", (IFormPlugin)this));
                    return true;
                }
            }
        }
        return false;
    }

    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        String callBackId = messageBoxClosedEvent.getCallBackId();
        if (HRStringUtils.equals((String)"donothing_save", (String)callBackId) && messageBoxClosedEvent.getResult() == MessageBoxResult.Yes) {
            OperateOption operateOption = OperateOption.create();
            operateOption.setVariableValue("confirmCallBack", "1");
            this.getView().invokeOperation("donothing_save", operateOption);
        }
    }

    private Optional<String> wrapWritePersonInfoValid(DynamicObject targetEntityDy, DynamicObjectCollection subEntryEntityDys) {
        String pageNumber = targetEntityDy.getString("number");
        if (InfoGroupSupport.isSingleRowTpl((String)pageNumber)) {
            return Optional.empty();
        }
        List writePersonInfos = subEntryEntityDys.stream().filter(data -> Objects.nonNull(data.getDynamicObject("targetfieldnew")) && data.getBoolean("writepersoninfo")).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(writePersonInfos)) {
            return Optional.empty();
        }
        String entryEntityData = (String)this.getView().getFormShowParameter().getCustomParam("otherentryentitydata");
        if (StringUtils.isBlank((CharSequence)entryEntityData)) {
            return Optional.empty();
        }
        DynamicObjectCollection hcfEntryEntityDys = (DynamicObjectCollection)SerializationUtils.deSerializeFromBase64((String)entryEntityData);
        for (DynamicObject entryEntityDy : hcfEntryEntityDys) {
            DynamicObjectCollection hcfSubEntryEntityDys;
            List hcfWritePersonInfos;
            if (!targetEntityDy.getString("id").equals(entryEntityDy.getString("targetentity_c.id")) || CollectionUtils.isEmpty(hcfWritePersonInfos = (hcfSubEntryEntityDys = entryEntityDy.getDynamicObjectCollection("subentryentity_c")).stream().filter(data -> Objects.nonNull(data.getDynamicObject("targetfieldnew_c")) && data.getBoolean("writepersoninfo_c")).collect(Collectors.toList()))) continue;
            return Optional.of(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u4eba\u5458\u4fe1\u606f\u7ec4\u201c%s\u201d\u4e0d\u80fd\u540c\u65f6\u4e0e\u5355\u636e\u548c\u62df\u5165\u804c\u4eba\u5458\u5efa\u7acb\u6620\u5c04\u5173\u7cfb\uff0c\u8bf7\u4fee\u6539\u540e\u518d\u8bd5\u3002", (String)"FieldRelationEdit_3", (String)"hr-hpfs-formplugin", (Object[])new Object[0]), targetEntityDy.getString("name")));
        }
        return Optional.empty();
    }

    private Optional<String> otherWritePersonInfoValid(DynamicObject targetEntityDy, DynamicObjectCollection subEntryEntityDys) {
        String entryEntityData = (String)this.getView().getFormShowParameter().getCustomParam("otherentryentitydata");
        if (StringUtils.isBlank((CharSequence)entryEntityData)) {
            return Optional.empty();
        }
        DynamicObjectCollection parentEntryEntityDys = (DynamicObjectCollection)SerializationUtils.deSerializeFromBase64((String)entryEntityData);
        Map<Long, DynamicObject> selfTargetFieldNewAndDy = subEntryEntityDys.stream().filter(data -> Objects.nonNull(data.getDynamicObject("targetfieldnew")) && data.getBoolean("writepersoninfo")).collect(Collectors.toMap(data -> data.getLong("targetfieldnew.id"), data -> data, (o1, o2) -> o1));
        if (CollectionUtils.isEmpty(selfTargetFieldNewAndDy)) {
            return Optional.empty();
        }
        for (DynamicObject entryEntityDy : parentEntryEntityDys) {
            if (!targetEntityDy.getString("id").equals(entryEntityDy.getString("targetentity_c.id"))) continue;
            DynamicObjectCollection parentSubEntryEntityDys = entryEntityDy.getDynamicObjectCollection("subentryentity_c");
            Map<Long, DynamicObject> targetFieldNewAndDy = parentSubEntryEntityDys.stream().filter(data -> Objects.nonNull(data.getDynamicObject("targetfieldnew_c")) && data.getBoolean("writepersoninfo_c")).collect(Collectors.toMap(data -> data.getLong("targetfieldnew_c.id"), data -> data, (o1, o2) -> o1));
            for (Map.Entry<Long, DynamicObject> entry : selfTargetFieldNewAndDy.entrySet()) {
                if (!targetFieldNewAndDy.containsKey(entry.getKey())) continue;
                String targetFieldNewName = entry.getValue().getString("targetfieldnew.name");
                return Optional.of(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u4eba\u5458\u4fe1\u606f\u5b57\u6bb5\u201c%1$s\u201d\u5df2\u5728\u62df\u5165\u804c\u4eba\u5458\u4fe1\u606f\u7ec4\u201c%2$s\u201d\u4e2d\u542f\u7528\u4e86\u201c\u56de\u5199\u4eba\u5458\u4fe1\u606f\u201d\uff0c\u65e0\u6cd5\u540c\u65f6\u7531\u4e24\u4e2a\u4e0d\u540c\u7684\u5b57\u6bb5\u56de\u5199\uff0c\u8bf7\u8c03\u6574\u540e\u518d\u8bd5\u3002", (String)"FieldRelationEdit_2", (String)"hr-hpfs-formplugin", (Object[])new Object[0]), targetFieldNewName, entryEntityDy.getDynamicObject("sourceentity_c").getString("name")));
            }
        }
        return Optional.empty();
    }

    private Optional<String> writePersonInfoValid(DynamicObjectCollection subEntryEntityDys, String billInfoGroup, DynamicObject targetEntityDy) {
        List dynamicObjects;
        List billFields;
        Optional<List> first = subEntryEntityDys.stream().filter(data -> Objects.nonNull(data.getDynamicObject("targetfieldnew")) && data.getBoolean("writepersoninfo")).collect(Collectors.groupingBy(data -> data.getLong("targetfieldnew.id"))).values().stream().filter(data -> data.size() > 1).findFirst();
        if (first.isPresent() && (billFields = (dynamicObjects = first.get()).stream().filter(data -> HRStringUtils.isNotEmpty((String)data.getString("sourcefieldtag"))).map(dy -> dy.getString("sourcefield")).collect(Collectors.toList())).size() >= 2) {
            return Optional.of(ResManager.loadKDString((String)"\u4fe1\u606f\u7ec4\u201c%1$s\u201d\u4e2d\u5b57\u6bb5\u201c%2$s\u201d\u6620\u5c04\u5230\u540c\u4e00\u4eba\u5458\u4fe1\u606f\u7ec4\u201c%3$s\u201d\u7684\u5b57\u6bb5\u201c%4$s\u201c\uff0c\u8bf7\u4fee\u6539\u3002", (String)"FieldRelationEdit_5", (String)"hr-hpfs-formplugin", (Object[])new Object[]{billInfoGroup, billFields.stream().collect(Collectors.joining("\u3001")), targetEntityDy.getString("name"), ((DynamicObject)dynamicObjects.get(0)).getString("targetfieldnew.name")}));
        }
        return Optional.empty();
    }

    private Optional<String> loadPersonInfoValid(DynamicObjectCollection parentEntryEntityDys, String sourceEntryTag, DynamicObject targetEntityDy, DynamicObjectCollection subEntryEntityDys) {
        Map<String, DynamicObject> selfSourceFieldTagAndDyMap = subEntryEntityDys.stream().filter(data -> Objects.nonNull(data.getDynamicObject("targetfieldnew")) && data.getBoolean("loadpersoninfo")).collect(Collectors.toMap(data -> data.getString("sourcefieldtag"), data -> data, (o1, o2) -> o1));
        if (CollectionUtils.isEmpty(selfSourceFieldTagAndDyMap)) {
            return Optional.empty();
        }
        for (DynamicObject entryEntityDy : parentEntryEntityDys) {
            if (sourceEntryTag.equals(entryEntityDy.getString("sourceentry")) && targetEntityDy.getString("id").equals(entryEntityDy.getString("targetentity.id"))) continue;
            DynamicObjectCollection parentSubEntryEntityDys = entryEntityDy.getDynamicObjectCollection("subentryentity");
            Map<String, DynamicObject> sourceFieldTagAndDyMap = parentSubEntryEntityDys.stream().filter(data -> Objects.nonNull(data.getDynamicObject("targetfieldnew")) && data.getBoolean("loadpersoninfo")).collect(Collectors.toMap(data -> data.getString("sourcefieldtag"), data -> data, (o1, o2) -> o1));
            for (Map.Entry<String, DynamicObject> entry : selfSourceFieldTagAndDyMap.entrySet()) {
                String sourceFieldTag = entry.getKey();
                if (!sourceFieldTagAndDyMap.containsKey(sourceFieldTag)) continue;
                DynamicObject dy = entry.getValue();
                String sourceField = dy.getString("sourcefield");
                return Optional.of(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5355\u636e\u5b57\u6bb5\u201c%1$s\u201d\u4e0d\u80fd\u540c\u65f6\u52a0\u8f7d\u4e24\u4e2a\u4eba\u5458\u4fe1\u606f\u5b57\u6bb5\uff0c\u8be5\u5b57\u6bb5\u5df2\u5728\u4eba\u5458\u4fe1\u606f\u7ec4\u201c%2$s\u201d\u4e2d\u542f\u7528\u4e86\u201c\u52a0\u8f7d\u4eba\u5458\u4fe1\u606f\u201d\uff0c\u8bf7\u8c03\u6574\u540e\u518d\u8bd5\u3002", (String)"FieldRelationEdit_0", (String)"hr-hpfs-formplugin", (Object[])new Object[0]), sourceField + "(" + sourceFieldTag + ")", entryEntityDy.getString("targetentity.name")));
            }
        }
        return Optional.empty();
    }

    public void afterCreateNewData(EventObject e) {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        String entryData = (String)this.getView().getFormShowParameter().getCustomParam("selectrowentitydy");
        String personnelBusinessBillNumber = (String)this.getView().getFormShowParameter().getCustomParam("personnelbusinessbillnumber");
        if (StringUtils.isBlank((CharSequence)personnelBusinessBillNumber)) {
            return;
        }
        if (StringUtils.isNotBlank((CharSequence)entryData)) {
            DynamicObject selectRowEntityDy = (DynamicObject)SerializationUtils.deSerializeFromBase64((String)entryData);
            if (Objects.isNull(selectRowEntityDy)) {
                return;
            }
            DynamicObjectCollection srcSubEntryEntityDys = selectRowEntityDy.getDynamicObjectCollection("subentryentity");
            IFileMapManagerService.getInstance().setSubEntryEntity(srcSubEntryEntityDys, this.getView());
            if (selectRowEntityDy.getBoolean("srcentryissyspreset")) {
                DynamicObjectCollection entryEntity = dataEntity.getDynamicObjectCollection("subentryentity");
                this.getModel().updateEntryCache(entryEntity);
                this.getView().updateView("subentryentity");
                return;
            }
            String sourceEntryTag = selectRowEntityDy.getString("sourceentry");
            if (StringUtils.isBlank((CharSequence)sourceEntryTag)) {
                return;
            }
            new FileMapManagerService().insertOrUpdatePersonfields(selectRowEntityDy);
            MainEntityType dataEntityType = IFileMapManagerService.getInstance().getEntityNameByExtEntityName(personnelBusinessBillNumber);
            String targetEntity = selectRowEntityDy.getString("targetentity.number");
            MainEntityType targetEntityType = EntityMetadataCache.getDataEntityType((String)targetEntity);
            if ("n_extinfo_fields".equals(sourceEntryTag)) {
                this.dealExtFieldsInfo(dataEntity, dataEntityType, targetEntityType);
            } else {
                this.dealExtEntryInfo(selectRowEntityDy, dataEntity, sourceEntryTag, dataEntityType);
            }
        }
    }

    private void dealExtEntryInfo(DynamicObject selectRowEntityDy, DynamicObject dataEntity, String sourceEntryTag, MainEntityType dataEntityType) {
        if (dataEntityType.getProperties().containsKey((Object)sourceEntryTag)) {
            String targetEntity = selectRowEntityDy.getString("targetentity.id");
            MainEntityType targetDataEntityType = MetadataServiceHelper.getDataEntityType((String)targetEntity);
            boolean hasId = targetDataEntityType.getProperties().containsKey((Object)"id");
            Set<String> blackListFieldSet = this.getBlackListFields(dataEntityType);
            HashMap allFieldMaps = new HashMap();
            ((EntityType)dataEntityType.getAllEntities().get(sourceEntryTag)).getFields().forEach((field, iDataEntityProperty) -> {
                if (!blackListFieldSet.contains(field)) {
                    allFieldMaps.put(field, iDataEntityProperty.getDisplayName().getLocaleValue());
                }
            });
            DynamicObjectCollection entryEntity = dataEntity.getDynamicObjectCollection("subentryentity");
            Map<String, DynamicObject> pageFieldMaps = entryEntity.stream().collect(Collectors.toMap(data -> data.getString("sourcefieldtag"), data -> data, (o1, o2) -> o1));
            for (Map.Entry entry : allFieldMaps.entrySet()) {
                String key = (String)entry.getKey();
                DynamicObject dynamicObject = pageFieldMaps.get(entry.getKey());
                if (Objects.isNull(dynamicObject)) {
                    dynamicObject = entryEntity.addNew();
                }
                if (key.endsWith("_tid")) {
                    DynamicObject[] personFields;
                    List number;
                    dynamicObject.set("sourcefield", (Object)allFieldMaps.getOrDefault(key, key));
                    dynamicObject.set("sourcefieldtag", (Object)key);
                    dynamicObject.set("ispkid", (Object)"1");
                    dynamicObject.set("isedit", (Object)false);
                    if (!hasId || CollectionUtils.isEmpty(number = Arrays.stream(personFields = this.getPersonFields(targetEntity)).filter(data -> "id".equals(data.getString("number"))).collect(Collectors.toList()))) continue;
                    dynamicObject.set("targetfieldnew", number.get(0));
                    continue;
                }
                dynamicObject.set("sourcefield", (Object)allFieldMaps.getOrDefault(key, key));
                dynamicObject.set("sourcefieldtag", (Object)key);
                dynamicObject.set("ispkid", (Object)"0");
            }
            List tIds = allFieldMaps.keySet().stream().filter(newField -> newField.endsWith("_tid")).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(tIds)) {
                String tid = (String)tIds.get(0);
                entryEntity.sort((o1, o2) -> {
                    int index1 = tid.equals(o1.getString("sourcefieldtag")) ? 0 : 1;
                    int index2 = tid.equals(o2.getString("sourcefieldtag")) ? 0 : 1;
                    return index1 - index2;
                });
            }
            this.getModel().updateEntryCache(entryEntity);
            this.getView().updateView("subentryentity");
        }
    }

    private void dealExtFieldsInfo(DynamicObject dataEntity, MainEntityType sourceEntityType, MainEntityType targetEntityType) {
        Set<String> blackListFieldSet = this.getBlackListFields(sourceEntityType);
        DynamicObjectCollection entryEntity = dataEntity.getDynamicObjectCollection("subentryentity");
        Map targetAllFields = targetEntityType.getAllFields();
        Map sourceAllFields = sourceEntityType.getAllFields();
        Map sourceAttachmentPanelKeyNameMap = AttachmentUtils.getAttachmentPanelKeyNameMapByFormId((String)sourceEntityType.getName());
        Map targetAttachmentPanelKeyNameMap = AttachmentUtils.getAttachmentPanelKeyNameMapByFormId((String)targetEntityType.getName());
        HashMap<String, DynamicObject> pageFieldMaps = new HashMap<String, DynamicObject>(entryEntity.size());
        for (int i = entryEntity.size() - 1; i >= 0; --i) {
            DynamicObject item = (DynamicObject)entryEntity.get(i);
            String sourceField = item.getString("sourcefieldtag");
            String targetField = item.getString("targetfieldnew.number");
            if (!sourceAllFields.containsKey(sourceField) && !sourceAttachmentPanelKeyNameMap.containsKey(sourceField)) {
                entryEntity.remove((Object)item);
                continue;
            }
            if (!(HRStringUtils.isEmpty((String)targetField) || targetAllFields.containsKey(targetField) || targetAttachmentPanelKeyNameMap.containsKey(targetField))) {
                item.set("targetfieldnew", null);
            }
            pageFieldMaps.put(sourceField, item);
        }
        sourceAllFields.forEach((field, dataEntityProperty) -> {
            if (blackListFieldSet.contains(field) || dataEntityProperty.getParent() instanceof EntryType) {
                return;
            }
            DynamicObject dynamicObject = (DynamicObject)pageFieldMaps.get(field);
            if (Objects.isNull(dynamicObject)) {
                dynamicObject = entryEntity.addNew();
                dynamicObject.set("ispkid", (Object)"0");
            }
            dynamicObject.set("sourcefield", (Object)dataEntityProperty.getDisplayName().getLocaleValue());
            dynamicObject.set("sourcefieldtag", field);
        });
        sourceAttachmentPanelKeyNameMap.forEach((sourceAttachmentPanelKey, sourceAttachmentPanelValue) -> {
            DynamicObject fieldEntryItem = (DynamicObject)pageFieldMaps.get(sourceAttachmentPanelKey);
            if (Objects.isNull(fieldEntryItem)) {
                fieldEntryItem = entryEntity.addNew();
                fieldEntryItem.set("ispkid", (Object)"0");
            }
            fieldEntryItem.set("sourcefield", sourceAttachmentPanelValue);
            fieldEntryItem.set("sourcefieldtag", sourceAttachmentPanelKey);
        });
        this.getModel().updateEntryCache(entryEntity);
        this.getView().updateView("subentryentity");
    }

    private Set<String> getBlackListFields(MainEntityType dataEntityType) {
        String blackListFieldStr = (String)this.getView().getFormShowParameter().getCustomParam("blackListField");
        Set blackListFieldSet = StringUtils.isEmpty((CharSequence)blackListFieldStr) ? new FileMapManagerService().getAllBlackListFields(dataEntityType, this.getPageCache()) : (Set)SerializationUtils.fromJsonString((String)blackListFieldStr, Set.class);
        return blackListFieldSet;
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        if (afterDoOperationEventArgs.getOperationResult() == null || !afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            return;
        }
        String operateKey = afterDoOperationEventArgs.getOperateKey();
        if ("donothing_save".equals(operateKey)) {
            DynamicObjectCollection subEntryEntity = this.getModel().getEntryEntity("subentryentity");
            this.getView().returnDataToParent((Object)SerializationUtils.serializeToBase64((Object)subEntryEntity));
            this.getPageCache().put("isdirectClose", "1");
        }
    }

    private DynamicObject[] getPersonFields(String targetEntity) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hpfs_personfield");
        QFilter qFilter = new QFilter("targetentity", "=", (Object)targetEntity);
        return helper.loadDynamicObjectArray(new QFilter[]{qFilter});
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String name = beforeF7SelectEvent.getProperty().getName();
        List customQFilters = beforeF7SelectEvent.getCustomQFilters();
        String entryData = (String)this.getView().getFormShowParameter().getCustomParam("selectrowentitydy");
        if (StringUtils.isBlank((CharSequence)entryData)) {
            return;
        }
        DynamicObject entity = (DynamicObject)SerializationUtils.deSerializeFromBase64((String)entryData);
        if (Objects.isNull(entity)) {
            return;
        }
        String targetEntity = entity.getString("targetentity.id");
        if (StringUtils.isBlank((CharSequence)targetEntity)) {
            return;
        }
        switch (name) {
            case "targetfieldnew": {
                QFilter qFilter = new QFilter("targetentity.id", "=", (Object)targetEntity);
                customQFilters.add(qFilter);
                customQFilters.add(new QFilter("entrykey", "=", (Object)""));
                break;
            }
        }
    }
}
