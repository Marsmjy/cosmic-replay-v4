/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicProperty
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.events.GetEntityTypeEventArgs
 *  kd.bos.entity.property.OrgProp
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.CellClickEvent
 *  kd.bos.form.control.events.CellClickListener
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.TextEdit
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.model.complexobj.FieldComplexType
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper
 *  kd.hr.hrcs.common.constants.earlywarn.WarnObjTplConstants
 *  org.apache.commons.lang.StringUtils
 */
package kd.hr.hrcs.formplugin.web.earlywarn.objecttpl;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.dynamicobject.DynamicProperty;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.events.GetEntityTypeEventArgs;
import kd.bos.entity.property.OrgProp;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.CellClickEvent;
import kd.bos.form.control.events.CellClickListener;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.TextEdit;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.service.labelandreport.AnalyseObjectUtil;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.model.complexobj.FieldComplexType;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.servicehelper.perm.dimension.EntityCtrlServiceHelper;
import kd.hr.hrcs.common.constants.earlywarn.WarnObjTplConstants;
import org.apache.commons.lang.StringUtils;

@ExcludeFromJacocoGeneratedReport
public final class WarnDataPermEdit
extends HRDataBaseEdit
implements CellClickListener,
WarnObjTplConstants {
    private static final Log LOGGER = LogFactory.getLog(WarnDataPermEdit.class);
    private static final String WARN_DATA_PERM_ENTRY_ENTITY_ROW_INDEX = "WARN_RC_PERM_ENTRY_ENTITY_ROW_INDEX";

    public void registerListener(EventObject e) {
        EntryGrid permEntryGrid;
        super.registerListener(e);
        TextEdit fieldNameEdit = (TextEdit)this.getView().getControl("warnfieldname");
        if (fieldNameEdit != null) {
            fieldNameEdit.addButtonClickListener((ClickListener)this);
        }
        if ((permEntryGrid = (EntryGrid)this.getView().getControl("warnpermentryentity")) != null) {
            permEntryGrid.addCellClickListener((CellClickListener)this);
        }
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        try {
            this.getModel().setDataChanged(false);
        }
        catch (Exception exception) {
            LOGGER.error("error:", (Throwable)exception);
        }
    }

    private List<Long> getPermDimensionIdList(List<Map<String, Object>> result) {
        ArrayList<Long> permDimensionIdList = new ArrayList<Long>(10);
        if (result != null && result.size() > 0) {
            result.forEach(item -> permDimensionIdList.add(Long.parseLong(String.valueOf(item.get("id")))));
        }
        return permDimensionIdList;
    }

    private void refreshWarnFormEntityEntry(String entityNumber, String propKey) {
        int idx2;
        List<Map<String, Object>> result = EntityCtrlServiceHelper.getCtrlDimensionByEntity((String)entityNumber, (String)propKey);
        LOGGER.info("refreshWarnFormEntityEntry_dimension_data_{}", (Object)result);
        result = result.stream().filter(en -> HRStringUtils.equalsIgnoreCase((String)"orgteam", (String)String.valueOf(en.get("datasource"))) && (((List)en.get("orgClassifys")).contains(1010L) || ((List)en.get("orgClassifys")).contains(1020L)) || StringUtils.equalsIgnoreCase((String)"basedata", (String)String.valueOf(en.get("datasource"))) && HRStringUtils.equalsIgnoreCase((String)"haos_adminorghrf7", (String)String.valueOf(en.get("entitytype")))).collect(Collectors.toList());
        List<Long> permDimensionIdList = this.getPermDimensionIdList(result);
        MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
        String permField = mainEntityType.getPermissionControlType().getDataDimensionField();
        if (kd.bos.util.StringUtils.isBlank((String)permField)) {
            permField = mainEntityType.getMainOrg();
        }
        HashMap<Object, Map<Object, Object>> permDimensionMap = new HashMap<Object, Map<Object, Object>>(16);
        if (kd.bos.util.StringUtils.isNotEmpty((String)permField)) {
            HashMap<String, String> dynoMap = new HashMap<String, String>(1);
            dynoMap.put("hbss_hrbucafunc", permField);
            permDimensionMap.put(1L, dynoMap);
        }
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.beginInit();
        ArrayList<Integer> needDeleteRows = new ArrayList<Integer>(10);
        DynamicObjectCollection dynColl = this.getModel().getEntryEntity("warnpermentryentity");
        if (dynColl != null && dynColl.size() > 0) {
            for (idx2 = 0; idx2 < dynColl.size(); ++idx2) {
                if (dynColl.get(idx2) == null || !permDimensionIdList.contains(((DynamicObject)dynColl.get(idx2)).getLong("warndataperm")) && 1L != ((DynamicObject)dynColl.get(idx2)).getLong("warndataperm")) {
                    needDeleteRows.add(idx2);
                    continue;
                }
                if (1L == ((DynamicObject)dynColl.get(idx2)).getLong("warndataperm")) {
                    if (!Objects.equals(mainEntityType.getProperty(permField).getDisplayName().getLocaleValue(), ((DynamicObject)dynColl.get(idx2)).getString("datapermname"))) {
                        needDeleteRows.add(idx2);
                        continue;
                    }
                    permField = "";
                    continue;
                }
                permDimensionIdList.remove(permDimensionIdList.indexOf(((DynamicObject)dynColl.get(idx2)).getLong("warndataperm")));
            }
        }
        needDeleteRows.forEach(idx -> this.getModel().deleteEntryRow("warnpermentryentity", idx.intValue()));
        dynColl = this.getModel().getEntryEntity("warnpermentryentity");
        for (idx2 = 0; idx2 < result.size(); ++idx2) {
            if (permDimensionIdList.size() > 0 && permDimensionIdList.contains(result.get(idx2).get("id"))) {
                DynamicObject newRow = dynColl.addNew();
                newRow.set("warnpermtype", (Object)"hrcs_dimension");
                newRow.set("warndataperm", result.get(idx2).get("id"));
                newRow.set("datapermname", result.get(idx2).get("name"));
            }
            permDimensionMap.put(result.get(idx2).get("id"), result.get(idx2));
        }
        if (kd.bos.util.StringUtils.isNotEmpty((String)permField)) {
            DynamicObject newRow = dynColl.addNew();
            newRow.set("warnpermtype", (Object)"hbss_hrbucafunc");
            newRow.set("warndataperm", (Object)1L);
            newRow.set("datapermname", (Object)mainEntityType.getProperty(permField).getDisplayName());
        }
        model.endInit();
        if (permDimensionMap.size() > 0) {
            this.getPageCache().put("warndataperm", SerializationUtils.toJsonString(permDimensionMap));
        }
        this.getView().updateView("warnpermentryentity");
    }

    public void cellClick(CellClickEvent cellClickEvent) {
        EntryGrid entryGrid;
        super.click((EventObject)cellClickEvent);
        if (cellClickEvent.getSource() instanceof EntryGrid && "warnpermentryentity".equals((entryGrid = (EntryGrid)cellClickEvent.getSource()).getEntryKey()) && "warnfieldname".equals(cellClickEvent.getFieldKey())) {
            this.getPageCache().put(WARN_DATA_PERM_ENTRY_ENTITY_ROW_INDEX, String.valueOf(cellClickEvent.getRow()));
            DynamicObjectCollection permEntryCol = this.getModel().getEntryEntity("warnpermentryentity");
            if (permEntryCol != null && permEntryCol.size() > cellClickEvent.getRow()) {
                DynamicObject permDyn = (DynamicObject)permEntryCol.get(cellClickEvent.getRow());
                this.setWarnScenePermField(permDyn);
            }
        }
    }

    public void cellDoubleClick(CellClickEvent cellClickEvent) {
    }

    private void setEntityFiled(Object returnData) {
        String idx = this.getPageCache().get(WARN_DATA_PERM_ENTRY_ENTITY_ROW_INDEX);
        if (returnData instanceof Map && HRStringUtils.isNotEmpty((String)idx)) {
            int rowIndex = Integer.parseInt(idx);
            Map fieldInfoMap = (Map)returnData;
            this.getView().getModel().setValue("warnfieldname", fieldInfoMap.get("originalName"), rowIndex);
            this.getView().getModel().setValue("warnentitynumber", fieldInfoMap.get("entityNumber"), rowIndex);
            this.getView().getModel().setValue("warnfieldalias", fieldInfoMap.get("fieldAlias"), rowIndex);
            this.getView().getModel().setValue("warnfieldpath", fieldInfoMap.get("fieldPath"), rowIndex);
            this.getView().getModel().setValue("warnvaluetype", fieldInfoMap.get("valueType"), rowIndex);
            this.getView().updateView("warnpermentryentity");
        }
    }

    public void click(EventObject evt) {
        super.click(evt);
        if (evt.getSource() instanceof TextEdit && evt.getSource() != null && "warnfieldname".equals(((TextEdit)evt.getSource()).getKey())) {
            this.openRcFieldF7();
        }
    }

    private void openRcFieldF7() {
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"hrcs_warnobjectfieldf7", (boolean)false, (int)0, (boolean)true);
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setMultiSelect(false);
        fsp.setShowTitle(false);
        fsp.setHasRight(true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "warnfieldname"));
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void setWarnScenePermField(DynamicObject permDimensionDyn) {
        Map permDimensionMapList;
        if (permDimensionDyn == null) {
            return;
        }
        String value = this.getView().getPageCache().get("warndataperm");
        if (HRStringUtils.isNotEmpty((String)value) && (permDimensionMapList = (Map)SerializationUtils.fromJsonString((String)value, Map.class)) != null && permDimensionMapList.containsKey(permDimensionDyn.getString("warndataperm"))) {
            Map permDimensionMap = (Map)permDimensionMapList.get(permDimensionDyn.getString("warndataperm"));
            value = this.getView().getPageCache().get("allQueryFieldTreeNodes");
            if (HRStringUtils.isEmpty((String)value)) {
                value = this.getView().getPageCache().get("allFieldTreeNodes");
            }
            if (HRStringUtils.isNotEmpty((String)value)) {
                List fieldList = (List)SerializationUtils.fromJsonString((String)value, List.class);
                ArrayList<Map> f7FieldList = new ArrayList<Map>(10);
                HashMap entityOrgMap = Maps.newHashMapWithExpectedSize((int)16);
                for (Map fieldMap : fieldList) {
                    boolean addFlag = false;
                    if (permDimensionMap.containsKey("hbss_hrbucafunc")) {
                        String number;
                        if (!AnalyseObjectUtil.isBaseDataType((String)String.valueOf(fieldMap.get("complexType"))) || !"bos_org".equals(String.valueOf(fieldMap.get("baseDataNum")))) continue;
                        String entityNumber = String.valueOf(fieldMap.get("entityNumber"));
                        String permField = (String)entityOrgMap.get(entityNumber);
                        if (!entityOrgMap.containsKey(entityNumber)) {
                            MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
                            permField = mainEntityType.getPermissionControlType().getDataDimensionField();
                            if (kd.bos.util.StringUtils.isBlank((String)permField)) {
                                permField = mainEntityType.getMainOrg();
                            }
                            entityOrgMap.put(entityNumber, permField);
                        }
                        if (kd.bos.util.StringUtils.isNotEmpty((String)permField) && (number = String.valueOf(fieldMap.get("fieldAlias"))).contains(permField)) {
                            addFlag = true;
                        }
                    } else if ("orgteam".equals(permDimensionMap.get("datasource"))) {
                        if (String.valueOf(fieldMap.get("key")).endsWith("boid") || String.valueOf(fieldMap.get("key")).endsWith(".id")) {
                            addFlag = true;
                        } else if (AnalyseObjectUtil.isBaseDataType((String)String.valueOf(fieldMap.get("complexType"))) && ("haos_adminorghrf7".equals(String.valueOf(fieldMap.get("baseDataNum"))) || "haos_projectteamhr".equals(String.valueOf(fieldMap.get("baseDataNum"))) || "haos_adminorgteam".equals(String.valueOf(fieldMap.get("baseDataNum"))))) {
                            addFlag = true;
                        } else if (FieldComplexType.ADMIN_ORG.getValue().equals(String.valueOf(fieldMap.get("complexType"))) && "01".equals(this.getOrgFuncs(String.valueOf(fieldMap.get("entityNumber")), String.valueOf(fieldMap.get("number"))))) {
                            addFlag = true;
                        }
                    } else if ("hrbu".equals(permDimensionMap.get("datasource"))) {
                        if (FieldComplexType.ADMIN_ORG.getValue().equals(String.valueOf(fieldMap.get("complexType")))) {
                            String orgFuncs = this.getOrgFuncs(String.valueOf(fieldMap.get("entityNumber")), String.valueOf(fieldMap.get("number")));
                            if (!"01".equals(orgFuncs) && orgFuncs != null && orgFuncs.equals(String.valueOf(permDimensionMap.get("bucafuncid")))) {
                                addFlag = true;
                            }
                        } else if (String.valueOf(fieldMap.get("key")).endsWith("boid") || String.valueOf(fieldMap.get("key")).endsWith(".id")) {
                            addFlag = true;
                        } else if (!AnalyseObjectUtil.isBaseDataType((String)String.valueOf(fieldMap.get("complexType")))) {
                            addFlag = true;
                        }
                    } else if ("basedata".equals(permDimensionMap.get("datasource"))) {
                        if ((FieldComplexType.BASE_DATA.getValue().equals(String.valueOf(fieldMap.get("complexType"))) || FieldComplexType.HIS_BASE_DATA.getValue().equals(String.valueOf(fieldMap.get("complexType")))) && String.valueOf(fieldMap.get("baseDataNum")).equals(permDimensionMap.get("entitytype"))) {
                            addFlag = true;
                        } else if (String.valueOf(fieldMap.get("key")).endsWith("boid") || String.valueOf(fieldMap.get("key")).endsWith(".id")) {
                            addFlag = true;
                        } else if (!AnalyseObjectUtil.isBaseDataType((String)String.valueOf(fieldMap.get("complexType")))) {
                            addFlag = true;
                        }
                    } else if ("enum".equals(permDimensionMap.get("datasource"))) {
                        if (String.valueOf(fieldMap.get("key")).endsWith("boid") || String.valueOf(fieldMap.get("key")).endsWith(".id")) {
                            addFlag = true;
                        } else if (!AnalyseObjectUtil.isBaseDataType((String)String.valueOf(fieldMap.get("complexType")))) {
                            addFlag = true;
                        }
                    }
                    if (!addFlag) continue;
                    f7FieldList.add(fieldMap);
                }
                this.getView().getPageCache().put("fieldname", SerializationUtils.toJsonString(f7FieldList));
            }
        }
    }

    private String getOrgFuncs(String entityNumber, String key) {
        String[] items = key.split("\\.");
        DynamicProperty dynamicProperty = EntityMetadataCache.getDataEntityType((String)entityNumber).getProperty(items[0]);
        if (dynamicProperty instanceof OrgProp) {
            return ((OrgProp)dynamicProperty).getOrgFunc();
        }
        return null;
    }

    public void getEntityType(GetEntityTypeEventArgs e) {
        super.getEntityType(e);
        MainEntityType originalEntityType = e.getOriginalEntityType();
        try {
            e.setNewEntityType((MainEntityType)originalEntityType.clone());
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        Object returnData = event.getReturnData();
        if (returnData == null) {
            return;
        }
        if ("warnfieldname".equals(event.getActionId())) {
            this.setEntityFiled(returnData);
        }
    }
}
