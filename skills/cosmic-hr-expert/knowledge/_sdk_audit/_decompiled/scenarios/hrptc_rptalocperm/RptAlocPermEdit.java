/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.entity.datamodel.events.BeforeDeleteRowEventArgs
 *  kd.bos.entity.property.EntryProp
 *  kd.bos.ext.hr.ruleengine.utils.IDStringUtils
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.id.ID
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hrptc.common.constant.perm.ReportUserPermConstants
 *  kd.hr.hrptc.common.constant.permission.EntityConstants
 *  kd.hr.hrptc.formplugin.perm.model.AllotUserVO
 *  kd.hr.hrptc.formplugin.perm.model.PermFieldData
 *  kd.hr.hrptc.formplugin.perm.model.PermGroup
 *  kd.hr.hrptc.formplugin.perm.model.RptAllotUserPermReturnVO
 *  kd.hr.hrptc.formplugin.perm.processor.RptAlocPermVOProcessor
 *  kd.hr.hrptmc.business.repdesign.RptCenterPublishService
 */
package kd.hr.hrptc.formplugin.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.entity.datamodel.events.BeforeDeleteRowEventArgs;
import kd.bos.entity.property.EntryProp;
import kd.bos.ext.hr.ruleengine.utils.IDStringUtils;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.id.ID;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hrptc.common.constant.perm.ReportUserPermConstants;
import kd.hr.hrptc.common.constant.permission.EntityConstants;
import kd.hr.hrptc.formplugin.perm.model.AllotUserVO;
import kd.hr.hrptc.formplugin.perm.model.PermFieldData;
import kd.hr.hrptc.formplugin.perm.model.PermGroup;
import kd.hr.hrptc.formplugin.perm.model.RptAllotUserPermReturnVO;
import kd.hr.hrptc.formplugin.perm.processor.RptAlocPermVOProcessor;
import kd.hr.hrptmc.business.repdesign.RptCenterPublishService;

public final class RptAlocPermEdit
extends HRBaseDataCommonEdit
implements EntityConstants,
ReportUserPermConstants {
    private static final String CALLBACK_ALLOT_USER = "callback_allot_user";
    private static final String OP_ALLOT_USER = "allotuser";
    private static final String OP_USER_PERM = "userperm";
    private final RptAlocPermVOProcessor voProcessor = new RptAlocPermVOProcessor((AbstractFormPlugin)this);

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        Long rptManageId = this.getRptManageId();
        if (IDStringUtils.idNotEmpty((Long)rptManageId)) {
            this.getModel().setValue("rptmanage", (Object)rptManageId);
            String publishPath = RptCenterPublishService.getPublishPath((Long)rptManageId);
            this.getModel().setValue("publishpath", (Object)publishPath);
        }
        this.getModel().setDataChanged(false);
    }

    public void afterCreateNewData(EventObject evt) {
        super.afterCreateNewData(evt);
        this.getModel().setValue("id", (Object)ID.genLongId());
    }

    public void afterLoadData(EventObject evt) {
        super.afterLoadData(evt);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if (OP_ALLOT_USER.equals(operateKey)) {
            DynamicObjectCollection coll = this.getModel().getEntryEntity("entryentity");
            List<Long> selectedUserIds = coll.stream().map(dy -> dy.getDynamicObject("disuser").getLong("id")).collect(Collectors.toList());
            this.showForm(selectedUserIds);
        } else if (OP_USER_PERM.equals(operateKey)) {
            this.showForm(null);
        }
    }

    public void beforeDeleteRow(BeforeDeleteRowEventArgs rowEventArgs) {
        super.beforeDeleteRow(rowEventArgs);
        EntryProp entryProp = rowEventArgs.getEntryProp();
        if ("entryentity".equals(entryProp.getName())) {
            int[] rowIndices;
            for (int i : rowIndices = rowEventArgs.getRowIndexs()) {
                DynamicObject disDy = this.getModel().getEntryRowEntity("entryentity", i);
                long userId = disDy.getDynamicObject("disuser").getLong("id");
                DynamicObjectCollection coll = this.getModel().getEntryEntity("rptpermdataentry");
                coll.removeIf(dy2 -> dy2.getDynamicObject("user").getLong("id") == userId);
            }
            this.getView().updateView("rptpermdataentry");
        }
    }

    private void showForm(List<Long> selectedUserIds) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrptc_allotuser");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        if (null == selectedUserIds) {
            showParameter.setCustomParam("allotUserVO", (Object)SerializationUtils.toJsonString((Object)this.getAllotUserVO()));
            showParameter.setStatus(OperationStatus.EDIT);
        } else {
            showParameter.setCustomParam("allotUserVO", (Object)SerializationUtils.toJsonString((Object)this.getInitAllotUserVO()));
            showParameter.setCustomParam("selectedUserIds", selectedUserIds);
            showParameter.setStatus(OperationStatus.ADDNEW);
        }
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CALLBACK_ALLOT_USER));
        this.getView().showForm(showParameter);
    }

    private AllotUserVO getAllotUserVO() {
        AllotUserVO vo = new AllotUserVO();
        int index = this.getModel().getEntryCurrentRowIndex("entryentity");
        DynamicObject dy = this.getModel().getEntryRowEntity("entryentity", index);
        long userId = dy.getDynamicObject("disuser").getLong("id");
        vo.setUserId(Long.valueOf(userId));
        vo.setStartDate(dy.getDate("disstartdate"));
        vo.setEndDate(dy.getDate("disenddate"));
        DynamicObjectCollection coll = this.getModel().getEntryEntity("rptpermdataentry");
        coll.removeIf(dy2 -> dy2.getDynamicObject("user").getLong("id") != userId);
        vo.setVo(this.voProcessor.getPackageData(this.getRptManageId().longValue(), coll));
        return vo;
    }

    private AllotUserVO getInitAllotUserVO() {
        AllotUserVO vo = new AllotUserVO();
        vo.setStartDate(HRDateTimeUtils.getNowDate());
        vo.setEndDate(HRDateTimeUtils.getSysMaxDate());
        vo.setVo(this.voProcessor.getPermInitData(this.getRptManageId().longValue()));
        return vo;
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        String actionId = event.getActionId();
        if (HRStringUtils.equals((String)actionId, (String)CALLBACK_ALLOT_USER)) {
            AllotUserVO vo = (AllotUserVO)event.getReturnData();
            this.setDisplayFieldEntry(vo);
            this.setEntryEntity(vo);
        }
    }

    private void setEntryEntity(AllotUserVO vo) {
        if (vo == null) {
            return;
        }
        RptAllotUserPermReturnVO returnVO = vo.getVo();
        List permGroups = returnVO.getMergeRepPermData();
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        long rptManageId = this.getRptManageId();
        DynamicObjectCollection coll = this.getModel().getEntryEntity("rptpermdataentry");
        List delIdx = coll.stream().filter(dy -> vo.getUserId().longValue() == dy.getDynamicObject("user").getLong("id") && rptManageId == dy.getDynamicObject("report").getLong("id")).map(dy -> dy.getInt("seq")).collect(Collectors.toList());
        int[] idxs = new int[delIdx.size()];
        for (int i = 0; i < delIdx.size(); ++i) {
            idxs[i] = (Integer)delIdx.get(i) - 1;
        }
        model.deleteEntryRows("rptpermdataentry", idxs);
        int count = model.getEntryRowCount("rptpermdataentry");
        if (!CollectionUtils.isEmpty((Collection)permGroups)) {
            for (PermGroup permGroup : permGroups) {
                for (PermFieldData permFieldData : permGroup.getUserRepPermFieldData()) {
                    if (StringUtils.isNotEmpty((CharSequence)permFieldData.getData()) || permFieldData.isNoLimit()) {
                        model.batchCreateNewEntryRow("rptpermdataentry", 1);
                        this.setFieldEntry(permFieldData, vo, rptManageId, permGroup.getGroupKey(), count++);
                    }
                    for (PermFieldData subPermFieldData : permFieldData.getSubGroupFields()) {
                        if (!StringUtils.isNotEmpty((CharSequence)subPermFieldData.getData()) && !subPermFieldData.isNoLimit()) continue;
                        model.batchCreateNewEntryRow("rptpermdataentry", 1);
                        this.setFieldEntry(subPermFieldData, vo, rptManageId, permGroup.getGroupKey(), count++);
                    }
                }
            }
        }
        model.endInit();
        this.getView().updateView("rptpermdataentry");
    }

    private void setFieldEntry(PermFieldData permFieldData, AllotUserVO vo, long rptManageId, String groupKey, int count) {
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.setValue("user", (Object)vo.getUserId(), count);
        model.setValue("modifyuser", (Object)RequestContext.get().getCurrUserId(), count);
        model.setValue("startdate", (Object)vo.getStartDate(), count);
        model.setValue("enddate", (Object)vo.getEndDate(), count);
        model.setValue("report", (Object)rptManageId, count);
        model.setValue("queryfield", (Object)Long.parseLong(permFieldData.getFieldId()), count);
        model.setValue("ismerge", (Object)false, count);
        model.setValue("data", (Object)permFieldData.getData(), count);
        model.setValue("permgroup", (Object)groupKey, count);
        model.setValue("adminorgstruct", (Object)permFieldData.getDimSubGroupId(), count);
        model.setValue("mustinput", (Object)permFieldData.isMustInput(), count);
        model.setValue("nolimit", (Object)permFieldData.isNoLimit(), count);
        model.setValue("includesub", (Object)permFieldData.isIncludeSub(), count);
    }

    private void setDisplayFieldEntry(AllotUserVO vo) {
        int i;
        if (null == vo) {
            return;
        }
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        DynamicObjectCollection coll = this.getModel().getEntryEntity("entryentity");
        for (i = 0; i < coll.size(); ++i) {
            if (vo.getUserId().longValue() != ((DynamicObject)coll.get(i)).getDynamicObject("disuser").getLong("id")) continue;
            this.setDisplayFieldEntry(model, vo, i);
            break;
        }
        if (i == coll.size()) {
            model.batchCreateNewEntryRow("entryentity", 1);
            model.setValue("disuser", (Object)vo.getUserId(), i);
            this.setDisplayFieldEntry(model, vo, i);
        }
        model.endInit();
        this.getView().updateView("entryentity");
    }

    private void setDisplayFieldEntry(AbstractFormDataModel model, AllotUserVO vo, int i) {
        model.setValue("dismodifyuser", (Object)RequestContext.get().getCurrUserId(), i);
        model.setValue("disstartdate", (Object)vo.getStartDate(), i);
        model.setValue("disenddate", (Object)vo.getEndDate(), i);
        model.setValue("disrptismerge", (Object)false, i);
        model.setValue("disreport", (Object)this.getRptManageId(), i);
    }

    private void setDisplayFieldEntry(DynamicObjectCollection coll) {
        if (null == coll || coll.isEmpty()) {
            return;
        }
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        TableValueSetter vs = new TableValueSetter(new String[0]);
        vs.addField("disuser", new Object[0]);
        vs.addField("dismodifyuser", new Object[0]);
        vs.addField("disstartdate", new Object[0]);
        vs.addField("disenddate", new Object[0]);
        ArrayList<Long> userIds = new ArrayList<Long>(coll.size());
        for (DynamicObject dy : coll) {
            Long userId = dy.getDynamicObject("user").getLong("id");
            if (userIds.contains(userId)) continue;
            vs.addRow(new Object[]{userId, dy.getDynamicObject("modifyuser").getLong("id"), dy.getDate("startdate"), dy.getDate("enddate")});
            userIds.add(userId);
        }
        model.batchCreateNewEntryRow("entryentity", vs);
        model.endInit();
        this.getView().updateView("entryentity");
    }

    private Long getRptManageId() {
        return this.getModel().getDataEntity().getLong("rptmanage.id");
    }
}
