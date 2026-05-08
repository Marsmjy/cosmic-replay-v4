/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.HashBasedTable
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.Row
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.events.BizDataEventArgs
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.DateTimeProp
 *  kd.bos.exception.BosErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.OpenStyle
 *  kd.bos.form.ShowType
 *  kd.bos.form.chart.Axis
 *  kd.bos.form.chart.Chart
 *  kd.bos.form.chart.PointLineChart
 *  kd.bos.form.chart.Series
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.Label
 *  kd.bos.form.control.events.TabSelectEvent
 *  kd.bos.form.control.events.TabSelectListener
 *  kd.bos.form.events.BeforeFieldPostBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.DateRangeEdit
 *  kd.bos.list.BillList
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.service.KDDateUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.DateUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hies.common.HiesCommonRes
 *  kd.hr.hies.common.enu.OprType
 *  kd.hr.hies.common.enu.TaskState
 *  kd.hr.hies.formplugin.DiaeMgrMonitorPlugin$CustomChart
 *  kd.hr.hies.formplugin.DiaeMgrMonitorPlugin$QueryDateInfo
 *  org.apache.commons.collections4.CollectionUtils
 *  org.apache.commons.lang3.ObjectUtils
 *  org.apache.commons.lang3.tuple.Pair
 */
package kd.hr.hies.formplugin;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.HashBasedTable;
import java.io.Serializable;
import java.lang.constant.Constable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.events.BizDataEventArgs;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.DateTimeProp;
import kd.bos.exception.BosErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.OpenStyle;
import kd.bos.form.ShowType;
import kd.bos.form.chart.Axis;
import kd.bos.form.chart.Chart;
import kd.bos.form.chart.PointLineChart;
import kd.bos.form.chart.Series;
import kd.bos.form.container.Tab;
import kd.bos.form.control.Control;
import kd.bos.form.control.Label;
import kd.bos.form.control.events.TabSelectEvent;
import kd.bos.form.control.events.TabSelectListener;
import kd.bos.form.events.BeforeFieldPostBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.DateRangeEdit;
import kd.bos.list.BillList;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.service.KDDateUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.DateUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hies.common.HiesCommonRes;
import kd.hr.hies.common.enu.OprType;
import kd.hr.hies.common.enu.TaskState;
import kd.hr.hies.formplugin.DiaeMgrMonitorPlugin;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;

/*
 * Exception performing whole class analysis ignored.
 */
