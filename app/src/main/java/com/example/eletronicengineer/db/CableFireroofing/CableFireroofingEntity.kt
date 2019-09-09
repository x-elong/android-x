package com.example.eletronicengineer.db.CableFireroofing

import java.io.Serializable

class CableFireroofingEntity <T>(
    var id:Long?,
    var nodeSubitemId:String?,
    var cableFireroofing:CableFireroofingSubmitEntity,
    var cableFireroofingMaterialsTypes:List<T>
):Serializable