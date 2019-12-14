package com.example.eletronicengineer.distributionFileSave

import java.io.Serializable

class OtherLease(
    var companyCredential:SupplyLeaseConstructionTool,//公用
    var leaseList:List<leaseList>,
    var leaseConstructionTool:leaseConstructionTool,
    var leaseFacility:SupplyLeaseFacility,//设备
    var leaseMachinery:SupplyLeaseMachinery,//机械
    var issuerBelongSite:String
):Serializable