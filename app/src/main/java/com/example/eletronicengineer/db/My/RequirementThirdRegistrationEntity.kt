package com.example.eletronicengineer.db.My

import java.io.Serializable

class RequirementThirdRegistrationEntity (
    val requirementThirdLoggingCheck:RequirementThirdLoggingCheckEntity,
    val enrollThirdLists:List<EnrollThirdListsEntity>?,
    val requirementVariety:String,
    val name:String,
    val phone:String
):Serializable