package com.example.eletronicengineer.db

import java.io.Serializable

class AerialPoleTowerAssemblageEntity (
    var id:Long,
    var materialsName:String,
    var unit:String,
    var designQuantity:Double,
    var comment:String,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var towerSubitemId:String,
    var nameIdentification:String?,
    var contentNavigation:String
):Serializable