/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.Row
 *  kd.bos.coderule.api.ICodeRuleService
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.DBRoute
 *  kd.bos.entity.BillEntityType
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.EntryType
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.property.MuliLangTextProp
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.entity.validate.GroupFieldsUniqueValidateResult
 *  kd.bos.exception.KDBizException
 *  kd.bos.inte.api.EnabledLang
 *  kd.bos.lang.Lang
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.service.ServiceFactory
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.QueryServiceHelper
 *  kd.bos.servicehelper.inte.InteServiceHelper
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.util.HRDBUtil
 *  kd.hr.hbp.opplugin.web.history.uniquevolidator.GroupKey
 *  kd.hr.hbp.opplugin.web.history.uniquevolidator.GroupKeyBuilder
 */
package kd.hr.hbp.opplugin.web.history.uniquevolidator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.coderule.api.ICodeRuleService;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.DBRoute;
import kd.bos.entity.BillEntityType;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.EntryType;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.property.MuliLangTextProp;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.entity.validate.GroupFieldsUniqueValidateResult;
import kd.bos.exception.KDBizException;
import kd.bos.inte.api.EnabledLang;
import kd.bos.lang.Lang;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.service.ServiceFactory;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.inte.InteServiceHelper;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.util.HRDBUtil;
import kd.hr.hbp.opplugin.web.history.uniquevolidator.GroupKey;
import kd.hr.hbp.opplugin.web.history.uniquevolidator.GroupKeyBuilder;

