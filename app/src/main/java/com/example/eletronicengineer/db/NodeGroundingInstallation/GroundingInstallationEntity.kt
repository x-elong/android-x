package com.example.eletronicengineer.db.NodeGroundingInstallation

class GroundingInstallationEntity<T1,T2,T3,T4,T5,T6> (
    var id:Long,
    var nodeSubitemId:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var contentNavigation:String?,
    var nameIdentification:String?,
    var groundingElectrodeMakeInstalls:List<T1>?,
    var groundingBusLayings:List<T2>?,
    var underInstallations:List<T3>?,
    var trenchExcavationDTOS:List<T4>?,
    var groundingAnticorrosions:List<T5>?,
    var groundingInstalls:List<T6>?
)