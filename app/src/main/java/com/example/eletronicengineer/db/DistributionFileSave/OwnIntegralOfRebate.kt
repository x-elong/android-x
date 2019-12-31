package com.example.eletronicengineer.db.DistributionFileSave

import java.io.Serializable


class OwnIntegralOfRebate (
    var monthIntegral:Long,
    var detailDTOList:List<OwnIntegralOfRebateDetail>
):Serializable