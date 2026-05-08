/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BeforeCreateListColumnsArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.field.events.BeforeFilterF7SelectEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.list.BillList
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdOrgRepository
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper
 *  kd.hr.haos.common.constants.masterdata.AdminOrgConstants
 *  kd.hr.haos.formplugin.web.adminorg.OrgDetailCommonListPlugin
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.haos.formplugin.web.adminorg;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.domain.adminorg.service.impl.AdOrgRepository;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper;
import kd.hr.haos.common.constants.masterdata.AdminOrgConstants;
import kd.hr.haos.formplugin.web.adminorg.OrgDetailCommonListPlugin;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;

public class OrgDetailList
extends OrgDetailCommonListPlugin {
    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        List listColumns = args.getListColumns();
        if (this.getView().getModel().getDataEntity().containsProperty("structproject")) {
            DynamicObject structProject = this.getView().getModel().getDataEntity().getDynamicObject("structproject");
            if (structProject != null) {
                if (AdminOrgConstants.ADMINORG_STRUCT.equals(structProject.getLong("id"))) {
                    this.setListColumnVisible(Boolean.FALSE, listColumns);
                } else {
                    this.setListColumnVisible(Boolean.TRUE, listColumns);
                }
                String isVirtualOrg = structProject.getString("isincludevirtualorg");
                if (HRStringUtils.equals((String)isVirtualOrg, (String)"true")) {
                    this.setIsVirtualOrgListColumnVisible(listColumns, Boolean.TRUE);
                } else {
                    this.setIsVirtualOrgListColumnVisible(listColumns, Boolean.FALSE);
                }
            } else {
                this.setListColumnVisible(Boolean.FALSE, listColumns);
                this.setIsVirtualOrgListColumnVisible(listColumns, Boolean.FALSE);
            }
        } else {
            this.setListColumnVisible(Boolean.FALSE, listColumns);
            this.setIsVirtualOrgListColumnVisible(listColumns, Boolean.FALSE);
        }
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        BillList list = (BillList)args.getHyperLinkClickEvent().getSource();
        Object primaryKeyValue = list.getFocusRowPkId();
        ListShowParameter listParameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (listParameter.isLookUp()) {
            return;
        }
        if (primaryKeyValue != null) {
            DynamicObject dynamicObject = AdOrgRepository.getInstance().queryByPk("id,name,isvirtualorg,datastatus,boid", primaryKeyValue);
            if (dynamicObject != null && HRStringUtils.equals((String)"true", (String)dynamicObject.getString("isvirtualorg")) && !HRStringUtils.equals((String)this.getView().getEntityId(), (String)"haos_orgstructlist")) {
                args.setCancel(Boolean.TRUE.booleanValue());
                BaseShowParameter baseShowParameter = new BaseShowParameter();
                baseShowParameter.setFormId("haos_virtualorgdetail");
                baseShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                if (this.getView().getMainView() != null) {
                    baseShowParameter.setPageId(primaryKeyValue.toString() + "_" + this.getView().getMainView().getPageId());
                }
                String prefix = this.getView().getModel().getDataEntity().getString("structproject.name");
                baseShowParameter.setCaption(prefix + "-" + dynamicObject.getString("name"));
                DynamicObject structProject = this.getView().getModel().getDataEntity().getDynamicObject("structproject");
                baseShowParameter.setCustomParam("custom_parent_f7_prop", (Object)"boid");
                baseShowParameter.setCustomParam("structproject", (Object)String.valueOf(structProject.getLong("id")));
                baseShowParameter.setStatus(OperationStatus.VIEW);
                baseShowParameter.setPkId(primaryKeyValue);
                this.getView().showForm((FormShowParameter)baseShowParameter);
                Date searchDate = this.getView().getModel().getDataEntity().getDate("searchdate");
                this.getView().getFormShowParameter().setCustomParam("searchdate", (Object)searchDate);
                args.setCancel(Boolean.TRUE.booleanValue());
                return;
            }
            if (dynamicObject != null && HRStringUtils.equals((String)HisModelDataStatusEnum.TO_BE_EFFECT.getStatus(), (String)dynamicObject.getString("datastatus")) && !HRStringUtils.equals((String)this.getView().getEntityId(), (String)"haos_orgstructlist")) {
                args.setCancel(Boolean.TRUE.booleanValue());
                BaseShowParameter baseShowParameter = new BaseShowParameter();
                baseShowParameter.setFormId("haos_adminorgdetailfuture");
                baseShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                baseShowParameter.setCustomParam("custom_parent_f7_prop", (Object)"boid");
                baseShowParameter.setCustomParam("structproject", (Object)"1010");
                baseShowParameter.setCustomParam("isShowScheduleEffect", (Object)"1");
                baseShowParameter.setStatus(OperationStatus.VIEW);
                baseShowParameter.setPkId(primaryKeyValue);
                if (this.getView().getMainView() != null) {
                    baseShowParameter.setPageId(primaryKeyValue.toString() + "_" + this.getView().getMainView().getPageId());
                }
                this.getView().showForm((FormShowParameter)baseShowParameter);
                return;
            }
            if (dynamicObject != null && HRStringUtils.equals((String)list.getBillFormId(), (String)"haos_adminorgdetailfuture") && !HRStringUtils.equals((String)HisModelDataStatusEnum.TO_BE_EFFECT.getStatus(), (String)dynamicObject.getString("datastatus"))) {
                args.setCancel(Boolean.TRUE.booleanValue());
                new AdminOrgDetailHelper().showAdminOrgViewPage((AbstractFormPlugin)this, dynamicObject.getLong("boid"), dynamicObject.getString("name"), null);
                return;
            }
        }
        if (!listParameter.isLookUp()) {
            Date searchDate = this.getView().getModel().getDataEntity().getDate("searchdate");
            this.getView().getFormShowParameter().setCustomParam("searchdate", (Object)searchDate);
        }
    }

    public void beforeShowBill(BeforeShowBillFormEvent e) {
        BillShowParameter parameter;
        ListShowParameter listParameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (!listParameter.isLookUp()) {
            String searchDateStr = this.getView().getPageCache().get("searchdate");
            if (StringUtils.isEmpty((CharSequence)searchDateStr)) {
                e.getParameter().setCustomParam("searchdate", (Object)new Date());
            } else {
                try {
                    searchDateStr = searchDateStr.replaceAll("\"", "");
                    Date searchDate = HRDateTimeUtils.parseDate((String)searchDateStr, (String)"yyyy-MM-dd");
                    e.getParameter().setCustomParam("searchdate", (Object)searchDate);
                }
                catch (ParseException parseException) {
                    LOGGER.error((Throwable)parseException);
                    throw new KDBizException(new ErrorCode("OrgDetailList", parseException.getMessage()), new Object[0]);
                }
            }
        }
        if (!(parameter = e.getParameter()).getStatus().equals((Object)OperationStatus.ADDNEW)) {
            Object pkId = parameter.getPkId();
            DynamicObject dataEntity = AdOrgRepository.getInstance().queryByPk("name", pkId);
            String name = dataEntity.getString("name");
            String prefix = this.getView().getModel().getDataEntity().getString("structproject.name");
            parameter.setCaption(prefix + "-" + name);
        }
        e.getParameter().setCustomParam("structproject", (Object)String.valueOf(this.getView().getModel().getDataEntity().getLong("structproject.id")));
    }

    public void filterContainerBeforeF7Select(BeforeFilterF7SelectEvent args) {
        if ("belongcompany.name".equals(args.getFieldName())) {
            args.addCustomQFilter(new QFilter("adminorgtype.adminorgtypestd.id", "in", Arrays.asList(1010L, 1020L)));
        } else if ("parentorg.id".equals(args.getFieldName())) {
            args.addCustomQFilter(new QFilter("otclassify", "=", (Object)1010L));
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        switch (formOperate.getOperateKey()) {
            case "showchargeperson": {
                if (this.getSelectedRows().size() <= 1) break;
                String msg = ResManager.loadKDString((String)"\u53ea\u80fd\u9009\u62e91\u6761\u6570\u636e\u8fdb\u884c\u8bbe\u7f6e\u3002", (String)"OrgDetailList_02", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
                this.getView().showTipNotification(msg);
                args.setCancel(true);
                break;
            }
        }
    }
}
