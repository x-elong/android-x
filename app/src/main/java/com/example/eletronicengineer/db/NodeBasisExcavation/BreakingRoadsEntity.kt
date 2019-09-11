package com.example.eletronicengineer.db.NodeBasisExcavation

import java.io.Serializable

class BreakingRoadsEntity (
    var id:Long,
    var basisExcavationId:Long,
    var roadForm:String,
    var thickness:Double?,
    var length:Double,
    var wide:Double,
    var area:Double,
    var artificialEarthMoving:Double?,
    var artificialTransportDistance:Double?,
    var machineryEarthMoving:Double?,
    var machineryTransportDistannce:Double?,
    var photoPath:String?,
    var taskNumber:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable