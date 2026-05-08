# HRMP 元数据引用对象清单

**扫描范围**: `D:\cosmic-dev\references\hcm\hrmp-entity-metadata\metadata`  
**元数据文件数**: 529  
**去重后引用对象总数**: 309

## 概览

| 分类 | 数量 | 说明 |
|------|------|------|
| 平台对象 | 19 | bos_/bd_ 前缀，不纳入ER图 |
| HR非主数据 | 67 | 基础资料-非主数据类，不纳入ER图 |
| HR代表元数据 | 175 | 已有元数据详情 |
| HR共享元数据 | 13 | 引用的是非代表元数据（F7/视图等） |
| HR未知引用 | 6 | HR前缀但不在HRMP实体清单中 |
| 其他未知 | 29 | 无法分类 |

---

## 1. 平台对象（已忽略）

| # | 引用对象 | 被引用次数 |
|---|---------|----------|
| 1 | `bd_admindivision` | 17 |
| 2 | `bd_bebank` | 1 |
| 3 | `bd_bizpartner` | 1 |
| 4 | `bd_country` | 30 |
| 5 | `bd_currency` | 4 |
| 6 | `bd_project` | 1 |
| 7 | `bos_assistantdata_detail` | 1 |
| 8 | `bos_devp_industry` | 2 |
| 9 | `bos_devportal_bizapp` | 34 |
| 10 | `bos_devportal_bizcloud` | 10 |
| 11 | `bos_entitymeta` | 2 |
| 12 | `bos_entityobject` | 33 |
| 13 | `bos_flex_property` | 1 |
| 14 | `bos_formmeta` | 1 |
| 15 | `bos_objecttype` | 11 |
| 16 | `bos_org` | 219 |
| 17 | `bos_org_biz` | 16 |
| 18 | `bos_org_pattern` | 1 |
| 19 | `bos_user` | 1167 |

---

## 2. HR非主数据引用（已忽略）

这些引用对象属于「基础资料-非主数据类」中的枚举/字典表和UI模板，不纳入ER图。

| # | 引用对象 | 被引用次数 | 引用来源（部分） |
|---|---------|----------|----------------|
| 1 | `haos_staffproject` | 2 | haos_staff.staffproject, haos_staffruleconfig.staffproject |
| 2 | `hbss_actiontype` | 1 | hbss_action.actiontype |
| 3 | `hbss_addresstype` | 2 | hcf_canaddress.addresstype, hrpi_peraddress.addresstype |
| 4 | `hbss_appointtype` | 2 | hrpi_appointremoverel.appointtype, hrpi_appointremoverel.dismisstype |
| 5 | `hbss_apptreasongroup` | 2 | hrpi_appointremoverel.apptreasonggroup, hrpi_appointremoverel.dismissreason |
| 6 | `hbss_bloodtype` | 2 | hcf_candidate.nbloodtype, hrpi_employee.nbloodtype |
| 7 | `hbss_bussinessfield` | 6 | hbss_actiontype.businessdomain, hbss_empgroup.bussinessfield, hrcs_bussinesstype.bussinesstype ...共6处 |
| 8 | `hbss_cadrecategory` | 2 | hrpi_appointremoverel.cadrecat, hrpi_empcadre.cadretype |
| 9 | `hbss_capacityrankscheme` | 1 | hbss_capacityitem.caprankscheme |
| 10 | `hbss_category` | 2 | hcf_personalarea.regresidencenature, hrpi_perregion.regresidencenature |
| 11 | `hbss_constellation` | 2 | hcf_candidate.constellation, hrpi_employee.constellation |
| 12 | `hbss_contracttypecat` | 1 | hbss_contracttypes.group |
| 13 | `hbss_contracttypes` | 1 | hrpi_contractinfo.contracttype |
| 14 | `hbss_credentialstype` | 7 | hcf_cancre.credentialstype, hrcs_contractest.identitytype, hrpi_blacklist.cardtype ...共7处 |
| 15 | `hbss_cycletype` | 1 | hbss_timestamp.cycletype |
| 16 | `hbss_degree` | 2 | hcf_caneduexp.degree, hrpi_pereduexp.degree |
| 17 | `hbss_depcytype` | 1 | hbjm_jobhr.depcytype |
| 18 | `hbss_diploma` | 6 | hbjm_jobhr.diplomareq, hbpm_positionhr.diplomareq, hcf_caneduexp.education ...共6处 |
| 19 | `hbss_diplomatype` | 2 | hcf_caneduexp.edunature, hrpi_pereduexp.edunature |
| 20 | `hbss_disptype` | 1 | hrpi_dispatchinfo.disptype |
| 21 | `hbss_educerttype` | 2 | hcf_caneduexp.certtype, hrpi_pereduexp.certtype |
| 22 | `hbss_emergcontactype` | 2 | hcf_cancontact.emergcontactype, hrpi_emrgcontact.emergcontactype |
| 23 | `hbss_entitytype` | 1 | hbss_relatepanelset.entitytype |
| 24 | `hbss_familiarity` | 10 | hcf_canlgability.listen, hcf_canlgability.read, hcf_canlgability.speak ...共10处 |
| 25 | `hbss_familymemberrel` | 2 | hcf_canfamily.familymembship, hrpi_familymemb.familymembship |
| 26 | `hbss_flok` | 4 | hcf_cancre.folk, hcf_candidate.folk, hrpi_employee.folk ...共4处 |
| 27 | `hbss_healthstatus` | 2 | hcf_candidate.healthstatus, hrpi_employee.healthstatus |
| 28 | `hbss_hrbucafunc` | 6 | hrcs_admingroupfunc.bucafunc, hrcs_dimension.hrbu, hrcs_dynaformctrl.bucafunc ...共6处 |
| 29 | `hbss_industrytype` | 6 | haos_adminorg.industrytype, haos_adminorgcompany.industrytype, hcf_canprework.trade ...共6处 |
| 30 | `hbss_laborrelstatus` | 1 | hrpi_empentrel.laborrelstatus |
| 31 | `hbss_laborreltype` | 6 | haos_dimstaffreport.laborreltype, haos_muldimendetail.laborreltype, haos_muldimendetailhis.laborreltype ...共6处 |
| 32 | `hbss_laborreltypecls` | 1 | hbss_laborreltype.laborreltypecls |
| 33 | `hbss_languagecert` | 2 | hcf_canlgability.languagecert, hrpi_perlgability.languagecert |
| 34 | `hbss_languagetype` | 2 | hcf_canlgability.language, hrpi_perlgability.language |
| 35 | `hbss_marriagestatus` | 2 | hcf_candidate.marriagestatus, hrpi_employee.marriagestatus |
| 36 | `hbss_nationality` | 6 | hcf_cancre.nationality, hcf_candidate.nationality, hrpi_blacklist.nation ...共6处 |
| 37 | `hbss_ocpqual` | 2 | hcf_canocpqual.qualification, hrpi_perocpqual.qualification |
| 38 | `hbss_ocpquallevel` | 3 | hcf_canocpqual.qualevel, hrpi_perocpqual.qualevel, hrpi_perpractqual.qualevel |
| 39 | `hbss_onboardsource` | 1 | hrpi_empentrel.onboardsource |
| 40 | `hbss_operationqual` | 1 | hrpi_perpractqual.qualification |
| 41 | `hbss_party` | 2 | hcf_personalarea.party, hrpi_perregion.party |
| 42 | `hbss_patentscategory` | 2 | hcf_rsmpatinv.patentcategoryid, hrpi_rsmpatinv.patentcategoryid |
| 43 | `hbss_patentstatus` | 2 | hcf_rsmpatinv.patentstatusid, hrpi_rsmpatinv.patentstatusid |
| 44 | `hbss_payrollacrelation` | 2 | hcf_canbankcard.accountrelation, hrpi_debardinfo.relation |
| 45 | `hbss_perflevel` | 1 | hrpi_perfresult.perflevel |
| 46 | `hbss_politicalstatus` | 4 | hcf_canfamily.politicalstatus, hcf_personalarea.politicalstatus, hrpi_familymemb.politicalstatus ...共4处 |
| 47 | `hbss_postcategory` | 1 | hbss_postype.postcategory |
| 48 | `hbss_poststate` | 2 | hrpi_appointremoverel.posstatus, hrpi_empposorgrel.posstatus |
| 49 | `hbss_poststatecls` | 1 | hbss_poststate.poststatecls |
| 50 | `hbss_postype` | 2 | hrpi_appointremoverel.postype, hrpi_empposorgrel.postype |
| 51 | `hbss_privacyusertype` | 1 | hbss_privacysigning.usertype |
| 52 | `hbss_procreatmode` | 1 | hrpi_fertilityinfo.procreatmode |
| 53 | `hbss_projecttype` | 2 | hcf_canprojectexp.projecttype, hrpi_empproexp.projecttype |
| 54 | `hbss_protitlelevel` | 1 | hrpi_perprotitle.prolevel |
| 55 | `hbss_protitletype` | 1 | hbss_protitle.protitletype |
| 56 | `hbss_religion` | 2 | hcf_personalarea.religion, hrpi_perregion.religion |
| 57 | `hbss_rewpnmlevel` | 1 | hrpi_perrprecord.level |
| 58 | `hbss_rewpnmtype` | 1 | hrpi_perrprecord.type |
| 59 | `hbss_rolesourcetype` | 1 | hbpm_positionrelation.sourcetype |
| 60 | `hbss_rotationtype` | 1 | hrpi_rotationinfo.rottype |
| 61 | `hbss_sex` | 6 | hcf_cancre.gender, hcf_candidate.gender, hrpi_blacklist.gender ...共6处 |
| 62 | `hbss_supplierlevel` | 1 | hbss_supplier.supplierlevel |
| 63 | `hbss_timelimittype` | 1 | hrpi_contractinfo.periodtype |
| 64 | `hbss_trainmode` | 2 | hcf_cantraining.trainmode, hrpi_emptrainfile.trainmode |
| 65 | `hbss_traintype` | 2 | hcf_cantraining.traintype, hrpi_emptrainfile.traintype |
| 66 | `hbss_zodiac` | 2 | hcf_candidate.symbolicanimals, hrpi_employee.symbolicanimals |
| 67 | `hrcs_privacyusertype` | 1 | hbss_loginconfig.usertype |

---

## 3. HR代表元数据引用（已有详情）

这些引用对象本身就是代表元数据，已有元数据详情页，可直接用于ER图。

| # | 引用对象 | 被引用次数 | 已有详情 | 引用来源（部分） |
|---|---------|----------|---------|----------------|
| 1 | `brm_ruledesign` | 1 | Y | hrcs_labelpolicyrule.brmrule |
| 2 | `brm_scene` | 11 | Y | brm_ruledesign.scene, brm_rulelist.rulescene, brm_target.scene ...共11处 |
| 3 | `brm_sceneinput` | 1 | Y | hrcs_labelparam.brmiputparam |
| 4 | `brm_target` | 1 | Y | hrcs_labelvaluerule.brmtarget |
| 5 | `haos_adminorgfunction` | 1 | Y | haos_adminorg.adminorgfunction |
| 6 | `haos_adminorglayer` | 1 | Y | haos_adminorg.adminorglayer |
| 7 | `haos_adminorgtype` | 1 | Y | haos_adminorg.adminorgtype |
| 8 | `haos_adminorgtypestd` | 1 | Y | haos_adminorgtype.adminorgtypestd |
| 9 | `haos_changeoperat` | 1 | Y | haos_adminorg_msgdetail.changeoperate |
| 10 | `haos_changescene` | 1 | Y | haos_adminorg_msgdetail.changescene |
| 11 | `haos_changesource` | 1 | Y | haos_chargeperson.changesource |
| 12 | `haos_companytype` | 1 | Y | haos_adminorgcompany.companytype |
| 13 | `haos_dutyorgdetail` | 1 | Y | haos_dutyorgdetailhis.dutyorgdetail |
| 14 | `haos_dutyorgdetailhis` | 1 | Y | haos_dutyorgdetail.vid |
| 15 | `haos_muldimendetail` | 2 | Y | haos_muldimendetailhis.muldimendetail, haos_muldimusestaff.muldimendetail |
| 16 | `haos_muldimendetailhis` | 1 | Y | haos_muldimendetail.vid |
| 17 | `haos_orgchangetype` | 1 | Y | haos_changescene.orgchangetype |
| 18 | `haos_orgusestaffdetail` | 2 | Y | haos_muldimusestaff.orgusestaffdetail, haos_personchangeevent.orgusestaffdetail |
| 19 | `haos_othrole` | 1 | Y | haos_othemproleorgrel.otherrole |
| 20 | `haos_othroletpl` | 1 | Y | haos_othrole.sourcetpl |
| 21 | `haos_personstaffinfo` | 4 | Y | haos_muldimusestaff.personstaffinfo, haos_orgpersonstaffinfo.personstaffinfo, haos_orgusestaffdetail.personstaffinfo ...共4处 |
| 22 | `haos_staff` | 4 | Y | haos_dutyorgdetail.staff, haos_muldimendetail.staff, haos_staffruleconfig.refstaff ...共4处 |
| 23 | `haos_staffactivity` | 1 | Y | haos_personchangeevent.changeevent |
| 24 | `haos_staffactivitytype` | 2 | Y | haos_personchangeevent.eventtype, haos_staffactivity.staffactivitytype |
| 25 | `haos_staffcycle` | 2 | Y | haos_staff.staffcycle, haos_staffruleconfig.staffcycle |
| 26 | `haos_staffflex` | 1 | Y | haos_staffflex_bd.hg |
| 27 | `haos_structtype` | 1 | Y | haos_othroletpl.structtype |
| 28 | `haos_structure` | 2 | Y | haos_adminorgstruct.structproject, haos_othorgstruct.structproject |
| 29 | `haos_teamcoopreltype` | 1 | Y | haos_orgteamcooprel.coopreltype |
| 30 | `haos_useorgdetail` | 2 | Y | haos_orgusestaffdetail.useorgdetail, haos_useorgdetailhis.useorgdetail |
| 31 | `haos_useorgdetailhis` | 1 | Y | haos_useorgdetail.vid |
| 32 | `hbjm_jobclasshr` | 3 | Y | hbjm_jobclasshr.parent, hbjm_jobhr.jobclass, hrpi_empjobrel.jobclass |
| 33 | `hbjm_jobfamilyhr` | 3 | Y | hbjm_jobclasshr.jobfamily, hbjm_jobhr.jobfamily, hrpi_empjobrel.jobfamily |
| 34 | `hbjm_jobgradehr` | 9 | Y | hbjm_jobgradehr.corjobgrade, hbjm_jobhr.highjobgrade, hbjm_jobhr.lowjobgrade ...共9处 |
| 35 | `hbjm_jobgradescmhr` | 5 | Y | hbjm_jobhr.jobgradescm, hbpm_positionhr.jobgradescm, hbpm_positiontpl.jobgradescm ...共5处 |
| 36 | `hbjm_jobhr` | 18 | Y | haos_chargeperson.job, haos_dimstaffreport.job, haos_muldimendetail.job ...共18处 |
| 37 | `hbjm_joblevelhr` | 12 | Y | haos_muldimendetail.joblevel, haos_muldimendetailhis.joblevel, haos_orgpersonstaffinfo.joblevel ...共12处 |
| 38 | `hbjm_joblevelscmhr` | 5 | Y | hbjm_jobhr.joblevelscm, hbpm_positionhr.joblevelscm, hbpm_positiontpl.joblevelscm ...共5处 |
| 39 | `hbjm_jobscmhr` | 4 | Y | hbpm_positionhr.jobscm, hbpm_positiontpl.jobscm, hrpi_empjobrel.jobscm ...共4处 |
| 40 | `hbjm_jobseqhr` | 4 | Y | hbjm_jobclasshr.jobseq, hbjm_jobfamilyhr.jobseq, hbjm_jobhr.jobseq ...共4处 |
| 41 | `hbjm_jobtype` | 1 | Y | hbjm_jobhr.jobtype |
| 42 | `hbjm_standardjobseqhr` | 1 | Y | hbjm_jobseqhr.standardjobseq |
| 43 | `hbp_calresultitem` | 1 | Y | hbp_formula_unittest.resultitem |
| 44 | `hbp_devportal_bizapp` | 19 | Y | brm_scene.bizappid, brm_target.bizapp, haos_structproconfig.bizapp ...共19处 |
| 45 | `hbp_entityobject` | 23 | Y | brm_sceneinput.basedatafield, brm_sceneinput.paramsobject, hrcs_chgeventblacklist.verifbilltype ...共23处 |
| 46 | `hbp_unittesthistpl01` | 8 | Y | hbp_unittesthisbugrp01.group, hbp_unittesthisgrp01.group, hbp_unittesthisoritpl01.bof7 ...共8处 |
| 47 | `hbp_unittesttime01` | 1 | Y | hbp_unittesttime01.timeline01 |
| 48 | `hbpm_changeoperate` | 4 | Y | hbpm_chgrecord.changeoperate, hbpm_chgrecorddetail.changeoperate, hbpm_position_msgdetail.changeoperate ...共4处 |
| 49 | `hbpm_changereason` | 4 | Y | hbpm_chgrecord.changereason, hbpm_chgrecord.searchchangereason, hbpm_chgrecordevt.changereason ...共4处 |
| 50 | `hbpm_changescene` | 4 | Y | hbpm_chgrecord.changescene, hbpm_chgrecordevt.changescene, hbpm_position_msgdetail.changescene ...共4处 |
| 51 | `hbpm_changetype` | 4 | Y | hbpm_changescene.changetype, hbpm_chgrecord.changetype, hbpm_chgrecordevt.changetype ...共4处 |
| 52 | `hbpm_positiontpl` | 1 | Y | hbpm_positionhr.positiontpl |
| 53 | `hbpm_positiontpltype` | 1 | Y | hbpm_positiontpl.posttpltype |
| 54 | `hbpm_positiontype` | 2 | Y | hbpm_positionhr.positiontype, hbpm_positiontpl.positiontype |
| 55 | `hbpm_reportcoreltype` | 2 | Y | hbpm_positionrelation.reportingtype, hrpi_empsuprel.reporttype |
| 56 | `hbss_capacitygroup` | 3 | Y | hbss_capacitygroup.parent, hbss_capacitygroup.parentdim, hbss_capacityitem.group |
| 57 | `hbss_cloud_app` | 1 | Y | hrptmc_unitestentity01.entrybasedata |
| 58 | `hbss_college` | 4 | Y | hbp_unittesttime01.college, hcf_caneduexp.graduateschool, hrpi_pereduexp.graduateschool ...共4处 |
| 59 | `hbss_companyscale` | 2 | Y | hcf_canprework.companyscale, hrpi_preworkexp.companyscale |
| 60 | `hbss_costcenter` | 1 | Y | hbss_costcenter.parent |
| 61 | `hbss_employeegroup` | 2 | Y | hrpi_assignment.persongroup, hrpi_assignmentmag.persongroup |
| 62 | `hbss_empnature` | 4 | Y | hcf_canprework.businesstypeid, hcf_canprojectexp.companynature, hrpi_empproexp.companynature ...共4处 |
| 63 | `hbss_enterprise` | 1 | Y | hrpi_empentrel.enterprise |
| 64 | `hbss_hrbuca` | 1 | Y | hbss_hrbuca.parent |
| 65 | `hbss_hrbusinessfield` | 5 | Y | hbp_datagrade_unittest.businessfield, hbss_bussinessfield.group, hrptmc_virtentityclass.businessfield ...共5处 |
| 66 | `hbss_lawentity` | 13 | Y | haos_adminorg.corporateorg, hbss_enterprise.lawentity, hbss_signcompany.lawentity ...共13处 |
| 67 | `hbss_loginscene` | 1 | Y | hbss_loginconfig.loginscene |
| 68 | `hbss_procreatstatus` | 2 | Y | hcf_candidate.procreatstatus, hrpi_employee.procreatstatus |
| 69 | `hbss_protitle` | 1 | Y | hrpi_perprotitle.professional |
| 70 | `hbss_safeuriconfig` | 1 | Y | hbss_safeuri.safeuriconfig |
| 71 | `hbss_scoreinterval` | 1 | Y | hbss_scoresystem.scoreinterval |
| 72 | `hbss_scoresystem` | 1 | Y | hrpi_perfresult.rulescore |
| 73 | `hbss_signcompany` | 1 | Y | hrpi_contractinfo.signcompany |
| 74 | `hbss_taxunit` | 1 | Y | hrpi_employeetaxcn.taxunit |
| 75 | `hbss_workplace` | 5 | Y | haos_adminorg.workplace, hbpm_positionhr.workplace, hrpi_dispatchinfo.dispworkplace ...共5处 |
| 76 | `hcf_candidate` | 21 | Y | haos_orgpersonstaffinfo.candidate, haos_personstaffinfo.candidate, hcf_canaddress.candidate ...共21处 |
| 77 | `hrcs_activity` | 5 | Y | hrcs_activityenablerec.activity, hrcs_activitygroupins.activity, hrcs_activityins.activity ...共5处 |
| 78 | `hrcs_activityins` | 2 | Y | hrcs_actassignrec.activityins, hrcs_activityexception.activityins |
| 79 | `hrcs_activityscheme` | 1 | Y | hrcs_activityins.actscheme |
| 80 | `hrcs_activitytype` | 1 | Y | hrcs_activity.activitytype |
| 81 | `hrcs_bgtaskregister` | 1 | Y | hrcs_bgtaskrecord.bgtaskregister |
| 82 | `hrcs_businessobject` | 1 | Y | hrcs_bussinesstype.bussinessobject |
| 83 | `hrcs_bussinesstype` | 5 | Y | hrcs_empstrategy.bussinessfield, hrcs_orgstrategy.bussinessfield, hrcs_projempstrategy.bussinessfield ...共5处 |
| 84 | `hrcs_commonvariable` | 1 | Y | hrcs_keywordmapping.variableid |
| 85 | `hrcs_coordbizfield` | 1 | Y | hrcs_coordfieldrule.coordbizfield |
| 86 | `hrcs_coordbizfieldgrp` | 1 | Y | hrcs_coordbizfield.group |
| 87 | `hrcs_coordbizobject` | 4 | Y | hrcs_coordappcfg.coordbizobject, hrcs_coordbizfieldgrp.coordbizobject, hrcs_coordrulesch.coordbizobject ...共4处 |
| 88 | `hrcs_coordrulesch` | 2 | Y | hrcs_coordappcfg.coordrulesch, hrcs_coordfieldrule.coordrulesch |
| 89 | `hrcs_coordstrategy` | 2 | Y | hrcs_coordappcfg.coordstrategy, hrcs_coordstrategylog.coordstrategy |
| 90 | `hrcs_coordstrategylog` | 1 | Y | hrcs_coordstrategy.currentlog |
| 91 | `hrcs_datarule` | 8 | Y | hrcs_dynaschdatarule.bddatarule, hrcs_dynaschdatarule.datarule, hrcs_perminitrecord.bd_datarule ...共8处 |
| 92 | `hrcs_dimension` | 12 | Y | hrcs_dynaschdimgrp.dimension, hrcs_dynaschexdimgrp.dimension, hrcs_entityctrl.dimension ...共12处 |
| 93 | `hrcs_dynaauthobject` | 2 | Y | hrcs_dynascheme.assignpersonitem, hrcs_dynascheme.cancelpersonitem |
| 94 | `hrcs_dynacond` | 5 | Y | hrcs_dynaschdimgrp.dynacond, hrcs_perminitrecord.dim_dynacond, hrcs_perminitrecord.rdata_dynacond ...共5处 |
| 95 | `hrcs_dynaruleitem` | 1 | Y | hrcs_dynaruleitem.relatruleparam |
| 96 | `hrcs_dynascheme` | 10 | Y | hrcs_dynamsgdealtrace.calparam_scheme, hrcs_dynamsgdealtrace.scheme, hrcs_dynaschdatarule.scheme ...共10处 |
| 97 | `hrcs_econtemplate` | 2 | Y | hrcs_contractest.econttmp, hrcs_signflow.econttmp |
| 98 | `hrcs_esignappcfg` | 5 | Y | hrcs_esigncoauth.authapp, hrcs_esigncoseal.esignapp, hrcs_esignspmgr.bdesignappcfg ...共5处 |
| 99 | `hrcs_esigncoseal` | 1 | Y | hrcs_esignsealauth.seal |
| 100 | `hrcs_esignsealtype` | 2 | Y | hrcs_econtemplate.sealtypeid, hrcs_esigncoseal.sealtype |
| 101 | `hrcs_esignspmgr` | 4 | Y | hrcs_esignappcfg.esignsp, hrcs_esigncoauth.esignspmgr, hrcs_esigncoseal.esignsp ...共4处 |
| 102 | `hrcs_essyncschemecfig` | 1 | Y | hrcs_essyncrecord.esscheme |
| 103 | `hrcs_functiontype` | 1 | Y | hrcs_function.group |
| 104 | `hrcs_label` | 8 | Y | hrcs_labelobjectrel.label, hrcs_labelparam.label, hrcs_labelpolicyrule.label ...共8处 |
| 105 | `hrcs_labelgroup` | 2 | Y | hrcs_label.group, hrcs_labelgroup.parent |
| 106 | `hrcs_labelobject` | 12 | Y | hrcs_label.labelobject, hrcs_labelobject.parent, hrcs_labelobjectrel.labelobject ...共12处 |
| 107 | `hrcs_labelvalue` | 4 | Y | hrcs_labelparam.labelvalue, hrcs_labelpolicyrule.labelvalue, hrcs_labelvaluerule.labelvalue ...共4处 |
| 108 | `hrcs_lbljoinentity` | 2 | Y | hrcs_lblentityrelation.entityid, hrcs_lblentityrelation.joinentityid |
| 109 | `hrcs_lblstrategy` | 3 | Y | hrcs_labelpolicyrule.labelpolicy, hrcs_labelpolicytask.labelpolicy, hrcs_lblstrategyfilter.lblstrategy |
| 110 | `hrcs_permfilegrp` | 3 | Y | hrcs_permfilegrp.parent, hrcs_permfilegrpmember.permfilegrp, hrcs_userpermfile.permfilegrp |
| 111 | `hrcs_permfilegrpmember` | 1 | Y | hrcs_userpermfile.permfilegrpmember |
| 112 | `hrcs_perminitrecord` | 1 | Y | hrcs_userrolerelat.initrecord |
| 113 | `hrcs_prompt` | 1 | Y | hrcs_promptrule.entryprompt |
| 114 | `hrcs_querydynsourcelist` | 1 | Y | hrcs_essyncschemecfig.queryentity |
| 115 | `hrcs_role` | 5 | Y | hrcs_dynascheme.hrcsrole, hrcs_dynaschfield.role, hrcs_roledatarule.role ...共5处 |
| 116 | `hrcs_rolegrp` | 1 | Y | hrcs_role.rolegrp |
| 117 | `hrcs_strategy` | 8 | Y | hrcs_empstrategy.defstrategytype, hrcs_empstrategy.entrydefstrategy, hrcs_orgstrategy.defstrategytype ...共8处 |
| 118 | `hrcs_userpermfile` | 4 | Y | hrcs_permapplybill.assign_permfile, hrcs_permapplybill.cancel_permfile, hrcs_permfilegrpmember.permfile ...共4处 |
| 119 | `hrcs_userrolerelat` | 5 | Y | hrcs_userdatarule.userrolerelate, hrcs_userfield.userrolerealt, hrcs_userrole.userrolerealt ...共5处 |
| 120 | `hrcs_varmappingscene` | 1 | Y | hrcs_commonvariable.varmappingscene |
| 121 | `hrcs_warncalfield` | 1 | Y | hrcs_warncalfield.refcalfield |
| 122 | `hrcs_warnmsgpersist` | 1 | Y | hrcs_warnmsgrowdata.msgpersist |
| 123 | `hrcs_warnobjtpl` | 1 | Y | hrcs_warnscene.warnobjtpl |
| 124 | `hrcs_warnpluginservice` | 1 | Y | hrcs_warncalfield.pluginservice |
| 125 | `hrcs_warnscene` | 1 | Y | hrcs_warnscheme.warnscene |
| 126 | `hrcs_warnscenejoinentity` | 2 | Y | hrcs_warnsceneentityrel.entityid, hrcs_warnsceneentityrel.joinentityid |
| 127 | `hrcs_warnscheme` | 1 | Y | hrcs_warnmsgpersist.warnscheme |
| 128 | `hrcs_workingplanquery` | 1 | Y | hrpi_empposorgrel.workcalendar |
| 129 | `hrpi_appointremoverel` | 1 | Y | hrpi_empcadre.appremoverel |
| 130 | `hrpi_assignment` | 16 | Y | hrpi_appointremoverel.assignment, hrpi_assignment.primaryassignment, hrpi_assignmentmag.assignment ...共16处 |
| 131 | `hrpi_assignmentf7query` | 2 | Y | hrpi_blacklist.assignment, hrpi_employeetaxcn.assignment |
| 132 | `hrpi_empjobrel` | 1 | Y | hrpi_empjobrel.beforeadjempjobrel |
| 133 | `hrpi_employee` | 6 | Y | haos_chargeperson.employee, haos_orgpersonstaffinfo.employee, haos_personstaffinfo.employee ...共6处 |
| 134 | `hrpi_employeenewf7query` | 46 | Y | haos_othemproleorgrel.employee, haos_staffcase.employee, hrcs_contractest.person ...共46处 |
| 135 | `hrpi_empposf7query` | 3 | Y | haos_staffcase.empposorgrel, hrpi_empsuprel.empposorgrel, hrpi_empsuprel.superiorempposorgrel |
| 136 | `hrpi_empposorgrel` | 7 | Y | haos_chargeperson.empposorgrel, haos_orgpersonstaffinfo.empposorgrel, haos_orgusestaffdetail.empposorgrel ...共7处 |
| 137 | `hrpi_empstage` | 13 | Y | hrpi_appointremoverel.empstage, hrpi_assignment.empstage, hrpi_assignmentmag.empstage ...共13处 |
| 138 | `hrpi_globalperson` | 1 | Y | hrpi_employee.globalperson |
| 139 | `hrpi_quittype` | 2 | Y | hrpi_blacklist.quittypeid, hrpi_terminationinfo.termtype |
| 140 | `hrpi_toblacklistreason` | 2 | Y | hrpi_blacklist.toreason, hrpi_blacklist.toreasonview |
| 141 | `hrptmc_anobjgroupfield` | 2 | Y | hrptmc_anobjsidebar.groupfield, hrptmc_filter.groupfield |
| 142 | `hrptmc_anobjjoinentity` | 2 | Y | hrptmc_anobjentityrel.entityid, hrptmc_anobjentityrel.joinentityid |
| 143 | `hrptmc_busiservice` | 1 | Y | hrptmc_preindex.service |
| 144 | `hrptmc_calculatefield` | 5 | Y | hrptmc_anobjgroupfield.anobjcalfield, hrptmc_calculatefield.refcalfield, hrptmc_calmaxlen.calfield ...共5处 |
| 145 | `hrptmc_colfield` | 4 | Y | hrptmc_colcustomsort.id, hrptmc_rptdispscmcol.colfield, hrptmc_rptdisscmety.colfield ...共4处 |
| 146 | `hrptmc_commonsort` | 1 | Y | hrptmc_rptcomref.commonsort |
| 147 | `hrptmc_esindex` | 3 | Y | hrptmc_esmapping.esindex, hrptmc_mysubscribe.esindex, hrptmc_sharerecord.esindex |
| 148 | `hrptmc_preindex` | 4 | Y | hrptmc_calculatefield.refpreindex, hrptmc_dimmap.preindex, hrptmc_reportpreindex.preindex ...共4处 |
| 149 | `hrptmc_queryscheme` | 2 | Y | hrptmc_queryscmchg.scheme, hrptmc_share_filterscheme.scheme |
| 150 | `hrptmc_reportconfig` | 1 | Y | hrptmc_algorithmcol.rptconfig |
| 151 | `hrptmc_reportmanage` | 27 | Y | hrptmc_calculatefield.report, hrptmc_colfield.rptmanage, hrptmc_customsort.rptmanage ...共27处 |
| 152 | `hrptmc_reportmark` | 1 | Y | hrptmc_rptmarkcontent.rptmark |
| 153 | `hrptmc_rowfield` | 10 | Y | hrptmc_rowcustomsort.id, hrptmc_rowfield.dataformat, hrptmc_rowfield.parentid ...共10处 |
| 154 | `hrptmc_rptdispscm` | 1 | Y | hrptmc_rptdisscmety.parent |
| 155 | `hrptmc_rptdisscmety` | 3 | Y | hrptmc_rptdispscmcol.rptdispscm, hrptmc_rptdispscmidx.rptdispscm, hrptmc_rptdispscmrow.rptdispscm |
| 156 | `hrptmc_selparam` | 1 | Y | hrptmc_preindex.selparam |
| 157 | `hrptmc_sharerecord` | 1 | Y | hrptmc_sharefilterrange.sharerecord |
| 158 | `hrptmc_splitdate` | 1 | Y | hrptmc_filter.splitdate |
| 159 | `hrptmc_subscriberecord` | 2 | Y | hrptmc_mysubscribe.subscriberecord, hrptmc_sharerecord.reportsubscribe |
| 160 | `hrptmc_unitestentity01` | 1 | Y | hrptmc_unitestentity02.report01 |
| 161 | `hrptmc_userdispscm` | 1 | Y | hrptmc_sharefilterrange.userdissche |
| 162 | `hrptmc_virtentityclass` | 1 | Y | hrptmc_virtualentity.classpath |
| 163 | `hrptmc_virtualentity` | 1 | Y | hrptmc_filesourcetable.virtualobj |
| 164 | `hrptmc_virtualfieldgroup` | 2 | Y | hrptmc_virtentfields.group, hrptmc_virtualentity.group |
| 165 | `hrptmc_workreport` | 11 | Y | hrptmc_colfield.workrpt, hrptmc_customsort.workrpt, hrptmc_dimmap.workrpt ...共11处 |
| 166 | `hrss_aiwordcategory` | 1 | Y | hrss_searchweight.aiwordcategory |
| 167 | `hrss_customfilter` | 1 | Y | hrss_searchscene.customfilter |
| 168 | `hrss_essyncscheme` | 1 | Y | hrss_essynrecord.essyncschemeid |
| 169 | `hrss_scenefiltergroup` | 1 | Y | hrss_searchscene.group |
| 170 | `hrss_schobjjoinentity` | 4 | Y | hrss_schobjentityrel.entityid, hrss_schobjentityrel.joinentityid, hrss_searchscene.searchobjentityid ...共4处 |
| 171 | `hrss_schobjqueryfield` | 2 | Y | hrss_searchscene.queryfield, hrss_searchscene.searchobjfield |
| 172 | `hrss_searchconfig` | 2 | Y | hrss_userlastsearchkey.searchpage, hrss_usersearchlog.searchpage |
| 173 | `hrss_searchobject` | 6 | Y | hrss_essyncscheme.searchobj, hrss_schobjentityrel.searchobj, hrss_schobjjoinentity.searchobj ...共6处 |
| 174 | `hrss_searchscene` | 4 | Y | hrss_searchweight.usescene, hrss_searchwtgrade.usescene, hrss_userlastsearchkey.searchscene ...共4处 |
| 175 | `hrss_searchwgentries` | 1 | Y | hrss_searchweight.grade |

---

## 4. HR共享元数据引用（需补充详情）

这些是非代表元数据（F7/视图/历史等共享元数据），被其他实体引用。
ER图应使用对应的**代表元数据**来关联。

| # | 引用对象（共享） | 代表元数据 | 被引用次数 | 代表元数据已有详情 |
|---|-----------------|-----------|----------|------------------|
| 1 | `brm_policy_edit` | `brm_rulelist` | 5 | Y |
| 2 | `haos_adminorgdetail` | `haos_adminorg` | 1 | Y |
| 3 | `haos_adminorghrf7` | `haos_adminorg` | 123 | Y |
| 4 | `haos_adminorgteam` | `haos_adminorg` | 1 | Y |
| 5 | `haos_dynamicdimension` | `haos_combdimension` | 3 | Y |
| 6 | `haos_otclassify` | `haos_structtype` | 13 | Y |
| 7 | `haos_othadminorg` | `haos_adminorg` | 2 | Y |
| 8 | `haos_structproject` | `haos_structure` | 11 | Y |
| 9 | `hbp_funcprementity` | `hbp_entityobject` | 2 | Y |
| 10 | `hbpm_positionhrf7` | `hbpm_positionhr` | 27 | Y |
| 11 | `hrcs_lblobjectfield` | `hrcs_labeldimension` | 4 | Y |
| 12 | `hrptmc_analyseobject` | `hrptmc_anobjtemplib` | 15 | Y |
| 13 | `hrptmc_anobjqueryfield` | `hrptmc_anobjfield_f7` | 51 | Y |

---

## 5. HR领域未知引用

这些引用有HR前缀但不在HRMP实体清单中，可能来自其他HR云。

| # | 引用对象 | 被引用次数 | 引用来源（部分） |
|---|---------|----------|----------------|
| 1 | `hpfs_chgaction` | 4 | hrcs_dynamsgdealtrace.chgaction, hrcs_permapplybill.chgaction, hrpi_empjobrel.chgaction ...共4处 |
| 2 | `hpfs_chgcategory` | 4 | hrcs_dynamsgdealtrace.chgcategory, hrcs_dynascheme.assignactype, hrcs_dynascheme.cancelactype ...共4处 |
| 3 | `hpfs_chgevent` | 3 | hrcs_chgeventblacklist.chgevent, hrcs_dynamsgdealtrace.chgevent, hrcs_permapplybill.chgevent |
| 4 | `hpfs_chgreason` | 3 | hrpi_blacklist.quitreasonid, hrpi_dispatchinfo.dispreason, hrpi_empposorgrel.chgreason |
| 5 | `hpfs_chgrecord` | 1 | hrcs_permapplybill.chgrecord |
| 6 | `hrcs_econtemplatetype` | 2 | hrcs_contractest.templatetype, hrcs_signflow.templatetypeid |

---

## 6. 其他未知引用

| # | 引用对象 | 被引用次数 | 引用来源（部分） |
|---|---------|----------|----------------|
| 1 | `bas_extenderp` | 4 | hrcs_ssctask.extenderp, hrcs_ssctaskhis.extenderp, hrpi_ssctask.extenderp ...共4处 |
| 2 | `cts_address` | 1 | hbss_addressdetail.detailaddr |
| 3 | `cts_personal_identity` | 1 | hbss_credentialstype.certnumformat |
| 4 | `cts_querydynsourcelist` | 1 | hrcs_esconfigwhitebill.queryentity |
| 5 | `evt_event` | 2 | hrcs_dynamsgdealtrace.message, hrcs_permapplybill.message |
| 6 | `evt_subscription` | 2 | hrcs_dynamsgdealtrace.subscriber, hrcs_permapplybill.subscriber |
| 7 | `gai_prompt` | 1 | hrss_searchscene.gptprompt |
| 8 | `gai_repo_info` | 1 | hrss_labelgptsync.gairepoid |
| 9 | `hrptc_reportgroup` | 1 | hrptmc_subscriberecord.reportgroup |
| 10 | `inte_language` | 3 | hbss_privacysigning.locale, hrcs_promptcontent.locale, hrcs_promptimport.locale |
| 11 | `inte_timezone` | 1 | hrcs_warnscheme.timezone |
| 12 | `perm_admingroup` | 10 | hrcs_admingroupfile.admingroup, hrcs_admingroupfunc.admingroup, hrcs_admingrouporg.admingroup ...共10处 |
| 13 | `perm_encryptionscheme` | 1 | hrcs_encapiregister.encryptscheme |
| 14 | `perm_permitem` | 7 | hrcs_dynaschdatarule.permitem, hrcs_entityforbid.permitem, hrcs_perminitrecord.rfunc_permitem ...共7处 |
| 15 | `perm_role` | 20 | hrcs_checkuserrolesyn.hmp_role, hrcs_checkuserrolesyn.sys_role, hrcs_dynaschdatarule.role ...共20处 |
| 16 | `perm_rolegroup` | 2 | hrcs_perminitrecord.rbase_group, hrcs_rolegrp.parent |
| 17 | `perm_userrole` | 1 | hrcs_userrole.userrolepf |
| 18 | `privacystatement` | 2 | hbss_loginconfig.privacystmt, hbss_privacysigning.privacystmt |
| 19 | `sch_job` | 2 | hrptmc_anobjextract.schejob, hrptmc_subscriberecord.schejob |
| 20 | `sch_schedule` | 5 | hrcs_needdeletetask.schedule, hrptmc_anobjextract.scheplan, hrptmc_anobjextract.scheplanlast ...共5处 |
| 21 | `task_decisionitem` | 4 | hrcs_ssctask.decisionitemnew, hrcs_ssctaskhis.decisionitemnew, hrpi_ssctask.decisionitemnew ...共4处 |
| 22 | `task_disrule` | 4 | hrcs_ssctask.matchrule, hrcs_ssctaskhis.matchrule, hrpi_ssctask.matchrule ...共4处 |
| 23 | `task_qualitysamplelibrary` | 4 | hrcs_ssctask.qualitysamplelibrary, hrcs_ssctaskhis.qualitysamplelibrary, hrpi_ssctask.qualitysamplelibrary ...共4处 |
| 24 | `task_taskbill` | 4 | hrcs_ssctask.billtype, hrcs_ssctaskhis.billtype, hrpi_ssctask.billtype ...共4处 |
| 25 | `task_taskbill_child` | 4 | hrcs_ssctask.createruleid, hrcs_ssctaskhis.createruleid, hrpi_ssctask.createruleid ...共4处 |
| 26 | `task_tasklevel` | 4 | hrcs_ssctask.tasklevelid, hrcs_ssctaskhis.tasklevelid, hrpi_ssctask.tasklevelid ...共4处 |
| 27 | `task_tasktype` | 12 | hrcs_ssctask.consignerid, hrcs_ssctask.flowbackstgid, hrcs_ssctask.tasktypeid ...共12处 |
| 28 | `task_usergroup` | 4 | hrcs_ssctask.usergroup, hrcs_ssctaskhis.usergroup, hrpi_ssctask.usergroup ...共4处 |
| 29 | `task_withdrawal` | 4 | hrcs_ssctask.unpassreasonid, hrcs_ssctaskhis.unpassreasonid, hrpi_ssctask.unpassreasonid ...共4处 |

---

## 7. ER图关系汇总（去重）

以下为用于ER图的实体间引用关系，共享元数据已映射到代表元数据。
已排除：平台对象、基础资料-非主数据类引用。

| # | 源实体 | 字段Key | 字段名 | 目标实体（代表） |
|---|--------|---------|--------|----------------|
| 1 | `brm_ruledesign` | policy | 所属策略 | `brm_rulelist` |
| 2 | `brm_ruledesign` | scene | 所属场景 | `brm_scene` |
| 3 | `brm_rulelist` | policy | 策略 | `brm_rulelist` |
| 4 | `brm_rulelist` | rulescene | 所属场景 | `brm_scene` |
| 5 | `brm_scene` | bizappid | 所属应用 | `hbp_devportal_bizapp` |
| 6 | `brm_sceneinput` | basedatafield | 基础资料 | `hbp_entityobject` |
| 7 | `brm_sceneinput` | paramsobject | 业务对象 | `hbp_entityobject` |
| 8 | `brm_target` | bizapp | 所属应用 | `hbp_devportal_bizapp` |
| 9 | `brm_target` | scene | 所属场景 | `brm_scene` |
| 10 | `haos_adminorg` | adminorgfunction | 行政组织职能 | `haos_adminorgfunction` |
| 11 | `haos_adminorg` | adminorglayer | 管理层级 | `haos_adminorglayer` |
| 12 | `haos_adminorg` | adminorgtype | 行政组织类型 | `haos_adminorgtype` |
| 13 | `haos_adminorg` | belongadminorg | 所属行政组织 | `haos_adminorg` |
| 14 | `haos_adminorg` | belongcompany | 所属公司 | `haos_adminorg` |
| 15 | `haos_adminorg` | belongdept | 所属部门 | `haos_adminorg` |
| 16 | `haos_adminorg` | corporateorg | 法律实体 | `hbss_lawentity` |
| 17 | `haos_adminorg` | otclassify | 组织分类 | `haos_structtype` |
| 18 | `haos_adminorg` | parentorg | 上级行政组织 | `haos_adminorg` |
| 19 | `haos_adminorg` | workplace | 工作地 | `hbss_workplace` |
| 20 | `haos_adminorg_msgdetail` | afterversion | 变更后版本 | `haos_adminorg` |
| 21 | `haos_adminorg_msgdetail` | beforeversion | 变更前版本 | `haos_adminorg` |
| 22 | `haos_adminorg_msgdetail` | bo | 组织当前版本数据 | `haos_adminorg` |
| 23 | `haos_adminorg_msgdetail` | changeoperate | 变动操作 | `haos_changeoperat` |
| 24 | `haos_adminorg_msgdetail` | changescene | 变动场景 | `haos_changescene` |
| 25 | `haos_adminorgcompany` | adminorg | 行政组织 | `haos_adminorg` |
| 26 | `haos_adminorgcompany` | companytype | 公司类型 | `haos_companytype` |
| 27 | `haos_adminorgstruct` | adminorg | 组织 | `haos_adminorg` |
| 28 | `haos_adminorgstruct` | otclassify | 组织分类 | `haos_structtype` |
| 29 | `haos_adminorgstruct` | parentorg | 上级组织 | `haos_adminorg` |
| 30 | `haos_adminorgstruct` | structproject | 架构方案 | `haos_structure` |
| 31 | `haos_adminorgtype` | adminorgtypestd | 类型归属 | `haos_adminorgtypestd` |
| 32 | `haos_changeoperat` | otclassify | 组织团队分类 | `haos_structtype` |
| 33 | `haos_changescene` | orgchangetype | 变动类型 | `haos_orgchangetype` |
| 34 | `haos_changescene` | otclassify | 组织团队分类 | `haos_structtype` |
| 35 | `haos_changescenesub` | otclassify | 组织团队分类 | `haos_structtype` |
| 36 | `haos_chargeperson` | adminorg | 行政组织 | `haos_adminorg` |
| 37 | `haos_chargeperson` | changesource | 变动来源 | `haos_changesource` |
| 38 | `haos_chargeperson` | depadminorg | 任职部门行政组织 | `haos_adminorg` |
| 39 | `haos_chargeperson` | employee | 人员 | `hrpi_employee` |
| 40 | `haos_chargeperson` | empposorgrel | 负责人 | `hrpi_empposorgrel` |
| 41 | `haos_chargeperson` | job | 职位 | `hbjm_jobhr` |
| 42 | `haos_chargeperson` | position | 岗位 | `hbpm_positionhr` |
| 43 | `haos_dimstaffreport` | adminorg | 行政组织 | `haos_adminorg` |
| 44 | `haos_dimstaffreport` | job | 职位 | `hbjm_jobhr` |
| 45 | `haos_dimstaffreport` | position | 岗位 | `hbpm_positionhr` |
| 46 | `haos_dimstaffreport` | staffdimension | 编制维度 | `haos_combdimension` |
| 47 | `haos_dutyorgdetail` | dutyorg | 责任组织 | `haos_adminorg` |
| 48 | `haos_dutyorgdetail` | staff | 编制规划内码 | `haos_staff` |
| 49 | `haos_dutyorgdetail` | vid | 历史最新版本 | `haos_dutyorgdetailhis` |
| 50 | `haos_dutyorgdetailhis` | dutyorg | 责任组织 | `haos_adminorg` |
| 51 | `haos_dutyorgdetailhis` | dutyorgdetail | 责任组织明细内码 | `haos_dutyorgdetail` |
| 52 | `haos_muldimendetail` | entrydimtype | 编制维度 | `haos_combdimension` |
| 53 | `haos_muldimendetail` | job | 职位 | `hbjm_jobhr` |
| 54 | `haos_muldimendetail` | joblevel | 职级 | `hbjm_joblevelhr` |
| 55 | `haos_muldimendetail` | orgteam | 组织团队 | `haos_adminorg` |
| 56 | `haos_muldimendetail` | position | 岗位 | `hbpm_positionhr` |
| 57 | `haos_muldimendetail` | staff | 编制规划内码 | `haos_staff` |
| 58 | `haos_muldimendetail` | useorg | 使用组织 | `haos_adminorg` |
| 59 | `haos_muldimendetail` | vid | 历史最新版本 | `haos_muldimendetailhis` |
| 60 | `haos_muldimendetailhis` | entrydimtype | 编制维度 | `haos_combdimension` |
| 61 | `haos_muldimendetailhis` | job | 职位 | `hbjm_jobhr` |
| 62 | `haos_muldimendetailhis` | joblevel | 职级 | `hbjm_joblevelhr` |
| 63 | `haos_muldimendetailhis` | muldimendetail | 多维控编明细内码 | `haos_muldimendetail` |
| 64 | `haos_muldimendetailhis` | orgteam | 组织团队 | `haos_adminorg` |
| 65 | `haos_muldimendetailhis` | position | 岗位 | `hbpm_positionhr` |
| 66 | `haos_muldimendetailhis` | useorg | 使用组织 | `haos_adminorg` |
| 67 | `haos_muldimusestaff` | muldimendetail | 多维控编明细 | `haos_muldimendetail` |
| 68 | `haos_muldimusestaff` | orgusestaffdetail | 组织占编信息 | `haos_orgusestaffdetail` |
| 69 | `haos_muldimusestaff` | personstaffinfo | 占编员工信息 | `haos_personstaffinfo` |
| 70 | `haos_orgchangereason` | otclassify | 组织团队分类 | `haos_structtype` |
| 71 | `haos_orgchangetype` | otclassify | 组织团队分类 | `haos_structtype` |
| 72 | `haos_orgfullname` | adminorg | 组织 | `haos_adminorg` |
| 73 | `haos_orgfullname` | levelorg1 | 一级组织 | `haos_adminorg` |
| 74 | `haos_orgfullname` | levelorg10 | 十级组织 | `haos_adminorg` |
| 75 | `haos_orgfullname` | levelorg11 | 十一级组织 | `haos_adminorg` |
| 76 | `haos_orgfullname` | levelorg12 | 十二级组织 | `haos_adminorg` |
| 77 | `haos_orgfullname` | levelorg13 | 十三级组织 | `haos_adminorg` |
| 78 | `haos_orgfullname` | levelorg14 | 十四级组织 | `haos_adminorg` |
| 79 | `haos_orgfullname` | levelorg15 | 十五级组织 | `haos_adminorg` |
| 80 | `haos_orgfullname` | levelorg16 | 十六级组织 | `haos_adminorg` |
| 81 | `haos_orgfullname` | levelorg17 | 十七级组织 | `haos_adminorg` |
| 82 | `haos_orgfullname` | levelorg18 | 十八级组织 | `haos_adminorg` |
| 83 | `haos_orgfullname` | levelorg19 | 十九级组织 | `haos_adminorg` |
| 84 | `haos_orgfullname` | levelorg2 | 二级组织 | `haos_adminorg` |
| 85 | `haos_orgfullname` | levelorg20 | 二十级组织 | `haos_adminorg` |
| 86 | `haos_orgfullname` | levelorg21 | 二十一级组织 | `haos_adminorg` |
| 87 | `haos_orgfullname` | levelorg22 | 二十二级组织 | `haos_adminorg` |
| 88 | `haos_orgfullname` | levelorg23 | 二十三级组织 | `haos_adminorg` |
| 89 | `haos_orgfullname` | levelorg24 | 二十四级组织 | `haos_adminorg` |
| 90 | `haos_orgfullname` | levelorg25 | 二十五级组织 | `haos_adminorg` |
| 91 | `haos_orgfullname` | levelorg26 | 二十六级组织 | `haos_adminorg` |
| 92 | `haos_orgfullname` | levelorg27 | 二十七级组织 | `haos_adminorg` |
| 93 | `haos_orgfullname` | levelorg28 | 二十八级组织 | `haos_adminorg` |
| 94 | `haos_orgfullname` | levelorg29 | 二十九级组织 | `haos_adminorg` |
| 95 | `haos_orgfullname` | levelorg3 | 三级组织 | `haos_adminorg` |
| 96 | `haos_orgfullname` | levelorg30 | 三十级组织 | `haos_adminorg` |
| 97 | `haos_orgfullname` | levelorg4 | 四级组织 | `haos_adminorg` |
| 98 | `haos_orgfullname` | levelorg5 | 五级组织 | `haos_adminorg` |
| 99 | `haos_orgfullname` | levelorg6 | 六级组织 | `haos_adminorg` |
| 100 | `haos_orgfullname` | levelorg7 | 七级组织 | `haos_adminorg` |
| 101 | `haos_orgfullname` | levelorg8 | 八级组织 | `haos_adminorg` |
| 102 | `haos_orgfullname` | levelorg9 | 九级组织 | `haos_adminorg` |
| 103 | `haos_orgpersonstaffinfo` | candidate | 候选人 | `hcf_candidate` |
| 104 | `haos_orgpersonstaffinfo` | employee | 员工 | `hrpi_employee` |
| 105 | `haos_orgpersonstaffinfo` | empposorgrel | 任职经历 | `hrpi_empposorgrel` |
| 106 | `haos_orgpersonstaffinfo` | job | 职位 | `hbjm_jobhr` |
| 107 | `haos_orgpersonstaffinfo` | joblevel | 职级 | `hbjm_joblevelhr` |
| 108 | `haos_orgpersonstaffinfo` | orgteam | 组织团队 | `haos_adminorg` |
| 109 | `haos_orgpersonstaffinfo` | personstaffinfo | 占编员工信息内码 | `haos_personstaffinfo` |
| 110 | `haos_orgpersonstaffinfo` | position | 岗位 | `hbpm_positionhr` |
| 111 | `haos_orgstaffreport` | adminorg | 行政组织 | `haos_adminorg` |
| 112 | `haos_orgteamcooprel` | cooporgteam | 目标组织 | `haos_adminorg` |
| 113 | `haos_orgteamcooprel` | coopreltype | 协作类型 | `haos_teamcoopreltype` |
| 114 | `haos_orgteamcooprel` | org | 源组织 | `haos_adminorg` |
| 115 | `haos_orgusestaffdetail` | empposorgrel | 任职经历 | `hrpi_empposorgrel` |
| 116 | `haos_orgusestaffdetail` | personstaffinfo | 占编员工信息 | `haos_personstaffinfo` |
| 117 | `haos_orgusestaffdetail` | useorg | 使用组织 | `haos_adminorg` |
| 118 | `haos_orgusestaffdetail` | useorgdetail | 使用组织明细 | `haos_useorgdetail` |
| 119 | `haos_othemproleorgrel` | adminorg | 所属组织 | `haos_adminorg` |
| 120 | `haos_othemproleorgrel` | employee | 人员 | `hrpi_employeenewf7query` |
| 121 | `haos_othemproleorgrel` | otherrole | 角色 | `haos_othrole` |
| 122 | `haos_othorgstruct` | adminorg | 组织 | `haos_adminorg` |
| 123 | `haos_othorgstruct` | otclassify | 组织分类 | `haos_structtype` |
| 124 | `haos_othorgstruct` | parentorg | 上级组织 | `haos_adminorg` |
| 125 | `haos_othorgstruct` | structproject | 架构方案 | `haos_structure` |
| 126 | `haos_othrole` | adminorg | 所属组织 | `haos_adminorg` |
| 127 | `haos_othrole` | sourcetpl | 来源角色模版 | `haos_othroletpl` |
| 128 | `haos_othroletpl` | structtype | 所属架构类型 | `haos_structtype` |
| 129 | `haos_personchangeevent` | changeevent | 变动活动 | `haos_staffactivity` |
| 130 | `haos_personchangeevent` | eventtype | 活动类型 | `haos_staffactivitytype` |
| 131 | `haos_personchangeevent` | orgusestaffdetail | 组织占编明细 | `haos_orgusestaffdetail` |
| 132 | `haos_personchangeevent` | staffperson | 占编员工信息 | `haos_personstaffinfo` |
| 133 | `haos_personstaffinfo` | candidate | 候选人 | `hcf_candidate` |
| 134 | `haos_personstaffinfo` | employee | 员工 | `hrpi_employee` |
| 135 | `haos_remainstafflist` | adminorg | 行政组织 | `haos_adminorg` |
| 136 | `haos_staff` | bdutyorg | 责任组织 | `haos_adminorg` |
| 137 | `haos_staff` | buseorg | 使用组织 | `haos_adminorg` |
| 138 | `haos_staff` | buseorgboid | 使用组织当前版本 | `haos_adminorg` |
| 139 | `haos_staff` | orgteam | 使用组织 | `haos_adminorg` |
| 140 | `haos_staff` | staffcycle | 填报期间 | `haos_staffcycle` |
| 141 | `haos_staff` | useorg | 使用组织 | `haos_adminorg` |
| 142 | `haos_staffactivity` | staffactivitytype | 活动类型 | `haos_staffactivitytype` |
| 143 | `haos_staffcase` | adminorg | 所属公司 | `haos_adminorg` |
| 144 | `haos_staffcase` | employee | 员工 | `hrpi_employeenewf7query` |
| 145 | `haos_staffcase` | empposorgrel | 员工 | `hrpi_empposf7query` |
| 146 | `haos_staffflex_bd` | hg | 横表 | `haos_staffflex` |
| 147 | `haos_stafforgempcount` | useorgbo | 行政组织 | `haos_adminorg` |
| 148 | `haos_staffruleconfig` | refstaff | 关联编制信息 | `haos_staff` |
| 149 | `haos_staffruleconfig` | staffcycle | 填报期间 | `haos_staffcycle` |
| 150 | `haos_staffruleconfig` | staffruleorgperm | 编制计划组织 | `haos_adminorg` |
| 151 | `haos_structproconfig` | bizapp | 应用 | `hbp_devportal_bizapp` |
| 152 | `haos_structproconfig` | structproject | 架构方案 | `haos_structure` |
| 153 | `haos_structure` | relyonstructproject | 依赖架构方案 | `haos_structure` |
| 154 | `haos_structure` | rootorg | 根组织 | `haos_adminorg` |
| 155 | `haos_useorgdetail` | dutyorg | 所属责任组织 | `haos_adminorg` |
| 156 | `haos_useorgdetail` | staff | 编制规划内码 | `haos_staff` |
| 157 | `haos_useorgdetail` | useorg | 使用组织 | `haos_adminorg` |
| 158 | `haos_useorgdetail` | vid | 历史最新版本 | `haos_useorgdetailhis` |
| 159 | `haos_useorgdetailhis` | dutyorg | 所属责任组织 | `haos_adminorg` |
| 160 | `haos_useorgdetailhis` | useorg | 使用组织 | `haos_adminorg` |
| 161 | `haos_useorgdetailhis` | useorgdetail | 使用组织明细内码 | `haos_useorgdetail` |
| 162 | `hbjm_job_msgdetail` | afterversion | 变更后版本 | `hbjm_jobhr` |
| 163 | `hbjm_job_msgdetail` | beforeversion | 变更前版本 | `hbjm_jobhr` |
| 164 | `hbjm_job_msgdetail` | bo | 职位bo | `hbjm_jobhr` |
| 165 | `hbjm_jobclasshr` | jobfamily | 职位族 | `hbjm_jobfamilyhr` |
| 166 | `hbjm_jobclasshr` | jobseq | 职位序列 | `hbjm_jobseqhr` |
| 167 | `hbjm_jobclasshr` | parent | 上级职位类 | `hbjm_jobclasshr` |
| 168 | `hbjm_jobfamilyhr` | jobseq | 职位序列 | `hbjm_jobseqhr` |
| 169 | `hbjm_jobgradehr` | corjobgrade | 集团职等 | `hbjm_jobgradehr` |
| 170 | `hbjm_jobhr` | highjobgrade | 最高职等 | `hbjm_jobgradehr` |
| 171 | `hbjm_jobhr` | highjoblevel | 最高职级 | `hbjm_joblevelhr` |
| 172 | `hbjm_jobhr` | jobclass | 职位类 | `hbjm_jobclasshr` |
| 173 | `hbjm_jobhr` | jobfamily | 职位族 | `hbjm_jobfamilyhr` |
| 174 | `hbjm_jobhr` | jobgradescm | 职等方案 | `hbjm_jobgradescmhr` |
| 175 | `hbjm_jobhr` | joblevelscm | 职级方案 | `hbjm_joblevelscmhr` |
| 176 | `hbjm_jobhr` | jobseq | 职位序列 | `hbjm_jobseqhr` |
| 177 | `hbjm_jobhr` | jobtype | 职位类别 | `hbjm_jobtype` |
| 178 | `hbjm_jobhr` | lowjobgrade | 最低职等 | `hbjm_jobgradehr` |
| 179 | `hbjm_jobhr` | lowjoblevel | 最低职级 | `hbjm_joblevelhr` |
| 180 | `hbjm_joblevelhr` | corjoblevel | 集团职级 | `hbjm_joblevelhr` |
| 181 | `hbjm_jobseqhr` | standardjobseq | 标准职位序列 | `hbjm_standardjobseqhr` |
| 182 | `hbp_datagrade_unittest` | businessfield | 业务领域(业务需要自行添加默认值) | `hbss_hrbusinessfield` |
| 183 | `hbp_formula_unittest` | resultitem | 结果参数 | `hbp_calresultitem` |
| 184 | `hbp_formulaeg` | app | 所属应用 | `hbp_devportal_bizapp` |
| 185 | `hbp_unittesthisbugrp01` | group | 分组 | `hbp_unittesthistpl01` |
| 186 | `hbp_unittesthisgrp01` | group | 分组 | `hbp_unittesthistpl01` |
| 187 | `hbp_unittesthisoritpl01` | bof7 | bof7 | `hbp_unittesthistpl01` |
| 188 | `hbp_unittesthistpl01` | hisbasedata01 | 版本F7 | `hbp_unittesthistpl01` |
| 189 | `hbp_unittesthistpl03` | bof7 | boF7 | `hbp_unittesthistpl01` |
| 190 | `hbp_unittesthistpl03` | versionf7 | versionF7 | `hbp_unittesthistpl01` |
| 191 | `hbp_unittesttime01` | college | 高等院校 | `hbss_college` |
| 192 | `hbp_unittesttime01` | timeline01 | 时间轴测试01 | `hbp_unittesttime01` |
| 193 | `hbp_unittesttpl01` | bof7 | boF7 | `hbp_unittesthistpl01` |
| 194 | `hbp_unittesttpl01` | verf7 | verF7 | `hbp_unittesthistpl01` |
| 195 | `hbpm_changescene` | changetype | 变动类型 | `hbpm_changetype` |
| 196 | `hbpm_chgrecord` | changeoperate | 变动操作 | `hbpm_changeoperate` |
| 197 | `hbpm_chgrecord` | changereason | 变动原因 | `hbpm_changereason` |
| 198 | `hbpm_chgrecord` | changescene | 变动场景 | `hbpm_changescene` |
| 199 | `hbpm_chgrecord` | changetype | 变动类型 | `hbpm_changetype` |
| 200 | `hbpm_chgrecord` | hisposition | 岗位历史版本ID | `hbpm_positionhr` |
| 201 | `hbpm_chgrecord` | position | 岗位 | `hbpm_positionhr` |
| 202 | `hbpm_chgrecord` | searchchangereason | 变动原因 | `hbpm_changereason` |
| 203 | `hbpm_chgrecord` | sourceposition | 关联岗位 | `hbpm_positionhr` |
| 204 | `hbpm_chgrecord` | targetposition | 目标岗位 | `hbpm_positionhr` |
| 205 | `hbpm_chgrecorddetail` | changeoperate | 变动操作 | `hbpm_changeoperate` |
| 206 | `hbpm_chgrecordevt` | changereason | 变动原因 | `hbpm_changereason` |
| 207 | `hbpm_chgrecordevt` | changescene | 变动场景 | `hbpm_changescene` |
| 208 | `hbpm_chgrecordevt` | changetype | 变动类型 | `hbpm_changetype` |
| 209 | `hbpm_chgrecordevt` | hisposition | 岗位历史版本ID | `hbpm_positionhr` |
| 210 | `hbpm_chgrecordevt` | sourceposition | 关联岗位 | `hbpm_positionhr` |
| 211 | `hbpm_chgrecordevt` | targetposition | 目标岗位 | `hbpm_positionhr` |
| 212 | `hbpm_position_msgdetail` | afterversion | 变更后版本 | `hbpm_positionhr` |
| 213 | `hbpm_position_msgdetail` | beforeversion | 变更前版本 | `hbpm_positionhr` |
| 214 | `hbpm_position_msgdetail` | bo | 岗位bo | `hbpm_positionhr` |
| 215 | `hbpm_position_msgdetail` | changeoperate | 变动操作 | `hbpm_changeoperate` |
| 216 | `hbpm_position_msgdetail` | changescene | 变动场景 | `hbpm_changescene` |
| 217 | `hbpm_positionhr` | adminorg | 行政组织 | `haos_adminorg` |
| 218 | `hbpm_positionhr` | applicableorg | 行政组织名称 | `haos_adminorg` |
| 219 | `hbpm_positionhr` | changedesc | 变动原因 | `hbpm_changereason` |
| 220 | `hbpm_positionhr` | changeoperate | 变动操作 | `hbpm_changeoperate` |
| 221 | `hbpm_positionhr` | changescene | 变动场景 | `hbpm_changescene` |
| 222 | `hbpm_positionhr` | changetype | 变动类型 | `hbpm_changetype` |
| 223 | `hbpm_positionhr` | highjobgrade | 最高职等 | `hbjm_jobgradehr` |
| 224 | `hbpm_positionhr` | highjoblevel | 最高职级 | `hbjm_joblevelhr` |
| 225 | `hbpm_positionhr` | job | 职位 | `hbjm_jobhr` |
| 226 | `hbpm_positionhr` | jobgradescm | 职等方案 | `hbjm_jobgradescmhr` |
| 227 | `hbpm_positionhr` | joblevelscm | 职级方案 | `hbjm_joblevelscmhr` |
| 228 | `hbpm_positionhr` | jobscm | 职位体系方案 | `hbjm_jobscmhr` |
| 229 | `hbpm_positionhr` | lowjobgrade | 最低职等 | `hbjm_jobgradehr` |
| 230 | `hbpm_positionhr` | lowjoblevel | 最低职级 | `hbjm_joblevelhr` |
| 231 | `hbpm_positionhr` | parent | 上级岗位 | `hbpm_positionhr` |
| 232 | `hbpm_positionhr` | positiontpl | 岗位模板 | `hbpm_positiontpl` |
| 233 | `hbpm_positionhr` | positiontype | 岗位类型 | `hbpm_positiontype` |
| 234 | `hbpm_positionhr` | workplace | 工作地 | `hbss_workplace` |
| 235 | `hbpm_positionrelation` | parent | 协作岗位 | `hbpm_positionhr` |
| 236 | `hbpm_positionrelation` | reportingtype | 角色协作类型 | `hbpm_reportcoreltype` |
| 237 | `hbpm_positionrelation` | role | 岗位 | `hbpm_positionhr` |
| 238 | `hbpm_positiontpl` | adminorg | 行政组织 | `haos_adminorg` |
| 239 | `hbpm_positiontpl` | exceptionadminorg | 行政组织 | `haos_adminorg` |
| 240 | `hbpm_positiontpl` | highjobgrade | 最高职等 | `hbjm_jobgradehr` |
| 241 | `hbpm_positiontpl` | highjoblevel | 最高职级 | `hbjm_joblevelhr` |
| 242 | `hbpm_positiontpl` | job | 职位 | `hbjm_jobhr` |
| 243 | `hbpm_positiontpl` | jobgradescm | 职等方案 | `hbjm_jobgradescmhr` |
| 244 | `hbpm_positiontpl` | joblevelscm | 职级方案 | `hbjm_joblevelscmhr` |
| 245 | `hbpm_positiontpl` | jobscm | 职位体系方案 | `hbjm_jobscmhr` |
| 246 | `hbpm_positiontpl` | lowjobgrade | 最低职等 | `hbjm_jobgradehr` |
| 247 | `hbpm_positiontpl` | lowjoblevel | 最低职级 | `hbjm_joblevelhr` |
| 248 | `hbpm_positiontpl` | positiontype | 岗位类型 | `hbpm_positiontype` |
| 249 | `hbpm_positiontpl` | posttpltype | 岗位模板类型 | `hbpm_positiontpltype` |
| 250 | `hbss_capacitygroup` | parent | 上级维度 | `hbss_capacitygroup` |
| 251 | `hbss_capacitygroup` | parentdim | 上级维度 | `hbss_capacitygroup` |
| 252 | `hbss_capacityitem` | group | 所属维度 | `hbss_capacitygroup` |
| 253 | `hbss_cloud_app` | app | 应用 | `hbp_devportal_bizapp` |
| 254 | `hbss_costcenter` | parent | 上级单位 | `hbss_costcenter` |
| 255 | `hbss_enterprise` | lawentity | 关联法律实体 | `hbss_lawentity` |
| 256 | `hbss_hrbuca` | parent | HR业务管理视图 | `hbss_hrbuca` |
| 257 | `hbss_loginconfig` | loginscene | 登录场景 | `hbss_loginscene` |
| 258 | `hbss_safeuri` | safeuriconfig | 短链配置 | `hbss_safeuriconfig` |
| 259 | `hbss_scoresystem` | scoreinterval | 评分间隔 | `hbss_scoreinterval` |
| 260 | `hbss_signcompany` | lawentity | 法律实体 | `hbss_lawentity` |
| 261 | `hbss_signcompany` | reorg | 关联法人 | `hbss_lawentity` |
| 262 | `hbss_signcompanyhis` | lawentity | 法律实体 | `hbss_lawentity` |
| 263 | `hbss_signcompanyhis` | reorg | 关联法人 | `hbss_lawentity` |
| 264 | `hbss_taxunit` | lawentity | 法律实体 | `hbss_lawentity` |
| 265 | `hcf_canaddress` | candidate | 拟入职人员 | `hcf_candidate` |
| 266 | `hcf_canbankcard` | candidate | 拟入职人员 | `hcf_candidate` |
| 267 | `hcf_cancontact` | candidate | 拟入职人员 | `hcf_candidate` |
| 268 | `hcf_cancontactinfo` | candidate | 拟入职人员 | `hcf_candidate` |
| 269 | `hcf_cancre` | candidate | 拟入职人员 | `hcf_candidate` |
| 270 | `hcf_candidate` | procreatstatus | 生育状况 | `hbss_procreatstatus` |
| 271 | `hcf_caneduexp` | candidate | 拟入职人员 | `hcf_candidate` |
| 272 | `hcf_caneduexp` | graduateschool | 毕业院校 | `hbss_college` |
| 273 | `hcf_canfamily` | candidate | 拟入职人员 | `hcf_candidate` |
| 274 | `hcf_canlgability` | candidate | 拟入职人员 | `hcf_candidate` |
| 275 | `hcf_canocpqual` | candidate | 拟入职人员 | `hcf_candidate` |
| 276 | `hcf_canprework` | businesstypeid | 企业性质 | `hbss_empnature` |
| 277 | `hcf_canprework` | candidate | 拟入职人员 | `hcf_candidate` |
| 278 | `hcf_canprework` | companyscale | 公司规模 | `hbss_companyscale` |
| 279 | `hcf_canprojectexp` | candidate | 拟入职人员 | `hcf_candidate` |
| 280 | `hcf_canprojectexp` | companynature | 企业性质 | `hbss_empnature` |
| 281 | `hcf_cantraining` | candidate | 拟入职人员 | `hcf_candidate` |
| 282 | `hcf_personalarea` | candidate | 拟入职人员 | `hcf_candidate` |
| 283 | `hcf_rsmhobby` | candidate | 拟入职人员 | `hcf_candidate` |
| 284 | `hcf_rsmpatinv` | candidate | 拟入职人员 | `hcf_candidate` |
| 285 | `hcf_rsmproskl` | candidate | 拟入职人员 | `hcf_candidate` |
| 286 | `hrcs_actassignrec` | activityins | 活动任务实例 | `hrcs_activityins` |
| 287 | `hrcs_activity` | activitytype | 活动类型 | `hrcs_activitytype` |
| 288 | `hrcs_activity` | adminorg | 管理行政组织 | `haos_adminorg` |
| 289 | `hrcs_activityenablerec` | activity | 基础资料 | `hrcs_activity` |
| 290 | `hrcs_activityexception` | activityins | 活动任务实例 | `hrcs_activityins` |
| 291 | `hrcs_activitygroupins` | activity | 活动 | `hrcs_activity` |
| 292 | `hrcs_activityins` | activity | 活动 | `hrcs_activity` |
| 293 | `hrcs_activityins` | actscheme | 活动方案 | `hrcs_activityscheme` |
| 294 | `hrcs_activityscheme` | activity | 请选择活动 | `hrcs_activity` |
| 295 | `hrcs_activityscheme` | adminorg | 管理行政组织 | `haos_adminorg` |
| 296 | `hrcs_activityscheme` | bizapp | 所属应用 | `hbp_devportal_bizapp` |
| 297 | `hrcs_activityscheme` | groupactivity | 活动 | `hrcs_activity` |
| 298 | `hrcs_admingrouporg` | adminorg | 行政组织 | `haos_adminorg` |
| 299 | `hrcs_admingrouporg` | otclassify | 团队分类 | `haos_structtype` |
| 300 | `hrcs_admingrouporg` | struct | 架构方案 | `haos_structure` |
| 301 | `hrcs_bgtaskrecord` | bgtaskregister | 悬浮球任务注册信息 | `hrcs_bgtaskregister` |
| 302 | `hrcs_businessobject` | orgtype | 组织类型 | `haos_structtype` |
| 303 | `hrcs_bussinesstype` | bussinessobject | 业务对象 | `hrcs_businessobject` |
| 304 | `hrcs_chgeventblacklist` | verifbilltype | 协作核定单 | `hbp_entityobject` |
| 305 | `hrcs_commonvariable` | varmappingscene | 所属场景 | `hrcs_varmappingscene` |
| 306 | `hrcs_contractest` | candidate | 候选人 | `hcf_candidate` |
| 307 | `hrcs_contractest` | econttmp | 电子合同模板 | `hrcs_econtemplate` |
| 308 | `hrcs_contractest` | lawentity | 法律实体 | `hbss_lawentity` |
| 309 | `hrcs_contractest` | person | 自然人 | `hrpi_employeenewf7query` |
| 310 | `hrcs_coordappcfg` | coordbizobject | 业务对象 | `hrcs_coordbizobject` |
| 311 | `hrcs_coordappcfg` | coordrulesch | 协作规则方案 | `hrcs_coordrulesch` |
| 312 | `hrcs_coordappcfg` | coordstrategy | 协作处理策略 | `hrcs_coordstrategy` |
| 313 | `hrcs_coordbizfield` | group | 字段分组 | `hrcs_coordbizfieldgrp` |
| 314 | `hrcs_coordbizfieldgrp` | basedata | 基础资料 | `hbp_entityobject` |
| 315 | `hrcs_coordbizfieldgrp` | coordbizobject | 业务对象 | `hrcs_coordbizobject` |
| 316 | `hrcs_coordbizobject` | basedata | 基础资料 | `hbp_entityobject` |
| 317 | `hrcs_coordbizobject` | policyscene | 策略所属场景 | `brm_scene` |
| 318 | `hrcs_coordbizobject` | scene | 所属场景 | `brm_scene` |
| 319 | `hrcs_coordfieldrule` | coordbizfield | 协作字段 | `hrcs_coordbizfield` |
| 320 | `hrcs_coordfieldrule` | coordrulesch | 协作规则方案 | `hrcs_coordrulesch` |
| 321 | `hrcs_coordfieldrule` | keepoldpolicy | 保持旧值策略 | `brm_rulelist` |
| 322 | `hrcs_coordfieldrule` | valuepolicy | 赋值策略 | `brm_rulelist` |
| 323 | `hrcs_coordrulesch` | coordbizobject | 业务对象 | `hrcs_coordbizobject` |
| 324 | `hrcs_coordrulesch` | scene | 所属场景 | `brm_scene` |
| 325 | `hrcs_coordsceneconf` | scene | 场景 | `brm_scene` |
| 326 | `hrcs_coordstrategy` | coordbizobject | 业务对象 | `hrcs_coordbizobject` |
| 327 | `hrcs_coordstrategy` | currentlog | 当前日志 | `hrcs_coordstrategylog` |
| 328 | `hrcs_coordstrategy` | policy | 处理策略（隐藏） | `brm_rulelist` |
| 329 | `hrcs_coordstrategy` | scene | 所属场景 | `brm_scene` |
| 330 | `hrcs_coordstrategylog` | coordstrategy | 协作规则策略 | `hrcs_coordstrategy` |
| 331 | `hrcs_dynaformctrl` | app | 应用 | `hbp_devportal_bizapp` |
| 332 | `hrcs_dynamsgdealtrace` | calparam_scheme | 方案 | `hrcs_dynascheme` |
| 333 | `hrcs_dynamsgdealtrace` | scheme | 方案 | `hrcs_dynascheme` |
| 334 | `hrcs_dynaruleitem` | relatruleparam | 主规则参数项 | `hrcs_dynaruleitem` |
| 335 | `hrcs_dynaschdatarule` | bddatarule | 方案 | `hrcs_datarule` |
| 336 | `hrcs_dynaschdatarule` | datarule | 数据规则方案 | `hrcs_datarule` |
| 337 | `hrcs_dynaschdatarule` | scheme | 动态权限方案 | `hrcs_dynascheme` |
| 338 | `hrcs_dynaschdimgrp` | dimension | 维度 | `hrcs_dimension` |
| 339 | `hrcs_dynaschdimgrp` | dynacond | 动态条件 | `hrcs_dynacond` |
| 340 | `hrcs_dynaschdimgrp` | scheme | 动态权限方案 | `hrcs_dynascheme` |
| 341 | `hrcs_dynaschdimgrp` | structproject | 架构方案 | `haos_structure` |
| 342 | `hrcs_dynascheme` | assignpersonitem | 人员 | `hrcs_dynaauthobject` |
| 343 | `hrcs_dynascheme` | cancelpersonitem | 人员 | `hrcs_dynaauthobject` |
| 344 | `hrcs_dynascheme` | hrcsrole | 中台角色 | `hrcs_role` |
| 345 | `hrcs_dynaschexdimgrp` | dimension | 维度 | `hrcs_dimension` |
| 346 | `hrcs_dynaschexdimgrp` | scheme | 动态权限方案 | `hrcs_dynascheme` |
| 347 | `hrcs_dynaschexdimgrp` | structproject | 架构方案 | `haos_structure` |
| 348 | `hrcs_dynaschfield` | role | 角色 | `hrcs_role` |
| 349 | `hrcs_dynaschfield` | scheme | 动态权限方案 | `hrcs_dynascheme` |
| 350 | `hrcs_dynaschorg` | scheme | 动态权限方案 | `hrcs_dynascheme` |
| 351 | `hrcs_econtemplate` | sealtypeid | 企业印章类型 | `hrcs_esignsealtype` |
| 352 | `hrcs_empstrategy` | bussinessfield | 业务类型关系 | `hrcs_bussinesstype` |
| 353 | `hrcs_empstrategy` | defstrategytype | 默认策略 | `hrcs_strategy` |
| 354 | `hrcs_empstrategy` | entrydefstrategy | 策略 | `hrcs_strategy` |
| 355 | `hrcs_empstrategy` | entryinheritedorg | 参照行政组织 | `haos_adminorg` |
| 356 | `hrcs_empstrategy` | entryorgteam | 分录组织团队 | `haos_adminorg` |
| 357 | `hrcs_empstrategy` | entrysourceorg | 源策略组织 | `haos_adminorg` |
| 358 | `hrcs_empstrategy` | inheritedorg | 参照行政组织 | `haos_adminorg` |
| 359 | `hrcs_empstrategy` | orgteam | 行政组织 | `haos_adminorg` |
| 360 | `hrcs_empstrategy` | sourceorg | 源策略组织 | `haos_adminorg` |
| 361 | `hrcs_entityctrl` | bizapp | 应用 | `hbp_devportal_bizapp` |
| 362 | `hrcs_entityctrl` | dimension | 维度 | `hrcs_dimension` |
| 363 | `hrcs_esignappcfg` | corporate | 应用所属企业 | `hbss_lawentity` |
| 364 | `hrcs_esignappcfg` | esignsp | 电子签服务商 | `hrcs_esignspmgr` |
| 365 | `hrcs_esigncoauth` | authapp | 授权应用 | `hrcs_esignappcfg` |
| 366 | `hrcs_esigncoauth` | esignspmgr | 电子签服务商 | `hrcs_esignspmgr` |
| 367 | `hrcs_esigncoauth` | lawentity | 法律实体 | `hbss_lawentity` |
| 368 | `hrcs_esigncoseal` | corporate | 企业 | `hbss_lawentity` |
| 369 | `hrcs_esigncoseal` | esignapp | 应用 | `hrcs_esignappcfg` |
| 370 | `hrcs_esigncoseal` | esignsp | 电子签服务商 | `hrcs_esignspmgr` |
| 371 | `hrcs_esigncoseal` | sealtype | 印章类型 | `hrcs_esignsealtype` |
| 372 | `hrcs_esignsealauth` | authcompany | 授权企业 | `hbss_lawentity` |
| 373 | `hrcs_esignsealauth` | esignsp | 电子签服务商 | `hrcs_esignspmgr` |
| 374 | `hrcs_esignsealauth` | seal | 电子签章 | `hrcs_esigncoseal` |
| 375 | `hrcs_esignspmgr` | bdesignappcfg | 集成应用配置 | `hrcs_esignappcfg` |
| 376 | `hrcs_essyncrecord` | esscheme | ES同步方案 | `hrcs_essyncschemecfig` |
| 377 | `hrcs_essyncschemecfig` | queryentity | 查询实体 | `hrcs_querydynsourcelist` |
| 378 | `hrcs_filterparam` | basedatafield | 基础资料 | `hbp_entityobject` |
| 379 | `hrcs_filterparam` | paramsobject | 业务对象 | `hbp_entityobject` |
| 380 | `hrcs_filterscene` | bizappid | 所属应用 | `hbp_devportal_bizapp` |
| 381 | `hrcs_function` | group | 函数分类 | `hrcs_functiontype` |
| 382 | `hrcs_keywordmapping` | variableid | 常用变量 | `hrcs_commonvariable` |
| 383 | `hrcs_label` | brmscene | 场景 | `brm_scene` |
| 384 | `hrcs_label` | group | 标签分类 | `hrcs_labelgroup` |
| 385 | `hrcs_label` | labelobject | 打标对象 | `hrcs_labelobject` |
| 386 | `hrcs_labelgroup` | parent | 上级 | `hrcs_labelgroup` |
| 387 | `hrcs_labelobject` | parent | 所属主对象 | `hrcs_labelobject` |
| 388 | `hrcs_labelobjectrel` | brmscene | 规则引擎场景 | `brm_scene` |
| 389 | `hrcs_labelobjectrel` | label | 标签 | `hrcs_label` |
| 390 | `hrcs_labelobjectrel` | labelobject | 打标对象 | `hrcs_labelobject` |
| 391 | `hrcs_labelparam` | brmiputparam | 规则引擎入参 | `brm_sceneinput` |
| 392 | `hrcs_labelparam` | brmscene | 规则引擎场景 | `brm_scene` |
| 393 | `hrcs_labelparam` | label | 标签 | `hrcs_label` |
| 394 | `hrcs_labelparam` | labelobject | 打签对象 | `hrcs_labelobject` |
| 395 | `hrcs_labelparam` | labelvalue | 标签值 | `hrcs_labelvalue` |
| 396 | `hrcs_labelparam` | param | 标签关联因子 | `hrcs_labeldimension` |
| 397 | `hrcs_labelpolicyrule` | brmrule | 规则引擎规则 | `brm_ruledesign` |
| 398 | `hrcs_labelpolicyrule` | label | 标签 | `hrcs_label` |
| 399 | `hrcs_labelpolicyrule` | labelpolicy | 打标策略 | `hrcs_lblstrategy` |
| 400 | `hrcs_labelpolicyrule` | labelvalue | 标签值 | `hrcs_labelvalue` |
| 401 | `hrcs_labelpolicytask` | labelpolicy | 打标策略 | `hrcs_lblstrategy` |
| 402 | `hrcs_labelscene` | bizappid | 所属应用 | `hbp_devportal_bizapp` |
| 403 | `hrcs_labelscene` | label | 标签 | `hrcs_label` |
| 404 | `hrcs_labelscene` | labelobject | 关联打标对象 | `hrcs_labelobject` |
| 405 | `hrcs_labelvalue` | label | 标签 | `hrcs_label` |
| 406 | `hrcs_labelvaluerule` | brmtarget | 规则引擎指标 | `brm_target` |
| 407 | `hrcs_labelvaluerule` | labelobject | 打标对象 | `hrcs_labelobject` |
| 408 | `hrcs_labelvaluerule` | labelvalue | 标签值 | `hrcs_labelvalue` |
| 409 | `hrcs_lbldimension` | labelobjectid | 打标对象 | `hrcs_labelobject` |
| 410 | `hrcs_lblentityrelation` | entityid | 左关联实体ID | `hrcs_lbljoinentity` |
| 411 | `hrcs_lblentityrelation` | joinentityid | 右关联实体ID | `hrcs_lbljoinentity` |
| 412 | `hrcs_lblentityrelation` | labelobject | 打标对象 | `hrcs_labelobject` |
| 413 | `hrcs_lblfieldtype` | labelobjectid | 打标对象 | `hrcs_labelobject` |
| 414 | `hrcs_lbljoinentity` | labelobject | 打标对象 | `hrcs_labelobject` |
| 415 | `hrcs_lblobjconfig` | condcolumnid | 字段名 | `hrcs_labeldimension` |
| 416 | `hrcs_lblobjconfig` | dispcolumnid | 字段名称 | `hrcs_labeldimension` |
| 417 | `hrcs_lblobjconfig` | labelobjectid | 打标对象 | `hrcs_labelobject` |
| 418 | `hrcs_lblstrategy` | label | 标签 | `hrcs_label` |
| 419 | `hrcs_lblstrategy` | labelobject | 打标对象 | `hrcs_labelobject` |
| 420 | `hrcs_lblstrategy` | labelvalue | 标签值 | `hrcs_labelvalue` |
| 421 | `hrcs_lblstrategyfilter` | lblfactor | 因子 | `hrcs_labeldimension` |
| 422 | `hrcs_lblstrategyfilter` | lblstrategy | 打标策略 | `hrcs_lblstrategy` |
| 423 | `hrcs_orgstrategy` | bussinessfield | 业务类型关系 | `hrcs_bussinesstype` |
| 424 | `hrcs_orgstrategy` | defstrategytype | 默认策略 | `hrcs_strategy` |
| 425 | `hrcs_orgstrategy` | entrydefstrategy | 策略 | `hrcs_strategy` |
| 426 | `hrcs_orgstrategy` | entryinheritedorg | 参照行政组织 | `haos_adminorg` |
| 427 | `hrcs_orgstrategy` | entryorgteam | 分录行政组织 | `haos_adminorg` |
| 428 | `hrcs_orgstrategy` | entrysourceorg | 源策略组织 | `haos_adminorg` |
| 429 | `hrcs_orgstrategy` | inheritedorg | 参照行政组织 | `haos_adminorg` |
| 430 | `hrcs_orgstrategy` | orgteam | 行政组织 | `haos_adminorg` |
| 431 | `hrcs_orgstrategy` | sourceorg | 源策略组织 | `haos_adminorg` |
| 432 | `hrcs_permapplybill` | assign_permfile | 权限档案 | `hrcs_userpermfile` |
| 433 | `hrcs_permapplybill` | assign_scheme | 方案 | `hrcs_dynascheme` |
| 434 | `hrcs_permapplybill` | cancel_permfile | 权限档案 | `hrcs_userpermfile` |
| 435 | `hrcs_permapplybill` | cancel_scheme | 方案 | `hrcs_dynascheme` |
| 436 | `hrcs_permfilegrp` | parent | 上级分组 | `hrcs_permfilegrp` |
| 437 | `hrcs_permfilegrpmember` | permfile | 权限档案 | `hrcs_userpermfile` |
| 438 | `hrcs_permfilegrpmember` | permfilegrp | 权限档案组 | `hrcs_permfilegrp` |
| 439 | `hrcs_perminitrecord` | bd_datarule | 数据规则方案 | `hrcs_datarule` |
| 440 | `hrcs_perminitrecord` | dim_dimension | 维度 | `hrcs_dimension` |
| 441 | `hrcs_perminitrecord` | dim_dynacond | 动态条件 | `hrcs_dynacond` |
| 442 | `hrcs_perminitrecord` | dim_otclassify | 团队分类来源 | `haos_structtype` |
| 443 | `hrcs_perminitrecord` | dim_structproject | 构架方案 | `haos_structure` |
| 444 | `hrcs_perminitrecord` | dr_datarule | 数据规则方案 | `hrcs_datarule` |
| 445 | `hrcs_perminitrecord` | rdata_dimension | 维度 | `hrcs_dimension` |
| 446 | `hrcs_perminitrecord` | rdata_dynacond | 动态条件 | `hrcs_dynacond` |
| 447 | `hrcs_perminitrecord` | rdata_structproject | 构架方案 | `haos_structure` |
| 448 | `hrcs_perminitrecord` | rdim_dimension | 维度 | `hrcs_dimension` |
| 449 | `hrcs_permrelat` | bizapp | 应用 | `hbp_devportal_bizapp` |
| 450 | `hrcs_permrelatcfg` | app | 应用 | `hbp_devportal_bizapp` |
| 451 | `hrcs_projempstrategy` | belongadminorg | 所属行政组织 | `haos_adminorg` |
| 452 | `hrcs_projempstrategy` | bussinessfield | 业务类型关系 | `hrcs_bussinesstype` |
| 453 | `hrcs_projempstrategy` | defstrategytype | 默认策略 | `hrcs_strategy` |
| 454 | `hrcs_projempstrategy` | entrydefstrategy | 策略 | `hrcs_strategy` |
| 455 | `hrcs_projempstrategy` | entryinheritedorg | 参照行政组织/项目团队 | `haos_adminorg` |
| 456 | `hrcs_projempstrategy` | entryorgteam | 分录组织团队 | `haos_adminorg` |
| 457 | `hrcs_projempstrategy` | entrysourceorg | 源策略组织 | `haos_adminorg` |
| 458 | `hrcs_projempstrategy` | inheritedorg | 参照行政组织/项目团队 | `haos_adminorg` |
| 459 | `hrcs_projempstrategy` | orgteam | 组织团队 | `haos_adminorg` |
| 460 | `hrcs_projempstrategy` | sourceorg | 源策略组织 | `haos_adminorg` |
| 461 | `hrcs_projorgstrategy` | belongadminorg | 所属行政组织 | `haos_adminorg` |
| 462 | `hrcs_projorgstrategy` | bussinessfield | 业务类型关系 | `hrcs_bussinesstype` |
| 463 | `hrcs_projorgstrategy` | defstrategytype | 默认策略 | `hrcs_strategy` |
| 464 | `hrcs_projorgstrategy` | entrydefstrategy | 策略 | `hrcs_strategy` |
| 465 | `hrcs_projorgstrategy` | entryinheritedorg | 参照行政组织/项目团队 | `haos_adminorg` |
| 466 | `hrcs_projorgstrategy` | entryorgteam | 分录组织团队 | `haos_adminorg` |
| 467 | `hrcs_projorgstrategy` | entrysourceorg | 源策略组织 | `haos_adminorg` |
| 468 | `hrcs_projorgstrategy` | inheritedorg | 参照行政组织/项目团队 | `haos_adminorg` |
| 469 | `hrcs_projorgstrategy` | orgteam | 组织团队 | `haos_adminorg` |
| 470 | `hrcs_projorgstrategy` | sourceorg | 源策略组织 | `haos_adminorg` |
| 471 | `hrcs_prompt` | businessobject | 业务对象 | `hbp_entityobject` |
| 472 | `hrcs_promptimport` | businessobject | 实体 | `hbp_entityobject` |
| 473 | `hrcs_promptrule` | businessobject | 页面 | `hbp_entityobject` |
| 474 | `hrcs_promptrule` | entryprompt | 提示语 | `hrcs_prompt` |
| 475 | `hrcs_relateentity` | entity | 实体 | `hbp_entityobject` |
| 476 | `hrcs_relateentity` | entityname | 实体 | `hbp_entityobject` |
| 477 | `hrcs_relateentity` | mainentity | 主实体 | `hbp_entityobject` |
| 478 | `hrcs_relateentity` | parententity | 实体 | `hbp_entityobject` |
| 479 | `hrcs_role` | rolegrp | 角色组 | `hrcs_rolegrp` |
| 480 | `hrcs_roledatarule` | bddatarule | 方案 | `hrcs_datarule` |
| 481 | `hrcs_roledatarule` | datarule | 方案 | `hrcs_datarule` |
| 482 | `hrcs_roledatarule` | role | 角色 | `hrcs_role` |
| 483 | `hrcs_roledimension` | dimension | 维度 | `hrcs_dimension` |
| 484 | `hrcs_roledimgrp` | dimension | 维度 | `hrcs_dimension` |
| 485 | `hrcs_roledimgrp` | dynacond | 动态条件 | `hrcs_dynacond` |
| 486 | `hrcs_roledimgrp` | role | HR角色 | `hrcs_role` |
| 487 | `hrcs_roledimgrp` | structproject | 架构方案 | `haos_structure` |
| 488 | `hrcs_roleexdimgrp` | dimension | 维度 | `hrcs_dimension` |
| 489 | `hrcs_roleexdimgrp` | structproject | 架构方案 | `haos_structure` |
| 490 | `hrcs_rolefield` | role | 角色 | `hrcs_role` |
| 491 | `hrcs_signfile` | signconfig | 签署配置信息 | `hrcs_esignappcfg` |
| 492 | `hrcs_signflow` | candidateid | 候选人 | `hcf_candidate` |
| 493 | `hrcs_signflow` | econttmp | 电子合同模板 | `hrcs_econtemplate` |
| 494 | `hrcs_signflow` | naturalid | 自然人 | `hrpi_employeenewf7query` |
| 495 | `hrcs_signflow` | signconfig | 签署配置信息 | `hrcs_esignappcfg` |
| 496 | `hrcs_signflow` | signorg | 合同签署主体 | `hbss_lawentity` |
| 497 | `hrcs_strategy` | bussinessfield | 业务类型 | `hrcs_bussinesstype` |
| 498 | `hrcs_tplvariableconfig` | mainentity | 主实体 | `hbp_entityobject` |
| 499 | `hrcs_tplvariableconfig` | parententity | 父业务对象 | `hbp_entityobject` |
| 500 | `hrcs_tplvariableconfig` | sonentity | 子业务对象 | `hbp_entityobject` |
| 501 | `hrcs_userdatarule` | bddatarule | 方案 | `hrcs_datarule` |
| 502 | `hrcs_userdatarule` | datarule | 数据规则方案 | `hrcs_datarule` |
| 503 | `hrcs_userdatarule` | userrolerelate | 用户角色关联 | `hrcs_userrolerelat` |
| 504 | `hrcs_userfield` | userrolerealt | 用户角色关联 | `hrcs_userrolerelat` |
| 505 | `hrcs_userpermfile` | permfilegrp | 用户组 | `hrcs_permfilegrp` |
| 506 | `hrcs_userpermfile` | permfilegrpmember | 档案组成员 | `hrcs_permfilegrpmember` |
| 507 | `hrcs_userrole` | userrolerealt | 用户角色关联关系 | `hrcs_userrolerelat` |
| 508 | `hrcs_userroledimgrp` | dimension | 维度 | `hrcs_dimension` |
| 509 | `hrcs_userroledimgrp` | dynacond | 动态条件 | `hrcs_dynacond` |
| 510 | `hrcs_userroledimgrp` | structproject | 架构方案 | `haos_structure` |
| 511 | `hrcs_userroledimgrp` | userrolerelat | 用户角色关联 | `hrcs_userrolerelat` |
| 512 | `hrcs_userroleexdimgrp` | dimension | 维度 | `hrcs_dimension` |
| 513 | `hrcs_userroleexdimgrp` | structproject | 架构方案 | `haos_structure` |
| 514 | `hrcs_userroleexdimgrp` | userrolerelat | 用户角色关联 | `hrcs_userrolerelat` |
| 515 | `hrcs_userrolerelat` | initrecord | 初始化记录 | `hrcs_perminitrecord` |
| 516 | `hrcs_userrolerelat` | permfile | 权限档案 | `hrcs_userpermfile` |
| 517 | `hrcs_userrolerelat` | scheme | 授权方案 | `hrcs_dynascheme` |
| 518 | `hrcs_varmappingscene` | bizapp | 所属应用 | `hbp_devportal_bizapp` |
| 519 | `hrcs_varmappingscene` | parententity | 取值对象主实体 | `hbp_entityobject` |
| 520 | `hrcs_warncalfield` | pluginservice | 插件服务 | `hrcs_warnpluginservice` |
| 521 | `hrcs_warncalfield` | refcalfield | 引用计算字段 | `hrcs_warncalfield` |
| 522 | `hrcs_warnmsgpersist` | warnscheme | 预警方案 | `hrcs_warnscheme` |
| 523 | `hrcs_warnmsgrowdata` | msgpersist | 消息详情 | `hrcs_warnmsgpersist` |
| 524 | `hrcs_warnobjtpl` | bizapp | 所属应用 | `hbp_devportal_bizapp` |
| 525 | `hrcs_warnobjtpl` | fromentity | 来源业务功能 | `hbp_entityobject` |
| 526 | `hrcs_warnpluginservice` | bizapp | 所属应用 | `hbp_devportal_bizapp` |
| 527 | `hrcs_warnscene` | bizapp | 所属应用 | `hbp_devportal_bizapp` |
| 528 | `hrcs_warnscene` | warnbizappid | 所属应用 | `hbp_devportal_bizapp` |
| 529 | `hrcs_warnscene` | warnentityperm | 控权维度 | `hrcs_dimension` |
| 530 | `hrcs_warnscene` | warnfromentiy | 处理预警业务功能 | `hbp_entityobject` |
| 531 | `hrcs_warnscene` | warnobjtpl | 预警对象(废弃) | `hrcs_warnobjtpl` |
| 532 | `hrcs_warnsceneentityrel` | entityid | 实体ID | `hrcs_warnscenejoinentity` |
| 533 | `hrcs_warnsceneentityrel` | joinentityid | 关联实体ID | `hrcs_warnscenejoinentity` |
| 534 | `hrcs_warnscheme` | adminorg | 所属行政组织 | `haos_adminorg` |
| 535 | `hrcs_warnscheme` | adminorgfield | 行政组织 | `haos_adminorg` |
| 536 | `hrcs_warnscheme` | position | 岗位 | `hbpm_positionhr` |
| 537 | `hrcs_warnscheme` | warnbizobj | 业务对象 | `hbp_entityobject` |
| 538 | `hrcs_warnscheme` | warnscene | 预警场景 | `hrcs_warnscene` |
| 539 | `hrpi_appointremoverel` | adminorg | 行政组织 | `haos_adminorg` |
| 540 | `hrpi_appointremoverel` | adminorgvid | 历史行政组织 | `haos_adminorg` |
| 541 | `hrpi_appointremoverel` | assignment | 组织分配 | `hrpi_assignment` |
| 542 | `hrpi_appointremoverel` | company | 所属公司 | `haos_adminorg` |
| 543 | `hrpi_appointremoverel` | employee | 员工 | `hrpi_employeenewf7query` |
| 544 | `hrpi_appointremoverel` | empposrel | 员工任职 | `hrpi_empposorgrel` |
| 545 | `hrpi_appointremoverel` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 546 | `hrpi_appointremoverel` | job | 职位 | `hbjm_jobhr` |
| 547 | `hrpi_appointremoverel` | jobvid | 历史职位 | `hbjm_jobhr` |
| 548 | `hrpi_appointremoverel` | position | 岗位 | `hbpm_positionhr` |
| 549 | `hrpi_appointremoverel` | positionvid | 历史岗位 | `hbpm_positionhr` |
| 550 | `hrpi_assignment` | adminorg | 管理部门 | `haos_adminorg` |
| 551 | `hrpi_assignment` | employee | 员工 | `hrpi_employee` |
| 552 | `hrpi_assignment` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 553 | `hrpi_assignment` | orgtype | 组织分类 | `haos_structtype` |
| 554 | `hrpi_assignment` | persongroup | 员工组 | `hbss_employeegroup` |
| 555 | `hrpi_assignment` | primaryassignment | 主组织分配 | `hrpi_assignment` |
| 556 | `hrpi_assignmentmag` | adminorg | 管理部门 | `haos_adminorg` |
| 557 | `hrpi_assignmentmag` | assignment | 组织分配 | `hrpi_assignment` |
| 558 | `hrpi_assignmentmag` | employee | 员工 | `hrpi_employeenewf7query` |
| 559 | `hrpi_assignmentmag` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 560 | `hrpi_assignmentmag` | orgtype | 组织分类 | `haos_structtype` |
| 561 | `hrpi_assignmentmag` | persongroup | 员工组 | `hbss_employeegroup` |
| 562 | `hrpi_assignmentmag` | primaryassignment | 主组织分配 | `hrpi_assignment` |
| 563 | `hrpi_blacklist` | adminorg | 原就任部门 | `haos_adminorg` |
| 564 | `hrpi_blacklist` | adminororg | 黑名单管理组织 | `haos_adminorg` |
| 565 | `hrpi_blacklist` | assignment | 主组织分配 | `hrpi_assignmentf7query` |
| 566 | `hrpi_blacklist` | employee | 员工 | `hrpi_employeenewf7query` |
| 567 | `hrpi_blacklist` | job | 原就任职位 | `hbjm_jobhr` |
| 568 | `hrpi_blacklist` | position | 原就任岗位 | `hbpm_positionhr` |
| 569 | `hrpi_blacklist` | quittypeid | 离职类型 | `hrpi_quittype` |
| 570 | `hrpi_blacklist` | toreason | 加入原因 | `hrpi_toblacklistreason` |
| 571 | `hrpi_blacklist` | toreasonview | 加入原因 | `hrpi_toblacklistreason` |
| 572 | `hrpi_contractinfo` | assignment | 组织分配 | `hrpi_assignment` |
| 573 | `hrpi_contractinfo` | employee | 员工 | `hrpi_employeenewf7query` |
| 574 | `hrpi_contractinfo` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 575 | `hrpi_contractinfo` | signcompany | 聘用单位 | `hbss_signcompany` |
| 576 | `hrpi_debardinfo` | employee | 员工 | `hrpi_employeenewf7query` |
| 577 | `hrpi_dispatchinfo` | adminorg | 部门 | `haos_adminorg` |
| 578 | `hrpi_dispatchinfo` | assignment | 组织分配 | `hrpi_assignment` |
| 579 | `hrpi_dispatchinfo` | company | 公司 | `haos_adminorg` |
| 580 | `hrpi_dispatchinfo` | dispworkplace | 常驻工作地 | `hbss_workplace` |
| 581 | `hrpi_dispatchinfo` | employee | 员工 | `hrpi_employeenewf7query` |
| 582 | `hrpi_dispatchinfo` | empposorgrel | 任职经历 | `hrpi_empposorgrel` |
| 583 | `hrpi_dispatchinfo` | job | 职位 | `hbjm_jobhr` |
| 584 | `hrpi_dispatchinfo` | position | 岗位 | `hbpm_positionhr` |
| 585 | `hrpi_empcadre` | appremoverel | 任免经历 | `hrpi_appointremoverel` |
| 586 | `hrpi_empcadre` | employee | 员工 | `hrpi_employeenewf7query` |
| 587 | `hrpi_empcadre` | empposorgrel | 任职经历 | `hrpi_empposorgrel` |
| 588 | `hrpi_empentrel` | candidate | 候选人id | `hcf_candidate` |
| 589 | `hrpi_empentrel` | employee | 员工 | `hrpi_employeenewf7query` |
| 590 | `hrpi_empentrel` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 591 | `hrpi_empentrel` | enterprise | 用人单位 | `hbss_enterprise` |
| 592 | `hrpi_empjobrel` | adminorg | 行政组织 | `haos_adminorg` |
| 593 | `hrpi_empjobrel` | assignment | 组织分配 | `hrpi_assignment` |
| 594 | `hrpi_empjobrel` | beforeadjempjobrel | 调整前职级职等 | `hrpi_empjobrel` |
| 595 | `hrpi_empjobrel` | company | 所属公司 | `haos_adminorg` |
| 596 | `hrpi_empjobrel` | employee | 员工 | `hrpi_employeenewf7query` |
| 597 | `hrpi_empjobrel` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 598 | `hrpi_empjobrel` | job | 职位 | `hbjm_jobhr` |
| 599 | `hrpi_empjobrel` | jobclass | 职位类 | `hbjm_jobclasshr` |
| 600 | `hrpi_empjobrel` | jobfamily | 职位族 | `hbjm_jobfamilyhr` |
| 601 | `hrpi_empjobrel` | jobgrade | 职等 | `hbjm_jobgradehr` |
| 602 | `hrpi_empjobrel` | jobgradescm | 职等方案 | `hbjm_jobgradescmhr` |
| 603 | `hrpi_empjobrel` | joblevel | 职级 | `hbjm_joblevelhr` |
| 604 | `hrpi_empjobrel` | joblevelscm | 职级方案 | `hbjm_joblevelscmhr` |
| 605 | `hrpi_empjobrel` | jobscm | 职位体系方案 | `hbjm_jobscmhr` |
| 606 | `hrpi_empjobrel` | jobseq | 职位序列 | `hbjm_jobseqhr` |
| 607 | `hrpi_empjobrel` | position | 岗位 | `hbpm_positionhr` |
| 608 | `hrpi_employee` | assignment | 组织分配 | `hrpi_assignment` |
| 609 | `hrpi_employee` | globalperson | 全球员工 | `hrpi_globalperson` |
| 610 | `hrpi_employee` | oldemployee | 前员工 | `hrpi_employee` |
| 611 | `hrpi_employee` | primaryemployee | 主员工 | `hrpi_employee` |
| 612 | `hrpi_employee` | procreatstatus | 生育状况 | `hbss_procreatstatus` |
| 613 | `hrpi_employeetaxcn` | adminorg | 行政组织 | `haos_adminorg` |
| 614 | `hrpi_employeetaxcn` | assignment | 组织分配号 | `hrpi_assignmentf7query` |
| 615 | `hrpi_employeetaxcn` | employee | 工号 | `hrpi_employeenewf7query` |
| 616 | `hrpi_employeetaxcn` | taxunit | 纳税单位 | `hbss_taxunit` |
| 617 | `hrpi_emporgrelall` | assignment | 组织分配 | `hrpi_assignment` |
| 618 | `hrpi_emporgrelall` | employee | 员工 | `hrpi_employeenewf7query` |
| 619 | `hrpi_empposorgrel` | adminorg | 行政组织 | `haos_adminorg` |
| 620 | `hrpi_empposorgrel` | adminorgvid | 历史行政组织 | `haos_adminorg` |
| 621 | `hrpi_empposorgrel` | assignment | 组织分配 | `hrpi_assignment` |
| 622 | `hrpi_empposorgrel` | company | 所属公司 | `haos_adminorg` |
| 623 | `hrpi_empposorgrel` | contractworkplace | 协议工作地 | `hbss_workplace` |
| 624 | `hrpi_empposorgrel` | employee | 员工 | `hrpi_employeenewf7query` |
| 625 | `hrpi_empposorgrel` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 626 | `hrpi_empposorgrel` | job | 职位 | `hbjm_jobhr` |
| 627 | `hrpi_empposorgrel` | jobgrade | 职等 | `hbjm_jobgradehr` |
| 628 | `hrpi_empposorgrel` | jobgradescm | 职等方案 | `hbjm_jobgradescmhr` |
| 629 | `hrpi_empposorgrel` | joblevel | 职级 | `hbjm_joblevelhr` |
| 630 | `hrpi_empposorgrel` | joblevelscm | 职级方案 | `hbjm_joblevelscmhr` |
| 631 | `hrpi_empposorgrel` | jobscm | 职位体系方案 | `hbjm_jobscmhr` |
| 632 | `hrpi_empposorgrel` | jobvid | 历史职位 | `hbjm_jobhr` |
| 633 | `hrpi_empposorgrel` | position | 岗位 | `hbpm_positionhr` |
| 634 | `hrpi_empposorgrel` | positionvid | 历史岗位 | `hbpm_positionhr` |
| 635 | `hrpi_empposorgrel` | workcalendar | 工作日历 | `hrcs_workingplanquery` |
| 636 | `hrpi_empposorgrel` | workplace | 常驻工作地 | `hbss_workplace` |
| 637 | `hrpi_empproexp` | companynature | 企业性质 | `hbss_empnature` |
| 638 | `hrpi_empproexp` | employee | 员工 | `hrpi_employeenewf7query` |
| 639 | `hrpi_empstage` | employee | 员工 | `hrpi_employeenewf7query` |
| 640 | `hrpi_empsuprel` | assignment | 组织分配 | `hrpi_assignment` |
| 641 | `hrpi_empsuprel` | employee | 员工 | `hrpi_employeenewf7query` |
| 642 | `hrpi_empsuprel` | empposorgrel | 任职 | `hrpi_empposf7query` |
| 643 | `hrpi_empsuprel` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 644 | `hrpi_empsuprel` | reporttype | 汇报关系类型 | `hbpm_reportcoreltype` |
| 645 | `hrpi_empsuprel` | superiorempposorgrel | 汇报上级 | `hrpi_empposf7query` |
| 646 | `hrpi_emptrainfile` | employee | 员工 | `hrpi_employeenewf7query` |
| 647 | `hrpi_emptutor` | employee | 员工 | `hrpi_employeenewf7query` |
| 648 | `hrpi_emptutor` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 649 | `hrpi_emptutor` | tutor | 导师 | `hrpi_employeenewf7query` |
| 650 | `hrpi_emrgcontact` | employee | 员工 | `hrpi_employeenewf7query` |
| 651 | `hrpi_familymemb` | employee | 员工 | `hrpi_employeenewf7query` |
| 652 | `hrpi_fertilityinfo` | employee | 员工 | `hrpi_employeenewf7query` |
| 653 | `hrpi_partymember` | employee | 员工 | `hrpi_employeenewf7query` |
| 654 | `hrpi_peraddress` | employee | 员工 | `hrpi_employeenewf7query` |
| 655 | `hrpi_percontact` | employee | 员工 | `hrpi_employeenewf7query` |
| 656 | `hrpi_percre` | employee | 员工 | `hrpi_employeenewf7query` |
| 657 | `hrpi_pereduexp` | employee | 员工 | `hrpi_employeenewf7query` |
| 658 | `hrpi_pereduexp` | graduateschool | 毕业院校 | `hbss_college` |
| 659 | `hrpi_perfresult` | assignment | 组织分配 | `hrpi_assignment` |
| 660 | `hrpi_perfresult` | employee | 员工 | `hrpi_employeenewf7query` |
| 661 | `hrpi_perfresult` | empposrel | 任职经历 | `hrpi_empposorgrel` |
| 662 | `hrpi_perfresult` | rulescore | 评分分制 | `hbss_scoresystem` |
| 663 | `hrpi_perhobby` | employee | 员工 | `hrpi_employeenewf7query` |
| 664 | `hrpi_perlgability` | employee | 员工 | `hrpi_employeenewf7query` |
| 665 | `hrpi_perocpqual` | employee | 员工 | `hrpi_employeenewf7query` |
| 666 | `hrpi_perpractqual` | employee | 员工 | `hrpi_employeenewf7query` |
| 667 | `hrpi_perprotitle` | employee | 员工 | `hrpi_employeenewf7query` |
| 668 | `hrpi_perprotitle` | professional | 职称 | `hbss_protitle` |
| 669 | `hrpi_perregion` | employee | 员工 | `hrpi_employeenewf7query` |
| 670 | `hrpi_perrprecord` | employee | 员工 | `hrpi_employeenewf7query` |
| 671 | `hrpi_perserlen` | assignment | 组织分配 | `hrpi_assignment` |
| 672 | `hrpi_perserlen` | employee | 员工 | `hrpi_employeenewf7query` |
| 673 | `hrpi_perserlen` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 674 | `hrpi_personuserrel` | employee | 员工 | `hrpi_employeenewf7query` |
| 675 | `hrpi_preworkexp` | businesstypeid | 企业性质 | `hbss_empnature` |
| 676 | `hrpi_preworkexp` | companyscale | 公司规模 | `hbss_companyscale` |
| 677 | `hrpi_preworkexp` | employee | 员工 | `hrpi_employeenewf7query` |
| 678 | `hrpi_rotationinfo` | adminorg | 轮岗部门 | `haos_adminorg` |
| 679 | `hrpi_rotationinfo` | assignment | 组织分配 | `hrpi_assignment` |
| 680 | `hrpi_rotationinfo` | company | 轮岗公司 | `haos_adminorg` |
| 681 | `hrpi_rotationinfo` | employee | 员工 | `hrpi_employeenewf7query` |
| 682 | `hrpi_rotationinfo` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 683 | `hrpi_rotationinfo` | job | 担任职位 | `hbjm_jobhr` |
| 684 | `hrpi_rotationinfo` | position | 担任岗位 | `hbpm_positionhr` |
| 685 | `hrpi_rsmpatinv` | employee | 员工 | `hrpi_employeenewf7query` |
| 686 | `hrpi_rsmproskl` | employee | 员工 | `hrpi_employeenewf7query` |
| 687 | `hrpi_terminationinfo` | assignment | 组织分配 | `hrpi_assignment` |
| 688 | `hrpi_terminationinfo` | employee | 员工 | `hrpi_employeenewf7query` |
| 689 | `hrpi_terminationinfo` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 690 | `hrpi_terminationinfo` | termtype | 离职类型 | `hrpi_quittype` |
| 691 | `hrpi_trialperiod` | assignment | 组织分配 | `hrpi_assignment` |
| 692 | `hrpi_trialperiod` | employee | 员工 | `hrpi_employeenewf7query` |
| 693 | `hrpi_trialperiod` | empstage | 雇佣阶段 | `hrpi_empstage` |
| 694 | `hrptmc_algorithmcol` | rptconfig | 报表配置 | `hrptmc_reportconfig` |
| 695 | `hrptmc_anobjentityrel` | anobj | 分析对象id | `hrptmc_anobjtemplib` |
| 696 | `hrptmc_anobjentityrel` | entityid | 实体ID | `hrptmc_anobjjoinentity` |
| 697 | `hrptmc_anobjentityrel` | joinentityid | 关联实体ID | `hrptmc_anobjjoinentity` |
| 698 | `hrptmc_anobjextract` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 699 | `hrptmc_anobjfieldmap` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 700 | `hrptmc_anobjgroupfield` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 701 | `hrptmc_anobjgroupfield` | anobjcalfield | 参照计算字段 | `hrptmc_calculatefield` |
| 702 | `hrptmc_anobjgroupfield` | anobjfield | 参照实体字段 | `hrptmc_anobjfield_f7` |
| 703 | `hrptmc_anobjjoinentity` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 704 | `hrptmc_anobjpivot` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 705 | `hrptmc_anobjsidebar` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 706 | `hrptmc_anobjsidebar` | groupfield | 分组赋值字段 | `hrptmc_anobjgroupfield` |
| 707 | `hrptmc_busiservice` | app | 所属应用 | `hbp_devportal_bizapp` |
| 708 | `hrptmc_calculatefield` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 709 | `hrptmc_calculatefield` | refcalfield | 引用计算字段 | `hrptmc_calculatefield` |
| 710 | `hrptmc_calculatefield` | refpreindex | 引用预置指标 | `hrptmc_preindex` |
| 711 | `hrptmc_calculatefield` | report | 报表 | `hrptmc_reportmanage` |
| 712 | `hrptmc_calmaxlen` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 713 | `hrptmc_calmaxlen` | calfield | 计算字段 | `hrptmc_calculatefield` |
| 714 | `hrptmc_colcustomsort` | id | 列字段 | `hrptmc_colfield` |
| 715 | `hrptmc_colfield` | anobjfield | 分析对象字段 | `hrptmc_anobjfield_f7` |
| 716 | `hrptmc_colfield` | calcidxfield | 计算指标字段 | `hrptmc_calculatefield` |
| 717 | `hrptmc_colfield` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 718 | `hrptmc_colfield` | workrpt | 工作表 | `hrptmc_workreport` |
| 719 | `hrptmc_customsort` | rptmanage | 报表 | `hrptmc_reportmanage` |
| 720 | `hrptmc_customsort` | workrpt | 工作表 | `hrptmc_workreport` |
| 721 | `hrptmc_datastoreinfo` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 722 | `hrptmc_datastoreinfo` | report | 报表 | `hrptmc_reportmanage` |
| 723 | `hrptmc_dimensioncount` | field | 分析对象查询字段 | `hrptmc_anobjfield_f7` |
| 724 | `hrptmc_dimensioncount` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 725 | `hrptmc_dimmap` | preindex | 预置指标 | `hrptmc_preindex` |
| 726 | `hrptmc_dimmap` | report | 报表 | `hrptmc_reportmanage` |
| 727 | `hrptmc_dimmap` | workrpt | 工作表 | `hrptmc_workreport` |
| 728 | `hrptmc_dispscmchg` | rptmanage | 报表ID | `hrptmc_reportmanage` |
| 729 | `hrptmc_dispscmchg` | workrpt | 工作表 | `hrptmc_workreport` |
| 730 | `hrptmc_esmapping` | esindex | es索引 | `hrptmc_esindex` |
| 731 | `hrptmc_filesourcetable` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 732 | `hrptmc_filesourcetable` | virtualobj | 虚拟对象 | `hrptmc_virtualentity` |
| 733 | `hrptmc_filter` | anobjfield | 分析对象字段 | `hrptmc_anobjfield_f7` |
| 734 | `hrptmc_filter` | begindate | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 735 | `hrptmc_filter` | begindate_ | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 736 | `hrptmc_filter` | begindate_0 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 737 | `hrptmc_filter` | begindate_1 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 738 | `hrptmc_filter` | begindate_10 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 739 | `hrptmc_filter` | begindate_11 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 740 | `hrptmc_filter` | begindate_12 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 741 | `hrptmc_filter` | begindate_13 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 742 | `hrptmc_filter` | begindate_14 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 743 | `hrptmc_filter` | begindate_15 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 744 | `hrptmc_filter` | begindate_16 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 745 | `hrptmc_filter` | begindate_17 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 746 | `hrptmc_filter` | begindate_18 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 747 | `hrptmc_filter` | begindate_19 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 748 | `hrptmc_filter` | begindate_2 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 749 | `hrptmc_filter` | begindate_3 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 750 | `hrptmc_filter` | begindate_4 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 751 | `hrptmc_filter` | begindate_5 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 752 | `hrptmc_filter` | begindate_6 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 753 | `hrptmc_filter` | begindate_7 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 754 | `hrptmc_filter` | begindate_8 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 755 | `hrptmc_filter` | begindate_9 | 历史时段查询-开始日期 | `hrptmc_anobjfield_f7` |
| 756 | `hrptmc_filter` | enddate | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 757 | `hrptmc_filter` | enddate_ | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 758 | `hrptmc_filter` | enddate_0 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 759 | `hrptmc_filter` | enddate_1 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 760 | `hrptmc_filter` | enddate_10 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 761 | `hrptmc_filter` | enddate_11 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 762 | `hrptmc_filter` | enddate_12 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 763 | `hrptmc_filter` | enddate_13 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 764 | `hrptmc_filter` | enddate_14 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 765 | `hrptmc_filter` | enddate_15 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 766 | `hrptmc_filter` | enddate_16 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 767 | `hrptmc_filter` | enddate_17 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 768 | `hrptmc_filter` | enddate_18 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 769 | `hrptmc_filter` | enddate_19 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 770 | `hrptmc_filter` | enddate_2 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 771 | `hrptmc_filter` | enddate_3 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 772 | `hrptmc_filter` | enddate_4 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 773 | `hrptmc_filter` | enddate_5 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 774 | `hrptmc_filter` | enddate_6 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 775 | `hrptmc_filter` | enddate_7 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 776 | `hrptmc_filter` | enddate_8 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 777 | `hrptmc_filter` | enddate_9 | 历史时段查询-结束日期 | `hrptmc_anobjfield_f7` |
| 778 | `hrptmc_filter` | groupfield | 分组赋值字段 | `hrptmc_anobjgroupfield` |
| 779 | `hrptmc_filter` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 780 | `hrptmc_filter` | splitdate | 日期拆分字段 | `hrptmc_splitdate` |
| 781 | `hrptmc_filterextfield` | report | 报表 | `hrptmc_reportmanage` |
| 782 | `hrptmc_mysubscribe` | esindex | es索引 | `hrptmc_esindex` |
| 783 | `hrptmc_mysubscribe` | subscriberecord | 订阅记录 | `hrptmc_subscriberecord` |
| 784 | `hrptmc_permrule` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 785 | `hrptmc_permrule` | anobjfield | 分析对象字段 | `hrptmc_anobjfield_f7` |
| 786 | `hrptmc_permrule` | permobj | 参照控权对象 | `hbp_entityobject` |
| 787 | `hrptmc_preindex` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 788 | `hrptmc_preindex` | selparam | 参数选择 | `hrptmc_selparam` |
| 789 | `hrptmc_preindex` | service | 服务 | `hrptmc_busiservice` |
| 790 | `hrptmc_publishmenu` | reportmanage | 报表管理 | `hrptmc_reportmanage` |
| 791 | `hrptmc_queryscheme` | rptmanage | 方案所属的报表 | `hrptmc_reportmanage` |
| 792 | `hrptmc_queryscmchg` | rptmanage | 报表ID | `hrptmc_reportmanage` |
| 793 | `hrptmc_queryscmchg` | scheme | 方案id | `hrptmc_queryscheme` |
| 794 | `hrptmc_reflineconf` | report | 报表 | `hrptmc_reportmanage` |
| 795 | `hrptmc_reflineconf` | workrpt | 工作表 | `hrptmc_workreport` |
| 796 | `hrptmc_reportconfig` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 797 | `hrptmc_reportconfig` | workrpt | 工作表 | `hrptmc_workreport` |
| 798 | `hrptmc_reportjump` | jumpreport | 跳转报表 | `hrptmc_reportmanage` |
| 799 | `hrptmc_reportjump` | report | 报表 | `hrptmc_reportmanage` |
| 800 | `hrptmc_reportjump` | workrpt | 工作表 | `hrptmc_workreport` |
| 801 | `hrptmc_reportmanage` | anobjid | 分析对象 | `hrptmc_anobjtemplib` |
| 802 | `hrptmc_reportmapping` | report | 报表 | `hrptmc_reportmanage` |
| 803 | `hrptmc_reportmark` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 804 | `hrptmc_reportpreindex` | preindex | 预置指标 | `hrptmc_preindex` |
| 805 | `hrptmc_reportpreindex` | report | 报表 | `hrptmc_reportmanage` |
| 806 | `hrptmc_rowcustomsort` | id | 行字段 | `hrptmc_rowfield` |
| 807 | `hrptmc_rowfield` | anobjfield | 分析对象字段 | `hrptmc_anobjfield_f7` |
| 808 | `hrptmc_rowfield` | calcidxfield | 计算指标字段 | `hrptmc_calculatefield` |
| 809 | `hrptmc_rowfield` | dataformat | 数据格式 | `hrptmc_rowfield` |
| 810 | `hrptmc_rowfield` | parentid | 父字段 | `hrptmc_rowfield` |
| 811 | `hrptmc_rowfield` | preidxfield | 预置指标字段 | `hrptmc_preindex` |
| 812 | `hrptmc_rowfield` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 813 | `hrptmc_rowfield` | workrpt | 工作表 | `hrptmc_workreport` |
| 814 | `hrptmc_rptcomref` | commonsort | 通用排序 | `hrptmc_commonsort` |
| 815 | `hrptmc_rptcomref` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 816 | `hrptmc_rptcomref` | workrpt | 工作表 | `hrptmc_workreport` |
| 817 | `hrptmc_rptdispscm` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 818 | `hrptmc_rptdispscm` | workrpt | 工作表 | `hrptmc_workreport` |
| 819 | `hrptmc_rptdispscmcol` | colfield | 列字段 | `hrptmc_colfield` |
| 820 | `hrptmc_rptdispscmcol` | rptdispscm | 报表显示方案 | `hrptmc_rptdisscmety` |
| 821 | `hrptmc_rptdispscmidx` | rowfield | 行字段 | `hrptmc_rowfield` |
| 822 | `hrptmc_rptdispscmidx` | rptdispscm | 报表显示方案 | `hrptmc_rptdisscmety` |
| 823 | `hrptmc_rptdispscmrow` | rowfield | 行字段 | `hrptmc_rowfield` |
| 824 | `hrptmc_rptdispscmrow` | rptdispscm | 报表显示方案 | `hrptmc_rptdisscmety` |
| 825 | `hrptmc_rptdisscmety` | colfield | 列维度 | `hrptmc_colfield` |
| 826 | `hrptmc_rptdisscmety` | indexfield | 指标 | `hrptmc_rowfield` |
| 827 | `hrptmc_rptdisscmety` | parent | 显示方案 | `hrptmc_rptdispscm` |
| 828 | `hrptmc_rptdisscmety` | parentfield | 父行维度 | `hrptmc_rowfield` |
| 829 | `hrptmc_rptdisscmety` | rowfield | 行维度 | `hrptmc_rowfield` |
| 830 | `hrptmc_rptmarkcontent` | rptmark | 报表说明 | `hrptmc_reportmark` |
| 831 | `hrptmc_share_filterscheme` | rptmanage | 方案所属的报表 | `hrptmc_reportmanage` |
| 832 | `hrptmc_share_filterscheme` | scheme | 方案名称 | `hrptmc_queryscheme` |
| 833 | `hrptmc_sharefilterrange` | sharerecord | 分享记录 | `hrptmc_sharerecord` |
| 834 | `hrptmc_sharefilterrange` | userdissche | 用户显示方案 | `hrptmc_userdispscm` |
| 835 | `hrptmc_sharerecord` | esindex | es索引 | `hrptmc_esindex` |
| 836 | `hrptmc_sharerecord` | reportsubscribe | 报表订阅 | `hrptmc_subscriberecord` |
| 837 | `hrptmc_splitdate` | anobjfield | 分析对象字段 | `hrptmc_anobjfield_f7` |
| 838 | `hrptmc_splitdate` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 839 | `hrptmc_subscriberecord` | report | 报表 | `hrptmc_reportmanage` |
| 840 | `hrptmc_syncpolicy` | anobj | 分析对象 | `hrptmc_anobjtemplib` |
| 841 | `hrptmc_unitestentity01` | adminorg | 行政组织 | `haos_adminorg` |
| 842 | `hrptmc_unitestentity01` | entrybasedata | 分录基础资料 | `hbss_cloud_app` |
| 843 | `hrptmc_unitestentity01` | hisbasedata | 时序历史基础资料 | `hbss_college` |
| 844 | `hrptmc_unitestentity02` | report01 | 报表单元测试实体01 | `hrptmc_unitestentity01` |
| 845 | `hrptmc_userdispscm` | colfield | 列维度 | `hrptmc_colfield` |
| 846 | `hrptmc_userdispscm` | indexfield | 指标 | `hrptmc_rowfield` |
| 847 | `hrptmc_userdispscm` | rowfield | 行维度 | `hrptmc_rowfield` |
| 848 | `hrptmc_userdispscm` | rptmanage | 方案所属的报表 | `hrptmc_reportmanage` |
| 849 | `hrptmc_userdispscm` | workrpt | 工作表 | `hrptmc_workreport` |
| 850 | `hrptmc_virtentfields` | group | 字段分组 | `hrptmc_virtualfieldgroup` |
| 851 | `hrptmc_virtentityclass` | businessfield | 所属业务领域 | `hbss_hrbusinessfield` |
| 852 | `hrptmc_virtualentity` | businessfield | 所属业务领域（废弃） | `hbss_hrbusinessfield` |
| 853 | `hrptmc_virtualentity` | classpath | 虚拟对象处理类 | `hrptmc_virtentityclass` |
| 854 | `hrptmc_virtualentity` | group | 字段分组 | `hrptmc_virtualfieldgroup` |
| 855 | `hrptmc_workreport` | rptmanage | 报表管理 | `hrptmc_reportmanage` |
| 856 | `hrss_customfilter` | basedata | 基础资料 | `hbp_entityobject` |
| 857 | `hrss_essyncscheme` | searchobj | 搜索对象 | `hrss_searchobject` |
| 858 | `hrss_essynrecord` | essyncschemeid | ES同步方案配置 | `hrss_essyncscheme` |
| 859 | `hrss_schobjentityrel` | entityid | 实体ID | `hrss_schobjjoinentity` |
| 860 | `hrss_schobjentityrel` | joinentityid | 关联实体ID | `hrss_schobjjoinentity` |
| 861 | `hrss_schobjentityrel` | searchobj | 搜索对象 | `hrss_searchobject` |
| 862 | `hrss_schobjjoinentity` | searchobj | 搜索对象 | `hrss_searchobject` |
| 863 | `hrss_schobjqueryfield` | searchobj | 搜索对象 | `hrss_searchobject` |
| 864 | `hrss_searchconfig` | basedatafield | 业务对象 | `hbp_entityobject` |
| 865 | `hrss_searchobject` | cloud | 所属业务领域 | `hbss_hrbusinessfield` |
| 866 | `hrss_searchscene` | app | 所属应用 | `hbp_devportal_bizapp` |
| 867 | `hrss_searchscene` | customfilter | 自定义过滤项 | `hrss_customfilter` |
| 868 | `hrss_searchscene` | entitytype | 基础资料 | `hbp_entityobject` |
| 869 | `hrss_searchscene` | group | 所属分组 | `hrss_scenefiltergroup` |
| 870 | `hrss_searchscene` | label | 标签 | `hrcs_label` |
| 871 | `hrss_searchscene` | labelfilterid | 标签 | `hrcs_label` |
| 872 | `hrss_searchscene` | queryfield | 搜索对象查询字段 | `hrss_schobjqueryfield` |
| 873 | `hrss_searchscene` | searchobj | 搜索对象 | `hrss_searchobject` |
| 874 | `hrss_searchscene` | searchobjentityid | 所属实体 | `hrss_schobjjoinentity` |
| 875 | `hrss_searchscene` | searchobjfield | 字段 | `hrss_schobjqueryfield` |
| 876 | `hrss_searchweight` | aiwordcategory | AI词性 | `hrss_aiwordcategory` |
| 877 | `hrss_searchweight` | grade | 权重等级 | `hrss_searchwgentries` |
| 878 | `hrss_searchweight` | searchobjentityid | 所属业务对象 | `hrss_schobjjoinentity` |
| 879 | `hrss_searchweight` | usescene | 搜索场景 | `hrss_searchscene` |
| 880 | `hrss_searchwtgrade` | usescene | 搜索场景 | `hrss_searchscene` |
| 881 | `hrss_syncparam` | searchobj | 搜索对象 | `hrss_searchobject` |
| 882 | `hrss_userlastsearchkey` | searchpage | 搜索页面 | `hrss_searchconfig` |
| 883 | `hrss_userlastsearchkey` | searchscene | 搜索场景 | `hrss_searchscene` |
| 884 | `hrss_usersearchlog` | searchpage | 搜索页面 | `hrss_searchconfig` |
| 885 | `hrss_usersearchlog` | searchscene | 搜索场景 | `hrss_searchscene` |

**ER关系总数**: 885

---

## 8. 需要补充生成的元数据详情

所有共享引用的代表元数据均已有详情页，无需额外生成。
