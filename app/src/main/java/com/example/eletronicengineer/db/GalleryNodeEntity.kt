package com.example.eletronicengineer.db

import java.io.Serializable

class GalleryNodeEntity(
    var id:Long,
    var galleryParameterId:Long,
    var galleryIndex:String,
    var snodeId:Long,
    var snodeIndex:String,
    var bnodeId:String,
    var bnodeIndex:String,
    var kind:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var nodeSubitemId:String
):Serializable
{

}