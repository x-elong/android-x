package com.example.eletronicengineer.db.NodePreformedUnitMakeInstallation

import java.io.Serializable

class MakeVolumesEntity(
    var id:Long,
    var preformedUnitMakeInstallationId:Long,
    var name :String,
    var componentModel:String,
    var rebarModel:String,
    var rebarWeight:Double,
    var preBuriedIron:Double,
    var photoPath:String?,
    var taskNumber:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable