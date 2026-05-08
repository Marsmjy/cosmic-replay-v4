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
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.WarnCalcFieldService
 *  kd.hr.hrcs.common.model.earlywarn.WarnCalFieldBo
 *  kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonEdit
 *  kd.hr.hrcs.formplugin.web.earlywarn.utils.ExpressionFieldPageUtil
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scene;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.WarnCalcFieldService;
import kd.hr.hrcs.common.model.earlywarn.WarnCalFieldBo;
import kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonEdit;
import kd.hr.hrcs.formplugin.web.earlywarn.utils.ExpressionFieldPageUtil;

@ExcludeFromJacocoGeneratedReport
public final class WarnSceneCalConfigEdit
extends WarnSceneCommonEdit {
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
        if (HRStringUtils.equals((String)"closeForAddCalFieldConfig", (String)event.getActionId()) && returnData instanceof Map) {
            Map expDataMap = (Map)returnData;
            String listExpFieldsStr = (String)expDataMap.get("RETURN_LIST_EXP_FIELD_KEY");
            List calculateFields = JSON.parseArray((String)listExpFieldsStr, WarnCalFieldBo.class);
            calculateFields.stream().forEach(data -> data.setSource("warnfield"));
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
        Object calFields = dataMap.get("calFields");
        List allFieldsForCalFieldPage = this.commonProcessor.getAllFieldsForCalFieldPage();
        List selectedCalculateFields = this.commonProcessor.getAllCalculateFields(calFields);
        ExpressionFieldPageUtil.showFormForAddNewForAnObj((IFormView)this.getView(), (List)allFieldsForCalFieldPage, (List)selectedCalculateFields, null, (String)this.getView().getPageCache().get("joinEntities"), (CloseCallBack)closeCallBack);
    }

    private void openModifyCalFieldPage(CustomEventArgs args) {
        Map dataMap = (Map)SerializationUtils.fromJsonString((String)args.getEventArgs(), Map.class);
        WarnCalFieldBo currentCalField = (WarnCalFieldBo)JSON.parseObject((String)SerializationUtils.toJsonString(dataMap.get("currentCalField")), WarnCalFieldBo.class);
        List fieldNodes = this.commonProcessor.getAllFieldsForCalFieldPage();
        List calFields = this.commonProcessor.getAllCalculateFields(dataMap.get("calFields"));
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "closeForAddCalFieldConfig");
        ExpressionFieldPageUtil.showFormForEditForAnObj((IFormView)this.getView(), (WarnCalFieldBo)currentCalField, (List)fieldNodes, (List)calFields, null, (String)this.getView().getPageCache().get("joinEntities"), (CloseCallBack)closeCallBack);
    }

    private void deleteCalFieldCustomEvent() {
        String delCurrentCalFieldStr = this.getPageCache().get("delCurrentCalField");
        WarnCalFieldBo delCurrentCalField = (WarnCalFieldBo)SerializationUtils.fromJsonString((String)delCurrentCalFieldStr, WarnCalFieldBo.class);
        String calFieldStr = this.getPageCache().get("calculateFields");
        List calFields = JSON.parseArray((String)calFieldStr, WarnCalFieldBo.class);
        WarnCalcFieldService service = WarnCalcFieldService.getInstance();
        StringBuilder errorMsg = new StringBuilder();
        if (service.deleteCalField(delCurrentCalField.getFieldNumber(), calFields, errorMsg)) {
            CustomControl customcontrol = (CustomControl)this.getView().getControl("customcontrolap");
            HashMap dataMap = Maps.newHashMapWithExpectedSize((int)16);
            dataMap.put("method", "setAllCalculateFields");
            List refFieldAliasList = this.commonProcessor.getAllRefFieldAliasByCalField(calFields);
            dataMap.put("refFieldAliasList", refFieldAliasList);
            dataMap.put("calFields", calFields);
            customcontrol.setData((Object)dataMap);
            this.getPageCache().put("calculateFields", SerializationUtils.toJsonString((Object)calFields));
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
        this.getView().showConfirm(ResManager.loadKDString((String)"\u786e\u5b9a\u5220\u9664\u8be5\u5b57\u6bb5\uff1f", (String)"WarnSceneCalConfigEdit_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, deleteMainEntityConfirmListener);
    }
}
