# hrcs_actassignrec — 任务操作记录

**表单编码**: `hrcs_actassignrec`  
**表单ID**: `2+TFQWL4P0MU`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_actassignrec（任务操作记录） [BillEntity]

- **数据库表**: `t_hrcs_actoprec`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 操作人 | CreaterField | fcreatorid |  | bos_user |
| activityins | 活动任务实例 | BasedataField | factivityinsid |  | hrcs_activityins |
| assigntype | 操作 | ComboField | fassigntype |  |  |
| description | 备注 | MuliLangTextField | fdescription |  |  |
| createtime | 操作时间 | CreateDateField | fcreatetime |  |  |
| mulhandler | 分配后处理人 | MulBasedataField | t_hrcs_actinsopnew（子表） |  |  |
| mulhandlerori | 原处理人 | MulBasedataField | t_hrcs_actinsopori（子表） |  |  |
| auditmessage | 处理意见 | MuliLangTextField | fauditmessage |  |  |

