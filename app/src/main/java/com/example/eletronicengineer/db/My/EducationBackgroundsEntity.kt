package com.example.eletronicengineer.db.My

import java.io.Serializable

class EducationBackgroundsEntity (
    var id:String?,
    var vipId:String?,
    var educationBackground:String,
    var graduationAcademy:String,
    var graduationTime:Long,
    var major:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable