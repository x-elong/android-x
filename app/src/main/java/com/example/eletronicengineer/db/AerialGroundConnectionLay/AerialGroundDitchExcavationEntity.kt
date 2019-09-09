package com.example.eletronicengineer.db.AerialGroundConnectionLay

import java.io.Serializable

class AerialGroundDitchExcavationEntity (
    var id:Long,
    var aerialGroundConnectionLayId:Long,
    var lengthD:Double,
    var widthW:Double,
    var heightH:Double,
    var excavationWay:Long,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?,
    var aerialGroundSoilProportion:AerialGroundSoilProportionEntity
):Serializable