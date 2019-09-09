package com.example.eletronicengineer.db

import java.io.Serializable

class PreformedUnitLandFillEntity (
    var id:Long,
    var content:String,
    var specificationsModels:String,
    var quantity:Int,
    var quantityUnit:String,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var contentNavigation:String?,
    var towerSubitemId:String,
    var nameIdentification:String
):Serializable