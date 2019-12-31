package com.example.eletronicengineer.db.DisplayDemand

import java.io.Serializable

class requirementPowerTransformationSalary(
    var id:String,
    var requirmentTeamServeId:String,
    var positionType:String,
    var needPeopleNumber:String,
    var salaryStandard:String,
    var personCertificate:String,
    var remark:String,
    var foundTime:String,
    var founder:String,
    var alterTime:String,
    var alterPeople:String,
    var delFlag:String,
    var version:String
)  : Serializable