public class HisGrpFieldsUniqueValidator
extends AbstractValidator
implements HisModelConstants {
    private static final Log LOGGER = LogFactory.getLog(HisGrpFieldsUniqueValidator.class);
    private final List<String> ENABLE_LANG_NUMBER = new ArrayList<String>(5);
    private final Map<String, String> ENABLE_LANG_NUMBE2_NAME = new HashMap<String, String>(5);
    private List<String> fieldKeys = null;
    private GroupKeyBuilder groupKeyBuilder = null;
    Map<GroupKey, List<ExtendedDataEntity>> extraAddMessageGroup = new HashMap<GroupKey, List<ExtendedDataEntity>>();
    private Set<Object> pkIdsInMemory = null;
    private Set<String> multilangFieldSet = new HashSet<String>();
    private Map<Object, DynamicObject> multilangDynamicObjectMap = new HashMap<Object, DynamicObject>();

    protected void initValidateResult() {
        this.validateResult = new GroupFieldsUniqueValidateResult();
    }

    protected List<String> getFields() {
        if (this.fieldKeys == null) {
            this.fieldKeys = new ArrayList<String>();
            List fieldsMap = (List)this.getValidation().get("fields");
            if (fieldsMap != null && !fieldsMap.isEmpty()) {
                for (Map fMap : fieldsMap) {
                    this.fieldKeys.add((String)fMap.get("id"));
                }
            }
        }
        return this.fieldKeys;
    }

    protected boolean isIgnoreDB() {
        return (Boolean)this.getValidation().get("isCheckAllEntity");
    }

    protected boolean isCheckMultilang() {
        if (this.getValidation() != null) {
            Object isCheckMultilang = this.getValidation().get("isCheckMultilang");
            if (ObjectUtils.isEmpty(isCheckMultilang)) {
                return false;
            }
            return (Boolean)isCheckMultilang;
        }
        return false;
    }

    protected boolean isIgnoreBlank() {
        ICodeRuleService codeRuleService;
        List rules;
        String billNo;
        if (this.getValidateContext() != null && this.getValidateContext().getBillEntityType() instanceof BillEntityType && StringUtils.isNotBlank((CharSequence)(billNo = ((BillEntityType)this.getValidateContext().getBillEntityType()).getBillNo())) && this.getFields().contains(billNo) && this.isSkipBillNoValidator() && !(rules = (codeRuleService = (ICodeRuleService)ServiceFactory.getService(ICodeRuleService.class)).getAllCodeRuleByEntity(this.getValidateContext().getBillEntityType().getName())).isEmpty()) {
            return true;
        }
        return (Boolean)this.getValidation().get("isCheckEmptyValue");
    }

    private boolean isSkipBillNoValidator() {
        return this.getOption() != null && Boolean.parseBoolean(this.getOption().getVariableValue("skipbillnovalidator", String.valueOf(false)));
    }

    protected LocaleString getValidateDesc() {
        return LocaleString.fromMap((Map)((Map)this.getValidationValueByKey("description")));
    }

    public Set<String> preparePropertys() {
        Set set = super.preparePropertys();
        List<String> fields = this.getFields();
        set.addAll(fields);
        return set;
    }

    public void initializeConfiguration() {
        super.initializeConfiguration();
        this.getEnableLangs();
        long startTime = System.currentTimeMillis();
        List<String> fields = this.getFields();
        boolean ignoreDB = this.isIgnoreDB();
        boolean ignoreBlank = this.isIgnoreBlank();
        boolean checkMultilang = this.isCheckMultilang();
        LocaleString validateDesc = this.getValidateDesc();
        this.groupKeyBuilder = new GroupKeyBuilder(this.getValidateContext().getSubEntityType(), this.getValidateContext().getOperateMetaMap(), fields, ignoreDB, ignoreBlank, checkMultilang);
        if (this.groupKeyBuilder.error) {
            throw new KDBizException(String.format(ResManager.loadKDString((String)"%1$s-%2$s\uff0c%3$s\uff1a\u914d\u7f6e\u9519\u8bef\uff0c%4$s", (String)"HisGrpFieldsUniqueValidator_0", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), this.getValidateContext().getBillEntityType().getDisplayName().toString(), this.getOperationName(), validateDesc.toString(), this.groupKeyBuilder.errMsg));
        }
        this.setEntityKey(this.groupKeyBuilder.entityKey);
        ((GroupFieldsUniqueValidateResult)this.getValidateResult()).setFieldKeys(this.groupKeyBuilder.propNames);
        ((GroupFieldsUniqueValidateResult)this.getValidateResult()).setFieldCaptions(this.groupKeyBuilder.fldCaptions);
        for (String prop : this.groupKeyBuilder.propNames) {
            DynamicProperty property = this.validateContext.getBillEntityType().getProperty(prop);
            if (!(property instanceof MuliLangTextProp)) continue;
            this.multilangFieldSet.add(prop);
        }
        LOGGER.info("HisGrpFieldsUniqueValidator_initializeConfiguration_cost time: {}, data count: {}", (Object)(startTime - System.currentTimeMillis()), (Object)this);
    }

    public void validate() {
        long begin = System.currentTimeMillis();
        this.pkIdsInMemory = new HashSet<Object>(this.dataEntities.length);
        Map<GroupKey, List<ExtendedDataEntity>> noRepeatedGroups = this.checkRepeatedInMemory();
        if (!this.groupKeyBuilder.ignoreDB) {
            this.checkRepeatedInDB(noRepeatedGroups);
        }
        long end = System.currentTimeMillis();
        LOGGER.info("HisGrpFieldsUniqueValidator: OperateKey = {} , validate_time = {} , dataCount = {} ", new Object[]{this.getOperateKey(), end - begin, this.getDataEntities().length});
    }

    private Map<GroupKey, List<ExtendedDataEntity>> checkRepeatedInMemory() {
        HashMap<GroupKey, ArrayList<ExtendedDataEntity>> groups = new HashMap<GroupKey, ArrayList<ExtendedDataEntity>>();
        for (ExtendedDataEntity dataEntity : this.dataEntities) {
            GroupKey groupKey = this.groupKeyBuilder.build(dataEntity);
            if (groupKey == null) continue;
            this.pkIdsInMemory.add(groupKey.getPkValue());
            ArrayList<ExtendedDataEntity> list = (ArrayList<ExtendedDataEntity>)groups.get(groupKey);
            if (list == null) {
                list = new ArrayList<ExtendedDataEntity>();
                groups.put(groupKey, list);
            }
            list.add(dataEntity);
        }
        HashMap<GroupKey, List<ExtendedDataEntity>> noRepeatedGroups = new HashMap<GroupKey, List<ExtendedDataEntity>>(groups.size());
        for (Map.Entry group : groups.entrySet()) {
            noRepeatedGroups.put((GroupKey)group.getKey(), (List<ExtendedDataEntity>)group.getValue());
            int repeatedRowCount = ((List)group.getValue()).size();
            ExtendedDataEntity firstDataEntity = (ExtendedDataEntity)((List)group.getValue()).get(0);
            if (repeatedRowCount <= 1) continue;
            long firstBoid = firstDataEntity.getDataEntity().getDataEntityType() instanceof EntryType ? ((DynamicObject)firstDataEntity.getDataEntity().getParent()).getLong("boid") : firstDataEntity.getDataEntity().getLong("boid");
            String errMsg = this.buildErrMsg((GroupKey)group.getKey());
            for (int i = 1; i < repeatedRowCount; ++i) {
                ExtendedDataEntity repeatedDataEntity = (ExtendedDataEntity)((List)group.getValue()).get(i);
                long boid = repeatedDataEntity.getDataEntity().getDataEntityType() instanceof EntryType ? ((DynamicObject)repeatedDataEntity.getDataEntity().getParent()).getLong("boid") : repeatedDataEntity.getDataEntity().getLong("boid");
                if (firstBoid != 0L && boid != 0L && firstBoid == boid) {
                    if (this.groupKeyBuilder.ignoreDB) {
                        this.addMessage(repeatedDataEntity, errMsg, this.getErrorLevl());
                        continue;
                    }
                    this.extraAddMessageGroup.computeIfAbsent((GroupKey)group.getKey(), (Function<GroupKey, List<ExtendedDataEntity>>)((Function<GroupKey, List>)value -> new ArrayList())).add(repeatedDataEntity);
                    continue;
                }
                this.addMessage(repeatedDataEntity, errMsg, this.getErrorLevl());
            }
        }
        return noRepeatedGroups;
    }

    private void checkRepeatedInDB(Map<GroupKey, List<ExtendedDataEntity>> groups) {
        if (groups.isEmpty()) {
            return;
        }
        if (this.groupKeyBuilder.fullPropNames.size() == 1) {
            this.checkRepeatedInDB1(groups);
        } else {
            this.checkRepeatedInDBMore(groups);
        }
    }

    private void checkRepeatedInDB1(Map<GroupKey, List<ExtendedDataEntity>> groups) {
        HashMap<GroupKey, GroupKey> groupKeys = new HashMap<GroupKey, GroupKey>(groups.size());
        for (Map.Entry<GroupKey, List<ExtendedDataEntity>> group : groups.entrySet()) {
            GroupKey groupKey = group.getKey();
            Object fldValue = groupKey.getFldValues()[0];
            if (StringUtils.isBlank((Object)fldValue)) {
                fldValue = null;
            }
            GroupKey key = new GroupKey(0, null, new Object[]{fldValue}, new Object[]{fldValue});
            key.setCheckMultilang(this.isCheckMultilang());
            groupKeys.put(key, groupKey);
        }
        String selectFields = this.buildSelectFields();
        QFilter[] fs = this.buildHisFilters(groups);
        if (fs == null) {
            return;
        }
        DynamicObjectCollection repObjs = QueryServiceHelper.query((String)this.validateContext.getEntityNumber(), (String)selectFields, (QFilter[])fs);
        if (this.isCheckMultilang() && this.checkHasMultilang()) {
            ArrayList<Object> pkList = new ArrayList<Object>();
            for (DynamicObject dynamicObject : repObjs) {
                pkList.add(dynamicObject.get("id"));
            }
            this.multilangDynamicObjectMap = this.loadMultilangField(this.validateContext.getEntityNumber(), pkList);
        }
        HashMap<GroupKey, List> repeatData = new HashMap<GroupKey, List>();
        if (repObjs != null && !repObjs.isEmpty()) {
            for (DynamicObject dynamicObject : repObjs) {
                DynamicObject dynamicObject2;
                Object pkId = dynamicObject.get("id");
                Long boid = (Long)dynamicObject.get("boid");
                if (this.pkIdsInMemory.contains(pkId)) continue;
                String propFieldName = (String)this.groupKeyBuilder.propNames.get(0);
                Object fldValue = dynamicObject.get(propFieldName);
                if (StringUtils.isBlank((Object)fldValue)) {
                    fldValue = null;
                }
                if (fldValue instanceof Timestamp) {
                    fldValue = new Date(((Timestamp)fldValue).getTime());
                }
                if (this.multilangFieldSet.contains(propFieldName) && (dynamicObject2 = this.multilangDynamicObjectMap.get(pkId)) != null) {
                    fldValue = dynamicObject2.get(propFieldName);
                }
                GroupKey key = new GroupKey(0, null, new Object[]{fldValue}, new Object[]{fldValue});
                key.setCheckMultilang(this.isCheckMultilang());
                GroupKey groupKey = this.getGroupKey(groupKeys, key);
                if (groupKey != null) {
                    List<ExtendedDataEntity> list = groups.get(groupKey);
                    if (list == null || list.isEmpty()) continue;
                    ExtendedDataEntity dataEntity = list.get(0);
                    long dataBoid = dataEntity.getDataEntity().getDataEntityType() instanceof EntryType ? ((DynamicObject)dataEntity.getDataEntity().getParent()).getLong("boid") : dataEntity.getDataEntity().getLong("boid");
                    if (!boid.equals(dataBoid)) {
                        repeatData.computeIfAbsent(groupKey, value -> new ArrayList()).add(dynamicObject);
                    }
                }
                if (!groupKeys.isEmpty()) continue;
                break;
            }
        }
        for (Map.Entry entry : repeatData.entrySet()) {
            List<ExtendedDataEntity> dataEntities;
            GroupKey key = (GroupKey)entry.getKey();
            List value2 = (List)entry.getValue();
            String errMsg = "";
            if (groups.containsKey(key)) {
                dataEntities = groups.get(key);
                ExtendedDataEntity dataEntity = dataEntities.get(0);
                errMsg = this.buildErrorMsg(key, value2);
                this.addMessage(dataEntity, errMsg, this.getErrorLevl());
            }
            if (!this.extraAddMessageGroup.containsKey(key)) continue;
            dataEntities = this.extraAddMessageGroup.get(key);
            for (ExtendedDataEntity dataEntity : dataEntities) {
                this.addMessage(dataEntity, errMsg, this.getErrorLevl());
            }
        }
    }

    private GroupKey getGroupKey(Map<GroupKey, GroupKey> groupKeys, GroupKey key) {
        if (groupKeys == null || groupKeys.size() == 0) {
            return null;
        }
        for (Map.Entry<GroupKey, GroupKey> gkEntry : groupKeys.entrySet()) {
            GroupKey gk = gkEntry.getKey();
            if (!gk.equalsIgnoreCase((Object)key)) continue;
            return gkEntry.getValue();
        }
        return null;
    }

    @ExcludeFromJacocoGeneratedReport
    private String buildErrorMsg(GroupKey groupKey, List<DynamicObject> repObjs) {
        String errMsg;
        block18: {
            String fldNames;
            if (repObjs == null || repObjs.size() == 0) {
                return null;
            }
            Object fldValue = groupKey.getFldValues()[0];
            Object fldValueDes = groupKey.getFldValuesDesc()[0];
            errMsg = "";
            boolean hasNumberField = false;
            MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)this.validateContext.getEntityNumber());
            if (dataEntityType.getAllFields().containsKey("number")) {
                hasNumberField = true;
            }
            HashMap<Long, List> repeatData = new HashMap<Long, List>();
            for (DynamicObject repObj : repObjs) {
                Long boid = repObj.getLong("boid");
                repeatData.computeIfAbsent(boid, key -> new ArrayList()).add(repObj);
            }
            List dys = (List)repeatData.entrySet().iterator().next().getValue();
            boolean iscurrentversion = dys.stream().anyMatch(dy -> dy.getBoolean("iscurrentversion"));
            if (iscurrentversion) {
                if (this.getFields().size() == 1) {
                    errMsg = fldValue == null || (fldValue.equals(-1) || fldValue.equals("")) && !fldValue.equals(fldValueDes) || fldValue.equals("") && fldValue.equals(fldValueDes) ? String.format(ResManager.loadKDString((String)"\u201c%s\u201d \u7a7a\u503c\u5df2\u5b58\u5728\uff0c\u8bf7\u8f93\u5165\u5176\u4ed6\u503c\u3002", (String)"HisGrpFieldsUniqueValidator_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), this.groupKeyBuilder.fldNames) : (fldValue instanceof OrmLocaleValue && ((OrmLocaleValue)fldValue).size() == 0 ? String.format(ResManager.loadKDString((String)"\u201c%s\u201d \u7a7a\u503c\u5df2\u5b58\u5728\uff0c\u8bf7\u8f93\u5165\u5176\u4ed6\u503c\u3002", (String)"HisGrpFieldsUniqueValidator_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), this.groupKeyBuilder.fldNames) : this.returnErrMsg(fldValue, dys, groupKey, errMsg));
                } else if (this.isIgnoreDB()) {
                    fldNames = Arrays.stream(this.groupKeyBuilder.fldNames.split("\\+")).map(name -> "\u201c" + name + "\u201d").collect(Collectors.joining("\u3001"));
                    errMsg = String.format(ResManager.loadKDString((String)"%s \u7684\u7ec4\u5408\u503c\u91cd\u590d\uff0c\u8bf7\u81f3\u5c11\u4fee\u6539\u4e00\u9879\u3002", (String)"HisGrpFieldsUniqueValidator_2", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), fldNames);
                } else {
                    fldNames = Arrays.stream(this.groupKeyBuilder.fldNames.split("\\+")).map(name -> "\u201c" + name + "\u201d").collect(Collectors.joining("\u3001"));
                    errMsg = String.format(ResManager.loadKDString((String)"%s \u7684\u7ec4\u5408\u503c\u4e0e\u5176\u4ed6\u8868\u5355\u91cd\u590d\uff0c\u8bf7\u81f3\u5c11\u4fee\u6539\u4e00\u9879\u3002", (String)"HisGrpFieldsUniqueValidator_3", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), fldNames);
                }
            } else if (hasNumberField) {
                HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(this.validateContext.getEntityNumber());
                DynamicObject dy2 = serviceHelper.queryOriginalOne("id,number", new QFilter("boid", "=", ((DynamicObject)dys.get(0)).get("boid")).and(new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE)));
                try {
                    if (this.getFields().size() == 1) {
                        errMsg = this.returnErrMsg2(fldValue, dys, groupKey, errMsg, dy2.getString("number"));
                        break block18;
                    }
                    String fldNames2 = Arrays.stream(this.groupKeyBuilder.fldNames.split("\\+")).map(name -> "\u201c" + name + "\u201d").collect(Collectors.joining("\u3001"));
                    errMsg = String.format(ResManager.loadKDString((String)"\u4e0e\u57fa\u7840\u8d44\u6599\u201c%1$s\u201d\u5386\u53f2\u7248\u672c\u7684\u201c%2$s\u201d \u7684\u7ec4\u5408\u503c\u91cd\u590d\uff0c\u8bf7\u81f3\u5c11\u4fee\u6539\u4e00\u9879\u3002", (String)"HisGrpFieldsUniqueValidator_4", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), dy2.getString("number"), fldNames2);
                }
                catch (Exception e) {
                    errMsg = ResManager.loadKDString((String)"\u6570\u636e\u6821\u9a8c\u5f02\u5e38\u3002", (String)"HisGrpFieldsUniqueValidator_5", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]);
                }
            } else if (this.getFields().size() == 1) {
                errMsg = fldValue == null || (fldValue.equals(-1) || fldValue.equals("")) && !fldValue.equals(fldValueDes) || fldValue.equals("") && fldValue.equals(fldValueDes) ? String.format(ResManager.loadKDString((String)"\u201c%s\u201d \u7a7a\u503c\u5df2\u5b58\u5728\uff0c\u8bf7\u8f93\u5165\u5176\u4ed6\u503c\u3002", (String)"HisGrpFieldsUniqueValidator_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), this.groupKeyBuilder.fldNames) : (fldValue instanceof OrmLocaleValue && ((OrmLocaleValue)fldValue).size() == 0 ? String.format(ResManager.loadKDString((String)"\u201c%s\u201d \u7a7a\u503c\u5df2\u5b58\u5728\uff0c\u8bf7\u8f93\u5165\u5176\u4ed6\u503c\u3002", (String)"HisGrpFieldsUniqueValidator_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), this.groupKeyBuilder.fldNames) : this.returnErrMsg(fldValue, dys, groupKey, errMsg));
            } else if (this.isIgnoreDB()) {
                fldNames = Arrays.stream(this.groupKeyBuilder.fldNames.split("\\+")).map(name -> "\u201c" + name + "\u201d").collect(Collectors.joining("\u3001"));
                errMsg = String.format(ResManager.loadKDString((String)"%s \u7684\u7ec4\u5408\u503c\u91cd\u590d\uff0c\u8bf7\u81f3\u5c11\u4fee\u6539\u4e00\u9879\u3002", (String)"HisGrpFieldsUniqueValidator_2", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), fldNames);
            } else {
                fldNames = Arrays.stream(this.groupKeyBuilder.fldNames.split("\\+")).map(name -> "\u201c" + name + "\u201d").collect(Collectors.joining("\u3001"));
                errMsg = String.format(ResManager.loadKDString((String)"%s \u7684\u7ec4\u5408\u503c\u4e0e\u5176\u4ed6\u8868\u5355\u91cd\u590d\uff0c\u8bf7\u81f3\u5c11\u4fee\u6539\u4e00\u9879\u3002", (String)"HisGrpFieldsUniqueValidator_3", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), fldNames);
            }
        }
        return errMsg;
    }

    private String returnErrMsg(Object fldValue, List<DynamicObject> dys, GroupKey groupKey, String errMsg) {
        StringBuilder fields = new StringBuilder();
        if (this.multilangDynamicObjectMap.size() != 0 && fldValue instanceof OrmLocaleValue && ((OrmLocaleValue)fldValue).size() != 0) {
            OrmLocaleValue obj = null;
            for (DynamicObject dy : dys) {
                Object[] fldValues;
                if (!dy.getBoolean("iscurrentversion")) continue;
                long id = dy.getLong("id");
                HRBaseServiceHelper helper = new HRBaseServiceHelper(this.getEntityKey());
                QFilter filter = new QFilter("id", "=", (Object)id);
                DynamicObject result = helper.loadOne((String)this.groupKeyBuilder.propNames.get(0), filter.toArray());
                obj = (OrmLocaleValue)result.get((String)this.groupKeyBuilder.propNames.get(0));
                for (Object fldVal : fldValues = groupKey.getFldValues()) {
                    if (!(fldVal instanceof OrmLocaleValue)) continue;
                    Set filedEntrySet = ((OrmLocaleValue)fldVal).entrySet();
                    for (Map.Entry fieldEntry : filedEntrySet) {
                        String fieldKey = (String)fieldEntry.getKey();
                        String fieldValue = (String)fieldEntry.getValue();
                        if (!fieldValue.equalsIgnoreCase(obj.get((Object)fieldKey)) || fieldKey.equalsIgnoreCase("GLang") || !this.getEnableLangs().contains(fieldKey)) continue;
                        fields.append(this.ENABLE_LANG_NUMBE2_NAME.get(fieldKey)).append(" ").append(fieldValue).append("\uff0c");
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty((CharSequence)fields)) {
            String fieldinfo = fields.substring(0, fields.length() - 1);
            errMsg = String.format(ResManager.loadKDString((String)"\u201c%1$s\u201d \u503c \u201c%2$s\u201d \u5df2\u5b58\u5728\uff0c\u8bf7\u8f93\u5165\u5176\u4ed6\u503c\u3002", (String)"HisGrpFieldsUniqueValidator_6", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), this.groupKeyBuilder.fldNames, fieldinfo);
        } else {
            errMsg = String.format(ResManager.loadKDString((String)"\u201c%1$s\u201d \u503c \u201c%2$s\u201d \u5df2\u5b58\u5728\uff0c\u8bf7\u8f93\u5165\u5176\u4ed6\u503c\u3002", (String)"HisGrpFieldsUniqueValidator_6", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), this.groupKeyBuilder.fldNames, groupKey.getFldValuesDesc()[0].toString());
        }
        return errMsg;
    }

    @ExcludeFromJacocoGeneratedReport
    private String returnErrMsg2(Object fldValue, List<DynamicObject> dys, GroupKey groupKey, String errMsg, String number) {
        StringBuilder fields = new StringBuilder();
        if (this.multilangDynamicObjectMap.size() != 0 && fldValue instanceof OrmLocaleValue && ((OrmLocaleValue)fldValue).size() != 0) {
            OrmLocaleValue obj = null;
            for (DynamicObject dy : dys) {
                Object[] fldValues;
                long id = dy.getLong("id");
                HRBaseServiceHelper helper = new HRBaseServiceHelper(this.getEntityKey());
                QFilter filter = new QFilter("id", "=", (Object)id);
                DynamicObject result = helper.queryOne((String)this.groupKeyBuilder.propNames.get(0), filter.toArray());
                obj = (OrmLocaleValue)result.get((String)this.groupKeyBuilder.propNames.get(0));
                for (Object fldVal : fldValues = groupKey.getFldValues()) {
                    if (!(fldVal instanceof OrmLocaleValue)) continue;
                    Set filedEntrySet = ((OrmLocaleValue)fldVal).entrySet();
                    for (Map.Entry fieldEntry : filedEntrySet) {
                        String fieldKey = (String)fieldEntry.getKey();
                        String fieldValue = (String)fieldEntry.getValue();
                        if (!fieldValue.equalsIgnoreCase(obj.get((Object)fieldKey)) || fieldKey.equalsIgnoreCase("GLang") || !this.getEnableLangs().contains(fieldKey)) continue;
                        fields.append(this.ENABLE_LANG_NUMBE2_NAME.get(fieldKey)).append(" ").append(fieldValue).append("\uff0c");
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty((CharSequence)fields)) {
            String fieldinfo = fields.substring(0, fields.length() - 1);
            errMsg = String.format(ResManager.loadKDString((String)"\u201c%1$s\u201d \u503c \u201c%2$s\u201d \u4e0e\u57fa\u7840\u8d44\u6599\u201c%3$s\u201d\u5386\u53f2\u7248\u672c\u7684\u201c%1$s\u201d \u5b57\u6bb5\u91cd\u590d\u3002", (String)"HisGrpFieldsUniqueValidator_7", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), this.groupKeyBuilder.fldNames, fieldinfo, number);
        } else {
            errMsg = String.format(ResManager.loadKDString((String)"\u201c%1$s\u201d \u503c \u201c%2$s\u201d \u4e0e\u57fa\u7840\u8d44\u6599\u201c%3$s\u201d\u5386\u53f2\u7248\u672c\u7684\u201c%1$s\u201d \u5b57\u6bb5\u91cd\u590d\u3002", (String)"HisGrpFieldsUniqueValidator_7", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), this.groupKeyBuilder.fldNames, groupKey.getFldValuesDesc()[0].toString(), number);
        }
        return errMsg;
    }

    private void checkRepeatedInDBMore(Map<GroupKey, List<ExtendedDataEntity>> groups) {
        QFilter[] fs = this.buildHisFilters(groups);
        String selectedFields = this.buildSelectFields();
        ORM orm = ORM.create();
        try (DataSet allData = orm.queryDataSet("kd.bos.service.operation.validate", this.validateContext.getEntityNumber(), selectedFields, fs);
             DataSet allDataCopy = allData.copy();){
            Object pkId;
            if (!allDataCopy.hasNext()) {
                return;
            }
            if (this.isCheckMultilang() && this.checkHasMultilang()) {
                DataSet idCopySet = allDataCopy.copy();
                ArrayList<Object> pkList = new ArrayList<Object>(10);
                while (idCopySet.hasNext()) {
                    Row next = idCopySet.next();
                    pkId = next.get("id");
                    pkList.add(pkId);
                }
                this.multilangDynamicObjectMap = this.loadMultilangField(this.validateContext.getEntityNumber(), pkList);
            }
            Set<Long> boidsInMemory = this.getBoidSet(groups);
            HashMap<GroupKey, List<DynamicObject>> repeatData = new HashMap<GroupKey, List<DynamicObject>>();
            while (allDataCopy.hasNext()) {
                boolean isMatch;
                Row row = allDataCopy.next();
                pkId = row.get("id");
                Long boid = (Long)row.get("boid");
                if (this.pkIdsInMemory.contains(pkId) || boidsInMemory.contains(boid) || (isMatch = this.matchRepeateRow(groups, row, false, repeatData))) continue;
                isMatch = this.matchRepeateRow(groups, row, true, repeatData);
            }
            for (Map.Entry entry : repeatData.entrySet()) {
                List<ExtendedDataEntity> dataEntities;
                GroupKey key = (GroupKey)entry.getKey();
                List value = (List)entry.getValue();
                String errMsg = "";
                if (groups.containsKey(key)) {
                    dataEntities = groups.get(key);
                    ExtendedDataEntity dataEntity = dataEntities.get(0);
                    errMsg = this.buildErrorMsg(key, value);
                    this.addMessage(dataEntity, errMsg, this.getErrorLevl());
                }
                if (!this.extraAddMessageGroup.containsKey(key)) continue;
                dataEntities = this.extraAddMessageGroup.get(key);
                for (ExtendedDataEntity dataEntity : dataEntities) {
                    this.addMessage(dataEntity, errMsg, this.getErrorLevl());
                }
            }
        }
    }

    private Set<Long> getBoidSet(Map<GroupKey, List<ExtendedDataEntity>> groups) {
        HashSet<Long> boidsInMemory = new HashSet<Long>(16);
        for (Map.Entry<GroupKey, List<ExtendedDataEntity>> entry : groups.entrySet()) {
            ExtendedDataEntity dataEntity = entry.getValue().get(0);
            long boid = dataEntity.getDataEntity().getDataEntityType() instanceof EntryType ? ((DynamicObject)dataEntity.getDataEntity().getParent()).getLong("boid") : dataEntity.getDataEntity().getLong("boid");
            boidsInMemory.add(boid);
        }
        return boidsInMemory;
    }

    private boolean matchRepeateRow(Map<GroupKey, List<ExtendedDataEntity>> groups, Row row, boolean ignoreCase, Map<GroupKey, List<DynamicObject>> repeatData) {
        boolean isMatch = false;
        for (Map.Entry<GroupKey, List<ExtendedDataEntity>> group : groups.entrySet()) {
            GroupKey groupKey = group.getKey();
            List<ExtendedDataEntity> list = group.getValue();
            if (list.isEmpty()) continue;
            boolean hasMatchData = true;
            for (int i = 0; i < this.groupKeyBuilder.fullPropNames.size(); ++i) {
                String propName = ((IDataEntityProperty)this.groupKeyBuilder.props.get(i)).getName();
                Object fldValue = groupKey.getFldValues()[i];
                if (this.isCheckMultilang() && this.multilangFieldSet.contains(propName)) {
                    OrmLocaleValue fldOrmLocalvalue = (OrmLocaleValue)fldValue;
                    Object pkid = row.get("id");
                    DynamicObject multilangObject = this.multilangDynamicObjectMap.get(pkid);
                    OrmLocaleValue multilangPropValue = (OrmLocaleValue)multilangObject.get(propName);
                    if (HisGrpFieldsUniqueValidator.checkMultilangHasEqualItem(fldOrmLocalvalue, multilangPropValue, this.isCheckMultilang(), ignoreCase)) continue;
                    hasMatchData = false;
                    break;
                }
                if (this.equalsValue(fldValue, row.get(propName), ignoreCase)) continue;
                hasMatchData = false;
                break;
            }
            if (!hasMatchData) continue;
            isMatch = true;
            HRBaseServiceHelper helper = new HRBaseServiceHelper(this.validateContext.getEntityNumber());
            DynamicObject dy = helper.generateEmptyDynamicObject();
            dy.set("id", row.get("id"));
            dy.set("boid", (Object)row.getLong("boid"));
            dy.set("iscurrentversion", (Object)row.getBoolean("iscurrentversion"));
            repeatData.computeIfAbsent(groupKey, key -> new ArrayList()).add(dy);
        }
        return isMatch;
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean equalsValue(Object v1, Object v2, boolean stringIgnoreCase) {
        if (v1 == null) {
            return v2 == null;
        }
        if (v1 instanceof Integer && v2 instanceof Long) {
            return ((Integer)v1).longValue() == ((Long)v2).longValue();
        }
        if (v1 instanceof Long && v2 instanceof Integer) {
            return ((Long)v1).longValue() == ((Integer)v2).longValue();
        }
        if (v1 instanceof BigDecimal && v2 instanceof BigDecimal) {
            return ((BigDecimal)v1).compareTo((BigDecimal)v2) == 0;
        }
        if (v1 instanceof String && v2 instanceof String && stringIgnoreCase) {
            return ((String)v1).trim().equalsIgnoreCase(((String)v2).trim());
        }
        if (v1 instanceof String && v2 instanceof String) {
            return ((String)v1).trim().equals(((String)v2).trim());
        }
        return v1.equals(v2);
    }

    private QFilter[] buildHisFilters(Map<GroupKey, List<ExtendedDataEntity>> groups) {
        int size = this.groupKeyBuilder.fullPropNames.size();
        ArrayList fldValues = new ArrayList(size);
        Boolean[] fldIsNulls = new Boolean[size];
        Boolean[] fldIsBlanks = new Boolean[size];
        for (int i = 0; i < size; ++i) {
            fldValues.add(new HashSet());
            fldIsNulls[i] = Boolean.FALSE;
            fldIsBlanks[i] = Boolean.FALSE;
        }
        HashMap mapMultiLang = Maps.newHashMapWithExpectedSize((int)16);
        for (Map.Entry<GroupKey, List<ExtendedDataEntity>> group : groups.entrySet()) {
            for (int i = 0; i < size; ++i) {
                Object fldValue = group.getKey().getFldValues()[i];
                if (fldValue == null) {
                    fldIsNulls[i] = Boolean.TRUE;
                    continue;
                }
                if (StringUtils.isBlank((Object)fldValue)) {
                    fldIsBlanks[i] = Boolean.TRUE;
                    continue;
                }
                if (fldValue instanceof OrmLocaleValue) {
                    OrmLocaleValue ormLocaleValue = (OrmLocaleValue)fldValue;
                    this.buildMultiLangValues(mapMultiLang, ormLocaleValue);
                    continue;
                }
                ((Set)fldValues.get(i)).add(fldValue);
            }
        }
        ArrayList qFilterList = Lists.newArrayListWithCapacity((int)10);
        for (int i = 0; i < size; ++i) {
            QFilter fldFilter;
            String currFullPropName = (String)this.groupKeyBuilder.fullPropNames.get(i);
            Set currFldVals = (Set)fldValues.get(i);
            Boolean hasNullValue = fldIsNulls[i];
            Boolean hasBlandValue = fldIsBlanks[i];
            QFilter nullFilter = null;
            if (hasNullValue.booleanValue()) {
                nullFilter = this.multilangFieldSet.contains(currFullPropName) ? new QFilter(currFullPropName, "is null", null).or(currFullPropName, "=", (Object)"").or(currFullPropName, "=", (Object)" ") : new QFilter(currFullPropName, "is null", null);
            } else if (hasBlandValue.booleanValue()) {
                nullFilter = new QFilter(currFullPropName, "=", (Object)" ").or(currFullPropName, "=", (Object)"");
            }
            if (currFldVals.isEmpty()) {
                fldFilter = nullFilter;
            } else {
                fldFilter = currFldVals.size() > 1 ? new QFilter(currFullPropName, "in", (Object)currFldVals.toArray()) : new QFilter(currFullPropName, "=", currFldVals.iterator().next());
                if (fldFilter != null && nullFilter != null) {
                    fldFilter.or(nullFilter);
                }
            }
            if (fldFilter != null) {
                qFilterList.add(fldFilter);
            }
            if (!this.isCheckMultilang() || !this.multilangFieldSet.contains(currFullPropName)) continue;
            Set<Object> idSet = this.buildIdSet(mapMultiLang, currFullPropName);
            if (CollectionUtils.isNotEmpty(idSet)) {
                qFilterList.add(new QFilter("id", "in", idSet));
                continue;
            }
            return null;
        }
        if (!this.checkAData() && this.groupKeyBuilder.statusProp != null && StringUtils.isNotBlank((CharSequence)this.groupKeyBuilder.statusDefValue)) {
            qFilterList.add(new QFilter(this.groupKeyBuilder.statusProp.getName(), "!=", (Object)this.groupKeyBuilder.statusDefValue));
        }
        if (!qFilterList.isEmpty()) {
            qFilterList.add(new QFilter("datastatus", "!=", (Object)HisModelDataStatusEnum.DISCARDED.getStatus()));
        }
        return qFilterList.toArray(new QFilter[qFilterList.size()]);
    }

    protected boolean checkAData() {
        if (this.getValidation() != null) {
            Object checkAData = this.getValidation().get("checkadata");
            return ObjectUtils.isEmpty(checkAData) ? false : (Boolean)checkAData;
        }
        return false;
    }

    private String buildSelectFields() {
        String billNo;
        StringBuilder selectFldBuilder = new StringBuilder();
        selectFldBuilder.append("id");
        if (this.validateContext.getBillEntityType() instanceof BillEntityType && StringUtils.isNotBlank((CharSequence)(billNo = ((BillEntityType)this.validateContext.getBillEntityType()).getBillNo())) && !this.getFields().contains(billNo)) {
            selectFldBuilder.append(',').append(billNo);
        }
        for (int i = 0; i < this.groupKeyBuilder.fullPropNames.size(); ++i) {
            selectFldBuilder.append(',').append((String)this.groupKeyBuilder.fullPropNames.get(i)).append(' ').append((String)this.groupKeyBuilder.propNames.get(i));
        }
        selectFldBuilder.append(',').append("boid boid");
        selectFldBuilder.append(',').append("iscurrentversion iscurrentversion");
        return selectFldBuilder.toString();
    }

    @ExcludeFromJacocoGeneratedReport
    protected String buildErrMsg(GroupKey groupKey) {
        String errMsg = "";
        if (this.getFields().size() == 1) {
            errMsg = String.format(ResManager.loadKDString((String)"%s \u5df2\u5b58\u5728\u3002", (String)"HisGrpFieldsUniqueValidator_8", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), "\u201c" + this.groupKeyBuilder.fldNames + "\u201d");
        } else if (this.isIgnoreDB()) {
            String fldNames = Arrays.asList(this.groupKeyBuilder.fldNames.split("\\+")).stream().map(name -> "\u201c" + name + "\u201d").collect(Collectors.joining("\u3001"));
            errMsg = String.format(ResManager.loadKDString((String)"%s \u7684\u7ec4\u5408\u503c\u91cd\u590d\uff0c\u8bf7\u81f3\u5c11\u4fee\u6539\u4e00\u9879\u3002", (String)"HisGrpFieldsUniqueValidator_2", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), fldNames);
        } else {
            String fldNames = Arrays.asList(this.groupKeyBuilder.fldNames.split("\\+")).stream().map(name -> "\u201c" + name + "\u201d").collect(Collectors.joining("\u3001"));
            errMsg = String.format(ResManager.loadKDString((String)"%s \u7684\u7ec4\u5408\u503c\u4e0e\u5176\u4ed6\u8868\u5355\u91cd\u590d\uff0c\u8bf7\u81f3\u5c11\u4fee\u6539\u4e00\u9879\u3002", (String)"HisGrpFieldsUniqueValidator_3", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]), fldNames);
        }
        return errMsg;
    }

    public List<String> getEnableLangs() {
        if (this.ENABLE_LANG_NUMBER.isEmpty()) {
            List enabledLangs = InteServiceHelper.getEnabledLang();
            for (EnabledLang enabledLang : enabledLangs) {
                String enableNumber = enabledLang.getNumber();
                this.ENABLE_LANG_NUMBER.add(enableNumber);
                this.ENABLE_LANG_NUMBE2_NAME.put(enableNumber, enabledLang.getName());
            }
        }
        return this.ENABLE_LANG_NUMBER;
    }

    public Map<Object, DynamicObject> loadMultilangField(String entityNumber, List<Object> ids) {
        DynamicObject[] dynamicObjects;
        HashMap<Object, DynamicObject> result = new HashMap<Object, DynamicObject>(16);
        QFilter[] filters = new QFilter[]{new QFilter("id", "in", ids)};
        StringBuilder fieldBuild = new StringBuilder("id");
        for (String mfield : this.multilangFieldSet) {
            fieldBuild.append(",").append(mfield);
        }
        for (DynamicObject dynamicObject : dynamicObjects = BusinessDataServiceHelper.load((String)entityNumber, (String)fieldBuild.toString(), (QFilter[])filters)) {
            result.put(dynamicObject.get("id"), dynamicObject);
        }
        return result;
    }

    private boolean checkHasMultilang() {
        boolean hasMultilang = false;
        for (String prop : this.groupKeyBuilder.propNames) {
            DynamicProperty property = this.validateContext.getBillEntityType().getProperty(prop);
            if (!(property instanceof MuliLangTextProp)) continue;
            hasMultilang = true;
            break;
        }
        return hasMultilang;
    }

    static boolean checkMultilangHasEqualItem(OrmLocaleValue currentValue, OrmLocaleValue dbValue, boolean checkMultilang, boolean ignoreCase) {
        boolean equal = false;
        if (currentValue == null && dbValue == null) {
            equal = false;
        } else if (currentValue == null || dbValue == null) {
            equal = true;
        } else {
            if (currentValue.size() == 0 && dbValue.size() == 0) {
                equal = true;
                return equal;
            }
            for (String currentKey : currentValue.keySet()) {
                if (!checkMultilang && !currentKey.equals(Lang.get().name()) || !(equal = ignoreCase ? StringUtils.equalsIgnoreCase((CharSequence)currentValue.get((Object)currentKey), (CharSequence)dbValue.get((Object)currentKey)) : StringUtils.equals((CharSequence)currentValue.get((Object)currentKey), (CharSequence)dbValue.get((Object)currentKey)))) continue;
                break;
            }
        }
        return equal;
    }

    private void buildMultiLangValues(Map<String, List<String>> mapMultiLang, OrmLocaleValue ormLocaleValue) {
        Set keySet = ormLocaleValue.keySet();
        if (CollectionUtils.isEmpty((Collection)keySet)) {
            return;
        }
        for (String key : keySet) {
            if (!mapMultiLang.containsKey(key)) {
                mapMultiLang.put(key, Lists.newArrayListWithCapacity((int)10));
            }
            List<String> valueList = mapMultiLang.get(key);
            valueList.add(ormLocaleValue.getItem(key));
        }
    }

    private String buildInSql(List<String> valueList) {
        StringBuilder whereSql = new StringBuilder();
        whereSql.append("(");
        for (int i = 0; i < valueList.size(); ++i) {
            String value = valueList.get(i);
            if (i == valueList.size() - 1) {
                whereSql.append("'").append(value.replace("'", "''")).append("'");
                continue;
            }
            whereSql.append("'").append(value.replace("'", "''")).append("'").append(",");
        }
        whereSql.append(")");
        return whereSql.toString();
    }

    private Set<Object> buildIdSet(Map<String, List<String>> mapMultiLang, String currFullPropName) {
        if (mapMultiLang == null || mapMultiLang.isEmpty()) {
            return null;
        }
        String entityNumber = this.validateContext.getEntityNumber();
        MainEntityType entityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        String tableName = entityType.getAlias() + "_l";
        String fieldName = entityType.getProperty(currFullPropName).getAlias();
        if (StringUtils.isEmpty((CharSequence)tableName) || StringUtils.isEmpty((CharSequence)fieldName)) {
            return null;
        }
        StringBuilder sqlSb = new StringBuilder("select fid from ");
        sqlSb.append(tableName);
        sqlSb.append(" where ");
        for (Map.Entry<String, List<String>> entry : mapMultiLang.entrySet()) {
            List<String> valueList = entry.getValue();
            String key = entry.getKey();
            if ("GLang".equals(key)) continue;
            sqlSb.append(" flocaleid  = '").append(key).append("' and ");
            sqlSb.append(fieldName).append(" in ").append(this.buildInSql(valueList));
            sqlSb.append(" or ");
        }
        String sql = sqlSb.toString();
        sql = sql.substring(0, sql.length() - 4);
        try {
            Set ids = (Set)HRDBUtil.query((DBRoute)new DBRoute(entityType.getDBRouteKey()), (String)sql, null, rs -> {
                HashSet idSet = Sets.newHashSetWithExpectedSize((int)16);
                while (rs.next()) {
                    idSet.add(rs.getObject("fid"));
                }
                return idSet;
            });
            return ids;
        }
        catch (Exception ex) {
            LOGGER.info("HisGrpFieldsUniqueValidator query table error:" + ex.getMessage());
            throw new KDBizException(ResManager.loadKDString((String)"\u591a\u8bed\u8a00\u5b57\u6bb5\u552f\u4e00\u6027\u6821\u9a8c\u51fa\u9519\uff0c\u67e5\u8be2\u591a\u8bed\u8a00\u8868\u51fa\u9519\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u3002", (String)"HisGrpFieldsUniqueValidator_9", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
        }
    }
}
