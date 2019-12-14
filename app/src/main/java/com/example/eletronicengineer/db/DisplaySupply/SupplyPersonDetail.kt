package com.example.eletronicengineer.distributionFileSave

import java.io.Serializable

class SupplyPersonDetail(
    var id:String,
    var certificatePath:String,
    var issuerWorkType:String,
    var issuerWorkerKind:String,
    var workExperience:String,
    var workMoney:String,
    var salaryUnit:String,
    var validTime:String,
    var remark:String,
    var issuerBelongSite:String,
    var contactPhone:String,
    var contact:String,
    var sex:String,
    var age:String
):Serializable