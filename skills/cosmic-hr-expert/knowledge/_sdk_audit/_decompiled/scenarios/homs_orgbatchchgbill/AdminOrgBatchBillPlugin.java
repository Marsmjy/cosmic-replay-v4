/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.BillEntityType
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.result.IOperateInfo
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.property.BasedataProp
 *  kd.bos.ext.hr.entity.property.AdminOrgFieldProp
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.EntryGridBindDataListener
 *  kd.bos.form.control.events.RowClickEventListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.LoadCustomControlMetasArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeBasedataSetValueEvent
 *  kd.bos.form.field.events.BeforeBasedataSetValueListener
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.lang.Lang
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.bill.BillView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.servicehelper.basedata.BaseDataServiceHelper
 *  kd.bos.servicehelper.coderule.CodeRuleServiceHelper
 *  kd.bos.servicehelper.org.OrgUnitServiceHelper
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.haos.business.domain.common.service.impl.BaseDataHelper
 *  kd.hr.haos.business.util.BaseDataUtils
 *  kd.hr.haos.business.util.OrgPermHelper
 *  kd.hr.haos.common.util.PlatformRootUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.model.AuthorizedOrgResult
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRDynamicObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.common.util.concurrent.NullableConcurrentHashMap
 *  kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit
 *  kd.hr.homs.business.domain.batchbill.enums.BillEntryHelperEnum
 *  kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatchChartBaseHelper
 *  kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatchChgHelper
 *  kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillAfterBindDataService
 *  kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillBeforeBindDataService
 *  kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillClosedCallBackService
 *  kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillConfirmCallBackService
 *  kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillCustomEventService
 *  kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillPropertyChangedService
 *  kd.hr.homs.business.domain.batcheffect.strategy.impl.AdminOrgBatchViewStrategyContext
 *  kd.hr.homs.business.domain.orgfast.enums.EntryEntityEnum
 *  kd.hr.homs.common.constants.batchchg.AdminOrgBatchBillConstants
 *  kd.hr.homs.common.constants.batchchg.OrgBatchChgBillConstants
 *  kd.hr.homs.common.enums.AuditStatusEnum
 *  org.apache.commons.lang3.time.DateUtils
 */
package kd.hr.homs.formplugin.web.orgbatch;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.BillEntityType;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.result.IOperateInfo;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.property.BasedataProp;
import kd.bos.ext.hr.entity.property.AdminOrgFieldProp;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.container.Tab;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.EntryGridBindDataListener;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.LoadCustomControlMetasArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeBasedataSetValueEvent;
import kd.bos.form.field.events.BeforeBasedataSetValueListener;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.lang.Lang;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.bill.BillView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.servicehelper.basedata.BaseDataServiceHelper;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.bos.servicehelper.org.OrgUnitServiceHelper;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.haos.business.domain.common.service.impl.BaseDataHelper;
import kd.hr.haos.business.util.BaseDataUtils;
import kd.hr.haos.business.util.OrgPermHelper;
import kd.hr.haos.common.util.PlatformRootUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.model.AuthorizedOrgResult;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRDynamicObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.common.util.concurrent.NullableConcurrentHashMap;
import kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit;
import kd.hr.homs.business.domain.batchbill.enums.BillEntryHelperEnum;
import kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatchChartBaseHelper;
import kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatchChgHelper;
import kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillAfterBindDataService;
import kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillBeforeBindDataService;
import kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillClosedCallBackService;
import kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillConfirmCallBackService;
import kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillCustomEventService;
import kd.hr.homs.business.domain.batchbill.service.impl.AdminOrgBatchBillPropertyChangedService;
import kd.hr.homs.business.domain.batcheffect.strategy.impl.AdminOrgBatchViewStrategyContext;
import kd.hr.homs.business.domain.orgfast.enums.EntryEntityEnum;
import kd.hr.homs.common.constants.batchchg.AdminOrgBatchBillConstants;
import kd.hr.homs.common.constants.batchchg.OrgBatchChgBillConstants;
import kd.hr.homs.common.enums.AuditStatusEnum;
import org.apache.commons.lang3.time.DateUtils;

public final class AdminOrgBatchBillPlugin
extends HRCoreBaseBillEdit
implements ClickListener,
BeforeF7SelectListener,
RowClickEventListener,
EntryGridBindDataListener,
AdminOrgBatchBillConstants,
BeforeBasedataSetValueListener {
    private static final Log LOG = LogFactory.getLog(AdminOrgBatchBillPlugin.class);
    private final Map<String, Map<?, DynamicObject>> baseDataCacheMap = new NullableConcurrentHashMap(3);
    private Map<Long, String> orgLongNameMap;
    private static final String ADD = "add";
    private static final String PARENT = "parent";
    private static final String DISABLE = "disable";
    private static final String INFO_CHG = "infochg";
    private static final String ADD_ORG = "add_org";
    private static final String IS_CANCEL_OVER = "isCancelOver";
    private static final String TAB_AP_ENTRY = "tabapentry";

    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
        FormShowParameter formShowParameter = e.getFormShowParameter();
        if (HRStringUtils.equals((String)formShowParameter.getAppId(), (String)"wftask")) {
            formShowParameter.setCustomParam("FormOperate_SkipAllOpCheckRight", (Object)Boolean.TRUE);
        }
    }

    public void afterCreateNewData(EventObject e) {
        DynamicObject org = this.getModel().getDataEntity().getDynamicObject("org");
        if (org == null) {
            HasPermOrgResult hrPermOrg = OrgPermHelper.getHRPermOrg((boolean)true);
            List hasPermOrgs = hrPermOrg.getHasPermOrgs();
            if (!Objects.isNull(hasPermOrgs) && hasPermOrgs.size() != 0) {
                if (hasPermOrgs.contains(RequestContext.get().getOrgId())) {
                    this.getModel().setValue("org", (Object)RequestContext.get().getOrgId());
                } else {
                    this.getModel().setValue("org", hasPermOrgs.get(0));
                }
            } else {
                LOG.error("AdminorgBatchBillPlugin can not get any org");
            }
        }
        if ((org = this.getModel().getDataEntity().getDynamicObject("org")) == null) {
            this.setBillNo(e);
        } else {
            long orgid = org.getLong("id");
            boolean cudeRule = CodeRuleServiceHelper.isExist((String)"homs_orgbatchchgbill", (DynamicObject)this.getModel().getDataEntity(), (String)String.valueOf(orgid));
            if (!cudeRule) {
                this.setBillNo(e);
            }
        }
    }

    private void setBillNo(EventObject e) {
        HashMap map = new HashMap();
        HashMap item = new HashMap();
        HashMap<Locale, String> emptyTip = new HashMap<Locale, String>();
        map.put("item", item);
        item.put("emptytip", emptyTip);
        Lang lang = Lang.get();
        emptyTip.put(lang.getLocale(), "");
        this.getView().updateControlMetadata("billno", map);
        this.getModel().setValue("billno", (Object)"");
        this.getView().setEnable(Boolean.TRUE, new String[]{"billno"});
        super.afterCreateNewData(e);
    }

    public void loadCustomControlMetas(LoadCustomControlMetasArgs e) {
        super.loadCustomControlMetas(e);
        BillShowParameter billShowParameter = (BillShowParameter)e.getSource();
        LocaleString caption = billShowParameter.getFormConfig().getCaption();
        if (billShowParameter.getPkId() == null) {
            caption.setLocaleValue(ResManager.loadKDString((String)"\u7ec4\u7ec7\u8c03\u6574\u7533\u8bf7", (String)"AdminorgBatchBillPlugin_38", (String)"odc-homs-formplugin", (Object[])new Object[0]));
        } else {
            HRBaseServiceHelper hrBaseServiceHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
            DynamicObject billObject = hrBaseServiceHelper.queryOne("id,billno", billShowParameter.getPkId());
            if (billObject != null) {
                caption.setLocaleValue(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7ec4\u7ec7\u8c03\u6574\u7533\u8bf7-%s", (String)"AdminorgBatchBillPlugin_16", (String)"odc-homs-formplugin", (Object[])new Object[0]), billObject.getString("billno")));
            }
        }
    }

    public void beforeBindData(EventObject e) {
        long startTime = System.currentTimeMillis();
        AdminOrgBatchBillBeforeBindDataService beforeBindDataService = new AdminOrgBatchBillBeforeBindDataService(this.getView());
        beforeBindDataService.beforeBindData(e);
        LOG.info(String.format(Locale.ROOT, "AdminorgBatchBillPlugin beforeBindData() time cost is: %s", System.currentTimeMillis() - startTime));
    }

    public void afterBindData(EventObject e) {
        long startTime = System.currentTimeMillis();
        AdminOrgBatchBillAfterBindDataService afterBindDataService = new AdminOrgBatchBillAfterBindDataService(this.getView());
        afterBindDataService.afterBindData(e);
        LOG.info(String.format(Locale.ROOT, "AdminorgBatchBillPlugin afterBindData() time cost is: %s", System.currentTimeMillis() - startTime));
    }

    public void beforeBasedataSetValue(BeforeBasedataSetValueEvent beforeBasedataSetValueEvent) {
        Object value = beforeBasedataSetValueEvent.getValue();
        Object source = beforeBasedataSetValueEvent.getSource();
        if (value instanceof ListSelectedRowCollection && source instanceof BasedataEdit) {
            ListSelectedRowCollection rows = (ListSelectedRowCollection)value;
            BasedataEdit basedataEdit = (BasedataEdit)source;
            IDataEntityProperty property = basedataEdit.getProperty();
            if (property instanceof AdminOrgFieldProp && basedataEdit.isF7MultipleSelect()) {
                DynamicObject[] sourceVDys;
                DynamicObject billDy = this.getModel().getDataEntity();
                String fieldKey = basedataEdit.getFieldKey();
                String baseEntityId = ((BasedataProp)property).getBaseEntityId();
                HRBaseServiceHelper helper = new HRBaseServiceHelper(baseEntityId);
                DynamicObject[] baseDataDys = helper.loadDynamicObjectArray(rows.stream().map(ListSelectedRow::getPrimaryKeyValue).distinct().toArray());
                HashSet parentBoIds = Sets.newHashSetWithExpectedSize((int)baseDataDys.length);
                HashMap baseDataDyMap = Maps.newHashMapWithExpectedSize((int)baseDataDys.length);
                for (DynamicObject orgDy : baseDataDys) {
                    parentBoIds.add(orgDy.getLong("parentorg.boid"));
                    baseDataDyMap.put(orgDy.getPkValue(), orgDy);
                }
                DynamicObject[] parentBoDys = helper.loadDynamicObjectArray(parentBoIds.toArray());
                HashSet sourceIds = Sets.newHashSetWithExpectedSize((int)parentBoDys.length);
                for (DynamicObject parentBoDy : parentBoDys) {
                    sourceIds.add(parentBoDy.getLong("sourcevid"));
                }
                for (DynamicObject sourceVDy : sourceVDys = helper.loadDynamicObjectArray(sourceIds.toArray())) {
                    baseDataDyMap.put(sourceVDy.getPkValue(), sourceVDy);
                }
                this.baseDataCacheMap.put(baseEntityId, baseDataDyMap);
                Date date = billDy.getDate("effdt");
                if (date == null) {
                    date = HRDateTimeUtils.getNowDate();
                }
                long billId = (Long)this.getModel().getValue("id");
                this.orgLongNameMap = AdminOrgBatchChgHelper.getThisBillOrgFullName((long)billId, (Collection)parentBoIds, (Date)date, (IFormView)this.getView());
                String prefix = AdminOrgBatchBillPlugin.getPrefixByFieldKey(fieldKey);
                if (HRStringUtils.isNotEmpty((String)prefix)) {
                    Long changeTypeId = BillEntryHelperEnum.getChangeTypeIdByPrefix((String)prefix);
                    QFilter statusFilter = new QFilter("status", "=", (Object)"C");
                    QFilter enableFilter = new QFilter("enable", "=", (Object)Character.valueOf('1'));
                    QFilter changeTyeFilter = new QFilter("orgchangetype.id", "=", (Object)changeTypeId);
                    DynamicObject buDy = billDy.getDynamicObject("org");
                    long buId = buDy == null ? RequestContext.get().getOrgId() : buDy.getLong("id");
                    QFilter baseDataFilter = BaseDataHelper.getBaseDataFilter((String)"haos_changescene", (long)buId);
                    statusFilter.and(enableFilter).and(changeTyeFilter).and(baseDataFilter);
                    this.baseDataCacheMap.put("haos_changescene", BaseDataUtils.getBaseDataMap((String)"haos_changescene", (QFilter)statusFilter));
                }
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        AdminOrgBatchBillPropertyChangedService propertyChangedService = new AdminOrgBatchBillPropertyChangedService(this.getView(), this.baseDataCacheMap, this.orgLongNameMap);
        propertyChangedService.setCloseCallBackPlugin((IFormPlugin)this);
        propertyChangedService.propertyChanged(e);
    }

    public void customEvent(CustomEventArgs evt) {
        AdminOrgBatchBillCustomEventService customEventService = new AdminOrgBatchBillCustomEventService(this.getView());
        customEventService.setCloseCallBackPlugin((IFormPlugin)this);
        customEventService.setPluginName(this.getPluginName());
        customEventService.customEvent(evt);
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        super.confirmCallBack(event);
        AdminOrgBatchBillConfirmCallBackService confirmCallBackService = new AdminOrgBatchBillConfirmCallBackService(this.getView());
        confirmCallBackService.confirmCallBack(event);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        AdminOrgBatchBillClosedCallBackService closedCallBackService = new AdminOrgBatchBillClosedCallBackService(this.getView());
        closedCallBackService.setPluginName(this.getPluginName());
        closedCallBackService.closedCallBack(closedCallBackEvent);
    }

    public void beforeClosed(BeforeClosedEvent e) {
        String isCancelOverFlag = this.getView().getPageCache().get(IS_CANCEL_OVER);
        if (HRStringUtils.isNotEmpty((String)isCancelOverFlag) && "true".equals(isCancelOverFlag)) {
            this.getModel().setDataChanged(false);
            this.getView().getPageCache().put(IS_CANCEL_OVER, "false");
            return;
        }
        boolean hasEntryData = false;
        DynamicObject dataEntity = this.getModel().getDataEntity(Boolean.TRUE.booleanValue());
        for (BillEntryHelperEnum billEntryEnum : BillEntryHelperEnum.values()) {
            DynamicObjectCollection entryDynColl = dataEntity.getDynamicObjectCollection(billEntryEnum.getEntryName());
            boolean bl = hasEntryData = entryDynColl != null && entryDynColl.size() > 0;
            if (hasEntryData) break;
        }
        HRBaseServiceHelper orgBatchChgBillHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
        long billId = this.getModel().getDataEntity().getLong("id");
        QFilter idFilter = new QFilter("id", "=", (Object)billId);
        boolean isExistFromDb = orgBatchChgBillHelper.isExists(idFilter);
        if (hasEntryData && !isExistFromDb) {
            e.setCancel(true);
            ConfirmCallBackListener confirmCallBacks = new ConfirmCallBackListener("close_bill", (IFormPlugin)this);
            HashMap<Integer, String> btnNameMaps = new HashMap<Integer, String>();
            btnNameMaps.put(MessageBoxResult.Cancel.getValue(), ResManager.loadKDString((String)"\u8fd4\u56de\u7f16\u8f91", (String)"AdminorgBatchBillPlugin_2", (String)"odc-homs-formplugin", (Object[])new Object[0]));
            btnNameMaps.put(MessageBoxResult.Yes.getValue(), ResManager.loadKDString((String)"\u76f4\u63a5\u9000\u51fa", (String)"AdminorgBatchBillPlugin_3", (String)"odc-homs-formplugin", (Object[])new Object[0]));
            MessageBoxOptions options = MessageBoxOptions.OKCancel;
            String msg = ResManager.loadKDString((String)"\u68c0\u6d4b\u5230\u60a8\u6709\u66f4\u6539\u5185\u5bb9\uff0c\u662f\u5426\u4e0d\u4fdd\u5b58\u76f4\u63a5\u9000\u51fa\uff1f\u82e5\u76f4\u63a5\u9000\u51fa\uff0c\u5c06\u4e22\u5931\u8fd9\u4e9b\u66f4\u6539\u3002", (String)"AdminorgBatchBillPlugin_1", (String)"odc-homs-formplugin", (Object[])new Object[0]);
            this.getView().showConfirm(msg, this.getModel().getChangeDesc(), options, ConfirmTypes.Save, confirmCallBacks, btnNameMaps);
        }
    }

    private boolean executeSaveOperation(String operateKey, OperateOption operateOption) {
        OperateOption option = OperateOption.create();
        if (operateOption != null) {
            option.mergeValue(operateOption);
        }
        option.setVariableValue("ignoreValidation", "true");
        option.setVariableValue("OP_OPERATE_KEY", operateKey);
        option.setVariableValue("isshowmessage", "false");
        OperationResult saveOperationResult = this.getView().invokeOperation("save_no_log", option);
        if (!saveOperationResult.isSuccess()) {
            this.getView().showOperationResult(saveOperationResult);
            return false;
        }
        return true;
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate operate = (AbstractOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        OperateOption option = operate.getOption();
        if (this.getAllEntrySize() == 0) {
            option.setVariableValue("ishasright", Boolean.TRUE.toString());
            option.setVariableValue("skipCheckDataPermission", Boolean.TRUE.toString());
            option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
        }
        if (HRStringUtils.equals((String)"import_multientry_hr", (String)operate.getOperateKey())) {
            if (this.getModel().getValue("org") == null) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u586b\u5199\u7ec4\u7ec7\u4f53\u7cfb\u7ba1\u7406\u7ec4\u7ec7\u3002", (String)"AdminorgBatchBillPlugin_64", (String)"odc-homs-formplugin", (Object[])new Object[0]));
                args.setCancel(Boolean.TRUE.booleanValue());
                return;
            }
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
            DynamicObject dataEntity = this.getModel().getDataEntity(Boolean.TRUE.booleanValue());
            long billId = dataEntity.getLong("id");
            DynamicObject[] dynamicObjects = serviceHelper.query("billstatus", new QFilter[]{new QFilter("id", "=", (Object)billId)});
            if (dynamicObjects.length > 0 && !Sets.newHashSet((Object[])new String[]{"A", "G"}).contains(dynamicObjects[0].getString("billstatus"))) {
                String auditStatus = dynamicObjects[0].getString("billstatus");
                this.getView().showTipNotification(String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u7684\u5355\u636e\u4e0d\u80fd\u5bfc\u5165\u3002", (String)"AdminorgBatchBillPlugin_65", (String)"odc-homs-formplugin", (Object[])new Object[0]), AuditStatusEnum.getName((String)auditStatus)));
                args.setCancel(Boolean.TRUE.booleanValue());
            }
        }
        if (HRStringUtils.equals((String)operateKey, (String)"save") || HRStringUtils.equals((String)operateKey, (String)"submit") || HRStringUtils.equals((String)operateKey, (String)"submiteffect")) {
            AdminOrgBatchChgHelper.setAdminorgBoIdToEntryEntity((IFormView)this.getView());
            if (HRStringUtils.equals((String)operateKey, (String)"submit") || HRStringUtils.equals((String)operateKey, (String)"submiteffect")) {
                if (!this.executeSaveOperation(operateKey, operate.getOption())) {
                    args.setCancel(true);
                } else {
                    option.setVariableValue("ignoreValidation", "true");
                }
            }
        }
        if ("delete_rows".equals(operateKey)) {
            String currentTab = ((Tab)this.getView().getControl(TAB_AP_ENTRY)).getCurrentTab();
            int index = currentTab.indexOf(95);
            String prefix = currentTab.substring(index + 1);
            String entryName = BillEntryHelperEnum.getEntryNameByPrefix((String)prefix);
            if (BillEntryHelperEnum.addEntry.getEntryName().equals(entryName)) {
                EntryGrid entryGrid = (EntryGrid)this.getView().getControl(entryName);
                int[] selectRows = entryGrid.getSelectRows();
                operate.getOption().setVariableValue("OP_ROW_INDEX", JSONObject.toJSONString((Object)selectRows));
            }
        }
        if (HRStringUtils.equals((String)this.getView().getFormShowParameter().getAppId(), (String)"wftask")) {
            option.setVariableValue("ishasright", Boolean.TRUE.toString());
            option.setVariableValue("skipCheckDataPermission", Boolean.TRUE.toString());
            option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs eventArgs) {
        OperationResult afterOperationResult;
        String[] splitArrStr;
        String opKey;
        super.afterDoOperation(eventArgs);
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (HRStringUtils.equals((String)this.getView().getFormShowParameter().getAppId(), (String)"wftask")) {
            ((BillView)this.getView()).setBillStatus(BillOperationStatus.EDIT);
            this.getView().getFormShowParameter().setStatus(OperationStatus.EDIT);
            ((BillShowParameter)this.getView().getFormShowParameter()).setBillStatus(BillOperationStatus.EDIT);
            String billStatus = ((BillEntityType)this.getModel().getDataEntityType()).getBillStatus();
            this.getModel().setValue(billStatus, (Object)"A");
            this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
        }
        if ("viewflowchart".equals(opKey = eventArgs.getOperateKey())) {
            return;
        }
        if ("submiteffect".equals(opKey) || "save".equals(opKey) || "submit".equals(opKey)) {
            List entryKeys = EntryEntityEnum.getEntryKeysWithExt();
            for (String entryKey : entryKeys) {
                splitArrStr = entryKey.split("_");
                AdminOrgBatchChgHelper.setTabPageTextAndVisible((String)splitArrStr[1], (IFormView)this.getView());
            }
        }
        if ((afterOperationResult = eventArgs.getOperationResult()) != null && !afterOperationResult.isSuccess()) {
            if ("submit".equals(opKey)) {
                this.afterSubmitError(afterOperationResult);
            }
            return;
        }
        switch (opKey) {
            case "add": {
                AdminOrgBatchChgHelper.openViewForm((IFormView)this.getView(), (String)ADD, (String)this.getPluginName(), null);
                break;
            }
            case "edit_add": {
                this.openEditAddPage(status, this.getModel(), this.getView());
                break;
            }
            case "parent_edit": {
                AdminOrgBatchChgHelper.entryEntityMore((IDataModel)this.getModel(), (IFormView)this.getView(), (String)PARENT, (OperationStatus)status);
                break;
            }
            case "edit_info": {
                AdminOrgBatchChgHelper.entryEntityMore((IDataModel)this.getModel(), (IFormView)this.getView(), (String)"info", (OperationStatus)status);
                break;
            }
            case "delete_rows": {
                this.deleteRowsConfirm();
                break;
            }
            case "save": {
                if (eventArgs.getOperationResult() == null || !eventArgs.getOperationResult().isSuccess()) break;
                this.getModel().setDataChanged(false);
                break;
            }
            case "unsubmit": {
                if (eventArgs.getOperationResult() == null || !eventArgs.getOperationResult().isSuccess()) break;
                this.getView().getFormShowParameter().setStatus(OperationStatus.EDIT);
                this.getView().cacheFormShowParameter();
                this.getView().updateView();
                this.getModel().setDataChanged(false);
                break;
            }
            case "submit": {
                this.afterSubmit();
                break;
            }
            case "org_merge": {
                this.mergeOperation();
                break;
            }
            case "edit_merge_detail": {
                this.moreMergeOrg();
                break;
            }
            case "org_split": {
                this.splitOperation();
                break;
            }
            case "edit_split_detail": {
                this.moreSplitOrg();
                break;
            }
            case "newentry_parent": 
            case "newentry_info": 
            case "newentry_disable": {
                splitArrStr = opKey.split("_");
                AdminOrgBatchChgHelper.setTabPageTextAndVisible((String)splitArrStr[1], (IFormView)this.getView());
                break;
            }
            case "submiteffect": {
                AdminOrgBatchViewStrategyContext.handleViewElementByAuditStatus((String)((String)this.getModel().getValue("status")), (IFormView)this.getView());
                break;
            }
        }
    }

    private void mergeOperation() {
        AdminOrgBatchChartBaseHelper.openNewMergePage(new HashSet(), (IFormView)this.getView(), (IFormPlugin)this);
    }

    private void moreMergeOrg() {
        EntryGrid entryEntity = (EntryGrid)this.getView().getControl("entryentity_merge");
        int row = entryEntity.getEntryState().getFocusRow();
        DynamicObject mergeOrgEntry = (DynamicObject)this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity_merge").get(row);
        AdminOrgBatchChartBaseHelper.openEditMergePage((DynamicObject)mergeOrgEntry, (IFormView)this.getView(), (IFormPlugin)this);
    }

    private void splitOperation() {
        AdminOrgBatchChartBaseHelper.openSplit(null, null, (IFormView)this.getView(), (CloseCallBack)new CloseCallBack((IFormPlugin)this, "org_split"));
    }

    private void moreSplitOrg() {
        EntryGrid entryEntity = (EntryGrid)this.getView().getControl("entryentity_split");
        int row = entryEntity.getEntryState().getFocusRow();
        DynamicObject splitOrgEntry = (DynamicObject)this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity_split").get(row);
        AdminOrgBatchChartBaseHelper.openSplit((Long)splitOrgEntry.getLong("split_origin_id"), null, (IFormView)this.getView(), (CloseCallBack)new CloseCallBack((IFormPlugin)this, "edit_split_detail"));
    }

    private void afterSubmitError(OperationResult operationResult) {
        boolean needWfAssignPersons = operationResult.isNeedWfAssignPersons();
        List allErrorOrValidateInfo = operationResult.getAllErrorOrValidateInfo();
        String errorMsg = allErrorOrValidateInfo.isEmpty() ? operationResult.getMessage() : ((IOperateInfo)operationResult.getAllErrorOrValidateInfo().get(0)).getMessage();
        if ((errorMsg == null || errorMsg.isEmpty()) && needWfAssignPersons) {
            return;
        }
        String exist = ResManager.loadKDString((String)"\u5df2\u5b58\u5728", (String)"AdminorgBatchBillPlugin_66", (String)"odc-homs-formplugin", (Object[])new Object[0]);
        if (errorMsg != null && !errorMsg.contains(exist)) {
            this.getView().showErrorNotification(errorMsg);
        }
    }

    private void afterSubmit() {
        LOG.info("afterDoSubmit:" + this.getModel().getDataEntity().get("billno"));
        LOG.info("afterDoSubmit getEntityId refresh:" + this.getView().getFormShowParameter().getAppId());
        if (HRStringUtils.equals((String)this.getView().getFormShowParameter().getAppId(), (String)"wftask")) {
            LOG.info("afterDoSubmit refresh:" + this.getModel().getDataEntity().get("billno"));
            this.getView().getParentView().invokeOperation("refresh");
            this.getView().invokeOperation("close");
        } else {
            this.getView().updateView();
        }
    }

    private void deleteRowsConfirm() {
        long id = this.getModel().getDataEntity().getLong("id");
        HRBaseServiceHelper helper = new HRBaseServiceHelper("homs_orgbatchchgbill");
        QFilter idFilter = new QFilter("id", "=", (Object)id);
        DynamicObject dyn = helper.queryOne("id, billstatus", new QFilter[]{idFilter});
        String billstatus = Objects.isNull(dyn) ? "A" : dyn.getString("billstatus");
        Boolean enableDeleteForUserPage = billstatus.equals("A") || billstatus.equals("G");
        Boolean enableDeleteForAuditPage = HRStringUtils.equals((String)this.getView().getFormShowParameter().getAppId(), (String)"wftask") && billstatus.equals("B");
        if (!enableDeleteForUserPage.booleanValue() && !enableDeleteForAuditPage.booleanValue()) {
            String auditStatusName = AdminOrgBatchChgHelper.getAuditstatusName((String)billstatus);
            this.getView().showErrorNotification(ResManager.loadKDString((String)"%s\u7684\u5355\u636e\u4e0d\u80fd\u5220\u9664\u8c03\u6574\u660e\u7ec6\u3002", (String)"AdminorgBatchBillPlugin_4", (String)"odc-homs-formplugin", (Object[])new Object[]{auditStatusName}));
            return;
        }
        String currentTab = ((Tab)this.getView().getControl(TAB_AP_ENTRY)).getCurrentTab();
        int index = currentTab.indexOf(95);
        String prefix = currentTab.substring(index + 1);
        String entryName = BillEntryHelperEnum.getEntryNameByPrefix((String)prefix);
        EntryGrid coopRelEntryGrid = (EntryGrid)this.getView().getControl(entryName);
        if (null == coopRelEntryGrid) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u4e2d\u9700\u8981\u5220\u9664\u7684\u884c\u3002", (String)"AdminorgBatchBillPlugin_9", (String)"odc-homs-formplugin", (Object[])new Object[0]));
            return;
        }
        int[] selectRows = coopRelEntryGrid.getSelectRows();
        int length = selectRows.length;
        if (selectRows.length > 0) {
            ConfirmCallBackListener deleteEntryCallBackListener = new ConfirmCallBackListener("del_rows", (IFormPlugin)this);
            if (("merge".equals(prefix) || "split".equals(prefix)) && this.showMessageForMergeAndSplit(prefix, selectRows, deleteEntryCallBackListener)) {
                return;
            }
            this.getView().showConfirm(ResManager.loadKDString((String)"\u5220\u9664\u9009\u4e2d\u7684\u6570\u636e\u540e\u5c06\u65e0\u6cd5\u6062\u590d\uff08\u9009\u4e2d\u6761\u6570\uff1a%s\uff09\uff0c\u786e\u5b9a\u8981\u5220\u9664\u5417\uff1f", (String)"AdminorgBatchBillPlugin_6", (String)"odc-homs-formplugin", (Object[])new Object[]{length}), MessageBoxOptions.OKCancel, ConfirmTypes.Delete, deleteEntryCallBackListener);
        } else {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u4e2d\u9700\u8981\u64cd\u4f5c\u7684\u884c\u3002", (String)"AdminorgBatchBillPlugin_7", (String)"odc-homs-formplugin", (Object[])new Object[0]));
        }
    }

    private boolean showMessageForMergeAndSplit(String prefix, int[] selectRows, ConfirmCallBackListener deleteEntryCallBackListener) {
        ArrayList selectDyn = Lists.newArrayListWithCapacity((int)16);
        for (int row : selectRows) {
            selectDyn.add(this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity_" + prefix).get(row));
        }
        HashMap orgId2Name = Maps.newHashMapWithExpectedSize((int)16);
        for (DynamicObject entryEntityDyn : selectDyn) {
            DynamicObjectCollection toMergeOrg = entryEntityDyn.getDynamicObjectCollection("merge".equals(prefix) ? "to_merge_org" : "split_target_org");
            DynamicObject mergeTargetOrg = entryEntityDyn.getDynamicObject("merge".equals(prefix) ? "merge_target_org" : "to_split_org");
            for (DynamicObject dy : toMergeOrg) {
                orgId2Name.put(dy.getLong("fbasedataid.id"), dy.getString("fbasedataid.name"));
            }
            if (mergeTargetOrg == null) continue;
            orgId2Name.put(mergeTargetOrg.getLong("id"), mergeTargetOrg.getString("name"));
        }
        HashMap orgIdToChangeType = Maps.newHashMapWithExpectedSize((int)16);
        List entryKeys = EntryEntityEnum.getEntryKeys();
        for (String entryKey : entryKeys) {
            if ("entryentity_merge".equals(entryKey) || "entryentity_split".equals(entryKey)) continue;
            DynamicObjectCollection dynamicObjectCollection = this.getModel().getDataEntity(true).getDynamicObjectCollection(entryKey);
            for (DynamicObject dy : dynamicObjectCollection) {
                if (entryKey.equals(EntryEntityEnum.addEntry.getEntryKey())) {
                    orgIdToChangeType.put(dy.getLong("add_adminorg"), OrgBatchChgBillConstants.CHANGE_TYPE_ADD);
                    continue;
                }
                orgIdToChangeType.put(dy.getLong(entryKey.substring(entryKey.indexOf("_") + 1) + "_adminorg.id"), BillEntryHelperEnum.getChangeTypeIdByPrefix((String)entryKey.substring(entryKey.indexOf("_") + 1)));
            }
        }
        StringBuilder showMsg = new StringBuilder();
        for (Map.Entry entry : orgId2Name.entrySet()) {
            if (!orgIdToChangeType.containsKey(entry.getKey())) continue;
            showMsg.append(String.format(ResManager.loadKDString((String)"\u884c\u653f\u7ec4\u7ec7\u201c%1$s\u201d\u5728\u672c\u5355\u4e2d\u5df2\u7ecf\u53d1\u8d77\u201c%2$s\u201d\u64cd\u4f5c\uff0c\u4e0d\u5141\u8bb8\u540c\u65f6\u5bf9\u7ec4\u7ec7\u8fdb\u884c\u591a\u79cd\u53d8\u52a8\u64cd\u4f5c\u3002", (String)"AdminorgBatchBillPlugin_42", (String)"odc-homs-formplugin", (Object[])new Object[0]), entry.getValue(), BillEntryHelperEnum.getEntryDescriptionByChangeType((Long)((Long)orgIdToChangeType.get(entry.getKey()))))).append('\n');
        }
        if (showMsg.length() != 0) {
            if ("merge".equals(prefix)) {
                this.getView().showConfirm(String.format(ResManager.loadKDString((String)"%s \u5220\u9664\u5408\u5e76\u6570\u636e\u540e\u7cfb\u7edf\u5c06\u4e0d\u4f1a\u81ea\u52a8\u5220\u9664\u8be5\u6761\u53d8\u52a8\u6570\u636e\uff0c\u9700\u7528\u6237\u624b\u52a8\u5220\u9664\uff0c\u786e\u5b9a\u5220\u9664\u5408\u5e76\u6570\u636e\uff1f", (String)"AdminorgBatchBillPlugin_41", (String)"odc-homs-formplugin", (Object[])new Object[0]), showMsg.toString()), MessageBoxOptions.OKCancel, ConfirmTypes.Delete, deleteEntryCallBackListener);
                return true;
            }
            this.getView().showConfirm(String.format(ResManager.loadKDString((String)"%s \u5220\u9664\u62c6\u5206\u6570\u636e\u540e\u7cfb\u7edf\u5c06\u4e0d\u4f1a\u81ea\u52a8\u5220\u9664\u8be5\u6761\u53d8\u52a8\u6570\u636e\uff0c\u9700\u7528\u6237\u624b\u52a8\u5220\u9664\uff0c\u786e\u5b9a\u5220\u9664\u62c6\u5206\u6570\u636e\uff1f", (String)"AdminorgBatchBillPlugin_39", (String)"odc-homs-formplugin", (Object[])new Object[0]), showMsg.toString()), MessageBoxOptions.OKCancel, ConfirmTypes.Delete, deleteEntryCallBackListener);
            return true;
        }
        return false;
    }

    private void openEditAddPage(OperationStatus status, IDataModel iDataModel, IFormView iFormView) {
        EntryGrid strategyEntryGrid = (EntryGrid)this.getView().getControl("entryentity_add");
        int row = strategyEntryGrid.getEntryState().getFocusRow();
        DynamicObject addDy = (DynamicObject)this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentity_add").get(row);
        Long addDyId = addDy.getLong("id");
        DynamicObject dataEntity = iDataModel.getDataEntity(true);
        DynamicObject dynamicObject = (DynamicObject)dataEntity.getDynamicObjectCollection("entryentity_add").get(row);
        Map map = HRDynamicObjectUtils.convertDynamicObjectToMap((DynamicObject)dynamicObject);
        BillShowParameter formShowParameter = new BillShowParameter();
        formShowParameter.setCustomParam("org_bsed", (Object)(iFormView.getModel().getValue("effdt") == null ? HRDateTimeUtils.getNowDateTime().getTime() : ((Date)iFormView.getModel().getValue("effdt")).getTime()));
        formShowParameter.setFormId("homs_batchorgentityadd");
        formShowParameter.setPkId((Object)addDyId);
        formShowParameter.setStatus(status);
        formShowParameter.setCustomParam("billid", (Object)iFormView.getModel().getDataEntity().getLong("id"));
        if (HRStringUtils.equals((String)this.getView().getFormShowParameter().getAppId(), (String)"wftask")) {
            formShowParameter.setHasRight(true);
        }
        if (!status.equals((Object)OperationStatus.VIEW)) {
            formShowParameter.setCloseCallBack(new CloseCallBack(this.getPluginName(), ADD_ORG));
        }
        formShowParameter.setCustomParam("selectObject", (Object)SerializationUtils.toJsonString((Object)map));
        formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
        formShowParameter.setCustomParam("OP_VALIDATOR_BILL_ORG_ALL_CHANGE_ORG_KEY", (Object)AdminOrgBatchChgHelper.getThisBillAllValidateDataStr((DynamicObject)dataEntity));
        formShowParameter.setCustomParam("OP_VALIDATOR_BILL_ORG_DISABLE_KEY", (Object)AdminOrgBatchChgHelper.getThisBillDisableOrgIdsStr((DynamicObject)dataEntity));
        formShowParameter.setCustomParam("OP_VALIDATOR_BILL_ORG_PARENT_TOBE_DISABLE_KEY", (Object)AdminOrgBatchChgHelper.getThisBillToBeDisableOrgIdsStr((DynamicObject)dataEntity));
        formShowParameter.setCustomParam("OP_VALIDATOR_BILL_ORG_NEW_ORG_NUMBERS_KEY", (Object)AdminOrgBatchChgHelper.getThisBillOrgNumbersStr((DynamicObject)dataEntity));
        this.getView().showForm((FormShowParameter)formShowParameter);
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity_parent");
        entryGrid.addDataBindListener((EntryGridBindDataListener)this);
        List<String> beforeF7ListenerControlList = this.getRegisterBeforeF7ListenerControlList();
        for (String registerBeforeF7Listener : beforeF7ListenerControlList) {
            BasedataEdit basedataEdit = (BasedataEdit)this.getControl(registerBeforeF7Listener);
            if (basedataEdit == null) continue;
            basedataEdit.setF7BatchFill(false);
            basedataEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
            basedataEdit.addBeforeBasedataSetValueListener((BeforeBasedataSetValueListener)this);
        }
    }

    private List<String> getRegisterBeforeF7ListenerControlList() {
        return Arrays.asList("info_org", "parent_org", "to_merge_org", "merge_target_org", "to_split_org", "split_target_org", "parent_adminorg", "parent_parentorg", "parent_adminorgtype", "parent_changescene", "parent_changereason", "parent_city", "info_adminorg", "info_adminorgtype", "info_changescene", "info_changereason", "info_city", "disable_adminorg", "disable_changescene", "disable_changereason", "parent_corporateorg", "info_corporateorg", "disorg", "parent_adminorglayer", "parent_adminorgfunction", "info_adminorglayer", "info_adminorgfunction", "merge_changescene", "merge_changereason", "split_changescene", "split_changereason");
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String fieldKey = beforeF7SelectEvent.getProperty().getName();
        this.setFuzzySearchAuthFilter(beforeF7SelectEvent);
        this.replaceFormIdByFieldKey(beforeF7SelectEvent);
        if ("disorg".equals(fieldKey)) {
            beforeF7SelectEvent.getFormShowParameter().setCaption(ResManager.loadKDString((String)"\u7b7e\u53d1\u7ec4\u7ec7", (String)"AdminorgBatchBillPlugin_10", (String)"odc-homs-formplugin", (Object[])new Object[0]));
            beforeF7SelectEvent.getCustomQFilters().add(BaseDataHelper.getAdminOrgBaseDataFilter((String)"haos_adminorgdetail", Collections.singletonList(((DynamicObject)this.getView().getModel().getValue("org")).getLong("id"))));
            return;
        }
        int rowIndex = beforeF7SelectEvent.getRow();
        String prefix = AdminOrgBatchBillPlugin.getPrefixByFieldKey(fieldKey);
        String entryName = BillEntryHelperEnum.getEntryNameByPrefix((String)prefix);
        DynamicObjectCollection entryDynColl = this.getModel().getDataEntity(true).getDynamicObjectCollection(entryName);
        DynamicObject focusEntryDyn = (DynamicObject)entryDynColl.get(rowIndex);
        Date date = DateUtils.truncate((Date)new Date(), (int)5);
        MainEntityType entityType = EntityMetadataCache.getDataEntityType((String)this.getView().getEntityId());
        IDataEntityProperty property = (IDataEntityProperty)entityType.getAllFields().get(fieldKey);
        IDataEntityType parent = property.getParent();
        Date effectDate = (Date)this.getModel().getValue("effdt");
        if (this.checkOrgValue(beforeF7SelectEvent, property, parent)) {
            return;
        }
        ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
        switch (fieldKey) {
            case "parent_adminorg": {
                QFilter rootFilter = new QFilter("boid", "!=", (Object)PlatformRootUtils.getLongRootIdOfPlatform());
                beforeF7SelectEvent.getCustomQFilters().add(rootFilter);
                showParameter.getCustomParams().put("searchdate", date);
                AdminOrgBatchChgHelper.filterF7Org((IFormView)this.getView(), (BeforeF7SelectEvent)beforeF7SelectEvent, (QFilter)rootFilter);
                this.setFilterByOrg(beforeF7SelectEvent);
                beforeF7SelectEvent.getCustomQFilters().add(new QFilter("isvirtualorg", "!=", (Object)Character.valueOf('1')));
                break;
            }
            case "info_adminorg": 
            case "disable_adminorg": {
                this.setFilterByOrg(beforeF7SelectEvent);
                showParameter.getCustomParams().put("searchdate", date);
                AdminOrgBatchChgHelper.filterF7Org((IFormView)this.getView(), (BeforeF7SelectEvent)beforeF7SelectEvent, null);
                beforeF7SelectEvent.getCustomQFilters().add(new QFilter("isvirtualorg", "!=", (Object)Character.valueOf('1')));
                this.excludeOrgIfNeed(beforeF7SelectEvent);
                if (!HRStringUtils.equals((String)fieldKey, (String)"disable_adminorg")) break;
                AdminOrgBatchChgHelper.excludeMergeSplitTargetOrg((IFormView)this.getView(), (List)beforeF7SelectEvent.getCustomQFilters());
                break;
            }
            case "parent_parentorg": {
                DynamicObject adminOrg = focusEntryDyn.getDynamicObject(prefix + "_" + "adminorg");
                AdminOrgBatchChgHelper.setParentFilter((IFormView)this.getView(), (DynamicObject)adminOrg, (BeforeF7SelectEvent)beforeF7SelectEvent);
                beforeF7SelectEvent.getFormShowParameter().setCaption(ResManager.loadKDString((String)"\u4e0a\u7ea7\u884c\u653f\u7ec4\u7ec7", (String)"AdminorgBatchBillPlugin_11", (String)"odc-homs-formplugin", (Object[])new Object[0]));
                break;
            }
            case "to_merge_org": 
            case "merge_target_org": 
            case "split_target_org": 
            case "to_split_org": {
                beforeF7SelectEvent.getFormShowParameter().setCaption(ResManager.loadKDString((String)"\u884c\u653f\u7ec4\u7ec7", (String)"AdminorgFastChgPlugin_9", (String)"odc-homs-formplugin", (Object[])new Object[0]));
                DynamicObject dataEntityParent = this.getView().getModel().getDataEntity(true);
                DynamicObjectCollection addCollection = dataEntityParent.getDynamicObjectCollection("entryentity_add");
                Set adminOrgSet = addCollection.stream().map(s -> s.getString("add_adminorg")).collect(Collectors.toSet());
                Set<Object> addOrgIdSet = new HashSet(adminOrgSet.size());
                if (HRStringUtils.equals((String)"merge_target_org", (String)fieldKey) || HRStringUtils.equals((String)"split_target_org", (String)fieldKey)) {
                    addOrgIdSet = adminOrgSet.stream().map(Long::valueOf).collect(Collectors.toSet());
                    DynamicObjectCollection disableObj = dataEntityParent.getDynamicObjectCollection("entryentity_disable");
                    Set disableOrgIdSet = disableObj.stream().map(obj -> obj.getLong("disable_adminorg.id")).collect(Collectors.toSet());
                    if (!CollectionUtils.isEmpty(disableOrgIdSet)) {
                        QFilter disableOrgFilter = new QFilter("id", "not in", disableOrgIdSet);
                        beforeF7SelectEvent.getCustomQFilters().add(disableOrgFilter);
                    }
                }
                QFilter currentOrderFilter = new QFilter("id", "in", addOrgIdSet);
                this.setFilterByOrg(beforeF7SelectEvent);
                this.excludeOrgIfNeed(beforeF7SelectEvent);
                AdminOrgBatchChgHelper.setMergeAndSplitOrgFilter((IFormView)this.getView(), (BeforeF7SelectEvent)beforeF7SelectEvent, (QFilter)currentOrderFilter);
                if ("merge_target_org".equals(beforeF7SelectEvent.getProperty().getName()) || "split_target_org".equals(beforeF7SelectEvent.getProperty().getName())) {
                    beforeF7SelectEvent.getCustomQFilters().add(new QFilter("iscurrentversion", "=", (Object)"0").or(currentOrderFilter));
                    break;
                }
                beforeF7SelectEvent.getFormShowParameter().getCustomParams().put("searchdate", date);
                beforeF7SelectEvent.getCustomQFilters().add(new QFilter("iscurrentversion", "=", (Object)"0").or(currentOrderFilter));
                break;
            }
            case "parent_changescene": 
            case "info_changescene": 
            case "disable_changescene": 
            case "merge_changescene": 
            case "split_changescene": {
                QFilter changeTyeFilter = new QFilter("orgchangetype.id", "=", (Object)focusEntryDyn.getLong(prefix + "_changetype.id")).and("id", "!=", (Object)OrgBatchChgBillConstants.CHANGE_SCENE_ENABLE);
                beforeF7SelectEvent.getCustomQFilters().add(changeTyeFilter);
                break;
            }
            case "info_adminorgtype": {
                long orgid;
                long rootOrgId = OrgUnitServiceHelper.getRootOrgId();
                long l = orgid = focusEntryDyn.getDynamicObject("info_adminorg") != null ? focusEntryDyn.getLong("info_adminorg.boid") : 0L;
                if (rootOrgId != orgid) break;
                QFilter qFilter = new QFilter("adminorgtypestd", "in", (Object)new Long[]{1010L, 1020L});
                beforeF7SelectEvent.getCustomQFilters().add(qFilter);
                break;
            }
            case "parent_changereason": 
            case "info_changereason": 
            case "disable_changereason": 
            case "merge_changereason": 
            case "split_changereason": {
                AdminOrgBatchChgHelper.setChangeReasonF7Filter((BeforeF7SelectEvent)beforeF7SelectEvent, (IDataModel)this.getModel(), (String)prefix, (Boolean)Boolean.TRUE);
                break;
            }
            case "parent_city": 
            case "info_city": {
                DynamicObject companyArea = ((DynamicObject)this.getModel().getDataEntity(true).getDynamicObjectCollection(entryName).get(rowIndex)).getDynamicObject(prefix + "_companyarea");
                if (companyArea != null && companyArea.getLong("id") != 0L) {
                    QFilter qFilter = new QFilter("country", "=", (Object)companyArea.getLong("id"));
                    ArrayList<QFilter> qFilters = new ArrayList<QFilter>();
                    qFilters.add(qFilter);
                    beforeF7SelectEvent.setCustomQFilters(qFilters);
                }
                beforeF7SelectEvent.getFormShowParameter().setCaption(ResManager.loadKDString((String)"\u6240\u5728\u57ce\u5e02", (String)"AdminorgBatchBillPlugin_12", (String)"odc-homs-formplugin", (Object[])new Object[0]));
                break;
            }
            case "info_corporateorg": 
            case "parent_corporateorg": {
                beforeF7SelectEvent.getCustomQFilters().add(new QFilter("oprsts", "=", (Object)"1"));
                break;
            }
            case "parent_org": 
            case "info_org": {
                HasPermOrgResult permOrgResult = PermissionServiceHelper.getUserHasPermOrgs((long)RequestContext.get().getCurrUserId(), (boolean)false);
                if (permOrgResult.hasAllOrgPerm()) break;
                List hasPermOrgs = permOrgResult.getHasPermOrgs();
                beforeF7SelectEvent.getCustomQFilters().add(new QFilter("id", "in", (Object)hasPermOrgs));
                break;
            }
        }
    }

    private boolean checkOrgValue(BeforeF7SelectEvent beforeF7SelectEvent, IDataEntityProperty property, IDataEntityType parent) {
        if (parent == null) {
            return false;
        }
        if (parent.getName().equals(this.getView().getEntityId())) {
            return false;
        }
        if (!this.getModel().getDataEntity().containsProperty(property.getParent().getName())) {
            return false;
        }
        DynamicObjectCollection dynamicObjectCollection = this.getModel().getDataEntity(true).getDynamicObjectCollection(parent.getName());
        DynamicObject dynamicObject = (DynamicObject)dynamicObjectCollection.get(beforeF7SelectEvent.getRow());
        String name = property.getName();
        String prefix = name.split("_")[0];
        String fieldKey = name.split("_")[1];
        if ("org".equals(fieldKey)) {
            return false;
        }
        if (!dynamicObject.getDataEntityType().getProperties().containsKey((Object)(prefix + '_' + "org"))) {
            return false;
        }
        Boolean baseDataCtrl = BaseDataServiceHelper.checkBaseDataCtrl((String)((BasedataProp)property).getBaseEntityId());
        DynamicObject orgDy = dynamicObject.getDynamicObject(prefix + '_' + "org");
        if (orgDy == null && baseDataCtrl.booleanValue()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u7ec4\u7ec7\u4f53\u7cfb\u7ba1\u7406\u7ec4\u7ec7\u3002", (String)"AdminorgFastChgPlugin_31", (String)"odc-homs-formplugin", (Object[])new Object[0]));
            beforeF7SelectEvent.setCancel(true);
            return true;
        }
        return false;
    }

    public static boolean shouldExclude(BeforeF7SelectEvent event) {
        return AdminOrgBatchChgHelper.mergeAndSplitFields.contains(event.getProperty().getName());
    }

    private final void excludeOrgIfNeed(BeforeF7SelectEvent beforeF7SelectEvent) {
        if (!AdminOrgBatchBillPlugin.shouldExclude(beforeF7SelectEvent)) {
            return;
        }
        String name = beforeF7SelectEvent.getProperty().getName();
        ArrayList ids = Lists.newArrayListWithCapacity((int)16);
        for (String entryName : AdminOrgBatchChgHelper.entryEntityName) {
            if (!entryName.equals(AdminOrgBatchChgHelper.mergeAndSplitFieldsExcludeMap.get(name))) continue;
            DynamicObjectCollection entryEntities = this.getModel().getDataEntity(true).getDynamicObjectCollection(entryName);
            for (DynamicObject dyn : entryEntities) {
                ids.add(dyn.getLong("entryentity_merge".equals(entryName) ? "merge_target_org.id" : "to_split_org.id"));
                ids.addAll(dyn.getDynamicObjectCollection("entryentity_merge".equals(entryName) ? "to_merge_org" : "split_target_org").stream().map(dy -> dy.getLong("fbasedataid.id")).collect(Collectors.toList()));
            }
        }
        if (ids.isEmpty()) {
            return;
        }
        QFilter orgIdFilter = new QFilter("id", "not in", (Object)ids);
        beforeF7SelectEvent.getCustomQFilters().add(orgIdFilter);
    }

    private void setFilterByOrg(BeforeF7SelectEvent beforeF7SelectEvent) {
        QFilter baseDataFilter = BaseDataHelper.getAdminOrgBaseDataFilter((String)"haos_adminorgdetail", Collections.singletonList(((DynamicObject)this.getModel().getValue("org")).getLong("id")));
        beforeF7SelectEvent.getCustomQFilters().add(baseDataFilter);
    }

    private void setFuzzySearchAuthFilter(BeforeF7SelectEvent beforeF7SelectEvent) {
        if ("getLookUpList".equals(beforeF7SelectEvent.getSourceMethod()) || "setItemByNumber".equals(beforeF7SelectEvent.getSourceMethod())) {
            AuthorizedOrgResult authorizedOrgResult = AdminOrgBatchChgHelper.getOrgAuth((IFormView)this.getView());
            if (authorizedOrgResult.isHasAllOrgPerm()) {
                return;
            }
            String fieldName = beforeF7SelectEvent.getProperty().getName();
            if ("parent_adminorg".equals(fieldName) || "info_adminorg".equals(fieldName) || "disable_adminorg".equals(fieldName) || "disorg".equals(fieldName)) {
                List hasPermOrgList = authorizedOrgResult.getHasPermOrgs();
                beforeF7SelectEvent.getCustomQFilters().add(new QFilter("boid", "in", (Object)hasPermOrgList));
            }
        }
    }

    private void replaceFormIdByFieldKey(BeforeF7SelectEvent beforeF7SelectEvent) {
        String fieldKey = beforeF7SelectEvent.getProperty().getName();
        if ("parent_adminorg".equals(fieldKey) || "info_adminorg".equals(fieldKey) || "disable_adminorg".equals(fieldKey) || "disorg".equals(fieldKey) || "to_split_org".equals(fieldKey) || "to_merge_org".equals(fieldKey)) {
            ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            showParameter.setFormId("haos_orgbatchtreelistf7");
        }
    }

    private static String getPrefixByFieldKey(String fieldKey) {
        if ("to_merge_org".equals(fieldKey)) {
            return "merge";
        }
        if ("to_split_org".equals(fieldKey)) {
            return "split";
        }
        int charIndex = fieldKey.indexOf(95);
        String prefix = "";
        if (charIndex != -1) {
            prefix = fieldKey.substring(0, charIndex);
        }
        return prefix;
    }

    private int getAllEntrySize() {
        DynamicObjectCollection addDyS = this.getModel().getEntryEntity("entryentity_add");
        DynamicObjectCollection parentDyS = this.getModel().getEntryEntity("entryentity_parent");
        DynamicObjectCollection infoDyS = this.getModel().getEntryEntity("entryentity_info");
        DynamicObjectCollection disDyS = this.getModel().getEntryEntity("entryentity_disable");
        return addDyS.size() + parentDyS.size() + infoDyS.size() + disDyS.size();
    }
}
