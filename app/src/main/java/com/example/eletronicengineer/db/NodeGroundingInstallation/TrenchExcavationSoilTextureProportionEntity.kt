package com.example.eletronicengineer.db.NodeGroundingInstallation

import java.io.Serializable

class TrenchExcavationSoilTextureProportionEntity(
    var id:Long,
    var trenchExcavationId:Long,
    var ordinarySoil:Double,
    var sanSoil:Double,
    var looseSand:Double,
    var muddyWaterHole:Double,
    var waterHole:Double,
    var driftSandHole:Double,
    var drySandHole:Double,
    var dynamiteRock:Double,
    var manuRock:Double,
    var kind:Long,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable