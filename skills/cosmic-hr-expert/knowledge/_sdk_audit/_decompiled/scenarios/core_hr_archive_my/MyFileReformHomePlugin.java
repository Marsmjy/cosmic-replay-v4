/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.AnchorItems
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.ext.form.control.AnchorControl
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.control.Button
 *  kd.bos.form.control.embedform.BeforeShowEmbedFormEvent
 *  kd.bos.form.control.embedform.EmbedForm
 *  kd.bos.form.control.embedform.EmbedFormListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.ClientCallBackEvent
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.common.util.HRMapUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hspm.business.util.CollectFileSupport
 *  kd.hr.hspm.business.util.FileViewUtil
 *  kd.hr.hspm.formplugin.web.reform.file.web.support.FileHomeSupport
 *  kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService
 *  kd.sdk.hr.hspm.business.internal.approval.ApprovalBillSupport
 *  kd.sdk.hr.hspm.business.internal.approval.FileViewSupport
 *  kd.sdk.hr.hspm.common.constants.HspmCommonConstants
 *  kd.sdk.hr.hspm.common.utils.PageCacheUtils
 */
package kd.hr.hspm.formplugin.web.reform.file.web.employee;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.AnchorItems;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.ext.form.control.AnchorControl;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.control.Button;
import kd.bos.form.control.embedform.BeforeShowEmbedFormEvent;
import kd.bos.form.control.embedform.EmbedForm;
import kd.bos.form.control.embedform.EmbedFormListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.ClientCallBackEvent;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.common.util.HRMapUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hspm.business.util.CollectFileSupport;
import kd.hr.hspm.business.util.FileViewUtil;
import kd.hr.hspm.formplugin.web.reform.file.web.support.FileHomeSupport;
import kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService;
import kd.sdk.hr.hspm.business.internal.approval.ApprovalBillSupport;
import kd.sdk.hr.hspm.business.internal.approval.FileViewSupport;
import kd.sdk.hr.hspm.common.constants.HspmCommonConstants;
import kd.sdk.hr.hspm.common.utils.PageCacheUtils;

