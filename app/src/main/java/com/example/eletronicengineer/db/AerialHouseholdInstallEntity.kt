package com.example.eletronicengineer.db

class AerialHouseholdInstallEntity <T1,T2>(
    var id:Long,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var towerSubitemId:String,
    var nameIdentification:String?,
    var aerialHouseholdInstallCommentList:List<T1>,
    var aerialHouseholdInstallRegistrationList:List<T2>
)