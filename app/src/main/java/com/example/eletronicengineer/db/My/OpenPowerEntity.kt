package com.example.eletronicengineer.db.My

import java.io.Serializable

class OpenPowerEntity(
    val id:String?,
    val userId:String?,
    val goodsPowerName:String,
    val failureTime:Long?
) :Serializable