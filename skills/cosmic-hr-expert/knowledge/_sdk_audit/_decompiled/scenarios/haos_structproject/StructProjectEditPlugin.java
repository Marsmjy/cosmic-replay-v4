/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.Tips
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.TipsSupport
 *  kd.bos.form.control.Control
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.TextEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.MutexHelper
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.portal.util.SerializationUtils
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.hr.haos.business.application.service.IStructProjectApplication
 *  kd.hr.haos.business.application.service.impl.StructProjectApplicationImpl
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdOrgRepository
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgCodeRuleServiceHelper
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgStructRepository
 *  kd.hr.haos.business.infrastructure.client.perm.HRCSRPCServiceHelper
 *  kd.hr.haos.business.util.OrgPermHelper
 *  kd.hr.haos.common.constants.structproject.StructProjectConstants
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.haos.formplugin.web.structures;

import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.Tips;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.TipsSupport;
import kd.bos.form.control.Control;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.TextEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.MutexHelper;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.portal.util.SerializationUtils;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.haos.business.application.service.IStructProjectApplication;
import kd.hr.haos.business.application.service.impl.StructProjectApplicationImpl;
import kd.hr.haos.business.domain.adminorg.service.impl.AdOrgRepository;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgCodeRuleServiceHelper;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgStructRepository;
import kd.hr.haos.business.infrastructure.client.perm.HRCSRPCServiceHelper;
import kd.hr.haos.business.util.OrgPermHelper;
import kd.hr.haos.common.constants.structproject.StructProjectConstants;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import org.apache.commons.lang3.StringUtils;

public final class StructProjectEditPlugin
extends HRDataBaseEdit
implements BeforeF7SelectListener,
StructProjectConstants {
    private static final Log LOG = LogFactory.getLog(StructProjectEditPlugin.class);
    private static final String ORGFIELD = "org";
    private static final String ROOTORG = "rootorg";
    private static final String MAINTAINFRAMEWORK = "maintain_struct";
    private static final String SAVE = "save";
    private static final String ENABLE = "enable";
    private static final String DISABLE = "disable";
    private static final String ROOT_TYPE = "roottype";
    private static final String IS_INCLUDE_VIRTUAL_ORG = "isincludevirtualorg";
    private static final String ROOT_EFF_DT = "rooteffdt";
    private static final String ORGORG = "orgorg";
    private IStructProjectApplication structProjectApplication = new StructProjectApplicationImpl();

    public void afterCreateNewData(EventObject e) {
        String orgId = (String)this.getView().getFormShowParameter().getCustomParam("orgId");
        if (!StringUtils.isEmpty((CharSequence)orgId)) {
            this.getModel().setValue(ORGFIELD, (Object)Long.parseLong(orgId));
        } else {
            HasPermOrgResult hrPermOrg = OrgPermHelper.getHRPermOrg((boolean)true);
            List hasPermOrgs = hrPermOrg.getHasPermOrgs();
            if (!Objects.isNull(hasPermOrgs) && hasPermOrgs.size() != 0) {
                if (hasPermOrgs.contains(RequestContext.get().getOrgId())) {
                    this.getModel().setValue(ORGFIELD, (Object)RequestContext.get().getOrgId());
                } else {
                    this.getModel().setValue(ORGFIELD, hasPermOrgs.get(0));
                }
            } else {
                LOG.error("StructProjectEditPlugin can not get any org");
            }
        }
    }

    public void afterBindData(EventObject e) {
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (OperationStatus.EDIT.equals((Object)status)) {
            Boolean rootOrgMaintain = this.structProjectApplication.isRootOrgMaintain(this.getModel().getDataEntity());
            this.getView().setEnable(Boolean.valueOf(rootOrgMaintain == false), new String[]{ROOTORG, ROOT_TYPE});
            this.getView().setEnable(Boolean.valueOf("10".equals(this.getModel().getDataEntity().getString(ENABLE))), new String[]{ROOT_EFF_DT});
            if ("2".equals(this.getModel().getDataEntity().getString(ROOT_TYPE))) {
                this.showVirtualRootInfo();
            }
            Long structId = (Long)this.getModel().getValue("id");
            DynamicObject[] dynamicObjects = AdminOrgStructRepository.getInstance().queryOriVirtualOrgByStructProId("id", structId);
            if (dynamicObjects != null && dynamicObjects.length > 0) {
                this.getView().setEnable(Boolean.FALSE, new String[]{IS_INCLUDE_VIRTUAL_ORG});
            }
            this.getView().setVisible(Boolean.valueOf(false), new String[]{ORGORG});
        } else if (OperationStatus.ADDNEW.equals((Object)status)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"rootnumber", "rootname", ROOT_EFF_DT, "rootdescription", ORGORG});
            this.setMustInput(false, "rootnumber", "rootname", ROOT_EFF_DT);
            this.setMustInput(true, ROOTORG);
        } else if (OperationStatus.VIEW.equals((Object)status)) {
            this.showVisibleOrDisVisible();
        }
        this.setTips("haos_structproject", ROOT_TYPE, ROOTORG, ROOT_EFF_DT, IS_INCLUDE_VIRTUAL_ORG, "effdt", "issyncorg");
    }

    private void showVirtualRootInfo() {
        long adminOrgId = this.getModel().getDataEntity().getLong("rootorg.id");
        DynamicObject rootOrgInfo = AdOrgRepository.getInstance().queryByPk("number,name,org,establishmentdate,description", (Object)adminOrgId);
        if (rootOrgInfo != null) {
            this.getModel().setValue("rootnumber", (Object)rootOrgInfo.getString("number"));
            this.getModel().setValue("rootname", rootOrgInfo.get("name"));
            this.getModel().setValue(ROOT_EFF_DT, (Object)rootOrgInfo.getDate("establishmentdate"));
            this.getModel().setValue("rootdescription", rootOrgInfo.get("description"));
        }
    }

    private void showVisibleOrDisVisible() {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        boolean isIncludeVirtualOrg = dataEntity.getBoolean(IS_INCLUDE_VIRTUAL_ORG);
        if (!isIncludeVirtualOrg) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{ROOT_TYPE, "rootnumber", "rootname", ROOT_EFF_DT, "rootdescription"});
            this.getView().setVisible(Boolean.valueOf(true), new String[]{ROOTORG});
            return;
        }
        String rootType = dataEntity.getString(ROOT_TYPE);
        if ("1".equals(rootType)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"rootnumber", "rootname", ROOT_EFF_DT, "rootdescription"});
            this.getView().setVisible(Boolean.valueOf(true), new String[]{ROOT_TYPE, ROOTORG});
            return;
        }
        this.getView().setVisible(Boolean.valueOf(true), new String[]{ROOT_TYPE, "rootnumber", "rootname", ROOT_EFF_DT, "rootdescription"});
        this.getView().setVisible(Boolean.valueOf(false), new String[]{ROOTORG});
        this.showVirtualRootInfo();
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        BasedataEdit areaReg = (BasedataEdit)this.getControl(ORGFIELD);
        areaReg.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit orgorg = (BasedataEdit)this.getControl(ORGORG);
        orgorg.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void preOpenForm(PreOpenFormEventArgs e) {
        String openType = (String)e.getFormShowParameter().getCustomParams().get("opentype");
        if (!e.getFormShowParameter().getStatus().equals((Object)OperationStatus.ADDNEW) && !HRStringUtils.equals((String)openType, (String)"1")) {
            e.getFormShowParameter().setStatus(OperationStatus.VIEW);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        HasPermOrgResult hrPermOrg;
        String f7Name = beforeF7SelectEvent.getProperty().getName();
        if ((ORGFIELD.equals(f7Name) || ORGORG.equals(f7Name)) && !(hrPermOrg = OrgPermHelper.getHRPermOrg((boolean)false)).hasAllOrgPerm()) {
            QFilter qFilter = new QFilter("id", "in", (Object)hrPermOrg.getHasPermOrgs());
            beforeF7SelectEvent.addCustomQFilter(qFilter);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String modifyClockStr;
        AbstractOperate operate = (AbstractOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        String roottype = (String)this.getModel().getValue(ROOT_TYPE);
        if (SAVE.equals(operateKey) && "2".equals(roottype)) {
            this.getModel().setValue(ROOT_EFF_DT, this.getModelVal("effdt"));
        } else if (HRStringUtils.equals((String)"modify", (String)operateKey) && ((modifyClockStr = this.getView().getPageCache().get("edit_struct_clock")) == null || HRStringUtils.equals((String)modifyClockStr, (String)"false"))) {
            boolean modifyClock = MutexHelper.require((IFormView)this.getView(), (String)"haos_structproject", (Object)this.getModel().getDataEntity().getLong("id"), (String)"edit_struct", (boolean)true, (StringBuilder)new StringBuilder());
            this.getView().getPageCache().put("edit_struct_clock", String.valueOf(modifyClock));
            if (!modifyClock) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5f53\u524d\u5355\u636e\u5df2\u5728\u5176\u4ed6\u9875\u7b7e\u4e2d\u6253\u5f00\uff0c\u5982\u9700\u7ee7\u7eed\u64cd\u4f5c\uff0c\u8bf7\u5173\u95ed\u5355\u636e\u540e\u91cd\u8bd5\uff0c\u6216\u91cd\u65b0\u767b\u5f55\u540e\uff0c\u518d\u6b21\u5c1d\u8bd5\u3002", (String)"OrgStructProjectPermTreeListPlugin_1", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs eventArgs) {
        super.afterDoOperation(eventArgs);
        String opKey = eventArgs.getOperateKey();
        if (eventArgs.getOperationResult() != null && eventArgs.getOperationResult().isSuccess()) {
            this.openOperationPage(opKey, eventArgs);
        }
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        IFormView parentView = this.getView().getParentView();
        if (parentView != null) {
            parentView.invokeOperation("refresh");
        }
    }

    public void propertyChanged(PropertyChangedArgs event) {
        Object newValue;
        String propertyName = event.getProperty().getName();
        if (ROOT_TYPE.equals(propertyName)) {
            newValue = event.getChangeSet()[0].getNewValue();
            this.getView().setVisible(Boolean.valueOf("2".equals(newValue)), new String[]{"rootnumber", "rootname", ROOT_EFF_DT, "rootdescription"});
            this.setMustInput("2".equals(newValue), "rootnumber", "rootname", ROOT_EFF_DT);
            this.getView().setVisible(Boolean.valueOf(!"2".equals(newValue)), new String[]{ROOTORG});
            this.getModel().setValue(ROOTORG, null);
            if ("2".equals(newValue)) {
                this.getView().setVisible(Boolean.valueOf(true), new String[]{ORGORG});
                this.setMustInput(false, ROOTORG);
                AdminOrgCodeRuleServiceHelper.create((IFormView)this.getView(), (IDataModel)this.getModel()).setOrgNumber(this.getHrDy(), "rootnumber");
            } else {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{ORGORG});
            }
        }
        if (IS_INCLUDE_VIRTUAL_ORG.equals(propertyName)) {
            newValue = event.getChangeSet()[0].getNewValue();
            this.setMustInput(Boolean.TRUE.equals(newValue), "rootnumber", "rootname", ROOT_EFF_DT);
            this.setMustInput(Boolean.FALSE.equals(newValue), ROOTORG);
            if (Boolean.TRUE.equals(newValue)) {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"rootnumber", "rootname", ROOT_EFF_DT, "rootdescription"});
                this.getView().setVisible(Boolean.valueOf(true), new String[]{ROOT_TYPE, ROOTORG});
                String rootType = this.getModel().getDataEntity().getString(ROOT_TYPE);
                this.getView().setVisible(Boolean.valueOf("2".equals(rootType)), new String[]{"rootnumber", "rootname", ROOT_EFF_DT, "rootdescription"});
                this.getView().setVisible(Boolean.valueOf(!"2".equals(rootType)), new String[]{ROOTORG});
                if ("2".equals(rootType)) {
                    this.getModel().setValue(ROOTORG, null);
                } else {
                    this.setMustInput(true, ROOTORG);
                }
            } else {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{ROOT_TYPE, "rootnumber", "rootname", ROOT_EFF_DT, "rootdescription"});
                this.getView().setVisible(Boolean.valueOf(true), new String[]{ROOTORG});
                this.getModel().setValue(ROOT_TYPE, (Object)"1");
                this.getModel().setValue(ROOTORG, null);
            }
        }
    }

    private void openOperationPage(String opKey, AfterDoOperationEventArgs eventArgs) {
        switch (opKey) {
            case "maintain_struct": {
                this.getView().getFormShowParameter().setStatus(OperationStatus.VIEW);
                this.showMaintainFrameworkForm();
                break;
            }
            case "save": {
                MutexHelper.release((String)"haos_structproject", (String)"edit_struct", (String)String.valueOf(this.getModel().getDataEntity().getLong("id")));
                this.getPageCache().put("MUTEX_ENTITY_KEY", (String)null);
                this.getPageCache().put("MUTEX_OPER_KEY", (String)null);
                this.getPageCache().put("MUTEX_OBJ_ID", (String)null);
                this.getView().getPageCache().remove("edit_struct_clock");
            }
            case "enable": 
            case "disable": {
                this.getView().getFormShowParameter().setStatus(OperationStatus.VIEW);
                break;
            }
            case "modify": {
                DynamicObject dataEntity = this.getModel().getDataEntity();
                if (!"2".equals(dataEntity.getString(ROOT_TYPE))) break;
                this.getView().setEnable(Boolean.valueOf(false), new String[]{IS_INCLUDE_VIRTUAL_ORG});
            }
        }
    }

    private void showMaintainFrameworkForm() {
        ListShowParameter listShowParameter = new ListShowParameter();
        long structProjectId = this.getModel().getDataEntity().getLong("id");
        String mainPageId = "";
        if (this.getView().getMainView() != null) {
            mainPageId = this.getView().getMainView().getPageId();
        }
        listShowParameter.setPageId("haos_orgstructlist_" + structProjectId + "_" + mainPageId);
        listShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        listShowParameter.setFormId("haos_orgstructlist");
        listShowParameter.setBillFormId("haos_structorgdetail");
        listShowParameter.setCustomParam("custom_parent_f7_prop", (Object)"boid");
        listShowParameter.setCustomParam("struct_project_ids", (Object)SerializationUtils.toJsonString(Collections.singletonList(structProjectId)));
        DynamicObject rootOrgDyn = (DynamicObject)this.getModel().getValue(ROOTORG);
        if (rootOrgDyn != null) {
            listShowParameter.setCustomParam(ROOTORG, (Object)rootOrgDyn.getString("id"));
        }
        listShowParameter.setCaption(this.getModel().getDataEntity().getString("name"));
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    private void setMustInput(boolean mustInput, String ... propertyNames) {
        for (String propertyName : propertyNames) {
            Control control = this.getView().getControl(propertyName);
            if (control instanceof BasedataEdit) {
                ((BasedataEdit)control).setMustInput(mustInput);
                continue;
            }
            if (control instanceof TextEdit) {
                ((TextEdit)control).setMustInput(mustInput);
                continue;
            }
            if (!(control instanceof ComboEdit)) continue;
            ((ComboEdit)control).setMustInput(mustInput);
        }
    }

    private void setTips(String pageName, String ... controlNames) {
        for (String controlName : controlNames) {
            Tips tips = new Tips();
            List tipList = HRCSRPCServiceHelper.queryPromptForString((IFormView)this.getView(), (String)pageName, (String)controlName);
            if (CollectionUtils.isEmpty((Collection)tipList)) continue;
            tips.setContent(new LocaleString((String)tipList.get(0)));
            tips.setType("text");
            tips.setTriggerType("hover");
            tips.setIsConfirm(false);
            tips.setShowIcon(true);
            TipsSupport control = (TipsSupport)this.getControl(controlName);
            if (control == null) continue;
            control.addTips(tips);
        }
    }

    private DynamicObject getHrDy() {
        DynamicObject adminOrgDetail = new DynamicObject((DynamicObjectType)MetadataServiceHelper.getDataEntityType((String)"haos_adminorgdetail"));
        adminOrgDetail.set("name", this.getModel().getValue("rootname"));
        adminOrgDetail.set("number", this.getModel().getValue("rootnumber"));
        adminOrgDetail.set(ORGFIELD, this.getModel().getValue(ORGFIELD));
        return adminOrgDetail;
    }
}
