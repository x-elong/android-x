package com.example.eletronicengineer.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyVipActivity
import com.example.eletronicengineer.activity.ProfessionalActivity
import com.example.eletronicengineer.activity.VipActivity
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.db.MajorDistribuionProjectEntity
import com.example.eletronicengineer.db.My.*
import com.example.eletronicengineer.fragment.my.*
import com.example.eletronicengineer.fragment.projectdisk.ProjectDiskFragment
import com.example.eletronicengineer.fragment.projectdisk.ProjectMoreFragment
import com.example.eletronicengineer.fragment.sdf.ImageFragment
import com.example.eletronicengineer.fragment.sdf.SupplyFragment
import com.example.eletronicengineer.fragment.sdf.UpIdCardFragment
import com.example.eletronicengineer.fragment.sdf.UploadPhoneFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.*
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getProjects
import com.example.eletronicengineer.utils.startSendMessage
import com.example.eletronicengineer.utils.startSendMultiPartMessage
import com.example.eletronicengineer.utils.uploadImage
import com.example.eletronicengineer.utils.putSimpleMessage
import com.example.eletronicengineer.wxapi.WXPayEntryActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_soil_ratio.view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.Serializable
import java.math.BigDecimal
import java.net.URLEncoder
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NetworkAdapter {
    //供
    class Provider {
        lateinit var mData: List<MultiStyleItem>
        lateinit var context: Context
        lateinit var jsonObject: JSONObject
        constructor(mData: List<MultiStyleItem>, context: Context) {
            this.mData = mData
            this.context = context
        }
        constructor(){}
        fun generateJsonArray(data: List<MultiStyleItem>):JSONArray
        {
            val array=JSONArray()
            for (i in data)
            {
                if(i.options!=MultiStyleItem.Options.BLANK&& i.necessary==true)
                    array.put(generateJsonObject(i.itemMultiStyleItem))
            }
            return array
        }
        fun generateJsonObject(mData: List<MultiStyleItem>):JSONObject
        {
            val jsonObject = JSONObject()
            for (i in mData) {
                when (i.sendFormat) {
                    "Long" -> {
                        jsonObject.put(i.key, parseToLong(i).toString())
                    }
                    "Double" ->{
                        jsonObject.put(i.key, parseToDouble(i).toString())
                    }
                    "String" -> {
                        jsonObject.put(i.key, parseToString(i))
                    }
                    "String[]" -> {
                        val keyList = i.key.split(" ")
                        val valueList = parseToStringArray(i)
                        for (j in 0 until valueList.size) {
                            jsonObject.put(keyList[j], valueList[j])
                        }
                    }
                    "Int" -> {
                        jsonObject.put(i.key, parseToInt(i).toString())
                    }
                    "Boolean" -> {
                        jsonObject.put(i.key, parseToBoolean(i))
                    }
                    "Double String" -> {
                        val doubleAlsoString = parseToDoubleAndString(i)
                        val keys = i.key.split(" ")
                        jsonObject.put(keys[0], doubleAlsoString.first)
                        jsonObject.put(keys[1], doubleAlsoString.second)
                    }
                    "Long String" -> {
                        val longAlsoString = parseToLongAndString(i)
                        val keys = i.key.split(" ")
                            jsonObject.put(keys[0], longAlsoString.first)
                            jsonObject.put(keys[1], longAlsoString.second)
                    }
                    "JsonArray"->
                    {
                        jsonObject.put(i.key,generateJsonArray(i.itemMultiStyleItem))
                    }
                }
            }
            return jsonObject
        }
        fun generateJsonRequestBody(json:JSONObject):Observable<RequestBody> {
          return Observable.create<RequestBody> {
                val jsonObject = json
                for (i in mData) {
                    when (i.sendFormat) {
                        "Long" -> {
                            jsonObject.put(i.key, parseToLong(i).toString())
                        }
                        "Double" ->{
                            jsonObject.put(i.key, parseToDouble(i).toString())
                        }
                        "String" -> {
                            jsonObject.put(i.key, parseToString(i))
                        }
                        "Boolean" -> {
                            jsonObject.put(i.key, parseToBoolean(i))
                        }
                        "Int" -> {
                            jsonObject.put(i.key, parseToInt(i).toString())
                        }
                        "String[]" -> {
                            val keyList = i.key.split(" ")
                            val valueList = parseToStringArray(i)
                            for (j in 0 until valueList.size) {
                                jsonObject.put(keyList[j], valueList[j])
                            }
                        }
                        "Long[]" -> {
                            val keyList = i.key.split(" ")
                            val valueList = parseToLongArray(i)
                            for (j in 0 until valueList.size) {
                                jsonObject.put(keyList[j], valueList[j])
                            }
                        }
                        "Long String" -> {
                            val longAlsoString = parseToLongAndString(i)
                            val keys = i.key.split(" ")
                            if (longAlsoString.second != i.inputMultiAbandonInput)
                                jsonObject.put(keys[0], longAlsoString.first)
                            jsonObject.put(keys[1], longAlsoString.second)
                        }
                        "Double String" -> {
                            val doubleAlsoString = parseToDoubleAndString(i)
                            val keys = i.key.split(" ")
                            jsonObject.put(keys[0], doubleAlsoString.first)
                            jsonObject.put(keys[1], doubleAlsoString.second)
                        }
                        "JsonArray"->
                        {
                            jsonObject.put(i.key,generateJsonArray(i.itemMultiStyleItem))
                        }
                    }
                }
                //建立网络请求体 (类型，内容)
                val requestBody = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
                it.onNext(requestBody)
            }

        }
        fun parseToLongAndString(data: MultiStyleItem): Pair<Long, String> {
            var longAlsoString: Pair<Long, String>
            when (data.options) {
                MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                    if (data.inputMultiSelectUnit != data.inputMultiAbandonInput)
                        longAlsoString = Pair(data.inputMultiContent.toLong(), data.inputMultiSelectUnit)
                    else
                        longAlsoString = Pair(Long.MIN_VALUE, data.inputMultiSelectUnit)
                }
                else -> {
                    longAlsoString = Pair(Long.MIN_VALUE, "")
                }
            }
            return longAlsoString
        }
        fun parseToLongArray(data: MultiStyleItem): List<Long> {
            val result: MutableList<Long> = ArrayList()
            when (data.options) {
                MultiStyleItem.Options.INPUT_RANGE -> {

                    result.add(data.inputRangeValue1.toLongOrNull().run {
                        this ?: 10.toLong()
                    })
                    result.add(data.inputRangeValue2.toLongOrNull().run {
                        this ?: 10.toLong()
                    })
                }
            }
            return result
        }

        private fun parseToStringArray(data: MultiStyleItem): List<String> {
            var resultList: MutableList<String> = ArrayList()
            when (data.options) {
                //带单位薪资标准   数值+单位
                MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                    if (data.inputMultiContent != "")
                        resultList.add(data.inputMultiContent)
                    if (data.inputMultiSelectUnit != "")
                        resultList.add(data.inputMultiSelectUnit)
                }
                MultiStyleItem.Options.INPUT_RANGE -> {
                    if (data.inputRangeValue1 != "")
                        resultList.add(data.inputRangeValue1)
                    if (data.inputRangeValue2 != "")
                        resultList.add(data.inputRangeValue2)
                }
                MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG->{
                    resultList=data.selectContent.split(" ") as MutableList<String>
                }
            }
            return resultList
        }
        fun parseToDoubleAndString(data: MultiStyleItem): Pair<Double?, String> {
            var doubleAlsoString: Pair<Double?, String>
            when (data.options) {
                MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                    if (data.inputMultiSelectUnit != data.inputMultiAbandonInput)
                        doubleAlsoString = Pair(data.inputMultiContent.toDoubleOrNull(), data.inputMultiSelectUnit)
                    else
                        doubleAlsoString = Pair(-1.0, data.inputMultiSelectUnit)
                }
                else -> {
                    doubleAlsoString = Pair(-1.0, "")
                }
            }
            return doubleAlsoString
        }

        fun parseToBoolean(data: MultiStyleItem): Boolean {
            var result = false
            when (data.options) {
                MultiStyleItem.Options.MULTI_RADIO_BUTTON -> {
                    if(data.radioButtonValue=="1"){
                        result = true
                    }
                   else if(data.radioButtonValue=="0"){
                        result = false
                    }

                }
                MultiStyleItem.Options.SELECT_DIALOG ->{
                    if(data.selectOption1Items.indexOf(data.selectContent)==0){
                        result=true
                    }
                }
            }
            return result
        }

        fun generateMultiPartRequestBody(baseUrl: String) {
            val map = HashMap<String, RequestBody>()
            val result = Observable.create<HashMap<String, RequestBody>> {
                for (i in mData) {
                    when (i.sendFormat) {
                        "String" -> {
                            val requestBody = RequestBody.create(MultipartBody.FORM, parseToString(i))
                            map[i.key] = requestBody
                        }
                        "String[]" -> {
                            val keys = i.key.split(" ")
                            val valueList = parseToStringArray(i)
                            for (j in 0 until keys.size) {
                                val requestBody = RequestBody.create(MultipartBody.FORM, valueList[j])
                                map[keys[j]] = requestBody
                            }
                        }
                        "Int" -> {
                            val requestBody = RequestBody.create(MultipartBody.FORM, parseToInt(i).toString())
                            map[i.key] = requestBody
                        }
                        "Boolean" -> {
                            val requestBody = RequestBody.create(MultipartBody.FORM, parseToBoolean(i).toString())
                            map[i.key] = requestBody
                        }
                    }
                }
                for (i in 0 until UnSerializeDataBase.fileList.size) {
                    val key = UnSerializeDataBase.fileList[i].key
                    val file = File(UnSerializeDataBase.fileList[i].path)
                    val builder = MultipartBody.Builder()
                    if (file.name.contains("jpg") || file.name.contains("png")) {
                        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                        builder.addFormDataPart(key, file.name, requestBody)
                    } else {
                        val requestBody =
                            RequestBody.create(MediaType.parse("application/octet-stream;charset=utf-8"), file)
                        if (file.exists())
                            Log.i("file", "exist")
                        if (file.canRead())
                            Log.i("file", "can read")
                        builder.addFormDataPart(key, URLEncoder.encode(file.name, "utf-8"), requestBody)
                    }
                    map[key] = builder.build()
                }
                it.onNext(map)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val loadingDialog = LoadingDialog(context, "正在发布...", R.mipmap.ic_dialog_loading)
                    loadingDialog.show()
                    val result = startSendMultiPartMessage(it, baseUrl).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            loadingDialog.dismiss()
                            var json = JSONObject(it.string())
                            if (json.getInt("code") == 200) {
                                Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show()
                            } else if (json.getInt("code") == 400) {
                                Toast.makeText(context, "发布失败", Toast.LENGTH_SHORT).show()
                            }
//                        Log.i("retrofitMsg", it.string())
                            for (i in 0 until UnSerializeDataBase.imgList.size) {
                                if (UnSerializeDataBase.imgList[i].needDelete) {
                                    val file = File(UnSerializeDataBase.imgList[i].path)
                                    file.delete()
                                }
                            }
                        },
                            {
                                loadingDialog.dismiss()
                                val toast = Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                it.printStackTrace()
                            })
                }
        }
        fun parseToDouble(data:MultiStyleItem):Double{
            var result: Double = Double.MIN_VALUE
            when (data.options) {
                MultiStyleItem.Options.SINGLE_INPUT -> {
                    val tmp = data.inputSingleContent.toDoubleOrNull()
                    result = if (tmp != null) {
                        tmp
                    } else
                        10.0
                }
                MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                    val tmp = data.inputMultiContent.toDoubleOrNull()
                    result = if (tmp != null) {
                        tmp
                    } else
                        10.0
                }
            }
            return result
        }
        fun parseToLong(data: MultiStyleItem): Long {
            var result: Long = Long.MIN_VALUE
            when (data.options) {
                MultiStyleItem.Options.MULTI_RADIO_BUTTON -> {
                    val tmp = data.radioButtonValue.toLongOrNull()
                    result = if (tmp == null) {
                        10
                    } else
                        tmp
                }
                MultiStyleItem.Options.MULTI_BUTTON -> {
                    for (i in 0 until data.buttonCheckList.size) {
                        if (data.buttonCheckList[i]) {
                            result = (1 - i).toLong()
                            break
                        }
                    }
                }
                MultiStyleItem.Options.INPUT_WITH_UNIT -> {
                    val tmp = data.inputUnitContent.toLongOrNull()
                    result = if (tmp != null) {
                        tmp
                    } else
                        10
                }
                MultiStyleItem.Options.SINGLE_INPUT->{
                    val tmp = data.inputSingleContent.toLongOrNull()
                    result = if (tmp != null) {
                        tmp
                    } else
                        10
                }
                MultiStyleItem.Options.MULTI_CHECKBOX -> {
                    var checkNum = 0
                    for (j in 0 until data.checkboxValueList.size) {
                        if (data.checkboxValueList[j]) {
                            if (checkNum != 0)
                                result += (1-j).toLong()
                            else
                                result = (1-j).toLong()
                            checkNum++
                        }
                    }
                }
                MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT->{
                    val tmp = data.singleDisplayRightContent.toLongOrNull()
                    result = if (tmp != null) {
                        tmp
                    } else
                        10
                }
                MultiStyleItem.Options.SELECT_DIALOG -> {
                    result = (1-data.selectOption1Items.indexOf(data.selectContent)).toLong()
                }
            }
            return result
        }

        fun parseToInt(data: MultiStyleItem): Int {
            var result = Int.MIN_VALUE
            when (data.options) {
                MultiStyleItem.Options.INPUT_WITH_UNIT -> {
                    result = data.inputUnitContent.toInt()
                }
                MultiStyleItem.Options.SELECT_DIALOG -> {
                    result = data.selectOption1Items.indexOf(data.selectContent) + 1
                }
            }
            return result
        }

        fun parseToString(data: MultiStyleItem): String {
            var result = ""
            when (data.options) {
                MultiStyleItem.Options.INPUT_WITH_TEXTAREA -> {
                    result = data.textAreaContent
                }
                MultiStyleItem.Options.SHIFT_INPUT -> {
                    if(data.shiftInputTitle=="车辆照片" || data.shiftInputTitle=="营业执照"){
                        result = data.shiftInputPicture
                    }
                    if(data.shiftInputTitle=="可服务地域"){
                        result=data.shiftInputContent
                    }else {
                        for(j in UnSerializeDataBase.imgList){
                            if(j.key==data.key){
                                result=j.path
                            }
                        }
//                        val results = try {
//                            for (j in UnSerializeDataBase.imgList) {
//                                if (j.key == data.key) {
//                                    val imagePath = j.path.split("|")
//                                    for (k in imagePath) {
//                                        val file = File(k)
//                                        val imagePart = MultipartBody.Part.createFormData(
//                                            "file",
//                                            file.name,
//                                            RequestBody.create(MediaType.parse("image/*"), file)
//                                        )
//                                        uploadImage(imagePart).observeOn(AndroidSchedulers.mainThread())
//                                            .subscribe(
//                                                {
//                                                    //Log.i("responseBody",it.string())
//                                                    if (result != "")
//                                                        result += "|"
//                                                    val json = JSONObject(it.string())
//                                                    if (json.getBoolean("success")) {
//                                                        result += json.getString("httpUrl")
//                                                    } else {
//                                                        result += ""
//                                                    }
//                                                },
//                                                {
//                                                    it.printStackTrace()
//                                                })
//                                    }
//                                }
//                            }
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
                    }
                }
                MultiStyleItem.Options.SINGLE_INPUT -> {
                    result = data.inputSingleContent
                }
                MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT -> {
                    if(data.singleDisplayRightTitle.contains("合同"))
                        result = data.filePath
                    else
                        result = data.singleDisplayRightContent
                }
                MultiStyleItem.Options.SELECT_DIALOG, MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG, MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG -> {
                    result = data.selectContent
                    if(data.selectTitle.contains("地点"))
                        result = result.replace(" "," / ")
                }
                MultiStyleItem.Options.MULTI_CHECKBOX -> {
                    val ch: ArrayList<String> = ArrayList()
                    for (j in 0 until data.checkboxValueList.size)
                        if (data.checkboxValueList[j]) {
                            ch.add(data.checkboxNameList[j])
                        }
                        result = ch.toString().replace(", ", "、")
                        result = result.substring(1, result.length - 1)
                }
                MultiStyleItem.Options.MULTI_RADIO_BUTTON -> {
                    val position = 1 - data.radioButtonValue.toInt()
                    result = data.radioButtonName[position]
                }
            }
            return result
        }
    }

    //data source  需
    var mData: List<MultiStyleItem> = ArrayList()
    lateinit var context: Context
    constructor(){}

    constructor(context: Context) {
        this.context = context
    }

    constructor(mData: List<MultiStyleItem>) {
        this.mData = mData
    }

    constructor(mData: List<MultiStyleItem>, context: Context) {
        this.mData = mData
        this.context = context
    }
    fun generateJsonArray(data: List<MultiStyleItem>):JSONArray
    {
        val array=JSONArray()
        for (i in data)
        {
            if(i.options!=MultiStyleItem.Options.BLANK&& i.necessary==true)
                array.put(generateJsonObject(i.itemMultiStyleItem))
        }
        return array
    }
    fun generateJsonObject(mData: List<MultiStyleItem>):JSONObject
    {
        val jsonObject = JSONObject()
        if(mData[0].id!="" && UnSerializeDataBase.inventoryIdKey!="" && UnSerializeDataBase.inventoryId!="" ){
            jsonObject.put(UnSerializeDataBase.inventoryIdKey,UnSerializeDataBase.inventoryId)
            jsonObject.put("id",mData[0].id)
        }
        for (i in mData) {
            when (i.sendFormat) {
                "Long" -> {
                    jsonObject.put(i.key, parseToLong(i).toString())
                }
                "Double" ->{
                    jsonObject.put(i.key, parseToDouble(i).toString())
                }
                "String" -> {
                    jsonObject.put(i.key, parseToString(i))
                }
                "String[]" -> {
                    val keyList = i.key.split(" ")
                    val valueList = parseToStringArray(i)
                    for (j in 0 until valueList.size) {
                        jsonObject.put(keyList[j], valueList[j])
                    }
                }
                "Int" -> {
                    jsonObject.put(i.key, parseToInt(i).toString())
                }
                "Boolean" -> {
                    jsonObject.put(i.key, parseToBoolean(i))
                }
                "Long String" -> {
                    val longAlsoString = parseToLongAndString(i)
                    val keys = i.key.split(" ")
                    if (longAlsoString.second != i.inputMultiAbandonInput)
                        jsonObject.put(keys[0], longAlsoString.first)
                    jsonObject.put(keys[1], longAlsoString.second)
                }
                "JsonArray"->
                {
                    jsonObject.put(i.key,generateJsonArray(i.itemMultiStyleItem))
                }
            }
        }
        return jsonObject
    }
    fun generateJsonRequestBody(json:JSONObject):Observable<RequestBody>{

        return  Observable.create<RequestBody> {
            val jsonObject = json
            for (i in mData) {
                when (i.sendFormat) {
                    "Long" -> {
                        jsonObject.put(i.key, parseToLong(i).toString())
                    }
                    "Double" ->{
                        jsonObject.put(i.key, parseToDouble(i).toString())
                    }
                    "String" -> {
                        jsonObject.put(i.key, parseToString(i))
                    }
                    "Int" -> {
                        jsonObject.put(i.key, parseToInt(i).toString())
                    }
                    "String[]" -> {
                        val keyList = i.key.split(" ")
                        val valueList = parseToStringArray(i)
                        for (j in 0 until valueList.size) {
                            jsonObject.put(keyList[j], valueList[j])
                        }
                    }
                    "Long[]" -> {
                        val keyList = i.key.split(" ")
                        val valueList = parseToLongArray(i)
                        for (j in 0 until valueList.size) {
                            jsonObject.put(keyList[j], valueList[j])
                        }
                    }
                    "Long String" -> {
                        val longAlsoString = parseToLongAndString(i)
                        val keys = i.key.split(" ")
                        if (longAlsoString.second != i.inputMultiAbandonInput)
                            jsonObject.put(keys[0], longAlsoString.first)
                        jsonObject.put(keys[1], longAlsoString.second)
                    }
                    "Double String" -> {
                        val doubleAlsoString = parseToDoubleAndString(i)
                        val keys = i.key.split(" ")
                        jsonObject.put(keys[0], doubleAlsoString.first)
                        jsonObject.put(keys[1], doubleAlsoString.second)
                    }
                    "JsonArray"->
                    {
                        jsonObject.put(i.key,generateJsonArray(i.itemMultiStyleItem))
                    }
                }
            }
            val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
            it.onNext(requestBody)
        }


    }

    fun generateMultiPartRequestBody(baseUrl: String) {
        val map = HashMap<String, RequestBody>()
        val result = Observable.create<HashMap<String, RequestBody>> {
            for (i in mData) {
                when (i.sendFormat) {
                    "Long" -> {
                        val requestBody = RequestBody.create(MultipartBody.FORM, parseToLong(i).toString())
                        map[i.key] = requestBody
                    }
                    "String" -> {
                        val requestBody = RequestBody.create(MultipartBody.FORM, parseToString(i))
                        map[i.key] = requestBody
                    }
                    "Long[]" -> {
                        val keyList = i.key.split(" ")
                        val valueList = parseToLongArray(i)
                        for (j in 0 until valueList.size) {
                            val requestBody = RequestBody.create(MultipartBody.FORM, valueList[j].toString())
                            map[keyList[j]] = requestBody
                        }
                    }
                    "String[]" -> {
                        val keys = i.key.split(" ")
                        val valueList = parseToStringArray(i)
                        for (j in 0 until keys.size) {
                            val requestBody = RequestBody.create(MultipartBody.FORM, valueList[j])
                            map[keys[j]] = requestBody
                        }
                    }
                    "Long String" -> {
                        val longAlsoString = parseToLongAndString(i)
                        val keys = i.key.split(" ")
                        map[keys[0]] = RequestBody.create(MultipartBody.FORM, longAlsoString.first.toString())
                        map[keys[1]] = RequestBody.create(MultipartBody.FORM, longAlsoString.second)
                    }
                    "Double String" -> {
                        val doubleAlsoString = parseToDoubleAndString(i)
                        val keys = i.key.split(" ")
                        map[keys[0]] = RequestBody.create(MultipartBody.FORM, doubleAlsoString.first.toString())
                        map[keys[1]] = RequestBody.create(MultipartBody.FORM, doubleAlsoString.second)
                    }
                }
            }
            for (i in 0 until UnSerializeDataBase.fileList.size) {
                val key = UnSerializeDataBase.fileList[i].key
                val file = File(UnSerializeDataBase.fileList[i].path)
                val builder = MultipartBody.Builder()
                if (file.name.contains("jpg") || file.name.contains("png")) {
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    builder.addFormDataPart(key, file.name, requestBody)
                } else {
                    val requestBody = RequestBody.create(MediaType.parse("text/plain"), file)
                    builder.addFormDataPart(key, file.name, requestBody)
                }
                map[key] = builder.build()
            }
            for (i in 0 until UnSerializeDataBase.imgList.size) {
                val key = UnSerializeDataBase.imgList[i].key
                val file = File(UnSerializeDataBase.imgList[i].path)
                val builder = MultipartBody.Builder()
                builder.addFormDataPart(key, file.name, RequestBody.create(MediaType.parse("image/*"), file))
                map[key] = builder.build()
            }
            it.onNext(map)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val loadingDialog = LoadingDialog(context, "正在处理...", R.mipmap.ic_dialog_loading)
                loadingDialog.show()
                val result = startSendMultiPartMessage(it, baseUrl).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        loadingDialog.dismiss()
                        var json = JSONObject(it.string())
                        if (json.getInt("code") == 200) {
                            Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show()
                        } else if (json.getInt("code") == 400) {
                            Toast.makeText(context, "发布失败", Toast.LENGTH_SHORT).show()
                        }
//                        Log.i("retrofitMsg", it.string())
                        for (i in 0 until UnSerializeDataBase.imgList.size) {
                            if (UnSerializeDataBase.imgList[i].needDelete) {
                                val file = File(UnSerializeDataBase.imgList[i].path)
                                file.delete()
                            }
                        }
                    },
                        {
                            loadingDialog.dismiss()
                            val toast = Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                            it.printStackTrace()
                        })
            }
    }

    fun parseToStringArray(data: MultiStyleItem): List<String> {
        val resultList: MutableList<String> = ArrayList()
        when (data.options) {
            MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                if (data.inputMultiContent != "")
                    resultList.add(data.inputMultiContent)
                if (data.inputMultiSelectUnit != "")
                    resultList.add(data.inputMultiSelectUnit)
            }
            MultiStyleItem.Options.INPUT_RANGE -> {
                if (data.inputRangeValue1 != "")
                    resultList.add(data.inputRangeValue1)

                if (data.inputRangeValue2 != "")
                    resultList.add(data.inputRangeValue2)
            }
            MultiStyleItem.Options.TWO_PAIR_INPUT -> {
                if (data.twoPairInputValue1 != "")
                    resultList.add(data.twoPairInputValue1)
                if (data.twoPairInputValue2 != "")
                    resultList.add(data.twoPairInputValue2)
            }
        }
        return resultList
    }
    fun parseToDouble(data:MultiStyleItem):Double{
        var result: Double = Double.MIN_VALUE
        when (data.options) {
            MultiStyleItem.Options.SINGLE_INPUT -> {
                val tmp = data.inputSingleContent.toDoubleOrNull()
                result = if (tmp != null) {
                    tmp
                } else
                    10.0
            }
            MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                val tmp = data.inputMultiContent.toDoubleOrNull()
                result = if (tmp != null) {
                    tmp
                } else
                    10.0
            }
        }
        return result
    }
    private fun parseToInt(data: MultiStyleItem): Int {
        var result = Int.MIN_VALUE
        when (data.options) {
            MultiStyleItem.Options.INPUT_WITH_UNIT -> {
                if(data.inputUnitContent.toIntOrNull()!=null){
                    result = data.inputUnitContent.toInt()
                }

            }
            MultiStyleItem.Options.SELECT_DIALOG ->{
                result = 1-data.selectOption1Items.indexOf(data.selectContent)
            }
        }
        return result
    }
    private fun parseToBoolean(data: MultiStyleItem): Boolean {
        var result = false
        when (data.options) {
            MultiStyleItem.Options.MULTI_RADIO_BUTTON -> {
                if(data.radioButtonValue=="1"){
                    result = true
                }
                else if(data.radioButtonValue=="0"){
                    result = false
                }
            }
            MultiStyleItem.Options.SELECT_DIALOG ->{
                result = (data.selectOption1Items.indexOf(data.selectContent)).toString().toBoolean()
            }
        }
        return result
    }
    fun parseToString(data: MultiStyleItem): String {
        var result = ""
        when (data.options) {
            MultiStyleItem.Options.TITLE -> {
                result = if (data.title1 != "") {
                    data.title1
                } else {
                    ""
                }
            }
            MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT -> {
                result = if (data.singleDisplayRightContent != "") {
                    data.singleDisplayRightContent
                } else
                    ""
            }
            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT -> {
                result = if (data.singleDisplayLeftContent != "") {
                    data.singleDisplayLeftContent
                } else
                    ""
            }
            MultiStyleItem.Options.SINGLE_INPUT -> {
                result = if (data.inputSingleContent != "") {
                    data.inputSingleContent
                } else
                    ""
            }
            MultiStyleItem.Options.INPUT_WITH_UNIT -> {
                result = if (data.inputUnitContent != "")
                    data.inputUnitContent
                else
                    ""
            }
            MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                if (data.inputMultiSelectUnit != data.inputMultiAbandonInput)
                    result = "${data.inputMultiContent.toDouble()}" + data.inputMultiSelectUnit
                else
                    result = "${Long.MIN_VALUE}" + data.inputMultiSelectUnit
            }

            MultiStyleItem.Options.INPUT_WITH_TEXTAREA -> {
                result = if (data.textAreaContent != "")
                    data.textAreaContent
                else
                    ""
            }
            MultiStyleItem.Options.SHIFT_INPUT -> {
                if(data.shiftInputTitle=="车辆照片")
                    result = data.shiftInputPicture
                else
                for(j in UnSerializeDataBase.imgList){
                    if(j.key==data.key){
                        result=j.path
                    }
                }
            }
            MultiStyleItem.Options.SELECT_DIALOG, MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG, MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG -> {
                result = if (data.selectContent != "")
                    data.selectContent
                else
                    ""
                if(data.selectTitle.contains("地点"))
                    result = result.replace(" "," / ")
            }
            MultiStyleItem.Options.MULTI_BUTTON -> {
                var checkNum = 0
                for (j in 0 until data.buttonCheckList.size) {
                    if (data.buttonCheckList[j]) {
                        if (checkNum != 0)
                            result += "|${1 - j}"
                        else
                            result = "${1 - j}"
                        checkNum++
                    }
                }
            }
            MultiStyleItem.Options.MULTI_CHECKBOX -> {
                val ch: ArrayList<String> = ArrayList()
                for (j in 0 until data.checkboxValueList.size)
                    if (data.checkboxValueList[j]) {
                        ch.add(data.checkboxNameList[j])
                    }
                result = ch.toString().replace(", ", "、")
                result = result.substring(1, result.length - 1)
            }
            MultiStyleItem.Options.MULTI_RADIO_BUTTON -> {
                result = data.radioButtonValue
            }
        }
        return result
    }

    fun parseToLong(data: MultiStyleItem): Long {
        var result: Long = Long.MIN_VALUE
        when (data.options) {
            MultiStyleItem.Options.MULTI_RADIO_BUTTON -> {
                val tmp = data.radioButtonValue.toLongOrNull()
                result = if (tmp == null) {
                    10
                } else
                    tmp
            }
            MultiStyleItem.Options.MULTI_BUTTON -> {
                for (i in 0 until data.buttonCheckList.size) {
                    if (data.buttonCheckList[i]) {
                        result = (1 - i).toLong()
                        break
                    }
                }
            }
            MultiStyleItem.Options.INPUT_WITH_UNIT -> {
                val tmp = data.inputUnitContent.toLongOrNull()
                result = if (tmp != null) {
                    tmp
                } else
                    10
            }
            MultiStyleItem.Options.SINGLE_INPUT->{
                val tmp = data.inputSingleContent.toLongOrNull()
                result = if (tmp != null) {
                    tmp
                } else
                    10
            }
            MultiStyleItem.Options.MULTI_CHECKBOX -> {
                var checkNum = 0
                for (j in 0 until data.checkboxValueList.size) {
                    if (data.checkboxValueList[j]) {
                        if (checkNum != 0)
                            result += (1-j).toLong()
                        else
                            result = (1-j).toLong()
                        checkNum++
                    }
                }
            }
            MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT->{
                val tmp = data.singleDisplayRightContent.toLongOrNull()
                result = if (tmp != null) {
                    tmp
                } else
                    10
            }
            MultiStyleItem.Options.SELECT_DIALOG -> {
                result = (1-data.selectOption1Items.indexOf(data.selectContent)).toLong()
            }
        }
        return result
    }

    fun parseToLongArray(data: MultiStyleItem): List<Long> {
        val result: MutableList<Long> = ArrayList()
        when (data.options) {
            MultiStyleItem.Options.INPUT_RANGE -> {

                result.add(data.inputRangeValue1.toLongOrNull().run {
                    this ?: 10.toLong()
                })
                result.add(data.inputRangeValue2.toLongOrNull().run {
                    this ?: 10.toLong()
                })
            }
        }
        return result
    }

    //parse two type
    fun parseToLongAndString(data: MultiStyleItem): Pair<Long, String> {
        val longAlsoString: Pair<Long, String>
        when (data.options) {
            MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                if (data.inputMultiSelectUnit != data.inputMultiAbandonInput)
                    longAlsoString = Pair(data.inputMultiContent.toLong(), data.inputMultiSelectUnit)
                else
                    longAlsoString = Pair(Long.MIN_VALUE, data.inputMultiSelectUnit)
            }
            else -> {
                longAlsoString = Pair(Long.MIN_VALUE, "")
            }
        }
        return longAlsoString
    }

    fun parseToDoubleAndString(data: MultiStyleItem): Pair<Double?, String> {
        val doubleAlsoString: Pair<Double?, String>
        when (data.options) {
            MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                if (data.inputMultiSelectUnit != data.inputMultiAbandonInput)
                    doubleAlsoString = Pair(data.inputMultiContent.toDoubleOrNull(), data.inputMultiSelectUnit)
                else
                    doubleAlsoString = Pair(-1.0, data.inputMultiSelectUnit)
            }
            else -> {
                doubleAlsoString = Pair(-1.0, "")
            }
        }
        return doubleAlsoString
    }

    fun getDataFromNetwork(
        id: Long,
        page: Int,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectDiskFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result =
            getProjects(id, page, baseUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.i("xxxxxxxxxx", "${it.code}  ${it.desc}")
                        val entityList = it.message.data
                        for (i in entityList) {
                            val item = MultiStyleItem(MultiStyleItem.Options.TEXT_EXPAND_SELECT, i.name, "")
                            item.tvExpandTitleListener = View.OnClickListener {
                                val intent = Intent(fragment.activity, ProfessionalActivity::class.java)
                                intent.putExtra("type", Constants.Subitem_TYPE.OVERHEAD_MORE.ordinal)
                                intent.putExtra("MajorDistributionProjectEntity", i)
                                fragment.activity!!.startActivity(intent)
                            }
                            data.add(item)
                        }

                        adapter.mData = data
                        fragment.pageCount = it.message.pageCount
                        adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
                        for (j in oldSize until adapter.mData.size) {
                            adapter.mData[j].tvExpandMoreListener = View.OnClickListener {
                                val option1Items = listOf("删除", "更多", "详情", "动态记录")
                                val handler = Handler(Handler.Callback {
                                    when (it.what) {
                                        RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
                                            val title = it.data.getString("selectContent")
                                            if (title == "删除") {
                                                val result = deleteMajorDistribuionProject(
                                                    entityList[j - oldSize].id,
                                                    baseUrl
                                                ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                    .subscribe {
                                                        Log.i("Body", it.string())
                                                        adapter.mData.toMutableList().removeAt(j)
                                                        adapter.notifyItemRemoved(j)
                                                        if (j != adapter.mData.size) {
                                                            adapter.notifyItemRangeChanged(j, adapter.mData.size - j)
                                                        }
                                                    }
                                            }
                                            false
                                        }
                                        else -> {
                                            false
                                        }
                                    }
                                })
                                val selectDialog = CustomDialog(
                                    CustomDialog.Options.SELECT_DIALOG,
                                    fragment.context!!,
                                    option1Items,
                                    handler
                                ).dialog
                                selectDialog.show()
                            }
                        }
                    },
                    {
                        it.printStackTrace()
                    }
                )
    }

    fun getDataPowerTowerParameters(
        majorDistributionProjectId: Long,
        pageNumber: Int,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result =
            getPowerTowerParameters(majorDistributionProjectId, pageNumber, baseUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    val entityList = it.message.data
                    for (i in entityList) {
                        val item = MultiStyleItem(
                            MultiStyleItem.Options.FIVE_DISPLAY,
                            i.poleNumber,
                            i.powerTowerType,
                            i.powerTowerHeight,
                            i.powerTowerAttribute,
                            "···"
                        )
                        item.fiveDisplayListener = View.OnClickListener {
                            val data = Bundle()
                            data.putInt("type", AdapterGenerate().getType("杆塔子项"))
                            data.putSerializable("overheadTower", i)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ProjectMoreFragment.newInstance(data),
                                ""
                            )
                        }
                        data.add(item)
                    }
                    adapter.mData = data
                    fragment.pageCount = it.message.pageCount
                    adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
                }
    }

    fun getDataGalleryParameter(
        majorDistributionProjectId: Long,
        pageNumber: Int,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getGalleryParameter(majorDistributionProjectId, pageNumber, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entityList = it.message.data.galleryNodeDTOList
                data[1].twoColumnDisplayContent1 = it.message.data.apType
                data[1].twoColumnDisplayContent2 = it.message.data.opType
                data[2].singleDisplayLeftContent = it.message.data.apName
                data[3].singleDisplayLeftContent = it.message.data.opName
                for (i in entityList) {
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.FIVE_DISPLAY,
                        i.galleryIndex,
                        i.bnodeIndex,
                        i.snodeIndex,
                        i.kind,
                        "···"
                    )
                    item.fiveDisplayListener = View.OnClickListener {
                        val data = Bundle()
                        data.putInt("type", AdapterGenerate().getType("通道子项"))
                        data.putSerializable("galleryNode", i)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ProjectMoreFragment.newInstance(data),
                            ""
                        )
                    }
                    data.add(item)
                }
                adapter.mData = data
                fragment.pageCount = it.message.pageCount
                adapter.notifyItemRangeChanged(1, 3)
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataNodeParameter(
        majorDistributionProjectId: Long,
        pageNumber: Int,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getNodeParameter(majorDistributionProjectId, pageNumber, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it.desc == "OK") {
                    val entityList = it.message.data.nodeDTOList
                    data[1].twoColumnDisplayContent1 = it.message.data.apType
                    data[1].twoColumnDisplayContent2 = it.message.data.opType
                    data[2].singleDisplayLeftContent = it.message.data.apName
                    data[3].singleDisplayLeftContent = it.message.data.opName
                    for (i in entityList) {
                        val item = MultiStyleItem(MultiStyleItem.Options.FOUR_DISPLAY, i.nodeIndex, i.kind, i.entail, "···")
                        item.fourDisplayListener = View.OnClickListener {
                            val data = Bundle()
                            data.putInt("type", AdapterGenerate().getType("节点子项"))
                            data.putSerializable("Node", i)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ProjectMoreFragment.newInstance(data),
                                ""
                            )
                        }
                        data.add(item)
                    }
                    adapter.mData = data
                    fragment.pageCount = it.message.pageCount
                    adapter.notifyItemRangeChanged(1, 3)
                    adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
                }
            }
    }

    fun getDataMaterialsTransport(
        towerSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getMaterialsTransport(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    for (k in j.aerialMaterialsAdd) {
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "${k.materialsName}/数量${k.unit}:",
                                k.quantity.toString()
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "${k.materialsName}/规格型号:",
                                k.specificationsModel
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "${k.materialsName}/单重(t):",
                                k.simpleWeight.toString()
                            )
                        )
                    }
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "小计重量(t)",
                            j.aerialMaterialsTransport.totalWeight.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.aerialMaterialsTransport.photoPath != null) {
                        UnSerializeDataBase.imgList.add(
                            BitmapMap(
                                j.aerialMaterialsTransport.photoPath.toString(),
                                "photoPath"
                            )
                        )
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j.aerialMaterialsTransport)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialMaterialsTransport)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.aerialMaterialsTransport.content, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataPreformedUnitLandFill(
        towerSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getPreformedUnitLandFill(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.quantityUnit}):",
                            j.quantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialPreformedUnitLandfill)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.content, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataALLAerialFacilityInstall(
        towerSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getALLAerialFacilityInstall(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.quantity.toString()
                        )
                    )
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.name, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialsStayguyMakeList(towerSubitemId: String, baseUrl: String, adapter: RecyclerviewAdapter) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialsStayguyMakeList(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.designQuantity.toString()
                        )
                    )
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.materialsName, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialEnclosureInstall(towerSubitemId: String, baseUrl: String, adapter: RecyclerviewAdapter) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialEnclosureInstall(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "电压等级:", j.voltageClass))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.quantity.toString()
                        )
                    )
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.addContent, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialInsulatorInstall(towerSubitemId: String, baseUrl: String, adapter: RecyclerviewAdapter) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialInsulatorInstall(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "电压等级:", j.voltageClass))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.quantity.toString()
                        )
                    )
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.addContent, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialPoleArmInstall(
        towerSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialPoleArmInstall(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "电压等级:", j.voltageClass))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "线材根数:", j.wireRodQuantity))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.quantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialPoleArmInstall)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.addContent, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialHouseholdInstall(
        towerSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialHouseholdInstall(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message.aerialHouseholdInstallCommentList
                if (entity.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "户表安装",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    if (j.specificationsModels != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "规格型号:",
                                j.specificationsModels.toString()
                            )
                        )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.quantity.toString()
                        )
                    )
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.addComment, "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }


                val entity1 = it.message.aerialHouseholdInstallRegistrationList
                if (entity1.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "表计编号登记",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity1) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "原表计号:", j.oldTableNumber))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "原表计号照片上传:", false))
                    if (j.oldTableLogging != null) {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.oldTableLogging.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "原表计号照片上传")
                        data.putString("key", "oldTableLogging")
                        data.putSerializable("serializable", j)
                        data.putString(
                            "baseUrl",
                            Constants.HttpUrlPath.Professional.updateAerialHouseholdInstallRegistration
                        )
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "新表计号:", j.newTableNumber))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "新表计号照片上传:", false))
                    if (j.newTableLogging != null) {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.newTableLogging.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "新表计号照片上传")
                        data.putString("key", "newTableLogging")
                        data.putSerializable("serializable", j)
                        data.putString(
                            "baseUrl",
                            Constants.HttpUrlPath.Professional.updateAerialHouseholdInstallRegistration
                        )
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "${it.message.aerialHouseholdInstallRegistrationList.indexOf(j) + 1}#",
                            "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }

                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialElectrificationJob(
        towerSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialElectrificationJob(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "子项:", j.addSubitem))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.quantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialElectrificationJob)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.addContent, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialFoundationExcavation(
        towerId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment,
        adapterGenerate: AdapterGenerate
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialFoundationExcavation(towerId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                var entity = it.message.aerialExcavationRoadCuts
                if (entity.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "路面切割",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "长度:",
                            j.roadCutLength.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialExcavationRoadCut)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()

                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "${entity.indexOf(j) + 1}#", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }
                var entity1 = it.message.aerialExplodeRoads
                if (entity1.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "破除路面",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity1) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "厚度(mm):",
                            j.thickness.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "长(m):",
                            j.length.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "宽(m):",
                            j.width.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "面积(㎡):",
                            j.aera.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "人工运土(m³):",
                            j.artificialEarthMoving.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "运距(km):",
                            j.artificialTransportDistance.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "机械运土(m³):",
                            j.machineryEarthMoving.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "运距(km):",
                            j.machineryTransportDistannce.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialExplodeRoad)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "路面形式:" + j.roadForm, "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }
                var entity2 = it.message.aerialPolePitExcavationDTOS
                if (entity2.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "电杆坑开挖",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity2) {
                    Log.i("xxx", j.aerialExcavationSoilProportion.toString())
                    // val json = JSONObject(j.aerialExcavationSoilProportion.toString())
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    val shape =
                        if (j.aerialPolePitExcavation.excavationShape == 1.toLong()) {
                            "方形"
                        } else {
                            "圆形"
                        }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "开挖形状:", shape))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "规格型号:", "详情"))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "开挖方式:",
                            j.aerialPolePitExcavation.excavationWay
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "土质比例:", "详情"))
                    val baseArmrest =
                        if (j.aerialPolePitExcavation.foundationDefenceWall == 1.toLong()) {
                            "有"
                        } else {
                            "无"
                        }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "基础护壁 :", baseArmrest))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "护壁类型:",
                            j.aerialPolePitExcavation.defenceWallType
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "挡土板(m³):",
                            j.aerialPolePitExcavation.damBoard.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "挡土板类型:",
                            j.aerialPolePitExcavation.damBoardType
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "回填方量(m³):",
                            j.aerialPolePitExcavation.backfillQuantity.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "回填方式:",
                            j.aerialPolePitExcavation.backfillWay
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "回填物比例:", "详情"))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "平基方量(m³):",
                            j.aerialPolePitExcavation.flatSubgradeQuantity.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "自动计算坑内方量(㎡):",
                            j.aerialPolePitExcavation.flatSubgradeQuantityCalculate.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "人工运土(m³):",
                            j.aerialPolePitExcavation.artificialEarthMoving.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "运距(km):",
                            j.aerialPolePitExcavation.artificialTransportDistance.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "机械运土(m³):",
                            j.aerialPolePitExcavation.machineryEarthMoving.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "运距(km):",
                            j.aerialPolePitExcavation.machineryTransportDistannce.toString()
                        )
                    )
                    //土质比例
                    expandList[3].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.SoilRatio()
                        mAdapter.mData[1].twoDisplayContent2 = j.aerialExcavationSoilProportion.ordinarySoil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = j.aerialExcavationSoilProportion.sanSoil.toString()
                        mAdapter.mData[3].twoDisplayContent2 = j.aerialExcavationSoilProportion.looseSand.toString()
                        mAdapter.mData[4].twoDisplayContent2 =
                            j.aerialExcavationSoilProportion.muddyWaterHole.toString()
                        mAdapter.mData[5].twoDisplayContent2 = j.aerialExcavationSoilProportion.waterHole.toString()
                        mAdapter.mData[6].twoDisplayContent2 = j.aerialExcavationSoilProportion.driftSandHole.toString()
                        mAdapter.mData[7].twoDisplayContent2 = j.aerialExcavationSoilProportion.drySandHole.toString()
                        mAdapter.mData[8].twoDisplayContent2 = j.aerialExcavationSoilProportion.dynamiteRock.toString()
                        mAdapter.mData[9].twoDisplayContent2 = j.aerialExcavationSoilProportion.manuRock.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    //规格型号
                    expandList[1].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        Log.i("xxxx", j.aerialExcavationSpecificationsModels[0].quantity.toString())
                        Log.i("xxxx", j.aerialExcavationSpecificationsModels[1].quantity.toString())
                        val ex = j.aerialExcavationSpecificationsModels
                        val mAdapter = adapterGenerate.SpecificationsModels(shape)
                        for (k in 0 until ex.size) {
                            mAdapter.mData[k + 1].twoColumnDisplayContent2 = ex[k].quantity.toString()
                        }
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    //回填物比例
                    expandList[10].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.BackfillProportion()
                        mAdapter.mData[1].twoDisplayContent2 = j.aerialPoleBackfillProportion.soil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = j.aerialPoleBackfillProportion.girt.toString()
                        mAdapter.mData[3].twoDisplayContent2 = j.aerialPoleBackfillProportion.mountain.toString()
                        mAdapter.mData[4].twoDisplayContent2 = j.aerialPoleBackfillProportion.detritus.toString()
                        mAdapter.mData[5].twoDisplayContent2 = j.aerialPoleBackfillProportion.dust.toString()
                        mAdapter.mData[6].twoDisplayContent2 = j.aerialPoleBackfillProportion.pieceStone.toString()
                        mAdapter.mData[7].twoDisplayContent2 = j.aerialPoleBackfillProportion.beton.toString()
                        mAdapter.mData[8].twoDisplayContent2 = j.aerialPoleBackfillProportion.others.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.aerialPolePitExcavation.photoPath != null) {
                        UnSerializeDataBase.imgList.add(
                            BitmapMap(
                                j.aerialPolePitExcavation.photoPath.toString(),
                                "photoPath"
                            )
                        )
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j.aerialPolePitExcavation)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialPolePitExcavation)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "基坑选择:${j.aerialPolePitExcavation.polePitChoice}", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }

                var entity3 = it.message.aerialStayguyHoleExcavationDTOS
                if (entity3.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "拉线洞开挖",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity3) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    val shape =
                        if (j.aerialStayguyHoleExcavation.excavationShape == 1.toLong()) {
                            "方形"
                        } else {
                            "圆形"
                        }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "开挖形状:", shape))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "规格型号:", "详情"))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "开挖方式:",
                            j.aerialStayguyHoleExcavation.excavationWay
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "土质比例:", "详情"))
                    val baseArmrest =
                        if (j.aerialStayguyHoleExcavation.foundationDefenceWall == 1.toLong()) {
                            "有"
                        } else {
                            " "
                        }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "基础护壁 :", baseArmrest))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "护壁类型:",
                            j.aerialStayguyHoleExcavation.defenceWallType
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "挡土板(m³):",
                            j.aerialStayguyHoleExcavation.damBoard.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "挡土板类型:",
                            j.aerialStayguyHoleExcavation.damBoardType
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "回填方量(m³):",
                            j.aerialStayguyHoleExcavation.backfillQuantity.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "回填方式:",
                            j.aerialStayguyHoleExcavation.backfillWay
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "回填物比例:", "详情"))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "平基方量(m³):",
                            j.aerialStayguyHoleExcavation.flatSubgradeQuantity.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "自动计算坑内方量(㎡):",
                            j.aerialStayguyHoleExcavation.flatSubgradeQuantityCalculate.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "人工运土(m³):",
                            j.aerialStayguyHoleExcavation.artificialEarthMoving.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "运距(km):",
                            j.aerialStayguyHoleExcavation.artificialTransportDistance.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "机械运土(m³):",
                            j.aerialStayguyHoleExcavation.machineryEarthMoving.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "运距(km):",
                            j.aerialStayguyHoleExcavation.machineryTransportDistannce.toString()
                        )
                    )
                    //土质比例
                    expandList[3].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.SoilRatio()
                        mAdapter.mData[1].twoDisplayContent2 = j.aerialExcavationSoilProportion.ordinarySoil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = j.aerialExcavationSoilProportion.sanSoil.toString()
                        mAdapter.mData[3].twoDisplayContent2 = j.aerialExcavationSoilProportion.looseSand.toString()
                        mAdapter.mData[4].twoDisplayContent2 =
                            j.aerialExcavationSoilProportion.muddyWaterHole.toString()
                        mAdapter.mData[5].twoDisplayContent2 = j.aerialExcavationSoilProportion.waterHole.toString()
                        mAdapter.mData[6].twoDisplayContent2 = j.aerialExcavationSoilProportion.driftSandHole.toString()
                        mAdapter.mData[7].twoDisplayContent2 = j.aerialExcavationSoilProportion.drySandHole.toString()
                        mAdapter.mData[8].twoDisplayContent2 = j.aerialExcavationSoilProportion.dynamiteRock.toString()
                        mAdapter.mData[9].twoDisplayContent2 = j.aerialExcavationSoilProportion.manuRock.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    //规格型号
                    expandList[1].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.SpecificationsModels(shape)
                        for (k in 0 until j.aerialExcavationSpecificationsModels.size) {
                            mAdapter.mData[k].twoColumnDisplayContent2 =
                                j.aerialExcavationSpecificationsModels[k].quantity.toString()
                        }
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    //回填物比例
                    expandList[10].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.BackfillProportion()
                        mAdapter.mData[1].twoDisplayContent2 = j.aerialPoleBackfillProportion.soil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = j.aerialPoleBackfillProportion.girt.toString()
                        mAdapter.mData[3].twoDisplayContent2 = j.aerialPoleBackfillProportion.mountain.toString()
                        mAdapter.mData[4].twoDisplayContent2 = j.aerialPoleBackfillProportion.detritus.toString()
                        mAdapter.mData[5].twoDisplayContent2 = j.aerialPoleBackfillProportion.dust.toString()
                        mAdapter.mData[6].twoDisplayContent2 = j.aerialPoleBackfillProportion.pieceStone.toString()
                        mAdapter.mData[7].twoDisplayContent2 = j.aerialPoleBackfillProportion.beton.toString()
                        mAdapter.mData[8].twoDisplayContent2 = j.aerialPoleBackfillProportion.others.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.aerialStayguyHoleExcavation.photoPath != null) {
                        UnSerializeDataBase.imgList.add(
                            BitmapMap(
                                j.aerialStayguyHoleExcavation.photoPath.toString(),
                                "photoPath"
                            )
                        )
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j.aerialStayguyHoleExcavation)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialStayguyHoleExcavation)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "基坑选择:${j.aerialStayguyHoleExcavation.polePitChoice}",
                            "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }
                var entity4 = it.message.aerialTowerPitExcavationDTOS
                if (entity4.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "塔坑开挖",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity4) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    val shape =
                        if (j.aerialTowerPitExcavation.excavationShape == 1.toLong()) {
                            "方形"
                        } else {
                            "圆形"
                        }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "开挖形状:", shape))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "规格型号:", "详情"))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "开挖方式:",
                            j.aerialTowerPitExcavation.excavationWay
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "土质比例:", "详情"))
                    val baseArmrest =
                        if (j.aerialTowerPitExcavation.foundationDefenceWall == 1.toLong()) {
                            "有"
                        } else {
                            "无"
                        }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "基础护壁 :", baseArmrest))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "护壁类型:",
                            j.aerialTowerPitExcavation.defenceWallType
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "挡土板(m³):",
                            j.aerialTowerPitExcavation.damBoard.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "挡土板类型:",
                            j.aerialTowerPitExcavation.damBoardType
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "回填方量(m³):",
                            j.aerialTowerPitExcavation.backfillQuantity.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "回填方式:",
                            j.aerialTowerPitExcavation.backfillWay
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "回填物比例:", "详情"))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "平基方量(m³):",
                            j.aerialTowerPitExcavation.flatSubgradeQuantity.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "自动计算坑内方量(㎡):",
                            j.aerialTowerPitExcavation.flatSubgradeQuantityCalculate.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "人工运土(m³):",
                            j.aerialTowerPitExcavation.artificialEarthMoving.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "运距(km):",
                            j.aerialTowerPitExcavation.artificialTransportDistance.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "机械运土(m³):",
                            j.aerialTowerPitExcavation.machineryEarthMoving.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "运距(km):",
                            j.aerialTowerPitExcavation.machineryTransportDistannce.toString()
                        )
                    )
                    //土质比例
                    expandList[3].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.SoilRatio()
                        mAdapter.mData[1].twoDisplayContent2 = j.aerialExcavationSoilProportion.ordinarySoil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = j.aerialExcavationSoilProportion.sanSoil.toString()
                        mAdapter.mData[3].twoDisplayContent2 = j.aerialExcavationSoilProportion.looseSand.toString()
                        mAdapter.mData[4].twoDisplayContent2 =
                            j.aerialExcavationSoilProportion.muddyWaterHole.toString()
                        mAdapter.mData[5].twoDisplayContent2 = j.aerialExcavationSoilProportion.waterHole.toString()
                        mAdapter.mData[6].twoDisplayContent2 = j.aerialExcavationSoilProportion.driftSandHole.toString()
                        mAdapter.mData[7].twoDisplayContent2 = j.aerialExcavationSoilProportion.drySandHole.toString()
                        mAdapter.mData[8].twoDisplayContent2 = j.aerialExcavationSoilProportion.dynamiteRock.toString()
                        mAdapter.mData[9].twoDisplayContent2 = j.aerialExcavationSoilProportion.manuRock.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    //规格型号
                    expandList[1].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.SpecificationsModels(shape)
                        for (k in 0 until j.aerialExcavationSpecificationsModels.size) {
                            mAdapter.mData[k].twoColumnDisplayContent2 =
                                j.aerialExcavationSpecificationsModels[k].quantity.toString()
                        }
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    //回填物比例
                    expandList[10].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.BackfillProportion()
                        mAdapter.mData[1].twoDisplayContent2 = j.aerialPoleBackfillProportion.soil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = j.aerialPoleBackfillProportion.girt.toString()
                        mAdapter.mData[3].twoDisplayContent2 = j.aerialPoleBackfillProportion.mountain.toString()
                        mAdapter.mData[4].twoDisplayContent2 = j.aerialPoleBackfillProportion.detritus.toString()
                        mAdapter.mData[5].twoDisplayContent2 = j.aerialPoleBackfillProportion.dust.toString()
                        mAdapter.mData[6].twoDisplayContent2 = j.aerialPoleBackfillProportion.pieceStone.toString()
                        mAdapter.mData[7].twoDisplayContent2 = j.aerialPoleBackfillProportion.beton.toString()
                        mAdapter.mData[8].twoDisplayContent2 = j.aerialPoleBackfillProportion.others.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.aerialTowerPitExcavation.photoPath != null) {
                        UnSerializeDataBase.imgList.add(
                            BitmapMap(
                                j.aerialTowerPitExcavation.photoPath.toString(),
                                "photoPath"
                            )
                        )
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j.aerialTowerPitExcavation)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialTowerPitExcavation)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "基坑选择:${j.aerialTowerPitExcavation.polePitChoice}", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialWeldMake(towerSubitemId: String, baseUrl: String, adapter: RecyclerviewAdapter) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialWeldMake(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.designQuantity.toString()
                        )
                    )
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.materialsName, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialPoleTowerAssemblage(
        towerSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {

        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialPoleTowerAssemblage(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()

                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.designQuantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "备注:", j.comment))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialPoleTowerAssemblage)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.materialsName, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialEconomy(
        aerialId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment,
        adapterGenerate: AdapterGenerate,
        ProjectAttribute: String
    ) {
        val data = mData.toMutableList()
        val result =
            getOverHeadEconomy(aerialId, baseUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val entity1 = it.message.aerialTwentyKvTypes
                    val entity2 = it.message.aerialThirtyfiveKvTypes
                    var aerialSoilTextureProportion = it.message.aerialSoilTextureProportion
                    val aerialTerrainProportion = it.message.aerialTerrainProportion
                    data[3].fourDisplayContent2 = it.message.manpowerDesignQuantity.toString()
                    data[3].fourDisplayContent3 = it.message.manpowerActualQuantity.toString()
                    data[4].fourDisplayContent2 = it.message.tractorsDesignQuantity.toString()
                    data[4].fourDisplayContent3 = it.message.tractorsActualQuantity.toString()
                    data[5].fourDisplayContent2 = it.message.carDesignQuantity.toString()
                    data[5].fourDisplayContent3 = it.message.carActualQuantity.toString()
                    data[6].fourDisplayContent2 = it.message.shipDesignQuantity.toString()
                    data[6].fourDisplayContent3 = it.message.shipActualQuantity.toString()
                    data[7].fourDisplayContent2 = it.message.ropewayDesignQuantity.toString()
                    data[7].fourDisplayContent3 = it.message.ropewayActualQuantity.toString()
                    data[9].fourDisplayListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.SoilRatio()
                        mAdapter.mData[1].twoDisplayContent2 = aerialSoilTextureProportion.ordinarySoil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = aerialSoilTextureProportion.sanSoil.toString()
                        mAdapter.mData[3].twoDisplayContent2 = aerialSoilTextureProportion.looseSand.toString()
                        mAdapter.mData[4].twoDisplayContent2 = aerialSoilTextureProportion.muddyWaterHole.toString()
                        mAdapter.mData[5].twoDisplayContent2 = aerialSoilTextureProportion.waterHole.toString()
                        mAdapter.mData[6].twoDisplayContent2 = aerialSoilTextureProportion.driftSandHole.toString()
                        mAdapter.mData[7].twoDisplayContent2 = aerialSoilTextureProportion.drySandHole.toString()
                        mAdapter.mData[8].twoDisplayContent2 = aerialSoilTextureProportion.dynamiteRock.toString()
                        mAdapter.mData[9].twoDisplayContent2 = aerialSoilTextureProportion.manuRock.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    data[10].fourDisplayListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.TerrainRatio()
                        mAdapter.mData[1].twoDisplayContent2 = aerialTerrainProportion.flat.toString()
                        mAdapter.mData[2].twoDisplayContent2 = aerialTerrainProportion.hill.toString()
                        mAdapter.mData[3].twoDisplayContent2 = aerialTerrainProportion.generalMountainsRegion.toString()
                        mAdapter.mData[4].twoDisplayContent2 = aerialTerrainProportion.mire.toString()
                        mAdapter.mData[5].twoDisplayContent2 = aerialTerrainProportion.desert.toString()
                        mAdapter.mData[6].twoDisplayContent2 = aerialTerrainProportion.hignMountain.toString()
                        mAdapter.mData[7].twoDisplayContent2 = aerialTerrainProportion.mountainsRegion.toString()
                        mAdapter.mData[8].twoDisplayContent2 = aerialTerrainProportion.riveNetwork.toString()
                        if (ProjectAttribute != "主网工程") {
                            val data = mAdapter.mData.toMutableList()
                            for (j in 0 until mAdapter.mData.size) {
                                if (mAdapter.mData[j].twoDisplayTitle in listOf("沙漠", "河网"))
                                    data.removeAt(j)
                            }
                            mAdapter.mData = data.toList()
                        }
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    data[11].fourDisplayListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val data: MutableList<MultiStyleItem> = ArrayList()
                        data.add(MultiStyleItem(MultiStyleItem.Options.TWO_DISPLAY, "20Kv及以下跨越类型", "单位", "数量"))
                        var sun: Long = 0
                        for (j in entity1) {
                            sun += j.quantity
                            data.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.TWO_DISPLAY,
                                    j.twentyKvType,
                                    j.unit,
                                    j.quantity.toString()
                                )
                            )
                        }
                        data.add(MultiStyleItem(MultiStyleItem.Options.TWO_DISPLAY, "小计", "", sun.toString()))
                        view.rv_soil_ratio_content.adapter = RecyclerviewAdapter(data)
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    data[12].fourDisplayListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val data: MutableList<MultiStyleItem> = ArrayList()
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.FIVE_DISPLAY,
                                "35Kv及以上跨越类型",
                                "单根线",
                                "35Kv",
                                "110Kv",
                                "220Kv"
                            )
                        )
                        for (j in entity2) {
                            data.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.FIVE_DISPLAY,
                                    j.thirtyFiveKvType,
                                    j.simpleWire.toString(),
                                    j.thirtyFiveKv.toString(),
                                    j.oneHundredTenKv.toString(),
                                    j.twoHundredTwentyKv.toString()
                                )
                            )
                        }
                        view.rv_soil_ratio_content.adapter = RecyclerviewAdapter(data)
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    adapter.mData = data
                    adapter.notifyDataSetChanged()
                }
    }

    fun getDataNodeEconomy(
        cableId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment,
        adapterGenerate: AdapterGenerate,
        ProjectAttribute: String
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result =
            getNodeEconomy(cableId, baseUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    var soilTextureProportion = it.message.soilTextureProportion
                    val terrainProportion = it.message.terrainProportion
                    data[3].fourDisplayContent2 = it.message.manpowerDesignQuantity.toString()
                    data[3].fourDisplayContent3 = it.message.manpowerActualQuantity.toString()
                    data[4].fourDisplayContent2 = it.message.carDesignQuantity.toString()
                    data[4].fourDisplayContent3 = it.message.carActualQuantity.toString()
                    data[5].fourDisplayContent2 = it.message.shipDesignQuantity.toString()
                    data[5].fourDisplayContent3 = it.message.shipActualQuantity.toString()
                    data[6].fourDisplayContent2 = it.message.ropewayDesignQuantity.toString()
                    data[6].fourDisplayContent3 = it.message.ropewayActualQuantity.toString()
                    data[8].fourDisplayListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.SoilRatio()
                        mAdapter.mData[1].twoDisplayContent2 = soilTextureProportion.ordinarySoil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = soilTextureProportion.sanSoil.toString()
                        mAdapter.mData[3].twoDisplayContent2 = soilTextureProportion.looseSand.toString()
                        mAdapter.mData[4].twoDisplayContent2 = soilTextureProportion.muddyWaterHole.toString()
                        mAdapter.mData[5].twoDisplayContent2 = soilTextureProportion.waterHole.toString()
                        mAdapter.mData[6].twoDisplayContent2 = soilTextureProportion.driftSandHole.toString()
                        mAdapter.mData[7].twoDisplayContent2 = soilTextureProportion.drySandHole.toString()
                        mAdapter.mData[8].twoDisplayContent2 = soilTextureProportion.dynamiteRock.toString()
                        mAdapter.mData[9].twoDisplayContent2 = soilTextureProportion.manuRock.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    data[9].fourDisplayListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.TerrainRatio()
                        mAdapter.mData[1].twoDisplayContent2 = terrainProportion.flat.toString()
                        mAdapter.mData[2].twoDisplayContent2 = terrainProportion.hill.toString()
                        mAdapter.mData[3].twoDisplayContent2 = terrainProportion.generalMountainsRegion.toString()
                        mAdapter.mData[4].twoDisplayContent2 = terrainProportion.mire.toString()
                        mAdapter.mData[5].twoDisplayContent2 = terrainProportion.desert.toString()
                        mAdapter.mData[6].twoDisplayContent2 = terrainProportion.hignMountain.toString()
                        mAdapter.mData[7].twoDisplayContent2 = terrainProportion.mountainsRegion.toString()
                        mAdapter.mData[8].twoDisplayContent2 = terrainProportion.riveNetwork.toString()
                        if (ProjectAttribute != "主网工程") {
                            val data = mAdapter.mData.toMutableList()
                            for (j in 0 until mAdapter.mData.size) {
                                if (mAdapter.mData[j].twoDisplayTitle in listOf("沙漠", "河网"))
                                    data.removeAt(j)
                            }
                            mAdapter.mData = data.toList()
                        }
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    adapter.mData = data
                    adapter.notifyDataSetChanged()
                }
    }

    fun getDataGalleryEconomy(
        cableId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment,
        adapterGenerate: AdapterGenerate,
        ProjectAttribute: String
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result =
            getGalleryEconomy(cableId, baseUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    var soilTextureProportion = it.message.soilTextureProportion
                    val terrainProportion = it.message.terrainProportion
                    data[3].fourDisplayContent2 = it.message.manpowerDesignQuantity.toString()
                    data[3].fourDisplayContent3 = it.message.manpowerActualQuantity.toString()
                    data[4].fourDisplayContent2 = it.message.carDesignQuantity.toString()
                    data[4].fourDisplayContent3 = it.message.carActualQuantity.toString()
                    data[5].fourDisplayContent2 = it.message.shipDesignQuantity.toString()
                    data[5].fourDisplayContent3 = it.message.shipActualQuantity.toString()
                    data[6].fourDisplayContent2 = it.message.ropewayDesignQuantity.toString()
                    data[6].fourDisplayContent3 = it.message.ropewayActualQuantity.toString()
                    data[8].fourDisplayListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.SoilRatio()
                        mAdapter.mData[1].twoDisplayContent2 = soilTextureProportion.ordinarySoil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = soilTextureProportion.sanSoil.toString()
                        mAdapter.mData[3].twoDisplayContent2 = soilTextureProportion.looseSand.toString()
                        mAdapter.mData[4].twoDisplayContent2 = soilTextureProportion.muddyWaterHole.toString()
                        mAdapter.mData[5].twoDisplayContent2 = soilTextureProportion.waterHole.toString()
                        mAdapter.mData[6].twoDisplayContent2 = soilTextureProportion.driftSandHole.toString()
                        mAdapter.mData[7].twoDisplayContent2 = soilTextureProportion.drySandHole.toString()
                        mAdapter.mData[8].twoDisplayContent2 = soilTextureProportion.dynamiteRock.toString()
                        mAdapter.mData[9].twoDisplayContent2 = soilTextureProportion.manuRock.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    data[9].fourDisplayListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.TerrainRatio()
                        mAdapter.mData[1].twoDisplayContent2 = terrainProportion.flat.toString()
                        mAdapter.mData[2].twoDisplayContent2 = terrainProportion.hill.toString()
                        mAdapter.mData[3].twoDisplayContent2 = terrainProportion.generalMountainsRegion.toString()
                        mAdapter.mData[4].twoDisplayContent2 = terrainProportion.mire.toString()
                        mAdapter.mData[5].twoDisplayContent2 = terrainProportion.desert.toString()
                        mAdapter.mData[6].twoDisplayContent2 = terrainProportion.hignMountain.toString()
                        mAdapter.mData[7].twoDisplayContent2 = terrainProportion.mountainsRegion.toString()
                        mAdapter.mData[8].twoDisplayContent2 = terrainProportion.riveNetwork.toString()
                        if (ProjectAttribute != "主网工程") {
                            val data = mAdapter.mData.toMutableList()
                            for (j in 0 until mAdapter.mData.size) {
                                if (mAdapter.mData[j].twoDisplayTitle in listOf("沙漠", "河网"))
                                    data.removeAt(j)
                            }
                            mAdapter.mData = data.toList()
                        }
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    adapter.mData = data
                    adapter.notifyDataSetChanged()
                }
    }

    fun getDataAerialFoundationCasting(
        towerSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialFoundationCasting(towerSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val datas: MutableList<MultiStyleItem> = ArrayList()
                val entity1 = it.message.aerialCastingFirsts
                for (j in entity1) {
                    var isExit = false
                    var data2: MutableList<MultiStyleItem> = ArrayList()
                    for (k in datas) {
                        if (k.expandListTitle == j.addContent) {
                            val position = datas.indexOf(k)
                            val data1 = k.expandListAdapter.mData.toMutableList()
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "垫层方量(m³):",
                                    j.cushionQuantity.toString()
                                )
                            )
                            data2.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "模板类型:", j.templateType))
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "基坑位置:",
                                    j.foundationPitLocation
                                )
                            )
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "主体混凝土类型:",
                                    j.mainBetonType
                                )
                            )
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "主体混凝土标号:",
                                    j.mainBetonNumber
                                )
                            )
                            data2.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "浇制类型:", j.castType))
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "主体混凝土方量(m³):",
                                    j.mainBetonQuantity.toString()
                                )
                            )
                            if (j.rebarMake != null)
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "钢筋制作(t):",
                                        j.rebarMake.toString()
                                    )
                                )
                            if (j.rebarInstall != null)
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "钢筋安装(t):",
                                        j.rebarInstall.toString()
                                    )
                                )
                            if (j.footBoltInstall != null)
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "地脚螺栓安装(t):",
                                        j.footBoltInstall.toString()
                                    )
                                )
                            //照片上次
                            data2.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                            data2[data2.size - 1].jumpListener = View.OnClickListener {
                                val data = Bundle()
                                data.putString("title", "照片上传")
                                data.putString("key", "photoPath")
                                data.putSerializable("serializable", j)
                                data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialCastingFirst)
                                (fragment.activity as ProfessionalActivity).switchFragment(
                                    ImageFragment.newInstance(data),
                                    "Capture"
                                )
                            }
                            data1.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.EXPAND_LIST, j.cushionType, "0",
                                    RecyclerviewAdapter(data2)
                                )
                            )
                            datas[position].expandListAdapter.mData = data1
                            isExit = true
                            break
                        }
                    }
                    if (!isExit) {
                        data2.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "垫层方量(m³):",
                                j.cushionQuantity.toString()
                            )
                        )
                        data2.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "模板类型:", j.templateType))
                        data2.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "基坑位置:",
                                j.foundationPitLocation
                            )
                        )
                        data2.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "主体混凝土类型:", j.mainBetonType))
                        data2.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "主体混凝土标号:", j.mainBetonNumber))
                        data2.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "浇制类型:", j.castType))
                        data2.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "主体混凝土方量(m³):",
                                j.mainBetonQuantity.toString()
                            )
                        )
                        if (j.rebarMake != null)
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "钢筋制作(t):",
                                    j.rebarMake.toString()
                                )
                            )
                        if (j.rebarInstall != null)
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "钢筋安装(t):",
                                    j.rebarInstall.toString()
                                )
                            )
                        if (j.footBoltInstall != null)
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "地脚螺栓安装(t):",
                                    j.footBoltInstall.toString()
                                )
                            )
                        //照片上次
                        data2.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                        data2[data2.size - 1].jumpListener = View.OnClickListener {
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialCastingFirst)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val data1: MutableList<MultiStyleItem> = ArrayList()
                        data1.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "垫层类型:" + j.cushionType, "0",
                                RecyclerviewAdapter(data2)
                            )
                        )
                        datas.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, j.addContent, "0",
                                RecyclerviewAdapter(data1)
                            )
                        )
                    }
                }
                val entity2 = it.message.aerialCastingSecondDTOS
                for (j in entity2) {
                    var isExit = false
                    val aerialCastingBeton = j.aerialCastingBeton
                    val aerialCastingMasonry = j.aerialCastingMasonry
                    val data2: MutableList<MultiStyleItem> = ArrayList()
                    for (k in datas) {
                        if (k.expandListTitle == j.aerialCastingSecond.addContent) {
                            val position = datas.indexOf(k)
                            val data1 = k.expandListAdapter.mData.toMutableList()
                            if (aerialCastingBeton != null) {

                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "模板类型:",
                                        aerialCastingBeton.templateType
                                    )
                                )
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "混凝土类型:",
                                        aerialCastingBeton.betonType
                                    )
                                )
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "混凝土标号:",
                                        aerialCastingBeton.betonNumber
                                    )
                                )
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "浇制类型:",
                                        aerialCastingBeton.pourType
                                    )
                                )
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "混凝土方量(m³):",
                                        aerialCastingBeton.betonQuantity.toString()
                                    )
                                )
                                if (aerialCastingBeton.rebarMake != null)
                                    data2.add(
                                        MultiStyleItem(
                                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                            "钢筋制作(t):",
                                            aerialCastingBeton.rebarMake.toString()
                                        )
                                    )
                                if (aerialCastingBeton.rebarInstall != null)
                                    data2.add(
                                        MultiStyleItem(
                                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                            "钢筋安装(t):",
                                            aerialCastingBeton.rebarInstall.toString()
                                        )
                                    )
                                if (aerialCastingBeton.hangingNet != null)
                                    data2.add(
                                        MultiStyleItem(
                                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                            "挂网(t):",
                                            aerialCastingBeton.hangingNet.toString()
                                        )
                                    )
                                //照片上次
                                data2.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                                data2[data2.size - 1].jumpListener = View.OnClickListener {
                                    val data = Bundle()
                                    data.putString("title", "照片上传")
                                    data.putString("key", "photoPath")
                                    data.putSerializable("serializable", aerialCastingBeton)
                                    data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialCastingBeton)
                                    (fragment.activity as ProfessionalActivity).switchFragment(
                                        ImageFragment.newInstance(
                                            data
                                        ), "Capture"
                                    )
                                }
                            } else if (aerialCastingMasonry != null) {
                                val masonryShape =
                                    if (aerialCastingMasonry.masonryShape == "1".toLong())
                                        "锥形"
                                    else
                                        "斜坡形"
                                data2.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "砌筑形状:", masonryShape))
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "砂浆比例:",
                                        aerialCastingMasonry.mortarProportion.toString()
                                    )
                                )
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "砌筑方量(m³):",
                                        aerialCastingMasonry.masonryQuantity.toString()
                                    )
                                )
                                data2.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                                data2[data2.size - 1].jumpListener = View.OnClickListener {
                                    val data = Bundle()
                                    data.putString("title", "照片上传")
                                    data.putString("key", "photoPath")
                                    data.putSerializable("serializable", aerialCastingMasonry)
                                    data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialCastingMasonry)
                                    (fragment.activity as ProfessionalActivity).switchFragment(
                                        ImageFragment.newInstance(
                                            data
                                        ), "Capture"
                                    )
                                }
                            }
                            data1.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.EXPAND_LIST, "浇砌筑方式:" + j.aerialCastingSecond.masonryWay, "0",
                                    RecyclerviewAdapter(data2)
                                )
                            )
                            datas[position].expandListAdapter.mData = data1
                            isExit = true
                            break
                        }
                    }
                    if (!isExit) {
                        if (aerialCastingBeton != null) {

                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "模板类型:",
                                    aerialCastingBeton.templateType
                                )
                            )
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "混凝土类型:",
                                    aerialCastingBeton.betonType
                                )
                            )
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "混凝土标号:",
                                    aerialCastingBeton.betonNumber
                                )
                            )
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "浇制类型:",
                                    aerialCastingBeton.pourType
                                )
                            )
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "混凝土方量(m³):",
                                    aerialCastingBeton.betonQuantity.toString()
                                )
                            )
                            if (aerialCastingBeton.rebarMake != null)
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "钢筋制作(t):",
                                        aerialCastingBeton.rebarMake.toString()
                                    )
                                )
                            if (aerialCastingBeton.rebarInstall != null)
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "钢筋安装(t):",
                                        aerialCastingBeton.rebarInstall.toString()
                                    )
                                )
                            if (aerialCastingBeton.hangingNet != null)
                                data2.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                        "挂网(t):",
                                        aerialCastingBeton.hangingNet.toString()
                                    )
                                )
                            //照片上次
                            data2.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                            data2[data2.size - 1].jumpListener = View.OnClickListener {
                                val data = Bundle()
                                data.putString("title", "照片上传")
                                data.putString("key", "photoPath")
                                data.putSerializable("serializable", aerialCastingBeton)
                                data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialCastingBeton)
                                (fragment.activity as ProfessionalActivity).switchFragment(
                                    ImageFragment.newInstance(data),
                                    "Capture"
                                )
                            }
                        } else if (aerialCastingMasonry != null) {
                            val masonryShape =
                                if (aerialCastingMasonry.masonryShape == "1".toLong())
                                    "锥形"
                                else
                                    "斜坡形"
                            data2.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "砌筑形状:", masonryShape))
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "砂浆比例:",
                                    aerialCastingMasonry.mortarProportion.toString()
                                )
                            )
                            data2.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "砌筑方量(m³):",
                                    aerialCastingMasonry.masonryQuantity.toString()
                                )
                            )
                            //照片上次
                            data2.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                            data2[data2.size - 1].jumpListener = View.OnClickListener {
                                val data = Bundle()
                                data.putString("title", "照片上传")
                                data.putString("key", "photoPath")
                                data.putSerializable("serializable", aerialCastingMasonry)
                                data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialCastingMasonry)
                                (fragment.activity as ProfessionalActivity).switchFragment(
                                    ImageFragment.newInstance(data),
                                    "Capture"
                                )
                            }
                        }

                        val data1: MutableList<MultiStyleItem> = ArrayList()
                        data1.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "浇砌筑方式:" + j.aerialCastingSecond.masonryWay, "0",
                                RecyclerviewAdapter(data2)
                            )
                        )
                        datas.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, j.aerialCastingSecond.addContent, "0",
                                RecyclerviewAdapter(data1)
                            )
                        )
                    }
                }
                data.addAll(datas)
                val aerialCastingFoundationFences = it.message.aerialCastingFoundationFences
                val data1: MutableList<MultiStyleItem> = ArrayList()
                for (j in aerialCastingFoundationFences) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "数量:", j.quantity.toString()))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialCastingFoundationFence)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    data1.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, j.fenceType, "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                }
                data.add(
                    MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, "防护类型:基础防护", "0",
                        RecyclerviewAdapter(data1)
                    )
                )
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataAerialGroundConnectionLay(
        aerialId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment,
        adapterGenerate: AdapterGenerate
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getAerialGroundConnectionLay(aerialId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                //接地沟开挖
                var entity = it.message.aerialGroundDitchExcavationDTOS
                if (entity.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "接地沟开挖",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )

                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "长:",
                            j.lengthD.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "宽:",
                            j.widthW.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "深:",
                            j.heightH.toString()
                        )
                    )
                    val excavationWay =
                        if (j.excavationWay == "0".toLong())
                            "人工"
                        else
                            "机械"
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "土质比例:", "详情"))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    expandList[expandList.size - 2].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.SoilRatio()
                        mAdapter.mData[1].twoDisplayContent2 = j.aerialGroundSoilProportion.ordinarySoil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = j.aerialGroundSoilProportion.sanSoil.toString()
                        mAdapter.mData[3].twoDisplayContent2 = j.aerialGroundSoilProportion.looseSand.toString()
                        mAdapter.mData[4].twoDisplayContent2 = j.aerialGroundSoilProportion.muddyWaterHole.toString()
                        mAdapter.mData[5].twoDisplayContent2 = j.aerialGroundSoilProportion.waterHole.toString()
                        mAdapter.mData[6].twoDisplayContent2 = j.aerialGroundSoilProportion.driftSandHole.toString()
                        mAdapter.mData[7].twoDisplayContent2 = j.aerialGroundSoilProportion.drySandHole.toString()
                        mAdapter.mData[8].twoDisplayContent2 = j.aerialGroundSoilProportion.dynamiteRock.toString()
                        mAdapter.mData[9].twoDisplayContent2 = j.aerialGroundSoilProportion.manuRock.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialGroundDitchExcavation)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "开挖方式:" + excavationWay,
                            "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }

                //接地极制作及安装
                var entity1 = it.message.aerialGroundEarthPoles
                if (entity1.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "接地极制作及安装",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity1) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.quantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialGroundEarthPole)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.materialsName, "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }

                //接地母线敷设
                var entity2 = it.message.aerialGroundGeneratrixLays
                if (entity2.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "接地母线敷设",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity2) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(m):",
                            j.quantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialGroundGeneratrixLay)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.materialsName, "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata

                }

                //引下安装
                var entity3 = it.message.aerialGroundDownInstalls
                if (entity3.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "引下安装",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity3) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(m):",
                            j.quantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialGroundDownInstall)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.materialsName, "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }


                //接地体安装
                var entity4 = it.message.aerialGroundBodyInstalls
                if (entity4.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "接地体安装",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity4) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量:",
                            j.quantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialGroundBodyInstall)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.materialsName, "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata

                }


                //接地防腐
                val entity5 = it.message.aerialGroundAczoilings
                if (entity5.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "接地防腐",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity5) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModels
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量:",
                            j.quantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateAerialGroundAczoiling)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "防腐方式:", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata

                }

                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataListFacility(nodeSubitemId: String, baseUrl: String, adapter: RecyclerviewAdapter) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getListFacility(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModel
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.quantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.HINT, "备注:\n${j.remark}"))
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.materialName, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataNodeHouseholdTableInstallation(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getNodeHouseholdTableInstallation(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message.listMeterMaterials
                if (entity.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "户表安装",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModel
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "数量(${j.unit}):",
                            j.designQuantity.toString()
                        )
                    )
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.materialName, "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }


                val entity1 = it.message.meterIndexRegisters
                if (entity1.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "表计编号登记",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity1) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "原表计号:", j.oldMeterInde))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "原表计号照片上传:", false))
                    if (j.oldPhotoPath != null) {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.oldPhotoPath.toString(), "oldPhotoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "原表计号照片上传")
                        data.putString("key", "oldPhotoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateMeterIndexRegister)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "新表计号:", j.newMeterInde))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "新表计号照片上传:", false))
                    if (j.newPhotoPath != null) {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.newPhotoPath.toString(), "newPhotoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "新表计号照片上传")
                        data.putString("key", "newPhotoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateMeterIndexRegister)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "${it.message.meterIndexRegisters.indexOf(j) + 1}#", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }

                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataNodePreformedUnitMakeInstallation(
        id: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getNodePreformedUnitMakeInstallation(id, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message.makeVolumes
                if (entity.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "制作方量",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "构件型号:",
                            j.componentModel
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "钢筋型号:",
                            j.rebarModel
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "钢筋重量(t):",
                            j.rebarWeight.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "预埋铁件(t):",
                            j.preBuriedIron.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传:", false))

                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        if (j.photoPath != null && j.photoPath != "") {
                            UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                        }
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateMakeVolume)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "构件名称:${j.name}", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }


                val entity1 = it.message.installVolumes
                if (entity1.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "安装方量",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity1) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "构件型号:",
                            j.componentModel
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "构件类型:",
                            j.kind
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "预埋铁件(t):",
                            j.preBuriedIron.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传:", false))

                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        if (j.photoPath != null && j.photoPath != "") {
                            UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                        }
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateInstallVolume)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "构件名称:${j.name}", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }

                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataNodeGroundingInstallation(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment,
        adapterGenerate: AdapterGenerate
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getNodeGroundingInstallation(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                //接地沟开挖
                var entity = it.message.trenchExcavationDTOS
                if (entity != null) {
                    if (entity.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "接地沟开挖",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )

                    for (j in entity) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "长:",
                                j.length.toString()
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "宽:",
                                j.wide.toString()
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "深:",
                                j.deep.toString()
                            )
                        )

                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "土质比例:", "详情"))
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                        expandList[expandList.size - 2].singleDisplayLeftListener = View.OnClickListener {
                            val builder = AlertDialog.Builder(fragment.view!!.context)
                            builder.setNegativeButton("返回", null)
                            builder.setCancelable(true)
                            val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                            val mAdapter = adapterGenerate.SoilRatio()
                            mAdapter.mData[1].twoDisplayContent2 =
                                j.trenchExcavationSoilTextureProportion.ordinarySoil.toString()
                            mAdapter.mData[2].twoDisplayContent2 =
                                j.trenchExcavationSoilTextureProportion.sanSoil.toString()
                            mAdapter.mData[3].twoDisplayContent2 =
                                j.trenchExcavationSoilTextureProportion.looseSand.toString()
                            mAdapter.mData[4].twoDisplayContent2 =
                                j.trenchExcavationSoilTextureProportion.muddyWaterHole.toString()
                            mAdapter.mData[5].twoDisplayContent2 =
                                j.trenchExcavationSoilTextureProportion.waterHole.toString()
                            mAdapter.mData[6].twoDisplayContent2 =
                                j.trenchExcavationSoilTextureProportion.driftSandHole.toString()
                            mAdapter.mData[7].twoDisplayContent2 =
                                j.trenchExcavationSoilTextureProportion.drySandHole.toString()
                            mAdapter.mData[8].twoDisplayContent2 =
                                j.trenchExcavationSoilTextureProportion.dynamiteRock.toString()
                            mAdapter.mData[9].twoDisplayContent2 =
                                j.trenchExcavationSoilTextureProportion.manuRock.toString()
                            view.rv_soil_ratio_content.adapter = mAdapter
                            view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                            builder.setView(view)
                            val dialog = builder.create()
                            dialog.show()
                        }
                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateTrenchExcavation)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "开挖方式:${j.way}",
                                "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata
                    }
                }


                //接地极制作及安装
                var entity1 = it.message.groundingElectrodeMakeInstalls
                if (entity1 != null) {
                    if (entity1.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "接地极制作及安装",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity1) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "规格型号:",
                                j.specificationsModel
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "数量(${j.quantityUnit}):",
                                j.quantity.toString()
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))

                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString(
                                "baseUrl",
                                Constants.HttpUrlPath.Professional.updateGroundingElectrodeMakeInstall
                            )
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.materialsName, "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata
                    }
                }


                //接地母线敷设
                var entity2 = it.message.groundingBusLayings
                if (entity2 != null) {
                    if (entity2.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "接地母线敷设",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity2) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "规格型号:",
                                j.specificationsModel
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "数量(m):",
                                j.quantity.toString()
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))

                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateGroundingBusLaying)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.materialsName, "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata

                    }
                }


                //引下安装
                var entity3 = it.message.underInstallations
                if (entity3 != null) {
                    if (entity3.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "引下安装",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity3) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "规格型号:",
                                j.specificationsModel
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "数量(m):",
                                j.quantity.toString()
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))

                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateUnderInstallation)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.materialsName, "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata
                    }
                }


                //接地体安装
                var entity4 = it.message.groundingInstalls
                if (entity4 != null) {
                    if (entity4.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "接地体安装",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity4) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "规格型号:",
                                j.specificationsModel
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "数量:",
                                j.quantity.toString()
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateGroundingInstall)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "物料名称:" + j.materialsName, "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata

                    }
                }


                //接地防腐
                val entity5 = it.message.groundingAnticorrosions
                if (entity5 != null) {
                    if (entity5.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "接地防腐",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity5) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "规格型号:",
                                j.specificationsModel
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "数量:",
                                j.quantity.toString()
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))

                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateGroundingAnticorrosion)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "防腐方式:", "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata

                    }
                }

                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }


    fun getDataNodeBasisPouring(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getNodeBasisPouring(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                //基层恢复
                var entity = it.message.primaryLevelRecovers
                if (entity != null) {
                    if (entity.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "基层恢复",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )

                    for (j in entity) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "基层类型:", j.kind))
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "施工方式:",
                                j.constructionForm
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updatePrimaryLevelRecover)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "${entity.indexOf(j) + 1}#",
                                "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata
                    }
                }


                //面层恢复
                var entity1 = it.message.surfaceRecovers
                if (entity1 != null) {
                    if (entity1.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "面层恢复",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity1) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "面层类型:", j.kind))
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "施工方式:",
                                j.constructForm
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))

                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateSurfaceRecover)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "${entity1.indexOf(j) + 1}#", "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata
                    }
                }


                //砖砌体
                var entity2 = it.message.brickSettings
                if (entity2 != null) {
                    if (entity2.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "砖砌体",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity2) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "砖块型号:",
                                j.model
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "砌灰标号:",
                                j.grayingLabel
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))

                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateBrickSetting)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "${entity2.indexOf(j) + 1}#", "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata

                    }
                }


                //路缘石
                var entity3 = it.message.curbs
                if (entity3 != null) {
                    if (entity3.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "路缘石",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity3) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "基层类型:",
                                j.kind
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "施工方式:",
                                j.constructForm
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))

                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateCurb)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "${entity3.indexOf(j) + 1}#", "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata
                    }
                }


                //装模
                var entity4 = it.message.loadMoldDTOs
                if (entity4 != null) {
                    if (entity4.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "装模",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity4) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "模板类型:",
                                j.kind
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "施工方式:",
                                j.constructForm
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateLoadMold)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "${entity4.indexOf(j) + 1}#", "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata

                    }
                }


                //抹灰
                val entity5 = it.message.plasterers
                if (entity5 != null) {
                    if (entity5.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "抹灰",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity5) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "抹灰类型:",
                                j.kind
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "抹面面积(㎡):",
                                j.area.toString()
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "灰浆类型:",
                                j.mortarType
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "灰浆标号:",
                                j.mortarMark
                            )
                        )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))

                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updatePlasterer)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "${entity5.indexOf(j) + 1}#", "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata

                    }
                }

                //混凝土浇筑
                val entity6 = it.message.concretePourings
                if (entity6 != null) {
                    if (entity6.size > 0)
                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST,
                                "混凝土浇筑",
                                "0",
                                RecyclerviewAdapter(ArrayList())
                            )
                        )
                    for (j in entity6) {
                        val expandList: MutableList<MultiStyleItem> = ArrayList()
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "浇筑类型:",
                                j.pouringKind
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "标号:",
                                j.markNumber
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "方量(m³):",
                                j.volume.toString()
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "混凝土类型:",
                                j.concreteKind
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "浇筑方式:",
                                j.pouringWay
                            )
                        )
                        if (j.steelBarBindingIn10 != null)
                            expandList.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "钢筋捆扎10以内(t):",
                                    j.steelBarBindingIn10.toString()
                                )
                            )
                        if (j.steelBarBindingAbove10 != null)
                            expandList.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "钢筋捆扎10以上(t):",
                                    j.steelBarBindingAbove10.toString()
                                )
                            )
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))

                        expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                            if (j.photoPath != null && j.photoPath != "") {
                                UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                            }
                            val data = Bundle()
                            data.putString("title", "照片上传")
                            data.putString("key", "photoPath")
                            data.putSerializable("serializable", j)
                            data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateConcretePouring)
                            (fragment.activity as ProfessionalActivity).switchFragment(
                                ImageFragment.newInstance(data),
                                "Capture"
                            )
                        }
                        val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                        mdata.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.EXPAND_LIST, "${entity6.indexOf(j) + 1}#", "0",
                                RecyclerviewAdapter(expandList)
                            )
                        )
                        data[data.size - 1].expandListAdapter.mData = mdata

                    }
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataNodeBasisExcavation(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment,
        adapterGenerate: AdapterGenerate
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getNodeBasisExcavation(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                var entity = it.message.roadCuttings
                if (entity.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "路面切割",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "长度:",
                            j.roadCutting.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateRoadCutting)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()

                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "${entity.indexOf(j) + 1}#", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }

                var entity1 = it.message.breakingRoads
                if (entity1.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "破除路面",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity1) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "路面形式:", j.roadForm))
                    if (j.thickness != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "厚度(mm):",
                                j.thickness.toString()
                            )
                        )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "长(m):",
                            j.length.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "宽(m):",
                            j.wide.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "面积(㎡):",
                            j.area.toString()
                        )
                    )
                    if (j.artificialEarthMoving != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "人工运土(m³):",
                                j.artificialEarthMoving.toString()
                            )
                        )
                    if (j.artificialTransportDistance != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "运距(km):",
                                j.artificialTransportDistance.toString()
                            )
                        )
                    if (j.machineryEarthMoving != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "机械运土(m³):",
                                j.machineryEarthMoving.toString()
                            )
                        )
                    if (j.machineryTransportDistannce != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "运距(km):",
                                j.machineryTransportDistannce.toString()
                            )
                        )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.photoPath != null && j.photoPath != "") {
                        UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateBreakingRoad)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "${entity1.indexOf(j) + 1}#", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }
                var entity2 = it.message.earthRockExcavationDTOS
                if (entity2.size > 0)
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "土石方开挖",
                            "0",
                            RecyclerviewAdapter(ArrayList())
                        )
                    )
                for (j in entity2) {

                    val expandList: MutableList<MultiStyleItem> = ArrayList()

                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "开挖方式:",
                            j.earthRockExcavation.excavationForm
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "长(m):",
                            j.earthRockExcavation.length.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "宽(m):",
                            j.earthRockExcavation.wide.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "深(m):",
                            j.earthRockExcavation.deep.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "开挖方量(m³):",
                            j.earthRockExcavation.excavationVolume.toString()
                        )
                    )
                    if (j.earthRockExcavation.flatBasisVolume != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "平基方量(m³):",
                                j.earthRockExcavation.flatBasisVolume.toString()
                            )
                        )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "土质比例:", "详情"))
                    //土质比例
                    expandList[expandList.size - 1].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.NodeSoilRatio()
                        mAdapter.mData[1].twoDisplayContent2 = j.earthRockExcavationProportion.commonSoil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = j.earthRockExcavationProportion.hardSoil.toString()
                        mAdapter.mData[3].twoDisplayContent2 = j.earthRockExcavationProportion.looseSand.toString()
                        mAdapter.mData[4].twoDisplayContent2 = j.earthRockExcavationProportion.slitQuicksand.toString()
                        mAdapter.mData[5].twoDisplayContent2 = j.earthRockExcavationProportion.tjaele.toString()
                        mAdapter.mData[6].twoDisplayContent2 = j.earthRockExcavationProportion.rock.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    if (j.earthRockExcavation.artificialSoil != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "人工运土(m³):",
                                j.earthRockExcavation.artificialSoil.toString()
                            )
                        )
                    if (j.earthRockExcavation.manuTransport != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "运距(km):",
                                j.earthRockExcavation.manuTransport.toString()
                            )
                        )
                    if (j.earthRockExcavation.mechanicalSoil != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "机械运土(m³):",
                                j.earthRockExcavation.mechanicalSoil.toString()
                            )
                        )
                    if (j.earthRockExcavation.mechanicalTransport != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "运距(km):",
                                j.earthRockExcavation.mechanicalTransport.toString()
                            )
                        )
                    if (j.earthRockExcavation.backfillVolume != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "回填方量(m³):",
                                j.earthRockExcavation.backfillVolume.toString()
                            )
                        )
                    if (j.earthRockExcavation.backfillForm != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "回填方式:",
                                j.earthRockExcavation.backfillForm.toString()
                            )
                        )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "回填物比例:", "详情"))
                    //回填物比例
                    expandList[expandList.size - 1].singleDisplayLeftListener = View.OnClickListener {
                        val builder = AlertDialog.Builder(fragment.view!!.context)
                        builder.setNegativeButton("返回", null)
                        builder.setCancelable(true)
                        val view = View.inflate(fragment.view!!.context, R.layout.dialog_soil_ratio, null)
                        val mAdapter = adapterGenerate.BackfillProportion()
                        mAdapter.mData[1].twoDisplayContent2 = j.backfillProportion.soil.toString()
                        mAdapter.mData[2].twoDisplayContent2 = j.backfillProportion.sand.toString()
                        mAdapter.mData[3].twoDisplayContent2 = j.backfillProportion.mountainFlour.toString()
                        mAdapter.mData[4].twoDisplayContent2 = j.backfillProportion.macadam.toString()
                        mAdapter.mData[5].twoDisplayContent2 = j.backfillProportion.dust.toString()
                        mAdapter.mData[6].twoDisplayContent2 = j.backfillProportion.flag.toString()
                        mAdapter.mData[7].twoDisplayContent2 = j.backfillProportion.beton.toString()
                        mAdapter.mData[8].twoDisplayContent2 = j.backfillProportion.other.toString()
                        view.rv_soil_ratio_content.adapter = mAdapter
                        view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                    }
                    if (j.earthRockExcavation.breastBoard != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "挡土板(㎡):",
                                j.earthRockExcavation.breastBoard.toString()
                            )
                        )

                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    if (j.earthRockExcavation.photoPath != null) {
                        UnSerializeDataBase.imgList.add(
                            BitmapMap(
                                j.earthRockExcavation.photoPath.toString(),
                                "photoPath"
                            )
                        )
                    }
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateEarthRockExcavation)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val mdata = data[data.size - 1].expandListAdapter.mData.toMutableList()
                    mdata.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST, "${entity2.indexOf(j) + 1}#", "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                    data[data.size - 1].expandListAdapter.mData = mdata
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataNodeMaterial(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val data = mData.toMutableList()
        val oldSize = adapter.mData.size
        val result = getNodeMaterial(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message.material3DTOS
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    for (k in j.materials) {
                        expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "物料名称:", k.name))
                        if (k.specificationsModel != null)
                            expandList.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                    "规格型号:",
                                    k.specificationsModel.toString()
                                )
                            )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "单重(t):",
                                k.pieceWeight.toString()
                            )
                        )
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "数量:",
                                k.quantity.toString()
                            )
                        )
                    }
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "小计重量(t)",
                            j.materialTransportStatistics.aggregate.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        if (j.materialTransportStatistics.photoPath != null) {
                            UnSerializeDataBase.imgList.add(
                                BitmapMap(
                                    j.materialTransportStatistics.photoPath.toString(),
                                    "photoPath"
                                )
                            )
                        }
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j.materialTransportStatistics)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateMaterialTransportStatistics)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    val item = MultiStyleItem(
                        MultiStyleItem.Options.EXPAND_LIST, j.materialTransportStatistics.kind, "0",
                        RecyclerviewAdapter(expandList)
                    )
                    data.add(item)
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataCableHeadMake(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val oldSize = adapter.mData.size
        var data = adapter.mData.toMutableList()
        val result = getCableHeadMake(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message.cableHeadMakes
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "添加内容:", j.kind))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "电压等级:", j.voltageGrade))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModel
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "制作数量:",
                            j.makeingQuantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        if (j.photoPath != null) {
                            UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                        }
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateCableHeadMake)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "${entity.indexOf(j) + 1}#",
                            "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataCableFireroofing(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val oldSize = adapter.mData.size
        var data = adapter.mData.toMutableList()
        val result = getCableFireroofing(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message.cableFireroofingMaterialsTypes
                val cableFireroofing = it.message.cableFireroofing
                for (j in entity) {
                    val type = if (j.specificationsModel == null) "" else j.specificationsModel.toString()
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.FOUR_DISPLAY,
                            j.name,
                            j.unit,
                            type,
                            j.designQuantity.toString()
                        )
                    )
                }
                data.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                data[data.size - 1].jumpListener = View.OnClickListener {
                    if (cableFireroofing.photoPath != null) {
                        UnSerializeDataBase.imgList.add(BitmapMap(cableFireroofing.photoPath.toString(), "photoPath"))
                    }
                    val data = Bundle()
                    data.putString("title", "照片上传")
                    data.putString("key", "photoPath")
                    data.putSerializable("serializable", cableFireroofing)
                    data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateCableFireroofing)
                    (fragment.activity as ProfessionalActivity).switchFragment(ImageFragment.newInstance(data), "Capture")
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataCableBridge(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val oldSize = adapter.mData.size
        var data = adapter.mData.toMutableList()
        val result = getCableBridge(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message.cableBridges
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "添加内容:", j.kind))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "铺设长度:",
                            j.layLength.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "型号:", j.model))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        if (j.photoPath != null) {
                            UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                        }
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateCableBridge)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "${entity.indexOf(j) + 1}#",
                            "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataCableLaying(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val oldSize = adapter.mData.size
        var data = adapter.mData.toMutableList()
        val result = getCableLaying(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message.cableLayings
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "添加内容:", j.kind))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "电压等级:", j.voltageGrade))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModel
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "敷设长度(km):",
                            j.layingLength.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "回路数(回):",
                            j.loopQuantity.toString()
                        )
                    )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        if (j.photoPath != null) {
                            UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                        }
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateCableLaying)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "${entity.indexOf(j) + 1}#",
                            "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataListCablePipe(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val oldSize = adapter.mData.size
        var data = adapter.mData.toMutableList()
        val result = getListCablePipe(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                val entity = it.message.listCablePipes
                for (j in entity) {
                    val expandList: MutableList<MultiStyleItem> = ArrayList()
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "添加内容:", j.kind))
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT, "配管类型:", j.pipeKind))
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "规格型号:",
                            j.specificationsModel
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "单孔长度(m):",
                            j.haploporeLength.toString()
                        )
                    )
                    expandList.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                            "孔数(孔):",
                            j.holeCount.toString()
                        )
                    )
                    if (j.concretePack != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "混凝土包装(m³):",
                                j.concretePack.toString()
                            )
                        )
                    if (j.length != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "长(m):",
                                j.length.toString()
                            )
                        )
                    if (j.wide != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "宽(m):",
                                j.wide.toString()
                            )
                        )
                    if (j.deep != null)
                        expandList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,
                                "深(m):",
                                j.deep.toString()
                            )
                        )
                    expandList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    expandList[expandList.size - 1].jumpListener = View.OnClickListener {
                        if (j.photoPath != null) {
                            UnSerializeDataBase.imgList.add(BitmapMap(j.photoPath.toString(), "photoPath"))
                        }
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", j)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateListCablePipe)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    data.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.EXPAND_LIST,
                            "${entity.indexOf(j) + 1}#",
                            "0",
                            RecyclerviewAdapter(expandList)
                        )
                    )
                }
                adapter.mData = data
                adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
            }
    }

    fun getDataCableTest(
        nodeSubitemId: String,
        baseUrl: String,
        adapter: RecyclerviewAdapter,
        fragment: ProjectMoreFragment
    ) {
        val oldSize = adapter.mData.size
        var data = adapter.mData.toMutableList()
        val result =
            getCableTest(nodeSubitemId, baseUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val entity = it.message.cableTestMaterialsTypes
                    var cableTest = it.message.cableTest
                    for (j in entity) {

                        data.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.FOUR_DISPLAY,
                                j.name,
                                j.voltageGrade,
                                j.unit,
                                j.designQuantity.toString()
                            )
                        )
                    }
                    data.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "照片上传", false))
                    data[data.size - 1].jumpListener = View.OnClickListener {
                        if (cableTest.photoPath != null) {
                            UnSerializeDataBase.imgList.add(BitmapMap(cableTest.photoPath.toString(), "photoPath"))
                        }
                        val data = Bundle()
                        data.putString("title", "照片上传")
                        data.putString("key", "photoPath")
                        data.putSerializable("serializable", cableTest)
                        data.putString("baseUrl", Constants.HttpUrlPath.Professional.updateCableTest)
                        (fragment.activity as ProfessionalActivity).switchFragment(
                            ImageFragment.newInstance(data),
                            "Capture"
                        )
                    }
                    adapter.mData = data
                    adapter.notifyItemRangeInserted(oldSize, adapter.mData.size - oldSize)
                }
    }

    fun updateData(data: Serializable, baseUrl: String, key: String, imagePath: String, fragment: ImageFragment) {
        val json = JSONObject(Gson().toJson(data))
        val result = Observable.create<RequestBody> {
            //json.remove(key)
            //val imagePath = upImage(key)
            //var jsonObject= json.put(key,upImage(key))
            var jsonObject = json.put(key, imagePath)
            when (baseUrl) {
                //节点 土方石开挖
                Constants.HttpUrlPath.Professional.updateEarthRockExcavation -> {
                    val js = json.getJSONObject("earthRockExcavation")
                    js.put(key, imagePath)
                    jsonObject = json.put("earthRockExcavation", js)
                    jsonObject.remove(key)
                }
                //电杆坑开挖
                Constants.HttpUrlPath.Professional.updateAerialPolePitExcavation -> {
                    jsonObject = JSONObject().put("aerialPolePitExcavation", jsonObject)
                }
                //拉线洞开挖
                Constants.HttpUrlPath.Professional.updateAerialStayguyHoleExcavation -> {
                    jsonObject = JSONObject().put("aerialStayguyHoleExcavation", jsonObject)
                }
                //塔坑开挖
                Constants.HttpUrlPath.Professional.updateAerialTowerPitExcavation -> {
                    jsonObject = JSONObject().put("aerialTowerPitExcavation", jsonObject)
                }
            }
            Log.i("json ,,", jsonObject.toString())
            val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result =
                putSimpleMessage(it, UnSerializeDataBase.dmsBasePath + baseUrl).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            if (JSONObject(it.string()).getInt("code") == 200) {
                                Toast.makeText(fragment.context, "照片上传成功", Toast.LENGTH_SHORT).show()
                                fragment.activity!!.supportFragmentManager.popBackStackImmediate()
                                UnSerializeDataBase.imgList.clear()
                            }
                        },
                        {
                            it.printStackTrace()
                        }
                    )
        }
    }

    fun uploadFile(filePath:String,fragment:Fragment){
        val loadingDialog = LoadingDialog(context,"正在上传...")
        loadingDialog.show()
        val map=HashMap<String, RequestBody>()
        map["nodeSubitemId"]=RequestBody.create(MultipartBody.FORM,"1")
        var fileName = ""
        val result= Observable.create<HashMap<String,RequestBody>>{
            val key = "file"
            val file= File(filePath)
            fileName = file.name
            val builder= MultipartBody.Builder()
            if (file.name.contains("jpg")||file.name.contains("png"))
            {
                val requestBody=RequestBody.create(MediaType.parse("image/*"),file)
                builder.addFormDataPart(key,file.name,requestBody)
            }
            else
            {
                val requestBody=RequestBody.create(MediaType.parse("application/octet-stream;charset=utf-8"),file)
                if (file.exists())
                    Log.i("file","exist")
                if (file.canRead())
                    Log.i("file","can read")
                builder.addFormDataPart(key, URLEncoder.encode(file.name,"utf-8"),requestBody)
            }
            map[key]=builder.build()
            it.onNext(map)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
               uploadFile(it).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe({
                        loadingDialog.dismiss()
                        //Log.i("retrofitMsg", it.string())
                        val json = JSONObject(it.string())
                        if(json.getBoolean("success")){
                            val filePath = json.getString("httpUrl")
                            if(fragment is SupplyFragment){
                                fragment.file(filePath)
                            }else if(fragment is SupplyModifyJobInformationFragment){
                                fragment.file(filePath)
                            }
                        }
//                        if (UnSerializeDataBase.imgList[0].needDelete) {
//                            val file = File(UnSerializeDataBase.imgList[0].path)
//                            file.delete()
//                        }
                        UnSerializeDataBase.fileList.clear()
                        ToastHelper.mToast(context,"上传成功！")
                    },
                        {
                            loadingDialog.dismiss()
                            ToastHelper.mToast(context,"网络异常！")
                            it.printStackTrace()
                        })
            }
    }
    fun upImage(imagePath: String,fragment:Fragment){
        val loadingDialog = LoadingDialog(context,"正在上传...")
        loadingDialog.show()
        var result = ""
        val file = File(imagePath)
        val imagePart = MultipartBody.Part.createFormData("file", file.name,
            RequestBody.create(MediaType.parse("image/*"), file))
        uploadImage(imagePart).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                loadingDialog.dismiss()
                val json = JSONObject(it.string())
                Log.i("json",json.toString())
                if(json.getBoolean("success")){
                    ToastHelper.mToast(context,"上传成功")
                    result = json.getString("httpUrl")
                    val mImagePaths = arrayListOf(result)
                    if(fragment is UploadPhoneFragment) {
                        fragment.refresh(mImagePaths[0])
                    }else if(fragment is UpIdCardFragment){
                        fragment.refresh(mImagePaths)
                    }else if(fragment is ImageFragment){
                        fragment.refresh(mImagePaths)
                    }else if(fragment is PersonalMaterialsFragment){
                        fragment.insertCertificate(mImagePaths[0])
                    }else if(fragment is PersonalCertificationFragment){
                        fragment.refresh(mImagePaths[0])
                    }else if(fragment is PersonalReCertificationFragment){
                        fragment.refresh(mImagePaths[0])
                    }else if(fragment is EnterpriseCertificationFragment){
                        fragment.refresh(mImagePaths[0])
                    }else if(fragment is EnterpriseReCertificationFragment){
                        fragment.refresh(mImagePaths[0])
                    }
                }else{
                    ToastHelper.mToast(context,"上传失败")
                }
            },
            {
                loadingDialog.dismiss()
                ToastHelper.mToast(context,"上传失败")
                it.printStackTrace()
            })
    }

    fun upImage(data: Serializable, baseUrl: String, key: String, fragment: ImageFragment): String {
        var result = ""
        val results = try {
            for (j in UnSerializeDataBase.imgList) {
                if (j.key == key) {
                    val ImagePath = j.path.split("|")
                    for (k in ImagePath) {
                        val file = File(k)
                        val imagePart = MultipartBody.Part.createFormData(
                            "file",
                            file.name,
                            RequestBody.create(MediaType.parse("image/*"), file)
                        )
                        uploadImage(imagePart).observeOn(AndroidSchedulers.mainThread()).subscribe(
                            {
                                //Log.i("responseBody",it.string())
                                if (result != "")
                                    result += "|"
                                result += it.string()
                                Log.i("result url", result)
                                if (result.split("|").size == ImagePath.size) {
                                    updateData(data, baseUrl, key, result, fragment)
                                }
                            },
                            {
                                if (result != "")
                                    result += "|"
                                result += file.path
                                Log.i("result", result)
                                it.printStackTrace()
                            })
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun check(): Boolean {
        var result = ""
        for (j in mData) {
            when (j.options) {
                MultiStyleItem.Options.SINGLE_INPUT -> {
                    if(j.necessary==true){//输入必选限制条件
                        when(j.inputSingleTitle){
                            "年龄","驾驶员年龄"->{//报名限制
                                if (j.inputSingleContent == "")
                                { result = "${j.inputSingleTitle.replace("：", "")}不能为空" }
                                else if(j.inputSingleContent.toInt()>60||j.inputSingleContent.toInt()<16)
                                { result = "请输入正确${j.inputSingleTitle.replace("：", "")}范围"}
                            }
                            "名称","规格型号","数量","单位","牌照号码","单价","姓名",
                            "报价清单(按单价)","个人证件名称","工作经验" ,
                            "项目","项目特征描述","计量单位",
                            "出租方单位名称","单位地址","单位名称","法人代表姓名","车辆数量","所在地址",
                            "公司名称","注册地址","经营范围"->{
                                if (j.inputSingleContent == "") { result = "${j.inputSingleTitle.replace("：", "")}不能为空" }
                            }
                        }
                    }else {
                        when (j.inputSingleTitle) {
                            "电话", "手机号码", "法人代表电话" -> {
                                if (j.inputSingleContent == "") {
                                    result = "${j.inputSingleTitle.replace("：", "")}不能为空"
                                } else if (j.inputSingleContent.length != 11) {
                                    result = "请输入正确的11位${j.inputSingleTitle.replace("：", "")}"
                                }
                            }
                            "身份证" -> {
                                if (j.inputSingleContent == "") {
                                    result = "${j.inputSingleTitle.replace("：", "")}不能为空"
                                } else if (j.inputSingleContent.length != 18) {
                                    result = "请输入正确的18位${j.inputSingleTitle.replace("：", "")}"
                                }
                            }
                            "企业注册号" -> {
                                if (j.inputSingleContent == "") {
                                    result = "${j.inputSingleTitle.replace("：", "")}不能为空"
                                } else if (j.inputSingleContent.length != 15 && j.inputSingleContent.length != 18) {
                                    result = "请输入正确的15位${j.inputSingleTitle.replace(
                                        "：",
                                        ""
                                    )}或18位统一社会信用代码"
                                }
                            }
                        }
                    }
                    if (result != "") {
                        ToastHelper.mToast(context,result)
                        return false
                    }
                }
                MultiStyleItem.Options.INPUT_RANGE->{
                          when (j.inputRangeTitle) {//此处为可选项但需检验
                            "年龄要求","驾驶员年龄" -> {
                                if (j.inputRangeValue1 != "" && j.inputRangeValue2 != "") {
                                    if (j.inputRangeValue1.toInt() >= j.inputRangeValue2.toInt() || j.inputRangeValue2.toInt() > 60 || j.inputRangeValue1.toInt() < 16) {
                                        result =
                                            "请输入正确${j.inputRangeTitle.replace("要求", "")}范围16-60岁"
                                    }
                                } else if (j.inputRangeValue1 == "" && j.inputRangeValue2 == "") {
                                    result = ""
                                } else {
                                    result = "如需填写${j.inputRangeTitle.replace("要求", "")}请填写完整"
                                }
                            }
                        }
                    if (result != "") {
                        ToastHelper.mToast(context,result)
                        return false
                    }
                }
                MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT->{
                    if(j.necessary==true) {//为true此项为必填需检验
                        when (j.singleDisplayRightTitle) {
                            "需要人数(人)" -> {
                                if (j.singleDisplayRightContent.toInt() == 0) {
                                    result = "请至少填写一条成员清册数据"
                                }
                            }
                            else -> {
                                if (j.singleDisplayRightTitle.contains("清册") && !j.SubmitIsNecessary) {
                                    result = "${j.singleDisplayRightTitle.replace("：", "")}没有填写完整"

                                } else {
                                    result = ""
                                }
                            }
                        }
                    }

                    if (result != "") {
                        ToastHelper.mToast(context,result)
                        return false
                    }
                }
                MultiStyleItem.Options.INPUT_WITH_UNIT->{
                    if(j.necessary==true) {//为true此项为必填需检验
                        when (j.inputUnitTitle) {
                            "工作经验" -> {//需求发布限制
                                if (j.inputUnitContent == "") {
                                    result = "${j.inputUnitTitle.replace("：", "")}不能为空"
                                } else if (j.inputUnitContent.toInt() < 0 || j.inputUnitContent.toInt() > 45) {
                                    result = "请输入正确${j.inputUnitTitle.replace("：", "")}范围0-45年"
                                }
                            }
                            "车辆数量"->{
                                if (j.inputUnitContent == "") {
                                    result = "${j.inputUnitTitle.replace("：", "")}不能为空"
                                } else if (j.inputUnitContent.toInt() < 1 || j.inputUnitContent.toInt() > 15) {
                                    result = "请输入正确${j.inputUnitTitle.replace("：", "")}范围1-15辆"
                                }
                            }
                            "发布有效期","有效期"->{
                                if (j.inputUnitContent == "") {
                                    result = "${j.inputUnitTitle.replace("：", "")}不能为空"
                                } else if (j.inputUnitContent.toInt() < 1 || j.inputUnitContent.toInt() > 90) {
                                    result = "请输入正确${j.inputUnitTitle.replace("：", "")}范围1-90天"
                                }
                            }
                            "计划工期", "需要桩基","需求人数","需要人数","马匹数量","施工工期","车辆","需要马匹" -> {
                                if (j.inputUnitContent == "") {
                                    result = "${j.inputUnitTitle.replace("：", "")}不能为空"
                                }
                            }
                            //报名车辆限制
                            "核载乘客"->{
                                if (j.inputUnitContent == "") {
                                    result = "${j.inputUnitTitle.replace("：", "")}不能为空"
                                } else if (j.inputUnitContent.toInt() < 0 || j.inputUnitContent.toInt() > 10) {
                                    result = "请输入正确${j.inputUnitTitle.replace("：", "")}范围0-10人"
                                }
                            }
                            "核准载重量"->{
                                if (j.inputUnitContent == "") {
                                    result = "${j.inputUnitTitle.replace("：", "")}不能为空"
                                } else if (j.inputUnitContent.toDouble() < 0 || j.inputUnitContent.toDouble() > 30) {
                                    result = "请输入正确${j.inputUnitTitle.replace("：", "")}范围0-30吨"
                                }
                            }
                            "车厢长度"->{
                                if (j.inputUnitContent == "") {
                                    result = "${j.inputUnitTitle.replace("：", "")}不能为空"
                                } else if (j.inputUnitContent.toDouble() < 0 || j.inputUnitContent.toDouble() > 15) {
                                    result = "请输入正确${j.inputUnitTitle.replace("：", "")}范围0-15米"
                                }
                            }
                        }
                    }
                    else {
                        when (j.inputUnitTitle) {
                            "带驾驶员","不带驾驶员"->{
                                if (j.inputUnitContent!=""&&(j.inputUnitContent.toDouble() < 1 || j.inputUnitContent.toDouble() > 1000)) {
                                    result = "请输入正确 ${j.inputUnitTitle.replace("：", "")} 范围1-1000元"
                                }
                            }
                            "年龄要求"->{
                                if (j.inputUnitContent!=""&&(j.inputUnitContent.toInt() < 16 || j.inputUnitContent.toInt() > 60)) {
                                    result = "请输入正确 ${j.inputUnitTitle.replace("：", "")} 范围16-60岁"
                                }
                            }
                        }
                    }
                    if (result != "") {
                        ToastHelper.mToast(context,result)
                        return false
                    }
                }
                MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT->{
                    if(j.necessary==true) {//为true此项为必填需检验
                        when (j.inputMultiUnitTitle) {
                            "薪资标准","薪资要求"->{
                                if (j.inputMultiContent == ""&&j.inputMultiSelectUnit!="面议") {
                                    result = "${j.inputMultiUnitTitle.replace("：", "")}不能为空"
                                }
                            }
                        }
                    }
                    if (result != "") {
                        ToastHelper.mToast(context,result)
                        return false
                    }
                }
                MultiStyleItem.Options.MULTI_BUTTON->
                {
                    if(j.buttonTitle.contains("清册")) {
                        if (j.necessary == false)
                            result = "${j.buttonTitle.replace("：", "")}没有填完整"
                        if (result != "") {
                            ToastHelper.mToast(context, result)
                            return false
                        }
                    }
                }
                MultiStyleItem.Options.TWO_PAIR_INPUT->{
                    if(j.necessary==true) {//为true此项为必填需检验
                        when (j.twoPairInputTitle) {
                            "规格条数" -> {
                                if (j.twoPairInputValue1 != "" && j.twoPairInputValue2 != "") {
                                    result = ""
                                } else {
                                    result = "${j.twoPairInputTitle.replace("：", "")}没有填完整"
                                }
                            }
                        }
                    }
                    if (result != "") {
                        ToastHelper.mToast(context, result)
                        return false
                    }
                }
                MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG,
                MultiStyleItem.Options.SELECT_DIALOG,
                MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG->{
                    if(j.necessary==true) {//为true此项为必填需检验
                        when (j.selectTitle) {
                            "项目地点", "孔洞最大直径","岗位类别",
                            "可实施地域","运送财产保险","资质类别及等级",
                                "性别要求", "机械设备", "跨越架材质", "财务运输保险",
                        "配送", "合作方属性", "薪资标准","费用标准","性别",
                        "可作业范围","是否配送","驾驶员性别","需求专业","车辆类型","是否配驾驶员","保险状态",
                            "专业工种"-> {
                                if (j.selectContent == ""||j.selectContent.replace(" ","")=="") {
                                    result = "${j.selectTitle.replace("：", "")}没有选择"
                                }
                            }
                        }
                    }
                    if (result != "") {
                        ToastHelper.mToast(context,result)
                        return false
                    }
                }
                MultiStyleItem.Options.MULTI_RADIO_BUTTON->{
                    if(j.necessary==true) {
                        when (j.radioButtonTitle) {
                            "性别要求", "机械设备", "跨越架材质", "财务运输保险",
                            "配送", "合作方属性", "薪资标准","费用标准","性别",
                            "可作业范围","是否配送","驾驶员性别"-> {
                                if (j.radioButtonValue == "")
                                    result = "${j.radioButtonTitle.replace("：", "")}没有选择"
                            }
                        }
                    }
                    if (result != "") {
                        ToastHelper.mToast(context,result)
                        return false
                    }
                }
                MultiStyleItem.Options.MULTI_CHECKBOX->{
                    if(j.necessary==true) {
                        when (j.checkboxTitle) {
                            "电压等级", "作业类别", "操作次级", "可实施范围","可操作电压等级",
                            "可设计电压等级","可设计范围","可作业类别"-> {
                                result = "${j.checkboxTitle.replace("：", "")}没有选择"
                                for (i in 0 until j.checkboxValueList.size)
                                    if (j.checkboxValueList[i]) {
                                        result = ""
                                    }
                            }
                        }
                    }
                    if (result != "") {
                        ToastHelper.mToast(context,result)
                        return false
                    }
                }



            }
        }
        return true
    }

    fun startSendMessage(jsonObject:JSONObject,baseUrl: String):Observable<ResponseBody>{
        val requestBody = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
        return startSendMessage(requestBody,baseUrl)
    }
    fun putSimpleMessage(jsonObject:JSONObject,baseUrl: String):Observable<ResponseBody>{

        val requestBody = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
        return putSimpleMessage(requestBody,baseUrl)
    }
    /**
     * @我的信息
     */
    fun getDataUser():Observable<HttpResult<UserEntity>>{
        return Observable.create<HttpResult<UserEntity>> {
                target->getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                target.onNext(it)
            },{
                it.printStackTrace()
            })
        }
    }

    /**
     * @我的家庭信息
     */
    fun getDataHomeChildren():Observable<HttpResult<List<HomeChildrensEntity>>>{
        return Observable.create<HttpResult<List<HomeChildrensEntity>>> {
                target->getHomeChildren().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                target.onNext(it)
            },{
                it.printStackTrace()
            })
        }
    }
    /**
     * @我的学历信息
     */
    fun getDataEducationBackground():Observable<HttpResult<List<EducationBackgroundsEntity>>>{
        return Observable.create<HttpResult<List<EducationBackgroundsEntity>>> {
                target->getEducationBackground().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                target.onNext(it)
            },{
                it.printStackTrace()
            })
        }
    }
    /**
     * @我的紧急联系人
     */
    fun getDataUrgentPeople():Observable<HttpResult<List<UrgentPeoplesEntity>>>{
        return Observable.create<HttpResult<List<UrgentPeoplesEntity>>> {
                target->getUrgentPeople().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                target.onNext(it)
            },{
                it.printStackTrace()
            })
        }
    }
    /**
     * @我的银行卡信息
     */
    fun getDataBankCard():Observable<HttpResult<List<BankCardsEntity>>>{
        return Observable.create<HttpResult<List<BankCardsEntity>>> {
                target->getBankCard().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                target.onNext(it)
            },{
                it.printStackTrace()
            })
        }
    }
    /**
     * @我的个人材料
     */
    fun getDataCertificate():Observable<HttpResult<List<CertificatesEntity>>>{
        return Observable.create<HttpResult<List<CertificatesEntity>>> {
                target->getCertificate().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                target.onNext(it)
            },{
                it.printStackTrace()
            })
        }
    }
    /**
     *@会员等级查询
     */
    fun getDataUserOpenPower():Observable<HttpResult<OpenPowerEntity>>{
        return Observable.create<HttpResult<OpenPowerEntity>> {
                target->
            getUserOpenPower().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    target.onNext(it)
                },{
                    it.printStackTrace()
                })
        }
    }
    /**
     * @微信创建商品支付订单
     */

    fun creatDataOrder(productId:String){
        creatOrder(productId).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread())
            .subscribe({
                val json = JSONObject(it.string())
                if(json.getInt("code")==200){
                    val intent = Intent(context, WXPayEntryActivity::class.java)
                    val jsonObject = json.getJSONObject("message")
                    jsonObject.put("openVipLevel",productId)
                    intent.putExtra("json",jsonObject.toString())
                    context.startActivity(intent)
                }
            },{
                ToastHelper.mToast(context,"网络异常")
                it.printStackTrace()
            })
    }

    /**
     * @支付宝创建商品支付订单
     */
    fun getAliPayOrder(productId:String){
        getAliPayOrderStr(productId).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread())
            .subscribe({
                PaymentHelper.startAlipay(context as VipActivity,JSONObject(it.string()).getString("message"),productId.toInt())
            },{
                ToastHelper.mToast(context,"网络异常")
                it.printStackTrace()
            })
    }

    /**
     * @推广人数支付
     */
    fun numPay(productId:String){
        val loadingDialog = LoadingDialog(context, "购买中...", R.mipmap.ic_dialog_loading)
        loadingDialog.show()
        numPayCreatOrder(productId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadingDialog.dismiss()
                val json = JSONObject(it.string())
                Log.i("json " ,json.toString())
                val message = json.getString("message")
                if(json.getString("payDesc")=="OK" ){
                    checkPaymentState(message,productId.toInt())
                }
            },{
                loadingDialog.dismiss()
                ToastHelper.mToast(context,"网络异常")
                it.printStackTrace()
            })
    }

    /**
     * @查询支付是否成功
     */
    fun checkPaymentState(orderNumber:String,openVipLevel:Int){
        val loadingDialog = LoadingDialog(context, "结果查询中...", R.mipmap.ic_dialog_loading)
        loadingDialog.show()
        val result = openPowerNotify(orderNumber).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread())
            .subscribe({
                loadingDialog.dismiss()
                val json = JSONObject(it.string())
                Log.i("json " ,json.toString())
                ToastHelper.mToast(context,json.getString("message"))
                if(json.getInt("code")==200){
                    UnSerializeDataBase.vipOpenState = 1
                    UnSerializeDataBase.userVipLevel = openVipLevel
                    if(context is WXPayEntryActivity)
                        (context as WXPayEntryActivity).finish()
                    else if(context is VipActivity)
                        (context as VipActivity).supportFragmentManager.popBackStackImmediate()
                }
            },{
                ToastHelper.mToast(context,"异常情况")
                loadingDialog.dismiss()
                it.printStackTrace()
            })
    }
}