/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.hr.hbp.common.ext.BosPositionValidateReq
 *  kd.hr.hbp.common.ext.BosPositionValidateResult
 *  kd.hr.hbp.common.util.InstanceGenUtil
 */
package kd.hrmp.hbpm.business.domain.position.service;

import java.util.List;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.hbp.common.ext.BosPositionValidateReq;
import kd.hr.hbp.common.ext.BosPositionValidateResult;
import kd.hr.hbp.common.util.InstanceGenUtil;
import kd.hrmp.hbpm.business.domain.position.service.impl.BosPositionServiceImpl;

public interface IBosPositionService {
    public static IBosPositionService getInstance() {
        return (IBosPositionService)InstanceGenUtil.getSingletonInstance(BosPositionServiceImpl.class);
    }

    public Map<String, List<Long>> addWaitSyncLogs(DynamicObject[] var1, DynamicObject[] var2);

    public List<BosPositionValidateResult> bosPositionValidate(List<BosPositionValidateReq> var1);

    public void addOrUpdatePositions(List<Long> var1);

    public void enablePositions(List<Long> var1);

    public void disablePositions(List<Long> var1);

    public void commonSyncPositions();

    public boolean isDisableSyncPlatformPosition();

    public void clear();
}
