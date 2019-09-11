package com.example.eletronicengineer.db.NodeBasisExcavation

class BasisExcavationEntity<T1,T2,T3> (
    var id:Long,
    var nodeSubitemId:String,
    var roadCuttings:List<T1>,
    var breakingRoads:List<T2>,
    var earthRockExcavationDTOS:List<T3>
)