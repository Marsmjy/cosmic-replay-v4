/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DataEntityState
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.EntityType
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.mvc.base.BaseModel
 *  kd.bos.permission.formplugin.util.PermFormCommonUtil
 *  kd.bos.util.CollectionUtils
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRCloseCheckFormPlugin
 *  kd.hr.hrcs.bussiness.service.esign.constant.ESignSPMgrEditPage
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignDBServiceServiceHelper
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper
 */
package kd.hr.hrcs.formplugin.web.esign;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DataEntityState;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.EntityType;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.mvc.base.BaseModel;
import kd.bos.permission.formplugin.util.PermFormCommonUtil;
import kd.bos.util.CollectionUtils;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRCloseCheckFormPlugin;
import kd.hr.hrcs.bussiness.service.esign.constant.ESignSPMgrEditPage;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignDBServiceServiceHelper;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper;

public final class ESignSPMgrCloseEdit
extends HRCloseCheckFormPlugin
implements ESignSPMgrEditPage {
    public void confirmCallBack(MessageBoxClosedEvent event) {
        if (HRStringUtils.equals((String)"page_close", (String)event.getCallBackId()) && MessageBoxResult.Yes.equals((Object)event.getResult())) {
            IDataModel model = this.getModel();
            IPageCache pageCache = this.getView().getPageCache();
            String addAppEntryIds = pageCache.get("addAppEntryIds");
            if (HRStringUtils.isNotBlank((CharSequence)addAppEntryIds)) {
                Set addAppEntryIdSet = Arrays.stream(addAppEntryIds.split(",")).map(Long::parseLong).collect(Collectors.toSet());
                Long pkValue = (Long)((BaseModel)model).getPKValue();
                if (pkValue == 0L) {
                    HRCSESignDBServiceServiceHelper.eSignAppCfgService.delete(addAppEntryIdSet.toArray());
                    this.closeConfirmStatus = true;
                    PermFormCommonUtil.closeClientForm((IFormView)this.getView());
                    return;
                }
                DynamicObjectCollection entryAppCfgDys = HRCSESignSPMgrServiceHelper.getESignSPAppInfos((Object)pkValue);
                Set<Object> dbAppEntryIds = entryAppCfgDys != null && entryAppCfgDys.size() > 0 ? entryAppCfgDys.stream().map(item -> item.getDynamicObject("bdesignappcfg").getLong("id")).collect(Collectors.toSet()) : new HashSet(0);
                Set<Object> tempDelAppEntryIds = new HashSet(8);
                DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("entryentity1");
                if (entryEntity != null && entryEntity.size() > 0) {
                    Set curAppEntryIds = entryEntity.stream().map(item -> item.getDynamicObject("bdesignappcfg").getLong("id")).collect(Collectors.toSet());
                    tempDelAppEntryIds.addAll(addAppEntryIdSet.stream().filter(item -> !curAppEntryIds.contains(item)).collect(Collectors.toSet()));
                    if (CollectionUtils.isNotEmpty(dbAppEntryIds)) {
                        tempDelAppEntryIds.addAll(addAppEntryIdSet.stream().filter(item -> !dbAppEntryIds.contains(item)).collect(Collectors.toSet()));
                    }
                } else if (CollectionUtils.isNotEmpty(dbAppEntryIds)) {
                    tempDelAppEntryIds = addAppEntryIdSet.stream().filter(item -> !dbAppEntryIds.contains(item)).collect(Collectors.toSet());
                }
                if (CollectionUtils.isNotEmpty(tempDelAppEntryIds)) {
                    HRCSESignDBServiceServiceHelper.eSignAppCfgService.delete(tempDelAppEntryIds.toArray());
                }
            }
            this.closeConfirmStatus = true;
            PermFormCommonUtil.closeClientForm((IFormView)this.getView());
        }
    }

    private Set<String> getDelAppEntryNumbers() {
        IDataModel model = this.getModel();
        boolean isUpd = model.getDataEntity().getDataEntityState().getFromDatabase();
        if (isUpd) {
            DynamicObjectCollection entryEntity = model.getEntryEntity("entryentity1");
            Set<Object> curAppEntryIds = entryEntity != null && entryEntity.size() > 0 ? entryEntity.stream().map(item -> item.getDynamicObject("bdesignappcfg").getLong("id")).collect(Collectors.toSet()) : new HashSet(0);
            Long pkValue = (Long)((BaseModel)model).getPKValue();
            DynamicObjectCollection entryAppCfgDys = HRCSESignSPMgrServiceHelper.getESignSPAppInfos((Object)pkValue);
            if (entryAppCfgDys != null && entryAppCfgDys.size() > 0) {
                Set dbEntryDyns = entryAppCfgDys.stream().map(item -> item.getDynamicObject("bdesignappcfg")).collect(Collectors.toSet());
                HashSet<String> delEntryNumbers = CollectionUtils.isNotEmpty(dbEntryDyns) ? dbEntryDyns.stream().filter(item -> !curAppEntryIds.contains(item.getLong("id"))).map(item -> item.getString("number")).collect(Collectors.toSet()) : new HashSet<String>(0);
                return delEntryNumbers;
            }
        }
        return new HashSet<String>(0);
    }

    protected List<String> getUnCheckFields() {
        return Lists.newArrayList((Object[])new String[]{"seq"});
    }

    protected String getDelEntryMsg(List<String> unCheckField, Map<String, EntityType> allEntities, String mainEntityNum, DataEntityState dataEntityState) {
        Set<Object> delAppEntryNumbers = new HashSet(8);
        for (Map.Entry<String, EntityType> entityTypeEntry : allEntities.entrySet()) {
            String key = entityTypeEntry.getKey();
            if (!HRStringUtils.equals((String)key, (String)"entryentity1")) continue;
            delAppEntryNumbers = this.getDelAppEntryNumbers();
            break;
        }
        if (HRCollUtil.isNotEmpty(delAppEntryNumbers)) {
            String entryDelMsg = ResManager.loadKDString((String)"%1$s\u5220\u9664\u4e86\u96c6\u6210\u5e94\u7528\u201c%2$s\u201d", (String)"ESignSPMgrCloseEdit_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
            String entryName = allEntities.get("entryentity1").getDisplayName().getLocaleValue();
            return String.format(entryDelMsg, entryName, String.join((CharSequence)"\uff0c", delAppEntryNumbers));
        }
        return "";
    }
}
