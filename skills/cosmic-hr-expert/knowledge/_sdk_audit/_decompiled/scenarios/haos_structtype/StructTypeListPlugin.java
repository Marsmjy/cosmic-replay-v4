/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.mvc.list.ListView
 *  kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hr.haos.formplugin.web.otherstruct.structtype;

import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.mvc.list.ListView;
import kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public final class StructTypeListPlugin
extends HRDataBaseList
implements ItemClickListener {
    public static final String DELETEALLREL = "deleteallrel";

    public void beforeItemClick(BeforeItemClickEvent evt) {
        super.itemClick((ItemClickEvent)evt);
        if (this.getView() instanceof ListView) {
            ListView listView = (ListView)this.getView();
            ListSelectedRowCollection rows = listView.getSelectedRows();
            String key = evt.getItemKey();
            if ("chgname".equals(key)) {
                if (rows.size() == 0) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u6761\u6570\u636e\u3002", (String)"StructTypeListPlugin_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                    evt.setCancel(true);
                    return;
                }
                if (rows.size() > 1) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u53ea\u80fd\u9009\u4e2d\u4e00\u884c\u6570\u636e\u8fdb\u884c\u64cd\u4f5c\u3002", (String)"StructTypeListPlugin_3", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                    evt.setCancel(true);
                    return;
                }
                OtherStructTypeService otherStructTypeService = new OtherStructTypeService();
                ListSelectedRow row = rows.get(0);
                Object primaryKeyValue = row.getPrimaryKeyValue();
                DynamicObject typeDy = otherStructTypeService.getStructTypeData(primaryKeyValue);
                if (typeDy != null && !"1".equals(typeDy.get("enable"))) {
                    ListSelectedRow listSelectedRow = listView.getCurrentSelectedRowInfo();
                    this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"%s\uff1a\u53ea\u6709\u53ef\u7528\u72b6\u6001\u624d\u80fd\u53d8\u66f4\u540d\u79f0\u3002", (String)"StructTypeListPlugin_4", (String)"hrmp-haos-formplugin", (Object[])new Object[0]), listSelectedRow.getNumber() + listSelectedRow.getName()));
                    evt.setCancel(true);
                }
            } else if ("tbldeleteallrel".equals(key)) {
                evt.setCancel(true);
                String rowKey = rows.stream().map(data -> data.getRowKey() + 1).sorted().map(String::valueOf).collect(Collectors.joining(","));
                ConfirmCallBackListener confirmCallBackListener = new ConfirmCallBackListener("haos_structtype", (IFormPlugin)this);
                this.getView().showConfirm(ResManager.loadKDString((String)"\u5220\u9664\u9009\u4e2d\u7684\u7b2c\u201c%s\u201d\u884c\u8bb0\u5f55\u540e\u5c06\u65e0\u6cd5\u6062\u590d\uff0c\u786e\u5b9a\u8981\u5220\u9664\u5417\uff1f", (String)"StructTypeListPlugin_6", (String)"hrmp-haos-formplugin", (Object[])new Object[]{rowKey}), MessageBoxOptions.OKCancel, confirmCallBackListener);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        String key = afterDoOperationEventArgs.getOperateKey();
        if (this.getView() instanceof ListView) {
            ListView listView = (ListView)this.getView();
            ListSelectedRowCollection rows = listView.getSelectedRows();
            if ("chgname".equals(key)) {
                ListSelectedRow row = rows.get(0);
                Object primaryKeyValue = row.getPrimaryKeyValue();
                FormShowParameter showParameter = new FormShowParameter();
                showParameter.setFormId("haos_structtypenamechg");
                showParameter.setCustomParam("pkid", primaryKeyValue);
                showParameter.getOpenStyle().setShowType(ShowType.Modal);
                showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "haos_structtypenamechg"));
                showParameter.setCaption(ResManager.loadKDString((String)"\u53d8\u66f4\u540d\u79f0", (String)"StructTypeListPlugin_1", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                this.getView().showForm(showParameter);
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        String actionId = closedCallBackEvent.getActionId();
        Object returnData = closedCallBackEvent.getReturnData();
        if ("haos_structtypenamechg".equals(actionId) && returnData instanceof Map) {
            Map returnDataMap = (Map)returnData;
            String success = (String)returnDataMap.get("success");
            if (Boolean.parseBoolean(success)) {
                this.getView().showSuccessNotification((String)returnDataMap.get("message"));
            } else {
                this.getView().showTipNotification((String)returnDataMap.get("message"));
            }
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        super.confirmCallBack(messageBoxClosedEvent);
        if ("haos_structtype".equals(messageBoxClosedEvent.getCallBackId()) && MessageBoxResult.Yes.equals((Object)messageBoxClosedEvent.getResult())) {
            this.getView().invokeOperation(DELETEALLREL);
            this.getView().invokeOperation("refresh");
        }
    }
}

