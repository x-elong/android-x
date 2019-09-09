package com.example.eletronicengineer.db.NodeBasisExcavation

import java.io.Serializable

class EarthRockExcavationSubmitEntity (
    var id:Long,
    var basisExcavationId:Long,
    var excavationForm:String,
    var length:Double,
    var wide:Double,
    var deep:Double,
    var excavationVolume:Double,
    var flatBasisVolume:Double?,
    var artificialSoil:Double?,
    var manuTransport:Double?,
    var mechanicalSoil:Double?,
    var mechanicalTransport:Double?,
    var backfillVolume:Double?,
    var backfillForm:String?,
    var breastBoard:Double?,
    var photoPath:String?,
    var taskNumber:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable