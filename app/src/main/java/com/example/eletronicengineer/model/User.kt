package com.example.eletronicengineer.model

import com.bin.david.form.annotation.SmartColumn

class User
{
    @SmartColumn(id=1,name = "姓名")
    lateinit var name:String
    @SmartColumn(id=2,name = "年龄")
    lateinit var age:String
    @SmartColumn(id=3,name = "更新时间")
    lateinit var time:String
    @SmartColumn(id=4,name = "头像")
    lateinit var portrait:String

    constructor(name: String, age: String, time: String, portrait: String) {
        this.name = name
        this.age = age
        this.time = time
        this.portrait = portrait
    }
}