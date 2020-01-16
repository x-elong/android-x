package com.example.eletronicengineer.model

class Constants {
    //Request Code
    enum class RequestCode {
        REQUEST_PHOTOGRAPHY,//拍照
        REQUEST_PICK_IMAGE,//选取照片
        REQUEST_PICK_FILE,//选取文件
        REQUEST_UPLOAD_FILE,//上传文件
        REQUEST_DOWNLOAD_FILE,//下载文件
        MY_SCAN_REQUEST_CODE
    }
    enum class InventoryType{

    }
    enum class HandlerMsg {
        UPLOAD_CLEARANCE_SALARY_SELECT_OK,//清工薪资
        UPLOAD_LIST_QUOTE_SELECT_OK//清单报价
    }

    enum class FragmentType {
        //行业黄页
        INDUSTRYYELLOWPAGES_TYPE,//行业黄页
        DEMAND_INDIVIDUAL_TYPE,
        DEMAND_GROUP_TYPE,
        DEMAND_LEASE_TYPE,
        DEMAND_TRIPARTITE_TYPE,
        PERSONAL_TYPE,//个人
        TRIPARTITE_TYPE,//三方
        PERSONAL_GENERAL_WORKERS_TYPE,  //个人普工
        PERSONAL_LEADING_CADRE_TYPE,//个人负责人
        PERSONAL_ENGINEER_TYPE,//个人工程师
        PERSONAL_DESIGNER_TYPE,//个人设计员
        PERSONAL_SPECIAL_WORK_TYPE,  //个人特种作业
        PERSONAL_PROFESSIONAL_OPERATION_TYPE,    //个人专业操作
        PERSONAL_SURVEYOR_TYPE,  //个人勘测员
        PERSONAL_DRIVER_TYPE,    //个人驾驶员
        PERSONAL_NINE_MEMBERS_TYPE,  //个人九大员
        PERSONAL_REGISTRATION_CLASS_TYPE,    //个人注册类
        PERSONAL_OTHER_TYPE, //个人其他
        SUBSTATION_CONSTRUCTION_TYPE,   //变电施工
        MAINNET_CONSTRUCTION_TYPE,      //主网施工
        DISTRIBUTIONNET_CONSTRUCTION_TYPE,  //配网施工
        MEASUREMENT_DESIGN_TYPE,    //测量设计
        CARAVAN_TRANSPORTATION_TYPE,    //马帮运输
        PILE_FOUNDATION_TYPE,   //桩基
        NON_EXCAVATION_TYPE,    //非开挖
        TEST_DEBUGGING_TYPE,    //试验调试
        CROSSING_FRAME_TYPE,    //跨越架
        OPERATION_AND_MAINTENANCE_TYPE, //运维
        VEHICLE_LEASING_TYPE,   //车辆
        TOOL_LEASING_TYPE,  //工器具
        MACHINERY_LEASING_TYPE, //机械
        EQUIPMENT_LEASING_TYPE, //设备
        TRIPARTITE_TRAINING_CERTIFICATE_TYPE,   //三方培训办证
        TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE,   //三方财务记账
        TRIPARTITE_AGENCY_QUALIFICATION_TYPE,   //三方代办资格
        TRIPARTITE_TENDER_SERVICE_TYPE, //三方标书服务
        TRIPARTITE_LEGAL_ADVICE_TYPE,   //三方法律咨询
        TRIPARTITE_SOFTWARE_SERVICE_TYPE,   //三方软件服务
        TRIPARTITE_OTHER_TYPE,  //三方其他
        PROVIDER_GROUP_PERSONAL_FRAGMENT,   //个人信息
        PROVIDER_GROUP_VEHICLE_FRAGMENT,    //车辆信息
        PROVIDER_GROUP_MEMBER_FRAGMENT
    }

    enum class Subitem_TYPE {


        NEW_PROJECT_DISK, //新建项目盘
        //架空
        OVERHEAD_MORE, //架空更多

        SELF_EXAMINATION,//自查自检
        SUPERVISION_ACCEPTANCE,//监理验收
        OWNER_ACCEPTANCE,//业主验收
        DATA_DYNAMIC,//资料动态

        OVERHEAD_ECONOMIC_INDICATOR, //经济指标

        OVERHEAD_TENSILE_SEGMENT,//耐张段
        OVERHEAD_TOWER_PARAMETERS, //杆塔参数

        OVERHEAD_TOWER_SUBITEM,  //杆塔子项
        OVERHEAD_RESURVEY_PITDIVIDING, //复测分坑
        OVERHEAD_FOUNDATION_EXCAVATION,  //基础开挖
        OVERHEAD_MATERIAL_TRANSPORTATION,//材料运输
        OVERHEAD_PREFABRICATED_LANDFILL,//预制件填埋
        OVERHEAD_FOUNDATION_POURING,//基础浇筑
        OVERHEAD_TOWER_ASSEMBLY,//杆塔组立
        OVERHEAD_WIRE_DRAWING,//拉线制作
        OVERHEAD_CROSSBAR_INSTALLATION,//横担安装
        OVERHEAD_INSULATOR_MOUNTING,//绝缘子安装
        OVERHEAD_WELDING_FABRICATION,//焊接制作
        OVERHEAD_WIRE_ERECTION,//导线架设
        OVERHEAD_ACCESSORIES_INSTALLING,//附件安装
        OVERHEAD_EQUIPMENT_INSTALLATION,//设备安装
        OVERHEAD_GROUND_LAYING,//接地敷设
        OVERHEAD_TABLE_INSTALLATION,//户表安装
        OVERHEAD_LIVE_WORK,//带电作业

        OVERHEAD_PUBLIC_POINT_POSITION,//公共点定位
        //节点
        NODE_ECONOMIC_INDICATOR, //经济指标

        NODE_PARAMETERS,  //节点参数

        NODE_SUBITEMS,//节点子项
        NODE_RESURVEY_PITDIVIDING, //复测分坑
        NODE_FOUNDATION_EXCAVATION,  //基础开挖
        NODE_MATERIAL_TRANSPORTATION,//材料运输
        NODE_MANUFACTURE_INSTALLATION,//预制构件制作及安装
        NODE_FOUNDATION_POURING,//基础浇筑
        NODE_EQUIPMENT_INSTALLATION,//设备安装
        NODE_GROUND_LAYING,//接地敷设
        NODE_TABLE_INSTALLATION,//户表安装

        //通道
        PASSAGEWAY_ECONOMIC_INDICATOR,//经济指标
        PASSAGEWAY_CHANNEL_PARAMETERS,//通道参数

        PASSAGEWAY_CHANNEL_SUBITEMS,//通道子项
        PASSAGEWAY_RESURVEY_PITDIVIDING, //复测分坑
        PASSAGEWAY_FOUNDATION_EXCAVATION,  //基础开挖
        PASSAGEWAY_MATERIAL_TRANSPORTATION,//材料运输
        PASSAGEWAY_MANUFACTURE_INSTALLATION,//预制构件制作及安装
        PASSAGEWAY_FOUNDATION_POURING,//基础浇筑
        PASSAGEWAY_CABLE_PIPING,//电缆配管
        PASSAGEWAY_CABLE_TRAY,//电缆桥架
        PASSAGEWAY_CABLE_LAYING,//电缆敷设
        PASSAGEWAY_CABLE_HEAD_FABRICATION,//电缆头制作
        PASSAGEWAY_CABLE_FIRE_PROTECTION,//电缆防火
        PASSAGEWEAY_CABLE_TEST,//电缆试验
        PASSAGEWAY_GROUND_LAYING,//接地敷设
    }

    enum class EXCEL_TYPE {
        EXCEL_MEMBER,
        EXCEL_VEHICLE
    }

    enum class PAGE_TYPE {
        PERSONAL,
        VEHICLE
    }
    class Goods{
        companion object{
            const val ELITE_VIP = "1"
            const val GOLD_VIP = "2"
        }
    }
    class HttpUrlPath {

        //我的
        class My{
            companion object{

                const val getRemoteCode = "UserGoodsPower/getRemoteCode/"
                /**
                 * @VIP会员
                 */
                const val numPayCreatOrder="numpay/creatOrder/{productId}"
                const val creatOrder = "weixinPay/creatOrderByApp/{productId}"
                const val payNotify ="weixinPay/payNotifyByApp/{orderNumber}"
                const val openPowerNotify= "UserGoodsPower/userOpenPower/{transactionOrderId}"
                const val getAliPayOrderStr ="alipay/getAliPayOrderStr/{productId}"
                const val checkAlipay="alipay/checkAlipay/"
                const val getUserOpenPower ="UserGoodsPower/getUserOpenPower/"

                const val updateHeaderImg = "TUpmsUser/updateHeaderImg/"
                const val updatePassword = "userInfo/alterPassword/"
                const val getUser="TUpmsUser/getUser/"
                const val upateDTO1 = "TUpmsUser/upateDTO1/"
                const val upateBankCar = "TUpmsUser/upateBankCar/"

                //邮箱
                const val sendEmailCode= "Mail/sendMailCode/{receiver}"
                const val retrievePasswordSendMailCode = "Mail/retrievePasswordSendMailCode/{receiver}"
                const val retrievePasswordValidMailCode = "Mail/retrievePasswordValidMailCode/{receiver}/{mailCode}"
                const val bindEmail = "Mail/validMailCode/{receiver}/{mailCode}"
                //个人学历
                const val getEducationBackground = "TUpmsUser/getAllEducationBackground/"
                const val updateEducationBackground = "TUpmsUser/updateEducationBackground/"
                const val insertEducationBackground = "TUpmsUser/insertEducationBackground/"
                const val deleteEducationBackground = "TUpmsUser/deleteEducationBackground/{id}"
                //家庭子女
                const val getHomeChildren =  "HomeChildren/GetAllHomeChildren/"
                const val delectChildren = "HomeChildren/deleteHomeChildren/{id}"
                const val updateHomeChildren = "HomeChildren/updateHomeChildren/"
                const val insertHomeChildren = "HomeChildren/insertHomeChildren/"
                //紧急联系人
                const val getUrgentPeople = "UrgentPeople/GetAllUrgentPeople/"
                const val updateUrgentPeople = "UrgentPeople/updateUrgentPeople/"
                const val insertUrgentPeople = "UrgentPeople/insertUrgentPeople/"
                const val deleteUrgentPeople = "UrgentPeople/deleteUrgentPeople/{id}"
                ///银行卡
                const val getBankCard = "TUpmsUser/getBankCard/"
                const val insertBankCard = "TUpmsUser/insertBankCard/"
                const val deleteBankCard = "TUpmsUser/deleteBankCard/{id}"
                //会员证书
                const val  getCertificate= "Certificate/GetAllCertificate/"
                const val insertCertificate = "Certificate/insertCertificate/"
                const val updateCertificate = "Certificate/updateCertificate/"
                const val deleteCertificate = "Certificate/deleteCertificate/{id}"
                /**
                 *我的发布
                 */
                const val getDemandIndividual = "RequirementPerson/getOwnByQueryConditions/"
                const val updateCheckDemandIndividual = "PersonRequirementInformationCheck/registrationNumber/{requirementPersonId}"

                const val getDemandGroup = "RequirementTeam/getRequirementTeamDTOListByFounder/"
                const val updateCheckDemandGroup = "RequirementTeamLoggingCheck/registrationNumber/{requirementTeamId}"

                const val getDemandLease = "RequirementLease/getRequirementLeaseDTOListByFounder/"
                const val updateCheckDemandLease = "LeaseLoggingCheck/registrationNumber/{requirementLeaseId}"

                const val getDemandTripartite = "RequirementThirdParty/getAllByFounder/"
                const val updateCheckDemandTripartite = "RequirementThirdLoggingCheck/registrationNumber/{requirementThirdId}"

                const val getPersonalIssue = "PersonalIssue/selectListPersonalIssueBytoken/"
                const val getTeamService = "RunningMaintain/getRunningMaintainALLDTOPerson/"
                const val getLeaseService = "Lease/getLeaseDTOListByFounder/"
                const val getThridService = "Third/selectByToken/"

                //行业黄页
                const val getIndustryYellowPages="IndustryYellowPages/getIndustryYellowPagesDTOListByFounder/"

                const val  getPersonalIssueMore = "PersonalIssue/insertPersonalIssue/{id}"
                const val getRequirementPersonMore = "RequirementPerson/getRequirementPerson/{id}"
                /**
                 * @报名
                 */
                const val getRegistration = "LeaseLoggingCheck/getByVip/"
                const val getPersonRequirement = "PersonRequirementInformationCheck/getListPersonRequirementInformationCheck/"
                const val getRequirementTeam = "RequirementTeamLoggingCheck/getRequirementTeamLoggingCheck/"
                const val getLease = "LeaseLoggingCheck/getListLeaseLoggingCheckController/"
                const val getRequirementThird = "RequirementThirdLoggingCheck/getListRequirementThirdLoggingCheck/"

                const val getPersonRequirement2 = "PersonRequirementInformationCheck/getList2PersonRequirementInformationCheck/"
                const val getRequirementTeam2 = "RequirementTeamLoggingCheck/getRequirementTeamLoggingCheck2/"
                const val getLease2 = "LeaseLoggingCheck/getList2LeaseLoggingCheckController/"
                const val getRequirementThird2 = "RequirementThirdLoggingCheck/getList2RequirementThirdLoggingCheck/"

                //需求个人
                const val deletePersonRequirementInformationCheck="PersonRequirementInformationCheck/deletePersonRequirementInformationCheck/{id}"
                //需求团队
                const val deleteRequirementTeamLoggingCheck= "RequirementTeamLoggingCheck/deleteRequirementTeamLoggingCheck/{id}"
                //车辆租赁
                const val deleteLeaseLoggingCheckController = "LeaseLoggingCheck/deleteLeaseLoggingCheckController/{id}"
                //需求三方
                const val deleteRequirementThirdLoggingCheck="RequirementThirdLoggingCheck/deleteRequirementThirdLoggingCheck/{id}"

                /**
                 * @认证
                 */

                const val enterpriseCertification = "OrganizationCertification/insertOrganizationCertification/"
                const val reCertification = "OrganizationCertification/updateOrganizationCertification/"

                const val personalCertification = "OrganizationCertification/insertOneOrganizationCertification/"
                const val certificationMore = "OrganizationCertification/getOrganizationCertificationDTOALL/"

                const val getALLOrganizationCertificationDTOList= "OrganizationCertification/getALLOrganizationCertificationDTOList/"
            }
        }

        //需求
        class Requirement {
            companion object {
                //个人报名
                const val insertPersonRequirementInformationCheck ="PersonRequirementInformationCheck/insertPersonRequirementInformationCheck/"
                //团队报名
                const val insertRequirementTeamLoggingCheck ="RequirementTeamLoggingCheck/insertRequirementTeamLoggingCheck/"
                //租赁报名
                const val insertLeaseLoggingCheckController ="LeaseLoggingCheck/insertLeaseLoggingCheckController/"
                //三方报名
                const val insertRequirementThirdLoggingCheck ="RequirementThirdLoggingCheck/insertRequirementThirdLoggingCheck/"
                //需求个人
                const val requirementPerson = "RequirementPerson/insertRequirementPerson/"
                //需求主网
                const val requirementMajorNetwork = "RequirementMajorNetwork/insertRequirementMajorNetwork/"
                //马帮运输
                const val requirementCaravanTransport =
                    "RequirementCaravanTransport/insertRequirementCaravanTransport/"
                //需求配网
                const val requirementDistribuionNetwork =
                    "RequirementDistribuionNetwork/insertRequirementDistribuionNetwork/"
                //测量设计
                const val requirementMeasureDesign = "RequirementMeasureDesign/insertRequirementMeasureDesign/"
                //桩基
                const val requirementPileFoundation =
                    "RequirementPileFoundation/insertRequirementPileFoundation/"
                //三方
                const val requirementThirdParty = "RequirementThirdParty/insertRequirementThirdParty/"
                //非开挖顶拉管
                const val requirementUnexcavation = "RequirementUnexcavation/insertRequirementUnexcavation/"
                //试验调试
                const val requirementTest = "RequirementTest/insertRequirementTest/"
                //跨越架
                const val requirementSpanWoodenSupprt =
                    "RequirementSpanWoodenSupprt/insertRequirementSpanWoodenSupprt/"
                //运行维护
                const val requirementRunningMaintain =
                    "RequirementRunningMaintain/insertRequirementRunningMaintain/"
                //变电施工
                const val requirementPowerTransformation =
                    "RequirementPowerTransformation/insertRequirementPowerTransformation/"
                //机械租赁
                const val requirementLeaseMachinery =
                    "RequirementLeaseMachinery/insertRequirementLeaseMachinery/"
                //车辆租赁
                const val requirementLeaseCar = "/RequirementLeaseCar/insertRequirementLeaseCar/"
                //工器具租赁
                const val requirementLeaseConstructionTool =
                    "RequirementLeaseConstructionTool/insertRequirementLeaseConstructionTool/"
                //设备租赁
                const val requirementLeaseFacility = "RequirementLeaseFacility/insertRequirementLeaseFacility/"
                //excel模板下载
                const val excel = "excel/downloadexcel/{fileName}/"


                /**
                 * @更新
                 */
                //需求个人
                const val updateRequirementPerson ="RequirementPerson/updateRequirementPerson/"
                //需求团队 主网
                const val updateRequirementMajorNetwork = "RequirementMajorNetwork/updateRequirementMajorNetwork/"
                //需求团队 配网
                const val updateRequirementDistribuionNetwork = "RequirementDistribuionNetwork/updateRequirementDistribuionNetwork/"
                //需求团队 变电
                const val updateRequirementPowerTransformation = "RequirementPowerTransformation/updateRequirementPowerTransformation/"
                //需求团队 测量设计
                const val updateRequirementMeasureDesign = "RequirementMeasureDesign/updateRequirementMeasureDesign/"
                //需求团队 马帮运输
                const val updateRequirementCaravanTransport = "RequirementCaravanTransport/updateRequirementCaravanTransport/"
                //需求团队 桩基服务
                const val updateRequirementPileFoundation = "RequirementPileFoundation/updateRequirementPileFoundation/"
                //需求团队 非开挖
                const val updateRequirementUnexcavation = "RequirementUnexcavation/updateRequirementUnexcavation/"
                //需求团队 试验调试
                const val updateRequirementTest = "RequirementTest/updateRequirementTest/"
                //需求团队 跨越架
                const val updateRequirementSpanWoodenSupprt = "RequirementSpanWoodenSupprt/updateRequirementSpanWoodenSupprt/"
                //需求团队 运行维护
                const val updateRequirementRunningMaintain = "RequirementRunningMaintain/updateRequirementRunningMaintain/"
                //需求团队 车辆租赁
                const val updateRequirementLeaseCar = "RequirementLeaseCar/updateRequirementLeaseCar/"
                //需求团队 工器具租赁
                const val updateRequirementLeaseConstructionTool = "RequirementLeaseConstructionTool/updateRequirementLeaseConstructionTool/"
                //需求团队 机械租赁
                const val updateRequirementLeaseMachinery = "RequirementLeaseMachinery/updateRequirementLeaseMachinery/"
                //需求团队 设备租赁
                const val updateRequirementLeaseFacility = "RequirementLeaseFacility/updateRequirementLeaseFacility/"
                //需求三方
                const val updateRequirementThirdParty = "RequirementThirdParty/updateRequirementThirdParty/"

                /**
                 * @删除
                 */

                //需求个人
                const val deleteRequirementPerson="RequirementPerson/deleteRequirementPerson/{id}"
                //需求团队 变电
                const val deleteRequirementPowerTransformation= "RequirementPowerTransformation/deleteRequirementPowerTransformation/{id}"
                //需求团队 主网
                const val deleteRequirementMajorNetwork= "RequirementMajorNetwork/deleteRequirementMajorNetwork/{id}"

                //需求团队 配网
                const val deleteRequirementDistribuionNetwork= "RequirementDistribuionNetwork/deleteRequirementDistribuionNetwork/{id}"

                //需求团队 测量
                const val deleteRequirementMeasureDesign= "RequirementMeasureDesign/deleteRequirementMeasureDesign/{id}"

                //需求团队 马帮
                const val deleteRequirementCaravanTransport= "RequirementCaravanTransport/deleteRequirementCaravanTransport/{id}"

                //需求团队 桩基
                const val deleteRequirementPileFoundation= "RequirementPileFoundation/deleteRequirementPileFoundation/{id}"
                //需求团队 非开挖
                const val deleteRequirementUnexcavation= "RequirementUnexcavation/deleteRequirementUnexcavation/{id}"
                //需求团队 跨越架
                const val deleteRequirementSpanWoodenSupprt= "RequirementSpanWoodenSupprt/deleteRequirementSpanWoodenSupprt/{id}"
                //需求团队 试验调试
                const val deleteRequirementTest= "RequirementTest/deleteRequirementTest/{id}"
                //需求团队 运行维护
                const val deleteRequirementRunningMaintain= "RequirementRunningMaintain/deleteRequirementRunningMaintain/{id}"

                //车辆租赁
                const val deleteRequirementLeaseCar = "RequirementLeaseCar/deleteRequirementLeaseCar/{id}"
                //工器具租赁
                const val deleteRequirementLeaseConstructionTool = "RequirementLeaseConstructionTool/deleteRequirementLeaseConstructionTool/{id}"
                //设备租赁
                const val deleteRequirementLeaseFacility = "RequirementLeaseFacility/deleteRequirementLeaseFacility/{id}"
                //机械租赁
                const val deleteRequirementLeaseMachinery = "RequirementLeaseMachinery/deleteRequirementLeaseMachinery/{id}"
                //需求三方
                const val deleteRequirementThirdParty="RequirementThirdParty/deleteRequirementThirdParty/{id}"
            }
        }
        //行业黄页
        class YellowPages{
            companion object{
                const val yellowPagesPublish = "IndustryYellowPages/insertIndustryYellowPages/"
            }
        }

