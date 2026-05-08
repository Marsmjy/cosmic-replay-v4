/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.RefObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.ISimpleProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.property.LongProp
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.ext.hr.ruleengine.utils.ParamsUtil
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.application.impl.common.HRLongValueParseService
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hrptc.common.constant.center.ReportCenterEntityConstants
 *  kd.hr.hrptc.common.constant.perm.ReportUserPermConstants
 *  kd.hr.hrptc.formplugin.perm.model.TransferControlDataVO
 *  kd.hr.hrptc.formplugin.perm.model.TransferTreeNodeVO
 *  kd.hr.hrptc.formplugin.perm.model.UserRepPermDataRangeReturnVO
 *  kd.hr.hrptc.formplugin.perm.model.UserRepPermFieldReturnVO
 *  kd.hr.hrptc.formplugin.perm.processor.ReportUserPermDataProcessor
 *  kd.hr.hrptc.formplugin.perm.processor.ReportUserPermVOProcessor
 *  kd.hr.hrptc.formplugin.perm.utils.ReportUserPermFormUtils
 *  kd.hr.hrptmc.common.constant.center.ReportCenterConstants
 *  kd.hr.hrptmc.common.constant.center.ReportCenterGroupConstants
 */
package kd.hr.hrptc.formplugin.perm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.RefObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.ISimpleProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.property.LongProp;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.ext.hr.ruleengine.utils.ParamsUtil;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.application.impl.common.HRLongValueParseService;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hrptc.common.constant.center.ReportCenterEntityConstants;
import kd.hr.hrptc.common.constant.perm.ReportUserPermConstants;
import kd.hr.hrptc.formplugin.perm.model.TransferControlDataVO;
import kd.hr.hrptc.formplugin.perm.model.TransferTreeNodeVO;
import kd.hr.hrptc.formplugin.perm.model.UserRepPermDataRangeReturnVO;
import kd.hr.hrptc.formplugin.perm.model.UserRepPermFieldReturnVO;
import kd.hr.hrptc.formplugin.perm.processor.ReportUserPermDataProcessor;
import kd.hr.hrptc.formplugin.perm.processor.ReportUserPermVOProcessor;
import kd.hr.hrptc.formplugin.perm.utils.ReportUserPermFormUtils;
import kd.hr.hrptmc.common.constant.center.ReportCenterConstants;
import kd.hr.hrptmc.common.constant.center.ReportCenterGroupConstants;

@ExcludeFromJacocoGeneratedReport
public final class ReportUserPermEdit
extends HRBaseDataCommonEdit
implements ReportUserPermConstants,
ReportCenterConstants {
    public static String INCLUDE_SUB = "includesub";
    private static final Log LOGGER = LogFactory.getLog(ReportUserPermEdit.class);
    private final ReportUserPermVOProcessor voProcessor = new ReportUserPermVOProcessor(this);
    private final ReportUserPermDataProcessor dataProcessor = new ReportUserPermDataProcessor(this, this.voProcessor);

    protected List<String> getUnCheckField() {
        List unCheckField = super.getUnCheckField();
        try {
            unCheckField.add("userposition");
            unCheckField.add("fielddisableentry");
            unCheckField.add("disablefieldreport");
            unCheckField.add("disablefield");
            unCheckField.add("rptpermdataentry");
            unCheckField.add("queryfield");
            unCheckField.add("ismerge");
            unCheckField.add("data");
            unCheckField.add("permgroup");
            unCheckField.add("adminorgstruct");
            unCheckField.add("selectreportentry");
            unCheckField.add("selectreport");
            unCheckField.add("selectreportismerge");
            unCheckField.add("selectreportgroupentry");
            unCheckField.add("selectreportgroupkey");
        }
        catch (Exception exception) {
            LOGGER.error("getUnCheckField_error_", (Throwable)exception);
        }
        return unCheckField;
    }

    public void beforeClosed(BeforeClosedEvent beforeClosedEvent) {
        try {
            String permDataIsChange;
            super.beforeClosed(beforeClosedEvent);
            if (!beforeClosedEvent.isCancel() && !this.isView() && "true".equals(permDataIsChange = this.getView().getPageCache().get("CACHE_KEY_IS_PERM_DATA_CHANGE"))) {
                ConfirmCallBackListener confirmCallBacks = new ConfirmCallBackListener("CONFIRM_CALL_BACK_CONTINUE_CLOSE", (IFormPlugin)this);
                HashMap<Integer, String> btnNameMaps = new HashMap<Integer, String>();
                btnNameMaps.put(MessageBoxResult.Cancel.getValue(), ResManager.loadKDString((String)"\u8fd4\u56de\u7f16\u8f91", (String)"ReportUserPermEdit_0", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]));
                btnNameMaps.put(MessageBoxResult.Yes.getValue(), ResManager.loadKDString((String)"\u76f4\u63a5\u9000\u51fa", (String)"ReportUserPermEdit_1", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]));
                MessageBoxOptions options = MessageBoxOptions.OKCancel;
                String msg = MessageFormat.format(ResManager.loadKDString((String)"\u68c0\u6d4b\u5230\u60a8\u6709\u66f4\u6539\u5185\u5bb9\uff0c\u786e\u5b9a\u4e0d\u4fdd\u5b58\u76f4\u63a5\u9000\u51fa\uff1f{0}\u82e5\u4e0d\u4fdd\u5b58\uff0c\u5c06\u4e22\u5931\u8fd9\u4e9b\u66f4\u6539\u3002", (String)"ReportUserPermEdit_2", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]), System.lineSeparator());
                this.getView().showConfirm(msg, ResManager.loadKDString((String)"\u6570\u636e\u8303\u56f4\u5df2\u53d8\u66f4\u3002", (String)"ReportUserPermEdit_3", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]), options, ConfirmTypes.Save, confirmCallBacks, btnNameMaps);
                beforeClosedEvent.setCancel(true);
            }
        }
        catch (Exception exception) {
            LOGGER.error("beforeClosed_error_", (Throwable)exception);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        try {
            super.confirmCallBack(evt);
            String callBackId = evt.getCallBackId();
            if ("CONFIRM_CALL_BACK_CONTINUE_CLOSE".equals(callBackId) && evt.getResult() == MessageBoxResult.Yes) {
                FormShowParameter showParameter = this.getView().getFormShowParameter();
                showParameter.setStatus(OperationStatus.VIEW);
                this.getView().close();
            }
        }
        catch (Exception exception) {
            LOGGER.error("confirmCallBack_error_", (Throwable)exception);
        }
    }

    public void beforeBindData(EventObject eventObject) {
        try {
            super.beforeBindData(eventObject);
            this.initRepTransControl();
            this.dataProcessor.initPermDataEntry();
            this.initRepPermDimControl();
            this.renderUserRepPermDataRangeControl("initDataRange");
            DynamicObject user = (DynamicObject)this.getModel().getValue("user");
            this.setMainPosition(user);
        }
        catch (Exception exception) {
            LOGGER.error("beforeBindData_error_", (Throwable)exception);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        try {
            super.beforeDoOperation(args);
            FormOperate formOperate = (FormOperate)args.getSource();
            if ("save".equals(formOperate.getOperateKey()) && !formOperate.getOption().tryGetVariableValue("OP_KEY_AFTER_SAVE_DATA_CHECK", new RefObject())) {
                this.dataProcessor.updateSelectReportSort();
                this.sendDataToControlUserDataRange("dataCheckBeforeSave", null);
                args.setCancel(true);
            }
        }
        catch (Exception exception) {
            LOGGER.error("beforeDoOperation_error_", (Throwable)exception);
        }
    }

    public void propertyChanged(PropertyChangedArgs changedArgs) {
        try {
            super.propertyChanged(changedArgs);
            IDataEntityProperty property = changedArgs.getProperty();
            ChangeData[] changeSet = changedArgs.getChangeSet();
            Object newValue = changeSet[0].getNewValue();
            if ("user".equals(property.getName())) {
                DynamicObject newUser = (DynamicObject)newValue;
                this.setMainPosition(newUser);
            }
        }
        catch (Exception exception) {
            LOGGER.error("propertyChanged_error_", (Throwable)exception);
        }
    }

    public void customEvent(CustomEventArgs customEvent) {
        try {
            super.customEvent(customEvent);
            String key = customEvent.getKey();
            String eventArgs = customEvent.getEventArgs();
            String eventName = customEvent.getEventName();
            if ("userpermfields".equals(key)) {
                JSONObject data = JSONObject.parseObject((String)eventArgs);
                Long reportId = data.getLong("reportId");
                Long fieldId = data.getLong("fieldId");
                Boolean open = data.getBoolean("open");
                Boolean mustInput = data.getBoolean("mustInput");
                this.dataProcessor.updateDisableReportFieldEntry(reportId, fieldId, open);
                this.dataProcessor.updateOnePermDataEntry(reportId, fieldId, open.booleanValue(), mustInput.booleanValue());
                this.renderPermControlData();
            } else if ("userreppermdatarange".equals(key)) {
                if ("updateDataRangeData".equals(eventName)) {
                    UserRepPermDataRangeReturnVO rangeReturnVO = (UserRepPermDataRangeReturnVO)JSONObject.parseObject((String)eventArgs, UserRepPermDataRangeReturnVO.class);
                    List userRepPermData = this.voProcessor.buildPermDataListByReturnVO(rangeReturnVO);
                    this.dataProcessor.updatePermDataEntry(userRepPermData, true);
                } else if ("openDataRangeFieldF7".equals(eventName)) {
                    this.openBaseDataF7(eventArgs);
                } else if ("selectReport".equals(eventName)) {
                    this.openReportF7(eventArgs);
                } else if ("deleteIndependentReport".equals(eventName)) {
                    this.deleteIndependentReport(eventArgs);
                } else if ("clearIndependentReport".equals(eventName)) {
                    this.clearIndependentReport(eventArgs);
                } else if ("saveData".equals(eventName)) {
                    OperateOption operateOption = OperateOption.create();
                    operateOption.setVariableValue("OP_KEY_AFTER_SAVE_DATA_CHECK", "true");
                    this.getView().invokeOperation("save", operateOption);
                }
            } else if ("hrptc_reportscope".equals(key) && "updateSelectReportIds".equals(eventName)) {
                this.updateSelectReportIdCache(eventArgs);
                this.renderPermControlData();
            }
        }
        catch (Exception exception) {
            LOGGER.error("customEvent_error_", (Throwable)exception);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        try {
            super.closedCallBack(closedCallBackEvent);
            String actionId = closedCallBackEvent.getActionId();
            if (HRStringUtils.isNotEmpty((String)actionId)) {
                if (actionId.startsWith("OPEN_DATA_RANGE_F7_CLOSE_CALL_BACK_PREFIX_")) {
                    ListSelectedRowCollection returnRows = (ListSelectedRowCollection)closedCallBackEvent.getReturnData();
                    if (null == returnRows || 0 == returnRows.size()) {
                        return;
                    }
                    String indexStr = actionId.replaceFirst("OPEN_DATA_RANGE_F7_CLOSE_CALL_BACK_PREFIX_", "");
                    String reportId = "";
                    if (indexStr.contains("|")) {
                        String[] split = indexStr.split("\\|");
                        reportId = split[0];
                        indexStr = split[1];
                    }
                    ArrayList ids = Lists.newArrayListWithExpectedSize((int)returnRows.size());
                    ArrayList names = Lists.newArrayListWithExpectedSize((int)returnRows.size());
                    for (ListSelectedRow returnRow : returnRows) {
                        ids.add(String.valueOf(returnRow.getPrimaryKeyValue()));
                        String name = returnRow.getName();
                        if (Boolean.parseBoolean(this.getView().getPageCache().get(INCLUDE_SUB))) {
                            name = MessageFormat.format(ResManager.loadKDString((String)"{0}\uff08\u5305\u542b\u4e0b\u7ea7\uff09", (String)"ReportUserPermEdit_4", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]), name);
                        }
                        names.add(name);
                    }
                    HashMap value = Maps.newHashMapWithExpectedSize((int)4);
                    value.put("reportId", reportId);
                    value.put("indexStr", indexStr);
                    value.put("ids", String.join((CharSequence)",", ids));
                    value.put("names", String.join((CharSequence)",", names));
                    String includeSub = this.getView().getPageCache().get(INCLUDE_SUB);
                    if (HRStringUtils.isNotEmpty((String)includeSub)) {
                        value.put("includeSub", Boolean.parseBoolean(includeSub));
                    }
                    this.invokeCustomPlugin("setDataRangeFieldF7CallBack", value);
                } else if (actionId.startsWith("OPEN_DATA_RANGE_REPORT_F7_CLOSE_CALL_BACK_PREFIX_")) {
                    ListSelectedRowCollection returnRows = (ListSelectedRowCollection)closedCallBackEvent.getReturnData();
                    if (null == returnRows || 0 == returnRows.size()) {
                        return;
                    }
                    ArrayList ids = Lists.newArrayListWithExpectedSize((int)returnRows.size());
                    for (ListSelectedRow returnRow : returnRows) {
                        ids.add(String.valueOf(returnRow.getPrimaryKeyValue()));
                    }
                    String oldReportId = this.getView().getPageCache().get("CACHE_KEY_OF_REPORT_F7_SELECT_ID");
                    this.dataProcessor.updateReportMergeTag(ids.get(0), (Object)oldReportId);
                    this.getView().getPageCache().remove("CACHE_KEY_OF_REPORT_F7_SELECT_ID");
                    this.renderUserRepPermDataRangeControl("updateDataRange");
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("closedCallBack_error_", (Throwable)exception);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        try {
            super.afterDoOperation(args);
            String operateKey = ((AbstractOperate)args.getSource()).getOperateKey();
            if ("save".equals(operateKey)) {
                this.getView().getPageCache().remove("CACHE_KEY_IS_PERM_DATA_CHANGE");
            }
        }
        catch (Exception exception) {
            LOGGER.error("afterDoOperation_error_", (Throwable)exception);
        }
    }

    private TransferTreeNodeVO initTransTreeNode() {
        DynamicObjectCollection rptFieldMapDys = REP_FIELD_MAP_HELPER.queryOriginalCollection("rptmanage", new QFilter[]{QFilter.of((String)"1 = 1", (Object[])new Object[0])});
        Set repManageIds = rptFieldMapDys.stream().filter(dy -> dy.get("rptmanage") != null).map(dy -> dy.get("rptmanage")).collect(Collectors.toSet());
        DynamicObject[] reportCenterData = ReportCenterEntityConstants.CENTER_HELPER.loadDynamicObjectArray(new QFilter[]{new QFilter("reportmanage", "in", repManageIds)});
        DynamicObject[] reportGroups = ReportCenterGroupConstants.CENTER_GROUP_HELPER.loadDynamicObjectArray(new QFilter[]{QFilter.of((String)" 1 = 1", (Object[])new Object[0])});
        int nodeSize = reportGroups.length + reportCenterData.length;
        HashMap nodeMap = Maps.newHashMapWithExpectedSize((int)nodeSize);
        HashMap parentToChildrenMaps = Maps.newHashMapWithExpectedSize((int)nodeSize);
        for (DynamicObject reportGroup : reportGroups) {
            String name = reportGroup.getString("name");
            String nodeId = reportGroup.getString("id");
            DynamicObject parentDy = reportGroup.getDynamicObject("parent");
            TransferTreeNodeVO node = new TransferTreeNodeVO(nodeId, name, Boolean.valueOf(false));
            if (parentDy != null) {
                String parentId = parentDy.getString("id");
                node.setParentId(parentId);
                TransferTreeNodeVO parentNode = (TransferTreeNodeVO)nodeMap.get(parentId);
                if (parentNode == null) {
                    parentToChildrenMaps.putIfAbsent(parentId, Lists.newArrayListWithExpectedSize((int)nodeSize));
                    parentToChildrenMaps.computeIfPresent(parentId, (oldKey, oldValue) -> {
                        oldValue.add(node);
                        return oldValue;
                    });
                } else {
                    parentNode.addChild(node);
                }
            }
            node.setIsOpened(Boolean.valueOf(true));
            nodeMap.put(nodeId, node);
            List children = (List)parentToChildrenMaps.get(nodeId);
            if (children == null) continue;
            node.setChildren(children);
            parentToChildrenMaps.remove(nodeId);
        }
        for (DynamicObject reportCenterDy : reportCenterData) {
            DynamicObject reportManageDy = reportCenterDy.getDynamicObject("reportmanage");
            DynamicObject reportGroup = reportCenterDy.getDynamicObject("reportgroup");
            String nodeId = reportManageDy.getString("id");
            String name = reportManageDy.getString("name");
            TransferTreeNodeVO node = new TransferTreeNodeVO(nodeId, name, Boolean.valueOf(false));
            String parentId = reportGroup.getString("id");
            TransferTreeNodeVO parentNode = (TransferTreeNodeVO)nodeMap.get(parentId);
            node.setReportNode(true);
            if (parentNode == null) continue;
            node.setParentId(parentId);
            parentNode.addChild(node);
        }
        TransferTreeNodeVO rootNode = (TransferTreeNodeVO)nodeMap.get(String.valueOf(1000L));
        this.deleteEmptyChildNode(rootNode);
        return rootNode;
    }

    private boolean deleteEmptyChildNode(TransferTreeNodeVO node) {
        List children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            children.removeIf(this::deleteEmptyChildNode);
            return children.isEmpty();
        }
        return !node.isReportNode();
    }

    private void initRepTransControl() {
        TransferControlDataVO transferControlDataVO = new TransferControlDataVO();
        CustomControl transBox = (CustomControl)this.getControl("hrptc_reportscope");
        TransferTreeNodeVO rootNode = this.initTransTreeNode();
        transferControlDataVO.setRootNode(rootNode);
        transferControlDataVO.setSelectIds(ReportUserPermFormUtils.getSelectedReportIsMergeMap((IDataModel)this.getModel()).keySet().stream().map(String::valueOf).collect(Collectors.toList()));
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        transferControlDataVO.setPageStatus(status.name());
        HashMap data = Maps.newHashMapWithExpectedSize((int)2);
        data.put("value", transferControlDataVO);
        data.put("date", System.currentTimeMillis());
        transBox.setData((Object)data);
    }

    private void renderPermControlData() {
        UserRepPermFieldReturnVO userRepPermFieldReturnVO = this.voProcessor.getReportPermFieldReturnVO();
        this.sendDataToControlUserPermDim(userRepPermFieldReturnVO);
        if (!userRepPermFieldReturnVO.isEmpty()) {
            this.dataProcessor.deleteSelectReport((Set)Sets.newHashSet((Iterable)userRepPermFieldReturnVO.getDeleteIds()));
            List userRepPermData = this.voProcessor.mergeUserRepPermFieldData(userRepPermFieldReturnVO);
            this.dataProcessor.updatePermDataEntry(userRepPermData, true);
        }
        this.renderUserRepPermDataRangeControl("updateDataRange");
    }

    private void initRepPermDimControl() {
        UserRepPermFieldReturnVO repPermDimReturnVO = new UserRepPermFieldReturnVO();
        Map disableFieldMap = this.voProcessor.getDisableFieldMap();
        Map selectedReportIsMergeMap = ReportUserPermFormUtils.getSelectedReportIsMergeMap((IDataModel)this.getModel());
        ArrayList userRepPermData = Lists.newArrayListWithExpectedSize((int)selectedReportIsMergeMap.size());
        DynamicObject[] fieldMapDys = REP_FIELD_MAP_HELPER.loadDynamicObjectArray(new QFilter[]{new QFilter("rptmanage", "in", selectedReportIsMergeMap.keySet())});
        Map queryFieldBoMap = ReportUserPermFormUtils.initQueryFieldBoMap((DynamicObject[])fieldMapDys);
        Map adminOrgFieldStructureVOMap = this.getAdminOrgFieldStructList(this.getView(), queryFieldBoMap.values());
        for (DynamicObject fieldMapDy : fieldMapDys) {
            userRepPermData.add(this.voProcessor.buildRepPermDimMap(fieldMapDy, disableFieldMap, selectedReportIsMergeMap, queryFieldBoMap, adminOrgFieldStructureVOMap));
        }
        repPermDimReturnVO.setInitUserRepPermFields((List)userRepPermData);
        this.sendDataToControlUserPermDim(repPermDimReturnVO);
    }

    private void sendDataToControlUserPermDim(UserRepPermFieldReturnVO repPermDimReturnVO) {
        CustomControl userPermDimsControl = (CustomControl)this.getView().getControl("userpermfields");
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        repPermDimReturnVO.setPageStatus(status.name());
        HashMap data = Maps.newHashMapWithExpectedSize((int)2);
        data.put("value", repPermDimReturnVO);
        data.put("date", System.currentTimeMillis());
        userPermDimsControl.setData((Object)data);
    }

    private void renderUserRepPermDataRangeControl(String method) {
        UserRepPermDataRangeReturnVO rangeReturnVO = this.voProcessor.getUserRepPermDataRangeReturnVO();
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        rangeReturnVO.setPageStatus(status.name());
        rangeReturnVO.sortData();
        this.sendDataToControlUserDataRange(method, rangeReturnVO);
    }

    private void sendDataToControlUserDataRange(String method, Object dataObj) {
        CustomControl userPermDimsControl = (CustomControl)this.getView().getControl("userreppermdatarange");
        HashMap data = Maps.newHashMapWithExpectedSize((int)2);
        data.put("value", dataObj);
        data.put("method", method);
        data.put("date", System.currentTimeMillis());
        userPermDimsControl.setData((Object)data);
    }

    private void openBaseDataF7(String args) {
        JSONObject dataObj = JSONObject.parseObject((String)args);
        String baseDataNumber = dataObj.getString("baseDataNumber");
        String baseDataIds = dataObj.getString("baseDataIds");
        String reportId = dataObj.getString("reportId");
        String dimSubGroupId = dataObj.getString("dimSubGroupId");
        String orgFuncId = dataObj.getString("orgFuncId");
        String orgViewSchemeNumber = dataObj.getString("orgViewSchemeNumber");
        String indexStr = dataObj.getString("indexStr");
        String includeSub = dataObj.getString("includeSub");
        String closeCallBackKey = HRStringUtils.isNotEmpty((String)reportId) ? reportId + "|" + indexStr : indexStr;
        ListShowParameter listShowParameter = ShowFormHelper.createShowListForm((String)baseDataNumber, (boolean)true, (int)0, (boolean)true);
        ArrayList<Object> ids = new ArrayList<Object>();
        if (HRStringUtils.isNotEmpty((String)baseDataIds)) {
            MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)baseDataNumber);
            ISimpleProperty primaryKey = dataEntityType.getPrimaryKey();
            boolean pkIsLong = primaryKey instanceof LongProp;
            for (String str : baseDataIds.split(",")) {
                if (!HRStringUtils.isNotEmpty((String)str)) continue;
                if (pkIsLong) {
                    ids.add(HRLongValueParseService.getInstance().parseLong((Object)str));
                    continue;
                }
                ids.add(str);
            }
            listShowParameter.setSelectedRows(ids.toArray());
        }
        listShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "OPEN_DATA_RANGE_F7_CLOSE_CALL_BACK_PREFIX_" + closeCallBackKey));
        listShowParameter.setCustomParam("struct_project_ids", (Object)SerializationUtils.toJsonString(Collections.singletonList(dimSubGroupId)));
        listShowParameter.setCustomParam("custom_parent_f7_prop", (Object)"adminorg");
        listShowParameter.setCustomParam("orgFuncId", (Object)orgFuncId);
        listShowParameter.setCustomParam("orgViewSchemeNumber", (Object)orgViewSchemeNumber);
        listShowParameter.setCustomParam(INCLUDE_SUB, (Object)includeSub);
        listShowParameter.setCustomParam("notReportCheckPerm", (Object)"true");
        listShowParameter.getOpenStyle().setShowType(ShowType.Modal);
        if (ParamsUtil.isAdminOrg((String)baseDataNumber)) {
            this.getView().getPageCache().put(INCLUDE_SUB, includeSub);
            listShowParameter.setBillFormId("hrptmc_adminorghr");
            listShowParameter.setFormId("hrptc_orgtreelistf7");
        } else {
            this.getView().getPageCache().put(INCLUDE_SUB, "false");
        }
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    private void openReportF7(String args) {
        QFilter idFilter;
        JSONObject dataObj = JSONObject.parseObject((String)args);
        String reportId = dataObj.getString("reportId");
        String index = dataObj.getString("index");
        ListShowParameter listShowParameter = ShowFormHelper.createShowListForm((String)"hrptmc_reportmanage", (boolean)false, (int)0, (boolean)true);
        ArrayList<Long> ids = new ArrayList<Long>();
        if (HRStringUtils.isNotEmpty((String)reportId)) {
            ids.add(Long.parseLong(reportId));
            listShowParameter.setSelectedRows(ids.toArray());
            this.getPageCache().put("CACHE_KEY_OF_REPORT_F7_SELECT_ID", reportId);
        }
        listShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "OPEN_DATA_RANGE_REPORT_F7_CLOSE_CALL_BACK_PREFIX_" + index));
        Set transSelectNodeIds = ReportUserPermFormUtils.getTransSelectNodeIds((IFormView)this.getView());
        List qFilters = listShowParameter.getListFilterParameter().getQFilters();
        if (!transSelectNodeIds.isEmpty()) {
            Map selectedReportIsMergeMap = ReportUserPermFormUtils.getSelectedReportIsMergeMap((IDataModel)this.getModel());
            Set independentReportIds = selectedReportIsMergeMap.entrySet().stream().filter(entry -> (Boolean)entry.getValue() == false).map(Map.Entry::getKey).map(id -> (Long)id).collect(Collectors.toSet());
            transSelectNodeIds.removeAll(independentReportIds);
            Map disableFieldMap = this.voProcessor.getDisableFieldMap();
            if (!transSelectNodeIds.isEmpty() && !disableFieldMap.isEmpty()) {
                DynamicObject[] dimFieldDys;
                for (DynamicObject dimFieldDy : dimFieldDys = REP_FIELD_MAP_HELPER.loadDynamicObjectArray(new QFilter[]{new QFilter("rptmanage", "in", (Object)transSelectNodeIds)})) {
                    DynamicObject reportDy = dimFieldDy.getDynamicObject("rptmanage");
                    String dbReportId = reportDy.getString("id");
                    Set disableFieldIds = (Set)disableFieldMap.get(dbReportId);
                    if (disableFieldIds == null || disableFieldIds.isEmpty()) continue;
                    DynamicObjectCollection dimFieldEntry = dimFieldDy.getDynamicObjectCollection("entryentity");
                    HashSet ableFieldIds = Sets.newHashSetWithExpectedSize((int)dimFieldEntry.size());
                    for (DynamicObject dimFieldEntryDy : dimFieldEntry) {
                        DynamicObject queryFieldDy = dimFieldEntryDy.getDynamicObject("aoqfield");
                        ableFieldIds.add(queryFieldDy.getString("id"));
                    }
                    ableFieldIds.removeAll(disableFieldIds);
                    if (!ableFieldIds.isEmpty()) continue;
                    transSelectNodeIds.remove(Long.parseLong(dbReportId));
                }
            }
            idFilter = !transSelectNodeIds.isEmpty() ? new QFilter("id", "in", (Object)transSelectNodeIds) : QFilter.of((String)"1 != 1", (Object[])new Object[0]);
        } else {
            idFilter = QFilter.of((String)"1 != 1", (Object[])new Object[0]);
        }
        qFilters.add(idFilter);
        listShowParameter.getOpenStyle().setShowType(ShowType.Modal);
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    private void deleteIndependentReport(String args) {
        JSONObject data = JSONObject.parseObject((String)args);
        String reportId = data.getString("reportId");
        this.dataProcessor.updateReportMergeTag(null, (Object)reportId);
        this.renderUserRepPermDataRangeControl("updateDataRange");
    }

    private void clearIndependentReport(String args) {
        JSONObject data = JSONObject.parseObject((String)args);
        String reportId = data.getString("reportId");
        this.dataProcessor.updateReportMergeTag(null, (Object)reportId);
        this.renderUserRepPermDataRangeControl("updateDataRangeOnlyMerge");
    }

    private void invokeCustomPlugin(String methodKey, Object value) {
        CustomControl commonCondition = (CustomControl)this.getView().getControl("userreppermdatarange");
        HashMap data = Maps.newHashMapWithExpectedSize((int)3);
        data.put("method", methodKey);
        data.put("date", String.valueOf(System.currentTimeMillis()));
        data.put("value", value);
        commonCondition.setData((Object)data);
    }

    private void updateSelectReportIdCache(String args) {
        IPageCache pageCache = this.getView().getPageCache();
        if (HRStringUtils.isNotEmpty((String)args)) {
            List selectReportIds = JSONArray.parseArray((String)args, Long.class);
            pageCache.put("CACHE_KEY_OF_REPORT_RANGE_SELECT_IDS", JSONArray.toJSONString((Object)selectReportIds));
        } else {
            pageCache.remove("CACHE_KEY_OF_REPORT_RANGE_SELECT_IDS");
        }
        pageCache.remove("CACHE_KEY_OF_INDEPENDENT_REPORT_SORT_INDEX");
    }

    private void setMainPosition(DynamicObject user) {
        if (user != null) {
            DynamicObjectCollection orgEntry = user.getDynamicObjectCollection("entryentity");
            for (DynamicObject orgDy : orgEntry) {
                String position;
                if (orgDy.getBoolean("ispartjob") || !HRStringUtils.isNotEmpty((String)(position = orgDy.getString("position")))) continue;
                this.getModel().setValue("userposition", (Object)position);
                break;
            }
        }
    }
}
