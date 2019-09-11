package com.example.eletronicengineer.db.NodeHouseholdTableInstallation

import java.io.Serializable

class MeterIndexRegistersEntity(
    var id:Long,
    var householdTableInstallationId:Long,
    var oldMeterInde:String,
    var newMeterInde:String,
    var oldPhotoPath:String?,
    var newPhotoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable