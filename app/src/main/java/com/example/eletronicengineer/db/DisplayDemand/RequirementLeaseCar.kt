package com.example.eletronicengineer.distributionFileSave

import com.example.eletronicengineer.db.DisplayDemand.RequirementCarList
import java.io.Serializable

class RequirementLeaseCar(
    var id:String,
    var vipId:String,
    var requirementTeamServeId:String,
    var requirementType:String,
    var requirementVariety:String,
    var projectName:String,
    var projectSite:String,
    var projectTime:String,
    var vehicleType:String,
    var workerExperience:String,
    var minAgeDemand:String,
    var maxAgeDemand:String,
    var roomBoardStandard:String,
    var journeyCarFare:String,
    var journeySalary:String,
    var salaryStandard:String,
    var salaryUnit:String,
    var vehicle:String,
    var additonalExplain:String,
    var foundTime:String,
    var founder:String,
    var alterTime:String,
    var alterPeople:String,
    var delFlag:String,
    var version:String,
    var validTime:String,
    var name:String,
    var phone:String,
    var requirementCarLists:List<RequirementCarList>?
    ): Serializable