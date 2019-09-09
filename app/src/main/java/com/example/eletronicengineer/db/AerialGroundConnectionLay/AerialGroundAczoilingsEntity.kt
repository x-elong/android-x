package com.example.eletronicengineer.db.AerialGroundConnectionLay

import java.io.Serializable

class AerialGroundAczoilingsEntity (
    var id:Long,
    var aerialGroundConnectionLayId:Long,
    var specificationsModels:String,
    var quantity:Double,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable