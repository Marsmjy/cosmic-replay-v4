# hbp_devportal_bizapp — HR业务应用实体

**表单编码**: `hbp_devportal_bizapp`  
**表单ID**: `2E06T8J0JUZF`  
**归属**: HR基础服务云 / HR基础平台  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbp_devportal_bizapp（HR业务应用实体） [BaseEntity]

- **数据库表**: ``  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sequence | 序号 | IntegerField | — |  |  |
| number | 编码 | TextField | fnumber |  |  |
| visible | 可见性 | CheckBoxField | — |  |  |
| simplenumber | 简码 | TextField | — |  |  |
| creater | 创建人 | CreaterField | — |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| version | 版本 | TextField | — |  |  |
| helpurl | 帮助地址 | TextField | — |  |  |
| bizcloud | 业务云 | BasedataField | — |  | bos_devportal_bizcloud |
| type | 应用类型 | ComboField | — |  |  |
| deploystatus | 启用状态 | ComboField | — |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| createdate | 创建时间 | CreateDateField | — |  |  |
| modifydate | 修改时间 | ModifyDateField | — |  |  |
| mainformname | 首页设置 | TextField | — |  |  |
| mainformid | 首页设置id | TextField | — |  |  |
| backimage | 背景图片 | TextField | — |  |  |
| image | 主题图片 | TextField | — |  |  |
| svnpath | svn地址 | TextField | — |  |  |
| dependency | 依赖应用 | TextField | — |  |  |
| openttype | 打开方式 | ComboField | — |  |  |
| dependencyid | 依赖应用id | TextField | — |  |  |
| alluserapp | 全员应用 | CheckBoxField | — |  |  |
| masterid | 原厂应用id | TextField | fmasterid |  |  |
| parentid | 上级应用id | TextField | — |  |  |
| usertype | 适用用户 | MulComboField | — |  |  |
| label | 标签 | TextField | — |  |  |
| inheritpath | 继承路径 | TextField | — |  |  |
| metadata | 元数据内容 | TextField | — |  |  |
| orgfunc | 职能类型 | ComboField | — |  |  |
| isv | 开发商标识 | TextField | — |  |  |
| dbroute | 数据库标识 | ComboField | — |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| db | 分库 | ComboField | — |  |  |
| industry | 行业 | BasedataField | — |  | bos_devp_industry |
| refappid | 关联应用id | TextField | — |  |  |
| refapp | 关联应用 | BasedataField | — |  | bos_devportal_bizapp |
| mainformtype | 首页类型 | ComboField | — |  |  |
| homeurl | 链接地址 | TextField | — |  |  |

