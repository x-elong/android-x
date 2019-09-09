package com.example.eletronicengineer.db.AerialFoundationExcavation

import java.io.Serializable

class AerialExcavationRoadCutsEntity (
    var id:Long,
    var aerialFoundationExcavationId:Long,
    var roadCutLength:Double,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable