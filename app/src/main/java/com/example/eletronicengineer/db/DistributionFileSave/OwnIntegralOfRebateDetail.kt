package com.example.eletronicengineer.db.DistributionFileSave

import java.io.Serializable


class OwnIntegralOfRebateDetail(
    var eventName:String,
    var userLevel:String,
    var identification:String,
    var foundTime:Long,
    var addIntegral:Double
):Serializable