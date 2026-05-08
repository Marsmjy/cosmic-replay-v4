/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.events.BeforePackageDataEvent
 *  kd.bos.entity.datamodel.events.PackageDataEvent
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.entity.list.column.AbstractColumnDesc
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.BillList
 *  kd.bos.list.IListView
 *  kd.bos.list.ListShowParameter
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hrmp.hbjm.business.domain.repository.HBJMHisRepository
 *  kd.hrmp.hbjm.business.domain.repository.JobRepository
 */
package kd.hrmp.hbjm.formplugin.web.job;

import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.events.BeforePackageDataEvent;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.entity.list.column.AbstractColumnDesc;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.BillList;
import kd.bos.list.IListView;
import kd.bos.list.ListShowParameter;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hrmp.hbjm.business.domain.repository.HBJMHisRepository;
import kd.hrmp.hbjm.business.domain.repository.JobRepository;

public final class JobListPlugin
extends HRDataBaseList {
    private Map<Long, String> jobClassMap = new ConcurrentHashMap<Long, String>(16);

    public void addItemClickListeners(String ... keys) {
        super.addItemClickListeners(keys);
    }

    public void beforePackageData(BeforePackageDataEvent event) {
        DynamicObjectCollection pageData = event.getPageData();
        List jobIdList = pageData.stream().map(data -> data.getLong("id")).collect(Collectors.toList());
        this.jobClassMap = HBJMHisRepository.getInstance().queryJobClassLongNameByJobAndBsedToMap(jobIdList);
    }

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        args.setListDataProvider((IListDataProvider)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void packageData(PackageDataEvent event) {
        super.packageData(event);
        AbstractColumnDesc columnDesc = (AbstractColumnDesc)event.getSource();
        DynamicObject rowData = event.getRowData();
        if ("jobseq_jobfamily_jobcls".equals(columnDesc.getFieldKey())) {
            StringBuilder nameBuilder = new StringBuilder();
            if (rowData.get("jobseq") != null) {
                nameBuilder.append(rowData.getDynamicObject("jobseq").getString("name")).append("/");
            }
            if (rowData.get("jobfamily") != null) {
                nameBuilder.append(rowData.getDynamicObject("jobfamily").getString("name")).append("/");
            }
            if (rowData.get("jobclass") != null) {
                String jobclass = this.jobClassMap.get(rowData.getLong("id")) + "-";
                nameBuilder.append(jobclass);
            }
            if (nameBuilder.length() > 0) {
                String name = nameBuilder.substring(0, nameBuilder.length() - 1);
                event.setFormatValue((Object)name);
            }
        }
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        super.billListHyperLinkClick(args);
        ListShowParameter parameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (parameter.isLookUp()) {
            return;
        }
        args.setCancel(true);
        BillList list = (BillList)args.getHyperLinkClickEvent().getSource();
        Object primaryKeyValue = list.getFocusRowPkId();
        DynamicObject job = JobRepository.getInstance().findById((Long)primaryKeyValue);
        if (job != null) {
            boolean isAudit;
            IListView listview = (IListView)this.getView();
            BillShowParameter showParameter = new BillShowParameter();
            if (this.getView().getMainView() != null) {
                showParameter.setPageId(String.valueOf(primaryKeyValue) + '_' + this.getView().getMainView().getPageId());
            }
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            showParameter.setFormId("hbjm_jobhr");
            showParameter.setPkId(listview.getFocusRowPkId());
            String name = job.getString("name");
            showParameter.setCaption(ResManager.loadKDString((String)"\u804c\u4f4d - ", (String)"JobListPlugin_1", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]).concat(name));
            boolean bl = isAudit = HRBaseDataConfigUtil.getAudit((String)"hbjm_jobhr") && HRStringUtils.equals((String)job.getString("enable"), (String)"1");
            if (HRStringUtils.equals((String)job.getString("enable"), (String)"10") || isAudit) {
                showParameter.setBillStatus(BillOperationStatus.EDIT);
                showParameter.setStatus(OperationStatus.EDIT);
            } else {
                showParameter.setBillStatus(BillOperationStatus.VIEW);
                showParameter.setStatus(OperationStatus.VIEW);
            }
            showParameter.setCustomParam("currentObjectPKId", (Object)String.valueOf(listview.getFocusRowPkId()));
            HashMap<String, Comparable<Date>> map = new HashMap<String, Comparable<Date>>();
            map.put("historyDate", job.getDate("bsed"));
            map.put("boid", Long.valueOf(job.getLong("boid")));
            showParameter.setCustomParam("customvariables", map);
            showParameter.setCustomParam("caption", (Object)ResManager.loadKDString((String)"\u804c\u4f4d\u5173\u8054\u4fe1\u606f", (String)"JobListPlugin_3", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]).concat("-").concat(name));
            showParameter.setCustomParam("useorgId", (Object)this.getPageCache().get("createOrg"));
            this.getView().showForm((FormShowParameter)showParameter);
        } else {
            this.getView().showErrorNotification(ResManager.loadKDString((String)"\u6570\u636e\u5df2\u88ab\u5220\u9664\uff0c\u8bf7\u68c0\u67e5\u3002", (String)"JobListPlugin_0", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]));
        }
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        String commonOrderBy = "jobscm,jobseq,jobfamily,jobclass desc,number";
        setFilterEvent.setOrderBy(commonOrderBy);
    }
}
