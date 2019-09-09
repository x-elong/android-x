package com.example.eletronicengineer.db.AerialFoundationCasting

import java.io.Serializable

class AerialCastingFirstsEntity (
    var id:Long,
    var aerialFoundationCastingId:Long,
    var addContent:String,
    var cushionType:String,
    var cushionQuantity:Double,
    var templateType:String,
    var foundationPitLocation:String,
    var mainBetonType:String,
    var mainBetonNumber:String,
    var castType:String,
    var mainBetonQuantity:Double,
    var rebarMake:Double?,
    var rebarInstall:Double?,
    var footBoltInstall:Double?,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable