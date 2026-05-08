# hbss_loginconfig — 登录页配置

**表单编码**: `hbss_loginconfig`  
**表单ID**: `2=45KD7VWJRL`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_loginconfig（登录页配置） [BaseEntity]

- **数据库表**: `t_hbss_loginconfig`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| businessname | 业务大类 | TextField | fbusinessname |  |  |
| loginscene | 登录场景 | BasedataField | floginsceneid | ✓ | hbss_loginscene |
| bannerpic | 登录logo图 | TextField | fbannerpic |  |  |
| backgroundpic | 登录页背景图 | TextField | fbackgroundpic |  |  |
| client | 客户端 | ComboField | fclient |  |  |
| picturefield0_5 | 登录logo图_PC端 | PictureField | — |  |  |
| picturefield1_5 | 登录页背景图_PC端 | PictureField | — |  |  |
| privacystmt | 隐私声明 | BasedataField | fprivacystmtid |  | privacystatement |
| bannergroupapp | logo按钮组app | RadioGroupField | fbannergroupapp |  |  |
| backgroundgroupapp | 背景图按钮组app | RadioGroupField | fbackgroundgroupapp |  |  |
| redirectform | 登录后跳转表单_移动端 | BasedataField | fredirectformid |  | bos_objecttype |
| usertype | 用户类型 | BasedataField | fusertypeid | ✓ | hrcs_privacyusertype |
| logintype | 登录方式 | MulComboField | flogintype | ✓ |  |
| picturefield0_51 | 登录logo图_移动端 | PictureField | — |  |  |
| bannergroup | logo按钮组 | RadioGroupField | fbannergroup |  |  |
| picturefield1_51 | 登录页背景图_移动端 | PictureField | — |  |  |
| backgroundgroup | 背景图按钮组 | RadioGroupField | fbackgroundgroup |  |  |
| appbannerpic | 登录logo图 | TextField | fappbannerpic |  |  |
| appbackgroundpic | 登录页背景图 | TextField | fappbackgroundpic |  |  |
| redirectappformid | 登录后跳转表单_PC端 | BasedataField | fredirectappformid |  | bos_objecttype |
| sysbannerpicl | 系统logoapp非中文 | TextField | fsysbannerpicl |  |  |
| sysbackgroundpic | 系统背景图片app | TextField | fsysbackgroundpic |  |  |
| sysappbannerpic | 系统logopc | TextField | fsysappbannerpic |  |  |
| sysappbackgroundpic | 系统背景图片pc | TextField | fsysappbackgroundpic |  |  |
| sysappbannerpicl | 系统logopc非中文 | TextField | fsysappbannerpicl |  |  |
| sysbannerpic | 系统logoapp | TextField | fsysbannerpic |  |  |
| loginphrase | 登录语 | MuliLangTextField | — |  |  |
| pcloginphrase | 登录语 | MuliLangTextField | — |  |  |

