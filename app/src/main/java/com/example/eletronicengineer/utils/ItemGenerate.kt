package com.electric.engineering.utils

import android.content.Context
import android.view.View
import com.electric.engineering.model.MultiStyleItem
import org.json.JSONArray
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
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.TITLE,jsonObject.getString("title1"))

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
                    val array1=jsonObject.getJSONArray("selectOption1Items")
                    val array2=jsonObject.getJSONArray("selectOption2Items")
                    val array3=jsonObject.getJSONArray("selectOption3Items")
                    val selectOption1Items:MutableList<String> =ArrayList()
                    val selectOption2Items:MutableList<MutableList<String>> =ArrayList()
                    val selectOption3Items:MutableList<MutableList<MutableList<String>>> =ArrayList()
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
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG,selectOption1Items,selectOption2Items,selectOption3Items,jsonObject.getString("selectTitle"))

                }
                "SINGLE_INPUT"->
                {
                    val inputSingleTitle=jsonObject.getString("inputSingleTitle")
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,inputSingleTitle)

                }
                "TWO_PAIR_INPUT"->
                {
                    val twoPairInputTitle=jsonObject.getString("twoPairInputTitle")
                    val twoPairInputItem1=jsonObject.getString("twoPairInputItem1")
                    val twoPairInputItem2=jsonObject.getString("twoPairInputItem2")
                    val twoPairInputItem3=jsonObject.getString("twoPairInputItem3")
                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.TWO_PAIR_INPUT,twoPairInputTitle,twoPairInputItem1,twoPairInputItem2,twoPairInputItem3)
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
                    multiStyleItem=MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_TEXTAREA,textAreaTitle)

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
                "SINGLE_DISPLAY"->
                {
                    val singleDisplayTitle=jsonObject.getString("singleDisplayTitle")
                    val singleDisplayContent=jsonObject.getString("singleDisplayContent")
                    multiStyleItem= MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY,singleDisplayTitle,singleDisplayContent)
                }
            }
            multiStyleItem.key=jsonObject.optString("key")
            data.add(multiStyleItem)
        }
        return data
    }
}