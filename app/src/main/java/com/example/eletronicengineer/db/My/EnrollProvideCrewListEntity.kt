package com.example.eletronicengineer.db.My

import java.io.Serializable

class EnrollProvideCrewListEntity(
    val id:String?,
    val signUpId:String?,
    val listId:String?,
    val positionType:String,
    val name:String,
    val sex:String,
    val age:String,
    val workExperience:String,
    val remark:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable