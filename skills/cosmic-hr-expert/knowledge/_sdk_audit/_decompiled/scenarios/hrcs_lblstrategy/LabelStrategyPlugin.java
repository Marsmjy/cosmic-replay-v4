/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.ext.hr.filter.control.HRFilter
 *  kd.bos.ext.hr.filter.control.HRFilterAp
 *  kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo
 *  kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.StyleCss
 *  kd.bos.form.container.Tab
 *  kd.bos.form.container.TabPage
 *  kd.bos.form.control.Button
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.Label
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.control.events.TabSelectEvent
 *  kd.bos.form.control.events.TabSelectListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.OnGetControlArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.field.TextEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.metadata.form.Border
 *  kd.bos.metadata.form.Margin
 *  kd.bos.metadata.form.Padding
 *  kd.bos.metadata.form.Style
 *  kd.bos.metadata.form.container.FlexPanelAp
 *  kd.bos.metadata.form.container.TabPageAp
 *  kd.bos.metadata.form.control.LabelAp
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.service.labelandreport.AnobjFilterUtil
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hrcs.bussiness.service.label.LabelDataService
 *  kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper
 *  kd.hr.hrcs.bussiness.service.label.LabelService
 *  kd.hr.hrcs.bussiness.service.label.LabelTaskStorageService
 *  kd.hr.hrcs.bussiness.service.label.LblStrategyServiceHelper
 *  kd.hr.hrcs.bussiness.util.GenFieldUtil
 *  kd.hr.hrcs.common.constants.label.LabelConstants
 *  kd.hr.hrcs.common.constants.label.LblStrategyConstants
 *  kd.hr.hrcs.formplugin.web.label.LabelDataEntryGrid
 */
package kd.hr.hrcs.formplugin.web.label;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.ext.hr.filter.control.HRFilter;
import kd.bos.ext.hr.filter.control.HRFilterAp;
import kd.bos.ext.hr.ruleengine.infos.RuleValidateInfo;
import kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.StyleCss;
import kd.bos.form.container.Tab;
import kd.bos.form.container.TabPage;
import kd.bos.form.control.Button;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Label;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.control.events.TabSelectEvent;
import kd.bos.form.control.events.TabSelectListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.OnGetControlArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.TextEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.metadata.form.Border;
import kd.bos.metadata.form.Margin;
import kd.bos.metadata.form.Padding;
import kd.bos.metadata.form.Style;
import kd.bos.metadata.form.container.FlexPanelAp;
import kd.bos.metadata.form.container.TabPageAp;
import kd.bos.metadata.form.control.LabelAp;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.service.labelandreport.AnobjFilterUtil;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hrcs.bussiness.service.label.LabelDataService;
import kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper;
import kd.hr.hrcs.bussiness.service.label.LabelService;
import kd.hr.hrcs.bussiness.service.label.LabelTaskStorageService;
import kd.hr.hrcs.bussiness.service.label.LblStrategyServiceHelper;
import kd.hr.hrcs.bussiness.util.GenFieldUtil;
import kd.hr.hrcs.common.constants.label.LabelConstants;
import kd.hr.hrcs.common.constants.label.LblStrategyConstants;
import kd.hr.hrcs.formplugin.web.label.LabelDataEntryGrid;

public final class LabelStrategyPlugin
extends HRBaseDataCommonEdit
implements BeforeF7SelectListener,
TabSelectListener,
LabelConstants,
LblStrategyConstants {
    private static final String NEXT_ONE = "nextone";
    private static final String LAST_ONE = "lastone";
    private static final String TAB_AP = "tabap";
    private static final String BAR_SAVE = "bar_save";
    private static final String KEY_LABEL_OBJECT = "labelobject";
    private static final String KEY_LABEL = "label";
    private static final String KEY_TAB_PAGE = "tabPageKey";
    private static final String KEY_TAB_PAGE_HAND = "handTabPageKey";
    private static final String IS_INIT_HAND = "isInitHand";
    private static final String KEY_PAGE_CACHE_FILTER = "pageCacheFilter";
    private static final String KEY_LABEL_VALUE_ID = "labelValueId";
    private static final String FREQUENCY = "customcontrolap";
    private static final String KEY_IMPORT = "import";
    private static final String KEY_IMPORT_FINISH = "importFinish";
    private static final String KEY_DB_BIZ_ID = "dbBizId";
    private static final Pattern compile = Pattern.compile("^[a-zA-Z0-9_.]+$");
    private static final Log LOGGER = LogFactory.getLog(LabelStrategyPlugin.class);
    private final LabelService labelService = new LabelService();

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        Toolbar toolbar = (Toolbar)this.getControl("tbmain");
        toolbar.addItemClickListener((ItemClickListener)this);
        BasedataEdit basedataEdit = (BasedataEdit)this.getControl(KEY_LABEL_OBJECT);
        basedataEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        Button button = (Button)this.getControl(KEY_IMPORT);
        button.addClickListener((ClickListener)this);
        Tab tab = (Tab)this.getControl("tabap2");
        tab.addTabSelectListener((TabSelectListener)this);
    }

    public void afterCreateNewData(EventObject e) {
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (OperationStatus.ADDNEW.equals((Object)status)) {
            this.getModel().setValue("startdate", (Object)new Date());
            LocalDate of = LocalDate.of(2099, 12, 31);
            this.getModel().setValue("enddate", (Object)of);
        }
    }

    public void afterLoadData(EventObject e) {
        String workType;
        BillOperationStatus billStatus = ((BaseShowParameter)this.getView().getFormShowParameter()).getBillStatus();
        if (BillOperationStatus.VIEW.equals((Object)billStatus) || BillOperationStatus.SUBMIT.equals((Object)billStatus) || BillOperationStatus.AUDIT.equals((Object)billStatus)) {
            this.getPageCache().put("pageState", "VIEW");
        }
        if ("0".equals(workType = (String)this.getModel().getValue("worktype"))) {
            this.loadByRuleWorkType();
            this.getPageCache().put("configtype", (String)this.getModel().getValue("configtype"));
        } else {
            this.setLockByEnableAndDate();
        }
        this.getPageCache().put("frequency", (String)this.getModel().getValue("frequency"));
    }

    public void beforeBindData(EventObject e) {
        boolean isHandWorkType;
        super.beforeBindData(e);
        DynamicObject label = (DynamicObject)this.getModel().getValue(KEY_LABEL);
        if (label == null) {
            this.getView().setEnable(Boolean.valueOf(false), new String[]{KEY_LABEL_OBJECT});
        }
        this.setFrequency();
        DynamicObject lblObj = (DynamicObject)this.getModel().getValue(KEY_LABEL_OBJECT);
        String workType = (String)this.getModel().getValue("worktype");
        boolean bl = isHandWorkType = !"0".equals(workType);
        if (lblObj == null) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap4"});
            this.getView().setEnable(Boolean.valueOf(false), new String[]{"worktype"});
        } else {
            DynamicObject lblObjConfig = LabelObjectServiceHelper.getLblObjConfig((Long)lblObj.getLong("id"));
            boolean isHand = lblObjConfig.getBoolean("manuallabel");
            assert (label != null);
            String type = label.getString("type");
            boolean isFactLabel = "20".equals(type);
            this.setHand(isHand, isFactLabel);
            this.setVisibleByWorkType(isHandWorkType);
            this.setLabelObjectDataFilter(lblObj);
            this.setFieldCaption(type);
        }
        String lock = this.getPageCache().get("isLock");
        if (HRStringUtils.isEmpty((String)lock)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap6"});
        }
        this.updateTabName(isHandWorkType);
        if (!isHandWorkType) {
            boolean isEasy = !"2".equals(this.getModel().getValue("configtype"));
            this.setVisibleByRadio(isEasy);
        }
    }

    public void click(EventObject evt) {
        super.click(evt);
        String key = ((Control)evt.getSource()).getKey();
        if (KEY_IMPORT.equals(key)) {
            FormShowParameter showParameter = new FormShowParameter();
            showParameter.setFormId("hrcs_labelimport");
            showParameter.getOpenStyle().setShowType(ShowType.Modal);
            DynamicObject label = (DynamicObject)this.getModel().getValue(KEY_LABEL);
            DynamicObject lblObj = (DynamicObject)this.getModel().getValue(KEY_LABEL_OBJECT);
            showParameter.setCustomParam(KEY_LABEL, (Object)(label.getLong("id") + ""));
            showParameter.setCustomParam(KEY_LABEL_OBJECT, (Object)(lblObj.getLong("id") + ""));
            Map<String, Set<String>> dbData = this.getDBData();
            showParameter.setCustomParam("labelData", (Object)SerializationUtils.toJsonString(dbData));
            showParameter.setCustomParam("labelObjFilter", (Object)this.getPageCache().get("labelObjFilter"));
            showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, KEY_IMPORT_FINISH));
            this.getView().showForm(showParameter);
        }
    }

    public void tabSelected(TabSelectEvent tabSelectEvent) {
        if (tabSelectEvent.getTabKey().startsWith("tabpagehand")) {
            String tabSelectKey = this.getPageCache().get("tabSelect");
            Set tabSelectedSet = !HRStringUtils.isEmpty((String)tabSelectKey) ? (Set)SerializationUtils.fromJsonString((String)tabSelectKey, Set.class) : Sets.newHashSetWithExpectedSize((int)8);
            tabSelectedSet.add(tabSelectEvent.getTabKey());
            this.getPageCache().put("tabSelect", SerializationUtils.toJsonString((Object)tabSelectedSet));
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        if (KEY_IMPORT_FINISH.equals(closedCallBackEvent.getActionId()) && closedCallBackEvent.getReturnData() != null) {
            this.importCloseCallBack(closedCallBackEvent);
        }
    }

    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        String itemKey = evt.getItemKey();
        if (LAST_ONE.equals(itemKey)) {
            this.previous();
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        String name = e.getProperty().getName();
        ChangeData changeData = e.getChangeSet()[0];
        if (KEY_LABEL_OBJECT.equals(name)) {
            DynamicObject newValue = (DynamicObject)changeData.getNewValue();
            boolean isHand = false;
            boolean isFactLabel = false;
            if (newValue != null) {
                this.getModel().initValue("configtype", (Object)"1");
                this.setVisibleByRadio(true);
                DynamicObject lblObjConfig = LabelObjectServiceHelper.getLblObjConfig((Long)newValue.getLong("id"));
                isHand = lblObjConfig.getBoolean("manuallabel");
                this.getView().setEnable(Boolean.valueOf(true), new String[]{"worktype"});
                DynamicObject label = (DynamicObject)this.getModel().getValue(KEY_LABEL);
                String type = label.getString("type");
                isFactLabel = "20".equals(type);
                this.loadPageByRule();
                String workType = (String)this.getModel().getValue("worktype");
                if ("0".equals(workType)) {
                    this.setVisibleByWorkType(false);
                } else {
                    this.setVisibleByWorkType(isHand);
                }
                if (!isHand) {
                    this.getModel().setValue("worktype", (Object)"0");
                }
                this.setLabelObjectDataFilter(newValue);
                if (this.labelService.validateDuplicate(Long.valueOf(label.getLong("id")), Long.valueOf(newValue.getLong("id")))) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u6807\u7b7e\u5df2\u5728\u6240\u9009\u6253\u6807\u5bf9\u8c61\u4e0b\u5b58\u5728\u6253\u6807\u7b56\u7565\uff0c\u8bf7\u52ff\u91cd\u590d\u521b\u5efa\u3002", (String)"LabelStrategyPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                }
            } else {
                this.getView().setEnable(Boolean.valueOf(false), new String[]{"worktype"});
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap4"});
                this.getModel().setValue("worktype", (Object)"0");
            }
            this.getPageCache().put(IS_INIT_HAND, null);
            this.getPageCache().put("isLoadEasy", null);
            this.getPageCache().put("isLoadPlugin", null);
            this.updateTabName(false);
            this.setHand(isHand, isFactLabel);
        } else if (KEY_LABEL.equals(name)) {
            this.getModel().setValue(KEY_LABEL_OBJECT, null);
            this.getView().setEnable(Boolean.valueOf(changeData.getNewValue() != null), new String[]{KEY_LABEL_OBJECT});
            this.getPageCache().put("dataPageCache", null);
            this.getPageCache().put(KEY_LABEL_VALUE_ID, null);
            DynamicObject newValue = (DynamicObject)changeData.getNewValue();
            if (newValue != null) {
                this.setFieldCaption(newValue.getString("type"));
            }
        } else if (HRStringUtils.equals((String)"ruledate", (String)name)) {
            Date newData = (Date)changeData.getNewValue();
            if (null == newData) {
                return;
            }
            String filterKey = this.getPageCache().get("hrFilterKey");
            Map filterKeyMap = (Map)SerializationUtils.fromJsonString((String)filterKey, Map.class);
            String dateFormat = this.getView().getPageCache().get("ruleDateFormat");
            String date = HRDateTimeUtils.format((Date)newData, (String)dateFormat);
            filterKeyMap.forEach((key, value) -> {
                HRFilter hrFilter = (HRFilter)this.getView().getControl(key);
                hrFilter.setDate(date);
            });
            this.getModel().setValue("ruledate", null);
        } else if ("worktype".equals(name)) {
            String newValue = (String)changeData.getNewValue();
            if ("0".equals(newValue)) {
                Object value2 = this.getModel().getValue(KEY_LABEL_OBJECT);
                if (value2 != null) {
                    this.setVisibleByWorkType(false);
                }
                this.updateTabName(false);
            } else if ("1".equals(newValue)) {
                this.setVisibleByWorkType(true);
                this.updateTabName(true);
            } else {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap4", FREQUENCY});
            }
        } else if ("enddate".equals(name)) {
            this.setLockByEnableAndDate();
        } else if ("configtype".equals(name)) {
            String caption;
            boolean isEmpty = true;
            if ("1".equals(changeData.getNewValue())) {
                DynamicObjectCollection col = this.getModel().getEntryEntity("entryentitydisplay");
                for (DynamicObject row : col) {
                    String service = row.getString("servicedisplay");
                    if (HRStringUtils.isEmpty((String)service)) continue;
                    isEmpty = false;
                    break;
                }
                caption = ResManager.loadKDString((String)"\u7b80\u5355\u6a21\u5f0f", (String)"LabelStrategyPlugin_21", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            } else {
                String hrFilterValue = this.getPageCache().get("hrFilterKey");
                Map hrFilterValueMap = (Map)SerializationUtils.fromJsonString((String)hrFilterValue, Map.class);
                for (Map.Entry entry : hrFilterValueMap.entrySet()) {
                    HRFilter hrFilter = (HRFilter)this.getControl((String)entry.getKey());
                    String newValue = hrFilter.getValue(true);
                    if (HRStringUtils.isEmpty((String)newValue)) continue;
                    isEmpty = false;
                    break;
                }
                caption = ResManager.loadKDString((String)"\u63d2\u4ef6\u6a21\u5f0f", (String)"LabelStrategyPlugin_22", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            }
            if (isEmpty) {
                this.configTypeConfirmCallBack();
            } else {
                this.getView().showConfirm(ResManager.loadKDString((String)"\u9009\u4e2d\u201c%s\u201d\u540e\uff0c\u5c06\u6e05\u7a7a\u5df2\u914d\u7f6e\u7684\u6253\u6807\u89c4\u5219\u3002\u786e\u5b9a\u6267\u884c\u6b64\u64cd\u4f5c\u5417\uff1f", (String)"LabelStrategyPlugin_20", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{caption}), MessageBoxOptions.YesNo, new ConfirmCallBackListener("configtype"));
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String name = beforeF7SelectEvent.getProperty().getName();
        if (KEY_LABEL_OBJECT.equals(name)) {
            DynamicObject label = (DynamicObject)this.getModel().getValue(KEY_LABEL);
            ListShowParameter formShowParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            if (label != null) {
                DynamicObject[] lblObjects = LabelObjectServiceHelper.getLblObjectByLabel((Long)label.getLong("id"));
                ArrayList lblObjId = Lists.newArrayListWithExpectedSize((int)lblObjects.length);
                for (DynamicObject lblObject : lblObjects) {
                    lblObjId.add(lblObject.getLong("labelobject.id"));
                }
                formShowParameter.getListFilterParameter().getQFilters().add(new QFilter("id", "in", (Object)lblObjId));
            } else {
                formShowParameter.getListFilterParameter().getQFilters().add(new QFilter("id", "=", (Object)0L));
            }
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        Tab tab;
        String currentTab;
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if ("save".equals(formOperate.getOperateKey())) {
            String workType = (String)this.getModel().getValue("worktype");
            if ("0".equals(workType)) {
                if (!this.beforeSaveWithRule(formOperate)) {
                    args.setCancel(true);
                    return;
                }
            } else {
                this.beforeSaveWithHand(formOperate);
            }
            Date endDate = (Date)this.getModel().getValue("enddate");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.set(11, 23);
            calendar.set(12, 59);
            calendar.set(13, 59);
            this.getModel().setValue("enddate", (Object)calendar.getTime());
        } else if (NEXT_ONE.equals(formOperate.getOperateKey()) && "tabpageap".equals(currentTab = (tab = (Tab)this.getView().getControl(TAB_AP)).getCurrentTab())) {
            DynamicObject lbl;
            long lblId;
            if (!this.validateMustInput()) {
                args.setCancel(true);
                return;
            }
            Long id = (Long)this.getModel().getValue("id");
            DynamicObject lblObj = (DynamicObject)this.getModel().getValue(KEY_LABEL_OBJECT);
            long lblObjId = lblObj.getLong("id");
            if ((id == null || id == 0L) && this.labelService.validateDuplicate(Long.valueOf(lblId = (lbl = (DynamicObject)this.getModel().getValue(KEY_LABEL)).getLong("id")), Long.valueOf(lblObjId))) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u5f53\u524d\u6807\u7b7e\u5df2\u5728\u6240\u9009\u6253\u6807\u5bf9\u8c61\u4e0b\u5b58\u5728\u6253\u6807\u7b56\u7565\uff0c\u8bf7\u52ff\u91cd\u590d\u521b\u5efa\u3002", (String)"LabelStrategyPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
            }
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        String callBackId = evt.getCallBackId();
        if ("configtype".equals(callBackId)) {
            if (evt.getResult() == MessageBoxResult.Yes) {
                this.configTypeConfirmCallBack();
            } else {
                String value = (String)this.getModel().getValue("configtype");
                String reverseValue = "1".equals(value) ? "2" : "1";
                DynamicObject dataEntity = this.getModel().getDataEntity();
                dataEntity.set("configtype", (Object)reverseValue);
                this.getView().updateView("configtype");
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        if (NEXT_ONE.equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            this.next();
            if (!HRBaseDataConfigUtil.getAudit((String)"hrcs_lblstrategy")) {
                this.getModel().setValue("status", (Object)"A");
            }
        } else if ("closetips".equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap6"});
        } else if (LAST_ONE.equals(afterDoOperationEventArgs.getOperateKey()) && afterDoOperationEventArgs.getOperationResult().isSuccess() && !HRBaseDataConfigUtil.getAudit((String)"hrcs_lblstrategy")) {
            this.getModel().setValue("status", (Object)"A");
        }
    }

    public void onGetControl(OnGetControlArgs e) {
        Map hrFilterValueMap;
        super.onGetControl(e);
        String hrFilterKey = this.getPageCache().get("hrFilterKey");
        if (!HRStringUtils.isEmpty((String)hrFilterKey) && (hrFilterValueMap = (Map)SerializationUtils.fromJsonString((String)hrFilterKey, Map.class)).containsKey(e.getKey())) {
            HRFilterAp hrFilterAp = new HRFilterAp();
            hrFilterAp.setKey(e.getKey());
            HRFilter control = (HRFilter)hrFilterAp.buildRuntimeControl();
            control.setView(this.getView());
            control.setModel(this.getModel());
            e.setControl((Control)control);
        }
    }

    public void customEvent(CustomEventArgs e) {
        if ("targetMenuOnchange".equals(e.getEventName())) {
            JSONArray jsonArray = JSONArray.parseArray((String)e.getEventArgs());
            String value = (String)(jsonArray.size() > 1 ? jsonArray.get(1) : jsonArray.get(0));
            this.getModel().setValue("frequency", (Object)value);
        }
    }

    protected List<String> getUnCheckField() {
        List unCheckField = super.getUnCheckField();
        unCheckField.add("entryentitydisplay");
        return unCheckField;
    }

    private boolean beforeSaveWithRule(FormOperate formOperate) {
        String oldConfigType;
        String configType = (String)this.getModel().getValue("configtype");
        if ("2".equals(configType) && "1".equals(oldConfigType = this.getPageCache().get("configtype"))) {
            formOperate.getOption().setVariableValue("isTypeChanged", Boolean.TRUE.toString());
        }
        if ("1".equals(configType)) {
            if (!this.checkRule()) {
                return false;
            }
            String hrFilterValue = this.getPageCache().get("hrFilterKey");
            Map hrFilterValueMap = (Map)SerializationUtils.fromJsonString((String)hrFilterValue, Map.class);
            HashMap newFilterValueMap = Maps.newHashMapWithExpectedSize((int)hrFilterValueMap.size());
            hrFilterValueMap.forEach((key, value) -> {
                HRFilter hrFilter = (HRFilter)this.getControl((String)key);
                String newValue = hrFilter.getValue(true);
                newFilterValueMap.put(key, newValue);
            });
            formOperate.getOption().setVariableValue("hrFilterKey", SerializationUtils.toJsonString((Object)newFilterValueMap));
        } else {
            String join;
            String name;
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentitydisplay");
            ArrayList matcherList = Lists.newArrayListWithExpectedSize((int)entryEntity.size());
            ArrayList emptyList = Lists.newArrayListWithExpectedSize((int)entryEntity.size());
            ArrayList serviceErrorList = Lists.newArrayListWithExpectedSize((int)entryEntity.size());
            HashMap appNumMap = Maps.newHashMapWithExpectedSize((int)entryEntity.size());
            for (DynamicObject entry : entryEntity) {
                String serviceDis = entry.getString("servicedisplay");
                String labelValue = entry.getString("labelvaluedisplay.value");
                String string = ResManager.loadKDString((String)"\u201c%s\u201d", (String)"LabelStrategyPlugin_28", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{labelValue});
                if (HRStringUtils.isEmpty((String)serviceDis)) {
                    emptyList.add(string);
                    continue;
                }
                Matcher matcher = compile.matcher(serviceDis);
                if (!matcher.find()) {
                    matcherList.add(string);
                    continue;
                }
                int index = serviceDis.indexOf(".");
                if (index == -1) {
                    serviceErrorList.add(string);
                    continue;
                }
                appNumMap.put(labelValue, serviceDis.substring(0, index));
            }
            ArrayList errMsgList = Lists.newArrayListWithExpectedSize((int)2);
            if (!emptyList.isEmpty()) {
                name = ResManager.loadKDString((String)"\u3001", (String)"LabelStrategyPlugin_24", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                join = String.join((CharSequence)name, emptyList);
                errMsgList.add(ResManager.loadKDString((String)"\u8bf7\u914d\u7f6e%s\u6253\u6807\u89c4\u5219\u3002", (String)"LabelStrategyPlugin_25", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{join}));
            }
            if (!matcherList.isEmpty()) {
                name = ResManager.loadKDString((String)"\u3001", (String)"LabelStrategyPlugin_24", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                join = String.join((CharSequence)name, matcherList);
                errMsgList.add(ResManager.loadKDString((String)"%s\u6253\u6807\u89c4\u5219\u670d\u52a1\u7c7b\u683c\u5f0f\u9519\u8bef\uff0c\u4ec5\u652f\u6301\u8f93\u5165\u82f1\u6587\u3001\u6570\u5b57\u3001\u201c_\u201d\u6216\u201c.\u201d\u3002", (String)"LabelStrategyPlugin_26", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{join}));
            }
            DynamicObject[] bizApps = LblStrategyServiceHelper.getBizApps((Set)Sets.newHashSet(appNumMap.values()));
            Set appNumSet = Arrays.stream(bizApps).map(ba -> ba.getString("number")).collect(Collectors.toSet());
            for (Map.Entry entry : appNumMap.entrySet()) {
                if (appNumSet.contains(entry.getValue())) continue;
                String langLabelValue = ResManager.loadKDString((String)"\u201c%s\u201d", (String)"LabelStrategyPlugin_28", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{entry.getKey()});
                serviceErrorList.add(langLabelValue);
            }
            if (!serviceErrorList.isEmpty()) {
                String name2 = ResManager.loadKDString((String)"\u3001", (String)"LabelStrategyPlugin_24", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                String string = String.join((CharSequence)name2, serviceErrorList);
                errMsgList.add(ResManager.loadKDString((String)"%s\u6253\u6807\u89c4\u5219\u670d\u52a1\u7c7b\u9519\u8bef\uff0c\u8bf7\u6309\u89c4\u8303\u201c\u670d\u52a1\u7c7b\u6240\u5c5e\u5e94\u7528\u7f16\u7801.\u670d\u52a1\u7c7b\u540d\u201d\u586b\u5199\u3002", (String)"LabelStrategyPlugin_27", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{string}));
            }
            if (!errMsgList.isEmpty()) {
                this.getView().showTipNotification(String.join((CharSequence)"\n", errMsgList));
                return false;
            }
        }
        String frequency = (String)this.getModel().getValue("frequency");
        if (!HRStringUtils.equals((String)frequency, (String)this.getPageCache().get("frequency"))) {
            this.getModel().setValue("nexttasktime", null);
        }
        formOperate.getOption().setVariableValue("filterValue", this.getPageCache().get("filterValue"));
        return true;
    }

    private void beforeSaveWithHand(FormOperate formOperate) {
        Map<String, List<Map<String, Object>>> entryData = this.getEntryData();
        String bizIdStr = this.getPageCache().get(KEY_DB_BIZ_ID);
        String delIdStr = null;
        if (!HRStringUtils.isEmpty((String)bizIdStr)) {
            Set bizIdList = (Set)SerializationUtils.fromJsonString((String)bizIdStr, Set.class);
            HashSet newBizIdList = Sets.newHashSetWithExpectedSize((int)entryData.size());
            for (Map.Entry entry : entryData.entrySet()) {
                for (Map dataMap : (List)entry.getValue()) {
                    newBizIdList.add(dataMap.get("id"));
                }
            }
            HashSet deleteIdList = Sets.newHashSetWithExpectedSize((int)bizIdList.size());
            for (Object bizId : bizIdList) {
                if (newBizIdList.contains(bizId)) continue;
                deleteIdList.add(bizId);
            }
            if (!deleteIdList.isEmpty()) {
                delIdStr = SerializationUtils.toJsonString((Object)deleteIdList);
            }
        }
        for (Map.Entry<String, List<Map<String, Object>>> entry : entryData.entrySet()) {
            for (Map map : entry.getValue()) {
                HashSet removeKeySet = Sets.newHashSetWithExpectedSize((int)map.size());
                for (Map.Entry fieldEntry : map.entrySet()) {
                    if (!((String)fieldEntry.getKey()).contains("_dm6h7z_")) continue;
                    removeKeySet.add(fieldEntry.getKey());
                }
                for (String key : removeKeySet) {
                    String newKey = key.replace("_dm6h7z_", "@");
                    map.put(newKey, map.remove(key));
                }
            }
        }
        formOperate.getOption().setVariableValue("delData", delIdStr);
        formOperate.getOption().setVariableValue("labelData", SerializationUtils.toJsonString(entryData));
        formOperate.getOption().setVariableValue("originESData", this.getPageCache().get("originESData"));
    }

    private void loadByRuleWorkType() {
        DynamicObject lbl = (DynamicObject)this.getModel().getValue(KEY_LABEL);
        DynamicObject lblObj = (DynamicObject)this.getModel().getValue(KEY_LABEL_OBJECT);
        Long id = (Long)this.getModel().getValue("id");
        long lblId = lbl.getLong("id");
        this.generateRuleFlex(id, lblId, lblObj.getLong("id"));
        DynamicObject[] filters = LabelObjectServiceHelper.getFilters((Long)id);
        Map<String, Map> dbFilterMap = Arrays.stream(filters).collect(Collectors.toMap(fl -> fl.getString("fieldkey"), fl -> {
            HashMap map = Maps.newHashMapWithExpectedSize((int)2);
            map.put("hasfilter", fl.getBoolean("hasfilter"));
            map.put("value", fl.getString("value"));
            return map;
        }, (oldValue, newValue) -> newValue));
        DynamicObject lblObjConfig = LabelObjectServiceHelper.getLblObjConfig((Long)lblObj.getLong("id"));
        DynamicObjectCollection conditionEntry = lblObjConfig.getDynamicObjectCollection("conditionentryentity");
        HashMap filterMap = Maps.newHashMapWithExpectedSize((int)conditionEntry.size());
        for (DynamicObject entry : conditionEntry) {
            String ruleEntityNumber = entry.getString("ruleentitynumberalias");
            String ruleFieldNumber = entry.getString("rulefieldnumber");
            String ruleDisplayName = entry.getString("ruledisplayname");
            String fieldKey = ruleEntityNumber + "." + ruleFieldNumber;
            Map map = dbFilterMap.getOrDefault(fieldKey, Maps.newHashMapWithExpectedSize((int)1));
            if (HRStringUtils.isEmpty((String)ruleDisplayName)) {
                map.put("name", entry.getString("rulefieldname"));
            } else {
                map.put("name", ruleDisplayName);
            }
            filterMap.put(ruleEntityNumber + "." + ruleFieldNumber, map);
        }
        this.showFilterForm(filterMap);
    }

    private Map<String, List<Map<String, Object>>> loadDataByHandWorkType(Map<String, Map<String, Object>> fieldKeyPropMap) {
        HashMap entryDataList = Maps.newHashMapWithExpectedSize((int)6);
        long id = (Long)this.getModel().getValue("id");
        if (id != 0L) {
            ArrayList selectFieldList = Lists.newArrayListWithExpectedSize((int)24);
            selectFieldList.add("id");
            selectFieldList.add("creatorId");
            selectFieldList.add("createTime");
            selectFieldList.add("labels.createTime");
            selectFieldList.add("labels.creatorId");
            selectFieldList.add("labels.labelValueId");
            for (Map.Entry<String, Map<String, Object>> entry : fieldKeyPropMap.entrySet()) {
                String newKey = entry.getKey().replace("_dm6h7z_", "@");
                selectFieldList.add("fields." + newKey);
            }
            selectFieldList.add("fields.id");
            LabelTaskStorageService labelTaskStorageService = new LabelTaskStorageService(Long.valueOf(id), "hand");
            int count = labelTaskStorageService.getCount(null);
            List resultList = labelTaskStorageService.getResultList(selectFieldList.toArray(new String[0]), null, 0, count);
            HashSet idSet = Sets.newHashSetWithExpectedSize((int)resultList.size());
            HashMap originData = Maps.newHashMapWithExpectedSize((int)resultList.size());
            for (Map map : resultList) {
                String dataId = (String)map.get("id");
                Object creatorId1 = map.get("creatorId");
                Object createTime = map.get("createTime");
                Map originDataSon = originData.getOrDefault(dataId, Maps.newHashMapWithExpectedSize((int)2));
                originDataSon.put("creatorId", (String)creatorId1);
                originDataSon.put("createTime", (String)createTime);
                originData.put(dataId, originDataSon);
                List labelList = (List)map.get("labels");
                List fieldDataList = (List)map.get("fields");
                for (Map fieldData : fieldDataList) {
                    HashSet removeKeySet = Sets.newHashSetWithExpectedSize((int)fieldData.size());
                    for (Map.Entry entry : fieldData.entrySet()) {
                        if (!((String)entry.getKey()).contains("@")) continue;
                        removeKeySet.add(entry.getKey());
                    }
                    for (Object key2 : removeKeySet) {
                        String newKey = ((String)key2).replace("@", "_dm6h7z_");
                        fieldData.put(newKey, fieldData.remove(key2));
                    }
                }
                for (Map labelMap : labelList) {
                    Object key2;
                    String date = (String)labelMap.get("createTime");
                    Object creatorId = labelMap.get("creatorId");
                    key2 = fieldDataList.iterator();
                    while (key2.hasNext()) {
                        Map data = (Map)key2.next();
                        try {
                            data.put("labeltime", HRDateTimeUtils.parseDate((String)date));
                        }
                        catch (ParseException e) {
                            LOGGER.error("loadByHandWorkType-ParseException:{}", (Object)e.getMessage());
                        }
                        data.put("labelperson", creatorId);
                        idSet.add(data.get("id"));
                    }
                    String labelValueId = (Long)labelMap.get(KEY_LABEL_VALUE_ID) + "";
                    List pageEntryDataList = entryDataList.getOrDefault(labelValueId, Lists.newArrayListWithExpectedSize((int)resultList.size()));
                    pageEntryDataList.addAll(fieldDataList);
                    entryDataList.put(labelValueId, pageEntryDataList);
                }
            }
            entryDataList.forEach((key, value) -> value.sort((x1, x2) -> ((Date)x2.get("labeltime")).compareTo((Date)x1.get("labeltime"))));
            if (!idSet.isEmpty()) {
                this.getPageCache().put(KEY_DB_BIZ_ID, SerializationUtils.toJsonString((Object)idSet));
            }
            this.getPageCache().put("originESData", SerializationUtils.toJsonString((Object)originData));
        }
        this.getPageCache().put("labelData", SerializationUtils.toJsonString((Object)entryDataList));
        return entryDataList;
    }

    private void setHand(boolean hasHand, boolean isFactLabel) {
        ComboEdit comboEdit = (ComboEdit)this.getControl("worktype");
        ArrayList comboItemList = Lists.newArrayListWithExpectedSize((int)2);
        String rule = ResManager.loadKDString((String)"\u89c4\u5219\u6253\u6807", (String)"LabelStrategyPlugin_18", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        ComboItem comboItem = new ComboItem();
        comboItem.setValue("0");
        comboItem.setCaption(new LocaleString(rule));
        comboItemList.add(comboItem);
        if (hasHand && !isFactLabel) {
            String hand = ResManager.loadKDString((String)"\u624b\u52a8\u6253\u6807", (String)"LabelStrategyPlugin_19", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            ComboItem comboItem2 = new ComboItem();
            comboItem2.setValue("1");
            comboItem2.setCaption(new LocaleString(hand));
            comboItemList.add(comboItem2);
        }
        comboEdit.setComboItems((List)comboItemList);
        comboEdit.selectedStore(comboItem);
    }

    private void showFilterForm(Map<String, Map<String, Object>> filterMap) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_labelstrategyfilter");
        showParameter.getOpenStyle().setShowType(ShowType.InContainer);
        showParameter.getOpenStyle().setTargetKey("flexpanelap4");
        showParameter.setStatus(this.getView().getFormShowParameter().getStatus());
        StyleCss style = new StyleCss();
        style.setWidth("100%");
        style.setHeight("100%");
        showParameter.getOpenStyle().setInlineStyleCss(style);
        showParameter.setCustomParam("filterMap", (Object)SerializationUtils.toJsonString(filterMap));
        this.getPageCache().put(KEY_PAGE_CACHE_FILTER, showParameter.getPageId());
        this.getView().showForm(showParameter);
    }

    private String showLabelDataForm(String tabPageKey, String targetKey, long lblObjId, long lblValueId, String labelVal, boolean isLock, Map<String, Object> paramMap) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_labeldatalist");
        showParameter.getOpenStyle().setShowType(ShowType.InContainer);
        showParameter.getOpenStyle().setTargetKey(targetKey);
        showParameter.setStatus(this.getView().getFormShowParameter().getStatus());
        StyleCss style = new StyleCss();
        style.setWidth("100%");
        style.setHeight("100%");
        showParameter.getOpenStyle().setInlineStyleCss(style);
        paramMap.put(KEY_LABEL_OBJECT, lblObjId + "");
        paramMap.put(KEY_LABEL_VALUE_ID, lblValueId + "");
        paramMap.put("labelValue", labelVal);
        paramMap.put(KEY_TAB_PAGE, tabPageKey);
        paramMap.put("isLock", isLock);
        paramMap.put("labelObjFilter", this.getPageCache().get("labelObjFilter"));
        showParameter.setCustomParams(paramMap);
        this.getView().showForm(showParameter);
        return showParameter.getPageId();
    }

    private boolean checkRule() {
        String hrFilterName = this.getPageCache().get("hrFilterLblValue");
        Map hrFilterNameMap = (Map)SerializationUtils.fromJsonString((String)hrFilterName, Map.class);
        HashMap errInfoMap = Maps.newHashMapWithExpectedSize((int)hrFilterNameMap.size());
        for (Map.Entry entry : hrFilterNameMap.entrySet()) {
            String key2 = (String)entry.getKey();
            String value2 = (String)entry.getValue();
            HRFilter hrFilter = (HRFilter)this.getControl(key2);
            String newValue = hrFilter.getValue(true);
            if (HRStringUtils.isEmpty((String)newValue)) {
                this.getView().showTipNotification(value2 + ":" + ResManager.loadKDString((String)"\u8bf7\u914d\u7f6e\u6761\u4ef6\u90e8\u5206", (String)"LabelStrategyPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                return false;
            }
            RuleValidateInfo info = RuleValidateUtil.validCondition((String)newValue, (boolean)false);
            if (info.isSuccess()) continue;
            errInfoMap.put(key2, info);
        }
        StringBuilder stringBuilder = new StringBuilder();
        errInfoMap.forEach((key, value) -> value.getMsgList().forEach(msg -> stringBuilder.append((String)hrFilterNameMap.get(key)).append(':').append((String)msg)));
        if (stringBuilder.length() > 0) {
            this.getView().showTipNotification(stringBuilder.toString());
            return false;
        }
        return true;
    }

    private void next() {
        Tab tab = (Tab)this.getView().getControl(TAB_AP);
        String currentTab = tab.getCurrentTab();
        if ("tabpageap".equals(currentTab)) {
            DynamicObject lblObj = (DynamicObject)this.getModel().getValue(KEY_LABEL_OBJECT);
            long lblObjId = lblObj.getLong("id");
            String workType = (String)this.getModel().getValue("worktype");
            String isInitHand = this.getPageCache().get(IS_INIT_HAND);
            if (!Boolean.TRUE.toString().equals(isInitHand) && "1".equals(workType)) {
                DynamicObject lbl = (DynamicObject)this.getModel().getValue(KEY_LABEL);
                long lblId = lbl.getLong("id");
                this.generateHandFlex(lblId, lblObjId);
                this.getPageCache().put(IS_INIT_HAND, Boolean.TRUE.toString());
            }
        }
        String nextTab = LabelService.doStepAction((Tab)tab, (boolean)true);
        this.setVisible(nextTab);
    }

    private void previous() {
        Tab tab = (Tab)this.getView().getControl(TAB_AP);
        String nextTab = LabelService.doStepAction((Tab)tab, (boolean)false);
        this.setVisible(nextTab);
    }

    private void setVisible(String nextTab) {
        if ("tabpageap1".equals(nextTab)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{NEXT_ONE});
            this.getView().setVisible(Boolean.valueOf(true), new String[]{BAR_SAVE, LAST_ONE});
        } else {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{LAST_ONE, BAR_SAVE});
            this.getView().setVisible(Boolean.valueOf(true), new String[]{NEXT_ONE});
        }
    }

    private void setVisibleByRadio(boolean isEasy) {
        this.getView().setVisible(Boolean.valueOf(isEasy), new String[]{"flexpanelap10"});
        this.getView().setVisible(Boolean.valueOf(!isEasy), new String[]{"flexpanelap11"});
    }

    private boolean validateMustInput() {
        TextEdit control;
        StringBuilder stringBuilder = new StringBuilder();
        DynamicObject label = (DynamicObject)this.getModel().getValue(KEY_LABEL);
        DynamicObject labelObj = (DynamicObject)this.getModel().getValue(KEY_LABEL_OBJECT);
        String number = (String)this.getModel().getValue("number");
        String workType = (String)this.getModel().getValue("worktype");
        Date startDate = (Date)this.getModel().getValue("startdate");
        Date endDate = (Date)this.getModel().getValue("enddate");
        String frequency = (String)this.getModel().getValue("frequency");
        boolean hasEmpty = false;
        if (HRStringUtils.isEmpty((String)number)) {
            control = (TextEdit)this.getControl("number");
            stringBuilder.append(ResManager.loadKDString((String)"\u201c%s\u201d\u3001", (String)"LabelStrategyPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{control.getProperty().getDisplayName().getLocaleValue()}));
            hasEmpty = true;
        }
        if (label == null) {
            control = (BasedataEdit)this.getControl(KEY_LABEL);
            stringBuilder.append(ResManager.loadKDString((String)"\u201c%s\u201d\u3001", (String)"LabelStrategyPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{control.getProperty().getDisplayName().getLocaleValue()}));
            hasEmpty = true;
        }
        if (labelObj == null) {
            control = (BasedataEdit)this.getControl(KEY_LABEL_OBJECT);
            stringBuilder.append(ResManager.loadKDString((String)"\u201c%s\u201d\u3001", (String)"LabelStrategyPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{control.getProperty().getDisplayName().getLocaleValue()}));
            hasEmpty = true;
        }
        if (HRStringUtils.isEmpty((String)workType)) {
            control = (ComboEdit)this.getControl("worktype");
            stringBuilder.append(ResManager.loadKDString((String)"\u201c%s\u201d\u3001", (String)"LabelStrategyPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{control.getProperty().getDisplayName().getLocaleValue()}));
            hasEmpty = true;
        }
        if (startDate == null || endDate == null) {
            stringBuilder.append(ResManager.loadKDString((String)"\u7b56\u7565\u6709\u6548\u671f\u3001", (String)"LabelStrategyPlugin_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            hasEmpty = true;
        }
        if ("0".equals(workType)) {
            if (labelObj != null) {
                boolean bl = hasEmpty = !this.checkFilter(stringBuilder) || hasEmpty;
            }
            if (HRStringUtils.isEmpty((String)frequency)) {
                String frequencyCap = ResManager.loadKDString((String)"\u6253\u6807\u9891\u7387", (String)"LabelStrategyPlugin_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                stringBuilder.append(ResManager.loadKDString((String)"\u201c%s\u201d\u3001", (String)"LabelStrategyPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{frequencyCap}));
                hasEmpty = true;
            }
        }
        if (hasEmpty) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            String errMsg = ResManager.loadKDString((String)"\u8bf7\u6309\u8981\u6c42\u586b\u5199%s\u3002", (String)"LabelStrategyPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{stringBuilder.toString()});
            this.getView().showTipNotification(errMsg);
        }
        return !hasEmpty;
    }

    private boolean checkFilter(StringBuilder errMsg) {
        String range = ResManager.loadKDString((String)"\u6253\u6807\u8303\u56f4", (String)"LabelStrategyPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        String filterPageId = this.getPageCache().get(KEY_PAGE_CACHE_FILTER);
        IFormView filterView = this.getView().getView(filterPageId);
        String customParam = (String)filterView.getFormShowParameter().getCustomParam("filterPropMap");
        Map map = (Map)SerializationUtils.fromJsonString((String)customParam, Map.class);
        HashMap valueMap = Maps.newHashMapWithExpectedSize((int)map.size());
        for (Map.Entry entry : map.entrySet()) {
            boolean hasFilter = false;
            String valueStr = "";
            for (String propKey : (List)entry.getValue()) {
                if (propKey.contains("hasfilter")) {
                    hasFilter = (Boolean)filterView.getModel().getValue(propKey);
                    continue;
                }
                valueStr = GenFieldUtil.getValue((String)propKey, (IFormView)filterView);
            }
            if (!hasFilter && HRStringUtils.isEmpty((String)valueStr)) {
                errMsg.append(ResManager.loadKDString((String)"\u201c%s\u201d\u3001", (String)"LabelStrategyPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[]{range}));
                return false;
            }
            Object data = hasFilter ? Boolean.valueOf(true) : valueStr;
            valueMap.put(entry.getKey(), data);
        }
        this.getPageCache().put("filterValue", SerializationUtils.toJsonString((Object)valueMap));
        return true;
    }

    private void generateHandFlex(long lblId, long lblObjId) {
        Tab tab = (Tab)this.getView().getControl("tabap2");
        String tabPageKey = this.getPageCache().get(KEY_TAB_PAGE_HAND);
        if (!HRStringUtils.isEmpty((String)tabPageKey)) {
            String[] split = tabPageKey.split(",");
            tab.deleteControls(split);
        }
        if (lblId > 0L && lblObjId > 0L) {
            LabelDataService labelDataService = new LabelDataService(lblObjId, true);
            Map fieldKeyPropMap = labelDataService.getFieldKeyPropMap();
            this.getPageCache().put("fieldPropType", SerializationUtils.toJsonString((Object)fieldKeyPropMap));
            Map<String, List<Map<String, Object>>> dataGroupMap = this.loadDataByHandWorkType(fieldKeyPropMap);
            DynamicObject[] labelValues = LblStrategyServiceHelper.getLabelValues((Long)lblId);
            ArrayList items = Lists.newArrayListWithExpectedSize((int)labelValues.length);
            CharSequence[] names = new String[labelValues.length];
            for (int i = 0; i < labelValues.length; ++i) {
                String labelValue = labelValues[i].getString("value");
                String labelValueIdStr = labelValues[i].getLong("id") + "";
                TabPageAp tabPageAp = new TabPageAp();
                String tabPageId = "tabpagehand" + labelValueIdStr;
                names[i] = tabPageId;
                tabPageAp.setKey(tabPageId);
                List<Map<String, Object>> dataList = dataGroupMap.get(labelValueIdStr);
                int size = dataList == null ? 0 : dataList.size();
                tabPageAp.setName(new LocaleString(labelValue + "(" + size + ")"));
                tabPageAp.getItems().add(this.generateHandDescFlexPanelAp(tabPageId, labelValues[i], lblObjId, dataList));
                tab.getItems().add(tabPageAp.buildRuntimeControl());
                Map tabPageMap = tabPageAp.createControl();
                tabPageMap.put(tabPageId, tabPageAp);
                items.add(tabPageMap);
            }
            HashSet tabKeySet = Sets.newHashSetWithExpectedSize((int)8);
            tabKeySet.add(names[0]);
            this.getPageCache().put("tabSelect", SerializationUtils.toJsonString((Object)tabKeySet));
            this.getPageCache().put(KEY_TAB_PAGE_HAND, String.join((CharSequence)",", names));
            tab.addControls((List)items);
        }
    }

    private void generateRuleFlex(Long id, Long lblId, Long lblObjId) {
        DynamicObject[] labelValues = LblStrategyServiceHelper.getLabelValues((Long)lblId);
        Object type = this.getModel().getValue("configtype");
        if ("2".equals(type)) {
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity");
            Map<Long, String> map = entryEntity.stream().collect(Collectors.toMap(entry -> entry.getLong("labelvalue.id"), entry -> entry.getString("service"), (k1, k2) -> k1));
            this.getModel().deleteEntryData("entryentitydisplay");
            this.getModel().batchCreateNewEntryRow("entryentitydisplay", labelValues.length);
            for (int i = 0; i < labelValues.length; ++i) {
                DynamicObject labelValue = labelValues[i];
                String service = map.get(labelValue.getLong("id"));
                this.getModel().setValue("labelvaluedisplay", (Object)labelValue, i);
                this.getModel().setValue("servicedisplay", (Object)service, i);
            }
        } else {
            Map<Object, Object> labelPolicyRuleGroupMap;
            Tab tab = (Tab)this.getView().getControl("tabap1");
            String tabPageKey = this.getPageCache().get(KEY_TAB_PAGE);
            if (!HRStringUtils.isEmpty((String)tabPageKey)) {
                String[] split = tabPageKey.split(",");
                tab.deleteControls(split);
            }
            if (id != null && id != 0L) {
                DynamicObject[] labelPolicyRules = LblStrategyServiceHelper.getLabelPolicyRules((Long)id);
                labelPolicyRuleGroupMap = Arrays.stream(labelPolicyRules).collect(Collectors.toMap(labelPolicyRule -> labelPolicyRule.getLong("labelvalue.id"), labelPolicyRule -> labelPolicyRule, (oldValue, newValue) -> newValue));
            } else {
                labelPolicyRuleGroupMap = Maps.newHashMap();
            }
            CharSequence[] names = new String[labelValues.length];
            ArrayList items = Lists.newArrayListWithExpectedSize((int)labelValues.length);
            String[] filterNames = new String[labelValues.length];
            HashMap filterValueMap = Maps.newHashMapWithExpectedSize((int)labelPolicyRuleGroupMap.size());
            HashMap filterNameMap = Maps.newHashMapWithExpectedSize((int)labelPolicyRuleGroupMap.size());
            for (int i = 0; i < labelValues.length; ++i) {
                String key;
                String labelValue = labelValues[i].getString("value");
                long labelValueId = labelValues[i].getLong("id");
                TabPageAp tabPageAp = new TabPageAp();
                String tabPageId = "tabpage" + i;
                names[i] = tabPageId;
                tabPageAp.setKey(tabPageId);
                tabPageAp.setName(new LocaleString(labelValue));
                filterNames[i] = key = labelValueId + "";
                tabPageAp.getItems().add(this.generateRuleTabPageFlex(labelValues[i]));
                Map tabPageMap = tabPageAp.createControl();
                tabPageMap.put(tabPageId, tabPageAp);
                items.add(tabPageMap);
                DynamicObject labelPolicyRule2 = (DynamicObject)labelPolicyRuleGroupMap.get(labelValueId);
                String condition = "";
                if (labelPolicyRule2 != null) {
                    condition = labelPolicyRule2.getString("conditions");
                }
                filterValueMap.put(key, condition);
                filterNameMap.put(key, labelValue);
            }
            this.getPageCache().put(KEY_TAB_PAGE, String.join((CharSequence)",", names));
            this.getPageCache().put("hrFilterKey", SerializationUtils.toJsonString((Object)filterValueMap));
            this.getPageCache().put("hrFilterLblValue", SerializationUtils.toJsonString((Object)filterNameMap));
            tab.addControls((List)items);
            for (String filterName : filterNames) {
                this.getView().updateView(filterName);
            }
            if (lblObjId != null) {
                List queryFieldCommonBoList = this.labelService.getQueryFieldCommonBoList(lblObjId);
                this.labelService.setBaseDataNum(queryFieldCommonBoList);
                List paramList = AnobjFilterUtil.getParamList((List)queryFieldCommonBoList);
                String pageStatus = this.getView().getPageCache().get("pageState");
                this.fillFilterParam(this.labelService.getDisplayList(paramList, lblObjId), filterValueMap, pageStatus);
            }
        }
    }

    private FlexPanelAp generateRuleTabPageFlex(DynamicObject labelValue) {
        FlexPanelAp flexPanelApDesc = this.generateFlexPanelAp(labelValue);
        long labelValueId = labelValue.getLong("id");
        HRFilterAp hrFilterAp = new HRFilterAp();
        hrFilterAp.setKey(labelValueId + "");
        hrFilterAp.setWidth(new LocaleString("100%"));
        FlexPanelAp flexPanelApTop = new FlexPanelAp();
        flexPanelApTop.setKey("flexPanelApRuleTop" + labelValueId);
        Style style = new Style();
        Padding margin = new Padding();
        margin.setBottom("12px");
        style.setPadding(margin);
        flexPanelApTop.setStyle(style);
        flexPanelApTop.setHeight(new LocaleString("560px"));
        flexPanelApTop.getItems().add(flexPanelApDesc);
        flexPanelApTop.getItems().add(hrFilterAp);
        flexPanelApTop.setGrow(0);
        return flexPanelApTop;
    }

    private FlexPanelAp generateHandDescFlexPanelAp(String tabPageKey, DynamicObject labelValue, long lblObjId, List<Map<String, Object>> dataList) {
        long labelValueId = labelValue.getLong("id");
        FlexPanelAp flexPanelAp = new FlexPanelAp();
        flexPanelAp.setKey("lfhand" + labelValueId);
        flexPanelAp.setDirection("column");
        flexPanelAp.setOverflow("visible");
        Style style0 = new Style();
        Border border = new Border();
        border.setTop("1px_solid_#d9d9d9");
        border.setLeft("1px_solid_#d9d9d9");
        border.setRight("1px_solid_#d9d9d9");
        border.setBottom("1px_solid_#d9d9d9");
        style0.setBorder(border);
        flexPanelAp.setStyle(style0);
        LabelAp labelAp = new LabelAp();
        labelAp.setKey("lhand" + labelValueId);
        labelAp.setName(new LocaleString(labelValue.getString("description")));
        labelAp.setFontSize(15);
        labelAp.setWidth(new LocaleString("100%"));
        Style style = new Style();
        Padding padding = new Padding();
        padding.setTop("10px");
        padding.setBottom("10px");
        padding.setLeft("10px");
        padding.setRight("10px");
        style.setPadding(padding);
        labelAp.setStyle(style);
        FlexPanelAp advConAp = new FlexPanelAp();
        advConAp.setKey("advConAp" + labelValueId);
        advConAp.setWidth(new LocaleString("100%"));
        advConAp.setStyle(style);
        String pageCacheMapStr = this.getPageCache().get("dataPageCache");
        Map pageCacheMap = HRStringUtils.isEmpty((String)pageCacheMapStr) ? Maps.newHashMapWithExpectedSize((int)4) : (Map)SerializationUtils.fromJsonString((String)pageCacheMapStr, Map.class);
        boolean isLock = Boolean.parseBoolean(this.getPageCache().get("isLock"));
        HashMap paramMap = Maps.newHashMapWithExpectedSize((int)6);
        if (!CollectionUtils.isEmpty(dataList)) {
            paramMap.put("labelData", SerializationUtils.toJsonString(dataList));
        }
        paramMap.put("fieldPropType", this.getPageCache().get("fieldPropType"));
        String value = labelValue.getString("value");
        String pageId = this.showLabelDataForm(tabPageKey, advConAp.getKey(), lblObjId, labelValueId, value, isLock, paramMap);
        pageCacheMap.put(labelValueId + "", pageId);
        String labelValueStr = this.getPageCache().get(KEY_LABEL_VALUE_ID);
        Map labelValueIdMap = HRStringUtils.isEmpty((String)labelValueStr) ? Maps.newHashMapWithExpectedSize((int)4) : (Map)SerializationUtils.fromJsonString((String)labelValueStr, Map.class);
        labelValueIdMap.put(labelValueId + "", value);
        this.getPageCache().put(KEY_LABEL_VALUE_ID, SerializationUtils.toJsonString((Object)labelValueIdMap));
        this.getPageCache().put("dataPageCache", SerializationUtils.toJsonString(pageCacheMap));
        flexPanelAp.getItems().add(labelAp);
        flexPanelAp.getItems().add(advConAp);
        return flexPanelAp;
    }

    private FlexPanelAp generateFlexPanelAp(DynamicObject labelValue) {
        long labelValueId = labelValue.getLong("id");
        FlexPanelAp flexPanelAp = new FlexPanelAp();
        flexPanelAp.setKey("lf" + labelValueId);
        Style style0 = new Style();
        Margin margin0 = new Margin();
        margin0.setTop("12px");
        margin0.setBottom("12px");
        style0.setMargin(margin0);
        flexPanelAp.setStyle(style0);
        flexPanelAp.setGrow(0);
        LabelAp labelAp = new LabelAp();
        labelAp.setKey("l" + labelValueId);
        DynamicObject label = (DynamicObject)this.getModel().getValue(KEY_LABEL);
        String type = label.getString("type");
        String name = "10".equals(type) ? ResManager.loadKDString((String)"\u6807\u7b7e\u503c\u91ca\u4e49", (String)"LabelPlugin_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u6807\u7b7e\u503c\u89c4\u5219\u91ca\u4e49", (String)"LabelPlugin_12", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        labelAp.setName(new LocaleString(name));
        labelAp.setFontSize(15);
        Style style = new Style();
        Margin margin = new Margin();
        margin.setRight("20px");
        style.setMargin(margin);
        labelAp.setStyle(style);
        LabelAp labelAp1 = new LabelAp();
        labelAp1.setKey("l1" + labelValueId);
        labelAp1.setName(new LocaleString(labelValue.getString("description")));
        labelAp1.setFontSize(14);
        labelAp1.setForeColor("#D9D9D9");
        labelAp1.setAlignSelf("center");
        flexPanelAp.getItems().add(labelAp);
        flexPanelAp.getItems().add(labelAp1);
        return flexPanelAp;
    }

    private void fillFilterParam(List<Map<String, String>> paramList, Map<String, String> filterMap, String pageState) {
        filterMap.forEach((key, value) -> {
            HRFilter hrFilter = (HRFilter)this.getControl((String)key);
            hrFilter.setValue(value);
            this.labelService.fillFilterParam(paramList, hrFilter, value, pageState);
        });
    }

    private void setFrequency() {
        String frequency = (String)this.getModel().getValue("frequency");
        String[] frequencies = new String[2];
        if (!HRStringUtils.isEmpty((String)frequency)) {
            String[] split = frequency.split("_");
            frequencies[0] = split[0];
            if (split.length > 1) {
                frequencies[1] = frequency;
            }
        }
        HashMap data = Maps.newHashMapWithExpectedSize((int)3);
        data.put("value", frequencies);
        data.put("caption", ResManager.loadKDString((String)"\u6253\u6807\u9891\u7387", (String)"LabelStrategyPlugin_6", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        this.setTipsData(data);
        data.put("combo", this.labelService.getFrequencyData());
        CustomControl customControl = (CustomControl)this.getControl(FREQUENCY);
        customControl.setData((Object)data);
    }

    private void setTipsData(Map<String, Object> data) {
        data.put("tips0", ResManager.loadKDString((String)"\u4ec5\u4e00\u6b21\uff1a\u7b56\u7565\u751f\u6548\u540e\uff0c\u6267\u884c\u4e00\u6b21", (String)"LabelStrategyPlugin_7", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        data.put("tips1", ResManager.loadKDString((String)"\u6bcf\u5929\uff1a\u7b56\u7565\u751f\u6548\u540e\uff0c\u6bcf\u5929\u6267\u884c\u4e00\u6b21\u3002", (String)"LabelStrategyPlugin_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        data.put("tips2", ResManager.loadKDString((String)"\u6bcf\u661f\u671f\uff1a\u7b56\u7565\u751f\u6548\u540e\uff0c\u6839\u636e\u914d\u7f6e\u6bcf\u661f\u671f\u6307\u5b9a\u65f6\u95f4\u6267\u884c\u4e00\u6b21\u3002", (String)"LabelStrategyPlugin_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        data.put("tips3", ResManager.loadKDString((String)"\u6bcf\u6708\uff1a\u7b56\u7565\u751f\u6548\u540e\uff0c\u6839\u636e\u914d\u7f6e\u6bcf\u6708\u6307\u5b9a\u65f6\u95f4\u6267\u884c\u4e00\u6b21\u3002\u6253\u6807\u9891\u7387\u82e5\u4e3a\u6bcf\u670830\u65e5\uff0c\u52192\u6708\u9ed8\u8ba4\u4e3a\u6700\u540e\u4e00\u5929\u6253\u6807\uff1b \u6253\u6807\u9891\u7387\u82e5\u4e3a\u6bcf\u670831\u65e5\uff0c\u52192\u6708\u30014\u6708\u30016\u6708\u30019\u6708\u300111\u6708\u9ed8\u8ba4\u4e3a\u6700\u540e\u4e00\u5929\u6253\u6807\u3002", (String)"LabelStrategyPlugin_10", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
    }

    private void setVisibleByWorkType(boolean isHand) {
        this.getView().setVisible(Boolean.valueOf(!isHand), new String[]{"flexpanelap4", FREQUENCY, "flexpanelap1"});
        this.getView().setVisible(Boolean.valueOf(isHand), new String[]{"flexpanelap2"});
    }

    private void importCloseCallBack(ClosedCallBackEvent closedCallBackEvent) {
        List dataList = JSON.parseArray((String)((String)closedCallBackEvent.getReturnData()), JSONObject.class);
        HashMap dataMap = Maps.newHashMapWithExpectedSize((int)dataList.size());
        HashSet idSet = Sets.newHashSetWithExpectedSize((int)dataList.size());
        for (JSONObject jsonObject : dataList) {
            String value = jsonObject.getString("value");
            if (HRStringUtils.isEmpty((String)value)) continue;
            String[] split = value.split(",");
            String idStr = jsonObject.getString("id");
            long id = Long.parseLong(idStr);
            idSet.add(id);
            for (String labelValue : split) {
                Set bizIdSet = dataMap.getOrDefault(labelValue, Sets.newHashSetWithExpectedSize((int)dataList.size()));
                bizIdSet.add(idStr);
                dataMap.put(labelValue, bizIdSet);
            }
        }
        String pageCacheStr = this.getPageCache().get("dataPageCache");
        Map pageCacheMap = (Map)SerializationUtils.fromJsonString((String)pageCacheStr, Map.class);
        String labelDataStr = this.getPageCache().get("labelData");
        Map dbDataMap = (Map)SerializationUtils.fromJsonString((String)labelDataStr, Map.class);
        HashMap data = Maps.newHashMapWithExpectedSize((int)pageCacheMap.size());
        HashMap idCountMap = Maps.newHashMapWithExpectedSize((int)dataList.size());
        HashMap dbDataIdMap = Maps.newHashMapWithExpectedSize((int)dataList.size());
        for (Map.Entry entry : pageCacheMap.entrySet()) {
            IFormView view = this.getView().getView((String)entry.getValue());
            LabelDataEntryGrid entryGrid = (LabelDataEntryGrid)view.getControl("entryentity");
            List dbEntryDataList = entryGrid.getData();
            if (dbEntryDataList == null) {
                dbEntryDataList = (List)dbDataMap.get(entry.getKey());
            }
            if (dbEntryDataList == null) continue;
            for (Map dbData : dbEntryDataList) {
                String idStr = dbData.get("id").toString();
                long id = Long.parseLong(idStr);
                if (idSet.contains(id)) {
                    Integer count;
                    Integer n = count = idCountMap.get(id) == null ? Integer.valueOf(0) : (Integer)idCountMap.get(id);
                    Integer n2 = count = Integer.valueOf(count + 1);
                    idCountMap.put(id, count);
                }
                Set bizIdSet = dbDataIdMap.getOrDefault(entry.getKey(), Sets.newHashSetWithExpectedSize((int)dataList.size()));
                bizIdSet.add(idStr);
                dbDataIdMap.put(entry.getKey(), bizIdSet);
            }
            data.put(entry.getKey(), dbEntryDataList);
        }
        for (Map.Entry entry : idCountMap.entrySet()) {
            if (((Integer)entry.getValue()).intValue() != pageCacheMap.size()) continue;
            idSet.remove(entry.getKey());
        }
        DynamicObject lblObj = (DynamicObject)this.getModel().getValue(KEY_LABEL_OBJECT);
        long l = lblObj.getLong("id");
        LabelDataService labelDataService = new LabelDataService(l, true);
        QFilter qFilter = new QFilter("id", "in", (Object)idSet);
        String lblObjFilterStr = this.getPageCache().get("labelObjFilter");
        if (!HRStringUtils.isEmpty((String)lblObjFilterStr)) {
            qFilter.and(QFilter.fromSerializedString((String)lblObjFilterStr));
        }
        List entryDataList = labelDataService.getEntryDataList(qFilter);
        Map<String, Map> entryDataGroup = entryDataList.stream().collect(Collectors.toMap(ed -> (String)ed.get("id"), ed -> ed, (oldValue, newValue) -> newValue));
        String labelValueStr = this.getPageCache().get(KEY_LABEL_VALUE_ID);
        Map labelValueMap = (Map)SerializationUtils.fromJsonString((String)labelValueStr, Map.class);
        boolean isLock = Boolean.parseBoolean(this.getPageCache().get("isLock"));
        String currUserId = RequestContext.get().getCurrUserId() + "";
        String lang = RequestContext.get().getLang().name();
        for (Map.Entry entry : labelValueMap.entrySet()) {
            Set bizIdSet = (Set)dataMap.get(entry.getValue());
            ArrayList pageEntryDataList = Lists.newArrayListWithExpectedSize((int)entryDataList.size());
            Set dbBizIdSet = (Set)dbDataIdMap.get(entry.getKey());
            if (bizIdSet != null) {
                for (String bizId : bizIdSet) {
                    Map newDataMap;
                    if (dbBizIdSet != null && dbBizIdSet.contains(bizId) || (newDataMap = entryDataGroup.get(bizId)) == null) continue;
                    newDataMap.put("labelperson", currUserId);
                    pageEntryDataList.add(newDataMap);
                }
            }
            if (pageEntryDataList.isEmpty()) continue;
            List dbEntryDataList = (List)data.get(entry.getKey());
            if (!CollectionUtils.isEmpty((Collection)dbEntryDataList)) {
                pageEntryDataList.addAll(dbEntryDataList);
            }
            dbDataMap.put(entry.getKey(), pageEntryDataList);
            HashMap paramMap = Maps.newHashMapWithExpectedSize((int)6);
            if (!CollectionUtils.isEmpty((Collection)dataList)) {
                paramMap.put("labelData", SerializationUtils.toJsonString((Object)pageEntryDataList));
            }
            paramMap.put("fieldPropType", this.getPageCache().get("fieldPropType"));
            String tabPageKey = "tabpagehand" + (String)entry.getKey();
            String pageId = this.showLabelDataForm(tabPageKey, "advConAp" + (String)entry.getKey(), l, Long.parseLong((String)entry.getKey()), (String)entry.getValue(), isLock, paramMap);
            pageCacheMap.put(entry.getKey(), pageId);
            HashMap map1 = Maps.newHashMapWithExpectedSize((int)2);
            map1.put(lang, (String)entry.getValue() + "(" + pageEntryDataList.size() + ")");
            HashMap map = Maps.newHashMapWithExpectedSize((int)2);
            map.put("text", map1);
            this.getView().updateControlMetadata(tabPageKey, (Map)map);
        }
        this.getPageCache().put("dataPageCache", SerializationUtils.toJsonString((Object)pageCacheMap));
        this.getPageCache().put("labelData", SerializationUtils.toJsonString((Object)dbDataMap));
    }

    private Map<String, List<Map<String, Object>>> getEntryData() {
        String pageCache = this.getPageCache().get("dataPageCache");
        Map pageCacheMap = (Map)SerializationUtils.fromJsonString((String)pageCache, Map.class);
        HashMap data = Maps.newHashMapWithExpectedSize((int)pageCacheMap.size());
        String labelDataStr = this.getPageCache().get("labelData");
        Map dbDataMap = (Map)SerializationUtils.fromJsonString((String)labelDataStr, Map.class);
        String tabSelectKey = this.getPageCache().get("tabSelect");
        Set tabKeySet = HRStringUtils.isEmpty((String)tabSelectKey) ? Sets.newHashSetWithExpectedSize((int)8) : (Set)SerializationUtils.fromJsonString((String)tabSelectKey, Set.class);
        for (Map.Entry entry : pageCacheMap.entrySet()) {
            List dbDataPageList;
            IFormView view = this.getView().getView((String)entry.getValue());
            LabelDataEntryGrid entryGrid = (LabelDataEntryGrid)view.getControl("entryentity");
            List dataList = entryGrid.getData();
            if (!CollectionUtils.isEmpty((Collection)dataList)) {
                data.put(entry.getKey(), dataList);
                continue;
            }
            if (tabKeySet.contains("tabpagehand" + (String)entry.getKey()) || (dbDataPageList = (List)dbDataMap.get(entry.getKey())) == null) continue;
            data.put(entry.getKey(), dbDataPageList);
        }
        return data;
    }

    private Map<String, Set<String>> getDBData() {
        String pageCache = this.getPageCache().get("dataPageCache");
        Map pageCacheMap = (Map)SerializationUtils.fromJsonString((String)pageCache, Map.class);
        String labelValueStr = this.getPageCache().get(KEY_LABEL_VALUE_ID);
        Map labelValueMap = (Map)SerializationUtils.fromJsonString((String)labelValueStr, Map.class);
        HashMap data = Maps.newHashMapWithExpectedSize((int)256);
        for (Map.Entry entry : pageCacheMap.entrySet()) {
            IFormView view = this.getView().getView((String)entry.getValue());
            LabelDataEntryGrid entryGrid = (LabelDataEntryGrid)view.getControl("entryentity");
            List dataList = entryGrid.getData();
            if (dataList == null) continue;
            for (Map dataMap : dataList) {
                if (dataMap.get("labeltime") == null) continue;
                Object id = dataMap.get("id");
                String labelValueId = (String)entry.getKey();
                String labelValue = (String)labelValueMap.get(labelValueId);
                Set labelValueSet = data.getOrDefault(id.toString(), Sets.newHashSetWithExpectedSize((int)pageCacheMap.size()));
                labelValueSet.add(labelValue);
                data.put(id.toString(), labelValueSet);
            }
        }
        return data;
    }

    private void updateTabName(boolean isHand) {
        String name = isHand ? ResManager.loadKDString((String)"\u6dfb\u52a0\u6253\u6807\u6570\u636e", (String)"LabelStrategyPlugin_13", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u914d\u7f6e\u6253\u6807\u89c4\u5219", (String)"LabelStrategyPlugin_14", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        Tab control = (Tab)this.getControl(TAB_AP);
        TabPage tabPage = (TabPage)control.getItems().get(1);
        tabPage.setText(new LocaleString(name));
    }

    private void loadPageByRule() {
        DynamicObject lblObj = (DynamicObject)this.getModel().getValue(KEY_LABEL_OBJECT);
        long lblObjId = lblObj.getLong("id");
        DynamicObject lblObjConfig = LabelObjectServiceHelper.getLblObjConfig((Long)lblObjId);
        DynamicObjectCollection conditionEntry = lblObjConfig.getDynamicObjectCollection("conditionentryentity");
        HashMap filterMap = Maps.newHashMapWithExpectedSize((int)conditionEntry.size());
        for (DynamicObject entry : conditionEntry) {
            String ruleEntityNumber = entry.getString("ruleentitynumberalias");
            String ruleFieldNumber = entry.getString("rulefieldnumber");
            String ruleDisplayName = entry.getString("ruledisplayname");
            String fieldKey = ruleEntityNumber + "." + ruleFieldNumber;
            HashMap map = Maps.newHashMapWithExpectedSize((int)1);
            if (HRStringUtils.isEmpty((String)ruleDisplayName)) {
                map.put("name", entry.getString("rulefieldname"));
            } else {
                map.put("name", ruleDisplayName);
            }
            filterMap.put(fieldKey, map);
        }
        this.showFilterForm(filterMap);
        DynamicObject lbl = (DynamicObject)this.getModel().getValue(KEY_LABEL);
        Long lblId = lbl.getLong("id");
        this.generateRuleFlex(null, lblId, lblObjId);
    }

    private void setLockByEnableAndDate() {
        String enable = (String)this.getModel().getValue("enable");
        Date endDate = (Date)this.getModel().getValue("enddate");
        boolean isLock = false;
        String text = ResManager.loadKDString((String)"\u6253\u6807\u7b56\u7565\u5df2\u88ab\u7981\u7528\uff0c\u4e0d\u5141\u8bb8\u6dfb\u52a0\u6216\u5220\u9664\u6253\u6807\u6570\u636e\u3002", (String)"LabelStrategyPlugin_17", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        if ("0".equals(enable)) {
            isLock = true;
        }
        if (endDate != null && new Date().after(endDate)) {
            text = isLock ? ResManager.loadKDString((String)"\u6253\u6807\u7b56\u7565\u5df2\u8fc7\u671f\uff0c\u6253\u6807\u7ed3\u679c\u5df2\u88ab\u6e05\u7a7a\uff0c\u4e14\u4e0d\u5141\u8bb8\u6dfb\u52a0\u6253\u6807\u6570\u636e\uff1b\u6253\u6807\u7b56\u7565\u5df2\u88ab\u7981\u7528\uff0c\u4e0d\u5141\u8bb8\u6dfb\u52a0\u6216\u5220\u9664\u6253\u6807\u6570\u636e\u3002", (String)"LabelStrategyPlugin_16", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u6253\u6807\u7b56\u7565\u5df2\u8fc7\u671f\uff0c\u6253\u6807\u7ed3\u679c\u5df2\u88ab\u6e05\u7a7a\uff0c\u4e14\u4e0d\u5141\u8bb8\u6dfb\u52a0\u6253\u6807\u6570\u636e\u3002", (String)"LabelStrategyPlugin_15", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            isLock = true;
        }
        if (isLock) {
            Label label = (Label)this.getControl("labelap");
            label.setText(text);
        }
        this.getView().setVisible(Boolean.valueOf(!isLock), new String[]{KEY_IMPORT});
        this.getView().setVisible(Boolean.valueOf(isLock), new String[]{"flexpanelap6"});
        String pageCache = this.getPageCache().get("dataPageCache");
        if (!HRStringUtils.isEmpty((String)pageCache)) {
            Map pageCacheMap = (Map)SerializationUtils.fromJsonString((String)pageCache, Map.class);
            for (Map.Entry entry : pageCacheMap.entrySet()) {
                IFormView view = this.getView().getView((String)entry.getValue());
                view.setVisible(Boolean.valueOf(!isLock), new String[]{"advcontoolbarap"});
                this.getView().sendFormAction(view);
            }
        }
        this.getPageCache().put("isLock", Boolean.toString(isLock));
    }

    private void setLabelObjectDataFilter(DynamicObject lblObj) {
        LabelService labelService = new LabelService();
        QFilter labelObjectDataFilter = labelService.getLabelObjectDataFilter(lblObj);
        if (labelObjectDataFilter != null) {
            this.getPageCache().put("labelObjFilter", labelObjectDataFilter.toSerializedString());
        } else {
            this.getPageCache().put("labelObjFilter", null);
        }
    }

    private void setFieldCaption(String type) {
        String des;
        String name;
        if ("20".equals(type)) {
            name = ResManager.loadKDString((String)"\u6807\u7b7e\u503c\u89c4\u5219\u540d\u79f0", (String)"LabelPlugin_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            des = ResManager.loadKDString((String)"\u6807\u7b7e\u503c\u89c4\u5219\u91ca\u4e49", (String)"LabelPlugin_12", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        } else {
            name = ResManager.loadKDString((String)"\u6807\u7b7e\u503c", (String)"LabelPlugin_10", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            des = ResManager.loadKDString((String)"\u6807\u7b7e\u503c\u91ca\u4e49", (String)"LabelPlugin_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        }
        EntryGrid entryGrid = (EntryGrid)this.getControl("entryentitydisplay");
        entryGrid.setColumnProperty("proplabelvalue", "header", (Object)new LocaleString(name));
        entryGrid.setColumnProperty("propdesc", "header", (Object)new LocaleString(des));
    }

    private void configTypeConfirmCallBack() {
        String value = (String)this.getModel().getValue("configtype");
        boolean isEasy = "1".equals(value);
        this.setVisibleByRadio(isEasy);
        DynamicObject label = (DynamicObject)this.getModel().getValue(KEY_LABEL);
        long lblId = label.getLong("id");
        Object id = this.getModel().getValue("id");
        DynamicObject lblObj = (DynamicObject)this.getModel().getValue(KEY_LABEL_OBJECT);
        long lblObjId = lblObj.getLong("id");
        if (isEasy) {
            this.getPageCache().put("isLoadPlugin", Boolean.TRUE.toString());
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentitydisplay");
            for (DynamicObject dynamicObject : entryEntity) {
                dynamicObject.set("servicedisplay", null);
            }
            this.getView().updateView("entryentitydisplay");
            if (!Boolean.TRUE.toString().equals(this.getPageCache().get("isLoadEasy"))) {
                this.generateRuleFlex((Long)id, lblId, lblObjId);
                this.getPageCache().put("isLoadEasy", Boolean.TRUE.toString());
            }
        } else {
            this.getPageCache().put("isLoadEasy", Boolean.TRUE.toString());
            String hrFilterValue = this.getPageCache().get("hrFilterKey");
            if (!HRStringUtils.isEmpty((String)hrFilterValue)) {
                Map hrFilterValueMap = (Map)SerializationUtils.fromJsonString((String)hrFilterValue, Map.class);
                hrFilterValueMap.forEach((key, condition) -> {
                    HRFilter hrFilter = (HRFilter)this.getView().getControl(key);
                    hrFilter.setValueAndUpdateControl(null);
                });
            }
            if (!Boolean.TRUE.toString().equals(this.getPageCache().get("isLoadPlugin"))) {
                this.generateRuleFlex((Long)id, lblId, lblObjId);
                this.getPageCache().put("isLoadPlugin", Boolean.TRUE.toString());
            }
        }
    }
}
