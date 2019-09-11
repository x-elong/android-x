package com.example.eletronicengineer.db.NodeHouseholdTableInstallation

class HouseholdTableInstallationEntity<T1,T2> (
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
    var listMeterMaterials:List<T1>,
    var meterIndexRegisters:List<T2>
)