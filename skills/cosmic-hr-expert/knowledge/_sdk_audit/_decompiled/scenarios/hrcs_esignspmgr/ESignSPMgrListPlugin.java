/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.list.column.ColumnDesc
 *  kd.bos.entity.operate.result.OperateInfo
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.validate.ValidateResult
 *  kd.bos.entity.validate.ValidateResultCollection
 *  kd.bos.filter.FilterColumn
 *  kd.bos.filter.FilterContainer
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.form.field.events.BeforeFilterF7SelectListener
 *  kd.bos.form.field.events.ListExpandEvent
 *  kd.bos.form.field.events.ListExpandListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.IListView
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.servicehelper.operation.OperationServiceHelper
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignDBServiceServiceHelper
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper
 *  kd.hr.hrcs.formplugin.common.ComPrompts
 *  kd.hr.hrcs.formplugin.common.HrcsFormpluginRes
 *  org.apache.commons.collections4.CollectionUtils
 *  org.apache.commons.lang3.ObjectUtils
 */
package kd.hr.hrcs.formplugin.web.esign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.list.column.ColumnDesc;
import kd.bos.entity.operate.result.OperateInfo;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.validate.ValidateResult;
import kd.bos.entity.validate.ValidateResultCollection;
import kd.bos.filter.FilterColumn;
import kd.bos.filter.FilterContainer;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.form.field.events.BeforeFilterF7SelectListener;
import kd.bos.form.field.events.ListExpandEvent;
import kd.bos.form.field.events.ListExpandListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.IListView;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignDBServiceServiceHelper;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper;
import kd.hr.hrcs.formplugin.common.ComPrompts;
import kd.hr.hrcs.formplugin.common.HrcsFormpluginRes;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

public final class ESignSPMgrListPlugin
extends HRDataBaseList
implements BeforeFilterF7SelectListener,
ListExpandListener {
    private static final Log LOGGER = LogFactory.getLog(ESignSPMgrListPlugin.class);
    private static final String DO_DEBUG = "dodebug";
    private static final String DO_ENABLE = "doenable";
    protected boolean closeConfirmStatus = false;

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        FilterContainer filterContainer = (FilterContainer)this.getView().getControl("filtercontainerap");
        filterContainer.addBeforeF7SelectListener((BeforeFilterF7SelectListener)this);
    }

    public void beforeF7Select(BeforeFilterF7SelectEvent evt) {
        if ("app.bizcloud.name".equals(evt.getFieldName())) {
            // empty if block
        }
    }

    public void filterContainerInit(FilterContainerInitArgs args) {
        super.filterContainerInit(args);
        IFormView view = this.getView();
        if (this.isLookup((IListView)view)) {
            return;
        }
        List commonFilterColumns = args.getCommonFilterColumns();
        for (FilterColumn column : commonFilterColumns) {
            String fieldName = column.getFieldName();
            if (!"enable".equals(fieldName)) continue;
            column.setDefaultValue(null);
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

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        if ("showapptab".equals(args.getFieldName())) {
            this.getView().getPageCache().put("showapptab", "showapptab");
        } else {
            this.getView().getPageCache().put("showapptab", "");
        }
    }

    public void expandClick(ListExpandEvent e) {
        super.expandClick(e);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        if (args.isCancel()) {
            return;
        }
        ListSelectedRowCollection listSelectedData = args.getListSelectedData();
        if (CollectionUtils.isEmpty((Collection)listSelectedData)) {
            return;
        }
        ListSelectedRow listSelectedRow = listSelectedData.get(0);
        long pkId = (Long)listSelectedRow.getPrimaryKeyValue();
        AbstractOperate op = (AbstractOperate)args.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "dodebug": {
                if (listSelectedData.size() > 1) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u652f\u6301\u6279\u91cf\u5728\u7ebf\u8c03\u8bd5\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u6570\u636e\u3002", (String)HrcsFormpluginRes.ESignSPMgrListPlugin_1.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    args.setCancel(true);
                    break;
                }
                DynamicObject dynamicObject = HRCSESignDBServiceServiceHelper.eSignSPMgrService.loadSingle((Object)pkId);
                DynamicObjectCollection entryEntitys = dynamicObject.getDynamicObjectCollection("entryentity");
                HRCSESignSPMgrServiceHelper.doDebug((String)listSelectedRow.getName(), (DynamicObjectCollection)entryEntitys, (IFormView)this.getView());
                break;
            }
            case "enable": {
                DynamicObjectCollection eSignApps;
                if (listSelectedData.size() > 1) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u7cfb\u7edf\u4ec5\u652f\u6301\u542f\u7528\u4e00\u4e2a\u7535\u5b50\u7b7e\u670d\u52a1\u5546\uff0c\u60a8\u9009\u62e9\u4e86\u591a\u6761\u6570\u636e\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u6570\u636e\u3002", (String)HrcsFormpluginRes.ESignSPMgrListPlugin_2.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    args.setCancel(true);
                    break;
                }
                ListSelectedRow row = args.getListSelectedData().get(0);
                Long curSelectPkId = (Long)row.getPrimaryKeyValue();
                DynamicObject curSelectDyn = HRCSESignDBServiceServiceHelper.eSignSPMgrService.loadSingle((Object)curSelectPkId);
                if ("1".equals(curSelectDyn.getString("enable"))) {
                    this.getView().showTipNotification(ComPrompts.isEnabled());
                    args.setCancel(true);
                    break;
                }
                if (!HRCSESignSPMgrServiceHelper.ignoreAppCfgApp.contains(curSelectPkId) && CollectionUtils.isEmpty((Collection)(eSignApps = curSelectDyn.getDynamicObjectCollection("entryentity1")))) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u96c6\u6210\u5e94\u7528\u914d\u7f6e\u4fe1\u606f\u4e3a\u7a7a\uff0c\u65e0\u6cd5\u542f\u7528\u3002\u8bf7\u5148\u5b8c\u6210\u96c6\u6210\u5e94\u7528\u4fe1\u606f\u914d\u7f6e\u540e\u518d\u64cd\u4f5c\u3002", (String)HrcsFormpluginRes.ESignSPMgrListPlugin_3.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    args.setCancel(true);
                    break;
                }
                Long onlineSpId = HRCSESignSPMgrServiceHelper.getOnlineSpId();
                if (!ObjectUtils.isNotEmpty((Object)onlineSpId)) break;
                ConfirmCallBackListener confirmListener = new ConfirmCallBackListener(DO_ENABLE, (IFormPlugin)this);
                this.getView().showConfirm(String.format(ResManager.loadKDString((String)"\u7cfb\u7edf\u4ec5\u652f\u6301\u542f\u7528\u4e00\u4e2a\u7535\u5b50\u7b7e\u670d\u52a1\u5546\uff0c\u786e\u5b9a\u542f\u7528\u201c%s\u201d\u5417\uff1f", (String)HrcsFormpluginRes.ESignSPMgrListPlugin_4.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), HRCSESignSPMgrServiceHelper.getSpName((Object)curSelectPkId)), null, MessageBoxOptions.OKCancel, null, confirmListener, null, onlineSpId + "|" + curSelectPkId);
                args.setCancel(true);
                break;
            }
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        if (HRStringUtils.equals((String)DO_ENABLE, (String)event.getCallBackId()) && MessageBoxResult.Yes.equals((Object)event.getResult())) {
            OperationResult enabledOpResult;
            this.closeConfirmStatus = true;
            String customValue = event.getCustomVaule();
            String[] split = customValue.split("\\|");
            String onlineSpId = split[0];
            String curSelectPkId = split[1];
            DynamicObject[] dyns = HRCSESignDBServiceServiceHelper.eSignSPMgrService.loadDynamicObjectArray(new Object[]{Long.valueOf(onlineSpId), Long.valueOf(curSelectPkId)});
            AtomicReference<String> oprNumber = new AtomicReference<String>("");
            ArrayList disabledDyns = new ArrayList(1);
            ArrayList enabledDyns = new ArrayList(1);
            ArrayList disabledNumbers = new ArrayList(8);
            Arrays.stream(dyns).forEach(item -> {
                if (onlineSpId.equals(item.getString("id"))) {
                    disabledDyns.add(item);
                    disabledNumbers.add(item.getString("number"));
                } else {
                    enabledDyns.add(item);
                    oprNumber.set(item.getString("number"));
                }
            });
            OperationResult disabledOpResult = null;
            if (HRCollUtil.isNotEmpty(disabledDyns)) {
                disabledOpResult = OperationServiceHelper.executeOperate((String)"disable", (String)"hrcs_esignspmgr", (DynamicObject[])disabledDyns.toArray(new DynamicObject[0]), (OperateOption)OperateOption.create());
            }
            ArrayList<String> errMsgs = new ArrayList<String>(8);
            if (disabledOpResult != null && !disabledOpResult.isSuccess()) {
                ESignSPMgrListPlugin.dealInvokeOpResult(disabledOpResult, errMsgs);
                errMsgs.add(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u7801\u201c%1$s\u201d\uff1a\u542f\u7528\u5931\u8d25\uff0c\u5176\u4ed6\u5df2\u542f\u7528\u7684\u6570\u636e\u7981\u7528\u5931\u8d25\uff0c\u7cfb\u7edf\u4ec5\u652f\u6301\u542f\u7528\u4e00\u4e2a\u7535\u5b50\u7b7e\u670d\u52a1\u5546", (String)HrcsFormpluginRes.ESignSPMgrListPlugin_10.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), oprNumber.get()));
                String title = ResManager.loadKDString((String)"\u64cd\u4f5c\u5931\u8d25", (String)HrcsFormpluginRes.ESignSPMgrListPlugin_8.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                this.getView().showConfirm(title, String.join((CharSequence)System.lineSeparator(), errMsgs), MessageBoxOptions.None, ConfirmTypes.Default, null);
                ((IListView)this.getView()).refresh();
                return;
            }
            if (HRCollUtil.isNotEmpty(enabledDyns) && (enabledOpResult = OperationServiceHelper.executeOperate((String)"enable", (String)"hrcs_esignspmgr", (DynamicObject[])enabledDyns.toArray(new DynamicObject[0]), (OperateOption)OperateOption.create())) != null && !enabledOpResult.isSuccess()) {
                ESignSPMgrListPlugin.dealInvokeOpResult(enabledOpResult, errMsgs);
                this.getView().showTipNotification(String.join((CharSequence)System.lineSeparator(), errMsgs));
                ((IListView)this.getView()).refresh();
                return;
            }
            ((IListView)this.getView()).refresh();
        }
    }

    private static void dealInvokeOpResult(OperationResult opResult, List<String> errMsgs) {
        List validateErrors;
        ValidateResultCollection validateResult = opResult.getValidateResult();
        if (!HRObjectUtils.isEmpty((Object)validateResult) && HRCollUtil.isNotEmpty((Collection)(validateErrors = validateResult.getValidateErrors()))) {
            for (ValidateResult validateError : validateErrors) {
                List allErrorInfo = validateError.getAllErrorInfo();
                List singleErrs = allErrorInfo.stream().map(OperateInfo::getMessage).collect(Collectors.toList());
                errMsgs.add(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u7801\u201c%1$s\u201d\uff1a%2$s", (String)HrcsFormpluginRes.ESignSPMgrListPlugin_9.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), String.join((CharSequence)",", opResult.getBillNos().values()), String.join((CharSequence)",", singleErrs)));
            }
            return;
        }
        errMsgs.add(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7f16\u7801\u201c%1$s\u201d\uff1a%2$s", (String)HrcsFormpluginRes.ESignSPMgrListPlugin_9.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), opResult.getBillNos().values(), opResult.getMessage()));
    }

    public void packageData(PackageDataEvent event) {
        super.packageData(event);
        Object source = event.getSource();
        ColumnDesc columnDesc = (ColumnDesc)source;
        String key = columnDesc.getKey();
        if ("showapptab".equals(key)) {
            event.setFormatValue((Object)ResManager.loadKDString((String)"\u96c6\u6210\u5e94\u7528\u914d\u7f6e", (String)HrcsFormpluginRes.ESignSPMgrListPlugin_7.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        }
    }
}
