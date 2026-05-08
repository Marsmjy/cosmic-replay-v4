/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.algo.DataSet
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper
 */
package kd.hr.hrcs.formplugin.web.perm.dimension;

import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper;

public final class DimensionNewEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final Log LOGGER = LogFactory.getLog(DimensionNewEdit.class);
    private static final String DIMENSION_NUM = "dimensionenum";
    private static final String ENTITY_CTRL = "entityctrl";
    private static final String KEY_CTRL_ENTRY = "ctrlentry";
    private static final String FIELD_IS_SYSPRESET = "issyspreset1";
    private static final String FIELD_IS_MUST = "ismust";
    private static final String FIELD_DESC = "desc";
    private static final String BAR_REFROLE = "refrole";

    public void registerListener(EventObject e) {
        super.registerListener(e);
        BasedataEdit entryEntityEdit = (BasedataEdit)this.getView().getControl("entitytype");
        entryEntityEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void afterBindData(EventObject evt) {
        super.afterBindData(evt);
        Object datasource = this.getModel().getValue("datasource");
        this.showEnumCtrl(datasource.toString());
        DynamicObject dataEntity = this.getModel().getDataEntity();
        long dimensionId = dataEntity.getLong("id");
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_entityctrl");
        QFilter filter = new QFilter("entryentity.dimension", "=", (Object)dimensionId);
        DynamicObject[] dys = serviceHelper.query("entitytype,entryentity.dimension,propkey,authrange,ismust,issyspreset,desc", new QFilter[]{filter});
        if (OperationStatus.EDIT.equals((Object)status) && dys.length > 0) {
            this.getView().setVisible(Boolean.TRUE, new String[]{ENTITY_CTRL});
            int index = 0;
            this.getModel().beginInit();
            for (DynamicObject dynamicObject : dys) {
                Map entityFieldMap;
                DynamicObjectCollection entryEntity = dynamicObject.getDynamicObjectCollection("entryentity");
                List filterRes = entryEntity.stream().filter(entry -> entry.getLong("dimension.id") == dimensionId).collect(Collectors.toList());
                String entityNum = dynamicObject.getString("entitytype.number");
                if (null == entityNum || (entityFieldMap = EntityCtrlServiceHelper.getEntityFieldMap((DynamicObject)dynamicObject.getDynamicObject("entitytype"))).isEmpty()) continue;
                for (DynamicObject dy : filterRes) {
                    this.getModel().createNewEntryRow(KEY_CTRL_ENTRY);
                    this.getModel().setValue("entity", (Object)entityNum, index);
                    String propKey = dy.getString("propkey");
                    String authRange = dy.getString("authrange");
                    this.getModel().setValue("authrange", (Object)authRange, index);
                    this.getModel().setValue("propkey", (Object)propKey, index);
                    this.getModel().setValue("propname", entityFieldMap.get(propKey) != null ? entityFieldMap.get(propKey) : propKey, index);
                    this.getModel().setValue(FIELD_IS_SYSPRESET, dy.get("issyspreset"), index);
                    this.getModel().setValue(FIELD_IS_MUST, dy.get(FIELD_IS_MUST), index);
                    this.getModel().setValue(FIELD_DESC, dy.get(FIELD_DESC), index++);
                }
            }
            this.getModel().endInit();
            this.getView().updateView(KEY_CTRL_ENTRY);
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{ENTITY_CTRL});
        }
        this.getModel().setDataChanged(false);
    }

    private void showEnumCtrl(String datasource) {
        BasedataEdit entityType = (BasedataEdit)this.getControl("entitytype");
        BasedataEdit hrBU = (BasedataEdit)this.getControl("hrbu");
        EntryGrid entry = (EntryGrid)this.getControl("entry");
        BasedataEdit orgClassify = (BasedataEdit)this.getControl("org_classify");
        this.getView().setVisible(Boolean.FALSE, new String[]{"org_classify"});
        if (!Objects.nonNull(datasource)) {
            return;
        }
        if (HRStringUtils.equals((String)"enum", (String)datasource)) {
            this.getView().setVisible(Boolean.TRUE, new String[]{DIMENSION_NUM});
            entityType.setMustInput(false);
            hrBU.setMustInput(false);
            orgClassify.setMustInput(false);
            long dimensionId = this.getModel().getDataEntity().getLong("id");
            DataSet roles = EntityCtrlServiceHelper.getRoles((long)dimensionId);
            int count = roles.count("id", true);
            roles.close();
            if (count > 0) {
                int size = this.getModel().getEntryEntity("entry").size();
                for (int index = 0; index < size; ++index) {
                    this.getView().setEnable(Boolean.FALSE, index, new String[]{"value"});
                }
            }
            entry.setMustInput("displayvalue", true);
            entry.setMustInput("value", true);
            this.getModel().setValue("hrbu", (Object)"");
        } else if (HRStringUtils.equals((String)"basedata", (String)datasource)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{DIMENSION_NUM});
            this.getModel().deleteEntryData("entry");
            entityType.setMustInput(true);
            hrBU.setMustInput(false);
            orgClassify.setMustInput(false);
            entry.setMustInput("displayvalue", false);
            entry.setMustInput("value", false);
            this.getView().setEnable(Boolean.FALSE, new String[]{"showtype"});
            this.getModel().setValue("hrbu", (Object)"");
        } else if (HRStringUtils.equals((String)"hrbu", (String)datasource)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{DIMENSION_NUM});
            this.getModel().deleteEntryData("entry");
            entityType.setMustInput(false);
            hrBU.setMustInput(true);
            orgClassify.setMustInput(false);
            entry.setMustInput("displayvalue", false);
            entry.setMustInput("value", false);
            this.getView().setEnable(Boolean.TRUE, new String[]{"showtype"});
        } else if (HRStringUtils.equals((String)"orgteam", (String)datasource)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{DIMENSION_NUM});
            this.getView().setVisible(Boolean.TRUE, new String[]{"org_classify"});
            this.getModel().deleteEntryData("entry");
            entityType.setMustInput(true);
            this.getModel().setValue("entitytype", (Object)"haos_adminorgdetail");
            hrBU.setMustInput(true);
            orgClassify.setMustInput(true);
            entry.setMustInput("displayvalue", false);
            entry.setMustInput("value", false);
        }
        this.getModel().setDataChanged(false);
    }

    private void handleShowTypeForEntityType() {
        DynamicObject item = (DynamicObject)this.getModel().getValue("entitytype");
        String mainEntityInheritPath = Optional.ofNullable(item).map(it -> EntityMetadataCache.getDataEntityType((String)it.getString("id")).getInheritPath()).orElse(null);
        String baseEntityInheritPath = EntityMetadataCache.getDataEntityType((String)"hbp_bd_treetpl_all").getInheritPath();
        if (HRStringUtils.isEmpty((String)mainEntityInheritPath) || !mainEntityInheritPath.startsWith(baseEntityInheritPath)) {
            this.getModel().setValue("showtype", (Object)"list");
            this.getView().setEnable(Boolean.FALSE, new String[]{"showtype"});
        } else {
            this.getView().setEnable(Boolean.TRUE, new String[]{"showtype"});
        }
    }

    public void propertyChanged(PropertyChangedArgs evt) {
        super.propertyChanged(evt);
        String property = evt.getProperty().getName();
        ChangeData[] changeDatas = evt.getChangeSet();
        switch (property) {
            case "datasource": {
                String newValue = (String)changeDatas[0].getNewValue();
                this.showEnumCtrl(newValue);
                break;
            }
            case "entitytype": {
                if (!HRStringUtils.equals((String)"basedata", (String)((String)this.getModel().getValue("datasource")))) break;
                this.limitBasedataType((DynamicObject)changeDatas[0].getNewValue());
                this.handleShowTypeForEntityType();
                break;
            }
        }
    }

    private void limitBasedataType(DynamicObject newValue) {
        if (null == newValue) {
            return;
        }
        DataEntityPropertyCollection toPros = EntityMetadataCache.getDataEntityType((String)newValue.getString("id")).getProperties();
        boolean hasName = false;
        boolean hasNumber = false;
        for (IDataEntityProperty prop : toPros) {
            if (HRStringUtils.equals((String)"name", (String)prop.getName())) {
                hasName = true;
            }
            if (HRStringUtils.equals((String)"number", (String)prop.getName())) {
                hasNumber = true;
            }
            if (!hasName || !hasNumber) continue;
            return;
        }
        this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u4e1a\u52a1\u5bf9\u8c61\u65e0\u7f16\u7801\u6216\u540d\u79f0\u5b57\u6bb5\uff0c\u4e0d\u5141\u8bb8\u914d\u7f6e\u7ef4\u5ea6\u3002", (String)"DimensionNewEdit_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        this.getModel().beginInit();
        this.getModel().setValue("entitytype", (Object)"");
        this.getModel().endInit();
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        super.beforeItemClick(evt);
        String itemKey = evt.getItemKey();
        if (HRStringUtils.equals((String)BAR_REFROLE, (String)itemKey)) {
            long dimensionId = this.getModel().getDataEntity().getLong("id");
            FormShowParameter fsp = new FormShowParameter();
            fsp.setFormId("hrcs_refdetails");
            fsp.setCustomParam("dimension.id", (Object)dimensionId);
            fsp.getOpenStyle().setShowType(ShowType.Modal);
            this.getView().showForm(fsp);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate source = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)"save", (CharSequence)source.getOperateKey())) {
            String hadConfirm;
            this.getModel().setValue("modifytime", (Object)new Date());
            if (HRStringUtils.equals((String)((String)this.getModel().getValue("datasource")), (String)"hrbu")) {
                this.getModel().setValue("entitytype", (Object)"bos_org");
            }
            if ((hadConfirm = this.getPageCache().get("hadConfirm")) != null) {
                this.getPageCache().remove("hadConfirm");
                args.setCancel(false);
                return;
            }
            if (!this.checkEntitytype()) {
                args.setCancel(true);
                return;
            }
            OperationStatus status = this.getView().getFormShowParameter().getStatus();
            String datasource = (String)this.getModel().getValue("datasource");
            if (status.equals((Object)OperationStatus.EDIT) && datasource.equals("enum")) {
                this.checkEnumChange(args);
            }
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        String callBackId = evt.getCallBackId();
        MessageBoxResult result = evt.getResult();
        if (callBackId.equals("save_continue") && result.equals((Object)MessageBoxResult.Yes)) {
            this.getPageCache().put("hadConfirm", "confirmed");
            this.getView().invokeOperation("save");
        }
    }

    private void checkEnumChange(BeforeDoOperationEventArgs args) {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        DynamicObjectCollection entry = dataEntity.getDynamicObjectCollection("entry");
        long dimensionId = dataEntity.getLong("id");
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_dimension");
        QFilter filter = new QFilter("id", "=", (Object)dimensionId);
        DynamicObject var = serviceHelper.queryOne("entry.value,entry.displayvalue", filter);
        DynamicObjectCollection entryFromDB = var.getDynamicObjectCollection("entry");
        Map<String, String> map = entry.stream().collect(Collectors.toMap(it -> it.getString("displayvalue"), it -> it.getString("value"), (oldValue, newValue) -> newValue));
        Map<String, String> mapFromDB = entryFromDB.stream().collect(Collectors.toMap(it -> it.getString("displayvalue"), it -> it.getString("value"), (oldValue, newValue) -> newValue));
        for (Map.Entry<String, String> tmp : mapFromDB.entrySet()) {
            if (map.containsKey(tmp.getKey()) && map.get(tmp.getKey()).equals(tmp.getValue())) continue;
            this.getView().showConfirm(ResManager.loadKDString((String)"\u8c03\u6574\u679a\u4e3e\u503c\u4f1a\u5f71\u54cd\u89d2\u8272\u7ef4\u5ea6\uff0c\u786e\u5b9a\u4fee\u6539\u5417\uff1f", (String)"DimensionNewEdit_01", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, new ConfirmCallBackListener("save_continue", (IFormPlugin)this));
            args.setCancel(true);
            break;
        }
    }

    private boolean checkEntitytype() {
        Object datasource = this.getModel().getValue("datasource");
        Object entityType = this.getModel().getValue("entitytype");
        if (Objects.nonNull(datasource) && HRStringUtils.equals((String)"basedata", (String)datasource.toString()) && Objects.isNull(entityType)) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e1a\u52a1\u5bf9\u8c61\u3002", (String)"DimensionNewEdit_02", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        if (Objects.nonNull(datasource)) {
            String showType = this.getModel().getValue("showtype").toString();
            if (HRStringUtils.equals((String)"basedata", (String)datasource.toString()) && HRStringUtils.equals((String)"checkbox", (String)showType)) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u57fa\u7840\u8d44\u6599\u7c7b\u578b\u7ef4\u5ea6\u7684\u663e\u793a\u7c7b\u578b\u4e0d\u80fd\u4e3a\u590d\u9009\u6846", (String)"DimensionNewEdit_06", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return false;
            }
            if (HRStringUtils.equals((String)"basedata", (String)datasource.toString()) && HRStringUtils.equals((String)"tree", (String)showType)) {
                DynamicObject item = (DynamicObject)entityType;
                String mainEntityInheritPath = EntityMetadataCache.getDataEntityType((String)item.getString("id")).getInheritPath();
                String baseEntityInheritPath = EntityMetadataCache.getDataEntityType((String)"hbp_bd_treetpl_all").getInheritPath();
                if (HRStringUtils.isEmpty((String)mainEntityInheritPath) || !mainEntityInheritPath.startsWith(baseEntityInheritPath)) {
                    this.getView().showErrorNotification(ResManager.loadKDString((String)"\u4e1a\u52a1\u5bf9\u8c61\u6ca1\u6709\u7ee7\u627f\u5e73\u53f0\u6811\u5f62\u57fa\u7840\u8d44\u6599\u6a21\u677f\uff08bos_basetreetpl\uff09\uff0c\u663e\u793a\u7c7b\u578b\u4e0d\u5141\u8bb8\u8bbe\u7f6e\u4e3a\u6811\u5f62", (String)"DimensionNewEdit_07", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    return false;
                }
            }
        }
        if (Objects.nonNull(datasource) && HRStringUtils.equals((String)"enum", (String)datasource.toString())) {
            if (CollectionUtils.isEmpty((Collection)this.getModel().getEntryEntity("entry"))) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u8bbe\u7f6e\u679a\u4e3e\u503c", (String)"DimensionNewEdit_03", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return false;
            }
            DynamicObjectCollection entry = this.getModel().getDataEntity().getDynamicObjectCollection("entry");
            Set displayValues = entry.stream().map(it -> it.getString("displayvalue")).collect(Collectors.toSet());
            if (entry.size() > displayValues.size()) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u679a\u4e3e\u540d\u79f0\u91cd\u590d\uff0c\u8bf7\u91cd\u65b0\u586b\u5199\u3002", (String)"DimensionNewEdit_04", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return false;
            }
        }
        return true;
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        ListShowParameter lsp = (ListShowParameter)evt.getFormShowParameter();
        String property = evt.getProperty().getName();
        if (HRStringUtils.equals((String)property, (String)"entitytype")) {
            QFilter filter = new QFilter("modeltype", "=", (Object)"BaseFormModel");
            lsp.getListFilterParameter().setFilter(filter);
            lsp.setFormId("bos_listf7");
        }
    }
}
