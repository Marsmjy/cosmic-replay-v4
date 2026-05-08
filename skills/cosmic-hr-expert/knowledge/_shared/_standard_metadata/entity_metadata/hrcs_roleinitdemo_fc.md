# hrcs_roleinitdemo_fc — 角色初始化功能权限样例

**表单编码**: `hrcs_roleinitdemo_fc`  
**表单ID**: `3FFHFTNS46MI`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_roleinitdemo_fc（角色初始化功能权限样例） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_roleinitdemo_fc` | BaseEntity | 主表 |
| `t_hrcs_roleinitdemo_fcet` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_roleinitdemo_fc（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_roleinitdemo_fc.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_roleinitdemo_fc.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_roleinitdemo_fc.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_roleinitdemo_fc.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_roleinitdemo_fc.finitdatasource |  |  |
| sceneid | 场景ID | IntegerField | t_hrcs_roleinitdemo_fc.fsceneid |  |  |
| scenename | 场景名 | MuliLangTextField | t_hrcs_roleinitdemo_fc.fscenename |  |  |
| scenedesc | 场景描述 | MuliLangTextField | t_hrcs_roleinitdemo_fc.fscenedesc |  |  |
| index | 排序号 | IntegerField | t_hrcs_roleinitdemo_fc.findex |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_roleinitdemo_fcet |  |  |

### 字段列表 — t_hrcs_roleinitdemo_fcet（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| cloudnum | 云编码 | TextField | t_hrcs_roleinitdemo_fcet.fcloudnum |  |  |
| appnum | 应用编码 | TextField | t_hrcs_roleinitdemo_fcet.fappnum |  |  |
| cloudname | 云名称 | MuliLangTextField | t_hrcs_roleinitdemo_fcet.fcloudname |  |  |
| appname | 应用名称 | MuliLangTextField | t_hrcs_roleinitdemo_fcet.fappname |  |  |
| entityname | 业务对象名称 | MuliLangTextField | t_hrcs_roleinitdemo_fcet.fentityname |  |  |
| bucafuncname | 职能类型 | MuliLangTextField | t_hrcs_roleinitdemo_fcet.fbucafuncname |  |  |
| dimname | 维度 | MuliLangTextField | t_hrcs_roleinitdemo_fcet.fdimname |  |  |
| name | 权限项 | MuliLangTextField | t_hrcs_roleinitdemo_fcet.fname |  |  |
| entitynum | 业务对象编码 | MuliLangTextField | t_hrcs_roleinitdemo_fcet.fentitynum |  |  |
| payroll_01 | 薪酬系统管理员 | TextField | t_hrcs_roleinitdemo_fcet.fpayroll_one |  |  |
| payroll_02 | 薪酬成本专员 | TextField | t_hrcs_roleinitdemo_fcet.fpayroll_two |  |  |
| payroll_03 | 人力成本管理员 | TextField | t_hrcs_roleinitdemo_fcet.fpayroll_three |  |  |
| ch_020 | 入职业务管理员 | TextField | t_hrcs_roleinitdemo_fcet.fch_two |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_roleinitdemo_fc（主表） | 9 |
| t_hrcs_roleinitdemo_fcet（单据体） | 13 |

