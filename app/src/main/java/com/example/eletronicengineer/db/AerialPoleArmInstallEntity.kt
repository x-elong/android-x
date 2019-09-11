package com.example.eletronicengineer.db

import java.io.Serializable

class AerialPoleArmInstallEntity (
    var id:Long,
    var addContent:String,
    var voltageClass:String,
    var specificationsModels:String,
    var wireRodQuantity:String,
    var unit:String,
    var quantity:Long,
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