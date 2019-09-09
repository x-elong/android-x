package com.example.eletronicengineer.db

import java.io.Serializable

class AerialHouseholdInstallRegistrationEntity (
    var id:Long,
    var aerialHouseholdInstallId:Long,
    var oldTableNumber:String,
    var oldTableLogging:String?,
    var newTableNumber:String,
    var newTableLogging:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable