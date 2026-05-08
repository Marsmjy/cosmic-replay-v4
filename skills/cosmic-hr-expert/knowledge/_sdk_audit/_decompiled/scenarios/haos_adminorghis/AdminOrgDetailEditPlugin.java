/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.container.Container
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper
 *  kd.hr.haos.business.domain.common.service.impl.BaseDataHelper
 *  kd.hr.haos.business.domain.org.async.AsyncEffectingReformHelper
 *  kd.hr.haos.business.domain.org.service.fullname.OrgFullNameServiceWrapper
 *  kd.hr.haos.business.util.AdminOrgSortUtils
 *  kd.hr.haos.business.util.OrgPermHelper
 *  kd.hr.haos.common.constants.AdminOrgDetailConstants
 *  kd.hr.haos.common.constants.masterdata.AdminOrgConstants
 *  kd.hr.haos.common.constants.structproject.StructProjectConstants
 *  kd.hr.haos.common.util.HaosOrgUnitServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.org.TreeTemplateHelper
 *  kd.hr.hbp.common.model.AuthorizedOrgResultWithSub
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRDyObjectPropUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.haos.formplugin.web.adminorg;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.BasedataProp;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.container.Container;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.api.HasPermOrgResult;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper;
import kd.hr.haos.business.domain.common.service.impl.BaseDataHelper;
import kd.hr.haos.business.domain.org.async.AsyncEffectingReformHelper;
import kd.hr.haos.business.domain.org.service.fullname.OrgFullNameServiceWrapper;
import kd.hr.haos.business.util.AdminOrgSortUtils;
import kd.hr.haos.business.util.OrgPermHelper;
import kd.hr.haos.common.constants.AdminOrgDetailConstants;
import kd.hr.haos.common.constants.masterdata.AdminOrgConstants;
import kd.hr.haos.common.constants.structproject.StructProjectConstants;
import kd.hr.haos.common.util.HaosOrgUnitServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.org.TreeTemplateHelper;
import kd.hr.hbp.common.model.AuthorizedOrgResultWithSub;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRDyObjectPropUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class AdminOrgDetailEditPlugin
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final Log LOG = LogFactory.getLog(AdminOrgDetailEditPlugin.class);
    private final String msg = ResManager.loadKDString((String)"\u5207\u7ec4\u7ec7\u4f53\u7cfb\u7ba1\u7406\u7ec4\u7ec7\u540e\uff0c\u5c06\u6e05\u9664\u4e0a\u7ea7\u884c\u653f\u7ec4\u7ec7\u4ee5\u53ca\u8be5\u7ec4\u7ec7\u4e0d\u53ef\u4f7f\u7528\u7684\u57fa\u7840\u8d44\u6599\u4fe1\u606f\uff0c\u662f\u5426\u786e\u8ba4\u5207\u6362\uff1f", (String)"AdminOrgDetailEditPlugin_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
    private final String coop_orgteam_caption = ResManager.loadKDString((String)"\u534f\u4f5c\u7ec4\u7ec7", (String)"AdminOrgDetailEditPlugin_3", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
    private final String parent_caption = ResManager.loadKDString((String)"\u4e0a\u7ea7\u884c\u653f\u7ec4\u7ec7", (String)"AdminOrgDetailEditPlugin_4", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
    private static final Set<String> BASE_ARR = Collections.unmodifiableSet(Sets.newConcurrentHashSet(Arrays.asList("changescene", "changereason", "adminorgtype", "adminorglayer", "adminorgfunction", "coopreltype", "corporateorg")));
    private static final Map<String, String> FIELD_ENTITY_MAP = Collections.unmodifiableMap(new /* Unavailable Anonymous Inner Class!! */);
    private static final String CONFIRM_CALL_BACK_KEY_CONTINUE_CLOSE = "continue_close";

    public void preOpenForm(PreOpenFormEventArgs args) {
        super.preOpenForm(args);
        FormShowParameter showParameter = args.getFormShowParameter();
        String operation = (String)showParameter.getCustomParam("adminorg_operation");
        if (operation != null) {
            showParameter.setCaption((String)showParameter.getCustomParam("caption"));
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"refresh")) {
            this.getView().getFormShowParameter().setCustomParam("adminorg_operation", null);
        }
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        String operation = (String)this.getView().getFormShowParameter().getCustomParam("adminorg_operation");
        if (operation != null) {
            DynamicObject dataEntity = this.getView().getModel().getDataEntity();
            if ("addnew".equals(operation) || "parentchg".equals(operation)) {
                DynamicObject parentOrg = dataEntity.getDynamicObject("parentorg");
                if (parentOrg != null) {
                    this.getView().returnDataToParent((Object)parentOrg.getLong("boid"));
                }
            } else if ("infochg".equals(operation)) {
                this.getView().returnDataToParent((Object)dataEntity.getLong("boid"));
            }
        }
    }

    public void beforeBindData(EventObject event) {
        super.beforeBindData(event);
        this.getView().getModel().setValue("effectdate", this.getModel().getValue("bsed"));
        String operation = (String)this.getView().getFormShowParameter().getCustomParam("adminorg_operation");
        if (operation == null) {
            this.setCaption();
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"new_save", "change_save", "parent_save", "newentry", "deleteentry", "tobedisableflag", "cancel", "parentorg_name"});
            this.getView().setVisible(Boolean.valueOf(true), new String[]{"fullname"});
            if (OperationStatus.VIEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"bar_close"});
            }
            return;
        }
        this.getView().getModel().setValue("changetype", this.getView().getFormShowParameter().getCustomParam("changetype.id"));
        this.getView().getPageCache().put("isAddNewFirst", Boolean.FALSE.toString());
        this.getView().setVisible(Boolean.valueOf(true), new String[]{"status_info", "tobedisableflag"});
        this.getView().setVisible(Boolean.valueOf(false), new String[]{"tobedisabledate", "disabledate"});
        this.getView().setEnable(Boolean.valueOf(false), new String[]{"tobedisableflag"});
        switch (operation) {
            case "infochg": {
                this.getView().setEnable(Boolean.valueOf(false), new String[]{"parentorg"});
                this.getView().setEnable(Boolean.valueOf(true), new String[]{"tobedisableflag"});
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"cancel", "fullname"});
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"bar_close"});
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"new_save", "parent_save", "enable", "effectdate", "parentorg_name"});
                break;
            }
            case "addnew": {
                Map customParamMap = this.getView().getFormShowParameter().getCustomParams();
                this.getView().getModel().setValue("parentorg", (Object)new AdminOrgDetailHelper().queryBoIdById(customParamMap.get("parentorg")));
                this.getModel().beginInit();
                this.getView().getModel().setValue("org", customParamMap.get("master_org"));
                this.getModel().endInit();
                String name = this.getModel().getDataEntity().getDynamicObjectType().getName();
                if ("haos_adminorgdetailfuture".equals(name)) {
                    this.getView().setVisible(Boolean.valueOf(true), new String[]{"cancel"});
                } else {
                    this.getView().setVisible(Boolean.valueOf(false), new String[]{"cancel"});
                }
                this.getPageCache().put("master_org", String.valueOf(customParamMap.get("master_org")));
                this.getView().getPageCache().put("isAddNewFirst", Boolean.TRUE.toString());
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"status_info", "tobedisableflag", "fullname"});
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"parentorg_name"});
                break;
            }
            case "parentchg": {
                this.getView().setEnable(Boolean.valueOf(true), new String[]{"tobedisableflag"});
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"cancel", "parentorg_name"});
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"bar_close", "fullname"});
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"new_save", "change_save", "enable", "effectdate"});
                break;
            }
            default: {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"new_save", "change_save", "parent_save"});
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"status_info", "tobedisableflag", "parentorg_name"});
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"fullname"});
            }
        }
        DynamicObject parentOrgDy = this.getModel().getDataEntity().getDynamicObject("parentorg");
        if (parentOrgDy != null) {
            long parentBoId = parentOrgDy.getLong("boid");
            Date modifyTime = this.getModel().getDataEntity().getDate("modifytime");
            OrgFullNameServiceWrapper wrapper = new OrgFullNameServiceWrapper();
            String orgLongName = wrapper.getOrgFullName(Long.valueOf(parentBoId), modifyTime);
            this.getModel().setValue("parentorg_name", (Object)orgLongName);
        }
    }

    private void setCaption() {
        String name = this.getModel().getDataEntity().getString("name");
        if (HRStringUtils.isNotEmpty((String)name)) {
            String caption = String.format(ResManager.loadKDString((String)"\u884c\u653f\u7ec4\u7ec7-%s", (String)"AdminOrgDetailEditPlugin_6", (String)"hrmp-haos-formplugin", (Object[])new Object[0]), name);
            this.getView().getFormShowParameter().setCaption(caption);
        }
    }

    public void afterBindData(EventObject event) {
        DynamicObject parentOrg;
        BasedataProp property;
        BasedataEdit edit;
        super.afterBindData(event);
        if (this.getModel().getValue("parentorg") != null) {
            edit = (BasedataEdit)this.getView().getControl("parentorg");
            edit.setMustInput(true);
            property = (BasedataProp)edit.getProperty();
            property.setMustInput(true);
        } else {
            edit = (BasedataEdit)this.getView().getControl("parentorg");
            edit.setMustInput(false);
            property = (BasedataProp)edit.getProperty();
            property.setMustInput(false);
        }
        this.getView().getPageCache().put("tobedisableflag", this.getModel().getDataEntity().getString("tobedisableflag"));
        Object tobeDisableDate = this.getModel().getValue("tobedisabledate");
        if (tobeDisableDate != null) {
            this.getView().getPageCache().put("tobedisabledate", HRDateTimeUtils.formatDate((Date)((Date)tobeDisableDate)));
        }
        this.getView().setVisible(Boolean.valueOf(true), new String[]{"changedescription"});
        Container container = (Container)this.getView().getControl("flexpanelrelateinfo");
        container.getItems().stream().forEach(item -> ((Container)item).getItems().forEach(innerItem -> {
            if (innerItem.getKey().equals("20301_s")) {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{item.getKey()});
            }
        }));
        String enable = this.getModel().getDataEntity().getString("enable");
        if (HRStringUtils.equals((String)enable, (String)"0")) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"info_chg", "parent_chg"});
        }
        if ((parentOrg = this.getModel().getDataEntity().getDynamicObject("parentorg")) == null) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"parent_chg"});
            ((BasedataProp)this.getView().getModel().getDataEntity().getDataEntityType().getProperties().get((Object)"parentorg")).setMustInput(false);
            this.getView().getModel().setValue("isroot", (Object)Boolean.TRUE);
            this.getView().getModel().setDataChanged(false);
        }
    }

    public void afterLoadData(EventObject event) {
        super.afterLoadData(event);
    }

    public void registerListener(EventObject event) {
        super.registerListener(event);
        BasedataEdit parent = (BasedataEdit)this.getView().getControl("parentorg");
        parent.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit city = (BasedataEdit)this.getView().getControl("city");
        city.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit changeScene = (BasedataEdit)this.getView().getControl("changescene");
        changeScene.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit changeReason = (BasedataEdit)this.getView().getControl("changereason");
        changeReason.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit coopOrgTream = (BasedataEdit)this.getView().getControl("cooporgteam");
        coopOrgTream.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit coopType = (BasedataEdit)this.getView().getControl("coopreltype");
        coopType.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit corporateOrg = (BasedataEdit)this.getView().getControl("corporateorg");
        corporateOrg.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        this.addItemClickListeners(new String[]{"change_save"});
        BasedataEdit structParentOrg = (BasedataEdit)this.getView().getControl("struct_parent_org");
        structParentOrg.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit adminOrgType = (BasedataEdit)this.getView().getControl("adminorgtype");
        adminOrgType.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeF7Select(BeforeF7SelectEvent event) {
        String fieldKey;
        switch (fieldKey = event.getProperty().getName()) {
            case "changereason": {
                this.setChangeReasonF7Filter(event);
                break;
            }
            case "changescene": {
                this.setChangeSeceneF7Filter(event);
                break;
            }
            case "city": {
                this.setCityF7Filter(event);
                break;
            }
            case "cooporgteam": {
                this.setCoopOrgTeamFilter(event);
                break;
            }
            case "coopreltype": {
                this.setCoopTypeFilter(event);
                break;
            }
            case "parentorg": {
                this.setParentFilter(event);
                break;
            }
            case "corporateorg": {
                event.getCustomQFilters().add(new QFilter("oprsts", "=", (Object)"1"));
                break;
            }
            case "struct_parent_org": {
                AdminOrgDetailHelper.showStructParentOrgF7((BeforeF7SelectEvent)event, (IFormView)this.getView());
                break;
            }
            case "adminorgtype": {
                this.setAdminOrgTypeFilter(event);
                break;
            }
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        super.beforeItemClick(evt);
        String operationKey = evt.getOperationKey();
        if ("confirmchange".equals(operationKey) || "confirmchangenoaudit".equals(operationKey)) {
            DynamicObject dataEntity = this.getView().getModel().getDataEntity();
            boolean openConfirm = AsyncEffectingReformHelper.showConfirm((String)operationKey, (IFormView)this.getView(), (IDataModel)this.getModel(), (IFormPlugin)this, (DynamicObject)dataEntity);
            if (openConfirm) {
                evt.setCancel(true);
            } else {
                this.getView().getPageCache().put("skipChangeTips", "true");
            }
        }
    }

    private void setParentFilter(BeforeF7SelectEvent event) {
        QFilter statusFilter = new QFilter("status", "=", (Object)"C");
        QFilter classifyFilter = new QFilter("otclassify", "=", (Object)AdminOrgDetailConstants.ADMINORG_CLASSIFY_ID);
        event.getCustomQFilters().add(statusFilter);
        event.getCustomQFilters().add(classifyFilter);
        HasPermOrgResult hrPermOrg = OrgPermHelper.getHRPermOrg();
        if (!hrPermOrg.hasAllOrgPerm()) {
            QFilter baseDataFilter = BaseDataHelper.getAdminOrgBaseDataFilter((String)"haos_adminorgdetail", (List)hrPermOrg.getHasPermOrgs());
            event.getCustomQFilters().add(baseDataFilter);
        }
        Long boId = (Long)this.getModel().getValue("boid");
        ListShowParameter listShowParameter = (ListShowParameter)event.getFormShowParameter();
        if (boId != null && boId > 0L) {
            event.getCustomQFilters().add(new QFilter("boid", "!=", (Object)boId));
            listShowParameter.getTreeFilterParameter().getQFilters().add(new QFilter("adminorg.id", "!=", (Object)boId));
        }
        event.getCustomQFilters().add(new QFilter("isvirtualorg", "=", (Object)"0"));
        listShowParameter.setCaption(this.parent_caption);
        Date now = HRDateTimeUtils.getNowDate();
        Date bsed = HRDateTimeUtils.truncateDate((Date)this.getModel().getDataEntity().getDate("bsed"));
        if (bsed != null && HRDateTimeUtils.dayBefore((Date)bsed, (Date)now)) {
            listShowParameter.setCustomParam("effectdate", (Object)now);
        }
    }

    private void setCoopTypeFilter(BeforeF7SelectEvent event) {
        QFilter coopreltypQf = new QFilter("orgteamtype.fbasedataid", "in", (Object)1010);
        ArrayList<Long> typeIds = new ArrayList<Long>();
        typeIds.add(1010L);
        event.getCustomQFilters().add(coopreltypQf);
        DynamicObjectCollection dynamicObjects = this.getModel().getDataEntity(true).getDynamicObjectCollection("cooprelentryentity");
        if (!CollectionUtils.isEmpty((Collection)dynamicObjects)) {
            dynamicObjects.forEach(dynamicObject -> {
                if (dynamicObject != null && dynamicObject.getDynamicObject("coopreltype") != null) {
                    typeIds.add(dynamicObject.getLong("coopreltype_id"));
                }
            });
        }
        event.getCustomQFilters().add(new QFilter("id", "not in", typeIds));
    }

    private void setCoopOrgTeamFilter(BeforeF7SelectEvent event) {
        Object org = this.getView().getModel().getValue("org");
        if (Objects.isNull(org)) {
            return;
        }
        QFilter baseFilter = new QFilter("iscurrentversion", "=", (Object)"1");
        baseFilter.and(new QFilter("enable", "=", (Object)"1"));
        long boId = this.getModel().getDataEntity().getLong("boid");
        if (boId != 0L) {
            baseFilter.and(new QFilter("boid", "!=", (Object)boId));
        }
        event.getCustomQFilters().add(baseFilter);
        boolean chooseAllOrg = new AdminOrgDetailHelper().getOrgParameter(Long.valueOf(((DynamicObject)org).getLong("id")));
        if (!chooseAllOrg) {
            AuthorizedOrgResultWithSub orgAuth = AdminOrgDetailHelper.getOrgAuthWithSub((IFormView)this.getView());
            if (!orgAuth.isHasAllOrgPerm()) {
                QFilter dataPermFilter = TreeTemplateHelper.getPermStructLongNumberFilter((AuthorizedOrgResultWithSub)orgAuth, (String)"boid");
                event.getCustomQFilters().add(dataPermFilter);
            }
            event.getFormShowParameter().getCustomParams().put("perm_org_result_withsub", SerializationUtils.toJsonString((Object)orgAuth));
            event.getFormShowParameter().setCustomParam("chooseallorg", (Object)chooseAllOrg);
        }
        event.getFormShowParameter().setCaption(this.coop_orgteam_caption);
        event.getFormShowParameter().setCustomParam("isShowDisableData", (Object)false);
    }

    private void setCityF7Filter(BeforeF7SelectEvent event) {
        DynamicObject area = this.getModel().getDataEntity().getDynamicObject("companyarea");
        if (area == null) {
            return;
        }
        QFilter filter = new QFilter("country.id", "=", (Object)area.getLong("id"));
        event.getCustomQFilters().add(filter);
    }

    private void setChangeSeceneF7Filter(BeforeF7SelectEvent event) {
        QFilter changeTyeFilter = new QFilter("orgchangetype.id", "=", (Object)this.getModel().getDataEntity().getLong("changetype.id")).and("id", "!=", (Object)AdminOrgDetailConstants.ID_CHANGESCENE_ENABLE);
        event.getCustomQFilters().add(changeTyeFilter);
        if (this.getModel().getValue("org") instanceof DynamicObject) {
            DynamicObject buOrg = (DynamicObject)this.getModel().getValue("org");
            event.getCustomQFilters().add(BaseDataHelper.getBaseDataFilter((String)"haos_changescene", (long)buOrg.getLong("id")));
        }
    }

    private void setChangeReasonF7Filter(BeforeF7SelectEvent event) {
        DynamicObject changeSceneDyn = (DynamicObject)this.getModel().getValue("changescene");
        if (Objects.nonNull(changeSceneDyn)) {
            Set changeReasonSet = changeSceneDyn.getDynamicObjectCollection("changereason").stream().map(dyn -> dyn.getLong("fbasedataid.id")).collect(Collectors.toSet());
            QFilter changeReasonFilter = new QFilter("id", "in", changeReasonSet);
            event.getCustomQFilters().add(changeReasonFilter);
        }
    }

    private void setAdminOrgTypeFilter(BeforeF7SelectEvent event) {
        DynamicObject orgDy = this.getModel().getDataEntity();
        DynamicObject parentOrg = orgDy.getDynamicObject("parentorg");
        boolean isRoot = orgDy.getBoolean("isroot");
        if (parentOrg == null && isRoot) {
            QFilter stdFilter = new QFilter("adminorgtypestd.id", "in", (Object)AdminOrgConstants.ADMIN_ORG_TYPE_COMPANY_AND_GROUP);
            event.getCustomQFilters().add(stdFilter);
        }
    }

    private void handleCity(DynamicObject dataEntity) {
        DynamicObject city = dataEntity.getDynamicObject("city");
        if (!ObjectUtils.isEmpty((Object)city)) {
            this.getModel().beginInit();
            this.getModel().setValue("companyarea", (Object)city.getDynamicObject("country"));
            this.getView().updateView("companyarea");
            this.getModel().endInit();
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        String name;
        switch (name = args.getProperty().getName()) {
            case "city": {
                this.handleCity(this.getModel().getDataEntity());
                break;
            }
            case "changescene": {
                this.getModel().setValue("changereason", null);
                break;
            }
            case "adminorgtype": {
                new AdminOrgDetailHelper().setBelongCompany(this.getView());
                this.updateFullName();
                break;
            }
            case "companyarea": {
                this.getModel().setValue("city", null);
            }
            case "bsed": {
                if (this.getModel().getValue("establishmentdate") == null) {
                    this.getModel().setValue("establishmentdate", this.getModel().getValue("bsed"));
                }
                this.getView().getModel().setValue("tobedisabledate", (Object)this.getTobeDisableDate(this.getModel().getDataEntity().getBoolean("tobedisableflag")));
                break;
            }
            case "org": {
                this.doOrgChanged(args);
                break;
            }
            case "parentorg": {
                this.doParentChanged(args);
                break;
            }
            case "tobedisableflag": {
                this.doTobeDisableFlagChanged(args);
                break;
            }
            case "name": {
                this.updateFullName();
                break;
            }
            case "struct_parent_org": {
                AdminOrgDetailHelper.structProjectChg((IFormView)this.getView(), (PropertyChangedArgs)args);
            }
        }
    }

    private void doTobeDisableFlagChanged(PropertyChangedArgs changedArgs) {
        ChangeData changeData = changedArgs.getChangeSet()[0];
        Boolean tobeDisableFlag = (Boolean)changeData.getNewValue();
        this.getView().getModel().setValue("tobedisabledate", (Object)this.getTobeDisableDate(tobeDisableFlag));
    }

    private Date getTobeDisableDate(Boolean tobeDisableFlag) {
        if (!tobeDisableFlag.booleanValue()) {
            return null;
        }
        String dbTobeDisableFlag = this.getView().getPageCache().get("tobedisableflag");
        Date bsed = this.getModel().getDataEntity().getDate("bsed");
        if (HRStringUtils.equals((String)Boolean.FALSE.toString(), (String)dbTobeDisableFlag)) {
            return this.getModel().getDataEntity().getDate("bsed");
        }
        if (HRStringUtils.equals((String)Boolean.TRUE.toString(), (String)dbTobeDisableFlag)) {
            try {
                return HRDateTimeUtils.parseDate((String)this.getView().getPageCache().get("tobedisabledate"));
            }
            catch (ParseException exception) {
                throw new KDBizException(new ErrorCode("AdminOrgDetailEditPlugin", exception.getMessage()), new Object[0]);
            }
        }
        return bsed;
    }

    private void doParentChanged(PropertyChangedArgs changedArgs) {
        ChangeData changeData = changedArgs.getChangeSet()[0];
        DynamicObject newValue = (DynamicObject)changeData.getNewValue();
        if (newValue == null) {
            return;
        }
        Long buOrgId = this.getModel().getValue("org") != null ? ((DynamicObject)this.getModel().getValue("org")).getLong("id") : 100000L;
        this.setCorporateOrg(newValue);
        long parentOrgBoid = newValue.getLong("boid");
        Date modifyTime = this.getModel().getDataEntity().getDate("modifytime");
        OrgFullNameServiceWrapper fullNameServiceWrapper = new OrgFullNameServiceWrapper();
        String orgLongName = fullNameServiceWrapper.getOrgFullName(Long.valueOf(parentOrgBoid), modifyTime);
        this.getModel().setValue("parentorg_name", (Object)orgLongName);
        this.getModel().setValue("fullname", (Object)(orgLongName + HaosOrgUnitServiceHelper.getFullNameSep() + this.getModel().getValue("name")));
        this.setIndex(newValue);
        QFilter qFilter = new QFilter("boid", "=", (Object)parentOrgBoid);
        qFilter.and("iscurrentversion", "=", (Object)Character.valueOf('1'));
        String operation = (String)this.getView().getFormShowParameter().getCustomParam("adminorg_operation");
        boolean isNewOrg = HRStringUtils.equals((String)operation, (String)"addnew");
        new AdminOrgDetailHelper().setBelongCompany(this.getView());
        if (isNewOrg && this.getModel().getDataEntity().getDynamicObject("companyarea") == null) {
            this.getModel().setValue("companyarea", (Object)HRDyObjectPropUtil.getIdLongObject((DynamicObject)newValue.getDynamicObject("companyarea")));
            this.getModel().setValue("city", (Object)HRDyObjectPropUtil.getIdLongObject((DynamicObject)newValue.getDynamicObject("city")));
            this.getModel().setValue("workplace", (Object)HRDyObjectPropUtil.getIdLongObject((DynamicObject)newValue.getDynamicObject("workplace")));
        }
    }

    private void setIndex(DynamicObject parentDy) {
        if (parentDy != null && parentDy.getLong("id") != 0L) {
            int index = AdminOrgSortUtils.getOrgIndex((Long)((Long)parentDy.get("id")));
            this.getModel().setValue("index", (Object)index);
        }
    }

    private void setCorporateOrg(DynamicObject parentOrg) {
        DynamicObject adminOrgTypeDyn = this.getModel().getDataEntity().getDynamicObject("adminorgtype");
        if (adminOrgTypeDyn != null) {
            Long adminOrgTypeStdId = adminOrgTypeDyn.getLong("adminorgtypestd.id");
            if (!adminOrgTypeStdId.equals(AdminOrgDetailConstants.ID_ORGTYPE_DEPARTMENT)) {
                return;
            }
            if (parentOrg == null || parentOrg.getLong("id") == 0L) {
                this.getModel().setValue("corporateorg", (Object)0L);
            } else {
                String selectLongStruct = "id,structlongnumber";
                HRBaseServiceHelper structServiceHelper = new HRBaseServiceHelper("haos_adminorgstruct");
                Date bsed = parentOrg.getDate("bsed");
                QFilter structQFilter = new QFilter("adminorg", "=", (Object)parentOrg.getLong("boid")).and("startdate", "<=", (Object)bsed).and("enddate", ">=", (Object)bsed).and((QFilter)StructProjectConstants.ORG_STRUCT_FILTER.get());
                DynamicObject longStructDy = structServiceHelper.queryOne(selectLongStruct, structQFilter);
                if (Objects.isNull(longStructDy)) {
                    return;
                }
                String longStruct = longStructDy.getString("structlongnumber");
                String[] adminstructArr = longStruct.split("!");
                String selectAdminStruct = "id,name,corporateorg,structnumber";
                HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
                QFilter qFilter = new QFilter("structnumber", "in", (Object)adminstructArr).and("iscurrentversion", "=", (Object)"1");
                DynamicObjectCollection dynamicObjects = serviceHelper.queryOriginalCollection(selectAdminStruct, qFilter.toArray());
                Map<String, Long> map = dynamicObjects.stream().collect(Collectors.toMap(org -> org.getString("structnumber"), org -> org.getLong("corporateorg")));
                for (int i = adminstructArr.length - 1; i >= 0; --i) {
                    Long corporateorg;
                    if (!map.containsKey(adminstructArr[i]) || (corporateorg = map.get(adminstructArr[i])) == null || corporateorg.equals(0L)) continue;
                    this.getModel().setValue("corporateorg", (Object)corporateorg);
                    return;
                }
            }
        }
    }

    private void doOrgChanged(PropertyChangedArgs changedArgs) {
        ChangeData changeData = changedArgs.getChangeSet()[0];
        DynamicObject newDy = (DynamicObject)changeData.getNewValue();
        DynamicObject oldDy = (DynamicObject)changeData.getOldValue();
        String masterOrg = this.getPageCache().get("master_org");
        if (changedArgs.getChangeSet()[0].getNewValue() == null) {
            return;
        }
        HRBaseServiceHelper hrBaseServiceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
        boolean exists = hrBaseServiceHelper.isExists((Object)this.getModel().getDataEntity().getLong("id"));
        if (exists) {
            return;
        }
        Long orgIdNew = newDy.getLong("id");
        if (!StringUtils.equals((CharSequence)masterOrg, (CharSequence)String.valueOf(orgIdNew))) {
            Long orgIDOld = oldDy == null ? 0L : oldDy.getLong("id");
            this.getPageCache().put("master_org", String.valueOf(orgIDOld));
            ConfirmCallBackListener confirmCallBackListener = new ConfirmCallBackListener("master_org", (IFormPlugin)this);
            this.getView().showConfirm(this.msg, MessageBoxOptions.OKCancel, confirmCallBackListener);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        super.confirmCallBack(event);
        int result = event.getResult().getValue();
        if (event.getCallBackId().equals("master_org")) {
            if (result == MessageBoxResult.Yes.getValue()) {
                this.getView().getPageCache().put("master_org", String.valueOf(this.getModel().getDataEntity().getLong("org.id")));
                this.cleanOrgBaseInfo();
            } else {
                Long masterOrgId = Long.valueOf(this.getPageCache().get("master_org"));
                this.getModel().setValue("org", (Object)(masterOrgId.equals(0L) ? null : masterOrgId));
            }
        } else if (event.getCallBackId().equals("async_confirm")) {
            if (result == MessageBoxResult.Yes.getValue()) {
                AsyncEffectingReformHelper.yesConfirm((IFormView)this.getView());
            }
        } else if (CONFIRM_CALL_BACK_KEY_CONTINUE_CLOSE.equals(event.getCallBackId()) && result == MessageBoxResult.Yes.getValue()) {
            new AdminOrgDetailHelper().showEmptyOpPage((AbstractFormPlugin)this, this.getModel().getDataEntity());
            this.getModel().setDataChanged(false);
            this.getView().close();
        }
    }

    private void cleanOrgBaseInfo() {
        String operate = (String)this.getView().getFormShowParameter().getCustomParam("adminorg_operation");
        if (!HRStringUtils.equals((String)"infochg", (String)operate)) {
            this.getView().getModel().setValue("parentorg", null);
        }
        Map entityBaseDataIdMap = new AdminOrgDetailHelper().getBaseDataIdsByHrOrgBuID(FIELD_ENTITY_MAP.values(), this.getModel().getDataEntity().getLong("org.id"));
        for (String baseName : BASE_ARR) {
            Set baseSet = (Set)entityBaseDataIdMap.get(FIELD_ENTITY_MAP.get(baseName));
            if (StringUtils.equals((CharSequence)baseName, (CharSequence)"coopreltype")) {
                DynamicObjectCollection cooprelentryentity = this.getModel().getDataEntity(true).getDynamicObjectCollection("cooprelentryentity");
                if (cooprelentryentity.size() <= 0) continue;
                for (int i = 0; i < cooprelentryentity.size(); ++i) {
                    DynamicObject dynamicObject = (DynamicObject)cooprelentryentity.get(i);
                    Long baseId = HRDyObjectPropUtil.getIdLongObject((DynamicObject)dynamicObject.getDynamicObject(baseName));
                    if (baseSet != null && baseSet.contains(baseId)) continue;
                    this.getModel().setValue(baseName, null, i);
                }
                continue;
            }
            Long baseId = HRDyObjectPropUtil.getIdLongObject((DynamicObject)this.getModel().getDataEntity().getDynamicObject(baseName));
            if (baseSet != null && baseSet.contains(baseId)) continue;
            this.getModel().setValue(baseName, null);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        String opKey = afterDoOperationEventArgs.getOperateKey();
        if (afterDoOperationEventArgs.getOperationResult() == null || !afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            return;
        }
        if (opKey.equals("parentchg")) {
            if (this.checkOrgParentChg(this.getModel().getDataEntity())) {
                if (this.getModel().getDataEntity().getLong("id") == 0L) {
                    this.getModel().getDataEntity().set("id", (Object)this.getModel().getDataEntity().getLong("boid"));
                }
                new AdminOrgDetailHelper().showChgParentPage((AbstractFormPlugin)this, this.getModel().getDataEntity());
                this.getView().close();
            }
        } else if (opKey.equals("infochg")) {
            if (this.getModel().getDataEntity().getLong("id") == 0L) {
                this.getModel().getDataEntity().set("id", (Object)this.getModel().getDataEntity().getLong("boid"));
            }
            new AdminOrgDetailHelper().showChangeInfoPage((AbstractFormPlugin)this, this.getModel().getDataEntity());
            this.getView().close();
        } else if (opKey.equals("cancel")) {
            DynamicObject dataEntity = this.getModel().getDataEntity();
            if (dataEntity.getLong("id") == 0L) {
                dataEntity.set("id", (Object)dataEntity.getLong("boid"));
            }
            if (this.getModel().getDataEntity().getDataEntityState().isBizChanged()) {
                this.showCloseTips();
            } else {
                new AdminOrgDetailHelper().showEmptyOpPage((AbstractFormPlugin)this, dataEntity);
                this.getView().close();
            }
        }
    }

    private boolean checkOrgParentChg(DynamicObject dynamicObject) {
        return new AdminOrgDetailHelper().checkOrgParentChg(this.getView(), dynamicObject);
    }

    private void showCloseTips() {
        ConfirmCallBackListener confirmCallBacks = new ConfirmCallBackListener(CONFIRM_CALL_BACK_KEY_CONTINUE_CLOSE, (IFormPlugin)this);
        HashMap btnNameMaps = Maps.newHashMapWithExpectedSize((int)2);
        btnNameMaps.put(MessageBoxResult.Cancel.getValue(), ResManager.loadKDString((String)"\u8fd4\u56de\u7f16\u8f91", (String)"AdminOrgDetailEditPlugin_7", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
        btnNameMaps.put(MessageBoxResult.Yes.getValue(), ResManager.loadKDString((String)"\u76f4\u63a5\u9000\u51fa", (String)"AdminOrgDetailEditPlugin_8", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
        MessageBoxOptions options = MessageBoxOptions.OKCancel;
        String msg = ResManager.loadKDString((String)"\u68c0\u6d4b\u5230\u60a8\u6709\u66f4\u6539\u5185\u5bb9\uff0c\u662f\u5426\u4e0d\u4fdd\u5b58\u76f4\u63a5\u9000\u51fa\uff1f", (String)"AdminOrgDetailEditPlugin_9", (String)"hrmp-haos-formplugin", (Object[])new Object[0]) + "\r\n" + ResManager.loadKDString((String)"\u82e5\u4e0d\u4fdd\u5b58\uff0c\u5c06\u4e22\u5931\u8fd9\u4e9b\u66f4\u6539\u3002", (String)"AdminOrgDetailEditPlugin_10", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
        msg = StringUtils.isNotBlank((CharSequence)msg) ? msg.replaceAll("\\\\r\\\\n", "\r\n") : msg;
        this.getView().showConfirm(msg, this.getModel().getChangeDesc(), options, ConfirmTypes.Save, confirmCallBacks, (Map)btnNameMaps);
    }

    private void updateFullName() {
        DynamicObject parentOrg = (DynamicObject)this.getModel().getValue("parentorg");
        if (parentOrg != null) {
            Long parentOrgBoId = parentOrg.getLong("boid");
            String structLongNumber = parentOrg.getString("structlongnumber");
            int level = structLongNumber.split("!").length;
            Date modifyTime = this.getModel().getDataEntity().getDate("modifytime");
            OrgFullNameServiceWrapper fullNameServiceWrapper = new OrgFullNameServiceWrapper();
            int configLevel = fullNameServiceWrapper.getConfigLevel();
            String orgFullName = null;
            boolean thisOrgValid = fullNameServiceWrapper.validOrgLevelDy(this.getModel().getDataEntity());
            if (level + 1 <= configLevel) {
                if (thisOrgValid) {
                    orgFullName = this.getModel().getDataEntity().getString("name");
                }
            } else {
                orgFullName = fullNameServiceWrapper.getOrgFullName(parentOrgBoId, modifyTime);
                if (thisOrgValid) {
                    orgFullName = orgFullName + HaosOrgUnitServiceHelper.getFullNameSep() + this.getModel().getValue("name");
                }
            }
            this.getModel().setValue("fullname", (Object)orgFullName);
        }
    }
}
