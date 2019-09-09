package com.example.eletronicengineer.db.ListCablePipe

import java.io.Serializable

class ListCablePipesEntity (
    var id:Long,
    var kind:String,
    var pipeKind:String,
    var specificationsModel:String,
    var haploporeLength:Double,
    var holeCount:Long,
    var concretePack:Double?,
    var length:Double?,
    var wide:Double?,
    var deep:Double?,
    var photoPath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var nodeSubitemId:String?,
    var nameIdentification:String
):Serializable