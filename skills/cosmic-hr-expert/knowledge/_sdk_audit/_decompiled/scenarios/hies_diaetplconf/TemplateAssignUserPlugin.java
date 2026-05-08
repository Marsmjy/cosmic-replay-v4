/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.algo.DataSet
 *  kd.bos.algo.RowMeta
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.orm.impl.ORMUtil
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.api.HrApiResponse
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 */
package kd.hr.hies.formplugin;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.algo.DataSet;
import kd.bos.algo.RowMeta;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.orm.impl.ORMUtil;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.api.HrApiResponse;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;

@ExcludeFromJacocoGeneratedReport
public final class TemplateAssignUserPlugin
extends HRBaseDataCommonEdit
implements BeforeF7SelectListener {
    public static final String FORM_BOS_USER = "bos_user";
    public static final String FORM_DIAETPLCONF = "hies_diaetplconf";
    public static final String ACTION_USEROBJ_HELP = "closeCallBack_userObj_Help";

    public void initialize() {
        super.initialize();
        this.addListener();
    }

    private void addListener() {
        this.addItemClickListeners(new String[]{"toolbar_user"});
        Toolbar userToolBar = (Toolbar)this.getControl("toolbar_user");
        userToolBar.addItemClickListener((ItemClickListener)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void afterLoadData(EventObject e) {
        this.buildAndRestoreUser();
    }

    private void buildAndRestoreUser() {
        DynamicObjectCollection collection = this.getModel().getEntryEntity("userlist");
        List<Long> userIds = collection.stream().filter(dynamicObject -> Objects.nonNull(dynamicObject.get("user"))).map(dynamicObject -> ((DynamicObject)dynamicObject.get("user")).getLong("id")).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        Map<Long, Map<String, Object>> userMap = this.getUerMap(userIds);
        for (DynamicObject dynamicObject2 : collection) {
            Long userId = ((DynamicObject)dynamicObject2.get("user")).getLong("id");
            Map<String, Object> data = userMap.get(userId);
            dynamicObject2.set("position", data.get("position"));
            dynamicObject2.set("userorg", data.get("adminorg"));
            dynamicObject2.set("company", data.get("company"));
        }
        this.getView().updateView("userlist");
    }

    private Map<Long, Map<String, Object>> getUerMap(List<Long> userIds) {
        int size = userIds.size();
        HashMap<Long, Long> userPersonMap = new HashMap<Long, Long>(size);
        for (Long userId : userIds) {
            Map data;
            ArrayList<Long> userPersonIds = new ArrayList<Long>(1);
            userPersonIds.add(userId);
            HrApiResponse result = (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmployeeService", (String)"queryEmployeeByUserIds", (Object[])new Object[]{userPersonIds, null, "1"});
            if (!result.isSuccess() || ObjectUtils.isEmpty((Object)(data = (Map)result.getData()))) continue;
            Long person = (Long)data.get("employee");
            userPersonMap.put(userId, person);
        }
        QFilter qFilter = new QFilter("hrpi_person.id", "in", userPersonMap.values());
        QFilter posstautsFilter = new QFilter("hrpi_empposorgrel.posstatus.number", "=", (Object)"1020_S");
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrpi_employeenewf7query");
        try (DataSet ds = serviceHelper.queryMultiEntityDataSet("hrpi_person.id,hrpi_empposorgrel.posstatus.name,hrpi_empposorgrel.posstatus.number,hrpi_empposorgrel.position.name,hrpi_empposorgrel.adminorg.name,hrpi_empposorgrel.company.name", new QFilter[]{qFilter, posstautsFilter}, null, false, 0, 100);){
            RowMeta rowMeta = ds.getRowMeta();
            Iterator iter = ds.iterator();
            DynamicObjectCollection dynamicObjects = ORMUtil.toDynamicObjectCollection((Iterator)iter, (RowMeta)rowMeta, (String)"hrpi_employeenewf7query");
            HashMap<Long, DynamicObject> personMap = new HashMap<Long, DynamicObject>(dynamicObjects.size());
            for (DynamicObject dynamicObject : dynamicObjects) {
                personMap.put(dynamicObject.getLong("hrpi_person.id"), dynamicObject);
            }
            HashMap userInfoMap = new HashMap(size);
            for (Long userId : userIds) {
                DynamicObject dynamicObject;
                Long personId = (Long)userPersonMap.get(userId);
                String position = "";
                String adminorg = "";
                String company = "";
                if (Objects.nonNull(personId) && Objects.nonNull(dynamicObject = (DynamicObject)personMap.get(personId))) {
                    position = dynamicObject.getString("hrpi_empposorgrel.position.name");
                    adminorg = dynamicObject.getString("hrpi_empposorgrel.adminorg.name");
                    company = dynamicObject.getString("hrpi_empposorgrel.company.name");
                }
                HashMap<String, String> data = new HashMap<String, String>(5);
                data.put("position", position);
                data.put("adminorg", adminorg);
                data.put("company", company);
                userInfoMap.put(userId, data);
            }
            HashMap hashMap = userInfoMap;
            return hashMap;
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        if (ACTION_USEROBJ_HELP.equals(actionId)) {
            Object returnData = closedCallBackEvent.getReturnData();
            if (!(returnData instanceof ListSelectedRowCollection)) {
                return;
            }
            ListSelectedRowCollection lsrc = (ListSelectedRowCollection)returnData;
            int size = lsrc.size();
            HashMap<Long, String> userInfos = new HashMap<Long, String>(size);
            for (ListSelectedRow listSelectedRow : lsrc) {
                Object pkValue = listSelectedRow.getPrimaryKeyValue();
                String dimObjName = listSelectedRow.getName();
                userInfos.put((Long)pkValue, dimObjName);
            }
            this.fillRoleObjList(userInfos);
        }
    }

    private void fillRoleObjList(Map<Long, String> userInfos) {
        if (CollectionUtils.isEmpty(userInfos)) {
            return;
        }
        int size = userInfos.size();
        HashMap<Long, Long> userPersonMap = new HashMap<Long, Long>(size);
        for (Long userId : userInfos.keySet()) {
            Map data;
            ArrayList<Long> userPersonIds = new ArrayList<Long>(1);
            userPersonIds.add(userId);
            HrApiResponse result = (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmployeeService", (String)"queryEmployeeByUserIds", (Object[])new Object[]{userPersonIds, null, "1"});
            if (!result.isSuccess() || ObjectUtils.isEmpty((Object)(data = (Map)result.getData()))) continue;
            Long person = (Long)data.get("employee");
            userPersonMap.put(userId, person);
        }
        QFilter qFilter = new QFilter("hrpi_person.id", "in", userPersonMap.values());
        QFilter posstautsFilter = new QFilter("hrpi_empposorgrel.posstatus.number", "=", (Object)"1020_S");
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrpi_employeenewf7query");
        try (DataSet ds = serviceHelper.queryMultiEntityDataSet("hrpi_person.id,hrpi_empposorgrel.posstatus.name,hrpi_empposorgrel.posstatus.number,hrpi_empposorgrel.position.name,hrpi_empposorgrel.adminorg.name,hrpi_empposorgrel.company.name", new QFilter[]{qFilter, posstautsFilter}, null, false, 0, 100);){
            RowMeta rowMeta = ds.getRowMeta();
            Iterator iter = ds.iterator();
            DynamicObjectCollection data = ORMUtil.toDynamicObjectCollection((Iterator)iter, (RowMeta)rowMeta, (String)"hrpi_employeenewf7query");
            Map<Long, DynamicObject> personMap = data.stream().collect(Collectors.toMap(dynamicObject -> dynamicObject.getLong("hrpi_person.id"), dynamicObject -> dynamicObject, (k1, k2) -> k1));
            this.getModel().beginInit();
            TableValueSetter vs = new TableValueSetter(new String[0]);
            vs.addField("user", new Object[0]);
            vs.addField("position", new Object[0]);
            vs.addField("userorg", new Object[0]);
            vs.addField("company", new Object[0]);
            for (Long userId : userInfos.keySet()) {
                DynamicObject dynamicObject2;
                Long person = (Long)userPersonMap.get(userId);
                String position = "";
                String adminorg = "";
                String company = "";
                if (Objects.nonNull(person) && Objects.nonNull(dynamicObject2 = personMap.get(person))) {
                    position = dynamicObject2.getString("hrpi_empposorgrel.position.name");
                    adminorg = dynamicObject2.getString("hrpi_empposorgrel.adminorg.name");
                    company = dynamicObject2.getString("hrpi_empposorgrel.company.name");
                }
                vs.addRow(new Object[]{userId, position, adminorg, company});
            }
            ((AbstractFormDataModel)this.getModel()).batchCreateNewEntryRow("userlist", vs);
            this.getModel().endInit();
            this.getView().updateView("userlist");
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
    }

    static /* synthetic */ IDataModel access$000(TemplateAssignUserPlugin x0) {
        return x0.getModel();
    }
}
