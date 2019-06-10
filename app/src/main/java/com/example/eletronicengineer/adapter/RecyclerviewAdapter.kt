package com.example.eletronicengineer.adapter

import android.animation.ValueAnimator
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
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import com.example.eletronicengineer.R
import com.example.eletronicengineer.custom.CustomDialog
import com.electric.engineering.model.MultiStyleItem
import kotlinx.android.synthetic.main.item_confirm.view.*
import kotlinx.android.synthetic.main.item_expand.view.*
import kotlinx.android.synthetic.main.item_hint.view.*
import kotlinx.android.synthetic.main.item_input_with_multi_unit.view.*
import kotlinx.android.synthetic.main.item_input_with_textarea.view.*

class RecyclerviewAdapter: RecyclerView.Adapter<RecyclerviewAdapter.VH> {
    companion object {
        const val TITLE_TYPE:Int=1
        const val SELECT_DIALOG_TYPE:Int=2
        const val SINGLE_INPUT_TYPE:Int=3
        const val INPUT_RANGE_TYPE:Int=4
        const val INPUT_WITH_UNIT_TYPE=5
        const val INPUT_WITH_MULTI_UNIT_TYPE=16

        const val MULTI_BUTTON_TYPE:Int=6
        const val MULTI_RADIO_BUTTON_TYPE:Int=7
        const val MULTI_CHECKBOX_TYPE=8
        const val MULTI_HYBRID_TYPE=14
        const val TWO_OPTIONS_SELECT_DIALOG_TYPE=9
        const val THREE_OPTIONS_SELECT_DIALOG_TYPE=10

        const val INPUT_WITH_TEXTAREA_TYPE=11
        const val HINT_TYPE=12
        const val SUBMIT_TYPE=13
        const val EXPAND_TYPE=15

        const val MESSAGE_SELECT_OK:Int=100
    }
    var mData:List<MultiStyleItem>
    var expandList:MutableList<VH> =ArrayList()
    var expandPosition:Int=-1
    val VHList:MutableList<VH> =ArrayList()
     inner class VH:RecyclerView.ViewHolder
    {
        lateinit var tvTitle1:TextView

        lateinit var multiTitle:TextView
        lateinit var llContainer: LinearLayout
        var btnList:MutableList<Button> = mutableListOf()

        lateinit var tvSingleInputTitle:TextView

        lateinit var tvRangeTitle:TextView
        lateinit var tvRangeUnit1:TextView
        lateinit var tvRangeUnit2:TextView

        lateinit var tvInputUnit: TextView
        lateinit var tvInputUnitTitle:TextView

        lateinit var spMultiInputUnit:Spinner
        lateinit var tvMultiInputUnitTitle:TextView


        lateinit var tvDialogSelectTitle:TextView
        lateinit var tvDialogSelectItem:TextView
        lateinit var tvDialogSelectShow:TextView

        lateinit var tvTextAreaTitle:TextView
        lateinit var etTextAreaContent:TextView

        lateinit var tvHint:TextView

        lateinit var btnSubmit:Button

        lateinit var tvExpandTitle:TextView
        lateinit var tvExpandContent:TextView
        lateinit var tvExpandButton:TextView
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                MESSAGE_SELECT_OK->{
                    val selectContent=it.data.getString("selectContent")
                    tvDialogSelectItem.text=selectContent
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
                }
                INPUT_RANGE_TYPE->
                {
                    tvRangeTitle=v.tv_range_title
                    tvRangeUnit1=v.tv_range_unit1
                    tvRangeUnit2=v.tv_range_unit2
                }
                INPUT_WITH_UNIT_TYPE->
                {
                    tvInputUnitTitle=v.tv_input_unit_title
                    tvInputUnit=v.tv_input_unit
                }
                INPUT_WITH_MULTI_UNIT_TYPE->
                {
                    spMultiInputUnit=v.sp_input_multi_unit_unit
                    tvMultiInputUnitTitle=v.tv_input_multi_unit_title
                }
                SELECT_DIALOG_TYPE->
                {
                    tvDialogSelectTitle=v.tv_dialog_select_title
                    tvDialogSelectItem=v.tv_dialog_select_item
                    tvDialogSelectShow=v.tv_dialog_select_show
                }
                TWO_OPTIONS_SELECT_DIALOG_TYPE->
                {
                    tvDialogSelectTitle=v.tv_dialog_select_title
                    tvDialogSelectItem=v.tv_dialog_select_item
                    tvDialogSelectShow=v.tv_dialog_select_show
                }
                THREE_OPTIONS_SELECT_DIALOG_TYPE->
                {
                    tvDialogSelectTitle=v.tv_dialog_select_title
                    tvDialogSelectItem=v.tv_dialog_select_item
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
            }
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
            MultiStyleItem.Options.MULTI_RADIO_BUTTON->return MULTI_RADIO_BUTTON_TYPE
            MultiStyleItem.Options.MULTI_CHECKBOX->return MULTI_CHECKBOX_TYPE
            MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG->return TWO_OPTIONS_SELECT_DIALOG_TYPE
            MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG->return THREE_OPTIONS_SELECT_DIALOG_TYPE
            MultiStyleItem.Options.INPUT_WITH_TEXTAREA->return INPUT_WITH_TEXTAREA_TYPE
            MultiStyleItem.Options.HINT->return HINT_TYPE
            MultiStyleItem.Options.SUBMIT->return SUBMIT_TYPE
            MultiStyleItem.Options.MULTI_HYBRID-> return MULTI_HYBRID_TYPE
            MultiStyleItem.Options.EXPAND->return EXPAND_TYPE
        }
    }
    override fun onBindViewHolder(vh: VH, position: Int) {
        VHList.add(vh)
        when(getItemViewType(position))
        {
            TITLE_TYPE->
            {
                //vh.tvTitle1.setText(mData[position].getTitle1())
                vh.tvTitle1.text=mData[position].title1
            }
            MULTI_BUTTON_TYPE->
            {
                vh.multiTitle.text=mData[position].buttonTitle
                if (vh.llContainer.childCount>0)
                    return
                for (name:String in mData[position].buttonName)
                {
                    val btnItem= Button(vh.itemView.context)
                    btnItem.text=name
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
                if (vh.llContainer.childCount>0)
                    return
                vh.multiTitle.text=mData[position].radioButtonTitle
                val radioGroup=RadioGroup(vh.itemView.context)
                radioGroup.orientation=LinearLayout.HORIZONTAL
                for (name:String in mData[position].radioButtonName)
                {
                    val radioItem=RadioButton(context)
                    radioItem.text=name
                    radioItem.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                    radioGroup.addView(radioItem)
                    val params=radioItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                    radioItem.layoutParams=params
                }
                vh.llContainer.addView(radioGroup)
            }
            MULTI_CHECKBOX_TYPE->
            {
                val context=vh.itemView.context
                if (vh.llContainer.childCount>0)
                    return
                vh.multiTitle.text=mData[position].checkboxTitle
                for (name in mData[position].checkboxNameList)
                {
                    val checkboxItem=CheckBox(context)
                    checkboxItem.text=name
                    checkboxItem.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                    vh.llContainer.addView(checkboxItem)
                    val params=checkboxItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                    checkboxItem.layoutParams=params
                }
            }
            MULTI_HYBRID_TYPE->
            {
                val context=vh.itemView.context
                if (vh.llContainer.childCount>1)
                    return
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
                        val btnItem= Button(vh.itemView.context)
                        btnItem.text=name
                        btnItem.setBackgroundResource(R.drawable.btn_style1)
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
            }
            INPUT_WITH_UNIT_TYPE->
            {
                vh.tvInputUnitTitle.text=mData[position].inputUnitTitle
                vh.tvInputUnit.text=mData[position].inputUnit
            }
            INPUT_WITH_MULTI_UNIT_TYPE->
            {
                val context=vh.itemView.context
                vh.tvMultiInputUnitTitle.text=mData[position].inputMultiUnitTitle
                val adapter=ArrayAdapter(context,R.layout.item_dropdown,mData[position].inputMultiUnit)
                adapter.setDropDownViewResource(R.layout.item_dropdown)
                vh.spMultiInputUnit.adapter=adapter
            }
            INPUT_RANGE_TYPE->
            {
                vh.tvRangeTitle.text=mData[position].inputRangeTitle
                vh.tvRangeUnit1.text=mData[position].inputRangeUnit
            }
            INPUT_WITH_TEXTAREA_TYPE->
            {
                vh.tvTextAreaTitle.text=mData[position].textAreaTitle
            }
            SELECT_DIALOG_TYPE->
            {
                vh.tvDialogSelectTitle.text=mData[position].selectTitle
                vh.tvDialogSelectItem.text=mData[position].selectOption1Items[0]
                vh.tvDialogSelectShow.setOnClickListener {
                    val selectDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,vh.tvDialogSelectTitle.context,mData[position].selectOption1Items,vh.mHandler).dialog
                    selectDialog.show()
                }
            }
            TWO_OPTIONS_SELECT_DIALOG_TYPE->
            {
                vh.tvDialogSelectTitle.text=mData[position].selectTitle
                vh.tvDialogSelectItem.text=mData[position].selectOption1Items[0]
                vh.tvDialogSelectShow.setOnClickListener {
                    val selectDialog=CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,vh.tvDialogSelectTitle.context,vh.mHandler,mData[position].selectOption1Items,mData[position].selectOption2Items).multiDialog
                    selectDialog.show()
                }
            }
            THREE_OPTIONS_SELECT_DIALOG_TYPE->
            {
                vh.tvDialogSelectTitle.text=mData[position].selectTitle
                vh.tvDialogSelectItem.text=mData[position].selectOption1Items[0]
                vh.tvDialogSelectShow.setOnClickListener {
                    val selectDialog=CustomDialog(CustomDialog.Options.THREE_OPTIONS_SELECT_DIALOG,vh.tvDialogSelectTitle.context,vh.mHandler,mData[position].selectOption1Items,mData[position].selectOption2Items,mData[position].selectOption3Items).multiDialog
                    selectDialog.show()
                }
            }
            HINT_TYPE->
            {
                vh.tvHint.text=mData[position].hintContent
            }
            SUBMIT_TYPE->
            {
                vh.btnSubmit.text=mData[position].submitContent
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

        }
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
        }
        return VH(root!!,TITLE_TYPE)
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}