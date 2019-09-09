package com.example.eletronicengineer.db.CableLaying

import java.io.Serializable

class CableLayingsEntity (
    var id:Long,
    var kind:String,
    var voltageGrade:String,
    var specificationsModel:String,
    var layingLength:Double,
    var loopQuantity:Long,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var nodeSubitemId:String?,
    var nameIdentification:String?
):Serializable