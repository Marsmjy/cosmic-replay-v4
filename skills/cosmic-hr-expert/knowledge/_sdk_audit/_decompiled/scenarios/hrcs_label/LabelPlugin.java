/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.EntryGridBindDataListener
 *  kd.bos.form.control.events.RowClickEventListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.mvc.base.BaseView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper
 *  kd.hr.hrcs.common.constants.label.LabelConstants
 *  kd.hr.hrcs.common.constants.label.LabelManageConstants
 *  kd.hr.hrcs.common.constants.label.LabelTypeEnum
 */
package kd.hr.hrcs.formplugin.web.label;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.entity.MulBasedataDynamicObjectCollection;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.EntryGridBindDataListener;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.mvc.base.BaseView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper;
import kd.hr.hrcs.common.constants.label.LabelConstants;
import kd.hr.hrcs.common.constants.label.LabelManageConstants;
import kd.hr.hrcs.common.constants.label.LabelTypeEnum;

public final class LabelPlugin
extends HRDataBaseEdit
implements BeforeF7SelectListener,
RowClickEventListener,
EntryGridBindDataListener,
LabelConstants,
LabelManageConstants {
    private static final LabelServiceHelper labelServiceHelper = new LabelServiceHelper();
    private static final String LABEL_ID_CACHE_KEY = "labelId";

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        BasedataEdit groupBaseDataEdit = (BasedataEdit)this.getView().getControl("group");
        groupBaseDataEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String returnData;
        super.closedCallBack(closedCallBackEvent);
        if ("cacheExpr".equals(closedCallBackEvent.getActionId()) && !HRStringUtils.isEmpty((String)(returnData = (String)closedCallBackEvent.getReturnData()))) {
            Map sonGroupMap;
            Map<String, Map> groupExprCache;
            Map data = (Map)SerializationUtils.fromJsonString((String)returnData, Map.class);
            String index = (String)data.get("index");
            Map exprMap = (Map)data.get("expr");
            String exprBean = this.getPageCache().get("expression");
            if (HRStringUtils.isEmpty((String)exprBean)) {
                groupExprCache = Maps.newHashMapWithExpectedSize((int)16);
                sonGroupMap = exprMap;
            } else {
                groupExprCache = (Map)SerializationUtils.fromJsonString((String)exprBean, Map.class);
                sonGroupMap = groupExprCache.getOrDefault(index, Maps.newHashMapWithExpectedSize((int)8));
                for (Map.Entry entry : exprMap.entrySet()) {
                    sonGroupMap.put(entry.getKey(), entry.getValue());
                }
            }
            groupExprCache.put(index, sonGroupMap);
            this.getPageCache().put("expression", SerializationUtils.toJsonString(groupExprCache));
            String lblObjId = null;
            String disPlayExpr = null;
            for (Map.Entry entry : exprMap.entrySet()) {
                lblObjId = (String)entry.getKey();
                disPlayExpr = (String)((Map)entry.getValue()).get("displayfunctiontext");
            }
            Map<String, Object> responseMap = this.getResponseMap("updateExpr");
            responseMap.put("index", index + "_" + lblObjId);
            responseMap.put("expr", disPlayExpr);
            this.setCustomControlData(responseMap);
        }
    }

    private boolean validateEntityRange(DynamicObjectCollection objectDimensionCollection) {
        if (objectDimensionCollection == null || objectDimensionCollection.size() == 0) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u586b\u5199\u6253\u6807\u5bf9\u8c61\uff0c\u518d\u914d\u7f6e\u6807\u7b7e\u503c\u89c4\u5219\u3002", (String)"LabelPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        return true;
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        Object value = this.getModel().getValue("type");
        if ("20".equals(value)) {
            this.loadByFactType();
        }
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        BaseView baseView = (BaseView)e.getSource();
        BaseShowParameter formShowParameterView = (BaseShowParameter)this.getView().getFormShowParameter();
        Object pkIdObj = formShowParameterView.getPkId();
        long labelId = 0L;
        if (pkIdObj instanceof Long) {
            labelId = (Long)pkIdObj;
        } else if (pkIdObj instanceof String) {
            labelId = Long.parseLong((String)pkIdObj);
        }
        if (labelId == 0L) {
            labelId = labelServiceHelper.getLabelId();
            this.getModel().setValue("id", (Object)labelId);
        }
        this.setCacheLabelId(labelId);
        if (OperationStatus.ADDNEW.equals((Object)baseView.getStatus())) {
            boolean isLeaf;
            Object labelDy = this.getModel().getValue("group");
            if (labelDy != null && !(isLeaf = ((DynamicObject)labelDy).getBoolean("isleaf"))) {
                this.getModel().setValue("group", null);
                this.getModel().setDataChanged(false);
            }
        } else if (OperationStatus.EDIT.equals((Object)baseView.getStatus())) {
            DynamicObjectCollection labelValue;
            this.getView().setEnable(Boolean.FALSE, new String[]{"type"});
            LabelServiceHelper labelServiceHelper = new LabelServiceHelper();
            if (labelServiceHelper.labelHaveFinishPolicyTask(Long.valueOf(labelId)) && (labelValue = this.getModel().getEntryEntity("entryentitylabelvalue")) != null) {
                for (int idx = 0; idx < labelValue.getRowCount(); ++idx) {
                    this.getView().setEnable(Boolean.FALSE, idx, new String[]{"labelvalue"});
                }
            }
        }
        this.setLabelObj();
        this.setVisibleByType();
        this.setFieldCaption((String)this.getModel().getValue("type"));
        this.getModel().setDataChanged(false);
    }

    public void customEvent(CustomEventArgs e) {
        String eventName = e.getEventName();
        if ("getAllData".equals(eventName)) {
            int i;
            List dataList = (List)SerializationUtils.fromJsonString((String)e.getEventArgs(), List.class);
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentitylabelvalue");
            for (i = 0; i < dataList.size() - entryEntity.size(); ++i) {
                entryEntity.addNew();
            }
            for (i = 0; i < dataList.size(); ++i) {
                Map data = (Map)dataList.get(i);
                Map map = (Map)data.get("labelvalue");
                DynamicObject row = (DynamicObject)entryEntity.get(i);
                row.set("labelvalue", map.get("name"));
                row.set("labelvaluedesc", data.get("labelvaluedesc"));
            }
            String value = (String)this.getModel().getValue("configtype");
            if ("2".equals(value)) {
                int i2 = 0;
                HashMap serviceGroupMap = Maps.newHashMapWithExpectedSize((int)(dataList.size() * 2));
                for (Map data : dataList) {
                    List serviceList = (List)data.get("service");
                    if (!CollectionUtils.isEmpty((Collection)serviceList)) {
                        List lblObjMap = (List)data.get("labelobject");
                        Map sonGroup = serviceGroupMap.getOrDefault(i2, Maps.newHashMapWithExpectedSize((int)lblObjMap.size()));
                        int j = 0;
                        for (Map entry : lblObjMap) {
                            String service = (String)serviceList.get(j);
                            Long lblObjId = Long.parseLong((String)entry.get("id"));
                            sonGroup.put(lblObjId, service);
                            ++j;
                        }
                        serviceGroupMap.put(i2, sonGroup);
                    }
                    ++i2;
                }
                this.getPageCache().put("service", SerializationUtils.toJsonString((Object)serviceGroupMap));
                if ("1".equals(this.getPageCache().get("configtype"))) {
                    this.getPageCache().put("isTypeChanged", Boolean.TRUE.toString());
                }
            }
            this.getPageCache().put("dataListSize", String.valueOf(dataList.size()));
            this.getModel().updateEntryCache(entryEntity);
            this.getView().invokeOperation(this.getPageCache().get("saveAndSubmit"));
        } else if ("clickExpr".equals(eventName)) {
            Map dataMap = (Map)SerializationUtils.fromJsonString((String)e.getEventArgs(), Map.class);
            String index = (String)dataMap.get("index");
            String[] split = index.split("_");
            this.clickExpr(split[0], split[1]);
        } else if ("deleteEntry".equals(eventName)) {
            Map dataMap = (Map)SerializationUtils.fromJsonString((String)e.getEventArgs(), Map.class);
            List idList = (List)dataMap.get("id");
            List<Long> idParseList = idList.stream().filter(id -> !HRStringUtils.isEmpty((String)id)).map(Long::parseLong).collect(Collectors.toList());
            List rowList = (List)dataMap.get("row");
            this.deleteEntry(rowList, idParseList);
        } else if ("addRow".equals(eventName)) {
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentitylabelvalue");
            entryEntity.addNew();
            this.getModel().updateEntryCache(entryEntity);
        } else if ("confirmChange".equals(eventName)) {
            boolean hasValue = Boolean.parseBoolean(e.getEventArgs());
            if (hasValue) {
                String msg = ResManager.loadKDString((String)"\u9009\u4e2d\u201c\u89c4\u5219\u914d\u7f6e\u201d\u540e\uff0c\u5c06\u6e05\u7a7a\u5df2\u914d\u7f6e\u7684\u6807\u7b7e\u503c\u89c4\u5219\u670d\u52a1\u7c7b\u3002\u786e\u5b9a\u6267\u884c\u6b64\u64cd\u4f5c\u5417\uff1f", (String)"LabelPlugin_13", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                this.getView().showConfirm(msg, MessageBoxOptions.YesNo, new ConfirmCallBackListener("configtype"));
            } else {
                this.configTypeConfirmCallBack();
            }
        } else if ("setService".equals(eventName)) {
            List dataList = (List)SerializationUtils.fromJsonString((String)e.getEventArgs(), List.class);
            DynamicObjectCollection col = this.getModel().getEntryEntity("entryentityrange");
            HashMap serviceGroupMap = Maps.newHashMapWithExpectedSize((int)(dataList.size() * 2));
            int i = 0;
            for (Map data : dataList) {
                List serviceList = (List)data.get("service");
                if (!CollectionUtils.isEmpty((Collection)serviceList)) {
                    Map sonGroup = serviceGroupMap.getOrDefault(i, Maps.newHashMapWithExpectedSize((int)col.size()));
                    int j = 0;
                    for (DynamicObject lblObjRow : col) {
                        String service = (String)serviceList.get(j);
                        if (!HRStringUtils.isEmpty((String)service)) {
                            Long lblObjId = lblObjRow.getLong("labelobject.id");
                            sonGroup.put(lblObjId, service);
                        }
                        ++j;
                    }
                    serviceGroupMap.put(i, sonGroup);
                }
                ++i;
            }
            this.getPageCache().put("service", SerializationUtils.toJsonString((Object)serviceGroupMap));
        } else if ("updateValue".equals(eventName)) {
            Map dataMap = (Map)SerializationUtils.fromJsonString((String)e.getEventArgs(), Map.class);
            Integer row = (Integer)dataMap.get("row");
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentitylabelvalue");
            DynamicObject dynamicObject = (DynamicObject)entryEntity.get(row.intValue());
            dynamicObject.set("labelvalue", dataMap.get("labelvalue"));
            dynamicObject.set("labelvaluedesc", dataMap.get("labelvaluedesc"));
            this.getModel().updateEntryCache(entryEntity);
        }
    }

    private void setLabelObj() {
        DynamicObjectCollection coll = this.getModel().getEntryEntity("entryentityrange");
        MulBasedataDynamicObjectCollection lblObj = (MulBasedataDynamicObjectCollection)this.getModel().getValue("labelobj");
        for (DynamicObject dy : coll) {
            DynamicObject dynamicObject = lblObj.addNew();
            dynamicObject.set("fbasedataid_id", dy.get("labelobject.id"));
            dynamicObject.set("fbasedataid", dy.get("labelobject"));
        }
    }

    private List<Map<String, Object>> coll2List(MulBasedataDynamicObjectCollection oldValue) {
        ArrayList<Map<String, Object>> oldList = new ArrayList<Map<String, Object>>(10);
        for (DynamicObject dy : oldValue) {
            HashMap<String, Object> oldMap = new HashMap<String, Object>(16);
            oldMap.put("fbasedataid_id", dy.get("fbasedataid_id"));
            oldMap.put("pkid", dy.get("pkid"));
            oldList.add(oldMap);
        }
        return oldList;
    }

    @ExcludeFromJacocoGeneratedReport
    private MulBasedataDynamicObjectCollection list2Coll(List<Map<String, Object>> oldList) {
        MulBasedataDynamicObjectCollection lblObj = (MulBasedataDynamicObjectCollection)this.getModel().getValue("labelobj");
        lblObj.clear();
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_labelobject");
        for (Map<String, Object> oldMap : oldList) {
            DynamicObject dynamicObject = lblObj.addNew();
            dynamicObject.set("fbasedataid_id", oldMap.get("fbasedataid_id"));
            dynamicObject.set("pkid", oldMap.get("pkid"));
            DynamicObject label = helper.generateEmptyDynamicObject();
            label.set("id", oldMap.get("fbasedataid_id"));
            dynamicObject.set("fbasedataid", (Object)label);
        }
        return lblObj;
    }

    private void setCacheLabelId(Long labelId) {
        this.getPageCache().put(LABEL_ID_CACHE_KEY, labelId.toString());
    }

    private Long getId() {
        return (Long)this.getModel().getValue("id");
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String propertyName = args.getProperty().getName();
        ChangeData changeSet = args.getChangeSet()[0];
        if ("type".equals(propertyName)) {
            String msg = "10".equals(changeSet.getNewValue()) ? ResManager.loadKDString((String)"\u9009\u4e2d\u201c\u6a21\u578b\u6807\u7b7e\u201d\u540e\uff0c\u5c06\u6e05\u7a7a\u5df2\u914d\u7f6e\u7684\u6807\u7b7e\u503c\u89c4\u5219\u3002\u786e\u5b9a\u6267\u884c\u6b64\u64cd\u4f5c\u5417\uff1f", (String)"LabelPlugin_15", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u9009\u4e2d\u201c\u4e8b\u5b9e\u6807\u7b7e\u201d\u540e\uff0c\u5c06\u6e05\u7a7a\u5df2\u914d\u7f6e\u7684\u6807\u7b7e\u503c\u3002\u786e\u5b9a\u6267\u884c\u6b64\u64cd\u4f5c\u5417\uff1f", (String)"LabelPlugin_16", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            if (this.isValueEmpty((String)changeSet.getOldValue())) {
                this.typeConfirmCallBack();
            } else {
                this.getView().showConfirm(msg, MessageBoxOptions.YesNo, new ConfirmCallBackListener("type"));
            }
        } else if ("labelobj".equals(propertyName)) {
            if (Boolean.parseBoolean(this.getPageCache().get("tipChanged"))) {
                this.getPageCache().put("tipChanged", null);
                return;
            }
            MulBasedataDynamicObjectCollection newValue = (MulBasedataDynamicObjectCollection)changeSet.getNewValue();
            MulBasedataDynamicObjectCollection oldValue = (MulBasedataDynamicObjectCollection)changeSet.getOldValue();
            List<Long> newLblObjIds = newValue.stream().map(dy -> dy.getLong("fbasedataid_id")).collect(Collectors.toList());
            List<Long> oldLblObjIds = oldValue.stream().map(dy -> dy.getLong("fbasedataid_id")).collect(Collectors.toList());
            this.getPageCache().put("oldLblObjIds", SerializationUtils.toJsonString(this.coll2List(oldValue)));
            List<Long> addLblObjIds = this.difference(newLblObjIds, oldLblObjIds);
            List<Long> deleteLblObjIds = this.difference(oldLblObjIds, newLblObjIds);
            this.getPageCache().put("addLblObjIds", SerializationUtils.toJsonString(addLblObjIds));
            this.getPageCache().put("deleteLblObjIds", SerializationUtils.toJsonString(deleteLblObjIds));
            if (!CollectionUtils.isEmpty(deleteLblObjIds)) {
                long labelId = this.getId();
                if (labelServiceHelper.isExistStrategy(Long.valueOf(labelId), deleteLblObjIds)) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u6807\u7b7e\u5df2\u5728\u6253\u6807\u5bf9\u8c61\u4e0a\u751f\u6210\u6253\u6807\u7b56\u7565\uff0c\u4e0d\u53ef\u5220\u9664", (String)"LabelPlugin_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                    this.getPageCache().put("tipChanged", "true");
                    this.getModel().setValue("labelobj", (Object)oldValue);
                    return;
                }
                if (LabelTypeEnum.FACT.getType().equals(this.getModel().getValue("type"))) {
                    String value = (String)this.getModel().getValue("configtype");
                    boolean isEmpty = true;
                    if ("1".equals(value)) {
                        String groupExprCacheStr = this.getPageCache().get("expression");
                        if (!HRStringUtils.isEmpty((String)groupExprCacheStr)) {
                            Map groupExprCache = (Map)SerializationUtils.fromJsonString((String)groupExprCacheStr, Map.class);
                            for (Map.Entry entry : groupExprCache.entrySet()) {
                                Map sonMap = (Map)entry.getValue();
                                if (CollectionUtils.isEmpty((Map)sonMap)) continue;
                                isEmpty = false;
                                break;
                            }
                        }
                    } else {
                        String variableValue = this.getPageCache().get("service");
                        if (!HRStringUtils.isEmpty((String)variableValue)) {
                            Map serviceGroupMap = (Map)SerializationUtils.fromJsonString((String)variableValue, Map.class);
                            for (Map.Entry entry : serviceGroupMap.entrySet()) {
                                Map sonMap = (Map)entry.getValue();
                                if (CollectionUtils.isEmpty((Map)sonMap)) continue;
                                isEmpty = false;
                                break;
                            }
                        }
                    }
                    if (!isEmpty) {
                        List deleteLabelObjectNames = oldValue.stream().filter(dy -> deleteLblObjIds.contains(dy.getLong("fbasedataid_id"))).map(dy -> dy.getString("fbasedataid.name")).collect(Collectors.toList());
                        String msg = String.format(ResManager.loadKDString((String)"\u53d6\u6d88\u9009\u4e2d\u6253\u6807\u5bf9\u8c61\u201c%s\u201d\u540e\u5c06\u4f1a\u5220\u9664\u5176\u6807\u7b7e\u503c\u89c4\u5219\u3002\u786e\u5b9a\u6267\u884c\u6b64\u64cd\u4f5c\u5417\uff1f", (String)"LabelPlugin_8", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), String.join((CharSequence)"\",\"", deleteLabelObjectNames));
                        this.getView().showConfirm(msg, MessageBoxOptions.OKCancel, new ConfirmCallBackListener("deleteEntryRange", (IFormPlugin)this));
                        return;
                    }
                }
                this.deleteEntry(deleteLblObjIds);
            }
            this.addEntry();
            this.setVisibleByType();
            this.sendActionByLabelObj(newValue);
        } else if ("configtype".equals(propertyName)) {
            if ("2".equals(changeSet.getNewValue())) {
                Map groupExprCache;
                boolean isEmpty = true;
                String exprBean = this.getPageCache().get("expression");
                if (!HRStringUtils.isEmpty((String)exprBean) && !CollectionUtils.isEmpty((Map)(groupExprCache = (Map)SerializationUtils.fromJsonString((String)exprBean, Map.class)))) {
                    isEmpty = false;
                }
                String msg = ResManager.loadKDString((String)"\u9009\u4e2d\u201c\u63d2\u4ef6\u914d\u7f6e\u201d\u540e\uff0c\u5c06\u6e05\u7a7a\u5df2\u914d\u7f6e\u7684\u6807\u7b7e\u503c\u89c4\u5219\u3002\u786e\u5b9a\u6267\u884c\u6b64\u64cd\u4f5c\u5417\uff1f", (String)"LabelPlugin_14", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                if (isEmpty) {
                    this.configTypeConfirmCallBack();
                } else {
                    this.getView().showConfirm(msg, MessageBoxOptions.YesNo, new ConfirmCallBackListener("configtype"));
                }
            } else {
                Map<String, Object> responseMap = this.getResponseMap("confirmChange");
                this.setCustomControlData(responseMap);
            }
        }
    }

    private void deleteEntry(List<Long> deleteLblObjIds) {
        DynamicObjectCollection entryEntityRange = this.getModel().getEntryEntity("entryentityrange");
        ArrayList<Integer> idx = new ArrayList<Integer>(deleteLblObjIds.size());
        for (int i = 0; i < entryEntityRange.size(); ++i) {
            if (!deleteLblObjIds.contains(((DynamicObject)entryEntityRange.get(i)).getLong("labelobject.id"))) continue;
            idx.add(i);
        }
        this.getModel().deleteEntryRows("entryentityrange", idx.stream().mapToInt(Integer::intValue).toArray());
    }

    private void addEntry() {
        List addLblObjIds = SerializationUtils.fromJsonStringToList((String)this.getPageCache().get("addLblObjIds"), Long.class);
        if (!CollectionUtils.isEmpty((Collection)addLblObjIds)) {
            DynamicObjectCollection entryEntityRange = this.getModel().getEntryEntity("entryentityrange");
            int idx = entryEntityRange.size();
            this.getModel().batchCreateNewEntryRow("entryentityrange", addLblObjIds.size());
            for (int i = 0; i < addLblObjIds.size(); ++i) {
                Long lblObjId = (Long)addLblObjIds.get(i);
                this.getModel().setValue("labelobject", (Object)lblObjId, idx + i);
            }
        }
    }

    private List<Long> difference(List<Long> list1, List<Long> list2) {
        ArrayList<Long> difference = new ArrayList<Long>(list1);
        difference.removeAll(list2);
        return difference;
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate operate = (AbstractOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        if ("getalldata".equals(operateKey) || "getalldatasubmit".equals(operateKey)) {
            String opKey = "getalldata".equals(operateKey) ? "save" : "submit";
            if (LabelTypeEnum.FACT.getType().equals(this.getModel().getValue("type"))) {
                Map<String, Object> responseMap = this.getResponseMap("getAllData");
                this.setCustomControlData(responseMap);
                this.getPageCache().put("saveAndSubmit", opKey);
            } else {
                this.getView().invokeOperation(opKey);
            }
            return;
        }
        this.checkDeleteModeEntry(args);
        if ("submit".equals(operateKey)) {
            OperateOption op = operate.getOption();
            op.setVariableValue("isFromForm", "Y");
        }
        FormOperate formOperate = (FormOperate)args.getSource();
        formOperate.getOption().setVariableValue("expression", this.getPageCache().get("expression"));
        formOperate.getOption().setVariableValue("service", this.getPageCache().get("service"));
        formOperate.getOption().setVariableValue("isTypeChanged", this.getPageCache().get("isTypeChanged"));
        formOperate.getOption().setVariableValue("dataListSize", this.getPageCache().get("dataListSize"));
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        boolean success;
        super.afterDoOperation(afterDoOperationEventArgs);
        if ("save".equals(afterDoOperationEventArgs.getOperateKey()) || "submit".equals(afterDoOperationEventArgs.getOperateKey())) {
            boolean success2 = afterDoOperationEventArgs.getOperationResult().isSuccess();
            String type = (String)this.getModel().getValue("type");
            if (success2 && LabelTypeEnum.FACT.getType().equals(type)) {
                String configType = (String)this.getModel().getValue("configtype");
                if ("1".equals(configType)) {
                    this.getPageCache().put("configtype", "1");
                } else {
                    this.getPageCache().put("configtype", "2");
                }
                this.getPageCache().put("isTypeChanged", null);
                this.updateControlData("save".equals(afterDoOperationEventArgs.getOperateKey()));
                this.setExprCache();
            }
        } else if ("audit".equals(afterDoOperationEventArgs.getOperateKey())) {
            boolean success3 = afterDoOperationEventArgs.getOperationResult().isSuccess();
            if (success3) {
                Map<String, Object> responseMap = this.getResponseMap("updateStatus");
                responseMap.put("status", BillOperationStatus.AUDIT.name());
                this.setCustomControlData(responseMap);
            }
        } else if (("unsubmit".equals(afterDoOperationEventArgs.getOperateKey()) || "unaudit".equals(afterDoOperationEventArgs.getOperateKey())) && (success = afterDoOperationEventArgs.getOperationResult().isSuccess())) {
            Map<String, Object> responseMap = this.getResponseMap("updateStatus");
            responseMap.put("status", BillOperationStatus.EDIT.name());
            this.setCustomControlData(responseMap);
        }
    }

    private void checkDeleteModeEntry(BeforeDoOperationEventArgs args) {
        AbstractOperate operate = (AbstractOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        if ("deleteentrylabelvaluemodel".equals(operateKey)) {
            EntryGrid entryGrid = (EntryGrid)this.getControl("entryentitylabelvalue");
            int[] selectRows = entryGrid.getSelectRows();
            DynamicObjectCollection entity = this.getModel().getEntryEntity("entryentitylabelvalue");
            if (selectRows != null && selectRows.length > 0) {
                ArrayList labelValueIdList = Lists.newArrayListWithExpectedSize((int)selectRows.length);
                for (int selectRow : selectRows) {
                    DynamicObject dynamicObject = (DynamicObject)entity.get(selectRow);
                    labelValueIdList.add(dynamicObject.getLong("id"));
                }
                if (!this.checkDeleteModeEntry(labelValueIdList)) {
                    args.setCancel(true);
                }
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        super.confirmCallBack(messageBoxClosedEvent);
        if ("deleteEntryRange".equals(messageBoxClosedEvent.getCallBackId())) {
            if (messageBoxClosedEvent.getResult() == MessageBoxResult.Cancel) {
                List oldLblObjIds = SerializationUtils.fromJsonStringToList((String)this.getPageCache().get("oldLblObjIds"), Map.class);
                MulBasedataDynamicObjectCollection coll = this.list2Coll(oldLblObjIds);
                this.getModel().setValue("labelobj", (Object)coll);
                return;
            }
            List delLabelObjIds = SerializationUtils.fromJsonStringToList((String)this.getPageCache().get("deleteLblObjIds"), Long.class);
            String exprJson = this.getPageCache().get("expression");
            if (!HRStringUtils.isEmpty((String)exprJson)) {
                Map exprMap = (Map)SerializationUtils.fromJsonString((String)exprJson, Map.class);
                Iterator iterator = exprMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry next = iterator.next();
                    delLabelObjIds.forEach(id -> ((Map)next.getValue()).remove(id.toString()));
                    if (!((Map)next.getValue()).isEmpty()) continue;
                    iterator.remove();
                }
                this.getPageCache().put("expression", exprMap.isEmpty() ? null : SerializationUtils.toJsonString((Object)exprMap));
            }
            this.deleteEntry(delLabelObjIds);
            this.addEntry();
            MulBasedataDynamicObjectCollection entryEntityRange = (MulBasedataDynamicObjectCollection)this.getModel().getValue("labelobj");
            this.sendActionByLabelObj(entryEntityRange);
        } else if ("configtype".equals(messageBoxClosedEvent.getCallBackId())) {
            if (messageBoxClosedEvent.getResult() == MessageBoxResult.Yes) {
                this.configTypeConfirmCallBack();
            } else {
                String value = (String)this.getModel().getValue("configtype");
                String reverseValue = "1".equals(value) ? "2" : "1";
                DynamicObject dataEntity = this.getModel().getDataEntity();
                dataEntity.set("configtype", (Object)reverseValue);
                this.getView().updateView("configtype");
            }
        } else if ("type".equals(messageBoxClosedEvent.getCallBackId())) {
            if (messageBoxClosedEvent.getResult() == MessageBoxResult.Yes) {
                this.typeConfirmCallBack();
            } else {
                String value = (String)this.getModel().getValue("type");
                String reverseValue = "10".equals(value) ? "20" : "10";
                DynamicObject dataEntity = this.getModel().getDataEntity();
                dataEntity.set("type", (Object)reverseValue);
                this.getView().updateView("type");
            }
        }
    }

    private void configTypeConfirmCallBack() {
        String value = (String)this.getModel().getValue("configtype");
        if ("1".equals(value)) {
            this.getPageCache().put("expression", null);
        }
        Map<String, Object> responseMap = this.getResponseMap("changeConfigType");
        responseMap.put("configType", value);
        this.setCustomControlData(responseMap);
    }

    private void typeConfirmCallBack() {
        String value = (String)this.getModel().getValue("type");
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentitylabelvalue");
        if ("10".equals(value)) {
            ArrayList rowList = Lists.newArrayListWithExpectedSize((int)entryEntity.size());
            for (int i = 0; i < entryEntity.size(); ++i) {
                rowList.add(i);
            }
            this.deleteEntry(rowList, Lists.newArrayList());
        } else {
            Map<String, Object> responseMap = this.getResponseMap("isLoad");
            responseMap.put("data", true);
            this.setCustomControlData(responseMap);
            entryEntity.clear();
            this.getModel().updateEntryCache(entryEntity);
            this.getView().updateView("entryentitylabelvalue");
        }
        this.setFieldCaption(value);
        this.setVisibleByType();
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String property = beforeF7SelectEvent.getProperty().toString();
        if ("group".equals(property)) {
            this.beforeF7SelectEventGroup(beforeF7SelectEvent);
        }
    }

    private void beforeF7SelectEventGroup(BeforeF7SelectEvent beforeF7SelectEvent) {
        ArrayList<QFilter> qFilterList = new ArrayList<QFilter>();
        QFilter leafFilter = new QFilter("isleaf", "=", (Object)"1");
        qFilterList.add(leafFilter);
        beforeF7SelectEvent.setCustomQFilters(qFilterList);
    }

    private Map<String, Map<String, Map<String, Object>>> setExprCache() {
        Long id = (Long)this.getModel().getValue("id");
        Map groupExprMap = null;
        if (id != null && !CollectionUtils.isEmpty((Map)(groupExprMap = labelServiceHelper.getGroupExprMap(id)))) {
            this.getPageCache().put("expression", SerializationUtils.toJsonString((Object)groupExprMap));
        }
        return groupExprMap;
    }

    private String transferToExpression(String index, String lblObjId) {
        Map groupExprCache;
        Map groupResultMap;
        String groupExprCacheStr = this.getPageCache().get("expression");
        String expression = null;
        if (!HRStringUtils.isEmpty((String)groupExprCacheStr) && !CollectionUtils.isEmpty((Map)(groupResultMap = (Map)(groupExprCache = (Map)SerializationUtils.fromJsonString((String)groupExprCacheStr, Map.class)).get(index)))) {
            HashMap exprMap = Maps.newHashMapWithExpectedSize((int)groupResultMap.size());
            Map oldExprMap = (Map)groupResultMap.get(lblObjId);
            if (!CollectionUtils.isEmpty((Map)oldExprMap)) {
                exprMap.put(lblObjId, (String)oldExprMap.get("expr"));
            }
            expression = SerializationUtils.toJsonString((Object)exprMap);
        }
        return expression;
    }

    private void setCustomControlData(Object data) {
        CustomControl customControl = (CustomControl)this.getControl("customcontrolap");
        customControl.setData(data);
    }

    private void setVisibleByType() {
        String type = (String)this.getModel().getValue("type");
        DynamicObjectCollection col = (DynamicObjectCollection)this.getModel().getValue("labelobj");
        if ("10".equals(type)) {
            if (!CollectionUtils.isEmpty((Collection)col)) {
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"flexpanelap1"});
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap11"});
                this.getPageCache().put("isShowValue", Boolean.TRUE.toString());
            } else if (this.getPageCache().get("isShowValue") != null) {
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"flexpanelap1"});
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap11"});
            }
        } else if ("20".equals(type)) {
            if (!CollectionUtils.isEmpty((Collection)col)) {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap1"});
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"flexpanelap11"});
                this.getPageCache().put("isShowValue", Boolean.TRUE.toString());
            } else if (this.getPageCache().get("isShowValue") != null) {
                this.getView().setVisible(Boolean.valueOf(false), new String[]{"flexpanelap1"});
                this.getView().setVisible(Boolean.valueOf(true), new String[]{"flexpanelap11"});
            }
        }
    }

    private void loadByFactType() {
        String configType = (String)this.getModel().getValue("configtype");
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentitylabelvalue");
        Map<String, Map<String, Map<String, Object>>> exprGroupMap = null;
        HashMap serviceGroupMap = null;
        if ("1".equals(configType)) {
            exprGroupMap = this.setExprCache();
            this.getPageCache().put("configtype", "1");
        } else {
            List lblValueIdList = entryEntity.stream().map(entry -> entry.getLong("id")).collect(Collectors.toList());
            DynamicObject[] rulesByLabelValue = labelServiceHelper.getRulesByLabelValue(lblValueIdList);
            serviceGroupMap = Maps.newHashMapWithExpectedSize((int)rulesByLabelValue.length);
            for (DynamicObject lblVR : rulesByLabelValue) {
                long lblVId = lblVR.getLong("labelvalue.id");
                Map sonGroup = serviceGroupMap.getOrDefault(lblVId, Maps.newHashMapWithExpectedSize((int)rulesByLabelValue.length));
                long lblObjId = lblVR.getLong("labelobject.id");
                String service = lblVR.getString("service");
                sonGroup.put(lblObjId, service);
                serviceGroupMap.put(lblVId, sonGroup);
            }
        }
        DynamicObjectCollection entryEntityRange = this.getModel().getEntryEntity("entryentityrange");
        ArrayList dataList = Lists.newArrayListWithExpectedSize((int)entryEntity.size());
        int i = 0;
        for (DynamicObject labelValue : entryEntity) {
            HashMap rowData = Maps.newHashMapWithExpectedSize((int)4);
            ILocaleString labelValueStr = labelValue.getLocaleString("labelvalue");
            ILocaleString labelValueDesc = labelValue.getLocaleString("labelvaluedesc");
            HashMap labelValueMap = Maps.newHashMapWithExpectedSize((int)2);
            labelValueMap.put("name", labelValueStr);
            labelValueMap.put("id", labelValue.getLong("id") + "");
            rowData.put("labelvalue", labelValueMap);
            rowData.put("labelvaluedesc", labelValueDesc);
            ArrayList labelObjectList = Lists.newArrayListWithExpectedSize((int)entryEntityRange.size());
            ArrayList exprList = Lists.newArrayListWithExpectedSize((int)entryEntityRange.size());
            ArrayList serviceList = Lists.newArrayListWithExpectedSize((int)entryEntityRange.size());
            for (DynamicObject labelObject : entryEntityRange) {
                Map serviceGroup;
                String service;
                HashMap labelObjectMap = Maps.newHashMapWithExpectedSize((int)2);
                labelObjectMap.put("id", labelObject.getString("labelobject.id"));
                labelObjectMap.put("name", labelObject.getLocaleString("labelobject.name"));
                labelObjectList.add(labelObjectMap);
                if (exprGroupMap != null) {
                    Map<String, Map<String, Object>> exprMap = exprGroupMap.get(i + "");
                    Map<String, Object> expr = exprMap.get(labelObject.getString("labelobject.id"));
                    String disPlayExpr = (String)expr.get("displayfunctiontext");
                    exprList.add(disPlayExpr);
                    continue;
                }
                if (serviceGroupMap == null || HRStringUtils.isEmpty((String)(service = (String)(serviceGroup = (Map)serviceGroupMap.get(labelValue.getLong("id"))).get(labelObject.getLong("labelobject.id"))))) continue;
                serviceList.add(service);
            }
            rowData.put("labelobject", labelObjectList);
            rowData.put("expr", exprList);
            rowData.put("service", serviceList);
            dataList.add(rowData);
            ++i;
        }
        Map<String, Object> responseMap = this.getResponseMap("init");
        responseMap.put("data", dataList);
        BillOperationStatus billOperationStatus = ((BillShowParameter)this.getView().getFormShowParameter()).getBillStatus();
        responseMap.put("status", billOperationStatus.name());
        responseMap.put("configType", configType);
        this.setCustomControlData(responseMap);
    }

    private void updateControlData(boolean isSave) {
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentitylabelvalue");
        ArrayList dataList = Lists.newArrayListWithExpectedSize((int)entryEntity.size());
        for (DynamicObject labelValue : entryEntity) {
            HashMap rowData = Maps.newHashMapWithExpectedSize((int)4);
            HashMap labelValueMap = Maps.newHashMapWithExpectedSize((int)2);
            labelValueMap.put("id", labelValue.getString("id"));
            rowData.put("labelvalue", labelValueMap);
            dataList.add(rowData);
        }
        Map<String, Object> responseMap = this.getResponseMap("updateId");
        BillOperationStatus billOperationStatus = ((BillShowParameter)this.getView().getFormShowParameter()).getBillStatus();
        billOperationStatus = isSave ? BillOperationStatus.EDIT : billOperationStatus;
        responseMap.put("status", billOperationStatus.name());
        responseMap.put("data", dataList);
        this.setCustomControlData(responseMap);
    }

    private void sendActionByLabelObj(MulBasedataDynamicObjectCollection newCol) {
        ArrayList labelObjectList = Lists.newArrayListWithExpectedSize((int)newCol.size());
        for (DynamicObject dyn : newCol) {
            DynamicObject lblObj = dyn.getDynamicObject("fbasedataid");
            HashMap lblObjMap = Maps.newHashMapWithExpectedSize((int)2);
            lblObjMap.put("id", lblObj.getString("id"));
            lblObjMap.put("name", lblObj.getLocaleString("name"));
            labelObjectList.add(lblObjMap);
        }
        Map<String, Object> responseMap = this.getResponseMap("changeLabelObject");
        responseMap.put("labelobject", labelObjectList);
        this.setCustomControlData(responseMap);
    }

    private void deleteEntry(List<Integer> delDowList, List<Long> labelValueIdList) {
        Map<String, Object> responseMap = this.getResponseMap("deleteEntryConfirm");
        if (this.checkDeleteModeEntry(labelValueIdList)) {
            DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentitylabelvalue");
            for (int i = entryEntity.size() - 1; i >= 0; --i) {
                if (!delDowList.contains(i)) continue;
                entryEntity.remove(i);
            }
            this.getModel().updateEntryCache(entryEntity);
            String expression = this.getPageCache().get("expression");
            if (!HRStringUtils.isEmpty((String)expression)) {
                Map exprMap = (Map)SerializationUtils.fromJsonString((String)expression, Map.class);
                for (Integer row : delDowList) {
                    exprMap.remove(row.toString());
                }
                LinkedHashMap newExprMap = Maps.newLinkedHashMapWithExpectedSize((int)exprMap.size());
                int index = 0;
                for (Map.Entry map : exprMap.entrySet()) {
                    newExprMap.put(index + "", map.getValue());
                    ++index;
                }
                this.getPageCache().put("expression", exprMap.isEmpty() ? null : SerializationUtils.toJsonString((Object)newExprMap));
            }
            responseMap.put("isDelete", Boolean.TRUE);
        } else {
            responseMap.put("isDelete", Boolean.FALSE);
        }
        responseMap.put("row", delDowList);
        this.setCustomControlData(responseMap);
    }

    private boolean checkDeleteModeEntry(List<Long> labelValueIdList) {
        long cacheLabelId = this.getId();
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_labelpolicyrule");
        boolean exists = serviceHelper.isExists(new QFilter[]{new QFilter("label", "=", (Object)cacheLabelId), new QFilter("labelvalue", "in", labelValueIdList)});
        if (exists) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u6807\u7b7e\u5df2\u5728\u6807\u7b7e\u503c\u4e0a\u751f\u6210\u6253\u6807\u7b56\u7565\uff0c\u4e0d\u53ef\u5220\u9664", (String)"LabelPlugin_5", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return false;
        }
        return true;
    }

    private void clickExpr(String rowIndex, String lblObjId) {
        Long labelValueId;
        BillOperationStatus billOperationStatus;
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (OperationStatus.EDIT.equals((Object)status) && (BillOperationStatus.SUBMIT.equals((Object)(billOperationStatus = ((BillShowParameter)this.getView().getFormShowParameter()).getBillStatus())) || BillOperationStatus.AUDIT.equals((Object)billOperationStatus))) {
            status = OperationStatus.VIEW;
        }
        OperationStatus finalStatus = status;
        DynamicObject labelValue = this.getModel().getEntryRowEntity("entryentitylabelvalue", Integer.parseInt(rowIndex));
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.setFormId("hrcs_labelvalueruleconf");
        showParameter.setStatus(finalStatus);
        HashMap<String, Object> params = new HashMap<String, Object>(16);
        Long labelId = this.getId();
        DynamicObjectCollection entryEntityRange = (DynamicObjectCollection)this.getModel().getDataEntity(true).get("entryentityrange");
        if (!this.validateEntityRange(entryEntityRange)) {
            return;
        }
        List labelObjectList = entryEntityRange.stream().filter(er -> HRStringUtils.equals((String)er.getString("labelobject.id"), (String)lblObjId)).map(dynamicObject -> dynamicObject.getDynamicObject("labelobject")).collect(Collectors.toList());
        params.put("label", labelId);
        params.put("labelobject", SerializationUtils.serializeToBase64(labelObjectList));
        Long l = labelValueId = labelValue == null ? null : Long.valueOf(labelValue.getLong("id"));
        if (labelValueId != null && labelValueId == 0L) {
            labelValueId = null;
        }
        params.put("labelvalue", labelValueId);
        String index = rowIndex + "";
        String expression = this.transferToExpression(index, lblObjId);
        params.put("expression", expression);
        params.put("index", index);
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        showParameter.setCustomParams(params);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "cacheExpr"));
        this.getView().showForm(showParameter);
    }

    private Map<String, Object> getResponseMap(String method) {
        HashMap map = Maps.newHashMapWithExpectedSize((int)16);
        map.put("method", method);
        map.put("random", System.currentTimeMillis());
        return map;
    }

    private void setFieldCaption(String type) {
        String des;
        String name;
        DynamicObjectCollection coll = this.getModel().getDataEntity(true).getDynamicObjectCollection("entryentitylabelvalue");
        DynamicObjectType item = coll.getDynamicObjectType();
        DynamicProperty property = item.getProperty("labelvalue");
        if ("20".equals(type)) {
            name = ResManager.loadKDString((String)"\u6807\u7b7e\u503c\u89c4\u5219\u540d\u79f0", (String)"LabelPlugin_9", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            des = ResManager.loadKDString((String)"\u6807\u7b7e\u503c\u89c4\u5219\u91ca\u4e49", (String)"LabelPlugin_12", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        } else {
            name = ResManager.loadKDString((String)"\u6807\u7b7e\u503c", (String)"LabelPlugin_10", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            des = ResManager.loadKDString((String)"\u6807\u7b7e\u503c\u91ca\u4e49", (String)"LabelPlugin_11", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        }
        property.getDisplayName().setLocaleValue(name);
        DynamicProperty property2 = item.getProperty("labelvaluedesc");
        property2.getDisplayName().setLocaleValue(des);
        EntryGrid entryGrid = (EntryGrid)this.getControl("entryentitylabelvalue");
        entryGrid.setColumnProperty("labelvalue", "header", (Object)new LocaleString(name));
        entryGrid.setColumnProperty("labelvaluedesc", "header", (Object)new LocaleString(des));
    }

    private boolean isValueEmpty(String oldType) {
        DynamicObjectCollection entity = this.getModel().getEntryEntity("entryentitylabelvalue");
        boolean isEmpty = true;
        if ("10".equals(oldType)) {
            for (DynamicObject dynamicObject : entity) {
                String lblValue = dynamicObject.getString("labelvalue");
                String lblValueDesc = dynamicObject.getString("labelvaluedesc");
                if (HRStringUtils.isEmpty((String)lblValue) && HRStringUtils.isEmpty((String)lblValueDesc)) continue;
                isEmpty = false;
                break;
            }
        } else {
            Map groupExprCache;
            String exprBean;
            for (DynamicObject dynamicObject : entity) {
                String lblValue = dynamicObject.getString("labelvalue");
                String lblValueDesc = dynamicObject.getString("labelvaluedesc");
                if (HRStringUtils.isEmpty((String)lblValue) && HRStringUtils.isEmpty((String)lblValueDesc)) continue;
                isEmpty = false;
                break;
            }
            if (!HRStringUtils.isEmpty((String)(exprBean = this.getPageCache().get("expression"))) && !CollectionUtils.isEmpty((Map)(groupExprCache = (Map)SerializationUtils.fromJsonString((String)exprBean, Map.class)))) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }
}
