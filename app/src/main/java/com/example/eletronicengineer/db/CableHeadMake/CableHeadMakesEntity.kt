package com.example.eletronicengineer.db.CableHeadMake

import java.io.Serializable

class CableHeadMakesEntity (
    var id:Long,
    var kind:String,
    var voltageGrade:String,
    var specificationsModel:String,
    var makeingQuantity:Long,
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