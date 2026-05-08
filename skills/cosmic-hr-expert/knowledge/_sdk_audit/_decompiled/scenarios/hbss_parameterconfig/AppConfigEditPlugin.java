/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.devportal.BizAppServiceHelp
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hbss.bussiness.config.HRBdWhiteListServiceHelper
 *  kd.hr.hbss.bussiness.config.HRConfigServiceHelper
 *  org.apache.commons.collections.CollectionUtils
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hr.hbss.formplugin.web.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.devportal.BizAppServiceHelp;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hbss.bussiness.config.HRBdWhiteListServiceHelper;
import kd.hr.hbss.bussiness.config.HRConfigServiceHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public final class AppConfigEditPlugin
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    public static final String ENABLESTATUS = "enablestatus";
    public static final String AUDITCHECK = "auditcheck";
    public static final String CHANGECHECK = "changecheck";
    public static final String BASEDATAFIELD = "basedatafield";
    public static final String ISOPEN = "isopen";
    public static final String APP = "app";

    @ExcludeFromJacocoGeneratedReport
    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String propertyName = args.getProperty().getName();
        ChangeData[] changeSet = args.getChangeSet();
        if (ISOPEN.equalsIgnoreCase(propertyName)) {
            String isopen = (String)changeSet[0].getNewValue();
            if (StringUtils.equals((CharSequence)isopen, (CharSequence)"0")) {
                this.getView().setEnable(Boolean.FALSE, new String[]{ENABLESTATUS, AUDITCHECK, CHANGECHECK});
            } else {
                this.getView().setEnable(Boolean.TRUE, new String[]{ENABLESTATUS, AUDITCHECK, CHANGECHECK});
            }
        } else if (BASEDATAFIELD.equalsIgnoreCase(propertyName)) {
            DynamicObject appDynamicObject = (DynamicObject)this.getModel().getValue(BASEDATAFIELD);
            if (appDynamicObject != null) {
                this.getView().setEnable(Boolean.TRUE, new String[]{ENABLESTATUS, AUDITCHECK, CHANGECHECK});
                this.getModel().setValue(AUDITCHECK, (Object)Boolean.FALSE);
                this.getModel().setValue(ENABLESTATUS, (Object)"1");
                this.getModel().setValue(CHANGECHECK, (Object)Boolean.TRUE);
                String appId = BizAppServiceHelp.getAppIdByFormNum((String)appDynamicObject.getString("number"));
                this.getModel().setValue(APP, (Object)appId);
                this.overWhiteConf(appDynamicObject);
            } else {
                this.getModel().setValue(APP, null);
            }
        }
    }

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        BasedataEdit basedataEdit = (BasedataEdit)this.getView().getControl(BASEDATAFIELD);
        basedataEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        DynamicObject appDynamicObject = (DynamicObject)this.getModel().getValue(BASEDATAFIELD);
        this.overWhiteConf(appDynamicObject);
    }

    @ExcludeFromJacocoGeneratedReport
    private void overWhiteConf(DynamicObject baseDataDyo) {
        if (baseDataDyo != null) {
            String entityNumber = baseDataDyo.getString("number");
            DynamicObject whiteListDyo = HRBdWhiteListServiceHelper.queryWhiteListByEntityNum((String)entityNumber);
            if (whiteListDyo == null) {
                return;
            }
            DynamicObjectCollection entryEntity = whiteListDyo.getDynamicObjectCollection("entryentity");
            for (DynamicObject dynamicObject : entryEntity) {
                String modifyEnable = dynamicObject.getString("modifyenable");
                String fieldName = HRBdWhiteListServiceHelper.getConfigField((String)dynamicObject.getString("paramtype"));
                this.getView().setEnable(Boolean.valueOf(!"0".equals(modifyEnable)), new String[]{fieldName});
            }
            Map paramByEntity = HRConfigServiceHelper.getParamByEntity((String)entityNumber);
            paramByEntity.forEach((key, val) -> this.getModel().setValue(key, val));
            this.getModel().setDataChanged(false);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        String fieldKey = beforeF7SelectEvent.getProperty().getName();
        if (HRStringUtils.equals((String)fieldKey, (String)BASEDATAFIELD)) {
            QFilter qFilter = new QFilter("modeltype", "=", (Object)"BaseFormModel");
            qFilter.and(new QFilter("bizappid.bizcloud.number", "in", AppConfigEditPlugin.getAllHRCloudIdInStr()));
            Set banBdNums = HRBdWhiteListServiceHelper.queryAllBanModifyBDNum();
            if (CollectionUtils.isNotEmpty((Collection)banBdNums)) {
                qFilter.and(new QFilter("id", "not in", (Object)banBdNums));
            }
            qFilter.and(new QFilter("id", "in", (Object)HRConfigServiceHelper.getAllExtHrmpEntNums()));
            ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
            showParameter.getListFilterParameter().getQFilters().add(qFilter);
        }
    }

    private static List<String> getAllHRCloudIdInStr() {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbss_cloud");
        DynamicObject[] clouds = serviceHelper.queryOriginalArray("cloud.number", null);
        return Arrays.stream(clouds).map(it -> it.getString("cloud.number")).collect(Collectors.toList());
    }
}