        class Provider {
            companion object {
                //个人劳务
                const val PersonalService = "PersonalIssue/savePersonalIssue/"
                const val  updatePersonalIssue = "PersonalIssue/updatePersonalIssue/"
                const val deletePersonalIssue = "PersonalIssue/deletePersonalIssue/{id}"


                //      const val deleteMajorNetworkById= mainPath+"/deleteMajorNetwork/{id}"
//      const val deleteMajorNetworkAll= mainPath+"/deleteMajorNetwork"

                //车辆租赁
                const val LeaseCar = "LeaseCar/saveLeaseCar/"
                const val deleteLeaseCar = "LeaseCar/deleteLeaseCar/{id}"
                const val  updateLeaseCar = "LeaseCar/updateLeaseCar/"
                //工器具
                const val LcTool = "LeaseConstructionTool/insertLeaseConstructionTool/"
                const val deleteLeaseConstructionTool = "LeaseConstructionTool/deleteLeaseConstructionTool/{id}"
                const val  updateLeaseConstructionTool = "LeaseConstructionTool/updateLeaseConstructionTool/"
                //设备
                const val LeaseFacility = "LeaseFacility/insertLeaseFacility/"
                const val deleteLeaseFacility = "LeaseFacility/deleteLeaseFacility/{id}"
                const val  updateLeaseFacility = "LeaseFacility/updateLeaseFacility/"
                //机械
                const val LeaseMachinery = "LeaseMachinery/insertLeaseMachinery/"
                const val  deleteLeaseMachinery = "LeaseMachinery/deleteLeaseMachinery/{id}"
                const val updateLeaseMachinery = "LeaseMachinery/updateLeaseFacility/"
                //变电
                const val PowerTransformation = "PowerTransformation/insertPowerTransformation/"
                const val deletePowerTransformation="PowerTransformation/deletePowerTransformation/{id}"
                const val updatePowerTransformation = "PowerTransformation/updatePowerTransformation/"
                //主网
                const val MajorNetwork = "MajorNetwork/insertMajorNetwork/"
                const val deleteMajorNetwork = "MajorNetwork/deleteMajorNetwork/{id}"
                const val updateMajorNetwork = "MajorNetwork/updateMajorNetwork/"
                //配网
                const val DistribuionNetwork = "DistribuionNetwork/insertDistribuionNetwork/"
                const val  deleteDistribuionNetwork = "DistribuionNetwork/deleteDistribuionNetwork/{id}"
                const val updateDistribuionNetworkDTO = "DistribuionNetwork/updateDistribuionNetworkDTO/"
                //测量设计
                const val MeasureDesign = "MeasureDesign/insertMeasureDesign/"
                const val deleteMeasureDesign = "MeasureDesign/deleteMeasureDesign/{id}"
                const val updateMeasureDesign = "MeasureDesign/updateMeasureDesign/"
                //马帮
                const val CaravanTransport = "CaravanTransport/insertCaravanTransport/"
                const val deleteCaravanTransport = "CaravanTransport/deleteCaravanTransport/{id}"
                const val updateCaravanTransport = "CaravanTransport/updateCaravanTransport/"
                //桩基
                const val PileFoundation = "PileFoundation/insertPileFoundation/"
                const val deletePileFoundation = "PileFoundation/deletePileFoundation/{id}"
                const val updatePileFoundation = "PileFoundation/updatePileFoundation/"
                //非开挖
                const val Unexcavation = "Unexcavation/saveUnexcavation/"
                const val deleteUnexcavation = "Unexcavation/deleteUnexcavation/{id}"
                const val updateUnexcavation = "Unexcavation/updateUnexcavation/"
                //试验调试
                const val TestTeam = "TestTeam/saveTestTeam/"
                const val deleteTestTeam =  "TestTeam/deleteTestTeam/{id}"
                const val updateTestTeam = "TestTeam/updateTestTeam/"
                //跨越架
                const val SpanWoodenSupprt = "SpanWoodenSupprt/saveSpanWoodenSupprt/"
                const val deleteSpanWoodenSupprt = "SpanWoodenSupprt/deleteSpanWoodenSupprt/{id}"
                const val updateSpanWoodenSupprt = "SpanWoodenSupprt/updateSpanWoodenSupprt/"
                //运维
                const val RunningMaintain = "RunningMaintain/saveRunningMaintain/"
                const val deleteRunningMaintain = "RunningMaintain/deleteRunningMaintain/{id}"
                const val updateRunningMaintain ="RunningMaintain/updateRunningMaintain/"


                //三方
                const val ThirdServices = "/Third/saveThirdServices/"
                const val deleteThirdServices = "Third/deleteThirdServices/{id}"
                const val  updateThirdServices = "Third/updateThirdServices/"
            }
        }

        class Professional {
            companion object {
                //获取去所有项目
                const val MajorDistribuionProject = "Vip/getMajorDistribuionProjectAllByVipId/{id}/{page}"
                //自检自查 查询
                const val selfInspectionQuery="SelfInspection/selectAll/{UpId}"
                //自检自查 上传文件
                const val selfInspectionUploadFile="SelfInspection/uploadFile/"
                //自检自查 下载文件
                const val selfInspectionDownloadFile="/SelfInspection/downloadFile/"
                //自检自查 修改名字
                const val selfInspectionUpdateFile="SelfInspection/updateFile/"
                //架空经济指标
                const val OverHeadAerialEconomy = "AerialEconomyIndex/selectByAerialEconomyIndexIndexKey/{aerialId}"
                //添加公共点定位
                const val AerialCommonPointLocation="AerialCommonPointLocation/insertAerialCommonPointLocation/"
                //获取杆塔参数
                const val PowerTowerParameters =
                    "PowerTowerParameter/getPowerTowerParameters/{majorDistribuionProjectId}/{pageNumber}"
                //基础浇筑
                const val AerialFoundationCasting =
                    "AerialFoundationCasting/selectAerialFoundationCasting/{towerSubitemId}"
                //更新基础浇筑_第一模块
                const val updateAerialCastingFirst = "AerialFoundationCasting/updateAerialCastingFirst/"
                //更新混凝土
                const val updateAerialCastingBeton = "AerialFoundationCasting/updateAerialCastingBeton/"
                //更新干浆砌
                const val updateAerialCastingMasonry = "AerialFoundationCasting/updateAerialCastingMasonry/"
                //更新基础防护
                const val updateAerialCastingFoundationFence =
                    "AerialFoundationCasting/updateAerialCastingFoundationFence/"
                //基础开挖
                const val AerialFoundationExcavation =
                    "AerialFoundationExcavation/selectAerialFoundationExcavation/{towerId}"
                //更新破除路面
                const val updateAerialExplodeRoad = "AerialFoundationExcavation/updateAerialExplodeRoad/"
                //更新路面切割
                const val updateAerialExcavationRoadCut = "AerialFoundationExcavation/updateAerialExcavationRoadCut/"
                //更新电杆坑开挖
                const val updateAerialPolePitExcavation = "AerialFoundationExcavation/updateAerialPolePitExcavation/"
                //更新拉线洞开挖
                const val updateAerialStayguyHoleExcavation =
                    "AerialFoundationExcavation/updateAerialStayguyHoleExcavation/"
                //更新塔坑开挖
                const val updateAerialTowerPitExcavation = "AerialFoundationExcavation/updateAerialTowerPitExcavation/"
                //更新基础开挖
                const val updateAerialFoundationExcavation =
                    "AerialFoundationExcavation/updateAerialFoundationExcavation/"
                //材料运输
                const val MaterialsTransport =
                    "AerialMaterialsTransport/selectByAerialMaterialsTransportKey/{towerSubitemId}"
                //更新材料运输
                const val updateAerialMaterialsTransport = "/AerialMaterialsTransport/updateAerialMaterialsTransport/"
                //预制件填埋
                const val PreformedUnitLandfill =
                    "AerialPreformedUnitLandfill/selectByAerialPreformedUnitLandfillKey/{towerSubitemId}"
                //更新预制件填埋
                const val updateAerialPreformedUnitLandfill =
                    "AerialPreformedUnitLandfill/updateAerialPreformedUnitLandfill/"
                //拉线制作
                const val AerialsStayguyMake = "AerialsStayguyMake/getAerialsStayguyMakeList/{id}"
                //横担安装
                const val AerialPoleArmInstall =
                    "AerialPoleArmInstall/selectByAerialPoleArmInstallKey/{towerSubitemId}"
                //更新横担安装
                const val updateAerialPoleArmInstall = "AerialPoleArmInstall/updateAerialPoleArmInstall/"
                //附件安装
                const val AerialEnclosureInstall =
                    "AerialEnclosureInstall/selectByAerialEnclosureInstallKey/{towerSubitemId}"
                //设备安装
                const val FacilityInstall = "AerialFacilityInstall/getALLAerialFacilityInstall/{towerSubitemId}"
                //户表安装
                const val AerialHouseholdInstall =
                    "AerialHouseholdInstall/selectByAerialHouseholdInstallIndexKey/{towerSubitemId}"
                //更新户表安装表计编号登记
                const val updateAerialHouseholdInstallRegistration =
                    "AerialHouseholdInstall/updateAerialHouseholdInstallRegistration/"
                //绝缘子安装
                const val AerialInsulatorInstall =
                    "AerialInsulatorInstall/selectByAerialInsulatorInstallKey/{towerSubitemId}"
                //杆塔组立
                const val AerialPoleTowerAssemblage = "AerialPoleTowerAssemblage/getAerialPoleTowerAssemblage/{id}"
                //更新杆塔组立
                const val updateAerialPoleTowerAssemblage =
                    "AerialPoleTowerAssemblage/updateAerialPoleTowerAssemblage/"
                //接地敷设
                const val AerialGroundConnectionLay =
                    "AerialGroundConnectionLay/selectByAerialGroundConnectionLayKey/{aerialId}"
                //更新接地防腐
                const val updateAerialGroundAczoiling = "AerialGroundConnectionLay/updateAerialGroundAczoiling/"
                //更新接地极制作
                const val updateAerialGroundEarthPole = "AerialGroundConnectionLay/updateAerialGroundEarthPole/"
                //更新接地沟开挖
                const val updateAerialGroundDitchExcavation =
                    "AerialGroundConnectionLay/updateAerialGroundDitchExcavation/"
                //更新接地母线敷设
                const val updateAerialGroundGeneratrixLay =
                    "AerialGroundConnectionLay/updateAerialGroundGeneratrixLay/"
                //更新引下安装
                const val updateAerialGroundDownInstall = "AerialGroundConnectionLay/updateAerialGroundDownInstall/"
                //更新接地体安装
                const val updateAerialGroundBodyInstall = "AerialGroundConnectionLay/updateAerialGroundBodyInstall/"
                //焊接制作
                const val AerialWeldMake = "AerialWeldMake/selectByAerialWeldMakeIndexKey/{towerSubitemId}"
                //带电作业
                const val AerialElectrificationJob =
                    "AerialElectrificationJob/selectByAerialElectrificationJobIndexKey/{towerSubitemId}"
                //更新带电作业
                const val updateAerialElectrificationJob = "AerialElectrificationJob/updateAerialElectrificationJob/"
                //节点参数
                const val NodeParameter = "Node/getNodeParameter/{id}/{page}"
                //节点经济指标
                const val NodeEconomy = "EconomicIndex/getEconomicIndexByPrimaryKey/{cableId}"
                //节点、通道 预制构件制作及安装
                const val NodePreformedUnitMakeInstallation =
                    "PreformedUnitMakeInstallation/getPreformedUnitMakeInstallation/{id}"
                //节点、通道 更新预制作方量
                const val updateMakeVolume = "PreformedUnitMakeInstallation/updateMakeVolume/"
                //节点、通道 更新安装方量
                const val updateInstallVolume = "PreformedUnitMakeInstallation/updateInstallVolume/"
                //节点设备安装
                const val ListFacility = "ListFacility/getALLListFacilitys/{nodeSubitemId}"
                //基础开挖
                const val NodeBasisExcavation = "BasisExcavation/getBasisExcavation/{nodeSubitemId}"
                //更新路面切割
                const val updateRoadCutting = "BasisExcavation/updateRoadCutting/"
                //更新破除路面
                const val updateBreakingRoad = "BasisExcavation/updateBreakingRoad/"
                //更新土方石开挖
                const val updateEarthRockExcavation = "BasisExcavation/updateEarthRockExcavationDTO/"
                //节点基础浇筑
                const val NodeBasisPouring = "BasisPouring/getBasisPouring/{nodeSubitemId}"
                //修改基层恢复
                const val updatePrimaryLevelRecover = "PrimaryLevelRecover/updatePrimaryLevelRecover/"
                //修改面层恢复
                const val updateSurfaceRecover = "SurfaceRecover/updateSurfaceRecover/"
                //修改砖砌体
                const val updateBrickSetting = "BrickSetting/updateBrickSetting/"
                //修改路缘石
                const val updateCurb = "Curb/updateCurb/"
                //修改装模
                const val updateLoadMold = "LoadMold/updateLoadMold/"
                //修改抹灰
                const val updatePlasterer = "Plasterer/updatePlasterer/"
                //修改混凝土
                const val updateConcretePouring = "ConcretePouring/updateConcretePouring/"
                //节点接地敷设
                const val NodeGroundingInstallation =
                    "GroundingInstallation/selectGroundingInstallationByPrimaryKey/{nodeSubitemId}"
                //修改接地沟开挖
                const val updateTrenchExcavation = "GroundingInstallation/updateTrenchExcavation/"
                //修改接地极制作及安装
                const val updateGroundingElectrodeMakeInstall =
                    "GroundingInstallation/updateGroundingElectrodeMakeInstall/"
                //修改接地母线敷设
                const val updateGroundingBusLaying = "GroundingInstallation/updateGroundingBusLaying/"
                //修改引下安装
                const val updateUnderInstallation = "GroundingInstallation/updateUnderInstallation/"
                //修改接地体安装
                const val updateGroundingInstall = "GroundingInstallation/updateGroundingInstall/"
                //修改接地体防腐
                const val updateGroundingAnticorrosion = "GroundingInstallation/updateGroundingAnticorrosion/"
                //节点 户表安装
                const val NodeHouseholdTableInstallation =
                    "HouseholdTableInstallation/selectHouseholdTableInstallationByPrimaryKey/{nodeSubitemId}"
                //节点 更新表计编号登记
                const val updateMeterIndexRegister = "HouseholdTableInstallation/updateMeterIndexRegister/"
                //节点 材料运输
                const val NodeMaterial = "Material//getMaterial/{nodeSubitemId}"
                //节点 更新材料运输
                const val updateMaterialTransportStatistics = "Material/updateMaterialTransportStatistics/"
                //获取通道参数
                const val GalleryParameter = "Gallery/getGalleryParameter/{id}/{page}"
                //通道经济指标
                const val GalleryEconomy = "EconomicIndex/getEconomicIndexByPrimaryKey/{cableId}"
                //通道 电缆头制作
                const val CableHeadMake="CableHeadMake/selectCableHeadMake/{nodeSubitemId}"
                //更新 电缆头制作
                const val updateCableHeadMake="CableHeadMake/updateCableHeadMake/"
                //通道 电缆防火
                const val CableFireroofing="CableFireroofing/seleteCableFireroofing/{nodeSubitemId}"
                //更新 电缆防火
                const val updateCableFireroofing="CableFireroofing/updateCableFireroofing/"
                //通道 电缆桥架
                const val CableBridge="CableBridge/selectCableBridge/{nodeSubitemId}"
                //更新 电缆桥架
                const val updateCableBridge="CableBridge/updateCableBridge/"
                //通道 电缆敷设
                const val CableLaying="CableLaying/selectCableLaying/{nodeSubitemId}"
                //更新 电缆桥架
                const val updateCableLaying="CableLaying/updateCableLaying/"

                //通道 电缆试验
                const val CableTest="CableTest/getCableTest/{nodeSubitemId}"
                //更新 电缆试验
                const val updateCableTest="CableTest/updateCableTest/"
                //通道 电缆配管
                const val ListCablePipe="ListCablePipe/selectListCablePipe/{nodeSubitemId}"
                //更新 电缆配管
                const val updateListCablePipe="ListCablePipe/updateListCablePipe/"
                //删除项目盘项目
                const val deleteMajorDistribuionProject = "Vip/deleteMajorDistribuionProject/{id}"

            }
        }
        class Distribution{
            companion object{
                //获取二维码
                const val QRCode="sale/getQRCode/"
                //获取个人总积分
                const val OwnSpreadIntegral="sale/getOwnIntegral"
                const val OwnReturnIntegral="sale/getOwnCashPoints"
                //获取收益（本月收益加今日收益）
                //type( day)
                const val UserSpreadIncome="sale/getIncomeByIntegral/{type}"
                const val UserReturnIncome="sale/getIncomeByCashPoints/{type}"
                //获取积分详情
                //date(二级选择器的selectContent)
                const val OwnIntegralOfRebate="sale/getOwnIntegralOfRebate/{date}/{type}"
                //获取下级会员
                //vipLevel(1 2)
                const val OwnExtendUser="sale/getOwnExtendUser/{vipLevel}"
                //获取推广详情
                //vipLevel(1 2)
                //year(一级选择器的selectContent)
                const val OwnExtendUAndI="sale/getOwnExtendUAndI/{vipLevel}/{year}"
            }
        }
        class Shopping{
            companion object{
                //人气商品列表
                const val PopularityGoods="Generalize/getPopularityGoodsInfo/"
                //新品商品列表
                const val NewGoods="Generalize/getNewGoodsInfo/"
                //人气推广列表
                const val GeneralizeGoods="Generalize/getGeneralizeGoodsInfo/"
                //
            }
        }
        class Login{
            companion object{
                const val sendCode="Jmessage/sendCode/{mobileNumber}"
                const val sendRegister="user/regist/"
                const val sendLogin="user/login"
                const val validFindCode = "Jmessage/validFindCode/{mobileNumber}/{code}"
                const val findPasswordByPhone = "userInfo/findPasswordByPhone/"
                const val  findPasswordByEmail = "userInfo/findPasswordByEmail/"
            }
        }
        class DisplayForYellowPages{
            companion object{
                //根据查询条件查询黄页的数量
                const val YellowPages="IndustryYellowPages/getIndustryYellowPagesDTOList"
                //                //根据id查询黄页详情
                const val YellowPagesDetail="IndustryYellowPages/getIndustryYellowPages/{id}"
            }
        }
        class DisplayForRequirement {
            companion object {
                //根据查询条件查询需求个人的数量
                const val RequirementPerson="RequirementPerson/getAllByQueryConditions"
                //                //根据id查询需求个人详情
                const val RequirementPersonDetail="RequirementPerson/getRequirementPerson/{id}"

                //根据查询条件查询需求团队的数量
                const val ReqiurementTeam="RequirementTeam/getRequirementTeamDTOList"
                //需求团队 变电
                const val RequirementPowerTransformation="RequirementPowerTransformation/getRequirementPowerTransformation/{id}"
                //需求团队 主网
                const val RequirementMajorNetwork="RequirementMajorNetwork/getRequirementMajorNetwork/{id}"
                //需求团队 配网
                const val RequirementDistributionNetwork="RequirementDistribuionNetwork/getRequirementDistribuionNetwork/{id}"
                //需求团队 测量设计
                const val RequirementMeasureDesign="RequirementMeasureDesign/getRequirementMeasureDesign/{id}"
                //需求团队 马帮运输
                const val RequirementCaravanTransport="RequirementCaravanTransport/getRequirementCaravanTransport/{id}"
                //需求团队 桩机服务
                const val RequirementPileFoundation="RequirementPileFoundation/getRequirementPileFoundation/{id}"
                //需求团队 非开挖拉管顶管作业
                const val RequirementUnexcavation="RequirementUnexcavation/getRequirementUnexcavation/{id}"
                //需求团队 试验调试
                const val RequirementTest="RequirementTest/getRequirementTest/{id}"
                //需求团队 跨越架
                const val RequirementSpanWoodenSupprt="RequirementSpanWoodenSupprt/getRequirementSpanWoodenSupprt/{id}"
                //需求团队 运行维护
                const val RequirementRunningMaintain="RequirementRunningMaintain/getRequirementRunningMaintain/{id}"

                //根据查询条件查询需求租赁数量
                const val RequirementLease="RequirementLease/getRequirementLeaseDTOList"
                //需求租赁 车辆租赁
                const val RequirementLeaseCar="RequirementLeaseCar/getRequirementLeaseCar/{id}"
                //需求租赁 机械租赁
                const val RequirementLeaseMachinery="RequirementLeaseMachinery/getRequirementLeaseMachinery/{id}"
                //需求租赁 设备租赁
                const val RequirementLeaseFacility="RequirementLeaseFacility/getRequirementLeaseFacility/{id}"
                //需求租赁 工器具租赁
                const val RequirementLeaseConstructionTool="RequirementLeaseConstructionTool/getRequirementLeaseConstructionTool/{id}"

                //根据查询条件查询需求三方
                const val RequirementThirdParty="RequirementThirdParty/getAllByQueryConditions"
                //需求三方
                const val RequirementThirdPartyDetail="RequirementThirdParty/getRequirementThirdParty/{id}"
            }
        }
        class DisplayForSupply{
            companion object{
                //个人劳务根据查询条件查询数量
                const val PersonalIssue="PersonalIssue/selectListPersonalIssue"
                //个人劳务根据id查询
                const val PersonalIssueDetail="PersonalIssue/selectPersonalIssue/{id}"

                //团队服务根据查询条件查询数量
                const val TeamIssue="RunningMaintain/getRunningMaintainALLDTOALL"
                //马帮运输
                const val CaravanTransport="CaravanTransport/selectCaravanTransportDTO/{id}"
                //试验调试
                const val TestTeam="TestTeam/getTestTeam/{id}"
                //跨越架
                const val SpanWoodenSupprt="SpanWoodenSupprt/getSpanWoodenSupprt/{id}"
                //运行维护
                const val RunningMaintain="RunningMaintain/getRunningMaintain/{id}"
                //非开挖
                const val Unexcavation="Unexcavation/getUnexcavation/{id}"
                //桩机服务
                const val PileFoundation="PileFoundation/selectPileFoundationDTO/{id}"
                //配网施工队
                const val DistribuionNetwork="DistribuionNetwork/selectDistribuionNetworkDTO/{id}"
                //主网施工队
                const val MajorNetwork="MajorNetwork/selectMajorNetworkDTO/{id}"
                //变电施工队
                const val PowerTransformation="PowerTransformation/selectPowerTransformationDTO/{id}"
                //测量设计
                const val MeasureDesign="MeasureDesign/selectMeasureDesignDTO/{id}"

                //租赁服务
                const val LeaseIssue="Lease/getLeaseDTOList"
                //车辆租赁
                const val LeaseCar="LeaseCar/getLeaseCarById/{id}"
                //设备租赁
                const val LeaseFacility="LeaseFacility/getLeaseFacilityById/{id}"
                //工器具租赁
                const val LeaseConstructionTool="LeaseConstructionTool/getLeaseConstructionToolById/{id}"
                //机械租赁
                const val LeaseMachinery="LeaseMachinery/getLeaseMachineryById/{id}"

                //三方
                const val ThirdIssue="Third/selectBy"
                //详情
                const val ThirdIssueDetail="Third/selectThirdServices/{id}"
            }
        }
    }
}
