package com.example.eletronicengineer.db.NodeBasisExcavation

import java.io.Serializable


class BackfillProportionEntity(
    var id:Long,
    var earthRockExcavationId:Long,
    var soil:Double,
    var sand:Double,
    var mountainFlour:Double,
    var macadam:Double,
    var dust:Double,
    var flag:Double,
    var beton:Double,
    var other:Double,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable