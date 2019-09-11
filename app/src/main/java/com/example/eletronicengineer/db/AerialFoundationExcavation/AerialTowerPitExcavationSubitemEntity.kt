package com.example.eletronicengineer.db.AerialFoundationExcavation

import java.io.Serializable

class AerialTowerPitExcavationSubitemEntity (
    var aerialTowerPitExcavation:AerialPolePitExcavationEntity,
    var aerialExcavationSoilProportion:AerialExcavationSoilProportionEntity,
    var aerialExcavationSpecificationsModels:List<AerialExcavationSpecificationsModelEntity>,
    var aerialPoleBackfillProportion:AerialPoleBackfillProportionEntity
):Serializable