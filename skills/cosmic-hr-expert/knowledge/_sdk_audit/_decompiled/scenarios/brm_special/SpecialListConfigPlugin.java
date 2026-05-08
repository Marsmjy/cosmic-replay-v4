/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.brm.business.model.UserRosterInfo
 *  kd.hr.brm.business.util.SpecialListUtil
 *  kd.hr.brm.business.web.PolicyServiceHelper
 *  kd.hr.brm.business.web.RosterHelper
 *  kd.hr.brm.business.web.SpecialListServiceHelper
 *  kd.hr.brm.common.constants.SpecialListConfigConstants
 *  kd.hr.brm.formplugin.web.IHROrgReset
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 */
package kd.hr.brm.formplugin.web;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.BasedataProp;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.brm.business.model.UserRosterInfo;
import kd.hr.brm.business.util.SpecialListUtil;
import kd.hr.brm.business.web.PolicyServiceHelper;
import kd.hr.brm.business.web.RosterHelper;
import kd.hr.brm.business.web.SpecialListServiceHelper;
import kd.hr.brm.common.constants.SpecialListConfigConstants;
import kd.hr.brm.formplugin.web.IHROrgReset;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;

public final class SpecialListConfigPlugin
extends HRBaseDataCommonEdit
implements IHROrgReset,
SpecialListConfigConstants,
BeforeF7SelectListener {
    private static final int START_LINE = -1;

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
    }

    private List<Long> getUserIds() {
        return this.getModel().getEntryEntity("brm_list_person").stream().map(dy -> dy.getLong("entityperson.id")).collect(Collectors.toList());
    }

    private List<Long> getEmpIds() {
        return this.getModel().getEntryEntity("brm_list_emp").stream().map(dy -> dy.getLong("entityemp.id")).collect(Collectors.toList());
    }

    private List<Long> getOrgIds() {
        return this.getModel().getEntryEntity("brm_list_org").stream().map(dy -> dy.getLong("entityorg.id")).collect(Collectors.toList());
    }

    public void afterBindData(EventObject e) {
        List<Long> ids;
        super.afterBindData(e);
        this.setListTypeVisible();
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (OperationStatus.EDIT == status) {
            this.getView().setEnable(Boolean.FALSE, new String[]{"listtype", "listcategory", "number"});
        } else {
            String createBu = (String)this.getView().getFormShowParameter().getCustomParams().get("bu");
            if (StringUtils.isNotEmpty((CharSequence)createBu)) {
                this.getModel().setValue("bu", (Object)Long.parseLong(createBu));
            } else {
                this.getModel().setValue("bu", (Object)RequestContext.get().getOrgId());
                this.resetHROrg(this.getModel(), "bu");
            }
        }
        this.getModel().setValue("listenble", this.getModel().getValue("enable"));
        this.getModel().setDataChanged(false);
        if (this.isEmployee()) {
            ids = this.getEmpIds();
            this.getModel().deleteEntryData("brm_list_emp");
            this.setPersonFieldEntry(ids, ids.size(), -1);
        }
        if (this.isUser()) {
            ids = this.getUserIds();
            this.getModel().deleteEntryData("brm_list_person");
            this.setPersonFieldEntry(ids, ids.size(), -1);
        }
    }

    private void setListTypeVisible() {
        Object listType = this.getModel().getValue("listtype");
        if (HRStringUtils.equals((String)"basedata", (String)listType.toString())) {
            this.setCategoryVisible("listcategory", "stringadvconap");
            this.setListCategoryVisible();
            this.clearCategory("list_string_param");
        } else {
            this.setCategoryVisible("stringadvconap", "listcategory", "personadvconap", "empadvconap", "orgadvconap");
            this.clearCategory("brm_list_org", "brm_list_person", "brm_list_emp");
        }
    }

    private void setListCategoryVisible() {
        Object listCategory = this.getModel().getValue("listcategory");
        if (null == listCategory) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"stringadvconap", "personadvconap", "empadvconap", "orgadvconap"});
            return;
        }
        if (this.isUser()) {
            this.setCategoryVisible("personadvconap", "empadvconap", "orgadvconap");
            this.clearCategory("brm_list_emp", "brm_list_org");
        } else if (this.isEmployee()) {
            this.setCategoryVisible("empadvconap", "orgadvconap", "personadvconap");
            this.clearCategory("brm_list_person", "brm_list_org");
        } else if (this.isOrg()) {
            this.setCategoryVisible("orgadvconap", "personadvconap", "empadvconap");
            this.clearCategory("brm_list_person", "brm_list_emp");
        }
    }

    private void clearCategory(String ... clearCategorys) {
        for (String clearCategory : clearCategorys) {
            this.getModel().deleteEntryData(clearCategory);
            this.getModel().getEntryEntity(clearCategory).clear();
            this.getModel().updateCache();
        }
    }

    private void setCategoryVisible(String visibleCategory, String ... unvisible) {
        this.getView().setVisible(Boolean.valueOf(true), new String[]{visibleCategory});
        this.getView().setVisible(Boolean.valueOf(false), unvisible);
        ((ComboEdit)this.getControl("listcategory")).setMustInput(true);
    }

    public void propertyChanged(PropertyChangedArgs e) {
        String prop;
        super.propertyChanged(e);
        switch (prop = e.getProperty().getName()) {
            case "listtype": {
                this.setListTypeVisible();
                break;
            }
            case "listcategory": {
                this.setListCategoryVisible();
                break;
            }
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "save": {
                this.validateRepeat(args);
                break;
            }
            case "newentry_person": {
                this.showPersonF7();
                args.setCancel(true);
                break;
            }
            case "newentry_org": {
                this.showOrgF7();
                args.setCancel(true);
                break;
            }
        }
    }

    private void validateRepeat(BeforeDoOperationEventArgs args) {
        PolicyServiceHelper policyHelper;
        DynamicObject[] policyCol;
        Long specialListId = (Long)this.getModel().getDataEntity().getPkValue();
        if (SpecialListServiceHelper.existSpecialListValue((Long)specialListId, (String)"number", (String)this.getModel().getValue("number").toString())) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u540d\u5355\u7f16\u7801\u5df2\u5b58\u5728\uff0c\u8bf7\u4fee\u6539\u3002", (String)"SpecialListConfigPlugin_0", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
        if (SpecialListServiceHelper.existSpecialListValue((Long)specialListId, (String)"name", (String)((OrmLocaleValue)this.getModel().getValue("name")).getLocaleValue())) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u540d\u5355\u540d\u79f0\u5df2\u5b58\u5728\uff0c\u8bf7\u4fee\u6539\u3002", (String)"SpecialListConfigPlugin_1", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
        Object listenble = this.getModel().getValue("listenble");
        if (null != specialListId && null != listenble && HRStringUtils.equals((String)"0", (String)listenble.toString()) && null != (policyCol = (policyHelper = new PolicyServiceHelper()).queryPolicyBySpecialList((Set)Sets.newHashSet((Object[])new Object[]{specialListId}))) && policyCol.length > 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8be5\u7279\u6b8a\u540d\u5355\u5df2\u88ab\u7b56\u7565\u5f15\u7528\uff0c\u4e0d\u5141\u8bb8\u7981\u7528\u7279\u6b8a\u540d\u5355\u3002", (String)"SpecialListConfigPlugin_2", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
        this.getModel().setValue("enable", listenble);
        Object listType = this.getModel().getValue("listtype");
        if (HRStringUtils.equals((String)"string", (String)listType.toString())) {
            DynamicObjectCollection listStringParam = this.getModel().getEntryEntity("list_string_param");
            if (null == listStringParam) {
                return;
            }
            Set notNullCollect = listStringParam.stream().map(dy -> dy.getString("liststringname").isEmpty()).collect(Collectors.toSet());
            if (notNullCollect.contains(Boolean.TRUE)) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u540d\u5355\u660e\u7ec6\u4e2d\u7684\u201c\u540d\u5355\u201d\u3002", (String)"SpecialListConfigPlugin_4", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            if ((long)listStringParam.size() != listStringParam.stream().map(dy -> dy.get("liststringname")).distinct().count()) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u5b57\u7b26\u4e32\u540d\u5355\u5b58\u5728\u91cd\u590d\uff0c\u8bf7\u4fee\u6539\u3002", (String)"SpecialListConfigPlugin_3", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
        }
    }

    private void showPersonF7() {
        String entityNumber = (String)this.getModel().getValue("listcategory");
        if (StringUtils.isEmpty((CharSequence)entityNumber)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u540d\u5355\u7c7b\u522b\u3002", (String)"SpecialListConfigPlugin_5", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
            return;
        }
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)entityNumber, (boolean)true, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, entityNumber));
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setMultiSelect(true);
        fsp.setShowTitle(false);
        fsp.setHasRight(true);
        ListFilterParameter filterParameter = new ListFilterParameter();
        if (this.isEmployee()) {
            List<Long> employeeIds = this.getEmpIds();
            if (!CollectionUtils.isEmpty(employeeIds)) {
                filterParameter.setFilter(new QFilter("id", "not in", employeeIds));
                fsp.setListFilterParameter(filterParameter);
            }
        } else {
            List<Long> userIds = this.getUserIds();
            if (!CollectionUtils.isEmpty(userIds)) {
                filterParameter.setFilter(new QFilter("id", "not in", userIds));
                fsp.setListFilterParameter(filterParameter);
            }
        }
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void showOrgF7() {
        String entityNumber = (String)this.getModel().getValue("listcategory");
        if (StringUtils.isEmpty((CharSequence)entityNumber)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u540d\u5355\u7c7b\u522b\u3002", (String)"SpecialListConfigPlugin_5", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
            return;
        }
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)entityNumber, (boolean)true, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, entityNumber));
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setMultiSelect(true);
        fsp.setShowTitle(false);
        fsp.setHasRight(true);
        List<Long> buIds = this.getOrgIds();
        if (!CollectionUtils.isEmpty(buIds)) {
            ListFilterParameter filterParameter = new ListFilterParameter();
            filterParameter.setFilter(new QFilter("id", "not in", buIds));
            fsp.setListFilterParameter(filterParameter);
        }
        if ("haos_adminorghrf7".equals(entityNumber)) {
            fsp.setCustomParam("queryDate", (Object)"1");
            fsp.setCustomParam("isIncludeFuture", (Object)true);
            fsp.setCustomParam("returnType", (Object)"boid");
        }
        this.getView().showForm((FormShowParameter)fsp);
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        if (null == evt.getReturnData()) {
            return;
        }
        String actionId = evt.getActionId();
        if ("haos_adminorghrf7".equals(actionId)) {
            this.fillOrgEntry((ListSelectedRowCollection)evt.getReturnData());
        } else if (actionId.contains("#")) {
            this.fillPersonEntry((ListSelectedRowCollection)evt.getReturnData(), Integer.parseInt(actionId.substring(actionId.lastIndexOf("#") + 1)));
        } else {
            this.fillPersonEntry((ListSelectedRowCollection)evt.getReturnData(), -1);
        }
    }

    private void fillPersonEntry(ListSelectedRowCollection selectedCol, int startLine) {
        int colSize = selectedCol.size();
        if (colSize == 0) {
            return;
        }
        List<Long> userIds = selectedCol.stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList());
        this.setPersonFieldEntry(userIds, colSize, startLine);
    }

    private void setPersonFieldEntry(List<Long> userIds, int addRows, int startLine) {
        List userRosterInfos;
        if (addRows == 0) {
            return;
        }
        if (this.isEmployee()) {
            Map userIdMap = SpecialListServiceHelper.getUserIdMap(userIds);
            userRosterInfos = SpecialListServiceHelper.getBosUserRosterInfo(new ArrayList(userIdMap.keySet()));
            userRosterInfos.forEach(userRosterInfo -> userRosterInfo.setId((Long)userIdMap.get(userRosterInfo.getId())));
        } else {
            userRosterInfos = SpecialListServiceHelper.getBosUserRosterInfo(userIds);
        }
        if (CollectionUtils.isEmpty((Collection)userRosterInfos)) {
            return;
        }
        if (this.isEmployee()) {
            this.setPersonFieldEntry(userRosterInfos, addRows, startLine, "brm_list_emp", "emp");
        } else {
            this.setPersonFieldEntry(userRosterInfos, addRows, startLine, "brm_list_person", "person");
        }
    }

    private void setPersonFieldEntry(List<UserRosterInfo> userRosterInfos, int addRows, int startLine, String entryEntity, String key) {
        int i;
        int startRows;
        IDataModel dataModel = this.getModel();
        dataModel.beginInit();
        if (startLine == -1) {
            startRows = dataModel.getEntryRowCount(entryEntity);
            dataModel.batchCreateNewEntryRow(entryEntity, addRows);
        } else {
            startRows = startLine;
            for (i = 1; i < addRows; ++i) {
                dataModel.insertEntryRow(entryEntity, startLine);
            }
        }
        i = 0;
        for (UserRosterInfo info : userRosterInfos) {
            dataModel.setValue("entity" + key, (Object)info.getId(), startRows + i);
            dataModel.setValue(key + "number", (Object)info.getNumber(), startRows + i);
            dataModel.setValue(key + "department", (Object)info.getDepartment(), startRows + i);
            ++i;
        }
        dataModel.endInit();
        this.getView().updateView(entryEntity);
    }

    private void fillOrgEntry(ListSelectedRowCollection selectedCol) {
        int colSize = selectedCol.size();
        if (colSize == 0) {
            return;
        }
        int rowCount = this.getModel().getEntryRowCount("brm_list_org");
        this.getModel().batchCreateNewEntryRow("brm_list_org", colSize);
        for (int i = 0; i < colSize; ++i) {
            ListSelectedRow row = selectedCol.get(i);
            long specialListId = (Long)row.getPrimaryKeyValue();
            this.getModel().setValue("entityorg", (Object)specialListId, rowCount + i);
        }
    }

    private boolean isEmployee() {
        return SpecialListUtil.isEmployee((String)((String)this.getModel().getValue("listcategory")));
    }

    private boolean isUser() {
        return SpecialListUtil.isUser((String)((String)this.getModel().getValue("listcategory")));
    }

    private boolean isOrg() {
        return SpecialListUtil.isOrg((String)((String)this.getModel().getValue("listcategory")));
    }

    private void changeEntityControl(String entityNumber) {
        BasedataProp basedataProp = (BasedataProp)((BasedataEdit)this.getControl("entityperson")).getProperty();
        basedataProp.setBaseEntityId(entityNumber);
        basedataProp.setComplexType((IDataEntityType)EntityMetadataCache.getDataEntityType((String)entityNumber));
    }

    protected List<String> getUnCheckField() {
        List uncheckFieldList = super.getUnCheckField();
        uncheckFieldList.add("brm_list_person");
        uncheckFieldList.add("brm_list_emp");
        uncheckFieldList.add("bu");
        return uncheckFieldList;
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        BasedataEdit mainEntityEdit = (BasedataEdit)this.getView().getControl("entityperson");
        mainEntityEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit mainEntityEdit2 = (BasedataEdit)this.getView().getControl("entityemp");
        mainEntityEdit2.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        List<Long> buIds;
        if (this.isEmployee()) {
            List<Long> employeeIds = this.getEmpIds();
            if (!CollectionUtils.isEmpty(employeeIds)) {
                beforeF7SelectEvent.getCustomQFilters().add(new QFilter("id", "not in", employeeIds));
                String action = this.getModel().getValue("listcategory") + "#" + beforeF7SelectEvent.getRow();
                beforeF7SelectEvent.getFormShowParameter().setCloseCallBack(new CloseCallBack((IFormPlugin)this, action));
            }
        } else if (this.isUser()) {
            List<Long> userIds = this.getUserIds();
            if (!CollectionUtils.isEmpty(userIds)) {
                beforeF7SelectEvent.getCustomQFilters().add(new QFilter("id", "not in", userIds));
                String action = this.getModel().getValue("listcategory") + "#" + beforeF7SelectEvent.getRow();
                beforeF7SelectEvent.getFormShowParameter().setCloseCallBack(new CloseCallBack((IFormPlugin)this, action));
            }
        } else if (this.isOrg() && !CollectionUtils.isEmpty(buIds = this.getOrgIds())) {
            beforeF7SelectEvent.getCustomQFilters().add(new QFilter("id", "not in", buIds));
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        FormOperate op = (FormOperate)args.getSource();
        String operateKey = op.getOperateKey();
        List<Long> ids = op.getListSelectedData().stream().map(dy -> (Long)dy.getPrimaryKeyValue()).collect(Collectors.toList());
        switch (operateKey) {
            case "save": 
            case "audit": 
            case "enable": 
            case "disable": 
            case "delete": {
                this.deleteCache(ids);
                break;
            }
        }
    }

    private void deleteCache(List<Long> ids) {
        RosterHelper helper = new RosterHelper();
        if (!ids.isEmpty()) {
            ids.forEach(arg_0 -> ((RosterHelper)helper).deleteRosterCache(arg_0));
        } else {
            Long id = (Long)this.getModel().getValue("id");
            helper.deleteRosterCache(id);
        }
    }
}
