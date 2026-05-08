/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.ImportDataEventArgs
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.DefaultEntityOperate
 *  kd.bos.entity.operate.Donothing
 *  kd.bos.entity.operate.Save
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.DateEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.IListView
 *  kd.bos.list.ITreeListView
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hrmp.hbjm.business.application.impl.job.JobApplicationImpl
 *  kd.hrmp.hbjm.business.domain.repository.JobRepository
 *  kd.hrmp.hbjm.business.utils.JobUtils
 */
package kd.hrmp.hbjm.formplugin.web.job;

import java.text.MessageFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Objects;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.ImportDataEventArgs;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.DefaultEntityOperate;
import kd.bos.entity.operate.Donothing;
import kd.bos.entity.operate.Save;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.DateEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.IListView;
import kd.bos.list.ITreeListView;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hrmp.hbjm.business.application.impl.job.JobApplicationImpl;
import kd.hrmp.hbjm.business.domain.repository.JobRepository;
import kd.hrmp.hbjm.business.utils.JobUtils;

public final class JobBasedataEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final Log logger = LogFactory.getLog(JobBasedataEdit.class);
    private static final String HBJM_JOBGRADERANGE_BAK = "HBJM_jobgraderange_bak";
    private static final String HBJM_JOBLEVELRANGE_BAK = "HBJM_joblevelrange_bak";
    private static final String HIGHJOBGRADE = "highjobgrade";
    private static final String LOWJOBGRADE = "lowjobgrade";
    private static final String HIGHJOBLEVEL = "highjoblevel";
    private static final String LOWJOBLEVEL = "lowjoblevel";
    private static final String JOBGRADERANG = "jobgraderang";
    private static final String JOBLEVELRANG = "joblevelrang";
    private String triggerKey = "";
    private boolean pageInit = false;

    public void preOpenForm(PreOpenFormEventArgs args) {
        FormShowParameter showParameter = (FormShowParameter)args.getSource();
        Object currentDataId = showParameter.getCustomParam("currentDataId");
        if (currentDataId != null) {
            DynamicObject job = JobRepository.getInstance().findById((Long)currentDataId);
            showParameter.setCaption(ResManager.loadKDString((String)"\u804c\u4f4d - ", (String)"JobListPlugin_1", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]).concat(job.getString("name")));
        }
        showParameter.setCustomParam("hbss_entitytype_id", (Object)"1030");
        showParameter.setCustomParam("isshowbsed", (Object)true);
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addClickListeners(new String[]{JOBLEVELRANG});
        this.addClickListeners(new String[]{JOBGRADERANG});
        BasedataEdit jobSeqEdit = (BasedataEdit)this.getView().getControl("jobseq");
        jobSeqEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit jobFamilyEdit = (BasedataEdit)this.getView().getControl("jobfamily");
        jobFamilyEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit jobClassEdit = (BasedataEdit)this.getView().getControl("jobclass");
        jobClassEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit jobScmEdit = (BasedataEdit)this.getView().getControl("jobscm");
        jobScmEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeBindData(EventObject e) {
        String nodeId;
        ITreeListView treeView;
        Boolean isSysPreSet;
        this.pageInit = true;
        DynamicObject baseDataEntity = this.getView().getModel().getDataEntity();
        if (JobUtils.isEnablingOrNoAuditData((DynamicObject)baseDataEntity)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap11"});
        }
        if ((isSysPreSet = Boolean.valueOf(baseDataEntity.getBoolean("issyspreset"))).booleanValue()) {
            ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
        }
        Object his_action = this.getView().getFormShowParameter().getCustomParam("his_action");
        IFormView parentview = this.getView().getParentViewNoPlugin();
        if (Objects.nonNull(his_action)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"bar_save"});
        }
        if (parentview instanceof IListView && ObjectUtils.isEmpty((Object)his_action) && (treeView = ((IListView)parentview).getTreeListView()) != null && !ObjectUtils.isEmpty((Object)(nodeId = (String)treeView.getTreeModel().getCurrentNodeId()))) {
            String[] ids = nodeId.split("#");
            if (ids.length >= 2) {
                this.getModel().setValue("jobscm", (Object)new Object[]{ids[1].substring(1)});
            }
            if (ids.length >= 3) {
                this.getModel().setValue("jobseq", (Object)ids[2].substring(1));
            }
            if (ids.length >= 4) {
                this.getModel().setValue("jobfamily", (Object)ids[3].substring(1));
            }
            if (ids.length >= 5) {
                this.getModel().setValue("jobclass", (Object)ids[ids.length - 1].substring(1));
            }
        }
        this.bindJobGradeAndlevelData();
        Boolean isHisView = (Boolean)this.getView().getFormShowParameter().getCustomParam("isHisView");
        if (this.getView().getParentViewNoPlugin() != null) {
            String billFormId = this.getView().getParentViewNoPlugin().getFormShowParameter().getIdentifyFormId();
            if (isHisView != null && isHisView.booleanValue() || billFormId != null && billFormId.contains("hjm_jobchangerecord")) {
                this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap11"});
            }
        }
        this.dealDisableData();
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        HashMap<String, String> customParam = (HashMap<String, String>)formShowParameter.getCustomParam("customPKFilter");
        if (customParam == null) {
            customParam = new HashMap<String, String>();
            formShowParameter.setCustomParam("customPKFilter", customParam);
        }
        customParam.put("boid", this.getModel().getDataEntity().getString("boid"));
        this.getView().cacheFormShowParameter();
    }

    private void setButtonVisble() {
        IFormView parentView = this.getView().getParentViewNoPlugin();
        boolean visible = parentView != null && parentView.getFormShowParameter() instanceof ListShowParameter;
        this.getView().setVisible(Boolean.valueOf(visible), new String[]{"bar_change", "flexpanelap11", "bar_hisversioninfo"});
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        super.beforeItemClick(evt);
        if ("confirmchangenoaudit".equals(evt.getOperationKey())) {
            if (this.getView().getParentViewNoPlugin() != null) {
                this.getView().getParentViewNoPlugin().getPageCache().put("lastButton", evt.getOperationKey());
            }
            Date blsed = JobRepository.getInstance().getBlsedByBsed((Date)this.getModel().getValue("bsed"), ((Long)this.getModel().getValue("boid")).longValue());
            this.getModel().setValue("bsled", (Object)blsed);
        }
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        DateEdit bsededit = (DateEdit)this.getView().getControl("bsed");
        bsededit.setMaxDate(new Date());
        String pageStatus = this.getPageCache().get("pageStatus");
        if ("view".equals(this.getView().getFormShowParameter().getCustomParam("pageStatus"))) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"insertdatabtn"});
        }
        if ("edit".equals(pageStatus)) {
            this.getView().setEnable(Boolean.FALSE, new String[]{"jobscm"});
        }
        this.setButtonVisble();
    }

    public void dealDisableData() {
        OperationStatus operationStatus = this.getView().getFormShowParameter().getStatus();
        if (operationStatus == OperationStatus.VIEW) {
            return;
        }
        DynamicObject seq = this.getModel().getDataEntity().getDynamicObject("jobseq");
        if (!ObjectUtils.isEmpty((Object)seq) && !HRStringUtils.equals((String)seq.getString("enable"), (String)"1")) {
            this.setValueNull("jobseq", "jobfamily", "jobclass", JOBGRADERANG, JOBLEVELRANG, HIGHJOBGRADE, LOWJOBGRADE, HIGHJOBLEVEL, LOWJOBLEVEL);
            this.getView().setEnable(Boolean.FALSE, new String[]{"jobfamily", "jobclass", JOBGRADERANG, JOBLEVELRANG});
            return;
        }
        DynamicObject family = this.getModel().getDataEntity().getDynamicObject("jobfamily");
        if (!ObjectUtils.isEmpty((Object)family) && !HRStringUtils.equals((String)family.getString("enable"), (String)"1")) {
            this.setValueNull("jobfamily", "jobclass", JOBGRADERANG, JOBLEVELRANG, HIGHJOBGRADE, LOWJOBGRADE, HIGHJOBLEVEL, LOWJOBLEVEL);
            this.getView().setEnable(Boolean.FALSE, new String[]{"jobclass", JOBGRADERANG, JOBLEVELRANG});
            return;
        }
        DynamicObject jobClass = this.getModel().getDataEntity().getDynamicObject("jobclass");
        if (!ObjectUtils.isEmpty((Object)jobClass) && !HRStringUtils.equals((String)jobClass.getString("enable"), (String)"1")) {
            this.setValueNull("jobclass", JOBGRADERANG, JOBLEVELRANG, HIGHJOBGRADE, LOWJOBGRADE, HIGHJOBLEVEL, LOWJOBLEVEL);
            this.getView().setEnable(Boolean.FALSE, new String[]{JOBGRADERANG, JOBLEVELRANG});
            return;
        }
    }

    public void click(EventObject evt) {
        super.click(evt);
        DynamicObject job = this.getModel().getDataEntity();
        Control source = (Control)evt.getSource();
        FormShowParameter form = new FormShowParameter();
        form.setCustomParam("jobscmid", (Object)job.getString("jobscm.id"));
        form.getOpenStyle().setShowType(ShowType.Modal);
        if (StringUtils.equals((CharSequence)JOBGRADERANG, (CharSequence)source.getKey()) || StringUtils.equals((CharSequence)JOBLEVELRANG, (CharSequence)source.getKey())) {
            boolean isLevelEmpty;
            DynamicObject jobGradeScm = this.getModel().getDataEntity().getDynamicObject("jobgradescm");
            DynamicObject jobLevelScm = this.getModel().getDataEntity().getDynamicObject("joblevelscm");
            boolean isGradeScmEmpty = StringUtils.equals((CharSequence)JOBGRADERANG, (CharSequence)source.getKey()) && Objects.isNull(jobGradeScm);
            boolean bl = isLevelEmpty = StringUtils.equals((CharSequence)JOBLEVELRANG, (CharSequence)source.getKey()) && Objects.isNull(jobLevelScm);
            if (isGradeScmEmpty || isLevelEmpty) {
                String msg = ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u201c\u804c\u7ea7\u65b9\u6848\u201d\u3002", (String)"JobBasedataEdit_3", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]);
                if (isGradeScmEmpty) {
                    msg = ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u201c\u804c\u7b49\u65b9\u6848\u201d\u3002", (String)"JobBasedataEdit_4", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]);
                }
                this.getView().showTipNotification(msg);
                return;
            }
        }
        if (JOBGRADERANG.equals(source.getKey())) {
            form.setFormId("hbjm_jobgraderange");
            form.setCloseCallBack(new CloseCallBack((IFormPlugin)this, HBJM_JOBGRADERANGE_BAK));
            long highjobgradeId = job.getLong("highjobgrade.id");
            long lowjobgradeId = job.getLong("lowjobgrade.id");
            long jobGradeScmId = job.getLong("jobgradescm_id");
            form.setCustomParam(LOWJOBGRADE, (Object)(lowjobgradeId == 0L ? null : Long.valueOf(lowjobgradeId)));
            form.setCustomParam(HIGHJOBGRADE, (Object)(highjobgradeId == 0L ? null : Long.valueOf(highjobgradeId)));
            form.setCustomParam("jobgradescm", (Object)(jobGradeScmId == 0L ? null : Long.valueOf(jobGradeScmId)));
            this.getView().showForm(form);
        } else if (JOBLEVELRANG.equals(source.getKey())) {
            form.setFormId("hbjm_joblevelrange");
            form.setCloseCallBack(new CloseCallBack((IFormPlugin)this, HBJM_JOBLEVELRANGE_BAK));
            long highjoblevelId = job.getLong("highjoblevel.id");
            long lowjoblevelId = job.getLong("lowjoblevel.id");
            long jobLevelScmId = job.getLong("joblevelscm_id");
            form.setCustomParam("joblevelscm", (Object)(jobLevelScmId == 0L ? null : Long.valueOf(jobLevelScmId)));
            if (highjoblevelId != 0L) {
                form.setCustomParam(HIGHJOBLEVEL, (Object)highjoblevelId);
            }
            if (lowjoblevelId != 0L) {
                form.setCustomParam(LOWJOBLEVEL, (Object)lowjoblevelId);
            }
            this.getView().showForm(form);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        Object source = args.getSource();
        if (source instanceof Donothing || source instanceof Save) {
            String opKey;
            DefaultEntityOperate btn = (DefaultEntityOperate)source;
            switch (opKey = btn.getOperateKey()) {
                case "confirmchange": 
                case "confirmchangenoaudit": 
                case "save": {
                    String joblevelrang;
                    String jobgraderang = (String)this.getModel().getValue(JOBGRADERANG);
                    if (kd.bos.util.StringUtils.isBlank((String)jobgraderang)) {
                        this.getModel().setValue(HIGHJOBGRADE, null);
                        this.getModel().setValue(LOWJOBGRADE, null);
                    }
                    if (!kd.bos.util.StringUtils.isBlank((String)(joblevelrang = (String)this.getModel().getValue(JOBLEVELRANG)))) break;
                    this.getModel().setValue(HIGHJOBLEVEL, null);
                    this.getModel().setValue(LOWJOBLEVEL, null);
                    break;
                }
                case "hisversioninfo": {
                    this.getModel().setValue("id", (Object)this.getModel().getDataEntity().getLong("boid"));
                }
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        if (!"confirmchange".equals(args.getOperateKey()) && !"confirmchangenoaudit".equals(args.getOperateKey()) || args.getOperationResult().isSuccess()) {
            // empty if block
        }
        if (args.getOperateKey().equals("change")) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"bar_change", "flexpanelap11"});
            this.getView().setVisible(Boolean.valueOf(true), new String[]{"bar_confirmchange"});
            this.getView().getFormShowParameter().setStatus(OperationStatus.EDIT);
            this.getView().getFormShowParameter().setCustomParam("hisPage", (Object)"changePage");
            this.getView().updateView("bar_change");
            this.getView().updateView("bar_confirmchange");
        }
        if (args.getOperateKey().equals("save") || args.getOperateKey().equals("confirmchange")) {
            DynamicObject baseDataEntity = this.getView().getModel().getDataEntity();
            if (JobUtils.isEnablingOrNoAuditData((DynamicObject)baseDataEntity)) {
                this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap11"});
            } else {
                this.getView().setVisible(Boolean.TRUE, new String[]{"flexpanelap11"});
            }
            this.getView().getFormShowParameter().setCustomParam("currentObjectPKId", (Object)baseDataEntity.getString("boid"));
            FormShowParameter formShowParameter = this.getView().getFormShowParameter();
            HashMap<String, String> customParam = (HashMap<String, String>)formShowParameter.getCustomParam("customPKFilter");
            if (customParam == null) {
                customParam = new HashMap<String, String>();
                formShowParameter.setCustomParam("customPKFilter", customParam);
            }
            customParam.put("boid", this.getModel().getDataEntity().getString("boid"));
            this.getView().cacheFormShowParameter();
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        HashMap returnData;
        super.closedCallBack(evt);
        String actionId = evt.getActionId();
        if (HBJM_JOBGRADERANGE_BAK.equals(actionId)) {
            HashMap returnData2 = (HashMap)evt.getReturnData();
            if (returnData2 != null) {
                DynamicObject highjobgrade = (DynamicObject)returnData2.get(HIGHJOBGRADE);
                DynamicObject lowjobgrade = (DynamicObject)returnData2.get(LOWJOBGRADE);
                this.setFieldRange(JOBGRADERANG, lowjobgrade, LOWJOBGRADE, highjobgrade, HIGHJOBGRADE);
            }
        } else if (HBJM_JOBLEVELRANGE_BAK.equals(actionId) && (returnData = (HashMap)evt.getReturnData()) != null) {
            DynamicObject highjoblevel = (DynamicObject)returnData.get(HIGHJOBLEVEL);
            DynamicObject lowjoblevel = (DynamicObject)returnData.get(LOWJOBLEVEL);
            this.setFieldRange(JOBLEVELRANG, lowjoblevel, LOWJOBLEVEL, highjoblevel, HIGHJOBLEVEL);
        }
    }

    public void beforePropertyChanged(PropertyChangedArgs e) {
        super.beforePropertyChanged(e);
    }

    public void propertyChanged(PropertyChangedArgs e) {
        String fieldKey = e.getProperty().getName();
        if (kd.bos.util.StringUtils.isEmpty((String)this.triggerKey) && !this.pageInit) {
            this.triggerKey = fieldKey;
            this.enableOrdisableCategoryControl(fieldKey);
        }
        this.dealJobGradeEnable(fieldKey);
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String fieldKey = evt.getProperty().getName();
        QFilter qFilter = null;
        if (!HRStringUtils.equals((String)fieldKey, (String)"jobseq")) {
            if (HRStringUtils.equals((String)fieldKey, (String)"jobfamily")) {
                qFilter = this.buildJobFamilyFilter();
            } else if (HRStringUtils.equals((String)fieldKey, (String)"jobclass")) {
                qFilter = this.buildJobClassFilter();
            }
        }
        ListShowParameter showParameter = (ListShowParameter)evt.getFormShowParameter();
        Long jobFamilyId = this.getModel().getDataEntity().getLong("jobfamily.id");
        Long jobSeqId = this.getModel().getDataEntity().getLong("jobseq.id");
        Long scmId = this.getModel().getDataEntity().getLong("jobscm.id");
        if (HRStringUtils.equals((String)fieldKey, (String)"jobclass") && !ObjectUtils.isEmpty((Object)jobFamilyId)) {
            showParameter.getShowParameter().setCustomParam("selectedJobFamilyId", (Object)(jobFamilyId == 0L ? null : jobFamilyId));
            showParameter.getShowParameter().setCustomParam("selectedScmId", (Object)(scmId == 0L ? null : scmId));
            showParameter.getShowParameter().setCustomParam("selectedJobSeqId", (Object)(jobSeqId == 0L ? null : jobSeqId));
        }
        showParameter.getListFilterParameter().setFilter(qFilter);
    }

    public void afterImportData(ImportDataEventArgs e) {
        DynamicObject jobFamily;
        DynamicObject ob = this.getModel().getDataEntity();
        DynamicObject jobSeq = ob.getDynamicObject("jobseq");
        if (ObjectUtils.isEmpty((Object)jobSeq) && !ObjectUtils.isEmpty((Object)(jobFamily = ob.getDynamicObject("jobfamily")))) {
            ob.set("jobseq", jobFamily.get("jobseq"));
        }
        JobApplicationImpl application = new JobApplicationImpl();
        application.dealGradeOrLevelImportToNull(e.getSourceData(), ob);
        super.afterImportData(e);
    }

    private void setFieldRange(String fieldJobObject, DynamicObject lowObject, String lowString, DynamicObject highObject, String highString) {
        String lowJobString = "";
        String highJobString = "";
        String massage = "";
        if (highObject != null) {
            highJobString = highObject.getString("name");
        }
        if (lowObject != null) {
            lowJobString = lowObject.getString("name");
        }
        if (kd.bos.util.StringUtils.isEmpty((String)lowJobString) && kd.bos.util.StringUtils.isEmpty((String)highJobString)) {
            this.getModel().setValue(fieldJobObject, (Object)"");
        }
        if (kd.bos.util.StringUtils.isNotEmpty((String)lowJobString)) {
            massage = MessageFormat.format(ResManager.loadKDString((String)"{0}\u53ca\u4ee5\u4e0a", (String)"JobBasedataEdit_1", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]), lowJobString);
            this.getModel().setValue(fieldJobObject, (Object)massage);
        }
        if (kd.bos.util.StringUtils.isNotEmpty((String)highJobString)) {
            massage = MessageFormat.format(ResManager.loadKDString((String)"{0}\u53ca\u4ee5\u4e0b", (String)"JobBasedataEdit_2", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]), highJobString);
            this.getModel().setValue(fieldJobObject, (Object)massage);
        }
        if (kd.bos.util.StringUtils.isNotEmpty((String)highJobString) && kd.bos.util.StringUtils.isNotEmpty((String)lowJobString)) {
            this.getModel().setValue(fieldJobObject, (Object)(lowJobString + "-" + highJobString));
        }
        this.getModel().setValue(lowString, (Object)lowObject);
        this.getModel().setValue(highString, (Object)highObject);
    }

    private void enableOrdisableCategoryControl(String changeKey) {
        if (changeKey.equals("jobscm")) {
            this.getView().setEnable(Boolean.TRUE, new String[]{"jobseq"});
        } else if (changeKey.equals("jobseq")) {
            this.getView().setEnable(Boolean.TRUE, new String[]{"jobfamily"});
            this.setValueNull("jobfamily", "jobclass");
        } else if (changeKey.equals("jobfamily")) {
            long jobFamilyId;
            long jobClassFamilyId;
            if (this.getModel().getValue("jobfamily") != null) {
                if (this.getModel().getValue("jobseq") == null) {
                    this.getModel().setValue("jobseq", (Object)((DynamicObject)this.getModel().getValue("jobfamily")).getLong("jobseq.id"));
                }
            } else {
                this.setValueNull("jobclass");
            }
            if (this.getModel().getValue("jobseq") != null && this.getModel().getValue("jobfamily") != null && this.getModel().getDataEntity().getDynamicObject("jobclass") != null && (jobClassFamilyId = this.getModel().getDataEntity().getDynamicObject("jobclass").getLong("jobfamily.id")) != (jobFamilyId = this.getModel().getDataEntity().getDynamicObject("jobfamily").getLong("id"))) {
                this.setValueNull("jobclass");
            }
        } else if (changeKey.equals("jobclass")) {
            if (this.getModel().getValue("jobseq") == null) {
                this.getModel().setValue("jobseq", (Object)((DynamicObject)this.getModel().getValue("jobclass")).getDynamicObject("jobseq"));
                this.getModel().setValue("jobfamily", (Object)((DynamicObject)this.getModel().getValue("jobclass")).getDynamicObject("jobfamily"));
            }
            if (this.getModel().getValue("jobfamily") == null && this.getModel().getValue("jobclass") != null) {
                this.getModel().setValue("jobfamily", (Object)((DynamicObject)this.getModel().getValue("jobclass")).getDynamicObject("jobfamily"));
            }
        }
    }

    private void setValueNull(String ... keys) {
        for (String key : keys) {
            this.getModel().setValue(key, null);
        }
    }

    private QFilter buildJobFamilyFilter() {
        DynamicObject jobSeqObject = (DynamicObject)this.getView().getModel().getValue("jobseq");
        if (jobSeqObject != null) {
            return new QFilter("jobseq", "in", jobSeqObject.get("id"));
        }
        return null;
    }

    private QFilter buildJobClassFilter() {
        DynamicObject jobSeqObject;
        DynamicObject jobFamilyObject = (DynamicObject)this.getView().getModel().getValue("jobfamily");
        QFilter qFilter = new QFilter("enable", "=", (Object)"1");
        if (jobFamilyObject != null) {
            qFilter.and("jobfamily", "in", jobFamilyObject.get("id"));
        }
        if ((jobSeqObject = (DynamicObject)this.getView().getModel().getValue("jobseq")) != null) {
            qFilter.and("jobseq", "in", jobSeqObject.get("id"));
        }
        qFilter.and("iscurrentversion", "=", (Object)"1");
        qFilter.and("datastatus", "=", (Object)"1");
        return qFilter;
    }

    private Long[] getIds(DynamicObject[] dynamicObjects, String param) {
        Long[] ids = new Long[dynamicObjects.length];
        int i = 0;
        for (DynamicObject dynamicObject : dynamicObjects) {
            ids[i] = dynamicObject.getLong(param);
            ++i;
        }
        return ids;
    }

    private void bindJobGradeAndlevelData() {
        DynamicObject job = this.getModel().getDataEntity();
        DynamicObject highjobgrade = (DynamicObject)job.get(HIGHJOBGRADE);
        DynamicObject lowjobgrade = (DynamicObject)job.get(LOWJOBGRADE);
        DynamicObject highjoblevel = (DynamicObject)job.get(HIGHJOBLEVEL);
        DynamicObject lowjoblevel = (DynamicObject)job.get(LOWJOBLEVEL);
        this.setFieldRange(JOBGRADERANG, lowjobgrade, LOWJOBGRADE, highjobgrade, HIGHJOBGRADE);
        this.setFieldRange(JOBLEVELRANG, lowjoblevel, LOWJOBLEVEL, highjoblevel, HIGHJOBLEVEL);
    }

    private void dealJobGradeEnable(String fieldKey) {
        if (HRStringUtils.equals((String)fieldKey, (String)"jobgradescm")) {
            this.clearJobGradeValue(this.getModel(), this.getView());
        } else if (HRStringUtils.equals((String)fieldKey, (String)"joblevelscm")) {
            this.clearJobLevelValue(this.getModel(), this.getView());
        }
    }

    private void clearJobGradeValue(IDataModel model, IFormView view) {
        model.setValue(JOBGRADERANG, null);
        model.setValue(HIGHJOBGRADE, null);
        model.setValue(LOWJOBGRADE, null);
        view.updateView(JOBGRADERANG);
    }

    private void clearJobLevelValue(IDataModel model, IFormView view) {
        model.setValue(JOBLEVELRANG, null);
        model.setValue(HIGHJOBLEVEL, null);
        model.setValue(LOWJOBLEVEL, null);
        view.updateView(JOBLEVELRANG);
    }
}
