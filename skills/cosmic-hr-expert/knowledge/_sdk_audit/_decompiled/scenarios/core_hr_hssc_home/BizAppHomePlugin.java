/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.cache.AppCache
 *  kd.bos.entity.datamodel.events.BizDataEventArgs
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.events.ClickEvent
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.TabSelectEvent
 *  kd.bos.form.control.events.TabSelectListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.mvc.form.FormView
 *  kd.bos.mvc.list.ListView
 *  kd.bos.servicehelper.DispatchServiceHelper
 */
package kd.bd.gbs.plugin.backgroundtask.form;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import kd.bos.bill.BillShowParameter;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.cache.AppCache;
import kd.bos.entity.datamodel.events.BizDataEventArgs;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.container.Tab;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.ClickEvent;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.TabSelectEvent;
import kd.bos.form.control.events.TabSelectListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.mvc.form.FormView;
import kd.bos.mvc.list.ListView;
import kd.bos.servicehelper.DispatchServiceHelper;

public class BizAppHomePlugin
extends AbstractFormPlugin
implements TabSelectListener {
    public void registerListener(EventObject e) {
        super.registerListener(e);
        Tab apptabs = (Tab)this.getView().getControl("_submaintab_");
        if (apptabs != null) {
            apptabs.addTabSelectListener((TabSelectListener)this);
            apptabs.addClickListener((ClickListener)this);
        }
        this.addItemClickListeners(new String[]{"toolbar"});
        this.addClickListeners(new String[]{"bgtaskviewfeature"});
        this.addClickListeners(new String[]{"closebgtaskappfeature"});
    }

    public void createNewData(BizDataEventArgs e) {
        super.createNewData(e);
        this.getView().setVisible(Boolean.FALSE, new String[]{"backtaskflexpanel"});
    }

    public void click(EventObject evt) {
        if (evt instanceof ClickEvent) {
            ClickEvent clickEvent = (ClickEvent)evt;
            Map paramsMap = clickEvent.getParamsMap();
            Control c = (Control)evt.getSource();
            String clickKey = c.getKey();
            if ("_submaintab_".equals(clickKey) && null != paramsMap) {
                Tab apptabs = (Tab)this.getView().getControl("_submaintab_");
                if (apptabs != null) {
                    String currentTab = apptabs.getCurrentTab();
                    IFormView view = this.getView().getViewNoPlugin(currentTab);
                    if (view == null) {
                        return;
                    }
                    if ("backgroundTask".equals(paramsMap.get("controlKey")) || "backgroundTaskGrey".equals(paramsMap.get("controlKey"))) {
                        String billFormId;
                        if (view instanceof ListView) {
                            billFormId = ((ListView)view).getBillFormId();
                        } else {
                            billFormId = (String)view.getFormShowParameter().getCustomParams().get("billFormId");
                            if (billFormId == null) {
                                billFormId = view.getFormShowParameter().getFormId();
                            }
                        }
                        if (billFormId == null) {
                            billFormId = view.getFormShowParameter().getFormId();
                        }
                        if (view instanceof ListView) {
                            this.showFormByList(billFormId);
                        } else if (view instanceof FormView) {
                            this.showForm(billFormId, view);
                        }
                    }
                }
            } else if ("bgtaskviewfeature".equals(clickKey)) {
                this.getView().setVisible(Boolean.FALSE, new String[]{"backtaskflexpanel"});
                IFormView formViewByMainTab = this.getFormViewByMainTab();
                if (formViewByMainTab instanceof ListView) {
                    ListView listView = (ListView)formViewByMainTab;
                    this.showFormByList(listView.getBillFormId());
                } else {
                    this.showForm(formViewByMainTab.getFormShowParameter().getFormId(), formViewByMainTab);
                }
            } else if ("closebgtaskappfeature".equals(clickKey)) {
                this.getView().setVisible(Boolean.FALSE, new String[]{"backtaskflexpanel"});
            }
        }
    }

    private IFormView getFormViewByMainTab() {
        Tab apptabs = (Tab)this.getView().getControl("_submaintab_");
        String currentTab = apptabs.getCurrentTab();
        return this.getView().getViewNoPlugin(currentTab);
    }

    private void showForm(String billFormId, IFormView view) {
        Object id = view.getModel().getValue("id");
        if (id == null) {
            return;
        }
        String idStr = String.valueOf(id);
        Object number = null;
        try {
            number = view.getModel().getValue("number");
        }
        catch (Exception e1) {
            number = view.getModel().getValue("billno");
        }
        IClientViewProxy proxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
        HashMap<String, Object> o1 = new HashMap<String, Object>(1);
        o1.put("dc", "right");
        o1.put("formId", "gbs_bgtaskdetailsidebar");
        HashMap<String, String> map = new HashMap<String, String>(1);
        map.put("top", "80");
        o1.put("offsetInAllDC", map);
        HashMap<String, String> styleMap = new HashMap<String, String>(1);
        styleMap.put("width", "360px");
        o1.put("style", styleMap);
        HashMap<String, Object> customMap = new HashMap<String, Object>(3);
        customMap.put("billNo", number);
        customMap.put("id", idStr);
        customMap.put("billType", billFormId);
        o1.put("params", customMap);
        proxy.addAction("showSlideBill", o1);
    }

    private void showFormByList(String billFormId) {
        IClientViewProxy proxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
        HashMap<String, Object> o1 = new HashMap<String, Object>(1);
        o1.put("dc", "right");
        o1.put("formId", "gbs_bgtasklistsidebar");
        HashMap<String, String> customMap = new HashMap<String, String>(3);
        customMap.put("billType", billFormId);
        o1.put("params", customMap);
        HashMap<String, String> map = new HashMap<String, String>(1);
        map.put("top", "82");
        o1.put("offsetInAllDC", map);
        HashMap<String, String> styleMap = new HashMap<String, String>(1);
        styleMap.put("width", "360px");
        o1.put("style", styleMap);
        AppCache.get((String)"gbs").put("pageId", (Object)this.getView().getPageId());
        AppCache.get((String)"gbs").put("billType", (Object)billFormId);
        proxy.addAction("showSlideBill", o1);
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        if ("openslipbybgtask".equals(afterDoOperationEventArgs.getOperateKey())) {
            FormOperate formOperate = (FormOperate)afterDoOperationEventArgs.getSource();
            OperateOption option = formOperate.getOption();
            String billType = option.getVariableValue("billType");
            String billId = option.getVariableValue("billId");
            BillShowParameter showParameter = new BillShowParameter();
            showParameter.setFormId(billType);
            showParameter.setPkId((Object)billId);
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            this.getView().showForm((FormShowParameter)showParameter);
        }
    }

    public void tabSelected(TabSelectEvent tabSelectEvent) {
        Tab apptabs = (Tab)this.getView().getControl("_submaintab_");
        this.getView().setVisible(Boolean.FALSE, new String[]{"backtaskflexpanel"});
        boolean highLightFlag = false;
        boolean unHighLightFlag = false;
        if (apptabs != null) {
            String currentTab = apptabs.getCurrentTab();
            if (!"appmiantab".equals(currentTab)) {
                Boolean flag;
                String billFormId;
                IFormView view = this.getView().getViewNoPlugin(currentTab);
                if (view == null) {
                    return;
                }
                if (view instanceof ListView) {
                    billFormId = ((ListView)view).getBillFormId();
                } else {
                    billFormId = (String)view.getFormShowParameter().getCustomParams().get("billFormId");
                    if (billFormId == null) {
                        billFormId = view.getFormShowParameter().getFormId();
                    }
                }
                if (billFormId == null) {
                    billFormId = view.getFormShowParameter().getFormId();
                }
                if ((flag = (Boolean)DispatchServiceHelper.invokeBizService((String)"bd", (String)"gbs", (String)"BgTaskExceptionService", (String)"getBackgroundTaskConfig", (Object[])new Object[]{billFormId})).booleanValue()) {
                    Long lastDay = (Long)DispatchServiceHelper.invokeBizService((String)"bd", (String)"gbs", (String)"BgTaskExceptionService", (String)"getLastDay", (Object[])new Object[0]);
                    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(lastDay));
                    int hasExceptionInfo = (Integer)DispatchServiceHelper.invokeBizService((String)"bd", (String)"gbs", (String)"BgTaskExceptionService", (String)"hasExceptionInfo", (Object[])new Object[]{billFormId, timestamp, 200});
                    if (hasExceptionInfo == 4) {
                        unHighLightFlag = true;
                    } else {
                        highLightFlag = true;
                    }
                    this.showSlip(hasExceptionInfo);
                }
            }
            IClientViewProxy proxy1 = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("id", "backgroundTask");
            map1.put("icon", "kdfont-yujing4");
            map1.put("title", ResManager.loadKDString((String)"\u5f02\u5e38\u670d\u52a1", (String)"BizAppHomePlugin_2", (String)"bos-ext-bd", (Object[])new Object[0]));
            map1.put("vi", highLightFlag);
            ArrayList tabIconList = new ArrayList();
            tabIconList.add(map1);
            HashMap<String, Object> map3 = new HashMap<String, Object>();
            map3.put("id", "backgroundTaskGrey");
            map3.put("icon", "kdfont-yujing4");
            map3.put("title", ResManager.loadKDString((String)"\u5f02\u5e38\u670d\u52a1", (String)"BizAppHomePlugin_2", (String)"bos-ext-bd", (Object[])new Object[0]));
            map3.put("vi", unHighLightFlag);
            tabIconList.add(map3);
            proxy1.invokeControlMethod("_submaintab_", "setTabIcons", new Object[]{tabIconList});
            IClientViewProxy proxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
            HashMap<String, Object> o1 = new HashMap<String, Object>(1);
            o1.put("dc", "right");
            o1.put("formId", "gbs_bgtasklistsidebar");
            HashMap<String, String> map = new HashMap<String, String>(1);
            map.put("top", "82");
            o1.put("offsetInAllDC", map);
            HashMap<String, String> styleMap = new HashMap<String, String>(1);
            styleMap.put("width", "360px");
            o1.put("style", styleMap);
            proxy.addAction("setSlideBillFormId", o1);
            HashMap<String, Object> o2 = new HashMap<String, Object>(1);
            o2.put("dc", "right");
            o2.put("formId", "gbs_bgtaskdetailsidebar");
            HashMap<String, String> map2 = new HashMap<String, String>(1);
            map2.put("top", "82");
            o2.put("offsetInAllDC", map2);
            o2.put("style", styleMap);
            proxy.addAction("setSlideBillFormId", o2);
        }
    }

    private void showSlip(int hasExceptionInfo) {
        if (hasExceptionInfo == 1) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"backtaskflexpanel"});
            ConfirmCallBackListener confirmCallBackListener = new ConfirmCallBackListener("show", (IFormPlugin)this);
            this.getView().showConfirm(ResManager.loadKDString((String)"\u5b58\u5728\u5f02\u5e38\u5355\u636e\uff0c\u8bf7\u53ca\u65f6\u5904\u7406\u3002", (String)"BizAppHomePlugin_1", (String)"bos-ext-bd", (Object[])new Object[0]), MessageBoxOptions.YesNo, confirmCallBackListener);
        } else if (hasExceptionInfo == 2) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"backtaskflexpanel"});
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{"backtaskflexpanel"});
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        super.confirmCallBack(messageBoxClosedEvent);
        if (messageBoxClosedEvent.getCallBackId().equals("show") && MessageBoxResult.Yes.equals((Object)messageBoxClosedEvent.getResult())) {
            IFormView formViewByMainTab = this.getFormViewByMainTab();
            if (formViewByMainTab instanceof ListView) {
                ListView listView = (ListView)formViewByMainTab;
                this.showFormByList(listView.getBillFormId());
            } else {
                this.showForm(formViewByMainTab.getFormShowParameter().getFormId(), formViewByMainTab);
            }
        }
    }
}
