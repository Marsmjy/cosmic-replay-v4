# HRMP — HR基础服务云 元数据清单

> 共 **1266** 个表单/实体（含共享物理表成员），去重后 **1181** 行（折叠 85 条共享成员）

## 概览

| 分类 | 数量 |
|------|------|
| 基础资料 | 488 |
| 基础资料-非主数据类 | 228 |
| 单据 | 31 |
| 动态表单 | 387 |
| 查询模型 | 27 |
| 报表 | 1 |
| 移动端 | 14 |
| 其他 | 5 |

## 共享物理表汇总

| 物理表 | 共享元数据数 | 元数据列表 |
|--------|-------------|------------|
| t_brm_policy | 2 | brm_policy_edit, brm_rulelist |
| t_brm_scene | 2 | brm_scene, brm_scene_controlselect |
| t_haos_adminorg | 22 | haos_adminorg, haos_adminorgbatch, haos_adminorgdetail, haos_adminorgdetailf7, haos_adminorgdetailfuture, haos_adminorgdetailinit, haos_adminorgf7, haos_adminorghis, haos_adminorghishr, haos_adminorghrf7, haos_adminorgteam, haos_adminorgteamnoperm, haos_orgdetail, haos_othadminorg, haos_structorgdetail, haos_virtualorg_f7, haos_virtualorgdetail, hrptmc_adminorghr, hbp_adminorgcurnopermtest, hbp_adminorgcurpermtest, hbp_adminorghisnopermtest, hbp_adminorghistest |
| t_haos_adminstruct | 3 | haos_adminorgstruct, haos_othorgstruct, haos_adminstruct |
| t_haos_chargeperson | 2 | haos_chargeperson, haos_chargeperson_add |
| t_haos_company | 2 | haos_adminorgcompany, haos_orgcompany |
| t_haos_dutyorgdetail | 2 | haos_dutyorgdetail, haos_dutyorgdetailquery |
| t_haos_otclassify | 2 | haos_structtype, haos_otclassify |
| t_haos_staffdimension | 3 | haos_combdimension, haos_dynamicdimension, haos_staffdimension |
| t_haos_structproject | 2 | haos_structproject, haos_structure |
| t_haos_useorgdetail | 2 | haos_useorgdetail, haos_useorgdetailquery |
| t_hbpm_position | 9 | hbpm_orgpositionlist, hbpm_position_future, hbpm_positiondetailrevise, hbpm_positionhis, hbpm_positionhr, hbpm_positionhrf7, hbpm_positionlist, hbpm_stposition, hbpm_subposition |
| t_hbss_capacityitem | 2 | hbss_capacityitem, hbss_capacityitemdetail |
| t_hbss_lawentity | 2 | hbss_lawentity, hbss_lawentitychg |
| t_hbss_log | 2 | hbss_history_logview, hbss_logview |
| t_hbss_signcompany | 2 | hbss_signcompany, hbss_signcompanych |
| t_hrcs_activity | 2 | hrcs_activity, hrcs_activityview |
| t_hrcs_actscheme | 2 | hrcs_activityschem_layout, hrcs_activityscheme |
| t_hrcs_coordapplyconf | 8 | hrcs_coordappcfg, hrcs_coordappcfg51, hrcs_coordappcfg61, hrcs_coordappcfg81, hrcs_coordapplyconf11, hrcs_coordapplyconf12, hrcs_coordapplyconf13, hrcs_coordapplyconf14 |
| t_hrcs_coordbizfield | 2 | hrcs_coordbizfield, hrcs_coordbizfieldlist |
| t_hrcs_coordbizobject | 8 | hrcs_coordbizobject, hrcs_coordbizobject11, hrcs_coordbizobject12, hrcs_coordbizobject13, hrcs_coordbizobject14, hrcs_coordbizobject51, hrcs_coordbizobject61, hrcs_coordbizobject81 |
| t_hrcs_coordrulesch | 8 | hrcs_coordrulesch, hrcs_coordrulesch11, hrcs_coordrulesch12, hrcs_coordrulesch13, hrcs_coordrulesch14, hrcs_coordrulesch51, hrcs_coordrulesch61, hrcs_coordrulesch81 |
| t_hrcs_coordstrategy | 8 | hrcs_coordstrategy, hrcs_coordstrategy11, hrcs_coordstrategy12, hrcs_coordstrategy13, hrcs_coordstrategy14, hrcs_coordstrategy51, hrcs_coordstrategy61, hrcs_coordstrategy81 |
| t_hrcs_label | 2 | hrcs_label, hrcs_labelvaluedisplay |
| t_hrcs_labelobject | 2 | hrcs_labelobject, hrcs_lbldelineatdim |
| t_hrcs_lblobjectfield | 2 | hrcs_labeldimension, hrcs_lblobjectfield |
| t_hrcs_privacysigning | 2 | hbss_privacysigning, hrcs_privacysigning |
| t_hrcs_privacyusertype | 2 | hbss_privacyusertype, hrcs_privacyusertype |
| t_hrcs_tplvariableconfig | 2 | hrcs_addtplvariableconfig, hrcs_tplvariableconfig |
| t_hrcs_warnobjectfield | 3 | hrcs_selectqueryfieldf7, hrcs_warnobjectfieldf7, hrcs_warnscenequeryfield |
| t_hrcs_warnobjecttpl | 2 | hrcs_warnobjtpl, hrcs_warnobjtpl_listout |
| t_hrptmc_analysisobject | 2 | hrptmc_analyseobject, hrptmc_anobjtemplib |
| t_hrptmc_anobjqueryfield | 2 | hrptmc_anobjfield_f7, hrptmc_anobjqueryfield |
| t_hrptmc_mysubscribe | 2 | hrptmc_mysubscribe, hrptmc_mysubscribe_layout |
| t_hrptmc_rptqueryconfig | 2 | hrptmc_paramconfig, hrptmc_rptqueryconfig |
| t_hrptmc_sharerecord | 3 | hrptmc_sharerecord, hrptmc_sharerecord_layout, hrptmc_sharerecord_log |
| t_isv_xxx | 2 | hbss_historyeventrel, haos_personnellist |
| t_meta_bizapp | 2 | hbp_devportal_bizapp, bos_devportal_bizapp |
| t_meta_formdesign | 2 | hrcs_formmeta, bos_formmeta |
| t_meta_mainentityinfo | 4 | hbp_entityobject, hbp_funcprementity, hrcs_entityobject, bos_entityobject |

