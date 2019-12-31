package com.example.eletronicengineer.db.My

import java.io.Serializable

class UserEntity (
    var vipLevel:String,
    var user:UserSubitemEntity,
    var bankCards:List<BankCardsEntity>?,
    var educationBackgrounds:List<EducationBackgroundsEntity>?,
    var certificates:List<CertificatesEntity>?,
    var homeChildrens:List<HomeChildrensEntity>?,
    var urgentPeoples:List<UrgentPeoplesEntity>?
):Serializable
/*
    性别
 1 男 0 女 LONG

    血型
 1 2 3 4(INT)
 A B AB O

    婚姻状态

 */