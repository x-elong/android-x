package com.example.eletronicengineer.lg

class resources<T> (
    var id: String,
    var menuNo: String,
    var parentMenuNo: String,
    var menuName: String,
    var menuAliasName: String,
    var menuType: Int,
    var seq: Int,
    var url: String,
    var action: String,
    var description: String,
    var iconStyle: String,
    var iconColor: String,
    var visiable: Int,
    var terminalType: String,
    var children: List<T>
)