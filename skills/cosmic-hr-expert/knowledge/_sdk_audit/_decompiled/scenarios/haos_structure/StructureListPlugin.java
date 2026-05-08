/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.BeforePackageDataEvent
 *  kd.bos.entity.operate.Donothing
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.BillListHyperLinkClickEvent
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.BillList
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.portal.util.SerializationUtils
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.haos.business.domain.common.service.impl.SystemParamHelper
 *  kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository
 *  kd.hr.haos.business.util.OrgPermHelper
 *  kd.hr.haos.common.constants.structproject.StructProjectConstants
 *  kd.hr.hbp.business.servicehelper.AppIdServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.model.AuthorizedStructResult
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hbp.formplugin.web.util.perm.HRPermUtil
 */
package kd.hr.haos.formplugin.web.structures;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.BeforePackageDataEvent;
import kd.bos.entity.operate.Donothing;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.BillListHyperLinkClickEvent;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.BillList;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.portal.util.SerializationUtils;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.haos.business.domain.common.service.impl.SystemParamHelper;
import kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository;
import kd.hr.haos.business.util.OrgPermHelper;
import kd.hr.haos.common.constants.structproject.StructProjectConstants;
import kd.hr.hbp.business.servicehelper.AppIdServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.model.AuthorizedStructResult;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hbp.formplugin.web.util.perm.HRPermUtil;

public final class StructureListPlugin
extends HRDataBaseList
implements ClickListener {
    private static final Log LOG = LogFactory.getLog(StructureListPlugin.class);
    private static final String CREATOR_HAS_PERMISSION = "creatorhaspermission";

    public void setFilter(SetFilterEvent event) {
        super.setFilter(event);
        QFilter noPreSetFilter = new QFilter("issyspreset", "=", (Object)"0");
        noPreSetFilter.or("id", "=", (Object)StructProjectConstants.STRUCT_PROJECT_MANAGE);
        event.getQFilters().add(noPreSetFilter);
        event.getQFilters().add(new QFilter("enable", "in", (Object)new String[]{"1", "0"}));
        HasPermOrgResult hrPermOrg = OrgPermHelper.getHRPermOrg((boolean)false);
        LOG.info("hrPermOrg:{}", (Object)hrPermOrg.hasAllOrgPerm());
        List hasPermOrgs = hrPermOrg.getHasPermOrgs();
        LOG.info("hrPermOrg orgs:{}", (Object)hasPermOrgs);
        HashSet hasPermOrgsSet = new HashSet(hasPermOrgs);
        Map batchOrgParameter = SystemParamHelper.getBatchOrgParameter(hasPermOrgsSet, (String)CREATOR_HAS_PERMISSION);
        LOG.info("batchOrgParameter: {}", (Object)batchOrgParameter);
        HashSet<Long> paramEnableOrgSet = new HashSet<Long>();
        for (Long orgId : hasPermOrgsSet) {
            boolean paramBool = (Boolean)batchOrgParameter.get(orgId);
            if (!paramBool) continue;
            paramEnableOrgSet.add(orgId);
        }
        long currUserId = RequestContext.get().getCurrUserId();
        QFilter creatorParamFilter = new QFilter("org.id", "in", paramEnableOrgSet).and(new QFilter("creator.id", "=", (Object)currUserId));
        HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_structure");
        DynamicObject[] structures = helper.queryOriginalArray("id", creatorParamFilter.toArray());
        Set structIds = Arrays.stream(structures).map(dy -> dy.getLong("id")).collect(Collectors.toSet());
        LOG.info("creator is myself structIds: {}", structIds);
        String appId = this.getAppIdWithDealThirdApp(this.getView().getFormShowParameter(), "haos_structure");
        AuthorizedStructResult permResult = (AuthorizedStructResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getUserStructProjectsF7", (Object[])new Object[]{currUserId, appId, "haos_structure", "47150e89000000ac", "rootorg", null});
        LOG.info("Struct permResult: {}", (Object)permResult.isHasAllStruct());
        if (permResult != null && !permResult.isHasAllStruct() && !permResult.getAuthorizedStructs().isEmpty()) {
            List authorizedStructs = permResult.getAuthorizedStructs();
            LOG.info("Struct permResults ids: {}", (Object)authorizedStructs);
            structIds.addAll(authorizedStructs);
        }
        event.getQFilters().add(new QFilter("id", "in", structIds));
        event.setOrderBy("enable desc,number asc");
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        String fieldName = args.getFieldName();
        ListSelectedRow currentRow = ((BillListHyperLinkClickEvent)args.getHyperLinkClickEvent()).getCurrentRow();
        if (HRStringUtils.equals((String)"name", (String)fieldName) || HRStringUtils.equals((String)"cardlistcolumnap", (String)fieldName) || HRStringUtils.equals((String)"cardlistcolumnap1", (String)fieldName)) {
            HRBaseServiceHelper helper;
            DynamicObject structure;
            DynamicObject rootorgDy;
            String appId = this.getView().getFormShowParameter().getCheckRightAppId();
            boolean hasPermission = PermissionServiceHelper.checkPermission((Long)RequestContext.get().getCurrUserId(), (String)appId, (String)"haos_structure", (String)"3F/95X2VSZ=1");
            if (!hasPermission) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u65e0\u201c\u77e9\u9635\u7ec4\u7ec7\u7ef4\u62a4\u201d\u7684\u201c\u7ef4\u62a4\u67b6\u6784\u201d\u6743\u9650\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u3002", (String)"StructureListPlugin_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            if (!HRObjectUtils.isEmpty((Object)currentRow) && HRObjectUtils.isEmpty((Object)(rootorgDy = (structure = (helper = new HRBaseServiceHelper("haos_structure")).queryOne("org, creator, id, enable,rootorg", new QFilter("id", "=", currentRow.getPrimaryKeyValue()))).getDynamicObject("rootorg")))) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5f53\u524d\u77e9\u9635\u67b6\u6784\u65e0\u6839\u7ec4\u7ec7\uff0c\u8bf7\u524d\u5f80\u201c\u7ec4\u7ec7\u7ba1\u7406>\u884c\u653f\u7ec4\u7ec7\u7ef4\u62a4>\u77e9\u9635\u7ec4\u7ec7\u8bbe\u7f6e\u201d\u7ef4\u62a4\u5176\u6839\u7ec4\u7ec7\u4fe1\u606f\u540e\u518d\u8fdb\u884c\u67b6\u6784\u7ef4\u62a4\u3002", (String)"StructureListPlugin_1", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            BillList list = (BillList)args.getHyperLinkClickEvent().getSource();
            Object primaryKeyValue = list.getFocusRowPkId();
            this.showStructListPage(primaryKeyValue);
            args.setCancel(true);
        }
    }

    private void showStructListPage(Object primaryKeyValue) {
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        String mainPageId = "";
        if (this.getView().getMainView() != null) {
            mainPageId = this.getView().getMainView().getPageId();
        }
        listShowParameter.setPageId("haos_orgstructlist_" + primaryKeyValue.toString() + "_" + mainPageId);
        listShowParameter.setFormId("haos_orgstructlist");
        listShowParameter.setBillFormId("haos_structorgdetail");
        listShowParameter.setCustomParam("custom_parent_f7_prop", (Object)"boid");
        listShowParameter.setCustomParam("struct_project_ids", (Object)SerializationUtils.toJsonString(Collections.singletonList(primaryKeyValue)));
        DynamicObject structDy = StructProjectRepository.getInstance().queryByPk("id,name", primaryKeyValue);
        listShowParameter.setCaption(structDy.getString("name"));
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    public void beforePackageData(BeforePackageDataEvent event) {
        super.beforePackageData(event);
        boolean isNeedAudit = HRBaseDataConfigUtil.getAudit((String)"haos_structure");
        DynamicObjectCollection pageData = event.getPageData();
        if (!isNeedAudit) {
            for (DynamicObject pageDatum : pageData) {
                pageDatum.set("status", (Object)"Z");
            }
        }
    }

    private String getAppIdWithDealThirdApp(FormShowParameter showParameter, String entityNumber) {
        String entryAppId = HRPermUtil.getAppIdFromShowParam((FormShowParameter)showParameter);
        return AppIdServiceHelper.getPermAppId((String)entryAppId, (String)entityNumber);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate operate = (AbstractOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        ListSelectedRowCollection selectedRows = this.getSelectedRows();
        Object[] keyValues = selectedRows.getPrimaryKeyValues();
        if ("vectorap".equals(operateKey)) {
            HRBaseServiceHelper helper;
            DynamicObject structure;
            DynamicObject rootorgDy;
            String appId = this.getView().getFormShowParameter().getCheckRightAppId();
            boolean hasPermission = PermissionServiceHelper.checkPermission((Long)RequestContext.get().getCurrUserId(), (String)appId, (String)"haos_structure", (String)"3F/95X2VSZ=1");
            if (!hasPermission) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u65e0\u201c\u77e9\u9635\u7ec4\u7ec7\u7ef4\u62a4\u201d\u7684\u201c\u7ef4\u62a4\u67b6\u6784\u201d\u6743\u9650\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u3002", (String)"StructureListPlugin_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                return;
            }
            if (!HRObjectUtils.isEmpty((Object)keyValues) && HRObjectUtils.isEmpty((Object)(rootorgDy = (structure = (helper = new HRBaseServiceHelper("haos_structure")).queryOne("org, creator, id, enable,rootorg", new QFilter("id", "=", keyValues[0]))).getDynamicObject("rootorg")))) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u5f53\u524d\u77e9\u9635\u67b6\u6784\u65e0\u6839\u7ec4\u7ec7\uff0c\u8bf7\u524d\u5f80\u201c\u7ec4\u7ec7\u7ba1\u7406>\u884c\u653f\u7ec4\u7ec7\u7ef4\u62a4>\u77e9\u9635\u7ec4\u7ec7\u8bbe\u7f6e\u201d\u7ef4\u62a4\u5176\u6839\u7ec4\u7ec7\u4fe1\u606f\u540e\u518d\u8fdb\u884c\u67b6\u6784\u7ef4\u62a4\u3002", (String)"StructureListPlugin_1", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                return;
            }
            this.showStructListPage(((Donothing)operate).getListFocusRow().getPrimaryKeyValue());
        }
    }
}
