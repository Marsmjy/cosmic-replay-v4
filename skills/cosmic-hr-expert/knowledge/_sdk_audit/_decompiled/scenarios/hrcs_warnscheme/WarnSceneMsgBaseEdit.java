/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.inte.api.EnabledLang
 *  kd.bos.lang.Lang
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.cache.PageCache
 *  kd.bos.servicehelper.inte.InteServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.WarnSchemeService
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.MsgChannelService
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.MsgConfigService
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.PlanModifyLogService
 *  kd.hr.hrcs.common.model.earlywarn.MsgContent
 *  kd.hr.hrcs.common.model.earlywarn.vo.MsgChannelVO
 *  kd.hr.hrcs.formplugin.web.earlywarn.scene.process.WarnMsgProcessor
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scene;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.inte.api.EnabledLang;
import kd.bos.lang.Lang;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.cache.PageCache;
import kd.bos.servicehelper.inte.InteServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.WarnSchemeService;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.MsgChannelService;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.MsgConfigService;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.PlanModifyLogService;
import kd.hr.hrcs.common.model.earlywarn.MsgContent;
import kd.hr.hrcs.common.model.earlywarn.vo.MsgChannelVO;
import kd.hr.hrcs.formplugin.web.earlywarn.scene.process.WarnMsgProcessor;
import org.apache.commons.lang3.StringUtils;

