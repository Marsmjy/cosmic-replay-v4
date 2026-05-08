/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.EntityTypeUtil
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.filter.FilterCondition
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.FilterGrid
 *  kd.bos.form.control.events.RowClickEvent
 *  kd.bos.form.control.events.RowClickEventListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.common.constants.activity.ActivitySchemeConstant
 */
package kd.hr.hrcs.formplugin.web.activity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.EntityTypeUtil;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.filter.FilterCondition;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.FilterGrid;
import kd.bos.form.control.events.RowClickEvent;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.common.constants.activity.ActivitySchemeConstant;

public final class ActivityGroupConfigEdit
extends HRDataBaseEdit
implements RowClickEventListener,
BeforeF7SelectListener,
ActivitySchemeConstant {
    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        EntryGrid entryGrid = (EntryGrid)this.getView().getControl("groupentry");
        entryGrid.addRowClickListener((RowClickEventListener)this);
        BasedataEdit groupActity = (BasedataEdit)this.getView().getControl("groupactivity");
        groupActity.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        DynamicObjectCollection groupentry = (DynamicObjectCollection)this.getModel().getValue("groupentry");
        if (groupentry.size() > 0) {
            this.setActivityGroupConfig(Boolean.FALSE);
        } else {
            this.setActivityGroupConfig(Boolean.TRUE);
        }
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (status.equals((Object)OperationStatus.VIEW)) {
            this.getView().setEnable(Boolean.TRUE, new String[]{"filtergridap"});
        } else {
            this.getView().setEnable(Boolean.FALSE, new String[]{"filtergridap"});
        }
        this.setRuleCondition();
        this.openGroup(this.getModel().getDataEntity().getDynamicObject("app"));
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        DynamicObjectCollection groupentry = (DynamicObjectCollection)this.getModel().getValue("groupentry");
        if (groupentry.size() > 0) {
            EntryGrid entryGrid = (EntryGrid)this.getControl("groupentry");
            entryGrid.selectRows(0);
            String fc = "var(--theme-color)";
            HashMap root = new HashMap(1);
            HashMap<String, String> map = new HashMap<String, String>(1);
            map.put("fc", fc);
            root.put("cardentryflexpanelap11", map);
            entryGrid.setCustomProperties("groupentry", 0, root);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        String operateKey = afterDoOperationEventArgs.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"newgroupentry") || HRStringUtils.equals((String)operateKey, (String)"deletegroupentry")) {
            int entryRowCount = this.getModel().getEntryRowCount("groupentry");
            if (entryRowCount > 0) {
                this.setActivityGroupConfig(Boolean.FALSE);
            } else {
                this.setActivityGroupConfig(Boolean.TRUE);
            }
        }
        if (HRStringUtils.equals((String)operateKey, (String)"deletegroupentry")) {
            this.clearFilterGrid();
            this.getPageCache().put("lastRowIndex", null);
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        String propName = args.getProperty().getName();
        if (HRStringUtils.equals((String)propName, (String)"actgroupnameview") || HRStringUtils.equals((String)propName, (String)"actgroupdescview")) {
            OrmLocaleValue actGroupNameView = (OrmLocaleValue)this.getModel().getValue(propName);
            int rowIndex = this.getModel().getEntryCurrentRowIndex("groupentry");
            this.getModel().setValue(HRStringUtils.substringBeforeLast((String)propName, (String)"view"), (Object)actGroupNameView, rowIndex);
        } else if (HRStringUtils.equals((String)propName, (String)"bizobj")) {
            this.setRuleCondition();
        } else if (HRStringUtils.equals((String)propName, (String)"app")) {
            this.openGroup(this.getModel().getDataEntity().getDynamicObject("app"));
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void entryRowClick(RowClickEvent evt) {
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (!status.equals((Object)OperationStatus.VIEW)) {
            this.getView().setEnable(Boolean.TRUE, new String[]{"filtergridap"});
        }
        int rowIndex = this.getModel().getEntryCurrentRowIndex("groupentry");
        OrmLocaleValue actGroupName = (OrmLocaleValue)this.getModel().getValue("actgroupname", rowIndex);
        this.getModel().setValue("actgroupnameview", (Object)actGroupName);
        OrmLocaleValue actGroupDesc = (OrmLocaleValue)this.getModel().getValue("actgroupdesc", rowIndex);
        this.getModel().setValue("actgroupdescview", (Object)actGroupDesc);
        FilterGrid filterGrid = (FilterGrid)this.getView().getControl("filtergridap");
        FilterCondition filterCondition = filterGrid.getFilterGridState().getFilterCondition();
        String fcString = SerializationUtils.toJsonString((Object)filterCondition);
        String lastRowIndexString = this.getPageCache().get("lastRowIndex");
        if (HRStringUtils.isNotEmpty((String)lastRowIndexString)) {
            int lastRowIndex = Integer.parseInt(lastRowIndexString);
            if (lastRowIndex != rowIndex) {
                this.getModel().setValue("condition", (Object)fcString, lastRowIndex);
            } else {
                this.setConditionValue(fcString);
                return;
            }
        }
        this.getPageCache().put("lastRowIndex", String.valueOf(rowIndex));
        String condition = (String)this.getModel().getValue("condition", rowIndex);
        if (HRStringUtils.isNotEmpty((String)condition)) {
            this.setConditionValue(condition);
        } else {
            this.clearFilterGrid();
        }
        if (status.equals((Object)OperationStatus.VIEW)) {
            this.getView().setEnable(Boolean.FALSE, new String[]{"filtergridap"});
        }
        this.doRowClick(evt);
        this.deleteActivity();
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        block5: {
            String operateKey;
            block4: {
                super.beforeDoOperation(args);
                FormOperate source = (FormOperate)args.getSource();
                operateKey = source.getOperateKey();
                if (!HRStringUtils.equals((String)operateKey, (String)"newgroupentry")) break block4;
                DynamicObject bizobj = (DynamicObject)this.getModel().getValue("bizobj");
                if (bizobj != null) break block5;
                String message = ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e1a\u52a1\u5bf9\u8c61\u3002", (String)"ActivityGroupConfigEdit_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                this.getView().showErrorNotification(message);
                args.setCancel(true);
                break block5;
            }
            if (HRStringUtils.equals((String)operateKey, (String)"deleteentry")) {
                int currentRowIndex = this.getModel().getEntryCurrentRowIndex("actschemeentry");
                DynamicObject activity = (DynamicObject)this.getModel().getValue("activity", currentRowIndex);
                if (activity != null) {
                    Long activityId = activity.getLong("id");
                    DynamicObjectCollection activityGroups = this.getModel().getEntryEntity("actgroupentity");
                    if (activityGroups != null) {
                        for (DynamicObject activityGroup : activityGroups) {
                            Long id;
                            DynamicObject groupactivity = activityGroup.getDynamicObject("groupactivity");
                            if (groupactivity == null || !(id = Long.valueOf(groupactivity.getLong("id"))).equals(activityId)) continue;
                            this.getView().showConfirm(ResManager.loadKDString((String)"\u8be5\u6d3b\u52a8\u5df2\u88ab\u6d3b\u52a8\u7ec4\u5f15\u7528\uff0c\u5220\u9664\u8be5\u6d3b\u52a8\u540e\uff0c\u4e5f\u5c06\u4f1a\u4ece\u6d3b\u52a8\u7ec4\u4e2d\u5220\u9664\uff0c\u786e\u5b9a\u5220\u9664\u5417\uff1f", (String)"ActivityGroupConfigEdit_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.YesNo, new ConfirmCallBackListener("deleteentry"));
                            args.setCancel(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        super.confirmCallBack(messageBoxClosedEvent);
        String callBackId = messageBoxClosedEvent.getCallBackId();
        if ("deleteentry".equals(callBackId) && messageBoxClosedEvent.getResult() == MessageBoxResult.Yes) {
            int currentRowIndex = this.getModel().getEntryCurrentRowIndex("actschemeentry");
            DynamicObject activity = (DynamicObject)this.getModel().getValue("activity", currentRowIndex);
            if (activity != null) {
                Long activityId = activity.getLong("id");
                DynamicObjectCollection activityGroups = this.getModel().getEntryEntity("actgroupentity");
                DynamicObject deleteDyn = null;
                int tag = 0;
                int delTag = 0;
                for (DynamicObject activityGroup : activityGroups) {
                    Long id;
                    DynamicObject groupactivity = activityGroup.getDynamicObject("groupactivity");
                    if (groupactivity != null && (id = Long.valueOf(groupactivity.getLong("id"))).equals(activityId)) {
                        deleteDyn = groupactivity;
                        delTag = tag;
                        break;
                    }
                    ++tag;
                }
                if (deleteDyn != null) {
                    this.getModel().deleteEntryRow("actgroupentity", delTag);
                }
                this.getModel().deleteEntryRow("actschemeentry", currentRowIndex);
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String propName = beforeF7SelectEvent.getProperty().getName();
        if (HRStringUtils.equals((String)propName, (String)"groupactivity")) {
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("actschemeentry");
            List activityIds = entryEntity.stream().map(entry -> entry.getLong("activity.id")).collect(Collectors.toList());
            DynamicObjectCollection activityGroups = this.getModel().getEntryEntity("actgroupentity");
            List activityGroupIds = activityGroups.stream().filter(entry -> entry.get("groupactivity") != null).map(entry -> entry.getLong("groupactivity.id")).collect(Collectors.toList());
            int rowIndex = this.getModel().getEntryCurrentRowIndex("actgroupentity");
            DynamicObject groupactivity = (DynamicObject)this.getModel().getValue("groupactivity", rowIndex);
            QFilter idFilter = new QFilter("id", "not in", activityGroupIds);
            if (groupactivity != null) {
                Long groupactivityId = groupactivity.getLong("id");
                idFilter.or(new QFilter("id", "=", (Object)groupactivityId));
                ((ListShowParameter)beforeF7SelectEvent.getFormShowParameter()).setSelectedRows((Object[])new Long[]{groupactivityId});
            }
            beforeF7SelectEvent.getCustomQFilters().add(new QFilter("id", "in", activityIds));
            beforeF7SelectEvent.getCustomQFilters().add(idFilter);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void setConditionValue(String condition) {
        FilterGrid filterGrid = (FilterGrid)this.getView().getControl("filtergridap");
        FilterCondition filterCondition1 = (FilterCondition)SerializationUtils.fromJsonString((String)condition, FilterCondition.class);
        filterGrid.SetValue(filterCondition1);
    }

    @ExcludeFromJacocoGeneratedReport
    private void deleteActivity() {
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("actschemeentry");
        List activityIds = entryEntity.stream().map(entry -> entry.getLong("activity.id")).collect(Collectors.toList());
        DynamicObjectCollection activityGroups = this.getModel().getEntryEntity("actgroupentity");
        int tag = 0;
        ArrayList delIndexs = Lists.newArrayListWithCapacity((int)activityGroups.size());
        for (DynamicObject activityGroup : activityGroups) {
            DynamicObject groupactivity = activityGroup.getDynamicObject("groupactivity");
            if (groupactivity != null) {
                Long id = groupactivity.getLong("id");
                if (!activityIds.contains(id)) {
                    delIndexs.add(tag);
                }
            } else {
                delIndexs.add(tag);
            }
            ++tag;
        }
        int[] indexs = delIndexs.stream().mapToInt(Integer::valueOf).toArray();
        this.getModel().deleteEntryRows("actgroupentity", indexs);
    }

    private void setActivityGroupConfig(Boolean hide) {
        this.getView().setVisible(Boolean.valueOf(hide == false), new String[]{"groupconf"});
        this.getView().setVisible(hide, new String[]{"groupnonedata"});
    }

    private void setRuleCondition() {
        DynamicObject bizobj = (DynamicObject)this.getModel().getValue("bizobj");
        if (bizobj == null) {
            return;
        }
        String number = bizobj.getString("number");
        FilterGrid filterGrid = (FilterGrid)this.getView().getControl("filtergridap");
        MainEntityType entityType = EntityMetadataCache.getDataEntityType((String)number);
        List filterColumns = EntityTypeUtil.getInstance().getFilterColumns((IDataEntityType)entityType);
        filterGrid.getFieldColumns().clear();
        filterGrid.setFieldColumns(filterColumns);
        filterGrid.setEntityNumber(number);
        this.clearFilterGrid();
        this.getPageCache().put("lastRowIndex", null);
    }

    private void clearFilterGrid() {
        FilterCondition filterCondition = new FilterCondition();
        FilterGrid filterGrid = (FilterGrid)this.getView().getControl("filtergridap");
        filterGrid.SetValue(filterCondition);
        filterCondition = null;
        filterGrid.SetValue(filterCondition);
        this.getView().updateView("filtergridap");
    }

    private void doRowClick(RowClickEvent evt) {
        int currentIndex = evt.getRow();
        int rowCount = this.getModel().getEntryRowCount("groupentry");
        for (int row = 0; row < rowCount; ++row) {
            String fc = row == currentIndex ? "var(--theme-color)" : "#000000";
            HashMap root = new HashMap(1);
            HashMap<String, String> map = new HashMap<String, String>(1);
            map.put("fc", fc);
            root.put("cardentryflexpanelap11", map);
            EntryGrid entryGrid = (EntryGrid)this.getControl("groupentry");
            entryGrid.setCustomProperties("groupentry", row, root);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void openGroup(DynamicObject app) {
        if (app == null) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"advconap11"});
        } else {
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_activitygroupconfig");
            String appId = app.getString("id");
            DynamicObject config = serviceHelper.queryOriginalOne("appid,isopen", new QFilter("appid", "=", (Object)appId));
            if (config == null || !config.getBoolean("isopen")) {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"advconap11"});
                this.getModel().deleteEntryData("groupentry");
            } else {
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"advconap11"});
            }
        }
    }
}
