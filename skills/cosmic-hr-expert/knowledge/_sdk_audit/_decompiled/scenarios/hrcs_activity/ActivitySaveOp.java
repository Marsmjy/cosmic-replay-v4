/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.ILocaleString
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.cache.helper.AppHelper
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.TimeServiceHelper
 *  kd.bos.servicehelper.operation.OperationServiceHelper
 *  kd.bos.servicehelper.workflow.WorkflowServiceHelper
 *  kd.bos.workflow.api.NodeTemplate
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.opplugin.validator.activity.ActivitySaveValidator
 */
package kd.hr.hrcs.opplugin.web.activity;

import java.util.ArrayList;
import java.util.HashMap;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.cache.helper.AppHelper;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.TimeServiceHelper;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.bos.servicehelper.workflow.WorkflowServiceHelper;
import kd.bos.workflow.api.NodeTemplate;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.opplugin.validator.activity.ActivitySaveValidator;

public final class ActivitySaveOp
extends HRDataBaseOp {
    private static final String WF_HR_GROUP_ID = "1392710806595991552";

    private static String getProperties(String name, String number, String actID, String nodeDoc) {
        return "{\"name\": \"" + name + "\",\"customParams\": [{\"number\":\"" + number + "\",\"name\":\"" + name + "\",\"id\":\"" + actID + "\",\"value\": \"" + nodeDoc + "\",\"builtIn\":" + true + "}]}";
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        e.addValidator((AbstractValidator)new ActivitySaveValidator());
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        DynamicObject[] dyArr = e.getDataEntities();
        ArrayList<DynamicObject> actTransRecSet = new ArrayList<DynamicObject>(dyArr.length);
        for (DynamicObject actInfo : dyArr) {
            if ("1".equals(actInfo.get("enable"))) {
                DynamicObject enableRec = BusinessDataServiceHelper.newDynamicObject((String)"hrcs_activityenablerec");
                enableRec.set("creator", (Object)RequestContext.get().getCurrUserId());
                enableRec.set("createtime", (Object)TimeServiceHelper.now());
                enableRec.set("activity", (Object)actInfo);
                actTransRecSet.add(enableRec);
            }
            if (!actInfo.getDataEntityState().getFromDatabase()) {
                this.addWFNodeTemplate(actInfo);
                continue;
            }
            this.updateWFNodeTemplate(actInfo);
        }
        OperationServiceHelper.executeOperate((String)"submit", (String)"hrcs_activityenablerec", (DynamicObject[])actTransRecSet.toArray(new DynamicObject[0]), (OperateOption)OperateOption.create());
    }

    private void addWFNodeTemplate(DynamicObject actInfo) {
        String nodeStencilType = "HRActivity";
        DynamicObject actNodeInfo = BusinessDataServiceHelper.loadSingle((String)"hrcs_activity", (String)"activitytype,bizobj,bizclassify,description,app", (QFilter[])new QFilter[]{new QFilter("id", "=", actInfo.getPkValue())});
        String nodeName = actNodeInfo.getString("name");
        String nodeNumber = actNodeInfo.getString("number");
        String nodeBizIdentification = String.valueOf(actInfo.getPkValue());
        String nodeDoc = actNodeInfo.getString("description");
        String nodeAppID = actNodeInfo.getString("app.id") == null ? AppHelper.getMetaAppInfo((String)"hrcs").getId() : actNodeInfo.getString("app.id");
        NodeTemplate template = new NodeTemplate(nodeStencilType, nodeName, nodeNumber, ActivitySaveOp.getProperties(nodeName, nodeNumber, nodeBizIdentification, nodeDoc), nodeBizIdentification, nodeAppID);
        template.setGroupId(WF_HR_GROUP_ID);
        WorkflowServiceHelper.addNodeTemplate((NodeTemplate)template);
    }

    private void updateWFNodeTemplate(DynamicObject actInfo) {
        String number = actInfo.getString("number");
        HashMap<String, ILocaleString> nodeTemplateInfo = new HashMap<String, ILocaleString>(16);
        nodeTemplateInfo.put("nodeTemplateName", actInfo.getLocaleString("name"));
        WorkflowServiceHelper.updateNodeTemplateInfo((String)number, nodeTemplateInfo);
    }
}