public final class MyFileReformHomePlugin
extends HRDataBaseEdit
implements EmbedFormListener,
HspmCommonConstants {
    private static final Log LOGGER = LogFactory.getLog(MyFileReformHomePlugin.class);
    private static final String OPERATION_KEY_SUBMITAUDIT = "submitaudit";

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        Long paramEmployeeId = (Long)this.getView().getFormShowParameter().getCustomParam("employee");
        Long paramAssignmentId = (Long)this.getView().getFormShowParameter().getCustomParam("assignment");
        this.initPage();
        DynamicObject assignmentDy = null;
        assignmentDy = paramAssignmentId != null && paramAssignmentId != 0L ? IAssignmentDomainService.getInstance().queryOriginalOne("id,employee.id", new QFilter[]{new QFilter("id", "=", (Object)paramAssignmentId)}) : (paramEmployeeId != null && paramEmployeeId != 0L ? FileViewSupport.getPrimaryAssignment((long)paramEmployeeId) : FileViewUtil.getLoginUserAssignment());
        if (Objects.isNull(assignmentDy)) {
            this.getView().getPageCache().put("personId", "0");
            LOGGER.error("MyFileReformHomePlugin#get assignmentDy null###{}", (Object)paramAssignmentId);
            return;
        }
        long assignmentId = assignmentDy.getLong("id");
        long employeeId = assignmentDy.getLong("employee.id");
        this.getView().getFormShowParameter().setCustomParam("employee", (Object)employeeId);
        this.getModel().setValue("assignment", (Object)assignmentId);
        long fileViewId = FileViewUtil.getFileViewIdByAssignmentId((long)assignmentId, (long)assignmentDy.getLong("employee.id"), (String)"1");
        CollectFileSupport collectFileSupport = new CollectFileSupport(this.getView(), false);
        collectFileSupport.setCollectContentTips();
        this.getModel().setValue("fileview", (Object)fileViewId);
        LOGGER.info("MyFileReformHomePlugin match fileview is :{},{}", (Object)fileViewId, this.getModel().getValue("fileview"));
        this.getView().getPageCache().put("personId", assignmentDy.getLong("employee.id") + "");
        FileHomeSupport fileHomeSupport = new FileHomeSupport(this.getView(), new LinkedHashMap(32), new HashMap(32), fileViewId);
        fileHomeSupport.setSelf("1");
        fileHomeSupport.setEmployeeId(employeeId);
        fileHomeSupport.setEmbedFormCache();
        fileHomeSupport.dealFileViewDetail();
        fileHomeSupport.putHeadEmbedInCache();
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        FileHomeSupport fileHomeSupport = new FileHomeSupport(this.getView(), "1");
        fileHomeSupport.initAnchor();
        this.showAndHiddenEmbedInfo(true);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        FileHomeSupport fileHomeSupport = new FileHomeSupport(this.getView(), "1");
        fileHomeSupport.closeCall(closedCallBackEvent);
    }

    public void click(EventObject evt) {
        super.click(evt);
        if (evt.getSource() instanceof Button) {
            String key = ((Button)evt.getSource()).getKey();
            if ("auditRecord".equalsIgnoreCase(key)) {
                ApprovalBillSupport.viewAuditRecord((IFormView)this.getView());
            } else if ("changeRecord".equalsIgnoreCase(key)) {
                ApprovalBillSupport.viewChangeRecord((IFormView)this.getView());
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        OperationResult operationResult = afterDoOperationEventArgs.getOperationResult();
        if (operationResult == null || !operationResult.isSuccess()) {
            return;
        }
        if (OPERATION_KEY_SUBMITAUDIT.equals(afterDoOperationEventArgs.getOperateKey())) {
            ApprovalBillSupport.submitConfirm((IFormView)this.getView(), (IFormPlugin)this);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        super.confirmCallBack(messageBoxClosedEvent);
        MessageBoxResult result = messageBoxClosedEvent.getResult();
        if (!MessageBoxResult.Yes.equals((Object)result) && !MessageBoxResult.OK.equals((Object)result)) {
            return;
        }
        String key = messageBoxClosedEvent.getCallBackId();
        if ("ApprovalSubmitCallBack".equals(key)) {
            ApprovalBillSupport.submit((IFormView)this.getView());
        }
    }

    public void clientCallBack(ClientCallBackEvent clientCallBackEvent) {
        String name = clientCallBackEvent.getName();
        String embedItemsViewMainStr = this.getPageCache().getBigObject("embedform_items_view_main");
        List embedItemsViewList = new ArrayList();
        if (!HRStringUtils.isEmpty((String)embedItemsViewMainStr)) {
            embedItemsViewList = SerializationUtils.fromJsonStringToList((String)embedItemsViewMainStr, String.class);
        }
        if (embedItemsViewList.contains(name)) {
            ArrayList groupFieldsList = new ArrayList();
            Map allCache = PageCacheUtils.getHomePageCache((IFormView)this.getView()).getAll();
            for (Map.Entry entry : allCache.entrySet()) {
                String key = (String)entry.getKey();
                if (HRStringUtils.isEmpty((String)key) || !key.endsWith("-entrycache")) continue;
                String cacheValue = (String)entry.getValue();
                List groupFields = (List)SerializationUtils.fromJsonString((String)cacheValue, List.class);
                groupFieldsList.addAll(groupFields);
            }
            for (Map map : groupFieldsList) {
                Object status = map.get("status");
                if (status == null || !"1".equals(status.toString())) continue;
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"notpsshint"});
                return;
            }
        }
    }

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        this.addClickListeners(new String[]{"auditRecord", "changeRecord", "submit"});
    }

    public void beforeShowEmbedForm(BeforeShowEmbedFormEvent event) {
        FileHomeSupport fileHomeSupport = new FileHomeSupport(this.getView(), "1");
        String employeeId = this.getView().getPageCache().get("personId");
        if (HRStringUtils.isNotEmpty((String)employeeId)) {
            fileHomeSupport.setEmployeeId(Long.parseLong(employeeId));
        }
        fileHomeSupport.dealBeforeShowEmbed(event);
    }

    private void initPage() {
        this.getView().setVisible(Boolean.valueOf(false), new String[]{"auditinghint", "notpsshint"});
        this.getView().setVisible(Boolean.valueOf(true), new String[]{"submit"});
    }

    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        IDataEntityProperty property = e.getProperty();
        String name = property.getName();
        ChangeData[] changeSet = e.getChangeSet();
        if ("showcollectbox".equals(name)) {
            Object newValue = changeSet[0].getNewValue();
            this.showAndHiddenEmbedInfo((Boolean)newValue);
        }
    }

    private void showAndHiddenEmbedInfo(boolean onlyShowCollect) {
        String collectStr = this.getPageCache().getBigObject("collect_entity_num");
        if (HRStringUtils.isEmpty((String)collectStr)) {
            return;
        }
        Map collectEntityMap = (Map)SerializationUtils.fromJsonString((String)collectStr, Map.class);
        if (HRMapUtils.isEmpty((Map)collectEntityMap)) {
            return;
        }
        Map embedAndParentMap = new HashMap(48);
        String embedAndParentStr = this.getPageCache().getBigObject("embedform_and_parent");
        if (!HRStringUtils.isEmpty((String)embedAndParentStr)) {
            embedAndParentMap = (Map)SerializationUtils.fromJsonString((String)embedAndParentStr, Map.class);
        }
        Map entityAndEmbedMap = new HashMap(48);
        String embedAndEntityStr = this.getPageCache().getBigObject("embedform_and_entity");
        if (!HRStringUtils.isEmpty((String)embedAndEntityStr)) {
            entityAndEmbedMap = (Map)SerializationUtils.fromJsonString((String)embedAndEntityStr, Map.class);
        }
        List cnfEntityList = new ArrayList(48);
        String cnfEntityStr = this.getPageCache().getBigObject("embedform_items_view_main");
        if (HRStringUtils.isNotEmpty((String)cnfEntityStr)) {
            cnfEntityList = (List)SerializationUtils.fromJsonString((String)cnfEntityStr, List.class);
        }
        String anchorCache = this.getPageCache().getBigObject("anchorctrol_items");
        List items = new ArrayList(48);
        Map<Object, Object> anchorMap = new HashMap(48);
        if (HRStringUtils.isNotEmpty((String)anchorCache)) {
            items = SerializationUtils.fromJsonStringToList((String)anchorCache, AnchorItems.class);
            anchorMap = items.stream().collect(Collectors.toMap(AnchorItems::getTarget, item -> item, (k1, k2) -> k1));
        }
        AnchorControl anchorControl = (AnchorControl)this.getView().getControl("anchorcontrolap");
        ArrayList<AnchorItems> collectAnchorItems = new ArrayList<AnchorItems>(collectEntityMap.size());
        for (Map.Entry entries : entityAndEmbedMap.entrySet()) {
            String advKey;
            String entityNum = (String)entries.getKey();
            String embedKey = (String)entries.getValue();
            if (onlyShowCollect) {
                advKey = (String)embedAndParentMap.get(embedKey);
                if (!collectEntityMap.containsKey(entityNum)) {
                    this.getView().setVisible(Boolean.valueOf(false), new String[]{advKey, embedKey});
                    continue;
                }
                AnchorItems anchorItems = (AnchorItems)anchorMap.get(advKey);
                if (anchorItems == null) continue;
                collectAnchorItems.add(anchorItems);
                continue;
            }
            if (CollectionUtils.isEmpty(cnfEntityList) || cnfEntityList.contains(entityNum)) {
                advKey = (String)embedAndParentMap.get(embedKey);
                this.getView().setVisible(Boolean.valueOf(true), new String[]{advKey, embedKey});
                EmbedForm control = (EmbedForm)this.getView().getControl(embedKey);
                if (control != null) {
                    control.showForm();
                }
            }
            anchorControl.addItems(items);
        }
        if (onlyShowCollect && !CollectionUtils.isEmpty(collectAnchorItems)) {
            anchorControl.addItems(collectAnchorItems);
        }
        this.getView().setVisible(Boolean.valueOf(true), new String[]{"anchorcontrolap"});
        Set collectSet = collectEntityMap.keySet();
        String collectEntity = cnfEntityList.stream().filter(collectSet::contains).findAny().orElse(null);
        if (onlyShowCollect && HRStringUtils.isEmpty((String)collectEntity)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"anchorcontrolap"});
        }
    }
}
