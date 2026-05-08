/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.ext.hr.filter.control.HRFilter
 *  kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo
 *  kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.service.labelandreport.HRFilterUtil
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrptmc.business.anobj.AnObjSideBarService
 *  kd.hr.hrptmc.business.anobj.AnalyseObjectService
 *  kd.hr.hrptmc.business.exception.HRPTMCBizException
 *  kd.hr.hrptmc.business.repdesign.info.ReportInfo
 *  kd.hr.hrptmc.business.repdesign.info.ReportManageConfigInfo
 *  kd.hr.hrptmc.business.repdesign.opt.InitCallBackInfo
 *  kd.hr.hrptmc.business.repdesign.opt.RptDrawOptCallBackInfo
 *  kd.hr.hrptmc.common.constant.anobj.AnObjGroupFieldConstants
 *  kd.hr.hrptmc.common.constant.anobj.AnalyseObjectConstants
 *  kd.hr.hrptmc.common.model.anobj.AnObjGroupField
 *  kd.hr.hrptmc.common.model.anobj.AnObjPivotBo
 *  kd.hr.hrptmc.common.model.anobj.AnObjSideBar
 *  kd.hr.hrptmc.common.model.anobj.QueryFieldBo
 *  kd.hr.hrptmc.common.util.HRReportParamUtils
 *  kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCommonEdit
 *  kd.hr.hrptmc.formplugin.web.repdesign.util.ReportQueryFilterUtil
 */
package kd.hr.hrptmc.formplugin.web.anobj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.ext.hr.filter.control.HRFilter;
import kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo;
import kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.container.Tab;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.service.labelandreport.HRFilterUtil;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrptmc.business.anobj.AnObjSideBarService;
import kd.hr.hrptmc.business.anobj.AnalyseObjectService;
import kd.hr.hrptmc.business.exception.HRPTMCBizException;
import kd.hr.hrptmc.business.repdesign.info.ReportInfo;
import kd.hr.hrptmc.business.repdesign.info.ReportManageConfigInfo;
import kd.hr.hrptmc.business.repdesign.opt.InitCallBackInfo;
import kd.hr.hrptmc.business.repdesign.opt.RptDrawOptCallBackInfo;
import kd.hr.hrptmc.common.constant.anobj.AnObjGroupFieldConstants;
import kd.hr.hrptmc.common.constant.anobj.AnalyseObjectConstants;
import kd.hr.hrptmc.common.model.anobj.AnObjGroupField;
import kd.hr.hrptmc.common.model.anobj.AnObjPivotBo;
import kd.hr.hrptmc.common.model.anobj.AnObjSideBar;
import kd.hr.hrptmc.common.model.anobj.QueryFieldBo;
import kd.hr.hrptmc.common.util.HRReportParamUtils;
import kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCommonEdit;
import kd.hr.hrptmc.formplugin.web.repdesign.util.ReportQueryFilterUtil;

public final class AnalyseObjectDataProcessEdit
extends AnalyseObjectCommonEdit
implements AnalyseObjectConstants,
AnObjGroupFieldConstants {
    private static final Log LOGGER = LogFactory.getLog(AnalyseObjectDataProcessEdit.class);

    public void beforeBindData(EventObject event) {
        super.beforeBindData(event);
        String sideBarsStr = this.getView().getPageCache().get("sideBar");
        if (HRStringUtils.isEmpty((String)sideBarsStr)) {
            ArrayList sideBars = Lists.newArrayListWithCapacity((int)10);
            this.getView().getPageCache().put("sideBar", SerializationUtils.toJsonString((Object)sideBars));
        }
    }

    public void customEvent(CustomEventArgs args) {
        switch (args.getEventName()) {
            case "showSideBar": {
                String currentBar = this.getPageCache().get("currentBarFlag");
                if (HRStringUtils.equals((String)currentBar, (String)"groupField")) {
                    String currentBarIndex = this.getView().getPageCache().get("currentBarIndex");
                    JSONObject params = JSON.parseObject((String)args.getEventArgs());
                    int index = (Integer)params.get("index");
                    if (HRStringUtils.isNotEmpty((String)currentBarIndex) && index == Integer.parseInt(currentBarIndex)) {
                        return;
                    }
                    this.groupFieldProcessor.requestCurrentGroupField();
                }
                this.showSideBar(args.getEventArgs());
                break;
            }
            case "deleteSideBar": {
                int index = Integer.parseInt(args.getEventArgs());
                this.getPageCache().put("deleteSideBarIndex", String.valueOf(--index));
                String sideBarStr = this.getPageCache().get("sideBar");
                List anObjSideBars = JSON.parseArray((String)sideBarStr, AnObjSideBar.class);
                AnObjSideBar anObjSideBar = (AnObjSideBar)anObjSideBars.get(index);
                if (HRStringUtils.equals((String)anObjSideBar.getType(), (String)"dataFilter")) {
                    ConfirmCallBackListener confirmListener = new ConfirmCallBackListener("confirmDelDataFilter", (IFormPlugin)this);
                    this.getView().showConfirm(ResManager.loadKDString((String)"\u786e\u8ba4\u8981\u5220\u9664\u6570\u636e\u8fc7\u6ee4\u5417\uff1f", (String)"AnalyseObjectDataProcessEdit_5", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, confirmListener);
                    break;
                }
                if (HRStringUtils.equals((String)anObjSideBar.getType(), (String)"pivot")) {
                    ConfirmCallBackListener confirmListener = new ConfirmCallBackListener("confirmDelPivot", (IFormPlugin)this);
                    this.getView().showConfirm(ResManager.loadKDString((String)"\u786e\u8ba4\u8981\u5220\u9664\u884c\u5217\u8f6c\u7f6e\u5417\uff1f", (String)"AnalyseObjectDataProcessEdit_6", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, confirmListener);
                    break;
                }
                if (!HRStringUtils.equals((String)anObjSideBar.getType(), (String)"groupField")) break;
                ConfirmCallBackListener confirmListener = new ConfirmCallBackListener("confirmDelGroupField", (IFormPlugin)this);
                this.getView().showConfirm(ResManager.loadKDString((String)"\u786e\u8ba4\u8981\u5220\u9664\u5206\u7ec4\u8d4b\u503c\u5417\uff1f", (String)"AnalyseObjectDataProcessEdit_8", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, confirmListener);
                break;
            }
            case "changeSelectedBaseDataOrEnumValFields": {
                this.changeSelectedBaseDataOrEnumValFields(args.getEventArgs());
                break;
            }
            case "selectDimVal": 
            case "selectIndexField": {
                this.selectDimValOrIndexField(args.getEventArgs());
                break;
            }
            case "previewPivotData": {
                this.previewPivotData(args.getEventArgs());
                break;
            }
            case "getDataView": {
                this.previewFilterData();
                break;
            }
            case "selectAnObjField": {
                this.groupFieldProcessor.selectAnObjField(args.getEventArgs());
                break;
            }
            case "openBaseDataF7": {
                this.groupFieldProcessor.openBaseDataF7(args.getEventArgs());
                break;
            }
            case "groupFieldDataView": {
                this.groupFieldPreviewData(args.getEventArgs());
                break;
            }
            case "getCurrentGroupField": {
                this.groupFieldProcessor.updateGroupFieldsCache(args.getEventArgs());
                break;
            }
            case "headFilter": {
                this.getPageCache().put("headFilter", "true");
                this.getPageCache().put("reportHeadFilter", args.getEventArgs());
                this.showTableData(true);
                this.getPageCache().put("headFilter", "false");
                break;
            }
            case "clearHeadFilter": {
                this.getPageCache().put("headFilter", "true");
                this.getPageCache().put("clearHeadFilter", args.getEventArgs());
                this.showTableData(true);
                this.getPageCache().remove("clearHeadFilter");
                this.getPageCache().put("headFilter", "false");
                break;
            }
        }
    }

    private void showSideBar(String evtArgs) {
        JSONObject params = JSON.parseObject((String)evtArgs);
        String sideBarType = (String)params.get("type");
        int index = (Integer)params.get("index");
        String sideBarStr = this.getPageCache().get("sideBar");
        List anObjSideBars = JSON.parseArray((String)sideBarStr, AnObjSideBar.class);
        if (HRStringUtils.equals((String)sideBarType, (String)"table")) {
            this.showTableData(false);
        } else if (HRStringUtils.equals((String)sideBarType, (String)"dataFilter")) {
            if (anObjSideBars.stream().noneMatch(sideBar -> HRStringUtils.equals((String)sideBar.getType(), (String)"dataFilter"))) {
                AnObjSideBarService.getInstance().newSideBarToCache(anObjSideBars, "dataFilter", this.getPageCache());
            }
            this.showDataFilter();
        } else if (HRStringUtils.equals((String)sideBarType, (String)"pivot")) {
            if (anObjSideBars.stream().noneMatch(sideBar -> HRStringUtils.equals((String)sideBar.getType(), (String)"pivot"))) {
                if (!this.groupFieldProcessor.validateAddPivotRefIndex()) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u5206\u6790\u5bf9\u8c61\u6709\u5206\u7ec4\u8d4b\u503c\u5b57\u6bb5\u5f15\u7528\u4e86\u6307\u6807\u7c7b\u578b\u7684\u5b57\u6bb5\uff0c\u65e0\u6cd5\u6dfb\u52a0\u884c\u5217\u8f6c\u7f6e\u3002", (String)"AnalyseObjectDataProcessEdit_10", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                    return;
                }
                CustomControl customcontrol = (CustomControl)this.getView().getControl("anobjsidebar");
                HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
                dataMap.put("method", "confirmAddPivot");
                dataMap.put("time", System.currentTimeMillis());
                customcontrol.setData((Object)dataMap);
                AnObjSideBarService.getInstance().newSideBarToCache(anObjSideBars, "pivot", this.getPageCache());
            }
            this.getView().setVisible(Boolean.FALSE, new String[]{"datafilterflex", "reporttableflex", "tableflex", "groupfieldflex"});
            this.getView().setVisible(Boolean.TRUE, new String[]{"anobjpivotplex"});
            this.updatePivot();
            this.showPivot();
        } else if (HRStringUtils.equals((String)sideBarType, (String)"groupField")) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"datafilterflex", "reporttableflex", "tableflex", "anobjpivotplex"});
            this.getView().setVisible(Boolean.TRUE, new String[]{"groupfieldflex"});
            String currentBar = this.getPageCache().get("currentBarFlag");
            this.groupFieldProcessor.showGroupField(index, anObjSideBars, HRStringUtils.equals((String)currentBar, (String)"groupField"));
        }
        this.getPageCache().put("sideBar", SerializationUtils.toJsonString((Object)anObjSideBars));
        this.getPageCache().put("currentBarFlag", sideBarType);
        this.getPageCache().put("currentBarIndex", String.valueOf(index));
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        if (HRStringUtils.equals((String)evt.getActionId(), (String)"baseDataF7Back")) {
            ListSelectedRowCollection rows = (ListSelectedRowCollection)evt.getReturnData();
            this.groupFieldProcessor.backF7Data(rows);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        if (!MessageBoxResult.Yes.equals((Object)evt.getResult())) {
            return;
        }
        String deleteSideBarIndex = this.getPageCache().get("deleteSideBarIndex");
        int index = Integer.parseInt(deleteSideBarIndex);
        List anObjSideBars = JSON.parseArray((String)this.getPageCache().get("sideBar"), AnObjSideBar.class);
        HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
        if ("confirmDelDataFilter".equals(evt.getCallBackId())) {
            AnObjSideBarService.getInstance().removeSideBarFromCache(index, this.getPageCache());
            this.getPageCache().put("currentBarFlag", "table");
            this.deleteDataFilter();
            this.showTableData(false);
            dataMap.put("method", "confirmDeleteDataFilter");
            dataMap.put("time", System.currentTimeMillis());
            this.formProcessor.clearDataFilterTable();
        } else if ("confirmDelPivot".equals(evt.getCallBackId())) {
            List originSideBars;
            boolean hasPivot;
            String originSideBar;
            Long id = (Long)this.getModel().getValue("id");
            Set<String> refReportUseFieldAliasList = Sets.newHashSetWithExpectedSize((int)16);
            if (id != null && id != 0L) {
                refReportUseFieldAliasList = AnalyseObjectService.getInstance().getRefReportUseFieldAliasList(id);
            }
            if ((originSideBar = this.getPageCache().get("originSideBar")) != null && (hasPivot = (originSideBars = JSON.parseArray((String)originSideBar, AnObjSideBar.class)).stream().anyMatch(sideBar -> HRStringUtils.equals((String)sideBar.getType(), (String)"pivot"))) && !refReportUseFieldAliasList.isEmpty() && !this.validateReportRefPivotFields(refReportUseFieldAliasList)) {
                return;
            }
            this.getPageCache().put("currentBarFlag", "table");
            AnObjSideBarService.getInstance().removeSideBarFromCache(index, this.getPageCache());
            this.deletePivot();
            this.showTableData(false);
            dataMap.put("method", "confirmDeletePivot");
            dataMap.put("time", System.currentTimeMillis());
        } else if ("confirmDelGroupField".equals(evt.getCallBackId())) {
            int removeSideBar = ((AnObjSideBar)anObjSideBars.get(index)).getGroupFieldIndex();
            if (!this.groupFieldProcessor.removeGroupField(index, removeSideBar)) {
                return;
            }
            dataMap.put("method", "confirmDeleteGroupField");
            dataMap.put("index", index + 1);
            dataMap.put("time", System.currentTimeMillis());
            this.formProcessor.clearGroupFieldTable();
            this.showTableData(false);
        }
        this.getPageCache().put("currentBarIndex", String.valueOf(index));
        CustomControl customcontrol = (CustomControl)this.getView().getControl("anobjsidebar");
        customcontrol.setData((Object)dataMap);
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean validateReportRefPivotFields(Set<String> refReportUseFieldAliasList) {
        List cachePivotConfig = this.pivotProcessor.getCachePivotConfig();
        for (AnObjPivotBo anObjPivotBo : cachePivotConfig) {
            if (!refReportUseFieldAliasList.contains(anObjPivotBo.getPivotIndexNum())) continue;
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u884c\u5217\u8f6c\u7f6e\u751f\u6210\u7684\u5b57\u6bb5\u5df2\u88ab\u62a5\u8868\u5f15\u7528\uff0c\u65e0\u6cd5\u5220\u9664\u3002", (String)"AnalyseObjectDataProcessEdit_7", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            return false;
        }
        return true;
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        super.beforeItemClick(evt);
        Tab tab = (Tab)this.getView().getControl("tabap");
        String currentBar = this.getPageCache().get("currentBarFlag");
        if (HRStringUtils.equals((String)evt.getItemKey(), (String)"bar_save") && HRStringUtils.equals((String)tab.getCurrentTab(), (String)"filterdata") && HRStringUtils.equals((String)currentBar, (String)"groupField")) {
            evt.setCancel(true);
            this.getPageCache().put("fromOp", "save");
            this.groupFieldProcessor.requestCurrentGroupField();
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String currentTab;
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        Tab tab = (Tab)this.getView().getControl("tabap");
        if (HRStringUtils.equals((String)operateKey, (String)"nextstep") && !args.isCancel()) {
            String nextTab = this.formProcessor.getNextTab(tab);
            if (nextTab == null) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u52ff\u91cd\u590d\u70b9\u51fb\u3002", (String)"AnalyseObjectDataProcessEdit_4", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            if (nextTab.equals("filterdata")) {
                String reportTableInitCompleted = this.getPageCache().get("reportTableInitCompleted");
                if (!HRStringUtils.equals((String)"true", (String)reportTableInitCompleted)) {
                    this.showReportTableAndQueryTableData();
                    this.getView().setVisible(Boolean.FALSE, new String[]{"datafilterflex", "anobjpivotplex", "groupfieldflex"});
                    this.initSideBar();
                    this.groupFieldProcessor.initGroupFieldControl();
                } else {
                    String currentBar = this.getPageCache().get("currentBarFlag");
                    if (HRStringUtils.equals((String)currentBar, (String)"pivot")) {
                        this.updatePivot();
                    } else if (HRStringUtils.equals((String)currentBar, (String)"groupField")) {
                        String sideBarStr = this.getPageCache().get("sideBar");
                        List anObjSideBars = JSON.parseArray((String)sideBarStr, AnObjSideBar.class);
                        String currentBarIndex = this.getPageCache().get("currentBarIndex");
                        this.groupFieldProcessor.showGroupField(Integer.parseInt(currentBarIndex), anObjSideBars, true);
                    } else if (HRStringUtils.isEmpty((String)currentBar) || HRStringUtils.equals((String)currentBar, (String)"table")) {
                        this.showTableData(true);
                    }
                }
            }
        } else if (HRStringUtils.equals((String)operateKey, (String)"save")) {
            if (HRStringUtils.equals((String)"filterdata", (String)tab.getCurrentTab()) && !this.groupFieldProcessor.validateAllGroupField()) {
                args.setCancel(true);
                return;
            }
            this.setValueToModel();
            if (!this.dataProcessor.validatePivotData()) {
                args.setCancel(true);
                return;
            }
            op.getOption().setVariableValue("sideBar", this.getPageCache().get("sideBar"));
            op.getOption().setVariableValue("cache_group_fields", this.getPageCache().get("cache_group_fields"));
        } else if (HRStringUtils.equals((String)operateKey, (String)"laststep") && HRStringUtils.equals((String)(currentTab = tab.getCurrentTab()), (String)"filterdata")) {
            String allIndexAliasStr = this.getPageCache().get("allIndexAlias");
            List selectedIndexAliasList = Lists.newArrayListWithCapacity((int)10);
            if (HRStringUtils.isNotEmpty((String)allIndexAliasStr)) {
                selectedIndexAliasList = (List)SerializationUtils.fromJsonString((String)allIndexAliasStr, List.class);
            }
            List indexAliasList = selectedIndexAliasList.stream().map(idx -> (String)idx.get("id")).collect(Collectors.toList());
            this.getPageCache().put("pivotindex", String.join((CharSequence)",", indexAliasList));
            String currentBar = this.getPageCache().get("currentBarFlag");
            if (HRStringUtils.equals((String)currentBar, (String)"groupField")) {
                this.groupFieldProcessor.requestCurrentGroupField();
            }
        }
    }

    private void setValueToModel() {
        List queryFields;
        Optional<QueryFieldBo> fieldOp;
        String pivotDim;
        String pivotIsCompleted = this.getPageCache().get("pivotIsCompleted");
        if (!HRStringUtils.equals((String)pivotIsCompleted, (String)"true")) {
            return;
        }
        String allIndexAliasStr = this.getPageCache().get("allIndexAlias");
        String allDimValItemsStr = this.getPageCache().get("allDimValItems");
        List selectedDimValItems = Lists.newArrayListWithCapacity((int)10);
        List selectedIndexAliasList = Lists.newArrayListWithCapacity((int)10);
        if (HRStringUtils.isNotEmpty((String)allDimValItemsStr)) {
            selectedDimValItems = (List)SerializationUtils.fromJsonString((String)allDimValItemsStr, List.class);
        }
        if (HRStringUtils.isNotEmpty((String)allIndexAliasStr)) {
            selectedIndexAliasList = (List)SerializationUtils.fromJsonString((String)allIndexAliasStr, List.class);
        }
        if (HRStringUtils.isNotEmpty((String)(pivotDim = (String)this.getModel().getValue("pivotdim"))) && !selectedDimValItems.isEmpty() && (fieldOp = (queryFields = this.commonProcessor.getCacheQueryFields(true)).stream().filter(field -> HRStringUtils.equals((String)pivotDim, (String)field.getFieldAlias())).findAny()).isPresent()) {
            this.pivotProcessor.getPivotDimValItemCount(fieldOp.get().getHRFilterParam(), this.getHRFilter().getValue(), selectedDimValItems);
        }
        if (selectedDimValItems.isEmpty()) {
            this.getModel().setValue("pivotdimval", null);
        } else {
            List dimValIdList = selectedDimValItems.stream().map(dim -> (String)dim.get("id")).collect(Collectors.toList());
            this.getModel().setValue("pivotdimval", (Object)String.join((CharSequence)",", dimValIdList));
        }
        if (selectedIndexAliasList.isEmpty()) {
            this.getModel().setValue("pivotindex", null);
        } else {
            List indexAliasList = selectedIndexAliasList.stream().map(idx -> (String)idx.get("id")).collect(Collectors.toList());
            this.getModel().setValue("pivotindex", (Object)String.join((CharSequence)",", indexAliasList));
        }
    }

    private void initSideBar() {
        String initCompleted = this.getPageCache().get("initSideBarCompleted");
        if (HRStringUtils.equals((String)initCompleted, (String)"1")) {
            return;
        }
        CustomControl customcontrol = (CustomControl)this.getView().getControl("anobjsidebar");
        HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
        dataMap.put("method", "initSideBar");
        ILocaleString anObjName = (ILocaleString)this.getModel().getValue("name");
        dataMap.put("anobjName", anObjName.getDefaultItem());
        dataMap.put("isRefByReport", Boolean.valueOf(this.getPageCache().get("anObjRefByReport")));
        String sideBarStr = this.getPageCache().get("sideBar");
        List anObjSideBars = Lists.newArrayListWithCapacity((int)10);
        if (HRStringUtils.isNotEmpty((String)sideBarStr)) {
            anObjSideBars = JSON.parseArray((String)sideBarStr, AnObjSideBar.class);
        }
        dataMap.put("sideList", anObjSideBars);
        String statusParam = this.getView().getFormShowParameter().getStatus().toString();
        if (HRStringUtils.equals((String)statusParam, (String)OperationStatus.EDIT.toString()) && ((Boolean)this.getModel().getValue("issyspreset")).booleanValue()) {
            statusParam = OperationStatus.VIEW.toString();
        }
        this.getPageCache().put("currentPageStatus", statusParam);
        dataMap.put("status", statusParam);
        customcontrol.setData((Object)dataMap);
        this.getPageCache().put("initSideBarCompleted", "1");
    }

    private void previewPivotData(String args) {
        JSONObject dataMap = JSON.parseObject((String)args);
        String dimField = (String)dataMap.get("dimField");
        List dimValItems = (List)dataMap.get("dimValItems");
        List indexFields = (List)dataMap.get("indexFields");
        if (HRStringUtils.isEmpty((String)dimField) || dimValItems.isEmpty() || indexFields.isEmpty()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u914d\u7f6e\u4e0d\u5b8c\u6574\uff0c\u8bf7\u4fee\u6b63\u540e\u518d\u8fdb\u884c\u6570\u636e\u9884\u89c8\u3002", (String)"AnalyseObjectDataProcessEdit_3", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
            return;
        }
        HashMap transInfo = Maps.newHashMapWithExpectedSize((int)3);
        transInfo.put("transNameAlias", dimField);
        this.pivotProcessor.generateNewIndex(dimValItems, indexFields, (Map)transInfo);
        this.getPageCache().put("transPositionInfo", SerializationUtils.toJsonString((Object)transInfo));
        this.previewData("anobjpivotcontrol", this.formProcessor.getFrontSideBar(this.getView()));
    }

    private void groupFieldPreviewData(String args) {
        StringBuilder errorMsg;
        List cacheCalculateFields;
        List cacheQueryFields;
        List cacheGroupFields;
        AnObjGroupField groupField = (AnObjGroupField)JSON.parseObject((String)args, AnObjGroupField.class);
        if (this.groupFieldProcessor.validateGroupField(groupField, cacheGroupFields = this.commonProcessor.getCacheGroupFields(), cacheQueryFields = this.commonProcessor.getCacheQueryFields(false), cacheCalculateFields = this.commonProcessor.getCacheCalculateFields(false), true, errorMsg = new StringBuilder())) {
            this.groupFieldProcessor.putGroupFieldsToCache(cacheGroupFields);
            this.previewData("groupfieldcontrol", this.formProcessor.getFrontSideBar(this.getView()));
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void previewFilterData() {
        RuleValidateInfo info;
        String dataFilter = this.getHRFilter().getValue();
        if (StringUtils.isNotEmpty((CharSequence)dataFilter) && !(info = RuleValidateUtil.validCondition((String)dataFilter, (boolean)true)).isSuccess()) {
            StringBuilder errorMsg = new StringBuilder();
            for (String msg : info.getMsgList()) {
                errorMsg.append(msg).append(',');
            }
            this.getView().showTipNotification(errorMsg.substring(0, errorMsg.length() - 1));
            return;
        }
        this.previewData("anobjdatafiltercontrol", this.formProcessor.getFrontSideBar(this.getView()));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void previewData(String control, List<AnObjSideBar> sideBars) {
        RuleValidateInfo info;
        String dataFilter;
        boolean addDataFilter = false;
        boolean addPivot = false;
        ArrayList groupFieldIndexList = Lists.newArrayListWithCapacity((int)10);
        for (AnObjSideBar sideBar : sideBars) {
            if (HRStringUtils.equals((String)sideBar.getType(), (String)"dataFilter")) {
                addDataFilter = true;
                continue;
            }
            if (HRStringUtils.equals((String)sideBar.getType(), (String)"pivot")) {
                addPivot = true;
                continue;
            }
            groupFieldIndexList.add(sideBar.getGroupFieldIndex());
        }
        List<Object> qFilters = Lists.newArrayListWithCapacity((int)10);
        if (addDataFilter && StringUtils.isNotEmpty((CharSequence)(dataFilter = this.getHRFilter().getValue())) && (info = RuleValidateUtil.validCondition((String)dataFilter, (boolean)true)).isSuccess()) {
            qFilters = this.transferToQFilter(dataFilter);
            qFilters.removeIf(Objects::isNull);
        }
        List groupFieldCalculates = this.groupFieldProcessor.transferCalculateFields((List)groupFieldIndexList);
        this.getView().showLoading(new LocaleString(ResManager.loadKDString((String)"\u6b63\u5728\u52a0\u8f7d...", (String)"AnalyseObjectDataProcessEdit_9", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0])));
        try {
            ILocaleString name = (ILocaleString)this.getModel().getValue("name");
            String number = (String)this.getModel().getValue("number");
            ReportManageConfigInfo reportManageConfigInfo = this.pivotProcessor.buildReportManageConfigInfo(name.getLocaleValue(), number, addPivot, groupFieldCalculates, this.getPageCache());
            ReportInfo reportInfo = this.pivotProcessor.getReportInfo(reportManageConfigInfo, qFilters, groupFieldCalculates, this.getPageCache());
            RptDrawOptCallBackInfo rptDrawOptCallBackInfo = new RptDrawOptCallBackInfo(reportInfo);
            IClientViewProxy clientViewProxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
            clientViewProxy.setEntryProperty(control, "data", (Object)rptDrawOptCallBackInfo);
        }
        catch (HRPTMCBizException exception) {
            LOGGER.error("queryData error:", (Throwable)exception);
            this.getView().showTipNotification(exception.getMessage());
        }
        catch (Exception exception) {
            LOGGER.error("queryData error:", (Throwable)exception);
        }
        finally {
            this.getView().hideLoading();
        }
    }

    private List<String> getSelectedDimValues() {
        String dimValStr = (String)this.getModel().getValue("pivotdimval");
        return Arrays.stream(dimValStr.split(",")).filter(HRStringUtils::isNotEmpty).collect(Collectors.toList());
    }

    private List<String> getSelectedIndexes() {
        String indexStr = (String)this.getModel().getValue("pivotindex");
        return Arrays.stream(indexStr.split(",")).filter(HRStringUtils::isNotEmpty).collect(Collectors.toList());
    }

    private void showTableData(boolean queryData) {
        this.getView().setVisible(Boolean.FALSE, new String[]{"datafilterflex", "anobjpivotplex", "groupfieldflex"});
        this.getView().setVisible(Boolean.TRUE, new String[]{"reporttableflex", "tableflex"});
        if (queryData) {
            this.getView().showLoading(new LocaleString(ResManager.loadKDString((String)"\u6b63\u5728\u52a0\u8f7d...", (String)"AnalyseObjectDataProcessEdit_9", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0])));
            try {
                IClientViewProxy clientViewProxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
                clientViewProxy.setEntryProperty("reportform", "data", (Object)new RptDrawOptCallBackInfo(this.getTableData()));
            }
            catch (HRPTMCBizException exception) {
                LOGGER.error("queryData error:", (Throwable)exception);
                this.getView().showTipNotification(exception.getMessage());
            }
            catch (Exception exception) {
                LOGGER.error("queryData error:", (Throwable)exception);
            }
            finally {
                this.getView().hideLoading();
            }
        }
    }

    private void showDataFilter() {
        this.getView().setVisible(Boolean.FALSE, new String[]{"reporttableflex", "anobjpivotplex", "groupfieldflex"});
        this.getView().setVisible(Boolean.TRUE, new String[]{"datafilterflex"});
    }

    @ExcludeFromJacocoGeneratedReport
    private void updatePivot() {
        String pivotIsCompleted = this.getPageCache().get("pivotIsCompleted");
        if (!HRStringUtils.equals((String)pivotIsCompleted, (String)"true")) {
            this.showPivot();
            return;
        }
        Map<String, Object> pivotData = this.getPivotData();
        List selectedBaseDataOrEnumValFields = (List)pivotData.get("selectedBaseDataOrEnumValFields");
        List indexFields = (List)pivotData.get("indexFields");
        List allGroupFieldsDependFieldsNumberList = this.groupFieldProcessor.getAllGroupFieldsDependFieldsNumberList();
        selectedBaseDataOrEnumValFields.removeIf(map -> allGroupFieldsDependFieldsNumberList.contains((String)map.get("key")));
        indexFields.removeIf(map -> allGroupFieldsDependFieldsNumberList.contains((String)map.get("id")));
        String allIndexAliasStr = this.getPageCache().get("allIndexAlias");
        if (HRStringUtils.isNotEmpty((String)allIndexAliasStr)) {
            List selectedIndexAliasList = (List)SerializationUtils.fromJsonString((String)allIndexAliasStr, List.class);
            Set selectedIndexNum = selectedIndexAliasList.stream().map(index -> (String)index.get("id")).collect(Collectors.toSet());
            indexFields.stream().filter(index -> selectedIndexNum.contains((String)index.get("id"))).forEach(index -> index.put("selected", true));
        }
        HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
        dataMap.put("method", "updatePivot");
        dataMap.put("selectedBaseDataOrEnumValFields", selectedBaseDataOrEnumValFields);
        dataMap.put("indexFields", indexFields);
        dataMap.put("dimValItems", pivotData.get("dimValItems"));
        dataMap.put("time", new Date());
        CustomControl customcontrol = (CustomControl)this.getView().getControl("anobjpivotcontrol");
        customcontrol.setData((Object)dataMap);
    }

    private void showPivot() {
        String pivotIsCompleted = this.getPageCache().get("pivotIsCompleted");
        if (HRStringUtils.equals((String)pivotIsCompleted, (String)"true")) {
            return;
        }
        Map<String, Object> pivotData = this.getPivotData();
        String dimField = (String)pivotData.get("dimField");
        List selectedBaseDataOrEnumValFields = (List)pivotData.get("selectedBaseDataOrEnumValFields");
        List indexFields = (List)pivotData.get("indexFields");
        List dimValItems = (List)pivotData.get("dimValItems");
        List selectedDimValItems = dimValItems.stream().filter(item -> item.get("selected") != null && (Boolean)item.get("selected") != false).collect(Collectors.toList());
        List selectedIndexFields = indexFields.stream().filter(item -> item.get("selected") != null && (Boolean)item.get("selected") != false).collect(Collectors.toList());
        this.getPageCache().put("allDimValItems", SerializationUtils.toJsonString(selectedDimValItems));
        this.getPageCache().put("allIndexAlias", SerializationUtils.toJsonString(selectedIndexFields));
        HashMap transPositionInfoMap = Maps.newHashMapWithExpectedSize((int)16);
        List newIndexes = this.pivotProcessor.generateNewIndex(selectedDimValItems, selectedIndexFields, (Map)transPositionInfoMap);
        if (!transPositionInfoMap.isEmpty()) {
            transPositionInfoMap.put("transNameAlias", dimField);
            this.getPageCache().put("transPositionInfo", SerializationUtils.toJsonString((Object)transPositionInfoMap));
        }
        List allGroupFieldsDependFieldsNumberList = this.groupFieldProcessor.getAllGroupFieldsDependFieldsNumberList();
        selectedBaseDataOrEnumValFields.removeIf(map -> allGroupFieldsDependFieldsNumberList.contains((String)map.get("key")));
        indexFields.removeIf(map -> allGroupFieldsDependFieldsNumberList.contains((String)map.get("id")));
        HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
        dataMap.put("method", "showPivot");
        dataMap.put("selectedBaseDataOrEnumValFields", selectedBaseDataOrEnumValFields);
        dataMap.put("dimValItems", dimValItems);
        dataMap.put("indexFields", indexFields);
        dataMap.put("newIndexes", newIndexes);
        dataMap.put("pageStatus", this.getPageCache().get("pageStatus"));
        dataMap.put("time", new Date());
        CustomControl customcontrol = (CustomControl)this.getView().getControl("anobjpivotcontrol");
        customcontrol.setData((Object)dataMap);
        this.getPageCache().put("pivotIsCompleted", "true");
    }

    private Map<String, Object> getPivotData() {
        List selectedBaseDataOrEnumValFields = this.pivotProcessor.getSelectedBaseDataOrEnumValFields();
        List indexFields = this.pivotProcessor.getSelectedIndexFields(this.getView());
        String dimField = (String)this.getModel().getValue("pivotdim");
        if (HRStringUtils.isNotEmpty((String)dimField)) {
            for (Map selectedBaseDataOrEnumValField : selectedBaseDataOrEnumValFields) {
                if (!HRStringUtils.equals((String)dimField, (String)((String)selectedBaseDataOrEnumValField.get("key")))) continue;
                selectedBaseDataOrEnumValField.put("selected", true);
            }
        }
        List anObjPivotConfigs = this.commonProcessor.getCachePivotConfig();
        ArrayList dimValueKeys = Lists.newArrayListWithCapacity((int)10);
        ArrayList indexesKeys = Lists.newArrayListWithCapacity((int)10);
        HashSet lockDimVal = Sets.newHashSetWithExpectedSize((int)16);
        HashSet lockIndexAlias = Sets.newHashSetWithExpectedSize((int)16);
        Set refByReportFieldAliasList = AnalyseObjectService.getInstance().getRefByReportFieldAliasCollection((Long)this.getModel().getValue("id"));
        if (!refByReportFieldAliasList.isEmpty()) {
            String pivotDimValStr = (String)this.getModel().getValue("pivotdimval");
            String pivotIndexStr = (String)this.getModel().getValue("pivotindex");
            String[] pivotDimValArr = pivotDimValStr.split(",");
            String[] pivotIndexArr = pivotIndexStr.split(",");
            for (String dimVal : pivotDimValArr) {
                for (String indexAlias : pivotIndexArr) {
                    anObjPivotConfigs.stream().filter(pivot -> HRStringUtils.equals((String)pivot.getPivotDimVal(), (String)dimVal) && HRStringUtils.equals((String)pivot.getPivotIndex(), (String)indexAlias)).findAny().ifPresent(pivotBo -> {
                        if (refByReportFieldAliasList.contains(pivotBo.getPivotIndexNum())) {
                            lockDimVal.add(dimVal);
                            lockIndexAlias.add(indexAlias);
                        }
                    });
                }
            }
        }
        if (!lockDimVal.isEmpty()) {
            selectedBaseDataOrEnumValFields.stream().filter(dim -> dim.get("selected") != null && (Boolean)dim.get("selected") != false).findAny().ifPresent(dim -> dim.put("lock", true));
        }
        List dimValItems = Lists.newArrayListWithCapacity((int)10);
        if (HRStringUtils.isNotEmpty((String)dimField)) {
            dimValItems = this.pivotProcessor.queryLatitudeItems(dimField);
            String queryFieldsStr = this.getPageCache().get("queryFields");
            List queryFields = JSON.parseArray((String)queryFieldsStr, QueryFieldBo.class);
            Optional<QueryFieldBo> queryFieldBo = queryFields.stream().filter(field -> HRStringUtils.equals((String)field.getFieldAlias(), (String)dimField)).findAny();
            if (queryFieldBo.isPresent()) {
                this.pivotProcessor.getPivotDimValItemCount(((QueryFieldBo)queryFieldBo.get()).getHRFilterParam(), this.getHRFilter().getValue(), dimValItems);
            }
        }
        List<String> selectedDimValues = this.getSelectedDimValues();
        List<String> selectedIndexes = this.getSelectedIndexes();
        for (Map item : dimValItems) {
            String id = (String)item.get("id");
            if (!selectedDimValues.contains(id)) continue;
            item.put("selected", true);
            item.put("lock", lockDimVal.contains(id));
            dimValueKeys.add((String)item.get("key"));
        }
        for (Map indexMap : indexFields) {
            String fieldAlias = (String)indexMap.get("id");
            if (!selectedIndexes.contains(fieldAlias)) continue;
            indexMap.put("selected", true);
            indexMap.put("lock", lockIndexAlias.contains(fieldAlias));
            indexesKeys.add((String)indexMap.get("key"));
        }
        this.getPageCache().put("pivotdimval", String.join((CharSequence)",", dimValueKeys));
        this.getPageCache().put("pivotindex", String.join((CharSequence)",", indexesKeys));
        HashMap returnData = Maps.newHashMapWithExpectedSize((int)16);
        returnData.put("dimField", dimField);
        returnData.put("selectedBaseDataOrEnumValFields", selectedBaseDataOrEnumValFields);
        returnData.put("indexFields", indexFields);
        returnData.put("dimValItems", dimValItems);
        return returnData;
    }

    private void changeSelectedBaseDataOrEnumValFields(String newDimField) {
        String queryFieldsStr = this.getPageCache().get("queryFields");
        List queryFields = JSON.parseArray((String)queryFieldsStr, QueryFieldBo.class);
        Optional<QueryFieldBo> queryFieldBo = queryFields.stream().filter(field -> HRStringUtils.equals((String)field.getFieldAlias(), (String)newDimField)).findAny();
        if (!queryFieldBo.isPresent()) {
            return;
        }
        List dimValItems = this.pivotProcessor.queryLatitudeItems(newDimField);
        int pivotDimCountThresh = HRReportParamUtils.getPivotDimCountThresh();
        int dimValItemCount = this.pivotProcessor.getPivotDimValItemCount(queryFieldBo.get().getHRFilterParam(), this.getHRFilter().getValue(), dimValItems);
        LOGGER.info("AnalyseObjectDataProcessEdit changeSelectedBaseDataOrEnumValFields dimValItemCount: {}", (Object)dimValItemCount);
        if (dimValItemCount > pivotDimCountThresh) {
            this.getView().showTipNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u53bb\u91cd\u540e\u6570\u636e\u91cf\u8d85\u8fc7\u6700\u5927\u503c\uff0c\u65e0\u6cd5\u4f5c\u4e3a\u8f6c\u7f6e\u7684\u5217\u3002", (String)"AnalyseObjectDataProcessEdit_2", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), queryFieldBo.get().getFieldName()));
            this.getModel().setValue("pivotdim", null);
            return;
        }
        this.getView().setVisible(Boolean.FALSE, new String[]{"labeldim"});
        dimValItems.forEach(item -> item.put("selected", true));
        this.getPageCache().put("allDimValItems", SerializationUtils.toJsonString((Object)dimValItems));
        this.getModel().setValue("pivotdim", (Object)newDimField);
        HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
        dataMap.put("method", "changeSelectedBaseDataOrEnumValFields");
        dataMap.put("newDimField", newDimField);
        List newIndexes = Collections.emptyList();
        List indexFields = Lists.newArrayListWithCapacity((int)10);
        String allIndexStr = this.getPageCache().get("allIndexAlias");
        if (HRStringUtils.isNotEmpty((String)allIndexStr)) {
            indexFields = (List)SerializationUtils.fromJsonString((String)allIndexStr, List.class);
        }
        if (HRStringUtils.isNotEmpty((String)newDimField) && !dimValItems.isEmpty() && !indexFields.isEmpty()) {
            newIndexes = this.pivotProcessor.generateNewIndex(dimValItems, indexFields, (Map)Maps.newHashMapWithExpectedSize((int)16));
        }
        dataMap.put("newIndexes", newIndexes);
        dataMap.put("dimValItems", dimValItems);
        dataMap.put("time", new Date());
        CustomControl customcontrol = (CustomControl)this.getView().getControl("anobjpivotcontrol");
        customcontrol.setData((Object)dataMap);
    }

    private void selectDimValOrIndexField(String args) {
        if (HRStringUtils.isEmpty((String)args)) {
            return;
        }
        JSONObject dataMap = JSON.parseObject((String)args);
        String dimField = (String)dataMap.get("dimField");
        List dimValItems = (List)dataMap.get("dimValItems");
        List indexFields = (List)dataMap.get("indexFields");
        this.getPageCache().put("allIndexAlias", SerializationUtils.toJsonString((Object)indexFields));
        this.getPageCache().put("allDimValItems", SerializationUtils.toJsonString((Object)dimValItems));
        List newIndexes = Collections.emptyList();
        if (HRStringUtils.isNotEmpty((String)dimField) && !dimValItems.isEmpty() && !indexFields.isEmpty()) {
            newIndexes = this.pivotProcessor.generateNewIndex(dimValItems, indexFields, (Map)Maps.newHashMapWithExpectedSize((int)16));
        }
        CustomControl customcontrol = (CustomControl)this.getView().getControl("anobjpivotcontrol");
        HashMap data = Maps.newHashMapWithExpectedSize((int)16);
        data.put("method", "updateNewIndexes");
        data.put("newIndexes", newIndexes);
        dataMap.put("time", new Date());
        customcontrol.setData((Object)data);
    }

    private void deleteDataFilter() {
        HRFilter filterAp = (HRFilter)this.getControl("hrfilterap");
        filterAp.clear();
    }

    private void deletePivot() {
        this.getModel().setValue("pivotdim", null);
        this.getModel().setValue("pivotdimval", null);
        this.getModel().setValue("pivotindex", null);
        this.getPageCache().remove("pivotdimval");
        this.getPageCache().remove("pivotindex");
        this.getPageCache().remove("allDimValItems");
        this.getPageCache().remove("allIndexAlias");
        this.getPageCache().remove("pivotIsCompleted");
        this.getPageCache().remove("transPositionInfo");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void showReportTableAndQueryTableData() {
        this.getView().showLoading(new LocaleString(ResManager.loadKDString((String)"\u6b63\u5728\u52a0\u8f7d...", (String)"AnalyseObjectDataProcessEdit_9", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0])));
        try {
            ILocaleString name = (ILocaleString)this.getModel().getValue("name");
            String number = (String)this.getModel().getValue("number");
            ReportManageConfigInfo reportManageConfigInfo = this.pivotProcessor.buildReportManageConfigInfo(name.getLocaleValue(), number, false, Collections.emptyList(), this.getPageCache());
            InitCallBackInfo initCallBackInfo = new InitCallBackInfo(this.getView().getFormShowParameter().getStatus().toString(), reportManageConfigInfo, true);
            ReportInfo reportInfo = this.pivotProcessor.getReportInfo(reportManageConfigInfo, (List)Lists.newArrayListWithExpectedSize((int)1), this.getPageCache());
            initCallBackInfo.getReportManageConfigInfo().setReportDetail(reportInfo);
            IClientViewProxy clientViewProxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
            clientViewProxy.setEntryProperty("reportform", "data", (Object)initCallBackInfo);
            this.getPageCache().put("reportTableInitCompleted", "true");
        }
        catch (HRPTMCBizException exception) {
            LOGGER.error("queryData error:", (Throwable)exception);
            this.getView().showTipNotification(exception.getMessage());
        }
        catch (Exception exception) {
            LOGGER.error("queryData error:", (Throwable)exception);
        }
        finally {
            this.getView().hideLoading();
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private ReportInfo getTableData() {
        ILocaleString name = (ILocaleString)this.getModel().getValue("name");
        String number = (String)this.getModel().getValue("number");
        ReportManageConfigInfo reportManageConfigInfo = this.pivotProcessor.buildReportManageConfigInfo(name.getLocaleValue(), number, true, Collections.emptyList(), this.getPageCache());
        List qFilters = ReportQueryFilterUtil.getHeadQFilterList((ReportManageConfigInfo)reportManageConfigInfo);
        return this.pivotProcessor.getReportInfo(reportManageConfigInfo, qFilters, this.getPageCache());
    }

    private List<QFilter> transferToQFilter(String dataFilter) {
        List queryFieldBos;
        QFilter qFilter;
        ArrayList qFilters = Lists.newArrayListWithCapacity((int)10);
        if (!HRStringUtils.isEmpty((String)dataFilter) && (qFilter = HRFilterUtil.condition2QFilter4HRReport((String)dataFilter, (String)SerializationUtils.toJsonString((Object)(queryFieldBos = JSON.parseArray((String)this.getPageCache().get("queryFields"), QueryFieldBo.class))))) != null) {
            qFilters.add(qFilter);
        }
        return qFilters;
    }
}
