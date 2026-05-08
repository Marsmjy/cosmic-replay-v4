/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.RefObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.exception.KDBizException
 *  kd.bos.ext.hr.ruleengine.utils.IDStringUtils
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrptmc.business.filesource.ReportFileSourceService
 *  kd.hr.hrptmc.business.publish.HRReportPublishMenuService
 *  kd.hr.hrptmc.business.repcalculate.org.helper.AdminOrgCalHelper
 *  kd.hr.hrptmc.business.repdesign.ReportManageService
 *  kd.hr.hrptmc.business.repdesign.ReportPreViewService
 *  kd.hr.hrptmc.business.repdesign.RptDisplaySchemeService
 *  kd.hr.hrptmc.business.repdesign.datastore.ReportDataStoreServiceHelper
 *  kd.hr.hrptmc.business.repdesign.info.AnObjDetailInfo
 *  kd.hr.hrptmc.business.repdesign.info.ReportBillHeadInfo
 *  kd.hr.hrptmc.business.repdesign.info.ReportManageConfigInfo
 *  kd.hr.hrptmc.business.repdesign.info.ReportMarkInfo
 *  kd.hr.hrptmc.business.repdesign.info.RptDisplaySchemeInfo
 *  kd.hr.hrptmc.business.repdesign.jump.ReportJumpConfigService
 *  kd.hr.hrptmc.business.repdesign.opt.InitCallBackInfo
 *  kd.hr.hrptmc.common.util.LocaleStringUtils
 *  kd.hr.hrptmc.formplugin.web.filesource.ReportFileSourceCustomSortHandler
 *  kd.hr.hrptmc.formplugin.web.preindex.util.PreIndexReportPageHandler
 *  kd.hr.hrptmc.formplugin.web.repdesign.ReportManageServicePlugin
 *  kd.hr.hrptmc.formplugin.web.repdesign.service.ReportConfigService
 *  kd.hr.hrptmc.formplugin.web.repdesign.util.DisplaySchemeUtil
 *  kd.hr.hrptmc.formplugin.web.repdesign.util.ReportCopyUtils
 *  kd.hr.hrptmc.formplugin.web.repdesign.util.ReportManagePopUtil
 *  kd.hr.hrptmc.formplugin.web.repdesign.util.ReportManageUtil
 *  kd.hr.hrptmc.formplugin.web.repdesign.util.ReportQuerySchemeUtils
 */
package kd.hr.hrptmc.formplugin.web.repdesign;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.RefObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.exception.KDBizException;
import kd.bos.ext.hr.ruleengine.utils.IDStringUtils;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrptmc.business.filesource.ReportFileSourceService;
import kd.hr.hrptmc.business.publish.HRReportPublishMenuService;
import kd.hr.hrptmc.business.repcalculate.org.helper.AdminOrgCalHelper;
import kd.hr.hrptmc.business.repdesign.ReportManageService;
import kd.hr.hrptmc.business.repdesign.ReportPreViewService;
import kd.hr.hrptmc.business.repdesign.RptDisplaySchemeService;
import kd.hr.hrptmc.business.repdesign.datastore.ReportDataStoreServiceHelper;
import kd.hr.hrptmc.business.repdesign.info.AnObjDetailInfo;
import kd.hr.hrptmc.business.repdesign.info.ReportBillHeadInfo;
import kd.hr.hrptmc.business.repdesign.info.ReportManageConfigInfo;
import kd.hr.hrptmc.business.repdesign.info.ReportMarkInfo;
import kd.hr.hrptmc.business.repdesign.info.RptDisplaySchemeInfo;
import kd.hr.hrptmc.business.repdesign.jump.ReportJumpConfigService;
import kd.hr.hrptmc.business.repdesign.opt.InitCallBackInfo;
import kd.hr.hrptmc.common.util.LocaleStringUtils;
import kd.hr.hrptmc.formplugin.web.filesource.ReportFileSourceCustomSortHandler;
import kd.hr.hrptmc.formplugin.web.preindex.util.PreIndexReportPageHandler;
import kd.hr.hrptmc.formplugin.web.repdesign.ReportManageServicePlugin;
import kd.hr.hrptmc.formplugin.web.repdesign.service.ReportConfigService;
import kd.hr.hrptmc.formplugin.web.repdesign.util.DisplaySchemeUtil;
import kd.hr.hrptmc.formplugin.web.repdesign.util.ReportCopyUtils;
import kd.hr.hrptmc.formplugin.web.repdesign.util.ReportManagePopUtil;
import kd.hr.hrptmc.formplugin.web.repdesign.util.ReportManageUtil;
import kd.hr.hrptmc.formplugin.web.repdesign.util.ReportQuerySchemeUtils;

public final class ReportManageEditPlugin
extends ReportManageServicePlugin {
    private static final Log LOGGER = LogFactory.getLog(ReportManageEditPlugin.class);
    private static final String KEY_FILE_SOURCE_TABLE_NAME = "fileSourceTableName";
    private static final String CALL_BACK_RPT_CENTER_OFFLINE = "callback_rpt_center_offline";
    private static final List<String> CLEAR_CACHE_KEY = Lists.newArrayListWithCapacity((int)16);

    public void pageRelease(EventObject evt) {
        AdminOrgCalHelper.cleanCalResultCache((String)this.getView().getPageId());
        super.pageRelease(evt);
        String billHead = this.getView().getPageCache().get("orgBillHeadInfo");
        ReportFileSourceService.getInstance().clearFileSourceDataForReport(((ReportBillHeadInfo)JSON.parseObject((String)billHead, ReportBillHeadInfo.class)).getNumber(), this.getPageCache().get(KEY_FILE_SOURCE_TABLE_NAME));
        AnObjDetailInfo anObjDetailInfo = (AnObjDetailInfo)SerializationUtils.fromJsonString((String)this.getView().getPageCache().get("anObjDetailInfo"), AnObjDetailInfo.class);
        ReportManageConfigInfo oriReportManageConfigInfo = (ReportManageConfigInfo)SerializationUtils.fromJsonString((String)this.getView().getPageCache().get("oriReportManageConfigInfo"), ReportManageConfigInfo.class);
        ReportFileSourceCustomSortHandler.handleCustomSortBeforeCloseReportPage((AnObjDetailInfo)anObjDetailInfo, (ReportManageConfigInfo)oriReportManageConfigInfo);
    }

    public void afterCreateNewData(EventObject evt) {
        super.afterCreateNewData(evt);
        String billHeadInfoStr = this.getView().getFormShowParameter().getCustomParam("billHeadInfo").toString();
        ReportBillHeadInfo billHeadInfo = (ReportBillHeadInfo)SerializationUtils.fromJsonString((String)billHeadInfoStr, ReportBillHeadInfo.class);
        this.updateBillHead(billHeadInfo);
        this.putPageCache("orgBillHeadInfo", billHeadInfo);
        if (IDStringUtils.idNotEmpty((Long)billHeadInfo.getSrcRptId())) {
            this.loadRptManageInfo(billHeadInfo.getSrcRptId(), true);
        } else {
            this.genReportManageConfigInfo();
        }
    }

    private ReportManageConfigInfo genReportManageConfigInfo() {
        this.clearPageCache();
        long rptMngCfgId = (Long)this.getModel().getValue("id");
        long anObjId = ((DynamicObject)this.getModel().getValue("anobjid")).getLong("id");
        ReportManageConfigInfo reportManageConfigInfo = ReportManageUtil.genEmptyRptMngCfgInfo((long)rptMngCfgId, (long)anObjId);
        this.putPageCache(reportManageConfigInfo);
        this.putPageCache("oriReportManageConfigInfo", reportManageConfigInfo);
        return reportManageConfigInfo;
    }

    private void updateBillHead(ReportBillHeadInfo billHeadInfo) {
        if (null != billHeadInfo) {
            this.getModel().setValue("createorg", (Object)billHeadInfo.getCreateOrgId());
            this.getModel().setValue("name", (Object)billHeadInfo.getName());
            this.getModel().setValue("number", (Object)billHeadInfo.getNumber());
            this.getModel().setValue("cloudid", (Object)billHeadInfo.getCloudId());
            this.getModel().setValue("anobjid", (Object)billHeadInfo.getAnObjId());
            this.getModel().setValue("description", (Object)billHeadInfo.getDescription());
            this.getPageCache().put(KEY_FILE_SOURCE_TABLE_NAME, billHeadInfo.getFileSourceTableName());
            this.getModel().setDataChanged(false);
        }
    }

    private ReportBillHeadInfo getBillHead() {
        ReportBillHeadInfo reportBillHeadInfo = new ReportBillHeadInfo();
        DynamicObject createOrgDy = (DynamicObject)this.getModel().getValue("createorg");
        if (createOrgDy != null) {
            reportBillHeadInfo.setCreateOrgId(Long.valueOf(createOrgDy.getLong("id")));
        }
        reportBillHeadInfo.setNumber((String)this.getModel().getValue("number"));
        reportBillHeadInfo.setName(LocaleStringUtils.getLocaleString((Object)this.getModel().getValue("name")));
        if (this.getModel().getDataEntity().getDynamicObject("cloudid") != null) {
            reportBillHeadInfo.setCloudId((String)this.getModel().getDataEntity().getDynamicObject("cloudid").getPkValue());
        }
        if (this.getModel().getDataEntity().getDynamicObject("anobjid") != null) {
            reportBillHeadInfo.setAnObjId((Long)this.getModel().getDataEntity().getDynamicObject("anobjid").getPkValue());
        }
        reportBillHeadInfo.setDescription(LocaleStringUtils.getLocaleString((Object)this.getModel().getValue("description")));
        reportBillHeadInfo.setFileSourceTableName(this.getPageCache().get(KEY_FILE_SOURCE_TABLE_NAME));
        return reportBillHeadInfo;
    }

    public void afterLoadData(EventObject evt) {
        super.afterLoadData(evt);
        this.getPageCache().put(KEY_FILE_SOURCE_TABLE_NAME, ReportFileSourceService.getInstance().getTableName((Long)this.getModel().getValue("id")));
        this.putPageCache("orgBillHeadInfo", this.getBillHead());
        this.loadRptManageInfo((Long)this.getModel().getValue("id"), false);
    }

    private void loadRptManageInfo(Long rptManageId, boolean isCopy) {
        long anObjId = ((DynamicObject)this.getModel().getValue("anobjid")).getLong("id");
        ReportManageConfigInfo reportManageConfigInfo = ReportManageUtil.genRptMngCfgInfo((Long)rptManageId, (long)anObjId);
        if (isCopy) {
            DynamicObject[] rptMackCntDys;
            DynamicObject rptMarkDy = ReportManageService.getReportMark((long)rptManageId);
            if (null != rptMarkDy && (rptMackCntDys = ReportManageService.getRptMarkContent((long)rptMarkDy.getLong("id"))) != null && rptMackCntDys.length > 0) {
                Map<String, String> contentMap = Arrays.stream(rptMackCntDys).collect(Collectors.toMap(dy -> dy.getString("locale"), dy -> dy.getString("markcontent")));
                ReportMarkInfo reportMarkInfo = new ReportMarkInfo(rptMarkDy.getString("show"), contentMap);
                this.putPageCache("reportMarkInfo", reportMarkInfo);
            }
            ReportCopyUtils.clearId((ReportManageConfigInfo)reportManageConfigInfo);
        }
        this.putPageCache(reportManageConfigInfo);
        this.putPageCache("oriReportManageConfigInfo", reportManageConfigInfo);
        this.putPageCache("oriAnObjDetailInfo", reportManageConfigInfo.getAssignObj());
    }

    public void afterBindData(EventObject evt) {
        super.afterBindData(evt);
        if (this.getView().getFormShowParameter().getStatus() == OperationStatus.EDIT && ((Boolean)this.getModel().getValue("issyspreset")).booleanValue()) {
            ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
        }
        ReportManageConfigInfo reportManageConfigInfo = (ReportManageConfigInfo)this.getPageCache("reportManageConfigInfo", ReportManageConfigInfo.class);
        OperationStatus status = (Boolean)this.getModel().getValue("issyspreset") != false ? OperationStatus.VIEW : this.getView().getFormShowParameter().getStatus();
        this.invokeControl(new InitCallBackInfo(status.toString(), reportManageConfigInfo));
        if (OperationStatus.VIEW == this.getView().getFormShowParameter().getStatus()) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"baritemap1"});
        } else {
            this.getView().setVisible(Boolean.valueOf(true), new String[]{"baritemap1"});
            Boolean[] publishStatue = HRReportPublishMenuService.getPublishStatus((Object)this.getModel().getDataEntity().getPkValue());
            this.showPublishOrOffline(publishStatue[0], publishStatue[1], publishStatue[2]);
        }
    }

    private void showPublishOrOffline(Boolean publishApp, Boolean publishRpt, Boolean publishWork) {
        this.getRptPublishService().showPublishOrOffline(publishApp, publishRpt, publishWork);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate operate = (AbstractOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        if ("saveandvalidate".equals(operateKey)) {
            OperateOption operateOption = OperateOption.create();
            ReportManageConfigInfo oriRptMngCfgInfo = (ReportManageConfigInfo)this.getPageCache("oriReportManageConfigInfo", ReportManageConfigInfo.class, new ReportManageConfigInfo());
            ReportManageConfigInfo newRptMngCfgInfo = (ReportManageConfigInfo)this.getPageCache("reportManageConfigInfo", ReportManageConfigInfo.class, new ReportManageConfigInfo());
            if (this.getRptPublishService().isPublish()) {
                HashMap result = new HashMap(2);
                DisplaySchemeUtil.checkDpySchChange((ReportManageConfigInfo)oriRptMngCfgInfo, (ReportManageConfigInfo)newRptMngCfgInfo, result);
                if (StringUtils.isNotEmpty((CharSequence)((CharSequence)result.get("msg")))) {
                    this.getView().showConfirm((String)result.get("msg"), MessageBoxOptions.OKCancel, new ConfirmCallBackListener("dispScmChange", (IFormPlugin)this));
                    args.setCancel(true);
                    return;
                }
            }
            if (!ReportJumpConfigService.getInstance().validate(newRptMngCfgInfo, this.getView())) {
                return;
            }
            this.getView().invokeOperation("save", operateOption);
        } else if ("save".equals(operateKey)) {
            if (this.getRptPublishService().isPublish()) {
                operate.getOption().setVariableValue("dispScmPublish", "true");
            }
            ReportManageConfigInfo oriRptMngCfgInfo = (ReportManageConfigInfo)this.getPageCache("oriReportManageConfigInfo", ReportManageConfigInfo.class, new ReportManageConfigInfo());
            ReportManageConfigInfo newRptMngCfgInfo = (ReportManageConfigInfo)this.getPageCache("reportManageConfigInfo", ReportManageConfigInfo.class, new ReportManageConfigInfo());
            HashMap result = new HashMap(2);
            DisplaySchemeUtil.checkDpySchChange((ReportManageConfigInfo)oriRptMngCfgInfo, (ReportManageConfigInfo)newRptMngCfgInfo, result);
            if ("true".equals(result.get("isChange"))) {
                operate.getOption().setVariableValue("dispScmChange", "true");
            }
            this.getModel().setValue("datafilter", (Object)newRptMngCfgInfo.getDataFilter());
            this.getModel().setValue("drillingdrl", (Object)SerializationUtils.toJsonString((Object)newRptMngCfgInfo.getDrillingDrl()));
            if (!this.getRptPublishService().checkGenMetaData(args)) {
                return;
            }
            operate.getOption().setVariableValue("oriReportManageConfigInfo", this.getPageCache("oriReportManageConfigInfo"));
            operate.getOption().setVariableValue("oriAnObjDetailInfo", this.getPageCache("oriAnObjDetailInfo"));
            operate.getOption().setVariableValue("orgBillHeadInfo", this.getPageCache("orgBillHeadInfo"));
            operate.getOption().setVariableValue("reportManageConfigInfo", this.getPageCache("reportManageConfigInfo"));
            operate.getOption().setVariableValue("reportMarkInfo", this.getPageCache("reportMarkInfo"));
            PreIndexReportPageHandler preIndexHandler = new PreIndexReportPageHandler((AbstractFormPlugin)this);
            operate.getOption().setVariableValue("rptPreIndex", SerializationUtils.toJsonString((Object)preIndexHandler.getPresetIndexByPageCache()));
        } else if ("newreport".equals(operateKey)) {
            args.setCancel(true);
            ReportManagePopUtil.openEditReportPop((AbstractFormPlugin)this, (ReportBillHeadInfo)this.getBillHead());
        } else if ("preview".equals(operateKey)) {
            args.setCancel(true);
            ReportManageConfigInfo reportManageConfigInfo = (ReportManageConfigInfo)this.getPageCache("reportManageConfigInfo", ReportManageConfigInfo.class);
            if (!ReportJumpConfigService.getInstance().validate(reportManageConfigInfo, this.getView())) {
                return;
            }
            List workRptInfoList = reportManageConfigInfo.getWorkRpt();
            if (!CollectionUtils.isEmpty((Collection)workRptInfoList)) {
                workRptInfoList.forEach(workRptInfo -> workRptInfo.getReportConfig().getHf().clear());
            }
            ReportManagePopUtil.openPreViewPage((AbstractFormPlugin)this, (String)SerializationUtils.toJsonString((Object)reportManageConfigInfo), (String)this.getPageCache("reportMarkInfo"));
        }
        this.getRptPublishService().beforeDoOperation(args);
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        if ("save".equals(operateKey)) {
            this.getModel().setDataChanged(false);
            if (args.getOperationResult().isSuccess()) {
                block24: {
                    RptDisplaySchemeInfo rptDisplaySchemeInfo;
                    ReportManageConfigInfo reportManageConfigInfo = (ReportManageConfigInfo)this.getPageCache("reportManageConfigInfo", ReportManageConfigInfo.class, new ReportManageConfigInfo());
                    if (IDStringUtils.idEmpty((Long)reportManageConfigInfo.getId())) {
                        reportManageConfigInfo.setId(Long.valueOf(this.getModel().getDataEntity().getLong("id")));
                    }
                    if (null != (rptDisplaySchemeInfo = reportManageConfigInfo.getReportConfig().getRptDisplaySchemeInfo()) && CollectionUtils.isNotEmpty((Collection)rptDisplaySchemeInfo.getChildren())) {
                        RptDisplaySchemeService.updateDisplaySchemeId((RptDisplaySchemeInfo)rptDisplaySchemeInfo);
                    }
                    this.putPageCache("oriReportManageConfigInfo", reportManageConfigInfo);
                    this.putPageCache("reportManageConfigInfo", reportManageConfigInfo);
                    this.putPageCache("orgBillHeadInfo", SerializationUtils.toJsonString((Object)this.getBillHead()));
                    this.putPageCache("isModify", "false");
                    this.getRptPublishService().save(args);
                    try (TXHandle txHandle = TX.required();){
                        try {
                            List fieldList = ReportQuerySchemeUtils.getInstance().getFieldList(reportManageConfigInfo, null, null);
                            String pageCache = this.getPageCache("rpt_changetype");
                            if (HRStringUtils.equals((String)pageCache, (String)"true")) {
                                this.getPageCache().remove("rpt_changetype");
                                ReportPreViewService.saveSchemeChangeChg((Long)reportManageConfigInfo.getId());
                                ReportPreViewService.deleteSchemeByManageId((long)reportManageConfigInfo.getId());
                                break block24;
                            }
                            ReportPreViewService.synSchemeByReportSave((Long)reportManageConfigInfo.getId(), (List)fieldList);
                        }
                        catch (Exception e1) {
                            txHandle.markRollback();
                            LOGGER.error("synSchemeByReportSave", (Throwable)e1);
                        }
                    }
                }
                ReportDataStoreServiceHelper.stopSyn((Long)this.getModel().getDataEntity().getLong("id"));
            } else {
                if (OperationStatus.ADDNEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
                    this.getView().setVisible(Boolean.FALSE, new String[]{"bar_delete"});
                }
                if (operate.getOption().tryGetVariableValue("isshowmessage", new RefObject())) {
                    throw new KDBizException(args.getOperationResult().getMessage());
                }
            }
        } else if ("delete".equals(operateKey)) {
            this.getView().getFormShowParameter().setCustomParam("isConfirm", (Object)Boolean.TRUE);
        }
        this.getRptPublishService().afterDoOperation(args);
    }

    public void customEvent(CustomEventArgs ceas) {
        super.customEvent(ceas);
        try {
            this.getRptOptService().customEvent(ceas);
        }
        catch (Exception e) {
            LOGGER.error("customEvent", (Throwable)e);
            this.getView().hideLoading();
            this.getView().showErrorNotification(e.getMessage());
        }
    }

    public void closedCallBack(ClosedCallBackEvent callBackEvent) {
        super.closedCallBack(callBackEvent);
        String actionId = callBackEvent.getActionId();
        if (actionId.equals("editreport")) {
            this.checkDataChange((ReportBillHeadInfo)callBackEvent.getReturnData());
            return;
        }
        this.getRptCallBackService().closedCallBack(callBackEvent);
        this.getRptPublishService().closedCallBack(callBackEvent);
    }

    public void beforeClosed(BeforeClosedEvent beforeClosedEvent) {
        Boolean isConfirm = (Boolean)this.getView().getFormShowParameter().getCustomParam("isConfirm");
        if (isConfirm == null || !isConfirm.booleanValue()) {
            String curReportManage = this.getPageCache("closeReportManageConfigInfo");
            String oriReportManage = this.getPageCache("oriReportManageConfigInfo");
            String isModify = this.getPageCache("isModify");
            String curBillHead = SerializationUtils.toJsonString((Object)this.getBillHead());
            String oriBillHead = this.getPageCache("orgBillHeadInfo");
            if (!oriReportManage.equals(curReportManage) || !oriBillHead.equals(curBillHead) || Boolean.parseBoolean(isModify)) {
                String msg = MessageFormat.format(ResManager.loadKDString((String)"\u68c0\u6d4b\u5230\u60a8\u6709\u66f4\u6539\u5185\u5bb9\uff0c\u662f\u5426\u4e0d\u4fdd\u5b58\u76f4\u63a5\u9000\u51fa\uff1f{0}\u82e5\u4e0d\u4fdd\u5b58\uff0c\u5c06\u4e22\u5931\u8fd9\u4e9b\u66f4\u6539\u3002", (String)"ReportManageEditPlugin_2", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), System.lineSeparator());
                HashMap<Integer, String> btnNameMaps = new HashMap<Integer, String>(16);
                btnNameMaps.put(MessageBoxResult.Cancel.getValue(), ResManager.loadKDString((String)"\u8fd4\u56de\u7f16\u8f91", (String)"ReportManageEditPlugin_11", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                btnNameMaps.put(MessageBoxResult.Yes.getValue(), ResManager.loadKDString((String)"\u76f4\u63a5\u9000\u51fa", (String)"ReportManageEditPlugin_12", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]));
                this.getView().showConfirm(msg, "", MessageBoxOptions.OKCancel, ConfirmTypes.Save, new ConfirmCallBackListener("close_page", (IFormPlugin)this), btnNameMaps);
                beforeClosedEvent.setCancel(true);
            }
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        String callBackId = evt.getCallBackId();
        MessageBoxResult result = evt.getResult();
        if (result == MessageBoxResult.Yes && "close_page".equals(callBackId)) {
            this.getView().getFormShowParameter().setCustomParam("isConfirm", (Object)Boolean.TRUE);
            this.getView().close();
        } else if (callBackId.startsWith("isChangeChartType") && result == MessageBoxResult.Yes) {
            ReportConfigService reportConfigService = new ReportConfigService((AbstractFormPlugin)this, this.getRptCacheService());
            reportConfigService.changePageChartType(callBackId.substring(callBackId.indexOf(95) + 1));
            this.putPageCache("rpt_changetype", "true");
        } else if (result == MessageBoxResult.Yes && HRStringUtils.equals((String)"dispScmChange", (String)callBackId)) {
            OperateOption operateOption = OperateOption.create();
            operateOption.setVariableValue("dispScmChange", "true");
            this.getView().invokeOperation("save", operateOption);
        }
        this.getRptPublishService().confirmCallBack(evt);
    }

    private void clearPageCache() {
        this.getPageCache().batchRemove(CLEAR_CACHE_KEY);
    }

    @ExcludeFromJacocoGeneratedReport
    protected List<String> getUnCheckField() {
        List uncheckFieldList = super.getUnCheckField();
        uncheckFieldList.add("name");
        uncheckFieldList.add("description");
        uncheckFieldList.add("anobjid");
        uncheckFieldList.add("cloudid");
        uncheckFieldList.add("row");
        uncheckFieldList.add("column");
        uncheckFieldList.add("drillingdrl");
        return uncheckFieldList;
    }

    private void checkDataChange(ReportBillHeadInfo reportBillHeadInfo) {
        if (this.isBillHeadChange(reportBillHeadInfo)) {
            Long anObjId = this.getModel().getDataEntity().getLong("anobjid.id");
            this.updateBillHead(reportBillHeadInfo);
            if (!ObjectUtils.nullSafeEquals((Object)anObjId, (Object)reportBillHeadInfo.getAnObjId())) {
                ReportManageConfigInfo reportManageConfigInfo = this.genReportManageConfigInfo();
                this.invokeControl(new InitCallBackInfo(this.getView().getFormShowParameter().getStatus().toString(), reportManageConfigInfo));
            }
        }
    }

    private boolean isBillHeadChange(ReportBillHeadInfo reportBillHeadInfo) {
        if (null == reportBillHeadInfo) {
            return false;
        }
        long createId = this.getModel().getDataEntity().getLong("createorg.id");
        String number = this.getModel().getDataEntity().getString("number");
        String name = this.getModel().getDataEntity().getString("name");
        String cloudId = this.getModel().getDataEntity().getString("cloudid.id");
        Long anObjId = this.getModel().getDataEntity().getLong("anobjid.id");
        String description = this.getModel().getDataEntity().getString("description");
        LocaleString localeName = reportBillHeadInfo.getName();
        LocaleString localeDescription = reportBillHeadInfo.getDescription();
        String newNumber = reportBillHeadInfo.getNumber();
        if (null != localeName && !StringUtils.equals((CharSequence)name, (CharSequence)localeName.getLocaleValue())) {
            return true;
        }
        if (HRStringUtils.isNotEmpty((String)newNumber) && !HRStringUtils.equals((String)newNumber, (String)number)) {
            return true;
        }
        if (null != localeDescription && !StringUtils.equals((CharSequence)description, (CharSequence)localeDescription.getLocaleValue())) {
            return true;
        }
        if (!ObjectUtils.nullSafeEquals((Object)anObjId, (Object)reportBillHeadInfo.getAnObjId())) {
            return true;
        }
        if (!StringUtils.equals((CharSequence)cloudId, (CharSequence)reportBillHeadInfo.getCloudId())) {
            return true;
        }
        return !Objects.equals(createId, reportBillHeadInfo.getCreateOrgId());
    }

    static {
        CLEAR_CACHE_KEY.add("anObjDetailInfo");
        CLEAR_CACHE_KEY.add("workRptInfo");
        CLEAR_CACHE_KEY.add("dataFilterCondition");
        CLEAR_CACHE_KEY.add("drillingDrl");
        CLEAR_CACHE_KEY.add("filter");
        CLEAR_CACHE_KEY.add("virtualConfigInfo");
        CLEAR_CACHE_KEY.add("reportMarkInfo");
        CLEAR_CACHE_KEY.add("reportInfo");
    }
}
