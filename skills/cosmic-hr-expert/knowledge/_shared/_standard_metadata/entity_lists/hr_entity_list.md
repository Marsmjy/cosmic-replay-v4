# HR — 核心人力云 元数据清单

> 共 **849** 个表单/实体（含共享物理表成员），去重后 **585** 行（折叠 264 条共享成员）

## 概览

| 分类 | 数量 |
|------|------|
| 基础资料 | 123 |
| 基础资料-非主数据类 | 11 |
| 单据 | 50 |
| 动态表单 | 212 |
| 布局 | 15 |
| 查询模型 | 11 |
| 移动端 | 160 |
| 其他 | 3 |

## 共享物理表汇总

| 物理表 | 共享元数据数 | 元数据列表 |
|--------|-------------|------------|
| t_hcf_canaddress | 4 | hom_canaddress, hom_canaddress_c, hom_canaddress_i, hcf_canaddress |
| t_hcf_canbankcard | 4 | hom_canbankcard, hom_canbankcard_c, hom_canbankcard_i, hcf_canbankcard |
| t_hcf_cancontact | 4 | hom_cancontact, hom_cancontact_c, hom_cancontact_i, hcf_cancontact |
| t_hcf_cancontactinfo | 4 | hom_cancontactinfo, hom_cancontactinfo_c, hom_cancontactinfo_i, hcf_cancontactinfo |
| t_hcf_cancre | 4 | hom_cancre, hom_cancre_c, hom_cancre_i, hcf_cancre |
| t_hcf_candidate | 4 | hom_candidate, hom_candidate_c, hom_candidate_i, hcf_candidate |
| t_hcf_caneduexp | 4 | hom_caneduexp, hom_caneduexp_c, hom_caneduexp_i, hcf_caneduexp |
| t_hcf_canfamily | 4 | hom_canfamily, hom_canfamily_c, hom_canfamily_i, hcf_canfamily |
| t_hcf_canlgability | 4 | hom_canlgability, hom_canlgability_c, hom_canlgability_i, hcf_canlgability |
| t_hcf_canocpqual | 2 | hom_canocpqual, hcf_canocpqual |
| t_hcf_canprework | 4 | hom_canprework, hom_canprework_c, hom_canprework_i, hcf_canprework |
| t_hcf_canprojectexp | 2 | hom_canprojectexp, hcf_canprojectexp |
| t_hcf_cantraining | 2 | hom_cantraining, hcf_cantraining |
| t_hcf_personalarea | 4 | hom_personalarea, hom_personalarea_c, hom_personalarea_i, hcf_personalarea |
| t_hcf_rsmhobby | 2 | hom_rsmhobby, hcf_rsmhobby |
| t_hcf_rsmpatinv | 4 | hom_rsmpatinv, hom_rsmpatinv_c, hom_rsmpatinv_i, hcf_rsmpatinv |
| t_hcf_rsmproskl | 2 | hom_rsmproskl, hcf_rsmproskl |
| t_hdm_parttimebatch | 2 | hdm_batchparttime, hdm_batchparttime_hstc |
| t_hdm_parttimesuperior | 3 | hdm_parttimeapply_hstc, hdm_parttimeapplybill, hdm_parttimebillentry |
| t_hdm_regaskdetails | 3 | hdm_regaskdetails, hdm_regaskdetailscard, hdm_regaskreply |
| t_hdm_regbasebill | 7 | hdm_entryregbill, hdm_regapplysource, hdm_regbasebill, hdm_regbasebill_hstc, hdm_regselfhelp_supply, hdm_regselfhelpbill, hdm_regselfhelpbill_hstc |
| t_hdm_regbatch | 2 | hdm_batchregbill, hdm_batchregbill_hstc |
| t_hdm_transferbatch | 3 | hdm_batchtransferbasedata, hdm_transferbatch, hdm_transferbatch_hstc |
| t_hdm_transferbill | 22 | hdm_infochangebill, hdm_mytrans_hr_hstc, hdm_mytrans_hr_layout, hdm_mytrans_leader_hstc, hdm_mytrans_leader_layout, hdm_mytransferbasebill, hdm_mytransferbill, hdm_mytransferbill_hstc, hdm_transferapply, hdm_transferapply_hstc, hdm_transferbasebill, hdm_transferbatchentry, hdm_transferconfirmbill, hdm_transferinbill, hdm_transferinbill_hstc, hdm_transferinbill_layout, hdm_transferintoout_hstc, hdm_transferlayoutbill, hdm_transferoutbil_layout, hdm_transferoutbill, hdm_transferoutbill_hstc, hdm_transferouttoin_hstc |
| t_hlcm_activity | 9 | hlcm_activity, hlcm_electric_beginlist, hlcm_electric_checklist, hlcm_electric_confirmlist, hlcm_electric_csignlist, hlcm_electric_esignlist, hlcm_paper_beginlist, hlcm_paper_checklist, hlcm_paper_complist |
| t_hlcm_contract | 8 | hlcm_contract, hlcm_contract_allf7, hlcm_contractcr, hlcm_contractfileemp, hlcm_contractfileempcr, hlcm_contractfileother, hlcm_contractfileothercr, hlcm_contractsource |
| t_hlcm_contracttemp | 2 | hlcm_contracttemplate, hlcm_contracttemplatech |
| t_hlcm_prewarn | 4 | hlcm_prewarn, hlcm_prewarn_type1list, hlcm_prewarn_type2list, hlcm_prewarn_type3list |
| t_hlcm_renewinquery | 3 | hlcm_inquerytask, hlcm_renewinquery, hlcm_renewinquerybase |
| t_hlcm_signbill | 38 | hlcm_contractapply, hlcm_contractapplybase, hlcm_contractapplycancel, hlcm_contractapplychange, hlcm_contractapplyend, hlcm_contractapplyhandle, hlcm_contractapplynew, hlcm_contractapplyrenew, hlcm_contractcancel_hstc, hlcm_contractchange_hstc, hlcm_contractend_hstc, hlcm_contractnew_hstc, hlcm_contractrenew_hstc, hlcm_electricsign_all, hlcm_electricsign_archive, hlcm_electricsign_begin, hlcm_electricsign_check, hlcm_electricsign_confirm, hlcm_electricsign_csign, hlcm_electricsign_esign, hlcm_electricsign_stop, hlcm_empprotocol, hlcm_empprotocolnew, hlcm_empprotocolnew_hstc, hlcm_empprotocolrel_hstc, hlcm_empprotocolrelieve, hlcm_empprotocolterminate, hlcm_otheragreements, hlcm_otheragreements_hstc, hlcm_papersign_all, hlcm_papersign_archive, hlcm_papersign_begin, hlcm_papersign_check, hlcm_papersign_comp, hlcm_papersign_stop, hlcm_signbill_tobase, hlcm_signmanage_all, hlcm_signmanagebase |
| t_hom_activityinfodetail | 13 | hom_active, hom_activeall_layout, hom_activebase, hom_activecompleted_l, hom_activehandle, hom_activehandling_l, hom_activeterminated_l, hom_collaborationall_lay, hom_collaborationdetail, hom_collaborationdone_lay, hom_collaborationing_lay, hom_collaborationstop_lay, hom_onbrdcollaborate |
| t_hom_collect | 21 | hom_acceptmanageall, hom_acceptmanagebreakup, hom_acceptmanageing, hom_acceptmanagepass, hom_acceptmanageunpass, hom_collect, hom_collectapproveall, hom_collectapprovefail, hom_collectapproveing, hom_collectapprovepass, hom_collectapprovereject, hom_collectapproveremit, hom_collectapproveterm, hom_collectmanageall, hom_collectmanagefin, hom_collectmanagesub, hom_collectmanageterm, hom_collectphone, hom_onbrdaccepte, hom_onbrdapprove, hom_onbrdcollect |
| t_hom_onbrdbill | 35 | hom_allcheckin, hom_allreservation, hom_breakupcheckin, hom_breakupreservation, hom_checkinbilltpl, hom_checkinfo, hom_checkinfo_c, hom_checkinfo_i, hom_checkininfo, hom_exceptioncheckin, hom_hascheckin, hom_hasreservation, hom_onbrdbillbase, hom_onbrdbilltpl, hom_onbrdbook, hom_onbrdcheckin, hom_onbrdcheckinbody, hom_onbrdinfo, hom_onbrdpersonbilltpl, hom_onbrdpersoninfo, hom_personallonbrd, hom_personbreakuponbrd, hom_personhasonbrd, hom_personwaitonbrd, hom_personwaitstart, hom_reservationbody, hom_reservationhrconfirm, hom_reservationinfo, hom_reservationonbrd, hom_reservationtpl, hom_waitcheckin, hom_waitconfirm, hom_waitresercheckin, hom_waitreservation, hom_wbwaitonblist |
| t_hom_onbrdinvite | 4 | hom_invitesendbill, hom_onbrdinvite, hom_onbrdinviteall, hom_onbrdinviteinfo |
| t_hom_prebatchonbrdbill | 2 | hom_preonbrdbasebill, hom_preonbrdbasedata |
| t_hpfs_chgrecord | 3 | hpfs_chgrecord, hspm_chgrecord, hspm_chgrecord_ly |
| t_hpfs_crossvalidation | 2 | hpfs_chgcrossvalid, hpfs_crossvalidation |
| t_hpfs_personflow | 3 | hpfs_personflow, hpfs_personflow_file, hspm_personflow_ly |
| t_hrpi_appointremoverel | 3 | hspm_appointremove_ly, hspm_appointremoverel_e, hrpi_appointremoverel |
| t_hrpi_assignment | 3 | hspm_assignment_ly, htm_quitassignment, hrpi_assignment |
| t_hrpi_assignmentmag | 2 | hspm_assignmentmag_ly, hrpi_assignmentmag |
| t_hrpi_blacklist | 2 | hpfs_blacklist, hrpi_blacklist |
| t_hrpi_blacklistlog | 2 | hpfs_blacklistlog, hrpi_blacklistlog |
| t_hrpi_contractinfo | 3 | hspm_contractinfo_e, hspm_contractinfo_ly, hrpi_contractinfo |
| t_hrpi_debardinfo | 3 | hspm_debardinfo_e, hspm_debardinfo_ly, hrpi_debardinfo |
| t_hrpi_dispatchinfo | 3 | hspm_dispatchinfo_e, hspm_dispatchinfo_ly, hrpi_dispatchinfo |
| t_hrpi_empcadre | 3 | hspm_empcadre_e, hspm_empcadre_ly, hrpi_empcadre |
| t_hrpi_empentrel | 2 | hspm_empentrel_ly, hrpi_empentrel |
| t_hrpi_empjobrel | 3 | hspm_empjobrel_e, hspm_empjobrel_ly, hrpi_empjobrel |
| t_hrpi_employee | 4 | hspm_employee_e, hspm_employee_ly, hspm_employee_tl, hrpi_employee |
| t_hrpi_empposorgrel | 5 | hdm_empposorgrel, hspm_empposorgrel_e, hspm_empposorgrel_ly, hspm_empposorgrel_tl, hrpi_empposorgrel |
| t_hrpi_empproexp | 4 | hspm_empproexp_e, hspm_empproexp_ly, hspm_empproexp_tl, hrpi_empproexp |
| t_hrpi_empsuprel | 4 | hspm_empsuprel_e, hspm_empsuprel_ly, hspm_empsuprelhis_ly, hrpi_empsuprel |
| t_hrpi_emptrainfile | 3 | hspm_emptrainfile_e, hspm_emptrainfile_ly, hrpi_emptrainfile |
| t_hrpi_emptutor | 3 | hspm_emptutor_e, hspm_emptutor_ly, hrpi_emptutor |
| t_hrpi_emrgcontact | 3 | hspm_emrgcontact_e, hspm_emrgcontact_ly, hrpi_emrgcontact |
| t_hrpi_familymemb | 3 | hspm_familymemb_e, hspm_familymemb_ly, hrpi_familymemb |
| t_hrpi_fertilityinfo | 3 | hspm_fertilityinfo_e, hspm_fertilityinfo_ly, hrpi_fertilityinfo |
| t_hrpi_partymember | 3 | hspm_partymember_e, hspm_partymember_ly, hrpi_partymember |
| t_hrpi_peraddress | 3 | hspm_peraddress_e, hspm_peraddress_ly, hrpi_peraddress |
| t_hrpi_percontact | 4 | hspm_percontact_e, hspm_percontact_ly, hspm_percontact_tl, hrpi_percontact |
| t_hrpi_percre | 3 | hspm_percre_e, hspm_percre_ly, hrpi_percre |
| t_hrpi_pereduexp | 4 | hspm_pereduexp_e, hspm_pereduexp_ly, hspm_pereduexp_tl, hrpi_pereduexp |
| t_hrpi_perfresult | 3 | hspm_perfresult_e, hspm_perfresult_ly, hrpi_perfresult |
| t_hrpi_perhobby | 3 | hspm_perhobby_e, hspm_perhobby_ly, hrpi_perhobby |
| t_hrpi_perlgability | 3 | hspm_perlgability_e, hspm_perlgability_ly, hrpi_perlgability |
| t_hrpi_perocpqual | 3 | hspm_perocpqual_e, hspm_perocpqual_ly, hrpi_perocpqual |
| t_hrpi_perpractqual | 3 | hspm_perpractqual_e, hspm_perpractqual_ly, hrpi_perpractqual |
| t_hrpi_perprotitle | 3 | hspm_perprotitle_e, hspm_perprotitle_ly, hrpi_perprotitle |
| t_hrpi_perregion | 3 | hspm_perregion_e, hspm_perregion_ly, hrpi_perregion |
| t_hrpi_perrprecord | 3 | hspm_perrprecord_e, hspm_perrprecord_ly, hrpi_perrprecord |
| t_hrpi_perserlen | 3 | hspm_perserlen_e, hspm_perserlen_ly, hrpi_perserlen |
| t_hrpi_preworkexp | 4 | hspm_preworkexp_e, hspm_preworkexp_ly, hspm_preworkexp_tl, hrpi_preworkexp |
| t_hrpi_rotationinfo | 3 | hspm_rotationinfo_e, hspm_rotationinfo_ly, hrpi_rotationinfo |
| t_hrpi_rsmpatinv | 3 | hspm_rsmpatinv_e, hspm_rsmpatinv_ly, hrpi_rsmpatinv |
| t_hrpi_rsmproskl | 3 | hspm_rsmproskl_e, hspm_rsmproskl_ly, hrpi_rsmproskl |
| t_hrpi_terminationinfo | 3 | hspm_terminationinfo_e, hspm_terminationinfo_ly, hrpi_terminationinfo |
| t_hrpi_trialperiod | 4 | hdm_probation, hspm_trialperiod_e, hspm_trialperiod_ly, hrpi_trialperiod |
| t_hspm_attachmentmanage | 3 | hspm_attachmentman_ly, hspm_attachmentmanage, hspm_attachmanage |
| t_hspm_fileviewconfig | 3 | hspm_fileviewconfig, hspm_fileviewconfigmob, hspm_fileviewconfigtpl |
| t_hspm_infoapproval | 2 | hspm_infoapproval, hspm_infoapproval_hstc |
| t_htm_coophandle | 8 | htm_compensation, htm_coopbase, htm_coopcommon, htm_coophandle, htm_coopmanage, htm_noncompete, htm_settlement, htm_workhandover |
| t_htm_interviewhandle | 3 | htm_interviewbase, htm_interviewhandle, htm_interviewmanage |
| t_htm_quitapplybill | 14 | htm_applybaseinfo, htm_hraudite, htm_hraudite_hstc, htm_modifyquitinfo, htm_quitapply, htm_quitapply_hstc, htm_quitapplybasebill, htm_quitapplyemp, htm_quitapplyfast, htm_quithandle, htm_superaudite, htm_superaudite_hstc, htm_supervisoraudite, htm_supervisoraudite_hstc |
| t_htm_quitguide | 2 | htm_quitguide, htm_quitguide_context |

## 基础资料（123）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | hdm_empposorgrel | 5EM26RAJ0S29 | t_hrpi_empposorgrel | hdm_empposorgrel, hspm_empposorgrel_e, hspm_empposorgrel_ly, hspm_empposorgrel_tl, hrpi_empposorgrel | 兼职任职经历 | 基础资料 | hdm |
| 2 | hdm_probation | 52FUDF9RU/P1 | t_hrpi_trialperiod | hdm_probation, hspm_trialperiod_e, hspm_trialperiod_ly, hrpi_trialperiod | 试用期信息单 | 基础资料 | hdm |
| 3 | hdm_regaskreply | 2=N5DWYQTQB7 | t_hdm_regaskdetails | hdm_regaskdetails, hdm_regaskdetailscard, hdm_regaskreply | 转正问询评价 | 基础资料 | hdm |
| 4 | hdm_regcategory | 273+M592R1W5 | t_hdm_regcategory |  | 转正分类 | 基础资料 | hdm |
| 5 | hdm_regcomment | 272BCC/4F7GT | t_hdm_regcomment |  | 转正意见 | 基础资料 | hdm |
| 6 | hdm_regevent | 2=G1XJUD8FWN | t_hdm_regkeyevent |  | 试用期关键事件 | 基础资料 | hdm |
| 7 | hdm_regprocess | 294R5=6ZF493 | t_hdm_regprocess |  | 转正流程跟踪 | 基础资料 | hdm |
| 8 | hdm_regtrace | 298HQGWIH+UH | t_hdm_regtrace |  | 转正足迹 | 基础资料 | hdm |
| 9 | hdm_transferclassify | 2VD=HBNN9ZBK | t_hdm_transferclassify |  | 调动类别 | 基础资料 | hdm |
| 10 | hdm_transfertermreason | 2+IS1UN1D3MC | t_hdm_transfertermreason |  | 终止调动原因 | 基础资料 | hdm |
| 11 | hlcm_activity | 29=UXGKR=8PH | t_hlcm_activity | hlcm_activity, hlcm_electric_beginlist, hlcm_electric_checklist, hlcm_electric_confirmlist, hlcm_electric_csignlist, hlcm_electric_esignlist, hlcm_paper_beginlist, hlcm_paper_checklist, hlcm_paper_complist | 活动基础资料 | 基础资料 | hlcm |
| 12 | hlcm_contract | 29YXD+QU=64+ | t_hlcm_contract | hlcm_contract, hlcm_contract_allf7, hlcm_contractcr, hlcm_contractfileemp, hlcm_contractfileempcr, hlcm_contractfileother, hlcm_contractfileothercr, hlcm_contractsource | 劳动合同档案 | 基础资料 | hlcm |
| 13 | hlcm_contractfieldmap | 4KLVUIWL9DWN | t_hlcm_mapconfig |  | 合同协议申请单与合同档案字段映射配置 | 基础资料 | hlcm |
| 14 | hlcm_contractotherf7 | 2BEM=3D7BSH5 |  |  | F7布局_合同档案通用 | 基础资料 | hlcm |
| 15 | hlcm_contractreason | 34DYXVNQTXI8 | t_hlcm_contractreason |  | 解除/终止合同原因 | 基础资料 | hlcm |
| 16 | hlcm_contracttemplate | 25ROLMAW5FTR | t_hlcm_contracttemp | hlcm_contracttemplate, hlcm_contracttemplatech | 合同模板 | 基础资料 | hlcm |
| 17 | hlcm_contracttemplatehis | 28HM1B8XVIG+ | t_hlcm_contracttemphis |  | 合同模板历史 | 基础资料 | hlcm |
| 18 | hlcm_hiredperson | 2B1OXA2NY2ZJ | t_hlcm_hiredperson |  | 员工已入职未签 | 基础资料 | hlcm |
| 19 | hlcm_invalicontract | 4I03V4N1OJ39 | t_hlcm_invalicontract |  | 作废合同(协议) | 基础资料 | hlcm |
| 20 | hlcm_prewarn | 2A3UJJKJSLPK | t_hlcm_prewarn | hlcm_prewarn, hlcm_prewarn_type1list, hlcm_prewarn_type2list, hlcm_prewarn_type3list | 合同预警页 | 基础资料 | hlcm |
| 21 | hlcm_reasontypecat | 34DXNO+VAHXI | t_hlcm_reasontypecat |  | 合同原因类别 | 基础资料 | hlcm |
| 22 | hlcm_terminatercause | 2=G90WO6H+Z= | t_hlcm_terminatereason |  | 终止流程原因 | 基础资料 | hlcm |
| 23 | hom_acceptmanagedetail | 28PVF/0TL+1N | t_hom_acceptmanagedetail |  | 资料验收详情-卡片 | 基础资料 | hom |
| 24 | hom_acceptmanagehead | 28SYPEXJ7WYT |  |  | 资料验收-头部文字 | 基础资料 | hom |
| 25 | hom_active | 2AA3JLYSZ/JR | t_hom_activityinfodetail | hom_active, hom_activeall_layout, hom_activebase, hom_activecompleted_l, hom_activehandle, hom_activehandling_l, hom_activeterminated_l, hom_collaborationall_lay, hom_collaborationdetail, hom_collaborationdone_lay, hom_collaborationing_lay, hom_collaborationstop_lay, hom_onbrdcollaborate | 入职活动 | 基础资料 | hom |
| 26 | hom_activityoverview | 1ZYAILN9=KLZ | t_hom_activityinfo |  | 入职活动总览 | 基础资料 | hom |
| 27 | hom_bdfield | 1X8=N+AWL0T5 | t_hom_infogroupfield |  | 信息组字段 | 基础资料 | hom |
| 28 | hom_bdinfogroup | 1X96H/CCR+0A | t_hom_infogroup |  | 信息组基础资料 | 基础资料 | hom |
| 29 | hom_canaddress | 4YZAANLZ9XKI | t_hcf_canaddress | hom_canaddress, hom_canaddress_c, hom_canaddress_i, hcf_canaddress | 拟入职人员地址 | 基础资料 | hom |
| 30 | hom_canbankcard | 4YZAH=E8J5+T | t_hcf_canbankcard | hom_canbankcard, hom_canbankcard_c, hom_canbankcard_i, hcf_canbankcard | 拟入职人员银行卡 | 基础资料 | hom |
| 31 | hom_cancontact | 4YZAK657SL53 | t_hcf_cancontact | hom_cancontact, hom_cancontact_c, hom_cancontact_i, hcf_cancontact | 拟入职人员紧急联系人 | 基础资料 | hom |
| 32 | hom_cancontactinfo | 4YZB/USR3TIX | t_hcf_cancontactinfo | hom_cancontactinfo, hom_cancontactinfo_c, hom_cancontactinfo_i, hcf_cancontactinfo | 拟入职人员联系方式 | 基础资料 | hom |
| 33 | hom_cancre | 4YZB2TWFK/J8 | t_hcf_cancre | hom_cancre, hom_cancre_c, hom_cancre_i, hcf_cancre | 拟入职人员证件信息 | 基础资料 | hom |
| 34 | hom_candidate | 4Z/CV00N7EHZ | t_hcf_candidate | hom_candidate, hom_candidate_c, hom_candidate_i, hcf_candidate | 拟入职人员 | 基础资料 | hom |
| 35 | hom_caneduexp | 4YZB5HH6STET | t_hcf_caneduexp | hom_caneduexp, hom_caneduexp_c, hom_caneduexp_i, hcf_caneduexp | 拟入职人员教育经历 | 基础资料 | hom |
| 36 | hom_canfamily | 4YZBA/RD7XIY | t_hcf_canfamily | hom_canfamily, hom_canfamily_c, hom_canfamily_i, hcf_canfamily | 拟入职人员家庭成员 | 基础资料 | hom |
| 37 | hom_canlgability | 4YZBBYYN+BX9 | t_hcf_canlgability | hom_canlgability, hom_canlgability_c, hom_canlgability_i, hcf_canlgability | 拟入职人员语言技能 | 基础资料 | hom |
| 38 | hom_canocpqual | 4YZBDYQEC6ML | t_hcf_canocpqual | hom_canocpqual, hcf_canocpqual | 拟入职人员职业资格 | 基础资料 | hom |
| 39 | hom_canprework | 4YZBG9UY9+6L | t_hcf_canprework | hom_canprework, hom_canprework_c, hom_canprework_i, hcf_canprework | 拟入职人员工作经历 | 基础资料 | hom |
| 40 | hom_canprojectexp | 4YZBIFL6N6EC | t_hcf_canprojectexp | hom_canprojectexp, hcf_canprojectexp | 拟入职人员项目经历 | 基础资料 | hom |
| 41 | hom_cantraining | 4YZBLFF2NRXQ | t_hcf_cantraining | hom_cantraining, hcf_cantraining | 拟入职人员培训经历 | 基础资料 | hom |
| 42 | hom_checkinfo | 2/C74U34MZW1 | t_hom_onbrdbill | hom_allcheckin, hom_allreservation, hom_breakupcheckin, hom_breakupreservation, hom_checkinbilltpl, hom_checkinfo, hom_checkinfo_c, hom_checkinfo_i, hom_checkininfo, hom_exceptioncheckin, hom_hascheckin, hom_hasreservation, hom_onbrdbillbase, hom_onbrdbilltpl, hom_onbrdbook, hom_onbrdcheckin, hom_onbrdcheckinbody, hom_onbrdinfo, hom_onbrdpersonbilltpl, hom_onbrdpersoninfo, hom_personallonbrd, hom_personbreakuponbrd, hom_personhasonbrd, hom_personwaitonbrd, hom_personwaitstart, hom_reservationbody, hom_reservationhrconfirm, hom_reservationinfo, hom_reservationonbrd, hom_reservationtpl, hom_waitcheckin, hom_waitconfirm, hom_waitresercheckin, hom_waitreservation, hom_wbwaitonblist | 体检信息 | 基础资料 | hom |
| 43 | hom_collect_aicertlog | 5ICVBI=CFBKL | t_hom_collect_aicertlog |  | 采集Ai证件识别记录 | 基础资料 | hom |
| 44 | hom_collect_aifieldmap | 5IN5R59964GM | t_hom_collect_aifieldmap |  | 采集Ai证件字段关系映射 | 基础资料 | hom |
| 45 | hom_guidscheme | 26D1EUIVWA9A | t_hom_guidscheme |  | 指引方案配置 | 基础资料 | hom |
| 46 | hom_infocollectconfig | 2F5VWYYXL29Y | t_hom_infocollectconfig |  | 信息采集配置 | 基础资料 | hom |
| 47 | hom_infocollectconfighis | 2F668B7RCIID | t_hom_infocollectconfhis |  | 信息采集模板配置(历史) | 基础资料 | hom |
| 48 | hom_onbrdguideconfig | 26NDOUCHT2H9 | t_hom_onbrdguideconfig |  | 入职指引配置 | 基础资料 | hom |
| 49 | hom_personalarea | 4YZBO3I6XD0F | t_hcf_personalarea | hom_personalarea, hom_personalarea_c, hom_personalarea_i, hcf_personalarea | 拟入职人员区域信息 | 基础资料 | hom |
| 50 | hom_placeentry | 2/9OONH5/S/2 | t_hom_place |  | 入职地点 | 基础资料 | hom |
| 51 | hom_remindinfo | 25H+UOK0IXJO | t_hom_remindinfo |  | 提醒信息 | 基础资料 | hom |
| 52 | hom_rsmhobby | 4YZBUKCUT+OX | t_hcf_rsmhobby | hom_rsmhobby, hcf_rsmhobby | 拟入职人员特长及爱好 | 基础资料 | hom |
| 53 | hom_rsmpatinv | 4YZBY7FLG5NY | t_hcf_rsmpatinv | hom_rsmpatinv, hom_rsmpatinv_c, hom_rsmpatinv_i, hcf_rsmpatinv | 拟入职人员专利发明 | 基础资料 | hom |
| 54 | hom_rsmproskl | 4YZC+6H//TI7 | t_hcf_rsmproskl | hom_rsmproskl, hcf_rsmproskl | 拟入职人员专业技能 | 基础资料 | hom |
| 55 | hom_welcomeletter | 2601GQ86LJBO | t_hom_welcomeletter |  | 欢迎信配置 | 基础资料 | hom |
| 56 | hpfs_blacklist | 3IN6UE1+FBRS | t_hrpi_blacklist | hpfs_blacklist, hrpi_blacklist | 黑名单人员详情 | 基础资料 | hpfs |
| 57 | hpfs_chgaction | 20OTJB6+OOG6 | t_hpfs_chgaction |  | 变动操作 | 基础资料 | hpfs |
| 58 | hpfs_chgcategory | 20LJ=0GWAGXP | t_hpfs_chgcategory |  | 变动类型 | 基础资料 | hpfs |
| 59 | hpfs_chgcrossvalid | 2UK+JBXGYSST | t_hpfs_crossvalidation | hpfs_chgcrossvalid, hpfs_crossvalidation | 人事事务变动交叉校验 | 基础资料 | hpfs |
| 60 | hpfs_chgevent | 23QDL8K=7DBH | t_hpfs_chgevent |  | 变动大类 | 基础资料 | hpfs |
| 61 | hpfs_chgreason | 4SQ8T/PFE=FN | t_hpfs_chgreason |  | 变动原因 | 基础资料 | hpfs |
| 62 | hpfs_chgrecord | 22FIX26BFI8R | t_hpfs_chgrecord | hpfs_chgrecord, hspm_chgrecord, hspm_chgrecord_ly | 事务变动记录 | 基础资料 | hpfs |
| 63 | hpfs_entityinfo | 3VG9RNRTUFVZ | t_meta_entityinfo |  | 包含分录的实体信息 | 基础资料 | hpfs |
| 64 | hpfs_fieldrelation | 43MFBYBCIY66 | t_hpfs_mappedtable |  | 单据与人员字段关系 | 基础资料 | hpfs |
| 65 | hpfs_fieldrelationc | 4510FWBPQ7BW | t_hpfs_mappedtablec |  | 候选人与人员字段关系 | 基础资料 | hpfs |
| 66 | hpfs_filemapmanager | 0LDT4PXT7=/4 | t_hpfs_filemapmanager |  | 单据与人员信息映射配置 | 基础资料 | hpfs |
| 67 | hpfs_licensedetail | 2DR+O95RO5O8 | t_hpfs_licensedetail |  | 员工关系许可明细 | 基础资料 | hpfs |
| 68 | hpfs_personfield | 444UM+DKQWWP | t_hpfs_personfield |  | 人员字段 | 基础资料 | hpfs |
| 69 | hpfs_personflow | 2NFDPSFMLL6N | t_hpfs_personflow | hpfs_personflow, hpfs_personflow_file, hspm_personflow_ly | 人员流入流出表 | 基础资料 | hpfs |
| 70 | hspm_appointremove_ly | 51HDMGY5BJ0R | t_hrpi_appointremoverel | hspm_appointremove_ly, hspm_appointremoverel_e, hrpi_appointremoverel | 任免经历 | 基础资料 | hspm |
| 71 | hspm_assignment_ly | 4SZH8VD2Z=3H | t_hrpi_assignment | hspm_assignment_ly, htm_quitassignment, hrpi_assignment | 组织分配 | 基础资料 | hspm |
| 72 | hspm_assignmentmag_ly | 5HY83KXNK9PR | t_hrpi_assignmentmag | hspm_assignmentmag_ly, hrpi_assignmentmag | 组织分配管理主体 | 基础资料 | hspm |
| 73 | hspm_attachmentman_ly | 5=UQB/T7PYR6 | t_hspm_attachmentmanage | hspm_attachmentman_ly, hspm_attachmentmanage, hspm_attachmanage | 附件查看 | 基础资料 | hspm |
| 74 | hspm_attachmentrecord | 4NX2F6W0CRL+ | t_hspm_attachrecord |  | 导入导出记录 | 基础资料 | hspm |
| 75 | hspm_attachmenttreelist | 5E=61FJYOQV2 |  |  | 附件查看树形列表 | 基础资料 | hspm |
| 76 | hspm_attachmenttype | 4NTVQBS3C=G7 | t_hspm_attachmenttype |  | 附件分类 | 基础资料 | hspm |
| 77 | hspm_attachnamerule | 4NC314DGHG2I | t_hspm_attachnamerule |  | 附件命名规则 | 基础资料 | hspm |
| 78 | hspm_attachpersonfield | 5ABGSNZ611DD | t_hspm_attachpersonfield |  | 人员字段清单 | 基础资料 | hspm |
| 79 | hspm_contractinfo_e | 4V=8QPRG840+ | t_hrpi_contractinfo | hspm_contractinfo_e, hspm_contractinfo_ly, hrpi_contractinfo | 合同信息 | 基础资料 | hspm |
| 80 | hspm_debardinfo_e | 5399UQC+5L40 | t_hrpi_debardinfo | hspm_debardinfo_e, hspm_debardinfo_ly, hrpi_debardinfo | 回避信息 | 基础资料 | hspm |
| 81 | hspm_dispatchinfo_e | 51DKF9JZD07O | t_hrpi_dispatchinfo | hspm_dispatchinfo_e, hspm_dispatchinfo_ly, hrpi_dispatchinfo | 外派信息 | 基础资料 | hspm |
| 82 | hspm_empcadre_e | 51Y8G5YJWBVM | t_hrpi_empcadre | hspm_empcadre_e, hspm_empcadre_ly, hrpi_empcadre | 最高干部身份信息 | 基础资料 | hspm |
| 83 | hspm_empentrel_ly | 4SZ=WXODS0H2 | t_hrpi_empentrel | hspm_empentrel_ly, hrpi_empentrel | 雇佣信息 | 基础资料 | hspm |
| 84 | hspm_empjobrel_e | 4SVZA7WSAQ25 | t_hrpi_empjobrel | hspm_empjobrel_e, hspm_empjobrel_ly, hrpi_empjobrel | 职级职等 | 基础资料 | hspm |
| 85 | hspm_employee_e | 4SPU20TTRNRZ | t_hrpi_employee | hspm_employee_e, hspm_employee_ly, hspm_employee_tl, hrpi_employee | 基本信息 | 基础资料 | hspm |
| 86 | hspm_empproexp_e | 51DK9UTN7XB6 | t_hrpi_empproexp | hspm_empproexp_e, hspm_empproexp_ly, hspm_empproexp_tl, hrpi_empproexp | 项目经历 | 基础资料 | hspm |
| 87 | hspm_empsuprel_e | 52G24SYHIUDR | t_hrpi_empsuprel | hspm_empsuprel_e, hspm_empsuprel_ly, hspm_empsuprelhis_ly, hrpi_empsuprel | 汇报关系 | 基础资料 | hspm |
| 88 | hspm_emptrainfile_e | 4TYNX80ETM/X | t_hrpi_emptrainfile | hspm_emptrainfile_e, hspm_emptrainfile_ly, hrpi_emptrainfile | 培训经历 | 基础资料 | hspm |
| 89 | hspm_emptutor_e | 51DL4Q5EH/FD | t_hrpi_emptutor | hspm_emptutor_e, hspm_emptutor_ly, hrpi_emptutor | 导师 | 基础资料 | hspm |
| 90 | hspm_emrgcontact_e | 4STFPC4JUS9S | t_hrpi_emrgcontact | hspm_emrgcontact_e, hspm_emrgcontact_ly, hrpi_emrgcontact | 紧急联系人 | 基础资料 | hspm |
| 91 | hspm_familymemb_e | 4STFNFPSIZ+/ | t_hrpi_familymemb | hspm_familymemb_e, hspm_familymemb_ly, hrpi_familymemb | 家庭成员 | 基础资料 | hspm |
| 92 | hspm_fertilityinfo_e | 4STFK92SNUGV | t_hrpi_fertilityinfo | hspm_fertilityinfo_e, hspm_fertilityinfo_ly, hrpi_fertilityinfo | 生育信息 | 基础资料 | hspm |
| 93 | hspm_fileviewconfig | 4LDQN12C5R7S | t_hspm_fileviewconfig | hspm_fileviewconfig, hspm_fileviewconfigmob, hspm_fileviewconfigtpl | 多视图配置 | 基础资料 | hspm |
| 94 | hspm_infocollect | 57FAD/YT5IKD | t_hspm_infocollect |  | 信息采集任务 | 基础资料 | hspm |
| 95 | hspm_infocollectrecord | 57FAS9VNH8GH | t_hspm_infocollectrecord |  | 员工完善档案记录 | 基础资料 | hspm |
| 96 | hspm_partymember_e | 51DF3ZOMKL7G | t_hrpi_partymember | hspm_partymember_e, hspm_partymember_ly, hrpi_partymember | 党员信息 | 基础资料 | hspm |
| 97 | hspm_peraddress_e | 4ST1/JQ7P2NY | t_hrpi_peraddress | hspm_peraddress_e, hspm_peraddress_ly, hrpi_peraddress | 人员地址 | 基础资料 | hspm |
| 98 | hspm_percontact_e | 4SSI4RTCHAIZ | t_hrpi_percontact | hspm_percontact_e, hspm_percontact_ly, hspm_percontact_tl, hrpi_percontact | 联系方式 | 基础资料 | hspm |
| 99 | hspm_percre_e | 4TV1JI/72WQP | t_hrpi_percre | hspm_percre_e, hspm_percre_ly, hrpi_percre | 证件信息 | 基础资料 | hspm |
| 100 | hspm_pereduexp_e | 4SVZC=RVSU22 | t_hrpi_pereduexp | hspm_pereduexp_e, hspm_pereduexp_ly, hspm_pereduexp_tl, hrpi_pereduexp | 教育经历 | 基础资料 | hspm |
| 101 | hspm_perfresult_e | 5IMDIR=5R2F9 | t_hrpi_perfresult | hspm_perfresult_e, hspm_perfresult_ly, hrpi_perfresult | 绩效结果 | 基础资料 | hspm |
| 102 | hspm_perhobby_e | 4ST=H+HHUMSJ | t_hrpi_perhobby | hspm_perhobby_e, hspm_perhobby_ly, hrpi_perhobby | 特长及爱好 | 基础资料 | hspm |
| 103 | hspm_perlgability_e | 4STFHBPLS+53 | t_hrpi_perlgability | hspm_perlgability_e, hspm_perlgability_ly, hrpi_perlgability | 语言技能 | 基础资料 | hspm |
| 104 | hspm_perocpqual_e | 51DK27QXXYQ3 | t_hrpi_perocpqual | hspm_perocpqual_e, hspm_perocpqual_ly, hrpi_perocpqual | 职业资格 | 基础资料 | hspm |
| 105 | hspm_perpractqual_e | 51G4XF=1BEHA | t_hrpi_perpractqual | hspm_perpractqual_e, hspm_perpractqual_ly, hrpi_perpractqual | 执业资格 | 基础资料 | hspm |
| 106 | hspm_perprotitle_e | 4SWK+1WT48J3 | t_hrpi_perprotitle | hspm_perprotitle_e, hspm_perprotitle_ly, hrpi_perprotitle | 职称信息 | 基础资料 | hspm |
| 107 | hspm_perregion_e | 4ST13BADWDOZ | t_hrpi_perregion | hspm_perregion_e, hspm_perregion_ly, hrpi_perregion | 区域信息 | 基础资料 | hspm |
| 108 | hspm_perrprecord_e | 51DL03+U7K9Q | t_hrpi_perrprecord | hspm_perrprecord_e, hspm_perrprecord_ly, hrpi_perrprecord | 奖惩记录 | 基础资料 | hspm |
| 109 | hspm_perserlen_e | 4SVZ5H0SJH=C | t_hrpi_perserlen | hspm_perserlen_e, hspm_perserlen_ly, hrpi_perserlen | 服务年限 | 基础资料 | hspm |
| 110 | hspm_preworkexp_e | 4SWOF91ONVUC | t_hrpi_preworkexp | hspm_preworkexp_e, hspm_preworkexp_ly, hspm_preworkexp_tl, hrpi_preworkexp | 前工作经历 | 基础资料 | hspm |
| 111 | hspm_rotationinfo_e | 51DKWTK/LBP2 | t_hrpi_rotationinfo | hspm_rotationinfo_e, hspm_rotationinfo_ly, hrpi_rotationinfo | 轮岗情况 | 基础资料 | hspm |
| 112 | hspm_rsmpatinv_e | 51DJSCFFYE8H | t_hrpi_rsmpatinv | hspm_rsmpatinv_e, hspm_rsmpatinv_ly, hrpi_rsmpatinv | 专利发明 | 基础资料 | hspm |
| 113 | hspm_rsmproskl_e | 4SWOK4HQ8J/H | t_hrpi_rsmproskl | hspm_rsmproskl_e, hspm_rsmproskl_ly, hrpi_rsmproskl | 专业技能 | 基础资料 | hspm |
| 114 | hspm_terminationinfo_e | 4SVZJKVPVPJX | t_hrpi_terminationinfo | hspm_terminationinfo_e, hspm_terminationinfo_ly, hrpi_terminationinfo | 离职信息 | 基础资料 | hspm |
| 115 | htm_coopbase | 1ZA4PLRA22E0 | t_htm_coophandle | htm_compensation, htm_coopbase, htm_coopcommon, htm_coophandle, htm_coopmanage, htm_noncompete, htm_settlement, htm_workhandover | 离职协作基础页面 | 基础资料 | htm |
| 116 | htm_delayreason | 2+PTW10L+WC4 | t_htm_delayreason |  | 申请延迟原因 | 基础资料 | htm |
| 117 | htm_interviewanswer | 2/BO9JL9PRRP | t_htm_interviewanswer |  | 离职访谈答案 | 基础资料 | htm |
| 118 | htm_interviewbase | 2+/Y2C376XPE | t_htm_interviewhandle | htm_interviewbase, htm_interviewhandle, htm_interviewmanage | 离职访谈基础页面 | 基础资料 | htm |
| 119 | htm_quitguide | 2CSGKNE52SX7 | t_htm_quitguide | htm_quitguide, htm_quitguide_context | 离职指引 | 基础资料 | htm |
| 120 | htm_quitreason | 2+PUX7MTNHTE | t_htm_quitreason |  | 离职原因（废弃） | 基础资料 | htm |
| 121 | htm_quittypenew | 2=GZOJC6/T3S | t_htm_quittype |  | 离职类型（废弃） | 基础资料 | htm |
| 122 | htm_termreason | 2+PU402RP35C | t_htm_termreason |  | 终止离职原因 | 基础资料 | htm |
| 123 | htm_workcalendar | 2C1TJEKF+REA | t_htm_todotaskcalendar |  | 离职工作日历基础资料 | 基础资料 | htm |

## 基础资料-非主数据类（11）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | hdm_specialevent_tpl | 2=G151E=XSCY |  |  | 特殊事件模板 | 基础资料 | hdm |
| 2 | hlcm_contractapplyentry | 49S=JF/NK5F1 | t_hlcm_signtext |  | 签署申请单分录 | 基础资料 | hlcm |
| 3 | hlcm_invalidlog | 4HH+JSUU99B9 | t_hlcm_invalidlog |  | 合同失效日志 | 基础资料 | hlcm |
| 4 | hom_config | 1X4VUWHRGDPB | t_hom_config |  | 入职参数配置 | 基础资料 | hom |
| 5 | hom_employeeno | 2W5OI8A5O=2/ |  |  | 工号编码规则 | 基础资料 | hom |
| 6 | hom_onbrdmultirowtpl | 2/CK1JI=C/QX |  |  | 入职多行表 | 基础资料 | hom |
| 7 | hom_onbrdsinglerowtpl | 2/C7BQ=48R5W |  |  | 入职单行表 | 基础资料 | hom |
| 8 | hom_personinfotpl | 2/C/SC08YR08 |  |  | 入职个人信息模板 | 基础资料 | hom |
| 9 | hpfs_blacklistlog | 3IN6ZO8QDXR= | t_hrpi_blacklistlog | hpfs_blacklistlog, hrpi_blacklistlog | 日志详情 | 基础资料 | hpfs |
| 10 | hpfs_chgrecordentry | 22FJJN2O1IUD | t_hpfs_chgrecordentry |  | 变动记录结果明细分录 | 基础资料 | hpfs |
| 11 | hpfs_devconfig | 2VK+918DF9KU | t_hpfs_devconfig |  | 核心人事开发参数配置 | 基础资料 | hpfs |

## 单据（50）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | hdm_baseparttimebill | 2A/U=WH9E+3K | t_hdm_parttimeapplybill |  | 标准兼职申请单 | 单据 | hdm |
| 2 | hdm_batchparttime | 3==UPSOH94S= | t_hdm_parttimebatch | hdm_batchparttime, hdm_batchparttime_hstc | 批量兼职申请 | 单据 | hdm |
| 3 | hdm_batchregbill | 3ABVYLQT7WYE | t_hdm_regbatch | hdm_batchregbill, hdm_batchregbill_hstc | 批量转正申请 | 单据 | hdm |
| 4 | hdm_mytransfer_entry | 3K92DJ8GSTL3 |  |  | 员工自助我要调动入口 | 单据 | hdm |
| 5 | hdm_parttimeapplybill | 2ACYBORMGM+1 | t_hdm_parttimesuperior | hdm_parttimeapply_hstc, hdm_parttimeapplybill, hdm_parttimebillentry | 兼职申请 | 单据 | hdm |
| 6 | hdm_parttimeendbill | 2AM8NK20/XWW | t_hdm_parttimeendbill |  | 兼职终止单 | 单据 | hdm |
| 7 | hdm_regbasebill | 27/OYPCZ5K+1 | t_hdm_regbasebill | hdm_entryregbill, hdm_regapplysource, hdm_regbasebill, hdm_regbasebill_hstc, hdm_regselfhelp_supply, hdm_regselfhelpbill, hdm_regselfhelpbill_hstc | 转正代申请 | 单据 | hdm |
| 8 | hdm_ssctask | 47SQX2GOXXK4 | t_hdm_ssctask |  | 处理中共享任务 | 单据 | hdm |
| 9 | hdm_ssctaskhis | 47SQYP2L+LZV | t_hdm_ssctaskhis |  | 已完成共享任务 | 单据 | hdm |
| 10 | hdm_transferapply | 2VJCJB1E+3+J | t_hdm_transferbill | hdm_infochangebill, hdm_mytrans_hr_hstc, hdm_mytrans_hr_layout, hdm_mytrans_leader_hstc, hdm_mytrans_leader_layout, hdm_mytransferbasebill, hdm_mytransferbill, hdm_mytransferbill_hstc, hdm_transferapply, hdm_transferapply_hstc, hdm_transferbasebill, hdm_transferbatchentry, hdm_transferconfirmbill, hdm_transferinbill, hdm_transferinbill_hstc, hdm_transferinbill_layout, hdm_transferintoout_hstc, hdm_transferlayoutbill, hdm_transferoutbil_layout, hdm_transferoutbill, hdm_transferoutbill_hstc, hdm_transferouttoin_hstc | 调动代申请 | 单据 | hdm |
| 11 | hdm_transferbatch | 25HBJL5GM+O/ | t_hdm_transferbatch | hdm_batchtransferbasedata, hdm_transferbatch, hdm_transferbatch_hstc | 批量调动申请 | 单据 | hdm |
| 12 | hdm_transfermulconfirm | 5J6+F=0FMLU7 |  |  | 确认到岗 | 单据 | hdm |
| 13 | hlcm_empprotocol | 2=GSA+6Y8R9O | t_hlcm_signbill | hlcm_contractapply, hlcm_contractapplybase, hlcm_contractapplycancel, hlcm_contractapplychange, hlcm_contractapplyend, hlcm_contractapplyhandle, hlcm_contractapplynew, hlcm_contractapplyrenew, hlcm_contractcancel_hstc, hlcm_contractchange_hstc, hlcm_contractend_hstc, hlcm_contractnew_hstc, hlcm_contractrenew_hstc, hlcm_electricsign_all, hlcm_electricsign_archive, hlcm_electricsign_begin, hlcm_electricsign_check, hlcm_electricsign_confirm, hlcm_electricsign_csign, hlcm_electricsign_esign, hlcm_electricsign_stop, hlcm_empprotocol, hlcm_empprotocolnew, hlcm_empprotocolnew_hstc, hlcm_empprotocolrel_hstc, hlcm_empprotocolrelieve, hlcm_empprotocolterminate, hlcm_otheragreements, hlcm_otheragreements_hstc, hlcm_papersign_all, hlcm_papersign_archive, hlcm_papersign_begin, hlcm_papersign_check, hlcm_papersign_comp, hlcm_papersign_stop, hlcm_signbill_tobase, hlcm_signmanage_all, hlcm_signmanagebase | 用工协议 | 单据 | hlcm |
| 14 | hlcm_inquerytask | 4KVXY4B4PPN4 | t_hlcm_renewinquery | hlcm_inquerytask, hlcm_renewinquery, hlcm_renewinquerybase | 续签问询任务 | 单据 | hlcm |
| 15 | hom_collect | 1ZJICV1PWT5X | t_hom_collect | hom_acceptmanageall, hom_acceptmanagebreakup, hom_acceptmanageing, hom_acceptmanagepass, hom_acceptmanageunpass, hom_collect, hom_collectapproveall, hom_collectapprovefail, hom_collectapproveing, hom_collectapprovepass, hom_collectapprovereject, hom_collectapproveremit, hom_collectapproveterm, hom_collectmanageall, hom_collectmanagefin, hom_collectmanagesub, hom_collectmanageterm, hom_collectphone, hom_onbrdaccepte, hom_onbrdapprove, hom_onbrdcollect | 采集管理 | 单据 | hom |
| 16 | hom_collectgroupstatus | 26NW68+S5T1= | t_hom_collectgroup |  | 采集活动信息组状态 | 单据 | hom |
| 17 | hom_collectinfogroup | 272YK80VPY8T | t_hom_collectgroupentry |  | 采集活动单个信息组状态 | 单据 | hom |
| 18 | hom_collectlog | 21816ZVZCRVE | t_hom_collectlog |  | 采集审核日志 | 单据 | hom |
| 19 | hom_collectlogfieldview | 21AA/IK24SY= | t_hom_collectlogentity |  | 采集日志详细字段展示 | 单据 | hom |
| 20 | hom_onbrdinvite | 25HE9R+H51/Z | t_hom_onbrdinvite | hom_invitesendbill, hom_onbrdinvite, hom_onbrdinviteall, hom_onbrdinviteinfo | 入职邀约列表 | 单据 | hom |
| 21 | hom_preonbrdbasebill | 3MY7RQS36Q6R | t_hom_prebatchonbrdbill | hom_preonbrdbasebill, hom_preonbrdbasedata | 预入职申请 | 单据 | hom |
| 22 | hom_preonbrdentry | 3PKN+LD34QGA | t_hom_preonbrdbill |  | 预入职入职单分录 | 单据 | hom |
| 23 | hom_ssctask | 47TGNZXZNCSR | t_hom_ssctask |  | 玩美入职处理中共享任务 | 单据 | hom |
| 24 | hom_ssctaskhis | 47TGPG9TMJ4K | t_hom_ssctaskhis |  | 玩美入职已完成共享任务 | 单据 | hom |
| 25 | hom_workcardphoto_c | 5+XDEFM29D6V | t_hom_obdasuperior |  | 工卡照 | 单据 | hom |
| 26 | hpfs_base01 | 5IXT7RZV5/RU | t_hpfs_base01 |  | 人事事务基础单据01 | 单据 | hpfs |
| 27 | hpfs_base02 | 5IXTBOJT38+N | t_hpfs_base02 |  | 人事事务基础单据02 | 单据 | hpfs |
| 28 | hpfs_base03 | 5IXTFEYNN28V | t_hpfs_base03 |  | 人事事务基础单据03 | 单据 | hpfs |
| 29 | hpfs_base04 | 5IXTJDD4NZDF | t_hpfs_base04 |  | 人事事务基础单据04 | 单据 | hpfs |
| 30 | hpfs_base05 | 5IXTNJ62H110 | t_hpfs_base05 |  | 人事事务基础单据05 | 单据 | hpfs |
| 31 | hpfs_basepretpl | 5IXSLPH+0SYC |  |  | 人事事务基础预置单据模板 | 单据 | hpfs |
| 32 | hpfs_billtpl | 5IXS7/IQ333X |  |  | HPFS单据模板 | 单据 | hpfs |
| 33 | hpfs_hrbillorgtpl | 20S9N+XAVPH7 |  |  | 人事事务变动带主BU单据基模板 | 单据 | hpfs |
| 34 | hpfs_hrbilltpl | 210VL36=TNBX |  |  | 人事事务变动无BU单据基模板 | 单据 | hpfs |
| 35 | hpfs_hrcommbillorgtpl | 3W8YQ7WGJYWC |  |  | 人事事务变动通用带主BU单据基模板 | 单据 | hpfs |
| 36 | hpfs_hrcommonbilltplext | 4A/NO8J5X8TN |  |  | 通用事务单据模板 | 单据 | hpfs |
| 37 | hpfs_hrendbillorgtpl | 5BS314CBBU0D |  |  | 终止任职带主BU单据基模板 | 单据 | hpfs |
| 38 | hpfs_hrendbillorgtplext | 4A0/J/2X1WQP |  |  | 任职终止类事务单据模板（如：兼职终止、外派终止） | 单据 | hpfs |
| 39 | hpfs_hrhdmbillorgtpl | 3VJ+JRMQL+6A |  |  | 调动带主BU单据基模板 | 单据 | hpfs |
| 40 | hpfs_hrhdmbillorgtplext | 460P8LQ2C6=V |  |  | 调动、用工关系类型转换类业务单据模板 | 单据 | hpfs |
| 41 | hpfs_hrhombillorgtpl | 3VJ+MQIXAJL3 |  |  | 入职带主BU单据基模板 | 单据 | hpfs |
| 42 | hpfs_hrhombillorgtplext | 4A/N9AD7SU9R |  |  | 入职类事务单据模板（如：入职、外包入场等） | 单据 | hpfs |
| 43 | hpfs_hrhtmbillorgtpl | 3VJ+L2O6N/IZ |  |  | 离职带主BU单据基模板 | 单据 | hpfs |
| 44 | hpfs_hrhtmbillorgtplext | 4A/NH6L97DAK |  |  | 用工终止类事务单据模板（如离职、退休类事务） | 单据 | hpfs |
| 45 | hpfs_hrnewbillorgtpl | 3V6VHNRXTFNA |  |  | 人事事务变动新带主BU单据基模板 | 单据 | hpfs |
| 46 | hpfs_hrpartbillorgtpl | 3VN6IX4OHQO9 |  |  | 兼职带主BU单据基模板 | 单据 | hpfs |
| 47 | hpfs_hrpartbillorgtplext | 45K/IFD1VL+C |  |  | 新增任职经历类（如兼职、轮岗、借调）业务单据模板 | 单据 | hpfs |
| 48 | hspm_infoapproval | 2BWX=EPWXPRK | t_hspm_infoapproval | hspm_infoapproval, hspm_infoapproval_hstc | 人员档案信息变更 | 单据 | hspm |
| 49 | hspm_infocollecttskbill | 57FBFDYY1TGN | t_hspm_infocollecttskbill |  | 信息采集任务 | 单据 | hspm |
| 50 | htm_hraudite | 28T4XZEUE7BL | t_htm_quitapplybill | htm_applybaseinfo, htm_hraudite, htm_hraudite_hstc, htm_modifyquitinfo, htm_quitapply, htm_quitapply_hstc, htm_quitapplybasebill, htm_quitapplyemp, htm_quitapplyfast, htm_quithandle, htm_superaudite, htm_superaudite_hstc, htm_supervisoraudite, htm_supervisoraudite_hstc | HR审批 | 单据 | htm |

## 动态表单（212）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | hdm_apphome | 1WX49ICNN+4/ |  |  | 调配管理_应用首页 | 动态表单 | hdm |
| 2 | hdm_apphome_reg | 35YT2PP4KVR= |  |  | 转正及试用_应用首页 | 动态表单 | hdm |
| 3 | hdm_askperson | 294SR7J2VN9V |  |  | 转正问询 | 动态表单 | hdm |
| 4 | hdm_batchendparttime | 5N9IVAQMKJY8 |  |  | 批量终止兼职 | 动态表单 | hdm |
| 5 | hdm_batchparttermconfirm | 3ENXAV5KEHN= |  |  | 批量兼职终止流程确认 | 动态表单 | hdm |
| 6 | hdm_batchregmulconfirm | 3AF5MYF+6/9W |  |  | 操作确认 | 动态表单 | hdm |
| 7 | hdm_batchregoperateresult | 3AMOF7YR4KS2 |  |  | 操作提示 | 动态表单 | hdm |
| 8 | hdm_businesscard | 350BHJEMGF+/ |  |  | 人员名片 | 动态表单 | hdm |
| 9 | hdm_cardviewlist | 52QCOAOGCH13 |  |  | 卡片试图模板 | 动态表单 | hdm |
| 10 | hdm_cardviewtpl | 2D9+WXS5X8GG |  |  | 卡片视图模板 | 动态表单 | hdm |
| 11 | hdm_deletecomfirm | 2B9EJE92OFNK |  |  | 删除确认 | 动态表单 | hdm |
| 12 | hdm_endparttime | 2D6URPPPISJ/ |  |  | 终止兼职 | 动态表单 | hdm |
| 13 | hdm_mulcommitconfirm | 245F/3OQKV68 |  |  | 提交确认 | 动态表单 | hdm |
| 14 | hdm_mulconfirm | 2/5=7ZOE2Z=Q |  |  | 调动多选提示弹窗 | 动态表单 | hdm |
| 15 | hdm_muluncommitconfirm | 28PJU8J274LX |  |  | 撤销确认 | 动态表单 | hdm |
| 16 | hdm_operateresult | 2Y=V740TC1WI |  |  | 操作结果 | 动态表单 | hdm |
| 17 | hdm_partpersoncard | 2ATVMPRTIRNU |  |  | 兼职人员卡片 | 动态表单 | hdm |
| 18 | hdm_parttimeconfirm | 5=UUO+FJ18BM |  |  | 兼职操作确认弹窗 | 动态表单 | hdm |
| 19 | hdm_parttimeoperateresult | 3BHTH240J78R |  |  | 操作提示 | 动态表单 | hdm |
| 20 | hdm_personalcard | 2=G4G+TGNP/= |  |  | 调动人员卡片 | 动态表单 | hdm |
| 21 | hdm_reg_self_submited_pc | 3201TFKKH8JR |  |  | pc端员工已提交-提示 | 动态表单 | hdm |
| 22 | hdm_regaskitems | 3985EPS=B1// |  |  | 转正问询 | 动态表单 | hdm |
| 23 | hdm_regaskview | 39E6QS2C44Z2 |  |  | 转正问询 | 动态表单 | hdm |
| 24 | hdm_regbatchtermconfirm | 3E/ZD7PCEWXA |  |  | 终止流程 | 动态表单 | hdm |
| 25 | hdm_regbillcancel | 2=QQ43B18GY+ |  |  | 转正批量撤销弹窗 | 动态表单 | hdm |
| 26 | hdm_regbillsubmit | 2=QQ2CSSXQZ8 |  |  | 转正批量提交弹窗 | 动态表单 | hdm |
| 27 | hdm_regbusinesscard | 39DIEO3OFX58 |  |  | 人员卡片 | 动态表单 | hdm |
| 28 | hdm_regrollbackconfirm | 5=NH1RD5=FP3 |  |  | 转正撤回 | 动态表单 | hdm |
| 29 | hdm_regtermconfirm | 2IQYXLVXXGQS |  |  | 终止转正流程 | 动态表单 | hdm |
| 30 | hdm_regupgrade_tips | 35OD0F/G217/ |  |  | 试用及转正升级提示 | 动态表单 | hdm |
| 31 | hdm_remindconfirmdelay | 385NY=VJFIDV |  |  | 催办 | 动态表单 | hdm |
| 32 | hdm_result | 22X228K840TW |  |  | 提交结果页面 | 动态表单 | hdm |
| 33 | hdm_tipsconfirm | 2XPC7K4X505I |  |  | 操作确认 | 动态表单 | hdm |
| 34 | hdm_transferconfirm | 2+XH4EM/651I |  |  | 确认到岗 | 动态表单 | hdm |
| 35 | hdm_transferevoke | 2+WJXBW5G28H |  |  | 撤回调动（已废弃） | 动态表单 | hdm |
| 36 | hdm_transferrollbackbox | 5ABNSX02FJGB |  |  | 调动撤回 | 动态表单 | hdm |
| 37 | hdm_transferterminate | 2+WJA6IG5L=G |  |  | 终止调动 | 动态表单 | hdm |
| 38 | hdm_wfremind | 59ACQFR+B3NB |  |  | 催办 | 动态表单 | hdm |
| 39 | hlcm_apphome | 1WXBXYXIM2CD |  |  | 劳动合同_应用首页 | 动态表单 | hlcm |
| 40 | hlcm_billheadadd | 25E1ZCL4EHAZ |  |  | 单据头新增态 | 动态表单 | hlcm |
| 41 | hlcm_billheadview | 25E6HWKEOF7S |  |  | 单据头查看态 | 动态表单 | hlcm |
| 42 | hlcm_contractheadinfo | 2=L+LKG=ZCHO |  |  | 查看态单据头部信息 | 动态表单 | hlcm |
| 43 | hlcm_contracthiscard | 2=CM8WR13S6B |  |  | 档案历史追溯 | 动态表单 | hlcm |
| 44 | hlcm_contractotherhis | 2=DEVO/H5CNA |  |  | 其他关联附属协议记录 | 动态表单 | hlcm |
| 45 | hlcm_contractperinfo | 28M+M5GUVAKW |  |  | 合同协议人员头信息 | 动态表单 | hlcm |
| 46 | hlcm_electricsign | 29A2QEMQ48=N |  |  | 电子签署 | 动态表单 | hlcm |
| 47 | hlcm_handlecheck | 2=GSJ197U=NL |  |  | 办理单洞察 | 动态表单 | hlcm |
| 48 | hlcm_hiredperson_tab | 2H4GISGY6OEQ |  |  | 已入职未签员工列表 | 动态表单 | hlcm |
| 49 | hlcm_inqueryheadinfo | 4LH5Z+WEW+RX |  |  | 头部信息 | 动态表单 | hlcm |
| 50 | hlcm_inquerytaskheadinfo | 4LR21=TC8XAW |  |  | 问询任务头部信息 | 动态表单 | hlcm |
| 51 | hlcm_mapjump | 4KRVGPWZ74XR |  |  | 跳转至配置页面 | 动态表单 | hlcm |
| 52 | hlcm_mulselectconfirm | 2A6Y5=RQ/VM4 |  |  | 列表多选提示弹窗 | 动态表单 | hlcm |
| 53 | hlcm_papersign | 29A5AR/85UD9 |  |  | 纸质签署 | 动态表单 | hlcm |
| 54 | hlcm_papersign_attach | 2C7Y5J22AN+3 |  |  | 确认完成签署 | 动态表单 | hlcm |
| 55 | hlcm_result | 2AXM403YSXOH |  |  | 结果页面 | 动态表单 | hlcm |
| 56 | hlcm_stopsign | 29SL4RNXGGHF |  |  | 终止签署 | 动态表单 | hlcm |
| 57 | hlcm_templatetreelist | 34DWPGYIZTUX |  |  | 合同标准树列表 | 动态表单 | hlcm |
| 58 | hlcm_terminateconfirm | 29B1P/=J85G1 |  |  | 终止流程 | 动态表单 | hlcm |
| 59 | hlcm_warning_tab | 2A6+J41UGK16 |  |  | 工作台预警页签布局 | 动态表单 | hlcm |
| 60 | hom_acceptmanagelist | 25+22MI+VS5W |  |  | 资料验收菜单查询 | 动态表单 | hom |
| 61 | hom_acceptnullpage | 29UV6VODBAJ+ |  |  | 空白界面 | 动态表单 | hom |
| 62 | hom_activehandlelist | 2AQGXGBFTRR4 |  |  | 通用协作菜单查询 | 动态表单 | hom |
| 63 | hom_activitytransfer | 2ARNG34U6YV9 |  |  | 转交 | 动态表单 | hom |
| 64 | hom_againforbidtip | 5C4J8NG/=AC+ |  |  | 再入职禁止提示 | 动态表单 | hom |
| 65 | hom_againrisktip | 5C4KZCGCUOQI |  |  | 再入职风险确认 | 动态表单 | hom |
| 66 | hom_apphome | 1WXB5HXMESW7 |  |  | 入职工作台 | 动态表单 | hom |
| 67 | hom_approveemptypage | 297MN72QNWYQ |  |  | 审核空页面 | 动态表单 | hom |
| 68 | hom_approvefailpop | 253VSAQ+UZR6 |  |  | 请填写审核不通过原因 | 动态表单 | hom |
| 69 | hom_approvemutilview | 25DYYXBI4CYR |  |  | 多行信息组驳回操作 | 动态表单 | hom |
| 70 | hom_approverejectview | 258EP0L2VV3E |  |  | 驳回修改 | 动态表单 | hom |
| 71 | hom_attachmentpanelap | 266JB3ICGH1K |  |  | 附件明细 | 动态表单 | hom |
| 72 | hom_blacklistvalidconfirm | 3KERKF71LYRQ |  |  | 黑名单校验提示 | 动态表单 | hom |
| 73 | hom_collaborationmanage | 2ACQRKMBQQNA |  |  | 入职协作管理菜单查询 | 动态表单 | hom |
| 74 | hom_collectapprovelist | 2/BOYEHTZUR7 |  |  | 信息审核菜单查询 | 动态表单 | hom |
| 75 | hom_collectlogbeanview | 21887I/W4UPX |  |  | 采集审核日志基本元素 | 动态表单 | hom |
| 76 | hom_collectlogfield | 21A8Y3O441VJ |  |  | 信息采集日志字段展示模块 | 动态表单 | hom |
| 77 | hom_collectlogview | 2185HYYCUQWL |  |  | 审核日志展示页面 | 动态表单 | hom |
| 78 | hom_collectmanagelist | 2/B7NKRXFGRV |  |  | 信息采集菜单查询 | 动态表单 | hom |
| 79 | hom_collectmatchtemplate | 2/ZCFSLAP2DT |  |  | 采集匹配弹窗 | 动态表单 | hom |
| 80 | hom_commonlogshow | 2AR2/CA8F2MY |  |  | 日志展示 | 动态表单 | hom |
| 81 | hom_configviewqrcode | 28S+ND=GEZT5 |  |  | 预览 | 动态表单 | hom |
| 82 | hom_confirm | /ML84MXHNDZO |  |  | 确认页面 | 动态表单 | hom |
| 83 | hom_defaultemptypage | 57IT4TZOJ6YS |  |  | 入职公用空白表单 | 动态表单 | hom |
| 84 | hom_embedinfo_list | 4Z7//9CMVVVR |  |  | 内嵌页面列表 | 动态表单 | hom |
| 85 | hom_embedinfoext_list | 5+96W=TZXQ6F |  |  | 内嵌页面扩展工具列表 | 动态表单 | hom |
| 86 | hom_empnumberrule | 2WF3EUHLEG+6 |  |  | 生成工号 | 动态表单 | hom |
| 87 | hom_emptypage | 2+T8B/OCZB+W |  |  | 空页面 | 动态表单 | hom |
| 88 | hom_guidpreview | 27+XN84OXQ09 |  |  | 指引方案预览 | 动态表单 | hom |
| 89 | hom_handlecheck | 2A5YRNSVF/EX |  |  | 入职办理检测项 | 动态表单 | hom |
| 90 | hom_infodetaidynpreviewh5 | 25OKFB1/NZQD |  |  | 配置页预览详情H5 | 动态表单 | hom |
| 91 | hom_infofieldadd | 1XLT3/=+C/Q0 |  |  | 添加信息组字段 | 动态表单 | hom |
| 92 | hom_infogroupdynview | 1YI/AQ1I93MC |  |  | 信息组页面 | 动态表单 | hom |
| 93 | hom_infogroupdynviewapp | 2FLV5GJEE4B8 |  |  | 信息组页面审核 | 动态表单 | hom |
| 94 | hom_infogroupedit | 1XEDEXX5S94H |  |  | 信息组名称修改 | 动态表单 | hom |
| 95 | hom_infogrouppop | 1X5W16=H/+07 |  |  | 信息组弹窗 | 动态表单 | hom |
| 96 | hom_multiembedframe | 51TYTI=9EAA2 |  |  | 多行表详情页平铺框架 | 动态表单 | hom |
| 97 | hom_onbrdactivityhead | 1Y47=N9CMZUQ |  |  | 入职活动头信息 | 动态表单 | hom |
| 98 | hom_onbrdbreakup | /I7ER5XK=MSV |  |  | 终止入职 | 动态表单 | hom |
| 99 | hom_onbrdcheckinlist | 1Y4QUG2F5Q34 |  |  | 入职报到菜单查询 | 动态表单 | hom |
| 100 | hom_onbrdconfirmcount | 17PH74=WXY7B |  |  | 确认入职 | 动态表单 | hom |
| 101 | hom_onbrdconfirmmulti | 17PNXWS1L/FV |  |  | 批量确认入职 | 动态表单 | hom |
| 102 | hom_onbrdconfirmsingle | 17N23ORC7LE9 |  |  | 确认入职（单条数据） | 动态表单 | hom |
| 103 | hom_onbrddetailhead | 1Y89YE2/UFS0 |  |  | 入职单详情页头部 | 动态表单 | hom |
| 104 | hom_onbrdinfoquerylistf7 | 3H4J41DGCFNL |  |  | 入职人员列表F7 | 动态表单 | hom |
| 105 | hom_onbrdinviteconfirm | 25KU6A1K4BVX |  |  | 入职邀约确认 | 动态表单 | hom |
| 106 | hom_onbrdnullpage | 1XS3T5P58=GT |  |  | 人员入职管理占位页面 | 动态表单 | hom |
| 107 | hom_onbrdpersoninfohead | 2=GYB4/JJIQS |  |  | 入职人员信息详情页头部 | 动态表单 | hom |
| 108 | hom_onbrdpersonlist | 17CT5OK8MO6T |  |  | 入职办理菜单查询 | 动态表单 | hom |
| 109 | hom_onbrdreservationlist | 2I=PASMRT+II |  |  | 入职预约菜单查询 | 动态表单 | hom |
| 110 | hom_orderonbrdmodal | 2W5J+RM6659M |  |  | 批量预约入职 | 动态表单 | hom |
| 111 | hom_persononbrdbody | 1XL72TQ97ICK |  |  | 入职人员管理主体 | 动态表单 | hom |
| 112 | hom_persononbrdhandlebody | 2=HBVMEH+L73 |  |  | 入职办理单据 | 动态表单 | hom |
| 113 | hom_phone | 25OJUZ05HE58 |  |  | 手机模型 | 动态表单 | hom |
| 114 | hom_rechecktips | 2ZQKKBG116K5 |  |  | 再入职试用期及用工关系状态提示 | 动态表单 | hom |
| 115 | hom_reentrytips | 3+R0DIKEZAP5 |  |  | 再入职提示语弹窗 | 动态表单 | hom |
| 116 | hom_remindloginconfirm | 25KU9=H/BF9H |  |  | 提醒登录确认 | 动态表单 | hom |
| 117 | hom_repealonbrdconfirm | 3CSIM4GN/GMG |  |  | 入职撤回 | 动态表单 | hom |
| 118 | hom_result | /ML8IQUTD4=B |  |  | 结果页面 | 动态表单 | hom |
| 119 | hom_result_all | 1=R9WAVTUPTC |  |  | 所有结果页面（包含成功与失败） | 动态表单 | hom |
| 120 | hom_result_fail | 1=R9M+OKM+== |  |  | 失败结果页面 | 动态表单 | hom |
| 121 | hom_result_success | 1=R8RZT5VTQO |  |  | 成功结果页面 | 动态表单 | hom |
| 122 | hom_routineskiptip | 31G9/6G1YYR7 |  |  | 跳转提示 | 动态表单 | hom |
| 123 | hom_wbdateevent | 2C7T0CH=TNKZ |  |  | 日历事件记录 | 动态表单 | hom |
| 124 | hom_wbquicksearch | 2=KG=T+SG38/ |  |  | 快速搜索弹窗 | 动态表单 | hom |
| 125 | hom_wbtips | 2=GXK6IVFERD |  |  | 提醒 | 动态表单 | hom |
| 126 | hom_workbench | 2=37OABH9TIP |  |  | 入职工作台 | 动态表单 | hom |
| 127 | hpfs_addsysperson | 3INBSCGXGLP0 |  |  | 添加离职人员 | 动态表单 | hpfs |
| 128 | hpfs_apphome | 1XL6VJI7TRB5 |  |  | 核心人事基础服务_应用首页 | 动态表单 | hpfs |
| 129 | hpfs_blackaddfail | 4M3Q7P8H4IBK |  |  | 添加失败 | 动态表单 | hpfs |
| 130 | hpfs_blackinfotip | 4LVB9KCKCYD+ |  |  | 温馨提示 | 动态表单 | hpfs |
| 131 | hpfs_blackopconfirm | 4MFWOK9DWOIL |  |  | 操作确认 | 动态表单 | hpfs |
| 132 | hpfs_chgactionlist | 40ECMYB1HNBA |  |  | 变动操作列表 | 动态表单 | hpfs |
| 133 | hpfs_dynfilepage | 1Y7M/K13RNP4 |  |  | 人员档案侧边栏 | 动态表单 | hpfs |
| 134 | hpfs_fixperchgmeta | 444EUR2YYJ3G |  |  | 修复事务变动单据元数据 | 动态表单 | hpfs |
| 135 | hpfs_mulselectconfirm | 1XLU9V1X=VRB |  |  | 列表多选提示弹窗模板 | 动态表单 | hpfs |
| 136 | hpfs_multiviewtemplate | 1XPR7S9ENT3+ |  |  | 多视图框架模板 | 动态表单 | hpfs |
| 137 | hpfs_paramconfig | 2XPJR8QSEZM2 |  |  | 参数设置 | 动态表单 | hpfs |
| 138 | hpfs_rmblacklist | 3INCCDGE6X77 |  |  | 移除黑名单 | 动态表单 | hpfs |
| 139 | hpfs_rpctest | 2OHWC=GR9UUA |  |  | rpc接口调用测试 | 动态表单 | hpfs |
| 140 | hpfs_simplecard | 3INCV9709ZWU |  |  | 黑名单人员卡片 | 动态表单 | hpfs |
| 141 | hpfs_supanddepimgtips | 5C127KINP3G4 |  |  | 上级和部门负责人弹窗 | 动态表单 | hpfs |
| 142 | hspm_apphome | 1WXBPNUTU+VH |  |  | 人员信息_应用首页 | 动态表单 | hspm |
| 143 | hspm_approval_attachment | 2L+6G6+SVCJ/ |  |  | 附件 | 动态表单 | hspm |
| 144 | hspm_approval_fileheader | 5DNPPDHN6AY9 |  |  | 人员档案审批单表头 | 动态表单 | hspm |
| 145 | hspm_approvalhead | 2CLUHC1E1UI0 |  |  | 信息组字段审批头部 | 动态表单 | hspm |
| 146 | hspm_assignmenttab | 4UY8JQD19U9W |  |  | 组织分配页签 | 动态表单 | hspm |
| 147 | hspm_attachment_view | 5=YK56X05SWA |  |  | 附件查看 | 动态表单 | hspm |
| 148 | hspm_attachmentapproval | 2IQYLNKJ1ZNI |  |  | 附件字段审批 | 动态表单 | hspm |
| 149 | hspm_attachmentdownload | 4QDEH2L2=M87 |  |  | 导出附件配置 | 动态表单 | hspm |
| 150 | hspm_attachmentexport | 5AWZBLQPGW4H |  |  | 附件批量导出 | 动态表单 | hspm |
| 151 | hspm_attachmentorglist | 5A5MYF3AVH3R |  |  | 附件查看列表模板 | 动态表单 | hspm |
| 152 | hspm_attachmenttypefield | 5=UMZBG9QBXZ |  |  | 附件类型字段弹窗 | 动态表单 | hspm |
| 153 | hspm_attachmentupload | 5=XW5A+RQ01Z |  |  | 附件批量导入 | 动态表单 | hspm |
| 154 | hspm_attachmnetuploadtask | 4S1P82CEMHTS |  |  | 上传任务执行进度 | 动态表单 | hspm |
| 155 | hspm_changerecord | 2ZK6T++BI=NV |  |  | 档案修改记录 | 动态表单 | hspm |
| 156 | hspm_changerecord_attach | 372YJHH0QAJ3 |  |  | 附件 | 动态表单 | hspm |
| 157 | hspm_changerecorddetail | 37/=XW37G24W |  |  | 查看详情 | 动态表单 | hspm |
| 158 | hspm_changerecordlist | 35DX7+BX=K7G |  |  | 变更记录列表 | 动态表单 | hspm |
| 159 | hspm_chgrecord_detail | 54KJ43+92I=1 |  |  | 变动字段信息明细 | 动态表单 | hspm |
| 160 | hspm_configsummarytips | 5678B6/4AUW4 |  |  | 员工端概要提示语 | 动态表单 | hspm |
| 161 | hspm_configtitletips | 567+GUAXT0HY |  |  | 员工端标题提示语 | 动态表单 | hspm |
| 162 | hspm_defaultemptypage | 5AXH030TKAKF |  |  | 人员信息公用空白表单 | 动态表单 | hspm |
| 163 | hspm_dynfileheadreform | 4KTKQTQJL+AM |  |  | 人员档案头部改造 | 动态表单 | hspm |
| 164 | hspm_empposorgrelchglist | 5CY=0RZXP=IH |  |  | 任职经历变化列表 | 动态表单 | hspm |
| 165 | hspm_empposorgreldialog | 5CVABNPZT1K9 |  |  | 任职变动记录确认 | 动态表单 | hspm |
| 166 | hspm_empposorgrelresult | 5CW+YF7Y+SSN |  |  | 结果示例 | 动态表单 | hspm |
| 167 | hspm_entityexportconform | 4YAK6VDE=L4R |  |  | 请选择要导出的人员信息 | 动态表单 | hspm |
| 168 | hspm_ermanfilereform | 4KT6EW7AS18S |  |  | 人员信息详情 | 动态表单 | hspm |
| 169 | hspm_ermanfiletreelisrefo | 4KTHI3V6QXQW |  |  | 人员档案左树改造 | 动态表单 | hspm |
| 170 | hspm_ermanfiletreelist | 2FC2H7/F=AR2 |  |  | 人员档案左树 | 动态表单 | hspm |
| 171 | hspm_fileviewfield | 4KT4T4UGZ3WI |  |  | 信息组字段 | 动态表单 | hspm |
| 172 | hspm_fileviewgroupedit | 4L8CQGUMX3=4 |  |  | 编辑信息组 | 动态表单 | hspm |
| 173 | hspm_fileviewinfogroup | 4L7X+3MEEQWQ |  |  | 信息组页面 | 动态表单 | hspm |
| 174 | hspm_groupfieldapproval | 2CC0DJ8HGI+T |  |  | 信息组字段审批 | 动态表单 | hspm |
| 175 | hspm_infocollectrectree | 57J1RUSC/H0Y |  |  | 员工档案完善记录左树 | 动态表单 | hspm |
| 176 | hspm_listtpl_filter | 53VRERO19DY7 |  |  | 标准列表（人员档案）带过滤面板 | 动态表单 | hspm |
| 177 | hspm_listtpl_hrmanfile | 4KWE62PIEB3P |  |  | 标准列表（人员档案） | 动态表单 | hspm |
| 178 | hspm_listtpl_hrmanfiletl | 4Q=AA4HJG/6K |  |  | 标准列表（管理端_人员档案） | 动态表单 | hspm |
| 179 | hspm_moresuper | 2O1PHPZ1FK9U |  |  | 更多信息 | 动态表单 | hspm |
| 180 | hspm_myermanfileheadref | 4L7VJHLCWTKF |  |  | 员工档案头部信息 | 动态表单 | hspm |
| 181 | hspm_myermanfilereform | 4L7V3L9EUYHP |  |  | 我的档案 | 动态表单 | hspm |
| 182 | hspm_neweduattach | 4TE=AO=G+9SV |  |  | 教育经历 | 动态表单 | hspm |
| 183 | hspm_orgtreefiter | 3DNCI29VB8AO |  |  | 人员档案HR组织过滤 | 动态表单 | hspm |
| 184 | hspm_revisedepartdialog | 5N9SN/VUY88+ |  |  | 修订离职日期 | 动态表单 | hspm |
| 185 | hspm_reviseentrydialog | 5N/UGZX1NUS1 |  |  | 修订入职日期 | 动态表单 | hspm |
| 186 | hspm_tasknotice | 57P2QEU662M6 |  |  | 发送通知 | 动态表单 | hspm |
| 187 | hspm_tasknoticeurge | 590QTAZE6DOP |  |  | 催办 | 动态表单 | hspm |
| 188 | htm_activitymultreject | 2+3=S+A0SR6D |  |  | 离职活动多选驳回确认 | 动态表单 | htm |
| 189 | htm_activitypanel | 254C/7PHW57E |  |  | 离职活动面板 | 动态表单 | htm |
| 190 | htm_activityreject | 1ZYQ7QU3BLYD |  |  | 驳回 | 动态表单 | htm |
| 191 | htm_addblacklistreason | 3DNPE5CQ3NZD |  |  | 加入黑名单 | 动态表单 | htm |
| 192 | htm_addblacklistreexcel | 3E/37C26KKI1 |  |  | 加入黑名单原因 | 动态表单 | htm |
| 193 | htm_apphome | 1WTT6M2+UNR6 |  |  | 离职管理工作台 | 动态表单 | htm |
| 194 | htm_applycreate | 266E+DIJF=HX |  |  | 申请单性能造数 | 动态表单 | htm |
| 195 | htm_applytotalview | 2=DWKFFG0HU6 |  |  | 离职总览表单 | 动态表单 | htm |
| 196 | htm_batchconfirm | 3HV/DNA/JH5Y |  |  | 批量操作确认 | 动态表单 | htm |
| 197 | htm_commonlogshow | 2+/NCBHLLDBR |  |  | 日志展示 | 动态表单 | htm |
| 198 | htm_handlecheck | 2+JUIAAQSFTJ |  |  | 离职办理检查项 | 动态表单 | htm |
| 199 | htm_handtaskanalyse | 2BBRE6WZDC2U |  |  | 办理台分析 | 动态表单 | htm |
| 200 | htm_handtaskanalyseview | 2A7AIG9ASWA2 |  |  | 办理任务分析页面 | 动态表单 | htm |
| 201 | htm_interview_pane | 2+9HJ7HVKCFH |  |  | 离职访谈面板 | 动态表单 | htm |
| 202 | htm_mulcommitconfirm | 1XM20=ZRUUUG |  |  | 提交离职申请确认 | 动态表单 | htm |
| 203 | htm_mulremind | 2A/XOMOAE=UV |  |  | 处理提醒 | 动态表单 | htm |
| 204 | htm_muluncommitconfirm | 1Z0UWAKZK46B |  |  | 离职申请多选撤销确认 | 动态表单 | htm |
| 205 | htm_mytodotask | 2=DWICJBOSOV |  |  | 我的待办表单 | 动态表单 | htm |
| 206 | htm_operateresult | 2IUHOVVA3VWD |  |  | 操作结果弹窗 | 动态表单 | htm |
| 207 | htm_personalcard | 1ACM2AMEJ9VT |  |  | 离职人员卡片 | 动态表单 | htm |
| 208 | htm_quitcalendar | 2B+90RIG4EV+ |  |  | 离职管理日历 | 动态表单 | htm |
| 209 | htm_revokequitapply | 3D00//FBZY=4 |  |  | 离职撤回 | 动态表单 | htm |
| 210 | htm_termquitapply | 1Y0MSLSXS97R |  |  | 终止离职流程 | 动态表单 | htm |
| 211 | htm_termquitapplybatch | 3I/EWWAAVG65 |  |  | 批量终止离职 | 动态表单 | htm |
| 212 | htm_todotask | 29Z3DH61C+5E |  |  | 待跟进任务列表 | 动态表单 | htm |

