/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.id.ID
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.log.AttachmentLogInfo
 *  kd.hr.hbp.business.service.timeline.TimelineLogHandler
 *  kd.hr.hbp.common.constants.timeline.TimelineConstants
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.opplugin.web.timeline.log;

import com.alibaba.fastjson.JSON;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.id.ID;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.log.AttachmentLogInfo;
import kd.hr.hbp.business.service.timeline.TimelineLogHandler;
import kd.hr.hbp.common.constants.timeline.TimelineConstants;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public final class TimelineLogOp
extends AbstractOperationServicePlugIn
implements TimelineConstants {
    private static final Log LOG = LogFactory.getLog(TimelineLogOp.class);
    private TimelineLogHandler timeLineLogHandler = new TimelineLogHandler();

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        DynamicObject[] dys = args.getDataEntities();
        if (dys == null || dys.length == 0) {
            return;
        }
        long startTime = System.currentTimeMillis();
        try {
            Map modifyInfoMap;
            this.getOption().setVariableValue("eventId", String.valueOf(ID.genLongId()));
            String attachmentPanelLogInfo = "";
            if (this.getOption().containsVariable("attachmentPanelLogInfo")) {
                attachmentPanelLogInfo = this.getOption().getVariableValue("attachmentPanelLogInfo");
            }
            if (HRStringUtils.isNotEmpty((String)attachmentPanelLogInfo)) {
                List attachmentLogInfoList = JSON.parseArray((String)attachmentPanelLogInfo, AttachmentLogInfo.class);
                modifyInfoMap = this.timeLineLogHandler.buildModifyContent(args.getOperationKey().toLowerCase(Locale.ROOT), dys, attachmentLogInfoList);
            } else {
                modifyInfoMap = this.timeLineLogHandler.buildModifyContent(args.getOperationKey().toLowerCase(Locale.ROOT), dys, null);
            }
            this.timeLineLogHandler.setModifyInfoMap(modifyInfoMap);
        }
        catch (Exception exception) {
            LOG.error((Throwable)exception);
        }
        LOG.info("TimelineLogOp beginOperationTransaction cost\uff1a{} ms", (Object)(System.currentTimeMillis() - startTime));
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        DynamicObject[] dys = args.getDataEntities();
        if (dys == null || dys.length == 0) {
            return;
        }
        long startTime = System.currentTimeMillis();
        List successPkIds = this.getOperationResult().getSuccessPkIds();
        if (successPkIds.size() > 0) {
            try {
                List successDys = Arrays.stream(dys).filter(dy -> successPkIds.contains(dy.getPkValue())).collect(Collectors.toList());
                Map variables = this.getOption().getVariables();
                String eventId = (String)variables.get("eventId");
                String logBizCustomVal = (String)variables.get("logBizCustomVal");
                if (HRStringUtils.isBlank((CharSequence)eventId)) {
                    this.timeLineLogHandler.batchInsertLog(successDys, logBizCustomVal);
                } else {
                    this.timeLineLogHandler.batchInsertLog(successDys, Long.valueOf(eventId), logBizCustomVal, this.timeLineLogHandler.getModifyInfoMap());
                }
            }
            catch (Exception exception) {
                LOG.error((Throwable)exception);
            }
        }
        LOG.info("TimelineLogOp afterExecuteOperationTransaction cost\uff1a{} ms", (Object)(System.currentTimeMillis() - startTime));
    }
}
