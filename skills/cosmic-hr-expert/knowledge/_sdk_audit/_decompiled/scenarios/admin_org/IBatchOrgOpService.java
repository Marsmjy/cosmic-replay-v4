/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.hr.haos.business.domain.org.async.AsyncEffectingContext
 *  kd.hr.haos.business.domain.org.async.event.AsyncEffectingEvent
 */
package kd.hr.haos.business.domain.org.abs;

import java.util.function.Function;
import kd.hr.haos.business.domain.org.async.AsyncEffectingContext;
import kd.hr.haos.business.domain.org.async.event.AsyncEffectingEvent;

public interface IBatchOrgOpService {
    public void beforeTransDoOp();

    public void doChangeOp();

    public void endDoChangeOp();

    public void afterTransDoOp();

    public boolean isAsync();

    public void runAsync();

    public void asyncLog(Class<? extends AsyncEffectingEvent> var1, Function<AsyncEffectingContext, AsyncEffectingContext> var2);
}
