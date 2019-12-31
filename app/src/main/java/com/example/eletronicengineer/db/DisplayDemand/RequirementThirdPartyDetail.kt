package com.example.eletronicengineer.db.DisplayDemand

import java.io.Serializable

class RequirementThirdPartyDetail(
    var id:String,
    var vipId:String,
    var requirementThirdPartyId:String,
    var requirementType:String,
    var requirementVariety:String,
    var partnerAttribute:String,
    var fareStandard:String,
    var additionalExplain:String,
    var cooperationRegion:String,
    var qualificationUnitName:String,
    var qualificationRequirementt:String,
    var name:String,
    var phone:String,
    var foundTime:String,
    var founder:String,
    var alterTime:String,
    var alterPeople:String,
    var delFlag:String,
    var version:String,
    var photoPath:String,
    var validTime:String,
    var thirdLists:List<thirdLists>?
): Serializable