package com.example.eletronicengineer.db.My

import java.io.Serializable

class EnrollMachineriesEntity (
    val id:String,
    val signUpId:String?,
    val listId:String?,
    val name:String,
    val specification:String,
    val quantity:String,
    val units:String,
    val details:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable