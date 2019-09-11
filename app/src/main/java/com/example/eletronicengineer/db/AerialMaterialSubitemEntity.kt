package com.example.eletronicengineer.db


class AerialMaterialSubitemEntity (
   var id:Long,
   var aerialMaterialsTransportId:Long,
   var materialsName:String,
   var specificationsModel:String,
   var unit:String,
   var quantity:Long,
   var simpleWeight:Double,
   var foundTime:String?,
   var founder:String?,
   var alterTime:String?,
   var alterPeople:String?,
   var delFlag:String?,
   var version:String?,
   var taskNumber:String?
)