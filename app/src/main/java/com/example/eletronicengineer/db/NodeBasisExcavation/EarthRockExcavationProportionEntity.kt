package com.example.eletronicengineer.db.NodeBasisExcavation

import java.io.Serializable

class EarthRockExcavationProportionEntity (
    var id:Long,
    var earthRockExcavationId:Long,
    var commonSoil:Double,
    var hardSoil:Double,
    var looseSand:Double,
    var slitQuicksand:Double,
    var tjaele:Double,
    var rock:Double,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable