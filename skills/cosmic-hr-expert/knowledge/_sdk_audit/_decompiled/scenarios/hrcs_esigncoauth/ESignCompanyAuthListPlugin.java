/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrcs.bussiness.service.esign.api.CorporateAuth
 *  kd.hr.hrcs.bussiness.service.esign.bo.ESignAppInfo
 *  kd.hr.hrcs.bussiness.service.esign.factory.BaseESignSrvFactory
 *  kd.hr.hrcs.bussiness.service.esign.factory.ServiceRouter
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignAppCfgServiceHelper
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper
 */
package kd.hr.hrcs.formplugin.web.econtract;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.bussiness.service.esign.api.CorporateAuth;
import kd.hr.hrcs.bussiness.service.esign.bo.ESignAppInfo;
import kd.hr.hrcs.bussiness.service.esign.factory.BaseESignSrvFactory;
import kd.hr.hrcs.bussiness.service.esign.factory.ServiceRouter;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignAppCfgServiceHelper;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper;

public final class ESignCompanyAuthListPlugin
extends HRDataBaseList {
    public void filterContainerInit(FilterContainerInitArgs args) {
        DynamicObject onlineESignSP = HRCSESignSPMgrServiceHelper.getOnlineESignSP();
        if (onlineESignSP != null) {
            args.getFilterColumn("esignspmgr.name").setDefaultValue(onlineESignSP.getPkValue().toString());
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"companyauth")) {
            String bindingStatus;
            ListSelectedRowCollection listSelectedData = args.getListSelectedData();
            if (listSelectedData.size() > 1) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u4ec5\u80fd\u64cd\u4f5c\u4e00\u6761\u6570\u636e\u3002", (String)"MsgSubList_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                args.setCancel(true);
                return;
            }
            ListSelectedRow listSelectedRow = listSelectedData.get(0);
            HRBaseServiceHelper companyAuthHelp = new HRBaseServiceHelper("hrcs_esigncoauth");
            DynamicObject dynamicObject = companyAuthHelp.queryOne(listSelectedRow.getPrimaryKeyValue());
            String authStatus = dynamicObject.getString("authstatus");
            DynamicObject lawEntity = dynamicObject.getDynamicObject("lawentity");
            if (HRStringUtils.equals((String)"1", (String)authStatus)) {
                this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"%1$s\u5df2\u5b8c\u6210\u4f01\u4e1a\u6388\u6743\uff0c\u65e0\u9700\u91cd\u590d\u6388\u6743\u3002", (String)"ESignCompanyAuthFormPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), lawEntity.getString("name")));
                args.setCancel(true);
                return;
            }
            ESignAppInfo authapp = HRCSESignAppCfgServiceHelper.getESignAppInfo((Object)dynamicObject.getDynamicObject("authapp").getPkValue());
            BaseESignSrvFactory baseESignSrvFactory = ServiceRouter.getSrvFactory((Long)dynamicObject.getLong("esignspmgr.id"), (Long)Long.valueOf(authapp.getCorporateId()));
            CorporateAuth initCorporateAuth = baseESignSrvFactory.getCorporateAuth();
            if (initCorporateAuth == null) {
                this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u672a\u627e\u5230\u670d\u52a1\u5b9e\u73b0\u7c7b\u3002", (String)"ESignCompanyAuthFormPlugin_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), lawEntity.getString("name")));
                args.setCancel(true);
                return;
            }
            HashMap queryParam = Maps.newHashMapWithExpectedSize((int)16);
            queryParam.put("clientCorpId", listSelectedRow.getPrimaryKeyValue().toString());
            Map result = initCorporateAuth.queryCorporateAuth((Map)queryParam);
            if (result != null && result.containsKey("bindingStatus") && "authorized".equals(bindingStatus = (String)result.get("bindingStatus"))) {
                dynamicObject.set("authstatus", (Object)"1");
                dynamicObject.set("thirdcompanyid", result.get("openCorpId"));
                companyAuthHelp.updateOne(dynamicObject);
                return;
            }
            HashMap param = Maps.newHashMapWithExpectedSize((int)16);
            param.put("corporateId", listSelectedRow.getPrimaryKeyValue().toString());
            param.put("corpName", dynamicObject.getDynamicObject("lawentity").getString("firmname"));
            param.put("corpIdentNo", dynamicObject.getDynamicObject("lawentity").getString("uniformsocialcreditcode"));
            param.put("mobile", dynamicObject.getDynamicObject("lawentity").getString("phone"));
            param.put("legalRepName", dynamicObject.getDynamicObject("lawentity").getString("representative"));
            HashMap authRes = (HashMap)initCorporateAuth.corporateAuth((Map)param);
            this.getView().openUrl((String)authRes.get("authUrl"));
        }
    }
}
