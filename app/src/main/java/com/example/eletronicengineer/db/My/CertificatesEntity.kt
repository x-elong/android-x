package com.example.eletronicengineer.db.My

import java.io.Serializable

class CertificatesEntity (
    var certificateId:String?,
    var vipId:String?,
    var name:String?,
    var certificatePath:String?,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable