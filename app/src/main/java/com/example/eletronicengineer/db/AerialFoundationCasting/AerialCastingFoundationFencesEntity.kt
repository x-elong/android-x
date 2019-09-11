package com.example.eletronicengineer.db.AerialFoundationCasting

import java.io.Serializable

class AerialCastingFoundationFencesEntity (
    var id:Long,
    var aerialFoundationCastingId:Long,
    var fenceType:String,
    var quantity:Double,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable