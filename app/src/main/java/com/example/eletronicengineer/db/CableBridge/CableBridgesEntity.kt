package com.example.eletronicengineer.db.CableBridge

import java.io.Serializable

class CableBridgesEntity (
    var id:Long,
    var kind:String,
    var model:String,
    var layLength:Double,
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