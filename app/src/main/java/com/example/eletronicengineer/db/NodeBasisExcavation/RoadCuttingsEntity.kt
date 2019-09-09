package com.example.eletronicengineer.db.NodeBasisExcavation

import java.io.Serializable

class RoadCuttingsEntity (
    var id:Long,
    var basisExcavationId:Long,
    var roadCutting:Double,
    var photoPath:String?,
    var taskNumber:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable