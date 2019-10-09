package com.example.eletronicengineer.db.My

import java.io.Serializable

class HomeChildrensEntity (
    var id:String?,
    var vipId:String?,
    var childrenName:String,
    var childrenSex:Long,
    var childrenBirthDate:Long,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable