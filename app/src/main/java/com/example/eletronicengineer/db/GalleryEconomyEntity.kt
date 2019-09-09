package com.example.eletronicengineer.db

class GalleryEconomyEntity (
    var id:Long,
    var manpowerDesignQuantity:Double,
    var manpowerActualQuantity:Double,
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
    var channelNumber:String,
    var nodeNumber:String,
    var soilTextureProportion:SoilTextureProportionEntity,
    var terrainProportion:TerrainProportionEntity
)