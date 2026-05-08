/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.cache.ThreadCache
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.devportal.common.util.AppUtils
 *  kd.bos.entity.devportal.AppMenuElement
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.devportal.AppMetadata
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.bos.servicehelper.devportal.AppMetaServiceHelper
 *  kd.bos.servicehelper.operation.DeleteServiceHelper
 *  kd.bos.util.CollectionUtils
 *  kd.bos.util.StringUtils
 *  kd.hr.haos.business.domain.otherstruct.helper.StructClassHelper
 *  kd.hr.haos.business.domain.otherstruct.helper.StructTypeHelper
 *  kd.hr.haos.business.domain.otherstruct.service.OtherStructMetaAndMenuDataService
 *  kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService
 *  kd.hr.haos.common.constants.otherstruct.BizRuleLibraryEnum
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.otherstruct.structtype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.cache.ThreadCache;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.devportal.common.util.AppUtils;
import kd.bos.entity.devportal.AppMenuElement;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.devportal.AppMetadata;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.bos.servicehelper.devportal.AppMetaServiceHelper;
import kd.bos.servicehelper.operation.DeleteServiceHelper;
import kd.bos.util.CollectionUtils;
import kd.bos.util.StringUtils;
import kd.hr.haos.business.domain.otherstruct.helper.StructClassHelper;
import kd.hr.haos.business.domain.otherstruct.helper.StructTypeHelper;
import kd.hr.haos.business.domain.otherstruct.service.OtherStructMetaAndMenuDataService;
import kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService;
import kd.hr.haos.common.constants.otherstruct.BizRuleLibraryEnum;
import kd.hr.haos.opplugin.web.otherstruct.structtype.validate.StructTypeDeleteValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructTypeDeleteDonothingOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(StructTypeDeleteDonothingOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        evt.getFieldKeys().add("status");
        evt.getFieldKeys().add("metanumsuffix");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new StructTypeDeleteValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        DynamicObject[] dataEntities = e.getDataEntities();
        OtherStructTypeService otherStructTypeService = new OtherStructTypeService();
        otherStructTypeService.deleteStructTypeData(dataEntities);
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
        DynamicObject[] dataEntities = e.getDataEntities();
        Set otClassifyId = Arrays.stream(dataEntities).map(dynamicObject -> dynamicObject.getString("id")).collect(Collectors.toSet());
        OtherStructMetaAndMenuDataService otherStructMetaAndMenuDataService = new OtherStructMetaAndMenuDataService();
        List metaNum = StructTypeHelper.splicingMetaDataEncoding((DynamicObject[])dataEntities);
        try (TXHandle required = TX.requiresNew();){
            try {
                DynamicObject[] metaDatumById = otherStructMetaAndMenuDataService.getMetaDataByNum(metaNum, "id,number,inheritpath,basedatafield.id");
                Set baseDataFieldId = Arrays.stream(metaDatumById).map(data -> data.getString("basedatafield.id")).collect(Collectors.toSet());
                DynamicObjectCollection metaMobileBill = otherStructMetaAndMenuDataService.getMetaMobileBillByNum(baseDataFieldId, "id,number,basedatafield.id,parentid.id,parentid.number");
                List metaMobileNumber = metaMobileBill.stream().filter(data -> StringUtils.isEmpty((String)data.getString("parentid.id"))).map(data -> data.getString("number")).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty((Collection)metaMobileBill)) {
                    StructClassHelper.filterMetaData((DynamicObjectCollection)metaMobileBill, metaMobileNumber);
                }
                ArrayList<String> inheritPathList = new ArrayList<String>(metaNum.size());
                for (DynamicObject dynamicObject2 : metaDatumById) {
                    inheritPathList.add(dynamicObject2.getString("inheritpath") + "," + dynamicObject2.getString("id"));
                }
                if (!inheritPathList.isEmpty()) {
                    DynamicObjectCollection metaDatumByPath = otherStructMetaAndMenuDataService.getMetaDataByPath(inheritPathList, "id,number,inheritpath,parentid.number");
                    StructClassHelper.filterMetaData((DynamicObjectCollection)metaDatumByPath, (List)metaNum);
                }
                LOG.info("\u9700\u8981\u5220\u9664\u7684\u79fb\u52a8\u7aef\u5143\u6570\u636e\uff1a{}", metaMobileNumber);
                LOG.info("\u9700\u8981\u5220\u9664\u7684\u5143\u6570\u636e\uff1a{}", (Object)metaNum);
                for (String metaId : metaMobileNumber) {
                    StructClassHelper.deleteMetaData((String)metaId);
                }
                for (String metaId : metaNum) {
                    StructClassHelper.deleteMetaData((String)metaId);
                }
                if (!metaNum.isEmpty()) {
                    DeleteServiceHelper.delete((IDataEntityType)MetadataServiceHelper.getDataEntityType((String)"bos_entityobject"), (Object[])metaNum.toArray());
                }
            }
            catch (Throwable ex) {
                required.markRollback();
                String message = Arrays.stream(dataEntities).map(data -> String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u5220\u9664\u5931\u8d25\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u5904\u7406\u3002", (String)"StructTypeDeleteOp_2", (String)"hrmp-haos-business", (Object[])new Object[0]), data.getString("name"))).collect(Collectors.joining("\r\n"));
                throw new KDBizException(message);
            }
        }
        String extendAppId = otherStructMetaAndMenuDataService.getExtAppId();
        AppMetadata metadata = AppMetaServiceHelper.loadAppMetadataFromCacheById((String)extendAppId, (boolean)false);
        List appMenus = metadata.getAppMenus();
        Set menuId = appMenus.stream().filter(data -> otClassifyId.contains(data.getId())).map(AppMenuElement::getId).collect(Collectors.toSet());
        if (!menuId.isEmpty()) {
            otherStructMetaAndMenuDataService.queryChildMenus(menuId, appMenus);
            appMenus.removeIf(val -> menuId.contains(val.getId()));
            ThreadCache.put((Object)"AppMetaDao.deleteApp.ignoreExtApp", (Object)"true");
            AppMetaServiceHelper.save((AppMetadata)metadata);
            ThreadCache.remove((Object)"AppMetaDao.deleteApp.ignoreExtApp");
            AppUtils.addLog((String)"bos_devportal_menu", (String)ResManager.loadKDString((String)"\u5220\u9664\u83dc\u5355", (String)"StructTypeDeleteOp_0", (String)"hrmp-haos-business", (Object[])new Object[0]), (String)ResManager.loadKDString((String)"\u5220\u9664\u83dc\u5355\u540e\u4fdd\u5b58", (String)"\u5220\u9664\u83dc\u5355\u6570\u636e\u540e\u4fdd\u5b58", (String)"hrmp-haos-business", (Object[])new Object[0]));
        }
        BizRuleLibraryEnum[] values = BizRuleLibraryEnum.values();
        ArrayList bizRuleLibraryNumbers = new ArrayList(values.length);
        ArrayList bizRuleNumbers = new ArrayList(values.length);
        HashSet opBizRuleSet = new HashSet(values.length);
        Map<String, List<BizRuleLibraryEnum>> bizRuleEnums = Arrays.stream(values).collect(Collectors.groupingBy(BizRuleLibraryEnum::getEntity));
        for (DynamicObject dataEntity : dataEntities) {
            String suffix = dataEntity.getString("metanumsuffix").toLowerCase(Locale.ROOT);
            StructClassHelper.getBizRuleNumber(bizRuleEnums, (String)suffix, bizRuleLibraryNumbers, bizRuleNumbers, opBizRuleSet, (boolean)true);
        }
        OtherStructTypeService.deleteGbsBizRuleData(bizRuleNumbers, (String)"id");
        OtherStructTypeService.deleteGbsBizRuleLibraryData(bizRuleLibraryNumbers, (String)"id");
        opBizRuleSet.remove("haos_structtype");
        OtherStructTypeService.deleteOpBizRuleSetData(opBizRuleSet, (String)"id");
    }
}

