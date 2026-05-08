/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrptmc.business.calfield.CalculateFieldService
 *  kd.hr.hrptmc.common.model.anobj.AnObjGroupField
 *  kd.hr.hrptmc.common.model.calfield.CalculateFieldBo
 *  kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCommonEdit
 *  kd.hr.hrptmc.formplugin.web.util.CalculateFieldPageUtil
 */
package kd.hr.hrptmc.formplugin.web.anobj;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.plugin.IFormPlugin;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrptmc.business.calfield.CalculateFieldService;
import kd.hr.hrptmc.common.model.anobj.AnObjGroupField;
import kd.hr.hrptmc.common.model.calfield.CalculateFieldBo;
import kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCommonEdit;
import kd.hr.hrptmc.formplugin.web.util.CalculateFieldPageUtil;

public final class AnalyseObjectCalculateConfigEdit
extends AnalyseObjectCommonEdit {
    public void customEvent(CustomEventArgs args) {
        if (HRStringUtils.equals((String)args.getEventName(), (String)"addCalField")) {
            this.addCalField(args);
        } else if (HRStringUtils.equals((String)args.getEventName(), (String)"modifyCalField")) {
            this.openModifyCalFieldPage(args);
        } else if (HRStringUtils.equals((String)args.getEventName(), (String)"deleteCalField")) {
            this.deleteCalField(args);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        super.confirmCallBack(event);
        int result = event.getResult().getValue();
        if (HRStringUtils.equals((String)event.getCallBackId(), (String)"deleteCalField") && result == MessageBoxResult.Yes.getValue()) {
            this.deleteCalFieldCustomEvent();
        }
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        Object returnData = event.getReturnData();
        if (returnData == null) {
            return;
        }
        if (HRStringUtils.equals((String)"closeForAddCalFieldConfig", (String)event.getActionId())) {
            List calculateFields = JSON.parseArray((String)returnData.toString(), CalculateFieldBo.class);
            CustomControl customcontrol = (CustomControl)this.getView().getControl("customcontrolap");
            HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
            dataMap.put("method", "setAllCalculateFields");
            dataMap.put("calFields", calculateFields);
            List refFieldAliasList = this.commonProcessor.getAllRefFieldAliasByCalField(calculateFields);
            dataMap.put("refFieldAliasList", refFieldAliasList);
            customcontrol.setData((Object)dataMap);
            this.getPageCache().put("calculateFields", SerializationUtils.toJsonString((Object)calculateFields));
        }
    }

    private void addCalField(CustomEventArgs args) {
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "closeForAddCalFieldConfig");
        Map dataMap = (Map)SerializationUtils.fromJsonString((String)args.getEventArgs(), Map.class);
        Object fieldNodes = dataMap.get("fieldNodes");
        Object calFields = dataMap.get("calFields");
        List allFieldsForCalFieldPage = this.commonProcessor.getAllFieldsForCalFieldPage(fieldNodes);
        List selectedCalculateFields = this.commonProcessor.getAllCalculateFields(calFields);
        List reportCalFields = this.commonProcessor.getReportCalFields();
        Set<String> hideFields = this.getHideFieldsForGroupFields(allFieldsForCalFieldPage);
        CalculateFieldPageUtil.showFormForAddNewForAnObj((IFormView)this.getView(), (List)allFieldsForCalFieldPage, (List)reportCalFields, (List)selectedCalculateFields, hideFields, (CloseCallBack)closeCallBack);
    }

    private Set<String> getHideFieldsForGroupFields(List<Map<String, Object>> allFieldsForCalFieldPage) {
        List cacheGroupFields = this.commonProcessor.getCacheGroupFields();
        cacheGroupFields.forEach(field -> {
            HashMap map = Maps.newHashMapWithExpectedSize((int)16);
            if (field.getName() != null) {
                map.put("fieldName", field.getName().getLocaleValue());
            }
            if (HRStringUtils.isNotEmpty((String)field.getNumber())) {
                map.put("fieldNumber", field.getNumber());
            }
            if (!map.isEmpty()) {
                allFieldsForCalFieldPage.add(map);
            }
        });
        return cacheGroupFields.stream().map(AnObjGroupField::getNumber).collect(Collectors.toSet());
    }

    private void openModifyCalFieldPage(CustomEventArgs args) {
        Map dataMap = (Map)SerializationUtils.fromJsonString((String)args.getEventArgs(), Map.class);
        CalculateFieldBo currentCalField = (CalculateFieldBo)JSON.parseObject((String)SerializationUtils.toJsonString(dataMap.get("currentCalField")), CalculateFieldBo.class);
        List fieldNodes = this.commonProcessor.getAllFieldsForCalFieldPage(dataMap.get("fieldNodes"));
        List calFields = this.commonProcessor.getAllCalculateFields(dataMap.get("calFields"));
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "closeForAddCalFieldConfig");
        List reportCalFields = this.commonProcessor.getReportCalFields();
        Set<String> hideFields = this.getHideFieldsForGroupFields(fieldNodes);
        CalculateFieldPageUtil.showFormForEditForAnObj((IFormView)this.getView(), (CalculateFieldBo)currentCalField, (List)fieldNodes, (List)reportCalFields, (List)calFields, hideFields, (CloseCallBack)closeCallBack);
    }

    private void deleteCalFieldCustomEvent() {
        String delCurrentCalFieldStr = this.getPageCache().get("delCurrentCalField");
        CalculateFieldBo delCurrentCalField = (CalculateFieldBo)SerializationUtils.fromJsonString((String)delCurrentCalFieldStr, CalculateFieldBo.class);
        String calFieldStr = this.getPageCache().get("calculateFields");
        List calFields = JSON.parseArray((String)calFieldStr, CalculateFieldBo.class);
        CalculateFieldService service = CalculateFieldService.getInstance();
        StringBuilder errorMsg = new StringBuilder();
        if (service.deleteCalculateField(delCurrentCalField.getFieldNumber(), calFields, errorMsg)) {
            CustomControl customcontrol = (CustomControl)this.getView().getControl("customcontrolap");
            HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
            dataMap.put("method", "setAllCalculateFields");
            List refFieldAliasList = this.commonProcessor.getAllRefFieldAliasByCalField(calFields);
            dataMap.put("refFieldAliasList", refFieldAliasList);
            dataMap.put("calFields", calFields);
            customcontrol.setData((Object)dataMap);
        } else {
            this.getView().showTipNotification(errorMsg.toString());
        }
    }

    private void deleteCalField(CustomEventArgs args) {
        Map dataMap = (Map)SerializationUtils.fromJsonString((String)args.getEventArgs(), Map.class);
        this.getPageCache().put("delCurrentCalField", SerializationUtils.toJsonString(dataMap.get("currentCalField")));
        Object calFields = dataMap.get("calFields");
        if (calFields != null) {
            this.getPageCache().put("calculateFields", SerializationUtils.toJsonString(calFields));
        }
        ConfirmCallBackListener deleteMainEntityConfirmListener = new ConfirmCallBackListener("deleteCalField", (IFormPlugin)this);
        this.getView().showConfirm(ResManager.loadKDString((String)"\u786e\u5b9a\u5220\u9664\u8be5\u5b57\u6bb5\uff1f", (String)"ReportAnalyseObjectEdit_11", (String)"hrmp-hrptmc-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, deleteMainEntityConfirmListener);
    }
}
