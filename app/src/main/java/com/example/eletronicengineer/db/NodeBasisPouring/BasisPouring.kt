package com.example.eletronicengineer.db.NodeBasisPouring

class BasisPouring <T1,T2,T3,T4,T5,T6,T7>(
    var id:Long,
    var nodeSubitemId:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?,
    var contentNavigation:String?,
    var nameIdentification:String?,
    var brickSettings:List<T1>?,
    var concretePourings:List<T2>?,
    var curbs:List<T3>?,
    var loadMoldDTOs:List<T4>?,
    var plasterers:List<T5>?,
    var primaryLevelRecovers:List<T6>?,
    var surfaceRecovers:List<T7>?
)