/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DataEntityBase
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ArrayUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.BeforePackageDataEvent
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.list.column.ColumnDesc
 *  kd.bos.entity.list.column.DynamicTextColumnDesc
 *  kd.bos.entity.list.column.TextColumnDesc
 *  kd.bos.filter.FilterColumn
 *  kd.bos.filter.FilterContainer
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormMetadataCache
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BeforeCreateListColumnsArgs
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operatecol.OperationColItem
 *  kd.bos.list.BillList
 *  kd.bos.list.IListColumn
 *  kd.bos.list.IListView
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.column.ListOperationColumnDesc
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.bos.url.UrlService
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRQFilterHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hies.business.TaskInfoHelper
 *  kd.hr.hies.business.TaskService
 *  kd.hr.hies.common.HiesCommonRes
 *  kd.hr.hies.common.bo.MetaMenuBO
 *  kd.hr.hies.common.dto.TaskInfo
 *  kd.hr.hies.common.enu.Operate
 *  kd.hr.hies.common.enu.OprType
 *  kd.hr.hies.common.enu.TaskResult
 *  kd.hr.hies.common.enu.TaskState
 *  kd.hr.hies.common.util.ExcelUtil
 *  kd.hr.hies.formplugin.TaskInfoListPlugin$1
 *  kd.hr.hies.formplugin.TaskInfoListPlugin$CurListDataProvider
 *  org.apache.commons.collections4.CollectionUtils
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.commons.lang3.ObjectUtils
 */
package kd.hr.hies.formplugin;

import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DataEntityBase;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ArrayUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.BeforePackageDataEvent;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.list.column.ColumnDesc;
import kd.bos.entity.list.column.DynamicTextColumnDesc;
import kd.bos.entity.list.column.TextColumnDesc;
import kd.bos.filter.FilterColumn;
import kd.bos.filter.FilterContainer;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormMetadataCache;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operatecol.OperationColItem;
import kd.bos.list.BillList;
import kd.bos.list.IListColumn;
import kd.bos.list.IListView;
import kd.bos.list.ListShowParameter;
import kd.bos.list.column.ListOperationColumnDesc;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.bos.url.UrlService;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRQFilterHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hies.business.TaskInfoHelper;
import kd.hr.hies.business.TaskService;
import kd.hr.hies.common.HiesCommonRes;
import kd.hr.hies.common.bo.MetaMenuBO;
import kd.hr.hies.common.dto.TaskInfo;
import kd.hr.hies.common.enu.Operate;
import kd.hr.hies.common.enu.OprType;
import kd.hr.hies.common.enu.TaskResult;
import kd.hr.hies.common.enu.TaskState;
import kd.hr.hies.common.util.ExcelUtil;
import kd.hr.hies.formplugin.TaskInfoListPlugin;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

@ExcludeFromJacocoGeneratedReport
public final class TaskInfoListPlugin
extends HRDataBaseList
implements BeforeFilterF7SelectListener {
    private static final Log LOGGER = LogFactory.getLog(TaskInfoListPlugin.class);
    private static final HRBaseServiceHelper dbHelper = HRBaseServiceHelper.create((String)"hies_taskinfo");
    private Map<Long, DynamicObject> currentDataMap = new HashMap<Long, DynamicObject>(16);
    private boolean hasDownLoadPermission = true;
    private static final HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbss_cloud");

    private Set<String> getHrCloud() {
        Object[] dyns = serviceHelper.query("id,cloud,cloud.id,cloud.name,index", new QFilter[0]);
        if (ArrayUtils.isNotEmpty((Object[])dyns)) {
            return Arrays.stream(dyns).map(item -> item.getString("cloud.id")).collect(Collectors.toSet());
        }
        return new HashSet<String>(0);
    }

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        FilterContainer filterContainer = (FilterContainer)this.getView().getControl("filtercontainerap");
        filterContainer.addBeforeF7SelectListener((BeforeFilterF7SelectListener)this);
    }

    public void beforeF7Select(BeforeFilterF7SelectEvent evt) {
        Set<String> hrCloud;
        if ("app.bizcloud.name".equals(evt.getFieldName()) && CollectionUtils.isNotEmpty(hrCloud = this.getHrCloud())) {
            evt.getQfilters().add(new QFilter("id", "in", hrCloud));
        }
    }

    public void beforePackageData(BeforePackageDataEvent event) {
        super.beforePackageData(event);
        List pkIds = event.getPageData().stream().map(DataEntityBase::getPkValue).collect(Collectors.toList());
        QFilter[] pkIdsFilter = new QFilter[]{new QFilter("id", "in", pkIds)};
        Object[] datas = dbHelper.query("id, taskid, serialno, creator, createtime, inttor, stime, etime, result, totalcost, failamount, totalamount, alldatafileurl, extparam", pkIdsFilter);
        if (org.apache.commons.lang3.ArrayUtils.isEmpty((Object[])datas)) {
            return;
        }
        this.currentDataMap.clear();
        for (Object data : datas) {
            this.currentDataMap.put(data.getLong("id"), (DynamicObject)data);
        }
        if (this.isManager()) {
            this.hasDownLoadPermission = PermissionServiceHelper.checkPermission((Long)RequestContext.get().getCurrUserId(), (String)this.getView().getFormShowParameter().getAppId(), (String)"hies_taskinfo_mgr", (String)"2NJ5XVVCMBCL");
        }
    }

    public void packageData(PackageDataEvent event) {
        super.packageData(event);
        Object source = event.getSource();
        if (source instanceof DynamicTextColumnDesc) {
            DynamicTextColumnDesc columnDesc = (DynamicTextColumnDesc)source;
            String key = columnDesc.getKey();
            DynamicObject rowData = event.getRowData();
            switch (key) {
                case "operator": {
                    DynamicObject taskInfo = this.currentRowData(rowData);
                    DynamicObject creator = taskInfo.getDynamicObject("creator");
                    DynamicObject intTor = taskInfo.getDynamicObject("inttor");
                    if (ObjectUtils.isEmpty((Object)creator)) break;
                    String creatorName = creator.getLocaleString("name").toString();
                    if (ObjectUtils.isNotEmpty((Object)intTor)) {
                        String intTorName = intTor.getLocaleString("name").toString();
                        if (creatorName.equals(intTorName)) {
                            event.setFormatValue((Object)creatorName);
                            break;
                        }
                        event.setFormatValue((Object)String.format(ResManager.loadKDString((String)"\u53d1\u8d77\u4eba\uff1a%1$s /\u7ec8\u6b62\u4eba\uff1a%2$s", (String)HiesCommonRes.TaskInfoListPlugin_3.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]), creatorName, intTorName));
                        break;
                    }
                    event.setFormatValue((Object)creatorName);
                    break;
                }
                case "costtimedesc": {
                    DynamicObject taskInfo2 = this.currentRowData(rowData);
                    Date endTime = taskInfo2.getDate("etime");
                    if (endTime == null) break;
                    event.setFormatValue((Object)TaskInfoHelper.costTimeIgnoreMs((Long)endTime.getTime(), (Long)taskInfo2.getDate("stime").getTime()));
                }
            }
        } else {
            ColumnDesc columnDesc = (ColumnDesc)source;
            String key = columnDesc.getKey();
            if (columnDesc instanceof TextColumnDesc) {
                if ("oprpage".equals(key)) {
                    try {
                        MetaMenuBO metaMenuBO = (MetaMenuBO)SerializationUtils.fromJsonString((String)event.getFormatValue().toString(), MetaMenuBO.class);
                        LocaleString formName = FormMetadataCache.getFormConfig((String)metaMenuBO.getFormId()).getCaption();
                        event.setFormatValue((Object)formName.getLocaleValue());
                    }
                    catch (Exception e) {
                        event.setFormatValue((Object)"");
                    }
                }
            } else if (columnDesc instanceof ListOperationColumnDesc && Operate.pageFieldKey().equals(key)) {
                DynamicObject rowData = event.getRowData();
                List operationColItems = (List)event.getFormatValue();
                String status = rowData.getString(TaskState.pageFieldKey());
                String oprType = rowData.getString(OprType.pageFieldKey());
                String result = rowData.getString(TaskResult.pageFieldKey());
                Map operateBooleanMap = Operate.showOprStrategy((TaskResult)TaskResult.tranEnum((String)result), (TaskState)TaskState.tranEnum((String)status), (OprType)OprType.tranEnum((String)oprType));
                for (OperationColItem opItem : operationColItems) {
                    Long successedValidateBillCount;
                    JSONObject reqParamJsonObject;
                    Boolean isPreValidateModel;
                    String reqParam;
                    long failAmount;
                    long totalAmount;
                    DynamicObject taskInfo;
                    String allDataFileUrl;
                    boolean checkDownLoadPerm;
                    String operationKey = opItem.getOperationKey();
                    Operate operateObj = Operate.tranEnumForce((String)operationKey);
                    if (!Boolean.TRUE.equals(operateBooleanMap.get(operateObj)) || (checkDownLoadPerm = operateObj.isCheckDownLoadPerm()) && !this.hasDownLoadPermission) continue;
                    if (Operate.isDownExportFile((String)operationKey)) {
                        opItem.setOperationName(new LocaleString(ResManager.loadKDString((String)"\u6e90\u6587\u4ef6", (String)HiesCommonRes.TaskInfoListPlugin_16.resId(), (String)"hrmp-hies-common", (Object[])new Object[0])));
                    } else if (Operate.isDownAllDataFile((String)operationKey) && HRStringUtils.isBlank((CharSequence)(allDataFileUrl = (taskInfo = this.currentRowData(rowData)).getString("alldatafileurl"))) && (totalAmount = taskInfo.getLong("totalamount")) == (failAmount = taskInfo.getLong("failamount")) && HRStringUtils.isNotBlank((CharSequence)(reqParam = taskInfo.getString("extparam"))) && (Boolean.FALSE.equals(isPreValidateModel = (reqParamJsonObject = JSONObject.parseObject((String)reqParam)).getBoolean("isPreValidateModel")) || ObjectUtils.isEmpty((Object)(successedValidateBillCount = reqParamJsonObject.getLong("successedValidateBillCount"))) || totalAmount == (long)successedValidateBillCount.intValue() || 0 == successedValidateBillCount.intValue())) continue;
                    opItem.setVisible(true);
                }
            }
        }
    }

    private DynamicObject currentRowData(DynamicObject rowData) {
        return this.currentDataMap.get(rowData.getLong("id"));
    }

    public void filterColumnSetFilter(SetFilterEvent args) {
        super.filterColumnSetFilter(args);
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        Object oprType;
        String oprFormId;
        FormShowParameter formShowParameter;
        Object queryStartDate;
        super.setFilter(setFilterEvent);
        List qFilters = setFilterEvent.getQFilters();
        if (!this.isManager()) {
            qFilters.add(new QFilter("creator", "=", (Object)RequestContext.getOrCreate().getCurrUserId()));
        }
        if (!Objects.isNull(queryStartDate = (formShowParameter = this.getView().getFormShowParameter()).getCustomParam("queryStartDate"))) {
            qFilters.add(new QFilter("stime", ">=", SerializationUtils.fromJsonString((String)((String)queryStartDate), Date.class)));
            qFilters.add(new QFilter("stime", "<", SerializationUtils.fromJsonString((String)((String)formShowParameter.getCustomParam("queryEndDate")), Date.class)));
        }
        if (!Objects.isNull(formShowParameter.getCustomParam("errdata"))) {
            qFilters.add(new QFilter("issysexpt", "=", (Object)true));
            qFilters.add(new QFilter("result", "=", (Object)"fail"));
        }
        if (HRStringUtils.isNotBlank((CharSequence)(oprFormId = (String)formShowParameter.getCustomParam("oprFormId")))) {
            qFilters.add(new QFilter("entitynum", "=", (Object)oprFormId));
        }
        if (!Objects.isNull(oprType = formShowParameter.getCustomParam("oprtype"))) {
            qFilters.add(HRQFilterHelper.buildEql((String)"oprtype", (Object)oprType));
        }
    }

    public void filterContainerInit(FilterContainerInitArgs args) {
        super.filterContainerInit(args);
        IFormView view = this.getView();
        if (this.isLookup((IListView)view)) {
            return;
        }
        List commonFilterColumns = args.getCommonFilterColumns();
        Iterator commonItr = commonFilterColumns.iterator();
        while (commonItr.hasNext()) {
            Object status;
            FilterColumn column = (FilterColumn)commonItr.next();
            String fieldName = column.getFieldName();
            if ("oprtype".equals(fieldName)) {
                String oprFormId = (String)this.getView().getFormShowParameter().getCustomParam("oprFormId");
                if (HRStringUtils.isNotBlank((CharSequence)oprFormId)) {
                    commonItr.remove();
                    continue;
                }
                Object oprType = view.getFormShowParameter().getCustomParam("oprtype");
                if (Objects.isNull(oprType)) continue;
                column.setDefaultValue((String)oprType);
                continue;
            }
            if (!"status".equals(fieldName) || Objects.isNull(status = view.getFormShowParameter().getCustomParam("status"))) continue;
            column.setDefaultValue((String)status);
        }
        List schemeFilterColumns = args.getSchemeFilterColumns();
        Iterator schemeItr = schemeFilterColumns.iterator();
        while (schemeItr.hasNext()) {
            String oprFormId;
            FilterColumn column = (FilterColumn)schemeItr.next();
            String fieldName = column.getFieldName();
            if (!"oprtype".equals(fieldName) || !HRStringUtils.isNotBlank((CharSequence)(oprFormId = (String)this.getView().getFormShowParameter().getCustomParam("oprFormId")))) continue;
            schemeItr.remove();
        }
    }

    private boolean isLookup(IListView view) {
        boolean isLookup = false;
        if (view.getFormShowParameter() instanceof ListShowParameter) {
            ListShowParameter listShowParameter = (ListShowParameter)view.getFormShowParameter();
            isLookup = listShowParameter.isLookUp();
        }
        return isLookup;
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        if (args.isCancel()) {
            return;
        }
        ListSelectedRowCollection listSelectedData = args.getListSelectedData();
        if (CollectionUtils.isEmpty((Collection)listSelectedData)) {
            return;
        }
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        Operate operate = Operate.tranEnum((String)operateKey);
        if (operate == null) {
            return;
        }
        long pkId = (Long)listSelectedData.get(0).getPrimaryKeyValue();
        QFilter[] pkIdsFilter = new QFilter[]{new QFilter("id", "=", (Object)pkId)};
        DynamicObject taskInfo = dbHelper.queryOriginalOne("id, serialno, taskid, oprtype, oprpage, uploadfileurl, alldatafileurl, errdatafileurl, downloadfileurl, result, status", pkIdsFilter);
        String oprPage = taskInfo.getString("oprpage");
        MetaMenuBO metaMenuBO = (MetaMenuBO)SerializationUtils.fromJsonString((String)oprPage, MetaMenuBO.class);
        String oprFormId = metaMenuBO.getFormId();
        String fileGenNotice = ResManager.loadKDString((String)"\u6587\u4ef6\u6b63\u5728\u751f\u6210\uff0c\u751f\u6210\u6210\u529f\u540e\u5c06\u4f1a\u81ea\u52a8\u4e0b\u8f7d\u3002", (String)HiesCommonRes.TaskInfoListPlugin_2.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]);
        IFormView view = this.getView();
        switch (1.$SwitchMap$kd$hr$hies$common$enu$Operate[operate.ordinal()]) {
            case 1: {
                String uploadFileUrl = taskInfo.getString("uploadfileurl");
                if (HRStringUtils.isBlank((CharSequence)uploadFileUrl)) {
                    view.showErrorNotification(ResManager.loadKDString((String)"\u6587\u4ef6\u5df2\u635f\u574f\uff0c\u65e0\u6cd5\u4e0b\u8f7d\uff0c\u5982\u9700\u67e5\u770b\u6570\u636e\uff0c\u8bf7\u91cd\u65b0\u5bfc\u51fa\u3002", (String)HiesCommonRes.TaskInfoListPlugin_1.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
                    return;
                }
                this.downLoad(uploadFileUrl, oprFormId, true, false);
                break;
            }
            case 2: {
                String allDataFileUrl = taskInfo.getString("alldatafileurl");
                if (HRStringUtils.isBlank((CharSequence)allDataFileUrl)) {
                    view.showTipNotification(fileGenNotice);
                    return;
                }
                this.downLoad(allDataFileUrl, oprFormId, true, false);
                break;
            }
            case 3: {
                String errDataFileUrl = taskInfo.getString("errdatafileurl");
                if (HRStringUtils.isBlank((CharSequence)errDataFileUrl)) {
                    view.showTipNotification(fileGenNotice);
                    return;
                }
                this.downLoad(errDataFileUrl, oprFormId, true, false);
                break;
            }
            case 4: {
                String downloadFileUrl = taskInfo.getString("downloadfileurl");
                if (HRStringUtils.isBlank((CharSequence)downloadFileUrl)) {
                    view.showTipNotification(fileGenNotice);
                    return;
                }
                this.downLoad(downloadFileUrl, oprFormId, true, true);
                break;
            }
            case 5: {
                long taskPkId = taskInfo.getLong("id");
                String result = taskInfo.getString("result");
                String status = taskInfo.getString("status");
                LOGGER.info("\u7ec8\u6b62\u4efb\u52a1\u4fe1\u606f taskPkId:{} result\uff1a{} status\uff1a{}", new Object[]{taskPkId, result, status});
                String oprType = taskInfo.getString("oprtype");
                if (this.isManager()) {
                    FormShowParameter showParameter = new FormShowParameter();
                    showParameter.setFormId("hies_taskint");
                    showParameter.getOpenStyle().setShowType(ShowType.Modal);
                    showParameter.getCustomParams().put("oprtype", oprType);
                    CloseCallBack closeCallBack = new CloseCallBack(((Object)((Object)this)).getClass().getName(), "save_intreason");
                    showParameter.setCloseCallBack(closeCallBack);
                    this.getView().showForm(showParameter);
                    return;
                }
                this.doTerminate(taskPkId, oprType, view);
                this.refreshList();
            }
        }
    }

    private void downLoad(String filePatch, String oprFormId, boolean isTempFileModel, boolean isExport) {
        if (isTempFileModel) {
            String permItem = "4730fc9f000003ae";
            if (this.isManager()) {
                oprFormId = this.getView().getEntityId();
                permItem = "2NJ5XVVCMBCL";
            } else if (isExport) {
                permItem = "4730fc9f000004ae";
            }
            String url = ExcelUtil.getDownloadUrl((String)filePatch, (String)oprFormId, (String)permItem);
            this.getView().download(url);
        } else {
            this.getView().download(UrlService.getAttachmentFullUrl((String)filePatch));
        }
    }

    private void refreshList() {
        ListView listView = (ListView)this.getView();
        listView.refresh();
        listView.clearSelection();
    }

    private boolean doTerminate(long taskPkId, String oprType, IFormView view) {
        boolean terminatorFlag = TaskService.terminatorTask((String)String.valueOf(taskPkId), (String)oprType);
        if (terminatorFlag) {
            view.showSuccessNotification(ResManager.loadKDString((String)"\u4efb\u52a1\u5df2\u7ec8\u6b62\u3002", (String)HiesCommonRes.TaskInfoListPlugin_5.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
            return true;
        }
        view.showErrorNotification(ResManager.loadKDString((String)"\u4efb\u52a1\u7ec8\u6b62\u5931\u8d25\u3002", (String)HiesCommonRes.TaskInfoListPlugin_6.resId(), (String)"hrmp-hies-common", (Object[])new Object[0]));
        return false;
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        Object returnData = event.getReturnData();
        if (returnData == null) {
            return;
        }
        String actionId = event.getActionId();
        if ("save_intreason".equals(actionId)) {
            IFormView view = this.getView();
            IListView listView = (IListView)view;
            ListSelectedRowCollection listSelectedData = listView.getSelectedRows();
            ListSelectedRow listSelectedRow = listSelectedData.get(0);
            Object taskPkId = listSelectedRow.getPrimaryKeyValue();
            Map returnDataMap = (Map)returnData;
            boolean isTerminate = this.doTerminate((Long)taskPkId, (String)returnDataMap.get("oprtype"), view);
            if (isTerminate) {
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setId((Long)taskPkId);
                taskInfo.setIntreason(((OrmLocaleValue)returnDataMap.get("intreason")).getLocaleValue());
                TaskInfoHelper.doUpdate((TaskInfo)taskInfo);
            }
            BillList billlist = (BillList)this.getControl("billlistap");
            billlist.refresh();
        }
    }

    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        List columns = args.getListColumns();
        Object oprtypeHyperlinkDisabled = this.getView().getFormShowParameter().getCustomParam("isHyperlink4Oprtype");
        for (IListColumn iListColumn : columns) {
            String listFieldKey = iListColumn.getListFieldKey();
            if (!"oprpage".equals(listFieldKey)) continue;
            iListColumn.setHyperlink(Boolean.FALSE.booleanValue());
        }
    }

    public boolean isManager() {
        return "hies_taskinfo_mgr".equals(((ListView)this.getView()).getBillFormId());
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new CurListDataProvider(this.isManager(), null));
    }

    static /* synthetic */ Log access$100() {
        return LOGGER;
    }
}
