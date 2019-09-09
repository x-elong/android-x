package com.example.eletronicengineer.db

class NodeParameterEntity <T> (
    var id:Long,
    var majorDistribuionProjectId:Long,
    var apType:String,
    var apName:String,
    var opType:String,
    var opName:String,
    var nodeNumber:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    val nodeDTOList:List<T>
)