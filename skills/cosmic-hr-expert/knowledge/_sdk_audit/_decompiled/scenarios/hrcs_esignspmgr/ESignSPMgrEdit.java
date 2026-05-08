/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.BeforeDeleteRowEventArgs
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.AbstractGrid$GridState
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.HyperLinkClickEvent
 *  kd.bos.form.events.HyperLinkClickListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.metadata.dao.MetaCategory
 *  kd.bos.metadata.dao.MetadataDao
 *  kd.bos.metadata.form.ControlAp
 *  kd.bos.metadata.form.FormMetadata
 *  kd.bos.orm.ORM
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.common.util.ReflectUtil
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.service.esign.constant.ESignSPMgrEditPage
 *  kd.hr.hrcs.bussiness.service.esign.enu.ServiceEnum
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignAppCfgServiceHelper
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignCOSealMgrServiceHelper
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignDBServiceServiceHelper
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper
 *  kd.hr.hrcs.formplugin.common.ComPrompts
 *  kd.hr.hrcs.formplugin.common.HrcsFormpluginRes
 *  org.apache.commons.lang3.ObjectUtils
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hrcs.formplugin.web.esign;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.BeforeDeleteRowEventArgs;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.CloseCallBack;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.ShowType;
import kd.bos.form.container.Tab;
import kd.bos.form.control.AbstractGrid;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.HyperLinkClickEvent;
import kd.bos.form.events.HyperLinkClickListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.metadata.dao.MetaCategory;
import kd.bos.metadata.dao.MetadataDao;
import kd.bos.metadata.form.ControlAp;
import kd.bos.metadata.form.FormMetadata;
import kd.bos.orm.ORM;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.common.util.ReflectUtil;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.service.esign.constant.ESignSPMgrEditPage;
import kd.hr.hrcs.bussiness.service.esign.enu.ServiceEnum;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignAppCfgServiceHelper;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignCOSealMgrServiceHelper;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignDBServiceServiceHelper;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper;
import kd.hr.hrcs.formplugin.common.ComPrompts;
import kd.hr.hrcs.formplugin.common.HrcsFormpluginRes;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public final class ESignSPMgrEdit
extends HRDataBaseEdit
implements ESignSPMgrEditPage,
HyperLinkClickListener {
    public void afterCreateNewData(EventObject e) {
        super.afterCreateNewData(e);
        int[] rowIndex = this.getModel().batchCreateNewEntryRow("entryentity", 13);
        this.initEntry(rowIndex[0], ServiceEnum.CORPORATE_AUTH);
        this.initEntry(rowIndex[1], ServiceEnum.CORPORATE_SEAL_QUERY);
        this.initEntry(rowIndex[2], ServiceEnum.CORPORATE_SEAL_CUD);
        this.initEntry(rowIndex[3], ServiceEnum.ACROSS_CORPORATE_AUTH);
        this.initEntry(rowIndex[4], ServiceEnum.INITIATE_SIGN_TASK);
        this.initEntry(rowIndex[5], ServiceEnum.OBTAIN_SIGN_LINK);
        this.initEntry(rowIndex[6], ServiceEnum.CORP_SIGN_TASK);
        this.initEntry(rowIndex[7], ServiceEnum.QUERY_SIGN_TASK);
        this.initEntry(rowIndex[8], ServiceEnum.DOWNLOAD_SIGN_FILE);
        this.initEntry(rowIndex[9], ServiceEnum.CANCEL_SIGN_TASK);
        this.initEntry(rowIndex[10], ServiceEnum.DISUSE_SIGN_TASK);
        this.initEntry(rowIndex[11], ServiceEnum.CALLBACK_EVENT);
        this.initEntry(rowIndex[12], ServiceEnum.BATCH_INITIATE_SIGN_TASK);
    }

    public void beforeBindData(EventObject e) {
        String showAppTab;
        IFormView view = this.getView();
        IDataModel model = this.getModel();
        view.setVisible(Boolean.valueOf(false), new String[]{"flexpanelapnotcfg"});
        view.setVisible(Boolean.valueOf(true), new String[]{"tabap"});
        boolean isSysPreset = (Boolean)model.getValue("issyspreset");
        if (isSysPreset) {
            view.setEnable(Boolean.valueOf(false), new String[]{"entryentity"});
        }
        if ("showapptab".equals(showAppTab = this.getView().getParentView().getPageCache().get("showapptab"))) {
            boolean isAddNew;
            boolean bl = isAddNew = OperationStatus.ADDNEW == this.getView().getFormShowParameter().getStatus();
            if (!isAddNew) {
                Tab tab = (Tab)this.getView().getControl("tabap");
                tab.activeTab("tabappcfg");
            }
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        IFormView view = this.getView();
        IDataModel model = this.getModel();
        switch (operate.getOperateKey()) {
            case "save": {
                if (this.checkImplementClass()) break;
                args.setCancel(true);
                return;
            }
            case "dodebug": {
                String name = ((OrmLocaleValue)model.getValue("name")).getLocaleValue();
                DynamicObjectCollection entryEntitys = model.getEntryEntity("entryentity");
                HRCSESignSPMgrServiceHelper.doDebug((String)name, (DynamicObjectCollection)entryEntitys, (IFormView)view);
                args.setCancel(true);
                break;
            }
            case "doentrybar_new": {
                List<String> errMsgs = this.getErrMsgs();
                if (CollectionUtils.isNotEmpty(errMsgs)) {
                    String title = ResManager.loadKDString((String)"\u8bf7\u5148\u914d\u7f6e\u670d\u52a1\u7c7b\u6ce8\u518c\u3002", (String)HrcsFormpluginRes.ESignSPMgrEdit_1.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                    this.getView().showConfirm(title, String.join((CharSequence)System.lineSeparator(), errMsgs), MessageBoxOptions.None, ConfirmTypes.Default, null);
                    args.setCancel(true);
                    return;
                }
                this.showESignAppCfg(null);
                args.setCancel(true);
                break;
            }
            case "deleteentry": {
                EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity1");
                int[] entrySelRowIndexs = entryGrid.getSelectRows();
                this.checkAppCfgInfo(model, entrySelRowIndexs, args);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        String operateKey;
        super.afterDoOperation(afterDoOperationEventArgs);
        OperationResult operationResult = afterDoOperationEventArgs.getOperationResult();
        boolean isSuccess = false;
        if (operationResult != null) {
            isSuccess = operationResult.isSuccess();
        }
        AbstractOperate op = (AbstractOperate)afterDoOperationEventArgs.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "save": {
                IPageCache pageCache;
                String addAppEntryIds;
                if (!isSuccess || !HRStringUtils.isNotBlank((CharSequence)(addAppEntryIds = (pageCache = this.getView().getPageCache()).get("addAppEntryIds")))) break;
                pageCache.remove("addAppEntryIds");
            }
        }
    }

    public void beforeDeleteRow(BeforeDeleteRowEventArgs e) {
        super.beforeDeleteRow(e);
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (!OperationStatus.ADDNEW.equals((Object)status) && !OperationStatus.EDIT.equals((Object)status)) {
            return;
        }
        IPageCache pageCache = this.getView().getPageCache();
        String addAppEntryIds = pageCache.get("addAppEntryIds");
        if (HRStringUtils.isBlank((CharSequence)addAppEntryIds)) {
            return;
        }
        Set addAppEntryIdSet = Arrays.stream(addAppEntryIds.split(",")).map(Long::parseLong).collect(Collectors.toSet());
        HashSet<Long> delTempAppEntryIds = new HashSet<Long>(8);
        EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity1");
        int[] selectRows = entryGrid.getSelectRows();
        DynamicObjectCollection entity = this.getModel().getEntryEntity("entryentity1");
        if (selectRows != null && selectRows.length > 0) {
            for (int selectRow : selectRows) {
                DynamicObject entryEntity = (DynamicObject)entity.get(selectRow);
                Long appId = entryEntity.getLong("bdesignappcfg_id");
                if (!addAppEntryIdSet.contains(appId)) continue;
                delTempAppEntryIds.add(appId);
            }
            if (CollectionUtils.isNotEmpty(delTempAppEntryIds)) {
                HRCSESignDBServiceServiceHelper.eSignAppCfgService.delete(delTempAppEntryIds.toArray());
            }
        }
    }

    private boolean checkAppCfgInfo(IDataModel model, int[] entrySelRowIndexs, boolean enable) {
        int dealNum = entrySelRowIndexs.length;
        if (dealNum == 0) {
            this.getView().showTipNotification(ComPrompts.noSelected());
            return false;
        }
        return enable ? this.checkEnable(model, entrySelRowIndexs, dealNum) : this.checkDisable(model, entrySelRowIndexs, dealNum);
    }

    private boolean checkEnable(IDataModel model, int[] entrySelRowIndexs, int dealNum) {
        String errTemplate = ComPrompts.isEnabled();
        HashMap<String, HashSet<Integer>> repeatAppId = new HashMap<String, HashSet<Integer>>(dealNum);
        ArrayList<String> errMsgs = new ArrayList<String>(dealNum);
        for (int entrySelRowIndex : entrySelRowIndexs) {
            DynamicObject appCfg = (DynamicObject)model.getValue("bdesignappcfg", entrySelRowIndex);
            String appId = appCfg.getString("appid");
            HashSet<Integer> appIdRowIdxs = (HashSet<Integer>)repeatAppId.get(appId);
            if (appIdRowIdxs == null) {
                appIdRowIdxs = new HashSet<Integer>(2);
            }
            appIdRowIdxs.add(entrySelRowIndex);
            repeatAppId.put(appId, appIdRowIdxs);
        }
        for (int entrySelRowIndex : entrySelRowIndexs) {
            StringBuilder errMsg = new StringBuilder();
            String enableFlagVal = (String)model.getValue("enable1", entrySelRowIndex);
            if ("1".equals(enableFlagVal)) {
                errMsg.append(errTemplate);
            }
            DynamicObject appCfg = (DynamicObject)model.getValue("bdesignappcfg", entrySelRowIndex);
            DynamicObject spDyn = appCfg.getDynamicObject("esignsp");
            String appId = appCfg.getString("appid");
            String enableAppNumber = HRCSESignAppCfgServiceHelper.getEnableAppNumber((String)appId, (Object)spDyn.get("id"));
            if (StringUtils.isNotBlank((CharSequence)enableAppNumber)) {
                String appNumber = appCfg.getString("number");
                if (appNumber.equals(enableAppNumber)) continue;
                if (errMsg.length() > 0) {
                    errMsg.append(",");
                }
                errMsg.append(String.format(ResManager.loadKDString((String)"\u540c\u4e00\u4e2aAppId\u53ea\u5141\u8bb8\u6709\u4e00\u6761\u542f\u7528\u7684\u5e94\u7528\uff0c\u201c%s\u201d\u5df2\u542f\u7528\u3002", (String)HrcsFormpluginRes.ESignSPMgrEdit_2.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), enableAppNumber));
                continue;
            }
            Set appIdRowIdxs = (Set)repeatAppId.get(appId);
            if (appIdRowIdxs.size() > 1) {
                Set repeatAppIds = appIdRowIdxs.stream().filter(item -> !item.equals(entrySelRowIndex)).map(item -> {
                    DynamicObject appCfgTemp = (DynamicObject)model.getValue("bdesignappcfg", entrySelRowIndex);
                    return appCfgTemp.getString("number");
                }).collect(Collectors.toSet());
                if (errMsg.length() > 0) {
                    errMsg.append(",");
                }
                errMsg.append(String.format(ResManager.loadKDString((String)"\u540c\u4e00\u4e2aAppId\u53ea\u5141\u8bb8\u6709\u4e00\u6761\u542f\u7528\u7684\u5e94\u7528\uff0c\u4e0d\u5141\u8bb8\u4e0e\u201c%s\u201d\u540c\u65f6\u542f\u7528\u3002", (String)HrcsFormpluginRes.ESignSPMgrEdit_3.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), String.join((CharSequence)",", repeatAppIds)));
            }
            if (errMsg.length() <= 0) continue;
            errMsg.insert(0, appCfg.getString("number") + "\uff1a");
            errMsgs.add(errMsg.toString());
        }
        int failSize = errMsgs.size();
        if (failSize == 0) {
            return true;
        }
        if (dealNum == 1) {
            this.getView().showTipNotification((String)errMsgs.get(0));
        } else {
            String title = ComPrompts.EnableTitle((int)dealNum, (int)(dealNum - failSize), (int)failSize);
            this.getView().showConfirm(title, String.join((CharSequence)System.lineSeparator(), errMsgs), MessageBoxOptions.None, ConfirmTypes.Default, null);
        }
        return false;
    }

    private boolean checkDisable(IDataModel model, int[] entrySelRowIndexs, int dealNum) {
        ArrayList<String> failNumbers = new ArrayList<String>(8);
        for (int entrySelRowIndex : entrySelRowIndexs) {
            String enableFlagVal = (String)model.getValue("enable1", entrySelRowIndex);
            if (!"0".equals(enableFlagVal)) continue;
            DynamicObject appCfg = (DynamicObject)model.getValue("bdesignappcfg", entrySelRowIndex);
            failNumbers.add(appCfg.getString("number"));
        }
        int failSize = failNumbers.size();
        if (failSize == 0) {
            return true;
        }
        String errTemplate = ResManager.loadKDString((String)"%s\uff1a\u6570\u636e\u5df2\u4e3a\u7981\u7528\u72b6\u6001\u3002", (String)HrcsFormpluginRes.ESignSPMgrEdit_4.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        if (dealNum == 1) {
            this.getView().showTipNotification(String.format(errTemplate, failNumbers.get(0)));
        } else {
            ArrayList errMsg = new ArrayList(failNumbers.size());
            failNumbers.forEach(item -> errMsg.add(String.format(errTemplate, item)));
            String title = ComPrompts.DisableTitle((int)dealNum, (int)(dealNum - failSize), (int)failSize);
            this.getView().showConfirm(title, String.join((CharSequence)System.lineSeparator(), errMsg), MessageBoxOptions.None, ConfirmTypes.Default, null);
        }
        return false;
    }

    private boolean checkAppCfgInfo(IDataModel model, int[] entrySelRowIndexs, BeforeDoOperationEventArgs args) {
        int dealNum = entrySelRowIndexs.length;
        if (dealNum == 0) {
            this.getView().showTipNotification(ComPrompts.noSelected());
            return false;
        }
        HashSet cancelRows = Sets.newHashSetWithExpectedSize((int)dealNum);
        Object spId = model.getValue("id");
        ArrayList<String> failNumbers = new ArrayList<String>(8);
        for (int entrySelRowIndex : entrySelRowIndexs) {
            DynamicObject appCfg = (DynamicObject)model.getValue("bdesignappcfg", entrySelRowIndex);
            boolean existsSeal = HRCSESignCOSealMgrServiceHelper.isExistsSeal((Object)spId, (Object)appCfg.get("corporate_id"));
            if (!existsSeal) continue;
            failNumbers.add(appCfg.getString("number"));
            cancelRows.add(entrySelRowIndex);
        }
        int failSize = failNumbers.size();
        if (failSize == 0) {
            return true;
        }
        String errTemplate = ResManager.loadKDString((String)"%1$s\uff1a\u5b58\u5728\u5f15\u7528\u4e0d\u80fd\u88ab\u5220\u9664\uff0c\u201c%2$s\u201d\u7684\u5b57\u6bb5\u201c%3$s\u201d\u5f15\u7528\u4e86\u6b64\u8d44\u6599\u6570\u636e\u3002", (String)HrcsFormpluginRes.ESignSPMgrEdit_5.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
        MainEntityType dataEntityType = MetadataServiceHelper.getDataEntityType((String)"hrcs_esigncosealmgr");
        String eSignCOSealMgrEntityName = dataEntityType.getDisplayName().getLocaleValue();
        MainEntityType entityType = EntityMetadataCache.getDataEntityType((String)"hrcs_esigncosealmgr");
        FormMetadata eSignCOSealMgrMeta = (FormMetadata)MetadataDao.readRuntimeMeta((String)MetadataDao.getIdByNumber((String)entityType.getName(), (MetaCategory)MetaCategory.Form), (MetaCategory)MetaCategory.Form);
        List items = eSignCOSealMgrMeta.getItems();
        Optional<ControlAp> tabCoSeal = items.stream().filter(item -> "tabcoseal".equals(item.getKey())).findFirst();
        String relEntityName = tabCoSeal.map(controlAp -> eSignCOSealMgrEntityName + "-" + controlAp.getName().getLocaleValue()).orElse(eSignCOSealMgrEntityName);
        MainEntityType eSignCOSealEntityType = MetadataServiceHelper.getDataEntityType((String)"hrcs_esigncoseal");
        String relPropName = eSignCOSealEntityType.getProperty("corporate").getDisplayName().getLocaleValue();
        int[] newSelectRows = Arrays.stream(entrySelRowIndexs).boxed().filter(index -> !cancelRows.contains(index)).mapToInt(Integer::valueOf).toArray();
        if (dealNum == 1) {
            this.getView().showTipNotification(String.format(errTemplate, failNumbers.get(0), relEntityName, relPropName));
        } else {
            String title = ComPrompts.DeleteTitle((int)dealNum, (int)(dealNum - failSize), (int)failSize);
            ArrayList<String> errMsg = new ArrayList<String>(failNumbers.size());
            for (String failNumber : failNumbers) {
                errMsg.add(String.format(errTemplate, failNumber, relEntityName, relPropName));
            }
            this.getView().showConfirm(title, String.join((CharSequence)System.lineSeparator(), errMsg), MessageBoxOptions.None, ConfirmTypes.Default, null);
        }
        EntryGrid appEntryentity = (EntryGrid)this.getControl("entryentity1");
        if (newSelectRows.length == 0) {
            args.setCancel(true);
        } else {
            AbstractGrid.GridState entryState = appEntryentity.getEntryState();
            entryState.selectRow(newSelectRows);
        }
        return false;
    }

    private boolean checkImplementClass() {
        List<String> errorMsg = this.getErrMsgs();
        if (CollectionUtils.isNotEmpty(errorMsg)) {
            this.getView().showTipNotification(StringUtils.join(errorMsg, (String)System.lineSeparator()));
            return false;
        }
        return true;
    }

    private List<String> getErrMsgs() {
        DynamicObjectCollection entryEntitys = this.getModel().getEntryEntity("entryentity");
        ArrayList<String> errorMsg = new ArrayList<String>(8);
        for (int i = 0; i < entryEntitys.size(); ++i) {
            DynamicObject entryEntity = (DynamicObject)entryEntitys.get(i);
            entryEntity.getDataEntityState().setBizChanged(true);
            entryEntity.getDataEntityState().setBizChangeFlags(new long[]{1L});
            String mustFlag = entryEntity.getString("mustflag");
            String plugin = entryEntity.getString("plugin");
            if (!"1".equals(mustFlag) && !StringUtils.isNotBlank((CharSequence)plugin)) continue;
            String interfaceClas = entryEntity.getString("interface");
            if (StringUtils.isBlank((CharSequence)plugin)) {
                ESignSPMgrEdit.addMustInputErrMsg(errorMsg, i, interfaceClas);
                continue;
            }
            try {
                Object obj = ReflectUtil.newInstance((String)plugin);
                Class classByInterFace = ServiceEnum.findClassByInterFace((String)interfaceClas);
                if (classByInterFace != null && classByInterFace.isAssignableFrom(obj.getClass())) continue;
                ESignSPMgrEdit.addErrMsg(errorMsg, i, interfaceClas);
                continue;
            }
            catch (Exception e) {
                ESignSPMgrEdit.addErrMsg(errorMsg, i, interfaceClas);
            }
        }
        return errorMsg;
    }

    public void showESignAppCfg(Object pkId) {
        BaseShowParameter showParameter = new BaseShowParameter();
        showParameter.setFormId("hrcs_esignappcfg");
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        if (pkId != null) {
            boolean isView;
            boolean bl = isView = OperationStatus.VIEW == this.getView().getFormShowParameter().getStatus();
            if (isView) {
                showParameter.setStatus(OperationStatus.VIEW);
            } else {
                showParameter.setStatus(OperationStatus.EDIT);
            }
            showParameter.setPkId(pkId);
        } else {
            IPageCache pageCache;
            String tempSpId;
            pkId = this.getModel().getValue("id");
            if ((ObjectUtils.isEmpty((Object)pkId) || (Long)pkId == 0L) && StringUtils.isBlank((CharSequence)(tempSpId = (pageCache = this.getPageCache()).get("temprelspid")))) {
                tempSpId = ORM.create().genStringId("hrcs_esignspmgr");
                pageCache.put("temprelspid", tempSpId);
            }
            showParameter.setStatus(OperationStatus.ADDNEW);
        }
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, "closeCallBack_save");
        showParameter.setCloseCallBack(closeCallBack);
        this.getView().showForm((FormShowParameter)showParameter);
    }

    private static void addErrMsg(List<String> errorMsg, int i, String interfaceClas) {
        String errMsg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7b2c%1$s\u884c\u63d2\u4ef6\u586b\u5199\u9519\u8bef\uff0c\u8bf7\u6b63\u786e\u586b\u5199\u63a5\u53e3\u201c%2$s\u201d\u7684\u5b9e\u73b0\u7c7b\u3002", (String)HrcsFormpluginRes.ESignSPMgrEdit_6.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), i + 1, interfaceClas);
        errorMsg.add(errMsg);
    }

    private static void addMustInputErrMsg(List<String> errorMsg, int i, String interfaceClas) {
        String errMsg = String.format(Locale.ROOT, ResManager.loadKDString((String)"\u7b2c%1$s\u884c\u63d2\u4ef6\u586b\u5199\u9519\u8bef\uff0c\u5fc5\u9700\u63a5\u53e3\u63d2\u4ef6\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a\uff0c\u8bf7\u586b\u5199\u63a5\u53e3\u201c%2$s\u201d\u7684\u5b9e\u73b0\u7c7b\u3002", (String)HrcsFormpluginRes.ESignSPMgrEdit_7.resId(), (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), i + 1, interfaceClas);
        errorMsg.add(errMsg);
    }

    private void initEntry(int rowIndex, ServiceEnum serviceEnum) {
        IDataModel model = this.getModel();
        model.setValue("srvname", (Object)serviceEnum.getSrvName(), rowIndex);
        model.setValue("srvdesc", (Object)serviceEnum.getSrvDesc(), rowIndex);
        model.setValue("interface", (Object)serviceEnum.getInterFace(), rowIndex);
        model.setValue("mustflag", (Object)serviceEnum.getMustFlag(), rowIndex);
        model.setValue("plugin", (Object)" ", rowIndex);
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        Object returnData = event.getReturnData();
        if (returnData == null) {
            return;
        }
        String actionId = event.getActionId();
        if ("closeCallBack_save".equals(actionId)) {
            IDataModel model = this.getModel();
            Map result = (Map)returnData;
            DynamicObjectCollection entryEntity = model.getEntryEntity("entryentity1");
            for (int i = 0; i < entryEntity.size(); ++i) {
                DynamicObject dynamicObject = (DynamicObject)entryEntity.get(i);
                long bdId = dynamicObject.getDynamicObject("bdesignappcfg").getLong("id");
                if (bdId != (Long)result.get("bdesignappcfg")) continue;
                for (Map.Entry item : result.entrySet()) {
                    model.setValue((String)item.getKey(), item.getValue(), i);
                }
                this.getView().updateView("entryentity1");
                return;
            }
            int entryRowCount = model.createNewEntryRow("entryentity1");
            DynamicObject entryRowEntity = model.getEntryRowEntity("entryentity1", entryRowCount);
            entryRowEntity.getDataEntityState().setBizChanged(true);
            entryRowEntity.getDataEntityState().setBizChangeFlags(new long[]{1L});
            for (Map.Entry item : result.entrySet()) {
                model.setValue((String)item.getKey(), item.getValue(), entryRowCount);
            }
        }
    }

    public void hyperLinkClick(HyperLinkClickEvent evt) {
        Optional<Long> pkId = Optional.ofNullable(this.getModel()).map(it -> it.getDataEntity(true)).map(it -> it.getDynamicObjectCollection("entryentity1")).map(it -> (DynamicObject)it.get(evt.getRowIndex())).map(it -> it.getDynamicObject("bdesignappcfg")).map(it -> it.getLong("id"));
        pkId.ifPresent(this::showESignAppCfg);
    }

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity1");
        entryGrid.addItemClickListener((ItemClickListener)this);
        entryGrid.addClickListener((ClickListener)this);
        entryGrid.addHyperClickListener((HyperLinkClickListener)this);
        Toolbar advConToolBarAp = (Toolbar)this.getControl("advcontoolbarap");
        advConToolBarAp.addItemClickListener((ItemClickListener)this);
    }

    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        IDataModel model = this.getModel();
        String itemKey = evt.getItemKey();
        EntryGrid entryGrid = (EntryGrid)this.getControl("entryentity1");
        int[] entrySelRowIndexs = entryGrid.getSelectRows();
        switch (itemKey) {
            case "entrybar_enable": {
                this.checkAppCfgInfo(model, entrySelRowIndexs, true);
                for (int entrySelRowIndex : entrySelRowIndexs) {
                    model.setValue("enable1", (Object)"1", entrySelRowIndex);
                }
                break;
            }
            case "entrybar_disable": {
                this.checkAppCfgInfo(model, entrySelRowIndexs, false);
                for (int entrySelRowIndex : entrySelRowIndexs) {
                    model.setValue("enable1", (Object)"0", entrySelRowIndex);
                }
                break;
            }
        }
    }
}
