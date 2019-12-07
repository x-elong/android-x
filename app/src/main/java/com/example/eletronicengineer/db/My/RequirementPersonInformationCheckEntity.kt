package com.example.eletronicengineer.db.My

import java.io.Serializable

class RequirementPersonInformationCheckEntity (
    val id:String,
    val requirementPersonId:String?,
    val vipId:String?,
    val comment:String?,
    val issuerDelFlag:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable