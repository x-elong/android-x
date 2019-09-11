package com.example.eletronicengineer.db

import java.io.Serializable

class AerialElectrificationJobEntity (
    var id:Long,
    var addContent:String,
    var addSubitem:String,
    var specificationsModels:String,
    var quantity:Double,
    var unit:String,
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