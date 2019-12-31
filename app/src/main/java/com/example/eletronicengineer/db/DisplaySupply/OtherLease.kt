package com.example.eletronicengineer.db.DisplaySupply

import java.io.Serializable

class OtherLease(
    var companyCredential:SupplyLeaseConstructionTool,//公用
    var leaseList:List<LeaseList>,
    var leaseConstructionTool:LeaseConstructionTool,
    var leaseFacility:SupplyLeaseFacility,//设备
    var leaseMachinery:SupplyLeaseMachinery,//机械
    var issuerBelongSite:String
):Serializable