package com.example.eletronicengineer.model

class Constants
{
    //Request Code
    enum class RequestCode
    {
        REQUEST_PICK_IMAGE,
        REQUEST_PICK_FILE,
        REQUEST_DOWNLOAD_FILE
    }
    enum class HandlerMsg
    {
        UPLOAD_CLEARANCE_SALARY_SELECT_OK,//清工薪资
        UPLOAD_LIST_QUOTE_SELECT_OK//清单报价
    }
    enum class FragmentType
    {
        PERSONAL_GENERAL_WORKERS_TYPE,  //个人普工
        PERSONAL_SPECIAL_WORK_TYPE,  //个人特种作业
        PERSONAL_PROFESSIONAL_OPERATION_TYPE,    //个人专业操作
        PERSONAL_SURVEYOR_TYPE,  //个人测量工
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
    enum class EXCEL_TYPE
    {
        EXCEL_MEMBER,
        EXCEL_VEHICLE
    }
    enum class PAGE_TYPE
    {
        PERSONAL,
        VEHICLE
    }
    class HttpUrlPath
    {
        //需求
        class Requirement
        {
            companion object
            {
                //需求主网
                const val requirementMajorNetwork="/RequirementMajorNetwork/insertRequirementMajorNetworkAndroid"
                //马帮运输
                const val requirementCaravanTransport="/RequirementCaravanTransport/insertRequirementCaravanTransportAndroid"
                //需求配网
                const val requirementDistribuionNetwork="/RequirementDistribuionNetwork/insertRequirementDistribuionNetworkAndroid"
                //测量设计
                const val requirementMeasureDesign="/RequirementMeasureDesign/insertRequirementMeasureDesignAndroid"
                //桩基
                const val requirementPileFoundation="/RequirementPileFoundation/insertRequirementPileFoundation"
                //三方
                const val requirementThirdParty="/RequirementThirdParty/insertRequirementThirdPartyAndroid"
                //非开挖顶拉管
                const val requirementUnexcavation="/RequirementUnexcavation/insertRequirementUnexcavationAndroid"
                //试验调试
                const val requirementTest="/RequirementTest/insertRequirementTestAndroid"
                //跨越架
                const val requirementSpanWoodenSupprt="/RequirementSpanWoodenSupprt/insertRequirementSpanWoodenSupprtAndroid"
                //运行维护
                const val requirementRunningMaintain="/RequirementRunningMaintain/insertRequirementRunningMaintainAndroid"
                //变电施工
                const val requirementPowerTransformation="/RequirementPowerTransformation/insertRequirementPowerTransformationAndroid"
                //机械租赁
                const val requirementLeaseMachinery="RequirementLeaseMachinery/insertRequirementLeaseMachineryAndroid"
                //车辆租赁
                const val requirementLeaseCar="/RequirementLeaseCar/insertRequirementLeaseCarAndroid"
                //工器具租赁
                const val requirementLeaseConstructionTool="/RequirementLeaseConstructionTool/insertRequirementLeaseConstructionToolAndroid"
                //设备租赁
                const val requirementLeaseFacility="/RequirementLeaseFacility/insertRequirementLeaseFacilityAndroid"
                //excel模板下载
                const val excel="/excel/downloadexcel/{fileName}"
            }
        }
        class Provider
        {
            companion object
            {
                //主网
                const val mainPath="/MajorNetwork"
                const val saveMajorNetwork= mainPath+"/saveMajorNetwork"
                const val deleteMajorNetworkById= mainPath+"/deleteMajorNetwork/{id}"
                const val deleteMajorNetworkAll= mainPath+"/deleteMajorNetwork"
                const val updateMajorNetwork= mainPath+"/updateMajorNetwork"
                const val getAllMajorNetwork= mainPath+"/selectAllMajorNetwork"
            }
        }

    }
}