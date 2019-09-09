package com.example.eletronicengineer.db

import java.io.Serializable

class FacilityInstallEntity (
    var id:Long,
    var name:String,
    var specificationsModels:String,
    var unit:String,
    var quantity:Long,
    var comment:String,
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