package com.example.eletronicengineer.db.CableTest

class CableTestEntity<T> (
    var id:Long,
    var nodeSubitemId:String,
    var cableTest:CableTestSubmitentity,
    var cableTestMaterialsTypes:List<T>
)