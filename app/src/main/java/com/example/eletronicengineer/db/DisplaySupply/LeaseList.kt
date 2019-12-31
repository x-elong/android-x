package com.example.eletronicengineer.db.DisplaySupply

import java.io.Serializable

class LeaseList(
    var id:String,
    var leaseServeId:String,
    var serialNumber:String,
    var type:String,
    var specificationsModels:String,
    var quantity:String,
    var unit:String,
    var remark:String,
    var foundTime:String,
    var founder:String,
    var alterTime:String,
    var alterPeople:String,
    var delFlag:String,
    var version:String
): Serializable