/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Lists
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.FilterContainerSearchClickArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.BillList
 *  kd.bos.list.IListView
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.SessionManager
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.query.QFilter$QFilterNest
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrcs.bussiness.servicehelper.perm.log.PermLogServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.perm.log.PermLogTaskServiceHelper
 *  kd.hr.hrcs.formplugin.common.HrcsFormpluginRes
 *  org.apache.commons.collections.CollectionUtils
 *  org.apache.commons.lang3.SerializationUtils
 */
package kd.hr.hrcs.formplugin.web.perm.log;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.FilterContainerSearchClickArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.BillList;
import kd.bos.list.IListView;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.SessionManager;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.bussiness.servicehelper.perm.log.PermLogServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.perm.log.PermLogTaskServiceHelper;
import kd.hr.hrcs.formplugin.common.HrcsFormpluginRes;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;

@ExcludeFromJacocoGeneratedReport
public final class PermLogListPlugin
extends HRDataBaseList {
    private static final Log LOGGER = LogFactory.getLog(PermLogListPlugin.class);
    public static final Set<String> quickSearchFieldConds = ImmutableSet.of((Object)"operator.username", (Object)"operator.number", (Object)"permfile.user.name", (Object)"permfile.user.number", (Object)"influuserentry.influuser_permfile.user.name", (Object)"influuserentry.influuser_permfile.user.number", (Object[])new String[]{"rolenumber", "influroleentry.influrole_rolenumber", "influroleentry.influrole_rolename"});
    public static final Set<String> excludeQuickSearchFieldConds = ImmutableSet.of((Object)"operator.username", (Object)"permfile.user.name", (Object)"influuserentry.influuser_permfile.user.name");
    private static final Set<String> roleLogType = ImmutableSet.builder().add((Object)"1010").add((Object)"1015").add((Object)"1020").add((Object)"1025").add((Object)"1030").add((Object)"1035").add((Object)"1040").build();
    private static final Set<String> userPermLogType = ImmutableSet.builder().add((Object)"1050").add((Object)"1060").add((Object)"2010").add((Object)"2015").add((Object)"2020").add((Object)"2030").add((Object)"2090").add((Object)"2095").add((Object)"2060").add((Object)"2065").add((Object)"2066").build();
    private static final Set<String> boDimMappingLogType = ImmutableSet.builder().add((Object)"4010").add((Object)"4015").add((Object)"4020").build();
    private static final Set<String> dataRuleLogType = ImmutableSet.builder().add((Object)"3015").build();

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)"processlog", (CharSequence)formOperate.getOperateKey())) {
            ListSelectedRowCollection coll = ((ListView)this.getView()).getSelectedRows();
            if (coll.size() > 1) {
                this.getView().showErrorNotification("please selected 1 row.");
                args.setCancel(true);
                return;
            }
            DynamicObject[] logTypes = PermLogTaskServiceHelper.getAllPermLogType();
            HashMap<Long, String> logTypeMap = new HashMap<Long, String>(16);
            for (DynamicObject logType : logTypes) {
                logTypeMap.put(logType.getLong("id"), logType.getString("handlerclass"));
            }
            for (ListSelectedRow row : coll) {
                long logId = (Long)row.getPrimaryKeyValue();
                DynamicObject permLogDyn = PermLogServiceHelper.getPermLog((long)logId);
                if (!logTypeMap.containsKey(permLogDyn.getDynamicObject("logtype").getLong("id"))) continue;
                try {
                    this.invoke((String)logTypeMap.get(permLogDyn.getDynamicObject("logtype").getLong("id")), "doHandler", logId);
                    this.getView().showSuccessNotification("success");
                }
                catch (Exception exp) {
                    LOGGER.error((Throwable)exp);
                    this.getView().showErrorNotification("exception:" + exp.getMessage());
                    args.setCancel(true);
                    return;
                }
            }
        }
    }

    private Object invoke(String clazzName, String methodName, long params) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = Class.forName(clazzName);
        Object instance = clazz.newInstance();
        Method method = clazz.getMethod(methodName, Long.TYPE);
        return method.invoke(instance, params);
    }

    public void afterDoOperation(AfterDoOperationEventArgs e) {
        String opKey;
        super.afterDoOperation(e);
        switch (opKey = e.getOperateKey()) {
            case "donothing_archiveset": {
                OperationResult operationResult = e.getOperationResult();
                if (null == operationResult || !operationResult.isSuccess()) break;
                this.showLogSetting();
                break;
            }
        }
    }

    private void showLogSetting() {
        FormShowParameter para = new FormShowParameter();
        para.getOpenStyle().setShowType(ShowType.Modal);
        para.setFormId("hrcs_permlogarchive_set");
        this.getView().showForm(para);
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        if ("number".equals(args.getFieldName())) {
            this.showDetail(args, false);
        } else if ("influusernumber".equals(args.getFieldName())) {
            this.showDetail(args, true);
        }
    }

    private void showDetail(HyperLinkClickArgs args, boolean isInfluusernumber) {
        BillList list = (BillList)args.getHyperLinkClickEvent().getSource();
        Object pkId = list.getFocusRowPkId();
        String newPageId = this.getView().getPageId() + "showForm" + pkId;
        String pageId = SessionManager.getCurrent().get(newPageId);
        IFormView view = SessionManager.getCurrent().getView(pageId);
        if (!HRStringUtils.isEmpty((String)pageId) && view != null) {
            args.setCancel(Boolean.TRUE.booleanValue());
            IClientViewProxy service = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
            service.addAction("activate", (Object)pageId);
            return;
        }
        HRBaseServiceHelper dynaPermApplyBillService = new HRBaseServiceHelper("hrcs_permlog");
        DynamicObject dyn = dynaPermApplyBillService.queryOne("logtype", pkId);
        DynamicObject logType = dyn.getDynamicObject("logtype");
        String logTypeNumber = logType.getString("number");
        if (roleLogType.contains(logTypeNumber)) {
            args.setCancel(Boolean.TRUE.booleanValue());
            this.showDetail("hrcs_permlog_role", pkId, isInfluusernumber, newPageId, logType);
        } else if (userPermLogType.contains(logTypeNumber)) {
            args.setCancel(Boolean.TRUE.booleanValue());
            this.showDetail("hrcs_permlog_userperm", pkId, false, newPageId, logType);
        } else if (dataRuleLogType.contains(logTypeNumber)) {
            args.setCancel(Boolean.TRUE.booleanValue());
            this.showDetail("hrcs_permlog_datarule", pkId, isInfluusernumber, newPageId, logType);
        } else if (boDimMappingLogType.contains(logTypeNumber)) {
            args.setCancel(Boolean.TRUE.booleanValue());
            this.showDetail("hrcs_permlog_bodimmapping", pkId, isInfluusernumber, newPageId, logType);
        }
    }

    private void showDetail(String formId, Object pkId, boolean isInfluusernumber, String newPageId, DynamicObject logType) {
        IListView listview = (IListView)this.getView();
        ListSelectedRow baseDataListRow = listview.getCurrentSelectedRowInfo();
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId(formId);
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setShowTitle(true);
        showParameter.setHasRight(true);
        showParameter.setStatus(OperationStatus.VIEW);
        String caption = String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u65e5\u5fd7\u8be6\u60c5", (String)HrcsFormpluginRes.PermLogListPlugin_0.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), logType.getString("name"));
        showParameter.setCaption(caption);
        showParameter.setCustomParam("caption", (Object)caption);
        showParameter.setCustomParam("pkId", pkId);
        showParameter.setCustomParam("isInfluusernumber", (Object)isInfluusernumber);
        showParameter.setCustomParam("logType", (Object)logType.getString("number"));
        showParameter.setCustomParam("ismergerows", (Object)false);
        showParameter.setPageId(newPageId);
        SessionManager.getCurrent().put(newPageId, newPageId);
        this.getView().showForm(showParameter);
    }

    public void setFilter(SetFilterEvent arg) {
        List qFilters = arg.getQFilters();
        if (CollectionUtils.isNotEmpty((Collection)qFilters)) {
            for (QFilter qFilter : qFilters) {
                QFilter cloneQFilter3;
                QFilter cloneQFilter2;
                QFilter cloneQFilter;
                String property = qFilter.getProperty();
                if ("1".equals(property)) {
                    String[] split;
                    String value = (String)qFilter.getValue();
                    if (!StringUtils.isNotBlank((CharSequence)value) || (split = value.split("#")).length != 2) continue;
                    LinkedHashSet<String> filterQuickSearchFieldConds = new LinkedHashSet<String>(12);
                    filterQuickSearchFieldConds.addAll(quickSearchFieldConds);
                    String[] split2 = value.split(",");
                    filterQuickSearchFieldConds.addAll(Arrays.asList(split2));
                    filterQuickSearchFieldConds.removeIf(excludeQuickSearchFieldConds::contains);
                    String extConds = String.join((CharSequence)",", filterQuickSearchFieldConds);
                    qFilter.__setValue((Object)extConds);
                    continue;
                }
                if ("permfile.user.name".equals(property)) {
                    cloneQFilter = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                    cloneQFilter2 = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                    QFilter cloneQFilter4 = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                    QFilter cloneQFilter5 = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                    PermLogListPlugin.warpQFilters(qFilter, "permfile.user.name", cloneQFilter);
                    PermLogListPlugin.warpQFilters(qFilter, "permfile.user.number", cloneQFilter2);
                    PermLogListPlugin.warpQFilters(qFilter, "influuserentry.influuser_permfile.user.name", cloneQFilter4);
                    PermLogListPlugin.warpQFilters(qFilter, "influuserentry.influuser_permfile.user.number", cloneQFilter5);
                    continue;
                }
                if ("permfile.id".equals(property)) {
                    cloneQFilter = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                    cloneQFilter2 = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                    cloneQFilter3 = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                    PermLogListPlugin.warpQFilters(qFilter, "permfile.user.id", cloneQFilter);
                    PermLogListPlugin.warpQFilters(qFilter, "influuserentry.influuser_permfile.id", cloneQFilter2);
                    PermLogListPlugin.warpQFilters(qFilter, "influuserentry.influuser_permfile.user.id", cloneQFilter3);
                    continue;
                }
                if (!"rolename".equals(property)) continue;
                cloneQFilter = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                cloneQFilter2 = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                cloneQFilter3 = (QFilter)SerializationUtils.clone((Serializable)qFilter);
                PermLogListPlugin.warpQFilters(qFilter, "rolenumber", cloneQFilter);
                PermLogListPlugin.warpQFilters(qFilter, "influroleentry.influrole_rolenumber", cloneQFilter2);
                PermLogListPlugin.warpQFilters(qFilter, "influroleentry.influrole_rolename", cloneQFilter3);
            }
        }
        ArrayList customQFilterList = Lists.newArrayList((Object[])new QFilter[]{new QFilter("hashandle", "=", (Object)"1")});
        arg.setCustomQFilters((List)customQFilterList);
    }

    private static void warpQFilters(QFilter qFilter, String targetProperty, QFilter cloneQFilter) {
        List nestsQFilters = cloneQFilter.getNests(true);
        if (CollectionUtils.isNotEmpty((Collection)nestsQFilters)) {
            String[] split = targetProperty.split("\\.");
            for (QFilter.QFilterNest nestsQFilter : nestsQFilters) {
                QFilter cloneNestsQFilterFilter;
                QFilter nestsQFilterFilter = nestsQFilter.getFilter();
                nestsQFilterFilter.__setProperty(split[0]);
                String nestsProperty = nestsQFilterFilter.getProperty();
                if (split.length == 3) {
                    cloneNestsQFilterFilter = (QFilter)SerializationUtils.clone((Serializable)nestsQFilterFilter);
                    cloneNestsQFilterFilter.__setProperty(split[0].concat(".").concat(split[1]));
                    cloneQFilter.or(cloneNestsQFilterFilter);
                    continue;
                }
                if (split.length != 4) continue;
                cloneNestsQFilterFilter = (QFilter)SerializationUtils.clone((Serializable)nestsQFilterFilter);
                cloneNestsQFilterFilter.__setProperty(split[0].concat(".").concat(split[1]));
                cloneQFilter.or(cloneNestsQFilterFilter);
                QFilter cloneNestsQFilterFilter4 = (QFilter)SerializationUtils.clone((Serializable)nestsQFilterFilter);
                cloneNestsQFilterFilter4.__setProperty(split[0].concat(".").concat(split[1]).concat(".").concat(split[2]));
                cloneQFilter.or(cloneNestsQFilterFilter4);
            }
        }
        cloneQFilter.__setProperty(targetProperty);
        qFilter.or(cloneQFilter);
    }

    public void filterContainerSearchClick(FilterContainerSearchClickArgs args) {
        super.filterContainerSearchClick(args);
    }
}