public final class WarnSceneMsgBaseEdit
extends HRDataBaseEdit {
    protected static final String MSG_LANG = "localeid";
    private static final Log LOGGER = LogFactory.getLog(WarnSceneMsgBaseEdit.class);
    private static final String MSG_CONTENT_CACHE_KEY = "msgContent";
    private static final String MSGCONF_PAGE_CACHE_KEY = "msgconfflex_pageId";

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        try {
            this.initMsgChannel();
            this.initEnabledLang();
            this.showWarnMsgForm();
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneMsgBaseEdit_beforeBindData_error:", (Throwable)exception);
        }
    }

    private void initMsgChannel() {
        ArrayList comboEditList = Lists.newArrayListWithExpectedSize((int)16);
        ComboItem comboItem = new ComboItem();
        comboItem.setValue("msgcenter");
        comboItem.setCaption(new LocaleString(ResManager.loadKDString((String)"\u6d88\u606f\u4e2d\u5fc3", (String)"WarnSceneMsgEdit_15", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0])));
        comboEditList.add(comboItem);
        List msgChannelList = MsgChannelService.getMsgChannelList((boolean)true);
        for (MsgChannelVO channelVO : msgChannelList) {
            ComboItem comboItemTemp = new ComboItem();
            comboItemTemp.setValue(channelVO.getChannelNumber());
            comboItemTemp.setCaption(new LocaleString(channelVO.getChannelName()));
            comboEditList.add(comboItemTemp);
        }
        ComboEdit comboEdit = (ComboEdit)this.getView().getControl("pushchannel");
        comboEdit.setComboItems((List)comboEditList);
        if (StringUtils.isBlank((CharSequence)((String)this.getModel().getValue("pushchannel")))) {
            this.getModel().setValue("pushchannel", (Object)"msgcenter");
        }
        this.getPageCache().put("ComboItemMap", SerializationUtils.toJsonString(comboEditList.stream().collect(Collectors.toMap(ComboItem::getValue, combo2 -> combo2.getCaption().getLocaleValue(), (x1, x2) -> x2))));
    }

    private void initEnabledLang() {
        ArrayList comboEditList = Lists.newArrayListWithExpectedSize((int)16);
        List enabledLang = InteServiceHelper.getMultiLangEnabledLang();
        for (EnabledLang lang : enabledLang) {
            ComboItem comboItem = new ComboItem();
            comboItem.setValue(lang.getNumber());
            comboItem.setCaption(new LocaleString(lang.getName()));
            comboEditList.add(comboItem);
        }
        ComboEdit comboEdit = (ComboEdit)this.getView().getControl(MSG_LANG);
        comboEdit.setComboItems((List)comboEditList);
        if (StringUtils.isBlank((CharSequence)((String)this.getModel().getValue(MSG_LANG)))) {
            this.getModel().setValue(MSG_LANG, (Object)Lang.get().getLocale().toString());
        }
    }

    public void propertyChanged(PropertyChangedArgs changedArgs) {
        String changeProperty = changedArgs.getProperty().getName();
        try {
            if ("warnbizobj".equals(changeProperty) || "warnscene".equals(changeProperty)) {
                MsgContent msgContent = new MsgContent("", "", "");
                msgContent.setLocaleId((String)this.getModel().getValue(MSG_LANG));
                this.getPageCache().put(MSG_CONTENT_CACHE_KEY, SerializationUtils.toJsonString((Object)msgContent));
                this.showWarnMsgForm();
            } else if (MSG_LANG.equals(changeProperty)) {
                this.showWarnMsgForm();
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneMsgBaseEdit_propertyChanged_error:", (Throwable)exception);
        }
    }

    private void showWarnMsgForm() {
        this.cacheCurMsgContent();
        HashMap param = Maps.newHashMapWithExpectedSize((int)1);
        param.put("formId", "hrcs_warnmsg");
        param.put("schemeId", ((BaseShowParameter)this.getView().getFormShowParameter()).getPkId());
        param.put("entity", this.getView().getEntityId());
        String firstSelChannel = this.getFirstSelChannel();
        param.put("pushchannel", firstSelChannel);
        String msgContentSrc = this.getPageCache().get(MSG_CONTENT_CACHE_KEY);
        param.put("msgContentSrc", msgContentSrc);
        FormShowParameter showParameter = FormShowParameter.createFormShowParameter((Map)param);
        showParameter.getOpenStyle().setShowType(ShowType.InContainer);
        showParameter.getOpenStyle().setTargetKey("msgconfflex");
        this.getPageCache().put(MSGCONF_PAGE_CACHE_KEY, showParameter.getPageId());
        this.getView().showForm(showParameter);
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        super.beforeItemClick(evt);
        try {
            if (HRStringUtils.equals((String)evt.getItemKey(), (String)"bar_save")) {
                boolean checked = false;
                if (!this.checkMsgData()) {
                    checked = true;
                    evt.setCancel(true);
                } else {
                    this.cacheCurMsgContent();
                }
                if (!checked && !this.checkCacheMsgData()) {
                    evt.setCancel(true);
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("beforeItemClick_error:", (Throwable)exception);
        }
    }

    private boolean checkMsgData() {
        boolean flag = true;
        IFormView childView = this.getChildView();
        if (childView == null && this.getModel().getValue("id").equals(0L) || childView != null && StringUtils.isBlank((CharSequence)((String)childView.getModel().getValue("msgtitlehide")))) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6d88\u606f\u6807\u9898\u3002", (String)"WarnSceneMsgEdit_4", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
            if (childView != null) {
                OperationStatus status = this.getView().getFormShowParameter().getStatus();
                new WarnMsgProcessor(childView).updateMsgContentTextData("msgtitle", "tip", "", status.name(), this.getView());
            }
            flag = false;
        }
        if (childView != null && StringUtils.isNotBlank((CharSequence)childView.getPageCache().get("mainTableData"))) {
            List mainTableList = JSONArray.parseArray((String)childView.getPageCache().get("mainTableData"), Map.class);
            StringBuilder errorCol = new StringBuilder();
            for (int index = 0; index < mainTableList.size(); ++index) {
                if (!HRStringUtils.isEmpty((String)((String)((Map)mainTableList.get(index)).get("titleline"))) && !HRStringUtils.isEmpty((String)((String)((Map)mainTableList.get(index)).get("dataline")))) continue;
                errorCol.append(index + 1).append(",");
            }
            if (!errorCol.toString().equals("")) {
                this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6b63\u6587\u8868\u683c\u7b2c%s\u5217\u3002", (String)"WarnSceneMsgEdit_23", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), errorCol.substring(0, errorCol.lastIndexOf(","))));
                flag = false;
            }
        }
        return flag;
    }

    private void cacheCurMsgContent() {
        IFormView childView = this.getChildView();
        if (childView == null) {
            return;
        }
        MsgContent msgContent = this.initMsgContentFromChildPage(childView);
        this.getPageCache().put(MSG_CONTENT_CACHE_KEY, SerializationUtils.toJsonString((Object)msgContent));
        LOGGER.info("WarnSceneMsgEdit.cacheCurMsgContent:{}", (Object)this.getPageCache().get(MSG_CONTENT_CACHE_KEY));
    }

    private boolean checkCacheMsgData() {
        IPageCache service = (IPageCache)this.getView().getService(IPageCache.class);
        String msgContentSrc = service.get(MSG_CONTENT_CACHE_KEY);
        MsgContent msgContent = (MsgContent)SerializationUtils.fromJsonString((String)msgContentSrc, MsgContent.class);
        String msgTitle = msgContent.getMsgTitle();
        if (StringUtils.isBlank((CharSequence)msgTitle)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6d88\u606f\u6807\u9898\u3002", (String)"WarnSceneMsgEdit_4", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
            return false;
        }
        Set<String> titleSpareLangName = this.getMsgContextSpareLangName(msgTitle, msgContent.getLocaleId());
        if (!titleSpareLangName.isEmpty()) {
            this.getView().showTipNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6d88\u606f\u6807\u9898\u914d\u7f6e\u4e86\u672a\u9009\u62e9\u7684\u901a\u77e5\u8bed\u8a00\u5b57\u6bb5\uff0c%s\u3002", (String)"WarnSceneMsgEdit_10", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), String.join((CharSequence)",", titleSpareLangName)));
            return false;
        }
        Set<String> mainSpareLangName = this.getMsgContextSpareLangName(msgContent.getMsgMain(), msgContent.getLocaleId());
        if (!mainSpareLangName.isEmpty()) {
            this.getView().showTipNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6d88\u606f\u6b63\u6587\u914d\u7f6e\u4e86\u672a\u9009\u62e9\u7684\u901a\u77e5\u8bed\u8a00\u5b57\u6bb5\uff0c%s\u3002", (String)"WarnSceneMsgEdit_11", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), String.join((CharSequence)",", mainSpareLangName)));
            return false;
        }
        List msgMainTable = msgContent.getMsgMainTable();
        StringBuilder errorCol = new StringBuilder();
        for (int i = 0; i < msgMainTable.size(); ++i) {
            Map dataMap = (Map)msgMainTable.get(i);
            if (!HRStringUtils.isEmpty((String)((String)dataMap.get("titleline"))) && !HRStringUtils.isEmpty((String)((String)dataMap.get("dataline")))) continue;
            errorCol.append(i + 1).append(",");
        }
        if (!errorCol.toString().isEmpty()) {
            this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u6b63\u6587\u8868\u683c\u7b2c%s\u5217\u3002", (String)"WarnSceneMsgEdit_23", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), errorCol.substring(0, errorCol.lastIndexOf(","))));
            return false;
        }
        StringBuilder spareCol = new StringBuilder();
        StringBuilder spareLangName = new StringBuilder();
        Map<String, String> unSelLangMap = this.getUnSelLangNameMap(msgContent.getLocaleId());
        for (int i = 0; i < msgMainTable.size(); ++i) {
            String fieldLocalId;
            Map dataMap = (Map)msgMainTable.get(i);
            String fieldAlias = (String)dataMap.get("datalineRes");
            if (!fieldAlias.contains("|") || !unSelLangMap.containsKey(fieldLocalId = fieldAlias.substring(fieldAlias.indexOf("|") + 1))) continue;
            spareCol.append(i + 1).append(",");
            spareLangName.append(unSelLangMap.get(fieldLocalId)).append(",");
        }
        if (!spareCol.toString().isEmpty()) {
            this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u6b63\u6587\u8868\u683c\u7b2c%s\u5217\u914d\u7f6e\u4e86\u672a\u9009\u62e9\u7684\u901a\u77e5\u8bed\u8a00\u5b57\u6bb5\uff0c%s\u3002", (String)"WarnSceneMsgEdit_12", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), spareCol.substring(0, spareCol.lastIndexOf(",")), spareLangName.substring(0, spareLangName.lastIndexOf(","))));
            return false;
        }
        Set<String> endSpareLangName = this.getMsgContextSpareLangName(msgContent.getMsgConclusion(), msgContent.getLocaleId());
        if (!endSpareLangName.isEmpty()) {
            this.getView().showTipNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6d88\u606f\u7ed3\u675f\u8bed\u914d\u7f6e\u4e86\u672a\u9009\u62e9\u7684\u901a\u77e5\u8bed\u8a00\u5b57\u6bb5\uff0c%s\u3002", (String)"WarnSceneMsgEdit_14", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), String.join((CharSequence)",", endSpareLangName)));
            return false;
        }
        return true;
    }

    private Map<String, String> getUnSelLangNameMap(String localId) {
        List langKeyList = WarnSchemeService.getSchemeSelLangKeys((String)localId);
        List enabledLang = InteServiceHelper.getMultiLangEnabledLang();
        Map<String, String> unSelLangMap = enabledLang.stream().filter(lang -> !langKeyList.contains(lang.getNumber())).collect(Collectors.toMap(EnabledLang::getNumber, EnabledLang::getName));
        return unSelLangMap;
    }

    private Set<String> getMsgContextSpareLangName(String context, String localId) {
        if (HRStringUtils.isEmpty((String)context) || HRStringUtils.isEmpty((String)localId)) {
            return Collections.emptySet();
        }
        Map<String, String> unSelLangMap = this.getUnSelLangNameMap(localId);
        HashSet<String> spareLangName = new HashSet<String>(16);
        unSelLangMap.forEach((langKey, langName) -> {
            String nameSuffix = "_" + langName + "]";
            if (context.contains(nameSuffix)) {
                spareLangName.add((String)langName);
            }
        });
        return spareLangName;
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        String operateKey = afterDoOperationEventArgs.getOperateKey();
        if ("save".equals(operateKey)) {
            LOGGER.info("WarnSceneMsgEdit.afterDoOperation_operationResult:{}", (Object)afterDoOperationEventArgs.getOperationResult());
            if (afterDoOperationEventArgs.getOperationResult().isSuccess()) {
                try {
                    IFormView childView = this.getChildView();
                    if (childView != null) {
                        Map nameToAlias = new WarnMsgProcessor(childView).getNameToAlias();
                        this.saveMsgContent(nameToAlias);
                    }
                }
                catch (Exception exception) {
                    afterDoOperationEventArgs.getOperationResult().setSuccess(false);
                    afterDoOperationEventArgs.getOperationResult().setMessage(exception.getMessage());
                    LOGGER.error("afterDoOperation_error:", (Throwable)exception);
                }
            }
        }
    }

    private void saveMsgContent(Map<String, String> nameToAlias) {
        IPageCache service = (IPageCache)this.getView().getService(IPageCache.class);
        String msgContentSrc = service.get(MSG_CONTENT_CACHE_KEY);
        LOGGER.info("WarnSceneMsgEdit.saveMsgContent:{}", (Object)msgContentSrc);
        if (HRStringUtils.isEmpty((String)msgContentSrc)) {
            return;
        }
        MsgContent msgContent = (MsgContent)SerializationUtils.fromJsonString((String)msgContentSrc, MsgContent.class);
        Long schemeId = (Long)this.getModel().getValue("id");
        if (schemeId != null && schemeId != 0L) {
            WarnSchemeService.deleteMsgConfAndTableByChannel((Object)schemeId, (String)this.getFirstSelChannel());
        }
        JSONArray msgConfArrayLog = new JSONArray();
        JSONArray msgTableArrayLog = new JSONArray();
        String comboItemMapStr = this.getPageCache().get("ComboItemMap");
        Long pk = (Long)this.getModel().getValue("id");
        String entityId = this.getView().getEntityId();
        MsgConfigService msgConfigService = new MsgConfigService(entityId, pk, comboItemMapStr);
        msgConfigService.saveMsgTableContent(msgContent.getMsgMainTable(), nameToAlias, msgContent.getLocaleId(), msgContent.getChannel(), msgTableArrayLog);
        msgConfigService.saveMsgCollection(nameToAlias, msgContent, msgConfArrayLog);
        service.remove(MSG_CONTENT_CACHE_KEY);
        String id = String.valueOf(pk);
        String number = String.valueOf(this.getModel().getValue("number"));
        PlanModifyLogService.saveModifyLog((String)entityId, (String)id, (String)number, (JSONArray)msgConfArrayLog, (JSONArray)msgTableArrayLog);
    }

    private IFormView getChildView() {
        return this.getView().getView(this.getPageCache().get(MSGCONF_PAGE_CACHE_KEY));
    }

    private String getFirstSelChannel() {
        String pushChannel = (String)this.getModel().getValue("pushchannel");
        if (HRStringUtils.isEmpty((String)pushChannel)) {
            return "msgcenter";
        }
        return Arrays.stream(pushChannel.split(",")).filter(HRStringUtils::isNotEmpty).findFirst().orElse("");
    }

    private MsgContent initMsgContentFromChildPage(IFormView childView) {
        IDataModel childViewModel = childView.getModel();
        String firstSelChannel = this.getFirstSelChannel();
        String localId = (String)this.getModel().getValue(MSG_LANG);
        PageCache pageCache = new PageCache(childView.getPageId());
        MsgContent msgContent = new MsgContent((String)childViewModel.getValue("msgtitlehide"), (String)childViewModel.getValue("msgmainhide"), (String)childViewModel.getValue("msgconclusionhide"));
        msgContent.setLocaleId(localId);
        msgContent.setChannel(firstSelChannel);
        String mainTableData = pageCache.get("mainTableData");
        if (mainTableData != null) {
            msgContent.setMsgMainTable(JSONArray.parseArray((String)mainTableData, Map.class));
        }
        return msgContent;
    }
}
