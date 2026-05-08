/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.bos.servicehelper.TimeServiceHelper
 *  kd.bos.servicehelper.operation.OperationServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.servicehelper.activity.ActivityNodeLogServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.activity.util.WorkflowEventEnum
 *  kd.hr.hrcs.opplugin.validator.activity.ActivityInsSaveValidator
 */
package kd.hr.hrcs.opplugin.web.activity;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.TimeServiceHelper;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.servicehelper.activity.ActivityNodeLogServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.activity.util.WorkflowEventEnum;
import kd.hr.hrcs.opplugin.validator.activity.ActivityInsSaveValidator;

public final class ActivityInsSaveOp
extends HRDataBaseOp {
    private static final String ENTITYNAME_REC = "hrcs_actassignrec";
    private static final String ENTITYNAME_INS = "hrcs_activityins";
    private List<DynamicObject> recInfoArr = null;

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new ActivityInsSaveValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        DynamicObject[] items = e.getDataEntities();
        int halfLen = (int)Math.ceil((double)items.length / 2.0);
        this.recInfoArr = new ArrayList<DynamicObject>(halfLen);
        Long currentUserId = RequestContext.get().getCurrUserId();
        HRBaseServiceHelper insHelper = new HRBaseServiceHelper(ENTITYNAME_INS);
        HashMap actIns = Maps.newHashMapWithExpectedSize((int)items.length);
        DynamicObject[] insInfos = (DynamicObject[])Arrays.stream(items).filter(Objects::nonNull).filter(item -> ENTITYNAME_INS.equals(item.getDynamicObjectType().getName())).toArray(DynamicObject[]::new);
        Arrays.stream(items).filter(Objects::nonNull).filter(item -> ENTITYNAME_REC.equals(item.getDynamicObjectType().getName())).forEach(item -> {
            this.recInfoArr.add((DynamicObject)item);
            actIns.put(item.getLong("activityins"), item);
        });
        Arrays.stream(insHelper.query("id,handlers.fbasedataid", new QFilter[]{new QFilter("id", "in", actIns.keySet())})).forEach(actInsInDb -> {
            DynamicObject item = (DynamicObject)actIns.get(actInsInDb.getLong("id"));
            HRBaseServiceHelper recHelper = new HRBaseServiceHelper(ENTITYNAME_REC);
            DynamicObject actTransRec = BusinessDataServiceHelper.newDynamicObject((String)ENTITYNAME_REC);
            DynamicObjectCollection oriHandlerColl = actTransRec.getDynamicObjectCollection("mulhandlerori");
            DynamicObjectType oriDyType = oriHandlerColl.getDynamicObjectType();
            DynamicObjectCollection insHandInDb = actInsInDb.getDynamicObjectCollection("handlers");
            String opType = item.getString("assigntype");
            if (this.isAssignOrCreate(opType)) {
                for (DynamicObject handler : insHandInDb) {
                    this.addMulHandler(oriHandlerColl, oriDyType, recHelper, handler.getDynamicObject("fbasedataid").getPkValue());
                }
            } else if (this.isTrans(opType)) {
                this.addMulHandler(oriHandlerColl, oriDyType, recHelper, currentUserId);
            }
            item.set("mulhandlerori", (Object)oriHandlerColl);
        });
        e.setDataEntities(insInfos);
    }

    private boolean isAssignOrCreate(String opType) {
        return "ASSIGN".equals(opType) || "CREATE".equals(opType);
    }

    private boolean isTrans(String opType) {
        return "TRANSFER".equals(opType);
    }

    private void addMulHandler(DynamicObjectCollection handlerColl, DynamicObjectType dyType, HRBaseServiceHelper recHelper, Object userId) {
        DynamicObject mulBasedataDy = new DynamicObject(dyType);
        DynamicObject handlerUser = recHelper.generateEmptyDynamicObject("bos_user");
        handlerUser.set("id", userId);
        mulBasedataDy.set("fbasedataid", (Object)handlerUser);
        handlerColl.add((Object)mulBasedataDy);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        String operationKey = args.getOperationKey();
        if ("save".equalsIgnoreCase(operationKey)) {
            boolean insert;
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(ENTITYNAME_INS);
            boolean bl = insert = !serviceHelper.isExists(new QFilter("id", "in", Arrays.stream(args.getDataEntities()).map(it -> it.getPkValue()).collect(Collectors.toSet())));
            if (insert) {
                DynamicObject[] items;
                Optional<QFilter> filter = Arrays.stream(args.getDataEntities()).map(it -> {
                    QFilter qFilter = new QFilter("wfprocessdefinitionid", "=", (Object)it.getLong("wfprocessdefinitionid"));
                    qFilter.and(new QFilter("wfprocessinsid", "=", (Object)it.getLong("wfprocessinsid")));
                    qFilter.and(new QFilter("wfnode", "=", (Object)it.getString("wfnode")));
                    return qFilter;
                }).reduce((acc, item) -> acc.or(item));
                if (filter.isPresent() && null != (items = (DynamicObject[])Arrays.stream(serviceHelper.query("id,isabandon", new QFilter[]{filter.get()})).map(it -> {
                    it.set("isabandon", (Object)"1");
                    return it;
                }).toArray(DynamicObject[]::new)) && items.length > 0) {
                    serviceHelper.update(items);
                }
                this.taskStatusLog(args.getDataEntities());
            }
        }
    }

    private void taskStatusLog(DynamicObject[] dataEntities) {
        if (dataEntities == null || dataEntities.length == 0) {
            return;
        }
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(ENTITYNAME_INS);
        Optional<QFilter> filter = Arrays.stream(dataEntities).map(it -> {
            QFilter qFilter = new QFilter("wfprocessdefinitionid", "=", (Object)it.getLong("wfprocessdefinitionid"));
            qFilter.and(new QFilter("wfprocessinsid", "=", (Object)it.getLong("wfprocessinsid")));
            return qFilter;
        }).reduce((acc, item) -> acc.or(item));
        DynamicObject[] items = serviceHelper.query("id,taskstatus", new QFilter[]{filter.get()}, "createtime desc");
        Optional<QFilter> filterNew = Arrays.stream(dataEntities).map(it -> {
            QFilter qFilter = new QFilter("wfprocessdefinitionid", "=", (Object)it.getLong("wfprocessdefinitionid"));
            qFilter.and(new QFilter("wfprocessinsid", "=", (Object)it.getLong("wfprocessinsid")));
            qFilter.and(new QFilter("wfnode", "=", (Object)it.getString("wfnode")));
            return qFilter;
        }).reduce((acc, item) -> acc.or(item));
        DynamicObject[] itemsNew = serviceHelper.query("id,taskstatus", new QFilter[]{filterNew.get()}, "createtime desc");
        if (null != items && items.length > 0 && itemsNew != null && itemsNew.length > 0) {
            String taskstatus = items[0].getString("taskstatus");
            if ("30".equals(taskstatus)) {
                ActivityNodeLogServiceHelper.save((String)itemsNew[0].getString("taskstatus"), (String)dataEntities[0].getString("taskstatus"), (String)WorkflowEventEnum.REJECT.name(), (Long)RequestContext.get().getCurrUserId(), (Long)dataEntities[0].getLong("wfprocessinsid"), (String)dataEntities[0].getString("wfnode"));
            } else {
                String opTypeName = "40".equals(taskstatus) ? WorkflowEventEnum.REJECT.name() : WorkflowEventEnum.SUBMIT.name();
                ActivityNodeLogServiceHelper.save((String)itemsNew[0].getString("taskstatus"), (String)dataEntities[0].getString("taskstatus"), (String)opTypeName, (Long)RequestContext.get().getCurrUserId(), (Long)dataEntities[0].getLong("wfprocessinsid"), (String)dataEntities[0].getString("wfnode"));
            }
        } else {
            ActivityNodeLogServiceHelper.save((String)"", (String)dataEntities[0].getString("taskstatus"), (String)WorkflowEventEnum.SUBMIT.name(), (Long)RequestContext.get().getCurrUserId(), (Long)dataEntities[0].getLong("wfprocessinsid"), (String)dataEntities[0].getString("wfnode"));
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        List<DynamicObject> recInfos = this.recInfoArr;
        Long curUserId = RequestContext.get().getCurrUserId();
        HRBaseServiceHelper insHelper = new HRBaseServiceHelper(ENTITYNAME_INS);
        Map<Long, DynamicObject> actInsMap = recInfos.stream().filter(item -> ENTITYNAME_REC.equals(item.getDynamicObjectType().getName())).collect(Collectors.toMap(item -> item.getLong("activityins"), item -> item, (oldValue, newValue) -> oldValue));
        DynamicObject[] result = (DynamicObject[])Arrays.stream(insHelper.query("id,handlers.fbasedataid", new QFilter[]{new QFilter("id", "in", actInsMap.keySet())})).map(item -> {
            Long activityInsId = item.getLong("id");
            DynamicObject recInfo = (DynamicObject)actInsMap.get(activityInsId);
            DynamicObjectCollection insHandlerColl = item.getDynamicObjectCollection("handlers");
            DynamicObject actTransRec = BusinessDataServiceHelper.newDynamicObject((String)ENTITYNAME_REC);
            actTransRec.set("assigntype", recInfo.get("assigntype"));
            actTransRec.set("creator", (Object)curUserId);
            actTransRec.set("createtime", (Object)TimeServiceHelper.now());
            actTransRec.set("activityins", (Object)activityInsId);
            actTransRec.set("auditmessage", recInfo.get("auditmessage"));
            actTransRec.set("description", recInfo.get("description"));
            actTransRec.set("mulhandlerori", recInfo.get("mulhandlerori"));
            HRBaseServiceHelper recHelper = new HRBaseServiceHelper(ENTITYNAME_REC);
            DynamicObjectCollection curHandlerColl = actTransRec.getDynamicObjectCollection("mulhandler");
            DynamicObjectType curDyType = curHandlerColl.getDynamicObjectType();
            String opType = recInfo.getString("assigntype");
            if (this.isAssignOrCreate(opType)) {
                for (DynamicObject handlerInDbInfo : insHandlerColl) {
                    String handlerId = handlerInDbInfo.getString("fbasedataid.id");
                    this.addMulHandler(curHandlerColl, curDyType, recHelper, handlerId);
                }
            } else if (this.isTrans(opType)) {
                actTransRec.set("mulhandler", recInfo.get("mulhandler"));
            }
            return actTransRec;
        }).toArray(DynamicObject[]::new);
        OperationServiceHelper.executeOperate((String)"submit", (String)ENTITYNAME_REC, (DynamicObject[])result, (OperateOption)OperateOption.create());
    }
}
