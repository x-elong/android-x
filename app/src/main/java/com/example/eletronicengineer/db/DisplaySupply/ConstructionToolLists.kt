package com.example.eletronicengineer.db.DisplaySupply

import java.io.Serializable

class ConstructionToolLists(
    var id:String,
    var teamServeId:String,
    var serialNumber:String,
    var category:String,
    var type:String,
    var specificationsModel:String,
    var quantity:String,
    var unit:String,
    var foundTime:String,
    var founder:String,
    var alterTime:String,
    var alterPeople:String,
    var delFlag:String,
    var version:String,
    var remark:String
): Serializable