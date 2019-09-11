package com.example.eletronicengineer.db.AerialFoundationExcavation

class AerialFoundationExcavationsEntity<T1,T2,T3,T4,T5> (
    var id:Long,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var towerSubitemId:String,
    var nameIdentification:String?,
    var contentNavigation:String?,
    var aerialExcavationRoadCuts:List<T1>,
    var aerialExplodeRoads:List<T2>,
    var aerialPolePitExcavationDTOS:List<T3>,
    var aerialStayguyHoleExcavationDTOS:List<T4>,
    var aerialTowerPitExcavationDTOS:List<T5>
)