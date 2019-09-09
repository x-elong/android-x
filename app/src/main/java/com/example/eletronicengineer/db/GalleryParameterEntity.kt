package com.example.eletronicengineer.db

class GalleryParameterEntity<T> (
    var id:Long,
    var majorDistribuionProjectId:Long,
    var apType:String,
    var apName:String,
    var opType:String,
    var opName:String,
    var channelNumber:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    val galleryNodeDTOList:List<T>
)