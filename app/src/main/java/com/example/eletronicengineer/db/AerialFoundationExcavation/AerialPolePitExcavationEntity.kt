package com.example.eletronicengineer.db.AerialFoundationExcavation

import java.io.Serializable

class AerialPolePitExcavationEntity (
    var id:Long,
    var aerialFoundationExcavationId:Long,
    var polePitChoice:String,
    var excavationShape:Long,
    var excavationWay:String,
    var soilProportion:String,
    var foundationDefenceWall:Long,
    var defenceWallType:String,
    var damBoard:Double,
    var damBoardType:String,
    var backfillQuantity:Double,
    var backfillWay:String,
    var flatSubgradeQuantity:Double,
    var flatSubgradeQuantityCalculate:Double,
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
    var excavationModelsSoilBackfill:String,
    var taskNumber:String?
):Serializable