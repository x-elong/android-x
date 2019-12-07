package com.example.eletronicengineer.db.DisplayDemand

import java.io.Serializable

class RequirementMembersList(
    val id: String,
    var requirementTeamServeId: String,
    var positionType: String,
    var needPeopleNumber: String,
    var salaryStandard: String,
    var personCertificate: String,
    var haveCertificate: String,
    var remark: String

) : Serializable