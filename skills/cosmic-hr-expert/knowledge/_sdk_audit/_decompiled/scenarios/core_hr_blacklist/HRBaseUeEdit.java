/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.metadata.dao.MetaCategory
 *  kd.bos.metadata.dao.MetadataReader
 *  kd.hr.hbp.business.application.impl.common.HrEntityCommonService
 *  kd.hr.hbp.common.constants.HRBaseConstants
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hbp.formplugin.web.util.HRBaseDataUtils
 */
package kd.hr.hbp.formplugin.web.template;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.FormShowParameter;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.metadata.dao.MetaCategory;
import kd.bos.metadata.dao.MetadataReader;
import kd.hr.hbp.business.application.impl.common.HrEntityCommonService;
import kd.hr.hbp.common.constants.HRBaseConstants;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hbp.formplugin.web.util.HRBaseDataUtils;

public final class HRBaseUeEdit
extends HRDataBaseEdit {
    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
        FormShowParameter showParameter = e.getFormShowParameter();
        if (HRBaseDataUtils.skipControlPageLabel((FormShowParameter)showParameter)) {
            return;
        }
        MetadataReader metadataReader = new MetadataReader();
        String formId = showParameter.getFormId();
        String formIdStr = metadataReader.loadIdByNumber(formId, MetaCategory.Entity);
        if (HRStringUtils.isEmpty((String)formIdStr)) {
            return;
        }
        if (HRStringUtils.isNotEmpty((String)formId) && showParameter instanceof BillShowParameter) {
            List parentEntity = HrEntityCommonService.getInstance().getParentEntity(formId);
            List templateDlgList = Arrays.stream(HRBaseConstants.templateDlg).collect(Collectors.toList());
            for (String templateDlgStr : templateDlgList) {
                if (!parentEntity.contains(templateDlgStr)) continue;
                return;
            }
            LocaleString caption = showParameter.getFormConfig().getCaption();
            if (showParameter.getStatus() == OperationStatus.ADDNEW) {
                String tittle = String.format(ResManager.loadKDString((String)"\u65b0\u589e%s", (String)"HRBaseUeEdit_0", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]), caption);
                showParameter.setCaption(tittle);
            }
        }
    }
}
