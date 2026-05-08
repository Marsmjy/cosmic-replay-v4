/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection
 *  kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.BasedataEntityType
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.events.BeforePackageDataEvent
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.list.column.BaseDataColumnDesc
 *  kd.bos.entity.list.column.TextColumnDesc
 *  kd.bos.entity.property.BillStatusProp
 *  kd.bos.entity.property.ComboProp
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListColumnsArgs
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BillListHyperLinkClickEvent
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.inte.api.EnabledLang
 *  kd.bos.inte.api.IInteService
 *  kd.bos.list.IListColumn
 *  kd.bos.list.ListShowParameter
 *  kd.bos.login.utils.DateUtils
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.orm.util.StringUtils
 *  kd.bos.service.ServiceFactory
 *  kd.bos.servicehelper.QueryServiceHelper
 *  kd.hr.haos.business.domain.adminorg.util.AdminOrgHisDynKey
 *  kd.hr.haos.business.domain.org.service.fullname.OrgFullNameServiceWrapper
 *  kd.hr.haos.business.util.OrgPermHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.homs.common.model.ChangeDetailVO
 *  kd.hr.homs.formplugin.web.changedetail.AdminOrgChgRecordListPlugin$SearchVO
 */
package kd.hr.homs.formplugin.web.changedetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.BasedataEntityType;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.events.BeforePackageDataEvent;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.list.column.BaseDataColumnDesc;
import kd.bos.entity.list.column.TextColumnDesc;
import kd.bos.entity.property.BillStatusProp;
import kd.bos.entity.property.ComboProp;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BillListHyperLinkClickEvent;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.inte.api.EnabledLang;
import kd.bos.inte.api.IInteService;
import kd.bos.list.IListColumn;
import kd.bos.list.ListShowParameter;
import kd.bos.login.utils.DateUtils;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.orm.util.StringUtils;
import kd.bos.service.ServiceFactory;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.hr.haos.business.domain.adminorg.util.AdminOrgHisDynKey;
import kd.hr.haos.business.domain.org.service.fullname.OrgFullNameServiceWrapper;
import kd.hr.haos.business.util.OrgPermHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.homs.common.model.ChangeDetailVO;
import kd.hr.homs.formplugin.web.changedetail.AdminOrgChgRecordListPlugin;

public final class AdminOrgChgRecordListPlugin
extends HRDataBaseList {
    private static final String CHGBILL = "orgchgentry.chgbill";
    private static final String DETAIL_ID = "orgchgentry.subentryentity.id";
    private static final String PARENT_ORG_ID = "adminorg.parentorg.id";
    private static final String ENTRY_ID = "orgchgentry.id";
    private static final String DELIMITER = "\u3001";
    private static final char LEFT_BRACKET = '\uff08';
    private static final char RIGHT_BRACKET = '\uff09';
    private static final String BEFORE_VALUE = "beforevalue";
    private static final String AFTER_VALUE = "aftervalue";
    private static final String CHANGE_FIELD = "changefield";
    private static final String BEFORE_NAME_NUMBER = "beforenamenumber";
    private static final String AFTER_NAME_NUMBER = "afternamenumber";
    private static final String MERGE_SPLIT_FLAG = "mergesplitflag";
    private static final String MUL_BASE_DATA_FIELD = "mulbasedatafield";
    private static final String MERGED_ORG = "mergedorg";
    private static final String F_BASE_DATA_ID = "fbasedataid";
    private static final String SPLITED_ORG = "splitedorg";
    private Map<Long, ChangeDetailVO> changeMap = new ConcurrentHashMap<Long, ChangeDetailVO>(16);
    private Map<Long, String> parentMap = new ConcurrentHashMap<Long, String>(16);
    private Map<Long, String> mergeAndSplitViewMap = new ConcurrentHashMap<Long, String>(16);
    private List<EnabledLang> enabledLangList;
    private final Map<String, String> SEARCH_MAP = new HashMap<String, String>(2);
    private DataEntityPropertyCollection properties = null;

    public AdminOrgChgRecordListPlugin() {
        this.SEARCH_MAP.put("searchdate", "orgchgentry.chgeffecttime");
        this.SEARCH_MAP.put("searchchangescene.id", "orgchgentry.changescene.id");
        this.SEARCH_MAP.put("searchbillno", "orgchgentry.chgbill.billno");
        this.SEARCH_MAP.put("searchdispatchno", "orgchgentry.chgbill.dispatchnumber");
        this.SEARCH_MAP.put("searchdispatchname", "orgchgentry.chgbill.dispatchname");
        this.SEARCH_MAP.put("seatchsimple", "adminorg.simplename");
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
        DynamicObject dynamicObject = serviceHelper.generateEmptyDynamicObject();
        this.properties = dynamicObject.getDataEntityType().getProperties();
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        List qFilterList = setFilterEvent.getQFilters();
        this.replaceProperty(qFilterList);
        qFilterList.add(OrgPermHelper.getHrPermFilter((String)((ListView)this.getView()).getBillFormId(), (String)"adminorg.org"));
        if (this.getView().getFormShowParameter().getCustomParam("needshowdetail") != null && ((Boolean)this.getView().getFormShowParameter().getCustomParam("needshowdetail")).booleanValue()) {
            qFilterList.remove(0);
            qFilterList.add(new QFilter("orgchgentry.chgbill.id", "=", this.getView().getFormShowParameter().getCustomParam("billidfilter")));
        }
        if (this.getView().getFormShowParameter().getCustomParam("needshowsingle") != null && ((Boolean)this.getView().getFormShowParameter().getCustomParam("needshowsingle")).booleanValue()) {
            qFilterList.remove(0);
            qFilterList.add(new QFilter("orgchgentry.chgbill.id", "=", this.getView().getFormShowParameter().getCustomParam("billidfilter")));
            qFilterList.add(new QFilter("orgchgentry.orgentry", "=", this.getView().getFormShowParameter().getCustomParam("entryid")));
        }
        qFilterList.add(new QFilter("adminorg.otclassify.id", "=", (Object)1010L));
        setFilterEvent.setOrderBy("adminorg.number,orgchgentry.chgeffecttime desc,orgchgentry.operationtime desc,orgchgentry.subentryentity.seq");
    }

    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
        if (e.getFormShowParameter().getCustomParam("needshowdetail") != null && ((Boolean)e.getFormShowParameter().getCustomParam("needshowdetail")).booleanValue() || e.getFormShowParameter().getCustomParam("needshowsingle") != null && ((Boolean)e.getFormShowParameter().getCustomParam("needshowsingle")).booleanValue()) {
            ((ListShowParameter)e.getFormShowParameter()).setSelectedEntity("subentryentity");
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs eventArgs) {
        super.afterDoOperation(eventArgs);
        if ("refresh".equals(eventArgs.getOperateKey())) {
            // empty if block
        }
    }

    private void replaceProperty(List<QFilter> qFilterList) {
        Consumer<QFilter> transferQueryField = qFilter -> {
            String value = this.SEARCH_MAP.get(qFilter.getProperty());
            if (value != null) {
                qFilter.__setProperty(value);
            }
        };
        qFilterList.forEach(qFilter -> {
            transferQueryField.accept((QFilter)qFilter);
            qFilter.getNests(true).forEach(nest -> transferQueryField.accept(nest.getFilter()));
        });
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        args.setCancel(true);
        ListSelectedRow row = ((BillListHyperLinkClickEvent)args.getHyperLinkClickEvent()).getCurrentRow();
        Object entryPkId = row.getEntryPrimaryKeyValue();
        HRBaseServiceHelper helper = new HRBaseServiceHelper("homs_orgchgrecord");
        QFilter idFilter = new QFilter(ENTRY_ID, "=", entryPkId);
        DynamicObject dy = helper.queryOriginalOne(CHGBILL, idFilter);
        if (dy == null) {
            return;
        }
        BillShowParameter showParameter = new BillShowParameter();
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setPkId((Object)dy.getLong(CHGBILL));
        showParameter.setBillStatus(BillOperationStatus.VIEW);
        showParameter.setStatus(OperationStatus.VIEW);
        showParameter.setFormId("homs_orgbatchchgbill");
        showParameter.setPageId(String.valueOf(entryPkId) + '_' + this.getView().getPageId());
        this.getView().showForm((FormShowParameter)showParameter);
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        List listColumns = args.getListColumns();
        for (IListColumn listColumn : listColumns) {
            if (!"adminorg.number".equals(listColumn.getListFieldKey()) && !"adminorg.name".equals(listColumn.getListFieldKey())) continue;
            listColumn.setFixed(true);
        }
    }

    public void beforePackageData(BeforePackageDataEvent event) {
        DynamicObjectCollection pageData = event.getPageData();
        if (CollectionUtils.isEmpty((Collection)pageData)) {
            return;
        }
        Set parentOrgList = pageData.stream().map(dy -> dy.getLong(PARENT_ORG_ID)).collect(Collectors.toSet());
        OrgFullNameServiceWrapper fullNameServiceWrapper = new OrgFullNameServiceWrapper();
        Map parentOrgLongName = fullNameServiceWrapper.getBatchOrgFullName(parentOrgList, HRDateTimeUtils.truncateDate((Date)new Date()));
        this.parentMap = new HashMap<Long, String>(pageData.size());
        this.mergeAndSplitViewMap = new HashMap<Long, String>(pageData.size());
        for (DynamicObject dy2 : pageData) {
            this.parentMap.put(dy2.getLong("id"), (String)parentOrgLongName.get(dy2.getLong(PARENT_ORG_ID)));
        }
        if (((DynamicObject)pageData.get(0)).getDynamicObjectType().getProperty(DETAIL_ID) == null) {
            return;
        }
        List<Long> entryIds = pageData.stream().map(pd -> pd.getLong(ENTRY_ID)).collect(Collectors.toList());
        List<Long> detailIdList = pageData.stream().map(pageDate -> pageDate.getLong(DETAIL_ID)).collect(Collectors.toList());
        this.buildSplitMerge(entryIds);
        this.buildData(detailIdList);
    }

    private void buildSplitMerge(List<Long> entryIds) {
        QFilter qFilter = new QFilter("id", "in", entryIds);
        qFilter.and(new QFilter(MERGE_SPLIT_FLAG, "in", Arrays.asList("1", "2")));
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("homs_orgchgentry");
        DynamicObject[] detailCol = serviceHelper.query("mergedorg,splitedorg,mergesplitflag,mulbasedatafield,id", new QFilter[]{qFilter});
        HashSet<Long> needQueryAdminOrgIds = new HashSet<Long>(16);
        HashSet<Long> needQueryAdminBoIds = new HashSet<Long>(16);
        for (DynamicObject dy : detailCol) {
            DynamicObject orgVersionDy;
            DynamicObjectCollection mulbasedatafield;
            if ("1".equals(dy.getString(MERGE_SPLIT_FLAG))) {
                needQueryAdminOrgIds.add(dy.getLong(MERGED_ORG));
                mulbasedatafield = dy.getDynamicObjectCollection(MUL_BASE_DATA_FIELD);
                for (DynamicObject dynamicObject : mulbasedatafield) {
                    orgVersionDy = dynamicObject.getDynamicObject(F_BASE_DATA_ID);
                    if (orgVersionDy == null) continue;
                    needQueryAdminBoIds.add(orgVersionDy.getLong("boid"));
                }
                continue;
            }
            if (!"2".equals(dy.getString(MERGE_SPLIT_FLAG))) continue;
            mulbasedatafield = dy.getDynamicObjectCollection(MUL_BASE_DATA_FIELD);
            for (DynamicObject dynamicObject : mulbasedatafield) {
                orgVersionDy = dynamicObject.getDynamicObject(F_BASE_DATA_ID);
                if (orgVersionDy == null) continue;
                needQueryAdminBoIds.add(orgVersionDy.getLong("boid"));
            }
            needQueryAdminOrgIds.add(dy.getLong(SPLITED_ORG));
        }
        Map<Long, DynamicObject> idToDyMap = new HashMap<Long, DynamicObject>(16);
        HashMap<Long, Long> vidToBoId = new HashMap<Long, Long>(16);
        if (needQueryAdminOrgIds.size() != 0) {
            DynamicObjectCollection query = QueryServiceHelper.query((String)"haos_adminorgdetail", (String)"id,boid", (QFilter[])new QFilter[]{new QFilter("id", "in", needQueryAdminOrgIds)});
            query.forEach(dyn -> {
                needQueryAdminBoIds.add(dyn.getLong("boid"));
                vidToBoId.putIfAbsent(dyn.getLong("id"), dyn.getLong("boid"));
            });
        }
        if (needQueryAdminBoIds.size() != 0) {
            DynamicObjectCollection query = QueryServiceHelper.query((String)"haos_adminorgdetail", (String)"id,number,name", (QFilter[])new QFilter[]{new QFilter("id", "in", needQueryAdminBoIds)});
            idToDyMap = query.stream().collect(Collectors.toMap(dyn -> dyn.getLong("id"), dyn -> dyn));
        }
        StringBuilder sb = new StringBuilder();
        for (DynamicObject dy : detailCol) {
            ChangeDetailVO changeDetailVO = this.getChangeDetailVO(idToDyMap, vidToBoId, dy, "1".equals(dy.getString(MERGE_SPLIT_FLAG)));
            this.mergeAndSplitViewMap.put(dy.getLong("id"), sb.append(changeDetailVO.getBeforeValue()).append("\n").append(changeDetailVO.getAfterValue()).toString());
            sb.setLength(0);
        }
    }

    public void packageData(PackageDataEvent event) {
        if (event.getSource() instanceof BaseDataColumnDesc) {
            String key = ((BaseDataColumnDesc)event.getSource()).getKey();
            if ("adminorg.parentorg.name".equals(key)) {
                // empty if block
            }
            return;
        }
        if (event.getSource() instanceof TextColumnDesc) {
            String key = ((TextColumnDesc)event.getSource()).getKey();
            if ("mergesplitview".equals(key)) {
                event.setFormatValue((Object)this.mergeAndSplitViewMap.get(event.getRowData().getLong(ENTRY_ID)));
            }
            if ("parentlongname".equals(key)) {
                event.setFormatValue((Object)this.parentMap.get(event.getRowData().getLong("id")));
            }
        }
        if (event.getSource() instanceof TextColumnDesc) {
            this.formatTextValue(event);
        }
    }

    private void formatTextValue(PackageDataEvent event) {
        if (CollectionUtils.isEmpty(this.changeMap)) {
            return;
        }
        DynamicObject rowData = event.getRowData();
        if (rowData.getDynamicObjectType().getProperty(DETAIL_ID) == null) {
            return;
        }
        String key = ((TextColumnDesc)event.getSource()).getKey();
        Long id = rowData.getLong(DETAIL_ID);
        ChangeDetailVO changeVO = this.changeMap.get(id);
        if (changeVO == null) {
            return;
        }
        switch (key) {
            case "changefield": {
                event.setFormatValue((Object)changeVO.getDisplayName());
                break;
            }
            case "beforevalue": {
                event.setFormatValue(changeVO.getBeforeValue());
                break;
            }
            case "aftervalue": {
                event.setFormatValue(changeVO.getAfterValue());
                break;
            }
            case "beforenamenumber": {
                event.setFormatValue(changeVO.getBefore());
                break;
            }
            case "afternamenumber": {
                event.setFormatValue(changeVO.getAfter());
                break;
            }
        }
    }

    private void buildData(List<Long> detailIdList) {
        QFilter qFilter = new QFilter("id", "in", detailIdList);
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("homs_subentryentity");
        DynamicObject[] detailCol = serviceHelper.query("id,chgentitynumber,chgpageelement,beforechgentity,afterchgentity,coopreltype", new QFilter[]{qFilter});
        HashMap<String, SearchVO> searchMap = new HashMap<String, SearchVO>(8);
        for (DynamicObject dy2 : detailCol) {
            SearchVO vo = searchMap.computeIfAbsent(dy2.getString("chgentitynumber"), k -> new SearchVO());
            vo.entityId.add(dy2.getLong("beforechgentity"));
            vo.entityId.add(dy2.getLong("afterchgentity"));
            vo.propertySet.add(dy2.getString("chgpageelement"));
            vo.coopRelTypeId.add(dy2.getLong("coopreltype"));
        }
        HashMap<String, Map<Long, DynamicObject>> entityMap = new HashMap<String, Map<Long, DynamicObject>>(searchMap.size());
        Map<Long, String> relTypeNameMap = new HashMap<Long, String>(0);
        Map<Long, String> structProjectNameMap = new HashMap<Long, String>(0);
        for (Map.Entry entry : searchMap.entrySet()) {
            String selectProperties;
            String entityNmae;
            DynamicObject[] dyArray;
            HRBaseServiceHelper helper;
            if ("haos_orgteamcooprel".equals(entry.getKey())) {
                helper = new HRBaseServiceHelper("haos_teamcoopreltype");
                dyArray = helper.query("name", new QFilter[]{new QFilter("id", "in", (Object)((SearchVO)entry.getValue()).coopRelTypeId)});
                relTypeNameMap = Arrays.stream(dyArray).collect(Collectors.toMap(dy -> dy.getLong("id"), dy -> dy.getString("name")));
                entityNmae = "haos_adminorgdetail";
                selectProperties = "name";
            } else if (AdminOrgHisDynKey.ADMIN_STRUCT_KEY.getDynKey().equals(entry.getKey())) {
                helper = new HRBaseServiceHelper("haos_structproject");
                dyArray = helper.query("name", new QFilter[]{new QFilter("id", "in", (Object)((SearchVO)entry.getValue()).coopRelTypeId)});
                structProjectNameMap = Arrays.stream(dyArray).collect(Collectors.toMap(dy -> dy.getLong("id"), dy -> dy.getString("name")));
                entityNmae = "haos_adminorgdetail";
                selectProperties = "name";
            } else {
                entityNmae = (String)entry.getKey();
                selectProperties = String.join((CharSequence)",", ((SearchVO)entry.getValue()).propertySet);
            }
            if (HRStringUtils.isEmpty((String)((String)entry.getKey()))) continue;
            helper = new HRBaseServiceHelper(entityNmae);
            dyArray = helper.query(selectProperties, new QFilter[]{new QFilter("id", "in", (Object)((SearchVO)entry.getValue()).entityId)});
            entityMap.put((String)entry.getKey(), Arrays.stream(dyArray).collect(Collectors.toMap(dy -> dy.getLong("id"), dy -> dy)));
        }
        this.buildChangeMap(detailCol, entityMap, relTypeNameMap, structProjectNameMap);
    }

    private void buildChangeMap(DynamicObject[] detailCol, Map<String, Map<Long, DynamicObject>> entityMap, Map<Long, String> relTypeNameMap, Map<Long, String> structProjectNameMap) {
        ArrayList<ChangeDetailVO> changeList = new ArrayList<ChangeDetailVO>(detailCol.length);
        for (DynamicObject dy2 : detailCol) {
            ChangeDetailVO changeDetailVO;
            Map<Long, DynamicObject> dyMap = entityMap.get(dy2.getString("chgentitynumber"));
            if (dyMap == null) continue;
            DynamicObject beforeDy = dyMap.get(dy2.getLong("beforechgentity"));
            DynamicObject afterDy = dyMap.get(dy2.getLong("afterchgentity"));
            if ("haos_orgteamcooprel".equals(dy2.getString("chgentitynumber"))) {
                changeDetailVO = this.buildCoolChangeVO(beforeDy, afterDy);
                changeDetailVO.setDisplayName(relTypeNameMap.get(dy2.getLong("coopreltype")));
            } else if (AdminOrgHisDynKey.ADMIN_STRUCT_KEY.getDynKey().equals(dy2.getString("chgentitynumber"))) {
                changeDetailVO = this.buildStructProjectChangeVO(beforeDy, afterDy);
                changeDetailVO.setDisplayName(structProjectNameMap.get(dy2.getLong("coopreltype")));
            } else {
                changeDetailVO = this.buildBaseChangeVO(beforeDy, afterDy, dy2.getString("chgpageelement"));
            }
            if (changeDetailVO == null) continue;
            changeDetailVO.setId(Long.valueOf(dy2.getLong("id")));
            changeList.add(changeDetailVO);
        }
        this.changeMap = changeList.stream().collect(Collectors.toMap(ChangeDetailVO::getId, dy -> dy));
    }

    public ChangeDetailVO getChangeDetailVO(Map<Long, DynamicObject> idToDyMap, Map<Long, Long> vidToBoId, DynamicObject dy, boolean isMerge) {
        ChangeDetailVO changeVo = new ChangeDetailVO();
        changeVo.setId(Long.valueOf(dy.getLong("id")));
        DynamicObjectCollection mulbasedatafield = dy.getDynamicObjectCollection(MUL_BASE_DATA_FIELD);
        StringBuilder sb = new StringBuilder();
        StringBuilder after = new StringBuilder();
        StringBuilder before = new StringBuilder();
        for (int idx = 0; idx < mulbasedatafield.size(); ++idx) {
            DynamicObject orgVersionDy = ((DynamicObject)mulbasedatafield.get(idx)).getDynamicObject(F_BASE_DATA_ID);
            if (orgVersionDy == null) continue;
            DynamicObject dynamicObject = idToDyMap.get(orgVersionDy.getLong("boid"));
            if (idx == mulbasedatafield.size() - 1) {
                sb.append(dynamicObject != null ? dynamicObject.getString("name") : "");
                continue;
            }
            sb.append(dynamicObject != null ? dynamicObject.getString("name") : "").append(";");
        }
        if (isMerge) {
            changeVo.setAfterValue((Object)after.append(ResManager.loadKDString((String)"\u5408\u5e76\u540e\u7ec4\u7ec7", (String)"AdminOrgChgRecordListPlugin_8", (String)"odc-homs-formplugin", (Object[])new Object[0])).append(":").append(idToDyMap.get(vidToBoId.get(dy.getLong(MERGED_ORG))) != null ? idToDyMap.get(vidToBoId.get(dy.getLong(MERGED_ORG))).getString("name") : ""));
            changeVo.setBeforeValue((Object)before.append(ResManager.loadKDString((String)"\u5408\u5e76\u524d\u7ec4\u7ec7", (String)"AdminOrgChgRecordListPlugin_7", (String)"odc-homs-formplugin", (Object[])new Object[0])).append(":").append((CharSequence)sb));
        } else {
            changeVo.setBeforeValue((Object)before.append(ResManager.loadKDString((String)"\u62c6\u5206\u524d\u7ec4\u7ec7", (String)"AdminOrgChgRecordListPlugin_9", (String)"odc-homs-formplugin", (Object[])new Object[0])).append(":").append(idToDyMap.get(vidToBoId.get(dy.getLong(SPLITED_ORG))) != null ? idToDyMap.get(vidToBoId.get(dy.getLong(SPLITED_ORG))).getString("name") : ""));
            changeVo.setAfterValue((Object)after.append(ResManager.loadKDString((String)"\u62c6\u5206\u540e\u7ec4\u7ec7", (String)"AdminOrgChgRecordListPlugin_10", (String)"odc-homs-formplugin", (Object[])new Object[0])).append(":").append((CharSequence)sb));
        }
        return changeVo;
    }

    private ChangeDetailVO buildCoolChangeVO(DynamicObject beforeDy, DynamicObject afterDy) {
        return this.buildChangeVO(beforeDy, afterDy, "adminorg");
    }

    private ChangeDetailVO buildStructProjectChangeVO(DynamicObject beforeDy, DynamicObject afterDy) {
        return this.buildChangeVO(beforeDy, afterDy, "adminorg");
    }

    private ChangeDetailVO buildBaseChangeVO(DynamicObject beforeDy, DynamicObject afterDy, String property) {
        DynamicObject dy;
        if (beforeDy == null && afterDy == null) {
            return null;
        }
        DynamicObject dynamicObject = dy = beforeDy != null ? beforeDy : afterDy;
        if (dy.getDynamicObjectType().getProperty(property) == null) {
            return null;
        }
        String displayName = null;
        Object beforeValue = null;
        Object afterValue = null;
        if (beforeDy != null) {
            displayName = beforeDy.getDynamicObjectType().getProperty(property).getDisplayName().getLocaleValue();
            beforeValue = beforeDy.get(property);
        }
        if (afterDy != null) {
            displayName = afterDy.getDynamicObjectType().getProperty(property).getDisplayName().getLocaleValue();
            afterValue = afterDy.get(property);
        }
        ChangeDetailVO vo = this.buildChangeVO(beforeValue, afterValue, property);
        vo.setDisplayName(displayName);
        return vo;
    }

    private List<EnabledLang> getEnabledLangList() {
        if (this.enabledLangList == null) {
            this.enabledLangList = ((IInteService)ServiceFactory.getService(IInteService.class)).getEnabledLang();
        }
        return this.enabledLangList;
    }

    private ChangeDetailVO buildChangeVO(Object before, Object after, String property) {
        if (before instanceof ILocaleString && after instanceof ILocaleString) {
            StringBuilder beforeStr = new StringBuilder();
            StringBuilder afterStr = new StringBuilder();
            List<EnabledLang> langList = this.getEnabledLangList();
            if (CollectionUtils.isEmpty(langList)) {
                return new ChangeDetailVO(before, after);
            }
            int size = langList.size();
            for (EnabledLang lang : langList) {
                String afterItem;
                String number = lang.getNumber();
                String beforeItem = (String)((ILocaleString)before).getItem(number);
                if (HRStringUtils.equals((String)beforeItem, (String)(afterItem = (String)((ILocaleString)after).getItem(number)))) continue;
                if (size > 1) {
                    if (!HRStringUtils.isEmpty((String)this.formatString(beforeItem))) {
                        beforeStr.append(this.formatString(beforeItem)).append('\uff08').append(lang.getLangCode()).append('\uff09').append(DELIMITER);
                    }
                    if (HRStringUtils.isEmpty((String)this.formatString(afterItem))) continue;
                    afterStr.append(this.formatString(afterItem)).append('\uff08').append(lang.getLangCode()).append('\uff09').append(DELIMITER);
                    continue;
                }
                beforeStr.append(this.formatString(beforeItem)).append(DELIMITER);
                afterStr.append(this.formatString(afterItem)).append(DELIMITER);
            }
            if (beforeStr.length() > 0 && afterStr.length() > 0) {
                return new ChangeDetailVO((Object)beforeStr.subSequence(0, beforeStr.lastIndexOf(DELIMITER)), (Object)afterStr.subSequence(0, afterStr.lastIndexOf(DELIMITER)));
            }
        }
        ChangeDetailVO vo = new ChangeDetailVO(this.formatValue(before, property), this.formatValue(after, property));
        if (before instanceof DynamicObject) {
            vo.setBefore((Object)this.appendValue((DynamicObject)before, property));
        }
        if (after instanceof DynamicObject) {
            vo.setAfter((Object)this.appendValue((DynamicObject)after, property));
        }
        if (before instanceof MulBasedataDynamicObjectCollection) {
            vo.setBeforeValue((Object)this.getMultiBaseDataString((MulBasedataDynamicObjectCollection)before).toString());
            vo.setBefore((Object)this.getMultiBaseDataStringWithNumber((MulBasedataDynamicObjectCollection)before).toString());
        }
        if (after instanceof MulBasedataDynamicObjectCollection) {
            vo.setAfterValue((Object)this.getMultiBaseDataString((MulBasedataDynamicObjectCollection)after).toString());
            vo.setAfter((Object)this.getMultiBaseDataStringWithNumber((MulBasedataDynamicObjectCollection)after).toString());
        }
        return vo;
    }

    private StringBuilder getMultiBaseDataStringWithNumber(MulBasedataDynamicObjectCollection collection) {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < collection.size(); ++index) {
            DynamicObject fbasedataid = ((DynamicObject)collection.get(index)).getDynamicObject(F_BASE_DATA_ID);
            if (index == collection.size() - 1) {
                if (fbasedataid == null) continue;
                sb.append(fbasedataid.getString("name")).append("(").append(fbasedataid.getString("number")).append(")");
                continue;
            }
            if (fbasedataid == null) continue;
            sb.append(fbasedataid.getString("name")).append("(").append(fbasedataid.getString("number")).append(")").append(",");
        }
        return sb;
    }

    private StringBuilder getMultiBaseDataString(MulBasedataDynamicObjectCollection collection) {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < collection.size(); ++index) {
            DynamicObject fbasedataid = ((DynamicObject)collection.get(index)).getDynamicObject(F_BASE_DATA_ID);
            if (index == collection.size() - 1) {
                if (fbasedataid == null) continue;
                sb.append(fbasedataid.getString("name"));
                continue;
            }
            if (fbasedataid == null) continue;
            sb.append(fbasedataid.getString("name")).append(",");
        }
        return sb;
    }

    private String appendValue(DynamicObject dy, String property) {
        int index;
        String name = "";
        String number = "";
        DynamicObjectType type = dy.getDynamicObjectType();
        if (type instanceof BasedataEntityType) {
            String numberProp = ((BasedataEntityType)type).getNumberProperty();
            String namePropTmp = ((BasedataEntityType)type).getNameProperty();
            String nameProp = StringUtils.isEmpty((Object)namePropTmp) ? "name" : namePropTmp;
            name = dy.getString(nameProp);
            number = dy.getString(numberProp);
        } else {
            name = dy.getString("name");
            number = dy.getString("number");
        }
        if ("adminorg".equals(property) && (index = number.indexOf("_")) != -1) {
            number = number.substring(index + 1);
        }
        return name + '\uff08' + number + '\uff09';
    }

    private String formatString(String value) {
        return value == null ? "" : value;
    }

    private Object formatValue(Object value, String property) {
        StringBuilder valueStr = new StringBuilder();
        if (value == null) {
            return "";
        }
        if ("enable".equals(property)) {
            BillStatusProp billStatusProp = (BillStatusProp)this.properties.get((Object)property);
            return billStatusProp.getItemByName((String)value);
        }
        if (value instanceof ILocaleString) {
            List<EnabledLang> langList = this.getEnabledLangList();
            if (CollectionUtils.isEmpty(langList)) {
                return value;
            }
            int size = langList.size();
            for (EnabledLang lang : langList) {
                String number = lang.getNumber();
                String valueItem = (String)((ILocaleString)value).getItem(number);
                if (HRStringUtils.isEmpty((String)valueItem)) continue;
                if (size > 1) {
                    if (HRStringUtils.isEmpty((String)this.formatString(valueItem))) continue;
                    valueStr.append(valueItem).append('\uff08').append(lang.getLangCode()).append('\uff09').append(DELIMITER);
                    continue;
                }
                valueStr.append(valueItem).append(DELIMITER);
            }
            if (valueStr.length() > 0) {
                return valueStr.subSequence(0, valueStr.lastIndexOf(DELIMITER));
            }
            return valueStr;
        }
        if (value instanceof Boolean) {
            return (Boolean)value != false ? ResManager.loadKDString((String)"\u662f", (String)"AdminOrgChgRecordListPlugin_1", (String)"odc-homs-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u5426", (String)"AdminOrgChgRecordListPlugin_2", (String)"odc-homs-formplugin", (Object[])new Object[0]);
        }
        if (value instanceof Date) {
            return DateUtils.formatDate((Date)((Date)value), (Object[])new Object[0]);
        }
        if (value instanceof DynamicObject) {
            String namePropTmp;
            DynamicObject cellValue = (DynamicObject)value;
            DynamicObjectType type = cellValue.getDynamicObjectType();
            String nameProp = "name";
            if (type instanceof BasedataEntityType && !StringUtils.isEmpty((Object)(namePropTmp = ((BasedataEntityType)type).getNameProperty()))) {
                nameProp = namePropTmp;
            }
            return cellValue.getString(nameProp);
        }
        if (this.properties.get((Object)property) instanceof ComboProp && value instanceof String) {
            ComboProp comboProp = (ComboProp)this.properties.get((Object)property);
            return comboProp.getItemByName((String)value);
        }
        return value;
    }
}
