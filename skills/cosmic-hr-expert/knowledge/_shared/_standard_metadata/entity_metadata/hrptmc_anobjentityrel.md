# hrptmc_anobjentityrel — 分析对象实体关联关系

**表单编码**: `hrptmc_anobjentityrel`  
**表单ID**: `2VJTMLQBE0RQ`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_anobjentityrel（分析对象实体关联关系） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_anobjentjoinrel` | BaseEntity | 主表 |
| `t_hrptmc_anobjjoincond` | EntryEntity |  |

### 字段列表 — t_hrptmc_anobjentjoinrel（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_anobjentjoinrel.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_anobjentjoinrel.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_anobjentjoinrel.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_anobjentjoinrel.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_anobjentjoinrel.finitdatasource |  |  |
| entityid | 实体ID | BasedataField | t_hrptmc_anobjentjoinrel.fentityid |  | hrptmc_anobjjoinentity |
| jointype | 关联类型 | ComboField | t_hrptmc_anobjentjoinrel.fjointype |  |  |
| joinentityid | 关联实体ID | BasedataField | t_hrptmc_anobjentjoinrel.fjoinentityid |  | hrptmc_anobjjoinentity |
| leftprop | 左字段 | TextField | t_hrptmc_anobjentjoinrel.fleftprop |  |  |
| comparetype | 比较条件 | ComboField | t_hrptmc_anobjentjoinrel.fcomparetype |  |  |
| rightprop | 右字段 | TextField | t_hrptmc_anobjentjoinrel.frightprop |  |  |
| rightproptype | 右字段类型 | ComboField | t_hrptmc_anobjentjoinrel.frightproptype |  |  |
| rightpropval | 右字段值 | TextField | t_hrptmc_anobjentjoinrel.frightpropval |  |  |
| logictype | 逻辑条件 | ComboField | t_hrptmc_anobjentjoinrel.flogictype |  |  |
| anobj | 分析对象id | BasedataField | t_hrptmc_anobjentjoinrel.fanobjid |  | hrptmc_analyseobject |
| isv | 开发商标识 | TextField | t_hrptmc_anobjentjoinrel.fisv |  |  |
| version | 版本号 | IntegerField | t_hrptmc_anobjentjoinrel.fversion |  |  |
|  |  | EntryEntity | → t_hrptmc_anobjjoincond |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_anobjentjoinrel（主表） | 17 |

