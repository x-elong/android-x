package com.example.eletronicengineer.utils

class PayResult {
    var resultStatus = ""
    var result = ""
    var memo = ""

    constructor(rawResult:Map<String, String>){
        if(rawResult.isEmpty())
            return
        for(key in rawResult.keys){
            when(key){
                "resultStatus"->resultStatus = rawResult.getValue(key)
                "result"->result = rawResult.getValue(key)
                "memo"->memo = rawResult.getValue(key)

            }
        }
    }

    override fun toString(): String {
        return ("resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}")
    }
}