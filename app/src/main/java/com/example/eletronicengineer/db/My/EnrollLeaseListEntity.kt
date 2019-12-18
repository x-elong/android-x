package com.example.eletronicengineer.db.My

import java.io.Serializable

class EnrollLeaseListEntity (
    val id:String,
    val signUpId:String?,
    val listId:String?,
    val name:String,
    val specificationsModels:String,
    val quantity:String,
    val units:String,
    val hireTime:String,
    val quotationList:String?,
    val detailsExplain:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable