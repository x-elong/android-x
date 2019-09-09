package com.example.eletronicengineer.db.AerialFoundationCasting

import java.io.Serializable

class AerialCastingBetonEntity (
    var id:Long,
    var aerialCastingSecondId:Long,
    var templateType:String,
    var betonType:String,
    var betonNumber:String,
    var pourType:String,
    var betonQuantity:Double,
    var rebarMake:Double?,
    var rebarInstall:Double?,
    var hangingNet:Double?,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable