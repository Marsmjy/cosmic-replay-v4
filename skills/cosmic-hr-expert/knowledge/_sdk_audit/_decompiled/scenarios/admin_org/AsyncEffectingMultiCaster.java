/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.trace.util.TraceIdUtil
 *  kd.hr.haos.business.domain.org.async.AsyncEffectingContext
 *  kd.hr.haos.business.domain.org.async.AsyncEffectingMultiCaster$EventTask
 *  kd.hr.haos.business.domain.org.async.AsyncEffectingReformHelper
 *  kd.hr.haos.business.domain.org.async.event.AsyncEffectingEvent
 *  kd.hr.haos.business.domain.org.async.listener.AbstractAsyncEffectingListener$AsyncEffectingHandleCascadeListener
 *  kd.hr.haos.business.domain.org.async.listener.AbstractAsyncEffectingListener$AsyncEffectingHandleOtherListener
 *  kd.hr.haos.business.domain.org.async.listener.AbstractAsyncEffectingListener$AsyncEffectingSaveAdminListener
 *  kd.hr.haos.business.domain.org.async.listener.AbstractAsyncEffectingListener$AsyncEffectingSaveChangeDetailListener
 *  kd.hr.haos.business.domain.org.async.listener.AbstractAsyncEffectingListener$AsyncEffectingSaveOtherListener
 *  kd.hr.haos.business.domain.org.async.listener.AbstractAsyncEffectingListener$AsyncEffectingSaveRollBackListener
 *  kd.hr.haos.business.domain.org.async.listener.AbstractAsyncEffectingListener$AsyncEffectingSendMessageListener
 *  kd.hr.haos.business.domain.org.async.listener.AbstractAsyncEffectingListener$AsyncEffectingStartedListener
 *  kd.hr.haos.business.domain.org.async.listener.AsyncEffectingListener
 */
package kd.hr.haos.business.domain.org.async;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.trace.util.TraceIdUtil;
import kd.hr.haos.business.domain.org.async.AsyncEffectingContext;
import kd.hr.haos.business.domain.org.async.AsyncEffectingMultiCaster;
import kd.hr.haos.business.domain.org.async.AsyncEffectingReformHelper;
import kd.hr.haos.business.domain.org.async.event.AsyncEffectingEvent;
import kd.hr.haos.business.domain.org.async.listener.AbstractAsyncEffectingListener;
import kd.hr.haos.business.domain.org.async.listener.AsyncEffectingListener;

public class AsyncEffectingMultiCaster {
    public static final Integer OK = 1;
    public static final Integer ERROR = 0;
    List<AsyncEffectingListener> listeners = new ArrayList<AsyncEffectingListener>();
    private final List<AsyncEffectingContext> contexts;
    private boolean asyncRun = false;

    public AsyncEffectingMultiCaster(DynamicObject[] orgData) {
        this.initSelf();
        this.contexts = Lists.newArrayListWithExpectedSize((int)orgData.length);
        for (DynamicObject org : orgData) {
            AsyncEffectingContext context = new AsyncEffectingContext();
            context.setOrg(org);
            context.setChangeOperateId(Long.valueOf(org.getLong("changetype.id")));
            this.contexts.add(context);
        }
    }

    private void initSelf() {
        this.listeners.add((AsyncEffectingListener)new AbstractAsyncEffectingListener.AsyncEffectingStartedListener());
        this.listeners.add((AsyncEffectingListener)new AbstractAsyncEffectingListener.AsyncEffectingHandleCascadeListener());
        this.listeners.add((AsyncEffectingListener)new AbstractAsyncEffectingListener.AsyncEffectingSaveAdminListener());
        this.listeners.add((AsyncEffectingListener)new AbstractAsyncEffectingListener.AsyncEffectingSaveOtherListener());
        this.listeners.add((AsyncEffectingListener)new AbstractAsyncEffectingListener.AsyncEffectingHandleOtherListener());
        this.listeners.add((AsyncEffectingListener)new AbstractAsyncEffectingListener.AsyncEffectingSaveChangeDetailListener());
        this.listeners.add((AsyncEffectingListener)new AbstractAsyncEffectingListener.AsyncEffectingSendMessageListener());
        this.listeners.add((AsyncEffectingListener)new AbstractAsyncEffectingListener.AsyncEffectingSaveRollBackListener());
    }

    public void dispatchEvent(AsyncEffectingEvent effectingEvent) {
        Future submit = AsyncEffectingReformHelper.EVENT_HANDLE_THREAD_POOL.submit((Callable)new EventTask(this.listeners, effectingEvent));
        try {
            submit.get();
        }
        catch (Throwable throwable) {
            String message = String.format(ResManager.loadKDString((String)"\u5f53\u524d\u529f\u80fd\u51fa\u73b0\u591a\u7ebf\u7a0b\u8fd0\u884c\u5f02\u5e38\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff1b\u5f02\u5e38traceId\uff1a%1$s\u3002", (String)"AsyncEffectingMultiCaster_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]), TraceIdUtil.getCurrentTraceIdString());
            throw new KDBizException(new ErrorCode("AsyncEffectingReformHelper execute error", message), new Object[0]);
        }
    }

    public List<AsyncEffectingContext> getContexts() {
        return this.contexts;
    }

    public void asyncRun() {
        this.asyncRun = true;
    }

    public boolean isAsyncRun() {
        return this.asyncRun;
    }
}
