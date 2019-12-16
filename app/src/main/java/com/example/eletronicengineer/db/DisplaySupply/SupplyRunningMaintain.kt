package com.example.eletronicengineer.distributionFileSave

import java.io.Serializable

class SupplyRunningMaintain(
    var id:String,
    var vipId:String,
    var name:String,
    var isConstructionTool:String,
    var isCar:String,
    var workTerritory:String,
    var foundTime:String,
    var founder:String,
    var alterTime:String,
    var alterPeople:String,
    var delFlag:String,
    var version:String,
    var teamServeId:String,
    var validTime:String,
    var issuerBelongSite:String,
    var provideCrewLists:List<ProvideCrewLists>?,
    var provideTransportMachines:List<ProvideTransportMachines>?,
    var constructionToolLists:List<ConstructionToolLists>?,
    var voltages:List<Voltages>?,
    var implementationRanges:String
):Serializable