package com.example.eletronicengineer.db.My

import java.io.Serializable

class RequirementTeamRegistrationEntity (
    val requirementTeamLoggingCheck:RequirementTeamLoggingCheckEntity,
    val enrollProvideCrewLists:List<EnrollProvideCrewListEntity>?,
    val enrollCars:List<EnrollCarListEntity>?,
    val enrollMachineries:List<EnrollMachineriesEntity>?,
    val name:String,
    val phone:String
):Serializable