# hrcs_roleinitdemo_role — 角色初始化角色清单样例

**表单编码**: `hrcs_roleinitdemo_role`  
**表单ID**: `3EY=222WUDW0`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_roleinitdemo_role（角色初始化角色清单样例） [BaseEntity]

- **数据库表**: `t_hrcs_roleinitdemo_role`  

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
| index | 排序号 | IntegerField | findex |  |  |
| rolenumber | 角色编码 | TextField | frolenumber |  |  |
| rolename | 角色名字 | MuliLangTextField | frolename |  |  |
| rolegroupnumber | 角色组编码 | TextField | frolegroupnumber |  |  |
| rolegroupname | 角色组名称 | MuliLangTextField | frolegroupname |  |  |
| roleproperty | 角色成员范围属性 | ComboField | froleproperty |  |  |
| rolecbisintersection | 自定义范围是否受限于角色范围内 | ComboField | frolecbisintersection |  |  |
| admingroupnumber | 所属管理员组编码 | TextField | fadmingroupnumber |  |  |
| usescope | 公开状态 | ComboField | fusescope |  |  |
| roleremark | 角色描述 | MuliLangTextField | froleremark |  |  |
| admingroupname | 所属管理员组名称 | MuliLangTextField | fadmingroupname |  |  |
| publicscopeview | 角色公开范围（查看） | MuliLangTextField | fpublicscopeview |  |  |
| publicscopeedit | 角色公开范围（编辑） | MuliLangTextField | fpublicscopeedit |  |  |

