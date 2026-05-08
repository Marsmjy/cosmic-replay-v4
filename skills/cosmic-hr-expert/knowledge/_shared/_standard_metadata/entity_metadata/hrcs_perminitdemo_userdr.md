# hrcs_perminitdemo_userdr — 权限初始化用户数据规则样例

**表单编码**: `hrcs_perminitdemo_userdr`  
**表单ID**: `35S6/L3EMPFM`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_perminitdemo_userdr（权限初始化用户数据规则样例） [BaseEntity]

- **数据库表**: `t_hrcs_pinitdemouserdr`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| sceneid | 场景ID | IntegerField | fsceneid |  |  |
| scenename | 场景名 | MuliLangTextField | fscenename |  |  |
| scenedesc | 场景描述 | MuliLangTextField | fscenedesc |  |  |
| usernumber | 用户工号 | TextField | fusernumber |  |  |
| usernumbertip | 用户工号tip | MuliLangTextField | fusernumbertip |  |  |
| username | 用户名字 | MuliLangTextField | fusername |  |  |
| usernametip | 用户名字tip | MuliLangTextField | fusernametip |  |  |
| fileorgnumber | 档案组织编码 | TextField | ffileorgnumber |  |  |
| fileorgnumbertip | 档案组织编码tip | MuliLangTextField | ffileorgnumbertip |  |  |
| fileorgname | 档案组织名字 | MuliLangTextField | ffileorgname |  |  |
| fileorgnametip | 档案组织名字tip | MuliLangTextField | ffileorgnametip |  |  |
| rolenumber | 角色编码 | TextField | frolenumber |  |  |
| rolenumbertip | 角色编码tip | MuliLangTextField | frolenumbertip |  |  |
| rolename | 角色名字 | MuliLangTextField | frolename |  |  |
| rolenametip | 角色名字tip | MuliLangTextField | frolenametip |  |  |
| appnumber | 应用编码 | TextField | fappnumber |  |  |
| appname | 应用名称 | MuliLangTextField | fappname |  |  |
| entitynumber | 业务对象编码 | TextField | fentitynumber |  |  |
| entityname | 业务对象名称 | MuliLangTextField | fentityname |  |  |
| permitem | 权限项 | MuliLangTextField | fpermitem |  |  |
| datarulenumber | 数据规则方案编码 | TextField | fdatarulenumber |  |  |
| datarulename | 数据规则方案名称 | MuliLangTextField | fdatarulename |  |  |
| appnumbertip | 应用编码tip | MuliLangTextField | fappnumbertip |  |  |
| appnametip | 应用名称tip | MuliLangTextField | fappnametip |  |  |
| entitynumbertip | 业务对象编码tip | MuliLangTextField | fentitynumbertip |  |  |
| entitynametip | 业务对象名称tip | MuliLangTextField | fentitynametip |  |  |
| permitemtip | 权限项tip | MuliLangTextField | fpermitemtip |  |  |
| datarulenumbertip | 数据规则方案编码tip | MuliLangTextField | fdatarulenumbertip |  |  |
| datarulenametip | 数据规则方案名称tip | MuliLangTextField | fdatarulenametip |  |  |
| index | 排序号 | IntegerField | findex |  |  |

