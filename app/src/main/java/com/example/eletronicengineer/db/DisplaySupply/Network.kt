package com.example.eletronicengineer.distributionFileSave

import java.io.Serializable

class Network(
    val id:String,
    val teamServeId:String,
    var majorNetwork:MajorNetwork,//主网
    var powerTransformation:PowerTransformation,//变电
    var distribuionNetwork:DistribuionNetwork,//配网
    var measureDesign:MeasureDesign,//测量
    var provideCrewLists:List<ProvideCrewLists>?,
    var provideTransportMachines:List<ProvideTransportMachines>?,
    var constructionToolLists:List<ConstructionToolLists>?,
    var voltages:List<Voltages>?,
    var implementationRanges:ImplementationRanges
):Serializable