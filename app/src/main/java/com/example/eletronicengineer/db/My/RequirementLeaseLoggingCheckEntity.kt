package com.example.eletronicengineer.db.My

import java.io.Serializable

class RequirementLeaseLoggingCheckEntity (
    val id:String,
    val requirementLeaseMachineryId:String,
    val vipId:String?,
    val type:String,
    val comment:String?,
    val issuerDelFlag:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable