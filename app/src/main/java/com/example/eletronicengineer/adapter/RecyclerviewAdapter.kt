package com.example.eletronicengineer.adapter

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.item_dialog_select.view.*
import kotlinx.android.synthetic.main.item_input_range.view.*
import kotlinx.android.synthetic.main.item_input_with_unit.view.*
import kotlinx.android.synthetic.main.item_multi.view.*
import kotlinx.android.synthetic.main.item_single_input.view.*
import kotlinx.android.synthetic.main.item_title.view.*
import android.os.Handler
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.custom.CustomDialog
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.activity.MainActivity
import com.example.eletronicengineer.utils.ObserverFactory
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMessage
import com.example.eletronicengineer.utils.startSendMultiPartMessage
import kotlinx.android.synthetic.main.item_confirm.view.*
import kotlinx.android.synthetic.main.item_expand.view.*
import kotlinx.android.synthetic.main.item_expand_list.view.*
import kotlinx.android.synthetic.main.item_hint.view.*
import kotlinx.android.synthetic.main.item_input_with_multi_unit.view.*
import kotlinx.android.synthetic.main.item_input_with_textarea.view.*
import kotlinx.android.synthetic.main.item_shift_input.view.*
import kotlinx.android.synthetic.main.item_single_display.view.*
import kotlinx.android.synthetic.main.item_three_display.view.*
import kotlinx.android.synthetic.main.item_two_column_display.view.*
import kotlinx.android.synthetic.main.item_two_pair_input.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

