package com.example.eletronicengineer.adapter

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
import kotlinx.android.synthetic.main.item_hint.view.*
import kotlinx.android.synthetic.main.item_input_with_textarea.view.*

class RecyclerviewAdapter: RecyclerView.Adapter<RecyclerviewAdapter.VH> {
    companion object {
        const val TITLE_TYPE:Int=1
        const val SELECT_DIALOG_TYPE:Int=2
        const val SINGLE_INPUT_TYPE:Int=3
        const val SELECT_RANGE_TYPE:Int=4
        const val INPUT_WITH_UNIT_TYPE=5

        const val MULTI_BUTTON_TYPE:Int=6
        const val MULTI_RADIO_BUTTON_TYPE:Int=7
        const val MULTI_CHECKBOX_TYPE=8
        const val MULTI_HYBRID=14
        const val TWO_OPTIONS_SELECT_DIALOG_TYPE=9
        const val THREE_OPTIONS_SELECT_DIALOG_TYPE=10

        const val INPUT_WITH_TEXTAREA=11
        const val HINT=12
        const val SUBMIT=13

        const val MESSAGE_SELECT_OK:Int=100
    }
    var mData:List<MultiStyleItem>
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

        lateinit var tvDialogSelectTitle:TextView
        lateinit var tvDialogSelectItem:TextView
        lateinit var tvDialogSelectShow:TextView

        lateinit var tvTextAreaTitle:TextView
        lateinit var etTextAreaContent:TextView

        lateinit var tvHint:TextView

        lateinit var btnSubmit:Button
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
                MULTI_HYBRID->
                {
                    llContainer=v.cl_multi_container
                    multiTitle=v.tv_multi_title
                }
                SINGLE_INPUT_TYPE->
                {
                    tvSingleInputTitle=v.tv_input_title
                }
                SELECT_RANGE_TYPE->
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
                INPUT_WITH_TEXTAREA->
                {
                    tvTextAreaTitle=v.tv_textarea_title
                    etTextAreaContent=v.et_textarea_content
                }
                HINT->
                {
                    tvHint=v.tv_hint_content
                }
                SUBMIT->
                {
                    btnSubmit=v.btn_confirm_submit
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
            MultiStyleItem.Options.SELECT_DIALOG->return SELECT_DIALOG_TYPE
            MultiStyleItem.Options.SELECT_RANGE->return SELECT_RANGE_TYPE
            MultiStyleItem.Options.MULTI_RADIO_BUTTON->return MULTI_RADIO_BUTTON_TYPE
            MultiStyleItem.Options.MULTI_CHECKBOX->return MULTI_CHECKBOX_TYPE
            MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG->return TWO_OPTIONS_SELECT_DIALOG_TYPE
            MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG->return THREE_OPTIONS_SELECT_DIALOG_TYPE
            MultiStyleItem.Options.INPUT_WITH_TEXTAREA->return INPUT_WITH_TEXTAREA
            MultiStyleItem.Options.HINT->return HINT
            MultiStyleItem.Options.SUBMIT->return SUBMIT
            MultiStyleItem.Options.MULTI_HYBRID-> return MULTI_HYBRID
        }
    }
    override fun onBindViewHolder(vh: VH, position: Int) {
        val viewType=getItemViewType(position)
        when(viewType)
        {
            TITLE_TYPE->
            {
                vh.tvTitle1.text=mData[position].title1
            }
            MULTI_BUTTON_TYPE->
            {
                vh.multiTitle.text=mData[position].buttonTitle
                for (name:String in mData[position].buttonName)
                {
                    val btnItem= Button(vh.itemView.context)
                    btnItem.text=name
                    btnItem.setBackgroundResource(R.drawable.btn_style1)
                    vh.llContainer.addView(btnItem)
                    vh.multiTitle.text=mData[position].buttonTitle
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
                    radioGroup.addView(radioItem)
                    val params=radioItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=context.resources.getDimension(R.dimen.general_20).toInt()
                    radioItem.layoutParams=params
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
                    checkboxItem.textSize=context.resources.getDimension(R.dimen.font_tv_hint_15)
                    vh.llContainer.addView(checkboxItem)
                    val params=checkboxItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                    checkboxItem.layoutParams=params
                }
            }
            MULTI_HYBRID->
            {
                val context=vh.itemView.context
                vh.multiTitle.text=mData[position].hybridTitle
                for (checkboxName in mData[position].hybridCheckBoxName)
                {
                    val checkboxItem=CheckBox(context)
                    checkboxItem.text=checkboxName
                    checkboxItem.textSize=context.resources.getDimension(R.dimen.font_tv_hint_15)
                    vh.llContainer.addView(checkboxItem)
                    val params=checkboxItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                    checkboxItem.layoutParams=params
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
            INPUT_WITH_TEXTAREA->
            {
                vh.tvTextAreaTitle.text=mData[position].textAreaTitle
            }
            HINT->
            {
                vh.tvHint.text=mData[position].hintContent
            }
            SUBMIT->
            {
                vh.btnSubmit.text=mData[position].submitContent
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
            MULTI_HYBRID->
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
            SELECT_RANGE_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_input_range,viewGroup,false)
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
            HINT->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hint,viewGroup,false)
                return VH(root, viewType)
            }
            SUBMIT->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_confirm,viewGroup,false)
                return VH(root, viewType)
            }
        }
        return VH(root!!,TITLE_TYPE)
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}