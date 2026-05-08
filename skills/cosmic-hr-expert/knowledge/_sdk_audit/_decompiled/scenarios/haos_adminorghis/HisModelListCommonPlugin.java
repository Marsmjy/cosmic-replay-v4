/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.filter.SortType
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeCreateListColumnsArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operatecol.OperationColItem
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.IListColumn
 *  kd.bos.list.ListColumn
 *  kd.bos.list.column.ListOperationColumnDesc
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.constants.history.HisModelTypeEnum
 *  kd.hr.hbp.common.constants.history.HisPageEnum
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hbp.formplugin.web.history.form.processor.HisModelDisableProcessor
 *  kd.hr.hbp.formplugin.web.history.list.processor.HisModelListProcessor
 *  kd.hr.hbp.formplugin.web.history.list.processor.HisModelListShowFormProcessor
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.formplugin.web.history.list;

import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.filter.SortType;
import kd.bos.form.CloseCallBack;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operatecol.OperationColItem;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.IListColumn;
import kd.bos.list.ListColumn;
import kd.bos.list.column.ListOperationColumnDesc;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.constants.history.HisModelTypeEnum;
import kd.hr.hbp.common.constants.history.HisPageEnum;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hbp.formplugin.web.history.form.processor.HisModelDisableProcessor;
import kd.hr.hbp.formplugin.web.history.list.processor.HisModelListProcessor;
import kd.hr.hbp.formplugin.web.history.list.processor.HisModelListShowFormProcessor;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public final class HisModelListCommonPlugin
extends HRDataBaseList
implements HisModelConstants {
    private static final Log LOGGER = LogFactory.getLog(HisModelListCommonPlugin.class);
    private final HisModelListProcessor listProcessor = new HisModelListProcessor((AbstractFormPlugin)this);
    private final HisModelListShowFormProcessor showFormProcessor = new HisModelListShowFormProcessor((AbstractFormPlugin)this);
    private final HisModelDisableProcessor disableProcessor = new HisModelDisableProcessor((AbstractFormPlugin)this);

    public void filterContainerInit(FilterContainerInitArgs args) {
        super.filterContainerInit(args);
        LOGGER.info("HisModelListCommonPlugin filterContainerInit start, entityNumber: {}, hisPage: {}", (Object)this.listProcessor.getEntityNumber(), (Object)this.listProcessor.getHisPageEnum());
        if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE || this.listProcessor.isF7() || this.listProcessor.isSkipFilterContainerInit()) {
            return;
        }
        this.listProcessor.initFilterContainer(args);
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        LOGGER.info("HisModelListCommonPlugin beforeBindData start, entityNumber: {}, hisPage: {}", (Object)this.listProcessor.getEntityNumber(), (Object)this.listProcessor.getHisPageEnum());
        if (this.listProcessor.isF7()) {
            return;
        }
        this.listProcessor.handleBtnVisible();
    }

    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        Set hisColumnKeys;
        super.beforeCreateListColumns(args);
        LOGGER.info("HisModelListCommonPlugin beforeCreateListColumns start, entityNumber: {}, hisPage: {}", (Object)this.listProcessor.getEntityNumber(), (Object)this.listProcessor.getHisPageEnum());
        if (this.listProcessor.getHisModelEntityConfig().getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            hisColumnKeys = Stream.of("bsed", "bsled", "firstbsed").collect(Collectors.toSet());
            args.getListColumns().removeIf(col -> hisColumnKeys.contains(col.getListFieldKey()));
        }
        if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE) {
            hisColumnKeys = Stream.of("hisversion", "changedescription", "datastatus", "bsed", "bsled", "hisoperation").collect(Collectors.toSet());
            args.getListColumns().removeIf(col -> hisColumnKeys.contains(col.getListFieldKey()));
        } else if (this.listProcessor.getHisPageEnum() == HisPageEnum.VERSION_LIST_PAGE) {
            int index;
            if (this.listProcessor.getHisModelEntityConfig().getModelType() == HisModelTypeEnum.NO_INTERRUPTION_NO_OVERLAP) {
                args.getListColumns().forEach(column -> column.setOrder(SortType.NotOrder.name()));
                args.getListColumns().stream().filter(col -> HRStringUtils.equals((String)col.getListFieldKey(), (String)"bsed")).findFirst().ifPresent(col -> col.setOrder(SortType.DESC.name()));
                args.getListColumns().stream().filter(col -> HRStringUtils.equals((String)col.getListFieldKey(), (String)"modifytime")).findFirst().ifPresent(col -> col.setOrder(SortType.DESC.name()));
            } else {
                args.getListColumns().forEach(column -> column.setOrder(SortType.NotOrder.name()));
                args.getListColumns().stream().filter(col -> HRStringUtils.equals((String)col.getListFieldKey(), (String)"hisversion")).findFirst().ifPresent(col -> col.setOrder(SortType.DESC.name()));
            }
            List removeCols = Stream.of("status", "firstbsed", "issyspreset").collect(Collectors.toList());
            args.getListColumns().removeIf(col -> removeCols.contains(col.getListFieldKey()));
            args.getListColumns().removeIf(col -> HRStringUtils.equals((String)col.getListFieldKey(), (String)"modifier.name") || HRStringUtils.equals((String)col.getListFieldKey(), (String)"modifytime"));
            ListColumn modifierCol = this.listProcessor.createListColumn("modifier.name", ResManager.loadKDString((String)"\u64cd\u4f5c\u4eba", (String)"HisModelListCommonPlugin_1", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]));
            ListColumn modifyTimeCol = this.listProcessor.createListColumn("modifytime", ResManager.loadKDString((String)"\u64cd\u4f5c\u65f6\u95f4", (String)"HisModelListCommonPlugin_2", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]));
            for (index = 0; index < args.getListColumns().size() && !"hisoperation".equals(((IListColumn)args.getListColumns().get(index)).getListFieldKey()); ++index) {
            }
            if (index > 0) {
                args.getListColumns().add(index, modifyTimeCol);
                args.getListColumns().add(index, modifierCol);
            }
        }
    }

    public void packageData(PackageDataEvent event) {
        if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE || this.listProcessor.isF7()) {
            super.packageData(event);
            return;
        }
        if (event.getSource() instanceof ListOperationColumnDesc && HRStringUtils.equals((String)event.getColKey(), (String)"hisoperation") && event.getRowData().containsProperty("datastatus")) {
            List operationColItems = (List)event.getFormatValue();
            String dataStatus = event.getRowData().getString("datastatus");
            if (HRStringUtils.equals((String)dataStatus, (String)HisModelDataStatusEnum.TEMP.getStatus())) {
                Iterator iterator = operationColItems.iterator();
                while (iterator.hasNext()) {
                    OperationColItem opItem;
                    opItem.setVisible("modify".equals((opItem = (OperationColItem)iterator.next()).getOperationKey()) || "hiscopy".equals(opItem.getOperationKey()) || "confirmchange".equals(opItem.getOperationKey()));
                }
            } else {
                Iterator iterator = operationColItems.iterator();
                while (iterator.hasNext()) {
                    OperationColItem opItem;
                    opItem.setVisible("hiscopy".equals((opItem = (OperationColItem)iterator.next()).getOperationKey()) || "revise".equals(opItem.getOperationKey()) && !HisModelDataStatusEnum.DISCARDED.getStatus().equals(dataStatus));
                }
            }
        }
        super.packageData(event);
    }

    public void setFilter(SetFilterEvent event) {
        super.setFilter(event);
        LOGGER.info("HisModelListCommonPlugin setFilter start, entityNumber: {}, hisPage: {}", (Object)this.listProcessor.getEntityNumber(), (Object)this.listProcessor.getHisPageEnum());
        if (this.listProcessor.isF7()) {
            return;
        }
        if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE) {
            QFilter currentDataFilter = new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE);
            event.getQFilters().add(currentDataFilter);
        } else {
            QFilter currentDataFilter = new QFilter("iscurrentversion", "=", (Object)Boolean.FALSE);
            event.getQFilters().add(currentDataFilter);
            event.getQFilters().removeIf(filter -> Objects.nonNull(filter) && HRStringUtils.equals((String)filter.getProperty(), (String)"ctrlstrategy"));
        }
    }

    public void beforeShowBill(BeforeShowBillFormEvent e) {
        super.beforeShowBill(e);
        LOGGER.info("HisModelListCommonPlugin beforeShowBill start, entityNumber: {}, hisPage: {}", (Object)this.listProcessor.getEntityNumber(), (Object)this.listProcessor.getHisPageEnum());
        if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE || this.listProcessor.isF7()) {
            return;
        }
        e.getParameter().setCustomParam("hisPage", (Object)HisPageEnum.VERSION_PAGE.getPage());
        e.getParameter().setCloseCallBack(new CloseCallBack((IFormPlugin)this, "closePageCloseCallBack"));
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        op.getOption().setVariableValue("fromListOperate", "true");
        switch (operateKey) {
            case "disable": {
                this.disableProcessor.showSetDisableDatePage(args);
                break;
            }
            case "confirmchange": {
                break;
            }
            case "importdata": {
                if (this.listProcessor.getHisModelEntityConfig().getModelType() != HisModelTypeEnum.NO_INTERRUPTION_NO_OVERLAP) break;
                args.setCancel(true);
                this.listProcessor.openHisImportStartPage();
                break;
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        String operateKey;
        super.afterDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "newhisversion": {
                this.showFormProcessor.showAddHisVersionPage();
                break;
            }
            case "hiscopy": {
                this.showFormProcessor.showCopyHisVersionPage(this.getFocusRowPkId());
                break;
            }
            case "revise": {
                this.showFormProcessor.showHisVersionEditPage(this.getFocusRowPkId());
                break;
            }
            case "versionchangecompare": {
                if (!args.getOperationResult().isSuccess()) {
                    return;
                }
                Set selectedRowIds = this.getSelectedRows().stream().map(ListSelectedRow::getPrimaryKeyValue).collect(Collectors.toSet());
                this.showFormProcessor.showVersionChangeCompareList(selectedRowIds);
                break;
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        String actionId = evt.getActionId();
        Object returnDate = evt.getReturnData();
        this.disableProcessor.closeCallBack(actionId, returnDate);
        switch (actionId) {
            case "closeForCopyHisVersion": 
            case "closeForNewHisVersion": {
                this.getView().invokeOperation("refresh");
            }
        }
    }
}
