package com.example.eletronicengineer.model

import com.bin.david.form.annotation.SmartColumn

class User
{
  lateinit var key1: String
  lateinit var key2: String
  lateinit var key3: String
  lateinit var key4: String
  lateinit var key5: String
  @SmartColumn(id=1,name = "姓名")
  lateinit var name:String
  @SmartColumn(id=2,name = "性别")
  lateinit var sex:String
  @SmartColumn(id=3,name = "年龄")
  lateinit var age:String
  @SmartColumn(id=4,name = "工种")
  lateinit var type:String
  @SmartColumn(id=5,name = "工作经验")
  lateinit var workTime:String
  @SmartColumn(id=6,name = "薪资标准")
  lateinit var money:String
  @SmartColumn(id=7,name = "备注")
  lateinit var hint:String
  constructor(key1:String,key2:String,key3:String,key4:String,key5:String)
  {

  }

  constructor(name: String, sex: String, age: String, type: String, workTime: String,money: String, hint: String) {
    this.name = name
    this.sex = sex
    this.age = age
    this.type = type
    this.workTime = workTime
    this.money = money
    this.hint = hint
  }

}