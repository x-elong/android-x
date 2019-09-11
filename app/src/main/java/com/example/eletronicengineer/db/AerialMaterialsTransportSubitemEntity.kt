package com.example.eletronicengineer.db

import java.io.Serializable

class AerialMaterialsTransportSubitemEntity (
    var id:Long,
    var content:String,
    var totalWeight:Double,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var towerSubitemId:String,
    var nameIdentification:String
):Serializable