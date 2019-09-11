package com.example.eletronicengineer.db.NodeMaterial

import java.io.Serializable

class MaterialsEntity (
    var id:Long,
    var materialTransportStatisticsId:Long,
    var name:String,
    var specificationsModel:String?,
    var unit:String,
    var quantity:Long,
    var pieceWeight:Double,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable