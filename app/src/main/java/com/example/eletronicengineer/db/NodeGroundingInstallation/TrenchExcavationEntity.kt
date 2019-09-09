package com.example.eletronicengineer.db.NodeGroundingInstallation

import java.io.Serializable

class TrenchExcavationEntity (
    var id:Long,
    var groundingInstallationId:Long,
    var length:Double,
    var wide:Double,
    var deep:Double,
    var way:Long,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?,
    var trenchExcavationSoilTextureProportion:TrenchExcavationSoilTextureProportionEntity
):Serializable