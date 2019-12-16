package com.example.eletronicengineer.distributionFileSave

import java.io.Serializable

class SupplyThirdParty(
    var id:String,
    var serveType:String,
    var companyCredential:CompanyCredential,
    var businessScope:String,
    var validTime:String,
    var thirdServicesContractPath:String,
    var cooperationObject:String,
    var cooperationAreas:String,
    var companyName:String,
    var companySite:String,
    var legalPersonName:String,
    var qualificationCondition:String,
    var issuerBelongSite:String,
    var contact:String,
    var contactPhone:String
):Serializable