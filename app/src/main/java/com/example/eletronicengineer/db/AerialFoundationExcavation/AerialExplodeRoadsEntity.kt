package com.example.eletronicengineer.db.AerialFoundationExcavation

import java.io.Serializable

class AerialExplodeRoadsEntity (
    var id:Long,
    var aerialFoundationExcavationId:Long,
    var roadForm:String,
    var thickness:Long,
    var length:Double,
    var width:Double,
    var aera:Double,
    var artificialEarthMoving:Double,
    var artificialTransportDistance:Double,
    var machineryEarthMoving:Double,
    var machineryTransportDistannce:Double,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    taskNumber:String
):Serializable