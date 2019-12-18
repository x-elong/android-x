package com.example.eletronicengineer.distributionFileSave

import java.io.Serializable

class SupplyLeaseCar(
    var id:String,
    var vipId:String,
    var money:String,
    var salaryUnit:String,
    var validTime:String,
    var contact:String,
    var contactPhone:String,
    var issuerBelongSite:String,
    var site:String,
    var comment:String,
    var carTable:ProvideTransportMachines,
    var variety:String
):Serializable