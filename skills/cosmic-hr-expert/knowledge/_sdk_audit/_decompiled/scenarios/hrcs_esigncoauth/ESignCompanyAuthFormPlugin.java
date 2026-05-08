/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.service.esign.api.CorporateAuth
 *  kd.hr.hrcs.bussiness.service.esign.bo.ESignAppInfo
 *  kd.hr.hrcs.bussiness.service.esign.factory.BaseESignSrvFactory
 *  kd.hr.hrcs.bussiness.service.esign.factory.ServiceRouter
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignAppCfgServiceHelper
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper
 */
package kd.hr.hrcs.formplugin.web.econtract;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.service.esign.api.CorporateAuth;
import kd.hr.hrcs.bussiness.service.esign.bo.ESignAppInfo;
import kd.hr.hrcs.bussiness.service.esign.factory.BaseESignSrvFactory;
import kd.hr.hrcs.bussiness.service.esign.factory.ServiceRouter;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignAppCfgServiceHelper;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper;

public final class ESignCompanyAuthFormPlugin
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final Log logger = LogFactory.getLog(ESignCompanyAuthFormPlugin.class);

    public void registerListener(EventObject e) {
        super.registerListener(e);
        BasedataEdit authapp = (BasedataEdit)this.getControl("authapp");
        authapp.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void propertyChanged(PropertyChangedArgs pca) {
        String fieldName = pca.getProperty().getName();
        ChangeData[] changeSet = pca.getChangeSet();
        if ("authapp".equals(fieldName)) {
            DynamicObject authapp = (DynamicObject)changeSet[0].getNewValue();
            if (authapp == null) {
                return;
            }
            ESignAppInfo eSignSPAppInfo = HRCSESignAppCfgServiceHelper.getESignAppInfo((Object)authapp.getPkValue());
            this.getModel().setValue("esignspmgr", (Object)eSignSPAppInfo.getSpId());
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)"save", (CharSequence)formOperate.getOperateKey())) {
            DynamicObject authApp = (DynamicObject)this.getModelVal("authapp");
            DynamicObject lawEntity = (DynamicObject)this.getModelVal("lawentity");
            if (authApp != null && lawEntity != null) {
                ESignAppInfo eSignSPAppInfo = HRCSESignAppCfgServiceHelper.getESignAppInfo((Object)authApp.getPkValue());
                if (lawEntity.getPkValue().toString().equals(eSignSPAppInfo.getCorporateId())) {
                    this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"%1$s\u5df2\u5b8c\u6210\u4f01\u4e1a\u6388\u6743\uff0c\u65e0\u9700\u91cd\u590d\u6388\u6743\u3002", (String)"ESignCompanyAuthFormPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), lawEntity.getString("name")));
                    args.setCancel(true);
                }
            }
            DynamicObject esignspmgr = (DynamicObject)this.getModelVal("esignspmgr");
            String thirdCompanyId = (String)this.getModelVal("thirdcompanyid");
            if (authApp != null && HRStringUtils.isNotEmpty((String)thirdCompanyId)) {
                ESignAppInfo authAppInfo = HRCSESignAppCfgServiceHelper.getESignAppInfo((Object)authApp.getPkValue());
                if (esignspmgr != null) {
                    BaseESignSrvFactory baseESignSrvFactory = ServiceRouter.getSrvFactory((Long)esignspmgr.getLong("id"), (Long)Long.valueOf(authAppInfo.getCorporateId()));
                    CorporateAuth initCorporateAuth = baseESignSrvFactory.getCorporateAuth();
                    HashMap param = Maps.newHashMapWithExpectedSize((int)16);
                    param.put("openCorpId", thirdCompanyId);
                    Map result = initCorporateAuth.queryCorporateAuth((Map)param);
                    if (result == null) {
                        return;
                    }
                    logger.info("query result ={}", (Object)result);
                    String code = (String)result.get("code");
                    if ("210032".equals(code)) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u201c\u7b2c\u4e09\u65b9\u4f01\u4e1aID\u201d\u5728\u7b2c\u4e09\u65b9\u7cfb\u7edf\u4e0d\u5b58\u5728\uff0c\u8bf7\u68c0\u67e5\u662f\u5426\u51c6\u786e\u6216\u6e05\u7a7a\u4fdd\u5b58\u540e\u70b9\u51fb\u201c\u4f01\u4e1a\u6388\u6743\u201d\u83b7\u53d6\u201c\u7b2c\u4e09\u65b9\u4f01\u4e1aID\u201d\u3002", (String)"ESignCompanyAuthFormPlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
                        args.setCancel(true);
                        return;
                    }
                    if (!result.containsKey("bindingStatus")) {
                        return;
                    }
                    String bindingStatus = (String)result.get("bindingStatus");
                    if ("authorized".equals(bindingStatus)) {
                        this.setModelVal("authstatus", "1");
                    }
                }
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String property = evt.getProperty().getName();
        if (HRStringUtils.equals((String)"authapp", (String)property)) {
            ListShowParameter showParameter = (ListShowParameter)evt.getFormShowParameter();
            DynamicObjectCollection allEnableAppInfos = HRCSESignSPMgrServiceHelper.getAllEnableAppInfos();
            if (!CollectionUtils.isEmpty((Collection)allEnableAppInfos)) {
                List collect = allEnableAppInfos.stream().map(info -> info.getLong("bdesignappcfg.id")).collect(Collectors.toList());
                showParameter.getListFilterParameter().setFilter(new QFilter("id", "not in", collect));
            }
        }
    }
}
