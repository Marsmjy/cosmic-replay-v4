/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.ext.hr.filter.control.PermFilter
 *  kd.bos.ext.hr.ruleengine.enums.ParamTypeEnum
 *  kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FieldTip
 *  kd.bos.form.FieldTip$FieldTipsLevel
 *  kd.bos.form.FieldTip$FieldTipsTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.AdvContainer
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.DecimalEdit
 *  kd.bos.form.field.events.AfterF7SelectEvent
 *  kd.bos.form.field.events.AfterF7SelectListener
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.cache.util.PermCommonUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.constants.history.HisPageEnum
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hr.hbp.common.util.HRMapUtils
 *  kd.hr.hbp.common.util.HRQFilterHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.service.perm.dyna.DynaPremEvtSubService
 *  kd.hr.hrcs.bussiness.service.perm.dyna.DynaRoleDetailServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dyna.DynaAuthSchemeParamRuleService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dyna.DynaAuthSchemeServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.role.HRRolePermHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.role.RoleDBServiceHelper
 *  kd.hr.hrcs.bussiness.util.DynaSchemeRoleAssignDetailBean
 *  kd.hr.hrcs.bussiness.util.PermRuleValidateUtil
 *  kd.hr.hrcs.bussiness.util.RulePreviewUtil
 *  kd.hr.hrcs.bussiness.util.RuleUtil
 *  kd.hr.hrcs.common.model.perm.PermConditionInfo
 *  kd.hr.hrcs.common.model.perm.RuleParamInfo
 *  org.apache.commons.lang3.ObjectUtils
 */
package kd.hr.hrcs.formplugin.web.perm.dyna;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.ext.hr.filter.control.PermFilter;
import kd.bos.ext.hr.ruleengine.enums.ParamTypeEnum;
import kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FieldTip;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.container.AdvContainer;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.DecimalEdit;
import kd.bos.form.field.events.AfterF7SelectEvent;
import kd.bos.form.field.events.AfterF7SelectListener;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.cache.util.PermCommonUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.constants.history.HisPageEnum;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hr.hbp.common.util.HRMapUtils;
import kd.hr.hbp.common.util.HRQFilterHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.service.perm.dyna.DynaPremEvtSubService;
import kd.hr.hrcs.bussiness.service.perm.dyna.DynaRoleDetailServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.dyna.DynaAuthSchemeParamRuleService;
import kd.hr.hrcs.bussiness.servicehelper.perm.dyna.DynaAuthSchemeServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.role.HRRolePermHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.role.RoleDBServiceHelper;
import kd.hr.hrcs.bussiness.util.DynaSchemeRoleAssignDetailBean;
import kd.hr.hrcs.bussiness.util.PermRuleValidateUtil;
import kd.hr.hrcs.bussiness.util.RulePreviewUtil;
import kd.hr.hrcs.bussiness.util.RuleUtil;
import kd.hr.hrcs.common.model.perm.PermConditionInfo;
import kd.hr.hrcs.common.model.perm.RuleParamInfo;
import org.apache.commons.lang3.ObjectUtils;

@ExcludeFromJacocoGeneratedReport
public final class DynaAuthSchemePlugin
extends HRDataBaseEdit
implements BeforeF7SelectListener,
AfterF7SelectListener,
ClickListener {
    private static final String FIELD_ASSIGN_PERSON_ITEM = "assignpersonitem";
    private static final String FIELD_CANCEL_PERSON_ITEM = "cancelpersonitem";
    private static final String FIELD_ASSIGN_ACTYPE = "assignactype";
    private static final String FIELD_CANCEL_ACTYPE = "cancelactype";
    private static final String FIELD_AUTH_ACTION = "authaction";
    private static final String FIELD_CONDITION = "condition";
    private static final String FIELD_ASSIGN_DAYS = "assigndays";
    private static final String FIELD_ROLE_HRCS = "hrcsrole";
    private static final String FIELD_ROLE_CUSTOM_ENABLE = "customenable";
    private static final String FIELD_ROLE_REMAKE = "roleremark";
    private static final String FIELD_ROLE_CUSTOM_INFO = "custominfo";
    private static final String RULE_CONFIG_AP = "advconap";
    private static final String RULE_CONFIG_TIP = "labelap4";
    private static final String KEY_ASSIGN_SCENE_ENTRY = "assignactionentry";
    private static final String KEY_CANCEL_SCENE_ENTRY = "cancelactionentry";
    private static final String KEY_ROLE_ENTRY = "roleentry";
    private static final String KEY_SCHEME_RULE = "permfilterap";
    private static final String CACHE_KEY_ALL_RULE_CONFIG = "allruleconfig";
    private static final String CACHE_KEY_LAST_COMMON_RULE_PARAM = "lastcommonruleparam";
    private static final String CACHE_KEY_CUR_COMMON_RULE_PARAM = "curcommonruleparam";
    private static final Map<String, String> convertParamTypeMap = Maps.newHashMapWithExpectedSize((int)2);
    String PARAM_SKIP_CHANGE_COMMON_TIPS = "skipChangeTips";
    String PARAM_SKIP_CHANGE_TIPS = "skipCurPageChangeTips";

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        BasedataEdit assignAcTypeBd = (BasedataEdit)this.getControl(FIELD_ASSIGN_ACTYPE);
        assignAcTypeBd.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        assignAcTypeBd.addAfterF7SelectListener((AfterF7SelectListener)this);
        BasedataEdit cancelAcTypeBd = (BasedataEdit)this.getControl(FIELD_CANCEL_ACTYPE);
        cancelAcTypeBd.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        cancelAcTypeBd.addAfterF7SelectListener((AfterF7SelectListener)this);
        BasedataEdit adminGroupBd = (BasedataEdit)this.getControl("admingroup");
        adminGroupBd.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeBindData(EventObject evt) {
        String schemeName;
        super.beforeBindData(evt);
        this.getModel().beginInit();
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        OperationStatus status = formShowParameter.getStatus();
        if (OperationStatus.ADDNEW.equals((Object)status)) {
            long userAdminGroupId;
            Set userAdminGroups = HRRolePermHelper.queryUserAdminGroups((long)RequestContext.get().getCurrUserId());
            long l = userAdminGroupId = userAdminGroups.isEmpty() ? 0L : (Long)userAdminGroups.iterator().next();
            if (0L != userAdminGroupId) {
                this.getModel().setValue("admingroup", (Object)userAdminGroupId);
            }
        }
        if (HRStringUtils.isNotEmpty((String)(schemeName = (String)formShowParameter.getCustomParam("schemeName"))) && status.equals((Object)OperationStatus.ADDNEW)) {
            this.getModel().setValue("name", (Object)schemeName);
        }
        String hyperEnter = (String)formShowParameter.getCustomParam("hyperEnter");
        String hisPage = (String)this.getView().getFormShowParameter().getCustomParam("hisPage");
        if (HRStringUtils.isNotEmpty((String)hyperEnter) || HisPageEnum.VERSION_PAGE.getPage().equals(hisPage)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"setadminrange"});
        }
        this.setRequiredField();
        Object isCopy = this.getView().getFormShowParameter().getCustomParam("iscopy");
        if (null != isCopy) {
            List permConditionInfoList = DynaAuthSchemeServiceHelper.queryDyanRuleItems();
            String ruleJson = SerializationUtils.toJsonString((Object)permConditionInfoList);
            this.getPageCache().put(CACHE_KEY_LAST_COMMON_RULE_PARAM, ruleJson);
            this.updateRuleControl(ruleJson);
        }
        IPageCache pageCache = this.getPageCache();
        String roleEntryReloadStr = pageCache.get("roleEntryReload");
        boolean roleEntryReload = HRStringUtils.isEmpty((String)roleEntryReloadStr);
        if ((!status.equals((Object)OperationStatus.ADDNEW) || null != isCopy) && roleEntryReload) {
            long schemeId = (Long)this.getModel().getValue("id");
            Object isChange = this.getView().getFormShowParameter().getCustomParam("isChange");
            BillOperationStatus billStatus = ((BaseShowParameter)this.getView().getFormShowParameter()).getBillStatus();
            boolean isCurrentVersion = (Boolean)this.getModel().getValue("iscurrentversion");
            if (null != isChange) {
                schemeId = (Long)this.getView().getFormShowParameter().getCustomParam("pkId");
                schemeId = this.getSourceVid(schemeId);
            }
            if (BillOperationStatus.AUDIT == billStatus && isCurrentVersion) {
                schemeId = this.getSourceVid(schemeId);
            }
            boolean sourceSchemeIdNotUsed = HRStringUtils.isEmpty((String)this.getPageCache().get("sourceSchemeIdUsed"));
            if (null != isCopy && sourceSchemeIdNotUsed) {
                schemeId = (Long)this.getView().getFormShowParameter().getCustomParam("sourceSchemeId");
                this.getPageCache().put("sourceSchemeIdUsed", "1");
            }
            DynaRoleDetailServiceHelper.loadRoleCustomInfo((long)schemeId, (IFormView)this.getView());
            if (null != isCopy && sourceSchemeIdNotUsed) {
                ORM orm = ORM.create();
                DynamicObjectCollection roleEntity = this.getModel().getEntryEntity(KEY_ROLE_ENTRY);
                long[] roleEntryIds = orm.genLongIds("hrcs_dynascheme.roleentry", roleEntity.size());
                int index = 0;
                for (DynamicObject roleRow : roleEntity) {
                    long roleEntryId = roleEntryIds[index++];
                    roleRow.set("id", (Object)roleEntryId);
                    String customRoleInfoStr = roleRow.getString(FIELD_ROLE_CUSTOM_INFO);
                    DynaSchemeRoleAssignDetailBean customRoleInfo = (DynaSchemeRoleAssignDetailBean)SerializationUtils.fromJsonString((String)customRoleInfoStr, DynaSchemeRoleAssignDetailBean.class);
                    customRoleInfo.setEntryId(roleEntryId);
                    roleRow.set(FIELD_ROLE_CUSTOM_INFO, (Object)SerializationUtils.toJsonString((Object)customRoleInfo));
                }
            }
            DynamicObjectCollection roleEntry = this.getModel().getEntryEntity(KEY_ROLE_ENTRY);
            for (DynamicObject roleRow : roleEntry) {
                long roleEntryId = roleRow.getLong("id");
                String customRoleInfoStr = roleRow.getString(FIELD_ROLE_CUSTOM_INFO);
                if (!HRStringUtils.isNotEmpty((String)customRoleInfoStr)) continue;
                DynaSchemeRoleAssignDetailBean customRoleInfo = (DynaSchemeRoleAssignDetailBean)SerializationUtils.fromJsonString((String)customRoleInfoStr, DynaSchemeRoleAssignDetailBean.class);
                customRoleInfo.setEntryId(roleEntryId);
                roleRow.set(FIELD_ROLE_CUSTOM_INFO, (Object)SerializationUtils.toJsonString((Object)customRoleInfo));
                roleRow.set(FIELD_ROLE_HRCS, (Object)RoleDBServiceHelper.loadHrRoleDyn((String)customRoleInfo.getRoleId()));
            }
        }
        if (!roleEntryReload) {
            String customInfoListStr = this.getPageCache().get("customInfoList");
            List customInfoList = (List)SerializationUtils.fromJsonString((String)customInfoListStr, List.class);
            int index = 0;
            for (String customInfo : customInfoList) {
                this.getModel().setValue(FIELD_ROLE_CUSTOM_INFO, (Object)customInfo, index++);
            }
            this.getPageCache().remove("roleEntryReload");
            this.getPageCache().remove("customInfoList");
        }
        this.getModel().setDataChanged(false);
        this.getModel().endInit();
    }

    private long getSourceVid(long schemeId) {
        HRBaseServiceHelper schemeHelper = new HRBaseServiceHelper("hrcs_dynascheme");
        return schemeHelper.queryOriginalOne("sourcevid", (Object)schemeId).getLong("sourcevid");
    }

    public void afterBindData(EventObject e) {
        BillOperationStatus status = ((BaseShowParameter)this.getView().getFormShowParameter()).getBillStatus();
        PermFilter permFilter = this.getRuleControl();
        HashMap data = Maps.newHashMapWithExpectedSize((int)8);
        String json = (String)this.getModel().getValue(FIELD_CONDITION);
        if (!PermRuleValidateUtil.isDecisionSetConditionEmpty((String)json)) {
            json = RuleUtil.getNewestConditionValue((String)json);
            permFilter.setValue(json);
            data.put("value", json);
        } else if (BillOperationStatus.VIEW == status || BillOperationStatus.SUBMIT == status || BillOperationStatus.AUDIT == status) {
            AdvContainer container = (AdvContainer)this.getControl(RULE_CONFIG_AP);
            container.setCollapse(true);
            this.getView().setVisible(Boolean.TRUE, new String[]{RULE_CONFIG_TIP});
            data.put("display", false);
            permFilter.setData((Map)data);
            return;
        }
        if (BillOperationStatus.VIEW == status || BillOperationStatus.SUBMIT == status || BillOperationStatus.AUDIT == status) {
            data.put("pageState", "VIEW");
        }
        List permConditionInfos = DynaAuthSchemeServiceHelper.queryDyanRuleItems();
        this.getPageCache().put(CACHE_KEY_LAST_COMMON_RULE_PARAM, SerializationUtils.toJsonString((Object)permConditionInfos));
        data.put("onlyUpdateWeb", "true");
        data.put("param", this.permCond2RuleParam(permConditionInfos));
        data.put("comparisonOpt", DynaAuthSchemeParamRuleService.getComparisonOperatorsMap());
        data.put("conditionExpress", permFilter.getConditionExpress());
        permFilter.setData((Map)data);
        Object isChange = this.getView().getFormShowParameter().getCustomParam("isChange");
        if (null != isChange) {
            ORM orm = ORM.create();
            long schemeId = orm.genLongId("hrcs_dynascheme");
            this.getModel().beginInit();
            this.getModel().setValue("id", (Object)schemeId);
            this.getModel().beginInit();
            this.getModel().setDataChanged(false);
            this.getView().updateView("id");
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String propKey = evt.getProperty().getName();
        ListShowParameter formShowParameter = (ListShowParameter)evt.getFormShowParameter();
        if (HRStringUtils.equals((String)FIELD_ASSIGN_ACTYPE, (String)propKey) || HRStringUtils.equals((String)FIELD_CANCEL_ACTYPE, (String)propKey)) {
            Set eventIds = DynaPremEvtSubService.queryChgRecordEffEventIds();
            String entryKey = HRStringUtils.equals((String)propKey, (String)FIELD_ASSIGN_ACTYPE) ? KEY_ASSIGN_SCENE_ENTRY : KEY_CANCEL_SCENE_ENTRY;
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity(entryKey);
            Set usedEventIds = entryEntity.stream().map(it -> it.getLong(propKey + ".id")).collect(Collectors.toSet());
            eventIds.removeAll(usedEventIds);
            QFilter idFilter = new QFilter("id", "in", (Object)eventIds);
            formShowParameter.getListFilterParameter().getQFilters().add(idFilter);
        } else if (HRStringUtils.equals((String)"admingroup", (String)propKey)) {
            QFilter idFilter = new QFilter("id", "in", (Object)HRRolePermHelper.queryUserAdminGroups((long)RequestContext.get().getCurrUserId()));
            QFilter domainFilter = new QFilter("isdomain", "=", (Object)"1").and("domain.number", "=", (Object)"hr");
            formShowParameter.getListFilterParameter().getQFilters().add(idFilter);
            formShowParameter.getListFilterParameter().getQFilters().add(domainFilter);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        MessageBoxResult result = evt.getResult();
        String callBackId = evt.getCallBackId();
        if (HRStringUtils.equals((String)"clearRuleControl", (String)callBackId)) {
            if (result.equals((Object)MessageBoxResult.Yes)) {
                this.updateRuleControlEmpty();
                this.setRequiredField();
                PermFilter ruleControl = this.getRuleControl();
                ruleControl.setValue("");
                this.getView().updateView("hintap");
            } else {
                String oldActionValue = this.getPageCache().get("oldActionValue");
                this.getPageCache().put("secondConfirmCancel", "true");
                this.getModel().setValue(FIELD_AUTH_ACTION, (Object)oldActionValue);
            }
        } else if ((StringUtils.equals((CharSequence)"confirmchange", (CharSequence)callBackId) || StringUtils.equals((CharSequence)"audit", (CharSequence)callBackId)) && evt.getResult() == MessageBoxResult.Yes) {
            this.getPageCache().put(this.PARAM_SKIP_CHANGE_COMMON_TIPS, "true");
            this.getPageCache().put(this.PARAM_SKIP_CHANGE_TIPS, "true");
            this.getView().invokeOperation(callBackId);
        }
    }

    private void updateRuleControl(String ruleJson) {
        if (HRStringUtils.isNotEmpty((String)ruleJson)) {
            List permConditionInfos = SerializationUtils.fromJsonStringToList((String)ruleJson, PermConditionInfo.class);
            HashMap<String, List<RuleParamInfo>> data = new HashMap<String, List<RuleParamInfo>>();
            data.put("param", this.permCond2RuleParam(permConditionInfos));
            this.invokeControl(data);
        }
    }

    private List<RuleParamInfo> permCond2RuleParam(List<PermConditionInfo> permConditionInfos) {
        ArrayList<RuleParamInfo> ruleParamInfos = new ArrayList<RuleParamInfo>(10);
        for (PermConditionInfo permConditionInfo : permConditionInfos) {
            String paramType = permConditionInfo.getParamType();
            paramType = convertParamTypeMap.getOrDefault(paramType, paramType);
            RuleParamInfo info = new RuleParamInfo();
            info.setId(String.valueOf(permConditionInfo.getParamId()));
            info.setText(permConditionInfo.getDisplayParam());
            info.setType(paramType);
            info.setLevel("1");
            info.setTypeDetail(paramType);
            if (ParamTypeEnum.ENUM.getValue().equals(paramType)) {
                info.setType(ParamTypeEnum.STRING.getValue());
                info.setEnumList(SerializationUtils.toJsonString((Object)permConditionInfo.getEnumInfos()));
                info.setTypeDetail("mul_enum");
            }
            if (ParamTypeEnum.DYNAMICOBJECT.getValue().equals(paramType)) {
                info.setType(ParamTypeEnum.DYNAMICOBJECT.getValue());
                info.setEntityNumber(permConditionInfo.getBaseDataNumber());
                info.setTypeDetail(paramType);
                info.setFilters(permConditionInfo.getFilters());
            }
            if (ParamTypeEnum.ADMINORG.getValue().equals(paramType)) {
                info.setType(ParamTypeEnum.DYNAMICOBJECT.getValue());
                info.setEntityNumber(permConditionInfo.getBaseDataNumber());
                info.setCategory(ParamTypeEnum.ADMINORG.getValue());
                info.setTypeDetail(paramType);
                info.setFilters(permConditionInfo.getFilters());
            }
            ruleParamInfos.add(info);
        }
        return ruleParamInfos;
    }

    private void invokeControl(Object data) {
        IClientViewProxy clientViewProxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
        clientViewProxy.invokeControlMethod(KEY_SCHEME_RULE, "updateData", new Object[]{data});
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate source = (FormOperate)args.getSource();
        String operateKey = source.getOperateKey();
        String authAction = this.getModelValStr(FIELD_AUTH_ACTION);
        if (HRStringUtils.equals((String)operateKey, (String)"save") || HRStringUtils.equals((String)operateKey, (String)"confirmchange") || HRStringUtils.equals((String)operateKey, (String)"submit")) {
            String allRuleConfigs = this.getAllRuleConfigs();
            if (!PermRuleValidateUtil.isDecisionSetConditionEmpty((String)allRuleConfigs)) {
                RuleValidateInfo ruleValidateInfo = PermRuleValidateUtil.validCondition((String)allRuleConfigs);
                if (!ruleValidateInfo.isSuccess()) {
                    this.getView().showErrorNotification(ruleValidateInfo.getMsgList().toString());
                    args.setCancel(true);
                    return;
                }
                this.getModel().setValue(FIELD_CONDITION, (Object)allRuleConfigs);
                this.resolveRuleDesc(allRuleConfigs);
            } else {
                if (HRStringUtils.equals((String)"3", (String)authAction)) {
                    this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u8bbe\u7f6e\u6761\u4ef6\u89c4\u5219", (String)"DynaAuthSchemePlugin_5", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    args.setCancel(true);
                    return;
                }
                this.getModel().setValue(FIELD_CONDITION, (Object)"");
                this.getModel().setValue("ruledescription", (Object)"");
            }
            this.getPageCache().put(CACHE_KEY_ALL_RULE_CONFIG, allRuleConfigs);
            if (HRStringUtils.equals((String)"1", (String)authAction)) {
                this.getModel().deleteEntryData(KEY_CANCEL_SCENE_ENTRY);
            } else if (HRStringUtils.equals((String)"2", (String)authAction)) {
                this.getModel().deleteEntryData(KEY_ASSIGN_SCENE_ENTRY);
                this.getModel().setValue(FIELD_ASSIGN_DAYS, (Object)0);
            }
            if (!PermRuleValidateUtil.isDecisionSetConditionEmpty((String)allRuleConfigs)) {
                DynaAuthSchemeServiceHelper.resolveRuleConfigToSearch((IFormView)this.getView());
            }
            DynaAuthSchemeServiceHelper.resolveSceneToSearch((IFormView)this.getView());
            if (HRStringUtils.equals((String)operateKey, (String)"confirmchange") || HRStringUtils.equals((String)operateKey, (String)"audit")) {
                String skipChangeTips = this.getView().getPageCache().get(this.PARAM_SKIP_CHANGE_TIPS);
                if (HRStringUtils.equals((String)skipChangeTips, (String)"true")) {
                    this.getView().getPageCache().remove(this.PARAM_SKIP_CHANGE_TIPS);
                    return;
                }
                DynamicObject dataEntity = this.getModel().getDataEntity(true);
                boolean isShowTips = DynaAuthSchemeServiceHelper.showChangeTips((IFormView)this.getView(), (long)dataEntity.getLong("boid"), (IFormPlugin)this, (String)operateKey);
                if (isShowTips) {
                    args.setCancel(true);
                }
                return;
            }
        } else if (HRStringUtils.equals((String)operateKey, (String)"checkroledetails")) {
            DynamicObject entryRow = this.getCurRoleEntryRow();
            Object role = entryRow.get("role");
            String customEnable = entryRow.getString(FIELD_ROLE_CUSTOM_ENABLE);
            if (ObjectUtils.isEmpty((Object)role)) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u89d2\u8272", (String)"DynaAuthSchemePlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            if (HRStringUtils.isEmpty((String)customEnable)) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u89d2\u8272\u6210\u5458\u8303\u56f4\u5c5e\u6027\u3002", (String)"DynaAuthSchemePlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
            }
        }
        if (HRStringUtils.equals((String)operateKey, (String)"confirmchange") || HRStringUtils.equals((String)operateKey, (String)"submit")) {
            ArrayList customInfoList = Lists.newArrayListWithExpectedSize((int)10);
            for (DynamicObject row : this.getModel().getEntryEntity(KEY_ROLE_ENTRY)) {
                customInfoList.add(row.getString(FIELD_ROLE_CUSTOM_INFO));
            }
            this.getPageCache().put("customInfoList", SerializationUtils.toJsonString((Object)customInfoList));
            this.getPageCache().put("roleEntryReload", "1");
        }
    }

    private void resolveRuleDesc(String allRuleConfigs) {
        Map previewMulLang = RulePreviewUtil.getConditionPreviewMulLang((String)allRuleConfigs);
        LocaleString ruleDesc = new LocaleString();
        for (Map.Entry entry : previewMulLang.entrySet()) {
            String trimedRuleDesc = RulePreviewUtil.trimMaxLength((String)((String)entry.getValue()), (int)1000);
            ruleDesc.setItem((String)entry.getKey(), (Object)trimedRuleDesc);
        }
        this.getModel().setValue("ruledescription", (Object)ruleDesc);
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        String operateKey = args.getOperateKey();
        OperationResult operationResult = args.getOperationResult();
        if (operationResult != null && !operationResult.isSuccess()) {
            return;
        }
        if (HRStringUtils.equals((String)"newassignentry", (String)operateKey) || HRStringUtils.equals((String)"newcancelentry", (String)operateKey)) {
            if (!this.isOnly1010()) {
                int[] selectIndex;
                if (HRStringUtils.equals((String)"newcancelentry", (String)operateKey)) {
                    selectIndex = ((EntryGrid)this.getControl(KEY_CANCEL_SCENE_ENTRY)).getSelectRows();
                    this.getModel().setValue(FIELD_CANCEL_PERSON_ITEM, null, selectIndex[0]);
                    ((DynamicObject)this.getModel().getEntryEntity(KEY_CANCEL_SCENE_ENTRY).get(selectIndex[0])).getDataEntityState().setBizChanged(false);
                } else if (HRStringUtils.equals((String)"newassignentry", (String)operateKey)) {
                    selectIndex = ((EntryGrid)this.getControl(KEY_ASSIGN_SCENE_ENTRY)).getSelectRows();
                    this.getModel().setValue(FIELD_ASSIGN_PERSON_ITEM, null, selectIndex[0]);
                    ((DynamicObject)this.getModel().getEntryEntity(KEY_ASSIGN_SCENE_ENTRY).get(selectIndex[0])).getDataEntityState().setBizChanged(false);
                }
            }
        } else if (HRStringUtils.equals((String)operateKey, (String)"setadminrange") && operationResult != null && operationResult.isSuccess()) {
            DynaAuthSchemeServiceHelper.showAdminRangeDetail((Object)this.getModel().getValue("id"), (IFormView)this.getView());
        } else if (HRStringUtils.equals((String)operateKey, (String)"checkroledetails") && operationResult != null && operationResult.isSuccess()) {
            DynamicObject entryRow = this.getCurRoleEntryRow();
            String customInfo = entryRow.getString(FIELD_ROLE_CUSTOM_INFO);
            this.showRoleDetails(customInfo);
        } else if ("unsubmit".equals(operateKey) || "unaudit".equals(operateKey)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{RULE_CONFIG_TIP});
            HashMap data = Maps.newHashMapWithExpectedSize((int)2);
            data.put("display", true);
            this.invokeControl(data);
        }
        if (HRStringUtils.equals((String)operateKey, (String)"addrole")) {
            if (HRStringUtils.isEmpty((String)this.getModel().getDataEntity().getLocaleString("name").getLocaleValue())) {
                FieldTip fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "name", ResManager.loadKDString((String)"\u8bf7\u5148\u5f55\u5165\u540d\u79f0\u3002", (String)"DynaAuthSchemePlugin_7", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                this.getView().showFieldTip(fieldTip);
                return;
            }
            Set selectedRoleIds = this.getModel().getEntryEntity(KEY_ROLE_ENTRY).stream().map(it -> it.getString("role_id")).collect(Collectors.toSet());
            HRRolePermHelper.showRoleF7((CloseCallBack)new CloseCallBack((IFormPlugin)this, "chooseRole"), (IFormView)this.getView(), selectedRoleIds);
        }
        this.setPermFilterStatus(operateKey);
        String hisPage = (String)this.getView().getFormShowParameter().getCustomParam("hisPage");
        if (HisPageEnum.CHANGE_PAGE.getPage().equals(hisPage)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"setadminrange"});
        } else if ("confirmchange".equals(operateKey) || "save".equals(operateKey)) {
            this.getView().setVisible(Boolean.valueOf(true), new String[]{"setadminrange"});
        }
    }

    private boolean isOnly1010() {
        DynamicObjectCollection hrcsDynaAuthObjectDyn = new HRBaseServiceHelper("hrcs_dynaauthobject").queryOriginalCollection("id", new QFilter[]{HRQFilterHelper.buildEnable()});
        if (HRCollUtil.isNotEmpty((Collection)hrcsDynaAuthObjectDyn)) {
            Set ids = hrcsDynaAuthObjectDyn.stream().map(item -> item.get("id")).collect(Collectors.toSet());
            return ids.size() == 1 && ids.contains(1010L);
        }
        return false;
    }

    private void setPermFilterStatus(String operateKey) {
        HashMap dataMap = Maps.newHashMapWithExpectedSize((int)1);
        PermFilter permFilter = this.getRuleControl();
        BillOperationStatus billStatus = ((BaseShowParameter)this.getView().getFormShowParameter()).getBillStatus();
        if (BillOperationStatus.VIEW == billStatus || BillOperationStatus.SUBMIT == billStatus || BillOperationStatus.AUDIT == billStatus) {
            dataMap.put("pageState", "VIEW");
        } else {
            OperationStatus status = this.getView().getFormShowParameter().getStatus();
            String statusParam = status.toString();
            dataMap.put("pageState", statusParam);
        }
        if (HRStringUtils.equals((String)operateKey, (String)"change")) {
            Optional<String> any;
            dataMap.put("pageState", OperationStatus.EDIT.toString());
            this.getView().getFormShowParameter().setCustomParam("isChange", (Object)"1");
            Map conditionExpress = permFilter.getConditionExpress();
            if (HRMapUtils.isNotEmpty((Map)conditionExpress) && (any = conditionExpress.values().stream().filter(it -> HRStringUtils.isEmpty((String)it)).findAny()).isPresent()) {
                List permConditionInfos = DynaAuthSchemeServiceHelper.queryDyanRuleItems();
                this.getPageCache().put(CACHE_KEY_LAST_COMMON_RULE_PARAM, SerializationUtils.toJsonString((Object)permConditionInfos));
                dataMap.put("onlyUpdateWeb", "true");
                dataMap.put("param", this.permCond2RuleParam(permConditionInfos));
                dataMap.put("comparisonOpt", DynaAuthSchemeParamRuleService.getComparisonOperatorsMap());
                dataMap.put("conditionExpress", conditionExpress);
            }
        }
        permFilter.setData((Map)dataMap);
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        String actionId = evt.getActionId();
        Object returnData = evt.getReturnData();
        if (HRStringUtils.equals((String)"chooseRole", (String)actionId)) {
            if (null != returnData) {
                ListSelectedRowCollection listSelectedRows = (ListSelectedRowCollection)returnData;
                String roleId = (String)listSelectedRows.getPrimaryKeyValues()[0];
                DynamicObject dataEntity = this.getModel().getDataEntity(true);
                ORM orm = ORM.create();
                long schemeId = dataEntity.getLong("id");
                if (schemeId == 0L) {
                    schemeId = orm.genLongId("hrcs_dynascheme");
                    this.getModel().setValue("id", (Object)schemeId);
                }
                long roleEntryId = orm.genLongId("hrcs_dynascheme.roleentry");
                DynamicObject roleDy = RoleDBServiceHelper.queryHrRoleDyn((String)roleId);
                String roleProperty = roleDy.getString("property");
                DynaSchemeRoleAssignDetailBean bean = new DynaSchemeRoleAssignDetailBean();
                bean.setRoleId(roleId);
                bean.setRoleProperty(roleProperty);
                bean.setDynaSchemeId(schemeId);
                bean.setEntryId(roleEntryId);
                this.showRoleDetails(SerializationUtils.toJsonString((Object)bean));
            }
        } else if (HRStringUtils.equals((String)"roleDetails", (String)actionId)) {
            if (null == returnData) {
                return;
            }
            if (returnData instanceof String) {
                String roleDetail = (String)returnData;
                DynaSchemeRoleAssignDetailBean roleAssignDetail = (DynaSchemeRoleAssignDetailBean)SerializationUtils.fromJsonString((String)roleDetail, DynaSchemeRoleAssignDetailBean.class);
                String dataProperty = roleAssignDetail.getDataProperty();
                LocaleString remark = roleAssignDetail.getRemark();
                String roleId = roleAssignDetail.getRoleId();
                long entryId = roleAssignDetail.getEntryId();
                DynamicObjectCollection roleEntry = this.getModel().getEntryEntity(KEY_ROLE_ENTRY);
                boolean hasExist = false;
                int roleIndex = 0;
                for (DynamicObject row : roleEntry) {
                    long curEntryId = row.getLong("id");
                    if (curEntryId == entryId) {
                        hasExist = true;
                        break;
                    }
                    ++roleIndex;
                }
                if (!hasExist) {
                    this.getModel().createNewEntryRow(KEY_ROLE_ENTRY);
                    roleEntry = this.getModel().getEntryEntity(KEY_ROLE_ENTRY);
                }
                this.getModel().setValue("role", (Object)roleId, roleIndex);
                this.getModel().setValue(FIELD_ROLE_HRCS, (Object)RoleDBServiceHelper.loadHrRoleDyn((String)roleId), roleIndex);
                this.getModel().setValue(FIELD_ROLE_CUSTOM_ENABLE, (Object)dataProperty, roleIndex);
                this.getModel().setValue(FIELD_ROLE_REMAKE, (Object)remark, roleIndex);
                ((DynamicObject)roleEntry.get(roleIndex)).set("id", (Object)entryId);
                this.getModel().setValue(FIELD_ROLE_CUSTOM_INFO, (Object)roleDetail, roleIndex);
                this.getView().updateView(KEY_ROLE_ENTRY);
            }
        }
    }

    private void showRoleDetails(String roleDetailJson) {
        boolean isView;
        String auditStatus = this.getModel().getDataEntity().getString("status");
        OperationStatus viewStatus = this.getView().getFormShowParameter().getStatus();
        FormShowParameter showParameter = new FormShowParameter();
        boolean bl = isView = HRStringUtils.equals((String)auditStatus, (String)"B") || HRStringUtils.equals((String)auditStatus, (String)"C") || viewStatus.equals((Object)OperationStatus.VIEW);
        if (isView) {
            showParameter.setStatus(OperationStatus.VIEW);
        }
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setFormId("hrcs_dyscassignroledetail");
        showParameter.setCaption(ResManager.loadKDString((String)"%s-\u52a8\u6001\u65b9\u6848\u5206\u914d\u89d2\u8272\u8be6\u60c5", (String)"DynaAuthSchemePlugin_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{this.getModel().getDataEntity().getLocaleString("name").getLocaleValue()}));
        showParameter.setCustomParam("dynaSchemeBean", (Object)roleDetailJson);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "roleDetails"));
        this.getView().showForm(showParameter);
    }

    private DynamicObject getCurRoleEntryRow() {
        int index = this.getModel().getEntryCurrentRowIndex(KEY_ROLE_ENTRY);
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity(KEY_ROLE_ENTRY);
        return (DynamicObject)entryEntity.get(index);
    }

    public void propertyChanged(PropertyChangedArgs args) {
        String propKey = args.getProperty().getName();
        ChangeData[] changeSet = args.getChangeSet();
        if (HRStringUtils.equals((String)FIELD_AUTH_ACTION, (String)propKey)) {
            ChangeData changeData = changeSet[0];
            String oldValue = (String)changeData.getOldValue();
            String newValue = (String)changeData.getNewValue();
            String allRuleConfigStr = this.getAllRuleConfigs();
            if (HRStringUtils.isNotEmpty((String)oldValue) && HRStringUtils.isNotEmpty((String)newValue) && !PermRuleValidateUtil.isDecisionSetConditionEmpty((String)allRuleConfigStr) && HRStringUtils.isEmpty((String)this.getPageCache().get("secondConfirmCancel"))) {
                this.getPageCache().put("oldActionValue", oldValue);
                this.getView().showConfirm(ResManager.loadKDString((String)"\u5207\u6362\u6388\u6743\u52a8\u4f5c\u540e\uff0c\u89c4\u5219\u914d\u7f6e\u5c06\u88ab\u6e05\u7a7a\uff0c\u786e\u5b9a\u5207\u6362\u5417\uff1f", (String)"DynaAuthSchemePlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, new ConfirmCallBackListener("clearRuleControl", (IFormPlugin)this));
                return;
            }
            this.getPageCache().remove("secondConfirmCancel");
            this.setRequiredField();
            this.updateRuleControlEmpty();
            this.getView().updateView("hintap");
        }
    }

    private void updateRuleControlEmpty() {
        IPageCache pageCache = this.getPageCache();
        List permConditionInfoList = DynaAuthSchemeServiceHelper.queryDyanRuleItems();
        String ruleJson = SerializationUtils.toJsonString((Object)permConditionInfoList);
        pageCache.put(CACHE_KEY_LAST_COMMON_RULE_PARAM, ruleJson);
        this.updateRuleControl(ruleJson);
    }

    private PermFilter getRuleControl() {
        return (PermFilter)this.getControl(KEY_SCHEME_RULE);
    }

    private String getAllRuleConfigs() {
        PermFilter permFilter = this.getRuleControl();
        return permFilter.getValue();
    }

    private void setRequiredField() {
        String action = this.getModelValStr(FIELD_AUTH_ACTION);
        if (HRStringUtils.equals((String)"1", (String)action)) {
            this.setAssignRequiredField(true);
            this.setCancelRequiredField(false);
            this.getView().setVisible(Boolean.TRUE, new String[]{"assignactionflex", "assignflex"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"cancelactionflex", "cancelflex"});
        } else if (HRStringUtils.equals((String)"2", (String)action)) {
            this.setCancelRequiredField(true);
            this.setAssignRequiredField(false);
            this.getView().setVisible(Boolean.FALSE, new String[]{"assignactionflex", "assignflex"});
            this.getView().setVisible(Boolean.TRUE, new String[]{"cancelactionflex", "cancelflex"});
        } else {
            this.setAssignRequiredField(true);
            this.setCancelRequiredField(true);
            this.getView().setVisible(Boolean.TRUE, new String[]{"assignactionflex", "assignflex", "cancelactionflex", "cancelflex"});
        }
    }

    private void setAssignRequiredField(boolean mustInput) {
        EntryGrid assignSceneEntry = (EntryGrid)this.getControl(KEY_ASSIGN_SCENE_ENTRY);
        assignSceneEntry.setMustInput(FIELD_ASSIGN_ACTYPE, mustInput);
        assignSceneEntry.setMustInput(FIELD_ASSIGN_PERSON_ITEM, mustInput);
        DecimalEdit assignDay = (DecimalEdit)this.getControl(FIELD_ASSIGN_DAYS);
        if (mustInput) {
            boolean enableValidateTime = PermCommonUtil.isEnableValidateTime();
            if (!enableValidateTime) {
                assignDay.setMustInput(false);
                this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap1"});
            } else {
                assignDay.setMustInput(true);
                this.getView().setVisible(Boolean.TRUE, new String[]{"flexpanelap1"});
            }
        } else {
            assignDay.setMustInput(false);
        }
    }

    private void setCancelRequiredField(boolean mustInput) {
        EntryGrid cancelSceneEntry = (EntryGrid)this.getControl(KEY_CANCEL_SCENE_ENTRY);
        cancelSceneEntry.setMustInput(FIELD_CANCEL_ACTYPE, mustInput);
        cancelSceneEntry.setMustInput(FIELD_CANCEL_PERSON_ITEM, mustInput);
    }

    public void afterF7Select(AfterF7SelectEvent event) {
        block5: {
            Object element;
            block4: {
                element = event.getSource();
                if (!(element instanceof BasedataEdit) || !HRStringUtils.equals((String)FIELD_ASSIGN_ACTYPE, (String)((BasedataEdit)element).getKey())) break block4;
                int size = event.getListSelectedRowCollection().size();
                int currentRowIndex = event.getCurrentRowIndex();
                if (size <= 1) break block5;
                for (int i = 0; i < size - 1; ++i) {
                    ++currentRowIndex;
                    if (this.isOnly1010()) continue;
                    ((DynamicObject)this.getModel().getEntryEntity(KEY_ASSIGN_SCENE_ENTRY).get(currentRowIndex)).getDataEntityState().setBizChanged(true);
                    this.getModel().setValue(FIELD_ASSIGN_PERSON_ITEM, null, currentRowIndex);
                }
                break block5;
            }
            if (element instanceof BasedataEdit && HRStringUtils.equals((String)FIELD_CANCEL_ACTYPE, (String)((BasedataEdit)element).getKey())) {
                int size = event.getListSelectedRowCollection().size();
                int currentRowIndex = event.getCurrentRowIndex();
                if (size > 1) {
                    for (int i = 0; i < size - 1; ++i) {
                        ++currentRowIndex;
                        if (this.isOnly1010()) continue;
                        ((DynamicObject)this.getModel().getEntryEntity(KEY_CANCEL_SCENE_ENTRY).get(currentRowIndex)).getDataEntityState().setBizChanged(true);
                        this.getModel().setValue(FIELD_CANCEL_PERSON_ITEM, null, currentRowIndex);
                    }
                }
            }
        }
    }

    static {
        convertParamTypeMap.put("org", ParamTypeEnum.ADMINORG.getValue());
        convertParamTypeMap.put("bd", ParamTypeEnum.DYNAMICOBJECT.getValue());
    }
}
