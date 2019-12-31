package com.example.eletronicengineer.db.DisplaySupply

import java.io.Serializable

class SupplyUnexcavation(
    var id:String,
    var vipId:String,
    var name:String,
    var isCar:String,
    var isConstructionTool:String,
    var foundTime:String,
    var founder:String,
    var alterTime:String,
    var alterPeople:String,
    var delFlag:String,
    var version:String,
    var teamServeId:String,
    var validTime:String,
    var issuerBelongSite:String,
    var issuerName:String,
    var phone:String,
    var provideCrewLists:List<ProvideCrewLists>,
    var provideTransportMachines:List<ProvideTransportMachines>,
    var constructionToolLists:List<ConstructionToolLists>
):Serializable