package com.example.eletronicengineer.db

import java.io.Serializable

class NodeNodeEntity (
    var id:Long,
    var nodeParameterId:Long,
    var nodeIndex:String,
    var kind:String,
    var entail:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var nodeSubitemId:String
): Serializable
{

}