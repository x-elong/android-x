package com.example.eletronicengineer.db

class AerialHouseholdInstallCommentEntity (
    var id:Long,
    var aerialHouseholdInstallId:Long,
    var addComment:String,
    var specificationsModels:String?,
    var unit:String,
    var quantity:Double,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var taskNumber:String?
)