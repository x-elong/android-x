package com.example.eletronicengineer.db.AerialFoundationCasting

class AerialFoundationCastingEntity <T1,T2,T3>(
    var aerialFoundationCasting:AerialFoundationCastingSubmitEntity,
    var aerialCastingFirsts:List<T1>,
    var aerialCastingSecondDTOS:List<T2>,
    var aerialCastingFoundationFences:List<T3>
)