class RecyclerviewAdapter: RecyclerView.Adapter<RecyclerviewAdapter.VH> {
    companion object {
        const val TITLE_TYPE:Int=1
        const val SELECT_DIALOG_TYPE:Int=2

        const val SINGLE_INPUT_TYPE:Int=3
        const val INPUT_RANGE_TYPE:Int=4
        const val INPUT_WITH_UNIT_TYPE=5
        const val INPUT_WITH_MULTI_UNIT_TYPE=16
        const val INPUT_WITH_TEXTAREA_TYPE=11
        const val SHIFT_INPUT_TYPE=17
        const val TWO_PAIR_INPUT_TYPE=18

        const val MULTI_BUTTON_TYPE:Int=6
        const val MULTI_RADIO_BUTTON_TYPE:Int=7
        const val MULTI_CHECKBOX_TYPE=8
        const val MULTI_HYBRID_TYPE=14
        const val TWO_OPTIONS_SELECT_DIALOG_TYPE=9
        const val THREE_OPTIONS_SELECT_DIALOG_TYPE=10

        const val HINT_TYPE=12
        const val SUBMIT_TYPE=13
        const val EXPAND_TYPE=15
        const val SINGLE_DISPLAY_TYPE=19

        const val THREE_DISPLAY_TYPE=20
        const val TWO_COLUMN_DISPLAY_TYPE=21
        const val EXPAND_LIST_TYPE=22
        const val EXPAND_LIST_DISPLAY_TYPE=23

        const val MESSAGE_SELECT_OK:Int=100
    }
    fun dataToMap(adapter: RecyclerviewAdapter):Map<String,String>
    {
        val result=HashMap<String,String>()
        val mData=adapter.mData
        val VHList=adapter.VHList
        for (i in 0 until mData.size)
        {
            val multiStyleItem=mData[i]
            val viewHolder=VHList[i]
            when(viewHolder.itemViewType)
            {
                TITLE_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.tvTitle1.text.toString()
                }
                SINGLE_INPUT_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etSingleInputContent.text.toString()
                }
                INPUT_WITH_UNIT_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etInputUnitValue.text.toString()
                }
                INPUT_WITH_MULTI_UNIT_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etMultiInputContent.text.toString()
                }
                INPUT_RANGE_TYPE->
                {
                    val keys=multiStyleItem.key.split(" ")
                    result[keys[0]]=viewHolder.etRangeValue1.text.toString()
                    result[keys[1]]=viewHolder.etRangeValue2.text.toString()
                }
                INPUT_WITH_TEXTAREA_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etTextAreaContent.text.toString()
                }
                TWO_PAIR_INPUT_TYPE->
                {
                    val keys = multiStyleItem.key.split(" ")
                    result[keys[0]] = viewHolder.etTwoPairInputValue1.text.toString()
                    result[keys[1]] = viewHolder.etTwoPairInputValue2.text.toString()
                }
                SELECT_DIALOG_TYPE, TWO_OPTIONS_SELECT_DIALOG_TYPE, THREE_OPTIONS_SELECT_DIALOG_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etDialogSelectItem.text.toString()
                }
                SHIFT_INPUT_TYPE->
                {

                }
                MULTI_RADIO_BUTTON_TYPE->
                {
                    var value=1
                    for (j in 0 until viewHolder.llContainer.childCount)
                    {
                        val radioButton=viewHolder.llContainer.getChildAt(j) as RadioButton
                        if (radioButton.isChecked)
                        {
                            result[multiStyleItem.key]=value.toString()
                            break
                        }
                        else
                            value--
                    }
                }
            }//end when
        }//end for
        return result
    }
    var mData:List<MultiStyleItem>
    var adapterObserver:ObserverFactory.RecyclerviewAdapterObserver?=null
    var expandList:MutableList<VH> =ArrayList()
    var expandPosition:Int=-1
    val VHList:MutableList<VH> =ArrayList()

    inner class VH:RecyclerView.ViewHolder
    {
        var itemPosition=-1
        lateinit var tvTitle1:TextView
        lateinit var tvBack:TextView

        lateinit var multiTitle:TextView
        lateinit var llContainer: LinearLayout
        var btnList:MutableList<Button> = mutableListOf()

        lateinit var tvSingleInputTitle:TextView
        lateinit var etSingleInputContent:EditText

        lateinit var tvRangeTitle:TextView
        lateinit var tvRangeUnit1:TextView
        lateinit var tvRangeUnit2:TextView
        lateinit var etRangeValue1:EditText
        lateinit var etRangeValue2:EditText

        lateinit var tvInputUnit: TextView
        lateinit var tvInputUnitTitle:TextView
        lateinit var etInputUnitValue:EditText

        lateinit var tvShiftInputTitle:TextView
        lateinit var tvShiftInputContent:TextView
        lateinit var tvShiftInputShow:TextView

        lateinit var tvTwoPairInputTitle:TextView
        lateinit var tvTwoPairInputItem1:TextView
        lateinit var tvTwoPairInputItem2:TextView
        lateinit var tvTwoPairInputItem3:TextView
        lateinit var etTwoPairInputValue1:EditText
        lateinit var etTwoPairInputValue2:EditText

        lateinit var tvThreeDisplayTitle:TextView
        lateinit var tvThreeDisplayItem1:TextView
        lateinit var tvThreeDisplayItem2:TextView
        lateinit var tvThreeDisplayItem3:TextView

        lateinit var tvTwoColumnDisplayTitle1:TextView
        lateinit var tvTwoColumnDisplayContent1:TextView
        lateinit var tvTwoColumnDisplayTitle2:TextView
        lateinit var tvTwoColumnDisplayContent2:TextView

        lateinit var tvExpandListTitle:TextView
        lateinit var rvExpandListContent:RecyclerView
        lateinit var tvExpandListButton:TextView

        lateinit var spMultiInputUnit:Spinner
        lateinit var tvMultiInputUnitTitle:TextView
        lateinit var etMultiInputContent:EditText


        lateinit var tvDialogSelectTitle:TextView
        lateinit var etDialogSelectItem:TextView
        lateinit var tvDialogSelectShow:TextView

        lateinit var tvTextAreaTitle:TextView
        lateinit var etTextAreaContent:TextView

        lateinit var tvHint:TextView

        lateinit var btnSubmit:Button

        lateinit var tvExpandTitle:TextView
        lateinit var tvExpandContent:TextView
        lateinit var tvExpandButton:TextView

        lateinit var tvSingleDisplayTitle:TextView
        lateinit var tvSingleDisplayContent:TextView
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                MESSAGE_SELECT_OK->
                {
                    val selectContent=it.data.getString("selectContent")
                    if(selectContent.equals("自定义") || selectContent.equals("其他") ||selectContent.equals("填写")){
                        etDialogSelectItem.isEnabled=true
                        etDialogSelectItem.hint="请输入"
                        etDialogSelectItem.text=""
                    }else{
                        etDialogSelectItem.isEnabled=false
                        etDialogSelectItem.text=selectContent
                        mData[itemPosition].selectContent=selectContent!!
                    }
                    false
                }
                else->
                {
                    false
                }
            }
        })
        constructor(v:View,viewType: Int):super(v)
        {
            when(viewType)
            {
                TITLE_TYPE->
                {
                    //root root.findViewById(R,id.tv_title_title1)
                    tvTitle1=v.tv_title_title1
                    tvBack=v.tv_title_back
                }
                MULTI_BUTTON_TYPE->
                {
                    llContainer=v.cl_multi_container
                    multiTitle=v.tv_multi_title
                }
                MULTI_RADIO_BUTTON_TYPE->
                {
                    llContainer=v.cl_multi_container
                    multiTitle=v.tv_multi_title
                }
                MULTI_CHECKBOX_TYPE->
                {
                    llContainer=v.cl_multi_container
                    multiTitle=v.tv_multi_title
                }
                MULTI_HYBRID_TYPE->
                {
                    llContainer=v.cl_multi_container
                    multiTitle=v.tv_multi_title
                }
                SINGLE_INPUT_TYPE->
                {
                    tvSingleInputTitle=v.tv_input_title
                    etSingleInputContent=v.et_input_content
                }
                TWO_PAIR_INPUT_TYPE->
                {
                    tvTwoPairInputTitle=v.tv_two_pair_input_title
                    tvTwoPairInputItem1=v.tv_two_pair_input_item1
                    tvTwoPairInputItem2=v.tv_two_pair_input_item2
                    tvTwoPairInputItem3=v.tv_two_pair_input_item3
                    etTwoPairInputValue1=v.et_two_pair_input_value1
                    etTwoPairInputValue2=v.et_two_pair_input_value2
                }

                THREE_DISPLAY_TYPE->
                {
                    tvThreeDisplayTitle=v.tv_three_display_title
                    tvThreeDisplayItem1=v.tv_three_display_item1
                    tvThreeDisplayItem2=v.tv_three_display_item2
                    tvThreeDisplayItem3=v.tv_three_display_item3
                }
                TWO_COLUMN_DISPLAY_TYPE->
                {
                    tvTwoColumnDisplayTitle1=v.tv_two_column_display_title1
                    tvTwoColumnDisplayContent1=v.tv_two_column_display_item1
                    tvTwoColumnDisplayTitle2=v.tv_two_column_display_title2
                    tvTwoColumnDisplayContent2=v.tv_two_column_display_item2
                }
                EXPAND_LIST_TYPE->
                {
                    tvExpandListTitle=v.tv_expand_list_title
                    tvExpandListButton=v.tv_expand_list_button
                    rvExpandListContent=v.rv_expand_list_content
                }

                INPUT_RANGE_TYPE->
                {
                    tvRangeTitle=v.tv_range_title
                    tvRangeUnit1=v.tv_range_unit1
                    tvRangeUnit2=v.tv_range_unit2
                    etRangeValue1=v.et_range_value1
                    etRangeValue2=v.et_range_value2
                }
                INPUT_WITH_UNIT_TYPE->
                {
                    tvInputUnitTitle=v.tv_input_unit_title
                    tvInputUnit=v.tv_input_unit
                    etInputUnitValue=v.et_input_unit_value
                }
                INPUT_WITH_MULTI_UNIT_TYPE->
                {
                    spMultiInputUnit=v.sp_input_multi_unit_unit
                    tvMultiInputUnitTitle=v.tv_input_multi_unit_title
                    etMultiInputContent=v.et_input_multi_unit_content
                }
                SHIFT_INPUT_TYPE->
                {
                    tvShiftInputTitle=v.tv_shift_input_title
                    tvShiftInputContent=v.et_shift_input_item
                    tvShiftInputShow=v.tv_shift_input_show
                }
                SELECT_DIALOG_TYPE->
                {
                    tvDialogSelectTitle=v.tv_dialog_select_title
                    etDialogSelectItem=v.et_dialog_select_item
                    tvDialogSelectShow=v.tv_dialog_select_show
                }
                TWO_OPTIONS_SELECT_DIALOG_TYPE->
                {
                    tvDialogSelectTitle=v.tv_dialog_select_title
                    etDialogSelectItem=v.et_dialog_select_item
                    tvDialogSelectShow=v.tv_dialog_select_show
                }
                THREE_OPTIONS_SELECT_DIALOG_TYPE->
                {
                    tvDialogSelectTitle=v.tv_dialog_select_title
                    etDialogSelectItem=v.et_dialog_select_item
                    tvDialogSelectShow=v.tv_dialog_select_show
                }
                INPUT_WITH_TEXTAREA_TYPE->
                {
                    tvTextAreaTitle=v.tv_textarea_title
                    etTextAreaContent=v.et_textarea_content
                }
                HINT_TYPE->
                {
                    tvHint=v.tv_hint_content
                }
                SUBMIT_TYPE->
                {
                    btnSubmit=v.btn_confirm_submit
                }
                EXPAND_TYPE->
                {
                    tvExpandButton=v.tv_expand_button
                    tvExpandTitle=v.tv_expand_title
                    tvExpandContent=v.tv_expand_content
                }
                EXPAND_LIST_TYPE->
                {
                    tvExpandListTitle=v.tv_expand_list_title
                    tvExpandListButton=v.tv_expand_list_button
                    rvExpandListContent=v.rv_expand_list_content
                }
                SINGLE_DISPLAY_TYPE->
                {
                    tvSingleDisplayTitle=v.tv_single_display_title
                    tvSingleDisplayContent=v.tv_single_display_content
                }
            }
            VHList.add(this)
        }
    }
    constructor(data:List<MultiStyleItem>)
    {
        this.mData=data
    }
    override fun getItemViewType(position: Int): Int {
        val multiStyleItem=mData.get(position)
        when(multiStyleItem.options)
        {
            MultiStyleItem.Options.TITLE->return TITLE_TYPE
            MultiStyleItem.Options.MULTI_BUTTON->return MULTI_BUTTON_TYPE
            MultiStyleItem.Options.SINGLE_INPUT->return SINGLE_INPUT_TYPE
            MultiStyleItem.Options.INPUT_WITH_UNIT->return INPUT_WITH_UNIT_TYPE
            MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT->return INPUT_WITH_MULTI_UNIT_TYPE
            MultiStyleItem.Options.SELECT_DIALOG->return SELECT_DIALOG_TYPE
            MultiStyleItem.Options.INPUT_RANGE->return INPUT_RANGE_TYPE
            MultiStyleItem.Options.SHIFT_INPUT->return SHIFT_INPUT_TYPE
            MultiStyleItem.Options.MULTI_RADIO_BUTTON->return MULTI_RADIO_BUTTON_TYPE
            MultiStyleItem.Options.MULTI_CHECKBOX->return MULTI_CHECKBOX_TYPE
            MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG->return TWO_OPTIONS_SELECT_DIALOG_TYPE
            MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG->return THREE_OPTIONS_SELECT_DIALOG_TYPE
            MultiStyleItem.Options.INPUT_WITH_TEXTAREA->return INPUT_WITH_TEXTAREA_TYPE
            MultiStyleItem.Options.HINT->return HINT_TYPE
            MultiStyleItem.Options.SUBMIT->return SUBMIT_TYPE
            MultiStyleItem.Options.MULTI_HYBRID-> return MULTI_HYBRID_TYPE
            MultiStyleItem.Options.EXPAND->return EXPAND_TYPE
            MultiStyleItem.Options.TWO_PAIR_INPUT->return TWO_PAIR_INPUT_TYPE
            MultiStyleItem.Options.SINGLE_DISPLAY->return SINGLE_DISPLAY_TYPE
            MultiStyleItem.Options.THREE_DISPLAY->return THREE_DISPLAY_TYPE
            MultiStyleItem.Options.TWO_COLUMN_DISPLAY->return TWO_COLUMN_DISPLAY_TYPE
            MultiStyleItem.Options.EXPAND_LIST->return EXPAND_LIST_TYPE
            else->
            {
                return TITLE_TYPE
            }
        }
    }
    var time=0
    override fun onBindViewHolder(vh: VH, position: Int) {
        vh.itemPosition=position
        vh.setIsRecyclable(false)
        if (adapterObserver!=null)
        {
            if (position==mData.size-1)
            {
                adapterObserver!!.onBindComplete()
            }
            adapterObserver!!.onBindRunning()
        }
        when(getItemViewType(position))
        {
            TITLE_TYPE->
            {
                //vh.tvTitle1.setText(mData[position].getTitle1())
                vh.tvTitle1.text=mData[position].title1
                if (mData[position].backListener!=null)
                {
                    vh.tvBack.setOnClickListener(mData[position].backListener)
                }
            }
            MULTI_BUTTON_TYPE->
            {
                vh.multiTitle.text=mData[position].buttonTitle
                for (name:String in mData[position].buttonName)
                {
                    val index=mData[position].buttonName.indexOf(name)
                    val btnItem= Button(vh.itemView.context)
                    btnItem.text=name
                    if (index<=mData[position].buttonListener.size-1)
                    {
                        btnItem.setOnClickListener(mData[position].buttonListener[index])
                    }
                    btnItem.setBackgroundResource(R.drawable.btn_style1)
                    vh.llContainer.addView(btnItem)
                    val params:ViewGroup.MarginLayoutParams= btnItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=vh.itemView.resources.getDimension(R.dimen.general_20).toInt()
                    btnItem.layoutParams=params
                    vh.btnList.add(btnItem)
                }
            }
            MULTI_RADIO_BUTTON_TYPE->
            {
                val context=vh.itemView.context
                vh.multiTitle.text=mData[position].radioButtonTitle
                val radioGroup=RadioGroup(vh.itemView.context)
                radioGroup.orientation=LinearLayout.HORIZONTAL
                for (name:String in mData[position].radioButtonName)
                {
                    val radioItem=RadioButton(context)
                    radioItem.text=name
                    radioItem.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                    radioItem.setOnCheckedChangeListener{
                            compoundButton, bool ->
                        if (bool)
                        {
                            val index=mData[position].radioButtonName.indexOf(compoundButton.text.toString())
                            mData[position].radioButtonValue=(1-index).toString()
                        }
                    }
                    radioGroup.addView(radioItem)
                    val params=radioItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                    radioItem.layoutParams=params
                }
                if (mData[position].radioButtonValue!="")
                {
                    val item=radioGroup.getChildAt(1-mData[position].radioButtonValue.toInt()) as RadioButton
                    item.isChecked=true
                }
                vh.llContainer.addView(radioGroup)
            }
            MULTI_CHECKBOX_TYPE->
            {
                val context=vh.itemView.context
                vh.multiTitle.text=mData[position].checkboxTitle
                for (name in mData[position].checkboxNameList)
                {
                    val checkboxItem=CheckBox(context)
                    checkboxItem.text=name
                    checkboxItem.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                    vh.llContainer.addView(checkboxItem)
                    mData[position].checkboxValueList=ArrayList(mData[position].checkboxNameList.size)
                    mData[position].checkboxValueList.add(false)
                    checkboxItem.setOnCheckedChangeListener{
                            compoundButton, bool ->
                        val index=mData[position].checkboxNameList.indexOf(compoundButton.text)
                        if (bool)
                        {
                            mData[position].checkboxValueList.set(index,true)
                        }
                        else
                        {
                            mData[position].checkboxValueList.set(index,false)
                        }
                    }
                    val params=checkboxItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                    checkboxItem.layoutParams=params
                }
                for (i in 0 until vh.llContainer.childCount-1)
                {
                    val item=vh.llContainer.getChildAt(i) as CheckBox
                    item.isChecked=mData[position].checkboxValueList[i]
                }
            }
            MULTI_HYBRID_TYPE->
            {
                val context=vh.itemView.context
                vh.multiTitle.text=mData[position].hybridTitle
                val radioGroup=RadioGroup(context)
                if (mData[position].hybridCheckBoxName.size>0)
                {
                    for (checkboxName in mData[position].hybridCheckBoxName)
                    {
                        val checkboxItem=CheckBox(context)
                        checkboxItem.text=checkboxName
                        checkboxItem.textSize=context.resources.getDimension(R.dimen.font_tv_hint_15)
                        vh.llContainer.addView(checkboxItem)
                        checkboxItem.setOnCheckedChangeListener{
                                compoundButton, bool ->
                            val index=mData[position].checkboxNameList.indexOf(compoundButton.text)
                            if (bool)
                            {
                                mData[position].checkboxValueList.set(index,true)
                            }
                            else
                            {
                                mData[position].checkboxValueList.set(index,false)
                            }
                        }
                        val params=checkboxItem.layoutParams as ViewGroup.MarginLayoutParams
                        params.topMargin=context.resources.getDimension(R.dimen.top_5).toInt()
                        params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                        checkboxItem.layoutParams=params
                    }
                }
                if (mData[position].hybridRadioButtonName.size>0)
                {
                    radioGroup.orientation=LinearLayout.HORIZONTAL
                    for (name:String in mData[position].hybridRadioButtonName)
                    {
                        val radioItem=RadioButton(context)
                        radioItem.text=name
                        radioItem.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                        radioGroup.addView(radioItem)
                        radioItem.setOnCheckedChangeListener{
                                compoundButton, bool ->
                            if (bool)
                            {
                                val index=mData[position].radioButtonName.indexOf(compoundButton.text.toString())
                                mData[position].radioButtonValue=(1-index).toString()
                            }
                        }
                        val params=radioItem.layoutParams as ViewGroup.MarginLayoutParams
                        params.topMargin=context.resources.getDimension(R.dimen.top_5).toInt()
                        params.leftMargin=context.resources.getDimension(R.dimen.general_20).toInt()
                        radioItem.layoutParams=params
                    }
                    vh.llContainer.addView(radioGroup)
                }
                if (mData[position].hybridButtonName.size>0)
                {
                    for (name:String in mData[position].hybridButtonName)
                    {
                        val index=mData[position].hybridButtonName.indexOf(name)
                        val btnItem= Button(vh.itemView.context)
                        btnItem.text=name
                        btnItem.setBackgroundResource(R.drawable.btn_style1)
                        if (index<=mData[position].hybridButtonListeners.size-1)
                        {
                            btnItem.setOnClickListener(mData[position].hybridButtonListeners[index])
                        }
                        vh.llContainer.addView(btnItem)
                        val params:ViewGroup.MarginLayoutParams= btnItem.layoutParams as ViewGroup.MarginLayoutParams
                        params.leftMargin=vh.itemView.resources.getDimension(R.dimen.general_20).toInt()
                        btnItem.layoutParams=params
                        vh.btnList.add(btnItem)
                    }
                }
            }
            SINGLE_INPUT_TYPE->
            {
                vh.tvSingleInputTitle.text=mData[position].inputSingleTitle
                vh.etSingleInputContent.addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputSingleContent=p0.toString()
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }
                })
                if (mData[position].inputSingleContent!="")
                {
                    vh.etSingleInputContent.setText(mData[position].inputSingleContent)
                }
                if(mData[position].inputSingleTitle.equals("类别")){
                    vh.tvShiftInputContent.hint="规格/型号状态"
                }
            }
            INPUT_WITH_UNIT_TYPE->
            {
                vh.tvInputUnitTitle.text=mData[position].inputUnitTitle
                vh.tvInputUnit.text=mData[position].inputUnit
                vh.etInputUnitValue.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputUnitContent=p0.toString()
                    }
                })
                if (mData[position].inputUnitContent!="")
                {
                    vh.etInputUnitValue.setText(mData[position].inputUnitContent)
                }
            }
            TWO_PAIR_INPUT_TYPE->
            {
                vh.tvTwoPairInputTitle.text=mData[position].twoPairInputTitle
                vh.tvTwoPairInputItem1.text=mData[position].twoPairInputItem1
                vh.tvTwoPairInputItem2.text=mData[position].twoPairInputItem2
                vh.tvTwoPairInputItem3.text=mData[position].twoPairInputItem3
                vh.etTwoPairInputValue1.addTextChangedListener(object:TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].twoPairInputValue1=p0.toString()
                    }
                })
                vh.etTwoPairInputValue2.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].twoPairInputValue2=p0.toString()
                    }
                })
            }
            THREE_DISPLAY_TYPE->
            {
                vh.tvThreeDisplayTitle.text=mData[position].threeDisplayTitle
                vh.tvThreeDisplayItem1.text=mData[position].threeDisplayContent1
                vh.tvThreeDisplayItem2.text=mData[position].threeDisplayContent2
                vh.tvThreeDisplayItem3.text=mData[position].threeDisplayContent3

                if (mData[position].threeDisplayContent2.equals("详情"))
                {
                    vh.tvThreeDisplayItem2.setBackgroundResource(R.drawable.btn_style2)
                    vh.tvThreeDisplayItem2.setOnClickListener(mData[position].threeDisplayListener)
                }
                if (mData[position].threeDisplayContent3.equals("详情"))
                {
                    vh.tvThreeDisplayItem3.setBackgroundResource(R.drawable.btn_style2)
                    vh.tvThreeDisplayItem3.setOnClickListener(mData[position].threeDisplayListener)
                }
            }

            TWO_COLUMN_DISPLAY_TYPE->
            {
                vh.tvTwoColumnDisplayTitle1.text=mData[position].twoColumnDisplayTitle1
                vh.tvTwoColumnDisplayContent1.text=mData[position].twoColumnDisplayContent1
                vh.tvTwoColumnDisplayTitle2.text=mData[position].twoColumnDisplayTitle2
                vh.tvTwoColumnDisplayContent2.text=mData[position].twoColumnDisplayContent2
            }
            INPUT_WITH_MULTI_UNIT_TYPE->
            {
                val context=vh.itemView.context
                vh.tvMultiInputUnitTitle.text=mData[position].inputMultiUnitTitle
                val adapter=ArrayAdapter(context,R.layout.item_dropdown,mData[position].inputMultiUnit)
                adapter.setDropDownViewResource(R.layout.item_dropdown)
                vh.etMultiInputContent.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputMultiContent=p0.toString()
                    }
                })
                vh.spMultiInputUnit.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        mData[position].inputMultiSelectUnit=adapter.getItem(p2).toString()
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
                if (mData[position].inputMultiContent!="")
                {
                    vh.etMultiInputContent.setText(mData[position].inputMultiContent)
                }
                if (mData[position].inputMultiSelectUnit!="")
                {
                    val position=adapter.getPosition(mData[position].inputMultiSelectUnit)
                    vh.spMultiInputUnit.setSelection(position)
                }
                vh.spMultiInputUnit.adapter=adapter
                vh.spMultiInputUnit.setSelection(1)
            }
            INPUT_RANGE_TYPE->
            {
                vh.tvRangeTitle.text=mData[position].inputRangeTitle
                vh.tvRangeUnit1.text=mData[position].inputRangeUnit
                vh.etRangeValue1.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                    override fun afterTextChanged(p0: Editable?) {
                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputRangeValue1=p0.toString()
                    }
                })
                vh.etRangeValue2.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                    override fun afterTextChanged(p0: Editable?) {
                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputRangeValue2=p0.toString()
                    }
                })
                if (mData[position].inputRangeValue1!="")
                {
                    vh.etRangeValue1.setText(mData[position].inputRangeValue1)
                }
                if (mData[position].inputRangeValue2!="")
                {
                    vh.etRangeValue2.setText(mData[position].inputRangeValue2)
                }

            }
            INPUT_WITH_TEXTAREA_TYPE->
            {
                vh.tvTextAreaTitle.text=mData[position].textAreaTitle
                vh.etTextAreaContent.addTextChangedListener(object:TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].textAreaContent=p0.toString()
                    }
                })
                if (mData[position].textAreaContent!="")
                {
                    vh.etTextAreaContent.setText(mData[position].textAreaContent)
                }

            }
            SHIFT_INPUT_TYPE->
            {
                vh.tvShiftInputTitle.text=mData[position].shiftInputTitle
                vh.tvShiftInputShow.setOnClickListener(mData[position].jumpListener)
                if (mData[position].necessary)
                {
                    val context=vh.itemView.context
                    val tvNecessary=TextView(context)
                    tvNecessary.text="*"
                    tvNecessary.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                    tvNecessary.setTextColor(context.resources.getColor(R.color.red,null))
                    (vh.itemView as ViewGroup).addView(tvNecessary,0)
                }
            }
            SELECT_DIALOG_TYPE->
            {
                vh.tvDialogSelectTitle.text=mData[position].selectTitle
                vh.etDialogSelectItem.text=mData[position].selectOption1Items[0]
                mData[position].selectContent=mData[position].selectOption1Items[0]
                vh.tvDialogSelectShow.setOnClickListener{
                    val selectDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,vh.itemView.context,mData[position].selectOption1Items,vh.mHandler).dialog
                    selectDialog.show()
                }
                if(mData[position].selectListener!=null)
                {
                    vh.tvDialogSelectShow.setOnClickListener(mData[position].selectListener)
                }
            }
            TWO_OPTIONS_SELECT_DIALOG_TYPE->
            {
                vh.tvDialogSelectTitle.text=mData[position].selectTitle
                //vh.etDialogSelectItem.text=mData[position].selectOption1Items[0]
                if (mData[position].selectContent!="")
                {
                    vh.etDialogSelectItem.text=mData[position].selectContent
                }
                vh.tvDialogSelectShow.setOnClickListener {
                    val selectDialog=CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,vh.tvDialogSelectTitle.context,vh.mHandler,mData[position].selectOption1Items,mData[position].selectOption2Items).multiDialog
                    selectDialog.show()
                }
                if(mData[position].selectListener!=null)
                {
                    vh.tvDialogSelectShow.setOnClickListener(mData[position].selectListener)
                }
            }
            THREE_OPTIONS_SELECT_DIALOG_TYPE->
            {
                vh.tvDialogSelectTitle.text=mData[position].selectTitle
                //vh.etDialogSelectItem.text=mData[position].selectOption1Items[0]
                vh.tvDialogSelectShow.setOnClickListener {
                    val selectDialog=CustomDialog(CustomDialog.Options.THREE_OPTIONS_SELECT_DIALOG,vh.tvDialogSelectTitle.context,vh.mHandler,mData[position].selectOption1Items,mData[position].selectOption2Items,mData[position].selectOption3Items).multiDialog
                    selectDialog.show()
                }
                if (mData[position].selectContent!="")
                {
                    vh.etDialogSelectItem.text=mData[position].selectContent
                }
                if(mData[position].selectListener!=null)
                {
                    vh.tvDialogSelectShow.setOnClickListener(mData[position].selectListener)
                }
            }
            HINT_TYPE->
            {
                if (mData[position].customFontColor==null)
                {
                    vh.tvHint.text=mData[position].hintContent
                }
                else
                {
                    val style=SpannableStringBuilder(mData[position].hintContent)
                    style.setSpan(ForegroundColorSpan(Color.parseColor(mData[position].customFontColor!!))
                        ,mData[position].startPosition!!
                        ,mData[position].endPosition!!
                        ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    vh.tvHint.text=style
                }
            }
            SUBMIT_TYPE->
            {
                vh.btnSubmit.text=mData[position].submitContent
                vh.btnSubmit.setOnClickListener{
                    val map=saveSimpleData()
                    val filesDir=it.context.filesDir.absolutePath
                    //downloadFile(filesDir,"工程量清册.xlsx","工程量清册模板","http://10.1.5.90:8012")
                    startSendMessage(map,"http://10.1.5.90:8012")
                }

            }
            EXPAND_TYPE->
            {
                vh.tvExpandTitle.text=mData[position].expandTitle
                vh.tvExpandContent.text=mData[position].expandContent
                vh.itemView.setOnClickListener{it->
                    if (expandPosition!=-1)
                    {
                        val valueAnimator=ValueAnimator.ofFloat(90f,0f)
                        valueAnimator.duration=200
                        val positionTemp=expandPosition
                        valueAnimator.addUpdateListener {
                            expandList[positionTemp].tvExpandButton.rotation=it.animatedValue.toString().toFloat()
                            if (expandList[positionTemp].tvExpandButton.rotation==90f)
                            {
                                expandList[positionTemp].tvExpandContent.visibility=View.GONE
                            }
                        }
                        valueAnimator.start()
                    }
                    if (expandList.indexOf(vh)!=expandPosition)
                    {
                        val valueAnimator2=ValueAnimator.ofFloat(0.toFloat(),90.toFloat())
                        valueAnimator2.duration=200
                        valueAnimator2.addUpdateListener {
                            vh.tvExpandButton.rotation=it.animatedValue.toString().toFloat()
                            if (vh.tvExpandButton.rotation==90f)
                            {
                                vh.tvExpandContent.visibility = View.VISIBLE
                            }
                        }
                        valueAnimator2.start()
                        vh.tvExpandButton.rotation = "90".toFloat()
                        expandPosition=expandList.indexOf(vh)
                    }
                    else
                    {
                        expandPosition=-1
                    }
                }
                expandList.add(vh)
            }
            EXPAND_LIST_TYPE->
            {
                vh.tvExpandListTitle.text=mData[position].expandListTitle
                vh.rvExpandListContent.adapter=mData[position].expandListAdapter
                vh.rvExpandListContent.layoutManager=LinearLayoutManager(vh.itemView.context)
                vh.itemView.setOnClickListener{it->
                    if (expandPosition!=-1)
                    {
                        val valueAnimator=ValueAnimator.ofFloat(90f,0f)
                        valueAnimator.duration=200
                        val positionTemp=expandPosition
                        valueAnimator.addUpdateListener {
                            expandList[positionTemp].tvExpandListButton.rotation=it.animatedValue.toString().toFloat()
                            if (expandList[positionTemp].tvExpandListButton.rotation==90f)
                            {
                                expandList[positionTemp].rvExpandListContent.visibility=View.GONE
                            }
                        }
                        valueAnimator.start()
                    }
                    if (expandList.indexOf(vh)!=expandPosition)
                    {
                        val valueAnimator2=ValueAnimator.ofFloat(0.toFloat(),90.toFloat())
                        valueAnimator2.duration=200
                        valueAnimator2.addUpdateListener {
                            vh.tvExpandListButton.rotation=it.animatedValue.toString().toFloat()
                            if (vh.tvExpandListButton.rotation==90f)
                            {
                                vh.rvExpandListContent.visibility = View.VISIBLE
                            }
                        }
                        valueAnimator2.start()
                        vh.tvExpandListButton.rotation = "90".toFloat()
                        expandPosition=expandList.indexOf(vh)
                    }
                    else
                    {
                        expandPosition=-1
                    }
                }
                expandList.add(vh)
            }
            SINGLE_DISPLAY_TYPE->
            {
                vh.tvSingleDisplayTitle.text=mData[position].singleDisplayTitle
                vh.tvSingleDisplayContent.text=mData[position].singleDisplayContent

            }
        }
        mData[position].viewHolderInitialize=true
        //VHList.add(vh)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VH {
        var root:View?=null
        when(viewType)
        {
            TITLE_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_title,viewGroup,false)
                return VH(root,viewType)
            }
            MULTI_BUTTON_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_multi,viewGroup,false)
                return VH(root,viewType)
            }
            MULTI_RADIO_BUTTON_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_multi,viewGroup,false)
                return VH(root,viewType)
            }
            MULTI_CHECKBOX_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_multi,viewGroup,false)
                return VH(root,viewType)
            }
            MULTI_HYBRID_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_multi,viewGroup,false)
                return VH(root,viewType)
            }
            SINGLE_INPUT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_single_input,viewGroup,false)
                return VH(root,viewType)
            }
            TWO_PAIR_INPUT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_two_pair_input,viewGroup,false)
                return VH(root,viewType)
            }

            THREE_DISPLAY_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_three_display,viewGroup,false)
                return VH(root,viewType)
            }
            TWO_COLUMN_DISPLAY_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_two_column_display,viewGroup,false)
                return VH(root,viewType)
            }
            EXPAND_LIST_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_expand_list,viewGroup,false)
                return VH(root, viewType)
            }
            EXPAND_LIST_DISPLAY_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_single_display_expand,viewGroup,false)
                return VH(root, viewType)
            }
            INPUT_WITH_UNIT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_input_with_unit,viewGroup,false)
                return VH(root, viewType)
            }
            INPUT_WITH_MULTI_UNIT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_input_with_multi_unit,viewGroup,false)
                return VH(root, viewType)
            }
            INPUT_RANGE_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_input_range,viewGroup,false)
                return VH(root, viewType)
            }
            INPUT_WITH_TEXTAREA_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_input_with_textarea,viewGroup,false)
                return VH(root, viewType)
            }
            SHIFT_INPUT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_shift_input,viewGroup,false)
                return VH(root, viewType)
            }
            SELECT_DIALOG_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_dialog_select,viewGroup,false)
                return VH(root, viewType)
            }
            TWO_OPTIONS_SELECT_DIALOG_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_dialog_select,viewGroup,false)
                return VH(root, viewType)
            }
            THREE_OPTIONS_SELECT_DIALOG_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_dialog_select,viewGroup,false)
                return VH(root, viewType)
            }
            HINT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hint,viewGroup,false)
                return VH(root, viewType)
            }
            SUBMIT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_confirm,viewGroup,false)
                return VH(root, viewType)
            }
            EXPAND_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_expand,viewGroup,false)
                return VH(root, viewType)
            }
            SINGLE_DISPLAY_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_single_display,viewGroup,false)
                return VH(root, viewType)
            }
        }
        return VH(root!!,TITLE_TYPE)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun saveSimpleData():HashMap<String,String>
    {
        val map=HashMap<String,String>(mData.size)
        for (i in 0 until mData.size-1)
        {
            val multiStyleItem=mData[i]
            val viewHolder=VHList[i]
            if (multiStyleItem.key=="")
                continue
            when(viewHolder.itemViewType)
            {
                TITLE_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.title1!="")
                    {
                        multiStyleItem.title1
                    }
                    else
                    {
                        ""
                    }
                }
                SINGLE_INPUT_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.inputSingleContent!="")
                    {
                        multiStyleItem.inputSingleContent
                    }
                    else
                        ""
                }
                INPUT_WITH_UNIT_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.inputUnitContent!="")
                        multiStyleItem.inputUnitContent
                    else
                        ""
                }
                INPUT_WITH_MULTI_UNIT_TYPE->
                {
                    map[multiStyleItem.key]=if (multiStyleItem.inputMultiContent!="")
                        multiStyleItem.inputMultiContent
                    else
                        ""
                }
                INPUT_RANGE_TYPE->
                {
                    val keys=multiStyleItem.key.split(" ")

                    map[keys[0]]=if (multiStyleItem.inputRangeValue1!="")
                        multiStyleItem.inputRangeValue1
                    else
                        ""

                    map[keys[1]]=if (multiStyleItem.inputRangeValue2!="")
                        multiStyleItem.inputRangeValue2
                    else
                        ""
                }
                INPUT_WITH_TEXTAREA_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.textAreaContent!="")
                        multiStyleItem.textAreaContent
                    else
                        ""
                }
                TWO_PAIR_INPUT_TYPE->
                {
                    val keys=multiStyleItem.key.split(" ")

                    map[keys[0]]=if (multiStyleItem.twoPairInputValue1!="")
                        multiStyleItem.twoPairInputValue1
                    else
                        ""
                    map[keys[1]]=if (multiStyleItem.twoPairInputValue2!="")
                        multiStyleItem.twoPairInputValue2
                    else
                        ""
                }
                SHIFT_INPUT_TYPE->
                {

                }
                SELECT_DIALOG_TYPE, TWO_OPTIONS_SELECT_DIALOG_TYPE, THREE_OPTIONS_SELECT_DIALOG_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.selectContent!="")
                        multiStyleItem.selectContent
                    else
                        ""
                }
                MULTI_RADIO_BUTTON_TYPE->
                {
                    map[multiStyleItem.key]=if (multiStyleItem.radioButtonValue!="")
                    {
                        multiStyleItem.radioButtonValue
                    }
                    else
                        ""
                }
                MULTI_CHECKBOX_TYPE->
                {
                    var checkNum=0
                    for (j in 0 until multiStyleItem.checkboxValueList.size-1)
                    {
                        if (multiStyleItem.checkboxValueList[j])
                        {
                            checkNum++
                            if (checkNum>=2)
                            {
                                map[multiStyleItem.key]="-1"
                            }
                            else
                            {
                                map[multiStyleItem.key]=(1-j).toString()
                            }
                        }
                    }
                }
            }//end when
        }//end for
        return map
    }
}