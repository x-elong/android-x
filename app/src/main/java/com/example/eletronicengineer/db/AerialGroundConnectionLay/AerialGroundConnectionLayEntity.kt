package com.example.eletronicengineer.db.AerialGroundConnectionLay

class AerialGroundConnectionLayEntity<T1,T2,T3,T4,T5,T6> (
    var id:Long,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var towerSubitemId:String,
    var contentNavigation:String?,
    var nameIdentification:String?,
    var aerialGroundAczoilings:List<T1>,
    var aerialGroundBodyInstalls:List<T2>,
    var aerialGroundDownInstalls:List<T3>,
    var aerialGroundEarthPoles:List<T4>,
    var aerialGroundGeneratrixLays:List<T5>,
    var aerialGroundDitchExcavationDTOS:List<T6>
)