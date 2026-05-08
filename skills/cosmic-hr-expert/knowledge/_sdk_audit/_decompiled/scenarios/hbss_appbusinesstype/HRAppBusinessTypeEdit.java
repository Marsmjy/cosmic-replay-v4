/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.QueryServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.hbss.formplugin.web.hrbu;

import java.util.Arrays;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class HRAppBusinessTypeEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        BasedataEdit fieldEdit = (BasedataEdit)this.getView().getControl("app");
        fieldEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        boolean isSysPreSet = this.getView().getModel().getDataEntity().getBoolean("issyspreset");
        if (!isSysPreSet) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"btnclose"});
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        DynamicObjectCollection docs;
        String fieldName = beforeF7SelectEvent.getProperty().getName();
        if ("app".equals(fieldName) && Objects.nonNull(docs = QueryServiceHelper.query((String)"hbss_appbusinesstype", (String)"app", null)) && docs.size() > 0) {
            HashSet<String> appIdSet = new HashSet<String>();
            for (DynamicObject doc : docs) {
                String appId = doc.getString("app");
                this.addAppIdSet(appIdSet, appId);
            }
            if (appIdSet.size() > 0) {
                HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbss_cloud");
                DynamicObject[] dynamicObjects = serviceHelper.queryOriginalArray("cloud", null);
                ListShowParameter showParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
                QFilter cloud = new QFilter("bizcloud", "in", Arrays.stream(dynamicObjects).map(el -> el.getString("cloud")).collect(Collectors.toSet()));
                showParameter.getListFilterParameter().getQFilters().add(cloud);
                showParameter.getListFilterParameter().getQFilters().add(new QFilter("id", "not in", (Object)appIdSet.toArray(new String[0])));
                showParameter.getListFilterParameter().getQFilters().add(new QFilter("type", "=", (Object)"0"));
            }
        }
    }

    private void addAppIdSet(Set<String> appIdSet, String appId) {
        if (HRStringUtils.isNotEmpty((String)appId)) {
            appIdSet.add(appId);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void afterCreateNewData(EventObject e) {
        Map customParams = this.getView().getFormShowParameter().getCustomParams();
        if (Objects.nonNull(customParams.get("primaryKeyValue"))) {
            HRBaseServiceHelper appBusinessHelper = new HRBaseServiceHelper("hbss_appbusinesstype");
            DynamicObject value = appBusinessHelper.queryOne(customParams.get("primaryKeyValue"));
            IDataModel model = this.getModel();
            model.setValue("businesstype", value.get("businesstype"));
        }
    }
}
