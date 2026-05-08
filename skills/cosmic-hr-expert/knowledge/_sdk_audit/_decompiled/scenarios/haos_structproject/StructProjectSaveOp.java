/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgStructRepository
 *  kd.hr.haos.business.domain.adminorg.util.AdminOrgHisDynKey
 *  kd.hr.haos.business.domain.org.abs.IBatchOrgOpService
 *  kd.hr.haos.business.domain.org.helper.HisModelHelper
 *  kd.hr.haos.business.domain.org.helper.TimeLineHelper
 *  kd.hr.haos.business.domain.org.service.BatchAdminOrgNewOpService
 *  kd.hr.haos.business.domain.org.service.BatchOrgDeleteOpService
 *  kd.hr.haos.business.domain.org.service.BatchOrgExecuteOpService
 *  kd.hr.haos.business.domain.structproject.models.OtherStructEntity
 *  kd.hr.haos.business.domain.structproject.models.OtherStructVO
 *  kd.hr.haos.business.domain.structproject.service.impl.OtherStructService
 *  kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository
 *  kd.hr.haos.common.constants.structproject.StructProjectConstants
 *  kd.hr.haos.opplugin.web.validate.StructProjectValidator
 *  kd.hr.hbp.business.application.impl.history.HisModelAPIService
 *  kd.hr.hbp.business.service.history.util.HisModelCopyUtil
 *  kd.hr.hbp.common.model.history.param.HisCreateVersionParam
 *  kd.hr.hbp.common.model.history.param.HisReviseParam
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.otherframework;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgStructRepository;
import kd.hr.haos.business.domain.adminorg.util.AdminOrgHisDynKey;
import kd.hr.haos.business.domain.org.abs.IBatchOrgOpService;
import kd.hr.haos.business.domain.org.helper.HisModelHelper;
import kd.hr.haos.business.domain.org.helper.TimeLineHelper;
import kd.hr.haos.business.domain.org.service.BatchAdminOrgNewOpService;
import kd.hr.haos.business.domain.org.service.BatchOrgDeleteOpService;
import kd.hr.haos.business.domain.org.service.BatchOrgExecuteOpService;
import kd.hr.haos.business.domain.structproject.models.OtherStructEntity;
import kd.hr.haos.business.domain.structproject.models.OtherStructVO;
import kd.hr.haos.business.domain.structproject.service.impl.OtherStructService;
import kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository;
import kd.hr.haos.common.constants.structproject.StructProjectConstants;
import kd.hr.haos.opplugin.web.validate.StructProjectValidator;
import kd.hr.hbp.business.application.impl.history.HisModelAPIService;
import kd.hr.hbp.business.service.history.util.HisModelCopyUtil;
import kd.hr.hbp.common.model.history.param.HisCreateVersionParam;
import kd.hr.hbp.common.model.history.param.HisReviseParam;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructProjectSaveOp
extends HRDataBaseOp
implements StructProjectConstants {
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new StructProjectValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        DynamicObject[] dataEntities = e.getDataEntities();
        DynamicObject dataEntity = dataEntities[0];
        this.saveStructProjectAndRootOrg(dataEntity);
    }

    private void saveStructProjectAndRootOrg(DynamicObject structProject) {
        long structProjectId = structProject.getLong("id");
        String newRootType = structProject.getString("roottype");
        DynamicObject[] dbStructProjectDys = StructProjectRepository.getInstance().loadByIds((Collection)Lists.newArrayList((Object[])new Long[]{structProjectId}));
        DynamicObject dbStructProjectDy = dbStructProjectDys.length > 0 ? dbStructProjectDys[0] : null;
        DynamicObject rootOrg = structProject.getDynamicObject("rootorg");
        Long rootOrgId = structProject.getLong("rootorg.id");
        Date effDate = structProject.getDate("effdt");
        if ("2".equals(newRootType)) {
            if (rootOrgId.equals(0L)) {
                rootOrgId = ORM.create().genLongId("haos_adminorgdetail");
                rootOrg = OrgRepository.getInstance().genEmptyDy();
                rootOrg.set("id", (Object)rootOrgId);
                rootOrg.set("number", structProject.get("rootnumber"));
                rootOrg.set("name", structProject.get("rootname"));
                rootOrg.set("description", structProject.get("rootdescription"));
                rootOrg.set("isvirtualorg", (Object)"1");
                rootOrg.set("bsed", (Object)effDate);
                rootOrg.set("establishmentdate", (Object)effDate);
                rootOrg.set("otclassify", (Object)1010L);
                rootOrg.set("index", (Object)1);
                structProject.set("rootorg", (Object)rootOrg);
            } else {
                rootOrg = OrgRepository.getInstance().loadCurrentVersionOrgDy((Object)rootOrgId);
                rootOrg.set("number", structProject.get("rootnumber"));
                rootOrg.set("name", structProject.get("rootname"));
                rootOrg.set("description", structProject.get("rootdescription"));
                rootOrg.set("bsed", (Object)effDate);
                rootOrg.set("establishmentdate", (Object)effDate);
            }
        } else {
            rootOrg = OrgRepository.getInstance().loadCurrentVersionOrgDy((Object)rootOrg.getLong("id"));
            rootOrg.set("index", (Object)1);
            rootOrg.set("bsed", (Object)effDate);
        }
        rootOrg.set("org", structProject.get("org"));
        rootOrg.set("parentorg", null);
        OperateOption operateOption = OperateOption.create();
        operateOption.setVariableValue("OP_ADD_ORG_TEMP_TAG", "true");
        if (dbStructProjectDy == null) {
            BatchOrgExecuteOpService executeOpService = new BatchOrgExecuteOpService((IBatchOrgOpService)new BatchAdminOrgNewOpService(new DynamicObject[]{rootOrg}, true, operateOption, Long.valueOf(structProjectId)));
            executeOpService.execute();
        } else {
            boolean virtualAndNotChange;
            String dbRootType = dbStructProjectDy.getString("roottype");
            Date dbEffDate = dbStructProjectDy.getDate("effdt");
            DynamicObject dbRootOrg = dbStructProjectDy.getDynamicObject("rootorg");
            long oldRootOrgId = dbRootOrg.getLong("id");
            boolean bl = virtualAndNotChange = "2".equals(dbRootType) && dbRootType.equals(newRootType);
            if (dbEffDate.compareTo(effDate) == 0) {
                if (virtualAndNotChange) {
                    HisModelAPIService hisModelAPIService = new HisModelAPIService();
                    rootOrg = new HisModelCopyUtil().copyTempVersionData(rootOrg);
                    HisCreateVersionParam hisCreateVersionParam = HisModelHelper.buildOneHisParam((String)AdminOrgHisDynKey.ADMIN_ORG_KEY.getDynKey(), (List)Lists.newArrayList((Object[])new DynamicObject[]{rootOrg}));
                    hisModelAPIService.createDataVersions(hisCreateVersionParam);
                } else if (!rootOrgId.equals(oldRootOrgId)) {
                    dbRootOrg = OrgRepository.getInstance().loadCurrentVersionOrgDy((Object)oldRootOrgId);
                    BatchOrgExecuteOpService executeOpService = new BatchOrgExecuteOpService((IBatchOrgOpService)new BatchOrgDeleteOpService(new DynamicObject[]{dbRootOrg}, true, operateOption, Long.valueOf(structProjectId), false));
                    executeOpService.execute();
                    executeOpService = new BatchOrgExecuteOpService((IBatchOrgOpService)new BatchAdminOrgNewOpService(new DynamicObject[]{rootOrg}, true, operateOption, Long.valueOf(structProjectId)));
                    executeOpService.execute();
                }
            } else {
                DynamicObject[] structDys = OrgStructRepository.getInstance().queryOriginalCurrentStructDys(Long.valueOf(structProjectId), null, "sortcode");
                ArrayList otherStructVOs = Lists.newArrayListWithExpectedSize((int)structDys.length);
                for (DynamicObject structDy : structDys) {
                    long orgId = structDy.getLong("adminorg.id");
                    OtherStructVO otherStructVO = new OtherStructVO();
                    long parentOrgId = structDy.getLong("parentorg.id");
                    if (oldRootOrgId == parentOrgId) {
                        parentOrgId = rootOrgId;
                    }
                    otherStructVO.setOrgId(Long.valueOf(orgId));
                    otherStructVO.setParentId(Long.valueOf(parentOrgId));
                    otherStructVOs.add(otherStructVO);
                }
                OtherStructEntity otherStructEntity = new OtherStructEntity((List)otherStructVOs);
                if (virtualAndNotChange) {
                    DynamicObject[] dbStructDys;
                    HisModelAPIService hisModelAPIService = new HisModelAPIService();
                    HisReviseParam hisReviseParam = HisModelHelper.buildOneReviseParam((String)AdminOrgHisDynKey.ADMIN_ORG_KEY.getDynKey(), (List)Lists.newArrayList((Object[])new DynamicObject[]{rootOrg}));
                    hisModelAPIService.reviseVersions(hisReviseParam);
                    otherStructEntity.setDeleteRoot(false);
                    for (DynamicObject dbStructDy : dbStructDys = OrgStructRepository.getInstance().loadCurrentStructDys(Long.valueOf(structProjectId), new QFilter("adminorg", "=", (Object)rootOrgId))) {
                        dbStructDy.set("startdate", (Object)effDate);
                    }
                    TimeLineHelper.saveTimelineDys((String)AdminOrgHisDynKey.ADMIN_STRUCT_KEY.getDynKey(), (DynamicObject[])dbStructDys);
                } else {
                    otherStructEntity.setDeleteRoot(true);
                }
                otherStructEntity.setStructProjectId(Long.valueOf(structProjectId));
                otherStructEntity.setRootOrgId(rootOrgId);
                otherStructEntity.setEnable(structProject.getString("enable"));
                otherStructEntity.setEffectDate(effDate);
                OtherStructService otherStructService = new OtherStructService(otherStructEntity);
                otherStructService.saveOtherStruct();
            }
        }
    }
}
