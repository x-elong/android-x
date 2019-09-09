package com.example.eletronicengineer.db.NodeBasisPouring

import java.io.Serializable

class SurfaceRecoversEntity (
    var id:Long,
    var basisPouringId:Long,
    var kind:String,
    var constructForm:String,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable