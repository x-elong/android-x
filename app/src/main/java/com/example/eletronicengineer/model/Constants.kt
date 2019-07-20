package com.example.eletronicengineer.model

class Constants
{
  //Request Code
  enum class RequestCode
  {
    REQUEST_PHOTOGRAPHY,//拍照
    REQUEST_PICK_IMAGE,//选取照片
    REQUEST_PICK_FILE,//选取文件
    REQUEST_DOWNLOAD_FILE//下载文件
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
  enum class Subitem_TYPE{


    NEW_PROJECT_DISK, //新建项目盘
    //架空
    OVERHEAD_MORE, //架空更多
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
    OVERHEAD_SELF_EXAMINATION,//自查自检
    OVERHEAD_SUPERVISION_ACCEPTANCE,//监理验收
    OVERHEAD_OWNER_ACCEPTANCE,//业主验收
    OVERHEAD_DATA_DYNAMIC,//资料动态
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

    NODE_SELF_EXAMINATION,  //自查自检
    NODE_SUPERVISION_ACCEPTANCE,//监理验收
    NODE_OWNER_ACCEPTANCE,//业主验收
    NODE_DATA_DYNAMIC,//资料动态
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

    PASSAGEWAY_SELF_EXAMINATION, //自查自检
    PASSAGEWAY_SUPERVISION_ACCEPTANCE,//监理验收
    PASSAGEWAY_OWNER_ACCEPTANCE,//业主验收
    PASSAGEWAY_DATA_DYNAMIC,//资料动态
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