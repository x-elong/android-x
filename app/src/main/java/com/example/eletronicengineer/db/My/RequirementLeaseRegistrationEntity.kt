package com.example.eletronicengineer.db.My

import java.io.Serializable

class RequirementLeaseRegistrationEntity (
    val leaseLoggingCheck:RequirementLeaseLoggingCheckEntity,
    val enrollRequirementLeaseList:List<EnrollLeaseListEntity>?,
    val enrollCar:List<EnrollCarListEntity>?,
    val vehicleType:String?,
    val name:String,
    val phone:String
):Serializable