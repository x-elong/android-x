package com.example.eletronicengineer.db.My

import java.io.Serializable

class RequirementThirdLoggingCheckEntity (
    val id:String,
    val requirementThirdPartyId:String?,
    val vipId:String?,
    val comment:String?,
    val companyName:String?,
    val issuerDelFlag:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
) :Serializable