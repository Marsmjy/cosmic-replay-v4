/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.designer.query.QueryEntityTreeBuildParameter
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.filter.FilterCondition
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.cardentry.CardEntry
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.FilterGrid
 *  kd.bos.form.control.SubEntryGrid
 *  kd.bos.form.control.events.RowClickEvent
 *  kd.bos.form.control.events.RowClickEventListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.lang.Lang
 *  kd.bos.list.ListShowParameter
 *  kd.bos.mvc.bill.BillView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.workflow.engine.enumeration.ConditionalRuleType
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.servicehelper.HRBizCloudServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.activity.ActivitySchemeServiceHelper
 *  kd.hr.hrcs.common.constants.activity.ActivitySchemeConstant
 *  kd.hr.hrcs.common.util.EntityFieldTreeUtil
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hrcs.formplugin.web.activity;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.designer.query.QueryEntityTreeBuildParameter;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.filter.FilterCondition;
import kd.bos.entity.tree.TreeNode;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.cardentry.CardEntry;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.FilterGrid;
import kd.bos.form.control.SubEntryGrid;
import kd.bos.form.control.events.RowClickEvent;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.lang.Lang;
import kd.bos.list.ListShowParameter;
import kd.bos.mvc.bill.BillView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.workflow.engine.enumeration.ConditionalRuleType;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.servicehelper.HRBizCloudServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.activity.ActivitySchemeServiceHelper;
import kd.hr.hrcs.common.constants.activity.ActivitySchemeConstant;
import kd.hr.hrcs.common.util.EntityFieldTreeUtil;
import org.apache.commons.lang3.StringUtils;

