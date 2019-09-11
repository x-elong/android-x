package com.example.eletronicengineer.db.NodeBasisPouring

import java.io.Serializable

class BrickSettingsEntity (
    var id:Long,
    var basisPouringId:Long,
    var model:String,
    var grayingLabel:String,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable