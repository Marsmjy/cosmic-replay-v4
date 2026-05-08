# hrptmc_share_filterscheme — 报表共享过滤方案

**表单编码**: `hrptmc_share_filterscheme`  
**表单ID**: `3OPAK4F6+==H`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_share_filterscheme（报表共享过滤方案） [BaseEntity]

- **数据库表**: `t_hrptmc_sharescheme`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| scheme | 方案名称 | BasedataField | fschemeid | ✓ | hrptmc_queryscheme |
| userfield | 共享用户 | MulBasedataField | t_hrprmc_schemeshareusers（子表） |  |  |
| rptmanage | 方案所属的报表 | BasedataField | frptmanageid |  | hrptmc_reportmanage |