@ExcludeFromJacocoGeneratedReport
public final class ActivitySchemeEdit
extends HRDataBaseEdit
implements RowClickEventListener,
BeforeF7SelectListener,
ActivitySchemeConstant {
    private static final String KEY_FROM = "fromActivityScheme";

    public void registerListener(EventObject event) {
        super.registerListener(event);
        this.addClickListeners(new String[]{"targetfieldnumber", "sourcefield"});
        ((CardEntry)this.getControl("actschemeentry")).addRowClickListener((RowClickEventListener)this);
        ((SubEntryGrid)this.getControl("paramconfig")).addRowClickListener((RowClickEventListener)this);
        ((SubEntryGrid)this.getControl("subentryentity")).addRowClickListener((RowClickEventListener)this);
        BasedataEdit permitem = (BasedataEdit)this.getControl("activityschemeref");
        permitem.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        this.addClickListeners(new String[]{"taskthemedisplay", "paramvalue", "newactivityentry"});
    }

    public void beforeBindData(EventObject event) {
        super.beforeBindData(event);
        List bizCloudNumberList = HRBizCloudServiceHelper.getBizCloudNumber();
        ((BasedataEdit)this.getControl("bizobj")).setQFilter(new QFilter("bizappid.bizcloud.number", "in", (Object)bizCloudNumberList));
        ((BasedataEdit)this.getControl("app")).setQFilter(new QFilter("bizcloud.number", "in", (Object)bizCloudNumberList));
        BillShowParameter formShowParameter = (BillShowParameter)this.getView().getFormShowParameter();
        Boolean readonly = (Boolean)formShowParameter.getCustomParam("readonly");
        if (null != readonly && readonly.booleanValue()) {
            this.initReadonly();
            this.setActivityConfig(Boolean.FALSE);
            this.getModel().setValue("viewstatus", (Object)"1");
        } else if (HRObjectUtils.isEmpty((Object)formShowParameter.getPkId())) {
            this.initVersion();
            this.setActivityConfig(Boolean.TRUE);
        } else {
            this.initView();
            this.setActivityConfig(Boolean.FALSE);
            this.getModel().setValue("viewstatus", (Object)"1");
        }
        this.fillActConfigData(0);
    }

    public void afterBindData(EventObject event) {
        String currentStatus;
        super.afterBindData(event);
        this.setViewStatus();
        Boolean fromActivityScheme = (Boolean)this.getView().getFormShowParameter().getCustomParam(KEY_FROM);
        if (null != fromActivityScheme && fromActivityScheme.booleanValue()) {
            String cacheFromActivityScheme = this.getPageCache().get(KEY_FROM);
            if (HRStringUtils.isEmpty((String)cacheFromActivityScheme)) {
                cacheFromActivityScheme = fromActivityScheme.toString();
            }
            if (Boolean.TRUE.toString().equals(cacheFromActivityScheme)) {
                this.getView().getFormShowParameter().setCustomParam(KEY_FROM, (Object)Boolean.FALSE);
                this.getPageCache().put(KEY_FROM, Boolean.FALSE.toString());
                this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u4fdd\u5b58\u6210\u529f\u3002", (String)"ActivitySchemeEdit_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            }
        }
        if ("view".equalsIgnoreCase(currentStatus = this.getView().getPageCache().get("currentStatus"))) {
            EntryGrid entryGrid = (EntryGrid)this.getControl("actschemeentry");
            entryGrid.selectRows(0);
            String fc = "var(--theme-color)";
            HashMap root = new HashMap(1);
            HashMap<String, String> map = new HashMap<String, String>(1);
            map.put("fc", fc);
            root.put("cardentryflexpanelap1", map);
            entryGrid.setCustomProperties("actschemeentry", 0, root);
            int index = Integer.parseInt(this.getPageCache().get("currentIndex"));
            DynamicObject entryRowEntity = this.getModel().getEntryRowEntity("actinfo", 0, index);
            this.setThemeDisplay(entryRowEntity.getString("tasktheme"), entryRowEntity);
            this.getView().updateView("actinfo", 0, index);
        }
        if (HRStringUtils.equals((String)"1", (String)((String)this.getView().getFormShowParameter().getCustomParam("changeFlag")))) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"bar_modify"});
        }
        this.showNewActivityEntryIcon();
        this.lockBySys();
    }

    private void showNewActivityEntryIcon() {
        if (HRStringUtils.equals((String)"1", (String)((String)this.getModel().getValue("viewstatus")))) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"cardentryfixrowap"});
        } else {
            this.getView().setVisible(Boolean.TRUE, new String[]{"cardentryfixrowap"});
        }
    }

    public void click(EventObject event) {
        super.click(event);
        String currentStatus = this.getView().getPageCache().get("currentStatus");
        if ("view".equalsIgnoreCase(currentStatus)) {
            return;
        }
        String key = ((Control)event.getSource()).getKey();
        String currentString = this.getPageCache().get("currentIndex");
        if ("newactivityentry".equalsIgnoreCase(key)) {
            this.showActivityF7();
        } else if ("taskthemedisplay".equalsIgnoreCase(key)) {
            if (!this.check()) {
                return;
            }
            int index = Integer.parseInt(currentString);
            DynamicObject value = (DynamicObject)this.getModel().getValue("bizobj");
            if (Objects.isNull(value)) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e1a\u52a1\u5bf9\u8c61\u3002", (String)"PermRelateEdit_03", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return;
            }
            String entityNumber = ((DynamicObject)this.getModel().getValue("bizobj")).getString("number");
            String taskThemeValue = this.getModel().getEntryRowEntity("actinfo", 0, index).getString("tasktheme");
            this.showTheme(taskThemeValue, entityNumber);
        } else if ("paramvalue".equalsIgnoreCase(key)) {
            if (!this.check()) {
                return;
            }
            String entityNumber = ((DynamicObject)this.getModel().getValue("bizobj")).getString("number");
            this.showExpressionForm(entityNumber);
        } else if ("targetfieldnumber".equalsIgnoreCase(key)) {
            DynamicObject modelVal = (DynamicObject)this.getModelVal("actbizobj");
            if (modelVal == null) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u6d3b\u52a8\u65e0\u4e1a\u52a1\u5bf9\u8c61\uff0c\u65e0\u6cd5\u914d\u7f6e\u76ee\u6807\u5b57\u6bb5\u3002", (String)"ActivitySchemeEdit_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return;
            }
            String number = modelVal.getString("number");
            this.getView().getPageCache().put("multiFlag", "0");
            List<String> targetFieldList = this.getSelectTargetField();
            this.showFieldForm(this.buildTreeNodesByEntityType(number, "", "", "all", targetFieldList, false), "targetfield", false, null);
        } else if ("sourcefield".equalsIgnoreCase(key)) {
            this.getView().getPageCache().put("multiFlag", "0");
            this.showSourceFieldForm();
        }
    }

    private void showSourceFieldForm() {
        String propertyName = this.getFieldTypeName();
        int selectRowIndex = this.getModel().getEntryCurrentRowIndex("actschemeentry");
        DynamicObject currentActivity = (DynamicObject)this.getModel().getValue("activity", selectRowIndex);
        long currActId = 0L;
        if (currentActivity != null) {
            currActId = currentActivity.getLong("id");
        }
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("actschemeentry");
        ArrayList bizobjList = Lists.newArrayListWithCapacity((int)16);
        for (DynamicObject dynamicObject : entryEntity) {
            DynamicObjectCollection dynamicObjectCollection = dynamicObject.getDynamicObjectCollection("actinfo");
            for (DynamicObject object : dynamicObjectCollection) {
                DynamicObject actDynamic;
                HashMap actBizObjMap = Maps.newHashMapWithExpectedSize((int)16);
                DynamicObject actBizDynamic = object.getDynamicObject("actbizobj");
                if (actBizDynamic == null || currActId == (actDynamic = dynamicObject.getDynamicObject("activity")).getLong("id")) continue;
                actBizObjMap.put("number", actBizDynamic.getString("number"));
                actBizObjMap.put("activityid", actDynamic.getLong("id"));
                bizobjList.add(actBizObjMap);
            }
        }
        DynamicObject bizobj = (DynamicObject)this.getModelVal("bizobj");
        if (bizobj != null) {
            HashMap bizobjMap = Maps.newHashMapWithExpectedSize((int)16);
            bizobjMap.put("number", bizobj.getString("number"));
            bizobjMap.put("activityid", null);
            bizobjList.add(0, bizobjMap);
        }
        TreeNode root = new TreeNode("", "1000", ResManager.loadKDString((String)"\u5168\u90e8", (String)"HREntityTreeListPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        root.setIsOpened(true);
        ArrayList childNodes = Lists.newArrayListWithCapacity((int)16);
        for (Map businessObj : bizobjList) {
            String number = (String)businessObj.get("number");
            Long activityid = (Long)businessObj.get("activityid");
            TreeNode entityNode = activityid == null ? this.buildTreeNodesByEntityType(number, "1000", "", propertyName, null, true) : this.buildTreeNodesByEntityType(number, "1000", activityid.toString(), propertyName, null, true);
            childNodes.add(entityNode);
        }
        root.addChildren((List)childNodes);
        int subSelectRowIndex = this.getModel().getEntryCurrentRowIndex("subentryentity");
        String targetField = (String)this.getModel().getValue("targetfieldnumber", subSelectRowIndex);
        HashMap customMap = Maps.newHashMapWithExpectedSize((int)2);
        if (!HRStringUtils.isEmpty((String)targetField)) {
            customMap.put("fieldType", propertyName);
        }
        this.showFieldForm(root, "soucefieldname", false, customMap);
    }

    private String getFieldTypeName() {
        int selectRowIndex = this.getModel().getEntryCurrentRowIndex("subentryentity");
        String targetField = (String)this.getModel().getValue("targetfieldnumber", selectRowIndex);
        String[] split = targetField.split("\\.");
        DynamicObject actBizObj = (DynamicObject)this.getModelVal("actbizobj");
        if (actBizObj == null) {
            return null;
        }
        String entityNumber = actBizObj.getString("number");
        return EntityFieldTreeUtil.getType((String)entityNumber, (String[])split);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String operateKey = ((FormOperate)args.getSource()).getOperateKey();
        if ("save".equalsIgnoreCase(operateKey)) {
            if (!args.isCancel()) {
                if (!this.check()) {
                    args.setCancel(true);
                    return;
                }
                if (!this.checkPluginFieldInput()) {
                    args.setCancel(true);
                    return;
                }
                if (!this.checkGroup()) {
                    args.setCancel(true);
                    return;
                }
                if (!this.checkFiledMapping()) {
                    args.setCancel(true);
                }
            }
        } else if ("addfieldmapping".equals(operateKey)) {
            DynamicObject modelVal = (DynamicObject)this.getModelVal("actbizobj");
            if (modelVal == null) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u6d3b\u52a8\u65e0\u4e1a\u52a1\u5bf9\u8c61\uff0c\u65e0\u6cd5\u914d\u7f6e\u76ee\u6807\u5b57\u6bb5\u3002", (String)"ActivitySchemeEdit_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return;
            }
            String number = modelVal.getString("number");
            this.getView().getPageCache().put("multiFlag", "1");
            List<String> targetFieldList = this.getSelectTargetField();
            this.showFieldForm(this.buildTreeNodesByEntityType(number, "", "", "all", targetFieldList, false), "targetfield", true, null);
        } else if ("deletefieldmapping".equals(operateKey)) {
            EntryGrid grid = (EntryGrid)this.getView().getControl("subentryentity");
            int[] selectRows = grid.getSelectRows();
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("subentryentity");
            for (int selectRow : selectRows) {
                DynamicObject dynamicObject = (DynamicObject)entryEntity.get(selectRow);
                if (!dynamicObject.getBoolean("sourceissyspreset")) continue;
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u6b64\u6570\u636e\u4e3a\u7cfb\u7edf\u9884\u7f6e\u6570\u636e\uff0c\u4e0d\u53ef\u5220\u9664\u3002", (String)"ActivitySchemeEdit_10", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
        }
    }

    private List<String> getSelectTargetField() {
        DynamicObjectCollection subentryentity = this.getModel().getEntryEntity("subentryentity");
        return subentryentity.stream().map(entryEntity -> entryEntity.getString("targetfieldnumber")).collect(Collectors.toList());
    }

    private boolean checkPluginFieldInput() {
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("actschemeentry");
        for (DynamicObject dynamicObject : entryEntity) {
            DynamicObjectCollection pluginEntryEntity = dynamicObject.getDynamicObjectCollection("pluginentry");
            for (DynamicObject pluginEntry : pluginEntryEntity) {
                if (pluginEntry == null) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u201c\u63d2\u4ef6\u7c7b\u578b\u201d\u3001\u201c\u529f\u80fd\u63cf\u8ff0\u201d\u3001\u201c\u6240\u5c5e\u5e94\u7528\u201d\u3001\u201c\u6240\u5c5e\u4e1a\u52a1\u4e91\u201d\u3001\u201c\u670d\u52a1\u7c7b\u201d\u3001\u201c\u670d\u52a1\u65b9\u6cd5\u201d\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"ActivitySchemeEdit_7", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    return false;
                }
                if (pluginEntry.get("plugintype") != null && !HRStringUtils.isEmpty((String)pluginEntry.getString("plugindesc")) && !HRStringUtils.isEmpty((String)pluginEntry.getString("bizapp")) && !HRStringUtils.isEmpty((String)pluginEntry.getString("service")) && !HRStringUtils.isEmpty((String)pluginEntry.getString("method"))) continue;
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u201c\u63d2\u4ef6\u7c7b\u578b\u201d\u3001\u201c\u529f\u80fd\u63cf\u8ff0\u201d\u3001\u201c\u6240\u5c5e\u5e94\u7528\u201d\u3001\u201c\u6240\u5c5e\u4e1a\u52a1\u4e91\u201d\u3001\u201c\u670d\u52a1\u7c7b\u201d\u3001\u201c\u670d\u52a1\u65b9\u6cd5\u201d\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"ActivitySchemeEdit_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return false;
            }
            DynamicObjectCollection fieldMappingEntryEntity = dynamicObject.getDynamicObjectCollection("subentryentity");
            for (DynamicObject fieldMapping : fieldMappingEntryEntity) {
                if (fieldMapping == null) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u201c\u76ee\u6807\u5b57\u6bb5\u201d\u3001\u201c\u76ee\u6807\u5b57\u6bb5\u6807\u8bc6\u201d\u3001\u201c\u6e90\u5b57\u6bb5\u201d\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"ActivitySchemeEdit_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    return false;
                }
                if (!HRStringUtils.isEmpty((String)fieldMapping.getString("targetfieldnumber")) && !HRStringUtils.isEmpty((String)fieldMapping.getString("targetfield")) && !HRStringUtils.isEmpty((String)fieldMapping.getString("soucefieldname"))) continue;
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u201c\u76ee\u6807\u5b57\u6bb5\u201d\u3001\u201c\u76ee\u6807\u5b57\u6bb5\u6807\u8bc6\u201d\u3001\u201c\u6e90\u5b57\u6bb5\u201d\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"ActivitySchemeEdit_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return false;
            }
        }
        return true;
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        String key;
        super.afterDoOperation(args);
        if (args.getOperationResult() != null && !args.getOperationResult().isSuccess()) {
            return;
        }
        switch (key = args.getOperateKey()) {
            case "modify": {
                this.afterModify();
                break;
            }
            case "save": {
                if (!args.getOperationResult().isSuccess()) break;
                Long id = (Long)args.getOperationResult().getSuccessPkIds().get(0);
                this.showNewActivityScheme(id);
                HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_activityclientconf");
                DynamicObject bizObj = (DynamicObject)this.getModel().getValue("bizobj");
                DynamicObject app = (DynamicObject)this.getModel().getValue("app");
                boolean isExists = serviceHelper.isExists(new QFilter("bizobj", "=", bizObj.getPkValue()));
                if (isExists) break;
                DynamicObject clientItem = serviceHelper.generateEmptyDynamicObject();
                clientItem.set("bizobj", (Object)bizObj);
                clientItem.set("cloud", app.get("bizcloud"));
                clientItem.set("transtatus", (Object)"0");
                serviceHelper.saveOne(clientItem);
                break;
            }
            case "deleteentry": {
                DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("actschemeentry");
                List selectedActivity = entryEntity.stream().map(it -> it.getLong("activity.id")).collect(Collectors.toList());
                if (0 != selectedActivity.size()) break;
                this.setActivityConfig(Boolean.TRUE);
                break;
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId;
        String currentStatus = this.getView().getPageCache().get("currentStatus");
        if ("view".equalsIgnoreCase(currentStatus)) {
            return;
        }
        switch (actionId = closedCallBackEvent.getActionId()) {
            case "activity": {
                ListSelectedRowCollection returnData = (ListSelectedRowCollection)closedCallBackEvent.getReturnData();
                if (null == returnData) break;
                Set<Long> activityIds = returnData.stream().map(ListSelectedRow::getPrimaryKeyValue).map(it -> (Long)it).collect(Collectors.toSet());
                this.setActivityConfig(Boolean.FALSE);
                this.fillActivityId(activityIds);
                break;
            }
            case "theme": {
                int index = Integer.parseInt(this.getPageCache().get("currentIndex"));
                Map data = (Map)closedCallBackEvent.getReturnData();
                if (null == data) break;
                String customSubject = data.getOrDefault("customSubject", "");
                DynamicObject entryRowEntity = this.getModel().getEntryRowEntity("actinfo", 0, index);
                entryRowEntity.set("tasktheme", (Object)customSubject);
                this.setThemeDisplay(customSubject, entryRowEntity);
                this.getView().updateView("actinfo", 0, index);
                break;
            }
            case "paramvalue": {
                int index = Integer.parseInt(this.getPageCache().get("currentIndex"));
                int paramConfigIndex = Integer.parseInt(this.getPageCache().get("paramConfigCurrentIndex"));
                Map data = (Map)closedCallBackEvent.getReturnData();
                if (null == data || !HRStringUtils.isNotEmpty((String)((String)data.get("expression")))) break;
                String value = "{" + (String)data.get("expression") + "}";
                this.getModel().getEntryRowEntity("paramconfig", paramConfigIndex, index).set("paramvalue", (Object)value);
                this.getView().updateView("paramconfig", paramConfigIndex, index);
                break;
            }
            case "targetfield": {
                this.updateSubEntryEntity(closedCallBackEvent, "targetfieldnumber", "targetfield");
                break;
            }
            case "soucefieldname": {
                this.updateSubEntryEntity(closedCallBackEvent, "sourcefield", "soucefieldname");
                break;
            }
        }
    }

    private void setThemeDisplay(String customSubject, DynamicObject entryRowEntity) {
        try {
            JSONObject jsonObject = JSONObject.parseObject((String)customSubject);
            String local = Lang.get().toString();
            entryRowEntity.set("taskthemedisplay", (Object)jsonObject.getString(local));
        }
        catch (Exception exception) {
            entryRowEntity.set("taskthemedisplay", (Object)customSubject);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        String callBackId;
        super.confirmCallBack(event);
        switch (callBackId = event.getCallBackId()) {
            case "save": {
                if (event.getResult() != MessageBoxResult.Yes) break;
                this.getPageCache().put("saveFlag", "1");
                this.getView().invokeOperation("save");
                break;
            }
            case "deleteentry": {
                DynamicObjectCollection entryEntity;
                List selectedActivity;
                if (event.getResult() != MessageBoxResult.Yes || 1 != (selectedActivity = (entryEntity = this.getModel().getEntryEntity("actschemeentry")).stream().map(it -> it.getLong("activity.id")).collect(Collectors.toList())).size()) break;
                this.setActivityConfig(Boolean.TRUE);
                break;
            }
        }
    }

    public void entryRowClick(RowClickEvent evt) {
        this.doRowClick(evt);
    }

    public void entryRowDoubleClick(RowClickEvent evt) {
        this.doRowClick(evt);
    }

    public void propertyChanged(PropertyChangedArgs propertyChangedArgs) {
        ChangeData changeData;
        String newValue2;
        String fieldKey = propertyChangedArgs.getProperty().getName();
        if (HRStringUtils.equals((String)"bizobj", (String)fieldKey)) {
            DynamicObject bizObj = (DynamicObject)this.getModel().getValue("bizobj");
            if (null != bizObj) {
                DynamicObject bizApp = bizObj.getDynamicObject("bizappid");
                this.getModel().setValue("app", (Object)bizApp);
            } else {
                this.getModel().setValue("app", null);
            }
        } else if (HRStringUtils.equals((String)"activityschemeref", (String)fieldKey)) {
            List activitySchemeIds = ((DynamicObjectCollection)this.getModel().getValue("activityschemeref")).stream().map(it -> it.getDynamicObject(1)).map(it -> it.getLong("id")).collect(Collectors.toList());
            List entry = ((Stream)Arrays.stream(ActivitySchemeServiceHelper.getEntryByActivitySchemeIds(activitySchemeIds)).map(it -> it.getDynamicObjectCollection("actschemeentry")).sequential()).flatMap(Collection::stream).collect(Collectors.toList());
            Set activities = ((Stream)entry.stream().sequential()).map(it -> it.getDynamicObject("activity")).collect(Collectors.toCollection(LinkedHashSet::new));
            Map<Long, DynamicObject> actObjMap = entry.stream().collect(Collectors.toMap(it -> it.getDynamicObject("activity").getLong("id"), it -> (DynamicObject)it.getDynamicObjectCollection("actinfo").get(0), (oldValue, newValue) -> oldValue));
            Map<Long, DynamicObjectCollection> paramConfigMap = entry.stream().collect(Collectors.toMap(it -> it.getDynamicObject("activity").getLong("id"), it -> it.getDynamicObjectCollection("paramconfig"), (oldValue, newValue) -> oldValue));
            Map<Long, DynamicObjectCollection> pluginConfigMap = entry.stream().collect(Collectors.toMap(it -> it.getDynamicObject("activity").getLong("id"), it -> it.getDynamicObjectCollection("pluginentry"), (oldValue, newValue) -> oldValue));
            Map<Long, DynamicObjectCollection> fieldMappingConfigMap = entry.stream().collect(Collectors.toMap(it -> it.getDynamicObject("activity").getLong("id"), it -> it.getDynamicObjectCollection("subentryentity"), (oldValue, newValue) -> oldValue));
            this.setActivityConfig(Boolean.FALSE);
            this.fillActivity(activities, actObjMap, paramConfigMap, pluginConfigMap, fieldMappingConfigMap);
            this.removeEmptyEntry();
        } else if ("sourcefield".equals(fieldKey)) {
            ChangeData changeData2 = propertyChangedArgs.getChangeSet()[0];
            String newValue3 = (String)changeData2.getNewValue();
            if (HRStringUtils.isEmpty((String)newValue3)) {
                changeData2.getDataEntity().set("soucefieldname", null);
                this.getView().updateView("subentryentity", changeData2.getRowIndex());
            }
        } else if ("targetfieldnumber".equals(fieldKey)) {
            ChangeData changeData3 = propertyChangedArgs.getChangeSet()[0];
            String newValue4 = (String)changeData3.getNewValue();
            changeData3.getDataEntity().set("sourcefield", null);
            changeData3.getDataEntity().set("soucefieldname", null);
            if (HRStringUtils.isEmpty((String)newValue4)) {
                changeData3.getDataEntity().set("targetfield", null);
            }
            this.getView().updateView("subentryentity", changeData3.getRowIndex());
        } else if ("taskthemedisplay".equals(fieldKey) && HRStringUtils.isEmpty((String)(newValue2 = (String)(changeData = propertyChangedArgs.getChangeSet()[0]).getNewValue()))) {
            String currentIndex = this.getPageCache().get("currentIndex");
            this.getModel().getEntryRowEntity("actinfo", 0, Integer.parseInt(currentIndex)).set("tasktheme", null);
        }
    }

    private boolean check() {
        List selectedActivity = this.getModel().getEntryEntity("actschemeentry").stream().map(it -> it.getLong("activity.id")).filter(it -> 0L != it).collect(Collectors.toList());
        String currentString = this.getPageCache().get("currentIndex");
        if (HRStringUtils.isEmpty((String)currentString) || selectedActivity.isEmpty()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u65b0\u589e\u6d3b\u52a8\u3002", (String)"ActivitySchemeEdit_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        return true;
    }

    private boolean checkFiledMapping() {
        String saveflag = this.getPageCache().get("saveFlag");
        if ("1".equals(saveflag)) {
            this.getPageCache().remove("saveFlag");
            return true;
        }
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("actschemeentry");
        ArrayList actName = Lists.newArrayListWithCapacity((int)16);
        for (DynamicObject activityEntry : entryEntity) {
            DynamicObjectCollection subentryentity = activityEntry.getDynamicObjectCollection("subentryentity");
            if (!CollectionUtils.isEmpty((Collection)subentryentity)) continue;
            actName.add(activityEntry.getString("activity.name"));
        }
        if (!CollectionUtils.isEmpty((Collection)actName)) {
            ConfirmCallBackListener confirmListener = new ConfirmCallBackListener("save", (IFormPlugin)this);
            String tips = StringUtils.join((Iterable)actName, (String)"\uff0c");
            String tipValue = String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u6d3b\u52a8\u7684\u5b57\u6bb5\u6620\u5c04\u5173\u7cfb\u8fd8\u672a\u914d\u7f6e\u5b8c\u6210\uff0c\u786e\u5b9a\u7ee7\u7eed\u5417\uff1f", (String)"ActivitySchemeEdit_5", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), tips);
            this.getView().showConfirm(tipValue, MessageBoxOptions.OKCancel, confirmListener);
            return false;
        }
        return true;
    }

    private void showActivityF7() {
        StyleCss css = new StyleCss();
        css.setWidth("960px");
        css.setHeight("580px");
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.getOpenStyle().setShowType(ShowType.Modal);
        listShowParameter.setFormId("bos_listf7");
        listShowParameter.setBillFormId("hrcs_activity");
        listShowParameter.setLookUp(true);
        listShowParameter.setShowTitle(false);
        listShowParameter.setMultiSelect(true);
        listShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "activity"));
        listShowParameter.setShowUsed(true);
        listShowParameter.getListFilterParameter().getQFilters().add(new QFilter("status", "=", (Object)"C"));
        listShowParameter.getOpenStyle().setInlineStyleCss(css);
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    private void fillActivityId(Set<Long> activityIds) {
        if (null != activityIds) {
            List exist = this.getModel().getEntryEntity("actschemeentry").stream().map(it -> it.getLong("activity.id")).filter(it -> 0L != it).collect(Collectors.toList());
            List preInsert = activityIds.stream().filter(it -> !exist.contains(it)).collect(Collectors.toList());
            if (preInsert.size() > 0) {
                HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_activity");
                DynamicObject[] items = helper.query("bizobj", new QFilter[]{new QFilter("id", "in", preInsert)});
                Map bizObjMap = Arrays.stream(items).collect(HashMap::new, (hashMap, item) -> hashMap.put(item.getLong("id"), item.getDynamicObject("bizobj")), HashMap::putAll);
                this.getModel().beginInit();
                int[] rowIndex = this.getModel().batchCreateNewEntryRow("actschemeentry", preInsert.size());
                for (int index = 0; index < preInsert.size(); ++index) {
                    this.getModel().setValue("activity", preInsert.get(index), rowIndex[index]);
                    this.getModel().getEntryRowEntity("actinfo", 0, rowIndex[index]).set("actbizobj", bizObjMap.get(preInsert.get(index)));
                }
                this.getModel().endInit();
                this.getView().updateView("actschemeentry");
                ((EntryGrid)this.getControl("actschemeentry")).selectRows(rowIndex[rowIndex.length - 1]);
                this.getPageCache().put("currentIndex", String.valueOf(rowIndex[rowIndex.length - 1]));
            }
        }
    }

    private void fillActivity(Set<DynamicObject> activities, Map<Long, DynamicObject> actObjMap, Map<Long, DynamicObjectCollection> paramConfigMap, Map<Long, DynamicObjectCollection> pluginConfigMap, Map<Long, DynamicObjectCollection> fieldMappingConfigMap) {
        if (null != activities) {
            ArrayList<DynamicObject> activityList = new ArrayList<DynamicObject>(activities);
            if (activities.size() > 0) {
                this.getModel().deleteEntryData("actschemeentry");
                int[] rowIndex = this.getModel().batchCreateNewEntryRow("actschemeentry", activities.size());
                this.getModel().updateCache();
                for (int index = 0; index < activities.size(); ++index) {
                    DynamicObject item;
                    int i;
                    DynamicObject activity = (DynamicObject)activityList.get(index);
                    Long activityId = activity.getLong("id");
                    this.getModel().setValue("activity", (Object)activity, rowIndex[index]);
                    DynamicObject actInfoDynamicObject = this.getModel().getEntryRowEntity("actinfo", 0, rowIndex[index]);
                    actInfoDynamicObject.set("actbizobj", (Object)actObjMap.get(activityId).getDynamicObject("actbizobj"));
                    actInfoDynamicObject.set("bindinglayoutid", (Object)actObjMap.get(activityId).getString("bindinglayoutid"));
                    actInfoDynamicObject.set("taskcreatetype", (Object)actObjMap.get(activityId).getString("taskcreatetype"));
                    String theme = actObjMap.get(activityId).getString("tasktheme");
                    actInfoDynamicObject.set("tasktheme", (Object)theme);
                    this.setThemeDisplay(theme, actInfoDynamicObject);
                    actInfoDynamicObject.set("taskassignmenttype", (Object)actObjMap.get(activityId).getString("taskassignmenttype"));
                    actInfoDynamicObject.set("sla", (Object)actObjMap.get(activityId).getInt("sla"));
                    actInfoDynamicObject.set("flowparam", (Object)actObjMap.get(activityId).getString("flowparam"));
                    DynamicObjectCollection items = paramConfigMap.get(activityId);
                    DynamicObjectCollection pluginColl = pluginConfigMap.get(activityId);
                    DynamicObjectCollection fieldColl = fieldMappingConfigMap.get(activityId);
                    ((EntryGrid)this.getControl("actschemeentry")).selectRows(rowIndex[index]);
                    this.getModel().beginInit();
                    if (items.size() > 0) {
                        int[] paramConfigIndex = this.getModel().batchCreateNewEntryRow("paramconfig", items.size());
                        for (i = 0; i < items.size(); ++i) {
                            item = (DynamicObject)items.get(i);
                            DynamicObject paramConfigDynamicObject = this.getModel().getEntryRowEntity("paramconfig", paramConfigIndex[i]);
                            paramConfigDynamicObject.set("paramname", (Object)item.getString("paramname"));
                            paramConfigDynamicObject.set("paramnumber", (Object)item.getString("paramnumber"));
                            paramConfigDynamicObject.set("paramvalue", (Object)item.getString("paramvalue"));
                        }
                        this.getView().updateView("paramconfig");
                    }
                    if (pluginColl.size() > 0) {
                        int[] pluginConfigIndex = this.getModel().batchCreateNewEntryRow("pluginentry", pluginColl.size());
                        for (i = 0; i < pluginColl.size(); ++i) {
                            item = (DynamicObject)pluginColl.get(i);
                            DynamicObject pluginConfigDynamicObject = this.getModel().getEntryRowEntity("pluginentry", pluginConfigIndex[i]);
                            pluginConfigDynamicObject.set("plugintype", (Object)item.getString("plugintype"));
                            pluginConfigDynamicObject.set("plugindesc", (Object)item.getString("plugindesc"));
                            pluginConfigDynamicObject.set("bizapp", item.get("bizapp"));
                            pluginConfigDynamicObject.set("service", (Object)item.getString("service"));
                            pluginConfigDynamicObject.set("method", (Object)item.getString("method"));
                        }
                        this.getView().updateView("pluginentry");
                    }
                    if (fieldColl.size() > 0) {
                        int[] fieldMappingConfigIndex = this.getModel().batchCreateNewEntryRow("subentryentity", fieldColl.size());
                        for (i = 0; i < fieldColl.size(); ++i) {
                            item = (DynamicObject)fieldColl.get(i);
                            DynamicObject fieldConfigDynamicObject = this.getModel().getEntryRowEntity("subentryentity", fieldMappingConfigIndex[i]);
                            fieldConfigDynamicObject.set("targetfield", (Object)item.getString("targetfield"));
                            fieldConfigDynamicObject.set("targetfieldnumber", (Object)item.getString("targetfieldnumber"));
                            fieldConfigDynamicObject.set("soucefieldname", (Object)item.getString("soucefieldname"));
                            fieldConfigDynamicObject.set("sourcefield", (Object)item.getString("sourcefield"));
                            fieldConfigDynamicObject.set("fieldactivityid", item.get("fieldactivityid"));
                        }
                        this.getView().updateView("subentryentity");
                    }
                    this.getModel().endInit();
                }
                this.getView().updateView("actinfo", 0, rowIndex[rowIndex.length - 1]);
                this.getPageCache().put("currentIndex", String.valueOf(rowIndex[rowIndex.length - 1]));
            }
        }
    }

    private void removeEmptyEntry() {
        Optional.ofNullable(this.getModel().getEntryEntity("actschemeentry").get(0)).filter(it -> 0L == it.getLong("activity.id")).ifPresent(it -> {
            this.getModel().deleteEntryRow("actschemeentry", 0);
            ((EntryGrid)this.getControl("actschemeentry")).selectRows(0);
        });
    }

    private void initView() {
        this.getView().setEnable(Boolean.FALSE, new String[]{"newactivityentry", "deleteactivity", "newparam", "deleteparam"});
        this.getView().setStatus(OperationStatus.VIEW);
        ((BillShowParameter)this.getView().getFormShowParameter()).setBillStatus(BillOperationStatus.VIEW);
        this.getPageCache().put("currentStatus", "view");
    }

    private void initEdit() {
        this.getView().setEnable(Boolean.TRUE, new String[]{"newactivityentry", "deleteactivity", "newparam", "deleteparam"});
        this.getView().setStatus(OperationStatus.EDIT);
        this.getPageCache().put("currentStatus", "edit");
    }

    private void initReadonly() {
        this.getView().setEnable(Boolean.FALSE, new String[]{"newactivityentry", "deleteactivity", "newparam", "deleteparam"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"bar_save", "bar_modify"});
        this.getView().setStatus(OperationStatus.VIEW);
        this.getPageCache().put("currentStatus", "view");
    }

    private void setActivityConfig(Boolean hide) {
        this.getView().setVisible(Boolean.valueOf(hide == false), new String[]{"actinfopanel", "paramconfigpanel", "tabap"});
        this.getView().setVisible(hide, new String[]{"nonedata"});
    }

    private void initVersion() {
        String versionDate = HRDateTimeUtils.format((Date)new Date(), (String)"yyyyMMdd");
        int versionSeq = 1;
        this.getModel().setValue("versiondate", (Object)versionDate);
        this.getModel().setValue("versionseq", (Object)versionSeq);
        this.getModel().setValue("version", (Object)(versionDate + "-" + StringUtils.leftPad((String)String.valueOf(versionSeq), (int)2, (String)"0")));
    }

    private void setNextVersion() {
        int versionDate = (Integer)this.getModel().getValue("versionDate");
        int versionSeq = (Integer)this.getModel().getValue("versionSeq") + 1;
        String now = HRDateTimeUtils.format((Date)new Date(), (String)"yyyyMMdd");
        if (!now.equalsIgnoreCase(String.valueOf(versionDate))) {
            versionDate = Integer.parseInt(now);
            versionSeq = 1;
        }
        this.getModel().setValue("versiondate", (Object)versionDate);
        this.getModel().setValue("versionseq", (Object)versionSeq);
        this.getModel().setValue("version", (Object)(versionDate + "-" + StringUtils.leftPad((String)String.valueOf(versionSeq), (int)2, (String)"0")));
    }

    private void showTheme(String value, String entityNumber) {
        FormShowParameter parameter = new FormShowParameter();
        parameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "theme"));
        parameter.setFormId("hrcs_actschemetheme");
        parameter.getOpenStyle().setShowType(ShowType.Modal);
        parameter.setHasRight(true);
        parameter.getCustomParams().put("entityNumber", entityNumber);
        parameter.getCustomParams().put("FROM", "MSG");
        parameter.getCustomParams().put("value", value);
        parameter.getCustomParams().put("isIgnoreLicense", Boolean.TRUE);
        this.getView().showForm(parameter);
    }

    private void showExpressionForm(String entityNumber) {
        FormShowParameter parameter = new FormShowParameter();
        parameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "paramvalue"));
        parameter.setFormId("hrcs_actschemethemeexp");
        parameter.getOpenStyle().setShowType(ShowType.Modal);
        parameter.setHasRight(true);
        parameter.getCustomParams().put("isIgnoreLicense", Boolean.TRUE);
        parameter.getCustomParams().put("entityNumber", entityNumber);
        parameter.getCustomParams().put("ruleType", ConditionalRuleType.sequenceFlow);
        parameter.getCustomParams().put("fromSubjectModelAndApp", "fromSubjectModelAndApp");
        this.getView().showForm(parameter);
    }

    private void showNewActivityScheme(Long id) {
        BillShowParameter parameter = new BillShowParameter();
        parameter.setFormId("hrcs_activityscheme");
        parameter.getOpenStyle().setShowType(ShowType.InCurrentForm);
        parameter.setPkId((Object)id);
        parameter.setBillStatus(BillOperationStatus.VIEW);
        parameter.setStatus(OperationStatus.VIEW);
        parameter.setCloseCallBack(this.getView().getFormShowParameter().getCloseCallBack());
        parameter.setParentPageId(this.getView().getFormShowParameter().getParentPageId());
        parameter.setCustomParam(KEY_FROM, (Object)Boolean.TRUE);
        this.getView().showForm((FormShowParameter)parameter);
    }

    private void afterModify() {
        this.initEdit();
        this.setNextVersion();
        this.getModel().setValue("viewstatus", (Object)"0");
        this.showNewActivityEntryIcon();
    }

    private void doRowClick(RowClickEvent evt) {
        String key = ((EntryGrid)evt.getSource()).getEntryKey();
        int currentIndex = evt.getRow();
        if ("actschemeentry".equalsIgnoreCase(key)) {
            this.fillActConfigData(currentIndex);
            DynamicObject entryRowEntity = this.getModel().getEntryRowEntity("actinfo", 0, currentIndex);
            this.setThemeDisplay(entryRowEntity.getString("tasktheme"), entryRowEntity);
            this.getView().updateView("actinfo", 0, currentIndex);
        } else if ("paramconfig".equalsIgnoreCase(key)) {
            this.getPageCache().put("paramConfigCurrentIndex", String.valueOf(currentIndex));
        }
    }

    private void fillBindingLayout(int currentIndex) {
        int entryCount = this.getModel().getEntryRowCount("actinfo");
        if (entryCount > 0) {
            Optional.ofNullable(this.getModel().getEntryRowEntity("actinfo", 0, currentIndex)).map(it -> it.getDynamicObject("actbizobj")).ifPresent(bizObj -> {
                String entityId = bizObj.getString("dentityid");
                ((ComboEdit)this.getControl("bindinglayoutid")).setComboItems(this.getBindingLayoutComboItems(entityId));
            });
        }
    }

    private List<ComboItem> getBindingLayoutComboItems(String entityId) {
        if (HRStringUtils.isEmpty((String)entityId)) {
            return Collections.emptyList();
        }
        DynamicObject[] items = ActivitySchemeServiceHelper.getLayoutByEntityId((String)entityId);
        return Arrays.stream(items).map(it -> new ComboItem(new LocaleString(it.getString("name")), it.getString("id"))).collect(Collectors.toList());
    }

    private void setViewStatus() {
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        BillView view = (BillView)this.getView();
        if (OperationStatus.VIEW.equals((Object)status)) {
            view.setBillStatus(BillOperationStatus.VIEW);
        } else if (OperationStatus.EDIT.equals((Object)status)) {
            view.setBillStatus(BillOperationStatus.EDIT);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String property = evt.getProperty().getName();
        if (HRStringUtils.equals((String)"activityschemeref", (String)property)) {
            FormShowParameter formShowParameter = evt.getFormShowParameter();
            formShowParameter.setCustomParam("changeFlag", (Object)"1");
        }
    }

    private void showFieldForm(TreeNode entityFieldTreeNode, String actionId, boolean multFlag, Map<String, Object> customMap) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_actfieldtree");
        String nodesJson = SerializationUtils.toJsonString((Object)entityFieldTreeNode);
        showParameter.getCustomParams().put("treenodes", nodesJson);
        showParameter.getCustomParams().put("rowindex", 0);
        showParameter.getCustomParams().put("multiFlag", multFlag);
        if (customMap != null) {
            showParameter.getCustomParams().putAll(customMap);
        }
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, actionId));
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        this.getView().showForm(showParameter);
    }

    private TreeNode buildTreeNodesByEntityType(String entityNumber, String parentId, String activityid, String propertyType, List<String> targetFieldList, boolean isLoadBaseDataRef) {
        MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        QueryEntityTreeBuildParameter parameter = new QueryEntityTreeBuildParameter(mainEntityType);
        parameter.setIncludePKField(false);
        TreeNode childNode = EntityFieldTreeUtil.buildFieldTreeNodes((QueryEntityTreeBuildParameter)parameter, (String)"", (String)activityid, (String)propertyType, targetFieldList, (boolean)isLoadBaseDataRef);
        childNode.setParentid(parentId);
        String oldId = childNode.getId();
        return childNode;
    }

    private void updateSubEntryEntity(ClosedCallBackEvent closedCallBackEvent, String field, String fieldName) {
        String multiFlag = this.getView().getPageCache().get("multiFlag");
        if (HRStringUtils.equals((String)"1", (String)multiFlag)) {
            Object returnDataObject = closedCallBackEvent.getReturnData();
            if (!this.instanceofList(returnDataObject)) {
                return;
            }
            List returnData = (List)closedCallBackEvent.getReturnData();
            if (!CollectionUtils.isEmpty((Collection)returnData)) {
                DynamicObjectCollection entryentity = this.getModel().getEntryEntity("subentryentity");
                int size = entryentity.size();
                this.getModel().beginInit();
                for (int i = 0; i < returnData.size(); ++i) {
                    String currNodeId;
                    String currNode = currNodeId = (String)((Map)returnData.get(i)).get("id");
                    String activityId = "";
                    if (currNodeId.contains("$")) {
                        currNode = currNodeId.substring(0, currNodeId.indexOf("$"));
                        activityId = currNodeId.substring(currNodeId.indexOf("$") + 1);
                        if ("sourcefield".equals(field)) {
                            this.getModel().setValue("fieldactivityid", (Object)activityId, size + i);
                        }
                    }
                    DynamicObject fieldMapping = new DynamicObject(entryentity.getDynamicObjectType());
                    String displayName = EntityFieldTreeUtil.getDisplayNameContainsEntry((String)currNode, (Boolean)Boolean.FALSE);
                    OrmLocaleValue localeValue = (OrmLocaleValue)fieldMapping.get(fieldName);
                    localeValue.setLocaleValue(displayName);
                    this.getModel().insertEntryRow("subentryentity", size + i);
                    this.getModel().setValue(fieldName, (Object)new LocaleString(displayName), size + i);
                    this.getModel().setValue(field, (Object)currNode.substring(currNode.indexOf(".") + 1), size + i);
                }
                this.getModel().endInit();
            }
        } else {
            int selectRowIndex = this.getModel().getEntryCurrentRowIndex("subentryentity");
            Object returnDataObject = closedCallBackEvent.getReturnData();
            if (!this.instanceofMap(returnDataObject)) {
                return;
            }
            HashMap returnData = (HashMap)closedCallBackEvent.getReturnData();
            if (!CollectionUtils.isEmpty((Map)returnData)) {
                String currNodeId;
                String currNode = currNodeId = (String)returnData.get("currNodeId");
                String activityId = "";
                if (currNodeId.contains("$")) {
                    int index;
                    currNode = currNodeId.substring(0, currNodeId.indexOf("$"));
                    if (HRStringUtils.equals((String)"sourcefield", (String)field) && (index = currNode.indexOf(".")) == -1) {
                        currNode = currNode + ".id";
                    }
                    activityId = currNodeId.substring(currNodeId.indexOf("$") + 1);
                    if ("sourcefield".equals(field)) {
                        this.getModel().setValue("fieldactivityid", (Object)activityId, selectRowIndex);
                    }
                }
                String displayName = "";
                if (HRStringUtils.equals((String)"sourcefield", (String)field)) {
                    displayName = EntityFieldTreeUtil.getDisplayNameContainsEntry((String)currNode, (Boolean)Boolean.TRUE);
                } else {
                    displayName = EntityFieldTreeUtil.getDisplayNameContainsEntry((String)currNode, (Boolean)Boolean.FALSE);
                    currNode = currNode.substring(currNode.indexOf(".") + 1);
                }
                this.getModel().setValue(fieldName, (Object)new LocaleString(displayName), selectRowIndex);
                this.getModel().setValue(field, (Object)currNode, selectRowIndex);
            }
        }
        this.getView().updateView("subentryentity");
        this.lockBySys();
    }

    private boolean instanceofList(Object returnDataObject) {
        return returnDataObject instanceof List;
    }

    private boolean instanceofMap(Object returnDataObject) {
        return returnDataObject instanceof Map;
    }

    private void lockBySys() {
        DynamicObjectCollection subEntry = (DynamicObjectCollection)this.getModel().getValue("subentryentity");
        if (subEntry != null) {
            for (int i = 0; i < subEntry.size(); ++i) {
                DynamicObject dynamicObject = (DynamicObject)subEntry.get(i);
                if (!dynamicObject.getBoolean("sourceissyspreset")) continue;
                this.getView().setEnable(Boolean.valueOf(false), i, new String[]{"targetfieldnumber", "targetfield", "sourcefield", "soucefieldname"});
            }
        }
    }

    private boolean checkGroup() {
        int rowIndex = this.getModel().getEntryCurrentRowIndex("groupentry");
        if (rowIndex == -1 || this.getModel().getEntryRowCount("groupentry") == 0) {
            return true;
        }
        FilterGrid filterGrid = (FilterGrid)this.getView().getControl("filtergridap");
        FilterCondition filterCondition = filterGrid.getFilterGridState().getFilterCondition();
        String fcString = SerializationUtils.toJsonString((Object)filterCondition);
        this.getModel().setValue("condition", (Object)fcString, rowIndex);
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("groupentry");
        boolean isPass = true;
        for (DynamicObject dynamicObject : entryEntity) {
            String actGroupName = dynamicObject.getString("actgroupname");
            if (HRStringUtils.isEmpty((String)actGroupName)) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6d3b\u52a8\u7ec4\u540d\u79f0\u3002", (String)"ActivityGroupConfigEdit_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                isPass = false;
            }
            DynamicObjectCollection dynamicObjectCollection = dynamicObject.getDynamicObjectCollection("actgroupentity");
            boolean isActGroup = true;
            for (DynamicObject subDynamicObject : dynamicObjectCollection) {
                DynamicObject actGroup = subDynamicObject.getDynamicObject("groupactivity");
                if (actGroup != null) continue;
                isActGroup = false;
                break;
            }
            if (CollectionUtils.isEmpty((Collection)dynamicObjectCollection)) {
                isActGroup = false;
            }
            if (isActGroup) continue;
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u914d\u7f6e\u6d3b\u52a8\u7ec4\u3002", (String)"ActivityGroupConfigEdit_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            isPass = false;
        }
        return isPass;
    }

    private void fillActConfigData(int currentIndex) {
        this.getPageCache().put("currentIndex", String.valueOf(currentIndex));
        this.fillBindingLayout(currentIndex);
        int rowCount = this.getModel().getEntryRowCount("actschemeentry");
        for (int i = 0; i < rowCount; ++i) {
            String fc = i == currentIndex ? "var(--theme-color)" : "#000000";
            HashMap root = new HashMap(1);
            HashMap<String, String> map = new HashMap<String, String>(1);
            map.put("fc", fc);
            root.put("cardentryflexpanelap1", map);
            EntryGrid entryGrid = (EntryGrid)this.getControl("actschemeentry");
            entryGrid.setCustomProperties("actschemeentry", i, root);
        }
    }
}
