package com.example.eletronicengineer.db.My

import java.io.Serializable

class EnrollThirdListsEntity (
    val id:String,
    val signUpId:String?,
    val listId:String?,
    val projectName:String,
    val specificationsModels:String,
    val quantity:String,
    val units:String,
    val quotationList:String?,
    val detailsExplain:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable