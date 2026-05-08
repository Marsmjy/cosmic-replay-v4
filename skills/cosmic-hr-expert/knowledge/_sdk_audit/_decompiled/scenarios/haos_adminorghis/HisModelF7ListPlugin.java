/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.datamodel.events.BeforePackageDataEvent
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.events.BeforeCreateListColumnsArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.service.history.util.HisModelCommonUtil
 *  kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.common.constants.history.HisModelTypeEnum
 *  kd.hr.hbp.common.model.history.HisModelF7PageParam
 *  kd.hr.hbp.common.util.DatePattern
 *  kd.hr.hbp.common.util.DateUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hbp.formplugin.web.history.list.processor.HisModelBUF7Processor
 *  kd.hr.hbp.formplugin.web.history.list.processor.HisModelF7Processor
 *  kd.hr.hbp.formplugin.web.history.list.processor.HisModelFilterPanelF7Processor
 *  kd.hr.hbp.formplugin.web.history.list.processor.HisModelListProcessor
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.formplugin.web.history.list;

import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.events.BeforePackageDataEvent;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.service.history.util.HisModelCommonUtil;
import kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.constants.history.HisModelTypeEnum;
import kd.hr.hbp.common.model.history.HisModelF7PageParam;
import kd.hr.hbp.common.util.DatePattern;
import kd.hr.hbp.common.util.DateUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hbp.formplugin.web.history.list.processor.HisModelBUF7Processor;
import kd.hr.hbp.formplugin.web.history.list.processor.HisModelF7Processor;
import kd.hr.hbp.formplugin.web.history.list.processor.HisModelFilterPanelF7Processor;
import kd.hr.hbp.formplugin.web.history.list.processor.HisModelListProcessor;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public final class HisModelF7ListPlugin
extends HRDataBaseList
implements HisModelConstants {
    public static final String F7_EFFDATE_START = "f7effdatestart";
    public static final String F7_EFFDATE_END = "f7effdateend";
    private final HisModelF7Processor f7Processor = new HisModelF7Processor((AbstractFormPlugin)this);
    private final HisModelBUF7Processor buF7Processor = new HisModelBUF7Processor((AbstractFormPlugin)this);
    private final HisModelListProcessor listProcessor = new HisModelListProcessor((AbstractFormPlugin)this);
    private final HisModelFilterPanelF7Processor filterPanelF7Processor = new HisModelFilterPanelF7Processor((AbstractFormPlugin)this);
    private Map<Object, DynamicObject> currentDataMap;

    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        super.beforeCreateListColumns(args);
        if (!this.listProcessor.isF7() || !this.f7Processor.isUseHisF7Tpl() || this.filterPanelF7Processor.isHisFieldF7() || this.listProcessor.getHisModelEntityConfig().getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            return;
        }
        this.f7Processor.createVersionF7Column(args, false);
        this.f7Processor.setFirstOpenF7Page();
    }

    public void setFilter(SetFilterEvent evt) {
        List<QFilter> currentQFilters;
        super.setFilter(evt);
        if (!this.listProcessor.isF7() || !this.f7Processor.isUseHisF7Tpl()) {
            return;
        }
        this.f7Processor.handelControlVisible();
        if (this.filterPanelF7Processor.isHisFieldF7()) {
            return;
        }
        if (!this.f7Processor.isBoF7()) {
            this.buF7Processor.changeHisListBdQFilter(this.f7Processor.getEntityNumber(), evt.getQFilters());
            this.buF7Processor.changeHisListBdQFilter(this.f7Processor.getEntityNumber(), evt.getBasedataCoreQFilters());
        }
        if (!this.f7Processor.isBoF7() && this.f7Processor.getHisModelF7PageParam().getShowCurrentNumAndName().booleanValue() && !(currentQFilters = evt.getQFilters().stream().filter(qFilter -> qFilter != null && "ftlike".equals(qFilter.getCP())).collect(Collectors.toList())).isEmpty()) {
            HRBaseServiceHelper helper = new HRBaseServiceHelper(this.f7Processor.getEntityNumber());
            QFilter versionQFilter = new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE);
            currentQFilters.add(versionQFilter);
            DynamicObjectCollection boCol = helper.queryOriginalCollection("id", currentQFilters.toArray(new QFilter[0]));
            QFilter newSearchFilter = new QFilter("boid", "in", boCol.stream().map(bo -> bo.get("id")).collect(Collectors.toSet()));
            currentQFilters.forEach(qFilter -> qFilter.or(newSearchFilter));
        }
        if (this.listProcessor.getHisModelEntityConfig().getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            evt.getQFilters().add(new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE));
        } else {
            if (this.f7Processor.isBoF7()) {
                evt.getQFilters().add(new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE));
            } else {
                this.f7Processor.handleSetVersionF7QFilters(evt);
            }
            this.f7Processor.handleEnableAndStatusQFilters(evt);
        }
    }

    public void beforePackageData(BeforePackageDataEvent event) {
        super.beforePackageData(event);
        if (!this.listProcessor.isF7() || !this.f7Processor.isUseHisF7Tpl() || this.filterPanelF7Processor.isVersionF7Filter()) {
            return;
        }
        if (this.f7Processor.isBoF7() || this.listProcessor.getHisModelEntityConfig().getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            return;
        }
        if (this.f7Processor.getHisModelF7PageParam().getShowCurrentNumAndName().booleanValue()) {
            this.currentDataMap = this.f7Processor.queryCurrentNumberAndName(event);
        }
    }

    public void packageData(PackageDataEvent e) {
        super.packageData(e);
        if (!this.listProcessor.isF7() || !this.f7Processor.isUseHisF7Tpl() || this.filterPanelF7Processor.isVersionF7Filter()) {
            return;
        }
        if (this.f7Processor.isBoF7() || this.listProcessor.getHisModelEntityConfig().getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            return;
        }
        if (this.f7Processor.getHisModelF7PageParam().getShowCurrentNumAndName().booleanValue()) {
            this.f7Processor.setCurrentNumberAndCurrentName(e, this.currentDataMap);
        }
    }

    public void propertyChanged(PropertyChangedArgs propertyChangedArgs) {
        if (!this.listProcessor.isF7() || !this.f7Processor.isUseHisF7Tpl()) {
            return;
        }
        String fieldKey = propertyChangedArgs.getProperty().getName();
        ChangeData[] changeSet = propertyChangedArgs.getChangeSet();
        if (HRStringUtils.equals((String)"effectdate", (String)fieldKey)) {
            this.f7Processor.getChangeDateMap().put("changeDate", (Date)changeSet[0].getNewValue());
            ListView listView = (ListView)this.getView();
            listView.refresh();
            listView.clearSelection();
        } else if (HRStringUtils.equals((String)"effdatestart", (String)fieldKey)) {
            this.f7Processor.getChangeDateMap().put("changeDateStart", (Date)changeSet[0].getNewValue());
            ListView listView = (ListView)this.getView();
            listView.refresh();
            listView.clearSelection();
        } else if (HRStringUtils.equals((String)"effdateend", (String)fieldKey)) {
            this.f7Processor.getChangeDateMap().put("changeDateEnd", (Date)changeSet[0].getNewValue());
            ListView listView = (ListView)this.getView();
            listView.refresh();
            listView.clearSelection();
        } else if (HRStringUtils.equals((String)F7_EFFDATE_START, (String)fieldKey)) {
            this.f7Processor.getChangeDateMap().put(F7_EFFDATE_START, (Date)changeSet[0].getNewValue());
            if (changeSet[0].getNewValue() == null) {
                this.getPageCache().put(F7_EFFDATE_START, "null");
            } else {
                this.getPageCache().put(F7_EFFDATE_START, DateUtils.dateToString((Date)((Date)changeSet[0].getNewValue()), (DatePattern)DatePattern.YYYY_MM_DD));
            }
            ListView listView = (ListView)this.getView();
            listView.refresh();
        } else if (HRStringUtils.equals((String)F7_EFFDATE_END, (String)fieldKey)) {
            this.f7Processor.getChangeDateMap().put(F7_EFFDATE_END, (Date)changeSet[0].getNewValue());
            if (changeSet[0].getNewValue() == null) {
                this.getPageCache().put(F7_EFFDATE_END, "null");
            } else {
                this.getPageCache().put(F7_EFFDATE_END, DateUtils.dateToString((Date)((Date)changeSet[0].getNewValue()), (DatePattern)DatePattern.YYYY_MM_DD));
            }
            ListView listView = (ListView)this.getView();
            listView.refresh();
        }
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        if (!this.listProcessor.isF7() || !this.f7Processor.isUseHisF7Tpl()) {
            return;
        }
        if (this.f7Processor.isBoF7() || this.listProcessor.getHisModelEntityConfig().getModelType() == HisModelTypeEnum.ONLY_ONE_EFFECT_VERSION) {
            return;
        }
        if (!this.f7Processor.isFirstOpenF7Page()) {
            return;
        }
        HisModelF7PageParam hisModelF7PageParam = this.f7Processor.getHisModelF7PageParam();
        if (!hisModelF7PageParam.getShowEffDateControl().booleanValue()) {
            return;
        }
        if ("effDate".equals(hisModelF7PageParam.getEffDateFieldType()) && hisModelF7PageParam.getEffectDate() == null) {
            this.getModel().setValue("effectdate", (Object)HisModelCommonUtil.getToday());
        } else if ("effDateRange".equals(hisModelF7PageParam.getEffDateFieldType()) && hisModelF7PageParam.getEffDateStart() == null && hisModelF7PageParam.getEffDateEnd() == null) {
            this.getModel().setValue("effdatestart", (Object)HisModelCommonUtil.getToday());
            this.getModel().setValue("effdateend", (Object)TimeLineServiceUtil.getMaxEffEndDate());
        }
    }
}
