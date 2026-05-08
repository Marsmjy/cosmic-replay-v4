/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  com.google.common.collect.Sets$SetView
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.DB
 *  kd.bos.db.DBRoute
 *  kd.bos.db.tx.TX
 *  kd.bos.devportal.util.MetaDataUtil
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.bos.ext.hr.ruleengine.utils.IDStringUtils
 *  kd.bos.kdtx.common.CommonParam
 *  kd.bos.kdtx.common.Param
 *  kd.bos.kdtx.sdk.session.DTX
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.threads.ThreadPools
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrptmc.business.anobj.AnalyseObjectService
 *  kd.hr.hrptmc.business.calfield.CalculateFieldService
 *  kd.hr.hrptmc.business.filesource.ReportFileSourceService
 *  kd.hr.hrptmc.business.repdesign.ReportManageService
 *  kd.hr.hrptmc.business.repdesign.ReportPermissionService
 *  kd.hr.hrptmc.business.repdesign.RptDisplaySchemeService
 *  kd.hr.hrptmc.business.repdesign.RptReferenceLineService
 *  kd.hr.hrptmc.business.repdesign.UserDisplaySchemeService
 *  kd.hr.hrptmc.business.repdesign.datastore.ReportDataStoreServiceHelper
 *  kd.hr.hrptmc.business.repdesign.hisversion.ReportHisVersionService
 *  kd.hr.hrptmc.business.repdesign.info.AnObjDetailInfo
 *  kd.hr.hrptmc.business.repdesign.info.AnObjFieldInfo
 *  kd.hr.hrptmc.business.repdesign.info.DataFormatInfo
 *  kd.hr.hrptmc.business.repdesign.info.DimensionFieldInfo
 *  kd.hr.hrptmc.business.repdesign.info.FieldInfo
 *  kd.hr.hrptmc.business.repdesign.info.IndexFieldInfo
 *  kd.hr.hrptmc.business.repdesign.info.ReportBillHeadInfo
 *  kd.hr.hrptmc.business.repdesign.info.ReportConfigInfo
 *  kd.hr.hrptmc.business.repdesign.info.ReportManageConfigInfo
 *  kd.hr.hrptmc.business.repdesign.info.ReportMarkInfo
 *  kd.hr.hrptmc.business.repdesign.info.RowFieldInfo
 *  kd.hr.hrptmc.business.repdesign.info.RptDisplaySchemeEtyInfo
 *  kd.hr.hrptmc.business.repdesign.info.RptDisplaySchemeInfo
 *  kd.hr.hrptmc.business.repdesign.info.SubtotalInfo
 *  kd.hr.hrptmc.business.repdesign.info.WorkRptInfo
 *  kd.hr.hrptmc.business.repdesign.jump.ReportJumpConfigService
 *  kd.hr.hrptmc.business.repdesign.sort.RptSortService
 *  kd.hr.hrptmc.business.task.TaskExecuteUtil
 *  kd.hr.hrptmc.common.constant.anobj.AnalyseObjectConstants
 *  kd.hr.hrptmc.common.constant.repdesign.ReportManageConstants
 *  kd.hr.hrptmc.common.constant.repdesign.filter.FilterType
 *  kd.hr.hrptmc.common.model.anobj.QueryFieldBo
 *  kd.hr.hrptmc.common.model.calfield.CalculateFieldBo
 *  kd.hr.hrptmc.common.model.preindex.DimMapBo
 *  kd.hr.hrptmc.common.model.preindex.DimMapEntryBo
 *  kd.hr.hrptmc.common.model.repdesign.filter.FilterBo
 *  kd.hr.hrptmc.common.model.repdesign.total.ReportTotalColConfigBo
 *  kd.hr.hrptmc.opplugin.validator.repdesign.ReportValidator
 *  kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp$SortParam
 *  org.apache.commons.collections.CollectionUtils
 */
package kd.hr.hrptmc.opplugin.web.repdesign;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.DB;
import kd.bos.db.DBRoute;
import kd.bos.db.tx.TX;
import kd.bos.devportal.util.MetaDataUtil;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.bos.ext.hr.ruleengine.utils.IDStringUtils;
import kd.bos.kdtx.common.CommonParam;
import kd.bos.kdtx.common.Param;
import kd.bos.kdtx.sdk.session.DTX;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.threads.ThreadPools;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrptmc.business.anobj.AnalyseObjectService;
import kd.hr.hrptmc.business.calfield.CalculateFieldService;
import kd.hr.hrptmc.business.filesource.ReportFileSourceService;
import kd.hr.hrptmc.business.repdesign.ReportManageService;
import kd.hr.hrptmc.business.repdesign.ReportPermissionService;
import kd.hr.hrptmc.business.repdesign.RptDisplaySchemeService;
import kd.hr.hrptmc.business.repdesign.RptReferenceLineService;
import kd.hr.hrptmc.business.repdesign.UserDisplaySchemeService;
import kd.hr.hrptmc.business.repdesign.datastore.ReportDataStoreServiceHelper;
import kd.hr.hrptmc.business.repdesign.hisversion.ReportHisVersionService;
import kd.hr.hrptmc.business.repdesign.info.AnObjDetailInfo;
import kd.hr.hrptmc.business.repdesign.info.AnObjFieldInfo;
import kd.hr.hrptmc.business.repdesign.info.DataFormatInfo;
import kd.hr.hrptmc.business.repdesign.info.DimensionFieldInfo;
import kd.hr.hrptmc.business.repdesign.info.FieldInfo;
import kd.hr.hrptmc.business.repdesign.info.IndexFieldInfo;
import kd.hr.hrptmc.business.repdesign.info.ReportBillHeadInfo;
import kd.hr.hrptmc.business.repdesign.info.ReportConfigInfo;
import kd.hr.hrptmc.business.repdesign.info.ReportManageConfigInfo;
import kd.hr.hrptmc.business.repdesign.info.ReportMarkInfo;
import kd.hr.hrptmc.business.repdesign.info.RowFieldInfo;
import kd.hr.hrptmc.business.repdesign.info.RptDisplaySchemeEtyInfo;
import kd.hr.hrptmc.business.repdesign.info.RptDisplaySchemeInfo;
import kd.hr.hrptmc.business.repdesign.info.SubtotalInfo;
import kd.hr.hrptmc.business.repdesign.info.WorkRptInfo;
import kd.hr.hrptmc.business.repdesign.jump.ReportJumpConfigService;
import kd.hr.hrptmc.business.repdesign.sort.RptSortService;
import kd.hr.hrptmc.business.task.TaskExecuteUtil;
import kd.hr.hrptmc.common.constant.anobj.AnalyseObjectConstants;
import kd.hr.hrptmc.common.constant.repdesign.ReportManageConstants;
import kd.hr.hrptmc.common.constant.repdesign.filter.FilterType;
import kd.hr.hrptmc.common.model.anobj.QueryFieldBo;
import kd.hr.hrptmc.common.model.calfield.CalculateFieldBo;
import kd.hr.hrptmc.common.model.preindex.DimMapBo;
import kd.hr.hrptmc.common.model.preindex.DimMapEntryBo;
import kd.hr.hrptmc.common.model.repdesign.filter.FilterBo;
import kd.hr.hrptmc.common.model.repdesign.total.ReportTotalColConfigBo;
import kd.hr.hrptmc.opplugin.validator.repdesign.ReportValidator;
import kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp;

public final class ReportManageOp
extends HRDataBaseOp
implements AnalyseObjectConstants,
ReportManageConstants {
    private static final Log LOGGER = LogFactory.getLog(ReportManageOp.class);
    HRBaseServiceHelper rptConfigHelper = new HRBaseServiceHelper("hrptmc_reportconfig");
    HRBaseServiceHelper splitDateHelper = new HRBaseServiceHelper("hrptmc_splitdate");
    HRBaseServiceHelper workRptHelper = new HRBaseServiceHelper("hrptmc_workreport");

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("id");
        fieldKeys.add("number");
        fieldKeys.add("name");
        fieldKeys.add("row");
        fieldKeys.add("column");
        fieldKeys.add("publishstatus");
        fieldKeys.add("anobjid");
        fieldKeys.add("publishrptstatus");
        fieldKeys.add("publishworkstatus");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new ReportValidator());
    }

    /*
     * Unable to fully structure code
     */
    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        var2_2 = args.getOperationKey();
        var3_3 = -1;
        switch (var2_2.hashCode()) {
            case 3522941: {
                if (!var2_2.equals("save")) break;
                var3_3 = 0;
                break;
            }
            case -1335458389: {
                if (!var2_2.equals("delete")) break;
                var3_3 = 1;
            }
        }
        switch (var3_3) {
            case 0: {
                required = TX.required();
                var5_6 = null;
                try {
                    this.saveReport(args);
                }
                catch (Exception e) {
                    required.markRollback();
                    ReportManageOp.LOGGER.info("save error!", (Object)e);
                    throw new KDBizException(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25", (String)"ReportManageOp_1", (String)"hrmp-hrptmc-opplugin", (Object[])new Object[0]));
                }
                if (required == null) break;
                if (var5_6 == null) ** GOTO lbl31
                try {
                    required.close();
                }
                catch (Throwable e) {
                    var5_6.addSuppressed(e);
                }
                break;
lbl31:
                // 1 sources

                required.close();
                break;
                catch (Throwable e) {
                    try {
                        var5_6 = e;
                        throw e;
                    }
                    catch (Throwable var7_15) {
                        if (required != null) {
                            if (var5_6 != null) {
                                try {
                                    required.close();
                                }
                                catch (Throwable var8_17) {
                                    var5_6.addSuppressed(var8_17);
                                }
                            } else {
                                required.close();
                            }
                        }
                        throw var7_15;
                    }
                }
            }
            case 1: {
                required = DTX.requiresNew((String)"hrptmc_reportdelete", (DBRoute)new DBRoute("hr"));
                var5_7 = null;
                try {
                    delMetaDataNumbers = Lists.newArrayListWithExpectedSize((int)args.getDataEntities().length);
                    for (DynamicObject reportManageDy : args.getDataEntities()) {
                        this.deleteReport(reportManageDy, delMetaDataNumbers);
                    }
                    commonParam = new CommonParam();
                    commonParam.put((Object)"numbers", (Object)delMetaDataNumbers);
                    required.register("hrmp", "hrptmc", "HRPTMCRepDelECSService", (Param)commonParam);
                }
                catch (Exception e) {
                    required.markRollback();
                    ReportManageOp.LOGGER.info(ResManager.loadKDString((String)"\u5220\u9664\u5931\u8d25", (String)"ReportManageOp_2", (String)"hrmp-hrptmc-opplugin", (Object[])new Object[0]), (Object)e);
                    throw new KDBizException(ResManager.loadKDString((String)"\u5220\u9664\u5931\u8d25", (String)"ReportManageOp_2", (String)"hrmp-hrptmc-opplugin", (Object[])new Object[0]));
                }
                if (required == null) break;
                if (var5_7 == null) ** GOTO lbl73
                try {
                    required.close();
                }
                catch (Throwable var6_13) {
                    var5_7.addSuppressed(var6_13);
                }
                break;
lbl73:
                // 1 sources

                required.close();
                break;
                catch (Throwable var6_14) {
                    try {
                        var5_7 = var6_14;
                        throw var6_14;
                    }
                    catch (Throwable var11_21) {
                        if (required != null) {
                            if (var5_7 != null) {
                                try {
                                    required.close();
                                }
                                catch (Throwable var12_22) {
                                    var5_7.addSuppressed(var12_22);
                                }
                            } else {
                                required.close();
                            }
                        }
                        throw var11_21;
                    }
                }
            }
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        String operationKey = args.getOperationKey();
        if (HRStringUtils.equals((String)operationKey, (String)"save")) {
            String tableName = this.operateOption.getVariableValue("fileSourceTableName", null);
            ReportFileSourceService.getInstance().clearFileSourceDataForReportSave(tableName, args.getDataEntities()[0]);
        } else if (HRStringUtils.equals((String)operationKey, (String)"delete")) {
            Arrays.stream(args.getDataEntities()).map(reportDy -> reportDy.getDynamicObject("anobjid").getLong("id")).forEach(anObjId -> ReportFileSourceService.getInstance().dropTableAndClearData(anObjId));
        }
    }

    private void deleteReport(DynamicObject reportManageDy, List<String> delMetaDataNumbers) {
        long reportManageId = reportManageDy.getLong("id");
        ReportDataStoreServiceHelper.deleteReportSchedule((long)reportManageId);
        ReportManageService.deleteRows((Long)reportManageId);
        ReportManageService.deleteCols((Long)reportManageId);
        ReportManageService.deleteWorkRpt((Long)reportManageId);
        List rptConfigIds = ReportManageService.getRptConfigId((Long)reportManageId);
        ReportManageService.deleteRptConfig((Long)reportManageId);
        ReportManageService.deleteCalcField((Long)reportManageId);
        ReportManageService.deletePreIdxRef((Long)reportManageId);
        ReportManageService.deleteDimMapRef((Long)reportManageId);
        ReportManageService.deleteFilter((Long)reportManageId);
        ReportManageService.deleteRptQueryConfig((Long)reportManageId);
        RptDisplaySchemeService.deleteDispScm((long)reportManageId);
        ReportManageService.deleteUserDispScm((long)reportManageId);
        ReportManageService.deleteUserDispScmChg((Long)reportManageId);
        ReportManageService.deleteCustomSort((long)reportManageId);
        ReportManageService.deleteRptComRef((long)reportManageId);
        ReportManageService.deleteReportMark((long)reportManageId);
        AnalyseObjectService.getInstance().removeRefByReportFieldAliasCache(Long.valueOf(reportManageDy.getDynamicObject("anobjid").getLong("id")));
        rptConfigIds.forEach(ReportManageService::deleteAlgorithmCol);
        ReportManageService.deleteSplitDate((long)reportManageId);
        ReportJumpConfigService.getInstance().deleteReportJumpConfig(reportManageId);
        RptReferenceLineService.getInstance().deleteRefLineConfig(reportManageId);
        String number = reportManageDy.getString("number");
        if (this.isMetaExist(number)) {
            delMetaDataNumbers.add(number);
        }
        ReportHisVersionService.getInstance().updateVersionWhenDeleteReport(reportManageId);
        ReportManageService.deleteReportCenter((long)reportManageId);
        ReportPermissionService.delRptCtrPerm((Long)reportManageId);
        ReportManageService.deleteStructProConfig((String)number);
    }

    private boolean isMetaExist(String metaNumber) {
        if (StringUtils.isEmpty((CharSequence)metaNumber)) {
            return false;
        }
        MetaDataUtil metaDataUtil = new MetaDataUtil();
        return metaDataUtil.checkNumber(metaNumber.toLowerCase());
    }

    private DynamicObject genWorkRptDy(long rptManageId, long workRptId, int i, int key, String rowStr, String colStr) {
        DynamicObject workRptDy = this.workRptHelper.generateEmptyDynamicObject();
        workRptDy.set("id", (Object)workRptId);
        workRptDy.set("index", (Object)i);
        workRptDy.set("key", (Object)key);
        workRptDy.set("rptmanage", (Object)rptManageId);
        workRptDy.set("row", (Object)rowStr);
        workRptDy.set("column", (Object)colStr);
        return workRptDy;
    }

    private void saveReport(BeginOperationTransactionArgs args) {
        DynamicObject reportManageDy = args.getDataEntities()[0];
        long reportManageId = reportManageDy.getLong("id");
        if (0L == reportManageId) {
            reportManageId = ORM.create().genLongId("hrptmc_reportmanage");
            reportManageDy.set("id", (Object)reportManageId);
        }
        ArrayList<SortParam> sortParamList = new ArrayList<SortParam>();
        String reportManageStr = this.getOption().getVariableValue("reportManageConfigInfo");
        if (StringUtils.isNotEmpty((CharSequence)reportManageStr)) {
            ReportManageConfigInfo reportManageConfigInfo = (ReportManageConfigInfo)SerializationUtils.fromJsonString((String)reportManageStr, ReportManageConfigInfo.class);
            List workRptInfos = reportManageConfigInfo.getWorkRpt();
            this.saveSplitDate(reportManageConfigInfo.getAssignObj(), reportManageId);
            DynamicObjectCollection workRptColl = new DynamicObjectCollection();
            for (int i = 0; i < workRptInfos.size(); ++i) {
                WorkRptInfo workRptInfo = (WorkRptInfo)workRptInfos.get(i);
                long workRptId = Long.parseLong(workRptInfo.getWorkRptId());
                List rows = workRptInfo.getRows();
                DynamicObjectCollection rowColls = this.saveRows(rows, reportManageId, workRptId);
                String rowStr = SerializationUtils.toJsonString((Object)rows);
                List columns = workRptInfo.getColumns();
                DynamicObjectCollection colColls = this.saveColumns(columns, reportManageId, workRptId);
                String colStr = SerializationUtils.toJsonString((Object)columns);
                workRptColl.add((Object)this.genWorkRptDy(reportManageId, workRptId, i, workRptInfo.getKey(), rowStr, colStr));
                if (i == 0) {
                    reportManageDy.set("row", (Object)rowStr);
                    reportManageDy.set("column", (Object)colStr);
                }
                this.saveDimMap(workRptInfo.getDimMaps(), reportManageId, workRptId);
                this.saveReportConfig(workRptInfo, reportManageId, workRptId, rowColls, colColls);
                sortParamList.add(new SortParam(workRptId, workRptInfo.getReportConfig().getLegend(), rows, columns));
            }
            this.deleteWorkRpt(reportManageId, workRptInfos);
            this.workRptHelper.deleteByFilter(new QFilter[]{new QFilter("rptmanage", "=", (Object)reportManageId)});
            this.workRptHelper.save(workRptColl);
            this.saveSort(sortParamList, reportManageId, reportManageConfigInfo.getAssignObj().getQueryFields());
            this.saveCalcIndex(reportManageId, reportManageConfigInfo.getAssignObj().getReportCalFields());
            this.saveFilter(reportManageId, reportManageConfigInfo.getFilter());
            this.savePreIndexRef(reportManageId);
            this.closeEnableScheme(reportManageId, workRptInfos.size() > 1);
            this.logReportConfigHisVersion(reportManageDy, reportManageConfigInfo);
        }
        this.saveReportMark(reportManageId);
        this.updateRptCtrPerm(reportManageId);
        AnalyseObjectService.getInstance().removeRefByReportFieldAliasCache(Long.valueOf(reportManageDy.getDynamicObject("anobjid").getLong("id")));
        String tableName = ReportFileSourceService.getInstance().getTableName(Long.valueOf(reportManageId));
        this.operateOption.setVariableValue("fileSourceTableName", tableName);
    }

    private void logReportConfigHisVersion(DynamicObject reportManageDy, ReportManageConfigInfo reportManageConfigInfo) {
        String originalManageInfoStr = this.getOption().getVariableValue("oriReportManageConfigInfo", null);
        ReportManageConfigInfo originalManageInfo = null;
        if (HRStringUtils.isNotEmpty((String)originalManageInfoStr)) {
            originalManageInfo = (ReportManageConfigInfo)SerializationUtils.fromJsonString((String)originalManageInfoStr, ReportManageConfigInfo.class);
            String originalAnObjDetailInfoStr = this.getOption().getVariableValue("oriAnObjDetailInfo", null);
            if (HRStringUtils.isNotEmpty((String)originalAnObjDetailInfoStr)) {
                AnObjDetailInfo originalAssignObj = (AnObjDetailInfo)SerializationUtils.fromJsonString((String)originalAnObjDetailInfoStr, AnObjDetailInfo.class);
                originalManageInfo.setAssignObj(originalAssignObj);
            }
        }
        String originalReportHeadInfoStr = this.getOption().getVariableValue("orgBillHeadInfo", null);
        ReportBillHeadInfo originalReportHeadInfo = null;
        if (HRStringUtils.isNotEmpty((String)originalReportHeadInfoStr)) {
            originalReportHeadInfo = (ReportBillHeadInfo)SerializationUtils.fromJsonString((String)originalReportHeadInfoStr, ReportBillHeadInfo.class);
        }
        ReportHisVersionService.getInstance().saveReportHisVersionConfigBo(reportManageDy, reportManageConfigInfo, originalManageInfo, originalReportHeadInfo);
    }

    private void updateRptCtrPerm(long rptManageId) {
        try {
            ReportPermissionService.updateRptCtrPerm((long)rptManageId);
        }
        catch (Exception e) {
            LOGGER.error("updateRptCtrPerm fail:", (Throwable)e);
        }
    }

    private void deleteWorkRpt(long reportManageId, List<WorkRptInfo> workRptInfos) {
        DynamicObject[] workRptDys = this.workRptHelper.queryOriginalArray("id", new QFilter[]{new QFilter("rptmanage", "=", (Object)reportManageId)});
        List<Long> oriWorkRptIds = Arrays.stream(workRptDys).map(dy -> dy.getLong("id")).collect(Collectors.toList());
        List newWorkRptIds = workRptInfos.stream().map(info -> Long.parseLong(info.getWorkRptId())).collect(Collectors.toList());
        oriWorkRptIds.removeAll(newWorkRptIds);
        if (!CollectionUtils.isEmpty(oriWorkRptIds)) {
            this.deleteWorkRpts(oriWorkRptIds);
        }
    }

    private void deleteWorkRpts(List<Long> workRptIds) {
        ReportManageService.deleteRowsByWorkRpts(workRptIds);
        ReportManageService.deleteColsByWorkRpts(workRptIds);
        ReportManageService.deleteDimMapByWorkRpts(workRptIds);
        ReportManageService.deleteRptCfgByWorkRpts(workRptIds);
        ReportManageService.deleteCustomSortByWorkRpts(workRptIds);
        ReportManageService.deleteComRefByWorkRpts(workRptIds);
    }

    private void saveSplitDate(AnObjDetailInfo anObjDetailInfo, long reportManageId) {
        Sets.SetView addSet;
        List dimensionList = anObjDetailInfo.getDimensionList();
        DynamicObject[] splitDateDys = ReportManageService.getSplitDate((long)reportManageId);
        Map<String, DynamicObject> oldSplitMap = Arrays.stream(splitDateDys).collect(Collectors.toMap(dy -> dy.getString("anobjfield.fieldpath") + "!" + dy.getString("number"), dy -> dy));
        Map<String, String> splitKeyList = dimensionList.stream().filter(DimensionFieldInfo::getSplitDateSub).collect(Collectors.toMap(AnObjFieldInfo::getFieldPath, AnObjFieldInfo::getFieldId));
        Sets.SetView deleteSet = Sets.difference(oldSplitMap.keySet(), splitKeyList.keySet());
        if (!CollectionUtils.isEmpty((Collection)deleteSet)) {
            List ids = Arrays.stream(splitDateDys).filter(arg_0 -> ReportManageOp.lambda$saveSplitDate$6((Set)deleteSet, arg_0)).map(dy -> dy.getLong("id")).collect(Collectors.toList());
            ReportManageService.deleteSplitDate(ids);
        }
        if (!CollectionUtils.isEmpty((Collection)(addSet = Sets.difference(splitKeyList.keySet(), oldSplitMap.keySet())))) {
            DynamicObjectCollection coll = new DynamicObjectCollection();
            for (String key : addSet) {
                DynamicObject dy2 = this.splitDateHelper.generateEmptyDynamicObject();
                dy2.set("number", (Object)key.split("!")[1]);
                dy2.set("anobjfield", (Object)splitKeyList.get(key));
                dy2.set("rptmanage", (Object)reportManageId);
                coll.add((Object)dy2);
            }
            ReportManageService.saveSplitDate((DynamicObjectCollection)coll);
        }
    }

    private boolean isVirtualEntity() {
        ReportManageConfigInfo reportManageConfigInfo;
        String reportManageStr = this.getOption().getVariableValue("reportManageConfigInfo");
        if (StringUtils.isNotEmpty((CharSequence)reportManageStr) && null != (reportManageConfigInfo = (ReportManageConfigInfo)SerializationUtils.fromJsonString((String)reportManageStr, ReportManageConfigInfo.class)).getAssignObj()) {
            return reportManageConfigInfo.getAssignObj().getVirtualEntity();
        }
        return false;
    }

    public static void recursive(List<FieldInfo> fields, Consumer<FieldInfo> consumer) {
        if (kd.bos.util.CollectionUtils.isEmpty(fields)) {
            return;
        }
        for (FieldInfo children : fields) {
            consumer.accept(children);
            if (!kd.bos.util.CollectionUtils.isNotEmpty((Collection)children.getFields())) continue;
            ReportManageOp.recursive(children.getFields(), consumer);
        }
    }

    private void saveSort(List<SortParam> sortParamList, long reportManageId, List<QueryFieldBo> queryFields) {
        ThreadPools.executeOnce((String)"reportSaveSortThread", () -> {
            for (SortParam sortParam : sortParamList) {
                RptSortService.saveSort((List)sortParam.getLegend(), (List)sortParam.getRows(), (List)sortParam.getColumns(), (long)reportManageId, (long)sortParam.getWorkRptId(), (List)queryFields);
            }
            try {
                if (this.isVirtualEntity()) {
                    TaskExecuteUtil.syncVirtualCustomSortData((Long)reportManageId);
                    TaskExecuteUtil.syncVirtualCommonSortData((Long)reportManageId);
                } else {
                    TaskExecuteUtil.syncCustomSortData((Long)reportManageId);
                    TaskExecuteUtil.syncCommonSortData((Long)reportManageId);
                }
            }
            catch (Exception e) {
                LOGGER.error("syncCustomSortData fail:", (Throwable)e);
            }
        });
    }

    private void saveFilter(long reportManageId, List<FilterBo> filterBos) {
        DynamicObject[] queryFilters = ReportManageService.queryFilter((Long)reportManageId);
        ArrayList longList = Lists.newArrayListWithExpectedSize((int)6);
        DynamicObject[] splitDates = ReportManageService.getSplitDate((long)reportManageId);
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrptmc_filter");
        DynamicObjectCollection filterColl = new DynamicObjectCollection();
        long[] colIds = ORM.create().genLongIds("hrptmc_colfield", filterBos.size());
        for (int i = 0; i < filterBos.size(); ++i) {
            FilterBo filterBo = filterBos.get(i);
            DynamicObject colDy = helper.generateEmptyDynamicObject();
            String fieldPath = filterBo.getFieldPath();
            if (HRStringUtils.isNotEmpty((String)fieldPath) && fieldPath.contains("!")) {
                String[] stringArray = fieldPath.split("!");
                String type = stringArray[1];
                for (DynamicObject splitDate : splitDates) {
                    String number = splitDate.getString("number");
                    String anObjField = splitDate.getString("anobjfield.id");
                    if (!HRStringUtils.equals((String)filterBo.getFieldId(), (String)anObjField) || !HRStringUtils.equals((String)number, (String)type)) continue;
                    colDy.set("splitdate", splitDate.get("id"));
                    break;
                }
            }
            for (DynamicObject queryFilter : queryFilters) {
                long anoBjFieldId = queryFilter.getLong("anobjfield_id");
                long splitDateId = queryFilter.getLong("splitdate_id");
                if (HRStringUtils.isNotEmpty((String)fieldPath) && fieldPath.contains("!")) {
                    long id = colDy.getLong("splitdate");
                    if (id != splitDateId) continue;
                    colDy = queryFilter;
                    longList.add(queryFilter.getLong("id"));
                    break;
                }
                if (!HRStringUtils.equals((String)String.valueOf(anoBjFieldId), (String)filterBo.getFieldId()) || splitDateId != 0L) continue;
                colDy = queryFilter;
                longList.add(queryFilter.getLong("id"));
                break;
            }
            if (colDy.getLong("id") == 0L) {
                colDy.set("id", (Object)colIds[i]);
            }
            colDy.set("ismust", (Object)(filterBo.getMustInput() ? "1" : "0"));
            colDy.set("basedataismul", (Object)(filterBo.getBaseDataMul() ? "1" : "0"));
            colDy.set("datescope", (Object)filterBo.getDateScope());
            colDy.set("quickscope", (Object)filterBo.getQuickScope());
            colDy.set("datetype", (Object)filterBo.getDateType());
            colDy.set("enable", (Object)(filterBo.getEnable() ? "1" : "0"));
            colDy.set("filtertype", (Object)filterBo.getFilterType());
            colDy.set("orglevel", (Object)(filterBo.getOrgLevel() ? "1" : "0"));
            colDy.set("rptmanage", (Object)reportManageId);
            colDy.set("suborg", (Object)filterBo.getContainSub());
            colDy.set("index", (Object)filterBo.getIndex());
            colDy.set("name", (Object)filterBo.getFilterAlias());
            colDy.set("anobjfield", (Object)this.parseFieldId(filterBo.getFieldId()));
            colDy.set("groupdate", (Object)(filterBo.getGroupDate() ? "1" : "0"));
            colDy.set("begindate", (Object)filterBo.getBeginDateId());
            colDy.set("enddate", (Object)filterBo.getEndDateId());
            colDy.set("mulgroupdate", (Object)SerializationUtils.toJsonString((Object)filterBo.getMulGroupDate()));
            colDy.set("textdefaultvalue", (Object)filterBo.getTextDefaultValue());
            colDy.set("textfilterrange", (Object)filterBo.getTextFilterRange());
            colDy.set("datefiltertype", (Object)filterBo.getDateFilterType());
            colDy.set("datefiltertext", (Object)filterBo.getDateFilterText());
            try {
                String string = filterBo.getFilterStartDateStr();
                String filterEndDateStr = filterBo.getFilterEndDateStr();
                if (HRStringUtils.isNotEmpty((String)string)) {
                    Date filterStartDate = HRDateTimeUtils.parseDate((String)string);
                    colDy.set("filterstartdate", (Object)filterStartDate);
                }
                if (HRStringUtils.isNotEmpty((String)filterEndDateStr)) {
                    Date filterEndDate = HRDateTimeUtils.parseDate((String)filterEndDateStr);
                    colDy.set("filterenddate", (Object)filterEndDate);
                }
            }
            catch (ParseException parseException) {
                LOGGER.error((Throwable)parseException);
            }
            colDy.set("bddefaultvalue", (Object)filterBo.getBdDefaultValue());
            colDy.set("bdfilterrange", (Object)filterBo.getBdFilterRange());
            colDy.set("groupfield", (Object)filterBo.getGroupFieldId());
            colDy.set("isgroupfield", (Object)filterBo.getGroupField());
            colDy.set("opt", (Object)filterBo.getOpt());
            if (HRStringUtils.equals((String)filterBo.getFilterType(), (String)FilterType.STRING.getValue())) {
                colDy.set("searchmode", (Object)filterBo.getSearchMode());
            }
            if (HRStringUtils.equals((String)filterBo.getFilterType(), (String)FilterType.BOOLEAN.getValue())) {
                colDy.set("truevalue", (Object)filterBo.getBooleanTrueValue());
                colDy.set("falsevalue", (Object)filterBo.getBooleanFalseValue());
                colDy.set("booleandefval", (Object)filterBo.getBooleanDefaultValue());
            }
            filterColl.add((Object)colDy);
        }
        ReportManageService.deleteFilterByQFilters((Long)reportManageId, (List)longList);
        helper.save(filterColl);
    }

    private void saveCalcIndex(long reportManageId, List<CalculateFieldBo> calculateFieldBos) {
        CalculateFieldService.getInstance().saveCalculateFieldForReport(true, Long.valueOf(reportManageId), calculateFieldBos);
    }

    private void saveReportConfig(WorkRptInfo workRptInfo, long reportManageId, long workRptId, DynamicObjectCollection rowColls, DynamicObjectCollection colColls) {
        ReportConfigInfo reportConfigInfo = workRptInfo.getReportConfig();
        DynamicObject reportConfigDy = ReportManageService.getRptConfigNotNull((Long)workRptId);
        reportConfigDy.set("type", (Object)reportConfigInfo.getType());
        reportConfigDy.set("total", (Object)reportConfigInfo.getTotal());
        reportConfigDy.set("totalname", (Object)reportConfigInfo.getTotalName());
        SubtotalInfo subtotalInfo = reportConfigInfo.getSubtotal();
        if (subtotalInfo.getSubtotal()) {
            List fields = subtotalInfo.getFields();
            Iterator iterator = fields.iterator();
            while (iterator.hasNext()) {
                boolean isRowExist = false;
                FieldInfo next = (FieldInfo)iterator.next();
                for (DynamicObject row : rowColls) {
                    if (!StringUtils.equals((CharSequence)row.getString("numberalias"), (CharSequence)next.getNumberAlias())) continue;
                    next.setRowFieldId(row.getString("id"));
                    isRowExist = true;
                    break;
                }
                if (isRowExist) continue;
                iterator.remove();
            }
            reportConfigDy.set("subtotalfield", (Object)ReportManageOp.getSubTotalIds((MulBasedataDynamicObjectCollection)reportConfigDy.getDynamicObjectCollection("subtotalfield"), fields));
            reportConfigDy.set("subtotalname", (Object)subtotalInfo.getName());
        } else {
            reportConfigDy.set("subtotalfield", null);
        }
        reportConfigDy.set("subtotal", (Object)subtotalInfo.getSubtotal());
        this.saveRowAlgorithm(reportConfigInfo, reportConfigDy);
        reportConfigDy.set("page", (Object)reportConfigInfo.getPage());
        reportConfigDy.set("showseq", (Object)reportConfigInfo.getShowSeq());
        reportConfigDy.set("mergecell", (Object)reportConfigInfo.getMergeCell());
        reportConfigDy.set("freeze", (Object)this.getFreeze(reportConfigInfo));
        this.saveDisplayScheme(reportManageId, workRptId, reportConfigInfo.getRptDisplaySchemeInfo(), rowColls, colColls, workRptInfo);
        if (null != reportConfigInfo.getHeaderMerge()) {
            reportConfigDy.set("headermerge", (Object)SerializationUtils.toJsonString((Object)reportConfigInfo.getHeaderMerge()));
        } else {
            reportConfigDy.set("headermerge", null);
        }
        if (null != reportConfigInfo.getRowColTransposition()) {
            reportConfigDy.set("rowcoltransposition", (Object)SerializationUtils.toJsonString((Object)reportConfigInfo.getRowColTransposition()));
        } else {
            reportConfigDy.set("rowcoltransposition", null);
        }
        reportConfigDy.set("rptmanage", (Object)reportManageId);
        reportConfigDy.set("workrpt", (Object)workRptId);
        this.saveLastStyle(reportConfigInfo, reportConfigDy);
        reportConfigDy.set("rowadvancesort", CollectionUtils.isEmpty((Collection)reportConfigInfo.getAdvanceSortRowList()) ? null : SerializationUtils.toJsonString((Object)reportConfigInfo.getAdvanceSortRowList()));
        reportConfigDy.set("coladvancesort", CollectionUtils.isEmpty((Collection)reportConfigInfo.getAdvanceSortColList()) ? null : SerializationUtils.toJsonString((Object)reportConfigInfo.getAdvanceSortColList()));
        reportConfigDy.set("showdatalabel", (Object)reportConfigInfo.getShowDataLabel());
        reportConfigDy.set("categoryname", (Object)reportConfigInfo.getCategoryName());
        reportConfigDy.set("categoryunit", (Object)reportConfigInfo.getCategoryUnit());
        reportConfigDy.set("valuename", (Object)reportConfigInfo.getValueName());
        reportConfigDy.set("valueunit", (Object)reportConfigInfo.getValueUnit());
        reportConfigDy.set("legend", CollectionUtils.isEmpty((Collection)reportConfigInfo.getLegend()) ? null : SerializationUtils.toJsonString((Object)reportConfigInfo.getLegend()));
        reportConfigDy.set("tableconfig", (Object)reportConfigInfo.getTableConfig());
        if (null != reportConfigInfo.getMo()) {
            reportConfigDy.set("sectors", (Object)reportConfigInfo.getMo().getC());
            reportConfigDy.set("othername", (Object)reportConfigInfo.getMo().getN());
        }
        reportConfigDy.set("chartname", (Object)reportConfigInfo.getCn());
        reportConfigDy.set("tagoverlap", (Object)reportConfigInfo.getTo());
        reportConfigDy.set("annularchart", (Object)reportConfigInfo.getAc());
        if (null != reportConfigInfo.getCr()) {
            reportConfigDy.set("shownote", (Object)reportConfigInfo.getCr().getSn());
            reportConfigDy.set("note", (Object)reportConfigInfo.getCr().getNt());
        }
        DynamicObject rptConfigDy = (DynamicObject)new HRBaseServiceHelper("hrptmc_reportconfig").saveOne(reportConfigDy);
        this.saveColAlgorithm(reportConfigInfo.getAlgorithmCol(), rptConfigDy.getLong("id"));
        ReportJumpConfigService.getInstance().saveReportJumpConfig(reportManageId, workRptId, reportConfigInfo.getReportJumpConfigList());
        RptReferenceLineService.getInstance().saveRefLineConfig(reportManageId, workRptId, reportConfigInfo.getReferenceLine());
    }

    private void saveColAlgorithm(ReportTotalColConfigBo algorithmColInfo, long rptConfigId) {
        HRBaseServiceHelper algorithmColHelper = new HRBaseServiceHelper("hrptmc_algorithmcol");
        if (algorithmColInfo == null) {
            algorithmColHelper.deleteByFilter(new QFilter[]{new QFilter("rptconfig", "=", (Object)rptConfigId)});
            return;
        }
        DynamicObject algorithmColDy = ReportManageService.getAlgorithmCol((long)rptConfigId);
        if (algorithmColDy == null) {
            algorithmColDy = algorithmColHelper.generateEmptyDynamicObject();
            algorithmColDy.set("rptconfig", (Object)rptConfigId);
        }
        algorithmColDy.set("showlocation", (Object)algorithmColInfo.getShowLocation());
        algorithmColDy.set("total", (Object)algorithmColInfo.getShowTotalCol());
        algorithmColDy.set("totalname", (Object)algorithmColInfo.getTotalColName());
        algorithmColDy.set("subtotal", (Object)algorithmColInfo.getShowSubTotalCol());
        algorithmColDy.set("subtotalname", (Object)algorithmColInfo.getSubTotalColName());
        algorithmColDy.set("dimfield", (Object)algorithmColInfo.getDimField());
        DynamicObjectCollection algorithmTypeColl = algorithmColDy.getDynamicObjectCollection("entryentity");
        if (!CollectionUtils.isEmpty((Collection)algorithmColInfo.getIndexAlgorithmList())) {
            this.saveAlgorithmType(algorithmColInfo.getIndexAlgorithmList(), algorithmTypeColl, algorithmColHelper, "entryentity");
            algorithmColDy.set("entryentity", (Object)algorithmTypeColl);
        } else {
            algorithmTypeColl.clear();
        }
        algorithmColHelper.saveOne(algorithmColDy);
    }

    @ExcludeFromJacocoGeneratedReport
    private void saveAlgorithmType(List<Map<String, String>> indexAlgorithmList, DynamicObjectCollection algorithmTypeColl, HRBaseServiceHelper algorithmTypeHelper, String entryEntity) {
        if (CollectionUtils.isEmpty(indexAlgorithmList)) {
            algorithmTypeColl.clear();
            return;
        }
        List oldIndexFieldList = algorithmTypeColl.stream().map(dy -> dy.getString("numberalias")).collect(Collectors.toList());
        List newIndexFieldList = indexAlgorithmList.stream().map(map -> (String)map.get("indexField")).collect(Collectors.toList());
        List deleteList = (List)org.apache.commons.collections.CollectionUtils.subtract(oldIndexFieldList, newIndexFieldList);
        List addList = (List)org.apache.commons.collections.CollectionUtils.subtract(newIndexFieldList, oldIndexFieldList);
        for (int i = 0; i < indexAlgorithmList.size(); ++i) {
            Map<String, String> map2 = indexAlgorithmList.get(i);
            if (addList.contains(map2.get("indexField"))) {
                DynamicObject algorithmTypeDy = algorithmTypeHelper.generateEmptyEntryDynamicObject(entryEntity);
                algorithmTypeDy.set("seq", (Object)i);
                algorithmTypeDy.set("numberalias", (Object)map2.get("indexField"));
                algorithmTypeDy.set("algorithm", (Object)map2.get("algorithm"));
                algorithmTypeColl.add((Object)algorithmTypeDy);
                continue;
            }
            if (deleteList.contains(map2.get("indexField"))) {
                algorithmTypeColl.removeIf(dy -> dy.getString("numberalias").equals(map2.get("indexField")));
                continue;
            }
            ((DynamicObject)algorithmTypeColl.get(i)).set("seq", (Object)i);
            ((DynamicObject)algorithmTypeColl.get(i)).set("algorithm", (Object)map2.get("algorithm"));
        }
    }

    private void saveRowAlgorithm(ReportConfigInfo reportConfigInfo, DynamicObject reportConfigDy) {
        List indexAlgorithmList = reportConfigInfo.getIndexAlgorithmList();
        DynamicObjectCollection rowAlgorithmColl = reportConfigDy.getDynamicObjectCollection("entryentity1");
        this.saveAlgorithmType(indexAlgorithmList, rowAlgorithmColl, this.rptConfigHelper, "entryentity1");
        reportConfigDy.set("entryentity1", (Object)rowAlgorithmColl);
    }

    private void saveLastStyle(ReportConfigInfo reportConfigInfo, DynamicObject reportConfigDy) {
        DynamicObjectCollection styleColl = reportConfigDy.getDynamicObjectCollection("entryentity");
        if (styleColl == null) {
            styleColl = this.rptConfigHelper.generateEmptyEntryCollection(reportConfigDy, "entryentity");
        } else {
            styleColl.clear();
        }
        styleColl.add((Object)this.getStyleDy(reportConfigInfo.getHls().getCs(), "0"));
        styleColl.add((Object)this.getStyleDy(reportConfigInfo.getHls().getBgs(), "1"));
        styleColl.add((Object)this.getStyleDy(reportConfigInfo.getBls().getCs(), "2"));
        styleColl.add((Object)this.getStyleDy(reportConfigInfo.getBls().getBgs(), "3"));
        reportConfigDy.set("headstyle", null == reportConfigInfo.getHs() ? null : SerializationUtils.toJsonString((Object)reportConfigInfo.getHs()));
        reportConfigDy.set("entryentity", (Object)styleColl);
    }

    private DynamicObject getStyleDy(List<String> colors, String styleType) {
        DynamicObject styleDy = this.rptConfigHelper.generateEmptyEntryDynamicObject("entryentity");
        styleDy.set("laststyle", (Object)SerializationUtils.toJsonString(colors));
        styleDy.set("styletype", (Object)styleType);
        return styleDy;
    }

    private int getFreeze(ReportConfigInfo reportConfigInfo) {
        return null != reportConfigInfo.getFreezeCol() && reportConfigInfo.getFreezeCol().getIsFreeze() ? reportConfigInfo.getFreezeCol().getFreezeNum() : -1;
    }

    private static MulBasedataDynamicObjectCollection getSubTotalIds(MulBasedataDynamicObjectCollection collection, List<FieldInfo> rowFieldIds) {
        long[] ids = DB.genLongIds((String)collection.getDynamicObjectType().getAlias(), (int)rowFieldIds.size());
        collection.clear();
        for (int i = 0; i < rowFieldIds.size(); ++i) {
            DynamicObject dy = collection.addNew();
            dy.set("pkid", (Object)ids[i]);
            dy.set("fbasedataid_id", (Object)rowFieldIds.get(i).getRowFieldId());
        }
        return collection;
    }

    private List<Long> getRowFieldIds(List<RowFieldInfo> rows) {
        ArrayList<Long> rowFieldIds = new ArrayList<Long>(rows.size());
        for (RowFieldInfo rowFieldInfo : rows) {
            if (IDStringUtils.idNotEmpty((String)rowFieldInfo.getGroupName().getRowFieldId())) {
                rowFieldIds.add(Long.parseLong(rowFieldInfo.getGroupName().getRowFieldId()));
            }
            ReportManageOp.recursive(rowFieldInfo._getChildFields(), fieldInfo -> {
                if (IDStringUtils.idNotEmpty((String)fieldInfo.getRowFieldId())) {
                    rowFieldIds.add(Long.parseLong(fieldInfo.getRowFieldId()));
                }
            });
        }
        return rowFieldIds;
    }

    private DynamicObjectCollection saveRows(List<RowFieldInfo> rows, long rptManageId, long workRptId) {
        if (CollectionUtils.isEmpty(rows)) {
            ReportManageService.deleteRowsByWorkRpt((Long)workRptId);
            return new DynamicObjectCollection();
        }
        List<Long> rowFieldIds = this.getRowFieldIds(rows);
        DynamicObject[] oldRowDys = ReportManageService.getRowFields(rowFieldIds);
        Map<String, DynamicObject> oldRowFieldMap = Arrays.stream(oldRowDys).collect(Collectors.toMap(dy -> dy.getString("id"), dy -> dy));
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrptmc_rowfield");
        helper.deleteByFilter(new QFilter[]{new QFilter("workrpt", "=", (Object)workRptId), new QFilter("id", "not in", rowFieldIds)});
        DynamicObjectCollection rowColls = new DynamicObjectCollection();
        long[] rowsId = ORM.create().genLongIds("hrptmc_rowfield", rows.size());
        AtomicInteger childrenSize = new AtomicInteger();
        for (RowFieldInfo rowFieldInfo : rows) {
            if (!rowFieldInfo._isMerge()) continue;
            childrenSize.addAndGet(rowFieldInfo._getChildFields().size());
            ReportManageOp.recursive(rowFieldInfo._getChildFields(), fieldInfo -> {
                if (!CollectionUtils.isEmpty((Collection)fieldInfo.getFields())) {
                    childrenSize.addAndGet(fieldInfo.getFields().size());
                }
            });
        }
        long[] childRowsIds = ORM.create().genLongIds("hrptmc_rowfield", childrenSize.get());
        Stack longStack = new Stack();
        Arrays.stream(childRowsIds).forEach(longStack::push);
        for (int i = 0; i < rows.size(); ++i) {
            DynamicObject rowDy;
            RowFieldInfo rowFieldInfo = rows.get(i);
            if (!oldRowFieldMap.containsKey(rowFieldInfo.getGroupName().getRowFieldId())) {
                rowDy = helper.generateEmptyDynamicObject();
                rowDy.set("id", (Object)rowsId[i]);
            } else {
                rowDy = oldRowFieldMap.get(rowFieldInfo.getGroupName().getRowFieldId());
            }
            rowColls.add((Object)rowDy);
            if (rowFieldInfo._isMerge()) {
                this.fieldInfo2RowDy(helper, rowDy, rowFieldInfo.getGroupName(), i, rptManageId, workRptId, null, rowFieldInfo.getType());
                Long parentId = Long.parseLong(rowFieldInfo.getGroupName().getRowFieldId());
                int finalI = i;
                ReportManageOp.recursive(rowFieldInfo._getChildFields(), fieldInfo -> {
                    DynamicObject childRowDy;
                    if (!oldRowFieldMap.containsKey(fieldInfo.getRowFieldId())) {
                        childRowDy = helper.generateEmptyDynamicObject();
                        childRowDy.set("id", longStack.pop());
                    } else {
                        childRowDy = (DynamicObject)oldRowFieldMap.get(fieldInfo.getRowFieldId());
                    }
                    this.fieldInfo2RowDy(helper, childRowDy, (FieldInfo)fieldInfo, finalI, rptManageId, workRptId, parentId, null);
                    rowColls.add((Object)childRowDy);
                });
                continue;
            }
            this.fieldInfo2RowDy(helper, rowDy, rowFieldInfo.getGroupName(), i, rptManageId, workRptId, null, null);
        }
        helper.save(rowColls);
        return rowColls;
    }

    private Long parseFieldId(String fieldId) {
        return StringUtils.isEmpty((CharSequence)fieldId) ? 0L : Long.parseLong(fieldId);
    }

    private void fieldInfo2RowDy(HRBaseServiceHelper helper, DynamicObject rowDy, FieldInfo fieldInfo, int seq, Long rptManageId, Long workRptId, Long parentId, String mergeType) {
        rowDy.set("bizindex", (Object)seq);
        rowDy.set("displayname", (Object)fieldInfo.getDisplayName());
        rowDy.set("numberalias", (Object)fieldInfo.getNumberAlias());
        rowDy.set("type", (Object)fieldInfo.getType());
        rowDy.set("algorithm", (Object)fieldInfo.getAlgorithm());
        this.setDataFormat(helper, rowDy, fieldInfo.getDataFormat());
        rowDy.set("sort", (Object)fieldInfo.getSort());
        rowDy.set("showemptycol", (Object)fieldInfo.getShowEmptyCol());
        rowDy.set("displaymode", (Object)fieldInfo.getDisplayMode());
        rowDy.set("orgversiondate", (Object)fieldInfo.getOrgVersionDate());
        rowDy.set("parentid", (Object)parentId);
        rowDy.set("mergetype", (Object)mergeType);
        rowDy.set("rptmanage", (Object)rptManageId);
        rowDy.set("workrpt", (Object)workRptId);
        this.setFieldRefId(rowDy, fieldInfo);
        fieldInfo.setRowFieldId(rowDy.getString("id"));
    }

    private void setFieldRefId(DynamicObject fieldDy, FieldInfo fieldInfo) {
        String fieldSrc = fieldInfo.getFieldSrc();
        Long fieldId = this.parseFieldId(fieldInfo.getFieldId());
        if ("0".equals(fieldSrc)) {
            fieldDy.set("anobjfield", (Object)fieldId);
        } else if ("1".equals(fieldSrc)) {
            fieldDy.set("calcidxfield", (Object)fieldId);
        } else if ("2".equals(fieldSrc)) {
            fieldDy.set("preidxfield", (Object)fieldId);
        }
    }

    private void setDataFormat(HRBaseServiceHelper helper, DynamicObject fieldDy, DataFormatInfo dataFormatInfo) {
        DynamicObjectCollection dataFormatCol;
        if (null == dataFormatInfo) {
            return;
        }
        if (fieldDy.getDataEntityState().getFromDatabase()) {
            dataFormatCol = fieldDy.getDynamicObjectCollection("entryentity");
            dataFormatCol.clear();
        } else {
            dataFormatCol = helper.generateEmptyEntryCollection(fieldDy, "entryentity");
        }
        DynamicObject dataFormatDy = helper.generateEmptyEntryDynamicObject("entryentity");
        dataFormatDy.set("datedisplaymode", (Object)dataFormatInfo.getDisplayMode());
        dataFormatDy.set("nullrule", (Object)dataFormatInfo.getNullRule());
        dataFormatDy.set("decimaldigits", (Object)dataFormatInfo.getDecimalDigits());
        dataFormatDy.set("roundmethod", (Object)dataFormatInfo.getRoundMethod());
        dataFormatCol.add((Object)dataFormatDy);
    }

    private List<Long> getColFieldIds(List<FieldInfo> columns) {
        ArrayList<Long> colFieldIds = new ArrayList<Long>(columns.size());
        for (FieldInfo fieldInfo : columns) {
            if (!IDStringUtils.idNotEmpty((String)fieldInfo.getRowFieldId())) continue;
            colFieldIds.add(Long.parseLong(fieldInfo.getRowFieldId()));
        }
        return colFieldIds;
    }

    private DynamicObjectCollection saveColumns(List<FieldInfo> columns, long reportManageId, long workRptId) {
        if (CollectionUtils.isEmpty(columns)) {
            ReportManageService.deleteColsByWorkRpt((Long)workRptId);
            return new DynamicObjectCollection();
        }
        List<Long> colFieldIds = this.getColFieldIds(columns);
        DynamicObject[] oldColDys = ReportManageService.getColFields(colFieldIds);
        Map<String, DynamicObject> oldColFieldMap = Arrays.stream(oldColDys).collect(Collectors.toMap(dy -> dy.getString("id"), dy -> dy));
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrptmc_colfield");
        helper.deleteByFilter(new QFilter[]{new QFilter("workrpt", "=", (Object)workRptId), new QFilter("id", "not in", colFieldIds)});
        DynamicObjectCollection colColls = new DynamicObjectCollection();
        long[] colIds = ORM.create().genLongIds("hrptmc_colfield", columns.size());
        for (int i = 0; i < columns.size(); ++i) {
            DynamicObject colDy;
            FieldInfo fieldInfo = columns.get(i);
            if (oldColFieldMap.containsKey(fieldInfo.getRowFieldId())) {
                colDy = oldColFieldMap.get(fieldInfo.getRowFieldId());
            } else {
                colDy = helper.generateEmptyDynamicObject();
                colDy.set("id", (Object)colIds[i]);
            }
            this.fieldInfo2ColDy(helper, colDy, fieldInfo, i, reportManageId, workRptId);
            colColls.add((Object)colDy);
        }
        helper.save(colColls);
        return colColls;
    }

    private void fieldInfo2ColDy(HRBaseServiceHelper helper, DynamicObject colDy, FieldInfo fieldInfo, int seq, Long rptManageId, Long workRptId) {
        colDy.set("bizindex", (Object)seq);
        colDy.set("displayname", (Object)fieldInfo.getDisplayName());
        colDy.set("numberalias", (Object)fieldInfo.getNumberAlias());
        colDy.set("rptmanage", (Object)rptManageId);
        colDy.set("workrpt", (Object)workRptId);
        colDy.set("sort", (Object)fieldInfo.getSort());
        colDy.set("showemptycol", (Object)fieldInfo.getShowEmptyCol());
        this.setFieldRefId(colDy, fieldInfo);
        this.setDataFormat(helper, colDy, fieldInfo.getDataFormat());
        fieldInfo.setRowFieldId(colDy.getString("id"));
    }

    private void savePreIndexRef(long reportManageId) {
        String cachePreIdxStr = this.getOption().getVariableValue("rptPreIndex");
        if (StringUtils.isNotEmpty((CharSequence)cachePreIdxStr)) {
            List indexFieldInfos = SerializationUtils.fromJsonStringToList((String)cachePreIdxStr, IndexFieldInfo.class);
            if (CollectionUtils.isEmpty((Collection)indexFieldInfos)) {
                this.clearIndexesAndDimMaps(reportManageId);
                return;
            }
            HRBaseServiceHelper helper = new HRBaseServiceHelper("hrptmc_reportpreindex");
            ReportManageService.deletePreIdxRef((Long)reportManageId);
            long[] ids = ORM.create().genLongIds("hrptmc_reportpreindex", indexFieldInfos.size());
            DynamicObject[] batchSave = new DynamicObject[indexFieldInfos.size()];
            for (int i = 0; i < indexFieldInfos.size(); ++i) {
                DynamicObject dy = helper.generateEmptyDynamicObject();
                dy.set("id", (Object)ids[i]);
                dy.set("report", (Object)reportManageId);
                dy.set("preindex", (Object)this.parseFieldId(((IndexFieldInfo)indexFieldInfos.get(i)).getFieldId()));
                batchSave[i] = dy;
            }
            helper.save(batchSave);
        } else {
            this.clearIndexesAndDimMaps(reportManageId);
        }
    }

    private void clearIndexesAndDimMaps(long reportManageId) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrptmc_reportpreindex");
        QFilter[] qFilters = new QFilter[]{new QFilter("report", "=", (Object)reportManageId)};
        helper.deleteByFilter(qFilters);
        HRBaseServiceHelper dimHelper = new HRBaseServiceHelper("hrptmc_dimmap");
        dimHelper.deleteByFilter(qFilters);
    }

    private void saveDimMap(List<DimMapBo> dimMapBoList, long reportManageId, long workRptId) {
        if (CollectionUtils.isEmpty(dimMapBoList)) {
            HRBaseServiceHelper dimHelper = new HRBaseServiceHelper("hrptmc_dimmap");
            QFilter[] qFilters = new QFilter[]{new QFilter("workrpt", "=", (Object)workRptId)};
            dimHelper.deleteByFilter(qFilters);
            return;
        }
        HRBaseServiceHelper dimMapHelper = new HRBaseServiceHelper("hrptmc_dimmap");
        ReportManageService.deleteDimMapRef((Long)workRptId);
        long[] dimIds = ORM.create().genLongIds("hrptmc_dimmap", dimMapBoList.size());
        DynamicObject[] batchSave = new DynamicObject[dimMapBoList.size()];
        for (int i = 0; i < dimMapBoList.size(); ++i) {
            DimMapBo dimMapInfo = dimMapBoList.get(i);
            DynamicObject dy = dimMapHelper.generateEmptyDynamicObject();
            dy.set("id", (Object)dimIds[i]);
            dy.set("preindexnumber", (Object)dimMapInfo.getPreIndexNumber());
            dy.set("preindex", (Object)dimMapInfo.getPreIndexId());
            dy.set("report", (Object)reportManageId);
            dy.set("workrpt", (Object)workRptId);
            List dimMapEntryBoList = dimMapInfo.getDimMapEntryBos();
            DynamicObjectCollection newEntries = dimMapHelper.generateEmptyEntryCollection(dy, "entryentity");
            for (DimMapEntryBo entryBo : dimMapEntryBoList) {
                DynamicObject newEntry = dimMapHelper.generateEmptyEntryDynamicObject("entryentity");
                newEntry.set("dimnumber", (Object)entryBo.getDimNumber());
                newEntry.set("dim", (Object)entryBo.getDim());
                newEntry.set("dimtype", (Object)entryBo.getDimType());
                newEntry.set("dimrealfrom", (Object)entryBo.getDimRealFrom());
                newEntry.set("dimfrom", (Object)entryBo.getDimFrom());
                newEntry.set("paramrule", (Object)entryBo.getParamRule());
                newEntry.set("preindexparam", (Object)entryBo.getPreIndexParam());
                newEntries.add((Object)newEntry);
            }
            dy.set("entryentity", (Object)newEntries);
            batchSave[i] = dy;
        }
        dimMapHelper.save(batchSave);
    }

    private boolean isDispScmChange() {
        boolean isDispScmChange = Boolean.parseBoolean(this.getOption().getVariableValue("dispScmChange", ""));
        LOGGER.info("isDispScmChange:{}", (Object)isDispScmChange);
        return isDispScmChange;
    }

    private boolean isDispScmPublish() {
        return Boolean.parseBoolean(this.getOption().getVariableValue("dispScmPublish", ""));
    }

    @ExcludeFromJacocoGeneratedReport
    private void saveDisplayScheme(long rptManageId, long workRptId, RptDisplaySchemeInfo displaySchemeInfo, DynamicObjectCollection rowColls, DynamicObjectCollection colColls, WorkRptInfo workRptInfo) {
        if (null == displaySchemeInfo) {
            return;
        }
        if (this.isDispScmPublish() && this.isDispScmChange()) {
            ReportManageService.batchUpdateUserDispScmChg((Long)rptManageId, (boolean)false, (String)displaySchemeInfo.getEnable());
        }
        Map<String, Long> rowFieldMap = rowColls.stream().collect(Collectors.toMap(row -> row.getString("numberalias"), row -> row.getLong("id")));
        Map<String, Long> colFieldMap = colColls.stream().collect(Collectors.toMap(col -> col.getString("numberalias"), col -> col.getLong("id")));
        RptDisplaySchemeService.saveRptDisplaySchemeInfo((long)rptManageId, (long)workRptId, (RptDisplaySchemeInfo)displaySchemeInfo, rowFieldMap, colFieldMap);
        List children = displaySchemeInfo.getChildren();
        this.saveUserDispScmIncrement(rptManageId, workRptId, children);
        if (this.isDispScmPublish()) {
            RptDisplaySchemeEtyInfo rptDisplaySchemeEtyInfo = RptDisplaySchemeService.genEmptyDisplaySchemeEty((List)workRptInfo.getRows(), (List)workRptInfo.getColumns());
            UserDisplaySchemeService.updateUserDispScm((long)rptManageId, (RptDisplaySchemeEtyInfo)rptDisplaySchemeEtyInfo);
            ReportManageService.batchUpdateUserDispScmChg((Long)rptManageId, (boolean)false, (String)"1");
        }
    }

    private void saveUserDispScmIncrement(long rptManageId, long workRptId, List<RptDisplaySchemeEtyInfo> children) {
        List deleteIds;
        DynamicObject[] existScmDys = ReportManageService.getRptUserDispScmByRptId((long)rptManageId);
        Map<String, DynamicObject> existScmMap = Arrays.stream(existScmDys).collect(Collectors.toMap(dy -> dy.getString("id"), dy -> dy, (v1, v2) -> v1));
        DynamicObjectCollection saveDys = new DynamicObjectCollection();
        HashSet<Long> needSaveIds = new HashSet<Long>();
        if (!CollectionUtils.isEmpty(children)) {
            for (RptDisplaySchemeEtyInfo schemeInfo : children) {
                String schemeKey = schemeInfo.getId();
                DynamicObject userDispScmDy = existScmMap.containsKey(schemeKey) ? existScmMap.get(schemeKey) : ReportManageService.generateUserDispScmDy();
                UserDisplaySchemeService.handlerUserDispScm((long)rptManageId, (long)workRptId, (RptDisplaySchemeEtyInfo)schemeInfo, (DynamicObject)userDispScmDy);
                saveDys.add((Object)userDispScmDy);
                needSaveIds.add(userDispScmDy.getLong("id"));
            }
        }
        if (!(deleteIds = Arrays.stream(existScmDys).map(dy -> dy.getLong("id")).filter(id -> !needSaveIds.contains(id)).collect(Collectors.toList())).isEmpty()) {
            ReportManageService.deleteUserDispScmByIds(deleteIds);
        }
        if (!saveDys.isEmpty()) {
            ReportManageService.batchSaveUserDispScm((DynamicObjectCollection)saveDys);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void saveReportMark(long rptManageId) {
        String reportMarkInfoStr = this.getOption().getVariableValue("reportMarkInfo");
        if (StringUtils.isNotEmpty((CharSequence)reportMarkInfoStr)) {
            ReportMarkInfo reportMarkInfo = (ReportMarkInfo)SerializationUtils.fromJsonString((String)reportMarkInfoStr, ReportMarkInfo.class);
            HRBaseServiceHelper rptMarkHelper = new HRBaseServiceHelper("hrptmc_reportmark");
            DynamicObject rptMarkDy = rptMarkHelper.loadDynamicObject(new QFilter("rptmanage", "=", (Object)rptManageId));
            if (null == rptMarkDy) {
                rptMarkDy = rptMarkHelper.generateEmptyDynamicObject();
                rptMarkDy.set("rptmanage", (Object)rptManageId);
                rptMarkDy.set("show", (Object)reportMarkInfo.getIsShow());
                rptMarkHelper.saveOne(rptMarkDy);
            } else if (!reportMarkInfo.getIsShow().equals(rptMarkDy.getString("show"))) {
                rptMarkDy.set("show", (Object)reportMarkInfo.getIsShow());
                rptMarkHelper.saveOne(rptMarkDy);
            }
            DynamicObjectCollection dyColls = new DynamicObjectCollection();
            HRBaseServiceHelper rptMarkCntHelper = new HRBaseServiceHelper("hrptmc_rptmarkcontent");
            DynamicObject[] rptMarkCntDys = rptMarkCntHelper.loadDynamicObjectArray(new QFilter[]{new QFilter("rptmark", "=", (Object)rptMarkDy.getLong("id"))});
            if (rptMarkCntDys == null || rptMarkCntDys.length == 0) {
                for (Map.Entry entry : reportMarkInfo.getLocaleContent().entrySet()) {
                    DynamicObject rptMarkCntDy = rptMarkCntHelper.generateEmptyDynamicObject();
                    rptMarkCntDy.set("locale", entry.getKey());
                    rptMarkCntDy.set("markcontent", entry.getValue());
                    rptMarkCntDy.set("rptmark", (Object)rptMarkDy.getLong("id"));
                    dyColls.add((Object)rptMarkCntDy);
                }
            } else {
                Map<String, DynamicObject> rptMarkCntMap = Arrays.stream(rptMarkCntDys).collect(Collectors.toMap(dy -> dy.getString("locale"), dy -> dy));
                for (Map.Entry entry : reportMarkInfo.getLocaleContent().entrySet()) {
                    DynamicObject rptMarkCntDy;
                    if (rptMarkCntMap.containsKey(entry.getKey())) {
                        rptMarkCntDy = rptMarkCntMap.get(entry.getKey());
                        rptMarkCntDy.set("markcontent", entry.getValue());
                        dyColls.add((Object)rptMarkCntDy);
                        continue;
                    }
                    rptMarkCntDy = rptMarkCntHelper.generateEmptyDynamicObject();
                    rptMarkCntDy.set("locale", entry.getKey());
                    rptMarkCntDy.set("markcontent", entry.getValue());
                    rptMarkCntDy.set("rptmark", (Object)rptMarkDy.getLong("id"));
                    dyColls.add((Object)rptMarkCntDy);
                }
            }
            rptMarkCntHelper.save(dyColls);
        }
    }

    private void closeEnableScheme(long reportId, boolean isMulWorkConfig) {
        if (isMulWorkConfig) {
            DynamicObject config = ReportManageService.getParamConfigWithSchedule((long)reportId);
            ReportManageService.closeEnableScheme((DynamicObject)config);
        }
    }

    private static /* synthetic */ boolean lambda$saveSplitDate$6(Set deleteSet, DynamicObject dy) {
        return deleteSet.contains(dy.getString("anobjfield.fieldpath") + "!" + dy.getString("number"));
    }
}
