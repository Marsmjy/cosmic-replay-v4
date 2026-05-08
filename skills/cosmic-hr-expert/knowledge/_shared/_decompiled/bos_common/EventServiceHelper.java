/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bec.api.IEventService
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.script.annotations.KSMethod
 *  kd.bos.script.annotations.KSObject
 *  kd.bos.service.ServiceFactory
 *  kd.bos.thread.ThreadTruck
 *  kd.sdk.annotation.SdkPublic
 *  kd.sdk.annotation.SdkService
 */
package kd.bos.servicehelper.workflow;

import kd.bos.bec.api.IEventService;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.script.annotations.KSMethod;
import kd.bos.script.annotations.KSObject;
import kd.bos.service.ServiceFactory;
import kd.bos.thread.ThreadTruck;
import kd.sdk.annotation.SdkPublic;
import kd.sdk.annotation.SdkService;

@KSObject
@SdkPublic
@SdkService(name="\u4e1a\u52a1\u4e8b\u4ef6\u4e2d\u5fc3\u670d\u52a1")
public class EventServiceHelper {
    public static final String IFEVENTFROMAPI = "ifEventFromApi";

    @KSMethod
    public static void triggerEventSubscribeJobs(String eventNumber, String businessKey, String entityNumber) {
        IEventService eventService = EventServiceHelper.getEventService();
        TXHandle tx = TX.required();
        try {
            EventServiceHelper.setThreadLocalVariables();
            eventService.triggerEventSubscribeJobs(eventNumber, businessKey, entityNumber);
        }
        catch (Exception e) {
            tx.markRollback();
            throw e;
        }
        finally {
            EventServiceHelper.removeThreadLocalVariables();
            tx.close();
        }
    }

    @KSMethod
    public static void triggerEventSubscribe(String eventNumber, String json) {
        IEventService eventService = EventServiceHelper.getEventService();
        TXHandle tx = TX.required();
        try {
            EventServiceHelper.setThreadLocalVariables();
            eventService.triggerEventSubscribe(eventNumber, json);
        }
        catch (Exception e) {
            tx.markRollback();
            throw e;
        }
        finally {
            EventServiceHelper.removeThreadLocalVariables();
            tx.close();
        }
    }

    @KSMethod
    public static void triggerEventSubscribeJobs(String eventNumber, DynamicObject[] objs) {
        IEventService eventService = EventServiceHelper.getEventService();
        TXHandle tx = TX.required();
        try {
            EventServiceHelper.setThreadLocalVariables();
            eventService.triggerEventSubscribeJobs(eventNumber, objs);
        }
        catch (Exception e) {
            tx.markRollback();
            throw e;
        }
        finally {
            EventServiceHelper.removeThreadLocalVariables();
            tx.close();
        }
    }

    private static IEventService getEventService() {
        return (IEventService)ServiceFactory.getService(IEventService.class);
    }

    private static void removeThreadLocalVariables() {
        ThreadTruck.remove((Object)IFEVENTFROMAPI);
    }

    private static void setThreadLocalVariables() {
        ThreadTruck.put((Object)IFEVENTFROMAPI, (Object)"true");
    }
}