## 基础资料（488）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | haos_adminorg | 4S6B8I6U+EBN | t_haos_adminorg | haos_adminorg, haos_adminorgbatch, haos_adminorgdetail, haos_adminorgdetailf7, haos_adminorgdetailfuture, haos_adminorgdetailinit, haos_adminorgf7, haos_adminorghis, haos_adminorghishr, haos_adminorghrf7, haos_adminorgteam, haos_adminorgteamnoperm, haos_orgdetail, haos_othadminorg, haos_structorgdetail, haos_virtualorg_f7, haos_virtualorgdetail, hrptmc_adminorghr, hbp_adminorgcurnopermtest, hbp_adminorgcurpermtest, hbp_adminorghisnopermtest, hbp_adminorghistest | 组织基本信息（主） | 基础资料 | haos |
| 2 | haos_adminorg_msgdetail | 5+27+=YVXO2Z | t_haos_adminorg_msgdetail |  | 组织消息明细 | 基础资料 | haos |
| 3 | haos_adminorgcompany | 22003ZY2I4WF | t_haos_company | haos_adminorgcompany, haos_orgcompany | 公司信息 | 基础资料 | haos |
| 4 | haos_adminorgfunction | 210PRLH7NPN+ | t_haos_adminorgfunction |  | 行政组织职能 | 基础资料 | haos |
| 5 | haos_adminorglayer | 2=45UCX7D255 | t_haos_adminorglayer |  | 管理层级 | 基础资料 | haos |
| 6 | haos_adminorgtype | XYVDJWTOPUK | t_haos_adminorgtype |  | 行政组织类型 | 基础资料 | haos |
| 7 | haos_adminorgtypestd | YJYNB0E72OI | t_haos_adminorgtypestd |  | 行政组织类型归属 | 基础资料 | haos |
| 8 | haos_basedatalist | 23FRJSWEOUTZ | t_haos_basedatalist |  | 组织基础资料 | 基础资料 | haos |
| 9 | haos_changeoperat | 2=G8LMKVXSZT | t_haos_changeoperat |  | 变动操作 | 基础资料 | haos |
| 10 | haos_changescene | 2=H+4F0C+GJJ | t_haos_changescene |  | 行政组织变动场景 | 基础资料 | haos |
| 11 | haos_changescenesub | 2=GU0ZZANORH | t_haos_changescenesub |  | 场景子类 | 基础资料 | haos |
| 12 | haos_changesource | 4V9IZC778CR8 | t_haos_changesource |  | 变动来源 | 基础资料 | haos |
| 13 | haos_chargeperson | 4V9J2/6AQJJH | t_haos_chargeperson | haos_chargeperson, haos_chargeperson_add | 部门负责人 | 基础资料 | haos |
| 14 | haos_combdimension | 4K+=BGU6W87/ | t_haos_staffdimension | haos_combdimension, haos_dynamicdimension, haos_staffdimension | 组合维度 | 基础资料 | haos |
| 15 | haos_commbasedatalist | 23G+WUW2B4Z7 | t_haos_commbasedatalist |  | 公共基础资料 | 基础资料 | haos |
| 16 | haos_companytype | XYUZHPWJW=F | t_haos_companytype |  | 公司类型 | 基础资料 | haos |
| 17 | haos_departmenttype | XYV164AO1SY | t_haos_departmenttype |  | 部门类型 | 基础资料 | haos |
| 18 | haos_dimstaffreport | 426L02UOB7SN | t_haos_dimstaffreport |  | 编制报表-维度编制 | 基础资料 | haos |
| 19 | haos_dutyorgdetail | 2IXTMAKH6JGS | t_haos_dutyorgdetail | haos_dutyorgdetail, haos_dutyorgdetailquery | 责任组织明细 | 基础资料 | haos |
| 20 | haos_dutyorgdetailhis | 2IXTNM0A45MM | t_haos_dutyorgdetailhis |  | 责任组织明细历史 | 基础资料 | haos |
| 21 | haos_futuretips_record | 536C1LWVY7RE | t_haos_futuretips_record |  | 未来生效指引记录 | 基础资料 | haos |
| 22 | haos_hismainpeoplelist | 2A34Z=RCG2+B |  |  | 历任管理团队列表 | 基础资料 | haos |
| 23 | haos_muldimendetail | 2IXS84C67LVF | t_haos_muldimendetail |  | 多维控编明细 | 基础资料 | haos |
| 24 | haos_muldimendetailhis | 2IXSA/RHF/PC | t_haos_muldimendetailhis |  | 多维控编明细历史 | 基础资料 | haos |
| 25 | haos_muldimusestaff | 2OPU5VJ4KF42 | t_haos_muldimusestaff |  | 多维占编明细 | 基础资料 | haos |
| 26 | haos_orgchangereason | 21CVNFII7OS1 | t_haos_orgchangereason |  | 行政组织变动原因 | 基础资料 | haos |
| 27 | haos_orgchangetype | 21CXZD/M3A3K | t_haos_orgchangetype |  | 变动类型 | 基础资料 | haos |
| 28 | haos_orgfullname | 5AJ8D6ROVMGQ | t_haos_orgfullname |  | 组织全称 | 基础资料 | haos |
| 29 | haos_orgfullnameconfig | 5AJ=1=P7I59K | t_haos_orgfullnameconfig |  | 行政组织全称规则 | 基础资料 | haos |
| 30 | haos_orgoperatesteps | 3KLRKF/=TPMS | t_haos_orgoperatesteps |  | 行政组织变更操作步骤 | 基础资料 | haos |
| 31 | haos_orgpersonstaffinfo | 2NQDPZ59+IK1 | t_haos_staffdimperson |  | 占编员工维度信息 | 基础资料 | haos |
| 32 | haos_orgstaffreport | 426KZUQHHDNK | t_haos_orgstaffreport |  | 编制报表-组织编制 | 基础资料 | haos |
| 33 | haos_orgteamcooprel | 22=IBO5Z1P5/ | t_haos_teamcooprel |  | 组织协作关系 | 基础资料 | haos |
| 34 | haos_orgusestaffdetail | 2NTWDCYO5Q=L | t_haos_orgusestaff |  | 组织占编明细 | 基础资料 | haos |
| 35 | haos_othemproleorgrel | 5COAPP7SHYGF | t_haos_othemproleorgrel |  | 其他形态组织-人员角色关系 | 基础资料 | haos |
| 36 | haos_othorgstruct | 5COC9SL+BXKM | t_haos_adminstruct | haos_adminorgstruct, haos_othorgstruct, haos_adminstruct | 其他形态-组织结构 | 基础资料 | haos |
| 37 | haos_othrole | 5CNUFA5O=0RN | t_haos_othrole |  | 其他形态组织-角色 | 基础资料 | haos |
| 38 | haos_othroletpl | 5CO=QHBW/YH9 | t_haos_othroletpl |  | 其他形态组织-角色库 | 基础资料 | haos |
| 39 | haos_personchangeevent | 2NQSOD1QSC34 | t_haos_perchangeevent |  | 员工变动活动 | 基础资料 | haos |
| 40 | haos_personstaffinfo | 2NQCH2IUH6JR | t_haos_staffperson |  | 占编员工信息 | 基础资料 | haos |
| 41 | haos_remainstafflist | 2ZXK3QBM208= | t_haos_emptyremainstaff |  | 组织编制使用情况 | 基础资料 | haos |
| 42 | haos_staff | 2IV213+ATZBR | t_haos_staff |  | 编制信息维护 | 基础资料 | haos |
| 43 | haos_staffactivity | 2OF7OH055Y3W | t_haos_staffactivity |  | 编制业务活动 | 基础资料 | haos |
| 44 | haos_staffactivitytype | 2OF/VTKI2ST9 | t_haos_staffactivitytype |  | 编制活动类型 | 基础资料 | haos |
| 45 | haos_staffcase | 34DUMP900WRM | t_haos_staffcase |  | 不占编员工明细 | 基础资料 | haos |
| 46 | haos_staffcycle | 2IY/RFLW/X5+ | t_haos_staffcycle |  | 编制周期 | 基础资料 | haos |
| 47 | haos_staffflex | 4GZ5TZRIX4Q6 | t_haos_staffflex |  | 编制弹性域横表 | 基础资料 | haos |
| 48 | haos_staffflex_bd | 4H0KPEZ28H9W | t_haos_staffflex_bd |  | 编制弹性域纵表 | 基础资料 | haos |
| 49 | haos_stafforgempcount | 2WQ2VOSWK3HO | t_haos_stafforgempcount |  | 组织人数信息 | 基础资料 | haos |
| 50 | haos_staffruleconfig | 2OM+E+Y=NQ7F | t_haos_staffruleconfig |  | 编制计划设置 | 基础资料 | haos |
| 51 | haos_structproconfig | 3BWQ64IF0255 | t_haos_structproconfig |  | 架构方案配置 | 基础资料 | haos |
| 52 | haos_structtype | 5CY=ZEYR9=CG | t_haos_otclassify | haos_structtype, haos_otclassify | 架构类型 | 基础资料 | haos |
| 53 | haos_structure | 3C5IN45RNAP4 | t_haos_structproject | haos_structproject, haos_structure | 矩阵组织维护 | 基础资料 | haos |
| 54 | haos_teamcoopreltype | 0S=+8RXR/K5V | t_haos_teamcoopreltype |  | 团队协作类型 | 基础资料 | haos |
| 55 | haos_useorgdetail | 2IXRXZ37HO/M | t_haos_useorgdetail | haos_useorgdetail, haos_useorgdetailquery | 使用组织明细 | 基础资料 | haos |
| 56 | haos_useorgdetailhis | 2IXS/XDCZA1B | t_haos_useorgdetailhis |  | 使用组织明细历史 | 基础资料 | haos |
| 57 | hbjm_job_msgdetail | 52N4QVV0QFK2 | t_hbjm_job_msgdetail |  | 职位消息明细 | 基础资料 | hbjm |
| 58 | hbjm_jobclasshr | /WJC1+BU/SJ3 | t_hbjm_jobclass |  | 职位类 | 基础资料 | hbjm |
| 59 | hbjm_jobfamilyhr | /WJJLJBB+6XH | t_hbjm_jobfamily |  | 职位族 | 基础资料 | hbjm |
| 60 | hbjm_jobgradehr | /IK4+TMI2QAJ | t_hbjm_jobgrade |  | 职等 | 基础资料 | hbjm |
| 61 | hbjm_jobgradescmhr | /IMVJYNA0YOV | t_hbjm_jobgradescm |  | 职等方案 | 基础资料 | hbjm |
| 62 | hbjm_jobhr | /IJRHGRN5RVY | t_hbjm_job |  | 职位 | 基础资料 | hbjm |
| 63 | hbjm_joblevelhr | /IK45U1VF2XK | t_hbjm_joblevel |  | 职级 | 基础资料 | hbjm |
| 64 | hbjm_joblevelscmhr | /IMW75UI5GC2 | t_hbjm_joblevelscm |  | 职级方案 | 基础资料 | hbjm |
| 65 | hbjm_jobscmhr | /WJKGEZB2ZIV | t_hbjm_jobscm |  | 职位体系方案 | 基础资料 | hbjm |
| 66 | hbjm_jobseqhr | /WJJWPT23FOS | t_hbjm_jobseq |  | 职位序列 | 基础资料 | hbjm |
| 67 | hbjm_jobtype | 15VMKG3/AYQN | t_hbjm_jobtype |  | 职位类别 | 基础资料 | hbjm |
| 68 | hbjm_standardjobseqhr | /WJK=UE0RIMU | t_hbjm_standardjobseq |  | HR标准职位序列 | 基础资料 | hbjm |
| 69 | hbp_calresultitem | 2A=EOZUCB126 | t_hbp_calresultitem |  | 计算公式结果参数 | 基础资料 | hbp |
| 70 | hbp_datagrade_unittest | 2A=0O01+2876 | t_hbp_datagrade_unittest |  | 数据分级单元测试元数据 | 基础资料 | hbp |
| 71 | hbp_devportal_bizapp | 2E06T8J0JUZF | t_meta_bizapp | hbp_devportal_bizapp, bos_devportal_bizapp | HR业务应用实体 | 基础资料 | hbp |
| 72 | hbp_empcoordruleparam | 4XRTDLDIFDVK |  |  | 员工变动协作规则参数 | 基础资料 | hbp |
| 73 | hbp_entityobject | 29E=9E3S658E | t_meta_mainentityinfo | hbp_entityobject, hbp_funcprementity, hrcs_entityobject, bos_entityobject | HR主实体对象 | 基础资料 | hbp |
| 74 | hbp_formula_unittest | 2A9HVRVHOMYS | t_hbp_formula_unittest |  | 计算公式单元测试 | 基础资料 | hbp |
| 75 | hbp_formulaeg | 5GLA0T2W9UH+ | t_hbp_formulaeg |  | 公式示例配置 | 基础资料 | hbp |
| 76 | hbp_rightrelatepaneltpl | 12DQMT3D05/H | t_haos_contactway |  | HR右侧标签栏模板 | 基础资料 | hbp |
| 77 | hbp_unittesthisbugrp01 | 50R9/G78X78V | t_hbp_unittesthisbugrp01 |  | 带BU带分组时序单测 | 基础资料 | hrcs |
| 78 | hbp_unittesthisgrp01 | 5BE7=1M8YOZ7 | t_hbp_unittesthisgrp01 |  | 分组全页面时序单测 | 基础资料 | hbp |
| 79 | hbp_unittesthisoritpl01 | 50R7LRFA8/3K | t_hbp_unittesthisoritpl01 |  | 原生时序历史单测 | 基础资料 | hbp |
| 80 | hbp_unittesthistpl01 | 50R7T9BPJA93 | t_hbp_unittesthistpl01 |  | 全页面时序历史单测 | 基础资料 | hbp |
| 81 | hbp_unittesthistpl02 | 50R8UZ8T0Z79 | t_hbp_unittesthistpl02 |  | 全页面非时序单测 | 基础资料 | hbp |
| 82 | hbp_unittesthistpl03 | 50S=BBWQ=RB0 | t_hbp_unittesthistpl03 |  | 全页面时序单测（需审核） | 基础资料 | hrptmc |
| 83 | hbp_unittesttime01 | 50UQZ5=7GTUS | t_hbp_unittesttime01 |  | 时间轴单测（不间断不重叠） | 基础资料 | hbp |
| 84 | hbp_unittesttime02 | 50URVQ7CB=LM | t_hbp_unittesttime02 |  | 时间轴单测（可间断不重叠） | 基础资料 | hbp |
| 85 | hbp_unittesttime03 | 50US/G1JCBSF | t_hbp_unittesttime03 |  | 时间轴单测（可间断可重叠） | 基础资料 | hbp |
| 86 | hbp_unittesttpl01 | 5BE5U8QHU2R4 | t_hbp_unittesttpl01 |  | 中台基础资料全页面单测 | 基础资料 | hbp |
| 87 | hbpm_basedatalist | 17/+0P1LBVA3 | t_hbpm_basedatalist |  | 岗位基础资料 | 基础资料 | hbpm |
| 88 | hbpm_changeoperate | 2A2BWBIS9GIY | t_hbpm_changeoperate |  | 变动操作 | 基础资料 | hbpm |
| 89 | hbpm_changereason | 198B9CZ=DP86 | t_hbpm_changereason |  | 变动原因 | 基础资料 | hbpm |
| 90 | hbpm_changescene | 2A2C5+JBV+27 | t_hbpm_changescene |  | 变动场景 | 基础资料 | hbpm |
| 91 | hbpm_changetype | 198B9QL3=1KA | t_hbpm_changetype |  | 变动类型 | 基础资料 | hbpm |
| 92 | hbpm_chgrecord | 2W+KC/N25T6Q | t_hbpm_chgrecord |  | 岗位变动明细 | 基础资料 | hbpm |
| 93 | hbpm_chgrecorddetail | 2W/6SLLQV+JH | t_hbpm_chgrecorddetail |  | 岗位变动明细详情分录 | 基础资料 | hbpm |
| 94 | hbpm_chgrecordevt | 2W/54SML==+1 | t_hbpm_chgrecordevt |  | 岗位变动明细事务分录 | 基础资料 | hbpm |
| 95 | hbpm_futuretips_record | 57IWK0PET09M | t_hbpm_futuretips_record |  | 岗位待生效指引记录 | 基础资料 | hbpm |
| 96 | hbpm_position_msgdetail | 5+25ENEXGHNU | t_hbpm_position_msgdetail |  | 岗位消息明细 | 基础资料 | hbpm |
| 97 | hbpm_positionhr | /IJP/IQGX57W | t_hbpm_position | hbpm_orgpositionlist, hbpm_position_future, hbpm_positiondetailrevise, hbpm_positionhis, hbpm_positionhr, hbpm_positionhrf7, hbpm_positionlist, hbpm_stposition, hbpm_subposition | 岗位信息维护 | 基础资料 | hbpm |
| 98 | hbpm_positionrelation | 4ST4OBG0Z3J4 | t_hbpm_positionrelation |  | 岗位汇报关系 | 基础资料 | hbpm |
| 99 | hbpm_positiontpl | 3FWXN363Y23+ | t_hbpm_positiontpl |  | 岗位模板 | 基础资料 | hbpm |
| 100 | hbpm_positiontplfield | 3NJMIJ0H/H/G | t_hbpm_positiontplfield |  | 岗位模板字段范围 | 基础资料 | hbpm |
| 101 | hbpm_positiontpltype | 3FXJE42OC5PE | t_hbpm_positiontpltype |  | 岗位模板类型 | 基础资料 | hbpm |
| 102 | hbpm_positiontype | 15V=/VBMMOSG | t_hbpm_positiontype |  | 岗位类型 | 基础资料 | hbpm |
| 103 | hbpm_reportcoreltype | 0+4/VI99B7N9 | t_hbpm_reportreltype |  | 协作类型 | 基础资料 | hbpm |
| 104 | hbpm_revisefieldconfig | 35NGQ9M0G0U6 | t_hbpm_revisefieldconfig |  | 岗位修订时间轴字段配置 | 基础资料 | hbpm |
| 105 | hbss_action | 0M7P993RUJ33 | t_hbss_action |  | 业务操作 | 基础资料 | hbss |
| 106 | hbss_addressdetail | 2+MG/15WKPN/ | t_hbss_addressdetail |  | 详细地址 | 基础资料 | hbss |
| 107 | hbss_appentryconfig | 2CRS8ROUMR=/ | t_hbss_appentryconfig |  | 应用入口配置 | 基础资料 | hbss |
| 108 | hbss_bankdeposit | /SPH4JAJPWW3 | t_hbss_bankdeposit |  | 开户行 | 基础资料 | hbss |
| 109 | hbss_basedatalist | /USU/S9OX2Q3 | t_hbss_basedatalist |  | HR基础资料 | 基础资料 | hbss |
| 110 | hbss_capacityaction | 2FPB3ZBG/H1F | t_hbss_capacityaction |  | 能力行为 | 基础资料 | hbss |
| 111 | hbss_capacitygroup | 2FP62N57PF4J | t_hbss_capacitygroup |  | 能力素质维度 | 基础资料 | hbss |
| 112 | hbss_capacityitem | 2GHHBD8K5BMH | t_hbss_capacityitem | hbss_capacityitem, hbss_capacityitemdetail | 能力素质项 | 基础资料 | hbss |
| 113 | hbss_certmember | 2DO1J20AV9XJ | t_hbss_certmember |  | 许可分组员工 | 基础资料 | hbss |
| 114 | hbss_certusedwarning | 2CI8YTIDPWS0 |  |  | 许可预警 | 基础资料 | hbss |
| 115 | hbss_cloud | 2/BQZ+XCJYVV | t_hbss_cloud |  | HR领域云 | 基础资料 | hbss |
| 116 | hbss_cloud_app | 2GZN386W4NN9 | t_hbss_cloudapp |  | HR云与应用 | 基础资料 | hbss |
| 117 | hbss_college | 08B2NE63B//H | t_hbss_college |  | 高等院校 | 基础资料 | hbss |
| 118 | hbss_companyscale | 1PI1N=L3/L+9 | t_hbss_companyscale |  | 公司规模 | 基础资料 | hbss |
| 119 | hbss_costcenter | 05KKQFLGUNB6 | t_hbss_costcenter |  | HR成本承担单位 | 基础资料 | hbss |
| 120 | hbss_empgroup | 1A4AI3M+1I01 | t_hbss_empgroup |  | 业务档案分组 | 基础资料 | hbss |
| 121 | hbss_employeegroup | 4TAS7/0J7U99 | t_hbss_employeegroup |  | 员工组 | 基础资料 | hbss |
| 122 | hbss_empnature | /=A1K36XST95 | t_hbss_empnature |  | 企业性质 | 基础资料 | hbss |
| 123 | hbss_encouragewords | 2CRSAEQFVQAI | t_hbss_encouragewords |  | 激励文案 | 基础资料 | hbss |
| 124 | hbss_enterprise | 1A4CDVNZ9IBB | t_hbss_enterprise |  | 用人单位 | 基础资料 | hbss |
| 125 | hbss_hrbu_hitfrequency | 4VD4=ZFDO69H | t_hbss_hrbu_hitfrequency |  | HR管理组织命中次数 | 基础资料 | hbss |
| 126 | hbss_hrbuca | /U0PJ52O6CCZ | t_hbss_hrbuca |  | HR业务管理视图 | 基础资料 | hbss |
| 127 | hbss_hrbuext | 0FTQ865QIOUF |  |  | HR管理组织 | 基础资料 | hbss |
| 128 | hbss_hrbusinessfield | 0S6CR75BL1CB | t_hbss_hrbusinessfield |  | 业务领域 | 基础资料 | hbss |
| 129 | hbss_hrbuviewext | 0HL33R7HMEOD |  |  | HR业务管理视图 | 基础资料 | hbss |
| 130 | hbss_laborrelsub | 1IK/=G9H49LC | t_hbss_laborrelsub |  | 人员分类 | 基础资料 | hbss |
| 131 | hbss_lawentity | 2BHUREU0=RYL | t_hbss_lawentity | hbss_lawentity, hbss_lawentitychg | 法律实体 | 基础资料 | hbss |
| 132 | hbss_lawentityuse | 2BVS1AGCBI4V | t_hbss_lawentityuse |  | 法律实体使用情况 | 基础资料 | hbss |
| 133 | hbss_lawentityvrinf | 2BZWWI09CV/X | t_hbss_lawentityvrinf |  | 法律实体版本详情 | 基础资料 | hbss |
| 134 | hbss_loginconfig | 2=45KD7VWJRL | t_hbss_loginconfig |  | 登录页配置 | 基础资料 | hbss |
| 135 | hbss_passessnode | 28P9YHL7D5EI | t_hbss_passessnode |  | 绩效业务活动 | 基础资料 | hbss |
| 136 | hbss_privacysigning | 5LRKR/PZXEIU | t_hrcs_privacysigning | hbss_privacysigning, hrcs_privacysigning | 隐私声明签署记录 | 基础资料 | hbss |
| 137 | hbss_procreatstatus | 1A4DMEQQEDM/ | t_hbss_procreatstatus |  | 生育状况 | 基础资料 | hbss |
| 138 | hbss_protitle | 1A4E05ZW/KVB | t_hbss_protitle |  | 职称 | 基础资料 | hbss |
| 139 | hbss_relatepanelset | 12IO=V7Q=SBQ | t_hbss_relatepanelset |  | 侧边栏配置 | 基础资料 | hbss |
| 140 | hbss_safeuri | 2=42FO47+WYL | t_hbss_safeuri |  | 链接明细信息 | 基础资料 | hbss |
| 141 | hbss_safeuriconfig | 2=42/8/AO3EP | t_hbss_safeuriconfig |  | 链接有效期配置 | 基础资料 | hbss |
| 142 | hbss_scoreinterval | 4NUB1KW74BGT | t_hbss_scoreinterval |  | 评分间隔 | 基础资料 | hbss |
| 143 | hbss_scoresystem | 2PWB17A4CCKA | t_hbss_scoresystem |  | 评分分制 | 基础资料 | hbss |
| 144 | hbss_signcompany | 26UOO/JKDR7R | t_hbss_signcompany | hbss_signcompany, hbss_signcompanych | 聘用单位 | 基础资料 | hbss |
| 145 | hbss_signcompanyhis | 26XRJ39UX6KT | t_hbss_signcompanyhis |  | 聘用单位历史 | 基础资料 | hbss |
| 146 | hbss_supplier | 0=6TE0O3GFAU | t_hbss_supplier |  | 供应商 | 基础资料 | hbss |
| 147 | hbss_taxunit | 0/X4XC2YHY5E | t_hbss_taxunit |  | 纳税单位 | 基础资料 | hbss |
| 148 | hbss_timestamp | 2XSXC8OYEPCF | t_hbss_timestamp |  | 期间标识 | 基础资料 | hbss |
| 149 | hbss_workplace | 1O963N1DELG3 | t_hbss_workplace |  | 工作地 | 基础资料 | hbss |
| 150 | hcf_canaddress | 15YWLDUCFNH= | t_hcf_canaddress |  | 拟入职人员地址 | 基础资料 | hcf |
| 151 | hcf_canbankcard | 15YYAXNJ1U7= | t_hcf_canbankcard |  | 拟入职人员银行卡 | 基础资料 | hcf |
| 152 | hcf_cancontact | 16/NGLLKABY8 | t_hcf_cancontact |  | 拟入职人员紧急联系人 | 基础资料 | hcf |
| 153 | hcf_cancontactinfo | 15Y4W1S1FGGU | t_hcf_cancontactinfo |  | 拟入职人员联系方式 | 基础资料 | hcf |
| 154 | hcf_cancre | 15Y9/5AHTCJ+ | t_hcf_cancre |  | 拟入职人员证件信息 | 基础资料 | hcf |
| 155 | hcf_candidate | 15W/UEVF+QP3 | t_hcf_candidate |  | 拟入职人员 | 基础资料 | hcf |
| 156 | hcf_caneduexp | 1606B53J7208 | t_hcf_caneduexp |  | 拟入职人员教育经历 | 基础资料 | hcf |
| 157 | hcf_canfamily | 15YTZ9Y2O21C | t_hcf_canfamily |  | 拟入职人员家庭成员 | 基础资料 | hcf |
| 158 | hcf_canlgability | 15YT7RJ4QO2F | t_hcf_canlgability |  | 拟入职人员语言技能 | 基础资料 | hcf |
| 159 | hcf_canocpqual | 15Z+2SF1B5UB | t_hcf_canocpqual |  | 拟入职人员职业资格 | 基础资料 | hcf |
| 160 | hcf_canprework | 15YYO1G0ZC4V | t_hcf_canprework |  | 拟入职人员工作经历 | 基础资料 | hcf |
| 161 | hcf_canprojectexp | 15YYKVZT/EGV | t_hcf_canprojectexp |  | 拟入职人员项目经历 | 基础资料 | hcf |
| 162 | hcf_cantraining | 15YYS8U9CYG2 | t_hcf_cantraining |  | 拟入职人员培训经历 | 基础资料 | hcf |
| 163 | hcf_personalarea | 1OP/BXZXSCZ3 | t_hcf_personalarea |  | 拟入职人员区域信息 | 基础资料 | hcf |
| 164 | hcf_rsmhobby | 1OP2H/PFIXU5 | t_hcf_rsmhobby |  | 拟入职人员特长及爱好 | 基础资料 | hcf |
| 165 | hcf_rsmpatinv | 1OP46JRPKKGA | t_hcf_rsmpatinv |  | 拟入职人员专利发明 | 基础资料 | hcf |
| 166 | hcf_rsmproskl | 1OPJT6M04N9= | t_hcf_rsmproskl |  | 拟入职人员专业技能 | 基础资料 | hcf |
| 167 | hrcs_activity | 2+QV1L7JE9WB | t_hrcs_activity | hrcs_activity, hrcs_activityview | 活动 | 基础资料 | hrcs |
| 168 | hrcs_activityclientconf | 21D2BCJ9/Q9Z | t_hrcs_activityclient |  | 活动编排-业务方MQ配置 | 基础资料 | hrcs |
| 169 | hrcs_activitygroupconfig | 3HADCR8C9IW9 | t_hrcs_actgroupconfig |  | 活动组配置 | 基础资料 | hrcs |
| 170 | hrcs_activitygroupins | 38F6U0=Y2N1+ | t_hrcs_activitygroupins |  | 活动组实例 | 基础资料 | hrcs |
| 171 | hrcs_activityins | 2+U7C=LT8+LM | t_hrcs_activityins |  | 活动任务实例 | 基础资料 | hrcs |
| 172 | hrcs_activitynodeconf | 4CHJ1ML+YC4W | t_hrcs_activitynodeconf |  | HR节点处理设置 | 基础资料 | hrcs |
| 173 | hrcs_activityscheme | 2/WZZ=1+H+FW | t_hrcs_actscheme | hrcs_activityschem_layout, hrcs_activityscheme | 活动方案 | 基础资料 | hrcs |
| 174 | hrcs_activitytype | 2+QWJSTURCFQ | t_hrcs_activitytype |  | 活动类型 | 基础资料 | hrcs |
| 175 | hrcs_admingroupfile | 2AMCMSA9ATSE | t_hrcs_admingroupfile |  | 权限档案范围 | 基础资料 | hrcs |
| 176 | hrcs_admingroupfunc | 2AMES8R3+SOE | t_hrcs_admingroupfunc |  | 职能组织范围 | 基础资料 | hrcs |
| 177 | hrcs_admingrouporg | 2AMD71FS09++ | t_hrcs_admingrouporg |  | 行政组织范围 | 基础资料 | hrcs |
| 178 | hrcs_bgtaskrecord | 2LYVSO6ED3Y0 | t_hrcs_bgtaskrecord |  | 悬浮球任务记录 | 基础资料 | hrcs |
| 179 | hrcs_bgtaskregister | 2LYUB=K/+C3J | t_hrcs_bgtaskregister |  | 悬浮球任务注册 | 基础资料 | hrcs |
| 180 | hrcs_bgtasksession | 2MALQ0TR65TR | t_hrcs_bgtasksession |  | 悬浮球任务执行会话 | 基础资料 | hrcs |
| 181 | hrcs_businessobject | 15I=B==/QM2V | t_hbss_bussinessobject |  | 业务对象 | 基础资料 | hrcs |
| 182 | hrcs_bussinesstype | 15I7OKI=KOIZ | t_hbss_bussinesstype |  | 业务类型关系 | 基础资料 | hrcs |
| 183 | hrcs_checkuserrolesyn | 46RM2FZ23U+1 | t_hrcs_checkuserrolesyn |  | 检查用户角色同步结果 | 基础资料 | hrcs |
| 184 | hrcs_chgeventblacklist | 5=CEVXJT9I/X | t_hrcs_chgeventblacklist |  | 变动大类黑名单 | 基础资料 | hrcs |
| 185 | hrcs_choose_permitem | 2+XHAXWD2DKG |  |  | 选择需要的权限项 | 基础资料 | hrcs |
| 186 | hrcs_choosefield_page | 2/F539N1JNEP |  |  | 选择需要的字段 | 基础资料 | hrcs |
| 187 | hrcs_commonvariable | 3MUMB2C=ZL6C | t_hrcs_commonvariable |  | 常用变量 | 基础资料 | hrcs |
| 188 | hrcs_conditionview | 4=D6=YYPCIZ0 |  |  | 动态方案规则预览 | 基础资料 | hrcs |
| 189 | hrcs_contractest | 2=O45MDY2GD5 | t_hrcs_contract |  | 电子合同_联调测试 | 基础资料 | hrcs |
| 190 | hrcs_controlchoose | 1LKENRX4N+P/ |  |  | 提示语可选控件 | 基础资料 | hrcs |
| 191 | hrcs_coordappcfg | 4ZJ71/4XEXR= | t_hrcs_coordapplyconf | hrcs_coordappcfg, hrcs_coordappcfg51, hrcs_coordappcfg61, hrcs_coordappcfg81, hrcs_coordapplyconf11, hrcs_coordapplyconf12, hrcs_coordapplyconf13, hrcs_coordapplyconf14 | 协作应用配置 | 基础资料 | hrcs |
| 192 | hrcs_coordbizfield | 4YF3E/MNA9NU | t_hrcs_coordbizfield | hrcs_coordbizfield, hrcs_coordbizfieldlist | 业务协作字段 | 基础资料 | hrcs |
| 193 | hrcs_coordbizfieldgrp | 4YF1951P812Z | t_hrcs_coordbizfieldgrp |  | 业务协作字段分组 | 基础资料 | hrcs |
| 194 | hrcs_coordbizobject | 4YEVMAR6Q7A1 | t_hrcs_coordbizobject | hrcs_coordbizobject, hrcs_coordbizobject11, hrcs_coordbizobject12, hrcs_coordbizobject13, hrcs_coordbizobject14, hrcs_coordbizobject51, hrcs_coordbizobject61, hrcs_coordbizobject81 | 协作业务对象 | 基础资料 | hrcs |
| 195 | hrcs_coordfieldrule | 4ZQCPPFJQOHD | t_hrcs_coordfieldrule |  | 协作规则方案字段规则映射 | 基础资料 | hrcs |
| 196 | hrcs_coordprichgrecord | 4YBFZLWFZ/C+ | t_hrcs_coordprichgrecord |  | 协作应用优先处理变动记录 | 基础资料 | hrcs |
| 197 | hrcs_coordrulesch | 4YYODD+Y7ZHQ | t_hrcs_coordrulesch | hrcs_coordrulesch, hrcs_coordrulesch11, hrcs_coordrulesch12, hrcs_coordrulesch13, hrcs_coordrulesch14, hrcs_coordrulesch51, hrcs_coordrulesch61, hrcs_coordrulesch81 | 协作规则方案 | 基础资料 | hrcs |
| 198 | hrcs_coordsceneconf | 4YDOY0ZC4U+A | t_hrcs_coordsceneconf |  | 协作场景应用配置 | 基础资料 | hrcs |
| 199 | hrcs_coordstrategy | 4Z0LH=7A/6IH | t_hrcs_coordstrategy | hrcs_coordstrategy, hrcs_coordstrategy11, hrcs_coordstrategy12, hrcs_coordstrategy13, hrcs_coordstrategy14, hrcs_coordstrategy51, hrcs_coordstrategy61, hrcs_coordstrategy81 | 协作处理策略 | 基础资料 | hrcs |
| 200 | hrcs_datarule | 167HXZTD4CP/ | t_hrcs_datarule |  | 数据规则 | 基础资料 | hrcs |
| 201 | hrcs_dimension | 1=7V+OW3JBX3 | t_hrcs_dimension |  | 维度 | 基础资料 | hrcs |
| 202 | hrcs_dynaauthobject | 4Z9SZG+IOQRJ | t_hrcs_dynaauthobject |  | 动态授权对象 | 基础资料 | hrcs |
| 203 | hrcs_dynacond | 3WCX+Q3WNN4+ | t_hrcs_dynacond |  | 动态数据范围配置 | 基础资料 | hrcs |
| 204 | hrcs_dynaformctrl | 2XASLI2R8GSQ | t_hrcs_dynaformctrl |  | 虚字段数据控权配置 | 基础资料 | hrcs |
| 205 | hrcs_dynamsgdealtrace | 46/VAGJZ5D28 | t_hrcs_dynamsgdealtrace |  | 动态权限消息处理跟踪 | 基础资料 | hrcs |
| 206 | hrcs_dynaruleitem | 4ZH=9/UKW/1/ | t_hrcs_dynaruleitem |  | 规则参数项 | 基础资料 | hrcs |
| 207 | hrcs_dynaruleoperator | 3UH1E=NOHDY8 | t_hrcs_dynaruleoperator |  | 动态权限方案规则比较运算 | 基础资料 | hrcs |
| 208 | hrcs_dynaschdatarule | 5+5ON49SFPM0 | t_hrcs_dynaschdatarule |  | 动态权限方案数据规则 | 基础资料 | hrcs |
| 209 | hrcs_dynaschdimgrp | 5+5R7E6AHL=1 | t_hrcs_dynaschdimgrp |  | 动态权限方案维度值组 | 基础资料 | hrcs |
| 210 | hrcs_dynascheme | 3V6C9072V4IO | t_hrcs_dynascheme |  | 动态授权方案 | 基础资料 | hrcs |
| 211 | hrcs_dynaschemerange | 3VJOP8WSJ4D2 | t_hrcs_dynaschemerange |  | 授权方案管理员范围 | 基础资料 | hrcs |
| 212 | hrcs_dynaschexdimgrp | 5+5RKV75IW+U | t_hrcs_dynaschexdimgrp |  | 动态权限方案例外维度值组 | 基础资料 | hrcs |
| 213 | hrcs_dynaschfield | 5+5RXCSA/9I8 | t_hrcs_dynaschfield |  | 动态权限方案字段权限 | 基础资料 | hrcs |
| 214 | hrcs_dynaschorg | 5+61J/5DHI76 | t_hrcs_dynaschorg |  | 动态权限方案角色业务组织 | 基础资料 | hrcs |
| 215 | hrcs_econtemplate | 2A9EWZYC3YRF | t_hrcs_econtemplate |  | 电子签署配置 | 基础资料 | hrcs |
| 216 | hrcs_econtnotify | 2BFD31WMERQ2 | t_hrcs_econtnotify |  | 电子合同异步通知注册表 | 基础资料 | hrcs |
| 217 | hrcs_empstrategy | 15I/L56JCAL3 | t_hbss_empstrategy |  | 行政组织-员工管理关系策略设置 | 基础资料 | hrcs |
| 218 | hrcs_encapiregister | 3BM0M1U+GBKE | t_hrcs_encapiregister |  | 微服务加密注册 | 基础资料 | hrcs |
| 219 | hrcs_entityctrl | 1ZKKMM842J/D | t_hrcs_entityctrl |  | 业务对象维度映射 | 基础资料 | hrcs |
| 220 | hrcs_entityfieldmp | 3EQGW71YI1JZ | t_hrcs_entityfieldmp |  | 实体字段取值关系 | 基础资料 | hrcs |
| 221 | hrcs_entityforbid | 294WL3RKR1YB | t_hrcs_entityforbid |  | 禁用权限 | 基础资料 | hrcs |
| 222 | hrcs_entityreleaseinfo | 1ZKF4F9=0IWY | t_hbss_entityreleaseinfo |  | 查询实体上线信息 | 基础资料 | hrcs |
| 223 | hrcs_entityspecfunc | 2CI1PN7B7T=U | t_hrcs_entityspecfunc |  | 实体特殊职能 | 基础资料 | hrcs |
| 224 | hrcs_entityspecfuncgen | 2CM2+9JFXTTC | t_hrcs_entityspecfuncgen |  | 实体特殊职能_自动生成 | 基础资料 | hrcs |
| 225 | hrcs_entitysyncconfig | 1ZKF89/6=FFB | t_hbss_entitysyncconfig |  | 实体同步配置 | 基础资料 | hrcs |
| 226 | hrcs_entitytreecfg | 1ZDCXJQ42F9L | t_hrcs_entitytreecfg |  | 实体权限树配置 | 基础资料 | hrcs |
| 227 | hrcs_esconfig | 2R2FVDAI8DPD | t_hrcs_esconfig |  | ES环境配置 | 基础资料 | hrcs |
| 228 | hrcs_esconfigwhitebill | 2M6S0I2M9WXD | t_hrcs_esconfigwhitebill |  | ES实体配置白名单 | 基础资料 | hrcs |
| 229 | hrcs_esignappcfg | 3ECU5A2RW1RT | t_hrcs_esignappcfg |  | 应用配置 | 基础资料 | hrcs |
| 230 | hrcs_esigncoauth | 3GSMPLNL9I2D | t_hrcs_esigncomauth |  | 企业授权管理 | 基础资料 | hrcs |
| 231 | hrcs_esigncoseal | 3F5C65E6JBX7 | t_hrcs_esigncoseal |  | 企业印章 | 基础资料 | hrcs |
| 232 | hrcs_esignsealauth | 3HDW+GB2W5+/ | t_hrcs_esignsealauth |  | 印章授权 | 基础资料 | hrcs |
| 233 | hrcs_esignsealtype | 3F2QR4=AOX/E | t_hrcs_esignsealtype |  | 印章类型 | 基础资料 | hrcs |
| 234 | hrcs_esignspmgr | 3E3457P0G9V3 | t_hrcs_esignspmgr |  | 电子签服务商管理 | 基础资料 | hrcs |
| 235 | hrcs_essyncrecord | 2LKA/46VRT52 | t_hrcs_essyncrecord |  | 同步记录 | 基础资料 | hrcs |
| 236 | hrcs_essyncschemecfig | 2MV1KXZUVE0A | t_hrcs_essyncschemeconf |  | ES同步方案配置 | 基础资料 | hrcs |
| 237 | hrcs_field_page | 3YUXJANFH55N |  |  | 选择需要的字段 | 基础资料 | hrcs |
| 238 | hrcs_filesyncfg | 4V6RN1MKOTTV | t_hrcs_filesyncfg |  | 权限档案同步配置 | 基础资料 | hrcs |
| 239 | hrcs_filterparam | 2VTVPMR65PD= | t_hrcs_filterparam |  | 过滤场景参数 | 基础资料 | hrcs |
| 240 | hrcs_filterscene | 2VTVMS+SQK5T | t_hrcs_filterscene |  | 过滤场景 | 基础资料 | hrcs |
| 241 | hrcs_formmeta | 2EQNYPOFJPZ+ | t_meta_formdesign | hrcs_formmeta, bos_formmeta | 表单元数据 | 基础资料 | hrcs |
| 242 | hrcs_function | 23IY21QP7M=I | t_hrcs_function |  | 函数配置 | 基础资料 | hrcs |
| 243 | hrcs_functiontype | 214OXXALDDD7 | t_hrcs_functiontype |  | 公式函数分类 | 基础资料 | hrcs |
| 244 | hrcs_hisbutimesequtest | 2RQLYGBZQ9L5 | t_hrcs_hisbutimesequtest |  | 时序受控模板单元测试 | 基础资料 | hrcs |
| 245 | hrcs_hrcloud | 21A2RL28V4B= | t_hrcs_hrcloud |  | HR云 | 基础资料 | hrcs |
| 246 | hrcs_ignorepermentity | 18ICWDE3HLTN | t_hrcs_ignorepermentity |  | 忽略验权实体 | 基础资料 | hrcs |
| 247 | hrcs_keywordmapping | 2AUM+LQ+SQZX | t_hrcs_keywordmapping |  | 模板变量取值关系配置 | 基础资料 | hrcs |
| 248 | hrcs_label | 2Y0T1H8RQE3Y | t_hrcs_label | hrcs_label, hrcs_labelvaluedisplay | 标签 | 基础资料 | hrcs |
| 249 | hrcs_labelapitest | 3Q87AW/EVZ8/ |  |  | API打标测试页面 | 基础资料 | hrcs |
| 250 | hrcs_labeldimension | 3J8WK7BM/QNC | t_hrcs_lblobjectfield | hrcs_labeldimension, hrcs_lblobjectfield | 打标圈定维度 | 基础资料 | hrcs |
| 251 | hrcs_labelgroup | 2W/PTDJWNGHP | t_hrcs_labelgroup |  | 标签分类 | 基础资料 | hrcs |
| 252 | hrcs_labelobject | 2UOL+82=+6A7 | t_hrcs_labelobject | hrcs_labelobject, hrcs_lbldelineatdim | 打标对象 | 基础资料 | hrcs |
| 253 | hrcs_labelobjectrel | 2YD=0JC6RA1N | t_hrcs_labelobjectrelnew |  | 标签打标对象关联关系 | 基础资料 | hrcs |
| 254 | hrcs_labelparam | 2X2TLQOWGVSS | t_hrcs_labelparam |  | 标签关联因子 | 基础资料 | hrcs |
| 255 | hrcs_labelpolicyrule | 2YNWH5H6DSTT | t_hrcs_labelpolicyrule |  | 打标策略规则 | 基础资料 | hrcs |
| 256 | hrcs_labelpolicytask | 2Y14/H41TF82 | t_hrcs_labelpolicytask |  | 打标策略执行表 | 基础资料 | hrcs |
| 257 | hrcs_labelscene | 2X5VK1KWHU1G | t_hrcs_labelscene |  | 标签场景 | 基础资料 | hrcs |
| 258 | hrcs_labelvalue | 2WT0+=Z+QR/M | t_hrcs_labelvaluenew |  | 标签值 | 基础资料 | hrcs |
| 259 | hrcs_labelvaluerule | 2X4WO=BF+SAP | t_hrcs_labelvaluerule |  | 标签值规则 | 基础资料 | hrcs |
| 260 | hrcs_lbldimension | 2X24/8ZTGDFD | t_hrcs_lbldimension |  | 打标对象维度（废弃） | 基础资料 | hrcs |
| 261 | hrcs_lblentityrelation | 2VK06PP=HP6O | t_hrcs_lblentityrelation |  | 标签实体关联关系 | 基础资料 | hrcs |
| 262 | hrcs_lblfieldtype | 2WQTZ4KS5EYE | t_hrcs_lblfieldtype |  | 因子分类 | 基础资料 | hrcs |
| 263 | hrcs_lbljoinentity | 2VJZ=6Q12PRB | t_hrcs_lbljoinentity |  | 标签关联实体 | 基础资料 | hrcs |
| 264 | hrcs_lblobjconfig | 2VGC47UA84G4 | t_hrcs_lblobjconfig |  | 能力配置 | 基础资料 | hrcs |
| 265 | hrcs_lblstrategy | 2WG/DV8/1EQU | t_hrcs_labelpolicy |  | 打标策略 | 基础资料 | hrcs |
| 266 | hrcs_lblstrategyfilter | 2WNBCF0HEYDH | t_hrcs_lblpolicyfilter |  | 打标策略打标范围 | 基础资料 | hrcs |
| 267 | hrcs_needdeletetask | 4BF6LOT2TAQW | t_hrcs_needdeletetask |  | 需要清除的定时任务白名单 | 基础资料 | hrcs |
| 268 | hrcs_odc_field_perm | 4=MZDBQYFQ2+ | t_hrcs_odc_field_perm |  | 组织基础资料控权 | 基础资料 | hrcs |
| 269 | hrcs_orgoptimconfig | 3KIHKU9FK/OS | t_hrcs_orgoptimconfig |  | 组织维度优化配置 | 基础资料 | hrcs |
| 270 | hrcs_orgstrategy | 15I1YPE6ME++ | t_hbss_orgstrategy |  | 行政组织-组织管理关系策略设置 | 基础资料 | hrcs |
| 271 | hrcs_orgtreepermpropcfg | 3VJ=PF6HOOAD | t_hrcs_orgtreepermpropcfg |  | 行政组织左树控权属性配置 | 基础资料 | hrcs |
| 272 | hrcs_permfilegrp | 0RIP/5XIQKWC | t_hbss_permfilegrp |  | 用户权限档案组 | 基础资料 | hrcs |
| 273 | hrcs_permfilegrpmember | 0RMF0LY4=6F8 | t_hbss_permfilegrpmember |  | 权限档案组明细表 | 基础资料 | hrcs |
| 274 | hrcs_perminitdemo_userbd | 35S7BFZ0SQZH | t_hrcs_pinitdemouserbd |  | 权限初始化用户基础资料范围样例 | 基础资料 | hrcs |
| 275 | hrcs_perminitdemo_userdim | 35R0+5L7IQOA | t_hrcs_pinitdemouserdim |  | 权限初始化用户维度值样例 | 基础资料 | hrcs |
| 276 | hrcs_perminitdemo_userdr | 35S6/L3EMPFM | t_hrcs_pinitdemouserdr |  | 权限初始化用户数据规则样例 | 基础资料 | hrcs |
| 277 | hrcs_perminitdemo_userfld | 35S7ZTMQ+JT6 | t_hrcs_pinitdemouserfld |  | 权限初始化用户字段权限样例 | 基础资料 | hrcs |
| 278 | hrcs_perminitrecord | 31QV=UQX91PY | t_hrcs_pinitrecord |  | 权限初始化任务 | 基础资料 | hrcs |
| 279 | hrcs_permrelat | 2+3C=1C+OMP6 | t_hrcs_permrelat |  | 关联权限项 | 基础资料 | hrcs |
| 280 | hrcs_permrelatcfg | 2KOO0O61IMMJ | t_hrcs_permrelatcfg |  | 独立授权 | 基础资料 | hrcs |
| 281 | hrcs_permtemp | 19OR06QETS9K | t_hrcs_permtemp |  | 权限模板导入配置 | 基础资料 | hrcs |
| 282 | hrcs_presetchange | 3YAV5DD47X9H | t_hrcs_presetchange |  | 预置数据变动登记表 | 基础资料 | hrcs |
| 283 | hrcs_projempstrategy | 2H4Q8+VDA4I/ | t_hrcs_projempstrategy |  | 项目团队-员工管理关系策略设置 | 基础资料 | hrcs |
| 284 | hrcs_projorgstrategy | 2H4QA4SYFXCC | t_hrcs_projorgstrategy |  | 项目团队-组织管理关系策略设置 | 基础资料 | hrcs |
| 285 | hrcs_prompt | 1GZPOTSAHIEK | t_hrcs_prompt |  | 提示语配置 | 基础资料 | hrcs |
| 286 | hrcs_promptcontent | 1GZPOZCX=NA7 | t_hrcs_promptcontent |  | 提示语内容 | 基础资料 | hrcs |
| 287 | hrcs_promptfieldchoose | 1I3WTAS5HZT/ |  |  | 提示语属性选择 | 基础资料 | hrcs |
| 288 | hrcs_promptimport | 1MXN2RCIBTUO | t_hrcs_promptimport |  | 提示语导入实体 | 基础资料 | hrcs |
| 289 | hrcs_promptrule | 1GZPP9KGFCN= | t_hrcs_promptrule |  | 提示语映射 | 基础资料 | hrcs |
| 290 | hrcs_querydynsourcelist | 294M5PJ4+20K | t_meta_entitydesign |  | HR多实体查询配置列表 | 基础资料 | hrcs |
| 291 | hrcs_rcrelation | 47WZLSBAV7YF |  |  | 预警接收人人员关系 | 基础资料 | hrcs |
| 292 | hrcs_realnameauthmode | 3E6+X725M8U= | t_hrcs_realnameauthmode |  | 个人实名认证方式 | 基础资料 | hrcs |
| 293 | hrcs_relateentity | 36REFQHN/9DC | t_hrcs_relateentity |  | 关联实体 | 基础资料 | hrcs |
| 294 | hrcs_role | 0RMF38CCFKWX | t_hbss_permrole |  | HR通用角色 | 基础资料 | hrcs |
| 295 | hrcs_roleassignscope | 2V7750SF2CPA | t_hrcs_roleassignscope |  | 角色分配范围 | 基础资料 | hrcs |
| 296 | hrcs_rolebu | 0SFJW2=VM=30 | t_hbss_hrrolebu |  | 角色业务单元范围 | 基础资料 | hrcs |
| 297 | hrcs_rolebucafunc | 0X53AZ7FB=GW | t_hbss_rolebucafunc |  | 角色功能权限职能 | 基础资料 | hrcs |
| 298 | hrcs_roledatarule | 16D6JCMHAFKU | t_hrcs_roledatarule |  | 角色数据规则 | 基础资料 | hrcs |
| 299 | hrcs_roledimension | 1=6WY3J64ISV | t_hrcs_roledimension |  | 角色维度关系 | 基础资料 | hrcs |
| 300 | hrcs_roledimgrp | 2+6JSNY6I=WX | t_hrcs_roledimgrp |  | 角色维度组 | 基础资料 | hrcs |
| 301 | hrcs_roleexdimgrp | 4HH24PTI4ADT | t_hrcs_roleexdimgrp |  | 角色例外维度组 | 基础资料 | hrcs |
| 302 | hrcs_rolefield | 17N7A9LV2Z1P | t_hrcs_rolefield |  | 角色实体列权限 | 基础资料 | hrcs |
| 303 | hrcs_rolegrp | 0SXB7BI4DEH4 | t_hbss_permrolegrp |  | 角色分组 | 基础资料 | hrcs |
| 304 | hrcs_roleinitdemo_dim | 3FM72P/64ZP6 | t_hrcs_roleinitdemo_dim |  | 角色初始化角色维度样例 | 基础资料 | hrcs |
| 305 | hrcs_roleinitdemo_dr | 3FXGFAKRBZ8X | t_hrcs_roleinitdemo_dr |  | 角色初始化角色数据范围样例 | 基础资料 | hrcs |
| 306 | hrcs_roleinitdemo_fc | 3FFHFTNS46MI | t_hrcs_roleinitdemo_fc |  | 角色初始化功能权限样例 | 基础资料 | hrcs |
| 307 | hrcs_roleinitdemo_fp | 3G+6O5PIVXOJ | t_hrcs_roleinitdemo_fp |  | 角色初始化字段权限样例 | 基础资料 | hrcs |
| 308 | hrcs_roleinitdemo_role | 3EY=222WUDW0 | t_hrcs_roleinitdemo_role |  | 角色初始化角色清单样例 | 基础资料 | hrcs |
| 309 | hrcs_roleopenscope | 2V78/TAA4CH3 | t_hrcs_roleopenscope |  | 角色公开范围 | 基础资料 | hrcs |
| 310 | hrcs_ruleschpolicynumber | 4Z73L6B7719V |  |  | 协作规则方案策略编码专用 | 基础资料 | hrcs |
| 311 | hrcs_serviceregister | 263K9=TMGYGE | t_hrcs_calserviceregister |  | 日历控件微服务注册 | 基础资料 | hrcs |
| 312 | hrcs_strategy | 15I4Q9T8WZX2 | t_hbss_strategy |  | 基础资料_策略类型 | 基础资料 | hrcs |
| 313 | hrcs_sysmaxdateconfig | 5I9KHT/FTO8T | t_hrcs_sysmaxdateconfig |  | 系统最大日期配置 | 基础资料 | hrcs |
| 314 | hrcs_tplvariableconfig | 3CZ7IJRRGRKR | t_hrcs_tplvariableconfig | hrcs_addtplvariableconfig, hrcs_tplvariableconfig | 取值对象配置 | 基础资料 | hrcs |
| 315 | hrcs_useradmingroup | 2BFJAVQXE6R3 | t_perm_useradmingroup |  | HR管理员 | 基础资料 | hrcs |
| 316 | hrcs_userdatarule | 16D6R0L4AHBL | t_hrcs_userdatarule |  | 用户角色数据规则 | 基础资料 | hrcs |
| 317 | hrcs_userfield | 17N2L=DLJJX+ | t_hrcs_userfield |  | 用户角色实体列权限 | 基础资料 | hrcs |
| 318 | hrcs_useroletest | 3CRHQ8Q810P1 |  |  | 用户角色接口测试 | 基础资料 | hrcs |
| 319 | hrcs_userpermfile | 0SFXP64OI6BW | t_hbss_permfiles |  | 用户权限档案 | 基础资料 | hrcs |
| 320 | hrcs_userrole | 0T4WTM1PSFVV | t_hbss_userrole |  | 用户角色业务单元范围 | 基础资料 | hrcs |
| 321 | hrcs_userroledimgrp | 2+6ZWDDVDAM1 | t_hrcs_userroledimgrp |  | 用户角色维度组 | 基础资料 | hrcs |
| 322 | hrcs_userroleexdimgrp | 4HH46JRND8UI | t_hrcs_userroleexdimgrp |  | 用户角色例外维度组 | 基础资料 | hrcs |
| 323 | hrcs_userrolerelat | 16RU630BQSU4 | t_hrcs_userrolerelat |  | 用户角色关联关系 | 基础资料 | hrcs |
| 324 | hrcs_varmappingscene | 5G5E=+45KX/M | t_hrcs_varmappingscene |  | 变量映射场景 | 基础资料 | hrcs |
| 325 | hrcs_warncalfield | 3UZ3BSIMQE2L | t_hrcs_warncalfield |  | HR预警计算字段 | 基础资料 | hrcs |
| 326 | hrcs_warnmonthevent | 4/9NNLS3BCG= | t_hrcs_warnmonthevent |  | 每月最后一天冲突事务表 | 基础资料 | hrcs |
| 327 | hrcs_warnmsgconf | 441RN5I4V17K | t_hrcs_warncontext |  | 预警消息 | 基础资料 | hrcs |
| 328 | hrcs_warnmsgpersist | 476625PZOZ7B | t_hrcs_warnmsgpersist |  | 消息详情 | 基础资料 | hrcs |
| 329 | hrcs_warnmsgrc | 47J4+OSR9NVJ | t_hrcs_warnmsgrcr |  | 消息接收人 | 基础资料 | hrcs |
| 330 | hrcs_warnmsgrowdata | 48M67=DGDGWC | t_hrcs_warnmsgrowdata |  | 消息详情持久化行数据 | 基础资料 | hrcs |
| 331 | hrcs_warnmsgtable | 441TZ6+6DESK | t_hrcs_warnmsgtable |  | 预警消息表格 | 基础资料 | hrcs |
| 332 | hrcs_warnobjectfieldf7 | 49S5+UU17FYP | t_hrcs_warnobjectfield | hrcs_selectqueryfieldf7, hrcs_warnobjectfieldf7, hrcs_warnscenequeryfield | 预警场景字段F7 | 基础资料 | hrcs |
| 333 | hrcs_warnobjtpl | 43+SLC3=E3OG | t_hrcs_warnobjecttpl | hrcs_warnobjtpl, hrcs_warnobjtpl_listout | 预警对象模板(废弃) | 基础资料 | hrcs |
| 334 | hrcs_warnpermrolef7 | 4KPWI+LZBOXB |  |  | 预警角色选择F7 | 基础资料 | hrcs |
| 335 | hrcs_warnpluginservice | 3XZVXLXAJP4T | t_hrcs_warnpluginservice |  | 预警插件服务 | 基础资料 | hrcs |
| 336 | hrcs_warnscene | 441YCYWTTUWA | t_hrcs_warnscene |  | 预警场景 | 基础资料 | hrcs |
| 337 | hrcs_warnsceneentityrel | 3V/YCZCAOP1= | t_hrcs_warnobjectrelation |  | 预警对象实体关联关系 | 基础资料 | hrcs |
| 338 | hrcs_warnscenejoinentity | 3V/ZOHTVR6ZK | t_hrcs_warnobjectentity |  | 预警场景关联实体 | 基础资料 | hrcs |
| 339 | hrcs_warnscheme | 40H1/OK3YH1U | t_hrcs_warnplan |  | 预警方案 | 基础资料 | hrcs |
| 340 | hrcs_workingplanallrange | 23J2C=89YS09 | t_hrcs_workingplanallrang |  | 工作日历适用范围 | 基础资料 | hrcs |
| 341 | hrcs_workingplanpreview | 22C2CZRZFUH/ |  |  | 预览 | 基础资料 | hrcs |
| 342 | hrpi_appointremoverel | 3ADU27WG/KPT | t_hrpi_appointremoverel |  | 任免经历 | 基础资料 | hrpi |
| 343 | hrpi_assignment | 4S28Q83PUQOP | t_hrpi_assignment |  | 组织分配 | 基础资料 | hrpi |
| 344 | hrpi_assignmentmag | 4S6F5D1M7PD1 | t_hrpi_assignmentmag |  | 组织分配管理主体 | 基础资料 | hrpi |
| 345 | hrpi_blacklist | 3D0HBFW7DE2P | t_hrpi_blacklist |  | 黑名单 | 基础资料 | hrpi |
| 346 | hrpi_blacklistrmreason | 3COXV+FMQNH5 | t_hrpi_blacklistrmreason |  | 移除原因 | 基础资料 | hrpi |
| 347 | hrpi_contractinfo | 4V=6+PGXWIIJ | t_hrpi_contractinfo |  | 合同信息 | 基础资料 | hrpi |
| 348 | hrpi_debardinfo | 3+99YGVCX8HD | t_hrpi_debardinfo |  | 回避信息 | 基础资料 | hrpi |
| 349 | hrpi_dispatchinfo | 4SF7N=NQ0W4T | t_hrpi_dispatchinfo |  | 外派信息 | 基础资料 | hrpi |
| 350 | hrpi_empcadre | 3KVJ+EQQ4=NA | t_hrpi_empcadre |  | 最高干部身份信息 | 基础资料 | hrpi |
| 351 | hrpi_empentrel | 15BEKB7/1N+G | t_hrpi_empentrel |  | 雇佣信息 | 基础资料 | hrpi |
| 352 | hrpi_empjobrel | 15BEN9B6=4FR | t_hrpi_empjobrel |  | 职级职等 | 基础资料 | hrpi |
| 353 | hrpi_employee | /IR5TB9YFR5S | t_hrpi_employee |  | 员工 | 基础资料 | hrpi |
| 354 | hrpi_employeehired_init | 5/+B71RZH98M |  |  | 人员信息中间表配置(简版) | 基础资料 | hrpi |
| 355 | hrpi_employeetaxcn | 5GHMP8P67X0P | t_hrpi_employeetaxcn |  | 员工个税信息 | 基础资料 | hrpi |
| 356 | hrpi_emporgrelall | 15D688PCFF12 | t_hrpi_emporgrelall |  | 任职经历总表 | 基础资料 | hrpi |
| 357 | hrpi_empposorgrel | 15D680ZV7YNR | t_hrpi_empposorgrel |  | 任职经历 | 基础资料 | hrpi |
| 358 | hrpi_empproexp | 15BEMQ/8K/2J | t_hrpi_empproexp |  | 项目经历 | 基础资料 | hrpi |
| 359 | hrpi_empstage | 59AD5BIUCL3V | t_hrpi_empstage |  | 雇佣阶段 | 基础资料 | hrpi |
| 360 | hrpi_empsuprel | 15BELENT0VN= | t_hrpi_empsuprel |  | 汇报关系 | 基础资料 | hrpi |
| 361 | hrpi_emptrainfile | 15BEKW7O50M/ | t_hrpi_emptrainfile |  | 培训经历 | 基础资料 | hrpi |
| 362 | hrpi_emptutor | 2AU201HQ6UT7 | t_hrpi_emptutor |  | 导师 | 基础资料 | hrpi |
| 363 | hrpi_emrgcontact | 15UTU8D62D1W | t_hrpi_emrgcontact |  | 紧急联系人 | 基础资料 | hrpi |
| 364 | hrpi_familymemb | 160IL+K0WSQ4 | t_hrpi_familymemb |  | 家庭成员 | 基础资料 | hrpi |
| 365 | hrpi_fertilityinfo | 1V747XRLVAK9 | t_hrpi_fertilityinfo |  | 生育信息 | 基础资料 | hrpi |
| 366 | hrpi_globalperson | 4S6A1J6R6P8K | t_hrpi_globalperson |  | 全球员工 | 基础资料 | hrpi |
| 367 | hrpi_partymember | 2ZXIMMX=7I78 | t_hrpi_partymember |  | 党员信息 | 基础资料 | hrpi |
| 368 | hrpi_peraddress | 15URMINHJMOE | t_hrpi_peraddress |  | 人员地址 | 基础资料 | hrpi |
| 369 | hrpi_percontact | 15BJSAYI2C94 | t_hrpi_percontact |  | 联系方式 | 基础资料 | hrpi |
| 370 | hrpi_percre | 15BJS5+H711/ | t_hrpi_percre |  | 证件信息 | 基础资料 | hrpi |
| 371 | hrpi_pereduexp | 15B5/4HZBDXR | t_hrpi_pereduexp |  | 教育经历 | 基础资料 | hrpi |
| 372 | hrpi_perfresult | 5GZ=S563DH+Y | t_hrpi_perfresult |  | 绩效结果 | 基础资料 | hrpi |
| 373 | hrpi_perhobby | 15BJVQ423D4J | t_hrpi_perhobby |  | 特长及爱好 | 基础资料 | hrpi |
| 374 | hrpi_perlgability | 4SMSPSZUN01B | t_hrpi_perlgability |  | 语言技能 | 基础资料 | hrpi |
| 375 | hrpi_perocpqual | 15BJUKF95J5Q | t_hrpi_perocpqual |  | 职业资格 | 基础资料 | hrpi |
| 376 | hrpi_perpractqual | 15BJUD1W351B | t_hrpi_perpractqual |  | 执业资格 | 基础资料 | hrpi |
| 377 | hrpi_perprotitle | 15BJTMZ+WPM7 | t_hrpi_perprotitle |  | 职称信息 | 基础资料 | hrpi |
| 378 | hrpi_perregion | 15EGKEJ0J32/ | t_hrpi_perregion |  | 区域信息 | 基础资料 | hrpi |
| 379 | hrpi_perrprecord | 15BJTFGI=2CS | t_hrpi_perrprecord |  | 奖惩记录 | 基础资料 | hrpi |
| 380 | hrpi_perserlen | 15AZ4444SCI6 | t_hrpi_perserlen |  | 服务年限 | 基础资料 | hrpi |
| 381 | hrpi_personentityconf | 2/9EQRTLMT74 | t_hrpi_personentityconf |  | 人员模型配置信息（废弃） | 基础资料 | hrpi |
| 382 | hrpi_personuserrel | 2H1CA7DCM6K/ | t_hrpi_personuserrel |  | HR人员与平台用户关联信息 | 基础资料 | hrpi |
| 383 | hrpi_preworkexp | 15VL/M36S033 | t_hrpi_preworkexp |  | 前工作经历 | 基础资料 | hrpi |
| 384 | hrpi_quittype | 4URHHB6W+GUT | t_hrpi_quittype |  | 离职类型 | 基础资料 | hrpi |
| 385 | hrpi_reemploymentrel | 5N=4I/0/R13+ |  |  | 再入职工号关联实体 | 基础资料 | hrpi |
| 386 | hrpi_rotationinfo | 3N/MW32RRZSB | t_hrpi_rotationinfo |  | 轮岗情况 | 基础资料 | hrpi |
| 387 | hrpi_rptcomonsort | 3D4CBLNC1D0V | t_hrpi_rptcomonsort |  | 人员报表通用排序 | 基础资料 | hrpi |
| 388 | hrpi_rptcustomsort | 3D4CCI7FTP+M | t_hrpi_rptcustomsort |  | 人员报表自定义排序 | 基础资料 | hrpi |
| 389 | hrpi_rsmpatinv | 1WBLHXLECOIB | t_hrpi_rsmpatinv |  | 专利发明 | 基础资料 | hrpi |
| 390 | hrpi_rsmproskl | 1V72406V6BC/ | t_hrpi_rsmproskl |  | 专业技能 | 基础资料 | hrpi |
| 391 | hrpi_sortcoderegister | 5IG71V9JL1RI | t_hrpi_sortcoderegister |  | 排序码注册列表 | 基础资料 | hrpi |
| 392 | hrpi_terminationinfo | 4SELNRISIN49 | t_hrpi_terminationinfo |  | 离职信息 | 基础资料 | hrpi |
| 393 | hrpi_toblacklistreason | 3D0+X9+QC=1U | t_hrpi_toblacklistreason |  | 加入原因 | 基础资料 | hrpi |
| 394 | hrpi_trialperiod | 1HDE8EY//M0A | t_hrpi_trialperiod |  | 试用期 | 基础资料 | hrpi |
| 395 | hrptmc_algorithmcol | 3F8R2+8H/GSB | t_hrptmc_algorithmcol |  | 汇总列 | 基础资料 | hrptmc |
| 396 | hrptmc_anobjconfighis | 47I=K0MF8RJ= | t_hrptmc_anobjconfighis |  | 分析对象配置历史版本 | 基础资料 | hrptmc |
| 397 | hrptmc_anobjentityrel | 2VJTMLQBE0RQ | t_hrptmc_anobjentjoinrel |  | 分析对象实体关联关系 | 基础资料 | hrptmc |
| 398 | hrptmc_anobjextract | 408Y/BLWRR81 | t_hrptmc_anobjextract |  | 分析对象数据抽取配置存储 | 基础资料 | hrptmc |
| 399 | hrptmc_anobjfield_f7 | 3MQWYEL60ZN6 | t_hrptmc_anobjqueryfield | hrptmc_anobjfield_f7, hrptmc_anobjqueryfield | 分析对象查询字段_F7布局 | 基础资料 | hrptmc |
| 400 | hrptmc_anobjfieldmap | 40=W34YQ=00+ | t_hrptmc_anobjfieldmap |  | 分析对象落地字段映射 | 基础资料 | hrptmc |
| 401 | hrptmc_anobjgroupfield | 3NFMXIK76=RL | t_hrptmc_angroupfield |  | 分析对象分组赋值 | 基础资料 | hrptmc |
| 402 | hrptmc_anobjjoinentity | 2VJT18CAI32C | t_hrptmc_anobjjoinentity |  | 分析对象关联实体 | 基础资料 | hrptmc |
| 403 | hrptmc_anobjpivot | 3SBTXR8I1REA | t_hrptmc_anobjpivot |  | 分析对象行列转置信息 | 基础资料 | hrptmc |
| 404 | hrptmc_anobjsidebar | 3NFM956HKGKZ | t_hrptmc_anobjsidebar |  | 分析对象数据加工侧边栏 | 基础资料 | hrptmc |
| 405 | hrptmc_anobjtemplib | 4+THSN0YF0/3 | t_hrptmc_analysisobject | hrptmc_analyseobject, hrptmc_anobjtemplib | 分析对象模板库 | 基础资料 | hrptmc |
| 406 | hrptmc_bizsortentity | 36J7UPTJZ2IL | t_hrptmc_bizsortentity |  | 业务排序实体信息 | 基础资料 | hrptmc |
| 407 | hrptmc_busiservice | 2WJCH56/Z46X | t_hrptmc_busiservice |  | 业务服务 | 基础资料 | hrptmc |
| 408 | hrptmc_calculatefield | 2VFS8JTKXAR5 | t_hrptmc_calculatefield |  | 计算字段 | 基础资料 | hrptmc |
| 409 | hrptmc_calmaxlen | 45A5QS4PVR70 | t_hrptmc_calmaxlen |  | 计算字段最大长度配置表 | 基础资料 | hrptmc |
| 410 | hrptmc_colcustomsort | 354F5LK126QB | t_hrptmc_colcustomsort |  | 自定义排序（废弃） | 基础资料 | hrptmc |
| 411 | hrptmc_colfield | 2W/O98P0RADW | t_hrptmc_colfield |  | 列字段 | 基础资料 | hrptmc |
| 412 | hrptmc_commonsort | 35SAL2YTWS4W | t_hrptmc_commonsort |  | 通用排序 | 基础资料 | hrptmc |
| 413 | hrptmc_commonsortdetail | 37+KJJE812YT | t_hrptmc_commonsortdet |  | 报表通用排序详情 | 基础资料 | hrptmc |
| 414 | hrptmc_customsort | 35R4=RKY0BY5 | t_hrptmc_customsort |  | 自定义排序 | 基础资料 | hrptmc |
| 415 | hrptmc_customsortdetail | 37+K6N6HHWRB | t_hrptmc_customsortdet |  | 报表字段自定义详情 | 基础资料 | hrptmc |
| 416 | hrptmc_datastoreinfo | 4NWJPQL45WDB | t_hrptmc_datastoreinfo |  | 数据落地信息查询 | 基础资料 | hrptmc |
| 417 | hrptmc_dimcount | 3/M7JP9QB459 | t_hrptmc_dimcount |  | 维度数量记录表 | 基础资料 | hrptmc |
| 418 | hrptmc_dimensioncount | 3914LAN/K0L+ | t_hrptmc_dimensioncount |  | 维度计数（废弃） | 基础资料 | hrptmc |
| 419 | hrptmc_dimmap | 2Y4VSGLRMENO | t_hrptmc_dimmap |  | 维度映射 | 基础资料 | hrptmc |
| 420 | hrptmc_dirtydatarepair | 42=6Y70JJ3=1 | t_hrptmc_dirtydatarepair |  | 抽取脏数据修复 | 基础资料 | hrptmc |
| 421 | hrptmc_dispscmchg | 3584CQX1Y8U/ | t_hrptmc_dispscmchg |  | 显示方案变更通知 | 基础资料 | hrptmc |
| 422 | hrptmc_esindex | 48P9JW=6B6Z+ | t_hrptmc_esindex |  | es索引 | 基础资料 | hrptmc |
| 423 | hrptmc_esmapping | 48P=8GVGSLV1 | t_hrptmc_esmapping |  | es映射 | 基础资料 | hrptmc |
| 424 | hrptmc_filesourceenum | 3W38J0S/9KPC | t_hrptmc_fsenum |  | 文件数据源枚举 | 基础资料 | hrptmc |
| 425 | hrptmc_filesourcetable | 3V6=M+JU75P1 | t_hrptmc_fstable |  | 文件数据源物理表信息 | 基础资料 | hrptmc |
| 426 | hrptmc_filter | 2VUH2N8NV7P4 | t_hrptmc_filter |  | 筛选器设置 | 基础资料 | hrptmc |
| 427 | hrptmc_filterextfield | 41=XN+JVEM8W | t_hrptmc_filterextfield |  | 筛选器二开插件字段 | 基础资料 | hrptmc |
| 428 | hrptmc_mysubscribe | 48+DFK4+PK4L | t_hrptmc_mysubscribe | hrptmc_mysubscribe, hrptmc_mysubscribe_layout | 我的订阅 | 基础资料 | hrptmc |
| 429 | hrptmc_permrule | 3MRLCON6Z2G3 | t_hrptmc_permrule |  | 分析对象数据控权规则 | 基础资料 | hrptmc |
| 430 | hrptmc_preindex | 2W/WJWOZYV9I | t_hrptmc_preindex |  | 预置指标 | 基础资料 | hrptmc |
| 431 | hrptmc_publishmenu | 2XYX+9H6VAO0 | t_hrptmc_publishmenu |  | 报表发布菜单 | 基础资料 | hrptmc |
| 432 | hrptmc_queryscheme | 3OOTZ11QRDC9 | t_hrptmc_queryscheme |  | 报表高级查询方案 | 基础资料 | hrptmc |
| 433 | hrptmc_queryscmchg | 3R=AWTW81Y9W | t_hrptmc_queryscmchg |  | 高级查询方案变更通知 | 基础资料 | hrptmc |
| 434 | hrptmc_reflineconf | 4NX3XD0LV2=W | t_hrptmc_reflineconf |  | 图表参考线配置 | 基础资料 | hrptmc |
| 435 | hrptmc_reportconfig | 2VY3N=YDWUTC | t_hrptmc_reportconfig |  | 报表配置 | 基础资料 | hrptmc |
| 436 | hrptmc_reportconfighis | 47ICKKT6=KN8 | t_hrptmc_reportconfighis |  | 报表配置历史版本 | 基础资料 | hrptmc |
| 437 | hrptmc_reportjump | 3EWQ8BT=+F/U | t_hrptmc_rptjump |  | 报表跳转配置 | 基础资料 | hrptmc |
| 438 | hrptmc_reportmanage | 2VY37BL22XAJ | t_hrptmc_reportmanage |  | 报表管理 | 基础资料 | hrptmc |
| 439 | hrptmc_reportmapping | 4/F55M7/8YJ/ | t_hrptmc_reportmapping |  | 报表抽取映射 | 基础资料 | hrptmc |
| 440 | hrptmc_reportmark | 3982YW2BF7O2 | t_hrptmc_reportmark |  | 报表说明配置 | 基础资料 | hrptmc |
| 441 | hrptmc_reportpreindex | 2Z6OXAU04Q46 | t_hrptmc_reportpreindex |  | 报表关联预置指标 | 基础资料 | hrptmc |
| 442 | hrptmc_rowcustomsort | 354CBC3110DS | t_hrptmc_rowcustomsort |  | 自定义排序（废弃） | 基础资料 | hrptmc |
| 443 | hrptmc_rowfield | 2VY68ZWZYE2T | t_hrptmc_rowfield |  | 行字段 | 基础资料 | hrptmc |
| 444 | hrptmc_rptcomref | 3984N=JX=PES | t_hrptmc_rptcomref |  | 报表通用排序关系 | 基础资料 | hrptmc |
| 445 | hrptmc_rptdispscm | 36K7IFZ2X8H4 | t_hrptmc_rptdispscm |  | 显示方案配置 | 基础资料 | hrptmc |
| 446 | hrptmc_rptdispscmcol | 36K83L4M+S63 | t_hrptmc_rptdispscmcol |  | 报表显示方案配置_列 | 基础资料 | hrptmc |
| 447 | hrptmc_rptdispscmidx | 36K8FMJH=EU7 | t_hrptmc_rptdispscmidx |  | 报表显示方案配置_指标 | 基础资料 | hrptmc |
| 448 | hrptmc_rptdispscmrow | 36K7QKR3+J+= | t_hrptmc_rptdispscmrow |  | 报表显示方案配置_行 | 基础资料 | hrptmc |
| 449 | hrptmc_rptdisscmety | 59ALJ=6+1QPQ | t_hrptmc_rptdispscmety |  | 显示方案配置 | 基础资料 | hrptmc |
| 450 | hrptmc_rptmarkcontent | 39GZEPUUI7X2 | t_hrptmc_rptmarkcontent |  | 报表说明内容 | 基础资料 | hrptmc |
| 451 | hrptmc_selparam | 2WMUWTGRQ47M | t_hrptmc_busisrvparam |  | 参数选择 | 基础资料 | hrptmc |
| 452 | hrptmc_setdataformat | 2XAUHAZARRUA |  |  | 数据格式设置 | 基础资料 | hrptmc |
| 453 | hrptmc_share_filterscheme | 3OPAK4F6+==H | t_hrptmc_sharescheme |  | 报表共享过滤方案 | 基础资料 | hrptmc |
| 454 | hrptmc_sharefilterrange | 48PB=T9ETJ93 | t_hrptmc_sharefilterrange |  | 分享报表筛选器选择范围 | 基础资料 | hrptmc |
| 455 | hrptmc_sharerecord | 48+EX4DS0OFG | t_hrptmc_sharerecord | hrptmc_sharerecord, hrptmc_sharerecord_layout, hrptmc_sharerecord_log | 分享记录 | 基础资料 | hrptmc |
| 456 | hrptmc_splitdate | 3N+QQDQVCW6N | t_hrptmc_splitdate |  | 日期字段拆分粒度表 | 基础资料 | hrptmc |
| 457 | hrptmc_subscriberecord | 48+4Q61=6JUA | t_hrptmc_subscriberecord |  | 订阅记录 | 基础资料 | hrptmc |
| 458 | hrptmc_syncpolicy | 2X4X6AHE71R= | t_hrptmc_syncpolicy |  | 同步策略 | 基础资料 | hrptmc |
| 459 | hrptmc_unitestentity01 | 3=NFH7CPV2SX | t_hrptmc_unitestentity01 |  | 报表单元测试实体01 | 基础资料 | hrptmc |
| 460 | hrptmc_unitestentity02 | 3=NH9L60I9UW | t_hrptmc_unitestentity02 |  | 报表单元测试实体02 | 基础资料 | hrptmc |
| 461 | hrptmc_userdispscm | 372NZTKTEMK0 | t_hrptmc_userdispscm |  | 显示方案配置 | 基础资料 | hrptmc |
| 462 | hrptmc_virtentfields | 3DO6N+1Y4NP+ | t_hrptmc_virtentfields |  | 虚拟对象字段 | 基础资料 | hrptmc |
| 463 | hrptmc_virtentityclass | 3DN9QUQH1WCD | t_hrptmc_virtentityclass |  | 虚拟对象处理类 | 基础资料 | hrptmc |
| 464 | hrptmc_virtualentity | 33G0W90+R3X+ | t_hrptmc_virtualentity |  | 虚拟对象 | 基础资料 | hrptmc |
| 465 | hrptmc_virtualfieldgroup | 33I2Z2XGOXOJ | t_hrptmc_vfieldgroup |  | 虚实体字段分组 | 基础资料 | hrptmc |
| 466 | hrptmc_workreport | 408DEN5YLMNT | t_hrptmc_workreport |  | 工作表 | 基础资料 | hrptmc |
| 467 | hrss_aiwordcategory | 2KBBLDUEDK8T | t_hrss_aiwordcategory |  | AI词性 | 基础资料 | hrss |
| 468 | hrss_comoperator | 41=SYVTI5979 | t_hrss_comoperator |  | 比较符管理 | 基础资料 | hrss |
| 469 | hrss_customfilter | 3U=ELGYH4J5X | t_hrss_customfilter |  | 自定义过滤项 | 基础资料 | hrss |
| 470 | hrss_essyncscheme | 3VG8LIFIMMIU | t_hrss_essyncscheme |  | ES同步任务管理 | 基础资料 | hrss |
| 471 | hrss_essynrecord | 3W69/J6OT5OW | t_hrss_essynrecord |  | ES同步记录 | 基础资料 | hrss |
| 472 | hrss_essyntimerecord | 3YZ3S4BJFSW+ | t_hrss_essyntimerecord |  | es同步时间记录 | 基础资料 | hrss |
| 473 | hrss_labelgptsync | 3W6X972QDK08 | t_hrss_labelgptsync |  | 标签GPT知识库同步 | 基础资料 | hrss |
| 474 | hrss_scenefiltergroup | 4J9KAL/TXLA1 | t_hrss_scenefiltergroup |  | 分组 | 基础资料 | hrss |
| 475 | hrss_schobjentityrel | 3TYSVLA9DZB5 | t_hrss_schobjrelation |  | 搜索对象实体关联关系 | 基础资料 | hrss |
| 476 | hrss_schobjjoinentity | 3TYSRB+Z/88R | t_hrss_schobjentity |  | 搜索对象关联实体 | 基础资料 | hrss |
| 477 | hrss_schobjqueryfield | 3TYSYWD99YIY | t_hrss_schobjfield |  | 搜索对象查询字段 | 基础资料 | hrss |
| 478 | hrss_searchconfig | 3UA6PZ6J5UTC | t_hrss_searchconfig |  | 业务搜索页面注册表 | 基础资料 | hrss |
| 479 | hrss_searchkeycount | 41NQV9R+MDND |  |  | 常用关键词搜索统计 | 基础资料 | hrss |
| 480 | hrss_searchobject | 3TYGC5Q0AWD= | t_hrss_searchobject |  | 搜索对象 | 基础资料 | hrss |
| 481 | hrss_searchrange | 2KLNS2V5JN=E |  |  | 精准搜索范围 | 基础资料 | hrss |
| 482 | hrss_searchscene | 2KL6Z7YBH6MJ | t_hrss_searchscenecfg |  | 搜索场景 | 基础资料 | hrss |
| 483 | hrss_searchweight | 2KS=AXFL0=8G | t_hrss_searchweightconf |  | 排序权重配置 | 基础资料 | hrss |
| 484 | hrss_searchwgentries | 2KZYVUZYXHZZ | t_hrss_searchwgentry |  | 搜索权重等级分录 | 基础资料 | hrss |
| 485 | hrss_searchwtgrade | 2A=DQV1DIJDL | t_hrss_searchwtgrade |  | 权重等级 | 基础资料 | hrss |
| 486 | hrss_syncparam | 44V0UQ40NZ2L | t_hrss_syncparam |  | 数据同步参数 | 基础资料 | hrss |
| 487 | hrss_userlastsearchkey | 40HN1F5YPQ/7 | t_hrss_userlastsearchkey |  | 用户最近搜索关键词 | 基础资料 | hrss |
| 488 | hrss_usersearchlog | 40HGUDD0T33X | t_hrss_usersearchlog |  | 用户搜索记录 | 基础资料 | hrss |

## 基础资料-非主数据类（228）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | brm_decision_tables | 1WX16=PBM7OR | t_brm_decisiontable |  | 决策表 | 基础资料 | brm |
| 2 | brm_decisiontabtmp | 2+2ASU01ZLFD | t_brm_decisiontabtmp |  | 决策表临时表 | 基础资料 | brm |
| 3 | brm_kbase | 1LNU1241KWEO | t_brm_drlkbase |  | Kbase管理 | 基础资料 | brm |
| 4 | brm_policy_his | 1IMWHN3CKZ6Q | t_brm_policy_his |  | 策略配置操作记录 | 基础资料 | brm |
| 5 | brm_policy_tmp | 1XPS47THNEHN | t_brm_policy_tmp |  | 策略临时表 | 基础资料 | brm |
| 6 | brm_rule_his | 1IMWIYU84E4Z | t_brm_rule_his |  | 规则操作记录 | 基础资料 | brm |
| 7 | brm_ruledesign | 1IMW1KY9951/ | t_brm_drlfilter |  | 规则设计 | 基础资料 | brm |
| 8 | brm_rulelist | 1SIH10AN8KEY | t_brm_policy | brm_policy_edit, brm_rulelist | 规则列表 | 基础资料 | brm |
| 9 | brm_ruleruntime | 2/8MP5FFSKEV | t_brm_drlrule |  | 运行时规则 | 基础资料 | brm |
| 10 | brm_scene | 1IMW4OF8QQ/L | t_brm_scene | brm_scene, brm_scene_controlselect | 场景管理 | 基础资料 | brm |
| 11 | brm_scene_his | 1IMWJHHUWM61 | t_brm_scene_his |  | 场景配置操作记录 | 基础资料 | brm |
| 12 | brm_sceneinput | 1IMW5O=NE+T1 | t_brm_sceneinput |  | 输入参数 | 基础资料 | brm |
| 13 | brm_sceneinput_his | 1IMWKGMPCEU2 | t_brm_sceneinput_his |  | 场景输入参数操作记录 | 基础资料 | brm |
| 14 | brm_sceneoutput | 1IMW6I8GIKUQ | t_brm_sceneoutput |  | 输出参数 | 基础资料 | brm |
| 15 | brm_sceneoutput_his | 1IMWL9WWHWVP | t_brm_sceneoutput_his |  | 场景输出参数操作记录 | 基础资料 | brm |
| 16 | brm_sceneparam_tmp | 1XPQ=HUTKK6K | t_brm_sceneparam_tmp |  | 场景参数临时表 | 基础资料 | brm |
| 17 | brm_special_list | 1S5KNZXRZGVG | t_brm_special_list |  | 名单管理 | 基础资料 | brm |
| 18 | brm_target | 1IMW8=N1XBOF | t_brm_target |  | 指标 | 基础资料 | brm |
| 19 | brm_target_his | 1IMWM=C+U0RY | t_brm_target_his |  | 指标操作记录 | 基础资料 | brm |
| 20 | brm_target_tmp | 1XPR==MDGG4H | t_brm_target_tmp |  | 指标临时表 | 基础资料 | brm |
| 21 | brm_targetref | 1IRKAPBRRKH/ | t_brm_targetref |  | 指标引用关系 | 基础资料 | brm |
| 22 | haos_adminorglayerdrop | XYV7CU0STVP |  |  | 基础资料模板 | 基础资料 | haos |
| 23 | haos_adminorglogentry | 53OXEYWZZLJJ | t_haos_adminorglogentry |  | 组织变动生效日志分录-组织 | 基础资料 | haos |
| 24 | haos_adminorgrelateinfo | 2A9CF5ME81LI |  |  | 行政组织关联信息 | 基础资料 | haos |
| 25 | haos_adminorgsort | 5IJM+YLFUJNI |  |  | 行政组织排序 | 基础资料 | haos |
| 26 | haos_chargelogentry | 4V9J029H2LRL | t_haos_chargelogentry |  | 部门负责人日志分录 | 基础资料 | haos |
| 27 | haos_chargepersonlog | 4V9KBY9V/KEI | t_haos_chargepersonlog |  | 部门负责人日志 | 基础资料 | haos |
| 28 | haos_log_chgbill | 53TRK6FBEHB8 | t_homs_orgchgbill |  | 组织变动生效日志-调整申请单 | 基础资料 | haos |
| 29 | haos_log_chgbillentry | 53TRRK5I3K2K | t_homs_orgchgbillentry |  | 组织变动生效日志-调整单分录 | 基础资料 | haos |
| 30 | haos_numberprefix | 5G4O0P/GAR/W |  |  | 编码后缀 | 基础资料 | haos |
| 31 | haos_odcconfigparam | 3FIR637+0509 | t_haos_odcconfigparam |  | 组织后台配置参数 | 基础资料 | haos |
| 32 | haos_orgchgeffectlog | 53BUR+JXPA+J | t_haos_orgchgeffectlog |  | 组织变动生效日志 | 基础资料 | haos |
| 33 | haos_orgdetailtopinfo | 2BVT08V2OLGH |  |  | 组织详情头部信息 | 基础资料 | haos |
| 34 | haos_orgsortconfig | 5J+F3+YJKRTP | t_haos_orgsortconfig |  | 组织排序规则配置 | 基础资料 | haos |
| 35 | haos_orgteamsreltemp | 3+G9GURVGPBD | t_haos_orgteamsreltemp |  | 组织与团队关系临时表 | 基础资料 | haos |
| 36 | haos_positionlogentry | 53SG4PXLDB0L | t_haos_positionlogentry |  | 岗位变动生效日志分录 | 基础资料 | haos |
| 37 | haos_specialrule | 2PNZ4=OZHZMF |  |  | 不占编规则设置 | 基础资料 | haos |
| 38 | haos_staff_choosefield | 4K23BLKHGJPD |  |  | 选择字段 | 基础资料 | haos |
| 39 | haos_staffproject | 2JJEJ=HXWUET | t_haos_staffproject |  | 控编规则设置 | 基础资料 | haos |
| 40 | haos_staffruleconfigentry | 2YLBLOEUWXK2 | t_haos_staffruleentry |  | 编制计划设置分录 | 基础资料 | haos |
| 41 | haos_syncorgfaillog | 1R6SFJWBY9ZK | t_haos_syncorgfaillog |  | 同步平台组织失败日志 | 基础资料 | haos |
| 42 | haos_syncorglog | 1R6V2UDVWWMQ | t_haos_syncorglog |  | 同步平台组织日志 | 基础资料 | haos |
| 43 | haos_synorgchangelog | 3Q4=NFC39RWX | t_haos_synorgchangelog |  | 组织变化同步日志 | 基础资料 | haos |
| 44 | hbjm_bd_orgtpl_dlg | 21VD4MHCS/NI |  |  | 职位带BU模板 | 基础资料 | hbjm |
| 45 | hbjm_hisbutimeseqtpl | 21VC3A4THFOS |  |  | 职位带BU带历史模板 | 基础资料 | hbjm |
| 46 | hbp_activitybasetpl | 4K13W7KPIVDY |  |  | 活动模板（基础资料） | 基础资料 | hbp |
| 47 | hbp_bd_grouporgtpl_all | 2+R8ZH7NO=6T |  |  | HR分组基础资料带组织全页面模板 | 基础资料 | hbp |
| 48 | hbp_bd_grouporgtpl_dlg | 2+U/V+MDCFT2 |  |  | HR分组基础资料带组织对话框模板 | 基础资料 | hbp |
| 49 | hbp_bd_grouptpl_all | 2+R939/K+92X |  |  | HR分组基础资料全页面模板 | 基础资料 | hbp |
| 50 | hbp_bd_grouptpl_dlg | 2+U/N3HSBIKR |  |  | HR分组基础资料对话框模板 | 基础资料 | hbp |
| 51 | hbp_bd_orgrp_tpl | 2A=C=/GL8Y32 |  |  | HR基础资料带组织带侧边栏模板 | 基础资料 | hbp |
| 52 | hbp_bd_orgtpl_all | 2+QPVWEP=LYN |  |  | HR带组织基础资料全页面模板 | 基础资料 | hbp |
| 53 | hbp_bd_orgtpl_dlg | 1QU7Y5XQVT/Z |  |  | HR基础资料带组织对话框模板 | 基础资料 | hbp |
| 54 | hbp_bd_originaldlgtpl | 1A3N/55RMKON |  |  | HR原生基础资料对话框模板 | 基础资料 | hbp |
| 55 | hbp_bd_originalmintpl | 0NKB12K4VH2O |  |  | HR原生基础资料最小模板 | 基础资料 | hbp |
| 56 | hbp_bd_originaltpl | 1A3I8S4ADNCO |  |  | HR原生基础资料模板 | 基础资料 | hbp |
| 57 | hbp_bd_timelinemintpl | 4P3X7D6IF4T1 |  |  | HR时间轴最小模板 | 基础资料 | hbp |
| 58 | hbp_bd_tpl_all | 2+QE4JA9QV27 |  |  | HR基础资料全页面模板 | 基础资料 | hbp |
| 59 | hbp_bd_tpl_dlg | 1QRR3L4TKSB5 |  |  | HR基础资料对话框模板 | 基础资料 | hbp |
| 60 | hbp_bd_treeorgtpl_all | 2+R8=BY/RB68 |  |  | HR树形基础资料带组织全页面模板 | 基础资料 | hbp |
| 61 | hbp_bd_treeorgtpl_dlg | 2+U+X89CA7Q= |  |  | HR树形基础资料带组织对话框模板 | 基础资料 | hbp |
| 62 | hbp_bd_treetpl_all | 2+R7VHVKOQHP |  |  | HR树形基础资料全页面模板 | 基础资料 | hbp |
| 63 | hbp_bd_treetpl_dlg | 2+U/=USJI=3J |  |  | HR树形基础资料对话框模板 | 基础资料 | hbp |
| 64 | hbp_bd_virfieldtpl | 4MFT3ZK4UASV |  |  | HR基础资料虚字段模板 | 基础资料 | hbp |
| 65 | hbp_coordappcfgtpl | 4ZGWNO3PYLTQ |  |  | 协作应用配置模板 | 基础资料 | hbp |
| 66 | hbp_coordchgrecord | 4YLDR1ZO237C |  |  | 事务变动记录模板 | 基础资料 | hbp |
| 67 | hbp_coordruleparam | 4XRR113N5BVD |  |  | 协作规则参数模板 | 基础资料 | hbp |
| 68 | hbp_coordrulesch | 4YDMOBCDAXM4 |  |  | 协作规则方案模板 | 基础资料 | hbp |
| 69 | hbp_coordstrategy | 4YDND9JZV+J2 |  |  | 协作处理策略模板 | 基础资料 | hbp |
| 70 | hbp_datagradetpl | 23IE46R0JEU1 |  |  | 数据分级基础资料模板 | 基础资料 | hbp |
| 71 | hbp_entityrelationtpl | 2VJ5AGNDU=4+ |  |  | 实体关联关系模板 | 基础资料 | hbp |
| 72 | hbp_formulatpl | 235C14WB9FPB |  |  | 计算公式模板 | 基础资料 | hbp |
| 73 | hbp_hisbugrptimeseqtpl | 2/TYGWXZFXTV |  |  | HR带BU带分组基础资料全页面历史模板 | 基础资料 | hbp |
| 74 | hbp_hisbutimeseqtpl | 2/TYVXZRP5/F |  |  | HR带BU基础资料全页面历史模板 | 基础资料 | hbp |
| 75 | hbp_hisgrptimeseqtpl | 2/TYD9B+8LRE |  |  | HR带分组基础资料全页面历史模板 | 基础资料 | hbp |
| 76 | hbp_histimeseqtpl | 2/TYOWOSM0TE |  |  | HR基础资料全页面历史模板 | 基础资料 | hbp |
| 77 | hbp_hrlogbizparam | 4JWAF0/FCYYH | t_hbss_hrlogbizparam |  | 日志业务场景参数 | 基础资料 | hbp |
| 78 | hbp_hrlogbiztype | 4GYI1DDH+MI4 | t_hbss_hrlogbiztype |  | 日志业务场景类型 | 基础资料 | hbp |
| 79 | hbp_joinentitytpl | 2VJ5HIFJRGG0 |  |  | 关联实体模板 | 基础资料 | hbp |
| 80 | hbp_orimintimeseqtpl | 2/TXG194/JP1 |  |  | HR原生基础资料最小历史模板 | 基础资料 | hbp |
| 81 | hbp_queryfieldtpl | 2VJ5M0K=/XIY |  |  | 查询字段模板 | 基础资料 | hbp |
| 82 | hbp_rptcomsorttpl | 36J=9FCLPYRX |  |  | 报表字段通用排序模板 | 基础资料 | hbp |
| 83 | hbp_rptcustomsorttpl | 36JAS6D/1BYZ |  |  | 报表字段自定义排序模板 | 基础资料 | hbp |
| 84 | hbpm_bd_orgtpl_dlg | 2LZ6FKS+IM5S |  |  | 岗位带BU模板 | 基础资料 | hbpm |
| 85 | hbpm_bd_tpl_dlg | 21VCDBV+1GF0 |  |  | 岗位基础资料通用模板 | 基础资料 | hbpm |
| 86 | hbpm_createbytplorgrange | 3NMO9OM768QP |  |  | 参数模板行政组织范围 | 基础资料 | hbpm |
| 87 | hbpm_positionsort | 5J/RFQA4BLLJ |  |  | 岗位排序 | 基础资料 | hbpm |
| 88 | hbpm_possortconfig | 5J/JXI=C9GCS | t_hbpm_possortconfig |  | 岗位排序规则配置 | 基础资料 | hbpm |
| 89 | hbpm_reviselog | 34ERF8TVUT9X | t_hbpm_posreviselog |  | 岗位修订日志 | 基础资料 | hbpm |
| 90 | hbpm_syncpositionlog | 42TRUJMICHW/ | t_hbpm_syncpositionlog |  | 同步岗位平台日志 | 基础资料 | hbpm |
| 91 | hbss_actiontype | 0M7QOQ04EEN/ | t_hbss_actiontype |  | 业务操作类型 | 基础资料 | hbss |
| 92 | hbss_addresstype | 17WMJ8T04ECN | t_hbss_addresstype |  | 地址类型 | 基础资料 | hbss |
| 93 | hbss_appbusinesstype | 0RUF0XSVJUUP | t_hbss_appbusinesstype |  | 应用与业务类型关系配置 | 基础资料 | hbss |
| 94 | hbss_appointtype | 38IN4IHFFLBE | t_hbss_appointtype |  | 任免类型 | 基础资料 | hbss |
| 95 | hbss_appointtypegroup | 38IN3648OU2A | t_hbss_appointtypegroup |  | 任免类型分组 | 基础资料 | hbss |
| 96 | hbss_apptreasongroup | 38IN1SZ6WO0F | t_hbss_apptreasongroup |  | 任免原因 | 基础资料 | hbss |
| 97 | hbss_bankpurpose | /SPJ1IJU1SRI | t_hbss_bankpurpose |  | 银行卡用途 | 基础资料 | hbss |
| 98 | hbss_bloodtype | 2+MUV03LE+0H | t_hbss_bloodtype |  | 血型 | 基础资料 | hbss |
| 99 | hbss_bussinessfield | 01VEIHJ69CWJ | t_hbss_bussinessfield |  | 业务类型 | 基础资料 | hbss |
| 100 | hbss_cadrecategory | 38IN0JMSHGOL | t_hbss_cadrecategory |  | 干部类别 | 基础资料 | hbss |
| 101 | hbss_capacityrankscheme | 2FQ1Q3HWR4=M | t_hbss_caprankscheme |  | 能力等级方案 | 基础资料 | hbss |
| 102 | hbss_capaitementry | 2OFIID0RMZUO | t_hbss_capitemrankentry |  | 能力素质项等级分录（只读） | 基础资料 | hbss |
| 103 | hbss_caprankinfoentry | 2KJN3VEZR7SG | t_hbss_caprankinfoentry |  | 能力等级分录 | 基础资料 | hbss |
| 104 | hbss_category | /U4Y2AU+34KC | t_hbss_category |  | 户口性质 | 基础资料 | hbss |
| 105 | hbss_constellation | /8I8=8+VX1WR | t_hbss_constellation |  | 星座 | 基础资料 | hbss |
| 106 | hbss_contracttypecat | 29W4Q77JSG09 | t_hbss_contracttypecat |  | 合同类型类别 | 基础资料 | hbss |
| 107 | hbss_contracttypes | 25L2WU/HHPLT | t_hbss_contracttypes |  | 合同类型 | 基础资料 | hbss |
| 108 | hbss_credentialstype | 1A4DU+AJMXVZ | t_hbss_credentialstype |  | 证件类型 | 基础资料 | hbss |
| 109 | hbss_cycletype | 28P7EYL2SDP6 | t_hbss_pcycletype |  | 周期类型 | 基础资料 | hbss |
| 110 | hbss_degree | /8AY1BZFH/JM | t_hbss_degree |  | 学位 | 基础资料 | hbss |
| 111 | hbss_depcytype | 0AJ29B/1I5AA | t_hbss_depcytype |  | 属地员工类别 | 基础资料 | hbss |
| 112 | hbss_diploma | ZPGV79XZX2A | t_hbss_diploma |  | 学历 | 基础资料 | hbss |
| 113 | hbss_diplomatype | ZPHA8LLV457 | t_hbss_diplomatype |  | 教育形式 | 基础资料 | hbss |
| 114 | hbss_disptype | 52U4GX58666V | t_hbss_disptype |  | 外派类型 | 基础资料 | hbss |
| 115 | hbss_educerttype | 25R6PXWG4YS9 | t_hbss_educerttype |  | 教育证件类型 | 基础资料 | hbss |
| 116 | hbss_emergcontactype | 1O9A82F2QV6I | t_hbss_emergcontactype |  | 紧急联系人类型 | 基础资料 | hbss |
| 117 | hbss_entityreforg | 1ZG2NBAU2VCY | t_hbss_entityreforg |  | HR组织与实体映射关系 | 基础资料 | hbss |
| 118 | hbss_entitytype | 12IP4K1M50B3 | t_hbss_entitytype |  | 实体类型 | 基础资料 | hbss |
| 119 | hbss_familiarity | /8AYGYQWGWDC | t_hbss_familiarity |  | 语言熟悉程度 | 基础资料 | hbss |
| 120 | hbss_familymemberrel | 16E3HA+WS9P1 | t_hbss_familymemberrel |  | 家庭成员关系 | 基础资料 | hbss |
| 121 | hbss_flok | /94E4I5VDAQM | t_hbss_flok |  | 民族 | 基础资料 | hbss |
| 122 | hbss_healthstatus | 1A4DD9F2/A4E | t_hbss_healthstatus |  | 健康状况 | 基础资料 | hbss |
| 123 | hbss_helpbuildtype | 3N0292R=9+HM | t_hbss_helpbuildtype |  | 援建类型 | 基础资料 | hbss |
| 124 | hbss_historyeventrel | 0P5GGSHUF3HV | t_isv_xxx | hbss_historyeventrel, haos_personnellist | 依赖事务关系表 | 基础资料 | hbss |
| 125 | hbss_hrbucafunc | 0RTSW5GM60HI | t_hbss_hrbucafunc |  | HR职能类型 | 基础资料 | hbss |
| 126 | hbss_industrytype | XYSBUT4=KKH | t_hbss_industrytype |  | 行业类别 | 基础资料 | hbss |
| 127 | hbss_laborrelstatus | /V4J=ID7S76O | t_hbss_laborrelstatus |  | 用工关系状态 | 基础资料 | hbss |
| 128 | hbss_laborreltype | /V4IWGFZ0N51 | t_hbss_laborreltype |  | 用工关系类型 | 基础资料 | hbss |
| 129 | hbss_laborreltypecls | /V464GMN2REB | t_hbss_laborreltypecls |  | 用工关系类型分类 | 基础资料 | hbss |
| 130 | hbss_languagecert | 16E2LN3NSLIY | t_hbss_languagecert |  | 语言证书 | 基础资料 | hbss |
| 131 | hbss_languagetype | 16NKGVQS3QG+ | t_hbss_languagetype |  | 语言种类 | 基础资料 | hbss |
| 132 | hbss_loginscene | 2=3HA=XTIQE5 | t_hbss_loginscene |  | 登录场景 | 基础资料 | hbss |
| 133 | hbss_logview | 2+5XGX=7L0KB | t_hbss_log | hbss_history_logview, hbss_logview | 基础资料日志 | 基础资料 | hbss |
| 134 | hbss_marriagestatus | 1A4C340SCXT2 | t_hbss_marriagestatus |  | 婚姻状况 | 基础资料 | hbss |
| 135 | hbss_nationality | /IU3WSPYU4BP | t_hbss_nationality |  | 国籍 | 基础资料 | hbss |
| 136 | hbss_ocpqual | 1A4FSSW5G634 | t_hbss_ocpqual |  | 职业资格 | 基础资料 | hbss |
| 137 | hbss_ocpquallevel | 1A4C=NX5ZEJY | t_hbss_ocpquallevel |  | 资格等级 | 基础资料 | hbss |
| 138 | hbss_onboardsource | 1A4BMV59MY98 | t_hbss_onboardsource |  | 入职来源 | 基础资料 | hbss |
| 139 | hbss_onboardtype | /K2E3R/GUT03 | t_hbss_onboardtype |  | 入职类型 | 基础资料 | hbss |
| 140 | hbss_onbrdbreakupreason | 2WLZSSDDKG7L | t_hbss_breakupreason |  | 终止入职原因 | 基础资料 | hbss |
| 141 | hbss_onbrdbreakuptype | 2WLZVERM/IJG | t_hbss_breakuptype |  | 终止入职类型 | 基础资料 | hbss |
| 142 | hbss_operationqual | 1A4FY+13CGHT | t_hbss_operationqual |  | 执业资格 | 基础资料 | hbss |
| 143 | hbss_parameterconfig | 1ZKD40O+IDII | t_hbss_parameterconfig |  | HR基础资料参数配置 | 基础资料 | hbss |
| 144 | hbss_paramwhitelist | 3NMM=N=IZDTN | t_hbss_paramwhitelist |  | HR基础资料参数白名单 | 基础资料 | hbss |
| 145 | hbss_party | 1A4FA82QHG4B | t_hbss_party |  | 党派 | 基础资料 | hbss |
| 146 | hbss_patentscategory | 1PI1Y66IUFOO | t_hbss_patentscategory |  | 专利类别 | 基础资料 | hbss |
| 147 | hbss_patentstatus | 1OLO1EU1BBU4 | t_hbss_patentstatus |  | 专利状态 | 基础资料 | hbss |
| 148 | hbss_payrollacrelation | 26D7E4P7HF6G | t_hbss_payrollacrelation |  | 社会关系类型 | 基础资料 | hbss |
| 149 | hbss_perflevel | 2QYO0=Q64V++ | t_hbss_perflevel |  | 等级规则 | 基础资料 | hbss |
| 150 | hbss_perflevelhr | 43+3HOYQZ/VP | t_hbss_perflevelconfig |  | HR绩效等级 | 基础资料 | hbss |
| 151 | hbss_perflevelrule | 2SF0JLJGB4Q1 | t_hbss_perflevelrule |  | 标尺等级引用 | 基础资料 | hbss |
| 152 | hbss_perflevelrulermap | 2SFERC65MB34 | t_hbss_perflevelrulermap |  | 标尺等级映射 | 基础资料 | hbss |
| 153 | hbss_politicalstatus | 1A4G6XSNZZ9= | t_hbss_politicalstatus |  | 政治面貌 | 基础资料 | hbss |
| 154 | hbss_postcategory | 0LAODKLN4R25 | t_hbss_postcategory |  | 任职类型分类 | 基础资料 | hbss |
| 155 | hbss_poststate | 0LA7NN79+/VQ | t_hbss_poststate |  | 任职状态 | 基础资料 | hbss |
| 156 | hbss_poststatecls | 21R+5U1GD=T7 | t_hbss_poststatecls |  | 任职状态分类 | 基础资料 | hbss |
| 157 | hbss_postype | 1A4GBQSA7MU/ | t_hbss_postype |  | 任职类型 | 基础资料 | hbss |
| 158 | hbss_privacyusertype | 5LRLGMIWYDTZ | t_hrcs_privacyusertype | hbss_privacyusertype, hrcs_privacyusertype | 外部用户类型 | 基础资料 | hbss |
| 159 | hbss_procreatmode | 21YGJO8++Z9Q | t_hbss_procreatmode |  | 生育方式 | 基础资料 | hbss |
| 160 | hbss_projecttype | 1A4ANXEZ/+H+ | t_hbss_projecttype |  | 项目类型 | 基础资料 | hbss |
| 161 | hbss_protitlelevel | 1A4E70RS=IPG | t_hbss_protitlelevel |  | 职称级别 | 基础资料 | hbss |
| 162 | hbss_protitletype | /YSHSX1OTCT9 | t_hbss_protitletype |  | 职称类别 | 基础资料 | hbss |
| 163 | hbss_recruscene | 2+JUZQTAVHCN | t_hbss_recruscene |  | 招聘场景 | 基础资料 | hbss |
| 164 | hbss_recrutyp | 2/5TXHMRWW9+ | t_hbss_recrutyp |  | 招聘类型 | 基础资料 | hbss |
| 165 | hbss_religion | 1A4EECW7T97R | t_hbss_religion |  | 宗教信仰 | 基础资料 | hbss |
| 166 | hbss_resacqmthd | 2950THQJRVHU | t_hbss_resacqmthd |  | 招聘渠道类型 | 基础资料 | hbss |
| 167 | hbss_rewpnmlevel | 1A4GOSZHMN99 | t_hbss_rewpnmlevel |  | 奖惩级别 | 基础资料 | hbss |
| 168 | hbss_rewpnmtype | 1A4D7APL/NEB | t_hbss_rewpnmtype |  | 奖惩类别 | 基础资料 | hbss |
| 169 | hbss_rolesourcetype | 0SJNX84HCU9H | t_hbss_rolesourcetype |  | 来源类型 | 基础资料 | hbss |
| 170 | hbss_rotationtype | 3N29K==8FQZP | t_hbss_rotationtype |  | 轮岗类型 | 基础资料 | hbss |
| 171 | hbss_rulegrade | 2R6+HMDE86T8 | t_hbss_rulegrade |  | 等级标尺 | 基础资料 | hbss |
| 172 | hbss_rulegradehr | 432CJ8L84F1C | t_hbss_rulegradeconfig |  | HR标尺等级 | 基础资料 | hbss |
| 173 | hbss_schooltype | 1A4AYK+B2AH1 | t_hbss_schooltype |  | 院校特性 | 基础资料 | hbss |
| 174 | hbss_sex | 1986YP6EMXR3 | t_hbss_sex |  | 性别 | 基础资料 | hbss |
| 175 | hbss_supplierlevel | 0=6T4CC10XGV | t_hbss_supplierlevel |  | 供应商等级 | 基础资料 | hbss |
| 176 | hbss_supsertype | 0=6T042N0L=8 | t_hbss_supsertype |  | 供应商服务类型 | 基础资料 | hbss |
| 177 | hbss_timelimittype | 1A4FFDLRELHI | t_hbss_timelimittype |  | 合同期限类型 | 基础资料 | hbss |
| 178 | hbss_trainmode | /DO=6V7WB17T | t_hbss_trainmode |  | 培训方式 | 基础资料 | hbss |
| 179 | hbss_traintype | /DO=1F4=U40L | t_hbss_traintype |  | 培训类别 | 基础资料 | hbss |
| 180 | hbss_ttakepostype | 3N2BLF158Y6+ | t_hbss_ttakepostype |  | 挂职类型 | 基础资料 | hbss |
| 181 | hbss_verifycodelog | 2=CXR=+04YZ0 | t_hbss_verifycodelog |  | 验证码日志 | 基础资料 | hbss |
| 182 | hbss_zodiac | /K+D4H+6ZH2F | t_hbss_zodiac |  | 生肖 | 基础资料 | hbss |
| 183 | hcf_candidaterelalltpl | 5+C4=8BF2VT+ |  |  | 拟入职人员附表全页面模板 | 基础资料 | hcf |
| 184 | hcf_candidatereltpl | 15VVWN7=X+QW |  |  | 拟入职人员附表对话框模板 | 基础资料 | hcf |
| 185 | hrcs_activitynodelog | 2XWL=YLADJDS | t_hrcs_activitynodelog |  | 活动节点日志 | 基础资料 | hrcs |
| 186 | hrcs_chooseapifield | 439NX0BY0FUB |  |  | 选择字段 | 基础资料 | hrcs |
| 187 | hrcs_coordruleschlog | 5+XD67912=9Q | t_hrcs_coordruleschlog |  | 协作规则方案日志 | 基础资料 | hrcs |
| 188 | hrcs_coordstrategylog | 51YE3FK9CS21 | t_hrcs_coordstrategylog |  | 协作处理策略日志表 | 基础资料 | hrcs |
| 189 | hrcs_earlywarn_log | 3V2WFIK=XLB4 | t_hrcs_earlywarn_log |  | 预警执行日志(废弃) | 基础资料 | hrcs |
| 190 | hrcs_empstrategy_tpl | 15HQBZWI50DJ |  |  | 员工管理关系策略模板 | 基础资料 | hrcs |
| 191 | hrcs_empstrentry | 15HM=32TJ06Z | t_hbss_empstrentry |  | 员工管理关系策略分录 | 基础资料 | hrcs |
| 192 | hrcs_esignparmcfg | 3EDA2WFBTU+1 | t_hrcs_esignparmcfg |  | 电子签参数配置 | 基础资料 | hrcs |
| 193 | hrcs_keymappingentry | 2BZH79CLNX3D | t_hrcs_keymappingentry |  | 电子合同字段映射分录实体 | 基础资料 | hrcs |
| 194 | hrcs_managestrategy | 15HOV+M2Y/11 |  |  | 管理关系策略模板 | 基础资料 | hrcs |
| 195 | hrcs_managestrategyentry | 15HOVDIIF/O9 |  |  | 管理关系策略分录模板 | 基础资料 | hrcs |
| 196 | hrcs_needdeletetasklog | 4BFKVT1VB0L0 | t_hrcs_needdeletetasklog |  | 清除定时任务日志 | 基础资料 | hrcs |
| 197 | hrcs_orgstrategy_tpl | 15HQ9/QY8WRF |  |  | 组织管理关系策略模板 | 基础资料 | hrcs |
| 198 | hrcs_orgstrentry | 15HN3ZP0GX0L | t_hbss_orgstrentry |  | 组织管理关系策略分录 | 基础资料 | hrcs |
| 199 | hrcs_permlog | 4/S7EHFPX+IS | t_hrcs_permlog |  | HR权限日志 | 基础资料 | hrcs |
| 200 | hrcs_permlogarchive | 408B=X19N8/U | t_hrcs_permlogarchive |  | HR权限日志归档 | 基础资料 | hrcs |
| 201 | hrcs_permlogtype | 4/QAXUDSY/Z1 | t_hrcs_permlogtype |  | HR权限日志类型 | 基础资料 | hrcs |
| 202 | hrcs_projempstrentry | 2J2UO6X8LS5B | t_hrcs_projempstrentry |  | 项目团队员工管理关系策略分录 | 基础资料 | hrcs |
| 203 | hrcs_projorgstrentry | 2J2US57M6T0G | t_hrcs_projorgstrentry |  | 项目团队组织管理关系策略分录 | 基础资料 | hrcs |
| 204 | hrcs_rolebdruleentry | 16D6OY+N+COW | t_hrcs_rolebdruleentry |  | 角色基础资料数据范围分录 | 基础资料 | hrcs |
| 205 | hrcs_roledataruleentry | 16D6LLA+5JNG | t_hrcs_roledataruleentry |  | 角色数据规则分录 | 基础资料 | hrcs |
| 206 | hrcs_ruledesignconfig | 4Z6H/VGEC2PH |  |  | 协作规则配置页面 | 基础资料 | hrcs |
| 207 | hrcs_userbdruleentry | 16D6VJN5EW5C | t_hrcs_userbdruleentry |  | 用户基础资料数据范围分录 | 基础资料 | hrcs |
| 208 | hrcs_userdataruleentry | 16D6TX3EDYOU | t_hrcs_userdataruleentry |  | 用户数据规则分录 | 基础资料 | hrcs |
| 209 | hrpi_assigndialogtpl | 5+IDZ0B+M0K= |  |  | 组织分配附表对话框模板 | 基础资料 | hrpi |
| 210 | hrpi_assigntimelinetpl | 4S264WARXWSY |  |  | 组织分配附表时间轴对话框模板 | 基础资料 | hrpi |
| 211 | hrpi_assigntpl | 4S1QMD18NBVI |  |  | 组织分配附表全页面模板 | 基础资料 | hrpi |
| 212 | hrpi_blacklistlog | 3CRZ+FOOTOFW | t_hrpi_blacklistlog |  | 日志详情 | 基础资料 | hrpi |
| 213 | hrpi_devconfig | 3LYV/RHB7S69 | t_hrpi_devconfig |  | 员工信息中心开发参数配置 | 基础资料 | hrpi |
| 214 | hrpi_employeedialogtpl | 5+IDXDQ48CFP |  |  | 员工附表对话框模板 | 基础资料 | hrpi |
| 215 | hrpi_employeetimelinetpl | 4S261NBA2/VN |  |  | 员工附表时间轴对话框模板 | 基础资料 | hrpi |
| 216 | hrpi_employeetpl | 4S1QHXFUTL+B |  |  | 员工附表全页面模板 | 基础资料 | hrpi |
| 217 | hrpi_syncintelog | 4L7RH9H1BM3Q | t_hrpi_syncintelog |  | 人员国际化信息同步平台日志表 | 基础资料 | hrpi |
| 218 | hrpi_synclog | 2B26/SMJ243V | t_hrpi_synclog |  | 人员同步系统用户日志 | 基础资料 | hrpi |
| 219 | hrptmc_datacomplog | 4A/ECPHT/3D2 | t_hrptmc_datacomplog |  | 数据对比日志 | 基础资料 | hrptmc |
| 220 | hrptmc_dataextractlog | 43LKA8Y2K=UV | t_hrptmc_dataextractlog |  | 数据抽取日志 | 基础资料 | hrptmc |
| 221 | hrptmc_datastoretemp | 4/W5AB981KHU |  |  | 数据落地元数据模板 | 基础资料 | hrptmc |
| 222 | hrptmc_esidxmodlog | 4AJC12=RZJRW | t_hrptmc_esidxmodlog |  | ES索引修改日志 | 基础资料 | hrptmc |
| 223 | hrptmc_esrefmodlog | 4AK7RSWKQFNS | t_hrptmc_esrefmodlog |  | ES引用修改日志 | 基础资料 | hrptmc |
| 224 | hrptmc_paramconfig | 4/4X630Q82V5 | t_hrptmc_rptqueryconfig | hrptmc_paramconfig, hrptmc_rptqueryconfig | 报表参数配置 | 基础资料 | hrptmc |
| 225 | hrptmc_report_bdtpl | 4MFTGM2NOIB5 |  |  | 报表基础资料模板 | 基础资料 | hrptmc |
| 226 | hrptmc_rptcallog | 4NPM5QLIMUQM | t_hrptmc_rptcallog |  | HR报表计算日志 | 基础资料 | hrptmc |
| 227 | hrptmc_rpttrackcnf | 3=/Q0JD3N29Q | t_hrptmc_rpttrcnf |  | 报表日志配置 | 基础资料 | hrptmc |
| 228 | hrss_syncerrorlog | 42=4/8BOY470 | t_hrss_syncerrorlog |  | 同步失败日志 | 基础资料 | hrss |

