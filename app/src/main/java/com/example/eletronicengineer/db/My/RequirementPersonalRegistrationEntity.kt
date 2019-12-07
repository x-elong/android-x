package com.example.eletronicengineer.db.My

import java.io.Serializable

class RequirementPersonalRegistrationEntity (
    val personRequirementInformationCheck:RequirementPersonInformationCheckEntity,
    val enrollProvideCrewList:List<EnrollProvideCrewListEntity>?,
    val requirementVariety:String,
    val requirementMajor:String,
    val salaryStandard:String,
    val salaryUnit:String,
    val name:String,
    val phone:String
):Serializable