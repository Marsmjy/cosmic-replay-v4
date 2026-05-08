/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
 *  kd.bos.form.IPageCache
 *  kd.hr.hbp.business.service.labelandreport.FieldDefineService
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.model.complexobj.labelandreport.FieldTreeNode
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.common.constants.earlywarn.WarningSceneConstants
 *  kd.hr.hrcs.common.model.earlywarn.WarnJoinEntityBo
 *  kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo
 *  kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterLeftTreeEditPlugin
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scene;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import java.util.List;
import kd.bos.form.IPageCache;
import kd.hr.hbp.business.service.labelandreport.FieldDefineService;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.model.complexobj.labelandreport.FieldTreeNode;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.common.constants.earlywarn.WarningSceneConstants;
import kd.hr.hrcs.common.model.earlywarn.WarnJoinEntityBo;
import kd.hr.hrcs.common.model.earlywarn.WarnQueryFieldBo;
import kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterLeftTreeEditPlugin;

@ExcludeFromJacocoGeneratedReport
public final class WarnSceneAdFilterLeftTreeEditPlugin
extends WarnAdFilterLeftTreeEditPlugin
implements WarningSceneConstants {
    protected List<FieldTreeNode> getTreeFieldNodes() {
        IPageCache pageCache = this.getView().getPageCache();
        String queryFieldsStr = pageCache.get("queryFields");
        String entitiesStr = pageCache.get("joinEntities");
        if (HRStringUtils.isNotEmpty((String)queryFieldsStr) && HRStringUtils.isNotEmpty((String)entitiesStr)) {
            List entityBos = JSON.parseArray((String)entitiesStr, WarnJoinEntityBo.class);
            FieldDefineService fieldDefineService = new FieldDefineService();
            fieldDefineService.setConcatFieldDisplayName(false);
            return fieldDefineService.getEntityAllFields(entityBos, JSON.parseArray((String)queryFieldsStr, WarnQueryFieldBo.class));
        }
        return Lists.newArrayListWithExpectedSize((int)0);
    }

    protected String getCalFieldCacheKey() {
        return "calculateFields";
    }
}