## 单据（31）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | brm_log | 526X3MUE/9PI | t_brm_exelog |  | 调用日志 | 日志单据 | brm |
| 2 | hbp_activitybilltpl | 38I7EO93LBZW |  |  | 活动模板（单据） | 单据 | hbp |
| 3 | hbp_coordverifbill | 4XBLJ6WSZLZJ |  |  | 协作核定单模板 | 单据 | hbp |
| 4 | hbp_empcoordverifbill | 4XRT37JZRVKS |  |  | 员工变动协作核定单模板 | 单据 | hbp |
| 5 | hbp_hismodellog | 4US3XK7X85VJ | t_hbp_hismodellog |  | HR历史模型日志 | 日志单据 | hbp |
| 6 | hbp_hrbillorgtpl | 0H+/G119RC+2 |  |  | HR带主组织单据基模板 | 单据 | hbp |
| 7 | hbp_hrbilltpl | 0LI5VZUESHUD |  |  | HR无组织单据基模板 | 单据 | hbp |
| 8 | hbp_hrlogtemplate | 2LZ93871=2AY |  |  | HR日志模板 | 日志单据 | hbp |
| 9 | hbp_ssctaskhistpl | 47ITBG15VOWO |  |  | 已完成共享任务模板 | 单据 | hbp |
| 10 | hbp_ssctasktpl | 47IEG6E1M0HI |  |  | 处理中共享任务模板 | 单据 | hbp |
| 11 | hbp_timelinelog | 4RF70H=M=M+3 | t_hbp_timelinelog |  | 时间轴模型日志 | 日志单据 | hbp |
| 12 | hbp_ut_hrbillorgtpl | 5DNLVQLWCXJ2 |  |  | HR带主组织单据单测 | 单据 | hbp |
| 13 | hrcs_actassignrec | 2+TFQWL4P0MU | t_hrcs_actoprec |  | 任务操作记录 | 单据 | hrcs |
| 14 | hrcs_activityenablerec | 22NLPNALP07P | t_hrcs_activityenablerec |  | 活动启动记录 | 单据 | hrcs |
| 15 | hrcs_activityerrparam | 2OF91Q4YP7H4 | t_hrcs_activityerrparam |  | 异常活动调用记录 | 单据 | hrcs |
| 16 | hrcs_activityexception | 2M7O85T8I3J6 | t_hrcs_activityexception |  | 异常监控 | 单据 | hrcs |
| 17 | hrcs_esigncallbackflow | 3HNA5+R1Z/O6 | t_hrcs_esigncallbackflow |  | 电子签回调流水信息 | 单据 | hrcs |
| 18 | hrcs_labelresultshow | 2Z/SARTT5B19 | t_hrcs_labelresult |  | 打标结果 | 单据 | hrcs |
| 19 | hrcs_labeltasklog | 2YS/YV/LN199 | t_hrcs_labeltasklog |  | 标签打标任务日志 | 日志单据 | hrcs |
| 20 | hrcs_permapplybill | 3TP3H2ITC7SJ | t_hrcs_permapplybill |  | 权限申请单 | 单据 | hrcs |
| 21 | hrcs_signfile | 3E34T3Q71VFI | t_hrcs_signfile |  | 电子签署文件 | 单据 | hrcs |
| 22 | hrcs_signflow | 3E3C1BR7NM38 | t_hrcs_signflow |  | 电子签署流程 | 单据 | hrcs |
| 23 | hrcs_ssctask | 4APA++5NYARO | t_hrcs_ssctask |  | （废弃）中台处理中共享任务 | 单据 | hrcs |
| 24 | hrcs_ssctaskhis | 4APA04QZAAR7 | t_hrcs_ssctaskhis |  | （废弃）中台已完成共享任务 | 单据 | hrcs |
| 25 | hrcs_warnexeclog | 5K4MYV8VH63/ | t_hrcs_warnexeclog |  | 预警执行日志 | 日志单据 | hrcs |
| 26 | hrpi_operatelog | 4Z/EIN=LITIM | t_hrpi_operatelog |  | 员工信息日志 | 日志单据 | hrpi |
| 27 | hrpi_redundantlog | 5A6LDPN2N38O | t_hrpi_redundantlog |  | 冗余字段更新日志 | 日志单据 | hrpi |
| 28 | hrpi_ssctask | 49=X1S+ETZI3 | t_hrpi_ssctask |  | HR库处理中共享任务 | 单据 | hrpi |
| 29 | hrpi_ssctaskhis | 49=WPL72UNV4 | t_hrpi_ssctaskhis |  | HR库已完成共享任务模板 | 单据 | hrpi |
| 30 | hrptmc_exportlog | 4EN65V8B5+VE |  |  | 导出结果 | 单据 | hrptmc |
| 31 | hrptmc_hrrptlog | 3=37JKRC0X+/ | t_hrptmc_hrrptlog |  | 报表日志 | 日志单据 | hrptmc |

