package com.electric.engineering.utils

import android.content.Context
import android.view.View
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class ItemGenerate
{
    var buttonListeners:MutableList<View.OnClickListener> =ArrayList()
    var hybridButtonListener:MutableList<View.OnClickListener> =ArrayList()
    lateinit var submitListener: View.OnClickListener
    lateinit var context:Context
    var path:String?=null
    fun getJsonFromAsset(path:String):List<MultiStyleItem>
    {
        val resultBuilder=StringBuilder()
        val bf= BufferedReader(InputStreamReader(context.assets.open(path)))
        try {
            var line=bf.readLine()
            while (line!=null)
            {
                resultBuilder.append(line)
                line=bf.readLine()
            }
        }
        catch (io:IOException)
        {
            io.printStackTrace()
        }
        val data:List<MultiStyleItem> =GenerateFromJsonArray(resultBuilder.toString())
        return data
    }
    fun GenerateFromJsonArray(json:String):List<MultiStyleItem>
    {
        val jsonArray=JSONArray(json)
        val data:MutableList<MultiStyleItem> =ArrayList()
        for (i in 0 until jsonArray.length())
        {
            var multiStyleItem= MultiStyleItem()
            val jsonObject=jsonArray.getJSONObject(i)
            //fun("type")
            when(jsonObject.getString("type"))
            {
                "TITLE"->
                {
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.TITLE,jsonObject.getString("title1"),jsonObject.getString("styleType"))

                }
                "MULTI_BUTTON"->
                {
                    val array=jsonObject.getJSONArray("buttonName")
                    val buttonName:MutableList<String> =ArrayList()
                    for (j in 0 until array.length())
                    {
                        buttonName.add(array[j].toString())
                    }
                    if (buttonListeners.size>0)
                    {
                        multiStyleItem=MultiStyleItem(MultiStyleItem.Options.MULTI_BUTTON,jsonObject.getString("buttonTitle"),buttonName,buttonListeners)
                    }
                    else
                    {
                        multiStyleItem=MultiStyleItem(MultiStyleItem.Options.MULTI_BUTTON,jsonObject.getString("buttonTitle"),buttonName,ArrayList())
                    }
                    if (jsonObject.optString("additionalKey")!=null)
                    {
                        multiStyleItem.additionalKey=jsonObject.optString("additionalKey")
                    }
                    for(i in buttonName)
                    {
                        multiStyleItem.buttonCheckList.add(false)
                    }
                }
                "MULTI_RADIO_BUTTON"->
                {
                    val array=jsonObject.getJSONArray("radioButtonName")
                    val radioName:MutableList<String> =ArrayList()
                    for (j in 0 until array.length())
                    {
                        radioName.add(array[j].toString())
                    }
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.MULTI_RADIO_BUTTON,radioName,jsonObject.getString("radioButtonTitle"))

                }
                "MULTI_HYBRID"->
                {
                    val buttonArray=jsonObject.optJSONArray("hybridButtonName")
                    val checkboxArray=jsonObject.optJSONArray("hybridCheckBoxName")
                    val radioButtonArray=jsonObject.optJSONArray("hybridRadioButtonName")
                    val hybridButtonName:MutableList<String> =ArrayList()
                    val hybridCheckBoxName:MutableList<String> =ArrayList()
                    val hybridRadioButtonName:MutableList<String> =ArrayList()
                    val hybridTitle=jsonObject.getString("hybridTitle")
                    if (buttonArray!=null)
                    {
                        for (j in 0 until buttonArray.length())
                        {
                            hybridButtonName.add(buttonArray.getString(j))
                        }
                    }
                    if(checkboxArray!=null)
                    {
                        for (j in 0 until checkboxArray.length())
                        {
                            hybridCheckBoxName.add(checkboxArray.getString(j))
                        }
                    }
                    if(radioButtonArray!=null)
                    {
                        for (j in 0 until radioButtonArray.length())
                        {
                            hybridRadioButtonName.add(radioButtonArray.getString(j))
                        }
                    }
                    if(hybridButtonListener.size>0)
                    {
                        multiStyleItem=MultiStyleItem(MultiStyleItem.Options.MULTI_HYBRID,hybridTitle,hybridButtonName,hybridRadioButtonName,hybridCheckBoxName,hybridButtonListener)
                    }
                    else
                        multiStyleItem=MultiStyleItem(MultiStyleItem.Options.MULTI_HYBRID,hybridTitle,hybridButtonName,hybridRadioButtonName,hybridCheckBoxName,ArrayList())

                }
                "MULTI_CHECKBOX" ->
                {
                    val array=jsonObject.getJSONArray("checkboxNameList")
                    val checkboxName:MutableList<String> =ArrayList()
                    for (j in 0 until array.length())
                    {
                        checkboxName.add(array[j].toString())
                    }
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.MULTI_CHECKBOX,checkboxName,jsonObject.getString("checkboxTitle"))

                }
                "SELECT_DIALOG"->
                {
                    val array=jsonObject.getJSONArray("selectOption1Items")
                    val selectOption1Items:MutableList<String> =ArrayList()
                    for (j in 0 until array.length())
                    {
                        selectOption1Items.add(array[j].toString())
                    }
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.SELECT_DIALOG,selectOption1Items,jsonObject.getString("selectTitle"))

                }
                "TWO_OPTIONS_SELECT_DIALOG"->
                {
                    val array1=jsonObject.getJSONArray("selectOption1Items")
                    val array2=jsonObject.getJSONArray("selectOption2Items")
                    val selectOption1Items:MutableList<String> =ArrayList()
                    val selectOption2Items:MutableList<MutableList<String>> =ArrayList()
                    for (j in 0 until array1.length())
                    {
                        selectOption1Items.add(array1[j].toString())
                    }
                    for (j in 0 until array2.length())
                    {
                        selectOption2Items.add(ArrayList())
                        for (k in 0 until array2.getJSONArray(j).length())
                        {
                            selectOption2Items[j].add(array2.getJSONArray(j).getString(k))
                        }
                    }
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG,selectOption1Items,selectOption2Items,ArrayList(),jsonObject.getString("selectTitle"))

                }
                "THREE_OPTIONS_SELECT_DIALOG"->
                {
                    val selectOption1Items:MutableList<String> =ArrayList()
                    val selectOption2Items:MutableList<MutableList<String>> =ArrayList()
                    val selectOption3Items:MutableList<MutableList<MutableList<String>>> =ArrayList()
                    if(jsonObject.getString("placePath")!=""){
                        val resultBuilder= StringBuilder()
                        val bf= BufferedReader(InputStreamReader(context.assets.open(jsonObject.getString("placePath"))))
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
                        val json= JSONObject(resultBuilder.toString())
                        val keys:Iterator<String> = json.keys()
                        while (keys.hasNext()){
                            val key = keys.next()
                            selectOption1Items.add(key)
                            val js = json.getJSONObject(key)
                            val jskeys = js.keys()
                            selectOption2Items.add(ArrayList())
                            selectOption3Items.add(ArrayList())
                            while (jskeys.hasNext()){
                                val jskey = jskeys.next()
                                selectOption2Items[selectOption2Items.size-1].add(jskey)
                                selectOption3Items[selectOption3Items.size-1].add(ArrayList())
                                var valueArray = js.getJSONArray(jskey)
                                for (j in 0 until valueArray.length()){
                                    selectOption3Items[selectOption3Items.size-1][selectOption3Items[selectOption3Items.size-1].size-1].add(valueArray[j].toString())
                                }
                            }
                        }
                    }
                    else{
                        val array1=jsonObject.getJSONArray("selectOption1Items")
                        val array2=jsonObject.getJSONArray("selectOption2Items")
                        val array3=jsonObject.getJSONArray("selectOption3Items")
                        for (j in 0 until array1.length())
                        {
                            selectOption1Items.add(array1[j].toString())
                        }
                        for (j in 0 until array2.length())
                        {
                            selectOption2Items.add(ArrayList())
                            for (k in 0 until array2.getJSONArray(j).length())
                            {
                                selectOption2Items[j].add(array2.getJSONArray(j).getString(k))
                            }
                        }
                        for (j in 0 until array2.length())
                        {
                            selectOption3Items.add(ArrayList())
                            for (k in 0 until array2.getJSONArray(j).length())
                            {
                                selectOption3Items[j].add(ArrayList())
                                for (m in 0 until array3.getJSONArray(j).getJSONArray(k).length())
                                {
                                    selectOption3Items[j][k].add(array3.getJSONArray(j).getJSONArray(k).getString(m))
                                }
                            }
                        }
                    }
                    val necessary= if(jsonObject.has("necessary")){ jsonObject.getBoolean("necessary")} else { false}
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG,selectOption1Items,selectOption2Items,selectOption3Items,jsonObject.getString("selectTitle"))

                }
                "SINGLE_INPUT"->
                {
                    val inputSingleTitle=jsonObject.getString("inputSingleTitle")
                    val necessary= if(jsonObject.has("necessary")){ jsonObject.getBoolean("necessary")} else { false}
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,inputSingleTitle,necessary)

                }
                "TWO_PAIR_INPUT"->
                {
                    val twoPairInputTitle=jsonObject.getString("twoPairInputTitle")
                    val twoPairInputItem1=jsonObject.getString("twoPairInputItem1")
                    val twoPairInputItem2=jsonObject.getString("twoPairInputItem2")
                    val twoPairInputItem3=jsonObject.getString("twoPairInputItem3")
                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.TWO_PAIR_INPUT,twoPairInputTitle,twoPairInputItem1,twoPairInputItem2,twoPairInputItem3)
                }
                "FOUR_DISPLAY"->
                {
                    val fourDisplayTitle=jsonObject.getString("fourDisplayTitle")
                    val fourDisplayContent1=jsonObject.getString("fourDisplayContent1")
                    val fourDisplayContent2=jsonObject.getString("fourDisplayContent2")
                    val fourDisplayContent3=jsonObject.getString("fourDisplayContent3")
                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.FOUR_DISPLAY,fourDisplayTitle,fourDisplayContent1,fourDisplayContent2,fourDisplayContent3)
                }
                "FIVE_DISPLAY"->
                {
                    val fiveDisplayTitle=jsonObject.getString("fiveDisplayTitle")
                    val fiveDisplayContent1=jsonObject.getString("fiveDisplayContent1")
                    val fiveDisplayContent2=jsonObject.getString("fiveDisplayContent2")
                    val fiveDisplayContent3=jsonObject.getString("fiveDisplayContent3")
                    val fiveDisplayContent4=jsonObject.getString("fiveDisplayContent4")
                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.FIVE_DISPLAY,fiveDisplayTitle,fiveDisplayContent1,fiveDisplayContent2,fiveDisplayContent3,fiveDisplayContent4)
                }
                "TWO_COLUMN_DISPLAY"->
                {
                    val twoColumnDisplayTitle1=jsonObject.getString("twoColumnDisplayTitle1")
                    val twoColumnDisplayContent1=jsonObject.getString("twoColumnDisplayContent1")
                    val twoColumnDisplayTitle2=jsonObject.getString("twoColumnDisplayTitle2")
                    val twoColumnDisplayContent2=jsonObject.getString("twoColumnDisplayContent2")

                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.TWO_COLUMN_DISPLAY,twoColumnDisplayTitle1,twoColumnDisplayContent1,twoColumnDisplayTitle2,twoColumnDisplayContent2)
                }
                "EXPAND_LIST"->
                {
                    val styleType = jsonObject.getString("styleType")
                    val expandListTitle=jsonObject.getString("expandListTitle")
                    val mData=jsonObject.getString("expandListPath")
                    if(mData!=null)
                    {
                        val expandListAdapter=RecyclerviewAdapter(this.getJsonFromAsset(mData))
                        multiStyleItem= MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST,expandListTitle,styleType,expandListAdapter)
                    }
                    else
                    {
                        val expandListAdapter= RecyclerviewAdapter(ArrayList())
                        multiStyleItem= MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST,expandListTitle,styleType,expandListAdapter)
                    }
                }
                "SINGLE_DISPLAY_LEFT"->
                {
                    val singleDisplayLeftTitle=jsonObject.getString("singleDisplayLeftTitle")
                    val singleDisplayLeftContent=jsonObject.getString("singleDisplayLeftContent")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,singleDisplayLeftTitle,singleDisplayLeftContent)
                }
                "TWO_DISPLAY"->
                {
                    val twoDisplayTitle=jsonObject.getString("twoDisplayTitle")
                    val twoDisplayContent1=jsonObject.getString("twoDisplayContent1")
                    val twoDisplayContent2=jsonObject.getString("twoDisplayContent2")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.TWO_DISPLAY,twoDisplayTitle,twoDisplayContent1,twoDisplayContent2)
                }
                "INPUT_RANGE"->
                {
                    val inputRangeTitle=jsonObject.getString("inputRangeTitle")
                    val inputRangeUnit=jsonObject.getString("inputRangeUnit")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.INPUT_RANGE,inputRangeTitle,inputRangeUnit)

                }
                "INPUT_WITH_UNIT"->
                {
                    val inputUnitTitle=jsonObject.getString("inputUnitTitle")
                    val inputUnit=jsonObject.getString("inputUnit")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT,inputUnitTitle,inputUnit)

                }
                "INPUT_WITH_MULTI_UNIT"->
                {
                    val inputMultiUnitTitle=jsonObject.getString("inputMultiUnitTitle")
                    val inputMultiUnit=jsonObject.getJSONArray("inputMultiUnit")
                    val unitList:MutableList<String> =ArrayList()
                    for (j in 0 until inputMultiUnit.length())
                    {
                        unitList.add(inputMultiUnit.getString(j))
                    }
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT,unitList,inputMultiUnitTitle)

                }
                "INPUT_WITH_TEXTAREA"->
                {
                    val textAreaTitle=jsonObject.getString("textAreaTitle")
                    val textAreaContent=jsonObject.getString("textAreaContent")
                    val necessary = jsonObject.getBoolean("necessary")
                    val hint=if(jsonObject.has("hint")){jsonObject.getString("hint")} else {""}
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_TEXTAREA,textAreaTitle,textAreaContent,necessary,hint)

                }
                "SHIFT_INPUT"->
                {
                    val shiftInputTitle=jsonObject.getString("shiftInputTitle")
                    val necessary=jsonObject.getBoolean("necessary")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,shiftInputTitle,necessary)
                }
                "HINT"->
                {
                    val hintContent=jsonObject.getString("hintContent")
                    val customFontColor=jsonObject.optString("customFontColor")
                    val startPosition=jsonObject.optInt("startPosition")
                    val endPosition=jsonObject.optInt("endPosition")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.HINT,hintContent)
                    multiStyleItem.customFontColor=customFontColor
                    multiStyleItem.startPosition=startPosition
                    multiStyleItem.endPosition=endPosition
                }
                "SUBMIT"->
                {
                    val submitContent=jsonObject.getString("submitContent")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.SUBMIT,submitContent)

                }
                "EXPAND"->
                {
                    val expandTitle=jsonObject.getString("expandTitle")
                    val expandContent=jsonObject.getString("expandContent")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.EXPAND,expandTitle,expandContent)
                }
                "SINGLE_DISPLAY_RIGHT"->
                {
                    val singleDisplayRightTitle=jsonObject.getString("singleDisplayRightTitle")
                    val singleDisplayRightContent=jsonObject.getString("singleDisplayRightContent")
                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,singleDisplayRightTitle,singleDisplayRightContent)
                }
                "TWO_TEXT_DIALOG"->
                {
                    val array1=jsonObject.getJSONArray("selectOption1Items1")
                    val selectOption1Items1:MutableList<String> =ArrayList()
                    for (j in 0 until array1.length())
                    {
                        selectOption1Items1.add(array1[j].toString())
                    }
                    val array2=jsonObject.getJSONArray("selectOption1Items2")
                    val selectOption1Items2:MutableList<String> =ArrayList()
                    for (j in 0 until array2.length())
                    {
                        selectOption1Items2.add(array2[j].toString())
                    }
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.TWO_TEXT_DIALOG,selectOption1Items1,jsonObject.getString("textFirstDialog"),
                        selectOption1Items2,jsonObject.getString("textSecondDialog")
                    )
                }
                "TEXT_SCOLLVIEW"->
                {
                    val textDetails=jsonObject.getString("textDetails")
                    val textMore=jsonObject.getString("textMore")
                    val textDelete=jsonObject.getString("textDelete")
                    val expandTitle=jsonObject.getString("expandTitle")
                    val expandContent=jsonObject.getString("expandContent")
                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.TEXT_SCOLLVIEW,textDetails,textMore,textDelete,expandTitle,expandContent)
                }
                "EXPAND_RIGHT"->
                {
                    val expandTitle=jsonObject.getString("expandTitle")
                    val expandContent=jsonObject.getString("expandContent")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.EXPAND_RIGHT,expandTitle,expandContent)
                }
                "BLANK"->{
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.BLANK,"")
                }
                // alterPosition time:2019/7/15
                // function:定位点添加
                "POSITION_ADD"->
                {
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.POSITION_ADD,jsonObject.getString("title1"))
                }
                // alterPosition time:2019/7/15
                // function:删除
                "POSITION_DELETE"->
                {
                    val inputPositionTitle=jsonObject.getString("inputPositionTitle")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.POSITION_DELETE,inputPositionTitle,jsonObject.getBoolean("necessary"))
                }
                // alterPosition time:2019/7/15
                // function:带边框的输入框
                "TITLE_INPUT_BG"->
                {
                    val inputSingleTitleWithBg=jsonObject.getString("inputSingleTitleWithBg")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.TITLE_INPUT_BG,inputSingleTitleWithBg)
                }
                // alterPosition time:2019/7/15
                // function:带边框的输入框+选择
                "TITLE_INPUT_BG_SELECT"->
                {
                    val inputSingleTitleWithBgSelect=jsonObject.getString("inputSingleTitleWithBgSelect")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.TITLE_INPUT_BG_SELECT,inputSingleTitleWithBgSelect)
                }
                // alterPosition time:2019/7/15
                // function:文本扩展+选择
                "TEXT_EXPAND_SELECT"->
                {
                    val expandTitle=jsonObject.getString("expandTitle")
                    val expandContent=jsonObject.getString("expandContent")
                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.TEXT_EXPAND_SELECT,expandTitle,expandContent)
                }
                // alterPosition time:2019/7/22
                // function:公共点定位，起始点与终点合并
                "POSITION_START_END"->
                {
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.POSITION_START_END,jsonObject.getString("inputPositionTransportTitle"))
                }
                "DISTANCE_POSITION"->{
                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.DISTANCE_POSITION,jsonObject.getString("inputDistancePositionTitle"),jsonObject.getString("inputDistancePositionUnit"),jsonObject.getBoolean("necessary"))
                }
                "EXPAND_MENU"->{
                    val array = jsonObject.getJSONArray("expandMenuContent")
                    val expandMenuContent:MutableList<String> =ArrayList()
                    for (j in 0 until array.length()) {
                        expandMenuContent.add(array[j].toString())
                    }
                    multiStyleItem = MultiStyleItem(MultiStyleItem.Options.EXPAND_MENU,expandMenuContent,jsonObject.getString("expandMenuTitle"))
                }
            }
            multiStyleItem.key=jsonObject.optString("key")
            multiStyleItem.sendFormat=jsonObject.optString("sendFormat")
            data.add(multiStyleItem)
        }
        return data
    }
}