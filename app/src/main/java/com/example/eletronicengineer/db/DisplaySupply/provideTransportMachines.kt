package com.example.eletronicengineer.distributionFileSave

import java.io.Serializable

class provideTransportMachines(
    var id:String,
    var teamServeId:String,
    var carNumber:String,
    var carType:String,
    var maxPassengers:String,
    var maxWeight:String,
    var construction:String,
    var lenghtCar:String,
    var isDriver:String,
    var isInsurance:String,
    var carPhotoPath:String,
    var foundTime:String,
    var founder:String,
    var alterTime:String,
    var alterPeople:String,
    var delFlag:String,
    var version:String
): Serializable