## 动态表单（387）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | bree_apphome | 1IMTM5DO2FRZ |  |  | 业务规则计算_应用首页 | 动态表单 | bree |
| 2 | brm_apphome | 1IMTC5Z1G8=K |  |  | 业务规则管理_应用首页 | 动态表单 | brm |
| 3 | brm_decision_param | 1X4T6ZA+/E2+ |  |  | 参数配置 | 动态表单 | brm |
| 4 | brm_decisiontab_import | 1Y8JEF60X/6F |  |  | 决策表导入向导 | 动态表单 | brm |
| 5 | brm_express_view | 1JTNXVKX=RKI |  |  | 逻辑表达式预览 | 动态表单 | brm |
| 6 | brm_import_correct | 1XF8A21789N9 |  |  | 导入确认 | 动态表单 | brm |
| 7 | brm_importing | 1WJH8PIL3E6/ |  |  | 策略导入进度 | 动态表单 | brm |
| 8 | brm_importsuccess | 1XZR6W5SZFRZ |  |  | 导入成功 | 动态表单 | brm |
| 9 | brm_policy_import | 1WIL6ZHTBJBM |  |  | 策略导入向导 | 动态表单 | brm |
| 10 | brm_ruletest | 1IMW3PXBWFZ8 |  |  | 规则测试 | 动态表单 | brm |
| 11 | brm_sceneparamprop | 3=6P73OGM/MN |  |  | 场景参数属性 | 动态表单 | brm |
| 12 | brm_targetf7 | 1IMW9BTK6Z3U |  |  | 指标动态新增F7 | 动态表单 | brm |
| 13 | brm_wfparticipant | 1XPQS3P4BREK |  |  | 工作流参与人扩展 | 动态表单 | brm |
| 14 | haos_addfromothroletpl | 5EM9G4E31F53 |  |  | 角色列表 | 动态表单 | haos |
| 15 | haos_adminorgedit | 3H72CLFC8NRZ |  |  | 编辑行政组织 | 动态表单 | haos |
| 16 | haos_adminorghislist | 4U70=N1IR9ZI |  |  | 行政组织历史树列表 | 动态表单 | haos |
| 17 | haos_adminorglist | 2BEN1QKHYHE+ |  |  | 行政组织树列表 | 动态表单 | haos |
| 18 | haos_adminorgtablist | 5327GGZCD5HU |  |  | 带页签行政组织树列表 | 动态表单 | haos |
| 19 | haos_adminorgtreelistf7 | 554Q2C9T+KL8 |  |  | HR组织树 | 动态表单 | haos |
| 20 | haos_appgridhome | /L57NHJZGX2L |  |  | 行政组织首页 | 动态表单 | haos |
| 21 | haos_apphome | W11RC0N+01/ |  |  | 行政组织管理_应用首页 | 动态表单 | haos |
| 22 | haos_chargepersoncard | 4V9KG9G6SCWJ |  |  | 部门负责人卡片 | 动态表单 | haos |
| 23 | haos_chargepersoncardmore | 4V9KT76IH+2V |  |  | 部门负责人卡片-更多 | 动态表单 | haos |
| 24 | haos_emptyorgteam | 2NTBPYHC7DYZ |  |  | 组织空f7（禁使用） | 动态表单 | haos |
| 25 | haos_futuretipsshow | 536BYPF0DV3T |  |  | 未来生效指引提示 | 动态表单 | haos |
| 26 | haos_hismainpeople | 2AQT7LTBSHGY |  |  | 历任管理团队 | 动态表单 | haos |
| 27 | haos_hismainpeoplecard | 2AMY2+5W2L90 |  |  | 历任管理团队时间轴 | 动态表单 | haos |
| 28 | haos_leaderlist | 2=H5N1IMDB3C |  |  | 部门负责人 | 动态表单 | haos |
| 29 | haos_list | 2BWKP3CEJVXS |  |  | 标准列表 | 动态表单 | haos |
| 30 | haos_multicyclecopy | 2KTT29NQBAMY |  |  | 复制编制 | 动态表单 | haos |
| 31 | haos_orgbatchtreelistf7 | 2DAGF0HZ+046 |  |  | 批量单组织F7-树 | 动态表单 | haos |
| 32 | haos_orgfastdisable | 52/SL4IQSGP5 |  |  | 停用组织 | 动态表单 | haos |
| 33 | haos_orgfastenable | 4T8ALC5OG2SR |  |  | 启用组织 | 动态表单 | haos |
| 34 | haos_orgfullnameexample | 5J6JB+BOT+TP |  |  | 组织全称生成示例 | 动态表单 | haos |
| 35 | haos_orgimportstart | 5J1XZJXP9R5= |  |  | 组织导入引导页面 | 动态表单 | haos |
| 36 | haos_orgstructlist | 3CZDJ4XDNJS1 |  |  | 行政组织架构树列表 | 动态表单 | haos |
| 37 | haos_orgstructlistpage | 3CV/XDLIGRO4 |  |  | 组织架构列表展示页 | 动态表单 | haos |
| 38 | haos_orgteamlist | 2B9KD8/MI1A5 |  |  | 组织团队F7-树(控权) | 动态表单 | haos |
| 39 | haos_orgteamlistnoperm | 2B9NM3=DAGLU |  |  | 组织团队F7-树(不控权) | 动态表单 | haos |
| 40 | haos_orgtreefiter | 3DQSWDC6T5=V |  |  | 管理架构组织过滤 | 动态表单 | haos |
| 41 | haos_orgtreelistf7 | 2BEN6JE=S0GX |  |  | 行政组织树 | 动态表单 | haos |
| 42 | haos_othaddbyorg | 5E3GR9+ZOOMB |  |  | 从行政组织添加 | 动态表单 | haos |
| 43 | haos_othemproleorgrelview | 5E34/+ULWOCZ |  |  | 成员查看 | 动态表单 | haos |
| 44 | haos_othorghmptreelistf7 | 5COBSA+3Z7SB |  |  | 其他组织树F7列表（中台权限使用） | 动态表单 | haos |
| 45 | haos_othorgroletreelist | 5COBUNP8VG9L |  |  | 其他组织树列表（成员角色使用） | 动态表单 | haos |
| 46 | haos_othorgroletreelistf7 | 5COBX2P7JLN+ |  |  | 其他组织树F7列表（成员角色使用） | 动态表单 | haos |
| 47 | haos_othorgtreelist | 5COBLCN3KP8T |  |  | 其他组织树列表（组织维护使用） | 动态表单 | haos |
| 48 | haos_othorgtreelistf7 | 5COBPIL32SEQ |  |  | 其他组织树F7列表（组织维护使用） | 动态表单 | haos |
| 49 | haos_reportorgtreefilter | 3E38GSQJM4YF |  |  | 报表树组织过滤 | 动态表单 | haos |
| 50 | haos_rolechange | 5ETQ111JO3KO |  |  | 角色变更 | 动态表单 | haos |
| 51 | haos_staffcompareview | 2P8Z7FR9LQWH |  |  | 组织同步 | 动态表单 | haos |
| 52 | haos_staffshowdisable | 2P5OUC/LQPBB |  |  | 使用组织是否展示禁用组织 | 动态表单 | haos |
| 53 | haos_structorgtreelistf7 | 3J/JUUZFIDFY |  |  | 其他架构组织F7 | 动态表单 | haos |
| 54 | haos_structstructurelist | 3IQYP3I888VX |  |  | 管理架构维护列表模板 | 动态表单 | haos |
| 55 | haos_structtypenamechg | 5CVW2H=1/TC6 |  |  | 架构类型变更名称 | 动态表单 | haos |
| 56 | haos_structtypetab | 5D=+KX7V9AB7 |  |  | 架构类型tab | 动态表单 | haos |
| 57 | haos_useorgcyclecopy | 2KBKVOAADB4O |  |  | 复制编制 | 动态表单 | haos |
| 58 | haos_virtualorg | 3GICT2SF9NJ6 |  |  | 虚拟组织 | 动态表单 | haos |
| 59 | hbjm_apphome | 14SL2NGHT/UQ |  |  | HR基础职位_应用首页 | 动态表单 | hbjm |
| 60 | hbjm_hisjobtreelistf7 | 30OPB23M7XHE |  |  | 职位历史树F7 | 动态表单 | hbjm |
| 61 | hbjm_jobclasstreelist | 14SUAJ94BOTN |  |  | 职位类树列表 | 动态表单 | hbjm |
| 62 | hbjm_jobclasstreelistf7 | 14SUBC6J0RCK |  |  | 职位类F7树形列表 | 动态表单 | hbjm |
| 63 | hbjm_jobgraderange | 54+46PYG2RMT |  |  | 职等范围选择 | 动态表单 | hbjm |
| 64 | hbjm_jobgradetreelist | 4Z036KZNDYOL |  |  | 职等树形列表 | 动态表单 | hbjm |
| 65 | hbjm_joblevelrange | 54+488/RMZYJ |  |  | 职级范围选择 | 动态表单 | hbjm |
| 66 | hbjm_jobleveltreelist | 4Z0AC/5AJHA2 |  |  | 职级树形列表 | 动态表单 | hbjm |
| 67 | hbjm_jobrelateinfo | 54DLRYM+VS2V |  |  | 职位关联信息 | 动态表单 | hbjm |
| 68 | hbjm_jobtreefilterdialog | 14SVQPBXM3XA |  |  | 职位树过滤 | 动态表单 | hbjm |
| 69 | hbjm_jobtreelist | 22GAW3A95SGM |  |  | 职位树形列表 | 动态表单 | hbjm |
| 70 | hbp_activityassign | 3940AEO+CUR/ |  |  | 转交 | 动态表单 | hbp |
| 71 | hbp_activityreject | 3940R4GE0YNS |  |  | 驳回 | 动态表单 | hbp |
| 72 | hbp_activityterm | 57FMTIPNFYE0 |  |  | 终止 | 动态表单 | hbp |
| 73 | hbp_address | 0HDFWBVC9T3= |  |  | 地址控件 | 动态表单 | hbp |
| 74 | hbp_apphome | WZDVZUZRDVA |  |  | HR基础平台_应用首页 | 动态表单 | hbp |
| 75 | hbp_bankcard | 0L9C6Z/60Q=V |  |  | 银行卡 | 动态表单 | hbp |
| 76 | hbp_bd_tinemlinefieldf7 | 4R32THFOHJK/ |  |  | 逻辑主键字段选择 | 动态表单 | hbp |
| 77 | hbp_cardviewtpl | /OZO7/781ARQ |  |  | 卡片视图模板 | 动态表单 | hbp |
| 78 | hbp_contacts | 0L8KCJDAOZXV |  |  | 紧急联系人 | 动态表单 | hbp |
| 79 | hbp_coordbatchexec | 5FB0RDEYRS82 |  |  | 协作批量编辑分批执行 | 动态表单 | hbp |
| 80 | hbp_credentials | 0HHYLEPWY4KW |  |  | 证件 | 动态表单 | hbp |
| 81 | hbp_datagradeparam | 23LTE0T=EGX= |  |  | 参数配置 | 动态表单 | hbp |
| 82 | hbp_datapreview | 2VD3ZJMIWQ=1 |  |  | 数据预览 | 动态表单 | hbp |
| 83 | hbp_defaultvalueedit | 50A0/WIGJ49W |  |  | 缺省值设置 | 动态表单 | hbp |
| 84 | hbp_dgsimulatecal | 28WNSO4K0GP/ |  |  | 模拟运算 | 动态表单 | hbp |
| 85 | hbp_empcoordbatchedit | 4Z2P53YNB1VV |  |  | 员工变动协作批量编辑模板 | 动态表单 | hbp |
| 86 | hbp_empcoordbatchsave | 56K+C1C/W+PK |  |  | 员工变动协作批量赋值 | 动态表单 | hbp |
| 87 | hbp_empcoordparamview | 5=K/1J6TE=EH |  |  | 查看全部参考信息 | 动态表单 | hbp |
| 88 | hbp_empcoordveriflist | 4YV8NEXN2VTR |  |  | 员工变动协作模板列表 | 动态表单 | hbp |
| 89 | hbp_empcoordviewdetail | 5+5JGOTVPNE/ |  |  | 员工变动协作修改详情 | 动态表单 | hbp |
| 90 | hbp_empty_treelistf7 | 47TK3JKD=APK |  |  | HR领域空白F7树形列表 | 动态表单 | hbp |
| 91 | hbp_f7treelistdemo | 3PH/957LO8YS |  |  | HRF7列表树demo | 动态表单 | hbp |
| 92 | hbp_fieldlistf7 | 5+F/IKYEIOBF |  |  | HR选择字段列表 | 动态表单 | hbp |
| 93 | hbp_formulaeglist | 5H0M1WOC/38B |  |  | 公式示例 | 动态表单 | hbp |
| 94 | hbp_formulapreview | 1X+PYUXS6//= |  |  | 公式预览 | 动态表单 | hbp |
| 95 | hbp_formulasimulation | 5H3CE+IR8/K7 |  |  | 公式模拟计算 | 动态表单 | hbp |
| 96 | hbp_funcentity_treelistf7 | 48FAY/WZHNQE |  |  | HR领域功能实体F7树形列表 | 动态表单 | hbp |
| 97 | hbp_functiontemplate | 2V7+X8W98X/= |  |  | 函数模板 | 动态表单 | hbp |
| 98 | hbp_hischangestyle | 2LNO3F6U+D85 |  |  | 变更方式 | 动态表单 | hbp |
| 99 | hbp_hisconfigselectfields | 4UNVOXCXIL3E |  |  | 历史模型配置控件选择字段 | 动态表单 | hbp |
| 100 | hbp_hisimportstart | 24O8DMXSQ/SR |  |  | 历史模型引入起始页 | 动态表单 | hbp |
| 101 | hbp_hislistf7 | 20O0WEOUWER5 |  |  | 历史F7标准列表 | 动态表单 | hbp |
| 102 | hbp_hispluginedit | 51DCJ7003J4K |  |  | 历史模型生效定时任务插件编辑器 | 动态表单 | hbp |
| 103 | hbp_hissetdisabledate | 29S0LW/HLOWD |  |  | 设置禁用日期 | 动态表单 | hbp |
| 104 | hbp_histemplatetreelistf7 | 22FPFM66SJ0F |  |  | 历史标准F7树形列表 | 动态表单 | hbp |
| 105 | hbp_hisversioncompare | 2OZFTR/29D1F |  |  | 版本对比 | 动态表单 | hbp |
| 106 | hbp_leftrelatepaneltpl | 12AYQDY3F2IY |  |  | HR左侧页签模板 | 动态表单 | hbp |
| 107 | hbp_listwithoutfilter | /XEL/FKO2QPY |  |  | HR无过滤列表 | 动态表单 | hbp |
| 108 | hbp_map | 18MWDMXR8OG4 |  |  | HR地图 | 动态表单 | hbp |
| 109 | hbp_modifyfieldname | 3+9=+5JVVN/F |  |  | 修改字段名称 | 动态表单 | hbp |
| 110 | hbp_orgtreefiter | 2ANP02YM2NAJ |  |  | HR组织过滤模板 | 动态表单 | hbp |
| 111 | hbp_orgtreelist | 2B9=DMZVK591 |  |  | HR组织树列表模板 | 动态表单 | hbp |
| 112 | hbp_orgtreelistf7 | 2B9A2P1WUYGO |  |  | HR组织树F7模板 | 动态表单 | hbp |
| 113 | hbp_reviselogpage | 4UUSO0PNFDT2 |  |  | 修订日志侧边栏 | 动态表单 | hbp |
| 114 | hbp_selectentityfields | 2VDAQ2+39+X6 |  |  | 选择实体字段 | 动态表单 | hbp |
| 115 | hbp_setentityrelation | 2VD17LOFM3T3 |  |  | 设置关联关系 | 动态表单 | hbp |
| 116 | hbp_showallversion | 2XRYGD3E3XWX |  |  | 查看所有版本 | 动态表单 | hbp |
| 117 | hbp_tabmenutpl | 5+3S7TFN4M1B |  |  | 页签样式菜单 | 动态表单 | hbp |
| 118 | hbp_timelinelogdetail | 4RGAU674G0XC |  |  | 日志详情 | 动态表单 | hbp |
| 119 | hbp_treelistf7 | 29E6S0FKE6AT |  |  | HR领域F7树型列表 | 动态表单 | hbp |
| 120 | hbp_treequerycriteria | 2ITU0QA4OI1O |  |  | 行政组织列表查询条件 | 动态表单 | hbp |
| 121 | hbp_unittestpage01 | 5BPJ5PUGJ+J= |  |  | hbp单测动态表单页面01 | 动态表单 | hbp |
| 122 | hbpm_addpositiondialog | 4SPRH955=GQX |  |  | 请选择行政组织和岗位模板 | 动态表单 | hbpm |
| 123 | hbpm_apphome | 14SP9PPVZ=5N |  |  | 岗位基础服务_应用首页 | 动态表单 | hbpm |
| 124 | hbpm_applicationscope | 3OBCQHVI9R=D |  |  | 设置适用范围 | 动态表单 | hbpm |
| 125 | hbpm_futuretipsshow | 57IWT/CR2PLL |  |  | 岗位待生效指引提示 | 动态表单 | hbpm |
| 126 | hbpm_list | 2ACP7A76ZR3X |  |  | 标准列表 | 动态表单 | hbpm |
| 127 | hbpm_onlypostreelistf7 | 4TA=LTEGG9B9 |  |  | 岗位F7组织树 | 动态表单 | hbpm |
| 128 | hbpm_positionhistree | 4SXHR21G3VY/ |  |  | 岗位历史树列表 | 动态表单 | hbpm |
| 129 | hbpm_positionorgtreelist | 3KETR0=/0EFM |  |  | 岗位列表组织树 | 动态表单 | hbpm |
| 130 | hbpm_positionrevise | 4SQ1CP/F44PK |  |  | 岗位修订 | 动态表单 | hbpm |
| 131 | hbpm_positiontpltreelist | 3FWRFAGZFH44 |  |  | 岗位模板左树右表模型 | 动态表单 | hbpm |
| 132 | hbpm_positiontreelistf7 | 14VMQW4X0T8R |  |  | 岗位历史版本树(岗位关系图) | 动态表单 | hbpm |
| 133 | hbpm_positiorelateinfo | 4T+BGU60FP0T |  |  | 岗位关联信息 | 动态表单 | hbpm |
| 134 | hbpm_posorgtreelistf7 | 3KEWBF9UN3BQ |  |  | 岗位/标岗F7组织树 | 动态表单 | hbpm |
| 135 | hbpm_reviselogdetail | 35ODEKASIHY5 |  |  | 岗位修订内容详情 | 动态表单 | hbpm |
| 136 | hbss_appgridhome | /L8LX1/XHON= |  |  | HR基础服务应用首页-栅格 | 动态表单 | hbss |
| 137 | hbss_apphome | XYRL7T/5F3L |  |  | HR基础服务系统_应用首页 | 动态表单 | hbss |
| 138 | hbss_assemblelist | /ST0C9OYIX2S |  |  | 生成查询列表 | 动态表单 | hbss |
| 139 | hbss_capacityitemtypeshow | 4+JBP=XZ8HB+ |  |  | 能力素质项描述方式展示 | 动态表单 | hbss |
| 140 | hbss_capacitytypeshow | 3OIELAUBLX4B |  |  | 能力素质维度描述方式展示 | 动态表单 | hbss |
| 141 | hbss_certgroupmember | 2D6XNNDXCS8Z |  |  | HR许可员工明细 | 动态表单 | hbss |
| 142 | hbss_certinterfacetest | 2E3MYD1DAWIB |  |  | 中台许可接口测试 | 动态表单 | hbss |
| 143 | hbss_cloud_app_treelist | 2H0Z+H+3LSSG |  |  | HR云与应用列表 | 动态表单 | hbss |
| 144 | hbss_commonloginpc | 46XQML6LS9=J |  |  | PC端登录页 | 动态表单 | hbss |
| 145 | hbss_failurereason | 289=KFST68FY |  |  | 失败原因 | 动态表单 | hbss |
| 146 | hbss_hrbudellimit | 2DH12+8BAYV5 |  |  | 移除校验范围示例 | 动态表单 | hbss |
| 147 | hbss_hrbuf7 | /US3HB008C/3 |  |  | 业务单元F7 | 动态表单 | hbss |
| 148 | hbss_levelscoremap | 2R/A3RP+HS/O |  |  | 等级与分制映射 | 动态表单 | hbss |
| 149 | hbss_licnotice | 4GHOP161G7LX |  |  | 许可校验异常 | 动态表单 | hbss |
| 150 | hbss_licover100 | 4GHTJIAX/=+4 |  |  | 许可占水位100 | 动态表单 | hbss |
| 151 | hbss_licover110 | 4GHTVRGOSWS4 |  |  | 许可占水位110 | 动态表单 | hbss |
| 152 | hbss_licover90 | 4GHT0EL0JE5V |  |  | 许可占水位90 | 动态表单 | hbss |
| 153 | hbss_logdetail | 2/FJ36G/717Q |  |  | 日志详情 | 动态表单 | hbss |
| 154 | hbss_nodatalist | /TIC62NPW/72 |  |  | 无数据列表 | 动态表单 | hbss |
| 155 | hbss_op_result | 2=NQYD5O/ZBK |  |  | 禁用操作结果 | 动态表单 | hbss |
| 156 | hbss_op_toconfirm | 29RHIVQYAXI= |  |  | 待确认 | 动态表单 | hbss |
| 157 | hbss_opconfirm | 2KAS+RXIHJYC |  |  | 操作确认 | 动态表单 | hbss |
| 158 | hbss_opresult | 2K8E6NGXAFIO |  |  | 操作结果 | 动态表单 | hbss |
| 159 | hbss_orgcheckresult | 288X4E9GGR1Q |  |  | 移除组织结果 | 动态表单 | hbss |
| 160 | hbss_pcprivacy | 47EZSHVB=8PY |  |  | PC隐私签署 | 动态表单 | hbss |
| 161 | hbss_rulechecktip | 3ZGM51VQVVM9 |  |  | 较低排序标尺等级不能排在较高排序的标尺等级前面 | 动态表单 | hbss |
| 162 | hbss_rulegrademap | 2R/A938+J/7D |  |  | 绩效等级与标尺等级映射 | 动态表单 | hbss |
| 163 | hbss_safeuriexpirypc | 47THHHW4RK3F |  |  | 短链失效 | 动态表单 | hbss |
| 164 | hbss_strategy_logdetail | 2MRJRZZCJ9/4 |  |  | 日志详情 | 动态表单 | hrcs |
| 165 | hbss_syncprocess | 2KOKJ7Y+EQQH |  |  | 同步策略进度条 | 动态表单 | hbss |
| 166 | hbss_taskprogress | 288B5R/O8DU6 |  |  | 移除组织校验 | 动态表单 | hbss |
| 167 | hbss_templatetreelist_ct | 2A0KE50/7+US |  |  | 合同类型标准树列表 | 动态表单 | hbss |
| 168 | hcf_apphome | 15W6KBRM308V |  |  | 拟入职人员_应用首页 | 动态表单 | hcf |
| 169 | hr_list | 13K+DNSNW+27 |  |  | HR列表 | 动态表单 | hbp |
| 170 | hrcs_actassign | 2/55B/UVIKH7 |  |  | 分配活动处理人 | 动态表单 | hrcs |
| 171 | hrcs_actfieldtree | 385F6TUO59Z+ |  |  | 字段选择 | 动态表单 | hrcs |
| 172 | hrcs_activitynodeconfig | 4/4Q+7FJZT/T |  |  | HR节点处理设置 | 动态表单 | hrcs |
| 173 | hrcs_actschemetheme | 2/M8QEV8GQDH |  |  | 活动方案主题 | 动态表单 | hrcs |
| 174 | hrcs_actschemethemeexp | 2/MB7AUI69N3 |  |  | 主题属性选择 | 动态表单 | hrcs |
| 175 | hrcs_addnoentityrule | 2+N/GXGPN+5= |  |  | 提示语显示规则(无实体) | 动态表单 | hrcs |
| 176 | hrcs_addrule | 2+KKZ7GZ8JS7 |  |  | 提示语显示规则(有实体) | 动态表单 | hrcs |
| 177 | hrcs_admin_group_treelist | 2ATZJ=VQKFH/ |  |  | HR管理员分组 | 动态表单 | hrcs |
| 178 | hrcs_admingroupdetail | 2ACH647ZVCAS |  |  | 管理员分组授权 | 动态表单 | hrcs |
| 179 | hrcs_admingrouptip | 2CS=859OSYJ1 |  |  | 数据授权范围默认提示 | 动态表单 | hrcs |
| 180 | hrcs_amingroupbatchauth | 4/S77OHB59D1 |  |  | 管理员分组批量授权 | 动态表单 | hrcs |
| 181 | hrcs_apphome | 15NPDZTF91FP |  |  | HR通用服务_应用首页 | 动态表单 | hrcs |
| 182 | hrcs_apptreelist | 1ZK6=4Z6XI7T |  |  | 应用左树列表 | 动态表单 | hrcs |
| 183 | hrcs_basedisplayprop | 23SPWTWWCM3L |  |  | 显示属性 | 动态表单 | hrcs |
| 184 | hrcs_basepropertyref | 2+NBKYL5+FFO |  |  | 引用属性 | 动态表单 | hrcs |
| 185 | hrcs_batchsetting | 19KNWL1S1A/9 |  |  | 数据规则设置 | 动态表单 | hrcs |
| 186 | hrcs_batchsetvalidity | 2YE3WCCA7PMS |  |  | 角色成员分配详情 | 动态表单 | hrcs |
| 187 | hrcs_bddatarulesetting | 20IT97UEFWYA |  |  | 基础资料数据规则设置 | 动态表单 | hrcs |
| 188 | hrcs_changeheaderselect | 15HKYXVX9D7= |  |  | 策略变更方式 | 动态表单 | hrcs |
| 189 | hrcs_checkrelate | 2PA71DFPW19D |  |  | 查看关联实体 | 动态表单 | hrcs |
| 190 | hrcs_coordruleschopguide | 5/IEUSE21SRW |  |  | 协作规则方案操作指引 | 动态表单 | hrcs |
| 191 | hrcs_copyperm | 3NG6WDD7/DMV |  |  | 复制权限 | 动态表单 | hrcs |
| 192 | hrcs_customizemp | 3FFC+6TJEYD2 |  |  | 自定义取值关系 | 动态表单 | hrcs |
| 193 | hrcs_datapermcontainer | 2/F6/52Q=/QJ |  |  | 数据范围控件容器 | 动态表单 | hrcs |
| 194 | hrcs_datarule_batchset | 2CHX44PB2JN9 |  |  | 批量设置 | 动态表单 | hrcs |
| 195 | hrcs_decision_param | 52NA=MDA+5P1 |  |  | 决策表参数配置 | 动态表单 | hrcs |
| 196 | hrcs_decisiontab_impor | 52N=PT=2=2UR |  |  | 决策表导入向导 | 动态表单 | hrcs |
| 197 | hrcs_dim_rel_rolelist | 326OK/CC5+0A |  |  | 维度关联角色列表 | 动态表单 | hrcs |
| 198 | hrcs_dimcard | 2/FU3DJB30BF |  |  | 维度卡片分录组件 | 动态表单 | hrcs |
| 199 | hrcs_dimgrpcontainer | 2/F5EVLN5D6N |  |  | 维度组容器 | 动态表单 | hrcs |
| 200 | hrcs_dimvalueselector | 3XSSF8B4UNTD |  |  | 维度值选择器 | 动态表单 | hrcs |
| 201 | hrcs_downtemplate | 3KM3P4I164OK |  |  | 下载打标数据 | 动态表单 | hrcs |
| 202 | hrcs_dynadim | 35=RCPTTL/06 |  |  | 动态维度 | 动态表单 | hrcs |
| 203 | hrcs_dynashemeassign | 3UDK2NW+FS/J |  |  | 方案分配管理员组 | 动态表单 | hrcs |
| 204 | hrcs_dyscassignroledetail | 3YV7M8QTDYQQ |  |  | 动态方案分配角色详情 | 动态表单 | hrcs |
| 205 | hrcs_editdisplayprop | 2ACEO9BCFFS9 |  |  | 编辑显示属性 | 动态表单 | hrcs |
| 206 | hrcs_entityctrlview | 29=ZA7805BUA |  |  | 实体维度关联视图 | 动态表单 | hrcs |
| 207 | hrcs_entityfieldmpf7tree | 3GOZQFHRUB36 |  |  | 实体字段取值关系F7树 | 动态表单 | hrcs |
| 208 | hrcs_entityfieldmpfilter | 3GSMJBXJ11K3 |  |  | 设置过滤条件 | 动态表单 | hrcs |
| 209 | hrcs_entityfieldtree | 36CB10JP35+5 |  |  | 选择字段 | 动态表单 | hrcs |
| 210 | hrcs_entitysyncconfigdw | 1ZKF897QWN5I |  |  | 实体同步配置对话框 | 动态表单 | hrcs |
| 211 | hrcs_entitytreelist | 19AWO03/N3TX |  |  | HR实体左树列表 | 动态表单 | hrcs |
| 212 | hrcs_esconfigconfirm | 2M3ATFIFJF5K |  |  | ES配置对话框 | 动态表单 | hrcs |
| 213 | hrcs_esentityfieldselect | 2L8H52G7Z9R6 |  |  | 实体字段选择器 | 动态表单 | hrcs |
| 214 | hrcs_esigncosealmgr | 3F4YGLYHTZJ6 |  |  | 企业印章管理 | 动态表单 | hrcs |
| 215 | hrcs_esigndebug | 3EX/O/K9DD+Y |  |  | 在线调试 | 动态表单 | hrcs |
| 216 | hrcs_essyncrecordlist | 2N6FHSDQXRW7 |  |  | 同步记录列表展示 | 动态表单 | hrcs |
| 217 | hrcs_exportperm | 390EB99+0M5U |  |  | 导出中 | 动态表单 | hrcs |
| 218 | hrcs_fieldbatchset | 3W2HYY/M73E5 |  |  | 批量设置 | 动态表单 | hrcs |
| 219 | hrcs_fieldperm | 2LA1EXQPY4E= |  |  | 字段权限 | 动态表单 | hrcs |
| 220 | hrcs_filtercondition | 36YL=3FFK48H |  |  | 过滤条件 | 动态表单 | hrcs |
| 221 | hrcs_filterseditnew | 2/58ADOS5CVG |  |  | 过滤条件配置 | 动态表单 | hrcs |
| 222 | hrcs_importing | 19KM8S03NG/R |  |  | 权限引入中 | 动态表单 | hrcs |
| 223 | hrcs_importstart | 19KMDR1F4J3B |  |  | 权限引入起始页 | 动态表单 | hrcs |
| 224 | hrcs_labeladddata | 3OM9+AF27XRX |  |  | 打标数据 | 动态表单 | hrcs |
| 225 | hrcs_labeldatalist | 3NJAXDUCYD0M |  |  | 打标数据列表 | 动态表单 | hrcs |
| 226 | hrcs_labeldialogshow | 3LZ3QG8D8Z3W |  |  | 操作确认 | 动态表单 | hrcs |
| 227 | hrcs_labelexportperm | 3Q36Z2W4UTBG |  |  | 导出中 | 动态表单 | hrcs |
| 228 | hrcs_labelgroupf7 | 2W5G9IHH7XTL |  |  | 标签分类f7 | 动态表单 | hrcs |
| 229 | hrcs_labelhitresult | 2WFWHXW7VQ=G |  |  | 标签命中结果 | 动态表单 | hrcs |
| 230 | hrcs_labelimport | 3KM8AU7/EAU4 |  |  | 导入打标数据 | 动态表单 | hrcs |
| 231 | hrcs_labellist | 31RQZL3USM1R |  |  | 标签列表左树 | 动态表单 | hrcs |
| 232 | hrcs_labelobjectlist | 2VUWIDAIDJNM |  |  | 打标对象列表（废弃） | 动态表单 | hrcs |
| 233 | hrcs_labelrelation | 2UUL3S=KPAY1 |  |  | 打标对象关联关系 | 动态表单 | hrcs |
| 234 | hrcs_labelstrategyfilter | 3JX/OZP0R6K4 |  |  | 打标策略范围页 | 动态表单 | hrcs |
| 235 | hrcs_labelvalueruleconf | 2WQ47V1Z38/D |  |  | 配置规则 | 动态表单 | hrcs |
| 236 | hrcs_labelvaluetreelistf7 | 41O7OUSNUFT1 |  |  | 标签值F7 | 动态表单 | hrcs |
| 237 | hrcs_lblassignfieldtype | 2WQVA8JYCYJ6 |  |  | 归置 | 动态表单 | hrcs |
| 238 | hrcs_modifyrole | 0TL4NW85MYLF |  |  | 修改角色 | 动态表单 | hrcs |
| 239 | hrcs_newrole | 0UBA6/K4CJBE |  |  | 新增角色 | 动态表单 | hrcs |
| 240 | hrcs_objectfield_list | 31O0+Z4PW2KY |  |  | 因子管理左树 | 动态表单 | hrcs |
| 241 | hrcs_onlinetestfunction | 25R1/HTPCDQQ |  |  | 函数在线调试 | 动态表单 | hrcs |
| 242 | hrcs_orgincostcenter | 066R=ITZJDQ+ |  |  | 成本中心包含行政组织 | 动态表单 | hrcs |
| 243 | hrcs_ottreepreview | 2N=+MQMZYMIZ |  |  | 预览详情 | 动态表单 | hrcs |
| 244 | hrcs_permapplybilldetail | 3ZGF27QCYTHV |  |  | 权限申请记录详情 | 动态表单 | hrcs |
| 245 | hrcs_permcommontreef7 | 1ZJT=PBDUGQH |  |  | HR权限通用树形基础资料f7 | 动态表单 | hrcs |
| 246 | hrcs_permfilegrptree | 0S6F3T=BZ+2R |  |  | 档案组 | 动态表单 | hrcs |
| 247 | hrcs_permimportstart | 33XG4T26DTGZ |  |  | 导入 | 动态表单 | hrcs |
| 248 | hrcs_permimportstartbase | 3DO0WW4FEJQ0 |  |  | 导入模板页 | 动态表单 | hrcs |
| 249 | hrcs_perminitcheckresult | 3=Y1LIPWZX3J |  |  | 检测结果 | 动态表单 | hrcs |
| 250 | hrcs_permitemcontainer | 20IT22MZN+EA |  |  | 权限项容器 | 动态表单 | hrcs |
| 251 | hrcs_permlog_bodimmapping | 449Y7Q5BSHP0 |  |  | 业务对象维度映射日志详情 | 动态表单 | hrcs |
| 252 | hrcs_permlog_datarule | 44=223L9ID70 |  |  | 数据规则日志详情 | 动态表单 | hrcs |
| 253 | hrcs_permlog_role | 43ME/8QZ2FRC |  |  | 角色日志详情 | 动态表单 | hrcs |
| 254 | hrcs_permlog_userperm | 43SLD4HZF7XI |  |  | 用户权限日志详情 | 动态表单 | hrcs |
| 255 | hrcs_permlogarchive_set | 4154=VX1151Z |  |  | HR权限日志归档设置 | 动态表单 | hrcs |
| 256 | hrcs_permotlazytreef7 | 2MNT+30S2AVP |  |  | 组织团队懒加载树形f7 | 动态表单 | hrcs |
| 257 | hrcs_permroleinitdim | 3GRMM=KT/FUW |  |  | 角色维度 | 动态表单 | hrcs |
| 258 | hrcs_permroleinitdr | 3GV4K9S/6YK0 |  |  | 角色数据范围 | 动态表单 | hrcs |
| 259 | hrcs_permroleinitfunc | 3GP+JIQ3NLK6 |  |  | 角色功能权限 | 动态表单 | hrcs |
| 260 | hrcs_permtreecharge | 1Z/O9=B1D/O5 |  |  | 权限树管理 | 动态表单 | hrcs |
| 261 | hrcs_personorgtreelistf7 | 2609O77F4VFV |  |  | 人员F7树型列表 | 动态表单 | hrcs |
| 262 | hrcs_precisequerylistf7 | 2E3NWLH5OV9N |  |  | 查询F7精确搜索列表 | 动态表单 | hrcs |
| 263 | hrcs_promptcustomvar | 2+3GVP0LPSXC |  |  | 自定义变量 | 动态表单 | hrcs |
| 264 | hrcs_promptcustomwindow | 2+=NN+2MED1S |  |  | 自定义弹窗 | 动态表单 | hrcs |
| 265 | hrcs_prompttgl | 1KBLA0HXAK9R |  |  | 系统文案 | 动态表单 | hrcs |
| 266 | hrcs_prompttreelistf7 | 1J9T+BQKXQE6 |  |  | 提示语配置F7 | 动态表单 | hrcs |
| 267 | hrcs_promptview | 2+RHOESCBKGN |  |  | 查看提示语内容 | 动态表单 | hrcs |
| 268 | hrcs_querydynsource | 294PA802GLRQ |  |  | 查询配置 | 动态表单 | hrcs |
| 269 | hrcs_querylistf7 | 2/T8CT//+N2F |  |  | 查询F7标准列表 | 动态表单 | hrcs |
| 270 | hrcs_querytreelistf7 | 2/T8J0+VQ/0E |  |  | 查询F7树型列表 | 动态表单 | hrcs |
| 271 | hrcs_refdetails | 2/IZ=MFJIY2I |  |  | 引用详情 | 动态表单 | hrcs |
| 272 | hrcs_refdisplaypropedit | 235KWN34CX24 |  |  | 显示属性 | 动态表单 | hrcs |
| 273 | hrcs_revedimgrpcontainer | 4HRDOG27R7UM |  |  | 例外维度组容器 | 动态表单 | hrcs |
| 274 | hrcs_rolehrbudialog | 0XD=B7GDBTHH |  |  | 提示 | 动态表单 | hrcs |
| 275 | hrcs_roleinitcheckresult | 3I46FL9T/6UA |  |  | 角色初始化检测结果 | 动态表单 | hrcs |
| 276 | hrcs_rolememassign | 2/69U0+6WQOT |  |  | 角色分配成员 | 动态表单 | hrcs |
| 277 | hrcs_rolememassigndetail | 2/TH7=/OTNSC |  |  | 角色成员分配详情 | 动态表单 | hrcs |
| 278 | hrcs_rolepermimportstart | 3E9GE5M1R/LE |  |  | 导入 | 动态表单 | hrcs |
| 279 | hrcs_roletreelistf7 | 1+YC/L2FU2/8 |  |  | HR角色f7 | 动态表单 | hrcs |
| 280 | hrcs_setentityrelation | 3TVNVMW+B0OL |  |  | 设置关联关系 | 动态表单 | hrcs |
| 281 | hrcs_showapplydim | 3WGX2WK8YPII |  |  | 适用维度 | 动态表单 | hrcs |
| 282 | hrcs_smartsearchtest | 2MUKPL/MLOVT |  |  | 智能搜索测试页面 | 动态表单 | hrcs |
| 283 | hrcs_strategyorgtreelist | 15HN7N2=ZOKK |  |  | 管理关系策略左树右表 | 动态表单 | hrcs |
| 284 | hrcs_strategyprojteamtree | 2I73/6QHVSYQ |  |  | 项目团队管理关系策略左树右表 | 动态表单 | hrcs |
| 285 | hrcs_syncpermfile | 3OCCV9H55WGI |  |  | 按组织分配生成权限档案 | 动态表单 | hrcs |
| 286 | hrcs_syncprocess | 2N=7YD8HCKL6 |  |  | 同步进度条 | 动态表单 | hrcs |
| 287 | hrcs_syncrolenotice | 2YERXEG1D75O |  |  | 关联权限项清单通知 | 动态表单 | hrcs |
| 288 | hrcs_syncroleperm | 2X1XKTCGPC10 |  |  | 关联权限项角色清单 | 动态表单 | hrcs |
| 289 | hrcs_syncrolesel | 2YERJMUDKO+X |  |  | 关联权限项清单 | 动态表单 | hrcs |
| 290 | hrcs_targetadmingroup | 40A5PNJ4EJ6S |  |  | 选择目标管理员分组 | 动态表单 | hrcs |
| 291 | hrcs_userassignrole | 1+=ANG/F9X/T |  |  | 用户分配角色 | 动态表单 | hrcs |
| 292 | hrcs_userassignroledetail | 20DR36DIYVXW |  |  | 成员分配角色详情 | 动态表单 | hrcs |
| 293 | hrcs_validdialog | 2YKWJDG30=4G |  |  | 批量设置有效期 | 动态表单 | hrcs |
| 294 | hrcs_walktestconfirm | 4N9NMUL5=VME |  |  | 模拟验证 | 动态表单 | hrcs |
| 295 | hrcs_warlktestprogress | 4N9M0+VEXATD |  |  | 执行中 | 动态表单 | hrcs |
| 296 | hrcs_warlktestresult | 4N9MX256BOH2 |  |  | 模拟验证结果 | 动态表单 | hrcs |
| 297 | hrcs_warnbilingualmsg | 4YE23LUEPRE8 |  |  | 双语预警消息(废弃) | 动态表单 | hrcs |
| 298 | hrcs_warncalconfig | 3UZ3TMS0+AWK |  |  | 计算字段配置 | 动态表单 | hrcs |
| 299 | hrcs_warncomcdcalconfig | 3WCOV0GE10/0 |  |  | 常用条件计算字段配置 | 动态表单 | hrcs |
| 300 | hrcs_warnexecdetail | 5K5+BZ0D26EZ |  |  | 预警日志执行详情 | 动态表单 | hrcs |
| 301 | hrcs_warnfieldselect | 5J2=95QRQB58 |  |  | 预警字段选择 | 动态表单 | hrcs |
| 302 | hrcs_warnfunctiontemplate | 5JTU1YH/57RI |  |  | 函数模板 | 动态表单 | hrcs |
| 303 | hrcs_warnmsg | 4GH9CB9ELL=W |  |  | 预警消息 | 动态表单 | hrcs |
| 304 | hrcs_warnmsgedit | 4YGHWF1N2TUS |  |  | 预警消息编辑区(废弃) | 动态表单 | hrcs |
| 305 | hrcs_warnpetreelistf7 | 48OFH047ZXZ2 |  |  | 预警角色F7树型列表 | 动态表单 | hrcs |
| 306 | hrcs_warnscenereceiver | 3W8ZBNCGYI/K |  |  | 预警接收人(废弃) | 动态表单 | hrcs |
| 307 | hrcs_warnscheme_bizmustcd | 4GI+APF9R7VK |  |  | 预警方案业务对象必要条件动态表单 | 动态表单 | hrcs |
| 308 | hrcs_workingplandy | 23IYHGA9/YV6 | t_hrcs_workingplanplace |  | 公共日历动态表单 | 动态表单 | hrcs |
| 309 | hrpi_apphome | 0QO143E56F/K |  |  | 员工信息中心_应用首页 | 动态表单 | hrpi |
| 310 | hrpi_cart_list | 5M=TXKBJPJNK |  |  | 标准列表 | 动态表单 | hrti |
| 311 | hrpi_employeequerylistf7 | 417CQVQ9V1/N |  |  | 人员F7标准列表 | 动态表单 | hrpi |
| 312 | hrpi_employeorgquerlistf7 | 4S97E5XN1EXO |  |  | 人员F7带组织左树列表 | 动态表单 | hrpi |
| 313 | hrpi_employepreciselistf7 | 417FOWBB5+K+ |  |  | 员工F7精确搜索列表 | 动态表单 | hrpi |
| 314 | hrpi_empmodelconfigfield | 4Z3TJR6LOXIU |  |  | 人员字段配置f7 | 动态表单 | hrpi |
| 315 | hrpi_empposquerylistf7 | 4S95MZ=3TMG5 |  |  | 任职经历F7标准列表 | 动态表单 | hrpi |
| 316 | hrpi_empquerytreelist | 03ZWVHPR2DA3 |  |  | 员工查询列表左树 | 动态表单 | hrpi |
| 317 | hrptmc_anobjmonitor | 3I2O8CF=NSEZ |  |  | 分析对象监控 | 动态表单 | hrptmc |
| 318 | hrptmc_anobjtemplatef7 | 4+M7=Z76IVYI |  |  | 选择分析对象模板 | 动态表单 | hrptmc |
| 319 | hrptmc_apphome | 2VKJ965H9WL7 |  |  | HR报表_应用首页 | 动态表单 | hrptmc |
| 320 | hrptmc_bdimportconfirm | 3QGU8OLI4J4V |  |  | 引用基础资料导入确认 | 动态表单 | hrptmc |
| 321 | hrptmc_calfieldconfig | 2VFMWTENIDPR |  |  | 计算字段配置 | 动态表单 | hrptmc |
| 322 | hrptmc_chartnotes | 4OES21ESL9U4 |  |  | 图表备注配置 | 动态表单 | hrptmc |
| 323 | hrptmc_choosepublishmenu | 2W21SA=ZGEAU |  |  | 选择发布菜单 | 动态表单 | hrptmc |
| 324 | hrptmc_configexportstart | 3P8JU50G+DWU |  |  | 导出 | 动态表单 | hrptmc |
| 325 | hrptmc_configimporting | 3OT63//0MM+Y |  |  | 导入中 | 动态表单 | hrptmc |
| 326 | hrptmc_configimportstart | 3OW71BB97=78 |  |  | 导入 | 动态表单 | hrptmc |
| 327 | hrptmc_copyreport | 4EGDT/1FDBSE |  |  | 复制报表 | 动态表单 | hrptmc |
| 328 | hrptmc_correctimport | 3P52BOU=CHG8 |  |  | 导入确认 | 动态表单 | hrptmc |
| 329 | hrptmc_createpivotdimval | 358FJFW11+AN |  |  | 动态创建维度值控件 | 动态表单 | hrptmc |
| 330 | hrptmc_dataextractconfig | 407IBCS3JIT0 |  |  | 数据抽取配置 | 动态表单 | hrptmc |
| 331 | hrptmc_datafilter | 2Z31D1T/SJUB |  |  | 数据过滤 | 动态表单 | hrptmc |
| 332 | hrptmc_datapermrule | 3MGSCCJ08SFM |  |  | 数据控权规则配置 | 动态表单 | hrptmc |
| 333 | hrptmc_displayinfo | 36Y4+F+C2G8J |  |  | 新增方案 | 动态表单 | hrptmc |
| 334 | hrptmc_exportdataprogress | 2ZRL86JJP+IS |  |  | 导出中 | 动态表单 | hrptmc |
| 335 | hrptmc_filesourcimporting | 3V00LGDQDQTZ |  |  | 导入中 | 动态表单 | hrptmc |
| 336 | hrptmc_filtersplitdate | 3MR2TI1/U+D= |  |  | 拆分日期粒度 | 动态表单 | hrptmc |
| 337 | hrptmc_importcomplete | 3QR9P+7ECTTJ |  |  | 导入完成 | 动态表单 | hrptmc |
| 338 | hrptmc_mergeother | 4NM+FZE60CLI |  |  | 合并数据为其他 | 动态表单 | hrptmc |
| 339 | hrptmc_moveto | 3TOYUDXSX/XT |  |  | 移动位置设置 | 动态表单 | hrptmc |
| 340 | hrptmc_newreport | 2X2IHN3BV03Y |  |  | 新增报表 | 动态表单 | hrptmc |
| 341 | hrptmc_orgpermtreelistf7 | 4BS1TSIAW=AE |  |  | HR报表校验权限的行政组织F7 | 动态表单 | hrptmc |
| 342 | hrptmc_preindexdimmap | 2Y4WEW9DE7B7 |  |  | 预置指标维度映射 | 动态表单 | hrptmc |
| 343 | hrptmc_publish2rpt | 49B2IMLKYS77 |  |  | 选择发布分组 | 动态表单 | hrptmc |
| 344 | hrptmc_report_preview | 30SEQ+91JA18 |  |  | 报表预览 | 动态表单 | hrptmc |
| 345 | hrptmc_report_tpl | 2TM0OXNJ06ZJ |  |  | 报表模板 | 动态表单 | hrptmc |
| 346 | hrptmc_reporthelp | 3=0OC5TRVV3K |  |  | 报表说明 | 动态表单 | hrptmc |
| 347 | hrptmc_reportmonitor | 3M=OON4=C=KF |  |  | 报表配置监控 | 动态表单 | hrptmc |
| 348 | hrptmc_reportquerychg | 3VNMZ59Q2AAQ |  |  | 报表查询插件变更通知 | 动态表单 | hrptmc |
| 349 | hrptmc_rptshareconfig | 47WVOD7ELCFH |  |  | 报表分享 | 动态表单 | hrptmc |
| 350 | hrptmc_selanobjtreef7 | 2W/JAD+EC6F7 |  |  | 选择分析对象树形F7 | 动态表单 | hrptmc |
| 351 | hrptmc_selcol | 2XWDPDPPK/S= |  |  | 字段选择 | 动态表单 | hrptmc |
| 352 | hrptmc_setcustomsort | 35R47FA8WXOF |  |  | 自定义排序 | 动态表单 | hrptmc |
| 353 | hrptmc_setdisplaymode | 2ZY0J8D45M1T |  |  | 设置展示方式 | 动态表单 | hrptmc |
| 354 | hrptmc_setdisplayname | 2WTK39MAQJF/ |  |  | 显示名设置 | 动态表单 | hrptmc |
| 355 | hrptmc_setdisplayscheme | 36K/L=MQGNBF |  |  | 显示方案配置 | 动态表单 | hrptmc |
| 356 | hrptmc_setfieldsort | 3EPOCFA0ENX6 |  |  | 字段排序 | 动态表单 | hrptmc |
| 357 | hrptmc_stopworld | 497WCRFD0PL0 |  |  | 停止订阅任务 | 动态表单 | hrptmc |
| 358 | hrptmc_subdataclear | 4ATK/NHGREI= |  |  | 订阅数据清理 | 动态表单 | hrptmc |
| 359 | hrptmc_subscribeconfig | 47TU7WQI6GA2 |  |  | 报表订阅 | 动态表单 | hrptmc |
| 360 | hrptmc_subtotallinecfg | 2X9I6M4AXAFC |  |  | 小计行设置 | 动态表单 | hrptmc |
| 361 | hrptmc_topnconfig | 4O94ZH9H03KI |  |  | TopN设置 | 动态表单 | hrptmc |
| 362 | hrptmc_totalcolumn | 3EMX39B2FWZ8 |  |  | 汇总列 | 动态表单 | hrptmc |
| 363 | hrptmc_totalrow | 3EMX16T=I4UD |  |  | 汇总行 | 动态表单 | hrptmc |
| 364 | hrptmc_validatefail | 3PW/6WIL4NN9 |  |  | 报表导入校验失败 | 动态表单 | hrptmc |
| 365 | hrss_aisearchinfo | 2KBA4H4UX3VZ |  |  | AI词性排序权重详情 | 动态表单 | hrss |
| 366 | hrss_apphome | 2K8WVF1CRQSL |  |  | HR智能搜索_应用首页 | 动态表单 | hrss |
| 367 | hrss_confirmdatasync | 3Z=Y/B4UYV34 |  |  | 确认是否重新同步ES数据 | 动态表单 | hrss |
| 368 | hrss_errormessage | 3WF09IMHWLYI |  |  | ES数据同步错误 | 动态表单 | hrss |
| 369 | hrss_essynrecordfilter | 40HQK7SH92WR |  |  | es同步记录子表 | 动态表单 | hrss |
| 370 | hrss_essynrecordlist | 3W6Y0GCLR9KQ |  |  | es同步记录列表展示 | 动态表单 | hrss |
| 371 | hrss_essynrunning | 3WCOJ/UXOY6Z |  |  | ES数据同步 | 动态表单 | hrss |
| 372 | hrss_fieldlabeltree | 4/S499X74//5 |  |  | 搜索字段 | 动态表单 | hrss |
| 373 | hrss_fieldorder | 3UVA4FWFDY6U |  |  | 移动至 | 动态表单 | hrss |
| 374 | hrss_filteritem | 3U=I3ZC/J0Z8 |  |  | 过滤项目 | 动态表单 | hrss |
| 375 | hrss_filteritemlist | 3UXZ580ZSB=D |  |  | 自定义过滤项列表 | 动态表单 | hrss |
| 376 | hrss_gptprompttest | 3VQL=0ZLBS7+ |  |  | 词性测试 | 动态表单 | hrss |
| 377 | hrss_labelgpttest | 3W3HY135SUR5 |  |  | 标签测试 | 动态表单 | hrss |
| 378 | hrss_labelselect | 408GN0A3AKH3 |  |  | 标签搜索 | 动态表单 | hrss |
| 379 | hrss_presearchscene | 2KWALD8M/UR8 |  |  | 预览搜索场景 | 动态表单 | hrss |
| 380 | hrss_searchconfigtarget | 3UV62KJMRI/F |  |  | 配置搜索标签 | 动态表单 | hrss |
| 381 | hrss_searchentityf7 | 2KLN5K7FPQYB |  |  | 对象选择 | 动态表单 | hrss |
| 382 | hrss_searchfieldsel | 5DK/SZBE72NV |  |  | 搜索字段选择 | 动态表单 | hrss |
| 383 | hrss_searchresult_info | 2KBD5S=P=B8W |  |  | 搜索结果排序权重设置信息 | 动态表单 | hrss |
| 384 | hrss_searchscenetest | 3VJN66QVVGM8 |  |  | 搜索场景测试 | 动态表单 | hrss |
| 385 | hrss_searchweighttree | 2KTCZXILM3VG |  |  | 搜索排序权重配置列表 | 动态表单 | hrss |
| 386 | hrss_selectgroup | 4JC1Q7KM5J=O |  |  | 设置分组 | 动态表单 | hrss |
| 387 | hrss_stopdts | 3ZXF1+AX6ECG |  |  | 停止DTS同步 | 动态表单 | hrss |

## 查询模型（27）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | haos_adminorgstructquery | 2P1WK4+OVXU8 |  |  | 行政组织结构 | 查询列表 | haos |
| 2 | hbss_hrbulist | 0G290HM=NQRJ |  |  | HR管理组织 | 查询列表 | hbss |
| 3 | hbss_hrbuquery | 0G/KAHMD8E9X |  |  | HR管理组织 | 查询列表 | hbss |
| 4 | hbss_hrbuviewlist | 0HLA05C0TQ6H |  |  | HR业务管理视图 | 查询列表 | hbss |
| 5 | hbss_hrbuviewquery | 0HL=5H+U=WFI |  |  | HR业务管理视图 | 查询列表 | hbss |
| 6 | hrcs_permfilelist | 0ROAUZBLXWIE |  |  | 用户授权 | 查询列表 | hrcs |
| 7 | hrcs_permfilequery | 0RMI67N5OR16 |  |  | 用户授权 | 查询列表 | hrcs |
| 8 | hrcs_promptlist | 1I/PEDD2NKXC |  |  | 提示语配置 | 查询列表 | hrcs |
| 9 | hrcs_promptquery | 1I/O9ME0YX0J |  |  | 提示语配置列表 | 查询列表 | hrcs |
| 10 | hrcs_rolelist | 0SXDNZK1PW66 |  |  | 角色管理 | 查询列表 | hrcs |
| 11 | hrcs_rolequery | 0SXDKS4AO4Q0 |  |  | 角色管理 | 查询列表 | hrcs |
| 12 | hrcs_workingplanlist | 23PWPQ=3+IW2 |  |  | 公共日历 | 查询列表 | hrcs |
| 13 | hrcs_workingplanquery | 23PVR0QBB6NV |  |  | 公共日历 | 查询列表 | hrcs |
| 14 | hrpi_assignf7querylist | 4S654B0NFJFJ |  |  | 组织分配 | 查询列表 | hrpi |
| 15 | hrpi_assignmentf7query | 4S57FXQUOYX5 |  |  | 组织分配 | 查询列表 | hrpi |
| 16 | hrpi_employeenewf7query | 4UR=Z37J/ADF |  |  | 员工 | 查询列表 | hrpi |
| 17 | hrpi_empnewf7querylist | 4URHJ3C9CICI |  |  | 员工 | 查询列表 | hrpi |
| 18 | hrpi_empposf7query | 4S5P7B1T8H42 |  |  | 任职经历 | 查询列表 | hrpi |
| 19 | hrpi_empposf7querylist | 4S64Z/DG27M5 |  |  | 任职经历 | 查询列表 | hrpi |
| 20 | hrpi_empposorgf7querylist | 4X5D5R6RDUTO |  |  | 任职经历 | 查询列表 | hrpi |
| 21 | hrpi_empposorgrellist | 26RBHFEOIG5I |  |  | 人员清单 | 查询列表 | hrpi |
| 22 | hrpi_empposorgrelquery | 26NWCKAUHC0E |  |  | 行政组织下人员清单 | 查询列表 | hrpi |
| 23 | hrpi_emptreef7querylist | 4URJQE8NTA3/ |  |  | 员工 | 查询列表 | hrpi |
| 24 | hrpi_joblist | 26UMKZ3YD2GX |  |  | 人员清单 | 查询列表 | hrpi |
| 25 | hrpi_jobquery | 26ULITPK3X+B |  |  | 人员清单 | 查询列表 | hrpi |
| 26 | hrpi_positionlist | 26UHLTK337HO |  |  | 人员清单 | 查询列表 | hrpi |
| 27 | hrpi_positionquery | 26UHDD9L1/QU |  |  | 岗位下人员清单 | 查询列表 | hrpi |

## 报表（1）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | haos_adminorgreporttree | 3DK7+UQTQJ1E |  |  | 行政组织树报表模板业务版本 | 报表 | haos |

## 移动端（14）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | haos_adminorgddmoblist | 345JHYEJ49MR |  |  | 行政组织下钻移动端列表 | 移动表单 | haos |
| 2 | haos_adminorgemptymoblist | 345/PT9BA2CR |  |  | 行政组织空移动端列表 | 移动表单 | haos |
| 3 | haos_adminorgflatmoblist | 341+7VS0J6T2 |  |  | 行政组织平铺移动端列表 | 移动表单 | haos |
| 4 | hbp_activityreject_mobi | 57FJF5QW5/XU |  |  | 驳回 | 移动表单 | hbp |
| 5 | hbp_activityterm_mobi | 57FMGDQVVZ=I |  |  | 终止 | 移动表单 | hbp |
| 6 | hbp_activitytransfer_mobi | 57FH5IDZZ2ZT |  |  | 转交 | 移动表单 | hbp |
| 7 | hbpm_posmoblisttabf7 | 5DRGZ0JRZ/RM |  |  | 岗位移动端F7 | 移动表单 | hbpm |
| 8 | hbss_commonlogin | 2=48ZZKB=NI2 |  |  | 通用登录 | 移动表单 | hbss |
| 9 | hbss_logintest | 2=DXHLUZ7IHG |  |  | 登录测试 | 移动表单 | hbss |
| 10 | hbss_privacy | 2IDFNUVW/+ZX |  |  | 隐私签署 | 移动表单 | hbss |
| 11 | hbss_privacydisable | 2BVU/N452==7 |  |  | 隐私禁用 | 移动表单 | hbss |
| 12 | hbss_privacysign | 2A3=+K1+R2P0 |  |  | 隐私签署 | 移动表单 | hbss |
| 13 | hbss_safeuriexpiry | 2=KI=60EVOG7 |  |  | 短链失效 | 移动表单 | hbss |
| 14 | hbss_sessiontimeout | 2AGIONSBAFRD |  |  | 会话超时 | 移动表单 | hbss |

## 其他（5）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | hbss_listdeletezero | 3DNAL0S2/TAU |  |  | 列表删除尾零模板 | ParameterFormModel_listoption | hbss |
| 2 | hbss_parameter | 1XE=P8E2BC/W |  |  | 中台服务云基础资料应用参数 | ParameterFormModel_application | hbss |
| 3 | hrcs_commonparams | 214KTVS4FFA2 |  |  | HR通用服务参数 | ParameterFormModel_application | hrcs |
| 4 | hrptmc_parameter | 3IJNJ1ONY7A7 |  |  | HR报表参数配置 | ParameterFormModel_application | hrptmc |
| 5 | hrss_parameterconfig | 3U7IYF/=U3NB |  |  | 智能能搜索参数配置 | ParameterFormModel_application | hrss |
