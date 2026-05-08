/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.entity.Tips
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.events.CellClickEvent
 *  kd.bos.form.control.events.CellClickListener
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.field.FieldEdit
 *  kd.bos.form.field.MulBasedataEdit
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.common.cache.HRPageCache
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.sdk.hr.hpfs.common.constants.ChgActionNewConstants
 *  kd.sdk.hr.hpfs.common.enums.EmpposChgTypeEnum
 */
package kd.hr.hpfs.formplugin;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.entity.Tips;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.CellClickEvent;
import kd.bos.form.control.events.CellClickListener;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.FieldEdit;
import kd.bos.form.field.MulBasedataEdit;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.common.cache.HRPageCache;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.sdk.hr.hpfs.common.constants.ChgActionNewConstants;
import kd.sdk.hr.hpfs.common.enums.EmpposChgTypeEnum;

public final class ChgActionEditPlugin
extends HRDataBaseEdit
implements CellClickListener {
    private static final Set<String> ORIGINAL_CHG_TYPE_SET;
    private static final Set<String> TARGET_ASSIGN_CHG_TYPE_SET;
    private static final Set<String> TARGET_CHG_TYPE_SET;
    private static final Set<Long> INVALID_CATEGORY_SET;
    private static final Integer ORIGINAL_ROW;
    private static final Integer TARGET_ROW;
    private static final String CACHE_KEY_LAST_INITED_COMBO = "last_inited_combo";
    private HRPageCache hrPageCache;

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        EntryGrid entryGrid = (EntryGrid)this.getView().getControl("entryentity");
        entryGrid.addCellClickListener((CellClickListener)this);
    }

    public void propertyChanged(PropertyChangedArgs evt) {
        super.propertyChanged(evt);
        String fieldKey = evt.getProperty().getName();
        Object newValue = evt.getChangeSet()[0].getNewValue();
        IDataModel model = this.getModel();
        DynamicObject dy = model.getDataEntity();
        switch (fieldKey) {
            case "chgcategory": {
                if (newValue == null) break;
                Long chgEventId = dy.getLong("chgcategory.chgevent.id");
                if (ChgActionNewConstants.CHG_EVENT_INFO_MODIFY.equals(chgEventId) || ChgActionNewConstants.CHG_EVENT_INIT.equals(chgEventId)) {
                    model.setValue("fowtype", null, ORIGINAL_ROW.intValue());
                    model.setValue("fowtype", null, TARGET_ROW.intValue());
                    model.setValue("chgtype", null, ORIGINAL_ROW.intValue());
                    model.setValue("chgtype", null, TARGET_ROW.intValue());
                } else {
                    model.setValue("fowtype", (Object)dy.getString("chgcategory.beforeflowtype"), ORIGINAL_ROW.intValue());
                    model.setValue("fowtype", (Object)dy.getString("chgcategory.afterflowtype"), TARGET_ROW.intValue());
                }
                long chgCategoryId = dy.getLong("chgcategory.id");
                if (1060L == chgCategoryId) {
                    model.setValue("createnewassign", (Object)true);
                }
                this.handleInvalidAssignment(dy);
                if (!ChgActionNewConstants.CHG_EVENT_INFO_QUIT.equals(chgEventId)) break;
                model.setValue("createnewassign", (Object)false);
                break;
            }
            case "createnewassign": {
                Long chgEventId;
                if (((Boolean)newValue).booleanValue() && !"2".equals(model.getValue("chgtype", TARGET_ROW.intValue()))) {
                    model.setValue("chgtype", null, TARGET_ROW.intValue());
                }
                if (!ChgActionNewConstants.CHG_EVENT_INFO_TRANSFER.equals(chgEventId = Long.valueOf(dy.getLong("chgcategory.chgevent.id"))) && !ChgActionNewConstants.CHG_EVENT_INFO_EXTERNAL_REGULAR.equals(chgEventId)) break;
                model.setValue("invalidassign", newValue);
                break;
            }
        }
    }

    public void beforeBindData(EventObject evt) {
        super.beforeBindData(evt);
        EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity");
        this.addEntryTips(entryGrid, "chgtype");
        this.addEntryTips(entryGrid, "fowtype");
        MulBasedataEdit laborTypeEdit = (MulBasedataEdit)this.getControl("mulemplaborreltype");
        this.addTips((FieldEdit)laborTypeEdit, "mulemplaborreltype");
        MulBasedataEdit laborStatusEdit = (MulBasedataEdit)this.getControl("mulemplaborrelstatus");
        this.addTips((FieldEdit)laborStatusEdit, "mulemplaborrelstatus");
    }

    public void afterCreateNewData(EventObject evt) {
        super.afterCreateNewData(evt);
        IDataModel model = this.getModel();
        model.setValue("chgobject", (Object)"0", ORIGINAL_ROW.intValue());
        model.setValue("chgobject", (Object)"1", TARGET_ROW.intValue());
    }

    public void cellClick(CellClickEvent evt) {
        String fieldKey = evt.getFieldKey();
        if ("chgtype".equals(fieldKey)) {
            this.setChgTypeComboProp(evt.getRow());
        }
    }

    public void cellDoubleClick(CellClickEvent cellClickEvent) {
    }

    private void setChgTypeComboProp(int rowIndex) {
        Set<String> visiableItemSet;
        IDataModel model = this.getModel();
        HRPageCache cache = this.getHRPageCache();
        if (this.isNeedlessInitCombo(cache, rowIndex)) {
            return;
        }
        if (ORIGINAL_ROW == rowIndex) {
            visiableItemSet = ORIGINAL_CHG_TYPE_SET;
        } else if (TARGET_ROW == rowIndex) {
            boolean isAssgin = model.getDataEntity().getBoolean("createnewassign");
            visiableItemSet = isAssgin ? TARGET_ASSIGN_CHG_TYPE_SET : TARGET_CHG_TYPE_SET;
        } else {
            return;
        }
        EmpposChgTypeEnum[] chgTypeEnums = EmpposChgTypeEnum.values();
        ArrayList<ComboItem> comboItems = new ArrayList<ComboItem>(chgTypeEnums.length);
        for (EmpposChgTypeEnum item : chgTypeEnums) {
            ComboItem comboItem = new ComboItem();
            comboItem.setCaption(new LocaleString(item.getDesc()));
            String code = item.getCode();
            comboItem.setValue(code);
            comboItem.setItemVisible(visiableItemSet.contains(code));
            comboItems.add(comboItem);
        }
        ComboEdit comboEdit = (ComboEdit)this.getControl("chgtype");
        comboEdit.setComboItems(comboItems);
        cache.put(CACHE_KEY_LAST_INITED_COMBO, (Object)String.valueOf(rowIndex));
    }

    private boolean isNeedlessInitCombo(HRPageCache cache, int rowIndex) {
        String expectCacheValue = TARGET_ROW == rowIndex ? String.valueOf(rowIndex) + this.getModel().getDataEntity().getBoolean("createnewassign") : String.valueOf(rowIndex);
        return HRStringUtils.equals((String)expectCacheValue, (String)((String)cache.get(CACHE_KEY_LAST_INITED_COMBO, String.class)));
    }

    public HRPageCache getHRPageCache() {
        if (null == this.hrPageCache) {
            this.hrPageCache = new HRPageCache(this.getPageCache());
        }
        return this.hrPageCache;
    }

    private void handleInvalidAssignment(DynamicObject dy) {
        Long chgEventId = dy.getLong("chgcategory.chgevent.id");
        long chgCategoryId = dy.getLong("chgcategory.id");
        boolean isInvalidAssign = false;
        if (ChgActionNewConstants.CHG_EVENT_INFO_QUIT.equals(chgEventId) || ChgActionNewConstants.CHG_EVENT_INFO_RETIRE.equals(chgEventId)) {
            isInvalidAssign = true;
        } else if (ChgActionNewConstants.CHG_EVENT_INFO_TRANSFER.equals(chgEventId) || ChgActionNewConstants.CHG_EVENT_INFO_EXTERNAL_REGULAR.equals(chgEventId)) {
            if (dy.getBoolean("createnewassign")) {
                isInvalidAssign = true;
            }
        } else if (INVALID_CATEGORY_SET.contains(chgCategoryId)) {
            isInvalidAssign = true;
        }
        this.getModel().setValue("invalidassign", (Object)isInvalidAssign);
    }

    private void addTips(FieldEdit edit, String controlKey) {
        Tips tips = new Tips();
        List tipsStr = (List)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSService", (String)"queryPromptForString", (Object[])new Object[]{"hpfs_chgaction", controlKey, this.getModel().getDataEntity()});
        if (!CollectionUtils.isEmpty((Collection)tipsStr)) {
            tips.setContent(new LocaleString((String)tipsStr.get(0)));
            tips.setType("text");
            tips.setTriggerType("hover");
            tips.setIsConfirm(false);
            tips.setShowIcon(true);
            edit.addTips(tips);
        }
    }

    private void addEntryTips(EntryGrid entryGrid, String controlKey) {
        List tipsStr = (List)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSService", (String)"queryPromptForString", (Object[])new Object[]{"hpfs_chgaction", controlKey, this.getModel().getDataEntity()});
        if (!CollectionUtils.isEmpty((Collection)tipsStr)) {
            HashMap<String, Object> retMap = new HashMap<String, Object>(16);
            HashMap<String, List> contentMap = new HashMap<String, List>(16);
            contentMap.put("zh_CN", tipsStr);
            retMap.put("content", contentMap);
            retMap.put("type", "text");
            retMap.put("title", null);
            entryGrid.setColumnProperty(controlKey, "tips", retMap);
        }
    }

    static {
        INVALID_CATEGORY_SET = Collections.unmodifiableSet(Sets.newHashSet((Object[])new Long[]{ChgActionNewConstants.CHG_CATEGORY_PARTTIME_QUIT, ChgActionNewConstants.CHG_CATEGORY_SECONDMENT_QUIT, ChgActionNewConstants.CHG_CATEGORY_DISPATCH_QUIT, ChgActionNewConstants.CHG_CATEGORY_REEMPLOY_QUIT}));
        HashSet<String> originalChgTypeSet = new HashSet<String>();
        originalChgTypeSet.add("0");
        originalChgTypeSet.add("4");
        originalChgTypeSet.add("3");
        ORIGINAL_CHG_TYPE_SET = Collections.unmodifiableSet(originalChgTypeSet);
        HashSet<String> targetAssingChgTypeSet = new HashSet<String>();
        targetAssingChgTypeSet.add("2");
        TARGET_ASSIGN_CHG_TYPE_SET = Collections.unmodifiableSet(targetAssingChgTypeSet);
        HashSet<String> targetChgTypeSet = new HashSet<String>();
        targetChgTypeSet.add("2");
        targetChgTypeSet.add("3");
        TARGET_CHG_TYPE_SET = Collections.unmodifiableSet(targetChgTypeSet);
        ORIGINAL_ROW = 0;
        TARGET_ROW = 1;
    }
}
