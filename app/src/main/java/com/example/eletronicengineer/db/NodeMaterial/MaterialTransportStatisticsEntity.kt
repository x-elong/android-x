package com.example.eletronicengineer.db.NodeMaterial

import java.io.Serializable

class MaterialTransportStatisticsEntity (
    var id:Long,
    var aggregate:Double,
    var kind:String,
    var photoPath:String?,
    var contentNavigation:String?,
    var nameIdentification:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var nodeSubitemId:String?
):Serializable