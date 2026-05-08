/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.EntityTypeUtil
 *  kd.bos.entity.GetFilterFieldsParameter
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.filter.FilterBuilder
 *  kd.bos.entity.filter.FilterCondition
 *  kd.bos.form.IFormView
 *  kd.bos.form.control.FilterGrid
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.common.constants.perm.PermFormCommonUtil
 *  kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper
 *  org.apache.commons.collections.CollectionUtils
 */
package kd.hr.hrcs.formplugin.web.datarule;

import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.EntityTypeUtil;
import kd.bos.entity.GetFilterFieldsParameter;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.filter.FilterBuilder;
import kd.bos.entity.filter.FilterCondition;
import kd.bos.form.IFormView;
import kd.bos.form.control.FilterGrid;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.common.constants.perm.PermFormCommonUtil;
import kd.sdk.hr.hbp.business.helper.history.HisModelServiceHelper;
import org.apache.commons.collections.CollectionUtils;

public final class HRDataRuleEditPlugin
extends HRDataBaseEdit
implements BeforeFilterF7SelectListener {
    private static final Log LOGGER = LogFactory.getLog(HRDataRuleEditPlugin.class);

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        FilterGrid filterGrid = (FilterGrid)this.getView().getControl("filtergridap");
        filterGrid.addBeforeF7SelectListener((BeforeFilterF7SelectListener)this);
    }

    public void beforeF7Select(BeforeFilterF7SelectEvent evt) {
        String entityNumber = evt.getRefEntityId();
        if (HRStringUtils.isEmpty((String)entityNumber)) {
            return;
        }
        if (HisModelServiceHelper.isInheritHisModelTemplate((String)entityNumber)) {
            evt.addCustomQFilter(new QFilter("iscurrentversion", "=", (Object)"1"));
        }
    }

    public void afterLoadData(EventObject evt) {
        super.afterLoadData(evt);
        this.getView().setEnable(Boolean.FALSE, new String[]{"entitynum"});
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        boolean visible = true;
        if (OperationStatus.VIEW.equals((Object)status)) {
            visible = false;
        }
        PermFormCommonUtil.setFilterGridAddBtnVisible((IFormView)this.getView(), (String)"filtergridap", (boolean)visible);
    }

    public void afterCreateNewData(EventObject evt) {
        super.afterCreateNewData(evt);
        String prevFocusEntNum = (String)this.getView().getFormShowParameter().getCustomParam("fsp_custom_param_entitynum");
        if (HRStringUtils.isNotEmpty((String)prevFocusEntNum)) {
            this.getModel().setValue("entitynum", (Object)prevFocusEntNum);
            this.getView().setEnable(Boolean.FALSE, new String[]{"entitynum"});
        } else {
            this.getView().setEnable(Boolean.TRUE, new String[]{"entitynum"});
        }
    }

    public void afterBindData(EventObject evt) {
        super.afterBindData(evt);
        this.refreshFilterGrid();
    }

    public void propertyChanged(PropertyChangedArgs evt) {
        super.propertyChanged(evt);
        String propName = evt.getProperty().getName();
        if (HRStringUtils.equals((String)"entitynum", (String)propName)) {
            this.clearDataPermFilterGrid();
            this.refreshFilterGrid();
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        AbstractOperate op;
        String operateKey;
        if (args.getSource() instanceof AbstractOperate && HRStringUtils.equals((String)"save", (String)(operateKey = (op = (AbstractOperate)args.getSource()).getOperateKey()))) {
            this.doSave(args);
        }
        FormOperate formOperate = (FormOperate)args.getSource();
        formOperate.getOption().setVariableValue("tag_of_view", Boolean.TRUE.toString());
    }

    private void doSave(BeforeDoOperationEventArgs args) {
        String ruleStr;
        FilterGrid filterGrid = (FilterGrid)this.getControl("filtergridap");
        FilterCondition fc = filterGrid.getFilterGridState().getFilterCondition();
        boolean canSave = false;
        if (fc != null && !CollectionUtils.isEmpty((Collection)fc.getFilterRow()) && HRStringUtils.isNotEmpty((String)(ruleStr = SerializationUtils.toJsonString((Object)fc)))) {
            DynamicObject dataEntity = this.getModel().getDataEntity();
            DynamicObject entityObj = dataEntity.getDynamicObject("entitynum");
            String entityNum = String.valueOf(entityObj.getPkValue());
            MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)entityNum);
            FilterBuilder filterBuilder = new FilterBuilder(dataEntityType, fc);
            filterBuilder.buildFilter();
            this.getModel().setValue("rule", (Object)ruleStr);
            canSave = true;
        }
        if (!canSave) {
            args.setCancel(true);
            String errorInfo = ResManager.loadKDString((String)"\u8bf7\u914d\u7f6e\u89c4\u5219\u3002", (String)"HRDataRuleEditPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            args.setCancelMessage(errorInfo);
            this.getView().showErrorNotification(errorInfo);
        }
    }

    private void clearDataPermFilterGrid() {
        FilterGrid filterGrid = (FilterGrid)this.getView().getControl("filtergridap");
        FilterCondition filterCondition = new FilterCondition();
        filterGrid.SetValue(filterCondition);
        filterCondition = null;
        filterGrid.SetValue(filterCondition);
        this.getModel().setValue("rule", null);
        this.getView().updateView("filtergridap");
    }

    private void refreshFilterGrid() {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        DynamicObject entityObj = dataEntity.getDynamicObject("entitynum");
        if (entityObj == null) {
            return;
        }
        String entityNum = String.valueOf(entityObj.getPkValue());
        String ruleStr = dataEntity.getString("rule");
        FilterGrid filterGrid = (FilterGrid)this.getControl("filtergridap");
        filterGrid.setEntityNumber(entityNum);
        MainEntityType entityType = EntityMetadataCache.getDataEntityType((String)entityNum);
        GetFilterFieldsParameter fieldsParameter = new GetFilterFieldsParameter((IDataEntityType)entityType);
        fieldsParameter.setNeedMulBasedataField(true);
        List filterColumns = EntityTypeUtil.createFilterColumns((GetFilterFieldsParameter)fieldsParameter);
        filterGrid.setFilterColumns(filterColumns);
        if (HRStringUtils.isNotEmpty((String)ruleStr)) {
            FilterCondition fc = (FilterCondition)SerializationUtils.fromJsonString((String)ruleStr, FilterCondition.class);
            filterGrid.SetValue(fc);
        }
        this.getView().updateView("filtergridap");
    }
}
