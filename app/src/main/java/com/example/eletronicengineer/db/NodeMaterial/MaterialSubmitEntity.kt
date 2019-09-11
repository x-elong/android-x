package com.example.eletronicengineer.db.NodeMaterial

import java.io.Serializable

class MaterialSubmitEntity (
    var materialTransportStatistics:MaterialTransportStatisticsEntity,
    var materials:List<MaterialsEntity>
):Serializable