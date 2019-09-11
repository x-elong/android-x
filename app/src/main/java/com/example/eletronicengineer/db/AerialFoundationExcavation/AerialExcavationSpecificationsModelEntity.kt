package com.example.eletronicengineer.db.AerialFoundationExcavation

import java.io.Serializable

class AerialExcavationSpecificationsModelEntity (
    var id:Long,
    var specificationsModels:String,
    var quantity:Double,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var excavationModelsSoilBackfill:String
)