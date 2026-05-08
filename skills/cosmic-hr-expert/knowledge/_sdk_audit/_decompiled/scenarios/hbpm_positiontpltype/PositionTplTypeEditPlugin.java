/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.Row
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.IFormView
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplTypeRepository
 *  kd.hrmp.hbpm.common.util.PositionTplUtil
 */
package kd.hrmp.hbpm.formplugin.web.position;

import java.util.EventObject;
import java.util.Iterator;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.IFormView;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplTypeRepository;
import kd.hrmp.hbpm.common.util.PositionTplUtil;

public final class PositionTplTypeEditPlugin
extends HRDataBaseEdit {
    private static Log logger = LogFactory.getLog(PositionTplTypeEditPlugin.class);
    private static final Integer DEFAULT_INDEX = 10;

    public void afterBindData(EventObject e) {
        Integer index;
        super.afterBindData(e);
        DynamicObject basedataEntity = this.getView().getModel().getDataEntity();
        String enable = basedataEntity.getString("enable");
        if (HRStringUtils.equals((String)enable, (String)"0")) {
            ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
            this.getView().setVisible(Boolean.FALSE, new String[]{"btnsave"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"btncancel"});
            this.getView().setVisible(Boolean.TRUE, new String[]{"btnclose"});
        }
        if (HRObjectUtils.isEmpty((Object)(index = Integer.valueOf(this.getModel().getDataEntity().getInt("index")))) || index.equals(0)) {
            try (DataSet rows = PositionTplTypeRepository.getInstance().queryOneIndexByIndexDesc();){
                Iterator iterator = rows.iterator();
                if (iterator.hasNext()) {
                    Row row = (Row)iterator.next();
                    Integer indexDb = row.getInteger("index");
                    this.getModel().setValue("index", (Object)(indexDb + DEFAULT_INDEX));
                } else {
                    this.getModel().setValue("index", (Object)DEFAULT_INDEX);
                }
                this.getModel().setDataChanged(false);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        if (null != args.getOperationResult() && !args.getOperationResult().isSuccess()) {
            boolean indexErrorExist;
            FormOperate formOperate = (FormOperate)args.getSource();
            OperateOption option = formOperate.getOption();
            boolean nameErrorExist = option.containsVariable("nameError");
            if (nameErrorExist) {
                String displayErrorInfo = ResManager.loadKDString((String)"\u5df2\u5b58\u5728\u540d\u79f0\u76f8\u540c\u4e14\u53ef\u7528\u7684\u5c97\u4f4d\u6a21\u677f\u7c7b\u578b\u3002", (String)"PositionTplTypeEditPlugin_0", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
                PositionTplUtil.isNameEnableRe((IFormView)this.getView(), (String)"name", (String)displayErrorInfo);
            }
            if (indexErrorExist = option.containsVariable("indexError")) {
                String indexErrorInfo = ResManager.loadKDString((String)"\u6570\u636e\u5df2\u5b58\u5728", (String)"PositionTplTypeEditPlugin_1", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
                PositionTplUtil.isNameEnableRe((IFormView)this.getView(), (String)"index", (String)indexErrorInfo);
            }
        }
    }
}

