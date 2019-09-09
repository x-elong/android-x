package com.example.eletronicengineer.db.AerialGroundConnectionLay

import java.io.Serializable

class AerialGroundSoilProportionEntity (
    var id:Long,
    var aerialGroundDitchExcavationId:Long,
    var ordinarySoil:Double,
    var sanSoil:Double,
    var looseSand:Double,
    var muddyWaterHole:Double,
    var waterHole:Double,
    var driftSandHole:Double,
    var drySandHole:Double,
    var dynamiteRock:Double,
    var manuRock:Double,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable