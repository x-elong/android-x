package com.example.eletronicengineer.db

class OverHeadAerialEconomyEntity<T1,T2> (
    var id:Long,
    var manpowerDesignQuantity:Double,
    var manpowerActualQuantity:Double,
    var tractorsDesignQuantity:Double,
    var tractorsActualQuantity:Double,
    var carDesignQuantity:Double,
    var carActualQuantity:Double,
    var shipDesignQuantity:Double,
    var shipActualQuantity:Double,
    var ropewayDesignQuantity:Double,
    var ropewayActualQuantity:Double,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var crossId:String,
    var aerialId:String,
    var aerialSoilTextureProportion:OverHeadAerialSoilTextureProportionEntity,
    var aerialTerrainProportion:OverHeadAerialTerrainProportionEntity,
    var aerialTwentyKvTypes:List<T1>,
    var aerialThirtyfiveKvTypes:List<T2>
)