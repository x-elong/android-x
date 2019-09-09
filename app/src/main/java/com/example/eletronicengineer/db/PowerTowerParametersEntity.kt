package com.example.eletronicengineer.db

import java.io.Serializable

class PowerTowerParametersEntity (
    var id:Long,
    var majorDistribuionProjectId:Long,
    var poleNumber:String,
    var powerTowerType:String,
    var powerTowerHeight:String,
    var powerTowerAttribute:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var aerialId:String,
    var towerSubitemId:String
):Serializable
{

}