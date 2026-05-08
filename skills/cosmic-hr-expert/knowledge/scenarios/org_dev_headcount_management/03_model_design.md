# org_dev_headcount_management · 数据模型

> 涉及 N 个标品 form + M 个 ISV 自建 form

## 模型分层全景

<待手工精修：参 case_001 03_model_design.md>

## 实体清单

### ISV 自建
- ${ISV_FLAG}_planyear_bill

### 标品消费
['homs_orgchgrecord', 'haos_adminorgdetail', 'hbpm_positionhr', 'hrpi_empposorgrel', 'hbss_position_dict', 'hbss_jobclass_dict', 'hjm_jobhr', 'hbjm_jobhr_ext']

## 模型扩展点

详见 [`_assets/headcount_management/customization_points.md`](../../_assets/headcount_management/customization_points.md)
