package com.example.eletronicengineer.db.NodeHouseholdTableInstallation


class MeterMaterialsEntity (
    var id:Long,
    var householdTableInstallationId:Long,
    var materialName:String,
    var specificationsModel:String,
    var unit:String,
    var designQuantity:Double,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
)