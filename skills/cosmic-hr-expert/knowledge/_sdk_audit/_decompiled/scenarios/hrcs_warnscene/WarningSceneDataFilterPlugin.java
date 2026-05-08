/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.ext.hr.filter.control.HRFilter
 *  kd.bos.ext.hr.ruleengine.controls.RuleCondition
 *  kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo
 *  kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.service.complexobj.ReportQueryService
 *  kd.hr.hbp.business.service.labelandreport.HRFilterUtil
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.model.complexobj.HRComplexObjContext
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.WarningSceneService
 *  kd.hr.hrcs.common.model.earlywarn.WarnCalFieldBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewBodyCellInfo
 *  kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewBodyInfo
 *  kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewHeadInfo
 *  kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewHeadRowInfo
 *  kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewInfo
 *  kd.hr.hrcs.common.model.earlywarn.WarnEntityRelationBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnJoinEntityBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo
 *  kd.hr.hrcs.common.util.earlywarn.WarnComplexObjTransferUtil
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scene;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.ext.hr.filter.control.HRFilter;
import kd.bos.ext.hr.ruleengine.controls.RuleCondition;
import kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo;
import kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.service.complexobj.ReportQueryService;
import kd.hr.hbp.business.service.labelandreport.HRFilterUtil;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.model.complexobj.HRComplexObjContext;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.WarningSceneService;
import kd.hr.hrcs.common.model.earlywarn.WarnCalFieldBo;
import kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewBo;
import kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewBodyCellInfo;
import kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewBodyInfo;
import kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewHeadInfo;
import kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewHeadRowInfo;
import kd.hr.hrcs.common.model.earlywarn.WarnDataPreviewInfo;
import kd.hr.hrcs.common.model.earlywarn.WarnEntityRelationBo;
import kd.hr.hrcs.common.model.earlywarn.WarnJoinEntityBo;
import kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo;
import kd.hr.hrcs.common.util.earlywarn.WarnComplexObjTransferUtil;

@ExcludeFromJacocoGeneratedReport
public final class WarningSceneDataFilterPlugin
extends HRDataBaseEdit {
    private static final Log LOG = LogFactory.getLog(WarningSceneDataFilterPlugin.class);
    private static final String DATA_FILTER_CONTROL = "hrfilterap";
    private static final String DATA_FILTER_FIELD = "datafilter";
    private static final String RULE_DATE_KEY = "ruledate";

    public void customEvent(CustomEventArgs args) {
        String eventName = args.getEventName();
        if ("getDataView".equals(eventName)) {
            this.previewFilterData();
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        RuleCondition ruleCondition;
        String dataFilter;
        super.beforeItemClick(evt);
        String itemKey = evt.getItemKey();
        if (("bar_save".equals(itemKey) || "laststepbtn".equals(itemKey)) && StringUtils.isNotEmpty((CharSequence)(dataFilter = (ruleCondition = (RuleCondition)this.getControl(DATA_FILTER_CONTROL)).getValue()))) {
            RuleValidateInfo info = RuleValidateUtil.validCondition((String)dataFilter, (boolean)true);
            if (!info.isSuccess()) {
                StringBuilder errorMsg = new StringBuilder();
                for (String msg : info.getMsgList()) {
                    errorMsg.append(msg).append('\uff0c');
                }
                evt.setCancel(true);
                this.getView().showTipNotification(errorMsg.substring(0, errorMsg.length() - 1));
            } else {
                this.getModel().setValue(DATA_FILTER_FIELD, (Object)ruleCondition.getValue());
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        try {
            String key = e.getProperty().getName();
            if (HRStringUtils.equals((String)RULE_DATE_KEY, (String)key)) {
                Date newData = (Date)e.getChangeSet()[0].getNewValue();
                if (null == newData) {
                    return;
                }
                String data = HRDateTimeUtils.format((Date)newData, (String)"yyyy-MM-dd");
                this.getHRFilter().setDate(data);
                this.getModel().setValue(RULE_DATE_KEY, null);
            }
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }

    public HRFilter getHRFilter() {
        return (HRFilter)this.getControl(DATA_FILTER_CONTROL);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @ExcludeFromJacocoGeneratedReport
    private void previewFilterData() {
        HRFilter ruleCondition = this.getHRFilter();
        String dataFilter = ruleCondition.getValue();
        List<QFilter> qFilters = null;
        if (StringUtils.isNotEmpty((CharSequence)dataFilter)) {
            RuleValidateInfo info = RuleValidateUtil.validCondition((String)dataFilter, (boolean)true);
            if (!info.isSuccess()) {
                StringBuilder errorMsg = new StringBuilder();
                for (String msg : info.getMsgList()) {
                    errorMsg.append(msg).append(',');
                }
                this.getView().showTipNotification(errorMsg.substring(0, errorMsg.length() - 1));
            } else {
                qFilters = this.transferToQFilter(dataFilter);
                qFilters.removeIf(Objects::isNull);
            }
        }
        this.getView().showLoading(new LocaleString(ResManager.loadKDString((String)"\u6b63\u5728\u52a0\u8f7d...", (String)"WarningSceneDataFilterPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0])));
        try {
            WarnDataPreviewBo warnDataPreviewBo = new WarnDataPreviewBo();
            warnDataPreviewBo.setReportInfo(this.getDataPreviewInfo(qFilters));
            IClientViewProxy clientViewProxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
            clientViewProxy.setEntryProperty("datafiltercontrol", "data", (Object)warnDataPreviewBo);
        }
        finally {
            this.getView().hideLoading();
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private WarnDataPreviewInfo getDataPreviewInfo(List<QFilter> qFilters) {
        List sortedCalFields;
        HRComplexObjContext hrComplexObjContext;
        String entityRelationsStr;
        WarnDataPreviewInfo warnDataPreviewInfo = new WarnDataPreviewInfo();
        List queryFields = WarningSceneService.getInstance().getCacheQueryFields(this.getView(), true);
        List calculateFields = WarningSceneService.getInstance().getCacheCalculateFields(this.getView(), true);
        List entityRelations = Collections.emptyList();
        List joinEntities = Collections.emptyList();
        String joinEntitiesStr = this.getPageCache().get("joinEntities");
        if (HRStringUtils.isNotEmpty((String)joinEntitiesStr)) {
            joinEntities = JSON.parseArray((String)joinEntitiesStr, WarnJoinEntityBo.class);
        }
        if (HRStringUtils.isNotEmpty((String)(entityRelationsStr = this.getPageCache().get("entityRelations")))) {
            entityRelations = JSON.parseArray((String)entityRelationsStr, WarnEntityRelationBo.class);
        }
        if ((hrComplexObjContext = WarnComplexObjTransferUtil.transferToComplexObjContext((List)joinEntities, (List)queryFields, (List)entityRelations, sortedCalFields = calculateFields.stream().sorted(Comparator.comparingInt(WarnCalFieldBo::getOrder)).collect(Collectors.toList()), qFilters)) != null) {
            ILocaleString name = (ILocaleString)this.getModel().getValue("name");
            hrComplexObjContext.setAlgoxJobKey(String.join((CharSequence)"_", "PRE", name.getLocaleValue()));
            hrComplexObjContext.setTransferField(false);
        }
        ReportQueryService reportQueryService = new ReportQueryService(hrComplexObjContext);
        List resultMap = reportQueryService.queryMap(0, 500);
        WarnDataPreviewHeadInfo warnDataPreviewHeadInfo = new WarnDataPreviewHeadInfo();
        ArrayList headRowInfos = Lists.newArrayListWithExpectedSize((int)16);
        queryFields.forEach(warnQueryFieldBo -> headRowInfos.add(new WarnDataPreviewHeadRowInfo(warnQueryFieldBo.getFieldNumber(), warnQueryFieldBo.getFieldAlias(), warnQueryFieldBo.getFieldName().getLocaleValue())));
        calculateFields.forEach(calField -> headRowInfos.add(new WarnDataPreviewHeadRowInfo(calField.getFieldNumber(), calField.getFieldNumber(), calField.getFieldName().getLocaleValue())));
        warnDataPreviewHeadInfo.setHf((List)headRowInfos);
        warnDataPreviewInfo.setHead(warnDataPreviewHeadInfo);
        List collect = headRowInfos.stream().map(WarnDataPreviewHeadRowInfo::getCode).collect(Collectors.toList());
        ArrayList warnDataPreviewBodyInfoList = Lists.newArrayListWithExpectedSize((int)16);
        for (Map map : resultMap) {
            WarnDataPreviewBodyInfo warnDataPreviewBodyInfo = new WarnDataPreviewBodyInfo();
            ArrayList cellInfoList = Lists.newArrayListWithCapacity((int)16);
            for (Map.Entry entry : map.entrySet()) {
                if (!collect.contains(entry.getKey())) continue;
                WarnDataPreviewBodyCellInfo cellInfo = new WarnDataPreviewBodyCellInfo();
                cellInfo.setK((String)entry.getKey());
                cellInfo.setV((Object)(entry.getValue() == null ? "" : entry.getValue()));
                cellInfoList.add(cellInfo);
            }
            warnDataPreviewBodyInfo.setR((List)cellInfoList);
            warnDataPreviewBodyInfoList.add(warnDataPreviewBodyInfo);
        }
        warnDataPreviewInfo.setBody((List)warnDataPreviewBodyInfoList);
        return warnDataPreviewInfo;
    }

    @ExcludeFromJacocoGeneratedReport
    private List<QFilter> transferToQFilter(String dataFilter) {
        List allQueryFieldBos;
        QFilter qFilter;
        ArrayList qFilters = Lists.newArrayListWithCapacity((int)10);
        if (HRStringUtils.isNotEmpty((String)dataFilter) && (qFilter = HRFilterUtil.condition2QFilter4HRReport((String)dataFilter, (String)SerializationUtils.toJsonString((Object)(allQueryFieldBos = JSON.parseArray((String)this.getPageCache().get("queryFields"), WarnQueryFieldBo.class))))) != null) {
            qFilters.add(qFilter);
        }
        return qFilters;
    }
}
