/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.CodeEdit
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.hr.hbp.business.service.formula.entity.item.FunctionItem
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.bussiness.formula.function.FuncDefineSplitHelper
 *  kd.hr.hrcs.common.constants.function.FunctionConstants
 *  kd.hr.hrcs.common.util.CodeUtils
 */
package kd.hr.hrcs.formplugin.web.function;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.CodeEdit;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.hr.hbp.business.service.formula.entity.item.FunctionItem;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.bussiness.formula.function.FuncDefineSplitHelper;
import kd.hr.hrcs.common.constants.function.FunctionConstants;
import kd.hr.hrcs.common.util.CodeUtils;

public final class FunctionEdit
extends HRDataBaseEdit
implements FunctionConstants {
    private static final Log LOGGER = LogFactory.getLog(FunctionEdit.class);

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        this.addItemClickListeners(new String[]{"tbmain"});
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        this.getModel().setDataChanged(false);
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        CodeEdit codeEdit = (CodeEdit)this.getView().getControl("codeeditap");
        String funcExp = this.getModel().getDataEntity().getString("funcexp");
        codeEdit.setText(funcExp);
        this.getModel().setDataChanged(false);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        if (HRStringUtils.equals((String)operate.getOperateKey(), (String)"save") || "onlinetestfunc".equals(operate.getOperateKey())) {
            CodeEdit codeEdit = (CodeEdit)this.getView().getControl("codeeditap");
            try {
                String funcExp = FuncDefineSplitHelper.formatMethodExp((String)codeEdit.getText().trim());
                this.getModel().setValue("funcexp", (Object)funcExp);
            }
            catch (Exception e) {
                this.getView().showErrorNotification(e.getMessage());
                args.setCancel(true);
                return;
            }
            if (HRStringUtils.equals((String)operate.getOperateKey(), (String)"save")) {
                String uniqueCode;
                Long id = (Long)this.getModel().getDataEntity().getPkValue();
                if (Objects.isNull(id) || 0L == id) {
                    this.getModel().setValue("id", (Object)ORM.create().genLongId((IDataEntityType)this.getModel().getDataEntityType()));
                }
                if (HRStringUtils.isEmpty((String)(uniqueCode = (String)this.getModel().getValue("uniquecode")))) {
                    this.getModel().setValue("uniquecode", this.getModel().getDataEntity().getPkValue());
                }
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        OperationResult operationResult = args.getOperationResult();
        if (HRStringUtils.equals((String)operate.getOperateKey(), (String)"deleteentry_params") || HRStringUtils.equals((String)operate.getOperateKey(), (String)"moveentryup_params") || HRStringUtils.equals((String)operate.getOperateKey(), (String)"moveentrydown_params")) {
            this.createFunctionCode();
        } else if (HRStringUtils.equals((String)operate.getOperateKey(), (String)"save")) {
            CodeEdit codeEdit = (CodeEdit)this.getView().getControl("codeeditap");
            String funcExp = this.getModel().getDataEntity().getString("funcexp");
            codeEdit.setText(funcExp);
        } else if ("onlinetestfunc".equals(operate.getOperateKey()) && operationResult.isSuccess()) {
            this.openFunctionTestAp();
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        String fieldKey;
        switch (fieldKey = e.getProperty().getName()) {
            case "define": 
            case "funcdatatype": 
            case "paramdatatype": {
                this.createFunctionCode();
                break;
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private void createFunctionCode() {
        DynamicObject data = this.getModel().getDataEntity();
        String funcName = data.getString("define");
        String dataType = data.getString("funcdatatype");
        CodeEdit codeEdit = (CodeEdit)this.getView().getControl("codeeditap");
        if (StringUtils.isEmpty((CharSequence)funcName) || StringUtils.isEmpty((CharSequence)dataType)) {
            return;
        }
        if ("DateTime".equalsIgnoreCase(dataType)) {
            dataType = "Date";
        }
        StringBuilder funcCode = new StringBuilder();
        funcCode.append("public ").append(dataType).append(' ').append(funcName).append('(');
        DynamicObjectCollection parameterList = this.getModel().getEntryEntity("params");
        String parameterType = null;
        String parameterName = null;
        if (parameterList != null && parameterList.size() > 0) {
            int seq = 0;
            for (DynamicObject parameter : parameterList) {
                parameterType = parameter.getString("paramdatatype");
                seq = parameter.getInt("seq");
                parameterName = "param" + seq;
                if (HRStringUtils.isEmpty((String)parameterType)) continue;
                if ("DateTime".equalsIgnoreCase(parameterType)) {
                    parameterType = "Date";
                }
                if (seq == parameterList.size()) {
                    funcCode.append(parameterType).append(' ').append(parameterName);
                    continue;
                }
                funcCode.append(parameterType).append(' ').append(parameterName).append(", ");
            }
        }
        String funcExp = codeEdit.getText();
        funcCode.append(") {").append('\n');
        if (StringUtils.isEmpty((CharSequence)funcExp)) {
            funcCode.append(' ').append("return").append(" null;").append('\n');
            funcCode.append('}');
        } else {
            funcCode.append(' ').append(FuncDefineSplitHelper.createMethodExp((String)funcExp));
        }
        codeEdit.setText(FuncDefineSplitHelper.formatMethodExp((String)funcCode.toString()));
    }

    @ExcludeFromJacocoGeneratedReport
    public void beforeClosed(BeforeClosedEvent e) {
        String funcExp;
        CodeEdit codeEdit = (CodeEdit)this.getView().getControl("codeeditap");
        String value = codeEdit.getText();
        if (!HRStringUtils.equals((String)value, (String)(funcExp = this.getModel().getDataEntity().getString("funcexp")))) {
            this.getModel().setValue("funcexp", (Object)value);
            e.setCancel(true);
            OperateOption operateOption = OperateOption.create();
            this.getView().invokeOperation("close", operateOption);
        }
    }

    private Set<String> getImportPackageSet() {
        HashSet<String> importCodes = new HashSet<String>();
        DynamicObjectCollection parameterList = this.getModel().getEntryEntity("params");
        if (parameterList != null && parameterList.size() > 0) {
            for (DynamicObject parameter : parameterList) {
                String parameterType = parameter.getString("paramdatatype");
                CodeUtils.getImportCodeByClassType((String)parameterType).ifPresent(importCodes::add);
            }
        }
        CodeUtils.getImportCodeByClassType((String)this.getModel().getDataEntity().getString("funcdatatype")).ifPresent(importCodes::add);
        DynamicObjectCollection custImportJarCodes = this.getModel().getEntryEntity("importentry");
        if (custImportJarCodes != null && custImportJarCodes.size() > 0) {
            for (DynamicObject custImport : custImportJarCodes) {
                String importJarCode = custImport.getString("importcode");
                if (!HRStringUtils.isNotEmpty((String)importJarCode)) continue;
                importCodes.add(importJarCode.trim());
            }
        }
        return importCodes;
    }

    private void openFunctionTestAp() {
        CodeEdit codeEdit = (CodeEdit)this.getView().getControl("codeeditap");
        String funcExp = FuncDefineSplitHelper.formatMethodExp((String)codeEdit.getText().trim());
        if (HRStringUtils.isEmpty((String)funcExp) || HRStringUtils.isEmpty((String)((String)this.getModel().getValue("define")))) {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u586b\u5199\u51fd\u6570\u4f53\u540e\u518d\u8fdb\u884c\u5728\u7ebf\u8c03\u8bd5\u51fd\u6570\u3002", (String)"FunctionEdit_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            return;
        }
        FormShowParameter form = new FormShowParameter();
        form.setFormId("hrcs_onlinetestfunction");
        form.setCustomParam("functionItem", (Object)FunctionItem.getFunctionItem((IDataModel)this.getModel()));
        form.setCustomParam("funcExp", (Object)funcExp);
        form.setCustomParam("importPackages", this.getImportPackageSet());
        form.getOpenStyle().setShowType(ShowType.Modal);
        this.getView().showForm(form);
    }
}
