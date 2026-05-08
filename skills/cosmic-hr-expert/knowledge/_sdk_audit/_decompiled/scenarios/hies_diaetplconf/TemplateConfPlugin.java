/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.base.Joiner
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.SqlParameter
 *  kd.bos.dataentity.entity.DataEntityState
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.metadata.ICollectionProperty
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicLocaleProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.DataEntitySerializer
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.DB
 *  kd.bos.db.DBRoute
 *  kd.bos.db.ResultSetHandler
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.QueryEntityType
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.DirtyManager
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.entity.datamodel.events.BeforeDeleteRowEventArgs
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.BillStatusProp
 *  kd.bos.entity.property.EntryProp
 *  kd.bos.entity.property.LinkEntryProp
 *  kd.bos.entity.property.MulBasedataProp
 *  kd.bos.exception.BosErrorCode
 *  kd.bos.exception.KDException
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.FieldTip
 *  kd.bos.form.FieldTip$FieldTipsLevel
 *  kd.bos.form.FieldTip$FieldTipsTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.MessageTypes
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Container
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.AttachmentPanel
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.Label
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.BeforeAttachmentUploadEvent
 *  kd.bos.form.control.events.BeforeAttachmentUploadListener
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.BeforeUploadEvent
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.control.events.RowClickEventListener
 *  kd.bos.form.control.events.TabSelectEvent
 *  kd.bos.form.control.events.TabSelectListener
 *  kd.bos.form.control.events.UploadEvent
 *  kd.bos.form.control.events.UploadListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.form.util.ImportOperationLog
 *  kd.bos.form.util.ImportOperationLog$OperationEnum
 *  kd.bos.inte.api.EnabledLang
 *  kd.bos.inte.service.InteServiceImpl
 *  kd.bos.lang.Lang
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.dao.MetadataDao
 *  kd.bos.mvc.bill.BillModel
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.formplugin.util.PermFormCommonUtil
 *  kd.bos.servicehelper.AttachmentServiceHelper
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.DispatchServiceHelper
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.bos.svc.util.FileHandlerUtil
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.common.util.ReflectUtil
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hies.common.HiesCommonRes
 *  kd.hr.hies.common.constant.HIESConstant
 *  kd.hr.hies.common.constant.MCConfigConstant
 *  kd.hr.hies.common.constant.TplGenModeConstant
 *  kd.hr.hies.common.dto.CustomTabPage
 *  kd.hr.hies.common.dto.CustomTabPageId
 *  kd.hr.hies.common.dto.EntityFieldContext
 *  kd.hr.hies.common.dto.Result
 *  kd.hr.hies.common.enu.EnumApplyScopeType
 *  kd.hr.hies.common.enu.OprCategory
 *  kd.hr.hies.common.util.HIESUtil
 *  kd.hr.hies.common.util.ImportFileUtil
 *  kd.hr.hies.common.util.MethodUtil
 *  kd.hr.hies.common.util.TemplateEntityFieldUtil
 *  kd.hr.hies.common.util.TemplateFormCommonUtil
 *  kd.hr.impt.business.task.TplDownLoadTask
 *  kd.hr.impt.common.util.LocalTplCheckUtil
 *  kd.hr.impt.core.end.LocalTplReadSheetHandler
 *  kd.hr.impt.core.parse.ExcelReader
 *  kd.hr.impt.core.parse.SheetHandler
 *  kd.hr.impt.mservice.api.ITemplateService
 *  org.apache.commons.collections4.MapUtils
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.commons.lang3.SerializationUtils
 *  org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException
 */
package kd.hr.impt.formplugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.MessageFormat;
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
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.SqlParameter;
import kd.bos.dataentity.entity.DataEntityState;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.metadata.ICollectionProperty;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.dynamicobject.DynamicLocaleProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.DataEntitySerializer;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.DB;
import kd.bos.db.DBRoute;
import kd.bos.db.ResultSetHandler;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.QueryEntityType;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.DirtyManager;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.entity.datamodel.events.BeforeDeleteRowEventArgs;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.BillStatusProp;
import kd.bos.entity.property.EntryProp;
import kd.bos.entity.property.LinkEntryProp;
import kd.bos.entity.property.MulBasedataProp;
import kd.bos.exception.BosErrorCode;
import kd.bos.exception.KDException;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.FieldTip;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.MessageTypes;
import kd.bos.form.ShowType;
import kd.bos.form.container.Container;
import kd.bos.form.container.Tab;
import kd.bos.form.control.AttachmentPanel;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Label;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.BeforeAttachmentUploadEvent;
import kd.bos.form.control.events.BeforeAttachmentUploadListener;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.BeforeUploadEvent;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.control.events.TabSelectEvent;
import kd.bos.form.control.events.TabSelectListener;
import kd.bos.form.control.events.UploadEvent;
import kd.bos.form.control.events.UploadListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.form.util.ImportOperationLog;
import kd.bos.inte.api.EnabledLang;
import kd.bos.inte.service.InteServiceImpl;
import kd.bos.lang.Lang;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.dao.MetadataDao;
import kd.bos.mvc.bill.BillModel;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.formplugin.util.PermFormCommonUtil;
import kd.bos.servicehelper.AttachmentServiceHelper;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.bos.svc.util.FileHandlerUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.common.util.ReflectUtil;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hies.common.HiesCommonRes;
import kd.hr.hies.common.constant.HIESConstant;
import kd.hr.hies.common.constant.MCConfigConstant;
import kd.hr.hies.common.constant.TplGenModeConstant;
import kd.hr.hies.common.dto.CustomTabPage;
import kd.hr.hies.common.dto.CustomTabPageId;
import kd.hr.hies.common.dto.EntityFieldContext;
import kd.hr.hies.common.dto.Result;
import kd.hr.hies.common.enu.EnumApplyScopeType;
import kd.hr.hies.common.enu.OprCategory;
import kd.hr.hies.common.util.HIESUtil;
import kd.hr.hies.common.util.ImportFileUtil;
import kd.hr.hies.common.util.MethodUtil;
import kd.hr.hies.common.util.TemplateEntityFieldUtil;
import kd.hr.hies.common.util.TemplateFormCommonUtil;
import kd.hr.impt.business.task.TplDownLoadTask;
import kd.hr.impt.common.util.LocalTplCheckUtil;
import kd.hr.impt.core.end.LocalTplReadSheetHandler;
import kd.hr.impt.core.parse.ExcelReader;
import kd.hr.impt.core.parse.SheetHandler;
import kd.hr.impt.mservice.api.ITemplateService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;

@ExcludeFromJacocoGeneratedReport
public final class TemplateConfPlugin
extends HRBaseDataCommonEdit
implements UploadListener,
BeforeAttachmentUploadListener,
RowClickEventListener,
TabSelectListener,
BeforeF7SelectListener,
ClickListener {
    private static final Log log = LogFactory.getLog(TemplateConfPlugin.class);
    public static final String ACTION_SELECT_SERVICE_PLUGINS = "closeCallBack_selectServicePlugins";
    public static final String ACTION_CLOSECALLBACK_ORGFIELD = "closeCallBack_orgField";
    public static final String ACTION_CLOSECALLBACK_CONFIGCSS = "closeCallBack_configcss";
    public static final String BTN_CONFIGCSS = "btn_configcss";
    public static final String KEY_BTNOK = "btnok";
    public static final String KEY_BTNCANCEL = "btncancel";
    private static final String TPL_CURRENT_TAB = "tpl_current_tab";
    private static final String FIELD_TEMPLATETYPE = "tmpltype";
    private static final String FIELD_BIZOBJ = "entity";
    private static final String FIELD_ORG = "orgfield";
    private static final String PARAM_BINDENTITYID = "bindEntityId";
    public static final String BAR_PREVIEW = "bar_preview";
    public static final String DO_PREVIEW = "dopreview";
    public static final String LABEL_DOWNTPL = "labelap_downtpl";
    public static final String LABEL_VIEWQUERYENTITY = "labelap_viewqueryentity";
    public static final String LABEL_MANUALLY = "labelap_manually";
    public static final String FIELD_CONF_PANEL = "advconap3";
    protected boolean closeConfirmStatus = false;

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        BasedataEdit orgField = (BasedataEdit)this.getView().getControl(FIELD_ORG);
        orgField.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        this.addClickListeners(new String[]{"advcontoolbarap3", "bdpropkey", "pagepanel", "configfieldcond", KEY_BTNOK});
        this.addClickListeners(new String[]{"plugin", LABEL_DOWNTPL, LABEL_MANUALLY, LABEL_VIEWQUERYENTITY});
        Toolbar subEntityToolBarAp = (Toolbar)this.getControl("toolbar_subentity");
        subEntityToolBarAp.addItemClickListener((ItemClickListener)this);
        Toolbar fieldToolbar = (Toolbar)this.getControl("toobar_field");
        fieldToolbar.addItemClickListener((ItemClickListener)this);
        Tab tab = (Tab)this.getControl("objtabap");
        tab.addTabSelectListener((TabSelectListener)this);
        AttachmentPanel attachmentPanel = (AttachmentPanel)this.getView().getControl("attpanelapcustomtpl");
        attachmentPanel.addUploadListener((UploadListener)this);
        attachmentPanel.addBeforeUploadListener((BeforeAttachmentUploadListener)this);
    }

    public void preOpenForm(PreOpenFormEventArgs e) {
        FormShowParameter formShowParameter = e.getFormShowParameter();
        Object isCopy = formShowParameter.getCustomParams().get("iscopy");
        if (formShowParameter instanceof BaseShowParameter) {
            Object pkId = ((BaseShowParameter)e.getFormShowParameter()).getPkId();
            if (Objects.isNull(pkId)) {
                return;
            }
            DynamicObject dynamicObject = BusinessDataServiceHelper.loadSingle((Object)pkId, (String)"hies_diaetplconf");
            boolean isImport = "IMPT".equals(dynamicObject.getString(FIELD_TEMPLATETYPE));
            if (isCopy != null) {
                if (isImport) {
                    formShowParameter.setCaption(ResManager.loadKDString((String)"\u590d\u5236\u5bfc\u5165\u6a21\u677f", (String)"TemplateConfPlugin_47", (String)"hrmp-hies-import", (Object[])new Object[0]));
                } else {
                    formShowParameter.setCaption(ResManager.loadKDString((String)"\u590d\u5236\u5bfc\u51fa\u6a21\u677f", (String)"TemplateConfPlugin_46", (String)"hrmp-hies-import", (Object[])new Object[0]));
                }
            } else if (isImport) {
                formShowParameter.setCaption(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5bfc\u5165\u6a21\u677f-%s", (String)"TemplateConfPlugin_48", (String)"hrmp-hies-import", (Object[])new Object[0]), dynamicObject.getString("name")));
            } else {
                formShowParameter.setCaption(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5bfc\u51fa\u6a21\u677f-%s", (String)"TemplateConfPlugin_49", (String)"hrmp-hies-import", (Object[])new Object[0]), dynamicObject.getString("name")));
            }
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        String itemKey;
        super.beforeItemClick(evt);
        switch (itemKey = evt.getItemKey()) {
            case "btn_configcss": {
                DynamicObject object = (DynamicObject)this.getModel().getValue(FIELD_BIZOBJ);
                if (Objects.isNull(object)) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u5b9e\u4f53", (String)"TemplateConfPlugin_5", (String)"hrmp-hies-import", (Object[])new Object[0]));
                    evt.setCancel(true);
                    return;
                }
                this.sortField(this.getView());
                FormShowParameter showParameter = new FormShowParameter();
                showParameter.setFormId("hies_tplfieldstyleconf");
                showParameter.setCustomParam(FIELD_TEMPLATETYPE, this.getModel().getValue(FIELD_TEMPLATETYPE));
                showParameter.setCustomParam("fieldstyle", this.getModel().getValue("fieldstyle"));
                showParameter.setCustomParam("fieldmerge", this.getModel().getValue("fieldmerge"));
                showParameter.getOpenStyle().setShowType(ShowType.Modal);
                showParameter.setCustomParam("entityFieldMap", (Object)JSON.toJSONString((Object)TemplateEntityFieldUtil.getEntityCssMap((IDataModel)this.getModel(), (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()))));
                showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, ACTION_CLOSECALLBACK_CONFIGCSS));
                this.getView().showForm(showParameter);
                break;
            }
        }
    }

    public static void showTplList(IFormView view, String formId, String entityNumber) {
        ListShowParameter showParameter = new ListShowParameter();
        showParameter.setBillFormId(formId);
        showParameter.setFormId("bos_treelist");
        showParameter.setLookUp(false);
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.getCustomParams().putAll(view.getFormShowParameter().getCustomParams());
        showParameter.setCustomParam("checkRightAppId", (Object)"15NPDX/GJFOO");
        showParameter.setCustomParam("oprFormId", (Object)entityNumber);
        view.showForm((FormShowParameter)showParameter);
    }

    private static void showTplDetail(IFormView view, String formId, Object pkId) {
        BillShowParameter showParameter = new BillShowParameter();
        showParameter.setFormId(formId);
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setShowTitle(true);
        showParameter.setHasRight(true);
        showParameter.setStatus(OperationStatus.VIEW);
        showParameter.setPkId(pkId);
        showParameter.setCustomParam("checkRightAppId", (Object)"15NPDX/GJFOO");
        view.showForm((FormShowParameter)showParameter);
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
            String enittyId = dataEntity.getString("queryentity.id");
            if (StringUtils.isEmpty((CharSequence)enittyId)) {
                enittyId = dataEntity.getString("entity.id");
            }
            MainEntityType dataEntityType = MetadataServiceHelper.getDataEntityType((String)enittyId);
            String appId = dataEntityType.getAppId();
            log.info("genPreView_appId={}", (Object)appId);
            String routeAppId = MethodUtil.getRouteAppId((String)appId);
            Map f7CountByTpl = TemplateEntityFieldUtil.computeF7CountByTpl((DynamicObject)dataEntity);
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
                Result result = (Result)DispatchServiceHelper.invokeService((String)"kd.hr.impt.servicehelper", (String)routeAppId, (String)ITemplateService.class.getSimpleName(), (String)"genPreView", (Object[])new Object[]{dataEntity, map});
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

    public void tabSelected(TabSelectEvent tabSelectEvent) {
        String tabKey = tabSelectEvent.getTabKey();
        this.getPageCache().put(TPL_CURRENT_TAB, tabKey);
    }

    public void afterCreateNewData(EventObject e) {
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        this.setDefaultValue();
        this.setDefaultVisible();
        String tplGenMode = (String)view.getFormShowParameter().getCustomParam("tplgenmode");
        if (StringUtils.isBlank((CharSequence)tplGenMode)) {
            tplGenMode = (String)this.getModel().getValue("tplgenmode");
        }
        if (TplGenModeConstant.isLocalUpload((String)tplGenMode)) {
            if (this.isExistLocalUploadFile()) {
                view.setVisible(Boolean.TRUE, new String[]{"importcfg"});
            } else {
                view.setVisible(Boolean.FALSE, new String[]{"importcfg"});
            }
        }
        String entityId = (String)this.getView().getFormShowParameter().getCustomParam(PARAM_BINDENTITYID);
        boolean canEdit = TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)model);
        boolean baseCanEdit = TemplateEntityFieldUtil.baseCanEdit((IDataModel)model);
        if (StringUtils.isNotBlank((CharSequence)entityId)) {
            this.getView().setVisible(Boolean.TRUE, new String[]{FIELD_CONF_PANEL});
            Container flexPanel = (Container)this.getView().getControl(FIELD_CONF_PANEL);
            flexPanel.setCollapse(false);
            this.getModel().setValue(FIELD_BIZOBJ, (Object)entityId);
            this.getModel().setValue("queryentity", (Object)entityId);
            DynamicObject bizobject = (DynamicObject)model.getValue(FIELD_BIZOBJ);
            String entityName = bizobject.getString("name");
            String entityNumber = bizobject.getString("number");
            DynamicObject queryBizobject = (DynamicObject)this.getModel().getValue("queryentity");
            String queryEntityNumber = queryBizobject.getString("number");
            String tmpltype = (String)this.getModel().getValue(FIELD_TEMPLATETYPE);
            boolean isImport = "IMPT".equalsIgnoreCase(tmpltype);
            TemplateFormCommonUtil.addTabPage((IFormView)view, (CustomTabPage)new CustomTabPage(entityId, entityName), (String)entityNumber, (String)queryEntityNumber, (boolean)isImport, (boolean)Boolean.TRUE, (Boolean)Boolean.TRUE, (Boolean)Boolean.TRUE, (Boolean)canEdit, (Boolean)baseCanEdit, (String)"", (OperationStatus)OperationStatus.EDIT);
            EntityFieldContext entityFieldContext = new EntityFieldContext(view, entityNumber, "tpltreeentryentity", isImport, true, canEdit, baseCanEdit, "");
            TemplateEntityFieldUtil.newInstance().addTreeEntryRow(entityFieldContext);
        }
    }

    private boolean isExistLocalUploadFile() {
        List attachments = AttachmentServiceHelper.getAttachments((String)"hies_diaetplconf", (Object)this.getModel().getValue("id"), (String)"attpanelapcustomtpl");
        return !CollectionUtils.isEmpty((Collection)attachments);
    }

    private void setDefaultValue() {
        String isImport = (String)this.getView().getFormShowParameter().getCustomParams().get("isImport");
        if ("1".equals(isImport)) {
            this.getView().setFormTitle(new LocaleString(ResManager.loadKDString((String)"\u65b0\u589e\u5bfc\u5165\u6a21\u677f", (String)"TemplateConfPlugin_43", (String)"hrmp-hies-import", (Object[])new Object[0])));
        } else {
            this.getView().setFormTitle(new LocaleString(ResManager.loadKDString((String)"\u65b0\u589e\u5bfc\u51fa\u6a21\u677f", (String)"TemplateConfPlugin_44", (String)"hrmp-hies-import", (Object[])new Object[0])));
        }
        if ("0".equals(isImport)) {
            this.getModel().setValue(FIELD_TEMPLATETYPE, (Object)"EXPT");
        } else {
            this.getModel().setValue(FIELD_TEMPLATETYPE, (Object)"IMPT");
        }
        this.getModel().setValue("enabledowncond", (Object)"1");
        this.getModel().setValue("entitytype", (Object)"SE");
        this.getModel().setValue("importtype", (Object)"new");
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
        this.getView().setVisible(Boolean.FALSE, new String[]{"advconap7", "applyscope", "tabap"});
        this.getView().setVisible(Boolean.FALSE, new String[]{EnumApplyScopeType.USER.getValue(), EnumApplyScopeType.ROLE.getValue(), EnumApplyScopeType.ORG.getValue(), EnumApplyScopeType.USER_ORG.getValue()});
        this.getView().setVisible(Boolean.FALSE, new String[]{"createtime"});
        this.getView().setVisible(Boolean.FALSE, new String[]{KEY_BTNOK, KEY_BTNCANCEL});
        String templateType = (String)this.getModel().getValue(FIELD_TEMPLATETYPE);
        boolean isImportTpl = "IMPT".equals(templateType);
        this.setControlVisible(isImportTpl);
        String tplGenMode = (String)this.getView().getFormShowParameter().getCustomParam("tplgenmode");
        if (TplGenModeConstant.isLocalUpload((String)tplGenMode)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"advconapdescsheet", "advconapexptpl"});
            Label labelap3 = (Label)this.getView().getControl("labelap3");
            labelap3.setText(ResManager.loadKDString((String)"\u672c\u5730\u4e0a\u4f20\u7684\u6a21\u677f\u9700\u57fa\u4e8e\u7cfb\u7edf\u6a21\u677f\u8fdb\u884c\u4fee\u6539\u3002\u8bf7\u5148\u5b8c\u6210\u7cfb\u7edf\u6a21\u677f\u914d\u7f6e\uff0c\u5e76\u5c06\u7cfb\u7edf\u6a21\u677f\u4e0b\u8f7d\u5230\u672c\u5730\u8c03\u6574\u540e\u518d\u6b21\u4e0a\u4f20\uff0c\u5373\u53ef\u4f7f\u7528\u4e0a\u4f20\u7684\u6587\u4ef6\u8fdb\u884c\u6570\u636e\u5bfc\u5165\u3002", (String)"TemplateConfPlugin_31", (String)"hrmp-hies-import", (Object[])new Object[0]));
            Label labelap = (Label)this.getView().getControl("labelap");
            labelap.setText(ResManager.loadKDString((String)"\u2460 \u8bf7\u52ff\u5220\u6539\u7cfb\u7edf\u6a21\u677f\u4e0a\u7684\u5173\u952e\u6807\u8bc6\uff0c\u5426\u5219\u6587\u4ef6\u65e0\u6cd5\u4e0a\u4f20\u3002\u5982\u6a21\u677f\u7f16\u7801\u548c\u540d\u79f0\uff08\u4e3b\u5b9e\u4f53Sheet\u7b2c\u4e00\u884c\uff09\u3001\u5b9e\u4f53\u7f16\u7801\u548c\u540d\u79f0\uff08\u4e3b\u5b9e\u4f53Sheet\u7b2c\u4e8c\u884c\u3001\u5b50\u5b9e\u4f53Sheet\u7b2c\u4e00\u884c\uff09\u3001\u5f00\u542f\u5fc5\u5f55\u7684\u5b57\u6bb5\uff08\u5e26*\u7684\u5b57\u6bb5\uff09", (String)"TemplateConfPlugin_29", (String)"hrmp-hies-import", (Object[])new Object[0]));
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{"localuploadremind"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"advconapcustomtpl"});
        }
        String entityType = (String)this.getModel().getValue("entitytype");
        this.updateImportType(entityType);
        this.getModel().setValue("importtype", (Object)"new");
        this.resetQueryEntityTips();
        this.updateEntityType(false);
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
        DynamicObject copyFrom = BusinessDataServiceHelper.loadSingle((Object)param.getPkId(), (String)"hies_diaetplconf");
        LocaleString copyResSuffix = ResManager.getLocaleString((String)"%s-\u590d\u5236", (String)"TemplateConfPlugin_0", (String)"hrmp-hies-import");
        OrmLocaleValue oriLocaleValue = (OrmLocaleValue)copyFrom.get("name");
        OrmLocaleValue localeValue = (OrmLocaleValue)SerializationUtils.clone((Serializable)oriLocaleValue);
        TemplateFormCommonUtil.clearDisabledLang((OrmLocaleValue)localeValue, (List)enabledLangList);
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
        String tplGenMode = (String)view.getFormShowParameter().getCustomParam("tplgenmode");
        if (StringUtils.isBlank((CharSequence)tplGenMode)) {
            tplGenMode = (String)this.getModel().getValue("tplgenmode");
        }
        if (TplGenModeConstant.isLocalUpload((String)tplGenMode)) {
            if (this.isExistLocalUploadFile()) {
                view.setVisible(Boolean.TRUE, new String[]{"importcfg"});
            } else {
                view.setVisible(Boolean.FALSE, new String[]{"importcfg"});
                this.deleteImportCfg();
            }
        }
        this.getView().setVisible(Boolean.FALSE, new String[]{"createtime"});
        this.getView().getFormShowParameter().getCustomParams().put("clickBtn", "copy");
        this.resetView();
        TemplateEntityFieldUtil.buildAndRestoreFieldTree((IFormView)view, (IDataModel)model, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)view), (OperationStatus)OperationStatus.EDIT);
    }

    public void afterLoadData(EventObject e) {
        String queryEntityNumber;
        MainEntityType mainType;
        super.afterLoadData(e);
        this.resetQueryEntityTips();
        DynamicObject queryEntity = (DynamicObject)this.getModel().getValue("queryentity");
        if (Objects.isNull(queryEntity)) {
            this.getModel().setValue("queryentity", this.getModel().getValue(FIELD_BIZOBJ));
        }
        IFormView view = this.getView();
        OperationStatus status = view.getFormShowParameter().getStatus();
        String btn = (String)view.getFormShowParameter().getCustomParam("btn");
        if ("assign".equals(btn)) {
            this.initAssignPage(view);
            view.setVisible(Boolean.valueOf("1".equals(this.getModel().getValue("allocationpolicy"))), new String[]{"applyscope", "tabap"});
            return;
        }
        Container flexPanel = (Container)this.getView().getControl(FIELD_CONF_PANEL);
        flexPanel.setCollapse(false);
        if (OperationStatus.EDIT == status || OperationStatus.VIEW == status) {
            String tplGenMode = (String)view.getFormShowParameter().getCustomParam("tplgenmode");
            if (StringUtils.isBlank((CharSequence)tplGenMode)) {
                tplGenMode = (String)this.getModel().getValue("tplgenmode");
            }
            if (TplGenModeConstant.isLocalUpload((String)tplGenMode)) {
                if (this.isExistLocalUploadFile()) {
                    if (OperationStatus.EDIT == status) {
                        view.setEnable(Boolean.FALSE, new String[]{"fs_baseinfo"});
                        view.setEnable(Boolean.FALSE, new String[]{"advconap6"});
                        view.setEnable(Boolean.FALSE, new String[]{FIELD_CONF_PANEL});
                        this.getPageCache().put("treeLockFlag", "Y");
                    }
                    view.setVisible(Boolean.TRUE, new String[]{"importcfg"});
                } else {
                    view.setVisible(Boolean.FALSE, new String[]{"importcfg"});
                    this.deleteImportCfg();
                }
            }
        }
        this.getView().getFormShowParameter().getCustomParams().put("clickBtn", "view");
        this.resetView();
        LinkedHashMap curPageAllEntity = TemplateFormCommonUtil.getAllEntity((IFormView)view);
        if (Objects.nonNull(queryEntity) && (mainType = EntityMetadataCache.getDataEntityType((String)(queryEntityNumber = queryEntity.getString("number")))) instanceof QueryEntityType) {
            QueryEntityType queryEntityType = (QueryEntityType)mainType;
            List sourceAllJoinEntity = queryEntityType.getAllJoinEntityType();
            MainEntityType mainEntityType = queryEntityType.getMainEntityType();
            ArrayList<MainEntityType> sourceAllEntity = new ArrayList<MainEntityType>(sourceAllJoinEntity.size() + 1);
            sourceAllEntity.add(mainEntityType);
            sourceAllEntity.addAll(sourceAllJoinEntity);
            Iterator iterator = sourceAllEntity.iterator();
            while (iterator.hasNext()) {
                MainEntityType next = (MainEntityType)iterator.next();
                for (Map.Entry entry : curPageAllEntity.entrySet()) {
                    String[] split = ((String)entry.getKey()).split(":");
                    String number = split[1];
                    if (!number.equals(next.getName())) continue;
                    iterator.remove();
                }
            }
            if (!CollectionUtils.isEmpty(sourceAllEntity)) {
                if (OperationStatus.VIEW == status) {
                    String message = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5f53\u524d\u6a21\u677f\u5bf9\u5e94\u7684\u67e5\u8be2\u5b9e\u4f53\u5b50\u5b9e\u4f53\u53d1\u751f\u4e86\u53d8\u5316\uff0c\u82e5\u9700\u6309\u7167\u6700\u65b0\u7684\u5b50\u5b9e\u4f53\u8fdb\u884c\u6570\u636e\u5bfc\u5165\u6216\u5bfc\u51fa\uff0c\u8bf7\u91cd\u65b0\u7f16\u8f91\u6a21\u677f\uff0c\u66f4\u65b0\u914d\u7f6e\u5b50\u5b9e\u4f53\u6570\u636e\u3002", (String)"TemplateConfPlugin_61", (String)"hrmp-hies-import", (Object[])new Object[0]), new Object[0]);
                    view.showConfirm(message, MessageBoxOptions.OK);
                }
            } else {
                DynamicObject mainDyObj;
                DynamicObjectCollection subEntityCollection = this.getModel().getEntryEntity("entityrelation");
                if (CollectionUtils.isEmpty((Collection)sourceAllJoinEntity) && !CollectionUtils.isEmpty((Collection)subEntityCollection) && OperationStatus.VIEW == status) {
                    String message = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5f53\u524d\u6a21\u677f\u5bf9\u5e94\u7684\u67e5\u8be2\u5b9e\u4f53\u5b50\u5b9e\u4f53\u53d1\u751f\u4e86\u53d8\u5316\uff0c\u82e5\u9700\u6309\u7167\u6700\u65b0\u7684\u5b50\u5b9e\u4f53\u8fdb\u884c\u6570\u636e\u5bfc\u5165\u6216\u5bfc\u51fa\uff0c\u8bf7\u91cd\u65b0\u7f16\u8f91\u6a21\u677f\uff0c\u66f4\u65b0\u914d\u7f6e\u5b50\u5b9e\u4f53\u6570\u636e\u3002", (String)"TemplateConfPlugin_61", (String)"hrmp-hies-import", (Object[])new Object[0]), new Object[0]);
                    view.showConfirm(message, MessageBoxOptions.OK);
                }
                if (Objects.nonNull(mainDyObj = (DynamicObject)this.getModel().getValue(FIELD_BIZOBJ))) {
                    String queryNumber;
                    MainEntityType type;
                    String entityNumber = mainDyObj.getString("number");
                    DynamicObject bizobject = (DynamicObject)this.getModel().getValue("queryentity");
                    String mainEntityNumber = "";
                    if (Objects.nonNull(bizobject) && (type = EntityMetadataCache.getDataEntityType((String)(queryNumber = bizobject.getString("number")))) instanceof QueryEntityType) {
                        mainEntityNumber = ((QueryEntityType)type).getMainEntityType().getName();
                    }
                    if (!HRStringUtils.equals((String)entityNumber, (String)mainEntityNumber)) {
                        String message = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5f53\u524d\u6a21\u677f\u5bf9\u5e94\u7684\u67e5\u8be2\u5b9e\u4f53\u5b50\u5b9e\u4f53\u53d1\u751f\u4e86\u53d8\u5316\uff0c\u82e5\u9700\u6309\u7167\u6700\u65b0\u7684\u5b50\u5b9e\u4f53\u8fdb\u884c\u6570\u636e\u5bfc\u5165\u6216\u5bfc\u51fa\uff0c\u8bf7\u91cd\u65b0\u7f16\u8f91\u6a21\u677f\uff0c\u66f4\u65b0\u914d\u7f6e\u5b50\u5b9e\u4f53\u6570\u636e\u3002", (String)"TemplateConfPlugin_61", (String)"hrmp-hies-import", (Object[])new Object[0]), new Object[0]);
                        view.showConfirm(message, MessageBoxOptions.OK);
                    }
                }
            }
            if (OperationStatus.EDIT == status) {
                String name = mainEntityType.getName();
                ArrayList<String> curSubEntityNumbers = new ArrayList<String>(sourceAllJoinEntity.size());
                for (Map.Entry entity : curPageAllEntity.entrySet()) {
                    String[] split = ((String)entity.getKey()).split(":");
                    String number = split[1];
                    if (number.equals(name)) continue;
                    curSubEntityNumbers.add(number);
                }
                this.rebuildSubEntry(this.getModel(), curSubEntityNumbers);
            }
        }
        TemplateEntityFieldUtil.buildAndRestoreFieldTree((IFormView)view, (IDataModel)this.getModel(), (LinkedHashMap)curPageAllEntity, (OperationStatus)status);
    }

    private void rebuildSubEntry(IDataModel model, List<String> curSubEntityNumbers) {
        int[] rows;
        String value = (String)model.getValue(FIELD_TEMPLATETYPE);
        boolean isImport = "IMPT".equalsIgnoreCase(value);
        String downloadCond = (String)model.getValue("enabledowncond");
        boolean isDownloadCond = "1".equals(downloadCond);
        boolean isOpen = TemplateEntityFieldUtil.mustInputIsOpen((IDataModel)model);
        boolean canEdit = TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)model);
        boolean baseCanEdit = TemplateEntityFieldUtil.baseCanEdit((IDataModel)model);
        String uniqueval = (String)model.getValue("mainentityuniqueval");
        Map subEntityMap = TemplateEntityFieldUtil.getSubEntityMap((IDataModel)model);
        model.beginInit();
        model.deleteEntryData("entityrelation");
        model.endInit();
        TableValueSetter vs = new TableValueSetter(new String[0]);
        vs.addField("rentity", new Object[0]);
        vs.addField("rentityalias", new Object[0]);
        vs.addField("entityuniqueval", new Object[0]);
        vs.addField("relationleftprop", new Object[0]);
        vs.addField("relationrightprop", new Object[0]);
        for (String entityNumber : curSubEntityNumbers) {
            LinkedHashMap data = (LinkedHashMap)subEntityMap.get(entityNumber);
            if (Objects.nonNull(data)) {
                vs.addRow(new Object[]{entityNumber, data.get("rentityalias"), data.get("entityuniqueval"), data.get("relationleftprop"), data.get("relationrightprop")});
                continue;
            }
            vs.addRow(new Object[]{entityNumber});
            EntityFieldContext entityFieldContext = new EntityFieldContext(this.getView(), entityNumber, "tpltreeentryentity", isImport, isOpen, canEdit, baseCanEdit, uniqueval);
            TemplateEntityFieldUtil.newInstance().addTreeEntryRow(entityFieldContext);
        }
        this.getModel().beginInit();
        for (int row : rows = ((AbstractFormDataModel)this.getModel()).batchCreateNewEntryRow("entityrelation", vs)) {
            DynamicObject entryEntity = ((BillModel)this.getModel()).getEntryEntity("entityrelation", row);
            entryEntity.getDataEntityState().setBizChanged(true);
            entryEntity.getDataEntityState().setBizChangeFlags(new long[]{1L});
        }
        this.getModel().endInit();
        this.getModel().updateEntryCache(this.getModel().getEntryEntity("entityrelation"));
        this.getView().updateView("entityrelation");
    }

    public void beforeBindData(EventObject e) {
        this.dealTextLineSeparator();
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        Object issyspreset = this.getModelVal("issyspreset");
        if (Boolean.TRUE.equals(issyspreset)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"bar_modify"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap5", "advconap", "advconap31"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"advconapcustomtpl", "advconapdescsheet", "advconapexptpl"});
        } else {
            OperationStatus status = this.getView().getFormShowParameter().getStatus();
            if (OperationStatus.EDIT == status) {
                this.getView().getPageCache().put("preEnable", this.getModelValStr("enable"));
            }
        }
        TemplateFormCommonUtil.displayEnablePersonIdImport((IFormView)this.getView());
        String importType = (String)this.getModel().getValue("entitytype");
        this.showMainEntityAliastEXT(importType);
        this.getModel().setDataChanged(false);
    }

    private void resetView() {
        IDataModel model = this.getModel();
        String entityType = (String)model.getValue("entitytype");
        String importType = (String)model.getValue("importtype");
        this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap1"});
        this.getView().setVisible(Boolean.FALSE, new String[]{EnumApplyScopeType.USER.getValue(), EnumApplyScopeType.ROLE.getValue(), EnumApplyScopeType.ORG.getValue(), EnumApplyScopeType.USER_ORG.getValue()});
        this.activeApplyScopeTab();
        this.getView().setVisible(Boolean.valueOf("1".equals(model.getValue("allocationpolicy"))), new String[]{"applyscope", "tabap"});
        String templateType = (String)model.getValue(FIELD_TEMPLATETYPE);
        boolean isImport = "IMPT".equals(templateType);
        this.setControlVisible(isImport);
        String tplGenMode = (String)this.getView().getFormShowParameter().getCustomParam("tplgenmode");
        if (StringUtils.isBlank((CharSequence)tplGenMode)) {
            tplGenMode = (String)model.getValue("tplgenmode");
        }
        if (TplGenModeConstant.isLocalUpload((String)tplGenMode)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"advconapdescsheet", "advconapexptpl"});
            Label labelap3 = (Label)this.getView().getControl("labelap3");
            labelap3.setText(ResManager.loadKDString((String)"\u672c\u5730\u4e0a\u4f20\u7684\u6a21\u677f\u9700\u57fa\u4e8e\u7cfb\u7edf\u6a21\u677f\u8fdb\u884c\u4fee\u6539\u3002\u8bf7\u5148\u5b8c\u6210\u7cfb\u7edf\u6a21\u677f\u914d\u7f6e\uff0c\u5e76\u5c06\u7cfb\u7edf\u6a21\u677f\u4e0b\u8f7d\u5230\u672c\u5730\u8c03\u6574\u540e\u518d\u6b21\u4e0a\u4f20\uff0c\u5373\u53ef\u4f7f\u7528\u4e0a\u4f20\u7684\u6587\u4ef6\u8fdb\u884c\u6570\u636e\u5bfc\u5165\u3002", (String)"TemplateConfPlugin_31", (String)"hrmp-hies-import", (Object[])new Object[0]));
            Label labelap = (Label)this.getView().getControl("labelap");
            labelap.setText(ResManager.loadKDString((String)"\u2460 \u8bf7\u52ff\u5220\u6539\u7cfb\u7edf\u6a21\u677f\u4e0a\u7684\u5173\u952e\u6807\u8bc6\uff0c\u5426\u5219\u6587\u4ef6\u65e0\u6cd5\u4e0a\u4f20\u3002\u5982\u6a21\u677f\u7f16\u7801\u548c\u540d\u79f0\uff08\u4e3b\u5b9e\u4f53sheet\u7b2c\u4e00\u884c\uff09\u3001\u5b9e\u4f53\u7f16\u7801\u548c\u540d\u79f0\uff08\u4e3b\u5b9e\u4f53sheet\u7b2c\u4e8c\u884c\u3001\u5b50\u5b9e\u4f53sheet\u7b2c\u4e00\u884c\uff09\u3001\u5f00\u542f\u5fc5\u5f55\u7684\u5b57\u6bb5\uff08\u5e26*\u7684\u5b57\u6bb5\uff09", (String)"TemplateConfPlugin_29", (String)"hrmp-hies-import", (Object[])new Object[0]));
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{"localuploadremind"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"advconapcustomtpl"});
        }
        this.getView().setVisible(Boolean.valueOf(!"SE".equals(model.getValue("entitytype"))), new String[]{"advconap7"});
        this.updateImportType(entityType);
        model.setValue("importtype", (Object)importType);
        this.getView().setVisible(Boolean.valueOf(!"update".equalsIgnoreCase(importType) && isImport), new String[]{"configfieldcond"});
        this.resetQueryEntityTips();
        this.updateEntityType(false);
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
            this.getView().updateControlMetadata("hies_diaetplconf", (Map)scopeCssMap);
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
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "dopreview": {
                args.setCancel(true);
                this.getView().getFormShowParameter().getCustomParams().put("clickBtn", DO_PREVIEW);
                List<String> tplConfErrMsg = this.checkPreViewTpl();
                if (!CollectionUtils.isEmpty(tplConfErrMsg)) {
                    if (tplConfErrMsg.size() == 1) {
                        this.getView().showErrorNotification(tplConfErrMsg.get(0));
                    } else if (tplConfErrMsg.size() > 1) {
                        this.getView().showMessage(ResManager.loadKDString((String)"\u9884\u89c8\u6a21\u677f\u5931\u8d25", (String)"TemplateConfPlugin_21", (String)"hrmp-hies-import", (Object[])new Object[0]), Joiner.on((String)"\r\n").join(tplConfErrMsg), MessageTypes.Default);
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
                TemplateFormCommonUtil.removeAllTabpage((IFormView)this.getView(), (Map)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()));
                break;
            }
            case "save_btnok": {
                IDataModel model2 = this.getModel();
                String allocationPolicy = (String)model2.getValue("allocationpolicy");
                if (!"1".equals(allocationPolicy)) break;
                String applyScopeStr = this.getApplyScopeStr(model2);
                if (StringUtils.isBlank((CharSequence)applyScopeStr)) {
                    this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6a21\u677f\u4f7f\u7528\u8303\u56f4\u3002", (String)"TemplateConfPlugin_6", (String)"hrmp-hies-import", (Object[])new Object[0]));
                    args.setCancel(true);
                }
                model2.setValue("applyscope", (Object)applyScopeStr);
                break;
            }
            case "save": 
            case "saveandnew": {
                DynamicObject bizobject;
                String mainEntityNumber;
                DynamicObjectCollection entryEntity;
                String entitytype;
                boolean isNew;
                op.getOption().setVariableValue("bos_checkFormDataVersion", "false");
                IDataModel model = this.getModel();
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
                boolean bl = isNew = !this.getModel().getDataEntity().getDataEntityState().getFromDatabase();
                if (isNew) {
                    Object tpGenMode = this.getView().getFormShowParameter().getCustomParam("tplgenmode");
                    if (!ObjectUtils.isEmpty((Object)tpGenMode)) {
                        model.setValue("tplgenmode", tpGenMode);
                    }
                } else {
                    String tplGenMode = (String)this.getView().getFormShowParameter().getCustomParam("tplgenmode");
                    if (StringUtils.isBlank((CharSequence)tplGenMode)) {
                        tplGenMode = (String)this.getModel().getValue("tplgenmode");
                    }
                    if (TplGenModeConstant.isLocalUpload((String)tplGenMode)) {
                        DynamicObjectCollection dataImportCfgEntrys = this.getModel().getEntryEntity("entryentity");
                        if (CollectionUtils.isEmpty((Collection)dataImportCfgEntrys)) {
                            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u4e0a\u4f20\u672c\u5730\u6a21\u677f\u6587\u4ef6\u3002", (String)"TemplateConfPlugin_16", (String)"hrmp-hies-import", (Object[])new Object[0]));
                            args.setCancel(true);
                            return;
                        }
                        String enable = (String)this.getModel().getValue("enable");
                        if (!"0".equals(enable)) {
                            this.getModel().setValue("enable", (Object)"1");
                        }
                    }
                }
                String allocationPolicy2 = (String)model.getValue("allocationpolicy");
                if ("1".equals(allocationPolicy2)) {
                    String applyScopeStr = this.getApplyScopeStr(model);
                    if (StringUtils.isBlank((CharSequence)applyScopeStr)) {
                        this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6a21\u677f\u4f7f\u7528\u8303\u56f4\u3002", (String)"TemplateConfPlugin_6", (String)"hrmp-hies-import", (Object[])new Object[0]));
                        args.setCancel(true);
                        return;
                    }
                    model.setValue("applyscope", (Object)applyScopeStr);
                }
                if (!"SE".equals(entitytype = (String)model.getValue("entitytype")) && CollectionUtils.isEmpty((Collection)(entryEntity = this.getModel().getEntryEntity("entityrelation")))) {
                    this.getView().showErrorNotification(ResManager.loadKDString((String)"\u591a\u5b9e\u4f53\u6a21\u677f\u9700\u81f3\u5c11\u5173\u8054\u4e00\u4e2a\u5b50\u5b9e\u4f53\u3002", (String)"TemplateConfPlugin_7", (String)"hrmp-hies-import", (Object[])new Object[0]));
                    args.setCancel(true);
                    return;
                }
                String number = (String)model.getValue("number");
                if (HRStringUtils.equals((String)number, (String)(mainEntityNumber = (bizobject = (DynamicObject)model.getValue(FIELD_BIZOBJ)).getString("number")))) {
                    this.getView().showTipNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u201c\u7f16\u7801\u201d \u503c \u201c%s\u201d \u5df2\u5b58\u5728\uff0c\u8bf7\u8f93\u5165\u5176\u4ed6\u503c\u3002", (String)"TemplateConfPlugin_12", (String)"hrmp-hies-import", (Object[])new Object[0]), mainEntityNumber));
                    args.setCancel(true);
                    return;
                }
                HashMap<String, List> importFieldMap = new HashMap<String, List>(16);
                DynamicObjectCollection dynColl = model.getEntryEntity("tpltreeentryentity");
                for (DynamicObject dyn : dynColl) {
                    String entityNumber = dyn.getString("childentity");
                    String fieldNumber = dyn.getString("fieldnumber");
                    boolean isExpt = dyn.getBoolean("isimport");
                    if (!Boolean.TRUE.equals(isExpt)) continue;
                    List fieldList = importFieldMap.getOrDefault(entityNumber, Lists.newArrayListWithExpectedSize((int)10));
                    fieldList.add(fieldNumber);
                    importFieldMap.put(entityNumber, fieldList);
                }
                String importText = ResManager.loadKDString((String)"\u5bfc\u5165", (String)HiesCommonRes.TemplateSaveValidator_1.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]);
                String exportText = ResManager.loadKDString((String)"\u5bfc\u51fa", (String)HiesCommonRes.TemplateSaveValidator_2.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]);
                String tmpltype = (String)this.getModel().getValue(FIELD_TEMPLATETYPE);
                String tip = "IMPT".equalsIgnoreCase(tmpltype) ? importText : exportText;
                LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)this.getView());
                for (Map.Entry entry : allEntity.entrySet()) {
                    String[] split = ((String)entry.getKey()).split(":");
                    String entityNumber = split[1];
                    List fieldList = (List)importFieldMap.get(entityNumber);
                    if (!CollectionUtils.isEmpty((Collection)fieldList)) continue;
                    this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6bcf\u4e2a\u5b9e\u4f53\u90fd\u5fc5\u987b\u81f3\u5c11\u914d\u7f6e\u4e00\u4e2a%1$s\u5b57\u6bb5\u3002", (String)"TemplateConfPlugin_13", (String)"hrmp-hies-import", (Object[])new Object[0]), tip));
                    args.setCancel(true);
                    return;
                }
                this.getView().getFormShowParameter().getCustomParams().put("clickBtn", "save");
                this.initDefFieldVal(this.getView());
                this.sortField(this.getView());
                this.getView().getFormShowParameter().setCustomParam("isNew", (Object)isNew);
                break;
            }
        }
    }

    private boolean checkDefField() {
        IFormView view = this.getView();
        LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)view);
        ArrayList fieldNumberList = Lists.newArrayListWithCapacity((int)16);
        for (Map.Entry entry : allEntity.entrySet()) {
            String key = (String)entry.getKey();
            String[] split = key.split(":");
            String tabPageKey = split[0];
            CustomTabPageId customTabPageId = TemplateFormCommonUtil.getTabPageIdCache((IFormView)view, (String)tabPageKey);
            IDataModel parentModel = view.getModel();
            String value = (String)parentModel.getValue(FIELD_TEMPLATETYPE);
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
            String message = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u201c%s\u201d\u5b57\u6bb5\u9ed8\u8ba4\u503c\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"TemplateConfPlugin_65", (String)"hrmp-hies-import", (Object[])new Object[0]), fieldNumber);
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
        TemplateEntityFieldUtil.cacheCusField((IFormView)view);
        this.rebuidTplFieldPage(fieldOrder);
    }

    private void rebuildFieldTree() {
        IFormView view = this.getView();
        TemplateFormCommonUtil.removeAllTabPageExEntity((IFormView)view);
        OperationStatus status = view.getFormShowParameter().getStatus();
        LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)view);
        TemplateEntityFieldUtil.buildAndRestoreFieldTree((IFormView)view, (IDataModel)this.getModel(), (LinkedHashMap)allEntity, (OperationStatus)status);
    }

    private boolean checkCusField() {
        JSONObject fieldOrder;
        boolean isImportTpl = "IMPT".equals(this.getModel().getValue(FIELD_TEMPLATETYPE));
        String importType = (String)this.getModel().getValue("importtype");
        boolean newCanEdit = "new".equals(importType) || "updateandnew".equals(importType);
        boolean isEntityTypeSE = "SE".equals(this.getModel().getValue("entitytype"));
        try {
            fieldOrder = this.getFieldOrder(this.getView());
        }
        catch (KDException ex) {
            if (ex.getErrorCode() != null && "bos.pageCacheInvalid".equals(ex.getErrorCode().getCode())) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u9875\u9762\u6b63\u5728\u52a0\u8f7d\u4e2d\uff0c\u8bf7\u7a0d\u540e\u518d\u64cd\u4f5c\u3002", (String)"TemplateConfPlugin_19", (String)"hrmp-hies-import", (Object[])new Object[0]));
                return true;
            }
            throw ex;
        }
        LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)this.getView());
        int entitySize = allEntity.size();
        HashMap<String, List> candidateFieldsMap = new HashMap<String, List>(entitySize);
        HashMap<String, List> defFieldsMap = new HashMap<String, List>(entitySize);
        HashMap entityMapName = new HashMap(entitySize);
        for (Map.Entry entry : allEntity.entrySet()) {
            String defFieldsStr;
            String key2 = (String)entry.getKey();
            String[] split = key2.split(":");
            String tabPageKey = split[0];
            entityMapName.put(tabPageKey, entry.getValue());
            String candidateFieldsStr = fieldOrder.getString(tabPageKey);
            if (StringUtils.isNotBlank((CharSequence)candidateFieldsStr)) {
                List candidateFields = kd.bos.dataentity.serialization.SerializationUtils.fromJsonStringToList((String)candidateFieldsStr, String.class);
                candidateFieldsMap.put(tabPageKey, candidateFields);
            }
            if (!StringUtils.isNotBlank((CharSequence)(defFieldsStr = fieldOrder.getString(tabPageKey + "deffieldnumber")))) continue;
            List defFields = kd.bos.dataentity.serialization.SerializationUtils.fromJsonStringToList((String)defFieldsStr, String.class);
            defFieldsMap.put(tabPageKey, defFields);
        }
        HashMap<String, Map> entityProps = new HashMap<String, Map>(entitySize);
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("tpltreeentryentity");
        Set entryIds = entryEntity.stream().filter(item -> !item.getBoolean("isfield") && HRStringUtils.isNotBlank((CharSequence)item.getString("fieldnumber"))).map(item -> item.getString("id")).collect(Collectors.toSet());
        Map entryFields = entryEntity.stream().filter(item -> item.getBoolean("isfield") && entryIds.contains(item.getString("pid"))).collect(Collectors.groupingBy(item -> item.getString("pid"), Collectors.mapping(item -> item.getString("fieldnumber"), Collectors.toList())));
        LinkedHashMap<String, List> toCheckEntryFields = new LinkedHashMap<String, List>(entryFields.size());
        HashMap<String, Boolean> isExistOtherEntryFields = new HashMap<String, Boolean>(4);
        ArrayList toCheckField = Lists.newArrayListWithExpectedSize((int)16);
        for (DynamicObject dynamicObject : entryEntity) {
            boolean isimport;
            String entityNumber = dynamicObject.getString("childentity");
            String fieldNumber = dynamicObject.getString("fieldnumber");
            String fieldName = dynamicObject.getString("fieldname");
            boolean isCusField = dynamicObject.getBoolean("iscusfield");
            boolean isField = dynamicObject.getBoolean("isfield");
            boolean isMustInputField = StringUtils.isNotBlank((CharSequence)fieldNumber) && StringUtils.isNotBlank((CharSequence)fieldName) && fieldName.contains("*");
            Long pkValue = dynamicObject.getLong("id");
            List candidateFields = (List)candidateFieldsMap.get(entityNumber);
            List defFields = (List)defFieldsMap.get(entityNumber);
            if (isImportTpl && !isCusField && newCanEdit && isMustInputField && !candidateFields.contains(fieldNumber) && !defFields.contains(fieldNumber)) {
                String parentName;
                String name;
                IDataEntityProperty property;
                Map propertyMap = (Map)entityProps.get(entityNumber);
                if (MapUtils.isEmpty((Map)propertyMap)) {
                    propertyMap = EntityMetadataCache.getDataEntityType((String)entityNumber).getAllFields();
                    entityProps.put(entityNumber, propertyMap);
                }
                if (ObjectUtils.isEmpty((Object)(property = (IDataEntityProperty)propertyMap.get(fieldNumber))) || "billno".equals(name = property.getName()) || property instanceof BillStatusProp) continue;
                String notificationName = fieldName;
                if (StringUtils.isNotBlank((CharSequence)fieldName) && fieldName.contains("*")) {
                    notificationName = notificationName.replace("*", "");
                }
                if (!entityNumber.equals(parentName = property.getParent().getName())) {
                    String pid = dynamicObject.getString("pid");
                    ArrayList<String> entryCheckFields = (ArrayList<String>)toCheckEntryFields.get(pid);
                    if (HRCollUtil.isEmpty((Collection)entryCheckFields)) {
                        entryCheckFields = new ArrayList<String>(8);
                    }
                    if (!isEntityTypeSE) {
                        notificationName = ((String)entityMapName.get(entityNumber)).concat(".").concat(property.getParent().getDisplayName().toString()).concat(".").concat(notificationName);
                        entryCheckFields.add(notificationName + "(" + fieldNumber + ")");
                    } else {
                        entryCheckFields.add(notificationName + "(" + fieldNumber + ")");
                    }
                    toCheckEntryFields.put(pid, entryCheckFields);
                } else if (!isEntityTypeSE) {
                    notificationName = ((String)entityMapName.get(entityNumber)).concat(".").concat(notificationName);
                    toCheckField.add(notificationName + "(" + fieldNumber + ")");
                } else {
                    toCheckField.add(notificationName + "(" + fieldNumber + ")");
                }
            }
            if (isimport = dynamicObject.getBoolean("isimport")) {
                String pid = dynamicObject.getString("pid");
                isExistOtherEntryFields.put(pid, true);
            }
            if (!isCusField) continue;
            if (StringUtils.isBlank((CharSequence)fieldNumber) && isField) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6a21\u677f\u5b57\u6bb5\u914d\u7f6e\u4e2d\u81ea\u5b9a\u4e49\u5b57\u6bb5\u7684\u201c\u5b57\u6bb5\u7f16\u7801\u201d\u3002", (String)"TemplateConfPlugin_26", (String)"hrmp-hies-import", (Object[])new Object[0]));
                return true;
            }
            if (StringUtils.isBlank((CharSequence)fieldName) && isField) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6a21\u677f\u5b57\u6bb5\u914d\u7f6e\u4e2d\u81ea\u5b9a\u4e49\u5b57\u6bb5\u7684\u201c\u5b57\u6bb5\u540d\u79f0\u201d\u3002", (String)"TemplateConfPlugin_28", (String)"hrmp-hies-import", (Object[])new Object[0]));
                return true;
            }
            if (HRStringUtils.isNotEmpty((String)fieldNumber) && !HIESConstant.CUSTOM_NUMBER_PATTERN.matcher(fieldNumber).matches()) {
                this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u81ea\u5b9a\u4e49\u5b57\u6bb5\u201c\u5b57\u6bb5\u7f16\u7801\u201d\u201c%s\u201d\u4e0d\u5408\u6cd5\uff0c\u4ec5\u652f\u6301\u5b57\u6bcd\u3001\u4e0b\u5212\u7ebf\u53ca\u6570\u5b57\uff0c\u4e14\u4e0d\u5f97\u4ee5\u6570\u5b57\u5f00\u5934\u3002", (String)"TemplateConfPlugin_70", (String)"hrmp-hies-import", (Object[])new Object[0]), fieldNumber));
                return true;
            }
            if (HRStringUtils.isNotEmpty((String)fieldName) && !HIESConstant.CUSTOM_NAME_PATTERN.matcher(fieldName).matches()) {
                this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u81ea\u5b9a\u4e49\u5b57\u6bb5\u201c\u5b57\u6bb5\u540d\u79f0\u201d\u201c%s\u201d\u4e0d\u5408\u6cd5\uff0c\u4e0d\u652f\u6301\u9664\u4e0b\u5212\u7ebf\u4ee5\u5916\u7684\u5176\u4ed6\u7279\u6b8a\u5b57\u7b26\u3002", (String)"TemplateConfPlugin_71", (String)"hrmp-hies-import", (Object[])new Object[0]), fieldName));
                return true;
            }
            for (DynamicObject dynamicObject2 : this.getModel().getEntryEntity("tpltreeentryentity")) {
                String fieldNumber2 = dynamicObject2.getString("fieldnumber");
                Long pkValue2 = dynamicObject2.getLong("id");
                if (!HRStringUtils.equals((String)fieldNumber2, (String)fieldNumber) || !isField || pkValue2.equals(pkValue)) continue;
                this.getView().showErrorNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6a21\u677f\u5b57\u6bb5\u914d\u7f6e\u4e2d\u5b57\u6bb5\u7f16\u7801\u201c%1$s\u201d\u91cd\u590d\uff0c\u8bf7\u68c0\u67e5\u3002", (String)"TemplateConfPlugin_27", (String)"hrmp-hies-common", (Object[])new Object[0]), fieldNumber));
                return true;
            }
        }
        toCheckEntryFields.forEach((key, val) -> {
            if (Boolean.TRUE.equals(isExistOtherEntryFields.get(key))) {
                toCheckField.addAll(val);
            }
        });
        if (!CollectionUtils.isEmpty((Collection)toCheckField)) {
            String fieldNumber = Joiner.on((String)",").join((Iterable)toCheckField);
            String message = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u68c0\u6d4b\u5230\u201c%s\u201d\u5b57\u6bb5\u662f\u5fc5\u586b\u9879\uff0c\u4f46\u5728\u201c\u6a21\u677f\u5b57\u6bb5\u914d\u7f6e\u201d\u4e2d\u672a\u5f00\u542f\u201c\u5fc5\u5f55\u9879\u201d\uff0c\u5728\u201c\u5b57\u6bb5\u9ed8\u8ba4\u503c\u914d\u7f6e\u201d\u4e2d\u4e5f\u6ca1\u6709\u7ef4\u62a4\uff1b\u8bf7\u786e\u8ba4\u8be5\u5b57\u6bb5\u662f\u5426\u6709\u989d\u5916\u7684\u5f00\u53d1\u903b\u8f91\u5904\u7406\uff0c\u5426\u5219\u6570\u636e\u65e0\u6cd5\u6210\u529f\u5b58\u50a8\u3002", (String)"TemplateConfPlugin_50", (String)"hrmp-hies-import", (Object[])new Object[0]), fieldNumber);
            this.getView().showTipNotification(message, Integer.valueOf(3000));
        }
        return false;
    }

    private JSONObject getFieldOrder(IFormView view) {
        JSONObject fieldOrder = new JSONObject();
        LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)view);
        for (Map.Entry entry : allEntity.entrySet()) {
            String key = (String)entry.getKey();
            String[] split = key.split(":");
            String tabPageKey = split[0];
            CustomTabPageId customTabPageId = TemplateFormCommonUtil.getTabPageIdCache((IFormView)view, (String)tabPageKey);
            IDataModel parentModel = view.getModel();
            String value = (String)parentModel.getValue(FIELD_TEMPLATETYPE);
            ArrayList fieldNumberList = Lists.newArrayListWithCapacity((int)16);
            Set defFieldNumberList = Sets.newHashSetWithExpectedSize((int)16);
            if (!ObjectUtils.isEmpty((Object)customTabPageId)) {
                IFormView childView = view.getView(customTabPageId.getTabPageId());
                IDataModel model = childView.getModel();
                DynamicObjectCollection entryEntity = model.getEntryEntity("treeentryentity");
                for (DynamicObject dynamicObject : entryEntity) {
                    boolean isField = dynamicObject.getBoolean("isfield");
                    if (!isField) continue;
                    fieldNumberList.add(dynamicObject.getString("fieldnumber"));
                }
                defFieldNumberList = TemplateFormCommonUtil.getAllListIdSet((IDataModel)model, (String)"cusfieldentryentity", (String)"deffieldnumber");
            }
            fieldOrder.put(tabPageKey, (Object)JSON.toJSONString((Object)fieldNumberList));
            fieldOrder.put(tabPageKey + "deffieldnumber", (Object)JSON.toJSONString((Object)defFieldNumberList));
        }
        return fieldOrder;
    }

    private boolean checkTemplate() {
        DynamicObjectCollection entryEntity;
        String tplGenMode;
        boolean forceCheck;
        DynamicObject dataEntity = this.getModel().getDataEntity();
        ArrayList msgList = Lists.newArrayListWithExpectedSize((int)4);
        String number = dataEntity.getString("number");
        if (HRStringUtils.isNotEmpty((String)number) && (HIESConstant.SpecCharPattern.matcher(number).find() || HIESConstant.CHINESE_PATTERN.matcher(number).find())) {
            msgList.add(ResManager.loadKDString((String)"\u7f16\u7801\u4e0d\u5408\u6cd5\uff0c\u4ec5\u652f\u6301\u8f93\u5165\u5b57\u6bcd\u3001\u4e0b\u5212\u7ebf\u53ca\u6570\u5b57\u3002", (String)"TemplateConfPlugin_62", (String)"hrmp-hies-import", (Object[])new Object[0]));
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
            String importType;
            boolean isNew;
            FieldTip fieldTip;
            boolean isImport = "IMPT".equals(this.getModel().getValue(FIELD_TEMPLATETYPE));
            if (HRStringUtils.isEmpty((String)number)) {
                msgList.add(ResManager.loadKDString((String)"\u201c\u7f16\u7801\u201d", (String)"TemplateConfPlugin_22", (String)"hrmp-hies-import", (Object[])new Object[0]));
                fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "number", ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)"TemplateConfPlugin_15", (String)"hrmp-hies-import", (Object[])new Object[0]));
                this.getView().showFieldTip(fieldTip);
            }
            if (HRStringUtils.isEmpty((String)name.toString())) {
                msgList.add(ResManager.loadKDString((String)"\u201c\u6a21\u677f\u540d\u79f0\u201d", (String)"TemplateConfPlugin_23", (String)"hrmp-hies-import", (Object[])new Object[0]));
                fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "name", ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)"TemplateConfPlugin_15", (String)"hrmp-hies-import", (Object[])new Object[0]));
                this.getView().showFieldTip(fieldTip);
            }
            if (ObjectUtils.isEmpty((Object)dataEntity.get(FIELD_BIZOBJ))) {
                msgList.add(ResManager.loadKDString((String)"\u201c\u5b9e\u4f53\u201d", (String)"TemplateConfPlugin_24", (String)"hrmp-hies-import", (Object[])new Object[0]));
                fieldTip = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, FIELD_BIZOBJ, ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)"TemplateConfPlugin_15", (String)"hrmp-hies-import", (Object[])new Object[0]));
                this.getView().showFieldTip(fieldTip);
            }
            if (!(isNew = OprCategory.isNew((String)(importType = (String)this.getModel().getValue("importtype")))) && isImport && ObjectUtils.isEmpty((Object)dataEntity.get("mainentityuniqueval"))) {
                msgList.add(ResManager.loadKDString((String)"\u201c\u9700\u66f4\u65b0\u8bb0\u5f55\u8bc6\u522b\u5b57\u6bb5\u201d", (String)"TemplateConfPlugin_25", (String)"hrmp-hies-import", (Object[])new Object[0]));
                FieldTip fieldTip2 = new FieldTip(FieldTip.FieldTipsLevel.Error, FieldTip.FieldTipsTypes.notNull, "mainentityuniqueval", ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)"TemplateConfPlugin_15", (String)"hrmp-hies-import", (Object[])new Object[0]));
                this.getView().showFieldTip(fieldTip2);
            }
        }
        if (StringUtils.isBlank((CharSequence)(tplGenMode = (String)this.getView().getFormShowParameter().getCustomParam("tplgenmode")))) {
            tplGenMode = (String)this.getModel().getValue("tplgenmode");
        }
        if (TplGenModeConstant.isLocalUpload((String)tplGenMode) && !CollectionUtils.isEmpty((Collection)(entryEntity = this.getModel().getEntryEntity("entryentity")))) {
            DynamicObject dynRowOne = (DynamicObject)entryEntity.get(0);
            if (dynRowOne.getInt("sheetreadrow") <= 4) {
                msgList.add(String.format(ResManager.loadKDString((String)"\u201c%1$s\u201dSheet\u9875\u6570\u636e\u5bfc\u5165\u884c\u8bbe\u7f6e\u8bf7\u8f93\u5165\u5927\u4e8e%2$s\u7684\u6574\u6570\u3002", (String)"TemplateConfPlugin_30", (String)"hrmp-hies-import", (Object[])new Object[0]), dynRowOne.getString("sheetname"), 4));
            }
            for (int i = 1; i < entryEntity.size(); ++i) {
                DynamicObject dynRow = (DynamicObject)entryEntity.get(i);
                if (dynRow.getInt("sheetreadrow") > 3) continue;
                msgList.add(String.format(ResManager.loadKDString((String)"\u201c%1$s\u201dSheet\u9875\u6570\u636e\u5bfc\u5165\u884c\u8bbe\u7f6e\u8bf7\u8f93\u5165\u5927\u4e8e%2$s\u7684\u6574\u6570\u3002", (String)"TemplateConfPlugin_30", (String)"hrmp-hies-import", (Object[])new Object[0]), dynRow.getString("sheetname"), 3));
            }
        }
        if (!CollectionUtils.isEmpty((Collection)msgList)) {
            if (msgList.size() == 1) {
                this.getView().showErrorNotification((String)msgList.get(0));
                return true;
            }
            if (msgList.size() > 1) {
                this.getView().showMessage(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25", (String)"TemplateConfPlugin_66", (String)"hrmp-hies-import", (Object[])new Object[0]), Joiner.on((String)"\r\n").join((Iterable)msgList), MessageTypes.Default);
                return true;
            }
        }
        return false;
    }

    public void beforeDeleteRow(BeforeDeleteRowEventArgs rowEventArgs) {
        super.beforeDeleteRow(rowEventArgs);
        EntryProp entryProp = rowEventArgs.getEntryProp();
        if ("entityrelation".equals(entryProp.getName())) {
            int[] rowIndices = rowEventArgs.getRowIndexs();
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity(entryProp.getName());
            for (int rowIndex : rowIndices) {
                DynamicObject subEntity = ((DynamicObject)entryEntity.get(rowIndex)).getDynamicObject("rentity");
                String entityId = subEntity.getString("id");
                TemplateFormCommonUtil.removeTabPageAndEntry((IFormView)this.getView(), (String)entityId);
            }
            this.getView().updateView("tpltreeentryentity");
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        IDataModel model = this.getModel();
        String propertyName = args.getProperty().getName();
        ChangeData[] changeSet = args.getChangeSet();
        String value = (String)model.getValue(FIELD_TEMPLATETYPE);
        boolean isImport = "IMPT".equalsIgnoreCase(value);
        String downloadCond = (String)model.getValue("enabledowncond");
        boolean isDownloadCond = "1".equals(downloadCond);
        boolean isOpen = TemplateEntityFieldUtil.mustInputIsOpen((IDataModel)model);
        boolean canEdit = TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)model);
        boolean baseCanEdit = TemplateEntityFieldUtil.baseCanEdit((IDataModel)model);
        String uniqueval = (String)model.getValue("mainentityuniqueval");
        LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)this.getView());
        DynamicObject bizobject = (DynamicObject)model.getValue("queryentity");
        String mainEntityNumber = "";
        String queryEntityNumber = "";
        boolean isQueryEn = false;
        if (Objects.nonNull(bizobject)) {
            mainEntityNumber = Objects.nonNull(bizobject) ? bizobject.getString("number") : "";
            queryEntityNumber = Objects.nonNull(bizobject) ? bizobject.getString("number") : "";
            MainEntityType type = EntityMetadataCache.getDataEntityType((String)mainEntityNumber);
            if (type instanceof QueryEntityType) {
                mainEntityNumber = ((QueryEntityType)type).getMainEntityType().getName();
                isQueryEn = true;
            }
        }
        switch (propertyName) {
            case "queryentity": {
                this.getView().setVisible(Boolean.TRUE, new String[]{FIELD_CONF_PANEL});
                Container flexPanel = (Container)this.getView().getControl(FIELD_CONF_PANEL);
                flexPanel.setCollapse(false);
                this.getModel().beginInit();
                this.updateEntityType(true);
                this.getModel().endInit();
                this.getView().updateView("entitytype");
                DynamicObject oldMainValue = (DynamicObject)changeSet[0].getOldValue();
                DynamicObject newValue = (DynamicObject)changeSet[0].getNewValue();
                if (Objects.isNull(oldMainValue) && Objects.nonNull(newValue)) {
                    boolean bl;
                    MainEntityType mainEntityType;
                    String newId = newValue.getString("id");
                    String newName = newValue.getString("name");
                    String newNumber = newValue.getString("number");
                    MainEntityType mainType = EntityMetadataCache.getDataEntityType((String)newNumber);
                    if (!TemplateFormCommonUtil.checkQueryEntity((IFormView)this.getView(), (MainEntityType)mainType)) {
                        this.getModel().setValue("queryentity", (Object)"");
                        return;
                    }
                    HashSet entityTypes = Sets.newHashSetWithExpectedSize((int)16);
                    boolean bl2 = false;
                    if (mainType instanceof QueryEntityType) {
                        QueryEntityType queryType = (QueryEntityType)mainType;
                        mainEntityType = queryType.getMainEntityType();
                        entityTypes.addAll(queryType.getAllJoinEntityType());
                        bl = true;
                        entityTypes.removeIf(it -> it.getName().equals(mainEntityType.getName()));
                    } else {
                        mainEntityType = null;
                        entityTypes.add(mainType);
                    }
                    if (bl && mainEntityType != null) {
                        this.getModel().setValue(FIELD_BIZOBJ, (Object)mainEntityType.getName());
                        TemplateFormCommonUtil.buildQueryEntityPage((IFormView)this.getView(), (String)queryEntityNumber, (boolean)isImport, (boolean)isDownloadCond, (boolean)isOpen, (boolean)canEdit, (boolean)baseCanEdit, (String)uniqueval, (Set)entityTypes, (MainEntityType)mainEntityType);
                    } else {
                        this.getModel().setValue(FIELD_BIZOBJ, (Object)newNumber);
                        TemplateFormCommonUtil.addTabPage((IFormView)this.getView(), (CustomTabPage)new CustomTabPage(newNumber, newName), (String)newNumber, (String)queryEntityNumber, (boolean)isImport, (boolean)isDownloadCond, (Boolean)isOpen, (Boolean)Boolean.TRUE, (Boolean)canEdit, (Boolean)baseCanEdit, (String)uniqueval, (OperationStatus)OperationStatus.EDIT);
                        EntityFieldContext entityFieldContext = new EntityFieldContext(this.getView(), newNumber, "tpltreeentryentity", isImport, isOpen, canEdit, baseCanEdit, uniqueval);
                        TemplateEntityFieldUtil.newInstance().addTreeEntryRow(entityFieldContext);
                    }
                } else if (Objects.isNull(newValue) && Objects.nonNull(oldMainValue)) {
                    String oldId = oldMainValue.getString("id");
                    this.getModel().setValue(FIELD_BIZOBJ, (Object)"");
                    MainEntityType oldMainType = EntityMetadataCache.getDataEntityType((String)oldId);
                    if (oldMainType instanceof QueryEntityType) {
                        TemplateFormCommonUtil.removeAllTabPage((IFormView)this.getView());
                    } else {
                        TemplateFormCommonUtil.removeTabPageAndEntry((IFormView)this.getView(), (String)oldId);
                    }
                } else if (Objects.nonNull(oldMainValue) && Objects.nonNull(newValue)) {
                    String newId = newValue.getString("id");
                    String newName = newValue.getString("name");
                    String newNumber = newValue.getString("number");
                    String oldNumber = oldMainValue.getString("number");
                    MainEntityType oldMainType = EntityMetadataCache.getDataEntityType((String)oldNumber);
                    MainEntityType newMainType = EntityMetadataCache.getDataEntityType((String)newNumber);
                    if (!TemplateFormCommonUtil.checkQueryEntity((IFormView)this.getView(), (MainEntityType)newMainType)) {
                        this.getModel().setValue("queryentity", (Object)"");
                        return;
                    }
                    if (oldMainType instanceof QueryEntityType) {
                        TemplateFormCommonUtil.removeAllTabPage((IFormView)this.getView());
                    } else {
                        String string = oldMainValue.getString("id");
                        TemplateFormCommonUtil.removeTabPageAndEntry((IFormView)this.getView(), (String)string);
                        TemplateFormCommonUtil.removeAllTabPageExEntity((IFormView)this.getView());
                    }
                    if (newMainType instanceof QueryEntityType) {
                        QueryEntityType queryEntityType = (QueryEntityType)newMainType;
                        MainEntityType mainEntityType = queryEntityType.getMainEntityType();
                        HashSet entityTypes = Sets.newHashSet((Iterable)queryEntityType.getAllJoinEntityType());
                        entityTypes.removeIf(it -> it.getName().equals(mainEntityType.getName()));
                        this.getModel().setValue(FIELD_BIZOBJ, (Object)mainEntityType.getName());
                        TemplateFormCommonUtil.buildQueryEntityPage((IFormView)this.getView(), (String)queryEntityNumber, (boolean)isImport, (boolean)isDownloadCond, (boolean)isOpen, (boolean)canEdit, (boolean)baseCanEdit, (String)uniqueval, (Set)entityTypes, (MainEntityType)mainEntityType);
                    } else {
                        this.getModel().setValue(FIELD_BIZOBJ, (Object)newNumber);
                        EntityFieldContext entityFieldContext = new EntityFieldContext(this.getView(), newId, "tpltreeentryentity", isImport, isOpen, canEdit, baseCanEdit, uniqueval);
                        TemplateEntityFieldUtil.newInstance().addTreeEntryRow(entityFieldContext);
                        TemplateEntityFieldUtil.rebuildEntryEntityAndInit((IFormView)this.getView(), (IDataModel)model, (boolean)isImport, (boolean)isDownloadCond, (boolean)isOpen, (Boolean)Boolean.FALSE, (Boolean)canEdit, (Boolean)baseCanEdit, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()), (OperationStatus)OperationStatus.EDIT, null, new HashMap(16));
                        TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)newId);
                    }
                    this.getModel().setValue("mainentityuniqueval", (Object)"");
                    this.getView().updateView("mainentityuniqueval");
                }
                this.getModel().setValue("fieldstyle", (Object)"");
                this.getModel().setValue("fieldmerge", (Object)"");
                this.getView().setVisible(Boolean.valueOf(!isQueryEn), new String[]{"subentity_new"});
                this.resetQueryEntityTips();
                TemplateFormCommonUtil.displayEnablePersonIdImport((IFormView)this.getView());
                break;
            }
            case "allocationpolicy": {
                this.activeApplyScopeTab();
                break;
            }
            case "entitytype": {
                String importType = (String)model.getValue("importtype");
                String oldEntityType = (String)changeSet[0].getOldValue();
                String entityType = (String)changeSet[0].getNewValue();
                model.beginInit();
                this.updateImportType(entityType);
                this.getModel().setValue("importtype", (Object)importType);
                model.endInit();
                this.showMainEntityAliastEXT(entityType);
                if (Objects.isNull(bizobject)) break;
                if (TemplateConfPlugin.isSwitchSe(oldEntityType, entityType)) {
                    model.beginInit();
                    model.deleteEntryData("entityrelation");
                    model.endInit();
                    for (Map.Entry entityEntry : allEntity.entrySet()) {
                        String[] split = ((String)entityEntry.getKey()).split(":");
                        String string = split[1];
                        if (string.equals(mainEntityNumber)) continue;
                        TemplateFormCommonUtil.removeTabPageAndEntry((IFormView)this.getView(), (String)string);
                    }
                    this.getView().updateView("entityrelation");
                    break;
                }
                if (!TemplateConfPlugin.isSeSwitchOther(oldEntityType, entityType)) break;
                TemplateFormCommonUtil.removeTabPageAndEntry((IFormView)this.getView(), (String)mainEntityNumber);
                String mainEntityAlias = this.getView().getModel().getValue("mainentityalias").toString();
                if (HRStringUtils.isBlank((CharSequence)mainEntityAlias)) {
                    mainEntityAlias = bizobject.getString("name");
                }
                TemplateFormCommonUtil.addTabPage((IFormView)this.getView(), (CustomTabPage)new CustomTabPage(mainEntityNumber, mainEntityAlias), (String)mainEntityNumber, (String)queryEntityNumber, (boolean)isImport, (boolean)isDownloadCond, (Boolean)isOpen, (Boolean)Boolean.TRUE, (Boolean)canEdit, (Boolean)baseCanEdit, (String)uniqueval, (OperationStatus)OperationStatus.EDIT);
                EntityFieldContext entityFieldContext = new EntityFieldContext(this.getView(), mainEntityNumber, "tpltreeentryentity", isImport, isOpen, canEdit, baseCanEdit, uniqueval);
                TemplateEntityFieldUtil.newInstance().addTreeEntryRow(entityFieldContext);
                break;
            }
            case "importtype": {
                String newImportType = (String)changeSet[0].getNewValue();
                boolean newCanEdit = !"new".equals(newImportType) && !"updateandnew".equals(newImportType);
                this.getView().setVisible(Boolean.valueOf(!"update".equalsIgnoreCase(newImportType) && isImport), new String[]{"configfieldcond"});
                if ("update".equals(newImportType) || "updateandnew".equals(newImportType)) {
                    String tip3 = ResManager.loadKDString((String)"3.\u5982\u9700\u66f4\u65b0\u5df2\u6709\u6570\u636e\uff0c\u8bf7\u5148\u6309\u5bfc\u5165\u6a21\u677f\u5bfc\u51fa\u6570\u636e\uff0c\u4fee\u6539\u540e\u518d\u6b21\u5bfc\u5165\u3002", (String)"TemplateConfPlugin_35", (String)"hrmp-hies-import", (Object[])new Object[0]);
                    StringBuilder stringBuilder = new StringBuilder(HIESUtil.getTplInstruction2());
                    stringBuilder.append(HIESUtil.getSysLineSeparator());
                    stringBuilder.append(tip3);
                    this.getModel().setValue("instruction", (Object)stringBuilder.toString());
                } else {
                    this.getModel().setValue("instruction", (Object)HIESUtil.getTplInstruction());
                }
                if (Objects.isNull(bizobject)) break;
                for (Map.Entry entry : allEntity.entrySet()) {
                    String[] split = ((String)entry.getKey()).split(":");
                    String entityId = split[0];
                    String mustInputField = TemplateFormCommonUtil.getEntityMustInputField((IDataModel)model, (String)entityId);
                    String metaMustInputField = TemplateFormCommonUtil.getEntityMetaMustInputField((IDataModel)model, (String)entityId);
                    TemplateFormCommonUtil.refreshTabPage((IFormView)this.getView(), (String)entityId, (String)metaMustInputField, (String)"", (Boolean)newCanEdit);
                    TemplateFormCommonUtil.refreshTabPage((IFormView)this.getView(), (String)entityId, (String)mustInputField, (String)"", (Boolean)Boolean.FALSE);
                }
                TemplateFormCommonUtil.activeMainEntityTabPage((IFormView)this.getView());
                break;
            }
            case "mainentityuniqueval": {
                String oldUnique = (String)changeSet[0].getOldValue();
                String string = (String)changeSet[0].getNewValue();
                List<Object> cancelUnique = new ArrayList(16);
                List<Object> newAddUnique = new ArrayList(16);
                if (StringUtils.isNotBlank((CharSequence)oldUnique)) {
                    List oldUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)oldUnique).collect(Collectors.toList());
                    if (StringUtils.isNotBlank((CharSequence)string)) {
                        List newUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)string).collect(Collectors.toList());
                        oldUniqueList.removeAll(newUniqueList);
                    }
                    cancelUnique = oldUniqueList;
                }
                if (StringUtils.isNotBlank((CharSequence)string)) {
                    List newUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)string).collect(Collectors.toList());
                    if (StringUtils.isNotBlank((CharSequence)oldUnique)) {
                        List oldUniqueList = Splitter.on((String)",").omitEmptyStrings().trimResults().splitToStream((CharSequence)oldUnique).collect(Collectors.toList());
                        newUniqueList.removeAll(oldUniqueList);
                    }
                    newAddUnique = newUniqueList;
                }
                String cancelUniqueVal = Joiner.on((String)",").join(cancelUnique);
                String mustInputField = TemplateFormCommonUtil.getEntityMustInputField((IDataModel)model, (String)mainEntityNumber);
                TemplateFormCommonUtil.refreshTree4UniqueVal((IFormView)this.getView(), (String)mainEntityNumber, (String)mustInputField, (String)cancelUniqueVal);
                String newAddUniqueVal = Joiner.on((String)",").join(newAddUnique);
                TemplateFormCommonUtil.refreshTabPage4UniqueVal((IFormView)this.getView(), (String)mainEntityNumber, (String)newAddUniqueVal, (String)cancelUniqueVal);
                TemplateFormCommonUtil.updateParentEntry4UniqueVal((IFormView)this.getView(), (String)mainEntityNumber, (String)newAddUniqueVal);
                TemplateFormCommonUtil.lockShowField4UniqueVal((IFormView)this.getView(), (String)mainEntityNumber, (String)mustInputField);
                TemplateFormCommonUtil.activeMainEntityTabPage((IFormView)this.getView());
                break;
            }
            case "mainentityalias": {
                if (Objects.isNull(bizobject)) break;
                String entityNumber = bizobject.getString("number");
                MainEntityType newMainType = EntityMetadataCache.getDataEntityType((String)entityNumber);
                if (!TemplateFormCommonUtil.checkQueryEntity((IFormView)this.getView(), (MainEntityType)newMainType)) {
                    this.getModel().setValue("queryentity", (Object)"");
                    return;
                }
                this.getView().setEnable(Boolean.FALSE, new String[]{"bar_save"});
                TemplateFormCommonUtil.removeAllTabpage((IFormView)this.getView(), (Map)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()));
                TemplateEntityFieldUtil.buildAndRestoreFieldTree((IFormView)this.getView(), (IDataModel)model, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()), (OperationStatus)OperationStatus.EDIT);
                this.getView().setEnable(Boolean.TRUE, new String[]{"bar_save"});
                break;
            }
        }
    }

    private void resetQueryEntityTips() {
        String mainEntityNumber;
        MainEntityType type;
        String tplType = (String)this.getModel().getValue(FIELD_TEMPLATETYPE);
        boolean isImportTpl = "IMPT".equalsIgnoreCase(tplType);
        DynamicObject bizobject = (DynamicObject)this.getModel().getValue("queryentity");
        boolean isQueryEntity = false;
        if (Objects.nonNull(bizobject) && (type = EntityMetadataCache.getDataEntityType((String)(mainEntityNumber = Objects.nonNull(bizobject) ? bizobject.getString("number") : ""))) instanceof QueryEntityType) {
            isQueryEntity = true;
        }
        this.getView().setVisible(Boolean.valueOf(isQueryEntity), new String[]{"flexpanelap12"});
        if (isQueryEntity) {
            Label label1 = (Label)this.getView().getControl("labelap5");
            Label label2 = (Label)this.getView().getControl("labelap6");
            if (isImportTpl) {
                label1.setText(ResManager.loadKDString((String)"1.\u60a8\u6240\u9009\u7684\u5b9e\u4f53\u4e3a\u67e5\u8be2\u5b9e\u4f53\uff0c\u4f7f\u7528\u8be5\u6a21\u677f\u5bfc\u5165\u65f6\uff0c\u6839\u636e\u4e0b\u65b9\u914d\u7f6e\u7684\u4f9d\u8d56\u5173\u7cfb\u8fdb\u884c\u5bfc\u5165\uff1b", (String)"TemplateConfPlugin_52", (String)"hrmp-hies-import", (Object[])new Object[0]));
                label2.setText(ResManager.loadKDString((String)"2.\u4f7f\u7528\u201c\u6309\u5bfc\u5165\u6a21\u677f\u5bfc\u51fa\u201d\u65f6\uff0c\u4e3b\u5b50\u5b9e\u4f53\u4f9d\u8d56\u5173\u7cfb\u6839\u636e\u67e5\u8be2\u5b9e\u4f53\u914d\u7f6e\u7684\u5173\u7cfb\u8fdb\u884c\u5bfc\u51fa\uff1b", (String)"TemplateConfPlugin_53", (String)"hrmp-hies-import", (Object[])new Object[0]));
            } else {
                label1.setText(ResManager.loadKDString((String)"1.\u60a8\u6240\u9009\u7684\u5b9e\u4f53\u4e3a\u67e5\u8be2\u5b9e\u4f53\uff0c\u4e3b/\u5b50\u5b9e\u4f53\u4f9d\u8d56\u5173\u7cfb\u4ee5\u67e5\u8be2\u5b9e\u4f53\u914d\u7f6e\u7684\u5173\u7cfb\u4e3a\u51c6\uff0c\u6b64\u5904\u4e0d\u9700\u8981\u914d\u7f6e\u4e3b/\u5b50\u5b9e\u4f53\u7684\u4f9d\u8d56\u5173\u7cfb\uff1b", (String)"TemplateConfPlugin_54", (String)"hrmp-hies-import", (Object[])new Object[0]));
                label2.setText(ResManager.loadKDString((String)"2.\u4f7f\u7528\u8be5\u6a21\u677f\u5bfc\u51fa\u65f6\uff0c\u4e3b\u5b50\u5b9e\u4f53\u4f9d\u8d56\u5173\u7cfb\u6839\u636e\u67e5\u8be2\u5b9e\u4f53\u914d\u7f6e\u7684\u5173\u7cfb\u8fdb\u884c\u5bfc\u51fa\uff1b", (String)"TemplateConfPlugin_55", (String)"hrmp-hies-import", (Object[])new Object[0]));
            }
        }
        if (isQueryEntity && !isImportTpl) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"relationleftpropname", "relationcondition", "relationrightpropname"});
        } else {
            this.getView().setVisible(Boolean.valueOf(true), new String[]{"relationleftpropname", "relationcondition", "relationrightpropname"});
        }
        this.getView().setVisible(Boolean.valueOf(!isQueryEntity), new String[]{"subentity_new"});
    }

    private static boolean isSwitchSe(String oldEntityType, String newEntityType) {
        return "SE".equals(newEntityType) && ("MESS".equals(oldEntityType) || "MEMS".equals(oldEntityType));
    }

    private static boolean isSeSwitchOther(String oldEntityType, String newEntityType) {
        return "SE".equals(oldEntityType) && ("MESS".equals(newEntityType) || "MEMS".equals(newEntityType));
    }

    private void updateImportType(String entityType) {
        ComboEdit comboEdit = (ComboEdit)this.getControl("importtype");
        ArrayList<ComboItem> comboItems = new ArrayList<ComboItem>(4);
        if ("SE".equals(entityType)) {
            ComboItem newItem = new ComboItem();
            newItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u65b0\u589e", (String)"TemplateConfPlugin_8", (String)"hrmp-hies-import", (Object[])new Object[0])));
            newItem.setValue("new");
            ComboItem updateItem = new ComboItem();
            updateItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u66f4\u65b0", (String)"TemplateConfPlugin_9", (String)"hrmp-hies-import", (Object[])new Object[0])));
            updateItem.setValue("update");
            ComboItem newAndUpdateItem = new ComboItem();
            newAndUpdateItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u65b0\u589e\u5e76\u66f4\u65b0", (String)"TemplateConfPlugin_10", (String)"hrmp-hies-import", (Object[])new Object[0])));
            newAndUpdateItem.setValue("updateandnew");
            ComboItem deleteItem = new ComboItem();
            deleteItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u5220\u9664", (String)"TemplateConfPlugin_11", (String)"hrmp-hies-import", (Object[])new Object[0])));
            deleteItem.setValue("delete");
            comboItems.add(newItem);
            comboItems.add(updateItem);
            comboItems.add(newAndUpdateItem);
            comboItems.add(deleteItem);
            comboEdit.setComboItems(comboItems);
            this.getView().setVisible(Boolean.FALSE, new String[]{"advconap7"});
        } else {
            ComboItem newItem = new ComboItem();
            newItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u65b0\u589e", (String)"TemplateConfPlugin_8", (String)"hrmp-hies-import", (Object[])new Object[0])));
            newItem.setValue("new");
            ComboItem updateItem = new ComboItem();
            updateItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u66f4\u65b0", (String)"TemplateConfPlugin_9", (String)"hrmp-hies-import", (Object[])new Object[0])));
            updateItem.setValue("update");
            ComboItem newAndUpdateItem = new ComboItem();
            newAndUpdateItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u65b0\u589e\u5e76\u66f4\u65b0", (String)"TemplateConfPlugin_10", (String)"hrmp-hies-import", (Object[])new Object[0])));
            newAndUpdateItem.setValue("updateandnew");
            comboItems.add(newItem);
            comboItems.add(updateItem);
            comboItems.add(newAndUpdateItem);
            comboEdit.setComboItems(comboItems);
            this.getView().setVisible(Boolean.TRUE, new String[]{"advconap7"});
        }
    }

    private void updateEntityType(boolean isInit) {
        ComboItem seItem;
        String mainEntityNumber;
        MainEntityType type;
        ComboEdit comboEdit = (ComboEdit)this.getControl("entitytype");
        String entityType = (String)this.getModel().getValue("entitytype");
        ArrayList<ComboItem> comboItems = new ArrayList<ComboItem>(4);
        DynamicObject bizobject = (DynamicObject)this.getModel().getValue("queryentity");
        boolean isQueryEntity = false;
        ArrayList entityTypes = new ArrayList();
        if (Objects.nonNull(bizobject) && (type = EntityMetadataCache.getDataEntityType((String)(mainEntityNumber = Objects.nonNull(bizobject) ? bizobject.getString("number") : ""))) instanceof QueryEntityType) {
            entityTypes.addAll(((QueryEntityType)type).getAllJoinEntityType());
            isQueryEntity = true;
        }
        if (isQueryEntity) {
            if (CollectionUtils.isEmpty(entityTypes)) {
                seItem = new ComboItem();
                seItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u5355\u5b9e\u4f53", (String)"TemplateConfPlugin_56", (String)"hrmp-hies-import", (Object[])new Object[0])));
                seItem.setValue("SE");
                comboItems.add(seItem);
                comboEdit.setComboItems(comboItems);
                this.getView().setVisible(Boolean.FALSE, new String[]{"advconap7"});
            } else {
                ComboItem messItem = new ComboItem();
                messItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u591a\u5b9e\u4f53\u5355Sheet", (String)"TemplateConfPlugin_57", (String)"hrmp-hies-import", (Object[])new Object[0])));
                messItem.setValue("MESS");
                ComboItem memsItem = new ComboItem();
                memsItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u591a\u5b9e\u4f53\u591aSheet", (String)"TemplateConfPlugin_58", (String)"hrmp-hies-import", (Object[])new Object[0])));
                memsItem.setValue("MEMS");
                comboItems.add(messItem);
                comboItems.add(memsItem);
                comboEdit.setComboItems(comboItems);
                this.getView().setVisible(Boolean.TRUE, new String[]{"advconap7"});
            }
        } else {
            seItem = new ComboItem();
            seItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u5355\u5b9e\u4f53", (String)"TemplateConfPlugin_56", (String)"hrmp-hies-import", (Object[])new Object[0])));
            seItem.setValue("SE");
            ComboItem messItem = new ComboItem();
            messItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u591a\u5b9e\u4f53\u5355Sheet", (String)"TemplateConfPlugin_57", (String)"hrmp-hies-import", (Object[])new Object[0])));
            messItem.setValue("MESS");
            ComboItem memsItem = new ComboItem();
            memsItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u591a\u5b9e\u4f53\u591aSheet", (String)"TemplateConfPlugin_58", (String)"hrmp-hies-import", (Object[])new Object[0])));
            memsItem.setValue("MEMS");
            comboItems.add(seItem);
            comboItems.add(messItem);
            comboItems.add(memsItem);
            comboEdit.setComboItems(comboItems);
        }
        if (isInit) {
            if (isQueryEntity) {
                if (CollectionUtils.isEmpty(entityTypes)) {
                    this.getModel().setValue("entitytype", (Object)"SE");
                } else {
                    this.getModel().setValue("entitytype", (Object)"MESS");
                }
            }
        } else {
            this.getModel().setValue("entitytype", (Object)entityType);
        }
    }

    private void setControlVisible(boolean isImportTpl) {
        this.getView().setVisible(Boolean.valueOf(isImportTpl), new String[]{"advconapcustomtpl", "advconapexptpl", "advconapdescsheet", "importtype", "enabledowncond", "mainentityuniqueval", "instruction", "configfieldcond", "ismustinput", "isdownloadcond", "iscondgetdata", "entitydescription", "displayname", "fieldimportdesc", "imptattr", "hidelogotypelineflag"});
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

    private void updateNodeLevel(Object nodeid, Map<Object, List<Object>> data, DynamicObjectCollection collection) {
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            DynamicObject dynamicObject = (DynamicObject)iterator.next();
            boolean isEntityId = nodeid.equals(dynamicObject.getPkValue());
            if (!isEntityId) continue;
            this.getModel().beginInit();
            iterator.remove();
            this.getModel().endInit();
        }
    }

    public void click(EventObject evt) {
        switch (((Control)evt.getSource()).getKey()) {
            case "plugin": {
                this.showPlugin();
                break;
            }
            case "labelap_viewqueryentity": {
                String mainEntityNumber;
                Map<String, String> bizAppMap;
                String queryEntityId;
                DynamicObject bizobject = (DynamicObject)this.getModel().getValue("queryentity");
                if (!Objects.nonNull(bizobject) || !StringUtils.isNotBlank((CharSequence)(queryEntityId = (bizAppMap = this.getBizAppMapByNum(mainEntityNumber = Objects.nonNull(bizobject) ? bizobject.getString("number") : "")).get("id")))) break;
                this.openPage(queryEntityId);
                break;
            }
            case "labelap_downtpl": {
                List<String> tplConfErrMsg = this.checkPreViewTpl();
                if (!CollectionUtils.isEmpty(tplConfErrMsg)) {
                    if (tplConfErrMsg.size() == 1) {
                        this.getView().showErrorNotification(tplConfErrMsg.get(0));
                    } else if (tplConfErrMsg.size() > 1) {
                        this.getView().showMessage(ResManager.loadKDString((String)"\u7cfb\u7edf\u6a21\u677f\u4e0b\u8f7d\u5931\u8d25\u3002", (String)"TemplateConfPlugin_67", (String)"hrmp-hies-import", (Object[])new Object[0]), Joiner.on((String)"\r\n").join(tplConfErrMsg), MessageTypes.Default);
                    }
                    return;
                }
                this.sortField(this.getView());
                this.tplPreView();
                break;
            }
            case "labelap_manually": {
                FormShowParameter parameter = new FormShowParameter();
                parameter.setCaption(ResManager.loadKDString((String)"\u67e5\u770b\u6570\u636e\u5bfc\u5165\u884c\u793a\u4f8b", (String)"TemplateConfPlugin_45", (String)"hrmp-hies-import", (Object[])new Object[0]));
                parameter.getOpenStyle().setShowType(ShowType.Modal);
                parameter.setFormId("hies_tpltips");
                this.getView().showForm(parameter);
                break;
            }
        }
    }

    private void openPage(String queryEntityId) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_querydynsource");
        showParameter.setStatus(OperationStatus.EDIT);
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        String queryEntityNumber = MetadataDao.getNumberById((String)queryEntityId);
        showParameter.setCustomParam("queryEntityNumber", (Object)queryEntityNumber);
        showParameter.setCustomParam("queryEntityId", (Object)queryEntityId);
        Map<String, String> appMap = this.getBizAppMap(queryEntityId);
        showParameter.setCustomParam("currentBizAppId", (Object)appMap.get("appId"));
        showParameter.setCustomParam("currentUnitId", (Object)appMap.get("unitId"));
        this.getView().showForm(showParameter);
    }

    private Map<String, String> getBizAppMap(String queryEntityId) {
        String sql = "select a.FBIZAPPID,b.FBIZUNITID from t_meta_formdesign a left join t_meta_bizunitrelform b on a.FBIZAPPID = b.FBIZAPPID and a.fid = b.fformid where a.FID = ?";
        Object[] params = new SqlParameter[]{new SqlParameter(":FID", 12, (Object)queryEntityId)};
        ResultSetHandler callBackHanlder = rs -> {
            HashMap<String, String> bizUnitMap = new HashMap<String, String>();
            try {
                if (rs.next()) {
                    bizUnitMap.put("appId", rs.getString(1));
                    bizUnitMap.put("unitId", rs.getString(2));
                }
            }
            catch (SQLException e) {
                throw new KDException((Throwable)e, BosErrorCode.sQL, new Object[]{String.format(Locale.ROOT, "Error:%s", e.getMessage())});
            }
            return bizUnitMap;
        };
        return (Map)DB.query((DBRoute)DBRoute.meta, (String)sql, (Object[])params, (ResultSetHandler)callBackHanlder);
    }

    private Map<String, String> getBizAppMapByNum(String queryEntityNumber) {
        String sql = "select FID from t_meta_formdesign where FNUMBER = ?";
        Object[] params = new SqlParameter[]{new SqlParameter(":FID", 12, (Object)queryEntityNumber)};
        ResultSetHandler callBackHanlder = rs -> {
            HashMap<String, String> bizUnitMap = new HashMap<String, String>();
            try {
                if (rs.next()) {
                    bizUnitMap.put("id", rs.getString(1));
                }
            }
            catch (SQLException e) {
                throw new KDException((Throwable)e, BosErrorCode.sQL, new Object[]{String.format(Locale.ROOT, "Error:%s", e.getMessage())});
            }
            return bizUnitMap;
        };
        return (Map)DB.query((DBRoute)DBRoute.meta, (String)sql, (Object[])params, (ResultSetHandler)callBackHanlder);
    }

    private List<String> checkPreViewTpl() {
        OrmLocaleValue langName;
        ArrayList<String> tplConfErrMsg = new ArrayList<String>(4);
        DynamicObject mainEntity = (DynamicObject)this.getModel().getValue(FIELD_BIZOBJ);
        if (Objects.isNull(mainEntity)) {
            tplConfErrMsg.add(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u5b9e\u4f53", (String)"TemplateConfPlugin_5", (String)"hrmp-hies-import", (Object[])new Object[0]));
        }
        if (ObjectUtils.isEmpty((Object)(langName = (OrmLocaleValue)this.getModelVal("name")).getLocaleValue()) && ObjectUtils.isEmpty((Object)langName.get((Object)"GLang")) || ObjectUtils.isEmpty((Object)this.getModelVal("number"))) {
            tplConfErrMsg.add(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u5b8c\u6574\u7684\u7f16\u7801\u548c\u540d\u79f0\u3002", (String)"TemplateConfPlugin_32", (String)"hrmp-hies-import", (Object[])new Object[0]));
        }
        if (ObjectUtils.isEmpty((Object)this.getModel().getValue("entitytype"))) {
            tplConfErrMsg.add(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u5b9e\u4f53\u7c7b\u578b", (String)"TemplateConfPlugin_64", (String)"hrmp-hies-import", (Object[])new Object[0]));
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
                boolean isNew = (Boolean)this.getView().getFormShowParameter().getCustomParam("isNew");
                if (isNew) {
                    String tplGenMode = (String)this.getView().getFormShowParameter().getCustomParam("tplgenmode");
                    if (StringUtils.isBlank((CharSequence)tplGenMode)) {
                        tplGenMode = (String)this.getModel().getValue("tplgenmode");
                    }
                    if (TplGenModeConstant.isLocalUpload((String)tplGenMode) && !this.isExistLocalUploadFile()) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u6a21\u677f\u672a\u4e0a\u4f20\u672c\u5730\u6587\u4ef6\uff0c\u4fdd\u5b58\u540e\u72b6\u6001\u9ed8\u8ba4\u4e3a\u201c\u5f85\u542f\u7528\u201d\uff0c\u4e0a\u4f20\u672c\u5730\u6587\u4ef6\u540e\u65b9\u53ef\u542f\u7528\u3002", (String)"TemplateConfPlugin_17", (String)"hrmp-hies-import", (Object[])new Object[0]));
                    }
                }
                this.getView().getPageCache().put("preEnable", this.getModelValStr("enable"));
                break;
            }
            case "moveentryup": 
            case "moveentrydown": {
                EntryGrid entryentity = (EntryGrid)this.getControl("entityrelation");
                int[] selectRows = entryentity.getSelectRows();
                if (selectRows == null || selectRows.length == 0) {
                    return;
                }
                IDataModel model = this.getModel();
                DynamicObject bizobject = (DynamicObject)model.getValue("queryentity");
                String entityNumber = bizobject.getString("number");
                MainEntityType newMainType = EntityMetadataCache.getDataEntityType((String)entityNumber);
                if (!TemplateFormCommonUtil.checkQueryEntity((IFormView)this.getView(), (MainEntityType)newMainType)) {
                    this.getModel().setValue("queryentity", (Object)"");
                    return;
                }
                TemplateFormCommonUtil.removeAllTabpage((IFormView)this.getView(), (Map)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()));
                TemplateEntityFieldUtil.buildAndRestoreFieldTree((IFormView)this.getView(), (IDataModel)model, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)this.getView()), (OperationStatus)OperationStatus.EDIT);
                DynamicObject curDynObj = (DynamicObject)this.getModel().getValue("rentity");
                TemplateFormCommonUtil.activeTabPage((IFormView)this.getView(), (String)curDynObj.getString("id"));
                break;
            }
        }
    }

    private void showPlugin() {
        Object value = this.getModel().getValue("plugin");
        String formId = this.getView().getFormShowParameter().getFormId();
        List<?> plugins = this.getPluginsArray(value);
        FormShowParameter formShowParameter = this.buildShowParams(formId, plugins, new CloseCallBack((IFormPlugin)this, ACTION_SELECT_SERVICE_PLUGINS));
        this.getView().showForm(formShowParameter);
    }

    private List<?> getPluginsArray(Object val) {
        List plugins = new ArrayList();
        if (val != null && StringUtils.isNotEmpty((CharSequence)val.toString())) {
            plugins = kd.bos.dataentity.serialization.SerializationUtils.fromJsonStringToList((String)val.toString(), Map.class);
        }
        return plugins;
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
            Object value = this.getModel().getValue(FIELD_BIZOBJ);
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
        boolean isOpen = TemplateEntityFieldUtil.mustInputIsOpen((IDataModel)model);
        boolean canEdit = TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)model);
        boolean baseCanEdit = TemplateEntityFieldUtil.baseCanEdit((IDataModel)model);
        DynamicObject bizobject = (DynamicObject)model.getValue(FIELD_BIZOBJ);
        if (Objects.nonNull(bizobject)) {
            Map importTypeUniqueMap = TemplateFormCommonUtil.getAllEntityUnique((IDataModel)model);
            Map importTypeRelationFieldMap = TemplateFormCommonUtil.getAllEntityRelationField((IDataModel)model);
            for (Map.Entry entityEntry : importTypeUniqueMap.entrySet()) {
                String entityNumber = (String)entityEntry.getKey();
                Map uniqueDataMap = (Map)entityEntry.getValue();
                Map relationFieldDataMap = (Map)importTypeRelationFieldMap.get(entityNumber);
                String uniqueVal = (String)uniqueDataMap.get("uniqueVal");
                if (!ObjectUtils.isEmpty((Object)relationFieldDataMap)) {
                    uniqueVal = uniqueVal + relationFieldDataMap.get("uniqueVal");
                }
                uniqueDataMap.put("uniqueVal", uniqueVal);
            }
            JSONObject fieldOrder = ((JSONObject)returnData).getJSONObject("fieldorder");
            TemplateFormCommonUtil.removeAllTabPageExEntity((IFormView)this.getView());
            TemplateEntityFieldUtil.rebuildEntryEntityAndInit((IFormView)view, (IDataModel)model, (boolean)isImport, (boolean)isDownloadCond, (boolean)isOpen, (Boolean)Boolean.FALSE, (Boolean)canEdit, (Boolean)baseCanEdit, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)view), (OperationStatus)OperationStatus.EDIT, (JSONObject)fieldOrder, (Map)importTypeUniqueMap);
        }
    }

    private void rebuidTplFieldPage(JSONObject fieldOrder) {
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        String templateType = (String)model.getValue(FIELD_TEMPLATETYPE);
        String downloadCond = (String)model.getValue("enabledowncond");
        boolean isImport = "IMPT".equals(templateType);
        boolean isDownloadCond = "1".equals(downloadCond);
        boolean isOpen = TemplateEntityFieldUtil.mustInputIsOpen((IDataModel)model);
        boolean canEdit = TemplateEntityFieldUtil.mustInputCanEdit((IDataModel)model);
        boolean baseCanEdit = TemplateEntityFieldUtil.baseCanEdit((IDataModel)model);
        DynamicObject bizobject = (DynamicObject)model.getValue(FIELD_BIZOBJ);
        if (Objects.nonNull(bizobject)) {
            Map importTypeUniqueMap = TemplateFormCommonUtil.getAllEntityUnique((IDataModel)model);
            Map importTypeRelationFieldMap = TemplateFormCommonUtil.getAllEntityRelationField((IDataModel)model);
            for (Map.Entry entityEntry : importTypeUniqueMap.entrySet()) {
                String entityNumber = (String)entityEntry.getKey();
                Map uniqueDataMap = (Map)entityEntry.getValue();
                Map relationFieldDataMap = (Map)importTypeRelationFieldMap.get(entityNumber);
                String uniqueVal = (String)uniqueDataMap.get("uniqueVal");
                if (!ObjectUtils.isEmpty((Object)relationFieldDataMap)) {
                    uniqueVal = uniqueVal + relationFieldDataMap.get("uniqueVal");
                }
                uniqueDataMap.put("uniqueVal", uniqueVal);
            }
            TemplateEntityFieldUtil.rebuildEntryEntityAndInitPage((IFormView)view, (IDataModel)model, (boolean)isImport, (boolean)isDownloadCond, (boolean)isOpen, (Boolean)Boolean.FALSE, (Boolean)canEdit, (Boolean)baseCanEdit, (LinkedHashMap)TemplateFormCommonUtil.getAllEntity((IFormView)view), (OperationStatus)OperationStatus.EDIT, (JSONObject)fieldOrder, (Map)importTypeUniqueMap);
        }
    }

    public void beforeUpload(BeforeUploadEvent evt) {
        List attachInfos = evt.getAttachInfos();
    }

    public void upload(UploadEvent evt) {
        if (evt.isCancel()) {
            return;
        }
        List<String> tplConfErrMsg = this.checkPreViewTpl();
        if (!CollectionUtils.isEmpty(tplConfErrMsg)) {
            evt.setCancel(true);
            String errMsg = String.join((CharSequence)" ", tplConfErrMsg);
            evt.setCancelMsg(errMsg);
            return;
        }
        Object[] urls = evt.getUrls();
        String callbackKey = evt.getCallbackKey();
        if (urls.length > 0) {
            String url;
            IFormView view = this.getView();
            if (urls[0] instanceof Map) {
                Map fileMap = (Map)urls[0];
                url = (String)fileMap.get("url");
            } else {
                url = (String)urls[0];
            }
            if ("attpanelapcustomtpl".equals(callbackKey)) {
                LinkedHashMap allEntity = TemplateFormCommonUtil.getAllEntity((IFormView)view);
                List entityNumbers = allEntity.entrySet().stream().map(item -> {
                    String[] split = ((String)item.getKey()).split(":");
                    return split[1];
                }).collect(Collectors.toList());
                try {
                    DynamicObject tplDy = this.getModel().getDataEntity(true);
                    boolean isMESS = "MESS".equalsIgnoreCase(tplDy.getString("entitytype"));
                    LocalTplReadSheetHandler sheetHandler = new LocalTplReadSheetHandler(tplDy.getString("number"), entityNumbers, isMESS);
                    String localPath = ImportFileUtil.downloadTempFile((String)url);
                    File file = new File(localPath);
                    new ExcelReader(null).read(file, (SheetHandler)sheetHandler);
                    DynamicObjectCollection fieldEntryCollection = tplDy.getDynamicObjectCollection("tpltreeentryentity");
                    String errMsg = LocalTplCheckUtil.check((LocalTplReadSheetHandler)sheetHandler, entityNumbers, (DynamicObjectCollection)fieldEntryCollection, (boolean)isMESS);
                    if (StringUtils.isNotBlank((CharSequence)errMsg)) {
                        evt.setCancel(true);
                        evt.setCancelMsg(errMsg);
                        return;
                    }
                    view.setEnable(Boolean.FALSE, new String[]{"fs_baseinfo"});
                    view.setEnable(Boolean.FALSE, new String[]{"advconap6"});
                    view.setEnable(Boolean.FALSE, new String[]{FIELD_CONF_PANEL});
                    view.setVisible(Boolean.TRUE, new String[]{"importcfg"});
                    this.getPageCache().put("treeLockFlag", "Y");
                    Map sheetEntityMap = sheetHandler.getSheetEntityMap();
                    int i = 0;
                    int[] rowIndex = this.getModel().batchCreateNewEntryRow("entryentity", sheetEntityMap.size());
                    for (Map.Entry entry : sheetEntityMap.entrySet()) {
                        this.initEntry(rowIndex[i++], (String)entry.getKey());
                    }
                    DynamicObject dataEntity = this.getModel().getDataEntity();
                    DataEntityState state = dataEntity.getDataEntityState();
                    boolean bizChanged = false;
                    if (state.isBizChanged()) {
                        bizChanged = true;
                    }
                    if (!bizChanged) {
                        dataEntity = this.getModel().getDataEntity(true);
                        bizChanged = dataEntity.getDataEntityState().getRemovedItems() != false ? true : this.isBizChanged(dataEntity);
                    }
                    this.rebuildFieldTree();
                    if (!bizChanged) {
                        this.getModel().setDataChanged(false);
                        this.getModel().getDataEntity(true).getDataEntityState().setRemovedItems(Boolean.valueOf(false));
                    }
                    this.getPageCache().put("localtplsuc", "1");
                    this.createOpLogAndDelFileByUpload(true, url);
                }
                catch (Throwable e) {
                    this.createOpLogAndDelFileByUpload(false, url);
                    log.error(e);
                    evt.setCancel(true);
                    if (e instanceof OLE2NotOfficeXmlFileException) {
                        evt.setCancelMsg(ResManager.loadKDString((String)"\u65e0\u6cd5\u4e0a\u4f20\u53ef\u80fd\u88ab\u52a0\u5bc6\u7684\u6587\u4ef6\uff0c\u8bf7\u68c0\u67e5\u3002", (String)"TemplateUtils_2", (String)"hrmp-hies-import", (Object[])new Object[0]));
                    } else {
                        evt.setCancelMsg(e.getMessage());
                    }
                    this.removeAttachmentPanel();
                }
            }
        }
    }

    private boolean isBizChanged(DynamicObject dataEntity) {
        IDataEntityType dt = dataEntity.getDataEntityType();
        boolean bizChanged = dataEntity.getDataEntityState().isBizChanged();
        for (ICollectionProperty property : dt.getProperties().getCollectionProperties(false)) {
            Object propValue;
            if ("entryentity".equals(property.getName()) || property instanceof MulBasedataProp || property instanceof LinkEntryProp || property instanceof DynamicLocaleProperty || (propValue = dataEntity.getDataStorage().getLocalValue((IDataEntityProperty)property)) == null) continue;
            DynamicObjectCollection entrys = (DynamicObjectCollection)propValue;
            for (int index = 0; index < entrys.size(); ++index) {
                DynamicObject item = (DynamicObject)entrys.get(index);
                if (!this.isBizChanged(item)) continue;
                bizChanged = true;
                return bizChanged;
            }
        }
        return bizChanged;
    }

    private void createOpLogAndDelFileByUpload(boolean status, String url) {
        if (status) {
            ImportOperationLog.getInstance().createAppLog(this.getView(), (String)this.getView().getFormShowParameter().getCustomParam("BillFormId"), ImportOperationLog.OperationEnum.UPLOAD_FILE_SUCCESS.getOpName(), String.format(Locale.ROOT, ImportOperationLog.OperationEnum.UPLOAD_FILE_SUCCESS.getOpDescFormat(), url.substring(url.lastIndexOf("/") + 1)));
            return;
        }
        FileHandlerUtil.tryDelFile((String)url);
        ImportOperationLog.getInstance().createAppLog(this.getView(), (String)this.getView().getFormShowParameter().getCustomParam("BillFormId"), ImportOperationLog.OperationEnum.UPLOAD_FILE_FAIL.getOpName(), String.format(Locale.ROOT, ImportOperationLog.OperationEnum.UPLOAD_FILE_FAIL.getOpDescFormat(), url.substring(url.lastIndexOf("/") + 1)));
    }

    public void beforeAttachmentUpload(BeforeAttachmentUploadEvent evt) {
    }

    public void afterRemove(UploadEvent evt) {
        String callbackKey = evt.getCallbackKey();
        IFormView view = this.getView();
        if ("attpanelapcustomtpl".equals(callbackKey)) {
            Iterable bizChangedProperties;
            IDataEntityProperty bizChangedProperty;
            view.setEnable(Boolean.TRUE, new String[]{"fs_baseinfo"});
            view.setEnable(Boolean.TRUE, new String[]{"advconap6"});
            view.setEnable(Boolean.TRUE, new String[]{FIELD_CONF_PANEL});
            this.deleteImportCfg();
            view.setVisible(Boolean.FALSE, new String[]{"importcfg"});
            HRBaseServiceHelper helper = new HRBaseServiceHelper("hies_diaetplconf");
            boolean isUpd = this.getModel().getDataEntity().getDataEntityState().getFromDatabase();
            if (isUpd) {
                DynamicObject dyn = helper.loadSingle(this.getModel().getValue("id"));
                dyn.set("enable", (Object)"10");
                helper.generateEmptyEntryCollection(dyn, "entryentity");
                helper.updateOne(dyn);
                this.getModel().setValue("enable", (Object)"10");
            }
            this.getPageCache().put("treeLockFlag", "N");
            DynamicObject dataEntity = this.getModel().getDataEntity();
            DataEntityState state = dataEntity.getDataEntityState();
            boolean bizChanged = false;
            if (state.isBizChanged() && !"enable".equals((bizChangedProperty = (IDataEntityProperty)(bizChangedProperties = state.getBizChangedProperties()).iterator().next()).getName())) {
                bizChanged = true;
            }
            if (!bizChanged && (state = (dataEntity = this.getModel().getDataEntity(true)).getDataEntityState()).isBizChanged() && !"enable".equals((bizChangedProperty = (IDataEntityProperty)(bizChangedProperties = dataEntity.getDataEntityState().getBizChangedProperties()).iterator().next()).getName())) {
                bizChanged = true;
            }
            this.rebuildFieldTree();
            if (!bizChanged) {
                this.getModel().setDataChanged(false);
                this.getModel().getDataEntity(true).getDataEntityState().setRemovedItems(Boolean.valueOf(false));
            }
            this.getPageCache().put("localtplsuc", "0");
        }
    }

    private void deleteImportCfg() {
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity");
        int size = entryEntity.size();
        if (size > 0) {
            int[] delRows = new int[size];
            for (int index = 0; index < delRows.length; ++index) {
                delRows[index] = index;
            }
            this.getModel().deleteEntryRows("entryentity", delRows);
        }
    }

    private void initEntry(int rowIndex, String sheetName) {
        IDataModel model = this.getModel();
        model.setValue("sheetname", (Object)sheetName, rowIndex);
    }

    private void removeAttachmentPanel() {
        AttachmentPanel attachmentPanel = (AttachmentPanel)this.getView().getControl("attpanelapcustomtpl");
        List attachmentData = attachmentPanel.getAttachmentData();
        if (kd.bos.util.CollectionUtils.isNotEmpty((Collection)attachmentData)) {
            attachmentPanel.remove((Map)attachmentData.get(0));
        }
    }

    public void beforeClosed(BeforeClosedEvent event) {
        if (this.closeConfirmStatus) {
            this.closeConfirmStatus = false;
            return;
        }
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (OperationStatus.VIEW == status) {
            return;
        }
        String tplGenMode = (String)this.getView().getFormShowParameter().getCustomParam("tplgenmode");
        if (StringUtils.isBlank((CharSequence)tplGenMode)) {
            tplGenMode = (String)this.getModel().getValue("tplgenmode");
        }
        if (!TplGenModeConstant.isLocalUpload((String)tplGenMode)) {
            return;
        }
        String localTplSuc = this.getPageCache().get("localtplsuc");
        if (this.isExistLocalUploadFile() || "1".equals(localTplSuc)) {
            return;
        }
        String enable = this.getPageCache().get("preEnable");
        if ("10".equals(enable)) {
            return;
        }
        ConfirmCallBackListener confirmCallBacks = new ConfirmCallBackListener("page_close", (IFormPlugin)this);
        HashMap<Integer, String> btnNameMaps = new HashMap<Integer, String>();
        btnNameMaps.put(MessageBoxResult.Cancel.getValue(), ResManager.loadKDString((String)"\u8fd4\u56de\u7f16\u8f91", (String)"TemplateConfPlugin_36", (String)"hrmp-hies-import", (Object[])new Object[0]));
        btnNameMaps.put(MessageBoxResult.Yes.getValue(), ResManager.loadKDString((String)"\u76f4\u63a5\u9000\u51fa", (String)"TemplateConfPlugin_37", (String)"hrmp-hies-import", (Object[])new Object[0]));
        MessageBoxOptions options = MessageBoxOptions.OKCancel;
        String msg = MessageFormat.format(ResManager.loadKDString((String)"\u7cfb\u7edf\u68c0\u6d4b\u5230\u201c\u672c\u5730\u6a21\u677f\u6587\u4ef6\u201d\u88ab\u5220\u9664\uff0c\u9000\u51fa\u540e\u6a21\u677f\u72b6\u6001\u5c06\u66f4\u65b0\u4e3a\u5f85\u542f\u7528\u3002", (String)"TemplateConfPlugin_38", (String)"hrmp-hies-import", (Object[])new Object[0]), System.lineSeparator());
        this.getView().showConfirm(msg, "", options, ConfirmTypes.Save, confirmCallBacks, btnNameMaps);
        event.setCancel(true);
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        if (HRStringUtils.equals((String)"page_close", (String)event.getCallBackId()) && MessageBoxResult.Yes.equals((Object)event.getResult())) {
            this.closeConfirmStatus = true;
            PermFormCommonUtil.closeClientForm((IFormView)this.getView());
            IFormView parentView = this.getView().getParentView();
            parentView.invokeOperation("refresh");
            this.getView().sendFormAction(parentView);
        }
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

    private void showMainEntityAliastEXT(String importType) {
        HashMap<String, String> arg = new HashMap<String, String>(3);
        String loadKDString = "SE".equals(importType) ? ResManager.loadKDString((String)"\u5b9e\u4f53\u522b\u540d", (String)"TemplateConfPlugin_72", (String)"hrmp-hies-import", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u4e3b\u5b9e\u4f53\u522b\u540d", (String)"TemplateConfPlugin_73", (String)"hrmp-hies-import", (Object[])new Object[0]);
        arg.put(RequestContext.get().getLang().toString(), loadKDString);
        HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>(3);
        map.put("caption", arg);
        this.getView().updateControlMetadata("mainentityalias", map);
    }
}
