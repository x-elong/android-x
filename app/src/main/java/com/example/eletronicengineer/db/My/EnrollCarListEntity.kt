package com.example.eletronicengineer.db.My

import java.io.Serializable

class EnrollCarListEntity (
    val id:String,
    val signUpId:String?,
    val listId:String?,
    val carNumber:String,
    val carType:String,
    val maxPassengers:String,
    val maxWeight:String?,
    val construction:String,
    val lenghtCar:String?,
    val isDriver:String,
    val isInsurance:String,
    val carPhotoPath:String,
    val sex:String,
    val age:String,
    val remark:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable