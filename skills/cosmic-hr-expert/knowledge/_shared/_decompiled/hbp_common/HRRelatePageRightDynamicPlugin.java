/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Container
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.Label
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.LoadCustomControlMetasArgs
 *  kd.bos.form.events.OnGetControlArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.form.Margin
 *  kd.bos.metadata.form.Style
 *  kd.bos.metadata.form.container.FlexPanelAp
 *  kd.bos.metadata.form.control.ImageAp
 *  kd.bos.metadata.form.control.LabelAp
 *  kd.bos.mvc.cache.PageCache
 *  kd.bos.mvc.form.FormView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.hbp.business.service.rp.HRRelatePanelSetFactory
 *  kd.hr.hbp.business.service.rp.RelatePageInfo
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.control.HRImageAp$Builder
 *  kd.hr.hbp.common.util.HRBaseUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin
 */
package kd.hr.hbp.formplugin.web.rp;

import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillShowParameter;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.container.Container;
import kd.bos.form.control.Control;
import kd.bos.form.control.Label;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.LoadCustomControlMetasArgs;
import kd.bos.form.events.OnGetControlArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.form.Margin;
import kd.bos.metadata.form.Style;
import kd.bos.metadata.form.container.FlexPanelAp;
import kd.bos.metadata.form.control.ImageAp;
import kd.bos.metadata.form.control.LabelAp;
import kd.bos.mvc.cache.PageCache;
import kd.bos.mvc.form.FormView;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.business.service.rp.HRRelatePanelSetFactory;
import kd.hr.hbp.business.service.rp.RelatePageInfo;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.control.HRImageAp;
import kd.hr.hbp.common.util.HRBaseUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin;

public class HRRelatePageRightDynamicPlugin
extends HRDynamicFormBasePlugin {
    private static final String FLEX_START = "flex-start";
    private static final Log logger = LogFactory.getLog(HRRelatePageRightDynamicPlugin.class);
    private static final String CACHE_RELATEPAGERIGHT = "cache_RelatePageRight";

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
    }

    public void loadCustomControlMetas(LoadCustomControlMetasArgs loadCustomControlMetasArgs) {
        super.loadCustomControlMetas(loadCustomControlMetasArgs);
        FormShowParameter showParameter = (FormShowParameter)loadCustomControlMetasArgs.getSource();
        String relateEntityTypeId = (String)showParameter.getCustomParam("hbss_entitytype_id");
        Map pageIconMap = (Map)showParameter.getCustomParam("pageIcons");
        if (!HRStringUtils.isEmpty((String)relateEntityTypeId)) {
            String pageId = showParameter.getPageId();
            FlexPanelAp headPanelAp = this.createRelatePageInfoPanelAp(relateEntityTypeId, pageIconMap, pageId);
            HashMap<String, String> mapHeadMap = new HashMap<String, String>();
            mapHeadMap.put("id", "flexpanelrelateinfo");
            mapHeadMap.put("items", (String)headPanelAp.createControl().get("items"));
            loadCustomControlMetasArgs.getItems().add(mapHeadMap);
        }
    }

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        FormView formView = (FormView)eventObject.getSource();
        String relateTypeId = (String)formView.getFormShowParameter().getCustomParam("hbss_entitytype_id");
        if (!HRStringUtils.isEmpty((String)relateTypeId)) {
            FlexPanelAp headPanelAp = this.createRelatePageInfoPanelAp(relateTypeId, null, this.getView().getPageId());
            Container relatePanel = (Container)this.getView().getControl("flexpanelrelateinfo");
            relatePanel.getItems().addAll(((Container)headPanelAp.buildRuntimeControl()).getItems());
            this.getView().createControlIndex(relatePanel.getItems());
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        switch (afterDoOperationEventArgs.getOperateKey()) {
            case "save": 
            case "saveandnew": {
                if (this.getView().getFormShowParameter() instanceof BillShowParameter) break;
                BaseShowParameter baseShowParameter = (BaseShowParameter)this.getView().getFormShowParameter();
                DynamicObject currentDynObj = this.getModel().getDataEntity();
                baseShowParameter.setPkId((Object)currentDynObj.getLong("id"));
                break;
            }
        }
    }

    public void onGetControl(OnGetControlArgs onGetControlArgs) {
        FormView baseView = (FormView)onGetControlArgs.getSource();
        String relateEntityTypeId = (String)baseView.getFormShowParameter().getCustomParam("hbss_entitytype_id");
        if (!HRStringUtils.isEmpty((String)relateEntityTypeId)) {
            List<RelatePageInfo> pageInfos = this.getPageInfoList(relateEntityTypeId, this.getView().getPageId());
            for (RelatePageInfo pageInfo : pageInfos) {
                RelatePageInfo relatePageInfo = pageInfo;
                String pageKeyStr = relatePageInfo.getNumber().toLowerCase(Locale.ROOT);
                if (!HRStringUtils.equals((String)pageKeyStr, (String)onGetControlArgs.getKey())) continue;
                Label relatePageLabel = new Label();
                relatePageLabel.setKey(pageKeyStr);
                relatePageLabel.setView(this.getView());
                relatePageLabel.addClickListener((ClickListener)this);
                onGetControlArgs.setControl((Control)relatePageLabel);
            }
        }
    }

    private List<RelatePageInfo> getPageInfoList(String relateEntityTypeId, String pageId) {
        List pageInfoList;
        PageCache pageCache;
        String chacheStr;
        if (logger.isInfoEnabled()) {
            logger.info("relateEntityTypeId:" + relateEntityTypeId + "pageId:" + pageId);
        }
        if (Objects.isNull(chacheStr = (pageCache = new PageCache(pageId)).get(CACHE_RELATEPAGERIGHT))) {
            pageInfoList = HRRelatePanelSetFactory.getRelatePageInfoList((String)relateEntityTypeId);
            pageCache.put(CACHE_RELATEPAGERIGHT, SerializationUtils.toJsonString((Object)pageInfoList));
        } else {
            pageInfoList = SerializationUtils.fromJsonStringToList((String)chacheStr, RelatePageInfo.class);
        }
        if (logger.isInfoEnabled()) {
            logger.info("pageInfoList:" + pageInfoList.size() + "," + pageInfoList);
        }
        return pageInfoList;
    }

    public void click(EventObject evt) {
        super.click(evt);
        if (evt.getSource() instanceof Label) {
            Label curClickLabel = (Label)evt.getSource();
            String labelName = curClickLabel.getKey();
            this.showRelatedPage(labelName.toUpperCase(Locale.ROOT));
        }
    }

    private void showRelatedPage(String currentRelatePageNumber) {
        IFormView view = this.getView();
        FormShowParameter formShowParameter = view.getFormShowParameter();
        String reateEntityTypeId = (String)formShowParameter.getCustomParam("hbss_entitytype_id");
        RelatePageInfo relatePageInfo = HRRelatePanelSetFactory.getRelatePageInfo((String)reateEntityTypeId, (String)currentRelatePageNumber);
        if (relatePageInfo == null) {
            this.getView().showErrorNotification(HRBaseUtils.getNoPermMsg());
            return;
        }
        boolean hasPerm = this.hasPerm(relatePageInfo.getPageNumber());
        if (!hasPerm) {
            this.getView().showErrorNotification(HRBaseUtils.getNoPermMsg());
            return;
        }
        String currentObjectPKId = this.getCurrentObjectOKId(formShowParameter);
        String caption = (String)formShowParameter.getCustomParam("caption");
        Map pageIconMap = (Map)formShowParameter.getCustomParam("pageIcons");
        Map customVariablesMap = (Map)formShowParameter.getCustomParam("customvariables");
        Map customPKFilterMap = (Map)formShowParameter.getCustomParam("customPKFilter");
        FormShowParameter pageFormShowParameter = new FormShowParameter();
        pageFormShowParameter.setCustomParam("currentRelatePage", (Object)relatePageInfo.getNumber());
        pageFormShowParameter.setCustomParam("currentObjectPKId", (Object)currentObjectPKId);
        pageFormShowParameter.setCustomParam("hbss_entitytype_id", (Object)reateEntityTypeId);
        pageFormShowParameter.setCustomParam("pageIcons", (Object)pageIconMap);
        String pageId = this.getView().getPageId() + "_" + currentRelatePageNumber;
        pageFormShowParameter.setPageId(pageId);
        pageFormShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        pageFormShowParameter.setCustomParam("customvariables", (Object)customVariablesMap);
        pageFormShowParameter.setCustomParam("customPKFilter", (Object)customPKFilterMap);
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbss_entitytype");
        QFilter idFilter = new QFilter("id", "=", (Object)Long.valueOf(reateEntityTypeId));
        QFilter[] idFilterArray = new QFilter[]{idFilter};
        DynamicObject entityTypeDynamicObj = serviceHelper.queryOne("id,name,relatepageinfo", idFilterArray);
        String relatePageName = entityTypeDynamicObj.getString("relatepageinfo");
        pageFormShowParameter.setFormId(relatePageName);
        if (!HRStringUtils.isEmpty((String)caption)) {
            pageFormShowParameter.setCaption(caption);
        }
        view.showForm(pageFormShowParameter);
    }

    private String getCurrentObjectOKId(FormShowParameter formShowParameter) {
        String currentObjectPKId;
        if (formShowParameter instanceof BaseShowParameter) {
            BaseShowParameter baseShowParameter = (BaseShowParameter)formShowParameter;
            Object pkId = baseShowParameter.getPkId();
            currentObjectPKId = pkId != null ? pkId.toString() : (null != baseShowParameter.getCustomParam("currentObjectPKId") ? baseShowParameter.getCustomParam("currentObjectPKId").toString() : this.getPageCache().get("currentObjectPKId"));
        } else {
            currentObjectPKId = (String)formShowParameter.getCustomParam("currentObjectPKId");
            if (HRStringUtils.isEmpty((String)currentObjectPKId)) {
                currentObjectPKId = this.getPageCache().get("currentObjectPKId");
            }
        }
        return currentObjectPKId;
    }

    private boolean hasPerm(String entityName) {
        long userId = Long.parseLong(RequestContext.get().getUserId());
        return PermissionServiceHelper.checkPermission((Long)userId, (String)this.getView().getFormShowParameter().getAppId(), (String)entityName, (String)"47150e89000000ac");
    }

    private FlexPanelAp createRelatePageInfoPanelAp(String relateEntityTypeId, Map<String, String> pageIconMap, String pageId) {
        FlexPanelAp relatePanelAp = this.assembleRelatePanelAp();
        List<RelatePageInfo> pageInfoList = this.getPageInfoList(relateEntityTypeId, pageId);
        this.addAllPagePanels(relatePanelAp, pageInfoList, pageIconMap);
        return relatePanelAp;
    }

    private void addAllPagePanels(FlexPanelAp relatePanelAp, List<RelatePageInfo> pageInfoList, Map<String, String> pageIconMap) {
        Style topStyle = this.assembleRelateLableTopStyle();
        int size = pageInfoList.size();
        for (int i = 0; i < size; ++i) {
            FlexPanelAp pagePanelAp = this.assemblePagePanelAp(i);
            RelatePageInfo pageInfo = pageInfoList.get(i);
            LabelAp lblRelatePageAp = this.assembleRelatePageLabelAp(pageInfo);
            pagePanelAp.getItems().add(lblRelatePageAp);
            lblRelatePageAp.setStyle(topStyle);
            if (pageIconMap != null && !pageIconMap.isEmpty() && pageIconMap.containsKey(pageInfo.getPageNumber())) {
                ImageAp pageIconAp = this.assembleImageAp(pageInfo.getPageNumber(), pageIconMap);
                pagePanelAp.getItems().add(pageIconAp);
            }
            relatePanelAp.getItems().add(pagePanelAp);
        }
    }

    private FlexPanelAp assemblePagePanelAp(int index) {
        FlexPanelAp pagePanelAp = new FlexPanelAp();
        pagePanelAp.setKey("pagepanelap" + index);
        pagePanelAp.setWrap(true);
        pagePanelAp.setDirection("row");
        pagePanelAp.setAlignContent(FLEX_START);
        pagePanelAp.setAlignItems(FLEX_START);
        pagePanelAp.setJustifyContent("center");
        return pagePanelAp;
    }

    private FlexPanelAp assembleRelatePanelAp() {
        FlexPanelAp relatePanelAp = new FlexPanelAp();
        relatePanelAp.setKey("relatePageflexpanellistap");
        relatePanelAp.setWrap(true);
        relatePanelAp.setDirection("column");
        relatePanelAp.setAlignContent(FLEX_START);
        relatePanelAp.setAlignItems(FLEX_START);
        relatePanelAp.setJustifyContent("center");
        return relatePanelAp;
    }

    private Style assembleRelateLableTopStyle() {
        Margin margin = new Margin();
        margin.setBottom("22px");
        Style style = new Style();
        style.setMargin(margin);
        return style;
    }

    private LabelAp assembleRelatePageLabelAp(RelatePageInfo pageInfo) {
        LabelAp lblRelatePageAp = new LabelAp();
        lblRelatePageAp.setId(pageInfo.getNumber());
        lblRelatePageAp.setKey(pageInfo.getNumber());
        lblRelatePageAp.setName(new LocaleString(pageInfo.getPageName()));
        lblRelatePageAp.setFontSize(14);
        lblRelatePageAp.setClickable(true);
        return lblRelatePageAp;
    }

    private ImageAp assembleImageAp(String number, Map<String, String> pageIconMap) {
        String imageKey = pageIconMap.get(number);
        return new HRImageAp.Builder("pageIcon" + number).setHeight("5px").setWidth("5px").setGrow(0).setShrink(1).setRadius("30px").setImageKey(imageKey).setClickable(true).build();
    }
}
