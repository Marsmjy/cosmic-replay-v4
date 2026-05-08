/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.IBillView
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.BillEntityType
 *  kd.bos.entity.DifferentialControlInfo
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.exception.BosErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.exception.KDException
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.SplitContainer
 *  kd.bos.form.control.SplitDirection
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.AbstractElement
 *  kd.bos.metadata.dao.MetaCategory
 *  kd.bos.metadata.dao.MetadataDao
 *  kd.bos.metadata.form.FormMetadata
 *  kd.bos.metadata.form.container.FieldsetPanelAp
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.BillTypeDifferentialHelper
 *  kd.bos.servicehelper.QueryServiceHelper
 *  kd.bos.servicehelper.org.OrgUnitServiceHelper
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.bos.servicehelper.user.UserServiceHelper
 *  kd.bos.workflow.component.ApprovalRecord
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 */
package kd.hros.hrom.formplugin.route.apply;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.IBillView;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.BillEntityType;
import kd.bos.entity.DifferentialControlInfo;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.exception.BosErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.exception.KDException;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.control.Control;
import kd.bos.form.control.SplitContainer;
import kd.bos.form.control.SplitDirection;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.AbstractElement;
import kd.bos.metadata.dao.MetaCategory;
import kd.bos.metadata.dao.MetadataDao;
import kd.bos.metadata.form.FormMetadata;
import kd.bos.metadata.form.container.FieldsetPanelAp;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BillTypeDifferentialHelper;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.org.OrgUnitServiceHelper;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;
import kd.bos.workflow.component.ApprovalRecord;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

public final class ApplyBaseFormPlugin
extends AbstractFormPlugin
implements BeforeF7SelectListener {
    private static final Log LOG = LogFactory.getLog(ApplyBaseFormPlugin.class);

    public void registerListener(EventObject event) {
        super.registerListener(event);
        this.addClickListeners(new String[]{"changeapplier"});
        BasedataEdit deptEdit = (BasedataEdit)this.getView().getControl("dept");
        deptEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit applicantEdit = (BasedataEdit)this.getView().getControl("applicant");
        applicantEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void preOpenForm(PreOpenFormEventArgs args) {
        super.preOpenForm(args);
        if (!(args.getSource() instanceof BillShowParameter)) {
            throw new KDException(BosErrorCode.paramError, new Object[]{"Event source is not of type BillShowParameter"});
        }
        BillShowParameter billShowParameter = (BillShowParameter)args.getSource();
        if (args.getFormShowParameter() == null) {
            throw new KDException(BosErrorCode.paramError, new Object[]{"billShowParameter is null"});
        }
        try {
            if (OperationStatus.ADDNEW.equals((Object)args.getFormShowParameter().getStatus())) {
                args.getFormShowParameter().setCaption(ResManager.loadKDString((String)"\u65b0\u589eHR\u670d\u52a1\u7533\u8bf7", (String)"ApplyBasePlugin_1", (String)"hros-hrom-formplugin", (Object[])new Object[0]));
            } else {
                Object pkId = billShowParameter.getPkId();
                if (pkId == null) {
                    LOG.info("pkId cannot be null.");
                    return;
                }
                HRBaseServiceHelper helper = new HRBaseServiceHelper("hrom_applybill");
                DynamicObject dynamicObject = helper.loadSingle(pkId);
                if (dynamicObject == null) {
                    throw new KDException(BosErrorCode.paramError, new Object[]{"DynamicObject could not be loaded for the given pkId."});
                }
                String billNo = dynamicObject.getString("billno");
                args.getFormShowParameter().setCaption(ResManager.loadKDString((String)"HR\u670d\u52a1\u7533\u8bf7-%s", (String)"ApplyBasePlugin_2", (String)"hros-hrom-formplugin", (Object[])new Object[]{billNo}));
            }
        }
        catch (Exception exception) {
            LOG.error("Error occurred in preOpenForm ", (Throwable)exception);
            throw new KDBizException((Throwable)exception, BosErrorCode.dataSource, new Object[]{exception.getMessage()});
        }
    }

    public void click(EventObject evt) {
        super.click(evt);
        Control control = (Control)evt.getSource();
        String fieldKey = control.getKey();
        if ("changeapplier".equals(fieldKey)) {
            ApplyBaseFormPlugin.clickSwitchButton(evt, this.getModel(), this.getView(), (IFormPlugin)this, "changeapplier");
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        ApplyBaseFormPlugin.switchButtonclosedCallBack(closedCallBackEvent, this.getModel(), "dept", "applierpositionv");
    }

    public static void clickSwitchButton(EventObject evt, IDataModel model, IFormView view, IFormPlugin iFormPlugin, String labelName) {
        Control source = (Control)evt.getSource();
        String key = source.getKey();
        if (labelName.equals(key)) {
            ListShowParameter para = ShowFormHelper.createShowListForm((String)"bos_user", (boolean)false, (int)1, (boolean)true);
            ListFilterParameter listFilterParameter = new ListFilterParameter();
            Object userId = ((DynamicObject)model.getValue("creator")).getPkValue();
            QFilter qfilter = new QFilter("id", "=", userId);
            listFilterParameter.getQFilters().add(qfilter);
            para.getOpenStyle().setShowType(ShowType.Modal);
            para.setListFilterParameter(listFilterParameter);
            para.setCustomParam("ismergerows", (Object)Boolean.FALSE);
            para.setCustomParam("isIncludeAllSub", (Object)Boolean.TRUE);
            para.setCloseCallBack(new CloseCallBack(iFormPlugin, "changeapplierclose"));
            view.showForm((FormShowParameter)para);
        }
    }

    public void afterBindData(EventObject event) {
        super.afterBindData(event);
        ApprovalRecord approvalRecord = (ApprovalRecord)this.getControl("approvalrecordap");
        if ("hstc".equals(this.getView().getFormShowParameter().getCustomParam("showway")) || null == approvalRecord || approvalRecord.getArData().size() == 0) {
            this.hideAutoRecordAp();
        }
        String formId = MetadataDao.getIdByNumber((String)this.getView().getEntityId(), (MetaCategory)MetaCategory.Form);
        FormMetadata formMeta = (FormMetadata)MetadataDao.readRuntimeMeta((String)formId, (MetaCategory)MetaCategory.Form);
        List fieldSetPanelApList = formMeta.getItems().stream().filter(ap -> ap instanceof FieldsetPanelAp).map(ap -> (FieldsetPanelAp)ap).collect(Collectors.toList());
        HashMap fieldSetPanelApMap = new HashMap(16);
        for (FieldsetPanelAp fieldSetPanelAp : fieldSetPanelApList) {
            List items = fieldSetPanelAp.getItems();
            String fieldSetPanelApKey = fieldSetPanelAp.getKey();
            List keyList = items.stream().map(AbstractElement::getKey).collect(Collectors.toList());
            fieldSetPanelApMap.put(fieldSetPanelApKey, keyList);
        }
        BillEntityType dt = (BillEntityType)this.getModel().getDataEntityType();
        String billTypeKey = dt.getBillType();
        if (StringUtils.isNotBlank((CharSequence)billTypeKey)) {
            DynamicObject billTypeObj = (DynamicObject)this.getModel().getValue(billTypeKey);
            if (billTypeObj == null) {
                LOG.error("billTypeObj is null");
                return;
            }
            LOG.info("billTypeObj is {}", billTypeObj.get("number"));
            long billTypeId = (Long)billTypeObj.getPkValue();
            List diffInfos = BillTypeDifferentialHelper.getRuntimeDiffControlInfos((String)this.getView().getEntityId(), (Long)billTypeId);
            Set fieldSet = diffInfos.stream().filter(info -> info.isVisible(this.getView().getFormShowParameter().getStatus().name()) != null && info.isVisible(this.getView().getFormShowParameter().getStatus().name()) == false).map(DifferentialControlInfo::getFieldKey).collect(Collectors.toSet());
            for (Map.Entry entry : fieldSetPanelApMap.entrySet()) {
                List valueList = (List)entry.getValue();
                List visibleFieldList = valueList.stream().filter(field -> !fieldSet.contains(field)).collect(Collectors.toList());
                this.getView().setVisible(Boolean.valueOf(visibleFieldList.size() != 0), new String[]{(String)entry.getKey()});
            }
        }
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        boolean hasPermission = PermissionServiceHelper.checkPermission((Long)RequestContext.get().getCurrUserId(), (String)formShowParameter.getAppId(), (String)formShowParameter.getFormId(), (String)"4715a0df000000ac");
        if (formShowParameter.getStatus() == OperationStatus.EDIT && !hasPermission) {
            this.getView().setStatus(OperationStatus.VIEW);
        }
    }

    private void hideAutoRecordAp() {
        SplitContainer splitContainer = (SplitContainer)this.getView().getControl("splitcontainerap");
        splitContainer.hidePanel(SplitDirection.right, true);
    }

    public void beforeBindData(EventObject event) {
        super.beforeBindData(event);
        this.initBaseInfo();
        this.getModel().setDataChanged(false);
    }

    public void afterCreateNewData(EventObject eventObject) {
        super.afterCreateNewData(eventObject);
        this.initBaseInfo();
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        IDataModel model = this.getModel();
        IBillView view = (IBillView)this.getView();
        if ("applicant".equals(args.getProperty().getName())) {
            if (model.getDataEntity().getDynamicObject("applicant") == null) {
                LOG.info("applicant is null");
                this.getView().setEnable(Boolean.valueOf(false), new String[]{"dept"});
                model.setValue("dept", null);
                return;
            }
            this.getView().setEnable(Boolean.valueOf(true), new String[]{"dept"});
            this.getView().updateView("dept");
            long applicantId = model.getDataEntity().getDynamicObject("applicant").getLong("id");
            long deptId = UserServiceHelper.getUserMainOrgId((long)applicantId);
            if (deptId <= 0L) {
                view.showErrorNotification(ResManager.loadKDString((String)"\u63d0\u5355\u4eba\u9700\u8981\u4e3b\u804c\u4f4d\u624d\u80fd\u63d0\u5355\uff0c\u8bf7\u5230\u201c\u7cfb\u7edf\u670d\u52a1\u4e91>\u57fa\u7840\u670d\u52a1>\u4eba\u5458\u201d\u5904\u8bbe\u7f6e\u3002", (String)"ApplyBasePlugin_3", (String)"hros-hrom-formplugin", (Object[])new Object[0]));
                return;
            }
            model.setValue("dept", (Object)deptId);
            Long companyId = ApplyBaseFormPlugin.initCompanyByDept(deptId);
            model.setValue("company", (Object)companyId);
            model.setValue("applierpositionv", (Object)UserServiceHelper.getUserMainJob((long)applicantId));
            String selectFields = "id,entryentity.post post";
            QFilter idFilter = new QFilter("id", "=", (Object)applicantId);
            QFilter mainFilter = new QFilter("entryentity.ispartjob", "=", (Object)Boolean.FALSE);
            QFilter[] filters = new QFilter[]{idFilter, mainFilter};
            DynamicObjectCollection userCol = QueryServiceHelper.query((String)"bos_user", (String)selectFields, (QFilter[])filters);
            if (userCol == null || userCol.isEmpty() || userCol.get(0) == null) {
                return;
            }
            Long post = ((DynamicObject)userCol.get(0)).getLong("post");
            model.setValue("positionhr", (Object)post);
        }
        if ("dept".equals(args.getProperty().getName())) {
            if (model.getDataEntity().getDynamicObject("applicant") == null) {
                LOG.warn("applicant is null");
                return;
            }
            Long applicantId = model.getDataEntity().getDynamicObject("applicant").getLong("id");
            DynamicObject dept = (DynamicObject)args.getChangeSet()[0].getNewValue();
            if (dept == null) {
                LOG.warn("dept is null");
                dept = (DynamicObject)args.getChangeSet()[0].getOldValue();
            }
            this.setDeptId(model, applicantId, dept.getLong("id"));
        }
    }

    private void initBaseInfo() {
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        if ("hrom_applybill_emp".equals(this.getView().getEntityId()) && model.getValue("applicant") == null) {
            DynamicObject creator = (DynamicObject)model.getValue("creator");
            Long creatorId = (Long)creator.getPkValue();
            model.setValue("applicant", (Object)creatorId);
            long deptId = UserServiceHelper.getUserMainOrgId((long)creatorId);
            if (deptId <= 0L) {
                view.showErrorNotification(ResManager.loadKDString((String)"\u63d0\u5355\u4eba\u9700\u8981\u4e3b\u804c\u4f4d\u624d\u80fd\u63d0\u5355\uff0c\u8bf7\u5230\u201c\u7cfb\u7edf\u670d\u52a1\u4e91>\u57fa\u7840\u670d\u52a1>\u4eba\u5458\u201d\u5904\u8bbe\u7f6e\u3002", (String)"ApplyBasePlugin_3", (String)"hros-hrom-formplugin", (Object[])new Object[0]));
                return;
            }
            model.setValue("dept", (Object)deptId);
            this.setBindDataModelValue(model, creatorId);
        }
        if (model.getDataEntity().getDynamicObject("applicant") != null) {
            Long applicantId = model.getDataEntity().getDynamicObject("applicant").getLong("id");
            this.setBindDataModelValue(model, applicantId);
        } else {
            this.getView().setEnable(Boolean.valueOf(false), new String[]{"dept"});
        }
    }

    private void setBindDataModelValue(IDataModel model, Long creatorId) {
        DynamicObject deptdy = (DynamicObject)model.getValue("dept");
        if (deptdy == null) {
            LOG.warn("dept is null");
            return;
        }
        Long deptId = deptdy.getLong("id");
        this.setDeptId(model, creatorId, deptId);
    }

    private void setDeptId(IDataModel model, Long creatorId, Long deptId) {
        ArrayList<Long> userIds = new ArrayList<Long>(1);
        userIds.add(creatorId);
        List users = UserServiceHelper.getPosition(userIds);
        Map user = (Map)users.get(0);
        ArrayList entryentity = (ArrayList)user.get("entryentity");
        LOG.info("deptId {}", (Object)deptId);
        if (entryentity != null && entryentity.size() > 0) {
            Iterator iterator = entryentity.iterator();
            while (iterator.hasNext()) {
                HashMap stringObjectHashMap;
                HashMap next = stringObjectHashMap = (HashMap)iterator.next();
                Long dptId = ((DynamicObject)next.get("dpt")).getLong("id");
                LOG.info("dptId {}", (Object)dptId);
                if (!dptId.equals(deptId)) continue;
                Long companyId = ApplyBaseFormPlugin.initCompanyByDept(deptId);
                model.setValue("company", (Object)companyId);
                ILocaleString position = (ILocaleString)next.get("position");
                DynamicObject post = (DynamicObject)next.get("post");
                model.setValue("applierpositionv", (Object)position);
                model.setValue("dept", (Object)deptId);
                model.setValue("positionhr", (Object)post);
            }
        }
    }

    public static void switchButtonclosedCallBack(ClosedCallBackEvent event, IDataModel model, String deptField, String positionField) {
        if ("changeapplierclose".equals(event.getActionId()) && null != event.getReturnData()) {
            ListSelectedRowCollection returnData = (ListSelectedRowCollection)event.getReturnData();
            if (returnData.size() == 0) {
                LOG.warn("returnData.size 0");
                return;
            }
            ListSelectedRow selectedRow = returnData.get(0);
            Long entryPkValue = (Long)selectedRow.getEntryPrimaryKeyValue();
            Long userId = (Long)selectedRow.getPrimaryKeyValue();
            ArrayList<Long> userIds = new ArrayList<Long>(1);
            userIds.add(userId);
            List users = UserServiceHelper.getPosition(userIds);
            Map user = (Map)users.get(0);
            ArrayList entryEntity = (ArrayList)user.get("entryentity");
            if (entryEntity == null || entryEntity.size() == 0) {
                LOG.warn("entryEntity is null or size is 0");
                return;
            }
            for (HashMap stringObjectHashMap : entryEntity) {
                HashMap next = stringObjectHashMap;
                if (!entryPkValue.equals(next.get("id"))) continue;
                ILocaleString position = (ILocaleString)next.get("position");
                Long deptId = ((DynamicObject)next.get("dpt")).getLong("id");
                if (deptId == 0L) continue;
                model.setValue(deptField, (Object)deptId);
                model.setValue(positionField, (Object)position);
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        IDataEntityProperty property = beforeF7SelectEvent.getProperty();
        String name = property.getName();
        if ("dept".equals(name)) {
            DynamicObject applicant = this.getModel().getDataEntity().getDynamicObject("applicant");
            if (applicant == null) {
                LOG.warn("applicant is null");
                return;
            }
            Long applicantId = applicant.getLong("id");
            List users = UserServiceHelper.getPosition(Collections.singletonList(applicantId));
            Map user = (Map)users.get(0);
            ArrayList entryEntity = (ArrayList)user.get("entryentity");
            List deptId = entryEntity.stream().map(next -> ((DynamicObject)next.get("dpt")).getLong("id")).collect(Collectors.toList());
            QFilter qFilter = new QFilter("id", "in", deptId);
            beforeF7SelectEvent.setCustomQFilters(Collections.singletonList(qFilter));
        }
    }

    public static Long initCompanyByDept(Long deptId) {
        Map companyInfoMap = OrgUnitServiceHelper.getCompanyfromOrg((Long)deptId);
        Long companyId = companyInfoMap != null ? (Long)companyInfoMap.get("id") : null;
        LOG.info("initCompanyByDept,dept:{},companyId2:{}", (Object)deptId, (Object)companyId);
        return companyId;
    }
}
