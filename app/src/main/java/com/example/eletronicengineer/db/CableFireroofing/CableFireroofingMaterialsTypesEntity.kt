package com.example.eletronicengineer.db.CableFireroofing

import java.io.Serializable

class CableFireroofingMaterialsTypesEntity (
    var id:Long,
    var cableFireroofingId:Long,
    var name:String,
    var specificationsModel:String?,
    var unit:String,
    var designQuantity:Long,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable