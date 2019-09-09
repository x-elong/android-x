package com.example.eletronicengineer.db

class OverHeadAerialSoilTextureProportionEntity (
    var id:Long,
    var aerialEconomyIndexId:Long,
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
    var version:String?,
    var designActualJudgement:Long
)