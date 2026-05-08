# hrpi_employee — 员工

**表单编码**: `hrpi_employee`  
**表单ID**: `/IR5TB9YFR5S`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_employee（员工） [BaseEntity]

### 物理表（垂直拆分表）

| 表名 | 说明 |
|------|------|
| `t_hrpi_employee` | 主表 |
| `t_hrpi_employee_a` | 扩展表a,用于二开字段存储 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrpi_employee.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrpi_employee.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrpi_employee.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrpi_employee.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrpi_employee.finitdatasource |  |  |
| boid | 业务ID | BigIntField | t_hrpi_employee.fboid |  |  |
| iscurrentversion | 当前生效数据 | CheckBoxField | t_hrpi_employee.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hrpi_employee.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hrpi_employee.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hrpi_employee.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hrpi_employee.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hrpi_employee.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hrpi_employee.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hrpi_employee.fhisversion |  |  |
| index | 人员序号 | IntegerField | t_hrpi_employee.findex | ✓ |  |
| globalperson | 全球员工 | BasedataField | t_hrpi_employee.fglobalpersonid |  | hrpi_globalperson |
| oldempnumber | 前工号 | TextField | t_hrpi_employee.foldempnumber |  |  |
| number | 员工系统ID | TextField | t_hrpi_employee.fnumber |  |  |
| empnumber | 工号 | TextField | t_hrpi_employee.fempnumber | ✓ |  |
| isprimary | 主员工标识 | CheckBoxField | t_hrpi_employee.fisprimary |  |  |
| primaryemployee | 主员工 | BasedataField | t_hrpi_employee.fprimaryemployeeid |  | hrpi_employee |
| procreatstatus | 生育状况 | BasedataField | t_hrpi_employee.fprocreatstatusid |  | hbss_procreatstatus |
| marriagestatus | 婚姻状况 | BasedataField | t_hrpi_employee.fmarriagestatusid |  | hbss_marriagestatus |
| healthstatus | 健康状况 | BasedataField | t_hrpi_employee.fhealthstatusid |  | hbss_healthstatus |
| childrennumber | 子女数 | IntegerField | t_hrpi_employee.fchildrennumber |  |  |
| name | 姓名 | TextField | t_hrpi_employee.fname | ✓ |  |
| headsculpture | 头像 | PictureField | t_hrpi_employee.fheadsculpture |  |  |
| nameen | 拼音名 | TextField | t_hrpi_employee.fnameen |  |  |
| enname | 英文名 | TextField | t_hrpi_employee.fenname |  |  |
| displayname | 显示名 | TextField | t_hrpi_employee.fdisplayname |  |  |
| title | 头衔 | MuliLangTextField | t_hrpi_employee.ftitle |  |  |
| nativelngname | 本地语言姓名 | TextField | t_hrpi_employee.fnativelngname |  |  |
| formername | 曾用名 | TextField | t_hrpi_employee.fformername |  |  |
| gender | 性别 | BasedataField | t_hrpi_employee.fgenderid |  | hbss_sex |
| nationality | 国籍 | BasedataField | t_hrpi_employee.fnationalityid |  | hbss_nationality |
| folk | 民族 | BasedataField | t_hrpi_employee.ffolkid |  | hbss_flok |
| symbolicanimals | 生肖 | BasedataField | t_hrpi_employee.fsymbolicanimalsid |  | hbss_zodiac |
| constellation | 星座 | BasedataField | t_hrpi_employee.fconstellationid |  | hbss_constellation |
| nbloodtype | 血型 | BasedataField | t_hrpi_employee.fnbloodtypeid |  | hbss_bloodtype |
| height | 身高 | IntegerField | t_hrpi_employee.fheight |  |  |
| age | 年龄 | IntegerField | t_hrpi_employee.fage |  |  |
| deathdate | 身故日期 | DateField | t_hrpi_employee.fdeathdate |  |  |
| birthday | 出生日期 | DateField | t_hrpi_employee.fbirthday |  |  |
| julianbirthday | 公历生日 | DateField | t_hrpi_employee.fjulianbirthday |  |  |
| lunarcalendarbirthday | 农历生日 | DateField | t_hrpi_employee.flunarcalendarbirthday |  |  |
| marriageregistdate | 结婚登记日期 | DateField | t_hrpi_employee.fmarriageregistdate |  |  |
| birthdaytype | 生日类型 | ComboField | t_hrpi_employee.fbirthdaytype |  |  |
| description | 描述 | MuliLangTextField | t_hrpi_employee.fdescription |  |  |
| oldemployee | 前员工 | BasedataField | t_hrpi_employee.foldemployeeid |  | hrpi_employee |
| sourcesyskey | 来源系统唯一标识 | TextField | t_hrpi_employee.fsourcesyskey |  |  |
| assignment | 组织分配 | BasedataField | t_hrpi_employee.fassignmentid |  | hrpi_assignment |
| isdeleted | 已删除 | CheckBoxField | t_hrpi_employee.fisdeleted |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrpi_employee（主表） | 52 |

