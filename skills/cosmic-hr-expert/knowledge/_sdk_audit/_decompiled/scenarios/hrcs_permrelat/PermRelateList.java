/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.cache.CacheFactory
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.DBRoute
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.exception.BosErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.exception.KDException
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListColumnsArgs
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.BillList
 *  kd.bos.list.IListColumn
 *  kd.bos.list.IListView
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.schedule.api.JobInfo
 *  kd.bos.schedule.api.JobType
 *  kd.bos.schedule.form.JobForm
 *  kd.bos.schedule.form.JobFormInfo
 *  kd.bos.sqlscript.PreInsDataScriptBuilder
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrcs.bussiness.service.perm.dimension.PermRtSyncService
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dimension.PermRelateServiceHelper
 *  kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList$PermItemProvider
 *  org.apache.commons.lang3.tuple.Pair
 */
package kd.hr.hrcs.formplugin.web.perm.dimension;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import kd.bos.cache.CacheFactory;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.DBRoute;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.exception.BosErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.exception.KDException;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.IListColumn;
import kd.bos.list.IListView;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.schedule.api.JobInfo;
import kd.bos.schedule.api.JobType;
import kd.bos.schedule.form.JobForm;
import kd.bos.schedule.form.JobFormInfo;
import kd.bos.sqlscript.PreInsDataScriptBuilder;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.bussiness.service.perm.dimension.PermRtSyncService;
import kd.hr.hrcs.bussiness.servicehelper.perm.dimension.PermRelateServiceHelper;
import kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList;
import org.apache.commons.lang3.tuple.Pair;

