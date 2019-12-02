package com.example.eletronicengineer.distributionFileSave

class Network(
    var majorNetwork:majorNetwork,//主网
    var powerTransformation:powerTransformation,//变电
    var distribuionNetwork:distribuionNetwork,//配网
    var measureDesign:measureDesign,//测量
    var provideCrewLists:List<provideCrewLists>?,
    var provideTransportMachines:List<provideTransportMachines>?,
    var constructionToolLists:List<constructionToolLists>?,
    var voltages:List<voltages>?,
    var implementationRanges:implementationRanges
)