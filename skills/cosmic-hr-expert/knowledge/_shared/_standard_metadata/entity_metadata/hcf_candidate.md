# hcf_candidate — 拟入职人员

**表单编码**: `hcf_candidate`  
**表单ID**: `15W/UEVF+QP3`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_candidate（拟入职人员） [BaseEntity]

### 物理表（垂直拆分表）

| 表名 | 说明 |
|------|------|
| `t_hcf_candidate` | 主表 |
| `t_hcf_candidate_A` | 候选人拆分表A |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hcf_candidate.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hcf_candidate.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hcf_candidate.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hcf_candidate.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hcf_candidate.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hcf_candidate.finitdatasource |  |  |
| number | 编号 | TextField | t_hcf_candidate.fnumber | ✓ |  |
| appfileid | 候选人ID | BigIntField | t_hcf_candidate.fappfileid |  |  |
| nameen | 拼音名 | TextField | t_hcf_candidate.fnameen |  |  |
| enname | 英文名 | TextField | t_hcf_candidate.fenname |  |  |
| gender | 性别 | BasedataField | t_hcf_candidate.fgenderid |  | hbss_sex |
| nationality | 国籍 | BasedataField | t_hcf_candidate.fnationalityid |  | hbss_nationality |
| folk | 民族 | BasedataField | t_hcf_candidate.ffolkid |  | hbss_flok |
| birthday | 出生日期 | DateField | t_hcf_candidate.fbirthday |  |  |
| height | 身高 | IntegerField | t_hcf_candidate.fheight |  |  |
| childrennumber | 子女数 | IntegerField | t_hcf_candidate.fchildrennumber |  |  |
| headsculpture | 头像 | PictureField | t_hcf_candidate.fheadsculpture |  |  |
| joinworktime | 参加工作日期 | DateField | t_hcf_candidate.fjoinworktime |  |  |
| age | 年龄 | IntegerField | t_hcf_candidate.fage |  |  |
| procreatstatus | 生育状况 | BasedataField | t_hcf_candidate.fprocreatstatusid |  | hbss_procreatstatus |
| healthstatus | 健康状况 | BasedataField | t_hcf_candidate.fhealthstatusid |  | hbss_healthstatus |
| constellation | 星座 | BasedataField | t_hcf_candidate.fconstellationid |  | hbss_constellation |
| symbolicanimals | 生肖 | BasedataField | t_hcf_candidate.fsymbolicanimalsid |  | hbss_zodiac |
| title | 头衔 | MuliLangTextField | t_hcf_candidate.ftitle |  |  |
| marriagestatus | 婚姻状况 | BasedataField | t_hcf_candidate.fmarriagestatusid |  | hbss_marriagestatus |
| lunarcalendarbirthday | 农历生日 | DateField | t_hcf_candidate.flunarcalendarbirthday |  |  |
| julianbirthday | 公历生日 | DateField | t_hcf_candidate.fjulianbirthday |  |  |
| birthdaytype | 生日类型 | ComboField | t_hcf_candidate.fbirthdaytype |  |  |
| displayname | 显示名 | TextField | t_hcf_candidate.fdisplayname |  |  |
| formername | 曾用名 | TextField | t_hcf_candidate.fformername |  |  |
| name | 姓名 | TextField | t_hcf_candidate.fname | ✓ |  |
| nativelngname | 本地语言姓名 | TextField | t_hcf_candidate.fnativelngname |  |  |
| nbloodtype | 血型 | BasedataField | t_hcf_candidate.fnbloodtypeid |  | hbss_bloodtype |
| marriageregistdate | 结婚登记日期 | DateField | t_hcf_candidate.fmarriageregistdate |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcf_candidate（主表） | 34 |

