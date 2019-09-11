package com.example.eletronicengineer.db.NodePreformedUnitMakeInstallation

class PreformedUnitMakeInstallationEntity<T1,T2> (
    var id:Long,
    var nodeSubitemId:String,
    var installVolumes:List<T1>,
    var makeVolumes:List<T2>
)