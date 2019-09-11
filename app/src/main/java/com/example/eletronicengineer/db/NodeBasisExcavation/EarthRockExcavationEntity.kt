package com.example.eletronicengineer.db.NodeBasisExcavation

import java.io.Serializable

class EarthRockExcavationEntity(
    var id:Long?,
    var earthRockExcavation:EarthRockExcavationSubmitEntity,
    var backfillProportion:BackfillProportionEntity,
    var earthRockExcavationProportion:EarthRockExcavationProportionEntity
):Serializable