package com.example.eletronicengineer.db.NodeGroundingInstallation

import java.io.Serializable

class UnderInstallationsEntity (
    var id:Long,
    var groundingInstallationId:Long,
    var materialsName:String,
    var specificationsModel:String,
    var quantity:Double,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable