/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.ExtendedDataEntity
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.haos.business.application.service.init.IOrgHisInitValidService
 *  kd.hr.haos.business.application.service.init.impl.OrgHisInitValidServiceImpl
 *  kd.hr.haos.common.constants.init.AdminOrgInitTipsEnum
 *  kd.hr.haos.common.util.init.AdminOrgInitCommonUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.haos.opplugin.web.adminorg.init.validate;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.haos.business.application.service.init.IOrgHisInitValidService;
import kd.hr.haos.business.application.service.init.impl.OrgHisInitValidServiceImpl;
import kd.hr.haos.common.constants.init.AdminOrgInitTipsEnum;
import kd.hr.haos.common.util.init.AdminOrgInitCommonUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;

public abstract class OrgHisAbstractValidator
extends AbstractValidator {
    private static final String ORG_SELECT_FIELDS = "id,boid,firstbsed,bsed,bsled,datastatus,initdatasource,belongcompany.id,adminorgtype.id,iscurrentversion,number,parentorg.id";
    protected LinkedHashMap<Long, StringBuilder> validErrMap = new LinkedHashMap(16);
    protected HRBaseServiceHelper adminOrgServiceHelper = new HRBaseServiceHelper("haos_adminorg");
    protected IOrgHisInitValidService adminOrgHisDateContinuityValidService = new OrgHisInitValidServiceImpl();
    protected Map<Long, DynamicObject> dbEffBizBoDynMap = new ConcurrentHashMap<Long, DynamicObject>(16);
    protected Map<Long, List<DynamicObject>> dbEffOrgVerDynListMap = new ConcurrentHashMap<Long, List<DynamicObject>>(16);
    protected Map<Long, List<DynamicObject>> dbEffAndDiscardOrgVerMap = new ConcurrentHashMap<Long, List<DynamicObject>>(16);
    public static final String VALID_FAIL_VID_LIST_KEY = "validFailVidList";
    public static final String DB_EFF_ORG_DYN_LIST_KEY = "dbEffOrgDynList";
    public static final String VALID_ERR_MAP_KEY = "validErrMap";
    protected List<DynamicObject> hisOrgDynList = new ArrayList<DynamicObject>(10);
    private static final long DAY_MILLISECONDS = 86400000L;
    private static final String INIT_ERR_RELATION_001 = "INIT_ERR_RELATION_001";

    public void validate() {
        this.initData();
        this.bizValidate(this.hisOrgDynList);
        this.getOption().setVariableValue(VALID_ERR_MAP_KEY, JSONObject.toJSONString(this.validErrMap));
    }

    protected void resetData() {
        this.dbEffBizBoDynMap.clear();
        this.dbEffOrgVerDynListMap.clear();
        this.validErrMap.clear();
        if (this.hisOrgDynList != null) {
            this.hisOrgDynList.clear();
        }
    }

    protected abstract void bizValidate(List<DynamicObject> var1);

    private void initData() {
        List dbEffAndDiscardOrgHisList;
        OperateOption option = this.getOption();
        String dbEffOrgDynListStr = option.containsVariable(DB_EFF_ORG_DYN_LIST_KEY) ? this.getOption().getVariableValue(DB_EFF_ORG_DYN_LIST_KEY) : "";
        String validErrMapStr = option.containsVariable(VALID_ERR_MAP_KEY) ? option.getVariableValue(VALID_ERR_MAP_KEY) : "";
        this.hisOrgDynList.addAll(Arrays.stream(this.getDataEntities()).map(ExtendedDataEntity::getDataEntity).collect(Collectors.toList()));
        if (HRCollUtil.isEmpty(this.hisOrgDynList)) {
            return;
        }
        if (StringUtils.isEmpty((CharSequence)dbEffOrgDynListStr)) {
            this.hisOrgDynList.sort(Comparator.comparing(dyn -> dyn.getDate("bsed")));
            this.adminOrgHisDateContinuityValidService.initHisOrgValidErrMap(this.hisOrgDynList, this.validErrMap);
            LinkedHashMap boDynListMap = this.hisOrgDynList.stream().collect(Collectors.groupingBy(dyn -> dyn.getLong("boid"), LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(dyn -> dyn.getDate("bsed"))).collect(Collectors.toList()))));
            DynamicObject[] effAndDiscardOrgHisArr = this.adminOrgServiceHelper.queryOriginalArray(ORG_SELECT_FIELDS, new QFilter[]{new QFilter("boid", "in", boDynListMap.keySet()), new QFilter("datastatus", "in", (Object)Sets.newHashSet((Object[])new String[]{HisModelDataStatusEnum.EFFECTING.getStatus(), HisModelDataStatusEnum.DISCARDED.getStatus()}))}, "bsed");
            List validOrgHisDynList = Arrays.stream(effAndDiscardOrgHisArr).filter(dyn -> HRStringUtils.equals((String)dyn.getString("datastatus"), (String)HisModelDataStatusEnum.EFFECTING.getStatus())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(validOrgHisDynList)) {
                this.hisOrgDynList.forEach(dyn -> AdminOrgInitCommonUtils.buildValidFailErrMap((DynamicObject)dyn, this.validErrMap, (String)AdminOrgInitTipsEnum.ADMIN_ORG_BO_NOT_EXIST_ERR.getInfo()));
                return;
            }
            dbEffAndDiscardOrgHisList = Arrays.stream(effAndDiscardOrgHisArr).collect(Collectors.toList());
            this.setHisVerEndDate(boDynListMap);
            this.getOption().setVariableValue(DB_EFF_ORG_DYN_LIST_KEY, SerializationUtils.serializeToBase64(dbEffAndDiscardOrgHisList));
        } else {
            dbEffAndDiscardOrgHisList = (List)SerializationUtils.deSerializeFromBase64((String)dbEffOrgDynListStr);
        }
        this.buildDbEffDynMap(dbEffAndDiscardOrgHisList);
        if (StringUtils.isNotEmpty((CharSequence)validErrMapStr)) {
            Map errMap = (Map)JSONObject.parseObject((String)validErrMapStr, HashMap.class);
            for (Map.Entry entry : errMap.entrySet()) {
                this.validErrMap.put((Long)entry.getKey(), new StringBuilder((String)entry.getValue()));
            }
        }
    }

    protected void oderByByStartDate(List<DynamicObject> hisOrgDynList) {
        if (CollectionUtils.isEmpty(hisOrgDynList)) {
            return;
        }
        hisOrgDynList.sort(Comparator.comparing(dyn -> dyn.getDate("bsed")));
    }

    protected void setHisVerEndDate(LinkedHashMap<Long, List<DynamicObject>> boDynListMap) {
        for (Map.Entry<Long, List<DynamicObject>> entry : boDynListMap.entrySet()) {
            List<DynamicObject> hisVerDynList = entry.getValue();
            if (CollectionUtils.isEmpty(hisVerDynList)) continue;
            DynamicObject startVerDyn = hisVerDynList.get(0);
            for (int i = 1; i < hisVerDynList.size(); ++i) {
                DynamicObject endVerDyn = hisVerDynList.get(i);
                Date startDynBsledDate = startVerDyn.getDate("bsled");
                if (startDynBsledDate == null && endVerDyn.getDate("bsed").getTime() - startVerDyn.getDate("bsed").getTime() >= 86400000L) {
                    startVerDyn.set("bsled", (Object)HRDateTimeUtils.addDay((Date)endVerDyn.getDate("bsed"), (long)-1L));
                }
                startVerDyn = endVerDyn;
            }
        }
    }

    private void buildDbEffDynMap(List<DynamicObject> dbEffAndDiscardOrgHisList) {
        if (CollectionUtils.isEmpty(dbEffAndDiscardOrgHisList)) {
            return;
        }
        this.dbEffBizBoDynMap.putAll(dbEffAndDiscardOrgHisList.stream().filter(dyn -> dyn.getBoolean("iscurrentversion")).collect(Collectors.toMap(dyn -> dyn.getLong("id"), dyn -> dyn, (key1, key2) -> key1)));
        this.dbEffOrgVerDynListMap = dbEffAndDiscardOrgHisList.stream().filter(dyn -> HRStringUtils.equals((String)dyn.getString("datastatus"), (String)HisModelDataStatusEnum.EFFECTING.getStatus())).filter(dyn -> !dyn.getBoolean("iscurrentversion")).collect(Collectors.groupingBy(dyn -> dyn.getLong("boid")));
        this.dbEffAndDiscardOrgVerMap = dbEffAndDiscardOrgHisList.stream().filter(dyn -> !dyn.getBoolean("iscurrentversion")).collect(Collectors.groupingBy(dyn -> dyn.getLong("boid")));
    }

    protected void addHisOrgErrMsg(LinkedHashMap<Long, StringBuilder> validErrMap, Set<Long> allFailIdSet) {
        if (ObjectUtils.isEmpty(validErrMap)) {
            return;
        }
        for (ExtendedDataEntity dataEntity : this.getDataEntities()) {
            DynamicObject bizDyn = dataEntity.getDataEntity();
            StringBuilder errSb = validErrMap.get(bizDyn.getLong("id"));
            if (StringUtils.isNotEmpty((CharSequence)errSb.toString())) {
                this.addErrorMessage(dataEntity, errSb.toString());
                continue;
            }
            if (!allFailIdSet.contains(bizDyn.getLong("id"))) continue;
            this.setAddBillNoForContent(false);
            this.addErrorMessage(dataEntity, INIT_ERR_RELATION_001);
            this.setAddBillNoForContent(true);
        }
    }
}
