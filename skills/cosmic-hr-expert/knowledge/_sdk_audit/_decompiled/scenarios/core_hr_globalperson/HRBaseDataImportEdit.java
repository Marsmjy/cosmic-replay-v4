/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.BasedataItem
 *  kd.bos.entity.datamodel.events.BeforeImportDataEventArgs
 *  kd.bos.entity.datamodel.events.ImportDataEventArgs
 *  kd.bos.entity.datamodel.events.InitImportDataEventArgs
 *  kd.bos.entity.datamodel.events.QueryImportBasedataEventArgs
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.entity.property.IBasedataField
 *  kd.bos.entity.property.MulBasedataProp
 *  kd.bos.form.operate.webapi.RowMapper
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.servicehelper.basedata.BaseDataServiceHelper
 *  kd.hr.hbp.business.service.history.HisModelCommonService
 *  kd.hr.hbp.business.service.history.core.HisModelGeneralService
 *  kd.hr.hbp.common.constants.history.EntityInheritTypeEnum
 *  kd.hr.hbp.common.model.history.HisBaseDataInfoBo
 *  kd.hr.hbp.common.model.history.HisBaseDataItem
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit
 *  kd.hr.hbp.formplugin.web.history.impt.handler.HisImportFilterF7DataHandler
 */
package kd.hr.hbp.formplugin.web.template;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.BasedataItem;
import kd.bos.entity.datamodel.events.BeforeImportDataEventArgs;
import kd.bos.entity.datamodel.events.ImportDataEventArgs;
import kd.bos.entity.datamodel.events.InitImportDataEventArgs;
import kd.bos.entity.datamodel.events.QueryImportBasedataEventArgs;
import kd.bos.entity.property.BasedataProp;
import kd.bos.entity.property.IBasedataField;
import kd.bos.entity.property.MulBasedataProp;
import kd.bos.form.operate.webapi.RowMapper;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.servicehelper.basedata.BaseDataServiceHelper;
import kd.hr.hbp.business.service.history.HisModelCommonService;
import kd.hr.hbp.business.service.history.core.HisModelGeneralService;
import kd.hr.hbp.common.constants.history.EntityInheritTypeEnum;
import kd.hr.hbp.common.model.history.HisBaseDataInfoBo;
import kd.hr.hbp.common.model.history.HisBaseDataItem;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit;
import kd.hr.hbp.formplugin.web.history.impt.handler.HisImportFilterF7DataHandler;

public final class HRBaseDataImportEdit
extends HRCoreBaseBillEdit {
    private static final Log logger = LogFactory.getLog(HRBaseDataImportEdit.class);
    private final HisImportFilterF7DataHandler handler = new HisImportFilterF7DataHandler();
    private static final int CACHE_MAX_CAPACITY = 1000;
    private static final int CACHE_INIT_CAPACITY = 16;
    private Boolean isImportEntityHisLineType = null;
    private Boolean isImportEntityBd = null;
    private final Map<HisBaseDataItem, Object> baseDataMap = Maps.newHashMapWithExpectedSize((int)16);
    private final Map<HisBaseDataItem, Object> bdBaseDataMap = Maps.newHashMapWithExpectedSize((int)16);
    private final Map<String, Boolean> inhRelationMap = Maps.newHashMapWithExpectedSize((int)16);
    private final Set<String> baseDataFieldNums = Sets.newHashSetWithExpectedSize((int)16);
    private Map<String, String> baseDataEffDateFieldMap = null;
    private Map<String, Boolean> baseDataSelDataMap = null;
    private Map<String, Boolean> isBdBaseDataMap = null;
    private Map<String, Boolean> orgPositionReturnDataMap = null;
    private final Map<Integer, Date> baseDataEffDateValueMap = Maps.newHashMapWithExpectedSize((int)16);
    private final Map<BasedataItem, List<Object>> baseDataIdMap = Maps.newHashMapWithExpectedSize((int)16);
    private Date today;
    private boolean initCompleted = false;

    public void initImportData(InitImportDataEventArgs args) {
        if (this.baseDataMap.size() > 1000) {
            this.baseDataMap.clear();
        }
        if (this.inhRelationMap.size() > 1000) {
            this.inhRelationMap.clear();
        }
        if (this.baseDataEffDateValueMap.size() > 1000) {
            this.baseDataEffDateValueMap.clear();
        }
        if (this.baseDataIdMap.size() > 1000) {
            this.baseDataIdMap.clear();
        }
        MainEntityType dataEntityType = this.getModel().getDataEntityType();
        if (this.isImportEntityHisLineType == null) {
            EntityInheritTypeEnum entityInheritTypeEnum = HisModelCommonService.getInstance().queryEntityModelType(dataEntityType.getName());
            this.isImportEntityHisLineType = entityInheritTypeEnum == EntityInheritTypeEnum.NO_INTERRUPTION_NO_OVERLAP;
            this.isImportEntityBd = BaseDataServiceHelper.checkBaseDataCtrl((String)dataEntityType.getName());
            this.today = this.formatDate(new Date());
        }
        HashMap baseDataEntityNumMap = Maps.newHashMapWithExpectedSize((int)16);
        if (this.baseDataFieldNums.isEmpty()) {
            for (Map.Entry propertyEntry : dataEntityType.getAllFields().entrySet()) {
                IDataEntityProperty property = (IDataEntityProperty)propertyEntry.getValue();
                if (!(property instanceof BasedataProp) && !(property instanceof MulBasedataProp)) continue;
                this.baseDataFieldNums.add(property.getName());
                baseDataEntityNumMap.put(property.getName(), ((IBasedataField)property).getBaseEntityId());
            }
        }
        if (!this.baseDataFieldNums.isEmpty() && this.baseDataEffDateFieldMap == null) {
            this.baseDataEffDateFieldMap = Maps.newHashMapWithExpectedSize((int)16);
            this.baseDataSelDataMap = Maps.newHashMapWithExpectedSize((int)16);
            this.isBdBaseDataMap = Maps.newHashMapWithExpectedSize((int)16);
            this.orgPositionReturnDataMap = Maps.newHashMapWithExpectedSize((int)16);
            List HisBaseDataInfoBos = HisModelGeneralService.getInstance().getHisControlAttributeBatch(this.getModel().getDataEntityType().getName(), this.baseDataFieldNums);
            for (HisBaseDataInfoBo hisBaseDataInfoBo : HisBaseDataInfoBos) {
                this.baseDataSelDataMap.put(hisBaseDataInfoBo.getFieldName(), hisBaseDataInfoBo.isSelData());
                if (this.isImportEntityBd.booleanValue()) {
                    Boolean isBaseDataCtrl = BaseDataServiceHelper.checkBaseDataCtrl((String)((String)baseDataEntityNumMap.get(hisBaseDataInfoBo.getFieldName())));
                    this.isBdBaseDataMap.put(hisBaseDataInfoBo.getFieldName(), isBaseDataCtrl);
                }
                if (this.isImportEntityHisLineType.booleanValue() && !hisBaseDataInfoBo.isPositionOrAdminOrgControl()) {
                    this.baseDataEffDateFieldMap.put(hisBaseDataInfoBo.getFieldName(), "bsed");
                } else {
                    String bsedField = hisBaseDataInfoBo.getBsedField();
                    if (HRStringUtils.isEmpty((String)bsedField)) {
                        this.baseDataEffDateFieldMap.put(hisBaseDataInfoBo.getFieldName(), "");
                    } else {
                        this.baseDataEffDateFieldMap.put(hisBaseDataInfoBo.getFieldName(), hisBaseDataInfoBo.getBsedField());
                    }
                }
                if (!hisBaseDataInfoBo.isPositionOrAdminOrgControl()) continue;
                this.orgPositionReturnDataMap.put(hisBaseDataInfoBo.getFieldName(), HRStringUtils.equals((String)hisBaseDataInfoBo.getReturnType(), (String)"boid"));
            }
        }
        if (this.baseDataEffDateFieldMap != null && !this.baseDataEffDateFieldMap.isEmpty()) {
            this.fillEffDateMap(dataEntityType, args.getSourceDataList());
        }
        this.initCompleted = true;
    }

    private void fillEffDateMap(MainEntityType dataEntityType, List<Map<String, Object>> dataMapList) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            for (Map.Entry<String, String> entry : this.baseDataEffDateFieldMap.entrySet()) {
                IDataEntityProperty property;
                String fieldKey = entry.getKey();
                String effDateField = entry.getValue();
                if (HRStringUtils.isEmpty((String)effDateField) || !((property = (IDataEntityProperty)dataEntityType.getProperties().get((Object)fieldKey)) instanceof BasedataProp)) continue;
                for (Map<String, Object> dataMap : dataMapList) {
                    Date effDate;
                    Object effDateObj = dataMap.get(effDateField);
                    if (effDateObj instanceof String) {
                        if (HRStringUtils.isEmpty((String)((String)effDateObj))) continue;
                        effDate = df.parse((String)effDateObj);
                    } else {
                        effDate = (Date)effDateObj;
                    }
                    this.baseDataEffDateValueMap.put((Integer)dataMap.get("rowNum"), effDate);
                }
            }
        }
        catch (Exception exception) {
            logger.error("import_fillEffDateMap_error", (Throwable)exception);
        }
    }

    public void beforeImportData(BeforeImportDataEventArgs e) {
        if (!this.initCompleted) {
            return;
        }
        this.handleImportBaseDataReference((ImportDataEventArgs)e);
    }

    public void afterImportData(ImportDataEventArgs e) {
        if (!this.initCompleted) {
            return;
        }
        this.handleImportBaseDataReference(e);
    }

    private void handleImportBaseDataReference(ImportDataEventArgs event) {
        if (event.getBasedataPks() == null) {
            return;
        }
        event.getSourceData().forEach((colNum, data) -> {
            if (this.baseDataFieldNums.contains(colNum)) {
                Map dataMap = (Map)data;
                if (dataMap == null) {
                    return;
                }
                String propKey = (String)dataMap.get("importprop");
                String searchVal = String.valueOf(dataMap.get(propKey));
                if (propKey == null) {
                    MainEntityType dataEntityType = this.getModel().getDataEntityType();
                    this.handler.handleOpenApiHisBaseDataId(dataEntityType, colNum, dataMap, this.inhRelationMap);
                } else {
                    for (Map.Entry<BasedataItem, Object> entry : event.getBasedataPks().entrySet()) {
                        if (!HRStringUtils.equals((String)colNum, (String)((BasedataItem)entry.getKey()).getFieldKey()) || !HRStringUtils.equals((String)propKey, (String)((BasedataItem)entry.getKey()).getSearchKey()) || !HRStringUtils.equals((String)searchVal, (String)((BasedataItem)entry.getKey()).getSearchValue())) continue;
                        this.validateHisF7Reference(event, entry);
                    }
                }
            }
        });
    }

    private void validateHisF7Reference(ImportDataEventArgs event, Map.Entry<BasedataItem, Object> baseDataEntry) {
        Integer rowIndex = (Integer)event.getSourceData().get("_dindex_");
        Date effDate = this.baseDataEffDateValueMap.get(rowIndex);
        if (effDate == null) {
            effDate = this.today;
        }
        StringBuilder error = new StringBuilder();
        if (!this.handler.validateHisF7Reference(this.getModel().getDataEntity().getDataEntityType().getName(), this.baseDataMap, this.bdBaseDataMap, baseDataEntry, this.inhRelationMap, this.isBdBaseDataMap, this.baseDataSelDataMap, effDate, error)) {
            Integer rowNum = (Integer)event.getSourceData().get("rowNum");
            event.setCancel(true);
            List errors = event.getCancelMessages().getOrDefault(rowNum, new ArrayList(10));
            errors.add(error.toString());
            event.getCancelMessages().putIfAbsent(rowNum, errors);
        }
    }

    public void queryImportBasedata(QueryImportBasedataEventArgs args) {
        if (!this.initCompleted) {
            return;
        }
        Map searchResult = args.getSearchResult();
        if (searchResult != null) {
            searchResult.forEach((baseDataItem, ids) -> {
                ArrayList copyIds = this.baseDataIdMap.get(baseDataItem);
                if (copyIds == null) {
                    copyIds = Lists.newArrayListWithCapacity((int)10);
                    copyIds.addAll(ids);
                    this.baseDataIdMap.put((BasedataItem)baseDataItem, copyIds);
                }
            });
            int rowIndex = ((RowMapper)args.getSource()).getExcelRowIndex();
            Date effDate = this.baseDataEffDateValueMap.get(rowIndex);
            if (effDate == null) {
                effDate = this.today;
            }
            this.handler.filterHisF7ReferenceData(this.baseDataMap, searchResult, this.isBdBaseDataMap, this.baseDataSelDataMap, this.inhRelationMap, this.baseDataIdMap, this.orgPositionReturnDataMap, effDate);
        }
    }

    private Date formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateFormat.format(date));
        }
        catch (ParseException ex) {
            logger.error((Throwable)ex);
            return date;
        }
    }
}
