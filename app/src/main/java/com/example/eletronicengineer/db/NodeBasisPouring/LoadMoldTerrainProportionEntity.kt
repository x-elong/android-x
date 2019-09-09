package com.example.eletronicengineer.db.NodeBasisPouring

import java.io.Serializable

class LoadMoldTerrainProportionEntity (
    var id:Long,
    var loadMoldId:Long,
    var flat:Double,
    var hill:Double,
    var generalMountainsRegion:Double,
    var mire:Double,
    var desert:Double,
    var hignMountain:Double,
    var mountainsRegion:Double,
    var riveNetwork:Double,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable