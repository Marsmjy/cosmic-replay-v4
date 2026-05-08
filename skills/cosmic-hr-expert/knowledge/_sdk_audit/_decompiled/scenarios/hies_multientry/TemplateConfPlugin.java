/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.base.Joiner
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DataEntityState
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.DataEntitySerializer
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.EntityType
 *  kd.bos.entity.LinkEntryType
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.SubEntryType
 *  kd.bos.entity.TreeEntryType
 *  kd.bos.entity.TreeSubEntryType
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.DirtyManager
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FieldTip
 *  kd.bos.form.FieldTip$FieldTipsLevel
 *  kd.bos.form.FieldTip$FieldTipsTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageTypes
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Container
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.AttachmentPanel
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.control.events.RowClickEventListener
 *  kd.bos.form.control.events.TabSelectEvent
 *  kd.bos.form.control.events.TabSelectListener
 *  kd.bos.form.control.events.UploadListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.inte.api.EnabledLang
 *  kd.bos.inte.service.InteServiceImpl
 *  kd.bos.lang.Lang
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.bill.BillModel
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.schedule.api.JobInfo
 *  kd.bos.schedule.api.JobType
 *  kd.bos.schedule.form.JobForm
 *  kd.bos.schedule.form.JobFormInfo
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.DispatchServiceHelper
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.bos.servicehelper.runmode.RunModeServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.common.util.ReflectUtil
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hies.common.constant.HIESConstant
 *  kd.hr.hies.common.constant.MCConfigConstant
 *  kd.hr.hies.common.constant.TemplateConfConst
 *  kd.hr.hies.common.dto.CustomTabPage
 *  kd.hr.hies.common.dto.CustomTabPageId
 *  kd.hr.hies.common.dto.EntityFieldContext
 *  kd.hr.hies.common.dto.Result
 *  kd.hr.hies.common.enu.EnumApplyScopeType
 *  kd.hr.hies.common.util.HIESUtil
 *  kd.hr.hies.common.util.MethodUtil
 *  kd.hr.hies.common.util.TemplateEntityFieldUtil
 *  kd.hr.hies.common.util.TemplateFormCommonUtil
 *  kd.hrmp.hies.multientry.business.template.TplDownLoadTask
 *  kd.hrmp.hies.multientry.common.EntryConstant
 *  kd.hrmp.hies.multientry.common.HiesEntryRes
 *  kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil
 *  kd.hrmp.hies.multientry.common.util.TemplateFormCommonUtil
 *  kd.hrmp.hies.multientry.mservice.api.IMultiEntryTemplateService
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.commons.lang3.SerializationUtils
 */
package kd.hrmp.hies.multientry.formplugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DataEntityState;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.DataEntitySerializer;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.EntityType;
import kd.bos.entity.LinkEntryType;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.SubEntryType;
import kd.bos.entity.TreeEntryType;
import kd.bos.entity.TreeSubEntryType;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.DirtyManager;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FieldTip;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageTypes;
import kd.bos.form.ShowType;
import kd.bos.form.container.Container;
import kd.bos.form.container.Tab;
import kd.bos.form.control.AttachmentPanel;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.control.events.TabSelectEvent;
import kd.bos.form.control.events.TabSelectListener;
import kd.bos.form.control.events.UploadListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.inte.api.EnabledLang;
import kd.bos.inte.service.InteServiceImpl;
import kd.bos.lang.Lang;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.bill.BillModel;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.schedule.api.JobInfo;
import kd.bos.schedule.api.JobType;
import kd.bos.schedule.form.JobForm;
import kd.bos.schedule.form.JobFormInfo;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.bos.servicehelper.runmode.RunModeServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.common.util.ReflectUtil;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hies.common.constant.HIESConstant;
import kd.hr.hies.common.constant.MCConfigConstant;
import kd.hr.hies.common.constant.TemplateConfConst;
import kd.hr.hies.common.dto.CustomTabPage;
import kd.hr.hies.common.dto.CustomTabPageId;
import kd.hr.hies.common.dto.EntityFieldContext;
import kd.hr.hies.common.dto.Result;
import kd.hr.hies.common.enu.EnumApplyScopeType;
import kd.hr.hies.common.util.HIESUtil;
import kd.hr.hies.common.util.MethodUtil;
import kd.hr.hies.common.util.TemplateEntityFieldUtil;
import kd.hrmp.hies.multientry.business.template.TplDownLoadTask;
import kd.hrmp.hies.multientry.common.EntryConstant;
import kd.hrmp.hies.multientry.common.HiesEntryRes;
import kd.hrmp.hies.multientry.common.util.TemplateFormCommonUtil;
import kd.hrmp.hies.multientry.mservice.api.IMultiEntryTemplateService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

@ExcludeFromJacocoGeneratedReport
public final class TemplateConfPlugin
extends HRBaseDataCommonEdit
implements UploadListener,
RowClickEventListener,
TabSelectListener,
BeforeF7SelectListener,
ClickListener,
TemplateConfConst,
EntryConstant {
    private static final Log log = LogFactory.getLog(TemplateConfPlugin.class);
    public static final String ACTION_SELECT_SERVICE_PLUGINS = "closeCallBack_selectServicePlugins";
    public static final String ACTION_CLOSECALLBACK_ORGFIELD = "closeCallBack_orgField";
    public static final String ACTION_CLOSECALLBACK_CONFIGCSS = "closeCallBack_configcss";
    public static final String BTN_CONFIGCSS = "btn_configcss";
    public static final String KEY_BTNOK = "btnok";
    public static final String KEY_BTNCANCEL = "btncancel";
    private static final String TPL_CURRENT_TAB = "tpl_current_tab";
    private static final String FIELD_TEMPLATETYPE = "tmpltype";
    private static final String FIELD_ORG = "orgfield";
    private static final String PARAM_BINDENTITYID = "bindEntityId";
    public static final String BTN_PREVIEW = "btn_preview";
    public static final String BAR_PREVIEW = "bar_preview";
    public static final String LABEL_DOWNTPL = "labelap_downtpl";
    public static final String LABEL_VIEWQUERYENTITY = "labelap_viewqueryentity";
    public static final String LABEL_MANUALLY = "labelap_manually";
    public static final String FIELD_CONF_PANEL = "advconap3";

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        BasedataEdit mainEntityBaseData = (BasedataEdit)this.getView().getControl("entity");
        mainEntityBaseData.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        this.addClickListeners(new String[]{"plugin", "configfieldcond", BTN_PREVIEW, BTN_CONFIGCSS, KEY_BTNOK});
        BasedataEdit orgField = (BasedataEdit)this.getView().getControl(FIELD_ORG);
        orgField.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        this.addClickListeners(new String[]{"advcontoolbarap3", "bdpropkey", "pagepanel", LABEL_DOWNTPL, LABEL_MANUALLY, LABEL_VIEWQUERYENTITY});
        Toolbar fieldToolbar = (Toolbar)this.getControl("toobar_field");
        fieldToolbar.addItemClickListener((ItemClickListener)this);
        Tab tab = (Tab)this.getControl("objtabap");
        tab.addTabSelectListener((TabSelectListener)this);
    }

    public void preOpenForm(PreOpenFormEventArgs e) {
        FormShowParameter formShowParameter = e.getFormShowParameter();
        Map customParams = formShowParameter.getCustomParams();
        Object iscopy = customParams.get("iscopy");
        if (formShowParameter instanceof BaseShowParameter) {
            Object pkId = ((BaseShowParameter)e.getFormShowParameter()).getPkId();
            if (Objects.isNull(pkId)) {
                return;
            }
            DynamicObject dynamicObject = BusinessDataServiceHelper.loadSingle((Object)pkId, (String)"hies_multientry_tpl");
            boolean isImport = "IMPT".equals(dynamicObject.getString(FIELD_TEMPLATETYPE));
            if (iscopy != null) {
                if (isImport) {
                    formShowParameter.setCaption(ResManager.loadKDString((String)"\u590d\u5236\u5355\u636e\u4f53\u5bfc\u5165\u6a21\u677f", (String)HiesEntryRes.TemplateConfPlugin_47.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                } else {
                    formShowParameter.setCaption(ResManager.loadKDString((String)"\u590d\u5236\u5355\u636e\u4f53\u5bfc\u51fa\u6a21\u677f", (String)HiesEntryRes.TemplateConfPlugin_46.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                }
            } else if (isImport) {
                formShowParameter.setCaption(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5355\u636e\u4f53\u5bfc\u5165\u6a21\u677f-%s", (String)HiesEntryRes.TemplateConfPlugin_48.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), dynamicObject.getString("name")));
            } else {
                formShowParameter.setCaption(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5355\u636e\u4f53\u5bfc\u51fa\u6a21\u677f-%s", (String)HiesEntryRes.TemplateConfPlugin_49.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), dynamicObject.getString("name")));
            }
        }
    }

    public void itemClick(ItemClickEvent evt) {
        String itemKey;
        switch (itemKey = evt.getItemKey()) {
            case "btn_configcss": {
                DynamicObject object = (DynamicObject)this.getModel().getValue("entity");
                if (Objects.isNull(object)) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u5b9e\u4f53", (String)HiesEntryRes.TemplateConfPlugin_5.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                    return;
                }
                String entryEntityKey = (String)this.getModel().getValue("entrytype");
                if (StringUtils.isBlank((CharSequence)entryEntityKey)) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u5355\u636e\u4f53", (String)HiesEntryRes.TemplateConfPlugin_4.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                    return;
                }
                this.sortField(this.getView());
                FormShowParameter showParameter = new FormShowParameter();
                showParameter.setFormId("hies_tplfieldstyleconf");
                showParameter.setCustomParam(FIELD_TEMPLATETYPE, this.getModel().getValue(FIELD_TEMPLATETYPE));
                showParameter.setCustomParam("fieldstyle", this.getModel().getValue("fieldstyle"));
                showParameter.setCustomParam("fieldmerge", this.getModel().getValue("fieldmerge"));
                showParameter.getOpenStyle().setShowType(ShowType.Modal);
                showParameter.setCustomParam("entityFieldMap", (Object)JSON.toJSONString((Object)kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.getEntityCssMap((IDataModel)this.getModel(), (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()))));
                showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, ACTION_CLOSECALLBACK_CONFIGCSS));
                this.getView().showForm(showParameter);
                break;
            }
        }
    }

    private boolean tplPreView() {
        DynamicObject dataEntity = this.getModel().getDataEntity(true);
        try {
            HashMap map = Maps.newHashMapWithExpectedSize((int)16);
            AttachmentPanel attachmentPanel = (AttachmentPanel)this.getControl("attpanelapdescsheet");
            List attachmentData = attachmentPanel.getAttachmentData();
            if (!CollectionUtils.isEmpty((Collection)attachmentData)) {
                map.put("sheetTplUrl", ((Map)attachmentData.get(0)).get("url"));
            }
            map.put("formId", this.getView().getFormShowParameter().getFormId());
            map.put("source", "preView");
            map.put("fieldstyle", this.getModelVal("fieldstyle"));
            map.put("fieldmerge", this.getModelVal("fieldmerge"));
            String enittyId = dataEntity.getString("entity.id");
            MainEntityType dataEntityType = MetadataServiceHelper.getDataEntityType((String)enittyId);
            String appId = dataEntityType.getAppId();
            log.info("genPreView_appId={}", (Object)appId);
            String routeAppId = MethodUtil.getRouteAppId((String)appId);
            Map f7CountByTpl = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.computeF7CountByTpl((DynamicObject)dataEntity);
            int maxCount = MCConfigConstant.getMaxF7CountForTplDownload();
            if ((Integer)f7CountByTpl.get("allF7") > maxCount || (Integer)f7CountByTpl.get("queryF7") > 1) {
                map.put("routeAppId", routeAppId);
                map.put("dataEntity", DataEntitySerializer.serializerToString((Object)dataEntity));
                FormShowParameter parameter = new FormShowParameter();
                parameter.setFormId("hies_tpldownprogress");
                parameter.getOpenStyle().setShowType(ShowType.Modal);
                parameter.setCustomParam("taskParam", (Object)JSONObject.toJSONString((Object)map));
                parameter.setCustomParam("taskClass", (Object)TplDownLoadTask.class.getName());
                this.getView().showForm(parameter);
            } else {
                Result result = (Result)DispatchServiceHelper.invokeService((String)"kd.hrmp.hies.multientry.servicehelper", (String)routeAppId, (String)IMultiEntryTemplateService.class.getSimpleName(), (String)"genPreView", (Object[])new Object[]{dataEntity, map});
                if (!ObjectUtils.isEmpty((Object)result)) {
                    if (result.isSuccess()) {
                        this.getView().download((String)result.getData());
                        return true;
                    }
                    this.getView().showErrorNotification(result.getMsg());
                }
            }
        }
        catch (Exception e) {
            this.getView().showErrorNotification(e.getMessage());
        }
        return false;
    }

    private void startJob() {
        JobInfo jobinfo = new JobInfo();
        jobinfo.setAppId("hies");
        jobinfo.setJobType(JobType.REALTIME);
        jobinfo.setRunByUserId(RequestContext.get().getCurrUserId());
        LocaleString name = new LocaleString();
        name.setLocaleValue("\u5bfc\u51fa\u4efb\u52a1");
        jobinfo.setlName(name);
        jobinfo.setId(UUID.randomUUID().toString());
        jobinfo.setTaskClassname(TplDownLoadTask.class.getName());
        Object taskParam = this.getView().getFormShowParameter().getCustomParam("taskParam");
        Map params = (Map)taskParam;
        jobinfo.setParams(params);
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "exportllrl");
        JobFormInfo jobFormlnfo = new JobFormInfo(jobinfo);
        jobFormlnfo.setCaption(ResManager.loadKDString((String)"\u5bfc\u51fa\u4efb\u52a1", (String)"HRMultiEntityExportPlugin.O", (String)"hrmp-hbp - formplugin", (Object[])new Object[0]));
        jobFormlnfo.setCloseCallBack(closeCallBack);
        jobFormlnfo.setCanBackground(false);
        jobFormlnfo.setCanStop(false);
        JobForm.dispatch((JobFormInfo)jobFormlnfo, (IFormView)this.getView());
    }

    public void tabSelected(TabSelectEvent tabSelectEvent) {
        String tabKey = tabSelectEvent.getTabKey();
        this.getPageCache().put(TPL_CURRENT_TAB, tabKey);
    }

    public void afterCreateNewData(EventObject e) {
        this.setDefaultValue();
        this.setDefaultVisible();
        String entityId = (String)this.getView().getFormShowParameter().getCustomParam(PARAM_BINDENTITYID);
        if (StringUtils.isNotBlank((CharSequence)entityId)) {
            this.getView().setVisible(Boolean.TRUE, new String[]{FIELD_CONF_PANEL});
            Container flexPanel = (Container)this.getView().getControl(FIELD_CONF_PANEL);
            flexPanel.setCollapse(false);
            this.getModel().setValue("entity", (Object)entityId);
            this.updateEntryType((DynamicObject)this.getModel().getValue("entity"), true, true);
        }
    }

    private void setDefaultValue() {
        Map customParams = this.getView().getFormShowParameter().getCustomParams();
        String isImport = (String)customParams.get("isImport");
        if ("0".equals(isImport)) {
            this.getModel().setValue(FIELD_TEMPLATETYPE, (Object)"EXPT");
        } else {
            this.getModel().setValue(FIELD_TEMPLATETYPE, (Object)"IMPT");
        }
        this.getModel().setValue("enabledowncond", (Object)"1");
        this.getModel().setValue("importtype", (Object)"updateandnew");
        if ("1".equals(isImport)) {
            this.getView().setFormTitle(new LocaleString(ResManager.loadKDString((String)"\u65b0\u589e\u5355\u636e\u4f53\u5bfc\u5165\u6a21\u677f", (String)HiesEntryRes.TemplateConfPlugin_90.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0])));
        } else {
            this.getView().setFormTitle(new LocaleString(ResManager.loadKDString((String)"\u65b0\u589e\u5355\u636e\u4f53\u5bfc\u51fa\u6a21\u677f", (String)HiesEntryRes.TemplateConfPlugin_91.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0])));
        }
        this.getModel().setValue("instruction", (Object)HIESUtil.getTplInstruction());
        this.getModel().setValue("tplscope", (Object)"standard");
        this.getModel().setValue("allocationpolicy", (Object)"0");
    }

    private void setDefaultVisible() {
        Container baseinfoPanel = (Container)this.getView().getControl("fs_baseinfo");
        baseinfoPanel.setCollapse(false);
        Container advPanel = (Container)this.getView().getControl("advconap6");
        advPanel.setCollapse(false);
        this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap1"});
        Container flexPanel = (Container)this.getView().getControl(FIELD_CONF_PANEL);
        flexPanel.setCollapse(false);
        this.getView().setVisible(Boolean.FALSE, new String[]{"applyscope", "tabap"});
        this.getView().setVisible(Boolean.FALSE, new String[]{EnumApplyScopeType.USER.getValue(), EnumApplyScopeType.ROLE.getValue(), EnumApplyScopeType.ORG.getValue(), EnumApplyScopeType.USER_ORG.getValue()});
        this.getView().setVisible(Boolean.FALSE, new String[]{"createtime"});
        this.getView().setVisible(Boolean.FALSE, new String[]{KEY_BTNOK, KEY_BTNCANCEL});
        String templateType = (String)this.getModel().getValue(FIELD_TEMPLATETYPE);
        boolean isImport = "IMPT".equals(templateType);
        this.setControlVisible(isImport);
        this.getModel().setValue("importtype", (Object)"updateandnew");
    }

    public void afterCopyData(EventObject e) {
        super.afterCopyData(e);
        IFormView view = this.getView();
        IDataModel model = this.getModel();
        InteServiceImpl intService = new InteServiceImpl();
        List enabledLangList = intService.getEnabledLang();
        Lang lang = RequestContext.get().getLang();
        String name = lang.name();
        enabledLangList.sort((x1, x2) -> {
            if (lang.name().equals(x2.getNumber())) {
                return 1;
            }
            if (lang.name().equals(x1.getNumber())) {
                return -1;
            }
            return 0;
        });
        BillShowParameter param = (BillShowParameter)this.getView().getFormShowParameter();
        DynamicObject copyFrom = BusinessDataServiceHelper.loadSingle((Object)param.getPkId(), (String)"hies_multientry_tpl");
        LocaleString copyResSuffix = ResManager.getLocaleString((String)"%s-\u590d\u5236", (String)HiesEntryRes.TemplateConfPlugin_0.resId(), (String)"hrmp-hies-entry");
        OrmLocaleValue oriLocaleValue = (OrmLocaleValue)copyFrom.get("name");
        OrmLocaleValue localeValue = (OrmLocaleValue)SerializationUtils.clone((Serializable)oriLocaleValue);
        kd.hr.hies.common.util.TemplateFormCommonUtil.clearDisabledLang((OrmLocaleValue)localeValue, (List)enabledLangList);
        for (EnabledLang enabledLang : enabledLangList) {
            String number = enabledLang.getNumber();
            String item = oriLocaleValue.getItem(number);
            if (StringUtils.isNotBlank((CharSequence)item)) {
                String copyResSuffixStr = (String)copyResSuffix.get((Object)number);
                if (StringUtils.isBlank((CharSequence)copyResSuffixStr)) {
                    localeValue.setItem(number, null);
                    continue;
                }
                String copyName = String.format(Locale.ROOT, copyResSuffixStr, item);
                localeValue.setItem(number, copyName);
                continue;
            }
            localeValue.setItem(number, item);
        }
        localeValue.setItem("GLang", localeValue.getLocaleValue());
        model.setValue("name", (Object)localeValue);
        this.getView().getFormShowParameter().getCustomParams().put("clickBtn", "copy");
        this.getView().setVisible(Boolean.FALSE, new String[]{"createtime"});
        this.resetView(model);
        kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.buildAndRestoreFieldTree((IFormView)view, (IDataModel)model, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)view), (OperationStatus)OperationStatus.EDIT);
    }

    public void afterLoadData(EventObject e) {
        super.afterLoadData(e);
        DynamicObject entity = (DynamicObject)this.getModel().getValue("entity");
        this.updateEntryType(entity, false, false);
        IFormView view = this.getView();
        OperationStatus status = view.getFormShowParameter().getStatus();
        String btn = (String)view.getFormShowParameter().getCustomParam("btn");
        if ("assign".equals(btn)) {
            this.initAssignPage(view);
            view.setVisible(Boolean.valueOf("1".equals(this.getModel().getValue("allocationpolicy"))), new String[]{"applyscope", "tabap"});
            return;
        }
        view.setVisible(Boolean.FALSE, new String[]{KEY_BTNOK, KEY_BTNCANCEL});
        Container flexPanel = (Container)this.getView().getControl(FIELD_CONF_PANEL);
        flexPanel.setCollapse(false);
        this.getView().getFormShowParameter().getCustomParams().put("clickBtn", "view");
        IDataModel model = this.getModel();
        this.resetView(model);
        kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.buildAndRestoreFieldTree((IFormView)view, (IDataModel)model, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)view), (OperationStatus)status);
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        Object issyspreset = this.getModelVal("issyspreset");
        if (Boolean.TRUE.equals(issyspreset)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"bar_modify"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap5", "advconap", "advconap31"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"advconapcustomtpl", "advconapdescsheet", "advconapexptpl"});
        }
        TemplateFormCommonUtil.displayEnablePersonIdImport((IFormView)this.getView());
        this.getModel().setDataChanged(false);
    }

    private void resetView(IDataModel model) {
        String importType = (String)this.getModel().getValue("importtype");
        this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap1"});
        this.getView().setVisible(Boolean.FALSE, new String[]{EnumApplyScopeType.USER.getValue(), EnumApplyScopeType.ROLE.getValue(), EnumApplyScopeType.ORG.getValue(), EnumApplyScopeType.USER_ORG.getValue()});
        this.activeApplyScopeTab();
        this.getView().setVisible(Boolean.valueOf("1".equals(this.getModel().getValue("allocationpolicy"))), new String[]{"applyscope", "tabap"});
        String templateType = (String)model.getValue(FIELD_TEMPLATETYPE);
        boolean isImport = "IMPT".equals(templateType);
        this.setControlVisible(isImport);
        this.getModel().setValue("importtype", (Object)importType);
        this.getView().setVisible(Boolean.valueOf(!"update".equalsIgnoreCase(importType) && isImport), new String[]{"configfieldcond"});
        DynamicObject entity = (DynamicObject)this.getModel().getValue("entity");
        this.updateEntryType(entity, false, false);
    }

    private void initAssignPage(IFormView view) {
        String btn = (String)this.getView().getFormShowParameter().getCustomParam("btn");
        if ("assign".equals(btn)) {
            view.setVisible(Boolean.valueOf(false), new String[]{"baseinfoap"});
            view.setVisible(Boolean.valueOf(false), new String[]{"titleapanel"});
            HashMap map = Maps.newHashMapWithExpectedSize((int)1);
            map.put("hideNav", true);
            HashMap scopeCssMap = Maps.newHashMapWithExpectedSize((int)1);
            scopeCssMap.put("bc", "white");
            this.getView().updateControlMetadata("tpltabap", (Map)map);
            this.getView().updateControlMetadata("flexpanelap3", (Map)scopeCssMap);
            this.getView().updateControlMetadata("hies_multientry_tpl", (Map)scopeCssMap);
            this.getView().updateControlMetadata("tabpageap2", (Map)scopeCssMap);
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{KEY_BTNOK, KEY_BTNCANCEL});
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String fieldKey = beforeF7SelectEvent.getProperty().getName();
        if (StringUtils.equals((CharSequence)fieldKey, (CharSequence)FIELD_ORG)) {
            ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, ACTION_CLOSECALLBACK_ORGFIELD));
            this.getPageCache().put(FIELD_TEMPLATETYPE, (String)this.getModel().getValue(FIELD_TEMPLATETYPE));
        } else if (StringUtils.equals((CharSequence)fieldKey, (CharSequence)"entity")) {
            ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            ListFilterParameter listFilterParameter = showParameter.getListFilterParameter();
            QFilter modelTypeFilter = new QFilter("modeltype", "in", Arrays.asList("BaseFormModel", "BillFormModel"));
            QFilter isTemplateFilter = new QFilter("istemplate", "=", (Object)Boolean.FALSE);
            QFilter enableImportFilter = new QFilter("enableimport", "=", (Object)Boolean.TRUE);
            QFilter[] filters = new QFilter[]{modelTypeFilter, isTemplateFilter, enableImportFilter};
            filters = RunModeServiceHelper.getEntityIdBlacklistFilters((QFilter[])filters, (String)"dentityid");
            listFilterParameter.setQFilters(Arrays.asList(filters));
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "dopreview": {
                args.setCancel(true);
                this.getView().getFormShowParameter().getCustomParams().put("clickBtn", "dopreview");
                List<String> tplConfErrMsg = this.checkPreViewTpl();
                if (!CollectionUtils.isEmpty(tplConfErrMsg)) {
                    if (tplConfErrMsg.size() == 1) {
                        this.getView().showErrorNotification(tplConfErrMsg.get(0));
                    } else if (tplConfErrMsg.size() > 1) {
                        this.getView().showMessage(ResManager.loadKDString((String)"\u9884\u89c8\u6a21\u677f\u5931\u8d25", (String)HiesEntryRes.TemplateConfPlugin_21.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), Joiner.on((String)"\r\n").join(tplConfErrMsg), MessageTypes.Default);
                    }
                    return;
                }
                if (this.checkCusField()) {
                    return;
                }
                DynamicObject dataEntity = this.getModel().getDataEntity(true);
                DirtyManager dirtyManager = (DirtyManager)ReflectUtil.newInstance(DirtyManager.class, (Object[])new Object[]{this.getModel()});
                Boolean isBizChanged = (Boolean)ReflectUtil.invoke((Object)dirtyManager, (String)"isBizChanged", (Object[])new Object[]{dataEntity});
                DataEntityState dataEntityState = dataEntity.getDataEntityState();
                Boolean removedItems = dataEntityState.getRemovedItems();
                this.sortField(this.getView());
                this.getView().setEnable(Boolean.FALSE, new String[]{BAR_PREVIEW});
                this.getView().updateView(BAR_PREVIEW);
                this.tplPreView();
                this.getView().setEnable(Boolean.TRUE, new String[]{BAR_PREVIEW});
                this.getView().updateView(BAR_PREVIEW);
                if (!Boolean.TRUE.equals(isBizChanged)) {
                    this.getModel().setDataChanged(false);
                    break;
                }
                if (Boolean.TRUE.equals(removedItems)) break;
                dataEntityState.setRemovedItems(Boolean.valueOf(false));
                break;
            }
            case "modify": 
            case "next": 
            case "previous": 
            case "first": 
            case "last": {
                this.removeAllTabpage(TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()));
                break;
            }
            case "save_btnok": {
                IDataModel model2 = this.getModel();
                String allocationPolicy = (String)model2.getValue("allocationpolicy");
                if (!"1".equals(allocationPolicy)) break;
                String applyScopeStr = this.getApplyScopeStr(model2);
                if (StringUtils.isBlank((CharSequence)applyScopeStr)) {
                    this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6a21\u677f\u4f7f\u7528\u8303\u56f4\u3002", (String)HiesEntryRes.TemplateConfPlugin_6.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                    args.setCancel(true);
                }
                model2.setValue("applyscope", (Object)applyScopeStr);
                break;
            }
            case "save": 
            case "saveandnew": {
                DynamicObject bizobject;
                String mainEntityNumber;
                String number;
                if (this.checkTemplate()) {
                    args.setCancel(true);
                    return;
                }
                if (this.checkCusField()) {
                    args.setCancel(true);
                    return;
                }
                if (this.checkDefField()) {
                    args.setCancel(true);
                    return;
                }
                IDataModel model = this.getModel();
                String allocationPolicy2 = (String)model.getValue("allocationpolicy");
                if ("1".equals(allocationPolicy2)) {
                    String applyScopeStr = this.getApplyScopeStr(model);
                    if (StringUtils.isBlank((CharSequence)applyScopeStr)) {
                        this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6a21\u677f\u4f7f\u7528\u8303\u56f4\u3002", (String)HiesEntryRes.TemplateConfPlugin_6.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                        args.setCancel(true);
                    }
                    model.setValue("applyscope", (Object)applyScopeStr);
                }
                if (HRStringUtils.equals((String)(number = (String)model.getValue("number")), (String)(mainEntityNumber = (bizobject = (DynamicObject)model.getValue("entity")).getString("number")))) {
                    this.getView().showTipNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u201c\u7f16\u7801\u201d \u503c \u201c%s\u201d \u5df2\u5b58\u5728\uff0c\u8bf7\u8f93\u5165\u5176\u4ed6\u503c\u3002", (String)HiesEntryRes.TemplateConfPlugin_12.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), mainEntityNumber));
                    args.setCancel(true);
                }
                this.getView().getFormShowParameter().getCustomParams().put("clickBtn", "save");
                this.initDefFieldVal(this.getView());
                this.sortField(this.getView());
                break;
            }
        }
    }

    private boolean checkDefField() {
        IFormView view = this.getView();
        LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)view);
        ArrayList fieldNumberList = Lists.newArrayListWithCapacity((int)16);
        for (Map.Entry entryMap : allEntity.entrySet()) {
            String[] entityAndEntryArr = ((String)entryMap.getKey()).split(":");
            String tabPageKey = entityAndEntryArr[1];
            CustomTabPageId customTabPageId = TemplateFormCommonUtil.getTabPageIdCache((IFormView)view, (String)tabPageKey);
            if (ObjectUtils.isEmpty((Object)customTabPageId)) continue;
            IFormView childView = view.getView(customTabPageId.getTabPageId());
            IDataModel model = childView.getModel();
            DynamicObjectCollection entryEntity = model.getEntryEntity("cusfieldentryentity");
            for (DynamicObject dynamicObject : entryEntity) {
                String fieldDefProp = dynamicObject.getString("deffieldvalprop");
                String fieldNumber = dynamicObject.getString("deffieldnumber");
                if (!StringUtils.isBlank((CharSequence)fieldDefProp) || !StringUtils.isNotBlank((CharSequence)fieldNumber)) continue;
                fieldNumberList.add(fieldNumber);
            }
        }
        if (!CollectionUtils.isEmpty((Collection)fieldNumberList)) {
            String fieldNumber = Joiner.on((String)",").join((Iterable)fieldNumberList);
            String message = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u201c%s\u201d\u5b57\u6bb5\u9ed8\u8ba4\u503c\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)HiesEntryRes.TemplateConfPlugin_65.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), fieldNumber);
            this.getView().showErrorNotification(message);
            return true;
        }
        return false;
    }

    private void initDefFieldVal(IFormView view) {
        IDataModel model = view.getModel();
        DynamicObjectCollection entryEntity = model.getEntryEntity("tpltreeentryentity");
        Iterator iterator = entryEntity.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map f7BdFormatConfig;
            Map formatConfigI;
            DynamicObject dynamicObject = (DynamicObject)iterator.next();
            String fieldnumber = dynamicObject.getString("fieldnumber");
            String entityNumber = dynamicObject.getString("childentity");
            String defValProp = dynamicObject.getString("defvalprop");
            if (StringUtils.isNotBlank((CharSequence)defValProp) && (formatConfigI = (Map)(f7BdFormatConfig = TemplateEntityFieldUtil.getImportF7BdFormatConfig((String)entityNumber)).get(fieldnumber)) != null) {
                String newVal = (String)formatConfigI.get("val");
                model.setValue("imptattr", (Object)newVal, i);
            }
            ++i;
        }
    }

    private void sortField(IFormView view) {
        JSONObject fieldOrder = this.getFieldOrder(view);
        kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.cacheCusField((IFormView)view);
        this.rebuidTplFieldPage(fieldOrder);
    }

    private boolean checkCusField() {
        boolean isImport = "IMPT".equals(this.getModel().getValue(FIELD_TEMPLATETYPE));
        String importType = (String)this.getModel().getValue("importtype");
        boolean newCanEdit = "new".equals(importType) || "updateandnew".equals(importType);
        JSONObject fieldOrder = this.getFieldOrder(this.getView());
        LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)this.getView());
        HashMap<String, List> candidateFieldsMap = new HashMap<String, List>(allEntity.size());
        HashMap<String, List> defFieldsMap = new HashMap<String, List>(allEntity.size());
        for (Map.Entry entryMap : allEntity.entrySet()) {
            String defFieldsStr;
            String[] entityAndEntryArr = ((String)entryMap.getKey()).split(":");
            String tabPageKey = entityAndEntryArr[1];
            String candidateFieldsStr = fieldOrder.getString(tabPageKey);
            if (StringUtils.isNotBlank((CharSequence)candidateFieldsStr)) {
                List candidateFields = kd.bos.dataentity.serialization.SerializationUtils.fromJsonStringToList((String)candidateFieldsStr, String.class);
                candidateFieldsMap.put(tabPageKey, candidateFields);
            }
            if (!StringUtils.isNotBlank((CharSequence)(defFieldsStr = fieldOrder.getString(tabPageKey + "deffieldnumber")))) continue;
            List defFields = kd.bos.dataentity.serialization.SerializationUtils.fromJsonStringToList((String)defFieldsStr, String.class);
            defFieldsMap.put(tabPageKey, defFields);
        }
        DynamicObjectCollection collection = this.getModel().getEntryEntity("tpltreeentryentity");
        ArrayList toCheckField = Lists.newArrayListWithExpectedSize((int)16);
        for (DynamicObject dynamicObject : collection) {
            String entryNumber = dynamicObject.getString("entrykey");
            String fieldNumber = dynamicObject.getString("fieldnumber");
            String fieldName = dynamicObject.getString("fieldname");
            boolean isCusField = dynamicObject.getBoolean("iscusfield");
            boolean isField = dynamicObject.getBoolean("isfield");
            boolean isMustInputField = StringUtils.isNotBlank((CharSequence)fieldNumber) && StringUtils.isNotBlank((CharSequence)fieldName) && fieldName.contains("*");
            Long pkValue = dynamicObject.getLong("id");
            List candidateFields = (List)candidateFieldsMap.get(entryNumber);
            if (ObjectUtils.isEmpty((Object)candidateFields)) continue;
            List defFields = (List)defFieldsMap.get(entryNumber);
            if (isImport && !isCusField && newCanEdit && isMustInputField && !candidateFields.contains(fieldNumber) && !defFields.contains(fieldNumber)) {
                String notificationName = fieldName;
                if (StringUtils.isNotBlank((CharSequence)fieldName) && fieldName.contains("*")) {
                    notificationName = notificationName.replace("*", "");
                }
                toCheckField.add(notificationName + "(" + fieldNumber + ")");
            }
            if (!isCusField) continue;
            if (StringUtils.isBlank((CharSequence)fieldNumber) && isField) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6a21\u677f\u5b57\u6bb5\u914d\u7f6e\u4e2d\u81ea\u5b9a\u4e49\u5b57\u6bb5\u7684\u201c\u5b57\u6bb5\u7f16\u7801\u201d\u3002", (String)HiesEntryRes.TemplateConfPlugin_26.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                return true;
            }
            if (StringUtils.isBlank((CharSequence)fieldName) && isField) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6a21\u677f\u5b57\u6bb5\u914d\u7f6e\u4e2d\u81ea\u5b9a\u4e49\u5b57\u6bb5\u7684\u201c\u5b57\u6bb5\u540d\u79f0\u201d\u3002", (String)HiesEntryRes.TemplateConfPlugin_28.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                return true;
            }
            if (HRStringUtils.isNotEmpty((String)fieldNumber) && !HIESConstant.CUSTOM_NUMBER_PATTERN.matcher(fieldNumber).matches()) {
                this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u81ea\u5b9a\u4e49\u5b57\u6bb5\u201c\u5b57\u6bb5\u7f16\u7801\u201d\u201c%s\u201d\u4e0d\u5408\u6cd5\uff0c\u4ec5\u652f\u6301\u5b57\u6bcd\u3001\u4e0b\u5212\u7ebf\u53ca\u6570\u5b57\uff0c\u4e14\u4e0d\u5f97\u4ee5\u6570\u5b57\u5f00\u5934\u3002", (String)HiesEntryRes.TemplateConfPlugin_70.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), fieldNumber));
                return true;
            }
            if (HRStringUtils.isNotEmpty((String)fieldName) && !HIESConstant.CUSTOM_NAME_PATTERN.matcher(fieldName).matches()) {
                this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u81ea\u5b9a\u4e49\u5b57\u6bb5\u201c\u5b57\u6bb5\u540d\u79f0\u201d\u201c%s\u201d\u4e0d\u5408\u6cd5\uff0c\u4e0d\u652f\u6301\u9664\u4e0b\u5212\u7ebf\u4ee5\u5916\u7684\u5176\u4ed6\u7279\u6b8a\u5b57\u7b26\u3002", (String)HiesEntryRes.TemplateConfPlugin_71.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), fieldName));
                return true;
            }
            for (DynamicObject dynamicObject2 : this.getModel().getEntryEntity("tpltreeentryentity")) {
                String fieldNumber2 = dynamicObject2.getString("fieldnumber");
                Long pkValue2 = dynamicObject2.getLong("id");
                if (!HRStringUtils.equals((String)fieldNumber2, (String)fieldNumber) || !isField || pkValue2.equals(pkValue)) continue;
                this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6a21\u677f\u5b57\u6bb5\u914d\u7f6e\u4e2d\u5b57\u6bb5\u7f16\u7801\u201c%1$s\u201d\u91cd\u590d\uff0c\u8bf7\u68c0\u67e5\u3002", (String)HiesEntryRes.TemplateConfPlugin_27.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), fieldNumber));
                return true;
            }
        }
        if (!CollectionUtils.isEmpty((Collection)toCheckField)) {
            String fieldNumber = Joiner.on((String)",").join((Iterable)toCheckField);
            String message = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u68c0\u6d4b\u5230\u201c%s\u201d\u5b57\u6bb5\u662f\u5fc5\u586b\u9879\uff0c\u4f46\u5728\u201c\u6a21\u677f\u5b57\u6bb5\u914d\u7f6e\u201d\u4e2d\u672a\u5f00\u542f\u201c\u5fc5\u5f55\u9879\u201d\uff0c\u5728\u201c\u5b57\u6bb5\u9ed8\u8ba4\u503c\u914d\u7f6e\u201d\u4e2d\u4e5f\u6ca1\u6709\u7ef4\u62a4\uff1b\u8bf7\u786e\u8ba4\u8be5\u5b57\u6bb5\u662f\u5426\u6709\u989d\u5916\u7684\u5f00\u53d1\u903b\u8f91\u5904\u7406\uff0c\u5426\u5219\u6570\u636e\u65e0\u6cd5\u6210\u529f\u5b58\u50a8\u3002", (String)HiesEntryRes.TemplateConfPlugin_50.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), fieldNumber);
            this.getView().showTipNotification(message, Integer.valueOf(3000));
        }
        return false;
    }

    private JSONObject getFieldOrder(IFormView view) {
        JSONObject fieldOrder = new JSONObject();
        LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)view);
        for (Map.Entry entryMap : allEntity.entrySet()) {
            String[] entityAndEntryArr = ((String)entryMap.getKey()).split(":");
            String tabPageKey = entityAndEntryArr[1];
            CustomTabPageId customTabPageId = TemplateFormCommonUtil.getTabPageIdCache((IFormView)view, (String)tabPageKey);
            ArrayList fieldNumberList = Lists.newArrayListWithCapacity((int)16);
            Set defFieldNumberList = Sets.newHashSetWithExpectedSize((int)16);
            if (!ObjectUtils.isEmpty((Object)customTabPageId)) {
                IFormView childView = view.getView(customTabPageId.getTabPageId());
                IDataModel model = childView.getModel();
                DynamicObjectCollection entryEntity = model.getEntryEntity("treeentryentity");
                for (DynamicObject dynamicObject : entryEntity) {
                    String fieldNumber = dynamicObject.getString("fieldnumber");
                    boolean isField = dynamicObject.getBoolean("isfield");
                    if (!isField) continue;
                    fieldNumberList.add(fieldNumber);
                }
                defFieldNumberList = TemplateFormCommonUtil.getAllListIdSet((IDataModel)model, (String)"cusfieldentryentity", (String)"deffieldnumber");
            }
            fieldOrder.put(tabPageKey, (Object)JSON.toJSONString((Object)fieldNumberList));
            fieldOrder.put(tabPageKey + "deffieldnumber", (Object)JSON.toJSONString((Object)defFieldNumberList));
        }
        return fieldOrder;
    }

    private boolean checkTemplate() {
        boolean forceCheck;
        DynamicObject dataEntity = this.getModel().getDataEntity();
        ArrayList msgList = Lists.newArrayListWithExpectedSize((int)4);
        String number = dataEntity.getString("number");
        if (HRStringUtils.isNotEmpty((String)number) && (HIESConstant.SpecCharPattern.matcher(number).find() || HIESConstant.CHINESE_PATTERN.matcher(number).find())) {
            msgList.add(ResManager.loadKDString((String)"\u7f16\u7801\u4e0d\u5408\u6cd5\uff0c\u4ec5\u652f\u6301\u8f93\u5165\u5b57\u6bcd\u3001\u4e0b\u5212\u7ebf\u53ca\u6570\u5b57\u3002", (String)HiesEntryRes.TemplateConfPlugin_62.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
        }
        ILocaleString name = dataEntity.getLocaleString("name");
        ArrayList<String> errLangStrs = new ArrayList<String>(4);
        for (Map.Entry entryName : name.entrySet()) {
            String langKey = (String)entryName.getKey();
            if ("GLang".equals(langKey) || !HRStringUtils.isNotEmpty((String)((String)entryName.getValue())) || !HIESConstant.TPL_NAME_PATTERN.matcher((CharSequence)entryName.getValue()).find()) continue;
            errLangStrs.add(langKey);
        }
        if (!CollectionUtils.isEmpty(errLangStrs)) {
            HRBaseServiceHelper langService = new HRBaseServiceHelper("inte_enabledlanguage");
            Object[] dynamicObjects = langService.query("number, languagename", new QFilter[]{QFilter.of((String)"1 = 1", (Object[])new Object[0])});
            Set languagNames = new HashSet(0);
            if (ArrayUtils.isNotEmpty((Object[])dynamicObjects)) {
                languagNames = Arrays.stream(dynamicObjects).filter(item -> errLangStrs.contains(item.getString("number"))).map(item -> item.getString("name")).collect(Collectors.toCollection(LinkedHashSet::new));
            }
            if (CollectionUtils.isEmpty(languagNames)) {
                msgList.add(ResManager.loadKDString((String)"\u6a21\u677f\u540d\u79f0\u4e0d\u5408\u6cd5\uff0c\u4e0d\u652f\u6301\u9664\u201c\u4e0b\u5212\u7ebf\u3001\u6a2a\u7ebf\u3001\u62ec\u53f7\u201d\u4ee5\u5916\u7684\u5176\u4ed6\u7279\u6b8a\u5b57\u7b26\u3002", (String)"TemplateConfPlugin_63", (String)"hrmp-hies-import", (Object[])new Object[0]));
            } else if (languagNames.size() == 1 && errLangStrs.contains(RequestContext.get().getLang().toString())) {
                msgList.add(ResManager.loadKDString((String)"\u6a21\u677f\u540d\u79f0\u4e0d\u5408\u6cd5\uff0c\u4e0d\u652f\u6301\u9664\u201c\u4e0b\u5212\u7ebf\u3001\u6a2a\u7ebf\u3001\u62ec\u53f7\u201d\u4ee5\u5916\u7684\u5176\u4ed6\u7279\u6b8a\u5b57\u7b26\u3002", (String)"TemplateConfPlugin_63", (String)"hrmp-hies-import", (Object[])new Object[0]));
            } else {
                msgList.add(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6a21\u677f\u540d\u79f0\u4e0d\u5408\u6cd5\uff0c\u201c%s\u201d\u6a21\u5f0f\u4e0d\u652f\u6301\u9664\u201c\u4e0b\u5212\u7ebf\u3001\u6a2a\u7ebf\u3001\u62ec\u53f7\u201d\u4ee5\u5916\u7684\u5176\u4ed6\u7279\u6b8a\u5b57\u7b26\u3002", (String)"TemplateConfPlugin_60", (String)"hrmp-hies-import", (Object[])new Object[0]), String.join((CharSequence)"\u3001", languagNames)));
            }
        }
        if (forceCheck = false) {
            FieldTip fieldTip;
            if (HRStringUtils.isEmpty((String)number)) {
                msgList.add(ResManager.loadKDString((String)"\u201c\u7f16\u7801\u201d", (String)HiesEntryRes.TemplateConfPlugin_22.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "number", ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)HiesEntryRes.TemplateConfPlugin_15.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                this.getView().showFieldTip(fieldTip);
            }
            if (HRStringUtils.isEmpty((String)name.toString())) {
                msgList.add(ResManager.loadKDString((String)"\u201c\u6a21\u677f\u540d\u79f0\u201d", (String)HiesEntryRes.TemplateConfPlugin_23.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "name", ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)HiesEntryRes.TemplateConfPlugin_15.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                this.getView().showFieldTip(fieldTip);
            }
            if (ObjectUtils.isEmpty((Object)dataEntity.get("entity"))) {
                msgList.add(ResManager.loadKDString((String)"\u201c\u5b9e\u4f53\u201d", (String)HiesEntryRes.TemplateConfPlugin_24.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "entity", ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)HiesEntryRes.TemplateConfPlugin_15.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
                this.getView().showFieldTip(fieldTip);
            }
        }
        if (!CollectionUtils.isEmpty((Collection)msgList)) {
            if (msgList.size() == 1) {
                this.getView().showErrorNotification((String)msgList.get(0));
                return true;
            }
            if (msgList.size() > 1) {
                this.getView().showMessage(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25", (String)HiesEntryRes.TemplateConfPlugin_66.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), Joiner.on((String)"\r\n").join((Iterable)msgList), MessageTypes.Default);
                return true;
            }
        }
        return false;
    }

    public void propertyChanged(PropertyChangedArgs args) {
        IDataModel model = this.getModel();
        String propertyName = args.getProperty().getName();
        ChangeData[] changeSet = args.getChangeSet();
        String value = (String)model.getValue(FIELD_TEMPLATETYPE);
        boolean isImport = "IMPT".equalsIgnoreCase(value);
        String downloadCond = (String)model.getValue("enabledowncond");
        boolean isDownloadCond = "1".equals(downloadCond);
        boolean isOpen = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.mustInputIsOpen((IDataModel)model);
        boolean canEdit = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)model);
        boolean baseCanEdit = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.baseCanEdit((IDataModel)model);
        String uniqueval = "";
        DynamicObject bizobject = (DynamicObject)model.getValue("entity");
        switch (propertyName) {
            case "entity": {
                this.getView().setVisible(Boolean.TRUE, new String[]{FIELD_CONF_PANEL});
                Container flexPanel = (Container)this.getView().getControl(FIELD_CONF_PANEL);
                flexPanel.setCollapse(false);
                DynamicObject oldMainValue = (DynamicObject)changeSet[0].getOldValue();
                DynamicObject newValue = (DynamicObject)changeSet[0].getNewValue();
                if (Objects.isNull(oldMainValue) && Objects.nonNull(newValue)) {
                    String newNumber = newValue.getString("number");
                    this.getModel().setValue("entity", (Object)newNumber);
                } else if (Objects.isNull(newValue) && Objects.nonNull(oldMainValue)) {
                    String oldId = oldMainValue.getString("id");
                    this.getModel().setValue("entity", (Object)"");
                    TemplateFormCommonUtil.removeTabPageAndEntry((IFormView)this.getView(), (String)oldId);
                } else if (Objects.nonNull(oldMainValue) && Objects.nonNull(newValue)) {
                    String newNumber = newValue.getString("number");
                    String oldId = oldMainValue.getString("id");
                    TemplateFormCommonUtil.removeTabPageAndEntry((IFormView)this.getView(), (String)oldId);
                    TemplateFormCommonUtil.removeAllTabPageExEntity((IFormView)this.getView());
                    this.getModel().setValue("entity", (Object)newNumber);
                }
                this.getModel().setValue("fieldstyle", (Object)"");
                this.getModel().setValue("fieldmerge", (Object)"");
                this.updateEntryType(newValue, true, true);
                TemplateFormCommonUtil.displayEnablePersonIdImport((IFormView)this.getView());
                break;
            }
            case "entrytype": {
                Set curSelectEntryKeys;
                if (Objects.isNull(bizobject)) break;
                String oldMainValue2 = (String)changeSet[0].getOldValue();
                String newId = bizobject.getString("id");
                String newValue2 = (String)changeSet[0].getNewValue();
                if (Objects.isNull(newValue2) && Objects.isNull(oldMainValue2)) break;
                String entityName = bizobject.getString("name");
                String entityId = bizobject.getString("number");
                MainEntityType newMainType = EntityMetadataCache.getDataEntityType((String)entityId);
                Set entries = newMainType.getAllEntities().entrySet();
                HashMap entryMap = new HashMap(entries.size());
                for (Map.Entry entry : entries) {
                    if (entityId.equals(entry.getKey())) continue;
                    entryMap.put(entry.getKey(), entry.getValue());
                }
                if (Objects.isNull(oldMainValue2)) {
                    model.deleteEntryData("entrylist");
                    TableValueSetter vs = new TableValueSetter(new String[0]);
                    vs.addField("entryname", new Object[0]);
                    vs.addField("entrynumber", new Object[0]);
                    curSelectEntryKeys = Arrays.stream(newValue2.split(",")).filter(StringUtils::isNotBlank).collect(Collectors.toCollection(LinkedHashSet::new));
                    for (Object curSelectEntryKey : curSelectEntryKeys) {
                        String entryNumber = curSelectEntryKey;
                        String entryName = ((EntityType)entryMap.get(curSelectEntryKey)).getDisplayName().toString();
                        TemplateFormCommonUtil.addTabPage((IFormView)this.getView(), (CustomTabPage)new CustomTabPage(entryNumber, entryName), (String)entityId, (boolean)isImport, (boolean)isDownloadCond, (Boolean)isOpen, (Boolean)Boolean.TRUE, (Boolean)canEdit, (Boolean)baseCanEdit, (String)uniqueval, null, (OperationStatus)OperationStatus.EDIT);
                        EntityFieldContext entityFieldContext = new EntityFieldContext(this.getView(), entityId, "tpltreeentryentity", isImport, isOpen, canEdit, baseCanEdit, uniqueval);
                        entityFieldContext.setEntryNumber(entryNumber);
                        kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.newInstance().addTreeEntryRow(entityFieldContext);
                        vs.addRow(new Object[]{entryName, entryNumber});
                    }
                    model.beginInit();
                    int[] rows = ((AbstractFormDataModel)model).batchCreateNewEntryRow("entrylist", vs);
                    for (Object row : (Object)rows) {
                        DynamicObject entryDyn = ((BillModel)model).getEntryEntity("entrylist", (int)row);
                        entryDyn.getDataEntityState().setBizChanged(true);
                        entryDyn.getDataEntityState().setBizChangeFlags(new long[]{1L});
                    }
                    model.endInit();
                    this.getView().updateView("entrylist");
                } else {
                    DynamicObject entryDyn;
                    boolean isSelectEntry = Objects.nonNull(newValue2);
                    if (!isSelectEntry) {
                        model.deleteEntryData("entrylist");
                        TemplateFormCommonUtil.removeAllTabPage((IFormView)this.getView());
                        return;
                    }
                    curSelectEntryKeys = Arrays.stream(newValue2.split(",")).filter(StringUtils::isNotBlank).collect(Collectors.toCollection(LinkedHashSet::new));
                    Set preSelectEntryKeys = Arrays.stream(oldMainValue2.split(",")).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
                    if (!CollectionUtils.isEmpty(preSelectEntryKeys)) {
                        DynamicObjectCollection entryDyns = model.getEntryEntity("entrylist");
                        for (int i = 0; i < entryDyns.size(); ++i) {
                            entryDyn = (DynamicObject)entryDyns.get(i);
                            String entryNumber = entryDyn.getString("entrynumber");
                            if (!preSelectEntryKeys.contains(entryNumber) || curSelectEntryKeys.contains(entryNumber)) continue;
                            model.deleteEntryRow("entrylist", i);
                            TemplateFormCommonUtil.removeTabPageAndEntry((IFormView)this.getView(), (String)entryNumber);
                        }
                    }
                    curSelectEntryKeys.removeAll(preSelectEntryKeys);
                    entryDyn = curSelectEntryKeys.iterator();
                    while (entryDyn.hasNext()) {
                        String curSelectEntryKey;
                        String entryNumber = curSelectEntryKey = (String)entryDyn.next();
                        LocaleString entryName = ((EntityType)entryMap.get(curSelectEntryKey)).getDisplayName();
                        TemplateFormCommonUtil.addTabPage((IFormView)this.getView(), (CustomTabPage)new CustomTabPage(entryNumber, entryName.getLocaleValue()), (String)entityId, (boolean)isImport, (boolean)isDownloadCond, (Boolean)isOpen, (Boolean)Boolean.TRUE, (Boolean)canEdit, (Boolean)baseCanEdit, (String)uniqueval, null, (OperationStatus)OperationStatus.EDIT);
                        EntityFieldContext entityFieldContext = new EntityFieldContext(this.getView(), entityId, "tpltreeentryentity", isImport, isOpen, canEdit, baseCanEdit, uniqueval);
                        entityFieldContext.setEntryNumber(entryNumber);
                        kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.newInstance().addTreeEntryRow(entityFieldContext);
                        DynamicObject entryDyn2 = model.getEntryEntity("entrylist").addNew();
                        entryDyn2.set("entryname", (Object)((EntityType)entryMap.get(curSelectEntryKey)).getDisplayName());
                        entryDyn2.set("entrynumber", (Object)curSelectEntryKey);
                    }
                    this.getModel().updateEntryCache(model.getEntryEntity("entrylist"));
                    this.getView().updateView("entrylist");
                }
                this.setNullValue(true);
                break;
            }
            case "enabledowncond": {
                break;
            }
            case "allocationpolicy": {
                this.activeApplyScopeTab();
                break;
            }
            case "applyscope": {
                break;
            }
            case "importtype": {
                StringBuilder builder;
                String newImportType = (String)changeSet[0].getNewValue();
                boolean newCanEdit = !"new".equals(newImportType) && !"updateandnew".equals(newImportType);
                this.getView().setVisible(Boolean.valueOf(!"update".equalsIgnoreCase(newImportType) && isImport), new String[]{"configfieldcond"});
                String tip3 = ResManager.loadKDString((String)"3.\u5982\u9700\u66f4\u65b0\u5df2\u6709\u6570\u636e\uff0c\u8bf7\u5148\u6309\u5bfc\u5165\u6a21\u677f\u5bfc\u51fa\u6570\u636e\uff0c\u4fee\u6539\u540e\u518d\u6b21\u5bfc\u5165\u3002", (String)HiesEntryRes.TemplateConfPlugin_35.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]);
                if ("update".equals(newImportType) || "updateandnew".equals(newImportType)) {
                    builder = new StringBuilder(HIESUtil.getTplInstruction2());
                    builder.append(HIESUtil.getSysLineSeparator());
                    builder.append(tip3);
                    this.getModel().setValue("instruction", (Object)builder.toString());
                } else {
                    builder = new StringBuilder(HIESUtil.getTplInstruction());
                    this.getModel().setValue("instruction", (Object)builder.toString());
                }
                if (Objects.isNull(bizobject)) break;
                LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)this.getView());
                for (Map.Entry entryMap2 : allEntity.entrySet()) {
                    String[] split = ((String)entryMap2.getKey()).split(":");
                    String entryNumber = split[1];
                    String mustInputField = TemplateFormCommonUtil.getEntityMustInputField((IDataModel)model, (String)entryNumber);
                    String metaMustInputField = TemplateFormCommonUtil.getEntityMetaMustInputField((IDataModel)model, (String)entryNumber);
                    TemplateFormCommonUtil.refreshTabPage((IFormView)this.getView(), (String)entryNumber, (String)metaMustInputField, (String)"", (Boolean)newCanEdit);
                    TemplateFormCommonUtil.refreshTabPage((IFormView)this.getView(), (String)entryNumber, (String)mustInputField, (String)"", (Boolean)Boolean.FALSE);
                }
                TemplateFormCommonUtil.activeMainEntityTabPage((IFormView)this.getView());
                break;
            }
        }
    }

    private void updateEntryType(DynamicObject entityDyn, boolean isInit, boolean isChangeEntity) {
        if (Objects.isNull(entityDyn)) {
            this.getModel().setValue("entrytype", null);
            ComboEdit comboEdit = (ComboEdit)this.getControl("entrytype");
            comboEdit.setComboItems(new ArrayList(0));
            return;
        }
        String newNumber = entityDyn.getString("number");
        MainEntityType newMainType = EntityMetadataCache.getDataEntityType((String)newNumber);
        String mainEntityNumber = newMainType.getName();
        Set entries = newMainType.getAllEntities().entrySet();
        if (isChangeEntity) {
            this.getModel().setValue("entrytype", null);
            ComboEdit comboEdit = (ComboEdit)this.getControl("entrytype");
            comboEdit.setComboItems(new ArrayList(0));
            if (entries.size() == 1) {
                return;
            }
        }
        ArrayList<ComboItem> comboItems = new ArrayList<ComboItem>(4);
        for (Map.Entry entry : entries) {
            String entryNumber;
            EntityType entityType = (EntityType)entry.getValue();
            if (entityType instanceof SubEntryType || entityType instanceof TreeEntryType || entityType instanceof TreeSubEntryType || entityType instanceof LinkEntryType || mainEntityNumber.equals(entryNumber = entityType.getName())) continue;
            ComboItem newItem = new ComboItem();
            newItem.setCaption(new LocaleString(entryNumber.concat("_").concat(entityType.getDisplayName().toString())));
            newItem.setValue(entryNumber);
            comboItems.add(newItem);
        }
        ComboEdit comboEdit = (ComboEdit)this.getControl("entrytype");
        comboEdit.setComboItems(comboItems);
        if (!isInit) {
            this.getModel().setValue("entrytype", this.getModel().getValue("entrytype"));
        }
    }

    private void removeAllTabpage(Map<String, String> allEntity) {
        for (Map.Entry<String, String> entryMap : allEntity.entrySet()) {
            String[] entityAndEntryArr = entryMap.getKey().split(":");
            String entityId = entityAndEntryArr[1];
            TemplateFormCommonUtil.removeTabPage((IFormView)this.getView(), (String)entityId);
        }
    }

    private void setNullValue(boolean isImport) {
        this.getModel().beginInit();
        this.setModelNullVal("fieldmerge");
        this.setModelNullVal("fieldstyle");
        if (isImport) {
            this.getModel().setValue("importtype", (Object)"updateandnew");
        }
        this.getModel().endInit();
        this.getView().updateView("fieldmerge");
        this.getView().updateView("fieldstyle");
    }

    private void setControlVisible(boolean newIsImport) {
        if (newIsImport) {
            this.changeVisible(true);
        } else {
            this.changeVisible(false);
        }
    }

    private void changeVisible(boolean newIsImport) {
        EntryGrid grid = (EntryGrid)this.getView().getControl("tpltreeentryentity");
        grid.setColumnProperty("isimport", "header", (Object)new LocaleString(newIsImport ? this.getResIfImportTip() : this.getResIfExportTip()));
        this.getView().setVisible(Boolean.valueOf(newIsImport), new String[]{"advconapcustomtpl", "advconapexptpl", "advconapdescsheet", "importtype", "enabledowncond", "instruction", "configfieldcond", "ismustinput", "isdownloadcond", "iscondgetdata", "entitydescription", "displayname", "fieldimportdesc", "imptattr", "hidelogotypelineflag"});
    }

    private void activeApplyScopeTab() {
        String[] split;
        List<String> scopes;
        String allocationPolicy = (String)this.getModel().getValue("allocationpolicy");
        if ("0".equals(allocationPolicy)) {
            this.getModel().setValue("applyscope", null);
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"applyscope", "tabap"});
            this.getView().setVisible(Boolean.FALSE, new String[]{EnumApplyScopeType.USER.getValue(), EnumApplyScopeType.ROLE.getValue(), EnumApplyScopeType.ORG.getValue(), EnumApplyScopeType.USER_ORG.getValue()});
        } else {
            this.getView().setVisible(Boolean.valueOf(true), new String[]{"applyscope", "tabap"});
            this.getView().setVisible(Boolean.TRUE, new String[]{EnumApplyScopeType.USER.getValue(), EnumApplyScopeType.ROLE.getValue(), EnumApplyScopeType.ORG.getValue(), EnumApplyScopeType.USER_ORG.getValue()});
        }
        String applyScope = (String)this.getModel().getValue("applyscope");
        if (StringUtils.isNotBlank((CharSequence)applyScope) && !CollectionUtils.isEmpty(scopes = Arrays.asList(split = StringUtils.split((String)applyScope, (String)",")))) {
            Tab tab = (Tab)this.getControl("tabap");
            tab.activeTab(EnumApplyScopeType.getApplyScopeValue((String)scopes.get(0)));
        }
    }

    private String getApplyScopeStr(IDataModel model) {
        ArrayList scopes = Lists.newArrayListWithCapacity((int)4);
        if (!CollectionUtils.isEmpty((Collection)model.getEntryEntity("userlist"))) {
            scopes.add(EnumApplyScopeType.getApplyScopeType((String)"userlist"));
        }
        if (!CollectionUtils.isEmpty((Collection)model.getEntryEntity("orglist"))) {
            scopes.add(EnumApplyScopeType.getApplyScopeType((String)"orglist"));
        }
        if (!CollectionUtils.isEmpty((Collection)model.getEntryEntity("rolelist"))) {
            scopes.add(EnumApplyScopeType.getApplyScopeType((String)"rolelist"));
        }
        if (!CollectionUtils.isEmpty((Collection)model.getEntryEntity("orgrolelist"))) {
            scopes.add(EnumApplyScopeType.getApplyScopeType((String)"orgrolelist"));
        }
        return String.join((CharSequence)",", scopes);
    }

    public void click(EventObject evt) {
        switch (((Control)evt.getSource()).getKey()) {
            case "plugin": {
                this.showPlugin();
                break;
            }
            case "labelap_downtpl": {
                List<String> tplConfErrMsg = this.checkPreViewTpl();
                if (!CollectionUtils.isEmpty(tplConfErrMsg)) {
                    if (tplConfErrMsg.size() == 1) {
                        this.getView().showErrorNotification(tplConfErrMsg.get(0));
                    } else if (tplConfErrMsg.size() > 1) {
                        this.getView().showMessage(ResManager.loadKDString((String)"\u7cfb\u7edf\u6a21\u677f\u4e0b\u8f7d\u5931\u8d25\u3002", (String)HiesEntryRes.TemplateConfPlugin_67.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]), Joiner.on((String)"\r\n").join(tplConfErrMsg), MessageTypes.Default);
                    }
                    return;
                }
                this.sortField(this.getView());
                this.tplPreView();
                break;
            }
        }
    }

    private List<String> checkPreViewTpl() {
        OrmLocaleValue langName;
        ArrayList<String> tplConfErrMsg = new ArrayList<String>(4);
        DynamicObject mainEntity = (DynamicObject)this.getModel().getValue("entity");
        if (Objects.isNull(mainEntity)) {
            tplConfErrMsg.add(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u5b9e\u4f53", (String)HiesEntryRes.TemplateConfPlugin_5.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
        }
        if (ObjectUtils.isEmpty((Object)(langName = (OrmLocaleValue)this.getModelVal("name")).getLocaleValue()) && ObjectUtils.isEmpty((Object)langName.get((Object)"GLang")) || ObjectUtils.isEmpty((Object)this.getModelVal("number"))) {
            tplConfErrMsg.add(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u5b8c\u6574\u7684\u7f16\u7801\u548c\u540d\u79f0\u3002", (String)HiesEntryRes.TemplateConfPlugin_32.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]));
        }
        return tplConfErrMsg;
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        FormOperate formOperate = (FormOperate)afterDoOperationEventArgs.getSource();
        switch (formOperate.getOperateKey()) {
            case "save_btnok": {
                this.getView().close();
                break;
            }
            case "save": {
                break;
            }
        }
    }

    private void showPlugin() {
        Object value = this.getModel().getValue("plugin");
        List plugins = new ArrayList();
        if (!ObjectUtils.isEmpty((Object)value)) {
            plugins = kd.bos.dataentity.serialization.SerializationUtils.fromJsonStringToList((String)value.toString(), Map.class);
        }
        String formId = this.getView().getFormShowParameter().getFormId();
        FormShowParameter formShowParameter = this.buildShowParams(formId, plugins, new CloseCallBack((IFormPlugin)this, ACTION_SELECT_SERVICE_PLUGINS));
        this.getView().showForm(formShowParameter);
    }

    private FormShowParameter buildShowParams(String formId, List<?> plugins, CloseCallBack callback) {
        FormShowParameter showParam = new FormShowParameter();
        showParam.setFormId("hies_plugins");
        showParam.getOpenStyle().setShowType(ShowType.Modal);
        showParam.setCustomParam("value", plugins);
        showParam.setCustomParam("formId", (Object)formId);
        showParam.setCloseCallBack(callback);
        return showParam;
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        Object returnData = evt.getReturnData();
        if (returnData == null) {
            return;
        }
        log.info("TemplateConfPlugin closedCallBack\uff1areturnData\uff1a{}", returnData);
        String actionId = evt.getActionId();
        if (ACTION_SELECT_SERVICE_PLUGINS.equalsIgnoreCase(actionId)) {
            Map value = (Map)returnData;
            if (value.get("value") != null && value.get("value") instanceof List) {
                List plugins = (List)value.get("value");
                String pluginString = "";
                if (!plugins.isEmpty()) {
                    pluginString = kd.bos.dataentity.serialization.SerializationUtils.toJsonString((Object)plugins);
                }
                this.getModel().setValue("plugin", (Object)pluginString);
            }
        } else if (ACTION_CLOSECALLBACK_CONFIGCSS.equalsIgnoreCase(actionId)) {
            this.getModel().setValue("fieldstyle", (Object)((JSONObject)returnData).getJSONObject("fieldstyle").toJSONString());
            this.getModel().setValue("fieldmerge", (Object)((JSONObject)returnData).getJSONObject("fieldmerge").toJSONString());
            this.rebuidTplField(returnData);
        } else if (ACTION_CLOSECALLBACK_ORGFIELD.equalsIgnoreCase(actionId)) {
            ListSelectedRowCollection selectedRows = (ListSelectedRowCollection)returnData;
            ListSelectedRow listSelectedRow = selectedRows.get(0);
            Object primaryKeyValue = listSelectedRow.getPrimaryKeyValue();
            this.getModel().setValue(FIELD_ORG, primaryKeyValue);
            Object value = this.getModel().getValue("entity");
            if (Objects.isNull(value)) {
                TemplateFormCommonUtil.removeAllTabPage((IFormView)this.getView());
                String tplType = (String)this.getModel().getValue(FIELD_TEMPLATETYPE);
                String cacheTplType = this.getPageCache().get(FIELD_TEMPLATETYPE);
                if (!StringUtils.equals((CharSequence)tplType, (CharSequence)cacheTplType)) {
                    this.getModel().setValue(FIELD_TEMPLATETYPE, (Object)cacheTplType);
                }
            }
        }
    }

    private void rebuidTplField(Object returnData) {
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        String templateType = (String)model.getValue(FIELD_TEMPLATETYPE);
        String downloadCond = (String)model.getValue("enabledowncond");
        boolean isImport = "IMPT".equals(templateType);
        boolean isDownloadCond = "1".equals(downloadCond);
        boolean isOpen = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.mustInputIsOpen((IDataModel)model);
        boolean canEdit = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)model);
        boolean baseCanEdit = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.baseCanEdit((IDataModel)model);
        DynamicObject bizobject = (DynamicObject)model.getValue("entity");
        if (Objects.nonNull(bizobject)) {
            Map importTypeUniqueMap = TemplateFormCommonUtil.getAllEntityUnique((IDataModel)model);
            for (Map.Entry entityEntry : importTypeUniqueMap.entrySet()) {
                String entityNumber = (String)entityEntry.getKey();
                Map uniqueDataMap = (Map)entityEntry.getValue();
                String uniqueVal = (String)uniqueDataMap.get("uniqueVal");
                uniqueDataMap.put("uniqueVal", uniqueVal);
            }
            JSONObject fieldOrder = ((JSONObject)returnData).getJSONObject("fieldorder");
            log.info("fieldOrder={}", (Object)fieldOrder);
            TemplateFormCommonUtil.removeAllTabPageExEntity((IFormView)this.getView());
            kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.rebuildEntryEntityAndInit((IFormView)view, (IDataModel)model, (boolean)isImport, (boolean)isDownloadCond, (boolean)isOpen, (Boolean)Boolean.FALSE, (Boolean)canEdit, (Boolean)baseCanEdit, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)view), (OperationStatus)OperationStatus.EDIT, (JSONObject)fieldOrder, (Map)importTypeUniqueMap);
        }
    }

    private void rebuidTplFieldPage(JSONObject fieldOrder) {
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        String templateType = (String)model.getValue(FIELD_TEMPLATETYPE);
        String downloadCond = (String)model.getValue("enabledowncond");
        boolean isImport = "IMPT".equals(templateType);
        boolean isDownloadCond = "1".equals(downloadCond);
        boolean isOpen = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.mustInputIsOpen((IDataModel)model);
        boolean canEdit = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)model);
        boolean baseCanEdit = kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.baseCanEdit((IDataModel)model);
        DynamicObject bizobject = (DynamicObject)model.getValue("entity");
        if (Objects.nonNull(bizobject)) {
            Map importTypeUniqueMap = TemplateFormCommonUtil.getAllEntityUnique((IDataModel)model);
            for (Map.Entry entityEntry : importTypeUniqueMap.entrySet()) {
                Map uniqueDataMap = (Map)entityEntry.getValue();
                String uniqueVal = (String)uniqueDataMap.get("uniqueVal");
                uniqueDataMap.put("uniqueVal", uniqueVal);
            }
            kd.hrmp.hies.multientry.common.util.TemplateEntityFieldUtil.rebuildEntryEntityAndInitPage((IFormView)view, (IDataModel)model, (boolean)isImport, (boolean)isDownloadCond, (boolean)isOpen, (Boolean)Boolean.FALSE, (Boolean)canEdit, (Boolean)baseCanEdit, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)view), (OperationStatus)OperationStatus.EDIT, (JSONObject)fieldOrder, (Map)importTypeUniqueMap);
        }
    }

    private String getResIfImportTip() {
        return ResManager.loadKDString((String)"\u662f\u5426\u5bfc\u5165", (String)HiesEntryRes.TemplateConfPlugin_68.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]);
    }

    private String getResIfExportTip() {
        return ResManager.loadKDString((String)"\u662f\u5426\u5bfc\u51fa", (String)HiesEntryRes.TemplateConfPlugin_69.resId(), (String)"hrmp-hies-entry", (Object[])new Object[0]);
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        this.dealTextLineSeparator();
    }

    private void dealTextLineSeparator() {
        ILocaleString value = (ILocaleString)this.getModel().getValue("instruction");
        String sysLineSeparator = HIESUtil.getSysLineSeparator();
        value.forEach((key, val) -> {
            val = val.replace("\\r\\n", sysLineSeparator);
            value.setItem(key, val);
        });
        this.getModel().setValue("instruction", (Object)value);
    }
}
