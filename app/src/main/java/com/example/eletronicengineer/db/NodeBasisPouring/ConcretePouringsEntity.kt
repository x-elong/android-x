package com.example.eletronicengineer.db.NodeBasisPouring

import java.io.Serializable

class ConcretePouringsEntity (
    var id:Long,
    var basisPouringId:Long,
    var pouringKind:String,
    var markNumber:String,
    var volume:Double,
    var concreteKind:String,
    var pouringWay:String,
    var steelBarBindingIn10:Double?,
    var steelBarBindingAbove10:Double?,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
):Serializable