@ExcludeFromJacocoGeneratedReport
public final class PermRelateList
extends HRDataBaseList {
    private static final Log LOGGER = LogFactory.getLog(PermRelateList.class);
    private static final String FIELD_PERM_ITEM = "mainpermitem";

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        Object[] primaryKeyValues = args.getListSelectedData().getPrimaryKeyValues();
        FormOperate source = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)"delete", (CharSequence)source.getOperateKey())) {
            QFilter filter = new QFilter("id", "in", (Object)primaryKeyValues);
            Set permRelateCfgs = PermRelateServiceHelper.queryPermRelates((QFilter)filter);
            this.getPageCache().put("deleteRows", SerializationUtils.toJsonString((Object)permRelateCfgs));
        } else if (HRStringUtils.equals((String)source.getOperateKey(), (String)"auth")) {
            ListShowParameter fsp = new ListShowParameter();
            fsp.setBillFormId("hrcs_permrelatcfg");
            fsp.setFormId("bos_list");
            fsp.setPageId("hrcs_permrelatcfg@" + this.getView().getPageId());
            fsp.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            this.getView().showForm((FormShowParameter)fsp);
        } else if (StringUtils.equals((CharSequence)"btnsycrole", (CharSequence)source.getOperateKey())) {
            MultiLangEnumBridge sycRoleName = new MultiLangEnumBridge("\u540c\u6b65\u89d2\u8272", "PermRelateList_06", "hrmp-hrcs-formplugin");
            MultiLangEnumBridge sycOpt = new MultiLangEnumBridge("\u540c\u6b65\u89d2\u8272\u64cd\u4f5c\u6267\u884c\u6210\u529f\u3002", "PermRelateList_07", "hrmp-hrcs-formplugin");
            MultiLangEnumBridge sycOpf = new MultiLangEnumBridge("\u540c\u6b65\u89d2\u8272\u64cd\u4f5c", "PermRelateList_08", "hrmp-hrcs-formplugin");
            ListSelectedRowCollection selectedRows = ((IListView)this.getView()).getSelectedRows();
            Set pkIdSet = selectedRows.stream().map(it -> Long.parseLong(String.valueOf(it.getPrimaryKeyValue()))).collect(Collectors.toSet());
            if (pkIdSet.isEmpty()) {
                ConfirmCallBackListener confirmCallBackListener = new ConfirmCallBackListener("syncAllRtPermRole", (IFormPlugin)this);
                String confirmTip = ResManager.loadKDString((String)"\u540c\u6b65\u5173\u8054\u6743\u9650\u9879\u5230\u76f8\u5173\u7684\u89d2\u8272\u4e2d\uff0c\u6570\u636e\u91cf\u5927\uff0c\u8bf7\u8c28\u614e\u64cd\u4f5c\uff0c\u786e\u8ba4\u662f\u5426\u540c\u6b65\uff1f", (String)"PermRelateList_05", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                this.getView().showConfirm(confirmTip, MessageBoxOptions.YesNo, ConfirmTypes.Delete, confirmCallBackListener);
                PermRtSyncService.writeOpLog((boolean)false, (String)this.getView().getFormShowParameter().getAppId(), (String)"hrcs_permrelat", (MultiLangEnumBridge)sycRoleName, (MultiLangEnumBridge)sycOpt, (MultiLangEnumBridge)sycOpf, (String)source.getOperateKey());
                return;
            }
            if (pkIdSet.size() > 10) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u80fd\u8d85\u51fa10\u884c\uff0c\u8bf7\u4fee\u6539\u3002", (String)"PermRelateList_02", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                PermRtSyncService.writeOpLog((boolean)false, (String)this.getView().getFormShowParameter().getAppId(), (String)"hrcs_permrelat", (MultiLangEnumBridge)sycRoleName, (MultiLangEnumBridge)sycOpt, (MultiLangEnumBridge)sycOpf, (String)source.getOperateKey());
                return;
            }
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_permrelat");
            DynamicObject[] permRelates = serviceHelper.query("entitytype,bizapp,mainpermitem,entryentity.entitytypeid,entryentity.app,entryentity.permitemid", new QFilter[]{new QFilter("id", "in", pkIdSet)});
            Pair relatePermInfoPair = PermRtSyncService.getRelatePermInfoPair((DynamicObject[])permRelates);
            List relatePermInfoList = (List)relatePermInfoPair.getKey();
            List mainPermInfoList = (List)relatePermInfoPair.getValue();
            if (relatePermInfoList.isEmpty()) {
                ConfirmCallBackListener confirmCallBackListener = new ConfirmCallBackListener("syncRoleBtn", (IFormPlugin)this);
                String confirmTip = ResManager.loadKDString((String)"\u5f53\u524d\u6743\u9650\u9879\u4e3a\u5141\u8bb8\u72ec\u7acb\u6388\u6743\uff0c\u4e0d\u5141\u8bb8\u540c\u6b65\u89d2\u8272\u3002", (String)"PermRelateList_03", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                this.getView().showConfirm(confirmTip, MessageBoxOptions.OK, ConfirmTypes.Default, confirmCallBackListener);
                PermRtSyncService.writeOpLog((boolean)false, (String)this.getView().getFormShowParameter().getAppId(), (String)"hrcs_permrelat", (MultiLangEnumBridge)sycRoleName, (MultiLangEnumBridge)sycOpt, (MultiLangEnumBridge)sycOpf, (String)source.getOperateKey());
                return;
            }
            long startTime = System.currentTimeMillis();
            LinkedHashMap resultRolePermMap = PermRtSyncService.calcRtPermRole((List)relatePermInfoList, (List)mainPermInfoList);
            LOGGER.info("permRelateList.beforeDoOperation calcRtPermRole cost time:{}", (Object)(System.currentTimeMillis() - startTime));
            if (CollectionUtils.isEmpty((Map)resultRolePermMap)) {
                ConfirmCallBackListener confirmCallBackListener = new ConfirmCallBackListener("syncRoleBtn", (IFormPlugin)this);
                String confirmTip = ResManager.loadKDString((String)"\u89d2\u8272\u5df2\u5305\u542b\u5173\u8054\u6743\u9650\u9879\uff0c\u65e0\u9700\u540c\u6b65\u3002", (String)"PermRelateList_04", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                this.getView().showConfirm(confirmTip, MessageBoxOptions.OK, ConfirmTypes.Default, confirmCallBackListener);
                PermRtSyncService.writeOpLog((boolean)false, (String)this.getView().getFormShowParameter().getAppId(), (String)"hrcs_permrelat", (MultiLangEnumBridge)sycRoleName, (MultiLangEnumBridge)sycOpt, (MultiLangEnumBridge)sycOpf, (String)source.getOperateKey());
                return;
            }
            FormShowParameter showParameter = new FormShowParameter();
            showParameter.setFormId("hrcs_syncroleperm");
            showParameter.getOpenStyle().setShowType(ShowType.Modal);
            showParameter.setCustomParam("roleInfo", (Object)SerializationUtils.toJsonString((Object)resultRolePermMap));
            showParameter.setCustomParam("roleCount", (Object)resultRolePermMap.keySet().size());
            showParameter.setCustomParam("permCount", (Object)relatePermInfoList.size());
            this.getView().showForm(showParameter);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        String operateKey = args.getOperateKey();
        if (HRStringUtils.equals((String)"delete", (String)operateKey) && args.getOperationResult().isSuccess()) {
            String deleteRowsStr = this.getPageCache().get("deleteRows");
            if (HRStringUtils.isEmpty((String)deleteRowsStr)) {
                return;
            }
            Set deleteRows = (Set)SerializationUtils.fromJsonString((String)deleteRowsStr, Set.class);
            PermRelateServiceHelper.deletePermRelateConfigs((Set)deleteRows);
        } else if ("exportscript".equals(operateKey) && args.getOperationResult().isSuccess()) {
            BillList billList = (BillList)this.getView().getControl("billlistap");
            ListSelectedRowCollection selectedRows = billList.getSelectedRows();
            if (selectedRows.isEmpty()) {
                return;
            }
            Object[] pkIds = selectedRows.getPrimaryKeyValues();
            this.generateSql(pkIds);
        }
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new PermItemProvider());
    }

    public void beforeShowBill(BeforeShowBillFormEvent evt) {
        evt.getParameter().setCloseCallBack(new CloseCallBack((IFormPlugin)this, "incPermTips"));
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        super.confirmCallBack(event);
        if (event.getResult() == MessageBoxResult.Yes && StringUtils.equals((CharSequence)"syncAllRtPermRole", (CharSequence)event.getCallBackId())) {
            this.startJob();
        }
    }

    private void startJob() {
        JobInfo jobInfo = new JobInfo();
        HashMap<String, Integer> params = new HashMap<String, Integer>(1);
        params.put("syncAll", 1);
        jobInfo.setParams(params);
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "syncAllRtPermRole");
        JobFormInfo jobFormInfo = new JobFormInfo(jobInfo);
        jobInfo.setAppId("hrcs");
        jobFormInfo.setCloseCallBack(closeCallBack);
        jobFormInfo.setCanBackground(true);
        jobInfo.setId(UUID.randomUUID().toString());
        jobInfo.setRunByLang(RequestContext.get().getLang());
        jobFormInfo.setTimeout(1200);
        jobFormInfo.setCanStop(false);
        jobInfo.setJobType(JobType.REALTIME);
        jobInfo.setTaskClassname("kd.hr.hrcs.bussiness.service.perm.dimension.HRRelatePermTask");
        LocaleString sycRoleName = ResManager.getLocaleString((String)"\u540c\u6b65\u89d2\u8272\u5173\u8054\u6743\u9650\u9879", (String)"PermRelateList_09", (String)"hrmp-hrcs-formplugin");
        jobInfo.setlName(sycRoleName);
        JobForm.dispatch((JobFormInfo)jobFormInfo, (IFormView)this.getView());
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        String actionId = evt.getActionId();
        Map returnData = (Map)evt.getReturnData();
        if (returnData == null) {
            return;
        }
        if (actionId.equals("incPermTips")) {
            if (returnData.containsKey("syncRole")) {
                Object roleInfo = returnData.get("resultRolePermMap");
                FormShowParameter showParameter = new FormShowParameter();
                showParameter.setFormId("hrcs_syncrolesel");
                showParameter.getOpenStyle().setShowType(ShowType.Modal);
                showParameter.setCustomParam("roleInfo", (Object)SerializationUtils.toJsonString(roleInfo));
                this.getView().showForm(showParameter);
            }
            if (returnData.get("changed") != null) {
                ListView listView = (ListView)this.getView();
                listView.clearSelection();
                listView.refresh();
            }
        }
    }

    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        List listColumns = args.getListColumns();
        String isShowFilter = (String)this.getView().getFormShowParameter().getCustomParam("isNotShowFilter");
        if (HRStringUtils.isNotEmpty((String)isShowFilter)) {
            for (IListColumn listColumn : listColumns) {
                if (!listColumn.getListFieldKey().equals("entitytype.number")) continue;
                listColumn.setHyperlink(false);
                return;
            }
        }
    }

    public void generateSql(Object[] ids) {
        try (TXHandle tx = TX.requiresNew();){
            try {
                HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_permrelat");
                DynamicObject[] query = serviceHelper.query("id,entryentity.issyspreset,entryentity.issynrole", new QFilter[]{new QFilter("id", "in", (Object)ids)});
                HashMap oldSynValueMap = Maps.newHashMapWithExpectedSize((int)query.length);
                for (DynamicObject dynamicObject : query) {
                    DynamicObjectCollection dynamicObjectCollection = dynamicObject.getDynamicObjectCollection("entryentity");
                    Map valueMap = oldSynValueMap.computeIfAbsent(dynamicObject.getLong("id"), k -> Maps.newHashMapWithExpectedSize((int)dynamicObjectCollection.size()));
                    for (DynamicObject row : dynamicObjectCollection) {
                        row.set("issyspreset", (Object)Character.valueOf('1'));
                        boolean isSynRole = row.getBoolean("issynrole");
                        valueMap.put(row.getLong("id"), isSynRole);
                        row.set("isSynRole", (Object)Character.valueOf('0'));
                    }
                }
                serviceHelper.save(query);
                ArrayList sqlList = Lists.newArrayListWithExpectedSize((int)(ids.length * 3));
                for (Object id : ids) {
                    String mainSql = this.execScript("T_HRCS_PERMRELAT", "fid,fentitytypeid,fpermitemid,fappid,fdescription,fcreatorid,fcreatetime,fmodifierid,fmodifytime", "fid = " + id);
                    String multiLangSql = this.execScript("T_HRCS_PERMRELAT_L", "fpkid,fid,flocaleid,fdescription", "fid = " + id + " and flocaleid = 'zh_CN'");
                    String entrySql = this.execScript("T_HRCS_PERMRELATENTRY", "fid,fentryid,fseq,fentitytypeid,fappid,fpermitemid,fissyspreset,fissynrole", "fid = " + id);
                    sqlList.add(mainSql);
                    sqlList.add(multiLangSql);
                    sqlList.add(entrySql);
                }
                String sql = String.join((CharSequence)"\n", sqlList);
                this.exportFile(sql);
                for (DynamicObject dynamicObject : query) {
                    Map valueMap = (Map)oldSynValueMap.get(dynamicObject.getLong("id"));
                    DynamicObjectCollection dynamicObjectCollection = dynamicObject.getDynamicObjectCollection("entryentity");
                    for (DynamicObject row : dynamicObjectCollection) {
                        row.set("isSynRole", valueMap.get(row.getLong("id")));
                    }
                }
                serviceHelper.save(query);
            }
            catch (Exception exception) {
                tx.markRollback();
                throw new KDBizException(exception.getMessage());
            }
        }
    }

    private String execScript(String fromField, String selectField, String whereField) {
        Map map;
        PreInsDataScriptBuilder builder = new PreInsDataScriptBuilder();
        try {
            map = builder.genInsertSQLScript(DBRoute.of((String)"hr"), fromField, selectField, whereField, "", "");
        }
        catch (Exception var49) {
            throw new KDBizException((Throwable)var49, BosErrorCode.bOS, new Object[]{String.format("KSQLExpPlugin Error:%s.", var49.getMessage())});
        }
        return (String)map.get("sql");
    }

    private void exportFile(String sql) {
        String sqlFileName = "kd_X.X.X_hrcs_permrelat.sql";
        try (ByteArrayInputStream ins = new ByteArrayInputStream(sql.getBytes(StandardCharsets.UTF_8));){
            String url = CacheFactory.getCommonCacheFactory().getTempFileCache().saveAsUrl(sqlFileName, (InputStream)ins, 5000);
            this.getView().openUrl(url);
        }
        catch (Exception var51) {
            throw new KDException((Throwable)var51, BosErrorCode.downloadFailed, new Object[]{String.format("KSQLExpPlugin error:%s.", var51.getMessage())});
        }
    }

    static /* synthetic */ Log access$000() {
        return LOGGER;
    }
}
