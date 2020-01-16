package com.example.eletronicengineer.utils

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class IDCardValidateUtil {

    companion object{
        val ValCodeArr = arrayListOf("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" )
        val Wi = arrayListOf( 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 )
        val MINIMAL_BIRTH_DATE = Date(-2209017600000L) //身份证的最小出生日期,1900年1月1日
        val BIRTH_DATE_FORMAT="yyyyMMdd"
        val CARD_NUMBER_LENGTH = 18
        val LENGTH_ERROR="身份证长度必须为18位！"
        val NUMBER_ERROR="身份证前17位应为数字！"
        val DATE_ERROR="身份证日期验证无效！"
        val AREA_ERROR="身份证地区编码错误!"
        val CHECKCODE_ERROR="身份证最后一位校验码有误！"
        val areaCode = ArrayList<String>()
        lateinit var mContext:Context

        fun validateEffective(idCardNumber: String):String{
            if(idCardNumber.length!=CARD_NUMBER_LENGTH)
                return LENGTH_ERROR

            for (j in 0 until CARD_NUMBER_LENGTH-1){
                if(idCardNumber[j]=='X')
                    return NUMBER_ERROR
            }
            if(idCardNumber[0]=='0')
                return AREA_ERROR
            val now = Date(System.currentTimeMillis())
            val birthStr = getBirthDayPart(idCardNumber)
            if(birthStr==DATE_ERROR)
                return birthStr
            val birth = SimpleDateFormat(BIRTH_DATE_FORMAT).parse(birthStr)
            if(birth.after(now))
                return DATE_ERROR
//            if(areaCode.isEmpty())
//                getAreaCode()
//            if(areaCode.indexOf(idCardNumber.substring(0,6))<0)
//                return AREA_ERROR
//            Log.i("areaCode", areaCode[areaCode.indexOf(idCardNumber.substring(0,6))])
//            if(calculateVerifyCode(idCardNumber)!=idCardNumber[CARD_NUMBER_LENGTH-1].toString())
//                return CHECKCODE_ERROR
            return "TRUE"
        }

        private fun calculateVerifyCode(cardNumber:String):String{
            var sum = 0
            for (j in 0 until CARD_NUMBER_LENGTH-1){
                sum += cardNumber[j].toInt() * Wi[j]
            }
            Log.i("mod ValCode", "${sum%11},${ValCodeArr[sum%11]}")
            return ValCodeArr[sum%11]
        }
        /**
         * @得到地区码
         */
        private fun getAreaCode(){
            val resultBuilder= StringBuilder()
            val bf= BufferedReader(InputStreamReader(mContext.assets.open("areas.json")))
            try {
                var line=bf.readLine()
                while (line!=null)
                {
                    resultBuilder.append(line)
                    line=bf.readLine()
                }
            }
            catch (io: IOException)
            {
                io.printStackTrace()
            }
            val jsonArr = JSONArray(resultBuilder.toString())
            for (j in 0 until jsonArr.length())
                areaCode.add(jsonArr.getJSONObject(j).getString("code"))
        }
        /**
         * @得到身份证日期
         */
        private fun getBirthDayPart(idCardNumber: String):String{
            val birth = idCardNumber.substring(6,14)
            val year = birth.substring(0,4).toInt()
            val month = birth.substring(4,6).toInt()
            val day = birth.substring(6).toInt()
            Log.i("year|month|day","${year}${month}${day}")
            if(year<1900)
                return DATE_ERROR
            if(month<=0 || month >12)
                return DATE_ERROR
            val c= Calendar.getInstance()
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH,month-1)
            if(day<=0 || day>c.getActualMaximum(Calendar.DAY_OF_MONTH))
                return DATE_ERROR
            return birth
        }
    }
}