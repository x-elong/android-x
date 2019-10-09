package com.example.eletronicengineer.db.My

import java.io.Serializable

class UrgentPeoplesEntity(
    var id: String?,
    var vipId: String?,
    var urgentPeopleId: String?,
    var urgentPeopleName: String,
    var phone: String,
    var relation: String,
    var address: String,
    var additonalExplain: String?,
    var foundTime: String?,
    var founder: String?,
    var alterTime: String?,
    var alterPeople: String?,
    var delFlag: String?,
    var version: String?
) : Serializable