@ExcludeFromJacocoGeneratedReport
public final class DiaeMgrMonitorPlugin
extends HRDataBaseEdit
implements TabSelectListener {
    private static final Log LOGGER = LogFactory.getLog(DiaeMgrMonitorPlugin.class);
    private static final String STYLE_NORMAL = "normal";
    private static final String ITEM_STYLE = "itemStyle";
    private static final String LINE_COLOR = "color";
    private static final String LINE_TYPE = "type";
    private static final String CTL_DATERANGE_TASK = "taskdaterange";
    private static final String CTL_DATERANGE_ERR = "errdaterange";
    private static final String CTL_DATERANGE_TREND = "trenddaterange";
    private static final String CTL_TAB_BOARD = "tabboard";
    private static final String CTL_TAB_MGRMONITOR = "tabmgrmonitor";
    private static final String CTL_LABEL_IMPORTFINISHED = "importfinished";
    private static final String CTL_LABEL_IMPORTAFOOT = "importafoot";
    private static final String CTL_LABEL_EXPORTFINISHED = "exportfinished";
    private static final String CTL_LABEL_EXPORTAFOOT = "exportafoot";
    private static final String CTL_LABEL_IMPORTERR = "importerr";
    private static final String CTL_LABEL_EXPORTERR = "exporterr";
    private static final HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hies_taskinfo");
    private static final SimpleDateFormat SDF_YYYYMM = new SimpleDateFormat("yyyy.MM");
    private static final String CHILD_PAGEID = "childpageid";
    private static final String COUNT = "count";
    private static final String TOOLTIP = "tooltip";
    private static final String FORMATTER = "formatter";

    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
    }

    public void createNewData(BizDataEventArgs e) {
        super.createNewData(e);
    }

    public void registerListener(EventObject eventObject) {
        this.addClickListeners(new String[]{"importfinished", "importafoot", "exportfinished", "exportafoot", "importerr", "exporterr"});
        Tab tab = (Tab)this.getView().getControl("tabap");
        tab.addTabSelectListener((TabSelectListener)this);
    }

    public void tabSelected(TabSelectEvent tabSelectEvent) {
        String tabKey = tabSelectEvent.getTabKey();
        if ("tabboard".equals(tabKey)) {
            this.showTaskCount();
            this.showErrTaskCount();
            this.showCustomChartByMonth();
        } else if ("tabmgrmonitor".equals(tabKey)) {
            IPageCache pageCache = this.getView().getPageCache();
            if (Objects.isNull(pageCache.get("childpageid")) || !Objects.isNull(pageCache.get("oprtype"))) {
                this.showMonitorForm("tabmgrmonitor", "hies_taskinfo_mgr");
            } else {
                this.refreshChildPage();
            }
        }
    }

    public void click(EventObject evt) {
        super.click(evt);
        String key = ((Control)evt.getSource()).getKey();
        IPageCache pageCache = this.getView().getPageCache();
        Pair<Date, Date> queryDateRange = null;
        String oprType = "";
        switch (key) {
            case "importfinished": {
                queryDateRange = this.getQueryDateRange("taskdaterange");
                if (queryDateRange == null) {
                    return;
                }
                oprType = OprType.IMPORT.getValue();
                pageCache.put("status", TaskState.FINISHED.getValue());
                break;
            }
            case "importafoot": {
                queryDateRange = this.getQueryDateRange("taskdaterange");
                if (queryDateRange == null) {
                    return;
                }
                oprType = OprType.IMPORT.getValue();
                pageCache.put("status", TaskState.AFOOT.getValue());
                break;
            }
            case "exportfinished": {
                queryDateRange = this.getQueryDateRange("taskdaterange");
                if (queryDateRange == null) {
                    return;
                }
                oprType = OprType.EXPORT.getValue();
                pageCache.put("status", TaskState.FINISHED.getValue());
                break;
            }
            case "exportafoot": {
                queryDateRange = this.getQueryDateRange("taskdaterange");
                if (queryDateRange == null) {
                    return;
                }
                oprType = OprType.EXPORT.getValue();
                pageCache.put("status", TaskState.AFOOT.getValue());
                break;
            }
            case "importerr": {
                queryDateRange = this.getQueryDateRange("errdaterange");
                if (queryDateRange == null) {
                    return;
                }
                oprType = OprType.IMPORT.getValue();
                pageCache.put("errdata", Boolean.TRUE.toString());
                break;
            }
            case "exporterr": {
                queryDateRange = this.getQueryDateRange("errdaterange");
                if (queryDateRange == null) {
                    return;
                }
                oprType = OprType.EXPORT.getValue();
                pageCache.put("errdata", Boolean.TRUE.toString());
                break;
            }
            default: {
                if (queryDateRange != null) break;
                return;
            }
        }
        pageCache.put("oprtype", oprType);
        pageCache.put("queryStartDate", JSON.toJSONString((Object)queryDateRange.getLeft()));
        pageCache.put("queryEndDate", JSON.toJSONString((Object)queryDateRange.getRight()));
        Tab tab = (Tab)this.getView().getControl("tabap");
        tab.activeTab("tabmgrmonitor");
    }

    private Pair<Date, Date> getQueryDateRange(String ctrlName) {
        Pair<Date, Date> queryDateRange = this.buildQueryDateInfo(ctrlName);
        if (queryDateRange == null) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u65e5\u671f\u4e0d\u80fd\u4e3a\u7a7a\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u3002", (String)HiesCommonRes.DiaeMgrMonitorPlugin_1.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
            return null;
        }
        return queryDateRange;
    }

    private void refreshChildPage() {
        String childPageId = this.getPageCache().get("childpageid");
        IFormView childView = this.getView().getView(childPageId);
        BillList billlist = (BillList)childView.getControl("billlistap");
        billlist.refresh();
        this.getView().sendFormAction(childView);
    }

    private void putCustomParams(FormShowParameter formShowParameter) {
        String queryEndDate;
        String queryStartDate;
        String errData;
        String status;
        IPageCache pageCache = this.getPageCache();
        String oprType = pageCache.get("oprtype");
        if (HRStringUtils.isNotBlank((CharSequence)oprType)) {
            pageCache.remove("oprtype");
            formShowParameter.setCustomParam("oprtype", (Object)oprType);
        }
        if (HRStringUtils.isNotBlank((CharSequence)(status = pageCache.get("status")))) {
            pageCache.remove("status");
            formShowParameter.setCustomParam("status", (Object)status);
        }
        if (HRStringUtils.isNotBlank((CharSequence)(errData = pageCache.get("errdata")))) {
            pageCache.remove("errdata");
            formShowParameter.setCustomParam("errdata", (Object)errData);
        }
        if (HRStringUtils.isNotBlank((CharSequence)(queryStartDate = pageCache.get("queryStartDate")))) {
            pageCache.remove("queryStartDate");
            formShowParameter.setCustomParam("queryStartDate", (Object)queryStartDate);
        }
        if (HRStringUtils.isNotBlank((CharSequence)(queryEndDate = pageCache.get("queryEndDate")))) {
            pageCache.remove("queryEndDate");
            formShowParameter.setCustomParam("queryEndDate", (Object)queryEndDate);
        }
    }

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        Tab tab = (Tab)this.getView().getControl("tabap");
        tab.activeTab("tabboard");
        Date initEndDayOfMonthTime = DiaeMgrMonitorPlugin.localDateTranDate(LocalDate.now());
        Date initStartDayOfMonthTime = DiaeMgrMonitorPlugin.localDateTranDate(LocalDate.now().minusMonths(12L));
        DateRangeEdit trendDateRange = (DateRangeEdit)this.getView().getControl("trenddaterange");
        this.getModel().setValue(trendDateRange.getStartDateFieldKey(), (Object)initStartDayOfMonthTime);
        this.getModel().setValue(trendDateRange.getEndDateFieldKey(), (Object)initEndDayOfMonthTime);
        DateRangeEdit taskDateRange = (DateRangeEdit)this.getView().getControl("taskdaterange");
        this.getModel().setValue(taskDateRange.getStartDateFieldKey(), (Object)initStartDayOfMonthTime);
        this.getModel().setValue(taskDateRange.getEndDateFieldKey(), (Object)initEndDayOfMonthTime);
        DateRangeEdit errDateRange = (DateRangeEdit)this.getView().getControl("errdaterange");
        this.getModel().setValue(errDateRange.getStartDateFieldKey(), (Object)initStartDayOfMonthTime);
        this.getModel().setValue(errDateRange.getEndDateFieldKey(), (Object)initEndDayOfMonthTime);
    }

    public void beforeFieldPostBack(BeforeFieldPostBackEvent e) {
        super.beforeFieldPostBack(e);
        Object source = e.getSource();
        if (source instanceof DateRangeEdit) {
            boolean dateRangeErr = false;
            DateRangeEdit dateRangeEdit = (DateRangeEdit)source;
            String dateValue = (String)e.getValue();
            IFormView view = this.getView();
            if (HRStringUtils.isBlank((CharSequence)dateValue)) {
                view.showTipNotification(ResManager.loadKDString((String)"\u65e5\u671f\u4e0d\u80fd\u4e3a\u7a7a\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u3002", (String)HiesCommonRes.DiaeMgrMonitorPlugin_1.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
                dateRangeErr = true;
            } else {
                Date queryDateEnd;
                Pair<Date, Date> realQueryDate = this.getRealQueryDate(dateValue, dateRangeEdit);
                Date queryDateStart = (Date)realQueryDate.getLeft();
                if (DiaeMgrMonitorPlugin.isOver12Month(queryDateStart, queryDateEnd = (Date)realQueryDate.getRight())) {
                    dateRangeErr = true;
                    view.showTipNotification(ResManager.loadKDString((String)"\u65e5\u671f\u8303\u56f4\u4e0d\u80fd\u8d85\u8fc712\u4e2a\u6708\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u3002", (String)HiesCommonRes.DiaeMgrMonitorPlugin_4.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
                } else {
                    view.getFormShowParameter().setCustomParam("queryStartDate", (Object)queryDateStart);
                    view.getFormShowParameter().setCustomParam("queryEndDate", (Object)queryDateEnd);
                }
            }
            if (dateRangeErr) {
                FormShowParameter formShowParameter = view.getFormShowParameter();
                formShowParameter.setCustomParam("dateRangeErr", (Object)true);
            }
        }
    }

    private static boolean isOver12Month(Date queryDateStart, Date queryDateEnd) {
        LocalDate localDateStart = DiaeMgrMonitorPlugin.getLocalDate(queryDateStart);
        LocalDate localDateEnd = DiaeMgrMonitorPlugin.getLocalDate(queryDateEnd);
        LocalDate localDateBefore12 = localDateEnd.minusMonths(12L);
        return localDateBefore12.isAfter(localDateStart);
    }

    private Pair<Date, Date> getRealQueryDate(Object value, DateRangeEdit dateRangeEdit) {
        String startDateFieldKey = dateRangeEdit.getStartDateFieldKey();
        IDataEntityProperty prop = this.getModel().getProperty(startDateFieldKey);
        SimpleDateFormat simpleDateFormat = ((DateTimeProp)prop).getRegionType() == 2 ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") : new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = null;
            Date endDate = null;
            if (value != null && value.toString().split(",").length == 2) {
                startDate = simpleDateFormat.parse(value.toString().split(",")[0]);
                endDate = simpleDateFormat.parse(value.toString().split(",")[1]);
            }
            return Pair.of(startDate, endDate);
        }
        catch (ParseException e) {
            throw new KDBizException((Throwable)e, BosErrorCode.parse, new Object[]{e.toString()});
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        String fieldKey = e.getProperty().getName();
        if (fieldKey.startsWith("trenddaterange")) {
            String ctlName = "trenddaterange";
        } else if (fieldKey.startsWith("taskdaterange")) {
            String ctlName = "taskdaterange";
        } else if (fieldKey.startsWith("errdaterange")) {
            String ctlName = "errdaterange";
        } else {
            return;
        }
        switch (fieldKey) {
            case "trenddaterange_startdate": 
            case "trenddaterange_enddate": {
                if (Boolean.TRUE.equals(formShowParameter.getCustomParam("dateRangeErr"))) {
                    this.getModel().setValue("trenddaterange_startdate", (Object)"");
                    this.getModel().setValue("trenddaterange_enddate", (Object)"");
                    return;
                }
                this.showCustomChartByMonth();
                break;
            }
            case "taskdaterange_startdate": 
            case "taskdaterange_enddate": {
                if (Boolean.TRUE.equals(formShowParameter.getCustomParam("dateRangeErr"))) {
                    this.getModel().setValue("taskdaterange_startdate", (Object)"");
                    this.getModel().setValue("taskdaterange_enddate", (Object)"");
                    return;
                }
                this.showTaskCount();
                break;
            }
            case "errdaterange_startdate": 
            case "errdaterange_enddate": {
                if (Boolean.TRUE.equals(formShowParameter.getCustomParam("dateRangeErr"))) {
                    this.getModel().setValue("errdaterange_startdate", (Object)"");
                    this.getModel().setValue("errdaterange_enddate", (Object)"");
                    return;
                }
                this.showErrTaskCount();
            }
        }
    }

    private void showMonitorForm(String controlKey, String monitorPage) {
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.setBillFormId(monitorPage);
        listShowParameter.setFormId("bos_list");
        listShowParameter.setLookUp(false);
        OpenStyle openStyle = listShowParameter.getOpenStyle();
        openStyle.setShowType(ShowType.InContainer);
        openStyle.setTargetKey(controlKey);
        listShowParameter.setCustomParam("isHyperlink4Oprtype", (Object)Boolean.FALSE);
        this.putCustomParams((FormShowParameter)listShowParameter);
        this.getView().showForm((FormShowParameter)listShowParameter);
        this.getPageCache().put("childpageid", listShowParameter.getPageId());
    }

    private void showCustomChartByMonth() {
        Pair<Date, Date> trendDateRange = this.buildQueryDateInfo("trenddaterange");
        if (trendDateRange == null) {
            return;
        }
        PointLineChart chart = this.buildPointLineChart("pointlinechartap", ResManager.loadKDString((String)"\u5bfc\u5165\u5bfc\u51fa\u8d8b\u52bf\u56fe", (String)HiesCommonRes.DiaeMgrMonitorPlugin_7.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
        String xAxisName = "";
        List<QueryDateInfo> dateInfos = this.buildQueryDates((Date)trendDateRange.getLeft(), (Date)trendDateRange.getRight());
        List xInfos = dateInfos.stream().map(item -> SDF_YYYYMM.format(QueryDateInfo.access$000((QueryDateInfo)item))).collect(Collectors.toList());
        List<Object> dataNums = this.getNumsByMonth(dateInfos, OprType.IMPORT, false);
        CustomChart customChart = new CustomChart(this, chart, xInfos, xAxisName).invoke();
        Series importDataSeries = customChart.getImportDataSeries();
        importDataSeries.setPropValue("data", dataNums);
        List<Object> dataNums2 = this.getNumsByMonth(dateInfos, OprType.EXPORT, false);
        Series exportDataSeries = customChart.getExportDataSeries();
        exportDataSeries.setPropValue("data", dataNums2);
        chart.refresh();
    }

    private PointLineChart buildPointLineChart(String lineChartName, String titleName) {
        PointLineChart pointLineChart = (PointLineChart)this.getControl(lineChartName);
        pointLineChart.clearData();
        pointLineChart.setMerge(false);
        pointLineChart.setName(new LocaleString(titleName));
        return pointLineChart;
    }

    private List<Object> getNumsByMonth(List<QueryDateInfo> dateInfos, OprType oprType, boolean isIgnore) {
        ArrayList<Object> datas = new ArrayList<Object>(dateInfos.size());
        for (QueryDateInfo dateInfo : dateInfos) {
            QFilter dateFilter = QFilter.of((String)"stime >= ? and stime < ? and oprtype = ? and status = 'finished'", (Object[])new Object[]{QueryDateInfo.access$000((QueryDateInfo)dateInfo), QueryDateInfo.access$100((QueryDateInfo)dateInfo), oprType.getValue()});
            long count = DiaeMgrMonitorPlugin.getCountByFilter(new QFilter[]{dateFilter}, "hies_taskinfo");
            if (isIgnore) {
                datas.add(count != 0L ? Long.valueOf(count) : "");
                continue;
            }
            datas.add(count);
        }
        return datas;
    }

    private void showTaskCount() {
        Pair<Date, Date> taskDateRange = this.buildQueryDateInfo("taskdaterange");
        if (taskDateRange == null) {
            return;
        }
        HashBasedTable results = HashBasedTable.create();
        try (DataSet result = serviceHelper.queryDataSet("hies_taskinfo".concat("_count_groupbystatus"), "id, oprtype, status", new QFilter[]{new QFilter("stime", ">=", taskDateRange.getLeft()), new QFilter("stime", "<", taskDateRange.getRight())});
             DataSet dataSet = result.groupBy(new String[]{"oprtype", "status"}).count("count").finish();){
            for (Row data : dataSet) {
                results.put((Object)data.getString("oprtype"), (Object)data.getString("status"), (Object)data.getInteger("count"));
            }
        }
        Label label = (Label)this.getControl("importfinished");
        Integer count = (Integer)results.get((Object)OprType.IMPORT.getValue(), (Object)TaskState.FINISHED.getValue());
        label.setText(count != null ? count.toString() : "0");
        label = (Label)this.getControl("importafoot");
        count = (Integer)results.get((Object)OprType.IMPORT.getValue(), (Object)TaskState.AFOOT.getValue());
        label.setText(count != null ? count.toString() : "0");
        label = (Label)this.getControl("exportfinished");
        count = (Integer)results.get((Object)OprType.EXPORT.getValue(), (Object)TaskState.FINISHED.getValue());
        label.setText(count != null ? count.toString() : "0");
        label = (Label)this.getControl("exportafoot");
        count = (Integer)results.get((Object)OprType.EXPORT.getValue(), (Object)TaskState.AFOOT.getValue());
        label.setText(count != null ? count.toString() : "0");
    }

    private Pair<Date, Date> buildQueryDateInfo(String dateControlKey) {
        IFormView view = this.getView();
        DateRangeEdit headFieldEdit = (DateRangeEdit)view.getControl(dateControlKey);
        String key_headdatestart = headFieldEdit.getStartDateFieldKey();
        String key_headdateend = headFieldEdit.getEndDateFieldKey();
        Date queryStartDate = (Date)this.getModel().getValue(key_headdatestart);
        Date queryEndDate = (Date)this.getModel().getValue(key_headdateend);
        if (ObjectUtils.isEmpty((Object)queryEndDate)) {
            FormShowParameter formShowParameter = this.getView().getFormShowParameter();
            queryStartDate = (Date)formShowParameter.getCustomParam("queryStartDate");
            queryEndDate = (Date)formShowParameter.getCustomParam("queryEndDate");
        }
        if (ObjectUtils.isEmpty((Object)queryStartDate) || ObjectUtils.isEmpty((Object)queryEndDate)) {
            return null;
        }
        if (DiaeMgrMonitorPlugin.isOver12Month(queryStartDate, queryEndDate)) {
            return null;
        }
        return Pair.of((Object)queryStartDate, (Object)this.getQueryEndNextDate(queryEndDate));
    }

    private void showErrTaskCount() {
        Pair<Date, Date> errDateRange = this.buildQueryDateInfo("errdaterange");
        if (errDateRange == null) {
            return;
        }
        HashMap<String, Integer> results = new HashMap<String, Integer>(2);
        try (DataSet result = serviceHelper.queryDataSet("hies_taskinfo".concat("_count_groupbyerr"), "id, oprtype", new QFilter[]{new QFilter("stime", ">=", errDateRange.getLeft()), new QFilter("stime", "<", errDateRange.getRight()), new QFilter("issysexpt", "=", (Object)true), new QFilter("result", "=", (Object)"fail")});
             DataSet dataSet = result.groupBy(new String[]{"oprtype"}).count("count").finish();){
            for (Row data : dataSet) {
                results.put(data.getString("oprtype"), data.getInteger("count"));
            }
        }
        Label label = (Label)this.getControl("importerr");
        Integer count = (Integer)results.get(OprType.IMPORT.getValue());
        label.setText(count != null ? count.toString() : "0");
        label = (Label)this.getControl("exporterr");
        count = (Integer)results.get(OprType.EXPORT.getValue());
        label.setText(count != null ? count.toString() : "0");
    }

    private List<QueryDateInfo> buildQueryDates(Date queryStartDate, Date queryEndDate) {
        QueryDateInfo queryEndDateInfo;
        ArrayList<QueryDateInfo> dateInfos = new ArrayList<QueryDateInfo>(12);
        LocalDate localDateStart = DiaeMgrMonitorPlugin.getLocalDate(queryStartDate);
        LocalDate localDateEnd = DiaeMgrMonitorPlugin.getLocalDate(queryEndDate);
        Date dateStart = DiaeMgrMonitorPlugin.localDateTranDate(localDateStart);
        Date dateEnd = DiaeMgrMonitorPlugin.localDateTranDate(localDateEnd);
        int yearStart = localDateStart.getYear();
        int monthStart = localDateStart.getMonthValue();
        int yearEnd = localDateEnd.getYear();
        int monthEnd = localDateEnd.getMonthValue();
        if (yearEnd == yearStart) {
            if (monthStart == monthEnd) {
                dateInfos.add(new QueryDateInfo(queryStartDate, queryEndDate));
            } else {
                int nextMonth = monthStart + 1;
                Date nextDate = DiaeMgrMonitorPlugin.toDate(DiaeMgrMonitorPlugin.buildDateStr(yearStart, nextMonth));
                dateInfos.add(new QueryDateInfo(queryStartDate, nextDate));
                while (nextMonth < monthEnd) {
                    Date startDate = nextDate;
                    nextDate = DiaeMgrMonitorPlugin.toDate(DiaeMgrMonitorPlugin.buildDateStr(yearStart, ++nextMonth));
                    dateInfos.add(new QueryDateInfo(startDate, nextDate));
                }
                dateInfos.add(new QueryDateInfo(nextDate, queryEndDate));
            }
        } else {
            Date startDate;
            int nextMonth = monthStart + 1;
            if (nextMonth > 12) {
                nextMonth = 1;
                Date nextDate = DiaeMgrMonitorPlugin.toDate(yearEnd + "-01-01");
                dateInfos.add(new QueryDateInfo(queryStartDate, nextDate));
                while (nextMonth < monthEnd) {
                    Date startDate2 = nextDate;
                    nextDate = DiaeMgrMonitorPlugin.toDate(DiaeMgrMonitorPlugin.buildDateStr(yearEnd, ++nextMonth));
                    dateInfos.add(new QueryDateInfo(startDate2, nextDate));
                }
                dateInfos.add(new QueryDateInfo(nextDate, queryEndDate));
                return dateInfos;
            }
            Date nextDate = DiaeMgrMonitorPlugin.toDate(DiaeMgrMonitorPlugin.buildDateStr(yearStart, nextMonth));
            dateInfos.add(new QueryDateInfo(queryStartDate, nextDate));
            while (nextMonth < 12) {
                startDate = nextDate;
                nextDate = DiaeMgrMonitorPlugin.toDate(DiaeMgrMonitorPlugin.buildDateStr(yearStart, ++nextMonth));
                dateInfos.add(new QueryDateInfo(startDate, nextDate));
            }
            if (nextMonth == 12) {
                nextMonth = 1;
                startDate = nextDate;
                nextDate = DiaeMgrMonitorPlugin.toDate(yearEnd + "-01-01");
                dateInfos.add(new QueryDateInfo(startDate, nextDate));
            }
            while (nextMonth < monthEnd) {
                startDate = nextDate;
                nextDate = DiaeMgrMonitorPlugin.toDate(DiaeMgrMonitorPlugin.buildDateStr(yearEnd, ++nextMonth));
                dateInfos.add(new QueryDateInfo(startDate, nextDate));
            }
            dateInfos.add(new QueryDateInfo(nextDate, queryEndDate));
        }
        LOGGER.info("dateInfos:{}", dateInfos.stream().map(item -> DateUtils.getDate((Date)QueryDateInfo.access$000((QueryDateInfo)item)).concat("--->").concat(DateUtils.getDate((Date)QueryDateInfo.access$100((QueryDateInfo)item)))).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(dateInfos) && QueryDateInfo.access$100((QueryDateInfo)(queryEndDateInfo = (QueryDateInfo)dateInfos.get(dateInfos.size() - 1))).compareTo(QueryDateInfo.access$000((QueryDateInfo)queryEndDateInfo)) == 0) {
            dateInfos.remove(queryEndDateInfo);
        }
        return dateInfos;
    }

    private static Date localDateTranDate(LocalDate localDate) {
        ZoneId zoneId = KDDateUtils.getSysTimeZone().toZoneId();
        Instant instantStart = localDate.atStartOfDay(zoneId).toInstant();
        return Date.from(instantStart);
    }

    private static LocalDate getLocalDate(Date date) {
        ZoneId zoneId = KDDateUtils.getSysTimeZone().toZoneId();
        return date.toInstant().atZone(zoneId).toLocalDate();
    }

    private static String buildDateStr(int year, int month) {
        return year + "-" + (month < 10 ? "0" + month : Integer.valueOf(month)) + "-01";
    }

    private static Date toDate(String dateStr) {
        ZoneId zoneId = KDDateUtils.getSysTimeZone().toZoneId();
        LocalDate localDate = LocalDate.parse(dateStr);
        Instant instant = localDate.atStartOfDay(zoneId).toInstant();
        return Date.from(instant);
    }

    public static int getCountByFilter(QFilter[] filters, String entityName) {
        try (DataSet ds = serviceHelper.queryDataSet(entityName + "_getCount", "id", filters);){
            int n = ds.count("id", true);
            return n;
        }
    }

    private Date getQueryEndNextDate(Date queryEndDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(queryEndDate);
        cal.add(5, 1);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }

    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof BigDecimal) {
            return ((BigDecimal)o).toPlainString();
        }
        String s = o.toString();
        if (s == null) {
            return null;
        }
        return (s = s.trim()).length() == 0 ? null : s;
    }

    private void setToolTipStyle(Chart chart, String[] dataSeriesColors) {
        ArrayList<String> toolTipFuncPath = new ArrayList<String>();
        toolTipFuncPath.add("tooltip");
        toolTipFuncPath.add("formatter");
        chart.addFuncPath(toolTipFuncPath);
        chart.addTooltip("formatter", (Object)this.getFormatter(dataSeriesColors));
    }

    private String getFormatter(String[] dataSeriesColors) {
        StringBuilder formatter = new StringBuilder();
        formatter.append("function(params){\n\tlet res = \"\";\n\n");
        formatter.append("\tres += params[0].axisValue + '<br/>';\n");
        formatter.append("\t\tfor (var i = 0, l = params.length; i < l; i++) {\n").append("\t\tres += params[i].seriesName + ' : ';\n").append("\t\tif(params[i].value) {\n").append("\t\t\tres += params[i].value;\n").append("\t\t}else{\n").append("\t\t\tres += '0';\n").append("\t\t}\n");
        formatter.append("\t\tres += '<br/>';\n\t}\n\treturn res;\n}");
        return formatter.toString();
    }

    private void setAxisTickStyle(Axis axis) {
        HashMap<String, Constable> axisTick = new HashMap<String, Constable>();
        axisTick.put("interval", Integer.valueOf(0));
        axisTick.put("show", Boolean.FALSE);
        axis.setPropValue("axisTick", axisTick);
    }

    private void setAxisLineStyle(Axis axis) {
        HashMap axisLineMap = new HashMap();
        HashMap<String, String> lineStyleMap = new HashMap<String, String>(1);
        lineStyleMap.put("color", "#999999");
        axisLineMap.put("lineStyle", lineStyleMap);
        axis.setPropValue("axisLine", axisLineMap);
    }

    private void setSplitLineStyle(Axis axis) {
        HashMap<String, Serializable> splitLine = new HashMap<String, Serializable>();
        HashMap<String, String> lineStyle = new HashMap<String, String>();
        lineStyle.put("type", "dotted");
        lineStyle.put("color", "#E2E2E2");
        splitLine.put("lineStyle", lineStyle);
        splitLine.put("show", Boolean.TRUE);
        axis.setPropValue("splitLine", splitLine);
    }

    private Map<String, Object> setColorStyle(String color) {
        HashMap<String, Object> itemStyle = new HashMap<String, Object>(1);
        HashMap<String, String> normal = new HashMap<String, String>(1);
        normal.put("color", color);
        itemStyle.put("normal", normal);
        return itemStyle;
    }

    static /* synthetic */ Map access$200(DiaeMgrMonitorPlugin x0, String x1) {
        return x0.setColorStyle(x1);
    }

    static /* synthetic */ void access$300(DiaeMgrMonitorPlugin x0, Axis x1) {
        x0.setSplitLineStyle(x1);
    }

    static /* synthetic */ void access$400(DiaeMgrMonitorPlugin x0, Axis x1) {
        x0.setAxisLineStyle(x1);
    }

    static /* synthetic */ void access$500(DiaeMgrMonitorPlugin x0, Axis x1) {
        x0.setAxisTickStyle(x1);
    }

    static /* synthetic */ void access$600(DiaeMgrMonitorPlugin x0, Chart x1, String[] x2) {
        x0.setToolTipStyle(x1, x2);
    }
}
