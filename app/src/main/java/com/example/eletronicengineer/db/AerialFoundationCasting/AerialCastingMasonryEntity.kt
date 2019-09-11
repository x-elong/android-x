package com.example.eletronicengineer.db.AerialFoundationCasting

import java.io.Serializable

class AerialCastingMasonryEntity (
    var id:Long,
    var aerialCastingSecondId:Long,
    var masonryShape:Long,
    var mortarProportion:Double,
    var masonryQuantity:Double,
    var photoPath:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable