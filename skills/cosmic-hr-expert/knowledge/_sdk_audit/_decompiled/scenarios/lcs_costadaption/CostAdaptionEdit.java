/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.IFormView
 *  kd.bos.form.container.Tab
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.mvc.SessionManager
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hrmp.lcs.formplugin.web.basedata;

import java.text.MessageFormat;
import java.util.EventObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.IFormView;
import kd.bos.form.container.Tab;
import kd.bos.form.field.BasedataEdit;
import kd.bos.mvc.SessionManager;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public class CostAdaptionEdit
extends HRDataBaseEdit {
    public void propertyChanged(PropertyChangedArgs e) {
        String name = e.getProperty().getName();
        if ("areatype".equals(name)) {
            this.countryTypeChanged(e.getChangeSet()[0].getNewValue());
        }
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        this.updateTabNameBySuffixValue();
    }

    public void afterBindData(EventObject e) {
        String countryType = this.getModel().getDataEntity().getString("areatype");
        this.updateCountryView(countryType);
    }

    private void countryTypeChanged(Object areatypeType) {
        if (null == areatypeType) {
            return;
        }
        String value = String.valueOf(areatypeType);
        this.updateCountryView(value);
    }

    private void updateCountryView(String areatypeTypeValue) {
        BasedataEdit control = (BasedataEdit)this.getControl("country");
        if (HRStringUtils.isEmpty((String)areatypeTypeValue) || "1".equals(areatypeTypeValue)) {
            control.setMustInput(false);
            this.getModel().setValue("country", null);
        }
        if ("2".equals(areatypeTypeValue)) {
            control.setMustInput(true);
        }
    }

    public void updateTabNameBySuffixValue() {
        String appId = this.getView().getFormShowParameter().getAppId();
        IFormView mainView = this.getView().getMainView();
        if (appId != null && mainView != null) {
            IFormView appView = SessionManager.getCurrent().getViewNoPlugin(this.getView().getFormShowParameter().getAppId() + mainView.getPageId());
            if (appView == null) {
                return;
            }
            Tab tab = (Tab)appView.getControl("_submaintab_");
            MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)this.getView().getEntityId());
            String displayName = mainEntityType.getDisplayName().getLocaleValue();
            long id = this.getView().getModel().getDataEntity().getLong("id");
            if (id == 0L) {
                displayName = MessageFormat.format(ResManager.loadKDString((String)"\u65b0\u589e{0}", (String)"CostAdaptionEdit_1", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]), displayName);
            } else {
                String suffixValue = this.getView().getModel().getDataEntity().getString("name");
                if (!ObjectUtils.isEmpty((Object)suffixValue)) {
                    displayName = displayName + " - " + suffixValue;
                }
            }
            if (tab != null) {
                tab.updateTabName(this.getView().getPageId(), displayName);
                this.getView().sendFormAction(appView);
            }
        }
    }
}