## 布局（15）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | hdm_regularerrorcard | 26XVCO79L2YU |  |  | 待定卡片 | 布局/卡片 | hdm |
| 2 | hdm_regularpersoncard | 26XVJR3K+ZB2 |  |  | 转正人员列表卡片 | 布局/卡片 | hdm |
| 3 | hdm_regworkbenchtask | 34OYN1LG2ES9 |  |  | 转正任务跟踪卡片 | 布局/卡片 | hdm |
| 4 | hdm_regworkbenchwarming | 34OWPE3FHT2Y |  |  | 转正预警卡片 | 布局/卡片 | hdm |
| 5 | hlcm_card_quickentrance | 2=KFHEKWODZA |  |  | 快速入口小卡片 | 布局/卡片 | hlcm |
| 6 | hlcm_card_warning | 2=KEHDJR05WL |  |  | 合同风控预警卡片 | 布局/卡片 | hlcm |
| 7 | hom_wbcalendar | 2=3TJDHOLVLF |  |  | 入职工作日历 | 布局/卡片 | hom |
| 8 | hom_wbwaitin | 2=3=VS1=5VK5 |  |  | 待入职人员总览 | 布局/卡片 | hom |
| 9 | hom_wbwarning | 2=3BHDDXEXC1 |  |  | 工作台事务预警 | 布局/卡片 | hom |
| 10 | hpfs_hrbm_config | 432HYHA2=UT4 |  |  | 单据与人员映射关系配置 | 布局/卡片 | hpfs |
| 11 | htm_applytotalviewcard | 2=CNYEX85F1G |  |  | 离职总览 | 布局/卡片 | htm |
| 12 | htm_handtaskanalysecard | 2AWP3KM7SXUD |  |  | 办理任务分析 | 布局/卡片 | htm |
| 13 | htm_mytodotaskcard | 2=CO8E4M5H96 |  |  | 我的待办 | 布局/卡片 | htm |
| 14 | htm_quitcalendarcard | 2B+8QE42DBZN |  |  | 离职管理日历 | 布局/卡片 | htm |
| 15 | htm_todotaskcard | 29Z1DE93FMFH |  |  | 待跟进任务总览 | 布局/卡片 | htm |

## 查询模型（11）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | hdm_partperson | 5EJIMPY6ZLZS |  |  | 兼职人员 | 查询列表 | hdm |
| 2 | hdm_partpersonlist | 5EJNHPY=B=VK |  |  | 兼职人员 | 查询列表 | hdm |
| 3 | hdm_probationquery | 28IZDGKWCW0D |  |  | 转正试用期人员 | 查询列表 | hdm |
| 4 | hdm_probationquerylist | 28L7P407NL9A |  |  | 试用期人员 | 查询列表 | hdm |
| 5 | hdm_regaskquerylist | 398570AI4=7C |  |  | 转正问询 | 查询列表 | hdm |
| 6 | hom_onbrdinfoquery | 3H3L2RRASR6= |  |  | 入职人员查询 | 查询列表 | hom |
| 7 | hom_onbrdinfoquerylist | 3H7VVCKTHRIA |  |  | 入职人员列表 | 查询列表 | hom |
| 8 | hspm_assignmentlist | 4SMDV=ZQS++R |  |  | 人员列表 | 查询列表 | hspm |
| 9 | hspm_assignmentquery | 4SMDCXAERKND |  |  | 人员列表 | 查询列表 | hspm |
| 10 | htm_quitpersonlist | 4UNEIY4RN/2O |  |  | 离职人员列表 | 查询列表 | htm |
| 11 | htm_quitpersonquery | 4UKK+KS+E1D6 |  |  | 离职人员列表 | 查询列表 | htm |

## 移动端（160）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | hdm_appdisable | /LEHHEKL4XV3 |  |  | 功能未开放 | 移动表单 | hdm |
| 2 | hdm_askdetail_layout | 38INOS6Q9WLA | t_hdm_regaskdetails | hdm_regaskdetails | Mob_单个转正问询详情 | 移动单据 | hdm |
| 3 | hdm_askdetails_layout_m | 38IKTE4P/IGE | t_hdm_regaskdetails | hdm_regaskdetails | Mob_转正问询明细_布局 | 移动单据 | hdm |
| 4 | hdm_batchparttimemob | 4HH=5VVRWNCG | t_hdm_parttimebatch | hdm_batchparttime | 批量兼职申请 | 移动单据 | hdm |
| 5 | hdm_batchregbillmob | 4HRR3PKVYY+L | t_hdm_regbatch | hdm_batchregbill | 批量转正申请 | 移动单据 | hdm |
| 6 | hdm_batchtransfermob | 4IX+/P+3Q0X2 | t_hdm_transferbatch | hdm_transferbatch | 批量调动申请 | 移动单据 | hdm |
| 7 | hdm_bemptransferdetail | 4MK+SW8=PBDP | t_hdm_transferbill | hdm_transferapply | 员工调动信息 | 移动单据 | hdm |
| 8 | hdm_bparttimedetail | 4IQMVO/CKDE4 |  |  | 员工兼职信息 | 移动表单 | hdm |
| 9 | hdm_bregentrydetail | 4IU3KXVNLAOZ |  |  | 员工转正信息 | 移动表单 | hdm |
| 10 | hdm_btransferdetail | 4IX+P+R9YO3G |  |  | 员工调动信息 | 移动表单 | hdm |
| 11 | hdm_empparttimeinfo | 4MDCB2ZVRHJP | t_hdm_parttimesuperior | hdm_parttimeapplybill | 员工兼职信息 | 移动单据 | hdm |
| 12 | hdm_mytransferbill_m | 5=73B7K=KG5V | t_hdm_transferbill | hdm_mytransferbill | 我要调动_移动审核页面 | 移动单据 | hdm |
| 13 | hdm_parttimeapplybillmob | 4HH0OEHIWBNP | t_hdm_parttimesuperior | hdm_parttimeapplybill | 兼职申请 | 移动单据 | hdm |
| 14 | hdm_quit_view | 2CRR1Z+0=YJ3 |  |  | 提示-离职员工 | 移动表单 | hdm |
| 15 | hdm_quiting_view | 2DDKIXMYKVJ3 |  |  | 提示-离职中 | 移动表单 | hdm |
| 16 | hdm_regbasebill_layout | 2YZ1WM3V=C62 | t_hdm_regbasebill | hdm_regbasebill | 转正申请单_代办审批页 | 移动单据 | hdm |
| 17 | hdm_regevent_layout | 3GI/2YRD+3B= | t_hdm_regkeyevent | hdm_regevent | 试用期关键事件_详情 | 移动单据 | hdm |
| 18 | hdm_regevent_selflist_m | 3QGT86C7W/CP | t_hdm_regkeyevent | hdm_regevent | 试用期关键事件_员工列表 | 移动单据 | hdm |
| 19 | hdm_reginfodetail | 4N/NQ=CC=K1= | t_hdm_regbasebill | hdm_regbasebill | 员工转正信息 | 移动单据 | hdm |
| 20 | hdm_reging_view | 2CZXTEOKE03J |  |  | 提示-在途转正单 | 移动表单 | hdm |
| 21 | hdm_regself_supply_m | 2ZTX5KEAGHEB | t_hdm_regbasebill | hdm_regselfhelpbill | 员工转正申请单_布局 | 移动单据 | hdm |
| 22 | hdm_regselfhelp | 2CP/=UUI6CBZ | t_hdm_regbasebill | hdm_regselfhelpbill | 员工自助发起转正 | 移动单据 | hdm |
| 23 | hdm_regular_view | 2CS7I=6EXQFX |  |  | 提示-正式员工 | 移动表单 | hdm |
| 24 | hdm_regworksum_edit | 2DYHXKTQHUW9 |  |  | 工作总结 | 移动表单 | hdm |
| 25 | hdm_self_submited | 2DDPRO3OU7UJ |  |  | 提示-转正单已提交 | 移动表单 | hdm |
| 26 | hdm_selfhelp_entry | 2DD35+546NXM |  |  | 自助转正入口 | 移动表单 | hdm |
| 27 | hdm_transfer_jobinfo_m | 3PLAVVDI3S0K | t_hdm_transferbill | hdm_transferlayoutbill | 调动基础布局_审批页_任职信息 | 移动单据 | hdm |
| 28 | hdm_transfer_joblevel_m | 3PLED0AEXXAS | t_hdm_transferbill | hdm_transferlayoutbill | 调动基础布局单据_审批页_职级职等 | 移动单据 | hdm |
| 29 | hdm_transferapply_audit_m | 3NJKHFQZL5VZ | t_hdm_transferbill | hdm_transferapply | 调动申请_移动审批页 | 移动单据 | hdm |
| 30 | hdm_transferdescription_m | 3O+I679=LTEX | t_hdm_transferbill | hdm_transferlayoutbill | 调动说明_查看态_移动端 | 移动单据 | hdm |
| 31 | hdm_transferinbill_m | 3PVWYO00OCZP | t_hdm_transferbill | hdm_transferinbill | 调入申请的移动端布局 | 移动单据 | hdm |
| 32 | hdm_transferlayout_head_m | 3NIMFF11AGWP | t_hdm_transferbill | hdm_transferlayoutbill | 调动基础布局_审批页_单据头 | 移动单据 | hdm |
| 33 | hdm_transferoutbill_m | 3PHMU6CT39WU | t_hdm_transferbill | hdm_transferoutbill | 调出申请的移动端布局 | 移动单据 | hdm |
| 34 | hlcm_contractchange_m | 4IY7KX4F9OK9 | t_hlcm_signbill | hlcm_contractapplychange | 劳动合同改签申请 | 移动单据 | hlcm |
| 35 | hlcm_contractinformation | 4A5NWDTR1U=E |  |  | 劳动合同和协议签署 | 移动表单 | hlcm |
| 36 | hlcm_contractrenew_m | 4HKWFM+TW61I | t_hlcm_signbill | hlcm_contractapplyrenew | 劳动合同续签申请 | 移动单据 | hlcm |
| 37 | hlcm_empinfoconfirm | 2=JMG0SMR/E0 |  |  | 员工个人信息确认 | 移动表单 | hlcm |
| 38 | hlcm_empprotocolnew_m | 4NMTBCY3RD3S | t_hlcm_signbill | hlcm_empprotocolnew | 用工协议新签申请 | 移动单据 | hlcm |
| 39 | hlcm_empprotocolrelieve_m | 4NMTWP8RUCMW | t_hlcm_signbill | hlcm_empprotocolrelieve | 用工协议解除申请 | 移动单据 | hlcm |
| 40 | hlcm_invalidateloadpage | 4KVUQMJ9XKZ8 |  |  | 合同作废加载页 | 移动表单 | hlcm |
| 41 | hlcm_maincontract_m | 4JX+M/JRL9S+ | t_hlcm_contract | hlcm_contract | 关联合同信息 | 移动单据 | hlcm |
| 42 | hlcm_ontractcancel_m | 4IY7VHQ4KHXE | t_hlcm_signbill | hlcm_contractapplycancel | 劳动合同解除申请 | 移动单据 | hlcm |
| 43 | hlcm_ontractnew_m | 4HHVB1Z3W/KM | t_hlcm_signbill | hlcm_contractapplynew | 劳动合同新签申请 | 移动单据 | hlcm |
| 44 | hlcm_ontractstop_m | 4IY7ZV1J0HZV | t_hlcm_signbill | hlcm_contractapplyend | 劳动合同终止申请 | 移动单据 | hlcm |
| 45 | hlcm_otheragreements_m | 4JW5L1V0OG8W | t_hlcm_signbill | hlcm_otheragreements | 其他附属协议新签申请 | 移动单据 | hlcm |
| 46 | hlcm_partyahis | 4I7AUJ5PBPRO | t_hbss_signcompanyhis | hbss_signcompanyhis | 甲方历史信息 | 移动单据 | hlcm |
| 47 | hlcm_personinfo | 4HKTOBW758SX |  |  | 移动端人员档案卡片 | 移动表单 | hlcm |
| 48 | hlcm_privacystatement | 2AUWE1GSU23U |  |  | 隐私声明 | 移动表单 | hlcm |
| 49 | hlcm_protocolinfodetail_m | 4HO+01ZU=YWO |  |  | 待新签其他附属协议信息 | 移动表单 | hlcm |
| 50 | hlcm_signcontractinfo | 2AY357T=2W+8 | t_hlcm_signbill | hlcm_signmanagebase | 合同信息 | 移动单据 | hlcm |
| 51 | hlcm_signfailed | 2XZOY/Z+=131 |  |  | 签署失败 | 移动表单 | hlcm |
| 52 | hlcm_signinitialization | 2U=MAQ4DNOIT |  |  | 电子合同签署 | 移动表单 | hlcm |
| 53 | hlcm_signresult | 2=DCH/PAEFOI |  |  | 签署结果反馈 | 移动表单 | hlcm |
| 54 | hom_canaddress_m | 5853UD1E4DSS | t_hcf_canaddress | hom_canaddress_c | 地址信息 | 移动单据 | hom |
| 55 | hom_canbankcard_m | 53BYLV1BFJE9 | t_hcf_canbankcard | hom_canbankcard_c | 银行卡信息 | 移动单据 | hom |
| 56 | hom_cancontact_m | 53BL9LNM/GD0 | t_hcf_cancontact | hom_cancontact_c | 紧急联系人 | 移动单据 | hom |
| 57 | hom_cancontactinfo_m | 5351MVHBEALC | t_hcf_cancontactinfo | hom_cancontactinfo_c | 联系方式 | 移动单据 | hom |
| 58 | hom_cancre_m | 538L0VCISUPT | t_hcf_cancre | hom_cancre_c | 证件信息 | 移动单据 | hom |
| 59 | hom_candidate_m | 52T8ZY=/4=5J | t_hcf_candidate | hom_candidate_c | 基本信息 | 移动单据 | hom |
| 60 | hom_caneduexp_m | 53C9447YVBA9 | t_hcf_caneduexp | hom_caneduexp_c | 教育经历 | 移动单据 | hom |
| 61 | hom_canfamily_m | 53BUNNC5OLA5 | t_hcf_canfamily | hom_canfamily_c | 家庭成员 | 移动单据 | hom |
| 62 | hom_canlgability_m | 53C+VODIAII+ | t_hcf_canlgability | hom_canlgability_c | 语言技能 | 移动单据 | hom |
| 63 | hom_canprework_m | 538J004UD2N7 | t_hcf_canprework | hom_canprework_c | 前工作经历 | 移动单据 | hom |
| 64 | hom_checkinbackward | 282EOO9GBYQG |  |  | 自助报到-逆向场景 | 移动表单 | hom |
| 65 | hom_checkined | 28HN6O8PCE2M |  |  | 自助报到-已报到 | 移动表单 | hom |
| 66 | hom_checkinempty | 26RGSCH/2AJC |  |  | 自助报到-无信息 | 移动表单 | hom |
| 67 | hom_checkinfo_m | 53AZQWCEFSZ8 | t_hom_onbrdbill | hom_checkinfo_c | 体检信息 | 移动单据 | hom |
| 68 | hom_checkinlocmap | 29ELA0CM8U95 |  |  | 自助报到-公司位置地图 | 移动表单 | hom |
| 69 | hom_checkinmap | 26=HZU1S6TMR |  |  | 自助报到-地图 | 移动表单 | hom |
| 70 | hom_checkinsuccess | 26TRNLOBYR/A |  |  | 自助报到-成功 | 移动表单 | hom |
| 71 | hom_checkinwait | 26RGQ+TW/UD/ |  |  | 自助报到-待报到 | 移动表单 | hom |
| 72 | hom_chekincommit | 2DXY8VKN/0RY |  |  | 自助报到-确认报到地图 | 移动表单 | hom |
| 73 | hom_collectdetailmob | 52T2YZM3O7WC | t_hom_collect | hom_collect | 移动端采集活动详情 | 移动单据 | hom |
| 74 | hom_companylocal | 2FN8XZUVKKVU |  |  | 自助报到-报到地点 | 移动表单 | hom |
| 75 | hom_datacheck | 2608ATI3+TZK |  |  | 资料验收 | 移动表单 | hom |
| 76 | hom_defaultemptypage_m | 57LQ=+POEZN9 |  |  | 入职公用空白表单 | 移动表单 | hom |
| 77 | hom_guidedetail | 26R66X8YFJK4 |  |  | 入职指引详情 | 移动表单 | hom |
| 78 | hom_homecheckin | 26GG4UFWA2M8 |  |  | 入职报到主页 | 移动表单 | hom |
| 79 | hom_homepage | 26GDGPUW+RG7 |  |  | 主页内容 | 移动表单 | hom |
| 80 | hom_infogroupmoblist | 535RFLWLODWZ |  |  | 移动端信息组列表布局 | 移动表单 | hom |
| 81 | hom_inmapscene | 2FN598+7+MNZ |  |  | 自助报到-地图场景 | 移动表单 | hom |
| 82 | hom_login | 25EFUS4A2OJZ |  |  | 登录 | 移动表单 | hom |
| 83 | hom_loginempty | 2DDI303+KR=H |  |  | 登录-无数据处理 | 移动表单 | hom |
| 84 | hom_loginjump | 2C8GVMY/TRBE |  |  | 登录跳转页面 | 移动表单 | hom |
| 85 | hom_moblanguage | 26MX6F6UI+F3 |  |  | 语言切换 | 移动表单 | hom |
| 86 | hom_mobreservation | 26DT04ZFJ5Z/ |  |  | 预约报到 | 移动表单 | hom |
| 87 | hom_navigationbar | 268/NWNG8UIM |  |  | 导航条 | 移动表单 | hom |
| 88 | hom_navigationmob | 260B9ATKJUX6 | t_hom_onbrdbill | hom_onbrdbilltpl | 手机导航条 | 移动单据 | hom |
| 89 | hom_onbrdinfo_h5 | 3PDJKW1L2AFW | t_hom_onbrdbill | hom_onbrdinfo | 入职办理 | 移动单据 | hom |
| 90 | hom_outmap | 2F/SDCFCD3=K |  |  | 自助报到-异常报到 | 移动表单 | hom |
| 91 | hom_personalarea_m | 57TLISQ/FKL1 | t_hcf_personalarea | hom_personalarea_c | 区域信息 | 移动单据 | hom |
| 92 | hom_personalcenter | 26DY6CGB5QCZ |  |  | 个人中心 | 移动表单 | hom |
| 93 | hom_photodemo | 2=R=KZ=VLWU9 |  |  | 证件照示例 | 移动表单 | hom |
| 94 | hom_privacy | 25P230EP9/4Y |  |  | 隐私页面 | 移动表单 | hom |
| 95 | hom_privacyshow | 2GKU516DTEWA |  |  | 隐私声明 | 移动表单 | hom |
| 96 | hom_rejectinfo | 27/9MA+X0LGB |  |  | 驳回提示弹窗 | 移动表单 | hom |
| 97 | hom_rsmpatinv_m | 53C/L33NNFN6 | t_hcf_rsmpatinv | hom_rsmpatinv_c | 专利发明 | 移动单据 | hom |
| 98 | hom_welcome | 25E7GE4MQ8AH |  |  | 欢迎页 | 移动表单 | hom |
| 99 | hom_welcomeconfigview | 2727VVNBW6B1 |  |  | 欢迎信配置预览 | 移动表单 | hom |
| 100 | hom_workcardphoto_m | 532BYQQVC1PW | t_hom_obdasuperior | hom_workcardphoto_c | 工卡照 | 移动单据 | hom |
| 101 | hpfs_hrplus | 26NMKD0NMSD4 |  |  | HR Plus | 移动表单 | hpfs |
| 102 | hspm_appointremove_emly | 539CD2I4WFP4 | t_hrpi_appointremoverel | hspm_appointremoverel_e | 任免经历 | 移动单据 | hspm |
| 103 | hspm_approvalsuccess | 2CYG1062VD0+ |  |  | 提交成功 | 移动表单 | hspm |
| 104 | hspm_changerecordmob | 2P4AM4Q6D9SI |  |  | 修改记录 | 移动表单 | hspm |
| 105 | hspm_contractinfo_emly | 4VRABG2BUBJ5 | t_hrpi_contractinfo | hspm_contractinfo_e | 合同信息 | 移动单据 | hspm |
| 106 | hspm_debardinfo_emly | 539GAM7WVQM8 | t_hrpi_debardinfo | hspm_debardinfo_e | 回避信息 | 移动单据 | hspm |
| 107 | hspm_defaultemptypagemob | 5MRD+226=3B/ |  |  | 移动端专用空白页面 | 移动表单 | hspm |
| 108 | hspm_dispatchinfo_emly | 51YQ/9ZQXY4L | t_hrpi_dispatchinfo | hspm_dispatchinfo_e | 外派信息 | 移动单据 | hspm |
| 109 | hspm_empcadre_emly | 539CGJ/INJ+O | t_hrpi_empcadre | hspm_empcadre_e | 最高干部身份信息 | 移动单据 | hspm |
| 110 | hspm_empjobrel_emly | 4TH9IYYCJ0ST | t_hrpi_empjobrel | hspm_empjobrel_e | 职级职等 | 移动单据 | hspm |
| 111 | hspm_employee_emly | 4TA=6070X=/3 | t_hrpi_employee | hspm_employee_e | 基本信息 | 移动单据 | hspm |
| 112 | hspm_employee_tlmob | 5JM4CH0SUXOV | t_hrpi_employee | hspm_employee_tl | 管理者_基本信息 | 移动单据 | hspm |
| 113 | hspm_empposorgrel_emly | 4TAYKMZKBWQO | t_hrpi_empposorgrel | hspm_empposorgrel_e | 任职经历 | 移动单据 | hspm |
| 114 | hspm_empposorgrel_tlmob | 5JM6GZ+HA5K+ | t_hrpi_empposorgrel | hspm_empposorgrel_tl | 管理者_任职经历 | 移动单据 | hspm |
| 115 | hspm_empproexp_emly | 551713M/QKKQ | t_hrpi_empproexp | hspm_empproexp_e | 项目经历 | 移动单据 | hspm |
| 116 | hspm_empproexp_tlmob | 5JM7PU1DLQ0H | t_hrpi_empproexp | hspm_empproexp_tl | 管理者_项目经历 | 移动单据 | hspm |
| 117 | hspm_empsuprel_emly | 539D+3NLOAJ4 | t_hrpi_empsuprel | hspm_empsuprel_e | 汇报关系 | 移动单据 | hspm |
| 118 | hspm_emptrainfile_emly | 52/JMGO=BCZ6 | t_hrpi_emptrainfile | hspm_emptrainfile_e | 培训经历 | 移动单据 | hspm |
| 119 | hspm_emptutor_emly | 52/NM8JZ1J=V | t_hrpi_emptutor | hspm_emptutor_e | 导师 | 移动单据 | hspm |
| 120 | hspm_ermanfilemob | 4KVYSVDCOWNP |  |  | 员工端档案 | 移动表单 | hspm |
| 121 | hspm_ermanfilemoblist | 4KSVB6A4XM9K |  |  | 档案附表实体列表 | 移动表单 | hspm |
| 122 | hspm_familymemb_emly | 4TAT8PYW+SP+ | t_hrpi_familymemb | hspm_familymemb_e | 家庭成员 | 移动单据 | hspm |
| 123 | hspm_fertilityinfo_emly | 4TATAN0/F4Y+ | t_hrpi_fertilityinfo | hspm_fertilityinfo_e | 生育信息 | 移动单据 | hspm |
| 124 | hspm_headmobile | 4L4RCR2QEW37 |  |  | 员工档案头部_移动端 | 移动表单 | hspm |
| 125 | hspm_infoapproval_layout | 2E=M76M5CKO= | t_hspm_infoapproval | hspm_infoapproval | 人员档案信息变更申请_内嵌布局 | 移动单据 | hspm |
| 126 | hspm_infoapproval_m | 4JIACNYCL5S1 | t_hspm_infoapproval | hspm_infoapproval | 人员档案信息变更申请 | 移动单据 | hspm |
| 127 | hspm_infochangetip | 4KI=FYKJOFK4 |  |  | 信息组变更提示词 | 移动表单 | hspm |
| 128 | hspm_infodetailmob | 4JIANCYMTLN/ |  |  | 信息组字段审批移动端 | 移动表单 | hspm |
| 129 | hspm_infomobattchment | 4KLFN4E/5/9V |  |  | 附件 | 移动表单 | hspm |
| 130 | hspm_listtpl_hrmanfiletlm | 5K91/11/Y/P+ |  |  | 移动端标准列表（管理端_人员档案) | 移动表单 | hspm |
| 131 | hspm_partymember_emly | 539CLSX2LQE9 | t_hrpi_partymember | hspm_partymember_e | 党员信息 | 移动单据 | hspm |
| 132 | hspm_peraddress_emly | 4T8UX=NW+Z0N | t_hrpi_peraddress | hspm_peraddress_e | 人员地址 | 移动单据 | hspm |
| 133 | hspm_percontact_emly | 4SSIRLR=SJ46 | t_hrpi_percontact | hspm_percontact_e | 联系方式 | 移动单据 | hspm |
| 134 | hspm_percontact_tlmob | 5JM5BKFR49UV | t_hrpi_percontact | hspm_percontact_tl | 管理者_联系方式 | 移动单据 | hspm |
| 135 | hspm_percre_emly | 4TV1T37I45W4 | t_hrpi_percre | hspm_percre_e | 证件信息 | 移动单据 | hspm |
| 136 | hspm_pereduexp_emly | 4TH7A00FU8PC | t_hrpi_pereduexp | hspm_pereduexp_e | 教育经历 | 移动单据 | hspm |
| 137 | hspm_pereduexp_tlmob | 5JM5RFTL5KBA | t_hrpi_pereduexp | hspm_pereduexp_tl | 管理者_教育经历 | 移动单据 | hspm |
| 138 | hspm_perfresult_emly | 5IMF7CBAGFWT | t_hrpi_perfresult | hspm_perfresult_e | 绩效结果 | 移动单据 | hspm |
| 139 | hspm_perhobby_emly | 4STBBNV1253J | t_hrpi_perhobby | hspm_perhobby_e | 特长及爱好 | 移动单据 | hspm |
| 140 | hspm_perlgability_emly | 4TATD168S4GV | t_hrpi_perlgability | hspm_perlgability_e | 语言技能 | 移动单据 | hspm |
| 141 | hspm_perocpqual_emly | 52/G6/9MXN4F | t_hrpi_perocpqual | hspm_perocpqual_e | 职业资格 | 移动单据 | hspm |
| 142 | hspm_perpractqual_emly | 539CPE/NVTNX | t_hrpi_perpractqual | hspm_perpractqual_e | 执业资格 | 移动单据 | hspm |
| 143 | hspm_perprotitle_emly | 4T8VIANJ79=2 | t_hrpi_perprotitle | hspm_perprotitle_e | 职称信息 | 移动单据 | hspm |
| 144 | hspm_perregion_emly | 4ST1=B7C8GCV | t_hrpi_perregion | hspm_perregion_e | 区域信息 | 移动单据 | hspm |
| 145 | hspm_perrprecord_emly | 539CR6+516QI | t_hrpi_perrprecord | hspm_perrprecord_e | 奖惩记录 | 移动单据 | hspm |
| 146 | hspm_perserlen_emly | 4SWCRTAYOMUH | t_hrpi_perserlen | hspm_perserlen_e | 服务年限 | 移动单据 | hspm |
| 147 | hspm_preworkexp_emly | 4T8WYH3QGLOP | t_hrpi_preworkexp | hspm_preworkexp_e | 前工作经历 | 移动单据 | hspm |
| 148 | hspm_preworkexp_tlmob | 5JM753=UJDUI | t_hrpi_preworkexp | hspm_preworkexp_tl | 管理者_前工作经历 | 移动单据 | hspm |
| 149 | hspm_rotationinfo_emly | 522E+RK7Z4Q9 | t_hrpi_rotationinfo | hspm_rotationinfo_e | 轮岗情况 | 移动单据 | hspm |
| 150 | hspm_rsmpatinv_emly | 52+YT3J=XPT1 | t_hrpi_rsmpatinv | hspm_rsmpatinv_e | 专利发明 | 移动单据 | hspm |
| 151 | hspm_rsmproskl_emly | 4T8WSGQ7WQ=5 | t_hrpi_rsmproskl | hspm_rsmproskl_e | 专业技能 | 移动单据 | hspm |
| 152 | hspm_trialperiod_emly | 4SSZES9KW667 | t_hrpi_trialperiod | hspm_trialperiod_e | 试用期 | 移动单据 | hspm |
| 153 | htm_activitypanelmob | 254CLLGW6C49 |  |  | 移动端离职活动面板 | 移动表单 | htm |
| 154 | htm_barcodemobile | 28SQA8RI8O7L |  |  | 移动端条形码页面 | 移动表单 | htm |
| 155 | htm_hrapproval | 25R3XN9XKPX4 | t_htm_quitapplybill | htm_quitapplyemp | HR审批 | 移动单据 | htm |
| 156 | htm_operatesuccess | 25K3C48BU61T |  |  | 操作成功页面 | 移动表单 | htm |
| 157 | htm_personinfo | 2XSQ+DGCHYP/ |  |  | 移动端人员档案卡片 | 移动表单 | htm |
| 158 | htm_quitapplymobaudit | 2I3S+5XINLT5 | t_htm_quitapplybill | htm_quitapply | 离职代申请移动端审批 | 移动单据 | htm |
| 159 | htm_superapproval | 25R4=1Z2L1B1 | t_htm_quitapplybill | htm_quitapplyemp | 上级审批 | 移动单据 | htm |
| 160 | htm_supervisorapproval | 25OAO5X9AVVQ | t_htm_quitapplybill | htm_quitapplyemp | 主管审批 | 移动单据 | htm |

## 其他（3）

| 序号 | 元数据 | 表单ID | 物理表 | 关联实体/共享元数据 | 表单名称 | 表单类型 | 所属应用 |
|------|--------|--------|--------|---------------------|----------|----------|----------|
| 1 | hdm_appconfig | 3PA1CX9M8/V9 |  |  | 调配应用参数 | ParameterFormModel_application | hdm |
| 2 | hom_appconfig | 2W/4E6=5UDU= |  |  | 入职应用参数 | ParameterFormModel_application | hom |
| 3 | hpfs_appconfig | 2XPK377CKI5X |  |  | 应用参数 | ParameterFormModel_application | hpfs |
