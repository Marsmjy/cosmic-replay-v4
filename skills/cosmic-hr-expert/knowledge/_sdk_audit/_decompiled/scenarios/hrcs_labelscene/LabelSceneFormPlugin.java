/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageTypes
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.control.Label
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.bussiness.servicehelper.label.LabelPolicyServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper
 *  kd.hr.hrcs.common.constants.label.LabelTypeEnum
 */
package kd.hr.hrcs.formplugin.web.label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageTypes;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.control.Label;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.bussiness.servicehelper.label.LabelPolicyServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper;
import kd.hr.hrcs.common.constants.label.LabelTypeEnum;

@ExcludeFromJacocoGeneratedReport
public final class LabelSceneFormPlugin
extends AbstractFormPlugin
implements BeforeF7SelectListener {
    private static final Log LOGGER = LogFactory.getLog(LabelSceneFormPlugin.class);
    private static final String CURRENT_MODULE = "hrmp-hrcs-formplugin";
    private static final String LABEL_ENTRY = "entryentity";
    private static final String LABEL_ID = "label.id";
    private static final String CLEAR_LABEL_ENTRY = "clearLabelEntry";
    private static final String KEY_ENTRYENTITY_BIZLABEL = "bizlabel";
    private static final String KEY_ENTRYENTITY_LABELTYPE = "label.type";
    private static final String KEY_ENTRYENTITY_LABELNAME = "label.name";
    private static final String KEY_LABEL = "labelap";
    private static final String KEY_CUSTOM = "customcontrolap";

    public void registerListener(EventObject e) {
        super.registerListener(e);
        BasedataEdit labelObjEdit = (BasedataEdit)this.getView().getControl("labelobject");
        labelObjEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        if ("closetips".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap6"});
        } else if ("submit".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess() || "audit".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess() || "disable".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            this.getView().updateView(LABEL_ENTRY);
        } else if ("unsubmit".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess() || "unaudit".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess() || "enable".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess() || "modify".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            this.setBizLabelReadOnly(false, true);
            this.getView().updateView(KEY_CUSTOM);
        }
    }

    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
        BaseShowParameter baseShowParameter = (BaseShowParameter)e.getFormShowParameter();
        if (OperationStatus.ADDNEW.equals((Object)baseShowParameter.getStatus())) {
            baseShowParameter.setCaption(ResManager.loadKDString((String)"\u65b0\u589e\u6807\u7b7e\u573a\u666f", (String)"LabelSceneFormPlugin_0", (String)CURRENT_MODULE, (Object[])new Object[0]));
        }
    }

    public void afterBindData(EventObject e) {
        OperationStatus operationStatus = this.getView().getFormShowParameter().getStatus();
        if (OperationStatus.ADDNEW.equals((Object)operationStatus)) {
            String bizAppId = (String)this.getView().getFormShowParameter().getCustomParam("biz_app_id");
            this.getModel().setValue("bizappid", (Object)bizAppId);
            this.getModel().setDataChanged(false);
        }
        this.setBizLabelReadOnly(true, false);
    }

    private void setBizLabelReadOnly(boolean isShowMsg, boolean isForceTips) {
        DynamicObjectCollection labelEntry = this.getModel().getEntryEntity(LABEL_ENTRY);
        ArrayList<String> deletePolicyLabels = new ArrayList<String>(16);
        ArrayList<String> disablePolicyLabels = new ArrayList<String>(16);
        HashMap<String, String> tipMap = new HashMap<String, String>(16);
        String factTips = ResManager.loadKDString((String)"\u4e0d\u5141\u8bb8\u4f7f\u7528\u4e8b\u5b9e\u6807\u7b7e\u8fdb\u884c\u4e1a\u52a1\u9875\u9762\u6253\u6807\u3002", (String)"LabelSceneFormPlugin_16", (String)CURRENT_MODULE, (Object[])new Object[0]);
        String noPolicyTips = ResManager.loadKDString((String)"\u8be5\u6a21\u578b\u6807\u7b7e\u5728\u5f53\u524d\u6253\u6807\u5bf9\u8c61\u4e0a\u65e0\u624b\u52a8\u6253\u6807\u7b56\u7565\uff0c\u4e0d\u5141\u8bb8\u5f00\u542f\u3002", (String)"LabelSceneFormPlugin_17", (String)CURRENT_MODULE, (Object[])new Object[0]);
        String disableTips = ResManager.loadKDString((String)"\u8be5\u6a21\u578b\u6807\u7b7e\u5728\u5f53\u524d\u6253\u6807\u5bf9\u8c61\u4e0a\u7684\u624b\u52a8\u6253\u6807\u7b56\u7565\u5df2\u88ab\u7981\u7528\uff0c\u4e0d\u5141\u8bb8\u5f00\u542f\u3002", (String)"LabelSceneFormPlugin_18", (String)CURRENT_MODULE, (Object[])new Object[0]);
        String outTimeTips = ResManager.loadKDString((String)"\u8be5\u6a21\u578b\u6807\u7b7e\u5728\u5f53\u524d\u6253\u6807\u5bf9\u8c61\u4e0a\u7684\u624b\u52a8\u6253\u6807\u7b56\u7565\u5df2\u8fc7\u671f\uff0c\u4e0d\u5141\u8bb8\u5f00\u542f\u3002", (String)"LabelSceneFormPlugin_19", (String)CURRENT_MODULE, (Object[])new Object[0]);
        String disAndOutTimeTips = ResManager.loadKDString((String)"\u8be5\u6a21\u578b\u6807\u7b7e\u5728\u5f53\u524d\u6253\u6807\u5bf9\u8c61\u4e0a\u7684\u624b\u52a8\u6253\u6807\u7b56\u7565\u5df2\u88ab\u7981\u7528\uff0c\u4e14\u5df2\u8fc7\u671f\uff0c\u4e0d\u5141\u8bb8\u5f00\u542f\u3002", (String)"LabelSceneFormPlugin_20", (String)CURRENT_MODULE, (Object[])new Object[0]);
        Date curDate = new Date();
        for (int idx = 0; idx < labelEntry.getRowCount(); ++idx) {
            if (LabelTypeEnum.FACT.getType().equals(((DynamicObject)labelEntry.get(idx)).get(KEY_ENTRYENTITY_LABELTYPE))) {
                this.getView().setEnable(Boolean.valueOf(false), idx, new String[]{KEY_ENTRYENTITY_BIZLABEL});
                tipMap.put(String.valueOf(idx), factTips);
                continue;
            }
            DynamicObject[] labalPolicyDyns = LabelPolicyServiceHelper.getPolicyDyns((Long)this.getModel().getDataEntity().getDynamicObject("labelobject").getLong("id"), (Long)((DynamicObject)labelEntry.get(idx)).getLong(LABEL_ID));
            if (((DynamicObject)labelEntry.get(idx)).getBoolean(KEY_ENTRYENTITY_BIZLABEL)) {
                if (labalPolicyDyns == null || labalPolicyDyns.length == 0) {
                    deletePolicyLabels.add(((DynamicObject)labelEntry.get(idx)).getString(KEY_ENTRYENTITY_LABELNAME));
                    this.getModel().setValue(KEY_ENTRYENTITY_BIZLABEL, (Object)Boolean.FALSE, idx);
                    continue;
                }
                if (!"0".equals(labalPolicyDyns[0].getString("enable"))) continue;
                disablePolicyLabels.add(((DynamicObject)labelEntry.get(idx)).getString(KEY_ENTRYENTITY_LABELNAME));
                this.getModel().setValue(KEY_ENTRYENTITY_BIZLABEL, (Object)Boolean.FALSE, idx);
                continue;
            }
            if (labalPolicyDyns == null || labalPolicyDyns.length == 0) {
                this.getView().setEnable(Boolean.valueOf(false), idx, new String[]{KEY_ENTRYENTITY_BIZLABEL});
                tipMap.put(String.valueOf(idx), noPolicyTips);
                continue;
            }
            Date endDate = labalPolicyDyns[0].getDate("enddate") == null ? new Date() : labalPolicyDyns[0].getDate("enddate");
            String enable = labalPolicyDyns[0].getString("enable");
            if ("0".equals(enable) && curDate.after(endDate)) {
                this.getView().setEnable(Boolean.valueOf(false), idx, new String[]{KEY_ENTRYENTITY_BIZLABEL});
                tipMap.put(String.valueOf(idx), disAndOutTimeTips);
                continue;
            }
            if (curDate.after(endDate)) {
                this.getView().setEnable(Boolean.valueOf(false), idx, new String[]{KEY_ENTRYENTITY_BIZLABEL});
                tipMap.put(String.valueOf(idx), outTimeTips);
                continue;
            }
            if (!"0".equals(enable)) continue;
            this.getView().setEnable(Boolean.valueOf(false), idx, new String[]{KEY_ENTRYENTITY_BIZLABEL});
            tipMap.put(String.valueOf(idx), disableTips);
        }
        this.showBizLabelTips(tipMap, isForceTips);
        if (isShowMsg) {
            String msg;
            String labelMsg = "";
            if (deletePolicyLabels.size() > 0 && disablePolicyLabels.size() > 0) {
                msg = ResManager.loadKDString((String)"%1$s\u6807\u7b7e\u7684\u624b\u52a8\u6253\u6807\u7b56\u7565\u5df2\u88ab\u7981\u7528\uff0c%2$s\u6807\u7b7e\u7684\u624b\u52a8\u6253\u6807\u7b56\u7565\u5df2\u88ab\u5220\u9664\uff0c\u4e0d\u652f\u6301\u4e1a\u52a1\u9875\u9762\u6253\u6807\uff0c\u5bf9\u5e94\u7684\u201c\u652f\u6301\u4e1a\u52a1\u9875\u9762\u6253\u6807\u201d\u5f00\u5173\u5df2\u5173\u95ed\u3002", (String)"LabelSceneFormPlugin_13", (String)CURRENT_MODULE, (Object[])new Object[0]);
                labelMsg = String.format(msg, this.getShowMsg(disablePolicyLabels), this.getShowMsg(deletePolicyLabels));
            } else if (deletePolicyLabels.size() > 0) {
                msg = ResManager.loadKDString((String)"%1$s\u6807\u7b7e\u7684\u624b\u52a8\u6253\u6807\u7b56\u7565\u5df2\u88ab\u5220\u9664\uff0c\u4e0d\u652f\u6301\u4e1a\u52a1\u9875\u9762\u6253\u6807\uff0c\u5bf9\u5e94\u7684\u201c\u652f\u6301\u4e1a\u52a1\u9875\u9762\u6253\u6807\u201d\u5f00\u5173\u5df2\u5173\u95ed\u3002", (String)"LabelSceneFormPlugin_14", (String)CURRENT_MODULE, (Object[])new Object[0]);
                labelMsg = String.format(msg, this.getShowMsg(deletePolicyLabels));
            } else if (disablePolicyLabels.size() > 0) {
                msg = ResManager.loadKDString((String)"%1$s\u6807\u7b7e\u7684\u624b\u52a8\u6253\u6807\u7b56\u7565\u5df2\u88ab\u7981\u7528\uff0c\u4e0d\u652f\u6301\u4e1a\u52a1\u9875\u9762\u6253\u6807\uff0c\u5bf9\u5e94\u7684\u201c\u652f\u6301\u4e1a\u52a1\u9875\u9762\u6253\u6807\u201d\u5f00\u5173\u5df2\u5173\u95ed\u3002", (String)"LabelSceneFormPlugin_15", (String)CURRENT_MODULE, (Object[])new Object[0]);
                labelMsg = String.format(msg, this.getShowMsg(disablePolicyLabels));
            }
            if (!"".equals(labelMsg)) {
                Label label = (Label)this.getView().getControl(KEY_LABEL);
                label.setText(labelMsg);
            } else {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap6"});
            }
        }
    }

    private String getShowMsg(List<String> labels) {
        StringBuilder sb = new StringBuilder();
        for (String msg : labels) {
            sb.append("\u201c").append(msg).append("\u201d\u3001");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public void propertyChanged(PropertyChangedArgs args) {
        ChangeData[] changeData;
        String changeKey = args.getProperty().getName();
        String clearLabelEntry = this.getPageCache().get(CLEAR_LABEL_ENTRY);
        if ("labelobject".equals(changeKey)) {
            if (Boolean.parseBoolean(clearLabelEntry)) {
                this.clearLabelEntry();
            }
            this.getPageCache().put(CLEAR_LABEL_ENTRY, "true");
        } else if (KEY_ENTRYENTITY_BIZLABEL.equals(changeKey) && (changeData = args.getChangeSet()) != null && changeData.length > 0) {
            int row = args.getChangeSet()[0].getRowIndex();
            DynamicObjectCollection coll = this.getModel().getEntryEntity(LABEL_ENTRY);
            if (((DynamicObject)coll.get(row)).getBoolean(KEY_ENTRYENTITY_BIZLABEL) && !this.checkCanBizLabel(this.getModel().getDataEntity().getDynamicObject("labelobject").getLong("id"), ((DynamicObject)coll.get(row)).getLong(LABEL_ID))) {
                this.getModel().setValue(KEY_ENTRYENTITY_BIZLABEL, (Object)Boolean.FALSE, row);
            }
        }
    }

    private void clearLabelEntry() {
        this.getModel().deleteEntryData(LABEL_ENTRY);
    }

    public void beforeBindData(EventObject e) {
        FormShowParameter fsp = this.getView().getFormShowParameter();
        OperationStatus status = fsp.getStatus();
        if (OperationStatus.EDIT.equals((Object)status) || OperationStatus.VIEW.equals((Object)status)) {
            DynamicObjectCollection coll = this.getModel().getEntryEntity(LABEL_ENTRY);
            List<Long> labelIds = coll.stream().map(dy -> dy.getLong(LABEL_ID)).collect(Collectors.toList());
            this.updateLabelValue(labelIds);
            MulBasedataDynamicObjectCollection labelObjColl = (MulBasedataDynamicObjectCollection)this.getModel().getValue("lblobjectids");
            if (!labelObjColl.isEmpty()) {
                this.getPageCache().put(CLEAR_LABEL_ENTRY, "false");
                this.getModel().setValue("labelobject", (Object)((DynamicObject)labelObjColl.get(0)).getLong("fbasedataid_id"));
            }
            this.getModel().setDataChanged(false);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        this.getModel().setValue("projectability", (Object)"labelshow;");
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if ("save".equals(operateKey) || "submit".equals(operateKey)) {
            DynamicObject labelObjectDy = this.getModel().getDataEntity().getDynamicObject("labelobject");
            MulBasedataDynamicObjectCollection labelObjColl = (MulBasedataDynamicObjectCollection)this.getModel().getValue("lblobjectids");
            labelObjColl.clear();
            DynamicObject labelObjDy = labelObjColl.addNew();
            labelObjDy.set("fbasedataid_id", (Object)labelObjectDy.getLong("id"));
            labelObjDy.set("fbasedataid", (Object)labelObjectDy);
            this.getModel().setValue("lblobjectids", (Object)labelObjColl);
            OperateOption option = op.getOption();
            option.setVariableValue("isFromForm", "Y");
        } else if ("addlabel".equals(operateKey)) {
            args.setCancel(true);
            DynamicObject labelObject = this.getModel().getDataEntity().getDynamicObject("labelobject");
            if (null == labelObject) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u6253\u6807\u5bf9\u8c61\u3002", (String)"LabelSceneFormPlugin_6", (String)CURRENT_MODULE, (Object[])new Object[0]));
                return;
            }
            this.openLabel(labelObject);
        }
    }

    private void openLabel(DynamicObject labelObject) {
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"hrcs_label", (boolean)true, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "hrcs_label"));
        QFilter filter = new QFilter("entryentityrange.labelobject", "=", labelObject.getPkValue());
        filter.and(new QFilter("status", "=", (Object)"C"));
        List selectedIds = this.getModel().getEntryEntity(LABEL_ENTRY).stream().map(dy -> dy.getLong(LABEL_ID)).collect(Collectors.toList());
        if (!selectedIds.isEmpty()) {
            filter.and(new QFilter("id", "not in", selectedIds));
        }
        ListFilterParameter filterParameter = new ListFilterParameter();
        filterParameter.setFilter(filter);
        fsp.setListFilterParameter(filterParameter);
        this.getView().showForm((FormShowParameter)fsp);
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String propertyName = evt.getProperty().getName();
        if (HRStringUtils.equals((String)propertyName, (String)"labelobject")) {
            DynamicObject labelObject = this.getModel().getDataEntity().getDynamicObject("labelobject");
            DynamicObjectCollection coll = this.getModel().getEntryEntity(LABEL_ENTRY);
            if (null != labelObject && !coll.isEmpty()) {
                evt.getFormShowParameter().setCustomParam("modifytip", (Object)ResManager.loadKDString((String)"\u4fee\u6539\u6253\u6807\u5bf9\u8c61\u540e\uff0c\u5c06\u6e05\u7a7a\u5df2\u8bbe\u7f6e\u7684\u5173\u8054\u6807\u7b7e\u3002\u786e\u5b9a\u4fee\u6539\u5417\uff1f", (String)"LabelSceneFormPlugin_7", (String)CURRENT_MODULE, (Object[])new Object[0]));
                evt.getFormShowParameter().setCustomParam("modifyid", (Object)labelObject.getString("id"));
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        String actionId = evt.getActionId();
        if (actionId.startsWith("hrcs_label")) {
            if (evt.getReturnData() == null) {
                return;
            }
            ListSelectedRowCollection selectedCol = (ListSelectedRowCollection)evt.getReturnData();
            List<Long> labelIds = selectedCol.stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList());
            this.updateLabelEntry(labelIds);
        }
    }

    private void updateLabelEntry(List<Long> labelIds) {
        Map<Long, String> idValueMap = this.getValueList(labelIds);
        this.getModel().beginInit();
        int idx = this.getModel().getEntryEntity(LABEL_ENTRY).size();
        this.getModel().batchCreateNewEntryRow(LABEL_ENTRY, labelIds.size());
        for (int i = 0; i < labelIds.size(); ++i) {
            Long id = labelIds.get(i);
            this.getModel().setValue("label", (Object)id, idx + i);
            this.getModel().setValue("labelvalue", (Object)idValueMap.get(id), idx + i);
        }
        this.getModel().endInit();
        this.getView().updateView(LABEL_ENTRY);
        this.setBizLabelReadOnly(false, true);
    }

    private void showBizLabelTips(Map<String, String> tipMap, boolean isForceTips) {
        if (!isForceTips) {
            BaseShowParameter bsp;
            if (OperationStatus.VIEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
                return;
            }
            if (this.getView().getFormShowParameter() instanceof BaseShowParameter && (BillOperationStatus.AUDIT.equals((Object)(bsp = (BaseShowParameter)this.getView().getFormShowParameter()).getBillStatus()) || BillOperationStatus.VIEW.equals((Object)bsp.getBillStatus()) || BillOperationStatus.SUBMIT.equals((Object)bsp.getBillStatus()))) {
                return;
            }
        }
        if (tipMap.size() > 0) {
            CustomControl customControl = (CustomControl)this.getView().getControl(KEY_CUSTOM);
            HashMap<String, Object> cellTips = new HashMap<String, Object>(16);
            cellTips.put("id", KEY_ENTRYENTITY_BIZLABEL);
            cellTips.put("ct", tipMap);
            cellTips.put("t", new Date().getTime());
            customControl.setData(cellTips);
            this.getView().updateView(KEY_CUSTOM);
        }
    }

    private Map<Long, String> getValueList(List<Long> labelIdList) {
        LabelServiceHelper labelServiceHelper = new LabelServiceHelper();
        DynamicObject[] labelValueCollection = labelServiceHelper.getLabelValueCollection(labelIdList);
        if (labelValueCollection == null || labelValueCollection.length < 1) {
            return new HashMap<Long, String>();
        }
        Map<Long, List<DynamicObject>> labelValueMap = Arrays.stream(labelValueCollection).collect(Collectors.groupingBy(dynamicObject -> dynamicObject.getLong(LABEL_ID)));
        HashMap<Long, String> labelValueNameMap = new HashMap<Long, String>(labelValueMap.size());
        for (Map.Entry<Long, List<DynamicObject>> entry : labelValueMap.entrySet()) {
            List<DynamicObject> valueDynamicObjectList = labelValueMap.get(entry.getKey());
            String valueName = valueDynamicObjectList.stream().map(value -> value.getString("value")).collect(Collectors.joining(","));
            labelValueNameMap.put(entry.getKey(), valueName);
        }
        return labelValueNameMap;
    }

    private void updateLabelValue(List<Long> labelIds) {
        Map<Long, String> idValueMap = this.getValueList(labelIds);
        DynamicObjectCollection coll = this.getModel().getEntryEntity(LABEL_ENTRY);
        for (int i = 0; i < coll.size(); ++i) {
            Long id = ((DynamicObject)coll.get(i)).getLong(LABEL_ID);
            if (!idValueMap.containsKey(id)) continue;
            this.getModel().setValue("labelvalue", (Object)idValueMap.get(id), i);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean checkCanBizLabel(Long labelObjectId, Long labelId) {
        DynamicObject[] labalPolicyDyns = LabelPolicyServiceHelper.getPolicyDyns((Long)labelObjectId, (Long)labelId);
        if (labalPolicyDyns == null || labalPolicyDyns.length == 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u6b64\u6807\u7b7e\u5728\u5f53\u524d\u6253\u6807\u5bf9\u8c61\u672a\u8bbe\u7f6e\u624b\u52a8\u6253\u6807\u7b56\u7565\uff0c\u4e0d\u5141\u8bb8\u5f00\u542f\u3002", (String)"LabelSceneFormPlugin_10", (String)CURRENT_MODULE, (Object[])new Object[0]));
            return false;
        }
        if (labalPolicyDyns.length > 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u6b64\u6807\u7b7e\u5728\u5f53\u524d\u6253\u6807\u5bf9\u8c61\u4e0a\u5b58\u5728\u591a\u4e2a\u6253\u6807\u7b56\u7565\uff0c\u6570\u636e\u5f02\u5e38\uff0c\u8bf7\u5220\u9664\u6253\u6807\u7b56\u7565\u540e\u91cd\u8bd5\u3002", (String)"LabelSceneFormPlugin_11", (String)CURRENT_MODULE, (Object[])new Object[0]));
            return false;
        }
        if ("0".equals(labalPolicyDyns[0].getString("worktype"))) {
            String msg = ResManager.loadKDString((String)"\u6b64\u6807\u7b7e\u5728\u5f53\u524d\u6253\u6807\u5bf9\u8c61\u4e0a\u5b58\u5728\u89c4\u5219\u6253\u6807\u7b56\u7565\uff0c\u4e0d\u652f\u6301\u4e1a\u52a1\u9875\u9762\u6253\u6807\uff0c\u5982\u6709\u9700\u8981\uff0c\u8bf7\u8054\u7cfb\u201c%1$s\uff08\u5de5\u53f7%2$s\uff09\u201d\u5220\u9664\u89c4\u5219\u6253\u6807\u7b56\u7565\uff0c\u5e76\u8bbe\u7f6e\u624b\u52a8\u6253\u6807\u7b56\u7565\u3002", (String)"LabelSceneFormPlugin_12", (String)CURRENT_MODULE, (Object[])new Object[0]);
            this.getView().showMessage(String.format(msg, labalPolicyDyns[0].getDynamicObject("creator").getString("name"), labalPolicyDyns[0].getDynamicObject("creator").getString("number")), null, MessageTypes.Default);
            return false;
        }
        return true;
    }
}
