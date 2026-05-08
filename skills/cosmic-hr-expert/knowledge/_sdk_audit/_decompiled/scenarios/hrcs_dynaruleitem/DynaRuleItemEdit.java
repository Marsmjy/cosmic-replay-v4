/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.AbstractGrid
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.mvc.bill.BillView
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.service.perm.dyna.DynaSchemeServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.log.RolePermLogServiceHelper
 *  org.apache.commons.collections4.CollectionUtils
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hrcs.formplugin.web.perm.dyna;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kd.bos.bill.BillOperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.control.AbstractGrid;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.mvc.bill.BillView;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.service.perm.dyna.DynaSchemeServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.log.RolePermLogServiceHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class DynaRuleItemEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final String F_DATATYPE = "datatype";
    private static final String F_ENTITYTYPE = "entitytype";
    private static final String V_DATATYPE_ENUM = "enum";
    private static final String V_DATATYPE_BD = "bd";
    private static final String V_DATATYPE_ORG = "org";
    private static final String FE_ENUM_VALUE = "value";
    private static final String FE_ENUM_DISPLAYVALUE = "displayvalue";
    private static final String F_IS_RELATE_PARAM = "isrelatparam";
    private static final String F_RELATE_RULE_ITEM = "relatruleparam";
    private static final String F_RELATE_PROP_KEY = "relatpropkey";
    private static final String F_RELATE_PROP_NAME = "relatpropname";
    private static final String V_VALSOURCETYPE_ENTITY = "1";
    private static final String F_VAL_SOURCE_TYPE = "valsourcetype";
    private static final String F_SOURCE_ENTITY_TYPE = "sourceentitytype";
    private static final String F_SOURCE_PROP_KEY = "sourcepropkey";
    private static final String F_SOURCE_PROP_NAME = "sourcepropname";
    private static final String F_MSERVICE_APP = "mserviceapp";
    private static final String F_MSERVICE_CLASS = "mserviceclass";
    private static final String CALLKEY_SELRELENTITYPROP = "selRelEntityProp";
    private static final String CALLKEY_SELSOURCEENTITYPROP = "selSourceEntityProp";
    private static final String F_ENUM_BAR = "enumbar";

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        BasedataEdit entityTypeBd = (BasedataEdit)this.getView().getControl(F_ENTITYTYPE);
        entityTypeBd.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        Toolbar enumToolBarAp = (Toolbar)this.getControl(F_ENUM_BAR);
        enumToolBarAp.addItemClickListener((ItemClickListener)this);
        BasedataEdit relateRuleItemBD = (BasedataEdit)this.getView().getControl(F_RELATE_RULE_ITEM);
        relateRuleItemBD.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit souRceEntityTypeBd = (BasedataEdit)this.getView().getControl(F_SOURCE_ENTITY_TYPE);
        souRceEntityTypeBd.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        this.addClickListeners(new String[]{F_RELATE_PROP_NAME});
        this.addClickListeners(new String[]{F_SOURCE_PROP_NAME});
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        this.fillRelateParamPropName();
        this.fillSourceEntityPropName();
        this.presetView();
        this.disableEnumEntry();
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String propertyName = args.getProperty().getName();
        ChangeData[] changeSet = args.getChangeSet();
        if (F_DATATYPE.equals(propertyName)) {
            String newDataType = (String)changeSet[0].getNewValue();
            if (V_DATATYPE_ORG.equals(newDataType)) {
                this.getModel().setValue(F_ENTITYTYPE, (Object)"haos_adminorghrf7");
            } else {
                this.getModel().setValue(F_ENTITYTYPE, null);
            }
        }
    }

    public void click(EventObject evt) {
        super.click(evt);
        Control c = (Control)evt.getSource();
        String ctrlKey = c.getKey();
        if (HRStringUtils.equals((String)F_RELATE_PROP_NAME, (String)ctrlKey)) {
            DynamicObject relateRuleParam = this.getModel().getDataEntity().getDynamicObject(F_RELATE_RULE_ITEM);
            if (null == relateRuleParam) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e3b\u89c4\u5219\u53c2\u6570\u9879\u3002", (String)"DynaRuleItemEdit_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return;
            }
            String entityType = relateRuleParam.getString("entitytype.id");
            this.showFieldSelF7(entityType, CALLKEY_SELRELENTITYPROP);
        } else if (HRStringUtils.equals((String)F_SOURCE_PROP_NAME, (String)ctrlKey)) {
            DynamicObject entityTypeDyo = this.getModel().getDataEntity().getDynamicObject(F_SOURCE_ENTITY_TYPE);
            if (null == entityTypeDyo) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u503c\u6765\u6e90\u5b9e\u4f53\u3002", (String)"DynaRuleItemEdit_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return;
            }
            this.showFieldSelF7(entityTypeDyo.getString("id"), CALLKEY_SELSOURCEENTITYPROP);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String propertyName = evt.getProperty().getName();
        if (F_ENTITYTYPE.equals(propertyName)) {
            ListShowParameter showParameter = (ListShowParameter)evt.getFormShowParameter();
            showParameter.getListFilterParameter().setFilter(new QFilter("modeltype", "=", (Object)"BaseFormModel"));
        } else if (HRStringUtils.equals((String)F_RELATE_RULE_ITEM, (String)propertyName)) {
            ListShowParameter showParameter = (ListShowParameter)evt.getFormShowParameter();
            QFilter notRelateParamFilter = new QFilter(F_IS_RELATE_PARAM, "=", (Object)Boolean.FALSE);
            showParameter.getListFilterParameter().getQFilters().add(notRelateParamFilter);
            QFilter dataTypeFilter = new QFilter(F_DATATYPE, "in", Arrays.asList(V_DATATYPE_BD, V_DATATYPE_ORG));
            showParameter.getListFilterParameter().getQFilters().add(dataTypeFilter);
        } else if (HRStringUtils.equals((String)F_SOURCE_ENTITY_TYPE, (String)propertyName)) {
            List<String> modelTypeList = Arrays.asList("BaseFormModel", "BillFormModel");
            ListShowParameter showParameter = (ListShowParameter)evt.getFormShowParameter();
            showParameter.getListFilterParameter().setFilter(new QFilter("modeltype", "in", modelTypeList));
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        String actionId = evt.getActionId();
        if (HRStringUtils.equals((String)CALLKEY_SELRELENTITYPROP, (String)actionId) || HRStringUtils.equals((String)CALLKEY_SELSOURCEENTITYPROP, (String)actionId)) {
            ListSelectedRowCollection res = (ListSelectedRowCollection)evt.getReturnData();
            if (null == res || res.isEmpty()) {
                return;
            }
            String key = (String)res.get(0).getPrimaryKeyValue();
            String[] infos = key.split("\\|\\|");
            if (HRStringUtils.equals((String)CALLKEY_SELRELENTITYPROP, (String)actionId)) {
                this.getModel().setValue(F_RELATE_PROP_KEY, (Object)infos[0]);
                this.getModel().setValue(F_RELATE_PROP_NAME, (Object)infos[1]);
            } else {
                this.getModel().setValue(F_SOURCE_PROP_KEY, (Object)infos[0]);
                this.getModel().setValue(F_SOURCE_PROP_NAME, (Object)infos[1]);
            }
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if (operateKey.equals("deleteentry")) {
            DynamicObject dataEntity = this.getModel().getDataEntity(true);
            Object[] schemeDynArr = DynaSchemeServiceHelper.queryRelDynaScheme((String)dataEntity.getString("id"));
            if (ArrayUtils.isEmpty((Object[])schemeDynArr)) {
                return;
            }
            Set<String> enumValSet = this.getSelEnumEntryValSet(dataEntity);
            Set<String> relEntryVals = this.checkRelSchemeVal((DynamicObject[])schemeDynArr, enumValSet);
            if (!relEntryVals.isEmpty()) {
                this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u679a\u4e3e\u503c\u88ab\u52a8\u6001\u6388\u6743\u65b9\u6848\u5f15\u7528\uff0c\u4e0d\u5141\u8bb8\u5220\u9664\uff1a%s\u3002", (String)"DynaRuleItemEdit_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), String.join((CharSequence)"\u3001", relEntryVals)));
                args.setCancel(true);
            }
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        String key;
        super.itemClick((ItemClickEvent)evt);
        switch (key = evt.getItemKey()) {
            case "bar_save": 
            case "bar_saveandnew": {
                List<String> errorMsgList;
                Object dataType = this.getModelVal(F_DATATYPE);
                if (V_DATATYPE_ENUM.equals(dataType) && !(errorMsgList = this.checkEnumEntry()).isEmpty()) {
                    this.getView().showTipNotification(String.join((CharSequence)"\uff1b", errorMsgList));
                    evt.setCancel(true);
                    return;
                }
                if (evt.isCancel()) break;
                this.clearUnMustData();
                break;
            }
        }
    }

    private Set<String> checkRelSchemeVal(DynamicObject[] schemeDynArr, Set<String> enumValSet) {
        EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity");
        int selectLength = entryGrid.getSelectRows().length;
        HashSet<String> relEntryVals = new HashSet<String>(selectLength);
        for (DynamicObject schemeDyn : schemeDynArr) {
            JSONObject jsonObject;
            JSONArray jsonArray;
            String condition = schemeDyn.getString("condition");
            if (StringUtils.isBlank((CharSequence)condition) || (jsonArray = (jsonObject = JSONObject.parseObject((String)condition)).getJSONArray("conditionList")) == null || jsonArray.isEmpty()) continue;
            for (Object item : jsonArray) {
                JSONObject paramRel = (JSONObject)item;
                String value = paramRel.getString(FE_ENUM_VALUE);
                if (!enumValSet.contains(value)) continue;
                relEntryVals.add(value);
            }
        }
        return relEntryVals;
    }

    private Set<String> getSelEnumEntryValSet(DynamicObject dataEntity) {
        EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity");
        int[] selectRows = entryGrid.getSelectRows();
        int selectLength = selectRows.length;
        HashSet<String> enumValSet = new HashSet<String>(selectLength);
        DynamicObjectCollection entrys = dataEntity.getDynamicObjectCollection("entryentity");
        for (int selectRow : selectRows) {
            enumValSet.add(((DynamicObject)entrys.get(selectRow)).getString(FE_ENUM_VALUE));
        }
        return enumValSet;
    }

    private void clearUnMustData() {
        String sourceType;
        boolean isRelatParam;
        String dataType = (String)this.getModelVal(F_DATATYPE);
        if (!V_DATATYPE_ENUM.equals(dataType)) {
            this.deleteEnumEntry();
        }
        if (!V_DATATYPE_BD.equals(dataType) && !V_DATATYPE_ORG.equals(dataType)) {
            this.setModelNullVal(F_ENTITYTYPE);
        }
        if (!(isRelatParam = ((Boolean)this.getModelVal(F_IS_RELATE_PARAM)).booleanValue())) {
            this.setModelNullVal(F_RELATE_RULE_ITEM);
            this.setModelNullVal(F_RELATE_PROP_KEY);
        }
        if (V_VALSOURCETYPE_ENTITY.equals(sourceType = (String)this.getModelVal(F_VAL_SOURCE_TYPE))) {
            this.setModelNullVal(F_MSERVICE_APP);
            this.setModelNullVal(F_MSERVICE_CLASS);
        } else {
            this.setModelNullVal(F_SOURCE_ENTITY_TYPE);
            this.setModelNullVal(F_SOURCE_PROP_KEY);
        }
    }

    private List<String> checkEnumEntry() {
        int firstErrRow = -1;
        String firstErrKey = "";
        ArrayList<String> errMsgs = new ArrayList<String>(3);
        DynamicObjectCollection entryEntitys = this.getView().getModel().getEntryEntity("entryentity");
        if (CollectionUtils.isEmpty((Collection)entryEntitys)) {
            errMsgs.add(ResManager.loadKDString((String)"\u5f53\u6570\u636e\u7c7b\u578b\u4e3a\u679a\u4e3e\u65f6\uff0c\u679a\u4e3e\u503c\u548c\u679a\u4e3e\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"DynaRuleItemEdit_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        } else {
            int entrySize = entryEntitys.size();
            HashSet<String> enumKeys = new HashSet<String>(entrySize);
            HashSet<String> enumVals = new HashSet<String>(entrySize);
            HashSet<String> repeatKeys = new HashSet<String>(entrySize);
            HashSet<String> repeatVals = new HashSet<String>(entrySize);
            int emptyRowNum = 0;
            for (int rowIndex = 0; rowIndex < entrySize; ++rowIndex) {
                DynamicObject enumDyo = (DynamicObject)entryEntitys.get(rowIndex);
                String enumKey = enumDyo.getString(FE_ENUM_VALUE);
                String enumVal = enumDyo.getString(FE_ENUM_DISPLAYVALUE);
                if (ObjectUtils.isEmpty((Object)enumKey) && ObjectUtils.isEmpty((Object)enumVal)) {
                    if (firstErrRow < 0) {
                        firstErrRow = rowIndex;
                        firstErrKey = FE_ENUM_VALUE;
                    }
                    ++emptyRowNum;
                    continue;
                }
                if (!enumKeys.add(enumKey)) {
                    repeatKeys.add(enumKey);
                    if (firstErrRow < 0) {
                        firstErrRow = rowIndex;
                        firstErrKey = FE_ENUM_VALUE;
                    }
                }
                if (enumVals.add(enumVal)) continue;
                repeatVals.add(enumVal);
                if (firstErrRow >= 0) continue;
                firstErrRow = rowIndex;
                firstErrKey = FE_ENUM_DISPLAYVALUE;
            }
            if (emptyRowNum > 0) {
                errMsgs.add(ResManager.loadKDString((String)"\u5f53\u6570\u636e\u7c7b\u578b\u4e3a\u679a\u4e3e\u65f6\uff0c\u679a\u4e3e\u503c\u548c\u679a\u4e3e\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"DynaRuleItemEdit_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            }
            if (CollectionUtils.isNotEmpty(repeatKeys)) {
                errMsgs.add(String.format(ResManager.loadKDString((String)"\u5b58\u5728\u76f8\u540c\u7684\u679a\u4e3e\u503c\uff0c\u8bf7\u4fee\u6539\u540e\u91cd\u8bd5\uff1a%s\u3002", (String)"DynaRuleItemEdit_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), String.join((CharSequence)"\u3001", repeatKeys)));
            }
            if (CollectionUtils.isNotEmpty(repeatVals)) {
                errMsgs.add(String.format(ResManager.loadKDString((String)"\u5b58\u5728\u76f8\u540c\u7684\u679a\u4e3e\u540d\u79f0\uff1a%s\u3002", (String)"DynaRuleItemEdit_7", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), String.join((CharSequence)"\u3001", repeatVals)));
            }
        }
        if (!errMsgs.isEmpty() && firstErrRow > -1) {
            AbstractGrid grid = (AbstractGrid)this.getView().getControl("entryentity");
            grid.focusCell(firstErrRow, firstErrKey);
        }
        return errMsgs;
    }

    private void deleteEnumEntry() {
        DynamicObjectCollection entryentitys = this.getModel().getEntryEntity("entryentity");
        int size = entryentitys.size();
        if (size > 0) {
            int[] delRows = new int[size];
            for (int index = 0; index < delRows.length; ++index) {
                delRows[index] = index;
            }
            this.getModel().deleteEntryRows("entryentity", delRows);
        }
    }

    private void fillSourceEntityPropName() {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        String valSourceType = dataEntity.getString(F_VAL_SOURCE_TYPE);
        if (!V_VALSOURCETYPE_ENTITY.equals(valSourceType)) {
            return;
        }
        DynamicObject sourceEntityTypeDyo = dataEntity.getDynamicObject(F_SOURCE_ENTITY_TYPE);
        String entityNumber = sourceEntityTypeDyo.getString("id");
        String sourcePropKey = dataEntity.getString(F_SOURCE_PROP_KEY);
        Map entityFieldMap = RolePermLogServiceHelper.getEntityFieldMap((String)entityNumber);
        this.getModel().setValue(F_SOURCE_PROP_NAME, entityFieldMap.get(sourcePropKey));
        this.getModel().setDataChanged(false);
    }

    private void fillRelateParamPropName() {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        boolean isRelateParam = dataEntity.getBoolean(F_IS_RELATE_PARAM);
        if (!isRelateParam) {
            return;
        }
        String relateParamEntity = dataEntity.getString("relatruleparam.entitytype.id");
        String relateParamPropKey = dataEntity.getString(F_RELATE_PROP_KEY);
        Map entityFieldMap = RolePermLogServiceHelper.getEntityFieldMap((String)relateParamEntity);
        this.getModel().setValue(F_RELATE_PROP_NAME, entityFieldMap.get(relateParamPropKey));
        this.getModel().setDataChanged(false);
    }

    private void showFieldSelF7(String entityNumber, String actionId) {
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"hrcs_choosefield_page", (boolean)false, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, actionId));
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setCustomParam("paramEntityName", (Object)entityNumber);
        fsp.setCustomParam("param_ifShowForDynaRule", (Object)"true");
        fsp.setHasRight(true);
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void disableEnumEntry() {
        DynamicObjectCollection entryDyoColl;
        String dataType = (String)this.getModelVal(F_DATATYPE);
        if (V_DATATYPE_ENUM.equals(dataType) && CollectionUtils.isNotEmpty((Collection)(entryDyoColl = this.getModel().getEntryEntity("entryentity")))) {
            for (int rowIndex = 0; rowIndex < entryDyoColl.size(); ++rowIndex) {
                this.getView().setEnable(Boolean.FALSE, rowIndex, new String[]{FE_ENUM_VALUE});
            }
        }
    }

    private void presetView() {
        if (this.getModel().getDataEntity().getBoolean("issyspreset")) {
            BillView billView = (BillView)this.getView();
            billView.setBillStatus(BillOperationStatus.VIEW);
            this.getView().setVisible(Boolean.FALSE, new String[]{F_ENUM_BAR});
        }
    }
}
